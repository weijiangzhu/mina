package com.weijiangzhu.minaserver.message;

public class Message {
	private Integer msgType;
	private Object body;

	public Message(Integer msgType, Object obj) {
		this.msgType = msgType;
		this.body = obj;
	}

	public Message(Object obj) {
		this.body = obj;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public Object getBody() {
		return body;
	}
}
