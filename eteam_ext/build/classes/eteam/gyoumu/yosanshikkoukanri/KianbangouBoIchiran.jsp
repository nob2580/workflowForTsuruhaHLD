<%@ page import="eteam.symbol.EteamSymbol"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!DOCTYPE html>
<html lang="ja">
	<head>
		<meta charset="utf-8">
		<title>起案番号簿一覧｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
	</head>

	<body>
    	<div id="wrap">

    		<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
  				<h1>起案番号簿一覧</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main' class='form-horizontal'>
				<form id='myForm'>

					<!-- 検索枠 -->
					<section>
						<h2>検索条件</h2>
						<div id="kensakuList" class='no-more-tables'>
							<div class='control-group'>
								<label class='control-label'>所属部門</label>
								<div class='controls'>
									<input type='text' name='kensakuBumonCd' class='input-small input-inline' value='${su:htmlEscape(kensakuBumonCd)}'>
									<input type='text' name='kensakuBumonName' class='input-medium input-inline' value='${su:htmlEscape(kensakuBumonName)}' disabled>
									<nobr><button type='button' class='btn btn-mini input-inline' id='bumonSearch'>選択</button></nobr>
									<label class='label'>年度</label>
									<input type='text' name='kensakuNendo' class='input-mini input-inline' maxlength='4' value='${su:htmlEscape(kensakuNendo)}'>
									<label class='label'>略号</label>
									<input type='text' name='kensakuRyakugou' class='input-medium input-inline' maxlength='7' value='${su:htmlEscape(kensakuRyakugou)}'>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>区分内容</label>
								<div class='controls'>
									<input type='text' name='kensakuKbnNaiyou' class='input-medium input-inline' value='${su:htmlEscape(kensakuKbnNaiyou)}'>
									<label class='label'>採番時表示</label>
									<select name='kensakuSaibanjiHyouji' class='input-medium input-inline'>
											<option></option>
										<c:forEach var='record' items='${kensakuSaibanjiHyoujiList}'>
											<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq kensakuSaibanjiHyouji}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
										</c:forEach>
									</select>
									<label class='label'>検索時表示</label>
									<select name='kensakuKensakujiHyouji' class='input-small input-inline'>
										<option></option>
										<c:forEach var='record' items='${kensakuKensakujiHyoujiList}'>
											<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq kensakuKensakujiHyouji}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class='blank'></div>
						<div id='kensakuButton'>
							<button type='button' class='btn' name='kensakuBtn' onClick="searchAction();"><i class='icon-search'></i> 検索実行</button>
							<button type='button' class='btn' name='joukenClearBtn'><i class='icon-remove-circle'></i> 条件クリア</button>
						</div>
					</section>

					<!-- 検索結果 -->
					<section>
						<h2>検索結果</h2>
						<div class='no-more-tables'>
							<input type='hidden' name='bumonCd' value='' />
							<input type='hidden' name='nendo' value='' />
							<input type='hidden' name='ryakugou' value='' />
							<input type='hidden' name='kianbangouFrom' value='' />
							<table class='table-bordered table-condensed' >
								<thead>
									<tr>
										<th><nobr>所属部門</nobr></th>
										<th><nobr>年度</nobr></th>
										<th><nobr>略号</nobr></th>
										<th><nobr>開始番号</nobr></th>
										<th><nobr>終了番号</nobr></th>
										<th><nobr>最終番号</nobr></th>
										<th><nobr>区分内容</nobr></th>
										<th><nobr>採番時表示</nobr></th>
										<th><nobr>検索時表示</nobr></th>
										<th><nobr>変更</nobr></th>
										<th><nobr>複製</nobr></th>
									</tr>
								</thead>
								<tbody id='result'>
<c:forEach var="record" items="${list}">
									<tr>
										<td><nobr>${su:htmlEscape(record.bumonNm)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.nendo)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.ryakugou)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.kianbangouFrom)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.kianbangouTo)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.kianbangouLast)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.kbnNaiyou)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.saibanjiHyoujiNm)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.kensakuHyoujiNm)}</nobr></td>
										<td><nobr><button type='button' class='btn' id='henkouBtn' onClick="henkouAction('${record.bumonCd}', '${record.nendo}', '${record.ryakugou}', '${record.kianbangouFrom}');"><i class='icon-refresh'></i> 変更</button></nobr></td>
										<td><nobr><button type='button' class='btn' id='copyBtn' onClick="copyAction('${record.bumonCd}', '${record.nendo}', '${record.ryakugou}', '${record.kianbangouFrom}');" data-toggle='modal' href='#kianbangouBoCopyModal'><i class='icon-repeat'></i> 複製</button></nobr></td>
									</tr>
</c:forEach>
								</tbody>
								
							</table>
						</div>
					</section>
					<!-- Modal -->
					<div id='kianbangouBoCopyModal' class='modal hide fade' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'></div>
					<div id='dialog'></div>
				</form>
				</div><!-- main -->
			</div> <!-- /container -->
			<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<!-- スクリプト -->
		<script style='text/javascript'>

$(document).ready(function(){

	// 所属部門コードロストフォーカス時アクション
	$("input[name=kensakuBumonCd]").blur(function(){
		dialogRetBumonCd =  $("input[name=kensakuBumonCd]");
		dialogRetBumonName =  $("input[name=kensakuBumonName]");
		commonBumonCdLostFocus(true, dialogRetBumonCd, dialogRetBumonName, "所属部門コード");
	});

	// 部門検索ボタン押下時アクション
	$("#bumonSearch").click(function(){
		dialogRetBumonCd =  $("input[name=kensakuBumonCd]");
		dialogRetBumonName =  $("input[name=kensakuBumonName]");
		commonBumonSentaku();
	});

	// 条件クリアボタン押下
	$("button[name=joukenClearBtn]").click(function(e){
		inputClear($(this).closest("form"));
	});
});

/**
 * 検索処理
 */
function searchAction() {
	var objForm = $("#myForm");
	objForm.attr("action", "kianbangou_bo_ichiran");
	objForm.attr("method", "get");
	objForm.attr("target", "_self");
	objForm.submit();
}

/**
 * 変更画面遷移
 * @param bumonCd 部門コード
 * @param nendo 年度
 * @param ryakugou 略号
 * @param fromNum 開始起案番号
 */
function henkouAction(bumonCd, nendo, ryakugou, fromNum) {

	// 画面表示のためのキー項目を非表示項目に設定する。
	$("input[name=bumonCd]").val(bumonCd);
	$("input[name=nendo]").val(nendo);
	$("input[name=ryakugou]").val(ryakugou);
	$("input[name=kianbangouFrom]").val(fromNum);

	// 変更画面に遷移する。
	var objForm = $("#myForm");
	objForm.attr("action", "kian_bangou_bo_henkou");
	objForm.attr("method", "get");
	objForm.attr("target", "_blank");
	objForm.submit();
}

/**
 * 起案番号簿複製ダイアログ表示
 * @param bumonCd 部門コード
 * @param nendo 年度
 * @param ryakugou 略号
 * @param fromNum 開始起案番号
 */
function copyAction(bumonCd, nendo, ryakugou, fromNum) {
 	// 画面表示のためのキー項目を非表示項目に設定する。 
 	$("input[name=bumonCd]").val(bumonCd);
 	$("input[name=nendo]").val(nendo);
 	$("input[name=ryakugou]").val(ryakugou);
 	$("input[name=kianbangouFrom]").val(fromNum);

 	// ダイアログ表示コード
 	var addHtml = "<div class='modal-header'>"
				+	"<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>×<\/button>"
				+	"<h3 id='myModalLabel'>起案番号簿複製<\/h3>"
				+ "<\/div>"
				+ "<div class='modal-body'><p>複製先年度<\/p>"
				+	"<input type='text' class='date input-small datepicker' name='copyNendo' value=''>"
				+ "<\/div>"
				+ "<div class='modal-footer'>"
				+	"<button class='btn' data-dismiss='modal' aria-hidden='true'>閉じる<\/button>"
				+	"<button type='button' class='btn btn-primary' onClick='copyJisshi()'>複製実施<\/button>"
				+ "<\/div>"
				;

	var objModal = $("#kianbangouBoCopyModal");
	objModal.empty();
	objModal.append(addHtml);
	objModal.find("input.datepicker:enabled:not([readonly])").attr("id", "").removeClass('hasDatepicker').datepicker();
	objModal.find("input.datepicker:enabled:not([readonly])").datepicker();

	// 複製先年度には＋１年した値を初期表示する。
	$("input[name=copyNendo]").val(parseInt(nendo, 10) + 1);
}

/**
 * 起案番号簿複製実施
 */
function copyJisshi() {
	// 起案番号簿を複製する。 
	var objForm = $("#myForm");
	objForm.attr("action", "kianbangou_bo_ichiran_copy");
	objForm.attr("method", "get");
	objForm.attr("target", "_self");
	objForm.submit();
}
		</script>
	</body>
</html>
