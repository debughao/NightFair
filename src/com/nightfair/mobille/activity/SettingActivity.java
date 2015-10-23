package com.nightfair.mobille.activity;

import com.nightfair.mobille.R;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.NetUtils;
import com.nightfair.mobille.util.ToastUtil;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class SettingActivity extends Activity {
	private TextView tv_title;
	private ImageView iv_back;
	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ActivityUtils.setActionBarByColor(this, R.layout.title_bar, R.color.title_color);
		initView();
		mContext=this;
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
	private void checkUpgrade() {
		// TODO Auto-generated method stub
		if (NetUtils.isNetAvailable(mContext)) {
			UmengUpdateAgent.forceUpdate(mContext);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				@Override
				public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
					switch (updateStatus) {
					case UpdateStatus.Yes:
						ToastUtil.show(mContext, "软件有更新");
						break;

					case UpdateStatus.No:
						ToastUtil.show(mContext, "已是最新版本");
						break;

					case UpdateStatus.NoneWifi:
						ToastUtil.show(mContext, "没有wifi连接， 只在wifi下更新");
						break;

					case UpdateStatus.Timeout:
						ToastUtil.show(mContext, "连接超时");
						break;
					}
				}
			});
		}
	}
}
