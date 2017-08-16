package com.neo.sys.utils;

import java.util.HashMap;
import java.util.Map;


public class AppValidateUtil {
	
	private static Map<String,String> validateMap = new HashMap<String,String>();

	public static void putMap(String key,String value) {
		
		try {

			if (key== null) {
				throw new NullPointerException("验证码内部错误");
			}

			if (value == null) {
				throw new Exception("验证码内部错误");
			}

			validateMap.put(key, value);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		
		}
	}


	public static String  getMap(String key) {
		
		return validateMap.get(key);
	}
}
