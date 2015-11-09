package com.nightfair.mobille.activity;

import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.util.ActivityUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class LookCouponNumActivity extends BaseActivity implements OnClickListener {
	protected Context mContext;
	private TextView mTv_title;
	private String coupon_name;
	private String coupon_num;
	private TextView tv_coupon_name,tv_coupon_num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_look_coupon_num);
		mContext = this;
		ActivityUtils.setActionBarLayout(getActionBar(), mContext, R.layout.title_bar_recharge);	
		Intent intent=getIntent();
		coupon_name=intent.getStringExtra("coupon_name");
		coupon_num=intent.getStringExtra("coupon_num");
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mTv_title = (TextView) findViewById(R.id.tv_recharge_back);
		mTv_title.setText(R.string.actionbar_title_coupon);
		mTv_title.setOnClickListener(this);
		tv_coupon_name=(TextView) findViewById(R.id.tv_coupon_name);
		tv_coupon_num=(TextView) findViewById(R.id.tv_coupon_num);
		 tv_coupon_name.setText(coupon_name+"优惠券");
		 tv_coupon_num.setText(coupon_num);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.tv_recharge_back:
			finish();
			break;
		default:
			break;
		}
	}

}
