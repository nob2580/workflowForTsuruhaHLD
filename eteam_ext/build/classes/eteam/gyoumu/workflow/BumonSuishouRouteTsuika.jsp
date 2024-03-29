<%@ page import="eteam.symbol.EteamSymbol"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!DOCTYPE html>
<html lang='ja'>
	<head>
		<meta charset='utf-8'>
		<title>部門推奨ルート追加｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
<!--
.add-left-margin {
	margin-left: 4px;
}

-->
		</style>
	</head>
	<body>
    	<div id='wrap'>
		
			<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>
			
			<!-- 中身 -->
			<div id='content' class='container'>
			
				<!-- タイトル -->
				<h1>部門推奨ルート追加</h1>
				
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'>
					<div><form id='myForm' method='post' action='bumon_suishou_route_tsuika_touroku' enctype='multipart/form-data' class='form-horizontal'>
						<input type="hidden" name='defaultFlg' value='${su:htmlEscape(defaultFlg)}'>
						<div class='control-group'>
							<h2>制御条件</h2>
							<label class='control-label'><span class='required'>*</span>伝票</label>
							<div class='controls'>
								${denpyouPull}
							</div>
						</div>
						<div id="default" class='control-group'>
							<label class='control-label'>デフォルト設定</label>
							<div class='controls'>
								<input type="checkbox" id='defaultFlgHyouji' name='defaultFlgHyouji' value='1' <c:if test='${"1" eq defaultFlgHyouji}'>checked</c:if>>
							</div>
						</div>
						<div id="torihiki" class='control-group'>
							<label class='control-label'>取引</label>
							<div id="torihikiList" class='controls'>
<c:if test='${"0" eq isError}'>
								<div>
									<input type="text" name='shiwakeEdaNo' class='input-small'>
									<input type='text' name='torihikiName' class='input-xlarge' disabled>
									<button type='button' name='torihikiSentakuButton' class='btn btn-small'>選択</button>
									<button type='button' name='torihikiAdd' class='btn btn-mini'>+</button>
									<button type='button' name='torihikiDel' class='btn btn-mini'>-</button>
								</div>
</c:if>
<c:if test='${"1" eq isError}'>
<c:forEach var="i" begin="0" end="${fn:length(shiwakeEdaNo) - 1}" step="1">
								<div>
									<input type="text" name='shiwakeEdaNo'  value='${su:htmlEscape(shiwakeEdaNo[i])}' class='input-small'>
									<input type='text' name='torihikiName' class='input-xlarge' disabled value='${su:htmlEscape(torihikiName[i])}'>
									<button type='button' name='torihikiSentakuButton' class='btn btn-small'>選択</button>
									<button type='button' name='torihikiAdd' class='btn btn-mini'>+</button>
									<button type='button' name='torihikiDel' class='btn btn-mini'>-</button>
								</div>
</c:forEach>
</c:if>
							</div>
						</div>
						<div class='control-group'>
							<label class='control-label'><span class='required'>*</span>起票部門</label>
							<div id="bumonSentakuList" class='controls'>
								<input type='text' name='bumon' class='input-inline' readonly="readonly" value='${su:htmlEscape(bumon)}'>
								<nobr><button type='button' class='btn btn-small bumonSearch input-inline'>部門選択</button></nobr>
								<input type="hidden" name='bumonCd' value='${su:htmlEscape(bumonCd)}'>
							</div>
						</div>
						<div class='control-group'>
							<label class='control-label'>金額</label>
							<div id="kingakuList" class='controls'>
								<div>
									<input type='text' name='kingakuFrom' class='input-medium autoNumeric' maxLength='15' value='${su:htmlEscape(kingakuFrom)}'>
									<span>円 ～ </span>
									<input type='text' name='kingakuTo' class='input-medium autoNumeric' maxLength='15' value='${su:htmlEscape(kingakuTo)}'>
									<span>円</span>
								</div>
							</div>
						</div>
						<h2>承認者</h2>
						<div class='control-group'>
							<label class='control-label'><span class='required'>*</span>承認者</label>
							<div id="bumonRoleList" class='controls'>
<c:if test='${"0" eq isError}'>
								<div>
									<input type='text' name='bumonRole' class='input-inline' style='width:145px;' readonly="readonly">
									<select name='shoriKengen' class='input-medium input-inline'>
										<c:forEach var='shori' items='${shoriKengenList}'>
											<option value='${su:htmlEscape(shori.shounin_shori_kengen_no)}'>${su:htmlEscape(shori.shounin_shori_kengen_name)}</option>
										</c:forEach>
									</select>
									<input type='text' name='gougiName' class='input-inline' style='width:300px;display:none' readonly="readonly">
									<span class='required' id ='gougiTop' style='display:none'><b>合議</b></span>
									<span id ='gougiSpan'>　　</span>
									<button type='button' name='shouninshaAdd' class='btn btn-mini' style='display:none'>+</button>
									<button type='button' name='shouninshaDel' class='btn btn-mini'>-</button>
									<button type='button' name='bumonRoleSearch' class='btn btn-small input-inline'>役割選択</button>
									<button type='button' name='gougiSearch' class='btn btn-small input-inline' style='display:none'>合議部署選択</button>
									<button type='button' name='gougiAdd' class='btn btn-small input-inline'>合議挿入</button>
									<button type='button' name='bumonRoleAdd' class='btn btn-small input-inline'>役割挿入</button>
									<input type="hidden" name='bumonRoleCd'>
									<input type="hidden" name='gougiCd'>
									<input type="hidden" name='gougiEdano'>
									<input type="hidden" name='bumonRoleHyouji' value="true">
								</div>
</c:if>
<c:if test='${"1" eq isError}'>
<c:forEach var="record" items="${bumonRoleList}" varStatus="status">
								<div <c:if test="${status.index > 0}"> class='blank' </c:if>>
									<input type='text' name='bumonRole' class='input-inline' readonly="readonly" style='<c:if test='${!record.bumonRoleHyouji}'>display:none;</c:if>width:145px;' value='${su:htmlEscape(record.bumonRole)}'>
									<select name='shoriKengen' class='input-medium input-inline' style='<c:if test='${!record.bumonRoleHyouji}'>display:none;</c:if>'>
										<c:forEach var='shori' items='${shoriKengenList}'>
											<option value='${su:htmlEscape(shori.shounin_shori_kengen_no)}' <c:if test='${shori.shounin_shori_kengen_no eq record.shoriKengen}'>selected</c:if>>${su:htmlEscape(shori.shounin_shori_kengen_name)}</option>
										</c:forEach>
									</select>
									<input type='text' name='gougiName' class='input-inline' style='<c:if test='${record.bumonRoleHyouji}'>display:none;</c:if> width:300px;' readonly="readonly" value='${su:htmlEscape(record.gougiName)}'>
									<span class='required' id ='gougiTop' <c:if test='${record.bumonRoleHyouji || record.gougiEdano ne "1"}'>style='display:none'</c:if>><b>合議</b></span>
									<span id ='gougiSpan' <c:if test='${!(record.bumonRoleHyouji || record.gougiEdano ne "1")}'>style='display:none'</c:if>>　　</span>
									<button type='button' name='shouninshaAdd' class='btn btn-mini' <c:if test='${record.bumonRoleHyouji}'>style='display:none'</c:if>>+</button>
									<button type='button' name='shouninshaDel' class='btn btn-mini'>-</button>
									<button type='button' name='bumonRoleSearch' class='btn btn-small input-inline' <c:if test='${!record.bumonRoleHyouji}'>style='display:none'</c:if> >役割選択</button>
									<button type='button' name='gougiSearch' class='btn btn-small input-inline' <c:if test='${record.bumonRoleHyouji}'>style='display:none'</c:if>>合議部署選択</button>
									<button type='button' name='gougiAdd' class='btn btn-small input-inline'>合議挿入</button>
									<button type='button' name='bumonRoleAdd' class='btn btn-small input-inline'>役割挿入</button>
									<input type="hidden" name='bumonRoleCd' value='${su:htmlEscape(record.bumonRoleCd)}'>
									<input type="hidden" name='gougiCd' value='${su:htmlEscape(record.gougiCd)}'>
									<input type="hidden" name='gougiEdano' value='${su:htmlEscape(record.gougiEdano)}'>
									<input type="hidden" name='bumonRoleHyouji' value='${su:htmlEscape(record.bumonRoleHyouji)}'>
								</div>
</c:forEach>
</c:if>
							</div>
						</div>
						<h2>有効期限</h2>
						<div class='control-group'>
							<label class='control-label'> 有効期限</label>
							<div class='controls'>
								<span class='required'>*</span>
								<input type='text' name='yuukouKigenFrom' class='input-small datepicker' value='${su:htmlEscape(yuukouKigenFrom)}'>
								<span> ～ </span>
								<input type='text' name='yuukouKigenTo' class='input-small datepicker' value='${su:htmlEscape(yuukouKigenTo)}'>
							</div>
						</div>
						<div class='control-group'>
<!-- 							<button type="submit" class='btn'><i class='icon-hdd'></i> 登録</button> -->
							<button type='button' onClick="eventBtn()" class='btn'><i class='icon-hdd'></i> 登録</button>
					</div>
						<!-- Modal -->
						<div id='dialog'></div>
					</form></div>
				</div><!-- main -->
			</div><!-- content -->
			<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>

/**
 * 部門選択ボタン押下時Function
 */
function bumonSensaku() {
	dialogRetBumonName = $("input[name=bumon]");
	dialogRetBumonCd   = $("input[name=bumonCd]");

	// 取引選択後に部門を変更した場合の相関チェック
	//dialogRetBumonSentakuCallback・・・機能していないようなので廃止

	commonBumonSentaku();
}

/**
 * 役割選択ボタン押下時Function
 */
function bumonRoleSensaku() {
	dialogRetBumonRoleName = $(this).closest("div").find("input[name=bumonRole]");
	dialogRetBumonRoleId = $(this).closest("div").find("input[name=bumonRoleCd]");
	commonBumonRoleSentaku();
}

/**
 * 合議部署選択ボタン押下時Function
 */
function gougiSensaku() {
	dialogRetGougiBushoName = $(this).closest("div").find("input[name=gougiName]");
	dialogRetGougiBushoId = $(this).closest("div").find("input[name=gougiCd]");
	commonGougiBushoSentaku();
}

/**
 * 承認者+ボタン押下時Function
 */
function shouninshaAdd() {
	var _div = $(this).closest("div").clone();
	$(this).closest("div").after(_div);
	var div = $(this).closest("div").next();
	div.addClass("blank");
	inputClear(div);
	div.find("input[name=bumonRoleCd]").val("");
	div.find("input[name=gougiCd]").val("");
	div.find("span#gougiTop").css("display", "none");
	div.find("span#gougiSpan").css("display", "");
	if (div.find("input[name=bumonRoleHyouji]").val() == 'true') {
		div.find("input[name=gougiEdano]").val("");
	} else {
		$(this).closest("div").nextAll().each(function(){
			if ($(this).find("input[name=bumonRoleHyouji]").val() != 'true') {
				var index = $(this).find("input[name=gougiEdano]").val();
				$(this).find("input[name=gougiEdano]").val(++index);
				var prevIndex = $(this).closest("div").next().find("input[name=gougiEdano]").val();
				if (prevIndex == 1) {
					return false;
				}
			} else {
				return false;
			}
		});
	}
}

/**
 * 承認者-ボタン押下時Function
 */
function shouninshaDel() {
	if(1 == $("#bumonRoleList div").length) {
		inputClear($(this).closest("div"));
		$(this).closest("div").find("input[name=bumonRoleCd]").val("");
		$(this).closest("div").find("input[name=gougiCd]").val("");
	} else {
		var div = $(this).closest("div");
		div.nextAll().each(function(){
			if ($(this).find("input[name=bumonRoleHyouji]").val() != 'true') {
				var index = $(this).find("input[name=gougiEdano]").val();
				if (index > 1) {
					$(this).find("input[name=gougiEdano]").val(--index);
					if (index == 1) {
						$(this).find("span#gougiTop").css("display", "");
						$(this).find("span#gougiSpan").css("display", "none");
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		});
		div.remove();
	}
}


/**
 * 合議挿入ボタン押下時Function
 */
function gougiAdd() {
	var _div = $(this).closest("div").clone();
	$(this).closest("div").after(_div);
	var div = $(this).closest("div").next();
	div.addClass("blank");
	inputClear(div);
	
	div.find("input[name=bumonRole]").css("display", "none");
	div.find("select[name=shoriKengen]").css("display", "none");
	div.find("input[name=gougiName]").css("display", "");
	div.find("span#gougiTop").css("display", "");
	div.find("span#gougiSpan").css("display", "none");
	div.find("button[name=shouninshaAdd]").css("display", "");
	div.find("button[name=bumonRoleSearch]").css("display", "none");
	div.find("button[name=gougiSearch]").css("display", "");
	
	div.find("input[name=bumonRoleCd]").val("");
	div.find("input[name=gougiCd]").val("");
	div.find("input[name=gougiEdano]").val(1);
	div.find("input[name=bumonRoleHyouji]").val(false);
}

/**
 * 役割挿入ボタン押下時Function
 */
function bumonRoleAdd() {
	var _div = $(this).closest("div").clone();
	$(this).closest("div").after(_div);
	var div = $(this).closest("div").next();
	div.addClass("blank");
	inputClear(div);
	
	div.find("input[name=bumonRole]").css("display", "");
	div.find("select[name=shoriKengen]").css("display", "");
	div.find("input[name=gougiName]").css("display", "none");
	div.find("span#gougiTop").css("display", "none");
	div.find("span#gougiSpan").css("display", "");
	div.find("button[name=shouninshaAdd]").css("display", "none");
	div.find("button[name=bumonRoleSearch]").css("display", "");
	div.find("button[name=gougiSearch]").css("display", "none");
	
	div.find("input[name=bumonRoleCd]").val("");
	div.find("input[name=gougiCd]").val("");
	div.find("input[name=gougiEdano]").val("");
	div.find("input[name=bumonRoleHyouji]").val(true);
	
	div.nextAll().each(function(){
		if ($(this).find("input[name=bumonRoleHyouji]").val() != 'true') {
			var prevDiv = $(this).closest("div").prev();
			if (prevDiv.find("input[name=bumonRoleHyouji]").val() != 'true') {
				var index = prevDiv.find("input[name=gougiEdano]").val();
				$(this).find("input[name=gougiEdano]").val(++index);
			} else {
				$(this).find("input[name=gougiEdano]").val(1);
				$(this).find("span#gougiTop").css("display", "");
				$(this).find("span#gougiSpan").css("display", "none");
			}
		} else {
			return false;
		}
	});
}

/**
 * 金額の活性制御
 */
function kingakuEnabled() {

	var version = jQuery('option:selected').attr("data-version");
	var cnt = jQuery('option:selected').attr("data-cnt");
	
	if (version >= 1 && cnt <= 0) {
		$("input[name=kingakuFrom]").val("");
		$("input[name=kingakuTo]").val("");
		$("input[name=kingakuFrom]").attr("disabled",true);
		$("input[name=kingakuTo]").attr("disabled",true);
	} else {
		$("input[name=kingakuFrom]").attr("disabled",false);
		$("input[name=kingakuTo]").attr("disabled",false);
	}
}

/**
 * 取引+ボタン押下時Function
 */
function torihikiAdd() {
	var _div = $(this).closest("div").clone();
	$(this).closest("div").after(_div);
	var div = $(this).closest("div").next();
	div.addClass("blank");
	inputClear(div);
	div.find("input[name=shiwakeEdaNo]").val("");
	div.find("input[name=torihikiName]").val("");
}

/**
 * 取引-ボタン押下時Function
 */
function torihikiDel() {
	if(1 == $("#torihikiList div").length) {
		inputClear($(this).closest("div"));
		$(this).closest("div").find("input[name=shiwakeEdaNo]").val("");
		$(this).closest("div").find("input[name=torihikiName]").val("");
	} else {
		var div = $(this).closest("div");
		div.remove();
	}
}

/**
 * 取引選択ボタン押下時Function
 */
function torihikiSensaku() {
	var denpyouKbn = $("select[name=denpyouKbn] option:selected").val();
	var bumonCd = $("input[name=bumonCd]").val();
	if(denpyouKbn == ""){
		alert("伝票区分を選択してください。");
	}else if(bumonCd == ""){
		alert("起票部門を入力してください。");
	}else{
		dialogRetTorihikiName				= $(this).closest("div").find("input[name=torihikiName]");
		dialogRetShiwakeEdaNo				= $(this).closest("div").find("input[name=shiwakeEdaNo]");
		dialogCallbackTorihikiSentaku		= function() { torihikiLostFocus(1) };
		commonTorihikiSentaku(denpyouKbn, bumonCd, "", "","");
	}
}

/**
 * 取引ロストフォーカス時Function
 */
function torihikiLostFocus(flg) {
	
	var denpyouKbn = $("select[name=denpyouKbn] option:selected").val();
	var bumonCd = $("input[name=bumonCd]").val();
	if(denpyouKbn == ""){
		alert("伝票区分を選択してください。");
	}else if(bumonCd == ""){
		alert("起票部門を入力してください。");
	}else{
		// 呼出元が取引選択ボタンの押下時Functionの場合はdialogRet○○は代入済なので処理不要
		if(flg != 1){
			dialogRetTorihikiName				= $(this).closest("div").find("input[name=torihikiName]");
			dialogRetShiwakeEdaNo				= $(this).closest("div").find("input[name=shiwakeEdaNo]");
		}
		var title = $(this).parents('#torihiki').find(".control-label").text();
	 	commonShiwakeEdaNoLostFocus(dialogRetShiwakeEdaNo, dialogRetTorihikiName, title, denpyouKbn, bumonCd, "", "");
	}
}

/**
 * 取引の表示制御Function
 */
function torihikiDispalySeigyo(speed){
	if("1" == $("[name=defaultFlg]").val()){
		//デフォルト設定の場合
		torihikiDellAll();
		$("#torihiki").hide(speed);
	} else {
		//デフォルト設定でない場合
		$("#torihiki").show(speed);
	}
}

/**
 * 取引毎設定に関する項目の表示制御
 */
function torihikiEnabled(speed) {

	var routeTorihikiFlg = jQuery('option:selected').attr("data-routeTorihikiFlg");
	
	if(1 != routeTorihikiFlg){
		$("[name=defaultFlgHyouji]").prop('checked', true);
		torihikiDellAll();
		$("#default").hide(speed);
		$("#torihiki").hide(speed);
	}else{
		if("1" == $("[name=defaultFlg]").val()){
			$("[name=defaultFlgHyouji]").prop('checked', true);
			torihikiDellAll();
			$("#torihiki").hide(speed);
		}else{
			$("[name=defaultFlgHyouji]").prop('checked', false);
			$("#torihiki").show(speed);
		}
		$("#default").show(speed);
	}
}

/**
 * デフォルト設定フラグ設定function
 */
function defaultFlgOncleck(){
	if ($("[name=defaultFlgHyouji]").is(':checked')){
		$("[name=defaultFlg]").val("1");
	} else {
		$("[name=defaultFlg]").val("0");
	}
}

/**
 * 取引全削除function
 */
function torihikiDellAll(){
	$.each($("#torihikiList button[name=torihikiDel]"), function(){
		if(1 == $("#torihikiList div").length) {
			inputClear($(this).closest("div"));
			$(this).closest("div").find("input[name=shiwakeEdaNo]").val("");
			$(this).closest("div").find("input[name=torihikiName]").val("");
		} else {
			var div = $(this).closest("div");
			div.remove();
		}
	});
}

//初期表示処理
$(document).ready(function(){
	
	// 金額の活性制御
	kingakuEnabled();
	
	// 伝票区分選択アクション紐付け
	$("select[name=denpyouKbn]").change(function(){
				kingakuEnabled();
				torihikiEnabled(100);
	});
	
	$("input[name=defaultFlgHyouji]").click(function(){
		defaultFlgOncleck();
		torihikiDispalySeigyo(100);
		torihikiDellAll();
	});

	// 取引アクション紐づけ
	$("body").on("click","button[name=torihikiSentakuButton]",torihikiSensaku); 
	$("body").on("change","input[name=shiwakeEdaNo]",torihikiLostFocus); 
	$("body").on("click","button[name=torihikiAdd]",torihikiAdd); 
	$("body").on("click","button[name=torihikiDel]",torihikiDel); 
	
	// 取引の表示制御
	torihikiEnabled(0);
	
	//部門選択アクション紐付け
	$("#bumonSentakuList").find("button.bumonSearch").click(bumonSensaku);

	//承認者選択アクション紐付け
	$("body").on("click","button[name=shouninshaAdd]",shouninshaAdd); 
	$("body").on("click","button[name=shouninshaDel]",shouninshaDel); 
	$("body").on("click","button[name=bumonRoleSearch]",bumonRoleSensaku); 
	$("body").on("click","button[name=gougiAdd]",gougiAdd); 
	$("body").on("click","button[name=gougiSearch]",gougiSensaku); 
	$("body").on("click","button[name=bumonRoleAdd]",bumonRoleAdd); 
	
});

/**
 * イベントボタン押下時のアクションの切り替え
 */
 function eventBtn() {
	var formObject = document.getElementById("myForm");
	defaultFlgOncleck();
	formObject.action = 'bumon_suishou_route_tsuika_touroku';
	$(formObject).submit();
}
		</script>
	</body>
</html>
