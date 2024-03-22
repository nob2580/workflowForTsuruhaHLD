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
	<section>
		<div id='kensaku'>
			<form class="form-inline">
				<div class='control-group'>
					<label class="label label-sub" for='d_nendo'>年度</label>
					<input type='text' id='d_nendo' name='d_nendo' class='input-mini'>
					<label class="label label-sub" for='d_ryakugou'>略号</label>
					<input type='text' id='d_ryakugou' name='d_ryakugou' class='input-small'>
					<button type='button' id='d_kianBangouSearchButton' class='btn'>検索</button>
				</div>
			</form>
		</div>
	</section>

	<div id='kianBangou'>
		<table class='table-bordered table-condensed'>
			<thead>
				<tr>
					<th>年度</th>
					<th>略号</th>
					<th>区分内容</th>
					<th>開始番号</th>
					<th>最終番号</th>
				</tr>
			</thead>
			<tbody>
<c:forEach var="record" items="${list}">
				<tr>
					<td><nobr>${su:htmlEscape(record.nendo)}</nobr></td>
					<td><nobr><a class='kianBangouSentaku link' data-nendo='${record.nendo}' data-ryakugou='${su:htmlEscape(record.ryakugou)}' onclick="kianBangouClick($(this))">${su:htmlEscape(record.ryakugou)}</a></nobr></td>
					<td><nobr>${su:htmlEscape(record.kbnNaiyou)}</nobr></td>
					<td><nobr>${su:htmlEscape(record.kianbangouFrom)}</nobr></td>
					<td><nobr>${su:htmlEscape(record.kianbangouLast)}</nobr></td>
				</tr>
</c:forEach>
			</tbody>
		</table>
	</div>
	
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>

/**
 * 部門検索
 */
function d_ryakugouSearch() {
	$("#dialogMain").find("a.kianBangouSentaku").each(function(){
		var nendo = $("input[name=d_nendo]").val();
		var bangou = $("input[name=d_ryakugou]").val();
		if (wordSearch($(this).attr("data-nendo"), nendo) && wordSearch($(this).text(), bangou)) {
			$(this).closest("tr").show();
		}else{
			$(this).closest("tr").hide();
		}
	});
}

/**
 * 部門検索ダイアログ：部門選択
 * @param a 押下した部門リンク
 */
function kianBangouClick(a) {
	// 部門名と部門コードを呼び出し元のオブジェクトへ設定します。
	if ("dialogRetKianBangouSentakuCallback" in window && dialogRetKianBangouSentakuCallback != null) {
		dialogRetKianBangouSentakuCallback(a);
	} else {
		if ("dialogRetKianBangouNendo" in window)		dialogRetKianBangouNendo.val(a.attr("data-nendo"));
		if ("dialogRetKianBangouRyakugou" in window)	dialogRetKianBangouRyakugou.val(a.attr("data-ryakugou"));
	}
	$("#dialog").children().remove();
	$("#dialog").dialog("close");
}

//部門検索ボタン
$("#d_kianBangouSearchButton").click(function(){
	d_ryakugouSearch();
});

//初期表示処理
$("#dialog").ready(function(){
	commonInit($("#kensaku"));
});
</script>
