package com.nightfair.mobille.activity;

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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class PersonalWalletActivity extends BaseActivity implements OnClickListener {

	private TextView mTv_title, tv_wallet_balance;
	private ImageView iv_back;
	protected Context mContext;
	private RelativeLayout rl_wallet_manage;
	private LinearLayout ll_wallet_add, ll_wallet_withdraw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_wallet);
		ActivityUtils.setActionBarByColor(this, R.layout.title_bar, R.color.title_color);
		mContext=this;
		initView();
		inintData();
	}


	private void initView() {
		// TODO Auto-generated method stub
		mTv_title = (TextView) findViewById(R.id.tv_actionbar_title);
		mTv_title.setText(R.string.actionbar_title_wallet);
		iv_back = (ImageView) findViewById(R.id.iv_actionbar_back);
		tv_wallet_balance = (TextView) findViewById(R.id.tv_wallet_balance);
		ll_wallet_add = (LinearLayout) findViewById(R.id.ll_wallet_add);
		ll_wallet_withdraw = (LinearLayout) findViewById(R.id.ll_wallet_withdraw);
		rl_wallet_manage=(RelativeLayout) findViewById(R.id.rl_wallet_manage);
		mySetOnClickListener(iv_back, ll_wallet_withdraw, ll_wallet_add,rl_wallet_manage);
	}
	private void mySetOnClickListener(View... v) {
		for (View view : v) {
			view.setOnClickListener(this);
		}

	}

	private void inintData() {
		// TODO Auto-generated method stub
		HttpUtils httpUtils=BaseApplication.httpUtils;
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
					String balance=jsonObject.optString("balance");
					if (state != 200) {
						if(state==405){
							ToastUtil.show(mContext, results);	
							Intent intent  =new Intent("com.nightfair.buyer.action.login");
							startActivityForResult(intent, 2);	
						}									
					} else {
						tv_wallet_balance.setText("￥ "+Double.parseDouble(balance));
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
		switch (v.getId()) {
		case R.id.iv_actionbar_back:
			finish();
			break;
		case R.id.ll_wallet_withdraw:
			ToastUtil.show(mContext, "此功能开发中，敬请期待！");
			break;
		case R.id.ll_wallet_add:
			ActivityUtils.startActivity(mContext, RechargeActivity.class);
			break;
		case R.id.rl_wallet_manage:
			ToastUtil.show(mContext, "此功能开发中，敬请期待！");
			break;
		default:
			break;
		}

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		inintData();
		super.onResume();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		LogUtils.e("requestCode---"+requestCode+"resultCode--------"+resultCode);
		switch (requestCode) {
		case 2:
			if (resultCode == Activity.RESULT_OK){
				ActivityUtils.showShortToast(mContext, "登录成功");
			}else if (resultCode == Activity.RESULT_CANCELED) {
				finish();
			} 
			break;
		default:
			break;
		}
	}
}
