<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- エラー -->
<input type='hidden' id="errorCnt" value='${fn:length(errorList)}'>
<%@ include file="/jsp/eteam/include/InputError.jsp" %>

<!-- メイン -->
<div id='dialogMainDenpyouMeisai'>
	<input type='hidden' name="dispMode" value='${su:htmlEscape(dispMode)}'>
	<input type="hidden" name='shouhyouShoruiHissuFlg' value='${su:htmlEscape(shouhyouShoruiHissuFlg)}'>
	<input type='hidden' name='ryoushuushoSeikyuushoDefault' value='${su:htmlEscape(ryoushuushoSeikyuushoDefault)}'>
	<input type='hidden' name='isNewMeisai' value='${su:htmlEscape(isNewMeisai)}'>
	<input type="hidden" name='index' value='${su:htmlEscape(index)}'>
	<input type="hidden" name='maxIndex' value='${su:htmlEscape(maxIndex)}'>
	<input type='hidden' name='zeroEnabled'			value='' >
	<input type='hidden' name='userId'				value='' >
	<input type='hidden' name='kaigaiFlgRyohi'		value='${su:htmlEscape(kaigaiFlgRyohi)}' >
	<input type='hidden' name='kazeiFlgRyohi'		value='${su:htmlEscape(kazeiFlgRyohi)}'>
	<input type='hidden' name='kazeiFlgKamoku'		value='${su:htmlEscape(kazeiFlgKamoku)}'>
	<input type='hidden' name='zeiKubun'			value='${su:htmlEscape(zeiKubun)}'>
	<input type='hidden' name='kazeiFlg'			value='' >
	<input type='hidden' name='nittouFlg'			value='' >
		
	<!-- ボタン -->
	<section>
		<button type='button' class='btn btn-small' name = 'kakoEntryButton' onclick="kakoEntry()">過去明細</button>
	</section>

	<!-- 入力フィールド -->
	<section class='print-unit form-horizontal'>
		<h2 style='line-height: 30px;<c:if test='${null eq index or "" eq index}'> display:none</c:if>'>
			<span>${su:htmlEscape(index + 1)} / ${su:htmlEscape(maxIndex + 1)}
				<button type='button' name='btnPrevious' class='btn' style='position: absolute; right: 90px; font-size: 14px;'><i class='icon-arrow-left'></i> 前へ</button>
				<button type='button' name='btnNext' class='btn' style='position: absolute; right: 10px;  font-size: 14px;'>次へ <i class='icon-arrow-right'></i></button>
			</span>
		</h2>
		<div>
			<!-- 種別 -->
			<div class='control-group' <c:if test='${not ks.shubetsu1.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.shubetsu1.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shubetsu1.name)}</label>
				<div class='controls'>
					<select name='shubetsu1' class='input-medium'>
<c:forEach var="shubetsu1Value" items="${shubetsu1List}">
						<option value='${su:htmlEscape(shubetsu1Value)}' <c:if test='${shubetsu1 eq shubetsu1Value}'>selected</c:if>>${su:htmlEscape(shubetsu1Value)}</option>
</c:forEach>
					</select>
				<label class='label'><c:if test='${ks.shubetsu2.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shubetsu2.name)}</label>
					<select name='shubetsu2' class='input-medium'>
		<c:forEach var="shubetsu2Value" items="${shubetsu2List}">
						<option value='${su:htmlEscape(shubetsu2Value.shubetsu2)}' data-nittouShukuhakuhiFlg='${shubetsu2Value.nittou_shukuhakuhi_flg}' data-shouhyouShoruiHissuFlg='${shubetsu2Value.shouhyou_shorui_hissu_flg}' <c:if test='${shubetsu2 eq shubetsu2Value.shubetsu2}'>selected</c:if>>${su:htmlEscape(shubetsu2Value.shubetsu2)}</option>
		</c:forEach>
					</select>
				</div>
			</div>
			<!-- 期間 -->
			<div class='control-group' <c:if test='${not ks.kikan.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.kikan.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kikan.name)}</label>
				<div class='controls'>
					<input type="text" style="width: 0; height: 0; top: -100px; position: absolute;"/>
					<input type='text' name='kikanFrom' class='date input-small datepicker' value='${su:htmlEscape(kikanFrom)}'> ～ 
					<input type='text' name='kikanTo' class='date input-small datepicker' value='${su:htmlEscape(kikanTo)}'>
					<label class='label' <c:if test='${not ks.kyuujitsuNissuu.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.kyuujitsuNissuu.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kyuujitsuNissuu.name)}</label>
					<input type='text' name='kyuujitsuNissuu' class='input-mini' maxlength='5' value='${su:htmlEscape(kyuujitsuNissuu)}' <c:if test='${not ks.kyuujitsuNissuu.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.kyuujitsuNissuu.hyoujiFlg}'>日</c:if>
				</div>
				
			</div>
<c:if test='${shouhyouShoruiHissuFlg eq "1" and denpyouKbn ne "A005"}'>
			<!-- 領収書・請求書等 -->
			<div class='control-group' <c:if test='${not ks.ryoushuushoSeikyuushoTouFlg.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label' for='ryoushuushoSeikyuushoTouFlg'><c:if test='${ks.ryoushuushoSeikyuushoTouFlg.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.ryoushuushoSeikyuushoTouFlg.name)}</label>
				<div class='controls'>
					<input type='checkbox' name='ryoushuushoSeikyuushoTouFlg' <c:if test='${"1" eq ryoushuushoSeikyuushoTouFlg}'>checked</c:if> value="1">
				</div>
			</div>
</c:if>
			<!-- 内容 -->
			<div class='control-group' <c:if test='${not ks.naiyouNittou.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.naiyouNittou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.naiyouNittou.name)}</label>
				<div class='controls'>
					<textarea name='naiyou' class='input-block-level' maxlength='512'>${su:htmlEscape(naiyou)}</textarea>
				</div>
			</div>
			<!-- 備考 -->
			<div class='control-group' <c:if test='${not ks.bikouNittou.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.bikouNittou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.bikouNittou.name)}</label>
				<div class='controls'>
					<input type='text' name='bikou' class='input-block-level' value='${su:htmlEscape(bikou)}' maxlength='40'>
				</div>
			</div>
			<!-- 幣種・レート -->
<c:if test='${enableGaika}'>
			<div class='control-group' <c:if test='${not (ks.heishu.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.heishu.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.heishu.name)}</label>
				<div class='controls'>
					<input type='text' class='input-medium' name='heishuCdRyohi' maxlength='4' value='${su:htmlEscape(heishuCdRyohi)}' <c:if test='${heishuKotei}'>disabled</c:if>>
					<button type='button' id='heishuSentakuButton' class='btn btn-small' <c:if test='${heishuKotei}'>disabled</c:if>>選択</button>
					<label class='label' <c:if test='${not ks.rate.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.rate.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.rate.name)}</label>
					<input type='text' class='input-medium' name='rateRyohi' value='${su:htmlEscape(rateRyohi)}' <c:if test='${heishuKotei}'>disabled</c:if>>
				</div>
			</div>
			<!-- 外貨 -->
			<div class='control-group' <c:if test='${not (ks.gaika.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.gaika.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.gaika.name)}</label>
				<div class='controls'>
					<input type='text' name='gaikaRyohi' class='input-medium autoNumericDecimal' value='${su:htmlEscape(gaikaRyohi)}' <c:if test='${gaikaKotei}'>disabled</c:if>><span id='tuukatani'>${su:htmlEscape(taniRyohi)}</span>
				</div>
			</div>
</c:if>
			<!-- 単価 -->
			<div class='control-group dialogKotei' <c:if test='${not ks.tanka.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.tanka.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.tanka.name)}</label>
				<div class='controls'>
					<input type='text' class='money input-medium autoNumeric' name="tanka" <c:if test='${tankaKotei}'>disabled</c:if> value='${su:htmlEscape(tanka)}'>円
					<span id=nissuuArea>
					<label class='label' <c:if test='${not ks.nissuu.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.nissuu.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.nissuu.name)}</label>
					<input type='text' name='nissuu' class='input-mini' maxlength='5' value='${su:htmlEscape(nissuu)}' <c:if test='${not ks.nissuu.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.nissuu.hyoujiFlg}'>日</c:if>
					</span>
					<label class='label' <c:if test='${not ks.ninzuu.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.ninzuu.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.ninzuu.name)}</label>
					<input type='text' name='ninzuu' class='input-mini' maxlength='2'  value='${su:htmlEscape(ninzuu)}' <c:if test='${not ks.ninzuu.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.ninzuu.hyoujiFlg}'>人</c:if>
				</div>
			</div>
			<div class='control-group dialogKotei'>
			</div>
			<!-- 金額 -->
			<div class='control-group' <c:if test='${not ks.meisaiKingaku.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.meisaiKingaku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.meisaiKingaku.name)}</label>
				<div class='controls'>
					<input type='text' name='meisaiKingaku' class='money input-medium autoNumeric' disabled>円
<c:if test="${enableHoujinCard}">
						<!-- 法人カード -->
						<label class='label' id='houjinCardFlgRyohiName' <c:if test='${not ks.houjinCardRiyou.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.houjinCardRiyou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.houjinCardRiyou.name)}</label>
						<input class='check-group' type='checkbox' name='houjinCardFlgRyohi' <c:if test='${"1" eq houjinCardFlgRyohi}'>checked</c:if> value="1" <c:if test='${not ks.houjinCardRiyou.hyoujiFlg}'>style='display:none;'</c:if>>
</c:if>
<c:if test="${enableKaishaTehai}">
						<!-- 会社手配 -->
						<label class='label' id='kaishaTehaiFlgRyohiName' <c:if test='${not ks.kaishaTehai.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.kaishaTehai.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kaishaTehai.name)}</label>
						<input class='check-group' type='checkbox' name='kaishaTehaiFlgRyohi' <c:if test='${"1" eq kaishaTehaiFlgRyohi}'>checked</c:if> value="1" <c:if test='${not ks.kaishaTehai.hyoujiFlg}'>style='display:none;'</c:if>>
</c:if>
						<input type='hidden' name='himodukeCardMeisaiRyohi'	value='${su:htmlEscape(himodukeCardMeisaiRyohi)}'>
				</div>
			</div>
<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
			<!-- 消費税額(うち消費税額) -->
			<div class='control-group invoiceOnly' name='shouhizeigaku'<c:if test='${not (ks.shouhizeigaku.hyoujiFlg && kaigaiFlgRyohi eq "0")}'>style='display:none;'</c:if>>
				<label class='control-label'>${su:htmlEscape(ks.shouhizeigaku.name)}</label>
				<div class='controls'>
					<input type='text' name='shouhizeigaku' id='shouhizeigaku'	class='input-medium autoNumericWithCalcBox' value='${su:htmlEscape(shouhizeigaku)}'>円
					<button class="btn btn-small" id='zeiShuseiBtn' name='zeiShuseiBtn'>修正</button>
					<input type='hidden' name='zeinukiKingaku' value='${su:htmlEscape(zeinukiKingaku)}'>
					<input type='hidden' name='zeigakuFixFlg' value='${su:htmlEscape(zeigakuFixFlg)}'>
				</div>
			</div>
			<!-- 支払先名 -->
			<div id='shiharaisakidiv' class='control-group invoiceOnly'>
				<label class='control-label' <c:if test='${not ks.shiharaisaki.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shiharaisaki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaisaki.name)}</label>
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
var shubetsu1Sel = meisai.find("select[name=shubetsu1]");
var shubetsu2Sel = meisai.find("select[name=shubetsu2]");

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
		kaigaiFlgRyohi : meisai.find("input[name=kaigaiFlgRyohi]").val() == "1" ? "1" : "0",
		dispMode : meisai.find("input[name=dispMode]").val(),
		kazeiFlgRyohi : meisai.find("input[name=kazeiFlgRyohi]").val(),
		kazeiFlgKamoku : meisai.find("input[name=kazeiFlgKamoku]").val(),
		
		shubetsuCd : "2",
		shubetsu1 : shubetsu1Sel.val(),
		shubetsu2 : shubetsu2Sel.val(),
		kikanFrom : meisai.find("input[name=kikanFrom]").val(),
		kikanTo : meisai.find("input[name=kikanTo]").val(),
		koutsuuShudan : "",
 		shouhyouShoruiHissuFlg : meisai.find("input[name=shouhyouShoruiHissuFlg]").val(),
		ryoushuushoSeikyuushoTouFlg : (meisai.find("input[name=ryoushuushoSeikyuushoTouFlg]:checked").length == 1) ? "1" : "0",
		houjinCardFlgRyohi : (meisai.find("input[name=houjinCardFlgRyohi]:checked").length == 1) ? "1" : "0",
		kaishaTehaiFlgRyohi : (meisai.find("input[name=kaishaTehaiFlgRyohi]:checked").length == 1) ? "1" : "0",
		naiyou : meisai.find("textarea[name=naiyou]").val(),
		bikou : meisai.find("input[name=bikou]").val(),
		oufukuFlg : "",
		jidounyuuryokuFlg : "",
		kyuujitsuNissuu : meisai.find("input[name=kyuujitsuNissuu]").val(),
		nissuu : meisai.find("input[name=nissuu]").val(),
		ninzuu : meisai.find("input[name=ninzuu]").val(),
		heishuCdRyohi : meisai.find("input[name=heishuCdRyohi]").val(),
		rateRyohi : meisai.find("input[name=rateRyohi]").val(),
		gaikaRyohi : meisai.find("input[name=gaikaRyohi]").val(),
		taniRyohi : meisai.find("#tuukatani").text(),
		tanka : meisai.find("input[name=tanka]").val(),
		meisaiKingaku : meisai.find("input[name=meisaiKingaku]").val(),
		zeiKubun : meisai.find("input[name=zeiKubun]").val(),
		kazeiFlgRyohiMeisai : meisai.find("input[name=kazeiFlg]").val(),
		nittouFlg : meisai.find("input[name=nittouFlg]").val(),
		himodukeCardMeisaiRyohi : meisai.find("input[name=himodukeCardMeisaiRyohi]").val(),

<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
		shouhizeigaku : meisai.find("input[name=shouhizeigaku]").val(),
		zeinukiKingaku : meisai.find("input[name=zeinukiKingaku]").val(),
		shiharaisaki : meisai.find("input[name=shiharaisakiName]").val(),
		jigyoushaKbn : meisai.find("input[name=jigyoushaKbn]:checked").val() == "1" ? "1": "0",
		jigyoushaKbnText : meisai.find("input[name=jigyoushaKbn] :checked").text(),
		zeigakuFixFlg : meisai.find("input[name=zeigakuFixFlg]").val(),
</c:if>
		
 		isNewMeisai : meisai.find("input[name=isNewMeisai]").val()
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
 * ダイアログ表示時に値をセットする。
 * セットする値はメイン画面のhidden項目をMAPで渡す。
 * @param isEnable(boolewan)	更新モードならtrue
 * @param meisaiInfo(MAP)		メイン画面のhidden情報(追加ならnull)
 */
function setDialogMeisaiInfo(isEnable, meisaiInfo) {
	
	//インボイス対応前の場合、インボイス対応項目を非表示とする
	var hyoujiFlg = $("#denpyouJouhou").find("select[name=invoiceDenpyou]").val();
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

	//追加処理の再表示 or 変更、参照処理
	if (meisaiInfo != null) {
		
		//既にないマスターでも参照モードなら無理やりプルダウンにつめる
		putNittouShubetsu(1, meisaiInfo["shubetsu1"]);
		putNittouShubetsu(2, meisaiInfo["shubetsu2"]);
		if (shubetsu2Sel.val() != "") shubetsu2Sel.css("display", "inline");
		
		if(!meisaiInfo["kikanFrom"]) {
			meisai.find("input[name=kikanFrom]").val(getKikanFrom().val());
		}
		else {
			meisai.find("input[name=kikanFrom]").val(meisaiInfo["kikanFrom"]);
		}
		if(!meisaiInfo["kikanTo"]) {
			meisai.find("input[name=kikanTo]").val(getKikanTo().val());
		}
		else {
			meisai.find("input[name=kikanTo]").val(meisaiInfo["kikanTo"]);
		}
		
		meisai.find("input[name=isNewMeisai]").val(meisaiInfo["isNewMeisai"]);
		
		meisai.find("input[name=shouhyouShoruiHissuFlg]").val(meisaiInfo["shouhyouShoruiHissuFlg"]);
		// 法人カード履歴から追加のときは証憑書類必須フラグが未設定なので選択されている種別から取得
 		if(meisaiInfo["shubetsu1"] == ""){
			meisai.find("input[name=shouhyouShoruiHissuFlg]").val(meisai.find("select[name=shubetsu2] option:selected").attr("data-shouhyouShoruiHissuFlg"));
		}

		if (meisai.find("input[name=shouhyouShoruiHissuFlg]").val() == "1"){
			if (meisai.find("input[name=isNewMeisai]").val() == "true"
					&& meisai.find("input[name=ryoushuushoSeikyuushoDefault]").val() == "1"){
				meisai.find("input[name=ryoushuushoSeikyuushoTouFlg]").prop("checked",true);
			}
		} else {
			meisai.find("input[name=ryoushuushoSeikyuushoTouFlg]").prop("checked",false);
		}
		
		meisai.find("input[name=kaigaiFlgRyohi]").val(meisaiInfo["kaigaiFlgRyohi"]);
		meisai.find("input[name=houjinCardFlgRyohi]").prop("checked", ("1" == meisaiInfo["houjinCardFlgRyohi"]));
		meisai.find("input[name=kaishaTehaiFlgRyohi]").prop("checked", ("1" == meisaiInfo["kaishaTehaiFlgRyohi"]));
		meisai.find("textarea[name=naiyou]").val(meisaiInfo["naiyou"]);
		meisai.find("input[name=bikou]").val(meisaiInfo["bikou"]);
		meisai.find("input[name=kyuujitsuNissuu]").val(meisaiInfo["kyuujitsuNissuu"]);
		meisai.find("input[name=heishuCdRyohi]").val(meisaiInfo["heishuCdRyohi"]);
		meisai.find("input[name=rateRyohi]").val(meisaiInfo["rateRyohi"]);
		meisai.find("input[name=gaikaRyohi]").val(meisaiInfo["gaikaRyohi"]);
		meisai.find("#tuukatani").text(meisaiInfo["taniRyohi"]);
		meisai.find("input[name=tanka]").val(meisaiInfo["tanka"]);
		meisai.find("input[name=nissuu]").val(meisaiInfo["nissuu"]);
		meisai.find("input[name=ninzuu]").val(meisaiInfo["ninzuu"]);
		
		meisai.find("input[name=zeiKubun]").val(meisaiInfo["zeiKubun"]);
		meisai.find("input[name=kazeiFlg]").val(meisaiInfo["kazeiFlgRyohiMeisai"]);
		meisai.find("input[name=kazeiFlgRyohi]").val(meisaiInfo["kazeiFlgRyohi"]);
		meisai.find("input[name=kazeiFlgKamoku]").val(meisaiInfo["kazeiFlgKamoku"]);
		meisai.find("input[name=nittouFlg]").val(meisaiInfo["nittouFlg"]);

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
	
	// 種別2による表示有無
	shubetsuSentaku();
}

/**
 * 種別１・２の変更時にinitイベントを実行（単価、課税フラグ再取得の為）
 */
function reloadDialog() {
	var tankaVal = meisai.find("input[name=tanka]").val();		// 退避（後で戻す）
	var meisaiKingakuVal = meisai.find("input[name=meisaiKingaku]").val();	//退避（後で戻す）
	var himodukeCard = meisai.find("input[name=himodukeCardMeisaiRyohi]").val();
	meisai.find("input[name=tanka]").val("");
	var url = "ryohi_seisan_shukuhakuryouhoka_nyuuryoku_init";
	
<c:if test='${enableGaika}'>
	//外貨が残っていると変更後に固定単価が表示されなくなるのまとめて初期化
	meisai.find("input[name=heishuCdRyohi]").val("");
	meisai.find("input[name=rateRyohi]").val("");
	meisai.find("input[name=gaikaRyohi]").val("");
	meisai.find("#tuukatani").text("");
</c:if>

	var data = getDialogMeisaiInfo();
	$("#dialogMeisai").load(url, data, function() {
		// 再取得後のデータで明細表示
		data = getDialogMeisaiInfo();
		setDialogMeisaiInfo(true, data);
		
		//法人カード履歴からの転記明細は単価と明細金額を元に戻す
		if(himodukeCard != ""){ 
			meisai.find("input[name=tanka]").val(tankaVal);
			meisai.find("input[name=meisaiKingaku]").val(meisaiKingakuVal);
		}
	});
	//課税フラグの取得
	getKazeiFlg();
	

}

//幣種選択ボタン押下時Function
$("#heishuSentakuButton").click(function(e){
	dialogRetHeishuCd = meisai.find("input[name=heishuCdRyohi]");
	dialogRetTsuukaTani = meisai.find("#tuukatani");
	dialogRetRate = meisai.find("input[name=rateRyohi]");
	dialogCallbackHeishuSentaku = function() {
		calcGaikaMoney();
	};
	commonHeishuSentaku();
});

//幣種コードロストフォーカス時Function
$("input[name=heishuCdRyohi]").blur(function(){
	var title = $(this).parent().parent().find(".control-label").text();
	var heishuCdRyohi  = meisai.find("input[name=heishuCdRyohi]").val();
	dialogRetHeishuCd = meisai.find("input[name=heishuCdRyohi]");
	dialogRetTsuukaTani = meisai.find("#tuukatani");
	dialogRetRate = meisai.find("input[name=rateRyohi]");
	dialogCallbackHeishuSentaku = function() {
		calcGaikaMoney();
	};
	commonHeishuCdLostFocus(dialogRetHeishuCd, dialogRetTsuukaTani, dialogRetRate, title);
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
	
	if(gaikaVal != "" || rateVal != ""){
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
		
		calcNittouMeisaiMoney();
	}
}

/**
 * 過去明細ボタン押下時Function
 */
function kakoEntry() {
	var denpyouId = $("input[name=denpyouId]").val();
	var denpyouKbn = $("input[name=denpyouKbn]").val();
	var kaigaiFlg = meisai.find("input[name=kaigaiFlgRyohi]").val();
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
	.load("ryohi_seisan_kakoMeisai_sentaku_kensaku?denpyouId=" + encodeURI(denpyouId) + "&denpyouKbn=" + encodeURI(denpyouKbn) + "&shubetsuCd=2" + "&kaigaiFlgRyohi=" + encodeURI(kaigaiFlg) + "&userId=" + encodeURI(userId) + "&kazeiFlgRyohi=" + encodeURI(kazeiFlgRyohi) + "&kazeiFlgKamoku=" + encodeURI(kazeiFlgKamoku));
}

/**
 * 日当合計計算
 * @return 正しく金額計算できた（入力状態が正しい)
 */
function calcNittouMeisaiMoney() {
	var tankaVal = meisai.find("input[name=tanka]").val().replaceAll(",", "");
	var nissuuVal = meisai.find("input[name=nissuu]").val();
	var ninzuuVal = meisai.find("input[name=ninzuu]").val();

	//単価が金額でなければ駄目
	if (isNaN(tankaVal)) {
		meisai.find("input[name=meisaiKingaku]").val("");
		return;
	}
	//日数が正の数値でなければ駄目
	if (isNaN(nissuuVal)) {
		meisai.find("input[name=meisaiKingaku]").val("");
		return;
	} else if (nissuuVal < 0 ) {
		meisai.find("input[name=meisaiKingaku]").val("");
		return;
	}
	
	//人数が正の数値でなければ1人として換算
 	if (isNaN(ninzuuVal)) ninzuuVal = 1;
 	else if (ninzuuVal < 0 ) ninzuuVal = 1;
 	else if ("" == ninzuuVal) ninzuuVal = 1;
	
	meisai.find("input[name=meisaiKingaku]").val(String(Math.ceil( Number(tankaVal) * Number(nissuuVal) * Number(ninzuuVal))).formatMoney());
<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
	meisaiKingakuShusei();
</c:if>
}

 /**
  * 日当合計計算（休日日数の入った場合）
  * @return 正しく金額計算できた（入力状態が正しい)
  */
 function calcKikanMeisaiMoney() {
 	var tankaVal = meisai.find("input[name=tanka]").val().replaceAll(",", "");
 	var nissuuVal = meisai.find("input[name=nissuu]").val();
 	var ninzuuVal = meisai.find("input[name=ninzuu]").val();
 	var fromDate = meisai.find("input[name=kikanFrom]").val();
 	var toDate = meisai.find("input[name=kikanTo]").val();
 	
 	//単価が金額でなければ駄目
 	if (! tankaVal.isInteger()) {
 		meisai.find("input[name=meisaiKingaku]").val("");
 		return;
 	}
 	
<c:if test='${empty himodukeCardMeisaiRyohi}'>
 	//日数の取得
 	if (!/^\d{4}\/\d{2}\/\d{2}$/.test(fromDate)) return;
 	if (!/^\d{4}\/\d{2}\/\d{2}$/.test(toDate)) return;
 	var kikanFrom = new Date(fromDate);
 	var kikanTo = new Date(toDate);
 	nissuuVal = Math.floor((kikanTo.getTime() - kikanFrom.getTime()) / (1000 * 60 * 60 *24));
 	
 	//休日日数の取得
 	var kyuujitsuNissuuVal = meisai.find("input[name=kyuujitsuNissuu]").val();
 	if (isNaN(kyuujitsuNissuuVal)) kyuujitsuNissuuVal = 0;
 	else if (kyuujitsuNissuuVal < 0) kyuujitsuNissuuVal = 0;
 	else if ("" == kyuujitsuNissuuVal) kyuujitsuNissuuVal = 0;
 	
 	//日数-休日日数を日数項目に入れる
 	nissuuVal = ++nissuuVal - kyuujitsuNissuuVal;
 	meisai.find("input[name=nissuu]").val(nissuuVal);
</c:if> 	
 	//人数が正の数値でなければ1人として換算
 	if (isNaN(ninzuuVal)) ninzuuVal = 1;
 	else if (ninzuuVal < 0 ) ninzuuVal = 1;
 	else if ("" == ninzuuVal) ninzuuVal = 1;
 	
 	meisai.find("input[name=meisaiKingaku]").val(String(Math.ceil( Number(tankaVal) * Number(nissuuVal) * Number(ninzuuVal))).formatMoney());
<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
	meisaiKingakuShusei();
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
 * 種別の変更
 * @param no	番号(1 or 2)
 * @param s		選択する値
 * @param force	プルダウンに候補値がなくてもoptionを追加する？
 */
function putNittouShubetsu(no, s, force) {
	if (dispMode == 3) force = true;
	s = s.replaceAll(" ", NBSP);
	var sFound = false;
	var shubetsuSel = (no == 1) ? shubetsu1Sel : shubetsu2Sel;
	shubetsuSel.find("option").each(function() {
		if ($(this).val() == s) {
			$(this).prop("selected", true);
			sFound = true;
		}
	});
	if (! sFound && force) {
		//既にないマスターだが無理やり表示させる
		var newOption = $("<option>").val(s).text(s);
		shubetsuSel.append(newOption);
		newOption.prop("selected", true);
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

/**
 * 選択された種別による法人カード・会社手配の表示制御Function
 */
 function shubetsuSentaku() {
	var nittouShukuhakuhiFlg = meisai.find("select[name=shubetsu2] option:selected").attr("data-nittouShukuhakuhiFlg");
	if("2" == nittouShukuhakuhiFlg){
		// チェックついている場合は外す
		if(meisai.find("input[name=houjinCardFlgRyohi]").prop('checked')){ meisai.find("input[name=houjinCardFlgRyohi]").prop('checked', false); }
		if(meisai.find("input[name=kaishaTehaiFlgRyohi]").prop('checked')){ meisai.find("input[name=kaishaTehaiFlgRyohi]").prop('checked', false); }
		// 法人カード利用、会社手配を非表示
		$("#houjinCardFlgRyohiName").hide();
		$("#kaishaTehaiFlgRyohiName").hide();
		meisai.find("input[name=houjinCardFlgRyohi]").hide();
		meisai.find("input[name=kaishaTehaiFlgRyohi]").hide();
		
		//日当フラグをセット
		meisai.find("input[name=nittouFlg]").val("1");
		  // ラジオボタンの値が0のものを選択する
		  $("[name=jigyoushaKbn][value=0]").prop("checked", true);
		  // ラジオボタンの値が1のものはdisabledにする
		  $("[name=jigyoushaKbn][value=1]").prop("disabled", true);
	}else{
		// 法人カード利用、会社手配を表示
		$("#houjinCardFlgRyohiName").show();
		$("#kaishaTehaiFlgRyohiName").show();
		meisai.find("input[name=houjinCardFlgRyohi]").show();
		meisai.find("input[name=kaishaTehaiFlgRyohi]").show();

		//日当フラグをセット
		meisai.find("input[name=nittouFlg]").val("");
		// ラジオボタンの値が1のものは課税区分・処理グループによって切り替える
		var shoriGroup = $("input[name=shoriGroupRyohi]").val();
		var kazeiKbn = $("select[name=kazeiKbnRyohi] option:selected").val();
		var flg = (shoriGroup == "21" ||
				((shoriGroup == "2"|| shoriGroup == "5"|| shoriGroup == "6"|| shoriGroup == "7"|| shoriGroup == "8"|| shoriGroup == "10")
						&& (kazeiKbn == "001" || kazeiKbn == "002" || kazeiKbn == "011" || kazeiKbn == "013")));
		$("[name=jigyoushaKbn][value=1]").prop("disabled", !flg);
	 }
}

/** 税区分、課税フラグの設定
 * 以下のタイミングで呼ばれる。
 * ・ダイアログロード直後
 * ・交通手段変更時
 */
function getKazeiFlg(){
	var zeiKubun = meisai.find("input[name=zeiKubun]").val();
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


//以下、初期化処理

//入力補助
commonInit(meisai);

//参照モード
if (dispMode == "3") {
	meisai.find('button,input[type!=hidden],select,textarea').prop("disabled", true);
	delCalculator(meisai.find("input[name=shouhizeigaku]"));
}

//種別２非表示
if (shubetsu2Sel.find("option").length == 0 || shubetsu2Sel.val() == "") {
	shubetsu2Sel.css("display", "none");
}

//課税フラグの設定
getKazeiFlg();

//明細金額の計算
calcNittouMeisaiMoney();

//種別変更時のリロード
shubetsu1Sel.change(function(){
	shubetsu2Sel.remove();
	reloadDialog();
});
shubetsu2Sel.change(reloadDialog);


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
			? $("#zeiritsuRyohi").find("option:selected").val(): 0);
	let meisaiKingaku = Number(target.find("input[name=meisaiKingaku]").val().replaceAll(",", ""));
	let hasuuShoriFlg = $("#workflowForm").find("input[name=hasuuShoriFlg]").val();
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
	target.find("input[name=shouhizeigaku]").val(shouhizeigaku.toString().formatMoney());
}

//法人カード履歴からの転記明細は金額入力不可とする。他項目の制御も。
var himodukeCard = meisai.find("input[name=himodukeCardMeisaiRyohi]").val();
if(himodukeCard != ""){ 
	meisai.find("[name=kakoEntryButton]").hide();
<c:if test='${not houjinCardDateEnabled}'>
	meisai.find("input[name=kikanFrom]").prop("disabled", true);
	meisai.find("input[name=kikanTo]").prop("disabled", true);
</c:if>
	meisai.find("input[name=kyuujitsuNissuu]").prop("disabled", true);
	meisai.find("input[name=heishuCdRyohi]").prop("disabled", true);
	meisai.find("#heishuSentakuButton").prop("disabled", true);
	meisai.find("input[name=rateRyohi]").prop("disabled", true);
	meisai.find("input[name=gaikaRyohi]").prop("disabled", true);
	meisai.find("input[name=tanka]").prop("disabled", true);
	meisai.find("#nissuuArea").hide();
	meisai.find("input[name=houjinCardFlgRyohi]").prop("disabled", true);
	meisai.find("input[name=kaishaTehaiFlgRyohi]").prop("disabled", true);
}
</c:if>

<c:if test='${ks.kyuujitsuNissuu.hyoujiFlg}'>
//休日日数が表示されている場合、日数の入力不可。期間による自動計算。
meisai.find("input[name=nissuu]").attr("disabled","disabled");
meisai.find("input[name=kikanFrom]").change(calcKikanMeisaiMoney);
meisai.find("input[name=kikanTo]").change(calcKikanMeisaiMoney);
meisai.find("input[name=kyuujitsuNissuu]").blur(calcKikanMeisaiMoney);
</c:if>

//レートによる外貨の計算
meisai.find("input[name=gaikaRyohi]").blur(calcGaikaMoney);
meisai.find("input[name=rateRyohi]").blur(calcGaikaMoney);

//単価や日数変更時の金額再計算
meisai.find("input[name=tanka]").blur(calcNittouMeisaiMoney);
meisai.find("input[name=nissuu]").blur(calcNittouMeisaiMoney);
meisai.find("input[name=ninzuu]").blur(calcNittouMeisaiMoney);

//法人カード・会社手配はどちらか１つのみ選択できるようにする
meisai.find("input[name=houjinCardFlgRyohi]").click(checkboxSeigyo);
meisai.find("input[name=kaishaTehaiFlgRyohi]").click(checkboxSeigyo);

//種別が変更されたとき、法人カード利用・会社手配を表示制御
meisai.find("select[name=shubetsu1]").change(shubetsuSentaku);

<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
//各種金額 手入力で修正した際に自動で再計算
meisai.find("input[name=shouhizeigaku]").blur(shouhizeigakuShusei);
meisai.find("input[name=meisaiKingaku]").blur(meisaiKingakuShusei);

/**
 * 消費税額修正ボタン押下時Function
 */
$("#zeiShuseiBtn").click(function(e) {
	var meisai = $("#dialogMainDenpyouMeisai");
	var flg = (meisai.find("input[name=shouhizeigaku]").prop('disabled'));
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

meisai.find("select[name=shubetsu2]").change(shubetsuSentaku);

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
</script>