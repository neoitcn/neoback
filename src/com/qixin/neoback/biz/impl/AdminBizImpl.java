package com.qixin.neoback.biz.impl;

import java.util.List;
import java.util.Map;

import com.qixin.neoback.biz.AdminBiz;
import com.qixin.neoback.entity.Edu_news;
import com.qixin.neoback.mapper.Edu_newsMapper;


public class AdminBizImpl  implements  AdminBiz{
	Edu_newsMapper edu_newsMapper;
	@Override
	public int insertSelective(Edu_news record) throws Exception {
		// TODO Auto-generated method stub
		return edu_newsMapper.insertSelective(record);
	}

	@Override
	public int updateByPrimaryKeySelective(Edu_news record) throws Exception {
		// TODO Auto-generated method stub
		return edu_newsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return edu_newsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<Edu_news> findNewsByTypeAndLevel(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return edu_newsMapper.findNewsByTypeAndLevel(map);
	}
	@Override
	public Edu_news selectByPrimaryKey(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return edu_newsMapper.selectByPrimaryKey(id);
	}

	public Edu_newsMapper getEdu_newsMapper() {
		return edu_newsMapper;
	}

	public void setEdu_newsMapper(Edu_newsMapper edu_newsMapper) {
		this.edu_newsMapper = edu_newsMapper;
	}
	

	
	
	

}
