<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- エラー -->
<%@ include file="/jsp/eteam/include/InputError.jsp" %>

<div id='dialogMain'>
<!-- メイン -->
	<section>
		<table class='table-bordered table-condensed'>
			<thead>
				<tr>
					<th>部署名</th>
				</tr>
			</thead>
			<tbody>
<c:forEach var="record" items="${list}">
				<tr>
					<td><a class='link' data-cd='${record.gougi_pattern_no}' data-name='${su:htmlEscape(record.gougi_name)}' onclick='javascript:gougiBushoClick($(this));'>${su:htmlEscape(record.gougi_name)}</a></td>
				</tr>
</c:forEach>
			</tbody>
		</table>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
<!--
/**
 * 合議部署検索ダイアログ：合議部署選択
 @param 選択された合議部署aタグ
 */
function gougiBushoClick(a) {
	if ( "gougiBushoData" in window && gougiBushoData != null) {
		gougiBushoData = a;
		$("#dialog").children().remove();
		$("#dialog").dialog("close");
	} else {
		dialogRetGougiBushoId.val(a.attr("data-cd"));
		dialogRetGougiBushoName.val(a.attr("data-name"));
		$("#dialog").dialog("close");
	}
}
//-->
</script>
