package com.nightfair.mobille.activity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bmob.pay.tool.BmobPay;
import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.base.Activitybase;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.config.BmobConstants;
import com.nightfair.mobille.util.SharePreferenceUtil;
import com.nightfair.mobille.util.VersionUtil;
import com.umeng.message.PushAgent;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
	public SharePreferenceUtil mSharedUtil;
	public BaseApplication mApplication;
	private String cityName;
	private boolean allowLocation, allowPush;
	private LocationClient mLocationClient;
	// 定位获取当前用户的地理位置
	// private LocationClient mLocationClient;
	/**
	 * 判断哪个页面执行finish方法
	 */
	public static int requestCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		BmobPay.init(this, "000ca7d3d028874f8e8401f27877171e");
		mApplication = BaseApplication.getInstance();
		mSharedUtil = mApplication.getSpUtil();	
		LogUtils.e("是否允许重新定位" + allowLocation);
		initView();
		startAnimation();

		// 可设置调试模式，当为true的时候，会在logcat的BmobChat下输出一些日志，包括推送服务是否正常运行，如果服务端返回错误，也会一并打印出来。方便开发者调试
		BmobChat.DEBUG_MODE = true;
		// BmobIM SDK初始化--只需要这一段代码即可完成初始化
		// 请到Bmob官网(http://www.bmob.cn/)申请ApplicationId,具体地址:http://docs.bmob.cn/android/faststart/index.html?menukey=fast_start&key=start_android
		BmobChat.getInstance(this).init(BmobConstants.APPLICATIONID);
		// 开启定位
		initLocClient();
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
		cityName = mSharedUtil.getLOCATION_CITYNAME();
		allowLocation = mSharedUtil.isAllowLocation();
		allowPush = mSharedUtil.isAllowPush();
		if (allowPush) {
			PushAgent mPushAgent = PushAgent.getInstance(this);
			mPushAgent.disable();
		}
	}

	/**
	 * 渐变展示启动屏
	 */
	private void startAnimation() {

		Animation aa = new Animation() {
		};
		aa.setDuration(5000);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				Intent intent = null;
				if (!TextUtils.isEmpty(cityName)) {
					if (allowLocation) {
						intent = new Intent(SplashActivity.this, LocationIndexActivity.class);
					} else {
						intent = new Intent(SplashActivity.this, MainActivity.class);
					}

				} else {
					intent = new Intent(SplashActivity.this, LocationIndexActivity.class);
				}
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
		mRvSplash.startAnimation(aa);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (userManager.getCurrentUser() != null) {
			// 每次自动登陆的时候就需要更新下当前位置和好友的资料，因为好友的头像，昵称啥的是经常变动的
			updateUserInfos();

		}
	}

	/**
	 * 开启定位，更新当前用户的经纬度坐标
	 */
	private void initLocClient() {
		mLocationClient = BaseApplication.getInstance().mLocationClient;
		// 设置定位参数
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(10000); // 10分钟扫描1次
		// 需要地址信息，设置为其他任何值（string类型，且不能为null）时，都表示无地址信息。
		option.setAddrType("all");
		// 设置是否返回POI的电话和地址等详细信息。默认值为false，即不返回POI的电话和地址信息。
		// option.setPoiExtraInfo(true);
		// 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
		option.setProdName("通过GPS定位我当前的位置");
		// 禁用启用缓存定位数据
		option.disableCache(true);
		// 设置最多可返回的POI个数，默认值为3。由于POI查询比较耗费流量，设置最多返回的POI个数，以便节省流量。
		// option.setPoiNumber(3);
		// 设置定位方式的优先级。
		// 当gps可用，而且获取了定位结果时，不再发起网络请求，直接返回给用户坐标。这个选项适合希望得到准确坐标位置的用户。如果gps不可用，再发起网络请求，进行定位。
		option.setPriority(LocationClientOption.GpsFirst);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
		}

		super.onDestroy();
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation arg0) {
			Log.e("info", "city = " + arg0.getCity());
			if (arg0.getCity() == null) {
				return;
			}
			arg0.getLongitude();
			arg0.getLatitude();
		}
	}
}
