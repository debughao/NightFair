package com.nightfair.mobille.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.activity.CouponDetailActivity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.bean.SellerAndCoupon;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.fragment.MainTab_Index;
import com.nightfair.mobille.util.PixelUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GuessCouponAdapter extends BaseAdapter {

	ArrayList<SellerAndCoupon> list = new ArrayList<SellerAndCoupon>();
	LayoutInflater mInflater;
	ViewHolder viewHolder;
	double mlatitude,mlongitude,pLatitude,pLongitude,distance;
	LatLng p1,p2;
	private Context context;
	public GuessCouponAdapter(ArrayList<SellerAndCoupon> list, Context context) {
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
			convertView=mInflater.inflate(R.layout.item_index_detail, null);
			viewHolder.imageView=(ImageView) convertView.findViewById(R.id.iv_index_img);
			viewHolder.name=(TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.distance=(TextView) convertView.findViewById(R.id.tv_distance);
			viewHolder.introduction=(TextView) convertView.findViewById(R.id.tv_detail_index);
			viewHolder.price=(TextView) convertView.findViewById(R.id.tv_money);
			viewHolder.count=(TextView) convertView.findViewById(R.id.tv_count);
			convertView.setTag(viewHolder);
			convertView.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					LogUtils.e("5555"+position);
					Intent intent = new Intent();
					intent.setClass(context, CouponDetailActivity.class);
					intent.putExtra("seller", list.get(position));
					intent.putExtra("couponposition", 0);
					context.startActivity(intent);
				}
			});
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		BaseApplication.bitmapUtils.display(viewHolder.imageView, AppConstants.SERVERIP+list.get(position).getImg());	
		String currentPrice=list.get(position).getCoupons().get(0).getCurrent_price();
		String originalPrice=list.get(position).getCoupons().get(0).getOriginal_price();
		String price="￥ "+currentPrice+"  ￥"+originalPrice;
		SpannableString spanString = new SpannableString(price);	
		spanString.setSpan(new ForegroundColorSpan(0xffe75858), 0, currentPrice.length()+2,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanString.setSpan(new AbsoluteSizeSpan(PixelUtil.sp2px(20, context)),2,currentPrice.length()+4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanString.setSpan(new StrikethroughSpan(),currentPrice.length()+4,currentPrice.length()+originalPrice.length()+5, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		viewHolder.name.setText(list.get(position).getSeller_name());
		//位置尚未计算出来，数据先由出售商品数量代替
		mlatitude=Double.parseDouble(list.get(position).getLatitude());
		mlongitude=Double.parseDouble(list.get(position).getLongitude());
		pLatitude=MainTab_Index.geoLat;
		pLongitude=MainTab_Index.geoLng;
		if (pLatitude!=0||pLongitude!=0) {
			p1=new LatLng(mlatitude, mlongitude);
			p2=new LatLng(pLatitude, pLongitude);
			distance=AMapUtils.calculateLineDistance(p1, p2);
			if (distance<500.0) {
				viewHolder.distance.setText("< 500m");
			}
			else if (distance>500&&distance<1000.0) {
				DecimalFormat decimalFormat=new DecimalFormat("#.0");
				viewHolder.distance.setText(decimalFormat.format(distance)+"km");
			}else if (distance>1000.0) {
				DecimalFormat decimalFormat=new DecimalFormat("#.0");
				viewHolder.distance.setText("> "+decimalFormat.format(distance/1000)+"km");
			}
		}
		viewHolder.introduction.setText(list.get(position).getCoupons().get(0).getDescription());
		viewHolder.price.setText(spanString);
		viewHolder.count.setText("已售 "+list.get(position).getCoupons().get(0).getSeller_counts());
		return convertView;
	}
	
	class ViewHolder{
		public ImageView imageView;//商品图片
		public TextView name;//商品名称
		public TextView distance;//商店距离
		public TextView introduction;//商品介绍
		public TextView price;//商品价格
		public TextView count;//已售商品数量
	}
 
}
