package com.nightfair.mobille.activity;

import java.util.ArrayList;
import java.util.List;

import com.nightfair.mobille.R;
import com.nightfair.mobille.adapter.AddFriendAdapter;
import com.nightfair.mobille.base.Activitybase;
import com.nightfair.mobille.util.CollectionUtils;
import com.nightfair.mobille.view.XListView;
import com.nightfair.mobille.view.XListView.IXListViewListener;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.task.BRequest;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

@SuppressLint("ResourceAsColor")
public class AddFriendActivity extends Activitybase implements OnClickListener,
		IXListViewListener, OnItemClickListener {
	EditText et_find_name;
	Button btn_search;
	List<BmobChatUser> users = new ArrayList<BmobChatUser>();
	XListView mListView;
	AddFriendAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		initActionbar("查找好友",0);
		initView();
	}
	
	/*//设置actionbar
	private void initActionbar() {
		ActivityUtils.setActionBarLayout(getActionBar(), AddFriendActivity.this,
				R.layout.common_title_bar);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.title_color, this);
		ImageView iv_left_back = (ImageView) findViewById(R.id.iv_left_back);
		TextView tv_center_text = (TextView) findViewById(R.id.tv_searchfriend);
		tv_center_text.setVisibility(View.VISIBLE);
		iv_left_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}*/

	private void initView() {
		et_find_name = (EditText) findViewById(R.id.et_find_name);
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);
		initXListView();
	}

	private void initXListView() {
		mListView = (XListView) findViewById(R.id.list_search);
		// 首先不允许加载更多
		mListView.setPullLoadEnable(false);
		// 不允许下拉
		mListView.setPullRefreshEnable(false);
		// 设置监听器
		mListView.setXListViewListener(this);
		//
		mListView.pullRefreshing();

		adapter = new AddFriendAdapter(this, users);
		mListView.setAdapter(adapter);

		mListView.setOnItemClickListener(this);
	}

	int curPage = 0;
	ProgressDialog progress;

	private void initSearchList(final boolean isUpdate) {
		if (!isUpdate) {
			progress = new ProgressDialog(AddFriendActivity.this);
			progress.setMessage("正在搜索...");
			progress.setCanceledOnTouchOutside(true);
			progress.show();
		}

		userManager.queryUserByPage(isUpdate, 0, searchName,
				new FindListener<BmobChatUser>() {

					@Override
					public void onError(int arg0, String arg1) {
						BmobLog.i("查询错误:" + arg1);
						if (users != null) {
							users.clear();
						}
						ShowToast("用户不存在");
						mListView.setPullLoadEnable(false);
						refreshPull();
						// 这样能保证每次查询都是从头开始
						curPage = 0;
					}

					@Override
					public void onSuccess(List<BmobChatUser> arg0) {
						if (CollectionUtils.isNotNull(arg0)) {
							if (isUpdate) {
								users.clear();
							}
							adapter.addAll(arg0);
							if (arg0.size() < BRequest.QUERY_LIMIT_COUNT) {
								mListView.setPullLoadEnable(false);
								ShowToast("用户搜索完成!");
							} else {
								mListView.setPullLoadEnable(true);
							}
						} else {
							BmobLog.i("查询成功:无返回值");
							if (users != null) {
								users.clear();
							}
							ShowToast("用户不存在");
						}
						if (!isUpdate) {
							progress.dismiss();
						} else {
							refreshPull();
						}
						// 这样能保证每次查询都是从头开始
						curPage = 0;
					}
				});

	}

	String searchName = "";

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search:// 搜索
			users.clear();
			searchName = et_find_name.getText().toString();
			if (searchName != null && !searchName.equals("")) {
				initSearchList(false);
			} else {
				ShowToast("请输入用户名");
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 查询更多
	 */
	private void queryMoreSearchList(int page) {
		userManager.queryUserByPage(true, page, searchName,
				new FindListener<BmobChatUser>() {

					@Override
					public void onSuccess(List<BmobChatUser> arg0) {
						if (CollectionUtils.isNotNull(arg0)) {
							adapter.addAll(arg0);
						}
						refreshLoad();
					}

					@Override
					public void onError(int arg0, String arg1) {
						ShowLog("搜索更多用户出错:" + arg1);
						mListView.setPullLoadEnable(false);
						refreshLoad();
					}

				});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		BmobChatUser user = (BmobChatUser) adapter.getItem(position - 1);
		Intent intent = new Intent(this, SetMyInfoActivity.class);
		intent.putExtra("from", "add");
		intent.putExtra("username", user.getUsername());
		startAnimActivity(intent);
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {
		userManager.querySearchTotalCount(searchName, new CountListener() {

			@Override
			public void onSuccess(int arg0) {
				if (arg0 > users.size()) {
					curPage++;
					queryMoreSearchList(curPage);
				} else {
					ShowToast("数据加载完成");
					mListView.setPullLoadEnable(false);
					refreshLoad();
				}
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				ShowLog("查询附近的人总数失败" + arg1);
				refreshLoad();
			}
		});
	}

	private void refreshLoad() {
		if (mListView.getPullLoading()) {
			mListView.stopLoadMore();
		}
	}

	private void refreshPull() {
		if (mListView.getPullRefreshing()) {
			mListView.stopRefresh();
		}
	}
}
