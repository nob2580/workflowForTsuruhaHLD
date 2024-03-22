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
		<title>${su:htmlEscape(pageTitle)}｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>${su:htmlEscape(pageTitle)}</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' action='seikyuusho_shime_shime' class='form-horizontal'>
					<input type='hidden' name='kaijobi'>
					<input type='hidden' name='denpyouKbn' value='${su:htmlEscape(denpyouKbn)}'>

					<!-- 締処理 -->
					<section>
						<h2>締処理</h2>
						<div class='control-group'>
							<label class='control-label'><span class='required'>*</span>締日</label>
							<div class='controls'>
								<input type='text' name='shimebi' class='input-small datepicker' value='${su:htmlEscape(shimebi)}'>
								<button type='button' class='btn btn-small' id='shimeButton'><i class='icon-check'></i> 締処理</button>
							</div>
						</div>
					</section>

					<!-- 締日 -->
					<section>
						<h2>締日</h2>
						<div class='no-more-tables'>
<c:if test="${fn:length(shimebiList) > 0}">
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th style='width: 120px;'>締日</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
	<c:forEach var="record" items="${shimebiList}" varStatus="status">
									<tr>
										<td class='text-c shimebi'>${record.shimebi}</td>
										<td><button type='button' class='btn btn-small kaijoButton'><i class='icon-remove'></i> 締解除</button></td>
									</tr>
	</c:forEach>
								</tbody>
							</table>
</c:if>
						</div>
					</section>
				</form></div><!-- main -->
			</div><!-- /container -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>
$(document).ready(function(){
	$("#shimeButton").click(function(){
		var denpyouKbn = document.forms.myForm.denpyouKbn.value;
		var title = $("h1").text();

		if ($("[name=shimebi]").val() == "") {
			alert("締日を入力してください。");
			return;
		}
		if (confirm("締日「" + $("[name=shimebi]").val() + "」で" + title + "の計上日を締めます。\nよろしいですか？")) {
			$("form").attr("action", "seikyuusho_shime_shime");
			$("[name=denpyouKbn]").val(denpyouKbn);
			$("form").submit();
		}
	});
	$(".kaijoButton").click(function(){
		var kaijobi = $(this).closest("tr").find(".shimebi").text();
		var denpyouKbn = document.forms.myForm.denpyouKbn.value;
		var title = $("h1").text();

		if (confirm(title + "の締日「" + kaijobi + "」を解除します。\nよろしいですか？")) {
			$("form").attr("action", "seikyuusho_shime_kaijo");
			$("[name=kaijobi]").val(kaijobi);
			$("[name=denpyouKbn]").val(denpyouKbn);
			$("form").submit();
		}
	});
});
		</script>
	</body>
</html>
