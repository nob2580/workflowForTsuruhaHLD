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
		<title>代行者指定｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>代行者指定</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'>
					<div class='form-horizontal'>
						<div class='control-group'>
							<label class='control-label'><span class='required'>*</span>被代行者</label>
							<div class='controls'>
								<button type='button' class='btn btn-small' id='hiDaikouUserSentaku'<c:if test="${not IsKanri}" >disabled</c:if>>選択</button>
								<input type='text' name='hiDaikouUserName' class='input-xlarge' disabled value='${su:htmlEscape(hiDaikouUserName)}'>
							</div>
						</div>
					</div>
					<div id='daikou' class='row'>
						<div class='span5'>
							
							<!-- サブタイトル -->
							<h2>1.代行者の所属部門</h2>
							<div class='thumbnail'>
								<div id='bumon'>
<c:forEach var="record" items="${bumonList}">
									<p>${record.prefix}<i class='icon-chevron-right'></i> 
										<a href="javascript:kensakuForm('${su:htmlEscape(record.bumon_cd)}')" <c:if test='${record.bumon_cd eq choiceBumonCd}'>class='choice-current'</c:if>>
											${su:htmlEscape(record.bumon_name)}（${su:htmlEscape(record.bumon_cd)}）
										</a>
									</p>
</c:forEach>
								</div>
							</div>
							<div style='text-align: center;'>
								<img alt="" style='width: 40px; height: 40px;' src='/eteam/static/img/Crystal_Clear_action_2downarrow.png'/>
							</div>
							<!-- サブタイトル -->
							<h2>2.代行者選択　${su:htmlEscape(choiceBumonName)}</h2>
							<div class='no-more-tables'>
<c:if test="${fn:length(daikoushaList) == 0 && !empty choiceBumonCd}" >
								<p><b>　所属するユーザーは０件です。</b></p>
</c:if>
								<table id='userTable' class='table-bordered table-striped table-condensed'>
									<!--thead>
										<tr>
											<th>ユーザー</th>
											<th>社員番号</th>
											<th>所属部門</th>
											<th>役割</th>
										</tr>
									</thead-->
<c:forEach var="record" items="${daikoushaList}">
									<tbody>
										<tr>
											<td data-title='ユーザー'><a href='#' class='userAdd'>${su:htmlEscape(record.user_full_name)}</a>
												<input type='hidden' name='daikouUserId'         value='${su:htmlEscape(record.user_id)}'>
											</td>
											<td data-title='社員番号'>${su:htmlEscape(record.shain_no)}</td>
										<%-- <td data-title='所属部門'>${su:htmlEscape(record.bumon_full_name)}</td> --%>
											<td data-title='役割'>
												${su:htmlEscape(record.bumon_role_name)}
												<input type='hidden' name='userId'         value='${su:htmlEscape(record.user_id)}'>
												<input type='hidden' name='shainNo'        value='${su:htmlEscape(record.shain_no)}'>
												<input type='hidden' name='userFullName'   value='${su:htmlEscape(record.user_full_name)}'>
												<input type='hidden' name='bumonCode'      value='${su:htmlEscape(record.bumon_cd)}'>
												<input type='hidden' name='bumonFullName'  value='${su:htmlEscape(record.bumon_full_name)}'>
												<input type='hidden' name='bumonRoleId'    value='${su:htmlEscape(record.bumon_role_id)}'>
												<input type='hidden' name='bumonRoleName'  value='${su:htmlEscape(record.bumon_role_name)}'>
											</td>
										</tr>
									</tbody>
</c:forEach>
								</table>
							</div>
							<div style='text-align: center;'>
							<img alt="" style='width: 40px; height: 40px;' src='/eteam/static/img/Crystal_Clear_action_2rightarrow.png'/>
							</div>
						</div>
						<div class='span7'>
							<!-- サブタイトル -->
							<h2>3.代行者</h2>
							<form id='tourokuForm' method='post' action='daikousha_shitei_touroku' class='form-horizontal'>
								<input type='hidden' name='choiceBumonCd'  value='${su:htmlEscape(choiceBumonCd)}'>
								<input type='hidden' name='hiDaikouUserId' value='${su:htmlEscape(hiDaikouUserId)}'>
							<div class='empty'>
								<div class='no-more-tables empty'>
									<table id='daikouTable' class='table-bordered table-condensed'>
										<thead>
											<tr>
												<th>ユーザー</th>
												<th><nobr>社員番号</nobr></th>
												<th style="width:270px">所属部門</th>
												<th>役割</th>
												<th style = 'width: 55px;'></th>
											</tr>
										</thead>
										<tbody id='route'>
<c:forEach var="record" items="${tourokuzumiDaikoushaList}">
											<tr>
												<td data-title='ユーザー'>${su:htmlEscape(record.user_full_name)}
													<input type='hidden' name='daikouUserId'         value='${su:htmlEscape(record.daikou_user_id)}'>
												</td>
												<td data-title='社員番号'>${su:htmlEscape(record.shain_no)}</td>
												<td data-title='所属部門'>${su:htmlEscape(record.bumon_full_name)}</td>
												<td data-title='部門ロール'>
													${su:htmlEscape(record.bumon_role_name)}
													<input type='hidden' name='userId'         value='${su:htmlEscape(record.user_id)}'>
													<input type='hidden' name='shainNo'        value='${su:htmlEscape(record.shain_no)}'>
													<input type='hidden' name='userFullName'   value='${su:htmlEscape(record.user_full_name)}'>
													<input type='hidden' name='bumonCode'      value='${su:htmlEscape(record.bumon_cd)}'>
													<input type='hidden' name='bumonFullName'  value='${su:htmlEscape(record.bumon_full_name)}'>
													<input type='hidden' name='bumonRoleId'    value='${su:htmlEscape(record.bumon_role_id)}'>
													<input type='hidden' name='bumonRoleName'  value='${su:htmlEscape(record.bumon_role_name)}'>
												</td>
												<td>
													<button type='button' name='userDelete' class='btn btn-mini'><i class='icon-minus' style='margin-top:-1px;'></i> 削除</button>
												</td>
											</tr>
</c:forEach>
										</tbody>
									</table>
								</div>
								<div class='empty' style="margin-top: 10px">
									<button type="submit" class='btn'><i class='icon-hdd'></i> 登録</button>
								</div>
							</div>
							</form>
						</div>
					</div>
				</div><!-- main -->
				<!-- Modal -->
				<div id='dialog'></div>
				<div id='dialog2'></div>
			</div><!-- content -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッターー -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>

function userAdd() {
	var leftTr = $(this).closest("tr");
	var leftTds = leftTr.children();
	var shainNo = leftTr.find("input[name=shainNo]").val();
	var bumonName = leftTr.find("input[name=bumonFullName]").val();
	var tr = $("<tr><td>" + $(leftTds[0]).children().html() + 
			"<input type='hidden' name='daikouUserId' value='" + leftTr.find("input[name=daikouUserId]").val() + "'>" +
			"<\/td><td>" + shainNo + "<\/td><td>" + bumonName + "<\/td><td>" + $(leftTds[2]).html() + "<\/td><td> <button type='button' name='userDelete' class='btn btn-mini'><i class='icon-minus' style='margin-top:-1px;'></i> 削除<\/button><\/tr>'");
	$("#route").append(tr);
}
function userDelete() {

	var leftTr = $(this).closest("tr");
	var spanCnt = leftTr.find('td:first').attr('rowspan');
	var nextTr;
	// 結合していなければ該当行を削除
	if (spanCnt == null) {
		leftTr.remove();
	} else {
		// 結合していれば結合行をすべて削除
		for (var i = 0; i < spanCnt; i++) {
			nextTr = leftTr.next();
			leftTr.remove();
			leftTr = nextTr;
		}
	}
}

$(document).ready(function(){

	// 被代行者が選択されるまでは他の領域は非表示
	if($("input[name=hiDaikouUserId]").val().length == 0) $("#daikou").hide();
	
	// 被代行者選択ボタン押下時アクション
	$("#hiDaikouUserSentaku").click(function(){
		dialogRetHiDaikouUserId = $("input[name=hiDaikouUserId]");
		dialogRetHiDaikouUsername =  $("input[name=hiDaikouUserName]");
		commonHiDaikouUserSentaku();
		dialogCallbackHiDaikouUserSentaku = function(){
			$("#daikou").show();
			var hidaikou = $("input[name=hiDaikouUserId]").val();
			$(location).attr("href", "daikousha_shitei?hiDaikouUserId=" + encodeURI(hidaikou));
		};
	});
	
	//ユーザー名リンククリック時アクション
	$("a.userAdd").click(userAdd);

	//部門選択
	$("#bumon a").click(function(e){
		displayOnOff($("#userTable tbody"), $("#" + $(this).attr("data-bumon")));
		choice($("#bumon a"), $(this));
	});

	//ユーザ-ボタン押下時アクション
	$("body").on("click","button[name=userDelete]",userDelete); 

	$('#daikouTable').each(function () {
		 var pre_element_first = null;
		 var pre_element_last = null;
		 var col_num = 0;
		 $(this).find('tr').each(function () {
			var nowTdFirst = $(this).find('td:first').eq(col_num);
			var nowTdLast = $(this).find('td:last').eq(col_num);
			// 最初のTD要素を設定
			if (pre_element_first == null) {
				pre_element_first = nowTdFirst;
				pre_element_last = nowTdLast;
			// 前回と今回のTD要素を比較
			} else if (nowTdFirst.text() == pre_element_first.text()) {
				
				// 今回のTD要素削除
				nowTdFirst.remove();
				nowTdLast.remove();
				
				if (pre_element_first.attr('rowspan') == null) {
					pre_element_first.attr('rowspan', 1);
					pre_element_last.attr('rowspan', 1);
				}
				pre_element_first.attr('rowspan', parseInt(pre_element_first.attr('rowspan'), 10) + 1);
				pre_element_last.attr('rowspan', parseInt(pre_element_last.attr('rowspan'), 10) + 1);
			} else {
				pre_element_first = nowTdFirst;
				pre_element_last = nowTdLast;
			}
		});
	});

});

/**
 * 代行者検索イベント
 */
function kensakuForm(bumonCd) {
	$('input[name=choiceBumonCd]').val(bumonCd);
	var form = document.getElementById("tourokuForm");
	form.action = 'daikousha_shitei_kensaku';
	$(form).submit();
}
		</script>
	</body>
</html>
