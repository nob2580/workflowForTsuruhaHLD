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
		<title>支払予定総括表出力｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
  				<h1>支払予定総括表 出力</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main' class='form-horizontal'><form id='myForm'>
					<input type='hidden' name="seigyoUserId" value='${su:htmlEscape(sessionScope.user.seigyoUserId)}'>

					<section>
						<h2>出力対象</h2>
						<div id="syutsuryokuTaishou" class='no-more-tables'>
							<div class='control-group'>
								<label class='control-label'>計上日</label>
								<div class='controls'>
									<input type='text' name='keijouBiFrom' class='input-small datepicker input-inline' value='${su:htmlEscape(keijouBiFrom)}'>
									～
									<input type='text' name='keijouBiTo' class='input-small datepicker input-inline' value='${su:htmlEscape(keijouBiTo)}'>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>支払予定日</label>
								<div class='controls'>
									<input type='text' name='shiharaiYoteiBiFrom' class='input-small datepicker input-inline' value='${su:htmlEscape(shiharaiYoteiBiFrom)}'>
									～
									<input type='text' name='shiharaiYoteiBiTo' class='input-small datepicker input-inline' value='${su:htmlEscape(shiharaiYoteiBiTo)}'>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>最終承認済</label>
								<div class="controls">
									<input type='checkbox' name='saisyuShouninZumiCheck'/>
								</div>
							</div>
						</div>
						<div class='blank'></div>
						<div>
							<button type='button' class='btn' id='pdfBtn'><i class='icon-print'></i> 出力</button>
						</div>
					</section>

				<!-- Modal -->
				<div id='dialog'></div>
				</form></div><!-- main -->
			</div> <!-- /container -->
			<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<!-- スクリプト -->
		<script style='text/javascript'>

$(document).ready(function(){
	//条件クリア時
	var userName = $("input[name=name]").val();
	var userId = $("input[name=id]").val();
	
	/*
	 * 帳票出力
	 */
	$("#pdfBtn").click(function(){
		$("#myForm").attr("action" , "shiharai_yotei_soukatsuhyo_output_pdf");
		$("#myForm").attr("method" , "post");
		$("#myForm").submit();
	});
	
	//デフォルト
	$("#syutsuryokuTaishou").find("input[type='checkbox']").prop('checked', true);
	
});
		</script>
	</body>
</html>
