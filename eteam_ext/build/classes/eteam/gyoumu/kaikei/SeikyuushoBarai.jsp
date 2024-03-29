<%@page import="eteam.common.open21.Open21Env"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<style type='text/css'>
/* balloon-1 top */
.balloon-1-top {
	color: #19283C;
	position: relative;
	display: inline-block;
	padding: 0 15px;
	width: auto;
	line-height: 32px;
	text-align: left;
	background: #F6F6F6;
	border: 3px solid #F75A5A;
	z-index: 0;
	margin-top:6px;
	margin-bottom:3px;
}
.balloon-1-top:before {
	content: "";
	position: absolute;
	top: -8px; left: 200px;
	margin-left: -9px;
	display: block;
	width: 0px;
	height: 0px;
	border-style: solid;
	border-width: 0 9px 9px 9px;
	border-color: transparent transparent #F6F6F6 transparent;
	z-index: 0;

}
.balloon-1-top:after {
	content: "";
	position: absolute;
	top: -12px; left: 200px;
	margin-left: -10px;
	display: block;
	width: 0px;
	height: 0px;
	border-style: solid;
	border-width: 0 10px 10px 10px;
	border-color: transparent transparent #F75A5A transparent;
	z-index: -1;
}
</style>
<div id='kaikeiContent' class='form-horizontal'>
	<section class='print-unit' id="shinseiSection">
		<h2>申請内容</h2>
		<div>
			<!-- 支払期限／領収書・請求書等 -->
			<div class='control-group'>
				<label class='control-label' <c:if test='${not ks.shiharaiKigen.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shiharaiKigen.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiKigen.name)}</label>
				<div class='controls'>
					<input type='text' name='shiharaiKigen' class='input-small datepicker' value='${su:htmlEscape(shiharaiKigen)}' <c:if test='${not ks.shiharaiKigen.hyoujiFlg}'>style='display:none;'</c:if>>
					<label class='label' <c:if test='${not ks.shouhyouShoruiFlg.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shouhyouShoruiFlg.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shouhyouShoruiFlg.name)}</label>
					<select name='shouhyouShoruiFlg' class='input-small' <c:if test='${not ks.shouhyouShoruiFlg.hyoujiFlg}'>style='display:none;'</c:if>>
						<option value='0' <c:if test='${shouhyouShoruiFlg eq "0"}'>selected</c:if>>なし</option>
						<option value='1' <c:if test='${shouhyouShoruiFlg eq "1"}'>selected</c:if>>あり</option>
					</select>
					<!-- 入力方式の表示は 常に表示 でOK　必須*マークは現時点では不要　後々WFチームから要望の可能性もある -->
					<c:if test='${ks.nyuryokuHoushiki.hyoujiFlg}'>
					<label class="label" for="nyuryokuHoushiki">入力方式</label>
					<select name="nyuryokuHoushiki" id="nyuryokuHoushiki" class='input-small'>
						 <c:forEach var="nyuryokuHoushikiRecord" items="${nyuryokuHoushikiList}">
							<option value='${nyuryokuHoushikiRecord.naibu_cd}'	<c:if test='${nyuryokuHoushikiRecord.naibu_cd eq nyuryokuHoushiki}'>selected</c:if>>${su:htmlEscape(nyuryokuHoushikiRecord.name)}</option>
						</c:forEach>
					</select>
					</c:if>
				</div>
			</div>
			<%@ include file="./kogamen/HeaderField.jsp" %>
			
			<!-- 掛け -->
			<div class='control-group' <c:if test='${not ks.kakeFlg.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.kakeFlg.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kakeFlg.name)}</label>
				<div class='controls'>
					<select name='hontaiKakeFlg' class='input-small' disabled>
						<option value='0' <c:if test='${hontaiKakeFlg eq "0"}'>selected</c:if>>なし</option>
						<option value='1' <c:if test='${hontaiKakeFlg eq "1"}'>selected</c:if>>あり</option>
					</select>
				</div>
			</div>

<c:if test="${1 <= keijouBiMode or 1 <= shiharaiBiMode}">
			<!-- 計上日／支払日／取引先マスター参照 -->
			<div class='control-group'>
				<label class='control-label'><span class='required'>*</span>計上日</label>
				<div class='controls'>
	<c:if test="${1 != keijouBiMode}">
						<input type='text' name='keijoubi' class='input-small datepicker' value='${su:htmlEscape(keijoubi)}' disabled>
	</c:if>
	<c:if test="${1 == keijouBiMode}">
		<c:if test="${setting.keijouNyuuryoku() eq 1}">
						<input type='text' name='keijoubi' class='input-small datepicker' value='${su:htmlEscape(keijoubi)}'>
		</c:if>
		<c:if test="${setting.keijouNyuuryoku() eq 2}">
			            <select name='keijoubi' class='input-medium'>
							<option value=''></option>
			<c:forEach var="record" items="${keijoubiList}">
							<option value='${record}' <c:if test='${record == keijoubi}'>selected</c:if>>${record}</option>
			</c:forEach>
						</select>
		</c:if>
	</c:if>
	<c:if test="${1 <= shiharaiBiMode}">
					<label class='label'>支払日</label>
					<input type='text' name='shiharaibi' class='input-small datepicker' <c:if test="${shiharaiBiMode ne 1}">disabled</c:if> value='${su:htmlEscape(shiharaibi)}'>
					<label class='label non-print' for='masrefFlg'>取引先マスター参照</label>
					<input type='checkbox' class='non-print' id='masrefFlg' name='masrefFlg' <c:if test="${shiharaiBiMode ne 1}">disabled</c:if> value='1' <c:if test='${masrefFlg eq "1"}'>checked</c:if>>
	</c:if>
				</div>
	<c:if test="${1 eq shiharaiBiMode and shiharaibiMitouroku}">
					<div class='balloon-1-top non-print'>
						<c:if test="${keijoubiMitouroku}">計上日/</c:if>支払日は未登録です。承認前に登録してください。<br>
		<% if (Open21Env.getVersion() == Open21Env.Version.DE3) { %>
						債務支払の支払先マスタに支払日決定を依存する場合、<br>取引先マスター参照のチェックを付けてください。
		<% } else { %>
						債務管理の仕入先マスタに支払日決定を依存する場合、<br>取引先マスター参照のチェックを付けてください。
		<% } %>
					</div>
	</c:if>
			</div>
</c:if>
		</div>
	</section>
	<%@ include file="./kogamen/DenpyouMeisaiList.jsp" %>
		<!-- インボイスで追加された合計欄  -->
	<section class='print-unit' id="goukeiSection">
		<div class='control-group'>
			<label class='control-label' <c:if test='${not ks.shiharaiKingakuGoukei.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shiharaiKingakuGoukei.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiKingakuGoukei.name)}</label>
			<div class='controls'>
				<input type='text' name='kingaku' class='input-medium autoNumericNoMax' disabled value='${su:htmlEscape(kingaku)}' <c:if test='${not ks.shiharaiKingakuGoukei.hyoujiFlg}'>style='display:none;'</c:if>>円
				<span style='display:none;'>
				（本体金額合計<input type='text' name='hontaiKingakuGoukei' class='input-medium autoNumericNoMax' disabled value='${su:htmlEscape(hontaiKingakuGoukei)}'>円
				　消費税額合計<input type='text' name='shouhizeigakuGoukei' class='input-medium autoNumericNoMax' disabled value='${su:htmlEscape(shouhizeigakuGoukei)}'>円）
				</span>
			</div>
		</div>
		<div class='control-group' name='kingakuGoukei10Percent'>
			<label class='control-label' for='shiharaiKingakuGoukei10Percent'<c:if test='${not ks.shiharaiKingakuGoukei10Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shiharaiKingakuGoukei10Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiKingakuGoukei10Percent.name)}</label>
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
	<!-- 補足 -->
	<section class='print-unit' <c:if test='${not ks.hosoku.hyoujiFlg}'>style='display:none;'</c:if>>
		<h2><c:if test='${ks.hosoku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.hosoku.name)}</h2>
		<div>
			<div class='control-group'>
				<textarea name="hosoku" maxlength='240' class='input-block-level'>${su:htmlEscape(hosoku)}</textarea>
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
	if('' != meisaiInfo['hontaiKakeFlg']){
		$('#shinseiSection select[name=hontaiKakeFlg]').val(meisaiInfo['hontaiKakeFlg']);
	}
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

	//計上日/支払日だけは別基準で入力制御
	var keijoubi		= $("[name=keijoubi]");
	var shiharaibi		= $("input[name=shiharaibi]");
	var masterFlg		= $("input[name=masrefFlg]");
	var masterFlgLabel	= $("label[for=masrefFlg]");
	var shiharaiKigen	= $("input[name=shiharaiKigen]");
	var kakeFlg			= $("select[name=hontaiKakeFlg]");

	//支払日とマスター参照は排他的
	if (masterFlg.prop("checked")) {
		shiharaibi.prop("disabled", true);
	}
	masterFlg.click(function(){
		if (masterFlg.prop("checked")) {
			shiharaibi.val("");
			shiharaibi.prop("disabled", true);
		} else {
			shiharaibi.val(shiharaiKigen.val());
			shiharaibi.prop("disabled", false);
		}
	});

	//掛けなしならマスター参照チェックは非表示
	if (kakeFlg.val() == "0") {
		masterFlg.hide();
		masterFlgLabel.hide();
	}

<c:if test='${shiharaiBiMode eq 1}'>
//支払日入力時▽
	<c:if test='${not shinseishaKeijoubiInput}'>
		<c:if test='${hontaiKakeFlg eq "1"}'>

	//申請者計上日入力なし 掛けあり...計上日は入力する
	keijoubi.prop("disabled", false);

		</c:if>

		<c:if test='${hontaiKakeFlg eq "0"}'>
	//申請者計上日入力なし 掛けなし...計上日は支払日連動させる
	keijoubi.prop("disabled", true);
	shiharaibi.change(function(){
		keijoubi.val(shiharaibi.val());
	});

		</c:if>
	</c:if>
//支払日入力時△
</c:if>

//消費税額詳細ボタンクリック
$('#zeigakuShousaiButton').on('click', function() {
    commonShouhizeiShousai($("input[name=denpyouKbn]").val());
  });
  
});

/**
 * イベントボタン押下時のアクションの切り替え
 */
function eventBtn(eventName) {
	var formObject = document.getElementById("workflowForm");
	var shiharaibi		= $("input[name=shiharaibi]");

	// 支払日チェック
	var checkShiharaiBi = (shiharaibi.length > 0 && ! shiharaibi.prop("disabled"));

	//支払日更新チェック
	if (checkShiharaiBi) {
		var keijouBi = $("[name=keijoubi]").val();
		var shiharaiBi = $("input[name=shiharaibi]").val();
		var masref = $("input[name=masrefFlg]");
		if (keijouBi == "" && (shiharaiBi == "" && ! masref.prop("checked"))) {
			//20220517
			if(!alert("計上日/支払日が未入力です。\n計上日/支払日を入力して、更新してください。")){
				$(formObject).find('button[type=button]').not('#chiharaibiCheckNotPass').removeAttr("disabled");
			}
			//alert("計上日/支払日が未入力です。\n計上日/支払日を入力して、更新してください。");
			return;
		} else if (keijouBi == "") {
			//20220517
			if(!alert("計上日が未入力です。\n計上日を入力して、更新してください。")){
				$(formObject).find('button[type=button]').not('#chiharaibiCheckNotPass').removeAttr("disabled");
			}
			//alert("計上日が未入力です。\n計上日を入力して、更新してください。");
			return;
		} else if ((shiharaiBi == "" && ! masref.prop("checked"))) {
			//20220517
			if(!alert("支払日が未入力です。\n支払日を入力して、更新してください。")){
				$(formObject).find('button[type=button]').not('#chiharaibiCheckNotPass').removeAttr("disabled");
			}
			//alert("支払日が未入力です。\n支払日を入力して、更新してください。");
			return;
		}
	}


	switch (eventName) {
	case 'entry':
		formObject.action = 'seikyuusho_barai_touroku';
		break;
	case 'update':
		formObject.action = 'seikyuusho_barai_koushin';
		break;
	case 'shinsei':
		formObject.action = 'seikyuusho_barai_shinsei';
		break;
	case 'shounin':
		formObject.action = 'seikyuusho_barai_shounin';
		break;
	case 'shouninkaijo':
		formObject.action = 'seikyuusho_barai_shouninkaijo';
		break;
	case 'shiharaibiUpdate':
		formObject.action = 'seikyuusho_barai_shiharaibi_koushin';
		break;
	}

	formObject.method = 'post';
	formObject.target = '_self';
	$(formObject).submit();
}

</script>
