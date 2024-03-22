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
		<title>支払依頼申請一括登録確認｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
	</head>

	<body>
    	<div id='wrap'>

    		<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
				<h1>支払依頼申請一括登録確認</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='tourokuForm' class='form-horizontal' method='post' enctype="multipart/form-data">
				
				<input type='hidden' name='nyuryokuHoushiki' value='${su:htmlEscape(nyuryokuHoushiki)}'>

					<!-- CSVファイル -->
					<section>
						<h2>CSVファイル</h2>
						<b>${su:htmlEscape(csvFileName)}</b>
					</section>

					<section>
						<h2>申請内容</h2>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th><nobr>#</nobr></th>
										<th><nobr>伝票<br>№</nobr></th>
<c:if test='${status eq "End"}'>
										<th><nobr>処理<br>結果</nobr></th>
</c:if>
<c:if test='${issias}'>
										<th>${su:htmlEscape(hfUfSeigyo.hf1Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.hf2Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.hf3Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.hf4Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.hf5Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.hf6Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.hf7Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.hf8Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.hf9Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.hf10Name)}</th>
</c:if>
										<th><nobr>取引先<br>コード</nobr></th>
										<th><nobr>一見先支払先名</nobr></th>
										<th><nobr>支払<br>種別</nobr></th>
										<th><nobr>計上日</nobr></th>
										<th><nobr>支払<br>予定日</nobr></th>
										<th><nobr>EDI</nobr></th>
										<th><nobr>振込先<br>銀行コード</nobr></th>
										<th><nobr>振込先<br>支店コード</nobr></th>
										<th><nobr>口座<br>種別</nobr></th>
										<th><nobr>口座<br>番号</nobr></th>
										<th><nobr>口座<br>名義人(ｶﾅ)</nobr></th>
										<th><nobr>手数料</nobr></th>
										<th><nobr>控除金額</nobr></th>
										<th><nobr>発生種別</nobr></th>
										
										<th><nobr>摘要</nobr></th>
										<th><nobr>支払金額</nobr></th>
										<th><nobr>消費税率</nobr></th>
										<th><nobr>軽減税率区分</nobr></th>
										<th><nobr>仕訳枝番号</nobr></th>
										<th><nobr>勘定科目<br>枝番コード</nobr></th>
										<th><nobr>負担部門<br>コード</nobr></th>
										<th>${su:htmlEscape(hfUfSeigyo.uf1Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.uf2Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.uf3Name)}</th>
<c:if test='${issias}'>
										<th>${su:htmlEscape(hfUfSeigyo.uf4Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.uf5Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.uf6Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.uf7Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.uf8Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.uf9Name)}</th>
										<th>${su:htmlEscape(hfUfSeigyo.uf10Name)}</th>
</c:if>
										<th>プロジェクトコード</th>
										<th>セグメントコード</th>
										<th>税抜金額</th>
										<th>消費税額</th>
										<th>事業者区分</th>
										<th>適格請求書発行事業者登録番号</th>
										<th>インボイス対応伝票</th>
<c:if test='${isshishutsuirai}'>
										<th>起案伝票ID</th>
</c:if>
									</tr>
								</thead>
								<tbody>
<c:forEach var="denpyou" items="${denpyouList}" varStatus="i">
	<c:forEach var="meisai" items="${denpyou.meisaiList}" varStatus="j">
									<tr>
										<td><nobr>${meisai.number}</nobr></td>
		<c:if test='${j.index eq 0}'>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.denpyouNo)}</nobr></td>
			<c:if test='${status eq "End"}'>
				<c:if test='${empty denpyou.denpyouId}'>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>登録失敗</nobr></td>
				</c:if>
				<c:if test='${not empty denpyou.denpyouId}'>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr><a href='shiharai_irai?denpyouKbn=A013&denpyouId=${denpyou.denpyouId}' target='_blank'>登録成功</a></nobr></td>
				</c:if>
			</c:if>
<c:if test='${issias}'>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.hf1Cd)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.hf2Cd)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.hf3Cd)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.hf4Cd)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.hf5Cd)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.hf6Cd)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.hf7Cd)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.hf8Cd)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.hf9Cd)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.hf10Cd)}</nobr></td>
</c:if>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.torihikisakiCd)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.ichigensakiTorihikisakiName)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.shihiaraiShubetsu)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.keijoubi)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.yoteibi)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.edi)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.furikomiGinkouCd)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.furikomiGinkouShitenCd)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.yokinShubetsu)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.kouzaBangou)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.kouzaMeiginin)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.tesuuryou)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.manekinGensen)}</nobr></td>
										<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.hasseiShubetsu)}</nobr></td>
		</c:if>
										<td><nobr>${su:htmlEscape(meisai.tekiyou)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.shiharaiKingaku)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.zeiritsu)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.keigenZeiritsuKbn)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.shiwakeEdaNo)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.kamokuEdabanCd)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.futanBumonCd)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.uf1Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.uf2Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.uf3Cd)}</nobr></td>
<c:if test='${issias}'>
										<td><nobr>${su:htmlEscape(meisai.uf4Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.uf5Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.uf6Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.uf7Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.uf8Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.uf9Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.uf10Cd)}</nobr></td>
</c:if>
										<td><nobr>${su:htmlEscape(meisai.projectCd)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.segmentCd)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.zeinukiKingaku)}</nobr></td>
										<td><nobr>${su:htmlEscape(meisai.shouhizeigaku)}</nobr></td>
										<c:if test='${j.index eq 0}'>
											<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.jigyoushaKbn)}</nobr></td>
											<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.jigyoushaNo)}</nobr></td>
											<td rowspan='${fn:length(denpyou.meisaiList)}'><nobr>${su:htmlEscape(denpyou.invoiceDenpyou)}</nobr></td>
										</c:if>
<c:if test='${isshishutsuirai}'>
										<td><nobr>${su:htmlEscape(denpyou.kianDenpyouId)}</nobr></td>
</c:if>
									</tr>
	</c:forEach>
</c:forEach>
								</tbody>
							</table>
						</div>
					</section>

					<section>
<c:if test='${status eq "Init"}'>
	<c:if test='${fn:length(errorList) eq 0}'>
								<button type='button' class='btn' name='okButton' onClick="buttonAction('touroku')"><i class='icon-hdd'></i> 登録</button>
								<button type='button' class='btn' onClick="location.href='shiharaiirai_csv_upload'" ><i class='icon-arrow-left'></i> 戻る</button>
	</c:if>
	<c:if test='${fn:length(errorList) ne 0}'>
								<button type='button' class='btn' onClick="location.href='shiharaiirai_csv_upload'" ><i class='icon-arrow-left'></i> 戻る</button>
	</c:if>
</c:if>
<c:if test='${status eq "Run"}'>
							<button type='button' class='btn' onClick="location.href='shiharaiirai_csv_upload_kakunin?status=Run'"><i class='icon-search'></i> 画面更新</button>
</c:if>
<c:if test='${status eq "End"}'>
							<button type='button' class='btn' onClick="location.href='shiharaiirai_csv_upload'" ><i class='icon-arrow-left'></i> 戻る</button>
</c:if>
					</section>
				</form></div><!-- main -->
				<!-- Modal -->
				<div id='dialog'></div>
			</div><!-- content -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<!-- スクリプト -->
		<script style='text/javascript'>
/**
 * イベントボタン押下時のアクションの切り替え
 */
function buttonAction(btnName) {
	// アップロード
	if(btnName == "touroku"){
		formObject = $("form#tourokuForm");
		formObject.attr("action","shiharaiirai_csv_upload_kakunin_touroku");
		formObject.submit();
	}
}
		</script>
	</body>
</html>
