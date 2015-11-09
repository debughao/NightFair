package com.nightfair.mobille.bean;

public class Collection {
	/*
	 * 2015年11月9日10:18:37
	 */
	private int id;
	private int buyer_uid;
	private int seller_id;
	private String time;
	private int coupon_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBuyer_uid() {
		return buyer_uid;
	}

	public void setBuyer_uid(int buyer_uid) {
		this.buyer_uid = buyer_uid;
	}

	public int getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getCoupon_id() {
		return coupon_id;
	}

	public void setCoupon_id(int coupon_id) {
		this.coupon_id = coupon_id;
	}

	public Collection() {
		// TODO Auto-generated constructor stub
	}

	public Collection(int buyer_uid, int coupon_id) {
		super();
		this.buyer_uid = buyer_uid;
		this.coupon_id = coupon_id;
	}

	public Collection(int id, int buyer_uid, int seller_id, String time, int coupon_id) {
		super();
		this.id = id;
		this.buyer_uid = buyer_uid;
		this.seller_id = seller_id;
		this.time = time;
		this.coupon_id = coupon_id;
	}

	@Override
	public String toString() {
		return "Collection [id=" + id + ", buyer_uid=" + buyer_uid + ", seller_id=" + seller_id + ", time=" + time
				+ ", coupon_id=" + coupon_id + "]";
	}

}
