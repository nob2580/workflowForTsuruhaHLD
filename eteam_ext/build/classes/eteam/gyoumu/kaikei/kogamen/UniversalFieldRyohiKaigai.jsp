<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- ユニバーサルフィールド(申請画面旅費・海外)用PartialView -->
			<!-- UF1 -->
			<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf1ShiyouFlg and ks.uf1.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.uf1.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf1Name)}</label>
				<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or "1" eq hfUfSeigyo.uf1ShiyouFlg}'>
					<input type='text' name="kaigaiUf1CdRyohi" maxlength='20' value='${su:htmlEscape(kaigaiUf1CdRyohi)}' <c:if test='${"UF1" eq shainCdRenkeiArea or not kaigaiUf1EnableRyohi}'>disabled</c:if>>
					<input type='hidden' name="kaigaiUf1NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf1ShiyouFlg or "3" eq hfUfSeigyo.uf1ShiyouFlg}'>
					<input type='text' name="kaigaiUf1CdRyohi" class='pc_only' <c:if test='${"UF1" eq shainCdRenkeiArea or not kaigaiUf1EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(kaigaiUf1CdRyohi)}'>
					<input type='text' name="kaigaiUf1NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(kaigaiUf1NameRyohi)}' <c:if test='${"UF1" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' id='kaigaiUf1SentakuButton' class='btn btn-small' <c:if test='${"UF1" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not kaigaiUf1EnableRyohi}'>disabled</c:if>>選択</button>
					<button type='button' id='kaigaiUf1ClearButton' class='btn btn-small' <c:if test='${"UF1" eq shainCdRenkeiArea or not kaigaiUf1EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
				</div>
			</div>
			<!-- UF2 -->
			<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf2ShiyouFlg and ks.uf2.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.uf2.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf2Name)}</label>
				<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or "1" eq hfUfSeigyo.uf2ShiyouFlg}'>
					<input type='text' name="kaigaiUf2CdRyohi" maxlength='20' value='${su:htmlEscape(kaigaiUf2CdRyohi)}' <c:if test='${"UF2" eq shainCdRenkeiArea or not kaigaiUf2EnableRyohi}'>disabled</c:if>>
					<input type='hidden' name="kaigaiUf2NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf2ShiyouFlg or "3" eq hfUfSeigyo.uf2ShiyouFlg}'>
					<input type='text' name="kaigaiUf2CdRyohi" class='pc_only' <c:if test='${"UF2" eq shainCdRenkeiArea or not kaigaiUf2EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(kaigaiUf2CdRyohi)}'>
					<input type='text' name="kaigaiUf2NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(kaigaiUf2NameRyohi)}' <c:if test='${"UF2" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' id='kaigaiUf2SentakuButton' class='btn btn-small' <c:if test='${"UF2" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not kaigaiUf2EnableRyohi}'>disabled</c:if>>選択</button>
					<button type='button' id='kaigaiUf2ClearButton' class='btn btn-small' <c:if test='${"UF2" eq shainCdRenkeiArea or not kaigaiUf2EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
				</div>
			</div>
			<!-- UF3 -->
			<div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf3ShiyouFlg and ks.uf3.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.uf3.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf3Name)}</label>
				<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or "1" eq hfUfSeigyo.uf3ShiyouFlg}'>
					<input type='text' name="kaigaiUf3CdRyohi" maxlength='20' value='${su:htmlEscape(kaigaiUf3CdRyohi)}' <c:if test='${"UF3" eq shainCdRenkeiArea or not kaigaiUf3EnableRyohi}'>disabled</c:if>>
					<input type='hidden' name="kaigaiUf3NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf3ShiyouFlg or "3" eq hfUfSeigyo.uf3ShiyouFlg}'>
					<input type='text' name="kaigaiUf3CdRyohi" class='pc_only' <c:if test='${"UF3" eq shainCdRenkeiArea or not kaigaiUf3EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(kaigaiUf3CdRyohi)}'>
					<input type='text' name="kaigaiUf3NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(kaigaiUf3NameRyohi)}' <c:if test='${"UF3" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' id='kaigaiUf3SentakuButton' class='btn btn-small' <c:if test='${"UF3" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not kaigaiUf3EnableRyohi}'>disabled</c:if>>選択</button>
					<button type='button' id='kaigaiUf3ClearButton' class='btn btn-small' <c:if test='${"UF3" eq shainCdRenkeiArea or not kaigaiUf3EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
				</div>
			</div>
<!-- UF4 -->
   <div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf4ShiyouFlg and ks.uf4.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'><c:if test='${ks.uf4.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf4Name)}</label>
    <div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or "1" eq hfUfSeigyo.uf4ShiyouFlg}'>
     <input type='text' name="kaigaiUf4CdRyohi" maxlength='20' value='${su:htmlEscape(kaigaiUf4CdRyohi)}' <c:if test='${"UF4" eq shainCdRenkeiArea or not kaigaiUf4EnableRyohi}'>disabled</c:if>>
     <input type='hidden' name="kaigaiUf4NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf4ShiyouFlg or "3" eq hfUfSeigyo.uf4ShiyouFlg}'>
     <input type='text' name="kaigaiUf4CdRyohi" class='pc_only' <c:if test='${"UF4" eq shainCdRenkeiArea or not kaigaiUf4EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(kaigaiUf4CdRyohi)}'>
     <input type='text' name="kaigaiUf4NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(kaigaiUf4NameRyohi)}' <c:if test='${"UF4" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
     <button type='button' id='kaigaiUf4SentakuButton' class='btn btn-small' <c:if test='${"UF4" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not kaigaiUf4EnableRyohi}'>disabled</c:if>>選択</button>
     <button type='button' id='kaigaiUf4ClearButton' class='btn btn-small' <c:if test='${"UF4" eq shainCdRenkeiArea or not kaigaiUf4EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
    </div>
   </div>
<!-- UF5 -->
   <div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf5ShiyouFlg and ks.uf5.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'><c:if test='${ks.uf5.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf5Name)}</label>
    <div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or "1" eq hfUfSeigyo.uf5ShiyouFlg}'>
     <input type='text' name="kaigaiUf5CdRyohi" maxlength='20' value='${su:htmlEscape(kaigaiUf5CdRyohi)}' <c:if test='${"UF5" eq shainCdRenkeiArea or not kaigaiUf5EnableRyohi}'>disabled</c:if>>
     <input type='hidden' name="kaigaiUf5NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf5ShiyouFlg or "3" eq hfUfSeigyo.uf5ShiyouFlg}'>
     <input type='text' name="kaigaiUf5CdRyohi" class='pc_only' <c:if test='${"UF5" eq shainCdRenkeiArea or not kaigaiUf5EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(kaigaiUf5CdRyohi)}'>
     <input type='text' name="kaigaiUf5NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(kaigaiUf5NameRyohi)}' <c:if test='${"UF5" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
     <button type='button' id='kaigaiUf5SentakuButton' class='btn btn-small' <c:if test='${"UF5" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not kaigaiUf5EnableRyohi}'>disabled</c:if>>選択</button>
     <button type='button' id='kaigaiUf5ClearButton' class='btn btn-small' <c:if test='${"UF5" eq shainCdRenkeiArea or not kaigaiUf5EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
    </div>
   </div>
<!-- UF6 -->
   <div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf6ShiyouFlg and ks.uf6.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'><c:if test='${ks.uf6.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf6Name)}</label>
    <div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or "1" eq hfUfSeigyo.uf6ShiyouFlg}'>
     <input type='text' name="kaigaiUf6CdRyohi" maxlength='20' value='${su:htmlEscape(kaigaiUf6CdRyohi)}' <c:if test='${"UF6" eq shainCdRenkeiArea or not kaigaiUf6EnableRyohi}'>disabled</c:if>>
     <input type='hidden' name="kaigaiUf6NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf6ShiyouFlg or "3" eq hfUfSeigyo.uf6ShiyouFlg}'>
     <input type='text' name="kaigaiUf6CdRyohi" class='pc_only' <c:if test='${"UF6" eq shainCdRenkeiArea or not kaigaiUf6EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(kaigaiUf6CdRyohi)}'>
     <input type='text' name="kaigaiUf6NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(kaigaiUf6NameRyohi)}' <c:if test='${"UF6" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
     <button type='button' id='kaigaiUf6SentakuButton' class='btn btn-small' <c:if test='${"UF6" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not kaigaiUf6EnableRyohi}'>disabled</c:if>>選択</button>
     <button type='button' id='kaigaiUf6ClearButton' class='btn btn-small' <c:if test='${"UF6" eq shainCdRenkeiArea or not kaigaiUf6EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
    </div>
   </div>
<!-- UF7 -->
   <div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf7ShiyouFlg and ks.uf7.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'><c:if test='${ks.uf7.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf7Name)}</label>
    <div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or "1" eq hfUfSeigyo.uf7ShiyouFlg}'>
     <input type='text' name="kaigaiUf7CdRyohi" maxlength='20' value='${su:htmlEscape(kaigaiUf7CdRyohi)}' <c:if test='${"UF7" eq shainCdRenkeiArea or not kaigaiUf7EnableRyohi}'>disabled</c:if>>
     <input type='hidden' name="kaigaiUf7NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf7ShiyouFlg or "3" eq hfUfSeigyo.uf7ShiyouFlg}'>
     <input type='text' name="kaigaiUf7CdRyohi" class='pc_only' <c:if test='${"UF7" eq shainCdRenkeiArea or not kaigaiUf7EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(kaigaiUf7CdRyohi)}'>
     <input type='text' name="kaigaiUf7NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(kaigaiUf7NameRyohi)}' <c:if test='${"UF7" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
     <button type='button' id='kaigaiUf7SentakuButton' class='btn btn-small' <c:if test='${"UF7" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not kaigaiUf7EnableRyohi}'>disabled</c:if>>選択</button>
     <button type='button' id='kaigaiUf7ClearButton' class='btn btn-small' <c:if test='${"UF7" eq shainCdRenkeiArea or not kaigaiUf7EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
    </div>
   </div>
<!-- UF8 -->
   <div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf8ShiyouFlg and ks.uf8.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'><c:if test='${ks.uf8.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf8Name)}</label>
    <div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or "1" eq hfUfSeigyo.uf8ShiyouFlg}'>
     <input type='text' name="kaigaiUf8CdRyohi" maxlength='20' value='${su:htmlEscape(kaigaiUf8CdRyohi)}' <c:if test='${"UF8" eq shainCdRenkeiArea or not kaigaiUf8EnableRyohi}'>disabled</c:if>>
     <input type='hidden' name="kaigaiUf8NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf8ShiyouFlg or "3" eq hfUfSeigyo.uf8ShiyouFlg}'>
     <input type='text' name="kaigaiUf8CdRyohi" class='pc_only' <c:if test='${"UF8" eq shainCdRenkeiArea or not kaigaiUf8EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(kaigaiUf8CdRyohi)}'>
     <input type='text' name="kaigaiUf8NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(kaigaiUf8NameRyohi)}' <c:if test='${"UF8" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
     <button type='button' id='kaigaiUf8SentakuButton' class='btn btn-small' <c:if test='${"UF8" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not kaigaiUf8EnableRyohi}'>disabled</c:if>>選択</button>
     <button type='button' id='kaigaiUf8ClearButton' class='btn btn-small' <c:if test='${"UF8" eq shainCdRenkeiArea or not kaigaiUf8EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
    </div>
   </div>
<!-- UF9 -->
   <div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf9ShiyouFlg and ks.uf9.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'><c:if test='${ks.uf9.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf9Name)}</label>
    <div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or "1" eq hfUfSeigyo.uf9ShiyouFlg}'>
     <input type='text' name="kaigaiUf9CdRyohi" maxlength='20' value='${su:htmlEscape(kaigaiUf9CdRyohi)}' <c:if test='${"UF9" eq shainCdRenkeiArea or not kaigaiUf9EnableRyohi}'>disabled</c:if>>
     <input type='hidden' name="kaigaiUf9NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf9ShiyouFlg or "3" eq hfUfSeigyo.uf9ShiyouFlg}'>
     <input type='text' name="kaigaiUf9CdRyohi" class='pc_only' <c:if test='${"UF9" eq shainCdRenkeiArea or not kaigaiUf9EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(kaigaiUf9CdRyohi)}'>
     <input type='text' name="kaigaiUf9NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(kaigaiUf9NameRyohi)}' <c:if test='${"UF9" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
     <button type='button' id='kaigaiUf9SentakuButton' class='btn btn-small' <c:if test='${"UF9" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not kaigaiUf9EnableRyohi}'>disabled</c:if>>選択</button>
     <button type='button' id='kaigaiUf9ClearButton' class='btn btn-small' <c:if test='${"UF9" eq shainCdRenkeiArea or not kaigaiUf9EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
    </div>
   </div>
<!-- UF10 -->
   <div class='control-group<c:if test='${not ("0" ne hfUfSeigyo.uf10ShiyouFlg and ks.uf10.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
    <label class='control-label'><c:if test='${ks.uf10.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf10Name)}</label>
    <div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or "1" eq hfUfSeigyo.uf10ShiyouFlg}'>
     <input type='text' name="kaigaiUf10CdRyohi" maxlength='20' value='${su:htmlEscape(kaigaiUf10CdRyohi)}' <c:if test='${"UF10" eq shainCdRenkeiArea or not kaigaiUf10EnableRyohi}'>disabled</c:if>>
     <input type='hidden' name="kaigaiUf10NameRyohi" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf10ShiyouFlg or "3" eq hfUfSeigyo.uf10ShiyouFlg}'>
     <input type='text' name="kaigaiUf10CdRyohi" class='pc_only' <c:if test='${"UF10" eq shainCdRenkeiArea or not kaigaiUf10EnableRyohi}'>disabled</c:if> value='${su:htmlEscape(kaigaiUf10CdRyohi)}'>
     <input type='text' name="kaigaiUf10NameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(kaigaiUf10NameRyohi)}' <c:if test='${"UF10" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
     <button type='button' id='kaigaiUf10SentakuButton' class='btn btn-small' <c:if test='${"UF10" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not kaigaiUf10EnableRyohi}'>disabled</c:if>>選択</button>
     <button type='button' id='kaigaiUf10ClearButton' class='btn btn-small' <c:if test='${"UF10" eq shainCdRenkeiArea or not kaigaiUf10EnableRyohi}'>style='display:none;'</c:if>>クリア</button>
</c:if>
    </div>
   </div>


<script style='text/javascript'>
<c:if test='${enableInput}'>
$(document).ready(function(){
	//海外ユニバーサルフィールド１選択ボタン押下時Function
	$("#kaigaiUf1SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=kaigaiUf1CdRyohi]");
		dialogRetUniversalFieldName		= $("input[name=kaigaiUf1NameRyohi]");
		commonUniversalSentaku($("[name=kaigaiKamokuCdRyohi]").val(), 1);
	});

	//海外ユニバーサルフィールド１クリアボタン押下時Function
	$("#kaigaiUf1ClearButton").click(function(e){
		$("input[name=kaigaiUf1CdRyohi]").val("");
		$("input[name=kaigaiUf1NameRyohi]").val("");
	});
	
	// 海外ユニバーサルフィールド１コードロストフォーカス時Function
	$("input[name=kaigaiUf1CdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#kaigaiUf1SentakuButton").is(":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=kaigaiUf1CdRyohi]");
			dialogRetUniversalFieldName		= $("input[name=kaigaiUf1NameRyohi]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 1, title);
		}
	});

	//海外ユニバーサルフィールド２選択ボタン押下時Function
	$("#kaigaiUf2SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=kaigaiUf2CdRyohi]");
		dialogRetUniversalFieldName		= $("input[name=kaigaiUf2NameRyohi]");
		commonUniversalSentaku($("[name=kaigaiKamokuCdRyohi]").val(), 2);
	});
		
	//海外ユニバーサルフィールド２クリアボタン押下時Function
	$("#kaigaiUf2ClearButton").click(function(e){
		$("input[name=kaigaiUf2CdRyohi]").val("");
		$("input[name=kaigaiUf2NameRyohi]").val("");
	});

	//海外ユニバーサルフィールド２コードロストフォーカス時Function
	$("input[name=kaigaiUf2CdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#kaigaiUf2SentakuButton").is(":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=kaigaiUf2CdRyohi]");
			dialogRetUniversalFieldName		= $("input[name=kaigaiUf2NameRyohi]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 2, title);
		}
	});
	
	//海外ユニバーサルフィールド３選択ボタン押下時Function
	$("#kaigaiUf3SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=kaigaiUf3CdRyohi]");
		dialogRetUniversalFieldName		= $("input[name=kaigaiUf3NameRyohi]");
		commonUniversalSentaku($("[name=kaigaiKamokuCdRyohi]").val(), 3);
	});

	//海外ユニバーサルフィールド３クリアボタン押下時Function
	$("#kaigaiUf3ClearButton").click(function(e){
		$("input[name=kaigaiUf3CdRyohi]").val("");
		$("input[name=kaigaiUf3NameRyohi]").val("");
	});
	
	// 海外ユニバーサルフィールド３コードロストフォーカス時Function
	$("input[name=kaigaiUf3CdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#kaigaiUf3SentakuButton").is(":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=kaigaiUf3CdRyohi]");
			dialogRetUniversalFieldName		= $("input[name=kaigaiUf3NameRyohi]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 3, title);
		}
	});
	
	//海外ユニバーサルフィールド４選択ボタン押下時Function
	$("#kaigaiUf4SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=kaigaiUf4CdRyohi]");
	    dialogRetUniversalFieldName = $("input[name=kaigaiUf4NameRyohi]");
	    commonUniversalSentaku($("[name=kaigaiKamokuCdRyohi]").val(), 4);
	});

	//海外ユニバーサルフィールド４クリアボタン押下時Function
	$("#kaigaiUf4ClearButton").click(function (e) {
	    $("input[name=kaigaiUf4CdRyohi]").val("");
	    $("input[name=kaigaiUf4NameRyohi]").val("");
	});

	// 海外ユニバーサルフィールド４コードロストフォーカス時Function
	$("input[name=kaigaiUf4CdRyohi]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#kaigaiUf4SentakuButton").is(":visible");
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=kaigaiUf4CdRyohi]");
	        dialogRetUniversalFieldName = $("input[name=kaigaiUf4NameRyohi]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 4, title);
	    }
	});
	//海外ユニバーサルフィールド５選択ボタン押下時Function
	$("#kaigaiUf5SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=kaigaiUf5CdRyohi]");
	    dialogRetUniversalFieldName = $("input[name=kaigaiUf5NameRyohi]");
	    commonUniversalSentaku($("[name=kaigaiKamokuCdRyohi]").val(), 5);
	});

	//海外ユニバーサルフィールド５クリアボタン押下時Function
	$("#kaigaiUf5ClearButton").click(function (e) {
	    $("input[name=kaigaiUf5CdRyohi]").val("");
	    $("input[name=kaigaiUf5NameRyohi]").val("");
	});

	// 海外ユニバーサルフィールド５コードロストフォーカス時Function
	$("input[name=kaigaiUf5CdRyohi]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#kaigaiUf5SentakuButton").is(":visible");
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=kaigaiUf5CdRyohi]");
	        dialogRetUniversalFieldName = $("input[name=kaigaiUf5NameRyohi]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 5, title);
	    }
	});
	//海外ユニバーサルフィールド６選択ボタン押下時Function
	$("#kaigaiUf6SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=kaigaiUf6CdRyohi]");
	    dialogRetUniversalFieldName = $("input[name=kaigaiUf6NameRyohi]");
	    commonUniversalSentaku($("[name=kaigaiKamokuCdRyohi]").val(), 6);
	});

	//海外ユニバーサルフィールド６クリアボタン押下時Function
	$("#kaigaiUf6ClearButton").click(function (e) {
	    $("input[name=kaigaiUf6CdRyohi]").val("");
	    $("input[name=kaigaiUf6NameRyohi]").val("");
	});

	// 海外ユニバーサルフィールド６コードロストフォーカス時Function
	$("input[name=kaigaiUf6CdRyohi]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#kaigaiUf6SentakuButton").is(":visible");
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=kaigaiUf6CdRyohi]");
	        dialogRetUniversalFieldName = $("input[name=kaigaiUf6NameRyohi]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 6, title);
	    }
	});
	//海外ユニバーサルフィールド７選択ボタン押下時Function
	$("#kaigaiUf7SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=kaigaiUf7CdRyohi]");
	    dialogRetUniversalFieldName = $("input[name=kaigaiUf7NameRyohi]");
	    commonUniversalSentaku($("[name=kaigaiKamokuCdRyohi]").val(), 7);
	});

	//海外ユニバーサルフィールド７クリアボタン押下時Function
	$("#kaigaiUf7ClearButton").click(function (e) {
	    $("input[name=kaigaiUf7CdRyohi]").val("");
	    $("input[name=kaigaiUf7NameRyohi]").val("");
	});

	// 海外ユニバーサルフィールド７コードロストフォーカス時Function
	$("input[name=kaigaiUf7CdRyohi]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#kaigaiUf7SentakuButton").is(":visible");
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=kaigaiUf7CdRyohi]");
	        dialogRetUniversalFieldName = $("input[name=kaigaiUf7NameRyohi]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 7, title);
	    }
	});
	//海外ユニバーサルフィールド８選択ボタン押下時Function
	$("#kaigaiUf8SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=kaigaiUf8CdRyohi]");
	    dialogRetUniversalFieldName = $("input[name=kaigaiUf8NameRyohi]");
	    commonUniversalSentaku($("[name=kaigaiKamokuCdRyohi]").val(), 8);
	});

	//海外ユニバーサルフィールド８クリアボタン押下時Function
	$("#kaigaiUf8ClearButton").click(function (e) {
	    $("input[name=kaigaiUf8CdRyohi]").val("");
	    $("input[name=kaigaiUf8NameRyohi]").val("");
	});

	// 海外ユニバーサルフィールド８コードロストフォーカス時Function
	$("input[name=kaigaiUf8CdRyohi]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#kaigaiUf8SentakuButton").is(":visible");
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=kaigaiUf8CdRyohi]");
	        dialogRetUniversalFieldName = $("input[name=kaigaiUf8NameRyohi]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 8, title);
	    }
	});
	//海外ユニバーサルフィールド９選択ボタン押下時Function
	$("#kaigaiUf9SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=kaigaiUf9CdRyohi]");
	    dialogRetUniversalFieldName = $("input[name=kaigaiUf9NameRyohi]");
	    commonUniversalSentaku($("[name=kaigaiKamokuCdRyohi]").val(), 9);
	});

	//海外ユニバーサルフィールド９クリアボタン押下時Function
	$("#kaigaiUf9ClearButton").click(function (e) {
	    $("input[name=kaigaiUf9CdRyohi]").val("");
	    $("input[name=kaigaiUf9NameRyohi]").val("");
	});

	// 海外ユニバーサルフィールド９コードロストフォーカス時Function
	$("input[name=kaigaiUf9CdRyohi]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#kaigaiUf9SentakuButton").is(":visible");
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=kaigaiUf9CdRyohi]");
	        dialogRetUniversalFieldName = $("input[name=kaigaiUf9NameRyohi]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 9, title);
	    }
	});
	//海外ユニバーサルフィールド１０選択ボタン押下時Function
	$("#kaigaiUf10SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=kaigaiUf10CdRyohi]");
	    dialogRetUniversalFieldName = $("input[name=kaigaiUf10NameRyohi]");
	    commonUniversalSentaku($("[name=kaigaiKamokuCdRyohi]").val(), 10);
	});

	//海外ユニバーサルフィールド１０クリアボタン押下時Function
	$("#kaigaiUf10ClearButton").click(function (e) {
	    $("input[name=kaigaiUf10CdRyohi]").val("");
	    $("input[name=kaigaiUf10NameRyohi]").val("");
	});

	// 海外ユニバーサルフィールド１０コードロストフォーカス時Function
	$("input[name=kaigaiUf10CdRyohi]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#kaigaiUf10SentakuButton").is(":visible");
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=kaigaiUf10CdRyohi]");
	        dialogRetUniversalFieldName = $("input[name=kaigaiUf10NameRyohi]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 10, title);
	    }
	});

});
</c:if>
</script>