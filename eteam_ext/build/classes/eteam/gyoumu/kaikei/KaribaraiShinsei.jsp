<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- 中身 -->
<div id='kaikeiContent' class='form-horizontal'>

	<!-- 入力フィールド -->
	<section class='print-unit'>
		<h2>申請内容</h2>
		<div>
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
			<div class='control-group' id='shiharai'>
				<label class='control-label'>支払</label>
					<div class='controls'>
						<!-- 支払希望日 -->
						<label class='label <c:if test='${not ks.shiharaiKiboubi.hyoujiFlg}'>never_show' style='display:none;</c:if>'><c:if test='${ks.shiharaiKiboubi.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiKiboubi.name)}</label>
						<input type='text' name='shiharaiKiboubi' class='input-small datepicker' value='${su:htmlEscape(shiharaiKiboubi)}' <c:if test='${not ks.shiharaiKiboubi.hyoujiFlg}'>style='display:none;'</c:if>>
						<!-- 支払方法 -->
						<label class='label <c:if test='${not ks.shiharaiHouhou.hyoujiFlg}'>never_show' style='display:none;</c:if>'><c:if test='${ks.shiharaiHouhou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiHouhou.name)}</label>
						<select id='shiharaihouhou' name='shiharaihouhou' class='input-small' <c:if test="${disableShiharaiHouhou}">disabled</c:if> <c:if test='${not ks.shiharaiHouhou.hyoujiFlg}'>style='display:none;'</c:if>>
<c:forEach var='shiharaihouhouList' items='${shiharaihouhouList}'>
							<option value='${su:htmlEscape(shiharaihouhouList.naibu_cd)}' <c:if test='${shiharaihouhouList.naibu_cd eq shiharaihouhou}'>selected</c:if>>${su:htmlEscape(shiharaihouhouList.name)}</option>
</c:forEach>
						</select>
						<!-- 支払日 --><%-- 申請後表示、経理承認時のみ変更可能 --%>
						<span id='shiharaibi'<c:if test="${shiharaiBiMode == 0}">class='never_show' </c:if><c:if test="${shiharaiBiMode == 0 || (shiharaiBiMode == 3 && karibaraiOn eq '0')}">style='display:none;'</c:if>>
							<label class='label hissu'><span class='required'>*</span>支払日</label>
							<input type='text' name='shiharaiBi' class='input-small datepicker' value='${su:htmlEscape(shiharaiBi)}' <c:if test="${shiharaiBiMode % 2 != 1}">disabled</c:if>><!-- 初期値disableだとdatepickerが動かない -->
						</span>
					</div>
			</div>
		</div>
		<span id="headerField">
		<%@ include file="./kogamen/HeaderField.jsp" %>
		</span>
		<div id = 'karibaraiAriArea'>
		<!-- 取引 -->
		<div class='control-group <c:if test='${not ks.torihiki.hyoujiFlg}'>never_show' style='display:none;</c:if>'>
			<label class='control-label'><c:if test='${ks.torihiki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.torihiki.name)}</label>
			<div class='controls'>
				<input type="text" name='shiwakeEdaNo'  value='${su:htmlEscape(shiwakeEdaNo)}' class='input-small pc_only'>
				<input type='text' name='torihikiName' class='input-xlarge' disabled value='${su:htmlEscape(torihikiName)}'>
				<button type='button' id='torihikiSentakuButton' class='btn btn-small'>選択</button>
			</div>
		</div>
		<!-- 勘定科目 -->
		<div class='control-group <c:if test='${not ks.kamoku.hyoujiFlg}'>never_show' style='display:none;</c:if>'>
			<label class='control-label'><c:if test='${ks.kamoku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kamoku.name)}</label>
			<div class='controls'>
				<input type='text' name='kamokuCd' class='input-small pc_only' disabled value='${su:htmlEscape(kamokuCd)}'>
				<input type='text' name='kamokuName' class='input-xlarge' disabled value='${su:htmlEscape(kamokuName)}'>
			</div>
		</div>
		<!-- 勘定科目枝番 -->
		<div class='control-group <c:if test='${not ks.kamokuEdaban.hyoujiFlg}'>never_show' style='display:none;</c:if>'>
			<label class='control-label'><c:if test='${ks.kamokuEdaban.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kamokuEdaban.name)}</label>
			<div class='controls'>
				<input type='text' name="kamokuEdabanCd" class='input-medium pc_only' <c:if test='${not kamokuEdabanEnable}'>disabled</c:if> value='${su:htmlEscape(kamokuEdabanCd)}'>
				<input type='text' name="kamokuEdabanName" class='input-xlarge' disabled value='${su:htmlEscape(kamokuEdabanName)}'>
				<button type='button' id='kamokuEdabanSentakuButton' class='btn btn-small' <c:if test='${not kamokuEdabanEnable}'>disabled</c:if>>選択</button>
			</div>
		</div>
		<!-- 負担部門 -->
		<div class='control-group <c:if test='${not ks.futanBumon.hyoujiFlg}'>never_show' style='display:none;</c:if>'>
			<label class='control-label'><c:if test='${ks.futanBumon.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.futanBumon.name)}</label>
			<div class='controls'>
				<input type='text' name='futanBumonCd' class='input-small pc_only' <c:if test='${not futanBumonEnable}'>disabled</c:if>  value='${su:htmlEscape(futanBumonCd)}'>
				<input type='text' name='futanBumonName' class='input-xlarge' disabled value='${su:htmlEscape(futanBumonName)}'>
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

		<!-- ユニバーサルフィールド -->
		<%@ include file="./kogamen/UniversalField.jsp" %>
		</div>
		<!-- 精算予定日 -->
		<div class='control-group <c:if test='${not ks.seisanYoteibi.hyoujiFlg}'>never_show' style='display:none;</c:if>'>
			<label class='control-label'><c:if test='${ks.seisanYoteibi.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.seisanYoteibi.name)}</label>
			<div class='controls'>
				<input type='text' name='seisanYoteiBi' class='input-small datepicker' value='${su:htmlEscape(seisanYoteiBi)}'>
				<span style="color:#ff0000;">${su:htmlEscape(shinseiChuuki)}</span>
			</div>
		</div>
		<!-- 申請金額／仮払金額 -->
		<div class='control-group'>
			<label class='control-label'><span class='required'>*</span>${su:htmlEscape(ks.shinseiKingaku.name)}</label>
			<div class='controls'>
				<!-- 申請金額 -->
				<input type='text' name='kingaku' class='input-medium autoNumeric<c:if test="${enableInput}">WithCalcBox</c:if>' value ='${su:htmlEscape(shinseiKingaku)}'>円
				<input type='hidden' name='shinseiKingaku' value ='${su:htmlEscape(shinseiKingaku)}'>
				<!-- 仮払金額 -->
				<label class='label'><span class='required'>*</span>${su:htmlEscape(ks.kingaku.name)}</label>
				<input type='text' name='karibaraiKingaku' class='input-medium autoNumeric<c:if test="${enableInput}">WithCalcBox</c:if>' value ='${su:htmlEscape(karibaraiKingaku)}'>円
			</div>
		</div>
		<!-- 摘要 -->
		<div class='control-group <c:if test='${not ks.tekiyou.hyoujiFlg}'>never_show' style='display:none;</c:if>' id='tekiyou' >
			<label class='control-label'><c:if test='${ks.tekiyou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.tekiyou.name)}</label>
			<div class='controls'>
				<input type='text' name='tekiyou' class='input-block-level' value='${su:htmlEscape(tekiyou)}'>
				<span style="line-height:25px;"><br><span style="color:#ff0000;">${su:htmlEscape(chuuki2)}</span></span>
			</div>
		</div>
	</section>
	<!-- 使用目的 -->
	<section class='print-unit' <c:if test='${not ks.hosoku.hyoujiFlg}'>style='display:none;'</c:if>>
		<h2><c:if test='${ks.hosoku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.hosoku.name)}</h2>
		<div>
			<div class='control-group'>
				<textarea name='shiyouMokuteki' maxlength="240" class='input-block-level'>${su:htmlEscape(shiyouMokuteki)}</textarea>
			</div>
		</div>
	</section>
</div><!-- content -->

<!-- スクリプト -->
<script style='text/javascript'>

$(document).ready(function(){

	var num = $("[name='karibaraiOn']:checked").val();
	<c:choose>
	<c:when test="${karibaraiSentakuFlg eq 0}">
		//仮払なしのみ
		$("[name='karibaraiOn']").val(["0"]);
		$("[name='karibaraiOn']").attr("disabled", "disabled");
		$("[name='kingaku']").removeAttr("disabled");
		addCalculator($("[name='kingaku']"));
		$("[name='karibaraiKingaku']").attr("disabled", "disabled");
		delCalculator($("[name='karibaraiKingaku']"));
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
		delCalculator($("[name='kingaku']"));
		$("[name='kingaku']").attr("disabled", "disabled");
		$("[name='karibaraiKingaku']").removeAttr("disabled");
		addCalculator($("[name='karibaraiKingaku']"));
    $("#headerField").children("div:not('.never_show')").css("display","");
		$("#shiharai").css("display","");
		$("#karibaraiAriArea").children("div:not('.never_show')").css("display","");
		$("[name='kingaku']").val("");
	</c:when>
	<c:when test="${karibaraiSentakuFlg eq 2}">
		//仮払あり・なし
		//仮払ありのとき仮払金額を変更可
		$("[name='karibaraiOn']").removeAttr("disabled");
		if( num == 1 ){
			$("[name='kingaku']").attr("disabled", "disabled");
			delCalculator($("[name='kingaku']"));
			$("[name='karibaraiKingaku']").removeAttr("disabled");
			addCalculator($("[name='karibaraiKingaku']"));
			$("#headerField").children("div:not('.never_show')").css("display","");
			$("#shiharai").css("display","");
			$("#karibaraiAriArea").children("div:not('.never_show')").css("display","");
		}else {
			$("[name='kingaku']").removeAttr("disabled");
			addCalculator($("[name='kingaku']"));
			$("[name='karibaraiKingaku']").attr("disabled", "disabled");
			delCalculator($("[name='karibaraiKingaku']"));
			<c:if test='${not yosanShikkouTaishouFlg}'>
				$("#headerField").children("div:not('.never_show')").css("display","none");
				$("#shiharai").css("display","none");
				$("#karibaraiAriArea").children("div:not('.never_show')").css("display","none");
				//入力値初期化
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
		}
		//仮払ラジオボタン変更時Function
		$("[name='karibaraiOn']").click(function(){
			var num = $("[name='karibaraiOn']:checked").val();
			if( num == 1 ){
				$("[name='kingaku']").attr("disabled", "disabled");
				delCalculator($("[name='kingaku']"));
				$("[name='karibaraiKingaku']").removeAttr("disabled");
				addCalculator($("[name='karibaraiKingaku']"));
				$("#headerField").children("div:not('.never_show')").css("display","");
				$("#shiharai").css("display","");
				$("#karibaraiAriArea").children("div:not('.never_show')").css("display","");
				//申請金額→値クリア、仮払金額→申請金額の入力値をコピー
				$("[name='karibaraiKingaku']").val($("[name='kingaku']").val() == "" ? $("[name='karibaraiKingaku']").val() : $("[name='kingaku']").val());
				$("[name='shinseiKingaku']").val("");
				$("[name='kingaku']").val("");
				<c:if test="${shiharaiBiMode % 2 == 1}">
					$("#shiharaibi").css("display","");
					$("input[name=shiharaiBi]").removeAttr("disabled");
				</c:if>
			}else {
				$("[name='kingaku']").removeAttr("disabled");
				addCalculator($("[name='kingaku']"));
				$("[name='karibaraiKingaku']").attr("disabled", "disabled");
				delCalculator($("[name='karibaraiKingaku']"));
				<c:if test='${not yosanShikkouTaishouFlg}'>
					$("#headerField").children("div:not('.never_show')").css("display","none");
					$("#shiharai").css("display","none");
					$("input[name=shiharaiKiboubi]").val("");
					$("#karibaraiAriArea").children("div:not('.never_show')").css("display","none");
					//入力値初期化
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
					$("input[name=shiharaiBi]").val("");
					$("input[name=shiharaiBi]").attr("disabled", "disabled");
				</c:if>
				//申請金額→仮払金額の入力値をコピー、仮払金額→値をコピー
				$("[name='kingaku']").val($("[name='karibaraiKingaku']").val() == "" ? $("[name='kingaku']").val() : $("[name='karibaraiKingaku']").val());
				$("[name='shinseiKingaku']").val($("[name='kingaku']").val());
				$("[name='karibaraiKingaku']").val("");
			}

			//タイトルの変更
			changeTitle();
		});
	</c:when>
	</c:choose>

<c:if test='${enableInput}'>;
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
		commonKamokuEdabanSentaku($("[name=kamokuCd]").val());
	});

	//勘定科目枝番コードロストフォーカス時Function
	$("input[name=kamokuEdabanCd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var kamokuCd = 	$("input[name=kamokuCd]").val();
		dialogRetKamokuEdabanCd				= $("input[name=kamokuEdabanCd]");
		dialogRetKamokuEdabanName			= $("input[name=kamokuEdabanName]");
		commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamokuCd, title);
	});

	//負担部門選択ボタン押下時Function
	$("#futanBumonSentakuButton").click(function(e){
		dialogRetFutanBumonCd				= $("input[name=futanBumonCd]");
		dialogRetFutanBumonName				= $("input[name=futanBumonName]");
		commonFutanBumonSentaku("1",$("input[name=kamokuCd]").val(),$("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val());
	});

	//負担部門コードロストフォーカス時Function
	$("input[name=futanBumonCd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetFutanBumonCd				= $("input[name=futanBumonCd]");
		dialogRetFutanBumonName				= $("input[name=futanBumonName]");
		commonFutanBumonCdLostFocus("1",dialogRetFutanBumonCd, dialogRetFutanBumonName, title,$("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val());
	});

	//取引先選択ボタン押下時Function
	$("#torihikisakiSentakuButton").click(function(e){
		dialogRetTorihikisakiCd				= $("input[name=torihikisakiCd]");
		dialogRetTorihikisakiName			= $("input[name=torihikisakiName]");
		dialogRetFurikomisaki 				= $("input[name=furikomisakiJouhou]");
		commonTorihikisakiSentaku();
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

	//申請金額コードロストフォーカス時Function
	$("input[name=kingaku]").blur(function(){
		//画面再表示用としてhidden項目に値をコピー
		$("input[name=shinseiKingaku]").val($("input[name=kingaku]").val());
	});
	
	//摘要文字数制限取得
	$("input[name=tekiyou]").attr('maxlength', $("#workflowForm").find("input[name=tekiyouMaxLength]").val());
	
</c:if>;

<c:if test='${not enableInput}'>;
	//起票モードの場合のみ入力可能なまま
	$("#kaikeiContent").find("button").css("display", "none");
	$("#kaikeiContent").find("input,textarea,select").prop("disabled", true);
</c:if>;

	//タイトルの変更
	changeTitle();

});

/**
 * イベントボタン押下時のアクションの切り替え
 */
function eventBtn(eventName) {
	var formObject = document.getElementById("workflowForm");

	// 支払日チェック
	var checkShiharaiBi = ($("input[name=shiharaiBi]").prop("disabled") == false && $("input[name=karibaraiOn]:checked").val() == '1');

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
		formObject.action = 'karibarai_shinsei_touroku';
		break;
	case 'update':
		formObject.action = 'karibarai_shinsei_koushin';
		break;
	case 'shinsei':
		formObject.action = 'karibarai_shinsei_shinsei';
		break;
	case 'shounin':
		formObject.action = 'karibarai_shinsei_shounin';
		break;
	case 'shouninkaijo':
		formObject.action = 'karibarai_shinsei_shouninkaijo';
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
	dialogCallbackTorihikiSentaku = function() { shiwakeEdaNoLostFocus() };
	commonTorihikiSentaku("A002", $("input[name=kihyouBumonCd]").val(), $("input[name=denpyouId]").val(), $("input[name=daihyouFutanBumonCd]").val(),"");
};

/**
 * 仕訳枝番号変更時Function
 *【WARNING】こちら変更した場合は必ずtorihikiSentaku()もメンテすること【WARNING】
 */
function shiwakeEdaNoLostFocus(){
	setTorihikiDialogRet();
	var title = $("input[name=shiwakeEdaNo]").parent().parent().find(".control-label").text();
	commonShiwakeEdaNoLostFocus(dialogRetShiwakeEdaNo, dialogRetTorihikiName, title, "A002", $("input[name=kihyouBumonCd]").val(), $("input[name=denpyouId]").val(), $("input[name=daihyouFutanBumonCd]").val());
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
		</script>
	</body>
</html>
