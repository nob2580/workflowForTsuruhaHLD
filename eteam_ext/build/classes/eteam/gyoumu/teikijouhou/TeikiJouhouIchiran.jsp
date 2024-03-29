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
		<title>定期情報一覧｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>定期情報一覧</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main' class='form-horizontal'>
				<form id="teikiKensakuForm" method="get">
					<input type="hidden" name='preEventUrl' value='${su:htmlEscape(preEventUrl)}'>
					
					<!-- 検索枠 -->
					<section>
						<h2>検索条件</h2>
						<div>
							<div class='no-more-tables'>
								<div class='control-group'>
									<label class='control-label'>ユーザー</label>
									<div class='controls'>
										<input type='text' id="shainNo" class='input-medium pc_only' value='${su:htmlEscape(shainNo)}'>
										<input type='text' id="userName" class='input-xlarge' disabled value='${su:htmlEscape(userName)}'>
										<input type='hidden' name="userId" value='${su:htmlEscape(userId)}'>
										<button type='button' name='userSentakuButton' class='btn btn-small'>選択</button>
									</div>
								</div>
								<div class='control-group'>
									<label class='control-label'>使用日</label>
									<div class='controls'>
										<input type='text' name='shiyouKaishiBi' class='input-small datepicker' value='${su:htmlEscape(shiyouKaishiBi)}'>
										～
										<input type='text' name='shiyouShuuryouBi' class='input-small datepicker' value='${su:htmlEscape(shiyouShuuryouBi)}'>
									</div>
								</div>

							</div>
							<div class='blank'></div>
							<div>
								<button type='button' name='kensakuButton' class='btn'><i class='icon-search'></i> 検索</button>
							</div>
						</div>
					</section>
				</form>
					<!-- 検索結果 -->

				<form id="teikiEnchouForm">
					<section>
						<h2>検索結果</h2>
						
<!-- 対象(チェックボックス) ユーザー名 使用開始日 使用終了日 乗車区間 変更ボタン列  -->
						
						<button type='button' name='tsuikaButton' class='btn'><i class='icon-plus'></i> 追加</button>
						<button type='button' name='ikkatsuEnchoButton' class='btn'>使用期間一括無期限延長</button>
						<input type='hidden' name="kensakuUserId">
						<input type='hidden' name='kensakuKaishiBi'>
						<input type='hidden' name='kensakuShuuryouBi'>
						

						
<c:if test="${fn:length(teikiJouhouList) > 0}" >
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th><nobr>対象</nobr></th>
										<th><nobr>ユーザー名</nobr></th>
										<th><nobr>使用開始日</nobr></th>
										<th><nobr>使用終了日</nobr></th>
										<th style="width:400px"><nobr>乗車区間</nobr></th>
										<th><nobr></nobr></th>
									</tr>
								</thead>
								<tbody>
	<c:forEach var="record" items="${teikiJouhouList}">
									<tr class='${record.bg_color}'>
										<td align="center"><input type='checkbox' name='sentaku' value='${su:htmlEscape(record.index)}' <c:if test="${record.isChecked eq true}" >checked</c:if> />
											<input type="hidden" name="sentakuFlg"/>
										</td>
										<td><nobr>${su:htmlEscape(record.user_full_name)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.shiyou_kaishibi)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.shiyou_shuuryoubi)}</nobr></td>
										<td>${su:htmlEscape(record.jyoushakukan)}</td>
										<td align = "center">
											<input type='hidden' name='rowUserId' value='${su:htmlEscape(record.user_id)}' >
											<input type='hidden' name='rowUserFullName' value='${su:htmlEscape(record.user_full_name)}' >
											<input type='hidden' name='rowKaishiBi' value='${su:htmlEscape(record.shiyou_kaishibi)}' >
											<input type='hidden' name='rowShuuryouBi' value='${su:htmlEscape(record.shiyou_shuuryoubi)}' >
											<input type='hidden' name='rowKukan' value='${su:htmlEscape(record.jyoushakukan)}' >
											<button type='button' name='listHenkouButton' class='btn btn-mini listHenkouButton'>変更</button><br>
										</td>
									</tr>
	</c:forEach>
								</tbody>
							</table>
						</div>
</c:if>
					</section>
				</form></div><!-- main -->
				<!-- Modal -->
				<div id='dialog'></div>
			</div><!-- cotent -->
			<div id='push'></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>

$(document).ready(function(){

	
	dialogRetuserId   = $("input[name=userId]");
	dialogRetusername = $("#userName");
	dialogRetshainNo  = $("#shainNo");
	if($('#shainNo').val() == ""){
		$("input[name=userId]").val("");
	}
	
	/**  社員番号ロストフォーカス時 */
	$('#shainNo').change(function(event) {
		commonShainNoLostFocus(dialogRetshainNo, dialogRetusername, "ユーザー", dialogRetuserId, false);
	});
	
	/** ユーザー選択ボタン押下時Function */
	$('button[name=userSentakuButton]').click(function(event) {
		commonUserSentaku();
	});
	
	/** 検索ボタン押下時Function */
	$('button[name=kensakuButton]').click(function(e){
		var formObject = document.getElementById("teikiKensakuForm");
		formObject.method = 'get';
		formObject.action = 'teiki_jouhou_ichiran_kensaku';
		$(formObject).submit();
	});
	
	/** 追加ボタン押下時Function */
	$('button[name=tsuikaButton]').click(function(e){
		var formObject = document.getElementById("teikiKensakuForm");
		formObject.method = 'post';
		formObject.action = 'teiki_jouhou_tsuika';
		$(formObject).submit();
	});
	
	/** 一括延長ボタン押下時Function */
	$('button[name=ikkatsuEnchoButton]').click(function(e){
		
		$("input[name=kensakuUserId]").val($("input[name=userId]").val())
		$("input[name=kensakuKaishiBi]").val($("input[name=shiyouKaishiBi]").val());
		$("input[name=kensakuShuuryouBi]").val($("input[name=shiyouShuuryouBi]").val());
		
		$("input[name=sentaku]").each(function(){
			if($(this).is(':checked')){
				$(this).parent().find('input[name=sentakuFlg]').val("1");
			}else{
				$(this).parent().find('input[name=sentakuFlg]').val("0");
			}
		});
		
		var formObject = document.getElementById("teikiEnchouForm");
		formObject.method = 'post';
		formObject.action = 'teiki_jouhou_ichiran_enchou';
		
		var chk = $("input[name=sentaku]:checked").length;
		if(chk == 0){
			alert("無期限延長対象を選択してください。");
		}else if(window.confirm('選択した定期を無期限有効にしますか？')) {
				$(formObject).submit();
		}
	});
	
	/** 変更ボタン押下時Function */
	$('button[name=listHenkouButton]').click(function(e){
		var formObject = document.getElementById("teikiKensakuForm");
		formObject.method = 'get';
		
		var rowUserId		= $(e.target).closest("td").find("input[name=rowUserId]").val();
		var rowKaishiBi		= $(e.target).closest("td").find("input[name=rowKaishiBi]").val();
		var rowShuuryouBi	= $(e.target).closest("td").find("input[name=rowShuuryouBi]").val();
		
		$("input[name=userId]").val(rowUserId);
		$("input[name=shiyouKaishiBi]").val(rowKaishiBi);
		$("input[name=shiyouShuuryouBi]").val(rowShuuryouBi);
		
		formObject.action = 'teiki_jouhou_henkou';
		$(formObject).submit();
	});

});

		</script>
	</body>
</html>
