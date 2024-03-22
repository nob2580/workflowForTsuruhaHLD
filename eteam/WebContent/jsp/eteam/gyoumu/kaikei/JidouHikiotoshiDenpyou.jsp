<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<div id='kaikeiContent' class='form-horizontal'>
	<section class='print-unit' id="shinseiSection">
		<h2>申請内容</h2>
		<div>
			<div class='control-group'>
				<label class='control-label' <c:if test='${not ks.hikiotoshibi.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.hikiotoshibi.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.hikiotoshibi.name)}</label>
				<div class='controls'>
					<input type='text' name='hikiotoshibi' class='input-small datepicker' <c:if test='${not ks.hikiotoshibi.hyoujiFlg}'>style='display:none;'</c:if> value='${su:htmlEscape(hikiotoshibi)}'>
					<!-- 計上日 --><%-- 会社設定により申請者のとき変更可能 または 申請後表示、経理承認時のみ変更可能※現金主義なら常に非表示 --%>
					<span id='keijoubi' <c:if test="${keijouBiMode == 0}">class='never_show' style='display:none;'</c:if>>
						<label class='label hissu'><span class='required'>*</span>計上日</label>
<c:if test="${keijouBiMode == 0 || keijouBiMode == 2}">
                        <input type='text' name='keijoubi' class='input-small datepicker hissu' value='${su:htmlEscape(keijoubi)}' disabled>
</c:if>
<c:if test="${keijouBiMode == 1}">
                        <input type='text' name='keijoubi' class='input-small datepicker hissu' value='${su:htmlEscape(keijoubi)}'>
</c:if>
<c:if test="${keijouBiMode == 3}">
                        <select name='keijoubi' class='input-medium hissu'>
                            <option value=''></option>
        <c:forEach var="record" items="${keijoubiList}">
                            <option value='${record}' <c:if test='${record == keijoubi}'>selected</c:if>>${record}</option>
        </c:forEach>
                        </select>
</c:if>
					</span>
					<label class='label' <c:if test='${not ks.shouhyouShoruiFlg.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shouhyouShoruiFlg.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shouhyouShoruiFlg.name)}</label>
					<select name='shouhyouShoruiFlg' class='input-small' <c:if test='${not ks.shouhyouShoruiFlg.hyoujiFlg}'>style='display:none;'</c:if>>
						<option value='0' <c:if test='${shouhyouShoruiFlg eq "0"}'>selected</c:if>>なし</option>
						<option value='1' <c:if test='${shouhyouShoruiFlg eq "1"}'>selected</c:if>>あり</option>
					</select>
					<c:if test='${ks.nyuryokuHoushiki.hyoujiFlg}'>
					<label class='label' for='nyuryokuHoushiki'>入力方式</label>
					<select name='nyuryokuHoushiki' id='nyuryokuHoushiki' class='input-small'>
						 <c:forEach var="nyuryokuHoushikiRecord" items="${nyuryokuHoushikiList}">
							<option value='${nyuryokuHoushikiRecord.naibu_cd}'	<c:if test='${nyuryokuHoushikiRecord.naibu_cd eq nyuryokuHoushiki}'>selected</c:if>>${su:htmlEscape(nyuryokuHoushikiRecord.name)}</option>
						</c:forEach>
					</select>
					</c:if>
				</div>
			</div>
			<%@ include file="./kogamen/HeaderField.jsp" %>
		</div>
	</section>

	<%@ include file="./kogamen/DenpyouMeisaiList.jsp" %>

	<section class='print-unit' id="goukeiSection">
		<div class='control-group'>
			<label class='control-label' <c:if test='${not ks.shiharaiKingakuGoukei.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shiharaiKingakuGoukei.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiKingakuGoukei.name)}</label>
			<div class='controls'>
				<input type='text' name='kingaku' class='input-medium autoNumericNoMax' disabled  <c:if test='${not ks.shiharaiKingakuGoukei.hyoujiFlg}'>style='display:none;'</c:if> value='${su:htmlEscape(kingaku)}'>円
				<span style='display:none;'> <!-- styleがnoneなのでそのまま -->
				（本体金額合計<input type='text' name='hontaiKingakuGoukei' class='input-medium autoNumericNoMax' disabled value='${su:htmlEscape(hontaiKingakuGoukei)}'>円
				　消費税額合計<input type='text' name='shouhizeigakuGoukei' class='input-medium autoNumericNoMax' disabled value='${su:htmlEscape(shouhizeigakuGoukei)}'>円）
				</span>
			</div>
		</div>
				<!-- インボイスで追加された合計欄  -->
		<div class='control-group' name='kingakuGoukei10Percent'>
			<label class='control-label' <c:if test='${not ks.shiharaiKingakuGoukei10Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shiharaiKingakuGoukei10Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiKingakuGoukei10Percent.name)}</label>
			<div class='controls'>
				<!-- 支払金額（10%） -->
				<c:if test='${ks.shiharaiKingakuGoukei10Percent.hyoujiFlg}'>
					<input type='text' name='shiharaiKingakuGoukei10Percent' class='input-medium autoNumericNoMax' disabled value='0'>円
				</c:if>
				<!-- 税抜金額（10%） -->
				<label class='label' <c:if test='${not ks.zeinukiKingaku10Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.zeinukiKingaku10Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.zeinukiKingaku10Percent.name)}</label>
				<c:if test='${ks.zeinukiKingaku10Percent.hyoujiFlg}'>
					<input type="text" name="zeinukiKingaku10Percent" class="input-medium autoNumericNoMax" disabled value='0'>円
				</c:if>
				<!-- 消費税額（10%） -->
				<label class='label' <c:if test='${not ks.shouhizeigaku10Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shouhizeigaku10Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shouhizeigaku10Percent.name)}</label>
				<c:if test='${ks.shouhizeigaku10Percent.hyoujiFlg}'>
					<input type="text" name="shouhizeigaku10Percent" class="input-medium autoNumericNoMax" disabled value='0'>円
				</c:if>
			</div>
		</div>
		<div class='control-group' name='kingakuGoukei8Percent'>
			<label class='control-label' <c:if test='${not ks.shiharaiKingakuGoukei8Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shiharaiKingakuGoukei8Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiKingakuGoukei8Percent.name)}</label>
			<div class='controls'>
				<!-- 支払金額（*8%） -->
				<c:if test='${ks.shiharaiKingakuGoukei8Percent.hyoujiFlg}'>
					<input type='text' name='shiharaiKingakuGoukei8Percent' class='input-medium autoNumericNoMax' disabled value='0'>円
				</c:if>
				<!-- 税抜金額（*8%） -->
				<label class='label' <c:if test='${not ks.zeinukiKingaku8Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.zeinukiKingaku8Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.zeinukiKingaku8Percent.name)}</label>
				<c:if test='${ks.zeinukiKingaku8Percent.hyoujiFlg}'>
					<input type="text" name="zeinukiKingaku8Percent" class="input-medium autoNumericNoMax" disabled value='0'>円
				</c:if>
				<!-- 消費税額（*8%） -->
				<label class='label' <c:if test='${not ks.shouhizeigaku8Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shouhizeigaku8Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shouhizeigaku8Percent.name)}</label>
				<c:if test='${ks.shouhizeigaku8Percent.hyoujiFlg}'>
					<input type="text" name="shouhizeigaku8Percent" class="input-medium autoNumericNoMax" disabled value='0'>円
				</c:if>
				<c:if test='${ks.shiharaiKingakuGoukei10Percent.hyoujiFlg}'><!-- とりあえず10%が表示されるなら出しておく（基本的に共通と思われるので -->
					<button type='button' id='zeigakuShousaiButton' class='btn btn-small'>消費税額詳細</button>
				</c:if>
			</div>
		</div>
	</section>

	<section class='print-unit' <c:if test='${not ks.hosoku.hyoujiFlg}'>style='display:none;'</c:if>>
		<h2><c:if test='${ks.hosoku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.hosoku.name)}</h2>
		<div>
			<div class='control-group'>
				<textarea name="hosoku" class='input-block-level' maxlength="240">${su:htmlEscape(hosoku)}</textarea>
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
	
	//本体・消費税の全明細分合計を計算
 	var hontaiKingakuGoukei = 0;
 	var shouhizeigakuGoukei = 0;
	$.each(getMeisaiList(), function(ii, meisaiInfo) {
		hontaiKingakuGoukei += meisaiInfo["hontaiKingakuNum"];
		shouhizeigakuGoukei += meisaiInfo["shouhizeigakuNum"];
	});
	hontaiKingakuGoukeiTag.putMoney(hontaiKingakuGoukei);
	shouhizeigakuGoukeiTag.putMoney(shouhizeigakuGoukei);
	kingakTag.putMoney(hontaiKingakuGoukei + shouhizeigakuGoukei);
	
	// インボイス関連（eteam.jsより読込）
	calcInvoiceKingaku();
}

//初期表示処理
$(document).ready(function(){
	// 明細表示をロード
	setDisplayMeisaiData();

<c:if test='${not enableInput}'>
	//起票モードの場合のみ入力や選択可能
	$("#kaikeiContent").find("button").css("display", "none");
	$("#kaikeiContent").find("input,textarea,select").prop("disabled", true);
</c:if>

// 消費税額詳細ボタンクリック
$('#zeigakuShousaiButton').on('click', function() {
    commonShouhizeiShousai($("input[name=denpyouKbn]").val());
  });

});

/**
 * イベントボタン押下時のアクションの切り替え
 */
function eventBtn(eventName) {
	var formObject = document.getElementById("workflowForm");

	switch (eventName) {
	case 'entry':
		formObject.action = 'jidou_hikiotoshi_denpyou_touroku';
		break;
	case 'update':
		formObject.action = 'jidou_hikiotoshi_denpyou_koushin';
		break;
	case 'shinsei':
		formObject.action = 'jidou_hikiotoshi_denpyou_shinsei';
		break;
	case 'shounin':
		formObject.action = 'jidou_hikiotoshi_denpyou_shounin';
		break;
	case 'shouninkaijo':
		formObject.action = 'jidou_hikiotoshi_denpyou_shouninkaijo';
		break;
	}
	formObject.method = 'post';
	formObject.target = '_self';
	$(formObject).submit();
}
</script>
