package com.nightfair.mobille.fragment;

import com.nightfair.mobille.R;
import com.nightfair.mobille.activity.AddFriendActivity;
import com.nightfair.mobille.activity.NewFriendActivity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.receiver.MyMessageReceiver;
import com.nightfair.mobille.util.FragmentUtils;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobNotifyManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.EventListener;

@SuppressLint({ "InflateParams", "ResourceAsColor" })
public class MainTab_Chat extends Fragment implements OnClickListener, EventListener {
	private View rootView;// 缓存Fragment view
	RadioGroup radioGroup;
	RadioButton cpbtn, ctbtn;
	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;
	private ContactFragment contactFragment;
	private RecentFragment recentFragment;

	ImageView iv_recent_tips, iv_contact_tips;// 消息提示
	private RadioButton chat_message;
	private RadioButton chat_contacts;
	NewBroadcastReceiver newReceiver;
	private int currentTabIndex;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.main_tab_chat, null);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		// ActivityUtils.setActionBarByColor(getActivity(),R.layout.title_bar_chat,
		// R.color.title_color);
		BmobChat.getInstance(getActivity()).startPollService(10);
		// 开启广播
		initNewMessageBroadCast();
		initTagMessageBroadCast();
		fragmentManager = getFragmentManager();
		initChatTitleBarViews();
		setTabSelection(0);
		return rootView;
	}

	private void initChatTitleBarViews() {
		radioGroup = (RadioGroup) rootView.findViewById(R.id.chat_title_radiogroup);
		chat_message = (RadioButton) rootView.findViewById(R.id.chat_title_message);
		chat_contacts = (RadioButton) rootView.findViewById(R.id.chat_title_contacts);
		ImageView addimgview = (ImageView) rootView.findViewById(R.id.chat_title_bar_iv_add);
		iv_recent_tips = (ImageView) rootView.findViewById(R.id.iv_recent_tips);
		addimgview.setOnClickListener(this);
		radioGroup.setOnClickListener(this);
		chat_message.setOnClickListener(this);
		chat_contacts.setOnClickListener(this);
	}

	private void setTabSelection(int index) {
		// 重置按钮
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index) {
		case 0:
			currentTabIndex=0;
			if (contactFragment == null) {
				// 如果contactFragment为空，则创建一个并添加到界面上
				contactFragment = new ContactFragment();
				transaction.add(R.id.fragment_container, contactFragment);
			} else {
				// 如果contactFragment不为空，则直接将它显示出来
				transaction.show(contactFragment);
			}
			break;
		case 1:
			currentTabIndex=1;
			if (recentFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				recentFragment = new RecentFragment();
				transaction.add(R.id.fragment_container, recentFragment);
			} else {
				// 如果recentFragment不为空，则直接将它显示出来
				transaction.show(recentFragment);
			}
			break;

		}
		transaction.commit();
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (contactFragment != null) {
			transaction.hide(contactFragment);

		}
		if (recentFragment != null) {
			transaction.hide(recentFragment);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_title_message:
			setTabSelection(1);
			break;
		case R.id.chat_title_contacts:
			setTabSelection(0);
			break;
		case R.id.chat_title_bar_iv_add:
			FragmentUtils.startActivity(this, AddFriendActivity.class);
			break;
		default:
			break;
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		// 小圆点提示
		if (BmobDB.create(getActivity()).hasUnReadMsg() || BmobDB.create(getActivity()).hasNewInvite()) {
			iv_recent_tips.setVisibility(View.VISIBLE);
		} else {
			iv_recent_tips.setVisibility(View.GONE);
		}
		MyMessageReceiver.ehList.add(this);// 监听推送的消息
		// 清空
		MyMessageReceiver.mNewNum = 0;
	}

	@Override
	public void onPause() {
		super.onPause();
		MyMessageReceiver.ehList.remove(this);// 取消监听推送的消息
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			getActivity().unregisterReceiver(newReceiver);
		} catch (Exception e) {
		}
		try {
			getActivity().unregisterReceiver(userReceiver);
		} catch (Exception e) {
		}
		// 取消定时检测服务
		BmobChat.getInstance(getActivity()).stopPollService();
	}

	@Override
	public void onAddUser(BmobInvitation arg0) {

	}

	@Override
	public void onMessage(BmobMsg arg0) {
		refreshNewMsg(arg0);
	}

	@Override
	public void onNetChange(boolean arg0) {

	}

	@Override
	public void onOffline() {

	}

	@Override
	public void onReaded(String arg0, String arg1) {

	}



	private void initNewMessageBroadCast() {
		// 注册接收消息广播
		newReceiver = new NewBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_NEW_MESSAGE);
		// 优先级要低于ChatActivity
		intentFilter.setPriority(3);
		getActivity().registerReceiver(newReceiver, intentFilter);
	}

	/**
	 * 新消息广播接收者
	 * 
	 */
	private class NewBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 刷新界面
			refreshNewMsg(null);
			// 记得把广播给终结掉
			abortBroadcast();
		}
	}

	/**
	 * 刷新界面
	 */
	private void refreshNewMsg(BmobMsg message) {
		// 声音提示
		boolean isAllow = BaseApplication.getInstance().getSpUtil().isAllowVoice();
		if (isAllow) {
			BaseApplication.getInstance().getMediaPlayer().start();
		}
		iv_recent_tips.setVisibility(View.VISIBLE);
		// 也要存储起来
		if (message != null) {
			BmobChatManager.getInstance(getActivity()).saveReceiveMessage(true, message);
		}
		if (currentTabIndex == 1) {
			// 当前页面如果为会话页面，刷新此页面
			if (recentFragment != null) {
				recentFragment.refresh();
			}
		}
	}

	/**
	 * 标签消息广播接收者
	 */
	private class TagBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			BmobInvitation message = (BmobInvitation) intent.getSerializableExtra("invite");
			refreshInvite(message);
			// 记得把广播给终结掉
			abortBroadcast();
		}
	}

	TagBroadcastReceiver userReceiver;

	private void initTagMessageBroadCast() {
		// 注册接收消息广播
		userReceiver = new TagBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_ADD_USER_MESSAGE);
		// 优先级要低于ChatActivity
		intentFilter.setPriority(3);
		getActivity().registerReceiver(userReceiver, intentFilter);
	}


	/**
	 * 刷新好友请求
	 * 
	 * @Title: notifyAddUser
	 */
	private void refreshInvite(BmobInvitation message) {
		boolean isAllow = BaseApplication.getInstance().getSpUtil().isAllowVoice();
		if (isAllow) {
			BaseApplication.getInstance().getMediaPlayer().start();
		}
		iv_recent_tips.setVisibility(View.VISIBLE);
		if (currentTabIndex == 0) {
			if (contactFragment != null) {
				contactFragment.refresh();
			}
		} else {
			// 同时提醒通知
			String tickerText = message.getFromname() + "请求添加好友";
			boolean isAllowVibrate = BaseApplication.getInstance().getSpUtil().isAllowVibrate();
			BmobNotifyManager.getInstance(getActivity()).showNotify(isAllow, isAllowVibrate, R.drawable.ic_launcher, tickerText,
					message.getFromname(), tickerText.toString(), NewFriendActivity.class);
		}
	}

	

}
