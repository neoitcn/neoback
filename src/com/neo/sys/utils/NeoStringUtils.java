package com.neo.sys.utils;

import org.apache.commons.lang.StringUtils;

public class NeoStringUtils extends StringUtils {

	/**
	 * 查找相同字符串中的第times个的位置。
	 * 比如 pages/aofa/sifa/index.jsp，查找第二个/的位置。
	 * @param str
	 * @param searchStr
	 * @param times
	 * @return
	 */
	public static int indexOf(String str,String searchStr,int times){
		
		int index = 0;
		while(index!=-1 && times-->0){
			index = str.indexOf(searchStr, index)+1;
		}
		return index == -1?-1:--index;
	}
}
