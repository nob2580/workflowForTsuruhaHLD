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
		<title>ユーザー定義届書一覧｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>ユーザー定義届書一覧</h1>
				
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'>

					<!-- ボタン郡 -->
					<div class='control-group'>
						<button type='button' onClick="location.href='kani_todoke_tsuika'" class='btn'><i class='icon-plus'></i> 追加</button>
					</div>

					<section>
						<div id="List" class='no-more-tables'>
							<table class="table-bordered table-condensed">
								<thead>
									<tr>
										<th>伝票種別</th>
										<th>内容</th>
										<th style='width:75px;'>複製</th>
									</tr>
								</thead>
								<tbody>
<c:forEach var="record" items="${list}">
									<tr>
										<td><a href='kani_todoke_henkou?denpyouKbn=${su:htmlEscape(record.denpyou_kbn)}&amp;version=${su:htmlEscape(record.version)}'>${su:htmlEscape(record.denpyou_shubetsu)}</a></td>
										<td style="word-wrap:break-word; word-break:break-all;">${su:htmlEscapeBrLink(record.naiyou)}</td>
										<td>
											<a href='kani_todoke_tsuika?copyDenpyouKbn=${su:htmlEscape(record.denpyou_kbn)}&amp;copyVersion=${su:htmlEscape(record.version)}'>
												<button type='button' name='copy' class='btn'><i class='icon-repeat'></i> 複製</button>
											</a>
										</td>
									</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
					</section>


				</div><!-- main -->
				
			</div><!-- content -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
<script style='text/javascript'>

</script>
	</body>
</html>
