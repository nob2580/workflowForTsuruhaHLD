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
					<label class="label label-sub">ｾｸﾞﾒﾝﾄｺｰﾄﾞ</label>
					<input type='text' name='segmentCd_dialog' class='input-small'>
					<label class="label label-sub">ｾｸﾞﾒﾝﾄ名</label>
					<input type='text' name='segmentName_dialog'>
					<button type='button' id='segmentSearchButton' class='btn'>検索</button>
				</div>
			</form>
		</div>
	</section>

	<!-- 検索結果 -->
	<section id='segmentSearchResult'>
	    <div>
	      <table class='table-bordered table-condensed'>
	        <thead>
	          <tr>
	            <th style='width: 100px'>ｾｸﾞﾒﾝﾄｺｰﾄﾞ</th>
	            <th style='width: 300px'>ｾｸﾞﾒﾝﾄ名</th>
	          </tr>
	        </thead>
	        <c:forEach var="record" items="${list}">
	          <tbody>
	            <tr>
	              <td>${record.segment_cd}</td>
	              <td><a class = 'link pj' data-cd='${record.segment_cd}'>${record.segment_name_ryakushiki}</a></td>
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
$("#segmentSearchButton").click(function(){
	$("a.pj").each(function(){
		var cd = $("input[name=segmentCd_dialog]").val();
		var name = $("input[name=segmentName_dialog]").val();
		if (wordSearch($(this).parent().prev().text(), cd) && wordSearch($(this).text(), name)) {
			$(this).parent().parent().css("display","table-row");
		}else{
			$(this).parent().parent().css("display","none");
		}
	});
});

/**
 * セグメントコード選択時
 */
$("a.pj").click(function(){
	var a = $(this);

	var title = a.closest("#dialog").parent().find(".ui-dialog-title").text();
	dialogRetSegmentCd.val(a.attr("data-cd"));
	commonSegmentCdLostFocus(dialogRetSegmentCd, dialogRetSegmentName, title);

	
	isDirty = true;

	$("#dialog").dialog("close");
});
$("#dialog").ready(function(){
	commonInit($("#kensakuJouken"));
});
</script>
