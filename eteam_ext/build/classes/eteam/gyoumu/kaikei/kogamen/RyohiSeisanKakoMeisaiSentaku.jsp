<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- エラー -->
<%@ include file="/jsp/eteam/include/InputError.jsp" %>

<!-- メイン -->
<div id='dialogMain'>

	<!-- 検索条件 -->
	<section id='kakoMeisaiSearchJouken'>
		<div id='kensaku'><form class="form-inline">
			<div class='control-group'>
				<label class="label label-sub">期間</label>
				<input type='text' name='joukenKikanFrom' class='input-small datepicker' value='${su:htmlEscape(joukenKikanFrom)}'> ～ 
				<input type='text' name='jokenKikanTo' class='input-small datepicker' value='${su:htmlEscape(jokenKikanTo)}'>
			</div>
			<div class='control-group'>
				<label class="label label-sub">内容</label>
				<input type='text' name='jokenNaiyou' class='input-xxlarge' value='${su:htmlEscape(jokenNaiyou)}'>
				<button type='button' id='kakoMeisaiSearchButton' class='btn'>検索</button>
			</div>
			<input type="hidden" name="denpyouId" value='${su:htmlEscape(denpyouId)}'>
			<input type="hidden" name="denpyouKbn" value='${su:htmlEscape(denpyouKbn)}'>
			<input type='hidden' name='shubetsuCd' value='${su:htmlEscape(shubetsuCd)}'>
			<input type='hidden' name='kaigaiFlgRyohi' value='${su:htmlEscape(kaigaiFlgRyohi)}'>
			<input type='hidden' name='userId' value='${su:htmlEscape(userId)}'>
			<input type='hidden' name=kazeiFlgRyohi value='${su:htmlEscape(kazeiFlgRyohi)}'>
			<input type='hidden' name=kazeiFlgKamoku value='${su:htmlEscape(kazeiFlgKamoku)}'>
		</form></div>
	</section>

	<!-- 検索結果 -->
	<section id='kakoMeisaiSearchResult'>
	    <div>
<c:if test="${fn:length(kakoMeisaiList) > 0}" >
		<table class='table-bordered table-condensed'>
			<thead>
				<tr>
					<th style='min-width:70px;'>種別</th>
					<th style='min-width:70px;'>期間</th>
					<th>内容</th>
					<th>備考</th>
					<th style='min-width:70px;'>単価(円)</th>
				</tr>
			</thead>
			<tbody>
<c:forEach var="record" items="${kakoMeisaiList}">
				<tr>
					<td><a class='link syubetsu' data-syubetsuCd='${record.shubetsu_cd}' 
												data-shubetsu1='${su:htmlEscape(record.shubetsu1)}' 
												data-shubetsu2='${su:htmlEscape(record.shubetsu2)}' 
												data-kikanFrom='${record.kikan_from}' 
												data-kikanTo='${record.kikan_to}' 
												data-shouhyouShoruiHissuFlg='${record.shouhyou_shorui_hissu_flg }'
												data-koutsuuShudan='${record.koutsuu_shudan}' 
												data-oufukuFlg='${record.oufuku_flg}'
												data-jidounyuuryokuFlg='${record.jidounyuuryoku_flg}'
												data-nissuu='${record.nissuu}'
												data-heishu='${record.heishu_cd}'
												data-rate='${record.rate}'
												data-gaika='${record.gaika}'
												data-tani='${record.currency_unit}'
												data-tanka='${su:htmlEscape(record.tanka)}'
												data-suuryouNyuryokuType='${su:htmlEscape(record.suuryou_nyuryoku_type)}'
												data-suuryou='${su:htmlEscape(record.suuryou)}'
												data-suuryouKigou='${su:htmlEscape(record.suuryouKigou)}'
												data-hayaFlg='${record.haya_flg}'
												data-yasuFlg='${record.yasu_flg}'
												data-rakuFlg='${record.raku_flg}'>
						${su:htmlEscape(record.shubetsu1)}</a></td>
					<td class='kikan'>${su:htmlEscape(record.kikan)}</td>
					<td class='kekkaNaiyou'>${su:htmlEscape(record.naiyou)}</td>
					<td class='kekkaBikou'>${su:htmlEscape(record.bikou)}</td>
					<td class='kekkaTanka text-r'>${record.tanka}</td>
				</tr>
</c:forEach>
	        </tbody>
	      </table>
</c:if>
	    </div>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
//入力補助
commonInit($("#dialogMain"));

/**
 * 検索ボタン押下時Function
 */
$("#kakoMeisaiSearchButton").click(function(){
	var div = $("#kensaku");
	var kikanFrom	= div.find("input[name=joukenKikanFrom]").val();
	var kikanTo		= div.find("input[name=jokenKikanTo]").val();
	var naiyou		= div.find("input[name=jokenNaiyou]").val();
	var denpyouId	= div.find("input[name=denpyouId]").val();
	var denpyouKbn  = div.find("input[name=denpyouKbn]").val();
	var shubetsuCd  = div.find("input[name=shubetsuCd]").val();
	var kaigaiFlgRyohi  = div.find("input[name=kaigaiFlgRyohi]").val();
	var userId      = div.find("input[name=userId]").val();
	
	$("#dialog").empty();
	$("#dialog").load("ryohi_seisan_kakoMeisai_sentaku_kensaku?joukenKikanFrom=" + encodeURI(kikanFrom) + "&jokenKikanTo=" + encodeURI(kikanTo) +"&jokenNaiyou=" + encodeURI(naiyou) + "&denpyouId=" + encodeURI(denpyouId) + "&denpyouKbn=" + encodeURI(denpyouKbn) + "&shubetsuCd=" + encodeURI(shubetsuCd) + "&kaigaiFlgRyohi=" + encodeURI(kaigaiFlgRyohi) + "&userId=" + encodeURI(userId));
});

/**
 * リンク選択時
 */
 $("a.syubetsu").click(function(){
	var a = $(this);
	var shubetsuCd = $("#kakoMeisaiSearchJouken").find("input[name=shubetsuCd]").val();

	//明細の中身作る
	var denpyouTmp = new Object();
	denpyouTmp.shubetsuCd			= shubetsuCd;
	denpyouTmp.shubetsu1			= a.attr("data-shubetsu1");
	denpyouTmp.shubetsu2			= a.attr("data-shubetsu2");
	//denpyouTmp.kikanFrom			= a.attr("data-kikanFrom");
	//denpyouTmp.kikanTo			= a.attr("data-kikanTo");
	denpyouTmp.koutsuuShudan		= a.attr("data-koutsuuShudan");
	denpyouTmp.shouhyouShoruiHissuFlg
									= a.attr("data-shouhyouShoruiHissuFlg");
	denpyouTmp.ryoushuushoSeikyuushoTouFlg
									= ("1" === shubetsuCd)? "" : "0";
	denpyouTmp.naiyou				= a.closest("tr").find("td.kekkaNaiyou").text();
	denpyouTmp.bikou				= a.closest("tr").find("td.kekkaBikou").text();
	denpyouTmp.oufukuFlg			= a.attr("data-oufukuFlg");
	denpyouTmp.jidounyuuryokuFlg	= a.attr("data-jidounyuuryokuFlg");
	denpyouTmp.nissuu				= a.attr("data-nissuu");
	denpyouTmp.tanka				= a.attr("data-tanka");
	denpyouTmp.suuryouNyuryokuType	= a.attr("data-suuryouNyuryokuType");
	denpyouTmp.suuryou				= a.attr("data-suuryou");
	denpyouTmp.suuryouKigou			= a.attr("data-suuryouKigou");
	denpyouTmp.heishuCdRyohi		= a.attr("data-heishu");
	denpyouTmp.rateRyohi			= a.attr("data-rate");
	denpyouTmp.gaikaRyohi			= a.attr("data-gaika");
	denpyouTmp.taniRyohi			= a.attr("data-tani");
	denpyouTmp.kaigaiFlgRyohi		= $("#kakoMeisaiSearchJouken").find("input[name=kaigaiFlgRyohi]").val();
	denpyouTmp.kazeiFlgRyohi		= $("#kakoMeisaiSearchJouken").find("input[name=kazeiFlgRyohi]").val();
	denpyouTmp.kazeiFlgKamoku		= $("#kakoMeisaiSearchJouken").find("input[name=kazeiFlgKamoku]").val();
	denpyouTmp.hayaFlg				= a.attr("data-hayaFlg");
	denpyouTmp.yasuFlg				= a.attr("data-yasuFlg");
	denpyouTmp.rakuFlg				= a.attr("data-rakuFlg");
	
	//親ダイアログ(交通費 or 日当等)に反映→入力制御再実行
	setDialogMeisaiInfo(true, denpyouTmp);
	if (shubetsuCd == "1") {
		reselectKoutsuuShudan();
		inputSeigyo();
	} else {
		//削除済みマスターでも一度クライアントではプルダウンに入るようにする。サーバー側のリロードで存在しないマスターならプルダウンからは消える
		putNittouShubetsu(1, denpyouTmp.shubetsu1, true);
		putNittouShubetsu(2, denpyouTmp.shubetsu2, true);
		reloadDialog();
	}
	
	$("#dialog").children().remove();
	$("#dialog").dialog("close");
});
</script>
