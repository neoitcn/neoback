package com.neo.sys.exception;

import org.springframework.validation.BindingResult;

/**
 * 将所有验证框架不通过的情况应该通过该异常来抛出。
 */
public class ValidateException extends RuntimeException{

    /**
     * 保存验证失败结果，在必要时向前台抛出
     */
    private BindingResult bindingResult;

    public ValidateException(){
        super("验证失败");
    }

    public ValidateException(BindingResult bindingResult){
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
