package com.nightfair.mobille.adapter;

import java.util.ArrayList;

import com.nightfair.mobille.R;
import com.nightfair.mobille.bean.SellerInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SerachAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<SellerInfo> sellername;
	ViewHolder viewHolder;

	public SerachAdapter(Context context, ArrayList<SellerInfo> sellername) {
		this.context = context;
		inflater = LayoutInflater.from(this.context);
		this.sellername = sellername;
	}

	@Override
	public int getCount() {
		return sellername.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_serach, null);
			viewHolder.sellername = (TextView) convertView.findViewById(R.id.tv_serach_item);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.sellername.setText(sellername.get(position).getSeller_name());
		return convertView;
	}

	class ViewHolder {

		public TextView sellername;// 已售商品数量
	}

}
