package com.nightfair.mobille.bean;

import java.util.List;

public class Province {
	private String name;
	private List<Citys> cityList;

	public Province() {
		super();
	}

	public Province(String name, List<Citys> cityList) {
		super();
		this.name = name;
		this.cityList = cityList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Citys> getCityList() {
		return cityList;
	}

	public void setCityList(List<Citys> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
	}

}
