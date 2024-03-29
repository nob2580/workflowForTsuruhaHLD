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
	<section id='kensakuJouken'>
		<div>
			<form class="form-inline">
				<input type='hidden' name='denpyouId' value='${su:htmlEscape(denpyouId)}'>
				<div class='control-group'>
					<label class="label label-sub">勘定科目ｺｰﾄﾞ</label>
					<input type='text' name='kanjouKamokuCd_dialog' class='input-small'>
					<label class="label label-sub">勘定科目名</label>
					<input type='text' name='kanjouKamokuName_dialog'>
					<button type='button' id='kamokuSearchButton' class='btn'>検索</button>
				</div>
			</form>
		</div>
	</section>

	<!-- 検索結果 -->
	<section id='kamokuSearchResult'>
	    <div>
	      <table class='table-bordered table-condensed'>
	        <thead>
	          <tr>
	            <th style='width: 100px'>勘定科目</th>
	            <th style='width: 300px'>勘定科目名</th>
	          </tr>
	        </thead>
	        <c:forEach var="record" items="${list}">
	          <tbody>
	            <tr>
	              <td>${record.kamokuGaibuCd}</td>
	              <td><a class = 'link kamoku' data-cd='${record.kamokuGaibuCd}'>${record.kamokuNameRyakushiki}</a></td>
	            </tr>
	          </tbody>
	        </c:forEach>
	      </table>
	    </div>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
/**
 * 検索ボタン押下時
 * 親画面のフィールドに選択結果を反映する。
 */
$("#kamokuSearchButton").click(function(){
	$("a.kamoku").each(function(){
		var cd = $("input[name=kanjouKamokuCd_dialog]").val();
		var name = $("input[name=kanjouKamokuName_dialog]").val();
		if (wordSearch($(this).parent().prev().text(), cd) && wordSearch($(this).text(), name)) {
			$(this).parent().parent().css("display","table-row");
		} else {
			$(this).parent().parent().css("display","none");
		}
	});
});

/**
 * 勘定科目選択時
 */
$("a.kamoku").click(function(){
	var a = $(this);

	var title = a.closest("#dialog").parent().find(".ui-dialog-title").text();
	dialogRetKamokuCd.val(a.attr("data-cd"));
	commonKamokuCdLostFocus(dialogRetKamokuCd, dialogRetKamokuName, title, null, $('#kensakuJouken input[name=denpyouId]').val(), dialogRetKazeiKbn, dialogRetBunriKbn, dialogRetKariShiireKbn, dialogRetShoriGroup, dialogRetZeiritsuKbn);


	isDirty = true;

	$("#dialog").dialog("close");
});
//初期表示処理
$("#dialog").ready(function(){
	commonInit($("#kensakuJouken"));
});
</script>
