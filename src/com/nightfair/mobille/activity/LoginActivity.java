package com.nightfair.mobille.activity;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nightfair.mobille.R;
import com.nightfair.mobille.base.Activitybase;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.bean.Buyer;
import com.nightfair.mobille.bean.BuyerInfo;
import com.nightfair.mobille.bean.User;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.ErrCodeUtils;
import com.nightfair.mobille.util.MD5Util;
import com.nightfair.mobille.util.NetUtils;
import com.nightfair.mobille.util.ToastUtil;
import com.nightfair.mobille.view.ClearEditText;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.SaveListener;

@SuppressLint("ResourceAsColor")
public class LoginActivity extends Activitybase implements OnClickListener {

	private TextView tv_login_cancel;
	private ClearEditText et_login_user;
	private ClearEditText et_login_password;
	private Button bt_login_check;
	private String username;
	private String password;
	private LoginActivity mContent;
	private ImageView iv_login_eye;
	private TextView tv_register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ActivityUtils.setActionBarLayout(getActionBar(), LoginActivity.this, R.layout.title_bar_login);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarDrawable(this.getResources().getDrawable(R.drawable.bg_login_actionbar), this);
		mContent = this;
		inintsView();
	}

	private void inintsView() {
		tv_login_cancel = (TextView) findViewById(R.id.tv_login_cancel);
		tv_login_cancel.setOnClickListener(this);
		et_login_user = (ClearEditText) findViewById(R.id.et_login_user);
		et_login_password = (ClearEditText) findViewById(R.id.et_login_password);
		bt_login_check = (Button) findViewById(R.id.bt_login_check);

		bt_login_check.setOnClickListener(this);
		tv_register = (TextView) findViewById(R.id.tv_login_register);
		tv_register.setOnClickListener(this);
		iv_login_eye = (ImageView) findViewById(R.id.iv_login_eye);
		iv_login_eye.setOnClickListener(this);

	}

	private void changePwdDisplay() {
		if (et_login_password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
			et_login_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			iv_login_eye.setImageResource(R.drawable.ic_pwd_hide);
		} else {
			et_login_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			iv_login_eye.setImageResource(R.drawable.ic_pwd_show);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_login_cancel:
			finish();
			break;
		case R.id.bt_login_check:
			normalLogin();
			break;
		case R.id.iv_login_eye:
			changePwdDisplay();
			break;
		case R.id.tv_login_register:
			Intent intent = new Intent("com.nightfair.register.action.callback");
			startActivityForResult(intent, 1);		
			break;
		default:
			break;
		}

	}

	private void normalLogin() {
		final ProgressDialog progress = new ProgressDialog(
				LoginActivity.this);
		progress.setMessage("正在登陆...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		username = et_login_user.getText().toString().trim();
		password = et_login_password.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			et_login_user.setShakeAnimation();
			ActivityUtils.showShortToast(mContent, "登录名不能为空");
			return;
		} else if (TextUtils.isEmpty(password)) {
			et_login_password.setShakeAnimation();
			ActivityUtils.showShortToast(mContent, "密码不能为空");
			return;
		} else {					
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			userManager.login(user, new SaveListener() {
				
				@Override
				public void onSuccess() {
//					runOnUiThread(new Runnable() {
//						public void run() {
//							progress.setMessage("正在获取好友列表...");
//						}
//					});
					//更新用户的地理位置以及好友的资料
					updateUserInfos();
				}			
				@Override
				public void onFailure( int arg0, String arg1) {
					progress.dismiss();
					BmobLog.i(arg1);
					
				}
			});
			
			RequestParams params = new RequestParams();
			params.addBodyParameter("key", AppConstants.KEY);
			params.addBodyParameter("userName", username);
			params.addBodyParameter("userpassword", MD5Util.MD5(password));
			final HttpUtils http = BaseApplication.httpUtils;
			http.configTimeout(3000);
			http.send(HttpMethod.POST, AppConstants.LOGINAPIURL, params, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					System.out.println(arg1);
					NetUtils.coonFairException(arg1, mContent);
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					String resultStatus = null;
					try {
						JSONObject jsonObject = new JSONObject(arg0.result.toString());
						resultStatus = jsonObject.get("status").toString();
						if ("200".equals(resultStatus)) {
							progress.dismiss();
							int userid = jsonObject.getInt("user_id");
							Buyer buyer = new Buyer(userid, username, password, true);
							String result = jsonObject.optString("info");
							Gson gson = new Gson();
							BuyerInfo buyerInfo = gson.fromJson(result, new TypeToken<BuyerInfo>() {
							}.getType());
							buyerInfo.buyer = buyer;
							BaseApplication.userid = userid;
							BaseApplication.buyerInfo = buyerInfo;
							BaseApplication.mBuyerDao.insertBuyer(buyer);
							BaseApplication.mBuyerDao.insertInfo(buyerInfo, userid);
							Intent intent = new Intent();
							setResult(Activity.RESULT_OK, intent);
							finish();
						} else {
							ToastUtil.showCenter(mContent, ErrCodeUtils.USERNAME_PASSWORD_ERROR);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode==Activity.RESULT_OK) {
				username=data.getStringExtra("phone");
				password=data.getStringExtra("password");
				et_login_user.setText(username);
				et_login_password.setText(password);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void finish() {

		super.finish();
	}

}
