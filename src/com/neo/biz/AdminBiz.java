package com.neo.biz;

import java.util.List;
import java.util.Map;

import com.neo.entity.Edu_news;



public interface AdminBiz {

	//�������
	int insertSelective(Edu_news record)throws Exception;
	//��������
	int updateByPrimaryKeySelective(Edu_news record)throws Exception;
	//ɾ������
	int deleteByPrimaryKey(Integer id)throws Exception;
    //�����������ͺ����ȼ���ѯ����
    List<Edu_news> findNewsByTypeAndLevel(Map<String,Object> map);
   
    Edu_news selectByPrimaryKey(Integer id)throws Exception;
	
	
	
}
