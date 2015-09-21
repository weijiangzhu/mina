package com.weijiangzhu.minaserver;

import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weijiangzhu.minaserver.message.MessageHandler;
import com.weijiangzhu.minaserver.message.MessageType;
import com.weijiangzhu.minaserver.messageProcessor.UserProcessor;

public class Client {
	public static void main(String[] args) {
		Logger log = LoggerFactory.getLogger(Client.class);
		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(3000);
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		MessageHandler messageHandler = new MessageHandler();
		messageHandler.putMessageProcessor(MessageType.USERINFO, new UserProcessor());
		connector.setHandler(messageHandler);
		IoSession session;
		for (;;) {
			try {
				ConnectFuture future = connector.connect(new InetSocketAddress("127.0.0.1", 9623));
				future.awaitUninterruptibly();
				session = future.getSession();
				break;
			} catch (RuntimeIoException e) {
				e.printStackTrace();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// wait until the summation is done
		session.getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}
}
