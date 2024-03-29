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
	<section id='ICKensakuJouken'>
		<div>
			<form class="form-inline">
				<!-- 検索条件 -->
				<input type='hidden' name='denpyouId' value='${su:htmlEscape(denpyouId)}'>
				<input type='hidden' name='userId' value='${su:htmlEscape(userId)}'>
				<input type='hidden' name='sortKbn' value='${su:htmlEscape(sortKbn)}'>
				<input type='hidden' name='jyogaiDataHyoujiFlg' value='${su:htmlEscape(jyogaiDataHyoujiFlg)}'>
				<input type='hidden' name='jyogaiChangeList' value=''>
				<input type='hidden' name='jyogaiChangeFlg' value=''>
				<div class='control-group'>
					<label class="label label-sub">利用日</label>
					<input type='text' name='joukenRiyouKikanFrom' class='input-small datepicker' value='${su:htmlEscape(joukenRiyouKikanFrom)}'> ～
					<input type='text' name='joukenRiyouKikanTo' class='input-small datepicker' value='${su:htmlEscape(joukenRiyouKikanTo)}'>
				</div>
				<div class='control-group'>
					<label class="label label-sub">出発路線名</label>
					<input type='text' name='joukenLineNameFrom' class='input-medium' value='${su:htmlEscape(joukenLineNameFrom)}'>
					<label class="label label-sub">出発駅名</label>
					<input type='text' name='joukenEkiNameFrom' class='input-medium' value='${su:htmlEscape(joukenEkiNameFrom)}'>
				</div>
				<div class='control-group'>
					<label class="label label-sub">到着路線名</label>
					<input type='text' name='joukenLineNameTo' class='input-medium' value='${su:htmlEscape(joukenLineNameTo)}'>
					<label class="label label-sub">到着駅名</label>
					<input type='text' name='joukenEkiNameTo' class='input-medium' value='${su:htmlEscape(joukenEkiNameTo)}'>
					<button type='button' id='icCardSearchButton' class='btn'>検索</button>
				</div>
				<div class='control-group'>
					<input type="checkbox" id='jyogaiDataHyouji' value='0' <c:if test='${"1" eq jyogaiDataHyoujiFlg}'>checked</c:if>>　除外データ表示
					<button type='button' id='jyogaiButton' class='btn' <c:if test='${"1" eq jyogaiDataHyoujiFlg}'>disabled</c:if>>除外</button>
					<button type='button' id='saihyoujiButton' class='btn' <c:if test='${"1" ne jyogaiDataHyoujiFlg}'>disabled</c:if>>復活</button>
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
						<th style='width: 30px'><input type='checkbox' id='allcheck'></th>
						<th style='width: 100px'>利用日<br>
							<a id='riyoubiAsc' class='link'>▲</a>
							<a id='riyoubiDesc' class='link'>▼</a>
						</th>
						<th style='width: 150px'>出発路線名</th>
						<th style='width: 150px'>出発駅名</th>
						<th style='width: 150px'>到着路線名</th>
						<th style='width: 150px'>到着駅名</th>
						<th style='width: 100px'>金額</th>
					</tr>
				</thead>
				<c:forEach var="record" items="${list}">
					<tbody name='icCardRirekiBody'>
						<tr>
							<td align="center"><input type='checkbox' name='sentaku' 
									ic-card-no			='${record.ic_card_no}'
									ic-card-sequence-no	='${record.ic_card_sequence_no}'
									tanmatu-cd			='${record.tanmatu_cd}'
									ic-card-riyoubi		='${record.ic_card_riyoubi}' 
									line-name-from		='${record.line_name_from}' 
									eki-name-from		='${record.eki_name_from}' 
									line-name-to		='${record.line_name_to}' 
									eki-name-to			='${record.eki_name_to}' 
									kingaku				='${record.kingaku}'></td>
							<td class='icCardRiyoubi'>${record.ic_card_riyoubi}</td>
							<td class='lineNameFrom'>${record.line_name_from}</td>
							<td class='ekiNameFrom'>${record.eki_name_from}</td>
							<td class='lineNameTo'>${record.line_name_to}</td>
							<td class='ekiNameTo'>${record.eki_name_to}</td>
							<td class='kingaku text-r'>${record.kingaku}</td>
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
//除外データ表示中は明細追加はさせない
if($("#jyogaiDataHyouji").prop("checked")){
	$('#icRirekiAddBtn').hide();
}else{
	$('#icRirekiAddBtn').show();
}
/**
 * 親画面で選択済みのICカード履歴は非表示とする
 */
$("tbody[name=icCardRirekiBody]").find("tr").each(function(){
	var icCardRirekiRow = $(this);
	var showIcCardNo = $("input[name=sentaku]",this).attr("ic-card-no");
	var showIcCardSequenceNo = $("input[name=sentaku]",this).attr("ic-card-sequence-no");
	
	$("tr.meisai").each(function(){
		var selectedIcCardNo = $("input:hidden[name=icCardNo]",this).val();
		var selectedSequenceNo = $("input:hidden[name=icCardSequenceNo]",this).val();
		
		if(showIcCardNo == selectedIcCardNo && showIcCardSequenceNo == selectedSequenceNo){
			icCardRirekiRow.hide();
		}
	});
});

/**
 * 検索ボタン押下時
 */
$("#icCardSearchButton").click(function(){
	var params = $("#dialogMain").find("form").serialize();
	$("#dialog").empty();
	$("#dialog").load("ic_card_rireki_sentaku?" + params);
});

/**
 * 除外ボタン押下時
 */
$("#jyogaiButton").click(function(){
	jyogaiFlgSetAction('1');
});

/**
 * 再表示ボタン押下時
 */
$("#saihyoujiButton").click(function(){
	jyogaiFlgSetAction('0');
});

/**
 * 全チェックボックスを選択
 */
$("#allcheck").click(function(){
	$("#icCardSearchResult").find("input[type='checkbox']").prop('checked', $(this).prop("checked"));
});

/**
 * 除外データ表示チェックボックス変更時
 */
$("#jyogaiDataHyouji").click(function(){
	sortAction();
});

/**
 * 利用日昇順ソート押下時
 */
$("#riyoubiAsc").click(function(){
	sortAction(1);
});
/**
 * 利用日降順ソート押下時
 */
$("#riyoubiDesc").click(function(){
	sortAction(2);
});


/**
 * 検索処理
 * @param sort ソート区分
 */
 function sortAction(sort) {
	if(sort != null){
		$("input[name=sortKbn]").val(sort);
	}
	if($("#jyogaiDataHyouji").prop("checked")){
		$("input[name=jyogaiDataHyoujiFlg]").val("1");
	}else{
		$("input[name=jyogaiDataHyoujiFlg]").val("0");
	}
	var params = $("#dialogMain").find("form").serialize();
	$("#dialog").empty();
	$("#dialog").load("ic_card_rireki_sentaku?" + params);
}

 /**
  * 除外フラグ設定処理
  * @param sort 除外フラグ
  */
function jyogaiFlgSetAction(flag) {
	if($("input[name=sentaku]:checked").length == 0){
		alert("ICカード履歴が選択されていません。");
		return;
	}
	var jyogaiList = "";
	$("input[name=sentaku]:checked").each(function(){
		jyogaiList = jyogaiList + ($(this).attr("ic-card-no") + "-" + $(this).attr("ic-card-sequence-no") + ",");
	});
	jyogaiList = jyogaiList.substring(0, (jyogaiList.length - 1));
	
	$("input[name=jyogaiChangeFlg]").val(flag);
	$("input[name=jyogaiChangeList]").val(jyogaiList);
	
	var params = $("#dialogMain").find("form").serialize();
	$("#dialog").empty();
	$("#dialog").load("ic_card_rireki_sentaku_jyogaichange?" + params);
}

</script>
