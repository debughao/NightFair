package com.nightfair.mobille.db.impl;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.nightfair.mobille.bean.Buyer;
import com.nightfair.mobille.bean.BuyerInfo;
import com.nightfair.mobille.db.BuyerDao;

import android.content.Context;
import android.util.Log;

public class BuyerDaoImpl implements BuyerDao {
	private DbUtils db;
	private Context context;

	public BuyerDaoImpl(Context context) {
		super();
		this.context = context;
		init();

	}

	private void init() {
		Buyer buyer = new Buyer();
		BuyerInfo buyerInfo = new BuyerInfo();
		buyerInfo.buyer = buyer;
		this.db = DbUtils.create(context, "nightfari.db");
		db.configAllowTransaction(true);
		db.configDebug(true);
		try {
			db.createTableIfNotExist(Buyer.class);
			db.createTableIfNotExist(BuyerInfo.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * try { db.saveBindingId(buyer);
		 * db.saveBindingId(buyerInfo);//保存对象关联数据库生成的id\
		 * 
		 * } catch (DbException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

	}

	@Override
	public void insertBuyer(Buyer buyer) {

		try {
			Buyer findItem = db.findFirst(Selector.from(Buyer.class).where("id", "=", buyer.getId()));
			if (findItem != null) {
				db.update(buyer, WhereBuilder.b("id", "=", buyer.getId()));
			} else {
				db.save(buyer);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void insertInfo(BuyerInfo buyerInfo, int userid) {
		try {
			BuyerInfo findItem = db.findFirst(Selector.from(BuyerInfo.class).where("user_id", "=", userid));
			if (findItem != null) {
				db.update(buyerInfo, WhereBuilder.b("user_id", "=", userid));
			} else {
				db.save(buyerInfo);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int queryLoginUserid() {
		int userid = 0;
		try {
			Buyer findItem = db.findFirst(Selector.from(Buyer.class).where("islogin", "=", 1));
			if (findItem != null) {
				userid = findItem.getId();
				Log.i("queryLoginUserid", "userid" + findItem.getId());
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return userid;

	}

	@Override
	public BuyerInfo queryinfo(int userid) {
		BuyerInfo findItem = null;
		try {
			findItem = db.findFirst(Selector.from(BuyerInfo.class).where("user_id", "=", userid));
			Log.i("queryinfo", "the queryinfo result is：" + findItem);
		} catch (DbException e) {
			e.printStackTrace();
		}
		return findItem;
	}

	@Override
	public void logout(int userid) {
		try {
			Buyer findItem = db.findFirst(Selector.from(Buyer.class).where("islogin", "=", 1));
			findItem.setLogin(false);
			db.update(findItem);
		} catch (DbException e) {
			e.printStackTrace();
		}
		
	}
  
}
