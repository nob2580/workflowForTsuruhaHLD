<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- エラー -->
<%@ include file="/jsp/eteam/include/InputError.jsp" %>

<!-- メイン -->
<div id='dialogMain'>
	<input type='hidden' id="errorCnt" value='${fn:length(errorList)}'>
	<input type="hidden" name='index' value='${su:htmlEscape(index)}'>
	<input type="hidden" name='maxIndex' value='${su:htmlEscape(maxIndex)}'>
	<input type='hidden' name='serialNo' value='${su:htmlEscape(serialNo)}' >
	<!-- 入力フィールド -->
	<section>
		<h2 style='line-height: 30px;<c:if test='${null eq index or "" eq index}'> display:none</c:if>'>
			<span>${su:htmlEscape(index)} / ${su:htmlEscape(maxIndex)}
				<button type='button' name='btnPrevious' class='btn' style='position: absolute; right: 90px; font-size: 14px;'><i class='icon-arrow-left'></i> 前へ</button>
				<button type='button' name='btnNext' class='btn' style='position: absolute; right: 10px;  font-size: 14px;'>次へ <i class='icon-arrow-right'></i></button>
			</span>
		</h2>
		<h3>不良データログ</h3>
		<div id='invalidFileList' class='no-more-tables'>
			<table class='table-bordered table-condensed'>
				<thead>
					<tr>
						<th>行No</th>
						<th>項目No</th>
						<th>項目名称</th>
						<th>不正な値</th>
						<th>エラー内容</th>
					</tr>
				</thead>
				<tbody id='meisaiList'>
<c:forEach var='record' items='${invalidFileList}' varStatus="st">
					<tr>
						<td>${su:htmlEscape(record.gyou_no)}</td>
						<td>${su:htmlEscape(record.koumoku_no)}</td>
						<td>${su:htmlEscape(record.koumoku_name)}</td>
						<td>${su:htmlEscape(record.invalid_value)}</td>
						<td>${su:htmlEscape(record.error_naiyou)}</td>
					</tr>
</c:forEach>
				</tbody>
			</table>
		</div>
		<h3>不良伝票ログ</h3>
		<div id='invalidDenpyouList' class='no-more-tables'>
			<table class='table-bordered table-condensed'>
				<thead>
					<tr>
						<th>伝票開始行</th>
						<th>伝票終了行</th>
						<th>伝票日付</th>
						<th>伝票番号</th>
						<th>貸借差額金額</th>
						<th>概要</th>
						<th>内容</th>
					</tr>
				</thead>
				<tbody id='meisaiList'>
<c:forEach var='record' items='${invalidDenpyouList}' varStatus="st">
					<tr>
						<td>${su:htmlEscape(record.denpyou_start_gyou)}</td>
						<td>${su:htmlEscape(record.denpyou_end_gyou)}</td>
						<td>${su:htmlEscape(record.denpyou_date)}</td>
						<td>${su:htmlEscape(record.denpyou_bangou)}</td>
						<td>${su:htmlEscape(record.taishaku_sagaku_kingaku)}</td>
						<td>${su:htmlEscape(record.gaiyou)}</td>
						<td>${su:htmlEscape(record.naiyou)}</td>
					</tr>
</c:forEach>
				</tbody>
			</table>
		</div>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
const identificationKey = "eteam.errorLogKogamen";
/**
 * 前の明細情報の表示
 */
 $("button[name=btnPrevious]").click(function(event) {
	var serialNo = $("#dialog").find("input[name=serialNo]").val();
	var index = $("#dialog").find("input[name=index]").val();
	
	// 前のインデックスへシフト
	index = parseInt(index) - 1;
	
	// 最小インデックスを下回る場合、インデックスを最小インデックスに留める
	if (index < 1) {
		index = 1;
	}

	// ダイアログの再表示
	$("#dialog").dialog("close");
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
	$("#dialog").load("error_log_hyouji_kogamen?serialNo="+serialNo+"&index="+index);
});

/**
 * 次の明細情報の表示
 */
 $("button[name=btnNext]").click(function(event) {
	var serialNo = $("#dialog").find("input[name=serialNo]").val();
	var index = $("#dialog").find("input[name=index]").val();
	var maxIndex = $("#dialog").find("input[name=maxIndex]").val();
	
	// 次のインデックスへシフト
	index = parseInt(index) + 1;
	
	// 最大インデックスを上回る場合、インデックスを最大インデックスに留める
	if (index > maxIndex) {
		index = maxIndex;
	}

	// ダイアログの再表示
	$("#dialog").dialog("close");
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
	$("#dialog").load("error_log_hyouji_kogamen?serialNo="+serialNo+"&index="+index);
});

</script>
