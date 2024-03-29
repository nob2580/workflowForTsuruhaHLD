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
		<title>定期情報変更｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>定期情報変更</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='myForm' class='form-horizontal' method='post'>
					<input type="hidden" name='denpyouKbn' value='A006'>
					<input type="hidden" name='preEventUrl' value='${su:htmlEscape(preEventUrl)}'>

					<input type="hidden" name='orgUserId' value='${su:htmlEscape(orgUserId)}'>
					<input type="hidden" name='orgKaishiBi' value='${su:htmlEscape(orgKaishiBi)}'>
					<input type="hidden" name='orgShuuryouBi' value='${su:htmlEscape(orgShuuryouBi)}'>

					<!-- 入力フィールド -->
					<section class='print-unit'>
						<div class='form-horizontal'>
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>ユーザー</label>
								<div class='controls'>
									<input type='text' name="shainNo" class='input-medium pc_only' value='${su:htmlEscape(shainNo)}'>
									<input type='text' name="userName" class='input-xlarge' disabled value='${su:htmlEscape(userName)}'>
									<input type='hidden' name="userId" value='${su:htmlEscape(userId)}'>
									<button type='button' name='userSentakuButton' class='btn btn-small'>選択</button>
								</div>
							</div>
							
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>使用期間</label>
								<div class='controls'>
<c:forEach var="shiyouKikanList" items="${shiyouKikanList}">
									<label class='radio inline'><input type='radio' name='shiyouKikanKbn' value='${su:htmlEscape(shiyouKikanList.naibu_cd)}' <c:if test='${shiyouKikanList.naibu_cd eq shiyouKikanKbn}'>checked</c:if>>${su:htmlEscape(shiyouKikanList.name)}</label>
</c:forEach>
								</div>
							</div>
							
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>使用開始日</label>
								<div class='controls'>
									<input type='text' name='shiyouKaishiBi' class='input-small datepicker' value='${su:htmlEscape(shiyouKaishiBi)}'>
									<label class="label label-sub"> <span class='required'>*</span>使用終了日</label>
									<input type='text' name='shiyouShuuryouBi' class='input-small datepicker' value='${su:htmlEscape(shiyouShuuryouBi)}' disabled>
								</div>
							</div>
							
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>乗車区間</label>
								<div class='controls'>
									<textarea name='jyoushaKukan' class='input-block-level' disabled >${su:htmlEscape(jyoushaKukan)}</textarea>
									<input type='hidden' name='teikiSerializeData' value='${su:htmlEscape(teikiSerializeData)}'>
								</div>
							</div>
							
							<!-- 経路検索 -->
							<section id='norikaeannai' class='non-print nonChkDirty'>
								<button type='button' class='btn btn-small' name = 'koutsuuhiNorikaeannaiButton' onclick="norikaeannaiKensakuShow()">乗換案内検索</button>
							</section>
							
							<!-- 処理ボタン -->
							<section>
								<button type='button' class='btn' onClick="eventBtn('update')"><i class='icon-refresh'></i> 変更</button>
								<button type='button' class='btn' onClick="eventBtn('delete')"><i class='icon-remove'></i> 削除</button>
							</section>
							
							
						</div>
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
		<script style='text/javascript'>

		
/**
 * 使用終了日計算Function(呼出用)
 */
 function callShiyouShuuryouHenkou() {
	var kaishi = $("input[name=shiyouKaishiBi]").val();
	var addMonths = Number($("input[name=shiyouKikanKbn]:checked").val());
	
	//「無期限」選択時は9999/12/31とする
	var chkTxt = $("input[name=shiyouKikanKbn]:checked").parent().text();
	if(chkTxt == "無期限"){
		$("input[name=shiyouShuuryouBi]").val("9999/12/31");
		return;
	}
	$("input[name=shiyouShuuryouBi]").val(shiyouShuuryouHenkou(kaishi,addMonths));
}


/**
 * イベントボタン押下時のアクションの切り替え
 */
 function eventBtn(eventName) {
	var formObject = document.getElementById("myForm");
	
	switch (eventName) {
	case 'update':
		formObject.action = 'teiki_jouhou_henkou_henkou';
		$(formObject).submit();
		break;
	case 'delete':
		formObject.action = 'teiki_jouhou_henkou_sakujo';
		var usrid = $("input[name=orgUserId]").val();
		var frm = $("input[name=orgKaishiBi]").val();
		var to = $("input[name=orgShuuryouBi]").val();
		if(window.confirm('[' + usrid + ']['+ frm + '～'+ to +']の定期券情報を削除してよろしいですか？')) {
			$(formObject).submit();
		}
		break;
	}
}

 /**
  * 交通費（乗換案内検索）ボタン押下時Function
  */
 function norikaeannaiKensakuShow() {
 	var width = "800";
 	var height = "520";
 	var cls = "";
 	if(windowSizeChk()) {
 		width = $(window).width() * 0.9;
 		cls = "norikaeKensakuPhone";
 	}
 	height = $(window).height();
 	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: height,
		title: "乗換案内検索",
		dialogClass: cls,
		close:function(e){$(".expGuiConditionPc").remove();},
		buttons: {閉じる: function() {$(this).dialog("close");}}
	});
 	$("#dialog").load("norikae_annai_kensaku?teikiMode=true");
 	return false;
 	
 }

$(document).ready(function(){
	
	var kaishi = $("input[name=shiyouKaishiBi]").val();
	var syuryo = $("input[name=shiyouShuuryouBi]").val();
	if(syuryo == "9999/12/31"){
		//無期限
		$('input[name=shiyouKikanKbn]').eq(0).prop('checked', true);
	}else{
		var checkInd = calcShiyouKikan(kaishi,syuryo);
		$('input[name=shiyouKikanKbn]').eq( checkInd + 1 ).prop('checked', true);
	}
	
	dialogRetuserId   = $("input[name=userId]");
	dialogRetusername = $("input[name=userName]");
	dialogRetshainNo  = $("input[name=shainNo]");
	
	/**  社員番号ロストフォーカス時 */
	$('input[name=shainNo]').change(function(event) {
		commonShainNoLostFocus(dialogRetshainNo, dialogRetusername, "ユーザー", dialogRetuserId, false);
	});
	
	/** ユーザー選択ボタン押下時Function */
	$('button[name=userSentakuButton]').click(function(event) {
		commonUserSentaku();
	});
	
	//使用区分ラジオまたは使用開始日変更時、期間終了日は自動で入る
	$("input[name=shiyouKikanKbn]").change(function(){
		callShiyouShuuryouHenkou();
	});
	$("input[name=shiyouKaishiBi]").change(function(){
		callShiyouShuuryouHenkou();
	});

	
});



		</script>
	</body>
</html>
