package com.nightfair.mobille.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.util.ActivityUtils;

public class FoodsActivity extends BaseActivity {

	TextView textView;
	TextView actionbarTextView;
	ImageButton actionbarImageButton;
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index_foods);
		ActivityUtils.setActionBarByColor(this, R.layout.title_bar, R.color.title_color);
		View view=getActionBar().getCustomView();
		actionbarTextView=(TextView) view.findViewById(R.id.tv_actionbar_title);
		actionbarImageButton=(ImageButton) view.findViewById(R.id.iv_actionbar_back);
		textView=(TextView) findViewById(R.id.tv_food_details);
		Intent intent=getIntent();
		String text=intent.getStringExtra("text");
		actionbarTextView.setText(text);
		textView.setText(text);
		actionbarImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
}
