package com.nightfair.mobille.activity;

import com.nightfair.mobille.R;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.FragmentUtils;
import android.app.Fragment;
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
	private RelativeLayout rl_personal_item_coupon;
	private RelativeLayout rl_personal_item_collection;
	private LinearLayout ll_login_already;
	private LinearLayout ll_login_normal;
	private TextView tv_personal_logout;
	private LinearLayout ll_personal_logout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		personalLayout = inflater.inflate(R.layout.main_tab_personal, container, false);
		inintsView();
		return personalLayout;

	}

	private void inintsView() {
		bt_login = (Button) personalLayout.findViewById(R.id.bt_login_normal);
		bt_login.setOnClickListener(this);
		rl_personal_item_coupon = (RelativeLayout) personalLayout.findViewById(R.id.personal_item_coupon);
		rl_personal_item_coupon.setOnClickListener(this);
		rl_personal_item_collection = (RelativeLayout) personalLayout.findViewById(R.id.personal_item_collection);
		rl_personal_item_collection.setOnClickListener(this);
		ll_login_normal = (LinearLayout) personalLayout.findViewById(R.id.ll_login_normal);
		ll_login_already = (LinearLayout) personalLayout.findViewById(R.id.ll_login_already);
		ll_login_already.setOnClickListener(this);
		ll_personal_logout = (LinearLayout) personalLayout.findViewById(R.id.ll_personal_logout);
		tv_personal_logout = (TextView) personalLayout.findViewById(R.id.tv_personal_logout);
		/* tv_personal_logout.setOnClickListener(this); */

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

			FragmentUtils.startActivity(this, Personal_Collecyion_Activity.class);
			break;
		case R.id.ll_login_already:

			FragmentUtils.startActivity(this, Personal_detail_Activity.class);
			break;
		default:
			break;
		}

	}

	/**
	 * VISIBLE:0 可见的 ;INVISIBILITY:4 不可见的，但还占着原来的空间; GONE:8 不可见的，不占用原来的布局空间
	 */
	@Override
	public void onResume() {

		if (SplashActivity.isLogin && SplashActivity.requestCode == 1) {
			refreshView();
			ActivityUtils.showShortToast(getActivity(), "登录成功");
		}
		super.onResume();
	}

	private void refreshView() {
		ll_login_normal.setVisibility(8);// 隐藏未登录界面
		ll_login_already.setVisibility(0);// 显示登录后界面
		ll_personal_logout.setVisibility(0);// 显示注销界面
	}

}
