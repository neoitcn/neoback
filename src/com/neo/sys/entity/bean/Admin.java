package com.neo.sys.entity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 管理员账户信息类,与数据库无关.

 *
 */
public class Admin implements Serializable {

	private static final long serialVersionUID = 5990998326165575381L;
	/**
	 * 管理员名称
	 */
	private String name;
	/**
	 * 管理员密码
	 */
	private String password;
	/**
	 * 特殊验证码
	 */
	private String validateCode;

	/**
	 * 姓名
	 */
	private String cnName;
	/**
	 * 具备的权限
	 */
	private List<String> controllerSecurity;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public List<String> getControllerSecurity() {
		return controllerSecurity;
	}

	public void setControllerSecurity(List<String> controllerSecurity) {
		this.controllerSecurity = controllerSecurity;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
}
