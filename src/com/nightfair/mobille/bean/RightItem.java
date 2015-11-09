package com.nightfair.mobille.bean;

import java.io.Serializable;

public class RightItem implements Serializable {

	private static final long serialVersionUID = 1L;
    private String text;
	public RightItem(String text) {
		super();
		this.text = text;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
    
}
