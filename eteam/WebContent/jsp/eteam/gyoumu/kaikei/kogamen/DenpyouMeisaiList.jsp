<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<c:set var='useKousaihi' value='${"A009" ne denpyouKbn and ks1.kousaihiShousai.hyoujiFlg}'/>

<style type='text/css'>
<!--
#meisaiTableDiv td {
	max-width: 200px;
}
-->
</style>

<section id='meisaiField' class='print-unit' <c:if test='${denpyouKbn eq "A004" and ! action.sonotaKeihiView}'>style='display:none'</c:if>>
	<h2>明細<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A005" || denpyouKbn eq "A011" || denpyouKbn eq "A012"}'>(その他　経費)</c:if></h2>
	<div>
		<input type='hidden' id="enableInput" value='${su:htmlEscape(enableInput)}'>
		<input type='hidden' name='meisaiAddTakeOverEnabled' value='${su:htmlEscape(meisaiAddTakeOverEnabled)}'>
		<button type='button' id='meisaiAddButton3' class='btn btn-small'>明細追加</button>
	</div>
	<div id='meisaiTableDiv' class='no-more-tables' style='display:none'>
				<table class='table-bordered table-condensed' style='margin-bottom:5px; border-collapse: collapse; word-break: break-all; word-wrap: break-word;'>
					<thead>
						<tr id='meisaiTableHeader1'>
							<th rowspan='5' id='headth'>No</th>
							<c:if test='${(("A011" eq denpyouKbn || "A012" eq denpyouKbn)) && ks1.shucchouKbn.hyoujiFlg}'><th>${su:htmlEscape(ks1.shucchouKbn.name)}</th></c:if>
							<c:if test='${("A001" eq denpyouKbn) && ks1.userName.hyoujiFlg}'><th>${su:htmlEscape(ks1.userName.name)}</th></c:if>
							<c:if test='${ks1.shiyoubi.hyoujiFlg}'><th>${su:htmlEscape(ks1.shiyoubi.name)}</th></c:if>
							<c:if test='${("A001" eq denpyouKbn || "A004" eq denpyouKbn || "A011" eq denpyouKbn) && ks1.shouhyouShoruiFlg.hyoujiFlg}'><th>証憑</th></c:if>
							<c:if test='${ks1.torihiki.hyoujiFlg}'><th colspan='2'>${su:htmlEscape(ks1.torihiki.name)}</th></c:if>
							<c:if test='${ks1.kamoku.hyoujiFlg}'><th>${su:htmlEscape(ks1.kamoku.name)}</th></c:if>
							<c:if test='${ks1.kamokuEdaban.hyoujiFlg}'><th>${su:htmlEscape(ks1.kamokuEdaban.name)}</th></c:if>
							<c:if test='${ks1.futanBumon.hyoujiFlg}'><th>${su:htmlEscape(ks1.futanBumon.name)}</th></c:if>
							<c:if test='${ks1.torihikisaki.hyoujiFlg}'><th>${su:htmlEscape(ks1.torihikisaki.name)}<c:if test='${ks1.furikomisakiJouhou.hyoujiFlg}'>/${su:htmlEscape(ks1.furikomisakiJouhou.name)}</c:if></th></c:if>
							<!-- 以下携帯用 -->
							<c:if test='${ks1.shiharaisaki.hyoujiFlg}'><th style='display:none;'><span class='invoiceOnly'>${su:htmlEscape(ks1.shiharaisaki.name)}</span></th></c:if>
							<c:if test='${ks1.jigyoushaKbn.hyoujiFlg}'><th style='display:none;'><span class='invoiceOnly'>${su:htmlEscape(ks1.jigyoushaKbn.name)}</span></th></c:if>
							<c:if test='${ks1.kazeiKbn.hyoujiFlg}'><th style='display:none;'>${su:htmlEscape(ks1.kazeiKbn.name)}</th></c:if>
							<c:if test='${ks1.zeiritsu.hyoujiFlg}'><th style='display:none;'>${su:htmlEscape(ks1.zeiritsu.name)}</th></c:if>
							<c:if test='${ks1.bunriKbn.hyoujiFlg}'><th style='display:none;'>${su:htmlEscape(ks1.bunriKbn.name)}</th></c:if>
							<c:if test='${ks1.shiireKbn.hyoujiFlg and shiireZeiAnbun eq "1"}'><th style='display:none;'>${su:htmlEscape(ks1.shiireKbn.name)}</th></c:if>
							<c:if test='${ks1.shiharaiKingaku.hyoujiFlg}'><th style='display:none;'>${su:htmlEscape(ks1.shiharaiKingaku.name)}</th></c:if>
							<c:if test='${ks1.zeinukiKingaku.hyoujiFlg}'><th style='display:none;'><span class='invoiceOnly'>${su:htmlEscape(ks1.zeinukiKingaku.name)}</span></th></c:if>
							<c:if test='${ks1.shouhizeigaku.hyoujiFlg}'><th style='display:none;'><span class='invoiceOnly'>${su:htmlEscape(ks1.shouhizeigaku.name)}</span></th></c:if>
							<c:if test='${("0" ne pjShiyouFlg and ks1.project.hyoujiFlg)}'><th style='display:none;'>${su:htmlEscape(project.uf1Name)}</th></c:if>
							<c:if test='${("0" ne segmentShiyouFlg and ks1.segment.hyoujiFlg)}'><th style='display:none;'>${su:htmlEscape(ks1.segment.name)}</th></c:if>
							<c:if test='${"0" ne hfUfSeigyo.uf1ShiyouFlg and ks1.uf1.hyoujiFlg}'><th style='display:none;'>${su:htmlEscape(hfUfSeigyo.uf1Name)}</th></c:if>
							<c:if test='${"0" ne hfUfSeigyo.uf2ShiyouFlg and ks1.uf2.hyoujiFlg}'><th style='display:none;'>${su:htmlEscape(hfUfSeigyo.uf2Name)}</th></c:if>
							<c:if test='${"0" ne hfUfSeigyo.uf3ShiyouFlg and ks1.uf3.hyoujiFlg}'><th style='display:none;'>${su:htmlEscape(hfUfSeigyo.uf3Name)}</th></c:if>
							<c:if test='${"0" ne hfUfSeigyo.uf4ShiyouFlg and ks1.uf4.hyoujiFlg}'><th style='display:none;'>${su:htmlEscape(hfUfSeigyo.uf4Name)}</th></c:if>
							<c:if test='${"0" ne hfUfSeigyo.uf5ShiyouFlg and ks1.uf5.hyoujiFlg}'><th style='display:none;'>${su:htmlEscape(hfUfSeigyo.uf5Name)}</th></c:if>
							<c:if test='${"0" ne hfUfSeigyo.uf6ShiyouFlg and ks1.uf6.hyoujiFlg}'><th style='display:none;'>${su:htmlEscape(hfUfSeigyo.uf6Name)}</th></c:if>
							<c:if test='${"0" ne hfUfSeigyo.uf7ShiyouFlg and ks1.uf7.hyoujiFlg}'><th style='display:none;'>${su:htmlEscape(hfUfSeigyo.uf7Name)}</th></c:if>
							<c:if test='${"0" ne hfUfSeigyo.uf8ShiyouFlg and ks1.uf8.hyoujiFlg}'><th style='display:none;'>${su:htmlEscape(hfUfSeigyo.uf8Name)}</th></c:if>
							<c:if test='${"0" ne hfUfSeigyo.uf9ShiyouFlg and ks1.uf9.hyoujiFlg}'><th style='display:none;'>${su:htmlEscape(hfUfSeigyo.uf9Name)}</th></c:if>
							<c:if test='${"0" ne hfUfSeigyo.uf10ShiyouFlg and ks1.uf10.hyoujiFlg}'><th style='display:none;'>${su:htmlEscape(hfUfSeigyo.uf10Name)}</th></c:if>
							<c:if test='${ks1.tekiyou.hyoujiFlg}'><th style='display:none;'>${su:htmlEscape(ks1.tekiyou.name)}</th></c:if>
							<c:if test='${useKousaihi}'><th style='display:none;'>${su:htmlEscape(ks1.kousaihiShousai.name)}</th></c:if>
							<c:if test='${useKousaihi}'><th style='display:none;'>人数</th></c:if>
							<c:if test='${useKousaihi}'><th style='display:none;'>一人当たりの金額</th></c:if>
							<c:if test="${ks1.heishu.hyoujiFlg && enableGaika}"><th style='display:none;'>${su:htmlEscape(ks1.heishu.name)}</th></c:if>
							<c:if test="${ks1.rate.hyoujiFlg && enableGaika}"><th style='display:none;'>${su:htmlEscape(ks1.rate.name)}</th></c:if>
							<c:if test="${ks1.gaika.hyoujiFlg && enableGaika}"><th style='display:none;'>${su:htmlEscape(ks1.gaika.name)}</th></c:if>
							<th rowspan='5' class='non-print control_col'></th>
							<c:if test="${houjinCardFlag || kaishaTehaiFlag}"><th rowspan='5' class='rangai_col'></th></c:if>
						</tr>
						<tr id='meisaiTableHeader2'>
							<c:if test='${ks1.shiharaisaki.hyoujiFlg}'><th><span class='invoiceOnly'>${su:htmlEscape(ks1.shiharaisaki.name)}</span></th></c:if>
							<c:if test='${ks1.jigyoushaKbn.hyoujiFlg}'><th><span class='invoiceOnly'>${su:htmlEscape(ks1.jigyoushaKbn.name)}</span></th></c:if>
							<c:if test='${ks1.kazeiKbn.hyoujiFlg}'><th>${su:htmlEscape(ks1.kazeiKbn.name)}</th></c:if>
							<c:if test='${ks1.zeiritsu.hyoujiFlg}'><th>${su:htmlEscape(ks1.zeiritsu.name)}</th></c:if>
							<c:if test='${ks1.bunriKbn.hyoujiFlg}'><th>${su:htmlEscape(ks1.bunriKbn.name)}</th></c:if>
							<c:if test='${ks1.shiireKbn.hyoujiFlg and shiireZeiAnbun eq "1"}'><th>${su:htmlEscape(ks1.shiireKbn.name)}</th></c:if>
							<c:if test='${ks1.shiharaiKingaku.hyoujiFlg}'><th>${su:htmlEscape(ks1.shiharaiKingaku.name)}</th></c:if>
							<c:if test='${ks1.zeinukiKingaku.hyoujiFlg}'><th><span class='invoiceOnly'>${su:htmlEscape(ks1.zeinukiKingaku.name)}</span></th></c:if>
							<c:if test='${ks1.shouhizeigaku.hyoujiFlg}'><th><span class='invoiceOnly'>${su:htmlEscape(ks1.shouhizeigaku.name)}</span></th></c:if>
						</tr>
						<tr id='meisaiTableHeader3'>
							<c:if test='${("0" ne pjShiyouFlg and ks1.project.hyoujiFlg)}'><th>${su:htmlEscape(ks1.project.name)}</th></c:if>
							<c:if test='${("0" ne segmentShiyouFlg and ks1.segment.hyoujiFlg)}'><th>${su:htmlEscape(ks1.segment.name)}</th></c:if>
							<c:if test='${("0" ne hfUfSeigyo.uf1ShiyouFlg and ks1.uf1.hyoujiFlg)}'><th>${su:htmlEscape(hfUfSeigyo.uf1Name)}</th></c:if>
							<c:if test='${("0" ne hfUfSeigyo.uf2ShiyouFlg and ks1.uf2.hyoujiFlg)}'><th>${su:htmlEscape(hfUfSeigyo.uf2Name)}</th></c:if>
							<c:if test='${("0" ne hfUfSeigyo.uf3ShiyouFlg and ks1.uf3.hyoujiFlg)}'><th>${su:htmlEscape(hfUfSeigyo.uf3Name)}</th></c:if>
							<c:if test='${("0" ne hfUfSeigyo.uf4ShiyouFlg and ks1.uf4.hyoujiFlg)}'><th>${su:htmlEscape(hfUfSeigyo.uf4Name)}</th></c:if>
							<c:if test='${("0" ne hfUfSeigyo.uf5ShiyouFlg and ks1.uf5.hyoujiFlg)}'><th>${su:htmlEscape(hfUfSeigyo.uf5Name)}</th></c:if>
							<c:if test='${("0" ne hfUfSeigyo.uf6ShiyouFlg and ks1.uf6.hyoujiFlg)}'><th>${su:htmlEscape(hfUfSeigyo.uf6Name)}</th></c:if>
						</tr>
						<tr id='meisaiTableHeader4'>
							<c:if test='${("0" ne hfUfSeigyo.uf7ShiyouFlg and ks1.uf7.hyoujiFlg)}'><th>${su:htmlEscape(hfUfSeigyo.uf7Name)}</th></c:if>
							<c:if test='${("0" ne hfUfSeigyo.uf8ShiyouFlg and ks1.uf8.hyoujiFlg)}'><th>${su:htmlEscape(hfUfSeigyo.uf8Name)}</th></c:if>
							<c:if test='${("0" ne hfUfSeigyo.uf9ShiyouFlg and ks1.uf9.hyoujiFlg)}'><th>${su:htmlEscape(hfUfSeigyo.uf9Name)}</th></c:if>
							<c:if test='${("0" ne hfUfSeigyo.uf10ShiyouFlg and ks1.uf10.hyoujiFlg)}'><th>${su:htmlEscape(hfUfSeigyo.uf10Name)}</th></c:if>							
							<c:if test="${ks1.heishu.hyoujiFlg && enableGaika}"><th>${su:htmlEscape(ks1.heishu.name)}</th></c:if>
							<c:if test="${ks1.rate.hyoujiFlg && enableGaika}"><th>${su:htmlEscape(ks1.rate.name)}</th></c:if>
							<c:if test="${ks1.gaika.hyoujiFlg && enableGaika}"><th>${su:htmlEscape(ks1.gaika.name)}</th></c:if>
						</tr>
						<tr id='meisaiTableHeader5'>
							<c:if test='${ks1.tekiyou.hyoujiFlg}'><th>${su:htmlEscape(ks1.tekiyou.name)}</th></c:if>
							<c:if test='${useKousaihi}'><th>${su:htmlEscape(ks1.kousaihiShousai.name)}</th></c:if>
							<c:if test='${useKousaihi}'><th>人数</th></c:if>
							<c:if test='${useKousaihi}'><th>一人当たりの金額</th></c:if>
						</tr>
					</thead>
					<tbody id='meisaiList'>
<c:forEach var="i" begin="0" end="${fn:length(shiwakeEdaNo) - 1}" step="1">
	<c:if test='${(fn:length(shiwakeEdaNo) == 1 && i == 0) or not(denpyouKbn eq "A001" and !allMeisaiView and sessionScope.user.seigyoUserId ne userId[i])}'>
								<tr class="meisai">
									<td rowspan='5' align='center' id='headtd'></td>
									<c:if test='${(("A011" eq denpyouKbn || "A012" eq denpyouKbn)) && ks1.shucchouKbn.hyoujiFlg}'><td></td></c:if>
									<c:if test='${("A001" eq denpyouKbn) && ks1.userName.hyoujiFlg}'><td></td></c:if>
									<c:if test='${ks1.shiyoubi.hyoujiFlg}'><td ></td></c:if>
									<c:if test='${("A001" eq denpyouKbn || "A004" eq denpyouKbn || "A011" eq denpyouKbn) && ks1.shouhyouShoruiFlg.hyoujiFlg}'><td align='center'></td></c:if>
									<c:if test='${ks1.torihiki.hyoujiFlg}'><td colspan='2'></td></c:if>
									<c:if test='${ks1.kamoku.hyoujiFlg}'><td ></td></c:if>
									<c:if test='${ks1.kamokuEdaban.hyoujiFlg}'><td ></td></c:if>
									<c:if test='${ks1.futanBumon.hyoujiFlg}'><td ></td></c:if>
									<c:if test='${ks1.torihikisaki.hyoujiFlg}'><td ></td></c:if>
									<!-- 以下携帯用 -->
									<c:if test='${ks1.shiharaisaki.hyoujiFlg}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${ks1.jigyoushaKbn.hyoujiFlg}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${ks1.kazeiKbn.hyoujiFlg}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${ks1.zeiritsu.hyoujiFlg}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${ks1.bunriKbn.hyoujiFlg}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${ks1.shiireKbn.hyoujiFlg and shiireZeiAnbun eq "1"}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${ks1.shiharaiKingaku.hyoujiFlg}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${ks1.zeinukiKingaku.hyoujiFlg}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${ks1.shouhizeigaku.hyoujiFlg}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${("0" ne pjShiyouFlg and ks1.project.hyoujiFlg)}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${("0" ne segmentShiyouFlg and ks1.segment.hyoujiFlg)}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf1ShiyouFlg and ks1.uf1.hyoujiFlg)}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf2ShiyouFlg and ks1.uf2.hyoujiFlg)}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf3ShiyouFlg and ks1.uf3.hyoujiFlg)}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf4ShiyouFlg and ks1.uf4.hyoujiFlg)}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf5ShiyouFlg and ks1.uf5.hyoujiFlg)}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf6ShiyouFlg and ks1.uf6.hyoujiFlg)}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf7ShiyouFlg and ks1.uf7.hyoujiFlg)}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf8ShiyouFlg and ks1.uf8.hyoujiFlg)}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf9ShiyouFlg and ks1.uf9.hyoujiFlg)}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf10ShiyouFlg and ks1.uf10.hyoujiFlg)}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${ks1.tekiyou.hyoujiFlg}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${useKousaihi}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${useKousaihi}'><td class="phone_only non-print"></td></c:if>
									<c:if test='${useKousaihi}'><td class="phone_only non-print"></td></c:if>
									<c:if test="${ks1.heishu.hyoujiFlg && enableGaika}"><td class="phone_only non-print"></td></c:if>
									<c:if test="${ks1.rate.hyoujiFlg && enableGaika}"><td class="phone_only non-print"></td></c:if>
									<c:if test="${ks1.gaika.hyoujiFlg && enableGaika}"><td class="phone_only non-print"></td></c:if>
									<td rowspan='5' style="width:140px" class='meisai_control non-print control_col'>
										<span class='non-print'>
											<button type='button' name='meisaiDelete' class='btn btn-mini'>削除</button>
											<button type='button' name='meisaiUp' class='btn btn-mini'>↑</button>
											<button type='button' name='meisaiDown' class='btn btn-mini'>↓</button>
											<button type='button' name='meisaiCopy' class='btn btn-mini'>ｺﾋﾟｰ</button>
										</span>
										<input type='hidden' name="kaigaiFlg"			value='${su:htmlEscape(kaigaiFlg[i])}'>
										<input type='hidden' name="userId"				value='${su:htmlEscape(userId[i])}'>
										<input type='hidden' name="userName"			value='${su:htmlEscape(userName[i])}'>
										<input type='hidden' name="shiyoubi"			value='${su:htmlEscape(shiyoubi[i])}'>
										<input type='hidden' name="shouhyouShorui"		value='${su:htmlEscape(shouhyouShorui[i])}'>
										<input type='hidden' name="shainNo"				value='${su:htmlEscape(shainNo[i])}'>
										<input type='hidden' name="shiwakeEdaNo"		value='${su:htmlEscape(shiwakeEdaNo[i])}'>
										<input type='hidden' name="kamokuEdabanEnable"	value='${su:htmlEscape(kamokuEdabanEnable[i])}'>
										<input type='hidden' name="futanBumonEnable"	value='${su:htmlEscape(futanBumonEnable[i])}'>
										<input type='hidden' name="torihikisakiEnable"	value='${su:htmlEscape(torihikisakiEnable[i])}'>
										<input type='hidden' name='kousaihiEnable'		value='${su:htmlEscape(kousaihiEnable[i])}' >
										<input type='hidden' name='ninzuuEnable'		value='${su:htmlEscape(ninzuuEnable[i])}' >
										<input type='hidden' name='projectEnable'		value='${su:htmlEscape(projectEnable[i])}' >
										<input type='hidden' name='segmentEnable'		value='${su:htmlEscape(segmentEnable[i])}' >
										<input type='hidden' name='zeiritsuEnable'		value='${su:htmlEscape(zeiritsuEnable[i])}' >
										<input type='hidden' name="torihikiName"		value='${su:htmlEscape(torihikiName[i])}'>
										<input type='hidden' name="furikomisakiJouhou"	value='${su:htmlEscape(furikomisakiJouhou[i])}'>
										<input type='hidden' name='kamokuCd'			value='${su:htmlEscape(kamokuCd[i])}'>
										<input type='hidden' name='kamokuName'			value='${su:htmlEscape(kamokuName[i])}'>
										<input type='hidden' name="kamokuEdabanCd"		value='${su:htmlEscape(kamokuEdabanCd[i])}'>
										<input type='hidden' name="kamokuEdabanName"	value='${su:htmlEscape(kamokuEdabanName[i])}'>
										<input type='hidden' name="futanBumonCd"		value='${su:htmlEscape(futanBumonCd[i])}'>
										<input type='hidden' name="futanBumonName"		value='${su:htmlEscape(futanBumonName[i])}'>
										<input type='hidden' name="torihikisakiCd"		value='${su:htmlEscape(torihikisakiCd[i])}'>
										<input type='hidden' name="torihikisakiName"	value='${su:htmlEscape(torihikisakiName[i])}'>
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
										<input type='hidden' name="heishuCd"			value='${su:htmlEscape(heishuCd[i])}'>
										<input type='hidden' name="rate"				value='${su:htmlEscape(rate[i])}'>
										<input type='hidden' name="gaika"				value='${su:htmlEscape(gaika[i])}'>
										<input type='hidden' name="tani"				value='${su:htmlEscape(tani[i])}'>
										<input type='hidden' name='shiharaiKingaku'		class='autoNumeric' value='${su:htmlEscape(shiharaiKingaku[i])}'>
										<input type='hidden' name='hontaiKingaku'		class='autoNumeric' value='${su:htmlEscape(hontaiKingaku[i])}'>
										<input type='hidden' name='shouhizeigaku'		class='autoNumeric' value='${su:htmlEscape(shouhizeigaku[i])}'>
										<input type='hidden' name="houjinCardFlgKeihi"	value='${su:htmlEscape(houjinCardFlgKeihi[i])}'>
										<input type='hidden' name="kaishaTehaiFlgKeihi"	value='${su:htmlEscape(kaishaTehaiFlgKeihi[i])}'>
										<input type='hidden' name='tekiyou'				value='${su:htmlEscape(tekiyou[i])}'>
										<c:if test='${"A004" ne denpyouKbn && "A011" ne denpyouKbn}'>
										<input type='hidden' name='shiharaisakiName'       value='${su:htmlEscape(shiharaisakiName[i])}'>
										<input type='hidden' name='jigyoushaKbn'       value='${su:htmlEscape(jigyoushaKbn[i])}'>
										</c:if>
										<c:if test='${"A004" eq denpyouKbn || "A011" eq denpyouKbn}'>
										<input type='hidden' name='shiharaisakiNameKeihi'       value='${su:htmlEscape(shiharaisakiNameKeihi[i])}'>
										<input type='hidden' name='jigyoushaKbnKeihi'       value='${su:htmlEscape(jigyoushaKbnKeihi[i])}'>
										</c:if>
										<input type='hidden' name='chuuki2'				value='${su:htmlEscape(chuuki2[i])}'>
										<input type='hidden' name='chuukiKousai2'		value='${su:htmlEscape(chuukiKousai2[i])}'>
										<input type='hidden' name="kousaihiShousai"		value='${su:htmlEscape(kousaihiShousai[i])}'>
										<input type='hidden' name="kousaihiNinzuu"		value='${su:htmlEscape(kousaihiNinzuu[i])}'>
										<input type='hidden' name="kousaihiHitoriKingaku"	value='${su:htmlEscape(kousaihiHitoriKingaku[i])}'>
										<input type='hidden' name="kousaihiHyoujiFlg"	value='${su:htmlEscape(kousaihiHyoujiFlg[i])}'>
										<input type='hidden' name="ninzuuRiyouFlg"	value='${su:htmlEscape(ninzuuRiyouFlg[i])}'>
										<input type='hidden' name="kazeiFlg"			value='${su:htmlEscape(kazeiFlg[i])}'>
										<input type='hidden' name="himodukeCardMeisaiKeihi"	value='${su:htmlEscape(himodukeCardMeisaiKeihi[i])}'>
										<input type='hidden' name='keigenZeiritsuKbn'		value='${su:htmlEscape(keigenZeiritsuKbn[i])}'>
										<input type='hidden' name='shoriGroup'			value='${su:htmlEscape(shoriGroup[i])}'>
										<select name='kazeiKbn' disabled style='display:none;'>
											<c:forEach var="kazeiKbnRecord" items="${kazeiKbnList}">
												<option value='${kazeiKbnRecord.naibu_cd}' data-kazeiKbnGroup='${kazeiKbnRecord.option1}' <c:if test='${kazeiKbnRecord.naibu_cd eq kazeiKbn[i]}'>selected</c:if>>${su:htmlEscape(kazeiKbnRecord.name)}</option>
											</c:forEach>
										</select>
										<select name='zeiritsu' style='display:none;'>
											<c:forEach var="zeiritsuRecord" items="${zeiritsuList}">
												<option value='${zeiritsuRecord.zeiritsu}' data-hasuuKeisanKbn='${zeiritsuRecord.hasuu_keisan_kbn}' data-keigenZeiritsuKbn='${zeiritsuRecord.keigen_zeiritsu_kbn}' <c:if test='${zeiritsuRecord.zeiritsu eq zeiritsu[i] && zeiritsuRecord.keigen_zeiritsu_kbn eq keigenZeiritsuKbn[i]}'>selected</c:if>><c:if test='${zeiritsuRecord.keigen_zeiritsu_kbn eq "1"}'>*</c:if>${su:htmlEscape(zeiritsuRecord.zeiritsu)}</option>
											</c:forEach>
										</select>
										<c:if test='${"A004" ne denpyouKbn && "A011" ne denpyouKbn}'>
										<select name='bunriKbn'  disabled style='display:none;'>
											<c:forEach var="bunriKbnRecord" items="${bunriKbnList}">
												<option value='${bunriKbnRecord.naibu_cd}'<c:if test='${bunriKbnRecord.naibu_cd eq bunriKbn[i]}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
											</c:forEach>
										</select>
										<select name='kariShiireKbn' disabled style='display:none;'>
											<c:forEach var="shiireKbnRecord" items="${shiireKbnList}">
												<option value='${shiireKbnRecord.naibu_cd}' <c:if test='${shiireKbnRecord.naibu_cd eq kariShiireKbn[i]}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
											</c:forEach>
										</select>
										</c:if>
										<c:if test='${"A004" eq denpyouKbn || "A011" eq denpyouKbn}'>
										<select name='bunriKbnKeihi'  disabled style='display:none;'>
											<c:forEach var="bunriKbnRecord" items="${bunriKbnList}">
												<option value='${bunriKbnRecord.naibu_cd}'<c:if test='${bunriKbnRecord.naibu_cd eq bunriKbnKeihi[i]}'>selected</c:if>>${su:htmlEscape(bunriKbnRecord.name)}</option>
											</c:forEach>
										</select>
										<select name='kariShiireKbnKeihi' disabled style='display:none;'>
											<c:forEach var="shiireKbnRecord" items="${shiireKbnList}">
												<option value='${shiireKbnRecord.naibu_cd}' <c:if test='${shiireKbnRecord.naibu_cd eq kariShiireKbnKeihi[i]}'>selected</c:if>>${su:htmlEscape(shiireKbnRecord.name)}</option>
											</c:forEach>
										</select>
										</c:if>
									</td>
									<c:if test="${houjinCardFlag || kaishaTehaiFlag}"><td rowspan='5' class='meisai_control rangai_col'></td></c:if>
								</tr>
								<tr class="meisaiView1 pc_only">
									<c:if test='${ks1.shiharaisaki.hyoujiFlg}'><td ></td></c:if>
									<c:if test='${ks1.jigyoushaKbn.hyoujiFlg}'><td ></td></c:if>
									<c:if test='${ks1.kazeiKbn.hyoujiFlg}'><td ></td></c:if>
									<c:if test='${ks1.zeiritsu.hyoujiFlg}'><td ></td></c:if>
									<c:if test='${ks1.bunriKbn.hyoujiFlg}'><td ></td></c:if>
									<c:if test='${ks1.shiireKbn.hyoujiFlg and shiireZeiAnbun eq "1"}'><td ></td></c:if>
									<c:if test='${ks1.shiharaiKingaku.hyoujiFlg}'><td align='right' style="word-break: normal;>"></td></c:if>
									<c:if test='${ks1.zeinukiKingaku.hyoujiFlg}'><td align='right' style="word-break: normal;>"></td></c:if>
									<c:if test='${ks1.shouhizeigaku.hyoujiFlg}'><td align='right' style="word-break: normal;>"></td></c:if>
								</tr>
								<tr class="meisaiView2 pc_only">
									<c:if test='${("0" ne pjShiyouFlg and ks1.project.hyoujiFlg)}'><td ></td></c:if>
									<c:if test='${("0" ne segmentShiyouFlg and ks1.segment.hyoujiFlg)}'><td ></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf1ShiyouFlg and ks1.uf1.hyoujiFlg)}'><td ></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf2ShiyouFlg and ks1.uf2.hyoujiFlg)}'><td ></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf3ShiyouFlg and ks1.uf3.hyoujiFlg)}'><td ></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf4ShiyouFlg and ks1.uf4.hyoujiFlg)}'><td ></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf5ShiyouFlg and ks1.uf5.hyoujiFlg)}'><td ></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf6ShiyouFlg and ks1.uf6.hyoujiFlg)}'><td ></td></c:if>
								</tr>
								<tr class="meisaiView3 pc_only">
									<c:if test='${("0" ne hfUfSeigyo.uf7ShiyouFlg and ks1.uf7.hyoujiFlg)}'><td ></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf8ShiyouFlg and ks1.uf8.hyoujiFlg)}'><td ></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf9ShiyouFlg and ks1.uf9.hyoujiFlg)}'><td ></td></c:if>
									<c:if test='${("0" ne hfUfSeigyo.uf10ShiyouFlg and ks1.uf10.hyoujiFlg)}'><td ></td></c:if>
									<c:if test="${ks1.heishu.hyoujiFlg && enableGaika}"><td></td></c:if>
									<c:if test="${ks1.rate.hyoujiFlg && enableGaika}"><td></td></c:if>
									<c:if test="${ks1.gaika.hyoujiFlg && enableGaika}"><td></td></c:if>
								</tr>
								<tr class="meisaiView4 pc_only">
									<c:if test='${ks1.tekiyou.hyoujiFlg}'><td ></td></c:if>
									<c:if test='${useKousaihi}'><td ></td></c:if>
									<c:if test='${useKousaihi}'><td align='right' style="word-break: normal;"></td></c:if>
									<c:if test='${useKousaihi}'><td align='right' style="word-break: normal;"></td></c:if>
								</tr>
	</c:if>
</c:forEach>
					</tbody>
				</table>
	</div>
<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A005" || denpyouKbn eq "A011" || denpyouKbn eq "A012"}'>
	<div class="control-group">
		<label class="control-label"><span class="required">*</span>小計</label>
		<div class="controls">
			<input name="shoukei03" class="input-medium autoNumericNoMax hissu" value="0" disabled="disabled" type="text">円
		<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A011"}'>
			<span class="invoiceOnly">
				<label class="label"><span class="required">*</span>${su:htmlEscape(ks.shouhizeigaku.name)}（10%）</label>
				<input name="10Percent03" class="input-medium autoNumericNoMax hissu" value="0" disabled="disabled" type="text">円
				<label class="label"><span class="required">*</span>${su:htmlEscape(ks.shouhizeigaku.name)}（8%）</label>
				<input name="8Percent03" class="input-medium autoNumericNoMax hissu" value="0" disabled="disabled" type="text">円
			</span>
		</c:if>
		</div>
	</div>
</c:if>
</section>

<!-- スクリプト -->
<script style='text/javascript'>

/**
 * 明細のhiddenに明細MAPを反映
 * ダイアログを明細部に反映する途中処理＞これの後の「setDisplayMeisaiData」で明細部を再描画する。
 * 明細の追加・変更時に setMeisaiInfo + setDisplayMeisaiDataが呼ばれる
 * @param index			行(0～)
 * @param meisaiInfo	明細情報MAP
 */
function setMeisaiInfo(index, meisaiInfo) {
	var meisai = $("#meisaiList tr.meisai:eq(" + index + ")");
	meisai.find("input[name=kaigaiFlg]").val(meisaiInfo["kaigaiFlg"]);
	meisai.find("input[name=userId]").val(meisaiInfo["userId"]);
	meisai.find("input[name=userName]").val(meisaiInfo["userName"]);
	meisai.find("input[name=shiyoubi]").val(meisaiInfo["shiyoubi"]);
	meisai.find("input[name=shouhyouShorui]").val(meisaiInfo["shouhyouShorui"]);
	meisai.find("input[name=shainNo]").val(meisaiInfo["shainNo"]);
	meisai.find("input[name=shiwakeEdaNo]").val(meisaiInfo["shiwakeEdaNo"]);
	meisai.find("input[name=torihikiName]").val(meisaiInfo["torihikiName"]);
	meisai.find("input[name=furikomisakiJouhou]").val(meisaiInfo["furikomisakiJouhou"]);
	meisai.find("input[name=kamokuCd]").val(meisaiInfo["kamokuCd"]);
	meisai.find("input[name=kamokuName]").val(meisaiInfo["kamokuName"]);
	meisai.find("input[name=kamokuEdabanCd]").val(meisaiInfo["kamokuEdabanCd"]);
	meisai.find("input[name=kamokuEdabanName]").val(meisaiInfo["kamokuEdabanName"]);
	meisai.find("input[name=futanBumonCd]").val(meisaiInfo["futanBumonCd"]);
	meisai.find("input[name=futanBumonName]").val(meisaiInfo["futanBumonName"]);
	meisai.find("input[name=torihikisakiCd]").val(meisaiInfo["torihikisakiCd"]),
	meisai.find("input[name=torihikisakiName]").val(meisaiInfo["torihikisakiName"]),
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
	meisai.find("select[name=zeiritsu]").val(meisaiInfo["zeiritsu"]);
	meisai.find("input[name=keigenZeiritsuKbn]").val(meisaiInfo["keigenZeiritsuKbn"]);
	meisai.find("input[name=heishuCd]").val(meisaiInfo["heishuCd"]);
	meisai.find("input[name=rate]").val(meisaiInfo["rate"]);
	meisai.find("input[name=gaika]").val(meisaiInfo["gaika"]);
	meisai.find("input[name=tani]").val(meisaiInfo["tani"]);
	meisai.find("input[name=shiharaiKingaku]").val(meisaiInfo["shiharaiKingaku"]);
	meisai.find("input[name=houjinCardFlgKeihi]").val(meisaiInfo["houjinCardFlgKeihi"]);
	meisai.find("input[name=kaishaTehaiFlgKeihi]").val(meisaiInfo["kaishaTehaiFlgKeihi"]);
	meisai.find("input[name=tekiyou]").val(meisaiInfo["tekiyou"]);
	meisai.find("input[name=chuuki2]").val(meisaiInfo["chuuki2"]);
	meisai.find("input[name=kousaihiShousai]").val(meisaiInfo["kousaihiShousai"]);
	meisai.find("input[name=kousaihiNinzuu]").val(meisaiInfo["kousaihiNinzuu"]);
	meisai.find("input[name=kousaihiHitoriKingaku]").val(meisaiInfo["kousaihiHitoriKingaku"]);
	meisai.find("input[name=kazeiFlg]").val(meisaiInfo["kazeiFlg"]);

	meisai.find("input[name=kamokuEdabanEnable]").val(meisaiInfo["kamokuEdabanEnable"]);
	meisai.find("input[name=futanBumonEnable]").val(meisaiInfo["futanBumonEnable"]);
	meisai.find("input[name=torihikisakiEnable]").val(meisaiInfo["torihikisakiEnable"]);
	meisai.find("input[name=kousaihiEnable]").val(meisaiInfo["kousaihiEnable"]);
	meisai.find("input[name=ninzuuEnable]").val(meisaiInfo["ninzuuEnable"]);
	meisai.find("input[name=projectEnable]").val(meisaiInfo["projectEnable"]);
	meisai.find("input[name=segmentEnable]").val(meisaiInfo["segmentEnable"]);
	meisai.find("input[name=zeiritsuEnable]").val(meisaiInfo["zeiritsuEnable"]);
	if($("input[name=denpyouKbn]").val() == 'A004' || $("input[name=denpyouKbn]").val() == 'A011')
	{
		meisai.find("input[name=jigyoushaKbnKeihi]").val(meisaiInfo["jigyoushaKbn"]);
		meisai.find("input[name=shiharaisakiNameKeihi]").val(meisaiInfo["shiharaisaki"]);
		meisai.find("select[name=bunriKbnKeihi]").val(meisaiInfo["bunriKbn"]);
		meisai.find("select[name=kariShiireKbnKeihi]").val(meisaiInfo["kariShiireKbn"]);
	}
	else
	{
		meisai.find("input[name=jigyoushaKbn]").val(meisaiInfo["jigyoushaKbn"]);
		meisai.find("input[name=shiharaisakiName]").val(meisaiInfo["shiharaisaki"]);
		meisai.find("select[name=bunriKbn]").val(meisaiInfo["bunriKbn"]);
		meisai.find("select[name=kariShiireKbn]").val(meisaiInfo["kariShiireKbn"]);
	}
	meisai.find("input[name=shoriGroup]").val(meisaiInfo["shoriGroup"]);
	
	if(meisai.find("select[name=zeiritsu] :selected").attr("data-keigenZeiritsuKbn") !== meisaiInfo["keigenZeiritsuKbn"]){
		//選択肢を選び直す
		meisai.find("select[name=zeiritsu] option").filter(function(index){
			var zeiritsuText = meisaiInfo["zeiritsu"];
			if("1" === meisaiInfo["keigenZeiritsuKbn"]) zeiritsuText = "*"+ meisaiInfo["zeiritsu"];
			return $(this).text() === zeiritsuText;
		}).prop('selected', true);
	}
	
	meisai.find("input[name=hontaiKingaku]").val(meisaiInfo["hontaiKingaku"]);
	meisai.find("input[name=shouhizeigaku]").val(meisaiInfo["shouhizeigaku"]);
	meisai.find("input[name=himodukeCardMeisaiKeihi]").val(meisaiInfo["himodukeCardMeisaiKeihi"]);
}

/**
 * 明細MAP取得
 * @param index	行(0～)
 */
function getMeisaiInfo(index) {
	var meisai = $("#meisaiList tr.meisai:eq(" + index + ")");
	
	// 一覧の最大インデックスを取得する
	var maxIndex = $("#meisaiList").find("tr.meisai").length - 1;

	let isRyohiSeisan = $("input[name=denpyouKbn]").val() == 'A004' || $("input[name=denpyouKbn]").val() == 'A011';

	//該当行の明細MAP返す
	return {
		denpyouId : $("input[name=denpyouId]").val(),
		denpyouKbn : $("input[name=denpyouKbn]").val(),
		zeroEnabled : $("input[name=karibaraiOn]:checked").length == 1 ? $("input[name=karibaraiOn]:checked").val() : "0",
		index : index,
		maxIndex : maxIndex,
		
		kaigaiFlg : meisai.find("input[name=kaigaiFlg]").val() == "1" ? "1" : "0",
		userId : $("input[name=denpyouKbn]").val() == 'A004' || $("input[name=denpyouKbn]").val() == 'A005' || $("input[name=denpyouKbn]").val() == 'A011' || $("input[name=denpyouKbn]").val() == 'A012' ? $("input[name=userIdRyohi]").val() : meisai.find("input[name=userId]").val(),
		userName : meisai.find("input[name=userName]").val(),
		shiyoubi : meisai.find("input[name=shiyoubi]").val(),
		shouhyouShorui : meisai.find("input[name=shouhyouShorui]").val(),
		shainNo : meisai.find("input[name=shainNo]").val(),
		shiwakeEdaNo : meisai.find("input[name=shiwakeEdaNo]").val(),
		torihikiName : meisai.find("input[name=torihikiName]").val(),
		furikomisakiJouhou : meisai.find("input[name=furikomisakiJouhou]").val(),
		kamokuCd : meisai.find("input[name=kamokuCd]").val(),
		kamokuName : meisai.find("input[name=kamokuName]").val(),
		kamokuEdabanCd : meisai.find("input[name=kamokuEdabanCd]").val(),
		kamokuEdabanName : meisai.find("input[name=kamokuEdabanName]").val(),
		futanBumonCd : meisai.find("input[name=futanBumonCd]").val(),
		futanBumonName : meisai.find("input[name=futanBumonName]").val(),
		torihikisakiCd : meisai.find("input[name=torihikisakiCd]").val(),
		torihikisakiName : meisai.find("input[name=torihikisakiName]").val(),
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
		heishuCd : meisai.find("input[name=heishuCd]").val(),
		rate : meisai.find("input[name=rate]").val(),
		gaika : meisai.find("input[name=gaika]").val(),
		tani : meisai.find("input[name=tani]").val(),
		shiharaiKingaku : meisai.find("input[name=shiharaiKingaku]").val(),
		houjinCardFlgKeihi : meisai.find("input[name=houjinCardFlgKeihi]").val(),
		kaishaTehaiFlgKeihi : meisai.find("input[name=kaishaTehaiFlgKeihi]").val(),
		tekiyou : meisai.find("input[name=tekiyou]").val(),
		jigyoushaKbn : meisai.find("input[name=jigyoushaKbn" + (isRyohiSeisan ? "Keihi" : "") + "]").val() == "1" ? "1" : "0",
		shiharaisaki : meisai.find("input[name=shiharaisakiName" + (isRyohiSeisan ? "Keihi" : "") + "]").val(),
		bunriKbn : meisai.find("select[name=bunriKbn" + (isRyohiSeisan ? "Keihi" : "") + "] :selected").val(),
		kariShiireKbn : meisai.find("select[name=kariShiireKbn" + (isRyohiSeisan ? "Keihi" : "") + "] :selected").val(),
		chuuki2	 : meisai.find("input[name=chuuki2]").val(),
		chuukiKousai2 : meisai.find("input[name=chuukiKousai2]").val(),
		kousaihiShousai : meisai.find("input[name=kousaihiShousai]").val(),
		kousaihiNinzuu : meisai.find("input[name=kousaihiNinzuu]").val(),
		kousaihiHitoriKingaku : meisai.find("input[name=kousaihiHitoriKingaku]").val(),
		kousaihiHyoujiFlg : meisai.find("input[name=kousaihiHyoujiFlg]").val(),
		ninzuuRiyouFlg : meisai.find("input[name=ninzuuRiyouFlg]").val(),
		kazeiFlg : meisai.find("input[name=kazeiFlg]").val(),
		enableInput : 'true' == $('#enableInput').val(),
		kamokuEdabanEnable : 'true' == meisai.find("input[name=kamokuEdabanEnable]").val(),
		futanBumonEnable : 'true' == meisai.find("input[name=futanBumonEnable]").val(),
		torihikisakiEnable : 'true' == meisai.find("input[name=torihikisakiEnable]").val(),
		kousaihiEnable : 'true' == meisai.find("input[name=kousaihiEnable]").val(),
		ninzuuEnable : 'true' == meisai.find("input[name=ninzuuEnable]").val(),
		projectEnable : 'true' == meisai.find("input[name=projectEnable]").val(),
		segmentEnable : 'true' == meisai.find("input[name=segmentEnable]").val(),
		zeiritsuEnable : 'true' == meisai.find("input[name=zeiritsuEnable]").val(),
		hontaiKingaku : meisai.find("input[name=hontaiKingaku]").val(),
		shouhizeigaku : meisai.find("input[name=shouhizeigaku]").val(),
		himodukeCardMeisaiKeihi : meisai.find("input[name=himodukeCardMeisaiKeihi]").val(),
		shoriGroup : meisai.find("input[name=shoriGroup]").val(),
		
		//以下計算用
		hontaiKingakuNum : meisai.find("input[name=hontaiKingaku]").getMoney(),
		shouhizeigakuNum : meisai.find("input[name=shouhizeigaku]").getMoney()
	};
}

/**
 * 明細行数を取得
 * 新規起票後の非表示(取引未選択)の明細はカウントしない
 * @return	明細行数
 */
function getEnableMeisaiCount() {
	var count = 0;
	$.each($("#meisaiList input[name=shiwakeEdaNo]"), function(ii, obj) {
		if ('' != $(obj).val()) {
			count++;
		}else if('' != $(obj).parent().find("input[name=himodukeCardMeisaiKeihi]").val()){
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
	return $.map($("#meisaiList tr.meisai"), function(tr, ii) {
		var meisaiInfo = getMeisaiInfo(ii);
		if ('' == meisaiInfo['shiwakeEdaNo'] && '' == meisaiInfo['himodukeCardMeisaiKeihi'] ) {
			return null;
		}
		return meisaiInfo;
	});
}

/**
 * テーブルのレイアウトを調整します。主にセル結合を調整します。
 * インボイス対応によりセルが増えます
 */
function setMeisaiTableLayout() {
	  // 最大のthタグの数を格納する変数
	var maxThCount = 0;
	
	  // 各行のthタグの数を格納する配列
	var thCounts = [];
	  
	var useKousaihi = <c:out value='${useKousaihi}'/>;
	var headMinusLength = 2;
<c:if test="${houjinCardFlag || kaishaTehaiFlag}">
	// 法人カード利用カラムがある時+1
	headMinusLength ++;
</c:if>

	  
	  // すべてのthead内のtr要素を取得
	  $("#meisaiTableDiv").find("table.table-bordered.table-condensed thead tr").each(function(index) {
	    // style属性が"display:none"でないthタグを取得
	    var thElements = $(this).find("th:not([style*='display:none'])");
	
	    // colspanとrowspanを考慮したthタグの数をカウント
	    var thCount = index == 0 ? -headMinusLength : 0; // Noとコピーetc用のセルは、最初の行の全行に影響するので無視
	    thElements.each(function() {
	      var colspan = $(this).attr("colspan") ? parseInt($(this).attr("colspan")) : 1;
	      thCount += colspan;
	    });
	
	    // 現在の行のthタグの数を配列に格納
	    thCounts.push(thCount);
	  });
	
	  maxThCount = Math.max(...thCounts);
	//摘要・交際費に割り当てる列数を計算
	var head5Col1Count = maxThCount;//摘要のみで専有
	var head5Col2Count = 0;
	if(useKousaihi){
		head5Col1Count = Math.ceil((maxThCount - 2) / 2);//4項目で専有、人数,金額は固定1列でいいので、余ったら摘要と交際費詳細で使い切る
		head5Col2Count = maxThCount - 2 - head5Col1Count;
	}
	  
	  // 最大のthタグの数に一致するように、セルを追加
	  $("#meisaiTableDiv").find("table.table-bordered.table-condensed thead tr").each(function(index) {
		//5段目の調整
		if(thCounts.length == index + 1){
			//ヘッダー
			if (useKousaihi) {
				$(this).find('th:eq(0)').attr('colspan', head5Col1Count);
				$(this).find('th:eq(1)').attr('colspan', head5Col2Count);
			} else {
				$(this).find('th:eq(0)').attr('colspan', head5Col1Count+head5Col2Count);
			}
			//明細
			if (useKousaihi) {
				$('.meisaiView' + index).find('td:eq(0)').attr('colspan', head5Col1Count);
				$('.meisaiView' + index).find('td:eq(1)').css("display", "");
				$('.meisaiView' + index).find('td:eq(1)').attr('colspan', head5Col2Count);
			} else {
				$('.meisaiView' + index).find('td:eq(0)').attr('colspan', head5Col1Count + head5Col2Count );
				$('.meisaiView' + index).find('td:eq(1)').css("display", "none");
			}
			thCounts[index] = 0;
		}

	    // 最大数と現在のthタグの数との差分を計算
		var diff = maxThCount - thCounts[index];

	    // 差分の数だけセルを追加。完全不可視の行は無視
		for (var i = 0; thCounts[index] > 0 && i < diff; i++) {
		    if(index == 0)
		    {
		    	$(this).find("#headth").after("<th class='addColumn pc_only'></th>");
		    	$('.meisai').find("#headtd").after("<td class='addColumn pc_only'></td>");
		    }
		    else{
				if($("#denpyouJouhou").find("select[name=invoiceDenpyou]").val() == "1" && $("input[name=denpyouKbn]").val() == 'A001' && index == 1 && i == 0){
					$(this).find('th:eq(0)').attr('colspan', 2);
					$('.meisaiView' + index).find('td:eq(0)').attr('colspan', 2);
					continue;
				}
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
		var v2Index = 0;
		var v3Index = 0;
		var v4Index = 0;
		var meisai = $(obj);
		var view = $(obj).next();           // meisaiView1
		var view2 = view.next();            // meisaiView2
		var view3 = view2.next();
		var view4 = view3.next();
		
		if ('' == meisai.find("input[name=shiwakeEdaNo]").val() && '' == meisai.find("input[name=himodukeCardMeisaiKeihi]").val()) {
			// 未設定扱い
			return ;
		}
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
		v2Index = v2Index + view2.find(".addColumn").length;
		v3Index = v3Index + view3.find(".addColumn").length;
		v4Index = v4Index + view4.find(".addColumn").length;
		
		// 出張区分
		var kaigaiFlg = "";
<c:if test='${("A011" eq denpyouKbn || "A012" eq denpyouKbn)}'>
<c:if test='${ks1.shucchouKbn.hyoujiFlg}'>
		kaigaiFlg = meisai.find("input[name=kaigaiFlg]").val();
		var kaigaiFlgStr = "　";
		if (kaigaiFlg == "1") {
			kaigaiFlgStr = "海外";
		} else {
			kaigaiFlgStr = "国内"
		}
		meisai.find("td:eq(" + index++ + ")").text(kaigaiFlgStr);
</c:if>
</c:if>
		
		// 使用者
<c:if test='${"A001" eq denpyouKbn}'>
<c:if test='${ks1.userName.hyoujiFlg}'>
		var userNameStr = meisai.find("input[name=userName]").val();
		if (userNameStr == "") {
			userNameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(userNameStr);
</c:if>
</c:if>
		
		// 使用日
<c:if test='${ks1.shiyoubi.hyoujiFlg}'>
		var shiyoubiStr = meisai.find("input[name=shiyoubi]").val();
		if (shiyoubiStr == "") {
			shiyoubiStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(shiyoubiStr);
</c:if>
		
		// 領収書・請求書等(証憑)
<c:if test='${"A001" eq denpyouKbn || "A004" eq denpyouKbn || "A011" eq denpyouKbn}'>
<c:if test='${ks1.shouhyouShoruiFlg.hyoujiFlg}'>
		var shouhyouShoruiStr = (meisai.find("input[name=shouhyouShorui]").val() == "1") ? "○" : "　";
		meisai.find("td:eq(" + index++ + ")").text(shouhyouShoruiStr);
</c:if>
</c:if>
		
		// 取引先
<c:if test='${ks1.torihiki.hyoujiFlg}'>
		var torihikiNameStr = meisai.find("input[name=torihikiName]").val();
		if (torihikiNameStr == "") {
			torihikiNameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(torihikiNameStr);
</c:if>

		// 科目
<c:if test='${ks1.kamoku.hyoujiFlg}'>
		var kamokuNameStr = meisai.find("input[name=kamokuName]").val();
		if (kamokuNameStr == "") {
			kamokuNameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(kamokuNameStr);
</c:if>

		// 科目枝番
<c:if test='${ks1.kamokuEdaban.hyoujiFlg}'>
		var kamokuEdabanNameStr = meisai.find("input[name=kamokuEdabanName]").val();
		if (kamokuEdabanNameStr == "") {
			kamokuEdabanNameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(kamokuEdabanNameStr);
</c:if>

		// 負担部門
<c:if test='${ks1.futanBumon.hyoujiFlg}'>
		var futanBumonNameStr = meisai.find("input[name=futanBumonName]").val();
		if (futanBumonNameStr == "") {
			futanBumonNameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(futanBumonNameStr);
</c:if>

		// 取引先
<c:if test='${ks1.torihikisaki.hyoujiFlg}'>
		var torihikisakiNameStr = meisai.find("input[name=torihikisakiName]").val();
		if (torihikisakiNameStr == "") {
			torihikisakiNameStr = "　";
		}
		
		var torihikisakiAndFurikomiStr = torihikisakiNameStr;

		// 振込先
<c:if test='${ks1.furikomisakiJouhou.hyoujiFlg}'>
		var furikomisakiNameStr = meisai.find("input[name=furikomisakiJouhou]").val();
		if (furikomisakiNameStr == "") {
			furikomisakiNameStr = "　";
		}
		torihikisakiAndFurikomiStr = torihikisakiNameStr + "<br>" + furikomisakiNameStr;
</c:if>

		meisai.find("td:eq(" + index++ + ")").html(torihikisakiAndFurikomiStr);
</c:if>


		// 以下、meisaiには携帯表示用に値詰め
//支払先名・事業者区分・税抜金額・消費税額はインボイス対応項目
//分離区分・仕入区分はインボイス対応前伝票でも表示する

let isRyohiSeisan = $("input[name=denpyouKbn]").val() == 'A004' || $("input[name=denpyouKbn]").val() == 'A011';
//支払先名
<c:if test='${ks1.shiharaisaki.hyoujiFlg}'>
		var shiharaisakiStr = meisai.find("input[name=shiharaisakiName" + (isRyohiSeisan ? "Keihi" : "") + "]").val();
		if (shiharaisakiStr == "") {
			shiharaisakiStr = "　";
		}
		shiharaisakiStr = '<span class="invoiceOnly">' + shiharaisakiStr + '</span>';
		meisai.find("td:eq(" + index++ + ")").html(shiharaisakiStr);
		view.find("td:eq(" + vIndex++ + ")").html(shiharaisakiStr);
</c:if>
//事業者区分
<c:if test='${ks1.jigyoushaKbn.hyoujiFlg}'>
		var jigyoushaKbnStr = meisai.find("input[name=jigyoushaKbn" + (isRyohiSeisan ? "Keihi" : "") + "]").val();
		var jigyoushaKbnText = '<span class="invoiceOnly">' + (jigyoushaKbnStr == "1" ? "免税80%" : "通常課税") + '</span>';
		meisai.find("td:eq(" + index++ + ")").html(jigyoushaKbnText);
		view.find("td:eq(" + vIndex++ + ")").html(jigyoushaKbnText);
</c:if>
//課税区分
<c:if test='${ks1.kazeiKbn.hyoujiFlg}'>
		var kazeiKbnStr = meisai.find("select[name=kazeiKbn] :selected").text();
		if (kazeiKbnStr == "") {
			kazeiKbnStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(kazeiKbnStr);
		view.find("td:eq(" + vIndex++ + ")").text(kazeiKbnStr);
</c:if>

		//消費税率
<c:if test='${ks1.zeiritsu.hyoujiFlg}'>
		var kazeiKbnGroup = meisai.find("select[name=kazeiKbn] option:selected").attr("data-kazeiKbnGroup");
		var zeiritsuStr;
		if ((kazeiKbnGroup == "1" || kazeiKbnGroup == "2") && kaigaiFlg != "1") {
			zeiritsuStr = meisai.find("select[name=zeiritsu] :selected").text() + "%";
		} else {
			zeiritsuStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(zeiritsuStr);
		view.find("td:eq(" + vIndex++ + ")").text(zeiritsuStr);
</c:if>

		//分離区分
<c:if test='${ks1.bunriKbn.hyoujiFlg}'>
		var bunriKbnStr = meisai.find("select[name=bunriKbn" + (isRyohiSeisan ? "Keihi" : "") + "] :selected").text();
		if (bunriKbnStr == "") {
			bunriKbnStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(bunriKbnStr);
		view.find("td:eq(" + vIndex++ + ")").text(bunriKbnStr);
</c:if>

		//仕入区分
<c:if test='${ks1.shiireKbn.hyoujiFlg and shiireZeiAnbun eq "1"}'>
		var shiireKbnStr = meisai.find("select[name=kariShiireKbn" + (isRyohiSeisan ? "Keihi" : "") + "] :selected").text();
		if (shiireKbnStr == "") {
			shiireKbnStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(shiireKbnStr);
		view.find("td:eq(" + vIndex++ + ")").text(shiireKbnStr);
</c:if>

// 支払金額・課税区分
<c:if test='${ks1.shiharaiKingaku.hyoujiFlg}'>
		var shiharaiKingakuStr = meisai.find("input[name=shiharaiKingaku]").val() + ' 円';
		<c:if test='${not ks1.shiharaiKingaku.hyoujiFlg}'>shiharaiKingakuStr = "";</c:if>
		if (shiharaiKingakuStr == "") {
			shiharaiKingakuStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(shiharaiKingakuStr);
		view.find("td:eq(" + vIndex++ + ")").text(shiharaiKingakuStr);
		</c:if>
		
//税抜金額
<c:if test='${ks1.zeinukiKingaku.hyoujiFlg}'>
		var hontaiKingakuStr = meisai.find("input[name=hontaiKingaku]").val() + ' 円';
		<c:if test='${not ks1.zeinukiKingaku.hyoujiFlg}'>hontaiKingakuStr = "";</c:if>
		if (hontaiKingakuStr == "") {
			hontaiKingakuStr = "　";
		}
		hontaiKigakuText = '<span class="invoiceOnly">' + hontaiKingakuStr + '</span>';
		meisai.find("td:eq(" + index++ + ")").html(hontaiKigakuText);
		view.find("td:eq(" + vIndex++ + ")").html(hontaiKigakuText);
</c:if>
//消費税額
<c:if test='${ks1.shouhizeigaku.hyoujiFlg}'>
		var shouhizeigakuStr = meisai.find("input[name=shouhizeigaku]").val() + ' 円';
		<c:if test='${not ks1.shouhizeigaku.hyoujiFlg}'>shouhizeigakuStr = "";</c:if>
		if (shouhizeigakuStr == "") {
			shouhizeigakuStr = "　";
		}
		shouhizeigakuText = '<span class="invoiceOnly">' + shouhizeigakuStr + '</span>';
		meisai.find("td:eq(" + index++ + ")").html(shouhizeigakuText);
		view.find("td:eq(" + vIndex++ + ")").html(shouhizeigakuText);
</c:if>
//プロジェクト
<c:if test='${"0" ne pjShiyouFlg and ks1.project.hyoujiFlg}'>
		var projectNameStr = meisai.find("input[name=projectName]").val();
		if (projectNameStr == "") {
			projectNameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(projectNameStr);
		view2.find("td:eq(" + v2Index++ + ")").text(projectNameStr);
</c:if>
		
		//セグメント
<c:if test='${"0" ne segmentShiyouFlg and ks1.segment.hyoujiFlg}'>
		var segmentNameStr = meisai.find("input[name=segmentName]").val();
		if (segmentNameStr == "") {
			segmentNameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(segmentNameStr);
		view2.find("td:eq(" + v2Index++ + ")").text(segmentNameStr);
</c:if>

		// UF1
<c:if test='${"1" eq hfUfSeigyo.uf1ShiyouFlg}'>
<c:if test='${ks1.uf1.hyoujiFlg}'>
		var uf1CdStr = meisai.find("input[name=uf1Cd]").val();
		if (uf1CdStr == "") {
			uf1CdStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf1CdStr);
		view2.find("td:eq(" + v2Index++ + ")").text(uf1CdStr);
</c:if>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf1ShiyouFlg or "3" eq hfUfSeigyo.uf1ShiyouFlg}'>
<c:if test='${ks1.uf1.hyoujiFlg}'>
		var uf1NameStr = meisai.find("input[name=uf1Name]").val();
		if (uf1NameStr == "") {
			uf1NameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf1NameStr);
		view2.find("td:eq(" + v2Index++ + ")").text(uf1NameStr);
</c:if>
</c:if>
		// UF2
<c:if test='${"1" eq hfUfSeigyo.uf2ShiyouFlg}'>
<c:if test='${ks1.uf2.hyoujiFlg}'>
		var uf2CdStr = meisai.find("input[name=uf2Cd]").val();
		if (uf2CdStr == "") {
			uf2CdStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf2CdStr);
		view2.find("td:eq(" + v2Index++ + ")").text(uf2CdStr);
</c:if>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf2ShiyouFlg or "3" eq hfUfSeigyo.uf2ShiyouFlg}'>
<c:if test='${ks1.uf2.hyoujiFlg}'>
		var uf2NameStr = meisai.find("input[name=uf2Name]").val();
		if (uf2NameStr == "") {
			uf2NameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf2NameStr);
		view2.find("td:eq(" + v2Index++ + ")").text(uf2NameStr);
</c:if>
</c:if>
		// UF3
<c:if test='${"1" eq hfUfSeigyo.uf3ShiyouFlg}'>
<c:if test='${ks1.uf3.hyoujiFlg}'>
		var uf3CdStr = meisai.find("input[name=uf3Cd]").val();
		if (uf3CdStr == "") {
			uf3CdStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf3CdStr);
		view2.find("td:eq(" + v2Index++ + ")").text(uf3CdStr);
</c:if>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf3ShiyouFlg or "3" eq hfUfSeigyo.uf3ShiyouFlg}'>
<c:if test='${ks1.uf3.hyoujiFlg}'>
		var uf3NameStr = meisai.find("input[name=uf3Name]").val();
		if (uf3NameStr == "") {
			uf3NameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf3NameStr);
		view2.find("td:eq(" + v2Index++ + ")").text(uf3NameStr);
</c:if>
</c:if>
		// UF4
<c:if test='${"1" eq hfUfSeigyo.uf4ShiyouFlg}'>
<c:if test='${ks1.uf4.hyoujiFlg}'>
		var uf4CdStr = meisai.find("input[name=uf4Cd]").val();
		if (uf4CdStr == "") {
			uf4CdStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf4CdStr);
		view2.find("td:eq(" + v2Index++ + ")").text(uf4CdStr);
</c:if>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf4ShiyouFlg or "3" eq hfUfSeigyo.uf4ShiyouFlg}'>
<c:if test='${ks1.uf4.hyoujiFlg}'>
		var uf4NameStr = meisai.find("input[name=uf4Name]").val();
		if (uf4NameStr == "") {
			uf4NameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf4NameStr);
		view2.find("td:eq(" + v2Index++ + ")").text(uf4NameStr);
</c:if>
</c:if>
		// UF5
<c:if test='${"1" eq hfUfSeigyo.uf5ShiyouFlg}'>
<c:if test='${ks1.uf5.hyoujiFlg}'>
		var uf5CdStr = meisai.find("input[name=uf5Cd]").val();
		if (uf5CdStr == "") {
			uf5CdStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf5CdStr);
		view2.find("td:eq(" + v2Index++ + ")").text(uf5CdStr);
</c:if>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf5ShiyouFlg or "3" eq hfUfSeigyo.uf5ShiyouFlg}'>
<c:if test='${ks1.uf5.hyoujiFlg}'>
		var uf5NameStr = meisai.find("input[name=uf5Name]").val();
		if (uf5NameStr == "") {
			uf5NameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf5NameStr);
		view2.find("td:eq(" + v2Index++ + ")").text(uf5NameStr);
</c:if>
</c:if>
		// UF6
<c:if test='${"1" eq hfUfSeigyo.uf6ShiyouFlg}'>
<c:if test='${ks1.uf6.hyoujiFlg}'>
		var uf6CdStr = meisai.find("input[name=uf6Cd]").val();
		if (uf6CdStr == "") {
			uf6CdStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf6CdStr);
		view2.find("td:eq(" + v2Index++ + ")").text(uf6CdStr);
</c:if>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf6ShiyouFlg or "3" eq hfUfSeigyo.uf6ShiyouFlg}'>
<c:if test='${ks1.uf6.hyoujiFlg}'>
		var uf6NameStr = meisai.find("input[name=uf6Name]").val();
		if (uf6NameStr == "") {
			uf6NameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf6NameStr);
		view2.find("td:eq(" + v2Index++ + ")").text(uf6NameStr);
</c:if>
</c:if>
		// UF7
<c:if test='${"1" eq hfUfSeigyo.uf7ShiyouFlg}'>
<c:if test='${ks1.uf7.hyoujiFlg}'>
		var uf7CdStr = meisai.find("input[name=uf7Cd]").val();
		if (uf7CdStr == "") {
			uf7CdStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf7CdStr);
		view3.find("td:eq(" + v3Index++ + ")").text(uf7CdStr);
</c:if>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf7ShiyouFlg or "3" eq hfUfSeigyo.uf7ShiyouFlg}'>
<c:if test='${ks1.uf7.hyoujiFlg}'>
		var uf7NameStr = meisai.find("input[name=uf7Name]").val();
		if (uf7NameStr == "") {
			uf7NameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf7NameStr);
		view3.find("td:eq(" + v3Index++ + ")").text(uf7NameStr);
</c:if>
</c:if>
		// UF8
<c:if test='${"1" eq hfUfSeigyo.uf8ShiyouFlg}'>
<c:if test='${ks1.uf8.hyoujiFlg}'>
		var uf8CdStr = meisai.find("input[name=uf8Cd]").val();
		if (uf8CdStr == "") {
			uf8CdStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf8CdStr);
		view3.find("td:eq(" + v3Index++ + ")").text(uf8CdStr);
</c:if>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf8ShiyouFlg or "3" eq hfUfSeigyo.uf8ShiyouFlg}'>
<c:if test='${ks1.uf8.hyoujiFlg}'>
		var uf8NameStr = meisai.find("input[name=uf8Name]").val();
		if (uf8NameStr == "") {
			uf8NameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf8NameStr);
		view3.find("td:eq(" + v3Index++ + ")").text(uf8NameStr);
</c:if>
</c:if>
		// UF9
<c:if test='${"1" eq hfUfSeigyo.uf9ShiyouFlg}'>
<c:if test='${ks1.uf9.hyoujiFlg}'>
		var uf9CdStr = meisai.find("input[name=uf9Cd]").val();
		if (uf9CdStr == "") {
			uf9CdStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf9CdStr);
		view3.find("td:eq(" + v3Index++ + ")").text(uf9CdStr);
</c:if>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf9ShiyouFlg or "3" eq hfUfSeigyo.uf9ShiyouFlg}'>
<c:if test='${ks1.uf9.hyoujiFlg}'>
		var uf9NameStr = meisai.find("input[name=uf9Name]").val();
		if (uf9NameStr == "") {
			uf9NameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf9NameStr);
		view3.find("td:eq(" + v3Index++ + ")").text(uf9NameStr);
</c:if>
</c:if>
		// UF10
<c:if test='${"1" eq hfUfSeigyo.uf10ShiyouFlg}'>
<c:if test='${ks1.uf10.hyoujiFlg}'>
		var uf10CdStr = meisai.find("input[name=uf10Cd]").val();
		if (uf10CdStr == "") {
			uf10CdStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf10CdStr);
		view3.find("td:eq(" + v3Index++ + ")").text(uf10CdStr);
</c:if>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf10ShiyouFlg or "3" eq hfUfSeigyo.uf10ShiyouFlg}'>
<c:if test='${ks1.uf10.hyoujiFlg}'>
		var uf10NameStr = meisai.find("input[name=uf10Name]").val();
		if (uf10NameStr == "") {
			uf10NameStr = "　";
		}
		meisai.find("td:eq(" + index++ + ")").text(uf10NameStr);
		view3.find("td:eq(" + v3Index++ + ")").text(uf10NameStr);
</c:if>
</c:if>


		//幣種
<c:if test="${ks1.heishu.hyoujiFlg && enableGaika}">
		var heishuStr = "　";
		if ("" != meisai.find("input[name=heishuCd]").val()) {
			heishuStr = meisai.find("input[name=heishuCd]").val();
		}
		meisai.find("td:eq(" + index++ + ")").text(heishuStr);
		view3.find("td:eq(" + v3Index++ + ")").text(heishuStr);
</c:if>

		//レート
<c:if test="${ks1.rate.hyoujiFlg && enableGaika}">
		var rateStr = "　";
		if ("" != meisai.find("input[name=rate]").val()) {
			rateStr = meisai.find("input[name=rate]").val();
		}
		meisai.find("td:eq(" + index++ + ")").text(rateStr);
		view3.find("td:eq(" + v3Index++ + ")").text(rateStr);
</c:if>

		//外貨
<c:if test="${ks1.gaika.hyoujiFlg && enableGaika}">
		var gaikaStr = "　";
		if ("" != meisai.find("input[name=gaika]").val()) {
			gaikaStr = meisai.find("input[name=gaika]").val() + meisai.find("input[name=tani]").val();
		}
		meisai.find("td:eq(" + index++ + ")").text(gaikaStr);
		view3.find("td:eq(" + v3Index++ + ")").text(gaikaStr);
</c:if>

		// 摘要
<c:if test='${ks1.tekiyou.hyoujiFlg}'>
		var tekiyouStr = meisai.find("input[name=tekiyou]").val();
		meisai.find("td:eq(" + index++ + ")").html(tekiyouStr + "<br><color>" + meisai.find("input[name=chuuki2]").val() + "</color>");
		view4.find("td:eq(0)").html(tekiyouStr + "<br><color>" + meisai.find("input[name=chuuki2]").val() + "</color>");
		$(function(){
		  $("color").css("color","red");
		});
</c:if>

		// 交際費詳細
		var kousaihiShousaiStr = meisai.find("input[name=kousaihiShousai]").val();
		if (kousaihiShousaiStr == "") {
			kousaihiShousaiStr = "　";
		}
		var kousaihiNinzuuStr = meisai.find("input[name=kousaihiNinzuu]").val();
		if (kousaihiNinzuuStr == "") {
			kousaihiNinzuuStr = "　";
		}
		var kousaihiHitoriKingakuStr = meisai.find("input[name=kousaihiHitoriKingaku]").val();
		if (kousaihiHitoriKingakuStr == "") {
			kousaihiHitoriKingakuStr = "　";
		}
		
<c:if test='${useKousaihi}'>
		if ('true' == meisai.find("input[name=kousaihiEnable]").val()) {
			var kousaihiBlankFlg = (kousaihiShousaiStr == "" || kousaihiShousaiStr == "　") ? true : false;
			meisai.find("td:eq(" + index++ + ")").html( (kousaihiBlankFlg ? "<color>" : kousaihiShousaiStr + "<br><color>") + meisai.find("input[name=chuukiKousai2]").val() + "</color>");
			view4.find("td:eq(1)").html( (kousaihiBlankFlg ? "<color>" : kousaihiShousaiStr + "<br><color>") + meisai.find("input[name=chuukiKousai2]").val() + "</color>");
			$(function(){
				$("color").css("color","red");
			});
		} else {
			meisai.find("td:eq(" + index++ + ")").text('　');
			view4.find("td:eq(1)").text('　');
		}
		
		if ('true' == meisai.find("input[name=ninzuuEnable]").val()) {
			meisai.find("td:eq(" + index++ + ")").text(kousaihiNinzuuStr + "名");
			meisai.find("td:eq(" + index++ + ")").text(kousaihiHitoriKingakuStr + " 円");
			view4.find("td:eq(2)").text(kousaihiNinzuuStr + "名");
			view4.find("td:eq(3)").text(kousaihiHitoriKingakuStr + " 円");
		}else{
			meisai.find("td:eq(" + index++ + ")").text('　');
			meisai.find("td:eq(" + index++ + ")").text('　');
			view4.find("td:eq(2)").text('　');
			view4.find("td:eq(3)").text('　');
		}
		
</c:if>
		

		
		//右欄外に表示
		//法人カード利用
		if (meisai.find("input[name=houjinCardFlgKeihi]").val() == '1') {
			meisai.find("td.rangai_col").html("<b>C</b>");
		//会社手配
		} else if (meisai.find("input[name=kaishaTehaiFlgKeihi]").val() == '1') {
			meisai.find("td.rangai_col").html("<b>K</b>");
		//それ以外
		} else {
			meisai.find("td.rangai_col").html("");
		}
		
		//法人カード使用履歴明細ならコピーさせない
		if(meisai.find("input[name=himodukeCardMeisaiKeihi]").val() != ""){
			meisai.find("[name=meisaiCopy]").hide();
		}else{
			meisai.find("[name=meisaiCopy]").show();
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
		//取引枝番未選択時(法人カード履歴参照時に発生)はコピーさせない
		if(meisaiInfo["shiwakeEdaNo"] == ""){
			alert("取引未設定の経費明細はコピーできません。")
			return;
		}
		meisaiInfo["himodukeCardMeisaiKeihi"] = "";		//法人カード履歴紐付リセット
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
 * 明細全削除Function
 */
function meisaiAllDelete() {
	$.each($("#meisaiList tr.meisai"), function (ii, tr) {
		if(getEnableMeisaiCount() == 0){
			// 明細がない場合は処理なし。
		}else{
			if (getEnableMeisaiCount() == 1) {
				meisaiClear($(tr));
			} else {
				for (var ii = 0; ii < parseInt($(tr).find('td.meisai_control').attr('rowspan')) - 1; ii++) {
					$(tr).next().remove();
				}
				$(tr).closest("tr.meisai").remove();
			}
			setDisplayMeisaiData();
			isDirty = true;
		}
	});
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
	const identificationKey = "eteam." + $("input[name=denpyouKbn]").val() + "-denpyouMeisai";
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
			"denpyou_meisai_confirm",
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
					meisaiDialogOpen(-1, meisaiInfo);
				}
				if (type == "insertNextClear") {
					//連続追加(クリア)ボタン：明細を追加、ダイアログをもう一度開く
					meisaiAdd(meisaiInfo);
					meisaiDialogOpen(-1, null);
				}
				updateDenpyouMeisaiEvent(meisaiInfo);
				changeInvoiceDenpyou()
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
	var width = "900";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	
	var meisai = $("#meisaiList tr.meisai:eq(" + index + ")");
	var view = $("#meisaiList tr.meisaiView1:eq(" + index + ")");
	var view2 = $("#meisaiList tr.meisaiView2:eq(" + index + ")");

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
				denpyouId:		$("input[name=denpyouId]").val(),
				denpyouKbn:		$("input[name=denpyouKbn]").val(),
				zeroEnabled:	$("input[name=karibaraiOn]:checked").length == 1 ? $("input[name=karibaraiOn]:checked").val() : "0",
				userId:			$("input[name=denpyouKbn]").val() == 'A004' || $("input[name=denpyouKbn]").val() == 'A005' || $("input[name=denpyouKbn]").val() == 'A011' || $("input[name=denpyouKbn]").val() == 'A012' ? $("input[name=userIdRyohi]").val() : ""
			}
			//既存明細を開く場合(明細番号・前・次)や複写追加の場合は明細情報全部渡してinitイベント
			:meisaiInfo;
	
	//ダイアログに明細初期表示イベントをロード(ロード直後は空)
	//ロード後に明細MAPをダイアログに反映
	dialog.load(
		"denpyou_meisai_init",
		actionParams,
		function() {
			setDialogMeisaiInfo(isEnableInput, meisaiInfo);
		});
}

/**
 * 金額の再計算処理
 */
function calcMeisaiMoney() {
	$.each($("#meisaiList tr.meisai"), function (ii, tr) {
	 	let meisai = $(tr);

		//諸々値を取り出して
		let kazeiKbnGroup		= meisai.find("select[name=kazeiKbn] :selected").attr("data-kazeiKbnGroup");
		let zeiritsu = kazeiKbnGroup == "1" ? Number(meisai.find("select[name=zeiritsu]").find("option:selected").val()) : 0;
		let hontaiKingakuTag	= meisai.find("input[name=hontaiKingaku]");
		let shouhizeigakuTag	= meisai.find("input[name=shouhizeigaku]");
		let shiharaiKingaku		= meisai.find("input[name=shiharaiKingaku]").getMoney();
		var jigyoushaFlg = 0;
		if($("input[name=denpyouKbn]").val() == 'A004' || $("input[name=denpyouKbn]").val() == 'A011')
		{
			jigyoushaFlg = meisai.find("input[name=jigyoushaKbnKeihi]").val() > 0 ? meisai.find("input[name=jigyoushaKbnKeihi]").val() : "0";
		}else{
			jigyoushaFlg = meisai.find("input[name=jigyoushaKbn]").val() > 0 ? meisai.find("input[name=jigyoushaKbn]").val() : "0";
		}
		let shiireKeikaSothiFlg = $("#workflowForm").find("input[name=shiirezeigakuKeikasothi]").val();
		let hasuuShoriFlg = $("#workflowForm").find("input[name=hasuuShoriFlg]").val();

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

		//明細の支払金額から本体金額、消費税額を計算
		let shouhizeigaku = hasuuKeisanZaimuFromImporter(dShiharaiKingaku.mul(dZeiritsu).div(dBunbo).mul(dJigyoushaNum), hasuuShoriFlg, false);
		let hontaiKingaku	= dShiharaiKingaku.sub(shouhizeigaku);

		//計算した本体金額、消費税額の値をセット or クリア
		hontaiKingakuTag.putMoney(hontaiKingaku);
		shouhizeigakuTag.putMoney(shouhizeigaku);
	});
}

//初期化処理
if ($('#enableInput').val()) {
	
	//明細追加ボタン
	$("#meisaiAddButton3").click(function(){
		meisaiDialogOpen(-1, null);
	});

	//明細イベント割り付け
	$("body").on("click","button[name=meisaiUp]",meisaiUp); 
	$("body").on("click","button[name=meisaiDown]",meisaiDown); 
	$("body").on("click","button[name=meisaiDelete]",meisaiDelete); 
	$("body").on("click","button[name=meisaiCopy]",meisaiCopy); 
}
</script>
