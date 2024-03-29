<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>



<!-- エラー -->
<%@ include file="/jsp/eteam/include/InputError.jsp" %>


<%@ include file="/jsp/eteam/include/EkispertScript.jsp" %>


<!-- 駅すぱあとイントラ版WebService版判定フラグ -->
<input type='hidden' name='isIntra' value='${su:htmlEscape(isIntra)}'>
<!-- 定期区間表示フラグ -->
<input type='hidden' name='isShowTeikiKukan' value='${su:htmlEscape(isShowTeikiKukan)}'>
<!-- イントラ版起動URL -->
<input type='hidden' name='intraAction' value='${su:htmlEscape(intraAction)}'>

<!-- 駅すぱあとイントラ版 -->
<c:if test="${isIntra == '1'}" >
	<form id='norikaeIntraForm' name='norikaeIntraForm' method='get' target='_self' enctype="multipart/form-data" class='form-horizontal'>

		<!-- 呼出インターフェース名 -->
		<input type='hidden' name='val_htmb'>
		<!-- 戻り先URL -->
		<input type='hidden' name='val_cgi_url' value='${su:htmlEscape(val_cgi_url)}'>
		<!-- 駅名検索時必須パラメータ -->
		<input type='hidden' name='val_in_name'>
		<!-- 駅名検索時必須パラメータ -->
		<input type='hidden' name='searchKbn'>
		<!-- 経路検索時金額表示区分 -->
		<input type='hidden' name='val_oneway' value='${su:htmlEscape(val_oneway)}'>
		<!-- リクエストパラメータ名の大文字小文字区別 -->
		<input type='hidden' name='val_upperparam' value='y'>
		<!-- 経路検索時パラメータ 年 -->
		<input type='hidden' name='val_year'>
		<!-- 経路検索時パラメータ 月 -->
		<input type='hidden' name='val_month'>
		<!-- 経路検索時パラメータ 日 -->
		<input type='hidden' name='val_day'>
		<!-- 探索結果への反映(0：反映しない、1：反映する、省略時は0) -->
		<input type='hidden' name='val_tassign_reflect'>
		<!-- 定期券利用時の運賃計算(詳細な計算結果) -->
		<input type='hidden' name=val_tassignmode>
		<!-- 方向性をもった定期区間経路文字列 -->
		<input type='hidden' name='val_tassign_restoreroute'>
		<!-- 運賃改定未対応 -->
		<input type='hidden' name='taxNonSupported'>

		<div class='control-group'>
			<label class='control-label'>出発</label>
			<div class='controls'>
				<input type='text' name='from' value='${su:htmlEscape(from)}'>
				<input type='hidden' name='val_from'>

				<button type='button' class='btn btn-small' onclick="setValHtmb('searchFrom');"><i class='icon-search'></i> 駅名選択</button>
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label'>経由/到着</label>
			<div class='controls'>
				<input type='text' name='to01' value='${su:htmlEscape(to01)}'>
				<input type='hidden' name='val_to01'>
				<button type='button' class='btn btn-small' onclick="setValHtmb('searchTo01');"><i class='icon-search'></i> 駅名選択</button>
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label'>経由/到着</label>
			<div class='controls'>
				<input type='text' name='to02' value='${su:htmlEscape(to02)}'>
				<input type='hidden' name='val_to02'>
				<button type='button' class='btn btn-small' onclick="setValHtmb('searchTo02');"><i class='icon-search'></i> 駅名選択</button>
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label'>経由/到着</label>
			<div class='controls'>
				<input type='text' name='to03' value='${su:htmlEscape(to03)}'>
				<input type='hidden' name='val_to03'>
				<button type='button' class='btn btn-small' onclick="setValHtmb('searchTo03');"><i class='icon-search'></i> 駅名選択</button>
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label'>到着</label>
			<div class='controls'>
				<input type='text' name='to04' value='${su:htmlEscape(to04)}'>
				<input type='hidden' name='val_to04'>
				<button type='button' class='btn btn-small' onclick="setValHtmb('searchTo04');"><i class='icon-search'></i> 駅名選択</button>
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label'>期間</label>
			<div class='controls'>
				<input type='text' class='input-small datepicker' name='norikaeKaishiBi' value=''>
			</div>
		</div>
		<c:if test="${isShowTeikiKukan == '1'}">
		<div class='control-group'>
			<label class='control-label'>往復</label>
			<div class='controls'>
				<input type='checkbox' id="oufukuLabel" name='oufukuEki' value="1">
			</div>
		</div>
		</c:if>

	    <div style="text-align:center;margin:5px;">
<c:if test="${isShowTeikiKukan == '0'}">
			<button type='button' class="btn ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button-text ui-state-hover" onclick="setValHtmb('teiki')">
	    		<span class="ui-button-text">経路検索</span>
	    	</button>
</c:if>
<c:if test="${isShowTeikiKukan == '1'}">
			<button type='button' class="btn ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button-text ui-state-hover" onclick="setValHtmb('route');">
	    		<span class="ui-button-text">経路検索</span>
	    	</button>
</c:if>
		</div>

		<div class='control-group'>
			<label class='control-label'>交通手段</label>
			<div class='controls'>
				<input type='hidden' name='val_shinkansen' value=''>
				<label class='checkbox inline'>新幹線<input type='checkbox' name='val_shinkansenChk' value='1' checked></label>

				　<input type='hidden' name='val_nozomi' value=''>
				<label class='checkbox inline'>新幹線のぞみ<input type='checkbox' name='val_nozomiChk' value='1' checked></label>

				　<input type='hidden' name='val_expressonly' value=''>
				<label class='checkbox inline'>有料特急<input type='checkbox' name='val_expressonlyChk' value='1' checked></label>
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label'>表示件数</label>
			<div class='controls'>
			<label class='radio inline'><input type='radio' name='val_max_result' value='5' <c:if test='${"5" eq val_max_result}'>checked</c:if>>５件</label>
			<label class='radio inline'><input type='radio' name='val_max_result' value='10' <c:if test='${"10" eq val_max_result}'>checked</c:if>>１０件</label>
			<label class='radio inline'><input type='radio' name='val_max_result' value='15'<c:if test='${"15" eq val_max_result}'>checked</c:if>>１５件</label>
			<label class='radio inline'><input type='radio' name='val_max_result' value='20'<c:if test='${"20" eq val_max_result}'>checked</c:if>>２０件</label>
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label'>表示順</label>
			<div class='controls'>
				<select name='val_sorttype' class='input-large'>
<c:forEach var="record" items="${ekispertSortList}">
					<option value='${su:htmlEscape(record.naibu_cd)}' data-value='${su:htmlEscape(record.option1)}' <c:if test='${record.naibu_cd eq val_sorttype}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
</c:forEach>
				</select>

			</div>
		</div>
		<div class='control-group'>
			<label class='control-label'>乗車券計算</label>
			<div class='controls'>
				<label class='radio inline'><input type='radio' name='val_icticket_rdo' value='0' <c:if test='${"0" eq val_icticket}'>checked</c:if>>普通乗車券</label>
				<label class='radio inline'><input type='radio' name='val_icticket_rdo' id='val_icticket_rdo1' value='1' <c:if test='${"1" eq val_icticket}'>checked</c:if>>ICカード乗車券</label>
				<input type='hidden' name='val_icticket'>
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label'>寝台列車</label>
			<div class='controls'>
				<label class='radio inline'><input type='radio' name='val_sleepingcar' value='y'>優先して利用</label>
				<label class='radio inline'><input type='radio' name='val_sleepingcar' value='n' checked>利用しない</label>
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label'>高速バス</label>
			<div class='controls'>
				<label class='radio inline'><input type='radio' name='val_highwaybus' value='1'>優先して利用</label>
				<label class='radio inline'><input type='radio' name='val_highwaybus' value='2' checked>普通に利用</label>
				<label class='radio inline'><input type='radio' name='val_highwaybus' value='3'>極力利用しない</label>
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label'>連絡バス</label>
			<div class='controls'>
				<label class='radio inline'><input type='radio' name='val_airbus' value='1'>優先して利用</label>
				<label class='radio inline'><input type='radio' name='val_airbus' value='2' checked>普通に利用</label>
				<label class='radio inline'><input type='radio' name='val_airbus' value='3'>極力利用しない</label>
			</div>
		</div>
		<div class='control-group'>
			<label class='control-label'>船</label>
			<div class='controls'>
				<label class='radio inline'><input type='radio' name='val_ship' value='1'>気軽に利用</label>
				<label class='radio inline'><input type='radio' name='val_ship' value='2' checked>普通に利用</label>
				<label class='radio inline'><input type='radio' name='val_ship' value='3'>極力利用しない</label>
			</div>
		</div>
<c:if test="${isShowTeikiKukan == '1'}">
		<div class='control-group'>
			<label class='control-label'>EX予約/スマートEX</label>
			<div class='controls'>
			<select name='val_jr_yoyaku' class='input-xlarge'>
<c:forEach var="record" items="${ekispertJrYoyakuList}">
					<option value='${su:htmlEscape(record.naibu_cd)}' data-value='${su:htmlEscape(record.option1)}' <c:if test='${record.naibu_cd eq val_jr_yoyaku}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
</c:forEach>
			</select>
			</div>
		</div>
				<div class='control-group'>
			<label class='control-label'>新幹線eチケット</label>
			<div class='controls'>
				<label class='radio inline'><input type='radio' name='val_sinkansen_e_ticket' value='1'>利用する</label>
				<label class='radio inline'><input type='radio' name='val_sinkansen_e_ticket' value='0' checked>利用しない</label>
			</div>
		</div>
</c:if>
	    <div id="teikikukan">
	    	<div style="float:left;width:140px;">定期区間</div>
	      	<div style="float:left;width:90%;">
	    		<textarea id="passRoute" style='width:100%;height:60px;' disabled></textarea>
	    	</div>
	    	<div style="clear:both;"></div>
	    </div>
		<input type='hidden' name='kikanKbn'>
		<input type='hidden' name='jousyaKukan' >
		<input type='hidden' name='norikaeKingaku'>
		<input type='hidden' name='idouKyori' >
		<input type='hidden' name='tassign_status'>
		<input type='hidden' name='waribikiName'>
	</form>
</c:if>

<!-- 駅すぱあとWebサービス版 -->
<c:if test="${isIntra == '0'}" >

    <!-- パーツ表示部分 -->
    <div id="dateTime"></div>
    <div>
    	<div style="float:left;width:140px;">出発</div>
    	<div id="station1" style="float:left;width:60%;"></div>
    	<div style="clear:both;"></div>
    </div>
    <div>
    	<div style="float:left;width:140px;">経由/到着</div>
    	<div id="station2" style="float:left;width:60%;"></div>
    	<div style="clear:both;"></div>
    </div>
    <div>
    	<div style="float:left;width:140px;">　</div>
    	<div id="station3" style="float:left;width:60%;"></div>
    	<div style="clear:both;"></div>
    </div>
    <div>
    	<div style="float:left;width:140px;">　</div>
    	<div id="station4" style="float:left;width:60%;"></div>
    	<div style="clear:both;"></div>
    </div>
    <div>
    	<div style="float:left;width:140px;">　</div>
    	<div id="station5" style="float:left;width:60%;"></div>
    	<div style="clear:both;"></div>
    </div>
    <div id="oufukuLabelBox"><!-- 20220217 追加 -->
    	<div style="float:left;width:140px;">往復</div>
    	<input type='checkbox' id="oufukuLabel" name='oufukuEki' value="1">
    	<div style="clear:both;"></div>
    </div>
    <div style="text-align:center;margin:5px;">
    	<button class="btn ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button-text ui-state-hover" onclick="Javascript:search();">
    		<span class="ui-button-text">経路検索</span>
    	</button>
    </div>
    <div id="condition"></div>
    <div id="teikikukan">
    	<div style="float:left;width:140px;">定期区間</div>
      	<div style="float:left;width:90%;">
    		<textarea id="passRoute" style='width:100%;height:60px;' disabled></textarea>
    		<input type='hidden' id='teikiSerializeData' value=''>
    	</div>
    	<div style="clear:both;"></div>
    </div>

    <div id="result"></div>

</c:if>




<script style='text/javascript'>
var isIntra = '${su:htmlEscape(isIntra)}';

//初期表示処理
$(document).ready(function(){

	//イントラ版
	if(isIntra == 1){
		commonInit($("#norikaeIntraForm"));
		var denpyouKbn = $("input[name=denpyouKbn]").val();

		//日付と期間区分(01/03/06)初期セット
		$("input[name=norikaeKaishiBi]").val(getDenpyouDate);
		if (denpyouKbn == 'A006') {
			$("input[name=kikanKbn]").val($("input[name=shiyouKikanKbn]:checked").val());
		}

		//のぞみがONなら新幹線自体ON
		$("[name=val_shinkansenChk]").change(function(){
			if(! $(this).prop("checked")){
				$("[name=val_nozomiChk]").prop("checked", false);
			}
		});
		$("[name=val_nozomiChk]").change(function(){
			if($(this).prop("checked")){
				$("[name=val_shinkansenChk]").prop("checked", true);
			}
		});

		// IC乗車券区分全社指定による検索条件設定可否の制御
		if( "${setting.ekispertIcZenshashitei()}" == "1"){
			$("[name=val_icticket_rdo]").prop("disabled", true);
		}

		// 伝票区分により検索料金デフォルト値設定
		if ($("input[name=denpyouKbn]").val() == 'A006'){
			$("#teikikukan").hide();
		}

		//定期区間を初期日付で引っ張ってくる※定期検索なら何もしない
		setPassRoute();

		return;
	}

	//以下はWEB版
	  // 日付入力パーツ初期化
	  dateTimeApp = new expGuiDateTime(document.getElementById("dateTime"));
	  dateTimeApp.dispDateTime();
	  // 駅名入力パーツ#1初期化
	  stationApp1 = new expGuiStation(document.getElementById("station1"));
	  stationApp1.dispStation();
	  // 駅名入力パーツ#2初期化
	  stationApp2 = new expGuiStation(document.getElementById("station2"));
	  stationApp2.dispStation();
	  // 駅名入力パーツ#3初期化
	  stationApp3 = new expGuiStation(document.getElementById("station3"));
	  stationApp3.dispStation();
	  // 駅名入力パーツ#4初期化
	  stationApp4 = new expGuiStation(document.getElementById("station4"));
	  stationApp4.dispStation();
	  // 駅名入力パーツ#5初期化
	  stationApp5 = new expGuiStation(document.getElementById("station5"));
	  stationApp5.dispStation();
	  // 探索条件パーツ初期化
	  conditonApp = new expGuiCondition(document.getElementById("condition"));
	  conditonApp.dispCondition();
	  // 経路表示パーツ初期化
	  resultApp = new expGuiCourse(document.getElementById("result"));

	  // 伝票区分により検索料金デフォルト値設定
	  if ($("input[name=denpyouKbn]").val() == 'A006'){
		  $("#teikikukan").hide();
		  $("#oufukuLabelBox").hide();
		  conditonApp.setCondition("priceType","teiki");
	  }else{
		  conditonApp.setCondition("priceType","oneway");
	  }
	  //表示運賃設定部を隠す
	  $("#condition\\:pricetype\\:condition").hide();

	  // 乗車券計算(ICか普通)を指定
	  var tcType = "${setting.ekispertIcKbn()}";
	  conditonApp.setCondition("ticketSystemType",tcType);

	  // 優先乗車券指定を設定
	  var icPref = "${setting.ekispertWebIcPreferred()}";
	  icPref = icPref.replace("preferredTicketOrder=","");
	  if (icPref == "" || tcType == "normal"){icPref = "none"};
	  conditonApp.setCondition("preferredTicketOrder",icPref);

  	  // IC乗車券区分全社指定による検索条件設定可否の制御
  	  if( "${setting.ekispertIcZenshashitei()}" == "1"){
   		$("#condition\\:ticketsystemtype\\:title").css("color","darkgray");
		$("label[for^=condition\\:ticketsystemtype]").css('color', "darkgray");
  		$("input[name=condition\\:ticketsystemtype]").prop('disabled', true);
  		$("#condition\\:preferredticketorder\\:title").css("color","darkgray");
		$("label[for^=condition\\:preferredticketorder]").css('color', "darkgray");
  		$("input[name=condition\\:preferredticketorder]").prop('disabled', true);
  		resultApp.setConfigure("PriceChange","false");

  		//スマートフォン向け画面制御
  		if($('#condition\\:ticketsystemtype').length){
  			$('#condition\\:ticketsystemtype').prop('disabled', true);
  		}
  		if($('#condition\\:preferredticketorder').length){
  			$('#condition\\:preferredticketorder').prop('disabled', true);

  		}
  	  }

	  // 日付指定されていた場合はダイアログ側に渡す
	  var dttmp = getDenpyouDate();
	  if (dttmp != null) {
		  dateTimeApp.setDate(dttmp);
	  }else{
		  //dateTimeApp.setNow();
	  }
	  // 初期検索形式を「平均」に指定
	  dateTimeApp.setSearchType("plain");
	  setPassRoute();

});

//駅リストロストフォーカス時の非表示処理
$(document).on("click","#dialog",function(e){

	if(isIntra == 1){return;};

	if(!$('#station1\\:stationInput').is(':focus')){
		$('#station1\\:stationList').hide();
		$('#station1\\:stationPopup').hide();
		$('#station1\\:stationPopupBack').hide();
	};
	if(!$('#station2\\:stationInput').is(':focus')){
		$('#station2\\:stationList').hide();
		$('#station2\\:stationPopup').hide();
		$('#station2\\:stationPopupBack').hide();
	};
	if(!$('#station3\\:stationInput').is(':focus')){
		$('#station3\\:stationList').hide();
		$('#station3\\:stationPopup').hide();
		$('#station3\\:stationPopupBack').hide();
	};
	if(!$('#station4\\:stationInput').is(':focus')){
		$('#station4\\:stationList').hide();
		$('#station4\\:stationPopup').hide();
		$('#station4\\:stationPopupBack').hide();
	};
	if(!$('#station5\\:stationInput').is(':focus')){
		$('#station5\\:stationList').hide();
		$('#station5\\:stationPopup').hide();
		$('#station5\\:stationPopupBack').hide();
	};
});

//日付が変わったら定期も引き直す
$("#dateTime\\:date\\:mm").change(function () {
	setPassRoute();
});
$("#dateTime\\:date\\:dd").change(function () {
	setPassRoute();
});
$("#dateTime\\:date").change(function () {
	setPassRoute();
});
$("#dateTime\\:setNow").click(function () {
	setPassRoute();
});
$("[name=norikaeKaishiBi]").change(function () {
	setPassRoute();
});


//定期情報の取得・転記
function setPassRoute(){

	var denpyouKbn = $("input[name=denpyouKbn]").val();

	if (denpyouKbn == 'A006') return;

	//国内海外旅費伺い・旅費精算の代理起票時は使用者のIDを渡す
	var dairiShiyouId = "";
	if(denpyouKbn == "A004" || denpyouKbn == "A005" || denpyouKbn == "A011" || denpyouKbn == "A012") {
		if($("input[name=dairiFlg]").val() == "1"){
			dairiShiyouId = $("input[name=userIdRyohi]").val();
		}
	}

	var dtfmt = "";
	if(isIntra == 1){
		var dtfmt = $("[name=norikaeKaishiBi]").val();
	}else{
		var dttmp = dateTimeApp.getDate();
		var dtfmt = dttmp.substring(0,4) + "/" + dttmp.substring(4,6) + "/" + dttmp.substring(6,8);
	}
	if(dtfmt == ""){
		if(isIntra == 1){
			$("[name=val_tassign_reflect]").val("");
			$("[name=val_tassignmode]").val("");
			$("[name=val_tassign_restoreroute]").val("");
		}
		return;
	}
	$.ajax({
		type : "GET",
		url : "teiki_kukan_get",
		data : "date=" + encodeURI(dtfmt) + "&isIntra=" + encodeURI($('input[name=isIntra]').val()) + "&denpyouId=" + encodeURI($('input[name=denpyouId]').val()) + "&dairiShiyouId=" + encodeURI(dairiShiyouId),
		dataType : 'text',
		success : function(response) {
			var dataAry = response.split("\t");
			if(isIntra == 1){
				$("#passRoute").val(dataAry[0]);
				$("[name=val_tassign_reflect]").val("1");
				$("[name=val_tassignmode]").val("3");
				$("[name=val_tassign_restoreroute]").val(dataAry[1]);
			}else{
				var kukan = dataAry[0];
				var seri = dataAry[1];
				$("#passRoute").val(kukan);
				$("#teikiSerializeData").val(seri);
			}
		}
	});
}

//経路検索
function search(){
	  //入力チェック後に動作
	  if(checkData()){
	    var searchWord = "";
	    // 発着地リストを作成
	    searchWord +="viaList="+ stationApp1.getStation() +":"+ stationApp2.getStation()
	    					   + (stationApp3.getStation() != '' ? ":" + stationApp3.getStation() : "")
	    					   + (stationApp4.getStation() != '' ? ":" + stationApp4.getStation() : "")
	    					   + (stationApp5.getStation() != '' ? ":" + stationApp5.getStation() : "");
	    // 探索種別
	    searchWord += '&searchType='+ dateTimeApp.getSearchType();
	    // 日時設定
	    searchWord += '&date='+ dateTimeApp.getDate();
	    if(dateTimeApp.getSearchType()==dateTimeApp.SEARCHTYPE_DEPARTURE || dateTimeApp.getSearchType()==dateTimeApp.SEARCHTYPE_ARRIVAL){
	      searchWord += '&time='+ dateTimeApp.getTime();
	    }
	    // ソート
	    searchWord += '&sort='+ conditonApp.getSortType();
	    // 探索結果数
	    searchWord += '&answerCount='+ conditonApp.getAnswerCount();
	    // 探索条件
	    searchWord += '&conditionDetail='+ conditonApp.getConditionDetail();
	    //20220217 追加
		if ($("input[name=denpyouKbn]").val() == 'A006'){
		  	conditonApp.setCondition("priceType","teiki");
	    }else{
			if($("input[name=oufukuEki]").prop('checked')){
				conditonApp.setCondition("priceType","round");
			}else{
		  		conditonApp.setCondition("priceType","oneway");
		  		//conditonApp.setCondition("priceType","round");
			}
	    }
	    // 定期券情報をセットする
	    var passRoute = document.getElementById("passRoute").value;
	    var teikiSerializeData = $("#teikiSerializeData").val();
		if (teikiSerializeData != null && teikiSerializeData != "" && !(typeof teikiSerializeData == 'undefined') ) {
			searchWord = searchWord + "&assignTeikiSerializeData=" + teikiSerializeData;
			//定期情報取得時のAPIエンジンバージョンによるチェックを実施しない
			searchWord = searchWord + "&checkEngineVersion=false";
		} else if (passRoute != null && passRoute != "" && !(typeof passRoute == 'undefined') ) {
			searchWord = searchWord + "&assignRoute=" + passRoute;
		}
	    //増税後10予想適用なし
		//searchWord += '&applyPredictedConsumptionTax=true';

	    // 探索を実行
	    resultApp.search(searchWord,conditonApp.getPriceType());
	  }
}

	/*
	 * 探索前に入力チェックを行う
	 */
function checkData(){
	  // メッセージの初期化
	  var errorMessage="";
	  if(!dateTimeApp.checkDate()){
	    // 日付入力パーツのチェック
	    errorMessage +="\n日付を正しく入力してください。";
	  }
	  if(stationApp1.getStation() == ""){
	    // 駅名入力パーツの空チェック
	    errorMessage +="\n出発駅は必須です。";
	  }
	  if(stationApp2.getStation() == ""){
	    // 駅名入力パーツの空チェック
	    errorMessage +="\n到着駅は必須です。";
	  }else{
	    if(stationApp1.getStation() == stationApp2.getStation()){
	      // 駅名同一チェック
	      errorMessage +="\n出発駅と目的駅が同一です。";
	    }
	  }
	  if(errorMessage != ""){
	    alert("下記の項目を確認してください。"+errorMessage);
	    return false;
	  }else{
	    return true;
	  }
}

//選択経路反映処理(Web版)
function setRoute(){

	//通勤定期申請の場合、定期金額が取得できない経路の指定時はエラーとする
	var denpyouKbn = $("input[name=denpyouKbn]").val();
	if (denpyouKbn == 'A006') {
		var kikan = Number($("input[name=shiyouKikanKbn]:checked").val());
		if(typeof resultApp.getTeikiPrice(kikan) == 'undefined'){
			alert("指定区間の定期金額が取得できません。");
			return false;
		}
		if(typeof resultApp.getTeikiSerializeData() == 'undefined'){
			alert("指定区間の定期シリアライズデータが取得できません。");
			return false;
		}
	}


	var strRoute = "";
	var lineStrArray;
	var pointStrArray = resultApp.getPointList().split(",");

	var strBikou = "";

	//会社設定[定期区間反映先]による分岐
	if( "${setting.ekispertKeiroKanihyouji()}" == '0' ){
		//0:経路詳細表示
		lineStrArray = resultApp.getLineList().split(",");

		//ICカード乗車券等の情報追加
		var strTicketKind = "";
		for(i = 0 ; i < lineStrArray.length ; i++){
			strTicketKind = resultApp.getTicketKind(i+1);
			if(strTicketKind != "" && denpyouKbn != 'A006'){
				lineStrArray[i] = lineStrArray[i] + "[" + strTicketKind + "]";
			}
		}

	}else{
		//1:経路簡易表示
		lineStrArray = resultApp.getLineListTypical().split(",");
		for(i = 0 ; i < lineStrArray.length ; i++){
			//「・**行」の記載がある場合削除(行先の削除された情報TypicalNameが設定されていない場合がある)
			lineStrArray[i] = lineStrArray[i].replace(new RegExp("・[^・]+行$"),"");
		}
	}

	//駅名＝路線名＝駅名＝路線名＝駅名・・・の順で結合
	for(i = 0 ; i < pointStrArray.length ; i++){
		strRoute = strRoute + pointStrArray[i];
		if(i == lineStrArray.length){break;}
		strRoute = strRoute + "＝" + lineStrArray[i] + "＝";
	}

	//距離情報を反映
	//ただし定期の入力なら反映させない
	var strKyori = resultApp.getDistance();
	if (denpyouKbn != 'A006') {
		if( "${setting.ekispertKyoriHanei()}" == '1' ){
			//内容
			strRoute = strRoute + "　" + strKyori;
		}else if( "${setting.ekispertKyoriHanei()}" == '2' ){
			//備考
			if(getBikouField() != null){
				getBikouField().val(strKyori);
			}
			//20220218 追加
			strBikou = strBikou + strKyori;
		}
	}

	//運賃改定未対応状況反映
	var kaiteiAlert = resultApp.getKaiteiAlert();
	if(kaiteiAlert != null && kaiteiAlert != ""){
		strRoute = strRoute  + "\r\n※" + kaiteiAlert;
	}

	//定期区間情報反映
	//※定期区間との重複区間が無い場合は出力させない
	if(resultApp.checkWithTeiki()){
		var strTeikikukan = "【定期区間】"+$("#passRoute").val();

		if( "${setting.ekispertTeikikukanHaneisaki()}" == '1' ){
			//1:内容
			strRoute = strRoute + "\r\n" + strTeikikukan;
		}else{
			//2:備考
			if(getBikouField() != null){
				if( "${setting.ekispertKyoriHanei()}" != '2' ){
					getBikouField().val(strTeikikukan);
				}else{
					getBikouField().val(strKyori + "　" + strTeikikukan);
				}
			}
		}
	}

	//指定乗車区間情報の反映
	getRouteField().val(strRoute);
	var dttmp = dateTimeApp.getDate();
	var dtfmt = dttmp.substring(0,4) + "/" + dttmp.substring(4,6) + "/" + dttmp.substring(6,8);
	if (denpyouKbn == 'A006') {
		// 通勤定期申請
		var kikan = Number($("input[name=shiyouKikanKbn]:checked").val());
		var seri = resultApp.getTeikiSerializeData();
		getKingakuField().val(String(resultApp.getTeikiPrice(kikan)).formatMoney());
		$("input[name=teikiSerializeData]").val(seri);
		$("input[name=shiyouKaishiBi]").val(dtfmt);
		$("input[name=tenyuuryokuFlg]").val("0");
		try{showTenyuuryokuFlg();}catch(e){}//呼出元=定期情報管理の時にここに入ってしまう
		if (typeof(changeEnableKukanKingaku) == 'function'){
			changeEnableKukanKingaku();
			shiyouShuuryouHenkou();
		}else{
			callShiyouShuuryouHenkou();
		}
		isDirty = true;
	}else{
		//20220217 追加
		var isRound = $("input[name=oufukuEki]").prop("checked");
		// 交通費明細
		//getKingakuField().val(String(resultApp.getPrice()).formatMoney());
		var kingaku = resultApp.getPrice(isRound ? "round" : "oneway");
		if(isRound){
			kingaku = kingaku / 2;
		}
		getKingakuField().val(String(kingaku).formatMoney());
		$("#dialogMainDenpyouMeisai").find("input[name=shubetsu1]").val("交通費");
		$("#dialogMainDenpyouMeisai").find("span.shubetsu1").text($("#dialogMainDenpyouMeisai").find("input[name=shubetsu1]").val());
		$("#dialogMainDenpyouMeisai").find("input[name=jidounyuuryokuFlg]").val("1");
		$("#dialogMainDenpyouMeisai").find("input[name=shubetsu2]").val("自動入力");
		$("#dialogMainDenpyouMeisai").find("span.shubetsu2").text($("#dialogMainDenpyouMeisai").find("input[name=shubetsu2]").val());
		$("#dialogMainDenpyouMeisai").find("input[name=kikanFrom]").val(dtfmt);
		$("#dialogMainDenpyouMeisai").find("input[name=ryoushuushoSeikyuushoTouFlg]").prop("checked", false);
		$("#dialogMainDenpyouMeisai").find("input[name=shouhyouShoruiHissuFlg]").val("0");

		//20220224 追加
		var remarkName = resultApp.getFullRemark();
		if( "${setting.ekispertWaribikiHanei()}" == '1' ){
			//内容
			strRoute = strRoute + "　" + remarkName;
			//getRouteField().val(strRoute);
		}else if( "${setting.ekispertWaribikiHanei()}" == '2' ){
			//備考
			if(strBikou.length > 0){
				strBikou = strBikou + "　";
			}
			strBikou = strBikou + remarkName;
		}

		$("#dialogMainDenpyouMeisai").find("input[name=oufukuFlg]").prop("checked", isRound);
		//20220217
		if(isRound){
			//往復チェックON
			//$("#dialogMainDenpyouMeisai").find("input[name=oufukuFlg]").prop("checked", true);
			//往復チェックボックスを無効にする
			//$("#dialogMainDenpyouMeisai").find("[name=oufukuFlg]").prop("disabled", true);
			//20220315 往復検索した場合に種別2の表示を変える　この文字列で往復チェック有効無効判定
			$("#dialogMainDenpyouMeisai").find("input[name=shubetsu2]").val("自動 往復");
			$("#dialogMainDenpyouMeisai").find("span.shubetsu2").text($("#dialogMainDenpyouMeisai").find("input[name=shubetsu2]").val());

			//往復割引の判定
			if(resultApp.isRoundDiscount()){
				if("${setting.ekispertOufukuwariHanei()}" == '1'){
					strRoute = strRoute + "　往復割引運賃適用";
				}else if( "${setting.ekispertOufukuwariHanei()}" == '2' ){
					//備考
					if(strBikou.length > 0){
						strBikou = strBikou + "　";
					}
					strBikou = strBikou + "往復割引運賃適用";
				}
			}
		}
		getRouteField().val(strRoute);
		if(getBikouField() != null){
			getBikouField().val(strBikou);
		}
		//202210早安楽
		var hayayasurakuStr = resultApp.isFastCheapMinTransfer();
		var hayaStr = hayayasurakuStr.substring(0,1);
		var yasuStr = hayayasurakuStr.substring(1,2);
		var rakuStr = hayayasurakuStr.substring(2);
		$("#dialogMainDenpyouMeisai").find("input[name=hayaFlg]").val(hayaStr);
		if(hayaStr == "1"){
			$("#dialogMainDenpyouMeisai").find("#hayaIcon").show();
		}else{
			$("#dialogMainDenpyouMeisai").find("#hayaIcon").hide();
		}
		$("#dialogMainDenpyouMeisai").find("input[name=yasuFlg]").val(yasuStr);
		if(yasuStr == "1"){
			$("#dialogMainDenpyouMeisai").find("#yasuIcon").show();
		}else{
			$("#dialogMainDenpyouMeisai").find("#yasuIcon").hide();
		}
		$("#dialogMainDenpyouMeisai").find("input[name=rakuFlg]").val(rakuStr);
		if(rakuStr == "1"){
			$("#dialogMainDenpyouMeisai").find("#rakuIcon").show();
		}else{
			$("#dialogMainDenpyouMeisai").find("#rakuIcon").hide();
		}
		
		inputSeigyo();
	}

	$("#dialog").children().remove();
	$("#dialog").dialog('close');
}

/**
 * アクション切り替え処理(イントラ版)
 */
function setValHtmb(eventName) {
	if(eventName == 'route' || eventName == 'teiki'){
		if($("[name=from]").val() == ""){
			alert("出発駅は必須です");
			return;
		}
		if($("[name=to01]").val() == ""){
			alert("到着駅は必須です");
			return;
		}
	}

	form = document.getElementById("norikaeIntraForm");
	form.action = $("input[name=intraAction]").val();
	form.method = 'get';
	form.target = '_blank';
	form.rel = 'opener'; //ChromeVer108でイントラ版を使用する対応

	try {
		// キャラセットにSHIFT-JISを設定
		// フォームのオプションにSHIFT-JISを設定
		// org
		org = document.charset;
		orgCharacterSet = document.characterSet;
		orgForm = form.acceptCharset;

		// 変更
		document.characterSet = 'Shift_JIS';
		document.charset = 'Shift_JIS';
		form.acceptCharset='Shift_JIS';
	}catch(e){}

	switch (eventName) {
	case 'searchFrom':
		$("input[name=val_htmb]").val("cgi_select_station");
		$("input[name=val_from]").val($("input[name=from]").val());
		$("input[name=val_in_name]").val($("input[name=from]").val());
		$("input[name=searchKbn]").val("searchFrom");
		break;
	case 'searchTo01':
		$("input[name=val_htmb]").val("cgi_select_station");
		$("input[name=val_to01]").val($("input[name=to01]").val());
		$("input[name=val_in_name]").val($("input[name=to01]").val());
		$("input[name=searchKbn]").val("searchTo01");

		break;
	case 'searchTo02':
		$("input[name=val_htmb]").val("cgi_select_station");
		$("input[name=val_to02]").val($("input[name=to02]").val());
		$("input[name=val_in_name]").val($("input[name=to02]").val());
		$("input[name=searchKbn]").val("searchTo02");

		break;
	case 'searchTo03':
		$("input[name=val_htmb]").val("cgi_select_station");
		$("input[name=val_to03]").val($("input[name=to03]").val());
		$("input[name=val_in_name]").val($("input[name=to03]").val());
		$("input[name=searchKbn]").val("searchTo03");

		break;
	case 'searchTo04':
		$("input[name=val_htmb]").val("cgi_select_station");
		$("input[name=val_to04]").val($("input[name=to04]").val());
		$("input[name=val_in_name]").val($("input[name=to04]").val());
		$("input[name=searchKbn]").val("searchTo04");

		break;
	case 'route':
		$("input[name=val_htmb]").val("cgi_result2");
		$("input[name=val_from]").val($("input[name=from]").val());
		$("input[name=val_to01]").val($("input[name=to01]").val());
		$("input[name=val_to02]").val($("input[name=to02]").val());
		$("input[name=val_to03]").val($("input[name=to03]").val());
		$("input[name=val_to04]").val($("input[name=to04]").val());
		$("input[name=searchKbn]").val("searchRoute");
		$("input[name=val_icticket]").val($("#val_icticket_rdo1").prop("checked") ? "1" : "0");
		$("input[name=val_shinkansen]").val($("input[name=val_shinkansenChk]").prop("checked") ? "1" : "0");
		$("input[name=val_nozomi]").val($("input[name=val_nozomiChk]").prop("checked") ? "1" : "0");
		$("input[name=val_expressonly]").val($("input[name=val_expressonlyChk]").prop("checked") ? "1" : "0");
		//20220217 追加
		$("input[name=val_oneway]").val($("input[name=oufukuEki]").prop("checked") ? "0" : "1");

		var date = $('input[name=norikaeKaishiBi]').val();
		if (date != null) {

			var aryDate = date.split("/");

			if (aryDate.length != 3) {
				//省略したら駅すぱあと側で本日日付としている
			} else {
				$("input[name=val_year]").val(aryDate[0]);
				$("input[name=val_month]").val(aryDate[1]);
				$("input[name=val_day]").val(aryDate[2]);
			}
		}
		break;
	case 'teiki':
		$("input[name=val_htmb]").val("cgi_result2");
		$("input[name=val_from]").val($("input[name=from]").val());
		$("input[name=val_to01]").val($("input[name=to01]").val());
		$("input[name=val_to02]").val($("input[name=to02]").val());
		$("input[name=val_to03]").val($("input[name=to03]").val());
		$("input[name=val_to04]").val($("input[name=to04]").val());
		$("input[name=searchKbn]").val("searchRoute");
		$("input[name=val_icticket]").val($("#val_icticket_rdo1").prop("checked") ? "1" : "0");
		$("input[name=val_shinkansen]").val($("input[name=val_shinkansenChk]").prop("checked") ? "1" : "0");
		$("input[name=val_nozomi]").val($("input[name=val_nozomiChk]").prop("checked") ? "1" : "0");
		$("input[name=val_expressonly]").val($("input[name=val_expressonlyChk]").prop("checked") ? "1" : "0");

		var date = $('input[name=norikaeKaishiBi]').val();
		if (date != null) {

			var aryDate = date.split("/");

			if (aryDate.length != 3) {
				//省略したら駅すぱあと側で本日日付としている
			} else {
				$("input[name=val_year]").val(aryDate[0]);
				$("input[name=val_month]").val(aryDate[1]);
				$("input[name=val_day]").val(aryDate[2]);
			}
		}
		break;
	default:
	}

	/*
	alert(
			  "val_htmb:" + "[" + $("[name=val_htmb]").val() + "]\n"
			+ "val_cgi_url:" + "[" + $("[name=val_cgi_url]").val() + "]\n"
			+ "val_in_name:" + "[" + $("[name=val_in_name]").val() + "]\n"
			+ "val_oneway:" + "[" + $("[name=val_oneway]").val() + "]\n"
			+ "val_shinkansen:" + "[" + $("[name=val_shinkansen]").val() + "]\n"
			+ "val_nozomi:" + "[" + $("[name=val_nozomi]").val() + "]\n"
			+ "val_expressonly:" + "[" + $("[name=val_expressonly]").val() + "]\n"
			+ "val_sleepingcar:" + "[" + $("[name=val_sleepingcar]:checked").val() + "]\n"
			+ "val_highwaybus:" + "[" + $("[name=val_highwaybus]:checked").val() + "]\n"
			+ "val_airbus:" + "[" + $("[name=val_airbus]:checked").val() + "]\n"
			+ "val_ship:" + "[" + $("[name=val_ship]:checked").val() + "]\n"
			+ "val_icticket:" + "[" + $("[name=val_icticket]").val() + "]\n"
			+ "val_upperparam:" + "[" + $("[name=val_upperparam]").val() + "]\n"
			+ "val_year:" + "[" + $("[name=val_year]").val() + "]\n"
			+ "val_month:" + "[" + $("[name=val_month]").val() + "]\n"
			+ "val_day:" + "[" + $("[name=val_day]").val() + "]\n"
			+ "val_from:" + "[" + $("[name=val_from]").val() + "]\n"
			+ "val_to01:" + "[" + $("[name=val_to01]").val() + "]\n"
			+ "val_to02:" + "[" + $("[name=val_to02]").val() + "]\n"
			+ "val_to03:" + "[" + $("[name=val_to03]").val() + "]\n"
			+ "val_to04:" + "[" + $("[name=val_to04]").val() + "]\n");
	alert(
			  "val_max_result:" + "[" + $("[name=val_max_result]").val() + "]\n"
			+ "val_sorttype:" + "[" + $("[name=val_sorttype]").val() + "]\n"
			+ "val_tassign_reflect:" + "[" + $("[name=val_tassign_reflect]").val() + "]\n"
			+ "val_tassignmode:" + "[" + $("[name=val_tassignmode]").val() + "]\n"
			+ "val_tassign_stationnamelist:" + "[" + $("[name=val_tassign_stationnamelist]").val() + "]\n"
			+ "val_tassign_railnamelist:" + "[" + $("[name=val_tassign_railnamelist]").val() + "]\n"
			+ "searchKbn:" + "[" + $("[name=searchKbn]").val() + "]\n"
			+ "norikaeKaishiBi:" + "[" + $("[name=norikaeKaishiBi]").val() + "]\n"
			+ "kikanKbn:" + "[" + $("[name=kikanKbn]").val() + "]\n"
			+ "jousyaKukan:" + "[" + $("[name=jousyaKukan]").val() + "]\n"
			+ "norikaeKingaku:" + "[" + $("[name=norikaeKingaku]").val() + "]\n"
			+ "tassign_status:" + "[" + $("[name=tassign_status]").val() + "]\n"
			);
	*/

	$(form).submit();

	try {
		// キャラセットをUTF-8に戻す
		document.charset = org;
		document.characterSet = orgCharacterSet;
		form.acceptCharset = orgForm;
	}catch(e){}
}


//選択経路反映処理(イントラ版)
function setRouteIntra(){

	//イントラ版の経路簡易表示オプションによる制御はEteamEkispertCommonIntra側で実施

	var strRoute = "";
	var strKingaku = "";
	var strKyori = "";

	var strWaribiki = "";
	var strBikou = "";

	strRoute = $('input[name=jousyaKukan]').val();
	strKingaku = $('input[name=norikaeKingaku]').val();
	strKyori = $('input[name=idouKyori]').val();
	strWaribiki = $('input[name=waribikiName]').val();

	//経路情報がない(イントラ版画面側でのキャンセル等)なら何もしない
	if(strRoute == '')return;

	var denpyouKbn = $("input[name=denpyouKbn]").val();

	//距離情報を反映
	//ただし定期の入力なら反映させない
	if (denpyouKbn != 'A006') {
		if( "${setting.ekispertKyoriHanei()}" == '1' ){
			//内容
			strRoute = strRoute + "　" + strKyori;
		}else if( "${setting.ekispertKyoriHanei()}" == '2' ){
			//備考
			/*if(getBikouField() != null){
				getBikouField().val(strKyori);
			}*/
			strBikou = strBikou + strKyori;
		}
	}

	//増税未対応反映
	if($('input[name=taxNonSupported]').val() == "1"){
		strRoute = strRoute + "\r\n" + "※運賃改定に未対応の金額を含みます。"
	}else if($('input[name=taxNonSupported]').val() == "2"){
		strRoute = strRoute + "\r\n" + "※「駅すぱあと」が予測した運賃改定後の見込の金額を含みます。"
	}

	//定期区間情報反映
	//※定期区間との重複区間が無い場合は出力させない
	if($("input[name=tassign_status]").val() == '0'){
		var strTeikikukan = "【定期区間】"+$("#passRoute").val();

		if( "${setting.ekispertTeikikukanHaneisaki()}" == '1' ){
			//1:内容
			strRoute = strRoute + "\r\n" + strTeikikukan;
		}else{
			//2:備考
			if(getBikouField() != null){
				if( "${setting.ekispertKyoriHanei()}" != '2' ){
					getBikouField().val(strTeikikukan);
				}else{
					getBikouField().val(strKyori + "　" + strTeikikukan);
				}
			}
		}
	}

	//指定乗車区間情報の反映
	getRouteField().val(strRoute);
	var dtfmt = $("input[name=norikaeKaishiBi]").val();
	if (denpyouKbn == 'A006') {
		// 通勤定期申請
		var kikan = Number($("input[name=shiyouKikanKbn]:checked").val());
		getKingakuField().val(String(strKingaku).formatMoney());
		$("input[name=shiyouKaishiBi]").val(dtfmt);
		$("input[name=tenyuuryokuFlg]").val("0");
		if (typeof(changeEnableKukanKingaku) == 'function'){
			changeEnableKukanKingaku();
			shiyouShuuryouHenkou();
		}else{
			callShiyouShuuryouHenkou();
		}
		isDirty = true;
	}else{
		//20220217 追加
		var isRound = $("input[name=oufukuEki]").prop("checked");
		// 交通費明細
		if(isRound){
			var hangaku = parseInt(strKingaku.replace(/,/, '')) / 2;
			getKingakuField().val(String(hangaku).formatMoney());
		}else{
			getKingakuField().val(String(strKingaku).formatMoney());
		}
		// 交通費明細
		//getKingakuField().val(String(strKingaku).formatMoney());
		$("#dialogMainDenpyouMeisai").find("input[name=shubetsu1]").val("交通費");
		$("#dialogMainDenpyouMeisai").find("span.shubetsu1").text($("#dialogMainDenpyouMeisai").find("input[name=shubetsu1]").val());
		$("#dialogMainDenpyouMeisai").find("input[name=jidounyuuryokuFlg]").val("1");
		$("#dialogMainDenpyouMeisai").find("input[name=shubetsu2]").val("自動入力");
		$("#dialogMainDenpyouMeisai").find("span.shubetsu2").text($("#dialogMainDenpyouMeisai").find("input[name=shubetsu2]").val());
		$("#dialogMainDenpyouMeisai").find("input[name=kikanFrom]").val(dtfmt);
		$("#dialogMainDenpyouMeisai").find("input[name=ryoushuushoSeikyuushoTouFlg]").prop("checked", false);
		$("#dialogMainDenpyouMeisai").find("input[name=shouhyouShoruiHissuFlg]").val("0");

		//20220218 ここにEX予約のことはいる
		if( "${setting.ekispertWaribikiHanei()}" == '1' ){
			//内容
			strRoute = strRoute + "　" + strWaribiki;
			getRouteField().val(strRoute);
		}else if( "${setting.ekispertWaribikiHanei()}" == '2' ){
			//備考
			if(strBikou.length > 0){
				strBikou = strBikou + "　";
			}
			strBikou = strBikou + strWaribiki;
		}
		if(getBikouField() != null){
			getBikouField().val(strBikou);
		}

		$("#dialogMainDenpyouMeisai").find("input[name=oufukuFlg]").prop("checked", isRound);
		if(isRound){
			//$("#dialogMainDenpyouMeisai").find("input[name=oufukuFlg]").prop("checked", true);
			//$("#dialogMainDenpyouMeisai").find("[name=oufukuFlg]").prop("disabled", true);
			//20220316 種別2で往復チェックボックスのDisable制御
			$("#dialogMainDenpyouMeisai").find("input[name=shubetsu2]").val("自動 往復");
			$("#dialogMainDenpyouMeisai").find("span.shubetsu2").text($("#dialogMainDenpyouMeisai").find("input[name=shubetsu2]").val());
		}

		inputSeigyo();
	}

	$("#dialog").children().remove();
	$("#dialog").dialog('close');
}

/**
 * 伝票毎の日付フィールドから日付を取得する
 */
function getDenpyouDate() {
	var denpyouKbn = $("input[name=denpyouKbn]").val();

	var date = "";
	// 通勤定期申請
	if (denpyouKbn == 'A006') {
		date = $('input[name=shiyouKaishiBi]').val();
	//交通費明細
	}else{
		date = $("#dialogMainDenpyouMeisai").find('input[name=kikanFrom]').val();
	}
	//本日日付デフォルト
	if(date == null || date == ""){
		date = todayYMD();
	}

	return date;
}

/**
 * 伝票毎の乗車区間フィールドを返す
 */
function getRouteField() {
	var denpyouKbn = $("input[name=denpyouKbn]").val();

	// 通勤定期申請
	if (denpyouKbn == 'A006') {
		route = $('textarea[name=jyoushaKukan]');
	}

	// 旅費精算
	if (denpyouKbn == 'A004') {
		route = $('textarea[name=naiyou]');
	}

	// 旅費仮払申請
	if (denpyouKbn == 'A005') {
		route = $('textarea[name=naiyou]');
	}

	// 海外旅費精算
	if (denpyouKbn == 'A011') {
		route = $('textarea[name=naiyou]');
	}

	// 海外旅費仮払申請
	if (denpyouKbn == 'A012') {
		route = $('textarea[name=naiyou]');
	}

	// 交通費精算
	if (denpyouKbn == 'A010') {
		route = $('textarea[name=naiyou]');
	}

	return route;
}

/**
 * 伝票毎の備考フィールドを返す
 */
function getBikouField() {
	var denpyouKbn = $("input[name=denpyouKbn]").val();

	// 通勤定期申請
	if (denpyouKbn == 'A006') {
		bikou = null;
	}

	// 旅費精算
	if (denpyouKbn == 'A004') {
		bikou = $('.meisaiBikouObj');
	}

	// 旅費仮払申請
	if (denpyouKbn == 'A005') {
		bikou = $('.meisaiBikouObj');
	}

	// 海外旅費精算
	if (denpyouKbn == 'A011') {
		bikou = $('.meisaiBikouObj');
	}

	// 海外旅費仮払申請
	if (denpyouKbn == 'A012') {
		bikou = $('.meisaiBikouObj');
	}

	// 交通費精算
	if (denpyouKbn == 'A010') {
		bikou = $('.meisaiBikouObj');
	}

	return bikou;
}

/**
 * 伝票毎の金額フィールドを返す
 */
function getKingakuField() {
	var denpyouKbn = $("input[name=denpyouKbn]").val();

	// 通勤定期申請
	if (denpyouKbn == 'A006') {
		money = $('input[name=kingaku]');
	}

	// 旅費精算
	if (denpyouKbn == 'A004') {
		money = $('.meisaiTankaObj');
	}

	// 旅費仮払申請
	if (denpyouKbn == 'A005') {
		money = $('.meisaiTankaObj');
	}

	// 海外旅費精算
	if (denpyouKbn == 'A011') {
		money = $('.meisaiTankaObj');
	}

	// 海外旅費仮払申請
	if (denpyouKbn == 'A012') {
		money = $('.meisaiTankaObj');
	}

	// 交通費精算
	if (denpyouKbn == 'A010') {
		money = $('.meisaiTankaObj');
	}

	return money;
}
</script>
