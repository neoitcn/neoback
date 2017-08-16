package com.neo.sys.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class InvokeUtil {
	
	/**
	 * 调用获取记录总条数的方法
	 * @param clazz
	 */
	public static Long invokeCount(String className,Map<String,Object> param) throws Exception{
		
		Object obj = SpringUtils.getApplicationBean(className);
		
		
		Method []ms = obj.getClass().getMethods();
		
		Method method = null;
		
		if(ms!=null){
			for(Method m:ms){
				if(m!=null && m.getName().matches("get(.*)Count")){
					method = m;
					break;
				}
			}
		}
		
		if(method == null){
			throw new Exception("查找函数失败！");
		}
		
		Long size = (Long) method.invoke(obj, param);
		
		return size;
	}
	
	public static Long invokeCount(String className,String invokeMethodName,Map<String,Object> param) throws Exception{
		
		Object obj = SpringUtils.getApplicationBean(className);
		
		
		Method []ms = obj.getClass().getMethods();
		
		Method method = null;
		
		if(ms!=null){
			for(Method m:ms){
				if(m!=null && m.getName().equals(invokeMethodName)){
					method = m;
					break;
				}
			}
		}
		
		if(method == null){
			throw new Exception("查找函数失败！");
		}
		
		Long size = (Long) method.invoke(obj, param);
		
		return size;
	}
	
	/**
	 * 执行对象的公共方法
	 * @param obj
	 * @param methodName
	 * @param param
	 * @return
	 */
	public static Object invokeMethod(Object obj, String methodName, Object... param) throws Exception{
		if (obj == null || methodName == null) {
			return null;
		}
		Class<?> clazz = obj.getClass();
		Method ms[] = clazz.getMethods();
		if (ms != null) {

			out: for (Method m : ms) {
				if (m.getName().equals(methodName)) {
					Class<?> mcla[] = m.getParameterTypes();
					if ((mcla == null || mcla.length == 0) && (param == null || param.length == 0)) {
						try {
							return m.invoke(obj, null);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
							throw e;
						}
					} else if (mcla.length == param.length) {

						for (int i = 0; i < mcla.length; i++) {
							Object o = param[i];
							if (o != null) {
								Class c = mcla[i];
								switch(c.getName()){
								case "int":
									c = Integer.class;
									break;
								case "double":
									c = Double.class;
									break;
								case "byte":
									c = Byte.class;
									break;
								case "long":
									c = Long.class;
									break;
								case "short":
									c = Short.class;
									break;
								case "float":
									c = Float.class;
									break;
								case "char":
									c = Character.class;
									break;
								case "boolean":
									c = Boolean.class;
									break;
								}
								if (!o.getClass().equals(c)) {
									continue out;
								}
							}
						}
						try {
							return m.invoke(obj, param);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
							throw e;
						}
					}

				}

			}

		}
		return null;
	}

}
