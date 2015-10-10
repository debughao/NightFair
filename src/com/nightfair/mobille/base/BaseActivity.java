package com.nightfair.mobille.base;

import com.nightfair.mobille.activity.SplashActivity;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activity-基类
 * 
 * @author tangqi
 * @data 2015年7月8日下午9:20:10
 */

public class BaseActivity extends Activity {
 public static int flag=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BaseApplication.getInstance().addActivity(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void finish() {
		SplashActivity.requestCode = flag;
		super.finish();
	}
}
