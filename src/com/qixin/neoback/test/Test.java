package com.qixin.neoback.test;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.tomcat.jni.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.qixin.neoback.biz.AdminBiz;

import com.qixin.neoback.entity.News;




public class Test {

	public static void main(String[] args) throws Exception {
		
		/*InputStream inputStream = Test.class.getResourceAsStream("/mybatis.xml");
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = factory.openSession();*/
		
		/*ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
	    UserBiz userbiz=(UserBiz) ac.getBean("userinfoBiz");
	    AdminBiz adminbiz=(AdminBiz) ac.getBean("adminBiz");
	    Map<String,Object>  map=new HashMap<String,Object>();
	 
	    map.put("type", 1);
	    map.put("level", 1);
	    System.out.println("kkk:"+adminbiz.findNewsByTypeAndLevel(map).size());
	    
	   for (News news : adminbiz.findNewsByTypeAndLevel(map)) {
		   System.out.println("名字:"+news.getResume());
	   }*/
	    
	    
	    
	    
	    /* map.put("name", "admin");
	    map.put("password", "123456");
	    System.out.println("ddd:"+userbiz.findUserByPsdAndName(map));
		System.out.println("gg:"+userbiz.selectByPrimaryKey(1).getMobile());
		*/
		/*Userinfo user1=new Userinfo();
		SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		
		Date date1=sdf.parse("2017-07-06 09:46:00");
		user1.setCreatetime(date1);
		user1.setEmail("110@qq.com");
		user1.setMobile("13077889941");
		user1.setName("zyj");
		user1.setPassword("123456");
		userbiz.insertSelective(user1);
		System.out.println("fff:"+userbiz.insertSelective(user1));
	    System.out.println("gg:"+userbiz.selectByPrimaryKey(2).getCreatetime());*/
		
		
		
		    File file=new File("neoback/WebContent/NEWS/IMAGES");
          // String path = "C:/Users/zhu/workspace/neoback/WebContent/NEWS/IMAGES/";
            String path=file.getAbsolutePath().replace("\\", "/")+"/";
		    System.out.println("hah00:"+path);
		     
		
	}

}
