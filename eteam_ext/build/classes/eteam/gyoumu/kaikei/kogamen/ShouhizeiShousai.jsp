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
	<!-- 入力フィールド -->
	<section>
		<div class='control-group'>
			<input class="nonChkDirty"type='hidden' name="denpyouId" value='${su:htmlEscape(denpyouId)}'>
			<input class="nonChkDirty"type='hidden' name="denpyouKbn" value='${su:htmlEscape(denpyouKbn)}'>
			<input class="nonChkDirty"type='hidden' name="karibaraiZeigaku10Percent" value='${su:htmlEscape(karibaraiZeigaku10Percent)}'>
			<input class="nonChkDirty"type='hidden' name="karibaraiZeigaku10PercentMenzei80" value='${su:htmlEscape(karibaraiZeigaku10PercentMenzei80)}'>
			<input class="nonChkDirty"type='hidden' name="karibaraiZeigaku8Percent" value='${su:htmlEscape(karibaraiZeigaku8Percent)}'>
			<input class="nonChkDirty"type='hidden' name="karibaraiZeigaku8PercentMenzei80" value='${su:htmlEscape(karibaraiZeigaku8PercentMenzei80)}'>
			<input class="nonChkDirty"type='hidden' name="kariukeZeigaku10Percent" value='${su:htmlEscape(kariukeZeigaku10Percent)}'>
			<input class="nonChkDirty"type='hidden' name="kariukeZeigaku10PercentTsumiage" value='${su:htmlEscape(kariukeZeigaku10PercentTsumiage)}'>
			<input class="nonChkDirty"type='hidden' name="kariukeZeigaku10PercentWarimodoshi" value='${su:htmlEscape(kariukeZeigaku10PercentWarimodoshi)}'>
			<input class="nonChkDirty"type='hidden' name="kariukeZeigaku8Percent" value='${su:htmlEscape(kariukeZeigaku8Percent)}'>
			<input class="nonChkDirty"type='hidden' name="kariukeZeigaku8PercentTsumiage" value='${su:htmlEscape(kariukeZeigaku8PercentTsumiage)}'>
			<input class="nonChkDirty"type='hidden' name="kariukeZeigaku8PercentWarimodoshi" value='${su:htmlEscape(kariukeZeigaku8PercentWarimodoshi)}'>
		</div>
		<div style='display: flex;'>
			<table class='table-bordered table-condensed' style='margin-right:10px;'>
				<thead>
					  <tr>
					    <th><nobr>仮払消費税</nobr></th>
					    <th><nobr>合計</nobr></th>
					  </tr>
				  </thead>
				<tbody>
				  <tr>
				    <td>10％</td>
				    <td class="text-r" id="karibaraiZeigaku10Percent">${su:htmlEscape(karibaraiZeigaku10Percent)}</td>
				  </tr>
				  <tr>
				    <td>10％（仕入税額控除経過措置割合（80％））</td>
				    <td class="text-r" id="karibaraiZeigaku10PercentMenzei80">${su:htmlEscape(karibaraiZeigaku10PercentMenzei80)}</td>
				  </tr>
				  <tr>
				    <td>軽減税率8%</td>
				    <td class="text-r" id="karibaraiZeigaku8Percent">${su:htmlEscape(karibaraiZeigaku8Percent)}</td>
				  </tr>
				  <tr>
				    <td>軽減税率8%（仕入税額控除経過措置割合（80％））</td>
				    <td class="text-r" id="karibaraiZeigaku8PercentMenzei80">${su:htmlEscape(karibaraiZeigaku8PercentMenzei80)}</td>
				  </tr>
			  </tbody>
			</table>
			<c:if test='${denpyouKbn eq "A007" || denpyouKbn eq "A008" || fn:startsWith(denpyouKbn, "Z")}'>
			<table class='table-bordered table-condensed' style='margin-left:10px;'>
				<thead>
				    <tr>
				      <th><nobr>仮受消費税</nobr></th>
				      <th><nobr>合計</nobr></th>
				    </tr>
			    </thead>
			    <tbody>
				    <tr>
				      <td>10％</td>
				      <td class="text-r" id="kariukeZeigaku10Percent">${su:htmlEscape(kariukeZeigaku10Percent)}</td>
				    </tr>
				    <tr>
				      <td>10％（積上げ計算）</td>
				      <td class="text-r" id="kariukeZeigaku10PercentTsumiage">${su:htmlEscape(kariukeZeigaku10PercentTsumiage)}</td>
				    </tr>
				    <tr>
				      <td>10％（割戻し計算）</td>
				      <td class="text-r" id="kariukeZeigaku10PercentWarimodoshi">${su:htmlEscape(kariukeZeigaku10PercentWarimodoshi)}</td>
				    </tr>
				    <tr>
				      <td>軽減税率8%</td>
				      <td class="text-r" id="kariukeZeigaku8Percent">${su:htmlEscape(kariukeZeigaku8Percent)}</td>
				    </tr>
				    <tr>
				      <td>軽減税率8%（積上げ計算）</td>
				      <td class="text-r" id="kariukeZeigaku8PercentTsumiage">${su:htmlEscape(kariukeZeigaku8PercentTsumiage)}</td>
				    </tr>
				    <tr>
				      <td>軽減税率8%（割戻し計算）</td>
				      <td class="text-r" id="kariukeZeigaku8PercentWarimodoshi">${su:htmlEscape(kariukeZeigaku8PercentWarimodoshi)}</td>
				    </tr>
			    </tbody>
			  </table>
			  </c:if>
		  </div>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>

async function setKingaku(){
	// タグ
	let karibaraiZeigaku10PercentTag = $("input[name=karibaraiZeigaku10Percent]");
	let karibaraiZeigaku10PercentMenzei80Tag = $("input[name=karibaraiZeigaku10PercentMenzei80]");
	let karibaraiZeigaku8PercentTag = $("input[name=karibaraiZeigaku8Percent]");
	let karibaraiZeigaku8PercentMenzei80Tag = $("input[name=karibaraiZeigaku8PercentMenzei80]");
	let kariukeZeigaku10PercentTag = $("input[name=kariukeZeigaku10Percent]");
	let kariukeZeigaku10PercentTsumiageTag = $("input[name=kariukeZeigaku10PercentTsumiage]");
	let kariukeZeigaku10PercentWarimodoshiTag = $("input[name=kariukeZeigaku10PercentWarimodoshi]");
	let kariukeZeigaku8PercentTag = $("input[name=kariukeZeigaku8Percent]");
	let kariukeZeigaku8PercentTsumiageTag = $("input[name=kariukeZeigaku8PercentTsumiage]");
	let kariukeZeigaku8PercentWarimodoshiTag = $("input[name=kariukeZeigaku8PercentWarimodoshi]");
	
	// 金額計算用変数
	let karibaraiZeigaku10Percent = 0;
	let karibaraiZeigaku10PercentMenzei80 = 0;
	let karibaraiZeigaku8Percent = 0;
	let karibaraiZeigaku8PercentMenzei80 = 0;
	let kariukeZeigaku10Percent = 0;
	let kariukeZeigaku10PercentTsumiage = 0;
	let kariukeZeigaku10PercentWarimodoshi = 0;
	let kariukeZeigaku8Percent = 0;
	let kariukeZeigaku8PercentTsumiage = 0;
	let kariukeZeigaku8PercentWarimodoshi = 0;
	
	// 諸々のリスト
	let kamokuCdList = [];
	let isKarikataList = [];
	let kazeiKbnList = [];
	let jigyoushaKbnList = [];
	let kingakuList = [];
	let zeigakuList = [];
	let zeiritsuList = [];
	let zeigakuKeisanList = [];
	
	// （経費）明細借方を見るもの
	// A001 経費立替精算
	// A003 請求書払い申請
	// A013 支払依頼申請
	// A009 自動引落伝票
	// A004 出張旅費精算（仮払精算）
	// A011 海外出張旅費精算（仮払精算）
	<c:if test = '${denpyouKbn eq "A001" || denpyouKbn eq "A003" || denpyouKbn eq "A013" || denpyouKbn eq "A009" || denpyouKbn eq "A004" || denpyouKbn eq "A011"}'>
	$.each(getMeisaiList(), function(ii, meisaiInfo) {
		let shiharaiKingaku = (meisaiInfo["shiharaiKingaku"])
		? parseInt(meisaiInfo["shiharaiKingaku"].split(',').join(''))
		: (meisaiInfo["meisaiKingaku"])
			? parseInt(meisaiInfo["meisaiKingaku"].split(',').join(''))
			: 0;
		
		let shouhizeigaku = (meisaiInfo["shouhizeigaku"]) ? parseInt(meisaiInfo["shouhizeigaku"].split(',').join('')) : 0;
	
		kamokuCdList.push(meisaiInfo["kamokuCdRyohi"] ?? meisaiInfo["kamokuCd"]);
		isKarikataList.push(true);
		kazeiKbnList.push(meisaiInfo["kazeiKbn"] ?? meisaiInfo["kariKazeiKbn"] ?? meisaiInfo["kazeiKbnRyohi"] ?? "0"); // 各明細に要追加
		jigyoushaKbnList.push($("input[name=jigyoushaKbn]:checked")?.val() ??meisaiInfo["jigyoushaKbn"] ?? meisaiInfo["kariJigyoushaKbn"] ?? "0"); // 各明細に要追加
		kingakuList.push(shiharaiKingaku);
		zeigakuList.push(shouhizeigaku);
		zeiritsuList.push(meisaiInfo["zeiritsu"] ?? meisaiInfo["zeiritsuRyohi"]);
		zeigakuKeisanList.push(""); // 関係しない区分なので空
	});
	</c:if>
	
	// 伝票本体借方+旅費明細借方
	// A004 出張旅費精算（仮払精算）
	// A011 海外出張旅費精算（仮払精算）
	// A010 交通費精算
	<c:if test = '${denpyouKbn eq "A004" || denpyouKbn eq "A011" || denpyouKbn eq "A010"}'>
	// 基本**Ryohiで統一
	// 国内ものを持ってくる
	let map1 = $.map($("#meisaiList01 tr.meisai"), function(tr, ii) {
		var meisaiInfo = getRyohiMeisaiInfo(ii, 1);
		if (!meisaiInfo['shubetsuCd']) { // 隠れ行ではshubetsuCdがundefinedなので
			return null;
		}
		return meisaiInfo;
	});
	let map2 = $.map($("#meisaiList02 tr.meisai"), function(tr, ii) {
		var meisaiInfo = getRyohiMeisaiInfo(ii, 2);
		if (!meisaiInfo['shubetsuCd']) {
			return null;
		}
		return meisaiInfo;
	});

	let kokunaiMap = $.merge(map1 ?? {}, map2 ?? {});

	$.each(kokunaiMap, function(ii, meisaiInfo) {
		let shiharaiKingaku = (meisaiInfo["meisaiKingaku"])
			? parseInt(meisaiInfo["meisaiKingaku"].split(',').join(''))
			: 0;
		
		let shouhizeigaku = (meisaiInfo["shouhizeigaku"]) ? parseInt(meisaiInfo["shouhizeigaku"].split(',').join('')) : 0;
		
		kamokuCdList.push($("input[name=kamokuCdRyohi]")?.val());
		isKarikataList.push(true);
		//不課税は合算しないようにするのに、課税区分で判定するため明確に取得
		kazeiKbnList.push($("#kaikeiContent").find("select[name=kazeiKbnRyohi]")?.val() ?? "0");
		//TODO 海外課税区分はこれでは取得できない　海外課税区分で課税系を使用するようになった時に再修正が必要
		jigyoushaKbnList.push(meisaiInfo["jigyoushaKbn"] ?? "0");
		kingakuList.push(shiharaiKingaku);
		zeigakuList.push(shouhizeigaku);
		zeiritsuList.push($("select[name=zeiritsuRyohi]")?.val());
		zeigakuKeisanList.push(""); // 関係しない区分なので空
	});

	// 無印旅費の差引対策
	if($("[name=denpyouKbn]").val() == "A004" && $("[name='sashihikiKingaku']").is(':visible')) {
		kamokuCdList.push($("input[name=kamokuCdRyohi]")?.val());
		isKarikataList.push(true);
		//不課税は合算しないようにするのに、課税区分で判定するため明確に取得
		kazeiKbnList.push($("#kaikeiContent").find("select[name=kazeiKbnRyohi]")?.val() ?? "0");
		jigyoushaKbnList.push("0"); // 固定
		kingakuList.push($("[name='sashihikiKingaku']").val().replaceAll(",",""));
		zeigakuList.push($("[name='sashihikiZeigakuForShousai']").val().replaceAll(",",""));
		zeiritsuList.push($("select[name=zeiritsuRyohi]")?.val());
		zeigakuKeisanList.push(""); // 関係しない区分なので空
	}
	</c:if>

	// 伝票本体借方（総合付替は付替元）
	// A006 通勤定期申請
	// A008 総合付替伝票
	<c:if test = '${denpyouKbn eq "A006" || denpyouKbn eq "A008"}'>
	// 基本は無印kamokuCd, motoKamokuCdに注意
	let total = 0;
	$("input[name=sakiShouhizeigaku]")?.each(function() {
	    let value = parseFloat($(this).val());
	    if (!isNaN(value)) {
	        total += value;
	    }
	});
	
	kamokuCdList.push($("input[name=kamokuCd]")?.val() ?? $("input[name=motoKamokuCd]")?.val());
	isKarikataList.push(true);
	kazeiKbnList.push($("select[name=kazeiKbn]")?.val() ?? $("select[name=motoKazeiKbn]")?.val() ?? "0");
	jigyoushaKbnList.push($("input[name=jigyoushaKbn]:checked")?.val() ?? $("select[name=motoJigyoushaKbn]")?.find("option:selected").val() ?? "0"); // 要追加
	kingakuList.push($("input[name=kingaku]").val().split(',').join(''));
	zeigakuList.push($("input[name=shouhizeigaku]")?.val().split(',').join('') ?? total ?? 0);
	zeiritsuList.push($("select[name=zeiritsu]").find("option:selected").val());
	zeigakuKeisanList.push($("select[name=motoZeigakuHoushiki]")?.find("option:selected").val() ?? ""); // 付替以外では関係しない区分なので空
	</c:if>
	
	// 本体・貸借両方
	// A007 振替伝票
	<c:if test = '${denpyouKbn eq "A007"}'>
	// kariKamokuCd, kashiKamokuCd
	let taishakuList = [ "kari", "kashi" ];
	for(const taishaku of taishakuList)
	{
		kamokuCdList.push($("input[name=" + taishaku + "KamokuCd]").val());
		isKarikataList.push(taishaku == "kari");
		kazeiKbnList.push($("select[name=" + taishaku + "KazeiKbn]")?.find("option:selected").val() ?? "0"); // 要追加
		jigyoushaKbnList.push($("input[name=" + taishaku + "JigyoushaKbn]:checked")?.val() ?? "0"); // 要追加
		kingakuList.push($("input[name=kingaku]").val().split(',').join(''));
		zeigakuList.push($("input[name=" + taishaku + "Shouhizeigaku]").val().split(',').join(''));
		zeiritsuList.push(Number($("select[name=" + taishaku + "Zeiritsu]").find("option:selected").val()));
		zeigakuKeisanList.push($("select[name=" + taishaku + "ZeigakuHoushiki]")?.val() ?? "0"); // 要追加
	}
	</c:if>
	
	// 付替先→明細貸方
	// A008 総合付替伝票
	<c:if test = '${denpyouKbn eq "A008"}'>
	// sakiKamokuCd
	$("input[name=sakiKamokuCd]").each(function(index) {
	    kamokuCdList.push($("input[name=sakiKamokuCd]")?.eq(index)?.val() ?? "0");
	    isKarikataList.push(false);
	    kazeiKbnList.push($("select[name=sakiKazeiKbn]")?.eq(index)?.val() ?? "0");
	    jigyoushaKbnList.push($("select[name=sakiJigyoushaKbn]")?.eq(index)?.val() ?? "0"); // 要追加
	    kingakuList.push($("input[name=sakiKingaku]")?.eq(index)?.val().replaceAll(",",""));
	    zeigakuList.push($("input[name=sakiShouhizeigaku]")?.eq(index)?.val().replaceAll(",","") ?? 0);
	    zeiritsuList.push(Number($("select[name=zeiritsu]").find("option:selected").val()));
	    zeigakuKeisanList.push($("select[name=sakiZeigakuHoushiki]")?.eq(index)?.val() ?? "0"); // 要追加
	});
	</c:if>

	// TODO 拠点の考慮（一旦eteam側では無視）

	//得たリストをもとに集計
	let listIndex = 0;
	for (const kamokuCd of kamokuCdList) {
	  let index = listIndex;
	  listIndex++;

	  // 処理グループ取得
	 let shoriGroup = await getKamokuShoriGroup(kamokuCd);
	
	  let zeigaku = shoriGroup < 21 ? zeigakuList[index] : kingakuList[index];
	
	  // 0円と空値は無視
	  if (zeigaku == null || zeigaku.valueOf() === 0) {
	    continue;
	  }
	  
	  //税込・税抜系以外の消費税は集計しない
	  //以降処理に合わせて0埋めしてない課税区分も確認
	  if(!["001", "002", "011", "012", "013", "014", "1", "2", "11", "12", "13", "14"].includes(kazeiKbnList[index])){
		  continue;
	  }
	
	  let isKarikata = isKarikataList[index];
	  let isKaribarai =
	    [5, 6, 10, 21].includes(shoriGroup) ||
	    ([2, 7, 8].includes(shoriGroup) &&
	      ["011", "013", "11", "13"].includes(kazeiKbnList[index]));
	  let is10Percent = zeiritsuList[index] == 10;
	  let isMenzei = isKaribarai && jigyoushaKbnList[index] == "1";
	  let isWarimodoshi = zeigakuKeisanList[index] == "0";
	
	  let multiplier = isKarikata == isKaribarai ? 1 : -1;
	
	  zeigaku = zeigaku * multiplier;
	
	  if (isKaribarai) {
	    if (is10Percent) {
	      if (isMenzei) {
	        karibaraiZeigaku10PercentMenzei80 += zeigaku;
	        continue;
	      }
	      karibaraiZeigaku10Percent += zeigaku;
	      continue;
	    }
	    if (isMenzei) {
	      karibaraiZeigaku8PercentMenzei80 += zeigaku;
	      continue;
	    }
	    karibaraiZeigaku8Percent += zeigaku;
	    continue;
	  }
	  if (is10Percent) {
	    kariukeZeigaku10Percent += zeigaku;
	    if (isWarimodoshi) {
	      kariukeZeigaku10PercentWarimodoshi += zeigaku;
	      continue;
	    }else if(zeigakuKeisanList[index] == "9"){
	    	continue;
	    }
	    kariukeZeigaku10PercentTsumiage += zeigaku;
	    continue;
	  }
	  kariukeZeigaku8Percent += zeigaku;
	  if (isWarimodoshi) {
	    kariukeZeigaku8PercentWarimodoshi += zeigaku;
	    continue;
	  }else if(zeigakuKeisanList[index] == "9"){
	    continue;
      }
	  kariukeZeigaku8PercentTsumiage += zeigaku;
	}
	
	// 金額の格納
	karibaraiZeigaku10PercentTag?.putMoney(karibaraiZeigaku10Percent);
	karibaraiZeigaku10PercentMenzei80Tag?.putMoney(karibaraiZeigaku10PercentMenzei80);
	karibaraiZeigaku8PercentTag?.putMoney(karibaraiZeigaku8Percent);
	karibaraiZeigaku8PercentMenzei80Tag?.putMoney(karibaraiZeigaku8PercentMenzei80);
	kariukeZeigaku10PercentTag?.putMoney(kariukeZeigaku10Percent);
	kariukeZeigaku10PercentTsumiageTag?.putMoney(kariukeZeigaku10PercentTsumiage);
	kariukeZeigaku10PercentWarimodoshiTag?.putMoney(kariukeZeigaku10PercentWarimodoshi);
	kariukeZeigaku8PercentTag?.putMoney(kariukeZeigaku8Percent);
	kariukeZeigaku8PercentTsumiageTag?.putMoney(kariukeZeigaku8PercentTsumiage);
	kariukeZeigaku8PercentWarimodoshiTag?.putMoney(kariukeZeigaku8PercentWarimodoshi);

    // 表示へのセット
    $('td.text-r').each(function() {
      $(this).text($('input[name="' + $(this).attr('id') + '"]').val().replace(".00",""));
    });
}

setKingaku();
</script>

