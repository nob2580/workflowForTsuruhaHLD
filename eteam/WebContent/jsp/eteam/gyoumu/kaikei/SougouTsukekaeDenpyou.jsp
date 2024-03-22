<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- 中身 -->
<div id='kaikeiContent' class='form-horizontal'>

	<!-- 申請内容 -->
	<section class='print-unit'>
		<h2>申請内容</h2>
		<div>
			<div class='control-group'>
				<label class='control-label' <c:if test='${not ks.denpyouDate.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.denpyouDate.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.denpyouDate.name)}</label>
				<div class='controls'>
					<input type='text' name='denpyouDate' class='input-small datepicker' value='${su:htmlEscape(denpyouDate)}'> <label class='label' <c:if test='${not ks.shouhyouShoruiFlg.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shouhyouShoruiFlg.hissuFlg}'>
							<span class='required'>*</span>
						</c:if>${su:htmlEscape(ks.shouhyouShoruiFlg.name)}</label> <select name='shouhyouShoruiFlg' class='input-small' <c:if test='${not ks.shouhyouShoruiFlg.hyoujiFlg}'>style='display:none;'</c:if>>
						<c:forEach var='shouhyouShoruiList' items='${shouhyouShoruiList}'>
							<option value='${su:htmlEscape(shouhyouShoruiList.naibu_cd)}' <c:if test='${shouhyouShoruiList.naibu_cd eq shouhyouShoruiFlg}'>selected</c:if>>${su:htmlEscape(shouhyouShoruiList.name)}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class='control-group'>
				<label class='control-label' <c:if test='${not ks.zeiritsu.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.zeiritsu.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.zeiritsu.name)}</label>
				<div class='controls'>
					<select name='zeiritsu' class='input-small'>
						<c:forEach var='zeiritsuList' items='${zeiritsuList}'>
							<option value='${su:htmlEscape(zeiritsuList.zeiritsu)}' data-hasuuKeisanKbn='${zeiritsuList.hasuu_keisan_kbn}' data-keigenZeiritsuKbn='${zeiritsuList.keigen_zeiritsu_kbn}' <c:if test='${zeiritsuList.zeiritsu eq zeiritsu && zeiritsuList.keigen_zeiritsu_kbn eq keigenZeiritsuKbn}'>selected</c:if>><c:if test='${zeiritsuList.keigen_zeiritsu_kbn eq "1"}'>*</c:if>${su:htmlEscape(zeiritsuList.zeiritsu)}%</option>
						</c:forEach>
					</select>
				<input type='hidden' name='keigenZeiritsuKbn' value='${su:htmlEscape(keigenZeiritsuKbn)}'>
				</div>
			</div>
			<div class='control-group'>
				<label class='control-label'>付替区分</label>
				<div class='controls'>
					<c:forEach var='tsukekaeKbnList' items='${tsukekaeKbnList}'>
						<label class='radio inline'> <input type='radio' name='tsukekaeKbn' value='${su:htmlEscape(tsukekaeKbnList.naibu_cd)}' <c:if test='${tsukekaeKbnList.naibu_cd eq tsukekaeKbn}'>checked</c:if>> ${su:htmlEscape(tsukekaeKbnList.name)}
						</label>
					</c:forEach>
				</div>
			</div>
			<%@ include file="./kogamen/HeaderField.jsp"%>
		</div>
	</section>

	<!-- 付替元 -->
	<section>
		<h2>
			付替元：<span class='motoTaishaku'></span>
		</h2>
		<div>
			<div class='control-group' <c:if test='${not ks.kamoku.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.kamoku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kamoku.name)}</label>
				<div class='controls'>
					<input type='text' name='motoKamokuCd' class='input-small pc_only' value='${su:htmlEscape(motoKamokuCd)}'> <input type='text' name='motoKamokuName' class='input-xlarge' value='${su:htmlEscape(motoKamokuName)}' disabled>
					<button type='button' id='motoKamokuSentakuButton' class='btn btn-small'>選択</button>
					<input type='hidden' name='motoShoriGroup' value='${su:htmlEscape(motoShoriGroup)}'>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ks.kazeiKbn.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.kazeiKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kazeiKbn.name)}</label>
				<div class='controls'>
					<select name='motoKazeiKbn' class='input-small'>
						<c:forEach var='kazeiKbnList' items='${kazeiKbnList}'>
							<option value='${su:htmlEscape(kazeiKbnList.naibu_cd)}' data-kazeiKbnGroup='${kazeiKbnList.option1}' <c:if test='${kazeiKbnList.naibu_cd eq motoKazeiKbn}'>selected</c:if>>${su:htmlEscape(kazeiKbnList.name)}</option>
						</c:forEach>
					</select> 
					<!-- 分離区分 -->
					<span <c:if test='${not ks.bunriKbn.hyoujiFlg}'>style='display:none;'</c:if>> <label class='label' for='bunriKbn'><c:if test='${ks.bunriKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.bunriKbn.name)}</label>
					<select name='motoBunriKbn' class='input-small'>
							<c:forEach var="bunriKbnRecord" items="${bunriKbnList}">
								<c:if test='${bunriKbnRecord.naibu_cd eq "9"}'>
									<option hidden value='${bunriKbnRecord.naibu_cd}' <c:if test='${bunriKbnRecord.naibu_cd eq motoBunriKbn}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
								</c:if>
								<c:if test='${bunriKbnRecord.naibu_cd ne "9"}'>
									<option value='${bunriKbnRecord.naibu_cd}' <c:if test='${bunriKbnRecord.naibu_cd eq motoBunriKbn}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
								</c:if>
							</c:forEach>
					</select>
					</span>
					<!-- 仕入区分 -->
					<span class='shiire' <c:if test='${shiireZeiAnbun eq "0"}'>style='display:none'</c:if>> <label class='label' for='motoShiireKbn'>${su:htmlEscape(ks.shiireKbn.name)}</label>
					<select name='motoShiireKbn' class='input-small'>
							<c:forEach var="shiireKbnRecord" items="${shiireKbnList}">
								<c:if test='${shiireKbnRecord.naibu_cd eq "0"}'>
									<option hidden value='${shiireKbnRecord.naibu_cd}' <c:if test='${shiireKbnRecord.naibu_cd eq motoShiireKbn}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
								</c:if>
								<c:if test='${shiireKbnRecord.naibu_cd ne "0"}'>
									<option value='${shiireKbnRecord.naibu_cd}' <c:if test='${shiireKbnRecord.naibu_cd eq motoShiireKbn}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
								</c:if>

							</c:forEach>
					</select>
					</span>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ks.kamokuEdaban.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.kamokuEdaban.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(ks.kamokuEdaban.name)}</label>
				<div class='controls'>
					<input type='text' name='motoKamokuEdabanCd' class='input-medium pc_only' value='${su:htmlEscape(motoKamokuEdabanCd)}'> <input type='text' name='motoKamokuEdabanName' class='input-xlarge' value='${su:htmlEscape(motoKamokuEdabanName)}' disabled>
					<button type='button' id='motoKamokuEdabanSentakuButton' class='btn btn-small'>選択</button>
					<button type='button' id='motoKamokuEdabanClearButton' class='btn btn-small'>クリア</button>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ks.futanBumon.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.futanBumon.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(ks.futanBumon.name)}</label>
				<div class='controls'>
					<input type='text' name='motoFutanBumonCd' class='input-small pc_only' value='${su:htmlEscape(motoFutanBumonCd)}'> <input type='text' name='motoFutanBumonName' class='input-xlarge' value='${su:htmlEscape(motoFutanBumonName)}' disabled>
					<button type='button' id='motoFutanBumonSentakuButton' class='btn btn-small'>選択</button>
					<button type='button' id='motoFutanBumonClearButton' class='btn btn-small'>クリア</button>
				</div>
			</div>
			<div class='control-group' <c:if test='${not ks.torihikisaki.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.torihikisaki.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(ks.torihikisaki.name)}</label>
				<div class='controls'>
					<input type='text' name='motoTorihikisakiCd' class='input-medium pc_only' value='${su:htmlEscape(motoTorihikisakiCd)}'> <input type='text' name='motoTorihikisakiNameRyakushiki' class='input-xlarge' value='${su:htmlEscape(motoTorihikisakiNameRyakushiki)}' disabled>
					<button type='button' id='motoTorihikisakiSentakuButton' name='motoTorihikisakiSentaku' class='btn btn-small'>選択</button>
					<button type='button' id='motoTorihikisakiClearButton' class='btn btn-small'>クリア</button>
				</div>
			</div>
			<!-- 事業者区分 -->
			<div class='control-group invoiceOnly jigyoushaKbn' id='jigyoushaKbnDiv' name='motoJigyoushaKbn' <c:if test='${not ks.jigyoushaKbn.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label' for='motoJigyoushaKbn'><c:if test='${ks.jigyoushaKbn.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(ks.jigyoushaKbn.name)}</label>
				<div class='controls'>
					<select name='motoJigyoushaKbn' class='input-small kari'>
					<c:forEach var="jigyoushaKbnRecord" items="${jigyoushaKbnList}">
						<option value='${jigyoushaKbnRecord.naibu_cd}' <c:if test='${jigyoushaKbnRecord.naibu_cd eq motoJigyoushaKbn}'>selected</c:if>>${jigyoushaKbnRecord.name}</option>
					</c:forEach>
					</select>
					<label class='label' for='motoZeigakuHoushiki'><c:if test='${ks.uriagezeigakuKeisan.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.uriagezeigakuKeisan.name)}</label>
					<select name='motoZeigakuHoushiki' class='input-small'>
						<c:forEach var="tmpZeigakuKeisan" items="${zeigakuKeisanList}">
							<c:if test='${tmpZeigakuKeisan.naibu_cd eq "9"}'>
								<option hidden value='${tmpZeigakuKeisan.naibu_cd}' <c:if test='${tmpZeigakuKeisan.naibu_cd eq motoZeigakuHoushiki}'>selected</c:if>>${tmpZeigakuKeisan.name}</option>
							</c:if>
							<c:if test='${tmpZeigakuKeisan.naibu_cd ne "9"}'>
								<option value='${tmpZeigakuKeisan.naibu_cd}' <c:if test='${tmpZeigakuKeisan.naibu_cd eq motoZeigakuHoushiki}'>selected</c:if>>${tmpZeigakuKeisan.name}</option>
							</c:if>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class='control-group' <c:if test='${not("0" ne pjShiyouFlg and ks.project.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.project.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(ks.project.name)}</label>
				<div class='controls'>
					<input type='text' name="motoProjectCd" class='pc_only' value='${su:htmlEscape(motoProjectCd)}'> <input type='text' name="motoProjectName" class='input-xlarge' disabled value='${su:htmlEscape(motoProjectName)}'>
					<button type='button' id='motoProjectSentakuButton' name='motoProjectSentakuButton' class='btn btn-small'>選択</button>
					<button type='button' id='motoProjectClearButton' name='motoProjectClearButton' class='btn btn-small'>クリア</button>
				</div>
			</div>
			<div class='control-group' <c:if test='${not("0" ne segmentShiyouFlg and ks.segment.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.segment.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(ks.segment.name)}</label>
				<div class='controls'>
					<input type='text' name="motoSegmentCd" class='pc_only' value='${su:htmlEscape(motoSegmentCd)}'> <input type='text' name="motoSegmentName" class='input-xlarge' disabled value='${su:htmlEscape(motoSegmentName)}'>
					<button type='button' id='motoSegmentSentakuButton' name='motoSegmentSentakuButton' class='btn btn-small'>選択</button>
					<button type='button' id='motoSegmentClearButton' name='motoSegmentClearButton' class='btn btn-small'>クリア</button>
				</div>
			</div>
			<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf1ShiyouFlg and ks.uf1.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.uf1.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(hfUfSeigyo.uf1Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or "1" eq hfUfSeigyo.uf1ShiyouFlg}'>
						<input type='text' name="motoUf1Cd" maxlength='20' value='${su:htmlEscape(motoUf1Cd)}'>
						<input type='hidden' name="motoUf1Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf1ShiyouFlg or "3" eq hfUfSeigyo.uf1ShiyouFlg}'>
						<input type='text' name="motoUf1Cd" class='pc_only' value='${su:htmlEscape(motoUf1Cd)}'>
						<input type='text' name="motoUf1Name" class='input-xlarge' disabled value='${su:htmlEscape(motoUf1Name)}'>
						<button type='button' id='motoUf1SentakuButton' class='btn btn-small'>選択</button>
						<button type='button' id='motoUf1ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf2ShiyouFlg and ks.uf2.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.uf2.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(hfUfSeigyo.uf2Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or "1" eq hfUfSeigyo.uf2ShiyouFlg}'>
						<input type='text' name="motoUf2Cd" maxlength='20' value='${su:htmlEscape(motoUf2Cd)}'>
						<input type='hidden' name="motoUf2Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf2ShiyouFlg or "3" eq hfUfSeigyo.uf2ShiyouFlg}'>
						<input type='text' name="motoUf2Cd" class='pc_only' value='${su:htmlEscape(motoUf2Cd)}'>
						<input type='text' name="motoUf2Name" class='input-xlarge' disabled value='${su:htmlEscape(motoUf2Name)}'>
						<button type='button' id='motoUf2SentakuButton' class='btn btn-small'>選択</button>
						<button type='button' id='motoUf2ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf3ShiyouFlg and ks.uf3.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.uf3.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(hfUfSeigyo.uf3Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or "1" eq hfUfSeigyo.uf3ShiyouFlg}'>
						<input type='text' name="motoUf3Cd" maxlength='20' value='${su:htmlEscape(motoUf3Cd)}'>
						<input type='hidden' name="motoUf3Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf3ShiyouFlg or "3" eq hfUfSeigyo.uf3ShiyouFlg}'>
						<input type='text' name="motoUf3Cd" class='pc_only' value='${su:htmlEscape(motoUf3Cd)}'>
						<input type='text' name="motoUf3Name" class='input-xlarge' disabled value='${su:htmlEscape(motoUf3Name)}'>
						<button type='button' id='motoUf3SentakuButton' class='btn btn-small'>選択</button>
						<button type='button' id='motoUf3ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf4ShiyouFlg and ks.uf4.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.uf4.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(hfUfSeigyo.uf4Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or "1" eq hfUfSeigyo.uf4ShiyouFlg}'>
						<input type='text' name="motoUf4Cd" maxlength='20' value='${su:htmlEscape(motoUf4Cd)}'>
						<input type='hidden' name="motoUf4Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf4ShiyouFlg or "3" eq hfUfSeigyo.uf4ShiyouFlg}'>
						<input type='text' name="motoUf4Cd" class='pc_only' value='${su:htmlEscape(motoUf4Cd)}'>
						<input type='text' name="motoUf4Name" class='input-xlarge' disabled value='${su:htmlEscape(motoUf4Name)}'>
						<button type='button' id='motoUf4SentakuButton' class='btn btn-small'>選択</button>
						<button type='button' id='motoUf4ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf5ShiyouFlg and ks.uf5.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.uf5.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(hfUfSeigyo.uf5Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or "1" eq hfUfSeigyo.uf5ShiyouFlg}'>
						<input type='text' name="motoUf5Cd" maxlength='20' value='${su:htmlEscape(motoUf5Cd)}'>
						<input type='hidden' name="motoUf5Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf5ShiyouFlg or "3" eq hfUfSeigyo.uf5ShiyouFlg}'>
						<input type='text' name="motoUf5Cd" class='pc_only' value='${su:htmlEscape(motoUf5Cd)}'>
						<input type='text' name="motoUf5Name" class='input-xlarge' disabled value='${su:htmlEscape(motoUf5Name)}'>
						<button type='button' id='motoUf5SentakuButton' class='btn btn-small'>選択</button>
						<button type='button' id='motoUf5ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf6ShiyouFlg and ks.uf6.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.uf6.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(hfUfSeigyo.uf6Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or "1" eq hfUfSeigyo.uf6ShiyouFlg}'>
						<input type='text' name="motoUf6Cd" maxlength='20' value='${su:htmlEscape(motoUf6Cd)}'>
						<input type='hidden' name="motoUf6Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf6ShiyouFlg or "3" eq hfUfSeigyo.uf6ShiyouFlg}'>
						<input type='text' name="motoUf6Cd" class='pc_only' value='${su:htmlEscape(motoUf6Cd)}'>
						<input type='text' name="motoUf6Name" class='input-xlarge' disabled value='${su:htmlEscape(motoUf6Name)}'>
						<button type='button' id='motoUf6SentakuButton' class='btn btn-small'>選択</button>
						<button type='button' id='motoUf6ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf7ShiyouFlg and ks.uf7.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.uf7.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(hfUfSeigyo.uf7Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or "1" eq hfUfSeigyo.uf7ShiyouFlg}'>
						<input type='text' name="motoUf7Cd" maxlength='20' value='${su:htmlEscape(motoUf7Cd)}'>
						<input type='hidden' name="motoUf7Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf7ShiyouFlg or "3" eq hfUfSeigyo.uf7ShiyouFlg}'>
						<input type='text' name="motoUf7Cd" class='pc_only' value='${su:htmlEscape(motoUf7Cd)}'>
						<input type='text' name="motoUf7Name" class='input-xlarge' disabled value='${su:htmlEscape(motoUf7Name)}'>
						<button type='button' id='motoUf7SentakuButton' class='btn btn-small'>選択</button>
						<button type='button' id='motoUf7ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf8ShiyouFlg and ks.uf8.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.uf8.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(hfUfSeigyo.uf8Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or "1" eq hfUfSeigyo.uf8ShiyouFlg}'>
						<input type='text' name="motoUf8Cd" maxlength='20' value='${su:htmlEscape(motoUf8Cd)}'>
						<input type='hidden' name="motoUf8Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf8ShiyouFlg or "3" eq hfUfSeigyo.uf8ShiyouFlg}'>
						<input type='text' name="motoUf8Cd" class='pc_only' value='${su:htmlEscape(motoUf8Cd)}'>
						<input type='text' name="motoUf8Name" class='input-xlarge' disabled value='${su:htmlEscape(motoUf8Name)}'>
						<button type='button' id='motoUf8SentakuButton' class='btn btn-small'>選択</button>
						<button type='button' id='motoUf8ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf9ShiyouFlg and ks.uf9.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.uf9.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(hfUfSeigyo.uf9Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or "1" eq hfUfSeigyo.uf9ShiyouFlg}'>
						<input type='text' name="motoUf9Cd" maxlength='20' value='${su:htmlEscape(motoUf9Cd)}'>
						<input type='hidden' name="motoUf9Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf9ShiyouFlg or "3" eq hfUfSeigyo.uf9ShiyouFlg}'>
						<input type='text' name="motoUf9Cd" class='pc_only' value='${su:htmlEscape(motoUf9Cd)}'>
						<input type='text' name="motoUf9Name" class='input-xlarge' disabled value='${su:htmlEscape(motoUf9Name)}'>
						<button type='button' id='motoUf9SentakuButton' class='btn btn-small'>選択</button>
						<button type='button' id='motoUf9ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
				</div>
			</div>
			<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf10ShiyouFlg and ks.uf10.hyoujiFlg)}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.uf10.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(hfUfSeigyo.uf10Name)}</label>
				<div class='controls'>
					<c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or "1" eq hfUfSeigyo.uf10ShiyouFlg}'>
						<input type='text' name="motoUf10Cd" maxlength='20' value='${su:htmlEscape(motoUf10Cd)}'>
						<input type='hidden' name="motoUf10Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf10ShiyouFlg or "3" eq hfUfSeigyo.uf10ShiyouFlg}'>
						<input type='text' name="motoUf10Cd" class='pc_only' value='${su:htmlEscape(motoUf10Cd)}'>
						<input type='text' name="motoUf10Name" class='input-xlarge' disabled value='${su:htmlEscape(motoUf10Name)}'>
						<button type='button' id='motoUf10SentakuButton' class='btn btn-small'>選択</button>
						<button type='button' id='motoUf10ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
				</div>
			</div>

			<div class='control-group' <c:if test='${not ks.kingakuGoukei.hyoujiFlg}'>style='display:none;'</c:if>>
				<label class='control-label'> <c:if test='${ks.kingakuGoukei.hissuFlg}'>
						<span class='required'>*</span>
					</c:if>${su:htmlEscape(ks.kingakuGoukei.name)}</label>
				<div class='controls'>
					<input type='text' name='kingaku' class='input-medium autoNumeric' value='${su:htmlEscape(kingaku)}' disabled>円
				</div>
				<input type='hidden' name='hontaiKingaku' value='${su:htmlEscape(hontaiKingaku)}' disabled>
				<input type='hidden' name='shouhizeigaku' value='${su:htmlEscape(shouhizeigaku)}' disabled>
			</div>
		</div>
	</section>
	<!-- 付替元 -->

	<!--  付替先 -->
	<section id='meisaiList'>
		<c:if test='${0 < fn:length(sakiKamokuCd)}'>
			<c:forEach var="i" begin="0" end="${fn:length(sakiKamokuCd) - 1}" step="1">
				<div class='blank meisai print-unit'>
					<h2>
						付替先：<span class='sakiTaishaku'></span>#<span id='denpyouEdano' class='number'>${i + 1}</span> <span class='non-print'>
							<button type='button' name='meisaiAdd' class='btn '>追加</button>
							<button type='button' name='meisaiDelete' class='btn '>削除</button>
							<button type='button' name='meisaiUp' class='btn '>↑</button>
							<button type='button' name='meisaiDown' class='btn '>↓</button>
							<button type='button' name='meisaiCopy' class='btn '>ｺﾋﾟｰ</button>
						</span>
					</h2>
					<div class='control-group' <c:if test='${not ks.kamoku.hyoujiFlg}'>style='display:none;'</c:if>>
						<label class='control-label'> <c:if test='${ks.kamoku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kamoku.name)}</label>
						<div class='controls'>
							<input type='text' name='sakiKamokuCd' class='input-small pc_only' value='${su:htmlEscape(sakiKamokuCd[i])}'> <input type='text' name='sakiKamokuName' class='input-xlarge' disabled value='${su:htmlEscape(sakiKamokuName[i])}'>
							<button type='button' name='sakiKamokuSentakuButton' class='btn btn-small'>選択</button>
							<input type='hidden' name='sakiShoriGroup' value='${su:htmlEscape(sakiShoriGroup[i])}'>
						</div>
					</div>
					<div class='control-group' <c:if test='${not ks.kazeiKbn.hyoujiFlg}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.kazeiKbn.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(ks.kazeiKbn.name)}</label>
						<div class='controls'>
							<select name='sakiKazeiKbn' class='input-small'>
								<c:forEach var='kazeiKbnList' items='${kazeiKbnList}'>
									<option value='${su:htmlEscape(kazeiKbnList.naibu_cd)}' data-kazeiKbnGroup='${kazeiKbnList.option1}' <c:if test='${kazeiKbnList.naibu_cd eq sakiKazeiKbn[i]}'>selected</c:if>>${su:htmlEscape(kazeiKbnList.name)}</option>
								</c:forEach>
							</select>
							<!-- 分離区分 -->
							<span <c:if test='${not ks.bunriKbn.hyoujiFlg}'>style='display:none;'</c:if>> <label class='label' for='bunriKbn'><c:if test='${ks.bunriKbn.hissuFlg}'>
										<span class='required'>*</span>
									</c:if>${su:htmlEscape(ks.bunriKbn.name)}</label> <select name='sakiBunriKbn' class='input-small'>
									<c:forEach var="bunriKbnRecord" items="${bunriKbnList}">
										<c:if test='${bunriKbnRecord.naibu_cd eq "9"}'>
											<option hidden value='${bunriKbnRecord.naibu_cd}' <c:if test='${bunriKbnRecord.naibu_cd eq sakiBunriKbn[i]}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
										</c:if>
										<c:if test='${bunriKbnRecord.naibu_cd ne "9"}'>
											<option value='${bunriKbnRecord.naibu_cd}' <c:if test='${bunriKbnRecord.naibu_cd eq sakiBunriKbn[i]}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
										</c:if>
									</c:forEach>
							</select>
							</span>
							<!-- 仕入区分 -->
							<span class='shiire'<c:if test='${shiireZeiAnbun eq "0"}'>style='display:none'</c:if>> <label class='label' for='sakiShiireKbn'>${su:htmlEscape(ks.shiireKbn.name)}</label>
							<select name='sakiShiireKbn' class='input-small'>
									<c:forEach var="shiireKbnRecord" items="${shiireKbnList}">
										<c:if test='${shiireKbnRecord.naibu_cd eq "0"}'>
											<option hidden value='${shiireKbnRecord.naibu_cd}' <c:if test='${shiireKbnRecord.naibu_cd eq sakiShiireKbn[i]}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
										</c:if>
										<c:if test='${shiireKbnRecord.naibu_cd ne "0"}'>
											<option value='${shiireKbnRecord.naibu_cd}' <c:if test='${shiireKbnRecord.naibu_cd eq sakiShiireKbn[i]}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
										</c:if>

									</c:forEach>
							</select>
							</span>
						</div>
					</div>


					<div class='control-group' <c:if test='${not ks.kamokuEdaban.hyoujiFlg}'>style='display:none;'</c:if>>
						<label class='control-label'> <c:if test='${ks.kamokuEdaban.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(ks.kamokuEdaban.name)}</label>
						<div class='controls'>
							<input type='text' name='sakiKamokuEdabanCd' class='input-medium pc_only' value='${su:htmlEscape(sakiKamokuEdabanCd[i])}'> <input type='text' name='sakiKamokuEdabanName' class='input-xlarge' disabled value='${su:htmlEscape(sakiKamokuEdabanName[i])}'>
							<button type='button' name='sakiKamokuEdabanSentakuButton' class='btn btn-small'>選択</button>
							<button type='button' name='sakiKamokuEdabanClearButton' class='btn btn-small'>クリア</button>
						</div>
					</div>
					<div class='control-group' <c:if test='${not ks.futanBumon.hyoujiFlg}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.futanBumon.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(ks.futanBumon.name)}</label>
						<div class='controls'>
							<input type='text' name='sakiFutanBumonCd' class='input-small pc_only' value='${su:htmlEscape(sakiFutanBumonCd[i])}'> <input type='text' name='sakiFutanBumonName' class='input-xlarge' disabled value='${su:htmlEscape(sakiFutanBumonName[i])}'>
							<button type='button' name='sakiFutanBumonSentakuButton' class='btn btn-small'>選択</button>
							<button type='button' name='sakiFutanBumonClearButton' class='btn btn-small'>クリア</button>
						</div>
					</div>
					<div class='control-group' <c:if test='${not ks.torihikisaki.hyoujiFlg}'>style='display:none;'</c:if>>
						<label class='control-label'> <c:if test='${ks.torihikisaki.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(ks.torihikisaki.name)}</label>
						<div class='controls'>
							<input type='text' name='sakiTorihikisakiCd' class='input-medium pc_only' value='${su:htmlEscape(sakiTorihikisakiCd[i])}'> <input type='text' name='sakiTorihikisakiNameRyakushiki' class='input-xlarge' disabled value='${su:htmlEscape(sakiTorihikisakiNameRyakushiki[i])}'>
							<button type='button' name='sakiTorihikisakiSentakuButton' class='btn btn-small'>選択</button>
							<button type='button' name='sakiTorihikisakiClearButton' class='btn btn-small'>クリア</button>
						</div>
					</div>
					<!-- 事業者区分 -->
					<div class='control-group invoiceOnly' id='jigyoushaKbnDiv' name='jigyoushaKbn' <c:if test='${not ks.jigyoushaKbn.hyoujiFlg}'>style='display:none;'</c:if>>
						<label class='control-label' for='sakiJigyoushaKbn'><c:if test='${ks.jigyoushaKbn.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(ks.jigyoushaKbn.name)}</label>
						<div class='controls'>
							<select name='sakiJigyoushaKbn' class='input-small kari'>
							<c:forEach var="jigyoushaKbnRecord" items="${jigyoushaKbnList}">
								<option value='${jigyoushaKbnRecord.naibu_cd}' <c:if test='${jigyoushaKbnRecord.naibu_cd eq sakiJigyoushaKbn[i]}'>selected</c:if>>${jigyoushaKbnRecord.name}</option>
							</c:forEach>
							</select>
							<label class='label' for='sakiZeigakuHoushiki'><c:if test='${ks.uriagezeigakuKeisan.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.uriagezeigakuKeisan.name)}</label>
								<select name='sakiZeigakuHoushiki' class='input-small'>
								<c:forEach var="tmpZeigakuKeisan" items="${zeigakuKeisanList}">
								<c:if test='${tmpZeigakuKeisan.naibu_cd eq "9"}'>
									<option hidden value='${tmpZeigakuKeisan.naibu_cd}' <c:if test='${tmpZeigakuKeisan.naibu_cd eq sakiZeigakuHoushiki[i]}'>selected</c:if>>${tmpZeigakuKeisan.name}</option>
								</c:if>
								<c:if test='${tmpZeigakuKeisan.naibu_cd ne "9"}'>
									<option value='${tmpZeigakuKeisan.naibu_cd}' <c:if test='${tmpZeigakuKeisan.naibu_cd eq sakiZeigakuHoushiki[i]}'>selected</c:if>>${tmpZeigakuKeisan.name}</option>
								</c:if>
								</c:forEach>
							</select>
						</div>
					</div>
					<input type='hidden' value='${su:htmlEscape(jigyoushaKbn)}'>
					<div class='control-group' <c:if test='${not("0" ne pjShiyouFlg and ks.project.hyoujiFlg)}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.project.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(ks.project.name)}</label>
						<div class='controls'>
							<input type='text' name="sakiProjectCd" class='pc_only' value='${su:htmlEscape(sakiProjectCd[i])}'> <input type='text' name="sakiProjectName" class='input-xlarge' disabled value='${su:htmlEscape(sakiProjectName[i])}'>
							<button type='button' name='sakiProjectSentakuButton' class='btn btn-small'>選択</button>
							<button type='button' name='sakiProjectClearButton' class='btn btn-small'>クリア</button>
						</div>
					</div>
					<div class='control-group' <c:if test='${not("0" ne segmentShiyouFlg and ks.segment.hyoujiFlg)}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.segment.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(ks.segment.name)}</label>
						<div class='controls'>
							<input type='text' name="sakiSegmentCd" class='pc_only' value='${su:htmlEscape(sakiSegmentCd[i])}'> <input type='text' name="sakiSegmentName" class='input-xlarge' disabled value='${su:htmlEscape(sakiSegmentName[i])}'>
							<button type='button' name='sakiSegmentSentakuButton' class='btn btn-small'>選択</button>
							<button type='button' name='sakiSegmentClearButton' class='btn btn-small'>クリア</button>
						</div>
					</div>
					<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf1ShiyouFlg and ks.uf1.hyoujiFlg)}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.uf1.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(hfUfSeigyo.uf1Name)}</label>
						<div class='controls'>
							<c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or "1" eq hfUfSeigyo.uf1ShiyouFlg}'>
								<input type='text' name="sakiUf1Cd" maxlength='20' value='${su:htmlEscape(sakiUf1Cd[i])}'>
								<input type='hidden' name="sakiUf1Name" value=''>
							</c:if>
							<c:if test='${"2" eq hfUfSeigyo.uf1ShiyouFlg or "3" eq hfUfSeigyo.uf1ShiyouFlg}'>
								<input type='text' name="sakiUf1Cd" class='pc_only' value='${su:htmlEscape(sakiUf1Cd[i])}'>
								<input type='text' name="sakiUf1Name" class='input-xlarge' disabled value='${su:htmlEscape(sakiUf1Name[i])}'>
								<button type='button' name='sakiUf1SentakuButton' class='btn btn-small'>選択</button>
								<button type='button' name='sakiUf1ClearButton' class='btn btn-small'>クリア</button>
							</c:if>
						</div>
					</div>
					<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf2ShiyouFlg and ks.uf2.hyoujiFlg)}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.uf2.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(hfUfSeigyo.uf2Name)}</label>
						<div class='controls'>
							<c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or "1" eq hfUfSeigyo.uf2ShiyouFlg}'>
								<input type='text' name="sakiUf2Cd" maxlength='20' value='${su:htmlEscape(sakiUf2Cd[i])}'>
								<input type='hidden' name="sakiUf2Name" value=''>
							</c:if>
							<c:if test='${"2" eq hfUfSeigyo.uf2ShiyouFlg or "3" eq hfUfSeigyo.uf2ShiyouFlg}'>
								<input type='text' name="sakiUf2Cd" class='pc_only' value='${su:htmlEscape(sakiUf2Cd[i])}'>
								<input type='text' name="sakiUf2Name" class='input-xlarge' disabled value='${su:htmlEscape(sakiUf2Name[i])}'>
								<button type='button' name='sakiUf2SentakuButton' class='btn btn-small'>選択</button>
								<button type='button' name='sakiUf2ClearButton' class='btn btn-small'>クリア</button>
							</c:if>
						</div>
					</div>
					<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf3ShiyouFlg and ks.uf3.hyoujiFlg)}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.uf3.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(hfUfSeigyo.uf3Name)}</label>
						<div class='controls'>
							<c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or "1" eq hfUfSeigyo.uf3ShiyouFlg}'>
								<input type='text' name="sakiUf3Cd" maxlength='20' value='${su:htmlEscape(sakiUf3Cd[i])}'>
								<input type='hidden' name="sakiUf3Name" value=''>
							</c:if>
							<c:if test='${"2" eq hfUfSeigyo.uf3ShiyouFlg or "3" eq hfUfSeigyo.uf3ShiyouFlg}'>
								<input type='text' name="sakiUf3Cd" class='pc_only' value='${su:htmlEscape(sakiUf3Cd[i])}'>
								<input type='text' name="sakiUf3Name" class='input-xlarge' disabled value='${su:htmlEscape(sakiUf3Name[i])}'>
								<button type='button' name='sakiUf3SentakuButton' class='btn btn-small'>選択</button>
								<button type='button' name='sakiUf3ClearButton' class='btn btn-small'>クリア</button>
							</c:if>
						</div>
					</div>
					<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf4ShiyouFlg and ks.uf4.hyoujiFlg)}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.uf4.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(hfUfSeigyo.uf4Name)}</label>
						<div class='controls'>
							<c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or "1" eq hfUfSeigyo.uf4ShiyouFlg}'>
								<input type='text' name="sakiUf4Cd" maxlength='20' value='${su:htmlEscape(sakiUf4Cd[i])}'>
								<input type='hidden' name="sakiUf4Name" value=''>
							</c:if>
							<c:if test='${"2" eq hfUfSeigyo.uf4ShiyouFlg or "3" eq hfUfSeigyo.uf4ShiyouFlg}'>
								<input type='text' name="sakiUf4Cd" class='pc_only' value='${su:htmlEscape(sakiUf4Cd[i])}'>
								<input type='text' name="sakiUf4Name" class='input-xlarge' disabled value='${su:htmlEscape(sakiUf4Name[i])}'>
								<button type='button' name='sakiUf4SentakuButton' class='btn btn-small'>選択</button>
								<button type='button' name='sakiUf4ClearButton' class='btn btn-small'>クリア</button>
							</c:if>
						</div>
					</div>
					<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf5ShiyouFlg and ks.uf5.hyoujiFlg)}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.uf5.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(hfUfSeigyo.uf5Name)}</label>
						<div class='controls'>
							<c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or "1" eq hfUfSeigyo.uf5ShiyouFlg}'>
								<input type='text' name="sakiUf5Cd" maxlength='20' value='${su:htmlEscape(sakiUf5Cd[i])}'>
								<input type='hidden' name="sakiUf5Name" value=''>
							</c:if>
							<c:if test='${"2" eq hfUfSeigyo.uf5ShiyouFlg or "3" eq hfUfSeigyo.uf5ShiyouFlg}'>
								<input type='text' name="sakiUf5Cd" class='pc_only' value='${su:htmlEscape(sakiUf5Cd[i])}'>
								<input type='text' name="sakiUf5Name" class='input-xlarge' disabled value='${su:htmlEscape(sakiUf5Name[i])}'>
								<button type='button' name='sakiUf5SentakuButton' class='btn btn-small'>選択</button>
								<button type='button' name='sakiUf5ClearButton' class='btn btn-small'>クリア</button>
							</c:if>
						</div>
					</div>
					<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf6ShiyouFlg and ks.uf6.hyoujiFlg)}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.uf6.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(hfUfSeigyo.uf6Name)}</label>
						<div class='controls'>
							<c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or "1" eq hfUfSeigyo.uf6ShiyouFlg}'>
								<input type='text' name="sakiUf6Cd" maxlength='20' value='${su:htmlEscape(sakiUf6Cd[i])}'>
								<input type='hidden' name="sakiUf6Name" value=''>
							</c:if>
							<c:if test='${"2" eq hfUfSeigyo.uf6ShiyouFlg or "3" eq hfUfSeigyo.uf6ShiyouFlg}'>
								<input type='text' name="sakiUf6Cd" class='pc_only' value='${su:htmlEscape(sakiUf6Cd[i])}'>
								<input type='text' name="sakiUf6Name" class='input-xlarge' disabled value='${su:htmlEscape(sakiUf6Name[i])}'>
								<button type='button' name='sakiUf6SentakuButton' class='btn btn-small'>選択</button>
								<button type='button' name='sakiUf6ClearButton' class='btn btn-small'>クリア</button>
							</c:if>
						</div>
					</div>
					<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf7ShiyouFlg and ks.uf7.hyoujiFlg)}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.uf7.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(hfUfSeigyo.uf7Name)}</label>
						<div class='controls'>
							<c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or "1" eq hfUfSeigyo.uf7ShiyouFlg}'>
								<input type='text' name="sakiUf7Cd" maxlength='20' value='${su:htmlEscape(sakiUf7Cd[i])}'>
								<input type='hidden' name="sakiUf7Name" value=''>
							</c:if>
							<c:if test='${"2" eq hfUfSeigyo.uf7ShiyouFlg or "3" eq hfUfSeigyo.uf7ShiyouFlg}'>
								<input type='text' name="sakiUf7Cd" class='pc_only' value='${su:htmlEscape(sakiUf7Cd[i])}'>
								<input type='text' name="sakiUf7Name" class='input-xlarge' disabled value='${su:htmlEscape(sakiUf7Name[i])}'>
								<button type='button' name='sakiUf7SentakuButton' class='btn btn-small'>選択</button>
								<button type='button' name='sakiUf7ClearButton' class='btn btn-small'>クリア</button>
							</c:if>
						</div>
					</div>
					<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf8ShiyouFlg and ks.uf8.hyoujiFlg)}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.uf8.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(hfUfSeigyo.uf8Name)}</label>
						<div class='controls'>
							<c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or "1" eq hfUfSeigyo.uf8ShiyouFlg}'>
								<input type='text' name="sakiUf8Cd" maxlength='20' value='${su:htmlEscape(sakiUf8Cd[i])}'>
								<input type='hidden' name="sakiUf8Name" value=''>
							</c:if>
							<c:if test='${"2" eq hfUfSeigyo.uf8ShiyouFlg or "3" eq hfUfSeigyo.uf8ShiyouFlg}'>
								<input type='text' name="sakiUf8Cd" class='pc_only' value='${su:htmlEscape(sakiUf8Cd[i])}'>
								<input type='text' name="sakiUf8Name" class='input-xlarge' disabled value='${su:htmlEscape(sakiUf8Name[i])}'>
								<button type='button' name='sakiUf8SentakuButton' class='btn btn-small'>選択</button>
								<button type='button' name='sakiUf8ClearButton' class='btn btn-small'>クリア</button>
							</c:if>
						</div>
					</div>
					<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf9ShiyouFlg and ks.uf9.hyoujiFlg)}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.uf9.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(hfUfSeigyo.uf9Name)}</label>
						<div class='controls'>
							<c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or "1" eq hfUfSeigyo.uf9ShiyouFlg}'>
								<input type='text' name="sakiUf9Cd" maxlength='20' value='${su:htmlEscape(sakiUf9Cd[i])}'>
								<input type='hidden' name="sakiUf9Name" value=''>
							</c:if>
							<c:if test='${"2" eq hfUfSeigyo.uf9ShiyouFlg or "3" eq hfUfSeigyo.uf9ShiyouFlg}'>
								<input type='text' name="sakiUf9Cd" class='pc_only' value='${su:htmlEscape(sakiUf9Cd[i])}'>
								<input type='text' name="sakiUf9Name" class='input-xlarge' disabled value='${su:htmlEscape(sakiUf9Name[i])}'>
								<button type='button' name='sakiUf9SentakuButton' class='btn btn-small'>選択</button>
								<button type='button' name='sakiUf9ClearButton' class='btn btn-small'>クリア</button>
							</c:if>
						</div>
					</div>
					<div class='control-group' <c:if test='${not("0" ne hfUfSeigyo.uf10ShiyouFlg and ks.uf10.hyoujiFlg)}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.uf10.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(hfUfSeigyo.uf10Name)}</label>
						<div class='controls'>
							<c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or "1" eq hfUfSeigyo.uf10ShiyouFlg}'>
								<input type='text' name="sakiUf10Cd" maxlength='20' value='${su:htmlEscape(sakiUf10Cd[i])}'>
								<input type='hidden' name="sakiUf10Name" value=''>
							</c:if>
							<c:if test='${"2" eq hfUfSeigyo.uf10ShiyouFlg or "3" eq hfUfSeigyo.uf10ShiyouFlg}'>
								<input type='text' name="sakiUf10Cd" class='pc_only' value='${su:htmlEscape(sakiUf10Cd[i])}'>
								<input type='text' name="sakiUf10Name" class='input-xlarge' disabled value='${su:htmlEscape(sakiUf10Name[i])}'>
								<button type='button' name='sakiUf10SentakuButton' class='btn btn-small'>選択</button>
								<button type='button' name='sakiUf10ClearButton' class='btn btn-small'>クリア</button>
							</c:if>
						</div>
					</div>

					<div class='control-group' <c:if test='${not ks.kingaku.hyoujiFlg}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.kingaku.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(ks.kingaku.name)}</label>
						<div class='controls'>
							<input type='text' name='sakiKingaku' class='input-medium autoNumeric<c:if test="${enableInput}">WithCalcBox</c:if>' value='${su:htmlEscape(sakiKingaku[i])}'>円 <span style='display: none;'>
							（本体金額<input type='text' name='sakiHontaiKingaku' class='input-medium autoNumeric' disabled value='${su:htmlEscape(sakiHontaiKingaku[i])}'>円
								消費税額<input type='text' name='sakiShouhizeigaku' class='input-medium autoNumeric' disabled value='${su:htmlEscape(sakiShouhizeigaku[i])}'>円）
							</span>
						</div>
					</div>
					<div class='control-group' <c:if test='${not ks.tekiyou.hyoujiFlg}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.tekiyou.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(ks.tekiyou.name)}</label>
						<div class='controls'>
							<input type='text' name="sakiTekiyou" class='input-block-level' value='${su:htmlEscape(sakiTekiyou[i])}'> <span style="line-height: 25px;"><br>
							<span style="color: #ff0000;">${su:htmlEscape(chuuki2[i])}</span></span>
						</div>
					</div>
					<div class='control-group' <c:if test='${not ks.bikou.hyoujiFlg}'>style='display:none;'</c:if>>
						<label class='control-label'><c:if test='${ks.bikou.hissuFlg}'>
								<span class='required'>*</span>
							</c:if>${su:htmlEscape(ks.bikou.name)}</label>
						<div class='controls'>
							<input type='text' name="sakiBikou" class='input-block-level' maxlength='40' value='${su:htmlEscape(sakiBikou[i])}'>
						</div>
					</div>
				</div>
			</c:forEach>
		</c:if>
	</section>

	<!-- 付替先 -->
	<section class='print-unit' <c:if test='${not ks.hosoku.hyoujiFlg}'>style='display:none;'</c:if>>
		<h2>
			<c:if test='${ks.hosoku.hissuFlg}'>
				<span class='required'>*</span>
			</c:if>${su:htmlEscape(ks.hosoku.name)}</h2>
		<div>
			<div class='control-group'>
				<textarea name="hosoku" class='input-block-level' maxlength="240">${su:htmlEscape(hosoku)}</textarea>
			</div>
		</div>
	</section>
	<!-- 付替先 -->
</div>
<!-- content -->

<!-- スクリプト -->
<script style=''>
/**
 * 軽減税率区分の値取得
 */
function getKeigenZeiritsuKbn(){
	var keigenZeiritsuKbn = $("select[name=zeiritsu]").find("option:selected").attr("data-keigenZeiritsuKbn");
	$("input[name=keigenZeiritsuKbn]").val(keigenZeiritsuKbn);
}

/**
 * 貸借表示切替
 */
function displayTaishaku() {
	$("span.motoTaishaku").text("1" == $("input[name=tsukekaeKbn]:checked").val() ? "借方" : "貸方");
	$("span.sakiTaishaku").text("1" == $("input[name=tsukekaeKbn]:checked").val() ? "貸方" : "借方");
}

/**
 * （付替先）勘定科目選択ボタン押下時Function
 */
 function sakiKamokuSentaku() {
	var meisai = $(this).closest("div.meisai");
	dialogRetKamokuCd = meisai.find("input[name=sakiKamokuCd]");
	dialogRetKamokuName = meisai.find("input[name=sakiKamokuName]");
	dialogRetShoriGroup = meisai.find("input[name=sakiShoriGroup]");
	dialogRetKazeiKbn = meisai.find("select[name=sakiKazeiKbn]");
    dialogRetBunriKbn = meisai.find("select[name=sakiBunriKbn]");
    dialogRetKariShiireKbn = meisai.find("select[name=sakiShiireKbn]");
	dialogCallbackKanjyouKamokuSentaku = function(){
		meisai.find("input[name=sakiKamokuEdabanCd]").val("");
		meisai.find("input[name=sakiKamokuEdabanName]").val("");
		if(dialogRetKamokuCd.val() == null || dialogRetKamokuCd.val() == "")
		{
			dialogRetKazeiKbn.val("100");
		}
		sakiSeigyo();
		$("input[name=sakiFutanBumonCd]").blur();
	};
	commonKamokuSentaku();
}

/**
 * （付替先）勘定科目コードロストフォーカス時Function
 */
function kamokuCdLostFocus() {
 	var meisai = $(this).closest("div.meisai");
 	var title = $(this).parent().parent().find(".control-label").text();
	dialogRetKamokuCd = meisai.find("input[name=sakiKamokuCd]");
	dialogRetKamokuName = meisai.find("input[name=sakiKamokuName]");
	dialogRetShoriGroup = meisai.find("input[name=sakiShoriGroup]");
	dialogRetKazeiKbn = meisai.find("select[name=sakiKazeiKbn]");
    dialogRetBunriKbn = meisai.find("select[name=sakiBunriKbn]");
    dialogRetKariShiireKbn = meisai.find("select[name=sakiShiireKbn]");
	dialogCallbackKanjyouKamokuSentaku = function(){
		meisai.find("input[name=sakiKamokuEdabanCd]").val("");
		meisai.find("input[name=sakiKamokuEdabanName]").val("");
		if(dialogRetKamokuCd.val() == null || dialogRetKamokuCd.val() == "")
		{
			dialogRetKazeiKbn.val("100");
		}
		sakiSeigyo();
		$("input[name=sakiFutanBumonCd]").blur();
	};
	commonKamokuCdLostFocus(dialogRetKamokuCd, dialogRetKamokuName, title, null, null, dialogRetKazeiKbn, dialogRetBunriKbn, dialogRetKariShiireKbn, dialogRetShoriGroup);
}

/**
 * （付替先）勘定科目枝番選択ボタン押下時Function
 */
function sakiKamokuEdabanSentaku() {
	var meisai = $(this).closest("div.meisai");
	if($("input[name=sakiKamokuCd]").val() == "") {
		alert("${su:htmlEscape(ks.kamoku.name)}を入力してください。");
	}else {
		dialogRetKamokuEdabanCd = meisai.find("input[name=sakiKamokuEdabanCd]");
		dialogRetKamokuEdabanName = meisai.find("input[name=sakiKamokuEdabanName]");
		dialogRetKazeiKbn = meisai.find("select[name=sakiKazeiKbn]");
		dialogRetBunriKbn = meisai.find("select[name=sakiBunriKbn]");
		dialogCallbackKanjyouKamokuEdabanSentaku = function(){
			sakiSeigyo();
			calcMoney();
			$("input[name=sakiFutanBumonCd]").blur();
		};
		commonKamokuEdabanSentaku(meisai.find("[name=sakiKamokuCd]").val());
	}
}

/**
 * （付替先）勘定科目枝番クリアボタン押下時Function
 */
function sakiKamokuEdabanClear() {
 	var meisai = $(this).closest("div.meisai");
 	meisai.find("input[name=sakiKamokuEdabanCd]").val("");
 	meisai.find("input[name=sakiKamokuEdabanName]").val("");
}

/**
 * （付替先）勘定科目枝番コードロストフォーカス時Function
 */
function kamokuEdabanCdLostFocus() {
	var meisai = $(this).closest("div.meisai");
	var kamokuCd = 	meisai.find("input[name=sakiKamokuCd]").val();
	dialogRetKamokuEdabanCd = meisai.find("input[name=sakiKamokuEdabanCd]");
	dialogRetKamokuEdabanName = meisai.find("input[name=sakiKamokuEdabanName]");
	dialogRetKazeiKbn = meisai.find("select[name=sakiKazeiKbn]");
	dialogRetBunriKbn = meisai.find("select[name=sakiBunriKbn]");
	if(kamokuCd == "" && dialogRetKamokuEdabanCd.val() != "") {
		alert("${su:htmlEscape(ks.kamoku.name)}を入力してください。");
	}else {
		var title = $(this).parent().parent().find(".control-label").text();;
		dialogCallbackKanjyouKamokuEdabanSentaku = function(){
			sakiSeigyo();
			calcMoney();
			$("input[name=sakiFutanBumonCd]").blur();
		};
		commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamokuCd, title,dialogRetKazeiKbn,dialogRetBunriKbn);
	}
}

/**
 * （付替先）負担部門選択ボタン押下時Function
 */
function sakiFutanBumonSentaku() {
	var meisai = $(this).closest("div.meisai");
	dialogRetFutanBumonCd = meisai.find("input[name=sakiFutanBumonCd]");
	dialogRetFutanBumonName = meisai.find("input[name=sakiFutanBumonName]");
	dialogRetKariShiireKbn = meisai.find("select[name=sakiShiireKbn]");
	commonFutanBumonSentaku("1",meisai.find("[name=sakiKamokuCd]").val(),$("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val());
}

/**
 * （付替先）負担部門クリアボタン押下時Function
 */
function sakiFutanBumonClear() {
	var meisai = $(this).closest("div.meisai");
	meisai.find("input[name=sakiFutanBumonCd]").val("");
	meisai.find("input[name=sakiFutanBumonName]").val("");
}

/**
 * （付替先）負担部門コードロストフォーカス時Function
 */
function futanBumonCdLostFocus() {
	var meisai = $(this).closest("div.meisai");
	var title = $(this).parent().parent().find(".control-label").text();
	dialogRetFutanBumonCd 	= meisai.find("input[name=sakiFutanBumonCd]");
	dialogRetFutanBumonName = meisai.find("input[name=sakiFutanBumonName]");
	dialogRetKariShiireKbn		 = meisai.find("select[name=sakiShiireKbn]");
	commonFutanBumonCdLostFocus("1",dialogRetFutanBumonCd, dialogRetFutanBumonName, title, $("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val(), meisai.find("[name=sakiKamokuCd]").val(), dialogRetKariShiireKbn);
}

/**
 * （付替先）取引先選択ボタン押下時Function
 */
function sakiTorihikisakiSentaku() {
	var meisai = $(this).closest("div.meisai");
	dialogRetTorihikisakiCd		= meisai.find("[name=sakiTorihikisakiCd]");
	dialogRetTorihikisakiName	= meisai.find("[name=sakiTorihikisakiNameRyakushiki]");
	dialogRetJigyoushaKbn 		=  meisai.find("[name=sakiJigyoushaKbn]");
	commonTorihikisakiSentaku();
	calcMoney();
	dialogCallbackTorihikisakiSentaku = function(){
		sakiSeigyo();
	};
}

/**
 * （付替先）取引先クリアボタン押下時Function
 */
function sakiTorihikisakiClear() {
	var meisai = $(this).closest("div.meisai");
	meisai.find("[name=sakiTorihikisakiCd]").val("");
	meisai.find("[name=sakiTorihikisakiNameRyakushiki]").val("");
}

/**
 * （付替先）取引先コードロストフォーカス時Function
 */
function torihikisakiCdLostFocus() {
	var meisai = $(this).closest("div.meisai");
	var title = $(this).parent().parent().find(".control-label").text();
	dialogRetTorihikisakiCd		= meisai.find("[name=sakiTorihikisakiCd]");
	dialogRetTorihikisakiName	= meisai.find("[name=sakiTorihikisakiNameRyakushiki]");
	dialogRetJigyoushaKbn 		= meisai.find("[name=sakiJigyoushaKbn]");
	commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd, dialogRetTorihikisakiName, title, null, null,dialogRetJigyoushaKbn);
	var flg = meisai.find("[name=sakiJigyoushaKbnFlg]").val();
	meisai.find("select[name=sakiJigyoushaKbn]:eq(0)").prop("selected",(0 == flg));
	meisai.find("select[name=sakiJigyoushaKbn]:eq(1)").prop("selected",(1 == flg));
	dialogCallbackTorihikisakiSentaku = function(){
		sakiSeigyo();
	};
}

/**
 * （付替先）プロジェクト選択ボタン押下時Function
 */
function sakiProjectSentaku() {
	var meisai = $(this).closest("div.meisai");
	dialogRetProjectCd		= meisai.find("[name=sakiProjectCd]");
	dialogRetProjectName	= meisai.find("[name=sakiProjectName]");
	commonProjectSentaku();
}

/**
 * （付替先）プロジェクトクリアボタン押下時Function
 */
function sakiProjectClear() {
	var meisai = $(this).closest("div.meisai");
	meisai.find("[name=sakiProjectCd]").val("");
	meisai.find("[name=sakiProjectName]").val("");
}

/**
 * （付替先）プロジェクトコードロストフォーカス時Function
 */
function pjCdLostFocus() {
	var meisai = $(this).closest("div.meisai");
	var title = $(this).parent().parent().find(".control-label").text();
	dialogRetProjectCd	= meisai.find("input[name=sakiProjectCd]");
	dialogRetProjectName = meisai.find("input[name=sakiProjectName]");
	commonProjectCdLostFocus(dialogRetProjectCd, dialogRetProjectName, title);
}

/**
 * （付替先）セグメント選択ボタン押下時Function
 */
function sakiSegmentSentaku() {
	var meisai = $(this).closest("div.meisai");
	dialogRetSegmentCd		= meisai.find("[name=sakiSegmentCd]");
	dialogRetSegmentName	= meisai.find("[name=sakiSegmentName]");
	commonSegmentSentaku();
}

/**
 * （付替先）セグメントクリアボタン押下時Function
 */
function sakiSegmentClear() {
	var meisai = $(this).closest("div.meisai");
	meisai.find("[name=sakiSegmentCd]").val("");
	meisai.find("[name=sakiSegmentName]").val("");
}

/**
 * （付替先）セグメントコードロストフォーカス時Function
 */
function segmentCdLostFocus() {
	var meisai = $(this).closest("div.meisai");
	var title = $(this).parent().parent().find(".control-label").text();
	dialogRetSegmentCd	= meisai.find("input[name=sakiSegmentCd]");
	dialogRetSegmentName = meisai.find("input[name=sakiSegmentName]");
	commonSegmentCdLostFocus(dialogRetSegmentCd, dialogRetSegmentName, title);
}

/**
 * （付替先）ユニバーサルフィールド１選択ボタン押下時Function
 */
function sakiUf1Sentaku() {
	var meisai = $(this).closest("div.meisai");
	dialogRetUniversalFieldCd		= meisai.find("input[name=sakiUf1Cd]");
	dialogRetUniversalFieldName		= meisai.find("input[name=sakiUf1Name]");
	commonUniversalSentaku(meisai.find("[name=sakiKamokuCd]").val(), "1");
}

/**
 * （付替先）ユニバーサルフィールド１クリアボタン押下時Function
 */
function sakiUf1Clear() {
	var meisai = $(this).closest("div.meisai");
	meisai.find("input[name=sakiUf1Cd]").val("");
	meisai.find("input[name=sakiUf1Name]").val("");
}

/**
 * （付替先）ユニバーサルフィールド１コードロストフォーカス時Function
 */
function uf1CdLostFocus() {
	var meisai = $(this).closest("div.meisai");
	var title = $(this).parent().parent().find(".control-label").text();
	var isBtnVisible = $("*[name=sakiUf1SentakuButton]").length > 0;
	if (isBtnVisible) {
		dialogRetUniversalFieldCd		= meisai.find("input[name=sakiUf1Cd]");
		dialogRetUniversalFieldName		= meisai.find("input[name=sakiUf1Name]");
		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 1, title);
	}
}

/**
 * （付替先）ユニバーサルフィールド２選択ボタン押下時Function
 */
function sakiUf2Sentaku() {
	var meisai = $(this).closest("div.meisai");
	dialogRetUniversalFieldCd		= meisai.find("input[name=sakiUf2Cd]");
	dialogRetUniversalFieldName		= meisai.find("input[name=sakiUf2Name]");
	commonUniversalSentaku(meisai.find("[name=sakiKamokuCd]").val(), 2);
}

/**
 * （付替先）ユニバーサルフィールド２クリアボタン押下時Function
 */
function sakiUf2Clear() {
	var meisai = $(this).closest("div.meisai");
	meisai.find("input[name=sakiUf2Cd]").val("");
	meisai.find("input[name=sakiUf2Name]").val("");
}

/**
 * （付替先）ユニバーサルフィールド２コードロストフォーカス時Function
 */
function uf2CdLostFocus() {
	var meisai = $(this).closest("div.meisai");
	var title = $(this).parent().parent().find(".control-label").text();
	var isBtnVisible = $("*[name=sakiUf2SentakuButton]").length > 0;
	if (isBtnVisible) {
		dialogRetUniversalFieldCd		= meisai.find("input[name=sakiUf2Cd]");
		dialogRetUniversalFieldName		= meisai.find("input[name=sakiUf2Name]");
		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 2, title);
	}
}

/**
 * （付替先）ユニバーサルフィールド３選択ボタン押下時Function
 */
function sakiUf3Sentaku() {
	var meisai = $(this).closest("div.meisai");
	dialogRetUniversalFieldCd		= meisai.find("input[name=sakiUf3Cd]");
	dialogRetUniversalFieldName		= meisai.find("input[name=sakiUf3Name]");
	commonUniversalSentaku(meisai.find("[name=sakiKamokuCd]").val(), 3);
}

/**
 * （付替先）ユニバーサルフィールド３クリアボタン押下時Function
 */
function sakiUf3Clear() {
	var meisai = $(this).closest("div.meisai");
	meisai.find("input[name=sakiUf3Cd]").val("");
	meisai.find("input[name=sakiUf3Name]").val("");
}

/**
 * （付替先）ユニバーサルフィールド３コードロストフォーカス時Function
 */
function uf3CdLostFocus() {
	var meisai = $(this).closest("div.meisai");
	var title = $(this).parent().parent().find(".control-label").text();
	var isBtnVisible = $("*[name=sakiUf3SentakuButton]").length > 0;
	if (isBtnVisible) {
		dialogRetUniversalFieldCd		= meisai.find("input[name=sakiUf3Cd]");
		dialogRetUniversalFieldName		= meisai.find("input[name=sakiUf3Name]");
		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 3, title);
	}
}

/**
* （付替先）ユニバーサルフィールド４選択ボタン押下時Function
*/
function sakiUf4Sentaku() {
    var meisai = $(this).closest("div.meisai");
    dialogRetUniversalFieldCd = meisai.find("input[name=sakiUf4Cd]");
    dialogRetUniversalFieldName = meisai.find("input[name=sakiUf4Name]");
    commonUniversalSentaku(meisai.find("[name=sakiKamokuCd]").val(), 4);
}

/**
 * （付替先）ユニバーサルフィールド４クリアボタン押下時Function
 */
function sakiUf4Clear() {
    var meisai = $(this).closest("div.meisai");
    meisai.find("input[name=sakiUf4Cd]").val("");
    meisai.find("input[name=sakiUf4Name]").val("");
}

/**
 * （付替先）ユニバーサルフィールド４コードロストフォーカス時Function
 */
function uf4CdLostFocus() {
    var meisai = $(this).closest("div.meisai");
    var title = $(this).parent().parent().find(".control-label").text();
	var isBtnVisible = $("*[name=sakiUf4SentakuButton]").length > 0;
    if (isBtnVisible) {
        dialogRetUniversalFieldCd = meisai.find("input[name=sakiUf4Cd]");
        dialogRetUniversalFieldName = meisai.find("input[name=sakiUf4Name]");
        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 4, title);
    }
}
/**
 * （付替先）ユニバーサルフィールド５選択ボタン押下時Function
 */
function sakiUf5Sentaku() {
    var meisai = $(this).closest("div.meisai");
    dialogRetUniversalFieldCd = meisai.find("input[name=sakiUf5Cd]");
    dialogRetUniversalFieldName = meisai.find("input[name=sakiUf5Name]");
    commonUniversalSentaku(meisai.find("[name=sakiKamokuCd]").val(), 5);
}

/**
 * （付替先）ユニバーサルフィールド５クリアボタン押下時Function
 */
function sakiUf5Clear() {
    var meisai = $(this).closest("div.meisai");
    meisai.find("input[name=sakiUf5Cd]").val("");
    meisai.find("input[name=sakiUf5Name]").val("");
}

/**
 * （付替先）ユニバーサルフィールド５コードロストフォーカス時Function
 */
function uf5CdLostFocus() {
    var meisai = $(this).closest("div.meisai");
    var title = $(this).parent().parent().find(".control-label").text();
	var isBtnVisible = $("*[name=sakiUf5SentakuButton]").length > 0;
    if (isBtnVisible) {
        dialogRetUniversalFieldCd = meisai.find("input[name=sakiUf5Cd]");
        dialogRetUniversalFieldName = meisai.find("input[name=sakiUf5Name]");
        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 5, title);
    }
}
/**
 * （付替先）ユニバーサルフィールド６選択ボタン押下時Function
 */
function sakiUf6Sentaku() {
    var meisai = $(this).closest("div.meisai");
    dialogRetUniversalFieldCd = meisai.find("input[name=sakiUf6Cd]");
    dialogRetUniversalFieldName = meisai.find("input[name=sakiUf6Name]");
    commonUniversalSentaku(meisai.find("[name=sakiKamokuCd]").val(), 6);
}

/**
 * （付替先）ユニバーサルフィールド６クリアボタン押下時Function
 */
function sakiUf6Clear() {
    var meisai = $(this).closest("div.meisai");
    meisai.find("input[name=sakiUf6Cd]").val("");
    meisai.find("input[name=sakiUf6Name]").val("");
}

/**
 * （付替先）ユニバーサルフィールド６コードロストフォーカス時Function
 */
function uf6CdLostFocus() {
    var meisai = $(this).closest("div.meisai");
    var title = $(this).parent().parent().find(".control-label").text();
	var isBtnVisible = $("*[name=sakiUf6SentakuButton]").length > 0;
    if (isBtnVisible) {
        dialogRetUniversalFieldCd = meisai.find("input[name=sakiUf6Cd]");
        dialogRetUniversalFieldName = meisai.find("input[name=sakiUf6Name]");
        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 6, title);
    }
}
/**
 * （付替先）ユニバーサルフィールド７選択ボタン押下時Function
 */
function sakiUf7Sentaku() {
    var meisai = $(this).closest("div.meisai");
    dialogRetUniversalFieldCd = meisai.find("input[name=sakiUf7Cd]");
    dialogRetUniversalFieldName = meisai.find("input[name=sakiUf7Name]");
    commonUniversalSentaku(meisai.find("[name=sakiKamokuCd]").val(), 7);
}

/**
 * （付替先）ユニバーサルフィールド７クリアボタン押下時Function
 */
function sakiUf7Clear() {
    var meisai = $(this).closest("div.meisai");
    meisai.find("input[name=sakiUf7Cd]").val("");
    meisai.find("input[name=sakiUf7Name]").val("");
}

/**
 * （付替先）ユニバーサルフィールド７コードロストフォーカス時Function
 */
function uf7CdLostFocus() {
    var meisai = $(this).closest("div.meisai");
    var title = $(this).parent().parent().find(".control-label").text();
	var isBtnVisible = $("*[name=sakiUf7SentakuButton]").length > 0;
    if (isBtnVisible) {
        dialogRetUniversalFieldCd = meisai.find("input[name=sakiUf7Cd]");
        dialogRetUniversalFieldName = meisai.find("input[name=sakiUf7Name]");
        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 7, title);
    }
}
/**
 * （付替先）ユニバーサルフィールド８選択ボタン押下時Function
 */
function sakiUf8Sentaku() {
    var meisai = $(this).closest("div.meisai");
    dialogRetUniversalFieldCd = meisai.find("input[name=sakiUf8Cd]");
    dialogRetUniversalFieldName = meisai.find("input[name=sakiUf8Name]");
    commonUniversalSentaku(meisai.find("[name=sakiKamokuCd]").val(), 8);
}

/**
 * （付替先）ユニバーサルフィールド８クリアボタン押下時Function
 */
function sakiUf8Clear() {
    var meisai = $(this).closest("div.meisai");
    meisai.find("input[name=sakiUf8Cd]").val("");
    meisai.find("input[name=sakiUf8Name]").val("");
}

/**
 * （付替先）ユニバーサルフィールド８コードロストフォーカス時Function
 */
function uf8CdLostFocus() {
    var meisai = $(this).closest("div.meisai");
    var title = $(this).parent().parent().find(".control-label").text();
	var isBtnVisible = $("*[name=sakiUf8SentakuButton]").length > 0;
    if (isBtnVisible) {
        dialogRetUniversalFieldCd = meisai.find("input[name=sakiUf8Cd]");
        dialogRetUniversalFieldName = meisai.find("input[name=sakiUf8Name]");
        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 8, title);
    }
}
/**
 * （付替先）ユニバーサルフィールド９選択ボタン押下時Function
 */
function sakiUf9Sentaku() {
    var meisai = $(this).closest("div.meisai");
    dialogRetUniversalFieldCd = meisai.find("input[name=sakiUf9Cd]");
    dialogRetUniversalFieldName = meisai.find("input[name=sakiUf9Name]");
    commonUniversalSentaku(meisai.find("[name=sakiKamokuCd]").val(), 9);
}

/**
 * （付替先）ユニバーサルフィールド９クリアボタン押下時Function
 */
function sakiUf9Clear() {
    var meisai = $(this).closest("div.meisai");
    meisai.find("input[name=sakiUf9Cd]").val("");
    meisai.find("input[name=sakiUf9Name]").val("");
}

/**
 * （付替先）ユニバーサルフィールド９コードロストフォーカス時Function
 */
function uf9CdLostFocus() {
    var meisai = $(this).closest("div.meisai");
    var title = $(this).parent().parent().find(".control-label").text();
	var isBtnVisible = $("*[name=sakiUf9SentakuButton]").length > 0;
    if (isBtnVisible) {
        dialogRetUniversalFieldCd = meisai.find("input[name=sakiUf9Cd]");
        dialogRetUniversalFieldName = meisai.find("input[name=sakiUf9Name]");
        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 9, title);
    }
}
/**
 * （付替先）ユニバーサルフィールド１０選択ボタン押下時Function
 */
function sakiUf10Sentaku() {
    var meisai = $(this).closest("div.meisai");
    dialogRetUniversalFieldCd = meisai.find("input[name=sakiUf10Cd]");
    dialogRetUniversalFieldName = meisai.find("input[name=sakiUf10Name]");
    commonUniversalSentaku(meisai.find("[name=sakiKamokuCd]").val(), 10);
}

/**
 * （付替先）ユニバーサルフィールド１０クリアボタン押下時Function
 */
function sakiUf10Clear() {
    var meisai = $(this).closest("div.meisai");
    meisai.find("input[name=sakiUf10Cd]").val("");
    meisai.find("input[name=sakiUf10Name]").val("");
}

/**
 * （付替先）ユニバーサルフィールド１０コードロストフォーカス時Function
 */
function uf10CdLostFocus() {
    var meisai = $(this).closest("div.meisai");
    var title = $(this).parent().parent().find(".control-label").text();
	var isBtnVisible = $("*[name=sakiUf10SentakuButton]").length > 0;
    if (isBtnVisible) {
        dialogRetUniversalFieldCd = meisai.find("input[name=sakiUf10Cd]");
        dialogRetUniversalFieldName = meisai.find("input[name=sakiUf10Name]");
        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 10, title);
    }
}

/**
 * 明細+ボタン押下時Function
 */
function meisaiAdd() {

	//１行目の明細をコピー
	var div = $("#meisaiList div:first").clone();
	$("#meisaiList").append(div);

	//中身を消して明細番号を振リ直し
	inputClear(div);
	commonInit(div);
	meisaiReset();
	sakiSeigyo();
	displayTaishaku();
}

/**
 * 明細-ボタン押下時Function
 */
function meisaiDelete() {
	if(1 == $("#meisaiList").children().length) return;
	if(window.confirm("明細を削除してもよろしいですか？")) {
		$(this).closest("div.meisai").remove();
		meisaiReset();
	}
}

/**
 * 明細↑ボタン押下時Function
 */
function meisaiUp() {
	var div = $(this).closest("div.meisai");
	if(div.prev()){
		div.insertBefore(div.prev());
	}
	meisaiReset();
}

/**
 * 明細↓ボタン押下時Function
 */
function meisaiDown() {
	var div = $(this).closest("div.meisai");
	if(div.next()){
		div.insertAfter(div.next());
	}
	meisaiReset();
}

/**
 * 明細ｺﾋﾟｰボタン押下時Function
 */
function meisaiCopy() {

	//選択行の明細をコピー
	var div = $(this).closest("div.meisai");
	var divClone = div.clone();
	$("#meisaiList").append(divClone);

	//中身を消して明細番号を振リ直し
	commonInit(divClone);
	meisaiReset();
	displayTaishaku();
	
	// select部品の値をコピーする
	selectValCopy(div, divClone);
}

/**
 * 明細にナンバーを振る、金額の合計値を再計算するFunction
 */
function meisaiReset() {
	$("div.meisai").each(function(){
		$(this).find("h2 *.number").text(($("div.meisai").index($(this)) + 1));
	});

	//自動入力系やり直し
	calcMoney();
}

/**
 * 金額の再計算処理
 */
function calcMoney() {
	var motoKazeiKbnGroup = $("select[name=motoKazeiKbn] option:selected").attr("data-kazeiKbnGroup");
	var zeiritsu = Number($("select[name=zeiritsu]").find("option:selected").val());
	var shiharaiKingaku = $("input[name=kingaku]").val().replaceAll(",", "");
	var shouhizeigaku = $("input[name=shouhizeigaku]").val().replaceAll(",", "");
	var hasuuShoriFlg = $("#workflowForm").find("input[name=hasuuShoriFlg]").val();
	var shiireKeikaSothiFlg = $("#workflowForm").find("input[name=shiirezeigakuKeikasothi]").val();
	var motoJigyoushaFlg = $("select[name=motoJigyoushaKbn] option:selected").val();
	var motoJigyoshaNum = 0;
	if("1" == motoJigyoushaFlg){
		motoJigyoshaNum = 0.8;
	}else if("2" == motoJigyoushaFlg) {
		motoJigyoshaNum = 0.5;
	}
	
	getKeigenZeiritsuKbn();
	var zeiritsu			= Number($("select[name=zeiritsu]").find("option:selected").val());
	var hasuuKeisanKbn		= $("select[name=zeiritsu]").find("option:selected").attr("data-hasuuKeisanKbn");
	
	var goukeiKingaku		= 0;
	var goukeiHontaiKingaku = 0;
	var goukeiZeigaku		= 0;

	//--------
	//明細単位に本体金額、消費税額計算
	//--------
	$("div.meisai").each(function(){
		var sakiKazeiKbnGroup	= $(this).find("select[name=sakiKazeiKbn]").find("option:selected").attr("data-kazeiKbnGroup");
		var sakiKingaku			= $(this).find("input[name=sakiKingaku]").getMoney();
		var sakiZeigaku			= $(this).find("input[name=sakiShouhizeigaku]").getMoney();
		var sakiJigyoushaFlg	= $(this).find("select[name=sakiJigyoushaKbn]").find("option:selected").val();
		var hontaiKingaku 		= 0;
		var shouhizeigaku 		= 0;
		var sakiKazeiKbn = $(this).find("select[name=sakiKazeiKbn] option:selected").val();
		//課税区分：税込系 なら 金額＝画面の金額、税抜金額＝計算した金額、消費税額＝計算した金額
		if(sakiKazeiKbn =="001"|| sakiKazeiKbn=="011"|| sakiKazeiKbn=="012"){
			var sakiJigyoshaNum = 0;
			if("1" == sakiJigyoushaFlg){
				sakiJigyoshaNum = 0.8;
			}else if("2" == sakiJigyoushaFlg){
				sakiJigyoshaNum = 0.5;
			}
			
			var jigyoshaNum =  sakiJigyoshaNum == "0" ? motoJigyoshaNum : sakiJigyoshaNum;
			
			if(shiireKeikaSothiFlg == "1"){
				shouhizeigaku	= Number(hasuuKeisanZaimuFromImporter(sakiKingaku * zeiritsu / (100 + zeiritsu), hasuuShoriFlg, false));
				hontaiKingaku	= sakiKingaku - shouhizeigaku;
			}else if(shiireKeikaSothiFlg == "0"){
				//明細の支払金額から本体金額、消費税額を計算
				shouhizeigaku	= sakiKingaku * zeiritsu / (100 + zeiritsu);
				if(jigyoshaNum > 0) shouhizeigaku = shouhizeigaku * jigyoshaNum;
				//shouhizeigaku	= hasuuKeisanZaimu(shiharaiKingaku * zeiritsu / (100 + zeiritsu), hasuuShoriFlg, false);
				shouhizeigaku	= Number(hasuuKeisanZaimuFromImporter(shouhizeigaku, hasuuShoriFlg, false));
				hontaiKingaku	= sakiKingaku - shouhizeigaku;
			}
		//それ以外(課税区分：税抜系,課税区分：消費税関係ない区分) 金額＝画面の金額、税抜金額＝画面の金額、消費税額＝0
		}else{
			hontaiKingaku = sakiKingaku;
		}

		//計算した本体金額、消費税額の値をセット or クリア
		$(this).find("input[name=sakiHontaiKingaku]").putMoney(hontaiKingaku);
		$(this).find("input[name=sakiShouhizeigaku]").putMoney(shouhizeigaku);

		goukeiZeigaku = goukeiZeigaku + shouhizeigaku;
		goukeiKingaku = goukeiKingaku + sakiKingaku;
	});
	$("input[name=kingaku]").putMoney(goukeiKingaku);
	$("input[name=shouhizeigaku]").putMoney(goukeiZeigaku);
	$("input[name=hontaiKingaku]").putMoney(goukeiKingaku - goukeiZeigaku);
}

/**
* 分離区分・仕入区分・税額計算方式の表示制御
* @param m tbody.meisai
* @param isMoto true:付替元 false:付替先
*/
function koumokuSeigyo(m, isMoto){
	jigyoushaKbnSeigyo(m, isMoto);
	let uriagezeiKeisan = $("#workflowForm").find("input[name=uriagezeigakuKeisan]").val();
	let shouhizeiKbn = $("#workflowForm").find("input[name=shouhizeikbn]").val();
 	let shiireZeiAnbun = $("#workflowForm").find("input[name=shiireZeiAnbun]").val();
	let shoriGroup = isMoto ? $("input[name=motoShoriGroup]").val() : m.find("input[name=sakiShoriGroup]").val();
	let kazeiKbn = isMoto ? $("select[name=motoKazeiKbn] option:selected").val() : m.find("select[name=sakiKazeiKbn] option:selected").val();
	let zeigakuHoushiki = isMoto ? $("select[name=motoZeigakuHoushiki]") : m.find("select[name=sakiZeigakuHoushiki]");
	let zeigakuHoushikiLbl = isMoto ? $("label[for=motoZeigakuHoushiki]") : m.find("label[for=sakiZeigakuHoushiki]");
	//税額計算方式の制御
	if(uriagezeiKeisan == "2"){
		if((['4', '9'].includes(shoriGroup)) && (['001', '002', '012', '014'].includes(kazeiKbn))){
			zeigakuHoushiki.prop("disabled", false);
			if(zeigakuHoushiki.val() == "9"){
				zeigakuHoushiki.val("0");
			}
		}else{
			zeigakuHoushiki.val("9");
			zeigakuHoushiki.prop("disabled", true);
		}
	}else{
		zeigakuHoushikiLbl.hide();
		zeigakuHoushiki.hide();
		if(['4', '9'].includes(shoriGroup) && ['001', '002', '012', '014'].includes(kazeiKbn)){
			zeigakuHoushiki.val(uriagezeiKeisan);			
		}else{
			zeigakuHoushiki.val("9");
		}
	}
	let zeiritsuFlg =  $("select[name=zeiritsu]").val();
	if(isMoto){
		setShouhizeiControls($("input[name=denpyouId]").val(), $("input[name=motoShoriGroup]").val(), $("select[name=motoKazeiKbn]"), $("select[name=zeiritsu]"), $("select[name=motoBunriKbn]"), $("select[name=motoShiireKbn]"));
		// 課税区分が内部コードにない値の場合、「未設定」にする
		if($("select[name=motoKazeiKbn]").val() == null){
			$("select[name=motoKazeiKbn]").val("");
		}
	}else{
		//付替先 明細複数行の場合の連動回避のためjsp側に記載
		m.find("select[name=sakiKazeiKbn] option").hide();
		if (['3', '4', '5', '6', '9', '10'].includes(shoriGroup)) {
			m.find("select[name=sakiKazeiKbn] option[value=000],option[value=001],option[value=003],option[value=004]").show();
			m.find('select[name=sakiKazeiKbn]').prop('disabled', false);
	        // 1-1-1. shouhizeiKbnが1,2ならばkazeiKbnのvalue=002の選択肢を追加で表示
	        if (['1', '2'].includes(shouhizeiKbn)) {
	            m.find('select[name=sakiKazeiKbn]  option[value=002]').show();
	        }
	    }
	    // 1-2. shoriGroupが2,7,8ならば、kazeiKbnのvalue=000, 003, 011, 012, 041, 042の選択肢を表示
	    else if (['2', '7', '8'].includes(shoriGroup)) {
	         m.find('select[name=sakiKazeiKbn] option[value=000],option[value=003],option[value=011],option[value=012],option[value=041],option[value=042]').show();
	         m.find('select[name=sakiKazeiKbn]').prop('disabled', false);
	        // 1-2-1. shouhizeiKbnが1,2ならばkazeiKbnのvalue=013, 014の選択肢を追加で表示
	        if (['1', '2'].includes(shouhizeiKbn)) {
	            m.find('select[name=sakiKazeiKbn] option[value=013], select[name=sakiKazeiKbn] option[value=014]').show();
	        }
	        // 1-3. shoriGroupが7ならば、kazeiKbnのvalue=004も追加で表示
	        if (shoriGroup == 7) {
	             m.find('select[name=sakiKazeiKbn] option[value=004]').show();
	        }
	    }
	    // 1-4. 上記以外の処理グループならば、kazeiKbnはvalue=100の選択肢のみを表示し、固定で選択・使用不可
	    else {
	         m.find('select[name=sakiKazeiKbn] option[value=100]').show().prop('selected', true);
	         m.find('select[name=sakiKazeiKbn]').prop('disabled', true);
	    }
		// 課税区分が内部コードにない値の場合、「未設定」にする
		if(m.find('select[name=sakiKazeiKbn]').val() == null){
			m.find('select[name=sakiKazeiKbn]').val("");
		}
		//消費税区分が0：税込方式なら問答無用でdisable
		//1：税抜方式　2：一括税抜方式のとき課税区分に従う
		//　課税区分について　2税抜・13課抜仕入・14課抜売上　は税抜扱い（SIAS消費税設定の情報より）
		//　1税込・11課込仕入・12課込売上　のみが自動分離を選択できる
		//　任意枝番で分離区分の登録がある枝番を選択した場合は枝番に登録されていた分離区分が優先される
		if(shouhizeiKbn == "0"){
			m.find("select[name=sakiBunriKbn]").val("9");
			m.find("select[name=sakiBunriKbn]").prop("disabled", true);
		//税抜系の場合、「無し」でdisable
		}else if(['002', '013', '014'].includes(kazeiKbn)){
			m.find("select[name=sakiBunriKbn]").prop("disabled", true);
			m.find("select[name=sakiBunriKbn]").val("0")
		}else if(['001', '011', '012'].includes(kazeiKbn)){
			m.find("select[name=sakiBunriKbn]").prop("disabled", false);
			if(m.find("select[name=sakiBunriKbn]").prop("selectedIndex") < 0 || m.find("select[name=sakiBunriKbn]").val() == "9"){
				m.find("select[name=sakiBunriKbn]").val("0");
			}
			//税抜方式の場合、自動分離 disabled固定
			if(shouhizeiKbn == "1"){
				m.find("select[name=sakiBunriKbn]").prop("disabled", true);
				m.find("select[name=sakiBunriKbn]").val("1");
			}
		}else {
			m.find("select[name=sakiBunriKbn]").prop("disabled", true);
			m.find("select[name=sakiBunriKbn]").val("9");
		}
		//仕入区分
		//仕入税額按分フラグが個別対応方式の場合enable
		//任意部門で仕入区分の登録がある部門を選択した場合は部門に登録されていた仕入区分が優先される
		if(shiireZeiAnbun == "1"){
			if((['2', '5', '6','7', '8', '10'].includes(shoriGroup)) && (['001', '002', '011','013'].includes(kazeiKbn))){
				m.find("select[name=sakiShiireKbn]").prop("disabled", false);
				if(m.find("select[name=sakiShiireKbn]").val() == "0"){
					m.find("select[name=sakiShiireKbn]").val("3");
				}
			}else{
				m.find("select[name=sakiShiireKbn]").val("0");
				m.find("select[name=sakiShiireKbn]").prop("disabled", true); //選択を「0」にして、グレーアウトにする
			}
		}
	}
	$("select[name=zeiritsu] option").show();
	$("select[name=zeiritsu]").val(zeiritsuFlg);
	$("select[name=zeiritsu]").prop("disabled",false);
}

/**
* 事業者区分の入力制御（入力可能な場合、必須となる）
* @param isKari true:借 false:貸
*/
function jigyoushaKbnSeigyo(m, isMoto){
	let taishaku = isMoto ? "moto" : "saki";
	var shoriGroup = isMoto ? $("input[name=motoShoriGroup]").val() : m.find("input[name=sakiShoriGroup]").val();
	var kazeiKbn = isMoto ? $("select[name=motoKazeiKbn] option:selected").val() : m.find("select[name=sakiKazeiKbn] option:selected").val();
	if($("#workflowForm").find("input[name=invoiceStartflg]").val() == "1"){
		if($("input[name=motoKamokuCd]").val() == ""){
			//取引をクリアした場合
			$("select[name=motoJigyoushaKbn]").prop("disabled", true);
			$("select[name=motoJigyoushaKbn]").val("0");
		}
		if(m.find("input[name=sakiKamokuCd]").val() == ""){
			//取引をクリアした場合
			m.find("select[name=sakiJigyoushaKbn]").prop("disabled", true);
			m.find("select[name=sakiJigyoushaKbn]").val("0");
		}
		//処理グループが21(仮払消費税)の場合、課税区分を考慮せず変更可能とする
		if((['2', '5', '6','7', '8', '10'].includes(shoriGroup) && ['001', '002', '011','013'].includes(kazeiKbn)) || shoriGroup == "21"){ 
			if(isMoto){
				$("select[name=motoJigyoushaKbn]").prop("disabled", false);
			}else{
				m.find("select[name=sakiJigyoushaKbn]").prop("disabled", false);
			}
		}else{
			if(isMoto){
				$("select[name=motoJigyoushaKbn]").prop("disabled", true); //選択を「0:通常課税」にして、グレーアウトにする
				$("select[name=motoJigyoushaKbn]").val("0");
			}else{
				m.find("select[name=sakiJigyoushaKbn]").prop("disabled", true); //選択を「0:通常課税」にして、グレーアウトにする
				m.find("select[name=sakiJigyoushaKbn]").val("0");
			}
		}
	}
}

//付替先用 項目制御まとめ
function sakiSeigyo(){
	$("div.meisai").each(function(){
		koumokuSeigyo($(this), false);
	});
}

/**
 * 明細のアクション紐付け
 * @param target
 */
function meisaiActionAdd(target) {
	//明細+-↑↓ｺﾋﾟｰボタン押下時アクション
	$("body").on("click","button[name=meisaiAdd]",meisaiAdd); 
	$("body").on("click","button[name=meisaiDelete]",meisaiDelete); 
	$("body").on("click","button[name=meisaiUp]",meisaiUp); 
	$("body").on("click","button[name=meisaiDown]",meisaiDown); 
	$("body").on("click","button[name=meisaiCopy]",meisaiCopy); 
	
	//選択ボタン押下時アクション
	$("body").on("click","button[name=sakiKamokuSentakuButton]",sakiKamokuSentaku); 
	$("body").on("click","button[name=sakiKamokuEdabanSentakuButton]",sakiKamokuEdabanSentaku);
	$("body").on("click","button[name=sakiKamokuEdabanClearButton]",sakiKamokuEdabanClear);
	$("body").on("click","button[name=sakiFutanBumonSentakuButton]",sakiFutanBumonSentaku);
	$("body").on("click","button[name=sakiFutanBumonClearButton]",sakiFutanBumonClear);
	$("body").on("click","button[name=sakiTorihikisakiSentakuButton]",sakiTorihikisakiSentaku);
	$("body").on("click","button[name=sakiTorihikisakiClearButton]",sakiTorihikisakiClear);
	$("body").on("click","button[name=sakiProjectSentakuButton]",sakiProjectSentaku);
	$("body").on("click","button[name=sakiProjectClearButton]",sakiProjectClear);
	$("body").on("click","button[name=sakiSegmentSentakuButton]",sakiSegmentSentaku);
	$("body").on("click","button[name=sakiSegmentClearButton]",sakiSegmentClear);
	$("body").on("click","button[name=sakiUf1SentakuButton]",sakiUf1Sentaku);
	$("body").on("click","button[name=sakiUf2SentakuButton]",sakiUf2Sentaku);
	$("body").on("click","button[name=sakiUf3SentakuButton]",sakiUf3Sentaku);
	$("body").on("click","button[name=sakiUf4SentakuButton]",sakiUf4Sentaku);
	$("body").on("click","button[name=sakiUf5SentakuButton]",sakiUf5Sentaku);
	$("body").on("click","button[name=sakiUf6SentakuButton]",sakiUf6Sentaku);
	$("body").on("click","button[name=sakiUf7SentakuButton]",sakiUf7Sentaku);
	$("body").on("click","button[name=sakiUf8SentakuButton]",sakiUf8Sentaku);
	$("body").on("click","button[name=sakiUf9SentakuButton]",sakiUf9Sentaku);
	$("body").on("click","button[name=sakiUf10SentakuButton]",sakiUf10Sentaku);
	$("body").on("click","button[name=sakiUf1ClearButton]",sakiUf1Clear);
	$("body").on("click","button[name=sakiUf2ClearButton]",sakiUf2Clear);
	$("body").on("click","button[name=sakiUf3ClearButton]",sakiUf3Clear);
	$("body").on("click","button[name=sakiUf4ClearButton]",sakiUf4Clear);
	$("body").on("click","button[name=sakiUf5ClearButton]",sakiUf5Clear);
	$("body").on("click","button[name=sakiUf6ClearButton]",sakiUf6Clear);
	$("body").on("click","button[name=sakiUf7ClearButton]",sakiUf7Clear);
	$("body").on("click","button[name=sakiUf8ClearButton]",sakiUf8Clear);
	$("body").on("click","button[name=sakiUf9ClearButton]",sakiUf9Clear);
	$("body").on("click","button[name=sakiUf10ClearButton]",sakiUf10Clear);

	//コードロストフォーカス時アクション
	$("body").on("blur","input[name=sakiKamokuCd]",kamokuCdLostFocus);
	$("body").on("blur","input[name=sakiKamokuEdabanCd]",kamokuEdabanCdLostFocus);
	$("body").on("blur","input[name=sakiFutanBumonCd]",futanBumonCdLostFocus);
	$("body").on("blur","input[name=sakiTorihikisakiCd]",torihikisakiCdLostFocus);
	$("body").on("blur","input[name=sakiProjectCd]",pjCdLostFocus);
	$("body").on("blur","input[name=sakiSegmentCd]",segmentCdLostFocus);
	$("body").on("blur","input[name=sakiUf1Cd]",uf1CdLostFocus);
	$("body").on("blur","input[name=sakiUf2Cd]",uf2CdLostFocus);
	$("body").on("blur","input[name=sakiUf3Cd]",uf3CdLostFocus);
	$("body").on("blur","input[name=sakiUf4Cd]",uf4CdLostFocus);
	$("body").on("blur","input[name=sakiUf5Cd]",uf5CdLostFocus);
	$("body").on("blur","input[name=sakiUf6Cd]",uf6CdLostFocus);
	$("body").on("blur","input[name=sakiUf7Cd]",uf7CdLostFocus);
	$("body").on("blur","input[name=sakiUf8Cd]",uf8CdLostFocus);
	$("body").on("blur","input[name=sakiUf9Cd]",uf9CdLostFocus);
	$("body").on("blur","input[name=sakiUf10Cd]",uf10CdLostFocus);

	//明細金額入力時アクション
	$("body").on("change","select[name=sakiKazeiKbn]",calcMoney);
	$("body").on("blur","input[name=sakiKingaku]",calcMoney);
	$("body").on("change","select[name=sakiKazeiKbn]",sakiSeigyo);
	$("body").on("change","select[name=sakiJigyoushaKbn]",calcMoney);
}

//初期表示処理
$(document).ready(function(){
	
	// 項目制御
	koumokuSeigyo($(this), true);
	sakiSeigyo();

<c:if test='${enableInput}'>

	//付替区分変更時
	$("input[name=tsukekaeKbn]").change(displayTaishaku);

	//（元）勘定科目選択ボタン押下時Function
	$("#motoKamokuSentakuButton").click(function(){
		dialogRetKamokuCd = $("input[name=motoKamokuCd]");
		dialogRetKamokuName = $("input[name=motoKamokuName]");
		dialogRetShoriGroup = $("input[name=motoShoriGroup]");
		dialogRetKazeiKbn = $("select[name=motoKazeiKbn]");
	    dialogRetBunriKbn = $("select[name=motoBunriKbn]");
	    dialogRetKariShiireKbn = $("select[name=motoShiireKbn]");
		dialogCallbackKanjyouKamokuSentaku = function(){
			// 科目選択時には科目枝番をクリア
			$("input[name=motoKamokuEdabanCd]").val("");
			$("input[name=motoKamokuEdabanName]").val("");
			if(dialogRetKamokuCd.val() == null || dialogRetKamokuCd.val() == "")
			{
				dialogRetKazeiKbn.val("100");
			}
			koumokuSeigyo($(document), true);
			jigyoushaKbnSeigyo($(document), true);
			$("input[name=motoFutanBumonCd]").blur();
		};
		commonKamokuSentaku();
	});
	
	//（元）勘定科目コードロストフォーカス時Function
	$("input[name=motoKamokuCd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetKamokuCd = $("input[name=motoKamokuCd]");
		dialogRetKamokuName = $("input[name=motoKamokuName]");
		dialogRetShoriGroup = $("input[name=motoShoriGroup]");
		dialogRetKazeiKbn = $("select[name=motoKazeiKbn]");
	    dialogRetBunriKbn = $("select[name=motoBunriKbn]");
	    dialogRetKariShiireKbn = $("select[name=motoShiireKbn]");
		dialogCallbackKanjyouKamokuSentaku = function(){
			// 科目選択時には科目枝番をクリア
			$("input[name=motoKamokuEdabanCd]").val("");
			$("input[name=motoKamokuEdabanName]").val("");
			if(dialogRetKamokuCd.val() == null || dialogRetKamokuCd.val() == "")
			{
				dialogRetKazeiKbn.val("100");
			}
			koumokuSeigyo($(document), true);
			jigyoushaKbnSeigyo($(document), true);
			$("input[name=motoFutanBumonCd]").blur();
		};
		commonKamokuCdLostFocus(dialogRetKamokuCd, dialogRetKamokuName, title, null, null, dialogRetKazeiKbn, dialogRetBunriKbn, dialogRetKariShiireKbn, dialogRetShoriGroup);
	});

	//（元）勘定科目枝番選択ボタン押下時Function
	$("#motoKamokuEdabanSentakuButton").click(function(){
		if($("input[name=motoKamokuCd]").val() == "") {
			alert("${su:htmlEscape(ks.kamoku.name)}を入力してください。");
		}else {
			dialogRetKamokuEdabanCd		= $("input[name=motoKamokuEdabanCd]");
			dialogRetKamokuEdabanName	= $("input[name=motoKamokuEdabanName]");
			dialogRetKazeiKbn 			= $("select[name=motoKazeiKbn]");
			dialogRetBunriKbn 			= $("select[name=motoBunriKbn]");
			dialogCallbackKanjyouKamokuEdabanSentaku = function(){
				koumokuSeigyo($(document), true);
				calcMoney();
				$("input[name=motoFutanBumonCd]").blur();
			};
			commonKamokuEdabanSentaku($("input[name=motoKamokuCd]").val());
		}
	});

	//（元）勘定科目枝番クリアボタン押下時Function
	$("#motoKamokuEdabanClearButton").click(function(){
		$("input[name=motoKamokuEdabanCd]").val("");
		$("input[name=motoKamokuEdabanName]").val("");
	});
	
	//（元）勘定科目枝番コードロストフォーカス時Function
	$("input[name=motoKamokuEdabanCd]").blur(function(){
		var kamokuCd = 	$("input[name=motoKamokuCd]").val();
		dialogRetKamokuEdabanCd				= $("input[name=motoKamokuEdabanCd]");
		dialogRetKamokuEdabanName			= $("input[name=motoKamokuEdabanName]");
		dialogRetKazeiKbn					= $("select[name=motoKazeiKbn]");
		dialogRetBunriKbn					= $("select[name=motoBunriKbn]");
		if(kamokuCd == "" && dialogRetKamokuEdabanCd.val() != "") {
			alert("${su:htmlEscape(ks.kamoku.name)}を入力してください。");
		}else {
			var title = $(this).parent().parent().find(".control-label").text();
			dialogCallbackKanjyouKamokuEdabanSentaku = function(){
				koumokuSeigyo($(document), true);
				calcMoney();
				$("input[name=motoFutanBumonCd]").blur();
			};
			commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamokuCd, title, dialogRetKazeiKbn, dialogRetBunriKbn);
		}
	});

	//（元）負担部門選択ボタン押下時Function
	$("#motoFutanBumonSentakuButton").click(function(){
		dialogRetFutanBumonCd			= $("input[name=motoFutanBumonCd]");
		dialogRetFutanBumonName			= $("input[name=motoFutanBumonName]");
		dialogRetKariShiireKbn 				= $("select[name=motoShiireKbn]");
		commonFutanBumonSentaku("1",$("input[name=motoKamokuCd]").val(),$("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val());
	});

	//（元）負担部門クリアボタン押下時Function
	$("#motoFutanBumonClearButton").click(function(){
		$("input[name=motoFutanBumonCd]").val("");
		$("input[name=motoFutanBumonName]").val("");
	});
	
	//（元）負担部門コードロストフォーカス時Function
	$("input[name=motoFutanBumonCd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetFutanBumonCd				= $("input[name=motoFutanBumonCd]");
		dialogRetFutanBumonName				= $("input[name=motoFutanBumonName]");
		dialogRetKariShiireKbn 					= $("select[name=motoShiireKbn]");
		commonFutanBumonCdLostFocus("1",dialogRetFutanBumonCd, dialogRetFutanBumonName, title,$("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val(), $("[name=motoKamokuCd]").val(), dialogRetKariShiireKbn);
	});

	//（元）取引先選択ボタン押下時、ダイアログ表示
	$("#motoTorihikisakiSentakuButton").click(function(){
		dialogRetTorihikisakiCd			= $("[name=motoTorihikisakiCd]");
		dialogRetTorihikisakiName		= $("[name=motoTorihikisakiNameRyakushiki]");
		dialogRetJigyoushaKbn 			= $("[name=motoJigyoushaKbn]");
		commonTorihikisakiSentaku();
		dialogCallbackTorihikisakiSentaku = function(){
			calcMoney();
			jigyoushaKbnSeigyo($(document), true);
		};
	});

	//（元）取引先クリアボタン押下時、取引先クリア
	$("#motoTorihikisakiClearButton").click(function(){
		$("[name=motoTorihikisakiCd]").val("");
		$("[name=motoTorihikisakiNameRyakushiki]").val("");
	});
	
	//（元）取引先コードロストフォーカス時、取引先名称表示
	$("[name=motoTorihikisakiCd]").blur(function(){
	 	var title = $(this).parent().parent().find(".control-label").text();
		dialogRetTorihikisakiCd			= $("[name=motoTorihikisakiCd]");
		dialogRetTorihikisakiName		= $("[name=motoTorihikisakiNameRyakushiki]");
		dialogRetJigyoushaKbn 			= $("[name=motoJigyoushaKbn]");
		commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd, dialogRetTorihikisakiName, title, null, null,dialogRetJigyoushaKbn);
		var flg = $("[name=motoJigyoushaKbnFlg]").val();
		$("select[name=motoJigyoushaKbn]:eq(0)").prop("selected",(0 == flg));
		$("select[name=motoJigyoushaKbn]:eq(1)").prop("selected",(1 == flg));
		dialogCallbackTorihikisakiSentaku = function(){
			calcMoney();
			jigyoushaKbnSeigyo($(document), true);
		};
	});

	//（元）プロジェクト選択ボタン押下時、ダイアログ表示
	$("#motoProjectSentakuButton").click(function(){
		dialogRetProjectCd			= $("[name=motoProjectCd]");
		dialogRetProjectName		= $("[name=motoProjectName]");
		commonProjectSentaku();
	});

	//（元）プロジェクトクリアボタン押下時、取引先クリア
	$("#motoProjectClearButton").click(function(){
		$("[name=motoProjectCd]").val("");
		$("[name=motoProjectName]").val("");
	});
	
	//（元）プロジェクトコードロストフォーカス時Function
	$("input[name=motoProjectCd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetProjectCd	= $("input[name=motoProjectCd]");
		dialogRetProjectName = $("input[name=motoProjectName]");
		commonProjectCdLostFocus(dialogRetProjectCd, dialogRetProjectName, title);
	});

	//（元）セグメント選択ボタン押下時、ダイアログ表示
	$("#motoSegmentSentakuButton").click(function(){
		dialogRetSegmentCd			= $("[name=motoSegmentCd]");
		dialogRetSegmentName		= $("[name=motoSegmentName]");
		commonSegmentSentaku();
	});

	//（元）セグメントクリアボタン押下時、取引先クリア
	$("#motoSegmentClearButton").click(function(){
		$("[name=motoSegmentCd]").val("");
		$("[name=motoSegmentName]").val("");
	});

	//（元）セグメントコードロストフォーカス時Function
	$("input[name=motoSegmentCd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetSegmentCd	= $("input[name=motoSegmentCd]");
		dialogRetSegmentName = $("input[name=motoSegmentName]");
		commonSegmentCdLostFocus(dialogRetSegmentCd, dialogRetSegmentName, title);
	});
	
	//（元）ユニバーサルフィールド１選択ボタン押下時Function
	$("#motoUf1SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=motoUf1Cd]");
		dialogRetUniversalFieldName		= $("input[name=motoUf1Name]");
		commonUniversalSentaku($("[name=motoKamokuCd]").val(), 1);
	});

	//（元）ユニバーサルフィールド１クリアボタン押下時Function
	$("#motoUf1ClearButton").click(function(e){
		$("input[name=motoUf1Cd]").val("");
		$("input[name=motoUf1Name]").val("");
	});
	
	//（元）ユニバーサルフィールド１コードロストフォーカス時Function
	$("input[name=motoUf1Cd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#motoUf1SentakuButton").length > 0;
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=motoUf1Cd]");
			dialogRetUniversalFieldName		= $("input[name=motoUf1Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 1, title);
		}
	});

	//（元）ユニバーサルフィールド２選択ボタン押下時Function
	$("#motoUf2SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=motoUf2Cd]");
		dialogRetUniversalFieldName		= $("input[name=motoUf2Name]");
		commonUniversalSentaku($("[name=motoKamokuCd]").val(), 2);
	});
		
	//（元）ユニバーサルフィールド２クリアボタン押下時Function
	$("#motoUf2ClearButton").click(function(e){
		$("input[name=motoUf2Cd]").val("");
		$("input[name=motoUf2Name]").val("");
	});
	
	//（元）ユニバーサルフィールド２コードロストフォーカス時Function
	$("input[name=motoUf2Cd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#motoUf2SentakuButton").length > 0;
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=motoUf2Cd]");
			dialogRetUniversalFieldName		= $("input[name=motoUf2Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 2, title);
		}
	});

	//（元）ユニバーサルフィールド３選択ボタン押下時Function
	$("#motoUf3SentakuButton").click(function(e){
		dialogRetUniversalFieldCd		= $("input[name=motoUf3Cd]");
		dialogRetUniversalFieldName		= $("input[name=motoUf3Name]");
		commonUniversalSentaku($("[name=motoKamokuCd]").val(), 3);
	});

	//ユニバーサルフィールド３クリアボタン押下時Function
	$("#motoUf3ClearButton").click(function(e){
		$("input[name=motoUf3Cd]").val("");
		$("input[name=motoUf3Name]").val("");
	});
		
	//（元）ユニバーサルフィールド３コードロストフォーカス時Function
	$("input[name=motoUf3Cd]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var isBtnVisible = $("#motoUf3SentakuButton").length > 0;
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=motoUf3Cd]");
			dialogRetUniversalFieldName		= $("input[name=motoUf3Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 3, title);
		}
	});

	//（元）ユニバーサルフィールド４選択ボタン押下時Function
	$("#motoUf4SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=motoUf4Cd]");
	    dialogRetUniversalFieldName = $("input[name=motoUf4Name]");
	    commonUniversalSentaku($("[name=motoKamokuCd]").val(), 4);
	});

	//ユニバーサルフィールド４クリアボタン押下時Function
	$("#motoUf4ClearButton").click(function (e) {
	    $("input[name=motoUf4Cd]").val("");
	    $("input[name=motoUf4Name]").val("");
	});

	//（元）ユニバーサルフィールド４コードロストフォーカス時Function
	$("input[name=motoUf4Cd]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#motoUf4SentakuButton").length > 0;
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=motoUf4Cd]");
	        dialogRetUniversalFieldName = $("input[name=motoUf4Name]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 4, title);
	    }
	});
	//（元）ユニバーサルフィールド５選択ボタン押下時Function
	$("#motoUf5SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=motoUf5Cd]");
	    dialogRetUniversalFieldName = $("input[name=motoUf5Name]");
	    commonUniversalSentaku($("[name=motoKamokuCd]").val(), 5);
	});

	//ユニバーサルフィールド５クリアボタン押下時Function
	$("#motoUf5ClearButton").click(function (e) {
	    $("input[name=motoUf5Cd]").val("");
	    $("input[name=motoUf5Name]").val("");
	});

	//（元）ユニバーサルフィールド５コードロストフォーカス時Function
	$("input[name=motoUf5Cd]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#motoUf5SentakuButton").length > 0;
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=motoUf5Cd]");
	        dialogRetUniversalFieldName = $("input[name=motoUf5Name]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 5, title);
	    }
	});
	//（元）ユニバーサルフィールド６選択ボタン押下時Function
	$("#motoUf6SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=motoUf6Cd]");
	    dialogRetUniversalFieldName = $("input[name=motoUf6Name]");
	    commonUniversalSentaku($("[name=motoKamokuCd]").val(), 6);
	});

	//ユニバーサルフィールド６クリアボタン押下時Function
	$("#motoUf6ClearButton").click(function (e) {
	    $("input[name=motoUf6Cd]").val("");
	    $("input[name=motoUf6Name]").val("");
	});

	//（元）ユニバーサルフィールド６コードロストフォーカス時Function
	$("input[name=motoUf6Cd]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#motoUf6SentakuButton").length > 0;
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=motoUf6Cd]");
	        dialogRetUniversalFieldName = $("input[name=motoUf6Name]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 6, title);
	    }
	});
	//（元）ユニバーサルフィールド７選択ボタン押下時Function
	$("#motoUf7SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=motoUf7Cd]");
	    dialogRetUniversalFieldName = $("input[name=motoUf7Name]");
	    commonUniversalSentaku($("[name=motoKamokuCd]").val(), 7);
	});

	//ユニバーサルフィールド７クリアボタン押下時Function
	$("#motoUf7ClearButton").click(function (e) {
	    $("input[name=motoUf7Cd]").val("");
	    $("input[name=motoUf7Name]").val("");
	});

	//（元）ユニバーサルフィールド７コードロストフォーカス時Function
	$("input[name=motoUf7Cd]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#motoUf7SentakuButton").length > 0;
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=motoUf7Cd]");
	        dialogRetUniversalFieldName = $("input[name=motoUf7Name]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 7, title);
	    }
	});
	//（元）ユニバーサルフィールド８選択ボタン押下時Function
	$("#motoUf8SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=motoUf8Cd]");
	    dialogRetUniversalFieldName = $("input[name=motoUf8Name]");
	    commonUniversalSentaku($("[name=motoKamokuCd]").val(), 8);
	});

	//ユニバーサルフィールド８クリアボタン押下時Function
	$("#motoUf8ClearButton").click(function (e) {
	    $("input[name=motoUf8Cd]").val("");
	    $("input[name=motoUf8Name]").val("");
	});

	//（元）ユニバーサルフィールド８コードロストフォーカス時Function
	$("input[name=motoUf8Cd]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#motoUf8SentakuButton").length > 0;
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=motoUf8Cd]");
	        dialogRetUniversalFieldName = $("input[name=motoUf8Name]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 8, title);
	    }
	});
	//（元）ユニバーサルフィールド９選択ボタン押下時Function
	$("#motoUf9SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=motoUf9Cd]");
	    dialogRetUniversalFieldName = $("input[name=motoUf9Name]");
	    commonUniversalSentaku($("[name=motoKamokuCd]").val(), 9);
	});

	//ユニバーサルフィールド９クリアボタン押下時Function
	$("#motoUf9ClearButton").click(function (e) {
	    $("input[name=motoUf9Cd]").val("");
	    $("input[name=motoUf9Name]").val("");
	});

	//（元）ユニバーサルフィールド９コードロストフォーカス時Function
	$("input[name=motoUf9Cd]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#motoUf9SentakuButton").length > 0;
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=motoUf9Cd]");
	        dialogRetUniversalFieldName = $("input[name=motoUf9Name]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 9, title);
	    }
	});
	//（元）ユニバーサルフィールド１０選択ボタン押下時Function
	$("#motoUf10SentakuButton").click(function (e) {
	    dialogRetUniversalFieldCd = $("input[name=motoUf10Cd]");
	    dialogRetUniversalFieldName = $("input[name=motoUf10Name]");
	    commonUniversalSentaku($("[name=motoKamokuCd]").val(), 10);
	});

	//ユニバーサルフィールド１０クリアボタン押下時Function
	$("#motoUf10ClearButton").click(function (e) {
	    $("input[name=motoUf10Cd]").val("");
	    $("input[name=motoUf10Name]").val("");
	});

	//（元）ユニバーサルフィールド１０コードロストフォーカス時Function
	$("input[name=motoUf10Cd]").blur(function () {
	    var title = $(this).parent().parent().find(".control-label").text();
	    var isBtnVisible = $("#motoUf10SentakuButton").length > 0;
	    if (isBtnVisible) {
	        dialogRetUniversalFieldCd = $("input[name=motoUf10Cd]");
	        dialogRetUniversalFieldName = $("input[name=motoUf10Name]");
	        commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 10, title);
	    }
	});

	//消費税変更時、金額再計算及び軽減税率区分の値再取得
	$("select[name=zeiritsu]").change(function(){
		calcMoney();
		getKeigenZeiritsuKbn();
	});
	
	$("select[name=motoKazeiKbn]").click(function(){
		koumokuSeigyo($(this).parent(), true);
		calcMoney();
	});
	
	$("select[name=motoJigyoushaKbn]").change(function(){
		calcMoney();
	});

	//明細のアクション紐付け
	meisaiActionAdd($("#meisaiList"));
	//摘要文字数制限取得
	$("input[name=sakiTekiyou]").attr('maxlength', $("#workflowForm").find("input[name=tekiyouMaxLength]").val());

	//金額計算
	calcMoney();
</c:if>

<c:if test='${not enableInput}'>
	//起票モードの場合のみ入力や選択可能
	$("#kaikeiContent").find("button").css("display", "none");
	$("#kaikeiContent").find("input,textarea,select").prop("disabled", true);
</c:if>

	//貸借表示
	displayTaishaku();
});

/**
 * イベントボタン押下時のアクションの切り替え
 */
function eventBtn(eventName) {
	var formObject = document.getElementById("workflowForm");

	switch (eventName) {
	case 'entry':
		formObject.action = 'sougou_tsukekae_denpyou_touroku';
		break;
	case 'update':
		formObject.action = 'sougou_tsukekae_denpyou_koushin';
		break;
	case 'shinsei':
		formObject.action = 'sougou_tsukekae_denpyou_shinsei';
		break;
	case 'shounin':
		formObject.action = 'sougou_tsukekae_denpyou_shounin';
		break;
	case 'shouninkaijo':
		formObject.action = 'sougou_tsukekae_denpyou_shouninkaijo';
		break;
	}
	formObject.method = 'post';
	formObject.target = '_self';
	$(formObject).submit();
}

</script>
