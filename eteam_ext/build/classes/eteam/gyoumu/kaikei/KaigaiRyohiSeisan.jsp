<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!-- 注意点　項目を再表示させないようにするときはclassにnever_showを使用すること -->
<div id='kaikeiContent' class='form-horizontal'>

	<!-- 申請内容 -->
	<section class='print-unit' id="shinseiSection">
		<h2>申請内容</h2>
		<div>
			<!-- 使用者 -->
			<div class='control-group hissu-group' <c:if test='${not ks.userName.hyoujiFlg}'>style='display:none;'</c:if>>
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
			<div class='control-group hissu-group' <c:if test='${not karibaraiIsEnabled}'>style='display:none;'</c:if>>
				<label class='control-label'>${su:htmlEscape(ks.karibaraiSentaku.name)}</label>
				<div class='controls'>
<c:if test='${"1" eq userKaribaraiUmuFlg}'><button type='button' id='karibaraiSentakuButton' class='btn btn-small nonChkDirty'><c:if test='${"0" eq dairiFlg}'>データあり</c:if><c:if test='${"1" eq dairiFlg}'>選択</c:if></button></c:if>
					<div id='karibaraiAnken' class='no-more-tables karibaraiAnken'>
						<table class='table-bordered table-condensed non-print' style='margin-bottom:5px; border-collapse: collapse;'>
							<thead>
								<tr>
									<th>${su:htmlEscape(ks.karibaraiDenpyouId.name)}</th>
									<th>${su:htmlEscape(ks.karibaraiOn.name)}</th>
									<th style='width: 200px;'>${su:htmlEscape(ks.karibaraiTekiyou.name)}</th>
									<th>${su:htmlEscape(ks.shinseiKingaku.name)}</th>
									<th <c:if test='${not ks.karibaraiKingakuSagaku.hyoujiFlg}'>style='display:none;'</c:if>>${su:htmlEscape(ks.karibaraiKingakuSagaku.name)}</th>
									<th>${su:htmlEscape(ksKari.karibaraiKingaku.name)}</th>
									<th class='kariari'>${su:htmlEscape(ks.karibaraiMishiyouFlg.name)}</th>
									<th class='karinashi'>${su:htmlEscape(ks.shucchouChuushiFlg.name)}</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td id='karibaraiDenpyouIdHyouji'><a href='kaigai_ryohi_karibarai_shinsei?denpyouId=${su:htmlEscape(karibaraiDenpyouId)}&denpyouKbn=A012' target='_blank'>${su:htmlEscape(karibaraiDenpyouId)}</a></td>
									<td id='karibaraiOnHyouji'>${su:htmlEscape(karibaraiOn eq "1"?"あり":"なし")}</td>
									<td id='karibaraiTekiyouHyouji'>${su:htmlEscape(karibaraiTekiyou)}</td>
									<td id='karibaraiShinseiKingakuHyouji'>${su:htmlEscape(karibaraiShinseiKingaku)}</td>
									<td id='karibaraiKingakuSagakuHyouji' <c:if test='${not ks.karibaraiKingakuSagaku.hyoujiFlg}'>style='display:none;'</c:if>>${su:htmlEscape(karibaraiKingakuSagaku)}</td>
									<td id='karibaraiKingakuHyouji'>${su:htmlEscape(karibaraiKingaku)}</td>
									<td class='kariari' style="text-align:center;"><input type='checkbox' id='karibaraiMishiyouFlg' name='karibaraiMishiyouFlg' value='1' <c:if test='${1 eq karibaraiMishiyouFlg}'>checked</c:if>></td>
									<td class='karinashi' style="text-align:center;"><input type='checkbox' id='shucchouChuushiFlg' name='shucchouChuushiFlg' value='1' <c:if test='${1 eq shucchouChuushiFlg}'>checked</c:if>></td>
								</tr>
							</tbody>
						</table>
						<table class='table-bordered table-condensed print_only' style='margin-bottom:5px; border-collapse: collapse;'>
							<tr>
								<th>${su:htmlEscape(ks.karibaraiDenpyouId.name)}</th>
								<td id='karibaraiDenpyouIdHyouji_print'>${su:htmlEscape(karibaraiDenpyouId)}</td>
								<th>${su:htmlEscape(ksKari.karibaraiKingaku.name)}</th>
								<td id='karibaraiKingakuHyouji_print'>${su:htmlEscape(karibaraiShinseiKingaku)}</td>
							</tr>
						</table>
						<input type='hidden' name='karibaraiDenpyouId' value='${su:htmlEscape(karibaraiDenpyouId)}'>
						<input type='hidden' name='karibaraiOn' value='${su:htmlEscape(karibaraiOn)}'>
						<input type='hidden' name='karibaraiTekiyou' value='${su:htmlEscape(karibaraiTekiyou)}'>
						<input type='hidden' class='autoNumeric' name='karibaraiShinseiKingaku' value='${su:htmlEscape(karibaraiShinseiKingaku)}'>
						<input type='hidden' class='autoNumeric' name='karibaraiKingaku' value='${su:htmlEscape(karibaraiKingaku)}'>
						<input type='hidden' class='autoNumeric' name='karibaraiKingakuSagaku' value='${su:htmlEscape(karibaraiKingakuSagaku)}'>
						<input type='hidden' name='karibaraiKianTenpuZumiFlg' value='${su:htmlEscape(karibaraiKianTenpuZumiFlg)}'>
					</div>
<c:if test='${(not enableInput && 0 eq fn:length(karibaraiDenpyouId)) || (enableInput && "0" eq userKaribaraiUmuFlg && 0 eq fn:length(karibaraiDenpyouId)) }'>データなし</c:if>
				</div>
			</div>
			<!-- ヘッダーフィールド -->
			<%@ include file="./kogamen/HeaderField.jsp" %>
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
				</div>
			</div>
			<!-- 支払希望日／支払方法／支払日／計上日 -->
			<!-- 例外的に、項目レベルでnever_showを入れる -->
			<div class='control-group hissu-group' id='shiharai'>
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
					<span id='shiharaibi' <c:if test="${shiharaiBiMode == 0}">class='never_show' style='display:none;'</c:if>>
						<label class='label'><span class='required'>*</span>支払日</label>
						<input type='text' name='shiharaibi' class='input-small datepicker' value='${su:htmlEscape(shiharaibi)}' <c:if test="${shiharaiBiMode != 1}">disabled</c:if>>
					</span>
					<!-- 計上日 --><%-- 会社設定により申請者のとき変更可能 または 申請後表示、経理承認時のみ変更可能※現金主義なら常に非表示 --%>
					<span id='keijoubi' <c:if test="${keijouBiMode == 0}">class='never_show' style='display:none;'</c:if>>
						<label class='label'><span class='required'>*</span>計上日</label>
<c:if test="${keijouBiMode == 0 || keijouBiMode == 2}">
						<input type='text' name='keijoubi' class='input-small datepicker' value='${su:htmlEscape(keijoubi)}' disabled>
</c:if>
<c:if test="${keijouBiMode == 1}">
						<input type='text' name='keijoubi' class='input-small datepicker' value='${su:htmlEscape(keijoubi)}'>
</c:if>
<c:if test="${keijouBiMode == 3}">
			            <select name='keijoubi' class='input-medium'>
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
		<div class='control-group<c:if test='${!houjinCardRirekiEnable}'> never_show' style='display:none;</c:if>'>
			<input type='hidden' name='ryoushuushoSeikyuushoDefaultRyohi' value='${su:htmlEscape(ryoushuushoSeikyuushoDefaultRyohi)}'>
			<input type='hidden' name='ryoushuushoSeikyuushoDefault' value='${su:htmlEscape(ryoushuushoSeikyuushoDefault)}'>
			<button type='button' id='houjinCardRirekiAddButton' class="btn btn-small"<c:if test='${(not enableInput)}'> disabled </c:if> >法人カード履歴参照</button>
		</div>
	</section>

	<!-- 明細 -->
	<div id='ryohiMeisai'>
	<h2>海外分</h2>
	<!-- 取引 -->
	<div class='control-group<c:if test='${not ks.torihiki.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
		<label class='control-label'><c:if test='${ks.torihiki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.torihiki.name)}</label>
		<div class='controls'>
			<input type="text" name='kaigaiShiwakeEdaNoRyohi'  value='${su:htmlEscape(kaigaiShiwakeEdaNoRyohi)}' class='input-small pc_only'>
			<input type='text' name='kaigaiTorihikiNameRyohi' class='input-xlarge' value='${su:htmlEscape(kaigaiTorihikiNameRyohi)}' disabled>
			<input type='hidden' name='kazeiFlgRyohiKaigai' value='${su:htmlEscape(kazeiFlgRyohiKaigai)}' >
			<input type='hidden' name='kazeiFlgRyohiKaigaiKamoku' value='${su:htmlEscape(kazeiFlgRyohiKaigaiKamoku)}' >
			<button type='button' id='kaigaiTorihikiSentakuButton' class='btn btn-small'>選択</button>
		</div>
	</div>
	<!-- 勘定科目 -->
	<div class='control-group<c:if test='${not ks.kamoku.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
		<label class='control-label'><c:if test='${ks.kamoku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kamoku.name)}</label>
		<div class='controls'>
			<input type='text' name='kaigaiKamokuCdRyohi' class='input-small pc_only' value='${su:htmlEscape(kaigaiKamokuCdRyohi)}' disabled>
			<input type='text' name='kaigaiKamokuNameRyohi' class='input-xlarge' value='${su:htmlEscape(kaigaiKamokuNameRyohi)}' disabled>
			<input type='hidden' name='kaigaiShoriGroupRyohi' value='${su:htmlEscape(kaigaiShoriGroupRyohi)}'>
		</div>
	</div>
	<!-- 課税区分 -->
	<div class='control-group' id='kazeiDiv'<c:if test='${not ks.kazeiKbn.hyoujiFlg}'>style='display:none;'</c:if>>
		<label class='control-label'><c:if test='${ks.kazeiKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kazeiKbn.name)}</label>
		<div class='controls'>
			<select name='kaigaiKazeiKbnRyohi' class='input-medium' disabled>
				<c:forEach var="kazeiKbnRecord" items="${kazeiKbnList}">
					<option value='${kazeiKbnRecord.naibuCd}' data-kazeiKbnGroup='${kazeiKbnRecord.option1}' <c:if test='${kazeiKbnRecord.naibuCd eq kaigaiKazeiKbnRyohi}'>selected</c:if>>${su:htmlEscape(kazeiKbnRecord.name)}</option>
				</c:forEach>
			</select>
		<!-- 分離区分 -->
		<span <c:if test='${not ks.bunriKbn.hyoujiFlg}'>style='display:none;'</c:if>>
		<label class='label' for='bunriKbn'><c:if test='${ks.bunriKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.bunriKbn.name)}</label>
			<select name='kaigaiBunriKbn' id="kaigaiBunriKbn" class='input-small'>
				<c:forEach var="bunriKbnRecord" items="${bunriKbnList}">
				<c:if test='${bunriKbnRecord.naibuCd eq "9"}'>
					<option hidden value='${bunriKbnRecord.naibuCd}' <c:if test='${bunriKbnRecord.naibuCd eq kaigaiBunriKbn}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
				</c:if>
				<c:if test='${bunriKbnRecord.naibuCd ne "9"}'>
					<option value='${bunriKbnRecord.naibuCd}'<c:if test='${bunriKbnRecord.naibuCd eq kaigaiBunriKbn}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
				</c:if>
				</c:forEach>
			</select>
		</span>
		<!-- 仕入区分 -->
		<span <c:if test='${shiireZeiAnbun eq "0"}'>style='display:none;'</c:if>>
		<label class='label' for='kariShiireKbn'>${su:htmlEscape(ks.shiireKbn.name)}</label>
			<select name='kaigaiKariShiireKbn' id="kaigaiKariShiireKbn" class='input-small'>
				<c:forEach var="shiireKbnRecord" items="${shiireKbnList}">
				<c:if test='${shiireKbnRecord.naibuCd eq "0"}'>
					<option hidden value='${shiireKbnRecord.naibuCd}' <c:if test='${shiireKbnRecord.naibuCd eq kaigaiKariShiireKbn}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
				</c:if>
				<c:if test='${shiireKbnRecord.naibuCd ne "0"}'>
					<option value='${shiireKbnRecord.naibuCd}' <c:if test='${shiireKbnRecord.naibuCd eq kaigaiKariShiireKbn}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
				</c:if>
					
				</c:forEach>
			</select>
		</span>
		<input type='hidden' name='kariShiireKbnVal' value='${su:htmlEscape(kaigaiKariShiireKbn)}'>
		</div>
	</div>
	<!-- 勘定科目枝番 -->
	<div class='control-group<c:if test='${not ks.kamokuEdaban.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
		<label class='control-label'><c:if test='${ks.kamokuEdaban.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kamokuEdaban.name)}</label>
		<div class='controls'>
			<input type='text' name='kaigaiKamokuEdabanCdRyohi' class='input-medium pc_only' value='${su:htmlEscape(kaigaiKamokuEdabanCdRyohi)}' <c:if test='${not kaigaiKamokuEdabanEnableRyohi}'>disabled</c:if>>
			<input type='text' name='kaigaiKamokuEdabanNameRyohi' class='input-xlarge' value='${su:htmlEscape(kaigaiKamokuEdabanNameRyohi)}' disabled>
			<button type='button' id='kaigaiKamokuEdabanSentakuButton' class='btn btn-small' <c:if test='${not kaigaiKamokuEdabanEnableRyohi}'>disabled</c:if>>選択</button>
		</div>
	</div>
	<!-- 負担部門 -->
	<div class='control-group<c:if test='${not ks.futanBumon.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
		<label class='control-label'><c:if test='${ks.futanBumon.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.futanBumon.name)}</label>
		<div class='controls'>
			<input type='text' name="kaigaiFutanBumonCdRyohi" class='input-small pc_only' value='${su:htmlEscape(kaigaiFutanBumonCdRyohi)}' <c:if test='${not kaigaiFutanBumonEnableRyohi}'>disabled</c:if>>
			<input type='text' name="kaigaiFutanBumonNameRyohi" class='input-xlarge' value='${su:htmlEscape(kaigaiFutanBumonNameRyohi)}' disabled>
			<button type='button' id='kaigaiFutanBumonSentakuButton' class='btn btn-small' <c:if test='${not kaigaiFutanBumonEnableRyohi}'>disabled</c:if>>選択</button>
		</div>
	</div>
	<!-- 取引先 -->
	<div class='control-group <c:if test='${not ks.torihikisaki.hyoujiFlg}'>never_show' style='display:none;</c:if>'>
		<label class='control-label'><c:if test='${ks.torihikisaki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.torihikisaki.name)}</label>
		<div class='controls'>
			<input type='text' name="kaigaiTorihikisakiCdRyohi" class='input-medium pc_only' <c:if test='${not kaigaiTorihikisakiEnableRyohi}'>disabled</c:if> value='${su:htmlEscape(kaigaiTorihikisakiCdRyohi)}'>
			<input type='text' name="kaigaiTorihikisakiNameRyohi" class='input-xlarge' value='${su:htmlEscape(kaigaiTorihikisakiNameRyohi)}' disabled>
			<button type='button' id='kaigaiTorihikisakiSentakuButton' class='btn btn-small' <c:if test='${not kaigaiTorihikisakiEnableRyohi}'>disabled</c:if>>選択</button>
			<button type='button' id='kaigaiTorihikisakiClearButton' class='btn btn-small' <c:if test='${not kaigaiTorihikisakiEnableRyohi}'>disabled</c:if>>クリア</button>
		</div>
	</div>
	<!-- プロジェクト -->
	<div class='control-group<c:if test='${not("0" ne pjShiyouFlg and ks.project.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
		<label class='control-label'><c:if test='${ks.project.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.project.name)}</label>
		<div class='controls'>
			<input type='text' name="kaigaiProjectCdRyohi" class='pc_only' <c:if test='${not kaigaiProjectEnableRyohi}'>disabled</c:if> value='${su:htmlEscape(kaigaiProjectCdRyohi)}'>
			<input type='text' name="kaigaiProjectNameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(kaigaiProjectNameRyohi)}'>
			<button type='button' id='kaigaiProjectSentakuButton' class='btn btn-small' <c:if test='${not kaigaiProjectEnableRyohi}'>disabled</c:if>>選択</button>
		</div>
	</div>
	<!-- セグメント -->
	<div class='control-group<c:if test='${not("0" ne segmentShiyouFlg and ks.segment.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
		<label class='control-label'><c:if test='${ks.segment.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.segment.name)}</label>
		<div class='controls'>
			<input type='text' name="kaigaiSegmentCdRyohi" class='pc_only' <c:if test='${not kaigaiSegmentEnableRyohi}'>disabled</c:if> value='${su:htmlEscape(kaigaiSegmentCdRyohi)}'>
			<input type='text' name="kaigaiSegmentNameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(kaigaiSegmentNameRyohi)}'>
			<button type='button' id='kaigaiSegmentSentakuButton' class='btn btn-small' <c:if test='${not kaigaiSegmentEnableRyohi}'>disabled</c:if>>選択</button>
		</div>
	</div>
	<!-- ユニバーサルフィールド(海外)-->
	<%@ include file="./kogamen/UniversalFieldRyohiKaigai.jsp" %>
	<!-- 摘要 -->
	<div class='control-group<c:if test='${not ks.tekiyou.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
		<label class='control-label'><c:if test='${ks.tekiyou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.tekiyou.name)}</label>
		<div class='controls'>
			<input type='text' name='kaigaiTekiyouRyohi' maxlength='${su:htmlEscape(tekiyouMaxLength)}' class='input-block-level' value='${su:htmlEscape(kaigaiTekiyouRyohi)}'>
			<span style="line-height:25px;"><br><span style="color:#ff0000;">${su:htmlEscape(kaigaiChuuki2Ryohi)}</span></span>
		</div>
	</div>
	<%@ include file="./kogamen/KaigaiRyohiKoutsuuhiMeisaiList.jsp" %>
	<h2>国内分</h2>
	<!-- 取引 -->
	<div class='control-group<c:if test='${not ks.torihiki.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
		<label class='control-label'>${su:htmlEscape(ks.torihiki.name)}</label>
		<div class='controls'>
			<input type="text" name='shiwakeEdaNoRyohi'  value='${su:htmlEscape(shiwakeEdaNoRyohi)}' class='input-small pc_only'>
			<input type='text' name='torihikiNameRyohi' class='input-xlarge' value='${su:htmlEscape(torihikiNameRyohi)}' disabled>
			<input type='hidden' name='kazeiFlgRyohi' value='${su:htmlEscape(kazeiFlgRyohi)}' >
			<input type='hidden' name='kazeiFlgRyohiKamoku' value='${su:htmlEscape(kazeiFlgRyohiKamoku)}' >
			<button type='button' id='torihikiSentakuButton' class='btn btn-small'>選択</button>
		</div>
	</div>
	<!-- 勘定科目 -->
	<div class='control-group<c:if test='${not ks.kamoku.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
		<label class='control-label'>${su:htmlEscape(ks.kamoku.name)}</label>
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
				<span id='zeiritsuAreaRyohi' <c:if test='${not ks.zeiritsu.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='label' for='zeiritsu'><c:if test='${ks.zeiritsu.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.zeiritsu.name)}</label>
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
					<select name='bunriKbn' id="bunriKbn" class='input-small'>
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
				<span <c:if test='${shiireZeiAnbun eq "0"}'>style='display:none;'</c:if>>
				<label class='label' for='kariShiireKbn'>${su:htmlEscape(ks.shiireKbn.name)}</label>
					<select name='kariShiireKbn' id="kariShiireKbn" class='input-small'>
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
	<!-- 勘定科目枝番 -->
	<div class='control-group<c:if test='${not ks.kamokuEdaban.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
		<label class='control-label'>${su:htmlEscape(ks.kamokuEdaban.name)}</label>
		<div class='controls'>
			<input type='text' name='kamokuEdabanCdRyohi' class='input-medium pc_only' value='${su:htmlEscape(kamokuEdabanCdRyohi)}' <c:if test='${not kamokuEdabanEnableRyohi}'>disabled</c:if>>
			<input type='text' name='kamokuEdabanNameRyohi' class='input-xlarge' value='${su:htmlEscape(kamokuEdabanNameRyohi)}' disabled>
			<button type='button' id='kamokuEdabanSentakuButton' class='btn btn-small' <c:if test='${not kamokuEdabanEnableRyohi}'>disabled</c:if>>選択</button>
		</div>
	</div>
	<!-- 負担部門 -->
	<div class='control-group<c:if test='${not ks.futanBumon.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
		<label class='control-label'>${su:htmlEscape(ks.futanBumon.name)}</label>
		<div class='controls'>
			<input type='text' name="futanBumonCdRyohi" class='input-small pc_only' value='${su:htmlEscape(futanBumonCdRyohi)}' <c:if test='${not futanBumonEnableRyohi}'>disabled</c:if>>
			<input type='text' name="futanBumonNameRyohi" class='input-xlarge' value='${su:htmlEscape(futanBumonNameRyohi)}' disabled>
			<button type='button' id='futanBumonSentakuButton' class='btn btn-small' <c:if test='${not futanBumonEnableRyohi}'>disabled</c:if>>選択</button>
		</div>
	</div>
	<!-- 取引先 -->
	<div class='control-group <c:if test='${not ks.torihikisaki.hyoujiFlg}'>never_show' style='display:none;</c:if>'>
		<label class='control-label'>${su:htmlEscape(ks.torihikisaki.name)}</label>
		<div class='controls'>
			<input type='text' name="torihikisakiCdRyohi" class='input-medium pc_only' <c:if test='${not torihikisakiEnableRyohi}'>disabled</c:if> value='${su:htmlEscape(torihikisakiCdRyohi)}'>
			<input type='text' name="torihikisakiNameRyohi" class='input-xlarge' value='${su:htmlEscape(torihikisakiNameRyohi)}' disabled>
			<button type='button' id='torihikisakiSentakuButton' class='btn btn-small' <c:if test='${not torihikisakiEnableRyohi}'>disabled</c:if>>選択</button>
			<button type='button' id='torihikisakiClearButton' class='btn btn-small' <c:if test='${not torihikisakiEnableRyohi}'>disabled</c:if>>クリア</button>
		</div>
	</div>
	<!-- プロジェクト -->
	<div class='control-group<c:if test='${not("0" ne pjShiyouFlg and ks.project.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
		<label class='control-label'>${su:htmlEscape(ks.project.name)}</label>
		<div class='controls'>
			<input type='text' name="projectCdRyohi" class='pc_only' <c:if test='${not projectEnableRyohi}'>disabled</c:if> value='${su:htmlEscape(projectCdRyohi)}'>
			<input type='text' name="projectNameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(projectNameRyohi)}'>
			<button type='button' id='projectSentakuButton' class='btn btn-small' <c:if test='${not projectEnableRyohi}'>disabled</c:if>>選択</button>
		</div>
	</div>
	<!-- セグメント -->
	<div class='control-group<c:if test='${not("0" ne segmentShiyouFlg and ks.segment.hyoujiFlg)}'> never_show' style='display:none;</c:if>'>
		<label class='control-label'>${su:htmlEscape(ks.segment.name)}</label>
		<div class='controls'>
			<input type='text' name="segmentCdRyohi" class='pc_only' <c:if test='${not segmentEnableRyohi}'>disabled</c:if> value='${su:htmlEscape(segmentCdRyohi)}'>
			<input type='text' name="segmentNameRyohi" class='input-xlarge' disabled value='${su:htmlEscape(segmentNameRyohi)}'>
			<button type='button' id='segmentSentakuButton' class='btn btn-small' <c:if test='${not segmentEnableRyohi}'>disabled</c:if>>選択</button>
		</div>
	</div>

	<!-- ユニバーサルフィールド-->
	<%@ include file="./kogamen/UniversalFieldRyohi.jsp" %>

	<!-- 摘要 -->
	<div class='control-group<c:if test='${not ks.tekiyou.hyoujiFlg}'> never_show' style='display:none;</c:if>'>
		<label class='control-label'>${su:htmlEscape(ks.tekiyou.name)}</label>
		<div class='controls'>
			<input type='text' name='tekiyouRyohi' maxlength='${su:htmlEscape(tekiyouMaxLength)}' class='input-block-level' value='${su:htmlEscape(tekiyouRyohi)}'>
			<span style="line-height:25px;"><br><span style="color:#ff0000;">${su:htmlEscape(chuuki2Ryohi)}</span></span>
		</div>
	</div>
	<%@ include file="./kogamen/RyohiKoutsuuhiMeisaiList.jsp" %>
	<h2>海外・国内共通</h2>
	<%@ include file="./kogamen/DenpyouMeisaiList.jsp" %>
	</div>

	<!-- 明細金額合計／差引支給金額／消費税率 -->
	<section class='print-unit' id="goukeiSection">
		<div class='control-group'>
			<label class='control-label'><span class='required'>*</span>${su:htmlEscape(ks.goukeiKingaku.name)}</label>
			<div class='controls'>
				<!-- 明細金額合計 -->
				<input type='text' name='kingaku' class='input-medium autoNumericNoMax hissu' disabled value ='${su:htmlEscape(kingaku)}'>円
				<!-- 法人カード利用合計 -->
				<label class="label hissu <c:if test="${!houjinCardFlag}">never_show" style="display:none;</c:if>">${su:htmlEscape(ks.uchiHoujinCardRiyouGoukei.name)}</label>
				<input type="text" name="houjinCardRiyouGoukei" class="input-medium autoNumericNoMax hissu <c:if test="${!houjinCardFlag}">never_show" style="display:none;</c:if>" disabled value='${su:htmlEscape(houjinCardRiyouGoukei)}'><c:if test="${houjinCardFlag}">円</c:if>
				<!-- 会社手配合計 -->
				<label class="label hissu <c:if test="${!kaishaTehaiFlag}">never_show" style="display:none;</c:if>">${su:htmlEscape(ks.kaishaTehaiGoukei.name)}</label>
				<input type="text" name="kaishaTehaiGoukei" class="input-medium autoNumericNoMax hissu <c:if test="${!kaishaTehaiFlag}">never_show" style="display:none;</c:if>" disabled value='${su:htmlEscape(kaishaTehaiGoukei)}'><c:if test="${kaishaTehaiFlag}">円</c:if>
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label hissu'><span class='required'>*</span>${su:htmlEscape(ks.sashihikiShikyuuKingaku.name)}</label>
			<div class='controls'>
				<!-- 差引支給金額合計 -->
				<input type='text' name='sashihikiShikyuuKingaku' class='input-medium autoNumericNoMax hissu' disabled value ='${su:htmlEscape(sashihikiShikyuuKingaku)}'>円
				<!-- 仮払金額 -->
				<label class="label hissu">${su:htmlEscape(ksKari.karibaraiKingaku.name)}</label>
				<input type="text" name="karibaraiKingakuGoukei" class="input-medium autoNumericNoMax hissu" disabled>円
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label hissu'>${su:htmlEscape(ks.hiKazeiKingaku.name)}</label>
			<div class='controls'>
				<!-- 非課税金額 -->
				<input type='text' name='hikazeiKingaku' class='input-medium autoNumericNoMax hissu' disabled>円
				<!-- 課税金額 -->
				<label class="label hissu">${su:htmlEscape(ks.kazeiKingaku.name)}</label>
				<input type="text" name="kazeiKingaku" class="input-medium autoNumericNoMax hissu" disabled>円
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

<!-- 旅費精算共通部 -->
<%@ include file="/jsp/eteam/gyoumu/kaikei/RyohiSeisanCommonScript.jsp" %>
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

	// 税額小計
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
	$("#meisaiList02 tr.meisai").each(function() {
		calcMeisaiZeigaku(kazeiKbn, zeiritsu, hasuuShoriFlg, $(this), shiireKeikaSothiFlg);
	});
	
	amountKingaku($("#meisaiList01 input[name=shouhizeigakuRyohi]"), $("[name='"+ zeiritsu +"Percent01']"));
	amountKingaku($("#meisaiList02 input[name=shouhizeigakuRyohi]"), $("[name='" + zeiritsu + "Percent02']"));

	// 自分と違う税率に何か値が入っている場合、これをクリアする
	let otherZeiritsu = zeiritsu == "8"
		? ["10"]
		: zeiritsu == "10"
			? ["8"]
			: ["8", "10"];
	for(let i = 0; i < otherZeiritsu.length; i++) {
		$("[name='"+ otherZeiritsu[i] +"Percent01']").val("0");
		$("[name='"+ otherZeiritsu[i] +"Percent02']").val("0");
	}
		
	amountKingaku($("#meisaiList input[name=shouhizeigaku]").filter(function() { return $(this).closest('td').find('select[name=zeiritsu]').val() == '8'; }), $("[name='8Percent03']"));
	amountKingaku($("#meisaiList input[name=shouhizeigaku]").filter(function() { return $(this).closest('td').find('select[name=zeiritsu]').val() == '10'; }), $("[name='10Percent03']"));

<c:if test='${sasihikiHyoujiFlg}'>
	// 小計から差引金額を減額
	calcSashihikiMoney(0);
</c:if>
<c:if test='${sasihikiHyoujiFlgKaigai}'>
	// 小計から差引金額を減額
	calcSashihikiMoney(1);
</c:if>

	if ($("#karibaraiMishiyouFlg").prop("checked") || $("#shucchouChuushiFlg").prop("checked")) {
		// 伝票の金額(0)
		$("[name='kingaku']").val(0);
		$("[name='houjinCardRiyouGoukei']").val(0);
		$("[name='kaishaTehaiGoukei']").val(0);
	} else {
		// 伝票の金額(明細の金額合計)
		amountKingaku($("[name='shoukei01'],[name='shoukei02'],[name='shoukei03'],[name='shoukei04'],[name='shoukei05']"), $("[name='kingaku']"));
		// 法人カード利用合計の算出
		var houjinCardRiyouGoukei = 0;
		for (var i = 0; i < $("#meisaiList01 input[name=houjinCardFlgRyohi]").length; i++) {
			if ($("#meisaiList01").find("[name=houjinCardFlgRyohi]").eq(i).val() == '1') {
				houjinCardRiyouGoukei += parseInt($("#meisaiList01").find("[name=meisaiKingaku]").eq(i).val().replaceAll(",", ""));
			}
		}
		for (var i = 0; i < $("#meisaiList02 input[name=houjinCardFlgRyohi]").length; i++) {
			if ($("#meisaiList02").find("[name=houjinCardFlgRyohi]").eq(i).val() == '1') {
				houjinCardRiyouGoukei += parseInt($("#meisaiList02").find("[name=meisaiKingaku]").eq(i).val().replaceAll(",", ""));
			}
		}
		for (var i = 0; i < $("#meisaiList input[name=houjinCardFlgKeihi]").length; i++) {
			if ($("#meisaiList").find("[name=houjinCardFlgKeihi]").eq(i).val() == '1') {
				houjinCardRiyouGoukei += parseInt($("#meisaiList").find("[name=shiharaiKingaku]").eq(i).val().replaceAll(",", ""));
			}
		}
		for (var i = 0; i < $("#kaigaiMeisaiList01 input[name=houjinCardFlgRyohi]").length; i++) {
			if ($("#kaigaiMeisaiList01").find("[name=houjinCardFlgRyohi]").eq(i).val() == '1') {
				houjinCardRiyouGoukei += parseInt($("#kaigaiMeisaiList01").find("[name=meisaiKingaku]").eq(i).val().replaceAll(",", ""));
			}
		}
		for (var i = 0; i < $("#kaigaiMeisaiList02 input[name=houjinCardFlgRyohi]").length; i++) {
			if ($("#kaigaiMeisaiList02").find("[name=houjinCardFlgRyohi]").eq(i).val() == '1') {
				houjinCardRiyouGoukei += parseInt($("#kaigaiMeisaiList02").find("[name=meisaiKingaku]").eq(i).val().replaceAll(",", ""));
			}
		}
		$("[name='houjinCardRiyouGoukei']").val(String(houjinCardRiyouGoukei).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		// 会社手配合計の算出
		var kaishaTehaiGoukei = 0;
		for (var i = 0; i < $("#meisaiList01 input[name=kaishaTehaiFlgRyohi]").length; i++) {
			if ($("#meisaiList01").find("[name=kaishaTehaiFlgRyohi]").eq(i).val() == '1') {
				kaishaTehaiGoukei += parseInt($("#meisaiList01").find("[name=meisaiKingaku]").eq(i).val().replaceAll(",", ""));
			}
		}
		for (var i = 0; i < $("#meisaiList02 input[name=kaishaTehaiFlgRyohi]").length; i++) {
			if ($("#meisaiList02").find("[name=kaishaTehaiFlgRyohi]").eq(i).val() == '1') {
				kaishaTehaiGoukei += parseInt($("#meisaiList02").find("[name=meisaiKingaku]").eq(i).val().replaceAll(",", ""));
			}
		}
		for (var i = 0; i < $("#meisaiList input[name=kaishaTehaiFlgKeihi]").length; i++) {
			if ($("#meisaiList").find("[name=kaishaTehaiFlgKeihi]").eq(i).val() == '1') {
				kaishaTehaiGoukei += parseInt($("#meisaiList").find("[name=shiharaiKingaku]").eq(i).val().replaceAll(",", ""));
			}
		}
		for (var i = 0; i < $("#kaigaiMeisaiList01 input[name=kaishaTehaiFlgRyohi]").length; i++) {
			if ($("#kaigaiMeisaiList01").find("[name=kaishaTehaiFlgRyohi]").eq(i).val() == '1') {
				kaishaTehaiGoukei += parseInt($("#kaigaiMeisaiList01").find("[name=meisaiKingaku]").eq(i).val().replaceAll(",", ""));
			}
		}
		for (var i = 0; i < $("#kaigaiMeisaiList02 input[name=kaishaTehaiFlgRyohi]").length; i++) {
			if ($("#kaigaiMeisaiList02").find("[name=kaishaTehaiFlgRyohi]").eq(i).val() == '1') {
				kaishaTehaiGoukei += parseInt($("#kaigaiMeisaiList02").find("[name=meisaiKingaku]").eq(i).val().replaceAll(",", ""));
			}
		}
		$("[name='kaishaTehaiGoukei']").val(String(kaishaTehaiGoukei).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	}

 	//差引支給金額
 	var karibaraiKingaku = 0;
 	if ($("[name='karibaraiOn']").val() == "1") {
 		karibaraiKingaku = $("[name='karibaraiKingaku']").getMoney();
 	}
 	//差引支給金額 = 明細合計金額 - 法人カード利用合計 - 会社手配合計 - 仮払金額
 	var sashihikiShikyuuKingaku = $("[name='kingaku']").getMoney() - $("[name='houjinCardRiyouGoukei']").getMoney() - $("[name='kaishaTehaiGoukei']").getMoney() - karibaraiKingaku;
 	$("[name='sashihikiShikyuuKingaku']").putMoney(sashihikiShikyuuKingaku);

 	//仮払金額
	$("[name='karibaraiKingakuGoukei']").putMoney(karibaraiKingaku);

 	//仮払金額の差額
 	var shinseiSagaku = $("[name='karibaraiShinseiKingaku']").getMoney() - $("[name='kingaku']").getMoney();
 	$("[name='karibaraiKingakuSagaku']").putMoney(shinseiSagaku);
 	$("td[id=karibaraiKingakuSagakuHyouji]").text($("[name='karibaraiKingakuSagaku']").val());

 	if ($("#karibaraiMishiyouFlg").prop("checked") || $("#shucchouChuushiFlg").prop("checked")) {
//		$("#meisaiSection").hide(300);
		$("#meisaiRyohiField").find("botton[name=meisaiAddButton]").prop("disabled", true);
		$("#meisaiKaigaiRyohiField").find("botton[name=kaigaiMeisaiAddButton]").prop("disabled", true);
	} else {
//		$("#meisaiSection").show(300);
		$("#meisaiRyohiField").find("botton[name=meisaiAddButton]").prop("disabled", false);
		$("#meisaiKaigaiRyohiField").find("botton[name=kaigaiMeisaiAddButton]").prop("disabled", false);
	}

	calcKazeiMoney();
	// インボイス関連（eteam.jsより読込）
	calcInvoiceKingaku();
	
}

function resetKazeiFlg(kaigaiFlg){
	if("1" == kaigaiFlg){
		//海外分
		$("#kaigaiMeisaiList01 .meisai").each(function(){
			if ("" != $(this).find("input[name=shubetsuCd]").val()) {
				if("" == $(this).find("input[name=zeiKubun]").val()){
					// 税区分がブランクの時は、取引の課税区分に基づく課税フラグをセット
					$(this).find("input[name=kazeiFlgRyohiMeisai]").val($("input[name=kazeiFlgRyohiKaigai]").val());
				}else if("0" == $(this).find("input[name=zeiKubun]").val()){
					//税区分が未設定(0)の場合は、科目マスターの課税区分に基づく課税フラグをセット
					$(this).find("input[name=kazeiFlgRyohiMeisai]").val($("input[name=kazeiFlgRyohiKaigaiKamoku]").val());
				}
			}
		});
		$("#kaigaiMeisaiList02 .meisai").each(function(){
			if ("" != $(this).find("input[name=shubetsuCd]").val()) {
				if("" == $(this).find("input[name=zeiKubun]").val()){
					// 税区分がブランクの時は、取引の課税区分に基づく課税フラグをセット
					$(this).find("input[name=kazeiFlgRyohiMeisai]").val($("input[name=kazeiFlgRyohiKaigai]").val());
				}else if("0" == $(this).find("input[name=zeiKubun]").val()){
					//税区分が未設定(0)の場合は、科目マスターの課税区分に基づく課税フラグをセット
					$(this).find("input[name=kazeiFlgRyohiMeisai]").val($("input[name=kazeiFlgRyohiKaigaiKamoku]").val());
				}
			}
		});
	}else{
		//国内分
		$("#meisaiList01 .meisai").each(function(){
			if ("" != $(this).find("input[name=shubetsuCd]").val()) {
				if("" == $(this).find("input[name=zeiKubun]").val()){
					// 税区分がブランクの時は、取引の課税区分に基づく課税フラグをセット
					$(this).find("input[name=kazeiFlgRyohiMeisai]").val($("input[name=kazeiFlgRyohi]").val());
				}else if("0" == $(this).find("input[name=zeiKubun]").val()){
					//税区分が未設定(0)の場合は、科目マスターの課税区分に基づく課税フラグをセット
					$(this).find("input[name=kazeiFlgRyohiMeisai]").val($("input[name=kazeiFlgRyohiKamoku]").val());
				}
			}
		});
		$("#meisaiList02 .meisai").each(function(){
			if ("" != $(this).find("input[name=shubetsuCd]").val()) {
				if("" == $(this).find("input[name=zeiKubun]").val()){
					// 税区分がブランクの時は、取引の課税区分に基づく課税フラグをセット
					$(this).find("input[name=kazeiFlgRyohiMeisai]").val($("input[name=kazeiFlgRyohi]").val());
				}else if("0" == $(this).find("input[name=zeiKubun]").val()){
					//税区分が未設定(0)の場合は、科目マスターの課税区分に基づく課税フラグをセット
					$(this).find("input[name=kazeiFlgRyohiMeisai]").val($("input[name=kazeiFlgRyohiKamoku]").val());
				}
			}
		});
	}
}

function calcKazeiMoney() {
	//課税金額・非課税金額
	var kazeiKingaku = 0;
	var hikazeiKingaku = 0;
	var sashihikiKingaku = 0;
	var sashihikiKingakuKaigai = 0;
	var kaigaiKazeiFlg = ['001', '002', '011','012','013','014'].includes($("select[name=kaigaiKazeiKbnRyohi]").val()) ? "1":"0";

	// 2023/12/13 仮対応
	// 課税フラグが課税区分に正しく対応していなかったため、都度課税区分を取得して課税/非課税を判別して加算するよう仮対応
	// （変更前をCOで残すとごちゃごちゃしてしまったので、元を確認する場合はプッシュ履歴等からご確認ください…）
	//海外分
	$("#kaigaiMeisaiList01 .meisai").each(function(){
		if($(this).find("input[name=meisaiKingaku]").val() != ""){
			if ( kaigaiKazeiFlg == "1") {
				kazeiKingaku += parseInt($(this).find("input[name=meisaiKingaku]").val().replaceAll(",", ""));
			} else if(kaigaiKazeiFlg == "0") {
				hikazeiKingaku += parseInt($(this).find("input[name=meisaiKingaku]").val().replaceAll(",", ""));
			}
		}
	});
<c:if test='${sasihikiHyoujiFlgKaigai}'>
	//差引金額（海外）
	sashihikiKingakuKaigai = parseInt($("[name='sashihikiKingakuKaigai']").val().replaceAll(",", "").replaceAll("-", ""));
</c:if>
	$("#kaigaiMeisaiList02 .meisai").each(function(){
		var nittouFlg = $(this).find("input[name=nittouFlg]").val();
		var meisaiKingaku = meisaiKingaku = parseInt($(this).find("input[name=meisaiKingaku]").val().replaceAll(",", ""));

		if("1" === nittouFlg){
			if(meisaiKingaku > sashihikiKingakuKaigai){
				meisaiKingaku -= sashihikiKingakuKaigai;
				sashihikiKingakuKaigai = 0;
			}else{
				sashihikiKingakuKaigai -= meisaiKingaku;
				meisaiKingaku = 0;
			}
		}
		if($(this).find("input[name=meisaiKingaku]").val() != ""){
			if ( kaigaiKazeiFlg == "1") {
				kazeiKingaku += meisaiKingaku;
			} else if(kaigaiKazeiFlg == "0") {
				hikazeiKingaku += meisaiKingaku;
			}
		}
	});

	//国内分
	var kokunaiKazeiFlg = ['001', '002', '011','012','013','014'].includes($("select[name=kazeiKbnRyohi]").val()) ? "1":"0";
	$("#meisaiTableDiv01 .meisai").each(function(){
		if($(this).find("input[name=meisaiKingaku]").val() != ""){
			if ( kokunaiKazeiFlg == "1") {
				kazeiKingaku += parseInt($(this).find("input[name=meisaiKingaku]").val().replaceAll(",", ""));
			} else if(kokunaiKazeiFlg == "0") {
				hikazeiKingaku += parseInt($(this).find("input[name=meisaiKingaku]").val().replaceAll(",", ""));
			}
		}
	});
<c:if test='${sasihikiHyoujiFlg}'>
	//差引金額
	sashihikiKingaku = parseInt($("[name='sashihikiKingaku']").val().replaceAll(",", "").replaceAll("-", ""));
</c:if>
	$("#meisaiTableDiv02 .meisai").each(function(){
		var kazeiFlg = "";
		var nittouFlg = $(this).find("input[name=nittouFlg]").val();
		var meisaiKingaku = meisaiKingaku = parseInt($(this).find("input[name=meisaiKingaku]").val().replaceAll(",", ""));

		if("" != $(this).find("input[name=kazeiFlgRyohiMeisai]").val()){
			kazeiFlg = $(this).find("input[name=kazeiFlgRyohiMeisai]").val();
		}

		if("1" === nittouFlg){
			if(meisaiKingaku > sashihikiKingaku){
				meisaiKingaku -= sashihikiKingaku;
				sashihikiKingaku = 0;
			}else{
				sashihikiKingaku -= meisaiKingaku;
				meisaiKingaku = 0;
			}
		}
		if($(this).find("input[name=meisaiKingaku]").val() != ""){
			if ( kokunaiKazeiFlg == "1") {
				kazeiKingaku += meisaiKingaku;
			} else if(kokunaiKazeiFlg == "0") {
				hikazeiKingaku += meisaiKingaku;
			}
		}
	});

	//その他経費分
	$("#meisaiTableDiv .meisai").each(function(){
		if($(this).find("[name=shiharaiKingaku]").val() != ""){
			if (['001', '002', '011','012','013','014'].includes($(this).find("select[name=kazeiKbn]").val())) {
				kazeiKingaku += $(this).find("[name=shiharaiKingaku]").getMoney();
			} else {
				hikazeiKingaku += $(this).find("[name=shiharaiKingaku]").getMoney();
			}
		}
	});
	$("[name='kazeiKingaku']").putMoney(kazeiKingaku);
	$("[name='hikazeiKingaku']").putMoney(hikazeiKingaku);
}

//画面表示後の初期化
$(document).ready(function(){
	// 明細表示をロード
	setDisplayKaigaiRyohiMeisaiData(1);
	setDisplayKaigaiRyohiMeisaiData(2);
	setDisplayRyohiMeisaiData(1);
	setDisplayRyohiMeisaiData(2);
	setDisplayMeisaiData();

	// 仮払伝票IDがなければ仮払エリアを非表示
	if($("input[name=karibaraiDenpyouId]").val().length == 0) {
		$("#karibaraiAnken").css("display", "none");
	}

	// 仮払未使用フラグか出張中止フラグがONの場合、明細部を非表示
	if ($("#karibaraiMishiyouFlg").prop("checked") || $("#shucchouChuushiFlg").prop("checked")) {

		// 申請内容を非表示にする
		displayRyohiShinsei(false, 0);
	}

	//仮払ありのとき、仮払未使用ボタンを表示
	if ($("input[name=karibaraiOn]").val() == "1") {
		$("div#karibaraiAnken").find("thead").find("th.kariari").css("display" , "");
		$("div#karibaraiAnken").find("tbody").find("td.kariari").css("display" , "");
		$("div#karibaraiAnken").find("thead").find("th.karinashi").css("display", "none");
		$("div#karibaraiAnken").find("tbody").find("td.karinashi").css("display", "none");
	} else {
	//仮払なしのとき、出張中止ボタンを表示
		$("div#karibaraiAnken").find("thead").find("th.kariari").css("display" , "none");
		$("div#karibaraiAnken").find("tbody").find("td.kariari").css("display" , "none");
		$("div#karibaraiAnken").find("thead").find("th.karinashi").css("display", "");
		$("div#karibaraiAnken").find("tbody").find("td.karinashi").css("display", "");
	}

<c:if test='${enableInput}'>

	var dairiFlg = $("#workflowForm").find("input[name=dairiFlg]").val();
	//通常起票時：使用者固定
	if (dairiFlg == "0") {
		$("#userSentakuRyohiButton").prop("disabled", true);
		$("input[name=shainNoRyohi]").prop("disabled", true);
	}

	//選択ボタン押下時アクション
	$("#karibaraiSentakuButton").click(function(){
		if(!$("input[name=kaigaiShiwakeEdaNoRyohi]").val() || !$("input[name=shiwakeEdaNoRyohi]").val())
		{
			let isKaigaiMinyuuryoku = !$("input[name=kaigaiShiwakeEdaNoRyohi]").val();
			let isKokunaiMinyuuryoku = !$("input[name=shiwakeEdaNoRyohi]").val();
			let base = "取引を入力してください。";
			let message = (isKaigaiMinyuuryoku ? ("海外" + base) : "") + ((isKaigaiMinyuuryoku && isKokunaiMinyuuryoku) ? "\n" : "")
				+ (isKokunaiMinyuuryoku ? ("国内" + base) : "");
			alert(message);
			return;
		}
		dialogRetKaribaraiAnkenCd = $("td[id=karibaraiDenpyouIdHyouji]");
		dialogRetKaribaraiAnkenOn  = $("td[id=karibaraiOnHyouji]");
		dialogRetKaribaraiAnkenName  = $("td[id=karibaraiTekiyouHyouji]");
		dialogRetShinseiKingaku  = $("td[id=karibaraiShinseiKingakuHyouji]");
		dialogRetKaribaraiKingaku	  = $("td[id=karibaraiKingakuHyouji]");
		dialogRetRingiHikitsugiUmFlg = $("input[name=karibaraiRingiHikitsugiUmFlg]");
		dialogRetKianTenpuZumiFlg = $("input[name=karibaraiKianTenpuZumiFlg]");
		dialogCallbackKaribaraiAnkenSentaku = function(){
			$("input[name=karibaraiDenpyouId]").val($("td[id=karibaraiDenpyouIdHyouji]").text());
			$("input[name=karibaraiOn]").val($("td[id=karibaraiOnHyouji]").text() == 'あり' ? "1":"0");
			$("input[name=karibaraiTekiyou]").val($("td[id=karibaraiTekiyouHyouji]").text());
			$("input[name=karibaraiShinseiKingaku]").val($("td[id=karibaraiShinseiKingakuHyouji]").text());
			$("input[name=karibaraiKingaku]").val($("td[id=karibaraiKingakuHyouji]").text());
			//仮払ありのとき、仮払未使用ボタンを表示
			if ($("input[name=karibaraiOn]").val() == "1") {
				$("#shucchouChuushiFlg").prop("checked", false);
				$("div#karibaraiAnken").find("thead").find("th.kariari").css("display" , "");
				$("div#karibaraiAnken").find("tbody").find("td.kariari").css("display" , "");
				$("div#karibaraiAnken").find("thead").find("th.karinashi").css("display", "none");
				$("div#karibaraiAnken").find("tbody").find("td.karinashi").css("display", "none");


				//仮払申請が別伝票で起案添付済みのとき、仮払金未使用チェックボックスを変更不可にする
				if ($("input[name=karibaraiKianTenpuZumiFlg]").val() == "1") {
					$("#karibaraiMishiyouFlg").prop("disabled", true);
				} else {
					$("#karibaraiMishiyouFlg").prop("disabled", false);
				}

			} else {
			//仮払なしのとき、出張中止ボタンを表示
				$("#karibaraiMishiyouFlg").prop("checked", false);
				$("div#karibaraiAnken").find("thead").find("th.kariari").css("display" , "");
				$("div#karibaraiAnken").find("tbody").find("td.kariari").css("display" , "");
				$("div#karibaraiAnken").find("thead").find("th.karinashi").css("display", "none");
				$("div#karibaraiAnken").find("tbody").find("td.karinashi").css("display", "none");
				//仮払申請が別伝票で起案添付済みのとき、出張中止チェックボックスを変更不可にする
				if ($("input[name=karibaraiKianTenpuZumiFlg]").val() == "1") {
					$("#shucchouChuushiFlg").prop("disabled", true);
				} else {
					$("#shucchouChuushiFlg").prop("disabled", false);
				}
			}
			// 入力済み明細データを全削除
			kaigaiRyohiMeisaiAllDelete();
			ryohiMeisaiAllDelete();
			meisaiAllDelete();

			calcMoney();
			//仮払の明細を伝票に追加
			if(confirm('明細データを引き継ぎますか？')){
				var formObject = document.getElementById("workflowForm");
				formObject.action = 'kaigai_ryohi_seisan_add_karibarai_meisai?meisaiTsuikaDenpyouId=' + $("input[name=karibaraiDenpyouId]").val();
				formObject.method = 'post';
				formObject.target = '_self';
				$(formObject).submit();
			}
			//稟議金額超過コメントの表示制御
			ringiKoumokuDisplaySeigyoMikihyou();
		};
		commonKaribaraiAnkenSentaku($("input[name=denpyouKbn]").val(), $("input[name=denpyouId]").val(), $("input[name=userIdRyohi]").val());
	});

	$("#karibaraiMishiyouFlg").click(function(){

		// 申請内容の表示
		displayRyohiShinsei(!$(this).prop("checked"), 300);

		// 金額計算
		calcMoney();
	});

	$("#shucchouChuushiFlg").click(function(){

		// 申請内容の表示
		displayRyohiShinsei(!$(this).prop("checked"), 300);

		// 金額計算
		calcMoney();
	});

	//仮払申請が別伝票で起案添付済みのとき、仮払金未使用チェックボックスを変更不可にする
	if ($("input[name=karibaraiOn]").val() == "1" && $("input[name=karibaraiKianTenpuZumiFlg]").val() == "1") {
		$("#karibaraiMishiyouFlg").prop("disabled", true);
	}

	//仮払申請が別伝票で起案添付済みのとき、出張中止チェックボックスを変更不可にする
	if ($("input[name=karibaraiOn]").val() != "1" && $("input[name=karibaraiKianTenpuZumiFlg]").val() == "1") {
		$("#shucchouChuushiFlg").prop("disabled", true);
	}

	//海外取引選択
	$("#kaigaiTorihikiSentakuButton").click(function(e){
		kaigaiTorihikiSentaku();
	});

	//海外仕訳枝番号変更時Function
	$("input[name=kaigaiShiwakeEdaNoRyohi]").change(function(){
		kaigaiShiwakeEdaNoLostFocus();
	});

	//海外勘定科目枝番選択ボタン押下時Function
	$("#kaigaiKamokuEdabanSentakuButton").click(function(e){
		dialogRetKamokuEdabanCd = $("input[name=kaigaiKamokuEdabanCdRyohi]");
		dialogRetKamokuEdabanName = $("input[name=kaigaiKamokuEdabanNameRyohi]");
		dialogRetKazeiKbn = $("select[name=kaigaiKazeiKbnRyohi]");
		dialogRetBunriKbn = $("select[name=kaigaiBunriKbn]");
		dialogRetKariShiireKbn = $("select[name=kaigaiKariShiireKbn]");
		dialogCallbackKanjyouKamokuEdabanSentaku = function(){
			kaigaiDisplaySeigyo();
			$("input[name=kaigaiFutanBumonCdRyohi]").blur();
		};
		commonKamokuEdabanSentaku($("[name=kaigaiKamokuCdRyohi]").val(), $("input[name=denpyouKbn]").val(), $("input[name=kaigaiShiwakeEdaNoRyohi]").val());
	});

	//海外勘定科目枝番コードロストフォーカス時Function
	$("input[name=kaigaiKamokuEdabanCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var kamokuCd = 	$("input[name=kaigaiKamokuCdRyohi]").val();
		dialogRetKamokuEdabanCd = $("input[name=kaigaiKamokuEdabanCdRyohi]");
		dialogRetKamokuEdabanName = $("input[name=kaigaiKamokuEdabanNameRyohi]");
		dialogRetKazeiKbn = $("select[name=kaigaiKazeiKbnRyohi]");
		dialogRetBunriKbn = $("select[name=kaigaiBunriKbn]");
		dialogRetKariShiireKbn = $("select[name=kaigaiKariShiireKbn]");
		dialogCallbackKanjyouKamokuEdabanSentaku = function(){
			kaigaiDisplaySeigyo();
			$("input[name=kaigaiFutanBumonCdRyohi]").blur();
		};
		commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamokuCd, title, $("select[name=kaigaiKazeiKbnRyohi]"), $("select[name=kaigaiBunriKbn]"), $("input[name=denpyouKbn]").val(), $("input[name=kaigaiShiwakeEdaNoRyohi]").val());
	});

	//海外負担部門選択ボタン押下時Function
	$("#kaigaiFutanBumonSentakuButton").click(function(e){
		dialogRetFutanBumonCd = $("input[name=kaigaiFutanBumonCdRyohi]");
		dialogRetFutanBumonName = $("input[name=kaigaiFutanBumonNameRyohi]");
		dialogRetKazeiKbn = $("select[name=kaigaiKazeiKbnRyohi]");
		dialogRetBunriKbn = $("select[name=kaigaiBunriKbn]");
		dialogRetKariShiireKbn = $("select[name=kaigaiKariShiireKbn]");
		dialogCallbackFutanBumonSentaku = function(){
			kaigaiDisplaySeigyo();
		};
		commonFutanBumonSentaku("1",$("input[name=kaigaiKamokuCdRyohi]").val(),$("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val(), $("input[name=denpyouKbn]").val(), $("input[name=kaigaiShiwakeEdaNoRyohi]").val());
	});

	//海外負担部門コードロストフォーカス時Function
	$("input[name=kaigaiFutanBumonCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetFutanBumonCd = $("input[name=kaigaiFutanBumonCdRyohi]");
		dialogRetFutanBumonName = $("input[name=kaigaiFutanBumonNameRyohi]");
		dialogRetKazeiKbn = $("select[name=kaigaiKazeiKbnRyohi]");
		dialogRetBunriKbn = $("select[name=kaigaiBunriKbn]");
		dialogRetKariShiireKbn = $("select[name=kaigaiKariShiireKbn]");
		dialogCallbackFutanBumonSentaku = function(){
			kaigaiDisplaySeigyo();
		};
		commonFutanBumonCdLostFocus("1",dialogRetFutanBumonCd, dialogRetFutanBumonName, title, $("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val(), $("input[name=kaigaiKamokuCdRyohi]").val(), $("select[name=kaigaiKariShiireKbn]"), $("input[name=denpyouKbn]").val(), $("input[name=kaigaiShiwakeEdaNoRyohi]").val());
	});

	//海外取引先選択ボタン押下時Function
	$("#kaigaiTorihikisakiSentakuButton").click(function(e){
		dialogRetTorihikisakiCd = $("input[name=kaigaiTorihikisakiCdRyohi]");
		dialogRetTorihikisakiName = $("input[name=kaigaiTorihikisakiNameRyohi]");
		dialogRetFurikomisaki  = $("input[name=kaigaiFurikomisakiJouhouRyohi]");
		commonTorihikisakiSentaku();
	});

	//海外取引先クリアボタン押下時Function
	$("#kaigaiTorihikisakiClearButton").click(function(e){
		$("input[name=kaigaiTorihikisakiCdRyohi]").val("");
		$("input[name=kaigaiTorihikisakiNameRyohi]").val("");
		$("input[name=kaigaiFurikomisakiJouhouRyohi]").val("");
	});

	//海外取引先コードロストフォーカス時Function
	$("input[name=kaigaiTorihikisakiCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetTorihikisakiCd = $("input[name=kaigaiTorihikisakiCdRyohi]");
		dialogRetTorihikisakiName = $("input[name=kaigaiTorihikisakiNameRyohi]");
		dialogRetFurikomisaki  = $("input[name=kaigaiFurikomisakiJouhouRyohi]");
		commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd, dialogRetTorihikisakiName, title, dialogRetFurikomisaki);
	});

	//海外プロジェクト選択ボタン押下時Function
	$("#kaigaiProjectSentakuButton").click(function(e){
		dialogRetProjectCd = $("input[name=kaigaiProjectCdRyohi]");
		dialogRetProjectName = $("input[name=kaigaiProjectNameRyohi]");
		commonProjectSentaku();
	});

	//海外プロジェクトコードロストフォーカス時Function
	$("input[name=kaigaiProjectCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetProjectCd = $("input[name=kaigaiProjectCdRyohi]");
		dialogRetProjectName = $("input[name=kaigaiProjectNameRyohi]");
		commonProjectCdLostFocus(dialogRetProjectCd, dialogRetProjectName, title);
	});

	//海外セグメント選択ボタン押下時Function
	$("#kaigaiSegmentSentakuButton").click(function(e){
		dialogRetSegmentCd = $("input[name=kaigaiSegmentCdRyohi]");
		dialogRetSegmentName = $("input[name=kaigaiSegmentNameRyohi]");
		commonSegmentSentaku();
	});

	//海外セグメントコードロストフォーカス時Function
	$("input[name=kaigaiSegmentCdRyohi]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetSegmentCd = $("input[name=kaigaiSegmentCdRyohi]");
		dialogRetSegmentName = $("input[name=kaigaiSegmentNameRyohi]");
		commonSegmentCdLostFocus(dialogRetSegmentCd, dialogRetSegmentName, title);
	});

	// 課税・分離・仕入区分の変更時
	$("select[name=kaigaiKazeiKbnRyohi]").change(function() {
		kaigaiDisplaySeigyo();
	});
	$("select[name=kaigaiBunriKbn]").change(function() {
		kaigaiDisplaySeigyo();
	});
	$("select[name=kaigaiKariShiireKbn]").change(function() {
		kaigaiDisplaySeigyo();
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
	
	//交通費明細、日当・宿泊費明細 消費税合計額初期表示
	let kazeiKbn = $("select[name=kazeiKbnRyohi] :selected").val();
	let zeiritsu = Number(["001", "002", "011", "012", "013", "014"].includes(kazeiKbn) 
			? $("#zeiritsuRyohi").find("option:selected").val()
			: 0);
	amountKingaku($("#meisaiList01 input[name=shouhizeigakuRyohi]"), $("[name='" + (zeiritsu == "8" ? "8" : "10") + "Percent01']"));
	amountKingaku($("#meisaiList02 input[name=shouhizeigakuRyohi]"), $("[name='" + (zeiritsu == "8" ? "8" : "10") + "Percent02']"));

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

	// 支払日チェック
	var checkShiharaiBi = ($("input[name=shiharaibi]").prop("disabled") == false);

	//支払日更新チェック
	if (checkShiharaiBi) {
		var shiharaiBi = $("input[name=shiharaibi]").val();
		if ("" == shiharaiBi && !($("#shucchouChuushiFlg").prop("checked")) ) {
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
		formObject.action = 'kaigai_ryohi_seisan_touroku';
		break;
	case 'update':
		formObject.action = 'kaigai_ryohi_seisan_koushin';
		break;
	case 'shinsei':
		formObject.action = 'kaigai_ryohi_seisan_shinsei';
		break;
	case 'shounin':
		formObject.action = 'kaigai_ryohi_seisan_shounin';
		break;
	case 'shouninkaijo':
		formObject.action = 'kaigai_ryohi_seisan_shouninkaijo';
		break;
	}

	formObject.method = 'post';
	formObject.target = '_self';
	$(formObject).submit();
}

/**
 * 仮払未使用チェックに連動した表示切替
 * @param isDisp	チェックなしならtrue
 * @param speed		表示時間
 */
function displayRyohiShinsei(isDisp, speed) {

	//ヘッダー部分
	var section = $("#shinseiSection");
	var divCtrlGrpList  = section.find("div.control-group");
	for (var i = 0; i < divCtrlGrpList.length; i++) {
		var divCtrlGrp = divCtrlGrpList.eq(i);

		//チェックOFF→元々非表示の行(never_show)除いて表示
		if (isDisp) {
			if(!(divCtrlGrp.hasClass("never_show"))){
				divCtrlGrp.show(speed);
			}
		//チェックON→hissu-groupの列(仮払選択、支払)以外は非表示
		} else {
			if (!divCtrlGrp.hasClass("hissu-group")) {
				divCtrlGrp.hide(speed);
			}
		}
	}

	//取引列
	var section = $("#ryohiMeisai");
	var divCtrlGrpList  = section.find("div.control-group");
	for (var i = 0; i < divCtrlGrpList.length; i++) {
		var divCtrlGrp = divCtrlGrpList.eq(i);

		//チェックOFF→元々非表示の行(never_show)除いて表示
		if (isDisp) {
			if(!(divCtrlGrp.hasClass("never_show"))){
				divCtrlGrp.show(speed);
			}
		//チェックON→hissu-groupの列(仮払選択、支払)以外は非表示
		} else {
			if (!divCtrlGrp.hasClass("hissu-group")) {
				divCtrlGrp.hide(speed);
			}
		}
	}

	//ヘッダー部(支払列)
	var divShiharai = $("#shiharai");
	var divShiharaiChildren = divShiharai.find(".controls").children();
	for (var i = 0; i < divShiharaiChildren.length; i++) {
		var divShiharaiChild = divShiharaiChildren.eq(i);
		//チェックOFF→元々表示していた項目(never_showなし)を表示
		if (isDisp) {
			if (! divShiharaiChild.hasClass("never_show")) {
				divShiharaiChild.show(0);
			}
			divShiharai.show(speed);
		//チェックON→支払日・計上日以外非表示、支払日・計上日は元々表示していたなら表示、支払日非表示なら支払ラベルごと非表示
		} else {
			if (divShiharaiChild.prop("id") != "shiharaibi" && divShiharaiChild.prop("id") != "keijoubi") {
				divShiharaiChild.hide(0);
			}
			if (divShiharaiChild.prop("id") == "shiharaibi") {
				if (divShiharaiChild.hasClass("never_show") || $("#shucchouChuushiFlg").prop("checked")) {
					divShiharaiChild.hide(0);
					divShiharai.hide(speed);
				}
			}
		}
	}

	//明細部
	//チェックOFF→表示
	if (isDisp) {
		$("#meisaiKaigaiRyohiField").show(speed);
		$("#meisaiRyohiField").show(speed);
		$("#meisaiField").show(speed);
	//チェックON→非表示
	} else {
		$("#meisaiKaigaiRyohiField").hide(speed);
		$("#meisaiRyohiField").hide(speed);
		$("#meisaiField").hide(speed);
	}

	//合計部
	var sectionGoukek = $("#goukeiSection");
	var divCtrls  = sectionGoukek.find("div.invoiceOnly"); // インボイス対応前からあった合計欄は実質不変
	for (var i = 0; i < divCtrls.length; i++) {
		var divCtrls = divCtrls.eq(i);
		//チェックOFF→元々表示していた項目(never_showなし)を表示
		if (isDisp) {
			if (! divCtrls.hasClass("never_show")) {
				divCtrls.show(0);
			}
		//チェックON→hissuが付いていない項目(消費税)を非表示
		} else {
			if (!divCtrls.hasClass("hissu")) {
				divCtrls.hide(speed);
			}
		}
	}
}

/**
 * 取引選択
 *【WARNING】こちら変更した場合は必ずryohiShiwakeEdaNoLostFocus()もメンテすること【WARNING】
 */
function ryohiTorihikiSentaku(){
	var denpyouKbn = $("input[name=denpyouKbn]").val();
	var dairiFlg = $("[name=dairiFlg]").val();
	var userId = "";
	if(denpyouKbn == "A011" && dairiFlg == "1") {
		userId = $("input[name=userIdRyohi]").val();
		if(userId == "") {
			alert("使用者名を入力してください。");
			return;
		}
	}
	setRyohiTorihikiDialogRet();
	var denpyouKbn = "A004";
	dialogCallbackTorihikiSentaku = function() { ryohiShiwakeEdaNoLostFocus() };
	commonTorihikiSentaku(denpyouKbn, $("input[name=kihyouBumonCd]").val(), $("input[name=denpyouId]").val(), $("input[name=daihyouFutanBumonCd]").val(), userId);
};

/**
 * 仕訳枝番号変更時Function
 *【WARNING】こちら変更した場合は必ずryohiTorihikiSentaku()もメンテすること【WARNING】
 */
function ryohiShiwakeEdaNoLostFocus(){
	var denpyouKbn = $("input[name=denpyouKbn]").val();
	var dairiFlg = $("[name=dairiFlg]").val();
	var userId = "";
	if(denpyouKbn == "A011" && dairiFlg == "1") {
		userId = $("input[name=userIdRyohi]").val();
		if(userId == "") {
			alert("使用者名を入力してください。");
			return;
		}
	}
	setRyohiTorihikiDialogRet();
	var title = $("input[name=shiwakeEdaNoRyohi]").parent().parent().find(".control-label").text();
	var denpyouKbn = "A004";
	commonShiwakeEdaNoLostFocus(dialogRetShiwakeEdaNo, dialogRetTorihikiName, title, denpyouKbn, $("input[name=kihyouBumonCd]").val(), $("input[name=denpyouId]").val(), $("input[name=daihyouFutanBumonCd]").val(), userId, $("#denpyouJouhou").find("select[name=invoiceDenpyou] option:selected").val());
	displaySeigyo();
};

/**
 * 取引ダイアログ表示後の設定項目Function
 */
function setRyohiTorihikiDialogRet() {
	dialogRetTorihikiName = $("input[name=torihikiNameRyohi]");
	dialogRetShiwakeEdaNo = $("input[name=shiwakeEdaNoRyohi]");
	dialogRetKamokuCd = $("input[name=kamokuCdRyohi]");
	dialogRetKamokuName = $("input[name=kamokuNameRyohi]");
	dialogRetShoriGroup = $("input[name=shoriGroupRyohi]");
	dialogRetKamokuEdabanCd = $("input[name=kamokuEdabanCdRyohi]");
	dialogRetKamokuEdabanName = $("input[name=kamokuEdabanNameRyohi]");
	dialogRetKamokuEdabanSentakuButton = $("#kamokuEdabanSentakuButton");
	dialogRetFutanBumonCd = $("input[name=futanBumonCdRyohi]");
	dialogRetFutanBumonName = $("input[name=futanBumonNameRyohi]");
	dialogRetFutanBumonSentakuButton = $("#futanBumonSentakuButton");
	dialogRetTorihikisakiCd = $("input[name=torihikisakiCdRyohi]");
	dialogRetTorihikisakiName = $("input[name=torihikisakiNameRyohi]");
	dialogRetFurikomisaki  = $("input[name=furikomisakiJouhouRyohi]");
	dialogRetTorihikisakiSentakuButton = $("#torihikisakiSentakuButton");
	dialogRetTorihikisakiClearButton = $("#torihikisakiClearButton");
	dialogRetProjectCd = $("input[name=projectCdRyohi]");
	dialogRetProjectName = $("input[name=projectNameRyohi]");
	dialogRetProjectSentakuButton = $("#projectSentakuButton");
	dialogRetSegmentCd = $("input[name=segmentCdRyohi]");
	dialogRetSegmentName = $("input[name=segmentNameRyohi]");
	dialogRetSegmentSentakuButton = $("#segmentSentakuButton");
	dialogRetTekiyou = $("input[name=tekiyouRyohi]");
	dialogRetKazeiFlg = $("input[name=kazeiFlgRyohi]");
	dialogRetKazeiFlgKamoku = $("input[name=kazeiFlgRyohiKamoku]");
	dialogRetKazeiKbn = $("select[name=kazeiKbnRyohi]");
	dialogRetBunriKbn = $("select[name=bunriKbn]");
	dialogRetKariShiireKbn = $("select[name=kariShiireKbn]");
<c:choose>
	<c:when test="${'UF1' == shainCdRenkeiArea}">
		dialogRetShainCd = $("input[name=uf1CdRyohi]");
		dialogRetSahinName = $("input[name=uf1NameRyohi]");
	</c:when>
	<c:when test="${'UF2' == shainCdRenkeiArea}">
		dialogRetShainCd = $("input[name=uf2CdRyohi]");
		dialogRetSahinName = $("input[name=uf2NameRyohi]");
	</c:when>
	<c:when test="${'UF3' == shainCdRenkeiArea}">
		dialogRetShainCd = $("input[name=uf3CdRyohi]");
		dialogRetSahinName = $("input[name=uf3NameRyohi]");
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
		dialogRetUf1Cd = $("input[name=uf1CdRyohi]");
		dialogRetUf1Name = $("input[name=uf1NameRyohi]");
		dialogRetUf1SentakuButton = $("#uf1SentakuButton");
	</c:if>
	<c:if test="${'UF2' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf2ShiyouFlg}">
		dialogRetUf2Cd = $("input[name=uf2CdRyohi]");
		dialogRetUf2Name = $("input[name=uf2NameRyohi]");
		dialogRetUf2SentakuButton = $("#uf2SentakuButton");
	</c:if>
	<c:if test="${'UF3' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf3ShiyouFlg}">
		dialogRetUf3Cd = $("input[name=uf3CdRyohi]");
		dialogRetUf3Name = $("input[name=uf3NameRyohi]");
		dialogRetUf3SentakuButton = $("#uf3SentakuButton");
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
	 dialogCallbackCommonSetTorihiki = function() {
		resetKazeiFlg(0);
		calcKazeiMoney();
		displayZeiritsuRyohi();
	}
}

/**
 * 海外取引選択
 *【WARNING】こちら変更した場合は必ずkaigaiShiwakeEdaNoLostFocus()もメンテすること【WARNING】
 */
function kaigaiTorihikiSentaku(){
	var denpyouKbn = $("input[name=denpyouKbn]").val();
	var dairiFlg = $("[name=dairiFlg]").val();
	var userId = "";
	if(denpyouKbn == "A011" && dairiFlg == "1") {
		userId = $("input[name=userIdRyohi]").val();
		if(userId == "") {
			alert("使用者名を入力してください。");
			return;
		}
	}
	setKaigaiRyohiTorihikiDialogRet();
	dialogCallbackTorihikiSentaku = function() { kaigaiShiwakeEdaNoLostFocus() };
	commonTorihikiSentaku($("input[name=denpyouKbn]").val(), $("input[name=kihyouBumonCd]").val(), $("input[name=denpyouId]").val(), $("input[name=daihyouFutanBumonCd]").val(), userId);
};

/**
 * 海外仕訳枝番号変更時Function
 *【WARNING】こちら変更した場合は必ずkaigaiTorihikiSentaku()もメンテすること【WARNING】
 */
function kaigaiShiwakeEdaNoLostFocus(){
	var denpyouKbn = $("input[name=denpyouKbn]").val();
	var dairiFlg = $("[name=dairiFlg]").val();
	var userId = "";
	if(denpyouKbn == "A011" && dairiFlg == "1") {
		userId = $("input[name=userIdRyohi]").val();
		if(userId == "") {
			alert("使用者名を入力してください。");
			return;
		}
	}
	setKaigaiRyohiTorihikiDialogRet();
	var title = $("input[name=kaigaiShiwakeEdaNoRyohi]").parent().parent().find(".control-label").text();
 	commonShiwakeEdaNoLostFocus(dialogRetShiwakeEdaNo, dialogRetTorihikiName, title, $("input[name=denpyouKbn]").val(), $("input[name=kihyouBumonCd]").val(), $("input[name=denpyouId]").val(), $("input[name=daihyouFutanBumonCd]").val(), userId,$("#denpyouJouhou").find("select[name=invoiceDenpyou] option:selected").val());
};

/**
 * 取引(海外)ダイアログ表示後の設定項目Function
 */
function setKaigaiRyohiTorihikiDialogRet() {
	dialogRetTorihikiName = $("input[name=kaigaiTorihikiNameRyohi]");
	dialogRetShiwakeEdaNo = $("input[name=kaigaiShiwakeEdaNoRyohi]");
	dialogRetKamokuCd = $("input[name=kaigaiKamokuCdRyohi]");
	dialogRetKamokuName = $("input[name=kaigaiKamokuNameRyohi]");
	dialogRetShoriGroup = $("input[name=kaigaiShoriGroupRyohi");
	dialogRetKamokuEdabanCd = $("input[name=kaigaiKamokuEdabanCdRyohi]");
	dialogRetKamokuEdabanName = $("input[name=kaigaiKamokuEdabanNameRyohi]");
	dialogRetKamokuEdabanSentakuButton = $("#kaigaiKamokuEdabanSentakuButton");
	dialogRetFutanBumonCd = $("input[name=kaigaiFutanBumonCdRyohi]");
	dialogRetFutanBumonName = $("input[name=kaigaiFutanBumonNameRyohi]");
	dialogRetFutanBumonSentakuButton = $("#kaigaiFutanBumonSentakuButton");
	dialogRetTorihikisakiCd = $("input[name=kaigaiTorihikisakiCdRyohi]");
	dialogRetTorihikisakiName = $("input[name=kaigaiTorihikisakiNameRyohi]");
	dialogRetFurikomisaki  = $("input[name=kaigaiFurikomisakiJouhouRyohi]");
	dialogRetTorihikisakiSentakuButton = $("#kaigaiTorihikisakiSentakuButton");
	dialogRetTorihikisakiClearButton = $("#kaigaiTorihikisakiClearButton");
	dialogRetProjectCd = $("input[name=kaigaiProjectCdRyohi]");
	dialogRetProjectName = $("input[name=kaigaiProjectNameRyohi]");
	dialogRetProjectSentakuButton = $("#kaigaiProjectSentakuButton");
	dialogRetSegmentCd = $("input[name=kaigaiSegmentCdRyohi]");
	dialogRetSegmentName = $("input[name=kaigaiSegmentNameRyohi]");
	dialogRetSegmentSentakuButton = $("#kaigaiSegmentSentakuButton");
	dialogRetTekiyou = $("input[name=kaigaiTekiyouRyohi]");
	dialogRetKazeiFlg = $("input[name=kazeiFlgRyohiKaigai]");
	dialogRetKazeiFlgKamoku = $("input[name=kazeiFlgRyohiKaigaiKamoku]");
	dialogRetKazeiKbn = $("select[name=kaigaiKazeiKbnRyohi]");
	dialogRetBunriKbn = $("select[name=kaigaiBunriKbn]");
	dialogRetKariShiireKbn = $("select[name=kaigaiKariShiireKbn]");
<c:choose>
	<c:when test="${'UF1' == shainCdRenkeiArea}">
	dialogRetShainCd = $("input[name=kaigaiUf1CdRyohi]");
	dialogRetSahinName = $("input[name=kaigaiUf1NameRyohi]");
	</c:when>
	<c:when test="${'UF2' == shainCdRenkeiArea}">
	dialogRetShainCd = $("input[name=kaigaiUf2CdRyohi]");
	dialogRetSahinName = $("input[name=kaigaiUf2NameRyohi]");
	</c:when>
	<c:when test="${'UF3' == shainCdRenkeiArea}">
	dialogRetShainCd = $("input[name=kaigaiUf3CdRyohi]");
	dialogRetSahinName = $("input[name=kaigaiUf3NameRyohi]");
	</c:when>
	<c:when test="${'UF4' == shainCdRenkeiArea}">
	dialogRetShainCd = $("input[name=kaigaiUf4CdRyohi]");
	dialogRetSahinName = $("input[name=kaigaiUf4NameRyohi]");
	</c:when>
	<c:when test="${'UF5' == shainCdRenkeiArea}">
	dialogRetShainCd = $("input[name=kaigaiUf5CdRyohi]");
	dialogRetSahinName = $("input[name=kaigaiUf5NameRyohi]");
	</c:when>
	<c:when test="${'UF6' == shainCdRenkeiArea}">
	dialogRetShainCd = $("input[name=kaigaiUf6CdRyohi]");
	dialogRetSahinName = $("input[name=kaigaiUf6NameRyohi]");
	</c:when>
	<c:when test="${'UF7' == shainCdRenkeiArea}">
	dialogRetShainCd = $("input[name=kaigaiUf7CdRyohi]");
	dialogRetSahinName = $("input[name=kaigaiUf7NameRyohi]");
	</c:when>
	<c:when test="${'UF8' == shainCdRenkeiArea}">
	dialogRetShainCd = $("input[name=kaigaiUf8CdRyohi]");
	dialogRetSahinName = $("input[name=kaigaiUf8NameRyohi]");
	</c:when>
	<c:when test="${'UF9' == shainCdRenkeiArea}">
	dialogRetShainCd = $("input[name=kaigaiUf9CdRyohi]");
	dialogRetSahinName = $("input[name=kaigaiUf9NameRyohi]");
	</c:when>
	<c:when test="${'UF10' == shainCdRenkeiArea}">
	dialogRetShainCd = $("input[name=kaigaiUf10CdRyohi]");
	dialogRetSahinName = $("input[name=kaigaiUf10NameRyohi]");
	</c:when>
</c:choose>
<c:if test="${'UF1' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf1ShiyouFlg}">
dialogRetUf1Cd = $("input[name=kaigaiUf1CdRyohi]");
dialogRetUf1Name = $("input[name=kaigaiUf1NameRyohi]");
dialogRetUf1SentakuButton = $("#kaigaiUf1SentakuButton");
</c:if>
<c:if test="${'UF2' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf2ShiyouFlg}">
dialogRetUf2Cd = $("input[name=kaigaiUf2CdRyohi]");
dialogRetUf2Name = $("input[name=kaigaiUf2NameRyohi]");
dialogRetUf2SentakuButton = $("#kaigaiUf2SentakuButton");
</c:if>
<c:if test="${'UF3' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf3ShiyouFlg}">
dialogRetUf3Cd = $("input[name=kaigaiUf3CdRyohi]");
dialogRetUf3Name = $("input[name=kaigaiUf3NameRyohi]");
dialogRetUf3SentakuButton = $("#kaigaiUf3SentakuButton");
</c:if>
<c:if test="${'UF4' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf4ShiyouFlg}">
dialogRetUf4Cd    = $("input[name=kaigaiUf4CdRyohi]");
dialogRetUf4Name   = $("input[name=kaigaiUf4NameRyohi]");
dialogRetUf4SentakuButton = $("#kaigaiUf4SentakuButton");
</c:if>
<c:if test="${'UF5' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf5ShiyouFlg}">
dialogRetUf5Cd    = $("input[name=kaigaiUf5CdRyohi]");
dialogRetUf5Name   = $("input[name=kaigaiUf5NameRyohi]");
dialogRetUf5SentakuButton = $("#kaigaiUf5SentakuButton");
</c:if>
<c:if test="${'UF6' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf6ShiyouFlg}">
dialogRetUf6Cd    = $("input[name=kaigaiUf6CdRyohi]");
dialogRetUf6Name   = $("input[name=kaigaiUf6NameRyohi]");
dialogRetUf6SentakuButton = $("#kaigaiUf6SentakuButton");
</c:if>
<c:if test="${'UF7' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf7ShiyouFlg}">
dialogRetUf7Cd    = $("input[name=kaigaiUf7CdRyohi]");
dialogRetUf7Name   = $("input[name=kaigaiUf7NameRyohi]");
dialogRetUf7SentakuButton = $("#kaigaiUf7SentakuButton");
</c:if>
<c:if test="${'UF8' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf8ShiyouFlg}">
dialogRetUf8Cd    = $("input[name=kaigaiUf8CdRyohi]");
dialogRetUf8Name   = $("input[name=kaigaiUf8NameRyohi]");
dialogRetUf8SentakuButton = $("#kaigaiUf8SentakuButton");
</c:if>
<c:if test="${'UF9' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf9ShiyouFlg}">
dialogRetUf9Cd    = $("input[name=kaigaiUf9CdRyohi]");
dialogRetUf9Name   = $("input[name=kaigaiUf9NameRyohi]");
dialogRetUf9SentakuButton = $("#kaigaiUf9SentakuButton");
</c:if>
<c:if test="${'UF10' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf10ShiyouFlg}">
dialogRetUf10Cd    = $("input[name=kaigaiUf10CdRyohi]");
dialogRetUf10Name   = $("input[name=kaigaiUf10NameRyohi]");
dialogRetUf10SentakuButton = $("#kaigaiUf10SentakuButton");
</c:if>
	dialogCallbackCommonSetTorihiki = function() {
		resetKazeiFlg(1);
		calcKazeiMoney();
		kaigaiDisplaySeigyo();
	}
}

/**
 * ICカード履歴選択ダイアログを開く
 */
function houjinCardRirekiSentakuDialogOpen() {
	var denpyouId = $("[name=denpyouId]").val();//未登録ならブランク、登録後なら入る
	if($("[name=userIdRyohi]").length > 0 && $("[name=userIdRyohi]").val() == ""){
		alert("使用者を選択してください。");
		return;
	}
	var userId = $("[name=userIdRyohi]").length > 0 ? $("[name=userIdRyohi]").val() : "";//交通費ならnull、国内海外出張なら使用者
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
									let message = "";
									if(!$("input[name=kaigaiShiwakeEdaNoRyohi]").val())
									{
										 $("input[name=sentaku]:checked").each(function(){
											var msShb = $(this).parent().find("select[name=meisaiShubetsu]").val();
											if(msShb %3 != 0 && msShb > 3)
											{
												message = "海外取引を入力してください。";
												return false;
											}
										 });
									}
									if(!$("input[name=shiwakeEdaNoRyohi]").val())
									{
										 $("input[name=sentaku]:checked").each(function(){
											var msShb = $(this).parent().find("select[name=meisaiShubetsu]").val();
											if(msShb %3 != 0 && msShb < 3)
											{
												message += (message != "" ? "\n" : "") + "国内取引を入力してください。";
												return false;
											}
										 });
									}
									
									if(message != "")
									{
										alert(message);
										$(this).dialog("close");
										return;
									}
									
									 $("input[name=sentaku]:checked").each(function(){
										var msShb = $(this).parent().find("select[name=meisaiShubetsu]").val();
										if(msShb == "1"){			//(国内)旅費・交通費
										 	var meisaiInfoRyohi = getHoujinCardRirekiInfoRyohi($(this),"0");
										 	ryohiMeisaiAdd(meisaiInfoRyohi,"1");
										}else if(msShb == "2"){		//(国内)旅費・宿泊費
										 	var meisaiInfoRyohi = getHoujinCardRirekiInfoShukuhakuhi($(this),"0");
										 	ryohiMeisaiAdd(meisaiInfoRyohi,"2");
										}else if(msShb == "3"){		//(国内)その他経費
										 	var meisaiInfoKeihi = getHoujinCardRirekiInfoKeihi($(this),"0");
										 	meisaiAdd(meisaiInfoKeihi);
										}else if(msShb == "4"){		//(海外)旅費・交通費
										 	var meisaiInfoRyohi = getHoujinCardRirekiInfoRyohi($(this),"1");
										 	kaigaiRyohiMeisaiAdd(meisaiInfoRyohi,"1");
										}else if(msShb == "5"){		//(海外)旅費・宿泊費
										 	var meisaiInfoRyohi = getHoujinCardRirekiInfoShukuhakuhi($(this),"1");
										 	kaigaiRyohiMeisaiAdd(meisaiInfoRyohi,"2");
										}else if(msShb == "6"){		//(海外)その他経費
										 	var meisaiInfoKeihi = getHoujinCardRirekiInfoKeihi($(this),"1");
										 	meisaiAdd(meisaiInfoKeihi);
										}
									 });
									 $(this).dialog("close");
								},
						  閉じる:function() {$(this).dialog("close");}}
	})
	.load("houjin_card_siyou_ichiran?denpyouId=" + encodeURI(denpyouId) + "&userId=" + encodeURI(userId) + "&denpyouKbn=" + encodeURI(denpyouKbn));
}


/**
 * 法人カード使用履歴 明細MAP取得(旅費・交通費)
 * @param obj		処理対象オブジェクト
 * @param kgFlg		海外フラグ
 */
function getHoujinCardRirekiInfoRyohi(obj, kgFlg) {

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
		,kaigaiFlgRyohi:				kgFlg
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
 * 法人カード使用履歴 明細MAP取得(旅費・宿泊費)
 * @param obj		処理対象オブジェクト
 * @param kgFlg		海外フラグ
 */
function getHoujinCardRirekiInfoShukuhakuhi(obj, kgFlg) {

	// 一覧の最大インデックスを取得する
	var maxIndex = $("#meisaiList02").find("tr.meisai").length - 1;

	//該当行の明細MAP返す
	return {
		 index:							"1"
		,maxIndex:						maxIndex

		,denpyouId:						$("input[name=denpyouId]").val()
		,denpyouKbn:					$("input[name=denpyouKbn]").val()
		,zeroEnabled:					$("input[name=karibaraiOn]:checked").length == 1 ? $("input[name=karibaraiOn]:checked").val() : "0"
		,dispMode:						('true' == $('#enableInput').val()) ? "2" : "3"

		,kikanTo:						obj.attr("houjin-card-riyoubi")
		,kikanFrom:						obj.attr("houjin-card-riyoubi")
		,kaigaiFlgRyohi:				kgFlg
		,shubetsuCd:					"2"
		,shubetsu1:						""
		,shubetsu2:						""
		,koutsuuShudan:					""
		,shouhyouShoruiHissuFlg:		""
		,ryoushuushoSeikyuushoTouFlg:	$("input[name=ryoushuushoSeikyuushoDefaultRyohi]").val()
		,houjinCardFlgRyohi:			"1"
		,kaishaTehaiFlgRyohi:			"0"
		,naiyou:						""
		,bikou:							""
		,oufukuFlg:						""
		,jidounyuuryokuFlg:				"0"
		,kyuujitsuNissuu:				"0"
		,nissuu:						"1"
		,ninzuu:						""
		,tanka:							obj.attr("kingaku")
		,meisaiKingaku:					obj.attr("kingaku")
		,himodukeCardMeisaiRyohi:		obj.attr("houjin-card-rireki-no")
		,hayaFlg:						"0"
		,yasuFlg:						"0"
		,rakuFlg:						"0"
	};
}

/**
 * 法人カード使用履歴 明細MAP取得(経費)
 * @param obj		処理対象オブジェクト
 * @param kgFlg		海外フラグ
 */
function getHoujinCardRirekiInfoKeihi(obj, kgFlg) {

	// 一覧の最大インデックスを取得する
	var maxIndex = $("#meisaiList").find("tr.meisai").length - 1;

	//該当行の明細MAP返す
	return {
		index:					"1",
		maxIndex:				maxIndex,

		denpyouId:				$("input[name=denpyouId]").val(),
		denpyouKbn:				$("input[name=denpyouKbn]").val(),
		zeroEnabled:			$("input[name=karibaraiOn]:checked").length == 1 ? $("input[name=karibaraiOn]:checked").val() : "0",

		kaigaiFlg:				kgFlg,
		userId:					$("input[name=userIdRyohi]").val(),
		userName:				"",
		shiyoubi:				obj.attr("houjin-card-riyoubi"),
		shouhyouShorui:			$("input[name=ryoushuushoSeikyuushoDefault]").val(),
		shainNo:				"",
		shiwakeEdaNo:			"",
		torihikiName:			"",
		furikomisakiJouhou:		"",
		kamokuCd:				"",
		kamokuName:				"",
		kamokuEdabanCd:			"",
		kamokuEdabanName:		"",
		futanBumonCd:			"",
		futanBumonName:			"",
		torihikisakiCd:			"",
		torihikisakiName:		"",
		projectCd:				"",
		projectName:			"",
		segmentCd:				"",
		segmentName:			"",
		uf1Cd:					"",
		uf1Name:				"",
		uf2Cd:					"",
		uf2Name:				"",
		uf3Cd:					"",
		uf3Name:				"",
		uf4Cd:     "",
		uf4Name:    "",
		uf5Cd:     "",
		uf5Name:    "",
		uf6Cd:     "",
		uf6Name:    "",
		uf7Cd:     "",
		uf7Name:    "",
		uf8Cd:     "",
		uf8Name:    "",
		uf9Cd:     "",
		uf9Name:    "",
		uf10Cd:     "",
		uf10Name:    "",
		kazeiKbn:				"",
		kazeiKbnText:			"",
		kazeiKbnGroup:			"",
		zeiritsu:				$("select[name=zeiritsuRyohi]").val(),
		zeiritsuText:			"",
		zeiritsuGroup:			"",
		heishuCd:				"",
		rate:					"",
		gaika:					"",
		tani:					"",
		shiharaiKingaku:		obj.attr("kingaku"),
		houjinCardFlgKeihi:		"1",
		kaishaTehaiFlgKeihi:	"0",
		tekiyou:				"",
		chuuki2	:				"",
		kousaihiShousai:		"",
		kousaihiHyoujiFlg:		"",
		ninzuuRiyouFlg:		"",
		kazeiFlg:				"",
		enableInput:			'true' == $('#enableInput').val(),
		kamokuEdabanEnable:		"",
		futanBumonEnable:		"",
		torihikisakiEnable:		"",
		kousaihiEnable:			"",
		ninzuuEnable:			"",
		projectEnable:			"",
		segmentEnable:			"",
		hontaiKingaku:			"",
		shouhizeigaku:			"",
		himodukeCardMeisaiKeihi:	obj.attr("houjin-card-rireki-no"),

		//以下計算用
		hontaiKingakuNum:		"",
		shouhizeigakuNum:		""
	};
}

/**
 * 使用者変更時に明細情報をリセット。内部処理はRyohiSeisanCommonScript.jspにて実行。
 */
function changeShiyousha(dialogRetuserId, dialogRetusername, dialogRetshainNo, title, shiyoushaStr){
	changeShiyoushaInternal(dialogRetuserId, dialogRetusername, dialogRetshainNo, title, shiyoushaStr, true);
}

//課税区分・分離区分・仕入区分は取引仕訳登録で入力した値しか認めないため、常にグレーアウト
function kaigaiDisplaySeigyo(){
	setShouhizeiControls($("input[name=denpyouId]").val(), $("input[name=kaigaiShoriGroupRyohi]").val(), $("select[name=kaigaiKazeiKbnRyohi]"), $("select[name=kaigaiZeiritsuDummy]"), $("select[name=kaigaiBunriKbn]"), $("select[name=kaigaiKariShiireKbn]"));
	$("select[name=kaigaiKazeiKbnRyohi]").prop("disabled",true);
	$("select[name=kaigaiBunriKbn]").prop("disabled",true);
	$("select[name=kaigaiKariShiireKbn]").prop("disabled",true);
}
//初期の使用可否
kaigaiDisplaySeigyo();
displayZeiritsuRyohi();

		</script>
	</body>
</html>
