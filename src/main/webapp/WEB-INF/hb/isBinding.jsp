<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>


<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,user-scalable=0">
<link rel="stylesheet" href="../../wx/static/weui/weui.min.css" />
<link rel="stylesheet" href="../../wx/static/weui/style.css" />

<title>绑定HeartBeat账号</title>
</head>
<body ontouchstart>


	<div class="weui_msg">
		<div class="weui_icon_area">
			<i class="weui_icon_safe weui_icon_info"></i>
		</div>
		<div class="weui_text_area">
			<h2 class="weui_msg_title">绑定信息</h2>

			<p class="weui_msg_desc">您的微信号已经绑定了HeartBeat账号: <%=request.getAttribute("hbUsername") %></p>
		</div>
<!-- 		<div class="weui_opr_area">
			<p class="weui_btn_area weui_msg_desc">
				<a href="javascript:window.close();"
					class="weui_btn weui_btn_default">关闭</a>
			</p>
		</div>
 -->		<div class="weui_extra_area">
			<p>HeartBeat</p>
		</div>
	</div>
</body>
</html>
