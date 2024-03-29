<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.hf1ShiyouFlg and ks.hf1.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
	<label class='control-label'><c:if test='${ks.hf1.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.hf1Name)}</label>
	<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.hf1ShiyouFlg or "1" eq hfUfSeigyo.hf1ShiyouFlg}'>
		<input type='text' name="hf1Cd" maxlength='20' value='${su:htmlEscape(hf1Cd)}' <c:if test='${"HF1" eq shainCdRenkeiArea}'>disabled</c:if>>
		<input type='hidden' name="hf1Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.hf1ShiyouFlg or "3" eq hfUfSeigyo.hf1ShiyouFlg}'>
		<input type='text' name='hf1Cd' class='pc_only' <c:if test='${"HF1" eq shainCdRenkeiArea}'>disabled</c:if> value='${su:htmlEscape(hf1Cd)}'>
		<input type='text' name='hf1Name' class='input-xlarge' disabled value='${su:htmlEscape(hf1Name)}' <c:if test='${"HF1" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
		<button type='button' id='hf1SentakuButton' class='btn btn-small' <c:if test='${"HF1" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>選択</button>
		<button type='button' id='hf1ClearButton' class='btn btn-small' <c:if test='${"HF1" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>クリア</button>
</c:if>
		</div>
</div>
		
<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.hf2ShiyouFlg and ks.hf2.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
	<label class='control-label'><c:if test='${ks.hf2.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.hf2Name)}</label>
	<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.hf2ShiyouFlg or "1" eq hfUfSeigyo.hf2ShiyouFlg}'>
		<input type='text' name="hf2Cd" maxlength='20' value='${su:htmlEscape(hf2Cd)}' <c:if test='${"HF2" eq shainCdRenkeiArea}'>disabled</c:if> >
		<input type='hidden' name="hf2Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.hf2ShiyouFlg or "3" eq hfUfSeigyo.hf2ShiyouFlg}'>
		<input type='text' name='hf2Cd' class='pc_only' <c:if test='${"HF2" eq shainCdRenkeiArea}'>disabled</c:if> value='${su:htmlEscape(hf2Cd)}'>
		<input type='text' name='hf2Name' class='input-xlarge' disabled value='${su:htmlEscape(hf2Name)}' <c:if test='${"HF2" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
		<button type='button' id='hf2SentakuButton' class='btn btn-small' <c:if test='${"HF2" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>選択</button>
		<button type='button' id='hf2ClearButton' class='btn btn-small' <c:if test='${"HF2" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>クリア</button>
</c:if>
	</div>
</div>
		
<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.hf3ShiyouFlg and ks.hf3.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
	<label class='control-label'><c:if test='${ks.hf3.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.hf3Name)}</label>
	<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.hf3ShiyouFlg or "1" eq hfUfSeigyo.hf3ShiyouFlg}'>
		<input type='text' name="hf3Cd" maxlength='20' value='${su:htmlEscape(hf3Cd)}' <c:if test='${"HF3" eq shainCdRenkeiArea}'>disabled</c:if>>
		<input type='hidden' name="hf3Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.hf3ShiyouFlg or "3" eq hfUfSeigyo.hf3ShiyouFlg}'>
		<input type='text' name='hf3Cd' class='pc_only' <c:if test='${"HF3" eq shainCdRenkeiArea}'>disabled</c:if> value='${su:htmlEscape(hf3Cd)}'>
		<input type='text' name='hf3Name' class='input-xlarge' disabled value='${su:htmlEscape(hf3Name)}' <c:if test='${"HF3" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
		<button type='button' id='hf3SentakuButton' class='btn btn-small' <c:if test='${"HF3" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>選択</button>
		<button type='button' id='hf3ClearButton' class='btn btn-small' <c:if test='${"HF3" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>クリア</button>
</c:if>
	</div>
</div>

<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.hf4ShiyouFlg and ks.hf4.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
	<label class='control-label'><c:if test='${ks.hf4.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.hf4Name)}</label>
	<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.hf4ShiyouFlg or "1" eq hfUfSeigyo.hf4ShiyouFlg}'>
		<input type='text' name="hf4Cd" maxlength='20' value='${su:htmlEscape(hf4Cd)}' <c:if test='${"HF4" eq shainCdRenkeiArea}'>disabled</c:if>>
		<input type='hidden' name="hf4Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.hf4ShiyouFlg or "3" eq hfUfSeigyo.hf4ShiyouFlg}'>
		<input type='text' name='hf4Cd' class='pc_only' <c:if test='${"HF4" eq shainCdRenkeiArea}'>disabled</c:if> value='${su:htmlEscape(hf4Cd)}'>
		<input type='text' name='hf4Name' class='input-xlarge' disabled value='${su:htmlEscape(hf4Name)}' <c:if test='${"HF4" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
		<button type='button' id='hf4SentakuButton' class='btn btn-small' <c:if test='${"HF4" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>選択</button>
		<button type='button' id='hf4ClearButton' class='btn btn-small' <c:if test='${"HF4" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>クリア</button>
</c:if>
	</div>
</div>
<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.hf5ShiyouFlg and ks.hf5.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
	<label class='control-label'><c:if test='${ks.hf5.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.hf5Name)}</label>
	<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.hf5ShiyouFlg or "1" eq hfUfSeigyo.hf5ShiyouFlg}'>
		<input type='text' name="hf5Cd" maxlength='20' value='${su:htmlEscape(hf5Cd)}' <c:if test='${"HF5" eq shainCdRenkeiArea}'>disabled</c:if>>
		<input type='hidden' name="hf5Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.hf5ShiyouFlg or "3" eq hfUfSeigyo.hf5ShiyouFlg}'>
		<input type='text' name='hf5Cd' class='pc_only' <c:if test='${"HF5" eq shainCdRenkeiArea}'>disabled</c:if> value='${su:htmlEscape(hf5Cd)}'>
		<input type='text' name='hf5Name' class='input-xlarge' disabled value='${su:htmlEscape(hf5Name)}' <c:if test='${"HF5" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
		<button type='button' id='hf5SentakuButton' class='btn btn-small' <c:if test='${"HF5" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>選択</button>
		<button type='button' id='hf5ClearButton' class='btn btn-small' <c:if test='${"HF5" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>クリア</button>
</c:if>
	</div>
</div>
<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.hf6ShiyouFlg and ks.hf6.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
	<label class='control-label'><c:if test='${ks.hf6.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.hf6Name)}</label>
	<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.hf6ShiyouFlg or "1" eq hfUfSeigyo.hf6ShiyouFlg}'>
		<input type='text' name="hf6Cd" maxlength='20' value='${su:htmlEscape(hf6Cd)}' <c:if test='${"HF6" eq shainCdRenkeiArea}'>disabled</c:if>>
		<input type='hidden' name="hf6Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.hf6ShiyouFlg or "3" eq hfUfSeigyo.hf6ShiyouFlg}'>
		<input type='text' name='hf6Cd' class='pc_only' <c:if test='${"HF6" eq shainCdRenkeiArea}'>disabled</c:if> value='${su:htmlEscape(hf6Cd)}'>
		<input type='text' name='hf6Name' class='input-xlarge' disabled value='${su:htmlEscape(hf6Name)}' <c:if test='${"HF6" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
		<button type='button' id='hf6SentakuButton' class='btn btn-small' <c:if test='${"HF6" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>選択</button>
		<button type='button' id='hf6ClearButton' class='btn btn-small' <c:if test='${"HF6" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>クリア</button>
</c:if>
	</div>
</div>
<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.hf7ShiyouFlg and ks.hf7.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
	<label class='control-label'><c:if test='${ks.hf7.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.hf7Name)}</label>
	<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.hf7ShiyouFlg or "1" eq hfUfSeigyo.hf7ShiyouFlg}'>
		<input type='text' name="hf7Cd" maxlength='20' value='${su:htmlEscape(hf7Cd)}' <c:if test='${"HF7" eq shainCdRenkeiArea}'>disabled</c:if>>
		<input type='hidden' name="hf7Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.hf7ShiyouFlg or "3" eq hfUfSeigyo.hf7ShiyouFlg}'>
		<input type='text' name='hf7Cd' class='pc_only' <c:if test='${"HF7" eq shainCdRenkeiArea}'>disabled</c:if> value='${su:htmlEscape(hf7Cd)}'>
		<input type='text' name='hf7Name' class='input-xlarge' disabled value='${su:htmlEscape(hf7Name)}' <c:if test='${"HF7" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
		<button type='button' id='hf7SentakuButton' class='btn btn-small' <c:if test='${"HF7" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>選択</button>
		<button type='button' id='hf7ClearButton' class='btn btn-small' <c:if test='${"HF7" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>クリア</button>
</c:if>
	</div>
</div>
<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.hf8ShiyouFlg and ks.hf8.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
	<label class='control-label'><c:if test='${ks.hf8.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.hf8Name)}</label>
	<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.hf8ShiyouFlg or "1" eq hfUfSeigyo.hf8ShiyouFlg}'>
		<input type='text' name="hf8Cd" maxlength='20' value='${su:htmlEscape(hf8Cd)}' <c:if test='${"HF8" eq shainCdRenkeiArea}'>disabled</c:if>>
		<input type='hidden' name="hf8Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.hf8ShiyouFlg or "3" eq hfUfSeigyo.hf8ShiyouFlg}'>
		<input type='text' name='hf8Cd' class='pc_only' <c:if test='${"HF8" eq shainCdRenkeiArea}'>disabled</c:if> value='${su:htmlEscape(hf8Cd)}'>
		<input type='text' name='hf8Name' class='input-xlarge' disabled value='${su:htmlEscape(hf8Name)}' <c:if test='${"HF8" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
		<button type='button' id='hf8SentakuButton' class='btn btn-small' <c:if test='${"HF8" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>選択</button>
		<button type='button' id='hf8ClearButton' class='btn btn-small' <c:if test='${"HF8" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>クリア</button>
</c:if>
	</div>
</div>
<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.hf9ShiyouFlg and ks.hf9.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
	<label class='control-label'><c:if test='${ks.hf9.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.hf9Name)}</label>
	<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.hf9ShiyouFlg or "1" eq hfUfSeigyo.hf9ShiyouFlg}'>
		<input type='text' name="hf9Cd" maxlength='20' value='${su:htmlEscape(hf9Cd)}' <c:if test='${"HF9" eq shainCdRenkeiArea}'>disabled</c:if>>
		<input type='hidden' name="hf9Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.hf9ShiyouFlg or "3" eq hfUfSeigyo.hf9ShiyouFlg}'>
		<input type='text' name='hf9Cd' class='pc_only' <c:if test='${"HF9" eq shainCdRenkeiArea}'>disabled</c:if> value='${su:htmlEscape(hf9Cd)}'>
		<input type='text' name='hf9Name' class='input-xlarge' disabled value='${su:htmlEscape(hf9Name)}' <c:if test='${"HF9" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
		<button type='button' id='hf9SentakuButton' class='btn btn-small' <c:if test='${"HF9" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>選択</button>
		<button type='button' id='hf9ClearButton' class='btn btn-small' <c:if test='${"HF9" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>クリア</button>
</c:if>
	</div>
</div>
<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.hf10ShiyouFlg and ks.hf10.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
	<label class='control-label'><c:if test='${ks.hf10.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.hf10Name)}</label>
	<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.hf10ShiyouFlg or "1" eq hfUfSeigyo.hf10ShiyouFlg}'>
		<input type='text' name="hf10Cd" maxlength='20' value='${su:htmlEscape(hf10Cd)}' <c:if test='${"HF10" eq shainCdRenkeiArea}'>disabled</c:if>>
		<input type='hidden' name="hf10Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.hf10ShiyouFlg or "3" eq hfUfSeigyo.hf10ShiyouFlg}'>
		<input type='text' name='hf10Cd' class='pc_only' <c:if test='${"HF10" eq shainCdRenkeiArea}'>disabled</c:if> value='${su:htmlEscape(hf10Cd)}'>
		<input type='text' name='hf10Name' class='input-xlarge' disabled value='${su:htmlEscape(hf10Name)}' <c:if test='${"HF10" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
		<button type='button' id='hf10SentakuButton' class='btn btn-small' <c:if test='${"HF10" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>選択</button>
		<button type='button' id='hf10ClearButton' class='btn btn-small' <c:if test='${"HF10" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>クリア</button>
</c:if>
	</div>
</div>

<script style='text/javascript'>
<c:if test='${enableInput}'>
$(document).ready(function(){
	//ヘッダーフィールド１選択ボタン押下時、ダイアログ表示
	$("#hf1SentakuButton").click(function(){
		dialogRetHeaderFieldCd = $("input[name=hf1Cd]");
		dialogRetHeaderFieldName = $("input[name=hf1Name]");
		commonHeaderSentaku(1);
	});

	//ヘッダーフィールド１クリアボタン押下時、ヘッダーフィールド１クリア
	$("#hf1ClearButton").click(function(){
		$("[name=hf1Cd]").val("");
		$("[name=hf1Name]").val("");
	});

	//ヘッダーフィールド１コードロストフォーカス時、ヘッダーフィールド１名称表示
	$("input[name=hf1Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("button[id=hf1SentakuButton]").is(":visible");
		if (isBtnVisible) {
			dialogRetHeaderFieldCd = $(this);
			dialogRetHeaderFieldName = $(this).closest("div").find("input[name=hf1Name]");
			commonHeaderCdLostFocus(dialogRetHeaderFieldCd, dialogRetHeaderFieldName, 1, title);
		}
	});
	
	//ヘッダーフィールド２選択ボタン押下時、ダイアログ表示
	$("#hf2SentakuButton").click(function(){
		dialogRetHeaderFieldCd = $("input[name=hf2Cd]");
		dialogRetHeaderFieldName = $("input[name=hf2Name]");
		commonHeaderSentaku(2);
	});

	//ヘッダーフィールド２クリアボタン押下時、ヘッダーフィールド２クリア
	$("#hf2ClearButton").click(function(){
		$("[name=hf2Cd]").val("");
		$("[name=hf2Name]").val("");
	});
	
	//ヘッダーフィールド２コードロストフォーカス時、ヘッダーフィールド２名称表示
	$("input[name=hf2Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("button[id=hf2SentakuButton]").is(":visible");
		if (isBtnVisible) {
			dialogRetHeaderFieldCd = $(this);
			dialogRetHeaderFieldName = $(this).closest("div").find("input[name=hf2Name]");
			commonHeaderCdLostFocus(dialogRetHeaderFieldCd, dialogRetHeaderFieldName, 2, title);
		}
	});
	
	//ヘッダーフィールド３選択ボタン押下時、ダイアログ表示
	$("#hf3SentakuButton").click(function(){
		dialogRetHeaderFieldCd = $("input[name=hf3Cd]");
		dialogRetHeaderFieldName = $("input[name=hf3Name]");
		commonHeaderSentaku(3);
	});

	//ヘッダーフィールド３クリアボタン押下時、ヘッダーフィールド３クリア
	$("#hf3ClearButton").click(function(){
		$("[name=hf3Cd]").val("");
		$("[name=hf3Name]").val("");
	});
	
	//ヘッダーフィールド３コードロストフォーカス時、ヘッダーフィールド３名称表示
	$("input[name=hf3Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("button[id=hf3SentakuButton]").is(":visible");
		if (isBtnVisible) {
			dialogRetHeaderFieldCd = $(this);
			dialogRetHeaderFieldName = $(this).closest("div").find("input[name=hf3Name]");
			commonHeaderCdLostFocus(dialogRetHeaderFieldCd, dialogRetHeaderFieldName, 3, title);
		}
	});
	//ヘッダーフィールド４選択ボタン押下時、ダイアログ表示
	$("#hf4SentakuButton").click(function(){
		dialogRetHeaderFieldCd = $("input[name=hf4Cd]");
		dialogRetHeaderFieldName = $("input[name=hf4Name]");
		commonHeaderSentaku(4);
	});

	//ヘッダーフィールド４クリアボタン押下時、ヘッダーフィールド４クリア
	$("#hf4ClearButton").click(function(){
		$("[name=hf4Cd]").val("");
		$("[name=hf4Name]").val("");
	});
	
	//ヘッダーフィールド４コードロストフォーカス時、ヘッダーフィールド４名称表示
	$("input[name=hf4Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("button[id=hf4SentakuButton]").is(":visible");
		if (isBtnVisible) {
			dialogRetHeaderFieldCd = $(this);
			dialogRetHeaderFieldName = $(this).closest("div").find("input[name=hf4Name]");
			commonHeaderCdLostFocus(dialogRetHeaderFieldCd, dialogRetHeaderFieldName, 4, title);
		}
	});
	//ヘッダーフィールド５選択ボタン押下時、ダイアログ表示
	$("#hf5SentakuButton").click(function(){
		dialogRetHeaderFieldCd = $("input[name=hf5Cd]");
		dialogRetHeaderFieldName = $("input[name=hf5Name]");
		commonHeaderSentaku(5);
	});

	//ヘッダーフィールド５クリアボタン押下時、ヘッダーフィールド５クリア
	$("#hf5ClearButton").click(function(){
		$("[name=hf5Cd]").val("");
		$("[name=hf5Name]").val("");
	});
	
	//ヘッダーフィールド５コードロストフォーカス時、ヘッダーフィールド５名称表示
	$("input[name=hf5Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("button[id=hf5SentakuButton]").is(":visible");
		if (isBtnVisible) {
			dialogRetHeaderFieldCd = $(this);
			dialogRetHeaderFieldName = $(this).closest("div").find("input[name=hf5Name]");
			commonHeaderCdLostFocus(dialogRetHeaderFieldCd, dialogRetHeaderFieldName, 5, title);
		}
	});

	//ヘッダーフィールド６選択ボタン押下時、ダイアログ表示
	$("#hf6SentakuButton").click(function(){
		dialogRetHeaderFieldCd = $("input[name=hf6Cd]");
		dialogRetHeaderFieldName = $("input[name=hf6Name]");
		commonHeaderSentaku(6);
	});

	//ヘッダーフィールド６クリアボタン押下時、ヘッダーフィールド６クリア
	$("#hf6ClearButton").click(function(){
		$("[name=hf6Cd]").val("");
		$("[name=hf6Name]").val("");
	});
	
	//ヘッダーフィールド６コードロストフォーカス時、ヘッダーフィールド６名称表示
	$("input[name=hf6Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("button[id=hf6SentakuButton]").is(":visible");
		if (isBtnVisible) {
			dialogRetHeaderFieldCd = $(this);
			dialogRetHeaderFieldName = $(this).closest("div").find("input[name=hf6Name]");
			commonHeaderCdLostFocus(dialogRetHeaderFieldCd, dialogRetHeaderFieldName, 6, title);
		}
	});
	
	//ヘッダーフィールド７選択ボタン押下時、ダイアログ表示
	$("#hf7SentakuButton").click(function(){
		dialogRetHeaderFieldCd = $("input[name=hf7Cd]");
		dialogRetHeaderFieldName = $("input[name=hf7Name]");
		commonHeaderSentaku(7);
	});

	//ヘッダーフィールド７クリアボタン押下時、ヘッダーフィールド７クリア
	$("#hf7ClearButton").click(function(){
		$("[name=hf7Cd]").val("");
		$("[name=hf7Name]").val("");
	});
	
	//ヘッダーフィールド７コードロストフォーカス時、ヘッダーフィールド７名称表示
	$("input[name=hf7Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("button[id=hf7SentakuButton]").is(":visible");
		if (isBtnVisible) {
			dialogRetHeaderFieldCd = $(this);
			dialogRetHeaderFieldName = $(this).closest("div").find("input[name=hf7Name]");
			commonHeaderCdLostFocus(dialogRetHeaderFieldCd, dialogRetHeaderFieldName, 7, title);
		}
	});
	//ヘッダーフィールド８選択ボタン押下時、ダイアログ表示
	$("#hf8SentakuButton").click(function(){
		dialogRetHeaderFieldCd = $("input[name=hf8Cd]");
		dialogRetHeaderFieldName = $("input[name=hf8Name]");
		commonHeaderSentaku(8);
	});

	//ヘッダーフィールド８クリアボタン押下時、ヘッダーフィールド８クリア
	$("#hf8ClearButton").click(function(){
		$("[name=hf8Cd]").val("");
		$("[name=hf8Name]").val("");
	});
	
	//ヘッダーフィールド８コードロストフォーカス時、ヘッダーフィールド８名称表示
	$("input[name=hf8Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("button[id=hf8SentakuButton]").is(":visible");
		if (isBtnVisible) {
			dialogRetHeaderFieldCd = $(this);
			dialogRetHeaderFieldName = $(this).closest("div").find("input[name=hf8Name]");
			commonHeaderCdLostFocus(dialogRetHeaderFieldCd, dialogRetHeaderFieldName, 8, title);
		}
	});
	//ヘッダーフィールド９選択ボタン押下時、ダイアログ表示
	$("#hf9SentakuButton").click(function(){
		dialogRetHeaderFieldCd = $("input[name=hf9Cd]");
		dialogRetHeaderFieldName = $("input[name=hf9Name]");
		commonHeaderSentaku(9);
	});

	//ヘッダーフィールド９クリアボタン押下時、ヘッダーフィールド９クリア
	$("#hf9ClearButton").click(function(){
		$("[name=hf9Cd]").val("");
		$("[name=hf9Name]").val("");
	});
	
	//ヘッダーフィールド９コードロストフォーカス時、ヘッダーフィールド９名称表示
	$("input[name=hf9Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("button[id=hf9SentakuButton]").is(":visible");
		if (isBtnVisible) {
			dialogRetHeaderFieldCd = $(this);
			dialogRetHeaderFieldName = $(this).closest("div").find("input[name=hf9Name]");
			commonHeaderCdLostFocus(dialogRetHeaderFieldCd, dialogRetHeaderFieldName, 9, title);
		}
	});
	
	//ヘッダーフィールド１０選択ボタン押下時、ダイアログ表示
	$("#hf10SentakuButton").click(function(){
		dialogRetHeaderFieldCd = $("input[name=hf10Cd]");
		dialogRetHeaderFieldName = $("input[name=hf10Name]");
		commonHeaderSentaku(10);
	});

	//ヘッダーフィールド１０クリアボタン押下時、ヘッダーフィールド１０クリア
	$("#hf10ClearButton").click(function(){
		$("[name=hf10Cd]").val("");
		$("[name=hf10Name]").val("");
	});
	
	//ヘッダーフィールド１０コードロストフォーカス時、ヘッダーフィールド１０名称表示
	$("input[name=hf10Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("button[id=hf10SentakuButton]").is(":visible");
		if (isBtnVisible) {
			dialogRetHeaderFieldCd = $(this);
			dialogRetHeaderFieldName = $(this).closest("div").find("input[name=hf10Name]");
			commonHeaderCdLostFocus(dialogRetHeaderFieldCd, dialogRetHeaderFieldName, 10, title);
		}
	});
});
</c:if>
</script>