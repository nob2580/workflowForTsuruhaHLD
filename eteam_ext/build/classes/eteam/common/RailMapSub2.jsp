<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<div id='railMapSub2' style='float: right'>
<c:if test='${imageFlg == "1"}'>
	<p id='railMap' style='position: relative;'>
		<img src='${su:htmlEscape(imageDir)}' name='image' alt='路線図' onclick='getXY(event)'>
		<span style="position: absolute; top: 5px; left: 42px;">
			<button type='button' name='up' class='btn btn-small input-inline' onclick="moveRailMap('up')">↑</button>
    	</span>
		<span style="position: absolute; top: 25px; left: 5px;">
			<button type='button' name='left' class='btn btn-small input-inline' onclick="moveRailMap('left')">←</button>
    	</span>
		<span style="position: absolute; top: 25px; left: 71px;">
			<button type='button' name='right' class='btn btn-small input-inline' onclick="moveRailMap('right')">→</button>
    	</span>
		<span style="position: absolute; top: 45px; left: 42px;">
			<button type='button' name='down' class='btn btn-small input-inline' onclick="moveRailMap('down')">↓</button>
    	</span>

		<input type='hidden' name='x' value='${su:htmlEscape(x)}'>
		<input type='hidden' name='y' value='${su:htmlEscape(y)}'>
	</p>
</c:if>
</div>
