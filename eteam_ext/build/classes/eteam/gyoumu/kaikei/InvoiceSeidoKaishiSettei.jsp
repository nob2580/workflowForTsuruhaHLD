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
		<title>インボイス制度開始設定｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
	<input type="hidden" name='invoiceStartFlg'  value='${su:htmlEscape(invoiceStartFlg)}'>
		<div id='wrap'>

			<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
				<h1>インボイス制度開始設定</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				
				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' action='invoice_seido_kaishi_touroku' class='form-horizontal'>

					<p style="color :green">インボイス制度に係る入力ができるように開始処理をします。</p>
					<p style="color :green">（当開始処理をしない場合、2023年10月1日以降でもインボイス制度に対応した入力はできません）</p>
					<!-- 処理ボタン -->
					<section>
						<button type='button' id='kaishiButton' class='btn'<c:if test='${"1" eq invoiceStartFlg}'>disabled</c:if>><i class='icon-plus'></i> 開始</button>
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
	$("#kaishiButton").click(function(){
		$("#myForm").attr("action", "invoice_seido_kaishi_touroku").submit();
	});
});
		</script>
	</body>
</html>
