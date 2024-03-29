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
		<title>画像アップロード｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>画像アップロード</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'>
					<form id='uploadForm' class='form-horizontal' enctype="multipart/form-data" method='post'>
						<section>
							<div class='control-group'>
								<label class='control-label' for=''><span class='required'>*</span>画像ファイル</label>
								<div class='controls'>
									<input type='file' name='gazouFile'>
								</div>
							</div>
							<button type='button' class='btn' name='sashikaeButton' onClick="buttonAction('sashikae')"><i class='icon-retweet'></i> 差し替え</button>
							<button type='button' class='btn' name='uploadButton' onClick="buttonAction('upload')"><i class='icon-upload'></i> アップロード</button>
							<button type='button' class='btn' name='sakujoButton' onClick="buttonAction('sakujo')"><i class='icon-remove'></i> 削除</button>
						</section>
						<section>
							<div class='control-group'>
								※ URLをコピーし、ヘルプ編集のツールバー「画像を追加する」のURL欄に貼り付けてください。
							</div>
							<div class='control-group'>
								<table class='table-bordered table-condensed'>
									<thead>
										<tr>
											<th>選択</th>
											<th>ファイル名</th>
											<th>URL</th>
											<th>登録日時</th>
											<th>更新日時</th>
										</tr>
									</thead>
									<tbody id='meisaiList'>
<c:forEach var="record" items="${DataList}">
										<tr>
											<td style="text-align: center;"><input type='checkbox' name="sentakuList" value="${su:htmlEscape(record.serialNo)}"></td>
											<td><a href="gazou_upload_download?downloadSerialNo=${su:htmlEscape(record.serialNo)}">${su:htmlEscape(record.fileName)}</a></td>
											<td>gazou_upload_download?downloadSerialNo=${su:htmlEscape(record.serialNo)}</td>
											<td>${su:htmlEscape(record.tourokuDate)}</td>
											<td>${su:htmlEscape(record.koushinDate)}</td>
										</tr>
</c:forEach>
									</tbody>
								</table>
							</div>
						</section>
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
	if (btnName == "upload") {
		formObject = $("form#uploadForm");
		formObject.attr("action", "gazou_upload_upload");
		formObject.submit();
	} else if (btnName == "sakujo") {
		if ($("[name='sentakuList']:checked").length == 0) {
			alert("削除対象が選択されていません。");
			return false;
		}
		if(window.confirm('選択したファイルを削除してもよろしいですか？')) {
			formObject = $("form#uploadForm");
			formObject.attr("action", "gazou_upload_sakujo");
			formObject.submit();
		}
	} else if(btnName == "sashikae"){
		if ($("[name='sentakuList']:checked").length == 0) {
			alert("差し替え対象が選択されていません。");
			return false;
		}
		if($("[name='sentakuList']:checked").length > 1){
			alert("複数選択はできません。");
			return false;
		}
		if(window.confirm('選択したファイルを差し替えてもよろしいですか？')){
			formObject = $("form#uploadForm");
			formObject.attr("action", "gazou_upload_sashikae");
			formObject.submit();
		}
	}
}
		</script>
	</body>
</html>
