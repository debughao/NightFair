package com.nightfair.mobille.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.nightfair.mobille.R;
import com.nightfair.mobille.activity.ShopDetailActivity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.bean.Nearby;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.fragment.MainTab_Nearby;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class NearbyShopAdapter extends BaseAdapter {

	ArrayList<Nearby> list = new ArrayList<Nearby>();
	Context context;
	LayoutInflater inflater;
	ViewHolder viewHolder;
	double mlatitude, mlongitude, pLatitude, pLongitude, distance;
	LatLng p1, p2;

	public NearbyShopAdapter(ArrayList<Nearby> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
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

	@SuppressLint({ "InflateParams", "UseValueOf" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_nearby_detail, null);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_nearby_img);
			viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.comment_ratingbar = (RatingBar) convertView.findViewById(R.id.comment_ratingbar);
			viewHolder.tv_near_commentnum = (TextView) convertView.findViewById(R.id.tv_near_commentnum);
			viewHolder.tv_nearby_address = (TextView) convertView.findViewById(R.id.tv_nearby_address);
			viewHolder.distance = (TextView) convertView.findViewById(R.id.tv_nearby_distance);
			convertView.setTag(viewHolder);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
	                 Intent intent =new Intent(context, ShopDetailActivity.class);
	                 intent.putExtra("seller_id", String.valueOf(list.get(position).getSeller_id()));
	                 context.startActivity(intent);
				}
			});
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		BaseApplication.bitmapUtils.display(viewHolder.imageView, AppConstants.SERVERIP + list.get(position).getSeller_img());
		viewHolder.tv_name.setText(list.get(position).getSeller_name());
		viewHolder.tv_near_commentnum.setText(list.get(position).getComment_num()+"评价");
		viewHolder.tv_nearby_address.setText(list.get(position).getArer());
		viewHolder.comment_ratingbar.setRating((float)list.get(position).getRank());
		mlatitude = list.get(position).getLatitude();
		mlongitude = list.get(position).getLongitude();
		pLatitude = MainTab_Nearby.geoLat;
		pLongitude = MainTab_Nearby.geoLng;
		if (pLatitude != 0 || pLongitude != 0) {
			p1 = new LatLng(mlatitude, mlongitude);
			p2 = new LatLng(pLatitude, pLongitude);
			distance = AMapUtils.calculateLineDistance(p1, p2);
			if (distance < 500.0) {
				viewHolder.distance.setText("< 500m");
			} else if (distance > 500 && distance < 1000) {
				DecimalFormat decimalFormat = new DecimalFormat("#.0");
				viewHolder.distance.setText(decimalFormat.format(distance) + "km");
			} else if (distance > 1000.0) {
				DecimalFormat decimalFormat = new DecimalFormat("#.0");
				viewHolder.distance.setText("> " +decimalFormat.format( distance / 1000) + "km");
			}
		}
	
		return convertView;
	}

	class ViewHolder {
		public ImageView imageView;
		public TextView tv_name;
		public TextView distance;
		public TextView tv_nearby_address;
		public RatingBar comment_ratingbar;
		public TextView tv_near_commentnum;
	}
}
