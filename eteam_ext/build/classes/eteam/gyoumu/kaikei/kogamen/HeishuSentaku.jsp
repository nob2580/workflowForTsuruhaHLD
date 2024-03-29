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
				<div class='control-group'>
					<label class="label label-sub">幣種ｺｰﾄﾞ</label>
					<input type='text' name='heishuCd_dialog' class='input-small'>
					<label class="label label-sub">国または地域</label>
					<input type='text' name='countryName_dialog'>
					<button type='button' id='heishuSearchButton' class='btn'>検索</button>
				</div>
			</form>
		</div>
	</section>

	<!-- 検索結果 -->
	<section id='heishuSearchResult'>
	    <div>
	      <table class='table-bordered table-condensed'>
	        <thead>
	          <tr>
	            <th style='width: 80px'>幣種ｺｰﾄﾞ</th>
	            <th style='width: 120px'>通貨単位</th>
	            <th style='width: 150px'>国または地域</th>
	            <th style='width: 100px'>レート</th>
	          </tr>
	        </thead>
	        <c:forEach var="record" items="${list}">
	          <tbody>
	            <tr>
	              <td><a class = 'link heishu' data-tsuukatani='${record.currency_unit}' data-rate='${record.rate}'>${record.heishu_cd}</a></td>
	              <td>${record.currency_unit}</td>
	              <td>${record.country_name}</td>
	              <td><fmt:formatNumber value ="${record.rate}" pattern="##0.00000###"/></td>
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
$("#heishuSearchButton").click(function(){
	$("a.heishu").each(function(){
		var cd = $("input[name=heishuCd_dialog]").val();
		var name = $("input[name=countryName_dialog]").val();
		if (wordSearch($(this).text(), cd) && wordSearch($(this).parent().next().next().text(), name)) {
			$(this).parent().parent().css("display","table-row");
		} else {
			$(this).parent().parent().css("display","none");
		}
	});
});

/**
 * 幣種選択時
 */
$("a.heishu").click(function(){
	var a = $(this);
	
	var title = a.closest("#dialog").parent().find(".ui-dialog-title").text();
	dialogRetHeishuCd.val(a.text());
	commonHeishuCdLostFocus(dialogRetHeishuCd, dialogRetTsuukaTani, dialogRetRate, title);

	isDirty = true;

	$("#dialog").dialog("close");
});
//初期表示処理
$("#dialog").ready(function(){
	commonInit($("#kensakuJouken"));
});
</script>
