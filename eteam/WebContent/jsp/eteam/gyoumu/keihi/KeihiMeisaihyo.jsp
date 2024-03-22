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
		<title>経費明細一覧表｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
		</style>
	</head>
	<body>
		<div id='wrap'>
			<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>
			<!-- 中身 -->
			<div id='content' class='container'>
				<!-- タイトル -->
				<h1>経費明細一覧</h1>
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				<!-- メイン -->
				<div id='main'><form id='myForm' class='form-horizontal'>
				<div id='kaikeiContent' class='container'>

					<!-- 入力フィールド -->
					<section id="search_cond" class='print-unit'>
						<h2>検索条件</h2>
						<div>
							<div class='row'>
								<div class='span5'>
									<div class='control-group'>
										<label class='control-label'><span class='required'>*</span>対象月</label>
										<div class='controls'>
											<select id='targetDate' name='targetDate' class='input-medium input-inline'>
												<c:forEach var="item" items="${monthList}">
													<option data-ki="${item.ki}" value="${item.key}" <c:if test='${item.key eq targetDate}'>selected</c:if>>${su:htmlEscape(item.val)}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
								<div class='span7'>
									<div class='control-group'>
										<label class='control-label'><span class='required'>*</span>集計部門</label>
										<div class='controls'>
											<input type='text' name='syuukeiBumonName' class='input-inline input-medium' readonly="readonly" value='${su:htmlEscape(syuukeiBumonName)}'>
											<input type='hidden' name='syuukeiBumonCd' value='${su:htmlEscape(syuukeiBumonCd)}'>
											<button type='button' id='syuukeiBumonSentakuBtn' class='btn btn-small input-inline'>選択</button>
										</div>
									</div>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>部門</label>
								<div id="bumonSentakuArea" class='controls'>
									<input type='text' name='fromBumonName' class='input-inline input-medium' readonly="readonly" value='${su:htmlEscape(fromBumonName)}'>
									<input type='hidden' name='fromBumonCd' value='${su:htmlEscape(fromBumonCd)}'>
									<button type='button' id='fromBumonSentakuBtn' class='btn btn-small input-inline' disabled>選択</button>
									<button type='button' id='fromBumonClearBtn'   class='btn btn-small input-inline'>クリア</button>
									～
									<input type='text' name='toBumonName' class='input-inline input-medium' readonly="readonly" value='${su:htmlEscape(toBumonName)}'>
									<input type='hidden' name='toBumonCd' value='${su:htmlEscape(toBumonCd)}'>
									<button type='button' id='toBumonSentakuBtn' class='btn btn-small input-inline' disabled>選択</button>
									<button type='button' id='toBumonClearBtn'   class='btn btn-small input-inline'>クリア</button>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>科目</label>
								<div class='controls'>
									<input type='text' name='fromKamokuName' class='input-inline input-medium' readonly="readonly" value='${su:htmlEscape(fromKamokuName)}'>
									<input type='hidden' name='fromKamokuCd' value='${su:htmlEscape(fromKamokuCd)}'>
									<button type='button' id='fromKamokuSentakuBtn' class='btn btn-small input-inline'>選択</button>
									<button type='button' id='fromKamokuClearBtn'   class='btn btn-small input-inline'>クリア</button>
									～
									<input type='text' name='toKamokuName' class='input-inline input-medium' readonly="readonly" value='${su:htmlEscape(toKamokuName)}'>
									<input type='hidden' name='toKamokuCd' value='${su:htmlEscape(toKamokuCd)}'>
									<button type='button' id='toKamokuSentakuBtn' class='btn btn-small input-inline'>選択</button>
									<button type='button' id='toKamokuClearBtn'   class='btn btn-small input-inline'>クリア</button>
								</div>
							</div>
						</div>
						<div class='blank'></div>
						<div>
							<button type='button' class='btn' id='kensakuBtn'><i class='icon-search'></i> 検索実行</button>
							<button type='button' class='btn' id='joukenClearBtn'><i class='icon-remove-circle'></i> 条件クリア</button>
						</div>
					</section>

					<section class='print-unit'>
						<h2>検索結果</h2>
						<div>
							<button type='button' class='btn' id='csvBtn'><i class='icon-download'></i> CSV出力</button>
							<button type='button' class='btn' id='excelBtn'><i class='icon-download'></i> 帳票出力</button>
						</div>
						<div class='blank'></div>
						<div class='no-more-tables'>
<c:forEach var="syuukeiBumon" items="${syuukeiBumonList}" varStatus="stsb">
							<h3>${su:htmlEscape(syuukeiBumon.name)}</h3>
							<table class='table-bordered table-condensed' >
								<thead>
									<tr>
										<c:if test="${showBumon}"><th><nobr>部門名称</nobr></th></c:if>
										<th><nobr>科目名</nobr></th>
										<c:if test="${showEdaban}"><th><nobr>枝番名称</nobr></th></c:if>
										<th><nobr>日付</nobr></th>
										<th><nobr>相手先</nobr></th>
										<th><nobr>摘要</nobr></th>
										<th><nobr>金額</nobr></th>
									</tr>
								</thead>
								<tbody>
	<c:forEach var="bumon" items="${syuukeiBumon.bumonList}" varStatus="stbm">
		<c:forEach var="kamokuList" items="${bumon.kamokuList}" varStatus="stkm">
			<c:forEach var="edList" items="${kamokuList.edabanList}" varStatus="sted">
				<c:forEach var="record" items="${edList.shiwakeList}" varStatus="strc">
												<tr>
													<c:if test="${showBumon}"><td><c:if test = "${strc.first && stkm.first && sted.first}">${su:htmlEscape(bumon.name)}</c:if></td></c:if>
													<td><c:if test="${strc.first && sted.first}">${su:htmlEscape(record.kamoku_name_ryakushiki)}</c:if></td>
	<!-- 												<c:if test="${showEdaban}"><td><c:if test="${strc.first}">${su:htmlEscape(record.edaban_name)}<c:if test = "${empty record.edaban_name}">(枝番なし)</c:if></c:if></td></c:if>  -->
													<c:if test="${showEdaban}"><td>${su:htmlEscape(record.edaban_name)}</td></c:if>
													<td>${su:htmlEscape(record.dymd)}</td>
													<td>${su:htmlEscape(record.torihikisaki_name_ryakushiki)}</td>
													<td>${su:htmlEscape(record.tky)}</td>
													<td align='right'>${su:htmlEscape(record.valu)}</td>
												</tr>
		<c:if test="${strc.last && showEdaban}">
												<tr>
													<c:if test="${showBumon}"><td></td></c:if>
													<td></td>
													<td colspan='${3 + (showEdaban ? 1:0)}'><b>小計</b></td>
													<td align='right'>${su:htmlEscape(record.total_eda)}</td>
												</tr>
		</c:if>
		<c:if test="${strc.last && sted.last}">
												<tr>
													<c:if test="${showBumon}"><td></td></c:if>
													<td colspan='${4 + (showEdaban ? 1:0)}'><b>合計</b></td>
													<td align='right'>${su:htmlEscape(record.total_kmk)}</td>
												</tr>
		</c:if>
		<c:if test="${strc.last && sted.last && stkm.last && showBumon}">
												<tr>
													<td colspan='${5 + (showEdaban ? 1:0)}'><b>総合計</b></td>
													<td align='right'>${su:htmlEscape(record.total_bmn)}</td>
												</tr>
		</c:if>
				</c:forEach>
			</c:forEach>
		</c:forEach>
	</c:forEach>
								</tbody>
							</table>
							<c:if test="${not stsb.last}"><div class='blank'>&nbsp;</div></c:if>
</c:forEach>
						</div>
						<div class='blank'></div>
						<%@ include file="/jsp/eteam/include/Paging.jsp" %>
					</section>
				</div>
				</form></div><!-- main -->
				<!-- Modal -->
				<div id='dialog'></div>
			</div><!-- /container -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
<script style='text/javascript'>
var dialogRetOptionCd = null;
var dialogRetOptionName = null;
var dialogRetCallback = null;
var selectedKi = "";
$(document).ready(function() {
	//集計部門選択
	$("#syuukeiBumonSentakuBtn").click(function() {
		
		dialogRetSyuukeiBumonCd = $("#main input[name=syuukeiBumonCd]");
		dialogRetSyuukeiBumonName = $("#main input[name=syuukeiBumonName]");

		dialogCallbackSyuukeiBumonSentaku = function() {
			$("#bumonSentakuArea input").val("");
			$("#bumonSentakuArea button").prop("disabled", false);
		};
		syuukeiDialogOpen();
		
	});
	//部門選択(FROM)
	$("#fromBumonSentakuBtn").click(function() {
		dialogRetOptionCd = $("#main input[name=fromBumonCd]");
		dialogRetOptionName = $("#main input[name=fromBumonName]");
		dialogRetCallback = null;
		sentakuDialogOpen("部門選択", 3);
	});
	$("#fromBumonClearBtn").click(function() {
		$("#main input[name=fromBumonCd]").val("");
		$("#main input[name=fromBumonName]").val("")
	});
	//部門選択(TO)
	$("#toBumonSentakuBtn").click(function() {
		dialogRetOptionCd = $("#main input[name=toBumonCd]");
		dialogRetOptionName = $("#main input[name=toBumonName]");
		dialogRetCallback = null;
		sentakuDialogOpen("部門選択", 3);
	});
	$("#toBumonClearBtn").click(function() {
		$("#main input[name=toBumonCd]").val("");
		$("#main input[name=toBumonName]").val("");
	});
	//科目選択(FROM)
	$("#fromKamokuSentakuBtn").click(function() {
		dialogRetOptionCd = $("#main input[name=fromKamokuCd]");
		dialogRetOptionName = $("#main input[name=fromKamokuName]");
		dialogRetCallback = null;
		sentakuDialogOpen("科目選択", 2);
	});
	$("#fromKamokuClearBtn").click(function() {
		$("#main input[name=fromKamokuCd]").val("");
		$("#main input[name=fromKamokuName]").val("");
	});
	//科目選択(TO)
	$("#toKamokuSentakuBtn").click(function() {
		dialogRetOptionCd = $("#main input[name=toKamokuCd]");
		dialogRetOptionName = $("#main input[name=toKamokuName]");
		dialogRetCallback = null;
		sentakuDialogOpen("科目選択", 2);
	});
	$("#toKamokuClearBtn").click(function() {
		$("#main input[name=toKamokuCd]").val("");
		$("#main input[name=toKamokuName]").val("");
	});

	//検索ボタン
	$("#kensakuBtn").click(function(e) {
		$("#myForm").attr("action", "keihi_data_meisai_search");
		$("#myForm").attr("method", "get");
		$("#myForm").submit();
	});

	//条件クリアボタン押下
	$("#joukenClearBtn").click(function(e) {
		inputClear($(this).closest("form"));
		$("#search_cond input").val("");
		$("#bumonSentakuArea button").prop("disabled", true);
	});

	//CSV出力ボタン押下
	$("#csvBtn").click(function(e) {
		$("#myForm").attr("action", "keihi_data_meisai_csvoutput");
		$("#myForm").attr("method", "post");
		$("#myForm").submit();
	});

	//帳票出力ボタン押下
	$("#excelBtn").click(function(e) {
		$("#myForm").attr("action", "keihi_data_meisai_excel");
		$("#myForm").attr("method", "post");
		$("#myForm").submit();
	});

	// 初期表示
	$("#bumonSentakuArea button").prop("disabled", $("input[name=syuukeiBumonCd]").val() == "");

});

/**
 * 各種ダイアログを表示させる
 * @param title	タイトル
 * @param type	1:、2:、3:
 */
function sentakuDialogOpen(title, type) {
	//表示法は現代式に
	var width = "600";
	if (windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog").dialog({
		modal: true,
		width: width,
		height: "520",
		title: title,
		buttons: {閉じる: function() {$(this).dialog("close");}},
		close: function() {
			$(this).children().remove();
		}
	}).load(
		"syuukei_option_sentaku",
		{
			type				: type,
			targetDate			: $("#targetDate").val(),
			ptn					: $("input[name=syuukeiBumonPtn]").val(),
			syuukeiBumonCd		: $("input[name=syuukeiBumonCd]").val(),
			parent				: "keihi"
		}
	);
}


/**
 * 集計部門選択ダイアログを表示させる
 */
function syuukeiDialogOpen() {
	var width = "600";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog").dialog({
		modal: true,
		width: width,
		height: "520",
		title:"集計部門選択",
		buttons: {閉じる: function() {$(this).dialog("close");}},
		close: function() {
			$(this).children().remove();
		}
	}).load("syuukei_bumon_sentaku",	{
		targetDate			: $("#targetDate").val(),
		denpyouId			: null,
		kihyouBumonCd		: null,
		parent				: "keihi"
	});
}

</script>
	</body>
</html>