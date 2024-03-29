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
		<title>執行状況一覧（みなし実績）｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>執行状況一覧（みなし実績）</h1>
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				<!-- メイン -->
				<div id='main'><form id='myForm' class='form-horizontal'>
				<div id='kaikeiContent' class='container'>
				<div>
				</div>

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
													<option value="${item.key}" <c:if test='${item.key eq targetDate}'>selected</c:if>>${su:htmlEscape(item.val)}</option>
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
							<div class='control-group' id='kamokuOnly'>
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
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>予算単位</label>
								<div class='controls'>
									<label class='radio inline'>
										<input type='radio' name='yosanTani' value='0' <c:if test='${"1" ne yosanTani}'>checked</c:if>>
										年予算
									</label>
									<label class='radio inline'>
										<input type='radio' name='yosanTani' value='1' <c:if test='${"1" eq yosanTani}'>checked</c:if>>
										累計予算
									</label>
								</div>
							</div>
							<div class='blank'></div>
							<div>
								<button type='button' class='btn' id='kensakuBtn'><i class='icon-search'></i> 検索実行</button>
								<button type='button' class='btn' id='joukenClearBtn'><i class='icon-remove-circle'></i> 条件クリア</button>
							</div>
						</div>
					</section>

					<section class='print-unit'>
						<h2>検索結果</h2>
						<div>
							<button type='button' class='btn' id='csvBtn'><i class='icon-download'></i> CSV出力</button>
						</div>
						<div class='blank'></div>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed' >
								<thead>
									<tr>
										<th><nobr>集計部門コード</nobr></th>
										<th><nobr>集計部門名称</nobr></th>
										<th><nobr>明細部門コード</nobr></th>
										<th><nobr>明細部門名称</nobr></th>
									<c:if test='${"1" eq checkTani}'>
										<th><nobr>科目コード</nobr></th>
										<th><nobr>科目名称</nobr></th>
									</c:if>
									<c:if test='${"1" ne yosanTani}'>
										<th><nobr>当年度予算</nobr></th>
									</c:if>
									<c:if test='${"1" eq yosanTani}'>
										<th><nobr>累計予算</nobr></th>
									</c:if>
										<th><nobr>累計執行高</nobr><br>（会計）</th>
										<th>WF内<br><nobr>みなし執行高</nobr></th>
										<th><nobr>執行高合計</nobr></th>
										<th><nobr>差引予算残</nobr></th>
										<th><nobr>執行率</nobr></th>
									</tr>
								</thead>
								<tbody>
<c:forEach var="record" items="${ichiranList}" varStatus="st1">
									<tr>
									<td>${su:htmlEscape(record.syuukei_bumon_cd)}</td>
										<td><nobr>${su:htmlEscape(record.syuukei_bumon_name)}</nobr></td>
										<td>${su:htmlEscape(record.meisai_bumon_cd)}</td>
										<td><nobr>${su:htmlEscape(record.meisai_bumon_name)}</nobr></td>
									<c:if test='${"1" eq checkTani}'>
										<td>${su:htmlEscape(record.kamoku_gaibu_cd)}</td>
										<td><nobr>${su:htmlEscape(record.kamoku_name_ryakushiki)}</nobr></td>
									</c:if>
										<td class="text-r">${su:htmlEscape(record.yosan)}</td>
										<td class="text-r">${su:htmlEscape(record.ruikei_shikkoudaka)}</td>
										<td class="text-r">${su:htmlEscape(record.minasi_shikkoudaka)}</td>
										<td class="text-r">${su:htmlEscape(record.shikkoudaka_goukei)}</td>
										<td class="text-r">${su:htmlEscape(record.yosanzan)}</td>
										<td align='right'>${su:htmlEscape(record.rate)}</td>
									</tr>
</c:forEach>
									</tbody>
								</table>
								<c:if test="${not st1.last}"><div class='blank'>&nbsp;</div></c:if>
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
		$("#myForm").attr("action", "shikkou_joukyou_search");
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
		$("#myForm").attr("action", "shikkou_joukyou_csvoutput");
		$("#myForm").attr("method", "post");
		$("#myForm").submit();
	});
	
<c:if test='${"1" ne checkTani}'>
	$("#kamokuOnly").hide();
</c:if>
	
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
			parent				: "yosanshikkou"
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
		parent				: "yosanshikkou"
	});
}
</script>
	</body>
</html>