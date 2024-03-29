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
		<meta charset="utf-8">
		<title>ガイダンスメンテナンス｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>ガイダンスメンテナンス</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' action='guidance_maintenance_koushin' class='form-horizontal'>

					<!-- 一覧 -->
					<section>
						<h2>見出し一覧</h2>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>#</th>
										<th>削除対象</th>
										<th>見出し名</th>
										<th></th>
									</tr>
								</thead>
								<tbody id='meisaiList'>
<c:forEach var="record" items="${list}">
										<tr>
											<td align="center"><span class='number'></span></td>
											<td style='text-align:center;'>
												<input type='checkbox' name='deleteMidashiId' value='${record.midashi_id}'>
											</td>
											<td>
												<a href='guidance_maintenance_midashi_hensyu?midashiId=${record.midashi_id}'>${su:htmlEscape(record.midashi_name)}</a>
											</td>
											<td>
												<input type='hidden' name='midashiId' value='${record.midashi_id}'>
												<button type='button' name='upButtom' class='btn btn-mini'>↑</button>
												<button type='button' name='downButtom' class='btn btn-mini'>↓</button>
											</td>
										</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
					</section>

					<!-- 処理ボタン -->
					<section>
						<button type='button' id='tsuikaButton' class='btn'><i class='icon-plus'></i> 見出し追加</button>
						<button type='button' id='koushinButton' class='btn'><i class='icon-hdd'></i> 更新</button>
					</section>
				
					<!-- Modal -->
					<div id='dialog'></div>
				</form></div><!-- main -->
				<div id='push'></div>
			</div><!-- content -->
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<!-- スクリプト -->
		<script style='text/javascript'>

/**
 * 明細↑ボタン押下時Function
 */
function up() {
	var tr = $(this).closest("tr");
	if(tr.prev()){
		tr.insertBefore(tr.prev());
	}
	numbering();
}

/**
 * 明細↓ボタン押下時Function
 */
function down() {
	var tr = $(this).closest("tr");
	if(tr.next()){
		tr.insertAfter(tr.next());
	}
	numbering();
}

/**
 * 明細の番号#1,#2,の番号部分を全て振りなおす
 */
function numbering() {
	$("#meisaiList tr").each(function(){
		$(this).find(".number").text(Number($("#meisaiList tr").index($(this))) + 1);
		$(this).find(".gyouNo").attr("value",Number($("#meisaiList tr").index($(this))) + 1);
	});
}

//画面表示後の初期化
$(document).ready(function(){

	//明細↑ボタン押下時アクション
	$("button[name=upButtom]").click(up);

	//明細↓ボタン押下時アクション
	$("button[name=downButtom]").click(down);

	//見出し追加ボタン
	$("#tsuikaButton").click(function(){
		location.href='guidance_maintenance_midashi_tsuika';
	});

	//更新ボタン
	$("#koushinButton").click(function(){
		$("#myForm").submit();
	});

	//初期ナンバリング
	numbering();
});
		</script>
	</body>
</html>
