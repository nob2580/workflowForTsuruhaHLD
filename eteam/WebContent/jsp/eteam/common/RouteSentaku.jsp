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
	<div class='no-more-tables'>
	<table class='table-bordered table-condensed'>
		<thead>
			<tr>
				<th colspan="2" align="center" valign="top">経路</th>
				<th align="center" valign="top">運賃</th>
<c:if test="${shiyouKikanKbn != '01' && shiyouKikanKbn != '03' && shiyouKikanKbn != '06'}">
				<th align="center" valign="top">料金</th>
</c:if>
			</tr>
		</thead>
		<tbody>
<c:forEach var="record" items="${keiroList}" varStatus="status">
			<tr>
				<td class="span1" align="center">
					<button type='button' class='btn btn-small' onclick="sentakuBtnClick(this)">選択</button>
				</td>
				<td>
					<label for="keiro">${su:htmlEscape(record.keiro)}</label>
				</td>
				<td>
					<label for="money" class='input-small autoNumeric'>${su:htmlEscape(record.price)}円</label>
				</td>
<c:if test="${shiyouKikanKbn != '01' && shiyouKikanKbn != '03' && shiyouKikanKbn != '06'}">
				<td>
	<c:if test="${!empty record.routeChargeList}">
		<c:forEach var="record2" items="${record.routeChargeList}" varStatus="status">
			<c:if test="${status.index > 0}">
				<br/>
			</c:if>
					<label for="trainName">${record2.trainName}</label>
					<select name='charge' class='input-medium'>
			<c:forEach var="record3" items="${record2.chargeList}">
						<option value='${su:htmlEscape(record3.charge)}'>${su:htmlEscape(record3.name)}:${su:htmlEscape(record3.charge)}円</option>
			</c:forEach>
					</select>
		</c:forEach>
	</c:if>
				</td>
</c:if>
			</tr>
</c:forEach>
		</tbody>
	</table>
	</div>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
function sentakuBtnClick(element) {
	
	var tr = $(element).closest("tr");

	// 経路情報
	var keiro = $(tr).find("label[for='keiro']").text();
	// 金額
	var money = $(tr).find("label[for='money']").text();

	var trainName = $(tr).find("label[for='trainName']");
	
	// 料金発生路線リスト
	var trainNameList = new Array();
	for (var i = 0; i < trainName.length; i++) {
		trainNameList[i] = $(trainName[i]).text();
	}
	
	var select = $(tr).find("select");
	
	// 料金リスト
	var selectChargeList = new Array();
	// 料金リスト文字列
	var selectChargeStrList = new Array();
	for (var i = 0; i < select.length; i++) {
		selectChargeList[i] = $(select[i]).find("option:selected").val();
		selectChargeStrList[i] = $(select[i]).find("option:selected").text();
	}
	
	// 金額(###,###円)を数値に変換
	money = money.replace(",", "");
	money = money.replace("円", "");
	
	// 料金リストを数値に変換
	for (var i = 0; i < selectChargeList.length; i++) {
		selectChargeList[i] = selectChargeList[i].replace(",", "");
	}

	// 運賃と料金を合算する
	for (var i = 0; i < selectChargeList.length; i++) {
		money = parseInt(money) + parseInt(selectChargeList[i]);
	}

	// 経路情報に料金情報を挿入する
	for (var i = 0; i < selectChargeStrList.length; i++) {
		keiro = keiro.replace("(" + trainNameList[i] + ")", "(" + trainNameList[i] + "(" +selectChargeStrList[i] + "))");
	}
	
	dialogRetRouteMoney.val(String(money).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	dialogRetRouteKukan.val(keiro);
	
	var denpyouKbn = $("input[name=denpyouKbn]").val();

	if (denpyouKbn == "A006") {
		dialogRetTenyuuryokuFlg.val("0");
		isDirty = true;
	}
	
	//呼び出し元の経路選択コールバック処理
	if ("routeSentakuCallback" in window) routeSentakuCallback();
	
	$("#dialog").children().remove();
	$("#dialog").dialog('close');
}
</script>
