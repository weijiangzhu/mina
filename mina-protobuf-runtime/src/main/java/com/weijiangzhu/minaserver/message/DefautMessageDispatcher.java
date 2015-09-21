package com.weijiangzhu.minaserver.message;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.weijiangzhu.minaserver.entity.User;
import com.weijiangzhu.minaserver.protobuf.GooglebufUtil;
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

	@SuppressWarnings("unchecked")
	@Override
	public <T> void sendMessage(IoSession session, Integer msgType, T t) {
		Class<T> cls = (Class<T>) t.getClass();
		byte[] bytes = GooglebufUtil.processEncode(t, cls);
		IoBuffer ioBuffer = IoBuffer.allocate(bytes.length + 8);
		ioBuffer.putInt(bytes.length);
		ioBuffer.putInt(msgType);
		ioBuffer.put(bytes);
		ioBuffer.flip();
		session.write(ioBuffer);
	}
}
