package com.nightfair.mobille.bean;

import java.io.Serializable;

public class Order implements Serializable {
	private static final long serialVersionUID = 1305904200L;
	private String order_time;
	private double amount;
	private int state;
	private String num;
	private int user_id;
	private int coupon_id;
	private int seller_id;
	private int order_id;
	private String coupon_num;
	private int isrefues;
	
	public String getOrder_time() {
		return order_time;
	}

	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getCoupon_id() {
		return coupon_id;
	}

	public void setCoupon_id(int coupon_id) {
		this.coupon_id = coupon_id;
	}

	public int getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public String getCoupon_num() {
		return coupon_num;
	}

	public void setCoupon_num(String coupon_num) {
		this.coupon_num = coupon_num;
	}

	public int getIsrefues() {
		return isrefues;
	}

	public void setIsrefues(int isrefues) {
		this.isrefues = isrefues;
	}

	public Order(String order_time, double amount, int state, String num, int user_id, int coupon_id, int seller_id,
			int order_id, String coupon_num, int isrefues) {
		super();
		this.order_time = order_time;
		this.amount = amount;
		this.state = state;
		this.num = num;
		this.user_id = user_id;
		this.coupon_id = coupon_id;
		this.seller_id = seller_id;
		this.order_id = order_id;
		this.coupon_num = coupon_num;
		this.isrefues = isrefues;
	}

	@Override
	public String toString() {
		return "Order [order_time=" + order_time + ", amount=" + amount + ", state=" + state + ", num=" + num
				+ ", user_id=" + user_id + ", coupon_id=" + coupon_id + ", seller_id=" + seller_id + ", order_id="
				+ order_id + ", coupon_num=" + coupon_num + ", isrefues=" + isrefues + "]";
	}
	
}