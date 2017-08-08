package com.qixin.neoback.mapper;

import java.util.List;
import java.util.Map;

import com.qixin.neoback.entity.News;

public interface NewsMapper {
	//�����������ͺ����ȼ���ѯ����
    List<News> findNewsByTypeAndLevel(Map<String,Object> map);
    
    
    int deleteByPrimaryKey(Integer id);

    int insert(News record);

    int insertSelective(News record);

    News selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(News record);

    int updateByPrimaryKey(News record);
}