package com.nightfair.mobille.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.bean.SegParm;
import com.nightfair.mobille.fragment.TranferDetaiFragment;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.ToastUtil;
import com.nightfair.mobille.view.ViewPagerIndicator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class TranferActivity extends FragmentActivity implements OnClickListener {
	private List<Fragment> mTabContents = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	private ViewPager mViewPager;
	private List<String> mDatas = Arrays.asList("综合", "少换乘", "少步行", "最快", "路程短");
	private int[] rc = new int[] { 0, 1, 2, 3, 4, 5 };
	private ViewPagerIndicator mIndicator;
	private TextView mTv_title;
	private ImageView iv_back;
	private String city;
	private double startLatLng, startLongitude, mEndLongitude, mEndLatitude;
	private SegParm mSegParm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tranfer);
		ActivityUtils.setActionBarByColor(this, R.layout.title_bar, R.color.title_color);
		Intent intent = getIntent();
		city = intent.getStringExtra("city");
		startLatLng = intent.getDoubleExtra("mLatitude", 0);
		startLongitude = intent.getDoubleExtra("mLongitude", 0);
		mEndLatitude = intent.getDoubleExtra("mEndLatitude", 0);
		mEndLongitude = intent.getDoubleExtra("mEndLongitude", 0);

		LogUtils.e(city + "---" + startLatLng + "---" + startLongitude + "---" + mEndLatitude + "----" + mEndLongitude);
		initView();
		initDatas();
		// 设置Tab上的标题
		mIndicator.setTabItemTitles(mDatas);
		mViewPager.setAdapter(mAdapter);
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				ToastUtil.showCenter(TranferActivity.this, "arg0" + arg0);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		// 设置关联的ViewPager
		mIndicator.setViewPager(mViewPager, 0);
	}

	private void initDatas() {
		for (int i : rc) {
			mSegParm = new SegParm(city, (float) startLongitude, (float) startLatLng, (float) mEndLongitude,
					(float) mEndLatitude, i);
			TranferDetaiFragment fragment = TranferDetaiFragment.newInstance(mSegParm);
			mTabContents.add(fragment);
		}
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return mTabContents.size();
			}

			@Override
			public Fragment getItem(int position) {
				return mTabContents.get(position);
			}

		};
	}

	private void initView() {
		mTv_title = (TextView) findViewById(R.id.tv_actionbar_title);
		mTv_title.setText(R.string.actionbar_title_tranfer_result);
		iv_back = (ImageView) findViewById(R.id.iv_actionbar_back);
		iv_back.setOnClickListener(this);
		mViewPager = (ViewPager) findViewById(R.id.id_vp);
		mIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_actionbar_back:
			finish();
			break;
		default:
			break;
		}

	}

}
