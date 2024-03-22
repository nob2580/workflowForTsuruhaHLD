<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- エラー -->
<%@ include file="/jsp/eteam/include/InputError.jsp" %>

<!-- メイン -->
<div id='dialogMainDenpyouMeisai' class='form-horizontal'>
	<input type='hidden' id="errorCnt" value='${fn:length(errorList)}'>
	<input type="hidden" name='index' value='${su:htmlEscape(index)}'>
	<input type="hidden" name='maxIndex' value='${su:htmlEscape(maxIndex)}'>

	<!-- 入力フィールド -->
	<section>
		<h2 style='line-height: 30px;<c:if test='${null eq index or "" eq index}'> display:none</c:if>'>
			<span>${su:htmlEscape(index + 1)} / ${su:htmlEscape(maxIndex + 1)}
				<button type='button' name='btnPrevious' class='btn' style='position: absolute; right: 90px; font-size: 14px;'><i class='icon-arrow-left'></i> 前へ</button>
				<button type='button' name='btnNext' class='btn' style='position: absolute; right: 10px;  font-size: 14px;'>次へ <i class='icon-arrow-right'></i></button>
			</span>
		</h2>
		<div class="meisai">
			<!-- 取引 -->
			<div class='control-group' <c:if test='${not ks1.torihiki.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.torihiki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.torihiki.name)}</label>
				<div class='controls'>
					<input type='text' name='shiwakeEdaNo' value='${su:htmlEscape(shiwakeEdaNo)}' class='input-small pc_only'>
					<input type='text' name="torihikiName" class='input-xlarge' disabled value='${su:htmlEscape(torihikiName)}'>
					<input type='hidden' name="shiwakeEdaNo" value='${su:htmlEscape(shiwakeEdaNo)}'>
					<button type='button' name='torihikiSentakuButton' class='btn btn-small'>選択</button>
				</div>
			</div>
			<!-- 勘定科目 -->
			<div class='control-group' <c:if test='${not ks1.kamoku.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.kamoku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.kamoku.name)}</label>
				<div class='controls'>
					<input type='text' name='kamokuCd' value='${su:htmlEscape(kamokuCd)}' class='input-small pc_only' disabled>
					<input type='text' name='kamokuName' class='input-xlarge' value='${su:htmlEscape(kamokuName)}' disabled>
					<input type='hidden' name='shoriGroup' value='${su:htmlEscape(shoriGroup)}'>
				</div>
			</div>
			<div class='control-group'<c:if test='${invoiceDenpyou eq 1}'>style='display:none;'</c:if>>
				<!-- 課税区分 -->
				<label class='control-label' <c:if test='${not ks1.kazeiKbn.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks1.kazeiKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.kazeiKbn.name)}</label>
				<div class='controls'>
					<select name='kazeiKbn' class='input-medium' disabled <c:if test='${not ks1.kazeiKbn.hyoujiFlg}'>style='display:none;'</c:if>>
						<c:forEach var="kazeiKbnRecord" items="${kazeiKbnList}">
							<option value='${kazeiKbnRecord.naibuCd}' data-kazeiKbnGroup='${kazeiKbnRecord.option1}' <c:if test='${kazeiKbnRecord.naibuCd eq kazeiKbn}'>selected</c:if> <c:if test='${kazeiKbnRecord.naibuCd eq ""}'>style='display:none;'</c:if>>${su:htmlEscape(kazeiKbnRecord.name)}</option>
						</c:forEach>
					</select>
					<!-- 税率 -->
					<span id='zeiritsuArea' <c:if test='${not ks1.zeiritsu.hyoujiFlg}'>style='display:none;'</c:if>>
					<label class='label'><c:if test='${ks1.zeiritsu.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.zeiritsu.name)}</label>
					<select name='zeiritsu' class='input-small' <c:if test='${not zeiritsuEnable}'>disabled</c:if>>
						<c:forEach var="tmpZeiritsu" items="${zeiritsuList}">
							<option value='${tmpZeiritsu.zeiritsu}' data-hasuuKeisanKbn='${tmpZeiritsu.hasuuKeisanKbn}' data-keigenZeiritsuKbn='${tmpZeiritsu.keigenZeiritsuKbn}' <c:if test='${tmpZeiritsu.zeiritsu eq zeiritsu && tmpZeiritsu.keigenZeiritsuKbn eq keigenZeiritsuKbn}'>selected</c:if>><c:if test='${tmpZeiritsu.keigenZeiritsuKbn eq "1"}'>*</c:if>${tmpZeiritsu.zeiritsu}%</option>
						</c:forEach>
					</select>
					<input type='hidden' name='keigenZeiritsuKbn' value='${su:htmlEscape(keigenZeiritsuKbn)}'>
					</span>
					<!-- 分離区分 -->
					<span <c:if test='${not ks1.bunriKbn.hyoujiFlg}'>style='display:none;'</c:if>>
						<label class='label'><c:if test='${ks1.bunriKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.bunriKbn.name)}</label>
						<select name='bunriKbn' class='input-small' disabled>
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
					<span <c:if test='${ not ks1.shiireKbn.hyoujiFlg}'>style='display:none;'</c:if>>
						<label class='label' for='kariShiireKbn'><c:if test='${ks1.shiireKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.shiireKbn.name)}</label>
						<select name='kariShiireKbn' class='input-small' disabled>
							<c:forEach var="shiireKbnRecord" items="${shiireKbnList}">
								<c:if test='${shiireKbnRecord.naibu_cd eq "0"}'>
									<option hidden value='${shiireKbnRecord.naibuCd}'  <c:if test='${shiireKbnRecord.naibuCd eq kariShiireKbn}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
								</c:if>
								<c:if test='${shiireKbnRecord.naibu_cd ne "0"}'>
									<option value='${shiireKbnRecord.naibuCd}'  <c:if test='${shiireKbnRecord.naibuCd eq kariShiireKbn}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
								</c:if>
							</c:forEach>
						</select>
					</span>
				</div>
			</div>
			<!-- 勘定科目枝番 -->
			<div class='control-group' <c:if test='${not ks1.kamokuEdaban.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.kamokuEdaban.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.kamokuEdaban.name)}</label>
				<div class='controls'>
					<input type='text' name="kamokuEdabanCd" class='input-medium pc_only' value='${su:htmlEscape(kamokuEdabanCd)}' <c:if test='${not kamokuEdabanEnable}'>disabled</c:if>>
					<input type='text' name="kamokuEdabanName" class='input-xlarge' value='${su:htmlEscape(kamokuEdabanName)}' disabled>
					<button type='button' name='kamokuEdabanSentakuButton' class='btn btn-small' <c:if test='${not kamokuEdabanEnable}'>disabled</c:if>>選択</button>
				</div>
			</div>
			<!-- 負担部門 -->
			<div class='control-group' <c:if test='${not ks1.futanBumon.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.futanBumon.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.futanBumon.name)}</label>
				<div class='controls'>
					<input type='text' name="futanBumonCd" class='input-small pc_only' value='${su:htmlEscape(futanBumonCd)}' <c:if test='${not futanBumonEnable}'>disabled</c:if>>
					<input type='text' name="futanBumonName" class='input-xlarge' value='${su:htmlEscape(futanBumonName)}' disabled>
					<button type='button' name='futanBumonSentakuButton' class='btn btn-small' <c:if test='${not futanBumonEnable}'>disabled</c:if>>選択</button>
				</div>
			</div>
			<!-- プロジェクト -->
			<div class='control-group' <c:if test='${not("0" ne pjShiyouFlg and ks1.project.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.project.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.project.name)}</label>
				<div class='controls'>
					<input type='text' name="projectCd" class='pc_only' <c:if test='${not projectEnable}'>disabled</c:if> value='${su:htmlEscape(projectCd[i])}'>
					<input type='text' name="projectName" class='input-xlarge' disabled value='${su:htmlEscape(projectName)}'>
					<button type='button' name='projectSentakuButton' class='btn btn-small' <c:if test='${not projectEnable}'>disabled</c:if>>選択</button>
				</div>
			</div>
			<!-- セグメント -->
			<div class='control-group' <c:if test='${not("0" ne segmentShiyouFlg and ks1.segment.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.segment.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.segment.name)}</label>
				<div class='controls'>
					<input type='text' name="segmentCd" class='pc_only' <c:if test='${not segmentEnable}'>disabled</c:if> value='${su:htmlEscape(segmentCd[i])}'>
					<input type='text' name="segmentName" class='input-xlarge' disabled value='${su:htmlEscape(segmentName)}'>
					<button type='button' name='segmentSentakuButton' class='btn btn-small' <c:if test='${not segmentEnable}'>disabled</c:if>>選択</button>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ("0" ne hfUfSeigyo.uf1ShiyouFlg and ks1.uf1.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.uf1.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf1Name)}</label>
				<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or "1" eq hfUfSeigyo.uf1ShiyouFlg}'>
					<input type='text' name="uf1Cd" maxlength='20' value='${su:htmlEscape(uf1Cd[i])}' <c:if test='${"UF1" eq shainCdRenkeiArea or not uf1Enable}'>disabled</c:if>>
					<input type='hidden' name="uf1Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf1ShiyouFlg or "3" eq hfUfSeigyo.uf1ShiyouFlg}'>
					<input type='text' name="uf1Cd" class='pc_only' <c:if test='${"UF1" eq shainCdRenkeiArea or not uf1Enable}'>disabled</c:if> value='${su:htmlEscape(uf1Cd[i])}'>
					<input type='text' name="uf1Name" class='input-xlarge' disabled value='${su:htmlEscape(uf1Name[i])}' <c:if test='${"UF1" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' name='uf1SentakuButton' class='btn btn-small' <c:if test='${"UF1" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf1Enable}'>disabled</c:if>>選択</button>
					<button type='button' name='uf1ClearButton' class='btn btn-small' <c:if test='${"UF1" eq shainCdRenkeiArea or not uf1Enable}'>style='display:none;'</c:if>>クリア</button>
</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ("0" ne hfUfSeigyo.uf2ShiyouFlg and ks1.uf2.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.uf2.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf2Name)}</label>
				<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or "1" eq hfUfSeigyo.uf2ShiyouFlg}'>
					<input type='text' name="uf2Cd" maxlength='20' value='${su:htmlEscape(uf2Cd[i])}' <c:if test='${"UF2" eq shainCdRenkeiArea or not uf2Enable}'>disabled</c:if>>
					<input type='hidden' name="uf2Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf2ShiyouFlg or "3" eq hfUfSeigyo.uf2ShiyouFlg}'>
					<input type='text' name="uf2Cd" class='pc_only' <c:if test='${"UF2" eq shainCdRenkeiArea or not uf2Enable}'>disabled</c:if> value='${su:htmlEscape(uf2Cd[i])}'>
					<input type='text' name="uf2Name" class='input-xlarge' disabled value='${su:htmlEscape(uf2Name[i])}' <c:if test='${"UF2" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' name='uf2SentakuButton' class='btn btn-small' <c:if test='${"UF2" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf2Enable}'>disabled</c:if>>選択</button>
					<button type='button' name='uf2ClearButton' class='btn btn-small' <c:if test='${"UF2" eq shainCdRenkeiArea or not uf2Enable}'>style='display:none;'</c:if>>クリア</button>
</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ("0" ne hfUfSeigyo.uf3ShiyouFlg and ks1.uf3.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.uf3.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf3Name)}</label>
				<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or "1" eq hfUfSeigyo.uf3ShiyouFlg}'>
					<input type='text' name="uf3Cd" maxlength='20' value='${su:htmlEscape(uf3Cd[i])}' <c:if test='${"UF3" eq shainCdRenkeiArea or not uf3Enable}'>disabled</c:if>>
					<input type='hidden' name="uf3Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf3ShiyouFlg or "3" eq hfUfSeigyo.uf3ShiyouFlg}'>
					<input type='text' name="uf3Cd" class='pc_only' <c:if test='${"UF3" eq shainCdRenkeiArea or not uf3Enable}'>disabled</c:if> value='${su:htmlEscape(uf3Cd[i])}'>
					<input type='text' name="uf3Name" class='input-xlarge' disabled value='${su:htmlEscape(uf3Name[i])}' <c:if test='${"UF3" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' name='uf3SentakuButton' class='btn btn-small' <c:if test='${"UF3" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf3Enable}'>disabled</c:if>>選択</button>
					<button type='button' name='uf3ClearButton' class='btn btn-small' <c:if test='${"UF3" eq shainCdRenkeiArea or not uf3Enable}'>style='display:none;'</c:if>>クリア</button>
</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ("0" ne hfUfSeigyo.uf4ShiyouFlg and ks1.uf4.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.uf4.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf4Name)}</label>
				<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or "1" eq hfUfSeigyo.uf4ShiyouFlg}'>
					<input type='text' name="uf4Cd" maxlength='20' value='${su:htmlEscape(uf4Cd[i])}' <c:if test='${"UF4" eq shainCdRenkeiArea or not uf4Enable}'>disabled</c:if>>
					<input type='hidden' name="uf4Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf4ShiyouFlg or "3" eq hfUfSeigyo.uf4ShiyouFlg}'>
					<input type='text' name="uf4Cd" class='pc_only' <c:if test='${"UF4" eq shainCdRenkeiArea or not uf4Enable}'>disabled</c:if> value='${su:htmlEscape(uf4Cd[i])}'>
					<input type='text' name="uf4Name" class='input-xlarge' disabled value='${su:htmlEscape(uf4Name[i])}' <c:if test='${"UF4" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' name='uf4SentakuButton' class='btn btn-small' <c:if test='${"UF4" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf4Enable}'>disabled</c:if>>選択</button>
					<button type='button' name='uf4ClearButton' class='btn btn-small' <c:if test='${"UF4" eq shainCdRenkeiArea or not uf4Enable}'>style='display:none;'</c:if>>クリア</button>
</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ("0" ne hfUfSeigyo.uf5ShiyouFlg and ks1.uf5.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.uf5.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf5Name)}</label>
				<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or "1" eq hfUfSeigyo.uf5ShiyouFlg}'>
					<input type='text' name="uf5Cd" maxlength='20' value='${su:htmlEscape(uf5Cd[i])}' <c:if test='${"UF5" eq shainCdRenkeiArea or not uf5Enable}'>disabled</c:if>>
					<input type='hidden' name="uf5Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf5ShiyouFlg or "3" eq hfUfSeigyo.uf5ShiyouFlg}'>
					<input type='text' name="uf5Cd" class='pc_only' <c:if test='${"UF5" eq shainCdRenkeiArea or not uf5Enable}'>disabled</c:if> value='${su:htmlEscape(uf5Cd[i])}'>
					<input type='text' name="uf5Name" class='input-xlarge' disabled value='${su:htmlEscape(uf5Name[i])}' <c:if test='${"UF5" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' name='uf5SentakuButton' class='btn btn-small' <c:if test='${"UF5" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf5Enable}'>disabled</c:if>>選択</button>
					<button type='button' name='uf5ClearButton' class='btn btn-small' <c:if test='${"UF5" eq shainCdRenkeiArea or not uf5Enable}'>style='display:none;'</c:if>>クリア</button>
</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ("0" ne hfUfSeigyo.uf6ShiyouFlg and ks1.uf6.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.uf6.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf6Name)}</label>
				<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or "1" eq hfUfSeigyo.uf6ShiyouFlg}'>
					<input type='text' name="uf6Cd" maxlength='20' value='${su:htmlEscape(uf6Cd[i])}' <c:if test='${"UF6" eq shainCdRenkeiArea or not uf6Enable}'>disabled</c:if>>
					<input type='hidden' name="uf6Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf6ShiyouFlg or "3" eq hfUfSeigyo.uf6ShiyouFlg}'>
					<input type='text' name="uf6Cd" class='pc_only' <c:if test='${"UF6" eq shainCdRenkeiArea or not uf6Enable}'>disabled</c:if> value='${su:htmlEscape(uf6Cd[i])}'>
					<input type='text' name="uf6Name" class='input-xlarge' disabled value='${su:htmlEscape(uf6Name[i])}' <c:if test='${"UF6" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' name='uf6SentakuButton' class='btn btn-small' <c:if test='${"UF6" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf6Enable}'>disabled</c:if>>選択</button>
					<button type='button' name='uf6ClearButton' class='btn btn-small' <c:if test='${"UF6" eq shainCdRenkeiArea or not uf6Enable}'>style='display:none;'</c:if>>クリア</button>
</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ("0" ne hfUfSeigyo.uf7ShiyouFlg and ks1.uf7.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.uf7.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf7Name)}</label>
				<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or "1" eq hfUfSeigyo.uf7ShiyouFlg}'>
					<input type='text' name="uf7Cd" maxlength='20' value='${su:htmlEscape(uf7Cd[i])}' <c:if test='${"UF7" eq shainCdRenkeiArea or not uf7Enable}'>disabled</c:if>>
					<input type='hidden' name="uf7Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf7ShiyouFlg or "3" eq hfUfSeigyo.uf7ShiyouFlg}'>
					<input type='text' name="uf7Cd" class='pc_only' <c:if test='${"UF7" eq shainCdRenkeiArea or not uf7Enable}'>disabled</c:if> value='${su:htmlEscape(uf7Cd[i])}'>
					<input type='text' name="uf7Name" class='input-xlarge' disabled value='${su:htmlEscape(uf7Name[i])}' <c:if test='${"UF7" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' name='uf7SentakuButton' class='btn btn-small' <c:if test='${"UF7" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf7Enable}'>disabled</c:if>>選択</button>
					<button type='button' name='uf7ClearButton' class='btn btn-small' <c:if test='${"UF7" eq shainCdRenkeiArea or not uf7Enable}'>style='display:none;'</c:if>>クリア</button>
</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ("0" ne hfUfSeigyo.uf8ShiyouFlg and ks1.uf8.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.uf8.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf8Name)}</label>
				<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or "1" eq hfUfSeigyo.uf8ShiyouFlg}'>
					<input type='text' name="uf8Cd" maxlength='20' value='${su:htmlEscape(uf8Cd[i])}' <c:if test='${"UF8" eq shainCdRenkeiArea or not uf8Enable}'>disabled</c:if>>
					<input type='hidden' name="uf8Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf8ShiyouFlg or "3" eq hfUfSeigyo.uf8ShiyouFlg}'>
					<input type='text' name="uf8Cd" class='pc_only' <c:if test='${"UF8" eq shainCdRenkeiArea or not uf8Enable}'>disabled</c:if> value='${su:htmlEscape(uf8Cd[i])}'>
					<input type='text' name="uf8Name" class='input-xlarge' disabled value='${su:htmlEscape(uf8Name[i])}' <c:if test='${"UF8" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' name='uf8SentakuButton' class='btn btn-small' <c:if test='${"UF8" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf8Enable}'>disabled</c:if>>選択</button>
					<button type='button' name='uf8ClearButton' class='btn btn-small' <c:if test='${"UF8" eq shainCdRenkeiArea or not uf8Enable}'>style='display:none;'</c:if>>クリア</button>
</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ("0" ne hfUfSeigyo.uf9ShiyouFlg and ks1.uf9.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.uf9.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf9Name)}</label>
				<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or "1" eq hfUfSeigyo.uf9ShiyouFlg}'>
					<input type='text' name="uf9Cd" maxlength='20' value='${su:htmlEscape(uf9Cd[i])}' <c:if test='${"UF9" eq shainCdRenkeiArea or not uf9Enable}'>disabled</c:if>>
					<input type='hidden' name="uf9Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf9ShiyouFlg or "3" eq hfUfSeigyo.uf9ShiyouFlg}'>
					<input type='text' name="uf9Cd" class='pc_only' <c:if test='${"UF9" eq shainCdRenkeiArea or not uf9Enable}'>disabled</c:if> value='${su:htmlEscape(uf9Cd[i])}'>
					<input type='text' name="uf9Name" class='input-xlarge' disabled value='${su:htmlEscape(uf9Name[i])}' <c:if test='${"UF9" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' name='uf9SentakuButton' class='btn btn-small' <c:if test='${"UF9" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf9Enable}'>disabled</c:if>>選択</button>
					<button type='button' name='uf9ClearButton' class='btn btn-small' <c:if test='${"UF9" eq shainCdRenkeiArea or not uf9Enable}'>style='display:none;'</c:if>>クリア</button>
</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ("0" ne hfUfSeigyo.uf10ShiyouFlg and ks1.uf10.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.uf10.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf10Name)}</label>
				<div class='controls'>
<c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or "1" eq hfUfSeigyo.uf10ShiyouFlg}'>
					<input type='text' name="uf10Cd" maxlength='20' value='${su:htmlEscape(uf10Cd[i])}' <c:if test='${"UF10" eq shainCdRenkeiArea or not uf10Enable}'>disabled</c:if>>
					<input type='hidden' name="uf10Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf10ShiyouFlg or "3" eq hfUfSeigyo.uf10ShiyouFlg}'>
					<input type='text' name="uf10Cd" class='pc_only' <c:if test='${"UF10" eq shainCdRenkeiArea or not uf10Enable}'>disabled</c:if> value='${su:htmlEscape(uf10Cd[i])}'>
					<input type='text' name="uf10Name" class='input-xlarge' disabled value='${su:htmlEscape(uf10Name[i])}' <c:if test='${"UF10" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
					<button type='button' name='uf10SentakuButton' class='btn btn-small' <c:if test='${"UF10" eq shainCdRenkeiArea}'>style='display:none;'</c:if> <c:if test='${not uf10Enable}'>disabled</c:if>>選択</button>
					<button type='button' name='uf10ClearButton' class='btn btn-small' <c:if test='${"UF10" eq shainCdRenkeiArea or not uf10Enable}'>style='display:none;'</c:if>>クリア</button>
</c:if>
				</div>
			</div>
			<!-- 支払金額 -->
			<div class='control-group'>
				<label class='control-label'><span class='required'>*</span>${su:htmlEscape(ks1.shiharaiKingaku.name)}</label>
				<div class='controls'>
					<input type='text' name='shiharaiKingaku' class='input-medium autoNumericWithCalcBox' value='${su:htmlEscape(shiharaiKingaku)}'>円
				</div>
			</div>	
			<!-- 税抜金額 （税抜3伝票） -->
			<div class='control-group invoiceOnly' <c:if test='${invoiceDenpyou eq "1" or not ks1.zeinukiKingaku.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.zeinukiKingaku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.zeinukiKingaku.name)}</label>
				<div class='controls'>
					<input type='text' name='zeinukiKingaku' class='input-medium autoNumericWithCalcBox' <c:if test='${!(empty himodukeCardMeisaiKeihi)}'> disabled </c:if> value='${su:htmlEscape(zeinukiKingaku)}'>円
				</div>
			</div>
			<!-- うち消費税額（経費精算系） -->
			<!-- 消費税額 （税抜3伝票） -->
			<div class='control-group invoiceOnly'<c:if test='${invoiceDenpyou eq "1" or not ks1.shouhizeigaku.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.shouhizeigaku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.shouhizeigaku.name)}</label>
				<div class='controls'>
					<input type='text' name='shouhizeigaku' id='shouhizeigaku' class='input-medium autoNumericWithCalcBox' value='${su:htmlEscape(shouhizeigaku)}'>円
					<button type='button' name='zeiShuseiBtn' id='zeiShuseiBtn' class='btn btn-small'>修正</button>
				</div>
			</div><!-- 摘要 -->
			<div class='control-group' <c:if test='${not ks1.tekiyou.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.tekiyou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.tekiyou.name)}</label>
				<div class='controls'>
					<input type='text' name='tekiyou' class='input-block-level' value='${su:htmlEscape(tekiyou)}'>
				</div>
			</div>
			<input type='hidden' name='denpyouKbn'			value='' >
			<input type='hidden' name='keshikomiFlg'		value='' >
		</div>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
/**
 * ダイアログ明細情報取得
 */
function getDialogMeisaiInfo() {
	var meisai = $("#dialogMainDenpyouMeisai");
	
	return {
		index:					$("input[name=index]").val(),
		maxIndex:				$("input[name=maxIndex]").val(),
		denpyouKbn:				meisai.find("input[name=denpyouKbn]").val(),
		shiwakeEdaNo:			meisai.find("input[name=shiwakeEdaNo]").val(),
		torihikiName:			meisai.find("input[name=torihikiName]").val(),
		kamokuCd:				meisai.find("input[name=kamokuCd]").val(),
		kamokuName:				meisai.find("input[name=kamokuName]").val(),
		kamokuEdabanCd:			meisai.find("input[name=kamokuEdabanCd]").val(),
		kamokuEdabanName:		meisai.find("input[name=kamokuEdabanName]").val(),
		futanBumonCd:			meisai.find("input[name=futanBumonCd]").val(),
		futanBumonName:			meisai.find("input[name=futanBumonName]").val(),
		projectCd:				meisai.find("input[name=projectCd]").val(),
		projectName:			meisai.find("input[name=projectName]").val(),
		segmentCd:				meisai.find("input[name=segmentCd]").val(),
		segmentName:			meisai.find("input[name=segmentName]").val(),
		uf1Cd:					meisai.find("input[name=uf1Cd]").val(),
		uf1Name:				meisai.find("input[name=uf1Name]").val(),
		uf2Cd:					meisai.find("input[name=uf2Cd]").val(),
		uf2Name:				meisai.find("input[name=uf2Name]").val(),
		uf3Cd:					meisai.find("input[name=uf3Cd]").val(),
		uf3Name:				meisai.find("input[name=uf3Name]").val(),
		uf4Cd:					meisai.find("input[name=uf4Cd]").val(),
		uf4Name:				meisai.find("input[name=uf4Name]").val(),
		uf5Cd:					meisai.find("input[name=uf5Cd]").val(),
		uf5Name:				meisai.find("input[name=uf5Name]").val(),
		uf6Cd:					meisai.find("input[name=uf6Cd]").val(),
		uf6Name:				meisai.find("input[name=uf6Name]").val(),
		uf7Cd:					meisai.find("input[name=uf7Cd]").val(),
		uf7Name:				meisai.find("input[name=uf7Name]").val(),
		uf8Cd:					meisai.find("input[name=uf8Cd]").val(),
		uf8Name:				meisai.find("input[name=uf8Name]").val(),
		uf9Cd:					meisai.find("input[name=uf9Cd]").val(),
		uf9Name:				meisai.find("input[name=uf9Name]").val(),
		uf10Cd:					meisai.find("input[name=uf10Cd]").val(),
		uf10Name:				meisai.find("input[name=uf10Name]").val(),
		kazeiKbn:				meisai.find("select[name=kazeiKbn] :selected").val(),
		kazeiKbnText:			meisai.find("select[name=kazeiKbn] :selected").text(),
		kazeiKbnGroup:			meisai.find("select[name=kazeiKbn] :selected").attr("data-kazeiKbnGroup"),
		zeiritsu:				meisai.find("select[name=zeiritsu] :selected").val(),
		zeiritsuText:			meisai.find("select[name=zeiritsu] :selected").text(),
		zeiritsuGroup:			meisai.find("select[name=zeiritsu] :selected").attr("data-hasuuKeisanKbn"),
		keigenZeiritsuKbn:		meisai.find("select[name=zeiritsu] :selected").attr("data-keigenZeiritsuKbn"),
		shiharaiKingaku:		meisai.find("input[name=shiharaiKingaku]").val(),
		tekiyou:				meisai.find("input[name=tekiyou]").val(),
		bunriKbn:				meisai.find("select[name=bunriKbn] :selected").val(),
		kariShiireKbn:			meisai.find("select[name=kariShiireKbn] :selected").val(),
		zeinukiKingaku:			meisai.find("input[name=zeinukiKingaku]").val(),
		shouhizeigaku:			meisai.find("input[name=shouhizeigaku]").val(),
		jigyoushaKbn : 			$("#kaikeiContent").find("input[name=jigyoushaKbn]:checked").val() == "1" ? "1": "0",
		jigyoushaKbnText:		$("#kaikeiContent").find("input[name=jigyoushaKbn] :checked").text(),
		
		enableInput:			'true',
		kamokuEdabanEnable:		!meisai.find("button[name=kamokuEdabanSentakuButton]").is(":disabled"),
		futanBumonEnable:		!meisai.find("button[name=futanBumonSentakuButton]").is(":disabled"),
		projectEnable:			!meisai.find("button[name=projectSentakuButton]").is(":disabled"),
		segmentEnable:			!meisai.find("button[name=segmentSentakuButton]").is(":disabled"),
		zeiritsuEnable:			!meisai.find("select[name=zeiritsu]").is(":disabled")
	};
}

/**
 * ダイアログ明細情報設定
 * ダイアログを開く時(明細追加、既存明細の変更・参照、明細の前次移動)にこのfunctionを通ることで、
 * 一覧の情報をダイアログに反映する。
 * @param isEnabled		更新モードか(追加・変更ならtrue、参照ならfalse)
 * @param meisaiInfo	明細情報(追加・変更なら明細情報の連想配列、参照ならnull)
 */
function setDialogMeisaiInfo(isEnable, meisaiInfo) {
	var meisai = $("#dialogMainDenpyouMeisai");

	if (isEnable) {
		//イベント設定
		meisaiActionAdd(meisai);
		
		//伝票区分をセット
		meisai.find("input[name=denpyouKbn]").val($("input[name=denpyouKbn]").val());
		if (false == !meisaiInfo) {
			meisai.find("button[name=kamokuEdabanSentakuButton]").prop("disabled", !meisaiInfo["kamokuEdabanEnable"]);
			meisai.find("button[name=futanBumonSentakuButton]").prop("disabled", !meisaiInfo["futanBumonEnable"]);
			meisai.find("button[name=projectSentakuButton]").prop("disabled", !meisaiInfo["projectEnable"]);
			meisai.find("select[name=zeiritsu]").prop("disabled", !meisaiInfo["zeiritsuEnable"]);
		}

		//税抜3伝票　税込税抜入力フラグ
		var nyuryokuFlg = $("#kaikeiContent").find("select[name=nyuryokuHoushiki] :selected").val();
		meisai.find("input[name=shiharaiKingaku]").prop("disabled", nyuryokuFlg == "1");
		meisai.find("input[name=zeinukiKingaku]").prop("disabled", nyuryokuFlg == "0");
		if($("#denpyouJouhou").find("select[name=invoiceDenpyou]").val() == "1"){
			meisai.find("input[name=shiharaiKingaku]").prop("disabled", false);
		}
	} else {
		meisai.find('button,input[type!=hidden],select,textarea').prop("disabled", true);
		meisai.find('.autoNumericWithCalcBox').removeClass('autoNumericWithCalcBox').addClass('autoNumeric');
	}
	//摘要文字数制限取得
	meisai.find("input[name=tekiyou]").attr('maxlength', $("#workflowForm").find("input[name=tekiyouMaxLength]").val());
	
	// 次へ・前へボタン押下イベント付与：click(data,fn)
	$("button[name=btnPrevious]").removeAttr("disabled");
	$("button[name=btnNext]").removeAttr("disabled");
	meisai.find("button[name=btnPrevious]").click(meisaiInfo, meisaiPrevious);
	meisai.find("button[name=btnNext]").click(meisaiInfo, meisaiNext);

	var displayStyle = $("#denpyouJouhou").find("select[name=invoiceDenpyou]").val() == '1' ? 'hide' : 'show';
	$(".invoiceOnly")[displayStyle]();
	
	//入力補助
	commonInit(meisai);
	if(isEnable){
		//税額修正ボタンの表示制御(commonInitで電卓追加されるためここで呼び出す)
		zeiShuseiBtnDisplay();
	}
	
	// フォーカス
	meisai.find('input:visible').first().focus();
	
	bunriShiireSeigyo();

	//新規追加(空の表示)なら終わり
	if (!meisaiInfo) {
		return ;
	}
	//以下、新規追加以外で表示内容が空でない時
	
	//明細情報をダイアログに反映
	meisai.find("input[name=shiwakeEdaNo]").val(meisaiInfo["shiwakeEdaNo"]);
	meisai.find("input[name=torihikiName]").val(meisaiInfo["torihikiName"]);
	meisai.find("input[name=sessionUserId]").val(meisaiInfo["sessionUserId"]);
	meisai.find("input[name=kamokuCd]").val(meisaiInfo["kamokuCd"]);
	meisai.find("input[name=kamokuName]").val(meisaiInfo["kamokuName"]);
	meisai.find("input[name=kamokuEdabanCd]").val(meisaiInfo["kamokuEdabanCd"]);
	meisai.find("input[name=kamokuEdabanName]").val(meisaiInfo["kamokuEdabanName"]);
	meisai.find("input[name=futanBumonCd]").val(meisaiInfo["futanBumonCd"]);
	meisai.find("input[name=futanBumonName]").val(meisaiInfo["futanBumonName"]);
	meisai.find("input[name=projectCd]").val(meisaiInfo["projectCd"]);
	meisai.find("input[name=projectName]").val(meisaiInfo["projectName"]);
	meisai.find("input[name=segmentCd]").val(meisaiInfo["segmentCd"]);
	meisai.find("input[name=segmentName]").val(meisaiInfo["segmentName"]);
	for(let i = 1; i <= 10; i++)
	{
		meisai.find("input[name=uf" + i + "Cd]").val(meisaiInfo["uf" + i + "Cd"]);
		meisai.find("input[name=uf" + i + "Name]").val(meisaiInfo["uf" + i + "Name"]);
	}
	meisai.find("select[name=kazeiKbn]").val(meisaiInfo["kazeiKbn"]);
	meisai.find("select[name=zeiritsu]").val(meisaiInfo["zeiritsu"]);
	meisai.find("input[name=keigenZeiritsuKbn]").val(meisaiInfo["keigenZeiritsuKbn"]);
	meisai.find("input[name=shiharaiKingaku]").val(meisaiInfo["shiharaiKingaku"]);
	meisai.find("input[name=tekiyou]").val(meisaiInfo["tekiyou"]);
	meisai.find("select[name=bunriKbn]").val(meisaiInfo["bunriKbn"]);
	meisai.find("select[name=kariShiireKbn]").val(meisaiInfo["kariShiireKbn"]);
	meisai.find("input[name=zeinukiKingaku]").val(meisaiInfo["zeinukiKingaku"]);
	meisai.find("input[name=shouhizeigaku]").val(meisaiInfo["shouhizeigaku"]);
	meisai.find("input[name=shiharaisakiName]").val(meisaiInfo["shiharaisaki"]);
	meisai.find("input[name=jigyoushaNo]").val(meisaiInfo["jigyoushaNo"]);

	//課税区分による税率表示有無
	displayZeiritsu();
	//消費税率プルダウンの選択状態を変更
	reselectZeiritsu();
}

//元画面から移植

/**
 * 取引選択ボタン押下時Function
 */
 function torihikiSentaku() {
	setTorihikiDialogRet();
	dialogCallbackTorihikiSentaku = function() { shiwakeEdaNoLostFocus() };
	commonTorihikiSentaku("A013", $("input[name=kihyouBumonCd]").val(), $("input[name=denpyouId]").val(), $("input[name=daihyouFutanBumonCd]").val(), "");
}

 /**
  * 仕訳枝番号ロストフォーカス時Function
  */
 function shiwakeEdaNoLostFocus() {
	setTorihikiDialogRet();
	var title = $(this).parent().parent().find(".control-label").text();
	commonShiwakeEdaNoLostFocus(dialogRetShiwakeEdaNo, dialogRetTorihikiName, title, "A013", $("input[name=kihyouBumonCd]").val(), $("input[name=denpyouId]").val(), $("input[name=daihyouFutanBumonCd]").val(), "", $("#denpyouJouhou").find("select[name=invoiceDenpyou] option:selected").val());
 }

 /**
  * 取引ダイアログ表示後の設定項目Function
  */
 function setTorihikiDialogRet() {
		var meisai = $("#dialogMainDenpyouMeisai").find("div.meisai");
	
	// 明細部
	dialogRetTorihikiName				= meisai.find("input[name=torihikiName]");
	dialogRetShiwakeEdaNo				= meisai.find("input[name=shiwakeEdaNo]");
	dialogRetKamokuCd					= meisai.find("input[name=kamokuCd]");
	dialogRetKamokuName					= meisai.find("input[name=kamokuName]");
	dialogRetKamokuEdabanCd				= meisai.find("input[name=kamokuEdabanCd]");
	dialogRetKamokuEdabanName			= meisai.find("input[name=kamokuEdabanName]");
	dialogRetKamokuEdabanSentakuButton	= meisai.find("button[name=kamokuEdabanSentakuButton]");
	dialogRetFutanBumonCd				= meisai.find("input[name=futanBumonCd]");
	dialogRetFutanBumonName				= meisai.find("input[name=futanBumonName]");
	dialogRetFutanBumonSentakuButton	= meisai.find("button[name=futanBumonSentakuButton]");
	dialogRetTorihikisakiCd				= null;
	dialogRetTorihikisakiName			= null;
	dialogRetProjectCd					= meisai.find("input[name=projectCd]");
	dialogRetProjectName				= meisai.find("input[name=projectName]");
	dialogRetProjectSentakuButton		= meisai.find("button[name=projectSentakuButton]");
	dialogRetSegmentCd					= meisai.find("input[name=segmentCd]");
	dialogRetSegmentName				= meisai.find("input[name=segmentName]");
	dialogRetSegmentSentakuButton		= meisai.find("button[name=segmentSentakuButton]");
	dialogRetKazeiKbn					= meisai.find("select[name=kazeiKbn]");
	dialogRetZeiritsu					= meisai.find("select[name=zeiritsu]");
	dialogRetKeigenZeiritsuKbn			= meisai.find("input[name=keigenZeiritsuKbn]");
	dialogRetJigyoushaKbn				= null;
	dialogRetBunriKbn					= meisai.find("select[name=bunriKbn]");
	dialogRetKariShiireKbn					= meisai.find("select[name=kariShiireKbn]");
	dialogRetTekiyou					= meisai.find("input[name=tekiyou]");
	dialogRetShoriGroup					= meisai.find("input[name=shoriGroup]");
	<c:choose>
	<c:when test="${'UF1' == shainCdRenkeiArea}">
		dialogRetShainCd	= meisai.find("input[name=uf1Cd]");
		dialogRetSahinName	= meisai.find("input[name=uf1Name]");
	</c:when>
	<c:when test="${'UF2' == shainCdRenkeiArea}">
		dialogRetShainCd	= meisai.find("input[name=uf2Cd]");
		dialogRetSahinName	= meisai.find("input[name=uf2Name]");
	</c:when>
	<c:when test="${'UF3' == shainCdRenkeiArea}">
		dialogRetShainCd	= meisai.find("input[name=uf3Cd]");
		dialogRetSahinName	= meisai.find("input[name=uf3Name]");
	</c:when>
	<c:when test="${'UF4' == shainCdRenkeiArea}">
		dialogRetShainCd	= meisai.find("input[name=uf4Cd]");
		dialogRetSahinName	= meisai.find("input[name=uf4Name]");
	</c:when>
	<c:when test="${'UF5' == shainCdRenkeiArea}">
		dialogRetShainCd	= meisai.find("input[name=uf5Cd]");
		dialogRetSahinName	= meisai.find("input[name=uf5Name]");
	</c:when>
	<c:when test="${'UF6' == shainCdRenkeiArea}">
		dialogRetShainCd	= meisai.find("input[name=uf6Cd]");
		dialogRetSahinName	= meisai.find("input[name=uf6Name]");
	</c:when>
	<c:when test="${'UF7' == shainCdRenkeiArea}">
		dialogRetShainCd	= meisai.find("input[name=uf7Cd]");
		dialogRetSahinName	= meisai.find("input[name=uf7Name]");
	</c:when>
	<c:when test="${'UF8' == shainCdRenkeiArea}">
		dialogRetShainCd	= meisai.find("input[name=uf8Cd]");
		dialogRetSahinName	= meisai.find("input[name=uf8Name]");
	</c:when>
	<c:when test="${'UF9' == shainCdRenkeiArea}">
		dialogRetShainCd	= meisai.find("input[name=uf9Cd]");
		dialogRetSahinName	= meisai.find("input[name=uf9Name]");
	</c:when>
	<c:when test="${'UF10' == shainCdRenkeiArea}">
		dialogRetShainCd	= meisai.find("input[name=uf10Cd]");
		dialogRetSahinName	= meisai.find("input[name=uf10Name]");
	</c:when>
</c:choose>

	<c:if test="${'UF1' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf1ShiyouFlg}">
		dialogRetUf1Cd				= meisai.find("input[name=uf1Cd]");
		dialogRetUf1Name			= meisai.find("input[name=uf1Name]");
		dialogRetUf1SentakuButton	= meisai.find("button[name=uf1SentakuButton]");
	</c:if>
	<c:if test="${'UF2' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf2ShiyouFlg}">
		dialogRetUf2Cd				= meisai.find("input[name=uf2Cd]");
		dialogRetUf2Name			= meisai.find("input[name=uf2Name]");
		dialogRetUf2SentakuButton	= meisai.find("button[name=uf2SentakuButton]");
	</c:if>
	<c:if test="${'UF3' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf3ShiyouFlg}">
		dialogRetUf3Cd				= meisai.find("input[name=uf3Cd]");
		dialogRetUf3Name			= meisai.find("input[name=uf3Name]");
		dialogRetUf3SentakuButton	= meisai.find("button[name=uf3SentakuButton]");
	</c:if>
	<c:if test="${'UF4' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf4ShiyouFlg}">
		dialogRetUf4Cd				= meisai.find("input[name=uf4Cd]");
		dialogRetUf4Name			= meisai.find("input[name=uf4Name]");
		dialogRetUf4SentakuButton	= meisai.find("button[name=uf4SentakuButton]");
	</c:if>
	<c:if test="${'UF5' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf5ShiyouFlg}">
		dialogRetUf5Cd				= meisai.find("input[name=uf5Cd]");
		dialogRetUf5Name			= meisai.find("input[name=uf5Name]");
		dialogRetUf5SentakuButton	= meisai.find("button[name=uf5SentakuButton]");
	</c:if>
	<c:if test="${'UF6' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf6ShiyouFlg}">
		dialogRetUf6Cd				= meisai.find("input[name=uf6Cd]");
		dialogRetUf6Name			= meisai.find("input[name=uf6Name]");
		dialogRetUf6SentakuButton	= meisai.find("button[name=uf6SentakuButton]");
	</c:if>
	<c:if test="${'UF7' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf7ShiyouFlg}">
		dialogRetUf7Cd				= meisai.find("input[name=uf7Cd]");
		dialogRetUf7Name			= meisai.find("input[name=uf7Name]");
		dialogRetUf7SentakuButton	= meisai.find("button[name=uf7SentakuButton]");
	</c:if>
	<c:if test="${'UF8' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf8ShiyouFlg}">
		dialogRetUf8Cd				= meisai.find("input[name=uf8Cd]");
		dialogRetUf8Name			= meisai.find("input[name=uf8Name]");
		dialogRetUf8SentakuButton	= meisai.find("button[name=uf8SentakuButton]");
	</c:if>
	<c:if test="${'UF9' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf9ShiyouFlg}">
		dialogRetUf9Cd				= meisai.find("input[name=uf9Cd]");
		dialogRetUf9Name			= meisai.find("input[name=uf9Name]");
		dialogRetUf9SentakuButton	= meisai.find("button[name=uf9SentakuButton]");
	</c:if>
	<c:if test="${'UF10' ne shainCdRenkeiArea and '0' ne hfUfSeigyo.uf10ShiyouFlg}">
		dialogRetUf10Cd				= meisai.find("input[name=uf10Cd]");
		dialogRetUf10Name			= meisai.find("input[name=uf10Name]");
		dialogRetUf10SentakuButton	= meisai.find("button[name=uf10SentakuButton]");
	</c:if>

	dialogCallbackCommonSetTorihiki = function() {
		reselectZeiritsu();
		displayZeiritsu();
		zeiShuseiBtnDisplay();
		bunriShiireSeigyo();
		if(meisai.find("input[name=shiwakeEdaNo]").val() == ""){
			meisai.find("input[name=shiharaiKingaku]").val("");
			meisai.find("input[name=zeinukiKingaku]").val("");
			meisai.find("input[name=shouhizeigaku]").val("");
		}
		kingakuSaikeisan();
	}
}
 
/**
 * 勘定科目枝番選択ボタン押下時Function
 */
 function kamokuEdabanSentaku() {
	var meisai = $("#dialogMainDenpyouMeisai").find("div.meisai");
	dialogRetKamokuEdabanCd = meisai.find("input[name=kamokuEdabanCd]");
	dialogRetKamokuEdabanName = meisai.find("input[name=kamokuEdabanName]");
	dialogRetKazeiKbn = meisai.find("select[name=kazeiKbn]");
	dialogRetBunriKbn = meisai.find("select[name=bunriKbn]");
	dialogCallbackKanjyouKamokuEdabanSentaku = function(){
		displayZeiritsu();
		zeiShuseiBtnDisplay();
		bunriShiireSeigyo();
		kingakuSaikeisan();
		futanBumonCdLostFocus();
	};
	commonKamokuEdabanSentaku(meisai.find("[name=kamokuCd]").val(), $("[name=denpyouKbn]").val(), meisai.find("input[name=shiwakeEdaNo]").val());
}

/**
 * 勘定科目枝番コードロストフォーカス時Function
 */
function kamokuEdabanCdLostFocus() {
	var meisai = $("#dialogMainDenpyouMeisai").find("div.meisai");
	var title = $(this).parent().parent().find(".control-label").text();
	var kamokuCd = 	meisai.find("input[name=kamokuCd]").val();
	dialogRetKamokuEdabanCd = meisai.find("input[name=kamokuEdabanCd]");
	dialogRetKamokuEdabanName = meisai.find("input[name=kamokuEdabanName]");
	dialogRetKazeiKbn = meisai.find("select[name=kazeiKbn]");
	dialogRetBunriKbn = meisai.find("select[name=bunriKbn]");
	dialogCallbackKanjyouKamokuEdabanSentaku = function(){
		displayZeiritsu();
		zeiShuseiBtnDisplay();
		bunriShiireSeigyo();
		kingakuSaikeisan();
		futanBumonCdLostFocus();
	};
	commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamokuCd, title,dialogRetKazeiKbn,dialogRetBunriKbn, $("[name=denpyouKbn]").val(), meisai.find("input[name=shiwakeEdaNo]").val());
}

/**
 * 負担部門選択ボタン押下時Function
 */
function futanBumonSentaku() {
	var meisai = $("#dialogMainDenpyouMeisai").find("div.meisai");
	dialogRetFutanBumonCd 	= meisai.find("input[name=futanBumonCd]");
	dialogRetFutanBumonName = meisai.find("input[name=futanBumonName]");
	dialogRetKariShiireKbn = meisai.find("select[name=kariShiireKbn]");
	commonFutanBumonSentaku("1",meisai.find("[name=kamokuCd]").val(), $("input[name=denpyouId]").val(), $("input[name=kihyouBumonCd]").val(), $("[name=denpyouKbn]").val(), meisai.find("input[name=shiwakeEdaNo]").val());
}

/**
 * 負担部門コードロストフォーカス時Function
 */
function futanBumonCdLostFocus() {
	var meisai = $("#dialogMainDenpyouMeisai").find("div.meisai");
	var title = $(this).parent().parent().find(".control-label").text();
	dialogRetFutanBumonCd 	= meisai.find("input[name=futanBumonCd]");
	dialogRetFutanBumonName = meisai.find("input[name=futanBumonName]");
	dialogRetKariShiireKbn = meisai.find("select[name=kariShiireKbn]");
	commonFutanBumonCdLostFocus("1",dialogRetFutanBumonCd, dialogRetFutanBumonName, title, $("input[name=denpyouId]").val(), $("input[name=kihyouBumonCd]").val(), meisai.find("[name=kamokuCd]").val(), dialogRetKariShiireKbn, $("[name=denpyouKbn]").val(), meisai.find("input[name=shiwakeEdaNo]").val());
}

/**
 * プロジェクト選択ボタン押下時Function
 */
 function pjSentaku(){
	var meisai = $(this).closest("div.meisai");
	dialogRetProjectCd	= meisai.find("input[name=projectCd]");
	dialogRetProjectName = meisai.find("input[name=projectName]");
	commonProjectSentaku();
}

/**
 * プロジェクトコードロストフォーカス時Function
 */
function pjCdLostFocus() {
	var meisai = $(this).closest("div.meisai");
	var title = $(this).parent().parent().find(".control-label").text();
	dialogRetProjectCd	= meisai.find("input[name=projectCd]");
	dialogRetProjectName = meisai.find("input[name=projectName]");
	commonProjectCdLostFocus(dialogRetProjectCd, dialogRetProjectName, title);
}

/**
 * セグメント選択ボタン押下時Function
 */
 function segmentSentaku(){
	var meisai = $(this).closest("div.meisai");
	dialogRetSegmentCd	= meisai.find("input[name=segmentCd]");
	dialogRetSegmentName = meisai.find("input[name=segmentName]");
	commonSegmentSentaku();
}

/**
 * セグメントコードロストフォーカス時Function
 */
function segmentCdLostFocus() {
	var meisai = $(this).closest("div.meisai");
	var title = $(this).parent().parent().find(".control-label").text();
	dialogRetSegmentCd	= meisai.find("input[name=segmentCd]");
	dialogRetSegmentName = meisai.find("input[name=segmentName]");
	commonSegmentCdLostFocus(dialogRetSegmentCd, dialogRetSegmentName, title);
}

/**
 * ユニバーサルフィールド選択ボタン押下時Function
 */
function ufSentaku(i, control) {
	var meisai = control.closest("div.meisai");
	dialogRetUniversalFieldCd = meisai.find("input[name=uf" + i + "Cd]");
	dialogRetUniversalFieldName = meisai.find("input[name=uf" + i + "Name]");
	commonUniversalSentaku(meisai.find("[name=kamokuCd]").val(), i);
}

/**
 * ユニバーサルフィールドクリアボタン押下時Function
 */
function ufClear(i, control) {
	var meisai = control.closest("div.meisai");
	meisai.find("input[name=uf" + i + "Cd]").val("");
	meisai.find("input[name=uf" + i + "Name]").val("");
	console.log(i);
}

/**
 * ユニバーサルフィールドコードロストフォーカス時Function
 */
function ufCdLostFocus(i, control) {
	var meisai = control.closest("div.meisai");
	var title = control.parent().parent().find(".control-label").text();
	var isBtnVisible = meisai.find("button[name=uf" + i + "SentakuButton]").is(":visible");
	if (isBtnVisible) {
		dialogRetUniversalFieldCd = meisai.find("input[name=uf" + i + "Cd]");
		dialogRetUniversalFieldName = meisai.find("input[name=uf" + i + "Name]");
		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, i, title);
	}
}
	
/**
* 消費税額を手入力で修正した際に自動で再計算
*/
function shouhizeigakuShusei(){
	var target = $("#dialogMainDenpyouMeisai");
	var shouhizeigaku			= target.find("input[name=shouhizeigaku]").val().replaceAll(",", "");
	var shiharaiKingaku			= target.find("input[name=shiharaiKingaku]").val().replaceAll(",", "");
	var nyuryokuFlg				= $("#kaikeiContent").find("select[name=nyuryokuHoushiki] option:selected").val();
	var shiireKeikaSothiFlg		= $("input[name=shiirezeigakuKeikasothi]").val();
	//仕入経過措置フラグの分岐はなし
	if(nyuryokuFlg == "1"){
		zeinukiKingakuShusei();
	}else{
		var dShiharaiKingaku = new Decimal(Number(shiharaiKingaku));
		var dShouhizeigaku = new Decimal(Number(shouhizeigaku));
		target.find("input[name=zeinukiKingaku]").val(dShiharaiKingaku.sub(dShouhizeigaku).toString().formatMoney());
	}
}

/**
* 支払金額を手入力で修正した際に自動で再計算(全伝票で使用) 
*/
function shiharaiKingakuShusei(){
	let target = $("#dialogMainDenpyouMeisai");
	let kazeiKbnGroup  = target.find("select[name=kazeiKbn] option:selected").attr("data-kazeiKbnGroup");
	let zeiritsu = kazeiKbnGroup == "1" ? Number(target.find("select[name=zeiritsu]").find("option:selected").val()) : 0;
	let kazeiKbn = target.find("select[name=kazeiKbn] :selected").val();
	let shiharaiKingaku = Number(target.find("input[name=shiharaiKingaku]").val().replaceAll(",", ""));
	let hasuuShoriFlg = $("#workflowForm").find("input[name=hasuuShoriFlg]").val();
	let jigyoushaFlg = $("#kaikeiContent").find("input[name=jigyoushaKbn]:checked").val() > 0 ? $("#kaikeiContent").find("input[name=jigyoushaKbn]:checked").val() : "0";
	let shiireKeikaSothiFlg = $("#workflowForm").find("input[name=shiirezeigakuKeikasothi]").val();
	
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
	let hontaiKingaku	= dShiharaiKingaku.sub(shouhizeigaku);

	//計算した本体金額、消費税額の値をセット or クリア
	target.find("input[name=zeinukiKingaku]").val(hontaiKingaku.toString().formatMoney());
	target.find("input[name=shouhizeigaku]").val(shouhizeigaku.toString().formatMoney());
}

/**
* 税抜金額を手入力で修正した際に自動で再計算(インボイス対応3伝票(請求書払い、支払依頼、自動引落)で使用) 
*/
function zeinukiKingakuShusei(){
	var target = $("#dialogMainDenpyouMeisai");
	var kazeiKbnGroup 		= target.find("select[name=kazeiKbn] option:selected").attr("data-kazeiKbnGroup");
	var zeiritsu			= kazeiKbnGroup == "1" ? Number(target.find("select[name=zeiritsu]").find("option:selected").val()) : "0";
	var hasuuKeisanKbn		= target.find("select[name=zeiritsu]").find("option:selected").attr("data-hasuuKeisanKbn");
	var kazeiKbn			= target.find("select[name=kazeiKbn] :selected").val();
	var hontaiKingaku		= target.find("input[name=zeinukiKingaku]").val().replaceAll(",", "");
	var hasuuShoriFlg		= $("#workflowForm").find("input[name=hasuuShoriFlg]").val();

	//計算用
	var shiharaiKingaku	= null;
	var shouhizeigaku;
	var dShouhizeigaku;
	
	shouhizeigaku = Number(target.find("input[name=shouhizeigaku]").val().replaceAll(",", ""));
	dShouhizeigaku = new Decimal(shouhizeigaku);
	shiharaiKingaku	= new Decimal(Number(hontaiKingaku)).add(dShouhizeigaku);
	//計算した本体金額、消費税額の値をセット or クリア
	target.find("input[name=shiharaiKingaku]").val(shiharaiKingaku.toString().formatMoney());
}

/**
 * 消費税率プルダウンの値を再選択
 */
function reselectZeiritsu(){
	 var meisai = $("#dialogMainDenpyouMeisai");
	 var zeiritau = meisai.find("select[name=zeiritsu] :selected").val();
	 var keigenZeiritsuKbn = meisai.find("select[name=zeiritsu] :selected").attr("data-keigenZeiritsuKbn");
	 var keigenZeiritsuKbnHidden = meisai.find("input[name=keigenZeiritsuKbn]").val();
	 var zeiritsuText = "";
	
	if(keigenZeiritsuKbn !== keigenZeiritsuKbnHidden){
		//選択肢を選び直す
		meisai.find("select[name=zeiritsu] option").filter(function(index){
			if("1" === keigenZeiritsuKbnHidden){
				zeiritsuText = "*"+ zeiritau + "%";
			}else{
				zeiritsuText = zeiritau + "%";
			}
			return $(this).text() === zeiritsuText;
		}).prop('selected', true);
	}
}

/**
 * 課税区分により税率の標示有無切替
 * 税込なら標示、それ以外なら非表示

 * ダイアログ表示時と、取引選択時に呼ばれる
 */
function displayZeiritsu() {
	var meisai = $("#dialogMainDenpyouMeisai");
	var kazeiKbnGroup = meisai.find("select[name=kazeiKbn] option:selected").attr("data-kazeiKbnGroup");
	var zeiritsuArea = meisai.find("select[name=zeiritsu]").closest(".control-group");
	var kaigaiFlg = meisai.find("input[name=kaigaiFlg]:checked").val();

	//インボイス対応前：課税区分が税込、税抜以外の場合は消費税入力欄を丸ごと消していた（zeiritsuArea.hide()）
	//20230329 ひとまずの対応として税込、税抜以外の場合は税率を非表示にする
	var hyoujiFlg = (kazeiKbnGroup == "1" || kazeiKbnGroup == "2") && kaigaiFlg != '1';
	var displayStyle = false == hyoujiFlg ? 'hide' : 'show';
	meisai.find("#zeiritsuArea")[displayStyle]();
}

//消費税額修正ボタンの表示制御
function zeiShuseiBtnDisplay(){
	var meisai = $("#dialogMainDenpyouMeisai");
	var zeigakuShuseiFlg = $("#workflowForm").find("input[name=zeigakuShuseiFlg]").val();
	var nyuryokuFlg = $("#kaikeiContent").find("select[name=nyuryokuHoushiki] option:selected").val();
	var kazeiKbn = meisai.find("select[name=kazeiKbn] option:selected").val();
	var isZeinuki = (kazeiKbn == "002" || kazeiKbn == "013" || kazeiKbn == "014");
	if(nyuryokuFlg == "1"){
		//入力方式が税抜入力　→課税系なら常に消費税入力可
		let isValidKazeiKbn = ["001", "002", "011", "012", "013", "014"].includes(kazeiKbn);
		meisai.find("input[name=shouhizeigaku]").prop("disabled", !isValidKazeiKbn);
		if(!isValidKazeiKbn) {
			meisai.find("input[name=shouhizeigaku]").val("0");
		}
		meisai.find("button[name=zeiShuseiBtn]").hide();
	}else if(isZeinuki){
		//入力方式が税込入力 かつ 課税区分が税抜系　→会社設定(zeigakuShuseiFlg)に従う
		if(zeigakuShuseiFlg == "1"){
			meisai.find("button[name=zeiShuseiBtn]").show();
			meisai.find("input[name=shouhizeigaku]").prop("disabled", true);
		}else {
			meisai.find("button[name=zeiShuseiBtn]").hide();
			meisai.find("input[name=shouhizeigaku]").prop("disabled", zeigakuShuseiFlg != "2");
		}
	}else{
		//入力方式が税込入力 かつ 課税区分が税抜系以外　→常に不可
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
 * 分離区分のdisable制御と仕入区分の非表示制御
 */
 function bunriShiireSeigyo() {
	 var meisai = $("#dialogMainDenpyouMeisai");
	 var shiireZeiAnbun = $("#workflowForm").find("input[name=shiireZeiAnbun]").val();
	//新規入力状態のときは取引を選択するまで入力不可
	//取引コードを消してフォーカスアウトしたらリセット
	if(meisai.find("input[name=shiwakeEdaNo]").val() == ''){
		meisai.find("select[name=bunriKbn]").val("9");
		meisai.find("select[name=bunriKbn]").prop("disabled", true);
		meisai.find("select[name=kariShiireKbn]").val("0");
		meisai.find("select[name=kariShiireKbn]").prop("disabled", true);
	}else{		
		//消費税区分が0：税込方式なら問答無用でdisable
		//1：税抜方式　2：一括税抜方式のとき課税区分に従う
		//　課税区分について　2税抜・13課抜仕入・14課抜売上　は税抜扱い（SIAS消費税設定の情報より）
		//　1税込・11課込仕入・12課込売上　のみが自動分離を選択できる
		//　任意枝番で分離区分の登録がある枝番を選択した場合は枝番に登録されていた分離区分が優先される
		
		//仕入区分
		//仕入税額按分フラグが比例配分の時は非表示、個別対応方式の場合は取引仕訳の設定値を(もしくは部門仕入)を表示してdisabled
		
		//課税区分の空白グレーアウトは、取引登録時に対応済みそしてここでは何もしない
		var nyuryokuFlg = $("#kaikeiContent").find("select[name=nyuryokuHoushiki] option:selected").val();
		var shouhizeiKbn = $("#workflowForm").find("input[name=shouhizeikbn]").val();
		var kazeiKbn = meisai.find("select[name=kazeiKbn] option:selected").val();
		if(shouhizeiKbn == "0"){
			meisai.find("select[name=bunriKbn]").val("9");
			meisai.find("select[name=bunriKbn]").prop("disabled", true);
		}else if(['001', '011','012'].includes(kazeiKbn)){
			//課税区分の分岐を資料記載通りにわかりやすく合わせる
			if(meisai.find("select[name=bunriKbn]").prop("selectedIndex") < 0){
				meisai.find("select[name=bunriKbn]").val("9");
			}
			if(shouhizeiKbn == "1"){
				//税抜方式の場合、自動分離
				meisai.find("select[name=bunriKbn]").val("1");
			}
		}else if(['002', '013','014'].includes(kazeiKbn)){
			//課税区分税抜系は0無し
			meisai.find("select[name=bunriKbn]").val("0");//無し
		}else{
			//その他空白
			meisai.find("select[name=bunriKbn]").val("9");//空白
		}
		
		var shoriGroup = meisai.find("input[name=shoriGroup]").val();
		if(shiireZeiAnbun == "1"){/*個別対応方式*/
			if(['', '100'].includes(kazeiKbn) || !((['2', '5', '6','7', '8', '10'].includes(shoriGroup)) && (['001', '002', '011','013'].includes(kazeiKbn)))){
				meisai.find("select[name=kariShiireKbn]").val("0");
			}else  if(meisai.find("select[name=kariShiireKbn] option[value=0]").prop('selected')){
				meisai.find("select[name=kariShiireKbn]").val("3");
	        }
		}
	}
	if(shiireZeiAnbun == "0"){/*比例配分*/
		meisai.find("label[for=kariShiireKbn]").hide();
		meisai.find("select[name=kariShiireKbn]").val("0"); //選択reset
		meisai.find("select[name=kariShiireKbn]").hide();
	}
}

/**
 * 前の明細情報の表示
 */
function meisaiPrevious(event) {
	
	var index = event.data.index;
	var maxIndex = event.data.maxIndex;
	
	// 前のインデックスへシフト
	index = parseInt(index) - 1;
	
	// 最小インデックスを下回る場合、インデックスを最小インデックスに留める
	if (index < 0) {
		index = 0;
	} 		
	
	// 明細情報の取得
	var meisaiInfo = getMeisaiInfo(index);

	// ダイアログの再表示
	$("#dialogMeisai").dialog("close");
	meisaiDialogOpen(index, null);
}

/**
 * 次の明細情報の表示
 */
function meisaiNext(event) {
	
	var index = event.data.index;
	var maxIndex = event.data.maxIndex;
	
	// 次のインデックスへシフト
	index = parseInt(index) + 1;
	
	// 最大インデックスを上回る場合、インデックスを最大インデックスに留める
	if (index > maxIndex) {
		index = maxIndex;
	} 	
	
	// 明細情報の取得
	var meisaiInfo = getMeisaiInfo(index);

	// ダイアログの再表示
	$("#dialogMeisai").dialog("close");
	meisaiDialogOpen(index, null);
}

/**
 * 消費税額修正ボタン押下時Function
 */
$("button[name=zeiShuseiBtn]").click(function(e) {
	var meisai = $("#dialogMainDenpyouMeisai");
	var flg = meisai.find("input[name=shouhizeigaku]").prop('disabled');
	meisai.find("input[name=shouhizeigaku]").prop("disabled", !flg);
	if(flg){
		addCalculator(meisai.find("input[name=shouhizeigaku]"));
	}else{
		delCalculator(meisai.find("input[name=shouhizeigaku]"));
	}
});

//金額を再計算する場合に、税込入力or税抜入力で計算方法を変える
function kingakuSaikeisan(){
	if($("#kaikeiContent").find("select[name=nyuryokuHoushiki] option:selected").val() == "0"){
		shiharaiKingakuShusei();
	}else{
		zeinukiKingakuShusei();
	}
}

/**
 * 明細のアクション紐付け
 * @param target
 */
function meisaiActionAdd(target) {
	//ボタン押下時アクション
	target.find("button[name=torihikiSentakuButton]").click(torihikiSentaku);
	target.find("button[name=kamokuEdabanSentakuButton]").click(kamokuEdabanSentaku);
	target.find("button[name=futanBumonSentakuButton]").click(futanBumonSentaku);
	target.find("button[name=projectSentakuButton]").click(pjSentaku);
	target.find("button[name=segmentSentakuButton]").click(segmentSentaku);
	for(let i = 1; i <= 10; i++)
	{
		target.find("button[name=uf" + i + "SentakuButton]").click(function(){ ufSentaku(i, $(this)); });
		target.find("button[name=uf" + i + "ClearButton]").click(function(){ ufClear(i, $(this)); });
		target.find("input[name=uf" + i + "Cd]").blur(function(){ ufCdLostFocus(i, $(this)); });
	}
	
	target.find("input[name=shiwakeEdaNo]").change(shiwakeEdaNoLostFocus);
	target.find("input[name=kamokuEdabanCd]").blur(kamokuEdabanCdLostFocus);
	target.find("input[name=futanBumonCd]").blur(futanBumonCdLostFocus);
	target.find("input[name=projectCd]").blur(pjCdLostFocus);
	target.find("input[name=segmentCd]").blur(segmentCdLostFocus);

	//各種金額 手入力で修正した際に自動で再計算
	target.find("input[name=shouhizeigaku]").blur(shouhizeigakuShusei);
	target.find("input[name=shiharaiKingaku]").blur(shiharaiKingakuShusei);
	target.find("input[name=zeinukiKingaku]").blur(zeinukiKingakuShusei);
	target.find("select[name=zeiritsu]").change(kingakuSaikeisan);

	// 課税区分
	// 税率の変更可否制御
	$("body").on("change", "select[name=kazeiKbn]", function(){
		displayZeiritsu();
		zeiShuseiBtnDisplay();
		bunriShiireSeigyo();
	});
}
</script>