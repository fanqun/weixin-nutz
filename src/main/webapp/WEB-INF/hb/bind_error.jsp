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
			<i class="weui_icon_safe weui_icon_safe_warn"></i>
		</div>
		<div class="weui_text_area">
			<h2 class="weui_msg_title">提示信息</h2>

			<p class="weui_msg_desc">绑定HeartBeat账号失败: <%=request.getAttribute("errmsg") %></p>
		</div>
		
<!-- 		<div class="weui_opr_area">
			<p class="weui_btn_area weui_msg_desc">
				<a href="javascript:window.close();"
					class="weui_btn weui_btn_default">关闭</a>
			</p>
		</div>
 -->		<div class="weui_extra_area">
			<p>杭州紫恒科技有限公司  HeartBeat</p>
		</div>
	</div>
</body>
</html>
