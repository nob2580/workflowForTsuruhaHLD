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

<!-- 	検索結果 -->
	<section>
<c:if test="${fn:length(furikomisakiList) > 0}" >
		<div><table class='table-bordered table-condensed'>
			<thead>
				<tr>
					<th>ID</th>
					<th>銀行</th>
					<th>支店</th>
					<th>口座種別</th>
					<th>口座番号</th>
					<th>口座名義人</th>
					<th>手数料負担</th>
				</tr>
			</thead>
			<tbody id='torihikisakiList'>
<c:forEach var="record" items="${furikomisakiList}">
				<tr>
					<td > <a class='link furikomisakiSentaku' data-furikomisaki='${su:htmlEscape(record.ginkou_id)}'>${su:htmlEscape(record.ginkou_id)}</a></td>
					<td class='ginkouName'data-furikomiCd='${su:htmlEscape(record.ginkou_cd)}'>${su:htmlEscape(record.ginkou_cd)}.${su:htmlEscape(record.ginkou_name)}</td>
					<td class='shitenName'>${su:htmlEscape(record.shiten_cd)}.${su:htmlEscape(record.shiten_name)}</td>
					<td class='kouzaShubetsu'>${su:htmlEscape(record.kouza_shubetsu)}</td>
					<td class='kouzaBangou'>${su:htmlEscape(record.kouza_bangou)}</td>
					<td class='kouzaMeiginin'>${su:htmlEscape(record.kouza_meiginin)}</td>
					<td class='tesuuryou'>${su:htmlEscape(record.tesuuryou_futan)}</td>
				</tr>
</c:forEach>
			</tbody>
		</table></div>
</c:if>
	</section>
 </div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>

/**
 * 銀行ID選択時
 */
$("a.furikomisakiSentaku").click(function(){
	var a = $(this);
	var tr = a.closest("tr");
	var td = tr.find("td.furikomisakiSentaku");
	$("input[name=furikomiGinkouId]").val(a.attr("data-furikomisaki"));
	$("input[name=furikomiGinkouCd]").val(tr.find("td.ginkouName").attr("data-furikomiCd"));

	realodTorihikisakiMaster();
	$("#dialog").empty();
	$("#dialog").dialog("close");
	
});

//初期表示処理
$("#dialog").ready(function(){
	commonInit($("#dialogMain"));
});
</script>
