package com.nightfair.mobille.bean;

import java.io.Serializable;
import java.util.List;


public class LeftItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private int img;
	private String text;
	private List<RightItem> list;
	
	public LeftItem(int img, String text, List<RightItem> list) {
		super();
		this.img = img;
		this.text = text;
		this.list = list;
	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<RightItem> getList() {
		return list;
	}

	public void setList(List<RightItem> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "LeftItem [img=" + img + ", text=" + text + ", list=" + list
				+ "]";
	}
	
	
	
}
