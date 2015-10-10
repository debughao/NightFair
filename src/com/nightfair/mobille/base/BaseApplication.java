package com.nightfair.mobille.base;

import java.util.ArrayList;
import java.util.List;

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
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		initImageLoader();

	/*	// 使用腾讯BUGLY上传崩溃信息
		initCrashReport();*/
	}

	/**
	 * 初始化ImageLoader
	 */
	private void initImageLoader() {

	}

	/**
	 * 初始化崩溃上传(腾讯BUGLY)
	 */
//	private void initCrashReport() {
//		CrashReport.initCrashReport(this, "900007710", false);
//	}

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
