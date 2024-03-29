<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!-- タイトル -->
<c:choose>
	<c:when test='${gamenTitle eq ""}'>
		<c:if test='${dispMode eq "1"}'><h1>届書テキスト追加</h1></c:if>
		<c:if test='${dispMode eq "2"}'><h1>届書テキスト変更</h1></c:if>
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
	<section id='textAddSection'>
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
					<label class="control-label label-sub"><span class='required'>*</span>形式</label>
					<div class='controls'>
						<c:forEach var="textFormatList" items="${textFormatList}">
						<label class='radio inline'><input type='radio' name='textFormat' value='${su:htmlEscape(textFormatList.naibu_cd)}' <c:if test='${textFormatList.naibu_cd eq textFormat}'>checked</c:if> <c:if test='${yosanDisableFlg eq "1"}'>disabled</c:if>>${su:htmlEscape(textFormatList.name)}</label>
						</c:forEach>
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
					<label class="control-label label-sub"><span class='required'>*</span>小数点以下桁数</label>
					<div class='controls'>
						<c:forEach var="decimalPointList" items="${decimalPointList}">
						<label class='radio inline'><input type='radio' name='decimalPoint' value='${su:htmlEscape(decimalPointList.naibu_cd)}' <c:if test='${decimalPointList.naibu_cd eq decimalPoint}'>checked</c:if>>${su:htmlEscape(decimalPointList.name)}</label>
						</c:forEach>
					</div>
				</div>
				<div class='control-group'>
					<label class="control-label label-sub">数値範囲</label>
					<div class='controls'>
						<input type='text' name='minValue' class='input-mini autoNumeric' disabled value='${su:htmlEscape(minValue)}'>
						～
						<input type='text' name='maxValue' class='input-mini autoNumeric' disabled value='${su:htmlEscape(maxValue)}'>
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
					<c:if test='${dispMode eq "1"}'><button type='button' onclick='add()' class='btn'><i class='icon-plus'></i> 追加</button></c:if>
					<c:if test='${dispMode eq "2"}'><button type='button' onclick='add()' class='btn'><i class='icon-refresh'></i> 変更</button></c:if>
				</div>
				<input type='hidden' name='yosanId' value='${su:htmlEscape(yosanId)}'>
			</form>
		</div>
	</section>
</div><!-- main -->
<script type="text/javascript">

// 予算執行項目の形式が文字の場合の最大桁数
var stringMaxLength = 999999999;

function add() {

	var label_name = $("input[name=labelName]").val();													// ラベル名
	var buhin_format = $("input:radio[name=textFormat]:checked").val();									// 部品形式
	var max_length = $("input[name=maxLength]").val().replace(",","").replace(",","").replace(",","");	// 最大桁数
	var min_value = $("input[name=minValue]").val().replace(",","").replace(",","").replace(",","");	// 最小値
	var max_value = $("input[name=maxValue]").val().replace(",","").replace(",","").replace(",","");	// 最大値
	var buhin_width = $("input:radio[name=textWidth]:checked").val();									// 部品幅
	var yosan_id = $("input[name=yosanId]").val();														// 予算執行項目ID
	var decimal_point = $("input:radio[name=decimalPoint]:checked").val();								// 小数点以下桁数
	var kotei_hyouji = $("input:radio[name=koteiHyouji]:checked").val();								// 固定表示

	/*
	 * 初期値設定
	 */

	// 形式が日付の場合
	if (buhin_format == "datepicker") {
		max_length = "10";
		buhin_width = "input-small";
	}
	// 形式が時刻の場合
	if (buhin_format == "timepicker") {
		max_length = "7";
		buhin_width = "input-small";
	}
	// 形式が金額の場合
	if (buhin_format == "autoNumericWithCalcBox") {
		max_length = "15";
		buhin_width = "input-medium";
	}
	// 形式が数値の場合
	if (buhin_format == "number") {
		max_length = "18";
	}
	// 形式が文字列の場合
	if (buhin_format == "string" && kotei_hyouji == "1") {
		$("input[name=hissuFlg]:eq(1)").prop("checked", true);
	}
	// 最大桁数のデフォルト値を設定
	if (max_length == "") {
		if (yosan_id == "") {
			// 予算執行項目以外の場合に従来の最大桁数を設定する。
			max_length = 100;
		} else {
			max_length = stringMaxLength;
		}
	}
	// 最小値のデフォルト値を設定
	if (min_value == "") {
		min_value = 1;
	}
	// 最大値のデフォルト値を設定
	if (max_value == "") {
		if (yosan_id == "") {
			// 予算執行項目以外の場合に従来の最大値を設定する。
			max_value = 999999999999;
		} else {
			max_value = stringMaxLength;
		}
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

	// 数値の場合は大小チェック
	if (buhin_format == "number") {
		if (Number(max_value) < Number(min_value)) {
			alert("最小値は、最大値より小さい値を入力してください。");
			return;
		}
	} 

	// 文字列の場合は最大文字数チェック
	if (buhin_format == "string") {
		if (yosan_id == "") {
			// 予算執行項目以外の場合に最大文字数のチェックを行う。
			if(max_length > 100 || max_length < 1) {
				alert("最大文字数を、100字以内で入力してください。");
				return;
			}
		}else{
			// 予算執行項目の場合の最大文字数をチェックする。
			if(max_length > stringMaxLength || max_length < 1) {
				alert("最大文字数を、" + stringMaxLength + "字以内で入力してください。");
				return;
			}
		}
	}

	var area_kbn = getAreaKbn();
	var hyouji_jun = parseInt($("input[name=" + area_kbn + "ItemCnt]").val()) + 1;
	var item_name = area_kbn + ("0" + hyouji_jun).slice(-2);
	var value_name = item_name + "_val1";

	// 入力項目
	var inputItem = "";

	inputItem += "<input type='hidden' name='min_value' value='{min_value}'>";
	inputItem += "<input type='hidden' name='max_value' value='{max_value}'>";
	inputItem += "<input type='text' name='{value_name}' class='input-inline {buhin_width} {buhin_format}' {list} maxlength='{max_length}' yosanId='{yosanId}' decimalPoint='{decimal_point}' koteiHyouji='{kotei_hyouji}'>{yen}";

	inputItem = inputItem.replace("{value_name}",value_name);		// 値名
	inputItem = inputItem.replace("{buhin_format}",buhin_format);	// 部品形式
	inputItem = inputItem.replace("{max_length}",max_length);		// 最大桁数
	inputItem = inputItem.replace("{list}",""); // リスト使用
	inputItem = inputItem.replace("{min_value}",min_value);			// 最小値
	inputItem = inputItem.replace("{max_value}",max_value);			// 最大値
	inputItem = inputItem.replace("{buhin_width}",buhin_width);		// 部品幅
	inputItem = (buhin_format == "autoNumericWithCalcBox") ? inputItem.replace("{yen}","円") : inputItem.replace("{yen}",""); // 円マーク
	inputItem = inputItem.replace("{yosanId}",yosan_id);			// 予算執行項目ID
	inputItem = inputItem.replace("{decimal_point}",decimal_point);	// 小数点以下桁数
	inputItem = inputItem.replace("{kotei_hyouji}",kotei_hyouji);	// 固定表示

	// 入力項目を追加する
	addInputItem(area_kbn, hyouji_jun, label_name, item_name, "text", value_name, inputItem);
	
	// 画面を閉じる
	$("#dialog").dialog("close");
}

//初期表示処理
$("#dialog").ready(function(){
	// 補助機能部品の再呼出
	commonInit($("#textAddSection").children());
	// 形式変更の再呼出
	buhinFormatChange($("input[name='textFormat']:checked").val());
});

//形式・固定の選択により項目制御やり直す
$('input[name="textFormat"]:radio').change( function() {
	buhinFormatChange($(this).val());
});
$('input[name="koteiHyouji"]:radio').change( function() {
	buhinFormatChange($(this).val());
});

//項目制御
function buhinFormatChange(buhin_format) {
	
	if (buhin_format == "datepicker" || buhin_format == "autoNumericWithCalcBox" || buhin_format == "timepicker") {
		$('input[name="hissuFlg"]').removeAttr("disabled");
		$('input[name="maxLength"]').attr("disabled", "disabled");		$('input[name="maxLength"]').val(null);
		$('input[name="decimalPoint"]').attr("disabled", "disabled");
		$('input[name="koteiHyouji"]').attr("disabled", "disabled");	$('input[name="koteiHyouji"]:eq(1)').prop("checked",true);
		$('input[name="textWidth"]').attr("disabled", "disabled");
		$('input[name="minValue"]').attr("disabled", "disabled");		$('input[name="minValue"]').val(null);
		$('input[name="maxValue"]').attr("disabled", "disabled");		$('input[name="maxValue"]').val(null);
	} else if (buhin_format == "number") {
		$('input[name="hissuFlg"]').removeAttr("disabled");
		$('input[name="maxLength"]').attr("disabled", "disabled");		$('input[name="maxLength"]').val(null);
		$('input[name="decimalPoint"]').removeAttr("disabled");
		$('input[name="koteiHyouji"]').attr("disabled", "disabled");	$('input[name="koteiHyouji"]:eq(1)').prop("checked",true);
		$('input[name="textWidth"]').removeAttr("disabled");
		$('input[name="minValue"]').removeAttr("disabled");
		$('input[name="maxValue"]').removeAttr("disabled");
	} else {
		if ($('input[name="yosanId"]').val() != null && $('input[name="yosanId"]').val() != "") {
			$('input[name="koteiHyouji"]').attr("disabled", "disabled");
		} else {
			$('input[name="koteiHyouji"]').removeAttr("disabled");
		}
		
		if($("input[name='koteiHyouji']:checked").val() == "1"){
			$('input[name="hissuFlg"]').attr("disabled", "disabled");		$('input[name="hissuFlg"]:eq(1)').prop("checked",true);
			$('input[name="maxLength"]').attr("disabled", "disabled");		$('input[name="maxLength"]').val(null);
			$('input[name="decimalPoint"]').attr("disabled", "disabled");
			$('input[name="minValue"]').attr("disabled", "disabled");		$('input[name="minValue"]').val(null);
			$('input[name="maxValue"]').attr("disabled", "disabled");		$('input[name="maxValue"]').val(null);
		}else{
			$('input[name="hissuFlg"]').removeAttr("disabled");
			$('input[name="maxLength"]').removeAttr("disabled");
			$('input[name="decimalPoint"]').attr("disabled", "disabled");
			$('input[name="minValue"]').attr("disabled", "disabled");		$('input[name="minValue"]').val(null);
			$('input[name="maxValue"]').attr("disabled", "disabled");		$('input[name="maxValue"]').val(null);
		}
	}
}
</script>
