<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- エラー -->
<%@ include file="/jsp/eteam/include/InputError.jsp" %>

<!-- メイン -->
<div id='syuukeiDialogMain'>
	<input type="hidden" name="type" value="${type}">
	<input type="hidden" name="targetDate" value="${targetDate}">
	<input type="hidden" name="syuukeiBumonCd" value="${syuukeiBumonCd}">
	
	<input type="hidden" name="denpyouId" value="${denpyouId}">
	<input type="hidden" name="kihyouBumonCd" value="${kihyouBumonCd}">
	
	<section>
		<h2>検索条件</h2>
		<div class="form-horizontal">
			<div class='control-group'>
				<label class="control-label">集計部門名</label>
				<div class='controls'>
					<input type="text" name='keyword' class="input-medium" maxlength="20" value="${su:htmlEscape(keyword)}">
					<button type='button' id='btnSearch' class='btn btn-small input-inline'>検索</button>
				</div>
			</div>
		</div>
	</section>

	<section>
		<h2>検索結果</h2>
		<div>
			<table class='table-bordered table-condensed'>
				<thead>
					<tr>
						<th>集計部門コード</th>
						<th>集計部門名</th>
					</tr>
				</thead>
				<tbody id="sbsentaku_result">
					<c:forEach var="record" items="${syuukeiBumonList}">
						<tr>
							<td>${record.syuukei_bumon_cd}</td>
							<td>
								<a class='link'  data-cd='${record.syuukei_bumon_cd}' data-name='${su:htmlEscape(record.syuukei_bumon_name)}' >${su:htmlEscape(record.syuukei_bumon_name)}</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
$(document).ready(function() {
	
	//検索ボタン押下
	$("#btnSearch").click(function(){
		$("#sbsentaku_result").find(".link").each(function(){
			if ($(this).text().zenkana2hankanaWithhyphen().replace(/\s+/g, "").indexOf($("input[name=keyword]").val().zenkana2hankanaWithhyphen().replace(/\s+/g, "")) >= 0) {
				$(this).closest("tr").css("display","table-row");
			} else {
				$(this).closest("tr").css("display","none");
			}
		});
	});
});

 /**
  * コード選択時アクション
  * @param a 選択されたコード名のaタグ
  */
$("a.link").click(function(){
	var a = $(this);
	if ("dialogRetSyuukeiBumonCd" in window)				dialogRetSyuukeiBumonCd.val(a.attr("data-cd"));
 	if ("dialogRetSyuukeiBumonName" in window)				dialogRetSyuukeiBumonName.val(a.attr("data-name"));
 	if ("dialogCallbackSyuukeiBumonSentaku" in window)		dialogCallbackSyuukeiBumonSentaku();
 	
 	a.parents('#syuukeiDialogMain').parent().dialog("close");
 	a.parents('#syuukeiDialogMain').parent().remove();
});

</script>
