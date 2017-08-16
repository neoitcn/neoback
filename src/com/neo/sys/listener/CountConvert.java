package com.neo.sys.listener;


public interface CountConvert<T,R> {

    public R apply(boolean isFirst,Object fieldName,T lastValue,T currentValue,Object lastObj,Object currentObj);


    public default Object getOrDefault(Object t,Object def){
        return t==null?def:t;
    }

}
