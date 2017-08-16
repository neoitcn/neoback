package com.neo.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.neo.sys.annotation.MyBatisDao;
import com.neo.sys.entity.bean.Code;
import com.neo.sys.entity.bean.CodeValue;

@MyBatisDao
public interface CodeDao {

	/**
	 * 获取所有的码表主表信息
	 * @param enabled
	 * @return
	 */
	public List<Code> getAllCode(@Param("enabled") int enabled);
	
	/**
	 * 根据码表主表id获取该记录信息
	 * @param id
	 * @return
	 */
	public Code getCodeById(int id);
	
	public Long getCodeCount(Map<String,Object> param);
	
	public Long getCodeValueCount(Map<String,Object> param);
	
	
	public List<Code> getCodes(Map<String,Object> param);
	
	public List<CodeValue> getCodeValues(Map<String,Object> param);
	
	/**
	 * 获取子表id获取信息
	 * @param id
	 * @return
	 */
	public CodeValue getCodeValueById(int id);
	
	/**
	 * 根据key获取
	 * @param key
	 * @return
	 */
	public Code getCodeByKey(String key);
	
	/**
	 * 依据主表获取字表所有的值
	 * @param codeId
	 * @param enabled
	 * @return
	 */
	public List<CodeValue> getValuesByCodeId(@Param("codeId") int codeId,@Param("enabled") int enabled);
	
	/**
	 * 获取某一个具体字段的值
	 * @param key
	 * @param value
	 * @return
	 */
	public CodeValue getCodeValue(@Param("key") String key,@Param("value") String value);
	
	/**
	 * 保存主表
	 * @param code
	 * @return
	 */
	public int saveCode(Code code);
	
	/**
	 * 保存子表
	 * @param codeValue
	 * @return
	 */
	public int saveCodeValue(CodeValue codeValue);
	
	/**
	 * 修改主表
	 * @param code
	 * @return
	 */
	public int updateCode(Code code);
	
	/**
	 * 修改子表
	 * @param code
	 * @return
	 */
	public int updateCodeValue(CodeValue code);
	
	/**
	 * 删除主表
	 * @param id
	 * @return
	 */
	public int deleteCodeById(Integer id);
	
	/**
	 * 删除子表
	 * @param id
	 * @return
	 */
	public int deleteCodeValueById(Integer id);
	
	
	public int deleteCodeValueByCodeId(Integer codeId);
}
