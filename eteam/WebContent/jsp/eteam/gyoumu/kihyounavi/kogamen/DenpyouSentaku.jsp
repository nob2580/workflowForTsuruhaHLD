<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- タイトル -->
<h1>伝票選択</h1>

<!-- エラー -->
<%@ include file="/jsp/eteam/include/InputError.jsp" %>

<!-- メイン -->
<div id='dialogMain'>
	<div id='DenpyouKbn'>
		<table class='table-bordered table-condensed'>
		<thead>
			<tr>
				<th style='min-width:100px;'>業務種別</th>
				<th style='min-width:100px;'>伝票種別</th>
				<th>説明</th>
			</tr>
		</thead>
		<tbody>
<c:forEach var="rec" items="${list}">
			<tr>
				<td>${su:htmlEscape(rec.gyoumuShubetsu)}</td>
				<td><p><a class='link' data-cd='${rec.denpyouKbn}' data-name='${su:htmlEscape(rec.denpyouShubetsu)}' data-kind='${su:htmlEscape(rec.gyoumuShubetsu)}' data-setsumei='${su:htmlEscape(rec.setsumei)}' onclick='javascript:denpyouClick($(this));'>${su:htmlEscape(rec.denpyouShubetsu)}</a></p></td>
				<td>${su:htmlEscape(rec.setsumei)}</td>
 			</tr>
</c:forEach>
		</tbody>
		</table>
	</div>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
<!--
/**
 * 伝票検索ダイアログ：伝票選択
 @param 選択された伝票aタグ
 */
function denpyouClick(a) {
	denpyouKbnData = a;
	$("#dialog").children().remove();
	$("#dialog").dialog("close");
	if ("dialogCallbackDenpyouSentaku" in window)	dialogCallbackDenpyouSentaku();
}
//-->
</script>
