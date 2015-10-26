package com.nightfair.mobille.adapter;

import java.util.ArrayList;
import java.util.List;
import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.bean.Goods;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsAdapter extends BaseAdapter {

	List<Goods> list = new ArrayList<Goods>();
	LayoutInflater mInflater;
	ViewHolder viewHolder;

	public GoodsAdapter(List<Goods> list, Context context) {
		super();
		this.list = list;
		this.mInflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null) {
			viewHolder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.item_index_detail, null);
			viewHolder.imageView=(ImageView) convertView.findViewById(R.id.iv_index_item);
			viewHolder.name=(TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.distance=(TextView) convertView.findViewById(R.id.tv_distance);
			viewHolder.introduction=(TextView) convertView.findViewById(R.id.tv_detail);
			viewHolder.price=(TextView) convertView.findViewById(R.id.tv_money);
			viewHolder.count=(TextView) convertView.findViewById(R.id.tv_count);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		BaseApplication.bitmapUtils.display(viewHolder.imageView, list.get(position).getImg());
		viewHolder.name.setText(list.get(position).getGood_name());
		//位置尚未计算出来，数据先由出售商品数量代替
		viewHolder.distance.setText(list.get(position).getSeller_counts());
		viewHolder.introduction.setText(list.get(position).getIntroduction());
		viewHolder.price.setText(list.get(position).getReal_price());
		viewHolder.count.setText(list.get(position).getSeller_counts());
		return convertView;
	}
	
	class ViewHolder{
		public ImageView imageView;//商品图片
		public TextView name;//商品名称
		public TextView distance;//商店距离
		public TextView introduction;//商品介绍
		public TextView price;//商品价格
		public TextView count;//已售商品数量
	}

}
