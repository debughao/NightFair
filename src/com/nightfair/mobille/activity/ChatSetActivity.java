package com.nightfair.mobille.activity;

import com.nightfair.mobille.R;
import com.nightfair.mobille.base.Activitybase;
import com.nightfair.mobille.util.SharePreferenceUtil;
import com.nightfair.mobille.util.ToastUtil;
import com.nightfair.mobille.view.SlideSwitchView;
import com.nightfair.mobille.view.SlideSwitchView.OnSwitchChangedListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class ChatSetActivity extends Activitybase implements OnClickListener,
		OnSwitchChangedListener {

	RelativeLayout rl_switch_notification, rl_switch_voice, rl_switch_vibrate,
			layout_blacklist;

	SlideSwitchView ss_notification, ss_voice, ss_vibrate;
	View view1, view2;

	
	SharePreferenceUtil mSharedUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_set);
		initActionbar(getString(R.string.text_chat_setting), 0);
		mSharedUtil = mApplication.getSpUtil();
		initView();
	}

	private void initView() {
		// 黑名单列表
		layout_blacklist = (RelativeLayout) findViewById(R.id.layout_blacklist);
		rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);
		rl_switch_voice = (RelativeLayout) findViewById(R.id.rl_switch_voice);
		rl_switch_vibrate = (RelativeLayout) findViewById(R.id.rl_switch_vibrate);
		rl_switch_notification.setOnClickListener(this);
		rl_switch_voice.setOnClickListener(this);
		rl_switch_vibrate.setOnClickListener(this);
		layout_blacklist.setOnClickListener(this);

		ss_notification = (SlideSwitchView) findViewById(R.id.ss_notification);
		ss_voice = (SlideSwitchView) findViewById(R.id.ss_voice);
		ss_vibrate = (SlideSwitchView) findViewById(R.id.ss_vibrate);

		ss_voice.setOnChangeListener(this);
		ss_notification.setOnChangeListener(this);
		ss_vibrate.setOnChangeListener(this);

		view1 = (View) findViewById(R.id.view1);
		view2 = (View) findViewById(R.id.view2);

		// 初始化
		/*boolean isAllowNotify = mSharedUtil.isAllowPushNotify();

		if (isAllowNotify) {
			ToastUtil.show(ChatSetActivity.this, "允许接收新消息");
		} else {
			ToastUtil.show(ChatSetActivity.this, "拒绝接收新消息");
		}
		
		boolean isAllowVoice = mSharedUtil.isAllowVoice();

		if (isAllowVoice) {
			//ToastUtil.show(ChatSetActivity.this, "开启声音");
		} else {
			ToastUtil.show(ChatSetActivity.this, "关闭声音");
		}
		
		boolean isAllowVibrate = mSharedUtil.isAllowVibrate();
		if (isAllowVibrate) {
			//ToastUtil.show(ChatSetActivity.this, "开启震动");
		} else {
			//ToastUtil.show(ChatSetActivity.this, "关闭震动");
		}*/
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_blacklist:// 启动到黑名单页面
			startAnimActivity(new Intent(this, BlackListActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void onSwitchChange(SlideSwitchView switchView, boolean isChecked) {
		switch (switchView.getId()) {
		case R.id.ss_notification:
			if (isChecked) {
				
				mSharedUtil.setPushNotifyEnable(true);
				rl_switch_vibrate.setVisibility(View.VISIBLE);
				rl_switch_voice.setVisibility(View.VISIBLE);
				view1.setVisibility(View.VISIBLE);
				view2.setVisibility(View.VISIBLE);
				ToastUtil.show(ChatSetActivity.this, "允许接收新消息");
			} else {
				mSharedUtil.setPushNotifyEnable(false);
				rl_switch_vibrate.setVisibility(View.GONE);
				rl_switch_voice.setVisibility(View.GONE);
				view1.setVisibility(View.GONE);
				view2.setVisibility(View.GONE);
				ToastUtil.show(ChatSetActivity.this, "拒绝接收新消息");
			}
			break;
		case R.id.ss_voice:
			if (isChecked) {
				mSharedUtil.setAllowVoiceEnable(true);
				ToastUtil.show(ChatSetActivity.this, "开启声音");
			} else {
				mSharedUtil.setAllowVoiceEnable(false);
				ToastUtil.show(ChatSetActivity.this, "关闭声音");
			}
			break;
		case R.id.ss_vibrate:
			if (isChecked) {
				mSharedUtil.setAllowVibrateEnable(true);
				ToastUtil.show(ChatSetActivity.this, "开启震动");
			} else {
				mSharedUtil.setAllowVibrateEnable(false);
				ToastUtil.show(ChatSetActivity.this, "关闭震动");
			}
			break;
		default:
			break;
		}
	}
}