package com.neo.sys.filter.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

public class AofaAuthenticationFilter extends FormAuthenticationFilter {  
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {  
        if(request.getAttribute("valicateCodeCheck") != null) {  
            return true;  
        }  
        return super.onAccessDenied(request, response, mappedValue);  
    }  
}   