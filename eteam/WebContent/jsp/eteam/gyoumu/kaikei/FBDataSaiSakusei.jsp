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
		<title>FBデータ作成｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>FBデータ作成</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='myForm' class='form-horizontal' action='fbdata_saisakusei_saisakusei' method='post'>
					<section>
						<div>
							<div class='control-group'>
								<label class='control-label' for=''><span class='required'>*</span>個人精算支払日</label>
								<div class='controls'>
									<select name='kojinSeisanShiharaiBi' class='input-large'>
										<option></option>
										<c:forEach var='record' items='${kojinSeisanShiharaiBiList}'>
											<option value='${su:htmlEscape(record.cd)}' <c:if test='${kojinSeisanShiharaiBi eq record.cd}'>selected</c:if>>${su:htmlEscape(record.val)}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
					</section>
					<span class='non_print'>
						<button type='button' id='saisakuseiButton' class='btn'><i class='icon-random'></i> 作成</button>
					</span>
				</form></div><!-- main -->
			</div><!-- /container -->
			<div id='push'></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>

$(document).ready(function(){

	//作成ボタン押下時
	$("#saisakuseiButton").click(function(){
		var val =  $("select[name=kojinSeisanShiharaiBi] option:selected").val();
		if (val == "") {
			alert("個人精算支払日を選択してください。");
			return;
		}
		var msg = "";
		msg = msg + "FBデータを作成します。よろしいですか？\n\n";
		msg = msg + "個人精算支払日「" + val + "」\n\n";
		msg = msg + "既に金融機関へ送信済みの場合は、\n各金融機関へお問い合わせ後に作成・送信してください。";
		if (window.confirm(msg)) $("#myForm").submit();
	});
});
		</script>
	</body>
</html>
