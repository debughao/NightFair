package com.nightfair.mobille.bean;

import java.util.ArrayList;

public class SellerCommentsBuyer {
	private int id;
	private double  rank;
	private ArrayList<Comments> comments;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getRank() {
		return rank;
	}
	public void setRank(double rank) {
		this.rank = rank;
	}
	public ArrayList<Comments> getComments() {
		return comments;
	}
	public void setComments(ArrayList<Comments> comments) {
		this.comments = comments;
	}
	public SellerCommentsBuyer(int id, double rank, ArrayList<Comments> comments) {
		super();
		this.id = id;
		this.rank = rank;
		this.comments = comments;
	}
	public SellerCommentsBuyer() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "SellerCommentsBuyer [id=" + id + ", rank=" + rank + ", comments=" + comments + "]";
	}
	
}
