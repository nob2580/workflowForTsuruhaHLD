<%@page import="eteam.common.JspUtil"%>
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
		<title>メニュー｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
	  			<h1>メニュー</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id="main">
<c:if test="${fn:length(infoList) > 0}" >
					<div>
					<h2>インフォメーション</h2>
						<div class='thumbnail' style='background-color: #ffffe0; word-wrap:break-word; word-break:break-all;'>
<c:forEach var="infoRecord" items="${infoList}">
								<ul>
									<li><p><a href='information_sanshou?informationId=${su:htmlEscape(infoRecord.informationId)}'>${su:htmlEscapeBr(infoRecord.infoNaiyou)}</a></p></li>
								</ul>
</c:forEach>
						</div>
					</div>
</c:if>
					<!-- Example row of columns -->
					<div class='row'>
						<div class='span8'>
							<div class='row'>
								<div class='span4' id='oneRow'>
									<div id ='shinkiKihyou'>
										<h2>新規起票</h2>
										<div class='thumbnail'>
											<ul>
<c:if test="${shinkiIchiranHyoujiFlg}" ><li><a href='shinki_kihyou'>一覧から起票</a></li></c:if>
<c:if test="${guidanceKihyouHyoujiFlg}" ><li><a href='guidance_kihyou'>ガイダンス起票</a></li></c:if>
<c:if test="${seikyuushoBaraiCSVUploadHyoujiFlg}" ><li><a href='seikyuushobarai_csv_upload'>請求書払い申請一括登録</a></li></c:if>
<c:if test="${shiharaiIraiCSVUploadHyoujiFlg}" ><li><a href='shiharaiirai_csv_upload'>支払依頼申請一括登録</a></li></c:if>
<c:if test="${keihiTatekaeSeisanDairikihyouFlg}" ><li><a href='keihi_tatekae_seisan?denpyouKbn=A001&dairiFlg=1' target='_blank'>経費立替精算代理起票</a></li></c:if>
<c:if test="${ryohiSeisanDairikihyouFlg }" ><li><a href='ryohi_seisan?denpyouKbn=A004&dairiFlg=1' target='_blank'>出張旅費精算代理起票</a></li></c:if>
<c:if test="${ryohiKaribaraiSeisanDairikihyouFlg}" ><li><a href='ryohi_karibarai_shinsei?denpyouKbn=A005&dairiFlg=1' target='_blank'>出張伺い申請代理起票</a></li></c:if>
<c:if test="${kaigaiRyohiSeisanDairikihyouFlg }" ><li><a href='kaigai_ryohi_seisan?denpyouKbn=A011&dairiFlg=1' target='_blank'>海外出張旅費精算代理起票</a></li></c:if>
<c:if test="${kaigaiRyohiKaribaraiSeisanDairikihyouFlg}" ><li><a href='kaigai_ryohi_karibarai_shinsei?denpyouKbn=A012&dairiFlg=1' target='_blank'>海外出張伺い申請代理起票</a></li></c:if>
<jsp:include page='<%= JspUtil.makeJspPath("eteam/gyoumu/menu/Menu_shinkiKihyou.jsp") %>' />
											</ul>
										</div>
									</div>
									<div id ='daikou'>
										<h2>代行</h2>
										<div class='thumbnail'>
											<ul>
<c:if test="${hidaikousyaSentakuHyoujiFlg}" ><li><a href='hi_daikousha_sentaku'>被代行者選択</a></li></c:if>
											</ul>
										</div>
									</div>
								</div>
								<div class='span4' id='twoRow'>
									<div id ='denpyouIchiran'>
										<h2>伝票一覧</h2>
										<div class='thumbnail'>
											<ul>
<c:if test="${kihyoucyuuIchiranHyoujiFlg}" ><li><a href='denpyou_ichiran_kensaku?kanrenDenpyou=010'>起票中の伝票（${su:htmlEscape(kihyoucyuCount)}件）</a></li></c:if>
<c:if test="${shinseichuuIchiranHyoujiFlg}" ><li><a href='denpyou_ichiran_kensaku?kanrenDenpyou=120'>申請中の伝票（${su:htmlEscape(shinseichuuCount)}件）</a></li></c:if>
<c:if test="${shouninmachiIchiranHyoujiFlg}" ><li><a href='denpyou_ichiran_kensaku?kanrenDenpyou=020'>承認待ちの伝票（${su:htmlEscape(shouninmachiCount)}件）</a></li></c:if>
<c:if test="${denpyouKensakuHyoujiFlg}" ><li><a href='denpyou_ichiran_kensaku'>伝票検索</a></li></c:if>
<c:if test="${shikyuuKingakuIchiranHyoujiFlg}" ><li><a href='shikyuu_kingaku_ichiran'>社員別支給金額一覧</a></li></c:if>
<c:if test="${keihiMeisaihyoHyoujiFlg}" ><li><a href='keihi_data_meisai_init'>経費明細一覧</a></li></c:if>
<c:if test="${shikkouJoukyouHyoujiFlg}" ><li><a href='shikkou_joukyou_init'>執行状況一覧（みなし実績）</a></li></c:if>
<c:if test="${shiharaiIraishoShutsuryokuFlg}" ><li><a href='shiharai_iraisho_shutsuryoku'>支払依頼書出力</a></li></c:if>
											</ul>
										</div>
									</div>
									<div id ='tsuuchi'>
										<h2>通知</h2>
										<div class='thumbnail'>
											<ul>
<c:if test="${tsuuchiIchiranHyoujiFlg}" ><li><a href='tsuuchi_ichiran'>通知一覧（未確認${su:htmlEscape(tsuuchiIchiranCount)}件）</a></li></c:if>
<c:if test="${HoujinCardSeisanHimodukeHyoujiFlg}" ><li><a href='houjin_card_seisan_himoduke'>法人カード精算紐付（未精算${su:htmlEscape(houjinCardMiseisanCount)}件）</a></li></c:if>
											</ul>
										</div>
									</div>
									
								</div>
								
							</div>
							<c:if test="${okiniiriKihyouFlg}">
								<h2>お気に入り起票</h2>
									<div class='no-more-tables'>
										<table class='table-bordered table-condensed'>
											<thead>
												<tr>
													<th style="min-width:100px">業務種別</th>
													<th style="min-width:100px">伝票種別</th>
													<th><nobr>内容</nobr></th>
												</tr>
											</thead>
<c:forEach var="record" items="${okiniiriList}">
												<tbody>
													<tr>
														<td>${su:htmlEscape(record.gyoumuShubetsu)}</td>
														<td>
															<c:choose>
																<c:when test='${record.version >= 1}'>
																	<a href='${su:htmlEscape(record.denpyouShubetsuUrl)}?denpyouKbn=${su:htmlEscape(record.denpyouKbn)}&version=${su:htmlEscape(record.version)}' target='_blank'>${su:htmlEscape(record.denpyouShubetsu)}</a>
																</c:when>
																<c:otherwise>
																	<a href='${su:htmlEscape(record.denpyouShubetsuUrl)}?denpyouKbn=${su:htmlEscape(record.denpyouKbn)}' target='_blank'>${su:htmlEscape(record.denpyouShubetsu)}</a>
																</c:otherwise>
															</c:choose>
														</td>
														<td style='max-width:750px; word-wrap:break-word; word-break:break-all;'>${su:htmlEscapeBrLink(record.memo)}</td>
													</tr>
												</tbody>
</c:forEach>
										</table>
									</div>
							</c:if>
						</div>
						
						<div class='span4'>
							<div id ='kojinSettei'>
								<h2>個人設定</h2>
								<div class='thumbnail'>
									<ul>
<c:if test="${daikousyaTourokuHyoujiFlg}" ><li><a href='daikousha_shitei'>代行者登録</a></li></c:if>
<c:if test="${mailTuuchiHyoujiFlg}" ><li><a href='mail_tsuuchi_settei'>メール通知設定</a></li></c:if>
<c:if test="${okiniiriMntHyoujiFlg}" ><li><a href='okiniiri_hensyu'>お気に入りメンテナンス</a></li></c:if>
<c:if test="${userJouhouHenkouHyoujiFlg}" ><li><a href='user_jouhou?userId=${su:htmlEscape(sessionScope.user.tourokuOrKoushinUserId)}' target='_blank'>ユーザー情報変更</a></li></c:if>
									</ul>
								</div>
							</div>
							<div id ='kaisyaSettei'>
							<h2>会社設定</h2>
								<div class='thumbnail'>
									<ul>
<c:if test="${bumonKanriHyoujiFlg}" ><li><a href='bumon_ichiran'>部門管理</a></li></c:if>
<c:if test="${yakuwariKanriHyoujiFlg}" ><li><a href='bumon_role_ichiran'>役割管理</a></li></c:if>
<c:if test="${gyoumuRoleKanriHyoujiFlg}" ><li><a href='gyoumu_role_ichiran'>業務ロール管理</a></li></c:if>
<c:if test="${userKanriHyoujiFlg}" ><li><a href='user_kensaku'>ユーザー管理</a></li></c:if>
<c:if test="${daikousyaTourokuKanriHyoujiFlg}" ><li><a href='daikousha_shitei'>代行者登録</a></li></c:if>
<c:if test="${bumonShuisyouRouteHyoujiFlg}" ><li><a href='bumon_suishou_route_ichiran'>部門推奨ルート登録</a></li></c:if>
<c:if test="${gougiBushoHyoujiFlg}" ><li><a href='gougi_busho_ichiran'>合議部署登録</a></li></c:if>
<c:if test="${shouninShoriHyoujiFlg}" ><li><a href='shounin_shori_kengen'>承認処理権限</a></li></c:if>
<c:if test="${saisyuuShouninsyaHyoujiFlg}" ><li><a href='chuuki_mongon_settei_ichiran'>最終承認者・注記文言設定</a></li></c:if>
<c:if test="${masterDataHyoujiFlg}" ><li><a href='master_data_ichiran'>マスターデータ管理</a></li></c:if>
<c:if test="${kianbangouKanriHyoujiFlg}" ><li><a href='kianbangou_bo_ichiran'>起案番号管理</a></li></c:if>
<c:if test="${denpyouKanriHyoujiFlg}" ><li><a href='denpyou_kanri'>伝票管理</a></li></c:if>
<c:if test="${guidanceKihyouMntHyoujiFlg}" ><li><a href='guidance_maintenance'>ガイダンス起票メンテナンス</a></li></c:if>
<c:if test="${kanniTodokeSakuseiHyoujiFlg}" ><li><a href='kani_todoke_ichiran'>届出ジェネレータ</a></li></c:if>
<c:if test="${teikiJouhouIchiranHyoujiFlg}" ><li><a href='teiki_jouhou_ichiran'>定期情報一覧</a></li></c:if>
<c:if test="${torihikiTourokuHyoujiFlg}" ><li><a href='torihiki_ichiran'>取引（仕訳）登録</a></li></c:if>
<c:if test="${bumonTorihikiTourokuHyoujiFlg}" ><li><a href='bumonbetsu_torihiki_ichiran'>部門別取引（仕訳）登録</a></li></c:if>
<c:if test="${infoTourokuHyoujiFlg}" ><li><a href='information_ichiran'>インフォメーション管理</a></li></c:if>
<c:if test="${systemKanriHyoujiFlg}" ><li><a href='system_kanri'>会社設定</a></li></c:if>
<c:if test="${invoiceSeidoKaishiSetteiHyoujiFlg}" ><li><a href='invoice_seido_kaishi_settei'>インボイス制度開始設定</a></li></c:if>
<c:if test="${gamenKoumokuKanriHyoujiFlg}" ><li><a href='gamen_koumoku_kanri'>画面項目設定</a></li></c:if>
<c:if test="${denpyouKoumokuKyoutsuSetteiHyoujiFlg}" ><li><a href='denpyou_koumoku_kyoutsu_settei'>伝票一覧全ユーザー共通表示項目設定</a></li></c:if>
<c:if test="${bumonIkkatsuTourokuHyoujiFlg}" ><li><a href='bumon_ikkatsu_touroku'>部門一括登録</a></li></c:if>
<c:if test="${userIkkatsuTourokuHyoujiFlg}" ><li><a href='user_ikkatsu_touroku'>ユーザー一括登録</a></li></c:if>
<c:if test="${bumonSuishouRouteIkkatsuTourokuHyoujiFlg}" ><li><a href='bumon_suishou_route_ikkatsu_touroku'>部門推奨ルート一括登録</a></li></c:if>
<c:if test="${torihikiShiwakeIkkatsuTourokuHyoujiFlg}" ><li><a href='torihiki_ikkatsu_touroku'>取引（仕訳）一括登録</a></li></c:if>
<c:if test="${bumonbetsuTorihikiShiwakeIkkatsuTourokuHyoujiFlg}" ><li><a href='bumonbetsu_torihiki_ikkatsu_touroku'>部門別取引（仕訳）一括登録</a></li></c:if>
									</ul>
								</div>
							</div>
							<div id ='keiriShori'>
								<h2>経理処理</h2>
								<div class='thumbnail'>
									<ul>
<c:if test="${nittyuuDataRenkeiHyoujiFlg}" ><li><a href='nittyuu_data_renkei'>データ連携</a></li></c:if>
<c:if test="${FBDataSaiSakuseiHyoujiFlg}" ><li><a href='fbdata_saisakusei'>FBデータ作成</a></li></c:if>
<c:if test="${csvDownloadHyoujiFlg}" ><li><a href='csv_download'>振込明細ダウンロード</a></li></c:if>
<c:if test="${SeikyuushoShimeHyoujiFlg}" ><li><a href='seikyuusho_shime?denpyouKbn=A003'>請求書払い申請締</a></li></c:if>
<c:if test="${ShiharaiShimeHyoujiFlg}" ><li><a href='seikyuusho_shime?denpyouKbn=A013'>支払依頼申請締</a></li></c:if>
<c:if test="${JidouhikiotoshiShimeHyoujiFlg}" ><li><a href='seikyuusho_shime?denpyouKbn=A009'>自動引落伝票締</a></li></c:if>
<c:if test="${KeihitatekaeShimeHyoujiFlg}" ><li><a href='seikyuusho_shime?denpyouKbn=A001'>経費立替精算締</a></li></c:if>
<c:if test="${RyohiShimeHyoujiFlg}" ><li><a href='seikyuusho_shime?denpyouKbn=A004'>出張旅費精算締</a></li></c:if>
<c:if test="${KaigaiRyohiShimeHyoujiFlg}" ><li><a href='seikyuusho_shime?denpyouKbn=A011'>海外出張旅費精算締</a></li></c:if>
<c:if test="${KoutsuuhiShimeHyoujiFlg}" ><li><a href='seikyuusho_shime?denpyouKbn=A010'>交通費精算締</a></li></c:if>
<c:if test="${keihiMeisaiDataKoushinHyoujiFlg}" ><li><a href='keihi_data_meisai_koushin'>経費明細データ更新</a></li></c:if>
<c:if test="${houjinCardRiyouHyoujiFlg}" ><li><a href='houjin_card_riyou_meisai'>法人カード利用明細</a></li></c:if>
<c:if test="${houjinCardCSVUploadHyoujiFlg}" ><li><a href='houjin_card_import'>法人カードデータインポート</a></li></c:if>
<c:if test="${shiharaiIraiOutput}" ><li><a href='shiharaiirai_output'>支払依頼データ作成</a></li></c:if>
<c:if test="${shiharaiYoteiSoukatsuhyoOutput}" ><li><a href='shiharai_yotei_soukatsuhyo_output'>支払予定総括表出力</a></li></c:if>
<c:if test="${yosanShikkouShoriNengetsuSettei}" ><li><a href='yosan_shikkou_shori'>予算執行処理年月設定</a></li></c:if>
<jsp:include page='<%= JspUtil.makeJspPath("eteam/gyoumu/menu/Menu_keiri.jsp") %>' />
									</ul>
								</div>
							</div>
							<div id ='systemSettei'>
								<h2>運用監視</h2>
								<div class='thumbnail'>
									<ul>
<c:if test="${dataAccessHyoujiFlg}" ><li><a href='data_access_ichiran'>データアクセス</a></li></c:if>
<c:if test="${batchRenkeiHyoujiFlg}" ><li><a href='batch_renkei_kekka_kakunin'>バッチ処理結果確認</a></li></c:if>
<c:if test="${backupUnyouHyoujiFlg}"><li><a href='backup_unyou'>バックアップ運用</a></li></c:if>
									</ul>
								</div>
							</div>
							<div id ='tool'>
								<h2>ツール</h2>
								<div class='thumbnail'>
									<ul>
<c:if test="${smartSeisanDownloadFlg}" ><li><a href='smart_seisan_download'>ICカード連携ツール ダウンロード</a></li></c:if>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div><!-- main -->
			</div><!-- container -->
			<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッターー -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<!-- スクリプト -->
		<script style='text/javascript'>
<!-- 
/**
* 中項目表示非表示Function
*/
function menuHyouji(koumoku) {
	var hyouji = koumoku.find("a").size();
	// 各画面(aタグ)がひとつも見つからなかったら非表示
	if(hyouji === 0) {
		koumoku.hide();
	}
}

/**
* 初期表示処理
*/
$(document).ready(function(){
	
	// メニューカテゴリーの表示制御
	menuHyouji($("#shinkiKihyou"));
	menuHyouji($("#daikou"));
	menuHyouji($("#denpyouIchiran"));
	menuHyouji($("#tsuuchi"));
	menuHyouji($("#kojinSettei"));
	menuHyouji($("#kaisyaSettei"));
	menuHyouji($("#keiriShori"));
	menuHyouji($("#systemSettei"));
	menuHyouji($("#tool"));
	
	menuHyouji($("#oneRow"));
	menuHyouji($("#twoRow"));
});


// -->
		</script>
	</body>
</html>
