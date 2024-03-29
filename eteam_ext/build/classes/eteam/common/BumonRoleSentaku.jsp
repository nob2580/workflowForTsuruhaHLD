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
		<div id='bumonRole'>
<c:forEach var="record" items="${list}">
			<!-- <p><a href='#' data-bumon='zensya'>全社</a></p> -->
			<p><a class='link' data-cd='${record.bumon_role_id}' data-name='${su:htmlEscape(record.bumon_role_name)}' onclick='javascript:bumonRoleClick($(this));'>${su:htmlEscape(record.bumon_role_name)}</a></p>
</c:forEach>
		</div>
	</div>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
<!--
/**
 * 部門ロール検索ダイアログ：部門ロール選択
 @param 選択された部門ロールaタグ
 */
function bumonRoleClick(a) {
	if ( "bumonRoleData" in window && bumonRoleData != null) {
		bumonRoleData = a;
		$("#dialog").children().remove();
		$("#dialog").dialog("close");
	} else {
		dialogRetBumonRoleId.val(a.attr("data-cd"));
		dialogRetBumonRoleName.val(a.attr("data-name"));
		$("#dialog").dialog("close");
	}
}
//-->
</script>
