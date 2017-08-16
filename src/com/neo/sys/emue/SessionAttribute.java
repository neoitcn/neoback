package com.neo.sys.emue;

public class SessionAttribute {

	/**
	 * 当前用户
	 */
	public final static String CURRENT_USER = "CURRENT_USER";
	
	/**
	 * 当前用户的所有菜单
	 */
	public final static String CURRENT_USER_MENU = "CURRENT_USER_MENU";

	/**
	 * 用户当前所在的机构，有可能为空。
	 */
	public final static String USER_CURRENT_OFFICE="USER_CURRENT_OFFICE";

	/**
	 *用户的所有上级机构，
	 */
	public final static String USER_PARENT_OFFICE="USER_PARENT_OFFICE";

	/**
	 * 用户的所有下级机构
	 */
	public final static String USER_CHILD_OFFICE="USER_CHILD_OFFICE";

	
	/**
	 * 当前用户需要输入的验证码信息
	 */
	public final static String VALIDATE_CODE = "VALIDATE_CODE";
	
	
	/**
	 * 当前用户的验证码图片真实地址
	 */
	public final static String VALIDATE_CODE_ADDRESS = "VALIDATE_CODE_ADDRESS";
	
	/**
	 * 是否需要验证码登录
	 */
	public final static String ISNEED_VALIDATE_CODE = "ISNEED_VALIDATE_CODE";
	
	public final static String disneed_validate_code = "0";
	
	public final static String need_validate_code = "1";
	
	
	/**
	 * 验证码是否处于生成状态，如果是，则不执行重新生成验证码操作
	 */
	public final static String IS_CODE_CREATING = "IS_CODE_CREATING";
	
	public final static String code_creating = "CODE_CREATING";
	
	/**
	 *用户防止表单重复提交 
	 */
	public final static String TOKEN = "token";
	
	/**
	 * 用于当用户提交后台出现错误时，回显token，以便再次提交。
	 * 控制范围仅在ajax提交表单中
	 */
	public final static String RETOKEN = "afterToken";

	/**
	 * 用户登录的时间
	 */
	public static final String LOGIN_TIME = "login_time";
	
	/**
	 * 上一次前台向后台请求警告数据的时间
	 */
	public static final String RECORD_WARN_TIME = "record_warn_time";
}
