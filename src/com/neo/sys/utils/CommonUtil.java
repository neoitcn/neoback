package com.neo.sys.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;


public class CommonUtil<K,V> {


    /**
     * 将List转为Map，key为List中类型的某一个属性名的值，value为该对象
     * @param fieldName
     * @param value
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K,V>Map<K,V> listToMap(String fieldName, Object value){
        Map<K,V> map = new HashMap<>();
        if(value == null){
            return map;
        }
        if(!(value instanceof List)){
            return map;
        }
        List<V> list = (List<V>) value;
        if(list.isEmpty()){
            return map;
        }
        try {
            for(V v:list){
                String methodName = "get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
                Object fieldValue = InvokeUtil.invokeMethod(v,methodName,null);
                map.put((K) fieldValue,v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }

    /**
     * 将List转为Map，key为List中类型的某一个属性名的值，value为该对象，p为过滤条件
     * @param fieldName
     * @param value
     * @param p
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K,V>Map<K,V> listToMap(String fieldName, Object value,Predicate<V> p){
        Map<K,V> map = new HashMap<>();
        if(value == null){
            return map;
        }
        if(!(value instanceof List)){
            return map;
        }
        List<V> list = (List<V>) value;
        if(list.isEmpty()){
            return map;
        }
        try {
            for(V v:list){
                if(p!=null){
                    if(!p.test(v)){
                        continue;
                    }
                }
                String methodName = "get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
                Object fieldValue = InvokeUtil.invokeMethod(v,methodName,null);
                map.put((K) fieldValue,v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }
}
