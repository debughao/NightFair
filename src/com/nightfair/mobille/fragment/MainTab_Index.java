package com.nightfair.mobille.fragment;

import com.nightfair.mobille.R;
import com.nightfair.mobille.activity.FoodsActivity;
import com.nightfair.mobille.activity.MoviesActivity;
import com.nightfair.mobille.activity.RecommendationActivity;
import com.nightfair.mobille.activity.TransitActivity;
import com.nightfair.mobille.util.ActivityUtils;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import krelve.view.Kanner;

@SuppressLint("ResourceAsColor")
public class MainTab_Index extends Fragment implements OnClickListener,
OnItemClickListener {

	private View indexView;

	private Kanner kanner;//自定义的轮播广告栏
	private FullyListView listView;
	private ScrollView scrollView;
	private TextView foodTextView;
	private TextView moviesTextView;
	private TextView transitTextView;
	private TextView recommendationTextView;
	private Intent intent;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (indexView == null) {
			indexView = inflater.inflate(R.layout.main_tab_index, container, false);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) indexView.getParent();
		if (parent != null) {
			parent.removeView(indexView);
		}
		ActivityUtils.setActionBarByColor(getActivity(),R.layout.title_bar_index, R.color.title_color);
		initView();
		initData();
		
		return indexView;
	}
	private void initView() {
		kanner = (Kanner) indexView.findViewById(R.id.kanner);
		listView = (FullyListView) indexView.findViewById(R.id.lv_index_item_detail);
		scrollView = (ScrollView) indexView.findViewById(R.id.sv_index_detail);
		foodTextView = (TextView) indexView.findViewById(R.id.tv_food);
		moviesTextView = (TextView) indexView.findViewById(R.id.tv_movies);
		transitTextView = (TextView) indexView.findViewById(R.id.tv_transit);
		recommendationTextView = (TextView) indexView.findViewById(R.id.tv_recommendation);
		foodTextView.setOnClickListener(this);
		moviesTextView.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		transitTextView.setOnClickListener(this);
		recommendationTextView.setOnClickListener(this);
		// 默认显示的首项是ListView，需要手动把ScrollView滚动至最顶端
		listView.setFocusable(false);
		scrollView.smoothScrollTo(0, 0);
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
			intent = new Intent(getActivity(), TransitActivity.class);
			intent.putExtra("text", "公交");
			startActivity(intent);
			break;
		case R.id.tv_recommendation:
			intent = new Intent(getActivity(), RecommendationActivity.class);
			intent.putExtra("text", "今日推荐");
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * listview单击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
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
		kanner.setImagesUrl(new String[] {
				"http://img04.muzhiwan.com/2015/06/16/upload_557fd293326f5.jpg",
				"http://img03.muzhiwan.com/2015/06/05/upload_557165f4850cf.png",
				"http://img02.muzhiwan.com/2015/06/11/upload_557903dc0f165.jpg",
				"http://img04.muzhiwan.com/2015/06/05/upload_5571659957d90.png",
				"http://img03.muzhiwan.com/2015/06/16/upload_557fd2a8da7a3.jpg" });
		// 从数据库下载数据
//		HttpUtils httpUtils = new HttpUtils();
//		httpUtils.send(HttpMethod.GET, "http://10.204.1.3:8080/NightFair/GetGoodsServlet", new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				LogUtils.e("网络异常");
//			}
//			@Override
//			public void onSuccess(ResponseInfo<String> arg0) {
//				String result = arg0.result;
//				Gson gson = new Gson();
//				Type typeOfT = new TypeToken<List<Goods>>() {
//				}.getType();
//				list = gson.fromJson(result, typeOfT);
//				adapter = new GoodsAdapter(list, getActivity());
//				listView.setAdapter(adapter);
//				adapter.notifyDataSetChanged();
//			}
//		});
	}
}
