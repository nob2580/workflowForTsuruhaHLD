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
				<div id='main'><form id='myForm' method='post' action='guidance_maintenance_jisyou_hensyu_koushin' class='form-horizontal'>
					<input type='hidden' name='midashiId' value='${midashiId}'/>
					<input type='hidden' name='jishouId' value='${jishouId}'/>

					<!-- ぱんくず -->
					<section>
							<h3><a href='guidance_maintenance'>ガイダンスメンテナンス</a> &gt; <a href='guidance_maintenance_midashi_hensyu?midashiId=${midashiId}'>${su:htmlEscape(midashiName)}</a> &gt; ${su:htmlEscape(jishouName)}</h3>
					</section>

					<!-- 入力フィールド -->
					<section>
						<h2>事象編集</h2>
						<div>
							<div class='control-group'>
								<label class="control-label"><span class='required'>*</span>事象名</label>
								<div class='controls'>
									<input type='text' name='jishouName' value='${su:htmlEscape(jishouName)}' maxlength='20' class=' input-xxlarge'>
								</div>
							</div>
						</div>
					</section>

					<!-- 伝票一覧 -->
					<section>
						<h2>事象に対する伝票一覧</h2>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed' style="margin-top:8px">
								<thead>
									<tr>
										<th>#</th>
										<th>伝票名</th>
										<th></th>
									</tr>
								</thead>
								<tbody id='denpyouList'>
<c:if test='${0 < fn:length(denpyouKbn)}'><c:forEach var="i" begin="0" end="${fn:length(denpyouKbn) - 1}" step="1">
									<tr>
										<td style='text-align:center;'><span class='number'></span></td>
										<td>${su:htmlEscape(denpyouName[i])}</td>
										<td>
											<input type='hidden' name='denpyouKbn' value='${denpyouKbn[i]}'/>
											<input type='hidden' name='denpyouName' value='${denpyouName[i]}'/>
											<button type='button' name='denpyouUpButtom' class='btn btn-mini'>↑</button>
											<button type='button' name='denpyouDownButtom' class='btn btn-mini'>↓</button>
											<button type='button' name='denpyouDeleteButtom' class='btn btn-mini'>削除</button>
										</td>
									</tr>
</c:forEach></c:if>
								</tbody>
							</table>
						</div>
					</section>

					<!-- 処理ボタン -->
					<section>
						<button type='button'  id='denpyouSentakuButton' class='btn'><i class='icon-plus'></i> 追加</button>
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
 * 伝票↑ボタン押下時Function
 */
function denpyouUp() {
	var tr = $(this).closest("tr");
	if(tr.prev()){
		tr.insertBefore(tr.prev());
	}
	numbering();
}

/**
 * 伝票↓ボタン押下時Function
 */
function denpyouDown() {
	var tr = $(this).closest("tr");
	if(tr.next()){
		tr.insertAfter(tr.next());
	}
	numbering();
}

/**
 * 伝票削除ボタン押下時Function
 */
function denpyouDelete() {
	var tr = $(this).closest("tr");
	tr.remove();
}

/**
 * 明細の番号#1,#2,の番号部分を全て振りなおす
 */
function numbering() {
	$("#denpyouList tr").each(function(){
		$(this).find(".number").text(Number($("#denpyouList tr").index($(this))) + 1);
		$(this).find(".gyouNo").attr("value",Number($("#denpyouList tr").index($(this))) + 1);
	});
}


/**
 * 伝票選択子画面からのコールバック関数
 */
 function callBackFunc() {
	//既存伝票が選択されても何もしない
	var denpyouKbn	= denpyouKbnData.attr("data-cd");
	var denpyouName	= denpyouKbnData.attr("data-name").htmlEscape();
	if ($("#denpyouList").find("input[name=denpyouKbn][value=" + denpyouKbn + "]").length != 0) return;
	
	var tr = "";
	tr +=	"<tr>";
	tr += 		"<td style='text-align:center;'><span class='number'></span></td>";
	tr += 		"<td>" + denpyouName + "</td>";
	tr += 		"<td>";
	tr += 			"<input type='hidden' name='denpyouKbn' value='" + denpyouKbn + "'>";
	tr += 			"<input type='hidden' name='denpyouName' value='" + denpyouName + "'>";
	tr +=			"<button type='button' name='denpyouUpButtom' class='btn btn-mini'>↑</button>";
	tr +=			"<button type='button' name='denpyouDownButtom' class='btn btn-mini'>↓</button>";
	tr +=			"<button type='button' name='denpyouDeleteButtom' class='btn btn-mini'>削除</button>";
	tr +=		"</td>";
	tr +=	"</tr>";
	tr = $(tr);
	$("#denpyouList").append(tr);
	numbering();
}

//画面表示後の初期化
$(document).ready(function(){

	//伝票アクション紐付け
	$("body").on("click","button[name=denpyouDeleteButtom]",denpyouDelete); 
	$("body").on("click","button[name=denpyouUpButtom]",denpyouUp); 
	$("body").on("click","button[name=denpyouDownButtom]",denpyouDown); 

	//伝票追加ボタン押下
	$("#denpyouSentakuButton").click(function(){
		dialogCallbackDenpyouSentaku = callBackFunc;
	 	commonDenpyouSentaku();
	});

	//更新ボタン押下
	$("#koushinButton").click(function(){
		$("#myForm").submit();
	});

	//初期ナンバリング
	numbering();
});
		</script>
	</body>
</html>
