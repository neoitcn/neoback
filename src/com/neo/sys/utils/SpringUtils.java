package com.neo.sys.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationObjectSupport;

/**
 *该类中的方法应该是在所有静态方法的类之前加载，
 *否则在其他类中直接静态加载类中的方法会抛空指针异常
 */
@Component
public class SpringUtils extends WebApplicationObjectSupport{

	private static ApplicationContext applicationContext = null;

	@Override
	protected void initApplicationContext(ApplicationContext context) {
		// TODO Auto-generated method stub
		super.initApplicationContext(context);
		applicationContext = context;
		System.out.println("init...........");
	}
	
	public static <T> T getApplicationBean(String name){
		return (T) applicationContext.getBean(name);
	}
	public static <T> T getApplicationBean(Class<T> clazz){
		return (T) applicationContext.getBean(clazz);
	}
}
