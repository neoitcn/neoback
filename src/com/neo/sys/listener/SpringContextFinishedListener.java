package com.neo.sys.listener;

import java.util.Calendar;

import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.neo.sys.utils.SequenceUtil;

@Component("BeanDefineConfigue")
public class SpringContextFinishedListener implements ApplicationListener<ContextRefreshedEvent>{

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		try {
			SequenceUtil.addSequence("OFFICE", "机构编号", Calendar.getInstance().get(Calendar.YEAR) + "000001");
			System.out.println("===============机构编号序列已自动创建===================");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		try {
			SequenceUtil.addSequence("USER", "用户工号", Calendar.getInstance().get(Calendar.YEAR) + "000001");
			System.out.println("===============用户编号序列已自动创建===================");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		

	}

}
