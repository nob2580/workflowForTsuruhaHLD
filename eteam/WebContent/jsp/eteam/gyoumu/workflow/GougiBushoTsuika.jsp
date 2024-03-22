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
		<title>合議部署追加｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>合議部署追加</h1>
				
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'>
					<div><form id='myForm' method='post' action='gougi_busho_tsuika_touroku' enctype='multipart/form-data' class='form-horizontal'>
						<div class='control-group'>
							<h2>制御条件</h2>
						</div>
						<div class='control-group'>
							<label class='control-label'><span class='required'>*</span>合議部署名</label>
							<div class='controls'>
								<input type='text' name='gougiBushoName' class='input-inline' style='width:400px' maxlength=20 value='${su:htmlEscape(gougiBushoName)}'>
							</div>
						</div>
						<h2>承認者</h2>
						<div class='control-group'>
							<label class='control-label'><span class='required'>*</span>承認者</label>
							<div id="shouninshaList" class='controls'>
<c:if test='${"0" eq isError}'>
								<div>
									<input type='text' name='bumon' class='input-medium input-inline' readonly="readonly">
									<button type='button' name='bumonSearch' class='btn btn-small input-inline'>部門選択</button>
									<input type='text' name='bumonRole' class='input-small input-inline' readonly="readonly">
									<button type='button' name='bumonRoleSearch' class='btn btn-small input-inline'>役割選択</button>
									<select name='shoriKengen' class='input-medium input-inline'>
										<option></option>
										<c:forEach var='record' items='${shoriKengenList}'>
											<option value='${su:htmlEscape(record.shounin_shori_kengen_no)}'>${su:htmlEscape(record.shounin_shori_kengen_name)}</option>
										</c:forEach>
									</select>
									<select name='hiritsu' class='input-medium input-inline'>
										<option value='1'>全員</option>
										<option value='2'>いずれか１人</option>
										<option value='3'>比率</option>
									</select>
									<span id='hiritsuPerArea' style="display:none"><input type='text' name='hiritsuPer' class='input-mini input-inline'>％</span>
									<button type='button' name='bumonRoleAdd' class='btn btn-mini'>+</button>
									<button type='button' name='bumonRoleDel' class='btn btn-mini'>-</button>
									<input type="hidden" name='bumonCd'>
									<input type="hidden" name='bumonRoleCd'>
								</div>
</c:if>
<c:if test='${"1" eq isError}'>
<c:forEach var="record" items="${shouninshaList}" varStatus="status">
								<div <c:if test="${status.index > 0}"> class='blank' </c:if>>
									<input type='text' name='bumon' class='input-medium input-inline' readonly="readonly" value='${su:htmlEscape(record.bumon)}'>
									<button type='button' name='bumonSearch' class='btn btn-small input-inline'>部門選択</button>
									<input type='text' name='bumonRole' class='input-medium input-inline' readonly="readonly" value='${su:htmlEscape(record.bumonRole)}'>
									<button type='button' name='bumonRoleSearch' class='btn btn-small input-inline'>役割選択</button>
									<select name='shoriKengen' class='input-medium input-inline'>
										<option></option>
										<c:forEach var='shori' items='${shoriKengenList}'>
											<option value='${su:htmlEscape(shori.shounin_shori_kengen_no)}' <c:if test='${shori.shounin_shori_kengen_no eq record.shoriKengenCd}'>selected</c:if>>${su:htmlEscape(shori.shounin_shori_kengen_name)}</option>
										</c:forEach>
									</select>
									<select name='hiritsu' class='input-medium input-inline'>
										<option value='1' <c:if test='${record.hiritsu eq "1"}'>selected</c:if>>全員</option>
										<option value='2' <c:if test='${record.hiritsu eq "2"}'>selected</c:if>>いずれか１人</option>
										<option value='3' <c:if test='${record.hiritsu eq "3"}'>selected</c:if>>比率</option>
									</select>
									<span id='hiritsuPerArea' <c:if test='${record.hiritsu ne "3"}'>style="display:none"</c:if>><input type='text' name='hiritsuPer' class='input-mini input-inline' value='${su:htmlEscape(record.hiritsuPer)}'>％</span>
									<button type='button' name='bumonRoleAdd' class='btn btn-mini'>+</button>
									<button type='button' name='bumonRoleDel' class='btn btn-mini'>-</button>
									<input type="hidden" name='bumonCd' value='${su:htmlEscape(record.bumonCd)}'>
									<input type="hidden" name='bumonRoleCd' value='${su:htmlEscape(record.bumonRoleCd)}'>
								</div>
</c:forEach>
</c:if>
							</div>
						</div>
						<div class='control-group'>
							<button type="submit" class='btn'><i class='icon-hdd'></i> 登録</button>
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
 * 承認必要人数変更時Function
 */
function hiritsuSentaku() {
	if ($(this).val() == "3") {
		$(this).closest("div").find("#hiritsuPerArea").css("display", "");
	} else {
		$(this).closest("div").find("#hiritsuPerArea").css("display", "none");
		$(this).closest("div").find("input[name=hiritsuPer]").val("");
	}
}

/**
 * 部門選択ボタン押下時Function
 */
function bumonSensaku() {
	dialogRetBumonName = $(this).closest("div").find("input[name=bumon]");
	dialogRetBumonCd   = $(this).closest("div").find("input[name=bumonCd]");
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
 * 役割+ボタン押下時Function
 */
function bumonRoleAdd() {
	var _div = $("#shouninshaList").find("div:first").clone();
	$("#shouninshaList").append(_div);
	var div = $("#shouninshaList").find("div:last");
	div.addClass("blank");
	inputClear(div);
	div.find("#hiritsuPerArea").css("display", "none");
}

/**
 * 役割-ボタン押下時Function
 */
function bumonRoleDel() {
	if(1 == $("#shouninshaList div").length) {
		inputClear($(this).closest("div"));
		$(this).closest("div").find("#hiritsuPerArea").css("display", "none");
	} else {
		$(this).closest("div").remove();
	}
}

//初期表示処理
$(document).ready(function(){
	//承認者アクション紐付け
	$("body").on("click","button[name=bumonRoleAdd]",bumonRoleAdd); 
	$("body").on("click","button[name=bumonRoleDel]",bumonRoleDel); 
	$("body").on("click","button[name=bumonRoleSearch]",bumonRoleSensaku); 
	$("body").on("click","button[name=bumonSearch]",bumonSensaku); 
	$("body").on("change","select[name=hiritsu]",hiritsuSentaku); 
});

		</script>
	</body>
</html>
