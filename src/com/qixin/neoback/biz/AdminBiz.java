package com.qixin.neoback.biz;

import java.util.List;
import java.util.Map;

import com.qixin.neoback.entity.News;

public interface AdminBiz {

	//�������
	int insert(News record)throws Exception;
	//��������
	int updateByPrimaryKeySelective(News record)throws Exception;
	//ɾ������
	int deleteByPrimaryKey(Integer id)throws Exception;
    //�����������ͺ����ȼ���ѯ����
    List<News> findNewsByTypeAndLevel(Map<String,Object> map);
    News selectByPrimaryKey(Integer id)throws Exception;
	
	
	
}
