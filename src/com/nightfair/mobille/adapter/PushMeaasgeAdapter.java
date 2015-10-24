package com.nightfair.mobille.adapter;

import java.util.List;

import com.nightfair.mobille.R;
import com.nightfair.mobille.bean.PushMessage;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PushMeaasgeAdapter extends BaseAdapter {
	
	private List<PushMessage> list;
	private ViewHolder holder;
	private Context context;
	public PushMeaasgeAdapter(List<PushMessage> list, Context context) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context)
					.inflate(R.layout.item_meaasge, null);
			holder.textDatetime = (TextView) convertView
					.findViewById(R.id.tv_message_date);
			holder.textTitle = (TextView) convertView
					.findViewById(R.id.tv_message_title);
			holder.textDescription = (TextView) convertView
					.findViewById(R.id.tv_message_description);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textDatetime.setText(list.get(position).getDatetime());
		holder.textTitle.setText(list.get(position).getTitle());
		holder.textDescription.setText(list.get(position).getDescription());
		return convertView;
	}
	private class ViewHolder {
		TextView textDatetime;
		TextView textTitle;
		TextView textDescription;
	}
}
