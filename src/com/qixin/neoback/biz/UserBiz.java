package com.qixin.neoback.biz;

import java.util.Map;

import com.qixin.neoback.entity.Userinfo;

public interface UserBiz {
     //验证登陆客户是否存在
	 Userinfo findUserByPsdAndName(Map<String, Object> map)throws Exception;
	  //查询客户
	 Userinfo selectByPrimaryKey(Integer id)throws Exception;
	//插入客户信息
	 int insertSelective(Userinfo record)throws Exception;
	 
}
