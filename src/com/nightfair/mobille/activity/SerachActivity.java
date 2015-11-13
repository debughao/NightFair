package com.nightfair.mobille.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.adapter.SerachAdapter;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.bean.Nearby;
import com.nightfair.mobille.bean.SellerInfo;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.ToastUtil;
import com.nightfair.mobille.view.ClearEditText;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class SerachActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
	protected Context mContext;
	private ImageButton iv_back;
	private ClearEditText ce_serache;
	private ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(10);
	private String currentSearchTip;
	private InputMethodManager inputMethodManager;
	private ListView mListView;
	private SerachAdapter mAdapter;
	private ArrayList<SellerInfo> list = new ArrayList<SellerInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_serach);
		mContext = this;
		ActivityUtils.setActionBarLayout(getActionBar(), mContext, R.layout.title_bar_search);
		initView();
		mAdapter = new SerachAdapter(mContext, list);
		mListView.setAdapter(mAdapter);
	}

	private void initView() {
		// TODO Auto-generated method stub
		iv_back = (ImageButton) findViewById(R.id.iv_actionbar_back);
		iv_back.setOnClickListener(this);
		ce_serache = (ClearEditText) findViewById(R.id.ce_serach);
		mListView = (ListView) findViewById(R.id.lv_serach);
		mListView.setOnItemClickListener(this);
		ce_serache.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (s != null && s.length() > 0) {
					currentSearchTip = s.toString().trim();
					showSearchTip(currentSearchTip);
				} else if (s.length() == 0) {
					list.clear();
					mAdapter.notifyDataSetChanged();
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() == 0) {
					list.clear();
					mAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	class SearchTipThread implements Runnable {

		String newText;

		public SearchTipThread(String newText) {
			this.newText = newText;
		}

		public void run() {
			// keep only one thread to load current search tip, u can get data
			// from network here
			if (newText != null && newText.equals(currentSearchTip)) {
				ininData(newText);
			}
		}
	}

	public void showSearchTip(String newText) {
		schedule(new SearchTipThread(newText), 500);
	}

	public void ininData(String newText) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("action", "serach");
		params.addBodyParameter("sellername", newText);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, AppConstants.GETNEARBY, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LogUtils.e("网络异常");

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					int state = jsonObject.optInt("state");
					if (state == 200) {
						String data = jsonObject.optString("data");
						Gson gson = new Gson();
						Type typeOfT = new TypeToken<ArrayList<SellerInfo>>() {
						}.getType();
						LogUtils.e("sdsds---------" + data);
						ArrayList<SellerInfo> sellerInfos = gson.fromJson(data, typeOfT);
						list.clear();
						list.addAll(sellerInfos);
						mAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ScheduledFuture<?> schedule(Runnable command, long delayTimeMills) {
		return scheduledExecutor.schedule(command, delayTimeMills, TimeUnit.MILLISECONDS);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_actionbar_back:
			finish();
			break;
		default:
			break;
		}
	}

	private void hideSoftInput() {
		if (inputMethodManager != null) {
			View v = this.getCurrentFocus();
			if (v == null) {
				return;
			}

			inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			ce_serache.clearFocus();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		 hideSoftInput();
		 Intent intent =new Intent(mContext, ShopDetailActivity.class);
         intent.putExtra("seller_id", String.valueOf(list.get(position).getId()));
        startActivity(intent);
	}

}
