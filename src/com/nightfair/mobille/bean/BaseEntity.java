package com.nightfair.mobille.bean;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;

/**
 * 
 * @ClassName: BaseEntity
 * @Description: TODO(实体类--基类)
 * @author debughao
 * @date 2015年10月16日
 */
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 4995176180527325406L;

	@Column(column = "id")
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
