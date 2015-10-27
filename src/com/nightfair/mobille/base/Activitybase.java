package com.nightfair.mobille.base;

import java.util.List;

import com.nightfair.mobille.R;
import com.nightfair.mobille.activity.LoginActivity;
import com.nightfair.mobille.bean.User;
import com.nightfair.mobille.dialog.DiaLogTips;
import com.nightfair.mobille.dialog.DiaLogTips.DialogTips;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.CollectionUtils;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Activity-基类
 * 
 * @author  zhanghao
 * @data 2015年10月20日16:41:30
 */

public class Activitybase extends BaseActivity {
	public BmobUserManager userManager;
	public BmobChatManager manager;
	public BaseApplication mApplication;
	protected int mScreenWidth;
	protected int mScreenHeight;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userManager = BmobUserManager.getInstance(this);
		manager = BmobChatManager.getInstance(this);
		mApplication = BaseApplication.getInstance();
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		MobclickAgent.onResume(this);
	}

	@Override
	public void finish() {
	
		super.finish();
	}


	/**
	 * 隐藏软键盘
	 */
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) this
				.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 显示下线的对话框
	 */
	public void showOfflineDialog(final Context context) {
		DiaLogTips base = new DiaLogTips();
		DialogTips dialog = base.new DialogTips(this, "您的账号已在其他设备上登录!", "重新登录");
		// 设置成功事件
		dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int userId) {
				BaseApplication.getInstance().logout();
				startActivity(new Intent(context, LoginActivity.class));
				finish();
				dialogInterface.dismiss();
			}
		});
		// 显示确认对话框
		dialog.show();
		dialog = null;
	}
	Toast mToast;

	public void ShowToast(final String text) {
		if (!TextUtils.isEmpty(text)) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (mToast == null) {
						mToast = Toast.makeText(getApplicationContext(), text,
								Toast.LENGTH_LONG);
					} else {
						mToast.setText(text);
					}
					mToast.show();
				}
			});

		}
	}

	public void ShowToast(final int resId) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (mToast == null) {
					mToast = Toast.makeText(
							Activitybase.this.getApplicationContext(), resId,
							Toast.LENGTH_LONG);
				} else {
					mToast.setText(resId);
				}
				mToast.show();
			}
		});
	}

	/**
	 * 打Log
	 */
	public void ShowLog(String msg) {
		Log.i("life", msg);
	}
	public void startAnimActivity(Class<?> cla) {
		this.startActivity(new Intent(this, cla));
	}

	public void startAnimActivity(Intent intent) {
		this.startActivity(intent);
	}

	/**
	 * 用于登陆或者自动登陆情况下的用户资料及好友资料的检测更新
	 */
	public void updateUserInfos() {
		// 更新地理位置信息
		updateUserLocation();
		// 查询该用户的好友列表(这个好友列表是去除黑名单用户的哦),目前支持的查询好友个数为100，如需修改请在调用这个方法前设置BmobConfig.LIMIT_CONTACTS即可。
		// 这里默认采取的是登陆成功之后即将好于列表存储到数据库中，并更新到当前内存中,
		userManager.queryCurrentContactList(new FindListener<BmobChatUser>() {

			@Override
			public void onError(int arg0, String arg1) {
				if (arg0 == BmobConfig.CODE_COMMON_NONE) {
					ShowLog(arg1);
				} else {
					ShowLog("查询好友列表失败：" + arg1);
				}
			}

			@Override
			public void onSuccess(List<BmobChatUser> arg0) {
				// 保存到application中方便比较
			BaseApplication.getInstance().setContactList(
					CollectionUtils.list2map(arg0));
			}
		});
	}

	/**
	 * 更新用户的经纬度信息
	 */
	public void updateUserLocation() {
		if (BaseApplication.lastPoint != null) {
			String saveLatitude = mApplication.getLatitude();
			String saveLongtitude = mApplication.getLongtitude();
			String newLat = String.valueOf(BaseApplication.lastPoint
					.getLatitude());
			String newLong = String.valueOf(BaseApplication.lastPoint
					.getLongitude());
			if (!saveLatitude.equals(newLat) || !saveLongtitude.equals(newLong)) {
				// 只有位置有变化就更新当前位置，达到实时更新的目的
				User u = (User) userManager.getCurrentUser(User.class);
				final User user = new User();
				user.setLocation(BaseApplication.lastPoint);
				user.setObjectId(u.getObjectId());
				user.update(this, new UpdateListener() {
					@Override
					public void onSuccess() {
						BaseApplication.getInstance()
								.setLatitude(
										String.valueOf(user.getLocation()
												.getLatitude()));
						BaseApplication.getInstance().setLongtitude(
								String.valueOf(user.getLocation()
										.getLongitude()));
						ShowLog("经纬度更新成功");
					}

					@Override
					public void onFailure(int code, String msg) {
						ShowLog("经纬度更新 失败:" + msg);
					}
				});
			} else {
				ShowLog("用户位置未发生过变化");
			}
		}
	}

	// 设置聊天有关的页面actionbar
	@SuppressLint("ResourceAsColor")
	protected void initActionbar(String text,int msg) {
		ActivityUtils.setActionBarLayout(getActionBar(),
				getApplicationContext(), R.layout.title_bar);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.title_color, this);
		ImageView iv_left_back = (ImageView) findViewById(R.id.iv_actionbar_back);
		TextView tv_center_text = (TextView) findViewById(R.id.tv_actionbar_title);
		Button btn_right_send = (Button) findViewById(R.id.btn_right_send);
		tv_center_text.setText(text);
		iv_left_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		switch (msg) {
		case 0:
			btn_right_send.setVisibility(View.GONE);
			break;
		case 1:
			btn_right_send.setVisibility(View.VISIBLE);
			btn_right_send.setText("发送");
			break;
		default:
			break;
		}
	}
	
}
