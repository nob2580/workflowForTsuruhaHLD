<%@ page import="eteam.symbol.EteamSymbol"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!DOCTYPE html>
<html lang='ja'>
	<head>
		<meta charset='utf-8'>
		<title>部門別取引（仕訳）一覧｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
<!--
-->
		</style>
	</head>
	<body>
		<div id='wrap'>
			<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>
			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
				<h1>部門別取引（仕訳）一覧</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				
				<!-- メイン -->
				<div id='main'><form id='inputFieldForm' name='inputFieldForm' class='form-horizontal'>
					<input type='hidden' id='shozokuBumonCd' name='shozokuBumonCd' value='${su:htmlEscape(shozokuBumonCd)}'>
					<input type='hidden' id='kijunBi' name='kijunBi' value='${su:htmlEscape(kijunBi)}'>
					<input type='hidden' id='denpyouKbn' name='denpyouKbn' value='${su:htmlEscape(denpyouKbn)}'>
					<input type='hidden' id='shiwakeEdano' name='shiwakeEdano' value='${su:htmlEscape(shiwakeEdano)}'>
					<input type='hidden' id='torihikiNm' name='torihikiNm' value='${su:htmlEscape(torihikiNm)}'>
					<input type='hidden' id='tourokuStatus' name='tourokuStatus' value='${su:htmlEscape(tourokuStatus)}'>
					<input type='hidden' id='sortItem' name='sortItem' value='${su:htmlEscape(sortItem)}'>
					<input type='hidden' id='sortOrder' name='sortOrder' value='${su:htmlEscape(sortOrder)}'>
					<input type='hidden' id='preEventUrl' name='preEventUrl' value='${su:htmlEscape(preEventUrl)}'>

					<!-- 検索条件 -->
					<section id='searchCondition'>
						<h2>検索条件</h2>
						<div class='control-group'>
							<label class='control-label'><span class='required'>*</span>所属部門</label>
							<div class='controls'>
								<input type='text' id='searchShozokuBumonCd' name='searchShozokuBumonCd' class='input-inline input-small' readonly="readonly" value='${su:htmlEscape(searchShozokuBumonCd)}'>
								<input type='text' id='searchShozokuBumonNm' name='searchShozokuBumonNm' class='input-inline input-medium' readonly="readonly" value='${su:htmlEscape(searchShozokuBumonNm)}'>
								<button type='button' id='searchShozokuBumonSentaku' name='searchShozokuBumonSentaku' class='btn btn-small bumonSearch input-inline'>選択</button>
							</div>
						</div>
						<div class='control-group'>
							<label class='control-label'><span class='required'>*</span>伝票種別</label>
							<div class='controls'>
								<select id='searchDenpyouShubetsu' name='searchDenpyouShubetsu' class='input-large'>
									<option></option>
									<c:forEach var='denpyouShubetsuList' items='${denpyouShubetsuList}'>
										<option value='${su:htmlEscape(denpyouShubetsuList.naibu_cd)}' <c:if test='${denpyouShubetsuList.naibu_cd eq searchDenpyouShubetsu}'>selected</c:if>>${su:htmlEscape(denpyouShubetsuList.name)}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class='control-group'>
							<label class='control-label'>取引名</label>
							<div class='controls'>
								<input type='text' id='searchTorihikiNm' name='searchTorihikiNm' class='input-inline' maxlength="20" value='${su:htmlEscape(searchTorihikiNm)}'>
							</div>
						</div>
						<div class='control-group'>
							<label class='control-label'><span class='required'>*</span>登録状態</label>
							<div class='controls'>
								<select id='searchTourokuJyoutai' name='searchTourokuJyoutai' class='input-small'>
									<c:forEach var='tourokuJyoutaiList' items='${tourokuJyoutaiList}'>
										<option value='${su:htmlEscape(tourokuJyoutaiList.naibu_cd)}' <c:if test='${tourokuJyoutaiList.naibu_cd eq searchTourokuJyoutai}'>selected</c:if>>${su:htmlEscape(tourokuJyoutaiList.name)}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div>
							<button type='button' id='kensakuButton' class='btn' ><i class='icon-search'></i> 検索実行</button>
							<button type='button' id='clearButton' class='btn'><i class='icon-remove-circle'></i> 条件クリア</button>
						</div>
					</section>

					<!-- 検索結果 -->
					<section id='searchResult'>
						<h2>検索結果</h2>
<c:if test='${"0" != initHyoujiFlag}'>
						<div>
							<button type='button' id='koushinButton' class='btn'><i class='icon-hdd'></i> 更新</button>
						</div>
						<div class="blank"></div>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th style='width: 45px'>全選択<br>
											<input type='checkbox' id='allcheck' >
										</th>
										<th style='width: 130px'>伝票種別</th>
										<th style='width: 130px'>分類1<br>
											<a id='headerBunrui1Asc' class='link' data-sort='headerBunrui1Asc'>▲</a>
											<a id='headerBunrui1Desc' class='link' data-sort='headerBunrui1Desc'>▼</a>
										</th>
										<th style='width: 130px'>分類2<br>
											<a id='headerBunrui2Asc' class='link' data-sort='headerBunrui2Asc'>▲</a>
											<a id='headerBunrui2Desc' class='link' data-sort='headerBunrui2Desc'>▼</a>
										</th>
										<th style='width: 130px'>分類3<br>
											<a id='headerBunrui3Asc' class='link' data-sort='headerBunrui3Asc'>▲</a>
											<a id='headerBunrui3Desc' class='link' data-sort='headerBunrui3Desc'>▼</a>
										</th>
										<th style='width: 160px'>取引名<br>
											<a id='headerTorihikiNmAsc' class='link' data-sort='headerTorihikiNmAsc'>▲</a>
											<a id='headerTorihikiNmDesc' class='link' data-sort='headerTorihikiNmDesc'>▼</a>
										</th>
										<th style='width: 180px'>借方科目<br>
											<a id='headerKarikataKamokuAsc' class='link' data-sort='headerKarikataKamokuAsc'>▲</a>
											<a id='headerKarikataKamokuDesc' class='link' data-sort='headerKarikataKamokuDesc'>▼</a>
										</th>		
										<th style='width: 110px'>デフォルト表示<br>
											<a id='headerDefaultHyoujiAsc' class='link' data-sort='headerDefaultHyoujiAsc'>▲</a>
											<a id='headerDefaultHyoujiDesc' class='link' data-sort='headerDefaultHyoujiDesc'>▼</a>
										</th>
										<th style='width: 35px'>　</th>
									</tr>
								</thead>
								<tbody id='meisaiList'>
								<c:forEach var='record' items='${kensakuKekkaList}' varStatus="st">
									<tr class='${record.bg_color}' data-edano='${su:htmlEscape(record.shiwake_edano)}'>
										<td align="center">
											<input type='checkbox' name='listSentaku' value='${su:htmlEscape(record.shiwake_edano)}' <c:if test='${"1" eq record.touroku_status}'>checked</c:if>>
										</td>
										<td>${su:htmlEscape(record.name)}</td>
										<td>${su:htmlEscape(record.bunrui1)}</td>
										<td>${su:htmlEscape(record.bunrui2)}</td>
										<td>${su:htmlEscape(record.bunrui3)}</td>
										<td>${su:htmlEscape(record.torihiki_name)}</td>
										<td>${su:htmlEscape(record.kari_kamoku_cd)}：${su:htmlEscape(record.kari_kamoku_name)}</td>
										<td>
											<c:if test='${"1" eq record.default_hyouji_flg}'>表示あり</c:if>
											<c:if test='${"0" eq record.default_hyouji_flg}'>表示なし</c:if>
										</td>
										<td><button type='button' id='listHenkouButton_${st.index}' class='btn btn-mini listHenkouButton'>変更</button> </td>
									</tr>
								</c:forEach>
								</tbody>
							</table>
						</div>
</c:if>
					</section>
				</form></div><!-- main -->
				<!-- Modal -->
				<div id='dialog'></div>
			</div><!-- /container -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>		
		<script style='text/javascript'>

$(document).ready(function(){

	//所属部門選択ボタン押下時Function
	$("#searchShozokuBumonSentaku").click(function(e){
		dialogRetBumonCd = $("input[name=searchShozokuBumonCd]");
		dialogRetBumonName = $("input[name=searchShozokuBumonNm]");
		commonBumonSentaku();
	});

	//検索
	$("#kensakuButton").click(function(e){
		$("#shozokuBumonCd").val($("#searchShozokuBumonCd").val());
		$("#denpyouKbn").val($("#searchDenpyouShubetsu").val());
		$("#torihikiNm").val($("#searchTorihikiNm").val().trim());
		$("#tourokuStatus").val($("#searchTourokuJyoutai").val());
		$("#sortItem").val("hyouji_jun");
		$("#sortOrder").val("ASC");
		$("form[name=inputFieldForm]").attr("action", "bumonbetsu_torihiki_ichiran_kensaku");
		$("form[name=inputFieldForm]").attr("method", "get");
		$("form[name=inputFieldForm]").submit();
	});

	//登録
	$("#koushinButton").click(function(e){
		var edaban="";
		$("input[name='listSentaku']").each(function() {
            if($(this).is(':checked')){
            	edaban += $(this).val()+",";
            }
        });

		$("#shiwakeEdano").val(edaban);
		$("form[name=inputFieldForm]").attr("action", "bumonbetsu_torihiki_ichiran_touroku");
		$("form[name=inputFieldForm]").attr("method", "post");
		$("form[name=inputFieldForm]").submit();
	});
	
	//変更
	$(".listHenkouButton").click(function(e){
		$("#shiwakeEdano").val($(e.target).closest('tr').data('edano'));
		$("form[name=inputFieldForm]").attr("action", "torihiki_henkou");
		$("form[name=inputFieldForm]").attr("method", "get");
		$("form[name=inputFieldForm]").submit();
	});

	// 全選択・全非選択
	$('#allcheck').click(function(e){
		$("input[type='checkbox']").prop('checked', $('#allcheck').prop('checked'));
	});

	//条件クリアボタン押下時
	$("#clearButton").click(function(e){
		inputClear($("#searchCondition"));
	});

	//ソート
	$(".link").click(function(e){
		
		//分類１
		if ($(e.target).closest('a').data('sort') == 'headerBunrui1Asc') {
			$("#sortItem").val('bunrui1');
			$("#sortOrder").val('ASC');
		} else if ($(e.target).closest('a').data('sort') == 'headerBunrui1Desc') {
			$("#sortItem").val('bunrui1');
			$("#sortOrder").val('DESC');
		
		//分類２
		} else if ($(e.target).closest('a').data('sort') == 'headerBunrui2Asc') {
			$("#sortItem").val('bunrui2');
			$("#sortOrder").val('ASC');
		} else if ($(e.target).closest('a').data('sort') == 'headerBunrui2Desc') {
			$("#sortItem").val('bunrui2');
			$("#sortOrder").val('DESC');
		
		//分類３
		} else if ($(e.target).closest('a').data('sort') == 'headerBunrui3Asc') {
			$("#sortItem").val('bunrui3');
			$("#sortOrder").val('ASC');
		} else if ($(e.target).closest('a').data('sort') == 'headerBunrui3Desc') {
			$("#sortItem").val('bunrui3');
			$("#sortOrder").val('DESC');
			
		//取引名
		} else if ($(e.target).closest('a').data('sort') == 'headerTorihikiNmAsc') {
			$("#sortItem").val('torihiki_name');
			$("#sortOrder").val('ASC');
		} else if ($(e.target).closest('a').data('sort') == 'headerTorihikiNmDesc') {
			$("#sortItem").val('torihiki_name');
			$("#sortOrder").val('DESC');
			
		//借方科目
		} else if ($(e.target).closest('a').data('sort') == 'headerKarikataKamokuAsc') {
			$("#sortItem").val('kari_kamoku_cd');
			$("#sortOrder").val('ASC');
		} else if ($(e.target).closest('a').data('sort') == 'headerKarikataKamokuDesc') {
			$("#sortItem").val('kari_kamoku_cd');
			$("#sortOrder").val('DESC');
			
		//デフォルト表示
		} else if ($(e.target).closest('a').data('sort') == 'headerDefaultHyoujiAsc') {
			$("#sortItem").val('default_hyouji_flg');
			$("#sortOrder").val('ASC');
		} else if ($(e.target).closest('a').data('sort') == 'headerDefaultHyoujiDesc') {
			$("#sortItem").val('default_hyouji_flg');
			$("#sortOrder").val('DESC');
		}

		$("form[name=inputFieldForm]").attr("action", "bumonbetsu_torihiki_ichiran_kensaku");
		$("form[name=inputFieldForm]").attr("method", "get");
		$("form[name=inputFieldForm]").submit();
	});
	
	//所属部門選択子画面で入力した基準日の値を取得する
	dialogRetBumonSentakuCallback = function(a){
		var kijunbi = $("input[name=d_kijunBi]").val();
		$("#kijunBi").val(kijunbi);
		var title = a.closest("#dialog").parent().find(".ui-dialog-title").text();
		dialogRetBumonCd.val(a.attr("data-cd"));
		commonBumonCdLostFocus(true, dialogRetBumonCd, dialogRetBumonName, title);
	}
	
});
		</script>
	</body>
</html>
