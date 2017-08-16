package com.neo.sys.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PathFilter implements Filter {

	/**
	 * 如果你需要在html页面或外部js中获取服务的绝对地址，通过Cookie获取。
	 */
	private Cookie pathCookie;
	
	@Override
	public void destroy() {
		pathCookie = null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest h = (HttpServletRequest)request;
		request.setAttribute("ctx",h.getContextPath());
		HttpServletResponse hresponse = (HttpServletResponse) response;
//		hresponse.addCookie(pathCookie);
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig fc) throws ServletException {
		String h = fc.getServletContext().getContextPath();
		//pathCookie  = new Cookie("ctx",h);
	}

}
