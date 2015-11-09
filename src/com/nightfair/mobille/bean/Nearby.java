package com.nightfair.mobille.bean;

public class Nearby {
	private int seller_id;
	private String seller_img;
	private String seller_name;
	private double rank;
	private int comment_num;
	private String arer;
	private double longitude;
	private double latitude;

	public int getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}

	public String getSeller_img() {
		return seller_img;
	}

	public void setSeller_img(String seller_img) {
		this.seller_img = seller_img;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public double getRank() {
		return rank;
	}

	public void setRank(double rank) {
		this.rank = rank;
	}

	public int getComment_num() {
		return comment_num;
	}

	public void setComment_num(int comment_num) {
		this.comment_num = comment_num;
	}

	public String getArer() {
		return arer;
	}

	public void setArer(String arer) {
		this.arer = arer;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public Nearby(int seller_id, String seller_img, String seller_name, double rank, int comment_num, String arer,
			double longitude, double latitude) {
		super();
		this.seller_id = seller_id;
		this.seller_img = seller_img;
		this.seller_name = seller_name;
		this.rank = rank;
		this.comment_num = comment_num;
		this.arer = arer;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public Nearby() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Nearby [seller_id=" + seller_id + ", seller_img=" + seller_img + ", seller_name=" + seller_name
				+ ", rank=" + rank + ", comment_num=" + comment_num + ", arer=" + arer + ", longitude=" + longitude
				+ ", latitude=" + latitude + "]";
	}

}
