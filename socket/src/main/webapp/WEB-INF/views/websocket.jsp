<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Java API for WebSocket (JSR-356)</title>
</head>
<body>
	<script type="text/javascript"
		src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>
	<script type="text/javascript"
		src="http://cdn.bootcss.com/sockjs-client/1.1.1/sockjs.js"></script>
	<script type="text/javascript">
		var websocket = null;
		if ('WebSocket' in window) {
			//Websocket的连接  
			websocket = new WebSocket(
					"ws://192.168.16.198:8088/socket/websocket/socketServer.do");//WebSocket对应的地址  
		} else if ('MozWebSocket' in window) {
			//Websocket的连接  
			websocket = new MozWebSocket(
					"ws://192.168.16.198:8088/socket/websocket/socketServer.do");//SockJS对应的地址  
		} else {
			//SockJS的连接  
			websocket = new SockJS(
					"http://192.168.16.198:8088/websocket/socketServer.do"); //SockJS对应的地址  
		}
		websocket.onopen = onOpen;
		websocket.onmessage = onMessage;
		websocket.onerror = onError;
		websocket.onclose = onClose;
		function onOpen(openEvt) {
			//alert(openEvt.Data);
		}
		function onMessage(evt) {
			try {
				//console.log(eval("(" + evt.data + ")"));
				//document.getElementById('img').src = 'data:image/jpg;'+ evt.data;
				var reader = new FileReader();
				reader.onload = function(evt) {
					if (evt.target.readyState == FileReader.DONE) {
						var url = evt.target.result;
						var img = document.getElementById("img");
						img.innerHTML = "<img src = "+url+" />";
					}
				}
				reader.readAsDataURL(evt.data);
			} catch (e) {
				alert(evt.data);
			}
		}
		function onError() {
		}
		function onClose() {
		}
		function doSend() {
			if (websocket.readyState == websocket.OPEN) {
				var msg = document.getElementById("inputMsg").value;
				websocket.send(msg);//调用后台handleTextMessage方法  
				alert("发送成功!");
			} else {
				alert("连接失败!");
			}
			/* $.ajax({
				url : "send",
				type : "POST",
				data : {
					sendYou : "89",
					info : document.getElementById("inputMsg").value
				},
				success : function(result) {
					alert(result)
				}
			}) */
		}
		window.close = function() {
			websocket.onclose();
		}
	</script>
	请输入：
	<textarea rows="3" cols="100" id="inputMsg" name="inputMsg"></textarea>
	<button onclick="doSend();">发送</button>
	<div id="img"></div>
</body>
</html>
