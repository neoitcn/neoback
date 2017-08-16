package com.neo.sys.dao;

import java.util.List;
import java.util.Map;

/**
 * 操作MyBatis的超级dao，该dao曾拥有一些通用方法，这些通用方法可以用在任何时候，无需在其他dao中再写这些方法。
 *
 * @param <T>
 */
public interface BaseDao<T> {

	/**
	 * 分页查询，param传入一些查询条件或其他参数。
	 * 你应该在Controller曾将这些参数说明清楚。
	 * @param param
	 * @return
	 */
	public List<T> getResultsByParam(Map<String,Object> param);
	
	/**
	 * 根据id获取唯一的一个结果集。
	 * @param id 由于某些表的id为Integer类型，有些为Stringl类型，这里为了统一，使用了Object类型。
	 * @return
	 */
	public T getResultById(Object id);
	
	/**
	 * 向数据库插入一条记录。
	 * @param t
	 * @return
	 */
	public int insert(T t);
	
	/**
	 * 向数据库更新一条记录
	 * @param t
	 * @return
	 */
	public int update(T t);
	
	/**
	 * 根据id删除一条记录
	 * @param id
	 * @return
	 */
	public int deleteById(Object id);
	
	
	/**
	 * 获取所有的已启用的记录
	 * 此方法被转移到service层
	 * @return
	 */
	/*public default List<T> getAllResults(){
		return getResultsByParam(1);
	}*/
}
