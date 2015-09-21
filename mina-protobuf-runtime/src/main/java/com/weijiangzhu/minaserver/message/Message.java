package com.weijiangzhu.minaserver.message;

public class Message {
	private Integer msgType;
	private Object body;

	public Message(Integer msgType, Object body) {
		this.msgType = msgType;
		this.body = body;
	}

	public Object getBody() {
		return body;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
}
