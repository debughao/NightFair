package com.nightfair.mobille.db;

import com.nightfair.mobille.bean.Buyer;
import com.nightfair.mobille.bean.BuyerInfo;

public interface BuyerDao {
	/**
	 * 用户登录
	 * 
	 * @param buyerInfo
	 */
	public abstract void insertBuyer(Buyer buyer);

	/**
	 * 初始化个人资料
	 * 
	 * @param buyerInfo
	 */
	public abstract void insertInfo(BuyerInfo buyerInfo, int userid);

	/**
	 * 查询当前登录用户的用户id
	 * 
	 * @param user_id
	 */

	public abstract int queryLoginUserid();
	/**
	 * 查询当前登录用户的基本资料
	 * 
	 * @param user_id
	 */

	public abstract BuyerInfo queryinfo(int userid);
	/**
	 * 当前登录用户退出登录
	 * 
	 * @param user_id
	 */
    public  abstract void logout(int useid);
}
