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
		<title>部門推奨ルート一覧｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>部門推奨ルート一覧</h1>
				
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- ボタン郡 -->
				<div class='control-group'>
					<button type='button' onClick="location.href='bumon_suishou_route_tsuika'" class='btn'><i class='icon-plus'></i> 追加</button>
				</div>

				<div class='no-more-tables'>
					<table class='table-bordered table-condensed'>
						<thead>
							<tr>
<c:if test="${torihikiHyoujiFlg}"><th colspan="5" align="left" valign="top">制御条件</th></c:if>
<c:if test="${!torihikiHyoujiFlg}"><th colspan="3" align="left" valign="top">制御条件</th></c:if>
								<th rowspan="2" align="left" valign="top">承認者</th>
								<th rowspan="2" align="left" valign="top" style="width:185px">有効期限</th>
							</tr>
							<tr>
								<th align="left" valign="top" style="width:100px">伝票</th>
	<c:if test="${torihikiHyoujiFlg}">
								<th align="left" valign="top">取引コード</th>
								<th align="left" valign="top">取引</th>
	</c:if>
								<th align="left" valign="top">起票部門</th>
								<th align="left" valign="top">金額</th>
							</tr>
						</thead>
<!-- 推奨ルート内容の表示の仕方
	 「取引コード」「取引」のみbumonRouteList:情報 = 1:n(n>=1)の関係となり少し複雑になっているため説明
	 ①1行目は全項目を記載(「取引コード」「取引」のみリストで格納しているので先頭の要素を取得)
	   この際、取引の設定数「取引コード」「取引」以外のセルを縦に結合しておく(1つのセルを分割することができないので周りのセルを結合することで整合性をとっている)
	 ②取引が複数設定されている場合は「取引コード」「取引」のみx行目(x>1)として記載
	   x行目以降のループは、先頭の要素を1行目で表示済みなので2つ目の要素から回している
-->
						<tbody>
<c:forEach var="record" items="${bumonRouteList}" varStatus="status">
	<c:set var="countTorihiki" value="${record.countTorihiki}"/>
							<tr class='${record.bg_color}'>
								<td rowspan="${countTorihiki}"><a href='bumon_suishou_route_henkou?denpyouKbn=${su:htmlEscape(record.denpyouKbn)}&bumonCd=${su:htmlEscape(record.bumonCd)}&edano=${su:htmlEscape(record.edano)}'>${su:htmlEscape(record.denpyouName)}</a></td>
	<c:if test="${torihikiHyoujiFlg}">
								<td>${su:htmlEscape(record.shiwakeEdano.get(0))}</td>
								<td>${su:htmlEscape(record.torihikiName.get(0))}</td>
	</c:if>
								<td rowspan="${countTorihiki}">${su:htmlEscape(record.kihyouBumon)}</td>
								<td rowspan="${countTorihiki}">${su:htmlEscape(record.kingaku)}</td>
								<td rowspan="${countTorihiki}"><c:out value="${su:htmlEscapeBr(record.shouninsha)}" escapeXml="false"/></td>
								<td rowspan="${countTorihiki}">${su:htmlEscape(record.yuukouKigen)}</td>
							</tr>
	<c:if test="${torihikiHyoujiFlg}">
		<c:forEach var="torihikiCd" items="${record.shiwakeEdano}" begin="1" varStatus="status">
							<tr class='${record.bg_color}'>
								<td>${su:htmlEscape(torihikiCd)}</td>
								<td>${su:htmlEscape(record.torihikiName.get(status.index))}</td>
							</tr>
		</c:forEach>
	</c:if>
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
