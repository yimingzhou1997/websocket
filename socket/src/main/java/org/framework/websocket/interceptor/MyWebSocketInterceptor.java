package org.framework.websocket.interceptor;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

public class MyWebSocketInterceptor implements HandshakeInterceptor {
	private final static Logger LOGGER = LoggerFactory.getLogger(MyWebSocketInterceptor.class);

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse serverHttpResponse,
			WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception {
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpSession session = servletRequest.getServletRequest().getSession(false);
			if (session != null) {
				// 使用userName区分WebSocketHandler，以便定向发送消息
				String userName = (String) session.getAttribute("SESSION_USERNAME");
				if (userName == null) {
					userName = "system-" + session.getId();
				}
				attributes.put("WEBSOCKET_USERNAME", userName);
			}
		}
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {
		System.out.println("After Handshake");
	}

}
class MyInterceptor extends HttpSessionHandshakeInterceptor{
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {
	}
}
