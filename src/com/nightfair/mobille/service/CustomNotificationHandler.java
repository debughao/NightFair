package com.nightfair.mobille.service;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;
import com.umeng.common.message.Log;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

public class CustomNotificationHandler extends UmengNotificationClickHandler {
	
	private static final String TAG = CustomNotificationHandler.class.getName();
	
	@Override
	public void dismissNotification(Context context, UMessage msg) {
		Log.d(TAG, "dismissNotification");
		super.dismissNotification(context, msg);
		MobclickAgent.onEvent(context, "dismiss_notification");
	}
	
	@Override
	public void launchApp(Context context, UMessage msg) {
		Log.d(TAG, "launchApp");
		super.launchApp(context, msg);
		Map<String, String> map = new HashMap<String, String>();
		map.put("action", "launch_app");
		MobclickAgent.onEvent(context, "click_notification", map);
	}
	
	@Override
	public void openActivity(Context context, UMessage msg) {
		Log.d(TAG, "openActivity");
		super.openActivity(context, msg);
		if ((msg.activity == null) || (TextUtils.isEmpty(msg.activity.trim()))) {
			return;
		}
		Intent localIntent = new Intent();
		
		localIntent.setClassName(context, msg.activity);
		localIntent.addFlags(268435456);
		localIntent.putExtra("seller_id", msg.extra.get("seller_id").toString());
		localIntent.putExtra("url", msg.extra.get("url").toString());
		context.startActivity(localIntent);
		Map<String, String> map = new HashMap<String, String>();
		map.put("action", "open_activity");
		MobclickAgent.onEvent(context, "click_notification", map);
	}
	
	@Override
	public void openUrl(Context context, UMessage msg) {
		Log.d(TAG, "openUrl");
		super.openUrl(context, msg);
		Map<String, String> map = new HashMap<String, String>();
		map.put("action", "open_url");
		MobclickAgent.onEvent(context, "click_notification", map);
	}
	
	@Override
	public void dealWithCustomAction(Context context, UMessage msg) {
		Log.d(TAG, "dealWithCustomAction");
		super.dealWithCustomAction(context, msg);
		Map<String, String> map = new HashMap<String, String>();
		map.put("action", "custom_action");
		MobclickAgent.onEvent(context, "click_notification", map);
	}
	
	@Override
	public void autoUpdate(Context context, UMessage msg) {
		Log.d(TAG, "autoUpdate");
		super.autoUpdate(context, msg);
		Map<String, String> map = new HashMap<String, String>();
		map.put("action", "auto_update");
		MobclickAgent.onEvent(context, "click_notification", map);
	}

}
