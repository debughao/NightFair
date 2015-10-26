package com.nightfair.mobille.bean;

import java.io.Serializable;


public class Goods implements Serializable {

	/**
	 * 字段类型全部写为String，方便解析
	 */
	private static final long serialVersionUID = 1L;
	
	private String img;//商品图片
	private String good_name;//商品名称
	private String distance;//商店距离
	private String introduction;//商品介绍
	private String real_price;//商品价格
	private String seller_counts;//已售商品数量
	
	public Goods() {
		super();
	}
	
	public Goods(String img, String good_name, String distance,
			String introduction, String real_price, String seller_counts) {
		super();
		this.img = img;
		this.good_name = good_name;
		this.distance = distance;
		this.introduction = introduction;
		this.real_price = real_price;
		this.seller_counts = seller_counts;
	}

	
	
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getGood_name() {
		return good_name;
	}

	public void setGood_name(String good_name) {
		this.good_name = good_name;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getReal_price() {
		return real_price;
	}

	public void setReal_price(String real_price) {
		this.real_price = real_price;
	}

	public String getSeller_counts() {
		return seller_counts;
	}

	public void setSeller_counts(String seller_counts) {
		this.seller_counts = seller_counts;
	}

	@Override
	public String toString() {
		return "Goods [img=" + img + ", good_name=" + good_name + ", distance="
				+ distance + ", introduction=" + introduction + ", real_price="
				+ real_price + ", seller_counts=" + seller_counts + "]";
	}

	
	
}
