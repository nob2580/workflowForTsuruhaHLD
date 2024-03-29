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
			<div class='control-group'>
				<label class="label label-sub" for='hfCd'>ｺｰﾄﾞ</label>
				<input type='text' id='hfCd' class='input-small' value='${su:htmlEscape(futanBumonCd)}'>
				<label class="label label-sub" for='hfName'>名称</label>
				<input type='text' id='hfName' value='${su:htmlEscape(futanBumonName)}'>
				<button type='button' id='hfSearchButton' class='btn'>検索</button>
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
<c:forEach var="record" items="${hfList}">
				<tr>
					<td class='hfCd'>${su:htmlEscape(record.hf_cd)}</td>
					<td><a class='link hfSentaku'>${su:htmlEscape(record.hf_name_ryakushiki)}</a></td>	            
				</tr>
</c:forEach>
			</tbody>
		</table>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
//検索ボタン押下
$("#hfSearchButton").click(function(){
	var hfCd = $("#hfCd").val();
	var hfName = $("#hfName").val();
	$("a.hfSentaku").each(function(){
		if (wordSearch($(this).closest("tr").find("td.hfCd").text(), hfCd) && wordSearch($(this).text(), hfName)) {
			$(this).closest("tr").css("display","table-row");
		}else{
			$(this).closest("tr").css("display","none");
		}
	});
});

//HF名リンク選択
$("a.hfSentaku").click(function(){
	var a = $(this);
	var title = a.closest("#dialog").parent().find(".ui-dialog-title").text();
	dialogRetHeaderFieldCd.val(a.closest("tr").find("td.hfCd").text());
	commonHeaderCdLostFocus(dialogRetHeaderFieldCd, dialogRetHeaderFieldName, $("#dialogMain").find("input[name=no]").val(), title);


	isDirty = true;
	
	$("#dialog").empty();
	$("#dialog").dialog("close");
});
</script>

