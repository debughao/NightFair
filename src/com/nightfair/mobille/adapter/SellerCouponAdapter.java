package com.nightfair.mobille.adapter;

import java.util.ArrayList;

import com.nightfair.mobille.R;
import com.nightfair.mobille.bean.Comments;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.util.ImageLoadOptions;
import com.nightfair.mobille.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class SellerCouponAdapter extends BaseAdapter {

	ArrayList<Comments> list = new ArrayList<Comments>();
	LayoutInflater mInflater;
	ViewHolder viewHolder;
	private Context context;
	public SellerCouponAdapter(ArrayList<Comments> list, Context context) {
		super();
		this.context=context;
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView==null) {
			viewHolder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.item_comment_detail, null);
			viewHolder.imageView=(CircleImageView) convertView.findViewById(R.id.iv_commenter_face);
			viewHolder.commenter_nick=(TextView) convertView.findViewById(R.id.tv_commenter_nick);
			viewHolder.room_ratingbar=(RatingBar) convertView.findViewById(R.id.comment_ratingbar);
			viewHolder.tv_comment_time=(TextView) convertView.findViewById(R.id.tv_comment_time);
			viewHolder.tv_comment_cotent=(TextView) convertView.findViewById(R.id.tv_comment_cotent);
			convertView.setTag(viewHolder);
//			viewHolder.imageView.setOnClickListener(new OnClickListener() {			
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					LogUtils.e("5555"+position);
//					Intent intent = new Intent();
//					intent.setClass(context, CouponDetailActivity.class);
//					intent.putExtra("seller", list.get(position));
//					intent.putExtra("couponposition", 0);
//					context.startActivity(intent);
//				}
//			});
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		final String avatar = list.get(position).getBuyerimg();
		if (!TextUtils.isEmpty(avatar)) {
			ImageLoader.getInstance().displayImage(AppConstants.SERVERIP+avatar, viewHolder.imageView, ImageLoadOptions.getOptions());
		} else {
			viewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.my_dd_icon_default));
		}
		viewHolder.commenter_nick.setText(list.get(position).getNickname());
		viewHolder.room_ratingbar.setRating((float) list.get(position).getGrade());
		viewHolder.tv_comment_time.setText(list.get(position).getTime());
		viewHolder.tv_comment_cotent.setText(list.get(position).getComment());
		return convertView;
	}	
	class ViewHolder{
		public CircleImageView imageView;//商品图片
		public TextView commenter_nick;//评论者昵称
		public RatingBar room_ratingbar;//评论者打分
		public TextView tv_comment_time;//评论时间
		public TextView tv_comment_cotent;//评论内容
	}
 
}
