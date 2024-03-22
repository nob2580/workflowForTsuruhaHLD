<%@page import="eteam.common.JspUtil"%>
<%@ page import="eteam.symbol.EteamSymbol"%>
<%@page import="java.util.List"%>
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
		<title>${title}｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
<!--
td.shouninran {
	width: 100px;
	height: 100px;
	vertical-align: top;
	text-align: center;
}
td.shounincomment {
	width: 30%;
}
td.ninicoment {
	width: 50%;
}
label.kianbangou {
	cursor: pointer;
	color: #005580;
}
span.kianbangou {
	position:absolute;
	right:240px;
	text-shadow:none;
	font-size:14px;
}

span.kianbangou_print {
	position:absolute;
	right:10px;
	text-shadow:none;
	font-size:14px;
}

span.ebunshoMeisaiRecord {
	white-space:nowrap;
}

@media screen and (min-width: 768px) {
	.ebleft{
		float:left;
		padding:3px 0px 3px 0px;
	}
	.ebright{
		float:right;
		width:100%;
		padding:0px 0px 0px 0px;
	}
}
@media screen and (max-width: 767px) {
	.ebleft{
		padding:25px 0px 5px 0px;
	}
	.ebright{
		padding:0px 0px 0px 0px;
	}
	.mainCell
	{
		width: 100%; /* スマホ版の幅が異様に狭くなってしまうので対策 */
	}
}

/* js無しで動く設定と、動かない設定が混在する模様。staticなcssが優先されているっぽく、importantを付けても改善されず */
<c:if test='${isPreviewTargetDenpyou}'>
@media screen and (min-width: 980px)
{
	.filePreviewCell
	{
		display: list-item;
	}
	#container
	{
		margin-right: unset;
		margin-left: unset;
	}
}
@media screen and (max-width: 979px)
{
	.filePreviewCell
	{
		display: none;
	}
	.mainCell
	{
		width: 100%;
	}
	#container
	{
		margin-right: auto;
		margin-left: auto;
	}
}
</c:if>
-->
		</style>
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
	</head>
	<body>
		<div id='wrap'>

			<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>


			<!-- 中身 -->
			<div id='content' class='container'>
				<ul id='contentTable' style="display:flex; list-style-type:none;">
				<li class = "mainCell">

				<!-- タイトル -->
				<h1><c:if test='${maruhiFlg == 1}'><span class='maruhiFlg'><b><font size ='6' color='red'>㊙</font></b></span></c:if>
					<span id='title'>${su:htmlEscape(title)}</span>
					<span id='kianBangouColumn' class='kianbangou non-print' style='display:none;'>
<c:if test="${isDispKianbangou != '0' && kianBangou == ''}">
						起案番号 ${su:htmlEscape(kianBangou)}
</c:if>
<c:if test="${isDispKianbangou != '0' && kianBangou != ''}">
						<a href="#" class='kianbangou' onClick="showKianbangouSyousaiKakunin();">起案番号</a> ${su:htmlEscape(kianBangou)}
</c:if>
					</span>
					<!-- ワークフロー共通（印刷用） -->
					<span class='kianbangou_print print_only'>
<c:if test="${isDispKianbangou != '0' && kianBangou != ''}">
						${su:htmlEscape(kianBangou)}
</c:if>
					</span>
<c:if test="${denpyouJoutai != '00'}">
					<span id='headerButtonArea' class='non-print'<c:if test='${not wfSeigyo.webInsatsu}'>style='display:none'</c:if>>
						<button type='button' class='btn btn-small pc_only' style='position: absolute; right: 120px;' id='PDFBtn'><i class='icon-print'></i> 帳票出力</button>
						<button type='button' class='btn btn-small pc_only' style='position: absolute; right: 10px;' onclick="var settings = changeWidthBeforePrint(); window.print(); changeWidthAfterPrint(settings);" id='printBtn'><i class='icon-print'></i> WEB印刷</button>
					</span>
</c:if>
				</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				<!-- メイン -->
				<div id='main'>
				<form id='shouninRouteTourokuForm' method='get' action = 'shounin_route_touroku'>
					<input type="hidden" name='denpyouId'  value='${su:htmlEscape(denpyouId)}'>
					<input type='hidden' name='shinseiKingaku' value='${su:htmlEscape(kingaku)}'>
					<input type='hidden' name='kijunbi' value='${su:htmlEscape(kijunbi)}'>
				</form>
				<form id='shanshouKihyouForm' method='post' action = 'denpyou_sanshou_kihyou' target="_blank">
					<input type="hidden" name='urlPath'    value='${su:htmlEscape(urlPath)}'>
					<input type="hidden" name='denpyouId'  value='${su:htmlEscape(denpyouId)}'>
					<input type="hidden" name='denpyouKbn' value='${su:htmlEscape(denpyouKbn)}'>
					<input type="hidden" name='version' value='${su:htmlEscape(version)}'>
				</form>
				<form id='pdfOutputForm' method='post' target="_self">
					<input type="hidden" name='denpyouId'  value='${su:htmlEscape(denpyouId)}'>
					<input type="hidden" name='denpyouKbn' value='${su:htmlEscape(denpyouKbn)}'>
					<input type="hidden" name='qrText'>
					<input type="hidden" name='sokyuuFlg'>
				</form>
				<form id='PDFKanren' method='post' target="_self">
					<input type="hidden" name='denpyouKbn'>
					<input type="hidden" name='denpyouId'>
					<input type='hidden' name="qrText">
				</form>
				<!-- 起案金額チェックダイアログのCSV出力用 -->
				<form id='kianKingakuCheckForm' method='post' action = 'kian_check_csvoutput' target="_self">
					<input type="hidden" name='denpyouId'  value='${su:htmlEscape(denpyouId)}'>
					<input type="hidden" name='denpyouKbn' value='${su:htmlEscape(denpyouKbn)}'>
					<input type="hidden" name='mode'>
					<input type="hidden" name='sentakuBumonCd'>
					<input type="hidden" name='sentakuNendo'>
					<input type="hidden" name='sentakuRyakugou'>
					<input type="hidden" name='sentakuKianbangouFrom'>
				</form>
				<!-- 予算チェックダイアログのCSV出力用 -->
				<form id='yosanCheckForm' method='post' action = 'yosan_check_csvoutput' target="_self">
					<input type="hidden" name='denpyouId'  value='${su:htmlEscape(denpyouId)}'>
					<input type="hidden" name='denpyouKbn' value='${su:htmlEscape(denpyouKbn)}'>
					<input type="hidden" name='mode'>
					<input type="hidden" name='sentakuBumonCd'>
					<input type="hidden" name='sentakuNendo'>
					<input type="hidden" name='sentakuRyakugou'>
					<input type="hidden" name='sentakuKianbangouFrom'>
				</form>
				<form id='workflowForm' name='workflowForm' method='post' target='_self' enctype="multipart/form-data" class='form-horizontal'>
					<input type="hidden" name='denpyouId'  value='${su:htmlEscape(denpyouId)}'>
					<input type="hidden" name='denpyouKbn' value='${su:htmlEscape(denpyouKbn)}'>
					<input type="hidden" name='serialNo' value='${su:htmlEscape(serialNo)}'>
					<input type="hidden" name='urlPath'    value='${su:htmlEscape(urlPath)}'>
					<input type="hidden" name='sanshouDenpyouId' value='${su:htmlEscape(sanshouDenpyouId)}'>
					<input type="hidden" name='downloadFileNo' value='1'>
					<input type="hidden" name='downloadEbunshoNo' value='1'>
					<input type="hidden" name='printFlg' value='${su:htmlEscape(printFlg)}'>
					<input type="hidden" name='ebunshoShiyouFlg' value='${su:htmlEscape(ebunshoShiyouFlg)}'>
					<input type="hidden" name='ebunshoTaishouCheckDefault' value='${su:htmlEscape(ebunshoTaishouCheckDefault)}'>
					<input type="hidden" name='ebunshoDenshitsCheckDefault' value='${su:htmlEscape(ebunshoDenshitsCheckDefault)}'>
					<input type='hidden' name='ebunshoEnableFlg' value ='${su:htmlEscape(ebunshoEnableFlg)}'>
					<input type="hidden" name='addTimestampEbunshoNo' value =''>
					<input type="hidden" name='zeigakuShuseiFlg' value ='${su:htmlEscape(zeigakuShuseiFlg)}'>
					<input type="hidden" name='nyuryokuDefaultflg' value ='${su:htmlEscape(nyuryokuDefaultFlg)}'>
					<input type="hidden" name='nyuryokuHenkouflg' value ='${su:htmlEscape(nyuryokuHenkouFlg)}'>
					<input type="hidden" name='jigyoushaKbnHenkouflg' value ='${su:htmlEscape(jigyoushaKbnHenkouFlg)}'>
					<input type="hidden" name='invoiceStartflg' value ='${su:htmlEscape(invoiceStartFlg)}'>
					<input type="hidden" name='invoiceSetteiflg' value ='${su:htmlEscape(invoiceSetteiFlg)}'>
					<input type="hidden" name='hasuuShoriFlg' value='${su:htmlEscape(hasuuShoriFlg)}'>
					<input type="hidden" name='shouhizeikbn' value='${su:htmlEscape(shouhizeikbn)}'>
					<input type="hidden" name='shiireZeiAnbun' value='${su:htmlEscape(shiireZeiAnbun)}'>
					<input type="hidden" name='shiirezeigakuKeikasothi' value='${su:htmlEscape(shiirezeigakuKeikasothi)}'>
					<input type="hidden" name='uriagezeigakuKeisan' value='${su:htmlEscape(uriagezeigakuKeisan)}'>
					<input type="hidden" name='tekiyouMaxLength' value='${su:htmlEscape(tekiyouMaxLength)}'>


					<c:forEach var="i" begin="0" end="7" step="1">
						<!-- 年月日任意フラグ -->
						<input type="hidden" name='nengappi_nini_flg' value='${su:htmlEscape(nengappi_nini_flg[i])}'>
						<!-- 金額任意フラグ -->
						<input type="hidden" name='kingaku_nini_flg' value='${su:htmlEscape(kingaku_nini_flg[i])}'>
						<!-- 発行者任意フラグ -->
						<input type="hidden" name='hakkousha_nini_flg' value='${su:htmlEscape(hakkousha_nini_flg[i])}'>
					</c:forEach>

					<input type="hidden" name='kihyouBumonCd' value='${su:htmlEscape(kihyouBumonCd)}'>
					<input type="hidden" name='kihyouBumonName'   value='${su:htmlEscape(kihyouBumonName)}'>
					<input type="hidden" name='bumonRoleId'   value='${su:htmlEscape(bumonRoleId)}'>
					<input type="hidden" name='bumonRoleName' value='${su:htmlEscape(bumonRoleName)}'>
					<input type="hidden" name='daihyouFutanBumonCd' value='${su:htmlEscape(daihyouFutanBumonCd)}'>

					<input type="hidden" name='dairiFlg' value='${su:htmlEscape(dairiFlg)}'>

					<!-- 起案番号簿選択ダイアログ表示要否 -->
					<input type="hidden" name='isDispKianbangouBoDialog' value='${su:htmlEscape(isDispKianbangouBoDialog)}'>
					<!-- 起案番号簿選択ダイアログ選択値（部門コード） -->
					<input type="hidden" name='kianbangouBoDialogBumonCd' value=''>
					<!-- 起案番号簿選択ダイアログ選択値（年度） -->
					<input type="hidden" name='kianbangouBoDialogNendo' value=''>
					<!-- 起案番号簿選択ダイアログ選択値（略号） -->
					<input type="hidden" name='kianbangouBoDialogRyakugou' value=''>
					<!-- 起案番号簿選択ダイアログ選択値（開始起案番号） -->
					<input type="hidden" name='kianbangouBoDialogKianbangouFrom' value=''>
					<!-- 画面上部の起案番号データ表示要否 -->
					<input type="hidden" name='isDispKianbangou' value='${su:htmlEscape(isDispKianbangou)}'>
					<!-- 画面上部の起案番号データ表示値 -->
					<input type="hidden" name='kianBangou' value='${su:htmlEscape(kianBangou)}'>
					<!-- 起案添付セクション表示要否 -->
					<input type="hidden" name='isDispKiantenpuSection' value='${su:htmlEscape(isDispKiantenpuSection)}'>
					<!-- 起案伝票紐付けダイアログ表示要否 -->
					<input type="hidden" name='kianHimodukeDialogFlg' value='${su:htmlEscape(kianHimodukeDialogFlg)}'>
					<!-- 起案伝票紐付け確認要否 -->
					<input type="hidden" name='kianHimodukeFlg' value='${su:htmlEscape(kianHimodukeFlg)}'>
					<!-- 起案終了ボタン表示要否 -->
					<input type="hidden" name='isDispKianShuuryou' value='${su:htmlEscape(isDispKianShuuryou)}'>
					<!-- 起案終了ボタン表示切り替え値 -->
					<input type="hidden" name='kianShuuryouFlg' value='${su:htmlEscape(kianShuuryouFlg)}'>
					<!-- 起案終了ボタン表示切り替え値 -->
					<input type="hidden" name='kianYosanCheckFlg' value=''>

					<!-- 予算チェックセクション表示要否 -->
					<input type="hidden" name='isDispYosanCheckSection' value='${su:htmlEscape(isDispYosanCheckSection)}'>
					<!-- 予算チェック対象フラグ -->
					<input type="hidden" name='yosanCheckTaishougaiFlg' value='${su:htmlEscape(yosanCheckTaishougaiFlg)}'>
					<!-- 予算チェック対象フラグ（起案番号選択時に取り直した値 -->
					<input type="hidden" name='kianbangouBoDialogYosanCheckTaishougaiFlg' value=''>

					<!-- 伝票共通 -->
					<div>
						<input type='hidden' name='ringiKingakuChoukaCommentHyoujiFlg' value='${su:htmlEscape(ringiKingakuChoukaCommentHyoujiFlg)}'>
<c:if test="${denpyouJoutai eq '00'}">
						<input type='hidden' name='kanrenDenpyouRingiHikitsugiUmFlg' value='${su:htmlEscape(kanrenDenpyouRingiHikitsugiUmFlg)}'>
						<input type='hidden' name='karibaraiRingiHikitsugiUmFlg' value='${su:htmlEscape(karibaraiRingiHikitsugiUmFlg)}'>
</c:if>
<c:if test="${fn:length(ringiKingaku) > 0}">
						<input type='hidden' name='ringiChoukaHantei' value='${su:htmlEscape(ringiChoukaHantei)}'>
						<input type='hidden' name='hiddenRingiKingaku' value='${su:htmlEscape(ringiKingaku)}'>
						<input type='hidden' name='hiddenRingiKingakuZandaka' value='${su:htmlEscape(ringiKingakuZandaka)}'>
</c:if>
						<div id='denpyouJouhou'>
								<label class='label' for='denpyouId'>伝票ID</label>
							<span id='denpyouId'>${su:htmlEscape(denpyouId)}</span>
							<label class='label' for='serialNo'>伝票番号</label>
							<span id='serialNo'>${su:htmlEscape(serialNo)}</span>
<c:if test="${fn:length(ringiKingaku) > 0}">
							<span id='ringiKingakuColumn'>
								<label class='label'>${su:htmlEscape(ringiKingakuName)}</label>
								<span id='ringiKingaku'>${su:htmlEscape(ringiKingaku)} 円</span>
								<label class='label'>${su:htmlEscape(ringiKingakuName)}残高</label>
								<span id='ringiKingakuZandaka'>${su:htmlEscape(ringiKingakuZandaka)} 円</span>
								<span id='ringiKingakuZandakaWarning' class="label label-warning" style='display:none'>!</span>
								<span id='ringiKingakuZandakaImportant' class="label label-important" style='display:none'>!!</span>
							</span>
</c:if>
							<ul class='nav pull-right'>
								<select name="invoiceDenpyou" id="invoiceDenpyou" class='input-large'>
								 <c:forEach var="invoiceDenpyouRecord" items="${invoiceDenpyouList}">
									<option value='${invoiceDenpyouRecord.naibuCd}'	<c:if test='${invoiceDenpyouRecord.naibuCd eq invoiceDenpyou}'>selected</c:if>>${su:htmlEscape(invoiceDenpyouRecord.name)}</option>
								</c:forEach>
								</select>
							</ul>
						</div>
						<div>
							<label class='label' for='kihyouBumonSelect'>起票部門</label>
<c:choose>
	<c:when test="${fn:length(kihyouBumonList) > 1}">
							<select id='kihyouBumonSelect' class='input-xlarge'>
		<c:forEach var="record" items="${kihyouBumonList}">
								<option value='${su:htmlEscape(record.bumon_cd)}'
										data-bName='${su:htmlEscape(record.bumon_full_name)}'
										data-brId='${su:htmlEscape(record.bumon_role_id)}'
										data-brName='${su:htmlEscape(record.bumon_role_name)}'
										data-daiBuCd='${su:htmlEscape(record.daihyou_futan_bumon_cd)}'
										<c:if test='${record.bumon_cd eq kihyouBumonCd}'>selected</c:if>>
									${su:htmlEscape(record.bumon_full_name)}
								</option>
		</c:forEach>
							</select>
	</c:when>
	<c:otherwise>
							<span id='kihyouBumonSpan'>${su:htmlEscape(kihyouBumonName)}</span>
	</c:otherwise>
</c:choose>
							<label class='label' for='kihyouUser'>起票者</label>
							<span id='kihyouUser'>${su:htmlEscape(kihyouUser)}</span>
							<label class='label' for='kihyouBi'>起票日</label>
							<span id='kihyouBi'>${su:htmlEscape(kihyouBi)}</span>
							<label class='label' for='denpyouJoutai'>状態</label>
							<span id='denpyouJoutai'>${su:htmlEscape(denpyouJoutaiMei)}</span>
						</div>
						<div>
							<label class='label' for='shouninSha'>承認者</label>
							<span id='shouninSha'>${su:htmlEscape(shouninSha)}</span>
<c:if test='${not empty chuuki1}'>
							<br><font size="4" color="#ff0000">${su:htmlEscapeBr(chuuki1)}</font>
</c:if>
<c:if test='${not empty chuukiKousai1}'>
							<br><font size="4" color="#0000ff">${su:htmlEscapeBr(chuukiKousai1)}</font>
</c:if>
						</div>
					</div>
					<div id='ringiKingakuCommentSection' class='control-group' style='display:none'>
							<label class='label'>${su:htmlEscape(ringiKingakuName)}<c:if test="${fn:length(ringiKingakuName) eq 0}">稟議金額</c:if>超過コメント<c:if test="${ringiKingakuChoukaCommentHissuFlg eq '1'}"><span class='required'>*</span></c:if></label>
							<textarea class='input-block-level' name='ringiKingakuChoukaComment' placeholder='${su:htmlEscape(ringiKingakuName)}<c:if test="${fn:length(ringiKingakuName) eq 0}">稟議金額</c:if>の超過理由を入力してください。(200字)'>${su:htmlEscape(hyoujiRingiKingakuChoukaComment)}</textarea>
					</div>


					<!-- ワークフロー共通（印刷用） -->
					<div class='print_only'>
						<h2>承認欄</h2>
						<div>
							<div style='float: left;'>
								<div class='no-more-tables'>
									<table class='table-bordered'>
										<tbody>
											<tr>
												<td class="shouninran">経理</td>
												<td class="shouninran"></td>
												<td class="shouninran"></td>
												<td class="shouninran"></td>
												<td class="shouninran">起票者</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<div style='float:right;'>
								<img id='qr' alt='' src='' style='height: 90px;'>
							</div>
							<div style='clear: both;'></div>
						</div>
					</div>

					<!-- 伝票共通（起案添付） -->
					<div id='kiantenpuSection' <c:if test="${isDispKiantenpuSection == '0'}">style='display:none'</c:if>>
						<h2>起案伝票</h2>
						<c:if test="${enableInput}">
						<button type='button' id='kianAddButton' class='btn btn-small'>起案追加</button>
						</c:if>
						<div class='control-group hissu-group'>
							<div id='kianDenpyouTableDiv' class='no-more-tables' style='display:none'>
								<table class='table-bordered table-condensed' style='margin-bottom:5px; border-collapse: collapse; word-break: break-all; word-wrap: break-word;'>
									<thead>
										<tr id='denpyouTableHeader'>
											<th>伝票種別</th>
											<th>起案番号</th>
											<th>件名</th>
											<th colspan="2">伝票ID</th>
											<th style="display:none;">帳票出力</th>
											<th>添付ファイル</th>
											<c:if test="${enableInput}">
											<th>添付解除</th>
											</c:if>
										</tr>
									</thead>
									<tbody id='kianDenpyouList'>
									<c:forEach var="i" begin="0" end="${kianCount - 1}" step="1">
											<tr class="kianDenpyou">
												<td style="white-space:nowrap;"></td>
												<td style="white-space:nowrap;"></td>
												<td style="white-space:nowrap;"></td>
												<td style="white-space:nowrap;"></td>
												<td style="white-space:nowrap;"></td>
												<td style="vertical-align:top;"></td>
											<c:if test="${enableInput}">
												<td style="white-space:nowrap;"></td>
											</c:if>
												<input type="hidden" name='hyoujiKianEmbedSpace' value='${su:htmlEscape(hyoujiKianEmbedSpace[i])}'>
												<input type="hidden" name='hyoujiKianDenpyouKbn' value='${su:htmlEscape(hyoujiKianDenpyouKbn[i])}'>
												<input type="hidden" name='hyoujiKianbangou' value='${su:htmlEscape(hyoujiKianbangou[i])}'>
												<input type="hidden" name='hyoujiKianKenmei' value='${su:htmlEscape(hyoujiKianKenmei[i])}'>
												<input type='hidden' name="hyoujiKianDenpyouShubetsuUrl"  value='${su:htmlEscape(hyoujiKianDenpyouShubetsuUrl[i])}'>
												<input type='hidden' name="hyoujiKianDenpyouUrl"  value='${su:htmlEscape(hyoujiKianDenpyouUrl[i])}'>
												<input type="hidden" name='hyoujiKianDenpyouShubetsu' value='${su:htmlEscape(hyoujiKianDenpyouShubetsu[i])}'>
												<input type="hidden" name='hyoujiKianDenpyouId' value='${su:htmlEscape(hyoujiKianDenpyouId[i])}'>
												<input type="hidden" name='hyoujiKianTenpuFileName' value='${hyoujiKianTenpuFileName[i]}'>
												<input type="hidden" name='hyoujiKianTenpuFileUrl' value='${hyoujiKianTenpuFileUrl[i]}'>

												<input type="hidden" name='kianDenpyouId' value='${su:htmlEscape(kianDenpyouId[i])}'>
												<input type="hidden" name='kianDenpyouKbn' value='${su:htmlEscape(kianDenpyouKbn[i])}'>
											</tr>
									</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>

					<!-- 伝票共通（関連伝票） -->
<c:if test="${kanrenFlg}">
					<div>
						<h2>添付伝票</h2>
						<c:if test="${enableInput}">
						<button type='button' id='denpyouAddButton' class='btn btn-small'>伝票追加</button>
						</c:if>
						<div class='control-group hissu-group'>
							<div id='kanrenDenpyouTableDiv' class='no-more-tables' style='display:none'>
								<table class='table-bordered table-condensed' style='margin-bottom:5px; border-collapse: collapse; word-break: break-all; word-wrap: break-word;'>
									<thead>
										<tr id='denpyouTableHeader'>
											<th>伝票種別</th>
											<th colspan="2">伝票ID</th>
											<th style="display:none;">帳票出力</th>
											<th>添付ファイル</th>
											<c:if test="${enableInput}">
											<th>添付解除</th>
											</c:if>
											<th>稟議金額引継ぎ</th>
										</tr>
									</thead>
									<tbody id='kanrenDenpyouList'>
									<c:forEach var="i" begin="0" end="${kanrenCount - 1}" step="1">
											<tr class="kanrenDenpyou">
												<td style="white-space:nowrap;"></td>
												<td style="white-space:nowrap;"></td>
												<td style="white-space:nowrap;"></td>
												<td style="vertical-align:top;"></td>
												<c:if test="${enableInput}">
												<td style="white-space:nowrap;"></td>
												</c:if>
												<td style="text-align:center"></td>
												<input type="hidden" name='hyoujiKanrenEmbedSpace' value='${su:htmlEscape(hyoujiKanrenEmbedSpace[i])}'>
												<input type="hidden" name='hyoujiKanrenDenpyouKbn' value='${su:htmlEscape(hyoujiKanrenDenpyouKbn[i])}'>
												<input type='hidden' name="hyoujiKanrenDenpyouShubetsuUrl"  value='${su:htmlEscape(hyoujiKanrenDenpyouShubetsuUrl[i])}'>
												<input type='hidden' name="hyoujiKanrenDenpyouUrl"  value='${su:htmlEscape(hyoujiKanrenDenpyouUrl[i])}'>
												<input type="hidden" name='hyoujiKanrenDenpyouShubetsu' value='${su:htmlEscape(hyoujiKanrenDenpyouShubetsu[i])}'>
												<input type="hidden" name='hyoujiKanrenDenpyouId' value='${su:htmlEscape(hyoujiKanrenDenpyouId[i])}'>
												<input type="hidden" name='hyoujiKanrenTenpuFileName' value='${hyoujiKanrenTenpuFileName[i]}'>
												<input type="hidden" name='hyoujiKanrenTenpuFileUrl' value='${hyoujiKanrenTenpuFileUrl[i]}'>
												<input type="hidden" name='hyoujiKanrenKihyouBi' value='${su:htmlEscape(hyoujiKanrenKihyouBi[i])}'>
												<input type="hidden" name='hyoujiKanrenShouninBi' value='${su:htmlEscape(hyoujiKanrenShouninBi[i])}'>

												<input type="hidden" name='kanrenDenpyouId' value='${su:htmlEscape(kanrenDenpyouId[i])}'>
												<input type="hidden" name='kanrenDenpyouKbn' value='${su:htmlEscape(kanrenDenpyouKbn[i])}'>
												<input type="hidden" name='kanrenTourokuTime' value='${su:htmlEscape(kanrenTourokuTime[i])}'>
												<input type="hidden" name='kanrenShouninTime' value='${su:htmlEscape(kanrenShouninTime[i])}'>

												<input type="hidden" name='ringiKingakuHikitsugiFlg' value='${su:htmlEscape(ringiKingakuHikitsugiFlg[i])}'>
											</tr>
									</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
</c:if>
<c:if test="${isDispYosanCheckSection eq '1'}">
					<!-- 伝票共通（予算チェック） -->
					<div id='yosanCheckSection'>
						<h2>予算チェック</h2>
						<div class='control-group hissu-group'>
							<label class='control-label'><span class='required'>*</span>対象月</label>
							<div class='controls'>
								<select id='yosanCheckNengetsu' name='yosanCheckNengetsu' class='input-medium input-inline' <c:if test="${denpyouJoutai != '00' and denpyouJoutai != '10'}">disabled</c:if>>
								<option value='' <c:if test='${record.key eq yosanCheckNengetsu}'>selected</c:if>></option>
								<c:forEach var="item" items="${monthList}">
									<option value="${item.key}" <c:if test="${item.key eq yosanCheckNengetsu}">selected</c:if>>${su:htmlEscape(item.val)}</option>
								</c:forEach>
								</select>
							</div>
						</div>
					</div>
</c:if>
					<!-- 各伝票の画面 -->
<jsp:include page='<%= JspUtil.makeJspPath("eteam/gyoumu/workflow/DenpyouCommon_gamen.jsp") %>' />
					<!-- ワークフロー -->
					<!-- 添付ファイル -->
<c:if test="${wfSeigyo.fileTenpuRef}">
					<div class='non-print'>
						<h2>添付ファイル</h2>
	<c:if test="${denpyouJoutai != '00'}">
						<div id='tenpuzumiList'>
							<!-- 添付済みファイルにe文書情報がある場合 -->
			<c:forEach var="record" items="${tempFileEbunshoList}">
							<div class='tenpuzumifileDiv'>
								<div style="clear:both"></div>
								<div class='tenpuzumiMeisai ebleft'>
								<!-- 添付ファイルのとき　または　タイムスタンプ再付与ボタンが表示されてるとき -->
				<c:if test="${ (record.ebunsho_no == null || record.ebunsho_no == '') || ((record.touroku_time == null || record.touroku_time == '') && (record.ebunsho_no != null && record.ebunsho_no != '')) }">
					<c:if test="${wfSeigyo.fileTenpuUpd}">
									<button type='button' class='btn btn-mini' onClick="fileDeleteEventBtn(${record.edano}, $(this).parent())">削除</button>
					</c:if>
				</c:if>
									<a href="denpyou_file_download?denpyouId=${su:htmlEscape(denpyouId)}&denpyouKbn=${su:htmlEscape(denpyouKbn)}&downloadFileNo=${record.edano}">${su:htmlEscape(record.file_name)}</a>
				<c:if test="${(ebunshoShiyouFlg eq '1' || ebunshoShiyouFlg eq '2') && (record.ebunsho_no != null && record.ebunsho_no != '' )}">
					<c:if test="${ record.touroku_time != null && record.touroku_time != '' }">
						<c:if test="${ record.denshitorihiki_flg == '1' && record.tsfuyo_flg == '0' }">
									<span style='margin-right:5px;'><font color="#0088cc">タイムスタンプ付与対象外</font></span>
						</c:if>
						<c:if test="${ record.denshitorihiki_flg != '1' || record.tsfuyo_flg != '0' }">
									<span style='margin-right:5px;'><font color="#0088cc">タイムスタンプ付与済</font></span>
						</c:if>
					</c:if>
					<c:if test="${ (record.touroku_time == null || record.touroku_time == '') && (record.ebunsho_no != null && record.ebunsho_no != '')}">
									<button name='addTimestamp' class='btn' type='button' style='margin-right:5px;' >タイムスタンプ再付与</button>
					</c:if>
						<c:if test="${ record.ebunsho_no != null && record.ebunsho_no != '' }">
									<br>
									<a href="ebunsho_file_download?denpyouId=${su:htmlEscape(denpyouId)}&denpyouKbn=${su:htmlEscape(denpyouKbn)}&downloadEbunshoNo=${su:htmlEscape(record.ebunsho_no)}" class="eblink">(${su:htmlEscape(record.ebunsho_no)}.${record.denshitorihiki_flg == '1' && record.tsfuyo_flg == '0' ? record.extension : 'pdf'})</a>
						</c:if>
				</c:if>
				<c:if test="${(ebunshoShiyouFlg eq '1' || ebunshoShiyouFlg eq '2')}">
									<input type='checkbox' name='tenpuzumi_denshitorihiki_chk' style='margin-right:10px;margin-bottom:5px' <c:if test="${ record.denshitorihiki_flg == '1' }"> checked </c:if> <c:if test="${ record.ebunsho_no != null && record.ebunsho_no != '' }"> disabled </c:if>>電子取引（
									<input type='checkbox' name='tenpuzumi_tsfuyo_chk' style='margin-bottom:5px' <c:if test="${ record.tsfuyo_flg == '1' }"> checked </c:if> <c:if test="${ (record.touroku_time != null && record.touroku_time != '') || (record.tsfuyo_flg != '1' && record.ebunsho_no != null && record.ebunsho_no != '') }"> disabled</c:if>>TS付与）
									<input type='checkbox' name='tenpuzumi_ebunshoCheckbox' style='margin-right:10px;margin-bottom:5px' <c:if test="${ record.ebunsho_no != null && record.ebunsho_no != '' }"> checked disabled </c:if>>e文書対象
				</c:if>
									<input type='hidden' name='tenpuzumi_edano' value='${su:htmlEscape(record.edano)}'>
									<input type='hidden' name='tenpuzumi_filename' value='${su:htmlEscape(record.file_name)}'>
									<input type='hidden' name='tenpuzumi_denshitorihikiFlg' value='${su:htmlEscape(record.denshitorihiki_flg)}'>
									<input type='hidden' name='tenpuzumi_tsfuyoFlg' value='${su:htmlEscape(record.tsfuyo_flg)}'>
									<input type='hidden' name='tenpuzumi_ebunshodata_count' value=''>
									<input type='hidden' name='tenpuzumi_ebunsho_no' value='${su:htmlEscape(record.ebunsho_no)}'>
									<input type='hidden' name='tenpuzumi_ebunsho_tourokutime' value ='${su:htmlEscape(record.touroku_time)}'>
									<input type='hidden' name='tenpuzumi_ebunshoflg' value=''>
								</div>
								<div class='tenpuzumiEbunshoMeisai ebright'>
				<c:choose>
					<c:when test="${! (record.ebunshoDatalist == null) }">
										<c:forEach var="record2" items="${record.ebunshoDatalist}">
									<span class='ebunshoMeisaiRecord'>
										<select id='tenpuzumi_ebunsho_shubetsu' name='tenpuzumi_ebunsho_shubetsu' class='input-small'>
											<c:forEach var='record' items='${ebushoShubetsuList}'>
												<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq record2.ebunsho_shubetsu}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
											</c:forEach>
										</select>
										<button type='button' name='tenpuzumi_ebunshoMeisaiAdd' class='btn btn-mini'>+</button>
										<button type='button' name='tenpuzumi_ebunshoMeisaiDelete' id='chiharaibiCheckNotPass' class='btn btn-mini'>-</button>
										<input type='hidden' name='tenpuzumi_ebunsho_edano' value='${su:htmlEscape(record2.ebunsho_edano)}'>
										<c:set var='ebunsho_shubetsu_int' value='${Integer.parseInt((record2.ebunsho_shubetsu == null || record2.ebunsho_shubetsu.equals("")) ? "0" : record2.ebunsho_shubetsu)}' />
										<label class='label ebNengappi'><c:if test= '${nengappi_nini_flg[ebunsho_shubetsu_int] == 0}'><span class='required'>*</span></c:if>年月日</label>
										<input type='text' name='tenpuzumi_ebunsho_nengappi' class='input-small datepicker' value='${su:htmlEscape(record2.ebunsho_nengappi)}'>
										<label class='label ebKingaku'><c:if test='${kingaku_nini_flg[ebunsho_shubetsu_int] == 0}'><span class='required'>*</span></c:if>金額</label>
										<input type='text' name='tenpuzumi_ebunsho_kingaku' class='input-medium autoNumeric' value ='${su:htmlEscape(record2.ebunsho_kingaku)}'>円
										<span class='hinmei-group' <c:if test='${record2.ebunsho_shubetsu ne "3" || ebunshoEnableFlg ne "1"}'>style='display:none;'</c:if>>
											<label class='label'><span class='required'>*</span>品名</label>
											<input type='text' name='tenpuzumi_ebunsho_hinmei' class='input-large' maxlength='50' value ='${su:htmlEscape(record2.ebunsho_hinmei)}'>
										</span>
										<label class='label ebHakkousha'><c:if test= '${hakkousha_nini_flg[ebunsho_shubetsu_int] == 0}'><span class='required'>*</span></c:if>発行者名称</label>
										<input type='text' name='tenpuzumi_ebunsho_hakkousha' class='input-xlarge300' maxlength='20' value ='${su:htmlEscape(record2.ebunsho_hakkousha)}'>
										<span style="clear:right"></span>
										<br>
									</span>
					</c:forEach>
					</c:when>
					<c:otherwise>
								<c:if test="${(ebunshoShiyouFlg eq '1' || ebunshoShiyouFlg eq '2')}">
									<span class='ebunshoMeisaiRecord'>
										<select id='tenpuzumi_ebunsho_shubetsu' name='tenpuzumi_ebunsho_shubetsu' class='input-small'>
											<c:forEach var='record' items='${ebushoShubetsuList}'>
												<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq record2.ebunsho_shubetsu}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
											</c:forEach>
										</select>
										<button type='button' name='tenpuzumi_ebunshoMeisaiAdd' class='btn btn-mini'>+</button>
										<button type='button' name='tenpuzumi_ebunshoMeisaiDelete' class='btn btn-mini'>-</button>
										<input type='hidden' name='tenpuzumi_ebunsho_edano' value='${su:htmlEscape(record2.ebunsho_edano)}'>
										<c:set var='ebunsho_shubetsu_int' value='${Integer.parseInt((record2.ebunsho_shubetsu == null || record2.ebunsho_shubetsu.equals("")) ? "0" : record2.ebunsho_shubetsu)}' />
										<label class='label ebNengappi'><c:if test= '${nengappi_nini_flg[ebunsho_shubetsu_int] == 0}'><span class='required'>*</span></c:if>年月日</label>
										<input type='text' name='tenpuzumi_ebunsho_nengappi' class='input-small datepicker' value='${su:htmlEscape(record2.ebunsho_nengappi)}'>
										<label class='label ebKingaku'><c:if test='${kingaku_nini_flg[ebunsho_shubetsu_int] == 0}'><span class='required'>*</span></c:if>金額</label>
										<input type='text' name='tenpuzumi_ebunsho_kingaku' class='input-medium autoNumeric' value ='${su:htmlEscape(record2.ebunsho_kingaku)}'>円
										<span class='hinmei-group' <c:if test='${record2.ebunsho_shubetsu ne "3" || ebunshoEnableFlg ne "1"}'>style='display:none;'</c:if>>
											<label class='label'><span class='required'>*</span>品名</label>
											<input type='text' name='tenpuzumi_ebunsho_hinmei' class='input-large' maxlength='50' value ='${su:htmlEscape(record2.ebunsho_hinmei)}'>
										</span>
										<label class='label ebHakkousha'><c:if test= '${hakkousha_nini_flg[ebunsho_shubetsu_int] == 0}'><span class='required'>*</span></c:if>発行者名称</label>
										<input type='text' name='tenpuzumi_ebunsho_hakkousha' class='input-xlarge300' maxlength='20' value ='${su:htmlEscape(record2.ebunsho_hakkousha)}'>
										<span style="clear:right"></span>
										<br>
									</span>
								</c:if>
					</c:otherwise>
				</c:choose>
								</div>
								<div style="clear:both"></div>
								<hr style='padding:0; margin:1px;'>
							</div>
			</c:forEach>
						</div>
	</c:if>
<% /* 伝票状態が未起票もしくは起票中、且つ、ログオンユーザー区分が起票者の場合 */ %>
	<c:if test="${wfSeigyo.fileTenpuUpd}">
						<div id='tenpuList' style="clear:right">
<c:if test="${fn:length(ebunshoflg) gt 0}"><c:set var="tenpucnt" value="${fn:length(ebunshoflg) - 1}" /></c:if>
<c:if test="${fn:length(ebunshoflg) le 0}"><c:set var="tenpucnt" value="0" /></c:if>
<c:set var="msno" value="0" />
		<c:forEach var="i" begin="0" end="${tenpucnt}" step="1">
							<div class='newfileDiv'>
								<div style="clear:both"></div>
								<div class='tenpudata ebleft'>
									<input type='file' size='15' name='uploadFile'/>
									<button type='button' name='tenpuFileAdd' class='btn btn-mini'>+</button>
									<button type='button' name='tenpuFileDelete' class='btn btn-mini'>-</button>
			<c:if test="${ebunshoShiyouFlg eq '1' or ebunshoShiyouFlg eq '2'}">
									<!-- e文書使用フラグがオンの場合 -->
									<input type='checkbox' name='denshitorihikiCheckbox' style='margin-bottom:5px' <c:if test="${ denshitorihikiFlg ne null && denshitorihikiFlg[i] eq '1'}">checked</c:if>>電子取引（
									<input type='checkbox' name='tsfuyoCheckbox' style='margin-bottom:5px' <c:if test="${ tsfuyoFlg ne null && tsfuyoFlg[i] eq '1'}">checked</c:if>>TS付与）
									<input type='checkbox' name='ebunshoCheckbox' style='margin-bottom:5px' <c:if test="${ ebunshoflg ne null && ebunshoflg[i] eq '1'}">checked</c:if> <c:if test="${ denshitorihikiFlg ne null && denshitorihikiFlg[i] eq '1'}">disabled</c:if>>e文書対象
									<input type='hidden' name='denshitorihikiFlg' value=''>
									<input type='hidden' name='tsfuyoFlg' value=''>
									<input type='hidden' name='ebunshoflg' value=''>
									<input type='hidden' name='ebunsho_tenpufilename_header' value =''>
									<input type='hidden' name='ebunsho_meisaicnt' value =''>
			</c:if>
								</div>
			<c:if test="${ebunshoShiyouFlg eq '1' or ebunshoShiyouFlg eq '2'}">
				<c:if test="${fn:length(ebunsho_meisaicnt) gt 0}"><c:set var="mscnt" value="${ebunsho_meisaicnt[i] - 1}" /></c:if>
				<c:if test="${fn:length(ebunsho_meisaicnt) le 0}"><c:set var="mscnt" value="0" /></c:if>
				<c:if test="${mscnt le 0}"><c:set var="mscnt" value="0" /></c:if>
								<div class='ebunshoMeisai ebright'>
				<c:forEach var="gyo" begin="0" end="${mscnt}" step="1">
									<span class='ebunshoMeisaiRecord'>
										<select id='ebunsho_shubetsu' name='ebunsho_shubetsu' class='input-small'>
											<c:forEach var='record' items='${ebushoShubetsuList}'>
												<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq ebunsho_shubetsu[msno]}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
											</c:forEach>
										</select>
										<button type='button' name='ebunshoMeisaiAdd' class='btn btn-mini'>+</button>
										<button type='button' name='ebunshoMeisaiDelete' class='btn btn-mini'>-</button>
										<c:set var='ebunsho_shubetsu_int' value='${Integer.parseInt((ebunsho_shubetsu[msno] == null || ebunsho_shubetsu[msno].equals("")) ? "0" : ebunsho_shubetsu[msno])}' />
										<label class='label ebNengappi'><c:if test= '${nengappi_nini_flg[ebunsho_shubetsu_int] == 0}'><span class='required'>*</span></c:if>年月日</label>
										<input type='text' name='ebunsho_nengappi' class='input-small datepicker' value='${ebunsho_nengappi[msno]}'>
<!-- 2022/06/24 Ver22.05.31.01 条件から「納品書」を削除 -->										
<!-- 									<label class='label ebKingaku'><c:if test='${ebunsho_shubetsu[msno] ne "3" && kingaku_nini_flg[ebunsho_shubetsu_int] == 0}'><span class='required'>*</span></c:if>金額</label>  -->
										<label class='label ebKingaku'><c:if test='${kingaku_nini_flg[ebunsho_shubetsu_int] == 0}'><span class='required'>*</span></c:if>金額</label>

										<input type='text' name='ebunsho_kingaku' class='input-medium autoNumeric' value ='${ebunsho_kingaku[msno]}'>円
										<span class='hinmei-group' <c:if test='${ebunsho_shubetsu[msno] ne "3" || ebunshoEnableFlg ne "1"}'>style='display:none;'</c:if>>
											<label class='label'><span class='required'>*</span>品名</label>
											<input type='text' name='ebunsho_hinmei' class='input-large ' maxlength='50' value ='${ebunsho_hinmei[msno]}'>
										</span>
										<label class='label ebHakkousha'><c:if test= '${hakkousha_nini_flg[ebunsho_shubetsu_int] == 0}'><span class='required'>*</span></c:if>発行者名称</label>
										<input type='text' name='ebunsho_hakkousha' class='input-xlarge300' maxlength='20' value ='${ebunsho_hakkousha[msno]}'>
										<input type='hidden' name='ebunsho_tenpufilename' value =''>
										<span style="clear:right"></span>
										<br>
									</span>
					<c:set var="msno" value="${msno + 1}" />
				</c:forEach>
								</div>
			</c:if>
								<div style="clear:both"></div>
								<hr style='padding:0; margin:1px;'>
							</div>
		</c:forEach>
						</div>
	</c:if>
					</div>
</c:if>
					<div style="clear:both"></div>

					<!-- 承認状況 -->
					<div class='non-print'>
						<h2>承認状況</h2>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>日時</th>
										<th style="width:300px">所属部門</th>
										<th>ユーザー</th>
										<th>役割</th>
										<th>処理権限</th>
										<th>状況</th>
										<th class='comment-view'>コメント</th>
									</tr>
								</thead>
								<tbody id='shouninCommentBody'>
<c:forEach var="record" items="${shouninJoukyouList}" varStatus="varStatus">
	<c:if test='${!empty(record.gougiOya) || (varStatus.index != 0 && !empty(shouninJoukyouList[varStatus.index-1].gougiOya))}'>
									<tr>
										<td colspan="7"></td>
									</tr>
	</c:if>
	<c:if test='${empty(record.gougiOya)}'>
									<tr <c:if test="${record.genzai_flg == '1'}">class="wait-period-bgcolor"</c:if>>
										<td>${su:htmlEscape(record.koushin_time)}</td>
										<td>${su:htmlEscape(record.bumon_full_name)}</td>
										<td>${su:htmlEscape(record.user_full_name)}</td>
	<c:choose>
		<c:when test="${empty record.gyoumu_role_name}">
										<td>${su:htmlEscape(record.bumon_role_name)}</td>
										<td>${su:htmlEscape(record.shounin_shori_kengen_name)}</td>
		</c:when>
		<c:otherwise>
										<td>${su:htmlEscape(record.gyoumu_role_name)}</td>
										<td>${su:htmlEscape(record.shounin_shori_kengen_name)}</td>
		</c:otherwise>
	</c:choose>
										<td>${su:htmlEscape(record.joukyou)}</td>
										<td class="shounincomment" style="word-wrap:break-word; word-break:break-all;">${su:htmlEscapeBrLink(record.comment)}</td>
									</tr>
	</c:if>
	<c:if test='${!empty(record.gougiOya)}'>
		<c:forEach var="gougiOya" items="${record.gougiOya}">
									<tr>
										<td style='background:pink'>合議</td>
										<td colspan="6">${su:htmlEscape(gougiOya.gougi_name)}</td>
									</tr>
			<c:forEach var="gougiKo" items="${gougiOya.gougiKo}">
									<tr <c:if test="${gougiKo.gougi_genzai_flg == '1'}">class="wait-period-bgcolor"</c:if>>
										<td>${su:htmlEscape(gougiKo.koushin_time)}</td>
										<td>${su:htmlEscape(gougiKo.bumon_full_name)}</td>
										<td>${su:htmlEscape(gougiKo.user_full_name)}</td>
										<td>${su:htmlEscape(gougiKo.bumon_role_name)}</td>
										<td>${su:htmlEscape(gougiKo.shounin_shori_kengen_name)}</td>
										<td>${su:htmlEscape(gougiKo.joukyou)}</td>
										<td class="shounincomment" style="word-wrap:break-word; word-break:break-all;">${su:htmlEscapeBrLink(gougiKo.comment)}</td>
									</tr>
			</c:forEach>
		</c:forEach>
	</c:if>
</c:forEach>
								</tbody>
							</table>
						</div>
					</div>

					<!-- 操作履歴 -->
					<button id='sousaRirekiButton' type='button' class='btn btn-small pc_only'>操作履歴欄表示</button>
					<div id='sousaRireki' class='non-print'>
						<h2>操作履歴</h2>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>日時</th>
										<th style="width:300px">所属部門</th>
										<th>ユーザー</th>
										<th>役割</th>
										<th>処理権限</th>
										<th>状況</th>
										<th class='comment-view'>コメント</th>
									</tr>
								</thead>
								<tbody id='shouninCommentBody'>
<c:forEach var="record" items="${sousaList}" varStatus="varStatus">
									<tr <c:if test="${record.genzai_flg == '1'}">class="wait-period-bgcolor"</c:if>>
										<td>${su:htmlEscape(record.koushin_time)}</td>
										<td>${su:htmlEscape(record.bumon_full_name)}</td>
										<td>${su:htmlEscape(record.user_full_name)}</td>
	<c:choose>
		<c:when test="${empty record.gyoumu_role_name}">
										<td>${su:htmlEscape(record.bumon_role_name)}</td>
										<td>${su:htmlEscape(record.shounin_shori_kengen_name)}</td>
		</c:when>
		<c:otherwise>
										<td>${su:htmlEscape(record.gyoumu_role_name)}</td>
										<td>${su:htmlEscape(record.shounin_shori_kengen_name)}</td>
		</c:otherwise>
	</c:choose>
										<td>${su:htmlEscape(record.joukyou)}</td>
										<td class="shounincomment" style="word-wrap:break-word; word-break:break-all;">${su:htmlEscapeBrLink(record.comment)}</td>
									</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
					</div>

					<!-- 操作ボタン -->
					<div>
						<div class='blank non-print'>
<c:if test="${wfSeigyo.touroku}"><button type='button' class='btn' onClick="eventBtn('entry')"><i class='icon-hdd'></i> 登録</button></c:if>
<c:if test="${wfSeigyo.koushin}"><button type='button' id='koushinBtn' class='btn' onClick="eventBtn('update')"><i class='icon-hdd'></i> 更新</button></c:if>
<c:if test="${wfSeigyo.shouninRouteTouroku}"><button type='button' class='btn' onClick="shouninRouteEventBtn()"><i class='icon-signal'></i> 承認ルート登録</button></c:if>
<c:if test="${wfSeigyo.maruhiSettei}"><button type='button' class='btn' name='maruhiSetteiBtn' href='#maruhiSetteiModal' role='button' data-toggle='modal'><i class='icon-exclamation-sign'></i> マル秘設定</button></c:if>
<c:if test="${wfSeigyo.maruhiKaijyo}"><button type='button' class='btn' name='maruhiKaijyoBtn' href='#maruhiKaijyoModal' role='button' data-toggle='modal'><i class='icon-exclamation-sign'></i> マル秘解除</button></c:if>
<c:if test="${wfSeigyo.shinsei}"><button type='button' class='btn' name='workflowBtn' data-code='shinsei' data-name='申請' href='#workflowModal' role='button' data-toggle='modal' id='shinsei'><i class='icon-hand-up'></i> 申請</button></c:if>
<c:if test="${wfSeigyo.torisage}"><button type='button' class='btn' name='workflowBtn' data-code='torisage' data-name='取下げ' href='#workflowModal' role='button' data-toggle='modal'><i class='icon-hand-down'></i> 取下げ</button></c:if>
<c:if test="${wfSeigyo.sashimodoshi}"><button type='button' class='btn' name='workflowBtn' data-code='sashimodoshi' data-name='差戻し' href='#sashimodoshiModal' role='button' data-toggle='modal'><i class='icon-thumbs-down'></i> 差戻し</button></c:if>
<c:if test="${wfSeigyo.shounin}"><button type='button' class='btn' name='workflowBtn' data-code='shounin' data-name='${su:htmlEscape(wfSeigyo.shouninMongon)}' href='#workflowModal' role='button' data-toggle='modal'><i class='icon-ok'></i> ${su:htmlEscape(wfSeigyo.shouninMongon)}</button></c:if>
<c:if test="${wfSeigyo.hinin}"><button type='button' class='btn' name='workflowBtn' data-code='hinin' data-name='否認' href='#workflowModal' role='button' data-toggle='modal'><i class='icon-remove'></i> 否認</button></c:if>
<c:if test="${wfSeigyo.torimodoshi}"><button type='button' class='btn' name='workflowBtn' data-code='torimodoshi' data-name='取戻し' href='#workflowModal' role='button' data-toggle='modal'><i class='icon-thumbs-down'></i> 取戻し</button></c:if>
<c:if test="${wfSeigyo.sanshouKihyou}"><button type='button' class='btn' onClick="sanshouKihyouEventBtn()"><i class='icon-retweet'></i> 参照起票</button></c:if>
<c:if test="${wfSeigyo.kianCheck}"><button type='button' class='btn' onClick="commonKianKingakuCheck(0)" id='kianKingakuCheck'><i class='icon-pencil'></i> 起案確認</button></c:if>
<c:if test="${wfSeigyo.yosanCheck}"><button type='button' class='btn' onClick="commonYosanCheck(0)" id='yosanCheck'><i class='icon-pencil'></i> 予算確認</button></c:if>
<c:if test="${isDispKianShuuryou == '1' && kianShuuryouFlg == '0'}"><button type='button' class='btn' onClick="kianShuuryouEventBtn('1')"><i class='icon-retweet'></i> 起案終了</button></c:if>
<c:if test="${isDispKianShuuryou == '1' && kianShuuryouFlg == '1'}"><button type='button' class='btn' onClick="kianShuuryouEventBtn('0')"><i class='icon-retweet'></i> 起案終了解除</button></c:if>
<jsp:include page='<%= JspUtil.makeJspPath("eteam/gyoumu/workflow/DenpyouCommon_sousaButton.jsp") %>' />
<c:if test="${wfSeigyo.shiwakeDataHenkou != null}"><button type='button' class='btn' onclick='shiwakeDataHenkouEventBtn("${wfSeigyo.shiwakeDataHenkou}")'><i class='icon-refresh'></i> 仕訳データ変更</button></c:if>
<button type='button' id='closeButton' class='btn' onclick="winClose();" title='画面が閉じられない場合は、ブラウザの[×]ボタン等で閉じてください。'><i class='icon-folder-close'></i> 閉じる</button>
<!-- 202207 #115594　承認解除ボタン -->
<c:if test="${wfSeigyo.shouninKaijo}"><button type='button' class='btn' name='workflowBtn' data-code='shouninkaijo' data-name='承認解除' href='#workflowModal' role='button' data-toggle='modal'><i class='icon-retweet'></i> 承認解除</button></c:if>
						</div>

<c:if test="${denpyouJoutai != '00'}">
						<!-- Modal ワークフロー -->
						<div id='workflowModal' class='modal hide fade' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'></div>

						<!-- 差戻し START -->
						<div id='sashimodoshiModal' class='modal hide fade' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>
							<div class='modal-header'>
								<button type='button' name='dialogclosebtn' class='close' data-dismiss='modal' aria-hidden='true'>×</button>
								<h3 id='myModalLabel'>差戻し</h3>
							</div>
							<div class='modal-body'>
								<h2>差戻し先一覧</h2>
								<input type='hidden' name='sashimodoshiSakiGougiEdano' value=''>
								<input type='hidden' name='sashimodoshiSakiGougiEdaedano' value=''>
								<div class='no-more-tables'><table class='table-bordered'>
									<thead>
										<tr>
											<th></th>
											<th style='width: 100px;'>ユーザー</th>
											<th>所属部門</th>
											<th style='width: 100px;'>役割</th>
										</tr>
									</thead>
									<tbody>
	<c:forEach var="record" items="${sashimodoshiSakiList}">
										<tr>
											<td><input type='radio' id='sashimodoshiSakiEdano${record.edano}' name='sashimodoshiSakiEdano' value='${record.edano}' <c:if test='${record.edano == 1}'>checked</c:if> data-gougiEdano='${record.gougi_edano}' data-gougiEdaedano='${record.gougi_edaedano}'></td>
		<c:if test='${empty record.gyoumu_role_id}'>
											<td><label for='sashimodoshiSakiEdano${record.edano}'>${su:htmlEscapeBr(record.user_full_name)}</label></td>
											<td>${su:htmlEscapeBr(record.bumon_full_name)}</td>
											<td>${su:htmlEscape(record.bumon_role_name)}</td>
		</c:if>
		<c:if test='${not empty record.gyoumu_role_id}'>
											<td>-</td>
											<td>-</td>
											<td>${su:htmlEscape(record.gyoumu_role_name)}</td>
		</c:if>
										</tr>
	</c:forEach>
									</tbody>
								</table></div>
								<br>
								<div>
									<p>コメント（任意）</p>
									<textarea class='input-block-level' name='sashimodoshiComment' placeholder='任意でコメントを入力してください。(400字)'></textarea>
								</div>
							</div>
							<div class='modal-footer'>
								<button class='btn' name='dialogclosebtn' data-dismiss='modal' aria-hidden='true'>閉じる</button>
								<button type='button' class='btn btn-danger' id='chiharaibiCheckNotPass' name='fordenpyouhenkoukengen' onClick='workflowEventBtn("sashimodoshi")'>差戻し</button>
							</div>
						</div>
						<!-- 差戻し END -->

						<!-- マル秘文書フラグ設定 START -->
						<div id='maruhiSetteiModal' class='modal hide fade' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>
							<div class='modal-header'>
								<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>×</button>
								<h3 id='myModalLabel'>マル秘文書フラグ設定</h3>
							</div>
							<div class='modal-footer'>
								<button class='btn' data-dismiss='modal' aria-hidden='true'>閉じる</button>
								<button type='button' class='btn btn-primary' onClick='maruhiSetteiEventBtn()'>設定</button>
							</div>
						</div>
						<div id='maruhiKaijyoModal' class='modal hide fade' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>
							<div class='modal-header'>
								<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>×</button>
								<h3 id='myModalLabel'>マル秘文書フラグ解除</h3>
							</div>
							<div class='modal-footer'>
								<button class='btn' data-dismiss='modal' aria-hidden='true'>閉じる</button>
								<button type='button' class='btn btn-primary' onClick='maruhiKaijyoEventBtn()'>解除</button>
							</div>
						</div>
						<!-- マル秘文書フラグ設定 END -->
</c:if>
					</div>

<c:if test="${denpyouJoutai != '00' && !empty denpyouId}">
					<!-- 任意メモ -->
					<div <c:if test='${0 == fn:length(niniMemoList)}'>class='non-print'</c:if>>
						<h2>任意メモ</h2>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>日時</th>
										<th>ユーザー</th>
										<th class='comment-view'>コメント</th>
									</tr>
								</thead>
								<tbody id='niniComentBody'>
	<c:forEach var="record" items="${niniMemoList}">
									<tr>
										<td>${su:htmlEscape(record.koushin_time)}</td>
										<td>${su:htmlEscape(record.user_full_name)}</td>
										<td class="ninicoment" style="word-wrap:break-word; word-break:break-all;">${su:htmlEscapeBrLink(record.comment)}</td>
									</tr>
	</c:forEach>
								</tbody>
							</table>
						</div>
						<div class='blank non-print'>
							<button type='button' name='memobutton' class='btn' href='#memo' role='button' data-toggle='modal'><i class='icon-pencil'></i> メモ</button>
						</div>

						<!-- Modal 任意メモ -->
						<div id='memo' class='modal hide fade' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>
							<div class='modal-header'>
								<button type='button' name='memobutton' class='close' data-dismiss='modal' aria-hidden='true'>×</button>
								<h3 id='myModalLabel'>任意メモ</h3>
							</div>
							<div class='modal-body'>
								<p>メモ</p>
								<textarea class='input-block-level' name='niniMemo' placeholder='メモを入力してください。(400字)'>${su:htmlEscape(niniMemo)}</textarea>
							</div>
							<div class='modal-footer'>
								<button name='memobutton' class='btn' data-dismiss='modal' aria-hidden='true'>閉じる</button>
								<button type='button' name='memobutton' class='btn btn-primary' onClick="workflowEventBtn('niniMemo')">登録</button>
							</div>
						</div>
					</div>
</c:if>

					<!-- Modal -->
					<div id='dialog' style="position:relative"></div>
					<div id='dialogMeisai' style="position:relative"></div>
				</form>
				</div><!-- メイン -->
				</li>
				<!--  こっちにも分離先にも必要らしい -->
				<c:set var="userAgent" value="${header['User-Agent']}" scope="session"/>
				<c:set var="length" value="${fn:length(tenpuFileName)}" />
				<c:set var="isPreviewTargetDenpyou" value="${isPreviewTargetDenpyou}" />
				<jsp:include page='<%= JspUtil.makeJspPath("eteam/gyoumu/workflow/DenpyouCommon_previewFile.jsp") %>' />
			</ul>
		</div>
		<!-- content -->
		<div id="push"></div>
	</div>
	<!-- wrap -->

			<!-- フッター -->
			<%@ include file="/jsp/eteam/include/Footer.jsp" %>

<!-- スクリプト -->
<script style='text/javascript'>
// 印刷幅ずれ対策スクリプト
// プリント前に印刷用幅に本体を設定して
function changeWidthBeforePrint(){
	if(navigator.userAgent.toLowerCase().includes('firefox'))
	{
		return null; // firefoxでは特殊処理は不要
	}
	
    var minWidth = window.getComputedStyle(document.getElementById("kaikeiContent")).getPropertyValue('min-width');
    var marginLeft = window.getComputedStyle(document.getElementById("content")).getPropertyValue('margin-left');
    $("#kaikeiContent").css("min-width", "");
    $("#content").css('margin-left', '9px');
    return minWidth + "," + marginLeft;
}

// 終わったら戻す
function changeWidthAfterPrint(settings){
	if(settings == null)
	{
		return; // firefoxでは前の特殊処理がないので後処理も不要
	}
	var settingsArray = settings.split(',');
    $("#kaikeiContent").css("min-width", settingsArray[0]);
    $("#content").css('margin-left', settingsArray[1]);
}

<c:if test='${isPreviewTargetDenpyou && length > 0}'>

const UARegExp = new RegExp("(iPhone|Android|Windows Phone|BlackBerry|Tablet|Mobile)"); // モバイルUAの特徴文字列リスト

function changeDisplay()
{
	$("#contentTable").css("margin-left", "0px");
	$("#contentTable").css("margin-right", "0px");
	if(window.innerWidth < 980 || UARegExp.test(navigator.userAgent))
	{
		$(".filePreviewCell").css('display', 'none');
		$("#content").css('margin-right', 'auto');
		$("#content").css('margin-left', 'auto');
		$("#kaikeiContent").css("min-width", ""); // プレビューファイル非表示の時は不要
	}
	else
	{
		$("#kaikeiContent").css("min-width", $("#content").width()); // 会計の中身について、contentの本来のWidthが最小幅になるように強制
		var baseSize = $("#kaikeiContent").width() + 656;
		$(".filePreviewCell").css('display', '');
		$("#content").css('margin-right', 'unset');
		$("#content").css('margin-left', window.innerWidth < baseSize + 1 ? '9px' : (Math.round((window.innerWidth - baseSize)/2)+9) + 'px'); // スクロール不要な幅の時、プレビュー込みで全体が中央ぞろえになる幅を自動計算
	}
}
</c:if>
<c:if test='${(not isPreviewTargetDenpyou) || length <= 0}'>
function changeDisplay()
{
	$("#contentTable").css("margin-left", "0px");
	if(window.innerWidth > 1200)
	{
		$("#kaikeiContent").css("min-width", $("#content").width()); // 会計の中身について、contentの本来のWidthが最小幅になるように強制
	}
	else
	{
		$("#kaikeiContent").css("min-width", ""); // 1200px未満の時は不要
	}
}
</c:if>

// ロード時・リサイズ時にプレビュー表示有無を変更
window.onload = changeDisplay; // ちょっとラグいが、document.readyに置き換えると動かないので今のところ仕方なし
window.onresize = changeDisplay;

/** 添付ファイル「＋」ボタン押下時Function */
function tenpuFileAdd() {

	//添付ファイル合計が10以上なら中止
	if(10 <= $("#tenpuList div.newfileDiv").length + $("#tenpuzumiList a[href ^='denpyou_file_download']").length) return;

	//１行目をコピー
	var div = $("<div class='newfileDiv'>" + $("#tenpuList div:first").html() + "<\/div>");
	div.children().children().remove("span:gt(0)");
	div.children().children().remove("br");
	$("#tenpuList").append(div);

	//中身を消してアクション紐付け
	inputClear(div);
	div.find("input[name=ebunshoCheckbox]").prop("disabled",false);
	var flg = ($("input[name=ebunshoTaishouCheckDefault]").val() == "1");
	div.find("div.tenpudata").find("input[name=ebunshoCheckbox]").prop("checked",flg);
	div.find("input[name=ebunsho_nengappi]").prop("disabled", false);//入力できないとcommonInitでDatepickerが設定されないので
	commonInit(div);
	changeEbunshoMeisaiMain(div, div.find("select[name=ebunsho_shubetsu]").val());
	changeEbunshoEnable(div,flg);
	ebunshoMeisaiInit(div);
	//e文書処理用内部情報更新
	resetEbunshoNaibuData();
}

/** 添付ファイル「－」ボタン押下時Function */
function tenpuFileDelete() {

	//1行しかないなら中止
	if($("div.newfileDiv").length <= 1) return;

	$(this).closest("div").parent("div.newfileDiv").remove();
	//e文書処理用内部情報更新
	resetEbunshoNaibuData();
}

/** 添付済みe文書明細部「＋」ボタン押下時Function */
function tenpuzumiEbunshoMeisaiAdd() {

	//各添付ファイル明細が99件以上なら中止
	if(99 <= $(this).parent("span").parent("div").find("span.ebunshoMeisaiRecord").length ) return;

	//指定行をコピー
	var span = $("<span class='ebunshoMeisaiRecord'>" + $(this).closest("span.ebunshoMeisaiRecord").html() + "<\/span>");
	span.find("button[name=tenpuzumi_ebunshoMeisaiDelete]").prop("disabled", false);
	span.find("select[name=tenpuzumi_ebunsho_shubetsu]").css("visibility", "hidden");
	$(this).closest("span.ebunshoMeisaiRecord").after(span);

	//中身を消してアクション紐付け
	inputClear(span);
	commonInit(span);
	tenpuzumiebunshoMeisaiInit(span);

	//e文書処理用内部情報更新
	resetEbunshoNaibuData();

	setTenpuzumiEbunshoShubetsu(span);
}

/** 添付済みe文書明細部「－」ボタン押下時Function */
function tenpuzumiEbunshoMeisaiDelete() {

	//添付済みe文書情報が1つしかないなら中止
	if($(this).closest("span.ebunshoMeisaiRecord").parent("div").find("span.ebunshoMeisaiRecord").length <= 1) return;

	var div = $(this).closest("div.tenpuzumiEbunshoMeisai");
	$(this).closest("span.ebunshoMeisaiRecord").remove();
	div.find("select[name=tenpuzumi_ebunsho_shubetsu]").first().css("visibility", "visible");

	//e文書処理用内部情報更新
	resetEbunshoNaibuData();
}

/**
 * 添付済みe文書明細部のアクション紐付け
 * @param target
 */
function tenpuzumiebunshoMeisaiInit(target) {
	//該当行削除可否を再設定
	$.each(target.find("input[name=tenpuzumi_ebunsho_nengappi]"),checkTenpuzumiEbunshoMeisaiDelete);
	//e文書フラグ有効でない場合、またはwfSeigyo.fileTenpuUpd=false(=tenpuListが無い)の場合はe文書情報を入力不可に変更
	if($("#workflowForm").find("input[name=ebunshoShiyouFlg]").val() == '0' || !($("#tenpuList").size()) ) {
		target.find("select[name=tenpuzumi_ebunsho_shubetsu]").prop("disabled", true);
		target.find("button[name=tenpuzumi_ebunshoMeisaiAdd]").prop("disabled", true);
		target.find("button[name=tenpuzumi_ebunshoMeisaiDelete]").prop("disabled", true);
		target.find("input[name=tenpuzumi_ebunsho_nengappi]").prop("disabled", true);
		target.find("input[name=tenpuzumi_ebunsho_kingaku]").prop("disabled", true);
		target.find("input[name=tenpuzumi_ebunsho_hinmei]").prop("disabled", true);
		target.find("input[name=tenpuzumi_ebunsho_hakkousha]").prop("disabled", true);
		target.find("input[name=tenpuzumi_denshitorihiki_chk]").prop("disabled", true);
		target.find("input[name=tenpuzumi_tsfuyo_chk]").prop("disabled", true);
		target.find("input[name=tenpuzumi_ebunshoCheckbox]").prop("disabled", true);
	}
	//1行目以外の種別非表示
	$(".tenpuzumiEbunshoMeisai").each(function() {
		$(this).find("select[name=tenpuzumi_ebunsho_shubetsu]").css("visibility", "hidden");
		$(this).find("select[name=tenpuzumi_ebunsho_shubetsu]").first().css("visibility", "visible");
	});
}

/** 新規添付ファイルe文書明細部「＋」ボタン押下時Function */
function ebunshoMeisaiAdd() {

	//各添付ファイル明細が99件以上なら中止
	if(99 <= $(this).closest("div").children("span").length) return;

	//１行目をコピー
	var span = $("<span class='ebunshoMeisaiRecord'> " + $(this).closest(".ebunshoMeisaiRecord").html() + "<\/span>");
	span.find("button[name=ebunshoMeisaiDelete]").prop("disabled", false);
	span.find("select[name=ebunsho_shubetsu]").css("visibility", "hidden");
	$(this).closest("div").append(span);

	//中身を消してアクション紐付け
	inputClear(span);
	commonInit(span);
	ebunshoMeisaiInit(span);

	//e文書処理用内部情報更新
	resetEbunshoNaibuData();

	setEbunshoShubetsu(span);
}

/** 新規添付ファイルe文書明細部「－」ボタン押下時Function */
function ebunshoMeisaiDelete() {

	//各添付ファイル明細が1件しかないなら中止
	if( 1 == $(this).closest("div").children("span.ebunshoMeisaiRecord").length ) return;

	var div = $(this).closest("div.ebunshoMeisai");

	$(this).closest("span.ebunshoMeisaiRecord").remove();
	div.find("select[name=ebunsho_shubetsu]").first().css("visibility", "visible");
	//e文書処理用内部情報更新
	resetEbunshoNaibuData();
}

/**
 * 新規添付ファイルe文書明細部の初期設定
 * @param target
 */
function ebunshoMeisaiInit(target) {
	//該当行削除可否を再設定
	$.each($("button[name=ebunshoMeisaiAdd]"),checkEbunshoMeisaiDelete);
	//1行目以外の種別非表示
	$(".ebunshoMeisai").each(function() {
		$(this).find("select[name=ebunsho_shubetsu]").css("visibility", "hidden");
		$(this).find("select[name=ebunsho_shubetsu]").first().css("visibility", "visible");
	});
}



/** e文書使用チェックボックス変更時やe文書明細情報変更時、該当行のe文書明細を削除可能にするか判別・設定 */
function setEbunshoDeletable(){
	var div = $(this).closest("div.newfileDiv").find("div.tenpudata");
	var flg = (div.find("input[name=ebunshoCheckbox]").prop('checked'));
	div = div.parent("div.newfileDiv");
	changeEbunshoEnable(div,flg);
	//e文書処理用内部情報更新
	resetEbunshoNaibuData()
}

/** 添付済みのリンクファイルのe文書使用チェックボックス変更時やe文書明細情報変更時、該当行のe文書明細を削除可能にするか判別・設定 */
function setTenpuzumiEbunshoDeletable(){
	var div = $(this).closest("div.tenpuzumifileDiv").find("div.tenpuzumiMeisai");
	var flg = (div.find("input[name=tenpuzumi_ebunshoCheckbox]").prop('checked'));
	div = div.parent("div.tenpuzumifileDiv");
	changeTenpuzumiEbunshoEnable(div,flg);
	resetEbunshoNaibuData()
}

/** 新規添付ファイルe文書明細の品名表示非表示制御 */
function changeEbunshoMeisai(){
	changeEbunshoMeisaiMain($(this).closest("div.ebunshoMeisai"), $(this).val());
	setEbunshoShubetsu($(this));
}

/** 添付済みe文書明細の品名表示非表示制御 */
function changeTenpuzumiEbunshoMeisai(){
	changeEbunshoMeisaiMain($(this).closest("div.tenpuzumiEbunshoMeisai"), $(this).val());
	setTenpuzumiEbunshoShubetsu($(this));
}

/** e文書明細のレイアウトを種別に応じて変更 */
function changeEbunshoMeisaiMain(div, shubetsu){
	var hinmei = div.find(".hinmei-group");
	var nengappiLabel = div.find(".ebNengappi");
	var kingakuLabel = div.find(".ebKingaku");
	var hakkoushaLabel = div.find(".ebHakkousha");
	var ebunshoEnableFlg = $("input[name=ebunshoEnableFlg]").val();
	var nengappi_nini_flg = document.getElementsByName("nengappi_nini_flg");
	var kingaku_nini_flg = document.getElementsByName("kingaku_nini_flg");
	var hakkousha_nini_flg = document.getElementsByName("hakkousha_nini_flg");
	ebunsho_shubetsu_int = parseInt(div.find("select[name=ebunsho_shubetsu]").val() ?? div.find("select[name=tenpuzumi_ebunsho_shubetsu]").val(), 10);

	nengappiLabel.children("span").remove();
	kingakuLabel.children("span").remove();
	hakkoushaLabel.children("span").remove();

	if(shubetsu == "3"){
		// 種別が3:納品書の場合のみ表示
		if(ebunshoEnableFlg == "1")hinmei.css("display","");

	}else{
		hinmei.css("display","none");
	};

	if(nengappi_nini_flg[ebunsho_shubetsu_int].value == 0)
	{
		nengappiLabel.prepend($("<span class='required'>*</span>"));
	}

// 2022/06/24 Ver22.05.31.01 納品書の条件を削除	
//	if(shubetsu != "3" && kingaku_nini_flg[ebunsho_shubetsu_int].value == 0)
	if(kingaku_nini_flg[ebunsho_shubetsu_int].value == 0)
	{
		kingakuLabel.prepend($("<span class='required'>*</span>"));
	}

	if(hakkousha_nini_flg[ebunsho_shubetsu_int].value == 0)
	{
		hakkoushaLabel.prepend($("<span class='required'>*</span>"));
	}
}

/** 新規添付ファイルe文書明細のhiddenにしている種別の値を更新*/
function setEbunshoShubetsu(div){
	var target = div.closest("div.ebunshoMeisai");
	var shubetsu = target.find("select[name=ebunsho_shubetsu]").first().val();
	target.find("select[name=ebunsho_shubetsu]").val(shubetsu);
	ebunsho_shubetsu_int = parseInt(shubetsu, 10);
}

/** 添付済みe文書明細のhiddenにしている種別の値を更新*/
function setTenpuzumiEbunshoShubetsu(div){
	var target = div.closest("div.tenpuzumiEbunshoMeisai");
	var shubetsu = target.find("select[name=tenpuzumi_ebunsho_shubetsu]").first().val();
	target.find("select[name=tenpuzumi_ebunsho_shubetsu]").val(shubetsu);
	ebunsho_shubetsu_int = parseInt(shubetsu, 10);
}



/** 電子取引チェックボックス切り替え時処理 */
function changeDenshitorihiki(){
	var target = $(this).closest(".tenpudata")
	if(target.find("input[name=denshitorihikiCheckbox]").prop("checked")){
		target.find("input[name=ebunshoCheckbox]").prop("checked",true);
		target.find("input[name=ebunshoCheckbox]").prop("disabled",true);
		if($("#workflowForm").find("input[name=ebunshoDenshitsCheckDefault]").val() == '1'){
			target.find("input[name=tsfuyoCheckbox]").prop("checked",true);
		}else{
			target.find("input[name=tsfuyoCheckbox]").prop("checked",false);
		}
		var div = $(this).parent().parent("div.newfileDiv");
		changeEbunshoEnable(div,true);
	}else{
		target.find("input[name=ebunshoCheckbox]").prop("disabled",false);
		target.find("input[name=tsfuyoCheckbox]").prop("checked",false);
	}
	resetEbunshoNaibuData(); // フラグ値更新
}

/** 添付済みファイルの電子取引チェックボックス切り替え時処理 */
function changeTenpuzumiDenshitorihiki(){
	var target = $(this).closest(".tenpuzumiMeisai")
	if(target.find("input[name=tenpuzumi_denshitorihiki_chk]").prop("checked")){
		target.find("input[name=tenpuzumi_ebunshoCheckbox]").prop("checked",true);
		target.find("input[name=tenpuzumi_ebunshoCheckbox]").prop("disabled",true);
		if($("#workflowForm").find("input[name=ebunshoDenshitsCheckDefault]").val() == '1'){
			target.find("input[name=tenpuzumi_tsfuyo_chk]").prop("checked",true);
		}else{
			target.find("input[name=tenpuzumi_tsfuyo_chk]").prop("checked",false);
		}
		var div = $(this).parent().parent("div.tenpuzumifileDiv");
		changeTenpuzumiEbunshoEnable(div,true);
	}else{
		target.find("input[name=tenpuzumi_ebunshoCheckbox]").prop("disabled",false);
		target.find("input[name=tenpuzumi_tsfuyo_chk]").prop("checked",false);
	}
	resetEbunshoNaibuData();
}

/** TS付与チェックボックス切り替え時処理 */
function changeTsfuyo(){
	var target = $(this).closest(".tenpudata")
	if(target.find("input[name=tsfuyoCheckbox]").prop("checked")){
		target.find("input[name=ebunshoCheckbox]").prop("checked",true);
		target.find("input[name=ebunshoCheckbox]").prop("disabled",true);
		target.find("input[name=denshitorihikiCheckbox]").prop("checked",true);
		var div = $(this).parent().parent("div.newfileDiv");
		changeEbunshoEnable(div,true);
	}
	resetEbunshoNaibuData(); // フラグ値更新
}

/** 添付済みファイルのTS付与チェックボックス切り替え時処理 */
function changeTenpuzumiTsfuyo(){
	var target = $(this).closest(".tenpuzumiMeisai");
	// 切り替えはタイムスタンプ再付与対象でないときだけ
	if(target.find("input[name=tenpuzumi_tsfuyo_chk]").prop("checked") && target.find("button[name=addTimestamp]").length == 0){
		target.find("input[name=tenpuzumi_ebunshoCheckbox]").prop("checked",true);
		target.find("input[name=tenpuzumi_ebunshoCheckbox]").prop("disabled",true);
		target.find("input[name=tenpuzumi_denshitorihiki_chk]").prop("checked",true);
		var div = $(this).parent().parent("div.tenpuzumifileDiv");
		changeTenpuzumiEbunshoEnable(div,true);
	}
	resetEbunshoNaibuData();
}

/**
 * e文書明細部の入力可否切り替え
 * @param target	添付ファイルのdiv
 * @param flg		true:有効 false:無効
 */
function changeEbunshoEnable(target,flg){
	target.children("div.ebunshoMeisai").children("span.ebunshoMeisaiRecord").find("select[name=ebunsho_shubetsu]").prop("disabled", !flg);
	target.children("div.ebunshoMeisai").children("span.ebunshoMeisaiRecord").find("button[name=ebunshoMeisaiAdd]").prop("disabled", !flg);
	target.children("div.ebunshoMeisai").children("span.ebunshoMeisaiRecord").find("button[name=ebunshoMeisaiDelete]").prop("disabled", !flg);
	target.children("div.ebunshoMeisai").children("span.ebunshoMeisaiRecord").find("input[name=ebunsho_nengappi]").prop("disabled", !flg);
	target.children("div.ebunshoMeisai").children("span.ebunshoMeisaiRecord").find("input[name=ebunsho_kingaku]").prop("disabled", !flg);
	target.children("div.ebunshoMeisai").children("span.ebunshoMeisaiRecord").find("input[name=ebunsho_hinmei]").prop("disabled", !flg);
	target.children("div.ebunshoMeisai").children("span.ebunshoMeisaiRecord").find("input[name=ebunsho_hakkousha]").prop("disabled", !flg);

	target.children("div.tenpudata").find("button[name=tenpuFileDelete]").prop("disabled", false);
	if(flg == false){
		target.children("div.tenpudata").find("input[name=denshitorihikiCheckbox]").prop("checked", false);
		target.children("div.tenpudata").find("input[name=tsfuyoCheckbox]").prop("checked", false);
	}

	if((target.children("div.tenpudata").find("input[name=denshitorihikiCheckbox]").prop('checked'))){
		target.children("div.tenpudata").find("input[name=denshitorihikiFlg]").val("1");
	}else{
		target.children("div.tenpudata").find("input[name=denshitorihikiFlg]").val("0");
	}

	if((target.children("div.tenpudata").find("input[name=tsfuyoCheckbox]").prop('checked'))){
		target.children("div.tenpudata").find("input[name=tsfuyoFlg]").val("1");
	}else{
		target.children("div.tenpudata").find("input[name=tsfuyoFlg]").val("0");
	}

	$.each($("input[name=ebunsho_nengappi]"),checkEbunshoMeisaiDelete);
}

/**
 * 添付済みファイルのe文書明細部の入力可否切り替え
 */
 function changeTenpuzumiEbunshoEnable(target,flg){
	// 新規添付欄があるとき
	if($("#tenpuList").size()){
		target.children("div.tenpuzumiEbunshoMeisai").children("span.ebunshoMeisaiRecord").find("select[name=tenpuzumi_ebunsho_shubetsu]").prop("disabled", !flg);
		target.children("div.tenpuzumiEbunshoMeisai").children("span.ebunshoMeisaiRecord").find("button[name=tenpuzumi_ebunshoMeisaiAdd]").prop("disabled", !flg);
		target.children("div.tenpuzumiEbunshoMeisai").children("span.ebunshoMeisaiRecord").find("button[name=tenpuzumi_ebunshoMeisaiDelete]").prop("disabled", !flg);
		target.children("div.tenpuzumiEbunshoMeisai").children("span.ebunshoMeisaiRecord").find("input[name=tenpuzumi_ebunsho_nengappi]").prop("disabled", !flg);
		target.children("div.tenpuzumiEbunshoMeisai").children("span.ebunshoMeisaiRecord").find("input[name=tenpuzumi_ebunsho_kingaku]").prop("disabled", !flg);
		target.children("div.tenpuzumiEbunshoMeisai").children("span.ebunshoMeisaiRecord").find("input[name=tenpuzumi_ebunsho_hinmei]").prop("disabled", !flg);
		target.children("div.tenpuzumiEbunshoMeisai").children("span.ebunshoMeisaiRecord").find("input[name=tenpuzumi_ebunsho_hakkousha]").prop("disabled", !flg);

		//target.children("div.tenpuzumiMeisai").find("button[name=tenpuzumi_ebunshoMeisaiDelete]").prop("disabled", false);

		if(flg == false){
			target.children("div.tenpuzumiMeisai").find("input[name=tenpuzumi_denshitorihiki_chk]").prop("checked", false);
			target.children("div.tenpuzumiMeisai").find("input[name=tenpuzumi_tsfuyo_chk]").prop("checked", false);
		}

		if((target.children("div.tenpuzumiMeisai").find("input[name=tenpuzumi_denshitorihiki_chk]").prop('checked'))){
			target.children("div.tenpuzumiMeisai").find("input[name=tenpuzumi_denshitorihikiFlg]").val("1");
		}else{
			target.children("div.tenpuzumiMeisai").find("input[name=tenpuzumi_denshitorihikiFlg]").val("0");
		}

		if((target.children("div.tenpuzumiMeisai").find("input[name=tenpuzumi_tsfuyo_chk]").prop('checked'))){
			target.children("div.tenpuzumiMeisai").find("input[name=tenpuzumi_tsfuyoFlg]").val("1");
		}else{
			target.children("div.tenpuzumiMeisai").find("input[name=tenpuzumi_tsfuyoFlg]").val("0");
		}

		$.each($("input[name=tenpuzumi_ebunsho_nengappi]"),checkTenpuzumiEbunshoMeisaiDelete);
	}
}

/** 添付済みe文書明細に入力がある場合には該当行の「－」ボタンを無効化 */
function checkTenpuzumiEbunshoMeisaiDelete(){
	var target = $(this).parent("span")
	var count = target.parent("div").find("span.ebunshoMeisaiRecord").length;

	var nen = target.find("input[name=tenpuzumi_ebunsho_nengappi]").val();
	var kin = target.find("input[name=tenpuzumi_ebunsho_kingaku]").val();
	var hin = target.find("input[name=tenpuzumi_ebunsho_hinmei]").val();
	var hak = target.find("input[name=tenpuzumi_ebunsho_hakkousha]").val();
	if(nen != "" || kin != "" || hak != "" || hin != ""){
		target.find("button[name=tenpuzumi_ebunshoMeisaiDelete]").prop("disabled", true);
	}else{
		//該当項目残り1つでなければ有効にする
		if( count > 1 && target.parent("div").parent("div.tenpuzumifileDiv").find("div.tenpuzumiMeisai").find("input[name=tenpuzumi_ebunshoCheckbox]").prop('checked')){
			target.find("button[name=tenpuzumi_ebunshoMeisaiDelete]").prop("disabled", false);
		}
	}
}

/** 新規添付ファイルe文書明細に入力がある場合には該当行の「－」ボタンを無効化 */
function checkEbunshoMeisaiDelete(){
	var target = $(this).closest(".ebunshoMeisaiRecord")

	var nen = target.find("input[name=ebunsho_nengappi]").val();
	var kin = target.find("input[name=ebunsho_kingaku]").val();
	var hin = target.find("input[name=ebunsho_hinmei]").val();
	var hak = target.find("input[name=ebunsho_hakkousha]").val();
	if(nen != "" || kin != "" || hak != "" || hin != ""){
		target.find("button[name=ebunshoMeisaiDelete]").prop("disabled", true);
		target.parent("div").parent("div.newfileDiv").find("div.tenpudata").find("button[name=tenpuFileDelete]").prop("disabled", true);
	}else{
		if(target.parent("div").parent("div.newfileDiv").find("div.tenpudata").find("input[name=ebunshoCheckbox]").prop('checked')){
			target.find("button[name=ebunshoMeisaiDelete]").prop("disabled", false);
		}
	}
}

/** e文書明細の内部処理用変数と各添付ファイルのe文書使用フラグを再設定 */
function resetEbunshoNaibuData(){
	$.each( $("div.tenpuzumifileDiv"),function(){
		if(($(this).find("div.tenpuzumiMeisai").find("input[name=tenpuzumi_ebunshoCheckbox]").prop('checked'))){
			$(this).find("div.tenpuzumiMeisai").find("input[name=tenpuzumi_ebunshoflg]").val("1");
			flg = true;

			if(($(this).find("div.tenpuzumiMeisai").find("input[name=tenpuzumi_denshitorihiki_chk]").prop('checked'))){
				$(this).find("div.tenpuzumiMeisai").find("input[name=tenpuzumi_denshitorihikiFlg]").val("1");
			}else{
				$(this).find("div.tenpuzumiMeisai").find("input[name=tenpuzumi_denshitorihikiFlg]").val("0");
			}

			if(($(this).find("div.tenpuzumiMeisai").find("input[name=tenpuzumi_tsfuyo_chk]").prop('checked'))){
				$(this).find("div.tenpuzumiMeisai").find("input[name=tenpuzumi_tsfuyoFlg]").val("1");
			}else{
				$(this).find("div.tenpuzumiMeisai").find("input[name=tenpuzumi_tsfuyoFlg]").val("0");
			}
		}else{
			$(this).find("div.tenpuzumiMeisai").find("input[name=tenpuzumi_ebunshoflg]").val("0");
			$(this).find("div.tenpuzumiMeisai").find("input[name=tenpuzumi_denshitorihikiFlg]").val("0");
			$(this).find("div.tenpuzumiMeisai").find("input[name=tenpuzumi_tsfuyoFlg]").val("0");
		}

		var count = $(this).find("div.tenpuzumiEbunshoMeisai").children("span.ebunshoMeisaiRecord").length;
		$(this).find("input[name=tenpuzumi_ebunshodata_count]").val(count);
	});

	$.each( $("div.newfileDiv"),function(){
		//添付ファイルごとのe文書使用フラグがONか確認
		var flg = false;
		if(($(this).find("div.tenpudata").find("input[name=ebunshoCheckbox]").prop('checked'))){
			$(this).find("div.tenpudata").find("input[name=ebunshoflg]").val("1");
			flg = true;

			if(($(this).find("div.tenpudata").find("input[name=denshitorihikiCheckbox]").prop('checked'))){
				$(this).find("div.tenpudata").find("input[name=denshitorihikiFlg]").val("1");
			}else{
				$(this).find("div.tenpudata").find("input[name=denshitorihikiFlg]").val("0");
			}

			if(($(this).find("div.tenpudata").find("input[name=tsfuyoCheckbox]").prop('checked'))){
				$(this).find("div.tenpudata").find("input[name=tsfuyoFlg]").val("1");
			}else{
				$(this).find("div.tenpudata").find("input[name=tsfuyoFlg]").val("0");
			}

		}else{
			$(this).find("div.tenpudata").find("input[name=ebunshoflg]").val("0");
			$(this).find("div.tenpudata").find("input[name=denshitorihikiFlg]").val("0");
			$(this).find("div.tenpudata").find("input[name=tsfuyoFlg]").val("0");
		}

		//親要素にファイルが添付済みかつe文書使用フラグがONならファイル名登録
		var fileNm = $(this).find("div.tenpudata").find("input[name=uploadFile]").val();
		if(fileNm != "" && flg == true){
			$(this).find("div.tenpudata").find("input[name=ebunsho_tenpufilename_header]").val(fileNm);
			$.each( $(this).find("div.ebunshoMeisai").children("span.ebunshoMeisaiRecord"),function(){
				$(this).find("input[name=ebunsho_tenpufilename]").val(fileNm);
			});
		}else{
			//ファイル未添付かe文書使用フラグがオフなら空白値を指定
			$(this).find("div.tenpudata").find("input[name=ebunsho_tenpufilename_header]").val("");
			$.each( $(this).find("div.ebunshoMeisai").children("span.ebunshoMeisaiRecord"),function(){
				$(this).find("input[name=ebunsho_tenpufilename]").val("");
			});
		}

		//明細行カウントを更新
		var msct = $(this).find("div.ebunshoMeisai").children("span.ebunshoMeisaiRecord").length;
		$(this).find("div.tenpudata").find("input[name=ebunsho_meisaicnt]").val(msct);
	});
}

/** 添付済みe文書明細で指定したファイルに該当する明細件数を調べる */
function tenpuzumiEbunshoMeisaiCount() {
	var cnt = $(this).parent("span").parent("div").find("span.ebunshoMeisaiRecord").length;
	alert(cnt);
	return cnt;
}

/** タイムスタンプ再付与イベント */
function addTimestampEventBtn() {
	if(isDirty){
		if(!confirm("伝票内容が変更されています。\n変更を破棄しますか？\n変更を反映する場合はキャンセルし、｢更新｣してください。")){
			return false;}
	}
	$(this).prop("disabled", true);
	var ebNo = $(this).parent("div.tenpuzumiMeisai").find("input[name=tenpuzumi_ebunsho_no]").val();
	$('input[name=addTimestampEbunshoNo]').val(ebNo);

	var form = document.getElementById("workflowForm");
	form.action = 'ebunsho_add_timestamp';
	$(form).submit();
}

/** ワークフローイベント */
function workflowEventBtn(eventCode) {

	//コメントの必須、長さチェック
	var comment;
	if ('niniMemo' == eventCode) {
		comment = $("textarea[name=niniMemo]").val();
	} else {
		comment = $("textarea[name=comment]").val();
	}
	var crNum = comment.match(/\r\n|\n/g);
	var mojiNum = comment.length;
	if (crNum != null) {
		mojiNum = mojiNum + crNum.length;
	}
	
	/*202207 承認解除で未入力の時*/
	if('shouninkaijo' == eventCode && mojiNum == 0){
		alert("コメントが未入力のため、承認解除できません。");
		return;
	}
	
	if ('niniMemo' == eventCode && mojiNum == 0) {
		alert("メモを入力して下さい。");
		return;
	} else if (mojiNum > 400) {
		alert("入力できる文字数は400文字までです。");
		return;
	}

	//差戻し先設定
	if(eventCode == 'sashimodoshi'){
		var saki = $("input[name=sashimodoshiSakiEdano]:checked");
		$("input[name=sashimodoshiSakiGougiEdano]").val(saki.attr("data-gougiEdano"));
		$("input[name=sashimodoshiSakiGougiEdaedano]").val(saki.attr("data-gougiEdaedano"));
	}

	var form = document.getElementById("workflowForm");
	$(form).find('button[type=button]').prop('disabled', 'true');	//サーバー処理中に再度ワークフローボタン押下できないように制御

	//以降イベントを発生させる
	if('shinsei' == eventCode || 'shounin' == eventCode || 'shouninkaijo' == eventCode) {
		//申請と承認は個々の画面で定義
		eventBtn(eventCode);
	} else {
		switch (eventCode) {
			case 'torimodoshi':   form.action = 'denpyou_torimodoshi';       break; // 取戻し
			case 'torisage':      form.action = 'denpyou_torisage';          break; // 取下げ
			case 'hinin':         form.action = 'denpyou_hinin';             break; // 否認
			case 'sashimodoshi':  form.action = 'denpyou_sashimodoshi';      break; // 差戻し
			case 'niniMemo':      form.action = 'denpyou_nini_memo_touroku'; break; // 任意メモ登録
			//case 'shouninkaijo':  form.action = 'denpyou_shouninkaijo';      break; // 承認解除
			default: form.action = '';
		}
		$(form).submit();
	}
}

/** ワークフロー個別伝票処理 */
function kobetsuDenpyouProc(eventCode) {
	var errorFlg = true;
	switch (eventCode) {
	// 申請
	case 'shinsei':
		if( typeof kobetsuDenpyouProc_shinsei == "function") {
			if(! kobetsuDenpyouProc_shinsei()) {errorFlg = false;};
		} break;
	// 取戻し
	case 'torimodoshi':
		if( typeof kobetsuDenpyouProc_torimodoshi == "function") {
			if(! kobetsuDenpyouProc_torimodoshi()) {errorFlg = false;};
		} break;
	// 取下げ
	case 'torisage':
		if( typeof kobetsuDenpyouProc_torisage == "function") {
			if(! kobetsuDenpyouProc_torisage()) {errorFlg = false;};
		} break;
	// 承認
	case 'shounin':
		if( typeof kobetsuDenpyouProc_shounin == "function") {
			if(! kobetsuDenpyouProc_shounin()) {errorFlg = false;};
		} break;
	// 否認
	case 'hinin':
		if( typeof kobetsuDenpyouProc_hinin == "function") {
			if(! kobetsuDenpyouProc_hinin()) {errorFlg = false;};
		} break;
	// 差戻し
	case 'sashimodoshi':
		if( typeof kobetsuDenpyouProc_sashimodoshi == "function") {
			if(! kobetsuDenpyouProc_sashimodoshi()) {errorFlg = false;};
		} break;
	// 任意メモ登録
	case 'niniMemo':
		if( typeof kobetsuDenpyouProc_niniMemo == "function") {
			if(! kobetsuDenpyouProc_niniMemo()) {errorFlg = false;};
		} break;
	// 参照起票
	case 'sanshouKihyou':
		if( typeof kobetsuDenpyouProc_sanshouKihyou == "function") {
			if(! kobetsuDenpyouProc_sanshouKihyou()) {errorFlg = false;};
		} break;
	}
	return errorFlg;
}

/** 承認ルート登録イベント */
function shouninRouteEventBtn() {
	if(isDirty){
		if(!confirm("伝票内容が変更されています。\n変更を破棄して承認ルート登録に進みますか？\n変更を反映する場合はキャンセルし、｢更新｣してください。")){
			return;}
	}
	var form = document.getElementById("shouninRouteTourokuForm");
	$(form).submit();
}

/** マル秘設定イベント */
function maruhiSetteiEventBtn() {

	var form = document.getElementById("workflowForm");
	form.action = 'denpyou_maruhi_settei';
	$(form).submit();
}

/** マル秘解除イベント */
function maruhiKaijyoEventBtn() {

	var form = document.getElementById("workflowForm");
	form.action = 'denpyou_maruhi_kaijyo';
	$(form).submit();
}

/** 参照起票イベント */
function sanshouKihyouEventBtn() {
	var form = document.getElementById("shanshouKihyouForm");
	$(form).submit();
}

/**
 * 仕訳データ変更イベント
 * env:"de3" or "sias"
 */
function shiwakeDataHenkouEventBtn(env) {
	if (env == "de3") {
		window.location.href = "shiwake_data_de3_henkou?denpyouId=${denpyouId}";
	} else {
		window.location.href = "shiwake_data_sias_henkou?denpyouId=${denpyouId}";
	}
}

/** 添付ファイル削除イベント */
function fileDeleteEventBtn(edano, ebunshoDiv) {

<c:if test="${denpyouJoutai != '00' and denpyouJoutai != '10'}">
	if(!confirm("伝票への添付済みファイルを削除しますか？")){
		return false;
	}
</c:if>
	if(isDirty){
		if(!confirm("伝票内容が変更されています。\n変更を破棄しますか？\n変更を反映する場合はキャンセルし、｢更新｣してください。")){
			return false;}
	}
	$('input[name=downloadFileNo]').val(edano);
	//20220610 e文書の削除用処理
	var ebNo = ebunshoDiv.find("input[name=tenpuzumi_ebunsho_no]").val();
	if(ebNo != null || ebNo != ""){
		$('input[name=downloadEbunshoNo]').val(ebNo);
	}
	var form = document.getElementById("workflowForm");
	form.action = 'denpyou_file_delete';
	$(form).submit();
}

/*
 * 起票部門セレクターの初期化
 */
function kihyouBumonSelectInit(target) {
	if(target.length > 0) {
		var selected = target.find('option:selected');
		$('input[name=kihyouBumonCd]').val(target.val());
		$('input[name=kihyouBumonName]').val(selected.attr('data-bName'));
		$('input[name=bumonRoleId]').val(selected.attr('data-brId'));
		$('input[name=bumonRoleName]').val(selected.attr('data-brName'));
		$('input[name=daihyouFutanBumonCd]').val(selected.attr('data-daiBuCd'));

		target.change(function(){
			var selected = $(this).find('option:selected');
			$('input[name=kihyouBumonCd]').val($(this).val());
			$('input[name=kihyouBumonName]').val(selected.attr('data-bName'));
			$('input[name=bumonRoleId]').val(selected.attr('data-brId'));
			$('input[name=bumonRoleName]').val(selected.attr('data-brName'));
			$('input[name=daihyouFutanBumonCd]').val(selected.attr('data-daiBuCd'));
		});
	}
}

/**
 * 起票部門変更時
 */
$("#kihyouBumonSelect").change(function(){
	kianKaijo();
});

/**
 * 関連伝票選択ボタン押下時Function
 * 'dialog'id内に関連伝票選択画面をロードする。
 */
function commonKanrenDenpyouSentaku() {

	var obj =[];

	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}

	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "添付伝票選択",
		buttons:
		[
			{   text :'追加',
				id   :'tenpuDenpyouAddBtn',
				click:function() {
				$(this).dialog().load(
					"kanren_denpyou_sentaku_confirm?gamenDenpyouId=" + $("span#denpyouId").text(),
					$.map(
						/* チェックボックスがチェックされたもののみ送信 */
						$(dialog).find("input"),
						function(n, i) {
							if (!n.name) {
								return null;
							}
							//checkboxは本来のsubmit同様、チェックが無ければ送信しない。
							if (n.type == 'checkbox' && !$(n).prop('checked')) {
								return null;
							}
							return {
								name  : n.name,
								value : $(n).val()
							};
						},
						/* チェックされた要素を取得 */
						$(this).find("input:checked").each(
							function(){
								obj.push($(this).parents("tr"));
							}
						)
					),
					//callback
					function() {
						//確認イベントで入力エラーがあればダイアログを表示したまま
						if ("0" != $(dialog).find("#errorCnt").val()) {
							return ;
						}

						//旅費仮払の明細を旅費伝票に追加
						var meisaiTsuikaDenpyouId = "";
						var meisaiTsuikaFlg = false;
						for (var i = 0; i < obj.length; i++) {
							if($("#workflowForm").find("input[name=denpyouKbn]").val() == 'A004' && obj[i].find("input[name=kanrenDenpyouKbn]").val() == 'A005'){
								meisaiTsuikaDenpyouId += obj[i].find("input[name=kanrenDenpyouId]").val() + ",";
								meisaiTsuikaFlg = true;
							}
						}

						if(meisaiTsuikaFlg){
							if (confirm('旅費仮払の明細データを引き継ぎますか？')){
								var formObject = document.getElementById("workflowForm");
								formObject.action = 'ryohi_seisan_add_karibarai_meisai?meisaiTsuikaDenpyouId=' + meisaiTsuikaDenpyouId;
								formObject.method = 'post';
								formObject.target = '_self';
							}
						}

						//確認イベントが正常ならばダイアログを閉じてメイン画面に反映
						$(this).dialog("close");

						for (var i = 0; i < obj.length; i++) {
							setDialogKanrenDenpyouInfo(obj[i]);
						}

						isDirty = true;

						//フォームの送信
						$(formObject).submit();
					}
				);
				}
			},
			{	text :'閉じる',
				click:function() {$(this).dialog("close");}
			}
		]
	})
	.load("kanren_denpyou_sentaku_init?gamenDenpyouId=" + $("span#denpyouId").text());
}

/**
 * ダイアログ関連伝票情報設定
 */
function setDialogKanrenDenpyouInfo(kanren) {
	//イベント設定
	kanrenDenpyouAdd(kanren);
	//入力補助
	commonInit(kanren);
}

/**
 * 関連伝票行追加処理
 */
function kanrenDenpyouAdd(kanrenInfo) {
	var tr = null;
	if ($("#kanrenDenpyouTableDiv").css("display") != "none") {
		//１行目の関連伝票をコピーして内容を削除
		var _tr = $("#kanrenDenpyouList tr.kanrenDenpyou:first");
		tr = _tr.clone();
		// 行の挿入
		$("#kanrenDenpyouList").append(tr);
		// 初期化
		commonInit($(tr));
		_tr = _tr.next();
		tr = $("#kanrenDenpyouList tr.kanrenDenpyou:last");
	} else {
		// 空ならそこをそのまま使用する。
		tr = $("#kanrenDenpyouList tr.kanrenDenpyou:first");
	}

	// 行の初期化
	kanrenDenpyouClear(tr);

	// 値の設定
	setKanrenDenpyouInfo($("#kanrenDenpyouList tr.kanrenDenpyou").length - 1, kanrenInfo);

	// 再表示
	displayKanrenDenpyou();
	isDirty = true;
}

/**
 * 関連伝票行をクリアします。
 */
function kanrenDenpyouClear(tr) {
	var _tr = tr;
	$.each($(_tr).find("input"), function(ii, obj) {
		$(obj).val("");
	});
	$.each($(_tr).find("td"), function(ii, obj) {
		$(obj).text("");
	});
	_tr = _tr.next();
}
/**
 * 内容設定されている関連伝票の数を取得します。
 */
function getEnableKanrenCount() {
	var count = 0;
	$.each($("#kanrenDenpyouList input[name=kanrenDenpyouId]"), function(ii, obj) {
		if ('' != $(obj).val()) {
			count++;
		}
	});
	return count;
}

/**
 * 関連伝票情報設定
 */
function setKanrenDenpyouInfo(ii, kanrenInfo) {
	var kanren = $("#kanrenDenpyouList tr.kanrenDenpyou:eq(" + ii + ")");
	kanren.find("input[name=hyoujiKanrenEmbedSpace]").val(kanrenInfo.find("input[name=hyoujiKanrenEmbedSpace]").val());
	kanren.find("input[name=hyoujiKanrenDenpyouKbn]").val(kanrenInfo.find("input[name=hyoujiKanrenDenpyouKbn]").val());
	kanren.find("input[name=hyoujiKanrenDenpyouShubetsuUrl]").val(kanrenInfo.find("input[name=hyoujiKanrenDenpyouShubetsuUrl]").val());
	kanren.find("input[name=hyoujiKanrenDenpyouUrl]").val(kanrenInfo.find("input[name=hyoujiKanrenDenpyouUrl]").val());
	kanren.find("input[name=hyoujiKanrenDenpyouShubetsu]").val(kanrenInfo.find("input[name=hyoujiKanrenDenpyouShubetsu]").val());
	kanren.find("input[name=hyoujiKanrenDenpyouId]").val(kanrenInfo.find("input[name=hyoujiKanrenDenpyouId]").val());
	kanren.find("input[name=hyoujiKanrenTenpuFileName]").val(kanrenInfo.find("input[name=hyoujiKanrenTenpuFileName]").val());
	kanren.find("input[name=hyoujiKanrenTenpuFileUrl]").val(kanrenInfo.find("input[name=hyoujiKanrenTenpuFileUrl]").val());
	kanren.find("input[name=hyoujiKanrenKihyouBi]").val(kanrenInfo.find("input[name=hyoujiKanrenKihyouBi]").val());
	kanren.find("input[name=hyoujiKanrenShouninBi]").val(kanrenInfo.find("input[name=hyoujiKanrenShouninBi]").val());

	kanren.find("input[name=kanrenDenpyouId]").val(kanrenInfo.find("input[name=kanrenDenpyouId]").val());
	kanren.find("input[name=kanrenDenpyouKbn]").val(kanrenInfo.find("input[name=kanrenDenpyouKbn]").val());
	kanren.find("input[name=kanrenTourokuTime]").val(kanrenInfo.find("input[name=kanrenTourokuTime]").val());
	kanren.find("input[name=kanrenShouninTime]").val(kanrenInfo.find("input[name=kanrenShouninTime]").val());

	kanren.find("input[name=ringiKingakuHikitsugiFlg]").val("0");
}


/**
 * 内部項目(Hidden設定情報)から画面表示を行います。
 * 以下のタイミングでhyoujiKanrenEmbedSpaceのhiddenが変更され、当functionが呼ばれます。
 * ・伝票画面の表示(WorkflowEventControl、配列初期化)
 * ・関連伝票ダイアログのの追加ボタン押下時(JS制御、配列への追加)
 * ・仮払選択時(JS制御、配列への追加)
 * ・関連伝票解除時(JS制御、配列からの削除)
 */
function displayKanrenDenpyou() {

	if (0 < getEnableKanrenCount()) {
		$('#kanrenDenpyouTableDiv').css('display', '');
	} else {
		$('#kanrenDenpyouTableDiv').css('display', 'none');
	}

	var kanrenDenpyouList = $("#kanrenDenpyouList");
	$.each(kanrenDenpyouList.find("tr.kanrenDenpyou"), function (ii, obj) {
		var index = 0;
		var kanren = $(obj);
		var kanrenDenpyouShubetsuUrlStr = kanren.find("input[name=hyoujiKanrenDenpyouShubetsuUrl]").val();
		var kanrenDenpyouUrlStr = kanren.find("input[name=hyoujiKanrenDenpyouUrl]").val();
		var kanrenDenpyouKbnStr = kanren.find("input[name=hyoujiKanrenDenpyouKbn]").val();
		var kanrenDenpyouIdStr = kanren.find("input[name=hyoujiKanrenDenpyouId]").val();
		var kanrenEmbedSpaceStr = kanren.find("input[name=hyoujiKanrenEmbedSpace]").val();

		if ('' == kanren.find("input[name=hyoujiKanrenDenpyouShubetsu]").val()) {
			// 未設定扱い
			return ;
		}

		// 伝票種別
		var kanrenDenpyouShubetsuStr = kanren.find("input[name=hyoujiKanrenDenpyouShubetsu]").val();
		if (kanrenDenpyouShubetsuStr == "") {
			kanrenDenpyouShubetsuStr = "　";
		}
		var shubetsuObj = kanren.find("td:eq(" + index++ + ")");
		shubetsuObj.children().remove();
		shubetsuObj.text('');
		shubetsuObj.append(kanrenDenpyouShubetsuStr);

		// 伝票ID
		var denpyouObj = kanren.find("td:eq(" + index++ + ")");
		var chouhyouObj = kanren.find("td:eq(" + index++ + ")");
		denpyouObj.children().remove();
		denpyouObj.text('');
		chouhyouObj.children().remove();
		chouhyouObj.text('');

		// 空白を消去
		var kanrenDenpyouIdStr = kanrenDenpyouIdStr.replace(/　/g,"");
		// 伝票リスト作成
		var kanrenDenpyouIdList = kanrenDenpyouIdStr.split("<br>");
		var kanrenDenpyouShubetsuUrlList = kanrenDenpyouShubetsuUrlStr.split(",");
		var kanrenDenpyouKbnList = kanrenDenpyouKbnStr.split(",");
		var kanrenDenpyouUrlList = kanrenDenpyouUrlStr.split(",");
		var kanrenEmbedSpaceList = kanrenEmbedSpaceStr.split(",");

		for (var i = 0; i < kanrenDenpyouIdList.length; i++) {

			if (!windowSizeChk()) {
				denpyouObj.append(kanrenEmbedSpaceList[i]);
			}
			// 関連伝票URL
			denpyouObj.append($("<a/>").text(kanrenDenpyouIdList[i]).attr("id", "link_" + kanrenDenpyouIdList[i]));
			denpyouObj.find("#link_" + kanrenDenpyouIdList[i]).attr('href', kanrenDenpyouUrlList[i]);
			denpyouObj.find("#link_" + kanrenDenpyouIdList[i]).attr('target', '_blank');

			//帳票出力
			chouhyouObj.append($("<a/>").text("帳票出力").attr('id', i).click(
					function() {
						var kanrenLocation = $(this).attr("id");
						var form = document.getElementById("PDFKanren");

						$("#PDFKanren").find("[name=denpyouKbn]").val(kanrenDenpyouKbnList[kanrenLocation]);
						$("#PDFKanren").find("[name=denpyouId]").val(kanrenDenpyouIdList[kanrenLocation]);
						$("#PDFKanren").find("[name=qrText]").val(kanrenDenpyouShubetsuUrlList[kanrenLocation]);

						switch ($("#PDFKanren").find("[name=denpyouKbn]").val()){
							case 'A001':
								form.action = 'keihi_tatekae_seisan_pdf';
								break;
							case 'A002':
								form.action = 'Karibarai_shinsei_pdf';
								break;
							case 'A003':
								form.action = 'seikyuusyo_barai_shinsei_pdf';
								break;
							case 'A004':
								form.action = 'ryohi_seisan_pdf';
								break;
							case 'A005':
								form.action = 'ryohi_karibarai_shinsei_pdf';
								break;
							case 'A006':
								form.action = 'tsuukin_teiki_shinsei_pdf';
								break;
							case 'A007':
								form.action = 'furikae_denpyou_pdf';
								break;
							case 'A008':
								form.action = 'sougou_tsukekae_denpyou_pdf';
								break;
							case 'A009':
								form.action = 'jidou_hikiotoshi_pdf';
								break;
							case 'A010':
								form.action = 'koutsuuhi_seisan_pdf';
								break;
							case 'A011':
								form.action = 'kaigai_ryohi_seisan_pdf';
								break;
							case 'A012':
								form.action = 'kaigai_ryohi_karibarai_shinsei_pdf';
								break;
<jsp:include page='<%= JspUtil.makeJspPath("eteam/gyoumu/workflow/DenpyouCommon_chouhyou.jsp") %>' />
							default:
								form.action = 'kani_todoke_pdf';
						}

						$(form).submit();
					}
				));

			denpyouObj.append("<br>");
			chouhyouObj.append("<br>");
		}

		// 添付ファイル
		var kanrenTenpuFileNameStr = kanren.find("input[name=hyoujiKanrenTenpuFileName]").val();
		var kanrenTenpuFileUrlStr = kanren.find("input[name=hyoujiKanrenTenpuFileUrl]").val();
		var kanrenTenpuFileNameList = kanrenTenpuFileNameStr.split(",");
		var kanrenTenpuFileUrlList = kanrenTenpuFileUrlStr.split(",");

		var tenpuObj = kanren.find("td:eq(" + index++ + ")");
		tenpuObj.children().remove();
		tenpuObj.text('');
		var count = 0;
		var tmpDenpyouId = kanrenDenpyouIdList[count];
		for (var i = 0; i < kanrenTenpuFileNameList.length - 1; ) {
			// 添付ファイルは対象伝票と同じ位置に表示されるように改行
			if (kanrenTenpuFileUrlList[i].indexOf(tmpDenpyouId) == -1) {
				tenpuObj.append("<br>");
				tmpDenpyouId = kanrenDenpyouIdList[++count];
				continue;
			}
			tenpuObj.append($("<a/>").text(kanrenTenpuFileNameList[i]).attr("id", "tenpu_" + i));
			tenpuObj.find("#tenpu_" + i).attr('href', kanrenTenpuFileUrlList[i]);
			tenpuObj.append(" ");
			i++;
		}
		//小型端末用
		if (tenpuObj.text() == "") {
			tenpuObj.text("　");
		}

		// 添付解除
<c:if test="${enableInput}">
		var kaijoObj = kanren.find("td:eq(" + index++ + ")");
		kaijoObj.children().remove();
		kaijoObj.text('');
		kaijoObj.append(
				$("<a/>").text("添付解除").click(function() {
					if(!confirm("添付を解除してよろしいですか？")){
						return false;
					}

					if ($("#kanrenDenpyouList .kanrenDenpyou").length > 1) {
						$(this).parents("tr.kanrenDenpyou").remove();
						displayKanrenDenpyou();
					} else {
						kanrenDenpyouClear($(this).parents("tr.kanrenDenpyou"));
						$('#kanrenDenpyouTableDiv').css('display', 'none');
					}

					isDirty = true;

					//稟議金額超過コメントの表示制御
					chekckKanrenDenpyouRingiHikitsugiUm();
					ringiKoumokuDisplaySeigyoMikihyou();
				})
			);
</c:if>

		//稟議金額引継ぎ
		var hikitsugiObj = kanren.find("td:eq(" + index++ + ")");
		hikitsugiObj.children().remove();
		hikitsugiObj.text('');
		hikitsugiObj.append("<input type='checkbox' id='hikitsugiSentaku' name='hikitsugiSentaku' onClick='hikitsugiSentakuCheck(" + ii + ")'>");
<c:if test="${not enableInput}">
		hikitsugiObj.find("input").prop('disabled', true);
</c:if>
		switch ($("#workflowForm").find("[name=denpyouKbn]").val()){
		case 'A001':
		case 'A004':
		case 'A011':
			var kanrenDenpyouId = kanren.find("input[name=kanrenDenpyouId]").val();
			var karibaraiDenpyouId = $("#shinseiSection").find("input[name=karibaraiDenpyouId]").val();
			if(kanrenDenpyouId == karibaraiDenpyouId){
				kanren.find("input[name=ringiKingakuHikitsugiFlg]").val("0");
			}
			break;
		default:
			break;
		}
		var ringiKingakuHikitsugiFlg = kanren.find("input[name=ringiKingakuHikitsugiFlg]").val();
		if(ringiKingakuHikitsugiFlg == "1"){
			hikitsugiObj.find("input").prop('checked', true);
		}
	});
}

/**
 * 稟議金額引継ぎチェック変更時Function
 */
function hikitsugiSentakuCheck(ii){
	var kanren = $("#kanrenDenpyouList tr.kanrenDenpyou:eq(" + ii + ")");
	var ringiKingakuHikitsugiFlg = kanren.find("input[name=ringiKingakuHikitsugiFlg]");
	if(kanren.find("input[name=hikitsugiSentaku]").prop('checked')){
		ringiKingakuHikitsugiFlg.val("1");
	}else{
		ringiKingakuHikitsugiFlg.val("0");
	}
	isDirty = true;

	//稟議金額超過コメントの表示制御
	chekckKanrenDenpyouRingiHikitsugiUm();
	ringiKoumokuDisplaySeigyoMikihyou();
}

/**
 * 稟議金額引継ぎ有無チェックFunction
 * 稟議金額引継ぎ有無が1つ以上チェックされている場合は引継ぎ有無フラグを1にする。
 */
function chekckKanrenDenpyouRingiHikitsugiUm(){
	var HikitsugiUmFlgDiv = $("#workflowForm").find("input[name=kanrenDenpyouRingiHikitsugiUmFlg]");
	//フラグが存在する（伝票状態=未起票）場合
	if(HikitsugiUmFlgDiv !== undefined){
		HikitsugiUmFlgDiv.val("");			//フラグを初期化
		$("#kanrenDenpyouList tr.kanrenDenpyou").each(function(){
			if("1" != HikitsugiUmFlgDiv.val()){
				if("1" == $(this).find("input[name=ringiKingakuHikitsugiFlg]").val()){
					HikitsugiUmFlgDiv.val("1");
				}
			}
		})
	}
}

/**
 * 起案番号採番ダイアログ表示Function
 * 'dialog'id内に起案番号採番画面をロードする。
 */
function commonKianbangouBoSaiban(kihyouBumonCd) {

	var obj =[];
	var denpyouId = $("#workflowForm").find("input[name=denpyouId]").val();

	var width = "600";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}

	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "420",
		title: "起案番号採番",
		buttons: {
			採番: function() {
				$(this).dialog().load(
					"kian_bangou_bo_saiban_saiban",
					$.map(
						/* チェックボックスがチェックされたもののみ送信 */
						$(dialog).find("input,select"),
						function(n, i) {
							if (!n.name) {
								return null;
							}
							//checkboxは本来のsubmit同様、チェックが無ければ送信しない。
							if (n.type == 'checkbox' && !$(n).prop('checked')) {
								return null;
							}
							return {
								name  : n.name,
								value : $(n).val()
							};
						},
						/* チェックされた要素を取得 */
						$(this).find("input:checked").each(
							function(){
								obj.push($(this).parents("tr"));
							}
						)
					),
					//callback
					function() {
						// 採番イベントで入力エラーがあればダイアログを表示したまま
						if ("0" != $(dialog).find("#errorCnt").val()) {
							return ;
						}

						// ダイアログで選択された情報を取得する。
						var sentakuBumonCd = $(dialog).find('input[name=sentakuBumonCd]').val();
						var sentakuNendo = $(dialog).find('input[name=sentakuNendo]').val();
						var sentakuRyakugou =$(dialog).find('input[name=sentakuRyakugou]').val();
						var sentakuKianbangouFrom =$(dialog).find('input[name=sentakuKianbangouFrom]').val();

						// 取得情報を画面項目に移送する。
						$('input[name=kianbangouBoDialogBumonCd]').val(sentakuBumonCd);
						$('input[name=kianbangouBoDialogNendo]').val(sentakuNendo);
						$('input[name=kianbangouBoDialogRyakugou]').val(sentakuRyakugou);
						$('input[name=kianbangouBoDialogKianbangouFrom]').val(sentakuKianbangouFrom);

						// 採番イベントが正常ならばダイアログを閉じてメイン画面に反映
						$(this).dialog("close");

						var params = {};
						var form = document.getElementById("workflowForm");
						let denpyouKbn = $("#workflowForm").find("[name=denpyouKbn]").val();
						let denpyouName = 
							(denpyouKbn == 'A001') ? 'keihi_tatekae_seisan' :
							(denpyouKbn == 'A002') ? 'karibarai' :
							(denpyouKbn == 'A003') ? 'seikyuusho_barai' :
							(denpyouKbn == 'A004') ? 'ryohiseisan' :
							(denpyouKbn == 'A005') ? 'ryohi_karibarai' :
							(denpyouKbn == 'A011') ? 'kaigai_ryohiseisan' :
							(denpyouKbn == 'A012') ? 'kaigai_ryohi_karibarai' :
							(denpyouKbn == 'A013') ? 'shiharai_irai' :
							'';
						switch (denpyouKbn){
						case 'A001':
						case 'A002':
						case 'A003':
						case 'A013':
							form.action = denpyouName + '_reset_is_yosan_check_taishougai';
							params = {
									denpyouId: $("#workflowForm").find("[name=denpyouId]").val(),
									kianbangouBoDialogNendo: sentakuNendo,
									kamokuCd: $("#workflowForm").find("[name=kamokuCd]").val(),
									}
							break;
						case 'A004':
						case 'A005':
						case 'A011':
						case 'A012':
							form.action = denpyouName + '_reset_is_yosan_check_taishougai';
							params = {
									denpyouId: $("#workflowForm").find("[name=denpyouId]").val(),
									kianbangouBoDialogNendo: sentakuNendo,
									kamokuCdRyohi: $("#workflowForm").find("[name=kamokuCdRyohi]").val(),
									kamokuCd: $("#workflowForm").find("[name=kamokuCd]").valArray(),
									}
							break;
						default:
							form.action = 'kani_todoke_reset_is_yosan_check_taishougai';
							params = {
								denpyouId:					$("#workflowForm").find("[name=denpyouId]").val(),
								denpyouKbn:					$("#workflowForm").find("[name=denpyouKbn]").val(),
								version:					$("#workflowForm").find("[name=version]").val(),
								kianbangouBoDialogNendo:	sentakuNendo,
								meisai01_val1:				$("#workflowForm").find("[name=meisai01_val1]").valArray(),
								meisai02_val1:				$("#workflowForm").find("[name=meisai02_val1]").valArray(),
								meisai03_val1:				$("#workflowForm").find("[name=meisai03_val1]").valArray(),
								meisai04_val1:				$("#workflowForm").find("[name=meisai04_val1]").valArray(),
								meisai05_val1:				$("#workflowForm").find("[name=meisai05_val1]").valArray(),
								meisai06_val1:				$("#workflowForm").find("[name=meisai06_val1]").valArray(),
								meisai07_val1:				$("#workflowForm").find("[name=meisai07_val1]").valArray(),
								meisai08_val1:				$("#workflowForm").find("[name=meisai08_val1]").valArray(),
								meisai09_val1:				$("#workflowForm").find("[name=meisai09_val1]").valArray(),
								meisai10_val1:				$("#workflowForm").find("[name=meisai10_val1]").valArray(),
								meisai11_val1:				$("#workflowForm").find("[name=meisai11_val1]").valArray(),
								meisai12_val1:				$("#workflowForm").find("[name=meisai12_val1]").valArray(),
								meisai13_val1:				$("#workflowForm").find("[name=meisai13_val1]").valArray(),
								meisai14_val1:				$("#workflowForm").find("[name=meisai14_val1]").valArray(),
								meisai15_val1:				$("#workflowForm").find("[name=meisai15_val1]").valArray(),
								meisai16_val1:				$("#workflowForm").find("[name=meisai16_val1]").valArray(),
								meisai17_val1:				$("#workflowForm").find("[name=meisai17_val1]").valArray(),
								meisai18_val1:				$("#workflowForm").find("[name=meisai18_val1]").valArray(),
								meisai19_val1:				$("#workflowForm").find("[name=meisai19_val1]").valArray(),
								meisai20_val1:				$("#workflowForm").find("[name=meisai20_val1]").valArray(),
								meisai21_val1:				$("#workflowForm").find("[name=meisai21_val1]").valArray(),
								meisai22_val1:				$("#workflowForm").find("[name=meisai22_val1]").valArray(),
								meisai23_val1:				$("#workflowForm").find("[name=meisai23_val1]").valArray(),
								meisai24_val1:				$("#workflowForm").find("[name=meisai24_val1]").valArray(),
								meisai25_val1:				$("#workflowForm").find("[name=meisai25_val1]").valArray(),
								meisai26_val1:				$("#workflowForm").find("[name=meisai26_val1]").valArray(),
								meisai27_val1:				$("#workflowForm").find("[name=meisai27_val1]").valArray(),
								meisai28_val1:				$("#workflowForm").find("[name=meisai28_val1]").valArray(),
								meisai29_val1:				$("#workflowForm").find("[name=meisai29_val1]").valArray(),
								meisai30_val1:				$("#workflowForm").find("[name=meisai30_val1]").valArray(),
								}
						}

						$.when(
							$.ajax({
								type : "POST",
								url : form.action,
								data : params,
								dataType : 'text',
								traditional : true,
						 		success : function(response) {
						 			$('input[name=kianbangouBoDialogYosanCheckTaishougaiFlg]').val(response);
						 		}
							})
						).done(function(data){
							// 申請ボタン押下の処理を継続する。
							$("#shinsei").click();
						});
					}
				);
			},
			閉じる: function() {$(this).dialog("close");}}
	})
	.load("kian_bangou_bo_saiban_init?kensakuBumonCd=" + kihyouBumonCd + "&denpyouId=" + denpyouId);
}

// 選択済み起案番号簿のクリア
function clearKianbangouBoSentaku(){
	// 起案番号簿の選択を解除
	$(dialog).find('input[name=sentakuBumonCd]').val("");
	$('input[name=kianYosanCheckFlg]').val("0");
}

// 起案番号詳細確認ダイアログ表示
function showKianbangouSyousaiKakunin(){
	var kianbangou = $("input[name=kianBangou]").val();
	if ("" === kianbangou){
		return false;
	}

	var width = "450";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}

	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "260",
		title: "起案番号詳細確認",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("kian_bangou_syousai_kakunin?denpyouKbn=${denpyouKbn}&denpyouId=${denpyouId}");
}

// 起案終了／起案終了解除ボタン押下処理
function kianShuuryouEventBtn(flg){
	var form = document.getElementById("workflowForm");
	$('input[name=kianShuuryouFlg]').val(flg);
	form.action = 'denpyou_kianshuuryou';
	$(form).submit();
}

/**
* 起案追加ダイアログ表示Function
* 'dialog'id内に起案追加画面をロードする。
*/
function commonKianTsuikaSentaku(kihyouBumonCd, denpyouKbn) {

	var obj =[];

	var width = "940";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}

	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "512",
		title: "起案追加",
		buttons: {
			追加: function() {
				$(this).dialog().load(
					"kian_tsuika_tsuika",
					$.map(
						/* チェックボックスがチェックされたもののみ送信 */
						$(dialog).find("input,select"),
						function(n, i) {
							if (!n.name) {
								return null;
							}
							//checkboxは本来のsubmit同様、チェックが無ければ送信しない。
							if (n.type == 'checkbox' && !$(n).prop('checked')) {
								return null;
							}
							return {
								name  : n.name,
								value : $(n).val()
							};
						},
						/* チェックされた要素を取得 */
						$(this).find("input:checked").each(
							function(){
								obj.push($(this).parents("tr"));
							}
						)
					),
					//callback
					function() {
						// 採番イベントで入力エラーがあればダイアログを表示したまま
						if ("0" != $(dialog).find("#errorCnt").val()) {
							return ;
						}

						// 追加イベントが正常ならばダイアログを閉じてメイン画面に反映
						$(this).dialog("close");

						for (var i = 0; i < obj.length; i++) {
							setDialogKianDenpyouInfo(obj[i]);
						}

						isDirty = true;

						meisaiAddExt(obj);
					}
				);
			},
			閉じる: function() {$(this).dialog("close");}}
	})
	.load("kian_tsuika?denpyouKbn=" + denpyouKbn + "&kensakuBumonCd=" + kihyouBumonCd);
}

/**
* 起案確認ダイアログ表示Function
* 'dialog'id内に起案追加画面をロードする。
*/
function commonKianKingakuCheck(mode){
	if(isDirty){
		alert("伝票内容が変更されています。\n｢更新｣を行ってから起案確認してください。");
		return;
	}
	if (isKaribaraiMishiyouCheckOn() == true) {
		alert("仮払金未使用のため起案確認は不要です。");
		return;
	}
	if (isShucchouChuushiCheckOn() == true){
		alert("出張中止のため起案確認は不要です。");
		return;
	}
	var buttons = {};
	var width = "940";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$.ajaxSetup({traditional : true});
<c:if test="${wfSeigyo.shinsei}">
	buttons["更新"] = function() {
		$(this).dialog().load(
				"kian_check_confirm",
				getDispInfo(0),
				//callback
				function() {
					// エラーがあればダイアログを表示したまま
					if ("0" != $(dialog).find("#errorCnt").val()) {
						return ;
					}
				}
			);
		};
</c:if>
	buttons["CSV出力"] = function() {
		$(this).dialog().load(
				"kian_check_csvoutputcheck",
				getDispInfo(2),
				//callback
				function() {
					// エラーがあればダイアログを表示したまま
					if ("0" != $(dialog).find("#errorCnt").val()) {
						return ;
					}

					// ダイアログの情報を画面項目に移送する。
					var formObj = $('#kianKingakuCheckForm');
					formObj.find("input[name=denpyouId]").val($("input[name=denpyouId]").val());
					formObj.find("input[name=denpyouKbn]").val($("input[name=denpyouKbn]").val());
					formObj.find("input[name=mode]").val("0");
					formObj.find("input[name=sentakuBumonCd]").val($(dialog).find('input[name=sentakuBumonCd]').val());
					formObj.find("input[name=sentakuNendo]").val($(dialog).find('input[name=sentakuNendo]').val());
					formObj.find("input[name=sentakuRyakugou]").val($(dialog).find('input[name=sentakuRyakugou]').val());
					formObj.find("input[name=sentakuKianbangouFrom]").val($(dialog).find('input[name=sentakuKianbangouFrom]').val());
					formObj.action = 'kian_check_csvoutput2';
					formObj.method = 'post';
					formObj.target = '_self';
					formObj.submit();
				}
			);
		};
<c:if test="${wfSeigyo.shinsei}">
	if("1" == mode){
		buttons["申請"] = function() {
			$(this).dialog().load(
					"kian_check_confirm",
					getDispInfo(1),
					//callback
					function() {
						// エラーがあればダイアログを表示したまま
						if ("0" != $(dialog).find("#errorCnt").val()) {
							return ;
						}
						// イベントが正常ならばダイアログを閉じてメイン画面に反映
						$(this).dialog("close");

						// 起案番号採番ダイアログで選択された情報を移送する。
						var sentakuBumonCd = $(dialog).find('input[name=sentakuBumonCd]').val();
						var sentakuNendo = $(dialog).find('input[name=sentakuNendo]').val();
						var sentakuRyakugou =$(dialog).find('input[name=sentakuRyakugou]').val();
						var sentakuKianbangouFrom =$(dialog).find('input[name=sentakuKianbangouFrom]').val();
						$('input[name=kianbangouBoDialogBumonCd]').val(sentakuBumonCd);
						$('input[name=kianbangouBoDialogNendo]').val(sentakuNendo);
						$('input[name=kianbangouBoDialogRyakugou]').val(sentakuRyakugou);
						$('input[name=kianbangouBoDialogKianbangouFrom]').val(sentakuKianbangouFrom);

						// 申請ボタン押下の処理を継続する。
						$('input[name=kianYosanCheckFlg]').val("1");
						$("#shinsei").click();
					}
			);
		};
	}
</c:if>
	buttons["閉じる"] = function() {$(this).dialog("close"); clearKianbangouBoSentaku();};

	// 起案番号簿の選択情報を画面に引き渡す（起案番号簿ダイアログを表示させないため）。
	var kianbangouBoParam = '';
<c:if test="${wfSeigyo.shinsei}">
	if("1" == mode){
		var sentakuBumonCd = $('input[name=kianbangouBoDialogBumonCd]').val();
		var sentakuNendo =  $('input[name=kianbangouBoDialogNendo]').val();
		var sentakuRyakugou = $('input[name=kianbangouBoDialogRyakugou]').val();
		var sentakuKianbangouFrom = $('input[name=kianbangouBoDialogKianbangouFrom]').val();
		kianbangouBoParam = '&sentakuBumonCd=' + sentakuBumonCd
						  + '&sentakuNendo=' + sentakuNendo
						  + '&sentakuRyakugou=' + encodeURI(sentakuRyakugou)
						  + '&sentakuKianbangouFrom=' + sentakuKianbangouFrom;
	}
</c:if>

	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "512",
		title: "起案金額チェック",
		buttons: buttons
	})
	.load("kian_check_init?denpyouKbn=" + $("#workflowForm").find("input[name=denpyouKbn]").val() + "&denpyouId=" + $("#workflowForm").find("input[name=denpyouId]").val() + "&mode=" + mode + kianbangouBoParam);
}

/**
* 予算確認ダイアログ表示Function
* 'dialog'id内に起案追加画面をロードする。
*/
function commonYosanCheck(mode){
	if(isDirty){
		alert("伝票内容が変更されています。\n｢更新｣を行ってから予算確認してください。");
		return;
	}
	if (isKaribaraiMishiyouCheckOn() == true) {
		alert("仮払金未使用のため予算確認は不要です。");
		return;
	}
	if (isShucchouChuushiCheckOn() == true){
		alert("出張中止のため予算確認は不要です。");
		return;
	}

	if("0" == mode){	// 予算確認の場合のみ。申請の場合は事前にチェック済み。
		if ($('input[name=yosanCheckTaishougaiFlg]').val() == 'true'){
			alert("チェック対象のデータが存在しません。")
			return;
		}
	}
	var buttons = {};
	var width = "1000";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$.ajaxSetup({traditional : true});
<c:if test="${wfSeigyo.shinsei}">
	buttons["更新"] = function() {
		$(this).dialog().load(
				"yosan_check_confirm",
				getDispInfo(0),
				//callback

				function() {
					// エラーがあればダイアログを表示したまま
					if ("0" != $(dialog).find("#errorCnt").val()) {
						return ;
					}
				}
			);
		};
</c:if>
	buttons["CSV出力"] = function() {
		$(this).dialog().load(
				"yosan_check_csvoutputcheck",
				getDispInfo(2),
				//callback
				function() {
					// エラーがあればダイアログを表示したまま
					if ("0" != $(dialog).find("#errorCnt").val()) {
						return ;
					}

					// ダイアログの情報を画面項目に移送する。
					var formObj = $('#yosanCheckForm');
					formObj.find("input[name=denpyouId]").val($("input[name=denpyouId]").val());
					formObj.find("input[name=denpyouKbn]").val($("input[name=denpyouKbn]").val());
					formObj.find("input[name=mode]").val("0");
					formObj.find("input[name=sentakuBumonCd]").val($(dialog).find('input[name=sentakuBumonCd]').val());
					formObj.find("input[name=sentakuNendo]").val($(dialog).find('input[name=sentakuNendo]').val());
					formObj.find("input[name=sentakuRyakugou]").val($(dialog).find('input[name=sentakuRyakugou]').val());
					formObj.find("input[name=sentakuKianbangouFrom]").val($(dialog).find('input[name=sentakuKianbangouFrom]').val());
					formObj.action = 'yosan_check_csvoutput';
					formObj.method = 'post';
					formObj.target = '_self';
					formObj.submit();
				}
			);
		};
<c:if test="${wfSeigyo.shinsei}">
	if("1" == mode){
		buttons["申請"] = function() {
			$(this).dialog().load(
					"yosan_check_confirm",
					getDispInfo(1),
					//callback
					function() {
						// エラーがあればダイアログを表示したまま
						if ("0" != $(dialog).find("#errorCnt").val()) {
							return ;
						}
						// イベントが正常ならばダイアログを閉じてメイン画面に反映
						$(this).dialog("close");

						// 起案番号採番ダイアログで選択された情報を移送する。
						var sentakuBumonCd = $(dialog).find('input[name=sentakuBumonCd]').val();
						var sentakuNendo = $(dialog).find('input[name=sentakuNendo]').val();
						var sentakuRyakugou =$(dialog).find('input[name=sentakuRyakugou]').val();
						var sentakuKianbangouFrom =$(dialog).find('input[name=sentakuKianbangouFrom]').val();
						$('input[name=kianbangouBoDialogBumonCd]').val(sentakuBumonCd);
						$('input[name=kianbangouBoDialogNendo]').val(sentakuNendo);
						$('input[name=kianbangouBoDialogRyakugou]').val(sentakuRyakugou);
						$('input[name=kianbangouBoDialogKianbangouFrom]').val(sentakuKianbangouFrom);

						// 申請ボタン押下の処理を継続する。
						$('input[name=kianYosanCheckFlg]').val("1");
						$("#shinsei").click();
					}
				);
			};
	}
</c:if>
	buttons["閉じる"] = function() {$(this).dialog("close"); clearKianbangouBoSentaku();};

	// 起案番号簿の選択情報を画面に引き渡す（起案番号簿ダイアログを表示させないため）。
	var kianbangouBoParam = '';
<c:if test="${wfSeigyo.shinsei}">
	if("1" == mode){
		var sentakuBumonCd = $('input[name=kianbangouBoDialogBumonCd]').val();
		var sentakuNendo =  $('input[name=kianbangouBoDialogNendo]').val();
		var sentakuRyakugou = $('input[name=kianbangouBoDialogRyakugou]').val();
		var sentakuKianbangouFrom = $('input[name=kianbangouBoDialogKianbangouFrom]').val();
		kianbangouBoParam = '&sentakuBumonCd=' + sentakuBumonCd
						  + '&sentakuNendo=' + sentakuNendo
						  + '&sentakuRyakugou=' + encodeURI(sentakuRyakugou)
						  + '&sentakuKianbangouFrom=' + sentakuKianbangouFrom;
	}
</c:if>

	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "512",
		title: "予算チェック",
		buttons: buttons
	})
	.load("yosan_check_init?denpyouKbn=" + $("#workflowForm").find("input[name=denpyouKbn]").val() + "&denpyouId=" + $("#workflowForm").find("input[name=denpyouId]").val() + "&mode=" + mode + kianbangouBoParam);
}

/**
 * ダイアログ起案伝票情報設定
 */
function setDialogKianDenpyouInfo(kian) {
	//イベント設定
	kianDenpyouAdd(kian);
	//入力補助
	commonInit(kian);
}

/**
 * 起案伝票行追加処理
 */
function kianDenpyouAdd(kianInfo) {
	var tr = null;
	if ($("#kianDenpyouTableDiv").css("display") != "none") {
		//１行目の関連伝票をコピーして内容を削除
		var _tr = $("#kianDenpyouList tr.kianDenpyou:first");
		tr = _tr.clone();
		// 行の挿入
		$("#kianDenpyouList").append(tr);
		// 初期化
		commonInit($(tr));
		_tr = _tr.next();
		tr = $("#kianDenpyouList tr.kianDenpyou:last");
	} else {
		// 空ならそこをそのまま使用する。
		tr = $("#kianDenpyouList tr.kianDenpyou:first");
	}

	// 行の初期化（関連伝票のものを使う）
	kanrenDenpyouClear(tr);

	// 値の設定
	setKianDenpyouInfo($("#kianDenpyouList tr.kianDenpyou").length - 1, kianInfo);

	// 再表示
	displayKianDenpyou();
	isDirty = true;
}

/**
 * 内容設定されている関連伝票の数を取得します。
 */
function getEnableKianCount() {
	var count = 0;
	$.each($("#kianDenpyouList input[name=kianDenpyouId]"), function(ii, obj) {
		if ('' != $(obj).val()) {
			count++;
		}
	});
	return count;
}

/**
 * 起案伝票情報設定<br>
 * 起案追加画面で選択された情報を伝票共通画面側に移送する。
 */
function setKianDenpyouInfo(ii, kianInfo) {
	var kian = $("#kianDenpyouList tr.kianDenpyou:eq(" + ii + ")");
	kian.find("input[name=hyoujiKianEmbedSpace]").val(kianInfo.find("input[name=hyoujiKianEmbedSpace]").val());
	kian.find("input[name=hyoujiKianDenpyouKbn]").val(kianInfo.find("input[name=hyoujiKianDenpyouKbn]").val());
	kian.find("input[name=hyoujiKianbangou]").val(kianInfo.find("input[name=hyoujiKianbangou]").val());
	kian.find("input[name=hyoujiKianKenmei]").val(kianInfo.find("input[name=hyoujiKianKenmei]").val());
	kian.find("input[name=hyoujiKianDenpyouShubetsuUrl]").val(kianInfo.find("input[name=hyoujiKianDenpyouShubetsuUrl]").val());
	kian.find("input[name=hyoujiKianDenpyouUrl]").val(kianInfo.find("input[name=hyoujiKianDenpyouUrl]").val());
	kian.find("input[name=hyoujiKianDenpyouShubetsu]").val(kianInfo.find("input[name=hyoujiKianDenpyouShubetsu]").val());
	kian.find("input[name=hyoujiKianDenpyouId]").val(kianInfo.find("input[name=hyoujiKianDenpyouId]").val());
	kian.find("input[name=hyoujiKianTenpuFileName]").val(kianInfo.find("input[name=hyoujiKianTenpuFileName]").val());
	kian.find("input[name=hyoujiKianTenpuFileUrl]").val(kianInfo.find("input[name=hyoujiKianTenpuFileUrl]").val());

	kian.find("input[name=kianDenpyouId]").val(kianInfo.find("input[name=kianDenpyouId]").val());
	kian.find("input[name=kianDenpyouKbn]").val(kianInfo.find("input[name=kianDenpyouKbn]").val());
}

/**
 * 内部項目(Hidden設定情報)から画面表示を行います。
 * 以下のタイミングでhyoujiKianEmbedSpaceのhiddenが変更され、当functionが呼ばれます。
 * ・伝票画面の表示(WorkflowEventControl、配列初期化)
 * ・起案伝票ダイアログの追加ボタン押下時(JS制御、配列への追加)
 * ・起案伝票解除時(JS制御、配列からの削除)
 */
function displayKianDenpyou() {

	if (0 < getEnableKianCount()) {
		$('#kianDenpyouTableDiv').css('display', '');
		// 起案追加ボタンを非活性に変更
		$("#kianAddButton").attr("disabled", true);

	} else {
		$('#kianDenpyouTableDiv').css('display', 'none');
		return;
	}

	var kianDenpyouList = $("#kianDenpyouList");
	$.each(kianDenpyouList.find("tr.kianDenpyou"), function (ii, obj) {
		var index = 0;
		var kian = $(obj);
		var kianDenpyouShubetsuUrlStr = kian.find("input[name=hyoujiKianDenpyouShubetsuUrl]").val();
		var kianDenpyouUrlStr = kian.find("input[name=hyoujiKianDenpyouUrl]").val();
		var kianDenpyouKbnStr = kian.find("input[name=hyoujiKianDenpyouKbn]").val();
		var kianDenpyouIdStr = kian.find("input[name=hyoujiKianDenpyouId]").val();
		var kianEmbedSpaceStr = kian.find("input[name=hyoujiKianEmbedSpace]").val();

		if ('' == kian.find("input[name=hyoujiKianDenpyouShubetsu]").val()) {
			// 未設定扱い
			return ;
		}

		// 伝票種別
		var kianDenpyouShubetsuStr = kian.find("input[name=hyoujiKianDenpyouShubetsu]").val();
		if (kianDenpyouShubetsuStr == "") {
			kianDenpyouShubetsuStr = "　";
		}
		var shubetsuObj = kian.find("td:eq(" + index++ + ")");
		shubetsuObj.children().remove();
		shubetsuObj.text('');
		shubetsuObj.append(kianDenpyouShubetsuStr);

		// 起案番号
		var kianbangouStr = kian.find("input[name=hyoujiKianbangou]").val();
		var kianbangouObj = kian.find("td:eq(" + index++ + ")");
		kianbangouObj.children().remove();
		kianbangouObj.text('');
		kianbangouObj.append(kianbangouStr);

		// 件名
		var kianKenmeiStr = kian.find("input[name=hyoujiKianKenmei]").val();
		var kenmeiObj = kian.find("td:eq(" + index++ + ")");
		kenmeiObj.children().remove();
		kenmeiObj.text('');
		kenmeiObj.append(kianKenmeiStr);


		// 伝票ID
		var denpyouObj = kian.find("td:eq(" + index++ + ")");
		var chouhyouObj = kian.find("td:eq(" + index++ + ")");
		denpyouObj.children().remove();
		denpyouObj.text('');
		chouhyouObj.children().remove();
		chouhyouObj.text('');

		// 空白を消去
		var kianDenpyouIdStr = kianDenpyouIdStr.replace(/　/g,"");
		// 伝票リスト作成
		var kianDenpyouIdList = kianDenpyouIdStr.split("<br>");
		var kianDenpyouShubetsuUrlList = kianDenpyouShubetsuUrlStr.split(",");
		var kianDenpyouKbnList = kianDenpyouKbnStr.split(",");
		var kianDenpyouUrlList = kianDenpyouUrlStr.split(",");
		var kianEmbedSpaceList = kianEmbedSpaceStr.split(",");

		for (var i = 0; i < kianDenpyouIdList.length; i++) {

			if (!windowSizeChk()) {
				denpyouObj.append(kianEmbedSpaceList[i]);
			}
			// 関連伝票URL
			denpyouObj.append($("<a/>").text(kianDenpyouIdList[i]).attr("id", "link_" + kianDenpyouIdList[i]));
			denpyouObj.find("#link_" + kianDenpyouIdList[i]).attr('href', kianDenpyouUrlList[i]);
			denpyouObj.find("#link_" + kianDenpyouIdList[i]).attr('target', '_blank');

			//帳票出力
			chouhyouObj.append($("<a/>").text("帳票出力").attr('id', i).click(
					function() {
						var kianLocation = $(this).attr("id");
						var form = document.getElementById("PDFKanren");

						$("#PDFKanren").find("[name=denpyouKbn]").val(kianDenpyouKbnList[kianLocation]);
						$("#PDFKanren").find("[name=denpyouId]").val(kianDenpyouIdList[kianLocation]);
						$("#PDFKanren").find("[name=qrText]").val(kianDenpyouShubetsuUrlList[kianLocation]);

						switch ($("#PDFKanren").find("[name=denpyouKbn]").val()){
							case 'A001':
								form.action = 'keihi_tatekae_seisan_pdf';
								break;
							case 'A002':
								form.action = 'Karibarai_shinsei_pdf';
								break;
							case 'A003':
								form.action = 'seikyuusyo_barai_shinsei_pdf';
								break;
							case 'A004':
								form.action = 'ryohi_seisan_pdf';
								break;
							case 'A005':
								form.action = 'ryohi_karibarai_shinsei_pdf';
								break;
							case 'A006':
								form.action = 'tsuukin_teiki_shinsei_pdf';
								break;
							case 'A007':
								form.action = 'furikae_denpyou_pdf';
								break;
							case 'A008':
								form.action = 'sougou_tsukekae_denpyou_pdf';
								break;
							case 'A009':
								form.action = 'jidou_hikiotoshi_pdf';
								break;
							case 'A010':
								form.action = 'koutsuuhi_seisan_pdf';
								break;
							case 'A011':
								form.action = 'kaigai_ryohi_seisan_pdf';
								break;
							case 'A012':
								form.action = 'kaigai_ryohi_karibarai_shinsei_pdf';
								break;
<jsp:include page='<%= JspUtil.makeJspPath("eteam/gyoumu/workflow/DenpyouCommon_chouhyou.jsp") %>' />
							default:
								form.action = 'kani_todoke_pdf';
						}

						$(form).submit();
					}
				));

			denpyouObj.append("<br>");
			chouhyouObj.append("<br>");
		}

		// 添付ファイル
		var kianTenpuFileNameStr = kian.find("input[name=hyoujiKianTenpuFileName]").val();
		var kianTenpuFileUrlStr = kian.find("input[name=hyoujiKianTenpuFileUrl]").val();
		var kianTenpuFileNameList = kianTenpuFileNameStr.split(",");
		var kianTenpuFileUrlList = kianTenpuFileUrlStr.split(",");

		var tenpuObj = kian.find("td:eq(" + index++ + ")");
		tenpuObj.children().remove();
		tenpuObj.text('');
		var count = 0;
		var tmpDenpyouId = kianDenpyouIdList[count];
		for (var i = 0; i < kianTenpuFileNameList.length - 1; ) {
			// 添付ファイルは対象伝票と同じ位置に表示されるように改行
			if (kianTenpuFileUrlList[i].indexOf(tmpDenpyouId) == -1) {
				tenpuObj.append("<br>");
				tmpDenpyouId = kianDenpyouIdList[++count];
				continue;
			}
			tenpuObj.append($("<a/>").text(kianTenpuFileNameList[i]).attr("id", "tenpu_" + i));
			tenpuObj.find("#tenpu_" + i).attr('href', kianTenpuFileUrlList[i]);
			tenpuObj.append(" ");
			i++;
		}
		//小型端末用
		if (tenpuObj.text() == "") {
			tenpuObj.text("　");
		}

		// 添付解除
		var kaijoObj = kian.find("td:eq(" + index++ + ")");
		kaijoObj.children().remove();
		kaijoObj.text('');
		kaijoObj.append(
				$("<a id='kianKaijoLink'/>").text("添付解除").click(function() {
					if(!confirm("添付を解除してよろしいですか？")){
						return false;
					}

					kianKaijo();

					isDirty = true;

				})
			);
	});
}

/**
 * 起案添付を解除
 */
function kianKaijo(){
	if($("#kiantenpuSection").is(":visible")){
		// 起案追加ボタンを活性に変更
		$("#kianAddButton").attr("disabled", false);

		// 起案番号簿選択ダイアログの選択値をクリアする。
		$('input[name=kianbangouBoDialogBumonCd]').val("");
		$('input[name=kianbangouBoDialogSerialNo]').val("");
		$('input[name=kianbangouBoDialogNendo]').val("");

		// 起案伝票セクションを更新する。
		if ($("#kianDenpyouList .kianDenpyou").length > 1) {
			$("#kianKaijoLink").parents("tr.kianDenpyou").remove();
			displaykianDenpyou();
		} else {
			kanrenDenpyouClear($("#kianKaijoLink").parents("tr.kianDenpyou"));
			$('#kianDenpyouTableDiv').css('display', 'none');
		}
	}
}

/**
 * 稟議金額の関連項目の表示制御
 */
function ringiKoumokuDisplaySeigyo(){
	//初期化
	$('#ringiKingakuZandakaWarning').css('display', 'none');
	$('#ringiKingakuZandakaImportant').css('display', 'none');
	$('#ringiKingakuCommentSection').css('display', 'none');

	var hyoujiFlg = $("#workflowForm").find("input[name=ringiKingakuChoukaCommentHyoujiFlg]").val();
	if(hyoujiFlg == '1'){
		$('#ringiKingakuCommentSection').css('display', '');
		var ringiKingakuZandaka = $("#workflowForm").find("input[name=hiddenRingiKingakuZandaka]").val();
		if(ringiKingakuZandaka !== undefined){
			if(ringiKingakuZandaka.replaceAll(",","") < 0){
				var choukaHantei = $("#workflowForm").find("input[name=ringiChoukaHantei]").val();
				var ringiKingaku = $("#workflowForm").find("input[name=hiddenRingiKingaku]").val();
				var choukaRate = (ringiKingaku.replaceAll(",","") - ringiKingakuZandaka.replaceAll(",","")) / ringiKingaku.replaceAll(",","") * 100
				if(choukaRate <= choukaHantei){
					$('#ringiKingakuZandakaWarning').css('display', '');
				}else{
					$('#ringiKingakuZandakaImportant').css('display', '');
				}
			}
		}
	}else{
		ringiKoumokuDisplaySeigyoMikihyou();
	}
}

/**
 * 稟議金額の関連項目の表示制御（未起票時）
 */
function ringiKoumokuDisplaySeigyoMikihyou(){
	//稟議金額を引継ぐ可能性がある場合は超過コメントを表示
	var kanrenDenpyouRingiHikitsugiUmFlg = $("#workflowForm").find("input[name=kanrenDenpyouRingiHikitsugiUmFlg]").val();
	var karibaraiRingiHikitsugiUmFlg = $("#workflowForm").find("input[name=karibaraiRingiHikitsugiUmFlg]").val();
	if(kanrenDenpyouRingiHikitsugiUmFlg !== undefined){
		if('1' == kanrenDenpyouRingiHikitsugiUmFlg || '1' == karibaraiRingiHikitsugiUmFlg){
			$('#ringiKingakuCommentSection').css('display', '');
		}else{
			//コメントが表示（入力あり）→非表示になる場合を考慮し、コメントを初期化
			$('#ringiKingakuCommentSection').find("textarea").val("");
			$('#ringiKingakuCommentSection').css('display', 'none');
		}
	}
}

/**
 * 仮払金未使用がチェックされているかどうか
 */
function isKaribaraiMishiyouCheckOn(){
	if(document.getElementById("karibaraiMishiyouFlg") != null && $("#karibaraiMishiyouFlg").prop("checked")){
		return true;
	}else{
		return false;
	}
}

/**
 * 出張中止がチェックされているかどうか
 */
function isShucchouChuushiCheckOn(){
	if(document.getElementById("shucchouChuushiFlg") != null && $("#shucchouChuushiFlg").prop("checked")){
		return true;
	}else{
		return false;
	}
}

/*
 * 支出依頼に区分されうる伝票種別はtrueを返却
 */
function isShishutsuIrai(){
	switch ($("#workflowForm").find("input[name=denpyouKbn]").val()) {
	case 'A001':  return true;		// 経費立替精算
	case 'A002':  return false;		// 仮払申請
	case 'A003':  return true;		// 請求書払い申請
	case 'A004':  return true;		// 旅費精算
	case 'A005':  return false;		// 旅費仮払
	case 'A011':  return true;		// 海外旅費精算
	case 'A012':  return false;		// 海外旅費仮払い
	case 'A013':  return true;		// 支払依頼申請
	default:      return false;
	}
}

/** 拡張用 */
function eventCancelConfirm(eventCode) { return false; }
function meisaiAddExt(){};

//画面表示後の初期化
$(document).ready(function(){

	// 添付ファイルのアクション紐付け
	$("body").on("click","button[name=tenpuFileAdd]",tenpuFileAdd);
	$("body").on("click","button[name=tenpuFileDelete]",tenpuFileDelete);
	$("body").on("change","input[name=denshitorihikiCheckbox]",changeDenshitorihiki);
	$("body").on("change","input[name=tsfuyoCheckbox]",changeTsfuyo);
	$("body").on("change","input[name=ebunshoCheckbox]",setEbunshoDeletable);
	$("body").on("change","input[name=uploadFile]",resetEbunshoNaibuData);

	//e文書処理用内部情報更新
	resetEbunshoNaibuData();

	// e文書明細のアクション紐づけ
	$("body").on("click","button[name=ebunshoMeisaiAdd]",ebunshoMeisaiAdd);
	$("body").on("click","button[name=ebunshoMeisaiDelete]",ebunshoMeisaiDelete);
	$("body").on("change","input[name=ebunsho_nengappi]",setEbunshoDeletable);
	$("body").on("change","input[name=ebunsho_kingaku]",setEbunshoDeletable);
	$("body").on("change","input[name=ebunsho_hinmei]",setEbunshoDeletable);
	$("body").on("change","input[name=ebunsho_hakkousha]",setEbunshoDeletable);
	$("body").on("change","select[name=ebunsho_shubetsu]",changeEbunshoMeisai);
	ebunshoMeisaiInit($("#tenpuList"));

	// 添付済みe文書明細部のアクション紐づけ
	$("body").on("click","button[name=tenpuzumi_ebunshoMeisaiAdd]",tenpuzumiEbunshoMeisaiAdd);
	$("body").on("click","button[name=tenpuzumi_ebunshoMeisaiDelete]",tenpuzumiEbunshoMeisaiDelete);
	$("body").on("change","input[name=tenpuzumi_tsfuyo_chk]",resetEbunshoNaibuData);
	$("body").on("change","input[name=tenpuzumi_ebunsho_nengappi]",checkTenpuzumiEbunshoMeisaiDelete);
	$("body").on("change","input[name=tenpuzumi_ebunsho_kingaku]",checkTenpuzumiEbunshoMeisaiDelete);
	$("body").on("change","input[name=tenpuzumi_ebunsho_hinmei]",checkTenpuzumiEbunshoMeisaiDelete);
	$("body").on("change","input[name=tenpuzumi_ebunsho_hakkousha]",checkTenpuzumiEbunshoMeisaiDelete);
	$("body").on("change","select[name=tenpuzumi_ebunsho_shubetsu]",changeTenpuzumiEbunshoMeisai);

	//添付済みファイルのチェックボックスも紐づける
	$("body").on("change","input[name=tenpuzumi_ebunshoCheckbox]",setTenpuzumiEbunshoDeletable);
	$("body").on("change","input[name=tenpuzumi_denshitorihiki_chk]",changeTenpuzumiDenshitorihiki);
	$("body").on("change","input[name=tenpuzumi_tsfuyo_chk]",changeTenpuzumiTsfuyo);
	tenpuzumiebunshoMeisaiInit($("#tenpuzumiList"));
	
	//インボイス対応伝票表示制御
	$("body").on("change","select[name=invoiceDenpyou]",changeInvoiceDenpyou);

	$.each($("select[name=ebunsho_shubetsu]"),changeEbunshoMeisai);
	$.each($("input[name=ebunshoCheckbox]"), function() {
		if ( !($(this).prop("checked")) ) {
			var span = $(this).parent().parent().find("span.ebunshoMeisaiRecord");
			span.find("input[name=ebunsho_nengappi]").val("");
			span.find("input[name=ebunsho_kingaku]").val("");
			span.find("input[name=ebunsho_hinmei]").val("");
			span.find("input[name=ebunsho_hakkousha]").val("");
		}
	});
<c:if test='${fn:length(errorList) eq 0}'>
	//新規表示時のe文書チェックボックスのオンオフ設定
	if( ($("input[name=ebunshoTaishouCheckDefault]").val() == "1") && (1 == $("#tenpuList div.newfileDiv").length) ){
		$("input[name=ebunshoCheckbox]").prop("checked",true);
	}
</c:if>

	$.each($("input[name=ebunshoCheckbox]"),setEbunshoDeletable);
	$("button[name=addTimestamp]").click(addTimestampEventBtn);

	$.each($("input[name=tenpuzumi_ebunshoCheckbox]"),setTenpuzumiEbunshoDeletable);

	//添付ファイル10個以上なら添付ファイル追加を無効にしておく
	if(10 < $("#tenpuList div.newfileDiv").length + $("#tenpuzumiList a[href ^='denpyou_file_download']").length){
		$("input[name='uploadFile']").prop("disabled", true);
		$("button[name='tenpuFileAdd']").prop("disabled", true);
		$("button[name='tenpuFileDelete']").prop("disabled", true);
		$("input[name='ebunshoCheckbox']").prop("disabled", true);
	}
	
	//インボイス 入力方式の初期表示および変更可否
	var denpyouKbn = $("#workflowForm").find("input[name=denpyouKbn]").val();
	var nyuryokuDefaultFlg = $("#workflowForm").find("input[name=nyuryokuDefaultflg]").val();
	var nyuryokuHenkouFlg = $("#workflowForm").find("input[name=nyuryokuHenkouflg]").val();
	if(denpyouKbn == 'A003' ||denpyouKbn == 'A009' || denpyouKbn == 'A013'){
		$("#kaikeiContent").find("select[name=nyuryokuHoushiki]").prop("disabled", '0' == nyuryokuHenkouFlg);
		let nyuuryokuHoushikiVal = $("#kaikeiContent").find("select[name=nyuryokuHoushiki]").val();
		$("#kaikeiContent").find("select[name=nyuryokuHoushiki]").val(nyuuryokuHoushikiVal ?? nyuryokuDefaultFlg);
	}

	// 起票部門セレクターの初期化
	kihyouBumonSelectInit($('#kihyouBumonSelect'));
	//QRコードの設定
	$("#qr").attr("src", "qr_code_sakusei?qrText=" + encodeURIComponent(location.href) + "&time=" + new Date().getTime());
	//親画面のリロード：承認したのが承認待ち一覧から消える、等の為
	try {
		var parentTitle = window.opener.document.title;
		if ("伝票一覧｜<%=EteamSymbol.SYSTEM_NAME%>" == parentTitle) {
			window.opener.location.reload();
		}
	}catch(e){}

	// 明細表示をロード
	displayKianDenpyou();
	displayKanrenDenpyou();

	// 起案追加ボタン押下時、ダイアログ表示
	$("#kianAddButton").click(function(){
		commonKianTsuikaSentaku($("input[name=kihyouBumonCd]").val(), $("input[name=denpyouKbn]").val());
	});

	//関連伝票選択ボタン押下時、ダイアログ表示
	$("#denpyouAddButton").click(function(){
		commonKanrenDenpyouSentaku();
	});

	/** ワークフローボタン押下時Function */
	$('button[name=workflowBtn]').click(function(event) {
		var eventCode = $(this).attr("data-code");
		var eventName = $(this).attr("data-name");
		var kianYosanCheckFlg = $('input[name=kianYosanCheckFlg]').val();

		// 起案番号採番ダイアログの表示要否を取得する。
		var dispKianbangouBoDialog = $('input[name=isDispKianbangouBoDialog]').val();
		// 起案番号採番ダイアログで選択したデータの部門コードを取得する。
		var sentakuBumonCd = $(dialog).find('input[name=sentakuBumonCd]').val();

		// 申請ボタン押下でかつ、起案番号採番ダイアログを表示する必要があり、
		// まだ未選択の場合に起案番号採番ダイアログを表示する。
		if('shinsei' == eventCode && "1" === dispKianbangouBoDialog && (undefined == sentakuBumonCd || "" == sentakuBumonCd) && "1" != kianYosanCheckFlg){
			commonKianbangouBoSaiban($("input[name=kihyouBumonCd]").val());
			// ここで処理を止めないと後続が進んでしまう。
			return false;
		}

		// 拡張用に追加
		// パッケージではデットロジック
		if(eventCancelConfirm(eventCode)){ return false; }

		if('shinsei' == eventCode){
<c:if test="${wfSeigyo.kianCheck}">
			// 起案金額チェックの対象伝票でかつ、申請時の起案金額チェック未実施の場合
			if ("1" != kianYosanCheckFlg){
				if (isKaribaraiMishiyouCheckOn() == false && isShucchouChuushiCheckOn() == false) {
					commonKianKingakuCheck(1);
	 				return false;
				}
			}
</c:if>
			var confirmMessege = "チェック対象のデータが存在しません。\nこのまま申請しますか？";
<c:if test="${wfSeigyo.yosanCheck}">
			// 予算金額チェックの対象伝票でかつ、申請時の予算チェック未実施の場合
			if ("1" != kianYosanCheckFlg){
				if (isKaribaraiMishiyouCheckOn() == false && isShucchouChuushiCheckOn() == false) {
					if(false == isShishutsuIrai() && 'true' == $('input[name=kianbangouBoDialogYosanCheckTaishougaiFlg]').val()){
						if(!confirm(confirmMessege)){
							clearKianbangouBoSentaku();
							return false;
						}
					}else if(isShishutsuIrai() && 'true' == $('input[name=yosanCheckTaishougaiFlg]').val()){
						if(!confirm(confirmMessege)){
							return false;
						}
					}else{
						commonYosanCheck(1);
						return false;
					}
				}
			}
</c:if>
		}

		// 伝票個別処理があれば行う
		if(!kobetsuDenpyouProc(eventCode)) {
			// 起案番号簿の選択を解除
			clearKianbangouBoSentaku();
			return false;
		}

		// 承認の場合、画面が変更されていればメッセージを出す。
		if('shounin' == eventCode && isDirty){
			if(!confirm("伝票内容が変更されています。\n変更を破棄して${su:htmlEscape(wfSeigyo.shouninMongon)}しますか？\n変更を反映する場合はキャンセルし、｢更新｣してください。")){
				// 起案番号簿の選択を解除
				clearKianbangouBoSentaku();
				return false;
			}
		}

		/** ボタン色変更 */
		var btnclass;
		if (eventCode == "torisage" || eventCode == "sashimodoshi" || eventCode == "hinin" || eventCode == "torimodoshi" || eventCode == "shouninkaijo"){
			btnclass = "btn-danger";
		}else{
			btnclass = "btn-primary";
		}
		
		/*202207 承認解除時のコメントウィンドウはココで*/
		if (eventCode == "shouninkaijo") {
			$("#workflowModal").empty();
			$("#workflowModal").append("<div class='modal-header'><button type='button' name='dialogclosebtn' class='close' data-dismiss='modal' aria-hidden='true' onClick='clearKianbangouBoSentaku();'>×<\/button><h3 id='myModalLabel'>" + eventName +"<\/h3><\/div><div class='modal-body'><p>コメント（必須）<\/p><textarea class='input-block-level' name='comment' placeholder='必ずコメントを入力してください。(400字)'><\/textarea><\/div><div class='modal-footer'><button class='btn' name='dialogclosebtn' data-dismiss='modal' aria-hidden='true' onClick='clearKianbangouBoSentaku();'>閉じる<\/button><button type='button' name='fordenpyouhenkoukengen' id='chiharaibiCheckNotPass' class='btn " + btnclass + "' onClick='workflowEventBtn(\"" + eventCode + "\")'>" + eventName + "<\/button><\/div>");
		}
		/*202207 条件に「承認解除以外」を追加*/
		if (event != "sashimodoshi" && eventCode != "shouninkaijo") {
			$("#workflowModal").empty();
			$("#workflowModal").append("<div class='modal-header'><button type='button' name='dialogclosebtn' class='close' data-dismiss='modal' aria-hidden='true' onClick='clearKianbangouBoSentaku();'>×<\/button><h3 id='myModalLabel'>" + eventName +"<\/h3><\/div><div class='modal-body'><p>コメント（任意）<\/p><textarea class='input-block-level' name='comment' placeholder='任意でコメントを入力してください。(400字)'><\/textarea><\/div><div class='modal-footer'><button class='btn' name='dialogclosebtn' data-dismiss='modal' aria-hidden='true' onClick='clearKianbangouBoSentaku();'>閉じる<\/button><button type='button' name='fordenpyouhenkoukengen' id='chiharaibiCheckNotPass' class='btn " + btnclass + "' onClick='workflowEventBtn(\"" + eventCode + "\")'>" + eventName + "<\/button><\/div>");
		}
	});

	/** マル秘設定ボタン押下時Function */
	$('button[name=maruhiSetteiBtn]').click(function() {
		if(isDirty){
			if(!confirm("伝票内容が変更されています。\n変更を破棄しますか？\n変更を反映する場合はキャンセルし、｢更新｣してください。")){
				return false;
			}
		}
	});

	/** マル秘解除ボタン押下時Function */
	$('button[name=maruhiKaijyoBtn]').click(function() {
		if(isDirty){
			if(!confirm("伝票内容が変更されています。\n変更を破棄しますか？\n変更を反映する場合はキャンセルし、｢更新｣してください。")){
				return false;
			}
		}
	});

	/*
	 * 帳票ダウンロードボタン
	 */
	$("#PDFBtn").click(function(){
		if(isDirty){
			alert("伝票内容が変更されています。\n｢更新｣を行ってから帳票作成してください。");
			return;
		}
		$("#pdfOutputForm").find("input[name=qrText]").val(location.href);

		var sokyuu = $("#pdfOutputForm").find("input[name=sokyuuFlg]");
		var denpyouKbn = $("#pdfOutputForm").find("input[name=denpyouKbn]").val();
		var form = document.getElementById("pdfOutputForm");

		switch ($("#pdfOutputForm").find("input[name=denpyouKbn]").val()){
			case 'A001':
				form.action = 'keihi_tatekae_seisan_pdf';
				break;
			case 'A002':
				form.action = 'Karibarai_shinsei_pdf';
				break;
			case 'A003':
				form.action = 'seikyuusyo_barai_shinsei_pdf';
				break;
			case 'A004':
				form.action = 'ryohi_seisan_pdf';
				break;
			case 'A005':
				form.action = 'ryohi_karibarai_shinsei_pdf';
				break;
			case 'A006':
				form.action = 'tsuukin_teiki_shinsei_pdf';
				break;
			case 'A007':
				form.action = 'furikae_denpyou_pdf';
				break;
			case 'A008':
				form.action = 'sougou_tsukekae_denpyou_pdf';
				break;
			case 'A009':
				form.action = 'jidou_hikiotoshi_pdf';
				break;
			case 'A010':
				form.action = 'koutsuuhi_seisan_pdf';
				break;
			case 'A011':
				form.action = 'kaigai_ryohi_seisan_pdf';
				break;
			case 'A012':
				form.action = 'kaigai_ryohi_karibarai_shinsei_pdf';
				break;
			case 'A013':
				form.action = 'shiharai_irai_pdf';
				break;
	<jsp:include page='<%= JspUtil.makeJspPath("eteam/gyoumu/workflow/DenpyouCommon_chouhyou.jsp") %>' />
			default:
				form.action = 'kani_todoke_pdf';
		}

<c:choose>
<c:when test='${sokyuuSettei eq 1}'>
		if (denpyouKbn.match(/B/)) {
			var buttons = {};
			var width = "400";
			if(windowSizeChk()) {
				width = $(window).width() * 0.9;
			}
			$.ajaxSetup({traditional : true});
			buttons["はい"] = function() {
				sokyuu.val(true);
				$(this).dialog("close");
				$(form).submit();
				};
			buttons["いいえ"] = function() {
				sokyuu.val(false);
				$(this).dialog("close");
				$(form).submit();
				};
			buttons["キャンセル"] = function() {
				$(this).dialog("close");
				};
			$("<div>遡及日適用印刷をしますか？</div>")
			.dialog({
				modal: true,
				width: width,
				height: "150",
				title:"遡及日印刷確認",
				buttons: buttons
			}).dialog('open');
		} else {
			$(form).submit();
		}
</c:when>
<c:otherwise>
		$(form).submit();
</c:otherwise>
</c:choose>

	});

	//div.control-group配下で項目全て非表示ならラベルもろとも非表示
	$("#kaikeiContent").find("div.control-group").each(function(){
		if ($(this).find("input:not(:hidden),select:not(:hidden),textarea:not(:hidden),button:not(:hidden),table:not(:hidden),p:not(:hidden)").length == 0) {
			$(this).css("display", "none");
		}
	});

	//div.control-group配下でメインラベルが非表示でサブラベルが表示だと段差ができてしまうので解消
	$("#kaikeiContent").find("div.control-group").each(function(){
		var mainLabel = $(this).find("label.control-label");
		var subLabel = $(this).find("div.controls label.label");
		if (
			(mainLabel.length == 0 || ! $(mainLabel[0]).is(':visible')) &&
			(subLabel.length > 0 && $(subLabel[0]).is(':visible'))
		) {
			var labelOrg = $(subLabel[0]);
			labelOrg.hide();
			var label = labelOrg.clone().removeClass("label").addClass("control-label").clone();
			label.insertBefore($(this).find("div.controls"));
			label.show();
		}
	});

	// リクエスト内にprintFlg=TRUEがあればPDF出力アクション
	if("TRUE" == $("#workflowForm").find("input[name=printFlg]").val()) {
		$('#PDFBtn').trigger("click");
	}

	/*
	 * 起案番号欄の表示制御
	 */

	// 伝票の状態が未起票ではない場合に起案番号欄を表示設定にする。
	 var joutai = '${denpyouJoutai}';
	if ("00" != joutai){
		// 起案番号項目を表示
		$('#kianBangouColumn').css("display", "inline");
	}

	// 稟議金額関係の項目の表示制御
	ringiKoumokuDisplaySeigyo();
	
	//会社設定の設定値によるインボイスフラグの変更可否制御
	invoiceSetteiSeigyo();
	//インボイスフラグによる項目表示制御
	changeInvoiceDenpyou();

<c:if test='${not enableInput}'>
	//起票モードの場合のみ入力や選択可能
	$("#ringiKingakuCommentSection").find("textarea").prop("disabled", true);
	$("#denpyouJouhou").find("select").prop("disabled", true);
	$("#kaikeiContent").find("select[name=nyuryokuHoushiki]").prop("disabled", true);
</c:if>

	//WEB印刷ボタンが非表示の場合
<c:if test='${not wfSeigyo.webInsatsu}'>
	$('#printBtn').remove();
	$('#PDFBtn').css('right', '10px');
	$('#headerButtonArea').show();
</c:if>

	//操作履歴欄の表示制御
<c:choose>
<c:when test='${wfSeigyo.sousaRirekiView}'>
	$('#sousaRirekiButton').hide();
</c:when>
<c:otherwise>
	$('#sousaRireki').hide();
	$("#sousaRirekiButton").click(function(){
		if($('#sousaRireki').is(':hidden')){
			$('#sousaRireki').show();
			$("#sousaRirekiButton").text("操作履歴欄非表示")
		}else{
			$('#sousaRireki').hide();
			$("#sousaRirekiButton").text("操作履歴欄表示")
		}
	});
</c:otherwise>
</c:choose>

	$("#closeButton").tooltip();
	
	//202208 最終承認者編集不可のdisableはScriptでやりたい
	if(${wfSeigyo.denpyouHenkouKengen}){
		var item = document.getElementsByTagName("input");
		for(var i=0; i<item.length; i++){
			item[i].disabled = true;
		}
		
		var select = document.getElementsByTagName("select");
		for(var i=0; i<select.length; i++){
			select[i].disabled = true;
		}
		
		var btn = document.getElementsByTagName("button");
		for(var i=0; i<btn.length; i++){
			btn[i].disabled = true;
		}
		$("input[name=hikiotoshibi]").prop("disabled",false);
		$("button[name=workflowBtn]").prop("disabled",false);
		$("button[name=memobutton]").prop("disabled",false);
		$("button[name=dialogclosebtn]").prop("disabled",false);
		$("input[name=sashimodoshiSakiEdano]").prop("disabled",false);
		$("button[name=fordenpyouhenkoukengen]").prop("disabled",false);
		$("input[name=masrefFlg]").prop("disabled",false);
		$("button[name=headerBtn]").prop("disabled",false);
		// 添付ファイルプレビューのボタンまで不可にされていたので追加
		$("#btnPreviousFile").prop("disabled", false);
		$("#btnNextFile").prop("disabled", false);
		//操作履歴表示
		$("#sousaRirekiButton").prop("disabled", false);
		//予算確認
		$("#yosanCheck").prop("disabled", false);
		//起案確認
		$("#kianKingakuCheck").prop("disabled", false);
		//disable除外分は、書き方をjQueryに統一
		$("#closeButton").prop("disabled", false);
		$("#PDFBtn").prop("disabled", false);
		$("#printBtn").prop("disabled", false);
		$("#koushinBtn").prop("disabled", false);
		
		//支払日・計上日はenable
		//更新・差戻し・承認・否認・閉じるはinputではなくbutton
		
		if($("input[name=denpyouKbn]").val() != "A013" && (!($('#masrefFlg').length) || $("input[name=masrefFlg]").prop("checked") == false)){
			$("input[name=shiharaibi]").prop("disabled",false);
			$("input[name=shiharaiBi]").prop("disabled",false);
		}
		//$("input[name=shiharaibi]").prop("disabled",false);
		
		if(${wfSeigyo.enableDenpyouHenkouKengenKeijou}){
			$("input[name=keijoubi]").prop("disabled",false);
			$("select[name=keijoubi]").prop("disabled",false);
		}
		
		var shiharaiShubetsu = $("select[name=shiharaiShubetsu]").val();
		//種別=定期　→ 一見先以外　予定日入力不可
		if (shiharaiShubetsu == "1") {
			$("[name=yoteibi]").prop("disabled", true);
		//種別=その他 or 未入力　→ 一見先　支払予定日入力可
		} else {
			$("[name=yoteibi]").prop("disabled", false);
		}
	}
	//「消費税額詳細」ボタンは常に表示
	$("#zeigakuShousaiButton").show();
	$("#zeigakuShousaiButton").prop("disabled", false);
});

/*
 * 画面が表示されたタイミングで実施する処理(readyより遅い)
 */
$(window).load(function(){

	// 支出依頼の新規起票の場合、起案番号を引き継ぐか確認するダイアログを表示する。
	var joutai = '${denpyouJoutai}';
	if ("00" == joutai){
		if ("1" == $('input[name=kianHimodukeFlg]').val()){
			if ("1" == $('input[name=kianHimodukeDialogFlg]').val()){
				var errorCnt = '${fn:length(errorList)}';
				if (0 == errorCnt){
					// エラーメッセージなしの画面表示でダイアログを表示する。
					if(confirm("起案と紐付けますか？")){
						// 起案番号項目を表示
						$('#kianBangouColumn').css("display", "inline");
						$('input[name=isDispKianbangou]').val("2");
						$('input[name=kianBangou]').val("");
						// 起案伝票セクションを表示
						$('#kiantenpuSection').css("display", "block");
						$('input[name=isDispKiantenpuSection]').val("1");
					}else{
						// 起案番号を引き継がないに設定
						$('input[name=kianHimodukeFlg]').val("0");
					}
					// 紐付けダイアログを一度表示したら以降表示させない。
					$('input[name=kianHimodukeDialogFlg]').val("0");
				}
			}
		}else{
			// 起案番号簿選択ダイアログ表示する場合は起案番号項目を表示
			if ("1" == $('input[name=isDispKianbangouBoDialog]').val()){
				$('#kianBangouColumn').css("display", "inline");
			}
		}
	}

<c:if test="${isDispYosanCheckSection eq '1'}">
	// 予算チェックセクションを非表示
	if($('input[name=isDispKiantenpuSection]').val() == "1"){
		$('#yosanCheckSection').remove();
	}
</c:if>
});

// 添付ファイルのプレビュー表示：前へ
function previewPreviousFile(current, maxIndex)
{
	previewFile(current, -1, maxIndex);
}

//添付ファイルのプレビュー表示：次へ
function previewNextFile(current, maxIndex)
{
	previewFile(current, 1, maxIndex);
}

// プレビュー先変更の共通処理(internal)
function previewFile(current, prevOrNext, maxIndex)
{
	var newIndex = current + prevOrNext;
	if(newIndex < 0 || newIndex > maxIndex)
	{
		return;
	}
	
	var newTenpuFileName = document.getElementsByName("tenpuFileName")[newIndex].value;
	var newTenpuFileExtension = document.getElementsByName("tenpuFileExtension")[newIndex].value.toLowerCase();
	var newTenpuFileDisplayName = document.getElementsByName("tenpuFileDisplayName")[newIndex].value;
	
	var newLink = location.protocol + "//" + location.host + "/eteam/static/tmp/" + newTenpuFileName + "." + newTenpuFileExtension;
	// PDF.jsの採用により、PDFだけリンク形式を変更
	var newPDFLink = location.protocol + "//" + location.host + "/eteam/static/js/pdfjs-dist/web/viewer.html?file=" + location.protocol + "//" + location.host + "/eteam/static/tmp/" + newTenpuFileName + "." + newTenpuFileExtension + "#pagemode=none";
	var pdfPreview = document.getElementById("pdfPreview");
	var imagePreview = document.getElementById("imagePreview");
	var previewError = document.getElementById("previewError");
	var previewErrorLink = document.getElementById("previewErrorLink");
	
	// リンク更新
	pdfPreview.src = newPDFLink;
	imagePreview.src = newLink;
	previewErrorLink.href = newLink;
	
	// display更新
	pdfPreview.style.display = newTenpuFileExtension == "pdf" ? "" : "none";
	imagePreview.style.display = ["bmp", "jpg", "jpeg", "png"].includes(newTenpuFileExtension) ? "" : "none";
	previewError.style.display = ["bmp", "jpg", "jpeg", "png", "pdf"].includes(newTenpuFileExtension) ? "none" : "";
	
	// ボタン処理更新
	document.getElementById("btnPreviousFile").setAttribute("onclick", "previewPreviousFile(" + newIndex + "," + maxIndex + ")");
	document.getElementById("btnNextFile").setAttribute("onclick", "previewNextFile(" + newIndex + "," + maxIndex + ")");
	
	// 表記更新
	document.getElementById("previewPageCount").textContent = "(" + (newIndex + 1) + " / " + (maxIndex + 1) + ") " + newTenpuFileDisplayName;
}

//インボイスフラグの変更時処理
//インボイス対応前の場合、インボイス対応項目を非表示とする
function changeInvoiceDenpyou(){
	let isInvoiceStarted = $("#workflowForm").find("input[name=invoiceStartflg]").val() == "1";
	var hyoujiFlg = $("#denpyouJouhou").find("select[name=invoiceDenpyou]").val();
	var denpyouKbn = $("#workflowForm").find("[name=denpyouKbn]").val();
	var displayStyle = 'hide';
	let isInvoiceDenpyouKbn = !["A002", "A005", "A012"].includes(denpyouKbn) && !denpyouKbn.includes("B");
	var jigyoushaKbnHenkouflg = $("#workflowForm").find("input[name=jigyoushaKbnHenkouflg]").val();
	
	if(!isInvoiceStarted || !isInvoiceDenpyouKbn){
		$("select[name=invoiceDenpyou]")[displayStyle]();
		$("#denpyouJouhou").find("select[name=invoiceDenpyou]").val("1");
	}else if('0' == hyoujiFlg) {
		displayStyle = 'show';
	}
	// インボイス開始後のインボイス対応区分で、「対応前」に変更されたとき
	else
	{
		$("input[name*=igyoushaKbn][type=hidden]").val("0"); // 隠れ項目の更新（文字の大小があってもいいように2文字目からの部分一致）
		$("input[name*=igyoushaKbn][type=radio][value=0]").prop("checked", true); // radioの更新。eq(0)だと一つ目しか変更されないのですべて変更させるためにvalueで指定。
		$("#workflowForm").find("input[name=jigyoushaKbnHenkouflg]").val(jigyoushaKbnHenkouflg); //0で上書きされてしまう為値を取得しなおす

		// もしかすると支払先の対応も必要かも

		try{
			calcMoney(); // calcMoneyがある伝票の時は、一度再計算させておく。
		}catch(ex)
		{
		}
		
		//一先ず旅費系のみ
		// 旅費系の場合、旅費明細リスト更新
		if(["A004", "A010", "A011"].includes(denpyouKbn))
		{
			setDisplayRyohiMeisaiData(1);
			setDisplayRyohiMeisaiData(2);
		}

		// 経費明細リストの共通更新処理（旅費以外の使用区分も追加すること）
		if(["A001", "A003", "A004", "A009", "A011"].includes(denpyouKbn))
		{
			calcMeisaiMoney();
			setMeisaiTableLayout(); // 経費明細のレイアウト更新
			setDisplayMeisaiData();
		}

		//通勤定期申請 再計算のみ（表示の切り替えは不要）
		if(denpyouKbn == "A006"){
			calcKingaku();
		}
		if(denpyouKbn == "A008"){
			$("select[name*=igyoushaKbn]").val("0"); // 隠れ項目の更新（文字の大小があってもいいように2文字目からの部分一致）
			calcMoney();
		}

		// TODO 支払依頼明細リストの更新処理
		if(denpyouKbn == "A013")
		{
			let jigyoushaFlg = $("input[name=jigyoushaKbn]:checked").val() > 0 ? $("input[name=jigyoushaKbn]:checked").val() : "0";
			let hasuuShoriFlg = $("#workflowForm").find("input[name=hasuuShoriFlg]").val();
			let shiireKeikaSothiFlg = $("#workflowForm").find("input[name=shiirezeigakuKeikasothi]").val();
			$("#meisaiList tr.meisai").each(function() {
				calcMeisaiZeigaku(jigyoushaFlg, hasuuShoriFlg, shiireKeikaSothiFlg, $(this));
			});
			setDisplayMeisaiData();
		}

		// TODO 必要であれば他の金額更新処理
	}
	
	$("#kaikeiContent").find("label[for=nyuryokuHoushiki]")[displayStyle]();
	$("#kaikeiContent").find("select[name=nyuryokuHoushiki]")[displayStyle]();
	$("#kaikeiContent").find("[name=kingakuGoukei10Percent]")[displayStyle]();
	$("#kaikeiContent").find("[name=kingakuGoukei8Percent]")[displayStyle]();
	$(".invoiceOnly")[displayStyle]();
}

//インボイスフラグ 会社設定での変更可否の制御（変更不可の場合はインボイス対応固定）
function invoiceSetteiSeigyo(){
	var joutai = '${denpyouJoutai}';
	var hyoujiFlg = $("#workflowForm").find("input[name=invoiceSetteiflg]").val();
	$("#denpyouJouhou").find("select[name=invoiceDenpyou]").prop("disabled", hyoujiFlg == "0");
	//未起票の場合のみ「インボイス伝票」固定（既に旧伝票で起票済の伝票は旧伝票のまま）
	if ("00" == joutai && $("select[name=invoiceDenpyou]").val() != "1"){
		$("#denpyouJouhou").find("select[name=invoiceDenpyou]").val("0");
	}
}

// インボイス対応フラグの影響する箇所がキリがないので、海外旅費の海外明細のケースを除き、何か変わったら常に更新（荒業）
$(document).on('change', 'input, select, textarea', function() {
	if(($("#dialogMainDenpyouMeisai")?.find("input[name=kaigaiFlgRyohi]") ?? $("#dialogMainDenpyouMeisai")?.find("input[name=kaigaiFlg]:checked"))?.val() != "1") {
	    changeInvoiceDenpyou();
	}
});
$(document).on('click', 'button', function() {
	if(($("#dialogMainDenpyouMeisai")?.find("input[name=kaigaiFlgRyohi]") ?? $("#dialogMainDenpyouMeisai")?.find("input[name=kaigaiFlg]:checked"))?.val() != "1") {
	    changeInvoiceDenpyou();
	}
});
</script>
<jsp:include page='<%= JspUtil.makeJspPath("eteam/include/Custmize.jsp") %>' />
</body>
</html>