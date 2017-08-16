package com.neo.sys.web;

import com.neo.sys.emue.SessionAttribute;
import com.neo.sys.entity.JqGridPage;
import com.neo.sys.entity.Page;
import com.neo.sys.listener.AccessInterface;
import com.neo.sys.listener.JqGridConverter;
import com.neo.sys.utils.JsonDataUtils;
import com.neo.sys.utils.ListPageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class BaseController {

	@Autowired
	protected HttpSession session;

	protected final static Map<String,Object> SUCCESS = JsonDataUtils.createJSONMessage(true, null, null);
	protected final static Map<String,Object> FAILED = JsonDataUtils.createJSONMessage(false, null, null);


	protected final Map<String,Object> SUCCESS(Object backObj){
		return createJSONMessage(true,null,backObj);
	}

	protected final Map<String,Object> FAILED(String message){
		return createJSONMessage(false,message,null);
	}

	public Map<String,Object> createJSONMessage(boolean isSuccess,String message,Object data){
		return JsonDataUtils.createJSONMessage(isSuccess, message, data);
	}

	protected Map<String,Object> createHttpJSONMessage(String isSuccess,String message,Object data){
		return JsonDataUtils.createHttpJSONMessage(isSuccess, message, data);
	}

	/**
	 * 创建一个分页对象，其中，listResult是从数据库中查询到的所有结果，page是分页对象，将返回一个过滤好的结果集
	 * @param page 分页对象
	 * @param originalList 原来的查询的对象
	 * @param p 过滤条件，那些结果会被过滤
	 * @param jqc 转换条件，根据属性结果集进行转换
	 * @param fields 需要被保留的属性
	 * @return 返回一个适用于easyui的表格结果集
	 */
	protected JqGridPage<?> createJqGridPage(Page page, List<?> originalList, AccessInterface<?> p,JqGridConverter jqc,String... fields){
		return ListPageHelper.createResult(page,originalList,jqc,p,fields);
	}

	/**
	 * 创建一个JSON分页对象，
	 * @param clazz 对应的service层的类
	 * @param page 分页信息
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	protected JqGridPage<?> createJqGridPage(Class<?> clazz,Page page) throws Exception{
		return JsonDataUtils.createJqGridPage(clazz, page);
	}
	
	/**
	 * 在比如Code封装类中出现了多个get*Count方法，则可以指定自己需要的那个方法获取记录总条数。
	 * @param clazz
	 * @param countMethod
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	protected JqGridPage<?> createJqGridPage(Class<?> clazz,String countMethod,Page page) throws Exception{
		return JsonDataUtils.createJqGridPage(clazz,countMethod, page);
	}
	
	
	/**
	 * 将查询结果直接绑定到结果集
	 * @param clazz
	 * @param page
	 * @param listResult
	 * @param fileds
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	protected JqGridPage<?> createJqGridPage(Class<?> clazz,Page page,List<?> listResult,JqGridConverter jqc,String ...fileds) throws Exception{
		JqGridPage<Map<String, Object>> j = JsonDataUtils.createJqGridPage(clazz, page);
		j.setRows(JsonDataUtils.createJqGridResult(listResult, true,jqc,fileds));
		return j;
	}
	
	/**
	 * 将查询结果直接绑定到结果集
	 * @param clazz
	 * @param countMethod
	 * @param page 
	 * @param listResult 已查询的结果集
	 * @param fileds 需要将哪些属性绑定到结果集，如果为空，则将所有的属性都绑定到结果集。某些属性的值为空，则这些属性不会被绑定到结果集。如果前台的属性名称和后台不一致，则可以使用后台属性名=前台属性名的方式为其起别名
	 * @return
			* @throws Exception
	 */
	@Deprecated
	protected JqGridPage<?> createJqGridPage(Class<?> clazz,String countMethod,Page page,List<?> listResult,JqGridConverter jqc,String ...fileds) throws Exception{
		JqGridPage<Map<String,Object>> j = JsonDataUtils.createJqGridPage(clazz,countMethod, page);
		j.setRows(JsonDataUtils.createJqGridResult(listResult,true,jqc, fileds));
		return j;
	}

//	@ModelAttribute()  
//    public void autoCreateProperty() throws Exception {
//		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
//    }  
}
