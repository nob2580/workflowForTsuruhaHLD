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
		<meta charset="utf-8">
		<title>ガイダンスメンテナンス｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
	</head>

	<body>
    	<div id='wrap'>

    		<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
				<h1>ガイダンスメンテナンス</h1>
	
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
	
				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' action='guidance_maintenance_jisyou_tsuika_touroku' class='form-horizontal'>
					<input type='hidden' name='midashiId' value='${midashiId}'/>

					<!-- ぱんくず -->
					<section>
						<h3><a href='guidance_maintenance'>ガイダンスメンテナンス</a> &gt; <a href='guidance_maintenance_midashi_hensyu?midashiId=${midashiId}'>${su:htmlEscape(midashiName)}</a> &gt; 事象追加</h3>
					</section>

					<!-- 入力フィールド -->
					<section>
						<h2>事象設定</h2>
						<div>
							<div class='control-group'>
								<label class="control-label"><span class='required'>*</span>事象名</label>
								<div class='controls'>
									<input type='text' name='jishouName' class=' input-xxlarge' maxlength='20' value='${su:htmlEscape(jishouName)}'>
								</div>
							</div>
						</div>
					</section>

					<!-- 処理ボタン -->
					<section>
						<button type='button' id='tourokuButton' class='btn'><i class='icon-hdd'></i> 登録</button>
					</section>
					<!-- Modal -->
					<div id='dialog'></div>
				</form></div><!-- main -->
			</div><!-- content -->
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<!-- スクリプト -->
		<script style='text/javascript'>
$(document).ready(function(){
	
	//登録ボタン押下
		$("#tourokuButton").click(function(){
		$("#myForm").submit();
	});
});
		</script>
	</body>
</html>
