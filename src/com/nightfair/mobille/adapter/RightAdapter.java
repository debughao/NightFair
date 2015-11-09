package com.nightfair.mobille.adapter;

import java.util.List;

import com.nightfair.mobille.R;
import com.nightfair.mobille.bean.RightItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class RightAdapter extends BaseAdapter {

	private Context context;
    private List<RightItem> list;
	
	public RightAdapter(Context context, List<RightItem> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list==null?0:list.size();
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
        if (convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recommendation_right_details, null);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_recommendation_right_details);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(list.get(position).getText());

        return convertView;
	}
	 private class ViewHolder{
	        TextView textView;
	    }
}
