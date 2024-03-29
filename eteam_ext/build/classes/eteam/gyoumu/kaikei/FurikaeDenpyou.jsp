
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<style type='text/css'>
<!--
.row-title {
	background-color: #E0FFB0;
	  width: 105px;
	  text-align: right;
}
-->
</style>
<div id='kaikeiContent' class='form-horizontal'>
	
	<input type="hidden" name='uriagezeigakuKeisan' value='${su:htmlEscape(uriagezeigakuKeisan)}'>

	<section class='print-unit'>
		<h2>申請内容</h2>
		<div>
			<div class='control-group'>
				<label class='control-label' <c:if test='${not ks.denpyouDate.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.denpyouDate.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.denpyouDate.name)}</label>
				<div class='controls'>
					<input type='text' name='denpyouDate' class='input-small datepicker' value='${su:htmlEscape(denpyouDate)}'>
				<label class='label' <c:if test='${not ks.shouhyouShoruiFlg.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.shouhyouShoruiFlg.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.shouhyouShoruiFlg.name)}</label>
					<select name='shouhyouShoruiFlg' class='input-small'<c:if test='${not ks.shouhyouShoruiFlg.hyoujiFlg}'>style='display:none;'</c:if>>
						<option value='0' <c:if test='${shouhyouShoruiFlg eq "0"}'>selected</c:if>>なし</option>
						<option value='1' <c:if test='${shouhyouShoruiFlg eq "1"}'>selected = shouhyouShoruiFlg</c:if>>あり</option>
					</select>
				</div>
			</div>
			<div class='control-group'>
				<label class='control-label'<c:if test='${not ks.kingaku.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.kingaku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kingaku.name)}</label>
				<div class='controls'>
					<input type='text' name='kingaku' class='input-medium autoNumeric<c:if test="${enableInput}">WithCalcBox</c:if>' value='${su:htmlEscape(kingaku)}'>円
					<!-- 現時点では税額計算をしていないため「0」になるが念のためhiddenで保持 -->
					<input type='hidden' name='hontaiKingaku' class='input-medium autoNumeric' value='${su:htmlEscape(hontaiKingaku)}'>
					<input type='hidden' name='shouhizeigaku' class='input-medium autoNumeric' value='${su:htmlEscape(shouhizeigaku)}'>
				</div>
			</div>
			<%@ include file="./kogamen/HeaderField.jsp" %>
		</div>

		<div class='blank'></div>
			<div class='no-more-tables'>
				 <table class='table-bordered table-condensed'>
						<thead>
							<tr>
								<th>項目</th>
								<th>借方</th>
								<th>貸方</th>
							</tr>
						</thead>
						<tbody>
							<tr <c:if test='${not ks.kamoku.hyoujiFlg}'>style='display:none;'</c:if>>
								<th class='row-title'><c:if test='${ks.kamoku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kamoku.name)}</th>
								<td>									
									<input type='text' name='kariKamokuCd' class='input-small pc_only' value='${su:htmlEscape(kariKamokuCd)}' >
									<input type='text' name='kariKamokuName' class='' disabled value='${su:htmlEscape(kariKamokuName)}'>
									<input type='hidden' name='kariShoriGroup' value='${su:htmlEscape(kariShoriGroup)}'>
									<button type='button' id='kariKamokuSenakuButton' class='btn btn-small'>選択</button>
								</td>
								<td>									
									<input type='text' name='kashiKamokuCd' class='input-small pc_only' value='${su:htmlEscape(kashiKamokuCd)}' >
									<input type='text' name='kashiKamokuName' class='' disabled value='${su:htmlEscape(kashiKamokuName)}'>
									<input type='hidden' name='kashiShoriGroup' value='${su:htmlEscape(kashiShoriGroup)}'>
									<button type='button' id='kashiKamokuSenakuButton' class='btn btn-small'>選択</button>
								</td>
							</tr>
							<!-- 借方課税区分・消費税率・分離区分 -->
							<tr <c:if test='${not ks.kazeiKbn.hyoujiFlg}'>style='display:none;'</c:if>>
								<th class='row-title'><c:if test='${ks.kazeiKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kazeiKbn.name)}</th>
								<td>
									<select name='kariKazeiKbn' class='input-small'>
										<c:forEach var="tmpKazeiKbn" items="${kazeiKbnList}">
											<option value='${tmpKazeiKbn.naibu_cd}' data-kazeiKbnGroup='${tmpKazeiKbn.option1}' <c:if test='${tmpKazeiKbn.naibu_cd eq kariKazeiKbn}'>selected</c:if>>${su:htmlEscape(tmpKazeiKbn.name)}</option>
										</c:forEach>
									</select>
									<label class='label' for='kariZeiritsu'<c:if test='${not ks.zeiritsu.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.zeiritsu.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.zeiritsu.name)}</label>
									<select name='kariZeiritsu' class='input-small'>
										<c:forEach var="tmpZeiritsu" items="${zeiritsuList}">
											<option value='${tmpZeiritsu.zeiritsu}' data-hasuuKeisanKbn='${tmpZeiritsu.hasuu_keisan_kbn}' data-keigenZeiritsuKbn='${tmpZeiritsu.keigen_zeiritsu_kbn}'<c:if test='${tmpZeiritsu.zeiritsu eq kariZeiritsu && tmpZeiritsu.keigen_zeiritsu_kbn eq kariKeigenZeiritsuKbn}'>selected</c:if>><c:if test='${tmpZeiritsu.keigen_zeiritsu_kbn eq "1"}'>*</c:if>${tmpZeiritsu.zeiritsu}%</option>
										</c:forEach>
									</select>		
							<input type='hidden' name='kariKeigenZeiritsuKbn' value='${su:htmlEscape(kariKeigenZeiritsuKbn)}'>
								<span <c:if test='${not ks.bunriKbn.hyoujiFlg}'>style='display:none;'</c:if>>
								<label class='label' for='bunriKbn'><c:if test='${ks.bunriKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.bunriKbn.name)}</label>
									<select name='kariBunriKbn' class='input-small'>
										<c:forEach var="bunriKbnRecord" items="${bunriKbnList}">
										<c:if test='${bunriKbnRecord.naibu_cd eq "9"}'>
											<option hidden value='${bunriKbnRecord.naibu_cd}' <c:if test='${bunriKbnRecord.naibu_cd eq kariBunriKbn}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
										</c:if>
										<c:if test='${bunriKbnRecord.naibu_cd ne "9"}'>
											<option value='${bunriKbnRecord.naibu_cd}'<c:if test='${bunriKbnRecord.naibu_cd eq kariBunriKbn}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
										</c:if>
										</c:forEach>
									</select>
								</span>
								</td>
								
								<td>
									<select name='kashiKazeiKbn' class='input-small'>
										<c:forEach var="tmpKazeiKbn" items="${kazeiKbnList}">
											<option value='${tmpKazeiKbn.naibu_cd}' data-kazeiKbnGroup='${tmpKazeiKbn.option1}' <c:if test='${tmpKazeiKbn.naibu_cd eq kashiKazeiKbn}'>selected</c:if>>${su:htmlEscape(tmpKazeiKbn.name)}</option>
										</c:forEach>
									</select>
									<label class='label' for='kashiZeiritsu'<c:if test='${not ks.zeiritsu.hyoujiFlg}'>style='display:none;'</c:if>><c:if test='${ks.zeiritsu.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.zeiritsu.name)}</label>
									<select name='kashiZeiritsu' class='input-small'>
										<c:forEach var="tmpZeiritsu" items="${zeiritsuList}">
											<option value='${tmpZeiritsu.zeiritsu}' data-hasuuKeisanKbn='${tmpZeiritsu.hasuu_keisan_kbn}' data-keigenZeiritsuKbn='${tmpZeiritsu.keigen_zeiritsu_kbn}'<c:if test='${tmpZeiritsu.zeiritsu eq kashiZeiritsu && tmpZeiritsu.keigen_zeiritsu_kbn eq kashiKeigenZeiritsuKbn}'>selected</c:if>><c:if test='${tmpZeiritsu.keigen_zeiritsu_kbn eq "1"}'>*</c:if>${tmpZeiritsu.zeiritsu}%</option>
										</c:forEach>
									</select>	
											<input type='hidden' name='kashiKeigenZeiritsuKbn' value='${su:htmlEscape(kashiKeigenZeiritsuKbn)}'>
									<span <c:if test='${not ks.bunriKbn.hyoujiFlg}'>style='display:none;'</c:if>>
									<label class='label' for='bunriKbn'><c:if test='${ks.bunriKbn.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.bunriKbn.name)}</label>
										<select name='kashiBunriKbn' class='input-small'>
											<c:forEach var="bunriKbnRecord" items="${bunriKbnList}">
											<c:if test='${bunriKbnRecord.naibu_cd eq "9"}'>
												<option hidden value='${bunriKbnRecord.naibu_cd}' <c:if test='${bunriKbnRecord.naibu_cd eq kashibunriKbn}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
											</c:if>
											<c:if test='${bunriKbnRecord.naibu_cd ne "9"}'>
												<option value='${bunriKbnRecord.naibu_cd}'<c:if test='${bunriKbnRecord.naibu_cd eq kashiBunriKbn}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
											</c:if>
											</c:forEach>
										</select>
									</span>
								</td>
							</tr>
							
							<tr class='shiire'<c:if test='${not ks.shiireKbn.hyoujiFlg}'>style='display:none;'</c:if>>
								<th class='row-title'>${su:htmlEscape(ks.shiireKbn.name)}</th>
								<td>									
									<select name='kariShiireKbn' class='input-small'>
										<c:forEach var="shiireKbnRecord" items="${shiireKbnList}">
										<c:if test='${shiireKbnRecord.naibu_cd eq "0"}'>
											<option hidden value='${shiireKbnRecord.naibu_cd}' <c:if test='${shiireKbnRecord.naibu_cd eq kariShiireKbn}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
										</c:if>
										<c:if test='${shiireKbnRecord.naibu_cd ne "0"}'>
											<option value='${shiireKbnRecord.naibu_cd}' <c:if test='${shiireKbnRecord.naibu_cd eq kariShiireKbn}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
										</c:if>
											
										</c:forEach>
									</select>
									<input type='hidden' name='kariShiireKbnVal' value='${su:htmlEscape(kariShiireKbn)}'>
								</td>
								<td>									
									<select name='kashiShiireKbn'class='input-small'>
										<c:forEach var="shiireKbnRecord" items="${shiireKbnList}">
										<c:if test='${shiireKbnRecord.naibu_cd eq "0"}'>
											<option hidden value='${shiireKbnRecord.naibu_cd}' <c:if test='${shiireKbnRecord.naibu_cd eq kashiShiireKbn}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
										</c:if>
										<c:if test='${shiireKbnRecord.naibu_cd ne "0"}'>
											<option value='${shiireKbnRecord.naibu_cd}' <c:if test='${shiireKbnRecord.naibu_cd eq kashiShiireKbn}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
										</c:if>
											
										</c:forEach>
									</select>
								</td>
							</tr>

							<tr <c:if test='${not ks.kamokuEdaban.hyoujiFlg}'>style='display:none;'</c:if>>
								<th class='row-title'><c:if test='${ks.kamokuEdaban.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.kamokuEdaban.name)}</th>
								<td>									
									<input type='text' name='kariKamokuEdabanCd' class='input-medium pc_only' value='${su:htmlEscape(kariKamokuEdabanCd)}'>
									<input type='text' name='kariKamokuEdabanName' class='' disabled value='${su:htmlEscape(kariKamokuEdabanName)}'>
									<button type='button' id='kariKamokuEdabanSenakuButton' class='btn btn-small' >選択</button>
									<button type='button' id='kariKamokuEdabanClearButton' class='btn btn-small' >クリア</button>
								</td>
								<td>									
									<input type='text' name='kashiKamokuEdabanCd' class='input-medium pc_only' value='${su:htmlEscape(kashiKamokuEdabanCd)}'>
									<input type='text' name='kashiKamokuEdabanName' class='' disabled value='${su:htmlEscape(kashiKamokuEdabanName)}'>
									<button type='button' id='kashiKamokuEdabanSenakuButton' class='btn btn-small' >選択</button>
									<button type='button' id='kashiKamokuEdabanClearButton' class='btn btn-small' >クリア</button>
								</td>
							</tr>

							<tr <c:if test='${not ks.futanBumon.hyoujiFlg}'>style='display:none;'</c:if>>
								<th class='row-title' ><c:if test='${ks.futanBumon.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.futanBumon.name)}</th>
								<td>									
									<input type='text' name='kariFutanBumonCd' class='input-small pc_only' value='${su:htmlEscape(kariFutanBumonCd)}'>
									<input type='text' name='kariFutanBumonName' class='' disabled value='${su:htmlEscape(kariFutanBumonName)}' >
									<button type='button' id='kariFutanBumonSentakuButton' class='btn btn-small' >選択</button>
									<button type='button' id='kariFutanBumonClearButton' class='btn btn-small' >クリア</button>
								</td>
								<td>									
									<input type='text' name='kashiFutanBumonCd' class='input-small pc_only' value='${su:htmlEscape(kashiFutanBumonCd)}'>
									<input type='text' name='kashiFutanBumonName' class='' disabled value='${su:htmlEscape(kashiFutanBumonName)}'>
									<button type='button' id='kashiFutanBumonSentakuButton' class='btn btn-small' >選択</button>
									<button type='button' id='kashiFutanBumonClearButton' class='btn btn-small' >クリア</button>
								</td>
							</tr>

							<tr <c:if test='${not ks.torihikisaki.hyoujiFlg}'>style='display:none;'</c:if>>
								<th class='row-title'><c:if test='${ks.torihikisaki.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.torihikisaki.name)}</th>
								<td>									
									<input type='text' name='kariTorihikisakiCd' class='input-medium pc_only' value='${su:htmlEscape(kariTorihikisakiCd)}'>
									<input type='text' name='kariTorihikisakiName' disabled value='${su:htmlEscape(kariTorihikisakiName)}'>
									<button type='button' id='kariTorihikisakiSentakuButton' class='btn btn-small' >選択</button>
									<button type='button' id='kariTorihikisakiClearButton' class='btn btn-small' >クリア</button>
								</td>
								<td>									
									<input type='text' name='kashiTorihikisakiCd' class='input-medium pc_only' value='${su:htmlEscape(kashiTorihikisakiCd)}'>
									<input type='text' name='kashiTorihikisakiName' disabled value='${su:htmlEscape(kashiTorihikisakiName)}'>
									<button type='button' id='kashiTorihikisakiSentakuButton' class='btn btn-small' >選択</button>
									<button type='button' id='kashiTorihikisakiClearButton' class='btn btn-small' >クリア</button>
								</td>
							</tr>
							
							<tr class='invoiceOnly'<c:if test='${not ks.jigyoushaKbn.hyoujiFlg}'>style='display:none;'</c:if>>
								<th class='row-title' for='jigyoushaKbn'>${su:htmlEscape(ks.jigyoushaKbn.name)}</th>
								<td>	
								<div class='control-group' id='jigyoushaKbnDiv' name='kariJigyoushaKbn'>
									<c:forEach var="jigyoushaKbnRecord" items="${jigyoushaKbnList}">
										<label class='radio inline'><input type='radio' name='kariJigyoushaKbn' value='${su:htmlEscape(jigyoushaKbnRecord.naibu_cd)}' <c:if test='${jigyoushaKbnRecord.naibu_cd eq kariJigyoushaKbn}'>checked </c:if>>${su:htmlEscape(jigyoushaKbnRecord.name)}</label>
									</c:forEach>
									<label class='label' for='kariZeigakuHoushiki'>${su:htmlEscape(ks.uriagezeigakuKeisan.name)}</label>
										<select name='kariZeigakuHoushiki' class='input-small'>
											<c:forEach var="tmpZeigakuKeisan" items="${zeigakuKeisanList}">
												<c:if test='${tmpZeigakuKeisan.naibu_cd eq "9"}'>
													<option hidden value='${tmpZeigakuKeisan.naibu_cd}' <c:if test='${tmpZeigakuKeisan.naibu_cd eq kariZeigakuHoushiki}'>selected</c:if>>${tmpZeigakuKeisan.name}</option>
												</c:if>
												<c:if test='${tmpZeigakuKeisan.naibu_cd ne "9"}'>
													<option value='${tmpZeigakuKeisan.naibu_cd}' <c:if test='${tmpZeigakuKeisan.naibu_cd eq kariZeigakuHoushiki}'>selected</c:if>>${tmpZeigakuKeisan.name}</option>
												</c:if>
											</c:forEach>
										</select>
										<input type="hidden" name="zeigakuHoushikiHissuFlg" value='${su:htmlEscape(zeigakuHoushikiHissuFlg)}'>
								</div>								
								</td>
								<td>			
								<div class='control-group' id='jigyoushaKbnDiv' name='kashiJigyoushaKbn'>						
									<c:forEach var="jigyoushaKbnRecord" items="${jigyoushaKbnList}">
										<label class='radio inline'><input type='radio' name='kashiJigyoushaKbn' value='${su:htmlEscape(jigyoushaKbnRecord.naibu_cd)}' <c:if test='${jigyoushaKbnRecord.naibu_cd eq kashiJigyoushaKbn}'>checked </c:if>>${su:htmlEscape(jigyoushaKbnRecord.name)}</label>
									</c:forEach>
								<label class='label' for='kashiZeigakuHoushiki'><c:if test='${ks.uriagezeigakuKeisan.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.uriagezeigakuKeisan.name)}</label>
										 <select name='kashiZeigakuHoushiki' class='input-small'>
											<c:forEach var="tmpZeigakuKeisan" items="${zeigakuKeisanList}">
												<c:if test='${tmpZeigakuKeisan.naibu_cd eq "9"}'>
													<option hidden value='${tmpZeigakuKeisan.naibu_cd}' <c:if test='${tmpZeigakuKeisan.naibu_cd eq kashiZeigakuHoushiki}'>selected</c:if>>${tmpZeigakuKeisan.name}</option>
												</c:if>
												<c:if test='${tmpZeigakuKeisan.naibu_cd ne "9"}'>
													<option value='${tmpZeigakuKeisan.naibu_cd}' <c:if test='${tmpZeigakuKeisan.naibu_cd eq kashiZeigakuHoushiki}'>selected</c:if>>${tmpZeigakuKeisan.name}</option>
												</c:if>
											</c:forEach>
										</select>
								</div>
								</td>
							</tr>

							<tr <c:if test='${not("0" ne pjShiyouFlg and ks.project.hyoujiFlg)}'>style='display:none;'</c:if>>
								<th class='row-title'><c:if test='${ks.project.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.project.name)}</th>
								<td>									
									<input type='text' name='kariProjectCd' class='input-medium pc_only' value='${su:htmlEscape(kariProjectCd)}'>
									<input type='text' name='kariProjectName' disabled value='${su:htmlEscape(kariProjectName)}'>
									<button type='button' id='kariProjectSentakuButton' class='btn btn-small' >選択</button>
									<button type='button' id='kariProjectClearButton' class='btn btn-small' >クリア</button>
								</td>
								<td>									
									<input type='text' name='kashiProjectCd' class='input-medium pc_only' value='${su:htmlEscape(kashiProjectCd)}'>
									<input type='text' name='kashiProjectName' disabled value='${su:htmlEscape(kashiProjectName)}'>
									<button type='button' id='kashiProjectSentakuButton' class='btn btn-small' >選択</button>
									<button type='button' id='kashiProjectClearButton' class='btn btn-small' >クリア</button>
								</td>
							</tr>
							<tr <c:if test='${not("0" ne segmentShiyouFlg and ks.segment.hyoujiFlg)}'>style='display:none;'</c:if>>
								<th class='row-title'><c:if test='${ks.segment.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.segment.name)}</th>
								<td>									
									<input type='text' name='kariSegmentCd' class='input-medium pc_only' value='${su:htmlEscape(kariSegmentCd)}'>
									<input type='text' name='kariSegmentName' disabled value='${su:htmlEscape(kariSegmentName)}'>
									<button type='button' id='kariSegmentSentakuButton' class='btn btn-small' >選択</button>
									<button type='button' id='kariSegmentClearButton' class='btn btn-small' >クリア</button>
								</td>
								<td>									
									<input type='text' name='kashiSegmentCd' class='input-medium pc_only' value='${su:htmlEscape(kashiSegmentCd)}'>
									<input type='text' name='kashiSegmentName' disabled value='${su:htmlEscape(kashiSegmentName)}'>
									<button type='button' id='kashiSegmentSentakuButton' class='btn btn-small' >選択</button>
									<button type='button' id='kashiSegmentClearButton' class='btn btn-small' >クリア</button>
								</td>
							</tr>
							<tr <c:if test='${not("0" ne hfUfSeigyo.uf1ShiyouFlg and ks.uf1.hyoujiFlg)}'>style='display:none;'</c:if>>
								<th class='row-title'><c:if test='${ks.uf1.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf1Name)}</th>
								<td>
<c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or "1" eq hfUfSeigyo.uf1ShiyouFlg}'>
									<input type='text' name="kariUf1Cd" maxlength='20' value='${su:htmlEscape(kariUf1Cd)}'>
									<input type='hidden' name="kariUf1Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf1ShiyouFlg or "3" eq hfUfSeigyo.uf1ShiyouFlg}'>									
									<input type='text' name="kariUf1Cd"  class='pc_only' value='${su:htmlEscape(kariUf1Cd)}'>
									<input type='text' name="kariUf1Name" disabled value='${su:htmlEscape(kariUf1Name)}'>
									<button type='button' id='kariUf1SentakuButton' class='btn btn-small' >選択</button>
									<button type='button' id='kariUf1ClearButton' class='btn btn-small'>クリア</button>
</c:if>
								</td>
								<td>
<c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or "1" eq hfUfSeigyo.uf1ShiyouFlg}'>
									<input type='text' name="kashiUf1Cd" maxlength='20' value='${su:htmlEscape(kashiUf1Cd)}'>
									<input type='hidden' name="kashiUf1Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf1ShiyouFlg or "3" eq hfUfSeigyo.uf1ShiyouFlg}'>									
									<input type='text' name="kashiUf1Cd" class='pc_only' value='${su:htmlEscape(kashiUf1Cd)}'>
									<input type='text' name="kashiUf1Name" disabled value='${su:htmlEscape(kashiUf1Name)}'>
									<button type='button' id='kashiUf1SentakuButton' class='btn btn-small' >選択</button>
									<button type='button' id='kashiUf1ClearButton' class='btn btn-small'>クリア</button>
</c:if>
								</td>
							</tr>

							<tr <c:if test='${not("0" ne hfUfSeigyo.uf2ShiyouFlg and ks.uf2.hyoujiFlg)}'>style='display:none;'</c:if>>
								<th class='row-title'><c:if test='${ks.uf2.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf2Name)}</th>
								<td>
<c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or "1" eq hfUfSeigyo.uf2ShiyouFlg}'>
									<input type='text' name="kariUf2Cd" maxlength='20' value='${su:htmlEscape(kariUf2Cd)}'>
									<input type='hidden' name="kariUf2Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf2ShiyouFlg or "3" eq hfUfSeigyo.uf2ShiyouFlg}'>									
									<input type='text' name="kariUf2Cd"  class='pc_only' value='${su:htmlEscape(kariUf2Cd)}'>
									<input type='text' name="kariUf2Name" disabled value='${su:htmlEscape(kariUf2Name)}'>
									<button type='button' id='kariUf2SentakuButton' class='btn btn-small' >選択</button>
									<button type='button' id='kariUf2ClearButton' class='btn btn-small'>クリア</button>
</c:if>
								</td>
								<td>
<c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or "1" eq hfUfSeigyo.uf2ShiyouFlg}'>
									<input type='text' name="kashiUf2Cd" maxlength='20' value='${su:htmlEscape(kashiUf2Cd)}'>
									<input type='hidden' name="kashiUf2Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf2ShiyouFlg or "3" eq hfUfSeigyo.uf2ShiyouFlg}'>									
									<input type='text' name="kashiUf2Cd"  class='pc_only' value='${su:htmlEscape(kashiUf2Cd)}'>
									<input type='text' name="kashiUf2Name" disabled value='${su:htmlEscape(kashiUf2Name)}'>
									<button type='button' id='kashiUf2SentakuButton' class='btn btn-small' >選択</button>
									<button type='button' id='kashiUf2ClearButton' class='btn btn-small'>クリア</button>
</c:if>
								</td>
							</tr>

							<tr <c:if test='${not("0" ne hfUfSeigyo.uf3ShiyouFlg and ks.uf3.hyoujiFlg)}'>style='display:none;'</c:if>>
								<th class='row-title'><c:if test='${ks.uf3.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf3Name)}</th>
								<td>
<c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or "1" eq hfUfSeigyo.uf3ShiyouFlg}'>
									<input type='text' name="kariUf3Cd" maxlength='20' value='${su:htmlEscape(kariUf3Cd)}'>
									<input type='hidden' name="kariUf3Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf3ShiyouFlg or "3" eq hfUfSeigyo.uf3ShiyouFlg}'>									
									<input type='text' name="kariUf3Cd"  class='pc_only' value='${su:htmlEscape(kariUf3Cd)}'>
									<input type='text' name="kariUf3Name" disabled value='${su:htmlEscape(kariUf3Name)}'>
									<button type='button' id='kariUf3SentakuButton' class='btn btn-small' >選択</button>
									<button type='button' id='kariUf3ClearButton' class='btn btn-small'>クリア</button>
</c:if>
								</td>
								<td>
<c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or "1" eq hfUfSeigyo.uf3ShiyouFlg}'>
									<input type='text' name="kashiUf3Cd" maxlength='20' value='${su:htmlEscape(kashiUf3Cd)}'>
									<input type='hidden' name="kashiUf3Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf3ShiyouFlg or "3" eq hfUfSeigyo.uf3ShiyouFlg}'>								
									<input type='text' name="kashiUf3Cd"  class='pc_only' value='${su:htmlEscape(kashiUf3Cd)}'>
									<input type='text' name="kashiUf3Name" disabled value='${su:htmlEscape(kashiUf3Name)}'>
									<button type='button' id='kashiUf3SentakuButton' class='btn btn-small' >選択</button>
									<button type='button' id='kashiUf3ClearButton' class='btn btn-small'>クリア</button>
</c:if>
								</td>
							</tr>
					       <tr <c:if test='${not("0" ne hfUfSeigyo.uf4ShiyouFlg and ks.uf4.hyoujiFlg)}'>style='display:none;'</c:if>>
					        <th class='row-title'><c:if test='${ks.uf4.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf4Name)}</th>
					        <td>
					<c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or "1" eq hfUfSeigyo.uf4ShiyouFlg}'>
					         <input type='text' name="kariUf4Cd" maxlength='20' value='${su:htmlEscape(kariUf4Cd)}'>
					         <input type='hidden' name="kariUf4Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf4ShiyouFlg or "3" eq hfUfSeigyo.uf4ShiyouFlg}'>         
					         <input type='text' name="kariUf4Cd"  class='pc_only' value='${su:htmlEscape(kariUf4Cd)}'>
					         <input type='text' name="kariUf4Name" disabled value='${su:htmlEscape(kariUf4Name)}'>
					         <button type='button' id='kariUf4SentakuButton' class='btn btn-small' >選択</button>
					         <button type='button' id='kariUf4ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
					        </td>
					        <td>
					<c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or "1" eq hfUfSeigyo.uf4ShiyouFlg}'>
					         <input type='text' name="kashiUf4Cd" maxlength='20' value='${su:htmlEscape(kashiUf4Cd)}'>
					         <input type='hidden' name="kashiUf4Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf4ShiyouFlg or "3" eq hfUfSeigyo.uf4ShiyouFlg}'>        
					         <input type='text' name="kashiUf4Cd"  class='pc_only' value='${su:htmlEscape(kashiUf4Cd)}'>
					         <input type='text' name="kashiUf4Name" disabled value='${su:htmlEscape(kashiUf4Name)}'>
					         <button type='button' id='kashiUf4SentakuButton' class='btn btn-small' >選択</button>
					         <button type='button' id='kashiUf4ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
					        </td>
					       </tr>
						
					       <tr <c:if test='${not("0" ne hfUfSeigyo.uf5ShiyouFlg and ks.uf5.hyoujiFlg)}'>style='display:none;'</c:if>>
					        <th class='row-title'><c:if test='${ks.uf5.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf5Name)}</th>
					        <td>
					<c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or "1" eq hfUfSeigyo.uf5ShiyouFlg}'>
					         <input type='text' name="kariUf5Cd" maxlength='20' value='${su:htmlEscape(kariUf5Cd)}'>
					         <input type='hidden' name="kariUf5Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf5ShiyouFlg or "3" eq hfUfSeigyo.uf5ShiyouFlg}'>         
					         <input type='text' name="kariUf5Cd"  class='pc_only' value='${su:htmlEscape(kariUf5Cd)}'>
					         <input type='text' name="kariUf5Name" disabled value='${su:htmlEscape(kariUf5Name)}'>
					         <button type='button' id='kariUf5SentakuButton' class='btn btn-small' >選択</button>
					         <button type='button' id='kariUf5ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
					        </td>
					        <td>
					<c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or "1" eq hfUfSeigyo.uf5ShiyouFlg}'>
					         <input type='text' name="kashiUf5Cd" maxlength='20' value='${su:htmlEscape(kashiUf5Cd)}'>
					         <input type='hidden' name="kashiUf5Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf5ShiyouFlg or "3" eq hfUfSeigyo.uf5ShiyouFlg}'>        
					         <input type='text' name="kashiUf5Cd"  class='pc_only' value='${su:htmlEscape(kashiUf5Cd)}'>
					         <input type='text' name="kashiUf5Name" disabled value='${su:htmlEscape(kashiUf5Name)}'>
					         <button type='button' id='kashiUf5SentakuButton' class='btn btn-small' >選択</button>
					         <button type='button' id='kashiUf5ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
					        </td>
					       </tr>
						
					       <tr <c:if test='${not("0" ne hfUfSeigyo.uf6ShiyouFlg and ks.uf6.hyoujiFlg)}'>style='display:none;'</c:if>>
					        <th class='row-title'><c:if test='${ks.uf6.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf6Name)}</th>
					        <td>
					<c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or "1" eq hfUfSeigyo.uf6ShiyouFlg}'>
					         <input type='text' name="kariUf6Cd" maxlength='20' value='${su:htmlEscape(kariUf6Cd)}'>
					         <input type='hidden' name="kariUf6Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf6ShiyouFlg or "3" eq hfUfSeigyo.uf6ShiyouFlg}'>         
					         <input type='text' name="kariUf6Cd"  class='pc_only' value='${su:htmlEscape(kariUf6Cd)}'>
					         <input type='text' name="kariUf6Name" disabled value='${su:htmlEscape(kariUf6Name)}'>
					         <button type='button' id='kariUf6SentakuButton' class='btn btn-small' >選択</button>
					         <button type='button' id='kariUf6ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
					        </td>
					        <td>
					<c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or "1" eq hfUfSeigyo.uf6ShiyouFlg}'>
					         <input type='text' name="kashiUf6Cd" maxlength='20' value='${su:htmlEscape(kashiUf6Cd)}'>
					         <input type='hidden' name="kashiUf6Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf6ShiyouFlg or "3" eq hfUfSeigyo.uf6ShiyouFlg}'>        
					         <input type='text' name="kashiUf6Cd"  class='pc_only' value='${su:htmlEscape(kashiUf6Cd)}'>
					         <input type='text' name="kashiUf6Name" disabled value='${su:htmlEscape(kashiUf6Name)}'>
					         <button type='button' id='kashiUf6SentakuButton' class='btn btn-small' >選択</button>
					         <button type='button' id='kashiUf6ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
					        </td>
					       </tr>
						
					       <tr <c:if test='${not("0" ne hfUfSeigyo.uf7ShiyouFlg and ks.uf7.hyoujiFlg)}'>style='display:none;'</c:if>>
					        <th class='row-title'><c:if test='${ks.uf7.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf7Name)}</th>
					        <td>
					<c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or "1" eq hfUfSeigyo.uf7ShiyouFlg}'>
					         <input type='text' name="kariUf7Cd" maxlength='20' value='${su:htmlEscape(kariUf7Cd)}'>
					         <input type='hidden' name="kariUf7Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf7ShiyouFlg or "3" eq hfUfSeigyo.uf7ShiyouFlg}'>         
					         <input type='text' name="kariUf7Cd"  class='pc_only' value='${su:htmlEscape(kariUf7Cd)}'>
					         <input type='text' name="kariUf7Name" disabled value='${su:htmlEscape(kariUf7Name)}'>
					         <button type='button' id='kariUf7SentakuButton' class='btn btn-small' >選択</button>
					         <button type='button' id='kariUf7ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
					        </td>
					        <td>
					<c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or "1" eq hfUfSeigyo.uf7ShiyouFlg}'>
					         <input type='text' name="kashiUf7Cd" maxlength='20' value='${su:htmlEscape(kashiUf7Cd)}'>
					         <input type='hidden' name="kashiUf7Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf7ShiyouFlg or "3" eq hfUfSeigyo.uf7ShiyouFlg}'>        
					         <input type='text' name="kashiUf7Cd"  class='pc_only' value='${su:htmlEscape(kashiUf7Cd)}'>
					         <input type='text' name="kashiUf7Name" disabled value='${su:htmlEscape(kashiUf7Name)}'>
					         <button type='button' id='kashiUf7SentakuButton' class='btn btn-small' >選択</button>
					         <button type='button' id='kashiUf7ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
					        </td>
					       </tr>
						
					       <tr <c:if test='${not("0" ne hfUfSeigyo.uf8ShiyouFlg and ks.uf8.hyoujiFlg)}'>style='display:none;'</c:if>>
					        <th class='row-title'><c:if test='${ks.uf8.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf8Name)}</th>
					        <td>
					<c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or "1" eq hfUfSeigyo.uf8ShiyouFlg}'>
					         <input type='text' name="kariUf8Cd" maxlength='20' value='${su:htmlEscape(kariUf8Cd)}'>
					         <input type='hidden' name="kariUf8Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf8ShiyouFlg or "3" eq hfUfSeigyo.uf8ShiyouFlg}'>         
					         <input type='text' name="kariUf8Cd"  class='pc_only' value='${su:htmlEscape(kariUf8Cd)}'>
					         <input type='text' name="kariUf8Name" disabled value='${su:htmlEscape(kariUf8Name)}'>
					         <button type='button' id='kariUf8SentakuButton' class='btn btn-small' >選択</button>
					         <button type='button' id='kariUf8ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
					        </td>
					        <td>
					<c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or "1" eq hfUfSeigyo.uf8ShiyouFlg}'>
					         <input type='text' name="kashiUf8Cd" maxlength='20' value='${su:htmlEscape(kashiUf8Cd)}'>
					         <input type='hidden' name="kashiUf8Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf8ShiyouFlg or "3" eq hfUfSeigyo.uf8ShiyouFlg}'>        
					         <input type='text' name="kashiUf8Cd"  class='pc_only' value='${su:htmlEscape(kashiUf8Cd)}'>
					         <input type='text' name="kashiUf8Name" disabled value='${su:htmlEscape(kashiUf8Name)}'>
					         <button type='button' id='kashiUf8SentakuButton' class='btn btn-small' >選択</button>
					         <button type='button' id='kashiUf8ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
					        </td>
					       </tr>
						
					       <tr <c:if test='${not("0" ne hfUfSeigyo.uf9ShiyouFlg and ks.uf9.hyoujiFlg)}'>style='display:none;'</c:if>>
					        <th class='row-title'><c:if test='${ks.uf9.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf9Name)}</th>
					        <td>
					<c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or "1" eq hfUfSeigyo.uf9ShiyouFlg}'>
					         <input type='text' name="kariUf9Cd" maxlength='20' value='${su:htmlEscape(kariUf9Cd)}'>
					         <input type='hidden' name="kariUf9Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf9ShiyouFlg or "3" eq hfUfSeigyo.uf9ShiyouFlg}'>         
					         <input type='text' name="kariUf9Cd"  class='pc_only' value='${su:htmlEscape(kariUf9Cd)}'>
					         <input type='text' name="kariUf9Name" disabled value='${su:htmlEscape(kariUf9Name)}'>
					         <button type='button' id='kariUf9SentakuButton' class='btn btn-small' >選択</button>
					         <button type='button' id='kariUf9ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
					        </td>
					        <td>
					<c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or "1" eq hfUfSeigyo.uf9ShiyouFlg}'>
					         <input type='text' name="kashiUf9Cd" maxlength='20' value='${su:htmlEscape(kashiUf9Cd)}'>
					         <input type='hidden' name="kashiUf9Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf9ShiyouFlg or "3" eq hfUfSeigyo.uf9ShiyouFlg}'>        
					         <input type='text' name="kashiUf9Cd"  class='pc_only' value='${su:htmlEscape(kashiUf9Cd)}'>
					         <input type='text' name="kashiUf9Name" disabled value='${su:htmlEscape(kashiUf9Name)}'>
					         <button type='button' id='kashiUf9SentakuButton' class='btn btn-small' >選択</button>
					         <button type='button' id='kashiUf9ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
					        </td>
					       </tr>
						
					       <tr <c:if test='${not("0" ne hfUfSeigyo.uf10ShiyouFlg and ks.uf10.hyoujiFlg)}'>style='display:none;'</c:if>>
					        <th class='row-title'><c:if test='${ks.uf10.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(hfUfSeigyo.uf10Name)}</th>
					        <td>
					<c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or "1" eq hfUfSeigyo.uf10ShiyouFlg}'>
					         <input type='text' name="kariUf10Cd" maxlength='20' value='${su:htmlEscape(kariUf10Cd)}'>
					         <input type='hidden' name="kariUf10Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf10ShiyouFlg or "3" eq hfUfSeigyo.uf10ShiyouFlg}'>         
					         <input type='text' name="kariUf10Cd"  class='pc_only' value='${su:htmlEscape(kariUf10Cd)}'>
					         <input type='text' name="kariUf10Name" disabled value='${su:htmlEscape(kariUf10Name)}'>
					         <button type='button' id='kariUf10SentakuButton' class='btn btn-small' >選択</button>
					         <button type='button' id='kariUf10ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
					        </td>
					        <td>
					<c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or "1" eq hfUfSeigyo.uf10ShiyouFlg}'>
					         <input type='text' name="kashiUf10Cd" maxlength='20' value='${su:htmlEscape(kashiUf10Cd)}'>
					         <input type='hidden' name="kashiUf10Name" value=''>
					</c:if>
					<c:if test='${"2" eq hfUfSeigyo.uf10ShiyouFlg or "3" eq hfUfSeigyo.uf10ShiyouFlg}'>        
					         <input type='text' name="kashiUf10Cd"  class='pc_only' value='${su:htmlEscape(kashiUf10Cd)}'>
					         <input type='text' name="kashiUf10Name" disabled value='${su:htmlEscape(kashiUf10Name)}'>
					         <button type='button' id='kashiUf10SentakuButton' class='btn btn-small' >選択</button>
					         <button type='button' id='kashiUf10ClearButton' class='btn btn-small'>クリア</button>
					</c:if>
					        </td>
					       </tr>


							<tr <c:if test='${not ks.tekiyou.hyoujiFlg}'>style='display:none;'</c:if>>
								<th class='row-title'><c:if test='${ks.tekiyou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.tekiyou.name)}</th>
								<td colspan="2"><input type='text' name='tekiyou' class='input-block-level' maxlength='120' value='${su:htmlEscape(tekiyou)}'>
								<span style="line-height:25px;"><br><span style="color:#ff0000;">${su:htmlEscape(chuuki2)}</span></span>
								</td>
							</tr>
							<tr <c:if test='${not ks.bikou.hyoujiFlg}'>style='display:none;'</c:if>>
								<th class='row-title'><c:if test='${ks.bikou.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.bikou.name)}</th>
								<td colspan="2"><input type='text' name='bikou' class='input-block-level' maxlength='40' value='${su:htmlEscape(bikou)}'></td>
							</tr>

						</tbody>
				</table>
			</div>
	</section>
</div>

<!-- スクリプト -->
<script style='text/javascript'>
/**
 * 借方軽減税率区分の値取得
 */
function getKeigenZeiritsuKbn(isKari){
	var taishaku = isKari ? "kari" : "kashi";
	var keigenZeiritsuKbn = $("select[name="+ taishaku +"Zeiritsu]").find("option:selected").attr("data-keigenZeiritsuKbn");
	$("input[name="+ taishaku +"KeigenZeiritsuKbn]").val(keigenZeiritsuKbn);
}

/**
 * 金額を再計算するFunction
 */
function saiKeisan() {

	//ブランクと0ややこしいので、入力可能なブランクは0リセット
	$("input.autoNumericWithCalcBox").each(function(){
		if ((typeof $(this).attr("disabled") == "undefined") && "" == $(this).val()) {
			$(this).putMoney(0);
		}
	});
}

/**
 * 課税区分および処理グループにより税率の標示有無切替
 * 課税区分 税込・税抜、仮受(仮払)消費税の場合税率を表示（それ以外なら非表示）
 * ダイアログ表示時と、科目選択時に呼ばれる
 */
function displayZeiritsu(isKari) {
	let taishaku = isKari ? "kari" : "kashi";
	let kazeiKbnGroup = $("select[name="+ taishaku +"KazeiKbn] option:selected").attr("data-kazeiKbnGroup");
	let shoriGroup = $("input[name="+ taishaku +"ShoriGroup]").val();
	getKeigenZeiritsuKbn(isKari);

	let hyoujiFlg = (['1', '2'].includes(kazeiKbnGroup) || ['21', '22'].includes(shoriGroup));
	let displayStyle = false == hyoujiFlg ? 'hide' : 'show';
	$("label[for="+ taishaku +"Zeiritsu]")[displayStyle]();
	$("select[name="+ taishaku +"Zeiritsu]")[displayStyle]();
	if(!hyoujiFlg){
		$("select[name="+ taishaku +"Zeiritsu]").val("0");
	}
	//科目が未選択に戻された場合はdisabledで表示
	if($("input[name="+ taishaku +"KamokuEdabanCd]").val() == ''){
		$("label[for="+ taishaku +"Zeiritsu]")[displayStyle]();
	}
}

/**
* 分離区分・仕入区分・税額計算方式の表示制御
* @param isKari true:借 false:貸
*/
function koumokuSeigyo(isKari){
	let taishaku = isKari ? "kari" : "kashi";
	var uriagezeiKeisan = $("#workflowForm").find("input[name=uriagezeigakuKeisan]").val();
	var shouhizeiKbn = $("#workflowForm").find("input[name=shouhizeikbn]").val();
 	var shiireZeiAnbun = $("#workflowForm").find("input[name=shiireZeiAnbun]").val();
	var taishakuList = [ "kari", "kashi" ];
	var shoriGroup = $("input[name=" + taishaku +"ShoriGroup]").val();
	var kazeiKbn = $("select[name=" + taishaku +"KazeiKbn] option:selected").val();
	
	displayZeiritsu(isKari);
	//税額計算方式の制御
	if(uriagezeiKeisan == "2"){
		if((['4', '9'].includes(shoriGroup)) && (['001', '002', '012', '014'].includes(kazeiKbn))){
			$("select[name=" + taishaku +"ZeigakuHoushiki]").prop("disabled", false);
			if($("select[name=" + taishaku +"ZeigakuHoushiki]").val() == "9"){
				$("select[name=" + taishaku +"ZeigakuHoushiki]").val("0");
			}
		}else{
			$("select[name=" + taishaku +"ZeigakuHoushiki]").val("9");
			$("select[name=" + taishaku +"ZeigakuHoushiki]").prop("disabled", true);
		}
	}else{
		$("label[for=" + taishaku +"ZeigakuHoushiki]").hide();
		$("select[name=" + taishaku +"ZeigakuHoushiki]").hide();
		if(['4', '9'].includes(shoriGroup) && ['001', '002', '012', '014'].includes(kazeiKbn)){
			$("select[name=" + taishaku +"ZeigakuHoushiki]").val(uriagezeiKeisan);			
		}else{
			$("select[name=" + taishaku +"ZeigakuHoushiki]").val("9");
		}
	}

	//新規入力状態のときは取引を選択するまで入力不可
	//取引コードを消してフォーカスアウトしたらリセット
	if($("input[name=" +taishaku +"KamokuCd]").val() == ''){
		$("select[name=" +taishaku +"BunriKbn]").val("9");
		$("select[name=" +taishaku +"BunriKbn]").prop("disabled", true);
		$("select[name=" +taishaku +"ShiireKbn]").val("0");
		$("select[name=" +taishaku +"ShiireKbn]").prop("disabled", true);
	}else{
		//消費税区分が0：税込方式なら問答無用でdisable
		//1：税抜方式　2：一括税抜方式のとき課税区分に従う
		//　課税区分について　2税抜・13課抜仕入・14課抜売上　は税抜扱い（SIAS消費税設定の情報より）
		//　1税込・11課込仕入・12課込売上　のみが自動分離を選択できる
		//　任意枝番で分離区分の登録がある枝番を選択した場合は枝番に登録されていた分離区分が優先される
		
		//仕入区分
		//仕入税額按分フラグが比例配分の時は非表示、個別対応方式の場合表示してenable
		//任意部門で仕入区分の登録がある部門を選択した場合は部門に登録されていた仕入区分が優先される
		
		//課税区分の空白グレーアウトは、取引登録時に対応済みそしてここでは何もしない
		
		setShouhizeiControls($("input[name=denpyouId]").val(), $("input[name=" + taishaku +"ShoriGroup]").val(), $("select[name=" + taishaku +"KazeiKbn]"), $("select[name=" + taishaku +"Zeiritsu]"), $("select[name=" + taishaku +"BunriKbn]"), $("select[name=" + taishaku +"ShiireKbn]"));
	}

	if(shiireZeiAnbun == "0"){
		$("select[name=" +taishaku +"ShiireKbn]").val("0");
		$(".shiire").hide();
	}
	if($("#workflowForm").find("input[name=invoiceStartflg]").val() == "1"){
		jigyoushaKbnSeigyo(isKari);	
	}
}

/**
* 事業者区分の入力制御（入力可能な場合、必須となる）
* @param isKari true:借 false:貸
*/
function jigyoushaKbnSeigyo(isKari){
	let taishaku = isKari ? "kari" : "kashi";
	var shoriGroup = $("input[name=" + taishaku +"ShoriGroup]").val();
	var kazeiKbn = $("select[name=" + taishaku +"KazeiKbn] option:selected").val();
	if($("input[name=" + taishaku +"KamokuCd]").val() == ''){
		//取引をクリアした場合
		$("input[name=" + taishaku +"JigyoushaKbn]").prop("disabled", true);
		$("input[name=" + taishaku +"JigyoushaKbn]:eq(0)").prop("checked", true);
	//処理グループが21(仮払消費税)の場合、課税区分を考慮せず変更可能とする
	}else if(((shoriGroup == "2"|| shoriGroup == "5"|| shoriGroup == "6"|| shoriGroup == "7"|| shoriGroup == "8"|| shoriGroup == "10")
			&& (kazeiKbn == "001" || kazeiKbn == "002" || kazeiKbn == "011" || kazeiKbn == "013")) || shoriGroup == "21"){ 
		$("input[name=" + taishaku +"JigyoushaKbn]").prop("disabled", false);
		var notCheck = $('[name="' + taishaku +'JigyoushaKbn"]:checked').length;
		if(notCheck < 1){
			//変更可能時、取引の取引先が任意だったなどの理由で、事業者区分ラジオボタンがどちらもチェックされていなかった場合
			$("input[name=" + taishaku +"JigyoushaKbn]:eq(0)").prop("checked", true);
		}
	}else{
		$("input[name=" + taishaku +"JigyoushaKbn]").prop("disabled", true); //選択を「0:通常課税」にして、グレーアウトにする
		$("input[name=" + taishaku +"JigyoushaKbn]:eq(0)").prop("checked",true);
	}
}

//初期表示処理
$(document).ready(function(){
	
	koumokuSeigyo(true);
	koumokuSeigyo(false);

<c:if test= '${enableInput}'>;

	//（借方）勘定科目選択ボタン押下時Function
	$("#kariKamokuSenakuButton").click(function(){
		dialogRetKamokuCd = $("input[name=kariKamokuCd]");
		dialogRetKamokuName = $("input[name=kariKamokuName]");
		dialogRetKazeiKbn = $("select[name=kariKazeiKbn]");
		dialogRetShoriGroup	= $("input[name=kariShoriGroup]");
		dialogRetBunriKbn = $("select[name=kariBunriKbn]");
		dialogRetKariShiireKbn = $("select[name=kariShiireKbn]");
		dialogCallbackKanjyouKamokuSentaku = function(){
			// 科目選択時には科目枝番をクリア
			$("input[name=kariKamokuEdabanCd]").val("");
			$("input[name=kariKamokuEdabanName]").val("");
			koumokuSeigyo(true);
			$("input[name=kariFutanBumonCd]").blur();
		};
		commonKamokuSentaku();
	});
	//（借方）勘定科目コードロストフォーカス時Function
	$("input[name=kariKamokuCd]").blur(function(){
		var title = $(this).parent().parent().find(".row-title").text();
		dialogRetKamokuCd = $("input[name=kariKamokuCd]");
		dialogRetKamokuName = $("input[name=kariKamokuName]");
		dialogRetShoriGroup = $("input[name=kariShoriGroup]");
		dialogRetKazeiKbn = $("select[name=kariKazeiKbn]");
		dialogRetBunriKbn = $("select[name=kariBunriKbn]");
		dialogRetKariShiireKbn = $("select[name=kariShiireKbn]");
		dialogCallbackKanjyouKamokuSentaku = function(){
			// 科目選択時には科目枝番をクリア
			$("input[name=kariKamokuEdabanCd]").val("");
			$("input[name=kariKamokuEdabanName]").val("");
			koumokuSeigyo(true);
			$("input[name=kariFutanBumonCd]").blur();
		};
		commonKamokuCdLostFocus(dialogRetKamokuCd, dialogRetKamokuName, title, null, null, dialogRetKazeiKbn, dialogRetBunriKbn, dialogRetKariShiireKbn, dialogRetShoriGroup);
	});
	//（貸方）勘定科目選択ボタン押下時Function
	$("#kashiKamokuSenakuButton").click(function(){
		dialogRetKamokuCd = $("input[name=kashiKamokuCd]");
		dialogRetKamokuName = $("input[name=kashiKamokuName]");
		dialogRetShoriGroup	= $("input[name=kashiShoriGroup]");
		dialogRetKazeiKbn = $("select[name=kashiKazeiKbn]");
		dialogRetBunriKbn = $("select[name=kashiBunriKbn]");
		dialogRetKariShiireKbn = $("select[name=kashiShiireKbn]");
		dialogCallbackKanjyouKamokuSentaku = function(){
			// 科目選択時には科目枝番をクリア
			$("input[name=kashiKamokuEdabanCd]").val("");
			$("input[name=kashiKamokuEdabanName]").val("");
			koumokuSeigyo(false);
			$("input[name=kashiFutanBumonCd]").blur();
		};
		commonKamokuSentaku();
	});
	//（貸方）勘定科目コードロストフォーカス時Function
	$("input[name=kashiKamokuCd]").blur(function(){
		var title = $(this).parent().parent().find(".row-title").text();
		dialogRetKamokuCd = $("input[name=kashiKamokuCd]");
		dialogRetKamokuName = $("input[name=kashiKamokuName]");
		dialogRetShoriGroup	= $("input[name=kashiShoriGroup]");
		dialogRetKazeiKbn = $("select[name=kashiKazeiKbn]");
		dialogRetBunriKbn = $("select[name=kashiBunriKbn]");
		dialogRetKariShiireKbn = $("select[name=kashiShiireKbn]");
		dialogCallbackKanjyouKamokuSentaku = function(){
			// 科目選択時には科目枝番をクリア
			$("input[name=kashiKamokuEdabanCd]").val("");
			$("input[name=kashiKamokuEdabanName]").val("");
			koumokuSeigyo(false);
			$("input[name=kashiFutanBumonCd]").blur();
		};
		commonKamokuCdLostFocus(dialogRetKamokuCd, dialogRetKamokuName, title, null, null, dialogRetKazeiKbn, dialogRetBunriKbn, dialogRetKariShiireKbn, dialogRetShoriGroup);
	});
	//（借方）勘定科目枝番選択ボタン押下時Function
	$("#kariKamokuEdabanSenakuButton").click(function(){
		if($("input[name=kariKamokuName]").val() == "") {
			alert("(借方)${su:htmlEscape(ks.kamoku.name)}を入力してください。");
		}else {
			dialogRetKamokuEdabanCd = $("input[name=kariKamokuEdabanCd]");
			dialogRetKamokuEdabanName = $("input[name=kariKamokuEdabanName]");
			dialogRetKazeiKbn = $("select[name=kariKazeiKbn]");
			dialogRetBunriKbn = $("select[name=kariBunriKbn]");
			commonKamokuEdabanSentaku($("input[name=kariKamokuCd]").val());
			dialogCallbackKanjyouKamokuEdabanSentaku = function(){
				koumokuSeigyo(true);
				$("input[name=kariFutanBumonCd]").blur();
			};
		}
	});
	//（借方）勘定科目枝番クリアボタン押下時Function
	$("#kariKamokuEdabanClearButton").click(function(){
		$("input[name=kariKamokuEdabanCd]").val("");
		$("input[name=kariKamokuEdabanName]").val("");
	});
	//（借方）勘定科目枝番コードロストフォーカス時Function
	$("input[name=kariKamokuEdabanCd]").blur(function(){
		var kamokuCd = 	$("input[name=kariKamokuCd]").val();
		dialogRetKamokuEdabanCd = $("input[name=kariKamokuEdabanCd]");
		dialogRetKamokuEdabanName = $("input[name=kariKamokuEdabanName]");
		dialogRetKazeiKbn = $("select[name=kariKazeiKbn]");
		dialogRetBunriKbn = $("select[name=kariBunriKbn]");
		if(kamokuCd == "" && dialogRetKamokuEdabanCd.val() != "") {
			alert("(借方)${su:htmlEscape(ks.kamoku.name)}を入力してください。");
		}else {
			dialogCallbackKanjyouKamokuEdabanSentaku = function(){
				koumokuSeigyo(true);
				$("input[name=kariFutanBumonCd]").blur();
			};
			var title = $(this).parent().parent().find(".row-title").text();
			commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamokuCd, title,dialogRetKazeiKbn,dialogRetBunriKbn);
		}
	});
	//（貸方）勘定科目枝番選択ボタン押下時Function
	$("#kashiKamokuEdabanSenakuButton").click(function(){
		if($("input[name=kashiKamokuName]").val() == "") {
			alert("(貸方)${su:htmlEscape(ks.kamoku.name)}を入力してください。");
		}else {
			dialogRetKamokuEdabanCd = $("input[name=kashiKamokuEdabanCd]");
			dialogRetKamokuEdabanName = $("input[name=kashiKamokuEdabanName]");
			dialogRetKazeiKbn = $("select[name=kashiKazeiKbn]");
			dialogRetBunriKbn = $("select[name=kashiBunriKbn]");
			dialogCallbackKanjyouKamokuEdabanSentaku = function(){
				koumokuSeigyo(false);
				$("input[name=kashiFutanBumonCd]").blur();
			};
			commonKamokuEdabanSentaku($("input[name=kashiKamokuCd]").val());
		}
	});
	//（貸方）勘定科目枝番クリアボタン押下時Function
	$("#kashiKamokuEdabanClearButton").click(function(){
		$("input[name=kashiKamokuEdabanCd]").val("");
		$("input[name=kashiKamokuEdabanName]").val("");
	});
	//（貸方）勘定科目枝番コードロストフォーカス時Function
	$("input[name=kashiKamokuEdabanCd]").blur(function(){
		var kamokuCd = 	$("input[name=kashiKamokuCd]").val();
		dialogRetKamokuEdabanCd				= $("input[name=kashiKamokuEdabanCd]");
		dialogRetKamokuEdabanName			= $("input[name=kashiKamokuEdabanName]");
		dialogRetKazeiKbn = $("select[name=kashiKazeiKbn]");
		dialogRetBunriKbn = $("select[name=kashiBunriKbn]");
		if(kamokuCd == "" && dialogRetKamokuEdabanCd.val() != "") {
				alert("(貸方)${su:htmlEscape(ks.kamoku.name)}を入力してください。");
		}else {
			dialogCallbackKanjyouKamokuEdabanSentaku = function(){
				koumokuSeigyo(false);
				$("input[name=kashiFutanBumonCd]").blur();
			};
			var title = $(this).parent().parent().find(".row-title").text();
			commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamokuCd, title,dialogRetKazeiKbn,dialogRetBunriKbn);
		}
	});
	//（借方）負担部門選択ボタン押下時Function
	$("#kariFutanBumonSentakuButton").click(function(){
		dialogRetFutanBumonCd = $("input[name=kariFutanBumonCd]");
		dialogRetFutanBumonName = $("input[name=kariFutanBumonName]");
		dialogRetKariShiireKbn = $("select[name=kariShiireKbn]");
		commonFutanBumonSentaku("1",$("input[name=kariKamokuCd]").val(),$("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val());
	});
	//（貸方）負担部門選択ボタン押下時Function
	$("#kashiFutanBumonSentakuButton").click(function(){
		dialogRetFutanBumonCd = $("input[name=kashiFutanBumonCd]");
		dialogRetFutanBumonName = $("input[name=kashiFutanBumonName]");
		dialogRetKariShiireKbn = $("select[name=kashiShiireKbn]");
		commonFutanBumonSentaku("1",$("input[name=kashiKamokuCd]").val(),$("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val());
	});
	//（借方）負担部門クリアボタン押下時Function
	$("#kariFutanBumonClearButton").click(function(){
		$("input[name=kariFutanBumonCd]").val("");
		$("input[name=kariFutanBumonName]").val("");
	});
	//（貸方）負担部門クリアボタン押下時Function
	$("#kashiFutanBumonClearButton").click(function(){
		$("input[name=kashiFutanBumonCd]").val("");
		$("input[name=kashiFutanBumonName]").val("");
	});
	//（借方）負担部門コードロストフォーカス時Function
	$("input[name=kariFutanBumonCd]").blur(function(){
		var title = $(this).parent().parent().find(".row-title").text();
		dialogRetFutanBumonCd				= $("input[name=kariFutanBumonCd]");
		dialogRetFutanBumonName				= $("input[name=kariFutanBumonName]");
		dialogRetKariShiireKbn 				= $("select[name=kariShiireKbn]");
		commonFutanBumonCdLostFocus("1",dialogRetFutanBumonCd, dialogRetFutanBumonName, title, $("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val(), $("[name=kariKamokuCd]").val(), dialogRetKariShiireKbn);
	});
	//（貸方）負担部門コードロストフォーカス時Function
	$("input[name=kashiFutanBumonCd]").blur(function(){
		var title = $(this).parent().parent().find(".row-title").text();
		dialogRetFutanBumonCd				= $("input[name=kashiFutanBumonCd]");
		dialogRetFutanBumonName				= $("input[name=kashiFutanBumonName]");
		dialogRetKariShiireKbn 			= $("select[name=kashiShiireKbn]");
		commonFutanBumonCdLostFocus("1",dialogRetFutanBumonCd, dialogRetFutanBumonName, title, $("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val(), $("[name=kashiKamokuCd]").val(), dialogRetKariShiireKbn);
	});
	//（借方）取引先選択ボタン押下時Function
	$("#kariTorihikisakiSentakuButton").click(function(){
		dialogRetTorihikisakiCd = $("input[name=kariTorihikisakiCd]");
		dialogRetTorihikisakiName = $("input[name=kariTorihikisakiName]");
		dialogRetJigyoushaKbn = $("[name=kariJigyoushaKbn]");
		commonTorihikisakiSentaku();
		dialogCallbackTorihikisakiSentaku = function(){
			jigyoushaKbnSeigyo(true);
		};
	});
	//（借方）取引先クリアボタン押下時Function
	$("#kariTorihikisakiClearButton").click(function(){
		$("input[name=kariTorihikisakiCd]").val("");
		$("input[name=kariTorihikisakiName]").val("");
	});
	//（借方）取引先コードロストフォーカス時、取引先名称表示
	$("input[name=kariTorihikisakiCd]").blur(function(){
	 	var title = $(this).parent().parent().find(".row-title").text();
		dialogRetTorihikisakiCd = $("input[name=kariTorihikisakiCd]");
		dialogRetTorihikisakiName = $("input[name=kariTorihikisakiName]");
		dialogRetJigyoushaKbn = $("[name=kariJigyoushaKbn]");
		commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd, dialogRetTorihikisakiName, title, null, null,dialogRetJigyoushaKbn);
		dialogCallbackTorihikisakiSentaku = function(){
			jigyoushaKbnSeigyo(true);
		};
	});
	//（貸方）取引先選択ボタン押下時Function
	$("#kashiTorihikisakiSentakuButton").click(function(){
		dialogRetTorihikisakiCd = $("input[name=kashiTorihikisakiCd]");
		dialogRetTorihikisakiName = $("input[name=kashiTorihikisakiName]");
		dialogRetJigyoushaKbn = $("[name=kashiJigyoushaKbn]");
		commonTorihikisakiSentaku();
		dialogCallbackTorihikisakiSentaku = function(){
			jigyoushaKbnSeigyo(false);
		};
	});
	//（貸方）取引先クリアボタン押下時Function
	$("#kashiTorihikisakiClearButton").click(function(){
		$("input[name=kashiTorihikisakiCd]").val("");
		$("input[name=kashiTorihikisakiName]").val("");
	});
	//（貸方）取引先コードロストフォーカス時、取引先名称表示
	$("input[name=kashiTorihikisakiCd]").blur(function(){
	 	var title = $(this).parent().parent().find(".row-title").text();
		dialogRetTorihikisakiCd = $("input[name=kashiTorihikisakiCd]");
		dialogRetTorihikisakiName = $("input[name=kashiTorihikisakiName]");
		dialogRetJigyoushaKbn = $("[name=kashiJigyoushaKbn]");
		commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd, dialogRetTorihikisakiName, title, null, null,dialogRetJigyoushaKbn);
		dialogCallbackTorihikisakiSentaku = function(){
			jigyoushaKbnSeigyo(false);
		};
	});
	//（借方）プロジェクト選択ボタン押下時Function
	$("#kariProjectSentakuButton").click(function(){
		dialogRetProjectCd = $("input[name=kariProjectCd]");
		dialogRetProjectName = $("input[name=kariProjectName]");
		commonProjectSentaku();
	});
	//（借方）プロジェクトクリアボタン押下時Function
	$("#kariProjectClearButton").click(function(){
		$("input[name=kariProjectCd]").val("");
		$("input[name=kariProjectName]").val("");
	});
	//（借方）プロジェクトコードロストフォーカス時Function
	$("input[name=kariProjectCd]").blur(function(){
		var title = $(this).parent().parent().find(".row-title").text();
		dialogRetProjectCd	= $("input[name=kariProjectCd]");
		dialogRetProjectName = $("input[name=kariProjectName]");
		commonProjectCdLostFocus(dialogRetProjectCd, dialogRetProjectName, title);
	});
	//（貸方）プロジェクト選択ボタン押下時Function
	$("#kashiProjectSentakuButton").click(function(){
		dialogRetProjectCd = $("input[name=kashiProjectCd]");
		dialogRetProjectName = $("input[name=kashiProjectName]");
		commonProjectSentaku();
	});
	//（貸方）プロジェクトクリアボタン押下時Function
	$("#kashiProjectClearButton").click(function(){
		$("input[name=kashiProjectCd]").val("");
		$("input[name=kashiProjectName]").val("");
	});
	//（貸方）プロジェクトコードロストフォーカス時Function
	$("input[name=kashiProjectCd]").blur(function(){
		var title = $(this).parent().parent().find(".row-title").text();
		dialogRetProjectCd	= $("input[name=kashiProjectCd]");
		dialogRetProjectName = $("input[name=kashiProjectName]");
		commonProjectCdLostFocus(dialogRetProjectCd, dialogRetProjectName, title);
	});
	
	//（借方）セグメント選択ボタン押下時Function
	$("#kariSegmentSentakuButton").click(function(){
		dialogRetSegmentCd = $("input[name=kariSegmentCd]");
		dialogRetSegmentName = $("input[name=kariSegmentName]");
		commonSegmentSentaku();
	});
	//（借方）セグメントクリアボタン押下時Function
	$("#kariSegmentClearButton").click(function(){
		$("input[name=kariSegmentCd]").val("");
		$("input[name=kariSegmentName]").val("");
	});
	//（借方）セグメントコードロストフォーカス時Function
	$("input[name=kariSegmentCd]").blur(function(){
		var title = $(this).parent().parent().find(".row-title").text();
		dialogRetSegmentCd	= $("input[name=kariSegmentCd]");
		dialogRetSegmentName = $("input[name=kariSegmentName]");
		commonSegmentCdLostFocus(dialogRetSegmentCd, dialogRetSegmentName, title);
	});
	//（貸方）セグメント選択ボタン押下時Function
	$("#kashiSegmentSentakuButton").click(function(){
		dialogRetSegmentCd = $("input[name=kashiSegmentCd]");
		dialogRetSegmentName = $("input[name=kashiSegmentName]");
		commonSegmentSentaku();
	});
	//（貸方）セグメントクリアボタン押下時Function
	$("#kashiSegmentClearButton").click(function(){
		$("input[name=kashiSegmentCd]").val("");
		$("input[name=kashiSegmentName]").val("");
	});
	//（貸方）セグメントコードロストフォーカス時Function
	$("input[name=kashiSegmentCd]").blur(function(){
		var title = $(this).parent().parent().find(".row-title").text();
		dialogRetSegmentCd	= $("input[name=kashiSegmentCd]");
		dialogRetSegmentName = $("input[name=kashiSegmentName]");
		commonSegmentCdLostFocus(dialogRetSegmentCd, dialogRetSegmentName, title);
	});
	
	//（借方）ユニバーサルフィールド１選択ボタン押下時Function
	$("#kariUf1SentakuButton").click(function(){
		dialogRetUniversalFieldCd = $("input[name=kariUf1Cd]");
		dialogRetUniversalFieldName = $("input[name=kariUf1Name]");
		commonUniversalSentaku($("input[name=kariKamokuCd]").val() ,1);
	});
	//（借方）ユニバーサルフィールド１クリアボタン押下時Function
	$("#kariUf1ClearButton").click(function(){
		$("input[name=kariUf1Cd]").val("");
		$("input[name=kariUf1Name]").val("");
	});
	//（借方）ユニバーサルフィールド１コードロストフォーカス時Function
	$("input[name=kariUf1Cd]").blur(function(){
		var title = $(this).parent().parent().find(".row-title").text();
		var isBtnVisible = $("#kariUf1SentakuButton").length > 0;
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=kariUf1Cd]");
			dialogRetUniversalFieldName		= $("input[name=kariUf1Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 1, title);
		}
	});
	//（貸方）ユニバーサルフィールド１選択ボタン押下時Function
	$("#kashiUf1SentakuButton").click(function(){
		dialogRetUniversalFieldCd = $("input[name=kashiUf1Cd]");
		dialogRetUniversalFieldName = $("input[name=kashiUf1Name]");
		commonUniversalSentaku($("input[name=kashiKamokuCd]").val() ,1);
	});
	//（貸方）ユニバーサルフィールド１クリアボタン押下時Function
	$("#kashiUf1ClearButton").click(function(){
		$("input[name=kashiUf1Cd]").val("");
		$("input[name=kashiUf1Name]").val("");
	});
	//（貸方）ユニバーサルフィールド１コードロストフォーカス時Function
	$("input[name=kashiUf1Cd]").blur(function(){
		var title = $(this).parent().parent().find(".row-title").text();
		var isBtnVisible = $("#kashiUf1SentakuButton").length > 0;
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=kashiUf1Cd]");
			dialogRetUniversalFieldName		= $("input[name=kashiUf1Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 1, title);
		}
	});
	//（借方）ユニバーサルフィールド２選択ボタン押下時Function
	$("#kariUf2SentakuButton").click(function(){
		dialogRetUniversalFieldCd = $("input[name=kariUf2Cd]");
		dialogRetUniversalFieldName = $("input[name=kariUf2Name]");
		commonUniversalSentaku($("input[name=kariKamokuCd]").val() ,2);
	});
	//（借方）ユニバーサルフィールド２クリアボタン押下時Function
	$("#kariUf2ClearButton").click(function(){
		$("input[name=kariUf2Cd]").val("");
		$("input[name=kariUf2Name]").val("");
	});
	//（借方）ユニバーサルフィールド２コードロストフォーカス時Function
	$("input[name=kariUf2Cd]").blur(function(){
		var title = $(this).parent().parent().find(".row-title").text();
		var isBtnVisible = $("#kariUf2SentakuButton").length > 0;
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=kariUf2Cd]");
			dialogRetUniversalFieldName		= $("input[name=kariUf2Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 2, title);
		}
	});
	//（貸方）ユニバーサルフィールド２選択ボタン押下時Function
	$("#kashiUf2SentakuButton").click(function(){
		dialogRetUniversalFieldCd = $("input[name=kashiUf2Cd]");
		dialogRetUniversalFieldName = $("input[name=kashiUf2Name]");
		commonUniversalSentaku($("input[name=kashiKamokuCd]").val() ,2);
	});
	//（貸方）ユニバーサルフィールド２クリアボタン押下時Function
	$("#kashiUf2ClearButton").click(function(){
		$("input[name=kashiUf2Cd]").val("");
		$("input[name=kashiUf2Name]").val("");
	});
	//（貸方）ユニバーサルフィールド２コードロストフォーカス時Function
	$("input[name=kashiUf2Cd]").blur(function(){
		var title = $(this).parent().parent().find(".row-title").text();
		var isBtnVisible = $("#kashiUf2SentakuButton").length > 0;
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=kashiUf2Cd]");
			dialogRetUniversalFieldName		= $("input[name=kashiUf2Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 2, title);
		}
	});
	//（借方）ユニバーサルフィールド３選択ボタン押下時Function
	$("#kariUf3SentakuButton").click(function(){
		dialogRetUniversalFieldCd = $("input[name=kariUf3Cd]");
		dialogRetUniversalFieldName = $("input[name=kariUf3Name]");
		commonUniversalSentaku($("input[name=kariKamokuCd]").val() ,3);
	});
	//（借方）ユニバーサルフィールド３クリアボタン押下時Function
	$("#kariUf3ClearButton").click(function(){
	 	$("input[name=kariUf3Cd]").val("");
	 	$("input[name=kariUf3Name]").val("");
	});
	//（借方）ユニバーサルフィールド３コードロストフォーカス時Function
	$("input[name=kariUf3Cd]").blur(function(){
		var title = $(this).parent().parent().find(".row-title").text();
		var isBtnVisible = $("#kariUf3SentakuButton").length > 0;
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=kariUf3Cd]");
			dialogRetUniversalFieldName		= $("input[name=kariUf3Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 3, title);
		}
	});
	//（貸方）ユニバーサルフィールド３選択ボタン押下時Function
	$("#kashiUf3SentakuButton").click(function(){
		dialogRetUniversalFieldCd = $("input[name=kashiUf3Cd]");
		dialogRetUniversalFieldName = $("input[name=kashiUf3Name]");
		commonUniversalSentaku($("input[name=kashiKamokuCd]").val() ,3);
	});
	//（貸方）ユニバーサルフィールド３クリアボタン押下時Function
	$("#kashiUf3ClearButton").click(function(){
		$("input[name=kashiUf3Cd]").val("");
		$("input[name=kashiUf3Name]").val("");
	});
	//（貸方）ユニバーサルフィールド３コードロストフォーカス時Function
	$("input[name=kashiUf3Cd]").blur(function(){
		var title = $(this).parent().parent().find(".row-title").text();
		var isBtnVisible = $("#kashiUf3SentakuButton").length > 0;
		if (isBtnVisible) {
			dialogRetUniversalFieldCd		= $("input[name=kashiUf3Cd]");
			dialogRetUniversalFieldName		= $("input[name=kashiUf3Name]");
			commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 3, title);
		}
	});
	//（借方）ユニバーサルフィールド４選択ボタン押下時Function
	 $("#kariUf4SentakuButton").click(function(){
	  dialogRetUniversalFieldCd = $("input[name=kariUf4Cd]");
	  dialogRetUniversalFieldName = $("input[name=kariUf4Name]");
	  commonUniversalSentaku($("input[name=kariKamokuCd]").val() ,4);
	 });
	 //（借方）ユニバーサルフィールド４クリアボタン押下時Function
	 $("#kariUf4ClearButton").click(function(){
	   $("input[name=kariUf4Cd]").val("");
	   $("input[name=kariUf4Name]").val("");
	 });
	//（借方）ユニバーサルフィールド４コードロストフォーカス時Function
	 $("input[name=kariUf4Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".row-title").text();
	 	var isBtnVisible = $("#kariUf4SentakuButton").length > 0;
	 	if (isBtnVisible) {
	 		dialogRetUniversalFieldCd  = $("input[name=kariUf4Cd]");
	 		dialogRetUniversalFieldName  = $("input[name=kariUf4Name]");
	 		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 4, title);
	 	}
	 });
	 //（貸方）ユニバーサルフィールド４選択ボタン押下時Function
	 $("#kashiUf4SentakuButton").click(function(){
	 	dialogRetUniversalFieldCd = $("input[name=kashiUf4Cd]");
	 	dialogRetUniversalFieldName = $("input[name=kashiUf4Name]");
	 	commonUniversalSentaku($("input[name=kashiKamokuCd]").val() ,4);
	 });
	 //（貸方）ユニバーサルフィールド４クリアボタン押下時Function
	 $("#kashiUf4ClearButton").click(function(){
	 	$("input[name=kashiUf4Cd]").val("");
	 	$("input[name=kashiUf4Name]").val("");
	 });
	 //（貸方）ユニバーサルフィールド４コードロストフォーカス時Function
	 $("input[name=kashiUf4Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".row-title").text();
	 	var isBtnVisible = $("#kashiUf4SentakuButton").length > 0;
	 	if (isBtnVisible) {
	 		dialogRetUniversalFieldCd  = $("input[name=kashiUf4Cd]");
	 		dialogRetUniversalFieldName  = $("input[name=kashiUf4Name]");
	 		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 4, title);
	 	}
	 });
	 //（借方）ユニバーサルフィールド５選択ボタン押下時Function
	 $("#kariUf5SentakuButton").click(function(){
	 	dialogRetUniversalFieldCd = $("input[name=kariUf5Cd]");
	 	dialogRetUniversalFieldName = $("input[name=kariUf5Name]");
	 	commonUniversalSentaku($("input[name=kariKamokuCd]").val() ,5);
	 });
	 //（借方）ユニバーサルフィールド５クリアボタン押下時Function
	 $("#kariUf5ClearButton").click(function(){
	 	$("input[name=kariUf5Cd]").val("");
	 	$("input[name=kariUf5Name]").val("");
	 });
	 //（借方）ユニバーサルフィールド５コードロストフォーカス時Function
	 $("input[name=kariUf5Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".row-title").text();
	 	var isBtnVisible = $("#kariUf5SentakuButton").length > 0;
	 	if (isBtnVisible) {
	 		dialogRetUniversalFieldCd  = $("input[name=kariUf5Cd]");
	 		dialogRetUniversalFieldName  = $("input[name=kariUf5Name]");
	 		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 5, title);
	 	}
	 });
	 //（貸方）ユニバーサルフィールド５選択ボタン押下時Function
	 $("#kashiUf5SentakuButton").click(function(){
	 	dialogRetUniversalFieldCd = $("input[name=kashiUf5Cd]");
	 	dialogRetUniversalFieldName = $("input[name=kashiUf5Name]");
	 	commonUniversalSentaku($("input[name=kashiKamokuCd]").val() ,5);
	 });
	 //（貸方）ユニバーサルフィールド５クリアボタン押下時Function
	 $("#kashiUf5ClearButton").click(function(){
	 	$("input[name=kashiUf5Cd]").val("");
	 	$("input[name=kashiUf5Name]").val("");
	 });
	 //（貸方）ユニバーサルフィールド５コードロストフォーカス時Function
	 $("input[name=kashiUf5Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".row-title").text();
	 	var isBtnVisible = $("#kashiUf5SentakuButton").length > 0;
	 	if (isBtnVisible) {
	 		dialogRetUniversalFieldCd  = $("input[name=kashiUf5Cd]");
	 		dialogRetUniversalFieldName  = $("input[name=kashiUf5Name]");
	 		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 5, title);
	 	}
	 });
	 //（借方）ユニバーサルフィールド６選択ボタン押下時Function
	 $("#kariUf6SentakuButton").click(function(){
	 	dialogRetUniversalFieldCd = $("input[name=kariUf6Cd]");
	 	dialogRetUniversalFieldName = $("input[name=kariUf6Name]");
	 	commonUniversalSentaku($("input[name=kariKamokuCd]").val() ,6);
	 });
	 //（借方）ユニバーサルフィールド６クリアボタン押下時Function
	 $("#kariUf6ClearButton").click(function(){
	 	$("input[name=kariUf6Cd]").val("");
	 	$("input[name=kariUf6Name]").val("");
	 });
	 //（借方）ユニバーサルフィールド６コードロストフォーカス時Function
	 $("input[name=kariUf6Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".row-title").text();
	 	var isBtnVisible = $("#kariUf6SentakuButton").length > 0;
	 	if (isBtnVisible) {
	 		dialogRetUniversalFieldCd  = $("input[name=kariUf6Cd]");
	 		dialogRetUniversalFieldName  = $("input[name=kariUf6Name]");
	 		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 6, title);
	 	}
	 });
	 //（貸方）ユニバーサルフィールド６選択ボタン押下時Function
	 $("#kashiUf6SentakuButton").click(function(){
	 	dialogRetUniversalFieldCd = $("input[name=kashiUf6Cd]");
	 	dialogRetUniversalFieldName = $("input[name=kashiUf6Name]");
	 	commonUniversalSentaku($("input[name=kashiKamokuCd]").val() ,6);
	 });
	 //（貸方）ユニバーサルフィールド６クリアボタン押下時Function
	 $("#kashiUf6ClearButton").click(function(){
	 	$("input[name=kashiUf6Cd]").val("");
	 	$("input[name=kashiUf6Name]").val("");
	 });
	 //（貸方）ユニバーサルフィールド６コードロストフォーカス時Function
	 $("input[name=kashiUf6Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".row-title").text();
	 	var isBtnVisible = $("#kashiUf6SentakuButton").length > 0;
	 	if (isBtnVisible) {
	 		dialogRetUniversalFieldCd  = $("input[name=kashiUf6Cd]");
	 		dialogRetUniversalFieldName  = $("input[name=kashiUf6Name]");
	 		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 6, title);
	 	}
	 });
	 //（借方）ユニバーサルフィールド７選択ボタン押下時Function
	 $("#kariUf7SentakuButton").click(function(){
	 	dialogRetUniversalFieldCd = $("input[name=kariUf7Cd]");
	 	dialogRetUniversalFieldName = $("input[name=kariUf7Name]");
	 	commonUniversalSentaku($("input[name=kariKamokuCd]").val() ,7);
	 });
	 //（借方）ユニバーサルフィールド７クリアボタン押下時Function
	 $("#kariUf7ClearButton").click(function(){
	 	$("input[name=kariUf7Cd]").val("");
	 	$("input[name=kariUf7Name]").val("");
	 });
	 //（借方）ユニバーサルフィールド７コードロストフォーカス時Function
	 $("input[name=kariUf7Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".row-title").text();
	 	var isBtnVisible = $("#kariUf7SentakuButton").length > 0;
	 	if (isBtnVisible) {
	 		dialogRetUniversalFieldCd  = $("input[name=kariUf7Cd]");
	 		dialogRetUniversalFieldName  = $("input[name=kariUf7Name]");
	 		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 7, title);
	 	}
	 });
	 //（貸方）ユニバーサルフィールド７選択ボタン押下時Function
	 $("#kashiUf7SentakuButton").click(function(){
	 	dialogRetUniversalFieldCd = $("input[name=kashiUf7Cd]");
	 	dialogRetUniversalFieldName = $("input[name=kashiUf7Name]");
	 	commonUniversalSentaku($("input[name=kashiKamokuCd]").val() ,7);
	 });
	 //（貸方）ユニバーサルフィールド７クリアボタン押下時Function
	 $("#kashiUf7ClearButton").click(function(){
	 	$("input[name=kashiUf7Cd]").val("");
	 	$("input[name=kashiUf7Name]").val("");
	 });
	 //（貸方）ユニバーサルフィールド７コードロストフォーカス時Function
	 $("input[name=kashiUf7Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".row-title").text();
	 	var isBtnVisible = $("#kashiUf7SentakuButton").length > 0;
	 	if (isBtnVisible) {
	 		dialogRetUniversalFieldCd  = $("input[name=kashiUf7Cd]");
	 		dialogRetUniversalFieldName  = $("input[name=kashiUf7Name]");
	 		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 7, title);
	 	}
	 });
	 //（借方）ユニバーサルフィールド８選択ボタン押下時Function
	 $("#kariUf8SentakuButton").click(function(){
	 	dialogRetUniversalFieldCd = $("input[name=kariUf8Cd]");
	 	dialogRetUniversalFieldName = $("input[name=kariUf8Name]");
	 	commonUniversalSentaku($("input[name=kariKamokuCd]").val() ,8);
	 });
	 //（借方）ユニバーサルフィールド８クリアボタン押下時Function
	 $("#kariUf8ClearButton").click(function(){
	 	$("input[name=kariUf8Cd]").val("");
	 	$("input[name=kariUf8Name]").val("");
	 });
	 //（借方）ユニバーサルフィールド８コードロストフォーカス時Function
	 $("input[name=kariUf8Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".row-title").text();
	 	var isBtnVisible = $("#kariUf8SentakuButton").length > 0;
	 	if (isBtnVisible) {
	 		dialogRetUniversalFieldCd  = $("input[name=kariUf8Cd]");
	 		dialogRetUniversalFieldName  = $("input[name=kariUf8Name]");
	 		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 8, title);
	 	}
	 });
	 //（貸方）ユニバーサルフィールド８選択ボタン押下時Function
	 $("#kashiUf8SentakuButton").click(function(){
	 	dialogRetUniversalFieldCd = $("input[name=kashiUf8Cd]");
	 	dialogRetUniversalFieldName = $("input[name=kashiUf8Name]");
	 	commonUniversalSentaku($("input[name=kashiKamokuCd]").val() ,8);
	 });
	 //（貸方）ユニバーサルフィールド８クリアボタン押下時Function
	 $("#kashiUf8ClearButton").click(function(){
	 	$("input[name=kashiUf8Cd]").val("");
	 	$("input[name=kashiUf8Name]").val("");
	 });
	 //（貸方）ユニバーサルフィールド８コードロストフォーカス時Function
	 $("input[name=kashiUf8Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".row-title").text();
	 	var isBtnVisible = $("#kashiUf8SentakuButton").length > 0;
	 	if (isBtnVisible) {
	 		dialogRetUniversalFieldCd  = $("input[name=kashiUf8Cd]");
	 		dialogRetUniversalFieldName  = $("input[name=kashiUf8Name]");
	 		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 8, title);
	 	}
	 });
	 //（借方）ユニバーサルフィールド９選択ボタン押下時Function
	 $("#kariUf9SentakuButton").click(function(){
	 	dialogRetUniversalFieldCd = $("input[name=kariUf9Cd]");
	 	dialogRetUniversalFieldName = $("input[name=kariUf9Name]");
	 	commonUniversalSentaku($("input[name=kariKamokuCd]").val() ,9);
	 });
	 //（借方）ユニバーサルフィールド９クリアボタン押下時Function
	 $("#kariUf9ClearButton").click(function(){
	 	$("input[name=kariUf9Cd]").val("");
	 	$("input[name=kariUf9Name]").val("");
	 });
	 //（借方）ユニバーサルフィールド９コードロストフォーカス時Function
	 $("input[name=kariUf9Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".row-title").text();
	 	var isBtnVisible = $("#kariUf9SentakuButton").length > 0;
	 	if (isBtnVisible) {
	 		dialogRetUniversalFieldCd  = $("input[name=kariUf9Cd]");
	 		dialogRetUniversalFieldName  = $("input[name=kariUf9Name]");
	 		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 9, title);
	 	}
	 });
	 //（貸方）ユニバーサルフィールド９選択ボタン押下時Function
	 $("#kashiUf9SentakuButton").click(function(){
	 	dialogRetUniversalFieldCd = $("input[name=kashiUf9Cd]");
	 	dialogRetUniversalFieldName = $("input[name=kashiUf9Name]");
	 	commonUniversalSentaku($("input[name=kashiKamokuCd]").val() ,9);
	 });
	 //（貸方）ユニバーサルフィールド９クリアボタン押下時Function
	 $("#kashiUf9ClearButton").click(function(){
	 	$("input[name=kashiUf9Cd]").val("");
	 	$("input[name=kashiUf9Name]").val("");
	 });
	 //（貸方）ユニバーサルフィールド９コードロストフォーカス時Function
	 $("input[name=kashiUf9Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".row-title").text();
	 	var isBtnVisible = $("#kashiUf9SentakuButton").length > 0;
	 	if (isBtnVisible) {
	 		dialogRetUniversalFieldCd  = $("input[name=kashiUf9Cd]");
	 		dialogRetUniversalFieldName  = $("input[name=kashiUf9Name]");
	 		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 9, title);
	 	}
	 });
	 //（借方）ユニバーサルフィールド１０選択ボタン押下時Function
	 $("#kariUf10SentakuButton").click(function(){
	 	dialogRetUniversalFieldCd = $("input[name=kariUf10Cd]");
	 	dialogRetUniversalFieldName = $("input[name=kariUf10Name]");
	 	commonUniversalSentaku($("input[name=kariKamokuCd]").val() ,10);
	 });
	 //（借方）ユニバーサルフィールド１０クリアボタン押下時Function
	 $("#kariUf10ClearButton").click(function(){
	 	$("input[name=kariUf10Cd]").val("");
	 	$("input[name=kariUf10Name]").val("");
	 });
	 //（借方）ユニバーサルフィールド１０コードロストフォーカス時Function
	 $("input[name=kariUf10Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".row-title").text();
	 	var isBtnVisible = $("#kariUf10SentakuButton").length > 0;
	 	if (isBtnVisible) {
	 		dialogRetUniversalFieldCd  = $("input[name=kariUf10Cd]");
	 		dialogRetUniversalFieldName  = $("input[name=kariUf10Name]");
	 		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 10, title);
	 	}
	 });
	 //（貸方）ユニバーサルフィールド１０選択ボタン押下時Function
	 $("#kashiUf10SentakuButton").click(function(){
	 	dialogRetUniversalFieldCd = $("input[name=kashiUf10Cd]");
	 	dialogRetUniversalFieldName = $("input[name=kashiUf10Name]");
	 	commonUniversalSentaku($("input[name=kashiKamokuCd]").val() ,10);
	 });
	 //（貸方）ユニバーサルフィールド１０クリアボタン押下時Function
	 $("#kashiUf10ClearButton").click(function(){
	 	$("input[name=kashiUf10Cd]").val("");
	 	$("input[name=kashiUf10Name]").val("");
	 });
	 //（貸方）ユニバーサルフィールド１０コードロストフォーカス時Function
	 $("input[name=kashiUf10Cd]").blur(function(){
	 	var title = $(this).parent().parent().find(".row-title").text();
	 	var isBtnVisible = $("#kashiUf10SentakuButton").length > 0;
	 	if (isBtnVisible) {
	 		dialogRetUniversalFieldCd  = $("input[name=kashiUf10Cd]");
	 		dialogRetUniversalFieldName  = $("input[name=kashiUf10Name]");
	 		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 10, title);
	 	}
	 });

	//消費税変更時、金額再計算及び軽減税率区分の値再取得
	$("select[name=kariZeiritsu]").change(function(){
		getKeigenZeiritsuKbn(true);
	});

	$("select[name=kashiZeiritsu]").change(function(){
		getKeigenZeiritsuKbn(false);
	});

	//借方課税区分変更時、金額再計算
	$("select[name=kariKazeiKbn]").change(function(){
		koumokuSeigyo(true);
	});

	//貸方課税区分変更時、金額再計算
	$("select[name=kashiKazeiKbn]").change(function(){
		koumokuSeigyo(false);
	});
	
	//事業者区分変更時再計算
	var taishakuList = [ "kari", "kashi" ];
	for(const taishaku of taishakuList){
		$("input[name="+ taishaku +"JigyoushaKbn]").change(function(){
			getKeigenZeiritsuKbn(taishaku == "kari");
		});
	}

	//摘要文字数制限取得
	$("input[name=tekiyou]").attr('maxlength', $("#workflowForm").find("input[name=tekiyouMaxLength]").val());

	//金額計算
	saiKeisan();
	
	
</c:if>;

<c:if test='${not enableInput}'>
	//起票モードの場合のみ入力や選択可能
	$("#kaikeiContent").find("button").css("display", "none");
	$("#kaikeiContent").find("input,textarea,select").prop("disabled", true);
</c:if>

});

/**
 * イベントボタン押下時のアクションの切り替え
 */
function eventBtn(eventName) {
	var formObject = document.getElementById("workflowForm");
	switch (eventName) {
	case 'entry':
		formObject.action = 'furikae_denpyou_touroku';
		break;
	case 'update':
		formObject.action = 'furikae_denpyou_koushin';
		break;
	case 'shinsei':
		formObject.action = 'furikae_denpyou_shinsei';
		break;
	case 'shounin':
		formObject.action = 'furikae_denpyou_shounin';
		break;
	case 'shouninkaijo':
		formObject.action = 'furikae_denpyou_shouninkaijo';
		break;
	}
	formObject.method = 'post';
	formObject.target = '_self';
	$(formObject).submit();
}

var taishakuList = [ "kari", "kashi" ];
for(const taishaku of taishakuList){
	setShouhizeiControls($("input[name=denpyouId]").val(), $("input[name="+ taishaku +"ShoriGroup]").val(), $("select[name="+ taishaku +"KazeiKbn]"), $("select[name="+ taishaku +"Zeiritsu]"), $("select[name="+ taishaku +"BunriKbn]"), $("select[name="+ taishaku +"ShiireKbn]"));
}
</script>
