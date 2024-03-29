<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>


<c:set var="flg03" value="0" />
<c:set var="flg04" value="0" />
<c:if test='${0 < fn:length(shubetsuCd)}'><c:forEach var="i" begin="0" end="${fn:length(shubetsuCd) - 1}" step="1">
<c:if test='${shubetsuCd[i] eq "1" and kaigaiFlgRyohi[i] eq "1"}'><c:set var="flg03" value="1" /></c:if>
<c:if test='${shubetsuCd[i] eq "2" and kaigaiFlgRyohi[i] eq "1"}'><c:set var="flg04" value="1" /></c:if>
</c:forEach></c:if>

<section id='meisaiKaigaiRyohiField' class='print-unit'>

<c:choose>
<c:when test='${enableNittou}'>
	<h2>明細（交通費）</h2>
</c:when>
<c:otherwise>
	<h2>明細</h2>
</c:otherwise>
</c:choose>	
	<div>
		<input type='hidden' id="enableInput"		value='${su:htmlEscape(enableInput)}'>
		<button type='button' id='kaigaiMeisaiAddButton' class='btn btn-small'>明細追加</button>
	</div>
	<div id='kaigaiMeisaiTableDiv01' class='no-more-tables' style='display:none'>
				<table class='table-bordered table-condensed' style='margin-bottom:5px; border-collapse: collapse; word-break: break-all; word-wrap: break-word;'>
					<thead>
						<tr>
							<th style='min-width:30px;'>No</th>
							<c:if test='${ks.shubetsu1.hyoujiFlg}'><th style="min-width:70px;">${su:htmlEscape(ks.shubetsu1.name)}</th></c:if>
							<c:if test='${ks.shubetsu2.hyoujiFlg}'><th style="min-width:70px;"><nobr>${su:htmlEscape(ks.shubetsu2.name)}</nobr></th></c:if>
							<c:if test='${ks.koutsuuShudan.hyoujiFlg}'><th style="min-width:70px;"><nobr>${su:htmlEscape(ks.koutsuuShudan.name)}</nobr></th></c:if>
							<c:if test='${denpyouKbn ne "A005" and denpyouKbn ne "A012"}'><th><nobr>証憑</nobr></th></c:if>
							<c:if test='${ks.kikan.hyoujiFlg}'><th><nobr>${su:htmlEscape(ks.kikan.name)}</nobr></th></c:if>
							<c:if test="${ks.naiyouKoutsuuhi.hyoujiFlg or ks.bikouKoutsuuhi.hyoujiFlg or ks.shiharaisaki.hyoujiFlg}"><th style='min-width:70px;'>
								<c:if test='${ks.naiyouKoutsuuhi.hyoujiFlg}'>${su:htmlEscape(ks.naiyouKoutsuuhi.name)}</c:if>
								<c:if test='${ks.naiyouKoutsuuhi.hyoujiFlg and ks.bikouKoutsuuhi.hyoujiFlg}'>/</c:if>
								<c:if test='${ks.bikouKoutsuuhi.hyoujiFlg}'>${su:htmlEscape(ks.bikouKoutsuuhi.name)}</c:if>
								<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
								<span class="invoiceOnly">
									<c:if test='${(ks.naiyouNittou.hyoujiFlg or ks.bikouNittou.hyoujiFlg) and ks.shiharaisaki.hyoujiFlg}'>/</c:if>
									<c:if test='${ks.shiharaisaki.hyoujiFlg}'>${su:htmlEscape(ks.shiharaisaki.name)}</c:if>
								</span>
								</c:if>
							</th></c:if>
							<c:if test="${ks.heishu.hyoujiFlg && enableGaika}"><th>${su:htmlEscape(ks.heishu.name)}</th></c:if>
							<c:if test="${ks.rate.hyoujiFlg && enableGaika}"><th>${su:htmlEscape(ks.rate.name)}</th></c:if>
							<c:if test="${ks.gaika.hyoujiFlg && enableGaika}"><th>${su:htmlEscape(ks.gaika.name)}</th></c:if>
							<c:if test='${ks.oufukuFlg.hyoujiFlg}'><th><nobr>${su:htmlEscape(ks.oufukuFlg.name)}</nobr></th></c:if>
							<c:if test='${ks.meisaiKingaku.hyoujiFlg}'><th><nobr>${su:htmlEscape(ks.meisaiKingaku.name)}</nobr></th></c:if>
							<th class='non-print control_col'></th>
							<c:if test="${houjinCardFlag || kaishaTehaiFlag}"><th class='rangai_col'></th></c:if>
						</tr>
					</thead>
					<tbody id='kaigaiMeisaiList01'>
<c:if test='${0 < fn:length(shubetsuCd)}'><c:forEach var="i" begin="0" end="${fn:length(shubetsuCd) - 1}" step="1">
<c:if test='${shubetsuCd[i] eq "1" and kaigaiFlgRyohi[i] eq "1"}'>
						<tr class='meisai'>
							<td></td>
							<c:if test='${ks.shubetsu1.hyoujiFlg}'><td style="word-break: break-word;"></td></c:if>
							<c:if test='${ks.shubetsu2.hyoujiFlg}'><td style="word-break: break-word;"></td></c:if>
							<c:if test='${ks.koutsuuShudan.hyoujiFlg}'><td style="word-break: break-word;"></td></c:if>
							<c:if test='${denpyouKbn ne "A005" and denpyouKbn ne "A012"}'><td align='center'></td></c:if>
							<c:if test='${ks.kikan.hyoujiFlg}'><td style="word-break: normal;"></td></c:if>
							<c:if test="${ks.naiyouKoutsuuhi.hyoujiFlg or ks.bikouKoutsuuhi.hyoujiFlg or ks.shiharaisaki.hyoujiFlg}"><td></td></c:if>
							<c:if test="${ks.heishu.hyoujiFlg && enableGaika}"><td></td></c:if>
							<c:if test="${ks.rate.hyoujiFlg && enableGaika}"><td></td></c:if>
							<c:if test="${ks.gaika.hyoujiFlg && enableGaika}"><td></td></c:if>
							<c:if test='${ks.oufukuFlg.hyoujiFlg}'><td align='center'></td></c:if>
							<c:if test='${ks.meisaiKingaku.hyoujiFlg}'><td nowrap align='right' style="word-break: normal;"></td></c:if>
							<td rowspan='1' style="width:140px" class='meisai_control non-print control_col'>
								<span class='non-print'>
									<button type='button' name='kaigaiRyohiMeisaiDelete' class='btn btn-mini'>削除</button>
									<button type='button' name='kaigaiRyohiMeisaiUp' class='btn btn-mini'>↑</button>
									<button type='button' name='kaigaiRyohiMeisaiDown' class='btn btn-mini'>↓</button>
									<button type='button' name='kaigaiRyohiMeisaiCopy' class='btn btn-mini'>ｺﾋﾟｰ</button>
								</span>
								<input type='hidden' name="kaigaiFlgRyohi"				value='${su:htmlEscape(kaigaiFlgRyohi[i])}'>
								<input type='hidden' name="shubetsuCd"					value='${su:htmlEscape(shubetsuCd[i])}'>
								<input type='hidden' name="shubetsu1"					value='${su:htmlEscape(shubetsu1[i])}'>
								<input type='hidden' name="shubetsu2"					value='${su:htmlEscape(shubetsu2[i])}'>
								<input type='hidden' name="koutsuuShudan"				value='${su:htmlEscape(koutsuuShudan[i])}'>
								<input type='hidden' name="kikanFrom"					value='${su:htmlEscape(kikanFrom[i])}'>
								<input type='hidden' name="kikanTo"						value='${su:htmlEscape(kikanTo[i])}'>
								<input type='hidden' name="kyuujitsuNissuu"				value='${su:htmlEscape(kyuujitsuNissuu[i])}'>
								<input type='hidden' name="shouhyouShoruiHissuFlg"		value='${su:htmlEscape(shouhyouShoruiHissuFlg[i])}'>
								<input type='hidden' name="ryoushuushoSeikyuushoTouFlg"	value='${su:htmlEscape(ryoushuushoSeikyuushoTouFlg[i])}'>
								<input type='hidden' name="naiyou"						value='${su:htmlEscape(naiyou[i])}'>
								<input type='hidden' name="bikou"						value='${su:htmlEscape(bikou[i])}'>
								<input type='hidden' name="oufukuFlg"					value='${su:htmlEscape(oufukuFlg[i])}'>
								<input type='hidden' name="houjinCardFlgRyohi"			value='${su:htmlEscape(houjinCardFlgRyohi[i])}'>
								<input type='hidden' name="kaishaTehaiFlgRyohi"			value='${su:htmlEscape(kaishaTehaiFlgRyohi[i])}'>
								<input type='hidden' name="jidounyuuryokuFlg"			value='${su:htmlEscape(jidounyuuryokuFlg[i])}'>
								<input type='hidden' name="nissuu"						value='${su:htmlEscape(nissuu[i])}'>
								<input type='hidden' name="ninzuu"						value='${su:htmlEscape(ninzuu[i])}'>
								<input type='hidden' name="heishuCdRyohi"				value='${su:htmlEscape(heishuCdRyohi[i])}'>
								<input type='hidden' name="rateRyohi"					value='${su:htmlEscape(rateRyohi[i])}'>
								<input type='hidden' name="gaikaRyohi"					value='${su:htmlEscape(gaikaRyohi[i])}'>
								<input type='hidden' name="taniRyohi"					value='${su:htmlEscape(taniRyohi[i])}'>
								<input type='hidden' name="tanka"						value='${su:htmlEscape(tanka[i])}'>
								<input type='hidden' name="suuryouNyuryokuType"			value=''>
								<input type='hidden' name="suuryou"						value=''>
								<input type='hidden' name="suuryouKigou"				value=''>
								<input type='hidden' name="meisaiKingaku"				value='${su:htmlEscape(meisaiKingaku[i])}'>
								<input type='hidden' name="zeiKubun"					value='${su:htmlEscape(zeiKubun[i])}'>
								<input type='hidden' name="kazeiFlgRyohiMeisai"			value='${su:htmlEscape(kazeiFlgRyohiMeisai[i])}'>
								<input type='hidden' name="nittouFlg"					value=''>
								<input type='hidden' name="icCardNo"					value=''>
								<input type='hidden' name="icCardSequenceNo"			value=''>
								<input type='hidden' name="himodukeCardMeisaiRyohi"		value='${su:htmlEscape(himodukeCardMeisaiRyohi[i])}'>
								<input type='hidden' name="hayaFlg"						value=''>
								<input type='hidden' name="yasuFlg"						value=''>
								<input type='hidden' name="rakuFlg"						value=''>
<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
								<input type='hidden' name='shouhizeigakuRyohi' class='autoNumeric' value='${su:htmlEscape(shouhizeigakuRyohi[i])}'>
								<input type='hidden' name='zeinukiKingakuRyohi' class='autoNumeric' value='${su:htmlEscape(zeinukiKingakuRyohi[i])}'>
								<input type='hidden' name='shiharaisakiNameRyohi' value='${su:htmlEscape(shiharaisakiNameRyohi[i])}'>
								<input type='hidden' name='jigyoushaKbnRyohi' value='${su:htmlEscape(jigyoushaKbnRyohi[i])}'>
								<input type='hidden' name='zeigakuFixFlg' value='${su:htmlEscape(zeigakuFixFlg[i])}'>
</c:if>
							</td>
							<c:if test="${houjinCardFlag || kaishaTehaiFlag}"><td rowspan='1' class='meisai_control rangai_col'></td></c:if>
					</tr>
</c:if>
<c:if test='${flg03 eq "0"}'>
						<tr class='meisai'>
							<td></td>
							<c:if test='${ks.shubetsu1.hyoujiFlg}'><td style="word-break: break-word;"></td></c:if>
							<c:if test='${ks.shubetsu2.hyoujiFlg}'><td style="word-break: break-word;"></td></c:if>
							<c:if test='${ks.koutsuuShudan.hyoujiFlg}'><td style="word-break: break-word;"></td></c:if>
							<c:if test='${denpyouKbn ne "A005" and denpyouKbn ne "A012"}'><td align='center'></td></c:if>
							<c:if test='${ks.kikan.hyoujiFlg}'><td style="word-break: normal;"></td></c:if>
							<c:if test="${ks.naiyouKoutsuuhi.hyoujiFlg or ks.bikouKoutsuuhi.hyoujiFlg or ks.shiharaisaki.hyoujiFlg}"><td></td></c:if>
							<c:if test="${ks.heishu.hyoujiFlg && enableGaika}"><td></td></c:if>
							<c:if test="${ks.rate.hyoujiFlg && enableGaika}"><td></td></c:if>
							<c:if test="${ks.gaika.hyoujiFlg && enableGaika}"><td></td></c:if>
							<c:if test='${ks.oufukuFlg.hyoujiFlg}'><td align='center'></td></c:if>
							<c:if test='${ks.meisaiKingaku.hyoujiFlg}'><td nowrap align='right' style="word-break: normal;"></td></c:if>
							<td rowspan='1' style="width:140px" class='meisai_control non-print control_col'>
								<span class='non-print'>
									<button type='button' name='kaigaiRyohiMeisaiDelete' class='btn btn-mini'>削除</button>
									<button type='button' name='kaigaiRyohiMeisaiUp' class='btn btn-mini'>↑</button>
									<button type='button' name='kaigaiRyohiMeisaiDown' class='btn btn-mini'>↓</button>
									<button type='button' name='kaigaiRyohiMeisaiCopy' class='btn btn-mini'>ｺﾋﾟｰ</button>
								</span>
								<input type='hidden' name="kaigaiFlgRyohi"				value=''>
								<input type='hidden' name="shubetsuCd"					value=''>
								<input type='hidden' name="shubetsu1"					value=''>
								<input type='hidden' name="shubetsu2"					value=''>
								<input type='hidden' name="koutsuuShudan"				value=''>
								<input type='hidden' name="kikanFrom"					value=''>
								<input type='hidden' name="kikanTo"						value=''>
								<input type='hidden' name="kyuujitsuNissuu"				value=''>
								<input type='hidden' name="shouhyouShoruiHissuFlg"		value=''>
								<input type='hidden' name="ryoushuushoSeikyuushoTouFlg"	value=''>
								<input type='hidden' name="naiyou"						value=''>
								<input type='hidden' name="bikou"						value=''>
								<input type='hidden' name="oufukuFlg"					value=''>
								<input type='hidden' name="houjinCardFlgRyohi"			value=''>
								<input type='hidden' name="kaishaTehaiFlgRyohi"			value=''>
								<input type='hidden' name="jidounyuuryokuFlg"			value=''>
								<input type='hidden' name="nissuu"						value=''>
								<input type='hidden' name="ninzuu"						value=''>
								<input type='hidden' name="heishuCdRyohi"				value=''>
								<input type='hidden' name="rateRyohi"					value=''>
								<input type='hidden' name="gaikaRyohi"					value=''>
								<input type='hidden' name="taniRyohi"					value=''>
								<input type='hidden' name="tanka"						value=''>
								<input type='hidden' name="suuryouNyuryokuType"			value=''>
								<input type='hidden' name="suuryou"						value=''>
								<input type='hidden' name="suuryouKigou"				value=''>
								<input type='hidden' name="meisaiKingaku"				value=''>
								<input type='hidden' name="zeiKubun"					value=''>
								<input type='hidden' name="kazeiFlgRyohiMeisai"			value=''>
								<input type='hidden' name="nittouFlg"					value=''>
								<input type='hidden' name="icCardNo"					value=''>
								<input type='hidden' name="icCardSequenceNo"			value=''>
								<input type='hidden' name="himodukeCardMeisaiRyohi"		value=''>
								<input type='hidden' name="hayaFlg"						value=''>
								<input type='hidden' name="yasuFlg"						value=''>
								<input type='hidden' name="rakuFlg"						value=''>
<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
								<input type='hidden' name='shouhizeigakuRyohi' class='autoNumeric' value=''>
								<input type='hidden' name='zeinukiKingakuRyohi' class='autoNumeric' value=''>
								<input type='hidden' name='shiharaisakiNameRyohi' value=''>
								<input type='hidden' name='jigyoushaKbnRyohi' value=''>
								<input type='hidden' name='zeigakuFixFlg' value=''>
</c:if>
							</td>
							<c:if test="${houjinCardFlag || kaishaTehaiFlag}"><td rowspan='1' class='meisai_control rangai_col'></td></c:if>
					</tr>
<c:set var="flg03" value="1" />
</c:if>
</c:forEach></c:if>
				</tbody>
			</table>
	</div>
	
<c:if test='${enableNittou}'>
	<div class="control-group">
		<label class="control-label"><span class="required">*</span>小計</label>
		<div class="controls">
			<input name="shoukei04" class="input-medium autoNumericNoMax hissu" value="0" disabled="disabled" type="text">円
		</div>
	</div>
</c:if>	

<c:if test='${enableNittou}'>

	<h2>明細（日当・宿泊費等）</h2>
	<div>
		<input type='hidden' id="enableInput"		value='${su:htmlEscape(enableInput)}'>
		<input type='hidden' class='autoNumeric' name="sashihikiTankaKaigai" value='${su:htmlEscape(sashihikiTankaKaigai)}'>
		<button type='button' id='kaigaiMeisaiAddButton2' class='btn btn-small'>明細追加</button>
	</div>
	
	<div id='kaigaiMeisaiTableDiv02' class='no-more-tables' style='display:none'>
				<table class='table-bordered table-condensed' style='margin-bottom:5px; border-collapse: collapse; word-break: break-all; word-wrap: break-word;'>
				
					<thead>
						<tr>
							<th style="min-width:30px;">No</th>
							<!--  <th style="min-width:70px;">種別</th>  -->
							<c:if test='${ks.shubetsu1.hyoujiFlg}'><th style="min-width:70px;">${su:htmlEscape(ks.shubetsu1.name)}</th></c:if>
							<c:if test='${ks.shubetsu2.hyoujiFlg}'><th style="min-width:70px;"><nobr>${su:htmlEscape(ks.shubetsu2.name)}</nobr></th></c:if>
							<!--  <th><nobr>交通手段</nobr></th> -->
							<c:if test='${denpyouKbn ne "A005" and denpyouKbn ne "A012"}'><th><nobr>証憑</nobr></th></c:if>
							<c:if test='${ks.kikan.hyoujiFlg}'><th><nobr>${su:htmlEscape(ks.kikan.name)}</nobr></th></c:if>
							<c:if test='${ks.kyuujitsuNissuu.hyoujiFlg}'><th>${su:htmlEscape(ks.kyuujitsuNissuu.name)}</th></c:if>
							<c:if test="${ks.naiyouNittou.hyoujiFlg or ks.bikouNittou.hyoujiFlg or ks.shiharaisaki.hyoujiFlg}"><th style='min-width:70px;'>
								<c:if test='${ks.naiyouNittou.hyoujiFlg}'>${su:htmlEscape(ks.naiyouNittou.name)}</c:if>
								<c:if test='${ks.naiyouNittou.hyoujiFlg and ks.bikouNittou.hyoujiFlg}'>/</c:if>
								<c:if test='${ks.bikouNittou.hyoujiFlg}'>${su:htmlEscape(ks.bikouNittou.name)}</c:if>
								<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
								<span class="invoiceOnly">
									<c:if test='${(ks.naiyouNittou.hyoujiFlg or ks.bikouNittou.hyoujiFlg) and ks.shiharaisaki.hyoujiFlg}'>/</c:if>
									<c:if test='${ks.shiharaisaki.hyoujiFlg}'>${su:htmlEscape(ks.shiharaisaki.name)}</c:if>
								</span>
								</c:if>
							</th></c:if>
							<c:if test="${ks.heishu.hyoujiFlg && enableGaika}"><th>${su:htmlEscape(ks.heishu.name)}</th></c:if>
							<c:if test="${ks.rate.hyoujiFlg && enableGaika}"><th>${su:htmlEscape(ks.rate.name)}</th></c:if>
							<c:if test="${ks.gaika.hyoujiFlg && enableGaika}"><th>${su:htmlEscape(ks.gaika.name)}</th></c:if>
							<c:if test='${ks.meisaiKingaku.hyoujiFlg}'><th><nobr>${su:htmlEscape(ks.meisaiKingaku.name)}</nobr></th></c:if>
							<th class="non-print control_col"></th>
							<c:if test="${houjinCardFlag || kaishaTehaiFlag}"><th class="rangai_col"></th></c:if>
						</tr>
					</thead>
					<tbody id='kaigaiMeisaiList02'>
<c:if test='${0 < fn:length(shubetsuCd)}'><c:forEach var="i" begin="0" end="${fn:length(shubetsuCd) - 1}" step="1">
<c:if test='${shubetsuCd[i] eq "2" and kaigaiFlgRyohi[i] eq "1"}'>
						<tr class='meisai'>
							<td></td>
							<c:if test='${ks.shubetsu1.hyoujiFlg}'><td style="word-break: break-word;"></td></c:if>
							<c:if test='${ks.shubetsu2.hyoujiFlg}'><td style="word-break: break-word;"></td></c:if>
							<c:if test='${denpyouKbn ne "A005" and denpyouKbn ne "A012"}'><td align='center'></td></c:if>
							<c:if test='${ks.kikan.hyoujiFlg}'><td style="word-break: normal;"></td></c:if>
							<c:if test='${ks.kyuujitsuNissuu.hyoujiFlg}'><td></td></c:if>
							<c:if test="${ks.naiyouNittou.hyoujiFlg or ks.bikouNittou.hyoujiFlg or ks.shiharaisaki.hyoujiFlg}"><td></td></c:if>
							<c:if test="${ks.heishu.hyoujiFlg && enableGaika}"><td></td></c:if>
							<c:if test="${ks.rate.hyoujiFlg && enableGaika}"><td></td></c:if>
							<c:if test="${ks.gaika.hyoujiFlg && enableGaika}"><td></td></c:if>
							<c:if test='${ks.meisaiKingaku.hyoujiFlg}'><td nowrap align='right' style="word-break: normal;"></td></c:if>
							<td rowspan='1' style="width:140px" class='meisai_control non-print control_col'>
								<span class='non-print'>
									<button type='button' name='kaigaiRyohiMeisaiDelete' class='btn btn-mini'>削除</button>
									<button type='button' name='kaigaiRyohiMeisaiUp' class='btn btn-mini'>↑</button>
									<button type='button' name='kaigaiRyohiMeisaiDown' class='btn btn-mini'>↓</button>
									<button type='button' name='kaigaiRyohiMeisaiCopy' class='btn btn-mini'>ｺﾋﾟｰ</button>
								</span>
								<input type='hidden' name="kaigaiFlgRyohi"				value='${su:htmlEscape(kaigaiFlgRyohi[i])}'>
								<input type='hidden' name="shubetsuCd"					value='${su:htmlEscape(shubetsuCd[i])}'>
								<input type='hidden' name="shubetsu1"					value='${su:htmlEscape(shubetsu1[i])}'>
								<input type='hidden' name="shubetsu2"					value='${su:htmlEscape(shubetsu2[i])}'>
								<input type='hidden' name="koutsuuShudan"				value='${su:htmlEscape(koutsuuShudan[i])}'>
								<input type='hidden' name="kikanFrom"					value='${su:htmlEscape(kikanFrom[i])}'>
								<input type='hidden' name="kikanTo"						value='${su:htmlEscape(kikanTo[i])}'>
								<input type='hidden' name="kyuujitsuNissuu"				value='${su:htmlEscape(kyuujitsuNissuu[i])}'>
								<input type='hidden' name="shouhyouShoruiHissuFlg"		value='${su:htmlEscape(shouhyouShoruiHissuFlg[i])}'>
								<input type='hidden' name="ryoushuushoSeikyuushoTouFlg"	value='${su:htmlEscape(ryoushuushoSeikyuushoTouFlg[i])}'>
								<input type='hidden' name="naiyou"						value='${su:htmlEscape(naiyou[i])}'>
								<input type='hidden' name="bikou"						value='${su:htmlEscape(bikou[i])}'>
								<input type='hidden' name="oufukuFlg"					value='${su:htmlEscape(oufukuFlg[i])}'>
								<input type='hidden' name="houjinCardFlgRyohi"			value='${su:htmlEscape(houjinCardFlgRyohi[i])}'>
								<input type='hidden' name="kaishaTehaiFlgRyohi"			value='${su:htmlEscape(kaishaTehaiFlgRyohi[i])}'>
								<input type='hidden' name="jidounyuuryokuFlg"			value='${su:htmlEscape(jidounyuuryokuFlg[i])}'>
								<input type='hidden' name="nissuu"						value='${su:htmlEscape(nissuu[i])}'>
								<input type='hidden' name="ninzuu"						value='${su:htmlEscape(ninzuu[i])}'>
								<input type='hidden' name="heishuCdRyohi"				value='${su:htmlEscape(heishuCdRyohi[i])}'>
								<input type='hidden' name="rateRyohi"					value='${su:htmlEscape(rateRyohi[i])}'>
								<input type='hidden' name="gaikaRyohi"					value='${su:htmlEscape(gaikaRyohi[i])}'>
								<input type='hidden' name="taniRyohi"					value='${su:htmlEscape(taniRyohi[i])}'>
								<input type='hidden' name="tanka"						value='${su:htmlEscape(tanka[i])}'>
								<input type='hidden' name="suuryouNyuryokuType"			value=''>
								<input type='hidden' name="suuryou"						value=''>
								<input type='hidden' name="suuryouKigou"				value=''>
								<input type='hidden' name="meisaiKingaku"				value='${su:htmlEscape(meisaiKingaku[i])}'>
								<input type='hidden' name="zeiKubun"					value='${su:htmlEscape(zeiKubun[i])}'>
								<input type='hidden' name="kazeiFlgRyohiMeisai"			value='${su:htmlEscape(kazeiFlgRyohiMeisai[i])}'>
								<input type='hidden' name="nittouFlg"					value='${su:htmlEscape(nittouFlg[i])}'>
								<input type='hidden' name="icCardNo"					value=''>
								<input type='hidden' name="icCardSequenceNo"			value=''>
								<input type='hidden' name="himodukeCardMeisaiRyohi"		value='${su:htmlEscape(himodukeCardMeisaiRyohi[i])}'>
								<input type='hidden' name="hayaFlg"						value=''>
								<input type='hidden' name="yasuFlg"						value=''>
								<input type='hidden' name="rakuFlg"						value=''>
<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
								<input type='hidden' name='shouhizeigakuRyohi' class='autoNumeric' value='${su:htmlEscape(shouhizeigakuRyohi[i])}'>
								<input type='hidden' name='zeinukiKingakuRyohi' class='autoNumeric' value='${su:htmlEscape(zeinukiKingakuRyohi[i])}'>
								<input type='hidden' name='shiharaisakiNameRyohi' value='${su:htmlEscape(shiharaisakiNameRyohi[i])}'>
								<input type='hidden' name='jigyoushaKbnRyohi' value='${su:htmlEscape(jigyoushaKbnRyohi[i])}'>
								<input type='hidden' name='zeigakuFixFlg' value='${su:htmlEscape(zeigakuFixFlg[i])}'>
</c:if>
							</td>
							<c:if test="${houjinCardFlag || kaishaTehaiFlag}"><td rowspan='1' class='meisai_control rangai_col'></td></c:if>
						</tr>
</c:if>
<c:if test='${flg04 eq "0"}'>
						<tr class='meisai'>
							<td></td>
							<c:if test='${ks.shubetsu1.hyoujiFlg}'><td style="word-break: break-word;"></td></c:if>
							<c:if test='${ks.shubetsu2.hyoujiFlg}'><td style="word-break: break-word;"></td></c:if>
							<c:if test='${denpyouKbn ne "A005" and denpyouKbn ne "A012"}'><td align='center'></td></c:if>
							<c:if test='${ks.kikan.hyoujiFlg}'><td style="word-break: normal;"></td></c:if>
							<c:if test='${ks.kyuujitsuNissuu.hyoujiFlg}'><td></td></c:if>
							<c:if test="${ks.naiyouNittou.hyoujiFlg or ks.bikouNittou.hyoujiFlg or ks.shiharaisaki.hyoujiFlg}"><td></td></c:if>
							<c:if test="${ks.heishu.hyoujiFlg && enableGaika}"><td></td></c:if>
							<c:if test="${ks.rate.hyoujiFlg && enableGaika}"><td></td></c:if>
							<c:if test="${ks.gaika.hyoujiFlg && enableGaika}"><td></td></c:if>
							<c:if test='${ks.meisaiKingaku.hyoujiFlg}'><td nowrap align='right' style="word-break: normal;"></td></c:if>
							<td rowspan='1' style="width:140px" class='meisai_control non-print control_col'>
								<span class='non-print'>
									<button type='button' name='kaigaiRyohiMeisaiDelete' class='btn btn-mini'>削除</button>
									<button type='button' name='kaigaiRyohiMeisaiUp' class='btn btn-mini'>↑</button>
									<button type='button' name='kaigaiRyohiMeisaiDown' class='btn btn-mini'>↓</button>
									<button type='button' name='kaigaiRyohiMeisaiCopy' class='btn btn-mini'>ｺﾋﾟｰ</button>
								</span>
								<input type='hidden' name="kaigaiFlgRyohi"				value=''>
								<input type='hidden' name="shubetsuCd"					value=''>
								<input type='hidden' name="shubetsu1"					value=''>
								<input type='hidden' name="shubetsu2"					value=''>
								<input type='hidden' name="koutsuuShudan"				value=''>
								<input type='hidden' name="kikanFrom"					value=''>
								<input type='hidden' name="kikanTo"						value=''>
								<input type='hidden' name="kyuujitsuNissuu"				value=''>
								<input type='hidden' name="shouhyouShoruiHissuFlg"		value=''>
								<input type='hidden' name="ryoushuushoSeikyuushoTouFlg"	value=''>
								<input type='hidden' name="naiyou"						value=''>
								<input type='hidden' name="bikou"						value=''>
								<input type='hidden' name="oufukuFlg"					value=''>
								<input type='hidden' name="houjinCardFlgRyohi"			value=''>
								<input type='hidden' name="kaishaTehaiFlgRyohi"			value=''>
								<input type='hidden' name="jidounyuuryokuFlg"			value=''>
								<input type='hidden' name="nissuu"						value=''>
								<input type='hidden' name="ninzuu"						value=''>
								<input type='hidden' name="heishuCdRyohi"				value=''>
								<input type='hidden' name="rateRyohi"					value=''>
								<input type='hidden' name="gaikaRyohi"					value=''>
								<input type='hidden' name="taniRyohi"					value=''>
								<input type='hidden' name="tanka"						value=''>
								<input type='hidden' name="suuryouNyuryokuType"			value=''>
								<input type='hidden' name="suuryou"						value=''>
								<input type='hidden' name="suuryouKigou"				value=''>
								<input type='hidden' name="meisaiKingaku"				value=''>
								<input type='hidden' name="zeiKubun"					value=''>
								<input type='hidden' name="kazeiFlgRyohiMeisai"			value=''>
								<input type='hidden' name="nittouFlg"					value=''>
								<input type='hidden' name="icCardNo"					value=''>
								<input type='hidden' name="icCardSequenceNo"			value=''>
								<input type='hidden' name="himodukeCardMeisaiRyohi"		value=''>
								<input type='hidden' name="hayaFlg"						value=''>
								<input type='hidden' name="yasuFlg"						value=''>
								<input type='hidden' name="rakuFlg"						value=''>
<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
								<input type='hidden' name='shouhizeigakuRyohi' class='autoNumeric' value=''>
								<input type='hidden' name='zeinukiKingakuRyohi' class='autoNumeric' value=''>
								<input type='hidden' name='shiharaisakiNameRyohi' value=''>
								<input type='hidden' name='jigyoushaKbnRyohi' value=''>
								<input type='hidden' name='zeigakuFixFlg' value=''>
</c:if>
							</td>
							<c:if test="${houjinCardFlag || kaishaTehaiFlag}"><td rowspan='1' class='meisai_control rangai_col'></td></c:if>
						</tr>
<c:set var="flg04" value="1" />
</c:if>
</c:forEach></c:if>
					</tbody>
				</table>
	</div>
	<div class='control-group<c:if test='${not sasihikiHyoujiFlgKaigai}'> never_show' style='display:none;</c:if>' >
		<div class='control-group<c:if test='${not sashihikiHyoujiFlgKaigaiGaika}'> never_show' style='display:none;</c:if>' >
			<label class="control-label <c:if test='${not sashihikiHyoujiFlgKaigaiGaika}'> never_show' style='display:none;</c:if>">${su:htmlEscape(ks.sashihikiHeishuCdKaigai.name)}</label>
			<div class="controls">
			<input type='text' name="sashihikiHeishuCdKaigai" class="input-medium" disabled value='${su:htmlEscape(sashihikiHeishuCdKaigai)}'>
				<label class="label">${su:htmlEscape(ks.sashihikiRateKaigai.name)}</label>
				<input type='text' class='input-medium' name='sashihikiRateKaigai' disabled value='${su:htmlEscape(sashihikiRateKaigai)}'>
			</div>
		</div>
		
		<div class='control-group<c:if test='${not sashihikiHyoujiFlgKaigaiGaika}'> never_show' style='display:none;</c:if>' >
			<label class="control-label">${su:htmlEscape(ks.sashihikiTankaKaigaiGaika.name)}</label>
			<div class="controls">
			<input type='text' name="sashihikiTankaKaigaiGaika" class="input-medium" disabled value='${su:htmlEscape(sashihikiTankaKaigaiGaika)}'>
				<label class="label">${su:htmlEscape(ks.sashihikiTankaKaigai.name)}</label>
				<input type='text' class='input-medium autoNumeric' name='sasihikiHouka' disabled value='${su:htmlEscape(sashihikiTankaKaigai)}'>
			</div>
		</div>
		
		<label class="control-label"><c:if test='${ks.sashihikiNum.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(sashihikiNameKaigai)}</label>
		<div class="controls">
		<input name="sashihikiNumKaigai" class="input-medium" type="tel" maxlength='2' value='${su:htmlEscape(sashihikiNumKaigai)}'>回
			<label class="label"><c:if test='${ks.sashihikiKingaku.hissuFlg}'><span class='required'>*</span></c:if>${su:htmlEscape(ks.sashihikiKingaku.name)}</label>
			<input name="sashihikiKingakuKaigai" class="input-medium autoNumericNoMax" value="0" disabled="disabled" type="text">円
		</div>
	</div>
	<div class="control-group">
		<label class="control-label"><span class="required">*</span>小計</label>
		<div class="controls">
			<input name="shoukei05" class="input-medium autoNumericNoMax hissu" value="0" disabled="disabled" type="text">円
		</div>
	</div>
</c:if>

</section>

<!-- スクリプト -->
<script style='text/javascript'>

/**
 * 明細のhiddenに明細MAPを反映
 * ダイアログを明細部に反映する途中処理＞これの後の「setDisplayKaigaiRyohiMeisaiData」で明細部を再描画する。
 * 明細の追加・変更時に setKaigaiRyohiMeisaiInfo + setDisplayRyohiMeisaiDataが呼ばれる
 * @param index				行(0～)
 * @param meisaiInfo		明細情報(MAP)
 * @param meisaiShubetsuCd	明細の種別
 */
function setKaigaiRyohiMeisaiInfo(index, meisaiInfo, meisaiShubetsuCd) {
	var meisai = $("#kaigaiMeisaiList0" + meisaiShubetsuCd +  " tr.meisai:eq(" + index + ")");
	meisai.find("input[name=kaigaiFlgRyohi]").val(meisaiInfo["kaigaiFlgRyohi"]);
	meisai.find("input[name=shubetsuCd]").val(meisaiInfo["shubetsuCd"]);
	meisai.find("input[name=shubetsu1]").val(meisaiInfo["shubetsu1"]);
	meisai.find("input[name=shubetsu2]").val(meisaiInfo["shubetsu2"]);
 	meisai.find("input[name=koutsuuShudan]").val(meisaiInfo["koutsuuShudan"]);
	meisai.find("input[name=kikanFrom]").val(meisaiInfo["kikanFrom"]);
	meisai.find("input[name=kikanTo]").val(meisaiInfo["kikanTo"]);
	meisai.find("input[name=kyuujitsuNissuu]").val(meisaiInfo["kyuujitsuNissuu"]);
 	meisai.find("input[name=shouhyouShoruiHissuFlg]").val(meisaiInfo["shouhyouShoruiHissuFlg"]);
 	meisai.find("input[name=ryoushuushoSeikyuushoTouFlg]").val(meisaiInfo["ryoushuushoSeikyuushoTouFlg"]);
	meisai.find("input[name=naiyou]").val(meisaiInfo["naiyou"]);
	meisai.find("input[name=bikou]").val(meisaiInfo["bikou"]);
	meisai.find("input[name=oufukuFlg]").val(meisaiInfo["oufukuFlg"]);
	meisai.find("input[name=houjinCardFlgRyohi]").val(meisaiInfo["houjinCardFlgRyohi"]);
	meisai.find("input[name=kaishaTehaiFlgRyohi]").val(meisaiInfo["kaishaTehaiFlgRyohi"]);
	meisai.find("input[name=jidounyuuryokuFlg]").val(meisaiInfo["jidounyuuryokuFlg"]);
	meisai.find("input[name=nissuu]").val(meisaiInfo["nissuu"]);
	meisai.find("input[name=ninzuu]").val(meisaiInfo["ninzuu"]);
	meisai.find("input[name=heishuCdRyohi]").val(meisaiInfo["heishuCdRyohi"]);
	meisai.find("input[name=rateRyohi]").val(meisaiInfo["rateRyohi"]);
	meisai.find("input[name=gaikaRyohi]").val(meisaiInfo["gaikaRyohi"]);
	meisai.find("input[name=taniRyohi]").val(meisaiInfo["taniRyohi"]);
	meisai.find("input[name=tanka]").val(meisaiInfo["tanka"]);
	meisai.find("input[name=suuryouNyuryokuType]").val(meisaiInfo["suuryouNyuryokuType"]);
	meisai.find("input[name=suuryou]").val(meisaiInfo["suuryou"]);
	meisai.find("input[name=suuryouKigou]").val(meisaiInfo["suuryouKigou"]);
	meisai.find("input[name=meisaiKingaku]").val(meisaiInfo["meisaiKingaku"]);
	meisai.find("input[name=zeiKubun]").val(meisaiInfo["zeiKubun"]);
	meisai.find("input[name=kazeiFlgRyohiMeisai]").val(meisaiInfo["kazeiFlgRyohiMeisai"]);
	meisai.find("input[name=nittouFlg]").val(meisaiInfo["nittouFlg"]);
	meisai.find("input[name=himodukeCardMeisaiRyohi]").val(meisaiInfo["himodukeCardMeisaiRyohi"]);
	meisai.find("input[name=hayaFlg]").val(meisaiInfo["hayaFlg"]);
	meisai.find("input[name=yasuFlg]").val(meisaiInfo["yasuFlg"]);
	meisai.find("input[name=rakuFlg]").val(meisaiInfo["rakuFlg"]);
<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
	meisai.find("input[name=shouhizeigakuRyohi]").val(meisaiInfo["shouhizeigaku"]);
	meisai.find("input[name=zeinukiKingakuRyohi]").val(meisaiInfo["zeinukiKingaku"]);
	meisai.find("input[name=shiharaisakiNameRyohi]").val(meisaiInfo["shiharaisaki"]);
	meisai.find("input[name=jigyoushaKbnRyohi]").val(meisaiInfo["jigyoushaKbn"]);
	meisai.find("input[name=zeigakuFixFlg]").val(meisaiInfo["zeigakuFixFlg"]);
</c:if>
}

/**
 * 明細MAP取得
 * @param index				行(0～)
 * @param meisaiShubetsuCd	明細の種別
 */
function getKaigaiRyohiMeisaiInfo(index, meisaiShubetsuCd) {
	var meisai = $("#kaigaiMeisaiList0" + meisaiShubetsuCd + " tr.meisai:eq(" + index + ")");
	//return getMeisaiInfoFromTr(meisai, index, meisaiShubetsuCd);
	
	// 一覧の最大インデックスを取得する
	var maxIndex = $("#kaigaiMeisaiList0" + meisaiShubetsuCd).find("tr.meisai").length - 1;

	//該当行の明細MAP返す
	return {
		 index : index,
		maxIndex : maxIndex,
		
		//enableInput : 'true' == $('#enableInput').val(),
		denpyouId : $("input[name=denpyouId]").val(),
		denpyouKbn : $("input[name=denpyouKbn]").val(),
		zeroEnabled : $("input[name=karibaraiOn]:checked").length == 1 ? $("input[name=karibaraiOn]:checked").val() : "0",
		userId : $("input[name=userIdRyohi]").val(),
		dispMode : ('true' == $('#enableInput').val()) ? "2" : "3",
		kazeiFlgRyohi : $("input[name=kazeiFlgRyohiKaigai]").val(),
		kazeiFlgKamoku : $("input[name=kazeiFlgRyohiKaigaiKamoku]").val(),

		kikanFrom : meisai.find("input[name=kikanFrom]").val(),
		kikanTo : meisai.find("input[name=kikanTo]").val(),
		kyuujitsuNissuu : meisai.find("input[name=kyuujitsuNissuu]").val(),
		kaigaiFlgRyohi : meisai.find("input[name=kaigaiFlgRyohi]").val() == "1" ? "1" : "0",
		shubetsuCd : meisai.find("input[name=shubetsuCd]").val(),
		shubetsu1 : meisai.find("input[name=shubetsu1]").val(),
		shubetsu2 : meisai.find("input[name=shubetsu2]").val(),
		koutsuuShudan : meisai.find("input[name=koutsuuShudan]").val(),
		shouhyouShoruiHissuFlg : meisai.find("input[name=shouhyouShoruiHissuFlg]").val(),
		ryoushuushoSeikyuushoTouFlg : meisai.find("input[name=ryoushuushoSeikyuushoTouFlg]").val(),
		naiyou : meisai.find("input[name=naiyou]").val(),
		bikou : meisai.find("input[name=bikou]").val(),
		oufukuFlg : meisai.find("input[name=oufukuFlg]").val(),
		houjinCardFlgRyohi : meisai.find("input[name=houjinCardFlgRyohi]").val(),
		kaishaTehaiFlgRyohi : meisai.find("input[name=kaishaTehaiFlgRyohi]").val(),
		jidounyuuryokuFlg : meisai.find("input[name=jidounyuuryokuFlg]").val(),
		nissuu : meisai.find("input[name=nissuu]").val(),
		ninzuu : meisai.find("input[name=ninzuu]").val(),
		heishuCdRyohi : meisai.find("input[name=heishuCdRyohi]").val(),
		rateRyohi : meisai.find("input[name=rateRyohi]").val(),
		gaikaRyohi : meisai.find("input[name=gaikaRyohi]").val(),
		taniRyohi : meisai.find("input[name=taniRyohi]").val(),
		tanka : meisai.find("input[name=tanka]").val(),
		suuryouNyuryokuType : meisai.find("input[name=suuryouNyuryokuType]").val(),
		suuryou : meisai.find("input[name=suuryou]").val(),
		suuryouKigou : meisai.find("input[name=suuryouKigou]").val(),
		meisaiKingaku : meisai.find("input[name=meisaiKingaku]").val(),
		zeiKubun : meisai.find("input[name=zeiKubun]").val(),
		kazeiFlgRyohiMeisai : meisai.find("input[name=kazeiFlgRyohiMeisai]").val(),
		nittouFlg : meisai.find("input[name=nittouFlg]").val(),
		himodukeCardMeisaiRyohi : meisai.find("input[name=himodukeCardMeisaiRyohi]").val(),
		hayaFlg : meisai.find("input[name=hayaFlg]").val() == "1" ? "1" : "0",
		yasuFlg : meisai.find("input[name=yasuFlg]").val() == "1" ? "1" : "0",
		rakuFlg : meisai.find("input[name=rakuFlg]").val() == "1" ? "1" : "0",
<c:if test='${denpyouKbn eq "A004" || denpyouKbn eq "A010" || denpyouKbn eq "A011"}'>
		shouhizeigaku : meisai.find("input[name=shouhizeigakuRyohi]").val(),
		zeinukiKingaku : meisai.find("input[name=zeinukiKingakuRyohi]").val(),
		shiharaisaki : meisai.find("input[name=shiharaisakiNameRyohi]").val(),
		jigyoushaKbn : meisai.find("input[name=jigyoushaKbnRyohi]").val(),
		zeigakuFixFlg : meisai.find("input[name=zeigakuFixFlg]").val(),
</c:if>
	};
}

/**
 * 内容設定されている明細の数を取得します。
 */
function getEnableKaigaiRyohiMeisaiCount(meisaiShubetsuCd) {
	var count = 0;
	$.each($("#kaigaiMeisaiList0" + meisaiShubetsuCd + " input[name=shubetsuCd]"), function(ii, obj) {
		if ('' != $(obj).val()) {
			count++;
		}
	});
	return count;
}

/**
 * 内部項目(Hidden設定情報)から画面表示を行います。
 * 全ての明細が対象となります。
 */
function setDisplayKaigaiRyohiMeisaiData( meisaiShubetsuCd ) {

	if (0 < getEnableKaigaiRyohiMeisaiCount(meisaiShubetsuCd)) {
		$('#kaigaiMeisaiTableDiv0' + meisaiShubetsuCd).css('display', '');
	} else {
		$('#kaigaiMeisaiTableDiv0' + meisaiShubetsuCd).css('display', 'none');
		calcMoney();
		return ;
	}

	var kaigaiMeisaiList = $("#kaigaiMeisaiList0" + meisaiShubetsuCd);
	$.each(kaigaiMeisaiList.find("tr.meisai"), function (ii, obj) {
		var index = 0;
		var meisai = $(obj);
		var no = ii + 1;
		var shubetsuCd = meisai.find("input[name=shubetsuCd]").val();
		var hissuFlg = meisai.find("input[name=shouhyouShoruiHissuFlg]").val();

		if ('' == shubetsuCd) {
			// 未設定扱い
			return ;
		}

		// 枝番
		var noObj = meisai.find("td:eq(" + index++ + ")");
		noObj.children().remove();
		noObj.text('');
		noObj.append(
			$("<a/>").text("#" + no).click(function() {
				kaigaiRyohiMeisaiDialogOpen(ii, shubetsuCd, null);
			})
		);

		// 種別1
		var shubetsu01 = meisai.find("input[name=shubetsu1]").val().htmlEscape();
		if (meisai.find("input[name=shubetsu2]").val() == "") {
			if (shubetsuCd == "2" && hissuFlg == "1" ) {
				shubetsu01 = shubetsu01 + "*";
			}
		}
		meisai.find("td:eq(" + index++ + ")").html(shubetsu01);
		
		// 種別2
		var shubetsu02 = "　";
		if (meisai.find("input[name=shubetsu2]").val() != "") {
			shubetsu02 =  meisai.find("input[name=shubetsu2]").val().htmlEscape();
			if (meisaiShubetsuCd == "2" && shubetsuCd == "2" && hissuFlg == "1" ) {
				shubetsu02 = shubetsu02 + "*";
			}
		}
		meisai.find("td:eq(" + index++ + ")").html(shubetsu02);
		
		//法人カード使用履歴明細ならコピーさせない
		if(meisai.find("input[name=shubetsu2]").val() == "法人カード"){
			meisai.find("[name=kaigaiRyohiMeisaiCopy]").hide();
		}else if(meisai.find("input[name=himodukeCardMeisaiRyohi]").val() != ""){
			meisai.find("[name=kaigaiRyohiMeisaiCopy]").hide();
		}else{
			meisai.find("[name=kaigaiRyohiMeisaiCopy]").show();
		}
		
		// 交通手段
		if( meisaiShubetsuCd == "1"){
			var koutsuuShudan = "　";
			if (shubetsuCd == "1") {
				koutsuuShudan = meisai.find("input[name=koutsuuShudan]").val().htmlEscape();
				if(hissuFlg == "1") koutsuuShudan = koutsuuShudan + "*";
			}
			meisai.find("td:eq(" + index++ + ")").html(koutsuuShudan);
		}
		
		// 領収書・請求書等
<c:if test='${denpyouKbn ne "A005" and denpyouKbn ne "A012"}'>
		var shouhyou = (meisai.find("input[name=ryoushuushoSeikyuushoTouFlg]").val() == "1") ? "○" : "　";
		meisai.find("td:eq(" + index++ + ")").text(shouhyou);
</c:if>
		
		// 期間
		var kikan = ("" == meisai.find("input[name=kikanTo]").val()) ?
					meisai.find("input[name=kikanFrom]").val() :
					meisai.find("input[name=kikanFrom]").val() + "<br>" + meisai.find("input[name=kikanTo]").val();
		meisai.find("td:eq(" + index++ + ")").html(kikan);

		// 休日日数
<c:if test="${ks.kyuujitsuNissuu.hyoujiFlg}"> 
		if( meisaiShubetsuCd == "2"){
			var kyuujitsuNissuuStr = "0日";
			if ("" != meisai.find("input[name=kyuujitsuNissuu]").val()) {
				kyuujitsuNissuuStr = meisai.find("input[name=kyuujitsuNissuu]").val() + "日";
			}
			meisai.find("td:eq(" + index++ + ")").html(kyuujitsuNissuuStr);
		}
</c:if>

		// 内容、備考
<c:if test="${ks.naiyouKoutsuuhi.hyoujiFlg or ks.bikouKoutsuuhi.hyoujiFlg or ks.shiharaisaki.hyoujiFlg}"> 
		if (meisaiShubetsuCd == '1') {
			inputNaiyouBikouKaigai (meisai, index++);
		}
</c:if>
<c:if test="${ks.naiyouNittou.hyoujiFlg or ks.bikouNittou.hyoujiFlg or ks.shiharaisaki.hyoujiFlg}"> 
		if (meisaiShubetsuCd == '2') {
			inputNaiyouBikouKaigai (meisai, index++);
		}
</c:if>

		// 幣種
<c:if test="${ks.heishu.hyoujiFlg && enableGaika}">
		var heishuStr = "　";
		if ("" != meisai.find("input[name=heishuCdRyohi]").val()) {
			heishuStr = meisai.find("input[name=heishuCdRyohi]").val();
		}
		meisai.find("td:eq(" + index++ + ")").html(heishuStr);
</c:if>
		// レート
<c:if test="${ks.rate.hyoujiFlg && enableGaika}">
		var rateStr = "　";
		if ("" != meisai.find("input[name=rateRyohi]").val()) {
			rateStr = meisai.find("input[name=rateRyohi]").val();
		}
		meisai.find("td:eq(" + index++ + ")").html(rateStr);
</c:if>
		// 外貨
<c:if test="${ks.gaika.hyoujiFlg && enableGaika}">
		var gaikaStr = "　";
		if ("" != meisai.find("input[name=gaikaRyohi]").val()) {
			gaikaStr = meisai.find("input[name=gaikaRyohi]").val() + meisai.find("input[name=taniRyohi]").val();
		}
		meisai.find("td:eq(" + index++ + ")").html(gaikaStr);
</c:if>
	
	// 往復
<c:if test="${ks.oufukuFlg.hyoujiFlg}"> 
		if (meisaiShubetsuCd == '1') {
				var oufuku = (meisai.find("input[name=oufukuFlg]").val() == "1") ? "○" : "　";
				meisai.find("td:eq(" + index++ + ")").text(oufuku);
		}
</c:if>

		// 明細金額（単価×日数）
		var meisaiKingakuStr = meisai.find("input[name=meisaiKingaku]").val() + "円";
		if ("" != meisai.find("input[name=nissuu]").val()) {
			var nissuu = meisai.find("input[name=nissuu]").val();
			var kyuujitsuNissuu = meisai.find("input[name=kyuujitsuNissuu]").val();
			if ("" == kyuujitsuNissuu) {kyuujitsuNissuu = 0;}
			var nissuuStr = "　";
			if ("" != nissuu) {
				nissuuStr = nissuu + "日";
			}
			var tankaStr = meisai.find("input[name=tanka]").val() + "円";
			
			//人数が入力されている場合（単価×日数×人数）
			if ("" != meisai.find("input[name=ninzuu]").val()) {
				var ninzuuStr = meisai.find("input[name=ninzuu]").val() + "人";
				meisaiKingakuStr = meisaiKingakuStr + "<br>" + "(" + tankaStr + "×" + nissuuStr + "×" + ninzuuStr + ")";
			} else {
				meisaiKingakuStr = meisaiKingakuStr + "<br>" + "(" + tankaStr + "×" + nissuuStr + ")";
			}
		}
		meisai.find("td:eq(" + index++ + ")").html(meisaiKingakuStr);
		
		//右欄外に表示
		//法人カード利用
		if (meisai.find("input[name=houjinCardFlgRyohi]").val() == '1') {
			meisai.find("td.rangai_col").html("<b>C</b>");
		//会社手配
		} else if (meisai.find("input[name=kaishaTehaiFlgRyohi]").val() == '1') {
			meisai.find("td.rangai_col").html("<b>K</b>");
		//それ以外
		} else {
			meisai.find("td.rangai_col").html("");
		}
		
	});
	
	//明細右のボタン非表示
	if ($("#enableInput").val() != 'true') {
		$("#kaigaiMeisaiTableDiv0" + meisaiShubetsuCd + " .control_col").css("display", "none");
	}

	//金額等再計算
	calcMoney();//親JSPの定義
}

/** 内容・備考の明細表示制御*/
function inputNaiyouBikouKaigai (meisai, index) {
	var naiyou = meisai.find("input[name=naiyou]").val().htmlEscape().replaceAll("\n", "<br>");
	var bikou = meisai.find("input[name=bikou]").val().htmlEscape();
	var shiharaisaki = $("input[name=denpyouKbn]").val() == "A011" ? meisai.find("input[name=shiharaisakiNameRyohi]").val().htmlEscape():false;

	var naiyou_bikou = "　"; // 全角スペースを初期値として設定
	var isTrulyEmpty = true; // 全角スペース（入力）との識別用
	
	if (naiyou) {
	    naiyou_bikou = naiyou;
	    isTrulyEmpty = false;
	}

	if (bikou) {
	    if (!isTrulyEmpty) {
	        naiyou_bikou += "<br>";
	    }
	    else
	    {
		    naiyou_bikou = "";
		    isTrulyEmpty = false;
	    }
	    naiyou_bikou += bikou;
	}

	if (shiharaisaki) {
	    if (!isTrulyEmpty) {
	        naiyou_bikou += "<br>";
	    }
	    else
	    {
		    naiyou_bikou = "";
	    }
	    naiyou_bikou += '<span class="invoiceOnly">' + shiharaisaki + "</span>";
	}
	
	meisai.find("td:eq(" + index + ")").html(naiyou_bikou);
}

/**
 * 指定された行を直前の行と入れ替えます。
 */
function kaigaiRyohiSwapRow(tr, meisaiShubetsuCd) {
	var cnt = parseInt($(tr).find('td.meisai_control').attr('rowspan'));

	if (0 < tr.prevAll('.meisai:first').length) {
		var base = tr.prevAll('.meisai:first');
		var targ = tr;
		for (var ii = 0; ii < cnt; ii++) {
			var next = targ.next();
			targ.insertBefore(base);
			targ = next;
		}
		setDisplayKaigaiRyohiMeisaiData(meisaiShubetsuCd);
		isDirty = true;
	}
}

/**
 * 明細↑ボタン押下時Function
 */
function kaigaiRyohiMeisaiUp() {
	var tr = $(this).closest("tr.meisai");
	var meisaiShubetsuCd = tr.find("[name=shubetsuCd]").val();
	kaigaiRyohiSwapRow(tr, meisaiShubetsuCd);
}

/**
 * 明細↓ボタン押下時Function
 */
function kaigaiRyohiMeisaiDown() {
	var tr = $(this).closest("tr.meisai");
	var meisaiShubetsuCd = tr.find("[name=shubetsuCd]").val();
	if (0 < tr.nextAll('.meisai:first').length) {
		kaigaiRyohiSwapRow(tr.nextAll('.meisai:first'), meisaiShubetsuCd);
	}
}

/**
 * 明細削除ボタン押下時Function
 */
function kaigaiRyohiMeisaiDelete() {
	var tr = $(this).closest("tr.meisai");
	var meisaiShubetsuCd = tr.find("[name=shubetsuCd]").val();

	if(window.confirm("明細を削除してもよろしいですか？")) {
		if (getEnableKaigaiRyohiMeisaiCount(meisaiShubetsuCd) == 1) {
			kaigaiRyohiMeisaiClear($(this).closest("tr.meisai"));
		} else {
			for (var ii = 0; ii < parseInt($(this).closest('td.meisai_control').attr('rowspan')) - 1; ii++) {
				tr.next().remove();
			}
			$(this).closest("tr.meisai").remove();
		}
		setDisplayKaigaiRyohiMeisaiData(meisaiShubetsuCd);
		
		isDirty = true;
	}
}

/**
 * 明細コピーボタン押下時Function
 */
function kaigaiRyohiMeisaiCopy() {
	var tr = $(this).closest("tr.meisai");
	var no = parseInt(tr.find("a").text().replace("#",""));
	var index = no - 1;
	var meisaiShubetsuCd = tr.find("[name=shubetsuCd]").val();
	
	if (index >= 0) {
		var meisaiInfo = getKaigaiRyohiMeisaiInfo(index, meisaiShubetsuCd);
		meisaiInfo["himodukeCardMeisaiRyohi"] = "";		//法人カード履歴紐付リセット
		kaigaiRyohiMeisaiAdd(meisaiInfo, meisaiShubetsuCd);
	}
}

/**
 * 明細行をクリアします。
 * @param tr	明細行TR
 */
function kaigaiRyohiMeisaiClear(tr) {
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
function kaigaiRyohiMeisaiAllDelete() {
	$.each($("#kaigaiMeisaiList01 tr.meisai"), function (ii, tr) {
		kaigaiRyohiMeisaiLineDelete(tr);
	});
	$.each($("#kaigaiMeisaiList02 tr.meisai"), function (ii, tr) {
		kaigaiRyohiMeisaiLineDelete(tr);
	});
}

/**
 * 明細行削除Function
 */
function kaigaiRyohiMeisaiLineDelete(tr){
	var meisaiShubetsuCd = $(tr).find("[name=shubetsuCd]").val();
	var count = getEnableKaigaiRyohiMeisaiCount(meisaiShubetsuCd);
	if (count == 0) {
		// 明細がない場合は処理なし。
	}else{
		if (count == 1) {
			kaigaiRyohiMeisaiClear($(tr));
		} else {
			for (var ii = 0; ii < parseInt($(tr).find('td.meisai_control').attr('rowspan')) - 1; ii++) {
				$(tr).next().remove();
			}
			$(tr).remove();
		}
		setDisplayKaigaiRyohiMeisaiData(meisaiShubetsuCd);
		isDirty = true;
	}
}

/**
 * 明細行追加
 * @param meisaiInfo	明細MAP
 */
function kaigaiRyohiMeisaiAdd(meisaiInfo, meisaiShubetsuCd) {
	var tr = null;
	
	if (0 < getEnableKaigaiRyohiMeisaiCount(meisaiShubetsuCd)) {
		//１行目の明細をコピーして内容を削除
		var _tr = $("#kaigaiMeisaiList0" + meisaiShubetsuCd + " tr.meisai:first");
		var cnt = parseInt(_tr.find('td.meisai_control').attr('rowspan'));
		
		for (var ii = 0; ii < cnt; ii++) {
			tr = _tr.clone();
			// 行の挿入
			$("#kaigaiMeisaiList0" + meisaiShubetsuCd).append(tr);
			// 初期化
			commonInit($(tr));
			_tr = _tr.next();
		}
		tr = $("#kaigaiMeisaiList0" + meisaiShubetsuCd + " tr.meisai:last");
	} else {
		// 空ならそこをそのまま使用する。
		tr = $("#kaigaiMeisaiList0" + meisaiShubetsuCd + " tr.meisai:first");
	}

	// 行の初期化
	kaigaiRyohiMeisaiClear(tr);

	// 値の設定
	setKaigaiRyohiMeisaiInfo($("#kaigaiMeisaiList0" + meisaiShubetsuCd + " tr.meisai").length - 1, meisaiInfo, meisaiShubetsuCd);

	// 再表示
	setDisplayKaigaiRyohiMeisaiData(meisaiShubetsuCd);
	
	isDirty = true;
}

/**
 * 明細行変更
 * @param index			行(0～)
 * @param meisaiInfo	明細MAP
 */
function kaigaiRyohiMeisaiUpdate(index, meisaiInfo, meisaiShubetsuCd) {
	setKaigaiRyohiMeisaiInfo(index, meisaiInfo,meisaiShubetsuCd);
	setDisplayKaigaiRyohiMeisaiData(meisaiShubetsuCd);
	isDirty = true;
}

/**
 * 明細ダイアログを開く
 * 追加(index == -1)、変更(index >= 0)で呼ばれる。
 * @param index				何行目を開くか(0～)、追加なら-1
 * @param shubetsuCd		種別CD(1:交通費、2:出張手当等)
 * @param sourceMeisaiInfo	コピー元の明細MAP。連続追加(複写)以外では付与
 */
function kaigaiRyohiMeisaiDialogOpen(index, shubetsuCd, sourceMeisaiInfo) {
	
	if($("[name=userIdRyohi]").length > 0 && $("[name=userIdRyohi]").val() == ""){
		alert("使用者を選択してください。");
		return;
	}
	
	const identificationKey = "eteam." + $("input[name=denpyouKbn]").val() + "-kaigaiKoutsuuhiMeisai";
	var isEnableInput = $('#enableInput').val() == 'true';
	var dialog = null;
	var parent = $("#dialogMeisai");
	parent.children().remove();

	dlgUrlBase = (shubetsuCd == 1) ? "ryohi_koutsuuhi_meisai" : "ryohi_seisan_shukuhakuryouhoka_nyuuryoku";
	
	//出張手当ダイアログ選択時、使用者が入力されていない場合、エラー
	if (shubetsuCd != 1 && $("input[name=shainNoRyohi]").val() == "") {
		var labelText = $("input[name=shainNoRyohi]").parent().parent().find(".control-label").text();
		alert(labelText.replace("*","") + "を入力してください");
		return;
	}
	
	//----------
	//ダイアログの追加・更新処理
	//----------
	var registFunction = function(type) {
		
		//①dialog内の表示状態をMAPに
 		var meisaiInfo = getDialogMeisaiInfo();
		
		//②dialog内のAJAXイベント(confirm)を呼ぶ
		dialog.load(
			dlgUrlBase + "_confirm",
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
					kaigaiRyohiMeisaiUpdate(index, meisaiInfo, shubetsuCd);
				}
				if (type == "insertClose") {
					//追加して閉じるボタン：明細を追加
					kaigaiRyohiMeisaiAdd(meisaiInfo, shubetsuCd);
				}
				if (type == "insertNextCopy") {
					//連続追加(複写)ボタン：明細を追加、ダイアログをもう一度開く、今の状態を再現
					kaigaiRyohiMeisaiAdd(meisaiInfo, shubetsuCd);
					kaigaiRyohiMeisaiDialogOpen(-1, shubetsuCd, meisaiInfo);
				}
				if (type == "insertNextClear") {
					//連続追加(クリア)ボタン：明細を追加、ダイアログをもう一度開く
					kaigaiRyohiMeisaiAdd(meisaiInfo, shubetsuCd);
					kaigaiRyohiMeisaiDialogOpen(-1, shubetsuCd, null);
				}
				changeInvoiceDenpyou();
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
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	
	var meisai = $("#kaigaiMeisaiList0" + shubetsuCd + " tr.meisai:eq(" + index + ")");
	var view = $("#kaigaiMeisaiList0" + shubetsuCd + " tr.meisaiView:eq(" + index + ")");
	
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
	}

	//----------
	//ダイアログの表示
	//----------
	
	//ダイアログに表示する明細MAPを作る
	var meisaiInfo = null;
	if (0 <= index) {
		//既存明細を開く場合(明細番号・前・次)、該当明細のhidden情報から
		meisaiInfo = getKaigaiRyohiMeisaiInfo(index, shubetsuCd);
	} else {
		//明細追加の場合、基本はnull(まっさら標示)だけど、複写追加の時だけコピー元MAPもらっている
		meisaiInfo = sourceMeisaiInfo;//普通の追加ならnull、複写追加なら複写元
	}
	
	//ダイアログのinitイベントのパラメータMAPを作る
	if (index == -1 && sourceMeisaiInfo == null){
		//追加ボタンの場合は必要キーだけ渡してinitイベント
		var actionParams = {
				denpyouId	:	$("input[name=denpyouId]").val(),
				denpyouKbn	:	$("input[name=denpyouKbn]").val(),
				zeroEnabled	:	$("input[name=karibaraiOn]:checked").length == 1 ? $("input[name=karibaraiOn]:checked").val() : "0",
				userId		:	$("input[name=userIdRyohi]").val(),
				kaigaiFlgRyohi:	"1",
				kazeiFlgRyohi:	$("input[name=denpyouKbn]").val() == "A011" ? $("input[name=kazeiFlgRyohiKaigai]").val() : "",
				kazeiFlgKamoku:	$("input[name=denpyouKbn]").val() == "A011" ? $("input[name=kazeiFlgRyohiKaigaiKamoku]").val() : "",
				dispMode	:	"1",
				kikanFrom	:	getKikanFrom().val(),
				kikanTo		:	getKikanTo().val()
			}
	}
	else{
		meisaiInfo.requestKbn = "notNew";
		//既存明細を開く場合(明細番号・前・次)や複写追加の場合は明細情報全部渡してinitイベント
		var actionParams = meisaiInfo;
	}

	actionParams.init = "true"; //Actionで初回表示識別する
	
	//ダイアログに明細初期表示イベントをロード
	//ロード後に明細MAPをダイアログに反映
	dialog.load(
		dlgUrlBase + "_init",
		actionParams,
		function() {
			setDialogMeisaiInfo(isEnableInput, meisaiInfo);
		}
	);

}

function isEmptyKaigaiTorihiki()
{
	let denpyouKbn = $("input[name=denpyouKbn]").val(); // そもそも不必要に言語をまたぐのは厄介ごとの元なので、JSPにあるタグをわざわざc:ifにはしない
	if(["A011"].includes(denpyouKbn) && !$("input[name=kaigaiShiwakeEdaNoRyohi]").val())
	{
		alert("海外取引を入力してください。");
		return true;
	}
	return false;
}

// 初期化処理
if ($('#enableInput').val()) {
	
	//明細追加ボタン
	$("#kaigaiMeisaiAddButton").click(function(){
		if(isEmptyKaigaiTorihiki())
		{
			return;
		}
		kaigaiRyohiMeisaiDialogOpen(-1, 1, null);
	});
	$("#kaigaiMeisaiAddButton2").click(function(){
		if(isEmptyKaigaiTorihiki())
		{
			return;
		}
		kaigaiRyohiMeisaiDialogOpen(-1, 2, null);
	});

	//明細イベント割り付け
	$("body").on("click","button[name=kaigaiRyohiMeisaiUp]",kaigaiRyohiMeisaiUp); 
	$("body").on("click","button[name=kaigaiRyohiMeisaiDown]",kaigaiRyohiMeisaiDown); 
	$("body").on("click","button[name=kaigaiRyohiMeisaiDelete]",kaigaiRyohiMeisaiDelete); 
	$("body").on("click","button[name=kaigaiRyohiMeisaiCopy]",kaigaiRyohiMeisaiCopy); 
}
</script>