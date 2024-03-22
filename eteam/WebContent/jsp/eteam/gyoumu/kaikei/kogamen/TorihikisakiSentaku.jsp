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

	<!-- 入力フィールド -->
	<section>
		<form class="form-inline">
		<input type='hidden' id='denpyouKbn' name='denpyouKbn' value='${su:htmlEscape(denpyouKbn)}'>
		<div class='control-group'>
			<label class="label label-sub" for='torihikisakiCd'>取引先ｺｰﾄﾞ</label>
			<input type='text' id='torihikisakiCd' name='torihikisakiCd' class='input-medium' value="${su:htmlEscape(torihikisakiCd)}">
			<label class="label label-sub" for='torihikisakiName'>取引先名</label>
			<input type='text' id='torihikisakiName' name='torihikisakiName' value="${su:htmlEscape(torihikisakiName)}">
			<button type='button' id='torihikisakiSearchButton' class='btn'>検索</button>
		</div>
		</form>
	</section>

	<!-- 検索結果 -->
	<section>
<c:if test="${fn:length(torihikisakiList) > 0}" >
		<div><table class='table-bordered table-condensed'>
			<thead>
				<tr>
					<th>取引先ｺｰﾄﾞ</th>
					<th>取引先名</th>
					<th>振込先情報</th>
				</tr>
			</thead>
			<tbody id='torihikisakiList'>
<c:forEach var="record" items="${torihikisakiList}">
				<tr>
					<td class='torihikisakiCd' style='width:120px;'>${su:htmlEscape(record.torihikisaki_cd)}</td>
					<td style='width:200px;'><a class='link torihikisakiSentaku' data-furikomisaki='${su:htmlEscape(record.furikomisaki)}'>${su:htmlEscape(record.torihikisaki_name_ryakushiki)}</a></td>
					<td class='furikomisaki'>${su:htmlEscape(record.furikomisaki)}</td>
				</tr>
</c:forEach>
			</tbody>
		</table></div>
</c:if>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>

/**
 * 検索ボタン押下時
 */
$("#torihikisakiSearchButton").click(function(){
	var kensakuForm = $(this).closest("#dialogMain").find("form");
	var kensakuCode = kensakuForm.find("#torihikisakiCd").val();
	var kensakuName = kensakuForm.find("#torihikisakiName").val();
    var denpyouKbn = kensakuForm.find("#denpyouKbn").val();
	$("#dialog").empty();
	$("#dialog").load("torihikisaki_sentaku_kensaku?torihikisakiCd=" + encodeURI(kensakuCode) + "&torihikisakiName=" + encodeURI(kensakuName) + "&denpyouKbn=" + encodeURI(denpyouKbn));
});

/**
 * 取引先選択時
 */
$("a.torihikisakiSentaku").click(function(){
	var a = $(this);
	var tr = a.closest("tr");
	var td = tr.find("td.torihikisakiCd");
    var kensakuForm = $(this).closest("#dialogMain").find("form");
    var denpyouKbn = kensakuForm.find("#denpyouKbn").val();

	var title = a.closest("#dialog").parent().find(".ui-dialog-title").text();
	dialogRetTorihikisakiCd.val(td.text());
	if ("dialogRetFurikomisaki" in window)    {
        commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd, dialogRetTorihikisakiName, title, dialogRetFurikomisaki, denpyouKbn,dialogRetJigyoushaKbn);
    }else{
        commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd, dialogRetTorihikisakiName, title, null,denpyouKbn, dialogRetJigyoushaKbn);
    }


	
	isDirty = true;

	$("#dialog").empty();
	$("#dialog").dialog("close");
});
//初期表示処理
$("#dialog").ready(function(){
	commonInit($("#dialogMain"));
});
</script>
