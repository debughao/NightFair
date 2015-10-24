package com.nightfair.mobille.activity;

import java.io.File;

import com.nightfair.mobille.R;
import com.nightfair.mobille.config.FilePathConfig;
import com.nightfair.mobille.dialog.BaseDialog.OnConfirmListener;
import com.nightfair.mobille.dialog.LoadingDialog;
import com.nightfair.mobille.dialog.SelectionDialog;
import com.nightfair.mobille.task.FileCalculateAsyncTask;
import com.nightfair.mobille.task.FileDeleteAsyncTask;
import com.nightfair.mobille.task.OnResponseListener;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.FileUtils;
import com.nightfair.mobille.util.NetUtils;
import com.nightfair.mobille.util.ToastUtil;
import com.nightfair.mobille.util.VersionUtil;
import com.nightfair.mobille.view.SlideSwitchView;
import com.nightfair.mobille.view.SlideSwitchView.OnSwitchChangedListener;
import com.umeng.message.PushAgent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class SettingActivity extends Activity implements OnClickListener,OnSwitchChangedListener {
	/**
	 * SD卡缓存大小
	 */
	private long mExternCacheSize;

	/**
	 * 外部缓存目录
	 */
	private File mExternalCacheFile;

	/**
	 * 缓存数据View
	 */
	
	private TextView mTv_title, mTvVersionName,mTvCacheSize;
	private ImageView iv_back;
	protected Context mContext;
	private RelativeLayout rl_update, rl_cache, rl_photo, rl_push;
	private SlideSwitchView mSs_Location,mSs_Push;
	private PushAgent mPushAgent;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ActivityUtils.setActionBarByColor(this, R.layout.title_bar, R.color.title_color);
		initView();
		mContext = this;
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		updateData();
	}

	private void initView() {

		mTv_title = (TextView) findViewById(R.id.tv_actionbar_title);
		mTv_title.setText(R.string.actionbar_title_setting);
		iv_back = (ImageView) findViewById(R.id.iv_actionbar_back);
		rl_update = (RelativeLayout) findViewById(R.id.rl_setting_update);
		rl_cache = (RelativeLayout) findViewById(R.id.rl_setting_remove_photo);
		rl_photo = (RelativeLayout) findViewById(R.id.rl_setting_wifi_photo);
		rl_push = (RelativeLayout) findViewById(R.id.rl_setting_push);
		mTvVersionName = (TextView) findViewById(R.id.tv_setting_version);
		mSs_Location=(SlideSwitchView) findViewById(R.id.ss_location);
		mSs_Location.setOnChangeListener(this);
		mSs_Push=(SlideSwitchView) findViewById(R.id.ss_push);
		mSs_Push.setOnChangeListener(this);
		mTvVersionName.setText(getString(R.string.text_setttings_now_version) + VersionUtil.getVersionName(this));
		mySetOnClickListener(iv_back, rl_update, rl_cache, rl_photo, rl_push);
	}

	private void mySetOnClickListener(View... v) {
		for (View view : v) {
			view.setOnClickListener(this);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_actionbar_back:
			finish();
			break;
		case R.id.rl_setting_update:
			checkUpgrade();
			break;
		case R.id.rl_setting_remove_photo:
			clearCache(); 
			break;
		case R.id.rl_setting_wifi_photo:
			ToastUtil.show(mContext, "点击了2");
			break;
		case R.id.rl_setting_location:
			ToastUtil.show(mContext, "点击了3");
			break;
		case R.id.rl_setting_push:
			ToastUtil.show(mContext, "点击了4");
			break;
		default:
			break;
		}

	}
	@Override
	public void onSwitchChange(SlideSwitchView switchView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (switchView.getId()) {
		case R.id.ss_location:
			if(isChecked){
				ToastUtil.show(mContext, "位置开关被打开");
			}else {
				ToastUtil.show(mContext, "位置开关被关闭");
			}
			break;
		case R.id.ss_push:
			if(isChecked){
				mPushAgent.enable();
				ToastUtil.show(mContext, "推送开关被打开");
			}else {
				mPushAgent.disable();
				ToastUtil.show(mContext, "推送开关被关闭");
			}
			break;
		default:
			break;
		}
		
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

	/**
	 * 更新数据
	 */
	private void updateData() {
		

		FileCalculateAsyncTask task = new FileCalculateAsyncTask(this);
		mExternalCacheFile = new File(FilePathConfig.getExternalCachePath(SettingActivity.this));
		task.execute(mExternalCacheFile);
		task.setOnResponseListener(new OnResponseListener() {

			@Override
			public void onResponse(String resultString) {
				
				try {
					mExternCacheSize = Long.valueOf(resultString);
					mTvCacheSize.setText(FileUtils.formatSize(mExternCacheSize));
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 清除缓存
	 */
	private void clearCache() {
		// TODO Auto-generated method stub
		SelectionDialog dialog = new SelectionDialog(this, "确定清除" + FileUtils.formatSize(mExternCacheSize) + "缓存吗？");
		dialog.setOnConfirmListener(new OnConfirmListener() {

			@Override
			public void onConfirm(String result) {
				// TODO Auto-generated method stub

				final LoadingDialog dialog = new LoadingDialog(mContext, "正在清理缓存");
				dialog.show();
				
				FileDeleteAsyncTask task = new FileDeleteAsyncTask(mContext);
				task.execute(mExternalCacheFile);
				task.setOnResponseListener(new OnResponseListener() {

					@Override
					public void onResponse(String resultString) {

						dialog.dismiss();
						ToastUtil.showCenter(mContext, "清理成功");
						updateData();
					}
				});
			}
		});
		dialog.show();
	}

}
