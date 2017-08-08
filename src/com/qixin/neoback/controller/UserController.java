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
			 System.out.println("已经进入login......");
		     ModelAndView mv=new ModelAndView();
		    //防止一般的sql注入...
			String name=user.getName();
			String psd=user.getPassword();
			if(name.contains("or")||name.contains("and")||name.contains(" ")){
				System.out.println("输入非法...");
				mv.addObject("msg","输入非法！防SQL注入");
				mv.setViewName("index");
				return mv;
			}
			if(psd.contains("or")||psd.contains("and")||psd.contains(" ")){
				System.out.println("输入非法...");
				mv.addObject("msg","输入非法！防SQL注入.");
				mv.setViewName("index");
				return mv;
			}
			
			
			
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", user.getName());
			map.put("password", user.getPassword());
			Userinfo userinfo=userinfoBiz.findUserByPsdAndName(map);
			if(userinfo==null){
				System.out.println("用户名或密码不正确....");
				mv.addObject("msg","用户名或密码不正确！");
				mv.setViewName("index");
				return mv;
			}
			session.setAttribute("loginUser", userinfo);
			System.out.println("正在转发....");
			mv.setViewName("main");
			return mv;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
