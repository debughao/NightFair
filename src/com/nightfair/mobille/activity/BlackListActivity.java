package com.nightfair.mobille.activity;


import com.nightfair.mobille.R;
import com.nightfair.mobille.adapter.BlackListAdapter;
import com.nightfair.mobille.base.Activitybase;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.dialog.DiaLogTips;
import com.nightfair.mobille.dialog.DiaLogTips.DialogTips;
import com.nightfair.mobille.util.CollectionUtils;
import com.nightfair.mobille.util.ToastUtil;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.listener.UpdateListener;


/**
 * 黑名单列表
 */
public class BlackListActivity extends Activitybase implements OnItemClickListener {

	ListView listview;
	BlackListAdapter adapter;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=this;
		setContentView(R.layout.activity_blacklist);
		initActionbar("黑名单",0);
		initView();
	}

	private void initView() {
		adapter = new BlackListAdapter(this, BmobDB.create(this).getBlackList());
		listview = (ListView) findViewById(R.id.list_blacklist);
		listview.setOnItemClickListener(this);
		listview.setAdapter(adapter);
		
	}

	/** 显示移除黑名单对话框
	  */
	public void showRemoveBlackDialog(final int position, final BmobChatUser user) {
		DiaLogTips base = new DiaLogTips();
		DialogTips dialog = base.new DialogTips(this, "移出黑名单",
				"你确定将"+user.getUsername()+"移出黑名单吗?", "确定", true, true);
		// 设置成功事件
		dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int userId) {
				adapter.remove(position);
				userManager.removeBlack(user.getUsername(),new UpdateListener() {
					
					

					@Override
					public void onSuccess() {
						ToastUtil.showCenter(context,"移出黑名单成功");
						//重新设置下内存中保存的好友列表
						BaseApplication.getInstance().setContactList(CollectionUtils.list2map(BmobDB.create(getApplicationContext()).getContactList()));	
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						ToastUtil.showCenter(context,"移出黑名单失败:"+arg1);
					}
				});
			}
		});
		// 显示确认对话框
		dialog.show();
		dialog = null;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		BmobChatUser invite = (BmobChatUser) adapter.getItem(arg2);
		showRemoveBlackDialog(arg2,invite);
	}


}
