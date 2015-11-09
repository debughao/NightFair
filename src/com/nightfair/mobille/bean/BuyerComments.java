package com.nightfair.mobille.bean;

import java.io.Serializable;

public class BuyerComments implements Serializable {

	/**
	 * 2015年11月8日22:06:00
	 */
	private static final long serialVersionUID = 3040781125315312888L;
	private int id;
	private int seller_id;
	private double grade;
	private String comment;
	private String time;
	private String seller_name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}
	public double getGrade() {
		return grade;
	}
	public void setGrade(double grade) {
		this.grade = grade;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSeller_name() {
		return seller_name;
	}
	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}
	public BuyerComments(int id, int seller_id, double grade, String comment, String time, String seller_name) {
		super();
		this.id = id;
		this.seller_id = seller_id;
		this.grade = grade;
		this.comment = comment;
		this.time = time;
		this.seller_name = seller_name;
	}
	public BuyerComments() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "BuyerComments [id=" + id + ", seller_id=" + seller_id + ", grade=" + grade + ", comment=" + comment
				+ ", time=" + time + ", seller_name=" + seller_name + "]";
	}
	
	
	

	
}
