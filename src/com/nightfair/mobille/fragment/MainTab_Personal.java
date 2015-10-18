package com.nightfair.mobille.fragment;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.activity.Personal_Collection_Activity;
import com.nightfair.mobille.activity.Personal_Coupon_Activity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.config.FilePathConfig;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.FragmentUtils;
import com.nightfair.mobille.util.ToastUtil;
import com.nightfair.mobille.widget.CircleImageView;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @ClassName: MainTab_Personal
 * @Description:
 * @author debughao
 * @date 2015年10月10日修改
 */
public class MainTab_Personal extends Fragment implements OnClickListener {

	private Button bt_login;
	private View personalLayout;
	private RelativeLayout rl__coupon;
	private RelativeLayout rl__collection;
	private LinearLayout ll_login_already;
	private LinearLayout ll_login_normal;
	private TextView tv_logout;
	private LinearLayout ll_logout;
	private  ImageView iv_face;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		personalLayout = inflater.inflate(R.layout.main_tab_personal, container, false);
		inintView();
		return personalLayout;

	}

	private void inintView() {
		bt_login = (Button) personalLayout.findViewById(R.id.bt_login_normal);
		rl__coupon = (RelativeLayout) personalLayout.findViewById(R.id.personal_item_coupon);
		rl__collection = (RelativeLayout) personalLayout.findViewById(R.id.personal_item_collection);
		ll_login_normal = (LinearLayout) personalLayout.findViewById(R.id.ll_login_normal);
		ll_login_already = (LinearLayout) personalLayout.findViewById(R.id.ll_login_already);
		ll_logout = (LinearLayout) personalLayout.findViewById(R.id.ll_personal_logout);
		tv_logout = (TextView) personalLayout.findViewById(R.id.tv_personal_logout);
		iv_face = (ImageView) personalLayout.findViewById(R.id.iv_personal_face);
		mySetOnClickListener(bt_login, rl__coupon, ll_login_already, rl__collection, tv_logout);
		
	}
   private void inintHeadFace(){
	   String image_url=BaseApplication.buyerInfo.getImage();
	  BitmapUtils bitmapUtils =new BitmapUtils(getActivity(), FilePathConfig.getHeadFaceParh(getActivity()));
	  bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);
	  bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(getActivity()).scaleDown(3));
	  LogUtils.e("图片"+AppConstants.ServerIp+image_url);
	 BitmapLoadCallBack<ImageView> bitmapLoadCallBack =new BitmapLoadCallBack<ImageView>() {
		
		@Override
		public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLoadCompleted(ImageView arg0, String arg1, Bitmap arg2, BitmapDisplayConfig arg3,
				BitmapLoadFrom arg4) {
			
			iv_face.setImageBitmap(arg2);
		}
	};
	
   }
	private void mySetOnClickListener(View... v) {
		for (View view : v) {
			view.setOnClickListener(this);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login_normal:
			Intent intent = new Intent("com.nightfair.buyer.action.login");
			startActivityForResult(intent, 0);
			break;
		case R.id.personal_item_coupon:

			FragmentUtils.startActivity(this, Personal_Coupon_Activity.class);
			break;
		case R.id.personal_item_collection:

			FragmentUtils.startActivity(this, Personal_Collection_Activity.class);
			break;
		case R.id.ll_login_already:
			 intent = new Intent("com.nightfair.buyer.action.update");
			startActivityForResult(intent, 1);
			break;
		case R.id.tv_personal_logout:
			BaseApplication.cookieStore.clear();
			logout();
			break;
		default:
			break;
		}

	}

	/**
	 * VISIBLE:0 可见的 ;INVISIBILITY:4 不可见的，但还占着原来的空间; GONE:8 不可见的，不占用原来的布局空间
	 */

	private void logout() {
		ll_login_normal.setVisibility(View.VISIBLE);// 显示未登录界面
		ll_login_already.setVisibility(View.GONE);// 隐藏登录后界面
		ll_logout.setVisibility(View.GONE);// 隐藏注销界面
		ToastUtil.showCenter(getActivity(), "客观你慢走，欢迎下次光临^-^");

	}

	private void refreshView() {
		ll_login_normal.setVisibility(View.GONE);// 隐藏未登录界面
		ll_login_already.setVisibility(View.VISIBLE);// 显示登录后界面
		ll_logout.setVisibility(View.VISIBLE);// 显示注销界面
		inintHeadFace();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			refreshView();
			ActivityUtils.showShortToast(getActivity(), "登录成功");
			break;
		case 1:
			if(resultCode==Activity.RESULT_OK){
			ToastUtil.showCenter(getActivity(), "修改资料成功");
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {

//		if (SplashActivity.isLogin && SplashActivity.requestCode == 1) {
//			refreshView();
//			ActivityUtils.showShortToast(getActivity(), "登录成功");
//		}
		super.onResume();
	}
}
