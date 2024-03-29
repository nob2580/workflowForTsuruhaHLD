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
		<title>取引一括登録確認｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
	</head>

	<body>
    	<div id='wrap'>

    		<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
				<h1>取引一括登録確認</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='tourokuForm' class='form-horizontal' method='post' enctype="multipart/form-data">

					<!-- CSVファイル -->
					<section>
						<h2>CSVファイル</h2>
						<b>${su:htmlEscape(csvFileName)}</b>
					</section>

					<section>
						<h2>登録内容</h2>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>#</th>
<c:if test='${status eq "End"}'>
										<th>処理結果</th>
</c:if>
										<th>伝票区分</th>
										<th>仕訳枝番号</th>
										<th>削除フラグ</th>
										<th>有効期限開始日</th>
										<th>有効期限終了日</th>
										<th>分類1</th>
										<th>分類2</th>
										<th>分類3</th>
										<th>取引名</th>
										<th>摘要フラグ</th>
										<th>摘要</th>
										<th>デフォルト表示フラグ</th>
										<th>交際費表示フラグ</th>
										<th>人数項目表示フラグ</th>
										<th>交際費基準額</th>
										<th>交際費チェック方法</th>
										<th>交際費チェック後登録許可</th>
										<th>掛けフラグ</th>
										<th>表示順</th>
										<th>社員コード連携フラグ</th>
										<th>財務枝番コード連携フラグ</th>
										<!-- 借方 -->
										<th>借方負担部門コード（仕訳パターン）</th>
										<th>借方科目コード（仕訳パターン）</th>
										<th>借方科目枝番コード（仕訳パターン）</th>
										<th>借方取引先コード（仕訳パターン）</th>
										<th>借方プロジェクトコード（仕訳パターン）</th>
										<th>借方セグメントコード（仕訳パターン）</th>
										<th>借方UFコード1</th>
										<th>借方UFコード2</th>
										<th>借方UFコード3</th>
<c:if test='${issias}'>
										<th>借方UFコード4</th>
										<th>借方UFコード5</th>
										<th>借方UFコード6</th>
										<th>借方UFコード7</th>
										<th>借方UFコード8</th>
										<th>借方UFコード9</th>
										<th>借方UFコード10</th>
										<th>借方UFコード(固定)1</th>
										<th>借方UFコード(固定)2</th>
										<th>借方UFコード(固定)3</th>
										<th>借方UFコード(固定)4</th>
										<th>借方UFコード(固定)5</th>
										<th>借方UFコード(固定)6</th>
										<th>借方UFコード(固定)7</th>
										<th>借方UFコード(固定)8</th>
										<th>借方UFコード(固定)9</th>
										<th>借方UFコード(固定)10</th>
</c:if>
										<th>借方課税区分（仕訳パターン）</th>
										<th>借方消費税率（仕訳パターン）</th>
										<th>借方軽減税率区分（仕訳パターン）</th>
										<th>借方分離区分（仕訳パターン）</th>
										<th>借方仕入区分（仕訳パターン）</th>
										<!-- 貸方1 -->
										<th>貸方負担部門コード１（仕訳パターン）</th>
										<th>貸方科目コード１（仕訳パターン）</th>
										<th>貸方科目枝番コード１（仕訳パターン）</th>
										<th>貸方取引先コード１（仕訳パターン）</th>
										<th>貸方プロジェクトコード１（仕訳パターン）</th>
										<th>貸方セグメントコード１（仕訳パターン）</th>
										<th>貸方１UFコード1</th>
										<th>貸方１UFコード2</th>
										<th>貸方１UFコード3</th>
<c:if test='${issias}'>
										<th>貸方１UFコード4</th>
										<th>貸方１UFコード5</th>
										<th>貸方１UFコード6</th>
										<th>貸方１UFコード7</th>
										<th>貸方１UFコード8</th>
										<th>貸方１UFコード9</th>
										<th>貸方１UFコード10</th>
										<th>貸方１UFコード(固定)1</th>
										<th>貸方１UFコード(固定)2</th>
										<th>貸方１UFコード(固定)3</th>
										<th>貸方１UFコード(固定)4</th>
										<th>貸方１UFコード(固定)5</th>
										<th>貸方１UFコード(固定)6</th>
										<th>貸方１UFコード(固定)7</th>
										<th>貸方１UFコード(固定)8</th>
										<th>貸方１UFコード(固定)9</th>
										<th>貸方１UFコード(固定)10</th>
</c:if>
										<th>貸方課税区分１（仕訳パターン）</th>
										<th>貸方分離区分１（仕訳パターン）</th>
										<th>貸方仕入区分１（仕訳パターン）</th>
										<!-- 貸方2 -->
										<th>貸方負担部門コード２（仕訳パターン）</th>
										<th>貸方取引先コード２（仕訳パターン）</th>
										<th>貸方科目コード２（仕訳パターン）</th>
										<th>貸方科目枝番コード２（仕訳パターン）</th>
										<th>貸方プロジェクトコード２（仕訳パターン）</th>
										<th>貸方セグメントコード２（仕訳パターン）</th>
										<th>貸方２UFコード1</th>
										<th>貸方２UFコード2</th>
										<th>貸方２UFコード3</th>
<c:if test='${issias}'>
										<th>貸方２UFコード4</th>
										<th>貸方２UFコード5</th>
										<th>貸方２UFコード6</th>
										<th>貸方２UFコード7</th>
										<th>貸方２UFコード8</th>
										<th>貸方２UFコード9</th>
										<th>貸方２UFコード10</th>
										<th>貸方２UFコード(固定)1</th>
										<th>貸方２UFコード(固定)2</th>
										<th>貸方２UFコード(固定)3</th>
										<th>貸方２UFコード(固定)4</th>
										<th>貸方２UFコード(固定)5</th>
										<th>貸方２UFコード(固定)6</th>
										<th>貸方２UFコード(固定)7</th>
										<th>貸方２UFコード(固定)8</th>
										<th>貸方２UFコード(固定)9</th>
										<th>貸方２UFコード(固定)10</th>
</c:if>
										<th>貸方課税区分２（仕訳パターン）</th>
										<th>貸方分離区分２（仕訳パターン）</th>
										<th>貸方仕入区分２（仕訳パターン）</th>
										<!-- 貸方3 -->
										<th>貸方負担部門コード３（仕訳パターン）</th>
										<th>貸方取引先コード３（仕訳パターン）</th>
										<th>貸方科目コード３（仕訳パターン）</th>
										<th>貸方科目枝番コード３（仕訳パターン）</th>
										<th>貸方プロジェクトコード３（仕訳パターン）</th>
										<th>貸方セグメントコード３（仕訳パターン）</th>
										<th>貸方３UFコード1</th>
										<th>貸方３UFコード2</th>
										<th>貸方３UFコード3</th>
<c:if test='${issias}'>
										<th>貸方３UFコード4</th>
										<th>貸方３UFコード5</th>
										<th>貸方３UFコード6</th>
										<th>貸方３UFコード7</th>
										<th>貸方３UFコード8</th>
										<th>貸方３UFコード9</th>
										<th>貸方３UFコード10</th>
										<th>貸方３UFコード(固定)1</th>
										<th>貸方３UFコード(固定)2</th>
										<th>貸方３UFコード(固定)3</th>
										<th>貸方３UFコード(固定)4</th>
										<th>貸方３UFコード(固定)5</th>
										<th>貸方３UFコード(固定)6</th>
										<th>貸方３UFコード(固定)7</th>
										<th>貸方３UFコード(固定)8</th>
										<th>貸方３UFコード(固定)9</th>
										<th>貸方３UFコード(固定)10</th>
</c:if>
										<th>貸方課税区分３（仕訳パターン）</th>
										<th>貸方分離区分３（仕訳パターン）</th>
										<th>貸方仕入区分３（仕訳パターン）</th>
										<!-- 貸方4 -->
										<th>貸方負担部門コード４（仕訳パターン）</th>
										<th>貸方取引先コード４（仕訳パターン）</th>
										<th>貸方科目コード４（仕訳パターン）</th>
										<th>貸方科目枝番コード４（仕訳パターン）</th>
										<th>貸方プロジェクトコード４（仕訳パターン）</th>
										<th>貸方セグメントコード４（仕訳パターン）</th>
										<th>貸方４UFコード1</th>
										<th>貸方４UFコード2</th>
										<th>貸方４UFコード3</th>
<c:if test='${issias}'>
										<th>貸方４UFコード4</th>
										<th>貸方４UFコード5</th>
										<th>貸方４UFコード6</th>
										<th>貸方４UFコード7</th>
										<th>貸方４UFコード8</th>
										<th>貸方４UFコード9</th>
										<th>貸方４UFコード10</th>
										<th>貸方４UFコード(固定)1</th>
										<th>貸方４UFコード(固定)2</th>
										<th>貸方４UFコード(固定)3</th>
										<th>貸方４UFコード(固定)4</th>
										<th>貸方４UFコード(固定)5</th>
										<th>貸方４UFコード(固定)6</th>
										<th>貸方４UFコード(固定)7</th>
										<th>貸方４UFコード(固定)8</th>
										<th>貸方４UFコード(固定)9</th>
										<th>貸方４UFコード(固定)10</th>
</c:if>
										<th>貸方課税区分４（仕訳パターン）</th>
										<th>貸方分離区分４（仕訳パターン）</th>
										<th>貸方仕入区分４（仕訳パターン）</th>
										<!-- 貸方5 -->
										<th>貸方負担部門コード５（仕訳パターン）</th>
										<th>貸方取引先コード５（仕訳パターン）</th>
										<th>貸方科目コード５（仕訳パターン）</th>
										<th>貸方科目枝番コード５（仕訳パターン）</th>
										<th>貸方プロジェクトコード５（仕訳パターン）</th>
										<th>貸方セグメントコード５（仕訳パターン）</th>
										<th>貸方５UFコード1</th>
										<th>貸方５UFコード2</th>
										<th>貸方５UFコード3</th>
<c:if test='${issias}'>
										<th>貸方５UFコード4</th>
										<th>貸方５UFコード5</th>
										<th>貸方５UFコード6</th>
										<th>貸方５UFコード7</th>
										<th>貸方５UFコード8</th>
										<th>貸方５UFコード9</th>
										<th>貸方５UFコード10</th>
										<th>貸方５UFコード(固定)1</th>
										<th>貸方５UFコード(固定)2</th>
										<th>貸方５UFコード(固定)3</th>
										<th>貸方５UFコード(固定)4</th>
										<th>貸方５UFコード(固定)5</th>
										<th>貸方５UFコード(固定)6</th>
										<th>貸方５UFコード(固定)7</th>
										<th>貸方５UFコード(固定)8</th>
										<th>貸方５UFコード(固定)9</th>
										<th>貸方５UFコード(固定)10</th>
</c:if>
										<th>貸方課税区分５（仕訳パターン）</th>
										<th>貸方分離区分５（仕訳パターン）</th>
										<th>貸方仕入区分５（仕訳パターン）</th>
									</tr>
								</thead>
								<tbody>
<c:forEach var="torihiki" items="${torihikiList}" varStatus="i">
									<tr>
										<td><nobr>${torihiki.number}</nobr></td>
	<c:if test='${status eq "End"}'>
		<c:if test='${fn:length(errorList) eq 0}'>
										<td><nobr>登録成功</nobr></td>
		</c:if>
		<c:if test='${fn:length(errorList) ne 0}'>
			<c:if test='${torihiki.errorFlg eq true}'>
										<td><nobr>登録失敗</nobr></td>
			</c:if>
			<c:if test='${torihiki.errorFlg eq false}'>
										<td><nobr>未登録</nobr></td>
			</c:if>
		</c:if>
				 
	</c:if>
										<td><nobr>${su:htmlEscape(torihiki.denpyouKbn)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.shiwakeEdano)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.deleteFlg)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.yuukouKigenFrom)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.yuukouKigenTo)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.bunrui1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.bunrui2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.bunrui3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.torihikiName)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.tekiyouFlg)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.tekiyou)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.defaultHyoujiFlg)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kousaihiHyoujiFlg)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kousaihiNinzuuRiyouFlg)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kousaihiKijungaku)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kousaihiCheckHouhou)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kousaihiCheckResult)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kakeFlg)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.hyoujiJun)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.shainCdRenkeiFlg)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.zaimuEdabanRenkeiFlg)}</nobr></td>
										<!-- 借方 -->
										<td><nobr>${su:htmlEscape(torihiki.kariFutanBumonCd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariKamokuCd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariKamokuEdabanCd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariTorihikisakiCd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariProjectCd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariSegmentCd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUf1Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUf2Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUf3Cd)}</nobr></td>
										<c:if test='${issias}'>
										<td><nobr>${su:htmlEscape(torihiki.kariUf4Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUf5Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUf6Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUf7Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUf8Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUf9Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUf10Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUfKotei1Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUfKotei2Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUfKotei3Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUfKotei4Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUfKotei5Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUfKotei6Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUfKotei7Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUfKotei8Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUfKotei9Cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariUfKotei10Cd)}</nobr></td>
										</c:if>
										<td><nobr>${su:htmlEscape(torihiki.kariKazeiKbn)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariZeiritsu)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariKeigenZeiritsuKbn)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariBunriKbn)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kariShiireKbn)}</nobr></td>
										<!-- 貸方1 -->
										<td><nobr>${su:htmlEscape(torihiki.kashiFutanBumonCd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiKamokuCd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiKamokuEdabanCd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiTorihikisakiCd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiProjectCd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiSegmentCd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf1Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf2Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf3Cd1)}</nobr></td>
										<c:if test='${issias}'>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf4Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf5Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf6Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf7Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf8Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf9Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf10Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei1Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei2Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei3Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei4Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei5Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei6Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei7Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei8Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei9Cd1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei10Cd1)}</nobr></td>
										</c:if>
										<td><nobr>${su:htmlEscape(torihiki.kashiKazeiKbn1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiBunriKbn1)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiShiireKbn1)}</nobr></td>
										<!-- 貸方2 -->
										<td><nobr>${su:htmlEscape(torihiki.kashiFutanBumonCd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiTorihikisakiCd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiKamokuCd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiKamokuEdabanCd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiProjectCd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiSegmentCd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf1Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf2Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf3Cd2)}</nobr></td>
										<c:if test='${issias}'>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf4Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf5Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf6Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf7Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf8Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf9Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf10Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei1Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei2Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei3Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei4Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei5Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei6Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei7Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei8Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei9Cd2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei10Cd2)}</nobr></td>
										</c:if>
										<td><nobr>${su:htmlEscape(torihiki.kashiKazeiKbn2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiBunriKbn2)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiShiireKbn2)}</nobr></td>
										<!-- 貸方3 -->
										<td><nobr>${su:htmlEscape(torihiki.kashiFutanBumonCd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiTorihikisakiCd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiKamokuCd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiKamokuEdabanCd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiProjectCd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiSegmentCd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf1Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf2Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf3Cd3)}</nobr></td>
										<c:if test='${issias}'>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf4Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf5Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf6Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf7Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf8Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf9Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf10Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei1Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei2Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei3Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei4Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei5Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei6Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei7Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei8Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei9Cd3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei10Cd3)}</nobr></td>
										</c:if>
										<td><nobr>${su:htmlEscape(torihiki.kashiKazeiKbn3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiBunriKbn3)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiShiireKbn3)}</nobr></td>
										<!-- 貸方4 -->
										<td><nobr>${su:htmlEscape(torihiki.kashiFutanBumonCd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiTorihikisakiCd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiKamokuCd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiKamokuEdabanCd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiProjectCd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiSegmentCd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf1Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf2Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf3Cd4)}</nobr></td>
										<c:if test='${issias}'>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf4Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf5Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf6Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf7Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf8Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf9Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf10Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei1Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei2Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei3Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei4Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei5Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei6Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei7Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei8Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei9Cd4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei10Cd4)}</nobr></td>
										</c:if>
										<td><nobr>${su:htmlEscape(torihiki.kashiKazeiKbn4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiBunriKbn4)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiShiireKbn4)}</nobr></td>
										<!-- 貸方5 -->
										<td><nobr>${su:htmlEscape(torihiki.kashiFutanBumonCd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiTorihikisakiCd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiKamokuCd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiKamokuEdabanCd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiProjectCd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiSegmentCd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf1Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf2Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf3Cd5)}</nobr></td>
										<c:if test='${issias}'>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf4Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf5Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf6Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf7Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf8Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf9Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUf10Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei1Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei2Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei3Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei4Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei5Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei6Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei7Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei8Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei9Cd5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiUfKotei10Cd5)}</nobr></td>
										</c:if>
										<td><nobr>${su:htmlEscape(torihiki.kashiKazeiKbn5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiBunriKbn5)}</nobr></td>
										<td><nobr>${su:htmlEscape(torihiki.kashiShiireKbn5)}</nobr></td>
									</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
					</section>

					<section>
					
<c:if test='${status eq "Init"}'>
	<c:if test='${fn:length(errorList) eq 0}'>
								<button type='button' class='btn' name='okButton' onClick="buttonAction('touroku')"><i class='icon-hdd'></i> 登録</button>
								<button type='button' class='btn' onClick="location.href='torihiki_ikkatsu_touroku'" ><i class='icon-arrow-left'></i> 戻る</button>
	</c:if>
	<c:if test='${fn:length(errorList) ne 0}'>
								<button type='button' class='btn' onClick="location.href='torihiki_ikkatsu_touroku'" ><i class='icon-arrow-left'></i> 戻る</button>
	</c:if>
</c:if>
<c:if test='${status eq "Run"}'>
							<button type='button' class='btn' onClick="location.href='ikkatsu_touroku_torihiki_csv_upload_kakunin?status=Run'"><i class='icon-search'></i> 画面更新</button>
</c:if>
<c:if test='${status eq "End"}'>
							<button type='button' class='btn' onClick="location.href='torihiki_ikkatsu_touroku'" ><i class='icon-arrow-left'></i> 戻る</button>
</c:if>
					</section>
				</form></div><!-- main -->
				<!-- Modal -->
				<div id='dialog'></div>
			</div><!-- content -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<!-- スクリプト -->
		<script style='text/javascript'>
/**
 * イベントボタン押下時のアクションの切り替え
 */
function buttonAction(btnName) {
	// アップロード
	if(btnName == "touroku"){
		formObject = $("form#tourokuForm");
		formObject.attr("action","ikkatsu_touroku_torihiki_csv_upload_kakunin_touroku");
		formObject.submit();
	}
}
		</script>
	</body>
</html>
