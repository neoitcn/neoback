package com.neo.sys.entity.bean;

import java.io.Serializable;

public class Sequence implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1566549149576314494L;

	private Integer id;

	private Integer initNum;

	/**
	 * 序列编号，可以清0 最长支持12位
	 */
	private String sequenceNum;

	private String name;

	private String keyword;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSequenceNum() {
		return sequenceNum;
	}

	public void setSequenceNum(String sequenceNum) {
		this.sequenceNum = sequenceNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getInitNum() {
		return initNum;
	}

	public void setInitNum(Integer initNum) {
		this.initNum = initNum;
	}

}