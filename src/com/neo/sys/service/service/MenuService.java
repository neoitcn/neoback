package com.neo.sys.service.service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import com.neo.entity.Sys_user;
import com.neo.sys.emue.SessionAttribute;
import com.neo.sys.entity.bean.Admin;
import com.neo.sys.entity.bean.MenuRole;
import com.neo.sys.entity.bean.SysFile;
import com.neo.sys.tlds.ElUtil;
import com.neo.sys.utils.AdminUtil;
import com.neo.sys.utils.AutoSetColumn;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neo.sys.dao.MenuDao;
import com.neo.sys.entity.bean.Menu;
import com.neo.sys.service.BaseService;

import javax.servlet.http.HttpServletRequest;

import static java.awt.SystemColor.menu;

@Service
public class MenuService{

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private SysFileService sysFileService;

	@Transactional(readOnly = true)
	public List<Menu> getAllMenuList(int enabled) throws Exception {
		return menuDao.getAllMenuList(enabled);
	}

	@Transactional(readOnly = true)
	public List<Menu> getMenuByParentId(Integer parentId, int enabled) throws Exception {
		return menuDao.getMenuByParentId(parentId, enabled);
	}

	@Transactional(readOnly = true)
	public Menu getMenuDetail(Integer id) throws Exception {
		return menuDao.getMenuDetail(id);
	}

	/**
	 * 获取当前menu的所有子元素
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<Menu> getMenuChildren(Integer id) throws Exception {
		return menuDao.getMenuChildren(id);
	}
	
	@Transactional(readOnly = true)
	public List<Menu> getMenuByControllerName(String controllerName){
		return menuDao.getMenuByControllerName(controllerName);
	}

	@Transactional(readOnly = false)
	public int addMenu(Menu menu) {
		AutoSetColumn.autoInsert(menu);
		return menuDao.saveMenu(menu);
	}

	@Transactional(readOnly = false)
	public int updateMenu(Menu menu) {
		AutoSetColumn.autoUpdate(menu);
		return menuDao.updateMenu(menu);
	}

	@Transactional(readOnly = false)
	public int deleteMenu(Integer id){
		List<Menu> menuList = menuDao.getMenuChildren(id);
		if(menuList!=null&&menuList.size()>1){
			throw new RuntimeException("该菜单下有子菜单，请先删除子菜单");
		}
		return menuDao.deleteMenu(id);
	}


	/**
	 * 新增菜单或修改菜单的标准方法
	 * @param menu
	 * @param deleteIcon
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Transactional( readOnly = false)
	public Menu saveMenu(Menu menu,String deleteIcon, HttpServletRequest request) throws Exception{
		if (menu.getParentId() == null) {
			menu.setParentId(-1);
		}
		boolean isCreate = false;
		if (menu.getId() == null) {
			isCreate = true;
			addMenu(menu);
			List<SysFile> sysFileList = sysFileService.addFile(request, "menu-ico", menu.getId()+"menu");
			if (sysFileList.size() > 0) {
				menu.setIconUrl(sysFileList.get(0).getFilePath());
				updateMenu(menu);
			}
		} else {
			if ("1".equals(deleteIcon)) {
				if (StringUtils.isNotBlank(menu.getIcon())) {
					sysFileService.deleteFileByObjectId(menu.getId()+"menu",request);
					menu.setIconUrl(null);
				}
			}else{
				List<SysFile> sysFileList = sysFileService.addFile(request, "menu-ico",menu.getId()+"menu");
				if (sysFileList!=null && sysFileList.size() > 0) {
					List<SysFile> sysFileListTemp = sysFileService.getFileByObjectId(menu.getId()+"menu");
					if(sysFileListTemp!=null){
						for(SysFile sy:sysFileListTemp){
							sysFileService.deleteFile(sy.getId(), request);
						}
					}
					menu.setIconUrl(sysFileList.get(0).getFilePath());
				}else{
					Menu m = getMenuDetail(menu.getId());
					menu.setIconUrl(m.getIconUrl());
				}
			}
			updateMenu(menu);
		}
		//更新成功后更新session中的menu字段 2017.2.9新增 系统不更新所有在线用户的session，只更新当前用户的session
		//如果修改的菜单的排序被更改则需要重新对该菜单进行排序。
		List<Menu> listMenu = (List<Menu>) request.getSession().getAttribute(SessionAttribute.CURRENT_USER_MENU);
		boolean isSort = false;
		if(!isCreate){
			Menu m;
			for(int i=0;i<listMenu.size();i++){
				m = listMenu.get(i);
				if(m.getId() == menu.getId()){
					isSort=m.getSort() != menu.getSort();
					if(isSort){
						listMenu.remove(i);
					}else{
						listMenu.set(i,menu);
					}
					break;
				}
			}
		}
		if(isCreate || isSort){
			Menu m;
			boolean isAdd = false;
			if(menu.getSort()!=null){
				for(int i=0;i<listMenu.size();i++){
					m = listMenu.get(i);
					if(m.getSort() == null || m.getParentId() != menu.getParentId()){
						continue;
					}
					if(m.getSort()>menu.getSort()){
						isAdd = true;
						listMenu.add(i,menu);
						break;
					}
				}

			}
			if(!isAdd){
				listMenu.add(menu);
			}
		}
		return menu;
	}

	/**
	 * 标准删除方法，将所有业务处理代码存放在Service层，不会发生脏读等事务性问题
	 * @param id
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public void deleteMenu(Integer id, HttpServletRequest request) throws Exception{
		Menu menu = getMenuDetail(id);
		if(menu == null){
			throw new Exception("菜单不存在");
		}
		List<Menu> list = getMenuChildren(id);
		if(list != null){
			for(int i=0;i<list.size();i++){
				if(list.get(i).getId() == id){
					list.remove(i);
					break;
				}
			}
			if(list.size()>0){
				throw new Exception("菜单下有子菜单，不允许删除");
			}
		}
		deleteMenu(id);
		String icoPath = menu.getIconUrl();
		String realPath = request.getServletContext().getRealPath("/");
		File f = new File(realPath+icoPath);
		while(f.exists()){
			f.delete();
			if(f.exists()){
				Thread.sleep(1000);
			}
		}

		//删除成功后更新session中的menu字段 2017.2.9新增 系统不更新所有在线用户的session，只更新当前用户的session
		List<Menu> listMenu = (List<Menu>) request.getSession().getAttribute(SessionAttribute.CURRENT_USER_MENU);
		Menu m;
		for(int i=0;i<listMenu.size();i++){
			m = listMenu.get(i);
			if(m.getId() == id){
				listMenu.remove(i);
				break;
			}
		}
	}

//	/**
//	 * 获取用户的所有菜单，如果用户的id为空，则判定该用户为管理员。
//	 * listMenuRole纯粹是为了在登录时能够获取用户的MenuRole信息而创建的。该参数在使用是可以设置为空。
//	 * @param user
//	 * @return
//	 */
//	public List<Menu> getUserMenu(Sys_user user, List<MenuRole> listMenuRole){
//		List<Menu> listMenu;
//		if(user.getId() == null){
//			Integer i = null;
//			listMenu = ElUtil.getMenuList(i);
//			Admin admin = AdminUtil.getAdminByUser(user);
//			if(admin==null){
//				return null;
//			}
//			List<String> listControllerName = admin.getControllerSecurity();
//			if(listControllerName != null){
//				if(listMenu!=null){
//					here:for(int j=0;j<listMenu.size();){
//						Menu m = listMenu.get(j);
//						for(String str:listControllerName){
//							if(str.equals(m.getControllerName()) || StringUtils.isBlank(m.getControllerName())){
//								j++;
//								continue here;
//							}
//						}
//						listMenu.remove(j);
//					}
//				}
//			}
//		}else{
//			if(listMenuRole == null){
//				listMenu = ElUtil.getMenuList(user);
//			}else{
//				listMenu = ElUtil.getMenuList(user,listMenuRole);
//			}
//		}
//		return listMenu;
//	}
}
