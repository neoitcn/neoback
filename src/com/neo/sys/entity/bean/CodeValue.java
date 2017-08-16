package com.neo.sys.entity.bean;

import java.io.Serializable;

import com.neo.sys.entity.BaseBean;

public class CodeValue extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7700233499892397279L;

	private Integer id;
	private Integer codeId;
	private String name;
	private String value;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCodeId() {
		return codeId;
	}

	public void setCodeId(Integer codeId) {
		this.codeId = codeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
