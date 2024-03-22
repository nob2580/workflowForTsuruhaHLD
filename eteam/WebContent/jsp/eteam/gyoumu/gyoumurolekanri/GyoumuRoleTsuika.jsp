<%@ page import="eteam.symbol.EteamSymbol"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!DOCTYPE html>
<html lang='ja'>
	<head>
		<meta charset='utf-8'>
		<title>業務ロール追加｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
<!--
-->
		</style>
	</head>
	<body>
		<div id='wrap'>

			<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
				<h1>業務ロール追加</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form method='post' action='gyoumu_role_tsuika_touroku' class='form-horizontal'>

					<!-- 入力フィールド -->
					<section>
						<div class='control-group'>
							<label class='control-label'><span class='required'>*</span>業務ロール名</label>
							<div class='controls'>
								<input type='text' name='gyoumuRoleName' maxlength='20' value='${su:htmlEscape(gyoumuRoleName)}'>
							</div>
						</div>
					</section>

					<!-- 権限指定 -->
					<section>
						<h2>可能操作</h2>
						<div class='control-group'>
							<label class='control-label'>ワークフロー</label>
							<div class='controls'>
								<input type='checkbox' name='workflow' value='1' <c:if test='${"1" eq workflow}'>checked</c:if>/>
							</div>
						</div>
						<div class='control-group'>
							<label class='control-label'>会社設定</label>
							<div class='controls'>
								<input type='checkbox' name='kaishaSettei' value='1' <c:if test='${"1" eq kaishaSettei}'>checked</c:if>>
							</div>
						</div>
						<div class='control-group'>
							<label class='control-label'>経理処理</label>
							<div class='controls'>
								<input type='checkbox' name='keiriShori' value='1' <c:if test='${"1" eq keiriShori}'>checked</c:if>>
							</div>
						</div>
					</section>

					<!-- 処理ボタン -->
					<section>
						<button type="submit" class='btn'><i class='icon-hdd'></i> 登録</button>
					</section>
				</form></div>
			</div><!-- content -->
			<div id="push"></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>
<!--
-->
		</script>
	</body>
</html>
