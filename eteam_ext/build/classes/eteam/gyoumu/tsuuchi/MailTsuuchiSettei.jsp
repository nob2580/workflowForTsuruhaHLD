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
		<title>メール通知設定｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>メール通知設定</h1>
				
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				
				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' action='mail_tsuuchi_settei_touroku' class='form-horizontal'>

					<!-- 入力フィールド -->
					<section>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th colspan="3" align="left">通知種類</th>
										<th>送信有無</th>
									</tr>
								</thead>
								<tbody id='mailSendChkList'>
									<tr>
										<td colspan="3">滞留通知を行う（固定時刻）</td>
										<td align="center"><input type="checkbox" id='tairyuuTsuuchi' name='tairyuuTsuuchi' value='1' <c:if test='${"1" eq tairyuuTsuuchi}'>checked</c:if>></input></td>
									</tr>
									<tr>
										<td rowspan="7" valign="top">リアルタイム通知を行う。</td>
										<td rowspan="3" valign="top">要処理</td>
										<td>自分に伝票が回ってきた。</td>
										<td align="center"><input type="checkbox" id='realTimeMyTurnShounin' name='realTimeMyTurnShounin' value='1' <c:if test='${"1" eq realTimeMyTurnShounin}'>checked</c:if>></input></td>
									</tr>
									<tr>
										<td>自分が申請(承認)した伝票が差戻しされた。</td>
										<td align="center"><input type="checkbox" id='realTimeMyTurnSashimodoshi' name='realTimeMyTurnSashimodoshi' value='1' <c:if test='${"1" eq realTimeMyTurnSashimodoshi}'>checked</c:if>></input></td>
									</tr>
									<tr>
										<td>自分の所属する合議部署に伝票が回ってきた。</td>
										<td align="center"><input type="checkbox" id='realTimeMyTurnGougiShounin' name='realTimeMyTurnGougiShounin' value='1' <c:if test='${"1" eq realTimeMyTurnGougiShounin}'>checked</c:if>></input></td>
									</tr>
									<tr>
										<td rowspan="4" valign="top">通知</td>
										<td>自分が申請した伝票が最終承認された。</td>
										<td align="center"><input type="checkbox" id='realTimeMyDenpyouSaisyuushounin' name='realTimeMyDenpyouSaisyuushounin' value='1' <c:if test='${"1" eq realTimeMyDenpyouSaisyuushounin}'>checked</c:if>></input></td>
									</tr>
									<tr>
										<td>自分が申請した伝票が否認された。</td>
										<td align="center"><input type="checkbox" id='realTimeMyDenpyouHinin' name='realTimeMyDenpyouHinin' value='1' <c:if test='${"1" eq realTimeMyDenpyouHinin}'>checked</c:if>></input></td>
									</tr>
									<tr>
										<td>自分が承認ルートに含まれる伝票が最終承認された。</td>
										<td align="center"><input type="checkbox" id='realTimeSaisyuuShounin' name='realTimeSaisyuuShounin' value='1' <c:if test='${"1" eq realTimeSaisyuuShounin}'>checked</c:if>></input></td>
									</tr>
									<tr>
										<td>自分が承認ルートに含まれる伝票が否認された。</td>
										<td align="center"><input type="checkbox" id='realTimeHinin' name='realTimeHinin' value='1' <c:if test='${"1" eq realTimeHinin}'>checked</c:if>></input></td>
									</tr>
								</tbody>
							</table>
						</div>
					</section>

					<!-- 処理ボタン -->
					<section>
						<button type='submit' class='btn'><i class='icon-hdd'></i> 登録</button>
					</section>
				</form></div>
				
			</div><!-- content -->
			<div id="push"></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>
		</script>
	</body>
</html>
