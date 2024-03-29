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
		<title>法人カードデータインポート確認｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
	</head>
	<style type='text/css'>
<!--
span.duplicateAlert {
	color: #FF0000;
} 
-->
	</style>

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
				
				<!-- アラート -->
<c:if test='${duplicateHyoujiFlg}'><span class=duplicateAlert>${su:htmlEscape(alertMsg)}</span></c:if>

				<!-- メイン -->
				<div id='main'><form id='tourokuForm' class='form-horizontal' method='post' enctype="multipart/form-data">

					<!-- CSVファイル -->
					<section>
						<h2>インポートファイル</h2>
						<b>${su:htmlEscape(csvFileName)}</b>
					</section>

					<section>
						<h2>登録内容</h2>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>#</th>

<c:if test='${sumi}'>
										<th>処理結果</th>
</c:if>
<c:if test='${duplicateHyoujiFlg}'>
										<th>重複あり</th>
										<th>取込対象</th>
</c:if>

<!-- リストヘッダ行の出力 -->
<c:forEach var="headerList" items="${hyoujiHeaderList}" varStatus="ii">
										<th>${su:htmlEscape(headerList)}</th>
</c:forEach>
									</tr>
								</thead>
								<tbody>
<c:forEach var="rireki" items="${rirekiList}" varStatus="i">
									<tr>
										<td><nobr>${rireki.number}</nobr></td>
	<c:if test='${sumi}'>
		<c:if test='${fn:length(errorList) eq 0}'>
			<c:if test='${rireki.mitorikomiFlg eq false}'>
										<td><nobr>登録成功</nobr></td>
			</c:if>
			<c:if test='${rireki.mitorikomiFlg eq true}'>
										<td><nobr>未登録(重複)</nobr></td>
			</c:if>
		</c:if>
		<c:if test='${fn:length(errorList) ne 0}'>
			<c:if test='${rireki.errorFlg eq true}'>
										<td><nobr>登録失敗</nobr></td>
			</c:if>
			<c:if test='${rireki.errorFlg eq false}'>
										<td><nobr>未登録</nobr></td>
			</c:if>
		</c:if>
				 
	</c:if>
	<c:if test='${duplicateHyoujiFlg}'>
										<td><c:if test='${rireki.duplicateFlg}'><span class=duplicateAlert>●</span></c:if></td>
										<td><input type='checkbox' name='torikomiTaishouCheck'  <c:if test='${torikomiTaishouFlg[i.index] eq "1"}'>checked</c:if> <c:if test='${not rireki.duplicateFlg}'>style='display:none'</c:if>></td>
	</c:if>
										<input type='hidden' name='torikomiTaishouFlg' value='${su:htmlEscape(torikomiTaishouFlg[i.index])}'>
<!-- リストデータ行の出力 -->
	<c:forEach var="hyoujiData" items="${hyoujiDataList[i.index]}" varStatus="k">
										<td><nobr>${su:htmlEscape(hyoujiData)}</nobr></td>
	</c:forEach>
									</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
					</section>

					<section>
<c:if test='${(not sumi) && (fn:length(errorList) eq 0)}'>
							<button type='button' class='btn' name='okButton' onClick="buttonAction('touroku')"><i class='icon-hdd'></i> 登録</button>
</c:if>
							<button type='button' class='btn' onClick="location.href='houjin_card_import'" ><i class='icon-arrow-left'></i> 戻る</button>
					</section>
				</form></div><!-- main -->
				<!-- Modal -->
				<div id='dialog'></div>
			</div><!-- content -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<!-- スクリプト -->
		<script style='text/javascript'>
		$(document).ready(function(){
			/**
			 * 取込対象チェックボックスの値切替
			 */
			 $("input[name='torikomiTaishouCheck']").change(function(){
				 if($(this).prop("checked")){
					 $(this).parents("tr").find("input[name='torikomiTaishouFlg']").val("1");
				 }else{
					 $(this).parents("tr").find("input[name='torikomiTaishouFlg']").val("0");
				 }
			 })
		})
/**
 * イベントボタン押下時のアクションの切り替え
 */
function buttonAction(btnName) {
	// アップロード
	if(btnName == "touroku"){
		formObject = $("form#tourokuForm");
		formObject.attr("action","houjin_card_import_kakunin_touroku");
		formObject.submit();
	}
}
		</script>
	</body>
</html>
