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
		<title>部門一覧｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>部門一覧</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'>

					<!-- 一覧 -->
					<form id='inputFieldForm' name='inputFieldForm' class='form-horizontal'>
					<section>
						<div class='span6'>
							<div class='control-group'>
								<label class='control-label'>基準日</label>
								<div class='controls'>
									<input type="text" name='kijunBi'  value='${su:htmlEscape(kijunBi)}' class='input-small datepicker'>
									<button type='button' id='hyoujiButton' class='btn'>表示</button>
								</div>
							</div>
							<div class='control-group'>
								<button type='button' id='tsuikaButton' class='btn'><i class='icon-plus'></i> 追加</button>
							</div>
							<div class='thumbnail'>
								<div id='bumonList'>
<c:forEach var="record" items="${bumonList}">
									<p>${record.prefix}<i class='icon-chevron-right'></i> <a href='bumon_henkou?bumonCd=${su:htmlEscape(record.bumon_cd)}&kijunDate=${su:htmlEscape(kijunBi)}' class='${record.bg_color}' >${su:htmlEscape(record.bumon_name)}（${su:htmlEscape(record.bumon_cd)}）</a></p>
</c:forEach>
								</div>
							</div>
						</div>
					</section>
					</form>
				</div><!-- main -->
			</div><!-- content -->
			<div id='push'></div>
		</div><!-- wrap -->
		
			<!-- フッター -->
			<%@ include file="/jsp/eteam/include/Footer.jsp" %>

			<%@ include file="/jsp/eteam/include/Script.jsp" %>
			<script type="text/javascript">

$(document).ready(function(){

	// 全社を変更画面へ遷移しないように制御します。
	$("#bumonList a:first").click(function(){return false;});
	
	$("#hyoujiButton").click(function(){
		var formObject = document.getElementById("inputFieldForm");
		formObject.action = 'bumon_ichiran_kijun';
		formObject.method = 'post';
		formObject.target = '_self';
		$(formObject).submit();
	});
	
	//追加ボタン押下
	$("#tsuikaButton").click(function(){
		location.href='bumon_tsuika'
	});
});
		</script>
	</body>
</html>
