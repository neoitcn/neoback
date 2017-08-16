package com.neo.mapper;

import java.util.List;
import java.util.Map;

import com.neo.entity.Edu_news;

public interface Edu_newsMapper {
	//根据新闻类型和优先级查询新闻
    List<Edu_news> findNewsByTypeAndLevel(Map<String,Object> map);
   
    int deleteByPrimaryKey(Integer id);

    int insert(Edu_news record);

    int insertSelective(Edu_news record);

    Edu_news selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Edu_news record);

    int updateByPrimaryKey(Edu_news record);
}