package com.weijiangzhu.minaserver.protobuf;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.weijiangzhu.minaserver.message.Message;

public class ProtobufEncoder extends ProtocolEncoderAdapter {

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		byte[] bytes = GooglebufUtil.processEncode((Message) message, Message.class);
		IoBuffer buffer = IoBuffer.allocate(bytes.length + 4, false);
		buffer.putInt(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		out.write(buffer);
	}

}
