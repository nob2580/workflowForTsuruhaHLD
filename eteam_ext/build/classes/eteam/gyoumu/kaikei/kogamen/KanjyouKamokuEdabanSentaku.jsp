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
	<section id='kamokuEdabanSearchJouken'>
		<div>
			<form class="form-inline">
				<input type='hidden' name='denpyouKbn' value='${su:htmlEscape(denpyouKbn)}'>
				<input type='hidden' name='shiwakeEdano' value='${su:htmlEscape(shiwakeEdano)}'>
<c:if test="${!empty kamokuCd && !empty kamokuName}">
				<div class='control-group'>
					<label class="label label-sub">勘定科目ｺｰﾄﾞ</label>
					<input type='text' class='input-small' name='kamokuCd' value='${su:htmlEscape(kamokuCd)}' readonly>
					<label class="label label-sub">勘定科目名</label>
					<input type='text' name='kamokuName' value='${su:htmlEscape(kamokuName)}' readonly>
				</div>
</c:if>
				
				<div class='control-group'>
					<label class="label label-sub">勘定科目枝番コード</label>
					<input type='text' name='kamokuEdabanCd_dialog' class='input-small'>
					<label class="label label-sub">勘定科目枝番名</label>
					<input type='text' name='kamokuEdabanName_dialog'>
					<button type='button' id='kamokuEdabanSearchButton' class='btn'>検索</button>
				</div>
			</form>
		</div>
	</section>

	<section id='kamokuEdabanSearchResult'>
	    <div>
			<table class='table-bordered table-condensed'>
				<thead>
					<tr>
						<th style='width: 200px'>勘定科目枝番コード</th>
						<th style='width: 300px'>勘定科目枝番名</th>
					</tr>
				</thead>
				<c:forEach var="record" items="${list}">
					<tbody>
						<tr>
							<td class='kamokuEdabanCd'>${record.kamokuEdabanCd}</td>
							<td><a class='link kamokuEdabanName'>${record.edabanName}</a></td>
						</tr>
					</tbody>
				</c:forEach>
			</table>
		</div>
	</section>
</div>
<!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
/**
 * 検索ボタン押下時
 * 親画面のフィールドに選択結果を反映する。
 */
$("#kamokuEdabanSearchButton").click(function(){
	$("a.kamokuEdabanName").each(function(){
		var cd = $("input[name=kamokuEdabanCd_dialog]").val();
		var name = $("input[name=kamokuEdabanName_dialog]").val();
		if (wordSearch($(this).parent().prev().text(), cd) && wordSearch($(this).text(), name)) {
			$(this).parent().parent().css("display","table-row");
		}else{
			$(this).parent().parent().css("display","none");
		}
		
	});
});

/**
 * 勘定科目枝番選択時
 */
$("a.kamokuEdabanName").click(function(){
	var a = $(this);
	var title = a.closest("#dialog").parent().find(".ui-dialog-title").text();
	dialogRetKamokuEdabanCd.val(a.closest("tr").find("td.kamokuEdabanCd").text());
	// 代入していないdialogRetは呼び出し元からの引継ぎ
	commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, a.closest("#dialogMain").find("input[name=kamokuCd]").val(), title, dialogRetKazeiKbn, dialogRetBunriKbn,  a.closest("#dialogMain").find("input[name=denpyouKbn]").val(),  a.closest("#dialogMain").find("input[name=shiwakeEdano]").val());

	isDirty = true;

	$("#dialog").dialog("close");
});
//初期表示処理
$("#dialog").ready(function(){
	commonInit($("#kamokuEdabanSearchJouken"));
});
</script>
