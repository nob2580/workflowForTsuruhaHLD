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
		<title>インフォメーション参照｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
			<div id='content' class='container'>

				<!-- タイトル -->
  				<h1>インフォメーション参照</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main' class='form-horizontal'>

					<!-- 表示 -->
					<section>
						<div class='control-group'>
							<label class='control-label'>掲示期間</label>
							<div class='controls'>
								<input type='text' name='keijiKikanFrom' value='${su:htmlEscape(keijiKikanFrom)}' class='input-small datepicker input-inline' disabled>
								～
								<input type='text' name='keijiKikanTo' value='${su:htmlEscape(keijiKikanTo)}' class='input-small datepicker input-inline' disabled>
							</div>
						</div>
						<div class='control-group'>
						<label class='control-label'>掲示内容</label>
							<div class='controls non-input thumbnail' style="word-wrap:break-word; word-break:break-all;">
								${su:htmlEscapeBrLink(tsuuchinaiyou)}
							</div>
						</div>
					</section>
				</div><!-- main -->
			</div> <!-- /container -->
			<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		<%@ include file="/jsp/eteam/include/Script.jsp" %>

	</body>
</html>
