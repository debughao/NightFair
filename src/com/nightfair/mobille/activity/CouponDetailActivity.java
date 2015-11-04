package com.nightfair.mobille.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.adapter.GuessCouponAdapter;
import com.nightfair.mobille.adapter.SellerCouponAdapter;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.bean.Comments;
import com.nightfair.mobille.bean.SellerAndCoupon;
import com.nightfair.mobille.bean.SellerCommentsBuyer;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.ToastUtil;
import com.nightfair.mobille.view.FullyListView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class CouponDetailActivity extends BaseActivity implements OnClickListener,OnRefreshListener<ScrollView> {
	private Context mContext;
	private RelativeLayout rl_head;
	private PullToRefreshScrollView myScrollView;
	private ImageView iv_img, iv_seller_tell;
	private int height, couponposition;
	private SellerAndCoupon mAndCoupon;
	private TextView tv_name, tv_description, tv_deal_sell, tv_seller_name, tv_seller_adress, tv_seller_location,
			tv_coupon_price, tv_coupondetail_price, tv_coupon_back;
	private ImageButton iv_title_bar_message;
	private ToggleButton iv_title_bar_collect;
	private ArrayList<Comments> list = new ArrayList<Comments>();
	private SellerCouponAdapter adapter;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private FullyListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon_detail);
		mContext = this;
		Intent intent = getIntent();
		mAndCoupon = (SellerAndCoupon) intent.getSerializableExtra("seller");
		couponposition = intent.getIntExtra("couponposition", 0);
		ActivityUtils.setActionBarLayout(getActionBar(), mContext, R.layout.title_bar_coupon);
		ToastUtil.show(mContext, "用户id：" + mAndCoupon.getId());

		// int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		// LayoutParams params=rl_head.getLayoutParams();
		// params.height=screenWidth/3*2;

		myScrollView = (PullToRefreshScrollView) findViewById(R.id.sv_index_detail);
		inintView();
		inintActionbar();
		inintData();
		adapter = new SellerCouponAdapter(list, mContext);
		listView.setAdapter(adapter);
	}

	
	private void inintActionbar() {
		// TODO Auto-generated method stub
		tv_coupon_back = (TextView) findViewById(R.id.tv_coupon_back);
		iv_title_bar_message = (ImageButton) findViewById(R.id.iv_title_bar_message);
		iv_title_bar_collect = (ToggleButton) findViewById(R.id.iv_title_bar_collect);
		tv_coupon_back.setOnClickListener(this);
		iv_title_bar_message.setOnClickListener(this);
		iv_title_bar_collect.setOnClickListener(this);
	}

	private void inintView() {
		// TODO Auto-generated method stub
		iv_img = (ImageView) findViewById(R.id.iv_coupon_img);
		rl_head = (RelativeLayout) findViewById(R.id.rl_coupon_detail_head);
		tv_name = (TextView) findViewById(R.id.tv_coupon_name);
		tv_description = (TextView) findViewById(R.id.tv_coupon_description);
		tv_deal_sell = (TextView) findViewById(R.id.tv_coupon_deal_sell);
		iv_seller_tell = (ImageView) findViewById(R.id.iv_sellerinfo_tell);
		tv_seller_name = (TextView) findViewById(R.id.tv_sellerinfo_name);
		tv_seller_adress = (TextView) findViewById(R.id.tv_sellerinfo_adress);
		tv_seller_location = (TextView) findViewById(R.id.tv_seller_location);
		tv_coupon_price = (TextView) findViewById(R.id.tv_coupon_price);
		tv_coupondetail_price = (TextView) findViewById(R.id.tv_coupondetail_price);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.sv_coupon_detail);
		mPullRefreshScrollView.setOnRefreshListener(this);
		listView = (FullyListView) findViewById(R.id.lv_coupon_comment);
		iv_seller_tell.setOnClickListener(this);
		if (mAndCoupon != null) {
			tv_name.setText(mAndCoupon.getSeller_name());
			tv_description.setText(mAndCoupon.getCoupons().get(couponposition).getDescription());
			tv_deal_sell.setText("已售出 " + mAndCoupon.getCoupons().get(couponposition).getSeller_counts());
			tv_seller_name.setText(mAndCoupon.getSeller_name());
			tv_seller_adress.setText(mAndCoupon.getArer() + mAndCoupon.getStreet());
			tv_coupon_price.setText(mAndCoupon.getCoupons().get(couponposition).getOriginal_price());

			String currentPrice = mAndCoupon.getCoupons().get(couponposition).getCurrent_price();
			String originalPrice = mAndCoupon.getCoupons().get(couponposition).getOriginal_price();
			String price = "￥ " + currentPrice + "  ￥" + originalPrice;
			SpannableString spanString = new SpannableString(price);
			spanString.setSpan(new ForegroundColorSpan(0xffe75858), 0, currentPrice.length() + 2,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			spanString.setSpan(new AbsoluteSizeSpan(40), 2, currentPrice.length() + 4,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			spanString.setSpan(new StrikethroughSpan(), currentPrice.length() + 4,
					currentPrice.length() + originalPrice.length() + 5, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			tv_coupondetail_price.setText(spanString);
			BitmapLoadCallBack<ImageView> callback = new DefaultBitmapLoadCallBack<ImageView>() {

				@Override
				public void onLoadStarted(ImageView container, String uri, BitmapDisplayConfig config) {
					super.onLoadStarted(container, uri, config);

				}

				@Override
				public void onLoadCompleted(ImageView container, String uri, Bitmap bitmap, BitmapDisplayConfig config,
						BitmapLoadFrom from) {
					super.onLoadCompleted(container, uri, bitmap, config, from);
					int bwidth = bitmap.getWidth();
					int bHeight = bitmap.getHeight();
					int width = getWindowManager().getDefaultDisplay().getWidth();
					height = width * bHeight / bwidth;
					LayoutParams param = rl_head.getLayoutParams();
					param.height = height;
					rl_head.setLayoutParams(param);
				}
			};
			BitmapUtils bitmapUtils = BaseApplication.bitmapUtils;
			bitmapUtils.configDefaultLoadFailedImage(getResources().getDrawable(R.drawable.ic_head_default));
			bitmapUtils.display(iv_img, AppConstants.SERVERIP + mAndCoupon.getImg(), callback);
		} else {
			ToastUtil.show(mContext, "获取信息失败，稍后重试！");
		}

	}
	private void inintData() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("seller_id", mAndCoupon.getId());
		params.addBodyParameter("pageNum", "5");
		params.addBodyParameter("pageNow", "1");
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, AppConstants.GETCOMMENTS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LogUtils.e("网络异常");
				mPullRefreshScrollView.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					int state = jsonObject.optInt("state");
					String results = jsonObject.optString("result");
					if (state != 200) {					
						mPullRefreshScrollView.onRefreshComplete();
						ToastUtil.show(mContext, results);
					} else {
						String data = jsonObject.optString("data");
						Gson gson = new Gson();
						Type typeOfT = new TypeToken<SellerCommentsBuyer>() {
						}.getType();
						LogUtils.e("sdsds---------" + data);
						SellerCommentsBuyer sellerAndCoupons = gson.fromJson(data, typeOfT);
						list.clear();
						list.addAll(sellerAndCoupons.getComments());
						adapter.notifyDataSetChanged();
						mPullRefreshScrollView.onRefreshComplete();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_sellerinfo_tell:
			String phone = mAndCoupon.getPhone();
			if (phone != null && !phone.equals("")) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			} else {
				ToastUtil.show(mContext, "商家电话暂不可用！");
			}
			break;
		case R.id.iv_title_bar_message:
			share();
			break;
		case R.id.iv_title_bar_collect:

			break;
		case R.id.tv_coupon_back:
			finish();
			break;

		default:
			break;
		}
	}

	private void share() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		intent.putExtra(Intent.EXTRA_TEXT,
				"我在夜夜通发现" + mAndCoupon.getSeller_name() + "这家店铺不错哦，你也来看看吧！\n 去 " + AppConstants.APPURL + "\n可以下载！");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, "夜夜通分享"));
	}


	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		inintData();
	}
}
