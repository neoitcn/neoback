
package com.neo.sys.entity.bean;

import java.io.Serializable;

import com.neo.sys.utils.ConfigValues;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.neo.sys.entity.BaseBean;

/**
 * 菜单Entity
 * 
 */
public class Menu extends BaseBean implements Serializable {
	private static final long serialVersionUID = 5514633450615686917L;
	private Integer id;
	private Integer parentId;
	@NotEmpty
	private String name;
	private String url;
	private Integer sort;
	private String icon;
	private String parentIds;
	private String controllerName;
	private String iconUrl;

	// 该字段不属于数据表字段
	private String parentName;
	private String createrName;

	private String mappingIconUrl;

	public Menu() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	@Override
	public String toString() {
		return "Menu [id=" + id + ", parentId=" + parentId + ", name=" + name + ", url=" + url + ", sort=" + sort
				+ ", icon=" + icon + ", parentIds=" + parentIds + ", controllerName=" + controllerName + ", iconUrl="
				+ iconUrl + ", parentName=" + parentName + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof Menu) {
				Menu m = (Menu) obj;
				return this.getId() == m.getId();
			}
		}
		return false;
	}

	public String getMappingIconUrl() {
		return mappingIconUrl;
	}

	public void setMappingIconUrl(String mappingIconUrl) {
		this.mappingIconUrl = mappingIconUrl;
	}
}