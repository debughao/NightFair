package com.nightfair.mobille.base;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.util.PreferencesCookieStore;
import com.nightfair.mobille.bean.BuyerInfo;
import com.nightfair.mobille.db.BuyerDao;
import com.nightfair.mobille.db.DaoFactory;
import com.umeng.fb.push.FeedbackPush;

import android.app.Activity;
import android.app.Application;

/**
 * 
 * @ClassName: BaseApplication
 * @Description:
 * @author debughao
 * @date 2015年10月9日
 */

public class BaseApplication extends Application {

	private static BaseApplication mInstance;
	private List<Activity> mActivities = new ArrayList<Activity>();


	public static BuyerDao mBuyerDao;
	public static int userid;
	public static BuyerInfo buyerInfo;
	public static PreferencesCookieStore cookieStore;

	public static HttpUtils httpUtils;
	public static BitmapUtils bitmapUtils;

	// 单例模式中获取唯一的ExitApplication 实例
	public static BaseApplication getInstance() {
		if (null == mInstance) {
			mInstance = new BaseApplication();
		}
		return mInstance;

	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		FeedbackPush.getInstance(this).init(true);
		init();
		httpUtils = new HttpUtils();
		cookieStore = new PreferencesCookieStore(this);
		httpUtils.configCookieStore(cookieStore);
	}

	private void init() {
		LogUtils.customTagPrefix = "xUtilsSample"; // 方便调试时过滤 adb logcat 输出
		
		mBuyerDao = DaoFactory.getInstance().getBuyerDao(getApplicationContext());
		userid = mBuyerDao.queryLoginUserid();
		if(userid!=0){
			buyerInfo=mBuyerDao.queryinfo(userid);
		}
		initImageLoader();

		/*
		 * // 使用腾讯BUGLY上传崩溃信息 initCrashReport();
		 */
	}

	/**
	 * 初始化ImageLoader
	 */
	private void initImageLoader() {

	}

	/**
	 * 初始化崩溃上传(腾讯BUGLY)
	 */
	// private void initCrashReport() {
	// CrashReport.initCrashReport(this, "900007710", false);
	// }

	/**
	 * 把Activity加入历史堆栈
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		mActivities.add(activity);
	}

	/**
	 * 结束
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();

		for (Activity activity : mActivities) {
			activity.finish();
		}

		System.exit(0);
	}
}
