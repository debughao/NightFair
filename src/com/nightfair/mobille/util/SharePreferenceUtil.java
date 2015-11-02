package com.nightfair.mobille.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 首选项管理
 * 
 * @ClassName: SharePreferenceUtil
 * @Description: TODO
 * @author smile
 * @date 2014-6-10 下午4:20:14
 */

@SuppressLint("CommitPrefEdits")
public class SharePreferenceUtil {

	private SharedPreferences mSharedPreferences;
	private static SharedPreferences.Editor editor;

	public SharePreferenceUtil(Context context, String name) {
		mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		editor = mSharedPreferences.edit();
	}

	private String SHARED_KEY_NOTIFY = "shared_key_notify";
	private String SHARED_KEY_VOICE = "shared_key_sound";
	private String SHARED_KEY_VIBRATE = "shared_key_vibrate";
	private String LOCATION_CITYNAME = "cityName";
	private String SHARED_KEY_LOCATION = "location";
	private String SHARED_KEY_PUSH = "shared_key_push";

	// 是否允许推送通知
	public boolean isAllowPushNotify() {
		return mSharedPreferences.getBoolean(SHARED_KEY_NOTIFY, true);
	}

	public void setPushNotifyEnable(boolean isChecked) {
		editor.putBoolean(SHARED_KEY_NOTIFY, isChecked);
		editor.commit();
	}

	// 允许声音
	public boolean isAllowVoice() {
		return mSharedPreferences.getBoolean(SHARED_KEY_VOICE, true);
	}

	public void setAllowVoiceEnable(boolean isChecked) {
		editor.putBoolean(SHARED_KEY_VOICE, isChecked);
		editor.commit();
	}

	// 允许震动
	public boolean isAllowVibrate() {
		return mSharedPreferences.getBoolean(SHARED_KEY_VIBRATE, true);
	}

	public void setAllowVibrateEnable(boolean isChecked) {
		editor.putBoolean(SHARED_KEY_VIBRATE, isChecked);
		editor.commit();
	}

	// 定位结果
	public String getLOCATION_CITYNAME() {
		return mSharedPreferences.getString(LOCATION_CITYNAME, null);
	}

	public void setLOCATION_CITYNAME(String cityName) {
		editor.putString(LOCATION_CITYNAME, cityName);
		editor.commit();
	}

	// 是否使用上一次
	public boolean isAllowLocation() {
		return mSharedPreferences.getBoolean(SHARED_KEY_LOCATION, false);
	}

	public void setAllowLocationEnable(boolean isChecked) {
		editor.putBoolean(SHARED_KEY_LOCATION, isChecked);
		editor.commit();
	}

	// 推送
	public boolean isAllowPush() {
		return mSharedPreferences.getBoolean(SHARED_KEY_PUSH, false);
	}

	public void setAllowPushEnable(boolean isChecked) {
		editor.putBoolean(SHARED_KEY_PUSH, isChecked);
		editor.commit();
	}
}
