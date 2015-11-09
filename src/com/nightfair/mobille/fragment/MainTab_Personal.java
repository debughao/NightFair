package com.nightfair.mobille.fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.nightfair.mobille.R;
import com.nightfair.mobille.activity.AboutActivity;
import com.nightfair.mobille.activity.MessageActivity;
import com.nightfair.mobille.activity.MyColltctionActivity;
import com.nightfair.mobille.activity.MyCommentActivity;
import com.nightfair.mobille.activity.OrderManageActivity;
import com.nightfair.mobille.activity.PersonalWalletActivity;
import com.nightfair.mobille.activity.SettingActivity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.lib.umeng.CustomActivity;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.FragmentUtils;
import com.nightfair.mobille.util.ToastUtil;
import com.nightfair.mobille.widget.CircleImageView;
import com.nightfair.mobille.widget.ReboundScrollView;
import com.umeng.fb.FeedbackAgent;
import android.annotation.SuppressLint;
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
	private View personalView;
	private RelativeLayout rl__coupon;
	private RelativeLayout rl__collection;
	private LinearLayout ll_login_already;
	private LinearLayout ll_login_normal, ll_order_unpaid, ll_order_unused, ll_order_needfeedback, ll_order_refund;
	private TextView tv_nickname;
	private LinearLayout ll_logout;
	private CircleImageView iv_face;
	private RequestQueue queue;
	private RelativeLayout rl_about;
	private RelativeLayout rl__comment;
	private RelativeLayout rl__wallet;
	private RelativeLayout rl__recommend;
	private RelativeLayout rl__feedback;
	private ImageView iv_setting, iv_message;
	private ReboundScrollView reboundScrollView;

	@SuppressLint("ResourceAsColor")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (personalView == null) {
			personalView = inflater.inflate(R.layout.main_tab_personal, container, false);
		}
		ViewGroup parent = (ViewGroup) personalView.getParent();
		if (parent != null) {
			parent.removeView(personalView);
		}
		inintPersonalBar();
		inintView();
		queue = Volley.newRequestQueue(getActivity());
		if (BaseApplication.userid != 0) {
			refreshView();
		}
		return personalView;
	}

	private void inintView() {
		bt_login = (Button) personalView.findViewById(R.id.bt_login_normal);
		rl__coupon = (RelativeLayout) personalView.findViewById(R.id.personal_item_coupon);
		rl__collection = (RelativeLayout) personalView.findViewById(R.id.personal_item_collection);
		rl__comment = (RelativeLayout) personalView.findViewById(R.id.personal_item_comment);
		rl__wallet = (RelativeLayout) personalView.findViewById(R.id.personal_item_wallet);
		rl__recommend = (RelativeLayout) personalView.findViewById(R.id.personal_item_recommend);
		rl__feedback = (RelativeLayout) personalView.findViewById(R.id.personal_item_feedback);
		rl_about = (RelativeLayout) personalView.findViewById(R.id.personal_item_about);
		ll_login_normal = (LinearLayout) personalView.findViewById(R.id.ll_login_normal);
		ll_login_already = (LinearLayout) personalView.findViewById(R.id.ll_login_already);
		ll_order_unpaid = (LinearLayout) personalView.findViewById(R.id.ll_order_unpaid);
		ll_order_unused = (LinearLayout) personalView.findViewById(R.id.ll_order_unused);
		ll_order_needfeedback = (LinearLayout) personalView.findViewById(R.id.ll_order_needfeedback);
		ll_order_refund = (LinearLayout) personalView.findViewById(R.id.ll_order_refund);
		ll_logout = (LinearLayout) personalView.findViewById(R.id.ll_personal_logout);
		tv_nickname = (TextView) personalView.findViewById(R.id.tv_fm_nickname);
		iv_face = (CircleImageView) personalView.findViewById(R.id.iv_face);
		reboundScrollView = (ReboundScrollView) personalView.findViewById(R.id.reboundScrollView);
		mySetOnClickListener(bt_login, rl__coupon, ll_login_already, ll_order_unpaid, ll_order_unused, rl__collection,
				ll_logout, rl__comment, rl__wallet, rl__recommend, rl__feedback, rl_about, ll_order_needfeedback,
				ll_order_refund);

	}

	private void inintPersonalBar() {
		iv_message = (ImageView) personalView.findViewById(R.id.iv_title_bar_message);
		iv_setting = (ImageView) personalView.findViewById(R.id.iv_title_bar_setting);
		iv_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityUtils.startActivity(getActivity(), SettingActivity.class);
			}
		});
		iv_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityUtils.startActivity(getActivity(), MessageActivity.class);

			}
		});
	}

	private void inintHeadFace() {
		tv_nickname.setText(BaseApplication.buyerInfo.getNickname());
		String image_url = BaseApplication.buyerInfo.getImage();
		ImageRequest imageRequest = new ImageRequest((AppConstants.SERVERIP + image_url), new Listener<Bitmap>() {
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
		case R.id.personal_item_collection:
			FragmentUtils.startActivity(this, MyColltctionActivity.class);
			break;
		case R.id.personal_item_comment:
			FragmentUtils.startActivity(this, MyCommentActivity.class);
			break;
		case R.id.personal_item_about:
			FragmentUtils.startActivity(this, AboutActivity.class);
			break;
		case R.id.personal_item_feedback:
			feedback();
			break;
		case R.id.ll_login_already:
			intent = new Intent("com.nightfair.buyer.action.update");
			startActivityForResult(intent, 1);
			break;
		case R.id.ll_personal_logout:
			BaseApplication.cookieStore.clear();
			logout();
			break;
		case R.id.personal_item_coupon:
			intent = new Intent(getActivity(), OrderManageActivity.class);
			intent.putExtra("action", "all");
			intent.putExtra("type", "all");
			startActivity(intent);
			break;
		case R.id.ll_order_unpaid:
			intent = new Intent(getActivity(), OrderManageActivity.class);
			intent.putExtra("action", "unpaid");
			intent.putExtra("type", "single");
			startActivity(intent);
			break;
		case R.id.ll_order_unused:
			intent = new Intent(getActivity(), OrderManageActivity.class);
			intent.putExtra("action", "unused");
			intent.putExtra("type", "single");
			startActivity(intent);
			break;
		case R.id.ll_order_needfeedback:
			intent = new Intent(getActivity(), OrderManageActivity.class);
			intent.putExtra("action", "needfeedback");
			intent.putExtra("type", "single");
			startActivity(intent);
			break;
		case R.id.ll_order_refund:
			intent = new Intent(getActivity(), OrderManageActivity.class);
			intent.putExtra("action", "refund");
			intent.putExtra("type", "single");
			startActivity(intent);
			break;
		case R.id.personal_item_wallet:
			if (BaseApplication.userid != 0) {
				FragmentUtils.startActivity(this, PersonalWalletActivity.class);
			} else {
				intent = new Intent("com.nightfair.buyer.action.login");
				startActivityForResult(intent, 0);
			}
			break;
		case R.id.personal_item_recommend:
			share();
			break;
		default:
			break;
		}

	}

	/**
	 * 用户反馈
	 */
	private void feedback() {
		final FeedbackAgent fb = new FeedbackAgent(getActivity());
		fb.sync();
		fb.openAudioFeedback();
		fb.openFeedbackPush();
		// UserInfo info = fb.getUserInfo();
		// if (info == null)
		// info = new UserInfo();
		// Map<String, String> contact = info.getContact();
		// if (contact == null)
		// contact = new HashMap<String, String>();
		// // contact.put(KEY_UMENG_CONTACT_INFO_PLAIN_TEXT, contact_info);
		//
		// contact.put("email", "*******");
		// // contact.put("qq", "*******");
		// // contact.put("phone", "*******");
		// // contact.put("plain", "*******");
		// info.setContact(contact);
		//
		// // optional, setting user gender information.
		// info.setAgeGroup(1);
		// info.setGender("male");
		// // info.setGender("female");
		//
		// fb.setUserInfo(info);
		//
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// boolean result = fb.updateUserInfo();
		// }
		// }).start();
		FragmentUtils.startActivity(this, CustomActivity.class);
	}

	/**
	 * VISIBLE:0 可见的 ;INVISIBILITY:4 不可见的，但还占着原来的空间; GONE:8 不可见的，不占用原来的布局空间
	 */

	private void logout() {
		reboundScrollView.scrollTo(0, 0);
		ll_login_normal.setVisibility(View.VISIBLE);// 显示未登录界面
		ll_login_already.setVisibility(View.GONE);// 隐藏登录后界面
		ll_logout.setVisibility(View.GONE);// 隐藏注销界面
		BaseApplication.getInstance().logout();
		BaseApplication.userid = 0;
		BaseApplication.cookieStore.clear();
		BaseApplication.mBuyerDao.logout(BaseApplication.userid);
		BaseApplication.getInstance().logout();
		ToastUtil.showCenter(getActivity(), "客观你慢走，欢迎下次光临^-^");

	}

	private void refreshView() {
		ll_login_normal.setVisibility(View.GONE);// 隐藏未登录界面
		ll_login_already.setVisibility(View.VISIBLE);// 显示登录后界面
		ll_logout.setVisibility(View.VISIBLE);// 显示注销界面
		inintHeadFace();
	}
	private void share() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		intent.putExtra(Intent.EXTRA_TEXT,
				"我发现夜夜通这款APP不错，上面的优惠券真便宜，还有好的好玩的，你也来试试吧！\n 在 " + AppConstants.APPURL + "\n可以下载！");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, "夜夜通分享"));
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			if (resultCode == Activity.RESULT_OK) {
				refreshView();
				reboundScrollView.scrollTo(0, 0);
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
		if (BaseApplication.userid != 0)
			refreshView();
		super.onResume();
	}

	public interface ICallBack {
		public void index(int flag);
	}
}
