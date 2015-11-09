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
import com.nightfair.mobille.adapter.OrderManageAdapter;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.bean.BuyerOrder;
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
import com.nightfair.mobille.adapter.OrderManageAdapter.Callback;

public class OrderManageActivity extends BaseActivity implements OnClickListener, IXListViewListener {
	protected Context mContext;
	private TextView mTv_title, tv_seemore;
	private String action;
	private String state;
	private XListView mListView;
	private OrderManageAdapter mAdapter;
	private ArrayList<BuyerOrder> list = new ArrayList<BuyerOrder>();
	private LinearLayout ll_seemore;
	public SharePreferenceUtil mSharedUtil;
	public BaseApplication mApplication;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_manage);
		mApplication = BaseApplication.getInstance();
		mSharedUtil = mApplication.getSpUtil();
		Intent intent = getIntent();
		action = intent.getStringExtra("action");
		type = intent.getStringExtra("type");
		mContext = this;
		ActivityUtils.setActionBarLayout(getActionBar(), mContext, R.layout.title_bar_recharge);
		initView();
		initData();
		mAdapter = new OrderManageAdapter(list, mContext, new Callback() {

			@Override
			public void isEmpty(boolean isEmpty) {
				// TODO Auto-generated method stub
				if (isEmpty) {
					ll_seemore.setVisibility(View.VISIBLE);
				}
			}
		});
		mListView.setAdapter(mAdapter);

	}

	private void initView() {
		// TODO Auto-generated method stub
		mTv_title = (TextView) findViewById(R.id.tv_recharge_back);
		mTv_title.setOnClickListener(this);
		mListView = (XListView) findViewById(R.id.pull_refresh_list);
		ll_seemore = (LinearLayout) findViewById(R.id.ll_seemore);
		tv_seemore = (TextView) findViewById(R.id.tv_seemore);
		tv_seemore.setOnClickListener(this);
		inintSelect();
		initXListView();
	}

	private void inintSelect() {
		// TODO Auto-generated method stub
		if (action.equals("unpaid")) {
			state = "0";
			mTv_title.setText(R.string.text_order_unpaid);
		} else if (action.equals("unused")) {
			state = "1";
			mTv_title.setText(R.string.text_order_unused);

		} else if (action.equals("needfeedback")) {
			state = "2";
			mTv_title.setText(R.string.text_order_needfeedback);
		} else if (action.equals("refund")) {
			state = "3";
			mTv_title.setText(R.string.text_order_order_refund);
		} else if (action.equals("all")) {
			state = "4";
			mTv_title.setText(R.string.text_order_all);
		}
	}

	private void initData() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("action", "select");
		params.addBodyParameter("state", state);
		params.addBodyParameter("type", type);
		HttpUtils httpUtils = BaseApplication.httpUtils;
		httpUtils.send(HttpMethod.POST, AppConstants.GETORDER, params, new RequestCallBack<String>() {
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
						Type typeOfT = new TypeToken<ArrayList<BuyerOrder>>() {
						}.getType();
						ArrayList<BuyerOrder> buyerOrders = gson.fromJson(data, typeOfT);
						list.clear();
						list.addAll(buyerOrders);
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
	protected void onResume() {
		// TODO Auto-generated method stub
			initData();
		super.onResume();
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

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
			initData();

	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}
}
