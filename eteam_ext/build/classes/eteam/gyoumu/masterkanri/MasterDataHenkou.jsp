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
		<title>マスターデータ変更｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>マスターデータ変更</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id="myForm" method="post" action='master_data_henkou_kakunin' enctype="multipart/form-data" class='form-horizontal'>
					<input type="hidden" name='masterId'   value='${su:htmlEscape(masterId)}'>
					<input type="hidden" name='masterName' value='${su:htmlEscape(masterName)}'>

					<!-- ぱんくず -->
					<section><h3>
						<a href='master_data_ichiran'>一覧</a>
						&gt;
						<a href='master_data_shoukai?masterId=${su:htmlEscape(masterId)}'>照会</a>
					</h3></section>

					<!-- マスター属性表示 -->
					<section>
						<h2>マスターデータ</h2>
						<div>
							<div>
								<div class='control-group'>
									<label class='control-label'>マスターID</label>
									<div class='controls non-input'><b>${su:htmlEscape(masterId)}</b></div>
								</div>
								<div class='control-group'>
									<label class='control-label'>マスター名</label>
									<div class='controls non-input'><b>${su:htmlEscape(masterName)}</b></div>
								</div>
							</div>
						</div>
					</section>

					<!-- アップロード -->
					<section>
						<h2>CSVファイル</h2>
						<p><span class='required'>※ファイルの文字コードはSJISを使用して下さい。</span></p>
						<div class='control-group'>
							<input type='file' name='uploadFile'>
						</div>
					</section>

					<!-- 処理ボタン -->
					<section>
						<button type="submit" class='btn'><i class='icon-upload'></i><b> アップロード</b></button>
					</section>
				</form></div><!-- main -->
			</div><!-- content -->
			<div id="push"></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>
		</script>
	</body>
</html>
