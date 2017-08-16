package com.qixin.neoback.mapper;

import java.util.List;
import java.util.Map;

import com.qixin.neoback.entity.Edu_news;

public interface Edu_newsMapper {
	//根据类型和级别查询新闻
	List<Edu_news> findNewsByTypeAndLevel(Map<String, Object> map);
	
    int deleteByPrimaryKey(Integer id);

    int insert(Edu_news record);
    //添加新闻
    int insertSelective(Edu_news record);
   
    Edu_news selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Edu_news record);

    int updateByPrimaryKey(Edu_news record);
}