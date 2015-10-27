package com.nightfair.mobille.activity;

import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.fragment.MainTab_Chat;
import com.nightfair.mobille.fragment.MainTab_Index;
import com.nightfair.mobille.fragment.MainTab_Nearby;
import com.nightfair.mobille.fragment.MainTab_Personal;
import com.nightfair.mobille.util.ActivityUtils;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class MainActivity extends BaseActivity implements OnClickListener {
	private MainActivity mContext;
	private MainTab_Index mTab01;
	private MainTab_Nearby mTab02;
	private MainTab_Chat mTab03;
	private MainTab_Personal mTab04;

	/**
	 * 底部四个按钮
	 */
	private LinearLayout mTabBtnIndex;
	private LinearLayout mTabBtnNearby;
	private LinearLayout mTabBtnChat;
	private LinearLayout mTabBtnPersonal;
	private ImageButton mBtnindex;
	private ImageButton mBtnNearby;
	private ImageButton mBtnChat;
	private ImageButton mBtnPersonal;
	private TextView mTvIndex;
	private TextView mTvNearby;
	private TextView mTvChat;
	private TextView mTvPersonal;
	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;
	private long mExitTime;
	private final static long TIME_DIFF = 2 * 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		ActivityUtils.setTranslucentStatus(this.getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.title_color, this);
		initViews();
		fragmentManager = getFragmentManager();
		setTabSelection(0);
		// 初始化BmobSDK
	}


	private void initViews() {

		mTabBtnIndex = (LinearLayout) findViewById(R.id.id_tab_bottom_index);
		mTabBtnNearby = (LinearLayout) findViewById(R.id.id_tab_bottom_nearby);
		mTabBtnChat = (LinearLayout) findViewById(R.id.id_tab_bottom_chat);
		mTabBtnPersonal = (LinearLayout) findViewById(R.id.id_tab_bottom_personal);
		mTvIndex = ((TextView) mTabBtnIndex.findViewById(R.id.tv_tab_bootom_index));
		mBtnindex = ((ImageButton) mTabBtnIndex.findViewById(R.id.btn_tab_bottom_index));
		mTvPersonal = ((TextView) mTabBtnPersonal.findViewById(R.id.tv_tab_bootom_personal));
		mBtnNearby = ((ImageButton) mTabBtnNearby.findViewById(R.id.btn_tab_bottom_nearby));
		mTvNearby = ((TextView) mTabBtnNearby.findViewById(R.id.tv_tab_bootom_nearby));
		mBtnPersonal = ((ImageButton) mTabBtnPersonal.findViewById(R.id.btn_tab_bottom_personal));
		mBtnChat = ((ImageButton) mTabBtnChat.findViewById(R.id.btn_tab_bottom_chat));
		mTvChat = ((TextView) mTabBtnChat.findViewById(R.id.tv_tab_bootom_chat));
		mTabBtnIndex.setOnClickListener(this);
		mTabBtnNearby.setOnClickListener(this);
		mTabBtnChat.setOnClickListener(this);
		mTabBtnPersonal.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_tab_bottom_index:
			setTabSelection(0);
			break;
		case R.id.id_tab_bottom_nearby:
			setTabSelection(1);
			break;
		case R.id.id_tab_bottom_chat:
			setTabSelection(2);
			break;
		case R.id.id_tab_bottom_personal:
			setTabSelection(3);
			break;

		default:
			break;
		}

	}

	private void setTabSelection(int index) {
		// 重置按钮
		resetBtn();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index) {
		case 0:
			// 当点击了消息tab时，改变控件的图片和文字颜色
			mBtnindex.setImageResource(R.drawable.tab_index_pressed);
			mTvIndex.setTextColor(getResources().getColor(R.color.title_color));
			if (mTab01 == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				mTab01 = new MainTab_Index();
				transaction.add(R.id.id_content, mTab01);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(mTab01);
			}
			break;
		case 1:
			// 当点击了消息tab时，改变控件的图片和文字颜色
			mBtnNearby.setImageResource(R.drawable.tab_nearby_pressed);
			mTvNearby.setTextColor(getResources().getColor(R.color.title_color));

			if (mTab02 == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				mTab02 = new MainTab_Nearby();
				transaction.add(R.id.id_content, mTab02);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(mTab02);
			}
			break;
		case 2:
			// 当点击了动态tab时，改变控件的图片和文字颜色
			mBtnChat.setImageResource(R.drawable.tab_chat_pressed);
			mTvChat.setTextColor(getResources().getColor(R.color.title_color));
			if (mTab03 == null) {
				// 如果NewsFragment为空，则创建一个并添加到界面上
				mTab03 = new MainTab_Chat();
				transaction.add(R.id.id_content, mTab03);
			} else {
				// 如果NewsFragment不为空，则直接将它显示出来
				transaction.show(mTab03);
			}
			break;
		case 3:
			// 当点击了设置tab时，改变控件的图片和文字颜色
			mBtnPersonal.setImageResource(R.drawable.tab_personal_pressed);
			mTvPersonal.setTextColor(getResources().getColor(R.color.title_color));

			if (mTab04 == null) {
				// 如果SettingFragment为空，则创建一个并添加到界面上
				mTab04 = new MainTab_Personal();
				transaction.add(R.id.id_content, mTab04);
			} else {
				// 如果SettingFragment不为空，则直接将它显示出来
				transaction.show(mTab04);
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void resetBtn() {
		mBtnindex.setImageResource(R.drawable.tab_index_normal);
		mTvIndex.setTextColor(getResources().getColor(R.color.tab_title_normal_color));
		mBtnNearby.setImageResource(R.drawable.tab_nearby_normal);
		mTvNearby.setTextColor(getResources().getColor(R.color.tab_title_normal_color));
		mBtnChat.setImageResource(R.drawable.tab_chat_normal);
		mTvChat.setTextColor(getResources().getColor(R.color.tab_title_normal_color));
		mBtnPersonal.setImageResource(R.drawable.tab_personal_normal);
		mTvPersonal.setTextColor(getResources().getColor(R.color.tab_title_normal_color));
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */

	private void hideFragments(FragmentTransaction transaction) {
		if (mTab01 != null) {
			transaction.hide(mTab01);

		}
		if (mTab02 != null) {
			transaction.hide(mTab02);

		}
		if (mTab03 != null) {
			transaction.hide(mTab03);

		}
		if (mTab04 != null) {
			transaction.hide(mTab04);

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println(keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > TIME_DIFF) {
				ActivityUtils.showShortToast(MainActivity.this, "再按一次退出程序");
				mExitTime = System.currentTimeMillis();
			} else {
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
