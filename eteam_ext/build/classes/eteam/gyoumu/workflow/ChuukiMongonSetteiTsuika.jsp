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
		<title>最終承認者・注記文言設定追加｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>最終承認者・注記文言設定追加</h1>
				
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				
				<!-- メイン -->
				<div id='main'>
					<div>
						<form id='myForm' method='post' action='chuuki_mongon_settei_tsuika_touroku' enctype='multipart/form-data' class='form-horizontal'>
						<div class='control-group'>
							<h2>制御条件</h2>
							<label class='control-label'><span class='required'>*</span>伝票</label>
							<div class='controls'>
								${denpyouPull}
							</div>
						</div>
						<div class='control-group'>
							<label class='control-label'> 最終承認者</label>
							<div id="gyoumuRoleList" class='controls'>
<c:if test='${"0" eq isError}'>			
								<div>
									処理権限名 <input type='text' name='shorikengen' class='input-small' maxlength=6>
									<input type='text' name='gyoumuRole' class='input-inline' readonly="readonly">
									<button type='button' name='gyoumuRoleAdd' class='btn btn-mini'>+</button>
									<button type='button' name='gyoumuRoleDel' class='btn btn-mini'>-</button>
									<button type='button' name='gyoumuRoleSearch' class='btn btn-small input-inline'>業務ロール選択</button>
									<input type='hidden' name='gyoumuRoleId'>
								</div>
</c:if>
<c:if test='${"1" eq isError}'>
<c:forEach var="record" items="${gyoumuRoleList}" varStatus="status">

								<div <c:if test="${status.index > 0}"> class='blank' </c:if>>
									処理権限名 <input type='text' name='shorikengen' class='input-small' maxlength=6 value='${su:htmlEscape(record.shorikengen)}'>
									<input type='text' name='gyoumuRole' class='input-inline' readonly="readonly" value='${su:htmlEscape(record.gyoumuRole)}'>
									<button type='button' name='gyoumuRoleAdd' class='btn btn-mini'>+</button>
									<button type='button' name='gyoumuRoleDel' class='btn btn-mini'>-</button>
									<button type='button' name='gyoumuRoleSearch' class='btn btn-small input-inline'>業務ロール選択</button>
									<input type='hidden' name='gyoumuRoleId' value='${su:htmlEscape(record.gyoumuRoleId)}'>
								</div>
</c:forEach>
</c:if>
							</div>
						</div>
						<div class='control-group'>
							<label class='control-label'><span class='required'>*</span>注記文言</label>
							<div class='controls'>
								<textarea name="chuukiMongon" class='input-xxlarge textarea-height' rows="8" maxLength='400' placeholder="ここに注記文言を入力して下さい。">${su:htmlEscape(chuukiMongon)}</textarea>
							</div>
						</div>
						<div class='control-group'>
							<label class='control-label'>有効期限</label>
							<div class='controls'>
								<span class='required'>*</span><input type='text' name='yuukouKigenFrom' class='input-small datepicker' value='${su:htmlEscape(yuukouKigenFrom)}'>
								<span> ～ </span>
								<input type='text' name='yuukouKigenTo' class='input-small datepicker' value='${su:htmlEscape(yuukouKigenTo)}'>
							</div>
						</div>

					<div class='control-group'>
						<button type="submit" class='btn'><i class='icon-hdd'></i> 登録</button>
					</div>
					
					<!-- Modal -->
					<div id='dialog'></div>
					</form>
					</div>
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
 * 業務ロール選択ボタン押下時Function
 */
function gyoumuRoleKensaku() {
	dialogRetGyoumuRoleName = $(this).closest("div").find("input[name=gyoumuRole]");
	dialogRetGyoumuRoleId = $(this).closest("div").find("input[name=gyoumuRoleId]"); 
	commonGyoumuRoleSentaku();
}

/**
 * 業務ロール+ボタン押下時Function
 */
function gyoumuRoleAdd() {
	
	var div = $("<div class='blank'>処理権限名 <input type='text' name='shorikengen' class='input-small' maxlength=6><input type='text' name='gyoumuRole' class='input-inline add-left-margin' readonly='readonly'><button type='button' name='gyoumuRoleAdd' class='btn btn-mini add-left-margin'>+</button><button type='button' name='gyoumuRoleDel' class='btn btn-mini add-left-margin'>-</button><button type='button' name='gyoumuRoleSearch' class='btn btn-small input-inline add-left-margin'>業務ロール選択</button><input type='hidden' name='gyoumuRoleId'></div>");
	$("#gyoumuRoleList").append(div);
}

/**
 * 業務ロール-ボタン押下時Function
 */
function gyoumuRoleDel() {
	if(1 == $("#gyoumuRoleList div").length) {
		inputClear($(this).closest("div"));
		hiddenClear($(this).closest("div"));
	} else {
		$(this).closest("div").remove();
	}
}

//初期表示処理
$(document).ready(function(){

	//業務ロール選択アクション紐付け
	$("body").on("click","button[name=gyoumuRoleAdd]",gyoumuRoleAdd); 
	$("body").on("click","button[name=gyoumuRoleDel]",gyoumuRoleDel); 
	$("body").on("click","button[name=gyoumuRoleSearch]",gyoumuRoleKensaku); 
});

		</script>
	</body>
</html>
