package com.qixin.neoback.biz;

import java.util.Map;

import com.qixin.neoback.entity.Userinfo;

public interface UserBiz {
     //��֤��½�ͻ��Ƿ����
	 Userinfo findUserByPsdAndName(Map<String, Object> map)throws Exception;
	  //��ѯ�ͻ�
	 Userinfo selectByPrimaryKey(Integer id)throws Exception;
	//����ͻ���Ϣ
	 int insertSelective(Userinfo record)throws Exception;
	 
}
