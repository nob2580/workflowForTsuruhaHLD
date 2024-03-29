<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- エラー -->
<input type='hidden' id="errorCnt" value='${fn:length(errorList)}'>
<%@ include file="/jsp/eteam/include/InputError.jsp" %>

<style>
.early {
  height:30px;
  width:30px;
  border-radius:50%;
  line-height:30px;
  text-align:center;
  background:#00A8F3;
  font-size:20px;
  margin-left:5px;
  color:white;
}
.cheap {
  height:30px;
  width:30px;
  border-radius:50%;
  line-height:30px;
  text-align:center;
  background:#0ED145;
  font-size:20px;
  margin-left:5px;
  color:white;
}
.easy {
  height:30px;
  width:30px;
  border-radius:50%;
  line-height:30px;
  text-align:center;
  background:#FF7F27;
  font-size:20px;
  margin-left:5px;
  color:white;
}
.wrapper {
  display: flex;
  justify-content: center;
  align-items: center;

</style>


<!-- メイン -->
<div id='dialogMainDenpyouMeisai' class='form-horizontal'>
	<input type='hidden' name="dispMode" value='${su:htmlEscape(dispMode)}'>
	<input type='hidden' name='shubetsu1' value='${su:htmlEscape(shubetsu1)}'>
	<input type='hidden' name='shubetsu2' value='${su:htmlEscape(shubetsu2)}'>
	<input type="hidden" name='jidounyuuryokuFlg' value='${su:htmlEscape(jidounyuuryokuFlg)}'>
	<input type='hidden' name='shouhyouShoruiHissuFlg' value='${su:htmlEscape(shouhyouShoruiHissuFlg)}'>
	<input type='hidden' name='ryoushuushoSeikyuushoDefault' value='${su:htmlEscape(ryoushuushoSeikyuushoDefault)}'>
	<input type="hidden" name='index' value='${su:htmlEscape(index)}'>
	<input type="hidden" name='maxIndex' value='${su:htmlEscape(maxIndex)}'>
	<input type='hidden' name='zeroEnabled'			value='' >
	<input type='hidden' name='userId'				value='' >
	<input type='hidden' name='kaigaiFlgRyohi'		value='${su:htmlEscape(kaigaiFlgRyohi)}' >
	<input type='hidden' name='kazeiFlgRyohi'		value='${su:htmlEscape(kazeiFlgRyohi)}'>
	<input type='hidden' name='kazeiFlgKamoku'		value='${su:htmlEscape(kazeiFlgKamoku)}'>
	<input type='hidden' name='kazeiFlg'			value=''>
	<input type='hidden' name='icCardNo'			value='${su:htmlEscape(icCardNo)}'>
	<input type='hidden' name='icCardSequenceNo'	value='${su:htmlEscape(icCardSequenceNo)}'>
	<input type='hidden' name='suuryouNyuryokuType'	value='${su:htmlEscape(suuryouNyuryokuType)}'>
	<input type='hidden' name='suuryouKigou'		value='${su:htmlEscape(suuryouKigou)}'>
	<input type='hidden' name='hayaFlg'				value='${su:htmlEscape(hayaFlg)}'>
	<input type='hidden' name='yasuFlg'				value='${su:htmlEscape(yasuFlg)}'>
	<input type='hidden' name='rakuFlg'				value='${su:htmlEscape(rakuFlg)}'>
	<input type='hidden' name='invoiceDenpyou' value='${su:htmlEscape(invoiceDenpyou)}'>
	<!-- ボタン -->
	<section>
		<button type='button' class='btn btn-small' name = 'koutsuuhiNorikaeannaiButton' onclick="norikaeannaiKensakuShow()" <c:if test='${(not ekispertEnable) or (kaigaiFlgRyohi eq 1)}'>style='display:none;'</c:if>>乗換案内検索</button>
		<button type='button' class='btn btn-small' name = 'kakoEntryButton' onclick="kakoEntry()">過去明細</button>
	</section>

	<!-- 入力フィールド -->
	<section>
		<h2 style='line-height: 30px;<c:if test='${null eq index or "" eq index}'> display:none</c:if>'>
			<span>${su:htmlEscape(index + 1)} / ${su:htmlEscape(maxIndex + 1)}
				<button type='button' name='btnPrevious' class='btn' style='position: absolute; right: 90px; font-size: 14px;'><i class='icon-arrow-left'></i> 前へ</button>
				<button type='button' name='btnNext' class='btn' style='position: absolute; right: 10px;  font-size: 14px;'>次へ <i class='icon-arrow-right'></i></button>
			</span>
		</h2>
		<div>
			<div>
				<!-- 種別 -->
				<div class='control-group' <c:if test='${not ks.shubetsu1.hyoujiFlg}'>style='display:none;'</c:if>>
					<label class='control-label'><c:if test='${ks.shubetsu1.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shubetsu1.name)}</label>
					<div class='controls'>
						<span class='shubetsu1'>
							${su:htmlEscape(shubetsu1)}
						</span>
						　
						<label class='label'><c:if test='${ks.shubetsu2.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shubetsu2.name)}</label>
						<span class='shubetsu2'>
							${su:htmlEscape(shubetsu2)}
						</span>
					</div>
				</div>
				<!-- 期間 -->
				<div class='control-group' <c:if test='${not ks.kikan.hyoujiFlg}'>style='display:none;'</c:if>>
					<label class='control-label'><c:if test='${ks.kikan.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kikan.name)}</label>
					<div class='controls'>
						<input type='text' class='input-small datepicker' name='kikanFrom' value='${su:htmlEscape(kikanFrom)}' >
					</div>
				</div>
				<!-- 交通手段／領収書・請求書等 -->
				<div class='control-group'>
					<label class='control-label' for='koutsuu' <c:if test='${not ks.koutsuuShudan.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.koutsuuShudan.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.koutsuuShudan.name)}</label>
					<div class='controls'>
						<select name='koutsuuShudan' class='input-large' <c:if test='${not ks.koutsuuShudan.hyoujiFlg}'>style='display:none;'</c:if>>
							<option value='' style='display:none'></option>
<c:forEach var="record" items="${koutsuuShudanList}">
							<option value='${su:htmlEscape(record.koutsuu_shudan)}' data-shouhyouShoruiHissuFlg='${record.shouhyou_shorui_hissu_flg}' data-zeiKubun='${record.zei_kubun}' data-suuryouNyuryokuType='${record.suuryou_nyuryoku_type}' data-tanka='${record.tanka}' data-suuryouKigou='${record.suuryou_kigou}'<c:if test='${record.koutsuu_shudan eq koutsuuShudan && record.shouhyou_shorui_hissu_flg eq shouhyouShoruiHissuFlg}'>selected</c:if>>${su:htmlEscape(record.koutsuu_shudan)}</option>
</c:forEach>
						</select>
						<span class='ryoushuushoSeikyuushoTouArea' <c:if test='${denpyouKbn eq "A005"}'>style='display:none;'</c:if>>
							<label class='label' for='ryoushuushoSeikyuushoTouFlg' <c:if test='${not ks.ryoushuushoSeikyuushoTouFlg.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.ryoushuushoSeikyuushoTouFlg.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.ryoushuushoSeikyuushoTouFlg.name)}</label>
							<input type='checkbox' name='ryoushuushoSeikyuushoTouFlg' <c:if test='${"1" eq ryoushuushoSeikyuushoTouFlg}'>checked</c:if> value='${su:htmlEscape(ryoushuushoSeikyuushoTouFlg)}' <c:if test='${not ks.ryoushuushoSeikyuushoTouFlg.hyoujiFlg}'>style='display:none;'</c:if>>
						</span>
						<!-- 202210 早安楽のエリア -->
						<span class='wrapper' id='hayaYasuRakuArea' style='float: right; <c:if test='${not ks.hayaYasuRaku.hyoujiFlg}'> display:none</c:if>' >
							<span class="early" id='hayaIcon' <c:if test='${su:htmlEscape(hayaFlg) eq "1"}'> display:none</c:if>>早</span>
						    <span class="cheap" id='yasuIcon' <c:if test='${su:htmlEscape(yasuFlg) eq "1"}'> display:none</c:if>>安</span>
						    <span class="easy" id='rakuIcon' <c:if test='${su:htmlEscape(rakuFlg) eq "1"}'> display:none</c:if>>楽</span>
						</span>
					</div>
				</div>
				<!-- 内容 -->
				<div class='control-group' <c:if test='${not ks.naiyouKoutsuuhi.hyoujiFlg}'>style='display:none;'</c:if>>
					<label class='control-label'><c:if test='${ks.naiyouKoutsuuhi.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.naiyouKoutsuuhi.name)}</label>
					<div class='controls'>
						<textarea name='naiyou' maxlength='512' class='input-block-level'>${su:htmlEscape(naiyou)}</textarea>
					</div>
				</div>
				<!-- 備考 -->
				<div class='control-group' <c:if test='${not ks.bikouKoutsuuhi.hyoujiFlg}'>style='display:none;'</c:if>>
					<label class='control-label'><c:if test='${ks.bikouKoutsuuhi.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.bikouKoutsuuhi.name)}</label>
					<div class='controls'>
						<input type='text' class='input-block-level meisaiBikouObj' name='bikou' value='${su:htmlEscape(bikou)}'>
					</div>
				</div>
				<!-- 幣種・レート -->
				<div class='control-group' <c:if test='${not (ks.heishu.hyoujiFlg && enableGaika)}'>style='display:none;'</c:if>>
					<label class='control-label'><c:if test='${ks.heishu.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.heishu.name)}</label>
					<div class='controls'>
						<input type='text' class='input-medium' name='heishuCdRyohi' maxlength='200' value='${su:htmlEscape(heishuCdRyohi)}'>
						<button type='button' id='heishuSentakuButton' class='btn btn-small'>選択</button>
						<label class='label' <c:if test='${not ks.rate.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.rate.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.rate.name)}</label>
						<input type='text' class='input-medium' name='rateRyohi' maxlength='200' value='${su:htmlEscape(rateRyohi)}'>
					</div>
				</div>
				<!-- 外貨 -->
				<div class='control-group' <c:if test='${not (ks.gaika.hyoujiFlg && enableGaika)}'>style='display:none;'</c:if>>
					<label class='control-label'><c:if test='${ks.gaika.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.gaika.name)}</label>
					<div class='controls'>
						<input type='text' name='gaikaRyohi' class='input-medium autoNumericDecimal' value='${su:htmlEscape(gaikaRyohi)}'><span id='tuukatani'></span>
					</div>
				</div>
				<!-- 単価 -->
				<div class='control-group' <c:if test='${not (ks.tanka.hyoujiFlg || ks.oufukuFlg.hyoujiFlg)}'>style='display:none;'</c:if>>
					<label class='control-label' <c:if test='${not ks.tanka.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.tanka.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.tanka.name)}</label>
					<div class='controls'>
						<input type='text' name='tanka' class='input-medium meisaiTankaObj autoNumeric <c:if test='${tankaKotei}'>tanka_kotei</c:if>' <c:if test='${tankaKotei}'>disabled</c:if> value='${su:htmlEscape(tanka)}' <c:if test='${not ks.tanka.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.tanka.hyoujiFlg}'>円　
						</c:if><label class='label oufukuLabel' <c:if test='${not ks.oufukuFlg.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.oufukuFlg.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.oufukuFlg.name)}</label>
						<input type='checkbox' name='oufukuFlg' <c:if test='${"1" eq oufukuFlg}'>checked</c:if> value="1" <c:if test='${not ks.oufukuFlg.hyoujiFlg}'>style='display:none;'</c:if>>
					</div>
				</div>
				<!-- 数量 -->
				<div class='control-group suuryou' style='display:none;'>
					<label class='control-label'><span class='required'>*</span>数量</label>
					<div class='controls' style='white-space: nowrap;'>
						<input type='text' name='suuryou' class='input-medium autoNumericDecimal' value='${su:htmlEscape(suuryou)}'>
					</div>
				</div>
				<!-- 金額 -->
				<div class='control-group' <c:if test='${not ks.meisaiKingaku.hyoujiFlg}'>style='display:none;'</c:if>>
					<label class='control-label'><c:if test='${ks.meisaiKingaku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.meisaiKingaku.name)}</label>
					<div class='controls'>
						<input type='text' name='meisaiKingaku' class='input-medium autoNumericNoMax' disabled value='${su:htmlEscape(meisaiKingaku)}'>円
						　
<c:if test="${enableHoujinCard}">
						<!-- 法人カード -->
						<label class='label houjinCardLabel' <c:if test='${not ks.houjinCardRiyou.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.houjinCardRiyou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.houjinCardRiyou.name)}</label>
						<input class='check-group' type='checkbox' name='houjinCardFlgRyohi' <c:if test='${"1" eq houjinCardFlgRyohi}'>checked</c:if> value="1" <c:if test='${not ks.houjinCardRiyou.hyoujiFlg}'>style='display:none;'</c:if>>
</c:if>
<c:if test="${enableKaishaTehai}">
						<!-- 会社手配 -->
						<label class='label kaishaTehaiLabel' <c:if test='${not ks.kaishaTehai.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.kaishaTehai.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kaishaTehai.name)}</label>
						<input class='check-group' type='checkbox' name='kaishaTehaiFlgRyohi' <c:if test='${"1" eq kaishaTehaiFlgRyohi}'>checked</c:if> value="1" <c:if test='${not ks.kaishaTehai.hyoujiFlg}'>style='display:none;'</c:if>>
</c:if>
						<input type='hidden' name='himodukeCardMeisaiRyohi'	value='${su:htmlEscape(himodukeCardMeisaiRyohi)}'>
					</div>
				</div>
			</div>
<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
			<!-- 消費税額(うち消費税額) -->
			<div class='control-group invoiceOnly' name='shouhizeigaku'<c:if test='${not (ks.shouhizeigaku.hyoujiFlg && kaigaiFlgRyohi eq "0")}'>style='display:none;'</c:if>>
				<label class='control-label'>${su:htmlEscape(ks.shouhizeigaku.name)}</label>
				<div class='controls'>
					<input type='text' name='shouhizeigaku' id='shouhizeigaku' class='input-medium autoNumericWithCalcBox' value='${su:htmlEscape(shouhizeigaku)}'>円
					<button class="btn btn-small" id='zeiShuseiBtn' name='zeiShuseiBtn'>修正</button>
					<input type='hidden' name='zeinukiKingaku' value='${su:htmlEscape(zeinukiKingaku)}'>
					<input type='hidden' name='zeigakuFixFlg' value='${su:htmlEscape(zeigakuFixFlg)}'>
				</div>
			</div>
			<!-- 支払先名 -->
			<div class='control-group invoiceOnly' name='shiharaisakiName'>
				<label class='control-label'<c:if test='${not ks.shiharaisaki.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shiharaisaki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaisaki.name)}</label>
				<div class='controls' <c:if test='${not ks.shiharaisaki.hyoujiFlg}'>style='display:none;'</c:if>>
					<input type='text' name='shiharaisakiName' maxlength='60' class='input-block-level' value='${su:htmlEscape(shiharaisakiName)}'>
				</div>
			</div>
			<!-- 事業者区分 -->
				<div class='control-group invoiceOnly' id='jigyoushaKbnDiv' name='jigyoushaKbn'<c:if test='${invoiceDenpyou eq "1" || not (ks.jigyoushaKbn.hyoujiFlg && kaigaiFlgRyohi eq "0")}'>style='display:none;'</c:if>>
					<label class='control-label' for='jigyoushaKbn'>事業者区分</label>
					<div class='controls'>
						<c:forEach var="jigyoushaKbnRecord" items="${jigyoushaKbnList}">
							<label class='radio inline'><input type='radio' name='jigyoushaKbn' value='${su:htmlEscape(jigyoushaKbnRecord.naibuCd)}' <c:if test='${jigyoushaKbnRecord.naibuCd eq jigyoushaKbn}'>checked </c:if>>${su:htmlEscape(jigyoushaKbnRecord.name)}</label>
						</c:forEach>
					</div>
				</div>
				<input type='hidden' value='${su:htmlEscape(jigyoushaKbn)}'>
</c:if>
		</div>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>

var meisai = $("#dialogMainDenpyouMeisai");
var dispMode = meisai.find("input[name=dispMode]").val();

/**
 * 以下の場面で呼ばれる。
 * ダイアログの追加・変更ボタン押下時(confirmイベント投げる前)→POSTパラメータ生成用MAP
 * ダイアログの追加・変更ボタン押下後(confirmイベント投げた後のコールバック)→メイン画面にダイアログ内容を反映する為のMAP
 * @return 明細内容マップ
 */
function getDialogMeisaiInfo() {
	return {
		index : $("input[name=index]").val(),
		maxIndex : $("input[name=maxIndex]").val(),
		denpyouId : $("input[name=denpyouId]").val(),
		denpyouKbn : $("input[name=denpyouKbn]").val(),
		zeroEnabled : $("input[name=karibaraiOn]:checked").length == 1 ? $("input[name=karibaraiOn]:checked").val() : "0",
		userId : $("input[name=userIdRyohi]").val(),
		dispMode : meisai.find("input[name=dispMode]").val(),
		kazeiFlgRyohi : meisai.find("input[name=kazeiFlgRyohi]").val(),
		kazeiFlgKamoku : meisai.find("input[name=kazeiFlgKamoku]").val(),

		kikanFrom : meisai.find("input[name=kikanFrom]").val(),
		kikanTo : "",
		kaigaiFlgRyohi : meisai.find("input[name=kaigaiFlgRyohi]").val() == "1" ? "1" : "0",
		shubetsuCd : "1",
		shubetsu1 : meisai.find("input[name=shubetsu1]").val(),
		shubetsu2 : meisai.find("input[name=shubetsu2]").val(),
		koutsuuShudan : meisai.find("select[name=koutsuuShudan] :selected").val() === undefined ?  " " : meisai.find("select[name=koutsuuShudan] :selected").val(), //削除済交通手段の過去明細をそのまま追加すると交通手段がundefindになるので、ブランクに置き換える。(置き換えないとsetDialogMeisaiInfoでフリーズする。)
 		shouhyouShoruiHissuFlg : meisai.find("input[name=shouhyouShoruiHissuFlg]").val(),
		ryoushuushoSeikyuushoTouFlg : (meisai.find("input[name=ryoushuushoSeikyuushoTouFlg]:checked").length == 1) ? "1" : "0",
		naiyou : meisai.find("textarea[name=naiyou]").val(),
		bikou : meisai.find("input[name=bikou]").val(),
		oufukuFlg : (meisai.find("input[name=oufukuFlg]:checked").length == 1) ? "1" : "0",
		houjinCardFlgRyohi : (meisai.find("input[name=houjinCardFlgRyohi]:checked").length == 1) ? "1" : "0",
		kaishaTehaiFlgRyohi : (meisai.find("input[name=kaishaTehaiFlgRyohi]:checked").length == 1) ? "1" : "0",
		jidounyuuryokuFlg : meisai.find("input[name=jidounyuuryokuFlg]").val(),
		nissuu : meisai.find("input[name=nissuu]").val(),
		heishuCdRyohi : meisai.find("input[name=heishuCdRyohi]").val(),
		rateRyohi : meisai.find("input[name=rateRyohi]").val(),
		gaikaRyohi : meisai.find("input[name=gaikaRyohi]").val(),
		taniRyohi : meisai.find("#tuukatani").text(),
		tanka : meisai.find("input[name=tanka]").val(),
		suuryouNyuryokuType : meisai.find("input[name=suuryouNyuryokuType]").val(),
		suuryou : meisai.find("input[name=suuryou]").val(),
		suuryouKigou : meisai.find("input[name=suuryouKigou]").val(),
		meisaiKingaku : meisai.find("input[name=meisaiKingaku]").val(),
		zeiKubun : meisai.find("select[name=koutsuuShudan] :selected").attr("data-zeiKubun"),
		kazeiFlgRyohiMeisai : meisai.find("input[name=kazeiFlg]").val(),
		icCardNo : meisai.find("input[name=icCardNo]").val(),
		icCardSequenceNo : meisai.find("input[name=icCardSequenceNo]").val(),
		himodukeCardMeisaiRyohi : meisai.find("input[name=himodukeCardMeisaiRyohi]").val(),
		hayaFlg : meisai.find("input[name=hayaFlg]").val() == "1" ? "1" : "0",
		yasuFlg : meisai.find("input[name=yasuFlg]").val() == "1" ? "1" : "0",
		rakuFlg : meisai.find("input[name=rakuFlg]").val() == "1" ? "1" : "0",
<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
		shouhizeigaku : meisai.find("input[name=shouhizeigaku]").val(),
		zeinukiKingaku : meisai.find("input[name=zeinukiKingaku]").val(),
		shiharaisaki : meisai.find("input[name=shiharaisakiName]").val(),
		jigyoushaKbn : meisai.find("input[name=jigyoushaKbn]:checked").val() == "1" ? "1": "0",
		jigyoushaKbnText : meisai.find("input[name=jigyoushaKbn] :checked").text(),
		zeigakuFixFlg : meisai.find("input[name=zeigakuFixFlg]").val(),
</c:if>
	};
}

//消費税額修正ボタンの表示制御
function zeiShuseiBtnDisplay(){
	var meisai = $("#dialogMainDenpyouMeisai");
	var zeigakuShuseiFlg = $("#workflowForm").find("input[name=zeigakuShuseiFlg]").val();
	var kaigaiFlg = meisai.find("input[name=kaigaiFlgRyohi]").val();
	var kazeiKbn = kaigaiFlg == "1" ? $("select[name=kaigaiKazeiKbnRyohi] option:selected").val() : $("select[name=kazeiKbnRyohi] option:selected").val();
	var isZeinuki = (kazeiKbn == "002" || kazeiKbn == "013" || kazeiKbn == "014");
	if(isZeinuki){
		//課税区分が税抜系　→会社設定(zeigakuShuseiFlg)に従う
		if(zeigakuShuseiFlg == "1"){
			meisai.find("button[name=zeiShuseiBtn]").show();
			meisai.find("input[name=shouhizeigaku]").prop("disabled", true);
		}else {
			meisai.find("button[name=zeiShuseiBtn]").hide();
			meisai.find("input[name=shouhizeigaku]").prop("disabled", zeigakuShuseiFlg != "2" );
		}
		if(kaigaiFlg == "1"){
			//海外明細の場合には、課税区分が何であれ不可にする　うち消費税額が表示されていても、税修正ボタンが表示されないということでとりあえずOKとする
			//TODO （末尾21以降、海外取引でも課税区分：税込・税抜系に対応するまでの処理　不要になる予定）
			meisai.find("input[name=shouhizeigaku]").prop("disabled", true);
			meisai.find("button[name=zeiShuseiBtn]").hide();
		}
	}else{
		//課税区分が税抜系以外　→常に不可
		meisai.find("input[name=shouhizeigaku]").prop("disabled", true);
		meisai.find("button[name=zeiShuseiBtn]").hide();
	}
	if(meisai.find("input[name=shouhizeigaku]").prop("disabled")){
		delCalculator(meisai.find("input[name=shouhizeigaku]"));		
	}else{
		addCalculator(meisai.find("input[name=shouhizeigaku]"));
	}
}


/**
 * 事業者区分の入力可否を制御
 * インボイス対応前の伝票では非表示になるためこの制御では分岐しない
 */
function jigyoushaKbnSeigyo(){
	var meisai = $("#dialogMainDenpyouMeisai");
	var jigyoushaKbnHenkouFlg = $("#workflowForm").find("input[name=jigyoushaKbnHenkouflg]").val();
	//jigyoushaKbnHenkouFlg は事業者払い3伝票以外は必ず1になるようにjudgeJigyoushaKbnHenkou()でしている
	if($("input[name=shiwakeEdaNoRyohi]").val() == ''){
		//取引をクリアした場合
		meisai.find("input[name=jigyoushaKbn]").prop("disabled", true);
		meisai.find("input[name=jigyoushaKbn]:eq(0)").prop("checked", true);
	}else if(jigyoushaKbnHenkouFlg == "0" ){
		//(業者払い3伝票)会社設定で変更不可にされている
		meisai.find("input[name=jigyoushaKbn]").prop("disabled", true);
	}else{
		var shoriGroup = $("input[name=shoriGroupRyohi]").val();
		var kazeiKbn = $("select[name=kazeiKbnRyohi] option:selected").val();
		if(shoriGroup == "21" ||
			((shoriGroup == "2"|| shoriGroup == "5"|| shoriGroup == "6"|| shoriGroup == "7"|| shoriGroup == "8"|| shoriGroup == "10")
				&& (kazeiKbn == "001" || kazeiKbn == "002" || kazeiKbn == "011" || kazeiKbn == "013"))){
			//変更可
			meisai.find("input[name=jigyoushaKbn]").prop("disabled", false);
			var notCheck = $('[name="jigyoushaKbn"]:checked').length;
			if(notCheck < 1){
				//変更可能時、取引の取引先が任意だったなどの理由で、事業者区分ラジオボタンがどちらもチェックされていなかった場合
				meisai.find("input[name=jigyoushaKbn]:eq(0)").prop("checked", true);
			}
		}else{
			//科目処理グループと課税区分により変更不可
			meisai.find("input[name=jigyoushaKbn]").prop("disabled", true);
			meisai.find("input[name=jigyoushaKbn]:eq(0)").prop("checked", true);
		}
	}
}

/**
 * ダイアログ表示時に値をセットする。
 * セットする値はメイン画面のhidden項目をMAPで渡す。
 * @param isEnable(boolewan)	更新モードならtrue
 * @param meisaiInfo(MAP)		メイン画面のhidden情報(追加ならnull)
 */
function setDialogMeisaiInfo(isEnable, meisaiInfo) {
	
	//インボイス対応前の場合、インボイス対応項目を非表示とする
	var hyoujiFlg = $("#denpyouJouhou").find("select[name=invoiceDenpyou]").val();
	$("input[name=invoiceDenpyou]").val(hyoujiFlg);
	var displayStyle = '0' == hyoujiFlg ? 'show' : 'hide';
	if(hyoujiFlg == "1")
	{
		$(".invoiceOnly")[displayStyle]();
	}
	if(isEnable){
		zeiShuseiBtnDisplay();
		jigyoushaKbnSeigyo();
	}

	// フォーカス
	meisai.find('button:visible').first().focus();

	//追加処理のデフォルト表示
	if (meisaiInfo == null) {
		meisai.find("input[name=shubetsu1]").val("交通費");
		meisai.find("span.shubetsu1").text(meisai.find("input[name=shubetsu1]").val());
		meisai.find("input[name=shubetsu2]").val("手入力");
		meisai.find("span.shubetsu2").text(meisai.find("input[name=shubetsu2]").val());
		meisai.find("input[name=jidounyuuryokuFlg]").val("0");
		meisai.find("input[name=shouhyouShoruiHissuFlg]").val("0");
		meisai.find("input[name=hayaFlg]").val("0");
		meisai.find("#hayaIcon").hide();
		meisai.find("input[name=yasuFlg]").val("0");
		meisai.find("#yasuIcon").hide();
		meisai.find("input[name=rakuFlg]").val("0");
		meisai.find("#rakuIcon").hide();

	//追加処理の再表示 or 変更、参照処理
	} else {

		// スペース
		var nbsp = String.fromCharCode(160);

		meisai.find("input[name=shubetsu1]").val(meisaiInfo["shubetsu1"]);
		meisai.find("span.shubetsu1").text(meisaiInfo["shubetsu1"]);
		meisai.find("input[name=shubetsu2]").val(meisaiInfo["shubetsu2"]);
		meisai.find("span.shubetsu2").text(meisaiInfo["shubetsu2"]);
		meisai.find("input[name=jidounyuuryokuFlg]").val(meisaiInfo["jidounyuuryokuFlg"]);

		if(!meisaiInfo["kikanFrom"]) {
			meisai.find("input[name=kikanFrom]").val(getKikanFrom().val());
		}
		else {
			meisai.find("input[name=kikanFrom]").val(meisaiInfo["kikanFrom"]);
		}
		meisai.find("input[name=kaigaiFlgRyohi]").val(meisaiInfo["kaigaiFlgRyohi"]);
		meisai.find("select[name=koutsuuShudan]").val(meisaiInfo["koutsuuShudan"].replaceAll(" ", nbsp));
		meisai.find("input[name=shouhyouShoruiHissuFlg]").val(meisaiInfo["shouhyouShoruiHissuFlg"]);
		meisai.find("input[name=ryoushuushoSeikyuushoTouFlg]").val(meisaiInfo["ryoushuushoSeikyuushoTouFlg"]);
		meisai.find("textarea[name=naiyou]").val(meisaiInfo["naiyou"]);
		meisai.find("input[name=bikou]").val(meisaiInfo["bikou"]);
		meisai.find("input[name=oufukuFlg]").prop("checked", ("1" == meisaiInfo["oufukuFlg"]));
		meisai.find("input[name=houjinCardFlgRyohi]").prop("checked", ("1" == meisaiInfo["houjinCardFlgRyohi"]));
		meisai.find("input[name=kaishaTehaiFlgRyohi]").prop("checked", ("1" == meisaiInfo["kaishaTehaiFlgRyohi"]));
		meisai.find("input[name=nissuu]").val(meisaiInfo["nissuu"]);
		meisai.find("input[name=heishuCdRyohi]").val(meisaiInfo["heishuCdRyohi"]);
		meisai.find("input[name=rateRyohi]").val(meisaiInfo["rateRyohi"]);
		meisai.find("input[name=gaikaRyohi]").val(meisaiInfo["gaikaRyohi"]);
		meisai.find("#tuukatani").text(meisaiInfo["taniRyohi"]);
		meisai.find("input[name=tanka]").val(meisaiInfo["tanka"]);
		meisai.find("input[name=suuryouNyuryokuType]").val(meisaiInfo["suuryouNyuryokuType"]);
		meisai.find("input[name=suuryou]").val(meisaiInfo["suuryou"]);
		meisai.find("input[name=suuryouKigou]").val(meisaiInfo["suuryouKigou"]);
		meisai.find("input[name=zeiKubun]").val(meisaiInfo["zeiKubun"]);
		meisai.find("input[name=kazeiFlg]").val(meisaiInfo["kazeiFlgRyohiMeisai"]);
		meisai.find("input[name=kazeiFlgRyohi]").val(meisaiInfo["kazeiFlgRyohi"]);
		meisai.find("input[name=kazeiFlgKamoku]").val(meisaiInfo["kazeiFlgKamoku"]);

		meisai.find("input[name=icCardNo]").val(meisaiInfo["icCardNo"]);
		meisai.find("input[name=icCardSequenceNo]").val(meisaiInfo["icCardSequenceNo"]);
		meisai.find("input[name=himodukeCardMeisaiRyohi]").val(meisaiInfo["himodukeCardMeisaiRyohi"]);
		
		//202210
		meisai.find("input[name=hayaFlg]").val(meisaiInfo["hayaFlg"]);
		if(meisaiInfo["hayaFlg"] == "1"){
			meisai.find("#hayaIcon").show();
		}else{
			meisai.find("#hayaIcon").hide();
		}
		meisai.find("input[name=yasuFlg]").val(meisaiInfo["yasuFlg"]);
		if(meisaiInfo["yasuFlg"] == "1"){
			meisai.find("#yasuIcon").show();
		}else{
			meisai.find("#yasuIcon").hide();
		}
		meisai.find("input[name=rakuFlg]").val(meisaiInfo["rakuFlg"]);
		if(meisaiInfo["rakuFlg"] == "1"){
			meisai.find("#rakuIcon").show();
		}else{
			meisai.find("#rakuIcon").hide();
		}

<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
		meisai.find("input[name=shouhizeigaku]").val(meisaiInfo["shouhizeigaku"]);
		meisai.find("input[name=zeinukiKingaku]").val(meisaiInfo["zeinukiKingaku"]);
		meisai.find("input[name=shiharaisakiName]").val(meisaiInfo["shiharaisaki"]);
		meisai.find("input[name=jigyoushaKbn]:eq(0)").prop("checked",("0" == meisaiInfo["jigyoushaKbn"]));
		meisai.find("input[name=jigyoushaKbn]:eq(1)").prop("checked",("1" == meisaiInfo["jigyoushaKbn"]));
</c:if>
	}

	// 次へ・前へボタン押下イベント付与：click(data,fn)
	$("button[name=btnPrevious]").removeAttr("disabled");
	$("button[name=btnNext]").removeAttr("disabled");
	meisai.find("button[name=btnPrevious]").click(meisaiInfo, meisaiPrevious);
	meisai.find("button[name=btnNext]").click(meisaiInfo, meisaiNext);

}

/**
 * 交通手段を再選択する
 * 以下のタイミングで呼ばれる。
 * ・過去明細選択時
 */
function reselectKoutsuuShudan(){
	var shouhyouShoruiHissuFlg = meisai.find("input[name=shouhyouShoruiHissuFlg]").val();
	var shouhyouShoruiHissuFlgHidden = meisai.find("select[name=koutsuuShudan] :selected").attr("data-shouhyouShoruiHissuFlg");
	if(shouhyouShoruiHissuFlg !== shouhyouShoruiHissuFlgHidden){
		//選択肢を選び直す
		var koutsuuShudan = meisai.find("select[name=koutsuuShudan] :selected").text();
		meisai.find("select[name=koutsuuShudan] option").filter(function(index){
			return $(this).text() === koutsuuShudan && $(this).attr("data-shouhyouShoruiHissuFlg") === shouhyouShoruiHissuFlg;
		}).prop('selected', true);
	}
}

/**
 * 入力制御
 * 以下のタイミングで呼ばれる。
 * ・初期表示
 * ・乗換案内の下へ追加ボタン押下時
 * ・過去明細選択時
 */
function inputSeigyo() {
	//領収書・請求書等の表示切替
	displayryoushuushoSeikyuushoTou();
	//課税フラグの設定
	getKazeiFlg();

	//明細金額の計算
	calcKoutsuuhiMeisaiMoney();

	//手入力なら内容と単価を変更可能に
	var tenyu = ("1" == meisai.find("input[name=jidounyuuryokuFlg]").val());
	meisai.find("textarea[name=naiyou]").prop("disabled", tenyu);
	meisai.find("input[name=tanka]").prop("disabled", tenyu);
	if(meisai.find("input[name=tanka]").hasClass("tanka_kotei")) meisai.find("input[name=tanka]").prop("disabled", true);

	//20220315 乗換案内検索で往復検索をした場合、明細画面の往復チェックボックスを無効にする
	var jidouOufuku = (meisai.find("input[name=shubetsu2]").val().indexOf("往復") > -1);
	meisai.find("input[name=oufukuFlg]").prop("disabled", jidouOufuku);
	/*if(meisai.find("input[name=shubetsu2]").val().indexOf("往復") > -1){
		meisai.find("input[name=oufukuFlg]").prop("disabled", true);
	}else{
		meisai.find("input[name=oufukuFlg]").prop("disabled", false);
	}*/

	if(meisai.find("input[name=kaigaiFlgRyohi]").val() == "0"){
		//数量入力タイプによる表示制御
		judgeInitSuuryou();
		//国内旅費交通費明細で数量入力タイプが「0」の時は単価項目のautoNumericを変更する
		changeAutoNumeric();
	}


	//法人カード履歴からの転記明細は金額入力不可とする。他項目の制御も。
	var himodukeCard = meisai.find("input[name=himodukeCardMeisaiRyohi]").val();
	if(himodukeCard != ""){
		meisai.find("[name=koutsuuhiNorikaeannaiButton],[name=kakoEntryButton]").hide();
<c:if test='${not houjinCardDateEnabled}'>
		meisai.find("input[name=kikanFrom]").prop("disabled", true);
</c:if>
		meisai.find("input[name=heishuCdRyohi]").prop("disabled", true);
		meisai.find("#heishuSentakuButton").prop("disabled", true);
		meisai.find("input[name=rateRyohi]").prop("disabled", true);
		meisai.find("input[name=gaikaRyohi]").prop("disabled", true);
		meisai.find("[name=oufukuFlg]").prop("disabled",true);
		meisai.find("input[name=tanka]").prop("disabled", true);
		meisai.find("input[name=houjinCardFlgRyohi]").prop("disabled", true);
		meisai.find("input[name=kaishaTehaiFlgRyohi]").prop("disabled", true);
	}

	//ICカードなら日付を変更不可能に
	var icCardNo = meisai.find("input[name=icCardNo]").val();
	if(icCardNo != ""){
		meisai.find("[name=koutsuuhiNorikaeannaiButton],[name=kakoEntryButton]").hide();
		meisai.find("[name=oufukuFlg]").prop("disabled",true);
		meisai.find("[name=houjinCardFlgRyohi]").prop("disabled",true);
		meisai.find("[name=kaishaTehaiFlgRyohi]").prop("disabled",true);
<c:if test='${not icCardDateEnabled}'>
		meisai.find("[name=kikanFrom]").prop("disabled", true);
</c:if>
	}

	//参照モードなら基本変更不可能
	if (dispMode == "3") {
		meisai.find('button,input[type!=hidden],select,textarea').prop("disabled", true);
		delCalculator(meisai.find("input[name=shouhizeigaku]"));
	}
}

/**
 * 領収書・請求書等の表示切替
 * 以下のタイミングで呼ばれる。
 * ・ダイアログロード直後
 * ・交通手段変更時
 */
function displayryoushuushoSeikyuushoTou() {
	var hissuFlg = meisai.find("select[name=koutsuuShudan] :selected").attr("data-shouhyouShoruiHissuFlg");
	// hidden項目に、選択された交通手段の証憑書類必須フラグをセット
	meisai.find("input[name=shouhyouShoruiHissuFlg]").val(hissuFlg);

	//選択された交通手段の証憑書類必須フラグがONの場合、領収書・請求書等チェックボックスを表示
<c:if test='${denpyouKbn ne "A005"}'>
	if ("1" == hissuFlg) {
		meisai.find("span.ryoushuushoSeikyuushoTouArea").show();
		if(meisai.find("input[name=ryoushuushoSeikyuushoTouFlg]").val() == ""){
			if (meisai.find("input[name=ryoushuushoSeikyuushoDefault]").val() == "1"){
				meisai.find("input[name=ryoushuushoSeikyuushoTouFlg]").prop("checked", true);
			}else{
				meisai.find("input[name=ryoushuushoSeikyuushoTouFlg]").prop("checked", false);
			}
		}
	} else {
		meisai.find("input[name=ryoushuushoSeikyuushoTouFlg]").prop("checked", false);
		meisai.find("span.ryoushuushoSeikyuushoTouArea").hide();
	}
</c:if>
}

/** 課税フラグの設定
 * 以下のタイミングで呼ばれる。
 * ・ダイアログロード直後
 * ・交通手段変更時
 */
function getKazeiFlg(){
	var zeiKubun = meisai.find("select[name=koutsuuShudan] :selected").attr("data-zeiKubun");
	var kazeiFlg = meisai.find("input[name=kazeiFlgRyohi]").val();
	// 税区分が税込(2)または免税(3)の場合
	if("2" == zeiKubun || "3" == zeiKubun){
		kazeiFlg = "1";

	// 税区分が対象外(1)または非課税(4)の場合
	}else if("1" == zeiKubun || "4" == zeiKubun ){
		kazeiFlg = "0";

	// 税区分が未設定(0)の場合
	}else if("0" == zeiKubun){
		kazeiFlg = meisai.find("input[name=kazeiFlgKamoku]").val();
	}
	meisai.find("input[name=kazeiFlg]").val(kazeiFlg);
}

/**
 * 数量入力タイプによる項目制御
 * 以下のタイミングで呼ばれる。
 * ・ダイアログロード直後
 * ・交通手段変更時
 */
function judgeInitSuuryou(){
	// 数量入力タイプの取得とhidden項目への値追加
	var suuryouNyuryokuType = meisai.find("select[name=koutsuuShudan] :selected").attr("data-suuryouNyuryokuType");
	meisai.find("input[name=suuryouNyuryokuType]").val(suuryouNyuryokuType);

	//IC・法人カード履歴から登録、駅すぱあと連携により入力した交通費なら
	//・数量入力タイプ「1」「2」の交通手段が選択された場合はエラーダイアログ表示して交通手段をブランクに
	//・以降の単価等の制御は数量入力タイプに関わらずスキップ
	if(meisai.find("input[name=icCardNo]").val() != "" || meisai.find("input[name=himodukeCardMeisaiRyohi]").val() != "" || meisai.find("input[name=jidounyuuryokuFlg]").val() == "1"){
		if(suuryouNyuryokuType == "1" || suuryouNyuryokuType == "2"){
			alert("自動入力された明細では、数量・数量単価入力タイプの交通手段は指定できません。")
			meisai.find("select[name=koutsuuShudan]").val("");
			meisai.find("input[name=suuryouNyuryokuType]").val("");
		}
		return;
	}

	// 数量入力タイプが「1:数量入力」「2:数量と単価を入力」であった場合
	if(suuryouNyuryokuType == "1" || suuryouNyuryokuType == "2"){
		meisai.find(".suuryou").show();
		// 数量記号を表示させる(もし交通手段変更前に数量記号のテキスト表示がある場合は、それを一旦削除してから新規で追加する)
		var suuryouKigou = meisai.find("select[name=koutsuuShudan] :selected").attr("data-suuryouKigou");
		if(meisai.find("#suuryouKigouText").length){
			meisai.find("#suuryouKigouText").remove();
		}
		meisai.find("input[name=suuryou]").after("<p id='suuryouKigouText' style='display:inline;'>"+suuryouKigou+"</p>");
		meisai.find("input[name=suuryouKigou]").val(suuryouKigou);
		// 往復、法人カード、会社手配欄は必要ないので隠す
		meisai.find(".oufukuLabel").hide();
		meisai.find("input[name=oufukuFlg]").hide();
		meisai.find("input[name=oufukuFlg]").removeAttr('checked').prop("checked", false);
		meisai.find(".houjinCardLabel").hide();
		meisai.find("input[name=houjinCardFlgRyohi]").hide();
		meisai.find("input[name=houjinCardFlgRyohi]").removeAttr('checked').prop("checked", false);
		meisai.find(".kaishaTehaiLabel").hide();
		meisai.find("input[name=kaishaTehaiFlgRyohi]").hide();
		meisai.find("input[name=kaishaTehaiFlgRyohi]").removeAttr('checked').prop("checked", false);
	}else{
		// 数量入力タイプが「0:入力しない」であった場合
		meisai.find(".suuryou").hide();
		var suuryouKigou = meisai.find("select[name=koutsuuShudan] :selected").attr("data-suuryouKigou");
		meisai.find("input[name=suuryouKigou]").val(suuryouKigou);
		if(meisai.find("#suuryouKigouText").length){
			meisai.find("#suuryouKigouText").remove();
		}
		// 往復フラグの表示有無は画面項目制御も考慮する
<c:if test='${ks.oufukuFlg.hyoujiFlg}'>
		meisai.find(".oufukuLabel").show();
		meisai.find("input[name=oufukuFlg]").show();
</c:if>
<c:if test="${enableHoujinCard}">
		meisai.find(".houjinCardLabel").show();
		meisai.find("input[name=houjinCardFlgRyohi]").show();
</c:if>
<c:if test="${enableKaishaTehai}">
		meisai.find(".kaishaTehaiLabel").show();
		meisai.find("input[name=kaishaTehaiFlgRyohi]").show();
</c:if>
	}
	// 数量入力タイプが「1:数量入力」の場合、単価はマスタに登録された値固定になるのでdisabled処理を行う
	if(suuryouNyuryokuType == "1"){
		var tanka = meisai.find("select[name=koutsuuShudan] :selected").attr("data-tanka");
		meisai.find("input[name=tanka]").val(tanka);
		meisai.find("input[name=tanka]").prop("disabled", true);
	}else if(suuryouNyuryokuType == "0" || suuryouNyuryokuType == "2"){
		meisai.find("input[name=tanka]").prop("disabled", false);
	}
}

/**
 * 単価項目、数量項目のautoNumericを変更する。
 * 以下のタイミングで呼ばれる。
 * ・ダイアログロード直後
 * ・交通手段変更時
 */
function changeAutoNumeric(){
	var suuryouNyuryokuType = meisai.find("input[name=suuryouNyuryokuType]").val();
	if(suuryouNyuryokuType == "1" || suuryouNyuryokuType == "2"){
		meisai.find("input[name=tanka]").autoNumeric('destroy');
		if(meisai.find("input[name=tanka]").hasClass("autoNumeric")){
			meisai.find("input[name=tanka]").removeClass("autoNumeric");
		}else if(meisai.find("input[name=tanka]").hasClass("autoNumericDecimalThree")){
			meisai.find("input[name=tanka]").removeClass("autoNumericDecimalThree");
		}
		meisai.find("input[name=tanka]").addClass("autoNumericDecimalThree");
		meisai.find("input.autoNumericDecimalThree").autoNumeric("init", {aPad: false, vMin:-999999999999.999, vMax:999999999999.999});
		// 数量項目の0埋めを無効にする。
		meisai.find("input[name=suuryou]").autoNumeric('destroy');
		meisai.find("input.autoNumericDecimal").autoNumeric("init", {aPad: false, vMin:-999999999999});
	}else if(suuryouNyuryokuType == "0"){
		if(meisai.find("input[name=tanka]").hasClass("autoNumericDecimalThree")){
			meisai.find("input[name=tanka]").autoNumeric('destroy');
			meisai.find("input[name=tanka]").removeClass("autoNumericDecimalThree");
			meisai.find("input[name=tanka]").addClass("autoNumeric");
			meisai.find("input.autoNumeric").autoNumeric("init", {aPad: false, vMin:-999999999999, vMax:999999999999});
		}
	}
}

/**
 * 交通費（乗換案内検索）→下に追加ボタン押下時Function
 */
function koutsuuhiAutoEntry() {
	// 乗換案内連携で乗車区間と金額が入っていなかったらエラー
	if("" == $("input[name=jousyaKukan]").val() || "" == $("input[name=norikaeKingaku]").val()) {
		alert("経路選択から乗車区間・金額を入力してください。");
		return false;
	}

	// 定期区間があれば備考に。
	var teikiBikou = "";
	if("1" == $("input[name=isIntra]").val() && "" != $("input[name=intraTeikiKukan]").val()) { // 駅すぱあとイントラ版の場合
		teikiBikou = "【定期区間】" +  $("input[name=intraTeikiKukan]").val();
	} else if("0" == $("input[name=isIntra]").val() && "" != $("textarea[name=webServiceTeiki]").val()) {// 駅すぱあとWebサービス版の場合
		teikiBikou = "【定期区間】" +  $("textarea[name=webServiceTeiki]").val();
	}

	//明細の中身反映
	meisai.find("input[name=shubetsu1]").val("交通費");
	meisai.find("span.shubetsu1").text(meisai.find("input[name=shubetsu1]").val());
	meisai.find("input[name=jidounyuuryokuFlg]").val("1");
	meisai.find("input[name=shubetsu2]").val("自動入力");
	meisai.find("span.shubetsu2").text(meisai.find("input[name=shubetsu2]").val());
	meisai.find("input[name=kikanFrom]").val($("input[name=norikaeKaishiBi]").val());
	//meisai.find("select[name=koutsuuShudan]").val(meisaiInfo["koutsuuShudan"]);
	meisai.find("input[name=ryoushuushoSeikyuushoTouFlg]").prop("checked", false);
	meisai.find("input[name=shouhyouShoruiHissuFlg]").val("0");

<c:if test="${teikikukanHaneisaki eq '1'}">
	//定期内容を内容欄に追加で反映
	meisai.find("textarea[name=naiyou]").val($("input[name=jousyaKukan]").val() + "\r\n" + teikiBikou);
	meisai.find("input[name=bikou]").val("");
</c:if>
<c:if test="${teikikukanHaneisaki ne '1'}">
		//定期内容を備考欄に反映
	meisai.find("textarea[name=naiyou]").val($("input[name=jousyaKukan]").val());
	meisai.find("input[name=bikou]").val(teikiBikou);
</c:if>

	meisai.find("input[name=tanka]").val($("input[name=norikaeKingaku]").val());

	//入力制御再実行
	inputSeigyo();

	// 乗換案内エリアのクリア
	$("input[name=norikaeKaishiBi]").val("");
	$("textarea[name=webServiceTeiki]").val("");
	$("input[name=intraTeikiKukan]").val("");
	$("input[name=intraTeikiRosen]").val("");
	$("input[name=from]").val("");
	$("input[name=to01]").val("");
	$("input[name=to02]").val("");
	$("input[name=to03]").val("");
	$("input[name=to04]").val("");
	$("input[name=to04]").val("");
	$("input[name=jousyaKukan]").val("");
	$("input[name=norikaeKingaku]").val("");
	$("#norikaeannai").hide();
}

/**
 * 交通費（乗換案内検索）ボタン押下時Function
 */
function norikaeannaiKensakuShow() {
	var width = "800";
	var height = "520";
	var cls = "";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
		cls = "norikaeKensakuPhone";
	}
	height = $(window).height();
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: height,
		title: "乗換案内検索",
		dialogClass: cls,
		close:function(e){$("#dialog").children().remove(); $(".expGuiConditionPc,.norikaeOverlay").remove();},
		buttons: {閉じる: function() {$(this).dialog("close");}}
	});
	$("#dialogMeisai").append('<div class="norikaeOverlay ui-widget-overlay ui-front" style="z-index: 100;"></div>');
	$("#dialog").load("norikae_annai_kensaku");
	return false;

}

/**
 * 過去明細ボタン押下時Function
 */
function kakoEntry() {
	var denpyouId = $("input[name=denpyouId]").val();
	var denpyouKbn = $("input[name=denpyouKbn]").val();
	var kaigaiFlgRyohi = meisai.find("input[name=kaigaiFlgRyohi]").val();
	var userId = $("input[name=userIdRyohi]").val();
	var kazeiFlgRyohi = meisai.find("input[name=kazeiFlgRyohi]").val();
	var kazeiFlgKamoku = meisai.find("input[name=kazeiFlgKamoku]").val();

	//小画面呼び出し → 小画面のスクリプトで明細の中身設定 & 反映
	var width = 800;
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: 520,
		title: "過去明細選択",
		appendTo:"#content",
		buttons: {閉じる: function() {$("#dialog").children().remove();$(this).dialog("close");}}
	})
	.load("ryohi_seisan_kakoMeisai_sentaku_kensaku?denpyouId=" + encodeURI(denpyouId) + "&denpyouKbn=" + encodeURI(denpyouKbn) + "&shubetsuCd=1" + "&kaigaiFlgRyohi=" + encodeURI(kaigaiFlgRyohi) + "&userId=" + encodeURI(userId) + "&kazeiFlgRyohi=" + encodeURI(kazeiFlgRyohi) + "&kazeiFlgKamoku=" + encodeURI(kazeiFlgKamoku));
}

//幣種選択ボタン押下時Function
$("#heishuSentakuButton").click(function(e){
	dialogRetHeishuCd = meisai.find("input[name=heishuCdRyohi]");
	dialogRetTsuukaTani = meisai.find("#tuukatani");
	dialogRetRate = meisai.find("input[name=rateRyohi]");
	commonHeishuSentaku();
	dialogCallbackHeishuSentaku = function() {
		calcGaikaMoney();
	};
});

//幣種コードロストフォーカス時Function
$("input[name=heishuCdRyohi]").blur(function(){
	var title = $(this).parent().parent().find(".control-label").text();
	dialogRetHeishuCd = meisai.find("input[name=heishuCdRyohi]");
	dialogRetTsuukaTani = meisai.find("#tuukatani");
	dialogRetRate = meisai.find("input[name=rateRyohi]");
	commonHeishuCdLostFocus(dialogRetHeishuCd, dialogRetTsuukaTani, dialogRetRate, title);
	dialogCallbackHeishuSentaku = function() {
		calcGaikaMoney();
	};
});

/**
 * 外貨入力による計算、単価を計算するFunction
 */
function calcGaikaMoney() {
	var target = $("#dialogMainDenpyouMeisai");
	var regInt = /^[0-9]+$/;
	var regDeci = /^[0-9]+\.[0-9]+$/;
	var gaikaVal = target.find("input[name=gaikaRyohi]").val().replaceAll(",", "");
	var rateVal = target.find("input[name=rateRyohi]").val();

	//外貨　決定：単価入力値が金額ならそれ。そうでなければ０。
	gaikaVal = gaikaVal.match(regInt) != null || gaikaVal.match(regDeci) != null ? new Decimal(gaikaVal) : new Decimal(0);

	//レート　決定：単価入力値が金額ならそれ。そうでなければ１。
	rateVal = rateVal.match(regInt) != null || rateVal.match(regDeci) != null ? new Decimal(rateVal) : new Decimal(1);

	// 邦貨換算端数処理
	var kingaku = gaikaVal.times(rateVal);
	<c:choose>
	<c:when test="${houkaKansanHansuu == '0'}">
	// 切り捨て
	kingaku = kingaku.floor();
	</c:when>
	<c:when test="${houkaKansanHansuu == '1'}">
	// 切り上げ
	kingaku = kingaku.ceil();
	</c:when>
	<c:when test="${houkaKansanHansuu == '2'}">
	// 四捨五入
	kingaku = kingaku.round();
	</c:when>
	</c:choose>
	//外貨×レートを単価にセット
	if(kingaku.comparedTo(MAX_DECIMAL) == 1) kingaku = MAX_DECIMAL;
	target.find("input[name=tanka]").val(kingaku.toString().formatMoney());

	calcKoutsuuhiMeisaiMoney();
}

/**
 * 明細の金額計算、金額の合計値を計算するFunction
 */
function calcKoutsuuhiMeisaiMoney() {
	var target = $("#dialogMainDenpyouMeisai");

	//単価　決定：単価入力値が金額ならそれ。そうでなければ０。
	var tankaVal = target.find("input[name=tanka]").val().replaceAll(",", "");
	tankaVal = $.isNumeric(tankaVal) ? Number(tankaVal) : 0;
	//単位数決定
	var tanisuuVal = target.find("input[name=oufukuFlg]").prop("checked") ? 2 : 1;

	//国内旅費交通費明細で、数量が入力されている場合、単価×数量を求める。(端数は切り上げ)
	if(meisai.find("input[name=kaigaiFlgRyohi]").val() == "0"){
		var suuryouNyuryokuType = meisai.find("input[name=suuryouNyuryokuType]").val();
		if(suuryouNyuryokuType != 0){
			var suuryou = target.find("input[name=suuryou]");
			if(suuryou.val() != ""){
				var suuryouVal = "";
				suuryouVal = suuryou.val().replaceAll(",", "");
				tankaVal = tankaVal * suuryouVal;
			}else{
				// 数量が未入力の場合、金額は0固定となるので計算用の単価を0にしておく
				tankaVal = 0;
			}
		}
	}

	//単価?単位数を金額にセット
	target.find("input[name=meisaiKingaku]").val(String(Math.ceil(tankaVal * tanisuuVal)).formatMoney());
<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
	meisaiKingakuShusei()
</c:if>
}

/**
 * 前の明細情報の表示
 */
function meisaiPrevious(event) {

	var index = event.data.index;
	var maxIndex = event.data.maxIndex;
	var shubetsuCd = event.data.shubetsuCd;

	// 前のインデックスへシフト
	index = parseInt(index) - 1;

	// 最小インデックスを下回る場合、インデックスを最小インデックスに留める
	if (index < 0) {
		index = 0;
	}

	// 明細情報の取得
	var meisaiInfo = getRyohiMeisaiInfo(index, shubetsuCd);

	// ダイアログの再表示
	$("#dialogMeisai").dialog("close");

	// 海外旅費・国内旅費による明細ダイアログの振り分け
	if (meisai.find("input[name=kaigaiFlgRyohi]").val() == "1") {
		kaigaiRyohiMeisaiDialogOpen(index, shubetsuCd, meisaiInfo);
	}
	else {
		ryohiMeisaiDialogOpen(index, shubetsuCd, meisaiInfo);
	}
}

/**
 * 次の明細情報の表示
 */
function meisaiNext(event) {

	var index = event.data.index;
	var maxIndex = event.data.maxIndex;
	var shubetsuCd = event.data.shubetsuCd;

	// 次のインデックスへシフト
	index = parseInt(index) + 1;

	// 最大インデックスを上回る場合、インデックスを最大インデックスに留める
	if (index > maxIndex) {
		index = maxIndex;
	}

	// 明細情報の取得
	var meisaiInfo = getRyohiMeisaiInfo(index, shubetsuCd);

	// ダイアログの再表示
	$("#dialogMeisai").dialog("close");
	// 海外旅費・国内旅費による明細ダイアログの振り分け
	if (meisai.find("input[name=kaigaiFlgRyohi]").val() == "1") {
		kaigaiRyohiMeisaiDialogOpen(index, shubetsuCd, meisaiInfo);
	}
	else {
		ryohiMeisaiDialogOpen(index, shubetsuCd, meisaiInfo);
	}
}

/**
 * 法人カード・会社手配はどちらか１つのみ選択できるようにする
 */
function checkboxSeigyo() {
	var flg = $(this).prop('checked');
	$(".check-group").prop('checked', false);
	if (flg) {
		$(this).prop('checked', true);
	}
}

//以下、初期化処理

//入力補助
commonInit(meisai);

//入力制御
inputSeigyo();

//交通手段の変更時、領収書・請求書等の表示切替
meisai.find("select[name=koutsuuShudan]").change(function(){
	displayryoushuushoSeikyuushoTou();
	getKazeiFlg();
	if(meisai.find("input[name=kaigaiFlgRyohi]").val() == "0"){
		var oldSuuryouNyuryokuType = meisai.find("input[name=suuryouNyuryokuType]").val(); //切替前の数量入力フラグ
		judgeInitSuuryou();
		changeAutoNumeric();
		meisai.find("input[name=suuryou]").val("");
		var jidouFlg = (meisai.find("input[name=icCardNo]").val() != "" || meisai.find("input[name=himodukeCardMeisaiRyohi]").val() != "" || meisai.find("input[name=jidounyuuryokuFlg]").val() == "1");
		if(!jidouFlg){			///IC・法人カード履歴から登録、駅すぱあと連携により入力した交通費なら単価クリアさせない
			var newSuuryouNyuryokuType = meisai.find("input[name=suuryouNyuryokuType]").val(); //切替後の数量入力フラグ
			if(oldSuuryouNyuryokuType != "0" && newSuuryouNyuryokuType == "0" || newSuuryouNyuryokuType == "2"){
				meisai.find("input[name=tanka]").val("");
			}
		}
	}
	calcKoutsuuhiMeisaiMoney();
});

<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
/**
* 消費税額を手入力で修正した際に税抜金額を自動で再計算
*/
function shouhizeigakuShusei(){
	var target = $("#dialogMainDenpyouMeisai");
	var shouhizeigaku = target.find("input[name=shouhizeigaku]").val().replaceAll(",", "");
	var meisaiKingaku = target.find("input[name=meisaiKingaku]").val().replaceAll(",", "");

	target.find("input[name=zeinukiKingaku]").val((meisaiKingaku - shouhizeigaku).toString().formatMoney());

	// 手動修正フラグのセット
	meisai.find("input[name=zeigakuFixFlg]").val("1");
}

/**
* 支払金額を手入力で修正した際に自動で再計算(全伝票で使用) 
*/
function meisaiKingakuShusei(){
	let target = $("#dialogMainDenpyouMeisai");
	let kazeiKbn = $("select[name=kazeiKbnRyohi] :selected").val();
	let zeiritsu = Number(["001", "002", "011", "012", "013", "014"].includes(kazeiKbn) 
			? $("#zeiritsuRyohi").find("option:selected").val()
			: 0);
	let hasuuShoriFlg = $("#workflowForm").find("input[name=hasuuShoriFlg]").val();
	let meisaiKingaku = Number(target.find("input[name=meisaiKingaku]").val().replaceAll(",", ""));
	let jigyoushaFlg = target.find("input[name=jigyoushaKbn]:checked").val() > 0 ? target.find("input[name=jigyoushaKbn]:checked").val() : "0";
	let shiireKeikaSothiFlg = $("#workflowForm").find("input[name=shiirezeigakuKeikasothi]").val();
	
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
	target.find("input[name=zeinukiKingaku]").val(zeinukiKingaku.toString().formatMoney());
	target.find("input[name=shouhizeigaku]").val(shouhizeigaku?.toString()?.formatMoney());
}
</c:if>

//レートによる外貨の計算
meisai.find("input[name=gaikaRyohi]").blur(calcGaikaMoney);
meisai.find("input[name=rateRyohi]").blur(calcGaikaMoney);

//単価、往復（旅費）、日数（その他）の変更時、金額計算
meisai.find("input[name=oufukuFlg]").click(calcKoutsuuhiMeisaiMoney);
meisai.find("input[name=nissuu]").blur(calcKoutsuuhiMeisaiMoney);
meisai.find("input[name=tanka]").blur(calcKoutsuuhiMeisaiMoney);
meisai.find("input[name=suuryou]").blur(calcKoutsuuhiMeisaiMoney);

//法人カード・会社手配はどちらか１つのみ選択できるようにする
meisai.find("input[name=houjinCardFlgRyohi]").click(checkboxSeigyo);
meisai.find("input[name=kaishaTehaiFlgRyohi]").click(checkboxSeigyo);

<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
//各種金額 手入力で修正した際に自動で再計算
meisai.find("input[name=shouhizeigaku]").blur(shouhizeigakuShusei);
meisai.find("input[name=meisaiKingaku]").blur(meisaiKingakuShusei);

/**
 * 消費税額修正ボタン押下時Function
 */
$("#zeiShuseiBtn").click(function(e) {
	var meisai = $("#dialogMainDenpyouMeisai");
	var flg = meisai.find("input[name=shouhizeigaku]").prop('disabled');
	meisai.find("input[name=shouhizeigaku]").prop("disabled", !flg);
	if(flg){
		addCalculator(meisai.find("input[name=shouhizeigaku]"));
	}else{
		delCalculator(meisai.find("input[name=shouhizeigaku]"));		
	}
});

/**
 * 事業者区分Function
 */
$("input[name=jigyoushaKbn]").change(function(e) {
	meisaiKingakuShusei();
});
</c:if>

/**
 * 経路選択子画面で選択した時下へ追加を自動で
 */
routeSentakuCallback = function() {
	koutsuuhiAutoEntry();
};
</script>