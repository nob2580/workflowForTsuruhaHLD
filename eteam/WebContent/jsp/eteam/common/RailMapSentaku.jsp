<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- メイン -->
<div id='dialogMain'>
	<!-- タイトル -->
	<h1>路線図選択</h1>

	<div class='form-horizontal'>
	<!-- エラー -->
	<%@ include file="/jsp/eteam/include/InputError.jsp" %>

		<div class='control-group'>
			<label class='control-label' style='width: 154px'>地区</label>
			<div id="todouhukenList" class='controls'>
				<select name='todouhukenList' class='input-large' onchange="changeTodouhuken()">

<c:forEach var="record" items="${todouhuken}">
				<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq todouhukenCd}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
</c:forEach>
				</select>
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label' style='width: 154px'>路線種別</label>
			<div class='controls'>
				<select name='rosenList' class='input-large'>
					<option></option>
<c:forEach var="record" items="${list}">
					<option value='${su:htmlEscape(record.railMapId)}' <c:if test='${record.railMapId eq id}'>selected</c:if>>${su:htmlEscape(record.Name)}</option>

</c:forEach>
				</select>
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label' style='width: 154px'>基点駅名</label>
			<div class='controls'>
				<input type='text' name='stationName' value='${su:htmlEscape(stationName)}'>
				<button type='button' name='railMapSearch' class='btn btn-small input-inline' onclick="changeStation()"><i class='icon-search'></i> 路線表示</button>
			</div>
		</div>
	</div>

	<section>
	<!-- 駅すぱあと駅名入力部品 -->
	<%@ include file="RailMapSub1.jsp" %>


	<!-- 駅すぱあと路線図マップ部品 -->
	<%@ include file="RailMapSub2.jsp" %>
	</section>

</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
<!--

/**
 * 地区変更時Function
 */
function changeTodouhuken() {
	var element = $("select[name=todouhukenList] option:selected").val();
	
	$("#dialogMain").load("railmap_todouhuken?todouhukenCd=" + encodeURI(element) +
			"&stationName=" + encodeURI($('input[name=stationName]').val()) +
			"&selStName1=" + encodeURI($("input[name=selStName1]").val()) + "&selStName2=" + encodeURI($("input[name=selStName2]").val()) +
			"&selStName3=" + encodeURI($("input[name=selStName3]").val()) + "&selStName4=" + encodeURI($("input[name=selStName4]").val()) +
			"&selStName5=" + encodeURI($("input[name=selStName5]").val()));
}

/**
 * 路線種別変更時Function
 */
function changeStation() {
	
	var element = $("select[name=rosenList] option:selected").val();
	var element2 = $("select[name=todouhukenList] option:selected").val();
	
	$("#railMapSub2").load("railmap_hyouji?id=" + encodeURI(element) + "&todouhukenCd=" + encodeURI(element2) +
			"&stationName=" + encodeURI($('input[name=stationName]').val()) +
			"&selStName1=" + encodeURI($("input[name=selStName1]").val()) + "&selStName2=" + encodeURI($("input[name=selStName2]").val()) +
			"&selStName3=" + encodeURI($("input[name=selStName3]").val()) + "&selStName4=" + encodeURI($("input[name=selStName4]").val()) +
			"&selStName5=" + encodeURI($("input[name=selStName5]").val()));
}
/**
 * 路線図移動ボタン押下時Function
 */
function moveRailMap(value) {
	
	var element = $("select[name=rosenList] option:selected").val();
	var element2 = $("select[name=todouhukenList] option:selected").val();
	var x = parseInt($("input[name=x]").val());
	var y = parseInt($("input[name=y]").val());
	switch (value) {
	case 'up':
		y = y - 100;
		break;
	case 'left':
		x = x - 100;
		break;
	case 'right':
		x = x + 100;
		break;
	case 'down':	
		y = y + 100;
		break;
	default:
	}
	
	if (x < 1) {
		x = 1;
	}

	if (y < 1) {
		y = 1;
	}

	$("#railMapSub2").load("railmap_move?id=" + encodeURI(element) + "&todouhukenCd=" + encodeURI(element2) +
			"&stationName=" + encodeURI($('input[name=stationName]').val()) + "&tempX=" + encodeURI($('input[name=x]').val()) + "&tempY=" + encodeURI($('input[name=y]').val()) +
			"&x=" + encodeURI(x) + "&y=" + encodeURI(y) +
			"&selStName1=" + encodeURI($("input[name=selStName1]").val()) + "&selStName2=" + encodeURI($("input[name=selStName2]").val()) +
			"&selStName3=" + encodeURI($("input[name=selStName3]").val()) + "&selStName4=" + encodeURI($("input[name=selStName4]").val()) +
			"&selStName5=" + encodeURI($("input[name=selStName5]").val()));

}

/**
 * 路線図駅名クリック時Function
 */
function getXY(e) {
	
	var element = $("select[name=rosenList] option:selected").val();
	var element2 = $("select[name=todouhukenList] option:selected").val();
	var x = Math.round(e.pageX - $("img[name=image]").offset()["left"] + parseInt($("input[name=x]").val()));
	var y = Math.round(e.pageY - $("img[name=image]").offset()["top"] + parseInt($("input[name=y]").val()));
		
	$("#railMapSub1").load("railmap_click?id=" + encodeURI(element) + "&todouhukenCd=" + encodeURI(element2) +
			"&stationName=" + encodeURI($('input[name=stationName]').val()) + "&clickX=" + x + "&clickY=" + y +
			"&x=" + encodeURI($("input[name=x]").val()) + "&y=" + encodeURI($("input[name=y]").val()) +
			"&selStName1=" + encodeURI($("input[name=selStName1]").val()) + "&selStName2=" + encodeURI($("input[name=selStName2]").val()) +
			"&selStName3=" + encodeURI($("input[name=selStName3]").val()) + "&selStName4=" + encodeURI($("input[name=selStName4]").val()) +
			"&selStName5=" + encodeURI($("input[name=selStName5]").val()));

}

/**
 * 選択ボタン押下時Funciton
 * 呼びもとに目的地・到着地を返す
 */
function railMapSentakuClick() {
	
	dialogRetRailMapFrom.val($("input[name=selStName1]").val());
	dialogRetRailMapTo01.val($("input[name=selStName2]").val());
	dialogRetRailMapTo02.val($("input[name=selStName3]").val());
	dialogRetRailMapTo03.val($("input[name=selStName4]").val());
	dialogRetRailMapTo04.val($("input[name=selStName5]").val());

	$("#dialog").children().remove();
	$("#dialog").dialog("close");
}
//-->
</script>
