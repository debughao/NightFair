package com.nightfair.mobille.bean;

import java.io.Serializable;

public class Comments implements Serializable {
	/**
	 * 评论
	 * 2015-11-4 19:03:05
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private double grade;
	private String comment;
	private String time;
	private int  buyer_id;
	private String nickname;
	private String buyerimg;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getBuyerimg() {
		return buyerimg;
	}

	public void setBuyerimg(String buyerimg) {
		this.buyerimg = buyerimg;
	}

	public int getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(int buyer_id) {
		this.buyer_id = buyer_id;
	}

	public Comments(int id, double grade, String comment, String time, int buyer_id, String nickname, String buyerimg) {
		super();
		this.id = id;
		this.grade = grade;
		this.comment = comment;
		this.time = time;
		this.buyer_id = buyer_id;
		this.nickname = nickname;
		this.buyerimg = buyerimg;
	}

	public Comments() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
