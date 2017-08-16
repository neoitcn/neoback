package com.neo.sys.listener;

/**
 * Created by HJJ on 2017/2/10.
 */
public interface AccessInterface<T> {
    public boolean test(T t);
}