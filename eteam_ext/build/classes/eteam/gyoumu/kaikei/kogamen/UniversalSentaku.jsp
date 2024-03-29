<%@page import="eteam.base.EteamFormatter"%>
<%@page import="eteam.common.KaishaInfo"%>
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
	<input type='hidden' name='no' value='${no}'>
	<!-- 入力フィールド -->
	<section>
		<div><form class="form-inline">
			<input type='hidden' name='isKotei' value='${isKotei}'>
			<div class='control-group'>
				<label class="label label-sub">勘定科目ｺｰﾄﾞ</label>
				<input type='text' class='input-small' name='kamokuCd' value='${kamokuCd}' readonly>
				<label class="label label-sub">勘定科目名</label>
				<input type='text' name='kamokuName' class='input-xlarge' value='${kamokuName}' readonly>
			</div>
			<div class='control-group'>
				<label class="label label-sub" for='ufCd'>ｺｰﾄﾞ</label>
				<input type='text' id='ufCd' class='input-small'>
				<label class="label label-sub" for='ufName'>名称</label>
				<input type='text' id='ufName'>
				<button type='button' id='ufSearchButton' class='btn'>検索</button>
			</div>
		</form></div>
	</section>
	<section>
		<table class='table-bordered table-condensed'>
			<thead>
				<tr>
					<th>ｺｰﾄﾞ</th>
					<th>名称</th>
				</tr>
			</thead>
			<tbody>
<c:forEach var="record" items="${ufList}">
				<tr>
					<td class='ufCd'>${su:htmlEscape(record.uf_cd)}</td>
					<td><a class='link ufSentaku'>${su:htmlEscape(record.uf_name_ryakushiki)}</a></td>
				</tr>
</c:forEach>
			</tbody>
		</table>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
//検索ボタン押下
$("#ufSearchButton").click(function(){
	var ufCd = $("#ufCd").val();
	var ufName = $("#ufName").val();
	$("a.ufSentaku").each(function(){
		if (wordSearch($(this).closest("tr").find("td.ufCd").text(), ufCd) && wordSearch($(this).text(), ufName)) {
			$(this).closest("tr").css("display","table-row");
		}else{
			$(this).closest("tr").css("display","none");
		}
	});
});

//UF名リンク選択
$("a.ufSentaku").click(function(){
	var a = $(this);
	var title = a.closest("#dialog").parent().find(".ui-dialog-title").text();
	dialogRetUniversalFieldCd.val(a.closest("tr").find("td.ufCd").text());
	if($('input[name=isKotei]').val() == "0"){
		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, $("#dialogMain").find("input[name=no]").val(), title);
	}else{
		commonUniversalKoteiCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, $("#dialogMain").find("input[name=no]").val(), title);
	}
	
	isDirty = true;
	
	$("#dialog").empty();
	$("#dialog").dialog("close");
});

</script>

