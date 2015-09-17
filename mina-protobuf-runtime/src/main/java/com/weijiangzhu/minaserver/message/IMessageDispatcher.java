package com.weijiangzhu.minaserver.message;

import java.util.Collection;

import org.apache.mina.core.session.IoSession;

public interface IMessageDispatcher {
	void sendMessage(IoSession session, Integer msgType, Object body);

	void sendMessage(IoSession session, Message message);

	void addSession(IoSession session);

	void removeSession(IoSession session);
	
	Collection<IoSession> getIoSessions();
}
