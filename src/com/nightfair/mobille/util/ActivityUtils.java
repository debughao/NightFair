package com.nightfair.mobille.util;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;
import android.view.Window;
import android.view.WindowManager;

public class ActivityUtils {
	private static Toast mToast;
	@SuppressLint("InlinedApi") public static void setTranslucentStatus(Window win, boolean on) {
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	public static void setActionBarLayout(ActionBar actionBar, Context context,
			int layoutId) {
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setDisplayShowTitleEnabled(false);
			LayoutInflater inflator = LayoutInflater.from(context);
			View v = inflator.inflate(layoutId, null);
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(v, layout);
		}
	}

	public static void setStatusBarColor(int res, Activity activity) {
		SystemBarTintManager tintManager = new SystemBarTintManager(activity);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(res);
		
	}

	public static void setStatusBarDrawable(Drawable drawable, Activity activity) {
		SystemBarTintManager tintManager = new SystemBarTintManager(activity);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintDrawable(drawable);
		
	}
	public static void setActionBarByColor(Activity activity,int layoutId,int colorId){
		ActivityUtils.setActionBarLayout(activity.getActionBar(),activity, layoutId);
		ActivityUtils.setTranslucentStatus(activity.getWindow(), true);
		ActivityUtils.setStatusBarColor(colorId, activity);
	}
	public static void setActionBarByDrawable(Activity activity,int layoutId,Drawable drawable){
		ActivityUtils.setActionBarLayout(activity.getActionBar(),activity, layoutId);
		ActivityUtils.setTranslucentStatus(activity.getWindow(), true);
		ActivityUtils.setStatusBarDrawable(drawable, activity);
	}
	
	public static void startActivity(Context context, Class<?> cls) {
		Intent intent =new Intent(context, cls);
		context.startActivity(intent);
	}
	public static void showLongToast(Context context, String msg){
		if(mToast == null){
			mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		}else{
			mToast.setText(msg);
		}
		mToast.show();
	}
	public static void showShortToast(Context context, String msg){
		if(mToast == null){
			mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		}else{
			mToast.setText(msg);
		}
		mToast.show();
	}

}
