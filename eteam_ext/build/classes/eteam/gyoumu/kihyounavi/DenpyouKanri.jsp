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
		<title>伝票管理｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
  				<h1>伝票管理</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' action='denpyou_kanri_koushin'>
					<input type='hidden' name='yosanShikkouOption' value='${su:htmlEscape(yosanShikkouOption)}'>

					<!-- 一覧 -->
					<section>
<c:choose>
	<c:when test="${yosanShikkouOption == 'A'}"><c:set var="ichiranWidth" value="1750px"></c:set></c:when>
	<c:otherwise><c:set var="ichiranWidth" value="1540px"></c:set></c:otherwise>
</c:choose>
						<div class='no-more-tables' style="width:${ichiranWidth};">
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>#</th>
										<th><span class='required'>*</span>業務種別</th>
										<th>伝票種別<br>表示順変更</th>
										<th><span class='required'>*</span>内容</th>
										<th><span class='required'>*</span>有効期限</th>
										<th noWrap>添付伝票<br>選択対象<br>する<br>/しない</th>
										<th noWrap>添付伝票<br>入力欄<br>表示<br>/非表示</th>
										<th noWrap>申請時<br>帳票出力</th>
										<th noWrap>承認<br>状況欄<br>印刷する<br>/しない</th>
										<th <c:if test='${not routeHanteiDisp}'>style='display:none'</c:if>>ルート判定金額</th>
										<th noWrap>取引毎に<br>ルート<br>設定する<br>/しない</th>
										<th noWrap>申請者の<br>処理権限名</th>
<c:if test="${yosanShikkouOption == 'A'}">
										<th noWrap>起案番号<br>運用する<br>/しない</th>
										<th>予算執行対象</th>
</c:if>
                                        <th noWrap>取引先を<br>仕入先に<br>限定する<br>/しない</th>
									</tr>
								</thead>
								<tbody id='denpyoList'>
<c:if test='${0 < fn:length(denpyouKbn)}'><c:forEach var="i" begin="0" end="${fn:length(denpyouKbn) - 1}" step="1">
									<tr class='${su:htmlEscape(bgColor[i])}'>
										<td class='number' style='text-align:center;'></td>
										<td><input type='text' name='gyoumuShubetsu' class='input-medium' maxlength="20" value='${su:htmlEscape(gyoumuShubetsu[i])}' ></td>
										
										<td>
	<c:choose>
		<c:when test="${denpyouKbn[i] == 'A002' || denpyouKbn[i] == 'A005' || denpyouKbn[i] == 'A012'}">
												<nobr><input type='text' name='denpyouShubetsu' class='input-medium' maxlength="20" value='${su:htmlEscape(denpyouShubetsu[i])}' >(申請・仮払あり)</nobr>
												<br>
												<nobr><input type='text' name='denpyouKaribaraiNashiShubetsu' class='input-medium' maxlength="20" value='${su:htmlEscape(denpyouKaribaraiNashiShubetsu[i])}' >(申請・仮払なし)</nobr>
												<br>
												<nobr><input type='text' name='denpyouPrintShubetsu' class='input-medium' maxlength="20" value='${su:htmlEscape(denpyouPrintShubetsu[i])}' >(帳票・仮払あり)</nobr>
												<br>
												<nobr><input type='text' name='denpyouPrintKaribaraiNashiShubetsu' class='input-medium' maxlength="20" value='${su:htmlEscape(denpyouPrintKaribaraiNashiShubetsu[i])}' >(帳票・仮払なし)</nobr>
		</c:when>
		<c:when test="${denpyouKbn[i] >= 'B001' and denpyouKbn[i] <= 'B999'}">
												${su:htmlEscape(denpyouShubetsu[i])}
												<input type='hidden' name='denpyouShubetsu' value='${su:htmlEscape(denpyouShubetsu[i])}'>
												<input type='hidden' name='denpyouKaribaraiNashiShubetsu' value='${su:htmlEscape(denpyouKaribaraiNashiShubetsu[i])}'>
												<input type='hidden' name='denpyouPrintShubetsu' value='${su:htmlEscape(denpyouPrintShubetsu[i])}'>
												<input type='hidden' name='denpyouPrintKaribaraiNashiShubetsu' value='${su:htmlEscape(denpyouPrintKaribaraiNashiShubetsu[i])}'>
		</c:when>
		<c:when test="${denpyouKbn[i] >= 'A001' and denpyouKbn[i] <= 'A999'}">
												<nobr><input type='text'   name='denpyouShubetsu' class='input-medium' maxlength="20" value='${su:htmlEscape(denpyouShubetsu[i])}' >(申請)</nobr>
												<input type='hidden' name='denpyouKaribaraiNashiShubetsu' value='${su:htmlEscape(denpyouKaribaraiNashiShubetsu[i])}'>
												<br>
												<nobr><input type='text'   name='denpyouPrintShubetsu' class='input-medium' maxlength="20" value='${su:htmlEscape(denpyouPrintShubetsu[i])}' >(帳票)</nobr>
												<input type='hidden' name='denpyouPrintKaribaraiNashiShubetsu' value='${su:htmlEscape(denpyouPrintKaribaraiNashiShubetsu[i])}'>
		</c:when>
		<c:otherwise>
												<nobr><input type='text'   name='denpyouShubetsu' class='input-medium' maxlength="20" value='${su:htmlEscape(denpyouShubetsu[i])}' >(申請)</nobr>
												<input type='hidden' name='denpyouKaribaraiNashiShubetsu' value='${su:htmlEscape(denpyouKaribaraiNashiShubetsu[i])}'>
												<input type='hidden' name='denpyouPrintShubetsu' value='${su:htmlEscape(denpyouPrintShubetsu[i])}'>
												<input type='hidden' name='denpyouPrintKaribaraiNashiShubetsu' value='${su:htmlEscape(denpyouPrintKaribaraiNashiShubetsu[i])}'>
		</c:otherwise>
	</c:choose>
											<br>
											<button type='button' name='up' class='btn btn-mini'>↑</button>
											<button type='button' name='down' class='btn btn-mini'>↓</button>
										</td>
										
										<td><textarea name="naiyou" maxlength="160">${su:htmlEscape(naiyou[i])}</textarea></td>
										<td>
											<input type='text' name='yuukouKigenFrom' class='input-small datepicker' value='${su:htmlEscape(yuukouKigenFrom[i])}' >～<input type='text' name='yuukouKigenTo' class='input-small datepicker' value='${su:htmlEscape(yuukouKigenTo[i])}'>
											<input type='hidden' name='denpyouKbn' value='${su:htmlEscape(denpyouKbn[i])}'>
											<input type='hidden' name='bgColor' value='${su:htmlEscape(bgColor[i])}'>
										</td>
										<td align="center">
											<input type="checkbox" name="kanrenSentaku" <c:if test='${"1" eq su:htmlEscape(kanrenSentakuFlg[i])}'>checked </c:if> />
											<input type="hidden" name="kanrenSentakuFlg"/>
										</td>
										<td align="center">
											<input type="checkbox" name="kanrenHyouji" <c:if test='${"1" eq su:htmlEscape(kanrenHyoujiFlg[i])}'>checked </c:if> />
											<input type="hidden" name="kanrenHyoujiFlg"/>
										</td>
										<td align="center">
											<input type="checkbox" name="denpyouPrint" <c:if test='${"1" eq su:htmlEscape(denpyouPrintFlg[i])}'>checked </c:if> />
											<input type="hidden" name="denpyouPrintFlg"/>
										</td>
										<td align="center">
											<input type="checkbox" name="shouninJyoukyouPrint" <c:if test='${"1" eq su:htmlEscape(shouninJyoukyouPrintFlg[i])}'>checked</c:if> />
											<input type="hidden" name="shouninJyoukyouPrintFlg"/>
										</td>
										<td align="center" <c:if test='${not routeHanteiDisp}'>style='display:none'</c:if>>
											<select name='routeHanteiKingaku' class='input-medium' <c:if test='${"0" eq su:htmlEscape(routeHanteiKingakuSeigyo[i])}'>style='visibility:hidden'</c:if>>
												<c:forEach var="routeHanteiKingakuRecord" items="${routeHanteiKingakuList}">
													<option value='${routeHanteiKingakuRecord.naibu_cd}' <c:if test='${routeHanteiKingakuRecord.naibu_cd eq routeHanteiKingaku[i]}'>selected</c:if>>${su:htmlEscape(routeHanteiKingakuRecord.name)}</option>
												</c:forEach>
											</select>
										</td>
										<td align="center">
											<input type="checkbox" name="routeTorihikiPrint" <c:if test='${"1" eq su:htmlEscape(routeTorihikiFlg[i])}'>checked</c:if> <c:if test='${denpyouKbn[i] eq "A007" || denpyouKbn[i] eq "A008" || denpyouKbn[i] >= "B001"}'>disabled</c:if> />
											<input type="hidden" name="routeTorihikiFlg"/>
										</td>
										<td align="center">
											<input type="text" name="shorikengen"  class='input-small' maxlength=6 value='${su:htmlEscape(shorikengen[i])}'>
										</td>
<c:if test="${yosanShikkouOption == 'A'}">
										<td align="center">
											<input type="checkbox" name="kianBangouUnyou" <c:if test='${"1" eq su:htmlEscape(kianBangouUnyouFlg[i])}'>checked</c:if> <c:if test='${"0" eq su:htmlEscape(kianBangouUnyouSeigyo[i])}'>disabled</c:if> />
											<input type="hidden" name="kianBangouUnyouFlg"/>
										</td>
										<td align="center">
											<select name='yosanShikkouTaishou' class='input-small' <c:if test='${"0" eq su:htmlEscape(yosanShikkouTaishouSeigyo[i])}'>disabled</c:if>>
<c:choose>
<c:when test="${denpyouKbn[i] == 'A002' || denpyouKbn[i] == 'A005' || denpyouKbn[i] == 'A012'}">
												<c:forEach var="yosanShikkouTaishouRecord" items="${yosanShikkouTaishouKaribaraiList}">
													<option value='${yosanShikkouTaishouRecord.naibu_cd}' <c:if test='${yosanShikkouTaishouRecord.naibu_cd eq yosanShikkouTaishou[i]}'>selected</c:if>>${su:htmlEscape(yosanShikkouTaishouRecord.name)}</option>
												</c:forEach>
</c:when>
<c:when test="${denpyouKbn[i] == 'A001' || denpyouKbn[i] == 'A003' || denpyouKbn[i] == 'A004' || denpyouKbn[i] == 'A011' || denpyouKbn[i] == 'A013'}">
												<c:forEach var="yosanShikkouTaishouRecord" items="${yosanShikkouTaishouSeisanList}">
													<option value='${yosanShikkouTaishouRecord.naibu_cd}' <c:if test='${yosanShikkouTaishouRecord.naibu_cd eq yosanShikkouTaishou[i]}'>selected</c:if>>${su:htmlEscape(yosanShikkouTaishouRecord.name)}</option>
												</c:forEach>
</c:when>
<c:otherwise>
												<c:forEach var="yosanShikkouTaishouRecord" items="${yosanShikkouTaishouKanitodokeList}">
													<option value='${yosanShikkouTaishouRecord.naibu_cd}' <c:if test='${yosanShikkouTaishouRecord.naibu_cd eq yosanShikkouTaishou[i]}'>selected</c:if>>${su:htmlEscape(yosanShikkouTaishouRecord.name)}</option>
												</c:forEach>
</c:otherwise>
</c:choose>
											</select>
										</td>
</c:if>
                                        <td align="center">
                                            <input type="checkbox" name="shiiresakiCheck" <c:if test='${shiiresakiFlg[i] eq "1"}'>checked</c:if>>
                                            <input type="hidden" name="shiiresakiFlg"/>
                                        </td>
									</tr>
</c:forEach></c:if>
								</tbody>
							</table>
						</div>
					</section>

					<!-- 処理ボタン -->
					<section>
						<button type='button' id='koushinButton' class='btn'><i class='icon-hdd'></i> 更新</button>
					</section>

				</form></div><!-- main -->
			</div> <!-- /container -->
			<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<!-- スクリプト -->
		<script style='text/javascript'>


/**
 * ↑ボタン押下時
 */
function up() {
	var tr = $(this).closest("tr");
	if(tr.prev()){
		tr.insertBefore(tr.prev());
	}
	numbering();
}

/**
 * ↓ボタン押下時
 */
function down() {
	var tr = $(this).closest("tr");
	if(tr.next()){
		tr.insertAfter(tr.next());
	}
	numbering();
}

/**
 * 明細の番号#1,#2,の番号部分を全て振りなおす
 */
function numbering() {
	$("#denpyoList tr").each(function(){
		$(this).find(".number").text(Number($("#denpyoList tr").index($(this))) + 1);
		$(this).find(".gyouNo").attr("value",Number($("#denpyoList tr").index($(this))) + 1);
	});
}

/**
 * 予算執行対象によって「起案番号運用する/しない」の状態を制御する。
 */
 function kianBangouUnyouSeigyo(denpyouTr){
	// 予算執行対象ではない伝票種別(プルダウンがdisabled)の場合、処理を行わない
	var yosanShikkouTaishou = denpyouTr.find("select[name=yosanShikkouTaishou]");
	if(yosanShikkouTaishou.prop("disabled")) return;
	
	var yosanShikkouTaishouVal = $("option:selected", yosanShikkouTaishou).val();
	var kianBangouUnyou = denpyouTr.find("input[name=kianBangouUnyou]");
	//予算執行対象が「実施起案」「支出起案」の場合は、チェックON固定で変更不可とする。
	if(yosanShikkouTaishouVal == "A" || yosanShikkouTaishouVal == "B"){
		kianBangouUnyou.prop("disabled", true);
		kianBangouUnyou.prop("checked", true);
	}else{
		kianBangouUnyou.prop("disabled", false);
	}
}


$(document).ready(function() {

	//ユーザー↑ボタン押下時アクション
	$("button[name=up]").click(up);

	//ユーザー↓ボタン押下時アクション
	$("button[name=down]").click(down);

<c:if test="${yosanShikkouOption == 'A'}">
	//予算執行対象切替時
	$("select[name=yosanShikkouTaishou]").change(function(){
		kianBangouUnyouSeigyo($(this).parents("tr"));
	});
	//「起案番号運用する/しない」の状態制御
	$('#denpyoList tr').each(function(i, tr){
		kianBangouUnyouSeigyo($(tr));
	});
</c:if>

	//更新ボタン押下
	$("#koushinButton").click(function(){
		// チェックボックスがチェックされない時でもvalueを当てはめる
		$('#denpyoList tr').each(function(i, tr){
			if ($(tr).find('[name=kanrenSentaku]').is(':checked')){
				$(tr).find("[name=kanrenSentakuFlg]").val("1");
			} else {
				$(tr).find("[name=kanrenSentakuFlg]").val("0");
			}
			if ($(tr).find('[name=kanrenHyouji]').is(':checked')){
				$(tr).find("[name=kanrenHyoujiFlg]").val("1");
			} else {
				$(tr).find("[name=kanrenHyoujiFlg]").val("0");
			}
			if ($(tr).find('[name=denpyouPrint]').is(':checked')){
				$(tr).find("[name=denpyouPrintFlg]").val("1");
			} else {
				$(tr).find("[name=denpyouPrintFlg]").val("0");
			}
			if ("A" === $("input[name=yosanShikkouOption]").val()){
				// 起案番号運用
				if ($(tr).find('[name=kianBangouUnyou]').is(':checked')){
					$(tr).find("[name=kianBangouUnyouFlg]").val("1");
				} else {
					$(tr).find("[name=kianBangouUnyouFlg]").val("0");
				}
			}
			// 承認状況欄印刷
			if ($(tr).find('[name=shouninJyoukyouPrint]').is(':checked')){
				$(tr).find("[name=shouninJyoukyouPrintFlg]").val("1");
			} else {
				$(tr).find("[name=shouninJyoukyouPrintFlg]").val("0");
			}
			//取引毎ルート設定
			if ($(tr).find('[name=routeTorihikiPrint]').is(':checked')){
				$(tr).find("[name=routeTorihikiFlg]").val("1");
			} else {
				$(tr).find("[name=routeTorihikiFlg]").val("0");
			}
            //取引先を仕入先に限定する/しない
            if ($(tr).find('[name=shiiresakiCheck]').is(':checked')){
                $(tr).find("[name=shiiresakiFlg]").val("1");
            } else {
                $(tr).find("[name=shiiresakiFlg]").val("0");
            }

		});
		
		$("#myForm").attr("action", "denpyou_kanri_koushin");
		$("#myForm").submit();
	});

	//初期ナンバリング
	numbering();
});
		</script>
	</body>
</html>
