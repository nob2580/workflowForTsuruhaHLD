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
		<title>バックアップ運用受理｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>バックアップ運用受理</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'>
					<p>バックアップ運用処理を受け付けました。</p><br>
					<p class='highlight'>※処理が終了するまで、多少の時間がかかります。</p>
					<p><a href='../appl/'>メニューに戻る</a>　※処理実行中は、左記リンクを押下してもエラーが表示される場合があります。</p>
				</div><!-- main -->
			</div><!-- /container -->
			<div id='push'></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>
		</script>
	</body>
</html>
