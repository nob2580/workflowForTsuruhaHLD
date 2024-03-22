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
		<title>法人カード利用明細｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
  				<h1>法人カード利用明細</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main' class='form-horizontal'><form id='myForm'>
					<!-- 検索枠 -->
					<section>
						<h2>検索条件</h2>
						<div id="kensakuList" class='no-more-tables'>
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>利用日</label>
									<div class='controls'>
									<input type='text' name='riyouBiFrom' class='input-small datepicker input-inline' value='${su:htmlEscape(riyouBiFrom)}'>
									～
									<input type='text' name='riyouBiTo' class='input-small datepicker input-inline' value='${su:htmlEscape(riyouBiTo)}'>
								</div>
							</div>						
							<div class='control-group'>
								<label class='control-label'>起票日付</label>
									<div class='controls'>
										<input type='text' name='kihyouBiFrom' class='input-small datepicker input-inline' value='${su:htmlEscape(kihyouBiFrom)}'>
										～
										<input type='text' name='kihyouBiTo' class='input-small datepicker input-inline' value='${su:htmlEscape(kihyouBiTo)}'>
									</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>起票</label>
									<div class='controls'>
										<label class='label'>部門</label>
										<input type='text' name='kihyouShozokuBumonCd' class='input-small input-inline' value='${su:htmlEscape(kihyouShozokuBumonCd)}'>
										<input type='text' name='kihyouShozokuBumonName' class='input-medium input-inline' value='${su:htmlEscape(kihyouShozokuBumonName)}'>
										<nobr><button type='button' class='btn btn-mini input-inline' id='kihyouSyozokuSearch'>選択</button></nobr>
									</div>
									<div class='controls'>
										<label class='label'>社員番号</label>
										<input type='text' name='kihyouShainNoHyouji' class='input-medium input-inline' value='${su:htmlEscape(kihyouShainNo)}'>
										<input type='hidden' name='kihyouShainNo' value='${su:htmlEscape(kihyouShainNo)}'>
										<label class='label'>ｶｰﾄﾞ番号</label>
										<input type='text' name='kihyouCardNumHyouji' class='input-medium input-inline' value='${su:htmlEscape(kihyouCardNum)}'>
										<input type='hidden' name='kihyouCardNum' value='${su:htmlEscape(kihyouCardNum)}'>
										<label class='label'>名前</label>
										<input type='text' name='kihyouUserNameHyouji' class='input-medium input-inline' value='${su:htmlEscape(kihyouUserName)}'>
										<nobr><button type='button' class='btn btn-mini input-inline' id='kihyouNameSearch'>選択</button></nobr>
										<input type='hidden' name='kihyouUserName' value='${su:htmlEscape(kihyouUserName)}'>
									</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>状態</label>
								<div class='controls form-inline'>
									<label for='kihyouchuu'>${su:htmlEscape(kihyouchuuNm)}<input type="checkbox" id='kihyouchuu' name='kihyouchuu' value='1' <c:if test='${"1" eq kihyouchuu}'>checked</c:if>></label>
									　<label for='shinseichuu'>${su:htmlEscape(shinseichuuNm)}<input type="checkbox" id='shinseichuu' name='shinseichuu' value='1' <c:if test='${"1" eq shinseichuu}'>checked</c:if>></label>
									　<label for='syouninzumi'>${su:htmlEscape(syouninzumiNm)}<input type="checkbox" id='syouninzumi' name='syouninzumi' value='1' <c:if test='${"1" eq syouninzumi}'>checked</c:if>></label>
									　<label for='hininzumi'>${su:htmlEscape(hininzumiNm)}<input type="checkbox" id='hininzumi' name='hininzumi' value='1' <c:if test='${"1" eq hininzumi}'>checked</c:if>></label>
									　<label for='torisagezumi'>${su:htmlEscape(torisagezumiNm)}<input type="checkbox" id='torisagezumi' name='torisagezumi' value='1' <c:if test='${"1" eq torisagezumi}'>checked</c:if>></label>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>並べ替え</label>
								<div class='controls'>
									①
									<select name='sortColumn1' class='input-small'>
<c:forEach var="sortColumnList1" items="${sortColumnList1}">
									<option value='${su:htmlEscape(sortColumnList1.naibu_cd)}' <c:if test='${su:htmlEscape(sortColumnList1.naibu_cd) eq sortColumn1}'>selected</c:if>>${su:htmlEscape(sortColumnList1.name)}</option>
</c:forEach>
									</select>
									<select name='sort1' class='input-small'>
<c:forEach var="sortList1" items="${sortList1}">
									<option value='${su:htmlEscape(sortList1.naibu_cd)}' <c:if test='${su:htmlEscape(sortList1.naibu_cd) eq sort1}'>selected</c:if>>${su:htmlEscape(sortList1.name)}</option>
</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class='blank'></div>
						<div>
							<button type='button' class='btn' name='joukenClearBtn'><i class='icon-remove-circle'></i> 条件クリア</button>
						</div>
					</section>

					<!-- 検索結果 -->
					<section>
						<h2>検索結果</h2>
						<div>
							<button type='button' class='btn' id='csvBtn'><i class='icon-download'></i> CSV出力</button>
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

	//（起票者所属部門）部門検索ボタン押下時アクション
	$("#kihyouSyozokuSearch").click(function(){
		dialogRetBumonCd   =  $("input[name=kihyouShozokuBumonCd]");
		dialogRetBumonName =  $("input[name=kihyouShozokuBumonName]");
		commonBumonSentaku();
	});
	
	//（起票者所属部門）部門コードロストフォーカス時アクション
	$("input[name=kihyouShozokuBumonCd]").blur(function(){
		dialogRetBumonCd =  $("input[name=kihyouShozokuBumonCd]");
		dialogRetBumonName =  $("input[name=kihyouShozokuBumonName]");
		commonBumonCdLostFocus(true, dialogRetBumonCd, dialogRetBumonName, "起票部門コード");
	});
	
	//（社員番号）社員番号ロストフォーカス時Function
	$("input[name=kihyouShainNoHyouji]").blur(function(){
		dialogRetShainNo	= $("input[name=kihyouShainNoHyouji]");
		dialogRetCardNum =  $("input[name=kihyouCardNumHyouji]");
		dialogRetShainName	= $("input[name=kihyouUserNameHyouji]");
		commonShainNoLostFocus(dialogRetShainNo, dialogRetShainName, "社員番号");
	});
	
	//（起票者）ユーザー検索ボタン押下時アクション
	$("#kihyouNameSearch").click(function(){
		dialogRetshainNo =  $("input[name=kihyouShainNoHyouji]");
		dialogRetCardNum =  $("input[name=kihyouCardNumHyouji]");
		dialogRetusername =  $("input[name=kihyouUserNameHyouji]");
		commonUserSentaku();
	});
	
	//条件クリアボタン押下
	$("button[name=joukenClearBtn]").click(function(e){
		var dfSortClm2 = $("input[name=defaultSortColumn2]").val();
<c:if test="${IsKanri}" >
		inputClear($(this).closest("section"));
</c:if>
<c:if test="${not IsKanri}" >
		var shainNo = $("input[name=kihyouShainNo]").val();
		var cardNum = $("input[name=kihyouCardNum]").val();
		var shainNm = $("input[name=kihyouUserName]").val();
		inputClear($(this).closest("section"));
		$("input[name=kihyouShainNoHyouji]").val(shainNo);
		$("input[name=kihyouCardNumHyouji]").val(cardNum);
		$("input[name=kihyouUserNameHyouji]").val(shainNm);
</c:if>
		$("select[name=sortColumn2]").val(dfSortClm2);
	});

	//CSV出力
	$("#csvBtn").click(function(){
		$("input[name=kihyouShainNo]").val($("input[name=kihyouShainNoHyouji]").val());
		$("input[name=kihyouCardNum]").val($("input[name=kihyouCardNumHyouji]").val());
		$("input[name=kihyouUserName]").val($("input[name=kihyouUserNameHyouji]").val());
		$("#myForm").attr("action" , "houjin_card_riyou_meisai_csvoutput");
		$("#myForm").attr("method" , "post");
		$("#myForm").submit();
	});
	
	/*
	 * 帳票出力
	 */
	$("#pdfBtn").click(function(){
		$("input[name=kihyouShainNo]").val($("input[name=kihyouShainNoHyouji]").val());
		$("input[name=kihyouCardNum]").val($("input[name=kihyouCardNumHyouji]").val());
		$("input[name=kihyouUserName]").val($("input[name=kihyouUserNameHyouji]").val());
		$("#myForm").attr("action" , "houjin_card_riyou_meisai_pdfoutput");
		$("#myForm").attr("method" , "post");
		$("#myForm").submit();
	});
	
});
		</script>
	</body>
</html>
