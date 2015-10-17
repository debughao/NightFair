
package com.nightfair.mobille.db;

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

}
