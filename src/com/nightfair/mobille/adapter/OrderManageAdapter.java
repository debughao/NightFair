package com.nightfair.mobille.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.activity.CommentActivity;
import com.nightfair.mobille.activity.LookCouponNumActivity;
import com.nightfair.mobille.activity.OrderDetailActivity;
import com.nightfair.mobille.activity.SubmitorderActivity;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.bean.BuyerOrder;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.dialog.LoadingDialog;
import com.nightfair.mobille.dialog.SelectionDialog;
import com.nightfair.mobille.dialog.BaseDialog.OnConfirmListener;
import com.nightfair.mobille.util.NetUtils;
import com.nightfair.mobille.util.ToastUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrderManageAdapter extends BaseAdapter {
	ArrayList<BuyerOrder> list = new ArrayList<BuyerOrder>();
	LayoutInflater mInflater;
	ViewHolder viewHolder;
	private Context context;
	private Callback callback;
	private int state;

	public OrderManageAdapter(ArrayList<BuyerOrder> list, Context context, Callback callback) {
		super();
		this.callback = callback;
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
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
		// TODO Auto-generated method stub
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_order_manage, null);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_order_img);
			viewHolder.seller_nick = (TextView) convertView.findViewById(R.id.tv_order_couponname);
			viewHolder.coupon_num = (TextView) convertView.findViewById(R.id.tv_order_manseg_num);
			viewHolder.tv_order_amount = (TextView) convertView.findViewById(R.id.tv_order_amount);
			viewHolder.tv_order_msg = (TextView) convertView.findViewById(R.id.tv_order_msg);
			viewHolder.tv_order_msg_a = (Button) convertView.findViewById(R.id.tv_order_bt_msg_a);
			viewHolder.tv_order_msg_b = (Button) convertView.findViewById(R.id.tv_order_bt_msg_b);
			viewHolder.rl_msg = (RelativeLayout) convertView.findViewById(R.id.rl_msg);
			viewHolder.ll_order_parent = (LinearLayout) convertView.findViewById(R.id.ll_order_parent);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String img = list.get(position).getSellerInfo().getImg();
		state = list.get(position).getOrder().getState();
		if (!TextUtils.isEmpty(img)) {
			BaseApplication.bitmapUtils.display(viewHolder.imageView, AppConstants.SERVERIP + img);
		} else {
			viewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.my_dd_icon_default));
		}
		DecimalFormat df = new DecimalFormat("#.00");// 保留2位小数
		viewHolder.seller_nick.setText(list.get(position).getSellerInfo().getSeller_name() + "优惠券");
		viewHolder.coupon_num.setText(list.get(position).getOrder().getNum());
		viewHolder.tv_order_amount.setText(df.format(list.get(position).getOrder().getAmount()));
		ininMgsText(state, position);
		ininMgsClick(state, position);
		viewHolder.ll_order_parent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, OrderDetailActivity.class);
				intent.putExtra("BuyerOrder", list.get(position));
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	private void ininMgsText(int state, int position) {
		// TODO Auto-generated method stub
		switch (state) {
		case 0:
			viewHolder.tv_order_msg.setText("待付款");
			viewHolder.tv_order_msg_a.setText("取消订单");
			viewHolder.tv_order_msg_b.setText("付款");
			break;
		case 1:
			viewHolder.tv_order_msg.setText("待消费");
			viewHolder.tv_order_msg_a.setVisibility(View.GONE);
			viewHolder.tv_order_msg_b.setText("查看券码");
			break;
		case 2:
			viewHolder.tv_order_msg.setText("待评价");
			viewHolder.tv_order_msg_a.setVisibility(View.GONE);
			viewHolder.tv_order_msg_b.setText("评价");
			break;
		case 3:
			int isrefues = list.get(position).getOrder().getIsrefues();
			if (isrefues == 0) {
				viewHolder.tv_order_msg.setText("退款中");
			} else if (isrefues == 1) {
				viewHolder.tv_order_msg.setText("已退款");
			}
			viewHolder.tv_order_msg_a.setVisibility(View.GONE);
			viewHolder.rl_msg.setVisibility(View.GONE);
			break;
		case 4:
			viewHolder.tv_order_msg.setText("已评价");
			viewHolder.tv_order_msg_a.setVisibility(View.GONE);
			viewHolder.rl_msg.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	private void ininMgsClick(int state, final int position) {
		// TODO Auto-generated method stub
		switch (state) {
		case 0:
			viewHolder.tv_order_msg_a.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					cancalOrder(position);
				}
			});
			viewHolder.tv_order_msg_b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String seller_id = String.valueOf(list.get(position).getOrder().getSeller_id());
					LogUtils.e(position + "---------" + seller_id);
					Intent intent = new Intent();
					intent.setClass(context, SubmitorderActivity.class);
					intent.putExtra("action", "resubmint");
					intent.putExtra("coupon_name", list.get(position).getSellerInfo().getSeller_name());
					intent.putExtra("currentPrice", list.get(position).getCoupon().getCurrent_price());
					intent.putExtra("num", list.get(position).getOrder().getNum());
					intent.putExtra("seller_id", seller_id);
					intent.putExtra("coupon_id", String.valueOf(list.get(position).getCoupon().getId()));
					context.startActivity(intent);
				}
			});
			break;
		case 1:
			viewHolder.tv_order_msg_b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(context, LookCouponNumActivity.class);
					intent.putExtra("coupon_name", list.get(position).getSellerInfo().getSeller_name());
					intent.putExtra("coupon_num", list.get(position).getOrder().getCoupon_num());
					context.startActivity(intent);
				}
			});
			break;
		case 2:
			viewHolder.tv_order_msg_b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent =new Intent(context, CommentActivity.class);
					intent.putExtra("ordeid", list.get(position).getOrder().getOrder_id());
					intent.putExtra("sellerid",list.get(position).getSellerInfo().getId());
					context.startActivity(intent);
				}
			});
			break;

		default:
			break;
		}

	}

	private void cancalOrder(final int position) {
		// TODO Auto-generated method stub

		SelectionDialog dialog = new SelectionDialog(context, "确定取消订单吗？");
		dialog.setOnConfirmListener(new OnConfirmListener() {

			@Override
			public void onConfirm(String result) {
				// TODO Auto-generated method stub
				final LoadingDialog dialog = new LoadingDialog(context, "正在取消订单");
				dialog.show();
				int order_id = list.get(position).getOrder().getOrder_id();
				HttpUtils httpUtils = BaseApplication.httpUtils;
				RequestParams params = new RequestParams();
				params.addBodyParameter("key", AppConstants.KEY);
				params.addBodyParameter("action", "cancal");
				params.addBodyParameter("orderid", String.valueOf(order_id));
				httpUtils.send(HttpMethod.POST, AppConstants.GETORDER, params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {

						NetUtils.coonFairException(arg1, context);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String result = arg0.result;
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(result);
							int state = jsonObject.optInt("state");
							String results = jsonObject.optString("result");
							if (state == 200) {
								dialog.dismiss();
								list.remove(position);
								if (list.size() == 0) {
									callback.isEmpty(true);
								}
								notifyDataSetChanged();
								ToastUtil.show(context, results);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
		});
		dialog.show();
	}

	class ViewHolder {
		public ImageView imageView;
		public TextView seller_nick;
		public TextView coupon_num;
		public TextView tv_order_amount;
		public TextView tv_order_msg;
		public Button tv_order_msg_a;
		public Button tv_order_msg_b;
		public RelativeLayout rl_msg;
		public LinearLayout ll_order_parent;
	}

	public interface Callback {
		void isEmpty(boolean isEmpty);
	}
}
