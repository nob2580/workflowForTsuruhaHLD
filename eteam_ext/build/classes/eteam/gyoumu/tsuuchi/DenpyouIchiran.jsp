<%@ page import="eteam.symbol.EteamSymbol"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!DOCTYPE html>
<html lang="ja">
	<head>
		<meta charset="utf-8">
		<title>伝票一覧｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
	</head>

	<body>
    	<div id="wrap">

    		<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
  				<h1>伝票一覧</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<input type='hidden' name='beforeSelect' value='${su:htmlEscape(beforeSelect)}' />
				<input type='hidden' name='focusChangeFlg' value='0' />

				<div id='main' class='form-horizontal'>
				<form id='myForm'>
					<input type="hidden" name="pageNo" value="${su:htmlEscape(pageNo)}">
					<input type="hidden" name="sortKbn" value="${su:htmlEscape(sortKbn)}">

					<!-- 検索枠 -->
					<section>
						<input type='hidden' name='shousaiKensakuHyoujiFlg' value='${su:htmlEscape(shousaiKensakuHyoujiFlg)}' />
						<input type='hidden' name='kensakuJoukenHyoujiFlg' value='${su:htmlEscape(kensakuJoukenHyoujiFlg)}'/>
						<h2>検索条件
						<button type='button' id='kensakuOpen' name='kensakuOpen' class='btn btn-mini' <c:if test='${kensakuJoukenHyoujiFlg eq "1"}'>style='display:none'</c:if>>+</button>
						<button type='button' id='kensakuClose' name='kensakuClose' class='btn btn-mini' <c:if test='${kensakuJoukenHyoujiFlg eq "0"}'>style='display:none'</c:if>>-</button>
						</h2>
						<div id="kensakuList" class='no-more-tables' <c:if test='${kensakuJoukenHyoujiFlg eq "0"}'>style='display:none'</c:if> >
							<div class='control-group'>
								<label class='control-label'>関連伝票</label>
									<div class='controls'>
										<select name='kanrenDenpyou' class='input-inline'>
											<option></option>
											<c:forEach var='record' items='${kanrenDenpyouList}'>
												<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq kanrenDenpyou}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
											</c:forEach>
										</select>
									</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>伝票ID</label>
									<div class='controls'>
										<input type='text' name='kensakuDenpyouId' class='input input-inline' value='${su:htmlEscape(kensakuDenpyouId)}'>
										<label class='label'>伝票番号</label>
										<input type='text' name='kensakuSerialNo' class='input-small input-inline zeropadding' maxlength='8' value='${su:htmlEscape(kensakuSerialNo)}'>
									</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>状態</label>
									<div class='controls'>
										<select name='kensakuJoutai' class='input-medium input-inline'>
											<option></option>
											<c:forEach var='record' items='${denpyouJoutaiList}'>
												<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq kensakuJoutai}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
											</c:forEach>
										</select>
										<label class='label'>業務種別</label>
										<select name='kensakuGyoumuShubetsu' class='input-medium input-inline'>
												<option></option>
											<c:forEach var='record' items='${gyoumuShubetsuList}'>
												<option value='${su:htmlEscape(record.gyoumu_shubetsu)}' <c:if test='${record.gyoumu_shubetsu eq kensakuGyoumuShubetsu}'>selected</c:if>>${su:htmlEscape(record.gyoumu_shubetsu)}</option>
											</c:forEach>
										</select>
										<label class='label'>伝票種別</label>
										<select name='kensakuDenpyouShubetsu' class='input-large input-inline' onChange="changeDenpyouKbn()">
											<option></option>
											<c:forEach var='record' items='${denpyouShubetsuList}'>
												<option value='${su:htmlEscape(record.denpyou_kbn)}' <c:if test='${record.denpyou_kbn eq kensakuDenpyouShubetsu}'>selected</c:if>>${su:htmlEscape(record.denpyou_shubetsu)}</option>
											</c:forEach>
										</select>
										種別絞込<input type='text' name='dpkbSibori' class='input-small input-inline' value='${su:htmlEscape(dpkbSibori)}' onChange="changeDenpyouShubetsuSiborikomi()">
										<span id='shuukei'>
										<label class='label'>集計</label>
										<input type="checkbox" id='shuukeiKihyouBumon' name='shuukeiKihyouBumon' value='1' <c:if test='${"1" eq shuukeiKihyouBumon}'>checked</c:if>>起票部門
										<input type="checkbox" id='shuukeiKihyousha' name='shuukeiKihyousha' value='1' <c:if test='${"1" eq shuukeiKihyousha}'>checked</c:if>>起票者
										</span>
									</div>
							</div>
<c:if test="${yosanKannriFlg}">
							<div class='control-group'>
								<label class='control-label'>起案番号</label>
									<div class='controls'>
										<input type='text' name='kianBangouNendo' class='input-mini input-inline' value='${su:htmlEscape(kianBangouNendo)}'>
										<input type='text' name='kianBangouRyakugou' class='input-small input-inline' value='${su:htmlEscape(kianBangouRyakugou)}'>
										<nobr><button type='button' class='btn btn-mini input-inline' id='kianBangouSearch'>選択</button></nobr>
										<input type='text' name='kianBangou' class='input-mini input-inline' value='${su:htmlEscape(kianBangou)}'>号
										<label class='label'>起案番号 範囲指定</label>
										<input type='hidden' name='kianBangouNendoFromTo' value='${su:htmlEscape(kianBangouNendoFromTo)}'>
										<input type='text' name='kianBangouRyakugouFromTo' class='input-small input-inline' value='${su:htmlEscape(kianBangouRyakugouFromTo)}'>
										<nobr><button type='button' class='btn btn-mini input-inline' id='kianBangouFromToSearch'>選択</button></nobr>
										<input type='text' name='kianBangouFrom' class='input-mini input-inline' value='${su:htmlEscape(kianBangouFrom)}'>号
										～
										<input type='text' name='kianBangouTo' class='input-mini input-inline' value='${su:htmlEscape(kianBangouTo)}'>号
									</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>起案番号採番</label>
									<div class='controls'>
										<select name='kianBangouInput' class='input-small input-inline'>
											<c:forEach var='record' items='${kianBangouInputList}'>
												<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq kianBangouInput}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
											</c:forEach>
										</select>
										<label class='label'>起案終了</label>
										<select name='kianBangouEnd' class='input-small input-inline'>
											<c:forEach var='record' items='${kianBangouEndList}'>
												<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq kianBangouEnd}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
											</c:forEach>
										</select>
										<label class='label'>起案番号運用</label>
										<select name='kianBangouUnyou' class='input-small input-inline'>
											<c:forEach var='record' items='${kianBangouUnyouList}'>
												<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq kianBangouUnyou}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
											</c:forEach>
										</select>
										<label class='label'>起案番号あり且つ支出依頼なし分抽出</label>
										<input type="checkbox" id="kianBangouNoShishutsuIrai" name="kianBangouNoShishutsuIrai" value="1" <c:if test='${"1" eq kianBangouNoShishutsuIrai}'>checked</c:if>>
									</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>予算執行伝票種別</label>
									<div class='controls'>
										<c:forEach var='record' items='${yosanshikkouShubetsuList}'>
											<input type="checkbox" name="yosanshikkouShubetsu" value='${su:htmlEscape(record.naibu_cd)}'<c:if test='${su:htmlEscape(record.naibu_cd) eq yosanshikkouShubetsu[0] or su:htmlEscape(record.naibu_cd) eq yosanshikkouShubetsu[1] or su:htmlEscape(record.naibu_cd) eq yosanshikkouShubetsu[2] or su:htmlEscape(record.naibu_cd) eq yosanshikkouShubetsu[3]}'>checked</c:if>>${su:htmlEscape(record.name)}<span>　</span>
										</c:forEach>
<c:if test='${yosanCheckNengetsuFlg}'>
										<label class='label'>予算執行対象月</label>
										<select name='yosanCheckNengetsuFrom' class='input-medium input-inline'>
											<option value='' <c:if test='${record.key eq yosanCheckNengetsuFrom}'>selected</c:if>></option>
											<c:forEach var='record' items='${yosanCheckNengetsuList}'>
												<option value='${su:htmlEscape(record.key)}' <c:if test='${record.key eq yosanCheckNengetsuFrom}'>selected</c:if>>${su:htmlEscape(record.val)}</option>
											</c:forEach>
										</select>
										～
										<select name='yosanCheckNengetsuTo' class='input-medium input-inline'>
											<option value='' <c:if test='${record.key eq yosanCheckNengetsuTo}'>selected</c:if>></option>
											<c:forEach var='record' items='${yosanCheckNengetsuList}'>
												<option value='${su:htmlEscape(record.key)}' <c:if test='${record.key eq yosanCheckNengetsuTo}'>selected</c:if>>${su:htmlEscape(record.val)}</option>
											</c:forEach>
										</select>
</c:if>
									</div>
							</div>
</c:if>
							<div class='control-group'>

								<label class='control-label'>起票日付</label>
									<div class='controls'>
										<input type='text' name='kihyouBiFrom' class='input-small datepicker input-inline' value='${su:htmlEscape(kihyouBiFrom)}'>
										～
										<input type='text' name='kihyouBiTo' class='input-small datepicker input-inline' value='${su:htmlEscape(kihyouBiTo)}'>
										<label class='label'>起票者</label>
										<nobr>所属部門</nobr>
										<input type='text' name='kensakuKihyouShozokuBumonCd' class='input-small input-inline' value='${su:htmlEscape(kensakuKihyouShozokuBumonCd)}'>
										<input type='text' name='kensakuKihyouShozokuBumonName' class='input-medium input-inline' value='${su:htmlEscape(kensakuKihyouShozokuBumonName)}'>
										<nobr><button type='button' class='btn btn-mini input-inline' id='kihyouSyozokuSearch'>選択</button></nobr>
										<nobr>社員</nobr>
										<input type='text' name='kensakuKihyouShozokuUserShainNo' class='input-small input-inline' value='${su:htmlEscape(kensakuKihyouShozokuUserShainNo)}'>
										<nobr>名前</nobr>
										<input type='text' name='kensakuKihyouShozokuUserName' class='input-small input-inline' value='${su:htmlEscape(kensakuKihyouShozokuUserName)}'>
										<nobr><button type='button' class='btn btn-mini input-inline' id='kihyouNameSearch'>選択</button></nobr>
									</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>承認日付</label>
									<div class='controls'>
										<input type='text' name='shouninBiFrom' class='input-small datepicker input-inline' value='${su:htmlEscape(shouninBiFrom)}'>
										～
										<input type='text' name='shouninBiTo' class='input-small datepicker input-inline' value='${su:htmlEscape(shouninBiTo)}'>
										<label class='label'>承認者</label>
										<nobr>所属部門</nobr>
										<input type='text' name='shouninsyaShozokuBumonCd' class='input-small input-inline' value='${su:htmlEscape(shouninsyaShozokuBumonCd)}'>
										<input type='text' name='shouninsyaShozokuBumonName' class='input-medium input-inline' value='${su:htmlEscape(shouninsyaShozokuBumonName)}'>
										<nobr><button type='button' class='btn btn-mini input-inline' id='syouninSyozokuSearch'>選択</button></nobr>
										<nobr>社員</nobr>
										<input type='text' name='shouninsyaShozokuUserShainNo' class='input-small input-inline' value='${su:htmlEscape(shouninsyaShozokuUserShainNo)}'>
										<nobr>名前</nobr>
										<input type='text' name='shouninsyaShozokuUserName' class='input-small input-inline' value='${su:htmlEscape(shouninsyaShozokuUserName)}'>
										<nobr><button type='button' class='btn btn-mini input-inline' id='syouninNameSearch'>選択</button></nobr>
									</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>所有者</label>
									<div class='controls'>
										<nobr>所属部門</nobr>
										<input type='text' name='shoyuushaShozokuBumonCd' class='input-small input-inline' value='${su:htmlEscape(shoyuushaShozokuBumonCd)}'>
										<input type='text' name='shoyuushaShozokuBumonName' class='input-medium input-inline' value='${su:htmlEscape(shoyuushaShozokuBumonName)}'>
										<nobr><button type='button' class='btn btn-mini input-inline' id='shoyuuSyozokuSearch'>選択</button></nobr>
										<nobr>社員</nobr>
										<input type='text' name='shoyuushaShozokuUserShainNo' class='input-small input-inline' value='${su:htmlEscape(shoyuushaShozokuUserShainNo)}'>
										<nobr>名前</nobr>
										<input type='text' name='shoyuushaShozokuUserName' class='input-small input-inline' value='${su:htmlEscape(shoyuushaShozokuUserName)}'>
										<nobr><button type='button' class='btn btn-mini input-inline' id='shoyuuNameSearch'>選択</button></nobr>
									</div>
							</div>
<!-- 簡易届で配置が換わるコントロール -->
<c:if test="${kanitodokeFlag}">
	<c:if test="${maruhiFlag}">
							<div class='control-group'>
								<label class="control-label" for="maruhi">マル秘</label>
								<div class='controls'>
								<input type="checkbox" id="maruhi" name="maruhi" value="1" <c:if test='${"1" eq maruhi}'>checked</c:if>>
								</div>
							</div>
	</c:if>
							<div class='control-group'>
								<label class='control-label'>添付ファイル</label>
								<div class='controls'>
</c:if>
<c:if test="${not kanitodokeFlag}">
							<div class='control-group'>
								<label class='control-label'>金額(円)</label>
								<div class='controls'>
									<input type='text' name='kingakuFrom' class='input-medium input-inline autoNumeric' value='${su:htmlEscape(kingakuFrom)}'>
									～
									<input type='text' name='kingakuTo' class='input-medium input-inline autoNumeric' value='${su:htmlEscape(kingakuTo)}'>
<c:if test="${houjinCardFlag}">
									<label class="label" for="houjinCardRiyou">法人カード利用</label>
									<input type="checkbox" id="houjinCardRiyou" name="houjinCardRiyou" value="1" <c:if test='${"1" eq houjinCardRiyou}'>checked</c:if>>
</c:if>
<c:if test="${kaishaTehaiFlag}">
									<label class="label" for="kaishaTehai">会社手配</label>
									<input type="checkbox" id="kaishaTehai" name="kaishaTehai" value="1" <c:if test='${"1" eq kaishaTehai}'>checked</c:if>>
</c:if>
<c:if test="${maruhiFlag}">
									<label class="label" for="maruhi">マル秘</label>
									<input type="checkbox" id="maruhi" name="maruhi" value="1" <c:if test='${"1" eq maruhi}'>checked</c:if>>
</c:if>
								</div>
							</div>
							<c:if test="${invoiceStart}">
							<div class='control-group'>
								<label class='control-label'>税抜金額合計(円)</label>
								<div class='controls'>
									<input type='text' name='zeinukiKingakuFrom' class='input-medium input-inline autoNumeric' value='${su:htmlEscape(zeinukiKingakuFrom)}'>
									～
									<input type='text' name='zeinukiKingakuTo' class='input-medium input-inline autoNumeric' value='${su:htmlEscape(zeinukiKingakuTo)}'>
								</div>
							</div>
							</c:if>
							<div class='control-group'>
								<label class='control-label'>明細金額(円)</label>
								<div class='controls'>
									<input type='text' name='meisaiKingakuFrom' class='input-medium input-inline autoNumeric' value='${su:htmlEscape(meisaiKingakuFrom)}'>
									～
									<input type='text' name='meisaiKingakuTo' class='input-medium input-inline autoNumeric' value='${su:htmlEscape(meisaiKingakuTo)}'>
									<label class='label'>消費税率</label>
									<select name='queryZeiritsu' class='input-small input-inline'>
										<option></option>
										<c:forEach var='record' items='${queryZeiritsuList}'>
											<option value='${record.zeiritsu}' data-keigenZeiritsuKbn='${record.keigen_zeiritsu_kbn}' <c:if test='${record.zeiritsu eq queryZeiritsu && record.keigen_zeiritsu_kbn eq keigenZeiritsuKbn}'>selected</c:if>><c:if test='${record.keigen_zeiritsu_kbn eq "1"}'>*</c:if>${record.zeiritsu}%</option>
										</c:forEach>
									</select>
									<input type='hidden' name='keigenZeiritsuKbn' value='${su:htmlEscape(keigenZeiritsuKbn)}'>
									<c:if test="${invoiceStart}">
										<label class='label'>事業者区分</label>
										<select name='jigyoushaKbn' class='input-small input-inline'>
											<option></option>
											<option value = "0" <c:if test='${"0" eq jigyoushaKbn}'>selected</c:if>>通常</option>
											<option value = "1" <c:if test='${"1" eq jigyoushaKbn}'>selected</c:if>>免税</option>
										</select>
									</c:if>
								</div>
							</div>
							<c:if test="${invoiceStart}">
							<div class='control-group'>
								<label class='control-label'>税抜明細金額(円)</label>
								<div class='controls'>
									<input type='text' name='zeinukiMeisaiKingakuFrom' class='input-medium input-inline autoNumeric' value='${su:htmlEscape(zeinukiMeisaiKingakuFrom)}'>
									～
									<input type='text' name='zeinukiMeisaiKingakuTo' class='input-medium input-inline autoNumeric' value='${su:htmlEscape(zeinukiMeisaiKingakuTo)}'>
								</div>
							</div>
							</c:if>
							<div class='control-group'>
								<label class='control-label'>支払希望日</label>
									<div class='controls'>
									<input type='text' name='shiharaiKiboubiFrom' class='input-small datepicker input-inline' value='${su:htmlEscape(shiharaiKiboubiFrom)}'>
										～
									<input type='text' name='shiharaiKiboubiTo' class='input-small datepicker input-inline' value='${su:htmlEscape(shiharaiKiboubiTo)}'>
									<label class='label'>支払日</label>
									<input type='text' name='shiharaiBiFrom' class='input-small datepicker input-inline' value='${su:htmlEscape(shiharaiBiFrom)}'>
									～
									<input type='text' name='shiharaiBiTo' class='input-small datepicker input-inline' value='${su:htmlEscape(shiharaiBiTo)}'>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>使用日</label>
									<div class='controls'>
									<input type='text' name='keijoubiFrom' class='input-small datepicker input-inline' value='${su:htmlEscape(keijoubiFrom)}'>
										～
									<input type='text' name='keijoubiTo' class='input-small datepicker input-inline' value='${su:htmlEscape(keijoubiTo)}'>
									<label class='label'>計上日</label>
									<input type='text' name='shiwakeKeijoubiFrom' class='input-small datepicker input-inline' value='${su:htmlEscape(shiwakeKeijoubiFrom)}'>
									～
									<input type='text' name='shiwakeKeijoubiTo' class='input-small datepicker input-inline' value='${su:htmlEscape(shiwakeKeijoubiTo)}'>
								</div>
							</div>
							<c:if test="${invoiceStart}">
							<div class='control-group'>
								<label class='control-label'>支払先名</label>
								<div class='controls'>
									<input type='text' name='shiharaiName' class='input-xlarge input-inline' value='${su:htmlEscape(shiharaiName)}'>
								</div>
							</div>
							</c:if>
							<div class='control-group'>
								<label class='control-label'>摘要</label>
								<div class='controls'>
									<input type='text' name='tekiyou' class='input-medium input-inline' value='${su:htmlEscape(tekiyou)}'>
									<label class='label'>領収書・請求書等</label>
									<select name='ryoushuushoSeikyuushoTou' class='input-small'>
										<c:forEach var='record' items='${shouhyouShoruiList}'>
											<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq ryoushuushoSeikyuushoTou}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
										</c:forEach>
									</select>
									<label class='label'>未精算伺い・仮払</label>
									<input type="checkbox" id='miseisanKaribarai' name='miseisanKaribarai' value='1' <c:if test='${"1" eq miseisanKaribarai}'>checked</c:if>>仮払あり
									<input type="checkbox" id='miseisanUkagai' name='miseisanUkagai' value='1' <c:if test='${"1" eq miseisanUkagai}'>checked</c:if>>仮払なし
									<label class='label'>添付ファイル</label>
</c:if>
<!-- 添付ファイル共通部分 -->
									<select name='tenpuFileFlg' class='input-small'>
										<option></option>
										<option value = "1" <c:if test='${"1" eq tenpuFileFlg}'>selected</c:if>>あり</option>
										<option value = "0" <c:if test='${"0" eq tenpuFileFlg}'>selected</c:if>>なし</option>
									</select>
								</div>
							</div>
<!-- e文書関係 -->
<c:if test="${ebunshoHyoujiFlg}">
<div id = "ebunshoGroup">
							<div class='control-group'>
								<label class='control-label'>e文書(添付ファイル)</label>
								<div class='controls'>
									<c:forEach var='i' begin ='0' end = '3' step = '1'>
										<input type="checkbox" name="tenpuFileShubetsu" value='${i}'<c:if test='${su:htmlEscape(tenpuFileShubetsu[i]) != "" && su:htmlEscape(tenpuFileShubetsu[i]) eq i}'>checked</c:if>> e文書<c:if test="${i == 0}">以外</c:if><c:if test="${i == 1}">(スキャナ)</c:if><c:if test="${i > 1}">(電子取引(ﾀｲﾑｽﾀﾝﾌﾟ対象<c:if test="${i == 3}">外</c:if>))</c:if><c:if test='${su:htmlEscape(tenpuFileShubetsu[i]) == "" || su:htmlEscape(tenpuFileShubetsu[i]) != i}'><input type="hidden" name="tenpuFileShubetsu" value='-1'></c:if><span>　</span>
									</c:forEach>
								</div>
							</div>
							<div class='control-group'>
								<label class="control-label">書類種別</label>
								<div class='controls'>
									<select name='ebunshoShubetsu' class='input-small'>
										<option></option>
										<c:forEach var='record' items='${ebunshoShubetsuList}'>
											<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq ebunshoShubetsu}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
										</c:forEach>
									</select>
									<label class='label'>年月日</label>
									<input type='text' name='ebunshoNengappiFrom' class='input-small datepicker input-inline' value='${su:htmlEscape(ebunshoNengappiFrom)}'>
									～
									<input type='text' name='ebunshoNengappiTo' class='input-small datepicker input-inline' value='${su:htmlEscape(ebunshoNengappiTo)}'>
									<input type="checkbox" id='ebunshoNengappiMinyuuryokuFlg' name='ebunshoNengappiMinyuuryokuFlg' value='1' <c:if test='${"1" eq ebunshoNengappiMinyuuryokuFlg}'>checked</c:if>>未入力のみ
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>e文書 金額(円)</label>
								<div class='controls'>
									<input type='text' name='ebunshoKingakuFrom' class='input-medium input-inline autoNumeric' value='${su:htmlEscape(ebunshoKingakuFrom)}'>
									～
									<input type='text' name='ebunshoKingakuTo' class='input-medium input-inline autoNumeric' value='${su:htmlEscape(ebunshoKingakuTo)}'>
									<input type="checkbox" id='ebunshoKingakuMinyuuryokuFlg' name='ebunshoKingakuMinyuuryokuFlg' value='1' <c:if test='${"1" eq ebunshoKingakuMinyuuryokuFlg}'>checked</c:if>>未入力のみ
									<label class='label'>発行者名称</label>
									<input type='text' name='ebunshoHakkousha' class='input-xlarge input-inline' value='${su:htmlEscape(ebunshoHakkousha)}'>
									<input type="checkbox" id='ebunshoHakkoushaMinyuuryokuFlg' name='ebunshoHakkoushaMinyuuryokuFlg' value='1' <c:if test='${"1" eq ebunshoHakkoushaMinyuuryokuFlg}'>checked</c:if>>未入力のみ
								</div>
							</div>
</div>
</c:if>
<c:if test="${not kanitodokeFlag}">
							<div class='control-group'>
								<label class='control-label'>部門</label>
								<div class='controls'>
										<input type='text' name='bumonCdFrom' class='input-small input-inline' value='${su:htmlEscape(bumonCdFrom)}'>
										<input type='text' name='bumonNameFrom' class='input-medium input-inline' value='${su:htmlEscape(bumonNameFrom)}' disabled>
										<nobr><button type='button' class='btn btn-mini input-inline' id='bumonFromSearch'>選択</button></nobr>
										～
										<input type='text' name='bumonCdTo' class='input-small input-inline' value='${su:htmlEscape(bumonCdTo)}'>
										<input type='text' name='bumonNameTo' class='input-medium input-inline' value='${su:htmlEscape(bumonNameTo)}' disabled>
										<nobr><button type='button' class='btn btn-mini input-inline' id='bumonToSearch'>選択</button></nobr>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'><%=EteamFormatter.htmlEscape(EteamSettingInfo.getSettingInfo(Key.USER_TEIGI_TODOKE_KENSAKU_KENMEI))%></label>
								<div class='controls'>
									<input type='text' name='kanitodokeKenmei' class='input-xlarge input-inline' value='${su:htmlEscape(kanitodokeKenmei)}'>
									<label class='label'><%=EteamFormatter.htmlEscape(EteamSettingInfo.getSettingInfo(Key.USER_TEIGI_TODOKE_KENSAKU_NAIYOU))%></label>
									<input type='text' name='kanitodokeNaiyou' class='input-xlarge input-inline' value='${su:htmlEscape(kanitodokeNaiyou)}'>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>仕訳抽出状況</label>
								<div class='controls'>
									<select name='shiwakeStatus' class='input-medium input-inline'>
										<option></option>
										<option value='N' <c:if test='${shiwakeStatus eq "N"}'>selected</c:if>>未抽出</option>
										<option value='0' <c:if test='${shiwakeStatus eq "0"}'>selected</c:if>>抽出済</option>
										<option value='1' <c:if test='${shiwakeStatus eq "1"}'>selected</c:if>>OPEN21転記済</option>
									</select>
									<label class='label'>FBデータ作成状況</label>
									<select name='fbStatus' class='input-small'>
										<option></option>
										<option value='N' <c:if test='${fbStatus eq "N"}'>selected</c:if>>未作成</option>
										<option value='1' <c:if test='${fbStatus eq "1"}'>selected</c:if>>作成済</option>
									</select>
								</div>
							</div>
</c:if>
						</div>
<c:if test="${not kanitodokeFlag}">
						<div id='syousaiKensakuList' <c:if test='${shousaiKensakuHyoujiFlg eq "0" or kensakuJoukenHyoujiFlg eq "0"}'>style='display:none'</c:if> class='no-more-tables'>
							<table class='table-bordered table-condensed'>
									<tr>
										<th colspan='9'>
											<label class='label'>借方</label>
											<div style='float: right;'>
												<label class='radio inline'>
													<input type='radio' name='bumonSentakuFlag' value='0' <c:if test='${"0" eq bumonSentakuFlag}'>checked</c:if>>
													部門
												</label>
												<label class='radio inline'>
													<input type='radio' name='bumonSentakuFlag' value='1' <c:if test='${"1" eq bumonSentakuFlag}'>checked</c:if>>
													集計部門
												</label>
											</div>
										</th>
									</tr>
								<tbody>
									<tr>
										<th><label class='label'>部門</label></th>
										<td><nobr>コード</nobr></td>
										<td><input type='text' name='karikataBumonCdFrom' class='input-medium' value='${su:htmlEscape(karikataBumonCdFrom)}'>
										～
										<input type='text' name='karikataBumonCdTo' class='input-medium' value='${su:htmlEscape(karikataBumonCdTo)}'></td>
										<td><nobr>名前</nobr></td>
										<td><input type='text' name='karikataBumonNameFrom' class='input-medium' value='${su:htmlEscape(karikataBumonNameFrom)}' disabled></td>
										<td><nobr><button type='button' class='btn btn-mini' id='karikataBumonFromSearch'>選択</button></nobr></td>
										<td>～</td>
										<td><input type='text' name='karikataBumonNameTo' class='input-medium' value='${su:htmlEscape(karikataBumonNameTo)}' disabled></td>
										<td><nobr><button type='button' class='btn btn-mini' id='karikataBumonToSearch'>選択</button></nobr></td>
									</tr>
									<tr>
										<th><label class='label'>科目</label></th>
										<td><nobr>コード</nobr></td>
										<td><input type='text' name='karikataKamokuCdFrom' class='input-medium' value='${su:htmlEscape(karikataKamokuCdFrom)}'>
										～
										<input type='text' name='karikataKamokuCdTo' class='input-medium' value='${su:htmlEscape(karikataKamokuCdTo)}'></td>
										<td><nobr>名前</nobr></td>
										<td><input type='text' name='karikataKamokuNameFrom' class='input-medium' value='${su:htmlEscape(karikataKamokuNameFrom)}' disabled></td>
										<td><nobr><button type='button' class='btn btn-mini' id='karikataKamokuFromSearch'>選択</button></nobr></td>
										<td>～</td>
										<td><input type='text' name='karikataKamokuNameTo' class='input-medium' value='${su:htmlEscape(karikataKamokuNameTo)}' disabled></td>
										<td><nobr><button type='button' class='btn btn-mini' id='karikataKamokuToSearch'>選択</button></nobr></td>
									</tr>
									<tr>
										<th><label class='label'>枝番</label></th>
										<td><nobr>コード</nobr></td>
										<td><input type='text' name='karikataKamokuEdanoCdFrom' class='input-medium' value='${su:htmlEscape(karikataKamokuEdanoCdFrom)}'>
										～
										<input type='text' name='karikataKamokuEdanoCdTo' class='input-medium' value='${su:htmlEscape(karikataKamokuEdanoCdTo)}'></td>
										<td><nobr>名前</nobr></td>
										<td><input type='text' name='karikataKamokuEdanoNameFrom' class='input-medium' value='${su:htmlEscape(karikataKamokuEdanoNameFrom)}' disabled></td>
										<td><nobr><button type='button' class='btn btn-mini' id='karikataKamokuEdanoFromSearch'>選択</button></nobr></td>
										<td>～</td>
										<td><input type='text' name='karikataKamokuEdanoNameTo' class='input-medium' value='${su:htmlEscape(karikataKamokuEdanoNameTo)}' disabled></td>
										<td><nobr><button type='button' class='btn btn-mini' id='karikataKamokuEdanoToSearch'>選択</button></nobr></td>
									</tr>
									<tr>
										<th><label class='label'>取引先</label></th>
										<td><nobr>コード</nobr></td>
										<td><input type='text' name='karikataTorihikisakiCdFrom' class='input-medium' value='${su:htmlEscape(karikataTorihikisakiCdFrom)}'>
										～
										<input type='text' name='karikataTorihikisakiCdTo' class='input-medium' value='${su:htmlEscape(karikataTorihikisakiCdTo)}'></td>
										<td><nobr>名前</nobr></td>
										<td><input type='text' name='karikataTorihikisakiNameFrom' class='input-medium' value='${su:htmlEscape(karikataTorihikisakiNameFrom)}' disabled></td>
										<td><nobr><button type='button' class='btn btn-mini' id='karikataTorihikisakiFrombutton'>選択</button></nobr></td>
										<td>～</td>
										<td><input type='text' name='karikataTorihikisakiNameTo' class='input-medium' value='${su:htmlEscape(karikataTorihikisakiNameTo)}' disabled></td>
										<td><nobr><button type='button' class='btn btn-mini' id='karikataTorihikisakiTobutton'>選択</button></nobr></td>
									</tr>
								</tbody>
									<tr>
										<th colspan='9'>
											<label class='label'>貸方</label>
										</th>
									</tr>
								<tbody>
									<tr>
										<th><label class='label'>部門</label></th>
										<td><nobr>コード</nobr></td>
										<td><input type='text' name='kashikataBumonCdFrom' class='input-medium' value='${su:htmlEscape(kashikataBumonCdFrom)}'>
										～
										<input type='text' name='kashikataBumonCdTo' class='input-medium' value='${su:htmlEscape(kashikataBumonCdTo)}'></td>
										<td><nobr>名前</nobr></td>
										<td><input type='text' name='kashikataBumonNameFrom' class='input-medium' value='${su:htmlEscape(kashikataBumonNameFrom)}' disabled></td>
										<td><nobr><button type='button' class='btn btn-mini' id='kashikataBumonFromSearch'>選択</button></nobr></td>
										<td>～</td>
										<td><input type='text' name='kashikataBumonNameTo' class='input-medium' value='${su:htmlEscape(kashikataBumonNameTo)}' disabled></td>
										<td><nobr><button type='button' class='btn btn-mini' id='kashikataBumonToSearch'>選択</button></nobr></td>
									</tr>
									<tr>
										<th><label class='label'>科目</label></th>
										<td><nobr>コード</nobr></td>
										<td><input type='text' name='kashikataKamokuCdFrom' class='input-medium' value='${su:htmlEscape(kashikataKamokuCdFrom)}'>
										～
										<input type='text' name='kashikataKamokuCdTo' class='input-medium' value='${su:htmlEscape(kashikataKamokuCdTo)}'></td>
										<td><nobr>名前</nobr></td>
										<td><input type='text' name='kashikataKamokuNameFrom' class='input-medium' value='${su:htmlEscape(kashikataKamokuNameFrom)}' disabled></td>
										<td><nobr><button type='button' class='btn btn-mini' id='kashikataKamakuFromSearch'>選択</button></nobr></td>
										<td>～</td>
										<td><input type='text' name='kashikataKamokuNameTo' class='input-medium' value='${su:htmlEscape(kashikataKamokuNameTo)}' disabled></td>
										<td><nobr><button type='button' class='btn btn-mini' id='kashikataKamakuToSearch'>選択</button></nobr></td>
									</tr>
									<tr>
										<th><label class='label'>枝番</label></th>
										<td><nobr>コード</nobr></td>
										<td><input type='text' name='kashikataKamokuEdanoCdFrom' class='input-medium' value='${su:htmlEscape(kashikataKamokuEdanoCdFrom)}'>
										～
										<input type='text' name='kashikataKamokuEdanoCdTo' class='input-medium' value='${su:htmlEscape(kashikataKamokuEdanoCdTo)}'></td>
										<td><nobr>名前</nobr></td>
										<td><input type='text' name='kashikataKamokuEdanoNameFrom' class='input-medium' value='${su:htmlEscape(kashikataKamokuEdanoNameFrom)}' disabled></td>
										<td><nobr><button type='button' class='btn btn-mini' id='kashikataKamokuEdanoFromSearch'>選択</button></nobr></td>
										<td>～</td>
										<td><input type='text' name='kashikataKamokuEdanoNameTo' class='input-medium' value='${su:htmlEscape(kashikataKamokuEdanoNameTo)}' disabled></td>
										<td><nobr><button type='button' class='btn btn-mini' id='kashikataKamokuEdanoToSearch'>選択</button></nobr></td>
									</tr>
									<tr>
										<th><label class='label'>取引先</label></th>
										<td><nobr>コード</nobr></td>
										<td><input type='text' name='kashikataTorihikisakiCdFrom' class='input-medium' value='${su:htmlEscape(kashikataTorihikisakiCdFrom)}'>
										～
										<input type='text' name='kashikataTorihikisakiCdTo' class='input-medium' value='${su:htmlEscape(kashikataTorihikisakiCdTo)}'></td>
										<td><nobr>名前</nobr></td>
										<td><input type='text' name='kashikataTorihikisakiNameFrom' class='input-medium' value='${su:htmlEscape(kashikataTorihikisakiNameFrom)}' disabled></td>
										<td><nobr><button type='button' class='btn btn-mini' id='kashikataTorihikisakiFromSearch'>選択</button></nobr></td>
										<td>～</td>
										<td><input type='text' name='kashikataTorihikisakiNameTo' class='input-medium' value='${su:htmlEscape(kashikataTorihikisakiNameTo)}' disabled></td>
										<td><nobr><button type='button' class='btn btn-mini' id='kashikataTorihikisakiToSearch'>選択</button></nobr></td>
									</tr>
								</tbody>
							</table>
						</div>
</c:if>
<c:if test="${kanitodokeFlag}">
<!-- 簡易届項目ごとに検索項目作成  -->
<datalist id="jikoku"><c:forEach var='record' items='${jikokuList}'><option value='${su:htmlEscape(record.name)}'></c:forEach></datalist>
<!-- 申請部 -->
<c:forEach var="item" items="${shinseiLayoutList}" varStatus="st" >
	<c:set var="reqFrom" value="${item.item_name}[0]" scope="request"></c:set>
	<c:set var="reqTo" value="${item.item_name}[1]" scope="request"></c:set>
	<% String from = (String)request.getAttribute("reqFrom"); %>
	<% String to = (String)request.getAttribute("reqTo"); %>
	<div class='control-group'>
		<label class='control-label'>${su:htmlEscape(item.label_name)}</label>
		<div class='controls'>

<c:choose>
<c:when test="${item.buhin_type eq 'text'}">
	<!-- タイプごとの分岐必要 文字列,数値,日付,時間,金額 文字列以外はtoが必要 -->
	<!-- shinseiXXto,meisaiXXtoはDenpyouIchiranAction側に各30項目分作っておく -->
	<c:choose>
	<c:when test="${item.buhin_format eq 'string'}">
		<!-- 文字列 -->
		<input type='text' name='${item.item_name}' class='kaniItem input-xlarge input-inline' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from)) %>'>
	</c:when>
	<c:when test="${item.buhin_format eq 'number'}">
		<!-- 数値 -->
		<input type='text' name='${item.item_name}' class='kaniItem input-small text-r input-inline' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
		～
		<input type='text' name='${item.item_name}' class='kaniItem input-small text-r input-inline' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>'>
	</c:when>
	<c:when test="${item.buhin_format eq 'datepicker'}">
		<!-- 日付 -->
		<input type='text' name='${item.item_name}' class='kaniItem input-small text-r input-inline datepicker' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
		～
		<input type='text' name='${item.item_name}' class='kaniItem input-small text-r input-inline datepicker' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>'>
	</c:when>
	<c:when test="${item.buhin_format eq 'autoNumericWithCalcBox'}">
		<!-- 金額 -->
		<input type='text' name='${item.item_name}' class='kaniItem input-medium text-r input-inline autoNumericWithCalcBox' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
		～
		<input type='text' name='${item.item_name}' class='kaniItem input-medium text-r input-inline autoNumericWithCalcBox' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>'>
	</c:when>
	<c:when test="${item.buhin_format eq 'timepicker'}">
		<!-- 時間 -->
		<input type='text' name='${item.item_name}' class='kaniItem input-small input-inline timepicker' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
		～
		<input type='text' name='${item.item_name}' class='kaniItem input-small input-inline timepicker' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>'>
	</c:when>
	</c:choose>
</c:when>
<c:when test="${item.buhin_type eq 'textarea'}">
		<input type='text' name='${item.item_name}' class='kaniItem input-xlarge input-inline' value='<%=request.getAttribute(from)%>'>
</c:when>
<c:when test="${item.buhin_type eq 'checkbox'}">
		<% Map<String,Object> lblMp = (Map<String,Object>)(pageContext.findAttribute("item")); String lbl = (String)lblMp.get("checkbox_label_name");  %>
		<label class='checkbox inline'><input type='checkbox' name='${item.item_name}' class='kaniItem' value="1" <%if("1".equals(request.getAttribute(from))){%>checked<%}%> >${su:htmlEscape(item.checkbox_label_name)}</label>
</c:when>
<c:when test="${item.buhin_type eq 'radio'}">
	<!-- 該当項目の選択肢データ必要  -->
	<c:forEach var="sel" items="${shinseiOptionList}" varStatus="stsl" >
		<c:if test='${sel.item_name eq item.item_name}'>
		<% Map<String,Object> selMp = (Map<String,Object>)(pageContext.findAttribute("sel")); String sel = (String)selMp.get("text");  %>
		<label class='radio inline'><input type='radio' name='${item.item_name}' class='kaniItem' data-text='${sel.text}' value='${su:htmlEscape(sel.text)}' <%if(sel.equals(request.getAttribute(from))){%>checked<%}%> >${su:htmlEscape(sel.text)}</label>
		</c:if>
	</c:forEach>
</c:when>
<c:when test="${item.buhin_type eq 'pulldown'}">
	<!-- 該当項目の選択肢データ必要  -->
	<select name='${item.item_name}' class='kaniItem input-large'>
	<c:forEach var="sel" items="${shinseiOptionList}" varStatus="stsl" >
		<c:if test='${sel.item_name eq item.item_name}'>
		<% Map<String,Object> selMp = (Map<String,Object>)(pageContext.findAttribute("sel")); String sel = (String)selMp.get("text");  %>
		<option value='${su:htmlEscape(sel.text)}' <%if(sel.equals(request.getAttribute(from))){%>selected<%}%>>${su:htmlEscape(sel.text)}</option>
		</c:if>
	</c:forEach>
	</select>
</c:when>
<c:when test="${item.buhin_type eq 'master'}">
	<!-- master_kbnで分岐  -->
	<c:choose>
	<c:when test="${item.master_kbn eq 'futanBumonSentaku'}">
		<!-- 負担部門マスター -->
			<input type='text' name="${item.item_name}" class='kaniItem input-small kani_futan_cd' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
			<input type='text' name="${item.item_name}" class='kaniItem input-xlarge kani_name' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>' disabled>
			<button type='button' name='futanBumonSentakuButton' class='btn btn-small' >選択</button>
	</c:when>
	<c:when test="${item.master_kbn eq 'kanjyouKamokuSentaku'}">
		<!-- 勘定科目マスター -->
			<input type='text' name="${item.item_name}" class='kaniItem input-small kani_kamoku_cd' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
			<input type='text' name="${item.item_name}" class='kaniItem input-xlarge kani_name' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>' disabled>
			<button type='button' name='kanjyouKamokuSentakuButton' class='btn btn-small' >選択</button>
	</c:when>
	<c:when test="${item.master_kbn eq 'kanjyouKamokuEdabanSentaku'}">
		<!-- 勘定科目枝番マスター -->
			<input type='text' name="${item.item_name}" class='kaniItem input-medium kani_kamokuedaban_cd' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
			<input type='text' name="${item.item_name}" class='kaniItem input-xlarge kani_name' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>' disabled>
			<button type='button' name='kanjyouKamokuEdabanSentakuButton' class='btn btn-small' >選択</button>
	</c:when>
	<c:when test="${item.master_kbn eq 'torihikisakiSentaku'}">
		<!-- 取引先マスター -->
			<input type='text' name="${item.item_name}" class='kaniItem input-medium kani_tori_cd' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
			<input type='text' name="${item.item_name}" class='kaniItem input-xlarge kani_name' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>' disabled>
			<button type='button' name='torihikisakiSentakuButton' class='btn btn-small' >選択</button>
	</c:when>
	<c:when test="${item.master_kbn eq 'uf1Sentaku'}">
		<!-- UF1 -->
			<input type='text' name="${item.item_name}" class='kaniItem input-large kani_uf1_cd' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
			<input type='text' name="${item.item_name}" class='kaniItem input-xlarge kani_name' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>' disabled>
			<button type='button' name='UF1SentakuButton' class='btn btn-small' >選択</button>
	</c:when>
	<c:when test="${item.master_kbn eq 'uf2Sentaku'}">
		<!-- UF2 -->
			<input type='text' name="${item.item_name}" class='kaniItem input-large kani_uf2_cd' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
			<input type='text' name="${item.item_name}" class='kaniItem input-xlarge kani_name' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>' disabled>
			<button type='button' name='UF2SentakuButton' class='btn btn-small' >選択</button>
	</c:when>
	<c:when test="${item.master_kbn eq 'uf3Sentaku'}">
		<!-- UF3 -->
			<input type='text' name="${item.item_name}" class='kaniItem input-large kani_uf3_cd' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
			<input type='text' name="${item.item_name}" class='kaniItem input-xlarge kani_name' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>' disabled>
			<button type='button' name='UF3SentakuButton' class='btn btn-small' >選択</button>
	</c:when>
	</c:choose>

</c:when>
</c:choose>
		</div>
	</div>
</c:forEach>

<!-- 明細部 -->
<c:forEach var="item" items="${meisaiLayoutList}" varStatus="st" >
	<c:set var="reqFrom" value="${item.item_name}[0]" scope="request"></c:set>
	<c:set var="reqTo" value="${item.item_name}[1]" scope="request"></c:set>
	<% String from = (String)request.getAttribute("reqFrom"); %>
	<% String to = (String)request.getAttribute("reqTo"); %>
	<div class='control-group'>
		<label class='control-label'>${su:htmlEscape(item.label_name)}</label>
		<div class='controls'>

<c:choose>
<c:when test="${item.buhin_type eq 'text'}">
	<!-- タイプごとの分岐必要 文字列,数値,日付,時間,金額 文字列以外はtoが必要 -->
	<!-- shinseiXXto,meisaiXXtoはDenpyouIchiranAction側に各30項目分作っておく -->
	<c:choose>
	<c:when test="${item.buhin_format eq 'string'}">
		<!-- 文字列 -->
		<input type='text' name='${item.item_name}' class='kaniItem input-xlarge input-inline' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
	</c:when>
	<c:when test="${item.buhin_format eq 'number'}">
		<!-- 数値 -->
		<input type='text' name='${item.item_name}' class='kaniItem input-small text-r input-inline' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
		～
		<input type='text' name='${item.item_name}' class='kaniItem input-small text-r input-inline' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>'>
	</c:when>
	<c:when test="${item.buhin_format eq 'datepicker'}">
		<!-- 日付 -->
		<input type='text' name='${item.item_name}' class='kaniItem input-small text-r input-inline datepicker' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
		～
		<input type='text' name='${item.item_name}' class='kaniItem input-small text-r input-inline datepicker' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>'>
	</c:when>
	<c:when test="${item.buhin_format eq 'autoNumericWithCalcBox'}">
		<!-- 金額 -->
		<input type='text' name='${item.item_name}' class='kaniItem input-medium text-r input-inline autoNumericWithCalcBox' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
		～
		<input type='text' name='${item.item_name}' class='kaniItem input-medium text-r input-inline autoNumericWithCalcBox' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>'>
	</c:when>
	<c:when test="${item.buhin_format eq 'timepicker'}">
		<!-- 時間 -->
		<input type='text' name='${item.item_name}' class='kaniItem input-small input-inline timepicker' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
		～
		<input type='text' name='${item.item_name}' class='kaniItem input-small input-inline timepicker' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>'>
	</c:when>
	</c:choose>
</c:when>
<c:when test="${item.buhin_type eq 'textarea'}">
		<input type='text' name='${item.item_name}' class='kaniItem input-xlarge input-inline' value='<%=request.getAttribute(from)%>'>
</c:when>
<c:when test="${item.buhin_type eq 'checkbox'}">
		<% Map<String,Object> lblMp = (Map<String,Object>)(pageContext.findAttribute("item")); String lbl = (String)lblMp.get("checkbox_label_name");  %>
		<label class='checkbox inline'><input type='checkbox' name='${item.item_name}' class='kaniItem' value="1" <%if("1".equals(request.getAttribute(from))){%>checked<%}%> >${su:htmlEscape(item.checkbox_label_name)}</label>
</c:when>
<c:when test="${item.buhin_type eq 'radio'}">
	<!-- 該当項目の選択肢データ必要  -->
	<c:forEach var="sel" items="${meisaiOptionList}" varStatus="stsl" >
		<c:if test='${sel.item_name eq item.item_name}'>
		<% Map<String,Object> selMp = (Map<String,Object>)(pageContext.findAttribute("sel")); String sel = (String)selMp.get("text");  %>
		<label class='radio inline'><input type='radio' name='${item.item_name}' class='kaniItem' data-text='${sel.text}' value='${su:htmlEscape(sel.text)}' <%if(sel.equals(request.getAttribute(from))){%>checked<%}%> >${su:htmlEscape(sel.text)}</label>
		</c:if>
	</c:forEach>
</c:when>
<c:when test="${item.buhin_type eq 'pulldown'}">
	<!-- 該当項目の選択肢データ必要  -->
	<select name='${item.item_name}' class='kaniItem input-large'>
	<c:forEach var="sel" items="${meisaiOptionList}" varStatus="stsl" >
		<c:if test='${sel.item_name eq item.item_name}'>
		<% Map<String,Object> selMp = (Map<String,Object>)(pageContext.findAttribute("sel")); String sel = (String)selMp.get("text");  %>
		<option value='${su:htmlEscape(sel.text)}' <%if(sel.equals(request.getAttribute(from))){%>selected<%}%>>${su:htmlEscape(sel.text)}</option>
		</c:if>
	</c:forEach>
	</select>
</c:when>
<c:when test="${item.buhin_type eq 'master'}">
	<!-- master_kbnで分岐  -->
	<c:choose>
	<c:when test="${item.master_kbn eq 'futanBumonSentaku'}">
		<!-- 負担部門マスター -->
			<input type='text' name="${item.item_name}" class='kaniItem input-small kani_futan_cd' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
			<input type='text' name="${item.item_name}" class='kaniItem input-xlarge kani_name' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>' disabled>
			<button type='button' name='futanBumonSentakuButton' class='btn btn-small' >選択</button>
	</c:when>
	<c:when test="${item.master_kbn eq 'kanjyouKamokuSentaku'}">
		<!-- 勘定科目マスター -->
			<input type='text' name="${item.item_name}" class='kaniItem input-small kani_kamoku_cd <c:if test="${item.yosan_shikkou_koumoku_id eq 'syuunyuu_kamoku'}"> syuunyuu_kamoku </c:if><c:if test="${item.yosan_shikkou_koumoku_id eq 'shishutsu_kamoku'}"> shishutsu_kamoku </c:if>' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
			<input type='text' name="${item.item_name}" class='kaniItem input-xlarge kani_name' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>' disabled>
			<button type='button' name='kanjyouKamokuSentakuButton' class='btn btn-small' >選択</button>
	</c:when>
	<c:when test="${item.master_kbn eq 'kanjyouKamokuEdabanSentaku'}">
		<!-- 勘定科目枝番マスター -->
			<input type='text' name="${item.item_name}" class='kaniItem input-medium kani_kamokuedaban_cd <c:if test="${item.yosan_shikkou_koumoku_id eq 'syuunyuu_eda_num'}"> syuunyuu_eda_num </c:if><c:if test="${item.yosan_shikkou_koumoku_id eq 'shishutsu_eda_num'}"> shishutsu_eda_num </c:if>' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
			<input type='text' name="${item.item_name}" class='kaniItem input-xlarge kani_name' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>' disabled>
			<button type='button' name='kanjyouKamokuEdabanSentakuButton' class='btn btn-small' >選択</button>
	</c:when>
	<c:when test="${item.master_kbn eq 'torihikisakiSentaku'}">
		<!-- 取引先マスター -->
			<input type='text' name="${item.item_name}" class='kaniItem input-medium kani_tori_cd' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
			<input type='text' name="${item.item_name}" class='kaniItem input-xlarge kani_name' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>' disabled>
			<button type='button' name='torihikisakiSentakuButton' class='btn btn-small' >選択</button>
	</c:when>
	<c:when test="${item.master_kbn eq 'uf1Sentaku'}">
		<!-- UF1 -->
			<input type='text' name="${item.item_name}" class='kaniItem input-large kani_uf1_cd' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
			<input type='text' name="${item.item_name}" class='kaniItem input-xlarge kani_name' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>' disabled>
			<button type='button' name='UF1SentakuButton' class='btn btn-small' >選択</button>
	</c:when>
	<c:when test="${item.master_kbn eq 'uf2Sentaku'}">
		<!-- UF2 -->
			<input type='text' name="${item.item_name}" class='kaniItem input-large kani_uf2_cd' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
			<input type='text' name="${item.item_name}" class='kaniItem input-xlarge kani_name' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>' disabled>
			<button type='button' name='UF2SentakuButton' class='btn btn-small' >選択</button>
	</c:when>
	<c:when test="${item.master_kbn eq 'uf3Sentaku'}">
		<!-- UF3 -->
			<input type='text' name="${item.item_name}" class='kaniItem input-large kani_uf3_cd' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(from))%>'>
			<input type='text' name="${item.item_name}" class='kaniItem input-xlarge kani_name' value='<%=EteamFormatter.htmlEscape((String)request.getAttribute(to))%>' disabled>
			<button type='button' name='UF3SentakuButton' class='btn btn-small' >選択</button>
	</c:when>
	</c:choose>
</c:when>
</c:choose>
		</div>
	</div>
</c:forEach>

</c:if>
						<div class='blank'></div>
						<div id='kensakuButton' <c:if test='${kensakuJoukenHyoujiFlg eq "0"}'>style='display:none'</c:if> >
							<button type='button' class='btn' name='kensakuBtn' onClick="onclickSearch()"><i class='icon-search'></i> 検索実行</button>
							<button type='button' class='btn' name='joukenClearBtn'><i class='icon-remove-circle'></i> 条件クリア</button>
<c:if test="${not kanitodokeFlag}">
							<button type='button' class='btn' name='shousaiKensakuBtn' onclick="$('#syousaiKensakuList').toggle();"><i class='icon-zoom-in'></i> 詳細検索</button>
</c:if>
							<c:if test="${yosanKannriFlg}"><button type='button' class='btn' id='kianIchiranCsvBtn'><i class='icon-download'></i> 起案一覧CSV出力</button></c:if>
							<label class='label'>画面自動更新</label>
							<input type="checkbox" id='jidouKoushinChk' name='jidouKoushinChk' value='1' <c:if test='${"1" eq jidouKoushinFlg}'>checked</c:if>>
							<input type='hidden' name='jidouKoushinFlg' value='${su:htmlEscape(jidouKoushinFlg)}'>
							<label class='label'>取下済・否認済　表示</label>
							<input type="checkbox" id='torisageHininHyoujiChk' name='torisageHininHyoujiChk' value='1' <c:if test='${"1" eq torisageHininHyoujiFlg}'>checked</c:if>>
							<input type='hidden' name='torisageHininHyoujiFlg' value='${su:htmlEscape(torisageHininHyoujiFlg)}'>
						</div>
					</section>

					<!-- 検索結果 -->
					<section>
						<h2>検索結果  <span style="font-size:16px;">　全${totalCount}件    <c:if test="${kensakuGoukeiKingaku!=''}">　合計金額  ${su:htmlEscape(kensakuGoukeiKingaku)}円</c:if></span></h2>
						<div>
<c:if test="${kihyouFlg}">
							<button type='button' class='btn' id='sanshouKihyouBtn'><i class='icon-retweet'></i> 参照起票</button>
</c:if>
							<button type='button' class='btn' id='shouninBtn' data-toggle='modal'><i class='icon-ok'></i> 承認</button>
<c:if test="${shiharaibiTourokuFlg}">
							<button type='button' class='btn' id='shiharaibiBtn' data-toggle='modal' href='#shiharaibiModal'><i class='icon-calendar'></i> 支払日登録</button>
							<button type='button' class='btn' id='keijoubiBtn'><i class='icon-calendar'></i> 計上日更新</button>
							<input type='hidden' name='updateKeijoubi'>
</c:if>
<c:if test="${ikkatsuKianSyuryoFlg}">
							<button type='button' class='btn' id='ikkatsuKianSyuryoBtn'><i class='icon-retweet'></i> 起案終了</button>
							<button type='button' class='btn' id='ikkatsuKianSyuryoKaijyoBtn'><i class='icon-retweet'></i> 起案終了解除</button>
							<input type='hidden' name='kianSyuryoType'>
</c:if>
							<button type='button' class='btn' id='csvBtn'><i class='icon-download'></i> CSV出力</button>
<c:if test="${fileTenpuFlg}">
							<button type='button' class='btn' id='downLoadBtn'><i class='icon-download'></i> 添付ファイル一括Download</button>
</c:if>
							<button type='button' class='btn' id='itemCustomizeBtn'><i class='icon-wrench'></i> 表示項目カスタマイズ</button>
						</div>
						<div class='blank'></div>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed' >
								<thead>
									<tr>
										<th rowspan='2'><nobr>対象</nobr><br><input type='checkbox' id='allcheck'></th>
<c:forEach var="item" items="${itemList}">
	<c:if test="${item.display}">
		<c:choose>
		<c:when test="${item.name eq 'joutai'}">
										<th rowspan='2'><nobr>状態</nobr><br><a href="#" onClick="onclickSort(1)">▲</a>    <a href="#" onClick="onclickSort(2)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'id'}">
										<th rowspan='2'><nobr>伝票ID</nobr><br><a href="#" onClick="onclickSort(3)">▲</a>    <a href="#" onClick="onclickSort(4)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'jisshi_kian_bangou'}">
										<th rowspan='2'><nobr>実施起案番号</nobr><br><a href="#" onClick="onclickSort(51)">▲</a>    <a href="#" onClick="onclickSort(52)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'shishutsu_kian_bangou'}">
										<th rowspan='2'><nobr>支出起案番号</nobr><br><a href="#" onClick="onclickSort(53)">▲</a>    <a href="#" onClick="onclickSort(54)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'yosan_shikkou_taishou'}">
										<th rowspan='2'><nobr>予算執行伝票種別</nobr><br><a href="#" onClick="onclickSort(55)">▲</a>    <a href="#" onClick="onclickSort(56)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'yosan_check_nengetsu'}">
										<th rowspan='2'><nobr>予算執行対象月</nobr><br><a href="#" onClick="onclickSort(65)">▲</a>    <a href="#" onClick="onclickSort(66)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'shubetsu'}">
										<th rowspan='2'><nobr>伝票種別</nobr><br><a href="#" onClick="onclickSort(5)">▲</a>    <a href="#" onClick="onclickSort(6)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'kingaku'}">
										<th rowspan='2'><nobr>金額(円)</nobr><br><a href="#" onClick="onclickSort(7)">▲</a>    <a href="#" onClick="onclickSort(8)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'shiharaiKiboubi'}">
										<th rowspan='2'><nobr>支払希望日<br>支払期限</nobr><br><a href="#" onClick="onclickSort(17)">▲</a>    <a href="#" onClick="onclickSort(18)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'shiharaiBi'}">
										<th rowspan='2'><nobr>支払日</nobr><br><a href="#" onClick="onclickSort(19)">▲</a>    <a href="#" onClick="onclickSort(20)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'torihikisaki'}">
										<th rowspan='2'><nobr>取引先</nobr><br><a href="#" onClick="onclickSort(9)">▲</a>    <a href="#" onClick="onclickSort(10)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'kihyou'}">
										<th colspan='4'><nobr>起票</nobr></th>
		</c:when>
		<c:when test="${item.name eq 'shoyuu'}">
										<th colspan='2'><nobr>所有</nobr></th>
		</c:when>
		<c:when test="${item.name eq 'shiharaihouhou'}">
										<th rowspan='2'><nobr>支払方法</nobr><br><a href="#" onClick="onclickSort(29)">▲</a>    <a href="#" onClick="onclickSort(30)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'houjin_kingaku'}">
										<th rowspan='2'><nobr>法人カード利用金額(円)</nobr><br><a href="#" onClick="onclickSort(49)">▲</a>    <a href="#" onClick="onclickSort(50)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'sashihiki_shikyuu_kingaku'}">
										<th rowspan='2'><nobr>差引支給金額(円)</nobr><br><a href="#" onClick="onclickSort(31)">▲</a>    <a href="#" onClick="onclickSort(32)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'keijoubi'}">
										<th rowspan='2'><nobr>使用日</nobr><br><a href="#" onClick="onclickSort(33)">▲</a>    <a href="#" onClick="onclickSort(34)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'seisan_yoteibi'}">
										<th rowspan='2'><nobr>精算予定日</nobr><br><a href="#" onClick="onclickSort(35)">▲</a>    <a href="#" onClick="onclickSort(36)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'karibarai_denpyou_id'}">
										<th rowspan='2'><nobr>仮払伝票ID</nobr><br><a href="#" onClick="onclickSort(37)">▲</a>    <a href="#" onClick="onclickSort(38)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'shiwakeKeijoubi'}">
										<th rowspan='2'><nobr>計上日</nobr><br><a href="#" onClick="onclickSort(39)">▲</a>    <a href="#" onClick="onclickSort(40)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'shouninbi'}">
										<th rowspan='2'><nobr>最終承認日</nobr><br><a href="#" onClick="onclickSort(41)">▲</a>    <a href="#" onClick="onclickSort(42)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'serialNo'}">
										<th rowspan='2'><nobr>伝票番号</nobr><br><a href="#" onClick="onclickSort(43)">▲</a>    <a href="#" onClick="onclickSort(44)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'houmonsaki'}">
										<th rowspan='2'><nobr>出張先・訪問先</nobr><br><a href="#" onClick="onclickSort(45)">▲</a>    <a href="#" onClick="onclickSort(46)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'mokuteki'}">
										<th rowspan='2'><nobr>目的</nobr><br><a href="#" onClick="onclickSort(47)">▲</a>    <a href="#" onClick="onclickSort(48)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'kanitodoke_kenmei'}">
										<th rowspan='2'><nobr><%=EteamFormatter.htmlEscape(EteamSettingInfo.getSettingInfo(Key.USER_TEIGI_TODOKE_KENSAKU_KENMEI))%></nobr><br><a href="#" onClick="onclickSort(57)">▲</a>    <a href="#" onClick="onclickSort(58)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'kanitodoke_naiyou'}">
										<th rowspan='2'><nobr><%=EteamFormatter.htmlEscape(EteamSettingInfo.getSettingInfo(Key.USER_TEIGI_TODOKE_KENSAKU_NAIYOU))%></nobr><br><a href="#" onClick="onclickSort(59)">▲</a>    <a href="#" onClick="onclickSort(60)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'user_name'}">
										<th rowspan='2'><nobr>使用者</nobr><br><a href="#" onClick="onclickSort(61)">▲</a>    <a href="#" onClick="onclickSort(62)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'seisankikan'}">
										<th rowspan='2'><nobr>精算期間</nobr><br><a href="#" onClick="onclickSort(63)">▲</a>    <a href="#" onClick="onclickSort(64)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'hosoku'}">
								<th rowspan='2'><nobr>補足</nobr></th>
		</c:when>
		<c:when test="${item.name eq 'zeiritsu'}">
								<th rowspan='2'><nobr>消費税率(%)</nobr></th>
		</c:when>
		<c:when test="${item.name eq 'jigyousha_kbn'}">
								<c:if test="${invoiceStart}">
								<th rowspan='2'><nobr>事業者区分</nobr></th>
								</c:if>
		</c:when>
		<c:when test="${item.name eq 'zeinuki_kingaku'}">
								<c:if test="${invoiceStart}">
								<th rowspan='2'><nobr>税抜金額(円)</nobr></th>
								</c:if>
		</c:when>
		<c:otherwise>
			<!-- カスタマイズ用の列追加欄：ヘッダー -->
			<jsp:include page="${denpyouIchiranActionStrategy ne null ? '../../../../jsp_ext/eteam/gyoumu/tsuuchi/' : ''}DenpyouIchiranAdditionalColumnsHeader.jsp">
				<jsp:param name="name" value="${item.name}" />
			</jsp:include>
		</c:otherwise>
		</c:choose>
	</c:if>
</c:forEach>

<!-- 簡易届専用 -->
<c:if test='${kanitodokeFlag}'>
	<c:forEach var="witem" items="${wameiList}" varStatus="wst">
		<c:forEach var="bitem" items="${columnList}" varStatus="bst">
			<c:if test="${wst.index eq bst.index}">
										<th rowspan='2'><nobr>${su:htmlEscape(witem)}</nobr><br><a href="#" onClick="searchActionKanitodoke('${su:htmlEscape(bitem)}',1)">▲</a>    <a href="#" onClick="searchActionKanitodoke('${su:htmlEscape(bitem)}',2)">▼</a></th>
			</c:if>
		</c:forEach>
	</c:forEach>
</c:if>
									</tr>
									<tr>
<c:forEach var="item" items="${itemList}">
	<c:if test="${item.display}">
		<c:choose>
		<c:when test="${item.name eq 'kihyou'}">
										<th><nobr>起票日</nobr><br><a href="#" onClick="onclickSort(11)">▲</a>    <a href="#" onClick="onclickSort(12)">▼</a></th>
										<th><nobr>起票者部門</nobr><br><a href="#" onClick="onclickSort(13)">▲</a>    <a href="#" onClick="onclickSort(14)">▼</a></th>
										<th><nobr>起票者</nobr><br><a href="#" onClick="onclickSort(15)">▲</a>    <a href="#" onClick="onclickSort(16)">▼</a></th>
										<th><nobr>社員番号</nobr><br><a href="#" onClick="onclickSort(67)">▲</a>    <a href="#" onClick="onclickSort(68)">▼</a></th>
		</c:when>
		<c:when test="${item.name eq 'shoyuu'}">
										<th><nobr>所有者部門</nobr><br><a href="#" onClick="onclickSort(21)">▲</a>    <a href="#" onClick="onclickSort(22)">▼</a></th>
										<th><nobr>所有者</nobr><br><a href="#" onClick="onclickSort(27)">▲</a>    <a href="#" onClick="onclickSort(28)">▼</a></th>
		</c:when>
		</c:choose>
	</c:if>
</c:forEach>
									</tr>
								</thead>
								<tbody id='result'>
<c:forEach var="record" items="${list}">
									<tr>
										<td align="center"><input type='checkbox' name='sentaku' value='${su:htmlEscape(record.denpyouId)}' /></td>
	<c:forEach var="item" items="${itemList}">
		<c:if test="${item.display}">
			<c:choose>
			<c:when test="${item.name eq 'joutai'}">
										<td><nobr>${su:htmlEscape(record.joutai)}(${record.cur_cnt}/${record.all_cnt})</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'id'}">
										<td><a href='${su:htmlEscape(record.denpyouUrl)}' target='_blank' class='denpyou'><nobr>${su:htmlEscape(record.denpyouId)}</nobr></a></td>
			</c:when>
			<c:when test="${item.name eq 'jisshi_kian_bangou'}">
										<td><nobr>${su:htmlEscape(record.jisshiKianBangou)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'shishutsu_kian_bangou'}">
										<td><nobr>${su:htmlEscape(record.shishutsuKianBangou)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'yosan_shikkou_taishou'}">
										<td><nobr>${su:htmlEscape(record.yosanShikkouTaishou)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'yosan_check_nengetsu'}">
										<td><nobr>${su:htmlEscape(record.yosanCheckNengetsu)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'shubetsu'}">
										<td><nobr>${su:htmlEscape(record.denpyouShubetsu)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'kingaku'}">
										<td class="text-r"><nobr>${record.kingaku}<c:if test='${record.gaika!=null}'><br>(${su:htmlEscapeBr(record.gaika)})</c:if></nobr></td>
			</c:when>
			<c:when test="${item.name eq 'shiharaiKiboubi'}">
										<td><nobr>${su:htmlEscape(record.shiharaiKiboubi)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'shiharaiBi'}">
										<td><nobr>${su:htmlEscape(record.shiharaiBi)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'torihikisaki'}">
										<td><nobr>${su:htmlEscapeBr(record.torihikisaki)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'kihyou'}">
										<td><nobr>${record.kihyouBi}</nobr></td>
										<td><nobr>${su:htmlEscapeBr(record.kihyouShozokuBumonName)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.KihyouShozokuUserName)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.KihyouShozokuUserShainNo)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'shoyuu'}">
										<td><nobr>${su:htmlEscapeBr(record.gen_bumon_full_name)}</nobr></td>
										<td><nobr>${su:htmlEscapeBr(record.gen_name)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'shiharaihouhou'}">
										<td><nobr>${su:htmlEscape(record.shiharaihouhou)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'houjin_kingaku'}">
										<td class="text-r"><nobr>${su:htmlEscape(record.houjin_kingaku)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'sashihiki_shikyuu_kingaku'}">
										<td class="text-r"><nobr>${su:htmlEscape(record.sashihiki_shikyuu_kingaku)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'keijoubi'}">
										<td><nobr>${su:htmlEscape(record.keijoubi)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'seisan_yoteibi'}">
										<td><nobr>${su:htmlEscape(record.seisan_yoteibi)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'karibarai_denpyou_id'}">
										<td><a href='${su:htmlEscape(record.karibarai_Url)}?denpyouId=${su:htmlEscape(record.karibarai_denpyou_id)}&denpyouKbn=${su:htmlEscape(record.karibarai_denpyou_kbn)}' target='_blank'><nobr>${su:htmlEscape(record.karibarai_denpyou_id)}</nobr></a></td>
			</c:when>
			<c:when test="${item.name eq 'shiwakeKeijoubi'}">
										<td><nobr>${su:htmlEscape(record.shiwakeKeijoubi)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'shouninbi'}">
										<td><nobr>${su:htmlEscape(record.shouninbi)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'serialNo'}">
										<td><nobr>${su:htmlEscapeBr(record.serialNo)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'houmonsaki'}">
										<td><nobr>${su:htmlEscapeBr(record.houmonsaki)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'mokuteki'}">
										<td><nobr>${su:htmlEscapeBr(record.mokuteki)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'kanitodoke_kenmei'}">
										<td><nobr>${su:htmlEscapeBr(record.kanitodoke_kenmei)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'kanitodoke_naiyou'}">
										<td><nobr>${su:htmlEscapeBr(record.kanitodoke_naiyou)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'user_name'}">
										<td><nobr>${su:htmlEscapeBr(record.user_name)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'seisankikan'}">
										<td><nobr>${su:htmlEscapeBr(record.seisankikan)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'hosoku'}">
										<td><nobr>${su:htmlEscapeBr(record.hosoku)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'zeiritsu'}">
							<td><nobr>${su:htmlEscapeBr(record.formattedZeiritsu)}</nobr></td>
			</c:when>
			<c:when test="${item.name eq 'jigyousha_kbn'}">
							<c:if test="${invoiceStart}">
							<td><nobr>${su:htmlEscapeBr(record.jigyoushaKbn)}</nobr></td>
							</c:if>
			</c:when>
			<c:when test="${item.name eq 'zeinuki_kingaku'}">
							<c:if test="${invoiceStart}">
							<td class="text-r"><nobr>${su:htmlEscape(record.zeinukiKingaku)}</nobr></td>
							</c:if>
			</c:when>
			<c:otherwise>
				<!-- カスタマイズ用の列追加欄：データ -->
				
				<jsp:include page="${denpyouIchiranActionStrategy ne null ? '../../../../jsp_ext/eteam/gyoumu/tsuuchi/' : ''}DenpyouIchiranAdditionalColumnsRecords.jsp">
					<jsp:param name="name" value="${item.name}" />
					<jsp:param name="record" value="${record.get(item.name)}" />
				</jsp:include>
			</c:otherwise>
			</c:choose>
		</c:if>
	</c:forEach>

<!-- 簡易届専用 -->
	<c:if test='${kanitodokeFlag}'>
		<c:forEach var="item" items="${columnList}">
			<c:choose>
			<c:when test="${item eq 'shinsei01'}">
										<td class="${record.shinsei01Class}"><nobr>${su:htmlEscapeBr(record.shinsei01)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei02'}">
										<td class="${record.shinsei02Class}"><nobr>${su:htmlEscapeBr(record.shinsei02)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei03'}">
										<td class="${record.shinsei03Class}"><nobr>${su:htmlEscapeBr(record.shinsei03)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei04'}">
										<td class="${record.shinsei04Class}"><nobr>${su:htmlEscapeBr(record.shinsei04)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei05'}">
										<td class="${record.shinsei05Class}"><nobr>${su:htmlEscapeBr(record.shinsei05)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei06'}">
										<td class="${record.shinsei06Class}"><nobr>${su:htmlEscapeBr(record.shinsei06)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei07'}">
										<td class="${record.shinsei07Class}"><nobr>${su:htmlEscapeBr(record.shinsei07)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei08'}">
										<td class="${record.shinsei08Class}"><nobr>${su:htmlEscapeBr(record.shinsei08)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei09'}">
										<td class="${record.shinsei09Class}"><nobr>${su:htmlEscapeBr(record.shinsei09)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei10'}">
										<td class="${record.shinsei10Class}"><nobr>${su:htmlEscapeBr(record.shinsei10)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei11'}">
										<td class="${record.shinsei11Class}"><nobr>${su:htmlEscapeBr(record.shinsei11)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei12'}">
										<td class="${record.shinsei12Class}"><nobr>${su:htmlEscapeBr(record.shinsei12)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei13'}">
										<td class="${record.shinsei13Class}"><nobr>${su:htmlEscapeBr(record.shinsei13)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei14'}">
										<td class="${record.shinsei14Class}"><nobr>${su:htmlEscapeBr(record.shinsei14)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei15'}">
										<td class="${record.shinsei15Class}"><nobr>${su:htmlEscapeBr(record.shinsei15)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei16'}">
										<td class="${record.shinsei16Class}"><nobr>${su:htmlEscapeBr(record.shinsei16)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei17'}">
										<td class="${record.shinsei17Class}"><nobr>${su:htmlEscapeBr(record.shinsei17)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei18'}">
										<td class="${record.shinsei18Class}"><nobr>${su:htmlEscapeBr(record.shinsei18)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei19'}">
										<td class="${record.shinsei19Class}"><nobr>${su:htmlEscapeBr(record.shinsei19)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei20'}">
										<td class="${record.shinsei20Class}"><nobr>${su:htmlEscapeBr(record.shinsei20)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei21'}">
										<td class="${record.shinsei21Class}"><nobr>${su:htmlEscapeBr(record.shinsei21)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei22'}">
										<td class="${record.shinsei22Class}"><nobr>${su:htmlEscapeBr(record.shinsei22)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei23'}">
										<td class="${record.shinsei23Class}"><nobr>${su:htmlEscapeBr(record.shinsei23)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei24'}">
										<td class="${record.shinsei24Class}"><nobr>${su:htmlEscapeBr(record.shinsei24)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei25'}">
										<td class="${record.shinsei25Class}"><nobr>${su:htmlEscapeBr(record.shinsei25)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei26'}">
										<td class="${record.shinsei26Class}"><nobr>${su:htmlEscapeBr(record.shinsei26)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei27'}">
										<td class="${record.shinsei27Class}"><nobr>${su:htmlEscapeBr(record.shinsei27)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei28'}">
										<td class="${record.shinsei28Class}"><nobr>${su:htmlEscapeBr(record.shinsei28)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei29'}">
										<td class="${record.shinsei29Class}"><nobr>${su:htmlEscapeBr(record.shinsei29)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'shinsei30'}">
										<td class="${record.shinsei30Class}"><nobr>${su:htmlEscapeBr(record.shinsei30)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai01'}">
										<td class="${record.meisai01Class}"><nobr>${su:htmlEscapeBr(record.meisai01)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai02'}">
										<td class="${record.meisai02Class}"><nobr>${su:htmlEscapeBr(record.meisai02)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai03'}">
										<td class="${record.meisai03Class}"><nobr>${su:htmlEscapeBr(record.meisai03)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai04'}">
										<td class="${record.meisai04Class}"><nobr>${su:htmlEscapeBr(record.meisai04)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai05'}">
										<td class="${record.meisai05Class}"><nobr>${su:htmlEscapeBr(record.meisai05)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai06'}">
										<td class="${record.meisai06Class}"><nobr>${su:htmlEscapeBr(record.meisai06)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai07'}">
										<td class="${record.meisai07Class}"><nobr>${su:htmlEscapeBr(record.meisai07)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai08'}">
										<td class="${record.meisai08Class}"><nobr>${su:htmlEscapeBr(record.meisai08)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai09'}">
										<td class="${record.meisai09Class}"><nobr>${su:htmlEscapeBr(record.meisai09)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai10'}">
										<td class="${record.meisai10Class}"><nobr>${su:htmlEscapeBr(record.meisai10)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai11'}">
										<td class="${record.meisai11Class}"><nobr>${su:htmlEscapeBr(record.meisai11)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai12'}">
										<td class="${record.meisai12Class}"><nobr>${su:htmlEscapeBr(record.meisai12)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai13'}">
										<td class="${record.meisai13Class}"><nobr>${su:htmlEscapeBr(record.meisai13)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai14'}">
										<td class="${record.meisai14Class}"><nobr>${su:htmlEscapeBr(record.meisai14)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai15'}">
										<td class="${record.meisai15Class}"><nobr>${su:htmlEscapeBr(record.meisai15)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai16'}">
										<td class="${record.meisai16Class}"><nobr>${su:htmlEscapeBr(record.meisai16)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai17'}">
										<td class="${record.meisai17Class}"><nobr>${su:htmlEscapeBr(record.meisai17)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai18'}">
										<td class="${record.meisai18Class}"><nobr>${su:htmlEscapeBr(record.meisai18)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai19'}">
										<td class="${record.meisai19Class}"><nobr>${su:htmlEscapeBr(record.meisai19)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai20'}">
										<td class="${record.meisai20Class}"><nobr>${su:htmlEscapeBr(record.meisai20)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai21'}">
										<td class="${record.meisai21Class}"><nobr>${su:htmlEscapeBr(record.meisai21)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai22'}">
										<td class="${record.meisai22Class}"><nobr>${su:htmlEscapeBr(record.meisai22)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai23'}">
										<td class="${record.meisai23Class}"><nobr>${su:htmlEscapeBr(record.meisai23)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai24'}">
										<td class="${record.meisai24Class}"><nobr>${su:htmlEscapeBr(record.meisai24)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai25'}">
										<td class="${record.meisai25Class}"><nobr>${su:htmlEscapeBr(record.meisai25)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai26'}">
										<td class="${record.meisai26Class}"><nobr>${su:htmlEscapeBr(record.meisai26)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai27'}">
										<td class="${record.meisai27Class}"><nobr>${su:htmlEscapeBr(record.meisai27)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai28'}">
										<td class="${record.meisai28Class}"><nobr>${su:htmlEscapeBr(record.meisai28)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai29'}">
										<td class="${record.meisai29Class}"><nobr>${su:htmlEscapeBr(record.meisai29)}</nobr></td>
			</c:when>
			<c:when test="${item eq 'meisai30'}">
										<td class="${record.meisai30Class}"><nobr>${su:htmlEscapeBr(record.meisai30)}</nobr></td>
			</c:when>
			</c:choose>
		</c:forEach>
	</c:if>
									</tr>
</c:forEach>
								</tbody>
							</table>



						</div>
						<div class='blank'></div>
						<%@ include file="/jsp/eteam/include/Paging.jsp" %>
					</section>
					<!-- Modal -->
					<div id='shiharaibiModal' class='modal hide fade' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'></div>
					<div id='dialog'></div>
					<div id='syuukeiDialog'></div>
				</form>
				<form id='shanshouKihyouForm' method='post' action = 'denpyou_sanshou_kihyou' target="_blank">
					<input type="hidden" name='urlPath'    value='${su:htmlEscape(urlPath)}'>
					<input type="hidden" name='denpyouId'  value='${su:htmlEscape(denpyouId)}'>
					<input type="hidden" name='denpyouKbn' value='${su:htmlEscape(denpyouKbn)}'>
					<input type="hidden" name='version' value='${su:htmlEscape(version)}'>
				</form>
				</div><!-- main -->
			</div> <!-- /container -->
			<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>

		<!-- スクリプト -->
		<script style='text/javascript'>





var isKaniTodokeViewInit = isKaniTodokeView();
function isKaniTodokeView(){
	return $("select[name=kensakuDenpyouShubetsu]").val().substr(0,1) == "B";
}

function changeDenpyouKbn(){

	//以下の条件を満たせば画面再表示不要
	//・変更前の伝票区分がユーザー定義届書以外
	//・変更後の伝票区分がユーザー定義届書以外
	if(!isKaniTodokeViewInit && !isKaniTodokeView()){return;};

	//簡易届用の検索項目リセット
	$("input.kaniItem").val("");
	$("input.kaniItem").removeAttr('checked');
	$("select.kaniItem").children().attr('value','');

	if(!isKaniTodokeView()){
		searchAction(0);
	}else{
		//通常伝票用の検索項目リセット
		$("input[name=kingakuFrom]").val("");
		$("input[name=kingakuTo]").val("");
		$("input[name=houjinCardRiyou]").val("");
		$("input[name=kaishaTehai]").val("");
		//$("input[name=maruhi]").val(""); //簡易届でもマル秘検索するため、共通項目と同じようにクリアさせない
		$("input[name=shiharaiKiboubiFrom]").val("");
		$("input[name=shiharaiKiboubiTo]").val("");
		$("input[name=shiharaiBiFrom]").val("");
		$("input[name=shiharaiBiTo]").val("");
		$("input[name=keijoubiFrom]").val("");
		$("input[name=keijoubiTo]").val("");
		$("input[name=shiwakeKeijoubiFrom]").val("");
		$("input[name=shiwakeKeijoubiTo]").val("");
		$("input[name=tekiyou]").val("");
		$("input[name=ryoushuushoSeikyuushoTou]").val("");
		$("input[name=miseisanKaribarai]").val("");
		$("input[name=miseisanUkagai]").val("");
		$("input[name=bumonCdFrom]").val("");
		$("input[name=bumonNameFrom]").val("");
		$("input[name=bumonCdTo]").val("");
		$("input[name=bumonNameTo]").val("");
		$("input[name=kanitodokeKenmei]").val("");
		$("input[name=kanitodokeNaiyou]").val("");
		$("input[name=shiwakeStatus]").val("");
		$("input[name=fbStatus]").val("");
		$("input[name=karikataBumonCdFrom]").val("");
		$("input[name=karikataBumonNameFrom]").val("");
		$("input[name=kashikataBumonCdFrom]").val("");
		$("input[name=kashikataBumonNameFrom]").val("");
		$("input[name=karikataKamokuCdFrom]").val("");
		$("input[name=karikataKamokuNameFrom]").val("");
		$("input[name=kashikataKamokuCdFrom]").val("");
		$("input[name=kashikataKamokuNameFrom]").val("");
		$("input[name=karikataTorihikisakiCdFrom]").val("");
		$("input[name=karikataTorihikisakiNameFrom]").val("");
		$("input[name=kashikataTorihikisakiCdFrom]").val("");
		$("input[name=kashikataTorihikisakiNameFrom]").val("");
		searchActionKanitodoke(0);
	}

}

function changeDenpyouShubetsuSiborikomi(){
	var word = $("input[name=dpkbSibori]").val();
	//種別絞込に該当する伝票種別と、最初の空白項目だけ表示
	$("select[name=kensakuDenpyouShubetsu]").find("option").each(function(){
		if (
			("" == word || "" == $(this).text() || $(this).prop("selected") || (
				wordSearch($(this).text(), word)
			))
		) {
			$(this).optionShow();
		} else{
			$(this).optionHide();
		}
	});
}

function onclickSearch(){
	if(isKaniTodokeView()){
		searchActionKanitodoke(0);
	}else{
		searchAction(0);
	}
}

function onclickSort(sort){
	//画面表示時点の伝票種別で判別
	if(isKaniTodokeViewInit){
		searchActionKanitodoke(sort);
	}else{
		searchAction(sort);
	}
}

/**
 * 検索処理
 * @param sort ソート区分
 */
 function searchAction(sort) {
	$("input[name=pageNo]").val(1);
	$("input[name=sortKbn]").val(sort);
	$("#myForm").attr("action" , "denpyou_ichiran_kensaku");
	$("#myForm").attr("method" , "get");
	$("#myForm").submit();
}


 /**
  * 検索処理(簡易届用)
  * @param sort ソート区分
  * @param type 1(asc)/2(desc)の指定。typeがない場合は、項目名▲▼のソートではない。
  */
 function searchActionKanitodoke(sort,type) {
	$("input[name=pageNo]").val(1);
	if(type == 1){
		sort = sort + " = ''," + sort + " ASC";
	}else if(type == 2){
		sort = sort + " = ''," + sort + " DESC";
	}
	$("input[name=sortKbn]").val(sort);
	$("#myForm").attr("action" , "denpyou_ichiran_kensaku_kanitodoke");
	$("#myForm").attr("method" , "get");
	$("#myForm").submit();
}


 /** 支払日一括登録 */
 function shiharaibiTouroku() {

	if($("input[name=updateShiharaibi]").val() == ""){
		alert("支払日を入力してください。");
		return;
	}

	$("#myForm").attr("action" , "denpyou_ichiran_shiharaibi_touroku");
	$("#myForm").attr("method" , "post");
	$("#myForm").submit();
 }

 /** 計上日一括更新 */
 function keijoubiKoushin() {
	if($("#dialogMain").find("[name=tmpUpdateKeijoubi]").val() == ""){
		alert("計上日を入力してください。");
		return;
	}
	$("[name=updateKeijoubi]").val($("#dialogMain").find("[name=tmpUpdateKeijoubi]").val());

	$("#myForm").attr("action" , "denpyou_ichiran_keijoubi_koushin");
	$("#myForm").attr("method" , "post");
	$("#myForm").submit();
 }

/** 変更可否制御 */
function setDisabled(){
	$("[name='bumonNameFrom']").attr("disabled", "disabled");
	$("[name='bumonNameTo']").attr("disabled", "disabled");
}

/** 集計条件表示制御 */
function shuukeiJoukenDisplay(){
	if(isKaniTodokeView()){
		$("#shuukei").show();
	}else{
		$("input[name=shuukeiKihyouBumon]").prop("checked", false);
		$("input[name=shuukeiKihyousha]").prop("checked", false);
		$("#shuukei").hide();
	}
}

/**
 * 軽減税率区分の値取得
 */
function getKeigenZeiritsuKbn(){
	var keigenZeiritsuKbn = $(this).find("option:selected").attr("data-keigenZeiritsuKbn");
	$(this).next().val(keigenZeiritsuKbn);
}

/**
 * ユーザー設定系チェックボックス変更時
 */
$("#jidouKoushinChk").click(function(){
	ichiranSetAction(this, "denpyou_ichiran_jidoukoushin_set");
});

$("#torisageHininHyoujiChk").click(function(){
	ichiranSetAction(this, "denpyou_ichiran_torisagehininhyouji_set");
});

function ichiranSetAction(control, actionName){
	let flgName = $(control).attr("name").replace("Chk", "Flg");
	if($(control).prop("checked")){
		$("input[name=" + flgName + "]").val("1");
	}else{
		$("input[name=" + flgName + "]").val("0");
	}

	$("#myForm").attr("action" , actionName);
	$("#myForm").attr("method" , "get");
	$("#myForm").submit();
}

<c:if test="${'1' eq jidouKoushinFlg}">

$(window).bind("focus",function(){
	//フォーカス外れた後ならリロード
	if($("input[name=focusChangeFlg]").val() == "1"){
		location.reload();
	}
});

$(window).bind("blur",function(){
	//一覧画面からフォーカス外れたフラグを設定
	$("input[name=focusChangeFlg]").val("1");
});

</c:if>

$(document).ready(function(){

	//集計エリアの表示制御
	shuukeiJoukenDisplay();

	//伝票種別絞込の制御
	changeDenpyouShubetsuSiborikomi();

	// e文書・添付ファイル関係
	$("[name*='ebunsho']").prop("disabled", $("select[name=tenpuFileFlg]").val() != 1);
	$("[name='tenpuFileShubetsu']").prop("disabled", $("select[name=tenpuFileFlg]").val() != 1);

	// 伝票種別変更アクション
	$("select[name=kensakuDenpyouShubetsu]").change(shuukeiJoukenDisplay);

	// 起案番号検索ボタン押下アクション
	$("#kianBangouSearch").click(function(){
		dialogRetKianBangouNendo =  $("input[name=kianBangouNendo]");
		dialogRetKianBangouRyakugou =  $("input[name=kianBangouRyakugou]");
		commonKianBangouSentaku();
	});

	// 起案番号From検索ボタン押下アクション
	$("#kianBangouFromToSearch").click(function(){
		dialogRetKianBangouNendo =  $("input[name=kianBangouNendoFromTo]");
		dialogRetKianBangouRyakugou =  $("input[name=kianBangouRyakugouFromTo]");
		commonKianBangouSentaku();
	});

	//（起票者所属部門）部門検索ボタン押下時アクション
	$("#kihyouSyozokuSearch").click(function(){
		dialogRetBumonCd =  $("input[name=kensakuKihyouShozokuBumonCd]");
		dialogRetBumonName =  $("input[name=kensakuKihyouShozokuBumonName]");
		commonBumonSentaku();
	});

	//（起票者所属部門）部門コードロストフォーカス時アクション
	$("input[name=kensakuKihyouShozokuBumonCd]").blur(function(){
		dialogRetBumonCd =  $("input[name=kensakuKihyouShozokuBumonCd]");
		dialogRetBumonName =  $("input[name=kensakuKihyouShozokuBumonName]");
		commonBumonCdLostFocus(true, dialogRetBumonCd, dialogRetBumonName, "起票者所属部門コード");
	});

	//（起票者）ユーザー検索ボタン押下時アクション
	$("#kihyouNameSearch").click(function(){
		dialogRetshainNo =  $("input[name=kensakuKihyouShozokuUserShainNo]");
		dialogRetusername =  $("input[name=kensakuKihyouShozokuUserName]");
		commonUserSentaku();
	});

	//（起票者）社員番号ロストフォーカス時Function
	$("input[name=kensakuKihyouShozokuUserShainNo]").blur(function(){
		dialogRetShainNo	= $("input[name=kensakuKihyouShozokuUserShainNo]");
		dialogRetShainName	= $("input[name=kensakuKihyouShozokuUserName]");
		commonShainNoLostFocus(dialogRetShainNo, dialogRetShainName, "起票者社員番号");
	});

	//（承認者所属部門）部門検索ボタン押下時アクション
	$("#syouninSyozokuSearch").click(function(){
		dialogRetBumonCd =  $("input[name=shouninsyaShozokuBumonCd]");
		dialogRetBumonName =  $("input[name=shouninsyaShozokuBumonName]");
		commonBumonSentaku();
	});

	//（承認者所属部門）部門コードロストフォーカス時アクション
	$("input[name=shouninsyaShozokuBumonCd]").blur(function(){
		dialogRetBumonCd =  $("input[name=shouninsyaShozokuBumonCd]");
		dialogRetBumonName =  $("input[name=shouninsyaShozokuBumonName]");
		commonBumonCdLostFocus(true, dialogRetBumonCd, dialogRetBumonName, "承認者所属部門コード");
	});

	//（承認者）ユーザー検索ボタン押下時アクション
	$("#syouninNameSearch").click(function(){
		dialogRetshainNo =  $("input[name=shouninsyaShozokuUserShainNo]");
		dialogRetusername =  $("input[name=shouninsyaShozokuUserName]");
		commonUserSentaku();
	});

	//（承認者）社員番号ロストフォーカス時Function
	$("input[name=shouninsyaShozokuUserShainNo]").blur(function(){
		dialogRetShainNo	= $("input[name=shouninsyaShozokuUserShainNo]");
		dialogRetShainName	= $("input[name=shouninsyaShozokuUserName]");
		commonShainNoLostFocus(dialogRetShainNo, dialogRetShainName, "承認者社員番号");
	});


	//（所有者所属部門）部門検索ボタン押下時アクション
	$("#shoyuuSyozokuSearch").click(function(){
		dialogRetBumonCd =  $("input[name=shoyuushaShozokuBumonCd]");
		dialogRetBumonName =  $("input[name=shoyuushaShozokuBumonName]");
		commonBumonSentaku();
	});

	//（所有者所属部門）部門コードロストフォーカス時アクション
	$("input[name=shoyuushaShozokuBumonCd]").blur(function(){
		dialogRetBumonCd =  $("input[name=shoyuushaShozokuBumonCd]");
		dialogRetBumonName =  $("input[name=shoyuushaShozokuBumonName]");
		commonBumonCdLostFocus(true, dialogRetBumonCd, dialogRetBumonName, "所有者所属部門コード");
	});

	//（所有者）ユーザー検索ボタン押下時アクション
	$("#shoyuuNameSearch").click(function(){
		dialogRetshainNo =  $("input[name=shoyuushaShozokuUserShainNo]");
		dialogRetusername =  $("input[name=shoyuushaShozokuUserName]");
		commonUserSentaku();
	});

	//（所有者）社員番号ロストフォーカス時Function
	$("input[name=shoyuushaShozokuUserShainNo]").blur(function(){
		dialogRetShainNo	= $("input[name=shoyuushaShozokuUserShainNo]");
		dialogRetShainName	= $("input[name=shoyuushaShozokuUserName]");
		commonShainNoLostFocus(dialogRetShainNo, dialogRetShainName, "所有者社員番号");
	});

	// 税率更新時、軽減税率区分も合わせて更新する
	$("body").on("change", "select[name=queryZeiritsu]" ,getKeigenZeiritsuKbn);

	// 添付ファイルコンボボックス切り替え時処理
	$("select[name=tenpuFileFlg]").change(function() {
		var shouldDisable = $(this).val() != 1;
		
		if(shouldDisable)
		{
			// テキストボックスとコンボボックスのクリア
			// チェックボックスのvalueはそのままにしておく
			$("[name*='ebunsho']").not("[type=checkbox]").val(""); // 部分一致でクローンコードをだいぶ減らせるという話

			// チェックボックスのクリア
			$("[name*='ebunsho']").prop("checked", !shouldDisable);
			$("[name='tenpuFileShubetsu']").prop("checked", !shouldDisable);
		}
		
		$("[name*='ebunsho']").prop("disabled", shouldDisable);
		$("[name='tenpuFileShubetsu']").prop("disabled", shouldDisable);
	});


    // 添付ファイル種別で、チェックボックスのチェックが外れてしまうとフォームsubmit時の配列長が変わってチェック状態が引き継がれなくなるので対策
    // ChatGPTの生成コードを参考に編集
    $("input[name=tenpuFileShubetsu]").change(function() {
        // チェックボックスがチェックされたのなら、次にあるhiddenは取り除く
        if ($(this).is(":checked")) {
            while($(this).next().is('input[type=hidden]'))
            {
            	$(this).next("input[type=hidden]").remove();
            }
        }
        // チェックが外れたのなら、hiddenを追加して配列長を維持する
        else {
            $("<input>").attr({
                type: "hidden",
                name: "tenpuFileShubetsu",
                value: "-1"
            }).insertAfter($(this));
        }
    });
    
	// 未入力系チェックボックス切り替え時処理
	// 名称一致を活用してコード削減
	// この実装で、恐らく今後同様の条件の時の追加は不要になる
	$("[name$=MinyuuryokuFlg]").on("change", function() {
		
	    var checkboxStatus = $(this).is(":checked");
	    var nameHeader = $(this).attr("name").replace("MinyuuryokuFlg", "");

	    // チェックされている時、自分自身以外の名称一致入力欄を使用不可にしてクリアする
	    $("[name*='" + nameHeader + "']").not(this).prop("disabled", checkboxStatus);

	    if(checkboxStatus)
	    {
	   		$("[name*='" + nameHeader + "']").not(this).val("");
	    }
	});
	
	// 未入力をチェックしたときのリロード用
	// e文書以外の未入力フラグを含めることになった場合は要修正
	$("[name$=MinyuuryokuFlg]").each(function()
	{
		if($("select[name=tenpuFileFlg]").val() == 1)
		{
			 $(this).change();
		}
	});
	
	//部門From検索ボタン押下時アクション
	$("#bumonFromSearch").click(function(){
		dialogRetFutanBumonCd 	= $("input[name=bumonCdFrom]");
		dialogRetFutanBumonName = $("input[name=bumonNameFrom]");
		commonFutanBumonSentaku("3");
	});

	//部門Fromロストフォーカス時Function
	$("input[name=bumonCdFrom]").blur(function(){
		dialogRetFutanBumonCd				= $("input[name=bumonCdFrom]");
		dialogRetFutanBumonName				= $("input[name=bumonNameFrom]");
		commonFutanBumonCdLostFocus("3",dialogRetFutanBumonCd, dialogRetFutanBumonName, "部門コード(From)");
	});

	//部門To検索ボタン押下時アクション
	$("#bumonToSearch").click(function(){
		dialogRetFutanBumonCd 	= $("input[name=bumonCdTo]");
		dialogRetFutanBumonName = $("input[name=bumonNameTo]");
		commonFutanBumonSentaku("3");
	});

	//部門Toロストフォーカス時Function
	$("input[name=bumonCdTo]").blur(function(){
		dialogRetFutanBumonCd				= $("input[name=bumonCdTo]");
		dialogRetFutanBumonName				= $("input[name=bumonNameTo]");
		commonFutanBumonCdLostFocus("3",dialogRetFutanBumonCd, dialogRetFutanBumonName, "部門コード(To)");
	});

	//（借方）部門From検索ボタン押下時アクション
	$("#karikataBumonFromSearch").click(function(){
		if ($("[name=bumonSentakuFlag]:checked").val() == '1') {
			dialogRetSyuukeiBumonCd = $("input[name=karikataBumonCdFrom]");
			dialogRetSyuukeiBumonName = $("input[name=karikataBumonNameFrom]");
			dialogCallbackSyuukeiBumonSentaku = function() {syuukeiBumonCdLostFocusGamen("3", dialogRetSyuukeiBumonCd, dialogRetSyuukeiBumonName, "（借方）部門コード(From)");};
			syuukeiDialogOpen("3");
		} else {
			dialogRetFutanBumonCd =  $("input[name=karikataBumonCdFrom]");
			dialogRetFutanBumonName =  $("input[name=karikataBumonNameFrom]");
			commonFutanBumonSentaku("3");
		}
	});

	//（借方）部門Fromロストフォーカス時Function
	$("input[name=karikataBumonCdFrom]").blur(function(){
		if ($("[name=bumonSentakuFlag]:checked").val() == '1') {
			dialogRetSyuukeiBumonCd = $("input[name=karikataBumonCdFrom]");
			dialogRetSyuukeiBumonName = $("input[name=karikataBumonNameFrom]");
			syuukeiBumonCdLostFocusGamen("3", dialogRetSyuukeiBumonCd, dialogRetSyuukeiBumonName, "（借方）部門コード(From)");
		} else {
			dialogRetFutanBumonCd				= $("input[name=karikataBumonCdFrom]");
			dialogRetFutanBumonName				= $("input[name=karikataBumonNameFrom]");
			commonFutanBumonCdLostFocus("3",dialogRetFutanBumonCd, dialogRetFutanBumonName, "（借方）部門コード(From)");
		}
	});

	//（借方）部門検索Toボタン押下時アクション
	$("#karikataBumonToSearch").click(function(){
		if ($("[name=bumonSentakuFlag]:checked").val() == '1') {
			dialogRetSyuukeiBumonCd = $("input[name=karikataBumonCdTo]");
			dialogRetSyuukeiBumonName = $("input[name=karikataBumonNameTo]");
			dialogCallbackSyuukeiBumonSentaku = function() {syuukeiBumonCdLostFocusGamen("3", dialogRetSyuukeiBumonCd, dialogRetSyuukeiBumonName, "（借方）部門コード(To)");};
			syuukeiDialogOpen("3");
		} else {
			dialogRetFutanBumonCd =  $("input[name=karikataBumonCdTo]");
			dialogRetFutanBumonName =  $("input[name=karikataBumonNameTo]");
			commonFutanBumonSentaku("3");
		}
	});

	//（借方）部門Toロストフォーカス時Function
	$("input[name=karikataBumonCdTo]").blur(function(){
		if ($("[name=bumonSentakuFlag]:checked").val() == '1') {
			dialogRetSyuukeiBumonCd = $("input[name=karikataBumonCdTo]");
			dialogRetSyuukeiBumonName = $("input[name=karikataBumonNameTo]");
			syuukeiBumonCdLostFocusGamen("3", dialogRetSyuukeiBumonCd, dialogRetSyuukeiBumonName, "（借方）部門コード(To)");
		} else {
			dialogRetFutanBumonCd				= $("input[name=karikataBumonCdTo]");
			dialogRetFutanBumonName				= $("input[name=karikataBumonNameTo]");
			commonFutanBumonCdLostFocus("3",dialogRetFutanBumonCd, dialogRetFutanBumonName, "（借方）部門コード(To)");
		}
	});

	//（借方）科目From検索ボタン押下時アクション
	$("#karikataKamokuFromSearch").click(function(){
		dialogRetKamokuCd =  $("input[name=karikataKamokuCdFrom]");
		dialogRetKamokuName =  $("input[name=karikataKamokuNameFrom]");
		dialogCallbackKanjyouKamokuSentaku = function(){
			$("input[name=karikataKamokuEdanoCdFrom]").val("");
			$("input[name=karikataKamokuEdanoNameFrom]").val("");
		};
		commonKamokuSentaku();
	});

	//（借方）科目Fromロストフォーカス時Function
	$("input[name=karikataKamokuCdFrom]").blur(function(){
		dialogRetKamokuCd =  $("input[name=karikataKamokuCdFrom]");
		dialogRetKamokuName =  $("input[name=karikataKamokuNameFrom]");
		dialogCallbackKanjyouKamokuSentaku = function(){
			$("input[name=karikataKamokuEdanoCdFrom]").val("");
			$("input[name=karikataKamokuEdanoNameFrom]").val("");
		};
		commonKamokuCdLostFocus(dialogRetKamokuCd,dialogRetKamokuName,"（借方）科目コード(From)");
	});

	//（借方）科目To検索ボタン押下時アクション
	$("#karikataKamokuToSearch").click(function(){
		dialogRetKamokuCd =  $("input[name=karikataKamokuCdTo]");
		dialogRetKamokuName =  $("input[name=karikataKamokuNameTo]");
		dialogCallbackKanjyouKamokuSentaku = function(){
			$("input[name=karikataKamokuEdanoCdTo]").val("");
			$("input[name=karikataKamokuEdanoNameTo]").val("");
		};
		commonKamokuSentaku();
	});

	//（借方）科目Toロストフォーカス時Function
	$("input[name=karikataKamokuCdTo]").blur(function(){
		dialogRetKamokuCd =  $("input[name=karikataKamokuCdTo]");
		dialogRetKamokuName =  $("input[name=karikataKamokuNameTo]");
		dialogCallbackKanjyouKamokuSentaku = function(){
			$("input[name=karikataKamokuEdanoCdTo]").val("");
			$("input[name=karikataKamokuEdanoNameTo]").val("");
		};
		commonKamokuCdLostFocus(dialogRetKamokuCd,dialogRetKamokuName,"（借方）科目コード(To)");
	});

	//（借方）勘定科目枝番From選択ボタン押下時Function
	$("#karikataKamokuEdanoFromSearch").click(function(e){
		dialogRetKamokuEdabanCd				= $("input[name=karikataKamokuEdanoCdFrom]");
		dialogRetKamokuEdabanName			= $("input[name=karikataKamokuEdanoNameFrom]");
		kamoku = $("[name=karikataKamokuCdFrom]").val();
		if(kamoku == null || kamoku == ""){
			alert("（借方）科目コード(From)を入力してください。");
			return;
		}
		commonKamokuEdabanSentaku(kamoku);
	});

	//（借方）勘定科目枝番Fromコードロストフォーカス時Function
	$("input[name=karikataKamokuEdanoCdFrom]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var kamokuCd = 	$("input[name=karikataKamokuCdFrom]").val();
		dialogRetKamokuEdabanCd				= $("input[name=karikataKamokuEdanoCdFrom]");
		dialogRetKamokuEdabanName			= $("input[name=karikataKamokuEdanoNameFrom]");
		commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamokuCd, "（借方）科目枝番コード(From)");
	});

	//（借方）勘定科目枝番To選択ボタン押下時Function
	$("#karikataKamokuEdanoToSearch").click(function(e){
		dialogRetKamokuEdabanCd				= $("input[name=karikataKamokuEdanoCdTo]");
		dialogRetKamokuEdabanName			= $("input[name=karikataKamokuEdanoNameTo]");
		kamoku = $("[name=karikataKamokuCdTo]").val();
		if(kamoku == null || kamoku == ""){
			alert("（借方）科目コード(From)を入力してください。");
			return;
		}
		commonKamokuEdabanSentaku(kamoku);
	});

	//（借方）勘定科目枝番Toコードロストフォーカス時Function
	$("input[name=karikataKamokuEdanoCdTo]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var kamokuCd = 	$("input[name=karikataKamokuCdTo]").val();
		dialogRetKamokuEdabanCd				= $("input[name=karikataKamokuEdanoCdTo]");
		dialogRetKamokuEdabanName			= $("input[name=karikataKamokuEdanoNameTo]");
		commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamokuCd, "（借方）科目枝番コード(To)");
	});

	//（借方）取引先From検索ボタン押下時アクション
	$("#karikataTorihikisakiFrombutton").click(function(){
		dialogRetTorihikisakiCd =  $("input[name=karikataTorihikisakiCdFrom]");
		dialogRetTorihikisakiName =  $("input[name=karikataTorihikisakiNameFrom]");
		commonTorihikisakiSentaku();
	});

	//（借方）取引先Fromロストフォーカス時Function
	$("input[name=karikataTorihikisakiCdFrom]").blur(function(){
		dialogRetTorihikisakiCd =  $("input[name=karikataTorihikisakiCdFrom]");
		dialogRetTorihikisakiName =  $("input[name=karikataTorihikisakiNameFrom]");
		commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd,dialogRetTorihikisakiName,"（借方）取引先コード(From)");
	});

	//（借方）取引先To検索ボタン押下時アクション
	$("#karikataTorihikisakiTobutton").click(function(){
		dialogRetTorihikisakiCd =  $("input[name=karikataTorihikisakiCdTo]");
		dialogRetTorihikisakiName =  $("input[name=karikataTorihikisakiNameTo]");
		commonTorihikisakiSentaku();
	});

	//（借方）取引先Toロストフォーカス時Function
	$("input[name=karikataTorihikisakiCdTo]").blur(function(){
		dialogRetTorihikisakiCd =  $("input[name=karikataTorihikisakiCdTo]");
		dialogRetTorihikisakiName =  $("input[name=karikataTorihikisakiNameTo]");
		commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd,dialogRetTorihikisakiName,"（借方）取引先コード(To)");
	});

	//（貸方）部門From検索ボタン押下時アクション
	$("#kashikataBumonFromSearch").click(function(){
		if ($("[name=bumonSentakuFlag]:checked").val() == '1') {
			dialogRetSyuukeiBumonCd = $("input[name=kashikataBumonCdFrom]");
			dialogRetSyuukeiBumonName = $("input[name=kashikataBumonNameFrom]");
			dialogCallbackSyuukeiBumonSentaku = function() {syuukeiBumonCdLostFocusGamen("3", dialogRetSyuukeiBumonCd, dialogRetSyuukeiBumonName, "（貸方）部門コード(From)");};
			syuukeiDialogOpen("3");
		} else {
			dialogRetFutanBumonCd =  $("input[name=kashikataBumonCdFrom]");
			dialogRetFutanBumonName =  $("input[name=kashikataBumonNameFrom]");
			commonFutanBumonSentaku("3");
		}
	});

	//（貸方）部門Fromロストフォーカス時Function
	$("input[name=kashikataBumonCdFrom]").blur(function(){
		if ($("[name=bumonSentakuFlag]:checked").val() == '1') {
			dialogRetSyuukeiBumonCd = $("input[name=kashikataBumonCdFrom]");
			dialogRetSyuukeiBumonName = $("input[name=kashikataBumonNameFrom]");
			syuukeiBumonCdLostFocusGamen("3", dialogRetSyuukeiBumonCd, dialogRetSyuukeiBumonName, "（貸方）部門コード(From)");
		} else {
			dialogRetFutanBumonCd				= $("input[name=kashikataBumonCdFrom]");
			dialogRetFutanBumonName				= $("input[name=kashikataBumonNameFrom]");
			commonFutanBumonCdLostFocus("3",dialogRetFutanBumonCd, dialogRetFutanBumonName, "（貸方）部門コード(From)");
		}
	});

	//（貸方）部門検索Toボタン押下時アクション
	$("#kashikataBumonToSearch").click(function(){
		if ($("[name=bumonSentakuFlag]:checked").val() == '1') {
			dialogRetSyuukeiBumonCd = $("input[name=kashikataBumonCdTo]");
			dialogRetSyuukeiBumonName = $("input[name=kashikataBumonNameTo]");
			dialogCallbackSyuukeiBumonSentaku = function() {syuukeiBumonCdLostFocusGamen("3", dialogRetSyuukeiBumonCd, dialogRetSyuukeiBumonName, "（貸方）部門コード(To)");};
			syuukeiDialogOpen("3");
		} else {
			dialogRetFutanBumonCd =  $("input[name=kashikataBumonCdTo]");
			dialogRetFutanBumonName =  $("input[name=kashikataBumonNameTo]");
			commonFutanBumonSentaku("3");
		}
	});

	//（貸方）部門Toロストフォーカス時Function
	$("input[name=kashikataBumonCdTo]").blur(function(){
		if ($("[name=bumonSentakuFlag]:checked").val() == '1') {
			dialogRetSyuukeiBumonCd = $("input[name=kashikataBumonCdTo]");
			dialogRetSyuukeiBumonName = $("input[name=kashikataBumonNameTo]");
			syuukeiBumonCdLostFocusGamen("3", dialogRetSyuukeiBumonCd, dialogRetSyuukeiBumonName, "（貸方）部門コード(To)");
		} else {
			dialogRetFutanBumonCd				= $("input[name=kashikataBumonCdTo]");
			dialogRetFutanBumonName				= $("input[name=kashikataBumonNameTo]");
			commonFutanBumonCdLostFocus("3",dialogRetFutanBumonCd, dialogRetFutanBumonName, "（貸方）部門コード(To)");
		}
	});

	//（貸方）科目検索ボタン押下時アクション
	$("#kashikataKamakuFromSearch").click(function(){
		dialogRetKamokuCd =  $("input[name=kashikataKamokuCdFrom]");
		dialogRetKamokuName =  $("input[name=kashikataKamokuNameFrom]");
		dialogCallbackKanjyouKamokuSentaku = function(){
			$("input[name=kashikataKamokuEdanoCdFrom]").val("");
			$("input[name=kashikataKamokuEdanoNameFrom]").val("");
		};
		commonKamokuSentaku();
	});

	//（貸方）科目ロストフォーカス時Function
	$("input[name=kashikataKamokuCdFrom]").blur(function(){
		dialogRetKamokuCd =  $("input[name=kashikataKamokuCdFrom]");
		dialogRetKamokuName =  $("input[name=kashikataKamokuNameFrom]");
		dialogCallbackKanjyouKamokuSentaku = function(){
			$("input[name=kashikataKamokuEdanoCdFrom]").val("");
			$("input[name=kashikataKamokuEdanoNameFrom]").val("");
		};
		commonKamokuCdLostFocus(dialogRetKamokuCd,dialogRetKamokuName,"（貸方）科目コード(From)");
	});

	//（貸方）科目検索ボタン押下時アクション
	$("#kashikataKamakuToSearch").click(function(){
		dialogRetKamokuCd =  $("input[name=kashikataKamokuCdTo]");
		dialogRetKamokuName =  $("input[name=kashikataKamokuNameTo]");
		dialogCallbackKanjyouKamokuSentaku = function(){
			$("input[name=kashikataKamokuEdanoCdTo]").val("");
			$("input[name=kashikataKamokuEdanoNameTo]").val("");
		};
		commonKamokuSentaku();
	});

	//（貸方）科目ロストフォーカス時Function
	$("input[name=kashikataKamokuCdTo]").blur(function(){
		dialogRetKamokuCd =  $("input[name=kashikataKamokuCdTo]");
		dialogRetKamokuName =  $("input[name=kashikataKamokuNameTo]");
		dialogCallbackKanjyouKamokuSentaku = function(){
			$("input[name=kashikataKamokuEdanoCdTo]").val("");
			$("input[name=kashikataKamokuEdanoNameTo]").val("");
		};
		commonKamokuCdLostFocus(dialogRetKamokuCd,dialogRetKamokuName,"（貸方）科目コード(To)");
	});

	//（貸方）勘定科目枝番From選択ボタン押下時Function
	$("#kashikataKamokuEdanoFromSearch").click(function(e){
		dialogRetKamokuEdabanCd				= $("input[name=kashikataKamokuEdanoCdFrom]");
		dialogRetKamokuEdabanName			= $("input[name=kashikataKamokuEdanoNameFrom]");
		kamoku = $("[name=kashikataKamokuCdFrom]").val();
		if(kamoku == null || kamoku == ""){
			alert("（貸方）科目枝番コード(From)を入力してください。");
			return;
		}
		commonKamokuEdabanSentaku(kamoku);
	});

	//（貸方）勘定科目枝番Fromコードロストフォーカス時Function
	$("input[name=kashikataKamokuEdanoCdFrom]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var kamokuCd = 	$("input[name=kashikataKamokuCdFrom]").val();
		dialogRetKamokuEdabanCd				= $("input[name=kashikataKamokuEdanoCdFrom]");
		dialogRetKamokuEdabanName			= $("input[name=kashikataKamokuEdanoNameFrom]");
		commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamokuCd, "（貸方）科目枝番コード(From)");
	});

	//（貸方）勘定科目枝番To選択ボタン押下時Function
	$("#kashikataKamokuEdanoToSearch").click(function(e){
		dialogRetKamokuEdabanCd				= $("input[name=kashikataKamokuEdanoCdTo]");
		dialogRetKamokuEdabanName			= $("input[name=kashikataKamokuEdanoNameTo]");
		kamoku = $("[name=kashikataKamokuCdTo]").val();
		if(kamoku == null || kamoku == ""){
			alert("（貸方）科目枝番コード(To)を入力してください。");
			return;
		}
		commonKamokuEdabanSentaku(kamoku);
	});

	//（貸方）勘定科目枝番Toコードロストフォーカス時Function
	$("input[name=kashikataKamokuEdanoCdTo]").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		var kamokuCd = 	$("input[name=kashikataKamokuCdTo]").val();
		dialogRetKamokuEdabanCd				= $("input[name=kashikataKamokuEdanoCdTo]");
		dialogRetKamokuEdabanName			= $("input[name=kashikataKamokuEdanoNameTo]");
		commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamokuCd, "（貸方）科目枝番コード(To)");
	});

	//（貸方）取引先検索ボタン押下時アクション
	$("#kashikataTorihikisakiFromSearch").click(function(){
		dialogRetTorihikisakiCd =  $("input[name=kashikataTorihikisakiCdFrom]");
		dialogRetTorihikisakiName =  $("input[name=kashikataTorihikisakiNameFrom]");
		commonTorihikisakiSentaku();
	});

	//（貸方）取引先ロストフォーカス時Function
	$("input[name=kashikataTorihikisakiCdFrom]").blur(function(){
		dialogRetTorihikisakiCd =  $("input[name=kashikataTorihikisakiCdFrom]");
		dialogRetTorihikisakiName =  $("input[name=kashikataTorihikisakiNameFrom]");
		commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd,dialogRetTorihikisakiName,"（貸方）取引先コード(From)");
	});

	//（貸方）取引先検索ボタン押下時アクション
	$("#kashikataTorihikisakiToSearch").click(function(){
		dialogRetTorihikisakiCd =  $("input[name=kashikataTorihikisakiCdTo]");
		dialogRetTorihikisakiName =  $("input[name=kashikataTorihikisakiNameTo]");
		commonTorihikisakiSentaku();
	});

	//（貸方）取引先ロストフォーカス時Function
	$("input[name=kashikataTorihikisakiCdTo]").blur(function(){
		dialogRetTorihikisakiCd =  $("input[name=kashikataTorihikisakiCdTo]");
		dialogRetTorihikisakiName =  $("input[name=kashikataTorihikisakiNameTo]");
		commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd,dialogRetTorihikisakiName,"（貸方）取引先コード(To)");
	});

	/**
	 * 部門選択ラジオボタンチェンジFunction
	 */
	$( 'input[name="bumonSentakuFlag"]:radio' ).change( function() {
		$("input[name=karikataBumonCdFrom]").val("");
		$("input[name=karikataBumonNameFrom]").val("");
		$("input[name=karikataBumonCdTo]").val("");
		$("input[name=karikataBumonNameTo]").val("");
		$("input[name=kashikataBumonCdFrom]").val("");
		$("input[name=kashikataBumonNameFrom]").val("");
		$("input[name=kashikataBumonCdTo]").val("");
		$("input[name=kashikataBumonNameTo]").val("");
	});

	// 簡易届負担部門選択ボタンアクション
	$("button[name=futanBumonSentakuButton]").click(function(){
		dialogRetFutanBumonCd = $(this).parent().find(".kani_futan_cd");
		dialogRetFutanBumonName = $(this).parent().find(".kani_name");
		commonFutanBumonSentaku("3");
	});
	// 簡易届負担部門コードロストフォーカスアクション
	$(".kani_futan_cd").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetFutanBumonCd = $(this).parent().find(".kani_futan_cd");
		dialogRetFutanBumonName = $(this).parent().find(".kani_name");
		commonFutanBumonCdLostFocus("2",dialogRetFutanBumonCd, dialogRetFutanBumonName, title);
	});

	// 簡易届科目選択ボタンアクション
	$("button[name=kanjyouKamokuSentakuButton]").click(function(){
		dialogRetKamokuCd = $(this).parent().find(".kani_kamoku_cd");
		dialogRetKamokuName = $(this).parent().find(".kani_name");
		commonKamokuSentaku();
	});
	// 簡易届科目コードロストフォーカスアクション
	$(".kani_kamoku_cd").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetKamokuCd = $(this).parent().find(".kani_kamoku_cd");
		dialogRetKamokuName = $(this).parent().find(".kani_name");
		commonKamokuCdLostFocus(dialogRetKamokuCd, dialogRetKamokuName, title);
	});


	// 簡易届科目枝番選択ボタンアクション
	$("button[name=kanjyouKamokuEdabanSentakuButton]").click(function(){
		dialogRetKamokuEdabanCd = $(this).parent().find(".kani_kamokuedaban_cd");
		dialogRetKamokuEdabanName = $(this).parent().find(".kani_name");
		var kamoku = "";
		if($(this).parent().find(".kani_kamokuedaban_cd").hasClass("syuunyuu_eda_num")){
			kamoku = $("input.syuunyuu_kamoku").val();
			if(kamoku == null || kamoku == ""){
				alert("収入科目を入力してください。");
				return;
			}
		}else if($(this).parent().find(".kani_kamokuedaban_cd").hasClass("shishutsu_eda_num")){
			kamoku = $("input.shishutsu_kamoku").val();
			if(kamoku == null || kamoku == ""){
				alert("支出科目を入力してください。");
				return;
			}
		}
		commonKamokuEdabanSentaku(kamoku);
	});
	// 簡易届科目枝番コードロストフォーカスアクション
	$(".kani_kamokuedaban_cd").blur(function(){
		dialogRetKamokuEdabanCd = $(this).parent().find(".kani_kamokuedaban_cd");
		dialogRetKamokuEdabanName = $(this).parent().find(".kani_name");
		var title = $(this).parent().parent().find(".control-label").text();
		var kamoku = "";
		if($(this).parent().find(".kani_kamokuedaban_cd").hasClass("syuunyuu_eda_num")){
			kamoku = $("input.syuunyuu_kamoku").val();
			if(kamoku == null || kamoku == ""){
				alert("収入科目を入力してください。");
				dialogRetKamokuEdabanCd.val("");
				return;
			}
		}else if($(this).parent().find(".kani_kamokuedaban_cd").hasClass("shishutsu_eda_num")){
			kamoku = $("input.shishutsu_kamoku").val();
			if(kamoku == null || kamoku == ""){
				alert("支出科目を入力してください。");
				dialogRetKamokuEdabanCd.val("");
				return;
			}
		}
		commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamoku, title);
	});


	// 簡易届取引先選択ボタンアクション
	$("button[name=torihikisakiSentakuButton]").click(function(){
		dialogRetTorihikisakiCd = $(this).parent().find(".kani_tori_cd");
		dialogRetTorihikisakiName = $(this).parent().find(".kani_name");
		commonTorihikisakiSentaku($("[name=kensakuDenpyouShubetsu]").val());
	});
	// 簡易届取引先コードロストフォーカスアクション
	$(".kani_tori_cd").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetTorihikisakiCd = $(this).parent().find(".kani_tori_cd");
		dialogRetTorihikisakiName = $(this).parent().find(".kani_name");
		commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd, dialogRetTorihikisakiName, title, null, $("[name=kensakuDenpyouShubetsu]").val());
	});




	// 簡易届UF1選択ボタンアクション
	$("button[name=UF1SentakuButton]").click(function(){
		dialogRetUniversalFieldCd = $(this).parent().find(".kani_uf1_cd");
		dialogRetUniversalFieldName = $(this).parent().find(".kani_name");
		commonUniversalSentaku("", "1");
	});
	// 簡易届UF1ロストフォーカスアクション
	$(".kani_uf1_cd").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetUniversalFieldCd = $(this).parent().find(".kani_uf1_cd");
		dialogRetUniversalFieldName = $(this).parent().find(".kani_name");
		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 1, title);
	});
	// 簡易届UF2選択ボタンアクション
	$("button[name=UF2SentakuButton]").click(function(){
		dialogRetUniversalFieldCd = $(this).parent().find(".kani_uf2_cd");
		dialogRetUniversalFieldName = $(this).parent().find(".kani_name");
		commonUniversalSentaku("", "2");
	});
	// 簡易届UF2ロストフォーカスアクション
	$(".kani_uf2_cd").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetUniversalFieldCd = $(this).parent().find(".kani_uf2_cd");
		dialogRetUniversalFieldName = $(this).parent().find(".kani_name");
		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 2, title);
	});
	// 簡易届UF3選択ボタンアクション
	$("button[name=UF3SentakuButton]").click(function(){
		dialogRetUniversalFieldCd = $(this).parent().find(".kani_uf3_cd");
		dialogRetUniversalFieldName = $(this).parent().find(".kani_name");
		commonUniversalSentaku("", "3");
	});
	// 簡易届UF3ロストフォーカスアクション
	$(".kani_uf3_cd").blur(function(){
		var title = $(this).parent().parent().find(".control-label").text();
		dialogRetUniversalFieldCd = $(this).parent().find(".kani_uf3_cd");
		dialogRetUniversalFieldName = $(this).parent().find(".kani_name");
		commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 3, title);
	});



	//条件クリアボタン押下
	$("button[name=joukenClearBtn]").click(function(e){
		// 共通のクリア処理は、実際にはクリアしなくていい項目も見た目上書き換えてしまうので退避
		let isJidouKoushin = $("#jidouKoushinChk").is(":checked");
		let isTorisageHininHyouji = $("#torisageHininHyoujiChk").is(":checked");
		
		// 共通コードのクリア処理
		inputClear($(this).closest("form"));

		// 退避内容の復元
		$("#jidouKoushinChk").prop("checked", isJidouKoushin);
		$("#torisageHininHyoujiChk").prop("checked", isTorisageHininHyouji);

		// e文書関連使用可否の切り替え
		$("select[name=tenpuFileFlg]").change();
		
<c:if test="${yosanKannriFlg}">
		//デフォルト値指定の項目はデフォルト値が表示されるよう制御
		$("select[name='kianBangouInput']").find("option:first").prop("selected", true);
		$("select[name='kianBangouEnd']").find("option:first").prop("selected", true);
		$("select[name='kianBangouUnyou']").find("option:first").prop("selected", true);
		$("select[name='kianBangouNoShishutsuIrai']").find("option:first").prop("selected", true);
</c:if>
		//直前表示伝票種別が簡易届なら伝票一覧の初期表示呼出
		if(isKaniTodokeViewInit){
			searchAction(0);
		}
	});

	//詳細ボタン押下
	 $("button[name=shousaiKensakuBtn]").click(function(e){
		 var value = $("input[name='shousaiKensakuHyoujiFlg']").val();
		 if(value == "0"){
			 value = "1";
		 }else{
			 value = "0";
		 }
		 $("input[name='shousaiKensakuHyoujiFlg']").val(value);

		 // URL変更
		var tmp = $("input[name=beforeSelect]").val();
		if(value == "1"){
			 // beforeSelect
			$("input[name=beforeSelect]").val(tmp.replace("shousaiKensakuHyoujiFlg=0", "shousaiKensakuHyoujiFlg=1"));
			// ページング
			$("#paging").find("a").each(function(){
				var url = $(this).attr("href");
				$(this).attr("href", url.replace("shousaiKensakuHyoujiFlg=0", "shousaiKensakuHyoujiFlg=1"));
			});
		}else{
			// beforeSelect
			var tmp = $("input[name=beforeSelect]").val();
			$("input[name=beforeSelect]").val(tmp.replace("shousaiKensakuHyoujiFlg=1", "shousaiKensakuHyoujiFlg=0"));
			// ページング
			$("#paging").find("a").each(function(){
				var url = $(this).attr("href");
				$(this).attr("href", url.replace("shousaiKensakuHyoujiFlg=1", "shousaiKensakuHyoujiFlg=0"));
			});
		 }
	 });

	//チェック
	$("#allcheck").click(function(){
		$("#result").find("input[type='checkbox']").prop('checked', $(this).prop("checked"));
	});

	//参照起票
	$("#sanshouKihyouBtn").click(function(){
		var form = $("#shanshouKihyouForm");

		var sentaku = $("[name=sentaku]:checked");
		if (sentaku.length != 1) {
			alert("参照起票元を1件のみ選択してください。");
			return;
		}

		var urlHash = getUrlParams(sentaku.closest("tr").find("a.denpyou").attr("href"));
		form.find("[name=urlPath]")		.val(urlHash["url"]);
		form.find("[name=denpyouId]")	.val(urlHash["denpyouId"]);
		form.find("[name=denpyouKbn]")	.val(urlHash["denpyouKbn"]);
		if (urlHash["version"])
		form.find("[name=version]")		.val(urlHash["version"]);
		form.submit();
	});

	// 承認
	$("#shouninBtn").click(function(){
		//画面表示時点の伝票種別で判別
		if(isKaniTodokeViewInit){
			$("#myForm").attr("action" , "denpyou_ichiran_shounin_kanitodoke");
		}else{
			$("#myForm").attr("action" , "denpyou_ichiran_shounin");
		}

		$("#myForm").attr("method" , "post");
		$("#myForm").submit();
	});


	 // 支払日登録
	 $("#shiharaibiBtn").click(function() {
		//画面表示時点の伝票種別で判別
		if(isKaniTodokeViewInit){
			$("#shiharaibiBtn").attr("href","#");		//モーダル遷移させなくする
			alert("選択中の伝票種別では支払日登録を行うことはできません。");
			return;
		}

		$("#shiharaibiModal").empty();
	 	$("#shiharaibiModal").append("<div class='modal-header'><button type='button' class='close' data-dismiss='modal' aria-hidden='true'>×<\/button><h3 id='myModalLabel'>支払日登録<\/h3><\/div><div class='modal-body'><p>支払日<\/p><input type='text' class='date input-small datepicker' name='updateShiharaibi' value='${su:htmlEscape(updateShiharaibi)}'><\/div><div class='modal-footer'><button class='btn' data-dismiss='modal' aria-hidden='true'>閉じる<\/button><button type='button' class='btn btn-primary' onClick='shiharaibiTouroku()'>支払日登録<\/button><\/div>");
 	 	$("#shiharaibiModal").find("input.datepicker:enabled:not([readonly])").attr("id", "").removeClass('hasDatepicker').datepicker();
 	 	$("#shiharaibiModal").find("input.datepicker:enabled:not([readonly])").myDatePicker();
 	 	$("input[name=updateShiharaibi]").val("");
	 });

	// 起案終了
	$("#ikkatsuKianSyuryoBtn").click(function(){
		$("input[name=kianSyuryoType]").val("1");
		//画面表示時点の伝票種別で判別
		if(isKaniTodokeViewInit){
			$("#myForm").attr("action" , "denpyou_ikkatsu_kian_syuryo_kanitodoke");
		}else{
			$("#myForm").attr("action" , "denpyou_ikkatsu_kian_syuryo");
		}

		$("#myForm").attr("method" , "post");
		$("#myForm").submit();
	});

	// 起案終了解除
	$("#ikkatsuKianSyuryoKaijyoBtn").click(function(){
		$("input[name=kianSyuryoType]").val("0");
		//画面表示時点の伝票種別で判別
		if(isKaniTodokeViewInit){
			$("#myForm").attr("action" , "denpyou_ikkatsu_kian_syuryo_kanitodoke");
		}else{
			$("#myForm").attr("action" , "denpyou_ikkatsu_kian_syuryo");
		}

		$("#myForm").attr("method" , "post");
		$("#myForm").submit();
	});

	// 計上日更新
	 $("#keijoubiBtn").click(function() {
		 $("#dialog").dialog({
			modal: true,
			width: "300",
			height: "300",
			title: "計上日更新",
			buttons: {
				更新: function() {keijoubiKoushin();}
				,閉じる: function() {$(this).dialog("close");}
			},
			close: function() {
				$("#dialog").children().remove();
			}
		}).load("denpyou_ichiran_keijoubi"
			,{denpyouKbn:$("[name=kensakuDenpyouShubetsu]").val()});
	 });

	//CSV出力
	$("#csvBtn").click(function(){
		//画面表示時点の伝票種別で判別
		if(isKaniTodokeView()){
			$("#myForm").attr("action" , "denpyou_ichiran_csvoutput_kanitodoke");
		}else{
			$("#myForm").attr("action" , "denpyou_ichiran_csvoutput");
		}

		$("#myForm").attr("method" , "post");
		$("#myForm").submit();
		setDisabled();
	});

	//起案一覧CSV出力
	$("#kianIchiranCsvBtn").click(function(){
		//画面表示時点の伝票種別で判別
		if(isKaniTodokeView()){
			$("#myForm").attr("action" , "denpyou_ichiran_kian_csvoutput_kanitodoke");
		}else{
			$("#myForm").attr("action" , "denpyou_ichiran_kian_csvoutput");
		}

		$("#myForm").attr("method" , "post");
		$("#myForm").submit();
		setDisabled();
	});

	//ダウンロード
	$("#downLoadBtn").click(function(){
		//画面表示時点の伝票種別で判別
		if(isKaniTodokeView()){
			$("#myForm").attr("action" , "denpyou_ichiran_filedownload_kanitodoke");
		}else{
			$("#myForm").attr("action" , "denpyou_ichiran_filedownload");
		}

		$("#myForm").attr("method" , "post");
		$("#myForm").submit();
		setDisabled();
	});

	// 検索条件＋ボタン押下
	 $("button[name=kensakuOpen]").click(function(e){
		$("#kensakuList").show(300);
		if($("input[name=shousaiKensakuHyoujiFlg]").val() == 1){
			$("#syousaiKensakuList").show(300);
		}
		$("#kensakuButton").show(300);
		// +ボタン非表示
		$("#kensakuOpen").hide();
		// +ボタン表示
		$("#kensakuClose").show();

		// 検索条件表示フラグをonに設定
		$("input[name=kensakuJoukenHyoujiFlg]").val(1);

		// URL変更
		// beforeSelect
		var tmp = $("input[name=beforeSelect]").val();
		$("input[name=beforeSelect]").val(tmp.replace("kensakuJoukenHyoujiFlg=0", "kensakuJoukenHyoujiFlg=1"));
		// ページング
		$("#paging").find("a").each(function(){
			var url = $(this).attr("href");
			$(this).attr("href", url.replace("kensakuJoukenHyoujiFlg=0", "kensakuJoukenHyoujiFlg=1"));
		});
 	 });

	// 検索条件－ボタン押下
	$("button[name=kensakuClose]").click(function(e){
		$("#kensakuList").hide(300);
		if($("input[name=shousaiKensakuHyoujiFlg]").val() == 1){
			 $("#syousaiKensakuList").hide(300);
		}
		$("#kensakuButton").hide(300);
		// +ボタン表示
		$("#kensakuOpen").show();
		// -ボタン非表示
		$("#kensakuClose").hide();

		// 検索条件表示フラグをoffに設定
		$("input[name=kensakuJoukenHyoujiFlg]").val(0);

		// URL変更
		// beforeSelect
		var tmp = $("input[name=beforeSelect]").val();
		$("input[name=beforeSelect]").val(tmp.replace("kensakuJoukenHyoujiFlg=1", "kensakuJoukenHyoujiFlg=0"));
		// ページング
		$("#paging").find("a").each(function(){
			var url = $(this).attr("href");
			$(this).attr("href", url.replace("kensakuJoukenHyoujiFlg=1", "kensakuJoukenHyoujiFlg=0"));
		});
	 });

	// 表示項目カスタマイズ
	$("#itemCustomizeBtn").click(function(){
		var dialog = null;
		var width = "470";
		if(windowSizeChk()) {
			width = $(window).width() * 0.9;
		}
		dialog = $("#dialog").dialog({
			modal: true,
			width: width,
			height: "635",
			title: "表示項目カスタマイズ",
			buttons: {
				全ユーザー共通設定に戻す:function() {
					if(window.confirm('表示項目・順序を全ユーザー共通設定に戻してもよろしいですか？')) {
						dialog.load(
								"denpyou_ichiran_item_delete",
								{},
								//callback
								function() {
									// エラーは不正アクセスだけなので、無条件にダイアログクローズ・リフレッシュ。
									$(this).dialog("close");

									//画面表示時点の伝票種別で判別
									if(isKaniTodokeView()){
										$("#myForm").attr("action" , "denpyou_ichiran_kensaku_kanitodoke");
									}else{
										$("#myForm").attr("action" , "denpyou_ichiran_kensaku");
									}

									$("#myForm").attr("method" , "get");
									$("#myForm").submit();
								}
							);
					}
				},
				更新: function() {
					//dialog内のinputをPOSTで送信し、dialogにloadする。
					dialog.load(
						"denpyou_ichiran_item_update",
						$.map(
							$(dialog).find("input"),
							function(n, i) {
								if (!n.name) {
									return null;
								}
								//checkboxは本来のsubmit同様、チェックが無ければ送信しない。
								if (n.type == 'checkbox' && !$(n).prop('checked')) {
									return null;
								}
								return {
									name  : n.name,
									value : $(n).val()
								};
							}
						),
						//callback
						function() {
							// エラーは不正アクセスだけなので、無条件にダイアログクローズ・リフレッシュ。
							$(this).dialog("close");

							//画面表示時点の伝票種別で判別
							if(isKaniTodokeView()){
								$("#myForm").attr("action" , "denpyou_ichiran_kensaku_kanitodoke");
							}else{
								$("#myForm").attr("action" , "denpyou_ichiran_kensaku");
							}

							$("#myForm").attr("method" , "get");
							$("#myForm").submit();
						}
					);
				},
				閉じる: function() {$(this).dialog("close");}
			},
			close: function() {
				$("#dialog").children().remove();
			}
		});

		dialog.load("denpyou_ichiran_item_init");
	});

});
		</script>
	</body>
</html>
