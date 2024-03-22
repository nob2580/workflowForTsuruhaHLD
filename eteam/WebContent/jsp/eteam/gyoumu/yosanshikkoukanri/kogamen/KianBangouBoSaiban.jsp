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
	<!-- 起案番号採番ダイアログ選択値（部門コード） -->
	<input type="hidden" name='sentakuBumonCd' value='${su:htmlEscape(sentakuBumonCd)}'>
	<!-- 起案番号採番ダイアログ選択値（年度） -->
	<input type="hidden" name='sentakuNendo' value='${su:htmlEscape(sentakuNendo)}'>
	<!-- 起案番号採番ダイアログ選択値（略号） -->
	<input type="hidden" name='sentakuRyakugou' value='${su:htmlEscape(sentakuRyakugou)}'>
	<!-- 起案番号採番ダイアログ選択値（開始起案番号） -->
	<input type="hidden" name='sentakuKianbangouFrom' value='${su:htmlEscape(sentakuKianbangouFrom)}'>

	<!-- 入力フィールド -->
	<section>
		<div><form id='kanren' method='post' target="_self">
		<input type="hidden" name='kensakuBumonCd' value='${su:htmlEscape(kensakuBumonCd)}'>
		<input type="hidden" name='denpyouId'      value='${su:htmlEscape(denpyouId)}'>
		<table class='table-bordered table-condensed'>
			<thead>
				<tr>
					<th><nobr>選択</nobr></th>
					<th><nobr>年度</nobr></th>
					<th><nobr>略号</nobr></th>
					<th><nobr>区分内容</nobr></th>
					<th><nobr>開始番号</nobr></th>
					<th><nobr>終了番号</nobr></th>
					<th><nobr>最終番号</nobr></th>
				</tr>
			</thead>
			<tbody>
<c:forEach var="record" items="${list}">
				<tr>
					<td align="center"><input type='checkbox' name='sentaku' value='${su:htmlEscape(record.sentakuKey)}' <c:if test='${record.sentakuKey eq sentaku}'>checked</c:if>></td>
					<td style="white-space:nowrap;">${record.nendo}</td>
					<td style="white-space:nowrap;">${record.ryakugou}</td>
					<td style="white-space:nowrap;">${record.kbnNaiyou}</td>
					<td style="white-space:nowrap;">${record.kianbangouFrom}</td>
					<td style="white-space:nowrap;">${record.kianbangouTo}</td>
					<td style="white-space:nowrap;">${record.kianbangouLast}</td>
				</tr>
</c:forEach>
			</tbody>
		</table>
		</form></div>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>

// 初期表示処理
$("#dialog").ready(function(){

	// チェックボック選択（選択を単一にする）
	$("input[name=sentaku]").click(function(){
		if ($(this).prop('checked')){
			// 一旦全てをクリアして再チェックする
			$("input[name=sentaku]").prop('checked', false);
			$(this).prop('checked', true);
		}
	});
});

</script>

