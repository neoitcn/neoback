package com.neo.mapper.sys;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.neo.entity.Sys_user;
import com.neo.sys.annotation.MyBatisDao;
import com.neo.sys.dao.BaseDao;

@MyBatisDao
public interface UserDao extends BaseDao<Sys_user>{
	
	//public User getUserByName(String username);
	
	public Sys_user getUserByOrder(String loginName, String password);
	
	public Sys_user getUserByAccount(@Param("loginName") String account,@Param("loadOffice") String loadOffice);
	
	public int addUser(Sys_user user);
	
	public int updateUser(Sys_user user);
	
	public int updateUserPassword(@Param("id") String id,@Param("oldPassword") String oldPassword,@Param("password") String password);
	
	public int deleteUserById(String id);
	
	public List<Sys_user> checkUser(@Param("email") String email,@Param("loginName") String loginName,@Param("mobile") String mobile);
	
}
