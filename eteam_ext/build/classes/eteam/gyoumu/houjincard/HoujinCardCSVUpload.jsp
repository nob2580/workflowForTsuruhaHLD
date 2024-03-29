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
		<title>法人カードデータインポート｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>法人カードデータインポート</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'>
					<form id='uploadForm' class='form-horizontal' enctype="multipart/form-data" method='post'>
						<div>
							<div class='control-group'>
								<label class='control-label' for=''><span class='required'>*</span>インポートファイル</label>
								<div class='controls'>
								<input type='file' name='uploadFile'>
								</div>
							</div>
							<label class='control-label'><span class='required'>*</span>対象カード</label>
							<div class='controls'>
								<select name='cardShubetsu' class='input-large'>
<c:forEach var="record" items="${cardList}" varStatus="st">
									<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${su:htmlEscape(record.naibu_cd) eq cardShubetsu}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
</c:forEach>
								</select>
							</div>
						</div>
					</form>
					<button type='button' class='btn' id='uploadButton' onClick="buttonAction('upload')"><i class='icon-upload'></i> アップロード</button>
				</div><!-- main -->
				
			</div><!-- content -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<!-- スクリプト -->
		<script style='text/javascript'>
/**
 * イベントボタン押下時のアクションの切り替え
 */
 function buttonAction(btnName) {
	// アップロード
	if(btnName == "upload"){
		formObject = $("form#uploadForm");
		formObject.attr("action","houjin_card_import_upload");
		formObject.submit();
	}
}
		</script>
	</body>
</html>
