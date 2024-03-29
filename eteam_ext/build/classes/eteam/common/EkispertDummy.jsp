<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!-- 駅すぱあとイントラ版からの値を受け取るダミーＪＳＰ -->
<!DOCTYPE html>
<html lang='ja'>
	<head>
		<meta charset='utf-8'>
	</head>
	<body>
<!-- エラー -->
<%@ include file="/jsp/eteam/include/InputError.jsp" %>
		<input type='hidden' name='from' value='${su:htmlEscape(from)}'><br>
		<input type='hidden' name='to01' value='${su:htmlEscape(to01)}'><br>
		<input type='hidden' name='to02' value='${su:htmlEscape(to02)}'><br>
		<input type='hidden' name='to03' value='${su:htmlEscape(to03)}'><br>
		<input type='hidden' name='to04' value='${su:htmlEscape(to04)}'><br>
		<input type='hidden' name='route' value='${su:htmlEscape(route)}'><br>
		<input type='hidden' name='money' value='${su:htmlEscape(money)}'><br>
		<input type='hidden' name='distance' value='${su:htmlEscape(distance)}'>
		<input type='hidden' name='discountName' value='${su:htmlEscape(discountName)}'>
		<input type='hidden' name='val_tassign_status' value='${su:htmlEscape(val_tassign_status)}'><br>
		<input type='hidden' name='val_teiki_available' value='${su:htmlEscape(val_teiki_available)}'><br>
		<input type='hidden' name='val_restoreroute' value='${su:htmlEscape(val_restoreroute)}'><br>
		<input type='hidden' name='searchKbn' value='${su:htmlEscape(searchKbn)}'><br>
		<input type='hidden' name='kikanKbn' value='${su:htmlEscape(kikanKbn)}'>
		<input type='hidden' name='taxNonSupported' value='${su:htmlEscape(taxNonSupported)}'>

		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>
<!--
// 初期表示処理
$(document).ready(function() {

	var searchKbn = $('input[name=searchKbn]').val();
	var denpyouKbn = window.opener.$("input[name=denpyouKbn]").val();
	var kin = (String($('input[name=money]').val()).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	// 親画面に値を渡す

	if (searchKbn == 'searchRoute') {
		if(denpyouKbn == 'A006' && $('input[name=val_teiki_available]').val() == '0'){
			alert("選択した区間は定期区間として利用できません。");
			window.open('about:blank', '_top').close();
			return;
		}
		window.opener.$('input[name=jousyaKukan]').val($('input[name=route]').val());
		window.opener.$('input[name=norikaeKingaku]').val(kin);
		window.opener.$('input[name=idouKyori]').val($('input[name=distance]').val());
		window.opener.$('input[name=waribikiName]').val($('input[name=discountName]').val());
		window.opener.$('input[name=tassign_status]').val($('input[name=val_tassign_status]').val());
		window.opener.$('input[name=teikiSerializeData]').val($('input[name=val_restoreroute]').val());
		window.opener.$('input[name=taxNonSupported]').val($('input[name=taxNonSupported]').val());
		window.opener.setRouteIntra();
		setTenyuuryokuFlg();
	} else if (searchKbn == 'searchFrom') {
		window.opener.$('input[name=from]').val($('input[name=from]').val());
	} else if (searchKbn == 'searchTo01') {
		window.opener.$('input[name=to01]').val($('input[name=to01]').val());
	} else if (searchKbn == 'searchTo02') {
		window.opener.$('input[name=to02]').val($('input[name=to02]').val());
	} else if (searchKbn == 'searchTo03') {
		window.opener.$('input[name=to03]').val($('input[name=to03]').val());
	} else if (searchKbn == 'searchTo04') {
		window.opener.$('input[name=to04]').val($('input[name=to04]').val());
	}

	window.open('about:blank', '_top').close();
});

/**
 * 通勤定期申請の時のみ手入力フラグを初期化する
 */
function setTenyuuryokuFlg() {
	var denpyouKbn = window.opener.$("input[name=denpyouKbn]");
	var tenyuuryokuFlg = window.opener.$('input[name=tenyuuryokuFlg]');

	// 通勤定期申請
	if (denpyouKbn.length > 0  && denpyouKbn[0].value == 'A006' && tenyuuryokuFlg.length > 0) {
		tenyuuryokuFlg.val('0');
		try{window.opener.showTenyuuryokuFlg();}catch(e){}//呼出元=定期情報管理の時にここに入ってしまう
	}
}
-->
		</script>
	</body>
</html>
