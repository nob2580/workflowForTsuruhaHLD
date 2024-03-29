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

	<!-- 検索結果 -->
	<section>
		<form id='tourokuForm' class='form-horizontal'>
			<div class='empty'>
				<div class='no-more-tables empty'>
					<table class='table-bordered table-condensed'>
						<thead>
							<tr>
								<th>対象</th>
								<th>項目名</th>
								<th></th>
							</tr>
						</thead>
						<tbody id='enabled'>
							<tr>
								<td align="center"><input type="checkbox" disabled checked></td>
								<td>対象</td>
								<td>
									<input type='hidden' name='staticItem' value='true'>
								</td>
							</tr>
<c:forEach var="item" items="${itemList}">
							<tr>
								<td align="center"><input type="checkbox" name="displayItem" value="${su:htmlEscape(item.name)}" <c:if test="${!item.editable}">disabled</c:if> <c:if test="${item.display}">checked</c:if>></td>
								<td style="width:150px">
									${su:htmlEscapeBr(item.label)}
								</td>
								<td style="width:50px">
									<button type='button' name='itemUp'     class='btn btn-mini'>↑</button>
									<button type='button' name='itemDown'   class='btn btn-mini'>↓</button>
									<input type='hidden' name='itemName'	value='${su:htmlEscape(item.name)}'>
								</td>
							</tr>
</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</form>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
<!--
//入力補助
commonInit($("#dialogMain"));

//ユーザー「↑」ボタン押下時アクション
$("button[name=itemUp]").click(function() {
	var tr = $(this).closest("tr");
	if(tr.prev()){
		if ("true" != tr.prev().find("input[name=staticItem]").val()) {
			tr.insertBefore(tr.prev());
		}
	}
});

//ユーザー「↓」ボタン押下時アクション
$("button[name=itemDown]").click(function() {
	var tr = $(this).closest("tr");
	if(tr.next()){
		if ("true" != tr.next().find("input[name=staticItem]").val()) {
			tr.insertAfter(tr.next());
		}
	}
});

//-->
</script>
