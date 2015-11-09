package com.nightfair.mobille.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.bean.BuyerOrder;
import com.nightfair.mobille.bean.Coupon;
import com.nightfair.mobille.bean.Coupons;
import com.nightfair.mobille.bean.Order;
import com.nightfair.mobille.bean.SellerAndCoupon;
import com.nightfair.mobille.bean.SellerInfo;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.dialog.LoadingDialog;
import com.nightfair.mobille.dialog.SelectionDialog;
import com.nightfair.mobille.dialog.BaseDialog.OnConfirmListener;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.NetUtils;
import com.nightfair.mobille.util.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrderDetailActivity extends Activity implements OnClickListener {
	protected Context mContext;
	private TextView mTv_title, tv_order_sellername, tv_coupon_description, tv_order_amount, tv_detail_op, coupon_num,
			tv_datime, tv_detail_msg, tv_total, tv_sellerinfo_name, tv_sellerinfo_adress, tv_seller_location, tv_op,
			tv_orderid, tv_num;
	private int state;
	private ImageView iv_sellerinfo_tell;
	private ImageView iv_order_img;
	private BuyerOrder buyerOrder;
	private SellerInfo sellerInfo;
	private Order order;
	private Coupon coupon;
	private LinearLayout ll_coupon_num;
	private RelativeLayout rl_seller_detail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		mContext = this;
		Intent intent = getIntent();
		buyerOrder = (BuyerOrder) intent.getSerializableExtra("BuyerOrder");
		state = buyerOrder.getOrder().getState();
		sellerInfo = buyerOrder.getSellerInfo();
		order = buyerOrder.getOrder();
		coupon = buyerOrder.getCoupon();
		ActivityUtils.setActionBarLayout(getActionBar(), mContext, R.layout.title_bar_recharge);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mTv_title = (TextView) findViewById(R.id.tv_recharge_back);
		mTv_title.setText(R.string.actionbar_title_orderdetail);
		mTv_title.setOnClickListener(this);
		tv_order_sellername = (TextView) findViewById(R.id.tv_order_sellername);
		tv_coupon_description = (TextView) findViewById(R.id.tv_coupon_description);
		tv_order_amount = (TextView) findViewById(R.id.tv_order_amount);
		tv_detail_op = (TextView) findViewById(R.id.tv_detail_op);
		coupon_num = (TextView) findViewById(R.id.coupon_num);
		tv_detail_msg = (TextView) findViewById(R.id.tv_detail_msg);
		tv_sellerinfo_name = (TextView) findViewById(R.id.tv_sellerinfo_name);
		tv_sellerinfo_adress = (TextView) findViewById(R.id.tv_sellerinfo_adress);
		tv_seller_location = (TextView) findViewById(R.id.tv_seller_location);
		tv_orderid = (TextView) findViewById(R.id.tv_orderid);
		tv_datime = (TextView) findViewById(R.id.tv_datime);
		tv_num = (TextView) findViewById(R.id.tv_num);
		tv_total = (TextView) findViewById(R.id.tv_total);
		tv_op = (TextView) findViewById(R.id.tv_op);
		iv_sellerinfo_tell = (ImageView) findViewById(R.id.iv_sellerinfo_tell);
		iv_order_img = (ImageView) findViewById(R.id.iv_order_img);
		ll_coupon_num = (LinearLayout) findViewById(R.id.ll_coupon_num);
		rl_seller_detail = (RelativeLayout) findViewById(R.id.rl_seller_detail);
		rl_seller_detail.setOnClickListener(this);
		iv_sellerinfo_tell.setOnClickListener(this);
		tv_detail_op.setOnClickListener(this);
		initText();
		ininVisibieView();
	}

	private void initText() {
		// TODO Auto-generated method stub
		String currentPrice = coupon.getCurrent_price();
		String originalPrice = coupon.getOriginal_price();
		String price = "￥ " + currentPrice + "  ￥" + originalPrice;
		SpannableString spanString = new SpannableString(price);
		spanString.setSpan(new ForegroundColorSpan(0xffe75858), 0, currentPrice.length() + 2,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanString.setSpan(new AbsoluteSizeSpan(40), 2, currentPrice.length() + 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanString.setSpan(new StrikethroughSpan(), currentPrice.length() + 4,
				currentPrice.length() + originalPrice.length() + 5, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		tv_order_sellername.setText(sellerInfo.getSeller_name());
		tv_sellerinfo_name.setText(sellerInfo.getSeller_name());
		tv_coupon_description.setText(coupon.getDescription());
		tv_order_amount.setText(spanString);
		coupon_num.setText("券码：" + order.getCoupon_num());
		tv_sellerinfo_adress.setText(sellerInfo.getArer() + sellerInfo.getStreet());
		tv_orderid.setText(String.valueOf(coupon.getId()));
		tv_datime.setText(order.getOrder_time());
		tv_num.setText(order.getNum());
		tv_total.setText(String.valueOf(order.getAmount()));
		BitmapUtils bitmapUtils = BaseApplication.bitmapUtils;
		bitmapUtils.display(iv_order_img, AppConstants.SERVERIP + sellerInfo.getImg());
	}

	private void ininVisibieView() {
		// TODO Auto-generated method stub
		if (state == 0) {
			tv_op.setVisibility(View.VISIBLE);
			ll_coupon_num.setVisibility(View.GONE);
			tv_op.setOnClickListener(this);
		} else if (state == 1) {
			tv_detail_op.setText("申请退款");
			tv_detail_msg.setText("未消费");
		} else if (state == 2) {
			tv_op.setVisibility(View.VISIBLE);
			tv_op.setText("评价");
			tv_detail_msg.setText("已消费/待评价");
			tv_detail_op.setVisibility(View.GONE);
			tv_op.setOnClickListener(this);
		} else if (state == 3) {
			int isrefues=order.getIsrefues();
            if (isrefues==0) {
            	tv_detail_msg.setText("退款中");
			}else if (isrefues==1) {
				tv_detail_msg.setText("已退款");
			}
			tv_detail_op.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_recharge_back:
			finish();
			break;
		case R.id.iv_sellerinfo_tell:
			Phone();
			break;
		case R.id.tv_op:
			if (state == 0) {
				payOrder();
			} else if (state == 2) {
				Intent intent =new Intent(mContext, CommentActivity.class);
				intent.putExtra("ordeid", order.getOrder_id());
				intent.putExtra("sellerid", order.getSeller_id());
				startActivityForResult(intent, 1);
			}

			break;
		case R.id.tv_detail_op:
			refuesOrder();
			break;
		case R.id.rl_seller_detail:
			lookCouponDetail();
			break;
		default:
			break;
		}
	}

	private void payOrder() {
		// TODO Auto-generated method stub
		String seller_id = String.valueOf(sellerInfo.getId());
		Intent intent = new Intent();
		intent.setClass(mContext, SubmitorderActivity.class);
		intent.putExtra("action", "resubmint");
		intent.putExtra("coupon_name", sellerInfo.getSeller_name());
		intent.putExtra("currentPrice", coupon.getCurrent_price());
		intent.putExtra("num", order.getNum());
		intent.putExtra("seller_id", seller_id);
		intent.putExtra("coupon_id", String.valueOf(coupon.getId()));
		startActivity(intent);
	}

	private void Phone() {
		// TODO Auto-generated method stub
		String phone = sellerInfo.getPhone();
		if (phone != null && !phone.equals("")) {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			ToastUtil.show(mContext, "商家电话暂不可用！");
		}
	}

	private void refuesOrder() {
		// TODO Auto-generated method stub

		SelectionDialog dialog = new SelectionDialog(mContext, "确定退款吗？");
		dialog.setOnConfirmListener(new OnConfirmListener() {

			@Override
			public void onConfirm(String result) {
				// TODO Auto-generated method stub
				final LoadingDialog dialog = new LoadingDialog(mContext, "正在发送退款请求");
				dialog.show();
				int order_id = order.getOrder_id();
				HttpUtils httpUtils = BaseApplication.httpUtils;
				RequestParams params = new RequestParams();
				params.addBodyParameter("key", AppConstants.KEY);
				params.addBodyParameter("action", "refues");
				params.addBodyParameter("orderid", String.valueOf(order_id));
				httpUtils.send(HttpMethod.POST, AppConstants.GETORDER, params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						NetUtils.coonFairException(arg1, mContext);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String result = arg0.result;
						JSONObject jsonObject;
						dialog.dismiss();
						try {
							jsonObject = new JSONObject(result);
							int state = jsonObject.optInt("state");
							String results = jsonObject.optString("result");
							if (state == 200) {								
								ToastUtil.show(mContext, results);
								finish();
							} else if (state == 201) {
								ToastUtil.show(mContext, results);
							} else if (state == 405) {
								ToastUtil.show(mContext, results);
								Intent intent = new Intent("com.nightfair.buyer.action.login");
								startActivityForResult(intent, 2);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
		});
		dialog.show();
	}

	private void lookCouponDetail() {

		// TODO Auto-generated method stub
		Coupons coupons2 = new Coupons(coupon.getCurrent_price(), coupon.getDescription(), String.valueOf(state),
				String.valueOf(coupon.getSeller_id()), coupon.getUpdate_time(), String.valueOf(coupon.getId()),
				coupon.getOriginal_price(), String.valueOf(coupon.getIsguess()), coupon.getPublic_time(),
				String.valueOf(coupon.getSeller_counts()), String.valueOf(coupon.getIsrecommend()));
		ArrayList<Coupons> coupons = new ArrayList<Coupons>();
		coupons.add(coupons2);
		SellerAndCoupon sellerAndCoupon = new SellerAndCoupon(sellerInfo.getLongitude(),
				String.valueOf(sellerInfo.getUser_id()), sellerInfo.getArer(), String.valueOf(sellerInfo.getRank()),
				sellerInfo.getLatitude(), coupons, sellerInfo.getProvince(), sellerInfo.getPhone(),
				sellerInfo.getCity(), String.valueOf(sellerInfo.getId()), sellerInfo.getStreet(),
				sellerInfo.getSeller_name(), sellerInfo.getImg(), sellerInfo.getClassify_id());

		Intent intent = new Intent();
		intent.setClass(mContext, CouponDetailActivity.class);
		intent.putExtra("seller", sellerAndCoupon);
		intent.putExtra("couponposition", 0);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		LogUtils.e("requestCode---" + requestCode + "resultCode--------" + resultCode);
		switch (requestCode) {
		case 2:
			if (resultCode == Activity.RESULT_OK) {
				ActivityUtils.showShortToast(mContext, "登录成功");
			} else if (resultCode == Activity.RESULT_CANCELED) {
				finish();
			}
			break;
		case 1:
			if (resultCode == Activity.RESULT_OK) {
				ActivityUtils.showShortToast(mContext, "评论成功");
				finish();
			} 
			break;
		default:
			break;
		}
	}
}
