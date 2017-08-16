package com.neo.sys.utils;

import org.apache.commons.lang.StringUtils;

public class SupportFileTypeUtil {

	private static String[] supportType;
	private static String[] showType;
	private static String[] disableName;
	
	static{
		String supportType = SystemConfigUtil.getConfig("editfile.editfile");
		String showType = SystemConfigUtil.getConfig("editfile.filter");
		String disableType = SystemConfigUtil.getConfig("editfile.disableFile");
		if(StringUtils.isBlank(supportType)){
			supportType = "xml,properties,html,jsp,js,css";
		}
		SupportFileTypeUtil.supportType = supportType.split(",");
		if(StringUtils.isNotBlank(showType)){
			SupportFileTypeUtil.showType = showType.split(",");
		}else{
			SupportFileTypeUtil.showType = new String[0];
		}
		SupportFileTypeUtil.disableName = supportType.split(",");
		if(StringUtils.isNotBlank(disableType)){
			SupportFileTypeUtil.disableName = disableType.split(",");
		}else{
			SupportFileTypeUtil.disableName = new String[0];
		}
	}
	
	public static boolean checkIsSupport(String type){
		for(String str:supportType){
			if(str.equalsIgnoreCase(type)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkIsShow(String type){
		for(String str:showType){
			if(str.equalsIgnoreCase(type)){
				return true;
			}
		}
		return false;
	}
	public static boolean checkIsDisableShow(String name){
		for(String str:disableName){
			if(str.equals(name)){
				return true;
			}
		}
		return false;
	}
	
}
