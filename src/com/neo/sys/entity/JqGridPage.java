package com.neo.sys.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * jqgrid后台返回的数据类型
 * 
 *
 */
public class JqGridPage<T> {

	private Integer preNum;// 每页显示的记录条数

	private Long total; // 总页数

	private Long page;// 当前页

	private Long records;// 总记录数
	
	private List<?> rows = new ArrayList<>(); // 数据

	public JqGridPage() {
	}

	public JqGridPage(List<?> rows) {
		this.rows = rows;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	public Long getRecords() {
		return records;
	}

	public void setRecords(Long records) {
		this.total = records;
		this.records = records;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public Integer getPreNum() {
		return preNum;
	}

	public void setPreNum(Integer preNum) {
		this.preNum = preNum;
	}

}
