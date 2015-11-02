package com.nightfair.mobille.activity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.ToastUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class BusActivity extends Activity implements OnClickListener, AMapLocationListener, OnGeocodeSearchListener {
	private LocationManagerProxy mLocationManagerProxy;
	private GeocodeSearch geocoderSearch;
	// private LocationClient mLocationClient;
	// private MyLocationListener mMyLocationListener;
	// private GeoCoder mSearch;
	private String currentLocation, mEndstation;
	private Context mContext;
	private TextView mTv_title, mTv_location;
	private ImageView iv_back;
	private ImageButton ib_refresh;
	private double mLatitude, mLongitude, mEndLatitude, mEndLongitude;
	private Animation operatingAnim;
	private PopupWindow popupWindow;
	private EditText et_startstation, et_endstation;
	private Button bt_cheack;
	private ProgressDialog progress;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus);
		ActivityUtils.setActionBarByColor(this, R.layout.title_bar, R.color.title_color);
		mContext = this;
		initView();
		// inintBaidu();
		inintAmap();
	}

	private void inintAmap() {
		// 初始化定位，只采用网络定位
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.setGpsEnable(true);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次,
		// 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
		mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 0, 0, this);
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);

	}

	private void initView() {
		// TODO Auto-generated method stub
		mTv_title = (TextView) findViewById(R.id.tv_actionbar_title);
		mTv_title.setText(R.string.actionbar_title_bus);
		mTv_location = (TextView) findViewById(R.id.tv_bus_location);
		ib_refresh = (ImageButton) findViewById(R.id.ib_location_refresh);
		iv_back = (ImageView) findViewById(R.id.iv_actionbar_back);
		et_startstation = (EditText) findViewById(R.id.et_startstation);
		et_endstation = (EditText) findViewById(R.id.et_endstation);
		bt_cheack = (Button) findViewById(R.id.bt_serach_check);
		mTv_location.setText("正在定位");
		mySetOnClickListener(iv_back, ib_refresh, et_startstation, bt_cheack);
		operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		ib_refresh.startAnimation(operatingAnim);
	}

	private void mySetOnClickListener(View... v) {
		for (View view : v) {
			view.setOnClickListener(this);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_actionbar_back:
			finish();
			break;
		case R.id.ib_location_refresh:
			inintAmap();
			currentLocation = "";
			// mLocationClient.start();
			if (operatingAnim != null) {
				ib_refresh.startAnimation(operatingAnim);
			}
			break;
		case R.id.et_startstation:
			showPopUp(et_startstation);
			break;
		case R.id.bt_serach_check:
			mEndstation = et_endstation.getText().toString();
			if (TextUtils.isEmpty(et_startstation.getText())) {
				ToastUtil.show(mContext, "起始站不能为空");
			} else if (TextUtils.isEmpty(mEndstation)) {
				ToastUtil.show(mContext, "终点站不能为空");
			} else {
				progress = new ProgressDialog(mContext);
				progress.setMessage("正在查询...");
				progress.setCanceledOnTouchOutside(false);
				progress.show();
				getLatlon(mEndstation);
			}
		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void showPopUp(View v) {
		LinearLayout layout = new LinearLayout(this);
		layout.setBackgroundColor(getResources().getColor(R.color.gray_bg));
		layout.setBackground(getResources().getDrawable(R.drawable.button_serach_bg_gray));
		TextView tv = new TextView(this);
		tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		tv.setText("当前位置");
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setTextColor(getResources().getColor(R.color.black_text));
		layout.addView(tv);
		popupWindow = new PopupWindow(layout, v.getWidth(), 120);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		int[] location = new int[2];
		v.getLocationInWindow(location);
		popupWindow.showAsDropDown(v);
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 关闭popupWindow
				popupWindow.dismiss();
				popupWindow = null;
				et_startstation.setText("当前位置");
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 移除定位请求
		mLocationManagerProxy.removeUpdates(BusActivity.this);
		// 销毁定位
		mLocationManagerProxy.destroy();
	}

	@Override
	protected void onStop() {
		// mLocationClient.stop();
		super.onStop();
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
	public void onLocationChanged(AMapLocation arg0) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		if (arg0 != null && arg0.getAMapException().getErrorCode() == 0) {
			// 定位成功回调信息，设置相关消息
			currentLocation = arg0.getCity();
			mLatitude = arg0.getLatitude();
			mLongitude = arg0.getLongitude();
			LogUtils.e("-----定位地址--------" + arg0.getAddress() + "-----------" + arg0.getLatitude() + "---------"
					+ arg0.getLongitude());
			mTv_location.setText(arg0.getAddress());
			mLocationManagerProxy.removeUpdates(BusActivity.this);
			mLocationManagerProxy.destroy();
			ib_refresh.clearAnimation();
		}
	}

	/**
	 * 响应地理编码
	 */
	public void getLatlon(final String name) {
		GeocodeQuery query = new GeocodeQuery(name, "苏州");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}

	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		// TODO Auto-generated method stub
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null && result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				mEndLatitude = address.getLatLonPoint().getLatitude();
				mEndLongitude = address.getLatLonPoint().getLongitude();
				LogUtils.e("查询坐标-----------" + mEndLatitude + mEndLongitude);
				progress.dismiss();
				Intent intent = new Intent(mContext, TranferActivity.class);
				intent.putExtra("city", currentLocation);
				intent.putExtra("mLongitude", mLongitude);
				intent.putExtra("mLatitude", mLatitude);
				intent.putExtra("mEndLongitude", mEndLongitude);
				intent.putExtra("mEndLatitude", mEndLatitude);
				startActivity(intent);
			} else {
				ToastUtil.show(mContext, "对不起，终点站不在查询范围！");
				progress.dismiss();
			}
		} else if (rCode == 27) {
			ToastUtil.show(mContext, "搜索失败,请检查网络连接！");
			progress.dismiss();
		} else {
			ToastUtil.show(mContext, "未知错误，请稍后重试!错误码为" + rCode);
			progress.dismiss();
		}
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}
}
// private void inintBaidu() {
// // TODO Auto-generated method stub
// mLocationClient = new LocationClient(this.getApplicationContext());
// mMyLocationListener = new MyLocationListener();
// mLocationClient.registerLocationListener(mMyLocationListener);
// initLocation();
// mSearch = GeoCoder.newInstance();
// mLocationClient.start();
// }

// public void initLocation() {
// // 设置定位参数
// LocationClientOption option = new LocationClientOption();
// option.setCoorType("bd09ll"); // 设置坐标类型
// option.setScanSpan(1000); // 10分钟扫描1次
// // 需要地址信息，设置为其他任何值（string类型，且不能为null）时，都表示无地址信息。
// option.setAddrType("all");
// // 设置是否返回POI的电话和地址等详细信息。默认值为false，即不返回POI的电话和地址信息。
// option.setLocationMode(LocationMode.Hight_Accuracy);
// option.setProdName("通过GPS定位我当前的位置");
// option.setIsNeedLocationDescribe(true);
// // 禁用启用缓存定位数据
// option.setTimeOut(6000);
// option.disableCache(true);
// //
// 当gps可用，而且获取了定位结果时，不再发起网络请求，直接返回给用户坐标。这个选项适合希望得到准确坐标位置的用户。如果gps不可用，再发起网络请求，进行定位。
// option.setPriority(LocationClientOption.GpsFirst);
// mLocationClient.setLocOption(option);
// }
// /**
// * 实现实位回调监听
// */
// public class MyLocationListener implements BDLocationListener {
// @Override
// public void onReceiveLocation(BDLocation arg0) {
// mLongitude = arg0.getLongitude();
// mLatitude = arg0.getLatitude();
// LogUtils.e(arg0.getCity()+"----------"+mLongitude+"----------"+mLatitude);
// LatLng ptCenter = new LatLng(arg0.getLatitude(), arg0.getLongitude());
// mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
// mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
//
// @Override
// public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
// // TODO Auto-generated method stub
// currentLocation = result.getAddress();
// mTv_location.setText(currentLocation);
// ib_refresh.setVisibility(View.VISIBLE);
// if (!TextUtils.isEmpty(currentLocation)) {
// mLocationClient.stop();
// ib_refresh.clearAnimation();
// }
// }
// @Override
// public void onGetGeoCodeResult(GeoCodeResult arg0) {
// // TODO Auto-generated method stub
// }
// });
//
// }
//
// }
