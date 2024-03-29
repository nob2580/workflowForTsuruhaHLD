<%@page import="eteam.base.EteamFormatter"%>
<%@page import="eteam.common.KaishaInfo"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- エラー -->
<input type='hidden' id="errorCnt" value='${fn:length(errorList)}'>
<%@ include file="/jsp/eteam/include/InputError.jsp" %>

<!-- メイン -->
<div id='dialogMain'>

	<!-- 入力フィールド -->
	<section id = 'kianTsuika'>

		<h2>検索条件</h2>
		<div><form class="form-inline">
			<input type='hidden' name='yosanShikkouTaishou' value='${su:htmlEscape(yosanShikkouTaishou)}'>
			<input type='hidden' name='kensakuBumonCd' value='${su:htmlEscape(kensakuBumonCd)}'>
			<div class='control-group'>
				<label class="label label-sub" for=kensakuKianbangouBo>起案番号簿</label>
				<select name='kensakuKianbangouBo' id='kensakuKianbangouBo' class='input-xxlarge input-inline'>
						<option></option>
					<c:forEach var='record' items='${kianbangouBoList}'>
						<option value='${su:htmlEscape(record.key)}' <c:if test='${record.key eq kensakuKianbangouBo}'>selected</c:if>>${su:htmlEscape(record.dispNm)}</option>
					</c:forEach>
				</select>
				<label class="label label-sub" for=kensakuKianbangou>起案番号</label>
				<input type='text' name='kensakuKianbangou' id='kensakuKianbangou' class='input-mini input-inline' maxlength='6' value='${su:htmlEscape(kensakuKianbangou)}'>
			</div>
			<div class='control-group'>
				<label class="label label-sub" for=kensakuKenmei>件名</label>
				<input type='text' name='kensakuKenmei' id='kensakuKenmei' class='input-xlarge input-inline' value='${su:htmlEscape(kensakuKenmei)}'>
				<label class="label label-sub" for=kensakuDenpyouShubetsu>伝票種別</label>
				<select name='kensakuDenpyouShubetsu' id='kensakuDenpyouShubetsu' class='input-xlarge input-inline'>
						<option></option>
					<c:forEach var='record' items='${kensakuDenpyouShubetsuList}'>
					<!-- TODO コード値と表示名を書き換える -->
						<option value='${su:htmlEscape(record.denpyou_kbn)}' <c:if test='${record.denpyou_kbn eq kensakuDenpyouShubetsu}'>selected</c:if>>${su:htmlEscape(record.denpyou_shubetsu)}</option>
					</c:forEach>
				</select>
			</div>
			<div class='control-group'>
				<label class="label label-sub" for=kensakuKianbi>起案日</label>
				<input type='text' name='kensakuKianBiFrom' id='kensakuKianBi' class='input-small datepicker input-inline' value='${su:htmlEscape(kensakuKianBiFrom)}'>
				～
				<input type='text' name='kensakuKianBiTo' id='kensakuKianBi' class='input-small datepicker input-inline' value='${su:htmlEscape(kensakuKianBiTo)}'>
				<label class="label label-sub" for=kensakuSaishuuShouninBi>最終承認日</label>
				<input type='text' name='kensakuSaishuuShouninBiFrom' id='kensakuSaishuuShouninBi' class='input-small datepicker input-inline' value='${su:htmlEscape(kensakuSaishuuShouninBiFrom)}'>
				～
				<input type='text' name='kensakuSaishuuShouninBiTo' id='kensakuSaishuuShouninBi' class='input-small datepicker input-inline' value='${su:htmlEscape(kensakuSaishuuShouninBiTo)}'>
				<button type='button' id='kianTsuikaKensakuButton' class='btn'>検索</button>
			</div>
		</form></div>
	</section>
	<section>
		<h2>検索結果</h2>
		<div><form id='kanren' method='post' target="_self">
		<table class='table-bordered table-condensed'>
			<thead>
				<tr>
					<th><nobr>選択</nobr></th>
					<th><nobr>起案番号</nobr></th>
					<th><nobr>件名</nobr></th>
					<th><nobr>伝票種別</nobr></th>
					<th><nobr>起票日</nobr></th>
					<th><nobr>最終承認日</nobr></th>
				</tr>
			</thead>
			<tbody>
<c:forEach var="record" items="${list}">
				<tr>
					<td align="center"><input type='checkbox' name='sentaku' value='${su:htmlEscape(record.denpyou_id)}'></td>
					<td style="white-space:nowrap;">${su:htmlEscape(record.kianbangou)}</td>
					<td style="white-space:nowrap;">${su:htmlEscape(record.kenmei)}</td>
					<td style="white-space:nowrap;">${su:htmlEscape(record.denpyou_shubetsu)}</td>
					<td style="white-space:nowrap;">${su:htmlEscape(record.kihyouBi)}</td>
					<td style="white-space:nowrap;">${su:htmlEscape(record.saishuuShouninBi)}</td>
					<!-- 以下の非表示項目は伝票共通画面へ引き渡すための項目 -->
					<input type='hidden' name="hyoujiKianEmbedSpace" value='${su:htmlEscape(record.embed_space)}'>
					<input type='hidden' name="hyoujiKianDenpyouKbn" value='${su:htmlEscape(record.kbn)}'>
					<input type='hidden' name="hyoujiKianbangou" value='${su:htmlEscape(record.trKianbangou)}'>
					<input type='hidden' name="hyoujiKianKenmei" value='${su:htmlEscape(record.trKenmei)}'>
					<input type='hidden' name="hyoujiKianDenpyouShubetsuUrl" value='${su:htmlEscape(record.denpyou_shubetsu_url)}'>
					<input type='hidden' name="hyoujiKianDenpyouUrl" value='${su:htmlEscape(record.denpyou_url)}'>
					<input type='hidden' name="hyoujiKianDenpyouShubetsu" value='${su:htmlEscape(record.shubetsu)}'>
					<input type='hidden' name="hyoujiKianDenpyouId" value='${su:htmlEscape(record.id)}'>
					<input type='hidden' name="hyoujiKianTenpuFileName" value="${su:htmlEscape(record.file_name)}">
					<input type='hidden' name="hyoujiKianTenpuFileUrl" value="${su:htmlEscape(record.tenpu_url)}">
					<input type='hidden' name="kianDenpyouId" value='${su:htmlEscape(record.denpyou_id)}'>
					<input type='hidden' name="kianDenpyouKbn" value='${su:htmlEscape(record.denpyou_kbn)}'>
				</tr>
</c:forEach>
			</tbody>
		</table>
		</form></div>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>

// 初期表示処理
$("#dialog").ready(function(){
	commonInit($("#kianTsuika"));

	// チェックボック選択（選択を単一にする）
	$("input[name=sentaku]").click(function(){
		if ($(this).prop('checked')){
			// 一旦全てをクリアして再チェックする
			$("input[name=sentaku]").prop('checked', false);
			$(this).prop('checked', true);
		}
	});

	// 検索ボタン押下
	$("#kianTsuikaKensakuButton").click(function(){
		// 入力された各検索条件を取得する。
		var objSection = $("#kianTsuika");
		var yosanShikkouTaishou = "yosanShikkouTaishou=" + encodeURI(objSection.find("[name=yosanShikkouTaishou]").val());
		var kensakuKianbangouBo = "kensakuKianbangouBo=" + encodeURI(objSection.find("#kensakuKianbangouBo").find("option:selected").val());
		var kensakuKianbangou = "kensakuKianbangou=" + encodeURI(objSection.find("[name=kensakuKianbangou]").val());
		var kensakuKenmei = "kensakuKenmei=" + encodeURI(objSection.find("[name=kensakuKenmei]").val());
		var kensakuDenpyouShubetsu = "kensakuDenpyouShubetsu=" + encodeURI(objSection.find("#kensakuDenpyouShubetsu").find("option:selected").val());
		var kensakuKianBiFrom = "kensakuKianBiFrom=" + encodeURI(objSection.find("[name=kensakuKianBiFrom]").val());
		var kensakuKianBiTo = "kensakuKianBiTo=" + encodeURI(objSection.find("[name=kensakuKianBiTo]").val());
		var kensakuSaishuuShouninBiFrom = "kensakuSaishuuShouninBiFrom=" + encodeURI(objSection.find("[name=kensakuSaishuuShouninBiFrom]").val());
		var kensakuSaishuuShouninBiTo = "kensakuSaishuuShouninBiTo=" + encodeURI(objSection.find("[name=kensakuSaishuuShouninBiTo]").val());
		var kensakuBumonCd = "kensakuBumonCd=" + encodeURI(objSection.find("[name=kensakuBumonCd]").val());
		// 検索処理に検索条件も引き渡す。
		$("#dialog")
		.load("kian_tsuika_kensaku?" + yosanShikkouTaishou + "&" + kensakuKianbangouBo + "&" + kensakuKianbangou + "&" + kensakuKenmei + "&" + kensakuDenpyouShubetsu + "&" + kensakuKianBiFrom + "&" + kensakuKianBiTo + "&" + kensakuSaishuuShouninBiFrom + "&" + kensakuSaishuuShouninBiTo + "&" + kensakuBumonCd);
	});
});

</script>

