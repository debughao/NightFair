package com.nightfair.mobille.fragment;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.nightfair.mobille.R;
import com.nightfair.mobille.activity.Personal_Collection_Activity;
import com.nightfair.mobille.activity.Personal_Coupon_Activity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.FragmentUtils;
import com.nightfair.mobille.util.ToastUtil;
import com.nightfair.mobille.widget.CircleImageView;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
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
	private TextView tv_nickname;
	private LinearLayout ll_logout;
	private CircleImageView iv_face;
	private RequestQueue queue;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		personalLayout = inflater.inflate(R.layout.main_tab_personal, container, false);
		inintView();
		queue = Volley.newRequestQueue(getActivity());
		if (BaseApplication.userid != 0) {
			refreshView();
		}
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
		tv_nickname=(TextView) personalLayout.findViewById(R.id.tv_fm_nickname);
		iv_face = (CircleImageView) personalLayout.findViewById(R.id.iv_face);
		mySetOnClickListener(bt_login, rl__coupon, ll_login_already, rl__collection, tv_logout);

	}

	private void inintHeadFace() {	
		tv_nickname.setText(BaseApplication.buyerInfo.getNickname());
		String image_url = BaseApplication.buyerInfo.getImage();
		ImageRequest imageRequest = new ImageRequest((AppConstants.ServerIp + image_url), new Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap arg0) {
				iv_face.setImageBitmap(arg0);
			}
		}, 0, 0, Config.RGB_565, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				iv_face.setImageResource(R.drawable.my_dd_icon_default);
			}
		});
		queue.add(imageRequest);
		/**
		 * 下面使用xutils
		 * 的BitmapUtils加载图片出问题，对于imageview没问题，可能是使用自定义控件CircleImageView的原因，
		 * 兼容性需要处理
		 */
		// BitmapUtils bitmapUtils =new BitmapUtils(getActivity(),
		// FilePathConfig.getHeadFaceParh(getActivity()));
		// bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);
		// BitmapSize bitmapMaxSize=new BitmapSize(80, 80);
		// bitmapUtils.configDefaultAutoRotation(true);
		// bitmapUtils.configDefaultBitmapMaxSize(bitmapMaxSize);
		// bitmapUtils.display(iv_face, (AppConstants.ServerIp+image_url));
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
		BaseApplication.cookieStore.clear();
		BaseApplication.mBuyerDao.logout(BaseApplication.userid);
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
			if (resultCode == Activity.RESULT_OK) {
				refreshView();
				ActivityUtils.showShortToast(getActivity(), "登录成功");
			}
			break;
		case 1:
			inintHeadFace();
			if (resultCode == Activity.RESULT_OK) {
				
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

		// if (SplashActivity.isLogin && SplashActivity.requestCode == 1) {
		// refreshView();
		// ActivityUtils.showShortToast(getActivity(), "登录成功");
		// }
		super.onResume();
	}
}
