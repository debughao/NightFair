package com.nightfair.mobille.bean;

import java.io.Serializable;

public class BuyerOrder implements Serializable{	private static final long serialVersionUID = 871003705L;	private SellerInfo sellerInfo;	private Order order;	private Coupon coupon;
	public SellerInfo getSellerInfo() {		return this.sellerInfo;	}
	public void setSellerInfo(SellerInfo sellerInfo) {		this.sellerInfo = sellerInfo;	}
	public Order getOrder() {		return this.order;	}
	public void setOrder(Order order) {		this.order = order;	}
	public Coupon getCoupon() {		return this.coupon;	}
	public void setCoupon(Coupon coupon) {		this.coupon = coupon;	}
	public BuyerOrder() {}
	public BuyerOrder(SellerInfo sellerInfo, Order order, Coupon coupon){
		super();		this.sellerInfo = sellerInfo;		this.order = order;		this.coupon = coupon;
	}
	public String toString() {
		return "Data [sellerInfo = " + sellerInfo + ", order = " + order + ", coupon = " + coupon + "]";	}
}