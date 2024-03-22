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
		<title>ガイダンス起票｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
	</head>

	<body>
    	<div id='wrap'>

    		<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
				<h1>ガイダンス起票</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'>

					<!-- 手続き選択 -->
					<section>
<c:if test="${0 < fn:length(midashiList)}"><c:forEach var="i" begin="0" end="${fn:length(midashiList) - 1}" step="2">
						<div class='row'>
							<div class='span6'>
								<h2>${su:htmlEscape(midashiList[i].midashiName)}</h2>
								<div class='thumbnail'>
									<ul>
		<c:forEach var="jishouData" items="${midashiList[i].jishouList}">
										<li><a href='shinki_kihyou?jishouId=${su:htmlEscape(jishouData.jishouId)}'>${su:htmlEscape(jishouData.jishouName)}</a></li>
		</c:forEach>
									</ul>
								</div>
							</div>
	<c:if test="${i + 1 <= fn:length(midashiList) - 1}">
							<div class='span6'>
								<h2>${su:htmlEscape(midashiList[i+1].midashiName)}</h2>
								<div class='thumbnail'>
									<ul>
		<c:forEach var="jishouData" items="${midashiList[i+1].jishouList}">
										<li><a href='shinki_kihyou?jishouId=${su:htmlEscape(jishouData.jishouId)}'>${su:htmlEscape(jishouData.jishouName)}</a></li>
		</c:forEach>
									</ul>
								</div>
							</div>
	</c:if>
						</div>
</c:forEach></c:if>
					</section>
				</div><!-- main -->
				<div id='push'></div>
			</div><!-- content -->
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		<%@ include file="/jsp/eteam/include/Script.jsp" %>

		<!-- スクリプト -->
		<script style='text/javascript'>
		</script>
	</body>
</html>
