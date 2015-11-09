package com.nightfair.mobille.bean;

import java.io.Serializable;

public class Coupon implements Serializable{	private static final long serialVersionUID = 1305904200L;	private String current_price;	private String description;	private int seller_id;	private String update_time;	private int id;	private String original_price;	private int isguess;	private String public_time;	private int seller_counts;	private int isrecommend;
	
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

	public int getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(String original_price) {
		this.original_price = original_price;
	}

	public int getIsguess() {
		return isguess;
	}

	public void setIsguess(int isguess) {
		this.isguess = isguess;
	}

	public String getPublic_time() {
		return public_time;
	}

	public void setPublic_time(String public_time) {
		this.public_time = public_time;
	}

	public int getSeller_counts() {
		return seller_counts;
	}

	public void setSeller_counts(int seller_counts) {
		this.seller_counts = seller_counts;
	}

	public int getIsrecommend() {
		return isrecommend;
	}

	public void setIsrecommend(int isrecommend) {
		this.isrecommend = isrecommend;
	}

	@Override
	public String toString() {
		return "Coupon [current_price=" + current_price + ", description=" + description + ", seller_id=" + seller_id
				+ ", update_time=" + update_time + ", id=" + id + ", original_price=" + original_price + ", isguess="
				+ isguess + ", public_time=" + public_time + ", seller_counts=" + seller_counts + ", isrecommend="
				+ isrecommend + "]";
	}

	public Coupon() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Coupon(String current_price, String description, int seller_id, String update_time, int id,
			String original_price, int isguess, String public_time, int seller_counts, int isrecommend) {
		super();
		this.current_price = current_price;
		this.description = description;
		this.seller_id = seller_id;
		this.update_time = update_time;
		this.id = id;
		this.original_price = original_price;
		this.isguess = isguess;
		this.public_time = public_time;
		this.seller_counts = seller_counts;
		this.isrecommend = isrecommend;
	}
	
}