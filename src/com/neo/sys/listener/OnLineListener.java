package com.neo.sys.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.neo.sys.utils.SessionManagerUtil;
import com.neo.sys.utils.ValidateCodeImageUtil;

public class OnLineListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		ValidateCodeImageUtil.clearSessionValidateCodeInfo(sessionEvent.getSession());
		SessionManagerUtil.deleteSessionByUserId(sessionEvent.getSession().getId());
		System.out.println("session destoried");
	}

}
