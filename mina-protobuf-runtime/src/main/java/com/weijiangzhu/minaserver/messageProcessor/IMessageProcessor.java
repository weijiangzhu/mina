package com.weijiangzhu.minaserver.messageProcessor;

import org.apache.mina.core.session.IoSession;

public interface IMessageProcessor {
	void onMessage(IoSession session, Object obj);
}
