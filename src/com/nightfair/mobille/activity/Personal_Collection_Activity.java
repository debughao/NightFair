package com.nightfair.mobille.activity;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.config.AppConstants;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class Personal_Collection_Activity extends BaseActivity {

	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal__collection);
		tv = (TextView) findViewById(R.id.tv_cooike);
		SharedPreferences sharedPreferences = getSharedPreferences("share_data", 
				Activity.MODE_PRIVATE); 
				String name = sharedPreferences.getString("sessionid", "");
			System.out.println(name);
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("key", AppConstants.KEY);
		requestParams.addBodyParameter("action", "collection");
		HttpUtils httpUtils = BaseApplication.httpUtils;
		httpUtils.send(HttpMethod.POST, AppConstants.USERUPDATE, requestParams, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

				tv.setText("失败");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {

				tv.setText("成功");

			}
		});

	}

	@Override
	public void finish() {
	
		super.finish();
	}
}
