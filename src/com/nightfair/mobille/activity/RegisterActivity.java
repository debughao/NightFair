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
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.ErrCodeUtils;
import com.nightfair.mobille.util.NetUtils;
import com.nightfair.mobille.util.StringUtils;
import com.nightfair.mobille.util.ToastUtil;
import com.nightfair.mobille.view.ClearEditText;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

public class RegisterActivity extends Activity implements OnClickListener {

	private RegisterActivity mContext;
	private TextView tv_title;
	private ImageView iv_back;
	private Button btn_getCode, btn_register;
	private ClearEditText et_phone, et_code, et_password;
	private String phone;
	private String code;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ActivityUtils.setActionBarByDrawable(this, R.layout.title_bar_register,
				getResources().getDrawable(R.drawable.bg_login_actionbar));
		mContext = this;
		inintsView();
	}

	private void inintsView() {
		tv_title = (TextView) findViewById(R.id.tv_actionbar_title);
		tv_title.setText(R.string.text_regiser);
		btn_getCode = (Button) findViewById(R.id.btn_register_getcode);
		btn_getCode.setOnClickListener(this);
		btn_register = (Button) findViewById(R.id.bt_register_check);
		btn_register.setOnClickListener(this);
		iv_back = (ImageView) findViewById(R.id.iv_actionbar_back);
		iv_back.setOnClickListener(this);
		et_phone = (ClearEditText) findViewById(R.id.et_register_phone);
		et_password = (ClearEditText) findViewById(R.id.et_register_password);
		et_code = (ClearEditText) findViewById(R.id.et_register_code);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_actionbar_back:
			finish();
			break;
		case R.id.btn_register_getcode:
			getCode();
			break;
		case R.id.bt_register_check:
			register();
			break;
		default:
			break;
		}

	}

	private void register() {
		code = et_code.getText().toString();
		password = et_password.getText().toString();
		if (StringUtils.isPhone(mContext, et_phone)) {
			if (StringUtils.isVerCode(mContext, et_code)) {
				if (StringUtils.isPassword(mContext, et_password)) {
					validateCode();
				}
			}
		}
	}

	private void validateCode() {
		BmobSMS.verifySmsCode(mContext, phone, code, new VerifySMSCodeListener() {

			@Override
			public void done(BmobException ex) {
				if (ex == null) {// 短信验证码已验证成功
					LogUtils.e("验证通过");
					subRegister();

				} else {
					ToastUtil.show(mContext, ErrCodeUtils.ERROR_VERCODE_ERROR);
					LogUtils.e("验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
				}
			}
		});

	}

	protected void subRegister() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("phone", phone);
		params.addBodyParameter("password", password);
		params.addBodyParameter("action", "register");
		HttpUtils httpUtils = new HttpUtils(5000);
		httpUtils.send(HttpMethod.POST, AppConstants.REGISTER, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

				NetUtils.coonFairException(arg1, mContext);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {

				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(arg0.result.toString());
					String status = jsonObject.get("status").toString();
					String result = jsonObject.get("result").toString();
					if ("200".equals(status)) {
						ToastUtil.show(mContext, result);
						Intent intent = new Intent();
						intent.putExtra("phone", phone);
						intent.putExtra("password", password);
						setResult(Activity.RESULT_OK, intent);
						finish();
					} else {
						ToastUtil.show(mContext, result);
					}
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 获取验证码进行手机号验证
	 */
	private void getCode() {
		phone = et_phone.getText().toString();
		if (StringUtils.isPhone(this, et_phone)) {
			isAlready();
		}
	}

	/*
	 * 判断手机号是否被注册过
	 */
	private void isAlready() {

		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("phone", phone);
		params.addBodyParameter("action", "isAlready");
		HttpUtils httpUtils = new HttpUtils(3000);
		httpUtils.send(HttpMethod.POST, AppConstants.REGISTER, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				NetUtils.coonFairException(arg1, mContext);

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(arg0.result.toString());
					String status = jsonObject.get("status").toString();
					String result = jsonObject.get("result").toString();
					if ("200".equals(status)) {
						btn_getCode.setEnabled(false);
						new DownTimer(60000 * 2, 1000).start();
						sendCode();

					} else {
						ToastUtil.show(mContext, result);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

	}

	protected void sendCode() {
		
		BmobSMS.requestSMSCode(mContext, phone, "注册模板", new RequestSMSCodeListener() {

			@Override
			public void done(Integer smsId, BmobException ex) {
				// TODO Auto-generated method stub
				if (ex == null) {
					LogUtils.i("短信发送成功，短信id：" + smsId);// 用于查询本次短信发送详情
				} else {
					LogUtils.i("errorCode = " + ex.getErrorCode() + ",errorMsg = " + ex.getLocalizedMessage());
				}
			}

		});

	}

	/**
	 * 
	 * 倒计时验证码获取
	 *
	 */
	class DownTimer extends CountDownTimer {

		public DownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			btn_getCode.setText(millisUntilFinished / 1000 + "秒");
		}

		@Override
		public void onFinish() {
			btn_getCode.setText("重新获取");
			btn_getCode.setEnabled(true);
		}
	}
}
