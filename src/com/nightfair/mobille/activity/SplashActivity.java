package com.nightfair.mobille.activity;

import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.util.VersionUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashActivity extends BaseActivity {
	private TextView mTvVersion;
	private RelativeLayout mRvSplash;
	private ImageView mIvLogo;
	/**
	 * 判断用户是否登录
	 */
	public static boolean isLogin;
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

}
