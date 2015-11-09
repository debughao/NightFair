package com.nightfair.mobille.adapter;

import com.nightfair.mobille.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class RmSortAdapter extends BaseAdapter {

	Context context;
	String sort[];
	LayoutInflater inflater;
	int selectPositon=-1;
	
	public RmSortAdapter(Context context, String[] sort) {
		super();
		this.context = context;
		this.inflater=LayoutInflater.from(context);
		this.sort = sort;
	}

	@Override
	public int getCount() {
		return sort.length;
	}

	@Override
	public Object getItem(int position) {
		return sort[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	ViewHolder  viewHolder;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null) {
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_recommendation_sort_details, null);
			viewHolder.textView=(TextView) convertView.findViewById(R.id.tv_recommendation_sort_details);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.textView.setText(sort[position]);
		return convertView;
	}
	class ViewHolder
	{
		TextView textView;
	}
	public void setSelectedPosition(int positon)
	{
		selectPositon=positon;
	}
}
