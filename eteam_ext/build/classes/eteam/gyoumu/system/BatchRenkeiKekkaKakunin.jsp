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
		<title>バッチ処理結果確認｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>バッチ処理結果確認</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				
				<!-- メイン -->
				<div id='main'><form id='inputFieldForm' name='inputFieldForm' class='form-horizontal'>
					<input type='hidden' name='sortItem' value='${su:htmlEscape(sortItem)}'>
					<input type='hidden' name='sortOrder' value='${su:htmlEscape(sortOrder)}'>
					<input type="hidden" name="pageNo" value="${su:htmlEscape(pageNo)}">

					<!-- 検索条件 -->
					<section id='searchCondition' class='print-unit'>
						<h2>検索条件</h2>
						<div>
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>日時</label>
								<div class='controls'>
									<input type='text' name='searchKaishiHiduke' class='input-small datepicker' value='${su:htmlEscape(searchKaishiHiduke)}'>
									<select id='searchKaishiHour' name='searchKaishiHour' class='input-mini'>
										<c:forEach var='kaishiHourList' items='${hourList}'>
											<option value='${su:htmlEscape(kaishiHourList.naibu_cd)}' <c:if test='${kaishiHourList.naibu_cd eq searchKaishiHour}'>selected</c:if>>${su:htmlEscape(kaishiHourList.name)}</option>
										</c:forEach>
									</select> 時
									<select id='searchKaishiMin' name='searchKaishiMin' class='input-mini'>
										<c:forEach var='kaishiMinList' items='${minList}'>
											<option value='${su:htmlEscape(kaishiMinList.naibu_cd)}' <c:if test='${kaishiMinList.naibu_cd eq searchKaishiMin}'>selected</c:if>>${su:htmlEscape(kaishiMinList.name)}</option>
										</c:forEach>
									</select> 分 ～ 
									<input type='text' name='searchSyuuryouHiduke' class='input-small datepicker' value='${su:htmlEscape(searchSyuuryouHiduke)}'>
									<select id='searchSyuuryouHour' name='searchSyuuryouHour' class='input-mini'>
										<c:forEach var='syuuryouHourList' items='${hourList}'>
											<option value='${su:htmlEscape(syuuryouHourList.naibu_cd)}' <c:if test='${syuuryouHourList.naibu_cd eq searchSyuuryouHour}'>selected</c:if>>${su:htmlEscape(syuuryouHourList.name)}</option>
										</c:forEach>
									</select> 時
									<select id='searchSyuuryouMin' name='searchSyuuryouMin' class='input-mini'>
										<c:forEach var='syuuryouMinList' items='${minList}'>
											<option value='${su:htmlEscape(syuuryouMinList.naibu_cd)}' <c:if test='${syuuryouMinList.naibu_cd eq searchSyuuryouMin}'>selected</c:if>>${su:htmlEscape(syuuryouMinList.name)}</option>
										</c:forEach>
									</select> 分
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>バッチ名</label>
								<div class='controls'>
									<select name='searchBatchName' class='input-xlarge'>
										<option></option>
										<c:forEach var='batchName' items='${batchNameList}'>
											<option value='${su:htmlEscape(batchName.batch_name)}' <c:if test='${batchName.batch_name eq searchBatchName}'>selected</c:if>>${su:htmlEscape(batchName.batch_name)}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>ステータス</label>
								<div class='controls'>
									<select name='searchStatus' class='input-medium'>
										<option></option>
										<c:forEach var='statusList' items='${statusList}'>
											<option value='${su:htmlEscape(statusList.naibu_cd)}' <c:if test='${statusList.naibu_cd eq searchStatus}'>selected</c:if>>${su:htmlEscape(statusList.name)}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div>
							<button type='button' id='kensakuButton' class='btn'><i class='icon-search'></i> 検索実行</button>
							<button type='button' id='clearButton' class='btn'><i class='icon-remove-circle'></i> 条件クリア</button>
						</div>
					</section>

					<!-- 検索結果 -->
					<section>
						<h2>検索結果</h2>
						<div id='kensakuKekkaList' class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th >日時<br>
											<a class='sort' data-sortItem='start_time' data-sortOrder='ASC'>▲</a>
											<a class='sort' data-sortItem='start_time' data-sortOrder='DESC'>▼</a>
										</th>
										<th style='width: 200px'>バッチ名<br>
											<a class='sort' data-sortItem='batch_name' data-sortOrder='ASC'>▲</a>
											<a class='sort' data-sortItem='batch_name' data-sortOrder='DESC'>▼</a>
										</th>
										<th>ステータス<br>
											<a class='sort' data-sortItem='batch_status' data-sortOrder='ASC'>▲</a>
											<a class='sort' data-sortItem='batch_status' data-sortOrder='DESC'>▼</a>
										</th>
										<th>処理件数</th>
										<th>詳細</th>
									</tr>
								</thead>
								<tbody id='meisaiList'>
<c:forEach var='record' items='${kekkaList}' varStatus="st">
									<tr>
										<td>${su:htmlEscape(record.start_time)}</td>
										<td>${su:htmlEscape(record.batch_name)}</td>
										<td>${su:htmlEscape(record.batch_status_name)}</td>
										<td>${su:htmlEscape(record.count_name)}:${record.count}</td>
										<td>
										<c:if test='${record.shiwake_serial_no ne null}'><button type='button' name='kakuninButton' class='btn'>確認</button></c:if>
										<input type="hidden" name="serialNo" value="${su:htmlEscape(record.shiwake_serial_no)}">
										</td>
									</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
						<div class='blank'></div>
						<%@ include file="/jsp/eteam/include/Paging.jsp" %>
					</section>
				</form></div><!-- main -->
				<!-- Modal -->
				<div id='dialog'></div>
			</div><!-- /container -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>

const identificationKey = "eteam.errorLogKogamen";
$(document).ready(function(){

	//検索
	$("#kensakuButton").click(function(e){
		$("input[name=sortItem]").val("start_time");
		$("input[name=sortOrder]").val("DESC");
		$("input[name=pageNo]").val("1");
		$("form[name=inputFieldForm]").attr("action", "batch_renkei_kekka_kakunin_kensaku");
		$("form[name=inputFieldForm]").attr("method", "get");
		$("form[name=inputFieldForm]").submit();
	});

	//条件クリアボタン押下時
	$("#clearButton").click(function(e){
		$("form[name=inputFieldForm]").attr("action", "batch_renkei_kekka_kakunin");
		$("form[name=inputFieldForm]").attr("method", "get");
		$("form[name=inputFieldForm]").submit();
	});
	
	//確認ボタン押下時
	$("[name=kakuninButton]").click(function(e){
		var width = "800";
		if(windowSizeChk()) {
			width = $(window).width() * 0.9;
		}
		$("#dialog")
		.dialog({
			modal: true,
			width: width,
			height: "520",
			title: "エラーログ表示",
			buttons: {閉じる: function() {$(this).dialog("close");}},
			resizeStop: function() { saveMeisaiSize($(this), identificationKey); },
		});
		resizeMeisai($("#dialog"), identificationKey);
		$("#dialog").load("error_log_hyouji_kogamen?serialNo="+$(this).parent().find("input[name=serialNo]").val()+"&index=1");
	});

	//ソート
	$("a.sort").click(function(e){
		$("input[name=sortItem]").val($(this).attr('data-sortItem'));
		$("input[name=sortOrder]").val($(this).attr('data-sortOrder'));
		$("input[name=pageNo]").val("1");
		$("form[name=inputFieldForm]").attr("action", "batch_renkei_kekka_kakunin_kensaku");
		$("form[name=inputFieldForm]").attr("method", "get");
		$("form[name=inputFieldForm]").submit();
	});
});
		</script>
	</body>
</html>
