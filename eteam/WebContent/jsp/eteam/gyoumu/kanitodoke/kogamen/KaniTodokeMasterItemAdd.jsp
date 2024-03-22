<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!-- タイトル -->
<c:choose>
	<c:when test='${gamenTitle eq ""}'>
		<c:if test='${dispMode eq "1"}'><h1>届書マスター追加</h1></c:if>
		<c:if test='${dispMode eq "2"}'><h1>届書マスター変更</h1></c:if>
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
	<section id='masterAddSection'>
		<div class="form-horizontal">
			<form class="form-inline">
				<div class='control-group'>
					<label class="control-label label-sub"><span class='required'>*</span>マスター</label>
					<div class='controls'>
						<select name='masterList' class='input-large' onchange="changeList()">
<c:if test='${gamenTitle eq ""}'>
							<option selected></option>
</c:if>
							<c:forEach var='masterList' items='${masterList}'>
								<option value='${su:htmlEscape(masterList.cd)}' <c:if test='${masterKbn eq masterList.cd}'>selected</c:if>>${su:htmlEscape(masterList.name)}</option>
							</c:forEach>
						</select>
					</div>
				</div>
<c:if test='${gamenTitle ne ""}'>
				<div class='control-group'>
					<label class="control-label label-sub"><span class='required'>*</span>ラベル名</label>
					<div class='controls'>
						<input type='text' name='labelName' maxlength='10' value='${su:htmlEscape(labelName)}'>
					</div>
				</div>
</c:if>
				<div class='control-group'>
					<label class="control-label label-sub"><span class='required'>*</span>必須有無</label>
					<div class='controls'>
						<label class='radio inline'><input type='radio' name='hissuFlg' value='1' <c:if test='${hissuFlg eq "1"}'>checked</c:if>>有</label>
						<label class='radio inline'><input type='radio' name='hissuFlg' value='0' <c:if test='${hissuFlg eq "0"}'>checked</c:if>>無</label>
					</div>
				</div>
				<div class='control-group'>
					<c:if test='${dispMode eq "1"}'><button type='button' onclick='add()' class='btn'><i class='icon-plus'></i> 追加</button></c:if>
					<c:if test='${dispMode eq "2"}'><button type='button' onclick='add()' class='btn'><i class='icon-refresh'></i> 変更</button></c:if>
				</div>
				
				<section id='masterImage'>
					<c:if test='${dispMode eq "1"}'><h2>追加イメージ</h2></c:if>
					<c:if test='${dispMode eq "2"}'><h2>変更イメージ</h2></c:if>
<c:if test='${gamenTitle eq ""}'>
					<input type='hidden' name='labelName'>
</c:if>
					<input type='hidden' name='gamenTitle' value='${gamenTitle}'>
					<div class='control-group'></div>
				</section>
				<input type='hidden' name='tmpYosanId' value='${su:htmlEscape(yosanId)}'>
			</form>
		</div>
	</section>
</div><!-- main -->
<script type="text/javascript">
$(document).ready(function(){
	changeList();
});

/**
 * プルダウン変更時、追加する項目サンプルイメージを表示する
 */
function changeList() {

	var val = $("select[name=masterList] option:selected").val();
	var txt = $("select[name=masterList] option:selected").text();
	var yosan_id = $("input[name='tmpYosanId']").val();				// 予算執行項目ID

	// 行を全て削除
	$("#masterImage").find("button[name=select_button]").remove();
	$("#masterImage").find("button[name=clear_button]").remove();
	$("#masterImage").find("input[name=master_kbn]").remove();
	$("#masterImage").find("input[name=val1]").remove();
	$("#masterImage").find("input[name=val2]").remove();
//	$("#masterImage").find("input[name=yosanId]").remove();

	var label_name = "";
	var image = "";
	
	switch (val){
	  case "futanBumonSentaku":
	  case "kanjyouKamokuSentaku":
		label_name = txt;
		image += "<input type='hidden' name='master_kbn' value='" + val + "' yosanId='" + yosan_id + "'>";
		image += "<input type='text' name='val1' class='input-small' value='' onblur='" + val + "LostFocus(this)'> ";
		image += "<input type='text' name='val2' class='input-xlarge' value='' disabled> ";
		image += "<button type='button' name='select_button' class='btn btn-small' onclick='" + val + "(this)' disabled>選択</button> ";
		image += "<button type='button' name='clear_button' class='btn btn-small' onclick='clearMaster(this)' disabled>クリア</button>";
		break;
	  case "kanjyouKamokuEdabanSentaku":
	  case "torihikisakiSentaku":
		label_name = txt;
		image += "<input type='hidden' name='master_kbn' value='" + val + "' yosanId='" + yosan_id + "'>";
		image += "<input type='text' name='val1' class='input-medium' value='' onblur='" + val + "LostFocus(this)'> ";
		image += "<input type='text' name='val2' class='input-xlarge' value='' disabled> ";
		image += "<button type='button' name='select_button' class='btn btn-small' onclick='" + val + "(this)' disabled>選択</button> ";
		image += "<button type='button' name='clear_button' class='btn btn-small' onclick='clearMaster(this)' disabled>クリア</button>";
		break;
	  case "uf1Sentaku":
	  case "uf2Sentaku":
	  case "uf3Sentaku":
		label_name = txt;
		image += "<input type='hidden' name='master_kbn' value='" + val + "' yosanId='" + yosan_id + "'>";
		image += "<input type='text' name='val1' class='input-large' value='' onblur='" + val + "LostFocus(this)'> ";
		image += "<input type='text' name='val2' class='input-xlarge' value='' disabled> ";
		image += "<button type='button' name='select_button' class='btn btn-small' onclick='" + val + "(this)' disabled>選択</button> ";
		image += "<button type='button' name='clear_button' class='btn btn-small' onclick='clearMaster(this)' disabled>クリア</button>";
		break;
	}

	// 予算執行の入力部品以外の場合にプルダウン要素の名称をラベル名を設定する。
	var gamen_title = $("input[name=gamenTitle]").val();
	if (gamen_title == "") {
		$("input[name=labelName]").val(label_name);
	}
	$("#masterImage").find("div.control-group").append(image);
}

/**
 * プルダウンフィールドを親画面に追加する
 */
function add() {

	/*
	 * 入力チェック（先行分）
	 */

	// マスターの選択必須チェック
	if ($("select[name=masterList] option:selected").val() == "") {
		alert("選択項目を入力してください。");
		return;
	}

	var label_name = $("input[name=labelName]").val();
	var area_kbn = getAreaKbn();
	var hyouji_jun = parseInt($("input[name=" + area_kbn + "ItemCnt]").val()) + 1;
	var item_name = area_kbn + ("0" + hyouji_jun).slice(-2);
	var select_button = item_name + "_btn";
	var clear_button = item_name + "_clrbtn";
	var value1_name = item_name + "_val1";
	var value2_name = item_name + "_val2";
	var div = $("#masterImage").find("div");
	var yosan_id = $("input[name=tmpYosanId]").val();								// 予算執行項目ID

	/*
	 * 入力チェック
	 */

	// ラベル名の入力必須、特殊文字チェック
	if (yosan_id != "") {
		// 予算執行項目の場合にチェックを実施する。
		if (label_name == "") {
			alert("ラベル名を入力してください。");
			return;
		} else {
			if(containEscapeHTML(label_name)) {
				alert("ラベル名に使用できない文字が含まれています。");
				return;
			}
		}
	}

	// ボタンを活性状態にする(子画面から子画面を呼び出さないようにするため)
	div.find("button").removeAttr("disabled");

	// 入力項目
	var inputItem = div.html();
	inputItem = inputItem.replace("button type='button' disabled", "button type='button'");
	inputItem = inputItem.replace("val1", value1_name);
	inputItem = inputItem.replace("val2", value2_name);
	inputItem = inputItem.replace("select_button", select_button);
	inputItem = inputItem.replace("clear_button", clear_button);

	// 入力項目を追加する
	addInputItem(area_kbn, hyouji_jun, label_name, item_name, "master", value1_name, inputItem);

	// 画面を閉じる
	$("#dialog").dialog("close");
}
</script>
