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
		<title>承認処理権限登録｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
			<div id='content' class='container'><form id='myForm' method='post' action='gougi_busho_ichiran' class='form-horizontal'>

				<!-- タイトル -->
				<h1>承認処理権限登録</h1>
				
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- 一覧 -->
				<div class='no-more-tables'>
				<table class="table-bordered table-condensed">
					<tbody>
						<tr>
							<th><label class="label">承認処理権限名</label></th>
							<th><label class="label">基本モデル</label></th>
							<th><label class="label">承認必須</label></th>
							<th><label class="label">承認権</label></th>
							<th><label class="label">申請書変更</label></th>
							<th><label class="label">説明</label></th>
							<th><label class="label">承認文言</label></th>
							<th><label class="label">凡例表示</label></th>
							<th></th>
						</tr>
					</tbody>
					<tbody id='shoriKengenList'>
<c:forEach var="record" items="${shoriKengenList}" varStatus="status">
						<tr>
							<td><input type='text' name='shouninShoriKengenName' class='input-medium input-inline' maxlength=6 value='${su:htmlEscape(record.shounin_shori_kengen_name)}'></td>
							<td>
								<select name='kihonModelCd' class='input-small input-inline'>
								<c:forEach var="list" items="${kihonModeList}">
									<option value='${list.naibu_cd}' <c:if test='${record.kihon_model_cd eq list.naibu_cd}'>selected</c:if>>${su:htmlEscape(list.name)}</option>
								</c:forEach>
								</select>
							</td>
							<td align='center' ><input class='check-group' type='checkbox' name='shouninHissu' <c:if test='${"1" eq record.shounin_hissu_flg}'>checked</c:if> value="1" ></td>
							<td align='center' ><input class='check-group' type='checkbox' name='shouninKen' <c:if test='${"1" eq record.shounin_ken_flg}'>checked</c:if> value="1" ></td>
							<td align='center' ><input class='check-group' type='checkbox' name='henkou' <c:if test='${"1" eq record.henkou_flg}'>checked</c:if> value="1" ></td>
							<td><textarea name='setsumei' class='input-xlarge textarea' maxlength=100 >${su:htmlEscape(record.setsumei)}</textarea></td>
							<td><input type='text' name='shouninMongon' class='input-small input-inline' maxlength=6 value='${su:htmlEscape(record.shounin_mongon)}'></td>
							<td>
								<select name='hanreiHyoujiCd' class='input-small input-inline'>
								<c:forEach var="list" items="${hanreiHyoujiList}">
									<option value='${list.naibu_cd}' <c:if test='${record.hanrei_hyouji_cd eq list.naibu_cd}'>selected</c:if>>${su:htmlEscape(list.name)}</option>
								</c:forEach>
								</select>
							</td>
							<td>
								<button type='button' name='meisaiUp' class='btn btn-mini'>↑</button>
								<button type='button' name='meisaiDown' class='btn btn-mini'>↓</button>
								<button type='button' name='maisaiAdd' class='btn btn-mini'>+</button>
								<button type='button' name='meisaiDel' class='btn btn-mini'>-</button>
								<input type="hidden" name='shouninShoriKengenNo' value='${su:htmlEscape(record.shounin_shori_kengen_no)}'>
								<input type="hidden" name="shouninHissuFlg" value='${su:htmlEscape(record.shounin_hissu_flg)}'>
								<input type="hidden" name="shouninKenFlg" value='${su:htmlEscape(record.shounin_ken_flg)}'>
								<input type="hidden" name="henkouFlg" value='${su:htmlEscape(record.henkou_flg)}'>
							</td>
						</tr>
</c:forEach>
					</tbody>
				</table>
				</div>
				<section>
					<button type='button' onClick="eventBtn('touroku')" class='btn'><i class='icon-refresh'></i> 登録</button>
				</section>
			</form></div><!-- content -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>

/**
 * チェックボックス表示制御
 */
function kihonModelBoxSeigyo() {
	switch($(this).val()){
		// 承認者
		case "1":
			if ($(this).closest("tr").find("input[name=shouninHissu]").prop('checked')) {
				$(this).closest("tr").find("input[name=shouninHissu]").removeAttr('disabled');
				$(this).closest("tr").find("input[name=shouninKen]").removeAttr('disabled');
				$(this).closest("tr").find("input[name=henkou]").removeAttr('disabled');
			} else {
				$(this).closest("tr").find("input[name=shouninHissu]").removeAttr('disabled');
				$(this).closest("tr").find("input[name=shouninKen]").attr('disabled', 'disabled');
				$(this).closest("tr").find("input[name=henkou]").attr('disabled', 'disabled');
			}
		break;
		// 閲覧者
		case "2":
			$(this).closest("tr").find("input[name=shouninKen]").prop('checked', false);
			$(this).closest("tr").find("input[name=henkou]").prop('checked', false);
			$(this).closest("tr").find("input[name=shouninHissu]").removeAttr('disabled');
			$(this).closest("tr").find("input[name=shouninKen]").attr('disabled', 'disabled');
			$(this).closest("tr").find("input[name=henkou]").attr('disabled', 'disabled');
		break;
		// 後伺い
		case "3":
			$(this).closest("tr").find("input[name=shouninHissu]").prop('checked', true);
			$(this).closest("tr").find("input[name=shouninKen]").prop('checked', false);
			$(this).closest("tr").find("input[name=shouninHissu]").attr('disabled', 'disabled');
			$(this).closest("tr").find("input[name=shouninKen]").attr('disabled', 'disabled');
			$(this).closest("tr").find("input[name=henkou]").removeAttr('disabled');
		break;
	}
}

function shouninCheckboxSeigyo() {
	var kihonModel = $(this).closest("tr").find("select[name=kihonModelCd]").val();
	if (kihonModel == "1") {
		if ($(this).prop('checked')) {
			$(this).closest("tr").find("input[name=shouninKen]").removeAttr('disabled');
			$(this).closest("tr").find("input[name=henkou]").removeAttr('disabled');
		} else {
			$(this).closest("tr").find("input[name=shouninKen]").prop('checked', false);
			$(this).closest("tr").find("input[name=henkou]").prop('checked', false);
			$(this).closest("tr").find("input[name=shouninKen]").attr('disabled', 'disabled');
			$(this).closest("tr").find("input[name=henkou]").attr('disabled', 'disabled');
		}
	}
}

/**
 * 明細↑ボタン押下時Function
 */
function meisaiUp() {
	var tr = $(this).closest("tr");
	swapRow(tr);
}

/**
 * 明細↓ボタン押下時Function
 */
function meisaiDown() {
	var tr = $(this).closest("tr");
	if (0 < tr.nextAll(':first').length) {
		swapRow(tr.nextAll(':first'));
	}
}

/**
 * 指定された行を直前の行と入れ替える
 * @param tr	指定行TR
 */
function swapRow(tr) {
	if (0 < tr.prevAll(':first').length) {
		var base = tr.prevAll(':first');
		var targ = tr;
		var next = targ.next();
		targ.insertBefore(base);
		targ = next;
	}
}

/**
 * 役割+ボタン押下時Function
 */
function maisaiAdd() {
	var _tr = $("#shoriKengenList").find("tr:first").clone();
	$("#shoriKengenList").append(_tr);
	var tr = $("#shoriKengenList").find("tr:last");
	inputClear(tr);
	tr.find("input[name=shouninShoriKengenNo]").val("");
	tr.find("input[name=shouninHissuFlg]").val("");
	tr.find("input[name=shouninKenFlg]").val("");
	tr.find("input[name=henkouFlg]").val("");
	tr.find("input[name=shouninHissu]").removeAttr('disabled');
	tr.find("input[name=shouninKen]").attr('disabled', 'disabled');
	tr.find("input[name=henkou]").attr('disabled', 'disabled');
}

/**
 * 役割-ボタン押下時Function
 */
function meisaiDel() {
	if(1 == $("#shoriKengenList tr").length) {
		inputClear($(this).closest("tr"));
		$(this).closest("tr").find("input[name=shouninShoriKengenNo]").val("");
		$(this).closest("tr").find("input[name=shouninHissuFlg]").val("");
		$(this).closest("tr").find("input[name=shouninKenFlg]").val("");
		$(this).closest("tr").find("input[name=henkouFlg]").val("");
	} else {
		$(this).closest("tr").remove();
	}
}

//初期表示処理
$(document).ready(function(){
	//承認者アクション紐付け
	$("body").on("change","select[name=kihonModelCd]",kihonModelBoxSeigyo); 
	$("body").on("change","input[name=shouninHissu]",shouninCheckboxSeigyo); 
	$("body").on("click","button[name=meisaiUp]",meisaiUp); 
	$("body").on("click","button[name=meisaiDown]",meisaiDown); 
	$("body").on("click","button[name=maisaiAdd]",maisaiAdd); 
	$("body").on("click","button[name=meisaiDel]",meisaiDel); 
	//基本モデルによるチェックボックス表示可否
	$("#shoriKengenList").find("select[name=kihonModelCd]").each(kihonModelBoxSeigyo);
	//承認者のチェックのよるチェックボックス表示可否
	$("#shoriKengenList").find("input[name=shouninHissu]").each(shouninCheckboxSeigyo);
});

/**
 * イベントボタン押下時のアクションの切り替え
 */
 function eventBtn(eventName) {
	var formObject = document.getElementById("myForm");
	
	// チェックボックスがチェックされない時でもvalueを当てはめる
	$('#shoriKengenList tr').each(function(i, tr){
		if ($(tr).find('[name=shouninHissu]').is(':checked')){
			$(tr).find("[name=shouninHissuFlg]").val("1");
		} else {
			$(tr).find("[name=shouninHissuFlg]").val("0");
		}
		if ($(tr).find('[name=shouninKen]').is(':checked')){
			$(tr).find("[name=shouninKenFlg]").val("1");
		} else {
			$(tr).find("[name=shouninKenFlg]").val("0");
		}
		if ($(tr).find('[name=henkou]').is(':checked')){
			$(tr).find("[name=henkouFlg]").val("1");
		} else {
			$(tr).find("[name=henkouFlg]").val("0");
		}

	});
	
	formObject.action = 'shounin_shori_kengen_touroku';
	$(formObject).submit();
}
		</script>
	</body>
</html>
