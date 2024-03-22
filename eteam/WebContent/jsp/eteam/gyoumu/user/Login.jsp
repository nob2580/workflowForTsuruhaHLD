<%@page import="eteam.symbol.EteamSymbol"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!DOCTYPE html>
<html lang="ja">
	<head>
		<meta charset="utf-8">
		<title>ログオン｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type="text/css">
<!-- 
body {
<% if (Open21Env.getVersion() == Open21Env.Version.SIAS) { %>
	background-color: #f0f2f5;
<% } else { %>
	background-color: #f5f5f5;
<% } %>
}

.form-signin {
	max-width: 500px;
	padding: 0px 0px 0px;
	margin: 0 auto 20px;
	background-color: #fff;
	border: 1px solid #e5e5e5;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
	-webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
	-moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
	box-shadow: 0 1px 2px rgba(0,0,0,.05);
}
.form-signin .form-signin-heading,
.form-signin .checkbox {
	margin: 20px 15px 5px;
<% if (Open21Env.getVersion() == Open21Env.Version.SIAS) { %>
	color:#FFFFFF;
<% } %>
}
.form-signin input[type="text"],
.form-signin input[type="password"] {
	font-size: 16px;
	height: auto;
	margin: 15px 15px 0px;
	padding: 7px 9px;
	width:  470px;
}

.form-signin button {
	margin: 5px 15px 30px 15px;
<% if (Open21Env.getVersion() == Open21Env.Version.SIAS) { %>
	background:linear-gradient(#FFFFFF, #F4F4F4);
	color:#000000;
<% } %>
}

<% if (Open21Env.getVersion() == Open21Env.Version.SIAS) { %>
.form-signin div {
	background:linear-gradient(#005694, #003770);
}
<% } %>

-->
		</style>
	</head>

	<body>
		<div class="container" style="padding-top:40px;">
			<form id="myForm" action="login_login" method="post" class="form-signin">
				<img alt="ロゴ" style="height:auto;width:100%;" src="/eteam/static/img/logo_<%=Open21Env.getVersion()%>.png"/>
				<div>
					<%@ include file="/jsp/eteam/include/InputError.jsp" %>
					<input type="text" name="userIdOrMailAddress" maxlength="50" class="input-block-level" placeholder="ID または メールアドレス" value="${su:htmlEscape(userIdOrMailAddress)}">
					<input type="password" name="password" maxlength="32" class="input-block-level" placeholder="パスワード">
					<label class="checkbox">
						<input type="checkbox" name="ever" value="1" <c:if test='${ever eq "1"}'>checked</c:if>>ログオンしたままにする
					</label>
					<button type="submit" class="btn btn-primary">ログオン</button>
					<input type="hidden" id="kbn" name="kbn" value="${kbn}">
				</div>
			</form>
			<footer style="text-align: center;">
				<p><%=EteamSymbol.FOOTER%></p>
			</footer>
		</div><!-- /container -->

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script type="text/javascript">
<!--

//クッキー定数
var EXPIRES		= 120;
var CKEY_ID		= "eteam.userIdOrMailAddress";
var CKEY_PSWD	= "eteam.password";
var CKEY_CHK	= "eteam.ever";

$(document).ready(function(){

	//強制ログインは画面再表示
	if($("#kbn").val() == "1"){
		setTimeout("link()", 0);
	}
	
	//表示時クッキー復元
	try {
		if ("1" == Cookies.get(CKEY_CHK)) {
			$("[name=userIdOrMailAddress]").val(Cookies.get(CKEY_ID));
			$("[name=password]").val(Cookies.get(CKEY_PSWD));
			$("[name=ever]").attr("checked", Cookies.get(CKEY_CHK));
		}
	} catch(e){}

	//サブミット時クッキー保存
	//オプションにpathを設定しているのはライブラリ入替前との互換を持たすため
	$("form").submit(function(e) {
		try {
			var path = location.pathname.split('/');	// 例："/eteam/schemaname/appl/" → ["", "eteam", "schemaname", "appl" ""]
			if ($("[name=ever]").prop("checked")) {
				var option = {expires: EXPIRES, path:"/eteam/" + path[2] + "/appl"};
				Cookies.set(CKEY_ID, $("[name=userIdOrMailAddress]").val(), option);
				Cookies.set(CKEY_PSWD, $("[name=password]").val(), option);
				Cookies.set(CKEY_CHK, $("[name=ever]").val(), option);
			} else {
				//保存時にpathを設定していた場合、削除時にもそれを指定してあげないといけない
				Cookies.remove(CKEY_ID, {path:"/eteam/" + path[2] + "/appl"});
				Cookies.remove(CKEY_PSWD, {path:"/eteam/" + path[2] + "/appl"});
				Cookies.remove(CKEY_CHK, {path:"/eteam/" + path[2] + "/appl"});
			}
		} catch(e){}
	});
});

/**
 * 明細子画面内にログイン画面が表示されるのを防ぐ為に強制ログイン時は画面を再表示させます
 */
function link(){
	var urlBase = location.protocol + "//" + location.host
	var path = location.pathname.split('/');	// 例："/eteam/schemaname/appl/" → ["", "eteam", "schemaname", "appl" ""]
	var url = urlBase + "/" + path[1] + "/" + path[2] + "/appl/login"
	window.location.href=url;
}
-->
		</script>
	</body>
</html>
