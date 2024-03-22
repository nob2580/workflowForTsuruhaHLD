<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- エラー -->
<%@ include file="/jsp/eteam/include/InputError.jsp" %>
<!-- メイン -->
<div id='dialogMain'>
	<input type='hidden' name='isAllDate' value='${su:htmlEscape(isAllDate)}'>
	<section>
		<form class="form-inline">
			<div class='control-group'>
				<label class="label label-sub" for='d_bumonCd'>部門ｺｰﾄﾞ</label>
				<input type='text' id='d_bumonCd' name='d_bumonCd' class='input-small' value='${su:htmlEscape(d_bumonCd)}'>
				<label class="label label-sub" for='d_bumonName'>部門名</label>
				<input type='text' id='d_bumonName' name='d_bumonName' value='${su:htmlEscape(d_bumonName)}'>
			</div>
			<div class='control-group'>
				<label class="label label-sub" for='d_shainNo'>社員番号</label>
				<input type='text' id='d_shainNo' name='d_shainNo' class='input-medium' value='${su:htmlEscape(d_shainNo)}'>
				<label class="label label-sub" for='d_userName'>ユーザー名</label>
				<input type='text' id='d_userName' name='d_userName' value='${su:htmlEscape(d_userName)}'>
				<button type='button' id='d_userSearchButton' class='btn'>検索</button>
				<button type='button' id='d_clearButton' class='btn'>クリア</button>
			</div>
		</form>
	</section>
	
	<section>
		<div class='thumbnail' style="max-height:110px;overflow:auto;">
			<div id='bumon'>
	<c:forEach var="bumonrecord" items="${bumonList}">
				<p>${bumonrecord.prefix}<i class='icon-chevron-right' style="margin-top: -1px"></i><a class='bumonSentaku link' data-cd='${su:htmlEscape(bumonrecord.bumon_cd)}' data-name='${su:htmlEscape(bumonrecord.bumon_name)}' onclick='bumonClick(this)'> ${su:htmlEscape(bumonrecord.bumon_name)}（${bumonrecord.bumon_cd}）</a></p>
	</c:forEach>
			</div>
		</div>
		
		<div style='text-align: center;'>
			<img alt='↓' class='img_crystal_clear_narrow' src='/eteam/static/img/Crystal_Clear_action_2downarrow.png'/>
		</div>
		
		<div class='no-more-tables' style="max-height:110px;overflow:auto;">
			<table id='userTable' class='table-bordered table-condensed'>
				<thead>
					<tr>
						<th style="width:200px">所属部門</th>
						<th style="width:200px">役割</th>
						<th>社員番号</th>
						<th>ユーザー名</th>
					</tr>
				</thead>
				<tbody>
		<c:forEach var="record" items="${userList}">
					<tr class='${record.user_bg_color}'>
						<td style="width:200px">${su:htmlEscape(record.bumon_name)}</td>
						<td style="width:200px">${su:htmlEscape(record.bumon_role_name)}</td>
						<td>${su:htmlEscape(record.shain_no)}</td>
						<td>
							<a class='link user' data-userId='${su:htmlEscape(record.user_id)}' data-shainNo='${su:htmlEscape(record.shain_no)}' data-cardFlg='${su:htmlEscape(record.houjin_card_riyou_flag)}' data-userCardNum='${su:htmlEscape(record.houjin_card_shikibetsuyou_num)}' data-userFullName='${su:htmlEscape(record.user_sei)}　${su:htmlEscape(record.user_mei)}' data-bumonCode='${su:htmlEscape(record.bumon_cd)}' data-bumonName='${su:htmlEscape(record.bumon_name)}' data-bumonFullName='${su:htmlEscape(record.full_bumon_name)}' onclick="userClick(this)">${su:htmlEscape(record.user_sei)}　${su:htmlEscape(record.user_mei)}</a>
						</td>
					</tr>
		</c:forEach>
				</tbody>
			</table>
		</div>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>

/**
 * 検索
 */
function d_userSearch() {

	//部門枠の絞込
	var d_bumonCd = $("input[name=d_bumonCd]").val();
	var d_bumonName = $("input[name=d_bumonName]").val();
	$("#dialogMain").find("a.bumonSentaku").each(function(){
		var bumonP = $(this).closest("p");

		//非表示にしてからの
		bumonP.hide();
		//検索条件に該当するのだけ表示
		if (wordSearch($(this).attr("data-cd"), d_bumonCd) && wordSearch($(this).attr("data-name"), d_bumonName)) {
			bumonP.show();
		}
	});

	//表示中の部門コードリストをリスト化
	var variableBumonCdList = [];
	$("#dialogMain").find("a.bumonSentaku").each(function(){
		if ($(this).closest("p").is(":visible")) {
			variableBumonCdList.push($(this).attr("data-cd"));
		}
	});
	
	//ユーザー枠の絞込
	$("#dialogMain").find("a.user").each(function(){
		var userTr	= $(this).closest("tr");
		var cd	= $("input[name=d_shainNo]").val();
		var name= $("input[name=d_userName]").val();
		
		//非表示にしてからの
		userTr.hide();
		//表示中部門の所属である＆ユーザー検索条件に引っかかる ユーザーを表示
		if (
			variableBumonCdList.indexOf($(this).attr("data-bumonCode")) > -1
			&& wordSearch($(this).attr("data-shainNo"), cd)
			&& wordSearch($(this).text(), name)) {
			userTr.show();
		}
	});
}

/**
 * 部門選択時function
 * @param a 選択された部門リンク
 */
function bumonClick(a) {
	var bumonCd = $(a).attr("data-cd");
	var bumonName = $(a).attr("data-name");
	$("input[name=d_bumonCd]").val(bumonCd);
	$("input[name=d_bumonName]").val(bumonName);
	d_userSearch();
}

/**
 * ユーザー選択ダイアログ
 * @param a 選択されたユーザーリンク
 */

function userClick(a){

	var tr = $(a);
	
	var title = $("#dialog").parent().find(".ui-dialog-title").text();                         
	dialogRetshainNo.val(tr.attr("data-shainNo")); 
	if("dialogRetuserId" in window){
		dialogRetuserId.val(tr.attr("data-userId")); 
		commonShainNoLostFocus(dialogRetshainNo, dialogRetusername, title, dialogRetuserId, false);
	}else if("dialogRetCardNum" in window){
		commonShainNoLostFocus(dialogRetshainNo, dialogRetusername, title, false, dialogRetCardNum);
	}else{
		commonShainNoLostFocus(dialogRetshainNo, dialogRetusername, title, false);
	}
	if("dialogCallbackUserSentaku" in window) dialogCallbackUserSentaku(); 

	isDirty = true;
	$("#dialog").children().remove();
	$("#dialog").dialog("close"); 
}

//検索ボタン
$("#d_userSearchButton").click(function(){
	d_userSearch();
});

//検索ボタン
$("#d_clearButton").click(function(){
	$("#dialogMain").find("input[type=text]").val("");
	d_userSearch();
});
</script>
