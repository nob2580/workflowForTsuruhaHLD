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
		<title>ユーザー検索｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
<!--
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
				<h1>ユーザー検索</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main' class='form-horizontal'><form id="userKensakuForm" method="get" action='user_kensaku_kensaku'>
				
					<!-- 検索枠 -->
					<section>
						<h2>検索条件</h2>
						<div>
							<div class='no-more-tables'>
								<div class='control-group'>
									<label class='control-label' for='userName'>ユーザー名</label>
									<div class='controls'>
										<input type='text' id='userName' name='userName' class='input-medium' maxlength="20" value='${su:htmlEscape(userName)}'>
										<label class='label' for='userId'>ユーザーID</label>
										<input type='text' id='userId' name='userId' class='input-medium' maxlength="30" value='${su:htmlEscape(userId)}' style="ime-mode:disabled;">
										<label class='label' for='shainNo'>社員番号</label>
										<input type='text' id='shainNo' name='shainNo' class='input-medium' maxlength="15" value='${su:htmlEscape(shainNo)}' style="ime-mode:disabled;">
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
										<input type='text' name='bumonRoleName' class='input-inline input-medium' readonly="readonly" value='${su:htmlEscape(bumonRoleName)}'>
										<button type='button' id='bumonRoleSentakuBtn' name='bumonRoleSentakuBtn' class='btn btn-small input-inline'>選択</button>
										<input type="text" name='bumonRoleId' style='display:none' class='input-small' value='${su:htmlEscape(bumonRoleId)}'>
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
								<div class='control-group' <c:if test='<%= !RegAccess.checkEnableKeihiSeisan() %>'>style="display: none"</c:if>>
									<label class='control-label' for='dairiKihyouFlag'>代理申請可能</label>
									<div class='controls'>
										<input type="checkbox" id='dairiKihyouFlag' name='dairiKihyouFlag' value='1' <c:if test='${"1" eq dairiKihyouFlag}'>checked</c:if>>
									</div>
								</div>
								<div class='control-group'>
									<label class='control-label' for='accountTmpLockFlag'>アカウント一時ロック</label>
									<div class='controls'>
										<input type="checkbox" id='accountTmpLockFlag' name='accountTmpLockFlag' value='1' <c:if test='${"1" eq accountTmpLockFlag}'>checked</c:if>>
									</div>
								</div>
								<div class='control-group'>
									<label class='control-label' for='accountLockFlag'>アカウント永続ロック</label>
									<div class='controls'>
										<input type="checkbox" id='accountLockFlag' name='accountLockFlag' value='1' <c:if test='${"1" eq accountLockFlag}'>checked</c:if>>
									</div>
								</div>
							</div>
							<div class='blank'></div>
							<div>
								<button type='submit' class='btn'><i class='icon-search'></i> 検索実行</button>
								<button type="button" class='btn' name="clearButton"><i class='icon-remove'></i> 条件クリア</button>
								<button type="button" class='btn' name="newUser" onClick="window.open('user_tsuika')"><i class='icon-plus'></i> 追加</button>
							</div>
						</div>
					</section>
					
					<!-- 検索結果 -->
					<section>
						<h2>検索結果</h2>
<c:if test="${fn:length(userList) > 0}" >
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th><nobr>ユーザー名</nobr></th>
										<th><nobr>ユーザーID</nobr></th>
										<th><nobr>社員番号</nobr></th>
										<th style="width:300px"><nobr>所属部門</nobr></th>
										<th><nobr>役割</nobr></th>
										<th><nobr>役割の有効期限</nobr></th>
										<th><nobr>一時ロック日時</nobr></th>
										<th><nobr>永続ロック日時</nobr></th>
										<th><nobr>代理申請可</nobr></th>
									</tr>
								</thead>
								<tbody>
	<c:forEach var="record" items="${userList}">
									<tr class='${record.bg_color}'>
										<td><nobr><a href='user_jouhou?userId=${su:htmlEscape(record.user_id)}' target='_blank'>${su:htmlEscape(record.user_sei)}　${su:htmlEscape(record.user_mei)}</a></nobr></td>
										<td><nobr>${su:htmlEscape(record.user_id)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.shain_no)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.bumon_full_name)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.bumon_role_name)}</nobr></td>
										<td><nobr>${record.bumon_wariate_kigen_from}～${record.bumon_wariate_kigen_to}</nobr></td>
										<td><nobr>${record.tmpLockTime}</nobr></td>
										<td><nobr>${record.lockTime}</nobr></td>
										<td align = "center"><input type="checkbox" id='dairiKihyou' name='dairiKihyou' value='1' disabled="disabled" <c:if test='${"1" eq record.dairiKihyou}'>checked</c:if>></td>
									</tr>
	</c:forEach>
								</tbody>
							</table>
						</div>
</c:if>
					</section>
				</form></div><!-- main -->
				<!-- Modal -->
				<div id='dialog'></div>
			</div><!-- cotent -->
			<div id='push'></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>

/** 部門選択ボタン押下時Function */
$('button[name=bumonSentakuBtn]').click(function(event) {
	dialogRetBumonName =  $("input[name=bumonName]");
	dialogRetBumonCd =  $("input[name=bumonCd]");
	commonBumonSentaku();
	return false;
});

/** 役割選択ボタン押下時Function */
$('button[name=bumonRoleSentakuBtn]').click(function(event) {
	dialogRetBumonRoleName =  $("input[name=bumonRoleName]");
	dialogRetBumonRoleId =  $("input[name=bumonRoleId]");
	commonBumonRoleSentaku();
	return false;
});

/**
 * 業務ロール選択ボタン押下時Function
 */
$('button[name=gyoumuRoleSentakuBtn]').click(function(event) {
	dialogRetGyoumuRoleName = $("input[name=gyoumuRoleName]");
	dialogRetGyoumuRoleId = $("input[name=gyoumuRoleId]");
	commonGyoumuRoleSentaku();
	return false;
});

/** クリアボタン押下時Function */
$('button[name=clearButton]').click(function(event) {
	$("input[name=userName]").val("");
	$("input[name=userId]").val("");
	$("input[name=shainNo]").val("");
	$("input[name=bumonName]").val("");
	$("input[name=bumonCd]").val("");
	$("input[name=bumonRoleName]").val("");
	$("input[name=bumonRoleId]").val("");
	$("input[name=gyoumuRoleName]").val("");
	$("input[name=gyoumuRoleId]").val("");
	$("input[name=accountTmpLockFlag]").prop("checked", false);
	$("input[name=accountLockFlag]").prop("checked", false);
	$("input[name=dairiKihyouFlag]").prop("checked", false);
});
		</script>
	</body>
</html>
