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
		<title>インフォメーション追加｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
  				<h1>インフォメーション追加</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' action='information_tsuika_touroku' class='form-horizontal'>

					<!-- 入力フィールド -->
					<section>
						<div class='control-group'>
							<label class='control-label'>掲示期間</label>
							<div class='controls'>
								<span class='required'>*</span><input type='text' name='keijiKikanFrom' class='input-small datepicker' value='${su:htmlEscape(keijiKikanFrom)}'>
								～
								<input type='text' name='keijiKikanTo' class='input-small datepicker' value='${su:htmlEscape(keijiKikanTo)}'>
							</div>
						</div>
						<div class='control-group'>
							<label class='control-label'><span class='required'>*</span>掲示内容</label>
							<div class='controls'>
								<textarea name='tsuuchinaiyou' maxlength='3000' class='input-block-level textarea-great'>${su:htmlEscape(tsuuchinaiyou)}</textarea>
							</div>
						</div>
						<input type="hidden" name="sortKbn" value="${su:htmlEscape(sortKbn)}">
						<input type="hidden" name="status" value="${su:htmlEscape(status)}">
						<input type="hidden" name="pageNo" value="${su:htmlEscape(pageNo)}">
					</section>

					<!-- 処理ボタン -->
					<section>
						<button type='submit' class='btn'><i class='icon-hdd'></i> 登録</button>
					</section>
				</form></div><!-- main -->
			</div> <!-- /container -->
			<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
	</body>
</html>
