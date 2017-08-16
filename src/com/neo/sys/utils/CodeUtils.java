package com.neo.sys.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neo.sys.entity.bean.Code;
import com.neo.sys.entity.bean.CodeValue;
import com.neo.sys.service.service.CodeService;

/**
 * 获取码表的辅助类
 * 
 */
public class CodeUtils {

	private static CodeService codeService = SpringUtils.getApplicationBean(CodeService.class);

	/**
	 * 根据码表的类型和值获取码表的名称
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static String getCodeName(String key, String value) {
		CodeValue codeValue = codeService.getCodeValue(key, value);
		if (codeValue != null) {
			return codeValue.getName();
		}
		return "";
	}

	/**
	 * 根据码表的key获取对应的码表的所有键值对
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static Map<String, String> getCodes(String key) {
		Code code = codeService.getCodeByKey(key);
		Map<String, String> map = new HashMap<String, String>();
		if (code != null) {

			List<CodeValue> list = code.getListValue();

			if (list != null) {
				for (CodeValue cv : list) {
					map.put(cv.getValue(), cv.getName());
				}
			}

		}
		return map;
	}

}
