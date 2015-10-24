package com.nightfair.mobille.db;

import java.util.List;

import com.nightfair.mobille.bean.PushMessage;

public interface PushMessageDao {
	public abstract void insertPushMessage(PushMessage message);
	public abstract List<PushMessage> queryAllRecommendMessage();
	public abstract List<PushMessage> queryAllSystemMessage();
	public abstract void delete(PushMessage PushMessage);
	public abstract void deleteAll(List<PushMessage> list);
	public abstract void deleteAll();
}
