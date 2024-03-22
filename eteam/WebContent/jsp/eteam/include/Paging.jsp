<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<div id='paging'>
	<c:if test="${pageNo > 1}" ><a href='${pagingLink}pageNo=${pageNo-1}'>&lt;&lt;前へ</a></c:if>
	（全${totalCount}件　${pageNo}ページ/${totalPage}ページ中）
	<c:if test="${totalPage > pageNo}" ><a href='${pagingLink}pageNo=${pageNo+1}'>次へ&gt;&gt;</a></c:if>
</div>
