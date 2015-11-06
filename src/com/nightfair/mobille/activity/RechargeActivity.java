package com.nightfair.mobille.activity;

import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.view.ClearEditText;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class RechargeActivity extends BaseActivity implements OnClickListener {
	protected Context mContext;
	private TextView mTv_title, tv_wallet_ok;
	private ClearEditText cet_money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge);
		mContext = this;
		ActivityUtils.setActionBarLayout(getActionBar(), mContext, R.layout.title_bar_recharge);
		initView();
	}

	@SuppressLint("NewApi")
	private void initView() {
		// TODO Auto-generated method stub
		mTv_title = (TextView) findViewById(R.id.tv_recharge_back);
		mTv_title.setText(R.string.actionbar_title_recharge);
		mTv_title.setOnClickListener(this);
		cet_money = (ClearEditText) findViewById(R.id.cet_money);
		tv_wallet_ok = (TextView) findViewById(R.id.tv_wallet_ok);
		tv_wallet_ok.setOnClickListener(this);
		tv_wallet_ok.setBackground(getResources().getDrawable(R.drawable.actionbar_normal_con));
		setTextChangedListener(cet_money);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_recharge_back:
			finish();
			break;
		case R.id.tv_wallet_ok:
			String money=cet_money.getText().toString().trim();
			Intent intent = new Intent("com.nightfair.buyer.action.recharge");
			intent.putExtra("action", "recharge");
			intent.putExtra("ordertitle", "余额充值");
			intent.putExtra("money", money);
			startActivityForResult(intent, 0);
			break;
		default:
			break;
		}
	}

	private void setTextChangedListener(TextView... v) {
		for (TextView view : v) {
			view.addTextChangedListener(new TextWatcher() {
				@SuppressLint("NewApi")
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					if (after>0) {
						tv_wallet_ok.setBackground(getResources().getDrawable(R.drawable.button_serach_bg_con));
						tv_wallet_ok.setEnabled(true);
						
					}
					
				}

				@SuppressLint("NewApi")
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if (count>0) {
						tv_wallet_ok.setBackground(getResources().getDrawable(R.drawable.button_serach_bg_con));

						tv_wallet_ok.setEnabled(true);
					}
				}

				@SuppressLint("NewApi")
				@Override
				public void afterTextChanged(Editable s) {
					if (s.length()>0) {
						tv_wallet_ok.setBackground(getResources().getDrawable(R.drawable.button_serach_bg_con));
						tv_wallet_ok.setEnabled(true);
					}
					else {
						tv_wallet_ok.setBackground(getResources().getDrawable(R.drawable.actionbar_normal_con));					
						tv_wallet_ok.setEnabled(false);
					}
				}

			});
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		LogUtils.e("requestCode---"+requestCode+"resultCode--------"+resultCode);
		switch (requestCode) {
		case 0:
			if (resultCode == Activity.RESULT_OK){
				ActivityUtils.showShortToast(mContext, "充值成功");
				finish();
			}
			break;

		default:
			break;
		}
	}
}
