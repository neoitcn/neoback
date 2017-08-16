package com.neo.sys.entity.bean;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.neo.sys.entity.BaseBean;

public class Code extends BaseBean implements Serializable {


	private static final long serialVersionUID = 2889450139672788528L;

	private Integer id;
	@NotEmpty(message = "名称不能为空")
	private String name;
	@NotEmpty(message = "键 不能为空")
	private String keyWord;
	
	// 对应的值
	private List<CodeValue> listValue;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public List<CodeValue> getListValue() {
		return listValue;
	}

	public void setListValue(List<CodeValue> listValue) {
		this.listValue = listValue;
	}

}
