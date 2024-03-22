<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- エラー -->
<%@ include file="/jsp/eteam/include/InputError.jsp" %>
<!-- メイン -->
<div id='dialogMainHidaikou'>
	<div class='form-horizontal'><form id='kensakuForm' class='form-horizontal'>
				
					<!-- 検索枠 -->
					<section>
						<div>
							<div class='no-more-tables'>
								<div class='control-group'>
									<label class='control-label' for='userName'>ユーザー名</label>
									<div class='controls'>
										<input type='text' id='userName' name='userName' class='input-medium' maxlength="20" value='${su:htmlEscape(userName)}'>
									</div>
								</div>
								<div class='control-group'>
									<label class='control-label' for='bumonSentakuBtn'>所属部門</label>
									<div class='controls'>
										<input type='text' name='bumonName' class='input-inline input-medium' readonly="readonly" value='${su:htmlEscape(bumonName)}'>
										<button type='button' id='bumonSentakuBtn' name='bumonSentakuBtn' class='btn btn-small input-inline'>選択</button>
										<input type="text" name='bumonCd' style='display:none' class='input-small' value='${su:htmlEscape(bumonCd)}'>
									</div>
								</div>
								<div class='control-group'>
									<label class='control-label' for='bumonRoleSentakuBtn'>役割</label>
									<div class='controls'>
										<input type='text' name='bmnRoleName' class='input-inline input-medium' readonly="readonly" value='${su:htmlEscape(bmnRoleName)}'>
										<button type='button' id='bumonRoleSentakuBtn' name='bumonRoleSentakuBtn' class='btn btn-small input-inline'>選択</button>
										<input type="text" name='bmnRoleId' style='display:none' class='input-small' value='${su:htmlEscape(bmnRoleId)}'>
									</div>
								</div>
								<div class='control-group'>
									<label class='control-label' for='gyoumuRoleSentakuBtn'>業務ロール</label>
									<div class='controls'>
										<input type='text' name='gyoumuRoleName' class='input-inline input-medium' readonly="readonly" value='${su:htmlEscape(gyoumuRoleName)}'>
										<button type='button' id='gyoumuRoleSentakuBtn' name='gyoumuRoleSentakuBtn' class='btn btn-small input-inline'>選択</button>
										<input type="text" name='gyoumuRoleId' style='display:none' class='input-small' value='${su:htmlEscape(gyoumuRoleId)}'>
									</div>
								</div>
							</div>
							<div class='blank'></div>
							<div>
								<button type='button' class='btn' name="kensakuButton"><i class='icon-search'></i> 検索実行</button>
								<button type="button" class='btn' name="clearButton"><i class='icon-remove'></i> 条件クリア</button>
							</div>
						</div>
					</section>
					
					<!-- 検索結果 -->
					<section>
<c:if test="${fn:length(userList) > 0}" >
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>ユーザー名</th>
										<th>所属部門</th>
										<th>役割</th>
										<th>役割の有効期限</th>
									</tr>
								</thead>
								<tbody>
	<c:forEach var="record" items="${userList}">
									<tr class='${record.bg_color}'>
										<td><a class='link user' data-userId='${su:htmlEscape(record.user_id)}' data-userFullName='${su:htmlEscape(record.user_sei)}　${su:htmlEscape(record.user_mei)}' onclick="userClick(this)">${su:htmlEscape(record.user_sei)}　${su:htmlEscape(record.user_mei)}</a>
										<td>${su:htmlEscape(record.bumon_full_name)}</td>
										<td>${su:htmlEscape(record.bumon_role_name)}</td>
										<td>${record.bumon_wariate_kigen_from}～${record.bumon_wariate_kigen_to}</td>
									</tr>
	</c:forEach>
								</tbody>
							</table>
						</div>
</c:if>
					</section>
				</form></div>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
<!--
//入力補助
commonInit($("#dialogMainHidaikou"));

/** 部門選択ボタン押下時Function */
$('button[name=bumonSentakuBtn]').click(function(event) {
	dialogRetBumonName =  $("input[name=bumonName]");
	dialogRetBumonCd =  $("input[name=bumonCd]");
	commonBumonSentaku();

});

/** 役割選択ボタン押下時Function */
$('button[name=bumonRoleSentakuBtn]').click(function(event) {
	dialogRetBumonRoleName =  $("input[name=bmnRoleName]");
	dialogRetBumonRoleId =  $("input[name=bmnRoleId]");
	commonBumonRoleSentaku();
});

/**
 * 業務ロール選択ボタン押下時Function
 */
$('button[name=gyoumuRoleSentakuBtn]').click(function(event) {
	dialogRetGyoumuRoleName = $("input[name=gyoumuRoleName]");
	dialogRetGyoumuRoleId = $("input[name=gyoumuRoleId]");
	commonGyoumuRoleSentaku();
});

/**
 * 検索ボタン押下時function
 */
 $('button[name=kensakuButton]').click(function(event) {
	var userName = $("input[name=userName]").val();
	var bumonCd = $("input[name=bumonCd]").val();
	var bumonName = $("input[name=bumonName]").val();
	var bmnRoleId = $("input[name=bmnRoleId]").val();
	var bmnRoleName = $("input[name=bmnRoleName]").val();
	var gyoumuRoleId = $("input[name=gyoumuRoleId]").val();
	var gyoumuRoleName = $("input[name=gyoumuRoleName]").val();
	$("#dialog2").empty();
	$("#dialog2")
		.load("hi_daikou_user_sentaku?userName=" + encodeURI(userName) + "&bumonCd=" + encodeURI(bumonCd) + "&bumonName=" + encodeURI(bumonName) + "&bmnRoleId=" + encodeURI(bmnRoleId) + "&bmnRoleName=" + encodeURI(bmnRoleName) + "&gyoumuRoleId=" + encodeURI(gyoumuRoleId) + "&gyoumuRoleName=" + encodeURI(gyoumuRoleName));
});

/** クリアボタン押下時Function */
$('button[name=clearButton]').click(function(event) {
	$("input[name=userName]").val("");
	$("input[name=bumonName]").val("");
	$("input[name=bumonCd]").val("");
	$("input[name=bmnRoleName]").val("");
	$("input[name=bmnRoleId]").val("");
	$("input[name=gyoumuRoleName]").val("");
	$("input[name=gyoumuRoleId]").val("");
});

/**
 * ユーザー選択時Function
 * @param a 選択されたユーザーリンク
 */
function userClick(a) {
	
	var tr = $(a);

	if ("dialogRetHiDaikouUserId" in window) dialogRetHiDaikouUserId.val(tr.attr("data-userId"));
	if ("dialogRetHiDaikouUsername" in window) dialogRetHiDaikouUsername.val(tr.attr("data-userFullName"));
	if ("dialogCallbackHiDaikouUserSentaku" in window)	dialogCallbackHiDaikouUserSentaku();
	
	$("#dialog2").children().remove();
	$("#dialog2").dialog("close");

}
//-->
</script>
