package com.nightfair.mobille.bean;

import java.io.Serializable;

public class SellerInfo implements Serializable{	private static final long serialVersionUID = 896242253L;	private String longitude;	private int user_id;	private String arer;	private double rank;	private String latitude;	private String province;	private String phone;	private String city;	private int id;	private String street;	private String seller_name;	private String img;	private String classify_id;
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getArer() {
		return arer;
	}
	public void setArer(String arer) {
		this.arer = arer;
	}
	public double getRank() {
		return rank;
	}
	public void setRank(double rank) {
		this.rank = rank;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public SellerInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SellerInfo(String longitude, int user_id, String arer, double rank, String latitude, String province,
			String phone, String city, int id, String street, String seller_name, String img, String classify_id) {
		super();
		this.longitude = longitude;
		this.user_id = user_id;
		this.arer = arer;
		this.rank = rank;
		this.latitude = latitude;
		this.province = province;
		this.phone = phone;
		this.city = city;
		this.id = id;
		this.street = street;
		this.seller_name = seller_name;
		this.img = img;
		this.classify_id = classify_id;
	}
	@Override
	public String toString() {
		return "SellerInfo [longitude=" + longitude + ", user_id=" + user_id + ", arer=" + arer + ", rank=" + rank
				+ ", latitude=" + latitude + ", province=" + province + ", phone=" + phone + ", city=" + city + ", id="
				+ id + ", street=" + street + ", seller_name=" + seller_name + ", img=" + img + ", classify_id="
				+ classify_id + "]";
	}
	
}