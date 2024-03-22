<%@ page import="eteam.symbol.EteamSymbol"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!DOCTYPE html>
<html lang='ja'>
	<head>
		<meta charset='utf-8'>
		<title>データアクセス｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
<!--

-->
		</style>
	</head>
	<body>
		<div id='wrap'>
			<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>
			<!-- 中身 -->
			<div id='content' class='container'>
				<!-- タイトル -->
				<h1>データアクセス</h1>
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'>
					<form id='myForm'>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>テーブル名(和名)</th>
										<th>テーブル名(物理名)</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>${su:htmlEscape(tableComment)}</td>
										<td>${su:htmlEscape(tableName)}<input type='hidden' name='tableName' value='${su:htmlEscape(tableName)}'></td>
									</tr>
								</tbody>
							</table>
						</div>
						<section>
							<h2>検索条件</h2>
							<div class='form-horizontal'>
								<div class='control-group'>
									<label class='control-label'>条件１</label>
									<div class='controls'>
										<select name='column1' class='input-medium'>
											<option value='' selected></option>
										<c:forEach var="tmpColumn" items="${columnList}">
											<option value='${tmpColumn.column_name}' <c:if test='${tmpColumn.column_name eq column1}'>selected</c:if>>${tmpColumn.column_comment}</option>
										</c:forEach>
										</select>
										<select name='condition1' class='input-medium'>
											<option value='' selected></option>
											<option value='1' <c:if test='${condition1 eq "1"}'>selected</c:if>>＝（等しい）</option>
											<option value='2' <c:if test='${condition1 eq "2"}'>selected</c:if>>≠（等しくない）</option>
											<option value='3' <c:if test='${condition1 eq "3"}'>selected</c:if>>&gt;＝（以上）</option>
											<option value='4' <c:if test='${condition1 eq "4"}'>selected</c:if>>&lt;＝（以下）</option>
										</select>
										<input type='text' name='whereVal1' class='input-medium' maxlength='100' value='${su:htmlEscape(whereVal1)}'>
									</div>
								</div>
								<div class='control-group'>
									<label class='control-label'>条件２</label>
									<div class='controls'>
										<select name='column2' class='input-medium'>
											<option value='' selected></option>
										<c:forEach var="tmpColumn" items="${columnList}">
											<option value='${tmpColumn.column_name}' <c:if test='${tmpColumn.column_name eq column2}'>selected</c:if>>${tmpColumn.column_comment}</option>
										</c:forEach>
										</select>
										<select name='condition2' class='input-medium'>
											<option value='' selected></option>
											<option value='1' <c:if test='${condition2 eq "1"}'>selected</c:if>>＝（等しい）</option>
											<option value='2' <c:if test='${condition2 eq "2"}'>selected</c:if>>≠（等しくない）</option>
											<option value='3' <c:if test='${condition2 eq "3"}'>selected</c:if>>&gt;＝（以上）</option>
											<option value='4' <c:if test='${condition2 eq "4"}'>selected</c:if>>&lt;＝（以下）</option>
										</select>
										<input type='text' name='whereVal2' class='input-medium' maxlength='100' value='${su:htmlEscape(whereVal2)}'>
									</div>
								</div>
								<div class='control-group'>
									<label class='control-label'>条件３</label>
									<div class='controls'>
										<select name='column3' class='input-medium'>
											<option value='' selected></option>
										<c:forEach var="tmpColumn" items="${columnList}">
											<option value='${tmpColumn.column_name}' <c:if test='${tmpColumn.column_name eq column3}'>selected</c:if>>${tmpColumn.column_comment}</option>
										</c:forEach>
										</select>
										<select name='condition3' class='input-medium'>
											<option value='' selected></option>
											<option value='1' <c:if test='${condition3 eq "1"}'>selected</c:if>>＝（等しい）</option>
											<option value='2' <c:if test='${condition3 eq "2"}'>selected</c:if>>≠（等しくない）</option>
											<option value='3' <c:if test='${condition3 eq "3"}'>selected</c:if>>&gt;＝（以上）</option>
											<option value='4' <c:if test='${condition3 eq "4"}'>selected</c:if>>&lt;＝（以下）</option>
										</select>
										<input type='text' name='whereVal3' class='input-medium' maxlength='100' value='${su:htmlEscape(whereVal3)}'>
									</div>
								</div>
							</div>
							<div class='blank'></div>
							<div>
								<button type='button' class='btn' id='searchBtn'><i class='icon-search'></i> 検索実行</button>
								<button type='button' class='btn' id='clearBtn'><i class='icon-remove-circle'></i> 条件クリア</button>
							</div>
						</section>
	
						<!-- 一覧部 -->
						<section>
							<h2>検索結果</h2>
							<div>
								<button type='button' class='btn' id='csvBtn'><i class='icon-download'></i> CSV出力</button>
							</div>
							<div class='blank'></div>
							<div id="List" class='no-more-tables'>
								${listData}
								<%@ include file='/jsp/eteam/include/Paging.jsp' %>
							</div>
						</section>
					</form>
				</div><!-- main -->
			</div><!-- content -->
			<div id='push'></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script type="text/javascript">
<!-- 
 $("#clearBtn").click(function(){
	inputClear($(this).closest("form"));
});
 $("#searchBtn").click(function(){
	 
	 var column1 = $("select[name=column1]").val();
	 var column2 = $("select[name=column2]").val();
	 var column3 = $("select[name=column3]").val();
	 var condition1 = $("select[name=condition1]").val();
	 var condition2 = $("select[name=condition2]").val();
	 var condition3 = $("select[name=condition3]").val();
	 var whereVal1 = $("input[name=whereVal1]").val();
	 var whereVal2 = $("input[name=whereVal2]").val();
	 var whereVal3 = $("input[name=whereVal3]").val();
	 
	 if (column1 != "" || condition1 != "" || whereVal1 != "") {
	 	 if (column1 != "" && condition1 != "" && whereVal1 != "") {
	 	 } else {
	 		alert("条件１の内容をすべて入力してください。");
	 		return;
	 	 }
	 }
	 if (column2 != "" || condition2 != "" || whereVal2 != "") {
	 	 if (column2 != "" && condition2 != "" && whereVal2 != "") {
	 	 } else {
	 		alert("条件２の内容をすべて入力してください。");
	 		return;
	 	 }
	 }
	 if (column3 != "" || condition3 != "" || whereVal3 != "") {
	 	 if (column3 != "" && condition3 != "" && whereVal3 != "") {
	 	 } else {
	 		alert("条件３の内容をすべて入力してください。");
	 		return;
	 	 }
	 }
	  
	 $("#myForm").attr("action" , "data_access_kensaku");
	 $("#myForm").attr("method" , "post");
	 $("#myForm").submit();
 });
$("#csvBtn").click(function(){
	$("#myForm").attr("action" , "data_access_csvoutput");
	$("#myForm").attr("method" , "post");
	$("#myForm").submit();
});
-->
		</script>
	</body>
</html>
