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
		<title>取引（仕訳）変更｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
<!--
#taishaku tbody tr th {
	text-align: right;
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
				<h1>取引（仕訳）変更</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='myForm' class='form-horizontal' method='post' action='torihiki_henkou_koushin'>
					<input type="hidden" name='denpyouKbn' value='${su:htmlEscape(denpyouKbn)}'>
					<input type="hidden" name='preEventUrl' value='${su:htmlEscape(preEventUrl)}'>

<!-- 取引追加・変更共通部 -->
		<%@ include file="/jsp/eteam/gyoumu/kaikei/TorihikiCommonForm.jsp" %>

					<!-- 処理ボタン -->
					<section>
						<button type="submit" class='btn'><i class='icon-refresh'></i> 更新</button>
					</section>
				</form></div><!-- main -->
				<!-- Modal -->
				<div id='dialog'></div>
			</div><!-- /container -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<%@ include file="/jsp/eteam/gyoumu/kaikei/TorihikiCommonScript.jsp" %>
		<script style='text/javascript'>
		// 取引追加と共通ならTorihikiCommonScript.jspへ書くこと
		$(document).ready(function(){
			// 特殊仕訳（仕訳枝番号がマイナス）の場合、入力不可・ボタン非表示※現状無意味なはず
			var edaNo = $("input[name=shiwakeEdano]").val();
			if(edaNo < 0) {
			    $("input,select").prop("disabled", true);
			    $('button').hide();
			}
		});
		</script>
	</body>
</html>
