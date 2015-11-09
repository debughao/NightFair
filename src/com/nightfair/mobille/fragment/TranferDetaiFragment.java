package com.nightfair.mobille.fragment;

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
import com.nightfair.mobille.bean.SegParm;
import com.nightfair.mobille.util.ToastUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@SuppressLint("DefaultLocale")
public class TranferDetaiFragment extends Fragment {
	public static final String RC = "segParm";
	private SegParm rc ;
	private View mView;
	private ListView mListView;
	private BusTranferSegAdapter mAdapter;
	private ArrayList<Bus> mList = new ArrayList<Bus>();
	private AibangApi mAibang;
	private AibangAsyncTask mAsyncTask;
	// 这里请使用您申请的API KEY
	private static final String API_KEY = "8ac76173c6c65209f67d3f667ac202af";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle arguments = getArguments();
		if (arguments != null) {
			rc = (SegParm) arguments.getSerializable(RC);
			LogUtils.e("rc-------------"+rc);
		}
		if (mView == null) {
			mView = inflater.inflate(R.layout.bus_tranfer, container, false);
		}
		ViewGroup parent = (ViewGroup) mView.getParent();
		if (parent != null) {
			parent.removeView(mView);
		}
		initView();
		mAibang = new AibangApi(API_KEY);
		mAsyncTask = new AibangAsyncTask("bus");
		if (mAsyncTask != null) {
			mAsyncTask.execute();
		}		
		return mView;
	}

	

	private void initView() {
		mListView = (ListView) mView.findViewById(R.id.lv_bus_tranfer);
		mAdapter=new BusTranferSegAdapter(mList, getActivity());
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
			LogUtils.e("-------------JSONObject result"+result.toString());
			if (null!=result){
				String buses = result.optString("buses");
				String result_num=result.optString("result_num");
				
			if ("0".equals(result_num)) {
				ToastUtil.show(getActivity(), "查询无结果");
			}else {
				Gson gson = new Gson();
				Buses bus = gson.fromJson(buses, new TypeToken<Buses>() {
				}.getType());
				mListView.setAdapter(mAdapter);
				mList.clear();
				mList.addAll(bus.getBus());
				mAdapter.notifyDataSetChanged();
			}				
			}			
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			mAibang.setProxy(getProxy());
			try {
				try {
					return new JSONObject(
				//	mAibang.bus("苏州", null, null, 120.743549f, 31.274844f, 120.627823f, 31.304239f, null, null, null).toString());
				mAibang.bus(rc.getCity().substring(0, rc.getCity().length() - 1), null, null, rc.getStartLongitude(), rc.getStartLatLng(), rc.getEndLongitude(), rc.getEndLatitude(), rc.getRc(), null, null).toString());
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

	private HttpHost getProxy() {
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

	public static TranferDetaiFragment newInstance(SegParm segParm) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("segParm", segParm);
		TranferDetaiFragment fragment = new TranferDetaiFragment();
		fragment.setArguments(bundle);
		return fragment;
	}
}
