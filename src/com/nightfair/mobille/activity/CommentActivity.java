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
import com.nightfair.mobille.dialog.LoadingDialog;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.NetUtils;
import com.nightfair.mobille.util.ToastUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class CommentActivity extends BaseActivity implements OnClickListener {
	protected Context mContext;
	private TextView mTv_title, tv_ok;
	private int ordeid, sellerid;
	private RatingBar comment_taste, comment_environment, comment_services;
	private EditText het_comment;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		Intent intent = getIntent();
		ordeid = intent.getIntExtra("ordeid", 0);
		sellerid = intent.getIntExtra("sellerid", 0);
		mContext = this;
		ActivityUtils.setActionBarLayout(getActionBar(), mContext, R.layout.title_bar_recharge);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mTv_title = (TextView) findViewById(R.id.tv_recharge_back);
		mTv_title.setText(R.string.actionbar_title_comment);
		mTv_title.setOnClickListener(this);
		comment_taste = (RatingBar) findViewById(R.id.taste_ratingbar);
		comment_environment = (RatingBar) findViewById(R.id.comment_environment);
		comment_services = (RatingBar) findViewById(R.id.comment_services);
		tv_ok = (TextView) findViewById(R.id.tv_ok);
		het_comment = (EditText) findViewById(R.id.edit);
		tv_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_recharge_back:
			finish();
			break;
		case R.id.tv_ok:
			submit();
			break;
		default:
			break;
		}
	}

	private void submit() {
		// TODO Auto-generated method stub
		float grade = comment_taste.getRating() + comment_environment.getRating() + comment_services.getRating();
		DecimalFormat df = new DecimalFormat("#.00");// 保留2位小数
		String grades = df.format(grade / 3);// format 返回的是字符串
		String comment = het_comment.getText().toString();
		if (TextUtils.isEmpty(comment)) {
			ToastUtil.show(mContext, "评论内容不能为空");
		} else {
			final LoadingDialog dialog = new LoadingDialog(mContext, "正在提交评价信息...");
			dialog.show();
			HttpUtils httpUtils = BaseApplication.httpUtils;
			RequestParams params = new RequestParams();
			params.addBodyParameter("key", AppConstants.KEY);
			params.addBodyParameter("action", "comment");
			params.addBodyParameter("orderid", String.valueOf(ordeid));
			params.addBodyParameter("grades", grades);
			params.addBodyParameter("sellerid", String.valueOf(sellerid));
			params.addBodyParameter("comment", comment);
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
							Intent intent = new Intent();
							setResult(Activity.RESULT_OK, intent);
							finish();
						} else if (state == 404) {
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
		default:
			break;
		}
	}
}
