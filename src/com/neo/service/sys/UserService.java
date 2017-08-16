package com.neo.service.sys;

import java.util.List;
import java.util.Map;

import com.neo.sys.emue.SessionAttribute;
import com.neo.sys.utils.AccessUtil;
import com.neo.sys.utils.SequenceUtil;
import com.neo.entity.Sys_office;
import com.neo.entity.Sys_user;

import org.apache.axis.session.Session;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neo.mapper.sys.UserDao;
import com.neo.sys.service.BaseService;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService extends BaseService<Sys_user>{

	@Autowired
	private UserDao userMapper;

	@Transactional(readOnly = true)
	public Sys_user getUserByOrder(String loginName, String password){
		return userMapper.getUserByOrder(loginName, password);
	}

	/**
	 * t
	 * @param account 
	 * @param loadOffice 'true'表示加载user的所属机构
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public Sys_user getUserByAccount(String account, String loadOffice){
		return userMapper.getUserByAccount(account, loadOffice);
	}

	@Transactional(readOnly = false)
	public int updateUserPassword(String id, String oldPassword, String password) {
		return userMapper.updateUserPassword(id, oldPassword, password);
	}

	/**
	 * 检查以下这两个信息是否重复
	 * 
	 * @param email
	 * @param loginName
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Sys_user> checkUser(String email, String loginName, String mobile) {
		return userMapper.checkUser(email, loginName, mobile);
	}

//	@Transactional(readOnly = false)
//	public void saveUser(Sys_user user, HttpServletRequest request) throws Exception{
//		Sys_user u = (Sys_user)request.getSession().getAttribute(SessionAttribute.CURRENT_USER);
//		Integer uOfficeId;
//		if(u.getId() != null){
//			//如果当前用户修改的不是自己的账户，则需要判断是否有权限去修改这个用户。
//			List<Office> listOffice = AccessUtil.helpCreateCurrentDataChildOffice();
//			if(!u.getId().equals(user.getId())){
//				if(StringUtils.isNotBlank(user.getId())){
//					//此处判断正在被修改的用户和当前操作修改的用户是否是上下级关系，如果操作的用户级别大于被修改的用户，则可以修改。
//					User tempU = getResultById(user.getId());
//					if(tempU == null){
//						throw new Exception("用户未找到");
//					}
//					uOfficeId = tempU.getOfficeId();
//					boolean b = false;
//					for(Office o:listOffice){
//						if(o.getId().equals(uOfficeId)){
//							b = true;
//							break;
//						}
//					}
//					if(!b){
//						throw new Exception("当前您只能为自己的下属机构添加用户");
//					}
//				}
//				//然后再判断被修改的用户被添加的哪一个机构了。如果这个机构级别不当前操作用户的级别小，则可以修改，否则不允许修改或添加。
//				uOfficeId = user.getOfficeId();
//				AccessUtil.accessSuccess(uOfficeId!=null,"请为用户选择一个机构");
//				boolean b = false;
//				for(Office o:listOffice){
//					if(o.getId().equals(uOfficeId)){
//						b = true;
//						break;
//					}
//				}
//				if(!b){
//					throw new Exception("当前您只能为自己的下属机构添加用户");
//				}
//			}
//		}
//		if (StringUtils.isBlank(user.getId())) {
//			List<Sys_user> checkUserList = checkUser(user.getEmail(), user.getName(),
//					user.getMobile());
//			if (checkUserList != null && checkUserList.size() > 0) {
//				if (user.getEmail().equalsIgnoreCase((checkUserList.get(0).getEmail()))) {
//					throw new Exception("邮箱号已被占用");
//				} else if (user.getMobile().equalsIgnoreCase((checkUserList.get(0).getMobile()))) {
//					throw new Exception("手机号已被占用");
//				} else {
//					throw new Exception("用户名已被占用");
//				}
//			}
//			String no = SequenceUtil.getSequenceYearNumAndIncBy("USER", 1);
//			//user.setNo(no);
//			insert(user);
//		} else {
//			update(user);
//			//更新session中的用户
//			if(u.getId() == null && user.getId() == null){
//				if(u.getLoginName().equals(user.getLoginName())){
//					request.getSession().setAttribute(SessionAttribute.CURRENT_USER,user);
//				}
//			}else if(user.getId().equals(u.getId())){
//				request.getSession().setAttribute(SessionAttribute.CURRENT_USER,user);
//				user.setOffice((Office) request.getSession().getAttribute(SessionAttribute.USER_CURRENT_OFFICE));
//			}
//		}
//	}
//
	@Transactional(readOnly = false)
	public void deleteUserById(String id,HttpServletRequest request) throws Exception{
		Sys_user u= (Sys_user)request.getSession().getAttribute(SessionAttribute.CURRENT_USER);
		if(u.getId() != null){
			//用户只能去删除自己下属级别的用户
			if(!u.getId().equals(id)){
				Integer uOfficeId;
				if(id != null){
					//此处判断正在被修改的用户和当前操作修改的用户是否是上下级关系，如果操作的用户级别大于被修改的用户，则可以修改。
					Sys_user tempU = getResultById(id);
					if(tempU == null){
						throw new Exception("用户未找到");
					}
					uOfficeId = tempU.getOfficeId();
					List<Sys_office> listOffice = AccessUtil.helpCreateCurrentDataChildOfficeWithoutOwn();
					boolean b = false;
					for(Sys_office o:listOffice){
						if(o.getId().equals(uOfficeId)){
							b = true;
							break;
						}
					}
					if(!b){
						throw new Exception("当前您只能删除下属机构的用户");
					}
				}else{
					throw new Exception("id不能为空");
				}
			}else{
				throw new Exception("您不可以对自己的账户执行删除操作");
			}
		}
		deleteById(id);
	}

}
