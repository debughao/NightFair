package com.nightfair.mobille.activity;

import com.nightfair.mobille.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class ShopDetailActivity extends Activity {

	private String seller_id;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_detail);
		Intent  intent =getIntent();
		seller_id = intent.getStringExtra("seller_id");
		tv=(TextView)findViewById(R.id.tv_sellerid);
		tv.setText("获得的卖家id"+seller_id);
	}


}
