<%@ page import="eteam.symbol.EteamSymbol"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!DOCTYPE html>
<html lang="ja">
	<head>
		<meta charset="utf-8">
		<title>被代行者選択｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
	</head>

	<body>
    	<div id="wrap">

    		<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<!-- 中身 -->
			<div id="content" class="container">

				<!-- タイトル -->
				<h1>被代行者選択</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id="main"><form action="hi_daikousha_sentaku_sentaku" method="post">
					<input type="hidden" name="selectedUserId" value="">

					<h2>代行しない</h2>
					<div>
						<a href="#" data-userId="">代行せずにログオン</a>
					</div>
					
					<h2>代行する</h2>
					<div class="no-more-tables">
						<table class="table-bordered table-condensed">
							<thead>
								<tr>
									<th>ユーザー名</th>
									<th>社員番号</th>
									<th>所属部門</th>
								</tr>
							</thead>
							<tbody>
<c:forEach var="hiDaikouUser" items="${hiDaikouUserList}">
								<tr>
									<td><a href="#" data-userId="${hiDaikouUser.user_id}">${su:htmlEscape(hiDaikouUser.user_sei)}　${su:htmlEscape(hiDaikouUser.user_mei)}</a></td>
									<td>${su:htmlEscape(hiDaikouUser.shain_no)}</td>
									<td>
	<c:if test="${0 < fn:length(hiDaikouUser.bumonRoleList)}">
	<c:forEach var="j" begin="0" end="${fn:length(hiDaikouUser.bumonRoleList) - 1}">
										${su:htmlEscape(hiDaikouUser.bumonRoleList[j].bumon_full_name)}（${su:htmlEscape(hiDaikouUser.bumonRoleList[j].bumon_role_name)}）
										<c:if test="${j != fn:length(hiDaikouUser.bumonRoleList) - 1}"><br></c:if>
	</c:forEach>
	</c:if>
	<c:if test="${0 == fn:length(hiDaikouUser.bumonRoleList)}">
										（所属なし）
	</c:if>
									</td>
								</tr>
</c:forEach>
							</tbody>
						</table>
					</div>
				</form></div><!-- main -->
			</div><!-- container -->
			<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッターー -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script type="text/javascript">
<!--
$(document).ready(function(){

	//ユーザー名選択
	$("a[data-userId]").click(function(e){
		$("input[name=selectedUserId]").val($(this).attr("data-userId"));
		$("form").submit();
	});
});
-->
		</script>
	</body>
</html>
