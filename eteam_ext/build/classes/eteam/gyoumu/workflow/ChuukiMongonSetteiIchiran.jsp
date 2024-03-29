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
		<title>最終承認者・注記文言設定一覧｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>最終承認者・注記文言設定一覧</h1>
				
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				
				<!-- ボタン郡 -->
				<div class='control-group'>
					<button type='button' onClick="location.href='chuuki_mongon_settei_tsuika'" class='btn'><i class='icon-plus'></i> 追加</button>
				</div>

				<div class='no-more-tables'>
					<table class='table-bordered table-condensed'>
						<thead>
							<tr>
								<th align="left" valign="top" style="width:105px">伝票種別</th>
								<th align="left" valign="top">最終承認者</th>
								<th align="left" valign="top">注記文言</th>
								<th align="left" valign="top" style="width:190px">有効期限</th>
							</tr>
						</thead>
						<tbody>
<c:forEach var="record" items="${saishuuShouninList}" varStatus="status">
							<tr class='${record.bg_color}'>
								<td valign="top">
									<a href='chuuki_mongon_settei_henkou?denpyouKbn=${su:htmlEscape(record.denpyouKbn)}&edaNo=${su:htmlEscape(record.edaNo)}'>${su:htmlEscape(record.denpyouName)}</a>
								</td>
								<td valign="top">${su:htmlEscapeBr(record.shouninsha)}</td>
								<td valign="top" style='max-width:750px;word-break: break-all;'>${su:htmlEscapeBr(record.chuukiMongon)}</td>
								<td valign="top">${su:htmlEscape(record.yuukouKigen)}</td>
								
							</tr>
</c:forEach>
						</tbody>
					</table>
				</div>
				
			</div><!-- content -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>
<!--
 -->
		</script>
	</body>
</html>
