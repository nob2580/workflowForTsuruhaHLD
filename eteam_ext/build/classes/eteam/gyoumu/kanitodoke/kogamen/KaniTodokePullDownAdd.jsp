<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!-- タイトル -->
<c:if test='${dispMode eq "1"}'><h1>届書プルダウン追加</h1></c:if>
<c:if test='${dispMode eq "2"}'><h1>届書プルダウン変更</h1></c:if>

<!-- エラー -->
<%@ include file="/jsp/eteam/include/InputError.jsp" %>

<!-- メイン -->
<div id='dialogMain'>

	<!-- 入力フィールド -->
	<section id='pulldownAddSection'>
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
					<label class="control-label label-sub"><span class='required'>*</span>選択項目</label>
					<div class='controls'>
						<select name='pulldownList' class='input-large' onchange="changeList()">
							<c:forEach var='pulldownList' items='${pulldownList}'>
								<option value='${su:htmlEscape(pulldownList.select_item)}' <c:if test='${pulldownList.selected}'>selected</c:if>>${su:htmlEscape(pulldownList.select_item)}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class='control-group'>
					<c:if test='${dispMode eq "1"}'><button type='button' onclick='add()' class='btn'><i class='icon-plus'></i> 追加</button></c:if>
					<c:if test='${dispMode eq "2"}'><button type='button' onclick='add()' class='btn'><i class='icon-refresh'></i> 変更</button></c:if>
				</div>
				
				<section>
					<div class='no-more-tables'>
					<table class='table-bordered table-condensed' id="pulldownOptionList">
						<thead>
							<tr>
								<th>名称</th>
								<th>コード</th>
							</tr>
						</thead>
						<tbody id='pulldownItems'>
							<c:forEach var='pulldownOptionList' items='${pulldownOptionList}'>
							<tr>
								<td>${su:htmlEscape(pulldownOptionList.name)}</td>
								<td>
								    ${su:htmlEscape(pulldownOptionList.cd)}
								    <input type='hidden' name="select_item" value='${su:htmlEscape(pulldownOptionList.select_item)}'>
								    <input type='hidden' name="name" value='${su:htmlEscape(pulldownOptionList.name)}'>
								    <input type='hidden' name="cd" value='${su:htmlEscape(pulldownOptionList.cd)}'>
								</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
					</div>
				</section>
			</form>
		</div>
	</section>
</div><!-- main -->
<script type="text/javascript">
$(document).ready(function(){
	changeList();
});

function changeList() {
	
	var val = $("select[name=pulldownList] option:selected").val();
	
	$('table#pulldownOptionList tbody tr').each(function(){

		var valTmp = $(this).find("input[name=select_item]").val();
		
		$(this).show();

		if(val != valTmp)
		{
			$(this).hide();
		}
	});
}

/**
 * ラジオボタンフィールドを親画面に追加する
 */
function add() {
	
	// ラベル名
	var label_name = $("input[name=labelName]").val();

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
	
	if ($("select[name=pulldownList] option:selected").val() == "") {
		alert("選択項目を入力してください。");
		return;
	}
	
	var area_kbn = getAreaKbn();
	var hyouji_jun = parseInt($("input[name=" + area_kbn + "ItemCnt]").val()) + 1;
	var item_name = area_kbn + ("0" + hyouji_jun).slice(-2);
	var value_name = item_name + "_val1";
	
	// 入力項目
	var select = $("<select class='input-large'></select>");
	if($("input:radio[name=hissuFlg]:checked").val() == "0"){
		select.append($("<option></option>"));
	}
	$('table#pulldownOptionList tbody tr').each(function(){
		var option_text = $(this).find("input[name=name]").val();
		var option_value = $(this).find("input[name=cd]").val();
		var select_item = $(this).find("input[name=select_item]").val();
		if ($(this).is(':visible')) {
			var opt = $("<option></option>");
			opt.text(option_text);
			opt.attr("data-item",select_item);
			opt.val(option_value);
		}
		select.append(opt);
	});
	
	select.attr("name", value_name);
	var tmpdiv = $("<div></div>");
	tmpdiv.append(select);
	
	// 入力項目を追加する
	addInputItem(area_kbn, hyouji_jun, label_name, item_name, "pulldown", value_name, tmpdiv.html());

	// 画面を閉じる
	$("#dialog").dialog("close");
}
//初期表示処理
$("#dialog").ready(function(){

	// 補助機能部品の再呼出
	commonInit($("#pulldownAddSection").children());
});

</script>
