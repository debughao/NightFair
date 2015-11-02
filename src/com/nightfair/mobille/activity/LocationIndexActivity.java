package com.nightfair.mobille.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.adapter.HitCityAdapter;
import com.nightfair.mobille.adapter.HotCityAdapter;
import com.nightfair.mobille.adapter.ResultListAdapter;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.bean.City;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.DBHelper;
import com.nightfair.mobille.util.DatabaseHelper;
import com.nightfair.mobille.util.PingYinUtil;
import com.nightfair.mobille.util.SharePreferenceUtil;
import com.nightfair.mobille.widget.MyLetterListView;
import com.nightfair.mobille.widget.MyLetterListView.OnTouchingLetterChangedListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class LocationIndexActivity extends BaseActivity implements OnScrollListener {
	private BaseAdapter adapter;
	private ResultListAdapter resultListAdapter;
	private ListView personList;
	private ListView resultList;
	private MyLetterListView letterListView; // A-Z listview
	public SharePreferenceUtil mSharedUtil;
	public BaseApplication mApplication;
	private TextView overlay; // 对话框首字母textview
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	private String[] sections;// 存放存在的汉语拼音首字母
	private Handler handler;
	private OverlayThread overlayThread; // 显示首字母对话框

	private ArrayList<City> allCity_lists; // 所有城市列表
	private ArrayList<City> city_lists;// 城市列表
	private ArrayList<City> city_hot;
	private ArrayList<City> city_result;// 查询结果
	private ArrayList<String> city_history;
	private EditText searchCity;
	private TextView tv_noresult;

	private LocationClient mLocationClient;
	private MyLocationListener mMyLocationListener;

	private String currentCity; // 用于保存定位到的城市
	private int locateProcess = 1; // 记录当前定位的状态 正在定位-定位成功-定位失败
	private boolean isNeedFresh;

	private DatabaseHelper helper;

	@SuppressLint("ResourceAsColor")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.title_bar_location);
		ActivityUtils.setActionBarByColor(LocationIndexActivity.this, R.layout.title_bar, R.color.title_color);
		TextView mTv_title = (TextView) findViewById(R.id.tv_actionbar_title);
		ImageView iv_back = (ImageView) findViewById(R.id.iv_actionbar_back);
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mTv_title.setText(R.string.actionbar_title_location);
		mApplication = BaseApplication.getInstance();
		mSharedUtil = mApplication.getSpUtil();
		personList = (ListView) findViewById(R.id.list_view);
		allCity_lists = new ArrayList<City>();
		city_hot = new ArrayList<City>();
		city_result = new ArrayList<City>();
		city_history = new ArrayList<String>();
		resultList = (ListView) findViewById(R.id.search_result);
		searchCity = (EditText) findViewById(R.id.et_search_city);
		tv_noresult = (TextView) findViewById(R.id.tv_noresult);
		helper = new DatabaseHelper(this);
		searchCity.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.toString() == null || "".equals(s.toString())) {
					letterListView.setVisibility(View.VISIBLE);
					personList.setVisibility(View.VISIBLE);
					resultList.setVisibility(View.GONE);
					tv_noresult.setVisibility(View.GONE);
				} else {
					city_result.clear();
					letterListView.setVisibility(View.GONE);
					personList.setVisibility(View.GONE);
					getResultCityList(s.toString());// 在数据库进行查询
					if (city_result.size() <= 0) {
						tv_noresult.setVisibility(View.VISIBLE);
						resultList.setVisibility(View.GONE);
					} else {
						tv_noresult.setVisibility(View.GONE);
						resultList.setVisibility(View.VISIBLE);
						resultListAdapter = new ResultListAdapter(LocationIndexActivity.this, city_result);
						resultList.setAdapter(resultListAdapter);
						resultListAdapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		// alphaIndexer = new HashMap<String, Integer>();
		handler = new Handler();
		overlayThread = new OverlayThread();
		isNeedFresh = true;
		locateProcess = 1;
		personList.setAdapter(adapter);
		personList.setOnScrollListener(this);

		/**
		 * 右侧快速导航栏的单击事件
		 */
		letterListView = (MyLetterListView) findViewById(R.id.MyLetterListView01);
		letterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());

		/**
		 * 查询城市的结果的单击事件
		 */
		resultList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getApplicationContext(), city_result.get(position).getName(), Toast.LENGTH_SHORT).show();
				String cityName = city_result.get(position).getName();
				InsertCity(cityName);// 选中点击的城市，插入数据库
				callbackIndex(cityName);
				
				finish();
			}
		});

		/**
		 * ListView的item单击事件
		 */
		personList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position >= 4) {
					String cityName = allCity_lists.get(position).getName();
					InsertCity(cityName);// 选中点击的城市，插入数据库
					callbackIndex(cityName);
				}
			}
		});
		initOverlay();
		cityInit();
		hotCityInit();
		hisCityInit();
		setAdapter(allCity_lists, city_hot, city_history);
		// 定位
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		initLocation();
		mLocationClient.start();
	}

	/**
	 * 把最近访问过的城市的名字和访问时的时间插入数据库
	 */
	public void InsertCity(String name) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from recentcity where name = '" + name + "'", null);
		if (cursor.getCount() > 0) { //
			db.delete("recentcity", "name = ?", new String[] { name });
		}
		db.execSQL("insert into recentcity(name, date) values('" + name + "', " + System.currentTimeMillis() + ")");
		db.close();
	}

	public void initLocation() {
		// 设置定位参数
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(10000); // 10分钟扫描1次
		// 需要地址信息，设置为其他任何值（string类型，且不能为null）时，都表示无地址信息。
		option.setAddrType("all");
		// 设置是否返回POI的电话和地址等详细信息。默认值为false，即不返回POI的电话和地址信息。
		// option.setPoiExtraInfo(true);
		// 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
		option.setProdName("通过GPS定位我当前的位置");
		// 禁用启用缓存定位数据
		option.disableCache(true);
		// 设置最多可返回的POI个数，默认值为3。由于POI查询比较耗费流量，设置最多返回的POI个数，以便节省流量。
		// option.setPoiNumber(3);
		// 设置定位方式的优先级。
		// 当gps可用，而且获取了定位结果时，不再发起网络请求，直接返回给用户坐标。这个选项适合希望得到准确坐标位置的用户。如果gps不可用，再发起网络请求，进行定位。
		option.setPriority(LocationClientOption.GpsFirst);
		mLocationClient.setLocOption(option);
	}

	private void cityInit() {
		City city = new City("定位", "0"); // 当前定位城市
		allCity_lists.add(city);
		city = new City("最近", "1"); // 最近访问的城市
		allCity_lists.add(city);
		city = new City("热门", "2"); // 热门城市
		allCity_lists.add(city);
		city = new City("全部", "3"); // 全部城市
		allCity_lists.add(city);
		city_lists = getCityList();
		allCity_lists.addAll(city_lists);// 按字母列表分类的所有城市
	}

	/**
	 * 热门城市
	 */
	public void hotCityInit() {
		City city = new City("上海", "2");
		city_hot.add(city);
		city = new City("北京", "2");
		city_hot.add(city);
		city = new City("广州", "2");
		city_hot.add(city);
		city = new City("深圳", "2");
		city_hot.add(city);
		city = new City("武汉", "2");
		city_hot.add(city);
		city = new City("天津", "2");
		city_hot.add(city);
		city = new City("西安", "2");
		city_hot.add(city);
		city = new City("南京", "2");
		city_hot.add(city);
		city = new City("杭州", "2");
		city_hot.add(city);
		city = new City("成都", "2");
		city_hot.add(city);
		city = new City("重庆", "2");
		city_hot.add(city);
	}

	/**
	 * 最近访问过的城市
	 */
	private void hisCityInit() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from recentcity order by date desc limit 0, 3", null);
		while (cursor.moveToNext()) {
			city_history.add(cursor.getString(1));
		}
		cursor.close();
		db.close();
	}

	@SuppressWarnings("unchecked")
	private ArrayList<City> getCityList() {
		DBHelper dbHelper = new DBHelper(this);
		ArrayList<City> list = new ArrayList<City>();
		try {
			dbHelper.createDataBase();
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery("select * from city", null);
			City city;
			while (cursor.moveToNext()) {
				city = new City(cursor.getString(1), cursor.getString(2));
				list.add(city);
			}
			cursor.close();
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.sort(list, comparator);
		return list;
	}

	/**
	 * 获取搜索的结果，并按首字母a-z排序
	 */
	@SuppressWarnings("unchecked")
	private void getResultCityList(String keyword) {
		DBHelper dbHelper = new DBHelper(this);
		try {
			dbHelper.createDataBase();
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery(
					"select * from city where name like \"%" + keyword + "%\" or pinyin like \"%" + keyword + "%\"",
					null);
			City city;
			Log.e("info", "length = " + cursor.getCount());
			while (cursor.moveToNext()) {
				city = new City(cursor.getString(1), cursor.getString(2));
				city_result.add(city);
			}
			cursor.close();
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.sort(city_result, comparator);
	}

	/**
	 * a-z排序
	 */
	@SuppressWarnings("rawtypes")
	Comparator comparator = new Comparator<City>() {
		@Override
		public int compare(City lhs, City rhs) {
			String a = lhs.getPinyi().substring(0, 1);
			String b = rhs.getPinyi().substring(0, 1);
			int flag = a.compareTo(b);
			if (flag == 0) {
				return a.compareTo(b);
			} else {
				return flag;
			}
		}
	};

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			Log.e("info", "city = " + arg0.getCity());
			if (!isNeedFresh) {
				return;
			}
			isNeedFresh = false;
			if (arg0.getCity() == null) {
				locateProcess = 3; // 定位失败
				personList.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				return;
			}
			LogUtils.e("经度"+arg0.getLongitude());
			LogUtils.e("纬度"+arg0.getLatitude());
			currentCity = arg0.getCity().substring(0, arg0.getCity().length() - 1);
			locateProcess = 2; // 定位成功
			personList.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}

	}

	private void setAdapter(List<City> list, List<City> hotList, List<String> hisCity) {
		adapter = new ListAdapter(this, list, hotList, hisCity);
		personList.setAdapter(adapter);
	}

	public class ListAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private List<City> list;
		private List<City> hotList;
		private List<String> hisCity;
		final int VIEW_TYPE = 5;

		public ListAdapter(Context context, List<City> list, List<City> hotList, List<String> hisCity) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
			this.context = context;
			this.hotList = hotList;
			this.hisCity = hisCity;
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				// 当前汉语拼音首字母
				String currentStr = getAlpha(list.get(i).getPinyi());
				// 上一个汉语拼音首字母，如果不存在为" "
				String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1).getPinyi()) : " ";
				if (!previewStr.equals(currentStr)) {
					String name = getAlpha(list.get(i).getPinyi());
					alphaIndexer.put(name, i);
					sections[i] = name;
				}
			}
		}

		@Override
		public int getViewTypeCount() {
			return VIEW_TYPE;
		}

		@Override
		public int getItemViewType(int position) {
			return position < 4 ? position : 4;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		ViewHolder holder;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final TextView city;
			int viewType = getItemViewType(position);
			if (viewType == 0) { // 定位
				convertView = inflater.inflate(R.layout.first_list_item, null);
				TextView locateHint = (TextView) convertView.findViewById(R.id.locateHint);
				city = (TextView) convertView.findViewById(R.id.lng_city);
				city.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (locateProcess == 2) {
							String cityName = city.getText().toString();
							InsertCity(cityName);
							callbackIndex(cityName);
						} else if (locateProcess == 3) {
							locateProcess = 1;
							personList.setAdapter(adapter);
							adapter.notifyDataSetChanged();
							mLocationClient.stop();
							isNeedFresh = true;
							initLocation();
							currentCity = "";
							mLocationClient.start();
						}
					}
				});
				ProgressBar pbLocate = (ProgressBar) convertView.findViewById(R.id.pbLocate);
				if (locateProcess == 1) { // 正在定位
					locateHint.setText("正在定位");
					city.setVisibility(View.GONE);
					pbLocate.setVisibility(View.VISIBLE);
				} else if (locateProcess == 2) { // 定位成功
					locateHint.setText("当前定位城市");
					city.setVisibility(View.VISIBLE);
					city.setText(currentCity);
					mLocationClient.stop();
					pbLocate.setVisibility(View.GONE);
				} else if (locateProcess == 3) {
					locateHint.setText("未定位到城市,请选择");
					city.setVisibility(View.VISIBLE);
					city.setText("重新选择");
					pbLocate.setVisibility(View.GONE);
				}
			} else if (viewType == 1) { // 最近访问城市
				convertView = inflater.inflate(R.layout.recent_city, null);
				GridView rencentCity = (GridView) convertView.findViewById(R.id.recent_city);
				rencentCity.setAdapter(new HitCityAdapter(context, this.hisCity));
				rencentCity.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						String cityName = city_history.get(position);
						InsertCity(cityName);
						callbackIndex(cityName);
					}
				});
				TextView recentHint = (TextView) convertView.findViewById(R.id.recentHint);
				recentHint.setText("最近访问的城市");
			} else if (viewType == 2) {
				convertView = inflater.inflate(R.layout.recent_city, null);
				GridView hotCity = (GridView) convertView.findViewById(R.id.recent_city);
				hotCity.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						String cityName = city_hot.get(position).getName();
						InsertCity(cityName);
						callbackIndex(cityName);
					}
				});
				hotCity.setAdapter(new HotCityAdapter(context, this.hotList));
				TextView hotHint = (TextView) convertView.findViewById(R.id.recentHint);
				hotHint.setText("热门城市");
			} else if (viewType == 3) {
				convertView = inflater.inflate(R.layout.total_item, null);
			} else {
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.list_item, null);
					holder = new ViewHolder();
					holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
					holder.name = (TextView) convertView.findViewById(R.id.name);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				if (position >= 1) {
					holder.name.setText(list.get(position).getName());
					String currentStr = getAlpha(list.get(position).getPinyi());
					String previewStr = (position - 1) >= 0 ? getAlpha(list.get(position - 1).getPinyi()) : " ";
					if (!previewStr.equals(currentStr)) {
						holder.alpha.setVisibility(View.VISIBLE);
						holder.alpha.setText(currentStr);
					} else {
						holder.alpha.setVisibility(View.GONE);
					}
				}
			}
			return convertView;
		}

		private class ViewHolder {
			TextView alpha; // 首字母标题
			TextView name; // 城市名字
		}
	}

	@Override
	protected void onStop() {
		mLocationClient.stop();
		super.onStop();
	}

	private boolean mReady;

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		mReady = true;
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	private boolean isScroll = false;

	private class LetterListViewListener implements OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			isScroll = false;
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				personList.setSelection(position);
				overlay.setText(s);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// 延迟一秒后执行，让overlay为不可见
				handler.postDelayed(overlayThread, 1000);
			}
		}
	}

	// 设置overlay不可见
	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}
	}

	// 获得汉语拼音首字母
	@SuppressLint("DefaultLocale")
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else if (str.equals("0")) {
			return "定位";
		} else if (str.equals("1")) {
			return "最近";
		} else if (str.equals("2")) {
			return "热门";
		} else if (str.equals("3")) {
			return "全部";
		} else {
			return "#";
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING) {
			isScroll = true;
		}
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (!isScroll) {
			return;
		}

		if (mReady) {
			String text;
			String name = allCity_lists.get(firstVisibleItem).getName();
			String pinyin = allCity_lists.get(firstVisibleItem).getPinyi();
			if (firstVisibleItem < 4) {
				text = name;
			} else {
				text = PingYinUtil.converterToFirstSpell(pinyin).substring(0, 1).toUpperCase();
			}
			overlay.setText(text);
			overlay.setVisibility(View.VISIBLE);
			handler.removeCallbacks(overlayThread);
			// 延迟一秒后执行，让overlay为不可见
			handler.postDelayed(overlayThread, 1000);
		}
	}

	public void callbackIndex(String cityName) {
		mSharedUtil.setLOCATION_CITYNAME(cityName);
		Intent intent = new Intent();
		intent.setClass(LocationIndexActivity.this, MainActivity.class);
		intent.putExtra("cityName", cityName);
		startActivity(intent);
		finish();
	}
}