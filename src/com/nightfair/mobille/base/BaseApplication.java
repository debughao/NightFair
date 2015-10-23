package com.nightfair.mobille.base;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.util.PreferencesCookieStore;
import com.nightfair.mobille.R;
import com.nightfair.mobille.bean.BuyerInfo;
import com.nightfair.mobille.db.BuyerDao;
import com.nightfair.mobille.db.DaoFactory;
import com.nightfair.mobille.service.CustomNotificationHandler;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import cn.bmob.sms.BmobSMS;

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
	private PushAgent mPushAgent;
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
		BmobSMS.initialize(getApplicationContext(), "000ca7d3d028874f8e8401f27877171e");
		
		init();
		httpUtils = new HttpUtils();
		cookieStore = new PreferencesCookieStore(this);
		httpUtils.configCookieStore(cookieStore);
		/**
		 * 推送消息
		 */	
		umengPush();
		
	}

	private void umengPush() {
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		mPushAgent.setDebugMode(true);			
		UmengMessageHandler messageHandler = new UmengMessageHandler(){
		
			@Override
			public void dealWithCustomMessage(final Context context, final UMessage msg) {
				new Handler().post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						// 对自定义消息的处理方式，点击或者忽略
						boolean isClickOrDismissed = true;
						if(isClickOrDismissed) {
							//自定义消息的点击统计
							UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
						} else {
							//自定义消息的忽略统计
							UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
						}
						Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
					}
				});
			}
	
			@Override
			public Notification getNotification(Context context,
					UMessage msg) {
				LogUtils.e(msg.extra.get("name1"));
				switch (msg.builder_id) {
				case 1:
					NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
					RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
					myNotificationView.setTextViewText(R.id.notification_title, msg.title);
					myNotificationView.setTextViewText(R.id.notification_text, msg.text);
					myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
					myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
					builder.setContent(myNotificationView);
					builder.setContentTitle(msg.title)
					.setContentText(msg.text)
					.setTicker(msg.ticker)
					.setAutoCancel(true);
					Notification mNotification = builder.build();				
					mNotification.contentView = myNotificationView;
					
					return mNotification;
				default:
					//默认为0，若填写的builder_id并不存在，也使用默认。
					return super.getNotification(context, msg);
				}
			}
		};
		mPushAgent.setMessageHandler(messageHandler);
		CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
		mPushAgent.setNotificationClickHandler(notificationClickHandler);
		
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
