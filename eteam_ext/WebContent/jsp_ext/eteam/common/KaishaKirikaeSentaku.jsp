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
		<div id='kaishaKirikae'>
<c:forEach var="record" items="${list}">
			<p><a class='link' data-cd='${record.scheme_cd}' data-name='${su:htmlEscape(record.scheme_name)}' onclick="kaishaKirikaeClick($(this))">${su:htmlEscape(record.scheme_name)}</a></p>
</c:forEach>
		</div>
	</div>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
<!--

/**
 * 会社切替選択ダイアログ
 * @param a 選択された会社名リンク
 */
function kaishaKirikaeClick(a) {
	if ("dialogRetKaishaKirikaeSentakuCallback" in window && dialogRetKaishaKirikaeSentakuCallback != null) {
		dialogRetKaishaKirikaeSentakuCallback(a);
	} else {
		dialogRetKaishaKirikaeCd.val(a.attr("data-cd"));
		dialogRetKaishaKirikaeName.val(a.attr("data-name"));
	}
	$("#dialog").children().remove();
	$("#dialog").dialog("close");
}
//-->
</script>