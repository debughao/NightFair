package com.nightfair.mobille.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
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
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.adapter.LeftAdapter;
import com.nightfair.mobille.adapter.RecommandCouponAdapter;
import com.nightfair.mobille.adapter.RightAdapter;
import com.nightfair.mobille.adapter.RmAroundAdapter;
import com.nightfair.mobille.adapter.RmSortAdapter;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.bean.LeftItem;
import com.nightfair.mobille.bean.RightItem;
import com.nightfair.mobille.bean.SellerAndCoupon;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.ToastUtil;

import android.annotation.SuppressLint;
import android.graphics.drawable.PaintDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.RelativeLayout;

@SuppressLint("InflateParams")
public class RecommendationActivity extends BaseActivity
		implements OnClickListener, AMapLocationListener, OnRefreshListener<ListView> {

	PopupWindow popupWindow;
	View popupView, popupView_around, popupView_sort;
	List<LeftItem> leftList;
	List<RightItem> rightList;
	ListView leftListView, rightListView, aroundListView, sortListView;
	TextView newTextView, textView, aroundTextView, sorTextView;
	RmAroundAdapter rmAroundAdapter;
	RmSortAdapter rmSortAdapter;
	private TextView mTv_title;
	private ImageView iv_back;
	RecommandCouponAdapter adapter;
	ArrayList<SellerAndCoupon> list = new ArrayList<SellerAndCoupon>();
	// ListView listView;
	PullToRefreshListView listView;
	private String distance[] = new String[] { "全城", "1km", "3km", "5km" };
	private String sort[] = new String[] { "智能排序", "离我最近", "好评优先", "价格最低" };
	/**
	 * 今日新单 、附近 、智能排序
	 */
	private RelativeLayout recommendationLayout;
	private RelativeLayout aroundLayout;
	private RelativeLayout sortLayout;
	// 高德地图
	private LocationManagerProxy mLocationManagerProxy;	
	public static double geoLat, geoLng;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index_recommendation);
		ActivityUtils.setActionBarByColor(this, R.layout.title_bar, R.color.title_color);
		initview();
		initData();
		initLocation();
		adapter = new RecommandCouponAdapter(list, RecommendationActivity.this);
		listView.setAdapter(adapter);
	}

	private void initview() {
		mTv_title = (TextView) findViewById(R.id.tv_actionbar_title);
		mTv_title.setText(R.string.recommendation);
		iv_back = (ImageView) findViewById(R.id.iv_actionbar_back);
		listView = (PullToRefreshListView) findViewById(R.id.lv_recommendation);
		recommendationLayout = (RelativeLayout) findViewById(R.id.rl_recommendation_details);
		aroundLayout = (RelativeLayout) findViewById(R.id.rl_around_details);
		sortLayout = (RelativeLayout) findViewById(R.id.rl_sort_details);
		recommendationLayout.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		aroundLayout.setOnClickListener(this);
		sortLayout.setOnClickListener(this);
		listView.setOnRefreshListener(this);
		newTextView = (TextView) findViewById(R.id.tv_rm);
		aroundTextView = (TextView) findViewById(R.id.tv_around);
		sorTextView = (TextView) findViewById(R.id.tv_sort);
	}

	private void initLocation() {
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
		mLocationManagerProxy.setGpsEnable(false);
	}

	@SuppressLint("InflateParams")
	private void initPopupWindow(int resource) {
		popupView = LayoutInflater.from(RecommendationActivity.this).inflate(resource, null);
		popupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setBackgroundDrawable(new PaintDrawable());
		popupWindow.setFocusable(true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				setParentViewAlpha(1.0f);
			}
		});
		this.setParentViewAlpha(0.5f);
	}

	/**
	 * 设置父窗口的透明度
	 * 
	 * @param alpha
	 *            透明度
	 */
	private void setParentViewAlpha(float alpha) {
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.alpha = alpha;
		getWindow().setAttributes(params);
	}

	private void initData() {
		leftList = new ArrayList<LeftItem>();
		ArrayList<RightItem> r1 = new ArrayList<RightItem>();
		r1.add(new RightItem("全部美食"));
		r1.add(new RightItem("中餐"));
		r1.add(new RightItem("西餐"));
		leftList.add(new LeftItem(R.drawable.ic_recommendation_food, "全部美食", r1));

		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("action", "getRecommandCoupon");
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, AppConstants.GETCOUPON, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LogUtils.e("网络异常");
				listView.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					int state = jsonObject.optInt("state");
					String results = jsonObject.optString("result");
					if (state == 200) {				
						String data = jsonObject.optString("data");
						Gson gson = new Gson();
						Type typeOfT = new TypeToken<ArrayList<SellerAndCoupon>>() {
						}.getType();
						LogUtils.e("sdsds---------" + data);
						ArrayList<SellerAndCoupon> sellerAndCoupons = gson.fromJson(data, typeOfT);
						listView.onRefreshComplete();
						list.clear();
						list.addAll(sellerAndCoupons);
						adapter.notifyDataSetChanged();
					}else {
						ToastUtil.show(RecommendationActivity.this, results);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_recommendation_details:
			initPopupWindow(R.layout.popupwindow);
			showOrClose();
			init(v);
			break;
		case R.id.rl_around_details:
			initPopupWindow(R.layout.popupwindow_around);
			showOrClose();
			init(v);
			break;
		case R.id.rl_sort_details:
			initPopupWindow(R.layout.popupwindow_sort);
			showOrClose();
			init(v);
			break;
		default:
			break;
		}
	}

	private void showOrClose() {
		// 设定popupWindow的显示与隐藏，以及显示的位置
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
			this.setParentViewAlpha(1.0f);
		} else {
			popupWindow.showAsDropDown(recommendationLayout);
		}
	}

	public void init(View view) {
		if (view.getId() == R.id.rl_recommendation_details) {
			leftListView = (ListView) popupView.findViewById(R.id.lv_popupwindow_left);
			rightListView = (ListView) popupView.findViewById(R.id.lv_popupwindow_right);
			textView = (TextView) popupView.findViewById(R.id.tv_recommendation_right_details);
			final LeftAdapter leftAdapter = new LeftAdapter(RecommendationActivity.this, leftList);
			leftListView.setAdapter(leftAdapter);
			rightList = new ArrayList<RightItem>();
			rightList.addAll(leftList.get(0).getList());
			final RightAdapter secondAdapter = new RightAdapter(this, rightList);
			rightListView.setAdapter(secondAdapter);
			leftListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					List<RightItem> r3 = leftList.get(position).getList();
					if (r3 == null || r3.size() == 0) {
						String text = leftList.get(position).getText();
						newTextView.setText(text);
						popupWindow.dismiss();
						return;
					}
					LeftAdapter adapter = (LeftAdapter) (parent.getAdapter());
					// 如果上次点击的就是这一个item，则不进行任何操作
					if (adapter.getSelectedPosition() == position) {
						return;
					}
					// 根据左侧一级分类选中情况，更新背景色
					adapter.setSelectedPosition(position);
					adapter.notifyDataSetChanged();
					// 显示右侧二级分类
					updateSecondListView(r3, secondAdapter);
				}
			});
			rightListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// 关闭popupWindow，显示用户选择的分类
					popupWindow.dismiss();
					int leftPosition = leftAdapter.getSelectedPosition();
					String name = leftList.get(leftPosition).getList().get(position).getText();
					newTextView.setText(name);
				}
			});
		} else if (view.getId() == R.id.rl_around_details) {
			aroundListView = (ListView) popupView.findViewById(R.id.listview_around);
			rmAroundAdapter = new RmAroundAdapter(getApplicationContext(), distance);
			rmAroundAdapter.setSelectedPosition(0);
			aroundListView.setAdapter(rmAroundAdapter);
			aroundListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					popupWindow.dismiss();
					aroundTextView.setText(distance[position]);
				}
			});
		} else if (view.getId() == R.id.rl_sort_details) {
			sortListView = (ListView) popupView.findViewById(R.id.listview_sort);
			rmSortAdapter = new RmSortAdapter(getApplicationContext(), sort);
			rmSortAdapter.setSelectedPosition(0);
			sortListView.setAdapter(rmSortAdapter);
			sortListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					popupWindow.dismiss();
					sorTextView.setText(sort[position]);
				}
			});
		}
	}

	private void updateSecondListView(List<RightItem> r3, RightAdapter secondAdapter) {
		rightList.clear();
		rightList.addAll(r3);
		secondAdapter.notifyDataSetChanged();
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 移除定位请求
		mLocationManagerProxy.removeUpdates(this);
		// 销毁定位
		mLocationManagerProxy.destroy();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		initData();
	}

}
