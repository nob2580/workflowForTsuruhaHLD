<%@ page import="eteam.symbol.EteamSymbol"%>
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
		<title>ロール選択｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
	  			<h1>ロール選択</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id="main">
				<div><form action="role_sentaku_sentaku" method="post">
					<input type="hidden" name=selectedGyoumuRoleId value="">
					<ul>
						<li><a href="#" data-gyoumuRoleId="">一般ユーザー　としてログオン</a></li>
<c:if test="${0 < fn:length(gyoumuRoleId)}"><c:forEach var="i" begin="0" end="${fn:length(gyoumuRoleId) - 1}">
						<li><a href="#" data-gyoumuRoleId="${gyoumuRoleId[i]}">${su:htmlEscape(gyoumuRoleName[i])}　としてログオン</a></li>
</c:forEach></c:if>
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
<!--
$(document).ready(function(){

	//部門所属ユーザーとして or 業務ロールのリンク押下
	$("a[data-gyoumuRoleId]").click(function(e){
		$("input[name=selectedGyoumuRoleId]").val($(this).attr("data-gyoumuRoleId"));
		$("form").submit();
	});
});
-->
		</script>
	</body>
</html>
