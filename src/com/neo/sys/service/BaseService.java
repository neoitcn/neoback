package com.neo.sys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.neo.sys.entity.bean.SysFile;
import com.neo.sys.utils.AutoSetColumn;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.neo.sys.dao.BaseDao;

/**
 * Service层的基类
 * 所有的基础方法都已实现，所有的Service层只需要继承该Service即可实现相应的功能。
 * 可以自定义所需方法。
 * 该类不可以new，不可以被Spring初始化，只能继承。
 *
 * @param <T> 泛型
 */
public abstract class BaseService<T>{
	
	@Autowired
	private BaseDao<T> baseDao;
	
	/**
	 * 分页查询，param传入一些查询条件或其他参数。
	 * 你应该在Controller曾将这些参数说明清楚。
	 * 无需传入分页参数。
	 * @param param
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<T> getResultsByParam(Map<String,Object> param){
		return baseDao.getResultsByParam(param);
	}
	/**
	 * 根据id获取唯一的一个结果集。
	 * @param id 由于某些表的id为Integer类型，有些为Stringl类型，这里为了统一，使用了Object类型。
	 * @return
	 */
	@Transactional(readOnly = true)
	public T getResultById(Object id){
		return baseDao.getResultById(id);
	}
	
	/**
	 * 向数据库插入一条记录。
	 * @param obj
	 * @return
	 */
	@Transactional(readOnly = false)
	public int insert(T t){
		AutoSetColumn.autoInsert(t);
		return baseDao.insert(t);
	}
	
	/**
	 * 向数据库更新一条记录
	 * @param obj
	 * @return
	 */
	@Transactional(readOnly = false)
	public int update(T t){
		AutoSetColumn.autoUpdate(t);
		return baseDao.update(t);
	}
	
	/**
	 * 根据id删除一条记录
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public int deleteById(Object id){
		return baseDao.deleteById(id);
	}

	/**
	 * 获取所有的已启用的记录且未删除的记录
	 * @return
	 */
	@Transactional(readOnly = true)
	public final List<T> getAllResults(){
		Map<String,Object> param = new HashMap<>();
		param.put("enabled",1);
		param.put("deleted",1);
		return getResultsByParam(param);
	}
}
