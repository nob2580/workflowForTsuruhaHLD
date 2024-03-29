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
			<div class='control-group' <c:if test='${not ks.mokuteki.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.mokuteki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.mokuteki.name)}</label>
				<div class='controls'>
					<input type='text' name='mokuteki' maxlength='30' class='input-block-level' value='${su:htmlEscape(mokuteki)}'>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ks.seisankikan.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.seisankikan.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.seisankikan.name)}</label>
				<div class='controls'>
					<input type='text' name='seisankikanFrom' class='input-small datepicker' <c:if test='${not ks.seisankikan.hyoujiFlg}'>style='display:none;'</c:if> value='${su:htmlEscape(seisankikanFrom)}'>
					<span <c:if test='${not ks.seisankikanJikoku.hyoujiFlg}'>style='display:none;'</c:if>>
					<c:if test='${ks.seisankikanJikoku.hissuFlg}'><span class='required'>*</span></c:if>
					<input type='text' name='seisankikanFromHour' class='input-small zeropadding' style='width:40px;' maxlength='2' value='${su:htmlEscape(seisankikanFromHour)}'>時
					<input type='text' name='seisankikanFromMin' class='input-small zeropadding' style='width:40px;' maxlength='2' value='${su:htmlEscape(seisankikanFromMin)}'>分
					</span>
					～
					<input type='text' name='seisankikanTo' class='input-small datepicker' <c:if test='${not ks.seisankikan.hyoujiFlg}'>style='display:none;'</c:if> value='${su:htmlEscape(seisankikanTo)}'>
					<span <c:if test='${not ks.seisankikanJikoku.hyoujiFlg}'>style='display:none;'</c:if>>
					<c:if test='${ks.seisankikanJikoku.hissuFlg}'><span class='required'>*</span></c:if>
					<input type='text' name='seisankikanToHour' class='input-small zeropadding' style='width:40px;' maxlength='2' value='${su:htmlEscape(seisankikanToHour)}'>時
					<input type='text' name='seisankikanToMin' class='input-small zeropadding' style='width:40px;' maxlength='2' value='${su:htmlEscape(seisankikanToMin)}'>分
					</span>
				</div>
			</div>
			<%@ include file="./kogamen/HeaderField.jsp" %>
			<div class='control-group'  <c:if test='${not ks.torihiki.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.torihiki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.torihiki.name)}</label>
				<div class='controls'>
					<input type="text" name='shiwakeEdaNoRyohi'  value='${su:htmlEscape(shiwakeEdaNoRyohi)}' class='input-small pc_only'>
					<input type='text' name='torihikiNameRyohi' class='input-xlarge' value='${su:htmlEscape(torihikiNameRyohi)}' disabled>
					<button type='button' id='torihikiSentakuButton' class='btn btn-small'>選択</button>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ks.kamoku.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.kamoku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kamoku.name)}</label>
				<div class='controls'>
					<input type='text' name='kamokuCdRyohi' class='input-small pc_only' value='${su:htmlEscape(kamokuCdRyohi)}' disabled>
					<input type='text' name='kamokuNameRyohi' class='input-xlarge' value='${su:htmlEscape(kamokuNameRyohi)}' disabled>
					<input type='hidden' name='shoriGroupRyohi' value='${su:htmlEscape(shoriGroupRyohi)}'>
				</div>
			</div>
			<!-- 課税区分 -->
			<div class='control-group' id='kazeiDiv'<c:if test='${not ks.kazeiKbn.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.kazeiKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kazeiKbn.name)}</label>
				<div class='controls'>
					<select name='kazeiKbnRyohi' class='input-medium' disabled>
						<c:forEach var="kazeiKbnRecord" items="${kazeiKbnList}">
							<option value='${kazeiKbnRecord.naibuCd}' data-kazeiKbnGroup='${kazeiKbnRecord.option1}' <c:if test='${kazeiKbnRecord.naibuCd eq kazeiKbnRyohi}'>selected</c:if>>${su:htmlEscape(kazeiKbnRecord.name)}</option>
						</c:forEach>
					</select>
				<!-- 税率 -->
				<span id='zeiritsuArea' <c:if test='${not ks.zeiritsu.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='label' for='zeiritsuRyohi'><c:if test='${ks.zeiritsu.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.zeiritsu.name)}</label>
					<select id="zeiritsuRyohi" name='zeiritsuRyohi' class='input-small'>
						<c:forEach var="tmpZeiritsu" items="${zeiritsuRyohiList}">
							<option value='${tmpZeiritsu.zeiritsu}' data-hasuuKeisanKbn='${tmpZeiritsu.hasuuKeisanKbn}' data-keigenZeiritsuKbn='${tmpZeiritsu.keigenZeiritsuKbn}' 
							<c:if test='${tmpZeiritsu.zeiritsu eq zeiritsuRyohi}'>selected</c:if>>
							<c:if test='${tmpZeiritsu.keigenZeiritsuKbn eq "1"}'>*</c:if>${tmpZeiritsu.zeiritsu}%</option>
						</c:forEach>
					</select>
					<input type='hidden' name='keigenZeiritsuKbnRyohi' value='${su:htmlEscape(keigenZeiritsuKbnRyohi)}'>
				</span>
				<!-- 分離区分 -->
				<span <c:if test='${not ks.bunriKbn.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='label' for='bunriKbn'><c:if test='${ks.bunriKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.bunriKbn.name)}</label>
					<select name='bunriKbn' id="bunriKbn" class='input-small' disabled>
						<c:forEach var="bunriKbnRecord" items="${bunriKbnList}">
						<c:if test='${bunriKbnRecord.naibuCd eq "9"}'>
							<option hidden value='${bunriKbnRecord.naibuCd}' <c:if test='${bunriKbnRecord.naibuCd eq bunriKbn}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
						</c:if>
						<c:if test='${bunriKbnRecord.naibuCd ne "9"}'>
							<option value='${bunriKbnRecord.naibuCd}'<c:if test='${bunriKbnRecord.naibuCd eq bunriKbn}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
						</c:if>
						</c:forEach>
					</select>
				</span>
				<!-- 仕入区分 -->
				<span <c:if test='${not ks.shiireKbn.hyoujiFlg or shiireZeiAnbun eq "0"}'>style='display:none;'</c:if>>
				<label class='label' for='kariShiireKbn'>${su:htmlEscape(ks.shiireKbn.name)}</label>
					<select name='kariShiireKbn' id="kariShiireKbn" class='input-small' disabled>
						<c:forEach var="shiireKbnRecord" items="${shiireKbnList}">
						<c:if test='${shiireKbnRecord.naibuCd eq "0"}'>
							<option hidden value='${shiireKbnRecord.naibuCd}' <c:if test='${shiireKbnRecord.naibuCd eq kariShiireKbn}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
						</c:if>
						<c:if test='${shiireKbnRecord.naibuCd ne "0"}'>
							<option value='${shiireKbnRecord.naibuCd}' <c:if test='${shiireKbnRecord.naibuCd eq kariShiireKbn}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
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
					<input type='text' name='kamokuEdabanCdRyohi' class='input-medium pc_only' value='${su:htmlEscape(kamokuEdabanCdRyohi)}' <c:if test='${not kamokuEdabanEnableRyohi}'>disabled</c:if>>
					<input type='text' name='kamokuEdabanNameRyohi' class='input-xlarge' value='${su:htmlEscape(kamokuEdabanNameRyohi)}' disabled>
					<button type='button' id='kamokuEdabanSentakuButton' class='btn btn-small' <c:if test='${not kamokuEdabanEnableRyohi}'>disabled</c:if>>選択</button>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ks.futanBumon.hyoujiFlg}'>style='display:none;'</c:if>>
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
				<div class='control-group' <c:if test='${not("0" ne pjShiyouFlg and ks.project.hyoujiFlg)}'> never_show' style='display:none;</c:if>>
					<label class='control-label'><c:if test='${ks.project.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.project.name)}</label>
					<div class='controls'>
						<input type='text' name="projectCdRyohi" class='pc_only' <c:if test='${not projectEnableRyohi}'>disabled</c:if> value='${su:htmlEscape(projectCdRyohi)}'>
						<input type='text' name="projectNameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(projectNameRyohi)}'>
						<button type='button' id='projectSentakuButton' class='btn btn-small' <c:if test='${not projectEnableRyohi}'>disabled</c:if>>選択</button>
					</div>
				</div>
				<!-- セグメント -->
				<div class='control-group <c:if test='${not("0" ne segmentShiyouFlg and ks.segment.hyoujiFlg)}'>never_show' style='display:none;</c:if>'>
					<label class='control-label'><c:if test='${ks.segment.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.segment.name)}</label>
					<div class='controls'>
						<input type='text' name="segmentCdRyohi" class='pc_only' <c:if test='${not segmentEnableRyohi}'>disabled</c:if> value='${su:htmlEscape(segmentCdRyohi)}'>
						<input type='text' name="segmentNameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(segmentNameRyohi)}'>
						<button type='button' id='segmentSentakuButton' class='btn btn-small' <c:if test='${not segmentEnableRyohi}'>disabled</c:if>>選択</button>
					</div>
				</div>
		<!-- ユニバーサルフィールド -->
			<%@ include file="./kogamen/UniversalFieldRyohi.jsp" %>

			<div class='control-group' <c:if test='${not ks.tekiyou.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks.tekiyou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.tekiyou.name)}</label>
				<div class='controls'>
					<input type='text' name='tekiyouRyohi' maxlength='${su:htmlEscape(tekiyouMaxLength)}' class='input-block-level' value='${su:htmlEscape(tekiyouRyohi)}'>
					<span style="line-height:25px;"><br><span style="color:#ff0000;">${su:htmlEscape(chuuki2)}</span></span>
				</div>
			</div>

			<div class='control-group hissu-group'>
				<label for='shiharaiLabel' class='control-label'>支払</label>
				<div class='controls' id='shiharai'>
					<label class='label' <c:if test='${not ks.shiharaiKiboubi.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shiharaiKiboubi.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiKiboubi.name)}</label>
					<input type='text' name='shiharaiKiboubi' class='input-small datepicker' <c:if test='${not ks.shiharaiKiboubi.hyoujiFlg}'>style='display:none;'</c:if> value='${su:htmlEscape(shiharaiKiboubi)}'>
					<label class='label' <c:if test='${not ks.shiharaiHouhou.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shiharaiHouhou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shiharaiHouhou.name)}</label>
					<select id='shiharaihouhou' name='shiharaihouhou' class='input-small' <c:if test='${not ks.shiharaiHouhou.hyoujiFlg}'>style='display:none;'</c:if> <c:if test="${disableShiharaiHouhou}">disabled</c:if>>
						<c:forEach var='record' items='${shiharaihouhouList}'>
							<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq shiharaihouhou}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
						</c:forEach>
					</select>
					<!-- 支払日 --><%-- 申請後表示、経理承認時のみ変更可能 --%>
					<span <c:if test="${shiharaiBiMode == 0}">style='display:none;'</c:if>>
						<label class='label hissu'><span class='required'>*</span>支払日</label>
						<input type='text' name='shiharaibi' class='input-small datepicker hissu' value='${su:htmlEscape(shiharaibi)}' <c:if test="${shiharaiBiMode != 1}">disabled</c:if>>
					</span>
					<!-- 計上日 --><%-- 会社設定により申請者のとき変更可能 または 申請後表示、経理承認時のみ変更可能※現金主義なら常に非表示 --%>
					<span id='keijoubi' <c:if test="${keijouBiMode == 0}">class='never_show' style='display:none;'</c:if>>
						<label class='label hissu'><span class='required'>*</span>計上日</label>
<c:if test="${keijouBiMode == 0 || keijouBiMode == 2}">
						<input type='text' name='keijoubi' class='input-small datepicker hissu' value='${su:htmlEscape(keijoubi)}' disabled>
</c:if>
<c:if test="${keijouBiMode == 1}">
						<input type='text' name='keijoubi' class='input-small datepicker hissu' value='${su:htmlEscape(keijoubi)}'>
</c:if>
<c:if test="${keijouBiMode == 3}">
			            <select name='keijoubi' class='input-medium hissu'>
							<option value=''></option>
		<c:forEach var="record" items="${keijoubiList}">
							<option value='${record}' <c:if test='${record == keijoubi}'>selected</c:if>>${record}</option>
		</c:forEach>
						</select>
</c:if>
					</span>
				</div>
			</div>
		</div>
	</section>

	<div class='control-group<c:if test='${!houjinCardRirekiEnable}'> never_show' style='display:none;</c:if>'>
		<button type='button' id='houjinCardRirekiAddButton' class="btn btn-small"<c:if test='${(not enableInput)}'> disabled </c:if> >法人カード履歴参照</button>
	</div>

	<%@ include file="./kogamen/RyohiKoutsuuhiMeisaiList.jsp" %>

	<section class='print-unit' id="goukeiSection">
		<div class='control-group' <c:if test='${not ks.goukeiKingaku.hyoujiFlg}'>style='display:none;'</c:if>>
			<label class='control-label'><c:if test='${ks.goukeiKingaku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.goukeiKingaku.name)}</label>
			<div class='controls'>
				<input type='text' class='input-medium autoNumericNoMax hissu' name='kingaku' <c:if test='${not ks.goukeiKingaku.hyoujiFlg}'>style='display:none;'</c:if> value ='${su:htmlEscape(kingaku)}' disabled>円
				<!-- 法人カード -->
				<span <c:if test="${!houjinCardFlag}">style='display:none;'</c:if>>
				<label class="label">${su:htmlEscape(ks.uchiHoujinCardRiyouGoukei.name)}</label>
				<input type="text" name="houjinCardRiyouGoukei" class="input-medium autoNumericNoMax" disabled value='${su:htmlEscape(houjinCardRiyouGoukei)}'>円
				</span>
				<!-- 会社手配 -->
				<span <c:if test="${!kaishaTehaiFlag}">style='display:none;'</c:if>>
				<label class="label">${su:htmlEscape(ks.kaishaTehaiGoukei.name)}</label>
				<input type="text" name="kaishaTehaiGoukei" class="input-medium autoNumericNoMax" disabled value='${su:htmlEscape(kaishaTehaiGoukei)}'>円
				</span>
			</div>
		</div>
		<div class='control-group'>
				<label  class="control-label" <c:if  test="${not ks.sashihikiShikyuuKingaku.hyoujiFlg}">style='display:none;'</c:if>><c:if test='${ks.sashihikiShikyuuKingaku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.sashihikiShikyuuKingaku.name)}</label>
			<div class='controls'>
				<input <c:if test="${not ks.sashihikiShikyuuKingaku.hyoujiFlg}">style='display:none;'</c:if> type='text' name='sashihikiShikyuuKingaku' class='input-medium autoNumericNoMax hissu' disabled value ='${su:htmlEscape(sashihikiShikyuuKingaku)}'>円
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
				<c:if test='${ks.shiharaiKingakuGoukei10Percent.hyoujiFlg}'><!-- とりあえず10%が表示されるなら出しておく（基本的に共通と思われるので -->
					<button type='button' id='zeigakuShousaiButton' class='btn btn-small'>消費税額詳細</button>
				</c:if>
			</div>
		</div>
	</section>

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

//明細ダイアログ（共通）で親を呼ぶためのインターフェース
function getKikanFrom() {return $("[name=seisankikanFrom]")};
function getKikanTo() {return $("[name=seisankikanTo]")};

/**
 * 明細の金額計算、金額の合計値を計算するFunction
 */
function calcMoney() {
	// 伝票の金額(明細の金額合計)
	amountKingaku($("[name=meisaiKingaku]"), $("[name='kingaku']"));

 	var kingakTag					= $("input[name=kingaku]");
 	var houjinCardRiyouKingakuTag	= $("input[name='houjinCardRiyouGoukei']");
 	var kaishaTehaiKingakuTag		= $("input[name='kaishaTehaiGoukei']");
 	var sashihikiShikyuuKingakuTag  = $("input[name='sashihikiShikyuuKingaku']");

	let kazeiKbn = $("select[name=kazeiKbnRyohi] :selected").val();
	let zeiritsu = Number(["001", "002", "011", "012", "013", "014"].includes(kazeiKbn) 
			? $("#zeiritsuRyohi").find("option:selected").val()
			: 0);
	let hasuuShoriFlg = $("#workflowForm").find("input[name=hasuuShoriFlg]").val();
	let shiireKeikaSothiFlg = $("#workflowForm").find("input[name=shiirezeigakuKeikasothi]").val();

	// 税額計算
	$("#meisaiList01 tr.meisai").each(function() {
		calcMeisaiZeigaku(kazeiKbn, zeiritsu, hasuuShoriFlg, $(this), shiireKeikaSothiFlg);
	});
	//税額計算前に消費税額の値をクリア
	$("[name='shiharaiKingakuGoukei10Percent']").val("0");
	$("[name='zeinukiKingaku10Percent']").val("0");
	$("[name='shouhizeigaku10Percent']").val("0");
	$("[name='shiharaiKingakuGoukei8Percent']").val("0");
	$("[name='zeinukiKingaku8Percent']").val("0");
	$("[name='shouhizeigaku8Percent']").val("0");
	amountKingaku($("[name=meisaiKingaku]"), $("[name='shiharaiKingakuGoukei" + zeiritsu +"Percent']"));
	amountKingaku($("[name=zeinukiKingakuRyohi]"), $("[name='zeinukiKingaku" + zeiritsu +"Percent']"));
	amountKingaku($("input[name=shouhizeigakuRyohi]"), $("[name='shouhizeigaku" + zeiritsu +"Percent']"));
	
	// 法人カード利用合計
	var houjinCardRiyouGoukei = 0;
	for (var i = 0; i < $("[name=houjinCardFlgRyohi]").length; i++) {
		if ($("#meisaiList01").find("[name=houjinCardFlgRyohi]").eq(i).val() == '1') {
			houjinCardRiyouGoukei += parseInt($("#meisaiList01").find("[name=meisaiKingaku]").eq(i).val().replaceAll(",", ""));
		}
	}
	$("[name='houjinCardRiyouGoukei']").val(String(houjinCardRiyouGoukei).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	// 会社手配合計
	var kaishaTehaiGoukei = 0;
	for (var i = 0; i < $("[name=kaishaTehaiFlgRyohi]").length; i++) {
		if ($("#meisaiList01").find("[name=kaishaTehaiFlgRyohi]").eq(i).val() == '1') {
			kaishaTehaiGoukei += parseInt($("#meisaiList01").find("[name=meisaiKingaku]").eq(i).val().replaceAll(",", ""));
		}
	}
	$("[name='kaishaTehaiGoukei']").val(String(kaishaTehaiGoukei).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
 	//差引支給金額
 	var sashihikiShikyuuKingaku = kingakTag.getMoney() - houjinCardRiyouKingakuTag.getMoney() - kaishaTehaiKingakuTag.getMoney();
 	sashihikiShikyuuKingakuTag.putMoney(sashihikiShikyuuKingaku);;
 	
	// インボイス関連（eteam.jsより読込）
	//calcInvoiceKingaku();
}

//画面表示後の初期化
$(document).ready(function(){
	// 明細表示をロード
	setDisplayRyohiMeisaiData(1);

<c:if test='${enableInput}'>
	//取引先選択
	$("#torihikiSentakuButton").click(function(e){
		torihikiSentaku();
	});

	//仕訳枝番号変更時Function
	$("input[name=shiwakeEdaNoRyohi]").change(function(){
		shiwakeEdaNoLostFocus();
	});

	//勘定科目枝番選択ボタン押下時Function
	$("#kamokuEdabanSentakuButton").click(function(e){
		dialogRetKamokuEdabanCd				= $("input[name=kamokuEdabanCdRyohi]");
		dialogRetKamokuEdabanName			= $("input[name=kamokuEdabanNameRyohi]");
		dialogRetKazeiKbn = $("select[name=kazeiKbnRyohi]");
		dialogRetBunriKbn = $("select[name=bunriKbn]");
		dialogRetKariShiireKbn = $("select[name=kariShiireKbn]");
		dialogCallbackKanjyouKamokuEdabanSentaku = function(){
			displayZeiritsu();
			$("input[name=futanBumonCdRyohi]").blur();
		};
		commonKamokuEdabanSentaku($("[name=kamokuCdRyohi]").val(), $("input[name=denpyouKbn]").val(), $("input[name=shiwakeEdaNoRyohi]").val());
	});

	//勘定科目枝番コードロストフォーカス時Function
	$("input[name=kamokuEdabanCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var kamokuCd = 	$("input[name=kamokuCdRyohi]").val();
		dialogRetKamokuEdabanCd				= $("input[name=kamokuEdabanCdRyohi]");
		dialogRetKamokuEdabanName			= $("input[name=kamokuEdabanNameRyohi]");
		dialogRetKazeiKbn = $("select[name=kazeiKbnRyohi]");
		dialogRetBunriKbn = $("select[name=bunriKbn]");
		dialogRetKariShiireKbn = $("select[name=kariShiireKbn]");
		dialogCallbackKanjyouKamokuEdabanSentaku = function(){
			displayZeiritsu();
			$("input[name=futanBumonCdRyohi]").blur();
		};
		commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamokuCd, title, $("select[name=kazeiKbnRyohi]"), $("select[name=bunriKbn]"), $("input[name=denpyouKbn]").val(), $("input[name=shiwakeEdaNoRyohi]").val());
	});

	//負担部門選択ボタン押下時Function
	$("#futanBumonSentakuButton").click(function(e){
		dialogRetFutanBumonCd				= $("input[name=futanBumonCdRyohi]");
		dialogRetFutanBumonName				= $("input[name=futanBumonNameRyohi]");
		dialogRetKazeiKbn = $("select[name=kazeiKbnRyohi]");
		dialogRetBunriKbn = $("select[name=bunriKbn]");
		dialogRetKariShiireKbn = $("select[name=kariShiireKbn]");
		dialogCallbackFutanBumonSentaku = function(){
			displayZeiritsu();
		};
		commonFutanBumonSentaku("1",$("input[name=kamokuCdRyohi]").val(),$("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val(), $("input[name=denpyouKbn]").val(), $("input[name=shiwakeEdaNoRyohi]").val());
	});

	//負担部門コードロストフォーカス時Function
	$("input[name=futanBumonCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetFutanBumonCd				= $("input[name=futanBumonCdRyohi]");
		dialogRetFutanBumonName				= $("input[name=futanBumonNameRyohi]");
		dialogRetKazeiKbn = $("select[name=kazeiKbnRyohi]");
		dialogRetBunriKbn = $("select[name=bunriKbn]");
		dialogRetKariShiireKbn = $("select[name=kariShiireKbn]");
		dialogCallbackFutanBumonSentaku = function(){
			displayZeiritsu();
		};
		commonFutanBumonCdLostFocus("1",dialogRetFutanBumonCd, dialogRetFutanBumonName, title, $("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val(), $("input[name=kamokuCdRyohi]").val(), $("select[name=kariShiireKbn]"), $("input[name=denpyouKbn]").val(), $("input[name=shiwakeEdaNoRyohi]").val());
	});

	//取引先選択ボタン押下時Function
	$("#torihikisakiSentakuButton").click(function(e){
		dialogRetTorihikisakiCd				= $("input[name=torihikisakiCdRyohi]");
		dialogRetTorihikisakiName			= $("input[name=torihikisakiNameRyohi]");
		dialogRetFurikomisaki 				= $("input[name=furikomisakiJouhou]");
		commonTorihikisakiSentaku();
	});

	//取引先クリアボタン押下時Function
	$("#torihikisakiClearButton").click(function(e){
		$("input[name=torihikisakiCdRyohi]").val("");
		$("input[name=torihikisakiNameRyohi]").val("");
		$("input[name=furikomisakiJouhou]").val("");
	});

	//取引先コードロストフォーカス時Function
	$("input[name=torihikisakiCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetTorihikisakiCd				= $("input[name=torihikisakiCdRyohi]");
		dialogRetTorihikisakiName			= $("input[name=torihikisakiNameRyohi]");
		dialogRetFurikomisaki 				= $("input[name=furikomisakiJouhou]");
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

	//ユニバーサルフィールド１選択ボタン押下時Function
	$("#uf1SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=uf1CdRyohi]");
		dialogRetUniversalFieldName		= $("input[name=uf1NameRyohi]");
		commonUniversalSentaku($("[name=kamokuCdRyohi]").val(), 1);
	});

	// 精算期間を本体から明細に反映（明細の期間未入力の時=参照起票のみ)
	$("input[name=seisankikanFrom]").change(function(){
		if ($(this).val() == "") return;
		$("input[name=kikanFrom]").each(function(){
			if ($(this).val() == "") {
				$(this).val($("input[name=seisankikanFrom]").val());
			}
		});
		setDisplayRyohiMeisaiData(1);
	});

	//法人カード履歴参照ボタン
	$("#houjinCardRirekiAddButton").click(function(){
		houjinCardRirekiSentakuDialogOpen();
	});

</c:if>

<c:if test='${not enableInput}'>
	//起票モードの場合のみ表示可能に
	$("#kaikeiContent").find("button").css("display", "none");
	$("#kaikeiContent").find("input,textarea,select").prop("disabled", true);
</c:if>

	// 消費税額詳細ボタンクリック
	$('#zeigakuShousaiButton').on('click', function() {
	    commonShouhizeiShousai($("input[name=denpyouKbn]").val());
	  });
	  
	// 課税・分離・仕入区分の変更時
	$("select[name=kazeiKbnRyohi]").change(function() {
		displayZeiritsu();
	});
	$("select[name=bunriKbn]").change(function() {
		displayZeiritsu();
	});
	$("select[name=kariShiireKbn]").change(function() {
		displayZeiritsu();
	});
});

/**
 * イベントボタン押下時のアクションの切り替え
 */
function eventBtn(eventName) {
	var formObject = document.getElementById("workflowForm");

	// 支払日チェック
	var checkShiharaiBi = ($("input[name=shiharaibi]").prop("disabled") == false);

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
				//20220517
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
		formObject.action = 'koutsuuhi_seisan_touroku';
		break;
	case 'update':
		formObject.action = 'koutsuuhi_seisan_koushin';
		break;
	case 'shinsei':
		formObject.action = 'koutsuuhi_seisan_shinsei';
		break;
	case 'shounin':
		formObject.action = 'koutsuuhi_seisan_shounin';
		break;
	case 'shouninkaijo':
		formObject.action = 'koutsuuhi_seisan_shouninkaijo';
		break;
	}

	formObject.method = 'post';
	formObject.target = '_self';
	$(formObject).submit();
}

/**
 * 申請内容の表示切替
 */
function displayShinsei(isDisp, speed) {
	var section = $("#shinseiSection");
	var divCtrlGrp  = section.find("div.control-group");
	for (var i = 0; i < divCtrlGrp.length; i++) {
		var div = divCtrlGrp.eq(i);
		if (isDisp) {
			div.show(speed);
		} else {
			if (!div.hasClass("hissu-group")) {
				div.hide(speed);
			}
		}
	}

	var divShiharai = $("#shiharai");
	var divShiharaiChildren = divShiharai.children();
	for (var i = 0; i < divShiharaiChildren.length; i++) {
		var divChildren = divShiharaiChildren.eq(i);
		if (isDisp) {
			section.find("label[for='shiharaiLabel']").show(speed);
			if (!divChildren.hasClass("display-none")) {
				divChildren.show(speed);
			}
		} else {
			if (!divChildren.hasClass("hissu")) {
<c:if test="${0 == shiharaiBiMode}">
				section.find("label[for='shiharaiLabel']").hide(speed);
</c:if>
				divChildren.hide(speed);
			}
		}
	}

	if (isDisp) {
		// 明細を非表示にする
		$("#meisaiRyohiField").show(speed);
	} else {
		$("#meisaiRyohiField").hide(speed);
	}

	var sectionGoukek = $("#goukeiSection");
	var divCtrls  = sectionGoukek.find("div.controls");
	var divCtrlsChildren = divCtrls.children();
	for (var i = 0; i < divCtrlsChildren.length; i++) {
		var divChildren = divCtrlsChildren.eq(i);
		if (isDisp) {
			divChildren.show(speed);
		} else {
			if (!divChildren.hasClass("hissu")) {
				divChildren.hide(speed);
			}
		}
	}
}

/**
 * 取引選択
 *【WARNING】こちら変更した場合は必ずshiwakeEdaNoLostFocus()もメンテすること【WARNING】
 */
function torihikiSentaku(){
	setTorihikiDialogRet();
	dialogCallbackTorihikiSentaku = function() { shiwakeEdaNoLostFocus() };
	commonTorihikiSentaku($("input[name=denpyouKbn]").val(), $("input[name=kihyouBumonCd]").val(), $("input[name=denpyouId]").val(), $("input[name=daihyouFutanBumonCd]").val(), "");
};

/**
 * 仕訳枝番号変更時Function
 *【WARNING】こちら変更した場合は必ずtorihikiSentaku()もメンテすること【WARNING】
 */
function shiwakeEdaNoLostFocus(){
	setTorihikiDialogRet();
	var title = $("input[name=shiwakeEdaNo]").parent().parent().find(".control-label").text();
	commonShiwakeEdaNoLostFocus(dialogRetShiwakeEdaNo, dialogRetTorihikiName, title, $("input[name=denpyouKbn]").val(), $("input[name=kihyouBumonCd]").val(), $("input[name=denpyouId]").val(), $("input[name=daihyouFutanBumonCd]").val(),"",$("#denpyouJouhou").find("select[name=invoiceDenpyou] option:selected").val());
};

/**
 * 取引ダイアログ表示後の設定項目Function
 */
function setTorihikiDialogRet() {
	dialogRetTorihikiName				= $("input[name=torihikiNameRyohi]");
	dialogRetShiwakeEdaNo				= $("input[name=shiwakeEdaNoRyohi]");
	dialogRetKamokuCd					= $("input[name=kamokuCdRyohi]");
	dialogRetKamokuName					= $("input[name=kamokuNameRyohi]");
	dialogRetShoriGroup					= $("input[name=shoriGroupRyohi]");
	dialogRetKamokuEdabanCd				= $("input[name=kamokuEdabanCdRyohi]");
	dialogRetKamokuEdabanName			= $("input[name=kamokuEdabanNameRyohi]");
	dialogRetKamokuEdabanSentakuButton	= $("#kamokuEdabanSentakuButton");
	dialogRetFutanBumonCd				= $("input[name=futanBumonCdRyohi]");
	dialogRetFutanBumonName				= $("input[name=futanBumonNameRyohi]");
	dialogRetFutanBumonSentakuButton	= $("#futanBumonSentakuButton");
	dialogRetTorihikisakiCd				= $("input[name=torihikisakiCdRyohi]");
	dialogRetTorihikisakiName			= $("input[name=torihikisakiNameRyohi]");
	dialogRetFurikomisaki 				= $("input[name=furikomisakiJouhou]");
	dialogRetTorihikisakiSentakuButton	= $("#torihikisakiSentakuButton");
	dialogRetTorihikisakiClearButton	= $("#torihikisakiClearButton");
	dialogRetProjectCd					= $("input[name=projectCdRyohi]");
	dialogRetProjectName				= $("input[name=projectNameRyohi]");
	dialogRetProjectSentakuButton		= $("#projectSentakuButton");
	dialogRetSegmentCd					= $("input[name=segmentCdRyohi]");
	dialogRetSegmentName				= $("input[name=segmentNameRyohi]");
	dialogRetSegmentSentakuButton		= $("#segmentSentakuButton");
	dialogRetTekiyou					= $("input[name=tekiyouRyohi]");
	dialogRetKazeiKbn					= $("select[name=kazeiKbnRyohi]");
	dialogRetBunriKbn 					= $("select[name=bunriKbn]");
	dialogRetKariShiireKbn 				= $("select[name=kariShiireKbn]");
	dialogRetJigyoushaKbn				= $("input[name=jigyoushaKbn]");
	dialogRetKazeiKbn = $("select[name=kazeiKbnRyohi]");
	dialogRetBunriKbn = $("select[name=bunriKbn]");
	dialogRetKariShiireKbn = $("select[name=kariShiireKbn]");
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
	dialogRetShainCd	= $("input[name=uf4CdRyohi]");
	dialogRetSahinName	= $("input[name=uf4NameRyohi]");
	</c:when>
	<c:when test="${'UF5' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf5CdRyohi]");
		dialogRetSahinName	= $("input[name=uf5NameRyohi]");
	</c:when>
	<c:when test="${'UF6' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf6CdRyohi]");
		dialogRetSahinName	= $("input[name=uf6NameRyohi]");
	</c:when>
	<c:when test="${'UF7' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf7CdRyohi]");
		dialogRetSahinName	= $("input[name=uf7NameRyohi]");
	</c:when>
	<c:when test="${'UF8' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf8CdRyohi]");
		dialogRetSahinName	= $("input[name=uf8NameRyohi]");
	</c:when>
	<c:when test="${'UF9' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf9CdRyohi]");
		dialogRetSahinName	= $("input[name=uf9NameRyohi]");
	</c:when>
	<c:when test="${'UF10' == shainCdRenkeiArea}">
		dialogRetShainCd	= $("input[name=uf10CdRyohi]");
		dialogRetSahinName	= $("input[name=uf10NameRyohi]");
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
	dialogRetUf4Cd				= $("input[name=uf4CdRyohi]");
	dialogRetUf4Name			= $("input[name=uf4NameRyohi]");
	dialogRetUf4SentakuButton	= $("#uf4SentakuButton");
	</c:if>
	<c:if test="${'UF5' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf5ShiyouFlg}">
		dialogRetUf5Cd				= $("input[name=uf5CdRyohi]");
		dialogRetUf5Name			= $("input[name=uf5NameRyohi]");
		dialogRetUf5SentakuButton	= $("#uf5SentakuButton");
	</c:if>
	<c:if test="${'UF6' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf6ShiyouFlg}">
		dialogRetUf6Cd				= $("input[name=uf6CdRyohi]");
		dialogRetUf6Name			= $("input[name=uf6NameRyohi]");
		dialogRetUf6SentakuButton	= $("#uf6SentakuButton");
	</c:if>
	<c:if test="${'UF7' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf7ShiyouFlg}">
		dialogRetUf7Cd				= $("input[name=uf7CdRyohi]");
		dialogRetUf7Name			= $("input[name=uf7NameRyohi]");
		dialogRetUf7SentakuButton	= $("#uf7SentakuButton");
	</c:if>
	<c:if test="${'UF8' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf8ShiyouFlg}">
		dialogRetUf8Cd				= $("input[name=uf8CdRyohi]");
		dialogRetUf8Name			= $("input[name=uf8NameRyohi]");
		dialogRetUf8SentakuButton	= $("#uf8SentakuButton");
	</c:if>
	<c:if test="${'UF9' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf9ShiyouFlg}">
		dialogRetUf9Cd				= $("input[name=uf9CdRyohi]");
		dialogRetUf9Name			= $("input[name=uf9NameRyohi]");
		dialogRetUf9SentakuButton	= $("#uf9SentakuButton");
	</c:if>
	<c:if test="${'UF10' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf10ShiyouFlg}">
		dialogRetUf10Cd				= $("input[name=uf10CdRyohi]");
		dialogRetUf10Name			= $("input[name=uf10NameRyohi]");
		dialogRetUf10SentakuButton	= $("#uf10SentakuButton");
	</c:if>
	dialogCallbackCommonSetTorihiki = function() {
		displayZeiritsu();
	}
}


/**
 * 法人カード履歴選択ダイアログを開く
 */
function houjinCardRirekiSentakuDialogOpen() {
	var denpyouId = $("[name=denpyouId]").val();//未登録ならブランク、登録後なら入る
	var userId = ""; //起票者となる
	var denpyouKbn = $("input[name=denpyouKbn]").val();

	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "法人カード使用一覧",
		buttons: {追加して閉じる:function() {
									 $("input[name=sentaku]:checked").each(function(){
										if(!$("input[name=shiwakeEdaNoRyohi]").val())
										{
											alert("取引を入力してください。");
											return false;
										}
									 	var meisaiInfoRyohi = getHoujinCardRirekiInfoRyohi($(this));
									 	ryohiMeisaiAdd(meisaiInfoRyohi,"1");
									 });
									 $(this).dialog("close");
								},
						  閉じる:function() {$(this).dialog("close");}}
	})
	.load("houjin_card_siyou_ichiran?denpyouId=" + encodeURI(denpyouId) + "&userId=" + encodeURI(userId) + "&denpyouKbn=" + encodeURI(denpyouKbn));
}


/**
 * 法人カード使用履歴 明細MAP取得
 * @param obj		処理対象オブジェクト
 */
function getHoujinCardRirekiInfoRyohi(obj) {

	// 一覧の最大インデックスを取得する
	var maxIndex = $("#meisaiList01").find("tr.meisai").length - 1;

	//該当行の明細MAP返す
	return {
		 index:							"1"
		,maxIndex:						maxIndex

		,denpyouId:						$("input[name=denpyouId]").val()
		,denpyouKbn:					$("input[name=denpyouKbn]").val()
		,zeroEnabled:					$("input[name=karibaraiOn]:checked").length == 1 ? $("input[name=karibaraiOn]:checked").val() : "0"
		,dispMode:						('true' == $('#enableInput').val()) ? "2" : "3"

		,kikanFrom:						obj.attr("houjin-card-riyoubi")
		,shubetsuCd:					"1"
		,shubetsu1:						"交通費"
		,shubetsu2:						"法人カード"
		,koutsuuShudan:					""
		,shouhyouShoruiHissuFlg:		""
		,ryoushuushoSeikyuushoTouFlg:	""		//事前のデフォルト値セットは不要。明細側でセットする為。
		,houjinCardFlgRyohi:			"1"
		,kaishaTehaiFlgRyohi:			"0"
		,naiyou:						""
		,bikou:							""
		,oufukuFlg:						""
		,jidounyuuryokuFlg:				"0"
		,tanka:							obj.attr("kingaku")
		,meisaiKingaku:					obj.attr("kingaku")
		,icCardNo:						""
		,icCardSequenceNo:				""
		,himodukeCardMeisaiRyohi:		obj.attr("houjin-card-rireki-no")
		,hayaFlg:						"0"
		,yasuFlg:						"0"
		,rakuFlg:						"0"
	};
}

/**
 * 課税区分により税率の標示有無切替
 * 税込・税抜なら表示、それ以外なら非表示
 * 取引選択時に呼ばれる
 */
function displayZeiritsu() {
	var kazeiKbnGroup = $("select[name=kazeiKbnRyohi] option:selected").attr("data-kazeiKbnGroup");

	//インボイス対応前：課税区分が税込、税抜以外の場合は消費税入力欄を丸ごと消していた（zeiritsuArea.hide()）
	//20230329 ひとまずの対応として税込、税抜以外の場合は税率を非表示にする
	var hyoujiFlg = (kazeiKbnGroup == "1" || kazeiKbnGroup == "2");
	var displayStyle = ( false == hyoujiFlg ) ? 'hide' : 'show';
	$("#zeiritsuArea")[displayStyle]();
	displaySeigyo();
}

// 課税区分・分離区分・仕入区分は取引仕訳登録で入力した値しか認めないため、常にグレーアウト
function displaySeigyo(){
	setShouhizeiControls($("input[name=denpyouId]").val(), $("input[name=shoriGroupRyohi]").val(), $("select[name=kazeiKbnRyohi]"), $("select[name=zeiritsuRyohi]"), $("select[name=bunriKbn]"), $("select[name=kariShiireKbn]"));
	$("select[name=kazeiKbnRyohi]").prop("disabled",true);
	$("select[name=bunriKbn]").prop("disabled",true);
	$("select[name=kariShiireKbn]").prop("disabled",true);
}

// 明細税率を計算する
function calcMeisaiZeigaku(kazeiKbn, zeiritsu, hasuuShoriFlg, target, shiireKeikaSothiFlg) {
	// 手動修正されたなら手直ししない
	if(target.find("input[name=zeigakuFixFlg]").val() == "1") {
		return;
	}
	let meisaiKingaku = Number(target.find("input[name=meisaiKingaku]").val().replaceAll(",", ""));
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

//初期の使用可否
displayZeiritsu();
		</script>
	</body>
</html>
