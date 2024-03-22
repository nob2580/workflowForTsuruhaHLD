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
		<title>部門一括登録｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>部門一括登録</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'>
				<form id='uploadForm' class='form-horizontal' enctype="multipart/form-data" method='post'>
					<div>
						<div class='control-group'>
							<label class='control-label' for=''><span class='required'>*</span>CSVファイル</label>
							<div class='controls'>
								<button type='button' class='btn' id='downloadButton' onClick="buttonAction('download')"><i class='icon-download'></i> ダウンロード</button>
								<input type='file' name='uploadFile'>
							</div>
						</div>
					</div>
					<div>
						<button type='button' class='btn' id='uploadButton' onClick="buttonAction('upload')"><i class='icon-upload'></i> アップロード</button>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button type='button' class='btn' id='deleteButton' onClick="buttonAction('delete')"><i class='icon-remove'></i> 全件削除</button>
					</div>
				</form>
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
		formObject.attr("action","bumon_ikkatsu_touroku_upload");
		formObject.submit();
	}
	// ダウンロード
	if(btnName == "download"){
		formObject = $("form#uploadForm");
		formObject.attr("action","bumon_ikkatsu_download");
		formObject.submit();
	}
	// 全件削除
	if(btnName == "delete"){
		if(window.confirm('全部門を削除してもよろしいですか？')) {
			if(window.confirm('削除したデータは元に戻せません。本当によろしいですか？')) {
				formObject = $("form#uploadForm");
				formObject.attr("action","bumon_ikkatsu_sakujo");
				formObject.submit();
			}
		}
	}
}
		</script>
	</body>
</html>
