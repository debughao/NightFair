package com.nightfair.mobille.adapter;

import java.util.ArrayList;
import com.nightfair.mobille.R;
import com.nightfair.mobille.bean.BuyerComments;
import com.nightfair.mobille.util.ToastUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	
	private ArrayList<BuyerComments> list;
	private ViewHolder holder;
	private Context context;
	public CommentAdapter( ArrayList<BuyerComments> list, Context context) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context)
					.inflate(R.layout.item_comments, null);
			holder.tv_sellr_name = (TextView) convertView
					.findViewById(R.id.tv_sellr_name);
			holder.comment_ratingbar = (RatingBar) convertView
					.findViewById(R.id.comment_ratingbar);
			holder.texttime = (TextView) convertView
					.findViewById(R.id.tv_comment_time);
			holder.textDescription = (TextView) convertView
					.findViewById(R.id.tv_commnet_cotent);
			holder.rl_tv_commnet_seller = (RelativeLayout) convertView
					.findViewById(R.id.rl_tv_commnet_seller);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_sellr_name.setText(list.get(position).getSeller_name());
		holder.textDescription.setText(list.get(position).getComment());
		holder.comment_ratingbar.setRating((float)list.get(position).getGrade());
		holder.texttime.setText(list.get(position).getTime());
		holder.rl_tv_commnet_seller.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ToastUtil.show(context, ""+list.get(position).getSeller_id());
			}
		});
		return convertView;
	}
	private class ViewHolder {
		RatingBar comment_ratingbar;
		TextView tv_sellr_name;
		TextView textDescription;
		TextView texttime;
		RelativeLayout rl_tv_commnet_seller;
	}
}
