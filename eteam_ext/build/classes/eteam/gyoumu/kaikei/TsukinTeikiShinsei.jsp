<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- 中身 -->
<div id='kaikeiContent' class='form-horizontal'>
	<input type='hidden' name="tenyuuryokuFlg" value='${su:htmlEscape(tenyuuryokuFlg)}'>

	<!-- 申請内容 -->
	<section class='print-unit'>
		<h2>申請内容</h2>
		<div>
			<div class='control-group' <c:if test='${not ks.torihiki.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.torihiki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.torihiki.name)}</label>
				<div class='controls'>
					<input type="text" name='shiwakeEdaNo'  value='${su:htmlEscape(shiwakeEdaNo)}' class='input-small pc_only'>
					<input type='text' name='torihikiName' class='input-xlarge' disabled value='${su:htmlEscape(torihikiName)}'>
					<button type='button' id='torihikiSentakuButton' class='btn btn-small'>選択</button>
					<span style="line-height:25px;"><br><span style="color:#ff0000;">${su:htmlEscape(chuuki2)}</span></span>
				</div>
			</div>
			<%@ include file="./kogamen/HeaderField.jsp" %>
			<div class='control-group' <c:if test='${not ks.kamoku.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.kamoku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kamoku.name)}</label>
				<div class='controls'>
					<input type='text' name='kamokuCd' class='input-small pc_only' disabled value='${su:htmlEscape(kamokuCd)}'>
					<input type='text' name='kamokuName' class='input-xlarge' disabled value='${su:htmlEscape(kamokuName)}'>
					<input type='hidden' name='shoriGroup' value='${su:htmlEscape(shoriGroup)}'>
				</div>
			</div>
			<!-- 課税区分 -->
			<div class='control-group' <c:if test='${not ks.kazeiKbn.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.kazeiKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kazeiKbn.name)}</label>
				<div class='controls'>
					<select name='kazeiKbn' class='input-medium' disabled>
						<c:forEach var="kazeiKbnRecord" items="${kazeiKbnList}">
							<option value='${kazeiKbnRecord.naibu_cd}' data-kazeiKbnGroup='${kazeiKbnRecord.option1}' <c:if test='${kazeiKbnRecord.naibu_cd eq kazeiKbn}'>selected</c:if>>${su:htmlEscape(kazeiKbnRecord.name)}</option>
						</c:forEach>
					</select>
				<!-- 税率 -->
				<span id='zeiritsuArea' <c:if test='${not ks.zeiritsu.hyoujiFlg}'>style='display:none;'</c:if>>
					<label class='label' for='zeiritsu' ><c:if test='${ks.zeiritsu.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.zeiritsu.name)}</label>
					<select name='zeiritsu' class='input-small'>
						<c:forEach var="zeiritsuRecord" items="${zeiritsuList}">
							<option value='${zeiritsuRecord.zeiritsu}' data-hasuuKeisanKbn='${zeiritsuRecord.hasuu_keisan_kbn}' <c:if test='${zeiritsuRecord.zeiritsu eq zeiritsu}'>selected</c:if>>${zeiritsuRecord.zeiritsu}%</option>
						</c:forEach>
					</select>
				</span>
				<!-- 分離区分 -->
				<span <c:if test='${not ks.bunriKbn.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='label' for='bunriKbn'><c:if test='${ks.bunriKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.bunriKbn.name)}</label>
					<select name='bunriKbn' id="bunriKbn" class='input-small' disabled>
						<c:forEach var="bunriKbnRecord" items="${bunriKbnList}">
						<c:if test='${bunriKbnRecord.naibu_cd eq "9"}'>
							<option hidden value='${bunriKbnRecord.naibu_cd}' <c:if test='${bunriKbnRecord.naibu_cd eq bunriKbn}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
						</c:if>
						<c:if test='${bunriKbnRecord.naibu_cd ne "9"}'>
							<option value='${bunriKbnRecord.naibu_cd}'<c:if test='${bunriKbnRecord.naibu_cd eq bunriKbn}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
						</c:if>
						</c:forEach>
					</select>
				</span>
				<!-- 仕入区分 -->
				<span <c:if test='${shiireZeiAnbun eq "0"}'>style='display:none;'</c:if>>
				<label class='label' for='kariShiireKbn'>${su:htmlEscape(ks.shiireKbn.name)}</label>
					<select name='kariShiireKbn' id="kariShiireKbn" class='input-small' disabled>
						<c:forEach var="shiireKbnRecord" items="${shiireKbnList}">
						<c:if test='${shiireKbnRecord.naibu_cd eq "0"}'>
							<option hidden value='${shiireKbnRecord.naibu_cd}' <c:if test='${shiireKbnRecord.naibu_cd eq kariShiireKbn}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
						</c:if>
						<c:if test='${shiireKbnRecord.naibu_cd ne "0"}'>
							<option value='${shiireKbnRecord.naibu_cd}' <c:if test='${shiireKbnRecord.naibu_cd eq kariShiireKbn}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
						</c:if>
							
						</c:forEach>
					</select>
				</span>
				<input type='hidden' name='kariShiireKbnVal' value='${su:htmlEscape(kariShiireKbn)}'>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ks.kamokuEdaban.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.kamokuEdaban.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kamokuEdaban.name)}</label>
				<div class='controls'>
					<input type='text' name="kamokuEdabanCd" class='input-medium pc_only' value='${su:htmlEscape(kamokuEdabanCd)}' <c:if test='${not kamokuEdabanEnable}'>disabled</c:if>>
					<input type='text' name="kamokuEdabanName" class='input-xlarge' value='${su:htmlEscape(kamokuEdabanName)}' disabled>
					<button type='button' id='kamokuEdabanSentakuButton' class='btn btn-small' <c:if test='${not kamokuEdabanEnable}'>disabled</c:if>>選択</button>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ks.futanBumon.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.futanBumon.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.futanBumon.name)}</label>
				<div class='controls'>
					<input type='text' name='futanBumonCd' class='input-small pc_only'  value='${su:htmlEscape(futanBumonCd)}' <c:if test='${not futanBumonEnable}'>disabled</c:if>>
					<input type='text' name='futanBumonName' class='input-xlarge' value='${su:htmlEscape(futanBumonName)}' disabled>
					<button type='button' id='futanBumonSentakuButton' class='btn btn-small' <c:if test='${not futanBumonEnable}'>disabled</c:if>>選択</button>
				</div>
			</div>
		<!-- 取引先 -->
		<div class='control-group <c:if test='${not ks.torihikisaki.hyoujiFlg}'>never_show' style='display:none;</c:if>'>
			<label class='control-label'><c:if test='${ks.torihikisaki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.torihikisaki.name)}</label>
			<div class='controls'>
				<input type='text' name="torihikisakiCd" class='input-medium pc_only' <c:if test='${not torihikisakiEnable}'>disabled</c:if> value='${su:htmlEscape(torihikisakiCd)}'>
				<input type='text' name="torihikisakiName" class='input-xlarge' value='${su:htmlEscape(torihikisakiName)}' disabled>
				<button type='button' id='torihikisakiSentakuButton' class='btn btn-small' <c:if test='${not torihikisakiEnable}'>disabled</c:if>>選択</button>
				<button type='button' id='torihikisakiClearButton' class='btn btn-small' <c:if test='${not torihikisakiEnable}'>disabled</c:if>>クリア</button>
			</div>
		</div>
		<!-- プロジェクト -->
		<div class='control-group <c:if test='${not("0" ne pjShiyouFlg and ks.project.hyoujiFlg)}'>never_show' style='display:none;</c:if>'>
			<label class='control-label'><c:if test='${ks.project.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.project.name)}</label>
			<div class='controls'>
				<input type='text' name="projectCd" class='pc_only' <c:if test='${not projectEnable}'>disabled</c:if> value='${su:htmlEscape(projectCd)}'>
				<input type='text' name="projectName" class='input-xlarge' disabled value='${su:htmlEscape(projectName)}'>
				<button type='button' id='projectSentakuButton' class='btn btn-small' <c:if test='${not projectEnable}'>disabled</c:if>>選択</button>
			</div>
		</div>
		<!-- セグメント -->
		<div class='control-group <c:if test='${not("0" ne segmentShiyouFlg and ks.segment.hyoujiFlg)}'>never_show' style='display:none;</c:if>'>
			<label class='control-label'><c:if test='${ks.segment.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.segment.name)}</label>
			<div class='controls'>
				<input type='text' name="segmentCd" class='pc_only' <c:if test='${not segmentEnable}'>disabled</c:if> value='${su:htmlEscape(segmentCd)}'>
				<input type='text' name="segmentName" class='input-xlarge' disabled value='${su:htmlEscape(segmentName)}'>
				<button type='button' id='segmentSentakuButton' class='btn btn-small' <c:if test='${not segmentEnable}'>disabled</c:if>>選択</button>
			</div>
		</div>
		<!-- 支払先名 -->
		<div class='control-group invoiceOnly' id='shiharaisakiDiv' <c:if test='${invoiceDenpyou eq "1" or not ks.shiharaisaki.hyoujiFlg}'>style='display:none;'</c:if>>
			<label class='control-label'><c:if test='${ks.shiharaisaki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaisaki.name)}</label>
			<div class='controls'>
				<input type='text' name='shiharaisakiName' maxlength='60' class='input-xxlarge' value='${su:htmlEscape(shiharaisakiName)}'>
			</div>
		</div>
		<!-- 事業者区分 -->
		<div class='control-group invoiceOnly' id='jigyoushaKbnDiv' name='jigyoushaKbn'<c:if test='${invoiceDenpyou eq "1" or not ks.jigyoushaKbn.hyoujiFlg}'>style='display:none;'</c:if>>
			<label class='control-label' for='jigyoushaKbn'>${su:htmlEscape(ks.jigyoushaKbn.name)}</label>
			<div class='controls'>
				<c:forEach var="jigyoushaKbnRecord" items="${jigyoushaKbnList}">
					<label class='radio inline'><input type='radio' name='jigyoushaKbn' value='${su:htmlEscape(jigyoushaKbnRecord.naibu_cd)}' <c:if test='${jigyoushaKbnRecord.naibu_cd eq jigyoushaKbn}'>checked </c:if>>${su:htmlEscape(jigyoushaKbnRecord.name)}</label>
				</c:forEach>
			</div>
		</div>
			<%@ include file="./kogamen/UniversalField.jsp" %>
			<div class='control-group' <c:if test='${not ks.shiyouKikanKbn.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.shiyouKikanKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiyouKikanKbn.name)}</label>
				<div class='controls'>
<c:forEach var="shiyouKikanList" items="${shiyouKikanList}">
					<label class='radio inline'><input type='radio' name='shiyouKikanKbn' value='${su:htmlEscape(shiyouKikanList.naibu_cd)}' <c:if test='${shiyouKikanList.naibu_cd eq shiyouKikanKbn}'>checked</c:if>>${su:htmlEscape(shiyouKikanList.name)}</label>
</c:forEach>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ks.shiyouKaishibi.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.shiyouKaishibi.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiyouKaishibi.name)}</label>
				<div class='controls' <c:if test='${not ks.shiyouShuuryoubi.hyoujiFlg}'>style='display:none;'</c:if>>
					<input type='text' name='shiyouKaishiBi' class='input-small datepicker' value='${su:htmlEscape(shiyouKaishiBi)}'>
					<label class="label label-sub"> <c:if test='${ks.shiyouKaishibi.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiyouShuuryoubi.name)}</label>
					<input type='text' name='shiyouShuuryouBi' class='input-small datepicker' <c:if test='${not ks.shiyouShuuryoubi.hyoujiFlg}'>style='display:none;'</c:if> value='${su:htmlEscape(shiyouShuuryouBi)}' disabled>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ks.jyoushaKukan.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.jyoushaKukan.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.jyoushaKukan.name)}</label>
				<div class='controls'>
					<textarea name='jyoushaKukan' class='input-block-level'>${su:htmlEscape(jyoushaKukan)}</textarea>
					<input type='hidden' name='teikiSerializeData' value='${su:htmlEscape(teikiSerializeData)}'>
					<p id='tenyuuryokuView' style='padding-top:1px'></p>
				</div>
			</div>
			<div class='control-group' style="display: inline-block; _display: inline;"<c:if test='${not ks.kingaku.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.kingaku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kingaku.name)}</label>
					<div class='controls'>
						<input type='text' name='kingaku' class='input-medium autoNumeric<c:if test="${enableInput}">WithCalcBox</c:if>' value='${su:htmlEscape(kingaku)}'>円
					</div>
			</div>
			<div class='control-group invoiceOnly' style="display: inline-block; _display: inline;"<c:if test='${not ks.shouhizeigaku.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='label'<c:if test='${invoiceDenpyou eq "1"}'>style='display:none;'</c:if>>うち消費税額10%</label>
					<input type='text' name='shouhizeigaku' class='input-medium autoNumeric<c:if test="${enableInput}">WithCalcBox</c:if>' value='${su:htmlEscape(shouhizeigaku)}'>円
					<button type='button' class="btn btn-small" id='zeiShuseiBtn' name='zeiShuseiBtn'>修正</button>
			</div>
			<div class='control-group invoiceOnly'  style="text-align:right">
				<input type='hidden' name='zeinukiKingaku'<c:if test="${enableInput}">WithCalcBox</c:if> value='${su:htmlEscape(zeinukiKingaku)}'>
				<button type='button' id='zeigakuShousaiButton' class='btn btn-small' style='text-align:right'>消費税額詳細</button>
			</div>
			<!-- 支払日 --><%-- 申請後表示、経理承認時のみ変更可能 --%>
			<div class='control-group' <c:if test="${shiharaiBiMode == 0}">style='display:none;'</c:if>>
				<label class='control-label'><span class='required'>*</span>支払日</label>
				<div class='controls'>
					<input type='text' name='shiharaiBi' class='input-small datepicker' value='${su:htmlEscape(shiharaiBi)}' <c:if test="${shiharaiBiMode != 1}">disabled</c:if>>
				</div>
			</div>
		</div>
	</section>

<c:if test="${enableInput && ekispertEnable}">

	<button type='button' class='btn btn-small' name = 'norikaeannaiButton' onclick="norikaeannaiKensakuShow()" >乗換案内検索</button>

</c:if>
</div><!-- kaikeiContent -->

<!-- スクリプト -->
<script style='text/javascript'>

/**
 * 使用終了日計算Function
 */
function shiyouShuuryouHenkou() {
	var kaishi = $("input[name=shiyouKaishiBi]").val();

	if(kaishi != "") {
		var kaishiBi = kaishi.split("/");
		var year = Number(kaishiBi[0]);
		var month = Number(kaishiBi[1]);
		var day = Number(kaishiBi[2]);
		if(checkDate(year, month, day)) {
			var addMonths = Number($("input[name=shiyouKikanKbn]:checked").val());
			month += addMonths;
			// 月末日を求める
			var endDay = calcMonthEndDay(year, month);
			// 入力された日が月末日より大きかった場合、月末日を設定
			if(day > endDay) {day = endDay;}
			else {day = day -1;}
			var dt = new Date(year, month - 1, day);

			var getYear = String(dt.getFullYear());
			// 1～9月をMM形式にする
			if(dt.getMonth() + 1 <= 9) {
				getMonth = "0" + String(dt.getMonth() + 1);
			}
			else {
				getMonth = String(dt.getMonth() + 1);
			}
			// 1～9日をdd形式にする
			if(dt.getDate() <= 9) {
				getDate = "0" + String(dt.getDate());
			}
			else {
				getDate = String(dt.getDate());
			}

			$("input[name=shiyouShuuryouBi]").val(getYear  + "/" + getMonth + "/" + getDate );
		}
	}
}

$(document).ready(function(){
	
	zeiShuseiBtnDisplay();
	bunriShiireSeigyo();
	displayZeiritsu();

<c:if test='${enableInput}'>
	//取引選択ボタン押下時Function
	$("#torihikiSentakuButton").click(function(e){
		torihikiSentaku();
	});

	//仕訳枝番号変更時Function
	$("input[name=shiwakeEdaNo]").change(function(){
		shiwakeEdaNoLostFocus();
	});

	//勘定科目枝番選択ボタン押下時Function
	$("#kamokuEdabanSentakuButton").click(function(e){
		dialogRetKamokuEdabanCd				= $("input[name=kamokuEdabanCd]");
		dialogRetKamokuEdabanName			= $("input[name=kamokuEdabanName]");
		dialogRetKazeiKbn 					= $("select[name=kazeiKbn]");
		dialogRetBunriKbn 					= $("select[name=bunriKbn]");
		dialogCallbackKanjyouKamokuEdabanSentaku = function(){
			displayZeiritsu();
			zeiShuseiBtnDisplay();
			bunriShiireSeigyo();
			// 金額再計算
			calcKingaku();
			$("input[name=futanBumonCd]").blur();
		};
		commonKamokuEdabanSentaku($("[name=kamokuCd]").val(), $("input[name=denpyouKbn]").val(), $("input[name=shiwakeEdaNo]").val());
	});

	//勘定科目枝番コードロストフォーカス時Function
	$("input[name=kamokuEdabanCd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var kamokuCd = 	$("input[name=kamokuCd]").val();
		dialogRetKamokuEdabanCd				= $("input[name=kamokuEdabanCd]");
		dialogRetKamokuEdabanName			= $("input[name=kamokuEdabanName]");
		dialogRetKazeiKbn 					= $("select[name=kazeiKbn]");
		dialogRetBunriKbn 					= $("select[name=bunriKbn]");
		dialogCallbackKanjyouKamokuEdabanSentaku = function(){
			displayZeiritsu();
			zeiShuseiBtnDisplay();
			bunriShiireSeigyo();
			// 金額再計算
			calcKingaku();
			$("input[name=futanBumonCd]").blur();
		};
		commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamokuCd, title,dialogRetKazeiKbn,dialogRetBunriKbn, $("input[name=denpyouKbn]").val(), $("input[name=shiwakeEdaNo]").val());
	});

	//負担部門選択ボタン押下時Function
	$("#futanBumonSentakuButton").click(function(e){
		dialogRetFutanBumonCd				= $("input[name=futanBumonCd]");
		dialogRetFutanBumonName				= $("input[name=futanBumonName]");
		dialogRetKariShiireKbn 				= $("select[name=kariShiireKbn]");
		commonFutanBumonSentaku("1",$("input[name=kamokuCd]").val(),$("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val(), $("input[name=denpyouKbn]").val(), $("input[name=shiwakeEdaNo]").val());
	});

	//負担部門コードロストフォーカス時Function
	$("input[name=futanBumonCd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetFutanBumonCd				= $("input[name=futanBumonCd]");
		dialogRetFutanBumonName				= $("input[name=futanBumonName]");
		dialogRetKariShiireKbn 				= $("select[name=kariShiireKbn]");
		commonFutanBumonCdLostFocus("1",dialogRetFutanBumonCd, dialogRetFutanBumonName, title,$("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val(), $("[name=kamokuCd]").val(), dialogRetKariShiireKbn, $("input[name=denpyouKbn]").val(), $("input[name=shiwakeEdaNo]").val());
	});

	//取引先選択ボタン押下時Function
	$("#torihikisakiSentakuButton").click(function(e){
		dialogRetTorihikisakiCd				= $("input[name=torihikisakiCd]");
		dialogRetTorihikisakiName			= $("input[name=torihikisakiName]");
		dialogRetFurikomisaki 				= $("input[name=furikomisakiJouhou]");
		commonTorihikisakiSentaku();
		dialogCallbackTorihikisakiSentaku = function(){
			jigyoushaKbnSeigyo();
		};
	});

	//取引先クリアボタン押下時Function
	$("#torihikisakiClearButton").click(function(e){
		$("input[name=torihikisakiCd]").val("");
		$("input[name=torihikisakiName]").val("");
		$("input[name=furikomisakiJouhou]").val("");
	});

	//取引先コードロストフォーカス時Function
	$("input[name=torihikisakiCd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetTorihikisakiCd				= $("input[name=torihikisakiCd]");
		dialogRetTorihikisakiName			= $("input[name=torihikisakiName]");
		dialogRetFurikomisaki 				= $("input[name=furikomisakiJouhou]");
		dialogRetJigyoushaKbn 				= $("[name=jigyoushaKbn]");
		commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd, dialogRetTorihikisakiName, title, dialogRetFurikomisaki);
	});

	//プロジェクト選択ボタン押下時Function
	$("#projectSentakuButton").click(function(e){
		dialogRetProjectCd				= $("input[name=projectCd]");
		dialogRetProjectName			= $("input[name=projectName]");
		commonProjectSentaku();
	});

	//プロジェクトコードロストフォーカス時Function
	$("input[name=projectCd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetProjectCd				= $("input[name=projectCd]");
		dialogRetProjectName			= $("input[name=projectName]");
		commonProjectCdLostFocus(dialogRetProjectCd, dialogRetProjectName, title);
	});

	//セグメント選択ボタン押下時Function
	$("#segmentSentakuButton").click(function(e){
		dialogRetSegmentCd				= $("input[name=segmentCd]");
		dialogRetSegmentName			= $("input[name=segmentName]");
		commonSegmentSentaku();
	});

	//セグメントコードロストフォーカス時Function
	$("input[name=segmentCd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetSegmentCd				= $("input[name=segmentCd]");
		dialogRetSegmentName			= $("input[name=segmentName]");
		commonSegmentCdLostFocus(dialogRetSegmentCd, dialogRetSegmentName, title);
	});
	
	//消費税額ロストフォーカス時Function
	$("input[name=shouhizeigaku]").blur(function(){
		var target = $("#kaikeiContent");
		var shouhizeigaku		= target.find("input[name=shouhizeigaku]").val().replaceAll(",", "");
		var kingaku				= target.find("input[name=kingaku]").val().replaceAll(",", "");

		$("input[name=zeinukiKingaku]").val((kingaku - shouhizeigaku).toString().formatMoney());
	});
	
	 //消費税額修正ボタン押下時Function
	$("#zeiShuseiBtn").on('click', function() {
		var meisai = $("#kaikeiContent");
		var flg = (meisai.find("input[name=shouhizeigaku]").prop('disabled'));
		meisai.find("input[name=shouhizeigaku]").prop("disabled", !flg);
		delCalculator($("input[name=shouhizeigaku]"));
		if(!meisai.find("input[name=shouhizeigaku]").prop("disabled")){
			addCalculator($("input[name=shouhizeigaku]"));			
		}
	});

	//使用区分ラジオまたは使用開始日変更時、期間終了日は自動で入る
	$("input[name=shiyouKikanKbn]").change(function(){
		shiyouShuuryouHenkou();
		$("input[name=kingaku]").val("");
		//金額リセットされるため手入力扱いに変更
		$("input[name=tenyuuryokuFlg]").val("1");
		showTenyuuryokuFlg();
		changeEnableKukanKingaku();
	});
	$("input[name=shiyouKaishiBi]").change(shiyouShuuryouHenkou);

	//使用区間手変更時
	$("textarea[name=jyoushaKukan],input[name=kingaku]").change(function(){
		$("input[name=tenyuuryokuFlg]").val("1");
		showTenyuuryokuFlg();
		calcKingaku();
	});
	//金額自動再計算
	$("input[name=jigyoushaKbn]").change(calcKingaku);
	$("select[name=zeiritsu]").change(calcKingaku);

	routeSentakuCallback = function() {
		changeEnableKukanKingaku();
	};
	//changeEnableKukanKingaku();
	if($("input[name=tenyuuryokuFlg]").val() == "0"){
		$("textarea[name=jyoushaKukan],input[name=kingaku]").prop("disabled", true);
	}else{
		$("textarea[name=jyoushaKukan],input[name=kingaku]").prop("disabled", false);
	}

</c:if>

<c:if test='${not enableInput}'>
	//起票モードの場合のみ表示可能に
	$("#kaikeiContent").find("button").css("display", "none");
	$("#kaikeiContent").find("input,textarea,select").prop("disabled", true);
</c:if>

//消費税額詳細ボタンクリック
$('#zeigakuShousaiButton').on('click', function() {
    commonShouhizeiShousai($("input[name=denpyouKbn]").val());
});

	//自動入力・手入力の文言出す
	showTenyuuryokuFlg();
});

/**
 * イベントボタン押下時のアクションの切り替え
 */
function eventBtn(eventName) {
	var formObject = document.getElementById("workflowForm");

	// 支払日チェック
	var checkShiharaiBi = ($("input[name=shiharaiBi]").prop("disabled") == false);

	//支払日更新チェック
	if (checkShiharaiBi) {
		var shiharaiBi = $("input[name=shiharaiBi]").val();
		if ("" == shiharaiBi) {
			//20220517
			if(!alert("支払日が未入力です。\n支払日を入力して、更新してください。")){
				$(formObject).find('button[type=button]').not('#chiharaibiCheckNotPass').removeAttr("disabled");
			}
			//alert("支払日が未入力です。\n支払日を入力して、更新してください。");
			return;
		}
	}

	switch (eventName) {
	case 'entry':
		formObject.action = 'tsuukin_teiki_shinsei_touroku';
		break;
	case 'update':
		formObject.action = 'tsuukin_teiki_shinsei_koushin';
		break;
	case 'shinsei':
		formObject.action = 'tsuukin_teiki_shinsei_shinsei';
		break;
	case 'shounin':
		formObject.action = 'tsuukin_teiki_shinsei_shounin';
		break;
	case 'shouninkaijo':
		formObject.action = 'tsuukin_teiki_shinsei_shouninkaijo';
		break;
	}

	formObject.method = 'post';
	formObject.target = '_self';
	$(formObject).submit();
}

/**
 * 取引選択
 *【WARNING】こちら変更した場合は必ずshiwakeEdaNoLostFocus()もメンテすること【WARNING】
 */
function torihikiSentaku(){
	setTorihikiDialogRet();
	dialogCallbackTorihikiSentaku = function() { shiwakeEdaNoLostFocus(); };
	commonTorihikiSentaku("A006", $("input[name=kihyouBumonCd]").val(), $("input[name=denpyouId]").val(), $("input[name=daihyouFutanBumonCd]").val(),"");
};

/**
 * 仕訳枝番号変更時Function
 *【WARNING】こちら変更した場合は必ずtorihikiSentaku()もメンテすること【WARNING】
 */
function shiwakeEdaNoLostFocus(){
	setTorihikiDialogRet();
	var title = $("input[name=shiwakeEdaNo]").parent().parent().find(".control-label").text();
 	commonShiwakeEdaNoLostFocus(dialogRetShiwakeEdaNo, dialogRetTorihikiName, title, "A006", $("input[name=kihyouBumonCd]").val(), $("input[name=denpyouId]").val(), $("input[name=daihyouFutanBumonCd]").val(), "", $("#denpyouJouhou").find("select[name=invoiceDenpyou] option:selected").val());
};

/**
 * 取引ダイアログ表示後の設定項目Function
 */
function setTorihikiDialogRet() {
	dialogRetTorihikiName				= $("input[name=torihikiName]");
	dialogRetShiwakeEdaNo				= $("input[name=shiwakeEdaNo]");
	dialogRetKamokuCd					= $("input[name=kamokuCd]");
	dialogRetKamokuName					= $("input[name=kamokuName]");
	dialogRetKamokuEdabanCd				= $("input[name=kamokuEdabanCd]");
	dialogRetKamokuEdabanName			= $("input[name=kamokuEdabanName]");
	dialogRetKamokuEdabanSentakuButton	= $("#kamokuEdabanSentakuButton");
	dialogRetFutanBumonCd				= $("input[name=futanBumonCd]");
	dialogRetFutanBumonName				= $("input[name=futanBumonName]");
	dialogRetFutanBumonSentakuButton	= $("#futanBumonSentakuButton");
	dialogRetTorihikisakiCd				= $("input[name=torihikisakiCd]");
	dialogRetTorihikisakiName			= $("input[name=torihikisakiName]");
	dialogRetFurikomisaki 				= $("input[name=furikomisakiJouhou]");
	dialogRetTorihikisakiSentakuButton	= $("#torihikisakiSentakuButton");
	dialogRetTorihikisakiClearButton	= $("#torihikisakiClearButton");
	dialogRetProjectCd					= $("input[name=projectCd]");
	dialogRetProjectName				= $("input[name=projectName]");
	dialogRetProjectSentakuButton		= $("#projectSentakuButton");
	dialogRetSegmentCd					= $("input[name=segmentCd]");
	dialogRetSegmentName				= $("input[name=segmentName]");
	dialogRetSegmentSentakuButton		= $("#segmentSentakuButton");
	dialogRetTekiyou					= $("input[name=tekiyou]");
	dialogRetShiharaisakiName			= $("input[name=shiharaisakiName]");
	dialogRetKazeiKbn 					= $("select[name=kazeiKbn]");
	dialogRetZeiritsu					= $("select[name=zeiritsu]");
	dialogRetJigyoushaKbn				= $("input[name=jigyoushaKbn]");
	dialogRetBunriKbn 					= $("select[name=bunriKbn]");
	dialogRetKariShiireKbn 				= $("select[name=kariShiireKbn]");
	dialogRetShoriGroup					= $("input[name=shoriGroup]");
<c:choose>
	<c:when test="${'UF1' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf1Cd]");
		dialogRetSahinName	= $("input[name=uf1Name]");
	</c:when>
	<c:when test="${'UF2' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf2Cd]");
		dialogRetSahinName	= $("input[name=uf2Name]");
	</c:when>
	<c:when test="${'UF3' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf3Cd]");
		dialogRetSahinName	= $("input[name=uf3Name]");
	</c:when>
	<c:when test="${'UF4' == shainCdRenkeiArea}">
	dialogRetShainCd	= $("input[name=uf4Cd]");
	dialogRetSahinName	= $("input[name=uf4Name]");
	</c:when>
	<c:when test="${'UF5' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf5Cd]");
		dialogRetSahinName	= $("input[name=uf5Name]");
	</c:when>
	<c:when test="${'UF6' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf6Cd]");
		dialogRetSahinName	= $("input[name=uf6Name]");
	</c:when>
	<c:when test="${'UF7' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf7Cd]");
		dialogRetSahinName	= $("input[name=uf7Name]");
	</c:when>
	<c:when test="${'UF8' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf8Cd]");
		dialogRetSahinName	= $("input[name=uf8Name]");
	</c:when>
	<c:when test="${'UF9' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf9Cd]");
		dialogRetSahinName	= $("input[name=uf9Name]");
	</c:when>
	<c:when test="${'UF10' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf10Cd]");
		dialogRetSahinName	= $("input[name=uf10Name]");
	</c:when>
</c:choose>
	<c:if test="${'UF1' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf1ShiyouFlg}">
		dialogRetUf1Cd				= $("input[name=uf1Cd]");
		dialogRetUf1Name			= $("input[name=uf1Name]");
		dialogRetUf1SentakuButton	= $("#uf1SentakuButton");
	</c:if>
	<c:if test="${'UF2' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf2ShiyouFlg}">
		dialogRetUf2Cd				= $("input[name=uf2Cd]");
		dialogRetUf2Name			= $("input[name=uf2Name]");
		dialogRetUf2SentakuButton	= $("#uf2SentakuButton");
	</c:if>
	<c:if test="${'UF3' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf3ShiyouFlg}">
		dialogRetUf3Cd				= $("input[name=uf3Cd]");
		dialogRetUf3Name			= $("input[name=uf3Name]");
		dialogRetUf3SentakuButton	= $("#uf3SentakuButton");
	</c:if>
		<c:if test="${'UF4' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf4ShiyouFlg}">
		dialogRetUf4Cd				= $("input[name=uf4Cd]");
		dialogRetUf4Name			= $("input[name=uf4Name]");
		dialogRetUf4SentakuButton	= $("#uf4SentakuButton");
	</c:if>
	<c:if test="${'UF5' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf5ShiyouFlg}">
		dialogRetUf5Cd				= $("input[name=uf5Cd]");
		dialogRetUf5Name			= $("input[name=uf5Name]");
		dialogRetUf5SentakuButton	= $("#uf5SentakuButton");
	</c:if>
	<c:if test="${'UF6' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf6ShiyouFlg}">
		dialogRetUf6Cd				= $("input[name=uf6Cd]");
		dialogRetUf6Name			= $("input[name=uf6Name]");
		dialogRetUf6SentakuButton	= $("#uf6SentakuButton");
	</c:if>
	<c:if test="${'UF7' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf7ShiyouFlg}">
		dialogRetUf7Cd				= $("input[name=uf7Cd]");
		dialogRetUf7Name			= $("input[name=uf7Name]");
		dialogRetUf7SentakuButton	= $("#uf7SentakuButton");
	</c:if>
	<c:if test="${'UF8' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf8ShiyouFlg}">
		dialogRetUf8Cd				= $("input[name=uf8Cd]");
		dialogRetUf8Name			= $("input[name=uf8Name]");
		dialogRetUf8SentakuButton	= $("#uf8SentakuButton");
	</c:if>
	<c:if test="${'UF9' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf9ShiyouFlg}">
		dialogRetUf9Cd				= $("input[name=uf9Cd]");
		dialogRetUf9Name			= $("input[name=uf9Name]");
		dialogRetUf9SentakuButton	= $("#uf9SentakuButton");
	</c:if>
	<c:if test="${'UF10' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf10ShiyouFlg}">
		dialogRetUf10Cd				= $("input[name=uf10Cd]");
		dialogRetUf10Name			= $("input[name=uf10Name]");
		dialogRetUf10SentakuButton	= $("#uf10SentakuButton");
	</c:if>
	dialogCallbackCommonSetTorihiki = function() {
		displayZeiritsu();
		zeiShuseiBtnDisplay();
		bunriShiireSeigyo();
	}
}

/**
 * 定期区間自動入力時等の乗車区間・金額制御Function
 */
function changeEnableKukanKingaku() {
	calcKingaku();
	if($("input[name=tenyuuryokuFlg]").val() == "0"){
		$("textarea[name=jyoushaKukan],input[name=kingaku]").prop("disabled", true);
	}else{
		$("textarea[name=jyoushaKukan],input[name=kingaku]").prop("disabled", false);
	}
}

/**
 * 分離区分のdisable制御と仕入区分の非表示制御
 */
function bunriShiireSeigyo(){
	//新規入力状態のときは取引を選択するまで入力不可
	//取引コードを消してフォーカスアウトしたらリセット
	if($("input[name=shiwakeEdaNo]").val() == ''){
		$("select[name=bunriKbn]").val("9");
		$("select[name=bunriKbn]").prop("disabled", true);
		$("select[name=kariShiireKbn]").val("0");
		$("select[name=kariShiireKbn]").prop("disabled", true);
	}else{
		displayZeiritsu();
		//課税区分は常にdisabled
		$("select[name=kazeiKbn]").prop("disabled", true);
		//calcKingaku();
	}
	jigyoushaKbnSeigyo();
	zeiShuseiBtnDisplay();
}

/**
* 事業者区分の入力制御（入力可能な場合、必須となる）
*/
function jigyoushaKbnSeigyo(){
	var shoriGroup = $("input[name=shoriGroup]").val();
	var kazeiKbn = $("select[name=kazeiKbn] option:selected").val();
	//処理グループが21(仮払消費税)の場合、課税区分を考慮せず変更可能とする
	if($("input[name=shiwakeEdaNo]").val() == ''){
		//取引をクリアした場合
		$("input[name=jigyoushaKbn]").prop("disabled", true);
		$("input[name=jigyoushaKbn]:eq(0)").prop("checked", true);
	}else if((((shoriGroup == "2"|| shoriGroup == "5"|| shoriGroup == "6"|| shoriGroup == "7"|| shoriGroup == "8"|| shoriGroup == "10")
			&& (kazeiKbn == "001" || kazeiKbn == "002" || kazeiKbn == "011" || kazeiKbn == "013")) || shoriGroup == "21")
			&& $("#denpyouJouhou").find("select[name=invoiceDenpyou] option:selected").val() == "0"){ 
		$("input[name=jigyoushaKbn]").prop("disabled", false);
		var notCheck = $('[name="jigyoushaKbn"]:checked').length;
		if(notCheck < 1){
			//変更可能時、取引の取引先が任意だったなどの理由で、事業者区分ラジオボタンがどちらもチェックされていなかった場合
			$("input[name=jigyoushaKbn]:eq(0)").prop("checked", true);
		}
	}else{
		$("input[name=jigyoushaKbn]").prop("disabled", true); //選択を「0:通常課税」にして、グレーアウトにする
		$("input[name=jigyoushaKbn]:eq(0)").prop("checked",true);
	}
}

//消費税額修正ボタンの表示制御
function zeiShuseiBtnDisplay(){
	var zeigakuShuseiFlg = $("#workflowForm").find("input[name=zeigakuShuseiFlg]").val();
	var kazeiKbn = $("select[name=kazeiKbn] option:selected").val();
	if(kazeiKbn == "002" || kazeiKbn == "013" || kazeiKbn == "014"){
		if(zeigakuShuseiFlg == "1"){
			//課税区分が税抜系且つ会社設定(zeigakuShuseiFlg)が「1」→税修正ボタン表示
			$("button[name=zeiShuseiBtn]").show();
			$("input[name=shouhizeigaku]").prop("disabled", true);
			addCalculator($("#shouhizeigaku"));
		}else{
			//それ以外　→税修正ボタン常に非表示
			$("input[name=shouhizeigaku]").prop("disabled", zeigakuShuseiFlg != "2");
			$("button[name=zeiShuseiBtn]").hide();
			delCalculator($("input[name=shouhizeigaku]"));
		}
	}else{
		$("input[name=shouhizeigaku]").prop("disabled", true);
		$("button[name=zeiShuseiBtn]").hide();
		delCalculator($("input[name=shouhizeigaku]"));
	}
}

/**
 * 課税区分により税率の標示有無切替
 * 税込・税抜なら表示、それ以外なら非表示
 * 取引選択時に呼ばれる
 */
function displayZeiritsu() {
	var kazeiKbnGroup = $("select[name=kazeiKbn] option:selected").attr("data-kazeiKbnGroup");
	var zeiritsuArea = $("select[name=zeiritsu]").closest(".control-group");

	//インボイス対応前：課税区分が税込、税抜以外の場合は消費税入力欄を丸ごと消していた（zeiritsuArea.hide()）
	//20230329 ひとまずの対応として税込、税抜以外の場合は税率を非表示にする
	var hyoujiFlg = (kazeiKbnGroup == "1" || kazeiKbnGroup == "2");
	var displayStyle = ( false == hyoujiFlg ) ? 'hide' : 'show';
	$("label[for=zeiritsu]")[displayStyle]();
	$("select[name=zeiritsu]")[displayStyle]();
	if(!hyoujiFlg){
		$("select[name=zeiritsu]").val("");
	}
	//取引が未選択に戻された場合はdisabledで表示
	if($("input[name=shiwakeEdaNo]").val() == ''){
		$("label[for=zeiritsu]")[displayStyle]();
		$("select[name=zeiritsu]").val("");
	}
	displaySeigyo();
}

/**
 * 金額計算Function
 */
function calcKingaku() {
	var kingaku				= Number($("input[name=kingaku]").val().replaceAll(",", ""));
	var kazeiKbn			= $("select[name=kazeiKbn] :selected").val();
	var kazeiKbnGroup 		= $("select[name=kazeiKbn] option:selected").attr("data-kazeiKbnGroup");
	var zeiritsu 			= kazeiKbnGroup == "1" ? Number($("select[name=zeiritsu]").find("option:selected").val()) : "0";
	var hasuuKeisanKbn		= $("select[name=zeiritsu]").find("option:selected").attr("data-hasuuKeisanKbn");
	var jigyoushaFlg		= $("input[name=jigyoushaKbn]:checked").val() == "1" ? "1": "0";
	var hasuuShoriFlg		= $("#workflowForm").find("input[name=hasuuShoriFlg]").val();
	var shiireKeikaSothiFlg	= $("#workflowForm").find("input[name=shiirezeigakuKeikasothi]").val();
	
	let jigyoushaNum = (shiireKeikaSothiFlg == "1" || jigyoushaFlg == "0")
	? 1
	: "1" == jigyoushaFlg
		? 0.8
		: 0.5; // 掛け算なのでデフォは1
	
	//計算用
	var zeinukiKingaku	= null;
	var shouhizeigaku	= null;
	
	//decimalとして持つ
	let dZeiritsu = new Decimal(zeiritsu);
	let dKingaku = new Decimal(kingaku);
	let dJigyoushaNum = new Decimal(jigyoushaNum);
	let dBunbo = new Decimal(100).add(dZeiritsu);
	
	//明細の支払金額から本体金額、消費税額を計算
	shouhizeigaku = hasuuKeisanZaimuFromImporter(dKingaku.mul(dZeiritsu).div(dBunbo).mul(dJigyoushaNum), hasuuShoriFlg, false);
	zeinukiKingaku = dKingaku.sub(shouhizeigaku);
		
	//計算した本体金額、消費税額の値をセット or クリア
	$("input[name=shouhizeigaku]").val(shouhizeigaku.toString().formatMoney());
	$("input[name=zeinukiKingaku]").val(dKingaku.sub(shouhizeigaku).toString().formatMoney());
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
		close:function(e){$(".expGuiConditionPc").remove();},
		buttons: {閉じる: function() {$(this).dialog("close");}}
	});
	$("#dialog").load("norikae_annai_kensaku?teikiMode=true");
	return false;

}

/**
 * tenyuuryokuFlgから文言表示
 */
function showTenyuuryokuFlg(){
	$("#tenyuuryokuView").text($("[name=tenyuuryokuFlg]").val() == '1' ? "手入力" : "自動入力");
}

//課税区分・分離区分・仕入区分は取引仕訳登録で入力した値しか認めないため、常にグレーアウト
function displaySeigyo(){
	setShouhizeiControls($("input[name=denpyouId]").val(), $("input[name=shoriGroup]").val(), $("select[name=kazeiKbn]"), $("select[name=zeiritsu]"), $("select[name=bunriKbn]"), $("select[name=kariShiireKbn]"));
	$("select[name=kazeiKbn]").prop("disabled",true);
	$("select[name=bunriKbn]").prop("disabled",true);
	$("select[name=kariShiireKbn]").prop("disabled",true);
}
</script>
