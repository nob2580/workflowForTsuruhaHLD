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
		<title>業務ロール一覧｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>業務ロール一覧</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				
				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' action = 'gyoumu_role_ichiran_hyoujijun_koushin'>

					<!-- 処理ボタン -->
					<section>
						<button type='button' id='tsuikaButton' class='btn'><i class='icon-plus'></i> 追加</button>
					</section>

					<!-- ロール一覧 -->
					<section>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed' style="margin-top:8px">
								<thead>
									<tr>
										<th>ロール名</th>
										<th></th>
									</tr>
								</thead>
								<tbody id='gyoumuRoleList'>
<c:forEach var="record" items="${gyoumuRoleList}">
									<tr>
<c:if test="${record.gyoumu_role_id == '00000'}" >
										<td>${su:htmlEscape(record.gyoumu_role_name)}</td>
</c:if>
<c:if test="${record.gyoumu_role_id != '00000'}" >
										<td><a href='gyoumu_role_henkou?gyoumuRoleId=${su:htmlEscape(record.gyoumu_role_id)}'>${su:htmlEscape(record.gyoumu_role_name)}</a></td>
</c:if>
										<td>
<c:if test="${record.gyoumu_role_id != '00000'}" >
											<button type='button' name='gyoumuRoleUp'   class='btn btn-mini' data-id='${su:htmlEscape(record.gyoumu_role_id)}'>↑</button>
											<button type='button' name='gyoumuRoleDown' class='btn btn-mini' data-id='${su:htmlEscape(record.gyoumu_role_id)}'>↓</button>
											<input type='hidden' name='gyoumuRoleId' value='${su:htmlEscape(record.gyoumu_role_id)}'>
</c:if>
										</td>
									</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
					</section>
				</form></div><!-- main -->
			</div><!-- content -->
			<div id="push"></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script type="text/javascript">


$(document).ready(function(){

	//追加ボタン押下
	$("#tsuikaButton").click(function(){
		location.href='gyoumu_role_tsuika';
	});

	//上ボタン
	$("button[name=gyoumuRoleUp]").click(function(){
		var tr = $(this).closest("tr");
		
		// 1行目はイベント実行されないように制御します。
		if (tr[0].rowIndex == 1) return;
		
		if(tr.prev()){
			tr.insertBefore(tr.prev());
		}
		$("#myForm").submit();
	});

	//下ボタン押下
	$("button[name=gyoumuRoleDown]").click(function(){
		var tr = $(this).closest("tr");
		
		// 最終行のDOWNイベントは実行されないように制御します。
		if (tr[0].rowIndex == gyoumuRoleList.rows.length) return;
		
		if(tr.next()){
			tr.insertAfter(tr.next());
		}
		$("#myForm").submit();
	});
});
		</script>
	</body>
</html>
