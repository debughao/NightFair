package com.nightfair.mobille.adapter;

import java.util.List;

import com.nightfair.mobille.R;
import com.nightfair.mobille.bean.City;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HotCityAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<City> hotCitys;

	public HotCityAdapter(Context context, List<City> hotCitys) {
		this.context = context;
		inflater = LayoutInflater.from(this.context);
		this.hotCitys = hotCitys;
	}

	@Override
	public int getCount() {
		return hotCitys.size();
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
		convertView = inflater.inflate(R.layout.item_city, null);
		TextView city = (TextView) convertView.findViewById(R.id.city);
		city.setText(hotCitys.get(position).getName());
		return convertView;
	}
}
