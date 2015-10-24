
package com.nightfair.mobille.db;

import com.nightfair.mobille.db.impl.BuyerDaoImpl;
import com.nightfair.mobille.db.impl.PushMessageDaoImpl;

import android.content.Context;

/**
 * 数据库工厂类
 * 
 * @author Frank
 * @date 2015年8月22日上午10:26:06
 */

public class DaoFactory {

	private static DaoFactory mInstance = null;

	/**
	 * 获取DaoFactory的实例
	 * 
	 * @return
	 */
	public static DaoFactory getInstance() {
		if (mInstance == null) {
			synchronized (DaoFactory.class) {
				if (mInstance == null) {
					mInstance = new DaoFactory();
				}
			}
		}
		return mInstance;
	}
	/**
	 * 获取买家数据库
	 * 
	 * @param context
	 * @return
	 */
	public BuyerDao getBuyerDao(Context context) {
		return new BuyerDaoImpl(context);
	}
	/**
	 * 获取推送消息数据库
	 * 
	 * @param context
	 * @return
	 */
	public PushMessageDao getPushMessageDao(Context context) {
		return new PushMessageDaoImpl(context);
	}

}
