package com.nightfair.mobille.adapter;

import java.util.ArrayList;

import com.nightfair.mobille.R;
import com.nightfair.mobille.activity.CouponDetailActivity;
import com.nightfair.mobille.activity.ShopDetailActivity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.bean.Coupons;
import com.nightfair.mobille.config.AppConstants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopCouponAdapter extends BaseAdapter {

	ArrayList<Coupons> list = new ArrayList<Coupons>();
	LayoutInflater mInflater;
	ViewHolder viewHolder;
	private Context context;
	public ShopCouponAdapter(ArrayList<Coupons> list, Context context) {
		super();
		this.context=context;
		this.list = list;
		this.mInflater=LayoutInflater.from(context);
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView==null) {
			viewHolder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.item_shop_detail, null);
			viewHolder.imageView=(ImageView) convertView.findViewById(R.id.iv_index_item);
			viewHolder.introduction=(TextView) convertView.findViewById(R.id.tv_detail);
			viewHolder.price=(TextView) convertView.findViewById(R.id.tv_money);
			viewHolder.count=(TextView) convertView.findViewById(R.id.tv_count);
			convertView.setTag(viewHolder);
			convertView.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(context, CouponDetailActivity.class);
					intent.putExtra("couponposition", position);
					intent.putExtra("get", true);
					context.startActivity(intent);
				}
			});
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		BaseApplication.bitmapUtils.display(viewHolder.imageView, AppConstants.SERVERIP+ShopDetailActivity.imgUrl);	
		String currentPrice=list.get(position).getCurrent_price();
		String originalPrice=list.get(position).getOriginal_price();
		String price="￥ "+currentPrice+"  ￥"+originalPrice;
		SpannableString spanString = new SpannableString(price);	
		spanString.setSpan(new ForegroundColorSpan(0xffe75858), 0, currentPrice.length()+2,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanString.setSpan(new AbsoluteSizeSpan(40),2,currentPrice.length()+4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanString.setSpan(new StrikethroughSpan(),currentPrice.length()+4,currentPrice.length()+originalPrice.length()+5, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		//位置尚未计算出来，数据先由出售商品数量代替
		viewHolder.introduction.setText(list.get(position).getDescription());
		viewHolder.price.setText(spanString);
		viewHolder.count.setText("已售 "+list.get(position).getSeller_counts());
		return convertView;
	}
	
	class ViewHolder{
		public ImageView imageView;//商品图片
		public TextView introduction;//商品介绍
		public TextView price;//商品价格
		public TextView count;//已售商品数量
	}
 
}
