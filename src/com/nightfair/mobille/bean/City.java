package com.nightfair.mobille.bean;

import java.io.Serializable;

public class City implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public String pinyi;

	public City(String name, String pinyi) {
		super();
		this.name = name;
		this.pinyi = pinyi;
	}

	public City() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyi() {
		return pinyi;
	}

	public void setPinyi(String pinyi) {
		this.pinyi = pinyi;
	}

}

/*
 * 374：
 * 
 * private class ResultListAdapter extends BaseAdapter { private
 * LayoutInflater inflater; private ArrayList<City> results = new
 * ArrayList<City>();
 * 
 * public ResultListAdapter(Context context, ArrayList<City> results) {
 * inflater = LayoutInflater.from(context); this.results = results; }
 * 
 * @Override public int getCount() { return results.size(); }
 * 
 * @Override public Object getItem(int position) { return position; }
 * 
 * @Override public long getItemId(int position) { return position; }
 * 
 * @Override public View getView(int position, View convertView, ViewGroup
 * parent) { ViewHolder viewHolder = null; if (convertView == null) {
 * convertView = inflater.inflate(R.layout.list_item, null); viewHolder =
 * new ViewHolder(); viewHolder.name = (TextView) convertView
 * .findViewById(R.id.name); convertView.setTag(viewHolder); } else {
 * viewHolder = (ViewHolder) convertView.getTag(); }
 * viewHolder.name.setText(results.get(position).getName()); return
 * convertView; }
 * 
 * class ViewHolder { TextView name; } }
 */

/*
 * 
 * 
 * 
 * 
 * 
 * 588：
 * class HotCityAdapter extends BaseAdapter {
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
}*/

/*	class HitCityAdapter extends BaseAdapter {
private Context context;
private LayoutInflater inflater;
private List<String> hotCitys;

public HitCityAdapter(Context context, List<String> hotCitys) {
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
	city.setText(hotCitys.get(position));
	return convertView;
}
}*/
