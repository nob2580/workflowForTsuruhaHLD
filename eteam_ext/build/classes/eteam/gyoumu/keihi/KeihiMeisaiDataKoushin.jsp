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
		<title>経費明細データ更新｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>経費明細データ更新</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' action='keihi_data_meisai_koushin_renkei' class='form-horizontal'>
				
					<!-- 入力フィールド -->
					<section class='print-unit'>
						<div class='control-group'>
							<label class='control-label'><span class='required'>* </span>更新対象月</label>
							<div class='controls'>
								<select id='targetDate' name='targetDate' class='input-medium'>
									<c:forEach var="item" items="${monthList}">
										<option value="${item.key}">${su:htmlEscape(item.val)}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</section>

					<!-- 処理ボタン -->
					<section>
						<button type='submit' name='renkeiButton' class='btn'><i class='icon-random'></i> 連携</button>
					</section>
				</form></div><!-- main -->
			</div><!-- /container -->
			<div id='push'></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>

		</script>
	</body>
</html>