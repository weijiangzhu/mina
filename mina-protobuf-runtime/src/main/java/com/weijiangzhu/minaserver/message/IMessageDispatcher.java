package com.weijiangzhu.minaserver.message;

import java.util.Collection;

import org.apache.mina.core.session.IoSession;

public interface IMessageDispatcher {
	<T> void sendMessage(IoSession session, Integer msgType, T t);

	void addSession(IoSession session);

	void removeSession(IoSession session);

	Collection<IoSession> getIoSessions();
}
