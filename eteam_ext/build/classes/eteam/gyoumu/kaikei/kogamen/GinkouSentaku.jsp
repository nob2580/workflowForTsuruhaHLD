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
				<input type='text' id='ginkouCd' class='input-small zeropadding' maxlength='4'>
				<label class="label label-sub" for='ginkouName'>銀行名称</label>
				<input type='text' id='ginkouName'>
				<button type='button' id='searchButton' class='btn'>検索</button>
			</div>
		</form></div>
	</section>
	<section>
				<input type="hidden" name='isMasterSearch' value='${isMasterSearch}'>
		<table class='table-bordered table-condensed'>
			<thead>
				<tr>
					<th>銀行ｺｰﾄﾞ</th>
					<th>銀行名称</th>
				</tr>
			</thead>
			<tbody>
<c:forEach var="record" items="${list}">
				<tr>
					<td class='ginkouCd'><c:if test='${isMasterSearch eq "1"}'><a class='link ginkouCdLink'></c:if>${su:htmlEscape(record.kinyuukikan_cd)}<c:if test='${isMasterSearch eq "1"}'></a></c:if></td>
					<td><a class='link ginkouName'>${su:htmlEscape(record.kinyuukikan_name_kana)}</a></td>
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
	var ginkouCd = $("#ginkouCd").val();
	var ginkouName = $("#ginkouName").val();
	$("a.ginkouName").each(function(){
		if(
			(ginkouCd   == "" || $(this).closest("tr").find("td.ginkouCd").text() == ginkouCd) &&
			(ginkouName == "" || 0 <= $(this).text().indexOf(ginkouName))
		) {
			$(this).closest("tr").css("display","table-row");
		}else{
			$(this).closest("tr").css("display","none");
		}
	});
});

//リンク選択
$("a.ginkouName").click(function(){
	var a =$(this);
	var title = a.closest("#dialog").parent().find(".ui-dialog-title").text();
	var isMasterSearch = $("[name=isMasterSearch]").val();
	dialogRetGinkouCd.val(isMasterSearch == "1" ? a.closest("tr").find(".ginkouCdLink").text() : a.closest("tr").find("td.ginkouCd").text());
	commonGinkouCdLostFocus(dialogRetGinkouCd, dialogRetGinkouName, title, isMasterSearch == "" ? "0" : isMasterSearch);
	
	isDirty = true;
	
	$("#dialog").empty();
	$("#dialog").dialog("close");
});

// マスターの時のみ：空名称対策でコードもリンクとして選択可能とする
$("a.ginkouCdLink").click(function(){
	var a =$(this);
	var title = a.closest("#dialog").parent().find(".ui-dialog-title").text();
	var isMasterSearch = $("[name=isMasterSearch]").val();
	dialogRetGinkouCd.val(a.closest("tr").find(".ginkouCdLink").text());
	commonGinkouCdLostFocus(dialogRetGinkouCd, dialogRetGinkouName, title, isMasterSearch == "" ? "0" : isMasterSearch);
	
	isDirty = true;
	
	$("#dialog").empty();
	$("#dialog").dialog("close");
});
</script>

