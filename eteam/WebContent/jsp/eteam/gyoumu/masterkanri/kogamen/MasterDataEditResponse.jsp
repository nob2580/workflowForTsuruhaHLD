<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- メイン -->
<div style='display:none;'>
	<input type='hidden' id='returnCode' value='${su:htmlEscape(errorCode)}'>
	<input type='hidden' id='errorMessage' value='${su:htmlEscape(errorMessage)}'>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
</script>
