package com.nightfair.mobille.activity;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.nightfair.mobille.R;
import com.nightfair.mobille.base.Activitybase;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.config.BmobConstants;
import com.nightfair.mobille.util.VersionUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.im.BmobChat;

public class SplashActivity extends Activitybase {
	private TextView mTvVersion;
	private RelativeLayout mRvSplash;
	private ImageView mIvLogo;

	// 定位获取当前用户的地理位置
	private LocationClient mLocationClient;

	private BaiduReceiver mReceiver;// 注册广播接收器，用于监听网络以及验证key

	/**
	 * 判断哪个页面执行finish方法
	 */
	public static int requestCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		initView();
		startAnimation();
		// 可设置调试模式，当为true的时候，会在logcat的BmobChat下输出一些日志，包括推送服务是否正常运行，如果服务端返回错误，也会一并打印出来。方便开发者调试
		BmobChat.DEBUG_MODE = true;
		// BmobIM SDK初始化--只需要这一段代码即可完成初始化
		// 请到Bmob官网(http://www.bmob.cn/)申请ApplicationId,具体地址:http://docs.bmob.cn/android/faststart/index.html?menukey=fast_start&key=start_android
		BmobChat.getInstance(this).init(BmobConstants.APPLICATIONID);
		// 开启定位
		initLocClient();
		// 注册地图 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new BaiduReceiver();
		registerReceiver(mReceiver, iFilter);
	}

	/**
	 * 初始化
	 */
	private void initView() {
		mIvLogo = (ImageView) findViewById(R.id.iv_logo);
		mTvVersion = (TextView) findViewById(R.id.tv_version);
		mRvSplash = (RelativeLayout) findViewById(R.id.rl_splash);

		mIvLogo.setImageResource(R.drawable.ic_launcher);
		String str = VersionUtil.getVersionName(this);
		mTvVersion.setText("当前版本：" + str);
	}

	/**
	 * 渐变展示启动屏
	 */
	private void startAnimation() {

		Animation aa = new Animation() {
		};
		aa.setDuration(3000);
		mRvSplash.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {

			}

		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (userManager.getCurrentUser() != null) {
			// 每次自动登陆的时候就需要更新下当前位置和好友的资料，因为好友的头像，昵称啥的是经常变动的
			updateUserInfos();
		} else {

		}
	}

	/**
	 * 开启定位，更新当前用户的经纬度坐标
	 */
	private void initLocClient() {
		mLocationClient = BaseApplication.getInstance().mLocationClient;
		LocationClientOption option = new LocationClientOption();
		// 设置定位模式:高精度模式
		option.setLocationMode(LocationMode.Hight_Accuracy);
		// 设置坐标类型:百度经纬度
		option.setCoorType("bd09ll");
		// 设置发起定位请求的间隔时间为1000ms:低于1000为手动定位一次，大于或等于1000则为定时定位
		option.setScanSpan(1000);
		option.setIsNeedAddress(false);// 不需要包含地址信息
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class BaiduReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				ShowToast("key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
			} else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				ShowToast("当前网络连接不稳定，请检查您的网络设置!");
			}
		}
	}

}
