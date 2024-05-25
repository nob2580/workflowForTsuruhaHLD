<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<style type='text/css'>
<!--
#meisaiTableDiv td {
	max-width: 200px;
}
-->
</style>

<!-- 支払依頼申請明細 -->
<section id='meisaiField' class='print-unit'>
	<h2>明細</h2>
	<div>
		
		<input type='hidden' id="enableInput"		value='${su:htmlEscape(enableInput)}'>
		<input type='hidden' name='meisaiAddTakeOverEnabled'	value='${su:htmlEscape(meisaiAddTakeOverEnabled)}'>
		<input type='hidden' id="uf1ShiyouFlg" value='${su:htmlEscape(hfUfSeigyo.uf1ShiyouFlg)}'>
		<input type='hidden' id="uf2ShiyouFlg" value='${su:htmlEscape(hfUfSeigyo.uf2ShiyouFlg)}'>
		<input type='hidden' id="uf3ShiyouFlg" value='${su:htmlEscape(hfUfSeigyo.uf3ShiyouFlg)}'>
		<input type='hidden' id="uf4ShiyouFlg" value='${su:htmlEscape(hfUfSeigyo.uf4ShiyouFlg)}'>
		<input type='hidden' id="uf5ShiyouFlg" value='${su:htmlEscape(hfUfSeigyo.uf5ShiyouFlg)}'>
		<input type='hidden' id="uf6ShiyouFlg" value='${su:htmlEscape(hfUfSeigyo.uf6ShiyouFlg)}'>
		<input type='hidden' id="uf7ShiyouFlg" value='${su:htmlEscape(hfUfSeigyo.uf7ShiyouFlg)}'>
		<input type='hidden' id="uf8ShiyouFlg" value='${su:htmlEscape(hfUfSeigyo.uf8ShiyouFlg)}'>
		<input type='hidden' id="uf9ShiyouFlg" value='${su:htmlEscape(hfUfSeigyo.uf9ShiyouFlg)}'>
		<input type='hidden' id="uf10ShiyouFlg" value='${su:htmlEscape(hfUfSeigyo.uf10ShiyouFlg)}'>
		<button type='button' id='meisaiAddButton' class='btn btn-small'>明細追加</button>
	</div>
	<div id='meisaiTableDiv' class='no-more-tables' style='display:none'>
				<table class='table-bordered table-condensed' style='margin-bottom:5px; border-collapse: collapse; word-break: break-all; word-wrap: break-word;'>
					<thead>
						<tr id='meisaiTableHeader1'>
						    <th rowspan='4' id='headth'>No</th>
							<th colspan='2' <c:if test='${not ks1.torihiki.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(ks1.torihiki.name)}</th>
							<th <c:if test='${not ks1.kamoku.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(ks1.kamoku.name)}</th>
							<th <c:if test='${not ks1.kamokuEdaban.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(ks1.kamokuEdaban.name)}</th>
							<th <c:if test='${not ks1.futanBumon.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(ks1.futanBumon.name)}</th>
							<th colspan='3' <c:if test='${not ks1.tekiyou.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(ks1.tekiyou.name)}</th>
							<!-- 以下携帯用 -->
							<th style='display:none'>${su:htmlEscape(ks1.kazeiKbn.name)}</th>
							<th style='display:none'>${su:htmlEscape(ks1.zeiritsu.name)}</th>
							<th style='display:none'>${su:htmlEscape(ks1.bunriKbn.name)}</th>
							<th style='display:none'>${su:htmlEscape(ks1.shiireKbn.name)}</th>
							<th style='display:none'>${su:htmlEscape(ks1.shiharaiKingaku.name)}</th>
							<th style='display:none'>${su:htmlEscape(ks1.zeinukiKingaku.name)}</th>
							<th style='display:none'>${su:htmlEscape(ks1.shouhizeigaku.name)}</th>
						    <th style='display:none'>${su:htmlEscape(ks1.project.name)}</th>
						    <th style='display:none'>${su:htmlEscape(ks1.segment.name)}</th>
						    <th style='display:none'>${su:htmlEscape(hfUfSeigyo.uf1Name)}</th>
						    <th style='display:none'>${su:htmlEscape(hfUfSeigyo.uf2Name)}</th>
						    <th style='display:none'>${su:htmlEscape(hfUfSeigyo.uf3Name)}</th>
						    <th style='display:none'>${su:htmlEscape(hfUfSeigyo.uf4Name)}</th>
						    <th style='display:none'>${su:htmlEscape(hfUfSeigyo.uf5Name)}</th>
						    <th style='display:none'>${su:htmlEscape(hfUfSeigyo.uf6Name)}</th>
						    <th style='display:none'>${su:htmlEscape(hfUfSeigyo.uf7Name)}</th>
						    <th style='display:none'>${su:htmlEscape(hfUfSeigyo.uf8Name)}</th>
						    <th style='display:none'>${su:htmlEscape(hfUfSeigyo.uf9Name)}</th>
						    <th style='display:none'>${su:htmlEscape(hfUfSeigyo.uf10Name)}</th>
						    <th rowspan='4' class='non-print control_col'></th>
						</tr>
						<tr id='meisaiTableHeader2'>
							<th <c:if test='${not ks1.kazeiKbn.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(ks1.kazeiKbn.name)}</th>
							<th <c:if test='${not ks1.zeiritsu.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(ks1.zeiritsu.name)}</th>
							<th <c:if test='${not ks1.bunriKbn.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(ks1.bunriKbn.name)}</th>
							<th <c:if test='${not ks1.shiireKbn.hyoujiFlg or shiireZeiAnbun eq "0"}'>style='display:none'</c:if>>${su:htmlEscape(ks1.shiireKbn.name)}</th>
							<th <c:if test='${not ks1.shiharaiKingaku.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(ks1.shiharaiKingaku.name)}</th>
							<th <c:if test='${"1" eq invoiceDenpyou or !ks1.zeinukiKingaku.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(ks1.zeinukiKingaku.name)}</th>
							<th <c:if test='${"1" eq invoiceDenpyou or !ks1.shouhizeigaku.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(ks1.shouhizeigaku.name)}</th>
						</tr>
						<tr id='meisaiTableHeader3'>
							<th <c:if test='${"0" eq pjShiyouFlg or !ks1.project.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(ks1.project.name)}</th>
							<th <c:if test='${"0" eq segmentShiyouFlg or !ks1.segment.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(ks1.segment.name)}</th>
							<!-- UF1 から UF6 のヘッダーを追加 -->
							<th <c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or !ks1.uf1.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(hfUfSeigyo.uf1Name)}</th>
							<th <c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or !ks1.uf2.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(hfUfSeigyo.uf2Name)}</th>
							<th <c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or !ks1.uf3.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(hfUfSeigyo.uf3Name)}</th>
							<th <c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or !ks1.uf4.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(hfUfSeigyo.uf4Name)}</th>
							<th <c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or !ks1.uf5.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(hfUfSeigyo.uf5Name)}</th>
							<th <c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or !ks1.uf6.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(hfUfSeigyo.uf6Name)}</th>
						</tr>
						<tr id='meisaiTableHeader4'>
							<!-- UF7 から UF10 のヘッダーを追加 -->
							<th <c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or !ks1.uf7.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(hfUfSeigyo.uf7Name)}</th>
							<th <c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or !ks1.uf8.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(hfUfSeigyo.uf8Name)}</th>
							<th <c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or !ks1.uf9.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(hfUfSeigyo.uf9Name)}</th>
							<th <c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or !ks1.uf10.hyoujiFlg}'>style='display:none'</c:if>>${su:htmlEscape(hfUfSeigyo.uf10Name)}</th>
						</tr>
					</thead>
					<tbody id='meisaiList'>
<c:forEach var="i" begin="0" end="${fn:length(shiwakeEdaNo) - 1}" step="1">
						<tr class="meisai">
							<td rowspan='4' align='center' id='headtd'></td>
							<td colspan='2' <c:if test='${not ks1.torihiki.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td <c:if test='${not ks1.kamoku.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td <c:if test='${not ks1.kamokuEdaban.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td <c:if test='${not ks1.futanBumon.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td colspan='3' <c:if test='${not ks1.tekiyou.hyoujiFlg}'>style='display:none'</c:if>></td>
							<!-- 以下携帯用 -->
							<td class="phone_only non-print" <c:if test='${not ks1.kazeiKbn.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${not ks1.zeiritsu.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${not ks1.bunriKbn.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${not ks1.shiireKbn.hyoujiFlg or shiireZeiAnbun eq "0"}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${not ks1.shiharaiKingaku.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${"1" eq invoiceDenpyou or !ks1.zeinukiKingaku.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${"1" eq invoiceDenpyou or !ks1.shouhizeigaku.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${"0" eq pjShiyouFlg or !ks1.project.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${"0" eq segmentShiyouFlg or !ks1.segment.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or !ks1.uf1.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or !ks1.uf2.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or !ks1.uf3.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or !ks1.uf4.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or !ks1.uf5.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or !ks1.uf6.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or !ks1.uf7.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or !ks1.uf8.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or !ks1.uf9.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td class="phone_only non-print" <c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or !ks1.uf10.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td align='right' class="phone_only non-print"></td>
							<td rowspan='4' style="width:140px" class='meisai_control non-print control_col'>
								<span class='non-print'>
									<button type='button' name='meisaiDelete' class='btn btn-mini'>削除</button>
									<button type='button' name='meisaiUp' class='btn btn-mini'>↑</button>
									<button type='button' name='meisaiDown' class='btn btn-mini'>↓</button>
									<button type='button' name='meisaiCopy' class='btn btn-mini'>ｺﾋﾟｰ</button>
								</span>
								<input type='hidden' name="shiwakeEdaNo"		value='${su:htmlEscape(shiwakeEdaNo[i])}'>
								<input type='hidden' name="kamokuEdabanEnable"	value='${su:htmlEscape(kamokuEdabanEnable[i])}'>
								<input type='hidden' name="futanBumonEnable"	value='${su:htmlEscape(futanBumonEnable[i])}'>
								<input type='hidden' name='projectEnable'		value='${su:htmlEscape(projectEnable[i])}' >
								<input type='hidden' name='segmentEnable'		value='${su:htmlEscape(segmentEnable[i])}' >
								<input type='hidden' name='zeiritsuEnable'		value='${su:htmlEscape(zeiritsuEnable[i])}' >
								<input type='hidden' name="torihikiName"		value='${su:htmlEscape(torihikiName[i])}'>
								<input type='hidden' name='kamokuCd'			value='${su:htmlEscape(kamokuCd[i])}'>
								<input type='hidden' name='kamokuName'			value='${su:htmlEscape(kamokuName[i])}'>
								<input type='hidden' name="kamokuEdabanCd"		value='${su:htmlEscape(kamokuEdabanCd[i])}'>
								<input type='hidden' name="kamokuEdabanName"	value='${su:htmlEscape(kamokuEdabanName[i])}'>
								<input type='hidden' name="futanBumonCd"		value='${su:htmlEscape(futanBumonCd[i])}'>
								<input type='hidden' name="futanBumonName"		value='${su:htmlEscape(futanBumonName[i])}'>
								<input type='hidden' name="tokuisakiCd"			value='${su:htmlEscape(tokuisakiCd[i])}'>
								<input type='hidden' name="tokuisakiName"		value='${su:htmlEscape(tokuisakiName[i])}'>
								<input type='hidden' name="projectCd"			value='${su:htmlEscape(projectCd[i])}'>
								<input type='hidden' name="projectName"			value='${su:htmlEscape(projectName[i])}'>
								<input type='hidden' name="segmentCd"			value='${su:htmlEscape(segmentCd[i])}'>
								<input type='hidden' name="segmentName"			value='${su:htmlEscape(segmentName[i])}'>	
								<input type='hidden' name="uf1Cd"				value='${su:htmlEscape(uf1Cd[i])}'>
								<input type='hidden' name="uf1Name"				value='${su:htmlEscape(uf1Name[i])}'>
								<input type='hidden' name="uf2Cd"				value='${su:htmlEscape(uf2Cd[i])}'>
								<input type='hidden' name="uf2Name"				value='${su:htmlEscape(uf2Name[i])}'>
								<input type='hidden' name="uf3Cd"				value='${su:htmlEscape(uf3Cd[i])}'>
								<input type='hidden' name="uf3Name"				value='${su:htmlEscape(uf3Name[i])}'>
								<input type='hidden' name="uf4Cd"				value='${su:htmlEscape(uf4Cd[i])}'>
								<input type='hidden' name="uf4Name"				value='${su:htmlEscape(uf4Name[i])}'>
								<input type='hidden' name="uf5Cd"				value='${su:htmlEscape(uf5Cd[i])}'>
								<input type='hidden' name="uf5Name"				value='${su:htmlEscape(uf5Name[i])}'>
								<input type='hidden' name="uf6Cd"				value='${su:htmlEscape(uf6Cd[i])}'>
								<input type='hidden' name="uf6Name"				value='${su:htmlEscape(uf6Name[i])}'>
								<input type='hidden' name="uf7Cd"				value='${su:htmlEscape(uf7Cd[i])}'>
								<input type='hidden' name="uf7Name"				value='${su:htmlEscape(uf7Name[i])}'>
								<input type='hidden' name="uf8Cd"				value='${su:htmlEscape(uf8Cd[i])}'>
								<input type='hidden' name="uf8Name"				value='${su:htmlEscape(uf8Name[i])}'>
								<input type='hidden' name="uf9Cd"				value='${su:htmlEscape(uf9Cd[i])}'>
								<input type='hidden' name="uf9Name"				value='${su:htmlEscape(uf9Name[i])}'>
								<input type='hidden' name="uf10Cd"				value='${su:htmlEscape(uf10Cd[i])}'>
								<input type='hidden' name="uf10Name"			value='${su:htmlEscape(uf10Name[i])}'>
								<input type='hidden' name='shiharaiKingaku'		value='${su:htmlEscape(shiharaiKingaku[i])}'>
								<input type='hidden' name='tekiyou'				value='${su:htmlEscape(tekiyou[i])}'>
								<input type='hidden' name='zeinukiKingaku' value='${su:htmlEscape(zeinukiKingaku[i])}'>
								<input type='hidden' name='shouhizeigaku'  value='${su:htmlEscape(shouhizeigaku[i])}'>
								<input type='hidden' name='chuuki2'				value='${su:htmlEscape(chuuki2[i])}'>
								<input type='hidden' name='keigenZeiritsuKbn'		value='${su:htmlEscape(keigenZeiritsuKbn[i])}'>
								<select name='kazeiKbn' disabled style='display:none;'>
									<c:forEach var="kazeiKbnRecord" items="${kazeiKbnList}">
										<option value='${kazeiKbnRecord.naibuCd}' data-kazeiKbnGroup='${kazeiKbnRecord.option1}' <c:if test='${kazeiKbnRecord.naibuCd eq kazeiKbn[i]}'>selected</c:if>>${su:htmlEscape(kazeiKbnRecord.name)}</option>
									</c:forEach>
								</select>
								<select name='zeiritsu' style='display:none;'>
									<c:forEach var="zeiritsuRecord" items="${zeiritsuList}">
										<option value='${zeiritsuRecord.zeiritsu}' data-hasuuKeisanKbn='${zeiritsuRecord.hasuuKeisanKbn}' data-keigenZeiritsuKbn='${zeiritsuRecord.keigenZeiritsuKbn}' <c:if test='${zeiritsuRecord.zeiritsu eq zeiritsu[i] && zeiritsuRecord.keigenZeiritsuKbn eq keigenZeiritsuKbn[i]}'>selected</c:if>><c:if test='${zeiritsuRecord.keigenZeiritsuKbn eq "1"}'>*</c:if>${su:htmlEscape(zeiritsuRecord.zeiritsu)}</option>
									</c:forEach>
								</select>
								<select name='bunriKbn' style='display:none;'>
									<c:forEach var="bunriKbnRecord" items="${bunriKbnList}">
										<option value='${bunriKbnRecord.naibuCd}'  <c:if test='${bunriKbnRecord.naibuCd eq bunriKbn[i]}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
									</c:forEach>
								</select>
								<select name='kariShiireKbn' style='display:none;'>
									<c:forEach var="shiireKbnRecord" items="${shiireKbnList}">
										<option value='${shiireKbnRecord.naibuCd}'  <c:if test='${shiireKbnRecord.naibuCd eq kariShiireKbn[i]}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr class="meisaiView1 pc_only">
							<td <c:if test='${not ks1.kazeiKbn.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td <c:if test='${not ks1.zeiritsu.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td <c:if test='${not ks1.bunriKbn.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td <c:if test='${not ks1.shiireKbn.hyoujiFlg or shiireZeiAnbun eq "0"}'>style='display:none'</c:if>></td>
							<td align='right' style="word-break: normal;>" <c:if test='${not ks1.shiharaiKingaku.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td align='right' style="word-break: normal;>" <c:if test='${"1" eq invoiceDenpyou or !ks1.zeinukiKingaku.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td align='right' style="word-break: normal;>" <c:if test='${"1" eq invoiceDenpyou or !ks1.shouhizeigaku.hyoujiFlg}'>style='display:none'</c:if>></td>
						</tr>
						<tr class="meisaiView2 pc_only">
							<td <c:if test='${"0" eq pjShiyouFlg or !ks1.project.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td <c:if test='${"0" eq segmentShiyouFlg or !ks1.segment.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td <c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or !ks1.uf1.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td <c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or !ks1.uf2.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td <c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or !ks1.uf3.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td <c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or !ks1.uf4.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td <c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or !ks1.uf5.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td <c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or !ks1.uf6.hyoujiFlg}'>style='display:none'</c:if>></td>
						</tr>
						<tr class="meisaiView3 pc_only">
							<td <c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or !ks1.uf7.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td <c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or !ks1.uf8.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td <c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or !ks1.uf9.hyoujiFlg}'>style='display:none'</c:if>></td>
							<td <c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or !ks1.uf10.hyoujiFlg}'>style='display:none'</c:if>></td>
						</tr>
</c:forEach>
					</tbody>
				</table>
	</div>
</section>

<!-- スクリプト -->
<script style='text/javascript'>
var csvImportFlag =  $("#csvImportFlag").val();
/**
 * 明細のhiddenに明細MAPを反映
 * ダイアログを明細部に反映する途中処理＞これの後の「setDisplayMeisaiData」で明細部を再描画する。
 * 明細の追加・変更時に setMeisaiInfo + setDisplayMeisaiDataが呼ばれる
 * @param index			行(0～)
 * @param meisaiInfo	明細情報MAP
 */
function setMeisaiInfo(index, meisaiInfo) {
	var meisai = $("#meisaiList tr.meisai:eq(" + index + ")");
	meisai.find("input[name=shiwakeEdaNo]").val(meisaiInfo["shiwakeEdaNo"]);
	meisai.find("input[name=torihikiName]").val(meisaiInfo["torihikiName"]);
	meisai.find("input[name=kamokuCd]").val(meisaiInfo["kamokuCd"]);
	meisai.find("input[name=kamokuName]").val(meisaiInfo["kamokuName"]);
	meisai.find("input[name=kamokuEdabanCd]").val(meisaiInfo["kamokuEdabanCd"]);
	meisai.find("input[name=kamokuEdabanName]").val(meisaiInfo["kamokuEdabanName"]);
	meisai.find("input[name=futanBumonCd]").val(meisaiInfo["futanBumonCd"]);
	meisai.find("input[name=futanBumonName]").val(meisaiInfo["futanBumonName"]);
	meisai.find("input[name=tokuisakiCd]").val(meisaiInfo["tokuisakiCd"]),
	meisai.find("input[name=tokuisakiName]").val(meisaiInfo["tokuisakiName"]),
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
	meisai.find("input[name=chuuki2]").val(meisaiInfo["chuuki2"]);

	meisai.find("input[name=kamokuEdabanEnable]").val(meisaiInfo["kamokuEdabanEnable"]);
	meisai.find("input[name=futanBumonEnable]").val(meisaiInfo["futanBumonEnable"]);
	meisai.find("input[name=projectEnable]").val(meisaiInfo["projectEnable"]);
	meisai.find("input[name=segmentEnable]").val(meisaiInfo["segmentEnable"]);
	meisai.find("input[name=zeiritsuEnable]").val(meisaiInfo["zeiritsuEnable"]);
	
	if(meisai.find("select[name=zeiritsu] :selected").attr("data-keigenZeiritsuKbn") !== meisaiInfo["keigenZeiritsuKbn"]){
		//選択肢を選び直す
		meisai.find("select[name=zeiritsu] option").filter(function(index){
			var zeiritsuText = meisaiInfo["zeiritsu"];
			if("1" === meisaiInfo["keigenZeiritsuKbn"]) zeiritsuText = "*"+ meisaiInfo["zeiritsu"];
			return $(this).text() === zeiritsuText;
		}).prop('selected', true);
	}
}

/**
 * 明細MAP取得
 * @param index	行(0～)
 */
function getMeisaiInfo(index) {
	var meisai = $("#meisaiList tr.meisai:eq(" + index + ")");
	
	// 一覧の最大インデックスを取得する
	var maxIndex = $("#meisaiList").find("tr.meisai").length - 1;

	//該当行の明細MAP返す
	return {
		denpyouKbn:				$("input[name=denpyouKbn]").val(),
		index:					index,
		maxIndex:				maxIndex,
		
		shiwakeEdaNo:			meisai.find("input[name=shiwakeEdaNo]").val(),
		torihikiName:			meisai.find("input[name=torihikiName]").val(),
		furikomisakiJouhou:		meisai.find("input[name=furikomisakiJouhou]").val(),
		kamokuCd:				meisai.find("input[name=kamokuCd]").val(),
		kamokuName:				meisai.find("input[name=kamokuName]").val(),
		kamokuEdabanCd:			meisai.find("input[name=kamokuEdabanCd]").val(),
		kamokuEdabanName:		meisai.find("input[name=kamokuEdabanName]").val(),
		futanBumonCd:			meisai.find("input[name=futanBumonCd]").val(),
		futanBumonName:			meisai.find("input[name=futanBumonName]").val(),
		tokuisakiCd:			meisai.find("input[name=tokuisakiCd]").val(),
		tokuisakiName:			meisai.find("input[name=tokuisakiName]").val(),
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
		chuuki2	:				meisai.find("input[name=chuuki2]").val(),
		enableInput:			'true' == $('#enableInput').val(),
		kamokuEdabanEnable:		'true' == meisai.find("input[name=kamokuEdabanEnable]").val(),
		futanBumonEnable:		'true' == meisai.find("input[name=futanBumonEnable]").val(),
		projectEnable:			'true' == meisai.find("input[name=projectEnable]").val(),
		segmentEnable:			'true' == meisai.find("input[name=segmentEnable]").val(),
		zeiritsuEnable:			'true' == meisai.find("input[name=zeiritsuEnable]").val(),
	};
}

/**
 * 明細行数を取得
 * 新規起票後の非表示(取引未選択)の明細はカウントしない
 * @return	明細行数
 */
function getEnableMeisaiCount() {
	var count = 0;
	//▼カスタマイズ
	//var csvImportFlag =  "<c:out value="${csvImportFlag}" />";
	$.each($("#meisaiList input[name=shiwakeEdaNo]"), function(ii, obj) {
		//▼カスタマイズ　
		if ('' != $(obj).val() || csvImportFlag == 'true') {
			count++;
		}
	});
	return count;
}

/**
 * 明細MAPのリストを取得
 * 新規起票後の非表示(取引未選択)の明細だけならnullを返す
 * @return	明細MAPのリスト
 */
function getMeisaiList() {
	//▼カスタマイズ
	//var csvImportFlag =  "<c:out value="${csvImportFlag}" />";
	return $.map($("#meisaiList tr.meisai"), function(tr, ii) {
		var meisaiInfo = getMeisaiInfo(ii);
		//▼カスタマイズ
		if ('' == meisaiInfo['shiwakeEdaNo'] && csvImportFlag == 'false') {
			return null;
		}
		return meisaiInfo;
	});
}

/**
 * テーブルのレイアウトを調整します。主にセル結合を調整します。
 */
function setMeisaiTableLayout() {
	  // 最大のthタグの数を格納する変数
	  var maxThCount = 0;
	
	  // 各行のthタグの数を格納する配列
	  var thCounts = [];
	
	  // すべてのthead内のtr要素を取得
	  $("#meisaiTableDiv").find("table.table-bordered.table-condensed thead tr").each(function(index) {
	    // style属性が"display:none"でないthタグを取得
	    var thElements = $(this).find("th:not([style*='display:none'])");
	
	    // colspanとrowspanを考慮したthタグの数をカウント
	    var thCount = index == 0 ? -2 : 0; // Noとコピーetc用のセルは、最初の行の全行に影響するので無視
	    thElements.each(function() {
	      var colspan = $(this).attr("colspan") ? parseInt($(this).attr("colspan")) : 1;
	      thCount += colspan;
	    });
	
	    // 現在の行のthタグの数を配列に格納
	    thCounts.push(thCount);
	  });

	  maxThCount = Math.max(...thCounts);
	  
	  // 最大のthタグの数に一致するように、セルを追加
	  $("#meisaiTableDiv").find("table.table-bordered.table-condensed thead tr").each(function(index) {
	    // 最大数と現在のthタグの数との差分を計算
	    var diff = maxThCount - thCounts[index];

	    // 差分の数だけセルを追加。完全不可視の行は無視
	    for (var i = 0; thCounts[index] > 0 && i < diff; i++) {
		    if(index == 0)
		    {
		    	$(this).find("#headth").after("<th class='addColumn pc_only'></th>");
		    	$('.meisai').find("#headtd").after("<td class='addColumn pc_only'></td>");
		    }
		    else
		    {
			      $(this).prepend("<th class='addColumn pc_only'></th>");
			      $('.meisaiView' + index).prepend("<td class='addColumn pc_only'></td>");
		    }
	    }
	  });

	// 編集不能な場合、コピーetc用の欄は非表示
	if ($("#enableInput").val() != 'true') {
		$("#meisaiTableDiv .control_col").css("display", "none");
	}
}

/**
 * 内部項目(Hidden設定情報)から画面表示を行います。
 * 全ての明細が対象となります。
 */
function setDisplayMeisaiData() {
	
	if (0 < getEnableMeisaiCount()) {
		$('#meisaiTableDiv').css('display', '');
	} else {
		$('#meisaiTableDiv').css('display', 'none');
		calcMoney();
		return ;
	}

	var meisaiList = $("#meisaiList");
	var no = 0;
	$.each(meisaiList.find("tr.meisai"), function (ii, obj) {
		var index = 0;                      // meisaiの連番
		var vIndex = 0;                     // meisaiView1の連番
		var meisai = $(obj);
		var view = $(obj).next();           // meisaiView1
		var view2 = view.next();            // meisaiView2
		var view3 = view2.next();            // meisaiView3
		//▼カスタマイズ　csv登録時にない可能性あり
		//var csvImportFlag =  "<c:out value="${csvImportFlag}" />";
		if ('' == meisai.find("input[name=shiwakeEdaNo]").val() && csvImportFlag == 'false') {
			// 未設定扱い
			return ;
		}
		//▲カスタマイズ
		no = no + 1;
		// 枝番
		var noObj = meisai.find("td:eq(" + index++ + ")");
		noObj.children().remove();
		noObj.text('');
		noObj.append(
			$("<a/>").text("#" + no).click(function() {
				meisaiDialogOpen(ii, null);
			})
		);
		
		// 追加したカラム分移動
		index = index + meisai.find(".addColumn").length;
		vIndex = vIndex + view.find(".addColumn").length;
		
		// 取引先
		var torihikiNameStr = meisai.find("input[name=torihikiName]").val() || "　";
		meisai.find("td:eq(" + index++ + ")").text(torihikiNameStr);

		// 科目
		var kamokuNameStr = meisai.find("input[name=kamokuName]").val() || "　";
		meisai.find("td:eq(" + index++ + ")").text(kamokuNameStr);

		// 科目枝番
		var kamokuEdabanNameStr = meisai.find("input[name=kamokuEdabanName]").val() || "　";
		meisai.find("td:eq(" + index++ + ")").text(kamokuEdabanNameStr);

		// 負担部門
		var futanBumonNameStr = meisai.find("input[name=futanBumonName]").val() || "　";
		meisai.find("td:eq(" + index++ + ")").text(futanBumonNameStr);

		// 摘要
		var tekiyouStr = meisai.find("input[name=tekiyou]").val();
		meisai.find("td:eq(" + index++ + ")").html(tekiyouStr + "<br><color>" + meisai.find("input[name=chuuki2]").val() + "</color>");
		$(function(){
		  $("color").css("color","red");
		});

// 以下、meisaiには携帯表示用に値詰め

		//課税区分
		var kazeiKbnStr = meisai.find("select[name=kazeiKbn] :selected").text() || "　";
		meisai.find("td:eq(" + index++ + ")").text(kazeiKbnStr);
		view.find("td:eq(" + vIndex++ + ")").text(kazeiKbnStr);

		//消費税率
		var kazeiKbnGroup = meisai.find("select[name=kazeiKbn] option:selected").attr("data-kazeiKbnGroup");
		var zeiritsuStr;
		if (kazeiKbnGroup == "1" || kazeiKbnGroup == "2") {
			zeiritsuStr = meisai.find("select[name=zeiritsu] option:selected").text() + "%";
		} else {
			zeiritsuStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(zeiritsuStr);
		view.find("td:eq(" + vIndex++ + ")").text(zeiritsuStr);

		//分離区分
		var bunriKbnStr = meisai.find("select[name=bunriKbn] option:selected").text() || "　";
		meisai.find("td:eq(" + index++ + ")").text(bunriKbnStr);
		view.find("td:eq(" + vIndex++ + ")").text(bunriKbnStr);

		//仕入区分
		var shiireKbnStr = meisai.find("select[name=kariShiireKbn] option:selected").text() || "　";
		meisai.find("td:eq(" + index++ + ")").text(shiireKbnStr);
		view.find("td:eq(" + vIndex++ + ")").text(shiireKbnStr);
		
		// 支払金額
		var shiharaiKingakuStr = meisai.find("input[name=shiharaiKingaku]").val() ? meisai.find("input[name=shiharaiKingaku]").val() + ' 円' : "　";
		meisai.find("td:eq(" + index++ + ")").text(shiharaiKingakuStr);
		view.find("td:eq(" + vIndex++ + ")").text(shiharaiKingakuStr);
		
		// 税抜金額
		var zeinukiKingakuStr = meisai.find("input[name=zeinukiKingaku]").val() ? meisai.find("input[name=zeinukiKingaku]").val() + ' 円' : "　";
		meisai.find("td:eq(" + index++ + ")").text(zeinukiKingakuStr);
		view.find("td:eq(" + vIndex++ + ")").text(zeinukiKingakuStr);
		
		// 支払金額
		var shouhizeigakuStr = meisai.find("input[name=shouhizeigaku]").val() ? meisai.find("input[name=shouhizeigaku]").val() + ' 円' : "　";
		meisai.find("td:eq(" + index++ + ")").text(shouhizeigakuStr);
		view.find("td:eq(" + vIndex++ + ")").text(shouhizeigakuStr);

//プロジェクト
		vIndex = view2.find(".addColumn").length;
		var projectNameStr = meisai.find("input[name=projectName]").val() || "　";
		view2.find("td:eq(" + vIndex++ + ")").text(projectNameStr);
		meisai.find("td:eq(" + index++ + ")").text(projectNameStr);

//セグメント
		var segmentNameStr = meisai.find("input[name=segmentName]").val() || "　";
		view2.find("td:eq(" + vIndex++ + ")").text(segmentNameStr);
		meisai.find("td:eq(" + index++ + ")").text(segmentNameStr);
		
// UF1～10
		for(let i = 1; i <= 10; i++)
		{
			let shiyouFlg = $("#uf" + i + "ShiyouFlg").val();
			let ufStr = meisai.find("input[name=uf" + i + (shiyouFlg < 2 ? "Cd" : "Name") + "]").val() || "　";
			(i <= 6 ? view2 : view3).find("td:eq(" + vIndex++ + ")").text(ufStr);
			meisai.find("td:eq(" + index++ + ")").text(ufStr);

			vIndex = i == 6 ? view3.find(".addColumn").length : vIndex;
		}
	});

	//整形
	setMeisaiTableLayout();
	
	//金額再計算
	calcMoney();
}

/**
 * 指定された行を直前の行と入れ替える
 * @param tr	指定行TR
 */
function swapRow(tr) {
	var cnt = parseInt($(tr).find('td.meisai_control').attr('rowspan'));
	if (0 < tr.prevAll('.meisai:first').length) {
		var base = tr.prevAll('.meisai:first');
		var targ = tr;
		for (var ii = 0; ii < cnt; ii++) {
			var next = targ.next();
			targ.insertBefore(base);
			targ = next;
		}
	}
	setDisplayMeisaiData();
	isDirty = true;
}

/**
 * 明細↑ボタン押下時Function
 */
function meisaiUp() {
	var tr = $(this).closest("tr.meisai");
	swapRow(tr);
}

/**
 * 明細↓ボタン押下時Function
 */
function meisaiDown() {
	var tr = $(this).closest("tr.meisai");
	if (0 < tr.nextAll('.meisai:first').length) {
		swapRow(tr.nextAll('.meisai:first'));
	}
}

/**
 * 明細削除ボタン押下時Function
 */
function meisaiDelete() {
	var tr = $(this).closest("tr.meisai");
	
	if(window.confirm("明細を削除してもよろしいですか？")) {
		if (getEnableMeisaiCount() == 1) {
			meisaiClear($(this).closest("tr.meisai"));
			//▼カスタマイズ
			$('#kaikeiContent').find("input[name=csvImportFlag]").val(false);
			csvImportFlag = $('#kaikeiContent').find("input[name=csvImportFlag]").val();
		} else {
			for (var ii = 0; ii < parseInt($(this).closest('td.meisai_control').attr('rowspan')) - 1; ii++) {
				tr.next().remove();
			}
			$(this).closest("tr.meisai").remove();
		}
		setDisplayMeisaiData();
		isDirty = true;
	}
}

/**
 * 明細コピーボタン押下時Function
 */
function meisaiCopy() {
	var tr = $(this).closest("tr.meisai");
	var no = parseInt(tr.find("a").text().replace("#",""));
	var index = no - 1;
	if (index >= 0) {
		var meisaiInfo = getMeisaiInfo(index);
		meisaiAdd(meisaiInfo);
	}
}

/**
 * 明細行をクリアします。
 * @param tr	明細行TR
 */
function meisaiClear(tr) {
	var _tr = tr;
	var cnt = parseInt($(tr).find('td.meisai_control').attr('rowspan'));
	for (var ii = 0; ii < cnt; ii++) {
		$.each($(_tr).find("input"), function(ii, obj) {
			$(obj).val("");
		});
		$.each($(_tr).find("td:not(td.meisai_control)"), function(ii, obj) {
			$(obj).text("");
		});
		_tr = _tr.next();
	}
}

/**
 * 明細行追加
 * @param meisaiInfo	明細MAP
 */
function meisaiAdd(meisaiInfo) {
	var tr = null;
	if (0 < getEnableMeisaiCount()) {
		//１行目の明細をコピーして内容を削除
		var _tr = $("#meisaiList tr.meisai:first");
		var cnt = parseInt(_tr.find('td.meisai_control').attr('rowspan'));
		for (var ii = 0; ii < cnt; ii++) {
			tr = _tr.clone();
			// 行の挿入
			$("#meisaiList").append(tr);
			// 初期化
			commonInit($(tr));
			_tr = _tr.next();
		}
		tr = $("#meisaiList tr.meisai:last");
	} else {
		// 空ならそこをそのまま使用する。
		tr = $("#meisaiList tr.meisai:first");
	}

	// 行の初期化
	meisaiClear(tr);

	// 値の設定
	setMeisaiInfo($("#meisaiList tr.meisai").length - 1, meisaiInfo);

	// 再表示
	setDisplayMeisaiData();
	isDirty = true;
}

/**
 * 明細行変更
 * @param index			行(0～)
 * @param meisaiInfo	明細MAP
 */
function meisaiUpdate(index, meisaiInfo) {
	setMeisaiInfo(index, meisaiInfo);
	setDisplayMeisaiData();
	isDirty = true;
}

/**
 * 明細ダイアログを開く
 * 追加(index == -1)、変更(index >= 0)で呼ばれる。
 * @param index				何行目を開くか(0～)、追加なら-1
 * @param sourceMeisaiInfo	コピー元の明細MAP。連続追加(複写)以外では付与
 */
function meisaiDialogOpen(index, sourceMeisaiInfo) {
	const identificationKey = "eteam.shiharaiIraiMeisai";
	var isEnableInput = $('#enableInput').val() == 'true';
	var dialog = null;
	var parent = $("#dialogMeisai");
	parent.children().remove();

	//----------
	//ダイアログの追加・更新処理
	//----------
	var registFunction = function(type) {
		
		//①dialog内の表示状態をMAPに
		var meisaiInfo = getDialogMeisaiInfo();

		//②dialog内のAJAXイベント(confirm)を呼ぶ
		dialog.load(
			"shiharai_irai_meisai_confirm",
			meisaiInfo,
			function() {
				
				//③-1confirmイベントのエラーがあればダイアログはそのまま残す
				if ("0" != $(this).find("#errorCnt").val()) {
					setDialogMeisaiInfo(isEnableInput, meisaiInfo);
					return ;
				}
				
				//③-2confirmイベントのエラーがなければダイアログを閉じて①時点のMAPを明細部に反映する
				//後は変更、追加等のトリガーにより微妙
				$(this).dialog("close");
				if (type == "update") {
					//変更ボタン：呼び出し元行に反映
					meisaiUpdate(index, meisaiInfo);
				}
				if (type == "insertClose") {
					//追加して閉じるボタン：明細を追加
					meisaiAdd(meisaiInfo);
				}
				if (type == "insertNextCopy") {
					//連続追加(複写)ボタン：明細を追加、ダイアログをもう一度開く、今の状態を再現
					meisaiAdd(meisaiInfo);
					meisaiInfo.kariKanjouKeshikomiNo = "";
					meisaiDialogOpen(-1, meisaiInfo);
				}
				if (type == "insertNextClear") {
					//連続追加(クリア)ボタン：明細を追加、ダイアログをもう一度開く
					meisaiAdd(meisaiInfo);
					meisaiDialogOpen(-1, null);
				}
				updateDenpyouMeisaiEvent(meisaiInfo);
			}
		);
	};

	//----------
	//ダイアログ下部ボタン作る
	//----------
	var buttons = {};
	var title = "明細参照";
	if (isEnableInput) {
		if (0 <= index) {
			buttons["変更"] = function() {
				registFunction("update");
			};
			title = "明細変更";
		} else {
			buttons["連続追加(複写)"] = function() {
				registFunction("insertNextCopy");
			};
			buttons["連続追加(クリア)"] = function() {
				registFunction("insertNextClear");
			};
			buttons["追加して閉じる"] = function() {
				registFunction("insertClose");
			};
			title = "明細追加";
		}
	}
	buttons["閉じる"] = function() {
		$(this).dialog("close");
	};

	//----------
	//ダイアログDIV開く
	//----------
	var width = "950";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	
	var meisai = $("#meisaiList tr.meisai:eq(" + index + ")");
	var view = $("#meisaiList tr.meisaiView1:eq(" + index + ")");
	var view2 = $("#meisaiList tr.meisaiView2:eq(" + index + ")");
	var view3 = $("#meisaiList tr.meisaiView3:eq(" + index + ")");

	dialog = parent.dialog({
		modal: true,
		width: width,
		height: "520",
		title: title,
		appendTo:"#content",
		buttons: buttons,
		close: function() {
			parent.children().remove();
			if (0 <= index) {
				// 更新行の背景色を透明に変更する
				meisai.css('background-color','transparent');
				view.css('background-color','transparent');
				view2.css('background-color','transparent');
				view3.css('background-color','transparent');
			}
		},
		resizeStop: function() { saveMeisaiSize($(this), identificationKey); },
	});
	resizeMeisai(dialog, identificationKey);

	//----------
	//既存明細を開くなら(明細番号・前・次)、該当行の背景色を黄色にする
	//----------
	if (0 <= index) {
		meisai.css('background-color','yellow');
		view.css('background-color','yellow');
		view2.css('background-color','yellow');
		view3.css('background-color','yellow');
	}

	//----------
	//ダイアログの表示
	//----------
	
	//ダイアログに表示する明細MAPを作る
	var meisaiInfo = null;
	if  (0 <= index) {
		//既存明細を開く場合(明細番号・前・次)、該当明細のhidden情報から
		meisaiInfo = getMeisaiInfo(index);
	} else {
		//明細追加の場合、基本はnull(まっさら標示)だけど、複写追加の時だけコピー元MAPもらっている
		meisaiInfo = sourceMeisaiInfo;//普通の追加ならnull、複写追加なら複写元
	}
	//ダイアログのinitイベントのパラメータMAPを作る
	var actionParams = (index == -1 && sourceMeisaiInfo == null) ?
			//追加ボタンの場合は必要キーだけ渡してinitイベント
			{
				enableInput:	isEnableInput,
				denpyouKbn:		$("input[name=denpyouKbn]").val()
			}
			//既存明細を開く場合(明細番号・前・次)や複写追加の場合は明細情報全部渡してinitイベント
			:meisaiInfo;
	
	//ダイアログに明細初期表示イベントをロード(ロード直後は空)
	//ロード後に明細MAPをダイアログに反映
	dialog.load(
		"shiharai_irai_meisai_init",
		actionParams,
		function() {
			setDialogMeisaiInfo(isEnableInput, meisaiInfo);
		});
}

//初期化処理
if ($('#enableInput').val()) {
	
	//明細追加ボタン
	$("#meisaiAddButton").click(function(){
		meisaiDialogOpen(-1, null);
	});

	//明細イベント割り付け
	$("body").on("click","button[name=meisaiUp]",meisaiUp); 
	$("body").on("click","button[name=meisaiDown]",meisaiDown); 
	$("body").on("click","button[name=meisaiDelete]",meisaiDelete); 
	$("body").on("click","button[name=meisaiCopy]",meisaiCopy); 
}
</script>
