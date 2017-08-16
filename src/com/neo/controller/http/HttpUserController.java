package com.neo.controller.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neo.entity.Sys_office;
import com.neo.entity.Sys_user;
//import com.neo.service.sys.OfficeService;
import com.neo.service.sys.UserService;
//import com.neo.util.PointUti;
//import com.neo.util.entity.Point;
import com.neo.sys.annotation.ControllerName;
import com.neo.sys.emue.SessionAttribute;
import com.neo.sys.entity.bean.SysFile;
import com.neo.sys.service.service.SysFileService;
import com.neo.sys.utils.AppValidateUtil;
import com.neo.sys.utils.CodeUtils;
import com.neo.sys.utils.FileUtils;
import com.neo.sys.utils.FormatUtil;
import com.neo.sys.utils.ValidateCodeImageUtil;
import com.neo.sys.web.BaseController;


@Controller
@Scope(value="prototype")
@RequestMapping("httpUser")
@ControllerName
public class HttpUserController extends BaseController {
	
	@Autowired
	private UserService userService;
	

	@Autowired
	private SysFileService sysFileService;
	
	
	
	private ReentrantLock lock = new ReentrantLock();
	
	@ControllerName(open=false)
	@RequestMapping(value="login"/*,method=RequestMethod.POST*/)
	@ResponseBody
	public Map<String,Object> login(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String isSuccess ="0";
		String msg = "登录失败,请重新登录";
		Map<String,Object> m = new HashMap<>();
		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			String os=request.getParameter("os");
			String name=request.getParameter("name");
			String mac=request.getParameter("mac");
			String password=request.getParameter("password");
			String os_version=request.getParameter("os_Version");
			String version=request.getParameter("version");
			String validateCode=request.getParameter("validateCode");
			String appCode=request.getParameter("appCode");
			Sys_user user=new Sys_user();
			user.setName(name);
			user.setPassword(password);				
			String serverValidate=AppValidateUtil.getMap(appCode);
			if(StringUtils.isBlank(appCode)){
				throw new Exception("验证码过期");
			}
			Sys_user user1=	null;
			if(validateCode.equals(serverValidate))
			{
				//user1=	pService.appDoLogin(user);
			}
			else{
				throw new Exception("验证码输入不正确");
			}

			
			if(user1!=null){					
					m.put("id", user1.getId());
					m.put("loginName", user1.getName());
					//m.put("no", user1.getNo());
					m.put("name", user1.getName());
					m.put("email", user1.getEmail());
					 
					m.put("mobile",user1.getMobile());
					m.put("userType", user1.getUserType());	
					m.put("officeId", user1.getOfficeId());
					m.put("remarks", user1.getRemarks());
					msg="ok";
					isSuccess = "1";
			}
			
		} catch (Exception e) {
			msg=e.getMessage();
		
		}
		
		return createHttpJSONMessage(isSuccess,msg,m);
	}
	//08c10699ec8b47caa1832100794c78dc
	@RequestMapping("appCode")
	@ControllerName(open = false)
	@ResponseBody
	public Map<String,Object> appCode(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String isSuccess ="0";
		String msg = "";
		 String temp="";;
		 response.setHeader("Access-Control-Allow-Origin", "*");
			try {
				 UUID uuid = UUID.randomUUID();  
			        String str = uuid.toString();  
			        // 去掉"-"符号  
			       temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);  
			      msg="ok";
				  isSuccess = "1";
			} catch (Exception e) {
				msg=e.getMessage();
			} 
			
			return createHttpJSONMessage(isSuccess,msg,temp);
	}
	
	
	@RequestMapping("valdat-image")
	@ControllerName(open = false)
	public void validateImage(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 如果服务器已经正在生成验证码了，前台应该等待生成结果
		String appCode=request.getParameter("appCode");
		
			try {		

			String validate=ValidateCodeImageUtil.createRandomPutInSessionAndOutput(request.getSession(), response.getOutputStream());
			AppValidateUtil.putMap(appCode, validate);
		
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	
	@ControllerName(open=false)
	@RequestMapping(value="loginout")
	@ResponseBody
	public Map<String,Object> loginOut(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String isSuccess ="0";
		String msg = "";
		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
		request.getSession().removeAttribute(SessionAttribute.CURRENT_USER);
		request.getSession().removeAttribute(SessionAttribute.CURRENT_USER_MENU);
		msg="ok";
		isSuccess = "1";
		} catch (Exception e) {
			msg=e.getMessage();
		
		}
		return createHttpJSONMessage(isSuccess,msg,null);
	
	}
	
	@ControllerName(open=false)
	@RequestMapping(value="getUserInfo"/*,method=RequestMethod.POST*/)
	@ResponseBody
	public Map<String,Object> userInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String isSuccess ="0";
		String msg = "";
		Map<String,Object> m = new HashMap<>();
		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			String uid=request.getParameter("uid");
			
			Sys_user user1=userService.getResultById(uid);	
			if(user1!=null){					
					m.put("id", user1.getId());
					m.put("loginName", user1.getName());
				 
					m.put("name", user1.getName());
					m.put("email", user1.getEmail());
			 
					m.put("mobile",user1.getMobile());
					m.put("userType", user1.getUserType());	
					m.put("officeId", user1.getOfficeId());
					m.put("remarks", user1.getRemarks());
			}
			msg="ok";
			isSuccess = "1";
		} catch (Exception e) {
			msg=e.getMessage();		
		}
		return createHttpJSONMessage(isSuccess,msg,m);
	}
	
	
	@ControllerName(open=false)
	@RequestMapping(value="getUserInfoList"/*,method=RequestMethod.POST*/)
	@ResponseBody
	public Map<String,Object> userInfoList(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String isSuccess ="0";
		String msg = "";	
		String name=request.getParameter("name");		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("name", name);
		List<Object> list = new ArrayList<>();
		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			
			List<Sys_user> listUser = userService.getResultsByParam(param);
			if(listUser!=null){
				for(Sys_user u:listUser){
					Map<String,Object> m = new HashMap<>();
					m.put("id", u.getId());
					m.put("loginName", u.getName());
					m.put("name", u.getName());
					m.put("userType", u.getUserType());
					m.put("mobile", u.getMobile());
					m.put("email", u.getEmail());
					m.put("remarks", u.getRemarks());
					list.add(m);
				}
			}
			
			msg="ok";
			isSuccess = "1";
		} catch (Exception e) {
			msg=e.getMessage();
		
		}
		return createHttpJSONMessage(isSuccess,msg,list);
	}
	
	
	             
	
//获取当前的APP版本
	
	
	@RequestMapping("getCurrentVersion")
	@ResponseBody 
	@ControllerName(open=false)
	public Map<String,Object> getCurrentVersion(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String isSuccess ="0";
		String msg = "";
		String clientVersion=request.getParameter("version");
		//String clientVersion="2016.06.12";
		String nowVersion="";
		String isUpdate="1";
		Map<String,Object> m = new HashMap<>();
		
		try {
			File configFile = ResourceUtils.getFile("classpath:appVersion.properties");
			if (configFile.exists()) {
				Properties configP = new Properties();
				FileInputStream fis = new FileInputStream(configFile);
				configP.load(fis);
				fis.close();
				nowVersion = configP.getProperty("version");
			
			} else {
				System.out.println("版本更新配置文件不存在");
			}
			
			 if(clientVersion.equals(nowVersion))  
			 {
				 isUpdate="0";
			 }			   
		     m.put("version", isUpdate);   
			msg="ok";
			isSuccess ="1";
		  } catch (Exception e) {
		    msg=e.getMessage();
		 }		
		response.setHeader("Access-Control-Allow-Origin", "*");
		return createHttpJSONMessage(isSuccess,msg,m);
	}
	
	    
}
