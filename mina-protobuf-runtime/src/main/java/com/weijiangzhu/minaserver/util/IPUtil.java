package com.weijiangzhu.minaserver.util;

import java.net.InetSocketAddress;

import org.apache.mina.core.session.IoSession;

public class IPUtil {

	public static String getKey(String ip, int port) {
		StringBuilder sb = new StringBuilder(128);
		sb.append(ip);
		sb.append(":");
		sb.append(port);
		return sb.toString();
	}

	public static String getKey(IoSession session) {
		InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
		return getKey(address.getAddress().getHostAddress(), address.getPort());
	}

}