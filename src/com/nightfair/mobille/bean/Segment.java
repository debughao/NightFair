package com.nightfair.mobille.bean;

import java.io.Serializable;

public class Segment implements Serializable{	private static final long serialVersionUID = 685928974L;	private String line_dist;	private String foot_dist;	private String stat_xys;	private String stats;	private String start_stat;	private String end_stat;	private String line_name;	private String line_xys;
	public String getLine_dist() {		return this.line_dist;	}
	public void setLine_dist(String line_dist) {		this.line_dist = line_dist;	}
	public String getFoot_dist() {		return this.foot_dist;	}
	public void setFoot_dist(String foot_dist) {		this.foot_dist = foot_dist;	}
	public String getStat_xys() {		return this.stat_xys;	}
	public void setStat_xys(String stat_xys) {		this.stat_xys = stat_xys;	}
	public String getStats() {		return this.stats;	}
	public void setStats(String stats) {		this.stats = stats;	}
	public String getStart_stat() {		return this.start_stat;	}
	public void setStart_stat(String start_stat) {		this.start_stat = start_stat;	}
	public String getEnd_stat() {		return this.end_stat;	}
	public void setEnd_stat(String end_stat) {		this.end_stat = end_stat;	}
	public String getLine_name() {		return this.line_name;	}
	public void setLine_name(String line_name) {		this.line_name = line_name;	}
	public String getLine_xys() {		return this.line_xys;	}
	public void setLine_xys(String line_xys) {		this.line_xys = line_xys;	}
	public Segment() {}
	public Segment(String line_dist, String foot_dist, String stat_xys, String stats, String start_stat, String end_stat, String line_name, String line_xys){
		super();		this.line_dist = line_dist;		this.foot_dist = foot_dist;		this.stat_xys = stat_xys;		this.stats = stats;		this.start_stat = start_stat;		this.end_stat = end_stat;		this.line_name = line_name;		this.line_xys = line_xys;
	}
	public String toString() {
		return "Segment [line_dist = " + line_dist + ", foot_dist = " + foot_dist + ", stat_xys = " + stat_xys + ", stats = " + stats + ", start_stat = " + start_stat + ", end_stat = " + end_stat + ", line_name = " + line_name + ", line_xys = " + line_xys + "]";	}
}