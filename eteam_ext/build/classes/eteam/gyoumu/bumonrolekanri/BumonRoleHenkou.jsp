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
		<title>役割変更｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>役割変更</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id="myForm" method='post' class='form-horizontal'>
					<input type="hidden" name='bumonRoleId' value='${su:htmlEscape(bumonRoleId)}'>

					<!-- 入力フィールド -->
					<section>
						<div class='control-group'>
							<label class='control-label'><span class='required'>*</span>役割名</label>
							<div class='controls'>
								<input type='text' name='bumonRoleName' maxlength='20' value='${su:htmlEscape(bumonRoleName)}'>
							</div>
						</div>
					</section>

					<!-- 処理ボタン -->
					<section>
						<button type='button' id='henkouButton' class='btn'><i class='icon-refresh'></i> 変更</button>
						<button type='button' id='sakujoButton' class='btn'><i class='icon-remove'></i> 削除</button>
					</section>
				</form></div>
			</div><!-- content -->
			<div id="push"></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>

$(document).ready(function(){

	//変更ボタン押下
	$("#henkouButton").click(function(){
		$("#myForm").attr("action", "bumon_role_henkou_henkou");
		$("#myForm").submit();
	});

	//削除ボタン押下
	$("#sakujoButton").click(function(){
		if(window.confirm('削除してもよろしいですか？')) {
			$("#myForm").attr("action", "bumon_role_henkou_sakujo");
			$("#myForm").submit();
		}
	});
});
		</script>
	</body>
</html>
