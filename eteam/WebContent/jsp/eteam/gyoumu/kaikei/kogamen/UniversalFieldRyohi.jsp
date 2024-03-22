<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- ユニバーサルフィールド(申請画面旅費)用PartialView -->
			<!-- UF1 -->
			<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf1ShiyouFlg and ks.uf1.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.uf1.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf1Name)}</label>
				<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or "1" eq hfUfSeigyo.uf1ShiyouFlg}'>
					<input type='text' name="uf1CdRyohi" maxlength='20' value='${su:htmlEscape(uf1CdRyohi)}' <c:if test='${"UF1" eq shainCdRenkeiArea or not uf1EnableRyohi}'>disabled</c:if>>
					<input type='hidden' name="uf1NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf1ShiyouFlg or "3" eq hfUfSeigyo.uf1ShiyouFlg}'>
					<input type='text' name="uf1CdRyohi" class='pc_only' <c:if test='${"UF1" eq shainCdRenkeiArea or not uf1EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(uf1CdRyohi)}'>
					<input type='text' name="uf1NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(uf1NameRyohi)}' <c:if test='${"UF1" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' id='uf1SentakuButton' class='btn btn-small' <c:if test='${"UF1" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf1EnableRyohi}'>disabled</c:if>>選択</button>
					<button type='button' id='uf1ClearButton' class='btn btn-small' <c:if test='${"UF1" eq shainCdRenkeiArea or not uf1EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
				</div>
			</div>
			<!-- UF2 -->
			<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf2ShiyouFlg and ks.uf2.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.uf2.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf2Name)}</label>
				<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or "1" eq hfUfSeigyo.uf2ShiyouFlg}'>
					<input type='text' name="uf2CdRyohi" maxlength='20' value='${su:htmlEscape(uf2CdRyohi)}' <c:if test='${"UF2" eq shainCdRenkeiArea or not uf2EnableRyohi}'>disabled</c:if>>
					<input type='hidden' name="uf2NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf2ShiyouFlg or "3" eq hfUfSeigyo.uf2ShiyouFlg}'>
					<input type='text' name="uf2CdRyohi" class='pc_only' <c:if test='${"UF2" eq shainCdRenkeiArea or not uf2EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(uf2CdRyohi)}'>
					<input type='text' name="uf2NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(uf2NameRyohi)}' <c:if test='${"UF2" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' id='uf2SentakuButton' class='btn btn-small' <c:if test='${"UF2" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf2EnableRyohi}'>disabled</c:if>>選択</button>
					<button type='button' id='uf2ClearButton' class='btn btn-small' <c:if test='${"UF2" eq shainCdRenkeiArea or not uf2EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
				</div>
			</div>
			<!-- UF3 -->
			<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf3ShiyouFlg and ks.uf3.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.uf3.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf3Name)}</label>
				<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or "1" eq hfUfSeigyo.uf3ShiyouFlg}'>
					<input type='text' name="uf3CdRyohi" maxlength='20' value='${su:htmlEscape(uf3CdRyohi)}' <c:if test='${"UF3" eq shainCdRenkeiArea or not uf3EnableRyohi}'>disabled</c:if>>
					<input type='hidden' name="uf3NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf3ShiyouFlg or "3" eq hfUfSeigyo.uf3ShiyouFlg}'>
					<input type='text' name="uf3CdRyohi" class='pc_only' <c:if test='${"UF3" eq shainCdRenkeiArea or not uf3EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(uf3CdRyohi)}'>
					<input type='text' name="uf3NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(uf3NameRyohi)}' <c:if test='${"UF3" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' id='uf3SentakuButton' class='btn btn-small' <c:if test='${"UF3" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf3EnableRyohi}'>disabled</c:if>>選択</button>
					<button type='button' id='uf3ClearButton' class='btn btn-small' <c:if test='${"UF3" eq shainCdRenkeiArea or not uf3EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
				</div>
			</div>
<!-- UF4 -->
   <div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf4ShiyouFlg and ks.uf4.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'><c:if test='${ks.uf4.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf4Name)}</label>
    <div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or "1" eq hfUfSeigyo.uf4ShiyouFlg}'>
     <input type='text' name="uf4CdRyohi" maxlength='20' value='${su:htmlEscape(uf4CdRyohi)}' <c:if test='${"UF4" eq shainCdRenkeiArea or not uf4EnableRyohi}'>disabled</c:if>>
     <input type='hidden' name="uf4NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf4ShiyouFlg or "3" eq hfUfSeigyo.uf4ShiyouFlg}'>
     <input type='text' name="uf4CdRyohi" class='pc_only' <c:if test='${"UF4" eq shainCdRenkeiArea or not uf4EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(uf4CdRyohi)}'>
     <input type='text' name="uf4NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(uf4NameRyohi)}' <c:if test='${"UF4" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
     <button type='button' id='uf4SentakuButton' class='btn btn-small' <c:if test='${"UF4" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf4EnableRyohi}'>disabled</c:if>>選択</button>
     <button type='button' id='uf4ClearButton' class='btn btn-small' <c:if test='${"UF4" eq shainCdRenkeiArea or not uf4EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
    </div>
   </div>
<!-- UF5 -->
   <div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf5ShiyouFlg and ks.uf5.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'><c:if test='${ks.uf5.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf5Name)}</label>
    <div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or "1" eq hfUfSeigyo.uf5ShiyouFlg}'>
     <input type='text' name="uf5CdRyohi" maxlength='20' value='${su:htmlEscape(uf5CdRyohi)}' <c:if test='${"UF5" eq shainCdRenkeiArea or not uf5EnableRyohi}'>disabled</c:if>>
     <input type='hidden' name="uf5NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf5ShiyouFlg or "3" eq hfUfSeigyo.uf5ShiyouFlg}'>
     <input type='text' name="uf5CdRyohi" class='pc_only' <c:if test='${"UF5" eq shainCdRenkeiArea or not uf5EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(uf5CdRyohi)}'>
     <input type='text' name="uf5NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(uf5NameRyohi)}' <c:if test='${"UF5" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
     <button type='button' id='uf5SentakuButton' class='btn btn-small' <c:if test='${"UF5" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf5EnableRyohi}'>disabled</c:if>>選択</button>
     <button type='button' id='uf5ClearButton' class='btn btn-small' <c:if test='${"UF5" eq shainCdRenkeiArea or not uf5EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
    </div>
   </div>
<!-- UF6 -->
   <div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf6ShiyouFlg and ks.uf6.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'><c:if test='${ks.uf6.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf6Name)}</label>
    <div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or "1" eq hfUfSeigyo.uf6ShiyouFlg}'>
     <input type='text' name="uf6CdRyohi" maxlength='20' value='${su:htmlEscape(uf6CdRyohi)}' <c:if test='${"UF6" eq shainCdRenkeiArea or not uf6EnableRyohi}'>disabled</c:if>>
     <input type='hidden' name="uf6NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf6ShiyouFlg or "3" eq hfUfSeigyo.uf6ShiyouFlg}'>
     <input type='text' name="uf6CdRyohi" class='pc_only' <c:if test='${"UF6" eq shainCdRenkeiArea or not uf6EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(uf6CdRyohi)}'>
     <input type='text' name="uf6NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(uf6NameRyohi)}' <c:if test='${"UF6" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
     <button type='button' id='uf6SentakuButton' class='btn btn-small' <c:if test='${"UF6" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf6EnableRyohi}'>disabled</c:if>>選択</button>
     <button type='button' id='uf6ClearButton' class='btn btn-small' <c:if test='${"UF6" eq shainCdRenkeiArea or not uf6EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
    </div>
   </div>
<!-- UF7 -->
   <div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf7ShiyouFlg and ks.uf7.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'><c:if test='${ks.uf7.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf7Name)}</label>
    <div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or "1" eq hfUfSeigyo.uf7ShiyouFlg}'>
     <input type='text' name="uf7CdRyohi" maxlength='20' value='${su:htmlEscape(uf7CdRyohi)}' <c:if test='${"UF7" eq shainCdRenkeiArea or not uf7EnableRyohi}'>disabled</c:if>>
     <input type='hidden' name="uf7NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf7ShiyouFlg or "3" eq hfUfSeigyo.uf7ShiyouFlg}'>
     <input type='text' name="uf7CdRyohi" class='pc_only' <c:if test='${"UF7" eq shainCdRenkeiArea or not uf7EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(uf7CdRyohi)}'>
     <input type='text' name="uf7NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(uf7NameRyohi)}' <c:if test='${"UF7" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
     <button type='button' id='uf7SentakuButton' class='btn btn-small' <c:if test='${"UF7" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf7EnableRyohi}'>disabled</c:if>>選択</button>
     <button type='button' id='uf7ClearButton' class='btn btn-small' <c:if test='${"UF7" eq shainCdRenkeiArea or not uf7EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
    </div>
   </div>
<!-- UF8 -->
   <div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf8ShiyouFlg and ks.uf8.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'><c:if test='${ks.uf8.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf8Name)}</label>
    <div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or "1" eq hfUfSeigyo.uf8ShiyouFlg}'>
     <input type='text' name="uf8CdRyohi" maxlength='20' value='${su:htmlEscape(uf8CdRyohi)}' <c:if test='${"UF8" eq shainCdRenkeiArea or not uf8EnableRyohi}'>disabled</c:if>>
     <input type='hidden' name="uf8NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf8ShiyouFlg or "3" eq hfUfSeigyo.uf8ShiyouFlg}'>
     <input type='text' name="uf8CdRyohi" class='pc_only' <c:if test='${"UF8" eq shainCdRenkeiArea or not uf8EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(uf8CdRyohi)}'>
     <input type='text' name="uf8NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(uf8NameRyohi)}' <c:if test='${"UF8" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
     <button type='button' id='uf8SentakuButton' class='btn btn-small' <c:if test='${"UF8" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf8EnableRyohi}'>disabled</c:if>>選択</button>
     <button type='button' id='uf8ClearButton' class='btn btn-small' <c:if test='${"UF8" eq shainCdRenkeiArea or not uf8EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
    </div>
   </div>
<!-- UF9 -->
   <div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf9ShiyouFlg and ks.uf9.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'><c:if test='${ks.uf9.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf9Name)}</label>
    <div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or "1" eq hfUfSeigyo.uf9ShiyouFlg}'>
     <input type='text' name="uf9CdRyohi" maxlength='20' value='${su:htmlEscape(uf9CdRyohi)}' <c:if test='${"UF9" eq shainCdRenkeiArea or not uf9EnableRyohi}'>disabled</c:if>>
     <input type='hidden' name="uf9NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf9ShiyouFlg or "3" eq hfUfSeigyo.uf9ShiyouFlg}'>
     <input type='text' name="uf9CdRyohi" class='pc_only' <c:if test='${"UF9" eq shainCdRenkeiArea or not uf9EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(uf9CdRyohi)}'>
     <input type='text' name="uf9NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(uf9NameRyohi)}' <c:if test='${"UF9" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
     <button type='button' id='uf9SentakuButton' class='btn btn-small' <c:if test='${"UF9" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf9EnableRyohi}'>disabled</c:if>>選択</button>
     <button type='button' id='uf9ClearButton' class='btn btn-small' <c:if test='${"UF9" eq shainCdRenkeiArea or not uf9EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
    </div>
   </div>
<!-- UF10 -->
   <div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf10ShiyouFlg and ks.uf10.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'><c:if test='${ks.uf10.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf10Name)}</label>
    <div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or "1" eq hfUfSeigyo.uf10ShiyouFlg}'>
     <input type='text' name="uf10CdRyohi" maxlength='20' value='${su:htmlEscape(uf10CdRyohi)}' <c:if test='${"UF10" eq shainCdRenkeiArea or not uf10EnableRyohi}'>disabled</c:if>>
     <input type='hidden' name="uf10NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf10ShiyouFlg or "3" eq hfUfSeigyo.uf10ShiyouFlg}'>
     <input type='text' name="uf10CdRyohi" class='pc_only' <c:if test='${"UF10" eq shainCdRenkeiArea or not uf10EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(uf10CdRyohi)}'>
     <input type='text' name="uf10NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(uf10NameRyohi)}' <c:if test='${"UF10" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
     <button type='button' id='uf10SentakuButton' class='btn btn-small' <c:if test='${"UF10" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf10EnableRyohi}'>disabled</c:if>>選択</button>
     <button type='button' id='uf10ClearButton' class='btn btn-small' <c:if test='${"UF10" eq shainCdRenkeiArea or not uf10EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
    </div>
   </div>


<script style='text/javascript'>
<c:if test='${enableInput}'>
$(document).ready(function(){
	//ユニバーサルフィールド１選択ボタン押下時Function
	$("#uf1SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=uf1CdRyohi]");
		dialogRetUniversalFieldName		= $("input[name=uf1NameRyohi]");
		commonUniversalSentaku($("[name=kamokuCdRyohi]").val(), 1);
	});

	//ユニバーサルフィールド１クリアボタン押下時Function
	$("#uf1ClearButton").click(function(e){
		$("input[name=uf1CdRyohi]").val("");
		$("input[name=uf1NameRyohi]").val("");
	});
	
	// ユニバーサルフィールド１コードロストフォーカス時Function
	$("input[name=uf1CdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#uf1SentakuButton").is(":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=uf1CdRyohi]");
			dialogRetUniversalFieldName		= $("input[name=uf1NameRyohi]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 1, title);
		}
	});

	//ユニバーサルフィールド２選択ボタン押下時Function
	$("#uf2SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=uf2CdRyohi]");
		dialogRetUniversalFieldName		= $("input[name=uf2NameRyohi]");
		commonUniversalSentaku($("[name=kamokuCdRyohi]").val(), 2);
	});
		
	//ユニバーサルフィールド２クリアボタン押下時Function
	$("#uf2ClearButton").click(function(e){
		$("input[name=uf2CdRyohi]").val("");
		$("input[name=uf2NameRyohi]").val("");
	});

	//ユニバーサルフィールド２コードロストフォーカス時Function
	$("input[name=uf2CdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#uf2SentakuButton").is(":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=uf2CdRyohi]");
			dialogRetUniversalFieldName		= $("input[name=uf2NameRyohi]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 2, title);
		}
	});
	
	//ユニバーサルフィールド３選択ボタン押下時Function
	$("#uf3SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=uf3CdRyohi]");
		dialogRetUniversalFieldName		= $("input[name=uf3NameRyohi]");
		commonUniversalSentaku($("[name=kamokuCdRyohi]").val(), 3);
	});

	//ユニバーサルフィールド３クリアボタン押下時Function
	$("#uf3ClearButton").click(function(e){
		$("input[name=uf3CdRyohi]").val("");
		$("input[name=uf3NameRyohi]").val("");
	});
	
	// ユニバーサルフィールド３コードロストフォーカス時Function
	$("input[name=uf3CdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#uf3SentakuButton").is(":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=uf3CdRyohi]");
			dialogRetUniversalFieldName		= $("input[name=uf3NameRyohi]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 3, title);
		}
	});

	//ユニバーサルフィールド４選択ボタン押下時Function
	$("#uf4SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=uf4CdRyohi]");
	    dialogRetUniversalFieldName = $("input[name=uf4NameRyohi]");
	    commonUniversalSentaku($("[name=kamokuCdRyohi]").val(), 4);
	});

	//ユニバーサルフィールド４クリアボタン押下時Function
	$("#uf4ClearButton").click(function (e) {
	    $("input[name=uf4CdRyohi]").val("");
	    $("input[name=uf4NameRyohi]").val("");
	});

	// ユニバーサルフィールド４コードロストフォーカス時Function
	$("input[name=uf4CdRyohi]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#uf4SentakuButton").is(":visible");
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=uf4CdRyohi]");
	        dialogRetUniversalFieldName = $("input[name=uf4NameRyohi]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 4, title);
	    }
	});
	//ユニバーサルフィールド５選択ボタン押下時Function
	$("#uf5SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=uf5CdRyohi]");
	    dialogRetUniversalFieldName = $("input[name=uf5NameRyohi]");
	    commonUniversalSentaku($("[name=kamokuCdRyohi]").val(), 5);
	});

	//ユニバーサルフィールド５クリアボタン押下時Function
	$("#uf5ClearButton").click(function (e) {
	    $("input[name=uf5CdRyohi]").val("");
	    $("input[name=uf5NameRyohi]").val("");
	});

	// ユニバーサルフィールド５コードロストフォーカス時Function
	$("input[name=uf5CdRyohi]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#uf5SentakuButton").is(":visible");
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=uf5CdRyohi]");
	        dialogRetUniversalFieldName = $("input[name=uf5NameRyohi]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 5, title);
	    }
	});
	//ユニバーサルフィールド６選択ボタン押下時Function
	$("#uf6SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=uf6CdRyohi]");
	    dialogRetUniversalFieldName = $("input[name=uf6NameRyohi]");
	    commonUniversalSentaku($("[name=kamokuCdRyohi]").val(), 6);
	});

	//ユニバーサルフィールド６クリアボタン押下時Function
	$("#uf6ClearButton").click(function (e) {
	    $("input[name=uf6CdRyohi]").val("");
	    $("input[name=uf6NameRyohi]").val("");
	});

	// ユニバーサルフィールド６コードロストフォーカス時Function
	$("input[name=uf6CdRyohi]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#uf6SentakuButton").is(":visible");
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=uf6CdRyohi]");
	        dialogRetUniversalFieldName = $("input[name=uf6NameRyohi]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 6, title);
	    }
	});
	//ユニバーサルフィールド７選択ボタン押下時Function
	$("#uf7SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=uf7CdRyohi]");
	    dialogRetUniversalFieldName = $("input[name=uf7NameRyohi]");
	    commonUniversalSentaku($("[name=kamokuCdRyohi]").val(), 7);
	});

	//ユニバーサルフィールド７クリアボタン押下時Function
	$("#uf7ClearButton").click(function (e) {
	    $("input[name=uf7CdRyohi]").val("");
	    $("input[name=uf7NameRyohi]").val("");
	});

	// ユニバーサルフィールド７コードロストフォーカス時Function
	$("input[name=uf7CdRyohi]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#uf7SentakuButton").is(":visible");
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=uf7CdRyohi]");
	        dialogRetUniversalFieldName = $("input[name=uf7NameRyohi]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 7, title);
	    }
	});
	//ユニバーサルフィールド８選択ボタン押下時Function
	$("#uf8SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=uf8CdRyohi]");
	    dialogRetUniversalFieldName = $("input[name=uf8NameRyohi]");
	    commonUniversalSentaku($("[name=kamokuCdRyohi]").val(), 8);
	});

	//ユニバーサルフィールド８クリアボタン押下時Function
	$("#uf8ClearButton").click(function (e) {
	    $("input[name=uf8CdRyohi]").val("");
	    $("input[name=uf8NameRyohi]").val("");
	});

	// ユニバーサルフィールド８コードロストフォーカス時Function
	$("input[name=uf8CdRyohi]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#uf8SentakuButton").is(":visible");
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=uf8CdRyohi]");
	        dialogRetUniversalFieldName = $("input[name=uf8NameRyohi]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 8, title);
	    }
	});
	//ユニバーサルフィールド９選択ボタン押下時Function
	$("#uf9SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=uf9CdRyohi]");
	    dialogRetUniversalFieldName = $("input[name=uf9NameRyohi]");
	    commonUniversalSentaku($("[name=kamokuCdRyohi]").val(), 9);
	});

	//ユニバーサルフィールド９クリアボタン押下時Function
	$("#uf9ClearButton").click(function (e) {
	    $("input[name=uf9CdRyohi]").val("");
	    $("input[name=uf9NameRyohi]").val("");
	});

	// ユニバーサルフィールド９コードロストフォーカス時Function
	$("input[name=uf9CdRyohi]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#uf9SentakuButton").is(":visible");
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=uf9CdRyohi]");
	        dialogRetUniversalFieldName = $("input[name=uf9NameRyohi]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 9, title);
	    }
	});
	//ユニバーサルフィールド１０選択ボタン押下時Function
	$("#uf10SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=uf10CdRyohi]");
	    dialogRetUniversalFieldName = $("input[name=uf10NameRyohi]");
	    commonUniversalSentaku($("[name=kamokuCdRyohi]").val(), 10);
	});

	//ユニバーサルフィールド１０クリアボタン押下時Function
	$("#uf10ClearButton").click(function (e) {
	    $("input[name=uf10CdRyohi]").val("");
	    $("input[name=uf10NameRyohi]").val("");
	});

	// ユニバーサルフィールド１０コードロストフォーカス時Function
	$("input[name=uf10CdRyohi]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#uf10SentakuButton").is(":visible");
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=uf10CdRyohi]");
	        dialogRetUniversalFieldName = $("input[name=uf10NameRyohi]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 10, title);
	    }
	});

});
</c:if>
</script>