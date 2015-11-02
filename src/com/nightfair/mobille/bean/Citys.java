package com.nightfair.mobille.bean;

import java.util.List;

public class Citys {
	private String name;
	private List<District> districtList;
	
	public Citys() {
		super();
	}

	public Citys(String name, List<District> districtList) {
		super();
		this.name = name;
		this.districtList = districtList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<District> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<District> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", districtList=" + districtList
				+ "]";
	}
	
}
