package com.neo.sys.tlds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

//import com.neoit.sifa.entity.sys.Office;
import com.neo.entity.Sys_user;
import com.neo.sys.entity.bean.Code;
import com.neo.sys.entity.bean.CodeValue;
import com.neo.sys.entity.bean.Menu;
import com.neo.sys.entity.bean.MenuRole;
import com.neo.sys.service.service.CodeService;
import com.neo.sys.service.service.MenuRoleService;
import com.neo.sys.service.service.MenuService;
import com.neo.sys.utils.SpringUtils;

/**
 * 用于前台el表达式的方法调用
 * 
 */
public class ElUtil {

	private static MenuService menuService = SpringUtils.getApplicationBean(MenuService.class);
	private static CodeService codeService = SpringUtils.getApplicationBean(CodeService.class);
	private static MenuRoleService menuRoleService = SpringUtils.getApplicationBean(MenuRoleService.class);

	/**
	 * 获取当前用户授权菜单
	 * 
	 * @return
	 */
	public static List<Menu> getMenuList(Integer roleId) {

		List<Menu> listMenu = new ArrayList<>();
		try {
			if (roleId == null) {
				// listMenu = menuService.getAllMenuList(1);
				listMenu = menuService.getAllMenuList(1);
			} else {
				MenuRole menuRole = menuRoleService.getResultById(roleId);
				listMenu = menuRole.getListMenu();
				Collections.sort(listMenu, new Comparator<Menu>() {

					@Override
					public int compare(Menu o1, Menu o2) {
						if (o1.getSort() == null) {
							return 1;
						}
						if (o2.getSort() == null) {
							return -1;
						}
						return o1.getSort() - o2.getSort();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listMenu;

	}

//	public static List<Menu> getMenuList(Sys_user user) {
//		return getMenuList(user,menuRoleService.getMenuRoleByUserId(user.getId()));
//	}

	public static List<Menu> getMenuList(Sys_user user,List<MenuRole> listMenuRole) {
//		// 获取用户对应的信息表
//		if (user != null) {
//			Office office = user.getDepartId();
//			if (office != null) {
////				MenuRoleService menuRoleService = SpringUtils.getApplicationBean(MenuRoleService.class);
//				List<Menu> listMenu = new ArrayList<>();
//				if (listMenuRole != null) {
//					for (MenuRole mr : listMenuRole) {
//						List<Menu> l = mr.getListMenu();
//						if (l != null) {
//							listMenu.addAll(l);
//						}
//					}
//				}
//				//当一个机构拥有多个角色时，需要去掉角色对应的重复的功能
//				if (listMenuRole != null && listMenuRole.size() > 1) {
//					// 去重
//					for (int i = 0; i < listMenu.size(); i++) {
//						for (int j = i + 1; j < listMenu.size();) {
//							Menu m1 = listMenu.get(i);
//							Menu m2 = listMenu.get(j);
//							if (m1.getId() == m2.getId()) {
//								listMenu.remove(j);
//								continue;
//							}
//							j++;
//						}
//					}
//
//					Collections.sort(listMenu, new Comparator<Menu>() {
//
//						@Override
//						public int compare(Menu o1, Menu o2) {
//							if (o1.getSort() == null) {
//								return 1;
//							}
//							if (o2.getSort() == null) {
//								return -1;
//							}
//							return o1.getSort() - o2.getSort();
//						}
//					});
//				}
//
//				return listMenu;
//			}
//		}

		return null;
	}

	/**
	 * 判断字符串是否为空或空字符串
	 */
	public static boolean strIsBlank(String str) {
		return StringUtils.isBlank(str);
	}

	/**
	 * 获取码表内容
	 * 
	 * @param key
	 * @return
	 */
	public static List<CodeValue> getCodeValues(String key) {
		Code code = codeService.getCodeByKey(key);
		if (code != null) {
			List<CodeValue> list = codeService.getValuesByCodeId(code.getId(), -1);
			if (list != null) {
				return list;
			}
		}
		return new ArrayList<CodeValue>();
	}
}
