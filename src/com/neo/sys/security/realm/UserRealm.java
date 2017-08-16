package com.neo.sys.security.realm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.neo.entity.Sys_office;
//import com.neo.service.sys.OfficeService;
import com.neo.sys.service.service.MenuService;
import com.neo.sys.utils.ConfigValues;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ConcurrentAccessException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.neo.entity.Sys_user;
import com.neo.service.sys.UserService;
import com.neo.sys.emue.SessionAttribute;
import com.neo.sys.entity.bean.Admin;
import com.neo.sys.entity.bean.Menu;
import com.neo.sys.entity.bean.MenuRole;
import com.neo.sys.exception.NormalRuntimeException;
import com.neo.sys.service.service.MenuRoleService;
import com.neo.sys.tlds.ElUtil;
import com.neo.sys.utils.AdminUtil;
import com.neo.sys.utils.CodeUtils;
import com.neo.sys.utils.LoginFailManagerUtil;

/**
 * 登录身份认证，同时提供用户的身份信息。
 *
 */
public class UserRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;
	
	@Autowired
	private MenuRoleService menuRoleService;

	@Autowired
	private MenuService menuService;

//	@Autowired
//	private OfficeService officeService;
//	/* (non-Javadoc)
//	 * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
//	 * 该方法用于权限认证
//	 * //角色与权限。角色和权限是动态获取的，这里将使用编程方式进行验证。
//				//权限有：a:全部，r:查询，w:增删改，c:审核
//				//角色=岗位
//	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		String currentUsername = (String)super.getAvailablePrincipal(principal);
		if(StringUtils.isBlank(currentUsername)){
			return null;
		}
		Session session = SecurityUtils.getSubject().getSession();
		Sys_user user = (Sys_user) session.getAttribute(SessionAttribute.CURRENT_USER);
		if(user == null){
			throw new NormalRuntimeException("未登录");
		}
		List<MenuRole> listMenuRoles=null;
		if(user.getId() != null){
//			listMenuRoles = menuRoleService.getMenuRoleByUserId(user.getId());
//			if(listMenuRoles == null || listMenuRoles.size() == 0){
//				throw new NormalRuntimeException("用户没有任何权限");
//			}
		}
		SimpleAuthorizationInfo simpleAuthor = new SimpleAuthorizationInfo();
		String permission;
		char c[];
		if(listMenuRoles!=null){
			for(MenuRole m:listMenuRoles){
				permission = m.getPermission();
				if(StringUtils.isBlank(permission)){
					continue;
				}
				c = permission.toCharArray();
				for(char cc:c){
					simpleAuthor.addStringPermission("user:"+cc);
				}
				simpleAuthor.addRole("user");
			}
		}else{
			simpleAuthor.addRole("admin");
			Map<String, String> codes = CodeUtils.getCodes("PERMISSION");
			if(codes != null){
				for(String str:codes.keySet()){
					simpleAuthor.addStringPermission("admin:"+str);
				}
			}
		}
		return simpleAuthor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.
	 * apache.shiro.authc.AuthenticationToken) 登录身份认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authentic) throws AuthenticationException {
		UsernamePasswordToken upt = (UsernamePasswordToken) authentic;
		String userName = upt.getUsername();
		if (StringUtils.isBlank(userName)) {
			throw new UnknownAccountException("用户名不能为空");
		}
		if (LoginFailManagerUtil.isLocked(userName)) {
			throw new ConcurrentAccessException("您今日登录次数已超过三次，您的账号将被锁定不少于24小时");
		}
		char c[] = upt.getPassword();
		if (c == null || c.length == 0) {
			throw new ConcurrentAccessException("密码不能为空");
		}
		String password = Md5Hash.toString(new String(c).getBytes());
		Admin admin = null;
		try {
			admin = AdminUtil.checkUser(userName, password, "");
		} catch (Exception e) {
			throw new UnknownAccountException("系统出现内部错误");
		}
		Sys_user user = null;
		if (admin == null) {
			try {
				user = userService.getUserByOrder(userName, password);
			} catch (Exception e) {
				e.printStackTrace();
				throw new UnknownAccountException("服务器繁忙，请稍后再试");
			}
			if (user == null) {
				LoginFailManagerUtil.addFail(userName, new Date());
				throw new ConcurrentAccessException("用户名或密码错误");
			}
			if (user.getOfficeId() == null || user.getEnabled() == null || user.getOfficeId() != 1) {
				throw new ConcurrentAccessException("用户的机构不存在或已禁用");
			}
		} else {
			user = AdminUtil.adminToUser(admin);
		}
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		session.setAttribute(SessionAttribute.LOGIN_TIME, new Date());
		user.setPassword(null);
//		List<Menu> listMenu = menuService.getgetUserMenu(user,null);
//		if(listMenu == null || listMenu.isEmpty()){
//			throw new ConcurrentAccessException("没有可分配的功能，禁止登录");
//		}else{
//			boolean b = false;
//			for(int i=0;i<listMenu.size();i++){
//				if(listMenu.get(i).getParentId() == -1 || listMenu.get(i).getParentId() == null){
//					b = true;
//					break;
//				}
//			}
//			if(!b){
//				throw new ConcurrentAccessException("没有可分配的功能，禁止登录");
//			}
// 
//			listMenu.stream().forEach(m->m.setMappingIconUrl(StringUtils.isBlank(m.getIconUrl())?ConfigValues.getCommonResourceMapper()+"/img/ico/menu-ico/default_li_icon.png":(m.getIconUrl().startsWith(ConfigValues.getCommonResourceMapper())?"": ConfigValues.getDiyFolderMapper())+m.getIconUrl()));
//		}
		session.setAttribute(SessionAttribute.CURRENT_USER, user);
		//session.setAttribute(SessionAttribute.CURRENT_USER_MENU, listMenu);
		//提前将用户的所有上级机构和所有下级机构都查出来，以免用户操作频繁访问数据库。
//		if(user.getId()!=null){
//			if(user.getOfficeId()!=null){
//				List<Sys_office> parentOffices = officeService.getOfficeParent(user.getOfficeId());
//				List<Office> childrenOffices = officeService.getOfficeChildren(user.getOfficeId());
//				session.setAttribute(SessionAttribute.USER_CURRENT_OFFICE,user.getOffice());
//				session.setAttribute(SessionAttribute.USER_PARENT_OFFICE,parentOffices);
//				session.setAttribute(SessionAttribute.USER_CHILD_OFFICE,childrenOffices);
//			}
//		}
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userName, password,
				this.getName());
		
		return simpleAuthenticationInfo;
	}

}
