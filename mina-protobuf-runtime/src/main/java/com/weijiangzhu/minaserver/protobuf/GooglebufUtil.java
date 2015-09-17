package com.weijiangzhu.minaserver.protobuf;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.DefaultIdStrategy;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

public class GooglebufUtil {

	private static final int bufferSize = 2048;

	private static final ThreadLocal<LinkedBuffer> localBuffer = new ThreadLocal<LinkedBuffer>() {
		public LinkedBuffer initialValue() {
			return LinkedBuffer.allocate(bufferSize);
		}
	};

	private static LinkedBuffer getApplicationBuffer() {
		return localBuffer.get();
	}

	public static <T> byte[] processEncode(T t, Class<T> clazz) {
		Schema<T> schema = RuntimeSchema.getSchema(clazz);
		LinkedBuffer buffer = getApplicationBuffer();
		byte[] protobuf = null;
		try {
			protobuf = ProtobufIOUtil.toByteArray(t, schema, buffer);
		} finally {
			buffer.clear();
		}
		return protobuf;
	}

	public static synchronized <T> T processDecode(byte[] googlebuf, Class<T> clazz) {
		Schema<T> schema = RuntimeSchema.createFrom(clazz, new DefaultIdStrategy());
		T t = schema.newMessage();
		ProtobufIOUtil.mergeFrom(googlebuf, t, schema);
		return t;
	}
}
