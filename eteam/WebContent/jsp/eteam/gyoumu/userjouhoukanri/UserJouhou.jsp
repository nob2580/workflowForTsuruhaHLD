<%@ page import="eteam.symbol.EteamSymbol"%>
<%@ page import="eteam.common.RegAccess"%>
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
		<title>ユーザー情報｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
<!--
-->
		</style>
	</head>
	<body <c:if test="${successful}" >onLoad="window.close();"</c:if>>
		<div id='wrap'>
			<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>
			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
				<h1>ユーザー情報</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main' class='form-horizontal' >

<c:if test="${fn:length(bumonRoleList) > 0}" >
					<select name='bumonRoleList' class='input-small' style="display: none">
						<option></option>
	<c:forEach var="record" items="${bumonRoleList}">
						<option value='${su:htmlEscape(record.bumon_role_id)}'>${su:htmlEscape(record.bumon_role_name)}</option>
	</c:forEach>
					</select>
</c:if>

				<form id="myForm" method="post">

					<!-- ユーザー属性 -->
					<section>
						<h2>ユーザー情報</h2>
						<div class='control-group'>
							<label class='control-label' for='userId'>ユーザーID</label>
							<div class='controls' style="padding: 5px" id='userId'><b>${su:htmlEscape(userId)}</b></div>
							<input type='hidden' name='userId' value='${su:htmlEscape(userId)}'>
						</div>
						<div class='control-group'>
							<label class='control-label' for='shainNo'><span class='required'>*</span>社員番号</label>
							<div class='controls'>
								<input type='text' id='shainNo' name='shainNo' class='input-medium <c:if test="${userInfoHenkou}">data-delayenable</c:if>' maxlength="15" value='${su:htmlEscape(shainNo)}' disabled>
							</div>
						</div>
						<div class='control-group'>
							<label class='control-label' for='userSei'><span class='required'>*</span>ユーザー名</label>
							<div class='controls'>
								<input type='text' id='userSei' name='userSei' class='input-medium <c:if test="${userInfoHenkou}">data-delayenable</c:if>' maxlength="10" value='${su:htmlEscape(userSei)}' style="margin-right:8px;" disabled>
								<input type='text' id='userMei' name='userMei' class='input-medium <c:if test="${userInfoHenkou}">data-delayenable</c:if>' maxlength="10" value='${su:htmlEscape(userMei)}' disabled>
							</div>
						</div>
						<div class='control-group'>
							<label class='control-label' for='mailAddress'>メールアドレス</label>
							<div class='controls'>
								<input type='text' id='mailAddress' name='mailAddress' class='input-xlarge data-delayenable' maxlength="50" value='${su:htmlEscape(mailAddress)}' disabled>
							</div>
						</div>
<c:if test='<%= RegAccess.checkEnableKeihiSeisan() %>'>
						<div class='control-group' <c:if test="${!securityPatternFlag}"> style="display: none"</c:if>>
							<label class='control-label' for='securityPattern'>セキュリティパターン</label>
							<div class='controls'>
								<input type='text' id='securityPattern' name='securityPattern' class='input-small <c:if test="${logonIsKanri}">data-delayenable</c:if>' maxlength='4' value='${su:htmlEscape(securityPattern)}' disabled>
							</div>
						</div>
						<div class='control-group'>
							<label class='control-label' for='securityWfonlyFlg'>経費明細一覧データ</label>
							<div class='controls'>
								<input type='checkbox' name='securityWfonlyFlg' value='1' <c:if test='${"1" eq securityWfonlyFlg}'>checked</c:if> disabled> ワークフロー入力データのみ
							</div>
						</div>
</c:if>
						<div class='control-group'>
							<label class='control-label' for='password'>
								<input type='text' name='dummy' value='' maxlength="0" style='width:0px; height:0px; border:none; background:none; padding:0px;'>
								パスワード(変更時)
							</label>
							<div class='controls'>
								<input type='password' id='password' name='password' class='input-medium data-delayenable' maxlength="32" autocomplete='off' disabled>
								<label class='label'><span class='required'>*</span>確認用</label>
								<input type='text' name='dummy' value='qazxswedcvfrtgbnhyujmkio' style="display:none">
								<input type='password' id='password2' class='input-medium data-delayenable' maxlength="32" autocomplete='off' disabled>
							</div>
						</div>
						<div class='control-group' <c:if test="${viewIsAdmin}"> style="display: none"</c:if>>
							<label class='control-label'>パスワード有効期限</label>
							<div class='controls' style="padding: 5px">
								<span>${su:htmlEscape(passwordYuukoukigen)}</span>
								<input type="hidden" name='passwordYuukoukigen'   value='${su:htmlEscape(passwordYuukoukigen)}'>
							</div>
						</div>
						<div class='control-group' <c:if test="${!(logonIsKanri && !viewIsAdmin)}"> style="display: none"</c:if>>
							<label class='control-label' for='userYuukoukigenFrom'>ユーザー有効期限</label>
							<div class='controls'>
								<b><span class='required'>* </span></b>
								<input type='text' id='userYuukoukigenFrom' name='userYuukoukigenFrom' class='input-small datepicker' value='${userYuukoukigenFrom}'>
								<span> ～ </span>
								<input type='text' id='userYuukoukigenTo' name='userYuukoukigenTo' class='input-small datepicker' value='${userYuukoukigenTo}'>
							</div>
						</div>
<c:if test='<%= RegAccess.checkEnableKeihiSeisan() %>'>
						<div class='control-group' <c:if test="${viewIsAdmin}">style="display: none"</c:if>>
							<label class='control-label' for=dairiKihyouFlag>代理起票可能</label>
							<div class='controls'>
								<input type="checkbox" id='dairiKihyouFlag' name='dairiKihyouFlag' value='1' <c:if test='${"1" eq dairiKihyouFlag}'>checked</c:if> <c:if test="${!logonIsKanri}">disabled</c:if>>
							</div>
						</div>
						<div class="control-group" <c:if test="${!(houjinCardFlag && !viewIsAdmin)}">style='display:none;'</c:if>>
							<label class="control-label" for="houjinCardRiyouFlag">法人カード利用</label>
							<div class="controls">
								<input type="checkbox" id="houjinCardRiyouFlag" name="houjinCardRiyouFlag" value="1" <c:if test='${"1" eq houjinCardRiyouFlag}'>checked</c:if> <c:if test="${!logonIsKanri}">disabled</c:if>>
								<label class="label">法人カード識別用番号</label>
<!-- Ver22.05.31.00 識別用番号16→20＋枠を広げる -->								
								<input type="text" id="houjinCardShikibetsuyouNum" name="houjinCardShikibetsuyouNum" class="input-large <c:if test='${"1" eq houjinCardRiyouFlag and logonIsKanri}'>data-delayenable</c:if>" maxlength="20" value='${houjinCardShikibetsuyouNum}' disabled>
<!--  -->								
							</div>
						</div>
						<div class='control-group' <c:if test="${!(logonIsKanri && !viewIsAdmin && shouninRouteHenkouShouninsha)}"> style="display: none"</c:if>>
							<label class='control-label' for='shouninRouteHenkouLevel'>承認ルート変更権限</label>
							<div class="controls">
								<select name='shouninRouteHenkouLevel' class='input-xlarge'>
									<c:forEach var="tmpHenkouLevel" items="${henkouKengenLevelList}">
										<option value='${tmpHenkouLevel.naibu_cd}' <c:if test='${tmpHenkouLevel.naibu_cd eq shouninRouteHenkouLevel}'>selected</c:if>>${su:htmlEscape(tmpHenkouLevel.name)}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class='control-group' <c:if test="${!(logonIsKanri && !viewIsAdmin)}"> style="display: none"</c:if>>
							<label class='control-label' for='maruhiKengenFlag'>マル秘設定権限</label>
							<div class='controls'>
								<input type="checkbox" id='maruhiKengenFlag' name='maruhiKengenFlag' value='1' <c:if test='${"1" eq maruhiKengenFlag}'>checked</c:if>>
							</div>
						</div>
						<div class='control-group' <c:if test="${!(logonIsKanri && !viewIsAdmin)}"> style="display: none"</c:if>>
							<label class='control-label' for='maruhiKaijyoFlag'>マル秘解除権限</label>
							<div class='controls'>
								<input type="checkbox" id='maruhiKaijyoFlag' name='maruhiKaijyoFlag' value='1' <c:if test='${"1" eq maruhiKaijyoFlag}'>checked</c:if>>
							</div>
						</div>
</c:if>
						<div class='control-group' <c:if test="${!logonIsKanri}"> style="display: none"</c:if>>
							<label class='control-label' for='accountTmpLockFlag'>アカウント一時ロック</label>
							<div class='controls'>
								<input type="checkbox" id='accountTmpLockFlag' name='accountTmpLockFlag' value='1' <c:if test='${"1" eq accountTmpLockFlag}'>checked</c:if> <c:if test='${"1" ne accountTmpLockFlag}'>disabled</c:if>>
							</div>
						</div>
						<div class='control-group' <c:if test="${!(logonIsKanri && !viewIsAdmin)}"> style="display: none"</c:if>>
							<label class='control-label' for='accountLockFlag'>アカウント永続ロック</label>
							<div class='controls'>
								<input type="checkbox" id='accountLockFlag' name='accountLockFlag' value='1' <c:if test='${"1" eq accountLockFlag}'>checked</c:if>>
							</div>
						</div>
<c:if test='<%= RegAccess.checkEnableKeihiSeisan() && RegAccess.checkEnableZaimuKyotenOption() %>'>

						<div class='control-group' <c:if test="${!(logonIsKanri && !viewIsAdmin)}"> style="display: none"</c:if>>
							<label class='control-label' for='zaimuKyotenNyuryokuOnlyFlg'>拠点入力のみ使用</label>
							<div class='controls'>
								<input type="checkbox" id='zaimuKyotenNyuryokuOnlyFlg' name='zaimuKyotenNyuryokuOnlyFlg' value='1' <c:if test='${"1" eq zaimuKyotenNyuryokuOnlyFlg}'>checked</c:if>>
							</div>
						</div>
</c:if>
						<button type='button' class='btn' onClick="eventBtn('update')"><i class='icon-refresh'></i> 変更</button>
<c:if test="${logonIsKanri && !viewIsAdmin}">
						<button type='button' class='btn' onClick="eventBtn('delete')"><i class='icon-remove'></i> 削除</button>
</c:if>
					</section>

<c:if test="${logonIsKanri && !viewIsAdmin}">

					<!-- 部門 -->
					<div id='shozokuBumonKanriList'>
						<section class='print-unit'>
							<!-- サブタイトル -->
							<h2>所属部門管理（※必須）</h2>
							<b><span></span></b>
							<div class='no-more-tables'>
								<div class='control-group'>
									<button type='button' onClick="shozokuBumonAdd()" class='btn'><i class='icon-plus'></i> 追加</button>
								</div>
								<table class='table-bordered table-condensed' style="margin-top:8px;">
								<colgroup width="300">
									<thead>
										<tr>
											<th>所属部門</th>
											<th><b><span class='required'>* </span></b>役割</th>
											<th>
													<c:if test ='${userDaihyouFutanBumonMinyuuryokuCheck eq "1"}'><b><span class='required'>* </span></b></c:if>代表負担部門
											</th>
											<th>有効期限</th>
											<th></th>
										</tr>
									</thead>
									<tbody id='shozokuBumonList'>
	<c:forEach var="hoge" items="${bumonCd}" varStatus="stat">
										<tr style="vertical-align:top;">
											<td>
		<c:if test="${empty bumonName[stat.index]}">
												<span style="color:red; font-weight:bold; ">※所属部門は削除されています。</span>
		</c:if>
												${su:htmlEscape(bumonName[stat.index])}
												<input type="hidden" name='bumonCd'   value='${su:htmlEscape(bumonCd[stat.index])}'>
												<input type='hidden' name='bumonName' value='${su:htmlEscape(bumonName[stat.index])}'>
											</td>
											<td>
		<c:if test="${fn:length(bumonRoleList) > 0}">
												<select name='bumonRoleId' class='input-large'>
													<option></option>
			<c:forEach var="bumonRoleList" items="${bumonRoleList}">
													<option value='${su:htmlEscape(bumonRoleList.bumon_role_id)}' <c:if test='${bumonRoleList.bumon_role_id eq bumonRoleId[stat.index]}'>selected</c:if>>${su:htmlEscape(bumonRoleList.bumon_role_name)}</option>
			</c:forEach>
												</select>
		</c:if>
											</td>
											<td>
											<div>
												<input type='text' name='daihyouFutanBumonCd' class='input-small pc_only data-delayenable' maxlength="8" value='${su:htmlEscape(daihyouFutanBumonCd[stat.index])}' disabled>
		<c:choose>
			<c:when test="${empty daihyouFutanBumonName[stat.index] && !empty daihyouFutanBumonCd[stat.index]}">
												<input type='text'  name='daihyouFutanBumonName' class='' value = "※代表負担部門は削除されています" style="font-size:12.5px;" disabled ><br>
			</c:when>
			<c:otherwise>
												<input type='text' name='daihyouFutanBumonName' class='' value ='${su:htmlEscape(daihyouFutanBumonName[stat.index])}'disabled ><br>
			</c:otherwise>
		</c:choose>
												<button type='button' name='FutanBumonSentakuButton' class='btn btn-small' >選択</button>
												<button type='button' name='FutanBumonClearButton' class='btn btn-small'>クリア</button>
												</div>
									</td>
											<td>
												<b><span class='required'>* </span></b>
												<input type='text' name='bumonYuukoukigenFrom' class='input-small datepicker' value='${su:htmlEscape(bumonYuukoukigenFrom[stat.index])}'>
												<span> ～ </span><br>&nbsp;&nbsp;
												<input type='text' name='bumonYuukoukigenTo' class='input-small datepicker' value='${su:htmlEscape(bumonYuukoukigenTo[stat.index])}'>
											</td>
											<td>
												<button type='button' name='shozokuBumonDel' class='btn btn-small'> 削除</button><br>
												<input  type='hidden' name='hyoujiJun' value='${bumonList.hyouji_jun}'>
												<button type='button' name='shozokuBumonUp' class='btn btn-mini'>↑</button>
												<button type='button' name='shozokuBumonDown' class='btn btn-mini'>↓</button>
											</td>
										</tr>
	</c:forEach>
									</tbody>
								</table>
							</div>
						</section>
					</div>

					<!-- 業務ロール -->
					<div id='gyoumuRoleKanriList'>
						<section class='print-unit' style="margin-top:10px">
							<!-- サブタイトル -->
							<h2>業務ロール管理</h2>
							<div class='no-more-tables'>
								<div class='control-group'>
									<button type='button' onClick="gyoumuRoleAdd()" class='btn'><i class='icon-plus'></i> 追加</button>
								</div>
								<table class='table-bordered table-condensed' style="margin-top:8px">
									<thead>
										<tr>
											<th style="min-width:150px;">業務ロール</th>
											<th style="min-width:240px;">有効期限</th>
											<th style ="min-width:240px"><b><span class='required'>* </span></b>処理部門</th>
											<th style="min-width:65px;"></th>
										</tr>
									</thead>
									<tbody id='gyoumuRoleList'>
	<c:forEach var="hoge" items="${gyoumuRoleId}" varStatus="stat">
										<tr>
											<td>
		<c:if test="${empty gyoumuRoleName[stat.index]}">
												<span style="color:red; font-weight : bold;">※業務ロールは削除されています。</span>
		</c:if>
												${su:htmlEscape(gyoumuRoleName[stat.index])}
												<input type="hidden" name='gyoumuRoleId'   value='${su:htmlEscape(gyoumuRoleId[stat.index])}'>
												<input type='hidden' name='gyoumuRoleName' value='${su:htmlEscape(gyoumuRoleName[stat.index])}'>
											</td>
											<td>
												<b><span class='required'>* </span></b>
												<input type='text' name='gyoumuRoleYuukoukigenFrom' class='input-small datepicker' value='${gyoumuRoleYuukoukigenFrom[stat.index]}'>
												<span> ～ </span>
												<input type='text' name='gyoumuRoleYuukoukigenTo'   class='input-small datepicker' value='${gyoumuRoleYuukoukigenTo[stat.index]}'>
											</td>
											<td>
											<input type="hidden" name='gyoumuRoleShoriBumonCd'   value='${su:htmlEscape(gyoumuRoleShoriBumonCd[stat.index])}'>
		<c:choose>
			<c:when test="${empty gyoumuRoleShoriBumonName[stat.index]}">
												<input type='text'  name='gyoumuRoleShoriBumonName' class='' value = "※所属部門は削除されています" style="font-size:12.5px;" disabled >
			</c:when>
			<c:otherwise>
												<input type='text' name='gyoumuRoleShoriBumonName' class='' value ='${su:htmlEscape(gyoumuRoleShoriBumonName[stat.index])}'disabled >
			</c:otherwise>
		</c:choose>
											<button type='button' name='gyoumuRoleShoriBumonSentakuButton' class='btn btn-small' >選択</button>
											</td>
											<td>
												<button type='button' name='gyoumuRoleDel' class='btn btn-small'><i class='icon-minus'></i> 削除</button>
											</td>
										</tr>
	</c:forEach>
									</tbody>
								</table>
							</div>
						</section>
					</div>
</c:if>
				</form>
				</div><!-- main -->
				<!-- Modal -->
				<div id='dialog'></div>
			</div><!-- content -->
		<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>

/**
 * セキュリティコードに連動してワークフローのみ☑を制御
 */
function securityPatternChange() {
	var ptn = $("input[name=securityPattern]");
	var chk = $("input[name=securityWfonlyFlg]");
	if (ptn.is(":visible") && ptn.val() == "") {
		chk.prop("disabled", true);
		chk.prop("checked", false);
	} else {
<c:if test="${logonIsKanri}">
		chk.prop("disabled", false);
</c:if>
	}
}

/**### 以下、所属部門の処理 ##############################
 *
 * （所属部門）追加ボタン押下時Function
 */
function shozokuBumonAdd() {
	dialogRetBumonSentakuCallback = function(bumonData){
		// 所属部門のセルを作成しデータを追加
		var bumonCd   = bumonData.attr("data-cd");
		var bumonName = bumonData.attr("data-fullname");

		//（所属部門）追加ボタン押下時アクション
		shozokuBumonBody = document.getElementById("shozokuBumonList");
		// 所属部門のBodyへ行追加
		shozokuBumonNewTR = shozokuBumonBody.insertRow(-1);

		//表の行の縦位置を指定
		$(shozokuBumonNewTR).css("vertical-align","top");

		// 所属部門名のセルを作成しデータを追加
		var input_bcd = document.createElement('input');
		input_bcd.type = 'hidden';
		input_bcd.name = 'bumonCd';
		input_bcd.value = bumonCd;

		var input_bnm = document.createElement('input');
		input_bnm.type = 'hidden';
		input_bnm.name = 'bumonName';
		input_bnm.value = bumonName;

		var elem = document.createElement('div');
		elem.appendChild(document.createTextNode(bumonName));
		elem.appendChild(input_bcd);
		elem.appendChild(input_bnm);

		newTD1 = shozokuBumonNewTR.insertCell(0).innerHTML = elem.innerHTML;

		// 役割のセルを作成しデータを追加
		var selectOptionClone = $("select[name=bumonRoleList]").clone();
		newTD2 = shozokuBumonNewTR.insertCell(1).innerHTML = "<select name='bumonRoleId' class='input-large'>" + selectOptionClone.html(); + "<\/select>";

		//代表負担部門のセルを作成しデータを追加
		newTD3 = shozokuBumonNewTR.insertCell(2).innerHTML = "<input type='text' name='daihyouFutanBumonCd' class='input-small pc_only' maxlength=8 value='${su:htmlEscape(shokiDaihyouFutanBumonCd)}'> <input type='text' name='daihyouFutanBumonName' class=''  value='${su:htmlEscape(shokiDaihyouFutanBumonName)}'disabled ><br><button type='button' name='FutanBumonSentakuButton' class='btn btn-small' >選択</button> <button type='button' name='FutanBumonClearButton' class='btn btn-small'>クリア</button>";

		// 有効期限のセルを作成しデータを追加
		newTD4 = shozokuBumonNewTR.insertCell(3).innerHTML = "<b><span class='required'>* <\/span><\/b><input type='text' name='bumonYuukoukigenFrom' class='input-small datepicker'><span> ～ <\/span><br>&nbsp;&nbsp;&nbsp;<input type='text' name='bumonYuukoukigenTo' class='input-small datepicker'>";

		// 削除ボタンのセルを作成しボタンを作成
		newTD5 = shozokuBumonNewTR.insertCell(4).innerHTML = "<button type='button' name='shozokuBumonDel' class='btn btn-small'><\/i> 削除<\/button><br><button type='button' name='shozokuBumonUp' class='btn btn-mini'>↑</button> <button type='button' name='shozokuBumonDown' class='btn btn-mini'>↓</button>";

		// datepickerの初期化
		commonInit($("#shozokuBumonList").find("tr:last"));
	};
	commonBumonSentaku();
}

/**
 * 代表負担部門コードロストフォーカス時Function
 */
function daihyouFutanBumonCdLostFocus() {
	var title = "代表負担部門";
	var tr = $(this).closest("tr");
	dialogRetFutanBumonCd 	= tr.find("input[name=daihyouFutanBumonCd]");
	dialogRetFutanBumonName = tr.find("input[name=daihyouFutanBumonName]");
	commonFutanBumonCdLostFocus("2",dialogRetFutanBumonCd, dialogRetFutanBumonName, title);
}

/**
 * 代表負担部門選択ボタン押下時Function
 */
function daihyouFutanBumonSentaku() {
	var tr = $(this).closest("tr");
	dialogRetFutanBumonCd 	= tr.find("input[name=daihyouFutanBumonCd]");
	dialogRetFutanBumonName = tr.find("input[name=daihyouFutanBumonName]");
	commonFutanBumonSentaku("2");
}

/**
 * 代表負担部門クリアボタン押下時Function
 */
function daihyouFutanBumonClear() {
	var tr = $(this).closest("tr");
	dialogRetFutanBumonCd = tr.find("input[name=daihyouFutanBumonCd]").val("");
	dialogRetFutanBumonName = tr.find("input[name=daihyouFutanBumonName]").val("");
}

/**
 * （所属部門）削除ボタン押下時Function
 */
function shozokuBumonDel() {
	$(this).closest("tr").remove();
}

/**
 * 所属部門 ↑ボタン押下時Function
 */
function shozokuBumonUp(){
	var tr = $(this).closest("tr");
	if(tr.prev()){
		tr.insertBefore(tr.prev());
	}
}

/**
 * 所属部門↓ボタン押下時Function
 */
function shozokuBumonDown() {
	var tr = $(this).closest("tr");
	if(tr.next()){
		tr.insertAfter(tr.next());
	}
}

/**### 以下、業務ロールの追加時の処理 ##############################
 *
 * （業務ロール）追加ボタン押下時Function
 */
function gyoumuRoleAdd() {
	dialogRetGyoumuRoleSentakuCallback = function(gyoumuRoleData){
		var gyoumuRoleId   = gyoumuRoleData.attr("data-cd");
		var gyoumuRoleName = gyoumuRoleData.attr("data-name");

		// 業務ロールのBodyを取得
		gyoumuRoleBody = document.getElementById("gyoumuRoleList");
		// 業務ロールのBodyへ行追加
		gyoumuRoleNewTR = gyoumuRoleBody.insertRow(-1);

		// 業務ロールのセルを作成しデータを追加
		var input_grid = document.createElement('input');
		input_grid.type = 'hidden';
		input_grid.name = 'gyoumuRoleId';
		input_grid.value = gyoumuRoleId;

		var input_grnm = document.createElement('input');
		input_grnm.type = 'hidden';
		input_grnm.name = 'gyoumuRoleName';
		input_grnm.value = gyoumuRoleName;

		var elem = document.createElement('div');
		elem.appendChild(document.createTextNode(gyoumuRoleName));
		elem.appendChild(input_grid);
		elem.appendChild(input_grnm);

		newTD1 = gyoumuRoleNewTR.insertCell(0).innerHTML = elem.innerHTML;

		// 有効期限のセルを作成しデータを追加
		newTD2 = gyoumuRoleNewTR.insertCell(1).innerHTML = "<b><span class='required'>* <\/span><\/b><input type='text' name='gyoumuRoleYuukoukigenFrom' class='input-small datepicker'><span> ～ <\/span><input type='text' name='gyoumuRoleYuukoukigenTo' class='input-small datepicker'>";
		// datepickerの初期化
		commonInit($("#gyoumuRoleList td:last"));

		//処理部門のセルを作成しデータを追加
		newTD3 = gyoumuRoleNewTR.insertCell(2).innerHTML = "<input type='hidden' name='gyoumuRoleShoriBumonCd' value='${su:htmlEscape(gyoumuRoleShokiShoriShozokuBumonCd)}'> <input type='text' name='gyoumuRoleShoriBumonName' class='' value ='${su:htmlEscape(gyoumuRoleShokiShoriShozokuBumonName)}'disabled > <button type='button' name='gyoumuRoleShoriBumonSentakuButton' class='btn btn-small' >選択</button>";

		// 削除ボタンのセルを作成しボタンを作成
		newTD4 = gyoumuRoleNewTR.insertCell(3).innerHTML = "<button type='button' name='gyoumuRoleDel' class='btn btn-small'><i class='icon-minus'><\/i> 削除<\/button>";
	};
	commonGyoumuRoleSentaku();
}

/**
 * (業務ロール)処理部門選択ボタン押下時Function
 */
 function gyoumuRoleShoriBumonSentakuBtn(){
	var tr = $(this).closest("tr");
	dialogRetBumonName = tr.find("input[name=gyoumuRoleShoriBumonName]");
	dialogRetBumonCd   = tr.find("input[name=gyoumuRoleShoriBumonCd]");
	dialogRetBumonSentakuCallback = null;
	commonBumonSentaku("");
}

/**
 * （業務ロール）削除ボタン押下時Function
 */
function gyoumuRoleDel() {
	$(this).closest("tr").remove();
}

//画面表示後の初期化
$(document).ready(function(){

	//セキュリティパターン入力
	securityPatternChange();
	$("input[name=securityPattern]").blur(securityPatternChange);

	//所属部門のアクション紐付け
	$("body").on("blur","input[name=daihyouFutanBumonCd]",daihyouFutanBumonCdLostFocus);
	$("body").on("click","button[name=FutanBumonSentakuButton]",daihyouFutanBumonSentaku);
	$("body").on("click","button[name=FutanBumonClearButton]",daihyouFutanBumonClear);
	$("body").on("click","button[name=shozokuBumonDel]",shozokuBumonDel);
	$("body").on("click","button[name=shozokuBumonUp]",shozokuBumonUp);
	$("body").on("click","button[name=shozokuBumonDown]",shozokuBumonDown);

	//業務ロールのアクション紐付け
	$("body").on("click","button[name=gyoumuRoleAdd]",gyoumuRoleAdd);
	$("body").on("click","button[name=gyoumuRoleShoriBumonSentakuButton]",gyoumuRoleShoriBumonSentakuBtn);
	$("body").on("click","button[name=gyoumuRoleDel]",gyoumuRoleDel);

	//法人カード利用フラグによる項目の制御
	if ( ! $("input[name=houjinCardRiyouFlag]:checked").val() ) {
		$("input[name=houjinCardShikibetsuyouNum]").val("");
	}

	//chromeによる強制オートコンプリートの抑制用処理
	//※法人カード番号の入力可否制御の後で呼び出している
	delayEnable();

});

/**
 * イベントボタン押下時のアクションの切り替え
 */
 function eventBtn(eventName) {
	var formObject = document.getElementById("myForm");

	switch (eventName) {
	case 'update':
		if ($("#password").val() != $("#password2").val()) {
			alert("パスワードの入力が確認用の入力と不一致です。打ち直してください。");
			return;
		}
		formObject.action = 'user_jouhou_koushin';
		$(formObject).submit();
		break;
	case 'delete':
		formObject.action = 'user_jouhou_sakujo';
		if(window.confirm('削除してもよろしいですか？')) {
			$(formObject).submit();
		}
		break;
	}
}

/**
 * 法人カード利用フラグによる項目の制御
 */
 $("input[name=houjinCardRiyouFlag]").change(function() {
	if ($("input[name=houjinCardRiyouFlag]:checked").val()) {
		$("input[name=houjinCardShikibetsuyouNum]").prop("disabled", false);
	} else {
		$("input[name=houjinCardShikibetsuyouNum]").val("");
		$("input[name=houjinCardShikibetsuyouNum]").prop("disabled", true);
	}
 });
		</script>
	</body>
</html>
