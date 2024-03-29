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
					<label class="label label-sub" for='d_kijunBi'>基準日</label>
					<input type='text' id='kijunDate' name='d_kijunBi' class='input-small datepicker' value='${su:htmlEscape(d_kijunBi)}'>
					<button type='button' id='d_bumonKijunBiButton' class='btn'>表示</button>
				</div>
				<div class='control-group'>
					<label class="label label-sub" for='d_bumonCd'>部門ｺｰﾄﾞ</label>
					<input type='text' id='d_bumonCd' name='d_bumonCd' class='input-small'>
					<label class="label label-sub" for='d_bumonName'>部門名</label>
					<input type='text' id='d_bumonName' name='d_bumonName'>
					<button type='button' id='d_bummonSearchButton' class='btn'>検索</button>
				</div>
			</form>
		</div>
	</section>

	<section class='thumbnail'>
		<div id='bumon'>
<c:forEach var="record" items="${list}">
						<p>${record.prefix}<i class='icon-chevron-right' style="margin-top: -1px"></i><a class='bumonSentaku link ${record.bg_color}' data-cd='${record.bumon_cd}' data-name='${su:htmlEscape(record.bumon_name)}' data-fullname='${su:htmlEscape(record.full_bumon_name)}' >${su:htmlEscape(record.bumon_name)}（${record.bumon_cd}）</a></p>
</c:forEach>
		</div>
	</section>
	
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>

/**
 * 部門検索
 */
function d_bumonSearch() {
	$("#dialogMain").find("a.bumonSentaku").each(function(){
		var cd = $("input[name=d_bumonCd]").val();
		var name = $("input[name=d_bumonName]").val();
		if (wordSearch($(this).attr("data-cd"), cd) && wordSearch($(this).text(), name)) {
			$(this).closest("p").show();
		}else{
			$(this).closest("p").hide();
		}
	});
}

/**
 * 基準日指定
 */
function d_bumonKijunBi() {
	var kijunbi = $("input[name=d_kijunBi]").val();
	$("#dialog").load("bumon_sentaku?d_kijunBi="+kijunbi);
}

/**
 * 部門検索ダイアログ：部門選択
 * @param a 押下した部門リンク
 */

	// 部門名と部門コードを呼び出し元のオブジェクトへ設定します。

$("a").click(function(){
	var a = $(this);
	if ("dialogRetBumonSentakuCallback" in window && dialogRetBumonSentakuCallback != null) {
		dialogRetBumonSentakuCallback(a);
	}else{
		var title = a.closest("#dialog").parent().find(".ui-dialog-title").text();
		dialogRetBumonCd.val(a.attr("data-cd"));
		commonBumonCdLostFocus(true, dialogRetBumonCd, dialogRetBumonName, title);
	}

	$("#dialog").children().remove();
	$("#dialog").dialog("close");  
});

  

//部門検索ボタン
$("#d_bummonSearchButton").click(function(){
	d_bumonSearch();
});

//基準日ボタン
$("#d_bumonKijunBiButton").click(function(){
	d_bumonKijunBi();
});

//初期表示処理
$("#dialog").ready(function(){
	commonInit($("#kensaku"));
	var elem = document.activeElement;
	if (elem) {
		elem.blur();
	}
});
</script>
