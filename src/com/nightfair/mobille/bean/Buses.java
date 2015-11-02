package com.nightfair.mobille.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Buses implements Serializable {
	private static final long serialVersionUID = 2012239484L;
	private ArrayList<Bus> bus;

	public ArrayList<Bus> getBus() {
		return this.bus;
	}

	public void setBus(ArrayList<Bus> bus) {
		this.bus = bus;
	}

	public Buses() {
	}

	public Buses(ArrayList<Bus> bus) {
		super();
		this.bus = bus;
	}

	public String toString() {
		return "Buses [bus = " + bus + "]";
	}
}