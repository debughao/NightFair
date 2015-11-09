package com.nightfair.mobille.activity;

import java.util.ArrayList;

import org.apache.http.HttpHost;
import org.json.JSONException;
import org.json.JSONObject;

import com.aibang.open.client.AibangApi;
import com.aibang.open.client.exception.AibangException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.adapter.BusTranferSegAdapter;
import com.nightfair.mobille.bean.Bus;
import com.nightfair.mobille.bean.Buses;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

public class TestActivity extends Activity {
	private ListView mListView;
	private BusTranferSegAdapter mAdapter;
	private ArrayList<Bus> mList = new ArrayList<Bus>();
	private AibangApi mAibang;
	private AibangAsyncTask mAsyncTask;
	// 这里请使用您申请的API KEY
	private static final String API_KEY = "f41c8afccc586de03a99c86097e98ccb";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		mListView = (ListView) findViewById(R.id.lv_bus_tranfer);
		mAdapter = new BusTranferSegAdapter(mList, TestActivity.this);

		mAibang = new AibangApi(API_KEY);
		mAsyncTask = new AibangAsyncTask("bus");
		if (mAsyncTask != null) {
			mAsyncTask.execute();

		}

	}

	public class AibangAsyncTask extends AsyncTask<Void, Void, JSONObject> {
		 String mAction;

		public AibangAsyncTask(String action) {
			mAction = action;
		}

		protected void onPreExecute() {
			// Toast.makeText(getActivity(), "正在请求...",
			// Toast.LENGTH_SHORT).show();
		}

		protected void onPostExecute(JSONObject result) {
			LogUtils.e("--------onPostExecute---------" + result.toString());	
			String buses = result.optString("buses");
			Gson gson = new Gson();
			Buses bus = gson.fromJson(buses, new TypeToken<Buses>() {
			}.getType());
			mListView.setAdapter(mAdapter);
			mList.addAll(bus.getBus());
			mAdapter.notifyDataSetChanged();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			mAibang.setProxy(getProxy());
			try {
				try {
					return new JSONObject(
							mAibang.bus("苏州", null, null, 120.743549f, 31.274844f, 120.627823f, 31.304239f, null, null, null));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (AibangException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}

	@SuppressLint("DefaultLocale")
	private HttpHost getProxy() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		HttpHost none_host = null;
		if (cm == null) {
			return none_host;
		}

		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			return none_host;
		}

		if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
			return null;
		} else if (ni.getType() == ConnectivityManager.TYPE_MOBILE) {
			String extra = ni.getExtraInfo();
			if (TextUtils.isEmpty(extra)) {
				return none_host;
			}

			extra = extra.toLowerCase();
			if (extra.contains("cmnet") || extra.contains("ctnet") || extra.contains("uninet")
					|| extra.contains("3gnet")) {
				return none_host;
			} else if (extra.contains("cmwap") || extra.contains("uniwap") || extra.contains("3gwap")) {
				return new HttpHost("10.0.0.172");
			} else if (extra.contains("ctwap")) {
				return new HttpHost("10.0.0.200");
			}
		}

		return none_host;
	}

}
