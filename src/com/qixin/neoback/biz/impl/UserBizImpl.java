package com.qixin.neoback.biz.impl;

import java.util.Map;

import com.qixin.neoback.biz.UserBiz;
import com.qixin.neoback.entity.Userinfo;
import com.qixin.neoback.mapper.UserinfoMapper;

public class UserBizImpl implements UserBiz {
	UserinfoMapper userinfoMapper;
	@Override
	public Userinfo findUserByPsdAndName(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return userinfoMapper.findUserByPsdAndName(map);
	}
	@Override
	public Userinfo selectByPrimaryKey(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return userinfoMapper.selectByPrimaryKey(id);
	}
	@Override
	public int insertSelective(Userinfo record) throws Exception {
		// TODO Auto-generated method stub
		return userinfoMapper.insertSelective(record);
	}
	
	
	public UserinfoMapper getUserinfoMapper() {
		return userinfoMapper;
	}
	public void setUserinfoMapper(UserinfoMapper userinfoMapper) {
		this.userinfoMapper = userinfoMapper;
	}



}
