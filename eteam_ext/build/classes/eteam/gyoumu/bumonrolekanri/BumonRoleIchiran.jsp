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
		<title>役割一覧｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>役割一覧</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				
				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' action = 'bumon_role_ichiran_hyoujijun_koushin'>

					
					<!-- 処理ボタン -->
					<section>
						<button type='button' id='tsuikaButton' class='btn'><i class='icon-plus'></i> 追加</button>
					</section>

					<!-- ロール一覧 -->
					<section>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>役割名</th>
										<th></th>
									</tr>
								</thead>
								<tbody id='bumonRoleList'>
	<c:forEach var="record" items="${bumonRoleList}">
									<tr>
										<td><a href='bumon_role_henkou?bumonRoleId=${su:htmlEscape(record.bumon_role_id)}'>${su:htmlEscape(record.bumon_role_name)}</a></td>
										<td>
											<button type='button' name='bumonRoleUpButton'   class='btn btn-mini' data-id='${record.bumon_role_id}'>↑</button>
											<button type='button' name='bumonRoleDownButton' class='btn btn-mini' data-id='${record.bumon_role_id}'>↓</button>
											<input type='hidden' name='bumonRoleId' value='${su:htmlEscape(record.bumon_role_id)}'>
										</td>
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
		location.href = "bumon_role_tsuika";
	});

	//上ボタン
	$("button[name=bumonRoleUpButton]").click(function(){
		var tr = $(this).closest("tr");

		// １行目はイベント実行されないように制御します。
		if (tr[0].rowIndex == 1) return;

		if(tr.prev()){
			tr.insertBefore(tr.prev());
		}
		$("#myForm").submit();
	});
	//下ボタン押下
	$("button[name=bumonRoleDownButton]").click(function(){
		var tr = $(this).closest("tr");

		// 最終行のDOWNイベントは実行されないように制御します。
		if (tr[0].rowIndex == bumonRoleList.rows.length) return;
		
		if(tr.next()){
			tr.insertAfter(tr.next());
		}
			$("#myForm").submit();
		});
});
		</script>
	</body>
</html>
