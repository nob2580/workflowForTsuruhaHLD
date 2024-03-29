<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- エラー -->
<%@ include file="/jsp/eteam/include/InputError.jsp"%>

<!-- メイン -->
<div id='dialogMainDenpyouMeisai' class='form-horizontal'>
	<input type='hidden' id="errorCnt" value='${fn:length(errorList)}'>
	<input type="hidden" name='index' value='${su:htmlEscape(index)}'>
	<input type="hidden" name='maxIndex' value='${su:htmlEscape(maxIndex)}'>
	<input type='hidden' name='zeroEnabled'			value='' >
	<input type='hidden' name='enableHoujinCard'	value='${su:htmlEscape(enableHoujinCard)}' >
	<input type="hidden" name='nyuryokuHoushiki' value='${su:htmlEscape(nyuryokuHoushiki)}'>
	<input type="hidden" name='zeiritsuListCount' value='${su:htmlEscape(zeiritsuListCount)}'>

	<!-- 入力フィールド -->
	<section>
		<h2 style='line-height: 30px;<c:if test='${null eq index or "" eq index}'> display:none</c:if>'>
			<span>${su:htmlEscape(index + 1)} / ${su:htmlEscape(maxIndex + 1)}
				<button type='button' name='btnPrevious' class='btn'style='position: absolute; right: 90px; font-size: 14px;'><i class='icon-arrow-left'></i> 前へ</button>
				<button type='button' name='btnNext' class='btn' style='position: absolute; right: 10px; font-size: 14px;'>	次へ <i class='icon-arrow-right'></i></button>
			</span>
		</h2>
		<div class="meisai">
			<!-- 使用者 -->
			<c:if test='${denpyouKbn eq "A001"}'>
				<div class='control-group'	<c:if test='${not ks1.userName.hyoujiFlg}'>style='display:none;'</c:if>>
					<label class='control-label'><c:if	test='${ks1.userName.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.userName.name)}</label>
					<div class='controls'>
						<input type='text' name="shainNo" class='input-medium pc_only'value='${su:htmlEscape(shainNo)}'>
						<input type='text' name="userName" class='input-xlarge' disabled value='${su:htmlEscape(userName)}'>
						<input type='hidden' name="userId" value='${su:htmlEscape(userId)}'>
						<button type='button' name='userSentakuButton'	class='btn btn-small'>選択</button>
					</div>
				</div>
			</c:if>
			<!-- 出張区分 -->
			<div class='control-group'
				<c:if test='${not ks1.shucchouKbn.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if	test='${ks1.shucchouKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.shucchouKbn.name)}</label>
				<div class='controls'>
					<label class='radio inline'> 
						<input type='radio' name='kaigaiFlg' value='1'<c:if test='${!(empty himodukeCardMeisaiKeihi)}'> disabled </c:if>
						<c:if test='${"1" eq kaigaiFlg}'>checked</c:if>> 海外
					</label> <label class='radio inline'>
						<input type='radio'name='kaigaiFlg' value='0' <c:if test='${!(empty himodukeCardMeisaiKeihi)}'> disabled </c:if>
						<c:if test='${"0" eq kaigaiFlg}'>checked</c:if>> 国内
					</label>
				</div>
			</div>
			<!-- 使用日 -->
			<div class='control-group'>
				<label class='control-label'
					<c:if test='${not ks1.shiyoubi.hyoujiFlg}'>style='display:none;'</c:if>><c:if
						test='${ks1.shiyoubi.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(ks1.shiyoubi.name)}</label>
				<div class='controls'>
					<input type='text' name='shiyoubi' class='input-small datepicker' value='${su:htmlEscape(shiyoubi)}'<c:if test='${not houjinCardDateEnabled and !(empty himodukeCardMeisaiKeihi)}'> disabled </c:if><c:if test='${not ks1.shiyoubi.hyoujiFlg}'>style='display:none;'</c:if>>
					<!-- 領収書・請求書等 -->
					<label class='label'<c:if test='${not ks1.shouhyouShoruiFlg.hyoujiFlg}'>style='display:none;'</c:if>>
					<c:if test='${ks1.shouhyouShoruiFlg.hissuFlg}'>	<span class='required'>*</span></c:if>${su:htmlEscape(ks1.shouhyouShoruiFlg.name)}</label>
					<input type='checkbox' name='shouhyouShorui'
						<c:if test='${"1" eq shouhyouShorui}'>checked</c:if> value="1"
						<c:if test='${not ks1.shouhyouShoruiFlg.hyoujiFlg}'>style='display:none;'</c:if>>
				</div>
			</div>
			<!-- 取引 -->
			<div class='control-group'<c:if test='${not ks1.torihiki.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.torihiki.hissuFlg}'> <span class='required'>*</span></c:if>${su:htmlEscape(ks1.torihiki.name)}</label>
				<div class='controls'>
					<input type='text' name='shiwakeEdaNo' value='${su:htmlEscape(shiwakeEdaNo)}' class='input-small pc_only'>
					<input type='text' name="torihikiName" class='input-xlarge'	disabled value='${su:htmlEscape(torihikiName)}'>
					<input	type='hidden' name="shiwakeEdaNo" value='${su:htmlEscape(shiwakeEdaNo)}'>
					<button type='button' name='torihikiSentakuButton'	class='btn btn-small'>選択</button>
				</div>
			</div>
			<!-- 勘定科目 -->
			<div class='control-group'	<c:if test='${not ks1.kamoku.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if	test='${ks1.kamoku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.kamoku.name)}</label>
				<div class='controls'>
					<input type='text' name='kamokuCd'	value='${su:htmlEscape(kamokuCd)}' class='input-small pc_only' disabled> 
					<input type='text' name='kamokuName' class='input-xlarge' value='${su:htmlEscape(kamokuName)}' disabled>
					<input type='hidden' name='shoriGroup' value='${su:htmlEscape(shoriGroup)}'>
				</div>
			</div>
			<!-- 課税区分 -->
			<div class='control-group' id='kazeiDiv'<c:if test='${not ks1.kazeiKbn.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.kazeiKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.kazeiKbn.name)}</label>
				<div class='controls'>
					<select name='kazeiKbn' class='input-medium' disabled>
						<c:forEach var="kazeiKbnRecord" items="${kazeiKbnList}">
							<option value='${kazeiKbnRecord.naibu_cd}' data-kazeiKbnGroup='${kazeiKbnRecord.option1}' <c:if test='${kazeiKbnRecord.naibu_cd eq kazeiKbn}'>selected</c:if>>${su:htmlEscape(kazeiKbnRecord.name)}</option>
						</c:forEach>
					</select>
				<!-- 税率 -->
				<span id='zeiritsuArea' <c:if test='${not ks1.zeiritsu.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='label' for='zeiritsu'><c:if test='${ks1.zeiritsu.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.zeiritsu.name)}</label>
					<select name='zeiritsu' class='input-small' <c:if test='${not zeiritsuEnable}'>disabled</c:if>>
						<c:forEach var="tmpZeiritsu" items="${zeiritsuList}">
							<option value='${tmpZeiritsu.zeiritsu}' data-hasuuKeisanKbn='${tmpZeiritsu.hasuuKeisanKbn}' data-keigenZeiritsuKbn='${tmpZeiritsu.keigenZeiritsuKbn}' <c:if test='${tmpZeiritsu.zeiritsu eq zeiritsu && tmpZeiritsu.keigenZeiritsuKbn eq keigenZeiritsuKbn}'>selected</c:if>><c:if test='${tmpZeiritsu.keigenZeiritsuKbn eq "1"}'>*</c:if>${tmpZeiritsu.zeiritsu}%</option>
						</c:forEach>
					</select>
					<input type='hidden' name='keigenZeiritsuKbn' value='${su:htmlEscape(keigenZeiritsuKbn)}'>
				</span>
				<!-- 分離区分 -->
				<span id='bunriShiire1' <c:if test='${not ks1.bunriKbn.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='label' for='bunriKbn'><c:if test='${ks1.bunriKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.bunriKbn.name)}</label>
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
				<span id='bunriShiire2'>
				<label class='label' for='kariShiireKbn'>${su:htmlEscape(ks1.shiireKbn.name)}</label>
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
			<!-- 勘定科目枝番 -->
			<div class='control-group'	<c:if test='${not ks1.kamokuEdaban.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if	test='${ks1.kamokuEdaban.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.kamokuEdaban.name)}</label>
				<div class='controls'>
					<input type='text' name="kamokuEdabanCd" class='input-medium pc_only' value='${su:htmlEscape(kamokuEdabanCd)}'<c:if test='${not kamokuEdabanEnable}'>disabled</c:if>>
					<input type='text' name="kamokuEdabanName" class='input-xlarge' value='${su:htmlEscape(kamokuEdabanName)}' disabled>
					<button type='button' name='kamokuEdabanSentakuButton'	class='btn btn-small'<c:if test='${not kamokuEdabanEnable}'>disabled</c:if>>選択</button>
				</div>
			</div>
			<!-- 負担部門 -->
			<div class='control-group' <c:if test='${not ks1.futanBumon.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.futanBumon.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.futanBumon.name)}</label>
				<div class='controls'>
					<input type='text' name="futanBumonCd" class='input-small pc_only'	value='${su:htmlEscape(futanBumonCd)}'<c:if test='${not futanBumonEnable}'>disabled</c:if>>
						<input	type='text' name="futanBumonName" class='input-xlarge' value='${su:htmlEscape(futanBumonName)}' disabled>
					<button type='button' name='futanBumonSentakuButton' class='btn btn-small'<c:if test='${not futanBumonEnable}'>disabled</c:if>>選択</button>
				</div>
			</div>
			<!-- 取引先 -->
			<div class='control-group'<c:if test='${not ks1.torihikisaki.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.torihikisaki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.torihikisaki.name)}</label>
				<div class='controls'>
					<input type='text' name="torihikisakiCd" class='input-medium pc_only' value='${su:htmlEscape(torihikiCd)}'<c:if test='${not torihikisakiEnable}'>disabled</c:if>>
						<input type='text' name="torihikisakiName" class='input-xlarge' value='${su:htmlEscape(torihikiName)}' disabled>
					<button type='button' name='torihikisakiSentakuButton'	class='btn btn-small'<c:if test='${not torihikisakiEnable}'>disabled</c:if>>選択</button>
					<button type='button' name='torihikisakiClearButton' class='btn btn-small'<c:if test='${not torihikisakiEnable}'>disabled</c:if>>クリア</button>
				</div>
				<input type='hidden' name="menzeiJigyoushaFlg" value='${su:htmlEscape(menzeiJigyoushaFlg)}'>
			</div>
			<!-- 支払先名 -->
			<div id='shiharaisakidiv' class='control-group' <c:if test='${not ks1.shiharaisaki.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.shiharaisaki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.shiharaisaki.name)}</label>
				<div class='controls'>
					<input type='text' name='shiharaisakiName' maxlength='60' class='input-block-level' value='${su:htmlEscape(shiharaisakiName)}'>
				</div>
			</div>
			<!-- 事業者区分 -->
				<div class='control-group' id='jigyoushaKbnDiv' name='jigyoushaKbn'<c:if test='${not ks1.jigyoushaKbn.hyoujiFlg}'>style='display:none;'</c:if>>
					<label class='control-label' for='jigyoushaKbn'>事業者区分</label>
					<div class='controls'>
						<c:forEach var="jigyoushaKbnRecord" items="${jigyoushaKbnList}">
							<label class='radio inline'><input type='radio' name='jigyoushaKbn' value='${su:htmlEscape(jigyoushaKbnRecord.naibu_cd)}' <c:if test='${jigyoushaKbnRecord.naibu_cd eq jigyoushaKbn}'>checked </c:if>>${su:htmlEscape(jigyoushaKbnRecord.name)}</label>
						</c:forEach>
					</div>
				</div>
				<input type='hidden' value='${su:htmlEscape(jigyoushaKbn)}'>
			<!-- 振込先 -->
			<div class='control-group'<c:if test='${not ks1.furikomisakiJouhou.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'>
					<c:if test='${ks1.furikomisakiJouhou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.furikomisakiJouhou.name)}</label>
				<div class='controls'>
					<input type='text' name='furikomisakiJouhou' maxlength='150' class='input-block-level'<c:if test='${not torihikisakiEnable}'>disabled</c:if>value='${su:htmlEscape(furikomisakiJouhou)}'>
				</div>
			</div>
			<!-- プロジェクト -->
			<div class='control-group'<c:if test='${not("0" ne pjShiyouFlg and ks1.project.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if	test='${ks1.project.hissuFlg}'><span class='required'>*</span>
				</c:if>${su:htmlEscape(ks1.project.name)}</label>
				<div class='controls'>
					<input type='text' name="projectCd" class='pc_only'	<c:if test='${not projectEnable}'>disabled</c:if> value='${su:htmlEscape(projectCd[i])}'>
					<input type='text'	name="projectName" class='input-xlarge' disabled value='${su:htmlEscape(projectName)}'>
					<button type='button' name='projectSentakuButton' class='btn btn-small'	<c:if test='${not projectEnable}'>disabled</c:if>>選択</button>
				</div>
			</div>
			<!-- セグメント -->
			<div class='control-group'<c:if test='${not("0" ne segmentShiyouFlg and ks1.segment.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if	test='${ks1.segment.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.segment.name)}</label>
				<div class='controls'>
					<input type='text' name="segmentCd" class='pc_only'<c:if test='${not segmentEnable}'>disabled</c:if> value='${su:htmlEscape(segmentCd[i])}'>
						<input type='text'	name="segmentName" class='input-xlarge' disabled value='${su:htmlEscape(segmentName)}'>
					<button type='button' name='segmentSentakuButton'	class='btn btn-small'<c:if test='${not segmentEnable}'>disabled</c:if>>選択</button>
				</div>
			</div>
			<div class='control-group'<c:if test='${not ("0" ne hfUfSeigyo.uf1ShiyouFlg and ks1.uf1.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if	test='${ks1.uf1.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf1Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or "1" eq hfUfSeigyo.uf1ShiyouFlg}'>
						<input type='text' name="uf1Cd" maxlength='20'	value='${su:htmlEscape(uf1Cd[i])}'<c:if test='${"UF1" eq shainCdRenkeiArea or not uf1Enable}'>disabled</c:if>>
						<input type='hidden' name="uf1Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf1ShiyouFlg or "3" eq hfUfSeigyo.uf1ShiyouFlg}'>
						<input type='text' name="uf1Cd" class='pc_only'	<c:if test='${"UF1" eq shainCdRenkeiArea or not uf1Enable}'>disabled</c:if> value='${su:htmlEscape(uf1Cd[i])}'>
						<input type='text' name="uf1Name" class='input-xlarge' disabled value='${su:htmlEscape(uf1Name[i])}'<c:if test='${"UF1" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
						<button type='button' name='uf1SentakuButton' class='btn btn-small'	<c:if test='${"UF1" eq shainCdRenkeiArea}'>style='display:none;'</c:if><c:if test='${not uf1Enable}'>disabled</c:if>>選択</button>
						<button type='button' name='uf1ClearButton' class='btn btn-small'<c:if test='${"UF1" eq shainCdRenkeiArea or not uf1Enable}'>style='display:none;'</c:if>>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group'<c:if test='${not ("0" ne hfUfSeigyo.uf2ShiyouFlg and ks1.uf2.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.uf2.hissuFlg}'>	<span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf2Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or "1" eq hfUfSeigyo.uf2ShiyouFlg}'>
						<input type='text' name="uf2Cd" maxlength='20' value='${su:htmlEscape(uf2Cd[i])}'<c:if test='${"UF2" eq shainCdRenkeiArea or not uf2Enable}'>disabled</c:if>>
						<input type='hidden' name="uf2Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf2ShiyouFlg or "3" eq hfUfSeigyo.uf2ShiyouFlg}'>
						<input type='text' name="uf2Cd" class='pc_only'	<c:if test='${"UF2" eq shainCdRenkeiArea or not uf2Enable}'>disabled</c:if>value='${su:htmlEscape(uf2Cd[i])}'>
						<input type='text' name="uf2Name" class='input-xlarge' disabled value='${su:htmlEscape(uf2Name[i])}'<c:if test='${"UF2" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
						<button type='button' name='uf2SentakuButton' class='btn btn-small'	<c:if test='${"UF2" eq shainCdRenkeiArea}'>style='display:none;'</c:if><c:if test='${not uf2Enable}'>disabled</c:if>>選択</button>
						<button type='button' name='uf2ClearButton' class='btn btn-small'<c:if test='${"UF2" eq shainCdRenkeiArea or not uf2Enable}'>style='display:none;'</c:if>>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group'<c:if test='${not ("0" ne hfUfSeigyo.uf3ShiyouFlg and ks1.uf3.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if	test='${ks1.uf3.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf3Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or "1" eq hfUfSeigyo.uf3ShiyouFlg}'>
						<input type='text' name="uf3Cd" maxlength='20'	value='${su:htmlEscape(uf3Cd[i])}'<c:if test='${"UF3" eq shainCdRenkeiArea or not uf3Enable}'>disabled</c:if>>
						<input type='hidden' name="uf3Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf3ShiyouFlg or "3" eq hfUfSeigyo.uf3ShiyouFlg}'>
						<input type='text' name="uf3Cd" class='pc_only'	<c:if test='${"UF3" eq shainCdRenkeiArea or not uf3Enable}'>disabled</c:if>value='${su:htmlEscape(uf3Cd[i])}'>
						<input type='text' name="uf3Name" class='input-xlarge' disabled value='${su:htmlEscape(uf3Name[i])}'<c:if test='${"UF3" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
						<button type='button' name='uf3SentakuButton' class='btn btn-small'	<c:if test='${"UF3" eq shainCdRenkeiArea}'>style='display:none;'</c:if><c:if test='${not uf3Enable}'>disabled</c:if>>選択</button>
						<button type='button' name='uf3ClearButton' class='btn btn-small'<c:if test='${"UF3" eq shainCdRenkeiArea or not uf3Enable}'>style='display:none;'</c:if>>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group'<c:if test='${not ("0" ne hfUfSeigyo.uf4ShiyouFlg and ks1.uf4.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if	test='${ks1.uf4.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf4Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or "1" eq hfUfSeigyo.uf4ShiyouFlg}'>
						<input type='text' name="uf4Cd" maxlength='20'	value='${su:htmlEscape(uf4Cd[i])}'<c:if test='${"UF4" eq shainCdRenkeiArea or not uf4Enable}'>disabled</c:if>>
						<input type='hidden' name="uf4Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf4ShiyouFlg or "3" eq hfUfSeigyo.uf4ShiyouFlg}'>
						<input type='text' name="uf4Cd" class='pc_only'	<c:if test='${"UF4" eq shainCdRenkeiArea or not uf4Enable}'>disabled</c:if>value='${su:htmlEscape(uf4Cd[i])}'>
						<input type='text' name="uf4Name" class='input-xlarge' disabled value='${su:htmlEscape(uf4Name[i])}'<c:if test='${"UF4" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
						<button type='button' name='uf4SentakuButton' class='btn btn-small'<c:if test='${"UF4" eq shainCdRenkeiArea}'>style='display:none;'</c:if><c:if test='${not uf4Enable}'>disabled</c:if>>選択</button>
						<button type='button' name='uf4ClearButton' class='btn btn-small'<c:if test='${"UF4" eq shainCdRenkeiArea or not uf4Enable}'>style='display:none;'</c:if>>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group'<c:if test='${not ("0" ne hfUfSeigyo.uf5ShiyouFlg and ks1.uf5.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if	test='${ks1.uf5.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf5Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or "1" eq hfUfSeigyo.uf5ShiyouFlg}'>
						<input type='text' name="uf5Cd" maxlength='20'	value='${su:htmlEscape(uf5Cd[i])}'<c:if test='${"UF5" eq shainCdRenkeiArea or not uf5Enable}'>disabled</c:if>>
						<input type='hidden' name="uf5Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf5ShiyouFlg or "3" eq hfUfSeigyo.uf5ShiyouFlg}'>
						<input type='text' name="uf5Cd" class='pc_only'	<c:if test='${"UF5" eq shainCdRenkeiArea or not uf5Enable}'>disabled</c:if> value='${su:htmlEscape(uf5Cd[i])}'>
						<input type='text' name="uf5Name" class='input-xlarge' disabled value='${su:htmlEscape(uf5Name[i])}'<c:if test='${"UF5" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
						<button type='button' name='uf5SentakuButton' class='btn btn-small'<c:if test='${"UF5" eq shainCdRenkeiArea}'>style='display:none;'</c:if><c:if test='${not uf5Enable}'>disabled</c:if>>選択</button>
						<button type='button' name='uf5ClearButton' class='btn btn-small'<c:if test='${"UF5" eq shainCdRenkeiArea or not uf5Enable}'>style='display:none;'</c:if>>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group'<c:if test='${not ("0" ne hfUfSeigyo.uf6ShiyouFlg and ks1.uf6.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if	test='${ks1.uf6.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf6Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or "1" eq hfUfSeigyo.uf6ShiyouFlg}'>
						<input type='text' name="uf6Cd" maxlength='20' 	value='${su:htmlEscape(uf6Cd[i])}'<c:if test='${"UF6" eq shainCdRenkeiArea or not uf6Enable}'>disabled</c:if>>
						<input type='hidden' name="uf6Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf6ShiyouFlg or "3" eq hfUfSeigyo.uf6ShiyouFlg}'>
						<input type='text' name="uf6Cd" class='pc_only' <c:if test='${"UF6" eq shainCdRenkeiArea or not uf6Enable}'>disabled</c:if> value='${su:htmlEscape(uf6Cd[i])}'>
						<input type='text' name="uf6Name" class='input-xlarge' disabled value='${su:htmlEscape(uf6Name[i])}'<c:if test='${"UF6" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
						<button type='button' name='uf6SentakuButton' class='btn btn-small'	<c:if test='${"UF6" eq shainCdRenkeiArea}'>style='display:none;'</c:if><c:if test='${not uf6Enable}'>disabled</c:if>>選択</button>
						<button type='button' name='uf6ClearButton' class='btn btn-small' <c:if test='${"UF6" eq shainCdRenkeiArea or not uf6Enable}'>style='display:none;'</c:if>>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ("0" ne hfUfSeigyo.uf7ShiyouFlg and ks1.uf7.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if	test='${ks1.uf7.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf7Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or "1" eq hfUfSeigyo.uf7ShiyouFlg}'>
						<input type='text' name="uf7Cd" maxlength='20' 	value='${su:htmlEscape(uf7Cd[i])}'<c:if test='${"UF7" eq shainCdRenkeiArea or not uf7Enable}'>disabled</c:if>>
						<input type='hidden' name="uf7Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf7ShiyouFlg or "3" eq hfUfSeigyo.uf7ShiyouFlg}'>
						<input type='text' name="uf7Cd" class='pc_only'	<c:if test='${"UF7" eq shainCdRenkeiArea or not uf7Enable}'>disabled</c:if> value='${su:htmlEscape(uf7Cd[i])}'>
						<input type='text' name="uf7Name" class='input-xlarge' disabled value='${su:htmlEscape(uf7Name[i])}'<c:if test='${"UF7" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
						<button type='button' name='uf7SentakuButton' class='btn btn-small' <c:if test='${"UF7" eq shainCdRenkeiArea}'>style='display:none;'</c:if><c:if test='${not uf7Enable}'>disabled</c:if>>選択</button>
						<button type='button' name='uf7ClearButton' class='btn btn-small' <c:if test='${"UF7" eq shainCdRenkeiArea or not uf7Enable}'>style='display:none;'</c:if>>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group' 	<c:if test='${not ("0" ne hfUfSeigyo.uf8ShiyouFlg and ks1.uf8.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if	test='${ks1.uf8.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf8Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or "1" eq hfUfSeigyo.uf8ShiyouFlg}'>
						<input type='text' name="uf8Cd" maxlength='20' 	value='${su:htmlEscape(uf8Cd[i])}' <c:if test='${"UF8" eq shainCdRenkeiArea or not uf8Enable}'>disabled</c:if>>
						<input type='hidden' name="uf8Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf8ShiyouFlg or "3" eq hfUfSeigyo.uf8ShiyouFlg}'>
						<input type='text' name="uf8Cd" class='pc_only' <c:if test='${"UF8" eq shainCdRenkeiArea or not uf8Enable}'>disabled</c:if> value='${su:htmlEscape(uf8Cd[i])}'>
						<input type='text' name="uf8Name" class='input-xlarge' disabled value='${su:htmlEscape(uf8Name[i])}'<c:if test='${"UF8" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
						<button type='button' name='uf8SentakuButton' class='btn btn-small'	<c:if test='${"UF8" eq shainCdRenkeiArea}'>style='display:none;'</c:if><c:if test='${not uf8Enable}'>disabled</c:if>>選択</button>
						<button type='button' name='uf8ClearButton' class='btn btn-small'<c:if test='${"UF8" eq shainCdRenkeiArea or not uf8Enable}'>style='display:none;'</c:if>>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group'<c:if test='${not ("0" ne hfUfSeigyo.uf9ShiyouFlg and ks1.uf9.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if test='${ks1.uf9.hissuFlg}'><span class='required'>*</span>	</c:if>${su:htmlEscape(hfUfSeigyo.uf9Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or "1" eq hfUfSeigyo.uf9ShiyouFlg}'>
						<input type='text' name="uf9Cd" maxlength='20'	value='${su:htmlEscape(uf9Cd[i])}'<c:if test='${"UF9" eq shainCdRenkeiArea or not uf9Enable}'>disabled</c:if>>
						<input type='hidden' name="uf9Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf9ShiyouFlg or "3" eq hfUfSeigyo.uf9ShiyouFlg}'>
						<input type='text' name="uf9Cd" class='pc_only'	<c:if test='${"UF9" eq shainCdRenkeiArea or not uf9Enable}'>disabled</c:if>value='${su:htmlEscape(uf9Cd[i])}'>
						<input type='text' name="uf9Name" class='input-xlarge' disabled value='${su:htmlEscape(uf9Name[i])}'<c:if test='${"UF9" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
						<button type='button' name='uf9SentakuButton' class='btn btn-small'	<c:if test='${"UF9" eq shainCdRenkeiArea}'>style='display:none;'</c:if><c:if test='${not uf9Enable}'>disabled</c:if>>選択</button>
						<button type='button' name='uf9ClearButton' class='btn btn-small'<c:if test='${"UF9" eq shainCdRenkeiArea or not uf9Enable}'>style='display:none;'</c:if>>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group'<c:if test='${not ("0" ne hfUfSeigyo.uf10ShiyouFlg and ks1.uf10.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if	test='${ks1.uf10.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf10Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or "1" eq hfUfSeigyo.uf10ShiyouFlg}'>
						<input type='text' name="uf10Cd" maxlength='20' value='${su:htmlEscape(uf10Cd[i])}' <c:if test='${"UF10" eq shainCdRenkeiArea or not uf10Enable}'>disabled</c:if>>
						<input type='hidden' name="uf10Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf10ShiyouFlg or "3" eq hfUfSeigyo.uf10ShiyouFlg}'>
						<input type='text' name="uf10Cd" class='pc_only' <c:if test='${"UF10" eq shainCdRenkeiArea or not uf10Enable}'>disabled</c:if>	value='${su:htmlEscape(uf10Cd[i])}'>
						<input type='text' name="uf10Name" class='input-xlarge' disabled value='${su:htmlEscape(uf10Name[i])}'
							<c:if test='${"UF10" eq shainCdRenkeiArea}'>style='display:none;'</c:if>>
						<button type='button' name='uf10SentakuButton' 	class='btn btn-small'<c:if test='${"UF10" eq shainCdRenkeiArea}'>style='display:none;'</c:if><c:if test='${not uf10Enable}'>disabled</c:if>>選択</button>
						<button type='button' name='uf10ClearButton' class='btn btn-small'<c:if test='${"UF10" eq shainCdRenkeiArea or not uf10Enable}'>style='display:none;'</c:if>>クリア</button>
					</c:if>
				</div>
			</div>

			<c:if test='${enableGaika}'>
				<c:if test='${ks1.heishu.hyoujiFlg}'>
					<!-- 幣種・レート -->
					<div class='control-group'>
						<label class='control-label'><c:if	test='${ks1.heishu.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.heishu.name)}</label>
						<div class='controls'>
							<input type='text' class='input-medium' name='heishu' maxlength='200'<c:if test='${!(empty himodukeCardMeisaiKeihi)}'> disabled </c:if>value='${su:htmlEscape(heishu)}'>
							<button type='button' id='heishuSentakuButton'<c:if test='${!(empty himodukeCardMeisaiKeihi)}'> disabled </c:if> class='btn btn-small'>選択</button>
							<label class='label' <c:if test='${not ks1.rate.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks1.rate.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.rate.name)}</label>
								<input type='text' 	class='input-medium' name='rate' maxlength='200' <c:if test='${!(empty himodukeCardMeisaiKeihi)}'> disabled </c:if> value='${su:htmlEscape(rate)}'>
						</div>
					</div>
				</c:if>
				<c:if test='${ks1.gaika.hyoujiFlg}'>
					<!-- 外貨 -->
					<div class='control-group'>
						<label class='control-label'><c:if	test='${ks1.gaika.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.gaika.name)}</label>
						<div class='controls'>
							<input type='text' name='gaika' class='input-medium autoNumericDecimal'	<c:if test='${!(empty himodukeCardMeisaiKeihi)}'> disabled </c:if>	value='${su:htmlEscape(gaika)}'>
							<span id='tuukatani'></span>
						</div>
					</div>
				</c:if>
			</c:if>
			<!-- 支払金額 -->
			<div class='control-group'<c:if test='${not ks1.shiharaiKingaku.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if	test='${ks1.shiharaiKingaku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.shiharaiKingaku.name)}</label>
				<div class='controls'>
					<input type='text' name='shiharaiKingaku' class='input-medium autoNumericWithCalcBox'<c:if test='${!(empty himodukeCardMeisaiKeihi)}'> disabled </c:if> value='${su:htmlEscape(shiharaiKingaku)}'>円
					<!-- 法人カード -->
					<span> 
					<label class='label' id='houjinCardFlgKeihiName'<c:if test='${not ks1.houjinCardRiyou.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks1.houjinCardRiyou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.houjinCardRiyou.name)}</label>
						<input	class='check-group' type='checkbox' id='houjinCardFlgKeihi' name='houjinCardFlgKeihi'<c:if test='${"1" eq houjinCardFlgKeihi}'>checked</c:if> value="1"<c:if test='${!(empty himodukeCardMeisaiKeihi)}'> disabled </c:if><c:if test='${not ks1.houjinCardRiyou.hyoujiFlg}'>style='display:none;'</c:if>>
					</span>
					<!-- 会社手配 -->
					<span> 
					<label class='label' id='kaishaTehaiFlgKeihiName'<c:if test='${not (ks1.kaishaTehai.hyoujiFlg && enableKaishaTehai)}'>style='display:none;'</c:if>><c:if test='${ks1.kaishaTehai.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.kaishaTehai.name)}</label>
							<input class='check-group'	type='checkbox' id='kaishaTehaiFlgKeihi' name='kaishaTehaiFlgKeihi'<c:if test='${"1" eq kaishaTehaiFlgKeihi}'>checked</c:if>value="1"<c:if test='${!(empty himodukeCardMeisaiKeihi)}'> disabled </c:if><c:if test='${not (ks1.kaishaTehai.hyoujiFlg && enableKaishaTehai)}'>style='display:none;'</c:if>>
					</span> 
					<input type='hidden' name='himodukeCardMeisaiKeihi' value='${su:htmlEscape(himodukeCardMeisaiKeihi)}'>
				</div>
			</div>
			<!-- 税抜金額 -->
			<div class='control-group' name='zeinukiKingaku'  <c:if test='${not ks1.zeinukiKingaku.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'>${su:htmlEscape(ks1.zeinukiKingaku.name)}</label>
				<div class='controls'>
					<input type='text' name='hontaiKingaku' class='input-medium autoNumericWithCalcBox' value='${su:htmlEscape(hontaiKingaku)}'>円
				</div>
			</div>
			<!-- 消費税額(うち消費税額) -->
			<div class='control-group' name='shouhizeigaku'<c:if test='${not ks1.shouhizeigaku.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'>${su:htmlEscape(ks1.shouhizeigaku.name)}</label>
				<div class='controls'>
					<input type='text' name='shouhizeigaku' id='shouhizeigaku'	class='input-medium autoNumericWithCalcBox' value='${su:htmlEscape(shouhizeigaku)}'>円
					<button class="btn btn-small" id='zeiShuseiBtn' name='zeiShuseiBtn'>修正</button>
				</div>
			</div>
			<!-- 摘要 -->
			<div class='control-group'
				<c:if test='${not ks1.tekiyou.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if	test='${ks1.tekiyou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.tekiyou.name)}</label>
				<div class='controls'>
					<input type='text' name='tekiyou'  class='input-block-level' value='${su:htmlEscape(tekiyou)}'>
				</div>
			</div>
			<!-- 交際費詳細 -->
			<div class='control-group kousaihi'
				<c:if test='${not (ks1.kousaihiShousai.hyoujiFlg && kousaihiEnable)}'>style='display:none;'</c:if>>
				<div class='ninzuukoumoku'<c:if test='${not (ks1.kousaihiShousai.hyoujiFlg && kousaihiEnable && ninzuuEnable)}'>style='display:none;'</c:if>>
					<label class='control-label'><c:if	test='${ks1.kousaihiShousai.hissuFlg}'><span class='required'>*</span></c:if>人数</label>
					<div class='controls'>
						<input type='text' name='kousaihiNinzuu' maxlength='6'
							class='input-small' value='${su:htmlEscape(kousaihiNinzuu)}'>名
						<label class='label'>一人当たりの金額</label>
						<input type='text'	class='input-medium autoNumeric' name='kousaihiHitoriKingaku' maxlength='12' disabled value='${su:htmlEscape(kousaihiHitoriKingaku)}'>円
					</div>
					<input type='hidden' name='kousaihiHyoujiFlg' value='${su:htmlEscape(kousaihiHyoujiFlg)}'>
					<input	type='hidden' name='ninzuuRiyouFlg'	value='${su:htmlEscape(ninzuuRiyouFlg)}'>
				</div>
			</div>
			<div class='control-group kousaihi'<c:if test='${not (ks1.kousaihiShousai.hyoujiFlg && kousaihiEnable)}'>style='display:none;'</c:if>>
				<label class='control-label'><c:if	test='${ks1.kousaihiShousai.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks1.kousaihiShousai.name)}</label>
				<div class='controls'>
					<textarea name="kousaihiShousai" class="input-block-level">${su:htmlEscape(kousaihiShousai)}</textarea>
				</div>
			</div>

			<input type='hidden' name='torihikisakiCd'		value='' >
			<input type='hidden' name='torihikisakiName'	value='' >
			<input type='hidden' name='furikomisakiJouhou'	value='' >
			<input type='hidden' name='hontaiKakeFlg'		value='' >
			<input type='hidden' name='denpyouId'			value='' >
			<input type='hidden' name='denpyouKbn'			value='' >
			<input type='hidden' name='kazeiFlg'			value='' >
		</div>
	</section>
</div>
<!-- main -->

<!-- スクリプト -->
<script style=''>
	var denpyouKbn4DenpyouKanri = $("[name=denpyouKbn]").val();
	if (denpyouKbn4DenpyouKanri == "A004" || denpyouKbn4DenpyouKanri == "A005" || denpyouKbn4DenpyouKanri == "A011" || denpyouKbn4DenpyouKanri == "A012")
		denpyouKbn4DenpyouKanri = "A001";

	/**
	 * ダイアログ明細情報取得
	 */
	function getDialogMeisaiInfo() {
		var meisai = $("#dialogMainDenpyouMeisai");
		return {
			index : $("input[name=index]").val(),
			maxIndex : $("input[name=maxIndex]").val(),
			denpyouId : meisai.find("input[name=denpyouId]").val(),
			denpyouKbn : meisai.find("input[name=denpyouKbn]").val(),
			zeroEnabled : $("input[name=karibaraiOn]:checked").length == 1 ? $("input[name=karibaraiOn]:checked").val() : "0",
			shiwakeEdaNo : meisai.find("input[name=shiwakeEdaNo]").val(),
			torihikiName : meisai.find("input[name=torihikiName]").val(),
			userId : $("input[name=denpyouKbn]").val() == "A004"
					|| $("input[name=denpyouKbn]").val() == "A005"
					|| $("input[name=denpyouKbn]").val() == "A011"
					|| $("input[name=denpyouKbn]").val() == "A012" ? $("input[name=userIdRyohi]").val() : meisai.find("input[name=userId]").val(),
			userName : meisai.find("input[name=userName]").val(),
			kaigaiFlg : meisai.find("input[name=kaigaiFlg]:checked").val() == "1" ? "1": "0",
			shiyoubi : meisai.find("input[name=shiyoubi]").val(),
			shouhyouShorui : (meisai.find("input[name=shouhyouShorui]:checked").length == 1) ? "1": "0",
			shainNo : meisai.find("input[name=shainNo]").val(),
			kamokuCd : meisai.find("input[name=kamokuCd]").val(),
			kamokuName : meisai.find("input[name=kamokuName]").val(),
			kamokuEdabanCd : meisai.find("input[name=kamokuEdabanCd]").val(),
			kamokuEdabanName : meisai.find("input[name=kamokuEdabanName]").val(),
			shoriGroup : meisai.find("input[name=shoriGroup]").val(),
			futanBumonCd : meisai.find("input[name=futanBumonCd]").val(),
			futanBumonName : meisai.find("input[name=futanBumonName]").val(),
			torihikisakiCd : meisai.find("input[name=torihikisakiCd]").val(),
			torihikisakiName : meisai.find("input[name=torihikisakiName]").val(),
			furikomisakiJouhou : meisai.find("input[name=furikomisakiJouhou]").val(),
			projectCd : meisai.find("input[name=projectCd]").val(),
			projectName : meisai.find("input[name=projectName]").val(),
			segmentCd : meisai.find("input[name=segmentCd]").val(),
			segmentName : meisai.find("input[name=segmentName]").val(),
			uf1Cd : meisai.find("input[name=uf1Cd]").val(),
			uf1Name : meisai.find("input[name=uf1Name]").val(),
			uf2Cd : meisai.find("input[name=uf2Cd]").val(),
			uf2Name : meisai.find("input[name=uf2Name]").val(),
			uf3Cd : meisai.find("input[name=uf3Cd]").val(),
			uf3Name : meisai.find("input[name=uf3Name]").val(),
			uf4Cd : meisai.find("input[name=uf4Cd]").val(),
			uf4Name : meisai.find("input[name=uf4Name]").val(),
			uf5Cd : meisai.find("input[name=uf5Cd]").val(),
			uf5Name : meisai.find("input[name=uf5Name]").val(),
			uf6Cd : meisai.find("input[name=uf6Cd]").val(),
			uf6Name : meisai.find("input[name=uf6Name]").val(),
			uf7Cd : meisai.find("input[name=uf7Cd]").val(),
			uf7Name : meisai.find("input[name=uf7Name]").val(),
			uf8Cd : meisai.find("input[name=uf8Cd]").val(),
			uf8Name : meisai.find("input[name=uf8Name]").val(),
			uf9Cd : meisai.find("input[name=uf9Cd]").val(),
			uf9Name : meisai.find("input[name=uf9Name]").val(),
			uf10Cd : meisai.find("input[name=uf10Cd]").val(),
			uf10Name : meisai.find("input[name=uf10Name]").val(),
			kazeiKbn : meisai.find("select[name=kazeiKbn] :selected").val(),
			kazeiKbnText : meisai.find("select[name=kazeiKbn] :selected").text(),
			kazeiKbnGroup : meisai.find("select[name=kazeiKbn] :selected").attr("data-kazeiKbnGroup"),
			zeiritsu : meisai.find("select[name=zeiritsu] :selected").val(),
			zeiritsuText : meisai.find("select[name=zeiritsu] :selected").text(),
			zeiritsuGroup : meisai.find("select[name=zeiritsu] :selected").attr("data-hasuuKeisanKbn"),
			keigenZeiritsuKbn : meisai.find("select[name=zeiritsu] :selected").attr("data-keigenZeiritsuKbn"),
			shiharaiKingaku : meisai.find("input[name=shiharaiKingaku]").val(),
			houjinCardFlgKeihi : (meisai.find("input[name=houjinCardFlgKeihi]:checked").length == 1) ? "1": "0",
			kaishaTehaiFlgKeihi : (meisai.find("input[name=kaishaTehaiFlgKeihi]:checked").length == 1) ? "1": "0",
			tekiyou : meisai.find("input[name=tekiyou]").val(),
			kousaihiShousai : meisai.find("textarea[name=kousaihiShousai]").val(),
			kousaihiNinzuu : meisai.find("input[name=kousaihiNinzuu]").val(),
			kousaihiHitoriKingaku : meisai.find("input[name=kousaihiHitoriKingaku]").val(),
			kousaihiHyoujiFlg : meisai.find("input[name=kousaihiHyoujiFlg]").val(),
			ninzuuRiyouFlg : meisai.find("input[name=ninzuuRiyouFlg]").val(),

			enableInput : 'true',
			kamokuEdabanEnable : !meisai.find("button[name=kamokuEdabanSentakuButton]").is(":disabled"),
			futanBumonEnable : !meisai.find("button[name=futanBumonSentakuButton]").is(":disabled"),
			torihikisakiEnable : !meisai.find("button[name=torihikisakiSentakuButton]").is(":disabled"),
			projectEnable : !meisai.find("button[name=projectSentakuButton]").is(":disabled"),
			segmentEnable : !meisai.find("button[name=segmentSentakuButton]").is(":disabled"),
			zeiritsuEnable : !meisai.find("select[name=zeiritsu]").is(":disabled"),
			kousaihiEnable : 'none' == meisai.find("div.kousaihi").css("display") ? false : true,
			ninzuuEnable : 'none' == meisai.find("div.ninzuukoumoku").css("display") ? false : true,

			heishuCd : meisai.find("input[name=heishu]").val(),
			rate : meisai.find("input[name=rate]").val(),
			gaika : meisai.find("input[name=gaika]").val(),
			tani : meisai.find("#tuukatani").text(),
			kazeiFlg : meisai.find("input[name=kazeiFlg]").val() == "1" ? "1": "0",

			hontaiKingaku : meisai.find("input[name=hontaiKingaku]").val(),
			shouhizeigaku : meisai.find("input[name=shouhizeigaku]").val(),

			torihikisakiCd : meisai.find("input[name=torihikisakiCd]").val(),
			torihikisakiName : meisai.find("input[name=torihikisakiName]").val(),
			furikomisakiJouhou : meisai.find("input[name=furikomisakiJouhou]").val(),
			hontaiKakeFlg : meisai.find("input[name=hontaiKakeFlg]").val(),

			himodukeCardMeisaiKeihi : meisai.find("input[name='himodukeCardMeisaiKeihi']").val(),
			jigyoushaKbn : meisai.find("input[name=jigyoushaKbn]:checked").val() == "1" ? "1": "0",
			jigyoushaKbnText:		meisai.find("input[name=jigyoushaKbn] :checked").text(),
			bunriKbn : meisai.find("select[name=bunriKbn] :selected").val(),
			kariShiireKbn : meisai.find("select[name=kariShiireKbn] :selected").val(),
			shiharaisaki:			meisai.find("input[name=shiharaisakiName]").val()
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
		
		//インボイス対応前の場合、インボイス対応項目を非表示とする
		//分離区分・仕入区分は税抜対応での追加項目であり、インボイス対応前伝票でも表示する
		var hyoujiFlg = $("#denpyouJouhou").find("select[name=invoiceDenpyou]").val();
		var displayStyle = '0' == hyoujiFlg ? 'show' : 'hide';
		// 旧伝票と出張伺いの時だけ非表示の処理を行うようにする　
		// 事業者区分は常に表示のためifの外でもよかったけど、ソース上のstyleの表示がおかしかったので合わせる
		if(hyoujiFlg == "1" || ["A005", "A012"].includes($("input[name=denpyouKbn]").val())){
			$("#dialogMainDenpyouMeisai").find("#jigyoushaKbnDiv")[displayStyle]();
			$("#dialogMainDenpyouMeisai").find("#shiharaisakidiv")[displayStyle]();
			$("#dialogMainDenpyouMeisai").find("input[name=jigyoushaKbn]")[displayStyle]();
			$("#dialogMainDenpyouMeisai").find("[name=zeinukiKingaku]")[displayStyle]();
			$("#dialogMainDenpyouMeisai").find("[name=shouhizeigaku]")[displayStyle]();
		}
		
		bunriShiireSeigyo();
		jigyoushaKbnSeigyo(isEnable);
		
		var meisai = $("#dialogMainDenpyouMeisai");
		if (isEnable) {
			//イベント設定
			meisaiActionAdd(meisai);
			
			//伝票区分をセット
			meisai.find("input[name=denpyouKbn]").val($("input[name=denpyouKbn]").val());
			if (false == !meisaiInfo) {
				meisai.find("button[name=kamokuEdabanSentakuButton]").prop("disabled", !meisaiInfo["kamokuEdabanEnable"]);
				meisai.find("button[name=futanBumonSentakuButton]").prop("disabled", !meisaiInfo["futanBumonEnable"]);
				meisai.find("button[name=torihikisakiSentakuButton]").prop("disabled", !meisaiInfo["torihikisakiEnable"]);
				meisai.find("button[name=projectSentakuButton]").prop("disabled", !meisaiInfo["projectEnable"]);
				meisai.find("button[name=segmentSentakuButton]").prop("disabled", !meisaiInfo["segmentEnable"]);
				meisai.find("select[name=zeiritsu]").prop("disabled",!meisaiInfo["zeiritsuEnable"]);
			}
			//請求書払、自動引落 税込税抜入力フラグ
			if($("input[name=denpyouKbn]").val() == 'A003' || $("input[name=denpyouKbn]").val() == 'A009'){
				if(hyoujiFlg == "0"){
					var nyuryokuFlg = $("#kaikeiContent").find("select[name=nyuryokuHoushiki] :selected").val();
					meisai.find("input[name=shiharaiKingaku]").prop("disabled", nyuryokuFlg == "1");
					meisai.find("input[name=hontaiKingaku]").prop("disabled", nyuryokuFlg == "0");
				}
			}
		} else {
			meisai.find('button,input[type!=hidden],select,textarea').prop("disabled", true);
			meisai.find('.autoNumericWithCalcBox').removeClass('autoNumericWithCalcBox').addClass('autoNumeric');
		}

		//事業者区分の変更可否制御
		var jigyoushaKbnHenkouFlg = $("#workflowForm").find("input[name=jigyoushaKbnHenkouflg]").val();
		if(jigyoushaKbnHenkouFlg == "0"){
			$("#dialogMainDenpyouMeisai").find("input[name=jigyoushaKbn]").prop("disabled", true);
		}
			
		// 次へ・前へボタン押下イベント付与：click(data,fn)
		$("button[name=btnPrevious]").removeAttr("disabled");
		$("button[name=btnNext]").removeAttr("disabled");
		meisai.find("button[name=btnPrevious]").click(meisaiInfo, meisaiPrevious);
		meisai.find("button[name=btnNext]").click(meisaiInfo, meisaiNext);

		//入力補助
		commonInit(meisai);
		if(isEnable){
			//税額修正ボタンの表示制御(commonInitで電卓追加されるためここで呼び出す)
			zeiShuseiBtnDisplay();
		}

		// フォーカス
		meisai.find('input:visible').first().focus();
		
		//摘要文字数制限取得
		meisai.find("input[name=tekiyou]").attr('maxlength', $("#workflowForm").find("input[name=tekiyouMaxLength]").val());		
		
		//20230627 旅費仮払い系 DB上に分離・仕入区分が存在しないため一旦非表示に。（追々DBのカラム追加含め対応）
		if($("input[name=denpyouKbn]").val() == 'A005' || $("input[name=denpyouKbn]").val() == 'A012'){
			meisai.find("#bunriShiire1").hide();
			meisai.find("#bunriShiire2").hide();
		}

		<c:choose>
		<c:when test ='${denpyouKbn eq "A001"}'>
		var dairiFlg = $("#workflowForm").find("input[name=dairiFlg]").val();

		//法人カード使用フラグの表示可否(起票者により表示)
		var rirekiNo = $("#dialogMainDenpyouMeisai").find("input[name='himodukeCardMeisaiKeihi']").val();
		if ($("input[name=enableHoujinCard]").val() == 'true' || (rirekiNo != null && rirekiNo != "")) {
			$("#dialogMainDenpyouMeisai").find("#houjinCardFlgKeihi").closest("span").show();
		} else {
			$("#dialogMainDenpyouMeisai").find("#houjinCardFlgKeihi").closest("span").hide();
		}

		//通常起票時：使用者固定
		if (dairiFlg == "0") {
			$("#dialogMainDenpyouMeisai").find("button[name=userSentakuButton]").prop("disabled",true);
			$("#dialogMainDenpyouMeisai").find("input[name=shainNo]").prop("disabled", true);
			//経費精算 代理起票時：使用者は可変
		} else if ($("input[name=denpyouKbn]").val() == 'A001') {
			//追加の時、Actionでセットされた起票者=使用者をクリア
			if (!meisaiInfo) {
				meisai.find("input[name=userId]").val("");
				meisai.find("input[name=userName]").val("");
				meisai.find("input[name=shainNo]").val("");
				//使用者未選択なので法人カード使用フラグ非表示
				$("#dialogMainDenpyouMeisai").find("#houjinCardFlgKeihi").closest("span").hide();
			}
			//旅費精算・伺い精算 代理起票時：使用者（申請画面）は可変
		} else {
			if ($("input[name=enableHoujinCard]").val() != 'true') {
				//使用者未選択なので法人カード使用フラグ非表示
				$("#dialogMainDenpyouMeisai").find("#houjinCardFlgKeihi").closest("span").hide();
			}
		}
		$("#dialogMainDenpyouMeisai").find("input[name=dairiFlg]").val(dairiFlg);
		</c:when>
		<c:otherwise>
		//法人カード使用フラグは非表示
		$("#dialogMainDenpyouMeisai").find("#houjinCardFlgKeihi").closest("span").hide();
		</c:otherwise>
		</c:choose>
		
		//新規追加(空の表示)なら終わり
		if (!meisaiInfo) {
			return;
		}
		//以下、新規追加以外で表示内容が空でない時

		//明細情報をダイアログに反映
		meisai.find("input[name=kaigaiFlg]:eq(0)").prop("checked",("1" == meisaiInfo["kaigaiFlg"]));
		meisai.find("input[name=kaigaiFlg]:eq(1)").prop("checked",("0" == meisaiInfo["kaigaiFlg"]));
		meisai.find("input[name=shiwakeEdaNo]").val(meisaiInfo["shiwakeEdaNo"]);
		meisai.find("input[name=torihikiName]").val(meisaiInfo["torihikiName"]);
		meisai.find("input[name=sessionUserId]").val(meisaiInfo["sessionUserId"]);
		meisai.find("input[name=userId]").val(meisaiInfo["userId"]);
		meisai.find("input[name=userName]").val(meisaiInfo["userName"]);
		meisai.find("input[name=shiyoubi]").val(meisaiInfo["shiyoubi"]);
		meisai.find("input[name=shouhyouShorui]").val(meisaiInfo["shouhyouShorui"]);
		meisai.find("input[name=shainNo]").val(meisaiInfo["shainNo"]);
		meisai.find("input[name=kamokuCd]").val(meisaiInfo["kamokuCd"]);
		meisai.find("input[name=kamokuName]").val(meisaiInfo["kamokuName"]);
		meisai.find("input[name=kamokuEdabanCd]").val(meisaiInfo["kamokuEdabanCd"]);
		meisai.find("input[name=kamokuEdabanName]").val(meisaiInfo["kamokuEdabanName"]);
		if(meisaiInfo["shoriGroup"] != ""){
			//各種伝票の明細DBには処理グループは保存されていない
			//明細グリッドの処理グループ=""(空)でAction.javaで取得した処理グループを塗り替えないようにする
			meisai.find("input[name=shoriGroup]").val(meisaiInfo["shoriGroup"]);
		}
		meisai.find("input[name=futanBumonCd]").val(meisaiInfo["futanBumonCd"]);
		meisai.find("input[name=futanBumonName]").val(meisaiInfo["futanBumonName"]);
		meisai.find("input[name=torihikiCd]").val(meisaiInfo["torihikiCd"]);
		meisai.find("input[name=torihikiName]").val(meisaiInfo["torihikiName"]);
		meisai.find("input[name=furikomisakiJouhou]").val(meisaiInfo["furikomisakiJouhou"]);
		meisai.find("input[name=projectCd]").val(meisaiInfo["projectCd"]);
		meisai.find("input[name=projectName]").val(meisaiInfo["projectName"]);
		meisai.find("input[name=segmentCd]").val(meisaiInfo["segmentCd"]);
		meisai.find("input[name=segmentName]").val(meisaiInfo["segmentName"]);
		meisai.find("input[name=uf1Cd]").val(meisaiInfo["uf1Cd"]);
		meisai.find("input[name=uf1Name]").val(meisaiInfo["uf1Name"]);
		meisai.find("input[name=uf2Cd]").val(meisaiInfo["uf2Cd"]);
		meisai.find("input[name=uf2Name]").val(meisaiInfo["uf2Name"]);
		meisai.find("input[name=uf3Cd]").val(meisaiInfo["uf3Cd"]);
		meisai.find("input[name=uf3Name]").val(meisaiInfo["uf3Name"]);
		meisai.find("input[name=uf4Cd]").val(meisaiInfo["uf4Cd"]);
		meisai.find("input[name=uf4Name]").val(meisaiInfo["uf4Name"]);
		meisai.find("input[name=uf5Cd]").val(meisaiInfo["uf5Cd"]);
		meisai.find("input[name=uf5Name]").val(meisaiInfo["uf5Name"]);
		meisai.find("input[name=uf6Cd]").val(meisaiInfo["uf6Cd"]);
		meisai.find("input[name=uf6Name]").val(meisaiInfo["uf6Name"]);
		meisai.find("input[name=uf7Cd]").val(meisaiInfo["uf7Cd"]);
		meisai.find("input[name=uf7Name]").val(meisaiInfo["uf7Name"]);
		meisai.find("input[name=uf8Cd]").val(meisaiInfo["uf8Cd"]);
		meisai.find("input[name=uf8Name]").val(meisaiInfo["uf8Name"]);
		meisai.find("input[name=uf9Cd]").val(meisaiInfo["uf9Cd"]);
		meisai.find("input[name=uf9Name]").val(meisaiInfo["uf9Name"]);
		meisai.find("input[name=uf10Cd]").val(meisaiInfo["uf10Cd"]);
		meisai.find("input[name=uf10Name]").val(meisaiInfo["uf10Name"]);
		meisai.find("select[name=kazeiKbn]").val(meisaiInfo["kazeiKbn"]);
		meisai.find("select[name=zeiritsu]").attr({value:meisaiInfo["zeiritsu"],"data-keigenZeiritsuKbn":meisaiInfo["keigenZeiritsuKbn"]});
		meisai.find("input[name=keigenZeiritsuKbn]").val(meisaiInfo["keigenZeiritsuKbn"]);
		meisai.find("input[name=heishu]").val(meisaiInfo["heishuCd"]);
		meisai.find("input[name=rate]").val(meisaiInfo["rate"]);
		meisai.find("input[name=gaika]").val(meisaiInfo["gaika"]);
		meisai.find("#tuukatani").text(meisaiInfo["tani"]);
		meisai.find("input[name=shiharaiKingaku]").val(meisaiInfo["shiharaiKingaku"]);
		meisai.find("input[name=houjinCardFlgKeihi]").prop("checked",("1" == meisaiInfo["houjinCardFlgKeihi"]));
		meisai.find("input[name=kaishaTehaiFlgKeihi]").prop("checked",("1" == meisaiInfo["kaishaTehaiFlgKeihi"]));
		meisai.find("input[name=tekiyou]").val(meisaiInfo["tekiyou"]);
		meisai.find("textarea[name=kousaihiShousai]").val(meisaiInfo["kousaihiShousai"]);
		meisai.find("input[name=kousaihiHyoujiFlg]").val(meisaiInfo["kousaihiHyoujiFlg"]);
		meisai.find("input[name=ninzuuRiyouFlg]").val(meisaiInfo["ninzuuRiyouFlg"]);

		meisai.find("div.kousaihi").css("display",	meisaiInfo["kousaihiEnable"] ? "" : "none");
		meisai.find("div.ninzuukoumoku").css("display",	meisaiInfo["ninzuuEnable"] ? "" : "none");

		meisai.find("input[name=hontaiKingaku]").val(meisaiInfo["hontaiKingaku"]);
		meisai.find("input[name=shouhizeigaku]").val(meisaiInfo["shouhizeigaku"]);

		meisai.find("input[name=torihikisakiCd]").val(meisaiInfo["torihikisakiCd"]);
		meisai.find("input[name=torihikisakiName]").val(meisaiInfo["torihikisakiName"]);
		meisai.find("input[name=furikomisakiJouhou]").val(meisaiInfo["furikomisakiJouhou"]);
		meisai.find("input[name=hontaiKakeFlg]").val(meisaiInfo["hontaiKakeFlg"]);
		meisai.find("input[name=kazeiFlg]").val(meisaiInfo["kazeiFlg"]);
		meisai.find("input[name=himodukeCardMeisaiKeihi]").val(meisaiInfo["himodukeCardMeisaiKeihi"]);
		meisai.find("input[name=jigyoushaKbn]:eq(0)").prop("checked",("0" == meisaiInfo["jigyoushaKbn"]));
		meisai.find("input[name=jigyoushaKbn]:eq(1)").prop("checked",("1" == meisaiInfo["jigyoushaKbn"]));
		meisai.find("select[name=bunriKbn]").val(meisaiInfo["bunriKbn"]);
		meisai.find("select[name=kariShiireKbn]").val(meisaiInfo["kariShiireKbn"]);
		meisai.find("input[name=hontaiKingaku]").val(meisaiInfo["hontaiKingaku"]);
		meisai.find("input[name=shouhizeigaku]").val(meisaiInfo["shouhizeigaku"]);
		meisai.find("input[name=shiharaisakiName]").val(meisaiInfo["shiharaisaki"]);

		//課税区分による税率表示有無
		displayZeiritsu();
		//消費税率プルダウンの選択状態を変更
		reselectZeiritsu();
		//出張区分による表示有無
		kaigaiSentaku(isEnable);
	}

	/**
	 * 消費税率プルダウンの値を再選択
	 */
	function reselectZeiritsu() {
		var meisai = $("#dialogMainDenpyouMeisai");
		var zeiritau = meisai.find("select[name=zeiritsu] :selected").val();
		var keigenZeiritsuKbn = meisai.find("select[name=zeiritsu] :selected").attr("data-keigenZeiritsuKbn");
		var keigenZeiritsuKbnHidden = meisai.find("input[name=keigenZeiritsuKbn]").val();
		var zeiritsuText = "";

		if (keigenZeiritsuKbn !== keigenZeiritsuKbnHidden) {
			//選択肢を選び直す
			meisai.find("select[name=zeiritsu] option").filter(function(index) {
				if ("1" === keigenZeiritsuKbnHidden) {
					zeiritsuText = "*" + zeiritau + "%";
				} else {
					zeiritsuText = zeiritau + "%";
				}
				return $(this).text() === zeiritsuText;
			}).prop('selected', true);
			meisai.find("select[name=zeiritsu] :selected").attr("data-keigenZeiritsuKbn",keigenZeiritsuKbnHidden);
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
		var denpyouKbn = $("input[name=denpyouKbn]").val();
		var zeiritsuCount = $("input[name=zeiritsuListCount]").val();
		
		//海外旅費精算 その他経費(海外分)の場合、軽減税率(1)の税率はリスト上から非表示にする
		if(denpyouKbn == "A011" && kaigaiFlg == "1"){
			for(let i = 0; i < zeiritsuCount; i++){
				if(meisai.find("select[name=zeiritsu] option:eq("+i+")").attr("data-keigenZeiritsuKbn") == "1"){
					meisai.find("select[name=zeiritsu]").find("option").eq(i).toggle(false);
				}
			}
		//国内分の場合は再表示
		}else if(denpyouKbn == "A011"){
			meisai.find("select[name=zeiritsu]").find("option").toggle(true);
		}

		//税込、税抜以外の場合は税率を非表示にする
		var hyoujiFlg = (kazeiKbnGroup == "1" || kazeiKbnGroup == "2") && !($("input[name=denpyouKbn]").val() == "A012" && kaigaiFlg == '1');
		var displayStyle = false == hyoujiFlg ? 'hide' : 'show';
		$("#zeiritsuArea")[displayStyle]();
		
	}
	
	//消費税額修正ボタンの表示制御
	function zeiShuseiBtnDisplay(){
		var meisai = $("#dialogMainDenpyouMeisai");
		var zeigakuShuseiFlg = $("#workflowForm").find("input[name=zeigakuShuseiFlg]").val();
		var nyuryokuFlg = $("#kaikeiContent").find("select[name=nyuryokuHoushiki] :selected").val();
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
				meisai.find("input[name=shouhizeigaku]").prop("disabled", zeigakuShuseiFlg != "2" );
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
function bunriShiireSeigyo(){
	let meisai = $("#dialogMainDenpyouMeisai");
	let shiireZeiAnbun = $("#workflowForm").find("input[name=shiireZeiAnbun]").val();
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
		}else if(['001', '011','012'].includes(kazeiKbn)){
			//課税区分の分岐を資料記載通りにわかりやすく合わせる
			if(meisai.find("select[name=bunriKbn]").prop("selectedIndex") < 0){
				meisai.find("select[name=bunriKbn]").val("9");
			}
			//税抜方式の場合、課税区分：税込系の場合、自動分離
			if(shouhizeiKbn == "1"){
				meisai.find("select[name=bunriKbn]").val("1");
			}
		}else if(['002', '013', '014'].includes(kazeiKbn)){
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
			//使用可能な状態で空白の場合は「共売」をセット
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
 * 事業者区分の入力可否を制御
 * インボイス対応前の伝票では非表示になるためこの制御では分岐しない
 */
function jigyoushaKbnSeigyo(isEnable){
	let meisai = $("#dialogMainDenpyouMeisai");
	let jigyoushaKbnHenkouFlg = $("#workflowForm").find("input[name=jigyoushaKbnHenkouflg]").val();
    let kaigaiFlg = meisai.find("input[name=kaigaiFlg]:checked").val() == "1";
    let menzeiJigyoushaFlg = meisai.find("input[name=menzeiJigyoushaFlg]").val();
    
	//jigyoushaKbnHenkouFlg は事業者払い3伝票以外は必ず1になるようにjudgeJigyoushaKbnHenkou()でしている
	if(meisai.find("input[name=shiwakeEdaNo]").val() == ''){
		//取引をクリアした場合
		meisai.find("input[name=jigyoushaKbn]").prop("disabled", true);
		meisai.find("input[name=jigyoushaKbn]:eq(0)").prop("checked", true);
	}else{
		var shoriGroup = meisai.find("input[name=shoriGroup]").val();
		var kazeiKbn = meisai.find("select[name=kazeiKbn] option:selected").val();
		if((shoriGroup == "21" ||
			((shoriGroup == "2"|| shoriGroup == "5"|| shoriGroup == "6"|| shoriGroup == "7"|| shoriGroup == "8"|| shoriGroup == "10")
				&& (kazeiKbn == "001" || kazeiKbn == "002" || kazeiKbn == "011" || kazeiKbn == "013"))) && !kaigaiFlg){ //海外明細の場合は常に通常課税
			//変更可
			meisai.find("input[name=jigyoushaKbn]").prop("disabled", jigyoushaKbnHenkouFlg == "0");
			if( isEnable && menzeiJigyoushaFlg != "" && meisai.find("input[name=torihikisakiCd]").val() != ""){
				meisai.find("input[name=jigyoushaKbn]:eq("+ menzeiJigyoushaFlg +")").prop("checked", true);
			}
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
 * 法人カード・会社手配はどちらか１つのみ選択できるようにする
 */
function checkboxSeigyo() {
	var flg = $(this).prop('checked');
	$(".check-group").prop('checked', false);
	if (flg) {
		$(this).prop('checked', true);
	}
}

	//元画面から移植
	/**
	 * 使用者選択ボタン押下時Function
	 */
	function userSentaku() {
		dialogRetuserId = $(this).closest("div").find("input[name=userId]");
		dialogRetusername = $(this).closest("div").find("input[name=userName]");
		dialogRetshainNo = $(this).closest("div").find("input[name=shainNo]");
		dialogRetCard = $(this).closest("section").find(
				"input[name=houjinCardFlgKeihi]");
		dialogCallbackUserSentaku = function() {
			if ($("#dialogMainDenpyouMeisai").find("input[name=shiwakeEdaNo]")
					.val() != "") {
				shiwakeEdaNoLostFocus();
			}
			//法人カード使用フラグの表示可否(起票者により表示)
			var rirekiNo = $("#dialogMainDenpyouMeisai").find(
					"input[name='himodukeCardMeisaiKeihi']").val();
			if ($("input[name=enableHoujinCard]").val() == 'true'
					|| (rirekiNo != null && rirekiNo != "")) {
				$("#dialogMainDenpyouMeisai").find("#houjinCardFlgKeihi")
						.closest("span").show();
			} else {
				$("#dialogMainDenpyouMeisai").find("#houjinCardFlgKeihi")
						.closest("span").hide();
			}

		};
		commonUserSentaku(false, ($("#dialogMainDenpyouMeisai").find(
				"input[name='himodukeCardMeisaiKeihi']").val()));
	}
	/**
	 * 社員番号ロストフォーカス時
	 */
	function shaiNoLostFocus() {
		var meisai = $(this).closest("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetuserId = $(this).closest("div").find("input[name=userId]");
		dialogRetusername = $(this).closest("div").find("input[name=userName]");
		dialogRetshainNo = $(this).closest("div").find("input[name=shainNo]");
		dialogRetCard = $(this).closest("section").find(
				"input[name=houjinCardFlgKeihi]");

		//法人カード使用履歴紐付けがあるならチェック列は表示しておく(commonShainNoLostFocus側で法人カードチェックを外すためdialogRetCardを渡さない)
		var rirekiNo = $("#dialogMainDenpyouMeisai").find(
				"input[name='himodukeCardMeisaiKeihi']").val();
		if (rirekiNo != null && rirekiNo != "") {
			commonShainNoLostFocus(dialogRetshainNo, dialogRetusername, title,
					dialogRetuserId);
		} else {
			commonShainNoLostFocus(dialogRetshainNo, dialogRetusername, title,
					dialogRetuserId, dialogRetCard);
		}

		if ($("#dialogMainDenpyouMeisai").find("input[name=shiwakeEdaNo]")
				.val() != "") {
			shiwakeEdaNoLostFocus();
		}
	}

	/**
	 * 取引選択ボタン押下時Function
	 * 【WARNING】こちら変更した場合は必ずshiwakeEdaNoLostFocus()もメンテすること【WARNING】
	 */
	function torihikiSentaku() {
		var denpyouKbn = $("input[name=denpyouKbn]").val();
		var dairiFlg = $("[name=dairiFlg]").val();
		var userId = "";
		if (denpyouKbn == "A001" && dairiFlg == "1") {
			userId = $("#dialogMainDenpyouMeisai").find("input[name=userId]")
					.val();
			if (userId == "") {
				alert("使用者名を入力してください。");
				return;
			}
		}

		if ((denpyouKbn == "A004" || denpyouKbn == "A005") && dairiFlg == "1") {
			userId = $("input[name=userIdRyohi]").val();
		}

		if (denpyouKbn == "A011" || denpyouKbn == "A012") {
			userId = $("input[name=userIdRyohi]").val();
			kaigaiFlg = $("#dialogMainDenpyouMeisai").find("input[name=kaigaiFlg]:checked").length;
			if (kaigaiFlg != 1) {
				alert("出張区分を入力してください。");
				return;
			}
		}
		setTorihikiDialogRet();
		if ($("input[name=denpyouKbn]").val() == "A004"
				|| $("input[name=denpyouKbn]").val() == "A005") {
			denpyouKbn = "A001";
		} else if ($("input[name=denpyouKbn]").val() == "A011"
				|| $("input[name=denpyouKbn]").val() == "A012") {
			if ($("#dialogMainDenpyouMeisai").find(
					"input[name=kaigaiFlg]:checked").val() == "1") {
				denpyouKbn = "A901";
			} else {
				denpyouKbn = "A001";
			}
		}
		dialogCallbackTorihikiSentaku = function() {
			shiwakeEdaNoLostFocus()
		};
		dialogCallbackTorihikiSentaku = function() { shiwakeEdaNoLostFocus() };
		commonTorihikiSentaku(denpyouKbn, $("input[name=kihyouBumonCd]").val(), $("input[name=denpyouId]").val(), $("input[name=daihyouFutanBumonCd]").val(), userId);
	}

	/**
	 * 仕訳枝番号ロストフォーカス時Function
	 * 【WARNING】こちら変更した場合は必ずtorihikiSentaku()もメンテすること【WARNING】
	 */
	function shiwakeEdaNoLostFocus() {
		var denpyouKbn = $("input[name=denpyouKbn]").val();
		var dairiFlg = $("[name=dairiFlg]").val();
		var userId = "";
		if (denpyouKbn == "A001" && dairiFlg == "1") {
			userId = $("#dialogMainDenpyouMeisai").find("input[name=userId]")
					.val();
			if (userId == "") {
				alert("使用者名を入力してください。");
				$("#dialogMainDenpyouMeisai").find("[name=shiwakeEdaNo]").val("");
				$("#dialogMainDenpyouMeisai").find("[name=torihikiName]").val("");
				return;
			}
		}

		if ((denpyouKbn == "A004" || denpyouKbn == "A005") && dairiFlg == "1") {
			userId = $("input[name=userIdRyohi]").val();
		}

		if (denpyouKbn == "A011" || denpyouKbn == "A012") {
			userId = $("input[name=userIdRyohi]").val();
			kaigaiFlg = $("#dialogMainDenpyouMeisai").find("input[name=kaigaiFlg]:checked").length;
			if (kaigaiFlg != 1) {
				alert("出張区分を入力してください。");
				return;
			}
		}
		setTorihikiDialogRet();
		var title = $("#dialogMainDenpyouMeisai").find("[name=shiwakeEdaNo]")
				.parent().parent().find(".control-label").text();
		if ($("input[name=denpyouKbn]").val() == "A004"
				|| $("input[name=denpyouKbn]").val() == "A005") {
			denpyouKbn = "A001";
		} else if ($("input[name=denpyouKbn]").val() == "A011"
				|| $("input[name=denpyouKbn]").val() == "A012") {
			if ($("#dialogMainDenpyouMeisai").find(
					"input[name=kaigaiFlg]:checked").val() == "1") {
				denpyouKbn = "A901";
			} else {
				denpyouKbn = "A001";
			}
		}
		var inFlg = $("#denpyouJouhou").find("select[name=invoiceDenpyou] option:selected").val();
		if ($("input[name=denpyouKbn]").val() == "A002" || $("input[name=denpyouKbn]").val() == "A005" || $("input[name=denpyouKbn]").val() == "A012") {
			inFlg = "0"; //A002の対応はココでは必要ないかも
		}
		commonShiwakeEdaNoLostFocus(dialogRetShiwakeEdaNo,
				dialogRetTorihikiName, title, denpyouKbn, $(
						"input[name=kihyouBumonCd]").val(), $(
						"input[name=denpyouId]").val(), $(
						"input[name=daihyouFutanBumonCd]").val(), userId,inFlg);
	}

	/**
	 * 取引ダイアログ表示後の設定項目Function
	 */
	function setTorihikiDialogRet() {
		var meisai = $("#dialogMainDenpyouMeisai").find("div.meisai");
		// 取引先
		dialogRetShiwakeFurikomisaki = meisai.find("input[name=furikomisakiJouhou]");
		dialogRetKakeFlg = meisai.find("input[name=hontaiKakeFlg]");

		// 明細部
		dialogRetTorihikiName = meisai.find("input[name=torihikiName]");
		dialogRetShiwakeEdaNo = meisai.find("input[name=shiwakeEdaNo]");
		dialogRetUserId = meisai.find("input[name=userId]");
		dialogRetUserName = meisai.find("input[name=userName]");
		dialogRetShainNo = meisai.find("input[name=shainNo]");
		dialogRetKamokuCd = meisai.find("input[name=kamokuCd]");
		dialogRetKamokuName = meisai.find("input[name=kamokuName]");
		dialogRetKamokuEdabanCd = meisai.find("input[name=kamokuEdabanCd]");
		dialogRetKamokuEdabanName = meisai.find("input[name=kamokuEdabanName]");
		dialogRetKamokuEdabanSentakuButton = meisai.find("button[name=kamokuEdabanSentakuButton]");
		dialogRetFutanBumonCd = meisai.find("input[name=futanBumonCd]");
		dialogRetFutanBumonName = meisai.find("input[name=futanBumonName]");
		dialogRetFutanBumonSentakuButton = meisai.find("button[name=futanBumonSentakuButton]");
		dialogRetTorihikisakiCd = meisai.find("input[name=torihikisakiCd]");
		dialogRetTorihikisakiName = meisai.find("input[name=torihikisakiName]");
		dialogRetFurikomisaki = meisai.find("input[name=furikomisakiJouhou]");
		dialogRetTorihikisakiSentakuButton = meisai.find("button[name=torihikisakiSentakuButton]");
		dialogRetTorihikisakiClearButton = meisai.find("button[name=torihikisakiClearButton]");
		dialogRetProjectCd = meisai.find("input[name=projectCd]");
		dialogRetProjectName = meisai.find("input[name=projectName]");
		dialogRetProjectSentakuButton = meisai.find("button[name=projectSentakuButton]");
		dialogRetSegmentCd = meisai.find("input[name=segmentCd]");
		dialogRetSegmentName = meisai.find("input[name=segmentName]");
		dialogRetSegmentSentakuButton = meisai.find("button[name=segmentSentakuButton]");
		dialogRetKazeiKbn = meisai.find("select[name=kazeiKbn]");
		dialogRetZeiritsu = meisai.find("select[name=zeiritsu]");
		dialogRetKeigenZeiritsuKbn = meisai.find("input[name=keigenZeiritsuKbn]");
		dialogRetTekiyou = meisai.find("input[name=tekiyou]");
		dialogRetShoriGroup	= meisai.find("input[name=shoriGroup]");
		dialogRetKousaihiShousai = meisai.find("textarea[name=kousaihiShousai]");
		dialogRetKousaihiNinzuu = meisai.find("input[name=kousaihiNinzuu]");
		dialogRetKousaihiHitoriKingaku = meisai.find("input[name=kousaihiHitoriKingaku]");
		dialogRetKousaihiArea = meisai.find("div.kousaihi");
		dialogRetNinzuuArea = meisai.find("div.ninzuukoumoku");
		dialogRetKazeiFlg = meisai.find("input[name=kazeiFlg]");
		dialogRetJigyoushaKbn = meisai.find("[name=jigyoushaKbn]");
		dialogRetBunriKbn = meisai.find("select[name=bunriKbn]");
		dialogRetKariShiireKbn = meisai.find("select[name=kariShiireKbn]");
		<c:choose>
			<c:when test="${'UF1' == shainCdRenkeiArea}">
			dialogRetShainCd = meisai.find("input[name=uf1Cd]");
			dialogRetSahinName = meisai.find("input[name=uf1Name]");
			</c:when>
			<c:when test="${'UF2' == shainCdRenkeiArea}">
			dialogRetShainCd = meisai.find("input[name=uf2Cd]");
			dialogRetSahinName = meisai.find("input[name=uf2Name]");
			</c:when>
			<c:when test="${'UF3' == shainCdRenkeiArea}">
			dialogRetShainCd = meisai.find("input[name=uf3Cd]");
			dialogRetSahinName = meisai.find("input[name=uf3Name]");
			</c:when>
			<c:when test="${'UF4' == shainCdRenkeiArea}">
			dialogRetShainCd = meisai.find("input[name=uf4Cd]");
			dialogRetSahinName = meisai.find("input[name=uf4Name]");
			</c:when>
			<c:when test="${'UF5' == shainCdRenkeiArea}">
			dialogRetShainCd = meisai.find("input[name=uf5Cd]");
			dialogRetSahinName = meisai.find("input[name=uf5Name]");
			</c:when>
			<c:when test="${'UF6' == shainCdRenkeiArea}">
			dialogRetShainCd = meisai.find("input[name=uf6Cd]");
			dialogRetSahinName = meisai.find("input[name=uf6Name]");
			</c:when>
			<c:when test="${'UF7' == shainCdRenkeiArea}">
			dialogRetShainCd = meisai.find("input[name=uf7Cd]");
			dialogRetSahinName = meisai.find("input[name=uf7Name]");
			</c:when>
			<c:when test="${'UF8' == shainCdRenkeiArea}">
			dialogRetShainCd = meisai.find("input[name=uf8Cd]");
			dialogRetSahinName = meisai.find("input[name=uf8Name]");
			</c:when>
			<c:when test="${'UF9' == shainCdRenkeiArea}">
			dialogRetShainCd = meisai.find("input[name=uf9Cd]");
			dialogRetSahinName = meisai.find("input[name=uf9Name]");
			</c:when>
			<c:when test="${'UF10' == shainCdRenkeiArea}">
			dialogRetShainCd = meisai.find("input[name=uf10Cd]");
			dialogRetSahinName = meisai.find("input[name=uf10Name]");
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
			reloadJigyoushaKbn();
			bunriShiireSeigyo();
			jigyoushaKbnSeigyo($('#enableInput').val() == 'true');
			kingakuSaikeisan();
			if(meisai.find("input[name=shiwakeEdaNo]").val() == ""){
				meisai.find("input[name=shiharaiKingaku]").val("");
				meisai.find("input[name=zeinukiKingaku]").val("");
				meisai.find("input[name=shouhizeigaku]").val("");
			}
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
			reloadJigyoushaKbn();
			bunriShiireSeigyo();
			jigyoushaKbnSeigyo($('#enableInput').val() == 'true');
			kingakuSaikeisan();
			futanBumonCdLostFocus();
		};
		commonKamokuEdabanSentaku(meisai.find("[name=kamokuCd]").val(), denpyouKbn4DenpyouKanri, meisai.find("input[name=shiwakeEdaNo]").val());
	}

	/**
	 * 勘定科目枝番コードロストフォーカス時Function
	 */
	function kamokuEdabanCdLostFocus() {
		var meisai = $("#dialogMainDenpyouMeisai").find("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		var kamokuCd = meisai.find("input[name=kamokuCd]").val();
		dialogRetKamokuEdabanCd = meisai.find("input[name=kamokuEdabanCd]");
		dialogRetKamokuEdabanName = meisai.find("input[name=kamokuEdabanName]");
		dialogRetKazeiKbn = meisai.find("select[name=kazeiKbn]");
		dialogRetBunriKbn = meisai.find("select[name=bunriKbn]");
		dialogCallbackKanjyouKamokuEdabanSentaku = function(){
			displayZeiritsu();
			zeiShuseiBtnDisplay();
			reloadJigyoushaKbn();
			bunriShiireSeigyo();
			jigyoushaKbnSeigyo($('#enableInput').val() == 'true');
			kingakuSaikeisan();
			futanBumonCdLostFocus();
		};
		commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd,dialogRetKamokuEdabanName, kamokuCd, title,dialogRetKazeiKbn,dialogRetBunriKbn, denpyouKbn4DenpyouKanri, meisai.find("input[name=shiwakeEdaNo]").val());
	}

	/**
	 * 負担部門選択ボタン押下時Function
	 */
	function futanBumonSentaku() {
		var meisai = $("#dialogMainDenpyouMeisai").find("div.meisai");
		dialogRetFutanBumonCd = meisai.find("input[name=futanBumonCd]");
		dialogRetFutanBumonName = meisai.find("input[name=futanBumonName]");
		dialogRetKariShiireKbn = meisai.find("select[name=kariShiireKbn]");
		dialogCallbackFutanBumonSentaku = function(){ };
		commonFutanBumonSentaku("1", meisai.find("[name=kamokuCd]").val(), $("input[name=denpyouId]").val(), $("input[name=kihyouBumonCd]").val(), denpyouKbn4DenpyouKanri, meisai.find("input[name=shiwakeEdaNo]").val());
	}

	/**
	 * 負担部門コードロストフォーカス時Function
	 */
	function futanBumonCdLostFocus() {
		var meisai = $("#dialogMainDenpyouMeisai").find("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetFutanBumonCd = meisai.find("input[name=futanBumonCd]");
		dialogRetFutanBumonName = meisai.find("input[name=futanBumonName]");
		dialogRetKariShiireKbn = meisai.find("[name=kariShiireKbn]");
		dialogCallbackFutanBumonSentaku = function(){ };
		commonFutanBumonCdLostFocus("1", dialogRetFutanBumonCd,dialogRetFutanBumonName, title, $("input[name=denpyouId]").val(), $("input[name=kihyouBumonCd]").val(), meisai.find("[name=kamokuCd]").val(), dialogRetKariShiireKbn, denpyouKbn4DenpyouKanri, meisai.find("input[name=shiwakeEdaNo]").val());
	}

	//取引先選択ボタン押下時、ダイアログ表示
	function torihikisakiSentaku() {
		var meisai = $(this).closest("div.meisai");
		dialogRetTorihikisakiCd = meisai.find("[name=torihikisakiCd]");
		dialogRetTorihikisakiName = meisai.find("[name=torihikisakiName]");
		dialogRetFurikomisaki = meisai.find("[name=furikomisakiJouhou]");
		dialogRetJigyoushaKbn = meisai.find("[name=jigyoushaKbn]");
		commonTorihikisakiSentaku(denpyouKbn4DenpyouKanri);
		dialogCallbackTorihikisakiSentaku = function(){
			meisai.find("[name=menzeiJigyoushaFlg]").val(dialogRetJigyoushaKbn.val());
			jigyoushaKbnSeigyo($('#enableInput').val() == 'true');
			kingakuSaikeisan();
		};
	}

	//取引先クリアボタン押下時、取引先クリア
	function torihikisakiClear() {
		var meisai = $(this).closest("div.meisai");
		meisai.find("[name=torihikisakiCd]").val("");
		meisai.find("[name=torihikisakiName]").val("");
		meisai.find("[name=furikomisakiJouhou]").val("");
	}

	//取引先コードロストフォーカス時、取引先名称表示
	function torihikisakiCdLostFocus() {
		var meisai = $(this).closest("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetTorihikisakiCd = meisai.find("[name=torihikisakiCd]");
		dialogRetTorihikisakiName = meisai.find("[name=torihikisakiName]");
		dialogRetFurikomisaki = meisai.find("[name=furikomisakiJouhou]");
		dialogRetJigyoushaKbn = meisai.find("[name=jigyoushaKbn]");
		commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd,dialogRetTorihikisakiName, title, dialogRetFurikomisaki, denpyouKbn4DenpyouKanri,dialogRetJigyoushaKbn);
		dialogCallbackTorihikisakiSentaku = function(){
			meisai.find("[name=menzeiJigyoushaFlg]").val(dialogRetJigyoushaKbn.val());
			jigyoushaKbnSeigyo($('#enableInput').val() == 'true');
			kingakuSaikeisan();
		};
	}
	
	//事業者区分だけマスタから再読み込み(commonTorihikisakiCdLostFocus()を使用)
	function reloadJigyoushaKbn() {
		var meisai = $("#dialogMainDenpyouMeisai").find("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetTorihikisakiCd = meisai.find("[name=torihikisakiCd]");
		dialogRetTorihikisakiName = meisai.find("[name=torihikisakiName]");
		dialogRetFurikomisaki = null;
		dialogRetJigyoushaKbn = meisai.find("[name=jigyoushaKbn]");
		commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd,dialogRetTorihikisakiName, title, dialogRetFurikomisaki, denpyouKbn4DenpyouKanri,dialogRetJigyoushaKbn);
		dialogCallbackTorihikisakiSentaku = function(){
			//以下 処理順序的にdialogCallbackTorihikisakiSentakuで必要（無いと事業者区分がおかしくなる）
			meisai.find("[name=menzeiJigyoushaFlg]").val(dialogRetJigyoushaKbn.val());
			jigyoushaKbnSeigyo($('#enableInput').val() == 'true');
			kingakuSaikeisan();
		};
	}
	
	/**
	 * プロジェクト選択ボタン押下時Function
	 */
	function pjSentaku() {
		var meisai = $(this).closest("div.meisai");
		dialogRetProjectCd = meisai.find("input[name=projectCd]");
		dialogRetProjectName = meisai.find("input[name=projectName]");
		commonProjectSentaku();
	}

	/**
	 * プロジェクトコードロストフォーカス時Function
	 */
	function pjCdLostFocus() {
		var meisai = $(this).closest("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetProjectCd = meisai.find("input[name=projectCd]");
		dialogRetProjectName = meisai.find("input[name=projectName]");
		commonProjectCdLostFocus(dialogRetProjectCd, dialogRetProjectName,
				title);
	}

	/**
	 * セグメント選択ボタン押下時Function
	 */
	function segmentSentaku() {
		var meisai = $(this).closest("div.meisai");
		dialogRetSegmentCd = meisai.find("input[name=segmentCd]");
		dialogRetSegmentName = meisai.find("input[name=segmentName]");
		commonSegmentSentaku();
	}

	/**
	 * セグメントコードロストフォーカス時Function
	 */
	function segmentCdLostFocus() {
		var meisai = $(this).closest("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetSegmentCd = meisai.find("input[name=segmentCd]");
		dialogRetSegmentName = meisai.find("input[name=segmentName]");
		commonSegmentCdLostFocus(dialogRetSegmentCd, dialogRetSegmentName,
				title);
	}

	/**
	 * ユニバーサルフィールド１選択ボタン押下時Function
	 */
	function uf1Sentaku() {
		var meisai = $(this).closest("div.meisai");
		dialogRetUniversalFieldCd = meisai.find("input[name=uf1Cd]");
		dialogRetUniversalFieldName = meisai.find("input[name=uf1Name]");
		commonUniversalSentaku(meisai.find("[name=kamokuCd]").val(), "1");
	}

	/**
	 * ユニバーサルフィールド１クリアボタン押下時Function
	 */
	function uf1Clear() {
		var meisai = $(this).closest("div.meisai");
		meisai.find("input[name=uf1Cd]").val("");
		meisai.find("input[name=uf1Name]").val("");
	}

	/**
	 * ユニバーサルフィールド１コードロストフォーカス時Function
	 */
	function uf1CdLostFocus() {
		var meisai = $(this).closest("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = meisai.find("button[name=uf1SentakuButton]").is(
				":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd = meisai.find("input[name=uf1Cd]");
			dialogRetUniversalFieldName = meisai.find("input[name=uf1Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd,
					dialogRetUniversalFieldName, 1, title);
		}
	}

	/**
	 * ユニバーサルフィールド２選択ボタン押下時Function
	 */
	function uf2Sentaku() {
		var meisai = $(this).closest("div.meisai");
		dialogRetUniversalFieldCd = meisai.find("input[name=uf2Cd]");
		dialogRetUniversalFieldName = meisai.find("input[name=uf2Name]");
		commonUniversalSentaku(meisai.find("[name=kamokuCd]").val(), 2);
	}

	/**
	 * ユニバーサルフィールド２クリアボタン押下時Function
	 */
	function uf2Clear() {
		var meisai = $(this).closest("div.meisai");
		meisai.find("input[name=uf2Cd]").val("");
		meisai.find("input[name=uf2Name]").val("");
	}

	/**
	 * ユニバーサルフィールド２コードロストフォーカス時Function
	 */
	function uf2CdLostFocus() {
		var meisai = $(this).closest("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = meisai.find("button[name=uf2SentakuButton]").is(
				":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd = meisai.find("input[name=uf2Cd]");
			dialogRetUniversalFieldName = meisai.find("input[name=uf2Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd,
					dialogRetUniversalFieldName, 2, title);
		}
	}

	/**
	 * ユニバーサルフィールド３選択ボタン押下時Function
	 */
	function uf3Sentaku() {
		var meisai = $(this).closest("div.meisai");
		dialogRetUniversalFieldCd = meisai.find("input[name=uf3Cd]");
		dialogRetUniversalFieldName = meisai.find("input[name=uf3Name]");
		commonUniversalSentaku(meisai.find("[name=kamokuCd]").val(), 3);
	}

	/**
	 * ユニバーサルフィールド３クリアボタン押下時Function
	 */
	function uf3Clear() {
		var meisai = $(this).closest("div.meisai");
		meisai.find("input[name=uf3Cd]").val("");
		meisai.find("input[name=uf3Name]").val("");
	}

	/**
	 * ユニバーサルフィールド３コードロストフォーカス時Function
	 */
	function uf3CdLostFocus() {
		var meisai = $(this).closest("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = meisai.find("button[name=uf3SentakuButton]").is(
				":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd = meisai.find("input[name=uf3Cd]");
			dialogRetUniversalFieldName = meisai.find("input[name=uf3Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd,
					dialogRetUniversalFieldName, 3, title);
		}
	}

	/**
	 * ユニバーサルフィールド４選択ボタン押下時Function
	 */
	function uf4Sentaku() {
		var meisai = $(this).closest("div.meisai");
		dialogRetUniversalFieldCd = meisai.find("input[name=uf4Cd]");
		dialogRetUniversalFieldName = meisai.find("input[name=uf4Name]");
		commonUniversalSentaku(meisai.find("[name=kamokuCd]").val(), 4);
	}

	/**
	 * ユニバーサルフィールド４クリアボタン押下時Function
	 */
	function uf4Clear() {
		var meisai = $(this).closest("div.meisai");
		meisai.find("input[name=uf4Cd]").val("");
		meisai.find("input[name=uf4Name]").val("");
	}

	/**
	 * ユニバーサルフィールド４コードロストフォーカス時Function
	 */
	function uf4CdLostFocus() {
		var meisai = $(this).closest("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = meisai.find("button[name=uf4SentakuButton]").is(
				":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd = meisai.find("input[name=uf4Cd]");
			dialogRetUniversalFieldName = meisai.find("input[name=uf4Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd,
					dialogRetUniversalFieldName, 4, title);
		}
	}

	/**
	 * ユニバーサルフィールド５選択ボタン押下時Function
	 */
	function uf5Sentaku() {
		var meisai = $(this).closest("div.meisai");
		dialogRetUniversalFieldCd = meisai.find("input[name=uf5Cd]");
		dialogRetUniversalFieldName = meisai.find("input[name=uf5Name]");
		commonUniversalSentaku(meisai.find("[name=kamokuCd]").val(), 5);
	}

	/**
	 * ユニバーサルフィールド５クリアボタン押下時Function
	 */
	function uf5Clear() {
		var meisai = $(this).closest("div.meisai");
		meisai.find("input[name=uf5Cd]").val("");
		meisai.find("input[name=uf5Name]").val("");
	}

	/**
	 * ユニバーサルフィールド５コードロストフォーカス時Function
	 */
	function uf5CdLostFocus() {
		var meisai = $(this).closest("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = meisai.find("button[name=uf5SentakuButton]").is(
				":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd = meisai.find("input[name=uf5Cd]");
			dialogRetUniversalFieldName = meisai.find("input[name=uf5Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd,
					dialogRetUniversalFieldName, 5, title);
		}
	}

	/**
	 * ユニバーサルフィールド６選択ボタン押下時Function
	 */
	function uf6Sentaku() {
		var meisai = $(this).closest("div.meisai");
		dialogRetUniversalFieldCd = meisai.find("input[name=uf6Cd]");
		dialogRetUniversalFieldName = meisai.find("input[name=uf6Name]");
		commonUniversalSentaku(meisai.find("[name=kamokuCd]").val(), 6);
	}

	/**
	 * ユニバーサルフィールド６クリアボタン押下時Function
	 */
	function uf6Clear() {
		var meisai = $(this).closest("div.meisai");
		meisai.find("input[name=uf6Cd]").val("");
		meisai.find("input[name=uf6Name]").val("");
	}

	/**
	 * ユニバーサルフィールド６コードロストフォーカス時Function
	 */
	function uf6CdLostFocus() {
		var meisai = $(this).closest("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = meisai.find("button[name=uf6SentakuButton]").is(
				":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd = meisai.find("input[name=uf6Cd]");
			dialogRetUniversalFieldName = meisai.find("input[name=uf6Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd,
					dialogRetUniversalFieldName, 6, title);
		}
	}

	/**
	 * ユニバーサルフィールド７選択ボタン押下時Function
	 */
	function uf7Sentaku() {
		var meisai = $(this).closest("div.meisai");
		dialogRetUniversalFieldCd = meisai.find("input[name=uf7Cd]");
		dialogRetUniversalFieldName = meisai.find("input[name=uf7Name]");
		commonUniversalSentaku(meisai.find("[name=kamokuCd]").val(), 7);
	}

	/**
	 * ユニバーサルフィールド７クリアボタン押下時Function
	 */
	function uf7Clear() {
		var meisai = $(this).closest("div.meisai");
		meisai.find("input[name=uf7Cd]").val("");
		meisai.find("input[name=uf7Name]").val("");
	}

	/**
	 * ユニバーサルフィールド７コードロストフォーカス時Function
	 */
	function uf7CdLostFocus() {
		var meisai = $(this).closest("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = meisai.find("button[name=uf7SentakuButton]").is(
				":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd = meisai.find("input[name=uf7Cd]");
			dialogRetUniversalFieldName = meisai.find("input[name=uf7Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd,
					dialogRetUniversalFieldName, 7, title);
		}
	}

	/**
	 * ユニバーサルフィールド８選択ボタン押下時Function
	 */
	function uf8Sentaku() {
		var meisai = $(this).closest("div.meisai");
		dialogRetUniversalFieldCd = meisai.find("input[name=uf8Cd]");
		dialogRetUniversalFieldName = meisai.find("input[name=uf8Name]");
		commonUniversalSentaku(meisai.find("[name=kamokuCd]").val(), 8);
	}

	/**
	 * ユニバーサルフィールド８クリアボタン押下時Function
	 */
	function uf8Clear() {
		var meisai = $(this).closest("div.meisai");
		meisai.find("input[name=uf8Cd]").val("");
		meisai.find("input[name=uf8Name]").val("");
	}

	/**
	 * ユニバーサルフィールド８コードロストフォーカス時Function
	 */
	function uf8CdLostFocus() {
		var meisai = $(this).closest("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = meisai.find("button[name=uf8SentakuButton]").is(
				":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd = meisai.find("input[name=uf8Cd]");
			dialogRetUniversalFieldName = meisai.find("input[name=uf8Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd,
					dialogRetUniversalFieldName, 8, title);
		}
	}

	/**
	 * ユニバーサルフィールド９選択ボタン押下時Function
	 */
	function uf9Sentaku() {
		var meisai = $(this).closest("div.meisai");
		dialogRetUniversalFieldCd = meisai.find("input[name=uf9Cd]");
		dialogRetUniversalFieldName = meisai.find("input[name=uf9Name]");
		commonUniversalSentaku(meisai.find("[name=kamokuCd]").val(), 9);
	}

	/**
	 * ユニバーサルフィールド９クリアボタン押下時Function
	 */
	function uf9Clear() {
		var meisai = $(this).closest("div.meisai");
		meisai.find("input[name=uf9Cd]").val("");
		meisai.find("input[name=uf9Name]").val("");
	}

	/**
	 * ユニバーサルフィールド９コードロストフォーカス時Function
	 */
	function uf9CdLostFocus() {
		var meisai = $(this).closest("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = meisai.find("button[name=uf9SentakuButton]").is(
				":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd = meisai.find("input[name=uf9Cd]");
			dialogRetUniversalFieldName = meisai.find("input[name=uf9Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd,
					dialogRetUniversalFieldName, 9, title);
		}
	}

	/**
	 * ユニバーサルフィールド１０選択ボタン押下時Function
	 */
	function uf10Sentaku() {
		var meisai = $(this).closest("div.meisai");
		dialogRetUniversalFieldCd = meisai.find("input[name=uf10Cd]");
		dialogRetUniversalFieldName = meisai.find("input[name=uf10Name]");
		commonUniversalSentaku(meisai.find("[name=kamokuCd]").val(), 10);
	}

	/**
	 * ユニバーサルフィールド１０クリアボタン押下時Function
	 */
	function uf10Clear() {
		var meisai = $(this).closest("div.meisai");
		meisai.find("input[name=uf10Cd]").val("");
		meisai.find("input[name=uf10Name]").val("");
	}

	/**
	 * ユニバーサルフィールド１０コードロストフォーカス時Function
	 */
	function uf10CdLostFocus() {
		var meisai = $(this).closest("div.meisai");
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = meisai.find("button[name=uf10SentakuButton]").is(
				":visible");
		if (isBtnVisible) {
			dialogRetUniversalFieldCd = meisai.find("input[name=uf10Cd]");
			dialogRetUniversalFieldName = meisai.find("input[name=uf10Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd,
					dialogRetUniversalFieldName, 10, title);
		}
	}

	//幣種選択ボタン押下時Function
	$("#heishuSentakuButton").click(function(e) {
		var meisai = $("#dialogMainDenpyouMeisai").find("div.meisai");
		dialogRetHeishuCd = meisai.find("input[name=heishu]");
		dialogRetTsuukaTani = meisai.find("#tuukatani");
		dialogRetRate = meisai.find("input[name=rate]");
		commonHeishuSentaku();
		dialogCallbackHeishuSentaku = function() {
			calcGaikaMoney();
		};
	});

	//幣種コードロストフォーカス時Function
	$("input[name=heishu]").blur(function() {
				var meisai = $("#dialogMainDenpyouMeisai").find("div.meisai");
				var title = $(this).parent().parent().find(".control-label")
						.text();
				dialogRetHeishuCd = meisai.find("input[name=heishu]");
				dialogRetTsuukaTani = meisai.find("#tuukatani");
				dialogRetRate = meisai.find("input[name=rate]");
				commonHeishuCdLostFocus(dialogRetHeishuCd, dialogRetTsuukaTani,
						dialogRetRate, title);
				dialogCallbackHeishuSentaku = function() {
					calcGaikaMoney();
				};
			});

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


	//出張区分による表示変更Function
	function kaigaiSentaku(isEnable) {
		var meisai = $("#dialogMainDenpyouMeisai");
		var kaigaiFlg = meisai.find("input[name=kaigaiFlg]:checked").val();
		if (kaigaiFlg == '1') {
			//事業者区分の行丸ごと隠す
			meisai.find("#jigyoushaKbnDiv").hide();
			meisai.find("input[name=heishu]").parent().parent().show();
			meisai.find("input[name=gaika]").parent().parent().show();
			if (isEnable) {
				//海外分の場合、取引で税率の指定ができないため常に変更可能
				meisai.find("select[name=zeiritsu]").prop("disabled",false);
				if(meisai.find("input[name=gaika]").length > 0){
					meisai.find("input[name=shiharaiKingaku]").prop("disabled",	true);
				}
			}
		} else {
			var kazeiKbnGroup = meisai.find("select[name=kazeiKbn] option:selected").attr("data-kazeiKbnGroup");
			meisai.find("#kazeiDiv").show();
			// 事業者区分表示は海外出張旅費のみ
            if($("input[name=denpyouKbn]").val() == "A011" && $("#denpyouJouhou").find("select[name=invoiceDenpyou]").val() == "0") {
				meisai.find("#jigyoushaKbnDiv").show();
			}
			meisai.find("input[name=heishu]").parent().parent().hide();
			meisai.find("input[name=gaika]").parent().parent().hide();
			meisai.find("input[name=heishu]").val("");
			meisai.find("input[name=rate]").val("");
			meisai.find("input[name=gaika]").val("");
			meisai.find("#tuukatani").text("");
			var himodukeCard = meisai.find("input[name=himodukeCardMeisaiKeihi]").val();
			if (isEnable && himodukeCard == "" && $("#kaikeiContent").find("select[name=nyuryokuHoushiki] :selected").val() != "1") {
				meisai.find("input[name=shiharaiKingaku]").prop("disabled",	false);
			}
		}
		if(meisai.find("input[name=shiharaiKingaku]").prop("disabled")){
			delCalculator(meisai.find("input[name=shiharaiKingaku]"));		
		}else{
			addCalculator(meisai.find("input[name=shiharaiKingaku]"));
		}
	}
	//出張区分による表示変更Function
	function kaigaiSentakuChange() {
		kaigaiSentaku(true);
		var meisai = $("#dialogMainDenpyouMeisai");
		meisai.find("input[name=shiwakeEdaNo]").val("");
		meisai.find("input[name=torihikiName]").val("");
		meisai.find("input[name=kamokuCd]").val("");
		meisai.find("input[name=kamokuName]").val("");
		meisai.find("input[name=kamokuEdabanCd]").val("");
		meisai.find("input[name=kamokuEdabanName]").val("");
		meisai.find("input[name=futanBumonCd]").val("");
		meisai.find("input[name=futanBumonName]").val("");
		meisai.find("input[name=torihikisakiCd]").val("");
		meisai.find("input[name=torihikisakiName]").val("");
		meisai.find("input[name=furikomisakiJouhou]").val("");
		meisai.find("input[name=projectCd]").val("");
		meisai.find("input[name=projectName]").val("");
		meisai.find("input[name=tekiyou]").val("");
		meisai.find("input[name=kousaihiShousai]").val("");
		meisai.find("input[name=kousaihiNinzuu]").val("");
		meisai.find("input[name=kousaihiHitoriKingaku]").val("");
		meisai.find("select[name=kazeiKbn]").val("");
		meisai.find("select[name=zeiritsu]").val(""+ meisai.find("select[name=zeiritsu]").prop("selectedIndex",0).val() +""); //税率はリストの最初の値に戻す
		if (meisai.find("input[name=kaigaiFlg]:checked").val() == '1' && meisai.find("input[name=gaika]").length > 0) {
			meisai.find("input[name=shiharaiKingaku]").val("");
		}
		bunriShiireSeigyo();
		displayZeiritsu();
		zeiShuseiBtnDisplay();
		shiharaiKingakuShusei();
	}

	/**
	 * 外貨入力による計算、単価を計算するFunction
	 */
	function calcGaikaMoney() {
		var target = $("#dialogMainDenpyouMeisai");
		var regInt = /^[0-9]+$/;
		var regDeci = /^[0-9]+\.[0-9]+$/;
		var gaikaVal = target.find("input[name=gaika]").val().replaceAll(",",
				"");
		var rateVal = target.find("input[name=rate]").val();

		if (gaikaVal != "" || rateVal != "") {
			//外貨　決定：単価入力値が金額ならそれ。そうでなければ０。
			gaikaVal = gaikaVal.match(regInt) != null
					|| gaikaVal.match(regDeci) != null ? new Decimal(gaikaVal)
					: new Decimal(0);

			//レート　決定：単価入力値が金額ならそれ。そうでなければ１。
			rateVal = rateVal.match(regInt) != null
					|| rateVal.match(regDeci) != null ? new Decimal(rateVal)
					: new Decimal(1);

			// 邦貨換算端数処理
			var kingaku = gaikaVal.times(rateVal);
			<c:choose>
			<c:when test="${houkaKansanHansuu eq '0'}">
			// 切り捨て
			kingaku = kingaku.floor();
			</c:when>
			<c:when test="${houkaKansanHansuu eq '1'}">
			// 切り上げ
			kingaku = kingaku.ceil();
			</c:when>
			<c:when test="${houkaKansanHansuu eq '2'}">
			// 四捨五入
			kingaku = kingaku.round();
			</c:when>
			</c:choose>
			//外貨×レートを単価にセット
			if (kingaku.comparedTo(MAX_DECIMAL) == 1)
				kingaku = MAX_DECIMAL;
			target.find("input[name=shiharaiKingaku]").val(kingaku.toString().formatMoney());
			target.find("input[name=hontaiKingaku]").val(kingaku.toString().formatMoney());
			target.find("input[name=shouhizeigaku]").val("0");

			//外貨計算後に消費税額修正
			shiharaiKingakuShusei();
			//交際費関連項目も更新
			calcKousaihiHitoriKingaku();

		}
	}

	/**
	 * 交際費一人当たりの金額を計算するFunction
	 */
	function calcKousaihiHitoriKingaku() {
		//交際費フラグオフなら何もしない
		var target = $("#dialogMainDenpyouMeisai");
		if ('none' == target.find("div.kousaihi").css("display"))
			return;

		var regInt = /^[0-9]+$/;
		var kingakuVal = target.find("input[name=shiharaiKingaku]").val().replaceAll(",", "");
		var ninzuVal = target.find("input[name=kousaihiNinzuu]").val();

		// 金額÷人数 算出して一人当たりの金額欄に出力(どちらか未入力か0ならブランク)
		if (kingakuVal != "" || ninzuVal != "") {
			kingakuVal = kingakuVal.match(regInt) != null ? new Decimal(kingakuVal) : new Decimal(0);
			ninzuVal = ninzuVal.match(regInt) != null ? new Decimal(ninzuVal) : new Decimal(0);
			if (kingakuVal != 0 && ninzuVal != 0) {
				var kingaku = new Decimal(kingakuVal / ninzuVal);
				//小数点以下は切り捨て
				kingaku = kingaku.floor();
				target.find("input[name=kousaihiHitoriKingaku]").val(kingaku.toString().formatMoney());
			} else {
				target.find("input[name=kousaihiHitoriKingaku]").val("");
			}
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
* 消費税額を手入力で修正した際に自動で再計算
*/
function shouhizeigakuShusei(){
	var target = $("#dialogMainDenpyouMeisai");
	var shouhizeigaku			= target.find("input[name=shouhizeigaku]").val().replaceAll(",", "");
	var nyuryokuFlg				= $("#kaikeiContent").find("select[name=nyuryokuHoushiki] :selected").val();
	var shiharaiKingaku			= target.find("input[name=shiharaiKingaku]").val().replaceAll(",", "");
	var shiireKeikaSothiFlg		= $("#workflowForm").find("input[name=shiirezeigakuKeikasothi]").val();
	//仕入経過措置フラグの分岐はなし
	if(nyuryokuFlg == "1"){
		zeinukiKingakuShusei();
	}else{
		var dShiharaiKingaku = new Decimal(Number(shiharaiKingaku));
		var dShouhizeigaku = new Decimal(Number(shouhizeigaku));
		target.find("input[name=hontaiKingaku]").val(dShiharaiKingaku.sub(dShouhizeigaku).toString().formatMoney());
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
	if(target.find("input[name=shiharaiKingaku]").val() == undefined){
		return; //エラー避け
	}
	let shiharaiKingaku = Number(target.find("input[name=shiharaiKingaku]").val().replaceAll(",", ""));
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
	let dShiharaiKingaku = new Decimal(shiharaiKingaku);
	let dJigyoushaNum = new Decimal(jigyoushaNum);
	let dBunbo = new Decimal(100).add(dZeiritsu);
	//仕入経過措置フラグの分岐はなし
	//計算用
	let shouhizeigaku = hasuuKeisanZaimuFromImporter(dShiharaiKingaku.mul(dZeiritsu).div(dBunbo).mul(dJigyoushaNum), hasuuShoriFlg, false);
	let hontaiKingaku = dShiharaiKingaku.sub(shouhizeigaku);
	//税率0なら0円の分岐は残しておく
	/*
	if(zeiritsu != "0"){
		shouhizeigaku = hasuuKeisanZaimuFromImporter(dShiharaiKingaku.mul(dZeiritsu).div(dBunbo).mul(dJigyoushaNum), hasuuShoriFlg, false);
		hontaiKingaku	= dShiharaiKingaku.sub(shouhizeigaku);
	}
	*/
	
	//計算した本体金額、消費税額の値をセット or クリア
	target.find("input[name=hontaiKingaku]").val(hontaiKingaku.toString().formatMoney());
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
	var hontaiKingaku		= target.find("input[name=hontaiKingaku]").val().replaceAll(",", "");
	var hasuuShoriFlg		= $("#workflowForm").find("input[name=hasuuShoriFlg]").val();
	var shiireKeikaSothiFlg	= $("#workflowForm").find("input[name=shiirezeigakuKeikasothi]").val();
	
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
 * 支払先名を摘要の後ろに付け足す
 * フォーカスアウトする度に付け足されることになるのは仕様
 */
function tekiyouFukusha(){
	var meisai = $(this).closest("div.meisai");
	var shiharaisakiStr = meisai.find("input[name=shiharaisakiName]").val();
	if(shiharaisakiStr.length > 0){
		var tekiyouStr = meisai.find("input[name=tekiyou]").val();
		meisai.find("input[name=tekiyou]").val(tekiyouStr+"　"+shiharaisakiStr);
	}
}

//金額を再計算する場合に、税込入力or税抜入力で計算方法を変える
function kingakuSaikeisan(){
	if($("#kaikeiContent").find("select[name=nyuryokuHoushiki] option:selected").val() == "0" || 
			["A001","A004","A011"].includes($("[name=denpyouKbn]").val())){
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
	target.find("button[name=userSentakuButton]").click(userSentaku);
	target.find("button[name=torihikiSentakuButton]").click(torihikiSentaku);
	target.find("button[name=kamokuEdabanSentakuButton]").click(kamokuEdabanSentaku);
	target.find("button[name=futanBumonSentakuButton]").click(futanBumonSentaku);
	target.find("button[name=torihikisakiSentakuButton]").click(torihikisakiSentaku);
	target.find("button[name=torihikisakiClearButton]").click(torihikisakiClear);
	target.find("button[name=uf1SentakuButton]").click(uf1Sentaku);
	target.find("button[name=uf2SentakuButton]").click(uf2Sentaku);
	target.find("button[name=uf3SentakuButton]").click(uf3Sentaku);
	target.find("button[name=uf4SentakuButton]").click(uf4Sentaku);
	target.find("button[name=uf5SentakuButton]").click(uf5Sentaku);
	target.find("button[name=uf6SentakuButton]").click(uf6Sentaku);
	target.find("button[name=uf7SentakuButton]").click(uf7Sentaku);
	target.find("button[name=uf8SentakuButton]").click(uf8Sentaku);
	target.find("button[name=uf9SentakuButton]").click(uf9Sentaku);
	target.find("button[name=uf10SentakuButton]").click(uf10Sentaku);
	target.find("button[name=uf1ClearButton]").click(uf1Clear);
	target.find("button[name=uf2ClearButton]").click(uf2Clear);
	target.find("button[name=uf3ClearButton]").click(uf3Clear);
	target.find("button[name=uf4ClearButton]").click(uf4Clear);
	target.find("button[name=uf5ClearButton]").click(uf5Clear);
	target.find("button[name=uf6ClearButton]").click(uf6Clear);
	target.find("button[name=uf7ClearButton]").click(uf7Clear);
	target.find("button[name=uf8ClearButton]").click(uf8Clear);
	target.find("button[name=uf9ClearButton]").click(uf9Clear);
	target.find("button[name=uf10ClearButton]").click(uf10Clear);
	target.find("button[name=projectSentakuButton]").click(pjSentaku);
	target.find("button[name=segmentSentakuButton]").click(segmentSentaku);

	target.find("input[name=kaigaiFlg]").click(kaigaiSentakuChange);
	target.find("input[name=shainNo]").change(shaiNoLostFocus);
	target.find("input[name=shiwakeEdaNo]").change(shiwakeEdaNoLostFocus);
	target.find("input[name=kamokuEdabanCd]").blur(kamokuEdabanCdLostFocus);
	target.find("input[name=futanBumonCd]").blur(futanBumonCdLostFocus);
	target.find("input[name=torihikisakiCd]").blur(torihikisakiCdLostFocus);
	target.find("input[name=projectCd]").blur(pjCdLostFocus);
	target.find("input[name=segmentCd]").blur(segmentCdLostFocus);
	target.find("input[name=uf1Cd]").blur(uf1CdLostFocus);
	target.find("input[name=uf2Cd]").blur(uf2CdLostFocus);
	target.find("input[name=uf3Cd]").blur(uf3CdLostFocus);
	target.find("input[name=uf4Cd]").blur(uf4CdLostFocus);
	target.find("input[name=uf5Cd]").blur(uf5CdLostFocus);
	target.find("input[name=uf6Cd]").blur(uf6CdLostFocus);
	target.find("input[name=uf7Cd]").blur(uf7CdLostFocus);
	target.find("input[name=uf8Cd]").blur(uf8CdLostFocus);
	target.find("input[name=uf9Cd]").blur(uf9CdLostFocus);
	target.find("input[name=uf10Cd]").blur(uf10CdLostFocus);

	//レートによる外貨の計算
	target.find("input[name=gaika]").blur(calcGaikaMoney);
	target.find("input[name=rate]").blur(calcGaikaMoney);

	//交際費一人当たりの金額の計算
	target.find("input[name=shiharaiKingaku]").blur(calcKousaihiHitoriKingaku);
	target.find("input[name=kousaihiNinzuu]").blur(calcKousaihiHitoriKingaku);

	target.find("input[name=houjinCardFlgKeihi]").click(checkboxSeigyo);
	target.find("input[name=kaishaTehaiFlgKeihi]").click(checkboxSeigyo);
	
	//支払先名から摘要へ複写
	target.find("input[name=shiharaisakiName]").blur(tekiyouFukusha);
	//各種金額 手入力で修正した際に自動で再計算
	target.find("input[name=shouhizeigaku]").blur(shouhizeigakuShusei);
	target.find("input[name=shiharaiKingaku]").blur(shiharaiKingakuShusei);
	target.find("input[name=hontaiKingaku]").blur(zeinukiKingakuShusei);
	
	target.find("input[name=jigyoushaKbn]").change(kingakuSaikeisan);
	target.find("select[name=zeiritsu]").change(kingakuSaikeisan);
}
</script>