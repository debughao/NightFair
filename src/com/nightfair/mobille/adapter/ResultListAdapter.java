package com.nightfair.mobille.adapter;

import java.util.ArrayList;

import com.nightfair.mobille.R;
import com.nightfair.mobille.bean.City;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ResultListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<City> results = new ArrayList<City>();

	public ResultListAdapter(Context context, ArrayList<City> results) {
		inflater = LayoutInflater.from(context);
		this.results = results;
	}

	@Override
	public int getCount() {
		return results.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.name.setText(results.get(position).getName());
		return convertView;
	}

	class ViewHolder {
		TextView name;
	}
}
