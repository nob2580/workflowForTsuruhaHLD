<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!-- 注意点　項目を再表示させないようにするときはclassにnever_showを使用すること -->
<div id='kaikeiContent' class='form-horizontal'>
	<section class='print-unit' id="shinseiSection">
		<h2>申請内容</h2>
		<div>
			<!-- 仮払選択 -->
			<div class='control-group hissu-group' <c:if test='${not karibaraiIsEnabled}'>style='display:none;'</c:if>>
				<label class='control-label'>${su:htmlEscape(ks1.karibaraiSentaku.name)}</label>
				<div class='controls'>
<c:if test='${"1" eq userKaribaraiUmuFlg && "0" eq dairiFlg }'><button type='button' id='karibaraiSentakuButton' class='btn btn-small nonChkDirty'>データあり</button></c:if>
					<div id='karibaraiAnken' class='no-more-tables karibaraiAnken'>
						<!-- 伝票ID／摘要／金額／仮払金未使用 -->
				 		<table class='table-bordered table-condensed non-print' style='margin-bottom:5px;'>
							<thead>
								<tr>
									<th>${su:htmlEscape(ks1.karibaraiDenpyouId.name)}</th>
									<th>${su:htmlEscape(ks1.karibaraiOn.name)}</th>
									<th style='width: 200px'>${su:htmlEscape(ks1.karibaraiTekiyou.name)}</th>
									<th id='karibaraiShinseiKingakuHyouji'>${su:htmlEscape(ks1.shinseiKingaku.name)}</th>
									<th <c:if test='${not ks1.karibaraiKingakuSagaku.hyoujiFlg}'>style='display:none;'</c:if>>${su:htmlEscape(ks1.karibaraiKingakuSagaku.name)}</th>
									<th id='karibaraiKingakuHyouji'>${su:htmlEscape(ksKari.kingaku.name)}</th>
									<th>${su:htmlEscape(ks1.karibaraiMishiyouFlg.name)}</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td id='karibaraiDenpyouIdHyouji'><a href='karibarai_shinsei?denpyouId=${su:htmlEscape(karibaraiDenpyouId)}&denpyouKbn=A002' target='_blank'>${su:htmlEscape(karibaraiDenpyouId)}</a></td>
									<td id='karibaraiOnHyouji'>${su:htmlEscape(karibaraiOn eq "1"?"あり":"なし")}</td>
								 	<td id='karibaraiTekiyouHyouji'>${su:htmlEscape(karibaraiTekiyou)}</td>
									<td id='karibaraiShinseiKingakuHyouji'>${su:htmlEscape(karibaraiShinseiKingaku)}</td>
									<td id='karibaraiKingakuSagakuHyouji' <c:if test='${not ks1.karibaraiKingakuSagaku.hyoujiFlg}'>style='display:none;'</c:if>>${su:htmlEscape(karibaraiKingakuSagaku)}</td>
									<td id='karibaraiKingakuHyouji'>${su:htmlEscape(karibaraiKingaku)}</td>
									<td style="text-align:center"><input type='checkbox' id='karibaraiMishiyouFlg' name='karibaraiMishiyouFlg' value='1' <c:if test='${1 eq karibaraiMishiyouFlg}'>checked</c:if>></td>
								</tr>
							</tbody>
						</table>
						<!-- 伝票ID／金額 -->
				 		<table class='table-bordered table-condensed print_only' style='margin-bottom:5px; border-collapse: collapse;'>
							<tr>
								<th>${su:htmlEscape(ks1.karibaraiDenpyouId.name)}</th>
								<td id='karibaraiDenpyouIdHyouji_print'>${su:htmlEscape(karibaraiDenpyouId)}</td>
								<th id='karibaraiKingakuHyouji_print'>${su:htmlEscape(karibaraiOn eq "1"?ksKari.kingaku.name:ks1.shinseiKingaku.name)}</th>
								<td id='karibaraiKingakuHyouji_print'>${su:htmlEscape(karibaraiOn eq "1"?karibaraiKingaku:karibaraiShinseiKingaku)}</td>
							</tr>
						</table>
						<input type='hidden' name='karibaraiDenpyouId' value='${su:htmlEscape(karibaraiDenpyouId)}'>
						<input type='hidden' name='karibaraiOn' value='${su:htmlEscape(karibaraiOn)}'>
						<input type='hidden' name='karibaraiTekiyou' value='${su:htmlEscape(karibaraiTekiyou)}'>
						<input type='hidden' class='autoNumeric' name='karibaraiShinseiKingaku' value='${su:htmlEscape(karibaraiShinseiKingaku)}'>
						<input type='hidden' class='autoNumeric' name='karibaraiKingakuSagaku' value='${su:htmlEscape(karibaraiKingakuSagaku)}'>
						<input type='hidden'  class='autoNumeric' name='karibaraiKingaku' value='${su:htmlEscape(karibaraiKingaku)}'>
						<input type='hidden' name='karibaraiKianTenpuZumiFlg' value='${su:htmlEscape(karibaraiKianTenpuZumiFlg)}'>
					</div>
<c:if test='${(not enableInput && 0 eq fn:length(karibaraiDenpyouId)) || (enableInput && "0" eq userKaribaraiUmuFlg && 0 eq fn:length(karibaraiDenpyouId)) }'>データなし</c:if>
				</div>
			</div>
			<!-- 支払希望日／支払方法／支払日／計上日 -->
			<!-- 例外的に、項目レベルでnever_showを入れる -->
			<div class='control-group hissu-group' id='shiharai'>
				<label for='shiharaiLabel' class='control-label'>支払</label>
				<div class='controls'>
					<!-- 支払希望日 -->
					<label class='label <c:if test='${not ks1.shiharaiKiboubi.hyoujiFlg}'>never_show' style='display:none;</c:if>'><c:if test='${ks1.shiharaiKiboubi.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.shiharaiKiboubi.name)}</label>
					<input type='text' name='shiharaiKiboubi' class='input-small datepicker <c:if test='${not ks1.shiharaiKiboubi.hyoujiFlg}'>never_show' style='display:none;</c:if>' value='${su:htmlEscape(shiharaiKiboubi)}'>
					<!-- 支払方法 -->
					<label class='label <c:if test='${not ks1.shiharaiHouhou.hyoujiFlg}'>never_show' style='display:none;</c:if>'><c:if test='${ks1.shiharaiHouhou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.shiharaiHouhou.name)}</label>
					<select id='shiharaihouhou' name='shiharaihouhou' class='input-small <c:if test='${not ks1.shiharaiHouhou.hyoujiFlg}'>never_show' style='display:none;</c:if>' <c:if test="${disableShiharaiHouhou}">disabled</c:if>>
						<c:forEach var='record' items='${shiharaihouhouList}'>
							<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq shiharaihouhou}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
						</c:forEach>
					</select>
					<!-- 支払日 --><%-- 申請後表示、経理承認時のみ変更可能 --%>
					<span id='shiharaibi' <c:if test="${shiharaiBiMode == 0}">class='never_show' style='display:none;'</c:if>>
						<label class='label'><span class='required'>*</span>支払日</label>
						<input type='text' name='shiharaibi' class='input-small datepicker' value='${su:htmlEscape(shiharaibi)}' <c:if test="${shiharaiBiMode != 1}">disabled</c:if>>
					</span>
					<!-- 計上日 --><%-- 会社設定により申請者のとき変更可能 または 申請後表示、経理承認時のみ変更可能※現金主義なら常に非表示 --%>
					<span id='keijoubi' <c:if test="${keijouBiMode == 0}">class='never_show' style='display:none;'</c:if>>
						<label class='label'><span class='required'>*</span>計上日</label>
<c:if test="${keijouBiMode == 0 || keijouBiMode == 2}">
						<input type='text' name='keijoubi' class='input-small datepicker' value='${su:htmlEscape(keijoubi)}' disabled>
</c:if>
<c:if test="${keijouBiMode == 1}">
						<input type='text' name='keijoubi' class='input-small datepicker' value='${su:htmlEscape(keijoubi)}'>
</c:if>
<c:if test="${keijouBiMode == 3}">
			            <select name='keijoubi' class='input-medium'>
							<option value=''></option>
		<c:forEach var="record" items="${keijoubiList}">
							<option value='${record}' <c:if test='${record == keijoubi}'>selected</c:if>>${record}</option>
		</c:forEach>
						</select>
</c:if>
					</span>
				</div>
			</div>
			<!-- ヘッダーフィールド -->
			<%@ include file="./kogamen/HeaderField.jsp" %>
		</div>
		<div class='control-group<c:if test='${!houjinCardRirekiEnable}'> never_show' style='display:none;</c:if>'>
			<input type='hidden' name='kihyoushaUserId' value='${su:htmlEscape(kihyoushaUserId)}'>
			<input type='hidden' name='kihyoushaUserName' value='${su:htmlEscape(kihyoushaUserName)}'>
			<input type='hidden' name='kihyoushaShainNo' value='${su:htmlEscape(kihyoushaShainNo)}'>
			<input type='hidden' name='ryoushuushoSeikyuushoDefault' value='${su:htmlEscape(ryoushuushoSeikyuushoDefault)}'>
			<button type='button' id='houjinCardRirekiAddButton' class="btn btn-small"<c:if test='${(not enableInput)}'> disabled </c:if> >法人カード履歴参照</button>
		</div>
	</section>

	<!-- 明細 -->
	<%@ include file="./kogamen/DenpyouMeisaiList.jsp" %>

	<!-- 支払金額合計／差引支給金額／消費税率 -->
	<section class='print-unit' id="goukeiSection">
		<div class='control-group'>
			<label class='control-label'><span class='required'>*</span>${su:htmlEscape(ks1.shiharaiKingakuGoukei.name)}</label>
			<div class='controls'>
				<!-- 支払金額合計 -->
				<input type='text' name='kingaku' class='input-medium autoNumericNoMax hissu' disabled value ='${su:htmlEscape(kingaku)}'>円
				<!-- 本体金額合計／消費税額合計 -->
				<span class='never_show' style='display:none;'>
					（本体金額合計<input type='text' name='hontaiKingakuGoukei' class='input-medium autoNumericNoMax never_show' disabled value='${su:htmlEscape(hontaiKingakuGoukei)}'>円
					　消費税額合計<input type='text' name='shouhizeigakuGoukei' class='input-medium autoNumericNoMax never_show' disabled value ='${su:htmlEscape(shouhizeigakuGoukei)}'>円）
				</span>
				<!-- 法人カード利用合計 -->
				<label class="label hissu <c:if test="${!houjinCardFlag}">never_show" style="display:none;</c:if>">${su:htmlEscape(ks1.uchiHoujinCardRiyouGoukei.name)}</label>
				<input type="text" name="houjinCardRiyouGoukei" class="input-medium autoNumericNoMax hissu <c:if test="${!houjinCardFlag}">never_show" style="display:none;</c:if>"  disabled value='${su:htmlEscape(houjinCardRiyouGoukei)}'><c:if test="${houjinCardFlag}">円</c:if>
				<!-- 会社手配合計 -->
				<label class="label hissu <c:if test="${!kaishaTehaiFlag}">never_show" style="display:none;</c:if>">${su:htmlEscape(ks1.kaishaTehaiGoukei.name)}</label>
				<input type="text" name="kaishaTehaiGoukei" class="input-medium autoNumericNoMax hissu <c:if test="${!kaishaTehaiFlag}">never_show" style="display:none;</c:if>"  disabled value='${su:htmlEscape(kaishaTehaiGoukei)}'><c:if test="${kaishaTehaiFlag}">円</c:if>
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label hissu'><span class='required'>*</span>${su:htmlEscape(ks1.sashihikiShikyuuKingaku.name)}</label>
			<div class='controls'>
				<!-- 差引支給金額 -->
				<input type='text' name='sashihikiShikyuuKingaku' class='input-medium autoNumericNoMax hissu' disabled value ='${su:htmlEscape(sashihikiShikyuuKingaku)}'>円
				<!-- 仮払金額 -->
				<label class="label hissu">${su:htmlEscape(ksKari.kingaku.name)}</label>
				<input type="text" name="karibaraiKingakuGoukei" class="input-medium autoNumericNoMax hissu" disabled>円
			</div>
		</div>
				<!-- インボイスで追加された合計欄  -->
		<div class='control-group' name='kingakuGoukei10Percent'>
			<label class='control-label' <c:if test='${not ks.shiharaiKingakuGoukei10Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shiharaiKingakuGoukei10Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiKingakuGoukei10Percent.name)}</label>
			<div class='controls'>
				<!-- 支払金額（10%） -->
				<c:if test='${ks.shiharaiKingakuGoukei10Percent.hyoujiFlg}'>
					<input type='text' name='shiharaiKingakuGoukei10Percent' class='input-medium autoNumericNoMax' disabled value='0'><span>円</span>
				</c:if>
				<!-- 税抜金額（10%） -->
				<label class='label' <c:if test='${not ks.zeinukiKingaku10Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.zeinukiKingaku10Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.zeinukiKingaku10Percent.name)}</label>
				<c:if test='${ks.zeinukiKingaku10Percent.hyoujiFlg}'>
					<input type="text" name="zeinukiKingaku10Percent" class="input-medium autoNumericNoMax" disabled value='0'><span>円</span>
				</c:if>
				<!-- 消費税額（10%） -->
				<label class='label' <c:if test='${not ks.shouhizeigaku10Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shouhizeigaku10Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shouhizeigaku10Percent.name)}</label>
				<c:if test='${ks.shouhizeigaku10Percent.hyoujiFlg}'>
					<input type="text" name="shouhizeigaku10Percent" class="input-medium autoNumericNoMax" disabled value='0'><span>円</span>
				</c:if>
			</div>
		</div>
		<div class='control-group' name='kingakuGoukei8Percent'>
			<label class='control-label' <c:if test='${not ks.shiharaiKingakuGoukei8Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shiharaiKingakuGoukei8Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiKingakuGoukei8Percent.name)}</label>
			<div class='controls'>
				<!-- 支払金額（*8%） -->
				<c:if test='${ks.shiharaiKingakuGoukei8Percent.hyoujiFlg}'>
					<input type='text' name='shiharaiKingakuGoukei8Percent' class='input-medium autoNumericNoMax' disabled value='0'><span>円</span>
				</c:if>
				<!-- 税抜金額（*8%） -->
				<label class='label' <c:if test='${not ks.zeinukiKingaku8Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.zeinukiKingaku8Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.zeinukiKingaku8Percent.name)}</label>
				<c:if test='${ks.zeinukiKingaku8Percent.hyoujiFlg}'>
					<input type="text" name="zeinukiKingaku8Percent" class="input-medium autoNumericNoMax" disabled value='0'><span>円</span>
				</c:if>
				<!-- 消費税額（*8%） -->
				<label class='label' <c:if test='${not ks.shouhizeigaku8Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shouhizeigaku8Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shouhizeigaku8Percent.name)}</label>
				<c:if test='${ks.shouhizeigaku8Percent.hyoujiFlg}'>
					<input type="text" name="shouhizeigaku8Percent" class="input-medium autoNumericNoMax" disabled value='0'><span>円</span>
				</c:if>
				<c:if test='${ks.shiharaiKingakuGoukei10Percent.hyoujiFlg}'><!-- とりあえず10%が表示されるなら出しておく（基本的に共通と思われるので -->
					<button type='button' id='zeigakuShousaiButton' class='btn btn-small'>消費税額詳細</button>
				</c:if>
			</div>
		</div>
	</section>

	<!-- 補足 -->
	<section class='print-unit' <c:if test='${not ks1.hosoku.hyoujiFlg}'>style='display:none;'</c:if>>
		<h2><c:if test='${ks1.hosoku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.hosoku.name)}</h2>
		<div>
			<div class='control-group'>
				<textarea name='hosoku' maxlength='240' class='input-block-level'>${su:htmlEscape(hosoku)}</textarea>
			</div>
		</div>
	</section>
</div><!-- kaikeiContent -->

<!-- スクリプト -->
<script style='text/javascript'>

/**
 * （DenpyouMeisaiList→親JSP別JS呼び出し）
 * 明細行の更新時の処理
 * 明細部の更新時（追加・変更）に伝票本体に反映する
 *
 */
function updateDenpyouMeisaiEvent(meisaiInfo) {
}

/**
 * （DenpyouMeisaiList→親JSP別JS呼び出し）
 * 明細金額から本体金額を再計算、反映
 */
function calcMoney() {
 	var hontaiKingakuGoukeiTag		= $("input[name=hontaiKingakuGoukei]");
 	var shouhizeigakuGoukeiTag		= $("input[name=shouhizeigakuGoukei]");
 	var kingakTag					= $("input[name=kingaku]");
 	var houjinCardRiyouKingakuTag	= $("input[name='houjinCardRiyouGoukei']");
 	var kaishaTehaiKingakuTag		= $("input[name='kaishaTehaiGoukei']");
 	var karibaraiKingakuGoukeiTag	= $("input[name='karibaraiKingakuGoukei']");
 	var sashihikiShikyuuKingakuTag	= $("input[name=sashihikiShikyuuKingaku]");

	//明細単位に本体・消費税分離
	//calcMeisaiMoney();

	//本体・消費税の全明細分合計を計算
	var hontaiKingakuGoukei = 0;
	var shouhizeigakuGoukei = 0;
	$.each(getMeisaiList(), function(ii, meisaiInfo) {
		hontaiKingakuGoukei += meisaiInfo["hontaiKingakuNum"];
		shouhizeigakuGoukei += meisaiInfo["shouhizeigakuNum"];
	});
	if ($("#karibaraiMishiyouFlg").prop("checked")) {
		hontaiKingakuGoukeiTag.val(0)
		shouhizeigakuGoukeiTag.val(0);
		kingakTag.val(0);
		houjinCardRiyouKingakuTag.val(0);
		kaishaTehaiKingakuTag.val(0);
	} else {
		hontaiKingakuGoukeiTag.putMoney(hontaiKingakuGoukei);
		shouhizeigakuGoukeiTag.putMoney(shouhizeigakuGoukei);
		kingakTag.putMoney(hontaiKingakuGoukei + shouhizeigakuGoukei);
		// 法人カード利用合計の算出
		var houjinCardRiyouGoukei = 0;
		for (var i = 0; i < $("[name=houjinCardFlgKeihi]").length; i++) {
			if ($("#meisaiList").find("[name=houjinCardFlgKeihi]").eq(i).val() == '1') {
				houjinCardRiyouGoukei += parseInt($("#meisaiList").find("[name=shiharaiKingaku]").eq(i).val().replaceAll(",", ""));
			}
		}
		houjinCardRiyouKingakuTag.val(String(houjinCardRiyouGoukei).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		// 会社手配合計の算出
		var kaishaTehaiGoukei = 0;
		for (var i = 0; i < $("[name=kaishaTehaiFlgKeihi]").length; i++) {
			if ($("#meisaiList").find("[name=kaishaTehaiFlgKeihi]").eq(i).val() == '1') {
				kaishaTehaiGoukei += parseInt($("#meisaiList").find("[name=shiharaiKingaku]").eq(i).val().replaceAll(",", ""));
			}
		}
		kaishaTehaiKingakuTag.val(String(kaishaTehaiGoukei).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	}

	//仮払金額
	var karibaraiKingaku = 0;
 	if ($("[name='karibaraiOn']").val() == "1") {
 		karibaraiKingaku = $("[name='karibaraiKingaku']").getMoney();
 	}
	karibaraiKingakuGoukeiTag.putMoney(karibaraiKingaku);

 	//差引支給金額
 	var sashihikiShikyuuKingaku = kingakTag.getMoney() - karibaraiKingaku - houjinCardRiyouKingakuTag.getMoney() - kaishaTehaiKingakuTag.getMoney();
 	sashihikiShikyuuKingakuTag.putMoney(sashihikiShikyuuKingaku);

 	//仮払金額の差額
 	var shinseiSagaku = $("[name='karibaraiShinseiKingaku']").getMoney() - $("[name='kingaku']").getMoney();
 	$("[name='karibaraiKingakuSagaku']").putMoney(shinseiSagaku);
 	$("td[id=karibaraiKingakuSagakuHyouji]").text($("[name='karibaraiKingakuSagaku']").val());

	if ($("#karibaraiMishiyouFlg").prop("checked")) {
		$("#meisaiField").find("botton[name=meisaiAddButton]").prop("disabled", true);
	} else {
		$("#meisaiField").find("botton[name=meisaiAddButton]").prop("disabled", false);
	}
	
	// インボイス関連（eteam.jsより読込）
	calcInvoiceKingaku();
}

//初期表示処理
$(document).ready(function(){

	// 明細表示をロード
	setDisplayMeisaiData();

	// 仮払伝票IDがなければ仮払エリアを非表示
	if($("input[name=karibaraiDenpyouId]").val().length == 0) {
		$("#karibaraiAnken").css("display", "none");
	}

	// 仮払未使用フラグがONの場合、明細部を非表示
	if ($("#karibaraiMishiyouFlg").prop("checked")) {

		// 申請内容を非表示にする
		displayShinsei(false, 0);
	}

	//仮払ありのとき、仮払未使用チェックボックスを表示
	if ($("input[name=karibaraiOn]").val() == "1") {
		$(".non-print", "div#karibaraiAnken").find("thead").find("th:last").css("display", "");
		$(".non-print", "div#karibaraiAnken").find("tbody").find("td:last").css("display", "");
	} else {
		$(".non-print", "div#karibaraiAnken").find("thead").find("th:last").css("display", "none");
		$(".non-print", "div#karibaraiAnken").find("tbody").find("td:last").css("display", "none");
	}

<c:if test='${enableInput}'>

	//仮払案件選択ボタン押下時
	$("#karibaraiSentakuButton").click(function(){
		dialogRetKaribaraiAnkenCd			= $("td[id=karibaraiDenpyouIdHyouji]");
		dialogRetKaribaraiAnkenOn 			= $("td[id=karibaraiOnHyouji]");
		dialogRetKaribaraiAnkenName 		= $("td[id=karibaraiTekiyouHyouji]");
//		dialogRetKaribaraiAnkenNote 		= $("td[id=karibaraiKingakuHyouji]");
		dialogRetShinseiKingaku 			= $("td[id=karibaraiShinseiKingakuHyouji]");
		dialogRetKaribaraiKingaku	 		= $("td[id=karibaraiKingakuHyouji]");
		dialogRetRingiHikitsugiUmFlg		= $("input[name=karibaraiRingiHikitsugiUmFlg]");
		dialogRetKianTenpuZumiFlg			= $("input[name=karibaraiKianTenpuZumiFlg]");
		dialogCallbackKaribaraiAnkenSentaku = function() {
			// テキストの内容をhiddenにも設定
			$("input[name=karibaraiDenpyouId]").val($("td[id=karibaraiDenpyouIdHyouji]").text());
			$("input[name=karibaraiOn]").val($("td[id=karibaraiOnHyouji]").text() == 'あり' ? "1":"0");
			$("input[name=karibaraiTekiyou]").val($("td[id=karibaraiTekiyouHyouji]").text());
			$("input[name=karibaraiShinseiKingaku]").val($("td[id=karibaraiShinseiKingakuHyouji]").text());
			$("input[name=karibaraiKingaku]").val($("td[id=karibaraiKingakuHyouji]").text());
			// テキストの内容をPrint用テーブルにも設定
			$("td[id=karibaraiDenpyouIdHyouji_print]").text($("td[id=karibaraiDenpyouIdHyouji]").text());
			if ($("input[name=karibaraiOn]").val() == "1") {
				$("th[id=karibaraiKingakuHyouji_print]").text($("th[id=karibaraiKingakuHyouji]").text());
				$("td[id=karibaraiKingakuHyouji_print]").text($("td[id=karibaraiKingakuHyouji]").text());
			}else{
				$("th[id=karibaraiKingakuHyouji_print]").text($("th[id=karibaraiShinseiKingakuHyouji]").text());
				$("td[id=karibaraiKingakuHyouji_print]").text($("td[id=karibaraiShinseiKingakuHyouji]").text());

			}

			//仮払ありのとき、仮払未使用チェックボックスを表示
			if ($("input[name=karibaraiOn]").val() == "1") {
				$(".non-print", "div#karibaraiAnken").find("thead").find("th:last").css("display", "");
				$(".non-print", "div#karibaraiAnken").find("tbody").find("td:last").css("display", "");

				//仮払申請が別伝票で起案添付済みのとき、仮払金未使用チェックボックスを変更不可にする
				if ($("input[name=karibaraiKianTenpuZumiFlg]").val() == "1") {
					$("#karibaraiMishiyouFlg").prop("disabled", true);
				} else {
					$("#karibaraiMishiyouFlg").prop("disabled", false);
				}

			} else {
				$(".non-print", "div#karibaraiAnken").find("thead").find("th:last").css("display", "none");
				$(".non-print", "div#karibaraiAnken").find("tbody").find("td:last").css("display", "none");
			}
			calcMoney();

			//稟議金額超過コメントの表示制御
			ringiKoumokuDisplaySeigyoMikihyou();
		};
		commonKaribaraiAnkenSentaku($("input[name=denpyouKbn]").val(), $("input[name=denpyouId]").val(), "");
	});

	$("#karibaraiMishiyouFlg").click(function(){

		// 申請内容の表示
		displayShinsei(!$(this).prop("checked"), 300);

		// 金額計算
		calcMoney();
	});

	//仮払申請が別伝票で起案添付済みのとき、仮払金未使用チェックボックスを変更不可にする
	if ($("input[name=karibaraiOn]").val() == "1" && $("input[name=karibaraiKianTenpuZumiFlg]").val() == "1") {
		$("#karibaraiMishiyouFlg").prop("disabled", true);
	}

	//法人カード履歴参照ボタン
	$("#houjinCardRirekiAddButton").click(function(){
		houjinCardRirekiSentakuDialogOpen();
	});

</c:if>

<c:if test='${not enableInput}'>
	//起票モードの場合のみ入力や選択可能
	$("#kaikeiContent").find("button").css("display", "none");
	$("#kaikeiContent").find("input,textarea,select").prop("disabled", true);
</c:if>

//消費税額詳細ボタンクリック
$('#zeigakuShousaiButton').on('click', function() {
    commonShouhizeiShousai($("input[name=denpyouKbn]").val());
  });
  
});

/**
 * イベントボタン押下時のアクションの切り替え
 */
/*function eventBtn(eventName) {
	var formObject = document.getElementById("workflowForm");

	// 支払日チェック
	var checkShiharaiBi = ($("input[name=shiharaibi]").prop("disabled") == false);

	//支払日更新チェック
	if (checkShiharaiBi) {
		var shiharaiBi = $("input[name=shiharaibi]").val();
		if ("" == shiharaiBi) {
			alert("支払日が未入力です。\n支払日を入力して、更新してください。");
			return;
		}
		if ($("#keijoubi").css("display") != "none") {
			var keijouBi = $("input[name=keijoubi]").val();
			if ("" == keijouBi) {
				alert("計上日が未入力です。\n計上日を入力して、更新してください。");
				return;
			}
		}
	}

	switch (eventName) {
	case 'entry':

		formObject.action = 'keihi_tatekae_seisan_touroku';
		break;
	case 'update':
		formObject.action = 'keihi_tatekae_seisan_koushin';
		break;
	case 'shinsei':
		formObject.action = 'keihi_tatekae_seisan_shinsei';
		break;
	case 'shounin':
		formObject.action = 'keihi_tatekae_seisan_shounin';
		break;
	}

	formObject.method = 'post';
	formObject.target = '_self';
	/*20220415*/
	/*var child = formObject.childNodes;
	for(var i = 0;i<child.length;i++){

	}*/
	/*var cnt = LoopChilds(formObject);
	if(cnt > 0){
		alert("必須未入力");
	}
	$(formObject).submit();
}
*/

function eventBtn(eventName){
	var formObject = document.getElementById("workflowForm");

	// 支払日チェック
	var checkShiharaiBi = ($("input[name=shiharaibi]").prop("disabled") == false);

	//支払日更新チェック
	if (checkShiharaiBi){
		var shiharaiBi = $("input[name=shiharaibi]").val();
		if ("" == shiharaiBi){
			//20220517 アラートのOK押した結果をキャッチして制御する
			if(!alert("支払日が未入力です。\n支払日を入力して、更新してください。")){
				$(formObject).find('button[type=button]').not('#chiharaibiCheckNotPass').removeAttr("disabled");
			}
			return;
		}
		if ($("#keijoubi").css("display") != "none"){
			var keijouBi = $("input[name=keijoubi]").val();
			if ("" == keijouBi){
				//20220517
				if(!alert("計上日が未入力です。\n計上日を入力して、更新してください。")){
					$(formObject).find('button[type=button]').not('#chiharaibiCheckNotPass').removeAttr("disabled");
				}
				//alert("計上日が未入力です。\n計上日を入力して、更新してください。");
				return;
			}
		}
	}

	switch (eventName){
	case 'entry':
		/*20220415 ここで入力必須項目ぐらいはチェックできるかも*/
		formObject.action = 'keihi_tatekae_seisan_touroku';
		break;
	case 'update':
		formObject.action = 'keihi_tatekae_seisan_koushin';
		break;
	case 'shinsei':
		formObject.action = 'keihi_tatekae_seisan_shinsei';
		break;
	case 'shounin':
		formObject.action = 'keihi_tatekae_seisan_shounin';
		break;
	case 'shouninkaijo':
		formObject.action = 'keihi_tatekae_seisan_shouninkaijo';
		break;
	}

	formObject.method = 'post';
	formObject.target = '_self';
	$(formObject).submit();
}

/**
 * 仮払未使用チェックに連動した表示切替
 * @param isDisp	チェックなしならtrue
 * @param speed		表示時間
 */
function displayShinsei(isDisp, speed) {

	//ヘッダー部分
	var section = $("#shinseiSection");
	var divCtrlGrpList  = section.find("div.control-group");
	for (var i = 0; i < divCtrlGrpList.length; i++) {
		var divCtrlGrp = divCtrlGrpList.eq(i);

		//チェックOFF→元々非表示の行(never_show)除いて表示
		if (isDisp) {
			if(!(divCtrlGrp.hasClass("never_show"))){
				divCtrlGrp.show(speed);
			}
		//チェックON→hissu-groupの列(仮払選択、支払)以外は非表示
		} else {
			if (!divCtrlGrp.hasClass("hissu-group")) {
				divCtrlGrp.hide(speed);
			}
		}
	}

	//ヘッダー部(支払列)
	var divShiharai = $("#shiharai");
	var divShiharaiChildren = divShiharai.find(".controls").children();
	for (var i = 0; i < divShiharaiChildren.length; i++) {
		var divShiharaiChild = divShiharaiChildren.eq(i);
		//チェックOFF→元々表示していた項目(never_showなし)を表示
		if (isDisp) {
			if (! divShiharaiChild.hasClass("never_show")) {
				divShiharaiChild.show(0);
			}
			divShiharai.show(speed);
		//チェックON→支払日・計上日以外非表示、支払日・計上日は元々表示していたなら表示、支払日非表示なら支払ラベルごと非表示
		} else {
			if (divShiharaiChild.prop("id") != "shiharaibi" && divShiharaiChild.prop("id") != "keijoubi") {
				divShiharaiChild.hide(0);
			}
			if (divShiharaiChild.prop("id") == "shiharaibi") {
				if (divShiharaiChild.hasClass("never_show")) {
					divShiharaiChild.hide(0);
					divShiharai.hide(speed);
				}
			}
		}
	}

	//明細部
	//チェックOFF→表示
	if (isDisp) {
		$("#meisaiField").show(speed);
	//チェックON→非表示
	} else {
		$("#meisaiField").hide(speed);
	}

	//合計部
	var sectionGoukek = $("#goukeiSection");
	var divCtrls  = sectionGoukek.find("div.controls");
	var divCtrlsChildren = divCtrls.children();
	var div10per = $("[name=kingakuGoukei10Percent]");
	var div8per = $("[name=kingakuGoukei8Percent]");
	for (var i = 0; i < divCtrlsChildren.length; i++) {
		var divCtrlsChild = divCtrlsChildren.eq(i);
		//チェックOFF→元々表示していた項目(never_showなし)を表示
		if (isDisp) {
			if (! divCtrlsChild.hasClass("never_show")) {
				divCtrlsChild.show(0);
				div10per.show(0);
				div8per.show(0);
			}
		//チェックON→hissuが付いていない項目(本体・消費税)を非表示
		} else {
			if (!divCtrlsChild.hasClass("hissu")) {
				divCtrlsChild.hide(speed);
				div10per.hide(speed);
				div8per.hide(speed);
			}
		}
	}
}

/**
 * 法人カード履歴選択ダイアログを開く
 */
function houjinCardRirekiSentakuDialogOpen() {
	var denpyouId = $("[name=denpyouId]").val();//未登録ならブランク、登録後なら入る
	var userId = $("[name=kihyoushaUserId]").val();
	var denpyouKbn = $("input[name=denpyouKbn]").val();
	var dairiFlg = $("#workflowForm").find("input[name=dairiFlg]").val();

	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "法人カード使用一覧",
		buttons: {追加して閉じる:function() {
									 $("input[name=sentaku]:checked").each(function(){
										var msShb = $(this).parent().find("select[name=meisaiShubetsu]").val();
									 	var meisaiInfoKeihi = getHoujinCardRirekiInfoKeihi($(this));
									 	meisaiAdd(meisaiInfoKeihi);
									 });
									 $(this).dialog("close");
								},
						  閉じる:function() {$(this).dialog("close");}}
	})
	.load("houjin_card_siyou_ichiran?denpyouId=" + encodeURI(denpyouId) + "&userId=" + encodeURI(userId) + "&denpyouKbn=" + encodeURI(denpyouKbn) + "&dairiFlg=" + encodeURI(dairiFlg) );
}

function getHoujinCardRirekiInfoKeihi(obj) {

	// 一覧の最大インデックスを取得する
	var maxIndex = $("#meisaiList").find("tr.meisai").length - 1;

	var dairiFlg = $("#workflowForm").find("input[name=dairiFlg]").val();

	var userId   = dairiFlg == "1" ? "" : $("[name=kihyoushaUserId]").val();
	var userName = dairiFlg == "1" ? "" : $("[name=kihyoushaUserName]").val();
	var shainNo  = dairiFlg == "1" ? "" : $("[name=kihyoushaShainNo]").val();

	//該当行の明細MAP返す
	return {
		index:					"1",
		maxIndex:				maxIndex,

		denpyouId:				$("input[name=denpyouId]").val(),
		denpyouKbn:				$("input[name=denpyouKbn]").val(),
		zeroEnabled:			$("input[name=karibaraiOn]:checked").length == 1 ? $("input[name=karibaraiOn]:checked").val() : "0",

		kaigaiFlg:				"0",
		userId:					userId,									//TODO 起票者か使用者のデータを渡す必要がある
		userName:				userName,
		shiyoubi:				obj.attr("houjin-card-riyoubi"),
		shouhyouShorui:			$("input[name=ryoushuushoSeikyuushoDefault]").val(),
		shainNo:				shainNo,
		shiwakeEdaNo:			"",
		torihikiName:			"",
		furikomisakiJouhou:		"",
		kamokuCd:				"",
		kamokuName:				"",
		kamokuEdabanCd:			"",
		kamokuEdabanName:		"",
		futanBumonCd:			"",
		futanBumonName:			"",
		torihikisakiCd:			"",
		torihikisakiName:		"",
		projectCd:				"",
		projectName:			"",
		segmentCd:				"",
		segmentName:			"",
		uf1Cd:					"",
		uf1Name:				"",
		uf2Cd:					"",
		uf2Name:				"",
		uf3Cd:					"",
		uf3Name:				"",
		uf4Cd:					"",
		uf4Name:				"",
		uf5Cd:					"",
		uf5Name:				"",
		uf6d:					"",
		uf6Name:				"",
		uf7Cd:					"",
		uf7Name:				"",
		uf8Cd:					"",
		uf8Name:				"",
		uf9Cd:					"",
		uf9Name:				"",
		uf10Cd:					"",
		uf10Name:				"",
		kazeiKbn:				"",
		kazeiKbnText:			"",
		kazeiKbnGroup:			"",
		zeiritsu:				$("select[name=zeiritsu]").val(),
		zeiritsuText:			"",
		zeiritsuGroup:			"",
		heishuCd:				"",
		rate:					"",
		gaika:					"",
		tani:					"",
		shiharaiKingaku:		obj.attr("kingaku"),
		houjinCardFlgKeihi:		"1",
		kaishaTehaiFlgKeihi:	"0",
		tekiyou:				"",
		chuuki2	:				"",
		kousaihiShousai:		"",
		kousaihiHyoujiFlg:		"",
		ninzuuRiyouFlg:		"",
		kazeiFlg:				"",
		enableInput:			'true' == $('#enableInput').val(),
		kamokuEdabanEnable:		"",
		futanBumonEnable:		"",
		kousaihiEnable:			"",
		ninzuuEnable:			"",
		projectEnable:			"",
		segmentEnable:			"",
		hontaiKingaku:			"",
		shouhizeigaku:			"",
		himodukeCardMeisaiKeihi:	obj.attr("houjin-card-rireki-no"),

		//以下計算用
		hontaiKingakuNum:		"",
		shouhizeigakuNum:		""
	};
}

</script>
