package com.neo.sys.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于标识Controller的名称，名称对应数据库中的菜单controller_name字段，将通过该字段匹配执行权限过滤。
 * 当不指定value值时，则系统会默认将Controller的SimpleName作为它的值。
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerName {

	String value() default "";
	
	/**
	 * 某些方法希望不会被拦截，则将该值设置为false；
	 * 只有将此注解加在类上的时候，对某方法修饰时使用该值。
	 * @return
	 */
	boolean open() default true;
	
}
