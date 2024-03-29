<%@ page import="eteam.symbol.EteamSymbol"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!DOCTYPE html>
<html lang="ja">
	<head>
		<meta charset="utf-8">
		<title>支払依頼書出力｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
	</head>

	<body>
    	<div id="wrap">

    		<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
  				<h1>支払依頼書出力</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main' class='form-horizontal'><form id='myForm'>
					<input type='hidden' name="seigyoUserId" value='${su:htmlEscape(sessionScope.user.seigyoUserId)}'>

					<!-- 検索枠 -->
					<section>
						<h2>検索条件</h2>
						<div id="kensakuList" class='no-more-tables'>
							<div class='control-group'>
								<label class='control-label'>出力状況</label>
								<div class='controls'>
									<select name='shutsuryokuJoukyou' class='input-medium'>
										<option value=''></option>
<c:forEach var="shutsuryokuJoukyouList" items="${shutsuryokuJoukyouList}">
										<option value='${su:htmlEscape(shutsuryokuJoukyouList.naibu_cd)}' <c:if test='${su:htmlEscape(shutsuryokuJoukyouList.naibu_cd) eq shutsuryokuJoukyou}'>selected</c:if>>${su:htmlEscape(shutsuryokuJoukyouList.name)}</option>
</c:forEach>
									</select>
									<label class='label' style='display:none;'>支払種別</label>
									<select name='shiharaiShubetsu' class='input-medium' style='display:none;'>
										<option value=''></option>
<c:forEach var="shiharaiShubetsuList" items="${shiharaiShubetsuList}">
										<option value='${su:htmlEscape(shiharaiShubetsuList.naibu_cd)}' <c:if test='${su:htmlEscape(shiharaiShubetsuList.naibu_cd) eq shiharaiShubetsu}'>selected</c:if>>${su:htmlEscape(shiharaiShubetsuList.name)}</option>
</c:forEach>
									</select>
									<label class='label'>承認状況</label>
									<select name='shouninJoukyou' class='input-medium'>
										<option value=''></option>
<c:forEach var="shouninJoukyouList" items="${shouninJoukyouList}">
										<option value='${su:htmlEscape(shouninJoukyouList.naibu_cd)}' <c:if test='${su:htmlEscape(shouninJoukyouList.naibu_cd) eq shouninJoukyou}'>selected</c:if>>${su:htmlEscape(shouninJoukyouList.name)}</option>
</c:forEach>
									</select>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>計上日</label>
								<div class='controls'>
									<input type='text' name='keijouBiFrom' class='input-small datepicker input-inline' value='${su:htmlEscape(keijouBiFrom)}'>
									～
									<input type='text' name='keijouBiTo' class='input-small datepicker input-inline' value='${su:htmlEscape(keijouBiTo)}'>
									<label class='label'>支払日</label>
									<input type='text' name='shiharaiBiFrom' class='input-small datepicker input-inline' value='${su:htmlEscape(shiharaiBiFrom)}'>
									～
									<input type='text' name='shiharaiBiTo' class='input-small datepicker input-inline' value='${su:htmlEscape(shiharaiBiTo)}'>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>伝票番号</label>
									<div class='controls'>
										<input type='text' name='denpyouNoFrom' class='input-small input-inline zeropadding' maxlength=8 value='${su:htmlEscape(denpyouNoFrom)}'>
										～
										<input type='text' name='denpyouNoTo' class='input-small input-inline zeropadding' maxlength=8 value='${su:htmlEscape(denpyouNoTo)}'>
									</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>起票部門</label>
								<div class='controls'>
									<input type='text' name="kihyouBumonCd" class='input-small' value='${su:htmlEscape(kihyouBumonCd)}'>
									<input type='text' name="kihyouBumonName" class='input-xlarge' value='${su:htmlEscape(kihyouBumonName)}' disabled>
									<button type='button' name='kihyouBumonSentakuButton' class='btn btn-small'>選択</button>
									<button type='button' name='kihyouBumonClearButton' class='btn btn-small'>クリア</button>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>起票者</label>
								<div class='controls'>
									<input type='text' name="shainNo" class='input-small' value='${su:htmlEscape(shainNo)}'>
									<input type='text' name="userName" class='input-xlarge' disabled value='${su:htmlEscape(userName)}'>
									<input type='hidden' name="userId" value='${su:htmlEscape(userId)}'>
									<button type='button' name='userSentakuButton' class='btn btn-small'>選択</button>
									<button type='button' name='userClearButton' class='btn btn-small'>クリア</button>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>取引先</label>
								<div class='controls'>
									<input type='text' name="torihikisakiCd" class='input-small' value='${su:htmlEscape(torihikisakiCd)}'>
									<input type='text' name="torihikisakiName" class='input-xlarge' value='${su:htmlEscape(torihikisakiName)}' disabled>
									<button type='button' name='torihikisakiSentakuButton' class='btn btn-small'>選択</button>
									<button type='button' name='torihikisakiClearButton' class='btn btn-small'>クリア</button>
								</div>
							</div>
						</div>
						<div class='blank'></div>
						<div>
							<button type="submit" class="btn" name='kensakuBtn'><i class="icon-search"></i> 検索実行</button>
							<button type='button' class='btn' name='joukenClearBtn'><i class='icon-remove'></i> 条件クリア</button>
						</div>
					</section>

					<!-- 検索結果 -->
					<section>
						<h2>検索結果</h2>
						<div class='no-more-tables'>
							<input type='hidden' name='beforeSelect' value='${su:htmlEscape(beforeSelect)}' />
							<table class='table-bordered table-condensed' >
								<thead>
									<tr>
										<th rowspan='2'><input type='checkbox' id='allcheck'></th>
										<th rowspan='2'><nobr>伝票ID</nobr></th>
										<th rowspan='2'><nobr>起票者</nobr></th>
										<th rowspan='2'><nobr>支払方法</nobr></th>
										<th rowspan='2'><nobr>支払種別</nobr></th>
										<th rowspan='2'><nobr>取引先</nobr></th>
										<th rowspan='2'><nobr>計上日</nobr></th>
										<th rowspan='2'><nobr>支払予定日</nobr></th>
										<th rowspan='2'><nobr>支払日</nobr></th>
										<th rowspan='2'><nobr>印刷状況</nobr></th>
									</tr>
								</thead>
								<tbody id='result'>
								<c:forEach var="record" items="${list}">
									<tr>
										<td align="center"><input type='checkbox' name='sentaku' value='${record.denpyou_id}' /></td>
										<td><nobr><a href='shiharai_irai?denpyouId=${record.denpyou_id}&denpyouKbn=${record.denpyou_kbn}' target='_blank'>${record.denpyou_id}</a></nobr></td>
										<td><nobr>${su:htmlEscape(record.user_full_name)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.shiharai_houhou_name)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.shiharai_shubetsu_name)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.torihikisaki_name)}</nobr></td>
										<td><nobr>${record.keijoubi}</nobr></td>
										<td><nobr>${record.yoteibi}</nobr></td>
										<td><nobr>${record.shiharaibi}</nobr></td>
										<td align="center"><nobr><c:if test='${record.shutsuryoku_flg eq "1"}'>印刷済</c:if><c:if test='${record.shutsuryoku_flg eq "0"}'>未印刷</c:if></nobr></td>
									</tr>
								</c:forEach>
								</tbody>
								
							</table>
						</div>
						<div>
							<button type='button' class='btn' id='pdfBtn'><i class='icon-print'></i> 帳票出力</button>
						</div>
					</section>
				<!-- Modal -->
				<div id='dialog'></div>
				</form></div><!-- main -->
			</div> <!-- /container -->
			<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<!-- スクリプト -->
		<script style='text/javascript'>

$(document).ready(function(){
	
	//条件クリア時
	var userName = $("input[name=name]").val();
	var userId = $("input[name=id]").val();
	
	/**
	 * 起票部門選択ボタン押下時Function
	 */
	 $("button[name=kihyouBumonSentakuButton]").click(function() {
		dialogRetBumonCd 	= $("input[name=kihyouBumonCd]");
		dialogRetBumonName  = $("input[name=kihyouBumonName]");
		commonBumonSentaku();
	});
	//起票部門コードロストフォーカス時、起票部門名称表示
	$("[name=kihyouBumonCd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetBumonCd 	= $("input[name=kihyouBumonCd]");
		dialogRetBumonName  = $("input[name=kihyouBumonName]");
		commonBumonCdLostFocus(true, dialogRetBumonCd, dialogRetBumonName, title);
	});
	/**
	 * 起票部門クリアボタン押下時Function
	 */
	 $("button[name=kihyouBumonClearButton]").click(function() {
		$("input[name=kihyouBumonCd]").val("");
		$("input[name=kihyouBumonName]").val("");
	});
	
	/**
	 * 起票者選択ボタン押下時Function
	 */
	 $("button[name=userSentakuButton]").click(function() {
		dialogRetuserId   = $("input[name=userId]");
		dialogRetusername = $("input[name=userName]");
		dialogRetshainNo  = $("input[name=shainNo]");
		commonUserSentaku();
	});
	/**
	 * 社員番号ロストフォーカス時
	 */
	 $("input[name=shainNo]").blur(function() {
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetuserId   = $("input[name=userId]");
		dialogRetusername = $("input[name=userName]");
		dialogRetshainNo  = $("input[name=shainNo]");
		commonShainNoLostFocus(dialogRetshainNo, dialogRetusername, title, dialogRetuserId);
	});
	/**
	 * 起票者クリアボタン押下時Function
	 */
	 $("button[name=userClearButton]").click(function() {
		$("input[name=userId]").val("");
		$("input[name=userName]").val("");
		$("input[name=shainNo]").val("");
	});
	
	/**
	 * 取引先選択ボタン押下時、ダイアログ表示
	 */
	 $("button[name=torihikisakiSentakuButton]").click(function() {
	 	dialogRetTorihikisakiCd = $("[name=torihikisakiCd]");
	 	dialogRetTorihikisakiName = $("[name=torihikisakiName]");
	 	commonTorihikisakiSentaku("A013");
	 });
		
	//取引先コードロストフォーカス時、取引先名称表示
	$("[name=torihikisakiCd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetTorihikisakiCd = $("[name=torihikisakiCd]");
		dialogRetTorihikisakiName = $("[name=torihikisakiName]");
		commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd, dialogRetTorihikisakiName, title, null, "A013");
	});
	/**
	 * 取引先クリアボタン押下時、取引先クリア
	 */
	$("button[name=torihikisakiClearButton]").click(function() {
		$("[name=torihikisakiCd]").val("");
		$("[name=torihikisakiName]").val("");
		$("[name=furikomisakiJouhou]").val("");
	});
	 
	/**
	 * 検索処理
	 */
	 $("button[name=kensakuBtn]").click(function() {
		$("#myForm").attr("action" , "shiharai_iraisho_shutsuryoku_kensaku");
		$("#myForm").attr("method" , "kensaku");
		$("#myForm").submit();
	});
	 
	//条件クリアボタン押下
	$("button[name=joukenClearBtn]").click(function(e){
		inputClear($(this).closest("section"));
		$("input[name=userName]").val(userName);
		$("input[name=userId]").val(userId);
		//一般ユーザーの場合、未印刷にセット
		if ($("input[name=userId]").val() != "") {
			$("select[name=shutsuryokuJoukyou]").find("option").eq(1).prop("selected", true);
		}
	});
	
	/*
	 * 帳票出力
	 */
	$("#pdfBtn").click(function(){
		$("#myForm").attr("action" , "shiharai_iraisho_shutsuryoku_pdfoutput");
		$("#myForm").attr("method" , "post");
		$("#myForm").submit();
	});
	
	
	//チェック
	$("#allcheck").click(function(){
		$("#result").find("input[type='checkbox']").prop('checked', $(this).prop("checked"));
	});

<c:if test='${allCheck}'>
	//デフォルト
	$("#result").find("input[type='checkbox']").prop('checked', true);
</c:if>
	
});
		</script>
	</body>
</html>
