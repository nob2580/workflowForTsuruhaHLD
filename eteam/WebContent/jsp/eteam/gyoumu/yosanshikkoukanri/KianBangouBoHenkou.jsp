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
		<title>起案番号簿変更｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
<!--
-->
		</style>
	</head>
	<body <c:if test="${successful}" >onLoad="onLoadEvent();"</c:if>>
		<div id='wrap'>

			<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
				<h1>起案番号簿変更</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main' class='form-horizontal'>
				<form id="myForm" method="post" action='kian_bangou_bo_tsuika'>
					<input type='hidden' name='defaultCnt' value='${su:htmlEscape(defaultCnt)}'>

					<!-- 起案番号簿情報 -->
					<section>
						<h2>起案番号簿情報</h2>
						<div class='control-group'>
							<label class='control-label' for='bumonName'>所属部門</label>
							<div class='controls' style="padding: 5px" id='bumonName'>${su:htmlEscape(bumonName)}</div>
							<input type='hidden' name='bumonCd' value='${su:htmlEscape(bumonCd)}'>
							<input type='hidden' name='bumonName' value='${su:htmlEscape(bumonName)}'>
						</div>
						<div class='control-group'>
							<label class='control-label' for='nendo'>年度</label>
							<div class='controls' style="padding: 5px" id='nendo'>${su:htmlEscape(nendo)}</div>
							<input type='hidden' name='nendo' value='${su:htmlEscape(nendo)}'>
						</div>
						<div class='control-group'>
							<label class='control-label' for=kianbangouBo>起案番号簿</label>
							<div class='controls' style="padding: 5px" id='kianbangouBo'>${su:htmlEscape(ryakugou)} ${su:htmlEscape(kbnNaiyou)} ${su:htmlEscape(kianbangouFrom)}-${su:htmlEscape(kianbangouTo)}</div>
							<input type='hidden' name='ryakugou' value='${su:htmlEscape(ryakugou)}'>
							<input type='hidden' name='kbnNaiyou' value='${su:htmlEscape(kbnNaiyou)}'>
							<input type='hidden' name='kianbangouFrom' value='${su:htmlEscape(kianbangouFrom)}'>
							<input type='hidden' name='kianbangouTo' value='${su:htmlEscape(kianbangouTo)}'>
						</div>
						<div class='control-group'>
							<label class='control-label' for='saibanjiHyouji'><span class='required'>*</span>採番時表示</label>
							<div class='controls'>
								<select name='saibanjiHyouji' id='saibanjiHyouji' class='input-medium input-inline'>
									<c:forEach var='record' items='${saibanjiHyoujiList}'>
										<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq saibanjiHyouji}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class='control-group'>
							<label class='control-label' for='kensakujiHyouji'><span class='required'>*</span>検索時表示</label>
							<div class='controls'>
								<select name='kensakujiHyouji' id='kensakujiHyouji' class='input-small input-inline'>
									<c:forEach var='record' items='${kensakujiHyoujiList}'>
										<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq kensakujiHyouji}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div>
							<button type="button" id='btnTourokuKoushin' class='btn' onClick='koushinBtnEvent();'><i class='icon-refresh'></i> 更新</button>
							<button type='button' class='btn' onclick='closeBtnEvent();'><i class='icon-folder-close'></i> 閉じる</button>
						</div>
					</section>
				</form>
				</div><!-- main -->
				<!-- Modal -->
				<div id='dialog'></div>
			</div><!-- content -->
			<div id="push"></div>
			</div><!-- wrap -->
		
			<!-- フッター -->
			<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>

function onLoadEvent(){
	// 親画面のリロード：起案番号を変更したのでリロードで情報を再読込する
	try {
		var parentTitle = window.opener.document.title;
		if ("起案番号簿一覧｜<%=EteamSymbol.SYSTEM_NAME%>" == parentTitle) {
			window.opener.location.reload();
		}
	}catch(e){}
	closeBtnEvent();
}
function closeBtnEvent(){
	window.open('about:blank','_self').close();
}
function koushinBtnEvent(){

	// 起案番号簿表示欄に設定した値を取得する。
	var kianbangouBoHyouji = $("select[name=kianbangouBoHyouji]").find("option:selected").val();

	// 起案番号簿表示欄に設定した値がデフォルトの場合、デフォルト確認を行う。
	if ("2" === kianbangouBoHyouji){
		// 同一所属部門でデフォルト設定したレコード件数を取得する。
		var bumonCd = $("input[name=bumonCd]").val();
		var kianbangouBo = $("select[name=kianbangouBo]").find("option:selected").val();
		var nendo = $("input[name=nendo]").val();
		var defaultCnt = $("input[name=defaultCnt]");
		getDefaultCnt(bumonCd, kianbangouBo, nendo, defaultCnt, "デフォルト設定件数");
		if ("0" !== defaultCnt.val()){
			// 自身の他にデフォルト設定したレコードが存在する場合
			if (!confirm("同一所属部門で既にデフォルト指定したデータが存在します。\n本データをデフォルト指定に変更しますか？")){
				// 変更しない
				return;
			}
		}
	}

	// 更新ボタン押下
	$("#myForm").attr("action", "kian_bangou_bo_henkou_koushin");
	$("#myForm").submit();
}

/**
 * 同一所属部門デフォルト設定件数取得Function
 */
function getDefaultCnt(bumonCd, kianbangouBo, nendo, objDefaultCnt, title) {
	// 所属部門コードに入力がなければ処理しない
	if(bumonCd == "" || bumonCd == null) {
		objDefaultCnt.val("0");
		return;
	}
	// 同期実行モードで呼び出して処理完了を待つ
	$.ajax({
		async : false,
		type : "GET",
		url : "kian_bangou_bo_tsuika_default",
		data : "bumonCd=" + bumonCd + "&kianbangouBo=" + kianbangouBo + "&nendo=" + nendo,
		dataType : 'text',
		success : function(response) {
			objDefaultCnt.val(response);
			if (response == "" && title != "") {
				alert(title.replace("*","") + "が不正です。");
			}
		}
	});
}

$(document).ready(function(){

	// 所属部門コードロストフォーカス時アクション
	$("input[name=bumonCd]").blur(function(){
		dialogRetBumonCd =  $("input[name=bumonCd]");
		dialogRetBumonName =  $("input[name=bumonName]");
		commonBumonCdLostFocus(true, dialogRetBumonCd, dialogRetBumonName, "所属部門コード");
	});

	// 部門検索ボタン押下時アクション
	$("#bumonSearch").click(function(){
		dialogRetBumonCd =  $("input[name=bumonCd]");
		dialogRetBumonName =  $("input[name=bumonName]");
		commonBumonSentaku();
	});
});
		</script>
	</body>
</html>
