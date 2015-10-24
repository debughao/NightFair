package com.nightfair.mobille.bean;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Finder;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
/*
 * @ClassName: Buyer
 * @Description: TODO(用户表)
 * @author debughao
 * @date 2015年10月18日
 */

@Table(name = "buyer", execAfterTableCreated = "CREATE UNIQUE INDEX index_name ON buyer(username,password)") // 建议加上注解，
																												// 混淆后表名不受影响
public class Buyer implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@NoAutoIncrement
	@Column(column = "id")
	private int id;
	/*
	 * 用户登录名
	 */
	@Column(column = "username")
	private String username;
	/*
	 * 用户登录密码
	 */
	@Column(column = "password")
	private String password;
	/*
	 * 用户登录是否登录
	 */
	@Column(column = "isLogin")
	private boolean isLogin;
	@Finder(valueColumn = "id", targetColumn = "user_id")
	public BuyerInfo buyerInfo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswor() {
		return password;
	}

	public void setPasswor(String password) {
		this.password = password;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public Buyer(int id, String username, String password, boolean isLogin) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.isLogin = isLogin;
	}

	public Buyer() {

	}

}
