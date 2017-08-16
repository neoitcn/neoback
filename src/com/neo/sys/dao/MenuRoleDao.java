package com.neo.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.neo.sys.annotation.MyBatisDao;
import com.neo.sys.entity.bean.MenuRole;

@MyBatisDao
public interface MenuRoleDao extends BaseDao<MenuRole>{
	
	/**
	 * 根据机构ID获取角色
	 * @param officeId
	 * @return
	 */
	public List<MenuRole> getMenuRoleByUserId(@Param("userId") String userId);

	public int addMenuRoleLink(@Param("roleId") Integer roleId, @Param("menuId") Integer menuId);
	
	public int addUserRoleLink(@Param("roleId") Integer roleId, @Param("userId") String userId);

	public int deleteMenuRoleLinkByMenu(Integer menuId);

	public int deleteMenuRoleLinkByRole(Integer roleId);
	
	public int deleteUserLinkByUser(String userId);
	
	public int deleteUserLinkByDetail(@Param("userId") String userId,@Param("roleId") Integer roleId);
}