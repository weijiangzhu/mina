package com.weijiangzhu.minaserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.weijiangzhu.minaserver.entity.Car;
import com.weijiangzhu.minaserver.entity.User;
import com.weijiangzhu.minaserver.message.Message;
import com.weijiangzhu.minaserver.message.MessageHandler;
import com.weijiangzhu.minaserver.message.MessageType;
import com.weijiangzhu.minaserver.messageProcessor.UserProcessor;
import com.weijiangzhu.minaserver.protobuf.ProtobufDecoder;
import com.weijiangzhu.minaserver.protobuf.ProtobufEncoder;

public class Server {
	public static void main(String[] args) throws IOException, InterruptedException {
		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new ProtobufEncoder(), new ProtobufDecoder()));
		MessageHandler messageHandler = new MessageHandler();
		messageHandler.putMessageProcessor(MessageType.USERINFO, new UserProcessor());
		acceptor.setHandler(messageHandler);
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		acceptor.bind(new InetSocketAddress(9623));

		List<Car> cars = new ArrayList<Car>();
		cars.add(new Car("K5"));
		cars.add(new Car("k3"));
		while (true) {
			Collection<IoSession> sessions = messageHandler.getMessageDispatcher().getIoSessions();
			if (sessions.size() == 0) {
				Thread.sleep(3000);
			} else {
				for (IoSession ioSession : sessions) {
					ioSession.write(new Message(1000, new User(1, cars)));
					continue;
				}
				break;
			}
		}
	}
}
