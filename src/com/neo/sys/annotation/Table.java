package com.neo.sys.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解是对getResultByParam方法的支撑。
 * 从此版本后，getResultByParam由getAll()方法做支撑，配置文件中无需写getResultByParam方法，用户可以自定义查询的参数。
 * SeachParamHelper类将实现上述功能。

 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    /**
     * 描述此实体类对应的数据表的表名。
     * @return
     */
    public String value();
}
