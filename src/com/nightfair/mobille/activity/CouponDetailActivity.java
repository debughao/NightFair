package com.nightfair.mobille.activity;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class CouponDetailActivity extends BaseActivity implements OnClickListener, OnRefreshListener<ScrollView>, AMapLocationListener {
	private Context mContext;
	private RelativeLayout rl_head, rl_comment_detail,rl_sellerinfo_detail;
	private ImageView iv_img, iv_seller_tell;
	private int height, couponposition;
	private SellerAndCoupon mAndCoupon;
	private TextView tv_name, tv_description, tv_deal_sell, tv_seller_name, tv_seller_adress, tv_seller_location,
			tv_coupon_price, tv_coupondetail_price, tv_coupon_back, tv_nomore_comment, tv_look_morecomment;
	private ImageButton iv_title_bar_message;
	private ToggleButton iv_title_bar_collect;
	private ArrayList<Comments> list = new ArrayList<Comments>();
	private SellerCouponAdapter adapter;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private FullyListView listView;
	private TextView tv_coupondetail_buy;
	private String seller_name;
	private String currentPrice, coupon_id;
	private String way;
	private LocationManagerProxy mLocationManagerProxy;	
	public  double geoLat, geoLng, mlatitude,mlongitude,distance;
	LatLng p1,p2;
	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon_detail);
		mContext = this;
		Intent intent = getIntent();
		mAndCoupon = (SellerAndCoupon) intent.getSerializableExtra("seller");	
		couponposition = intent.getIntExtra("couponposition", 0);
		boolean get=intent.getBooleanExtra("get", false);
		if (get) {
			mAndCoupon=(SellerAndCoupon) BaseApplication.mCache.getAsObject("seller");
		}
		way = "select";
		mlatitude=Double.parseDouble(mAndCoupon.getLatitude());
		mlongitude=Double.parseDouble(mAndCoupon.getLongitude());
		ActivityUtils.setActionBarLayout(getActionBar(), mContext, R.layout.title_bar_coupon);
		// int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		// LayoutParams params=rl_head.getLayoutParams();
		// params.height=screenWidth/3*2;
		seller_name = mAndCoupon.getSeller_name();
		coupon_id = mAndCoupon.getCoupons().get(couponposition).getId();
		inintView();
		inintActionbar();
		inintCollection();
		inintData();
		initLocation(); 
		adapter = new SellerCouponAdapter(list, mContext);
		listView.setFocusable(false);
		listView.setAdapter(adapter);
	}
	private void initLocation() {
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork,  1000, 15, this);
		mLocationManagerProxy.setGpsEnable(true);
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
		rl_comment_detail = (RelativeLayout) findViewById(R.id.rl_comment_detail);
		tv_name = (TextView) findViewById(R.id.tv_coupon_name);
		tv_description = (TextView) findViewById(R.id.tv_coupon_description);
		tv_deal_sell = (TextView) findViewById(R.id.tv_coupon_deal_sell);
		iv_seller_tell = (ImageView) findViewById(R.id.iv_sellerinfo_tell);
		tv_seller_name = (TextView) findViewById(R.id.tv_sellerinfo_name);
		tv_seller_adress = (TextView) findViewById(R.id.tv_sellerinfo_adress);
		tv_seller_location = (TextView) findViewById(R.id.tv_seller_location);
		tv_coupon_price = (TextView) findViewById(R.id.tv_coupon_price);
		tv_coupondetail_price = (TextView) findViewById(R.id.tv_coupondetail_price);
		tv_nomore_comment = (TextView) findViewById(R.id.tv_nomore_comment);
		tv_look_morecomment = (TextView) findViewById(R.id.tv_look_morecomment);
		tv_coupondetail_buy = (TextView) findViewById(R.id.tv_coupondetail_buy);
		rl_sellerinfo_detail= (RelativeLayout) findViewById(R.id.rl_sellerinfo_detail);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.sv_coupon_detail);
		mPullRefreshScrollView.setOnRefreshListener(this);
		listView = (FullyListView) findViewById(R.id.lv_coupon_comment);
		iv_seller_tell.setOnClickListener(this);
		rl_comment_detail.setOnClickListener(this);
		tv_coupondetail_buy.setOnClickListener(this);
		rl_sellerinfo_detail.setOnClickListener(this);
		if (mAndCoupon != null) {
			tv_name.setText(seller_name);
			tv_description.setText(mAndCoupon.getCoupons().get(couponposition).getDescription());
			tv_deal_sell.setText("已售出 " + mAndCoupon.getCoupons().get(couponposition).getSeller_counts());
			tv_seller_name.setText(mAndCoupon.getSeller_name());
			tv_seller_adress.setText(mAndCoupon.getArer() + mAndCoupon.getStreet());
			tv_coupon_price.setText(mAndCoupon.getCoupons().get(couponposition).getOriginal_price());
			currentPrice = mAndCoupon.getCoupons().get(couponposition).getCurrent_price();
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
					@SuppressWarnings("deprecation")
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

	private void inintCollection() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("action", "my");
		params.addBodyParameter("seller_id", mAndCoupon.getId());
		params.addBodyParameter("way", way);
		params.addBodyParameter("couponid", mAndCoupon.getCoupons().get(couponposition).getId());
		HttpUtils httpUtils = BaseApplication.httpUtils;
		httpUtils.send(HttpMethod.POST, AppConstants.GETCOUPON, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LogUtils.e("网络异常");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					int state = jsonObject.optInt("state");
					String results = jsonObject.optString("result");
					if (state == 200) {
						if ("select".equals(way)) {
							iv_title_bar_collect.setChecked(true);
						} else {
							iv_title_bar_collect.setChecked(true);
							ToastUtil.showCenter(mContext, results);
						}
					} else if (state == 201) {
						if ("select".equals(way)) {
							iv_title_bar_collect.setChecked(false);
						} else {
							iv_title_bar_collect.setChecked(false);
							ToastUtil.showCenter(mContext, results);
						}
					}else if (state == 405) {
						if (!"select".equals(way)) {
							ToastUtil.show(mContext, results);
							Intent intent = new Intent("com.nightfair.buyer.action.login");
							startActivityForResult(intent, 2);
						}														
				}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void inintData() {
		// TODO Auto-generated method stub
		showDialog("加载中...");
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("seller_id", mAndCoupon.getId());
		params.addBodyParameter("action", "other");
		params.addBodyParameter("pageNum", "5");
		params.addBodyParameter("pageNow", "1");
		HttpUtils httpUtils = BaseApplication.httpUtils;
		httpUtils.send(HttpMethod.POST, AppConstants.GETCOMMENTS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LogUtils.e("网络异常");
				hideDialog();
				mPullRefreshScrollView.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				hideDialog();
				String result = arg0.result;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					int state = jsonObject.optInt("state");
					String results = jsonObject.optString("result");
					int total = jsonObject.optInt("total");
					if (total >= 6) {
						tv_nomore_comment.setVisibility(View.GONE);
						rl_comment_detail.setVisibility(View.VISIBLE);
						tv_look_morecomment.setText("查看全部" + total + "条评价");
					}
					mPullRefreshScrollView.onRefreshComplete();
					if (state != 200) {
						mPullRefreshScrollView.onRefreshComplete();
						if ("该商家暂无收到评论！".equals(results)) {
							tv_nomore_comment.setText(results);
						} else {

							ToastUtil.show(mContext, results);
						}
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
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	public void showDialog(String message) {
		try {
			if (dialog == null) {
				dialog = new ProgressDialog(this);
				dialog.setCancelable(false);
			}
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(true);
			dialog.setMessage(message);
			dialog.show();
		} catch (Exception e) {
			// 在其他线程调用dialog会报错
		}
	}

	public void hideDialog() {
		if (dialog != null && dialog.isShowing())
			try {
				dialog.dismiss();
			} catch (Exception e) {
			}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_sellerinfo_tell:
			Phone();
			break;
		case R.id.iv_title_bar_message:
			share();
			break;
		case R.id.iv_title_bar_collect:
			if (!iv_title_bar_collect.isChecked()) {
				way = "sub";				
				inintCollection();
			} else {		
				way = "add";
				inintCollection();
			}
			break;
		case R.id.tv_coupon_back:
			finish();
			break;
		case R.id.tv_coupondetail_buy:
			Intent intent = new Intent();
			intent.setClass(mContext, SubmitorderActivity.class);
			intent.putExtra("coupon_name", seller_name);
			intent.putExtra("currentPrice", currentPrice);
			intent.putExtra("seller_id", mAndCoupon.getId());
			intent.putExtra("coupon_id", coupon_id);
			startActivity(intent);
			break;
		case R.id.rl_sellerinfo_detail:
			Intent intent2 =new Intent(mContext, ShopDetailActivity.class);
			intent2.putExtra("seller_id", mAndCoupon.getId());
            startActivity(intent2);
			break;
		default:
			break;
		}
	}

	private void Phone() {
		// TODO Auto-generated method stub
		String phone = mAndCoupon.getPhone();
		if (phone != null && !phone.equals("")) {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			ToastUtil.show(mContext, "商家电话暂不可用！");
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
		inintCollection();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		LogUtils.e("requestCode---" + requestCode + "resultCode--------" + resultCode);
		switch (requestCode) {
		case 2:
			if (resultCode == Activity.RESULT_OK) {
				inintCollection();
				ActivityUtils.showShortToast(mContext, "登录成功");
			}                                                                                                                                         
			break;
		default:
			break;
		}
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if (amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0) {
			geoLat = amapLocation.getLatitude();
			geoLng = amapLocation.getLongitude();
			Log.e("经度---" + amapLocation.getLatitude(), "纬度---" + amapLocation.getLongitude());
			if (geoLat != 0 || geoLng != 0) {
				setText();
			}
		} else {
			Log.e("AmapErr", "Location ERR:" + amapLocation.getAMapException().getErrorCode());
		}
	}
	private void setText() {
		// TODO Auto-generated method stub
		
		if (geoLat!=0||geoLng!=0) {
			p1=new LatLng(mlatitude, mlongitude);
			p2=new LatLng(geoLat, geoLng);
			distance = AMapUtils.calculateLineDistance(p1, p2);
			if (distance<500.0) {
				tv_seller_location.setText("< 500m");
			}
			else if (distance>500&&distance<1000.0) {
				DecimalFormat decimalFormat=new DecimalFormat("#.0");
				tv_seller_location.setText(decimalFormat.format(distance)+"km");
			}else if (distance>1000.0) {
				DecimalFormat decimalFormat=new DecimalFormat("#.0");
				tv_seller_location.setText("> "+decimalFormat.format(distance/1000.0)+"km");
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 移除定位请求
		mLocationManagerProxy.removeUpdates(this);
		// 销毁定位
		mLocationManagerProxy.destroy();
	}
}
