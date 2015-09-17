package com.weijiangzhu.minaserver.messageProcessor;

import org.apache.mina.core.session.IoSession;

public abstract class MessageProcessor<TMessage> implements IMessageProcessor {

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(IoSession session, Object t) {
		processMessage(session, (TMessage) t);
	}

	protected abstract void processMessage(IoSession session, TMessage t);
}
