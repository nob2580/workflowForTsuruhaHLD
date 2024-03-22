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

	<!-- 入力フィールド -->
	<section id='kensakuJouken'>
		<div>
			<form class="form-inline">
				<input type='hidden' name='hcDialogDenpyouId' value='${su:htmlEscape(denpyouId)}'>
				<input type='hidden' name='hcDialogUserId' value='${su:htmlEscape(userId)}'>
				<input type='hidden' name='hcDialogDenpyouKbn' value='${su:htmlEscape(denpyouKbn)}'>
				<input type='hidden' name='hcDialogSortKbn' value='${su:htmlEscape(sortKbn)}'>
				<input type='hidden' name='hcDialogDairiFlg' value='${su:htmlEscape(dairiFlg)}'>
				<!-- 検索条件 -->
				<div class='control-group'>
					<label class="label label-sub">対象カード</label>
						<select id='searchCardShubetsu' name='searchCardShubetsu' class='input-large'>
<c:forEach var="record" items="${cardList}" varStatus="st">
							<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq searchCardShubetsu}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
</c:forEach>
						</select>
				</div>
				<div class='control-group'>
					<label class="label label-sub">日付</label>
					<input type='text' name='riyouKikanFrom_dialog' class='input-small datepicker' value='${su:htmlEscape(joukenRiyouKikanFrom)}'> ～
					<input type='text' name='riyouKikanTo_dialog' class='input-small datepicker' value='${su:htmlEscape(joukenRiyouKikanTo)}'>
				</div>
				<div class='control-group'>
					<button type='button' id='rirekiSearchButton' class='btn' onClick="searchAction()">検索</button>
				</div>
			</form>
		</div>
	</section>

	<!-- 検索結果 -->
	<section id='icCardSearchResult'>
	    <div>
			<table class='table-bordered table-condensed'>
				<thead>
					<tr>
						<th style='width: 150px'></th>
						<th style='width: 100px'>カード会社</th>
						<th style='width: 100px'>利用日<br><a href="#" onClick="searchAction(1)">▲</a>    <a href="#" onClick="searchAction(2)">▼</a></th>
						<th style='width: 150px'>使用者</th>
						<th style='width: 150px'>金額</th>
						<th style='width: 150px'>加盟店</th>
					</tr>
				</thead>
				<c:forEach var="record" items="${list}">
					<tbody name='houjinCardRirekiBody'>
						<tr class='${record.bg_color}'>
							<td><nobr><input type='checkbox' name='sentaku'
									houjin-card-rireki-no	='${record.card_jouhou_id}'
									houjin-card-riyoubi		='${record.riyoubi}'
									kingaku					='${record.kingaku}'
									<c:if test='${record.isUsed eq true}'> disabled</c:if> >
									<select name='meisaiShubetsu' class='input-large' <c:if test='${record.isUsed eq true}'> disabled</c:if>>
<c:forEach var="shls" items="${meisaiShuList}" varStatus="st">
										<option value='${su:htmlEscape(shls.naibu_cd)}' <c:if test='${st.index eq 0}'>selected</c:if>>${su:htmlEscape(shls.name)}</option>
</c:forEach>
									</select>
							</nobr></td>
							<td><nobr>${su:htmlEscape(record.cardName)}</nobr></td>
							<td><nobr>${su:htmlEscape(record.riyoubi)}</nobr></td>
							<td><nobr>${su:htmlEscape(record.shiyousha)}</nobr></td>
							<td class='text-r'><nobr>${record.kingaku}</nobr></td>
							<td><nobr>${su:htmlEscape(record.kameiten)}</nobr></td>
						</tr>
					</tbody>
				</c:forEach>
			</table>
		</div>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
//入力補助
commonInit($("#dialogMain"));

/**
 * 同一画面で選択済みの履歴を灰色にし、要素選択不可とする
 */
$("tbody[name=houjinCardRirekiBody]").find("tr").each(function(){
	var houjinCardRirekiRow = $(this);
	var showHoujinRirekiNo = $("input[name=sentaku]",this).attr("houjin-card-rireki-no");
	
	//経費明細部
	$("#meisaiTableDiv .meisai").each(function(){
		var selectedHoujinRirekiNo = $("input:hidden[name=himodukeCardMeisaiKeihi]",this).val();
		if(showHoujinRirekiNo == selectedHoujinRirekiNo){
			rirekiGray(houjinCardRirekiRow);
		}
	});
	
	//旅費明細部(国内)
	$("#meisaiTableDiv01 .meisai").each(function(){
		var selectedHoujinRirekiNo = $("input:hidden[name=himodukeCardMeisaiRyohi]",this).val();
		if(showHoujinRirekiNo == selectedHoujinRirekiNo){
			rirekiGray(houjinCardRirekiRow);
		}
	});
	
	//旅費明細部(海外)
	$("#kaigaiMeisaiTableDiv01 .meisai").each(function(){
		var selectedHoujinRirekiNo = $("input:hidden[name=himodukeCardMeisaiRyohi]",this).val();
		if(showHoujinRirekiNo == selectedHoujinRirekiNo){
			rirekiGray(houjinCardRirekiRow);
		}
	});
	
	//宿泊費明細部(国内)
	$("#meisaiTableDiv02 .meisai").each(function(){
		var selectedHoujinRirekiNo = $("input:hidden[name=himodukeCardMeisaiRyohi]",this).val();
		if(showHoujinRirekiNo == selectedHoujinRirekiNo){
			rirekiGray(houjinCardRirekiRow);
		}
	});
	
	//宿泊費明細部(海外)
	$("#kaigaiMeisaiTableDiv02 .meisai").each(function(){
		var selectedHoujinRirekiNo = $("input:hidden[name=himodukeCardMeisaiRyohi]",this).val();
		if(showHoujinRirekiNo == selectedHoujinRirekiNo){
			rirekiGray(houjinCardRirekiRow);
		}
	});
	
});

function rirekiGray(houjinCardRirekiRow){
	houjinCardRirekiRow.hide();
	//houjinCardRirekiRow.addClass("disabled-bgcolor");
	//houjinCardRirekiRow.find("input[name=sentaku]").prop('disabled', true);
	//houjinCardRirekiRow.find("select[name=meisaiShubetsu]").prop('disabled', true);
}

/**
 * 検索処理
 * @param sort ソート区分
 */
 function searchAction(sort) {
	if(sort != null){
		$("input[name=hcDialogSortKbn]").val(sort);
	}
	var searchCardShubetsu =  $("select[name=searchCardShubetsu]").val();
	var joukenRiyouKikanFrom = $("input[name=riyouKikanFrom_dialog]").val();
	var joukenRiyouKikanTo = $("input[name=riyouKikanTo_dialog]").val();
	var hcDialogDenpyouId =  $("input[name=hcDialogDenpyouId]").val();
	var hcDialogUserId = $("input[name=hcDialogUserId]").val();
	var hcDialogDenpyouKbn = $("input[name=hcDialogDenpyouKbn]").val();
	var hcDialogSortKbn = $("input[name=hcDialogSortKbn]").val();
	var hcDialogDairiFlg = $("input[name=hcDialogDairiFlg]").val();
	
	$("#dialog").empty();
	$("#dialog").load("houjin_card_siyou_ichiran?joukenRiyouKikanFrom=" + encodeURI(joukenRiyouKikanFrom)
			+ "&joukenRiyouKikanTo=" + encodeURI(joukenRiyouKikanTo)
			+ "&searchCardShubetsu=" + encodeURI(searchCardShubetsu)
			+ "&denpyouId=" + encodeURI(hcDialogDenpyouId)
			+ "&userId=" + encodeURI(hcDialogUserId)
			+ "&denpyouKbn=" + encodeURI(hcDialogDenpyouKbn)
			+ "&sortKbn=" + encodeURI(hcDialogSortKbn)
			+ "&dairiFlg=" + encodeURI(hcDialogDairiFlg));
}

</script>
