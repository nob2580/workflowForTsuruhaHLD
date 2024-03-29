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
		<title>お気に入り編集｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>お気に入りの編集</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' action='okiniiri_hensyu_touroku' class='form-horizontal'>

					<!-- 検索枠 -->
					<section>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>業務種別</th>
										<th>伝票種別</th>
										<th><span class='required'>*</span>メモ</th>
										<th></th>
									</tr>
								</thead>
								<tbody id='denpyouList'>
<c:if test='${0 < fn:length(denpyouKbn)}'><c:forEach var="i" begin="0" end="${fn:length(denpyouKbn) - 1}" step="1">
									<tr class='${su:htmlEscape(bgColor[i])}'>
										<td>${su:htmlEscape(gyoumuShubetsu[i])}</td>
										<td>${su:htmlEscape(denpyouShubetsu[i])}</td>
										<td><textarea name='memo' maxlength='160'>${su:htmlEscape(memo[i])}</textarea></td>
										<td>
											<button type='button' name='upButtom' class='btn btn-mini'>↑</button>
											<button type='button' name='downButtom' class='btn btn-mini'>↓</button>
											<button type='button' name='delButtom' class='btn btn-mini'>削除</button>
											<input type='hidden' name='denpyouKbn' value='${su:htmlEscape(denpyouKbn[i])}'>
											<input type='hidden' name='gyoumuShubetsu' value='${su:htmlEscape(gyoumuShubetsu[i])}'>
											<input type='hidden' name='denpyouShubetsu' value='${su:htmlEscape(denpyouShubetsu[i])}'>
											<input type='hidden' name='bgColor' value='${su:htmlEscape(bgColor[i])}'>
										</td>
									</tr>
</c:forEach></c:if>
								</tbody>
							</table>
						</div>
					</section>

					<!-- 処理ボタン -->
					<section>
							<button type='button' id='tsuikaButton' class='btn'><i class='icon-plus'></i> 伝票追加</button>
						<button type='button' id='tourokuButton' class='btn' ><i class='icon-hdd'></i> 登録</button>
					</section>
					<!-- Modal -->
					<div id='dialog'></div>
				</form></div><!-- main -->
				<div id='push'></div>
			</div><!-- cotent -->
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<!-- スクリプト -->
		<script style='text/javascript'>

/**
 * ↑ボタン押下時Function
 */
function upButtom() {
	var tr = $(this).closest("tr");
	if(tr.prev()){
		tr.insertBefore(tr.prev());
	}
	numbering();
}

/**
 * ↓ボタン押下時Function
 */
function downButtom() {
	var tr = $(this).closest("tr");
	if(tr.next()){
		tr.insertAfter(tr.next());
	}
	numbering();
}

/**
 * 削除ボタン押下時Function
 */
function delButtom() {
	$(this).closest("tr").remove();
	numbering();
}

/**
 * 明細行のナンバリング
 */
function numbering() {
	//この画面はナンバリング不要
}
/**
 * 伝票選択子画面からのコールバック関数
 */
function callBackFunc() {
	//既存伝票が選択されても何もしない
	var denpyouKbn		= denpyouKbnData.attr("data-cd");
	var denpyouName		= denpyouKbnData.attr("data-name").htmlEscape();
	var gyoumuShubetsu	= denpyouKbnData.attr("data-kind").htmlEscape();
	var memo			= denpyouKbnData.attr("data-setsumei").htmlEscape();
	if ($("#denpyouList").find("input[name=denpyouKbn][value=" + denpyouKbn + "]").length != 0) return;

	var tr = "";
	tr +=	"<tr>";
	tr +=	"<td>" + gyoumuShubetsu + "</td>";
	tr +=	"<td>" + denpyouName + "</td>";
	tr +=	"<td><textarea name='memo' maxlength='160'>" + memo + "</textarea></td>";
	tr +=	"<td>";
	tr +=		"<button type='button' name='upButtom' class='btn btn-mini'>↑</button>";
	tr +=		"<button type='button' name='downButtom' class='btn btn-mini'>↓</button>";
	tr +=		"<button type='button' name='delButtom' class='btn btn-mini'>削除</button>";
	tr +=		"<input type='hidden' name='denpyouKbn' value='" + denpyouKbn + "'>";
	tr +=		"<input type='hidden' name='gyoumuShubetsu' value='" + gyoumuShubetsu + "'>";
	tr +=		"<input type='hidden' name='denpyouShubetsu' value='" + denpyouName + "}'>";
	tr +=		"<input type='hidden' name='bgColor' value=''>";
	tr +=	"</td>";
	tr +=	"</tr>";
	tr = $(tr);
	$("#denpyouList").append(tr);
}
 $(document).ready(function(){

	//伝票アクション紐付け
	$("body").on("click","button[name=upButtom]",upButtom); 
	$("body").on("click","button[name=downButtom]",downButtom); 
	$("body").on("click","button[name=delButtom]",delButtom); 

	//伝票追加ボタン
	$("#tsuikaButton").click(function(){
	 	dialogCallbackDenpyouSentaku = callBackFunc;
	 	commonDenpyouSentaku();
	});
	//登録ボタン
	$("#tourokuButton").click(function(){
		$("#myForm").submit();
	});
});
 
		</script>
	</body>
</html>
