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
		<title>支払依頼データ作成｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
<!--
#description tbody th {
	text-align: left;
}
#description thead th {
	width: 160px;
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
				<h1>支払依頼データ作成</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='myForm' class='form-horizontal' method='post'>
				    <p id='exeRetMsg' style='display:none;'>${su:htmlEscape(exeRetMsg)}</p>
					<section>
						<div>
							<div class='control-group'>
								<label class='control-label' for=''><span class='required'>*</span>支払予定日</label>
								<div class='controls'>
									<select name='kojinSeisanShiharaiBi' class='input-large'>
										<option></option>
										<c:forEach var='record' items='${kojinSeisanShiharaiBiList}'>
											<option value='${su:htmlEscape(record.cd)}' <c:if test='${kojinSeisanShiharaiBi eq record.cd}'>selected</c:if>>${su:htmlEscape(record.val)}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class='control-group' style='display:none;'>
								<label class='control-label' for=''>支払種別</label>
								<div class='controls'>
									<select name="shiharaiShubetsu" class="input-small">
										<option value=''></option>
										<c:forEach var="tmpShiharaiShubetsu" items="${shiharaiShubetsuList}">
											<option value='${tmpShiharaiShubetsu.naibu_cd}' <c:if test='${tmpShiharaiShubetsu.naibu_cd eq shiharaiShubetsu}'>selected</c:if>>${su:htmlEscape(tmpShiharaiShubetsu.name)}</option>
										</c:forEach>
									</select>
									<span>※①債務支払データ作成にのみ影響</span>
								</div>
							</div>
						</div>
					</section>
					<section>
						<div style='display:none;'>
							<button type='button' id='torihikisakiButtonMisakusei' class='btn'><i class='icon-random'></i> ①取引先マスタデータ作成(未作成のみ)</button>
							<button type='button' id='torihikisakiButtonAll' class='btn'><i class='icon-random'></i> ①取引先マスタデータ作成(未作成+作成済)</button>
						</div>
						<div class='blank'></div>
						<div>
							<button type='button' id='saimuButtonMisakusei' class='btn' <c:if test='${saimuShiyouFlg eq "0"}'>disabled</c:if>><i class='icon-random'></i> ①債務支払データ作成(未作成のみ)</button>
							<button type='button' id='saimuButtonAll' class='btn' <c:if test='${saimuShiyouFlg eq "0"}'>disabled</c:if>><i class='icon-random'></i> ①債務支払データ作成(未作成+作成済)</button>
						</div>
						<div class='blank'></div>
						<div>
							<button type='button' id='fbButtonMisakusei' class='btn'><i class='icon-random'></i> ②FBデータ作成(未作成のみ)</button>
							<button type='button' id='fbButtonAll' class='btn'><i class='icon-random'></i> ②FBデータ作成(未作成+作成済)</button>
						</div>
					</section>
					<br>
					<section>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed' id='description'>
								<thead>
									<tr>
										<th>　</th>
										<th style='display:none;'>①取引先マスタ<br>データ作成</th>
										<th>①債務支払<br>データ作成</th>
										<th>②FB<br>データ作成</th>
										<th>支払仕訳<br>作成</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<th>振込(定期)(マスタ)</th>
										<td style='display:none;'>☓</td>
										<td>○</td>
										<td>☓</td>
										<td>☓</td>
									</tr>
									<tr>
										<th>振込(その他)(一見)</th>
										<td style='display:none;'>☓</td>
										<td>☓</td>
										<td>○</td>
										<td>○</td>
									</tr>
								</tbody>
							</table>
						</div>
					</section>
				</form></div><!-- main -->
			</div><!-- /container -->
			<div id='push'></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>

$(document).ready(function(){

	//取引先ボタン押下時
	$("#torihikisakiButtonMisakusei").click(function(){
		var val =  $("select[name=kojinSeisanShiharaiBi] option:selected").val();
		var shubetsu =  $("select[name=shiharaiShubetsu] option:selected").text();
		if (val == "") {
			alert("支払予定日を選択してください。");
			return;
		}
		var msg = "";
		msg = msg + "取引先マスタデータ(未作成のみ)を作成します。よろしいですか？\n\n";
		msg = msg + "支払日予定「" + val + "」\n\n";
		msg = msg + "支払種別「" + shubetsu + "」\n\n";
		if (window.confirm(msg)) {
			$("#myForm").attr("action", "shiharaiirai_output_torihikisaki_misakusei").submit();
		}
	});
	$("#torihikisakiButtonAll").click(function(){
		var val =  $("select[name=kojinSeisanShiharaiBi] option:selected").val();
		var shubetsu =  $("select[name=shiharaiShubetsu] option:selected").text();
		if (val == "") {
			alert("支払予定日を選択してください。");
			return;
		}
		var msg = "";
		msg = msg + "取引先マスタデータ(未作成+作成済)を作成します。よろしいですか？\n\n";
		msg = msg + "支払日予定「" + val + "」\n\n";
		msg = msg + "支払種別「" + shubetsu + "」\n\n";
		if (window.confirm(msg)) {
			$("#myForm").attr("action", "shiharaiirai_output_torihikisaki_all").submit();
		}
	});

	//債務ボタン押下時
	$("#saimuButtonMisakusei").click(function(){
		var val =  $("select[name=kojinSeisanShiharaiBi] option:selected").val();
		var shubetsu =  $("select[name=shiharaiShubetsu] option:selected").text();
		if (val == "") {
			alert("支払予定日を選択してください。");
			return;
		}
		var msg = "";
		msg = msg + "債務支払データ(未作成のみ)を作成します。よろしいですか？\n\n";
		msg = msg + "支払日予定「" + val + "」\n\n";
		//msg = msg + "支払種別「" + shubetsu + "」\n\n";
		if (window.confirm(msg)) {
			$("#myForm").attr("action", "shiharaiirai_output_saimu_misakusei").submit();
		}
	});
	$("#saimuButtonAll").click(function(){
		var val =  $("select[name=kojinSeisanShiharaiBi] option:selected").val();
		var shubetsu =  $("select[name=shiharaiShubetsu] option:selected").text();
		if (val == "") {
			alert("支払予定日を選択してください。");
			return;
		}
		var msg = "";
		msg = msg + "債務支払データ(未作成+作成済)を作成します。よろしいですか？\n\n";
		msg = msg + "支払日予定「" + val + "」\n\n";
		//msg = msg + "支払種別「" + shubetsu + "」\n\n";
		if (window.confirm(msg)) {
			$("#myForm").attr("action", "shiharaiirai_output_saimu_all").submit();
		}
	});

	//作成ボタン押下時
	$("#fbButtonMisakusei").click(function(){
		var val =  $("select[name=kojinSeisanShiharaiBi] option:selected").val();
		if (val == "") {
			alert("支払予定日を選択してください。");
			return;
		}
		var msg = "";
		msg = msg + "FBデータ(未作成のみ)を作成します。よろしいですか？\n\n";
		msg = msg + "支払予定日「" + val + "」\n\n";
		msg = msg + "既に金融機関へ送信済みの場合は、\n各金融機関へお問い合わせ後に作成・送信してください。";
		if (window.confirm(msg)) {
			$("#myForm").attr("action", "shiharaiirai_output_fb_misakusei").submit();
		}
	});
	$("#fbButtonAll").click(function(){
		var val =  $("select[name=kojinSeisanShiharaiBi] option:selected").val();
		if (val == "") {
			alert("支払予定日を選択してください。");
			return;
		}
		var msg = "";
		msg = msg + "FBデータ(未作成+作成済)を作成します。よろしいですか？\n\n";
		msg = msg + "支払予定日「" + val + "」\n\n";
		msg = msg + "既に金融機関へ送信済みの場合は、\n各金融機関へお問い合わせ後に作成・送信してください。";
		if (window.confirm(msg)) {
			$("#myForm").attr("action", "shiharaiirai_output_fb_all").submit();
		}
	});

	//債務支払データ作成処理後の再表示でEXEの処理結果メッセージ
	if($("#exeRetMsg").text() != ""){
	   alert($("#exeRetMsg").text());
    }
});
</script>
	</body>
</html>
