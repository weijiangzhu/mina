package com.weijiangzhu.minaserver.messageProcessor;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weijiangzhu.minaserver.entity.User;

public class UserProcessor extends MessageProcessor<User> {
	Logger log = LoggerFactory.getLogger(UserProcessor.class);

	@Override
	protected void processMessage(IoSession session, User user) {
		log.debug("UserProcessor:" + user);
	}
}
