package com.nightfair.mobille.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.OrderQueryListener;
import com.bmob.pay.tool.PayListener;
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PayActivity extends BaseActivity implements OnClickListener {
	protected Context mContext;
	private TextView mTv_title, tv_pay_order, tv_pay_total, tv_remain_pay, tv_wallet_remain, tv_wallet_ok;
	private String action, money, balance, ordertitle;
	private BmobPay bmobPay;
	private RelativeLayout rl_wallet_pay, rl_wallet_alipay, rl_wallet_wenxin;
	private CheckBox ck_pay_pay, ck_pay_alipay, ck_pay_wenxin;
	private ProgressDialog dialog;
	private String orderid, recharge_way, payThirdMoney, payownMoney;
	boolean trade_state = false;
	private double doubleBalance, doubleMoney, doubleReaminMoney;
	DecimalFormat df = new DecimalFormat("#.00");// 保留2位小数
	private LinearLayout pay_parent;
	private int order_id;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		Intent intent = getIntent();
		action = intent.getStringExtra("action");
		money = intent.getStringExtra("money");
		ordertitle = intent.getStringExtra("ordertitle");
		order_id = intent.getIntExtra("order_id", 0);
		payThirdMoney = money;
		doubleMoney = Double.parseDouble(money);
		LogUtils.e("action" + action + "金额" + money + "order_id" + order_id);
		mContext = this;
		ActivityUtils.setActionBarLayout(getActionBar(), mContext, R.layout.title_bar_recharge);
		bmobPay = new BmobPay(PayActivity.this);
		initView();
		inintData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mTv_title = (TextView) findViewById(R.id.tv_recharge_back);
		mTv_title.setText(R.string.actionbar_title_pay);
		tv_pay_order = (TextView) findViewById(R.id.tv_pay_order);
		tv_pay_total = (TextView) findViewById(R.id.tv_pay_total);
		tv_remain_pay = (TextView) findViewById(R.id.tv_remain_pay);
		rl_wallet_pay = (RelativeLayout) findViewById(R.id.rl_wallet_pay);
		rl_wallet_alipay = (RelativeLayout) findViewById(R.id.rl_wallet_alipay);
		rl_wallet_wenxin = (RelativeLayout) findViewById(R.id.rl_wallet_weixin);
		pay_parent = (LinearLayout) findViewById(R.id.pay_parent);
		ck_pay_pay = (CheckBox) findViewById(R.id.ck_pay_pay);
		ck_pay_alipay = (CheckBox) findViewById(R.id.ck_pay_alipay);
		ck_pay_wenxin = (CheckBox) findViewById(R.id.ck_pay_wenxin);
		tv_wallet_remain = (TextView) findViewById(R.id.tv_wallet_remain);
		tv_wallet_ok = (TextView) findViewById(R.id.tv_wallet_ok);
		rl_wallet_pay.setOnClickListener(this);
		rl_wallet_alipay.setOnClickListener(this);
		rl_wallet_wenxin.setOnClickListener(this);
		mTv_title.setOnClickListener(this);
		tv_wallet_ok.setOnClickListener(this);
		tv_pay_order.setText(ordertitle);
		tv_pay_total.setText("￥" + money);
		tv_remain_pay.setText("￥" + money);
		if (action.equals("recharge")) {
			rl_wallet_pay.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_recharge_back:
			finish();
			break;
		case R.id.rl_wallet_pay:
			if (ck_pay_pay.isChecked()) {
				ck_pay_pay.setChecked(false);
			} else {
				ck_pay_pay.setChecked(true);
			}
			break;
		case R.id.rl_wallet_alipay:
			ck_pay_alipay.setChecked(true);
			break;
		case R.id.rl_wallet_weixin:
			ck_pay_wenxin.setChecked(true);

			break;
		case R.id.tv_wallet_ok:
			if (action.equals("recharge")) {
				if (ck_pay_alipay.isChecked()) {
					recharge_way = "ALIPAY";
					payByAli();

				} else if (ck_pay_wenxin.isChecked()) {
					recharge_way = "WECHATPAY";
					payByWeiXin();
				}
			} else if (action.equals("apy")) {
				showDialog("正在支付订单...");
				if (doubleBalance >= doubleMoney) {
					if (ck_pay_pay.isChecked()) {
						recharge_way = "PAY";
						payBypay();

					} else if (ck_pay_alipay.isChecked()) {
						recharge_way = "ALIPAY";
						payByAli();
					} else if (ck_pay_wenxin.isChecked()) {
						recharge_way = "WECHATPAY";
						payByWeiXin();
					}
				} else if (doubleBalance <doubleMoney) {
					if (ck_pay_pay.isChecked()) {
						if (ck_pay_alipay.isChecked()) {
							recharge_way = "ALIPAY";
							payByAli();
						} else if (ck_pay_wenxin.isChecked()) {
							recharge_way = "WECHATPAY";
							payByWeiXin();
						}
					}else if (!ck_pay_pay.isChecked()) {
						if (ck_pay_alipay.isChecked()) {
							recharge_way = "ALIPAY";
							payByAli();
						} else if (ck_pay_wenxin.isChecked()) {
							recharge_way = "WECHATPAY";
							payByWeiXin();
						}
					}
				}
			}
			break;
		default:
			break;
		}
	}
	private void payBypay() {
		// TODO Auto-generated method stub

		HttpUtils httpUtils = BaseApplication.httpUtils;
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("action", "pay");
		params.addBodyParameter("money", payownMoney);
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
				try {
					jsonObject = new JSONObject(result);
					int state = jsonObject.optInt("state");
					String results = jsonObject.optString("result");
					if (state != 200) {
						ToastUtil.show(mContext, results);
						if (state == 405) {
							Intent intent = new Intent("com.nightfair.buyer.action.login");
							startActivityForResult(intent, 2);
						}
					} else {
						hideDialog();
						Intent intent = new Intent();
						setResult(Activity.RESULT_OK, intent);
						finish();
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

	// 默认为0.01
	public double getPrice() {
		double price = 0.01;
		try {
			if (action.equals("recharge")) {
				price = Double.parseDouble(money);
			} else {
				price = Double.parseDouble(payThirdMoney);
				ToastUtil.show(mContext, "dddd" + price);
			}

		} catch (NumberFormatException e) {
		}
		return price;
	}

	private void inintData() {
		// TODO Auto-generated method stub
		HttpUtils httpUtils = BaseApplication.httpUtils;
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("action", "select");
		httpUtils.send(HttpMethod.POST, AppConstants.GETWALLET, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
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
					balance = jsonObject.optString("balance");
					if (state != 200) {
						if (state == 405) {
							ToastUtil.show(mContext, results);
							Intent intent = new Intent("com.nightfair.buyer.action.login");
							startActivityForResult(intent, 2);
						}
					} else {
						doubleBalance = Double.parseDouble(balance);
						tv_wallet_remain.setText("￥ " + doubleBalance);
						pay_parent.setVisibility(View.VISIBLE);
						inintPayCheck();

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

	}

	private void inintPayCheck() {
		// TODO Auto-generated method stub
		ck_pay_pay.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					if (doubleBalance >= doubleMoney) {
						payThirdMoney = "0";
						tv_remain_pay.setText("￥0" + df.format(0.00));
						payownMoney = df.format(doubleMoney);// 自己支付方式
						ck_pay_wenxin.setChecked(false);
						ck_pay_alipay.setChecked(false);
					} else {
						doubleReaminMoney = doubleMoney - doubleBalance;
						tv_remain_pay.setText("￥" + df.format(doubleReaminMoney));
						payThirdMoney = df.format(doubleReaminMoney);
						payownMoney = df.format(doubleMoney - doubleReaminMoney);// 自己支付方式
						LogUtils.e("payownMoney"+payownMoney);
					}
				} else {
					if (doubleBalance >= doubleMoney) {
						ck_pay_alipay.setChecked(true);
					}
					payThirdMoney = money;
					tv_remain_pay.setText("￥" + money);
				}
			}
		});
		ck_pay_alipay.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					ck_pay_wenxin.setChecked(false);
					if (doubleBalance >= doubleMoney) {
						ck_pay_pay.setChecked(false);
					}
				} else {
					if (doubleBalance < doubleMoney) {
						ck_pay_wenxin.setChecked(true);
					}
				}
			}
		});
		ck_pay_wenxin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					ck_pay_alipay.setChecked(false);
					if (doubleBalance >= doubleMoney) {
						ck_pay_pay.setChecked(false);
					}
				} else {
					if (doubleBalance < doubleMoney) {
						ck_pay_alipay.setChecked(true);
					}
				}
			}
		});
	}

	// 调用支付宝支付
	public void payByAli() {
		showDialog("正在获取订单...");
		LogUtils.e("第三方支付金额" + getPrice());
		bmobPay.pay(getPrice(), ordertitle, new PayListener() {
			// 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
			@Override
			public void unknow() {

				Toast.makeText(mContext, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT).show();
				LogUtils.e(ordertitle + "'s pay status is unknow\n\n");
				hideDialog();
			}

			// 支付成功,如果金额较大请手动查询确认
			@Override
			public void succeed() {
				query();
				Toast.makeText(mContext, "支付成功!", Toast.LENGTH_SHORT).show();
				LogUtils.e(mContext + "'s pay status is success\n\n");
				hideDialog();
			}

			// 无论成功与否,返回订单号
			@Override
			public void orderId(String orderId) {
				// 此处应该保存订单号,比如保存进数据库等,以便以后查询
				orderid = orderId;
				LogUtils.e(ordertitle + "'s orderid is " + orderId + "\n\n");
				showDialog("获取订单成功!请等待跳转到支付页面~");
			}

			// 支付失败,原因可能是用户中断支付操作,也可能是网络原因
			@Override
			public void fail(int code, String reason) {
				Toast.makeText(PayActivity.this, "支付中断!", Toast.LENGTH_SHORT).show();
				LogUtils.e(
						ordertitle + "'s pay status is fail, error code is " + code + " ,reason is " + reason + "\n\n");
				hideDialog();
			}
		});
	}

	/**
	 * 调用微信支付
	 */

	public void payByWeiXin() {
		showDialog("正在获取订单...");
		bmobPay.payByWX(getPrice(), ordertitle, new PayListener() {

			// 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
			@Override
			public void unknow() {
				Toast.makeText(PayActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT).show();
				LogUtils.e(ordertitle + "'s pay status is unknow\n\n");
				hideDialog();
			}

			// 支付成功,如果金额较大请手动查询确认
			@Override
			public void succeed() {
				query();
				Toast.makeText(PayActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
				hideDialog();
			}

			// 无论成功与否,返回订单号
			@Override
			public void orderId(String orderId) {
				// 此处应该保存订单号,比如保存进数据库等,以便以后查询
				orderid = orderId;
				LogUtils.e(ordertitle + "'s orderid is " + orderId + "\n\n");
				showDialog("获取订单成功!请等待跳转到支付页面~");
			}

			// 支付失败,原因可能是用户中断支付操作,也可能是网络原因
			@Override
			public void fail(int code, String reason) {

				// 当code为-2,意味着用户中断了操作
				// code为-3意味着没有安装BmobPlugin插件
				if (code == -3) {
					new AlertDialog.Builder(mContext).setMessage("监测到你尚未安装支付插件,无法进行微信支付,请选择安装插件(已打包在本地,无流量消耗)还是用支付宝支付")
							.setPositiveButton("安装", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							installBmobPayPlugin("BmobPayPlugin.apk");
						}
					}).setNegativeButton("支付宝支付", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							payByAli();
						}
					}).create().show();
				} else {
					Toast.makeText(mContext, "支付中断!", Toast.LENGTH_SHORT).show();
				}
				LogUtils.e(
						ordertitle + "'s pay status is fail, error code is " + code + " ,reason is " + reason + "\n\n");
				hideDialog();
			}
		});
	}

	// 执行订单查询
	public void query() {
		bmobPay.query(orderid, new OrderQueryListener() {
			@Override
			public void succeed(String status) {
				LogUtils.e("pay status of" + orderid + " is " + status + "\n\n");
				if (status.equals("SUCCESS")) {
					LogUtils.e("----------------");
					if (action.equals("recharge")) {
						conFirmReCharger();
					} else {
						payBypay();
					}
				}
			}

			@Override
			public void fail(int code, String reason) {
				LogUtils.e("query order fail, error code is " + code + " ,reason is " + reason + "\n\n");
			}
		});
	}

	/*
	 * 充值保存到数据库
	 */
	private void conFirmReCharger() {
		// TODO Auto-generated method stub
		HttpUtils httpUtils = BaseApplication.httpUtils;
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("action", "add");
		params.addBodyParameter("recharge_way", recharge_way);
		params.addBodyParameter("transactionid", orderid);
		params.addBodyParameter("money", money);
		httpUtils.send(HttpMethod.POST, AppConstants.GETWALLET, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
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
					String balance = jsonObject.optString("balance");
					if (state != 200) {
						if (state == 405) {
							ToastUtil.show(mContext, results);
							Intent intent = new Intent("com.nightfair.buyer.action.login");
							startActivityForResult(intent, 2);
						}
					} else {
						tv_wallet_remain.setText("￥ " + Double.parseDouble(balance));
						finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void installBmobPayPlugin(String fileName) {
		try {
			InputStream is = getAssets().open(fileName);
			File file = new File(Environment.getExternalStorageDirectory() + File.separator + fileName);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + file), "application/vnd.android.package-archive");
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
