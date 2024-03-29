<%@page import="eteam.common.open21.Open21Env"%>
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
		<meta charset="utf-8">
		<title>Out Of Memory｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
	</head>

	<body>
    	<div id="wrap">

			<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/HeaderNonDispKaisha.jsp" %>

			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
	  			<h1>Out Of Memory</h1>

				<!-- メイン -->
				<div id="main">
					<div class="alert alert-block">
						<p>処理が混み合っています。時間を空けてから処理するか、数回に分けて実行してください。</p>
					</div>
				</div><!-- main -->
			</div><!-- container -->
			<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッターー -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
	</body>
</html>
