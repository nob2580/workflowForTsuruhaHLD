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
	<section>
		<h2>予算チェック</h2>
		<div class='control-group'>
			<input type='hidden' name="denpyouId" value='${su:htmlEscape(denpyouId)}'>
			<input type='hidden' name="denpyouKbn" value='${su:htmlEscape(denpyouKbn)}'>
			<input type='hidden' name="sentakuBumonCd" value='${su:htmlEscape(sentakuBumonCd)}'>
			<input type='hidden' name="sentakuNendo" value='${su:htmlEscape(sentakuNendo)}'>
			<input type='hidden' name="sentakuRyakugou" value='${su:htmlEscape(sentakuRyakugou)}'>
			<input type='hidden' name="sentakuKianbangouFrom" value='${su:htmlEscape(sentakuKianbangouFrom)}'>
			<input type='hidden' name="choukaKijun" value='${su:htmlEscape(choukaKijun)}'>
		</div>
		<table class='table-bordered table-condensed'>
			<thead>
				<tr>
					<th><nobr>集計部門</nobr></th>
<c:if test='${"1" eq checkTani}'>
					<th><nobr>科目</nobr></th>
					<th><nobr>科目枝番</nobr></th>
</c:if>
					<th><nobr>明細部門</nobr></th>
					<th><nobr>予算額</nobr></th>
					<th><nobr>累計実績</nobr></th>
					<th><nobr>申請額</nobr></th>
					<th><nobr>予算残高</nobr></th>
					<th<c:if test='${"0" eq choukaKijun}'> style="display:none;"</c:if>><nobr>判定コメント</nobr></th>
				</tr>
			</thead>
			<tbody id="list">
<c:forEach var="record" items="${dataList}">
	<c:if test='${"" ne record.syuukeiBumonCd && "" eq record.meisaiBumonCd}'>
				<tr class="syuukei">
					<td class="text-l syuukeiBumonCd"><a href="#" data-syuukei='${record.syuukeiBumonCd}'<c:if test='${"1" eq checkTani}'> data-kamoku='${record.kamokuCd}' data-edaban='${record.kamokuEdabanCd}'</c:if> onClick="hideToggle($(this)); return false;">${su:htmlEscape(record.syuukeiBumonCd)}　${su:htmlEscape(record.syuukeiBumonName)}</a></td>
		<c:if test='${"1" eq checkTani}'>
					<td class="text-r">${su:htmlEscape(record.kamokuName)}</td>
					<td class="text-r">${su:htmlEscape(record.kamokuEdabanName)}</td>
		</c:if>
					<td class="text-l"></td>
					<td class="text-r">${su:htmlEscape(record.kiangaku)}</td>
					<td class="text-r">${su:htmlEscape(record.jissekigaku)}</td>
					<td class="text-r">${su:htmlEscape(record.shinseigaku)}</td>
					<td class='zandaka text-r'>${su:htmlEscape(record.zandaka)}</td>
					<td<c:if test='${"0" eq choukaKijun}'> style="display:none;"</c:if>>${su:htmlEscape(record.judgeComment)}</td>
					<input type='hidden' name="syuukeiBumonCd" value='${record.syuukeiBumonCd}'>
					<input type='hidden' name="syuukeiBumonName" value='${record.syuukeiBumonName}'>
					<input type='hidden' name="kamokuCd" value='${record.kamokuCd}'>
					<input type='hidden' name="kamokuName" value='${record.kamokuName}'>
					<input type='hidden' name="kamokuEdabanCd" value='${record.kamokuEdabanCd}'>
					<input type='hidden' name="kamokuEdabanName" value='${record.kamokuEdabanName}'>
					<input type='hidden' name="meisaiBumonCd" value='${record.meisaiBumonCd}'>
					<input type='hidden' name="meisaiBumonName" value='${record.meisaiBumonName}'>
					<input type='hidden' name="kiangaku" value='${record.kiangaku}'>
					<input type='hidden' name="jissekigaku" value='${record.jissekigaku}'>
					<input type='hidden' name="shinseigaku" value='${record.shinseigaku}'>
					<input type='hidden' name="zandaka" value='${record.zandaka}'>
					<input type='hidden' name="judgeComment" value='${su:htmlEscape(record.judgeComment)}'>
				</tr>
	</c:if>
	<c:if test='${"" ne record.syuukeiBumonCd && "" ne record.meisaiBumonCd}'>
				<tr class="meisai">
					<td class="text-l"></td>
		<c:if test='${"1" eq checkTani}'>
					<td class="text-r">${su:htmlEscape(record.kamokuName)}</td>
					<td class="text-r">${su:htmlEscape(record.kamokuEdabanName)}</td>
		</c:if>
					<td class="text-l">${su:htmlEscape(record.meisaiBumonCd)}　${su:htmlEscape(record.meisaiBumonName)}</td>
					<td class="text-r">${su:htmlEscape(record.kiangaku)}</td>
					<td class="text-r">${su:htmlEscape(record.jissekigaku)}</td>
					<td class="text-r">${su:htmlEscape(record.shinseigaku)}</td>
					<td class='zandaka text-r'>${su:htmlEscape(record.zandaka)}</td>
					<td<c:if test='${"0" eq choukaKijun}'> style="display:none;"</c:if>>${su:htmlEscape(record.judgeComment)}</td>
					<input type='hidden' name="syuukeiBumonCd" value='${record.syuukeiBumonCd}'>
					<input type='hidden' name="syuukeiBumonName" value='${record.syuukeiBumonName}'>
					<input type='hidden' name="kamokuCd" value='${record.kamokuCd}'>
					<input type='hidden' name="kamokuName" value='${record.kamokuName}'>
					<input type='hidden' name="kamokuEdabanCd" value='${record.kamokuEdabanCd}'>
					<input type='hidden' name="kamokuEdabanName" value='${record.kamokuEdabanName}'>
					<input type='hidden' name="meisaiBumonCd" value='${record.meisaiBumonCd}'>
					<input type='hidden' name="meisaiBumonName" value='${record.meisaiBumonName}'>
					<input type='hidden' name="kiangaku" value='${record.kiangaku}'>
					<input type='hidden' name="jissekigaku" value='${record.jissekigaku}'>
					<input type='hidden' name="shinseigaku" value='${record.shinseigaku}'>
					<input type='hidden' name="zandaka" value='${record.zandaka}'>
					<input type='hidden' name="judgeComment" value='${su:htmlEscape(record.judgeComment)}'>
				</tr>
	</c:if>
	<c:if test='${"" eq record.syuukeiBumonCd}'>
				<tr class="goukei">
					<td class="text-l"></td>
		<c:if test='${"1" eq checkTani}'>
					<td class="text-r"></td>
					<td class="text-r"></td>
		</c:if>
					<td class="text-r">【合計】</td>
					<td class="text-r">${su:htmlEscape(record.kiangaku)}</td>
					<td class="text-r">${su:htmlEscape(record.jissekigaku)}</td>
					<td class="text-r">${su:htmlEscape(record.shinseigaku)}</td>
					<td class='zandaka text-r'>${su:htmlEscape(record.zandaka)}</td>
					<td<c:if test='${"0" eq choukaKijun}'> style="display:none;"</c:if>>${su:htmlEscape(record.judgeComment)}</td>
					<input type='hidden' name="syuukeiBumonCd" value="">
					<input type='hidden' name="syuukeiBumonName" value="">
					<input type='hidden' name="kamokuCd" value="">
					<input type='hidden' name="kamokuName" value="">
					<input type='hidden' name="kamokuEdabanCd" value="">
					<input type='hidden' name="kamokuEdabanName" value="">
					<input type='hidden' name="meisaiBumonCd" value="">
					<input type='hidden' name="meisaiBumonName" value="">
					<input type='hidden' name="kiangaku" value='${record.kiangaku}'>
					<input type='hidden' name="jissekigaku" value='${record.jissekigaku}'>
					<input type='hidden' name="shinseigaku" value='${record.shinseigaku}'>
					<input type='hidden' name="zandaka" value='${record.zandaka}'>
					<input type='hidden' name="judgeComment" value='${su:htmlEscape(record.judgeComment)}'>
				</tr>
	</c:if>
</c:forEach>
			</tbody>
		</table>
		<br>
		<div>
			<p>予算チェックコメント</p>
		<textarea class='input-block-level' id='comment' placeholder='予算を超過している場合はコメントを入力してください。(200字)'>${su:htmlEscape(comment)}</textarea>
		</div>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
function getDispInfo(mode){
	if("2" == mode){
		return {
			denpyouId:				$("input[name=denpyouId]").val(),
			denpyouKbn:				$("input[name=denpyouKbn]").val(),
			mode:					"0",
			sentakuBumonCd:			$("#dialogMain").find("input[name=sentakuBumonCd]").val(),
			sentakuNendo:			$("#dialogMain").find("input[name=sentakuNendo]").val(),
			sentakuRyakugou:		$("#dialogMain").find("input[name=sentakuRyakugou]").val(),
			sentakuKianbangouFrom:	$("#dialogMain").find("input[name=sentakuKianbangouFrom]").val(),
		}
	}else{
		var tr = $("#list").children("tr.syuukei, tr.meisai");
		return {
			denpyouId:				$("input[name=denpyouId]").val(),
			denpyouKbn:				$("input[name=denpyouKbn]").val(),
			mode:					mode,
			comment:				$("#comment").val(),
			syuukeiBumonCd:			$(tr).find("input[name=syuukeiBumonCd]").map(function(){return $(this).val();}).get(),
			syuukeiBumonName:		$(tr).find("input[name=syuukeiBumonName]").map(function(){return $(this).val();}).get(),
			kamokuCd:				$(tr).find("input[name=kamokuCd]").map(function(){return $(this).val();}).get(),
			kamokuName:				$(tr).find("input[name=kamokuName]").map(function(){return $(this).val();}).get(),
			kamokuEdabanCd:			$(tr).find("input[name=kamokuEdabanCd]").map(function(){return $(this).val();}).get(),
			kamokuEdabanName:		$(tr).find("input[name=kamokuEdabanName]").map(function(){return $(this).val();}).get(),
			meisaiBumonCd:			$(tr).find("input[name=meisaiBumonCd]").map(function(){return $(this).val();}).get(),
			meisaiBumonName:		$(tr).find("input[name=meisaiBumonName]").map(function(){return $(this).val();}).get(),
			kiangaku:				$(tr).find("input[name=kiangaku]").map(function(){return $(this).val().replaceAll(",", "");}).get(),
			jissekigaku:			$(tr).find("input[name=jissekigaku]").map(function(){return $(this).val().replaceAll(",", "");}).get(),
			shinseigaku:			$(tr).find("input[name=shinseigaku]").map(function(){return $(this).val().replaceAll(",", "");}).get(),
			zandaka:				$(tr).find("input[name=zandaka]").map(function(){return $(this).val().replaceAll(",", "");}).get(),
			judgeComment:			$(tr).find("input[name=judgeComment]").map(function(){return $(this).val();}).get(),
			judgeCommentGoukei:		$("#list").children("tr.goukei").find("input[name=judgeComment]").val(),
			sentakuBumonCd:			$("#dialogMain").find("input[name=sentakuBumonCd]").val(),
			sentakuNendo:			$("#dialogMain").find("input[name=sentakuNendo]").val(),
			sentakuRyakugou:		$("#dialogMain").find("input[name=sentakuRyakugou]").val(),
			sentakuKianbangouFrom:	$("#dialogMain").find("input[name=sentakuKianbangouFrom]").val(),
		}
	}
}

function hideToggle(a){
	var syuukeiBumonCd = a.attr("data-syuukei");
	$("#list").children("tr.meisai").each(function() {
		if($(this).find($("input[name=syuukeiBumonCd]")).val() == syuukeiBumonCd ){
			<c:if test='${"1" eq checkTani}'>
			var kamokuCd = a.attr("data-kamoku");
			var edabanCd = a.attr("data-edaban");
			if ($(this).find($("input[name=kamokuCd]")).val() == kamokuCd && $(this).find($("input[name=kamokuEdabanCd]")).val() == edabanCd){
			</c:if>
				if($(this).css("display")=="none"){
					$(this).show(100);
				}else{
					$(this).hide(100);
				}
			<c:if test='${"1" eq checkTani}'>
			}
			</c:if>
		}
	});
}


// 初期表示処理
$("#dialog").ready(function(){
	//残高がマイナスの場合の文字色を赤色表示
 	$("#list").children("tr").each(function(){
 		var target = $(this).find("td.zandaka");
 		var zandaka = parseInt(target.text().replaceAll(",", ""));
 		if(zandaka < 0){
 			target.css("color","red");
 		}
 	});
	
	//明細事業の列を非表示
	$("#list").children("tr.meisai").hide();
});

</script>

