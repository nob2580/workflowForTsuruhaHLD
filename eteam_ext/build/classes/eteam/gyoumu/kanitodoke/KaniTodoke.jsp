<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<style type='text/css'>
.number {
	text-align:right;
}
</style>
<div id='kaikeiContent' class='form-horizontal'>
	<input type='hidden' name='maxDenpyouEdaNo' value='${su:htmlEscape(maxDenpyouEdaNo)}'>
	<input type='hidden' name='version' value='${su:htmlEscape(version)}'>
	<input type='hidden' name='sanshouVersion' value='${su:htmlEscape(sanshouVersion)}'>
	${shinseiHtml}
	${meisaiHtml}
	<datalist id="jikoku"><c:forEach var='record' items='${jikokuList}'><option value='${su:htmlEscape(record.name)}'></c:forEach></datalist>
	
</div><!-- kaikeiContent -->

<!-- スクリプト -->
<script src="/eteam/static/js/eteam_kanitodoke.js"></script>
<script style='text/javascript'>

/**
 * 明細+ボタン押下時Function
 */
function meisaiAdd() {

	// チェック状態をHiddenに更新する
	updateHiddenRadio();

	//１行目の明細をコピー
	var div = $("#meisaiList div:first").clone();
	$("#meisaiList").append(div);

	//中身を消してアクション紐付け、明細番号を振リ直し
	inputClear(div);
	commonInit(div);
	meisaiReset();
}

/**
 * 明細-ボタン押下時Function
 */
function meisaiDelete() {
	if(1 == $("#meisaiList").children().length) return;
	if(window.confirm("明細を削除してもよろしいですか？")) {
		$(this).closest("div.meisai").remove();
		meisaiReset();
		// 金額の再計算を実施する。
		kingakuKeisan();
	}
}

/**
 * 明細↑ボタン押下時Function
 */
function meisaiUp() {
	var div = $(this).closest("div.meisai");
	if(div.prev()){
		div.insertBefore(div.prev());
	}
	meisaiReset();
}

/**
 * 明細↓ボタン押下時Function
 */
function meisaiDown() {
	var div = $(this).closest("div.meisai");
	if(div.next()){
		div.insertAfter(div.next());
	}
	meisaiReset();
}

/**
 * 明細ｺﾋﾟｰボタン押下時Function
 */
function meisaiCopy() {

	// チェック状態をHiddenに更新する
	updateHiddenRadio();

	//選択行の明細をコピー
	var div = $(this).closest("div.meisai");
	var divClone = div.clone();
	$("#meisaiList").append(divClone);

	//アクション紐付け、明細番号を振リ直し
	commonInit(divClone);
	meisaiReset();

	// 入力値のコピー
	selectValCopy(div, divClone);
	// 金額の再計算を実施する。
	kingakuKeisan();
}

/**
 * 明細リセットFunction
 */
function meisaiReset() {

	$("div.meisai").each(function(){

		// 明細にナンバーを振る
		var i = ($("div.meisai").index($(this)) + 1);
		$(this).find("h2 *.number").text(i);

		// ラジオボタンの名前を振りなおし
		var divs = $(this).find("div.controls");
		for (var j = 0; j < divs.length; j++) {
			var div = divs.eq(j);
			var hid = div.find("input[type=hidden]");
			var radios =div.find("input[type=radio]");
			var hidNm = hid.attr("name") + "_" + i;
			var hidVal = hid.val();
			for (var k = 0; k < radios.length; k++) {
				var radio = radios.eq(k);
				var valRadio = radio.val();
				radio.attr("name", hidNm);
				if (hidVal == valRadio) {
					radio.prop('checked',true);
				}
			}
		}

		// 最大伝票枝番号を更新
		$(':hidden[name="maxDenpyouEdaNo"]').val(i);
	});
}

//初期表示処理
$(document).ready(function(){

	<c:if test='${enableInput}'>
		//明細のアクション紐付け
		$("body").on("click","button[name=meisaiAdd]",meisaiAdd); 
		$("body").on("click","button[name=meisaiDelete]",meisaiDelete); 
		$("body").on("click","button[name=meisaiUp]",meisaiUp); 
		$("body").on("click","button[name=meisaiDown]",meisaiDown); 
		$("body").on("click","button[name=meisaiCopy]",meisaiCopy); 
	
		// 明細の金額にonchangeイベントを設定する。
		$("body").on("change","input[yosanid=syuunyuu_kingaku]",kingakuKeisan); 
		$("body").on("change","input[yosanid=shishutsu_kingaku]",kingakuKeisan); 
		// 金額の計算を実施する。
		kingakuKeisan();
		// 入力制御
		$("input.timepicker").timeInput();
	</c:if>

	<c:if test='${not enableInput}'>
		//起票モードの場合のみ入力や選択可能
		$("#kaikeiContent").find("button").css("display", "none");
		$("#kaikeiContent").find("input,textarea,select").prop("disabled", true);
	</c:if>

});

/**
 * イベントボタン押下時のアクションの切り替え
 */
function eventBtn(eventName) {

	// チェック状態をHiddenに更新する
	updateHiddenRadio();

	var formObject = document.getElementById("workflowForm");

	switch (eventName) {
	case 'entry':
		formObject.action = 'kani_todoke_touroku';
		// 金額の再計算を実施する。
		kingakuKeisan();
		break;
	case 'update':
		formObject.action = 'kani_todoke_koushin';
		// 金額の再計算を実施する。
		kingakuKeisan();
		break;
	case 'shinsei':
		formObject.action = 'kani_todoke_shinsei';
		break;
	case 'shounin':
		formObject.action = 'kani_todoke_shounin';
		break;
	case 'shouninkaijo':
		formObject.action = 'kani_todoke_shouninkaijo';
		break;
	}
	formObject.method = 'post';
	formObject.target = '_self';
	$(formObject).submit();
}


/**
 * チェック状態をHiddenに更新する
 */
function updateHiddenRadio() {

	$("#shinseiList").each(function(){

		var divs = $(this).find("div.controls");

		for (var i = 0; i < divs.length; i++) {
			var div = divs.eq(i);
			var hid = div.find("input[type=hidden]");
			// ラジオボタン
			var radios =div.find("input[type=radio]");
			if (radios.length > 0) {
				var radio = div.find("input[type=radio]:checked");
				hid.val(radio.val());
			}
			// チェックボックス
			var checks =div.find("input[type=checkbox]");
			if (checks.length > 0) {
				var check = div.find("input[type=checkbox]:checked");
				if (check.val() == 1) {
					hid.val(1);
				} else {
					hid.val(0);
				}
			}
		}
	});

	$("div.meisai").each(function(){
		var divs = $(this).find("div.controls");
		for (var i = 0; i < divs.length; i++) {
			var div = divs.eq(i);
			var hid = div.find("input[type=hidden]");
			// ラジオボタン
			var radios =div.find("input[type=radio]");
			if (radios.length > 0) {
				var radio = div.find("input[type=radio]:checked");
				hid.val(radio.val());
			}
			// チェックボックス
			var checks =div.find("input[type=checkbox]");
			if (checks.length > 0) {
				var check = div.find("input[type=checkbox]:checked");
				if (check.val() == 1) {
					hid.val(1);
				} else {
					hid.val(0);
				}
			}
		}
	});
}
</script>
