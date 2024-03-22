<%@ page import="eteam.symbol.EteamSymbol"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<script style='text/javascript'>
// 旅費精算のスクリプト・共通部分
$(document).ready(function() {

	//使用者選択
	$("#userSentakuRyohiButton").click(function(e){
		var title = $(this).parent().parent().find(".control-label").text();
		var shiyoushaStr = '${su:htmlEscape(ks.userName.name)}';
		dialogRetuserId   = $(this).closest("div").find("input[name=userIdRyohi]");
		dialogRetusername = $(this).closest("div").find("input[name=userNameRyohi]");
		dialogRetshainNo  = $(this).closest("div").find("input[name=shainNoRyohi]");
		dialogRetTableArea = $("#ryohiMeisai");
		dialogRetKaribaraiArea = $("div#karibaraiAnken");
		dialogCallbackShainNoLostFocus = function(){
			changeShiyousha(dialogRetuserId,dialogRetusername,dialogRetshainNo,title,shiyoushaStr);
		};
		commonUserSentaku();
	});

	// 社員番号変更時Function
	 $("input[name=shainNoRyohi]").change(function shaiNoLostFocus() {

		// 仮払選択をクリア
		karibaraiDispClear();

		var title = $(this).parent().parent().find(".control-label").text();
		var shiyoushaStr = '${su:htmlEscape(ks.userName.name)}';
		dialogRetuserId   = $(this).closest("div").find("input[name=userIdRyohi]");
		dialogRetusername = $(this).closest("div").find("input[name=userNameRyohi]");
		dialogRetshainNo  = $(this).closest("div").find("input[name=shainNoRyohi]");
		dialogRetTableArea = $("#ryohiMeisai");
		dialogCallbackShainNoLostFocus = function(){
			changeShiyousha(dialogRetuserId,dialogRetusername,dialogRetshainNo,title,shiyoushaStr);
		};
		commonShainNoLostFocus(dialogRetshainNo, dialogRetusername, title, dialogRetuserId, false);
	});

	//取引選択
	$("#torihikiSentakuButton").click(function(e){
		ryohiTorihikiSentaku();
		displayZeiritsuRyohi();
	});

	//仕訳枝番号変更時Function
	$("input[name=shiwakeEdaNoRyohi]").change(function(){
		ryohiShiwakeEdaNoLostFocus();
	});

	//勘定科目枝番選択ボタン押下時Function
	$("#kamokuEdabanSentakuButton").click(function(e){
		dialogRetKamokuEdabanCd = $("input[name=kamokuEdabanCdRyohi]");
		dialogRetKamokuEdabanName = $("input[name=kamokuEdabanNameRyohi]");
		dialogRetKazeiKbn = $("select[name=kazeiKbnRyohi]");
		dialogRetBunriKbn = $("select[name=bunriKbn]");
		dialogRetKariShiireKbn = $("select[name=kariShiireKbn]");
		dialogCallbackKanjyouKamokuEdabanSentaku = function(){
			displayZeiritsuRyohi();
			$("input[name=futanBumonCdRyohi]").blur();
		};
		commonKamokuEdabanSentaku($("[name=kamokuCdRyohi]").val(), "A004", $("input[name=shiwakeEdaNoRyohi]").val());
	});

	//勘定科目枝番コードロストフォーカス時Function
	$("input[name=kamokuEdabanCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var kamokuCd = 	$("input[name=kamokuCdRyohi]").val();
		dialogRetKamokuEdabanCd = $("input[name=kamokuEdabanCdRyohi]");
		dialogRetKamokuEdabanName = $("input[name=kamokuEdabanNameRyohi]");
		dialogRetKazeiKbn = $("select[name=kazeiKbnRyohi]");
		dialogRetBunriKbn = $("select[name=bunriKbn]");
		dialogRetKariShiireKbn = $("select[name=kariShiireKbn]");
		dialogCallbackKanjyouKamokuEdabanSentaku = function(){
			displayZeiritsuRyohi();
			$("input[name=futanBumonCdRyohi]").blur();
		};
		commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamokuCd, title, $("select[name=kazeiKbnRyohi]"), $("select[name=bunriKbn]"), "A004", $("input[name=shiwakeEdaNoRyohi]").val());
	});

	//負担部門選択ボタン押下時Function
	$("#futanBumonSentakuButton").click(function(e){
		dialogRetFutanBumonCd = $("input[name=futanBumonCdRyohi]");
		dialogRetFutanBumonName = $("input[name=futanBumonNameRyohi]");
		dialogRetKazeiKbn = $("select[name=kazeiKbnRyohi]");
		dialogRetBunriKbn = $("select[name=bunriKbn]");
		dialogRetKariShiireKbn = $("select[name=kariShiireKbn]");
		dialogCallbackFutanBumonSentaku = function(){
			displayZeiritsuRyohi();
		};
		commonFutanBumonSentaku("1",$("input[name=kamokuCdRyohi]").val(),$("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val(), "A004", $("input[name=shiwakeEdaNoRyohi]").val());
	});

	//負担部門コードロストフォーカス時Function
	$("input[name=futanBumonCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetFutanBumonCd = $("input[name=futanBumonCdRyohi]");
		dialogRetFutanBumonName = $("input[name=futanBumonNameRyohi]");
		dialogRetKazeiKbn = $("select[name=kazeiKbnRyohi]");
		dialogRetBunriKbn = $("select[name=bunriKbn]");
		dialogRetKariShiireKbn = $("select[name=kariShiireKbn]");
		dialogCallbackFutanBumonSentaku = function(){
			displayZeiritsuRyohi();
		};
		commonFutanBumonCdLostFocus("1",dialogRetFutanBumonCd, dialogRetFutanBumonName, title, $("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val(), $("input[name=kamokuCdRyohi]").val(), $("select[name=kariShiireKbn]"), "A004", $("input[name=shiwakeEdaNoRyohi]").val());
	});

	//取引先選択ボタン押下時Function
	$("#torihikisakiSentakuButton").click(function(e){
		dialogRetTorihikisakiCd = $("input[name=torihikisakiCdRyohi]");
		dialogRetTorihikisakiName = $("input[name=torihikisakiNameRyohi]");
		dialogRetFurikomisaki  = $("input[name=furikomisakiJouhouRyohi]");
		commonTorihikisakiSentaku("A004");
	});

	//取引先クリアボタン押下時Function
	$("#torihikisakiClearButton").click(function(e){
		$("input[name=torihikisakiCdRyohi]").val("");
		$("input[name=torihikisakiNameRyohi]").val("");
		$("input[name=furikomisakiJouhouRyohi]").val("");
	});

	//取引先コードロストフォーカス時Function
	$("input[name=torihikisakiCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetTorihikisakiCd = $("input[name=torihikisakiCdRyohi]");
		dialogRetTorihikisakiName = $("input[name=torihikisakiNameRyohi]");
		dialogRetFurikomisaki  = $("input[name=furikomisakiJouhouRyohi]");
		commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd, dialogRetTorihikisakiName, title, dialogRetFurikomisaki, "A004");
	});

	//プロジェクト選択ボタン押下時Function
	$("#projectSentakuButton").click(function(e){
		dialogRetProjectCd = $("input[name=projectCdRyohi]");
		dialogRetProjectName = $("input[name=projectNameRyohi]");
		commonProjectSentaku();
	});

	//プロジェクトコードロストフォーカス時Function
	$("input[name=projectCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetProjectCd = $("input[name=projectCdRyohi]");
		dialogRetProjectName = $("input[name=projectNameRyohi]");
		commonProjectCdLostFocus(dialogRetProjectCd, dialogRetProjectName, title);
	});

	//セグメント選択ボタン押下時Function
	$("#segmentSentakuButton").click(function(e){
		dialogRetSegmentCd = $("input[name=segmentCdRyohi]");
		dialogRetSegmentName = $("input[name=segmentNameRyohi]");
		commonSegmentSentaku();
	});

	//セグメントコードロストフォーカス時Function
	$("input[name=segmentCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetSegmentCd = $("input[name=segmentCdRyohi]");
		dialogRetSegmentName = $("input[name=segmentNameRyohi]");
		commonSegmentCdLostFocus(dialogRetSegmentCd, dialogRetSegmentName, title);
	});

	// 課税・分離・仕入区分の変更時
	$("select[name=kazeiKbnRyohi]").change(function() {
		displayZeiritsuRyohi();
	});
	$("select[name=bunriKbn]").change(function() {
		displayZeiritsuRyohi();
	});
	$("select[name=kariShiireKbn]").change(function() {
		displayZeiritsuRyohi();
	});
});

/**
 * 仮払選択の表示制御初期化Function
 */
function karibaraiDispClear(){

	// 値のクリア
	$("input[name=karibaraiDenpyouId]").val("");
	$("input[name=karibaraiOn]").val("");
	$("input[name=karibaraiTekiyou]").val("");
	$("input[name=karibaraiShinseiKingaku]").val("0");
	$("input[name=karibaraiKingaku]").val("0");
	$("#karibaraiMishiyouFlg").prop("checked", false);
	$("#shucchouChuushiFlg").prop("checked", false);

	// 表示制御を仮払未選択の状態に戻す
	$("#karibaraiAnken").css("display", "none");
	displayRyohiShinsei(true, 0);

	// 金額再計算
	calcMoney();
}

/**
 * 使用者変更時に明細情報をリセット
 * @param dialogRetuserId		ユーザーID反映部(commonShainNoLostFocus用)
 * @param dialogRetusername		ユーザー名反映部(commonShainNoLostFocus用)
 * @param dialogRetshainNo		社員番号反映部(commonShainNoLostFocus用)
 * @param title					タイトル文字列(commonShainNoLostFocus用)
 * @param shiyoushaStr			確認ダイアログ「使用者」部文言
 * @param isKaigai 海外旅費か？
 */
function changeShiyoushaInternal(dialogRetuserId, dialogRetusername, dialogRetshainNo, title, shiyoushaStr, isKaigai){
	if(dialogRetuserId.val() == "" || dialogRetshainNo.val() == "") return;
	var shainNoBefChg = $("input[name=shainNoBefChg]").val();
	if(shainNoBefChg != "" && dialogRetshainNo.val() != shainNoBefChg){
		if(window.confirm(shiyoushaStr  + "の変更にあたり、入力内容がクリアされます。よろしいですか？")){
			//全記入内容クリアのため、action再呼出
			var hrefStr = (isKaigai ? "kaigai_" : "") + "ryohi_seisan?denpyouKbn=" + (isKaigai ? "A011": "A004") + "&dairiFlg=1&shiyoushaHenkouShainNo=" + dialogRetshainNo.val();
			if($("#workflowForm").find("input[name=denpyouId]").val() != ""){
				var dpId = encodeURI($("#workflowForm").find("input[name=denpyouId]").val());
				hrefStr = hrefStr + "&denpyouId=" + dpId;
			}
			location.href = hrefStr;

		}else{
			//入力を変更前に戻す
			dialogRetshainNo.val($("input[name=shainNoBefChg]").val());
			commonShainNoLostFocus(dialogRetshainNo, dialogRetusername, title, dialogRetuserId, false);
			return;
		}
	}
	$("input[name=shainNoBefChg]").val(dialogRetshainNo.val());
}

/**
 * 課税区分により税率の標示有無切替
 * 税込・税抜なら表示、それ以外なら非表示
 * 取引選択時に呼ばれる
 */
function displayZeiritsuRyohi() {
	var kazeiKbnGroup = $("select[name=kazeiKbnRyohi] option:selected").attr("data-kazeiKbnGroup");

	//インボイス対応前：課税区分が税込、税抜以外の場合は消費税入力欄を丸ごと消していた（zeiritsuArea.hide()）
	//20230329 ひとまずの対応として税込、税抜以外の場合は税率を非表示にする
	var hyoujiFlg = (kazeiKbnGroup == "1" || kazeiKbnGroup == "2");
	var displayStyle = ( false == hyoujiFlg ) ? 'hide' : 'show';
	$("#zeiritsuAreaRyohi")[displayStyle]();
	displaySeigyo();
}

//課税区分・分離区分・仕入区分は取引仕訳登録で入力した値しか認めないため、常にグレーアウト
function displaySeigyo(){
	setShouhizeiControls($("input[name=denpyouId]").val(), $("input[name=shoriGroupRyohi]").val(), $("select[name=kazeiKbnRyohi]"), $("select[name=zeiritsuRyohi]"), $("select[name=bunriKbn]"), $("select[name=kariShiireKbn]"));
	$("select[name=kazeiKbnRyohi]").prop("disabled",true);
	$("select[name=bunriKbn]").prop("disabled",true);
	$("select[name=kariShiireKbn]").prop("disabled",true);
}

//明細税率を計算する
function calcMeisaiZeigaku(kazeiKbn, zeiritsu, hasuuShoriFlg, target, shiireKeikaSothiFlg) {
	// 手動修正されたなら手直ししない
	if(target.find("input[name=zeigakuFixFlg]").val() == "1") {
		return;
	}
	let meisaiKingaku = Number(target.find("input[name=meisaiKingaku]").val().replaceAll(",", ""));
	//不課税・免税の場合は事業者区分を通常課税に変更する（税率0は非課税とみなす）
	if(zeiritsu == 0){
		target.find("input[name=jigyoushaKbnRyohi]").val("0");
	}
	let jigyoushaFlg = target.find("input[name=jigyoushaKbnRyohi]").val() > 0 ? target.find("input[name=jigyoushaKbnRyohi]").val() : "0";
	
	let jigyoushaNum = (shiireKeikaSothiFlg == "1" || jigyoushaFlg == "0")
		? 1
		: "1" == jigyoushaFlg
			? 0.8
			: 0.5; // 掛け算なのでデフォは1

	//decimalとして持つ
	let dZeiritsu = new Decimal(zeiritsu);
	let dMeisaiKingaku = new Decimal(meisaiKingaku);
	let dJigyoushaNum = new Decimal(jigyoushaNum);
	let dBunbo = new Decimal(100).add(dZeiritsu);
	//計算用
	let shouhizeigaku = hasuuKeisanZaimuFromImporter(dMeisaiKingaku.mul(dZeiritsu).div(dBunbo).mul(dJigyoushaNum), hasuuShoriFlg, false);
	let zeinukiKingaku	= dMeisaiKingaku.sub(shouhizeigaku);
		
	//計算した本体金額、消費税額の値をセット or クリア
	target.find("input[name=zeinukiKingakuRyohi]").val(zeinukiKingaku.toString().formatMoney());
	target.find("input[name=shouhizeigakuRyohi]").val(shouhizeigaku?.toString()?.formatMoney());
}
</script>