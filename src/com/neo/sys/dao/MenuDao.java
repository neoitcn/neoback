package com.neo.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.neo.sys.annotation.MyBatisDao;
import com.neo.sys.entity.bean.Menu;

@MyBatisDao
public interface MenuDao {

	/**
	 * 获取所有的菜单
	 * @return
	 */
	public List<Menu> getAllMenuList(@Param("enabled") int enabled);
	
	/**
	 * 根据父级菜单id获取菜单，所有跟菜单的父级菜单id为-1
	 * @param id
	 * @return
	 */
	public List<Menu> getMenuByParentId(@Param("parentId") int parentId,@Param("enabled") int enabled);
	
	/**
	 * 获取菜单详情
	 * @param id
	 * @return
	 */
	public Menu getMenuDetail(Integer id);
	
	/**
	 * 获取当前菜单自己以及它的子节点
	 * @param id
	 * @return
	 */
	public List<Menu> getMenuChildren(Integer id);
	
	
	/**
	 * 根据权限名称获取菜单
	 * @param controllerName
	 * @return
	 */
	public List<Menu> getMenuByControllerName(String controllerName);
	
	/**
	 * 保存一个新建菜单
	 * @param menu
	 * @return
	 */
	public int saveMenu(Menu menu);
	
	/**
	 * 更新一个新建菜单
	 * @param menu
	 * @return
	 */
	public int updateMenu(Menu menu);
	
	
	public int deleteMenu(Integer id);
}
