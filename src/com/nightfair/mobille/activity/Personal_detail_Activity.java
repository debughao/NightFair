package com.nightfair.mobille.activity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.util.ActivityUtils;
import com.tencent.bugly.proguard.r;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @ClassName: Personal_detail_Activity
 * @Description: 个人资料编辑
 * @author debughao
 * @date 2015年10月10日21:43:50
 */
@SuppressLint("ResourceAsColor")
public class Personal_detail_Activity extends BaseActivity implements OnClickListener {
	private Personal_detail_Activity mContent;
	private TextView tv_ps_detail_cancel;
	@ViewInject(R.id.personal_detail_face)
	
	private RelativeLayout personal_detail_face;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_detail);
		mContent = this;
		ViewUtils.inject(this);
		ActivityUtils.setActionBarByColor(mContent, R.layout.title_bar_personal_detail, R.color.title_color);
		inintView();
	}

	private void inintView() {
		tv_ps_detail_cancel = (TextView) findViewById(R.id.tv_ps_detail_cancel);		
		MySetOnClickListener(tv_ps_detail_cancel,personal_detail_face);
	}

	private void MySetOnClickListener(View... v) {
		for (View view : v) {
			view.setOnClickListener(this);	
		}
				
	}

	@Override
	public void finish() {
		flag = 2;
		super.finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_ps_detail_cancel:
			finish();
			break;
		case R.id.personal_detail_face:
			ActivityUtils.showShortToast(mContent, "上传头像");
			break;

		default:
			break;
		}

	}

}
