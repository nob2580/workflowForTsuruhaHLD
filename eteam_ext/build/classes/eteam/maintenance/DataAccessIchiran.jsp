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
		<title>データアクセス一覧｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>データアクセス一覧</h1>
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				<div id='main'>
					<div class='no-more-tables'>
						<table class='table-bordered table-condensed'>
							<thead>
								<tr>
									<th>テーブル名(和名)</th>
									<th>テーブル名(物理名)</th>
								</tr>
							</thead>
							<tbody>
<c:forEach var="record" items="${tableList}" varStatus="status">
								<tr>
									<td><a href='data_access?tableName=${su:htmlEscape(record.table_name)}' target='_blank'>${su:htmlEscape(record.table_comment)}</a></td>
									<td>${su:htmlEscape(record.table_name)}</td>
								</tr>
</c:forEach>
							</tbody>
						</table>
					</div>
				</div><!-- main -->
			</div><!-- content -->
			<div id='push'></div>
			</div><!-- wrap -->
		
			<!-- フッター -->
			<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script type="text/javascript">
<!-- 

-->
		</script>
	</body>
</html>
