package com.qixin.neoback.biz;

import java.util.List;
import java.util.Map;

import com.qixin.neoback.entity.News;

public interface AdminBiz {

	//添加新闻
	int insert(News record)throws Exception;
	//更新新闻
	int updateByPrimaryKeySelective(News record)throws Exception;
	//删除新闻
	int deleteByPrimaryKey(Integer id)throws Exception;
    //根据新闻类型和优先级查询新闻
    List<News> findNewsByTypeAndLevel(Map<String,Object> map);
    News selectByPrimaryKey(Integer id)throws Exception;
	
	
	
}
