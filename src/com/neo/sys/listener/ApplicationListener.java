package com.neo.sys.listener;

import java.util.HashSet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

//import com.neoit.sys.init.ImageInit;

public class ApplicationListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent sc) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sc) {
		String root = sc.getServletContext().getRealPath("/");
		System.setProperty("log.root",root);
		//ImageInit.initLoadingImage(root+"resource/img/ico/process1.gif",root+"resource/img/ico/process2.gif");
	}

}
