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
	<!-- 入力フィールド -->
	<section>
		<div><form class="form-inline">
			<div class='control-group'>
				<label class="label label-sub" for='ginkouCd'>銀行ｺｰﾄﾞ</label>
				<input type='text' id='ginkouCd' class='input-small zeropadding' maxlength='4' disabled value='${su:htmlEscape(kinyuukikanCd)}'>
				<label class="label label-sub" for='ginkouName'>銀行名称</label>
				<input type='text' id='ginkouName' disabled value='${su:htmlEscape(kinyuukikanName)}'>
			</div>
			<div class='control-group'>
				<label class="label label-sub" for='shitenCd'>支店ｺｰﾄﾞ</label>
				<input type='text' id='shitenCd' class='input-small zeropadding' maxlength='3'>
				<label class="label label-sub" for='shitenName'>支店名称</label>
				<input type='text' id='shitenName'>
				<button type='button' id='searchButton' class='btn'>検索</button>
			</div>
		</form></div>
	</section>
	<section>
		<table class='table-bordered table-condensed'>
			<thead>
				<tr>
					<th>支店ｺｰﾄﾞ</th>
					<th>支店名称</th>
				</tr>
			</thead>
			<tbody>
<c:forEach var="record" items="${list}">
				<tr>
					<td class='shitenCd'>${su:htmlEscape(record.kinyuukikan_shiten_cd)}</td>
					<td><a class='link shitenName'>${su:htmlEscape(record.shiten_name_kana)}</a></td>
				</tr>
</c:forEach>
			</tbody>
		</table>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
commonInit($("#dialogMain"));

//検索ボタン押下
$("#searchButton").click(function(){
	var shitenCd = $("#shitenCd").val();
	var shitenName = $("#shitenName").val();
	$("a.shitenName").each(function(){
		if(
			(shitenCd   == "" || $(this).closest("tr").find("td.shitenCd").text() == shitenCd) &&
			(shitenName == "" || 0 <= $(this).text().indexOf(shitenName))
		) {
			$(this).closest("tr").css("display","table-row");
		}else{
			$(this).closest("tr").css("display","none");
		}
	});
});

//リンク選択
$("a.shitenName").click(function(){
// 	dialogRetGinkouShitenCd.val($(this).closest("tr").find("td.shitenCd").text());
// 	dialogRetGinkouShitenName.val($(this).text());
	var a =$(this);
	var title = a.closest("#dialog").parent().find(".ui-dialog-title").text();
	dialogRetGinkouShitenCd.val(a.closest("tr").find("td.shitenCd").text());
	ginkouCd = a.closest("div#dialogMain").find("#ginkouCd").val();
	commonGinkouShitenCdLostFocus(dialogRetGinkouShitenCd, dialogRetGinkouShitenName, title, ginkouCd);

	isDirty = true;
	
	$("#dialog").empty();
	$("#dialog").dialog("close");
});
</script>

