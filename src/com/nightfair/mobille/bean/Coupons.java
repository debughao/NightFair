package com.nightfair.mobille.bean;

import java.io.Serializable;

public class Coupons implements Serializable{	private static final long serialVersionUID = 1385702742L;	public String current_price;	public String description;	public String state;	public String seller_id;	public String update_time;	public String id;	public String original_price;	public String isguess;	public String public_time;	public String seller_counts;	public String isrecommend;	public Coupons() {}
	public String getCurrent_price() {
		return current_price;
	}

	public void setCurrent_price(String current_price) {
		this.current_price = current_price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(String original_price) {
		this.original_price = original_price;
	}

	public String getIsguess() {
		return isguess;
	}

	public void setIsguess(String isguess) {
		this.isguess = isguess;
	}

	public String getPublic_time() {
		return public_time;
	}

	public void setPublic_time(String public_time) {
		this.public_time = public_time;
	}

	public String getSeller_counts() {
		return seller_counts;
	}

	public void setSeller_counts(String seller_counts) {
		this.seller_counts = seller_counts;
	}

	public String getIsrecommend() {
		return isrecommend;
	}

	public void setIsrecommend(String isrecommend) {
		this.isrecommend = isrecommend;
	}

	public Coupons(String current_price, String description, String state, String seller_id, String update_time, String id, String original_price, String isguess, String public_time, String seller_counts, String isrecommend){
		super();		this.current_price = current_price;		this.description = description;		this.state = state;		this.seller_id = seller_id;		this.update_time = update_time;		this.id = id;		this.original_price = original_price;		this.isguess = isguess;		this.public_time = public_time;		this.seller_counts = seller_counts;		this.isrecommend = isrecommend;
	}
	public String toString() {
		return "Coupons [current_price = " + current_price + ", description = " + description + ", state = " + state + ", seller_id = " + seller_id + ", update_time = " + update_time + ", id = " + id + ", original_price = " + original_price + ", isguess = " + isguess + ", public_time = " + public_time + ", seller_counts = " + seller_counts + ", isrecommend = " + isrecommend + "]";	}
}