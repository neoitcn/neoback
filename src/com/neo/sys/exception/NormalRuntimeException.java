package com.neo.sys.exception;

/**
 * 整个程序抛异常时，逻辑异常不应该抛Exception异常，而是抛出该异常，并写明异常信息
 * 系统错误将使用Exception异常，系统异常不向用户展示。
 * 系统异常信息将会被异常处理类屏蔽，并使用指定的固定信息返回到前台
 *
 */
public class NormalRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -898809991079993550L;

	public NormalRuntimeException(){}
	
	public NormalRuntimeException(String message){
		super(message);
	}
	
}
