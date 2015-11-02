package com.nightfair.mobille.bean;

import java.io.Serializable;

public class Bus implements Serializable{	private static final long serialVersionUID = 2012239484L;	private String time;	private String dist;	private Segments segments;	private String last_foot_dist;	private String foot_dist;
	public String getTime() {		return this.time;	}
	public void setTime(String time) {		this.time = time;	}
	public String getDist() {		return this.dist;	}
	public void setDist(String dist) {		this.dist = dist;	}
	public Segments getSegments() {		return this.segments;	}
	public void setSegments(Segments segments) {		this.segments = segments;	}
	public String getLast_foot_dist() {		return this.last_foot_dist;	}
	public void setLast_foot_dist(String last_foot_dist) {		this.last_foot_dist = last_foot_dist;	}
	public String getFoot_dist() {		return this.foot_dist;	}
	public void setFoot_dist(String foot_dist) {		this.foot_dist = foot_dist;	}
	public Bus() {}
	public Bus(String time, String dist, Segments segments, String last_foot_dist, String foot_dist){
		super();		this.time = time;		this.dist = dist;		this.segments = segments;		this.last_foot_dist = last_foot_dist;		this.foot_dist = foot_dist;
	}
	public String toString() {
		return "Bus [time = " + time + ", dist = " + dist + ", segments = " + segments + ", last_foot_dist = " + last_foot_dist + ", foot_dist = " + foot_dist + "]";	}
}