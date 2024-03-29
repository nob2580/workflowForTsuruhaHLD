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
	<div class='thumbnail'>
		<div id='searchStation'>
<c:forEach var="record" items="${list}">
			<p><a class='link' data-name='${su:htmlEscape(record)}' onclick='javascript:stationClick($(this));'>${su:htmlEscape(record)}</a></p>
</c:forEach>
		</div>
	</div>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
<!--
/**
 * 駅名検索ダイアログ：駅名選択
 @param 選択された駅名aタグ
 */
function stationClick(a) {

	dialogRetStationName.val(a.attr("data-name"));
	$("#dialog").children().remove();
	$("#dialog").dialog("close");

}
//-->
</script>
