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
		<title>マニュアル選択｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
			<div id="content" class="container">

				<!-- タイトル -->
	  			<h1>マニュアル選択</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id="main">
				<div><form action="manual_sentaku_sentaku" method="post">
					<input type="hidden" name='keiriShoriRole' value='${su:htmlEscape(keiriShoriRole)}'>
					<input type="hidden" name='kanriRole' value='${su:htmlEscape(kanriRole)}'>
					<ul>
						<c:if test='${wfRole}'>
							<li><a href='/eteam/static/pdf/manual_SIAS_user.pdf' target='_blank'>一般ユーザー</a></li>
						</c:if>
						<c:if test='${sessionScope.user.gyoumuRoleId eq 00000 or keiriShoriRole}'>
							<li><a href='/eteam/static/pdf/manual_SIAS_accountant.pdf' target='_blank'>経理担当者</a></li>
						</c:if>
						<c:if test='${sessionScope.user.gyoumuRoleId eq 00000 or kanriRole}'>
							<li><a href='/eteam/static/pdf/manual_SIAS_admini.pdf' target='_blank'>管理者</a></li>
						</c:if>
					</ul>
				</form></div>
				</div><!-- main -->
			</div><!-- container -->
			<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッターー -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script type="text/javascript">
		</script>
	</body>
</html>
