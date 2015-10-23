package com.nightfair.mobille.util;

import android.content.Context;
import android.widget.EditText;

public class StringUtils {
	private static String reg1 = "^.*[\\d]+.*$";// 数字
	private static String reg2 = "^.*[A-Za-z]+.*$/";// 字母
	private static String reg3 = "^.*[_@#%&^+-/*\\/\\]+.*$";// 字符

	public static String[] convertStrToArry(String str) {
		String[] strArray = null;
		strArray = str.split("-");
		return strArray;
	}

	public static int getPosition(String str1, String[] str2) {
		int n = 0;
		for (int i = 0; i < str2.length; i++) {
			if (str2[i].equals(str1)) {
				n = i;
				break;
			}
		}
		return n;
	}

	public static boolean isPhone(Context context, EditText et_phone) {
		boolean isPass = true;
		String phone = et_phone.getText().toString();
		if (phone.length() == 0) {
			ToastUtil.show(context, ErrCodeUtils.ERROR_PHONE_NULL);
			isPass = false;
		} else if (!phone.matches("^[1][3,5,7,8][0-9]{9}$")) {
			ToastUtil.show(context, ErrCodeUtils.ERROR_PHONE_FORMAT);
			isPass = false;
		}
		return isPass;
	}

	public static boolean isPassword(Context context, EditText et_password) {
		boolean isPass = true;
		String password = et_password.getText().toString();
		if (password.length() == 0) {
			ToastUtil.show(context, ErrCodeUtils.ERROR_PASSWORD_NULL);
			isPass = false;
		} else if (password.length() < 8 || password.length() > 16) {
			ToastUtil.show(context, ErrCodeUtils.ERROR_PASSWORD_LENGTH);
			isPass = false;
		} 
		return isPass;
	}

	public static boolean isVerCode(Context context, EditText et_code) {
		boolean isPass = true;
		String code = et_code.getText().toString();
		if (code.length() == 0) {
			ToastUtil.show(context, ErrCodeUtils.ERROR_VERCODE_NULL);
			isPass = false;
		} else if (code.length() != 6) {
			ToastUtil.show(context, ErrCodeUtils.ERROR_VERCODE_FORMAT);
			isPass = false;
		}
		return isPass;
	}
}
