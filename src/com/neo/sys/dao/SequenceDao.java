package com.neo.sys.dao;

import org.apache.ibatis.annotations.Param;

import com.neo.sys.annotation.MyBatisDao;
import com.neo.sys.entity.bean.Sequence;

@MyBatisDao
public interface SequenceDao {
	public Sequence getSequenceById(Integer id);

	public Sequence getSequenceByKey(String key);

	public int addSequence(Sequence sequence);

	public int updateSequence(Sequence sequence);

	public int incSequence(@Param("id") Integer id,@Param("sequenceNum") Long sequenceNum);
	
	public int clearSequence(@Param("key") String key);
	
	public int setSequence(@Param("id") Integer id,@Param("sequenceNum") String sequenceNum);
}