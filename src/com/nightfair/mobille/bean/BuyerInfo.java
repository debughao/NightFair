package com.nightfair.mobille.bean;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;

public class BuyerInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 买家用户id
	 */
	@Column(column = "user_id")
	private String user_id;
	/**
	 * 买家昵称
	 */
	@Column(column = "nickname")
	private String nickname;
	/**
	 * 买家性别
	 */
	@Column(column = "sex")
	private String sex;
	/**
	 * 买家年龄
	 */
	@Column(column = "age")

	private String age;
	/**
	 * 买家星座
	 */
	@Column(column = "star")

	private String star;
	/**
	 * 买家故乡
	 */
	@Column(column = "hometown")
	private String hometown;
	/**
	 * 买家所在地
	 */
	@Column(column = "address")
	private String address;
	/**
	 * 买家签名
	 */
	@Column(column = "autograph")
	private String autograph;
	/**
	 * 买家头像
	 */
	@Column(column = "image")
	private String image;

	public BuyerInfo() {

	}

	@Override
	public String toString() {
		return "BuyerInfo [user_id=" + user_id + ", nickname=" + nickname + ", sex=" + sex + ", age=" + age + ", star="
				+ star + ", hometown=" + hometown + ", address=" + address + ", autograph=" + autograph + ", image="
				+ image + "]";
	}

	public BuyerInfo(String user_id, String nickname, String sex, String age, String star, String hometown,
			String address, String autograph, String image) {
		super();
		this.user_id = user_id;
		this.nickname = nickname;
		this.sex = sex;
		this.age = age;
		this.star = star;
		this.hometown = hometown;
		this.address = address;
		this.autograph = autograph;
		this.image = image;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAutograph() {
		return autograph;
	}

	public void setAutograph(String autograph) {
		this.autograph = autograph;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
