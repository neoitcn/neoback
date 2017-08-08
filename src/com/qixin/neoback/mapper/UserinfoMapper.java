package com.qixin.neoback.mapper;

import java.util.Map;

import com.qixin.neoback.entity.Userinfo;

public interface UserinfoMapper {
	//²éÑ¯¿Í»§
	Userinfo findUserByPsdAndName(Map<String, Object> map);
    
	int deleteByPrimaryKey(Integer id);

    int insert(Userinfo record);

    int insertSelective(Userinfo record);

    Userinfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Userinfo record);

    int updateByPrimaryKey(Userinfo record);
}