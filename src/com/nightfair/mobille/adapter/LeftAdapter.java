package com.nightfair.mobille.adapter;

import java.util.List;

import com.nightfair.mobille.R;
import com.nightfair.mobille.bean.LeftItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class LeftAdapter extends BaseAdapter {

	private Context context;
	private List<LeftItem> list;

	public LeftAdapter(Context context, List<LeftItem> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_recommendation_left_details, null);

			holder.textView = (TextView) convertView
					.findViewById(R.id.tv_recommendation_left_details);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.iv_recommendation_left_details);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imageView.setImageResource(R.drawable.ic_recommendation_food);
		holder.textView.setText(list.get(position).getText());
		return convertView;
	}

	private int selectedPosition = 0;

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	private class ViewHolder {
		TextView textView;
		ImageView imageView;
	}
}
