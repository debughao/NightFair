package com.nightfair.mobille.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Segments implements Serializable{	private static final long serialVersionUID = 1457347042L;	private ArrayList<Segment> segment;
	public ArrayList<Segment> getSegment() {		return this.segment;	}
	public void setSegment(ArrayList<Segment> segment) {		this.segment = segment;	}
	public Segments() {}
	public Segments(ArrayList<Segment> segment){
		super();		this.segment = segment;
	}
	public String toString() {
		return "Segments [segment = " + segment + "]";	}
}