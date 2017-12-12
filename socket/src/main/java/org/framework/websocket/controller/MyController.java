package org.framework.websocket.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.framework.websocket.handler.MyWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

@Controller
public class MyController {
	@Bean // 这个注解会从Spring容器拿出Bean
	public MyWebSocketHandler infoHandler() {
		return new MyWebSocketHandler();
	}

	@RequestMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		System.out.println(username + "登录");
		HttpSession session = request.getSession();
		session.setAttribute("SESSION_USERNAME", username);
		return "websocket";
	}

	@RequestMapping("send")
	@ResponseBody
	public String sendYou(String sendYou, String info) {
		infoHandler().sendMessageToUser(sendYou, new TextMessage(info));
		return "OK";
	}
}
