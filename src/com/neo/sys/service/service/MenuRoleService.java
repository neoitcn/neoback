package com.neo.sys.service.service;

import com.neo.entity.Sys_user;
import com.neo.sys.dao.MenuRoleDao;
import com.neo.sys.emue.SessionAttribute;
import com.neo.sys.entity.bean.MenuRole;
import com.neo.sys.service.BaseService;
import com.neo.sys.utils.CodeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
public class MenuRoleService extends BaseService<MenuRole>{

	@Autowired
	private MenuRoleDao menuRoleDao;

	@Autowired
	private MenuService menuService;

	@Transactional(readOnly = true)
	public List<MenuRole> getMenuRoleByUserId(String userId){
		return menuRoleDao.getMenuRoleByUserId(userId);
	}

	@Transactional(readOnly = false)
	public int addMenuRoleLink(Integer roleId, Integer menuId) {
		return menuRoleDao.addMenuRoleLink(roleId, menuId);
	}

	@Transactional(readOnly = false)
	public int deleteMenuRoleLinkByMenu(Integer menuId) {
		return menuRoleDao.deleteMenuRoleLinkByMenu(menuId);
	}

	@Transactional(readOnly = false)
	public int deleteMenuRoleLinkByRole(Integer roleId) {
		return menuRoleDao.deleteMenuRoleLinkByRole(roleId);
	}

	@Transactional(readOnly = false)
	public int addUserRoleLink(Integer roleId, String userId) {
		return menuRoleDao.addUserRoleLink(roleId, userId);
	}

	@Transactional(readOnly = false)
	public int deleteUserLinkByUser(String userId) {
		return menuRoleDao.deleteUserLinkByUser(userId);
	}
	
	@Transactional(readOnly = false)
	public int deleteUserLinkByDetail(String userId,Integer roleId){
		return menuRoleDao.deleteUserLinkByDetail(userId, roleId);
	}

	@Transactional(readOnly = false)
	public void saveMenuRole(String menuIds, String []permissions, MenuRole mr, HttpServletRequest request) throws Exception{
//		if(permissions!=null){
//			String ps = "";
//			for(String s:permissions){
//				if("a".equals(s)){
//					Map<String, String> codes = CodeUtils.getCodes("PERMISSION");
//					if(codes != null){
//						ps = "";
//						for(String str:codes.keySet()){
//							ps+=str;
//						}
//						break;
//					}
//				}
//				ps+=s;
//			}
//			mr.setPermission(ps);
//		}
//		if(mr.getId()==null){
//			insert(mr);
//		}else{
//			update(mr);
//		}
//		if(menuIds != null){
//			deleteMenuRoleLinkByRole(mr.getId());
//			if(StringUtils.isNotBlank(menuIds)){
//				String menuIdAr[] = menuIds.split(",");
//				for(String id:menuIdAr){
//					if(id!=null){
//						try{
//							addMenuRoleLink(mr.getId(),Integer.parseInt(id));
//						}catch(Exception e){
//							e.printStackTrace();
//							System.err.println("角色功能分配失败");
//						}
//					}
//				}
//			}
//		}
//		//此处更新完成后需要更新Session中的menu。系统不更新所有在线用户的session，只更新当前用户的session
//		Sys_user user = (Sys_user) request.getSession().getAttribute(SessionAttribute.CURRENT_USER);
//		if(user.getId() == null){
//			request.getSession().setAttribute(SessionAttribute.CURRENT_USER_MENU,menuService.getAllMenuList(1));
//		}else{
//			List<MenuRole> listMenuRole = getMenuRoleByUserId(user.getId());
//			if(listMenuRole != null){
//				for(MenuRole mre:listMenuRole){
//					//只有当前用户所具备的角色中包含被修改的角色才会更新session中的菜单。
//					if(mre.getId() == mr.getId()){
//						request.getSession().setAttribute(SessionAttribute.CURRENT_USER_MENU, menuService.getUserMenu(user,listMenuRole));
//						break;
//					}
//				}
//			}
//		}
	}

	@Transactional(readOnly = false)
	public void deleteMenuRoleById(Integer id) throws Exception{
		deleteById(id);
		deleteMenuRoleLinkByRole(id);
	}
}
