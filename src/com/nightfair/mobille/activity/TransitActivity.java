package com.nightfair.mobille.activity;

import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.util.ActivityUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TransitActivity extends BaseActivity {

	TextView textView;
	
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index_transit);
		ActivityUtils.setActionBarByColor(this, R.layout.title_bar,
				R.color.title_color);
		textView = (TextView) findViewById(R.id.tv_transit_details);
		Intent intent = getIntent();
		String text = intent.getStringExtra("text");
		textView.setText(text);
	}

}
