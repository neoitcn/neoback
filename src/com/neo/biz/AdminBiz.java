package com.neo.biz;

import java.util.List;
import java.util.Map;

import com.neo.entity.Edu_news;



public interface AdminBiz {

	//添加新闻
	int insertSelective(Edu_news record)throws Exception;
	//更新新闻
	int updateByPrimaryKeySelective(Edu_news record)throws Exception;
	//删除新闻
	int deleteByPrimaryKey(Integer id)throws Exception;
    //根据新闻类型和优先级查询新闻
    List<Edu_news> findNewsByTypeAndLevel(Map<String,Object> map);
   
    Edu_news selectByPrimaryKey(Integer id)throws Exception;
	
	
	
}
