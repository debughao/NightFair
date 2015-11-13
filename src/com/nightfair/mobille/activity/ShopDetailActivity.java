package com.nightfair.mobille.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
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
import com.nightfair.mobille.adapter.ShopCouponAdapter;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.bean.Coupons;
import com.nightfair.mobille.bean.SellerAndCoupon;
import com.nightfair.mobille.bean.SellerCommentsBuyer;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.ImageLoadOptions;
import com.nightfair.mobille.util.ToastUtil;
import com.nightfair.mobille.view.FullyListView;
import com.nightfair.mobille.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ShopDetailActivity extends Activity implements OnClickListener, OnRefreshListener<ScrollView> {
	private Context mContext;
	private String seller_id;
	private TextView tv_coupon_back, tv_seller_name, tv_seller_rank, tv_selleradress, tv_commenter_nick,
			tv_comment_time, tv_comment_cotent, tv_nomore_comment,tv_look_morecomment;
	private RatingBar seller_ratingbar, comment_ratingbar;
	private LinearLayout ll_seller_comments;
	private FullyListView listView;
	private PullToRefreshScrollView sv_coupon_detail;
	private ImageView iv_coupon_img, iv_sellerinfo_tell;
	private CircleImageView iv_commenter_face;
	private SellerAndCoupon mAndCoupon;
	private ArrayList<Coupons> list = new ArrayList<Coupons>();
	private RelativeLayout rl_seller_detail_comment;
	private ShopCouponAdapter mAdapter;
	public static String imgUrl;
	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_detail);
		mContext = this;
		Intent intent = getIntent();
		seller_id = intent.getStringExtra("seller_id");
		LogUtils.e("------------"+seller_id);
		ActivityUtils.setActionBarLayout(getActionBar(), mContext, R.layout.title_bar_coupon);
		inintView();
		
		ininData();

		mAdapter = new ShopCouponAdapter(list, mContext);
		listView.setAdapter(mAdapter);
	}

	private void inintView() {
		// TODO Auto-generated method stub
		tv_coupon_back = (TextView) findViewById(R.id.tv_coupon_back);
		tv_coupon_back.setText("商家详情");
		tv_coupon_back.setOnClickListener(this);
		sv_coupon_detail = (PullToRefreshScrollView) findViewById(R.id.sv_shop_detail);
		iv_coupon_img = (ImageView) findViewById(R.id.iv_shop_img);
		tv_seller_name = (TextView) findViewById(R.id.tv_sellerdetail_name);
		seller_ratingbar = (RatingBar) findViewById(R.id.sellerdetail_ratingbar);
		tv_seller_rank = (TextView) findViewById(R.id.tv_sellerdetail_rank);
		tv_selleradress = (TextView) findViewById(R.id.tv_sellerdetailadress);
		iv_sellerinfo_tell = (ImageView) findViewById(R.id.iv_sellerdetail_tell);
		listView = (FullyListView) findViewById(R.id.lv_sellerdetail_coupon);
		iv_commenter_face = (CircleImageView) findViewById(R.id.iv_sellerdetail_face);
		tv_commenter_nick = (TextView) findViewById(R.id.tv_commenter_nicks);
		comment_ratingbar = (RatingBar) findViewById(R.id.sellerdetail_comment_ratingbar);
		tv_comment_cotent = (TextView) findViewById(R.id.tv_seller_comment_cotents);
		ll_seller_comments = (LinearLayout) findViewById(R.id.ll_seller_comments);
		tv_comment_time= (TextView) findViewById(R.id.tv_seller_time);
		rl_seller_detail_comment = (RelativeLayout) findViewById(R.id.rl_seller_detail_comment);
		tv_nomore_comment = (TextView) findViewById(R.id.tv_nomore_comment);
		tv_look_morecomment= (TextView) findViewById(R.id.tv_look_morecomment);
		iv_sellerinfo_tell.setOnClickListener(this);
	}

	private void ininData() {
		// TODO Auto-generated method stub
		showDialog("加载中...");
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("action", "getallbysellerrid");
		params.addBodyParameter("seller_id", seller_id);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, AppConstants.GETCOUPON, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LogUtils.e("网络异常");
				sv_coupon_detail.onRefreshComplete();
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
						sv_coupon_detail.onRefreshComplete();
						ToastUtil.show(mContext, results);
					} else {
						String data = jsonObject.optString("data");
						Gson gson = new Gson();
						Type typeOfT = new TypeToken<SellerAndCoupon>() {
						}.getType();
						LogUtils.e("sdsds---------" + data);
						mAndCoupon = gson.fromJson(data, typeOfT);
						imgUrl = mAndCoupon.getImg();
						BaseApplication.mCache.put("seller", mAndCoupon);
						ArrayList<Coupons> coupons = mAndCoupon.getCoupons();
						setText();
						inintDataComment();
						list.clear();
						list.addAll(coupons);
						mAdapter.notifyDataSetChanged();
						sv_coupon_detail.onRefreshComplete();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			private void setText() {
				// TODO Auto-generated method stub
				if (mAndCoupon != null) {
					tv_seller_name.setText(mAndCoupon.getSeller_name());
					seller_ratingbar.setRating(Float.parseFloat(mAndCoupon.getRank()));
					tv_seller_rank.setText(mAndCoupon.getRank() + "评分");
					tv_selleradress.setText(mAndCoupon.getCity() + mAndCoupon.getArer() + mAndCoupon.getStreet());
					BitmapLoadCallBack<ImageView> callback = new DefaultBitmapLoadCallBack<ImageView>() {

						@Override
						public void onLoadStarted(ImageView container, String uri, BitmapDisplayConfig config) {
							super.onLoadStarted(container, uri, config);
						}

						@Override
						public void onLoadCompleted(ImageView container, String uri, Bitmap bitmap,
								BitmapDisplayConfig config, BitmapLoadFrom from) {
							super.onLoadCompleted(container, uri, bitmap, config, from);
							int bwidth = bitmap.getWidth();
							int bHeight = bitmap.getHeight();
							@SuppressWarnings("deprecation")
							int width = getWindowManager().getDefaultDisplay().getWidth();
							int height = width * bHeight / bwidth;
							LayoutParams param = iv_coupon_img.getLayoutParams();
							param.height = height;
							iv_coupon_img.setLayoutParams(param);
						}
					};
					BitmapUtils bitmapUtils = BaseApplication.bitmapUtils;
					bitmapUtils.configDefaultLoadFailedImage(getResources().getDrawable(R.drawable.ic_head_default));
					bitmapUtils.display(iv_coupon_img, AppConstants.SERVERIP + mAndCoupon.getImg(), callback);
				}
			}
		});
	}
	private void inintDataComment() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("seller_id", mAndCoupon.getId());
		params.addBodyParameter("action", "other");
		params.addBodyParameter("pageNum", "1");
		params.addBodyParameter("pageNow", "1");
		HttpUtils httpUtils = BaseApplication.httpUtils;
		httpUtils.send(HttpMethod.POST, AppConstants.GETCOMMENTS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				hideDialog();
				LogUtils.e("网络异常");
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
					if (total >= 1) {
						tv_nomore_comment.setVisibility(View.GONE);
						rl_seller_detail_comment.setVisibility(View.VISIBLE);
						tv_look_morecomment.setText("查看全部" + total + "条评价");
					}else {
						ll_seller_comments.setVisibility(View.GONE);
						tv_nomore_comment.setVisibility(View.VISIBLE);
						
					}
					if (state != 200) {
						if ("该商家暂无收到评论！".equals(results)) {
							tv_nomore_comment.setVisibility(View.VISIBLE);
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
						tv_commenter_nick.setText(sellerAndCoupons.getComments().get(0).getNickname());
						tv_comment_time.setText(sellerAndCoupons.getComments().get(0).getTime());
						tv_comment_cotent.setText(sellerAndCoupons.getComments().get(0).getComment());
						comment_ratingbar.setRating((float)sellerAndCoupons.getComments().get(0).getGrade());
						ImageLoader.getInstance().displayImage(AppConstants.SERVERIP+sellerAndCoupons.getComments().get(0).getBuyerimg(), iv_commenter_face, ImageLoadOptions.getOptions());
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

		case R.id.tv_coupon_back:
			finish();
			break;
		case R.id.iv_sellerdetail_tell:
			Phone();
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

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		ininData();
	}

}
