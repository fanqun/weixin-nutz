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
<body>
	<form id="formDto" action="../../hb/login/validate" method="post">
		<div class="weui_cells_title">请输入您登录HeartBeat的账号密码进行绑定</div>
		<div class="weui_cells weui_cells_form">
			<div class="weui_cell">
				<div class="weui_cell_hd">
					<label class="weui_label">HB账号</label>
				</div>
				<div class="weui_cell_bd weui_cell_primary">
					<input id="hbUsername" name="hbUsername" class="weui_input"
						placeholder="HB账号" required type="text" value="" />
				</div>
			</div>
			<div class="weui_cell">
				<div class="weui_cell_hd">
					<label class="weui_label">HB密码</label>
				</div>
				<div class="weui_cell_bd weui_cell_primary">
					<input id="password" name="password" class="weui_input"
						placeholder="HB密码" required type="password" value="" />
				</div>
			</div>
		</div>
		<div class="weui_cells_tips">
			没有HB账号,请在PC上访问&nbsp;<a href="http://demo.iegreen.net/hb/login.hb">HeartBeat</a>&nbsp;进行注册
		</div>
		<input type="hidden" name="openid"
			value="<%=session.getAttribute("openid")%>" />
		<div class="weui_btn_area">
			<button class="weui_btn weui_btn_primary" type="submit">提交绑定</button>
		</div>
		<div></div>
	</form>
</body>
</html>
