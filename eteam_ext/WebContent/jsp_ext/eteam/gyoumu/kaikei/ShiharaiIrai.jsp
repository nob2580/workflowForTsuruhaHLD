<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<style type='text/css'>
/* balloon-1 top */
.balloon-1-top {
	color: #19283C;
	position: relative;
	display: inline-block;
	padding: 0 15px;
	width: auto;
	line-height: 32px;
	text-align: left;
	background: #F6F6F6;
	border: 3px solid #F75A5A;
	z-index: 0;
	margin-top:6px;
	margin-bottom:3px;
}
.balloon-1-top:before {
	content: "";
	position: absolute;
	top: -8px; left: 400px;
	margin-left: -9px;
	display: block;
	width: 0px;
	height: 0px;
	border-style: solid;
	border-width: 0 9px 9px 9px;
	border-color: transparent transparent #F6F6F6 transparent;
	z-index: 0;

}
.balloon-1-top:after {
	content: "";
	position: absolute;
	top: -12px; left: 400px;
	margin-left: -10px;
	display: block;
	width: 0px;
	height: 0px;
	border-style: solid;
	border-width: 0 10px 10px 10px;
	border-color: transparent transparent #F75A5A transparent;
	z-index: -1;
}
</style>

<div id='kaikeiContent' class='form-horizontal'>
		
	<section class='print-unit' id="shinseiSection">

		<h2>申請内容</h2>
			<input type='hidden' name='manekinName' value='${su:htmlEscape(manekinName)}'>
			<input type='hidden' name='ichigenFlg' value='${su:htmlEscape(ichigenFlg)}'>
			<!-- ▼カスタマイズ CSVフラグ追加 -->
			<input type='hidden' name='csvUploadFlag' value='${su:htmlEscape(csvUploadFlag)}'>

			<!-- 入力方式 -->
			<c:if test='${ks.nyuryokuHoushiki.hyoujiFlg}'>
				<div class="control-group invoiceOnly" style="float:right;<c:if test='${invoiceDenpyou eq "1"}'>display:none</c:if>">
						<label class="control-label"><c:if test='${ks.nyuryokuHoushiki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.nyuryokuHoushiki.name)}</label>
						<select name="nyuryokuHoushiki" class="input-small">
							 <c:forEach var="nyuryokuHoushikiRecord" items="${nyuuryokuHoushikiList}">
								<option value='${nyuryokuHoushikiRecord.naibuCd}'	<c:if test='${nyuryokuHoushikiRecord.naibuCd eq nyuryokuHoushiki}'>selected</c:if>>${su:htmlEscape(nyuryokuHoushikiRecord.name)}</option>
							</c:forEach>
						</select>
				</div>
				<div style="clear:both"></div><!-- 回り込み回避 -->
			</c:if>
			<!-- 取引先 -->
			<div class='control-group' <c:if test='${not ks.torihikisaki.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.torihikisaki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.torihikisaki.name)}</label>
				<div class='controls'>
					<input type='text' name="torihikisakiCd" class='input-medium pc_only' value='${su:htmlEscape(torihikisakiCd)}'>
					<input type='text' name="torihikisakiNameRyakushiki" class='input-xlarge' value='${su:htmlEscape(torihikisakiNameRyakushiki)}' disabled>
					<button type='button' id='torihikisakiSentakuButton' class='btn btn-small'>選択</button>
					<button type='button' id='torihikisakiClearButton' class='btn btn-small'>クリア</button>
					<span <c:if test='${(not ichigenFlg) or (not ks.shiharaisaki.hyoujiFlg)}'>style='display:none'</c:if>>
						<label class="label">${su:htmlEscape(ks.shiharaisaki.name)}<c:if test='${ks.shiharaisaki.hissuFlg}'><span class='required'>*</span></c:if></label>
						<input type='text' name="ichigensakiTorihikisakiName" class='input-xlarge' value='${su:htmlEscape(ichigensakiTorihikisakiName)}'>
					</span>
				</div>
			</div>
			<!-- 事業者区分 -->
			<div class='control-group invoiceOnly' <c:if test='${invoiceDenpyou eq 1}'>style='display:none;'</c:if>>
				<label class='control-label' name='jigyoushaKbn' <c:if test='${not ks.jigyoushaKbn.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.jigyoushaKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.jigyoushaKbn.name)}</label>
				<div class='controls'>
						<c:forEach var="jigyoushaKbnRecord" items="${jigyoushaKbnList}">
							<label class='radio inline' for='jigyoushaKbn' <c:if test='${not ks.jigyoushaKbn.hyoujiFlg}'>style='display:none;'</c:if>><input type='radio' name='jigyoushaKbn' value='${su:htmlEscape(jigyoushaKbnRecord.naibuCd)}' <c:if test='${jigyoushaKbnRecord.naibuCd eq jigyoushaKbn}'>checked </c:if>>${su:htmlEscape(jigyoushaKbnRecord.name)}</label>
						</c:forEach>
					<span <c:if test='${(not ks.jigyoushaNo.hyoujiFlg) or (not ichigenFlg)}'>style='display:none'</c:if>>
						<label class="label">${su:htmlEscape(ks.jigyoushaNo.name)}<c:if test='${ks.jigyoushaNo.hissuFlg}'><span class='required'>*</span></c:if></label>
						<input type='text' name="jigyoushaNo" class='input-xlarge'  maxlength='14' value='${su:htmlEscape(jigyoushaNo)}'>
					</span>
				</div>
			</div>
			<!-- 支払 -->
			<div class="control-group">
				<label class="control-label">支払</label>
				<div class="controls">
					<div>
						<label class="label"><span class="required">*</span>方法</label>
						<select name="shiharaiHouhou" class="input-small" disabled>
						    <option value='1'>振込</option>
						</select>
						<label class="label">種別</label>
						<select name="shiharaiShubetsu" class="input-small" disabled>
							<option value=''></option>
							<c:forEach var="tmpShiharaiShubetsu" items="${shiharaiShubetsuList}">
								<option value='${tmpShiharaiShubetsu.naibuCd}' <c:if test='${tmpShiharaiShubetsu.naibuCd eq shiharaiShubetsu}'>selected</c:if>>${su:htmlEscape(tmpShiharaiShubetsu.name)}</option>
							</c:forEach>
						</select>
					</div>
					<div>
						<label class='label'><span class='required'>*</span>計上日</label>
<c:if test="${setting.keijouNyuuryoku() eq 1}">
                        <input type='text' name='keijoubi' class='input-small datepicker' value='${su:htmlEscape(keijoubi)}'>
</c:if>
<c:if test="${setting.keijouNyuuryoku() eq 2}">
                        <select name='keijoubi' class='input-medium'>
                            <option value=''></option>
        <c:forEach var="record" items="${keijoubiList}">
                            <option value='${record}' <c:if test='${record == keijoubi}'>selected</c:if>>${record}</option>
        </c:forEach>   
                        </select>
</c:if>
						<label class='label' <c:if test='${not ks.yoteibi.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.yoteibi.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.yoteibi.name)}</label>
						<input name="yoteibi" class="input-small datepicker" value="${su:htmlEscape(yoteibi)}" type="text" <c:if test='${not ks.yoteibi.hyoujiFlg}'>style='display:none;'</c:if>>
						<label class='label'>支払日</label>
						<input type='text' name='shiharaibi' class='input-small datepicker' disabled value='${su:htmlEscape(shiharaibi)}'>
						<label class='label'>支払期日</label>
						<input type='text' name='shiharaiKijitsu' class='input-small datepicker' disabled value='${su:htmlEscape(shiharaiKijitsu)}'>
					</div>
				</div>
			</div>
			<!-- 領収書・請求書等 -->
			<div class="control-group">
				<label class='control-label' <c:if test='${not ks.shouhyouShoruiFlg.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shouhyouShoruiFlg.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shouhyouShoruiFlg.name)}</label>
				<div class="controls">
					<select name='shouhyouShoruiFlg' class='input-small' <c:if test='${not ks.shouhyouShoruiFlg.hyoujiFlg}'>style='display:none;'</c:if>>
					<option value='0' <c:if test='${shouhyouShoruiFlg eq "0"}'>selected</c:if>>なし</option>
					<option value='1' <c:if test='${shouhyouShoruiFlg eq "1"}'>selected</c:if>>あり</option>
					</select>
				</div>
			</div>
			<%@ include file="/jsp/eteam/gyoumu/kaikei/kogamen/HeaderField.jsp" %>
			
			<!-- EDI -->
			<div class="control-group">
				<label class="control-label">EDI</label>
				<div class="controls">
					<input name="edi" class="input-large" maxlength=20 value="${edi}" type="text">
				</div>
			</div>
			
			<!-- 振込先 -->
			<div class="control-group" id='furikomisaki_area'>
				<label class="control-label">振込先</label>
				<div class="controls">
					<div>
						<input type='hidden' name='furikomiGinkouId' value='${su:htmlEscape(furikomiGinkouId)}'>
						<label class="label"><span class='required'>*</span>銀行</label>
						<input type="text" name="furikomiGinkouCd" class="input-mini " maxlength=4 value="${furikomiGinkouCd}">
						<input type="text" name="furikomiGinkouName" class="input-mediam" disabled value="${furikomiGinkouName}">
						<button type='button' id='ginkouSentakuButton' class='btn btn-small'>選択</button>
						<label class="label"><span class='required'>*</span>支店</label>
						<input type="text" name="furikomiGinkouShitenCd" class="input-mini " maxlength=3 value="${furikomiGinkouShitenCd}">
						<input type="text" name="furikomiGinkouShitenName" class="input-mediam" disabled value="${furikomiGinkouShitenName}">
						<button type='button' id='ginkouShitenSentakuButton' class='btn btn-small'>選択</button>
						<!-- ▼カスタマイズ -->
						
						<button type = 'button' id='furikomisakiSentakuButton' class = 'btn btn-mediam'>振込先選択</button>
						
						<!-- ▲カスタマイズ -->
					</div>
					<div>
						<label class="label"><span class='required'>*</span>口座種別</label>
						<select name="kouzaShubetsu" class="input-small " disabled="disabled">
							<option value=''></option>
<c:forEach var="yokinShubetsuRecord" items="${yokinShubetsuList}">
							<option value='${yokinShubetsuRecord.naibuCd}' <c:if test='${yokinShubetsuRecord.naibuCd eq kouzaShubetsu}'>selected</c:if>>${su:htmlEscape(yokinShubetsuRecord.name)}</option>
</c:forEach>
						</select>
						<label class="label"><span class='required'>*</span>口座番号</label>
						<input type="text" name="kouzaBangou" class="input-small zeropadding" maxlength='7' value="${kouzaBangou}">
						<label class="label"><span class='required'>*</span>口座名義人(ｶﾅ)</label>
						<input type="text" name="kouzaMeiginin" class="input-xlarge " maxlength='30' value="${kouzaMeiginin}">
					</div>
					<div>
						<label class="label"><span class='required'>*</span>手数料</label>
						<label class='radio inline'><input type='radio' name='tesuuryou' value='1' <c:if test='${tesuuryou eq "1"}'>checked</c:if>>先方負担</label>
						<label class='radio inline'><input type='radio' name='tesuuryou' value='0' <c:if test='${tesuuryou eq "0"}'>checked</c:if>>自社負担</label>
					</div>
				</div>
			</div>
			
	</section>
	
	<%@ include file="/jsp/eteam/gyoumu/kaikei/kogamen/ShiharaiIraiMeisaiList.jsp" %>
	 
	<!-- 支払金額合計／本体金額合計／消費税額合計／消費税率 -->
	<section class='print-unit' id="goukeiSection">
		<div class='control-group'>
			<label class='control-label' <c:if test='${not ks.shiharaiGoukei.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shiharaiGoukei.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiGoukei.name)}</label>
			<div class='controls'>
				<input type='text' name='shiharaiGoukei' class='input-medium autoNumericNoMax' disabled value='${su:htmlEscape(shiharaiGoukei)}' <c:if test='${not ks.shiharaiGoukei.hyoujiFlg}'>style='display:none;'</c:if>>円
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label' <c:if test='${not ks.sousaiGoukei.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.sousaiGoukei.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.sousaiGoukei.name)}</label>
			<div class='controls'>
				<input type='text' name='sousaiGoukei' class='input-medium autoNumericNoMax' disabled value='${su:htmlEscape(sousaiGoukei)}' <c:if test='${not ks.sousaiGoukei.hyoujiFlg}'>style='display:none;'</c:if>>円
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label' <c:if test='${not ks.sashihikiShiharaiGaku.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.sashihikiShiharaiGaku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.sashihikiShiharaiGaku.name)}</label>
			<div class='controls'>
				<input type='text' name='kingaku' class='input-medium autoNumericNoMax' disabled value='${su:htmlEscape(kingaku)}' <c:if test='${not ks.sashihikiShiharaiGaku.hyoujiFlg}'>style='display:none;'</c:if>>円
				<span style='<c:if test='${empty(manekinName)}'>display:none;</c:if>'>
					<label class='label'>${su:htmlEscape(manekinName)}</label>
					<input type='text' name='manekinGensen' class='input-medium autoNumericNoMax' value='${su:htmlEscape(manekinGensen)}' <c:if test='${not manekinFlg}'>disabled</c:if>>円
				</span>
			</div>
		</div>
		<!-- インボイスで追加された合計欄  -->
		<div class='control-group invoiceOnly'>
			<label class='control-label' <c:if test='${not ks.shiharaiKingakuGoukei10Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shiharaiKingakuGoukei10Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiKingakuGoukei10Percent.name)}</label>
			<div class='controls'>
				<!-- 支払金額（10%） -->
				<c:if test='${ks.shiharaiKingakuGoukei10Percent.hyoujiFlg}'>
					<input type='text' name='shiharaiKingakuGoukei10Percent' class='input-medium autoNumericNoMax' disabled value='0'>円
				</c:if>
				<!-- 税抜金額（10%） -->
				<label class='label' <c:if test='${not ks.zeinukiKingaku10Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.zeinukiKingaku10Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.zeinukiKingaku10Percent.name)}</label>
				<c:if test='${ks.zeinukiKingaku10Percent.hyoujiFlg}'>
					<input type="text" name="zeinukiKingaku10Percent" class="input-medium autoNumericNoMax" disabled value='0'>円
				</c:if>
				<!-- 消費税額（10%） -->
				<label class='label' <c:if test='${not ks.shouhizeigaku10Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shouhizeigaku10Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shouhizeigaku10Percent.name)}</label>
				<c:if test='${ks.shouhizeigaku10Percent.hyoujiFlg}'>
					<input type="text" name="shouhizeigaku10Percent" class="input-medium autoNumericNoMax" disabled value='0'>円
				</c:if>
			</div>
		</div>
		<div class='control-group invoiceOnly'>
			<label class='control-label' <c:if test='${not ks.shiharaiKingakuGoukei8Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shiharaiKingakuGoukei8Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiKingakuGoukei8Percent.name)}</label>
			<div class='controls'>
				<!-- 支払金額（*8%） -->
				<c:if test='${ks.shiharaiKingakuGoukei8Percent.hyoujiFlg}'>
					<input type='text' name='shiharaiKingakuGoukei8Percent' class='input-medium autoNumericNoMax' disabled value='0'>円
				</c:if>
				<!-- 税抜金額（*8%） -->
				<label class='label' <c:if test='${not ks.zeinukiKingaku8Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.zeinukiKingaku8Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.zeinukiKingaku8Percent.name)}</label>
				<c:if test='${ks.zeinukiKingaku8Percent.hyoujiFlg}'>
					<input type="text" name="zeinukiKingaku8Percent" class="input-medium autoNumericNoMax" disabled value='0'>円
				</c:if>
				<!-- 消費税額（*8%） -->
				<label class='label' <c:if test='${not ks.shouhizeigaku8Percent.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shouhizeigaku8Percent.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shouhizeigaku8Percent.name)}</label>
				<c:if test='${ks.shouhizeigaku8Percent.hyoujiFlg}'>
					<input type="text" name="shouhizeigaku8Percent" class="input-medium autoNumericNoMax" disabled value='0'>円
				</c:if>
				<button type='button' id='zeigakuShousaiButton' class='btn btn-small'>消費税額詳細</button>
			</div>
		</div>
	</section>
	
	<!-- 補足 -->
	<section class='print-unit' <c:if test='${not ks.hosoku.hyoujiFlg}'>style='display:none;'</c:if>>
		<h2><c:if test='${ks.hosoku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.hosoku.name)}</h2>
		<div>
			<div class='control-group'>
				<textarea name="hosoku" maxlength='240' class='input-block-level'>${su:htmlEscape(hosoku)}</textarea>
			</div>
		</div>
	</section>
</div><!-- kaikeiContent -->

<!-- スクリプト -->
<script style='text/javascript'>

/**
 * 明細行の更新時の処理
 */
function updateDenpyouMeisaiEvent(meisaiInfo) {
	calcMoney();
}

/**
 * 金額の再計算処理
 */
function calcMoney() {

	//ブランクと0ややこしいので、入力可能なブランクは0リセット
	$("input.autoNumericWithCalcBox").each(function(){
		if ((typeof $(this).attr("disabled") == "undefined") && "" == $(this).val()) {
			$(this).putMoney(0);
		}
	});
	var zeiritsuTag = $("select[name=zeiritsu]").find("option:selected");
	var zeiritsu = Number(zeiritsuTag.val()) * 0.01;
	var hasuuKeisanKbn = zeiritsuTag.attr("data-hasuuKeisanKbn");
	var shiharaiGoukeiTag = $("input[name=shiharaiGoukei]");
	var sousaiGoukeiTag = $("input[name=sousaiGoukei]");
	var manekinTag = $("input[name=manekinGensen]");
	var kingakTag = $("input[name=kingaku]");
	
	//--------
	//明細単位に金額調整
	//--------
	var shiharaiGoukei = 0;
	var sousaiGoukei = 0;
	var manekin = manekinTag.getMoney();

	$.each(getMeisaiList(), function(ii, meisaiInfo) {
		//諸々値を取り出して
 		var shiharaiKingaku	= (meisaiInfo["shiharaiKingaku"]) ? parseInt(meisaiInfo["shiharaiKingaku"].split(',').join('')) : 0;
 		
 		if (shiharaiKingaku > 0) {
 			shiharaiGoukei += shiharaiKingaku;
 		} else {
 			sousaiGoukei -= shiharaiKingaku;
 		}
	});

	//支払合計
	shiharaiGoukeiTag.putMoney(shiharaiGoukei);
	//相殺合計
	sousaiGoukeiTag.putMoney(sousaiGoukei);
	
	//差引支払額
	var kingaku = shiharaiGoukei - sousaiGoukei - manekin;
	kingakTag.putMoney(kingaku);

	// インボイス関連（eteam.jsより読込）
	calcInvoiceKingaku();
}

/**
 * 取引先変更 or 支払方法(種別)変更時に取引先マスターリロード
 */
function realodTorihikisakiMaster() {
	if ($("[name=torihikisakiNameRyakushiki]").val() != "") {
		eventBtn("torihikisaki_master_reload");
	}
}

/**
 * 支払関連入力制御(いろんなトリガーで都度)
 */
function controlShiharaiInput() {
	var shiharaiShubetsu	= $("select[name=shiharaiShubetsu]").val();
	
    //種別=定期なら支払予定日 入力NG、振込先 入力NG
    if (shiharaiShubetsu == "1") {
    	$("[name=yoteibi]").prop("disabled", true);
    	//▼カスタマイズ　入力制御は経理ロールではしない
    	<c:if test='${wfSeigyo.keiri}'>
    		$("[name=yoteibi]").prop("disabled", false);
    	</c:if>
        //$("[name=yoteibi]").prop("disabled", true);
        $("#furikomisaki_area").find("input,select,button").prop("disabled", true);
        //▼カスタマイズ
        if($("input[name=torihikisakiCd]").val() != ""){
        $("#furikomisakiSentakuButton").prop("disabled",false);
        }
    //種別=その他 or 未入力なら支払予定日 入力NG、振込先 入力OK
    } else {
        $("[name=yoteibi]").prop("disabled", false);
        $("#furikomisaki_area").find("input,select,button").prop("disabled", false);
        $("[name=furikomiGinkouName],[name=furikomiGinkouShitenName]").prop("disabled", true);
    }
}

//初期表示処理
$(document).ready(function(){
	
	// 明細表示をロード
	setDisplayMeisaiData();

<c:if test='${enableInput}'>
	//事業者区分の変更可否制御
	let jigyoushaKbnHenkouFlg = $("#workflowForm").find("input[name=jigyoushaKbnHenkouflg]").val() == "0";
	let ichigenFlg = $("input[name=ichigenFlg]").val() == "false";
	$("input[name=jigyoushaKbn]").prop("disabled", jigyoushaKbnHenkouFlg && ichigenFlg);

	//計上日変更時、支払先リロード
	$("[name=keijoubi]").change(function(){
		realodTorihikisakiMaster();
	});

	//取引先マスター選択、選択後、支払先リロード
	dialogCallbackTorihikisakiSentaku = function(){
		$("#furikomisaki_area").find("input,select").val("");
		realodTorihikisakiMaster();
	};
	$("#torihikisakiSentakuButton").click(function(){
		dialogRetTorihikisakiCd = $("[name=torihikisakiCd]");
		dialogRetTorihikisakiName = $("[name=torihikisakiNameRyakushiki]");
		dialogRetJigyoushaKbn = $("[name=jigyoushaKbn]");
		commonTorihikisakiSentaku();
	});
	$("[name=torihikisakiCd]").change(function(){
		dialogRetTorihikisakiCd = $("[name=torihikisakiCd]");
		dialogRetTorihikisakiName = $("[name=torihikisakiNameRyakushiki]");
		dialogRetJigyoushaKbn = $("[name=jigyoushaKbn]");
	 	var title = $(this).parent().parent().find(".control-label").text();
		commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd, dialogRetTorihikisakiName, title, null, null,dialogRetJigyoushaKbn);
	});
	
	//取引先クリアボタン押下時、取引先クリア
	$("#torihikisakiClearButton").click(function(){
		$("[name=torihikisakiCd]").val("");
		$("[name=torihikisakiNameRyakushiki]").val("");
	});
	
	//支払関連入力制御
	$("select[name=shiharaiHouhou]").change(function(){
		controlShiharaiInput();
		realodTorihikisakiMaster();
	});
	$("select[name=shiharaiShubetsu]").change(function(){
		controlShiharaiInput();
		realodTorihikisakiMaster();
	});
	controlShiharaiInput();
	
	//銀行コード入力欄ロストフォーカス時、名称取得
	$("[name=furikomiGinkouCd]").change(function(){
		var a = $(this);
		dialogRetGinkouCd = $("input[name=furikomiGinkouCd]");
		dialogRetGinkouName =  $("input[name=furikomiGinkouName]");
		var title = a.prev().text()+"コード";
		commonGinkouCdLostFocus(dialogRetGinkouCd, dialogRetGinkouName, title, "0");
		});

	//銀行選択ボタン押下時、ダイアログ表示
	$("#ginkouSentakuButton").click(ginkouSentaku);
	
	//支店コード入力欄ロストフォーカス時、名称取得
	$("[name=furikomiGinkouShitenCd]").change(function(){
		var a = $(this);
		dialogRetGinkouShitenCd = $("input[name=furikomiGinkouShitenCd]");
		dialogRetGinkouShitenName =  $("input[name=furikomiGinkouShitenName]");
		ginkouCd = a.parent().find("[name=furikomiGinkouCd]").val();
		var title = $("[name=furikomiGinkouCd]").prev().text()+"コード"+"、"+a.prev().text()+"コード";
		commonGinkouShitenCdLostFocus(dialogRetGinkouShitenCd, dialogRetGinkouShitenName, title, ginkouCd);
		});

	//銀行支店選択ボタン押下時、ダイアログ表示
	$("#ginkouShitenSentakuButton").click(shitenSentaku);
	
	//口座名義人カナからｶﾅ(EDI,稟議書番号も)
	$("[name=kouzaMeiginin],[name=edi],[name=hf1Cd]").change(function(){
		$(this).val(hankana2zenkanaCustomize($(this).val()));
	});
	
	//事業者区分変更時再計算
	$("input[name=jigyoushaKbn]").change(function(){
		// 税込方式の時だけ
		if ($("select[name=nyuryokuHoushiki] option:selected").val() == "0") {
			let jigyoushaFlg = $(this).val() > 0 ? $(this).val() : "0";
			let hasuuShoriFlg = $("#workflowForm").find("input[name=hasuuShoriFlg]").val();
			let shiireKeikaSothiFlg = $("#workflowForm").find("input[name=shiirezeigakuKeikasothi]").val();
			$("#meisaiList tr.meisai").each(function() {
				calcMeisaiZeigaku(jigyoushaFlg, hasuuShoriFlg, shiireKeikaSothiFlg, $(this));
			});
			setDisplayMeisaiData();
		}
	});
	
	//金額計算
	$("[name=manekinGensen]").change(calcMoney);
</c:if>

<c:if test='${not enableInput}'>
	//起票モードの場合のみ入力や選択可能
	$("#kaikeiContent").find("button").css("display", "none");
	$("#kaikeiContent").find("input,textarea,select").prop("disabled", true);
</c:if>

	// 消費税額詳細ボタンクリック
	$('#zeigakuShousaiButton').on('click', function() {
	    commonShouhizeiShousai($("input[name=denpyouKbn]").val());
	  });
});

/**
 * イベントボタン押下時のアクションの切り替え
 */
function eventBtn(eventName) {
	var formObject = document.getElementById("workflowForm");

<c:if test='${manekinFlg}'>
	if (eventName == 'entry' || eventName == 'update') {
		if ($("[name=manekinGensen]").val() == "" || Number($("[name=manekinGensen]").val()) == 0) {
			if (! confirm($("[name=manekinName]").val() + "が入力されていません。\nよろしいですか？")) {
				return;
			}
		}
	}
</c:if>
	
	switch (eventName) {
	case 'entry':
		formObject.action = 'shiharai_irai_touroku';
		break;
	case 'update':
		formObject.action = 'shiharai_irai_koushin';
		break;
	case 'shinsei':
		formObject.action = 'shiharai_irai_shinsei';
		break;
	case 'shounin':
		formObject.action = 'shiharai_irai_shounin';
		break;
	case 'shouninkaijo':
		formObject.action = 'shiharai_irai_shouninkaijo';
		break;
	case 'torihikisaki_master_reload':
		formObject.action = 'shiharai_irai_torihikisaki_master_reload';
		break;
	}
	
	formObject.method = 'post';
	formObject.target = '_self';
	$(formObject).submit();
}

/**
 * 銀行検索ボタン押下時Function
 */
function ginkouSentaku() {
	dialogRetGinkouCd = $("[name=furikomiGinkouCd]");
	dialogRetGinkouName = $("[name=furikomiGinkouName]");
	dialogCallbackRetGinkouSentaku = function() {
		$("[name=furikomiGinkouShitenCd]").val("");
		$("[name=furikomiGinkouShitenName]").val("");
	}
	
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "銀行選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("ginkou_sentaku");
}

/**
 * 銀行支店検索ボタン押下時Function
 */
function shitenSentaku() {
	dialogRetGinkouShitenCd = $("[name=furikomiGinkouShitenCd]");
	dialogRetGinkouShitenName = $("[name=furikomiGinkouShitenName]");
	
	var ginkouCd = $("[name=furikomiGinkouCd]").val();
	var ginkouName = $("[name=furikomiGinkouName]").val();
	if (ginkouCd == "") {
		alert("銀行コードを入力してください。");
		return;
	}
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "銀行支店選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("ginkou_shiten_sentaku?" + $.param({kinyuukikanCd: ginkouCd, kinyuukikanName: ginkouName}));
}

function hankana2zenkanaCustomize(str) {
	/** カタカナ */
	var katakanaCus = new Array('ァ','ィ','ゥ','ェ','ォ','ャ','ュ','ョ','ッ','ー','ゔ','ガ','ギ','グ','ゲ','ゴ','ザ','ジ','ズ','ゼ','ゾ','ダ','ヂ','ヅ','デ','ド','バ','ビ','ブ','ベ','ボ','パ','ピ','プ','ペ','ポ','ア','イ','ウ','エ','オ','カ','キ','ク','ケ','コ','サ','シ','ス','セ','ソ','タ','チ','ツ','テ','ト','ナ','ニ','ヌ','ネ','ノ','ハ','ヒ','フ','ヘ','ホ','マ','ミ','ム','メ','モ','ヤ','ユ','ヨ','ラ','リ','ル','レ','ロ','ワ','ヲ','ン');
	/** ひらがな */
	var hiraganaCus = new Array('ぁ','ぃ','ぅ','ぇ','ぉ','ゃ','ゅ','ょ','っ','ー','ゔ','が','ぎ','ぐ','げ','ご','ざ','じ','ず','ぜ','ぞ','だ','ぢ','づ','で','ど','ば','び','ぶ','べ','ぼ','ぱ','ぴ','ぷ','ぺ','ぽ','あ','い','う','え','お','か','き','く','け','こ','さ','し','す','せ','そ','た','ち','つ','て','と','な','に','ぬ','ね','の','は','ひ','ふ','へ','ほ','ま','み','む','め','も','や','ゆ','よ','ら','り','る','れ','ろ','わ','を','ん');
	/** ｶﾅ */
	var hankanaCus = new Array(
			'ｧ','ｨ','ｩ','ｪ','ｫ','ｬ','ｭ','ｮ','ｯ','ｰ',
			'ｳﾞ','ｶﾞ','ｷﾞ','ｸﾞ','ｹﾞ','ｺﾞ','ｻﾞ','ｼﾞ','ｽﾞ','ｾﾞ','ｿﾞ','ﾀﾞ','ﾁﾞ','ﾂﾞ','ﾃﾞ','ﾄﾞ','ﾊﾞ','ﾋﾞ','ﾌﾞ','ﾍﾞ','ﾎﾞ','ﾊﾟ','ﾋﾟ','ﾌﾟ','ﾍﾟ','ﾎﾟ',
			'ｱ','ｲ','ｳ','ｴ','ｵ','ｶ','ｷ','ｸ','ｹ','ｺ','ｻ','ｼ','ｽ','ｾ','ｿ','ﾀ','ﾁ','ﾂ','ﾃ','ﾄ','ﾅ','ﾆ','ﾇ','ﾈ','ﾉ','ﾊ','ﾋ','ﾌ','ﾍ','ﾎ','ﾏ','ﾐ','ﾑ','ﾒ','ﾓ','ﾔ','ﾕ','ﾖ','ﾗ','ﾘ','ﾙ','ﾚ','ﾛ','ﾜ','ｦ','ﾝ',
			'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
			'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			'0','1','2','3','4','5','6','7','8','9',
			'ﾞ','ﾟ','(',')','｢','｣','.',',','\\','-','/',
			' ');
	/** カナ */
	var zenkanaCus = new Array(
			'ァ','ィ','ゥ','ェ','ォ','ャ','ュ','ョ','ッ','ー',
			'ヴ','ガ','ギ','グ','ゲ','ゴ','ザ','ジ','ズ','ゼ','ゾ','ダ','ヂ','ヅ','デ','ド','バ','ビ','ブ','ベ','ボ','パ','ピ','プ','ペ','ポ',
			'ア','イ','ウ','エ','オ','カ','キ','ク','ケ','コ','サ','シ','ス','セ','ソ','タ','チ','ツ','テ','ト','ナ','ニ','ヌ','ネ','ノ','ハ','ヒ','フ','ヘ','ホ','マ','ミ','ム','メ','モ','ヤ','ユ','ヨ','ラ','リ','ル','レ','ロ','ワ','ヲ','ン',
			'Ａ','Ｂ','Ｃ','Ｄ','Ｅ','Ｆ','Ｇ','Ｈ','Ｉ','Ｊ','Ｋ','Ｌ','Ｍ','Ｎ','Ｏ','Ｐ','Ｑ','Ｒ','Ｓ','Ｔ','Ｕ','Ｖ','Ｗ','Ｘ','Ｙ','Ｚ',
			'ａ','ｂ','ｃ','ｄ','ｅ','ｆ','ｇ','ｈ','ｉ','ｊ','ｋ','ｌ','ｍ','ｎ','ｏ','ｐ','ｑ','ｒ','ｓ','ｔ','ｕ','ｖ','ｗ','ｘ','ｙ','ｚ',
			'０','１','２','３','４','５','６','７','８','９',
			'゛','゜','（','）','「','」','．','，','￥','－','／',
			'　');
	/** 半角カナ大文字 */
	var hankanaLarge = new Array('ｧ','ｨ','ｩ','ｪ','ｫ','ｬ','ｭ','ｮ','ｯ');
	/** 半角カナ小文字 */
	var hankanaSmall = new Array('ｱ','ｲ','ｳ','ｴ','ｵ','ﾔ','ﾕ','ﾖ','ﾂ');

	var henkanMatrix = [
		[katakanaCus,	hiraganaCus],
		[hankanaCus,	zenkanaCus],
		[hankanaSmall,	hankanaLarge]
	];
	
	for(var i = 0; i < henkanMatrix.length; i++) {
		for(var j = 0; j < henkanMatrix[i][0].length; j++) {
			str = str.replace(new RegExp(henkanMatrix[i][1][j], 'g'), henkanMatrix[i][0][j]);
		}
	}
	return str;
}

//明細税率を計算する
function calcMeisaiZeigaku(jigyoushaFlg, hasuuShoriFlg, shiireKeikaSothiFlg, target) {
	let kazeiKbnGroup  = target.find("select[name=kazeiKbn] option:selected").attr("data-kazeiKbnGroup");
	let zeiritsu = kazeiKbnGroup == "1" ? Number(target.find("select[name=zeiritsu]").find("option:selected").val()) : "0";
	let shiharaiKingaku = Number(target.find("input[name=shiharaiKingaku]").val().replaceAll(",", ""));
	
	let jigyoushaNum = (shiireKeikaSothiFlg == "1" || jigyoushaFlg == "0")
		? 1
		: "1" == jigyoushaFlg
			? 0.8
			: 0.5; // 掛け算なのでデフォは1

	//decimalとして持つ
	let dZeiritsu = new Decimal(zeiritsu);
	let dShiharaiKingaku = new Decimal(shiharaiKingaku);
	let dJigyoushaNum = new Decimal(jigyoushaNum);
	let dBunbo = new Decimal(100).add(dZeiritsu);
	//計算用
	let shouhizeigaku = hasuuKeisanZaimuFromImporter(dShiharaiKingaku.mul(dZeiritsu).div(dBunbo).mul(dJigyoushaNum), hasuuShoriFlg, false);
	let zeinukiKingaku = dShiharaiKingaku.sub(shouhizeigaku);
		
	//計算した本体金額、消費税額の値をセット or クリア
	target.find("input[name=zeinukiKingaku]").val(zeinukiKingaku.toString().formatMoney());
	target.find("input[name=shouhizeigaku]").val(shouhizeigaku?.toString()?.formatMoney());
}

//▼カスタマイズ
//予定日変更時、支払日も変更させる
$("input[name=yoteibi]").change(function(){
	$("input[name=shiharaibi]").val($(this).val());
});

//振込先選択ボタンアクション
$("#furikomisakiSentakuButton").click(function(){
	 var torihikisakiCd = $("input[name=torihikisakiCd]").val();
	 var width = "700";
		if(windowSizeChk()) {
			width = $(window).width() * 0.9;
		}
		$("#dialog")
		.dialog({
			modal: true,
			width: width,
			height: "520",
			title: "振込先選択",
			buttons: {閉じる: function() {$(this).dialog("close");}}
		})
		.load("furikomisaki_sentaku?torihikisakiCd=" + convTorihikisakiCd4Sentaku(torihikisakiCd));
})
</script>