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
<input type='hidden' name="tenpuDenpyouJidouFlg" value='${su:htmlEscape(tenpuDenpyouJidouFlg)}'>

	<!-- 入力フィールド -->
	<section id='karibaraiAnkenSearchJouken'>
		<div><form id='' method='post' action='' class="form-inline">
			<div class='control-group'>
				<label class="label label-sub">伝票ID</label>
				<input type='hidden' name='denpyouKbn' value='${su:htmlEscape(denpyouKbn)}'>
				<input type='text' name='denpyouID' value='${su:htmlEscape(denpyouID)}'>
				<button type='button' id='searchButton' class='btn' class='btn'>検索</button>
				<input type="hidden" name="oyaDenpyouId" value='${su:htmlEscape(oyaDenpyouId)}'>
				<input type='hidden' name='userId' value='${su:htmlEscape(userId)}'>
			</div>
		</form></div>
	</section>

	<section id='karibaraiAnkenSearchResult'>
	    <div>
		<c:if test="${fn:length(list) > 0}" >
		<table class='table-bordered table-condensed'>
	        <thead>
	          <tr>
	            <th>伝票ID</th>
	            <th style='width: 300px'>摘要</th>
	            <th>金額</th>
	          </tr>
	        </thead>
			<tbody>
				<c:forEach var="record" items="${list}">
				<tr>
					<c:choose>
						<c:when test="${'A004' == denpyouKbn || 'A011' == denpyouKbn}">
						  <td class='listTekiyou' style='max-width:300px;word-break: break-all;'><a class='link' onclick='javascript:karibaraiAnkenClick(this);' 
						  data-karibarai_on='${su:htmlEscape(record.karibarai_on)}'
						  data-cd='${su:htmlEscape(record.denpyou_id)}' 
						  data-name='${su:htmlEscape(record.tekiyou)}' 
						  data-note='${su:htmlEscape(record.kingaku)}' 
						  data-shinsei_kingaku='${su:htmlEscape(record.shinsei_kingaku)}' 
						  data-karibarai_kingaku='${su:htmlEscape(record.karibarai_kingaku)}' 
						  data-houmonsaki='${su:htmlEscape(record.houmonsaki)}' 
						  data-mokuteki='${su:htmlEscape(record.mokuteki)}' 
						  data-seisankikan_from='${su:htmlEscape(record.seisankikan_from)}'
						  data-seisankikan_from_hour='${su:htmlEscape(record.seisankikan_from_hour)}'
						  data-seisankikan_from_min='${su:htmlEscape(record.seisankikan_from_min)}'
						  data-seisankikan_to='${su:htmlEscape(record.seisankikan_to)}'
						  data-seisankikan_to_hour='${su:htmlEscape(record.seisankikan_to_hour)}'
						  data-seisankikan_to_min='${su:htmlEscape(record.seisankikan_to_min)}'
						  data-hosoku='${su:htmlEscape(record.hosoku)}'
						  data-ringi_hikitsugi_um_flg='${su:htmlEscape(record.ringi_hikitsugi_um_flg)}'
						  data-kian_tenpu_zumi_flg='${su:htmlEscape(record.kian_tenpu_zumi_flg)}'
						  >${su:htmlEscape(record.denpyou_id)}</a></td>
						</c:when>
						<c:otherwise>
						  <td class='listTekiyou' style='max-width:300px;word-break: break-all;'><a class='link' onclick='javascript:karibaraiAnkenClick(this);' 
						  data-karibarai_on='${su:htmlEscape(record.karibarai_on)}'
						  data-cd='${su:htmlEscape(record.denpyou_id)}' 
						  data-name='${su:htmlEscape(record.tekiyou)}' 
						  data-note='${su:htmlEscape(record.kingaku)}' 
						  data-shinsei_kingaku='${su:htmlEscape(record.shinsei_kingaku)}' 
						  data-karibarai_kingaku='${su:htmlEscape(record.karibarai_kingaku)}' 
						  data-ringi_hikitsugi_um_flg='${su:htmlEscape(record.ringi_hikitsugi_um_flg)}'
						  data-kian_tenpu_zumi_flg='${su:htmlEscape(record.kian_tenpu_zumi_flg)}'
						  >${su:htmlEscape(record.denpyou_id)}</a></td>
						</c:otherwise>
					</c:choose>
				<td class='listDenpyouID'>${su:htmlEscape(record.tekiyou)}</td>
				  <td class='listKingaku text-r'>${su:htmlEscape(record.kingaku)}</td> 
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
		</c:if>
	    </div>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
/**
 * 検索ボタン押下時Function
 */
$("#searchButton").click(function(){
	var form = $(this).closest("form");
	denpyouKbn = form.find("input[name=denpyouKbn]").val();
	denpyouID = form.find("input[name=denpyouID]").val();
	oyaDenpyouId = form.find("input[name=oyaDenpyouId]").val();
	userId = form.find("input[name=userId]").val();
	
	$("#dialog").load("karibarai_anken_sentaku?denpyouKbn=" + encodeURI(denpyouKbn) + "&denpyouID=" + encodeURI(denpyouID) + "&oyaDenpyouId=" + encodeURI(oyaDenpyouId) + "&userId=" + encodeURI(userId));
});

/**
 * 摘要選択
 */
 function karibaraiAnkenClick(a) {
	var kakoDenpyou = $("#karibaraiDenpyouIdHyouji").text();
	var a = $(a);
	var denpyouId = a.attr("data-cd");
	var denpyouKbn = denpyouId.substr(7, 4);
	var url = "";
	if (denpyouKbn == "A002") {
		url = "karibarai_shinsei"
	} else if (denpyouKbn == "A005") {
		url = "ryohi_karibarai_shinsei"
	} else if (denpyouKbn == "A012") {
		url = "kaigai_ryohi_karibarai_shinsei"
	}
	var kariLink = "<a href='" + url + "?denpyouId=" + denpyouId + "&denpyouKbn=" + denpyouKbn + "' target='_blank'>" + denpyouId + "</a>";
	$("div[id='karibaraiAnken']").show();
	dialogRetKaribaraiAnkenCd.html(kariLink);
	dialogRetKaribaraiAnkenName.text(a.attr("data-name"));
	dialogRetRingiHikitsugiUmFlg.val(a.attr("data-ringi_hikitsugi_um_flg"));
	dialogRetKianTenpuZumiFlg.val(a.attr("data-kian_tenpu_zumi_flg"));
//	if ("dialogRetKaribaraiAnkenNote" in window) dialogRetKaribaraiAnkenNote.text(a.attr("data-note"));
	dialogRetKaribaraiAnkenOn.text(a.attr("data-karibarai_on"));
	dialogRetShinseiKingaku.text(a.attr("data-shinsei_kingaku"));
	dialogRetKaribaraiKingaku.text(a.attr("data-karibarai_kingaku"));
	// 関連伝票追加・削除処理
	if ($("[name=tenpuDenpyouJidouFlg]").val() == "true") {
		tenpuDenpyouJidou(a, kakoDenpyou);
	}
	if ("dialogCallbackKaribaraiAnkenSentaku" in window)	dialogCallbackKaribaraiAnkenSentaku();
	isDirty = true;
	$("#dialog").dialog("close");
}

/**
 *  関連伝票追加・削除処理
 */
function tenpuDenpyouJidou (a, kakoDenpyou) {
	var tr = $("#link_" + kakoDenpyou).parents("tr.kanrenDenpyou");
	
	if (tr.length > 0) {
		if ($("#kanrenDenpyouList .kanrenDenpyou").length > 1) {
			tr.remove();
		} else {
			kanrenDenpyouClear(tr);
			$('#kanrenDenpyouTableDiv').css('display', 'none');
		}
	}
	setDialogKanrenDenpyouInfo(a.parents("tr"));
}

//初期表示処理
$("#dialog").ready(function(){
	commonInit($("#karibaraiAnkenSearchJouken"));
});
</script>
