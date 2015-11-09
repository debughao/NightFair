package com.nightfair.mobille.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

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
import com.nightfair.mobille.adapter.CollectionAdapter;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.bean.BuyerCollection;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.NetUtils;
import com.nightfair.mobille.util.SharePreferenceUtil;
import com.nightfair.mobille.util.ToastUtil;
import com.nightfair.mobille.view.XListView;
import com.nightfair.mobille.view.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyColltctionActivity extends BaseActivity implements OnClickListener,IXListViewListener{
	protected Context mContext;
	private TextView mTv_title,tv_seemore;
	private ArrayList<BuyerCollection> list = new ArrayList<BuyerCollection>();
	private CollectionAdapter mAdapter;
	private XListView mListView;
	private LinearLayout ll_seemore;
	public SharePreferenceUtil mSharedUtil;
	public BaseApplication mApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_colltction);
		mApplication = BaseApplication.getInstance();
		mSharedUtil = mApplication.getSpUtil();
		mContext = this;
		ActivityUtils.setActionBarLayout(getActionBar(), mContext, R.layout.title_bar_recharge);
		initView();
		initData();
		mAdapter = new CollectionAdapter(list, mContext);
		mListView.setAdapter(mAdapter);
	}
	private void initView() {
		// TODO Auto-generated method stub
		mTv_title = (TextView) findViewById(R.id.tv_recharge_back);
		mTv_title.setText(R.string.actionbar_title_mycollection);
		mTv_title.setOnClickListener(this);
		mListView = (XListView) findViewById(R.id.pull_refresh_list);
		ll_seemore = (LinearLayout) findViewById(R.id.ll_seemore);
		tv_seemore = (TextView) findViewById(R.id.tv_seemore);
		tv_seemore.setOnClickListener(this);
		initXListView();
	}
	private void initData() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		HttpUtils httpUtils = BaseApplication.httpUtils;
		httpUtils.send(HttpMethod.POST, AppConstants.GETCOLLECTION, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				mListView.stopRefresh();
				NetUtils.coonFairException(arg1, mContext);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				mListView.stopRefresh();
				String result = arg0.result;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					int state = jsonObject.optInt("state");
					String results = jsonObject.optString("result");
					if (state != 200) {
						if (state == 405) {
							ToastUtil.show(mContext, results);
							Intent intent = new Intent("com.nightfair.buyer.action.login");
							startActivityForResult(intent, 2);
						} else if (state == 201) {
							ll_seemore.setVisibility(View.VISIBLE);
							list.clear();
							mAdapter.notifyDataSetChanged();
						}
					} else {
						String data = jsonObject.optString("data");
						LogUtils.e("data---------" + data);
						Gson gson = new Gson();
						Type typeOfT = new TypeToken<ArrayList<BuyerCollection>>() {
						}.getType();
						ArrayList<BuyerCollection> buyerCollections = gson.fromJson(data, typeOfT);
						list.clear();
						list.addAll(buyerCollections);
						mAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void initXListView() {
		// 首先不允许加载更多

		mListView.setPullLoadEnable(false);
		// 允许下拉
		mListView.setPullRefreshEnable(true);
		// 设置监听器
		mListView.setXListViewListener(this);
		mListView.pullRefreshing();

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_recharge_back:
			finish();
			break;
		case R.id.tv_seemore:
			mSharedUtil.setAllowBackIndex(true);
			Intent intent = new Intent(mContext, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

			initData();

	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		LogUtils.e("requestCode---" + requestCode + "resultCode--------" + resultCode);
		switch (requestCode) {
		case 2:
			if (resultCode == Activity.RESULT_OK) {
				initData();
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
