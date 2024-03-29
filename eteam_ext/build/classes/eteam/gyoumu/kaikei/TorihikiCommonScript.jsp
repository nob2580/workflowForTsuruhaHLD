<%@ page import="eteam.symbol.EteamSymbol"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<script style='text/javascript'>
// 取引追加/変更の共通js

var denpyouKbn4DenpyouKanri = $("[name=denpyouKbn]").val();
if(denpyouKbn4DenpyouKanri == "A901") denpyouKbn4DenpyouKanri = "A001";
		
//掛け表示制御
function kakeSeigyo() {
	var kakeFlg = $("input[name=kakeOn]:checked").val();
	$("#taishaku").find("input,select").each(function(){
		if ($(this).attr("name").match(/2$/)) {
			if ("1" == kakeFlg)	$(this).show();
			else				$(this).hide();
		}
	});
	$("#taishaku").find("button").each(function(){
		if ($(this).attr("id").match(/2$/)) {
			if ("1" == kakeFlg)	$(this).show();
			else				$(this).hide();
		}
	});
}

//交際費表示制御
<c:if test='${inputSeigyo.kousaihiHyouji}'>
function kousaihiSeigyo() {
	var kousaihiFlgVar = $("input[name=kousaihiOn]:checked").val();
	var checkHouhouVar = $("input[name=kousaihiCheckHouhou]:checked").val();
	
	if(kousaihiFlgVar == "0"){
		$("input[name=ninzuuOn]:eq(1)").prop('checked', true);
		$("input[name=ninzuuOn]").attr("disabled", true);
	}else{
		$("input[name=ninzuuOn]").attr("disabled", false);
	}
	var ninzuuFlgVar   = $("input[name=ninzuuOn]:checked").val();
	
	if(kousaihiFlgVar == "1" && ninzuuFlgVar == "1"){
		$("input[name=kousaihiKijungaku]").attr("disabled", false);
		$("input[name=kousaihiCheckHouhou]").attr("disabled", false);
		
		if(checkHouhouVar == "1" || checkHouhouVar == "2"){
			$("input[name=kousaihiCheckResult]").attr("disabled", false);
		}else{
			$("input[name=kousaihiCheckResult]:eq(0)").prop('checked', true);
			$("input[name=kousaihiCheckResult]").attr("disabled", true);
		}
		
	}else{
		$("input[name=kousaihiKijungaku]").val("0");
		$("input[name=kousaihiCheckHouhou]:eq(0)").prop('checked', true);
		$("input[name=kousaihiCheckResult]:eq(0)").prop('checked', true);
		
		$("input[name=kousaihiKijungaku]").attr("disabled", true);
		$("input[name=kousaihiCheckHouhou]").attr("disabled", true);
		$("input[name=kousaihiCheckResult]").attr("disabled", true);
	}
}
</c:if>

<c:if test='${inputSeigyo.kariKamokuEda}'>
//財務枝番コード連携入力制御
function edabanRenkeiSeigyo() {
	if ($("select[name=zaimuEdabanRenkei]").val() == '1') {
		$("input[name=kariKamokuEdabanCd]").val("");
		$("input[name=kariKamokuEdabanCd]").attr("disabled", true);
		$("input[name=kariKamokuEdabanName]").val("");
		$("#kariKamokuEdabanSentaku").attr("disabled", true);
	}else{
		$("input[name=kariKamokuEdabanCd]").attr("disabled", false);
		$("#kariKamokuEdabanSentaku").attr("disabled", false);
	}
}
</c:if>

/**
 * 入力項目の値が、datalistの項目かどうか判定する
 */
function hasDataList(input) {
	var val = input.val();
	var listNm = input.attr('list');
	var obj = jQuery('#' + listNm).children();
	for( var i=0; i< obj.length; i++ ){
		var listVal = obj.eq(i).val();
		if (val === listVal) {
			return true;
		}
    }
	return false;
}

function setKbnSettings()
{
	setShouhizeiControls(null, $("input[name=kariShoriGroup]").val(), $("select[name=kariKazeiKbn]"), $("select[name=kariZeiritsu]"), $("select[name=kariBunriKbn]"), $("select[name=kariShiireKbn]"));

	if($("select[name=kariZeiritsu]").prop("disabled"))
	{
		//変更不可なら消費税率=任意消費税率
		$("select[name=kariZeiritsu]").val('<ZEIRITSU>');
		//軽減税率区分を非表示項目にセット
		$("input[name=kariKeigenZeiritsuKbn]").val($("select[name=kariZeiritsu] option:selected").attr("data-keigenZeiritsuKbn"));
	}
	if( $("input[name=kariKanjyouKamokuCd]").val() == ""){
		$("#kariDefaultKazeiKbn").text("");
		$("#kariDefaultBunriKbn").text("");
		$("#kariDefaultShiireKbn").text("");
		$("#kariDefaultZeiritsu").text("");
	}
}

function kbnDefaultCallback(prefix, type)
{
	if(prefix == "kari" && $("input[name=kariKanjyouKamokuCd]").val() != "")
	{
		$("#" + prefix + "Default" + type + "Kbn").text($("select[name=" + prefix + type + "Kbn] option:selected").text());
		if(type == "Bunri" && !(['001', '002', '011','012', '013','014'].includes($("select[name=kariKazeiKbn] option:selected").val()))){
			$("#kariDefaultBunriKbn").text("");
			$("#kariDefaultZeiritsu").text("");
		}
		if(type == "Shiire" && !(['001', '002', '011', '013'].includes($("select[name=kariKazeiKbn] option:selected").val())
				&& ['2', '5', '6', '7', '8', '10'].includes($("input[name=kariShoriGroup]").val()))){
			$("#kariDefaultShiireKbn").text("");
		}
	}
}

$(document).ready(function(){

	//摘要入力制御
	$("input[name=tekiyouNyuryokuOn]:eq(0)").click(function(e){
		$("input[name=tekiyou]").prop("disabled",true);
		$("input[name=tekiyou]").val("");
	});
	$("input[name=tekiyouNyuryokuOn]:eq(1)").click(function(e){
		$("input[name=tekiyou]").prop("disabled",false);
	});

	//掛け選択時表示制御
	$("input[name=kakeOn]").click(kakeSeigyo);
	if(denpyouKbn4DenpyouKanri == "A003"){
		kakeSeigyo();
	}
	
<c:if test='${inputSeigyo.kousaihiHyouji}'>
	//交際費選択時表示制御
	$("input[name=kousaihiOn]").click(kousaihiSeigyo);
	$("input[name=ninzuuOn]").click(kousaihiSeigyo);
	$("input[name=kousaihiCheckHouhou]").click(kousaihiSeigyo);
	kousaihiSeigyo();
</c:if>

	//社員コード連携によるUFの表示制御
	if ($("select[name=shainCdRenkei]").val() == '1') {
	<c:choose>
		<c:when test="${'UF1' == shainCdRenkeiArea}">
			$("#uf1").css("display","none");
		</c:when>
		<c:when test="${'UF2' == shainCdRenkeiArea}">
			$("#uf2").css("display","none");
		</c:when>
		<c:when test="${'UF3' == shainCdRenkeiArea}">
			$("#uf3").css("display","none");
		</c:when>
		<c:when test="${'UF4' == shainCdRenkeiArea}">
			$("#uf4").css("display","none");
		</c:when>
		<c:when test="${'UF5' == shainCdRenkeiArea}">
			$("#uf5").css("display","none");
		</c:when>
		<c:when test="${'UF6' == shainCdRenkeiArea}">
			$("#uf6").css("display","none");
		</c:when>
		<c:when test="${'UF7' == shainCdRenkeiArea}">
			$("#uf7").css("display","none");
		</c:when>
		<c:when test="${'UF8' == shainCdRenkeiArea}">
			$("#uf8").css("display","none");
		</c:when>
		<c:when test="${'UF9' == shainCdRenkeiArea}">
			$("#uf9").css("display","none");
		</c:when>
		<c:when test="${'UF10' == shainCdRenkeiArea}">
			$("#uf10").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI1' == shainCdRenkeiArea}">
			$("#ufKotei1").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI2' == shainCdRenkeiArea}">
			$("#ufKotei2").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI3' == shainCdRenkeiArea}">
			$("#ufKotei3").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI4' == shainCdRenkeiArea}">
			$("#ufKotei4").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI5' == shainCdRenkeiArea}">
			$("#ufKotei5").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI6' == shainCdRenkeiArea}">
			$("#ufKotei6").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI7' == shainCdRenkeiArea}">
			$("#ufKotei7").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI8' == shainCdRenkeiArea}">
			$("#ufKotei8").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI9' == shainCdRenkeiArea}">
			$("#ufKotei9").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI10' == shainCdRenkeiArea}">
			$("#ufKotei10").css("display","none");
		</c:when>
	</c:choose>
	} else {
	<c:choose>
		<c:when test="${'UF1' == shainCdRenkeiArea}">
			if (!$("#uf1").hasClass('never_show')) {
				$("#uf1").css("display","");
			}
		</c:when>
		<c:when test="${'UF2' == shainCdRenkeiArea}">
			if (!$("#uf2").hasClass('never_show')) {
				$("#uf2").css("display","");
			}
		</c:when>
		<c:when test="${'UF3' == shainCdRenkeiArea}">
			if (!$("#uf3").hasClass('never_show')) {
				$("#uf3").css("display","");
			}
		</c:when>
		<c:when test="${'UF4' == shainCdRenkeiArea}">
			if (!$("#uf4").hasClass('never_show')) {
				$("#uf4").css("display","");
			}
		</c:when>
		<c:when test="${'UF5' == shainCdRenkeiArea}">
			if (!$("#uf5").hasClass('never_show')) {
			$("#uf5").css("display","");
			}
		</c:when>
		<c:when test="${'UF6' == shainCdRenkeiArea}">
			if (!$("#uf6").hasClass('never_show')) {
			$("#uf6").css("display","");
			}
		</c:when>
		<c:when test="${'UF7' == shainCdRenkeiArea}">
			if (!$("#uf7").hasClass('never_show')) {
			$("#uf7").css("display","");
		}
		</c:when>
		<c:when test="${'UF8' == shainCdRenkeiArea}">
			if (!$("#uf8").hasClass('never_show')) {
			$("#uf8").css("display","");
		}
		</c:when>
		<c:when test="${'UF9' == shainCdRenkeiArea}">
			if (!$("#uf9").hasClass('never_show')) {
			$("#uf9").css("display","");
		}
		</c:when>
		<c:when test="${'UF10' == shainCdRenkeiArea}">
			if (!$("#uf10").hasClass('never_show')) {
			$("#uf10").css("display","");
		}
		</c:when>
		<c:when test="${'UF_KOTEI1' == shainCdRenkeiArea}">
			if (!$("#ufKotei1").hasClass('never_show')) {
				$("#ufKotei1").css("display","");
			}
		</c:when>
		<c:when test="${'UF_KOTEI2' == shainCdRenkeiArea}">
			if (!$("#ufKotei2").hasClass('never_show')) {
				$("#ufKotei2").css("display","");
			}
		</c:when>
		<c:when test="${'UF_KOTEI3' == shainCdRenkeiArea}">
			if (!$("#ufKotei3").hasClass('never_show')) {
				$("#ufKotei3").css("display","");
			}
		</c:when>
		<c:when test="${'UF_KOTEI4' == shainCdRenkeiArea}">
			if (!$("#ufKotei4").hasClass('never_show')) {
				$("#ufKotei4").css("display","");
			}
		</c:when>
		<c:when test="${'UF_KOTEI5' == shainCdRenkeiArea}">
			if (!$("#ufKotei5").hasClass('never_show')) {
			$("#ufKotei5").css("display","");
			}
		</c:when>
		<c:when test="${'UF_KOTEI6' == shainCdRenkeiArea}">
			if (!$("#ufKotei6").hasClass('never_show')) {
			$("#ufKotei6").css("display","");
			}
		</c:when>
		<c:when test="${'UF_KOTEI7' == shainCdRenkeiArea}">
			if (!$("#ufKotei7").hasClass('never_show')) {
			$("#ufKotei7").css("display","");
		}
		</c:when>
		<c:when test="${'UF_KOTEI8' == shainCdRenkeiArea}">
			if (!$("#ufKotei8").hasClass('never_show')) {
			$("#ufKotei8").css("display","");
		}
		</c:when>
		<c:when test="${'UF_KOTEI9' == shainCdRenkeiArea}">
			if (!$("#ufKotei9").hasClass('never_show')) {
			$("#ufKotei9").css("display","");
		}
		</c:when>
		<c:when test="${'UF_KOTEI10' == shainCdRenkeiArea}">
			if (!$("#ufKotei10").hasClass('never_show')) {
			$("#ufKotei10").css("display","");
		}
		</c:when>
	</c:choose>
	}
	$("select[name=shainCdRenkei]").change(function(){
		if ($(this).val() == '1') {
	<c:choose>
		<c:when test="${'UF1' == shainCdRenkeiArea}">
			$("#uf1").css("display","none");
		</c:when>
		<c:when test="${'UF2' == shainCdRenkeiArea}">
			$("#uf2").css("display","none");
		</c:when>
		<c:when test="${'UF3' == shainCdRenkeiArea}">
			$("#uf3").css("display","none");
		</c:when>
		<c:when test="${'UF4' == shainCdRenkeiArea}">
			$("#uf4").css("display","none");
		</c:when>
		<c:when test="${'UF5' == shainCdRenkeiArea}">
			$("#uf5").css("display","none");
		</c:when>
		<c:when test="${'UF6' == shainCdRenkeiArea}">
			$("#uf6").css("display","none");
		</c:when>
		<c:when test="${'UF7' == shainCdRenkeiArea}">
			$("#uf7").css("display","none");
		</c:when>
		<c:when test="${'UF8' == shainCdRenkeiArea}">
			$("#uf8").css("display","none");
		</c:when>
		<c:when test="${'UF9' == shainCdRenkeiArea}">
			$("#uf9").css("display","none");
		</c:when>
		<c:when test="${'UF10' == shainCdRenkeiArea}">
			$("#uf10").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI1' == shainCdRenkeiArea}">
			$("#ufKotei1").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI2' == shainCdRenkeiArea}">
			$("#ufKotei2").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI3' == shainCdRenkeiArea}">
			$("#ufKotei3").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI4' == shainCdRenkeiArea}">
			$("#ufKotei4").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI5' == shainCdRenkeiArea}">
			$("#ufKotei5").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI6' == shainCdRenkeiArea}">
			$("#ufKotei6").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI7' == shainCdRenkeiArea}">
			$("#ufKotei7").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI8' == shainCdRenkeiArea}">
			$("#ufKotei8").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI9' == shainCdRenkeiArea}">
			$("#ufKotei9").css("display","none");
		</c:when>
		<c:when test="${'UF_KOTEI10' == shainCdRenkeiArea}">
			$("#ufKotei10").css("display","none");
		</c:when>
	</c:choose>
		} else {
	<c:choose>
		<c:when test="${'UF1' == shainCdRenkeiArea}">
			if (!$("#uf1").hasClass('never_show')) {
				$("#uf1").css("display","");
			}
		</c:when>
		<c:when test="${'UF2' == shainCdRenkeiArea}">
			if (!$("#uf2").hasClass('never_show')) {
				$("#uf2").css("display","");
			}
		</c:when>
		<c:when test="${'UF3' == shainCdRenkeiArea}">
			if (!$("#uf3").hasClass('never_show')) {
				$("#uf3").css("display","");
			}
		</c:when>
		<c:when test="${'UF4' == shainCdRenkeiArea}">
			if (!$("#uf4").hasClass('never_show')) {
				$("#uf4").css("display","");
			}
		</c:when>
		<c:when test="${'UF5' == shainCdRenkeiArea}">
			if (!$("#uf5").hasClass('never_show')) {
				$("#uf5").css("display","");
			}
		</c:when>
		<c:when test="${'UF6' == shainCdRenkeiArea}">
			if (!$("#uf6").hasClass('never_show')) {
				$("#uf6").css("display","");
			}
		</c:when>
		<c:when test="${'UF7' == shainCdRenkeiArea}">
			if (!$("#uf7").hasClass('never_show')) {
				$("#uf7").css("display","");
			}
		</c:when>
		<c:when test="${'UF8' == shainCdRenkeiArea}">
			if (!$("#uf8").hasClass('never_show')) {
				$("#uf8").css("display","");
			}
		</c:when>
		<c:when test="${'UF9' == shainCdRenkeiArea}">
			if (!$("#uf9").hasClass('never_show')) {
				$("#uf9").css("display","");
			}
		</c:when>
		<c:when test="${'UF10' == shainCdRenkeiArea}">
			if (!$("#uf10").hasClass('never_show')) {
				$("#uf10").css("display","");
			}
		</c:when>
		<c:when test="${'UF_KOTEI1' == shainCdRenkeiArea}">
			if (!$("#ufKotei1").hasClass('never_show')) {
				$("#ufKotei1").css("display","");
			}
		</c:when>
		<c:when test="${'UF_KOTEI2' == shainCdRenkeiArea}">
			if (!$("#ufKotei2").hasClass('never_show')) {
				$("#ufKotei2").css("display","");
			}
		</c:when>
		<c:when test="${'UF_KOTEI3' == shainCdRenkeiArea}">
			if (!$("#ufKotei3").hasClass('never_show')) {
				$("#ufKotei3").css("display","");
			}
		</c:when>
		<c:when test="${'UF_KOTEI4' == shainCdRenkeiArea}">
			if (!$("#ufKotei4").hasClass('never_show')) {
				$("#ufKotei4").css("display","");
			}
		</c:when>
		<c:when test="${'UF_KOTEI5' == shainCdRenkeiArea}">
			if (!$("#ufKotei5").hasClass('never_show')) {
			$("#ufKotei5").css("display","");
			}
		</c:when>
		<c:when test="${'UF_KOTEI6' == shainCdRenkeiArea}">
			if (!$("#ufKotei6").hasClass('never_show')) {
			$("#ufKotei6").css("display","");
			}
		</c:when>
		<c:when test="${'UF_KOTEI7' == shainCdRenkeiArea}">
			if (!$("#ufKotei7").hasClass('never_show')) {
			$("#ufKotei7").css("display","");
		}
		</c:when>
		<c:when test="${'UF_KOTEI8' == shainCdRenkeiArea}">
			if (!$("#ufKotei8").hasClass('never_show')) {
			$("#ufKotei8").css("display","");
		}
		</c:when>
		<c:when test="${'UF_KOTEI9' == shainCdRenkeiArea}">
			if (!$("#ufKotei9").hasClass('never_show')) {
			$("#ufKotei9").css("display","");
		}
		</c:when>
		<c:when test="${'UF_KOTEI10' == shainCdRenkeiArea}">
			if (!$("#ufKotei10").hasClass('never_show')) {
			$("#ufKotei10").css("display","");
		}
		</c:when>
	</c:choose>
		}
	});
	
<c:if test='${inputSeigyo.kariKamokuEda}'>
	//借方枝番入力が可能な場合のみ有効化
	edabanRenkeiSeigyo();
	$("select[name=zaimuEdabanRenkei]").change(edabanRenkeiSeigyo);
</c:if>
	
	//(借方)課税区分による消費税率の入力制御
	if("1" !== $("select[name=kariKazeiKbn] option:selected").attr("data-kazeiKbnGroup")){
		//消費税率=任意消費税率 & 変更不可
		$("select[name=kariZeiritsu]").val("<ZEIRITSU>");
		$("select[name=kariZeiritsu]").attr("disabled", true);
		$("input[name=kariKeigenZeiritsuKbn]").val('');
	}
	
	// 負担部門ハンドラーの共通化
	function handleFutanBumonButtonClickAndInputBlur(prefix, sentakuButtonNumber) {
	    $("#" + prefix + "FutanBumonSentaku" + sentakuButtonNumber).click(function (e) {
	    	setFutanBumonDialogRets(prefix, sentakuButtonNumber);
	    	dialogCallbackFutanBumonSentaku = function()
        	{
        		kbnDefaultCallback(prefix, "Shiire");
	        };
	        commonFutanBumonSentaku("2", $("input[name=" + prefix + "KanjyouKamokuCd" + sentakuButtonNumber + "]").val());
	    });
	
	    $("input[name=" + prefix + "FutanBumonCd" + sentakuButtonNumber + "]").blur(function () {
	    	setFutanBumonDialogRets(prefix, sentakuButtonNumber);
	        var ret = hasDataList($(this));
	        if (ret) {
	            dialogRetFutanBumonName.val(null);
	        } else {
	            var title = $(this).parent().parent().find(".row-title").text();
		    	dialogCallbackFutanBumonSentaku = function()
	        	{
	        		kbnDefaultCallback(prefix, "Shiire");
		        };
	            commonFutanBumonCdLostFocus("2", dialogRetFutanBumonCd, dialogRetFutanBumonName, title, null, null, $("input[name=" + prefix + "KanjyouKamokuCd" + sentakuButtonNumber + "]").val(), dialogRetKariShiireKbn);
	        }
	    });
	}

	function setFutanBumonDialogRets(prefix, sentakuButtonNumber)
	{
		let isKari = prefix == "kari";
        dialogRetFutanBumonCd = $("input[name=" + prefix + "FutanBumonCd" + sentakuButtonNumber + "]");
        dialogRetFutanBumonName = $("input[name=" + prefix + "FutanBumonName" + sentakuButtonNumber + "]");
        dialogRetKariShiireKbn = isKari ? $("select[name=" + prefix + "ShiireKbn" + sentakuButtonNumber + "]") : null;
	}

	$("input[name=kariKanjyouKamokuCd]").ready(function () {
    	setKanjyouKamokuDialogRets("kari", "");
        var ret = hasDataList($(this));
        if (ret) {
            dialogRetKamokuName.val(null);
        } else {
            var title = $(this).parent().parent().find(".row-title").text();

	    	let prevKazeiKbn = $("select[name=kariKazeiKbn] option:selected").val();
	    	let prevBunriKbn = $("select[name=kariBunriKbn] option:selected").val();
	    	let prevShiireKbn = $("select[name=kariShiireKbn] option:selected").val();
	
	    	dialogCallbackKanjyouKamokuSentaku = function () {
	    		kbnDefaultCallback("kari", "Kazei");
	    		kbnDefaultCallback("kari", "Bunri");
	    		kbnDefaultCallback("kari", "Shiire");
	
	    		$("select[name=kariKazeiKbn]").val(prevKazeiKbn);
	    		$("select[name=kariBunriKbn]").val(prevBunriKbn);
	    		 //仕入区分非表示の場合には入力チェック回避のため値は入れない（税額按分方式変更時の対策）
	    		if($("input[name=shiireZeiAnbun]").val() == "1"){
		    		$("select[name=kariShiireKbn]").val(prevShiireKbn);
		    		$("select[name=kariShiireKbn]").prop('disabled', prevShiireKbn == "0");
	    		}
	    		setKbnSettings();
	    	};
        }
        commonKamokuCdLostFocus(dialogRetKamokuCd, dialogRetKamokuName, title, null, null, dialogRetKazeiKbn, dialogRetBunriKbn, dialogRetKariShiireKbn, dialogRetShoriGroup, dialogRetZeiritsuKbn);
	});
	
	// 勘定科目ハンドラーの共通化
	function handleKanjyouKamokuButtonClickAndInputBlur(prefix, sentakuButtonNumber) {
	    $("#" + prefix + "KanjyouKamokuSentaku" + sentakuButtonNumber).click(function (e) {
	    	setKanjyouKamokuDialogRets(prefix, sentakuButtonNumber);
	        dialogCallbackKanjyouKamokuSentaku = function () {
	            $("input[name=" + prefix + "KamokuEdabanCd" + sentakuButtonNumber + "]").val("");
	            $("input[name=" + prefix + "KamokuEdabanName" + sentakuButtonNumber + "]").val("");
	            $("input[name=" + prefix + "FutanBumonCd" + sentakuButtonNumber + "]").blur(); // 仕入区分については部門が優先
        		kbnDefaultCallback(prefix, "Kazei");
        		kbnDefaultCallback(prefix, "Bunri");
        		kbnDefaultCallback(prefix, "Shiire");
        		if(prefix == "kari")
        		{
	        		setKbnSettings();
        		}
	        };
	        commonKamokuSentaku();
	    });
	
	    $("input[name=" + prefix + "KanjyouKamokuCd" + sentakuButtonNumber + "]").blur(function () {
	    	setKanjyouKamokuDialogRets(prefix, sentakuButtonNumber);
	        var ret = hasDataList($(this));
	        if (ret) {
	            dialogRetKamokuName.val(null);
	        } else {
	            var title = $(this).parent().parent().find(".row-title").text();
	            dialogCallbackKanjyouKamokuSentaku = function () {
	                $("input[name=" + prefix + "KamokuEdabanCd" + sentakuButtonNumber + "]").val("");
	                $("input[name=" + prefix + "KamokuEdabanName" + sentakuButtonNumber + "]").val("");
	                $("input[name=" + prefix + "FutanBumonCd" + sentakuButtonNumber + "]").blur();
	        		kbnDefaultCallback(prefix, "Kazei");
	        		kbnDefaultCallback(prefix, "Bunri");
	        		kbnDefaultCallback(prefix, "Shiire");
	        		if(prefix == "kari")
	        		{
	            		if($("input[name=kariKanjyouKamokuCd]").val() == ""){
	            			$("select[name=kariKazeiKbn]").val("");
		            		setKbnSettings();
	            			$("select[name=kariKazeiKbn]").attr("disabled", true);
	            		}else{
	            			setKbnSettings();
	            		}
	        		}
	            };
	            commonKamokuCdLostFocus(dialogRetKamokuCd, dialogRetKamokuName, title, null, null, dialogRetKazeiKbn, dialogRetBunriKbn, dialogRetKariShiireKbn, dialogRetShoriGroup, dialogRetZeiritsuKbn);
	        }
	    });
	}

	function setKanjyouKamokuDialogRets(prefix, sentakuButtonNumber)
	{
		let isKari = prefix == "kari";
        dialogRetKamokuCd = $("input[name=" + prefix + "KanjyouKamokuCd" + sentakuButtonNumber + "]");
        dialogRetKamokuName = $("input[name=" + prefix + "KanjyouKamokuName" + sentakuButtonNumber + "]");
        dialogRetKazeiKbn = isKari ? $("select[name=" + prefix + "KazeiKbn" + sentakuButtonNumber + "]") : null;
        dialogRetBunriKbn = isKari ? $("select[name=" + prefix + "BunriKbn" + sentakuButtonNumber + "]") : null;
        dialogRetKariShiireKbn = isKari ? $("select[name=" + prefix + "ShiireKbn" + sentakuButtonNumber + "]") : null;
        dialogRetShoriGroup = isKari ? $("input[name=" + prefix + "ShoriGroup" + sentakuButtonNumber + "]") : null;
        dialogRetZeiritsuKbn = isKari ? $("#kariDefaultZeiritsu") : null;
	}
	
	// 勘定科目枝番ハンドラーの共通化
	function handleKamokuEdabanButtonClickAndInputBlur(prefix, sentakuButtonNumber) {
		let jaPrefix = prefix == "kashi" ? "貸方" : "借方";
	    $("#" + prefix + "KamokuEdabanSentaku" + sentakuButtonNumber).click(function (e) {
	        if ($("input[name=" + prefix + "KanjyouKamokuCd" + sentakuButtonNumber + "]").val() == "") {
	            alert("(" + jaPrefix + sentakuButtonNumber + ")勘定科目を入力してください。");
	        } else {
	        	setKamokuEdabanDialogRets(prefix, sentakuButtonNumber);
	        	dialogCallbackKanjyouKamokuEdabanSentaku = function()
	        	{
	        		kbnDefaultCallback(prefix, "Kazei");
	        		kbnDefaultCallback(prefix, "Bunri");
	        		setKbnSettings();
		        };
	            commonKamokuEdabanSentaku($("input[name=" + prefix + "KanjyouKamokuCd" + sentakuButtonNumber + "]").val());
	        }
	    });
	    
	    $("input[name=" + prefix + "KamokuEdabanCd" + sentakuButtonNumber + "]").blur(function () {
	        var kamokuCd = $("input[name=" + prefix + "KanjyouKamokuCd" + sentakuButtonNumber + "]").val();
	        setKamokuEdabanDialogRets(prefix, sentakuButtonNumber);
	        var ret = hasDataList($(this));
	        if (ret) {
	            dialogRetKamokuEdabanName.val(null);
	        } else {
	            if (kamokuCd == "" && dialogRetKamokuEdabanCd.val() != "") {
	                alert("(" + jaPrefix + sentakuButtonNumber + ")勘定科目を入力してください。");
	            } else {
	                var title = $(this).parent().parent().find(".row-title").text();
		        	dialogCallbackKanjyouKamokuEdabanSentaku = function()
		        	{
		        		kbnDefaultCallback(prefix, "Kazei");
		        		kbnDefaultCallback(prefix, "Bunri");
		        		if(prefix == "kari")
		        		{
		            		setKbnSettings();
		        		}
			        };
	                commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, kamokuCd, title, dialogRetKazeiKbn, dialogRetBunriKbn);
	            }
	        }
	    });
	}

	function setKamokuEdabanDialogRets(prefix, sentakuButtonNumber)
	{
		let isKari = prefix == "kari";
        dialogRetKamokuEdabanCd = $("input[name=" + prefix + "KamokuEdabanCd" + sentakuButtonNumber + "]");
        dialogRetKamokuEdabanName = $("input[name=" + prefix + "KamokuEdabanName" + sentakuButtonNumber + "]");
        dialogRetKazeiKbn = isKari ? $("select[name=" + prefix + "KazeiKbn" + sentakuButtonNumber + "]") : null;
        dialogRetBunriKbn = isKari ? $("select[name=" + prefix + "BunriKbn" + sentakuButtonNumber + "]") : null;
	}
	
	// 取引先ハンドラーの共通化
	function handleTorihikisakiButtonClickAndInputBlur(prefix, sentakuButtonNumber) {
	    $("#" + prefix + "TorihikisakiSentaku" + sentakuButtonNumber).click(function (e) {
	        dialogRetTorihikisakiCd = $("input[name=" + prefix + "TorihikisakiCd" + sentakuButtonNumber + "]");
	        dialogRetTorihikisakiName = $("input[name=" + prefix + "TorihikisakiName" + sentakuButtonNumber + "]");
	        commonTorihikisakiSentaku(denpyouKbn4DenpyouKanri);
	    });
	
	    $("input[name=" + prefix + "TorihikisakiCd" + sentakuButtonNumber + "]").blur(function () {
	        dialogRetTorihikisakiCd = $("input[name=" + prefix + "TorihikisakiCd" + sentakuButtonNumber + "]");
	        dialogRetTorihikisakiName = $("input[name=" + prefix + "TorihikisakiName" + sentakuButtonNumber + "]");
	        var ret = hasDataList($(this));
	        if (ret) {
	            dialogRetTorihikisakiName.val(null);
	        } else {
	            var title = $(this).parent().parent().find(".row-title").text();
	            commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd, dialogRetTorihikisakiName, title, null, denpyouKbn4DenpyouKanri);
	        }
	    });
	}
	
	
	// プロジェクトハンドラーの共通化
	function handleProjectButtonClickAndInputBlur(prefix, sentakuButtonNumber) {
	    $("#" + prefix + "ProjectSentaku" + sentakuButtonNumber).click(function (e) {
	        dialogRetProjectCd = $("input[name=" + prefix + "ProjectCd" + sentakuButtonNumber + "]");
	        dialogRetProjectName = $("input[name=" + prefix + "ProjectName" + sentakuButtonNumber + "]");
	        commonProjectSentaku();
	    });
	
	    $("input[name=" + prefix + "ProjectCd" + sentakuButtonNumber + "]").blur(function () {
	        dialogRetProjectCd = $("input[name=" + prefix + "ProjectCd" + sentakuButtonNumber + "]");
	        dialogRetProjectName = $("input[name=" + prefix + "ProjectName" + sentakuButtonNumber + "]");
	        var ret = hasDataList($(this));
	        if (ret) {
	            dialogRetProjectName.val(null);
	        } else {
	            var title = $(this).parent().parent().find(".row-title").text();
	            commonProjectCdLostFocus(dialogRetProjectCd, dialogRetProjectName, title);
	        }
	    });
	}
	
	// セグメントハンドラーの共通化
	function handleSegmentButtonClickAndInputBlur(prefix, sentakuButtonNumber) {
	    $("#" + prefix + "SegmentSentaku" + sentakuButtonNumber).click(function (e) {
	        dialogRetSegmentCd = $("input[name=" + prefix + "SegmentCd" + sentakuButtonNumber + "]");
	        dialogRetSegmentName = $("input[name=" + prefix + "SegmentName" + sentakuButtonNumber + "]");
	        commonSegmentSentaku();
	    });
	
	    $("input[name=" + prefix + "SegmentCd" + sentakuButtonNumber + "]").blur(function () {
	        dialogRetSegmentCd = $("input[name=" + prefix + "SegmentCd" + sentakuButtonNumber + "]");
	        dialogRetSegmentName = $("input[name=" + prefix + "SegmentName" + sentakuButtonNumber + "]");
	        var ret = hasDataList($(this));
	        if (ret) {
	            dialogRetSegmentName.val(null);
	        } else {
	            var title = $(this).parent().parent().find(".row-title").text();
	            commonSegmentCdLostFocus(dialogRetSegmentCd, dialogRetSegmentName, title);
	        }
	    });
	}
	
	// UFハンドラーの共通化
	function handleUfButtonClickAndInputBlur(prefix, ufNumber, ufLetter, sentakuButtonNumber) {
		let ufPrefix = prefix + ufLetter + ufNumber;
		let cdWithNumber = "Cd" + sentakuButtonNumber;
		let nameWithNumber = "Name" + sentakuButtonNumber;
		
		// Uf選択ボタンクリック時
	    $("#" + ufPrefix + "SentakuButton" + sentakuButtonNumber).click(function (e) {
	        dialogRetUniversalFieldCd = $("input[name=" + ufPrefix + cdWithNumber + "]");
	        dialogRetUniversalFieldName = $("input[name=" + ufPrefix + nameWithNumber + "]");
	        commonUniversalSentaku($("[name=" + prefix + "KanjyouKamokuCd" + sentakuButtonNumber + "]").val(), ufNumber);
	    });
	
		// UF入力欄フォーカスアウト時
	    $("input[name=" + ufPrefix + cdWithNumber + "]").blur(function () {
	        dialogRetUniversalFieldCd = $("input[name=" + ufPrefix + cdWithNumber + "]");
	        dialogRetUniversalFieldName = $("input[name=" + ufPrefix + nameWithNumber + "]");
	        var ret = hasDataList($(this));
	        if (ret) {
	            dialogRetUniversalFieldName.val(null);
	        } else {
	            var title = $(this).parent().parent().find(".row-title").text();
	            var isBtnVisible = $("#" + ufPrefix + "SentakuButton" + sentakuButtonNumber).is(":visible");
	            if (isBtnVisible) {
	                commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, ufNumber, title);
	            }
	        }
	    });
	}
	
	// イベントハンドラーの設定
	for (let sentakuButtonNumber = 1; sentakuButtonNumber <= 6; sentakuButtonNumber++) {
	
		let number = sentakuButtonNumber == 6 ? "" : sentakuButtonNumber;
		let prefix = sentakuButtonNumber == 6 ? "kari" : "kashi";
	
		// 負担部門
		handleFutanBumonButtonClickAndInputBlur(prefix, number);
		// 勘定科目
		handleKanjyouKamokuButtonClickAndInputBlur(prefix, number);
		// 勘定科目枝番
		handleKamokuEdabanButtonClickAndInputBlur(prefix, number);
		// 取引先
	    handleTorihikisakiButtonClickAndInputBlur(prefix, number);
		// プロジェクト
	    handleProjectButtonClickAndInputBlur(prefix, number);
		// セグメント
	    handleSegmentButtonClickAndInputBlur(prefix, number);
	    // UF・UF固定
		for (let ufNumber = 1; ufNumber <= 10; ufNumber++) {
	    	handleUfButtonClickAndInputBlur(prefix, ufNumber, "Uf", number);
	    	handleUfButtonClickAndInputBlur(prefix, ufNumber, "UfKotei", number);
		}
	    if(prefix == "kashi"){
	    	$("select[name=kashiBunriKbn"+ number + "]").val("9");
	    }
	}

	//(借方)課税区分変更時Function
	$("select[name=kariKazeiKbn]").change(function(){
		setShouhizeiControls(null, $("input[name=kariShoriGroup]").val(), $("select[name=kariKazeiKbn]"), $("select[name=kariZeiritsu]"), $("select[name=kariBunriKbn]"), $("select[name=kariShiireKbn]"));
		if($("select[name=kariZeiritsu]").prop("disabled"))
		{
			//変更不可なら消費税率=任意消費税率
			$("select[name=kariZeiritsu]").val('<ZEIRITSU>');
			//軽減税率区分を非表示項目にセット
			$("input[name=kariKeigenZeiritsuKbn]").val($("select[name=kariZeiritsu] option:selected").attr("data-keigenZeiritsuKbn"));
		}
	});
	
	//(借方)消費税率変更時Function
	$("select[name=kariZeiritsu]").blur(function(){
		//軽減税率区分を非表示項目にセット
		$("input[name=kariKeigenZeiritsuKbn]").val($("select[name=kariZeiritsu] option:selected").attr("data-keigenZeiritsuKbn"));
	});
});
</script>