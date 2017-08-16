package com.neo.sys.listener;

public interface JqGridConverter {

	/**
	 * 某些字段需要使用自定义方式去生成值到前台。
	 * 此时可以使用该方法进行转换。
	 * 需要注意的是，该方法的优先级要高于带有ToDate注解的转换器。
	 * 如果该方法返回null，转换器将认为用户没有对该字段定义转换，将执行默认的转换格式。
	 * 默认转换格式：Date:转为日期类型，String：如果为null则返回空字符串。否则直接返回该值。
	 * @param id 属性名
	 * @param value 属性值
	 * @param object 整个对象
	 * @return 返回转换后的结果
	 */
	public Object convert(String id,Object value,Object object);
	
}
