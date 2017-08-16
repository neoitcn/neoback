package com.neo.sys.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.neo.sys.entity.JqGridPage;
import com.neo.sys.entity.Page;
import com.neo.sys.annotation.ToDate;
import com.neo.sys.emue.DateFormatType;
import com.neo.sys.listener.AccessInterface;
import com.neo.sys.listener.JqGridConverter;

/**
 * 用于ajax返回请求
 *
 */
public class JsonDataUtils {

	/**
	 * 创建一个用于返回给前台的JSON对象，该对象由SpringMVC负责管理转换
	 * @param success
	 * @param message
	 * @param data
	 * @return
	 */
	public static Map<String, Object> createJSONMessage(boolean success, String message, Object data) {
		Map<String, Object> result = new HashMap<>();
		result.put("success", success);
		result.put("message", message);
		result.put("obj", data);
		return result;
	}
	
	/**
	 * 创建一个用于返回给前台的JSON字符串，由response对象负责写入到前台。
	 * @param success
	 * @param message
	 * @param data
	 * @return
	 */
	public static void writeJSONMessage(ServletResponse response,boolean success,String message,Object data){
		Map<String, Object> result = new HashMap<>();
		result.put("success", success);
		result.put("message", message);
		result.put("obj", data);
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("Content-Type:application/json");
			new ObjectMapper().writeValue(response.getOutputStream(), result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用于手机
	 * @author 姬燕萍
	 * @param isSuccess
	 * @param message
	 * @param data
	 * @return
	 */
	public static Map<String, Object> createHttpJSONMessage(String isSuccess, String message, Object data) {
		Map<String, Object> result = new HashMap<>();
		result.put("result", isSuccess);
		result.put("msg", message);
		result.put("obj", data);
		return result;
	}

	@Deprecated
	public static JqGridPage createJqGridPage(List<Map<String,Object>> listResult) throws Exception {
		JqGridPage jqpage = new JqGridPage<>();
		jqpage.setRows(listResult);
//		jqpage.setTotal(0L);
//		jqpage.setRecords(0L);
		return jqpage;
	}
	/**
	 * 创建一个JSON分页对象，
	 * 
	 * @param clazz
	 *            对应的service层的类
	 * @param page
	 *            分页信息
	 * 在手动实现获取总记录条数的时候使用它
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static JqGridPage createJqGridPage(Class clazz, Page page) throws Exception {

		String className = clazz.getSimpleName();

		if (!className.endsWith("Service")) {
			className = clazz.getSimpleName() + "Service";
		}

		className = className.substring(0, 1).toLowerCase() + className.substring(1);

		Long count = InvokeUtil.invokeCount(className, page.getParam());
		if (count == null) {
			count = 0L;
		}
		Long currentPageIndex = page.getPage();
		Integer rows = page.getRows();
		String orderBy = page.getSort();
		if (!"DESC".equals(orderBy) && !"ASC".equals(orderBy)) {
			page.setSort(null);
		}
		JqGridPage jqpage = new JqGridPage<>();
		jqpage.setPage(currentPageIndex);
		jqpage.setRecords(count);
		if (count == 0) {
			jqpage.setTotal(1L);
		} else {
			jqpage.setTotal(Long.valueOf((count - 1) / rows + 1));
		}
		return jqpage;
	}

	/**
	 * 在比如Code封装类中出现了多个get*Count方法，则可以指定自己需要的那个方法获取记录总条数。
	 * 在手动实现获取总记录条数的时候使用它
	 * @param clazz
	 * @param countMethod
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static JqGridPage createJqGridPage(Class clazz, String countMethod, Page page) throws Exception {

		String className = clazz.getSimpleName();

		if (!className.endsWith("Service")) {
			className = clazz.getSimpleName() + "Service";
		}

		className = className.substring(0, 1).toLowerCase() + className.substring(1);

		Long count = InvokeUtil.invokeCount(className, countMethod, page.getParam());
		if (count == null) {
			count = 0L;
		}
		Long currentPageIndex = page.getPage();
		Integer rows = page.getRows();
		String orderBy = page.getSort();
		if (!"DESC".equals(orderBy) && !"ASC".equals(orderBy)) {
			page.setSort(null);
		}
		JqGridPage jqpage = new JqGridPage<>();
		jqpage.setPage(currentPageIndex);
		jqpage.setRecords(count);
		if (count == 0) {
			jqpage.setTotal(1L);
		} else {
			jqpage.setTotal(Long.valueOf((count - 1) / rows + 1));
		}
		return jqpage;
	}

	/**
	 * 创建jqGrid的结果集，fields中，如果需要修改属性的别名， 如果前台名称为name，后台名称为user_name，则可以写为
	 * name=user_name
	 * 如果传入的field不存在，将会根据jqc转换器创建一个属性到jqgrid中，如果jqc不存在，则存入一个空字符串
	 * jqc的id值为后台属性名，
	 * 
	 * @param listResult
	 * @param ifNullContinue
	 *            如果获取到的属性为空则跳过，true跳过，false不跳过
	 * @param jqc
	 * @param fields
	 * @return
	 */
	public synchronized static List<Map<String, Object>> createJqGridResult(List<?> listResult, boolean ifNullContinue,JqGridConverter jqc,
			String... fields) {
		return createJqGridResult(listResult,ifNullContinue,jqc,null,fields);
	}
	/**
	 * 创建jqGrid的结果集，fields中，如果需要修改属性的别名， 如果前台名称为name，后台名称为user_name，则可以写为
	 * name=user_name
	 * 如果传入的field不存在，将会根据jqc转换器创建一个属性到jqgrid中，如果jqc不存在，则存入一个空字符串
	 * jqc的id值为后台属性名，
	 *
	 * @param listResult
	 * @param ifNullContinue
	 *            如果获取到的属性为空则跳过，true跳过，false不跳过
	 * @param jqc
	 * @param fields
	 * @return
	 */
	public synchronized static List<Map<String, Object>> createJqGridResult(List<?> listResult, boolean ifNullContinue, JqGridConverter jqc,
			AccessInterface p, String... fields) {

		if (listResult == null || listResult.isEmpty()) {
			return null;
		}

		List<Map<String, Object>> list = new ArrayList<>();
		if (fields != null && fields.length > 0) {
			for (Object obj : listResult) {

				if(p!=null){
					if(!p.test(obj)){
						continue;
					}
				}

				Map<String, Object> map = new HashMap<>();
				for (String filed : fields) {
					if (StringUtils.isNotBlank(filed)) {
						String ar[];
						if (filed.contains("=")) {
							ar = filed.trim().split("=");
						} else {
							ar = new String[2];
							ar[1] = filed.trim();
						}
						String f = "get" + ar[1].substring(0, 1).toUpperCase() + ar[1].substring(1);
						try {
							Method m = getMethod(obj.getClass(),f, null);
							if(m == null){
								throw new NoSuchMethodException(f+"：没有找到方法，属性将被新建");
							}
							Class<?> clazz = m.getReturnType();
							Object re = m.invoke(obj, null);
							if (ifNullContinue && re == null) {
								continue;
							}
							Object resultObj = null;
							if(jqc != null){
								resultObj = jqc.convert(ar[1],re,obj);
							}
							if(resultObj == null){
								if (clazz.equals(java.util.Date.class)) {
									Field fi = getFiledByName(obj.getClass(), ar[1]);
									boolean b = true;
									if (fi != null) {
										ToDate d = fi.getAnnotation(ToDate.class);
										if (d != null) {
											if (d.value() == DateFormatType.TIME) {
												map.put(StringUtils.isBlank(ar[0]) ? ar[1] : ar[0],
														FormatUtil.formatTime((java.util.Date) re));
												b = false;
											}
										}
									}
									if (b)
										map.put(StringUtils.isBlank(ar[0]) ? ar[1] : ar[0], FormatUtil.formatDate((java.util.Date) re));
								} else if (clazz.equals(java.lang.String.class)) {
									map.put(StringUtils.isBlank(ar[0]) ? ar[1] : ar[0], re == null ? "" : re);
								} else {
									map.put(StringUtils.isBlank(ar[0]) ? ar[1] : ar[0], re);
								}
							}else{
								map.put(StringUtils.isBlank(ar[0]) ? ar[1] : ar[0], resultObj);
							}
						}catch (NoSuchMethodException e) {
							Object re = null;
							if(jqc != null){
								re = jqc.convert(filed, null, obj);
							}
							map.put(StringUtils.isBlank(ar[0]) ? ar[1] : ar[0], re==null?"":re);
							System.out.println("jsonDataUtils自动封装问题："+e.getMessage());
						} catch(Exception e){
							e.printStackTrace();
						}
					}
				}
				list.add(map);
			}
		} else {

			Class<?> clazz = null;

			List<Field> listField = null;

			for (Object obj : listResult) {

				if (obj == null) {
					continue;
				}

				if(p!=null){
					if(!p.test(obj)){
						continue;
					}
				}

				if (clazz == null) {
					clazz = obj.getClass();
					listField = getAllFileds(obj.getClass());
				} else {
					if (!clazz.equals(obj.getClass())) {
						listField = getAllFileds(obj.getClass());
						clazz = obj.getClass();
					}
				}

				Map<String, Object> map = new HashMap<>();
				for (Field filed : listField) {
					if (filed != null) {
						String f = "get" + filed.getName().substring(0, 1).toUpperCase() + filed.getName().substring(1);
						try {
							Method m = obj.getClass().getMethod(f, null);
							Class<?> cla = m.getReturnType();
							Object re = m.invoke(obj, null);
							if (ifNullContinue && re == null) {
								continue;
							}
							Object resultObj = null;
							if (jqc != null) {
								resultObj = jqc.convert(filed.getName(), re,obj);
							}
							if (resultObj == null) {
								if (cla.equals(java.util.Date.class)) {
									boolean b = true;
									ToDate d = filed.getAnnotation(ToDate.class);
									if (d != null) {
										if (d.value() == DateFormatType.TIME) {
											map.put(filed.getName(), FormatUtil.formatTime((java.util.Date) re));
											b = false;
										}
									}
									if (b)
										map.put(filed.getName(), FormatUtil.formatDate((java.util.Date) re));
								} else if (cla.equals(java.lang.String.class)) {
									map.put(filed.getName(), re == null ? "" : re);
								} else {
									map.put(filed.getName(), re);
								}
							} else {
								map.put(filed.getName(), resultObj);
							}
						}catch(Exception e){
							System.out.println("jsonDataUtils自动封装问题："+e.getMessage());
						}
					}
				}
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 获取该类的所有属性，包括私有，继承，默认和公共，还包括它所继承来的所有属性。
	 * 当子类重写了父类的属性，则二者都能获取到。List中，父类的属性永远排在子类的后面。
	 * @param clazz
	 * @return
	 */
	private static List<Field> getAllFileds(Class<?> clazz) {

		List<Field> listField = new ArrayList<>();

		if (clazz != null) {
			Field field[] = clazz.getDeclaredFields();

			if (field != null) {
				for (Field f : field) {
					listField.add(f);
				}
			}

			listField.addAll(getAllFileds(clazz.getSuperclass()));
		}

		return listField;
	}

	/**
	 * 根据属性名获取属性。
	 * 如果子类重写了父类的属性名，则只会获取子类的属性名。
	 * @param clazz
	 * @param name
	 * @return
	 */
	private static Field getFiledByName(Class<?> clazz, String name) {

		Field f = null;

		if (clazz != null) {

			try {
				f = clazz.getDeclaredField(name);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (f == null) {
				f = getFiledByName(clazz.getSuperclass(), name);
			}

		}

		return f;
	}
	
	/**
	 * 根据方法名和参数获取该方法
	 * 
	 * @param clazz
	 * @param name
	 * @param param
	 * @return
	 */
	private static Method getMethod(Class<?> clazz,String name,Object... param){
		Class<?> arr[] = null;
		if(param!=null){
			arr = new Class<?>[param.length];
			for(int i=0;i<arr.length;i++){
				Object o = param[i];
				if(o != null){
					arr[i] = o.getClass();
				}
			}
		}
		Method method = _getMethod(clazz,name,arr);
		return method;
	}
	private static Method _getMethod(Class<?> clazz,String name,Class<?>... paramType){
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(name, paramType);
			if(method == null){
				method = _getMethod(clazz.getSuperclass(),name,paramType);
			}
		} catch (Exception e) {
			System.err.println("自动封装出错，方法不存在");
		}
		return method;
	}
}
