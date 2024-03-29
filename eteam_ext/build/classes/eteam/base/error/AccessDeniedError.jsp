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
		<title>Access Denied｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
	</head>

	<body>
    	<div id="wrap">

    		<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
	  			<h1>Access Denied</h1>

				<!-- メイン -->
				<div id="main">
					<div class="alert alert-block">
<c:if test='${empty exception.message}'>
						<p id='errorMessage'>該当ページへアクセスする権限がありません。</p>
</c:if>
<c:if test='${not empty exception.message}'>
						<p id='errorMessage' <c:if test='${exception.dialog}'>data-dialog='1'</c:if>>${su:htmlEscapeBr(exception.message)}</p>
</c:if>
					</div>
				</div><!-- main -->
			</div><!-- container -->
			<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッターー -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script type="text/javascript">
if($("#errorMessage").attr("data-dialog") == "1"){
	alert($("#errorMessage").text());
	window.close();
}
		</script>
	</body>
</html>
