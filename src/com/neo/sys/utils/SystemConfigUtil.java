package com.neo.sys.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.neo.sys.exception.NormalException;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ResourceUtils;

/**
 * 用于获取配置文件信息
 * 
 *
 */
public class SystemConfigUtil {

	/**
	 * 全局变量
	 */
	private static Map<String, String> pMap = new HashMap<>();

	/**
	 * 全局属性
	 */
	private static Map<String,Properties> pProMap = new HashMap<>();

	/**
	 * 属性文件的位置
	 */
	private static Map<String,String> pPath = new HashMap<>();
	
	static {
		try {
			File configFile = ResourceUtils.getFile("classpath:config.properties");
			if (configFile.exists()) {
				Properties configP = new Properties();
				FileInputStream fis = new FileInputStream(configFile);
				configP.load(fis);
				fis.close();
				String path = configP.getProperty("local");
				if (StringUtils.isNotBlank(path)) {
					String paths[] = path.split(";");
					if (paths != null) {
						for (String p : paths) {
							if (StringUtils.isNotBlank(p)) {
								File pFile = ResourceUtils.getFile("classpath:" + p + ".properties");
								fis = new FileInputStream(pFile);
								InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
								Properties pr = new Properties();
								pr.load(isr);
								fis.close();
								isr.close();
								pPath.put(p,pFile.getAbsolutePath());
								pProMap.put(p, pr);
								for (Object o : pr.keySet()) {
									pMap.put(p + "." + o, pr.getProperty((String) o));
								}
							}
						}
					}
				}
			} else {
				System.out.println("全局配置文件不存在");
			}
			System.out.println("---------------------->"+pMap);
		} catch (Exception e) {
			System.out.println("初始化配置文件失败，请检查后重启服务，否则会影响这个系统。");
			e.printStackTrace();
		}
	}

	public static String getConfig(String key) {
		return pMap.get(key);
	}

	public static Properties getConfigMap(String key) throws Exception{
		return pProMap.get(key);
	}
	
	public static Double getDoubleConfig(String key) {
		String p = getConfig(key);
		if (p != null)
			return Double.parseDouble(pMap.get(p));
		return null;
	}
	
	public static Integer getIntegerConfig(String key) {
		String p = getConfig(key);
		if (p != null)
			return Integer.parseInt(p);
		return null;
	}
	
	public static Long getLongConfig(String key) {
		String p = getConfig(key);
		if (p != null)
			return Long.parseLong(p);
		return null;
	}

	public synchronized static void saveConfig(String key,Properties p) throws Exception {
		String path = pPath.get(key);
		if(path == null){
			System.out.println("key指定的信息不存在");
			throw new NormalException("信息保存失败");
		}
		FileOutputStream fos = new FileOutputStream(path);
		OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
		p.store(osw,"admin config");
		osw.flush();
		osw.close();
		fos.flush();
		fos.close();
	}

}
