<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- カスタムスタイル -->
<style type='text/css'>
<!--
#torihiki_sentaku_ichiran * th, #torihiki_sentaku_ichiran * td {max-width: 200px;}
-->
</style>

<!-- エラー -->
<%@ include file="/jsp/eteam/include/InputError.jsp" %>

<!-- 本体 -->
<div id='dialogMain'>
	<section>
		<div class="form-horizontal">
			<div class='control-group'>
				<label class="control-label">分類１</label>
				<div class='controls'>
					<select id='searchBunrui1'></select>
				</div>
			</div>
			<div class='control-group'>
				<label class="control-label">分類２</label>
				<div class='controls'>
					<select id='searchBunrui2'></select>
				</div>
			</div>
			<div class='control-group'>
				<label class="control-label">分類３</label>
				<div class='controls'>
					<select id='searchBunrui3'></select>
				</div>
			</div>
			<div class='control-group'>
				<label class="control-label">キーワード</label>
				<div class='controls'>
					<input type="text" id='keyword'>
				</div>
			</div>
		</div>
		<div>
			<button type='button' class='btn' id='clearButton'><i class='icon-remove-circle'></i> 条件クリア</button>
		</div>
	</section>
	<section>
		<input type='hidden' name="shainCd" value='${su:htmlEscape(shainCd)}'>
		<table id='torihiki_sentaku_ichiran' class='table-bordered table-condensed'>
			<thead>
				<tr>
					<th class='mw'>#</th>
					<th class='mw'>分類１</th>
					<th class='mw'>分類２</th>
					<th class='mw'>分類３</th>
					<th class='mw'>取引名</th>
					<th class='mw'>勘定科目</th>
				</tr>
			</thead>
			<tbody id='torihikiSentakuBody'>
<c:forEach var="record" items="${list}">
				<tr>
					<td class='shiwakeEdano'>${su:htmlEscape(record.shiwake_edano)}</td>
					<td class='displayBunrui1'>${su:htmlEscape(record.bunrui1)}</td>
					<td class='displayBunrui2'>${su:htmlEscape(record.bunrui2)}</td>
					<td class='displayBunrui3'>${su:htmlEscape(record.bunrui3)}</td>
					<td><a class='link torihikiName'
						data-denpyouKbn='${denpyouKbn}'
						data-shiwakeEdaNo='${record.shiwake_edano}'
<%-- 						data-kamokuEdabanCd='${record.kari_kamoku_edaban_cd}' --%>
<%-- 						data-futanBumonCd='${record.kari_futan_bumon_cd}' --%>

						>${su:htmlEscape(record.torihiki_name)}</a></td>
					<td class='kamokuName' data-kamokuCd='${record.kari_kamoku_cd}'>${su:htmlEscape(record.kamoku_name_ryakushiki)}</td>
				</tr>
</c:forEach>
			</tbody>
		</table>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script type="text/javascript">

//初期表示でカテゴリ１作成
$("#searchBunrui1").append($("<option></option>"));
var c1List = new Array();
$("#torihikiSentakuBody").find("tr").each(function(){
	var tmpC1 = $(this).find(".displayBunrui1").text();
	if ("" != tmpC1 && -1 == $.inArray(tmpC1, c1List)) c1List.push(tmpC1);
});
for (var i = 0; i < c1List.length; i++) {
	var opt = $("<option></option>");
	opt.text(c1List[i]);
	$("#searchBunrui1").append(opt);
}

/**
 * カテゴリ１選択時
 */
$("#searchBunrui1").change(c1Change);
function c1Change() {
	var c1 = $("#searchBunrui1").children("option:selected").text();

	//C2C3空にする
	$("#searchBunrui2").empty();
	$("#searchBunrui3").empty();

	//検索
	torihikiSentakuSearch();

	//C2作成
	$("#searchBunrui2").append($("<option></option>"));
	var c2List = new Array();
	$("#torihikiSentakuBody").find("tr").each(function(){
		var tmpC1 = $(this).find(".displayBunrui1").text();
		var tmpC2 = $(this).find(".displayBunrui2").text();
		if (c1 == tmpC1 && "" != tmpC2 && -1 == $.inArray(tmpC2, c2List)) c2List.push(tmpC2);
	});
	for (var i = 0; i < c2List.length; i++) {
		var opt = $("<option></option>");
		opt.text(c2List[i]);
		$("#searchBunrui2").append(opt);
	}
}

/**
 * カテゴリ２選択時
 */
$("#searchBunrui2").change(function(){
	var c1 = $("#searchBunrui1").children("option:selected").text();
	var c2 = $("#searchBunrui2").children("option:selected").text();

	//C3空にする
	$("#searchBunrui3").empty();

	//検索
	torihikiSentakuSearch();

	//C3作成
	$("#searchBunrui3").append($("<option></option>"));
	var c3List = new Array();
	$("#torihikiSentakuBody").find("tr").each(function(){
		var tmpC1 = $(this).find(".displayBunrui1").text();
		var tmpC2 = $(this).find(".displayBunrui2").text();
		var tmpC3 = $(this).find(".displayBunrui3").text();
		if (c1 == tmpC1 && c2 == tmpC2 && "" != tmpC3 && -1 == $.inArray(tmpC3, c3List)) c3List.push(tmpC3);
	});
	for (var i = 0; i < c3List.length; i++) {
		var opt = $("<option></option>");
		opt.text(c3List[i]);
		$("#searchBunrui3").append(opt);
	}
});

/**
 * カテゴリ３選択時
 */
$("#searchBunrui3").change(function(){

	//検索
	torihikiSentakuSearch();
});

/**
 * 検索ワード変更時
 */
$("#keyword").change(function(){

	//検索
	torihikiSentakuSearch();
});

/**
 * 分類とテキストで絞り込む
 */
function torihikiSentakuSearch() {
	var c1 = $("#searchBunrui1").children("option:selected");
	c1 = (1 == c1.length) ? c1.text() : "";
	var c2 = $("#searchBunrui2").children("option:selected");
	c2 = (1 == c2.length) ? c2.text() : "";
	var c3 = $("#searchBunrui3").children("option:selected");
	c3 = (1 == c3.length) ? c3.text() : "";
	var word = $("#keyword").val();
	var excludedSpaceWord = word.zenkana2hankanaWithhyphen().replace(/\s+/g, "");

	//該当するものだけ表示
	$("#torihikiSentakuBody").find("tr").css("display", "none");
	$("#torihikiSentakuBody").find("tr").each(function(){
		if (
			("" == c1 || c1 == $(this).find(".displayBunrui1").text()) &&
			("" == c2 || c2 == $(this).find(".displayBunrui2").text()) &&
			("" == c3 || c3 == $(this).find(".displayBunrui3").text()) &&
			("" == word || (
				-1 < excludedSpace($(this), ".displayBunrui1").indexOf(excludedSpaceWord) ||
				-1 < excludedSpace($(this), ".displayBunrui2").indexOf(excludedSpaceWord) ||
				-1 < excludedSpace($(this), ".displayBunrui3").indexOf(excludedSpaceWord) ||
				-1 < excludedSpace($(this), ".torihikiName").indexOf(excludedSpaceWord) ||
				-1 < excludedSpace($(this), ".kamokuName").indexOf(excludedSpaceWord)
			))
		) {
			$(this).css("display", "");
		}
	});

};

/**
 * 空白を除去した文章を返す
 */
function excludedSpace(t, str) {
	return t.find(str).text().zenkana2hankanaWithhyphen().replace(/\s+/g, "");
}

/**
 * 条件クリアボタン押下時
 * 検索条件をクリアして再検索
 */
$("#clearButton").click(function(){
	$("#keyword").val("");
	$("#searchBunrui1").children("option:first").prop('selected', true);
	c1Change();
});


/**
 * 取引選択ダイアログ：取引項目選択
 */
$("#torihikiSentakuBody").find("a.torihikiName").click(function(){

	var torihiki = $(this);

	dialogRetShiwakeEdaNo.val(torihiki.attr("data-shiwakeEdaNo"));
	// ダイアログ起動時にコールバック処理するようにしておいたshiwakeEdaNoLostFocus()
	// これにより取引取得方法が枝番手入力時と同じになった。
	if("dialogCallbackTorihikiSentaku" in window) dialogCallbackTorihikiSentaku();

	isDirty = true;

	//オシマイ
	$("#dialog").children().remove();
	$("#dialog").dialog("close");
});
</script>

