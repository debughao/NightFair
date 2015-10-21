package com.nightfair.mobille.activity;

import com.nightfair.mobille.R;
import com.nightfair.mobille.util.ActivityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class SettingActivity extends Activity {
	private TextView tv_title;
	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ActivityUtils.setActionBarByColor(this, R.layout.title_bar, R.color.title_color);
		initView();
	}

	private void initView() {

		tv_title = (TextView) findViewById(R.id.tv_actionbar_title);
		tv_title.setText(R.string.actionbar_title_setting);
		iv_back = (ImageView) findViewById(R.id.iv_actionbar_back);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
