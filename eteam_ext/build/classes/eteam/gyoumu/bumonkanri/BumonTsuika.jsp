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
		<title>部門追加｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>部門追加</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' action='bumon_tsuika_touroku' class='form-horizontal'>

					<!-- 入力フィールド -->
					<section>
						<h2>部門情報</h2>
						<div>
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>部門コード</label>
								<div class='controls'>
									<input type='text' name='bumonCd' class='input-small' maxlength="8" value='${su:htmlEscape(bumonCd)}'>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>部門名</label>
								<div class='controls'>
									<input type='text' name='bumonName' class='input-large' maxlength="20" value='${su:htmlEscape(bumonName)}'>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>親部門コード</label>
								<div class='controls'>
									<input type='text' name='oyaBumonCd' class='input-inline input-small' disabled value='${su:htmlEscape(oyaBumonCd)}'>
									<button type='button' id='bumonSentakuButton' class='btn btn-small'>選択</button>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>親部門名</label>
								<div class='controls'>
									<input type='text' name='oyaBumonName' class='input-inline input-large' disabled value='${su:htmlEscape(oyaBumonName)}'>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>有効期限</label>
								<div class='controls'>
									<b><span class='required'>*</span></b>
									<input type='text' name='kigenFrom' class='input-small datepicker' value='${su:htmlEscape(kigenFrom)}'>
									<span> ～ </span>
									<input type='text' name='kigenTo' class='input-small datepicker' value='${su:htmlEscape(kigenTo)}'>
								</div>
							</div>
							<div class='control-group' <c:if test="${!securityPatternFlag}"> style="display: none"</c:if>>
								<label class='control-label' for='securityPattern'>セキュリティパターン</label>
								<div class='controls'>
									<input type='text' id='securityPattern' name='securityPattern' class='input-small' maxlength='4' value='${su:htmlEscape(securityPattern)}'>
								</div>
							</div>
						</div>
					</section>

					<!-- 処理ボタン -->
					<section>
						<button type="submit" class='btn'><i class='icon-hdd'></i> 登録</button>
					</section>
				</form></div><!-- main -->
				<!-- Modal -->
				<div id='dialog'></div>
			</div><!-- content -->
			<div id="push"></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>

$(document).ready(function(){	

	//部門選択ボタン押下時Function
	$('#bumonSentakuButton').click(function(event) {
		dialogRetBumonName =  $("input[name=oyaBumonName]");
		dialogRetBumonCd =  $("input[name=oyaBumonCd]");
		var kijunBi = $("input[name=kigenFrom]").val();
		if(kijunBi.length != 10){
			commonBumonSentaku();
			return false;
		}
		var width = "800";
		if(windowSizeChk()) {
			width = $(window).width() * 0.9;
		}
		$("#dialog")
		.dialog({
			modal: true,
			width: width,
			height: "520",
			title: "部門選択",
			buttons: {閉じる: function() {$(this).dialog("close");}}
		});
		$("#dialog").load("bumon_sentaku?d_kijunBi="+kijunBi);
		return false;
	});
	
	$('input[name=bumonCd]').blur(function(){
		
		var bcd = $('input[name=bumonCd]').val();
		
		if (bcd.match("^[0]+$")) {
			alert("入力した部門コードは使用できません。");
			return;
		}
		
		$.ajax({
			type : "GET",
			url : "bumon_name_get",
			data : "bumonCd=" + bcd + "&isAllDate=true",
			dataType : 'text',
			success : function(response) {
				if ( response != "" ) {
					if(window.confirm('既に同一部門コードのデータが存在するため、\r\n該当部門の変更画面に遷移します。よろしいですか？')) {
						location.href="bumon_henkou?bumonCd=" + bcd;
					}
				}
			}
		});
	});
	
});
		</script>
	</body>
</html>
