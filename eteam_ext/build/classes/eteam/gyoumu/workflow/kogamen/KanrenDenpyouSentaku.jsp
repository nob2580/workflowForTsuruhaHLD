<%@page import="eteam.base.EteamFormatter"%>
<%@page import="eteam.common.KaishaInfo"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- エラー -->
<input type='hidden' id="errorCnt" value='${fn:length(errorList)}'>
<%@ include file="/jsp/eteam/include/InputError.jsp" %>

<!-- メイン -->
<div id='dialogMain'>
	<!-- 入力フィールド -->
	<section id = 'kanrenDenpyou'>

		<h2>検索条件</h2>
		<div><form class="form-inline">
			<input type='hidden' name='jyogaiDataHyoujiFlg' value='${su:htmlEscape(jyogaiDataHyoujiFlg)}'>
			<div class='control-group'>
				<label class="label label-sub">伝票ID</label>
				<input type='text' class='input-nomal' name='kanrenDenpyouId' maxlength='19' value='${denpyouId}'>
				<label class="label label-sub">伝票種別</label>
				<select name='denpyouShubetsu' class='input-large'>
					<option></option>
				<c:forEach var="shubetsu" items="${shubetsuList}">
					<option value = '${su:htmlEscape(shubetsu.denpyou_kbn)}' <c:if test='${su:htmlEscape(shubetsu.denpyou_kbn) eq su:htmlEscape(denpyouShubetsu)}'> selected </c:if>>${su:htmlEscape(shubetsu.denpyou_shubetsu)}</option>
				</c:forEach>
				</select>
			</div>
			<div class='control-group'>
			<label class='label label-sub'>最終承認日</label>
				<input type='text' name='shouninFrom' class='input-small datepicker' value='${su:htmlEscape(shouninFrom)}'>
				～
				<input type='text' name='shouninTo' class='input-small datepicker' value='${su:htmlEscape(shouninTo)}'>
				<button type='button' id='denpyouSearchButton' class='btn'>検索</button>
			</div>
			<div class='control-group'>
				<input type="checkbox" id='jyogaiDataHyouji' value='0' <c:if test='${"1" eq jyogaiDataHyoujiFlg}'>checked</c:if>>　除外データ表示
				<button type='button' id='jyogaiButton' class='btn' <c:if test='${"1" eq jyogaiDataHyoujiFlg}'>disabled</c:if>>除外</button>
				<button type='button' id='saihyoujiButton' class='btn' <c:if test='${"1" ne jyogaiDataHyoujiFlg}'>disabled</c:if>>復活</button>
			</div>
		</form></div>
	</section>
	<section>
		<h2>検索結果</h2>
		<div><form id='kanren' method='post' target="_self">
		<table class='table-bordered table-condensed'>
			<thead>
				<tr>
					<th>選択</th>
					<th>伝票種別</th>
					<th>伝票ID</th>
					<th>起票日</th>
					<th>最終承認日</th>
				</tr>
			</thead>
			<tbody id='tenpuDenpyouList'>
<c:forEach var="record" items="${kanrenList}">
				<tr class="tenpuDenpyou">
					<td align="center"><input type='checkbox' name='sentaku' value='${su:htmlEscape(record.denpyou_id)}'></td>
					<td style="white-space:nowrap;">${record.shubetsu}</td>
					<td style="white-space:nowrap;">${record.id}</td>
					<td style="white-space:nowrap;">${record.kihyou_bi}</td>
					<td style="white-space:nowrap;">${record.shounin_bi}</td>
					<input type='hidden' name="hyoujiKanrenEmbedSpace" value='${su:htmlEscape(record.embed_space)}'>
					<input type='hidden' name="hyoujiKanrenDenpyouKbn" value='${su:htmlEscape(record.kbn)}'>
					<input type='hidden' name="hyoujiKanrenDenpyouShubetsuUrl" value='${su:htmlEscape(record.denpyou_shubetsu_url)}'>
					<input type='hidden' name="hyoujiKanrenDenpyouUrl" value='${su:htmlEscape(record.denpyou_url)}'>
					<input type='hidden' name="hyoujiKanrenDenpyouShubetsu" value='${su:htmlEscape(record.shubetsu)}'>
					<input type='hidden' name="hyoujiKanrenDenpyouId" value='${su:htmlEscape(record.id)}'>
					<input type='hidden' name="hyoujiKanrenTenpuFileName" value="${su:htmlEscape(record.file_name)}">
					<input type='hidden' name="hyoujiKanrenTenpuFileUrl" value="${su:htmlEscape(record.tenpu_url)}">
					<input type='hidden' name="hyoujiKanrenKihyouBi" value='${su:htmlEscape(record.kihyou_bi)}'>
					<input type='hidden' name="hyoujiKanrenShouninBi" value='${su:htmlEscape(record.shounin_bi)}'>
					
					<input type='hidden' name="kanrenDenpyouId" value='${su:htmlEscape(record.denpyou_id)}'>
					<input type='hidden' name="kanrenDenpyouKbn" value='${su:htmlEscape(record.denpyou_kbn)}'>
					<input type='hidden' name="kanrenTourokuTime" value='${su:htmlEscape(record.touroku_time)}'>
					<input type='hidden' name="kanrenShouninTime" value='${su:htmlEscape(record.shounin_time)}'>
				</tr>
</c:forEach>
			</tbody>
		</table>
		</form></div>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
//初期表示処理
$("#dialog").ready(function(){
	commonInit($("#kanrenDenpyou"));
	
	//リスト内伝票IDを対応するリンクに置換
	var tenpuDenpyouList = $("#tenpuDenpyouList");
	$.each(tenpuDenpyouList.find("tr.tenpuDenpyou"), function (ii, obj) {
		var tenpu = $(obj);
		
		// 伝票ID
		var denpyouObj = tenpu.find("td:eq(2)");
		denpyouObj.children().remove();
		denpyouObj.text('');

		var kanrenDenpyouIdStr = tenpu.find("input[name=hyoujiKanrenDenpyouId]").val();
		var kanrenDenpyouShubetsuUrlStr = tenpu.find("input[name=hyoujiKanrenDenpyouShubetsuUrl]").val();
		var kanrenDenpyouUrlStr = tenpu.find("input[name=hyoujiKanrenDenpyouUrl]").val();
		var kanrenEmbedSpaceStr = tenpu.find("input[name=hyoujiKanrenEmbedSpace]").val();
		
		// 空白を消去
		kanrenDenpyouIdStr = kanrenDenpyouIdStr.replace(/　/g,"");
		// 伝票リスト作成
		var kanrenDenpyouIdList = kanrenDenpyouIdStr.split("<br>");
		var kanrenDenpyouShubetsuUrlList = kanrenDenpyouShubetsuUrlStr.split(",");
		var kanrenDenpyouUrlList = kanrenDenpyouUrlStr.split(",");
		var kanrenEmbedSpaceList = kanrenEmbedSpaceStr.split(",");

		for (var i = 0; i < kanrenDenpyouIdList.length; i++) {
			if (!windowSizeChk()) {
				denpyouObj.append(kanrenEmbedSpaceList[i]);
			}
			// 関連伝票URL
			denpyouObj.append($("<a/>").text(kanrenDenpyouIdList[i]).attr("id", "link_" + kanrenDenpyouIdList[i]));
			denpyouObj.find("#link_" + kanrenDenpyouIdList[i]).attr('href', kanrenDenpyouUrlList[i]);
			denpyouObj.find("#link_" + kanrenDenpyouIdList[i]).attr('target', '_blank');
			denpyouObj.append("<br>");
		}
	});

});
//除外データ表示中は明細追加はさせない
if($("#jyogaiDataHyouji").prop("checked")){
	$('#tenpuDenpyouAddBtn').hide();
}else{
	$('#tenpuDenpyouAddBtn').show();
}

/**
 * 検索ボタン押下
 */
$("#denpyouSearchButton").click(function(){
	SearchAction();
});

/**
 * 除外データ表示チェックボックス変更時
 */
$("#jyogaiDataHyouji").click(function(){
	SearchAction();
});

/**
 * 除外ボタン押下時
 */
$("#jyogaiButton").click(function(){
	jyogaiFlgSetAction('1');
});

/**
 * 復活ボタン押下時
 */
$("#saihyoujiButton").click(function(){
	jyogaiFlgSetAction('0');
});

/**
 * 検索処理
 */
function SearchAction() {
	var kanrenSection = $("#kanrenDenpyou");
	var denpyouId = kanrenSection.find("[name=kanrenDenpyouId]").val();
	var denpyouShubetsu = kanrenSection.find("[name=denpyouShubetsu]").val();
	var shouninFrom = kanrenSection.find("[name=shouninFrom]").val();
	var shouninTo = kanrenSection.find("[name=shouninTo]").val();
	var jyogaiDataHyoujiFlg = $("#jyogaiDataHyouji").prop("checked") ? "1" : "0";
	$("#dialog")
	.load("kanren_denpyou_sentaku_kensaku?gamenDenpyouId=" + $("span#denpyouId").text() + "&denpyouId="+denpyouId+"&denpyouShubetsu="+denpyouShubetsu+"&shouninFrom="+shouninFrom+"&shouninTo="+shouninTo+"&jyogaiDataHyoujiFlg="+jyogaiDataHyoujiFlg);
}
/**
 * 除外フラグ設定処理
 * @param sort 除外フラグ
 */
function jyogaiFlgSetAction(flag) {
	
	if($("input[name=sentaku]:checked").length == 0){
		alert("関連伝票が選択されていません。");
		return;
	}
	
	var jyogaiList = "";
	$("input[name=sentaku]:checked").each(function(){
		jyogaiList = jyogaiList + ($(this).val() + ",");
	});
	jyogaiList = jyogaiList.substring(0, (jyogaiList.length - 1));
	var jhFlg = $("#jyogaiDataHyouji").prop("checked") ? "1" : "0";
	var gdId  = $("span#denpyouId").text();
	$("#dialog").empty();
	$("#dialog").load("kanren_denpyou_sentaku_jyogaichange",
		{jyogaiChangeList:jyogaiList, 
		 jyogaiChangeFlg :flag,
		 jyogaiDataHyoujiFlg:jhFlg,
		 gamenDenpyouId  :gdId});
}


</script>

