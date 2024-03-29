<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<html lang='ja'>
	<head>
		<meta charset='utf-8'>
		<title>伝票一覧全ユーザー共通表示項目設定｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>伝票一覧全ユーザー共通表示項目設定</h1>
				
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				
				<!-- メイン -->
				<div id='main'><form id='tourokuForm' method='post' action='denpyou_koumoku_kyoutsu_settei_touroku' class='form-horizontal'>
					<div class='empty'>
						<div class='no-more-tables empty'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>対象</th>
										<th>項目名</th>
										<th></th>
									</tr>
								</thead>
								<tbody id='enabled'>
									<tr>
										<td align="center"><input type="checkbox" disabled checked></td>
										<td>対象</td>
										<td>
											<input type='hidden' name='staticItem' value='true'>
										</td>
									</tr>
<c:forEach var="item" items="${itemList}">
									<tr>
										<td align="center"><input type="checkbox" name="displayItem" value="${su:htmlEscape(item.name)}" <c:if test="${!item.editable}">disabled</c:if> <c:if test="${item.display}">checked</c:if>></td>
										<td style="width:150px">
											${su:htmlEscapeBr(item.label)}
										</td>
										<td style="width:60px">
											<button type='button' name='itemUp'     class='btn btn-mini'>↑</button>
											<button type='button' name='itemDown'   class='btn btn-mini'>↓</button>
											<input type='hidden' name='itemName'	value='${su:htmlEscape(item.name)}'>
										</td>
									</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
					<!-- 処理ボタン -->
					<section>
						<button type='submit' class='btn'><i class='icon-hdd'></i> 全ユーザー共通設定として登録</button>
					</section>
				</form></div>
				
			</div><!-- content -->
			<div id="push"></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>
		</script>
	</body>
</html>

<!-- スクリプト -->
<script style='text/javascript'>
<!--
//入力補助
commonInit($("#dialogMain"));

//ユーザー「↑」ボタン押下時アクション
$("button[name=itemUp]").click(function() {
	var tr = $(this).closest("tr");
	if(tr.prev()){
		if ("true" != tr.prev().find("input[name=staticItem]").val()) {
			tr.insertBefore(tr.prev());
		}
	}
});

//ユーザー「↓」ボタン押下時アクション
$("button[name=itemDown]").click(function() {
	var tr = $(this).closest("tr");
	if(tr.next()){
		if ("true" != tr.next().find("input[name=staticItem]").val()) {
			tr.insertAfter(tr.next());
		}
	}
});

//-->
</script>
