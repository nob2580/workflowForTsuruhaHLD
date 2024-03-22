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
		<title>マスターデータ一覧｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
<!--
td.center {
	text-align: center;
}

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
				<h1>マスターデータ一覧</h1>
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				
				<!-- メイン -->
				<div id='main'><form id="myForm" method="get" action='master_data_henkou'>

					<!-- 一覧 -->
					<section>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>マスターID</th>
										<th>マスター名</th>
										<th style="width:135px">更新日時</th>
									</tr>
								</thead>
								<tbody>
<c:forEach var="record" items="${masterDataList}">
									<tr>
										<td style='max-width:300px;word-break:break-all;'>
<c:choose>
	<c:when test='${record.henkou_kahi_flg == "0"}'>
											${su:htmlEscape(record.master_id)}
	</c:when>
	<c:otherwise>
											<a href='master_data_shoukai?masterId=${su:htmlEscape(record.master_id)}'>
												${su:htmlEscape(record.master_id)}
											</a>
	</c:otherwise>
</c:choose>
										</td>
										<td>${su:htmlEscape(record.master_name)}</td>
										<td>${record.koushin_time}</td>
									</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
					</section>
				</form></div><!-- main -->
			</div><!-- content -->
			<div id='push'></div>
		</div><!-- wrap -->
			
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script type="text/javascript">

$(document).ready(function(){

	//追加ボタン押下
	$("#tsuikaButton").click(function(){
		location.href = "master_data_tsuika"; 
	});
});
		</script>
	</body>
</html>
