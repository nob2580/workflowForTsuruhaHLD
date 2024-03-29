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
		<title>承認ルート登録｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>承認ルート登録</h1>
				
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'>
					<div class='row'>
						<div class='span5'>
							<h2>1.ユーザーの所属部門</h2>
							<div class='thumbnail'>
								<div id='bumon'>
								
<c:forEach var="record" items="${bumonList}">
									<p>${record.prefix}<i class='icon-chevron-right'></i> 
										<a href="javascript:kensaku('${su:htmlEscape(record.bumon_cd)}')" <c:if test='${record.bumon_cd eq choiceBumonCd}'>class='choice-current'</c:if>>
											${su:htmlEscape(record.bumon_name)}（${su:htmlEscape(record.bumon_cd)}）
										</a>
									</p>
</c:forEach>
									<p><i class='icon-chevron-right'></i>
										<a href="javascript:kensaku('gyoumuRole')" <c:if test='${"gyoumuRole" eq choiceBumonCd}'>class='choice-current'</c:if>>
											業務ロール
										</a>
									</p>
									<p><i class='icon-chevron-right'></i>
										<a href="javascript:kensaku('gougiBusho')" <c:if test='${"gougiBusho" eq choiceBumonCd}'>class='choice-current'</c:if>>
											合議部署
										</a>
									</p>
								</div>
							</div>
							<div style='text-align: center;'>
								<img alt="" style='width: 40px; height: 40px;' src='/eteam/static/img/Crystal_Clear_action_2downarrow.png'/>
							</div>
							
							<h2>2.承認者選択</h2>
							<div class='no-more-tables'>
<c:if test="${fn:length(shouninshaList) == 0 && !empty choiceBumonCd}" >
								<p><b>　所属するユーザーは０件です。</b></p>
</c:if>
								<table id='userTable' class='table-bordered table-striped table-condensed'>
									<!--thead>
										<tr>
											<th>ユーザー</th>
											<th>所属部門</th>
											<th>役割</th>
										</tr>
									</thead-->
<c:forEach var="record" items="${shouninshaList}">
									<tbody>
										<tr>
<c:choose>
	<c:when test="${not empty record.gougi_name}">
											
											<td data-title='合議'>${su:htmlEscape(record.gougi_name)}</td>
										<%-- <td data-title='所属部門'>${su:htmlEscape(record.bumon_full_name)}</td> --%>
											<td data-title='合議部署'>
												<input type='checkbox' name='gougiAddCheck' value='1'>
												<input type='hidden' name='addGougiPatternNo'  value='${su:htmlEscape(record.gougi_pattern_no)}'>
												<input type='hidden' name='addGougiName'   value='${su:htmlEscape(record.gougi_name)}'>
											</td>
	</c:when>

	<c:when test="${empty record.gyoumu_role_name && empty record.gougi_name}">
											
											<td data-title='ユーザー'><a href='#' class='userAdd'>${su:htmlEscape(record.user_full_name)}</a></td>
										<%-- <td data-title='所属部門'>${su:htmlEscape(record.bumon_full_name)}</td> --%>
											<td data-title='役割'>
												${su:htmlEscape(record.bumon_role_name)}
												<input type='hidden' name='userId'         value='${su:htmlEscape(record.user_id)}'>
												<input type='hidden' name='userFullName'   value='${su:htmlEscape(record.user_full_name)}'>
												<input type='hidden' name='bumonCode'      value='${su:htmlEscape(record.bumon_cd)}'>
												<input type='hidden' name='bumonFullName'  value='${su:htmlEscape(record.bumon_full_name)}'>
												<input type='hidden' name='bumonRoleId'    value='${su:htmlEscape(record.bumon_role_id)}'>
												<input type='hidden' name='bumonRoleName'  value='${su:htmlEscape(record.bumon_role_name)}'>
												<input type='hidden' name='gyoumuRoleId'   value='${su:htmlEscape(record.gyoumu_role_id)}'>
												<input type='hidden' name='gyoumuRoleName' value='${su:htmlEscape(record.gyoumu_role_name)}'>
												<input type='hidden' name='routeEdaedano'  value=''>
												<input type='hidden' name='gougiPatternNo'  value=''>
												<input type='hidden' name='gougiEdano'   value=''>
												<input type='hidden' name='gougiName'   value=''>
												<input type='hidden' name='shouninNinzuuCd'        value=''>
												<input type='hidden' name='shouninNinzuuHiritsu'   value=''>
												<input type='hidden' name='gouginaiGroup'   value=''>
												
												<input type='hidden' name='genzaiUsrShouninKengen'   value=''>
												
												<input type='hidden' name='addedGougi'     value=''>
												<input type='hidden' name='addedUser'     value='1'>
												<input type='hidden' name='isGenzaiShouninsha'     value=''>
												<input type='hidden' name='gougiJoukyouEdano'    value=''>
												<input type='hidden' name='kengenDisableFlg'     value=''>
												
											</td>
	</c:when>
	<c:otherwise>
											<td data-title='役割'>
												<a href='#' class='roleAdd'>
													${su:htmlEscape(record.gyoumu_role_name)}
													<input type='hidden' name='userId'         value='${su:htmlEscape(record.user_id)}'>
													<input type='hidden' name='userFullName'   value='${su:htmlEscape(record.user_full_name)}'>
													<input type='hidden' name='bumonCode'      value='${su:htmlEscape(record.bumon_cd)}'>
													<input type='hidden' name='bumonFullName'  value='${su:htmlEscape(record.bumon_full_name)}'>
													<input type='hidden' name='bumonRoleId'    value='${su:htmlEscape(record.bumon_role_id)}'>
													<input type='hidden' name='bumonRoleName'  value='${su:htmlEscape(record.bumon_role_name)}'>
													<input type='hidden' name='gyoumuRoleId'   value='${su:htmlEscape(record.gyoumu_role_id)}'>
													<input type='hidden' name='gyoumuRoleName' value='${su:htmlEscape(record.gyoumu_role_name)}'>
													<input type='hidden' name='routeEdaedano'  value=''>
													<input type='hidden' name='gougiPatternNo'  value=''>
													<input type='hidden' name='gougiEdano'   value=''>
													<input type='hidden' name='gougiName'   value=''>
													<input type='hidden' name='shouninNinzuuCd'        value=''>
													<input type='hidden' name='shouninNinzuuHiritsu'   value=''>
													<input type='hidden' name='gouginaiGroup'   value=''>
													
													<input type='hidden' name='genzaiUsrShouninKengen'   value=''>
													
													<input type='hidden' name='addedGougi'     value=''>
													<input type='hidden' name='addedUser'     value='1'>
													<input type='hidden' name='isGenzaiShouninsha'     value=''>
													<input type='hidden' name='gougiJoukyouEdano'    value=''>
													<input type='hidden' name='kengenDisableFlg'     value=''>
													
												</a>
											</td>
	</c:otherwise>
</c:choose>
										</tr>
									</tbody>
</c:forEach>
								</table>
							</div>

<c:if test='${"gougiBusho" eq choiceBumonCd}'>
	<br>
	<button type="button" class='btn' onclick='gougiAdd()' <c:if test="${shouninRouteHenkouLevel lt 1}">disabled</c:if>><i class='icon-forward'></i> 合議追加</button>
</c:if>

							<div style='text-align: center;'>
							<img alt="" style='width: 40px; height: 40px;' src='/eteam/static/img/Crystal_Clear_action_2rightarrow.png'/>
							</div>
						</div>
						<div class='span7'>
							<h2>3.承認ルート確認</h2>
							<div class='alert alert-alert empty' style="word-wrap:break-word; word-break:break-all;">
							<p>${su:htmlEscapeBrLink(chuukiMongon)}</p>
							</div>
							<div>
<c:if test="${loginUserKbu == 1}" >
								<button type="button" class='btn' onclick='defaultBack()'><i class='icon-fast-backward'></i> デフォルトに戻す</button>
	<c:if test="${!empty sanshouDenpyouId}" >
								<button type="button" class='btn' onclick='zenkaiRouteBack()'><i class='icon-fast-backward'></i> 前回ルートに戻す</button>
	</c:if>
</c:if>
							</div>
							
							<div class='blank'></div>
							
							<form id='myForm' method='post' action='' class='form-horizontal'>
								<input type='hidden' name='denpyouId'      value='${su:htmlEscape(denpyouId)}'>
								<input type='hidden' name='denpyouKbn'     value='${su:htmlEscape(denpyouKbn)}'>
								<input type='hidden' name='version'        value='${su:htmlEscape(version)}'>
								<input type='hidden' name='shinseiKingaku' value='${su:htmlEscape(shinseiKingaku)}'>
								<input type='hidden' name='kijunbi'        value='${su:htmlEscape(kijunbi)}'>
								
								<input type='hidden' name='choiceBumonCd'      value='${su:htmlEscape(choiceBumonCd)}'>
								<input type='hidden' name='addGougiBushoList'  value=''>
							
							<div class='empty'>
								<div class='no-more-tables empty'>
									<table class='table-bordered table-condensed'>
										<thead>
											<tr>
												<th>ユーザー</th>
												<th style="width:300px">所属部門</th>
												<th>役割</th>
												<th>承認権限</th>
												<th></th>
											</tr>
										</thead>
										<tbody id='enabled'>
<c:forEach var="record" items="${shouninRouteTourokuList}">
<c:if test="${record.gougiLine ne 0}" >
											<tr class ="gougiStart <c:if test='${record.saishuuShouninFlg eq 1}' >routeSaishuuShounin</c:if>">
												<td rowspan = '${record.gougiLine}'><font color='red'><b>合議</b></font>
<c:if test="${record.kengenDisableFlg ne 1 && record.isGenzaiShouninsha ne 1}" >
													<br>
													<button type='button' name='gougiDelete' class='btn btn-mini' <c:if test="${(shouninRouteHenkouLevel le 2 && record.addedGougi ne 1) || (shouninRouteHenkouLevel eq 3 && record.saishuuShouninFlg eq 1)}">disabled</c:if>>-</button>
													<button type='button' name='gougiUp'     class='btn btn-mini' <c:if test="${(shouninRouteHenkouLevel le 2 && record.addedGougi ne 1) || (shouninRouteHenkouLevel eq 3 && record.saishuuShouninFlg eq 1)}">disabled</c:if>>↑</button>
													<button type='button' name='gougiDown'   class='btn btn-mini' <c:if test="${(shouninRouteHenkouLevel le 2 && record.addedGougi ne 1) || (shouninRouteHenkouLevel eq 3 && record.saishuuShouninFlg eq 1)}">disabled</c:if>>↓</button>
</c:if>
												</td>
												<td colspan="4" style="background-color:#FFCCCC">${record.gougi_name}</td>
											</tr>

</c:if>
<c:if test="${record.gougiLine eq 0 && record.gougiChangeFlg eq 1}" >
											<tr>
												<td colspan="4" style="background-color:#FFCCCC">${record.gougi_name}</td>
											</tr>
</c:if>
											<tr class="<c:if test='${record.gougiStartFlg eq 1 && record.gougiLine eq 0}' >gougiStart </c:if><c:if test='${record.gougiEndFlg eq 1}' >gougiLast </c:if><c:if test='${record.saishuuShouninFlg eq 1 && record.gougiLine eq 0}' >routeSaishuuShounin </c:if> <c:if test='${record.isGenzaiShouninsha eq 1}' >wait-period-bgcolor genzaiShouninsha </c:if>">
<c:choose>
	<c:when test="${empty record.gyoumu_role_name}">
												<td data-title='ユーザー'>${su:htmlEscape(record.user_full_name)}</td>
<c:if test="${record.gougiInnerFlg ne 1}" >
												<td data-title='所属部門'>${su:htmlEscape(record.bumon_full_name)}</td>
</c:if>												
												<td data-title='部門ロール'>${su:htmlEscape(record.bumon_role_name)}</td>
	</c:when>
	<c:otherwise>
												<td></td>
<c:if test="${record.gougiInnerFlg ne 1}" >
												<td></td>
</c:if>												
												<td data-title='業務ロール'>${su:htmlEscape(record.gyoumu_role_name)}</td>
	</c:otherwise>
</c:choose>
<c:if test="${record.addEnabled}" >
												<td>
													<select name='shoriKengenNo' class='input-small' <c:if test="${shouninRouteHenkouLevel lt 2 && record.addedUser ne 1}">disabled</c:if>>
														<c:forEach var="shoriKengenRecord" items="${shoriKengenList}">
	<c:if test="${ empty record.genzaiUsrShouninKengen || shoriKengenRecord.kihon_model_cd eq 3 || shoriKengenRecord.shounin_shori_kengen_no eq record.genzaiUsrShouninKengen }">
															<option value='${shoriKengenRecord.shounin_shori_kengen_no}' <c:if test='${shoriKengenRecord.shounin_shori_kengen_no eq record.shounin_shori_kengen_no}'>selected</c:if>>${su:htmlEscape(shoriKengenRecord.shounin_shori_kengen_name)}</option>
	</c:if>
														</c:forEach>
													</select>
													<input type='hidden' name='shoriKengenName' value=''>
												</td>
</c:if>
<c:if test="${record.gougiInnerFlg eq 1}" >
												<td>
													<select name='shoriKengenNo' class='input-small' <c:if test="${shouninRouteHenkouLevel lt 2 || not empty record.gougiJoukyouEdano || record.kengenDisableFlg eq 1 }">disabled</c:if>>
														<c:forEach var="shoriKengenRecord" items="${shoriKengenGougiList}">
	<c:if test="${ empty record.genzaiUsrShouninKengen || shoriKengenRecord.kihon_model_cd eq 3 || shoriKengenRecord.shounin_shori_kengen_no eq record.genzaiUsrShouninKengen }">
															<option value='${shoriKengenRecord.shounin_shori_kengen_no}' <c:if test='${shoriKengenRecord.shounin_shori_kengen_no eq record.shounin_shori_kengen_no}'>selected</c:if>>${su:htmlEscape(shoriKengenRecord.shounin_shori_kengen_name)}</option>
	</c:if>
														</c:forEach>
													</select>
													<input type='hidden' name='shoriKengenName' value=''>
												</td>
</c:if>
<c:if test="${!(record.addEnabled) && (record.gougiInnerFlg ne 1)}" >
												<td>${su:htmlEscape(record.shounin_shori_kengen_name)}
													<input type='hidden' name='shoriKengenNo' value=''>
													<input type='hidden' name='shoriKengenName' value='${su:htmlEscape(record.shounin_shori_kengen_name)}'>
												</td>
</c:if>
												<td style="width:80px">
													<input type='hidden' name='addEnabled'     value='${record.addEnabled}'>
													<input type='hidden' name='userId'         value='${su:htmlEscape(record.user_id)}'>
													<input type='hidden' name='userFullName'   value='${su:htmlEscape(record.user_full_name)}'>
													<input type='hidden' name='bumonCode'      value='${su:htmlEscape(record.bumon_cd)}'>
													<input type='hidden' name='bumonFullName'  value='${su:htmlEscape(record.bumon_full_name)}'>
													<input type='hidden' name='bumonRoleId'    value='${su:htmlEscape(record.bumon_role_id)}'>
													<input type='hidden' name='bumonRoleName'  value='${su:htmlEscape(record.bumon_role_name)}'>
													<input type='hidden' name='gyoumuRoleId'   value='${su:htmlEscape(record.gyoumu_role_id)}'>
													<input type='hidden' name='gyoumuRoleName' value='${su:htmlEscape(record.gyoumu_role_name)}'>
													
													
													<input type='hidden' name='routeEdaedano'  value='${su:htmlEscape(record.route_edaedano)}'>
													<input type='hidden' name='gougiPatternNo'  value='${su:htmlEscape(record.gougi_pattern_no)}'>
													<input type='hidden' name='gougiEdano'   value='${su:htmlEscape(record.gougi_edano)}'>
													<input type='hidden' name='gougiName'   value='${su:htmlEscape(record.gougi_name)}'>
													<input type='hidden' name='shouninNinzuuCd'        value='${su:htmlEscape(record.shounin_ninzuu_cd)}'>
													<input type='hidden' name='shouninNinzuuHiritsu'   value='${su:htmlEscape(record.shounin_ninzuu_hiritsu)}'>
													<input type='hidden' name='gouginaiGroup'   value='${su:htmlEscape(record.gouginai_group)}'>
													
													<input type='hidden' name='genzaiUsrShouninKengen'   value='${su:htmlEscape(record.genzaiUsrShouninKengen)}'>
													
													<input type='hidden' name='addedGougi'     value='${record.addedGougi}'>
													<input type='hidden' name='addedUser'     value='${record.addedUser}'>
													<input type='hidden' name='isGenzaiShouninsha'     value='${record.isGenzaiShouninsha}'>
													<input type='hidden' name='gougiJoukyouEdano'    value='${record.gougiJoukyouEdano}'>
													<input type='hidden' name='kengenDisableFlg'     value='${record.kengenDisableFlg}'>
<c:if test="${record.addEnabled && record.isGenzaiShouninsha ne 1}" >
													<button type='button' name='userDelete' class='btn btn-mini' <c:if test="${(shouninRouteHenkouLevel le 2 && record.addedUser ne 1) || (shouninRouteHenkouLevel eq 3 && record.saishuuShouninFlg eq 1)}">disabled</c:if>>-</button>
													<button type='button' name='userUp'     class='btn btn-mini' <c:if test="${(shouninRouteHenkouLevel le 2 && record.addedUser ne 1) || (shouninRouteHenkouLevel eq 3 && record.saishuuShouninFlg eq 1)}">disabled</c:if>>↑</button>
													<button type='button' name='userDown'   class='btn btn-mini' <c:if test="${(shouninRouteHenkouLevel le 2 && record.addedUser ne 1) || (shouninRouteHenkouLevel eq 3 && record.saishuuShouninFlg eq 1)}">disabled</c:if>>↓</button>
</c:if>
												</td>
											</tr>
</c:forEach>
										</tbody>
										<tbody id='saishuShouninsha'>
<c:forEach var="record" items="${saishuuShouninshaList}">
											<tr>
												<td></td>
												<td></td>
												<td>${su:htmlEscape(record.gyoumu_role_name)}</td>
												<td>${su:htmlEscape(record.saishuu_shounin_shori_kengen_name)}</td>
												<td></td>
											</tr>
</c:forEach>
										</tbody>
									</table>
								</div>
								
								<div class='blank'>
<c:if test="${loginUserKbu != 3}" >
									<button type="button" class='btn' onclick='touroku()'><i class='icon-hdd'></i> 登録</button>
</c:if>
									<button type="button" class='btn' onclick='historyBack()'><i class='icon-step-backward'></i> 戻る</button>
								</div>
							</div>
							</form>
						</div>
					</div>
				</div><!-- main -->
			</div><!-- content -->
			<div id="push"></div>
			<div id="shouninSelect" style='display:none;'>
					<select name='shoriKengenNo' class='input-small'>
						<c:forEach var="shoriKengenRecord" items="${shoriKengenList}">
							<option value='${shoriKengenRecord.shounin_shori_kengen_no}'>${su:htmlEscape(shoriKengenRecord.shounin_shori_kengen_name)}</option>
						</c:forEach>
					</select>
					<input type='hidden' name='shoriKengenName' value=''>
					<input type='hidden' name='shouninRouteHenkouLevel' value='${su:htmlEscape(shouninRouteHenkouLevel)}'>
			</div>
			</div><!-- wrap -->
			
			<!-- フッター -->
			<%@ include file="/jsp/eteam/include/Footer.jsp" %>
			
		
		
		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>

/**
 * 所属部門のユーザーを追加する。
 */
function userAdd() {
	var level = $("input[name=shouninRouteHenkouLevel]").val();
	if( level < 1 ){
		return;
	}
	
	var leftTr = $(this).closest("tr");
	var leftTds = leftTr.children();
	var shouninSelect = $("#shouninSelect");
	var bumonName = leftTr.find("input[name=bumonFullName]").val(); 
	var tr = $("<tr class='gougiStart gougiLast'><input type='hidden' name='addEnabled' value='true'><td>" + $(leftTds[0]).children().html() + "<\/td><td>" + bumonName + "<\/td><td>" + $(leftTds[1]).html() + "<\/td><td>" + $(shouninSelect).html() + "<\/td><td> <button type='button' name='userDelete' class='btn btn-mini'>-<\/button> <button type='button' name='userUp' class='btn btn-mini'>↑<\/button> <button type='button' name='userDown' class='btn btn-mini'>↓<\/button><\/tr>'");
	
	//基本は部門推奨ルートの最終承認ユーザの上に追加
	//ただし現在フラグが最終行、または最終行内部の合議にある場合は最終行の下に追加
	var addPosFlg = false;
	if($(".routeSaishuuShounin").length){
		var trnext = $(".routeSaishuuShounin");
		if(trnext.hasClass("genzaiShouninsha")){
			addPosFlg = true;
		}else{
			while(trnext.next().size()){
				trnext = trnext.next();
				if(trnext.hasClass("genzaiShouninsha")){
					addPosFlg = true;
					break;
				}
				if(trnext.hasClass("gougiLast")){
					break;
				}
			}
		}
	}else{
		addPosFlg = true;
	}
	if(addPosFlg == true){
		$("#enabled").append(tr);
	}else{
		$(".routeSaishuuShounin").before(tr);
	}
}

/**
 * 業務ロールを追加する。
 */
function roleAdd() {
	var level = $("input[name=shouninRouteHenkouLevel]").val();
	if( level < 1 ){
		return;
	}
	
	var leftTr = $(this).closest("tr");
	var leftTds = leftTr.children();
	var shouninSelect = $("#shouninSelect");
	var tr = $("<tr class='gougiStart gougiLast'><input type='hidden' name='addEnabled' value='true'><td><\/td><td><\/td><td>" + $(leftTds[0]).children().html() + "<\/td><td>" + $(shouninSelect).html() + "<\/td><td> <button type='button' name='userDelete' class='btn btn-mini'>-<\/button> <button type='button' name='userUp' class='btn btn-mini'>↑<\/button> <button type='button' name='userDown' class='btn btn-mini'>↓<\/button><\/tr>'");
	
	//基本は部門推奨ルートの最終承認ユーザの上に追加
	//ただし現在フラグが最終行、または最終行内部の合議にある場合は最終行の下に追加
	var addPosFlg = false;
	if($(".routeSaishuuShounin").length){
		var trnext = $(".routeSaishuuShounin");
		if(trnext.hasClass("genzaiShouninsha")){
			addPosFlg = true;
		}else{
			while(trnext.next().size()){
				trnext = trnext.next();
				if(trnext.hasClass("genzaiShouninsha")){
					addPosFlg = true;
					break;
				}
				if(trnext.hasClass("gougiLast")){
					break;
				}
			}
		}
	}else{
		addPosFlg = true;
	}
	if(addPosFlg == true){
		$("#enabled").append(tr);
	}else{
		$(".routeSaishuuShounin").before(tr);
	}
}


/**
 * 合議部署を追加する。
 */
function gougiAdd() {
	
	var level = $("input[name=shouninRouteHenkouLevel]").val();
	if( level < 1 ){
		return;
	}
	
	//合議部署が最低1つ選択されているかチェック
	var chkCnt = 0;
	var tmpText = "";
	
	$("input[name=gougiAddCheck]").each(function(){
		if($(this).prop("checked")){
			chkCnt++;
			var addText = $(this).parent().find("input[name=addGougiPatternNo]").val()
			addText = addText + ","
			tmpText = tmpText + addText;
		}
	})
	
	
	//TODO 現在フラグが最終列、または最終列内部の合議にある場合は最終列の下に追加
	
	if(chkCnt == 0){
		alert("合議部署を指定してください。")
		return;
	}
	tmpText = tmpText.substring(0, (tmpText.length - 1));
	$("input[name=addGougiBushoList]").val(tmpText);
	
	
	//合議部署はjsp側でデータを処理させるのは難しいため、actionとして追加させる
	var form = document.getElementById("myForm");
	form.action = 'shounin_route_tsuika_gougi_busho';
	$(form).submit();
}


/**
 * ユーザーを削除する。
 */
function userDelete() {
	$(this).closest("tr").remove();
}

/**
 * ユーザーを上へ移動する。
 */
function userUp() {
	var tr = $(this).closest("tr");
	var trprev = tr;
	while(trprev.prev().size()){
		trprev = trprev.prev();
		//現在承認者より上には移動させない
		if ( trprev.hasClass("genzaiShouninsha") ){
			break;
		}
		if (  trprev.hasClass("gougiStart") ) {
			tr.insertBefore(trprev);
			break;
		}
	}
}

/**
 * ユーザーを下へ移動する。
 */
function userDown() {
	var tr = $(this).closest("tr");
	var trnext = tr;
	var level = $("input[name=shouninRouteHenkouLevel]").val();
	while(trnext.next().size()){
		trnext = trnext.next();
		//権限レベルが4以上でなければ推奨ルート内最終承認者の下には移動不可
		if( trnext.hasClass("routeSaishuuShounin") && (level < 4) ){
			break;
		}
		if( trnext.hasClass("gougiLast") ){
			tr.insertAfter(trnext);
			break;
		}
	}
}

/**
 * 合議を削除する。
 */
function gougiDelete() {
	
	//該当合議内のtrを範囲で取得してまとめて削除

	var tr = $(this).closest("tr");
	var trnext = tr;
	while(trnext.next().size()){
		tr = trnext;
		trnext = trnext.next();
		tr.remove();
		if( trnext.hasClass("gougiLast") ){
			trnext.remove();
			break;
		}
	}
}

/**
 * 合議を上へ移動する。
 */
function gougiUp() {
	
	var tr = $(this).closest("tr");
	var trprev = tr;
	var trmovePos;
	
	//移動対象位置を取得
	while(trprev.prev().size()){
		trprev = trprev.prev();
		//現在承認者より上には移動させない
		if ( trprev.hasClass("genzaiShouninsha") ){
			break;
		}
		if (  trprev.hasClass("gougiStart") ) {
			trmovePos = trprev;
			break;
		}
	}
	
	//対象位置の一つ上に順次移動
	if(trmovePos != null){
		tr = $(this).closest("tr");
		var trnext = tr;
		while(trnext.next().size()){
			tr = trnext;
			trnext = trnext.next();
			tr.insertBefore(trmovePos);
			if( trnext.hasClass("gougiLast") ){
				trnext.insertBefore(trmovePos);
				break;
			}
		}
	}

}

/**
 * 合議を下へ移動する。
 */
function gougiDown() {
	
	var tr = $(this).closest("tr");
	tr.addClass("gougiMoveStart");
	var trnext = tr;
	var trmovePos;
	var trgougilastPos;
	var level = $("input[name=shouninRouteHenkouLevel]").val();
	
	//移動対象位置を取得
	//2番目に出現するgougiLastを探す(最初のgougiLastは移動する合議内のもの)
	while(trnext.next().size()){
		trnext = trnext.next();
		//権限レベルが4以上でなければ推奨ルート内最終承認者の下には移動不可
		if( trnext.hasClass("routeSaishuuShounin") && (level < 4) ){
			break;
		}
		if( trnext.hasClass("gougiLast") ){
			if(trgougilastPos == null){
				trgougilastPos = trnext;
				continue;
			}
			trmovePos = trnext;
			break;
		}
	}

	//対象位置の一つ下に移動対象合議の下から順次移動
	if(trmovePos != null){
		tr = trgougilastPos;
		var trprev = tr;
		while(trprev.prev().size()){
			tr = trprev;
			trprev = trprev.prev();
			tr.insertAfter(trmovePos);
			if( trprev.hasClass("gougiMoveStart") ){
				trprev.insertAfter(trmovePos);
				break;
			}
		}
	}
	$("tr").removeClass("gougiMoveStart");
	
}

/**
 * 承認者検索イベント
 */
function kensaku(bumonCd) {
	$('input[name=choiceBumonCd]').val(bumonCd);
	var form = $("#myForm");
	form.attr("action", "shounin_route_touroku_shouninsha_kensaku");
	$(form).submit();
}

/**
 * 登録イベント
 */
function touroku(){
	var form = $("#myForm");
	form.attr("action", "shounin_route_touroku_touroku");
	$(form).submit();
}

/**
 * 戻るボタンイベント
 */
function historyBack(){
	var url = "${urlPath}" + "?denpyouId=" + $("input[name=denpyouId]").val() + "&denpyouKbn=" + $("input[name=denpyouKbn]").val();
	if($("input[name=version]").val() != "" && $("input[name=version]").val() != "0") url = url + "&version=" + $("input[name=version]").val();
	location.href = url;
}

/**
 * デフォルトに戻るイベント
 */
function defaultBack() {
	var form = $("#myForm");
	form.attr("action", "shounin_route_touroku_default_back");
	$(form).submit();
}

/**
 * 前回ルートに戻るイベント
 */
function zenkaiRouteBack() {
	var form = $("#myForm");
	form.attr("action", "shounin_route_touroku_zenkai_route_back");
	$(form).submit();
}

$(document).ready(function(){

	//ユーザー名リンククリック時アクション
	$("a.userAdd").click(userAdd);
	$("a.roleAdd").click(roleAdd);

	//部門選択
	$("#bumon a").click(function(e){
		displayOnOff($("#userTable tbody"), $("#" + $(this).attr("data-bumon")));
		choice($("#bumon a"), $(this));
	});

	// ユーザー関連アクション
	$("body").on("click","button[name=userDelete]",userDelete); 
	$("body").on("click","button[name=userUp]",userUp); 
	$("body").on("click","button[name=userDown]",userDown); 
	// 合議関連アクション
	$("body").on("click","button[name=gougiDelete]",gougiDelete); 
	$("body").on("click","button[name=gougiUp]",gougiUp); 
	$("body").on("click","button[name=gougiDown]",gougiDown); 
	
});
		</script>
	</body>
</html>
