package com.weijiangzhu.minaserver.protobuf;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.weijiangzhu.minaserver.message.Message;

public class ProtobufDecoder implements ProtocolDecoder {

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		int lenth = in.getInt();
		byte[] bytes = new byte[lenth];
		in.get(bytes);
		Message message = GooglebufUtil.processDecode(bytes, Message.class);
		out.write(message);
	}

	@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {

	}

	@Override
	public void dispose(IoSession session) throws Exception {
	}

}
