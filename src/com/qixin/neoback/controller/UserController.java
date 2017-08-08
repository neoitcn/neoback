package com.qixin.neoback.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.qixin.neoback.biz.UserBiz;
import com.qixin.neoback.entity.Userinfo;

@Controller
@RequestMapping(value="users")
@SessionAttributes("loginUser")
public class UserController {
	@Resource(name="userinfoBiz")
	private UserBiz userinfoBiz;
	
	
	
	@RequestMapping(value="/login")
	public ModelAndView login(Userinfo user,HttpSession session){
		try {
			 System.out.println("�Ѿ�����login......");
		     ModelAndView mv=new ModelAndView();
		    //��ֹһ���sqlע��...
			String name=user.getName();
			String psd=user.getPassword();
			if(name.contains("or")||name.contains("and")||name.contains(" ")){
				System.out.println("����Ƿ�...");
				mv.addObject("msg","����Ƿ�����SQLע��");
				mv.setViewName("index");
				return mv;
			}
			if(psd.contains("or")||psd.contains("and")||psd.contains(" ")){
				System.out.println("����Ƿ�...");
				mv.addObject("msg","����Ƿ�����SQLע��.");
				mv.setViewName("index");
				return mv;
			}
			
			
			
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", user.getName());
			map.put("password", user.getPassword());
			Userinfo userinfo=userinfoBiz.findUserByPsdAndName(map);
			if(userinfo==null){
				System.out.println("�û��������벻��ȷ....");
				mv.addObject("msg","�û��������벻��ȷ��");
				mv.setViewName("index");
				return mv;
			}
			session.setAttribute("loginUser", userinfo);
			System.out.println("����ת��....");
			mv.setViewName("main");
			return mv;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
