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

public class Test {

	public static void main(String[] args) throws Exception {
		
		/*InputStream inputStream = Test.class.getResourceAsStream("/mybatis.xml");
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = factory.openSession();
		*/
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
	    AdminBiz adminbiz=(AdminBiz) ac.getBean("adminBiz");
	    
	   /* File file=new File("E:/github/neoback/WebContent/2.html");
          // String path = "C:/Users/zhu/workspace/neoback/WebContent/NEWS/IMAGES/";
        if(!file.exists()){
	    	System.out.println("不存在1..");
	    	file.createNewFile();
	    	System.out.println("不存在2..");
	    }*/
		
		String AA="<div class=\"navbar navbar-default navbar-static-top\">"
				+ "<div class=\"container\"><div class=\"navbar-header\">"
				+ "<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\".navbar-collapse\">"
				+ "<span class=\"icon-bar\"></span><span class=\"icon-bar\"></span><span class=\"icon-bar\"></span></button>";	
	
		
		System.out.println("11----:"+AA);
		     
		
	}

}
