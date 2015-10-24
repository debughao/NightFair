package com.nightfair.mobille.activity;

import java.util.ArrayList;
import java.util.List;

import com.nightfair.mobille.R;
import com.nightfair.mobille.adapter.PushMeaasgeAdapter;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.bean.PushMessage;
import com.nightfair.mobille.db.DaoFactory;
import com.nightfair.mobille.db.PushMessageDao;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.TimesGet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class RecommandMessageActivity extends Activity implements OnClickListener {
	private TextView mTv_title;
	private ImageView mIv_back;
	private PushMessageDao mPushMessageDao;
	private Context mContext;
	private List<PushMessage> mList=new ArrayList<PushMessage>();
    private PushMeaasgeAdapter mAdapter;
    private ListView mListView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_listview);
		ActivityUtils.setActionBarByColor(this, R.layout.title_bar, R.color.title_color);
		mContext = this;		 
		initView();
		initData();		
		mAdapter=new PushMeaasgeAdapter(mList, mContext);		
		mListView.setAdapter(mAdapter);
	}

	private void initView() {
		// TODO Auto-generated method stub
		mTv_title = (TextView) findViewById(R.id.tv_actionbar_title);
		mTv_title.setText(R.string.actionbar_title_message);
		mIv_back = (ImageView) findViewById(R.id.iv_actionbar_back);
		mIv_back.setOnClickListener(this);
		mListView=(ListView) findViewById(R.id.list_message);
	}

	private void initData() {
		mPushMessageDao = DaoFactory.getInstance().getPushMessageDao(mContext);
		mList=mPushMessageDao.queryAllRecommendMessage();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_actionbar_back:
			finish();
			break;
		default:
			break;
		}
	}

}
