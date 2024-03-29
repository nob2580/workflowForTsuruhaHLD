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
		<title>ヘルプ情報｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
<!-
-->
		</style>
	</head>

	<body>
    	<div id='wrap'>

			<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<div id='content' class='container'>

				<!-- タイトル -->
				<h1>ヘルプ情報 (${su:htmlEscape(gamenName)})</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form action='help_editor?gamenId=${su:htmlEscape(gamenId)}' class='form-horizontal'>
					<input type='hidden' name='gamenId' value='${su:htmlEscape(gamenId)}'>

					<!-- 表示 -->
					<section>
						<div><label>${helpInfo}</label></div>
					</section>

					<!-- 処理ボタン -->
					<section>
<c:if test="${editBtnFlg == '1'}">
						<button type='submit' class='btn'><i class='icon-pencil'></i> 編集</button>
</c:if>
					</section>
				</form></div><!-- main -->
			</div><!-- content -->
			<div id='push'></div>
		</div><!-- wrap -->
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<!-- スクリプト -->
		<script style='text/javascript'>
<!--
-->
		</script>
	</body>
</html>
