package com.nightfair.mobille.bean;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
@Table(name = "PushMessage") 
public class PushMessage extends BaseEntity implements Serializable {

	/**
	 * 系统消息实体类
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 标题
	 */
	@Column(column = "title")
    private String title;
	/**
	 * 时间
	 */
	@Column(column = "datetime")
    private String datetime;
    /*
     * 描述
     */
	@Column(column = "description")
	 private String description;
	/**
	 * 内容
	 */
	@Column(column = "content")
	 private String content;
	/**
	 * url
	 */
	@Column(column = "url")
	 private String url;
	 /**
	  * 商家 seller_id
	  */
	@Column(column = "seller_id")
	 private String seller_id;
	/*
	 * 推送消息
	 */
	@Column(column = "isRecommand")	
	private int isRecommand;
	/*
	 * 系统消息
	 */
	@Column(column = "isSystem")
	private int isSystem;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	public int isRecommand() {
		return isRecommand;
	}
	public void setRecommand(int isRecommand) {
		this.isRecommand = isRecommand;
	}
	public int isSystem() {
		return isSystem;
	}
	public void setSystem(int isSystem) {
		this.isSystem = isSystem;
	}
	public PushMessage(String title, String datetime, String description, String content, String url, String seller_id,
			int isRecommand, int isSystem) {
		super();
		this.title = title;
		this.datetime = datetime;
		this.description = description;
		this.content = content;
		this.url = url;
		this.seller_id = seller_id;
		this.isRecommand = isRecommand;
		this.isSystem = isSystem;
	}
	public PushMessage() {
		super();
		
	}
	@Override
	public String toString() {
		return "PushMessage [title=" + title + ", datetime=" + datetime + ", description=" + description + ", content="
				+ content + ", url=" + url + ", seller_id=" + seller_id + ", isRecommand=" + isRecommand + ", isSystem="
				+ isSystem + "]";
	}
	
	
	
}
