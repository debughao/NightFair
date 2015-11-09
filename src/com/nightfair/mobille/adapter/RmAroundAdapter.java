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
public class RmAroundAdapter extends BaseAdapter {

	Context context;
	String distance[];
	LayoutInflater inflater;
	int selectPositon=-1;
	
	
	
	public RmAroundAdapter(Context context, String[] distance) {
		super();
		this.context = context;
		this.distance = distance;
		this.inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return distance.length;
	}

	@Override
	public Object getItem(int position) {
		return distance[position];
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
			convertView=inflater.inflate(R.layout.item_recommendation_around_details, null);
			viewHolder.textView=(TextView) convertView.findViewById(R.id.tv_recommendation_around_details);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.textView.setText(distance[position]);
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
