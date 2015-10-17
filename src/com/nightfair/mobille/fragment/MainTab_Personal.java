package com.nightfair.mobille.fragment;

import com.nightfair.mobille.R;
import com.nightfair.mobille.activity.LoginActivity;
import com.nightfair.mobille.activity.Personal_Collection_Activity;
import com.nightfair.mobille.activity.Personal_Coupon_Activity;
import com.nightfair.mobille.activity.SplashActivity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.FragmentUtils;
import com.nightfair.mobille.util.ToastUtil;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		personalLayout = inflater.inflate(R.layout.main_tab_personal, container, false);
		inintsView();
		return personalLayout;

	}

	private void inintsView() {
		bt_login = (Button) personalLayout.findViewById(R.id.bt_login_normal);
		rl__coupon = (RelativeLayout) personalLayout.findViewById(R.id.personal_item_coupon);
		rl__collection = (RelativeLayout) personalLayout.findViewById(R.id.personal_item_collection);
		ll_login_normal = (LinearLayout) personalLayout.findViewById(R.id.ll_login_normal);
		ll_login_already = (LinearLayout) personalLayout.findViewById(R.id.ll_login_already);
		ll_logout = (LinearLayout) personalLayout.findViewById(R.id.ll_personal_logout);
		tv_logout = (TextView) personalLayout.findViewById(R.id.tv_personal_logout);
		mySetOnClickListener(bt_login, rl__coupon, ll_login_already, rl__collection, tv_logout);

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
			FragmentUtils.startActivity(this, LoginActivity.class);
			break;
		case R.id.personal_item_coupon:

			FragmentUtils.startActivity(this, Personal_Coupon_Activity.class);
			break;
		case R.id.personal_item_collection:

			FragmentUtils.startActivity(this, Personal_Collection_Activity.class);
			break;
		case R.id.ll_login_already:
			Intent intent = new Intent("com.nightfair.buyer.action.update");
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

	private void logout() {
		
		
	}

	/**
	 * VISIBLE:0 可见的 ;INVISIBILITY:4 不可见的，但还占着原来的空间; GONE:8 不可见的，不占用原来的布局空间
	 */


	private void refreshView() {
		ll_login_normal.setVisibility(View.GONE);// 隐藏未登录界面
		ll_login_already.setVisibility(View.VISIBLE);// 显示登录后界面
		ll_logout.setVisibility(View.VISIBLE);// 显示注销界面
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			ToastUtil.showCenter(getActivity(), "修改资料成功");
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void onResume() {

		if (SplashActivity.isLogin && SplashActivity.requestCode == 1) {
			refreshView();
			ActivityUtils.showShortToast(getActivity(), "登录成功");
		}
		super.onResume();
	}
}
