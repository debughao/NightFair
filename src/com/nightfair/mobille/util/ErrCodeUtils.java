package com.nightfair.mobille.util;

public class ErrCodeUtils {
	/**
	 * 网路错误
	 */
	public final static String NORMAL_LOGIN_TIMEOUT = "非常抱歉，连接服务器失败，请稍后重试。";
	public final static String NORMAL_LOGIN_NONNETWORK = "当前网络不可用，请检查网络设置";
	/**
	 * 登录注册错误
	 */
	public final static String USERNAME_PASSWORD_ERROR = "用户名或者密码错误";
	public static final String ERROR_PHONE_FORMAT = "手机号码格式错误！";
	public static final String ERROR_PHONE_NULL = "手机号码不能为空！";
	public static final String ERROR_VERCODE_NULL = "验证码不能为空！";
	public static final String ERROR_VERCODE_FORMAT = "验证码为6位数字！";
	public static final String ERROR_VERCODE_ERROR = "验证码错误！";
	public static final String ERROR_PASSWORD_NULL = "密码不能为空！";
	public static final String ERROR_PASSWORD_LENGTH = "密码长度应为8-16位！";
	public static final String ERROR_PASSWORD_FORMAT = "密码至少包含数字、字母中的两种类型，不能含有其它字符！";
}
