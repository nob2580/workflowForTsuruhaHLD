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
		<title>マスターデータ変更確認｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
<!--

b.black {
	color: black;
	font-size: large;
}
b.red {
	color: #FF0000;
	font-size: large;
}

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
				<h1>マスターデータ変更確認</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id="myForm" method="post" action='master_data_henkou_touroku' enctype="multipart/form-data" class='form-horizontal'>
					<input type="hidden" name='masterId'   value='${su:htmlEscape(masterId)}'>
					<input type="hidden" name='masterName' value='${su:htmlEscape(masterName)}'>

					<!-- ぱんくず -->
					<section><h3>
						<a href='master_data_ichiran'>一覧</a>
						&gt;
						<a href='master_data_shoukai?masterId=${su:htmlEscape(masterId)}'>照会</a>
					</h3></section>
						
					<!-- マスター属性表示 -->
					<section>
						<h2>マスターデータ</h2>
						<div>
							<div class='control-group'>
								<label class='control-label'>マスターID</label>
								<div class='controls non-input'><b>${su:htmlEscape(masterId)}</b></div>
							</div>
							<div class='control-group'>
								<label class='control-label'>マスター名</label>
								<div class='controls non-input'><b>${su:htmlEscape(masterName)}</b></div>
							</div>
						</div>
					</section>
						
					<!-- ファイル名 -->
					<section>
						<h2>CSVファイル</h2>
						<div>
							<div class='control-group' style="padding-left: 30px;"><b>${su:htmlEscape(uploadFileFileName)}</b></div>
						</div>
					</section>

					<!-- データ -->
					<section>
						<h2>データ</h2>
						<c:if test='${masterId ne "kinyuukikan"}'>
						<div class='control-group' style="padding: 5px 5px 10px 10px;">
							<input type="checkbox" id='viewCheck' style='margin-top:-2px;'>
							<b class='red'> ※現在のテーブル内容を表示する。</b>
						</div>
						</c:if>
						<div id='disp' style='display:none'>
							<div class='control-group' style="margin-bottom: 5px;">
								<b class='black'>---テーブルデータ---</b>
							</div>
							<div class='no-more-tables'>
								<table class='table-bordered table-condensed'>
									<thead>
										<tr>
<c:forEach var="record" items="${currentColumnList}">
											<th>${su:htmlEscape(record.column_comment)}</th>
</c:forEach>
										</tr>
										<tr>
<c:forEach var="record" items="${currentColumnList}">
											<th>${su:htmlEscape(record.column_name)}</th>
</c:forEach>
										</tr>
									</thead>
									<tbody>
<c:forEach var="record" items="${currentDataList}">
										<tr>
	<c:forEach var="data" items="${record}" varStatus="st" >
											<td <c:if test='${ fn:substring(dataTypeList[st.index],0,7).toLowerCase() eq "decimal"}'> align="right"</c:if>>${su:htmlEscape(data)} </td>
	</c:forEach>
										</tr>
</c:forEach>
									</tbody>
								</table>
							</div>
							<div class='control-group' style="margin-top: 15px; margin-bottom: 5px;">
								<b class='black'>---CSVファイルデータ---</b>
							</div>
						</div>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
<c:forEach var="record" items="${logicalNameList}">
										<th>${su:htmlEscape(record)}</th>
</c:forEach>
									</tr>
									<tr>
<c:forEach var="record" items="${physicalNameList}">
										<th>${su:htmlEscape(record)}</th>
</c:forEach>
									</tr>
								</thead>
								<tbody>
<c:forEach var="record" items="${dataList}">
									<tr>
	<c:forEach var="data" items="${record}" varStatus="st">
										<td <c:if test='${ fn:substring(dataTypeList[st.index],0,7).toLowerCase() eq "decimal"}'> align="right"</c:if>>${su:htmlEscape(data)}  </td>
	</c:forEach>
									</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
					</section>

					<!-- 処理ボタン -->
					<section>
						<p><b>上記の内容で変更しても宜しいですか？</b></p>
						<button type="submit" class='btn'><i class='icon-ok'></i> ＯＫ</button>
						<button type='button' id='modoruButton' class='btn'><i class='icon-step-backward'></i> 戻る</button>
					</section>
				</form></div><!-- main -->
			</div><!-- content -->
			<div id="push"></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>

$(document).ready(function(){

	//現在内容表示チェック
	$("#viewCheck").click(function(){
		document.getElementById("disp").style.display = $(this).prop("checked") ? "block" : "none";
	});

	//戻るボタン押下
	$("#modoruButton").click(function(){
		history.back();
	});
});
		</script>
	</body>
</html>
