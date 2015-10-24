package com.nightfair.mobille.db.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.nightfair.mobille.bean.PushMessage;
import com.nightfair.mobille.config.FilePathConfig;
import com.nightfair.mobille.db.PushMessageDao;
import com.nightfair.mobille.util.FileUtils;

import android.content.Context;

public class PushMessageDaoImpl implements PushMessageDao {
	private DbUtils db;
	private Context context;
	public PushMessageDaoImpl(Context context) {
		super();
		this.context = context;
		init();

	}
	private void init() {
		this.db = DbUtils.create(context, FilePathConfig.getPushMessageDbPath(context),"pushMessage_recommand.db");
		db.configAllowTransaction(true);
		db.configDebug(true);
		try {
			db.createTableIfNotExist(PushMessage.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void insertPushMessage(PushMessage message) {
		try {
			PushMessage findItem = db.findFirst(Selector.from(PushMessage.class).where("title", "=", message.getTitle()));
			if (findItem != null) {
				db.update(message, WhereBuilder.b("title", "=", message.getTitle()));
			} else {
				db.save(message);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<PushMessage> queryAllRecommendMessage() {
		List<PushMessage> pushMessages = new  ArrayList<PushMessage>();
		try {
			pushMessages=db.findAll(Selector.from(PushMessage.class).where("isRecommand", "=", 1));
		} catch (DbException e) {
		
			e.printStackTrace();
		}
		return pushMessages;
	}
	@Override
	public List<PushMessage> queryAllSystemMessage() {
		List<PushMessage> pushMessages = new  ArrayList<PushMessage>();
		try {
			pushMessages=db.findAll(Selector.from(PushMessage.class).where("isSystem", "=", 1));
		} catch (DbException e) {
		
			e.printStackTrace();
		}
		return pushMessages;
	}

	public void delete(PushMessage PushMessage) {
		try {
			db.delete(PushMessage);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public void deleteAll(List<PushMessage> list) {
		try {
			db.delete(list);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void deleteAll() {
		// 删除数据库文件
				File file = new File(FilePathConfig.getPushMessageDbPath(context) + File.separator + "pushMessage_recommand");
				FileUtils.delete(file);		
	}

}
