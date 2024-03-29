<%@page import="eteam.base.EteamFormatter"%>
<%@page import="eteam.common.KaishaInfo"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- エラー -->
<input type='hidden' id="errorCnt" value='${fn:length(errorList)}'>
<%@ include file="/jsp/eteam/include/InputError.jsp" %>

<style type='text/css'>
<!--
th.shousairan {
	text-align: right;
}
td.shousairan {
	width: 66%;
}
-->
</style>

<!-- メイン -->
<div id='dialogMain'>

	<!-- 入力フィールド -->
	<section>

		<h2>起案番号詳細情報</h2>
		<table class='table-bordered table-condensed'>
			<thead>
				<tr>
					<th><br></th>
					<th><nobr>年度</nobr></th>
					<th><nobr>番号</nobr></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th class='shousairan'>実施起案番号</th>
					<td>${su:htmlEscape(jisshiNendo)}</td>
					<td style="white-space:nowrap;">${su:htmlEscape(jisshiKianBangou)}</td>
				</tr>
				<tr>
					<th class='shousairan'>支出起案番号</th>
					<td>${su:htmlEscape(shishutsuNendo)}</td>
					<td style="white-space:nowrap;">${su:htmlEscape(shishutsuKianBangou)}</td>
				</tr>
			</tbody>
		</table>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>

// 初期表示処理
$("#dialog").ready(function(){
});

</script>

