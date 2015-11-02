package com.nightfair.mobille.bean;

import java.io.Serializable;

public class SegParm implements Serializable {
	/**
	 * 方案参数
	 */
	private static final long serialVersionUID = 1L;
	private String city;
	private Float startLongitude;
	private Float startLatLng;
	private Float EndLongitude;
	private Float EndLatitude;
	private int rc;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Float getStartLongitude() {
		return startLongitude;
	}

	public void setStartLongitude(Float startLongitude) {
		this.startLongitude = startLongitude;
	}

	public Float getStartLatLng() {
		return startLatLng;
	}

	public void setStartLatLng(Float startLatLng) {
		this.startLatLng = startLatLng;
	}

	public Float getEndLongitude() {
		return EndLongitude;
	}

	public void setEndLongitude(Float endLongitude) {
		EndLongitude = endLongitude;
	}

	public Float getEndLatitude() {
		return EndLatitude;
	}

	public void setEndLatitude(Float endLatitude) {
		EndLatitude = endLatitude;
	}

	public int getRc() {
		return rc;
	}

	public void setRc(int rc) {
		this.rc = rc;
	}

	public SegParm() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SegParm(String city, Float startLongitude, Float startLatLng, Float endLongitude, Float endLatitude,
			int rc) {
		super();
		this.city = city;
		this.startLongitude = startLongitude;
		this.startLatLng = startLatLng;
		EndLongitude = endLongitude;
		EndLatitude = endLatitude;
		this.rc = rc;
	}

	@Override
	public String toString() {
		return "SegParm [city=" + city + ", startLongitude=" + startLongitude + ", startLatLng=" + startLatLng
				+ ", EndLongitude=" + EndLongitude + ", EndLatitude=" + EndLatitude + ", rc=" + rc + "]";
	}

}
