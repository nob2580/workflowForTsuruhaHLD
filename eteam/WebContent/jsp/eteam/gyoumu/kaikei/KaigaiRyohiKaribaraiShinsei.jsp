<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<div id='kaikeiContent' class='form-horizontal'>

	<!-- 申請内容 -->
	<section class='print-unit' id="shinseiSection">
		<h2>申請内容</h2>
		<div>
			<!-- 使用者 -->
			<div class='control-group' <c:if test='${not ks.userName.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.userName.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.userName.name)}</label>
				<div class='controls'>
					<input type='text' name="shainNoRyohi" class='input-medium pc_only' value='${su:htmlEscape(shainNoRyohi)}'>
					<input type='text' name="userNameRyohi" class='input-xxlarge' disabled value='${su:htmlEscape(userNameRyohi)}'>
					<input type='hidden' name="userIdRyohi" value='${su:htmlEscape(userIdRyohi)}'>
					<input type='hidden' name="shainNoBefChg" value='${su:htmlEscape(shainNoRyohi)}'>
					<button type='button' id='userSentakuRyohiButton' class='btn btn-small'>選択</button>
				</div>
			</div>
			<!-- 仮払選択 -->
			<div class='control-group<c:if test='${not ks.karibaraiOn.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.karibaraiOn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.karibaraiOn.name)}</label>
				<div class='controls'>
					<label class='radio inline'>
						<input type='radio' name='karibaraiOn' value='1' <c:if test='${"1" eq karibaraiOn}'>checked</c:if>>
						あり
					</label>
					<label class='radio inline'>
						<input type='radio' name='karibaraiOn' value='0' <c:if test='${"0" eq karibaraiOn}'>checked</c:if>>
						なし
					</label>
				</div>
			</div>
			<!-- ヘッダーフィールド -->
			<span id="headerField">
			<%@ include file="./kogamen/HeaderField.jsp" %>
			</span>
			<!-- 出張先・訪問先 -->
			<div class='control-group<c:if test='${not ks.houmonsaki.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.houmonsaki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.houmonsaki.name)}</label>
				<div class='controls'>
					<textarea name='houmonsaki' maxlength='200' class='input-block-level'>${su:htmlEscape(houmonsaki)}</textarea>
				</div>
			</div>
			<!-- 目的 -->
			<div class='control-group<c:if test='${not ks.mokuteki.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.mokuteki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.mokuteki.name)}</label>
				<div class='controls'>
					<input type='text' name='mokuteki' maxlength='30' class='input-block-level' value='${su:htmlEscape(mokuteki)}'>
				</div>
			</div>
			<!-- 精算期間 -->
			<div class='control-group<c:if test='${not ks.seisankikan.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.seisankikan.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.seisankikan.name)}</label>
				<div class='controls'>
					<input type='text' name='seisankikanFrom' class='input-small datepicker' value='${su:htmlEscape(seisankikanFrom)}'>
					<span <c:if test='${not ks.seisankikanJikoku.hyoujiFlg}'>style='display:none;'</c:if>>
					<c:if test='${ks.seisankikanJikoku.hissuFlg}'><span class='required'>*</span></c:if>
					<input type='text' name='seisankikanFromHour' class='input-small zeropadding' style='width:40px;' maxlength='2' value='${su:htmlEscape(seisankikanFromHour)}'>時
					<input type='text' name='seisankikanFromMin' class='input-small zeropadding' style='width:40px;' maxlength='2' value='${su:htmlEscape(seisankikanFromMin)}'>分
					</span>
					～
					<input type='text' name='seisankikanTo' class='input-small datepicker' value='${su:htmlEscape(seisankikanTo)}'>
					<span <c:if test='${not ks.seisankikanJikoku.hyoujiFlg}'>style='display:none;'</c:if>>
					<c:if test='${ks.seisankikanJikoku.hissuFlg}'><span class='required'>*</span></c:if>
					<input type='text' name='seisankikanToHour' class='input-small zeropadding' style='width:40px;' maxlength='2' value='${su:htmlEscape(seisankikanToHour)}'>時
					<input type='text' name='seisankikanToMin' class='input-small zeropadding' style='width:40px;' maxlength='2' value='${su:htmlEscape(seisankikanToMin)}'>分
					</span>
					<span style="color:#ff0000;">${su:htmlEscape(shinseiChuuki)}</span>
				</div>
			</div>
			<div id = 'karibaraiAriArea'>
			<!-- 取引 -->
			<div class='control-group<c:if test='${not ks.torihiki.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.torihiki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.torihiki.name)}</label>
				<div class='controls'>
					<input type="text" name='shiwakeEdaNoRyohi'  value='${su:htmlEscape(shiwakeEdaNoRyohi)}' class='input-small pc_only'>
					<input type='text' name='torihikiNameRyohi' class='input-xlarge' value='${su:htmlEscape(torihikiNameRyohi)}' disabled>
					<button type='button' id='torihikiSentakuButton' class='btn btn-small'>選択</button>
				</div>
			</div>
			<!-- 勘定科目 -->
			<div class='control-group<c:if test='${not ks.kamoku.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.kamoku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kamoku.name)}</label>
				<div class='controls'>
					<input type='text' name='kamokuCdRyohi' class='input-small pc_only' value='${su:htmlEscape(kamokuCdRyohi)}' disabled>
					<input type='text' name='kamokuNameRyohi' class='input-xlarge' value='${su:htmlEscape(kamokuNameRyohi)}' disabled>
				</div>
			</div>
			<!-- 勘定科目枝番 -->
			<div class='control-group<c:if test='${not ks.kamokuEdaban.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.kamokuEdaban.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kamokuEdaban.name)}</label>
				<div class='controls'>
					<input type='text' name='kamokuEdabanCdRyohi' class='input-medium pc_only' value='${su:htmlEscape(kamokuEdabanCdRyohi)}' <c:if test='${not kamokuEdabanEnableRyohi}'>disabled</c:if>>
					<input type='text' name='kamokuEdabanNameRyohi' class='input-xlarge' value='${su:htmlEscape(kamokuEdabanNameRyohi)}' disabled>
					<button type='button' id='kamokuEdabanSentakuButton' class='btn btn-small' <c:if test='${not kamokuEdabanEnableRyohi}'>disabled</c:if>>選択</button>
				</div>
			</div>
			<!-- 負担部門 -->
			<div class='control-group<c:if test='${not ks.futanBumon.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.futanBumon.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.futanBumon.name)}</label>
				<div class='controls'>
					<input type='text' name="futanBumonCdRyohi" class='input-small pc_only' value='${su:htmlEscape(futanBumonCdRyohi)}' <c:if test='${not futanBumonEnableRyohi}'>disabled</c:if>>
					<input type='text' name="futanBumonNameRyohi" class='input-xlarge' value='${su:htmlEscape(futanBumonNameRyohi)}' disabled>
					<button type='button' id='futanBumonSentakuButton' class='btn btn-small' <c:if test='${not futanBumonEnableRyohi}'>disabled</c:if>>選択</button>
				</div>
			</div>
			<!-- 取引先 -->
			<div class='control-group <c:if test='${not ks.torihikisaki.hyoujiFlg}'>never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.torihikisaki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.torihikisaki.name)}</label>
				<div class='controls'>
					<input type='text' name="torihikisakiCdRyohi" class='input-medium pc_only' <c:if test='${not torihikisakiEnableRyohi}'>disabled</c:if> value='${su:htmlEscape(torihikisakiCdRyohi)}'>
					<input type='text' name="torihikisakiNameRyohi" class='input-xlarge' value='${su:htmlEscape(torihikisakiNameRyohi)}' disabled>
					<button type='button' id='torihikisakiSentakuButton' class='btn btn-small' <c:if test='${not torihikisakiEnableRyohi}'>disabled</c:if>>選択</button>
					<button type='button' id='torihikisakiClearButton' class='btn btn-small' <c:if test='${not torihikisakiEnableRyohi}'>disabled</c:if>>クリア</button>
				</div>
			</div>
			<!-- プロジェクト -->
			<div class='control-group<c:if test='${not("0" ne pjShiyouFlg and ks.project.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
					<label class='control-label'><c:if test='${ks.project.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.project.name)}</label>
					<div class='controls'>
						<input type='text' name="projectCdRyohi" class='pc_only' <c:if test='${not projectEnableRyohi}'>disabled</c:if> value='${su:htmlEscape(projectCdRyohi)}'>
						<input type='text' name="projectNameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(projectNameRyohi)}'>
						<button type='button' id='projectSentakuButton' class='btn btn-small' <c:if test='${not projectEnableRyohi}'>disabled</c:if>>選択</button>
					</div>
				</div>
			<!-- セグメント -->
			<div class='control-group<c:if test='${not("0" ne segmentShiyouFlg and ks.segment.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.segment.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.segment.name)}</label>
				<div class='controls'>
					<input type='text' name="segmentCdRyohi" class='pc_only' <c:if test='${not segmentEnableRyohi}'>disabled</c:if> value='${su:htmlEscape(segmentCdRyohi)}'>
					<input type='text' name="segmentNameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(segmentNameRyohi)}'>
					<button type='button' id='segmentSentakuButton' class='btn btn-small' <c:if test='${not segmentEnableRyohi}'>disabled</c:if>>選択</button>
				</div>
			</div>

			<!-- ユニバーサルフィールド-->
			<%@ include file="./kogamen/UniversalFieldRyohi.jsp" %>

			</div>
			<!-- 摘要 -->
			<div class='control-group<c:if test='${not ks.tekiyou.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
				<label class='control-label'><c:if test='${ks.tekiyou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.tekiyou.name)}</label>
				<div class='controls'>
					<input type='text' name='tekiyouRyohi' maxlength='${su:htmlEscape(tekiyouMaxLength)}' class='input-block-level' value='${su:htmlEscape(tekiyouRyohi)}'>
					<span style="line-height:25px;"><br><span style="color:#ff0000;">${su:htmlEscape(chuuki2Ryohi)}</span></span>
				</div>
			</div>
			<!-- 支払希望日／支払方法／支払日／計上日 -->
			<!-- 例外的に、項目レベルでnever_showを入れる -->
			<div class='control-group hissu-group' id='shiharai' >
				<label for='shiharaiLabel' class='control-label'>支払</label>
				<div class='controls'>
					<!-- 支払希望日 -->
					<label class='label <c:if test='${not ks.shiharaiKiboubi.hyoujiFlg}'>never_show' style='display:none;</c:if>'><c:if test='${ks.shiharaiKiboubi.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiKiboubi.name)}</label>
					<input type='text' name='shiharaiKiboubi' class='input-small datepicker <c:if test='${not ks.shiharaiKiboubi.hyoujiFlg}'>never_show' style='display:none;</c:if>' value='${su:htmlEscape(shiharaiKiboubi)}'>
					<!-- 支払方法 -->
					<label class='label <c:if test='${not ks.shiharaiHouhou.hyoujiFlg}'>never_show' style='display:none;</c:if>'><c:if test='${ks.shiharaiHouhou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiHouhou.name)}</label>
					<select id='shiharaihouhou' name='shiharaihouhou' class='input-small <c:if test='${not ks.shiharaiHouhou.hyoujiFlg}'>never_show' style='display:none;</c:if>' <c:if test="${disableShiharaiHouhou}">disabled</c:if>>
						<c:forEach var='record' items='${shiharaihouhouList}'>
							<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq shiharaihouhou}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
						</c:forEach>
					</select>
					<!-- 支払日 --><%-- 申請後表示、経理承認時のみ変更可能 --%>
					<span id='shiharaibi' <c:if test="${shiharaiBiMode == 0}">class='never_show' </c:if><c:if test="${shiharaiBiMode == 0 || (shiharaiBiMode == 3 && karibaraiOn eq '0')}">style='display:none;'</c:if>>
						<label class='label'><span class='required'>*</span>支払日</label>
						<input type='text' name='shiharaibi' class='input-small datepicker' value='${su:htmlEscape(shiharaibi)}' <c:if test="${shiharaiBiMode % 2 != 1}">disabled</c:if>><!-- 初期値disableだとdatepickerが動かない -->
					</span>
				</div>
			</div>
			<!-- 明細金額合計／差引支給金額／消費税率 -->
			<div class='control-group'>
				<label class='control-label'><span class='required'>*</span>${su:htmlEscape(ks.shinseiKingaku.name)}</label>
				<div class='controls'>
					<!-- 申請金額合計 -->
					<input type='text' name='kingaku' class='input-medium autoNumericNoMax hissu' disabled value ='${su:htmlEscape(kingaku)}'>円
					<!-- 仮払金額合計 -->
					<label class='label'><span class='required'>*</span>${su:htmlEscape(ks.karibaraiKingaku.name)}</label>
					<input type='text' name='karibaraiKingaku' class='input-medium autoNumeric' disabled value ='${su:htmlEscape(karibaraiKingaku)}'>円
				</div>
			</div>
		</div>
	</section>

	<!-- 明細 -->
	<h2>海外分</h2>
	<%@ include file="./kogamen/KaigaiRyohiKoutsuuhiMeisaiList.jsp" %>
	<h2>国内分</h2>
	<%@ include file="./kogamen/RyohiKoutsuuhiMeisaiList.jsp" %>
	<h2>海外・国内共通</h2>
	<%@ include file="./kogamen/DenpyouMeisaiList.jsp" %>

	<!-- 補足 -->
	<section class='print-unit' <c:if test='${not ks.hosoku.hyoujiFlg}'>style='display:none;'</c:if>>
		<h2><c:if test='${ks.hosoku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.hosoku.name)}</h2>
		<div>
			<div class='control-group'>
				<textarea name='hosoku' maxlength='240' class='input-block-level'>${su:htmlEscape(hosoku)}</textarea>
			</div>
		</div>
	</section>

</div><!-- kaikeiContent -->

<!-- スクリプト -->
<script style='text/javascript'>
/**
 * 日当・宿泊費等の差引金額を計算するFunction
 * flg=1（海外）、0（国内）
 */
function calcSashihikiMoney(flg) {
	var kingaku;
	var num
	var shoukei;
	var tanka;
	var tankaDiv;
	var numDiv;
	var kingakuDiv;
	var shoukeiDiv;

	kingaku = 0;
	num = 0
	// 国内分の場合
	if(flg === 0){
		tankaDiv = $("[name='sashihikiTanka']");
		numDiv = $("[name='sashihikiNum']");
		kingakuDiv = $("[name='sashihikiKingaku']");
		shoukeiDiv = $("[name='shoukei02']");
	}
	// 海外分の場合
	if(flg === 1){
		tankaDiv = $("[name='sashihikiTankaKaigai']");
		numDiv = $("[name='sashihikiNumKaigai']");
		kingakuDiv = $("[name='sashihikiKingakuKaigai']");
		shoukeiDiv = $("[name='shoukei05']");
	}

	// 小計を再計算
	tanka = tankaDiv.getMoney();
	if(numDiv.val() != ""){
		num = numDiv.val();
	}
	kingaku = kingaku - (tanka * num);
	shoukei = shoukeiDiv.getMoney() + kingaku;

	// 日当・宿泊費等の小計から差引金額を除算
 	kingakuDiv.putMoney(kingaku);
 	shoukeiDiv.putMoney(shoukei);
}

/**
 * （DenpyouMeisaiList→親JSP別JS呼び出し）
 * 明細行の更新時の処理
 * 明細部の更新時（追加・変更）に伝票本体に反映する
 *
 */
function updateDenpyouMeisaiEvent(meisaiInfo) {
}

//明細ダイアログ（共通）で親を呼ぶためのインターフェース
function getKikanFrom() {return $("[name=seisankikanFrom]")};
function getKikanTo() {return $("[name=seisankikanTo]")};

/**
 * 明細の金額計算、金額の合計値を計算するFunction
 */
function calcMoney() {

	//小計
	amountKingaku($("#meisaiList01 input[name=meisaiKingaku]"), $("[name='shoukei01']"));
	amountKingaku($("#meisaiList02 input[name=meisaiKingaku]"), $("[name='shoukei02']"));
	amountKingaku($("#meisaiList input[name=shiharaiKingaku]"), $("[name='shoukei03']"));
	amountKingaku($("#kaigaiMeisaiList01 input[name=meisaiKingaku]"), $("[name='shoukei04']"));
	amountKingaku($("#kaigaiMeisaiList02 input[name=meisaiKingaku]"), $("[name='shoukei05']"));

<c:if test='${sasihikiHyoujiFlg}'>
	// 小計から差引金額を減額
	calcSashihikiMoney(0);
</c:if>
<c:if test='${sasihikiHyoujiFlgKaigai}'>
	// 小計から差引金額を減額
	calcSashihikiMoney(1);
</c:if>

	// 伝票の金額(明細の金額合計)
	amountKingaku($("[name='shoukei01'],[name='shoukei02'],[name='shoukei03'],[name='shoukei04'],[name='shoukei05']"), $("[name='kingaku']"));

}

//画面表示後の初期化
$(document).ready(function(){

	// 明細表示をロード
	setDisplayKaigaiRyohiMeisaiData(1);
	setDisplayKaigaiRyohiMeisaiData(2);
	setDisplayRyohiMeisaiData(1);
	setDisplayRyohiMeisaiData(2);
	setDisplayMeisaiData();

	var num = $("[name='karibaraiOn']:checked").val();
	<c:choose>
	<c:when test="${karibaraiSentakuFlg eq 0}">
		//仮払なしのみ
		$("[name='karibaraiOn']").val(["0"]);
		$("[name='karibaraiOn']").attr("disabled", "disabled");
		$("[name='karibaraiKingaku']").attr("disabled", "disabled");
		<c:if test='${not yosanShikkouTaishouFlg}'>
			$("#headerField").children("div:not('.never_show')").css("display","none");
			$("#shiharai").css("display","none");
			$("#karibaraiAriArea").children("div:not('.never_show')").css("display","none");
		</c:if>
		$("[name='karibaraiKingaku']").val("");
	</c:when>
	<c:when test="${karibaraiSentakuFlg eq 1}">
		//仮払ありのみ
		$("[name='karibaraiOn']").val(["1"]);
		$("[name='karibaraiOn']").attr("disabled", "disabled");
		$("[name='karibaraiKingaku']").removeAttr("disabled");
	    $("#headerField").children("div:not('.never_show')").css("display","");
		$("#shiharai").css("display","");
		$("#karibaraiAriArea").children("div:not('.never_show')").css("display","");
	</c:when>
	<c:when test="${karibaraiSentakuFlg eq 2}">
		//仮払あり・なし
		//仮払ありのとき仮払金額を変更可
		$("[name='karibaraiOn']").removeAttr("disabled");
		if( num == 1 ){
			$("[name='karibaraiKingaku']").removeAttr("disabled");
			$("#headerField").children("div:not('.never_show')").css("display","");
			$("#shiharai").css("display","");
			$("#karibaraiAriArea").children("div:not('.never_show')").css("display","");
		}else {
			$("[name='karibaraiKingaku']").attr("disabled", "disabled");
			<c:if test='${not yosanShikkouTaishouFlg}'>
				$("#headerField").children("div:not('.never_show')").css("display","none");
				$("#shiharai").css("display","none");
				$("#karibaraiAriArea").children("div:not('.never_show')").css("display","none");
				$("#karibaraiAriArea").children("div:not('.never_show')").find("input").val("");
				$("input[name=hf1Cd]").val("");
				$("input[name=hf1Name]").val("");
				$("input[name=hf2Cd]").val("");
				$("input[name=hf2Name]").val("");
				$("input[name=hf3Cd]").val("");
				$("input[name=hf3Name]").val("");
				$("input[name=hf4Cd]").val("");
				$("input[name=hf4Name]").val("");
				$("input[name=hf5Cd]").val("");
				$("input[name=hf5Name]").val("");
				$("input[name=hf6Cd]").val("");
				$("input[name=hf6Name]").val("");
				$("input[name=hf7Cd]").val("");
				$("input[name=hf7Name]").val("");
				$("input[name=hf8Cd]").val("");
				$("input[name=hf8Name]").val("");
				$("input[name=hf9Cd]").val("");
				$("input[name=hf9Name]").val("");
				$("input[name=hf10Cd]").val("");
				$("input[name=hf10Name]").val("");
			</c:if>
			$("[name='karibaraiKingaku']").val("");
		}
		//仮払ラジオボタン変更時Function
		$("[name='karibaraiOn']").click(function(){
			var num = $("[name='karibaraiOn']:checked").val();
			if( num == 1 ){
				$("[name='karibaraiKingaku']").removeAttr("disabled");
				$("#headerField").children("div:not('.never_show')").css("display","");
				$("#shiharai").css("display","");
				$("#karibaraiAriArea").children("div:not('.never_show')").css("display","");
				<c:if test="${shiharaiBiMode % 2 == 1}">
					$("#shiharaibi").css("display","");
					$("input[name=shiharaibi]").removeAttr("disabled");
				</c:if>
			}else {
				$("[name='karibaraiKingaku']").attr("disabled", "disabled");
				<c:if test='${not yosanShikkouTaishouFlg}'>
					$("#headerField").children("div:not('.never_show')").css("display","none");
					$("#shiharai").css("display","none");
					$("input[name=shiharaiKiboubi]").val("");
					$("#karibaraiAriArea").children("div:not('.never_show')").css("display","none");
					$("#karibaraiAriArea").children("div:not('.never_show')").find("input").val("");
					$("input[name=hf1Cd]").val("");
					$("input[name=hf1Name]").val("");
					$("input[name=hf2Cd]").val("");
					$("input[name=hf2Name]").val("");
					$("input[name=hf3Cd]").val("");
					$("input[name=hf3Name]").val("");
					$("input[name=hf4Cd]").val("");
					$("input[name=hf4Name]").val("");
					$("input[name=hf5Cd]").val("");
					$("input[name=hf5Name]").val("");
					$("input[name=hf6Cd]").val("");
					$("input[name=hf6Name]").val("");
					$("input[name=hf7Cd]").val("");
					$("input[name=hf7Name]").val("");
					$("input[name=hf8Cd]").val("");
					$("input[name=hf8Name]").val("");
					$("input[name=hf9Cd]").val("");
					$("input[name=hf9Name]").val("");
					$("input[name=hf10Cd]").val("");
					$("input[name=hf10Name]").val("");
				</c:if>
				<c:if test="${shiharaiBiMode % 2 == 1}">
					$("#shiharaibi").css("display","none");
					$("input[name=shiharaibi]").val("");
					$("input[name=shiharaibi]").attr("disabled", "disabled");
				</c:if>
				$("[name='karibaraiKingaku']").val("");
			}

			//タイトルの変更
			changeTitle();
		});
	</c:when>
	</c:choose>

<c:if test='${enableInput}'>

	var dairiFlg = $("#workflowForm").find("input[name=dairiFlg]").val();
	//通常起票時：使用者固定
	if (dairiFlg == "0") {
		$("#userSentakuRyohiButton").prop("disabled", true);
		$("input[name=shainNoRyohi]").prop("disabled", true);
	}

	//仮払ありのとき、仮払未使用ボタンを表示
	if ($("input[name=karibaraiOn]").val() == "1") {
		$("div#karibaraiAnken").find("thead").find("th:last").css("display", "");
		$("div#karibaraiAnken").find("tbody").find("td:last").css("display", "");
	} else {
		$("div#karibaraiAnken").find("thead").find("th:last").css("display", "none");
		$("div#karibaraiAnken").find("tbody").find("td:last").css("display", "none");
	}

	//使用者選択
	$("#userSentakuRyohiButton").click(function(e){
		var title = $(this).parent().parent().find(".control-label").text();
		var shiyoushaStr = '${su:htmlEscape(ks.userName.name)}';
		dialogRetuserId   = $(this).closest("div").find("input[name=userIdRyohi]");
		dialogRetusername = $(this).closest("div").find("input[name=userNameRyohi]");
		dialogRetshainNo  = $(this).closest("div").find("input[name=shainNoRyohi]");
		dialogCallbackShainNoLostFocus = function(){
			changeShiyousha(dialogRetuserId,dialogRetusername,dialogRetshainNo,title,shiyoushaStr);
		};
		commonUserSentaku();
	});

	// 社員番号変更時Function
	 $("input[name=shainNoRyohi]").change(function shaiNoLostFocus() {
		var title = $(this).parent().parent().find(".control-label").text();
		var shiyoushaStr = '${su:htmlEscape(ks.userName.name)}';
		dialogRetuserId   = $(this).closest("div").find("input[name=userIdRyohi]");
		dialogRetusername = $(this).closest("div").find("input[name=userNameRyohi]");
		dialogRetshainNo  = $(this).closest("div").find("input[name=shainNoRyohi]");
		dialogCallbackShainNoLostFocus = function(){
			changeShiyousha(dialogRetuserId,dialogRetusername,dialogRetshainNo,title,shiyoushaStr);
		};
		commonShainNoLostFocus(dialogRetshainNo, dialogRetusername, title, dialogRetuserId, false);
	});

	//取引選択
	$("#torihikiSentakuButton").click(function(e){
		ryohiTorihikiSentaku();
	});

	//仕訳枝番号変更時Function
	$("input[name=shiwakeEdaNoRyohi]").change(function(){
		ryohiShiwakeEdaNoLostFocus();
	});

	//勘定科目枝番選択ボタン押下時Function
	$("#kamokuEdabanSentakuButton").click(function(e){
		dialogRetKamokuEdabanCd				= $("input[name=kamokuEdabanCdRyohi]");
		dialogRetKamokuEdabanName			= $("input[name=kamokuEdabanNameRyohi]");
		commonKamokuEdabanSentaku($("[name=kamokuCdRyohi]").val());
	});

	//勘定科目枝番コードロストフォーカス時Function
	$("input[name=kamokuEdabanCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var kamokuCd = 	$("input[name=kamokuCdRyohi]").val();
		dialogRetKamokuEdabanCd				= $("input[name=kamokuEdabanCdRyohi]");
		dialogRetKamokuEdabanName			= $("input[name=kamokuEdabanNameRyohi]");
		commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamokuCd, title);
	});

	//負担部門選択ボタン押下時Function
	$("#futanBumonSentakuButton").click(function(e){
		dialogRetFutanBumonCd				= $("input[name=futanBumonCdRyohi]");
		dialogRetFutanBumonName				= $("input[name=futanBumonNameRyohi]");
		commonFutanBumonSentaku("1",$("input[name=kamokuCdRyohi]").val(),$("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val());
	});

	//負担部門コードロストフォーカス時Function
	$("input[name=futanBumonCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetFutanBumonCd				= $("input[name=futanBumonCdRyohi]");
		dialogRetFutanBumonName				= $("input[name=futanBumonNameRyohi]");
		commonFutanBumonCdLostFocus("1",dialogRetFutanBumonCd, dialogRetFutanBumonName, title, $("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val());
	});

	//取引先選択ボタン押下時Function
	$("#torihikisakiSentakuButton").click(function(e){
		dialogRetTorihikisakiCd				= $("input[name=torihikisakiCdRyohi]");
		dialogRetTorihikisakiName			= $("input[name=torihikisakiNameRyohi]");
		dialogRetFurikomisaki 				= $("input[name=furikomisakiJouhouRyohi]");
		commonTorihikisakiSentaku();
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
		dialogRetTorihikisakiCd				= $("input[name=torihikisakiCdRyohi]");
		dialogRetTorihikisakiName			= $("input[name=torihikisakiNameRyohi]");
		dialogRetFurikomisaki 				= $("input[name=furikomisakiJouhouRyohi]");
		commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd, dialogRetTorihikisakiName, title, dialogRetFurikomisaki);
	});

	//プロジェクト選択ボタン押下時Function
	$("#projectSentakuButton").click(function(e){
		dialogRetProjectCd	= $("input[name=projectCdRyohi]");
		dialogRetProjectName = $("input[name=projectNameRyohi]");
		commonProjectSentaku();
	});

	//プロジェクトコードロストフォーカス時Function
	$("input[name=projectCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetProjectCd	= $("input[name=projectCdRyohi]");
		dialogRetProjectName = $("input[name=projectNameRyohi]");
		commonProjectCdLostFocus(dialogRetProjectCd, dialogRetProjectName, title);
	});

	//セグメント選択ボタン押下時Function
	$("#segmentSentakuButton").click(function(e){
		dialogRetSegmentCd				= $("input[name=segmentCdRyohi]");
		dialogRetSegmentName			= $("input[name=segmentNameRyohi]");
		commonSegmentSentaku();
	});

	//セグメントコードロストフォーカス時Function
	$("input[name=segmentCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetSegmentCd				= $("input[name=segmentCdRyohi]");
		dialogRetSegmentName			= $("input[name=segmentNameRyohi]");
		commonSegmentCdLostFocus(dialogRetSegmentCd, dialogRetSegmentName, title);
	});

	//差引回数日数変更時の金額再計算
	$("input[name=sashihikiNum]").blur(calcMoney);
	$("input[name=sashihikiNumKaigai]").blur(calcMoney);

	// 精算期間を本体から明細に反映（明細の期間未入力の時=参照起票のみ)
	$("input[name=seisankikanFrom]").change(function(){
		if ($(this).val() == "") return;
		$("input[name=kikanFrom]").each(function(){
			if ($(this).val() == "") {
				$(this).val($("input[name=seisankikanFrom]").val());
			}
		});
		setDisplayKaigaiRyohiMeisaiData(1);
		setDisplayKaigaiRyohiMeisaiData(2);
		setDisplayRyohiMeisaiData(1);
		setDisplayRyohiMeisaiData(2);
	});
	$("input[name=seisankikanTo]").change(function(){
		if ($(this).val() == "") return;
		$("input[name=kikanTo]").each(function(){
			if ($(this).val() == "" && $(this).prevAll("[name=shubetsuCd]").val() == "2") {
				$(this).val($("input[name=seisankikanTo]").val());
			}
		});
		setDisplayKaigaiRyohiMeisaiData(2);
		setDisplayRyohiMeisaiData(2);
	});
</c:if>

<c:if test='${not enableInput}'>
	//起票モードの場合のみ表示可能に
	$("#kaikeiContent").find("button").css("display", "none");
	$("#kaikeiContent").find("input,textarea,select").prop("disabled", true);
</c:if>

	//タイトルの変更
	changeTitle();

});

/**
 * イベントボタン押下時のアクションの切り替え
 */
function eventBtn(eventName) {
	var formObject = document.getElementById("workflowForm");

	// 支払日チェック
	var checkShiharaiBi = ($("input[name=shiharaibi]").prop("disabled") == false && $("input[name=karibaraiOn]:checked").val() == '1');

	//支払日更新チェック
	if (checkShiharaiBi) {
		var shiharaiBi = $("input[name=shiharaibi]").val();
		if ("" == shiharaiBi) {
			//20220517
			if(!alert("支払日が未入力です。\n支払日を入力して、更新してください。")){
				$(formObject).find('button[type=button]').not('#chiharaibiCheckNotPass').removeAttr("disabled");
			}
			//alert("支払日が未入力です。\n支払日を入力して、更新してください。");
			return;
		}
		if ($("#keijoubi").css("display") != "none") {
			var keijouBi = $("input[name=keijoubi]").val();
			if ("" == keijouBi) {
				if(!alert("計上日が未入力です。\n計上日を入力して、更新してください。")){
					$(formObject).find('button[type=button]').not('#chiharaibiCheckNotPass').removeAttr("disabled");
				}
				//alert("計上日が未入力です。\n計上日を入力して、更新してください。");
				return;
			}
		}
	}

	switch (eventName) {
	case 'entry':
		formObject.action = 'kaigai_ryohi_karibarai_shinsei_touroku';
		break;
	case 'update':
		formObject.action = 'kaigai_ryohi_karibarai_shinsei_koushin';
		break;
	case 'shinsei':
		formObject.action = 'kaigai_ryohi_karibarai_shinsei_shinsei';
		break;
	case 'shounin':
		formObject.action = 'kaigai_ryohi_karibarai_shinsei_shounin';
		break;
	case 'shouninkaijo':
		formObject.action = 'kaigai_ryohi_karibarai_shinsei_shouninkaijo';
		break;
	}

	formObject.method = 'post';
	formObject.target = '_self';
	$(formObject).submit();
}

/**
 * 取引選択
 *【WARNING】こちら変更した場合は必ずryohiShiwakeEdaNoLostFocus()もメンテすること【WARNING】
 */
function ryohiTorihikiSentaku(){
	var denpyouKbn = $("input[name=denpyouKbn]").val();
	var dairiFlg = $("[name=dairiFlg]").val();
	var userId = "";
	if(denpyouKbn == "A012" && dairiFlg == "1") {
		userId = $("input[name=userIdRyohi]").val();
		if(userId == "") {
			alert("使用者名を入力してください。");
			return;
		}
	}
	setRyohiTorihikiDialogRet();
	dialogCallbackTorihikiSentaku = function() { ryohiShiwakeEdaNoLostFocus() };
	commonTorihikiSentaku($("input[name=denpyouKbn]").val(), $("input[name=kihyouBumonCd]").val(), $("input[name=denpyouId]").val(), $("input[name=daihyouFutanBumonCd]").val(),userId);
};

/**
 * 仕訳枝番号変更時Function
 *【WARNING】こちら変更した場合は必ずryohiTorihikiSentaku()もメンテすること【WARNING】
 */
function ryohiShiwakeEdaNoLostFocus(){
	var denpyouKbn = $("input[name=denpyouKbn]").val();
	var dairiFlg = $("[name=dairiFlg]").val();
	var userId = "";
	if(denpyouKbn == "A012" && dairiFlg == "1") {
		userId = $("input[name=userIdRyohi]").val();
		if(userId == "") {
			alert("使用者名を入力してください。");
			return;
		}
	}
	setRyohiTorihikiDialogRet();
	var title = $("input[name=shiwakeEdaNoRyohi]").parent().parent().find(".control-label").text();
	commonShiwakeEdaNoLostFocus(dialogRetShiwakeEdaNo, dialogRetTorihikiName, title, $("input[name=denpyouKbn]").val(), $("input[name=kihyouBumonCd]").val(), $("input[name=denpyouId]").val(), $("input[name=daihyouFutanBumonCd]").val(),userId);
};

/**
 * 取引ダイアログ表示後の設定項目Function
 */
function setRyohiTorihikiDialogRet() {
	dialogRetTorihikiName				= $("input[name=torihikiNameRyohi]");
	dialogRetShiwakeEdaNo				= $("input[name=shiwakeEdaNoRyohi]");
	dialogRetKamokuCd					= $("input[name=kamokuCdRyohi]");
	dialogRetKamokuName					= $("input[name=kamokuNameRyohi]");
	dialogRetKamokuEdabanCd				= $("input[name=kamokuEdabanCdRyohi]");
	dialogRetKamokuEdabanName			= $("input[name=kamokuEdabanNameRyohi]");
	dialogRetKamokuEdabanSentakuButton	= $("#kamokuEdabanSentakuButton");
	dialogRetFutanBumonCd				= $("input[name=futanBumonCdRyohi]");
	dialogRetFutanBumonName				= $("input[name=futanBumonNameRyohi]");
	dialogRetFutanBumonSentakuButton	= $("#futanBumonSentakuButton");
	dialogRetTorihikisakiCd				= $("input[name=torihikisakiCdRyohi]");
	dialogRetTorihikisakiName			= $("input[name=torihikisakiNameRyohi]");
	dialogRetFurikomisaki 				= $("input[name=furikomisakiJouhouRyohi]");
	dialogRetTorihikisakiSentakuButton	= $("#torihikisakiSentakuButton");
	dialogRetTorihikisakiClearButton	= $("#torihikisakiClearButton");
	dialogRetProjectCd					= $("input[name=projectCdRyohi]");
	dialogRetProjectName				= $("input[name=projectNameRyohi]");
	dialogRetProjectSentakuButton		= $("#projectSentakuButton");
	dialogRetSegmentCd					= $("input[name=segmentCdRyohi]");
	dialogRetSegmentName				= $("input[name=segmentNameRyohi]");
	dialogRetSegmentSentakuButton		= $("#segmentSentakuButton");
	dialogRetTekiyou					= $("input[name=tekiyouRyohi]");
<c:choose>
	<c:when test="${'UF1' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf1CdRyohi]");
		dialogRetSahinName	= $("input[name=uf1NameRyohi]");
	</c:when>
	<c:when test="${'UF2' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf2CdRyohi]");
		dialogRetSahinName	= $("input[name=uf2NameRyohi]");
	</c:when>
	<c:when test="${'UF3' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf3CdRyohi]");
		dialogRetSahinName	= $("input[name=uf3NameRyohi]");
	</c:when>
	<c:when test="${'UF4' == shainCdRenkeiArea}">
	  dialogRetShainCd = $("input[name=uf4CdRyohi]");
	  dialogRetSahinName = $("input[name=uf4NameRyohi]");
	 </c:when>
	<c:when test="${'UF5' == shainCdRenkeiArea}">
	  dialogRetShainCd = $("input[name=uf5CdRyohi]");
	  dialogRetSahinName = $("input[name=uf5NameRyohi]");
	 </c:when>
	<c:when test="${'UF6' == shainCdRenkeiArea}">
	  dialogRetShainCd = $("input[name=uf6CdRyohi]");
	  dialogRetSahinName = $("input[name=uf6NameRyohi]");
	 </c:when>
	<c:when test="${'UF7' == shainCdRenkeiArea}">
	  dialogRetShainCd = $("input[name=uf7CdRyohi]");
	  dialogRetSahinName = $("input[name=uf7NameRyohi]");
	 </c:when>
	<c:when test="${'UF8' == shainCdRenkeiArea}">
	  dialogRetShainCd = $("input[name=uf8CdRyohi]");
	  dialogRetSahinName = $("input[name=uf8NameRyohi]");
	 </c:when>
	<c:when test="${'UF9' == shainCdRenkeiArea}">
	  dialogRetShainCd = $("input[name=uf9CdRyohi]");
	  dialogRetSahinName = $("input[name=uf9NameRyohi]");
	 </c:when>
	<c:when test="${'UF10' == shainCdRenkeiArea}">
	  dialogRetShainCd = $("input[name=uf10CdRyohi]");
	  dialogRetSahinName = $("input[name=uf10NameRyohi]");
	 </c:when>
</c:choose>
	<c:if test="${'UF1' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf1ShiyouFlg}">
		dialogRetUf1Cd				= $("input[name=uf1CdRyohi]");
		dialogRetUf1Name			= $("input[name=uf1NameRyohi]");
		dialogRetUf1SentakuButton	= $("#uf1SentakuButton");
	</c:if>
	<c:if test="${'UF2' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf2ShiyouFlg}">
		dialogRetUf2Cd				= $("input[name=uf2CdRyohi]");
		dialogRetUf2Name			= $("input[name=uf2NameRyohi]");
		dialogRetUf2SentakuButton	= $("#uf2SentakuButton");
	</c:if>
	<c:if test="${'UF3' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf3ShiyouFlg}">
		dialogRetUf3Cd				= $("input[name=uf3CdRyohi]");
		dialogRetUf3Name			= $("input[name=uf3NameRyohi]");
		dialogRetUf3SentakuButton	= $("#uf3SentakuButton");
	</c:if>
	<c:if test="${'UF4' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf4ShiyouFlg}">
	  dialogRetUf4Cd    = $("input[name=uf4CdRyohi]");
	  dialogRetUf4Name   = $("input[name=uf4NameRyohi]");
	  dialogRetUf4SentakuButton = $("#uf4SentakuButton");
	 </c:if>
	 <c:if test="${'UF5' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf5ShiyouFlg}">
	  dialogRetUf5Cd    = $("input[name=uf5CdRyohi]");
	  dialogRetUf5Name   = $("input[name=uf5NameRyohi]");
	  dialogRetUf5SentakuButton = $("#uf5SentakuButton");
	 </c:if>
	 <c:if test="${'UF6' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf6ShiyouFlg}">
	  dialogRetUf6Cd    = $("input[name=uf6CdRyohi]");
	  dialogRetUf6Name   = $("input[name=uf6NameRyohi]");
	  dialogRetUf6SentakuButton = $("#uf6SentakuButton");
	 </c:if>
	 <c:if test="${'UF7' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf7ShiyouFlg}">
	  dialogRetUf7Cd    = $("input[name=uf7CdRyohi]");
	  dialogRetUf7Name   = $("input[name=uf7NameRyohi]");
	  dialogRetUf7SentakuButton = $("#uf7SentakuButton");
	 </c:if>
	 <c:if test="${'UF8' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf8ShiyouFlg}">
	  dialogRetUf8Cd    = $("input[name=uf8CdRyohi]");
	  dialogRetUf8Name   = $("input[name=uf8NameRyohi]");
	  dialogRetUf8SentakuButton = $("#uf8SentakuButton");
	 </c:if>
	 <c:if test="${'UF9' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf9ShiyouFlg}">
	  dialogRetUf9Cd    = $("input[name=uf9CdRyohi]");
	  dialogRetUf9Name   = $("input[name=uf9NameRyohi]");
	  dialogRetUf9SentakuButton = $("#uf9SentakuButton");
	 </c:if>
	 <c:if test="${'UF10' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf10ShiyouFlg}">
	  dialogRetUf10Cd    = $("input[name=uf10CdRyohi]");
	  dialogRetUf10Name   = $("input[name=uf10NameRyohi]");
	  dialogRetUf10SentakuButton = $("#uf10SentakuButton");
	 </c:if>
}

/**
 * タイトルの変更
 */
function changeTitle() {
	var num = $("[name='karibaraiOn']:checked").val();
	var title = document.getElementById("title");
	if( num == 0 ){
		title.innerHTML = "${su:htmlEscape(denpyouKaribaraiNashiShubetsu)}";
	}
	else {
		title.innerHTML = "${su:htmlEscape(denpyouShubetsu)}";
	}
}

/**
 * 使用者変更時に明細情報をリセット
 * @param dialogRetuserId		ユーザーID反映部(commonShainNoLostFocus用)
 * @param dialogRetusername		ユーザー名反映部(commonShainNoLostFocus用)
 * @param dialogRetshainNo		社員番号反映部(commonShainNoLostFocus用)
 * @param title					タイトル文字列(commonShainNoLostFocus用)
 * @param shiyoushaStr			確認ダイアログ「使用者」部文言
 */
function changeShiyousha(dialogRetuserId, dialogRetusername, dialogRetshainNo, title, shiyoushaStr){
	if(dialogRetuserId.val() == "" || dialogRetshainNo.val() == "") return;
	var shainNoBefChg = $("input[name=shainNoBefChg]").val();
	if(shainNoBefChg != "" && dialogRetshainNo.val() != shainNoBefChg){
		if(window.confirm(shiyoushaStr  + "の変更にあたり、入力内容がクリアされます。よろしいですか？")){
			//全記入内容クリアのため、action再呼出
			var hrefStr = "kaigai_ryohi_karibarai_shinsei?denpyouKbn=A012&dairiFlg=1&shiyoushaHenkouShainNo=" + dialogRetshainNo.val();
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

		</script>
	</body>
</html>
