package com.nightfair.mobille.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.bean.Bus;
import com.nightfair.mobille.bean.Segment;
import com.nightfair.mobille.util.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BusTranferSegAdapter extends BaseAdapter {
	private ArrayList<Bus> list = new ArrayList<Bus>();
	private Context context;

	public BusTranferSegAdapter(ArrayList<Bus> list, Context mContext) {
		super();
		this.context = mContext;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_bus_tranfer, null);
			holder.num = (TextView) convertView.findViewById(R.id.tv_bus_tranfer_seg);
			holder.time = (TextView) convertView.findViewById(R.id.tv_bus_time);
			holder.dist = (TextView) convertView.findViewById(R.id.tv_bus_dist);
			holder.busnum = (TextView) convertView.findViewById(R.id.tv_bus_num);
			holder.footDist = (TextView) convertView.findViewById(R.id.tv_bus_footdist);
			holder.context = (TextView) convertView.findViewById(R.id.tv_bus_segcontent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ArrayList<Segment> segment = list.get(position).getSegments().getSegment();
		String busNum = "";
		if (segment.size() <= 1) {
			busNum = StringUtils.subString(segment.get(0).getLine_name(), "(");
		} else {
			for (int i = 0; i < segment.size() - 1; i++) {
				busNum = StringUtils.subString(segment.get(0).getLine_name(), "(") + "→";
			}
			busNum += StringUtils.subString(segment.get(segment.size() - 1).getLine_name(), "(");
		}
		double dist = Double.parseDouble(list.get(position).getDist()) / 1000;
		DecimalFormat df = new DecimalFormat("#.00");// 保留2位小数
		String dists = df.format(dist);
		// String context = getSegString(segment);
		holder.num.setText("方案" + String.valueOf(position + 1));
		holder.time.setText(list.get(position).getTime() + "分钟");
		holder.dist.setText(dists + "里");
		holder.footDist.setText("步行" + list.get(position).getFoot_dist() + "米");
		holder.busnum.setText(busNum);
		// holder.context.setText(context);
		addImageSpan(segment, holder.context);
		return convertView;
	}

	private String getSegString(ArrayList<Segment> segment) {
		// TODO Auto-generated method stub
		LogUtils.e("—————————————大小-----------segment" + segment.size());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < segment.size(); i++) {
			if (i == 0) {
				sb.append("1·····1");
				sb.append(segment.get(i).getStart_stat());
				sb.append("  →  1");
				sb.append(segment.get(i).getEnd_stat());
			} else {
				sb.append("·····1·····1");
				sb.append(segment.get(i).getStart_stat());
				sb.append("  →  1");
				sb.append(segment.get(i).getEnd_stat());
			}
		}
		sb.append("·····1····· 终点");
		return sb.toString();
	}

	// 图片置换
	private void addImageSpan(ArrayList<Segment> segment, TextView textView) {
		String s = getSegString(segment);
		SpannableString spanString = new SpannableString(s);
		Drawable d1 = context.getResources().getDrawable(R.drawable.ic_footer);
		d1.setBounds(0, 0, 40, 40);
		Drawable d2 = context.getResources().getDrawable(R.drawable.icon_seg_up);
		d2.setBounds(0, 0, 45, 45);
		Drawable d3 = context.getResources().getDrawable(R.drawable.icon_seg_down);
		d3.setBounds(0, 0, 45, 45);
		for (int i = 0; i < segment.size(); i++) {
			int n = segment.get(0).getStart_stat().length();
			int m = segment.get(0).getEnd_stat().length();
			if (i == 0) {
				spanString.setSpan(new ImageSpan(d1, ImageSpan.ALIGN_BASELINE), 0, 1, Spannable.SPAN_COMPOSING);
				spanString.setSpan(new ImageSpan(d2, ImageSpan.ALIGN_BASELINE), 6, 7, Spannable.SPAN_COMPOSING);
				spanString.setSpan(new ImageSpan(d3, ImageSpan.ALIGN_BASELINE), n + 12, n + 13,
						Spannable.SPAN_COMPOSING);
				spanString.setSpan(new ImageSpan(d1, ImageSpan.ALIGN_BASELINE), m + n + 18, m + n + 19,
						Spannable.SPAN_COMPOSING);
			} else if (i == 1) {
				int m2 = segment.get(1).getStart_stat().length();
				int n2 = segment.get(1).getEnd_stat().length();
				spanString.setSpan(new ImageSpan(d1, ImageSpan.ALIGN_BASELINE), m + n + 18, m + n + 19,
						Spannable.SPAN_COMPOSING);
				spanString.setSpan(new ImageSpan(d2, ImageSpan.ALIGN_BASELINE), m + n + 24, m + n + 25,
						Spannable.SPAN_COMPOSING);
				spanString.setSpan(new ImageSpan(d3, ImageSpan.ALIGN_BASELINE), m + n + m2 + 30, m + n + m2 + 31,
						Spannable.SPAN_COMPOSING);
				spanString.setSpan(new ImageSpan(d1, ImageSpan.ALIGN_BASELINE), m + n + m2 + n2 + 36,
						m + n + m2 + n2 + 37, Spannable.SPAN_COMPOSING);

			}
		}
		textView.setText(spanString);
	}

	class ViewHolder {
		public TextView num;
		public TextView time;// 时间
		public TextView dist;// 总距离
		public TextView footDist;// 步行距离
		public TextView busnum;// 换乘公交
		public TextView context;
	}
}
