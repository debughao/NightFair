package com.nightfair.mobille.activity;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.util.ToastUtil;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
public class CouponDetailActivity extends BaseActivity  {
	private Context mContext;
	private ImageView iv_coupon_img;
	private RelativeLayout rl_head;
	private PullToRefreshScrollView myScrollView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon_detail);
		mContext = this;
		Intent intent = getIntent();
		String coupon_id = intent.getStringExtra("coupon_id");
		ToastUtil.show(mContext, "获取的优惠券id：" + coupon_id);
		iv_coupon_img = (ImageView) findViewById(R.id.iv_coupon_img);
		rl_head = (RelativeLayout) findViewById(R.id.rl_coupon_detail_head);
		// int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		// LayoutParams params=rl_head.getLayoutParams();
		// params.height=screenWidth/3*2;
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.meituan_150c6537a07);
		int bwidth = bitmap.getWidth();
		int bHeight = bitmap.getHeight();
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = width * bHeight / bwidth;
		LayoutParams param = rl_head.getLayoutParams();
		param.height = height;
		rl_head.setLayoutParams(param);	
		myScrollView = (PullToRefreshScrollView) findViewById(R.id.sv_index_detail);
	}
}
