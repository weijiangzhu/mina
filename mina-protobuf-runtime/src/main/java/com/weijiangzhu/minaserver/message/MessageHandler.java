package com.weijiangzhu.minaserver.message;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weijiangzhu.minaserver.messageProcessor.IMessageProcessor;

public class MessageHandler extends IoHandlerAdapter {
	// 消息处理器缓存
	private Map<Integer, IMessageProcessor> messageProcessorCache;
	private IMessageDispatcher messageDispatcher;
	Logger log = LoggerFactory.getLogger(MessageHandler.class);

	public MessageHandler() {
		this.messageProcessorCache = new HashMap<Integer, IMessageProcessor>();
		this.messageDispatcher = new DefautMessageDispatcher();
	}

	public IMessageDispatcher getMessageDispatcher() {
		return this.messageDispatcher;
	}

	public void putMessageProcessor(Integer messageType, IMessageProcessor messageProcessor) {
		messageProcessorCache.put(messageType, messageProcessor);
	}

	public void sessionCreated(IoSession session) throws Exception {
		log.debug("session created");
	}

	public void sessionOpened(IoSession session) throws Exception {
		log.debug("session opened");
		messageDispatcher.addSession(session);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
		messageDispatcher.removeSession(session);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if (message instanceof Message) {
			Message msg = (Message) message;
			IMessageProcessor messageProcessor = messageProcessorCache.get(msg.getMsgType());
			messageProcessor.onMessage(session, msg.getBody());
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		messageDispatcher.removeSession(session);
	}
}
