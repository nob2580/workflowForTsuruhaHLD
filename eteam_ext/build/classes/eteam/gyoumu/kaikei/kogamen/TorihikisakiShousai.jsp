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

	<section>
		<input type="hidden" id='kensakuCd'   value="${su:htmlEscape(kensakuCd)}">
		<input type="hidden" id='kensakuName' value="${su:htmlEscape(kensakuName)}">
	
		<button type='button' id='modoruButton' class='btn'><i class='icon-step-backward'></i> 戻る</button>
		
		<h2>振込先情報</h2>
		<div class='form-horizontal'>
			<div class='control-group'>
				<label class='control-label' for='ginkou'>銀行名</label>
				<div class='controls'>
					<input type='text' id='ginkou' name='ginkou' value="${su:htmlEscape(ginkou)}" readonly>
				</div>
			</div>
			<div class='control-group'>
				<label class='control-label' for='shiten'>支店名</label>
				<div class='controls'>
					<input type='text' id='shiten' name='shiten' value="${su:htmlEscape(shiten)}" readonly>
				</div>
			</div>
			<div class='control-group'>
				<label class='control-label' for='kouza'>口座番号</label>
				<div class='controls'>
					<input type='text' id='kouza' name='kouza' value="${su:htmlEscape(kouza)}" readonly>
				</div>
			</div>
			<div class='control-group'>
				<label class='control-label' for='meigi'>口座名義人</label>
				<div class='controls'>
					<input type='text' id='meigi' name='meigi' value="${su:htmlEscape(meigi)}" readonly>
				</div>
			</div>
			<div class='control-group'>
				<label class='control-label' for='yokinShubetsu'>預金種別</label>
				<div class='controls'>
					<input type='text' id='yokinShubetsu' name='yokinShubetsu' value="${su:htmlEscape(yokinShubetsu)}" readonly>
				</div>
			</div>
			<div class='control-group'>
				<label class='control-label' for='shiharaiShubetsu'>支払種別</label>
				<div class='controls'>
					<input type='text' id='shiharaiShubetsu' name='shiharaiShubetsu' value="${su:htmlEscape(shiharaiShubetsu)}" readonly>
				</div>
			</div>
			<div class='control-group'>
				<label class='control-label' for='shiharaibi'>支払日</label>
				<div class='controls'>
					<input type='text' id='shiharaibi' name='shiharaibi' value="${shiharaibi}" readonly>
				</div>
			</div>
			<div class='control-group'>
				<label class='control-label' for='tesuuryouFutanKubun'>手数料負担区分</label>
				<div class='controls'>
					<input type='text' id='tesuuryouFutanKubun' name='tesuuryouFutanKubun' value="${su:htmlEscape(tesuuryouFutanKubun)}" readonly>
				</div>
			</div>
		</div>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
<!--
/**
 * 検索ボタン押下時
 */
$("#modoruButton").click(function(){
	var kensakuCode = document.getElementById("kensakuCd").value;
	var kensakuName = document.getElementById("kensakuName").value;
	$("#dialog").empty();
	$("#dialog").load("torihikisaki_sentaku_kensaku?torihikisakiCd=" + kensakuCode + "&torihikisakiName=" + kensakuName);
});

-->
</script>
