<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!-- タイトル -->
<c:if test='${dispMode eq "1"}'><h1>届書チェックボックス追加</h1></c:if>
<c:if test='${dispMode eq "2"}'><h1>届書チェックボックス変更</h1></c:if>

<!-- エラー -->
<%@ include file="/jsp/eteam/include/InputError.jsp" %>

<!-- メイン -->
<div id='dialogMain'>

	<!-- 入力フィールド -->
	<section id='checkBoxAddSection'>
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
					<label class="control-label label-sub">チェックボックス名</label>
					<div class='controls'>
						<input type='text' name='checkBoxName' maxlength='20' value='${su:htmlEscape(checkBoxName)}'>
					</div>
				</div>
				<div class='control-group'>
					<c:if test='${dispMode eq "1"}'><button type='button' onclick='add()' class='btn'><i class='icon-plus'></i> 追加</button></c:if>
					<c:if test='${dispMode eq "2"}'><button type='button' onclick='add()' class='btn'><i class='icon-refresh'></i> 変更</button></c:if>
				</div>
			</form>
		</div>
	</section>
</div><!-- main -->
<script type="text/javascript">
/**
 * プルダウンフィールドを親画面に追加する
 */
function add() {
	
	var label_name = $("input[name=labelName]").val();					// ラベル名
	var checkbox_label_name = $("input[name=checkBoxName]").val();		// チェックボックスラベル名
	
	// 入力チェック
	if (label_name == "") {
		alert("ラベル名を入力してください。");
		return;
	} else {
		if(containEscapeHTML(label_name)) {
			alert("ラベル名に使用できない文字が含まれています。");
			return;
		}
	}

	if (checkbox_label_name != "" && containEscapeHTML(checkbox_label_name)) {
		alert("チェックボックスラベル名に使用できない文字が含まれています。");
		return;
	}
	
	var area_kbn = getAreaKbn();
	var hyouji_jun = parseInt($("input[name=" + area_kbn + "ItemCnt]").val()) + 1;
	var item_name = area_kbn + ("0" + hyouji_jun).slice(-2);
	var value_name = item_name + "_val1";
	
	// 入力項目
	var inputItem = "";	
	
	inputItem += "<input type='hidden' name='{value_name}' value='0'>";	
	inputItem += "<label class='checkbox inline'><input type='checkbox' name='{value_name}_1' value='1'>{checkbox_label_name}</label>";	
	inputItem = inputItem.replace("{value_name}",value_name);						// 値名
	inputItem = inputItem.replace("{value_name}",value_name);						// 値名
	inputItem = inputItem.replace("{checkbox_label_name}",checkbox_label_name);		// チェックボックスラベル名
	
	// 入力項目を追加する
	addInputItem(area_kbn, hyouji_jun, label_name, item_name, "checkbox", value_name, inputItem);

	// 画面を閉じる
	$("#dialog").dialog("close");
}
//初期表示処理
$("#dialog").ready(function(){

	// 補助機能部品の再呼出
	commonInit($("#checkBoxAddSection").children());
});
</script>
