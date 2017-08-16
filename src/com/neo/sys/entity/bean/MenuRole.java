package com.neo.sys.entity.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.neo.entity.Sys_user;
import com.neo.sys.entity.BaseBean;

public class MenuRole extends BaseBean implements Serializable {
	private static final long serialVersionUID = -6595962831878870177L;

	/**
	 * 编号
	 */
	private Integer id;

	/**
	 * 菜单
	 */
	private List<Menu> listMenu = new ArrayList<>();

	/**
	 * 该角色被授予的用户
	 */
	private List<Sys_user> listUser = new ArrayList<>();

	/**
	 * 角色名称
	 */
	private String name;

	/**
	 * 角色类型
	 */
	private String roleType;

	/**
	 * 数据范围
	 */
	private String dataScope;

	/**
	 * 是否系统数据
	 */
	private String isSys;

	/**
	 * 备注信息
	 */
	private String remarks;

	/**
	 * 用户权限
	 * a全部 r查询 w增删改 c审核
	 * 比如，rw具备增删改查权限 rc具备审核和查询权限
	 */
	private String permission;

	private Integer enabled;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Menu> getListMenu() {
		return listMenu;
	}

	public void setListMenu(List<Menu> listMenu) {
		this.listMenu = listMenu;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

	public String getIsSys() {
		return isSys;
	}

	public void setIsSys(String isSys) {
		this.isSys = isSys;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<Sys_user> getListUser() {
		return listUser;
	}

	public void setListUser(List<Sys_user> listUser) {
		this.listUser = listUser;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

}