package com.nightfair.mobille.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class SellerAndCoupon implements Serializable{

		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getArer() {
		return arer;
	}

	public void setArer(String arer) {
		this.arer = arer;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public ArrayList<Coupons> getCoupons() {
		return coupons;
	}

	public void setCoupons(ArrayList<Coupons> coupons) {
		this.coupons = coupons;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getClassify_id() {
		return classify_id;
	}

	public void setClassify_id(String classify_id) {
		this.classify_id = classify_id;
	}

	public SellerAndCoupon(String longitude, String user_id, String arer, String rank, String latitude, ArrayList<Coupons> coupons, String province, String phone, String city, String id, String street, String seller_name, String img, String classify_id){
		super();
	}

		return "Data [longitude = " + longitude + ", user_id = " + user_id + ", arer = " + arer + ", rank = " + rank + ", latitude = " + latitude + ", coupons = " + coupons + ", province = " + province + ", phone = " + phone + ", city = " + city + ", id = " + id + ", street = " + street + ", seller_name = " + seller_name + ", img = " + img + ", classify_id = " + classify_id + "]";
}