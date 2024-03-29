<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<div id='railMapSub1' style='float: left;'>
	<label class='control-label'>出発</label>
	<div class='controls'>
		<input type='text' class='input-medium' name='selStName1' value='${su:htmlEscape(selStName1)}'>
	</div>
	<label class='control-label'>経由／到着</label>
	<div class='controls'>
		<input type='text' class='input-medium' name='selStName2' value='${su:htmlEscape(selStName2)}'>
	</div>
	<label class='control-label'>経由／到着</label>
	<div class='controls'>
		<input type='text' class='input-medium' name='selStName3' value='${su:htmlEscape(selStName3)}'>
	</div>
	<label class='control-label'>経由／到着</label>
	<div class='controls'>
		<input type='text' class='input-medium' name='selStName4' value='${su:htmlEscape(selStName4)}'>
	</div>
	<label class='control-label'>経由／到着</label>
	<div class='controls'>
		<input type='text' class='input-medium' name='selStName5' value='${su:htmlEscape(selStName5)}'>
	</div>
</div>
