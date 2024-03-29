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
		<title>部門推奨ルート一括登録確認｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>部門推奨ルート一括登録確認</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='tourokuForm' class='form-horizontal' method='post' enctype="multipart/form-data">

					<!-- CSVファイル（部門推奨ルート親） -->
					<section>
						<h2>CSVファイル（部門推奨ルート親）</h2>
						<b>${su:htmlEscape(csvFileNameOyaInfo)}</b>
					</section>

					<section>
						<h2>登録内容</h2>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>#</th>
<c:if test='${status eq "End"}'>
										<th>処理結果</th>
</c:if>
										<th>伝票区分</th>
										<th>部門コード</th>
										<th>枝番号</th>
										<th>デフォルト設定</th>
										<th>取引コード</th>
										<th>金額開始</th>
										<th>金額終了</th>
										<th>有効期限開始日</th>
										<th>有効期限終了日</th>
									</tr>
								</thead>
								<tbody>
<c:forEach var="oyaInfo" items="${oyaInfoList}" varStatus="i">
									<tr>
										<td><nobr>${oyaInfo.number}</nobr></td>
	<c:if test='${status eq "End"}'>
		<c:if test='${fn:length(errorList) eq 0}'>
										<td><nobr>登録成功</nobr></td>
		</c:if>
		<c:if test='${fn:length(errorList) ne 0}'>
			<c:if test='${oyaInfo.errorFlg eq true}'>
										<td><nobr>登録失敗</nobr></td>
			</c:if>
			<c:if test='${oyaInfo.errorFlg eq false}'>
										<td><nobr>未登録</nobr></td>
			</c:if>
		</c:if>
				 
	</c:if>
										<td><nobr>${su:htmlEscape(oyaInfo.denpyouKbn)}</nobr></td>
										<td><nobr>${su:htmlEscape(oyaInfo.bumonCd)}</nobr></td>
										<td><nobr>${su:htmlEscape(oyaInfo.edaNo)}</nobr></td>
										<td><nobr>${su:htmlEscape(oyaInfo.defaultFlg)}</nobr></td>
										<td><nobr>${su:htmlEscape(oyaInfo.shiwakeEdaNoHyouji)}</nobr></td>
										<td><nobr>${su:htmlEscape(oyaInfo.kingakuFrom)}</nobr></td>
										<td><nobr>${su:htmlEscape(oyaInfo.kingakuTo)}</nobr></td>
										<td><nobr>${su:htmlEscape(oyaInfo.yuukouKigenFrom)}</nobr></td>
										<td><nobr>${su:htmlEscape(oyaInfo.yuukouKigenTo)}</nobr></td>
									</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
					</section>
					
					<br>
					
					<!-- CSVファイル（部門推奨ルート子） -->
					<section>
						<h2>CSVファイル（部門推奨ルート子）</h2>
						<b>${su:htmlEscape(csvFileNameKoInfo)}</b>
					</section>

					<section>
						<h2>登録内容</h2>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>#</th>
<c:if test='${status eq "End"}'>
										<th>処理結果</th>
</c:if>
										<th>伝票区分</th>
										<th>部門コード</th>
										<th>枝番号</th>
										<th>枝々番号</th>
										<th>部門ロールID</th>
										<th>承認処理権限番号</th>
										<th>合議パターン番号</th>
										<th>合議枝番</th>
									</tr>
								</thead>
								<tbody>
<c:forEach var="koInfo" items="${koInfoList}" varStatus="i">
									<tr>
										<td><nobr>${koInfo.number}</nobr></td>
	<c:if test='${status eq "End"}'>
		<c:if test='${fn:length(errorList) eq 0}'>
										<td><nobr>登録成功</nobr></td>
		</c:if>
		<c:if test='${fn:length(errorList) ne 0}'>
			<c:if test='${koInfo.errorFlg eq true}'>
										<td><nobr>登録失敗</nobr></td>
			</c:if>
			<c:if test='${koInfo.errorFlg eq false}'>
										<td><nobr>未登録</nobr></td>
			</c:if>
		</c:if>
				 
	</c:if>
										<td><nobr>${su:htmlEscape(koInfo.denpyouKbn)}</nobr></td>
										<td><nobr>${su:htmlEscape(koInfo.bumonCd)}</nobr></td>
										<td><nobr>${su:htmlEscape(koInfo.edaNo)}</nobr></td>
										<td><nobr>${su:htmlEscape(koInfo.edaedaNo)}</nobr></td>
										<td><nobr>${su:htmlEscape(koInfo.bumonRoleId)}</nobr></td>
										<td><nobr>${su:htmlEscape(koInfo.shouninhoriKengenNo)}</nobr></td>
										<td><nobr>${su:htmlEscape(koInfo.gougiPatternNo)}</nobr></td>
										<td><nobr>${su:htmlEscape(koInfo.gougiEdano)}</nobr></td>
									</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
					</section>
					
					
					
					
					<section>
<c:if test='${status eq "Init"}'>
	<c:if test='${fn:length(errorList) eq 0}'>
								<button type='button' class='btn' name='okButton' onClick="buttonAction('touroku')"><i class='icon-hdd'></i> 登録</button>
								<button type='button' class='btn' onClick="location.href='bumon_suishou_route_ikkatsu_touroku'" ><i class='icon-arrow-left'></i> 戻る</button>
	</c:if>
	<c:if test='${fn:length(errorList) ne 0}'>
								<button type='button' class='btn' onClick="location.href='bumon_suishou_route_ikkatsu_touroku'" ><i class='icon-arrow-left'></i> 戻る</button>
	</c:if>
</c:if>
<c:if test='${status eq "Run"}'>
							<button type='button' class='btn' onClick="location.href='ikkatsu_touroku_bumon_suishou_route_csv_upload_kakunin?status=Run'"><i class='icon-search'></i> 画面更新</button>
</c:if>
<c:if test='${status eq "End"}'>
							<button type='button' class='btn' onClick="location.href='bumon_suishou_route_ikkatsu_touroku'" ><i class='icon-arrow-left'></i> 戻る</button>
</c:if>
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
/**
 * イベントボタン押下時のアクションの切り替え
 */
function buttonAction(btnName) {
	// アップロード
	if(btnName == "touroku"){
		formObject = $("form#tourokuForm");
		formObject.attr("action","ikkatsu_touroku_bumon_suishou_route_csv_upload_kakunin_touroku");
		formObject.submit();
	}
}
		</script>
	</body>
</html>
