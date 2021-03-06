package com.nightfair.mobille.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.activity.BusActivity;
import com.nightfair.mobille.activity.LocationIndexActivity;
import com.nightfair.mobille.activity.MainActivity;
import com.nightfair.mobille.activity.MessageActivity;
import com.nightfair.mobille.activity.MoviesActivity;
import com.nightfair.mobille.activity.RecommendationActivity;
import com.nightfair.mobille.activity.SerachActivity;
import com.nightfair.mobille.adapter.GuessCouponAdapter;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.bean.SellerAndCoupon;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.util.FragmentUtils;
import com.nightfair.mobille.util.SharePreferenceUtil;
import com.nightfair.mobille.util.ToastUtil;
import com.nightfair.mobille.view.FullyListView;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import krelve.view.Kanner;

@SuppressLint("ResourceAsColor")
public class MainTab_Index extends Fragment
		implements OnClickListener, AMapLocationListener, OnRefreshListener<ScrollView> {

	private View indexView;

	private Kanner kanner;// 自定义的轮播广告栏
	private FullyListView listView;
	private TextView recommendationTextView, tv_loaction, transitTextView, moviesTextView, foodTextView;
	private Intent intent;
	private RelativeLayout rl_loaction, rl_search;
	private String cityName;
	private ImageButton iv_message;
	private ArrayList<SellerAndCoupon> list = new ArrayList<SellerAndCoupon>();
	private GuessCouponAdapter adapter;
	private PullToRefreshScrollView mPullRefreshScrollView;
	public SharePreferenceUtil mSharedUtil;
	public BaseApplication mApplication;
	private LocationManagerProxy mLocationManagerProxy;
	public static double geoLat, geoLng;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (indexView == null) {
			indexView = inflater.inflate(R.layout.main_tab_index, container, false);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) indexView.getParent();
		if (parent != null) {
			parent.removeView(indexView);
		}
		mApplication = BaseApplication.getInstance();
		mSharedUtil = mApplication.getSpUtil();
		initView();
		initData();
		initLocation();
		adapter = new GuessCouponAdapter(list, getActivity());
		listView.setAdapter(adapter);
		return indexView;
	}

	private void initView() {
		kanner = (Kanner) indexView.findViewById(R.id.kanner);
		listView = (FullyListView) indexView.findViewById(R.id.lv_index_item_detail);
		foodTextView = (TextView) indexView.findViewById(R.id.tv_food);
		moviesTextView = (TextView) indexView.findViewById(R.id.tv_movies);
		transitTextView = (TextView) indexView.findViewById(R.id.tv_transit);
		tv_loaction = (TextView) indexView.findViewById(R.id.tv_title_bar_loaction);
		recommendationTextView = (TextView) indexView.findViewById(R.id.tv_recommendation);
		rl_loaction = (RelativeLayout) indexView.findViewById(R.id.rl_title_bar_loaction);
		rl_search = (RelativeLayout) indexView.findViewById(R.id.rl_title_bar_search);
		iv_message = (ImageButton) indexView.findViewById(R.id.iv_index_title_bar_message);
		mPullRefreshScrollView = (PullToRefreshScrollView) indexView.findViewById(R.id.sv_index_detail);
		mPullRefreshScrollView.setOnRefreshListener(this);
		// mPullRefreshScrollView.setFocusable(false);
		mySetOnClickListener(rl_loaction, rl_search, foodTextView, moviesTextView, transitTextView,
				recommendationTextView, iv_message);
		// 默认显示的首项是ListView，需要手动把ScrollView滚动至最顶端
		listView.setFocusable(true);
		// listView.setParentScrollView(mPullRefreshScrollView);

	}

	private void mySetOnClickListener(View... v) {
		for (View view : v) {
			view.setOnClickListener(this);
		}

	}

	private void initLocation() {
		mLocationManagerProxy = LocationManagerProxy.getInstance(getActivity());
		mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
		mLocationManagerProxy.setGpsEnable(false);
	}

	/**
	 * 导航单击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_food:
			mSharedUtil.setAllowBackSecond(true);
			intent = new Intent(getActivity(), MainActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_movies:
			intent = new Intent(getActivity(), MoviesActivity.class);
			intent.putExtra("url", "http://m.mtime.cn");
			startActivity(intent);
			break;
		case R.id.tv_transit:
			intent = new Intent(getActivity(), BusActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_recommendation:
			intent = new Intent(getActivity(), RecommendationActivity.class);
			intent.putExtra("text", "今日推荐");
			startActivity(intent);
			break;
		case R.id.rl_title_bar_loaction:
			intent = new Intent(getActivity(), LocationIndexActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_title_bar_search:
			intent = new Intent(getActivity(), SerachActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_index_title_bar_message:
			FragmentUtils.startActivity(this, MessageActivity.class);
			break;
		default:
			break;
		}
	}

	/**
	 * 广告栏和ListView数据来源
	 */
	private void initData() {
		kanner.setImagesUrl(new String[] { "http://115.28.70.177/image/i1.jpg", "http://115.28.70.177/image/i3.jpg",
				"http://115.28.70.177/image/i4.jpg", "http://115.28.70.177/image/i5.jpg",
				"http://115.28.70.177/image/i7.jpg" });
		// 从数据库下载数据
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("action", "getGuessCoupon");
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, AppConstants.GETCOUPON, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LogUtils.e("网络异常");
				mPullRefreshScrollView.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					int state = jsonObject.optInt("state");
					String results = jsonObject.optString("result");
					if (state != 200) {
						mPullRefreshScrollView.onRefreshComplete();
						ToastUtil.show(getActivity(), results);
					} else {
						String data = jsonObject.optString("data");
						Gson gson = new Gson();
						Type typeOfT = new TypeToken<ArrayList<SellerAndCoupon>>() {
						}.getType();
						LogUtils.e("sdsds---------" + data);
						ArrayList<SellerAndCoupon> sellerAndCoupons = gson.fromJson(data, typeOfT);
						list.clear();
						list.addAll(sellerAndCoupons);
						adapter.notifyDataSetChanged();
						mPullRefreshScrollView.onRefreshComplete();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		BaseApplication mApplication = BaseApplication.getInstance();
		SharePreferenceUtil mSharedUtil = mApplication.getSpUtil();
		cityName = mSharedUtil.getLOCATION_CITYNAME();
		tv_loaction.setText(cityName);
	}

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		initData();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if (amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0) {
			geoLat = amapLocation.getLatitude();
			geoLng = amapLocation.getLongitude();
			Log.e("经度---" + amapLocation.getLatitude(), "纬度---" + amapLocation.getLongitude());
			if (geoLat != 0 || geoLng != 0) {
				adapter.notifyDataSetChanged();
			}
		} else {
			Log.e("AmapErr", "Location ERR:" + amapLocation.getAMapException().getErrorCode());
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 移除定位请求
		mLocationManagerProxy.removeUpdates(this);
		// 销毁定位
		mLocationManagerProxy.destroy();
	}

}
