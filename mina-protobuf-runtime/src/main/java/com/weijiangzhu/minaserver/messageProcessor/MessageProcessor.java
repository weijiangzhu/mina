package com.weijiangzhu.minaserver.messageProcessor;

import java.lang.reflect.ParameterizedType;

import org.apache.mina.core.session.IoSession;

import com.weijiangzhu.minaserver.protobuf.GooglebufUtil;

public abstract class MessageProcessor<TMessage> implements IMessageProcessor {

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(IoSession session, Object obj) {
		Class<TMessage> cls = (Class<TMessage>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		processMessage(session, GooglebufUtil.processDecode((byte[]) obj, cls));
	}

	protected abstract void processMessage(IoSession session, TMessage t);
}
