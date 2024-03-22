<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- ユニバーサルフィールド(申請画面)用PartialView -->
<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf1ShiyouFlg and ks.uf1.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
			<label class='control-label'><c:if test='${ks.uf1.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf1Name)}</label>
			<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or "1" eq hfUfSeigyo.uf1ShiyouFlg}'>
					<input type='text' name="uf1Cd"  maxlength='20' value='${su:htmlEscape(uf1Cd)}' <c:if test='${"UF1" eq shainCdRenkeiArea or not uf1Enable}'>disabled</c:if>>
					<input type='hidden' name="uf1Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf1ShiyouFlg or "3" eq hfUfSeigyo.uf1ShiyouFlg}'>
					<input type='text' name="uf1Cd" class='pc_only' value='${su:htmlEscape(uf1Cd)}' <c:if test='${"UF1" eq shainCdRenkeiArea or not uf1Enable}'>disabled</c:if>>
					<input type='text' name="uf1Name" class='input-xlarge' value='${su:htmlEscape(uf1Name)}' disabled <c:if test='${"UF1" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' id='uf1SentakuButton' class='btn btn-small' <c:if test='${"UF1" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf1Enable}'>disabled</c:if>>選択</button>
					<button type='button' id='uf1ClearButton' class='btn btn-small' <c:if test='${"UF1" eq shainCdRenkeiArea or not uf1Enable}'>style='display:none;'</c:if>>クリア</button>
</c:if>
			</div>
		</div>
        <div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf2ShiyouFlg and ks.uf2.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
			<label class='control-label'><c:if test='${ks.uf2.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf2Name)}</label>
			<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or "1" eq hfUfSeigyo.uf2ShiyouFlg}'>
					<input type='text' name="uf2Cd" maxlength='20' value='${su:htmlEscape(uf2Cd)}' <c:if test='${"UF2" eq shainCdRenkeiArea or not uf2Enable}'>disabled</c:if>>
					<input type='hidden' name="uf2Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf2ShiyouFlg or "3" eq hfUfSeigyo.uf2ShiyouFlg}'>
					<input type='text' name="uf2Cd"  class='pc_only' <c:if test='${"UF2" eq shainCdRenkeiArea or not uf2Enable}'>disabled</c:if> value='${su:htmlEscape(uf2Cd)}'>
					<input type='text' name="uf2Name" class='input-xlarge' disabled value='${su:htmlEscape(uf2Name)}' <c:if test='${"UF2" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' id='uf2SentakuButton' class='btn btn-small' <c:if test='${"UF2" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf2Enable}'>disabled</c:if>>選択</button>
					<button type='button' id='uf2ClearButton' class='btn btn-small' <c:if test='${"UF2" eq shainCdRenkeiArea or not uf2Enable}'>style='display:none;'</c:if>>クリア</button>
</c:if>
			</div>
		</div>
        <div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf3ShiyouFlg and ks.uf3.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
			<label class='control-label'><c:if test='${ks.uf3.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf3Name)}</label>
			<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or "1" eq hfUfSeigyo.uf3ShiyouFlg}'>
					<input type='text' name="uf3Cd" maxlength='20' value='${su:htmlEscape(uf3Cd)}' <c:if test='${"UF3" eq shainCdRenkeiArea or not uf3Enable}'>disabled</c:if>>
					<input type='hidden' name="uf3Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf3ShiyouFlg or "3" eq hfUfSeigyo.uf3ShiyouFlg}'>
					<input type='text' name="uf3Cd" class='pc_only' <c:if test='${"UF3" eq shainCdRenkeiArea or not uf3Enable}'>disabled</c:if> value='${su:htmlEscape(uf3Cd)}' >
					<input type='text' name="uf3Name" class='input-xlarge' disabled value='${su:htmlEscape(uf3Name)}' <c:if test='${"UF3" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' id='uf3SentakuButton' class='btn btn-small' <c:if test='${"UF3" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf3Enable}'>disabled</c:if>>選択</button>
					<button type='button' id='uf3ClearButton' class='btn btn-small' <c:if test='${"UF3" eq shainCdRenkeiArea or not uf3Enable}'>style='display:none;'</c:if>>クリア</button>
</c:if>
			</div>
		</div>
<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf4ShiyouFlg and ks.uf4.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'>
        <c:if test='${ks.uf4.hissuFlg}'>
            <span class='required'>*</span>
        </c:if>${su:htmlEscape(hfUfSeigyo.uf4Name)}</label>
    <div class='controls'>
        <c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or "1" eq hfUfSeigyo.uf4ShiyouFlg}'>
            <input type='text' name="uf4Cd" maxlength='20' value='${su:htmlEscape(uf4Cd)}' <c:if test='${"UF4" eq shainCdRenkeiArea or not uf4Enable}'>disabled</c:if>>
        <input type='hidden' name="uf4Name" value=''>
        </c:if>
        <c:if test='${"2" eq hfUfSeigyo.uf4ShiyouFlg or "3" eq hfUfSeigyo.uf4ShiyouFlg}'>
            <input type='text' name="uf4Cd" class='pc_only' <c:if test='${"UF4" eq shainCdRenkeiArea or not uf4Enable}'>disabled</c:if> value='${su:htmlEscape(uf4Cd)}' >
        <input type='text' name="uf4Name" class='input-xlarge' disabled value='${su:htmlEscape(uf4Name)}' <c:if test='${"UF4" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
        <button type='button' id='uf4SentakuButton' class='btn btn-small' <c:if test='${"UF4" eq shainCdRenkeiArea}'>style='display:none;'</c:if>
            <c:if test='${not uf4Enable}'>disabled</c:if>>選択</button>
        <button type='button' id='uf4ClearButton' class='btn btn-small' <c:if test='${"UF4" eq shainCdRenkeiArea or not uf4Enable}'>style='display:none;'</c:if>>クリア</button>
        </c:if>
    </div>
</div>
<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf5ShiyouFlg and ks.uf5.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'>
        <c:if test='${ks.uf5.hissuFlg}'>
            <span class='required'>*</span>
        </c:if>${su:htmlEscape(hfUfSeigyo.uf5Name)}</label>
    <div class='controls'>
        <c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or "1" eq hfUfSeigyo.uf5ShiyouFlg}'>
            <input type='text' name="uf5Cd" maxlength='20' value='${su:htmlEscape(uf5Cd)}' <c:if test='${"UF5" eq shainCdRenkeiArea or not uf5Enable}'>disabled</c:if>>
        <input type='hidden' name="uf5Name" value=''>
        </c:if>
        <c:if test='${"2" eq hfUfSeigyo.uf5ShiyouFlg or "3" eq hfUfSeigyo.uf5ShiyouFlg}'>
            <input type='text' name="uf5Cd" class='pc_only' <c:if test='${"UF5" eq shainCdRenkeiArea or not uf5Enable}'>disabled</c:if> value='${su:htmlEscape(uf5Cd)}' >
        <input type='text' name="uf5Name" class='input-xlarge' disabled value='${su:htmlEscape(uf5Name)}' <c:if test='${"UF5" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
        <button type='button' id='uf5SentakuButton' class='btn btn-small' <c:if test='${"UF5" eq shainCdRenkeiArea}'>style='display:none;'</c:if>
            <c:if test='${not uf5Enable}'>disabled</c:if>>選択</button>
        <button type='button' id='uf5ClearButton' class='btn btn-small' <c:if test='${"UF5" eq shainCdRenkeiArea or not uf5Enable}'>style='display:none;'</c:if>>クリア</button>
        </c:if>
    </div>
</div>
<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf6ShiyouFlg and ks.uf6.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'>
        <c:if test='${ks.uf6.hissuFlg}'>
            <span class='required'>*</span>
        </c:if>${su:htmlEscape(hfUfSeigyo.uf6Name)}</label>
    <div class='controls'>
        <c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or "1" eq hfUfSeigyo.uf6ShiyouFlg}'>
            <input type='text' name="uf6Cd" maxlength='20' value='${su:htmlEscape(uf6Cd)}' <c:if test='${"UF6" eq shainCdRenkeiArea or not uf6Enable}'>disabled</c:if>>
        <input type='hidden' name="uf6Name" value=''>
        </c:if>
        <c:if test='${"2" eq hfUfSeigyo.uf6ShiyouFlg or "3" eq hfUfSeigyo.uf6ShiyouFlg}'>
            <input type='text' name="uf6Cd" class='pc_only' <c:if test='${"UF6" eq shainCdRenkeiArea or not uf6Enable}'>disabled</c:if> value='${su:htmlEscape(uf6Cd)}' >
        <input type='text' name="uf6Name" class='input-xlarge' disabled value='${su:htmlEscape(uf6Name)}' <c:if test='${"UF6" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
        <button type='button' id='uf6SentakuButton' class='btn btn-small' <c:if test='${"UF6" eq shainCdRenkeiArea}'>style='display:none;'</c:if>
            <c:if test='${not uf6Enable}'>disabled</c:if>>選択</button>
        <button type='button' id='uf6ClearButton' class='btn btn-small' <c:if test='${"UF6" eq shainCdRenkeiArea or not uf6Enable}'>style='display:none;'</c:if>>クリア</button>
        </c:if>
    </div>
</div>
<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf7ShiyouFlg and ks.uf7.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'>
        <c:if test='${ks.uf7.hissuFlg}'>
            <span class='required'>*</span>
        </c:if>${su:htmlEscape(hfUfSeigyo.uf7Name)}</label>
    <div class='controls'>
        <c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or "1" eq hfUfSeigyo.uf7ShiyouFlg}'>
            <input type='text' name="uf7Cd" maxlength='20' value='${su:htmlEscape(uf7Cd)}' <c:if test='${"UF7" eq shainCdRenkeiArea or not uf7Enable}'>disabled</c:if>>
        <input type='hidden' name="uf7Name" value=''>
        </c:if>
        <c:if test='${"2" eq hfUfSeigyo.uf7ShiyouFlg or "3" eq hfUfSeigyo.uf7ShiyouFlg}'>
            <input type='text' name="uf7Cd" class='pc_only' <c:if test='${"UF7" eq shainCdRenkeiArea or not uf7Enable}'>disabled</c:if> value='${su:htmlEscape(uf7Cd)}' >
        <input type='text' name="uf7Name" class='input-xlarge' disabled value='${su:htmlEscape(uf7Name)}' <c:if test='${"UF7" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
        <button type='button' id='uf7SentakuButton' class='btn btn-small' <c:if test='${"UF7" eq shainCdRenkeiArea}'>style='display:none;'</c:if>
            <c:if test='${not uf7Enable}'>disabled</c:if>>選択</button>
        <button type='button' id='uf7ClearButton' class='btn btn-small' <c:if test='${"UF7" eq shainCdRenkeiArea or not uf7Enable}'>style='display:none;'</c:if>>クリア</button>
        </c:if>
    </div>
</div>
<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf8ShiyouFlg and ks.uf8.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'>
        <c:if test='${ks.uf8.hissuFlg}'>
            <span class='required'>*</span>
        </c:if>${su:htmlEscape(hfUfSeigyo.uf8Name)}</label>
    <div class='controls'>
        <c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or "1" eq hfUfSeigyo.uf8ShiyouFlg}'>
            <input type='text' name="uf8Cd" maxlength='20' value='${su:htmlEscape(uf8Cd)}' <c:if test='${"UF8" eq shainCdRenkeiArea or not uf8Enable}'>disabled</c:if>>
        <input type='hidden' name="uf8Name" value=''>
        </c:if>
        <c:if test='${"2" eq hfUfSeigyo.uf8ShiyouFlg or "3" eq hfUfSeigyo.uf8ShiyouFlg}'>
            <input type='text' name="uf8Cd" class='pc_only' <c:if test='${"UF8" eq shainCdRenkeiArea or not uf8Enable}'>disabled</c:if> value='${su:htmlEscape(uf8Cd)}' >
        <input type='text' name="uf8Name" class='input-xlarge' disabled value='${su:htmlEscape(uf8Name)}' <c:if test='${"UF8" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
        <button type='button' id='uf8SentakuButton' class='btn btn-small' <c:if test='${"UF8" eq shainCdRenkeiArea}'>style='display:none;'</c:if>
            <c:if test='${not uf8Enable}'>disabled</c:if>>選択</button>
        <button type='button' id='uf8ClearButton' class='btn btn-small' <c:if test='${"UF8" eq shainCdRenkeiArea or not uf8Enable}'>style='display:none;'</c:if>>クリア</button>
        </c:if>
    </div>
</div>
<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf9ShiyouFlg and ks.uf9.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'>
        <c:if test='${ks.uf9.hissuFlg}'>
            <span class='required'>*</span>
        </c:if>${su:htmlEscape(hfUfSeigyo.uf9Name)}</label>
    <div class='controls'>
        <c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or "1" eq hfUfSeigyo.uf9ShiyouFlg}'>
            <input type='text' name="uf9Cd" maxlength='20' value='${su:htmlEscape(uf9Cd)}' <c:if test='${"UF9" eq shainCdRenkeiArea or not uf9Enable}'>disabled</c:if>>
        <input type='hidden' name="uf9Name" value=''>
        </c:if>
        <c:if test='${"2" eq hfUfSeigyo.uf9ShiyouFlg or "3" eq hfUfSeigyo.uf9ShiyouFlg}'>
            <input type='text' name="uf9Cd" class='pc_only' <c:if test='${"UF9" eq shainCdRenkeiArea or not uf9Enable}'>disabled</c:if> value='${su:htmlEscape(uf9Cd)}' >
        <input type='text' name="uf9Name" class='input-xlarge' disabled value='${su:htmlEscape(uf9Name)}' <c:if test='${"UF9" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
        <button type='button' id='uf9SentakuButton' class='btn btn-small' <c:if test='${"UF9" eq shainCdRenkeiArea}'>style='display:none;'</c:if>
            <c:if test='${not uf9Enable}'>disabled</c:if>>選択</button>
        <button type='button' id='uf9ClearButton' class='btn btn-small' <c:if test='${"UF9" eq shainCdRenkeiArea or not uf9Enable}'>style='display:none;'</c:if>>クリア</button>
        </c:if>
    </div>
</div>
<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf10ShiyouFlg and ks.uf10.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'>
        <c:if test='${ks.uf10.hissuFlg}'>
            <span class='required'>*</span>
        </c:if>${su:htmlEscape(hfUfSeigyo.uf10Name)}</label>
    <div class='controls'>
        <c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or "1" eq hfUfSeigyo.uf10ShiyouFlg}'>
            <input type='text' name="uf10Cd" maxlength='20' value='${su:htmlEscape(uf10Cd)}' <c:if test='${"UF10" eq shainCdRenkeiArea or not uf10Enable}'>disabled</c:if>>
        <input type='hidden' name="uf10Name" value=''>
        </c:if>
        <c:if test='${"2" eq hfUfSeigyo.uf10ShiyouFlg or "3" eq hfUfSeigyo.uf10ShiyouFlg}'>
            <input type='text' name="uf10Cd" class='pc_only' <c:if test='${"UF10" eq shainCdRenkeiArea or not uf10Enable}'>disabled</c:if> value='${su:htmlEscape(uf10Cd)}' >
        <input type='text' name="uf10Name" class='input-xlarge' disabled value='${su:htmlEscape(uf10Name)}' <c:if test='${"UF10" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
        <button type='button' id='uf10SentakuButton' class='btn btn-small' <c:if test='${"UF10" eq shainCdRenkeiArea}'>style='display:none;'</c:if>
            <c:if test='${not uf10Enable}'>disabled</c:if>>選択</button>
        <button type='button' id='uf10ClearButton' class='btn btn-small' <c:if test='${"UF10" eq shainCdRenkeiArea or not uf10Enable}'>style='display:none;'</c:if>>クリア</button>
        </c:if>
    </div>
</div>

<script style='text/javascript'>
<c:if test='${enableInput}'>
$(document).ready(function(){
	//ユニバーサルフィールド１選択ボタン押下時Function
	$("#uf1SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=uf1Cd]");
		dialogRetUniversalFieldName		= $("input[name=uf1Name]");
		commonUniversalSentaku($("[name=kamokuCd]").val(), 1);
	});
	
	//ユニバーサルフィールド１クリアボタン押下時Function
	$("#uf1ClearButton").click(function(e){
		$("input[name=uf1Cd]").val("");
		$("input[name=uf1Name]").val("");
	});
	
	//ユニバーサルフィールド１コードロストフォーカス時Function
	$("input[name=uf1Cd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#uf1SentakuButton").is(":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=uf1Cd]");
			dialogRetUniversalFieldName		= $("input[name=uf1Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 1, title);
		}
	});
	
	//ユニバーサルフィールド２選択ボタン押下時Function
	$("#uf2SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=uf2Cd]");
		dialogRetUniversalFieldName		= $("input[name=uf2Name]");
		commonUniversalSentaku($("[name=kamokuCd]").val(), 2);
	});
		
	//ユニバーサルフィールド２クリアボタン押下時Function
	$("#uf2ClearButton").click(function(e){
		$("input[name=uf2Cd]").val("");
		$("input[name=uf2Name]").val("");
	});
	
	//ユニバーサルフィールド２コードロストフォーカス時Function
	$("input[name=uf2Cd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#uf2SentakuButton").is(":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=uf2Cd]");
			dialogRetUniversalFieldName		= $("input[name=uf2Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 2, title);
		}
	});
	
	//ユニバーサルフィールド３選択ボタン押下時Function
	$("#uf3SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=uf3Cd]");
		dialogRetUniversalFieldName		= $("input[name=uf3Name]");
		commonUniversalSentaku($("[name=kamokuCd]").val(), 3);
	});
	
	//ユニバーサルフィールド３クリアボタン押下時Function
	$("#uf3ClearButton").click(function(e){
		$("input[name=uf3Cd]").val("");
		$("input[name=uf3Name]").val("");
	});
	
	//ユニバーサルフィールド３コードロストフォーカス時Function
	$("input[name=uf3Cd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#uf3SentakuButton").is(":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=uf3Cd]");
			dialogRetUniversalFieldName		= $("input[name=uf3Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 3, title);
		}
	});

	//ユニバーサルフィールド４選択ボタン押下時Function
	$("#uf4SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=uf4Cd]");
		dialogRetUniversalFieldName		= $("input[name=uf4Name]");
		commonUniversalSentaku($("[name=kamokuCd]").val(), 4);
	});

	//ユニバーサルフィールド４クリアボタン押下時Function
	$("#uf4ClearButton").click(function(e){
		$("input[name=uf4Cd]").val("");
		$("input[name=uf4Name]").val("");
	});
	
	//ユニバーサルフィールド４コードロストフォーカス時Function
	$("input[name=uf4Cd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#uf4SentakuButton").is(":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=uf4Cd]");
			dialogRetUniversalFieldName		= $("input[name=uf4Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 4, title);
		}
	});

	//ユニバーサルフィールド５選択ボタン押下時Function
	$("#uf5SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=uf5Cd]");
		dialogRetUniversalFieldName		= $("input[name=uf5Name]");
		commonUniversalSentaku($("[name=kamokuCd]").val(), 5);
	});

	//ユニバーサルフィールド５クリアボタン押下時Function
	$("#uf5ClearButton").click(function(e){
		$("input[name=uf5Cd]").val("");
		$("input[name=uf5Name]").val("");
	});
	
	//ユニバーサルフィールド５コードロストフォーカス時Function
	$("input[name=uf5Cd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#uf5SentakuButton").is(":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=uf5Cd]");
			dialogRetUniversalFieldName		= $("input[name=uf5Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 5, title);
		}
	});

//ユニバーサルフィールド６選択ボタン押下時Function
	$("#uf6SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=uf6Cd]");
		dialogRetUniversalFieldName		= $("input[name=uf6Name]");
		commonUniversalSentaku($("[name=kamokuCd]").val(), 6);
	});

	//ユニバーサルフィールド６クリアボタン押下時Function
	$("#uf6ClearButton").click(function(e){
		$("input[name=uf6Cd]").val("");
		$("input[name=uf6Name]").val("");
	});
	
	//ユニバーサルフィールド６コードロストフォーカス時Function
	$("input[name=uf6Cd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#uf6SentakuButton").is(":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=uf6Cd]");
			dialogRetUniversalFieldName		= $("input[name=uf6Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 6, title);
		}
	});

	//ユニバーサルフィールド７選択ボタン押下時Function
	$("#uf7SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=uf7Cd]");
		dialogRetUniversalFieldName		= $("input[name=uf7Name]");
		commonUniversalSentaku($("[name=kamokuCd]").val(), 7);
	});

	//ユニバーサルフィールド７クリアボタン押下時Function
	$("#uf7ClearButton").click(function(e){
		$("input[name=uf7Cd]").val("");
		$("input[name=uf7Name]").val("");
	});
	
	//ユニバーサルフィールド７コードロストフォーカス時Function
	$("input[name=uf7Cd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#uf7SentakuButton").is(":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=uf7Cd]");
			dialogRetUniversalFieldName		= $("input[name=uf7Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 7, title);
		}
	});
	
	//ユニバーサルフィールド８選択ボタン押下時Function
	$("#uf8SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=uf8Cd]");
		dialogRetUniversalFieldName		= $("input[name=uf8Name]");
		commonUniversalSentaku($("[name=kamokuCd]").val(), 8);
	});

	//ユニバーサルフィールド８クリアボタン押下時Function
	$("#uf8ClearButton").click(function(e){
		$("input[name=uf8Cd]").val("");
		$("input[name=uf8Name]").val("");
	});
	
	//ユニバーサルフィールド８コードロストフォーカス時Function
	$("input[name=uf8Cd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#uf8SentakuButton").is(":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=uf8Cd]");
			dialogRetUniversalFieldName		= $("input[name=uf8Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 8, title);
		}
	});

	//ユニバーサルフィールド９選択ボタン押下時Function
	$("#uf9SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=uf9Cd]");
		dialogRetUniversalFieldName		= $("input[name=uf9Name]");
		commonUniversalSentaku($("[name=kamokuCd]").val(), 9);
	});

	//ユニバーサルフィールド９クリアボタン押下時Function
	$("#uf9ClearButton").click(function(e){
		$("input[name=uf9Cd]").val("");
		$("input[name=uf9Name]").val("");
	});
	
	//ユニバーサルフィールド９コードロストフォーカス時Function
	$("input[name=uf9Cd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#uf9SentakuButton").is(":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=uf9Cd]");
			dialogRetUniversalFieldName		= $("input[name=uf9Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 9, title);
		}
	});

	//ユニバーサルフィールド１０選択ボタン押下時Function
	$("#uf10SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=uf10Cd]");
		dialogRetUniversalFieldName		= $("input[name=uf10Name]");
		commonUniversalSentaku($("[name=kamokuCd]").val(), 10);
	});

	//ユニバーサルフィールド１０クリアボタン押下時Function
	$("#uf10ClearButton").click(function(e){
		$("input[name=uf10Cd]").val("");
		$("input[name=uf10Name]").val("");
	});
	
	//ユニバーサルフィールド１０コードロストフォーカス時Function
	$("input[name=uf10Cd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#uf10SentakuButton").is(":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=uf10Cd]");
			dialogRetUniversalFieldName		= $("input[name=uf10Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 10, title);
		}
	});
	
});
</c:if>
</script>