package com.nightfair.mobille.activity;

import com.nightfair.mobille.R;
import com.nightfair.mobille.util.ActivityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;


public class ShopDetailActivity extends Activity implements OnClickListener {
	private Context mContext;
	private String seller_id;
	private TextView tv_coupon_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_detail);
		mContext = this;
		Intent  intent =getIntent();
		seller_id = intent.getStringExtra("seller_id");
		
	ActivityUtils.setActionBarLayout(getActionBar(), mContext, R.layout.title_bar_coupon);
	inintView();
	}
	private void inintView() {
		// TODO Auto-generated method stub
		tv_coupon_back = (TextView) findViewById(R.id.tv_coupon_back);
		tv_coupon_back.setText("商家详情");
		tv_coupon_back.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
