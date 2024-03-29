<%@ page import="eteam.symbol.EteamSymbol"%>
<%@ page import="eteam.common.RegAccess"%>
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
		<title>ユーザー一括登録確認｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>ユーザー一括登録確認</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='tourokuForm' class='form-horizontal' method='post' enctype="multipart/form-data">

					<!-- CSVファイル（ユーザー情報） -->
					<section>
						<h2>CSVファイル（ユーザー情報）</h2>
						<b>${su:htmlEscape(csvFileNameUserInfo)}</b>
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
										<th>ユーザーID</th>
										<th>社員番号</th>
										<th>ユーザー名</th>
										<th>ユーザー姓</th>
										<th>メールアドレス</th>
										<th>有効期限開始日</th>
										<th>有効期限終了日</th>
										<th>代理起票可能フラグ</th>
										<th>法人カード利用</th>
										<th>法人カード利用識別番号</th>
										<th>セキュリティパターン</th>
										<th>セキュリティワークフロー限定フラグ</th>
										<th>承認ルート変更権限レベル</th>
										<th>マル秘設定権限</th>
										<th>マル秘解除権限</th>
<c:if test='<%= RegAccess.checkEnableZaimuKyotenOption() %>'>
										<th>拠点入力のみ使用</th>
</c:if>
									</tr>
								</thead>
								<tbody>
<c:forEach var="userInfo" items="${userInfoList}" varStatus="i">
									<tr>
										<td><nobr>${userInfo.number}</nobr></td>
	<c:if test='${status eq "End"}'>
		<c:if test='${fn:length(errorList) eq 0}'>
										<td><nobr>登録成功</nobr></td>
		</c:if>
		<c:if test='${fn:length(errorList) ne 0}'>
			<c:if test='${userInfo.errorFlg eq true}'>
										<td><nobr>登録失敗</nobr></td>
			</c:if>
			<c:if test='${userInfo.errorFlg eq false}'>
										<td><nobr>未登録</nobr></td>
			</c:if>
		</c:if>
				 
	</c:if>
										<td><nobr>${su:htmlEscape(userInfo.userId)}</nobr></td>
										<td><nobr>${su:htmlEscape(userInfo.shainNo)}</nobr></td>
										<td><nobr>${su:htmlEscape(userInfo.userSei)}</nobr></td>
										<td><nobr>${su:htmlEscape(userInfo.userMei)}</nobr></td>
										<td><nobr>${su:htmlEscape(userInfo.mailAddress)}</nobr></td>
										<td><nobr>${su:htmlEscape(userInfo.yuukouKigenFrom)}</nobr></td>
										<td><nobr>${su:htmlEscape(userInfo.yuukouKigenTo)}</nobr></td>
										<td><nobr>${su:htmlEscape(userInfo.dairikihyouFlg)}</nobr></td>
										<td><nobr>${su:htmlEscape(userInfo.houjinCardRiyouFlag)}</nobr></td>
										<td><nobr>${su:htmlEscape(userInfo.houjinCardShikibetsuyouNum)}</nobr></td>
										<td><nobr>${su:htmlEscape(userInfo.securityPattern)}</nobr></td>
										<td><nobr>${su:htmlEscape(userInfo.securityWfonlyFlg)}</nobr></td>
										<td><nobr>${su:htmlEscape(userInfo.shouninRouteHenkouLevel)}</nobr></td>
										<td><nobr>${su:htmlEscape(userInfo.maruhiKengenFlg)}</nobr></td>
										<td><nobr>${su:htmlEscape(userInfo.maruhiKaijyoFlg)}</nobr></td>
<c:if test='<%= RegAccess.checkEnableZaimuKyotenOption() %>'>
										<td><nobr>${su:htmlEscape(userInfo.zaimuKyotenNyuryokuOnlyFlg)}</nobr></td>
</c:if>	
									</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
					</section>
					
					<br>
					
					<!-- CSVファイル（所属部門割り当て） -->
					<section>
						<h2>CSVファイル（所属部門割り当て）</h2>
						<b>${su:htmlEscape(csvFileNameShozokuBumonWariate)}</b>
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
										<th>所属部門コード</th>
										<th>部門ロールID</th>
										<th>ユーザーID</th>
										<th>代表負担部門コード</th>
										<th>有効期限開始日</th>
										<th>有効期限終了日</th>
										<th>表示順</th>
									</tr>
								</thead>
								<tbody>
<c:forEach var="shozokuBumonWariate" items="${shozokuBumonWariateList}" varStatus="i">
									<tr>
										<td><nobr>${shozokuBumonWariate.number}</nobr></td>
	<c:if test='${status eq "End"}'>
		<c:if test='${fn:length(errorList) eq 0}'>
										<td><nobr>登録成功</nobr></td>
		</c:if>
		<c:if test='${fn:length(errorList) ne 0}'>
			<c:if test='${shozokuBumonWariate.errorFlg eq true}'>
										<td><nobr>登録失敗</nobr></td>
			</c:if>
			<c:if test='${shozokuBumonWariate.errorFlg eq false}'>
										<td><nobr>未登録</nobr></td>
			</c:if>
		</c:if>
				 
	</c:if>
										<td><nobr>${su:htmlEscape(shozokuBumonWariate.bumonCd)}</nobr></td>
										<td><nobr>${su:htmlEscape(shozokuBumonWariate.bumonRoleId)}</nobr></td>
										<td><nobr>${su:htmlEscape(shozokuBumonWariate.userId)}</nobr></td>
										<td><nobr>${su:htmlEscape(shozokuBumonWariate.daihyouFutanBumonCd)}</nobr></td>
										<td><nobr>${su:htmlEscape(shozokuBumonWariate.yuukouKigenFrom)}</nobr></td>
										<td><nobr>${su:htmlEscape(shozokuBumonWariate.yuukouKigenTo)}</nobr></td>
										<td><nobr>${su:htmlEscape(shozokuBumonWariate.hyoujiJun)}</nobr></td>
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
								<button type='button' class='btn' onClick="location.href='user_ikkatsu_touroku'" ><i class='icon-arrow-left'></i> 戻る</button>
	</c:if>
	<c:if test='${fn:length(errorList) ne 0}'>
								<button type='button' class='btn' onClick="location.href='user_ikkatsu_touroku'" ><i class='icon-arrow-left'></i> 戻る</button>
	</c:if>
</c:if>
<c:if test='${status eq "Run"}'>
							<button type='button' class='btn' onClick="location.href='ikkatsu_touroku_user_csv_upload_kakunin?status=Run'"><i class='icon-search'></i> 画面更新</button>
</c:if>
<c:if test='${status eq "End"}'>
							<button type='button' class='btn' onClick="location.href='user_ikkatsu_touroku'" ><i class='icon-arrow-left'></i> 戻る</button>
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
		formObject.attr("action","ikkatsu_touroku_user_csv_upload_kakunin_touroku");
		formObject.submit();
	}
}
		</script>
	</body>
</html>
