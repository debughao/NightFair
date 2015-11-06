package com.nightfair.mobille.activity;

import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.NetUtils;
import com.nightfair.mobille.util.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SubmitorderActivity extends BaseActivity implements OnClickListener {
	protected Context mContext;
	private ProgressDialog dialog;
	private TextView mTv_title, tv_coupon_num, tv_coupon_totalamount, tv_wallet_ok, tv_coupon_name, tv_coupon_price;
	private ImageView ib_coupon_num_sub, ib_coupon_num_add;
	private int n = 1;
	private double pricae;
	private double total_money;
	private String coupon_name, currentPrice, seller_id, coupon_id, amount;
 private String num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submitorder);
		mContext = this;
		ActivityUtils.setActionBarLayout(getActionBar(), mContext, R.layout.title_bar_recharge);
		Intent intent = getIntent();
		coupon_name = intent.getStringExtra("coupon_name");
		currentPrice = intent.getStringExtra("currentPrice");
		seller_id = intent.getStringExtra("seller_id");
		coupon_id = intent.getStringExtra("coupon_id");
		pricae = Double.parseDouble(currentPrice);
		total_money = pricae;
		amount=currentPrice;
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mTv_title = (TextView) findViewById(R.id.tv_recharge_back);
		mTv_title.setText(R.string.confirm_order);
		tv_coupon_num = (TextView) findViewById(R.id.tv_coupon_num);
		tv_coupon_name = (TextView) findViewById(R.id.tv_coupon_name);
		tv_coupon_price = (TextView) findViewById(R.id.tv_coupon_price);
		ib_coupon_num_sub = (ImageView) findViewById(R.id.ib_coupon_num_sub);
		ib_coupon_num_add = (ImageView) findViewById(R.id.ib_coupon_num_add);
		tv_coupon_totalamount = (TextView) findViewById(R.id.tv_coupon_totalamount);
		tv_wallet_ok = (TextView) findViewById(R.id.tv_wallet_ok);
		ib_coupon_num_sub.setEnabled(false);
		tv_coupon_name.setText(coupon_name + "优惠券");
		tv_coupon_price.setText("￥" + currentPrice);
		tv_coupon_totalamount.setText("￥" + total_money);
		ib_coupon_num_sub.setOnClickListener(this);
		ib_coupon_num_add.setOnClickListener(this);
		tv_wallet_ok.setOnClickListener(this);
		mTv_title.setOnClickListener(this);
		tv_coupon_num.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				LogUtils.e(s.toString());
				if (s.toString().equals("")) {
					n = 0;
				} else {
					n = Integer.parseInt(s.toString());
				}
				if (n > 1) {
					ib_coupon_num_sub.setEnabled(true);
					ib_coupon_num_sub.setImageDrawable(getResources().getDrawable(R.drawable.ic_coupon_num_sub));
				} else {
					ib_coupon_num_sub.setEnabled(false);
					ib_coupon_num_sub.setImageDrawable(getResources().getDrawable(R.drawable.ic_coupon_num_sub_gray));

				}
				DecimalFormat df = new DecimalFormat("#.00");// 保留2位小数

				if (n == 0) {
					total_money = 0;
					amount = "0";
					tv_coupon_totalamount.setText("￥" + amount);
				} else {
					total_money = pricae * n;
					amount = df.format(total_money);
					tv_coupon_totalamount.setText("￥" + amount);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

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
		case R.id.tv_recharge_back:
			finish();
			break;
		case R.id.ib_coupon_num_sub:
			n--;
			tv_coupon_num.setText(String.valueOf(n));
			break;
		case R.id.ib_coupon_num_add:
			n++;
			tv_coupon_num.setText(String.valueOf(n));
			break;
		case R.id.tv_wallet_ok:
			if (NetUtils.isNetworkAvailable(mContext)) {
				showDialog("正在提交订单，请稍后");
				submitOrder();
			} else {
				ToastUtil.show(mContext, "网络未连接，稍后再试");
			}
			break;
		default:
			break;
		}
	}

	private void submitOrder() {
		// TODO Auto-generated method stub
		if (total_money == 0) {
			hideDialog();
			new AlertDialog.Builder(this).setMessage("请选择输入的数量").setTitle("提示").setPositiveButton("确定", null).create()
					.show();
		}
		num=tv_coupon_num.getText().toString();
		HttpUtils httpUtils = BaseApplication.httpUtils;
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("action", "submit");
		params.addBodyParameter("seller_id", seller_id);
		params.addBodyParameter("coupon_id", coupon_id);
		params.addBodyParameter("num", num);
		params.addBodyParameter("money", amount);
		httpUtils.send(HttpMethod.POST, AppConstants.GETORDER, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				hideDialog();
				NetUtils.coonFairException(arg1, mContext);			
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
						hideDialog();
						if (state == 405) {
							ToastUtil.show(mContext, results);
							Intent intent = new Intent("com.nightfair.buyer.action.login");
							startActivityForResult(intent, 2);
						} else {														
							ToastUtil.show(mContext, results);
						}
					} else {
						int order_id=jsonObject.getInt("order_id");
						LogUtils.e("订单号"+order_id);
						Intent intent = new Intent("com.nightfair.buyer.action.recharge");
						intent.putExtra("order_id", order_id);
						intent.putExtra("action", "apy");
						intent.putExtra("ordertitle", coupon_name + "优惠券");
						intent.putExtra("money", amount);
						startActivityForResult(intent, 1);

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		LogUtils.e("requestCode---" + requestCode + "resultCode--------" + resultCode);
		switch (requestCode) {
		case 1:
			if (resultCode == Activity.RESULT_OK) {
				ActivityUtils.showShortToast(mContext, "支付成功");
				finish();
			}else if (resultCode == Activity.RESULT_CANCELED) {
				hideDialog();
			}
			break;
		case 2:
			if (resultCode == Activity.RESULT_OK) {
				submitOrder();
			}
			break;
		default:
			break;
		}
	}
}
