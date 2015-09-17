package com.weijiangzhu.minaserver.message;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

import com.weijiangzhu.minaserver.util.IPUtil;

public class DefautMessageDispatcher implements IMessageDispatcher {
	private Map<String, IoSession> sessionCache;

	public DefautMessageDispatcher() {
		sessionCache = new ConcurrentHashMap<String, IoSession>();
	}

	public void addSession(IoSession session) {
		String key = IPUtil.getKey(session);
		this.sessionCache.put(key, session);
	}

	public void removeSession(IoSession session) {
		String key = IPUtil.getKey(session);
		this.sessionCache.remove(key);
	}

	public IoSession getSession(String key) {
		return sessionCache.get(key);
	}

	public Collection<IoSession> getIoSessions() {
		return sessionCache.values();
	}

	@Override
	public void sendMessage(IoSession session, Integer msgType, Object body) {
		sendMessage(session, new Message(msgType, body));
	}

	@Override
	public void sendMessage(IoSession session, Message message) {
		session.write(message);
	}
}
