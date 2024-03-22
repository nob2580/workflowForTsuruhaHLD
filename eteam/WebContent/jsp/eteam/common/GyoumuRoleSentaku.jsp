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
		<div id='gyoumuRole'>
<c:forEach var="record" items="${list}">
						<p><a class='link' data-cd='${record.gyoumu_role_id}' data-name='${su:htmlEscape(record.gyoumu_role_name)}' onclick="gyoumuRoleClick($(this))">${su:htmlEscape(record.gyoumu_role_name)}</a></p>
</c:forEach>
		</div>
	</div>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
<!--

/**
 * 業務ロール検索ダイアログ：業務ロール選択
 * @param a 選択された業務ロールリンク
 */
function gyoumuRoleClick(a) {
	if ("dialogRetGyoumuRoleSentakuCallback" in window && dialogRetGyoumuRoleSentakuCallback != null) {
		dialogRetGyoumuRoleSentakuCallback(a);
	} else {
		dialogRetGyoumuRoleId.val(a.attr("data-cd"));
		dialogRetGyoumuRoleName.val(a.attr("data-name"));
	}
	$("#dialog").children().remove();
	$("#dialog").dialog("close");
}
//-->
</script>
