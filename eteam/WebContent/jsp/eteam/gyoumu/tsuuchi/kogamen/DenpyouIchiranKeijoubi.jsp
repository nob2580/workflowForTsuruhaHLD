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

	<!-- 検索結果 -->
	<section>
		<p>計上日</p>

<c:if test="${setting.keijouNyuuryoku() eq 1}">
            <input type='text' class='input-small datepicker' name='tmpUpdateKeijoubi'>
</c:if>

<c:if test="${setting.keijouNyuuryoku() eq 2}">
		<select class='input-medium' name='tmpUpdateKeijoubi'>
			<option value=''></option>
<c:forEach var="record" items="${keijoubiList}">
			<option value='${record}'>${record}</option>
</c:forEach>
		</select>
</c:if>

	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
commonInit($("#dialogMain"));
</script>
