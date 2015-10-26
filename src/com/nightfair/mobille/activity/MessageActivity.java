package com.nightfair.mobille.activity;

import com.nightfair.mobille.R;
import com.nightfair.mobille.util.ActivityUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageActivity extends Activity implements OnClickListener {
	protected Context mContext;
	private TextView mTv_title;
	private ImageView iv_back;
	private RelativeLayout rl_system,rl_recommended;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		ActivityUtils.setActionBarByColor(this, R.layout.title_bar, R.color.title_color);
		mContext = this;
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mTv_title = (TextView) findViewById(R.id.tv_actionbar_title);
		mTv_title.setText(R.string.actionbar_title_push);
		iv_back = (ImageView) findViewById(R.id.iv_actionbar_back);
		rl_system=(RelativeLayout) findViewById(R.id.rl_message_system);
		rl_recommended=(RelativeLayout) findViewById(R.id.rl_message_recommended);
		iv_back.setOnClickListener(this);
		rl_system.setOnClickListener(this);
		rl_recommended.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_actionbar_back:
			finish();
			break;
		case R.id.rl_message_system:
			ActivityUtils.startActivity(mContext, SystemMessageActivity.class);
			break;
		case R.id.rl_message_recommended:
			ActivityUtils.startActivity(mContext, RecommandMessageActivity.class);
			break;	
		default:
			break;
		}
	}

}
