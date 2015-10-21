package com.nightfair.mobille.activity;

import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.VersionUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class AboutActivity extends BaseActivity {

	private TextView tv_title;
	private ImageView iv_back;
	private TextView tv_version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		ActivityUtils.setActionBarByColor(this, R.layout.title_bar, R.color.title_color);		
		initView();
	}

	private void initView() {
		tv_version=(TextView) findViewById(R.id.tv_about_version);
		tv_version.setText(getString(R.string.text_about_version)+VersionUtil.getVersionName(this));
		tv_title=(TextView) findViewById(R.id.tv_actionbar_title);
		tv_title.setText(R.string.actionbar_title_about);
		iv_back=(ImageView) findViewById(R.id.iv_actionbar_back);
		iv_back.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
	}
 
}
