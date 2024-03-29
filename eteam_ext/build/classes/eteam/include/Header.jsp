<%@page import="eteam.common.open21.Open21Env"%>
<%@page import="eteam.base.EteamFormatter"%>
<%@page import="eteam.symbol.EteamSymbol"%>
<%@page import="eteam.common.RegAccess"%>
<%@page import="eteam.common.EteamSettingInfo"%>
<%@page import="eteam.common.EteamSettingInfo.Key"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<div id='header' class='navbar navbar-fixed-top'>
	<div class='navbar-inner'>
		<div class='container'>
			<button type='button' name='headerBtn' class='btn btn-navbar' data-toggle='collapse' data-target='.nav-collapse'>
				<span class='icon-bar'></span>
				<span class='icon-bar'></span>
				<span class='icon-bar'></span>
			</button>
				<a class='brand' href='../appl/'><%=EteamSymbol.SYSTEM_NAME%></a>
				<div class='nav navbar-text'><%=EteamFormatter.htmlEscape(EteamSettingInfo.getSettingInfo(Key.HYOUJI_KAISHA_NUM))%></div>
			<div class='nav-collapse collapse'>
				<ul class='nav'>
<c:if test='<%= RegAccess.checkEnableZaimuKyotenOption() %>'>
					<li><a id='kyotenMenu' href='#' onClick='openKyotenMenu()' class='navbar-link'>Web拠点入力メニュー</a></li>
</c:if>
					<li><a href='/eteam/static/pdf/manual_<%=Open21Env.getVersion()%>.pdf' target='manual' class='navbar-link'>マニュアル</a></li>
<c:if test="${null != helpGamenId && null == forceGamen}">
					<li><a href='#' onClick="window.open('help_viewer?gamenId=${helpGamenId}')" class='navbar-link'>画面別ヘルプ</a></li>
</c:if>
				</ul>
				<ul class='nav pull-right'>
<c:if test="${sessionScope.user.enableRoleSentaku && null == forceGamen}">
					<li><a href='role_sentaku' class='navbar-link'>ロール選択</a></li>
</c:if>
					<li><a href="logout" class="navbar-link">ログアウト</a></li>
				</ul>
				<p class='navbar-text pull-right'>
					${su:htmlEscape(sessionScope.user.displayUserName)}
<c:if test="${null != sessionScope.user.gyoumuRoleId}">
					(${su:htmlEscape(sessionScope.user.gyoumuRoleName)})
</c:if>
					&nbsp;&nbsp;&nbsp;
				</p>
			</div><!--/.nav-collapse -->
		</div>
	</div>
</div>
<!-- スクリプト -->
<script style='text/javascript'>
var urlBase = location.protocol + "//" + location.host
var path = location.pathname.split('/');	// 例："/eteam/schemaname/appl/" → ["", "eteam", "schemaname", "appl" ""]
var url = urlBase + "/kyoten/" + path[2] + "/appl/"
function openKyotenMenu(){
	window.open(url, '_self');
}
</script>
