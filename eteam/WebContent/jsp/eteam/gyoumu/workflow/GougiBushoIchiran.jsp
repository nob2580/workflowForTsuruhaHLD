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
		<title>合議部署一覧｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
			<div id='content' class='container'><form id='myForm' method='post' action='gougi_busho_ichiran' class='form-horizontal'>
	
				<!-- タイトル -->
				<h1>合議部署一覧</h1>
				
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- ボタン郡 -->
				<div class='control-group'>
					<button type='button' onClick="location.href='gougi_busho_tsuika'" class='btn'><i class='icon-plus'></i> 追加</button>
				</div>

				<div class='no-more-tables'>
					<table class='table-bordered table-condensed'>
						<thead>
							<tr>
								<th colspan="1" align="left" valign="top">制御条件</th>
								<th colspan="4" align="left" valign="top">承認者</th>
								<th rowspan="2" align="left" valign="top"></th>
							</tr>
							<tr>
								<th align="left" valign="top" style="width:100px">合議部署名</th>
								<th align="left" valign="top">部門</th>
								<th align="left" valign="top">役割</th>
								<th align="left" valign="top">処理権限</th>
								<th align="left" valign="top">承認必要人数</th>
							</tr>
						</thead>
						<tbody>
<c:forEach var="record" items="${gougiList}" varStatus="status">
							<tr>
								<td><a href='gougi_busho_henkou?gougiNo=${su:htmlEscape(record.gougi_pattern_no)}'>${su:htmlEscape(record.gougi_name)}</a></td>
								<td><c:out value="${su:htmlEscapeBr(record.bumon)}" escapeXml="false"/></td>
								<td><c:out value="${su:htmlEscapeBr(record.yakuwari)}" escapeXml="false"/></td>
								<td><c:out value="${su:htmlEscapeBr(record.shori_kengen)}" escapeXml="false"/></td>
								<td><c:out value="${su:htmlEscapeBr(record.shounin_ninzuu)}" escapeXml="false"/></td>
								<td>
									<span class='non-print'>
										<button type='button' name='meisaiUp' class='btn btn-mini'>↑</button>
										<button type='button' name='meisaiDown' class='btn btn-mini'>↓</button>
									</span>
								</td>
							</tr>
</c:forEach>
						</tbody>
					</table>
				</div>
				
			</form></div><!-- content -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>
<!--

/**
 * 上ボタン押下時Function
 */
$("button[name=meisaiUp]").click(function(){
	var currentLocation = $("tbody").find("tr").index($(this).parents("tr"));
	if (currentLocation != '0') {
		var formObject = document.getElementById("myForm");
		formObject.action = 'gougi_busho_ichiran_junjo?currentLocation=' + ++currentLocation + '&upDownFlg=' + true;
		$(formObject).submit();
	}
});

/**
 * 下ボタン押下時Function
 */
$("button[name=meisaiDown]").click(function(){
	var currentLocation = $("tbody").find("tr").index($(this).parents("tr"));
	if (currentLocation != $("tbody").find("tr:last").index()) {
		var formObject = document.getElementById("myForm");
		formObject.action = 'gougi_busho_ichiran_junjo?currentLocation=' + ++currentLocation + '&upDownFlg=' + false;
		$(formObject).submit();
	}
});
 -->
		</script>
	</body>
</html>
