<%@ page import="eteam.symbol.EteamSymbol"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!DOCTYPE html>
<html lang='ja'>
	<head>
		<meta charset='utf-8'>
		<title>パスワード変更｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
<!--
-->
		</style>
	</head>
	<body>
		<div id='wrap'>
			<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>
			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
				<h1>パスワード変更</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main' class='form-horizontal'>

				<form id="myForm" method="post">

					<!-- ユーザー属性 -->
					<section>
						<h2>ユーザー情報</h2>
						<div class='control-group'>
							<label class='control-label' for='userId'>ユーザーID</label>
							<div class='controls' style="padding: 5px" id='userId'><b>${su:htmlEscape(userId)}</b></div>
							<input type='hidden' name='userId' value='${su:htmlEscape(userId)}'>
						</div>
						<div class='control-group'>
							<label class='control-label' for='password'>パスワード(変更後)</label>
							<div class='controls'>
								<input type='text' name='dummy' value='${su:htmlEscape(userId)}' maxlength="0" style="display:none">
								<input type='password' id='password' name='password' class='input-medium data-delayenable' maxlength="32" autocomplete='off' disabled>
								<label class='label'><span class='required'>*</span>確認用</label>
								<input type='text' name='dummy' value='${su:htmlEscape(userId)}' maxlength="0" style="display:none">
								<input type='password' id='password2' name='password2' class='input-medium data-delayenable' maxlength="32" autocomplete='off' disabled>
							</div>
						</div>
						<button type='button' class='btn' onClick="eventBtn()"><i class='icon-refresh'></i> 変更</button>
					</section>

				</form>
				</div><!-- main -->
				<!-- Modal -->
				<div id='dialog'></div>
			</div><!-- content -->
		<div id="push"></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>
<!--
/**
 * イベントボタン押下時のアクションの切り替え
 */
 function eventBtn(eventName) {
	var formObject = document.getElementById("myForm");
	
	if ($("#password").val() != $("#password2").val()) {
		alert("パスワードの入力が確認用の入力と不一致です。打ち直してください。");
		return;
	}
	formObject.action = 'forcepass_koushin';
	$(formObject).submit();
}
//画面表示後の初期化
$(document).ready(function(){
	//chromeによる強制オートコンプリートの抑制用処理
	delayEnable();
});
-->
		</script>
	</body>
</html>
