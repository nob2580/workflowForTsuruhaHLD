<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!-- タイトル -->
<c:choose>
	<c:when test='${gamenTitle eq ""}'>
		<c:if test='${dispMode eq "1"}'><h1>届書テキストエリア追加</h1></c:if>
		<c:if test='${dispMode eq "2"}'><h1>届書テキストエリア変更</h1></c:if>
	</c:when>
	<c:otherwise>
		<h1>${gamenTitle}<c:if test='${dispMode eq "1"}'>追加</c:if><c:if test='${dispMode eq "2"}'>変更</c:if></h1>
	</c:otherwise>
</c:choose>

<!-- エラー -->
<%@ include file="/jsp/eteam/include/InputError.jsp" %>

<!-- メイン -->
<div id='dialogMain'>

	<!-- 入力フィールド -->
	<section id='textAreaAddSection'>
		<div class="form-horizontal">
			<form class="form-inline">
				<div class='control-group'>
					<label class="control-label label-sub"><span class='required'>*</span>ラベル名</label>
					<div class='controls'>
						<input type='text' name='labelName' maxlength='10' value='${su:htmlEscape(labelName)}'>
					</div>
				</div>
				<div class='control-group'>
					<label class="control-label label-sub"><span class='required'>*</span>必須有無</label>
					<div class='controls'>
						<label class='radio inline'><input type='radio' name='hissuFlg' value='1' <c:if test='${hissuFlg eq "1"}'>checked</c:if>>有</label>
						<label class='radio inline'><input type='radio' name='hissuFlg' value='0' <c:if test='${hissuFlg eq "0"}'>checked</c:if>>無</label>
					</div>
				</div>
				<div class='control-group'>
					<label class="control-label label-sub"><span class='required'>*</span>固定表示</label>
					<div class='controls'>
						<c:forEach var="koteiHyoujiList" items="${koteiHyoujiList}">
						<label class='radio inline'><input type='radio' name='koteiHyouji' value='${su:htmlEscape(koteiHyoujiList.naibu_cd)}' <c:if test='${koteiHyoujiList.naibu_cd eq koteiHyouji}'>checked</c:if>>${su:htmlEscape(koteiHyoujiList.name)}</label>
						</c:forEach>
					</div>
				</div>
				<div class='control-group'>
					<label class="control-label label-sub">最大文字数</label>
					<div class='controls'>
						<input type='text' name='maxLength' class='input-mini autoNumeric' value='${su:htmlEscape(maxLength)}'>
					</div>
				</div>
				<div class='control-group'>
					<label class="control-label label-sub"><span class='required'>*</span>テキスト幅</label>
					<div class='controls'>
						<c:forEach var="textWidthList" items="${textWidthList}">
						<label class='radio inline'><input type='radio' name='textWidth' value='${su:htmlEscape(textWidthList.naibu_cd)}' <c:if test='${textWidthList.naibu_cd eq textWidth}'>checked</c:if>>${su:htmlEscape(textWidthList.name)}</label>
						</c:forEach>
					</div>
				</div>
				<div class='control-group'>
					<label class="control-label label-sub"><span class='required'>*</span>テキスト高さ</label>
					<div class='controls'>
						<c:forEach var="textHeightList" items="${textHeightList}">
						<label class='radio inline'><input type='radio' name='textHeight' value='${su:htmlEscape(textHeightList.naibu_cd)}' <c:if test='${textHeightList.naibu_cd eq textHeight}'>checked</c:if>>${su:htmlEscape(textHeightList.name)}</label>
						</c:forEach>
					</div>
				</div>
				<div class='control-group'>
					<c:if test='${dispMode eq "1"}'><button type='button' onclick='add()' class='btn'><i class='icon-plus'></i> 追加</button></c:if>
					<c:if test='${dispMode eq "2"}'><button type='button' onclick='add()' class='btn'><i class='icon-refresh'></i> 変更</button></c:if>
				</div>
				<input type='hidden' name='yosanId' value='${su:htmlEscape(yosanId)}'>
			</form>
		</div>
	</section>
</div><!-- main -->
<script type="text/javascript">

// 予算執行項目の最大桁数
var textareaMaxLength = 999999999;

function add() {

	var label_name = $("input[name=labelName]").val();													// ラベル名
	var max_length = $("input[name=maxLength]").val().replace(",","").replace(",","").replace(",","");	// 最大桁数
	var buhin_width = $("input:radio[name=textWidth]:checked").val();									// 部品幅
	var buhin_height = $("input:radio[name=textHeight]:checked").val();									// 部品高さ
	var yosan_id = $("input[name=yosanId]").val();														// 予算執行項目ID
	var kotei_hyouji = $("input:radio[name=koteiHyouji]:checked").val();								// 固定表示

	/*
	 * 初期値設定
	 */

	 // 最大桁数のデフォルト値を設定
	if (max_length == "") {
		if (yosan_id == "") {
			// 予算執行項目以外の場合に従来の最大桁数を設定する。
			max_length = 1000;
		} else {
			max_length = textareaMaxLength;
		}
	} 
	// 固定表示「する」のとき
	if (kotei_hyouji == "1") {
		$("input[name=hissuFlg]:eq(1)").prop("checked", true);
	}

	/*
	 * 入力チェック
	 */

	// ラベル名の入力必須、特殊文字チェック
	if (label_name == "") {
		alert("ラベル名を入力してください。");
		return;
	} else {
		if(containEscapeHTML(label_name)) {
			alert("ラベル名に使用できない文字が含まれています。");
			return;
		}
	}

	// 最大文字数チェック
	if (yosan_id == "") {
		// 予算執行項目以外の場合に最大文字数のチェックを行う。
		if(max_length > 1000 || max_length < 1) {
			alert("最大文字数を、1000字以内で入力してください。");
			return;
		}
	}else{
		// 予算執行項目の場合の最大文字数をチェックする。
		if(max_length > textareaMaxLength || max_length < 1) {
			alert("最大文字数を、" + textareaMaxLength + "字以内で入力してください。");
			return;
		}
	}

	var area_kbn = getAreaKbn();
	var hyouji_jun = parseInt($("input[name=" + area_kbn + "ItemCnt]").val()) + 1;
	var item_name = area_kbn + ("0" + hyouji_jun).slice(-2);
	var value_name = item_name + "_val1";

	// 入力項目
	var inputItem = "<textarea name='{value_name}' class='input-inline {buhin_width} {buhin_height}' maxlength='{max_length}' yosanId='{yosanId}' koteiHyouji='{kotei_hyouji}'></textarea>";	

	inputItem = inputItem.replace("{value_name}",value_name);		// 値名
	inputItem = inputItem.replace("{max_length}",max_length);		// 最大桁数
	inputItem = inputItem.replace("{buhin_width}",buhin_width);		// 部品幅
	inputItem = inputItem.replace("{buhin_height}",buhin_height);	// 部品高さ
	inputItem = inputItem.replace("{yosanId}",yosan_id);			// 予算執行項目ID
	inputItem = inputItem.replace("{kotei_hyouji}",kotei_hyouji);	// 固定表示

	// 入力項目を追加する
	addInputItem(area_kbn, hyouji_jun, label_name, item_name, "textarea", value_name, inputItem);

	// 画面を閉じる
	$("#dialog").dialog("close");
}

//初期表示処理
$("#dialog").ready(function(){
	// 補助機能部品の再呼出
	commonInit($("#textAreaAddSection").children());
	// 予算関連項目は固定不可能
	if ($('input[name="yosanId"]').val() != null && $('input[name="yosanId"]').val() != "") {
		$('input[name="koteiHyouji"]').attr("disabled", "disabled");
	} else {
		$('input[name="koteiHyouji"]').removeAttr("disabled");
	}
	// 固定表示変更の再呼出
	koteiHyoujiChange($("input[name='koteiHyouji']:checked").val());
});

//固定の選択により項目制御やり直す
$('input[name="koteiHyouji"]:radio').change( function() {
	koteiHyoujiChange($(this).val());
});

//項目制御
function koteiHyoujiChange(kotei_hyouji) {
	if (kotei_hyouji == "1") {
		$('input[name="hissuFlg"]').attr("disabled", "disabled");		$('input[name="hissuFlg"]:eq(1)').prop("checked",true);
		$('input[name="maxLength"]').attr("disabled", "disabled");		$('input[name="maxLength"]').val(null);
	} else {
		$('input[name="hissuFlg"]').removeAttr("disabled");
		$('input[name="maxLength"]').removeAttr("disabled");
	}
}
</script>
