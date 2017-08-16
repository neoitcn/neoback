package com.neo.sys.filter.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.web.filter.AccessControlFilter;

import com.neo.sys.emue.SessionAttribute;
import com.neo.sys.utils.JsonDataUtils;

/**
 * 由shiro负责管理类的验证码过滤器类，用于验证码验证
 *
 */
public class ValicateCodeFilter extends AccessControlFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession httpSession = httpRequest.getSession();
		if(SessionAttribute.need_validate_code.equals(httpSession.getAttribute(SessionAttribute.ISNEED_VALIDATE_CODE))){
			String sessionValidateCode = (String) httpSession.getAttribute(SessionAttribute.VALIDATE_CODE);
			String validateCode = httpRequest.getParameter("validateCode");
			if(validateCode == null){
				JsonDataUtils.writeJSONMessage(response, false, "请输入验证码", null);
				return false;
			}
			if(sessionValidateCode == null){
				JsonDataUtils.writeJSONMessage(response, false, "验证码已过期，请刷新验证码", null);
				return false;
			}
			sessionValidateCode = sessionValidateCode.toLowerCase();
			validateCode = validateCode.toLowerCase();
			
			if(validateCode.equals((sessionValidateCode))){
				request.setAttribute("valicateCodeCheck", "success");
				return true;
			}
		}
		JsonDataUtils.writeJSONMessage(response, false, "验证码错误", null);
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		return true;
	}

}
