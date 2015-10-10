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
import android.widget.RelativeLayout;

public class MainTab_Personal extends Fragment implements OnClickListener {

	private Button bt_login;
	private View newsLayout;
	private RelativeLayout rl_personal_item_coupon;
	private RelativeLayout rl_personal_item_collection;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		newsLayout = inflater.inflate(R.layout.main_tab_personal, container, false);
		inintsView();
		return newsLayout;

	}

	private void inintsView() {
		bt_login = (Button) newsLayout.findViewById(R.id.bt_login_normal);
		bt_login.setOnClickListener(this);
		rl_personal_item_coupon = (RelativeLayout) newsLayout.findViewById(R.id.personal_item_coupon);
		rl_personal_item_coupon.setOnClickListener(this);
		rl_personal_item_collection = (RelativeLayout) newsLayout.findViewById(R.id.personal_item_collection);
		rl_personal_item_collection.setOnClickListener(this);

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

		default:
			break;
		}

	}

	@Override
	public void onResume() {
		if (SplashActivity.isLogin) {
			ActivityUtils.showShortToast(getActivity(), "登录成功");	
		}	
		super.onResume();
	}
}
