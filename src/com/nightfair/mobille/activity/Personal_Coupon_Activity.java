package com.nightfair.mobille.activity;

import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.util.ToastUtil;

import android.os.Bundle;

public class Personal_Coupon_Activity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal__coupon);
		ToastUtil.showCenter(this, "当前登录用户id" + BaseApplication.userid);
	}

	@Override
	public void finish() {
		super.finish();
	}
}
