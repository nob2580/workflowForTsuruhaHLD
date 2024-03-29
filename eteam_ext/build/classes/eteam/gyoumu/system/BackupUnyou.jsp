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
		<title>バックアップ運用｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
	</head>
	<body>
		<div id='wrap'>
			<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>
			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
				<h1>バックアップ運用</h1>
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' class='form-horizontal'>
					<section>
						<input type='hidden' name='mode' value=''>
						<input type='hidden' name='selectedVersionFlg' value=''>
						<button type='button' class='btn' name='commentBtn' data-code='sakusei' data-name='バックアップ作成' href='#commentModal' role='button' data-toggle='modal'><i class='icon-plus'></i> 作成</button>
						<button type='button' class='btn' onClick="eventBtn('fukugen')"><i class='icon-repeat'></i> 復元</button>
						<button type='button' class='btn' onClick="eventBtn('sakujo')"><i class='icon-remove'></i> 削除</button>
					</section>
					<!-- ファイル一覧 -->
					<section>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>選択</th>
										<th>ファイル名</th>
										<th>作成日時</th>
										<th>コメント</th>
									</tr>
								</thead>
								<tbody id='meisaiList'>
<c:forEach var="record" items="${DataList}">
									<tr id='dataList'>
										<td style="text-align: center;"><input type='checkbox' name="sentakuList" value="${su:htmlEscape(record.recordId)}"></td>
										<td>${su:htmlEscape(record.fileName)}</td>
										<td>${su:htmlEscape(record.tourokuDay)}</td>
										<td>${su:htmlEscape(record.backupComment)}</td>
									</tr>
									<input type='hidden' name='existVersionFlg' value='${su:htmlEscape(record.existVersionFlg)}'>
</c:forEach>
								</tbody>
							</table>
						</div>
					</section>
					<div id='commentModal' class='modal hide fade' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'></div>
				</form></div><!-- main -->
			</div><!-- /container -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>
/**
 * イベントボタン押下時のアクションの切り替え
 */
 function eventBtn(eventName) {
	var formObject = document.getElementById("myForm");
	
	switch (eventName) {
	case 'sakusei':
		if(window.confirm('バックアップファイルを作成します。よろしいでしょうか？')) {
			formObject.action = 'backup_unyou_renkei';
			$("input[name=mode]").val("0");
			$(formObject).submit();
		}
		break;
	case 'fukugen':
		if($("[name='sentakuList']:checked").length == 0){
			alert("復元対象が選択されていません。");
			break;
		}
		if($("[name='sentakuList']:checked").length > 1){
			alert("復元対象が複数選択されています。");
			break;
		}
		var checkedVersionFlg = $("[name='sentakuList']:checked").closest("#dataList").next().val();
		if(checkedVersionFlg == "true"){
			if(window.confirm('選択したファイルで復元します。よろしいでしょうか？')){
				formObject.action = 'backup_unyou_renkei';
				$("input[name=mode]").val("1");
				$("input[name=selectedVersionFlg]").val(checkedVersionFlg);
				$(formObject).submit();
			}
			break;
		}else{
			if(window.confirm('選択したファイルのバージョン情報が見つかりません。復元してよろしいですか？')) {
				formObject.action = 'backup_unyou_renkei';
				$("input[name=mode]").val("1");
				$("input[name=selectedVersionFlg]").val(checkedVersionFlg);
				$(formObject).submit();
			}
		}
		break;
	case 'sakujo':
		if($("[name='sentakuList']:checked").length == 0){
			alert("削除対象が選択されていません。");
			break;
		}
		if(window.confirm('選択したファイルを削除してもよろしいですか？')) {
			formObject.action = 'backup_unyou_sakujo';
			$(formObject).submit();
		}
		break;
	}
}

 /** 作成ボタン押下時Function */
 $('button[name=commentBtn]').click(function(event) {
		var eventCode = $(this).attr("data-code");
		var eventName = $(this).attr("data-name");

		$("#commentModal").empty();
		$("#commentModal").append("<div class='modal-header'><button type='button' class='close' data-dismiss='modal' aria-hidden='true'>×<\/button><h3 id='myModalLabel'>" + eventName +"<\/h3><\/div><div class='modal-body'><p>コメント（任意）<\/p><input type='text' class='input-block-level' name='comment' maxlength='20' placeholder='コメントを入力してください。(20字)'><\/input><\/div><div class='modal-footer'><button class='btn' data-dismiss='modal' aria-hidden='true'>閉じる<\/button><button type='button' class='btn btn-primary' onClick='eventBtn(\"" + eventCode + "\")'>" + eventName + "<\/button><\/div>");
	});
		</script>
	</body>
</html>
