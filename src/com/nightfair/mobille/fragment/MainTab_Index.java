package com.nightfair.mobille.fragment;

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
import com.nightfair.mobille.activity.BusActivity;
import com.nightfair.mobille.activity.FoodsActivity;
import com.nightfair.mobille.activity.LocationIndexActivity;
import com.nightfair.mobille.activity.MessageActivity;
import com.nightfair.mobille.activity.MoviesActivity;
import com.nightfair.mobille.activity.RecommendationActivity;
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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import krelve.view.Kanner;

@SuppressLint("ResourceAsColor")
public class MainTab_Index extends Fragment implements OnClickListener, OnItemClickListener {

	private View indexView;

	private Kanner kanner;// 自定义的轮播广告栏
	private FullyListView listView;
	private ScrollView scrollView;
	private TextView recommendationTextView, tv_loaction, transitTextView, moviesTextView, foodTextView;
	private Intent intent;
	private RelativeLayout rl_loaction;
	private String cityName;
	private ImageButton iv_message;
	private ArrayList<SellerAndCoupon> list = new ArrayList<SellerAndCoupon>();
	private GuessCouponAdapter adapter;

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
		initView();	
		initData();	
		adapter = new GuessCouponAdapter(list, getActivity());
		listView.setAdapter(adapter);
		return indexView;
	}

	private void initView() {
		kanner = (Kanner) indexView.findViewById(R.id.kanner);
		listView = (FullyListView) indexView.findViewById(R.id.lv_index_item_detail);
		scrollView = (ScrollView) indexView.findViewById(R.id.sv_index_detail);
		foodTextView = (TextView) indexView.findViewById(R.id.tv_food);
		moviesTextView = (TextView) indexView.findViewById(R.id.tv_movies);
		transitTextView = (TextView) indexView.findViewById(R.id.tv_transit);
		tv_loaction = (TextView) indexView.findViewById(R.id.tv_title_bar_loaction);
		recommendationTextView = (TextView) indexView.findViewById(R.id.tv_recommendation);
		rl_loaction = (RelativeLayout) indexView.findViewById(R.id.rl_title_bar_loaction);
		iv_message = (ImageButton) indexView.findViewById(R.id.iv_index_title_bar_message);
		mySetOnClickListener(rl_loaction, foodTextView, moviesTextView, transitTextView, recommendationTextView,
				iv_message);
		listView.setOnItemClickListener(this);
		// 默认显示的首项是ListView，需要手动把ScrollView滚动至最顶端
		listView.setFocusable(false);
		scrollView.smoothScrollTo(0, 0);	
	}

	private void mySetOnClickListener(View... v) {
		for (View view : v) {
			view.setOnClickListener(this);
		}

	}

	/**
	 * 导航单击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_food:
			intent = new Intent(getActivity(), FoodsActivity.class);
			intent.putExtra("text", "美食");
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
			Intent intent = new Intent(getActivity(), LocationIndexActivity.class);
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
	 * listview单击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			Toast.makeText(getActivity(), "1", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
	}

	/**
	 * 广告栏和ListView数据来源
	 */
	private void initData() {
		kanner.setImagesUrl(new String[] { "http://img04.muzhiwan.com/2015/06/16/upload_557fd293326f5.jpg",
				"http://img03.muzhiwan.com/2015/06/05/upload_557165f4850cf.png",
				"http://img02.muzhiwan.com/2015/06/11/upload_557903dc0f165.jpg",
				"http://img04.muzhiwan.com/2015/06/05/upload_5571659957d90.png",
				"http://img03.muzhiwan.com/2015/06/16/upload_557fd2a8da7a3.jpg" });
		// 从数据库下载数据
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("action", "getGuessCoupon");
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, AppConstants.GETGUESSCOUPON, params, new RequestCallBack<String>() {
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
					String results = jsonObject.optString("result");
					if (state != 200) {
						ToastUtil.show(getActivity(), results);
					} else {
						String data=jsonObject.optString("data");
						Gson gson = new Gson();
						Type typeOfT = new TypeToken<ArrayList<SellerAndCoupon>>() {
						}.getType();
						LogUtils.e("sdsds---------"+data);
						ArrayList<SellerAndCoupon> sellerAndCoupons = gson.fromJson(data, typeOfT);
					
						list.addAll(sellerAndCoupons);
						adapter.notifyDataSetChanged();
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
}
