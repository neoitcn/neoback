package com.qixin.neoback.biz.impl;

import java.util.List;
import java.util.Map;

import com.qixin.neoback.biz.AdminBiz;
import com.qixin.neoback.entity.News;
import com.qixin.neoback.mapper.NewsMapper;

public class AdminBizImpl  implements  AdminBiz{
	NewsMapper newsMapper;
	@Override
	public int insert(News record) throws Exception {
		// TODO Auto-generated method stub
		return newsMapper.insert(record);
	}

	@Override
	public int updateByPrimaryKeySelective(News record) throws Exception {
		// TODO Auto-generated method stub
		return newsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return newsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<News> findNewsByTypeAndLevel(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return newsMapper.findNewsByTypeAndLevel(map);
	}
	@Override
	public News selectByPrimaryKey(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return newsMapper.selectByPrimaryKey(id);
	}
	public NewsMapper getNewsMapper() {
		return newsMapper;
	}

	public void setNewsMapper(NewsMapper newsMapper) {
		this.newsMapper = newsMapper;
	}

	
	
	

}
