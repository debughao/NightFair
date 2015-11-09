package com.nightfair.mobille.adapter;

import java.util.ArrayList;

import com.nightfair.mobille.R;
import com.nightfair.mobille.activity.CouponDetailActivity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.bean.BuyerCollection;
import com.nightfair.mobille.bean.Coupon;
import com.nightfair.mobille.bean.Coupons;
import com.nightfair.mobille.bean.SellerAndCoupon;
import com.nightfair.mobille.bean.SellerInfo;
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

public class CollectionAdapter extends BaseAdapter {
	
	private ArrayList<BuyerCollection> list;
	private ViewHolder holder;
	private Context context;
	public CollectionAdapter(ArrayList<BuyerCollection> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context)
					.inflate(R.layout.item_collection_detail, null);
			holder.tv_sellr_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			holder.iv_index_item = (ImageView) convertView
					.findViewById(R.id.iv_index_item);
			holder.tv_money = (TextView) convertView
					.findViewById(R.id.tv_money);
			holder.tv_detail = (TextView) convertView
					.findViewById(R.id.tv_detail);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		BaseApplication.bitmapUtils.display(holder.iv_index_item, AppConstants.SERVERIP+list.get(position).getSellerInfo().getImg());	
		String currentPrice=list.get(position).getCoupon().getCurrent_price();
		String originalPrice=list.get(position).getCoupon().getOriginal_price();
		String price="￥ "+currentPrice+"  ￥"+originalPrice;
		SpannableString spanString = new SpannableString(price);	
		spanString.setSpan(new ForegroundColorSpan(0xffe75858), 0, currentPrice.length()+2,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanString.setSpan(new AbsoluteSizeSpan(40),2,currentPrice.length()+4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanString.setSpan(new StrikethroughSpan(),currentPrice.length()+4,currentPrice.length()+originalPrice.length()+5, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		holder.tv_sellr_name.setText(list.get(position).getSellerInfo().getSeller_name());
		holder.tv_detail.setText(list.get(position).getCoupon().getDescription());		
		holder.tv_money.setText(spanString);
		Coupon coupon=list.get(position).getCoupon();
		SellerInfo sellerInfo=list.get(position).getSellerInfo();
		Coupons coupons2 = new Coupons(coupon.getCurrent_price(), coupon.getDescription(), null,
				String.valueOf(coupon.getSeller_id()), coupon.getUpdate_time(), String.valueOf(coupon.getId()),
				coupon.getOriginal_price(), String.valueOf(coupon.getIsguess()), coupon.getPublic_time(),
				String.valueOf(coupon.getSeller_counts()), String.valueOf(coupon.getIsrecommend()));
		ArrayList<Coupons> coupons = new ArrayList<Coupons>();
		coupons.add(coupons2);	
		final SellerAndCoupon sellerAndCoupon = new SellerAndCoupon(sellerInfo.getLongitude(),
				String.valueOf(sellerInfo.getUser_id()), sellerInfo.getArer(), String.valueOf(sellerInfo.getRank()),
				sellerInfo.getLatitude(), coupons, sellerInfo.getProvince(), sellerInfo.getPhone(),
				sellerInfo.getCity(), String.valueOf(sellerInfo.getId()), sellerInfo.getStreet(),
				sellerInfo.getSeller_name(), sellerInfo.getImg(), sellerInfo.getClassify_id());
		convertView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, CouponDetailActivity.class);
				intent.putExtra("seller", sellerAndCoupon);
				intent.putExtra("couponposition", 0);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	private class ViewHolder {
		ImageView iv_index_item;
		TextView tv_sellr_name;
		TextView tv_detail;
		TextView tv_money;
	}
}
