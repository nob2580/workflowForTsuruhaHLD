<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<c:if test='${fn:length(errorList) ne 0}'>
<div class="alert">
	<button type="button" class="close" data-dismiss="alert">&times;</button>
	<c:forEach var="errorMessage" items='${errorList}'>
	<p>${su:htmlEscape(errorMessage)}</p>
	</c:forEach>
</div>
</c:if>
