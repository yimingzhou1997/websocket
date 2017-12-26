package org.framework.websocket.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.framework.websocket.bean.Student;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {
	private final static Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);

	// 已建立连接的用户
	private static final ArrayList<WebSocketSession> users = new ArrayList<WebSocketSession>();

	/**
	 * 处理前端发送的文本信息 js调用websocket.send时候，会调用该方法
	 * 
	 * @param session
	 * @param message
	 * @throws Exception
	 */
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
		// 获取提交过来的消息详情
		LOGGER.debug("收到用户 " + username + "的消息:" + message.toString());
		// 回复一条信息，
		// session.sendMessage(new TextMessage("reply msg:" +
		// message.getPayload()));
		/*
		 * Student s = new Student(); s.setId(1); s.setName("离夏"); Map<String,
		 * Object> map = new HashMap<>(); map.put("v1", s); map.put("v2", s);
		 * map.put("v3", s); List<Student> list = new ArrayList<>();
		 * list.add(s); list.add(s); list.add(s); map.put("list", list);
		 * map.put("page", 1); map.put("size", "caosinima");
		 * session.sendMessage(new TextMessage(new JSONObject(map).toString()));
		 */
		String[] array = new String[] {
				"C:\\Users\\Administrator.JSJAEPVG320SZD7\\Desktop\\20171210083321-ac345982b2b7d0a2d4b4a0d6ccef76094b.png",
				"V:\\tup\\500d1304cde84.jpg" };
		String path = array[new Random().nextInt(2)];
		File file = new File(path);
		byte[] data = new byte[(int) file.length()];
		InputStream is = new FileInputStream(file);
		is.read(data);
		System.out.println("图片长度：" + data.length);
		sendMessageToUsers(new BinaryMessage(data));
	}

	/**
	 * 当新连接建立的时候，被调用 连接成功时候，会触发页面上onOpen方法
	 * 
	 * @param session
	 * @throws Exception
	 */
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		users.add(session);
		String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
		LOGGER.info("用户 " + username + " Connection Established");
		session.sendMessage(new TextMessage(username + " connect"));
		session.sendMessage(new TextMessage("hello wellcome"));
	}

	/**
	 * 当连接关闭时被调用
	 * 
	 * @param session
	 * @param status
	 * @throws Exception
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
		LOGGER.info("用户 " + username + " Connection closed. Status: " + status);
		users.remove(session);
	}

	/**
	 * 传输错误时调用
	 * 
	 * @param session
	 * @param exception
	 * @throws Exception
	 */
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
		if (session.isOpen()) {
			session.close();
		}
		LOGGER.debug("用户: " + username + " websocket connection closed......");
		users.remove(session);
	}

	/**单独开启一个线程
	 * 给所有在线用户发送消息
	 * 
	 * @param message
	 */
	public void sendMessageToUsers(final WebSocketMessage<?> message) {
		new Thread(new Runnable() {
			public void run() {
				System.out.println("我的一个道姑朋友");
				for (WebSocketSession user : users) {
					try {
						if (user.isOpen()) {
							user.sendMessage(message);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * 给某个用户发送消息
	 * 
	 * @param userName
	 * @param message
	 */
	public void sendMessageToUser(String userName, TextMessage message) {
		for (WebSocketSession user : users) {
			if (user.getAttributes().get("WEBSOCKET_USERNAME").equals(userName)) {
				try {
					if (user.isOpen()) {
						System.out.println(1111);
						user.sendMessage(message);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}
}
