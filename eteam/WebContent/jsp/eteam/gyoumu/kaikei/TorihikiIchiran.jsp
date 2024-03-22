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
 		<title>取引（仕訳）一覧｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>取引（仕訳）一覧</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				
				<!-- メイン -->
				<div id='main'>
					<form id='inputFieldForm' name='inputFieldForm' class='form-horizontal'>
						<!-- 入力フィールド -->
						<section class='print-unit'>
							<h2>検索条件</h2>
							<div>
								<div class='control-group'>
									<label class='control-label'><span class='required'>*</span>伝票種別</label>
									<div class='controls'>
										<select id='searchDenpyouShubetsu' name='searchDenpyouShubetsu' class='input-large'>
											<option value=''></option>
											<c:forEach var='denpyouShubetsuList' items='${infoDenpyouShubetsuList}'>
												<option value='${su:htmlEscape(denpyouShubetsuList.naibu_cd)}' data-shiwakeAddFlg='${denpyouShubetsuList.option1}' <c:if test='${denpyouShubetsuList.naibu_cd eq searchDenpyouShubetsu}'>selected</c:if>>${su:htmlEscape(denpyouShubetsuList.name)}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="form-horizontal">
									<div class='control-group'>
										<label class="control-label">分類１</label>
										<div class='controls'>
											<select id='searchBunrui1'></select>
											<label class="label">分類２</label>
											<select id='searchBunrui2'></select>
											<label class="label">分類３</label>
											<select id='searchBunrui3'></select>
										</div>
									</div>
									<div class='control-group'>
										<label class="control-label">キーワード</label>
										<div class='controls'>
											<input type="text" id='keyword'>
										</div>
									</div>
								</div>
								<div>
									<button type='button' class='btn' id='clearButton'><i class='icon-remove-circle'></i> 条件クリア</button>
									<button type='button' id='tsuikaButton' class='btn'><i class='icon-plus'></i> 追加</button>
								</div>
							</div>
						</section>
					</form>
						
					<section>
						<h2>検索結果</h2>
						<div class='blank'></div>
						
						<c:if test='${"0" != initHyoujiFlag}'>
						<%-- <c:if test='${fn:length(torihikiList) > 0}' > --%>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>#</th>
										<th>有効期限<br>
											<a id='headerYuukouKigenAsc' class='link' data-sort='headerYuukouKigenAsc'>▲</a>
											<a id='headerYuukouKigenDesc' class='link' data-sort='headerYuukouKigenDesc'>▼</a>
										</th>
										<th style='width: 120px'>分類1<br>
											<a id='headerBunrui1Asc' class='link' data-sort='headerBunrui1Asc'>▲</a>
											<a id='headerBunrui1Desc' class='link' data-sort='headerBunrui1Desc'>▼</a>
										</th>
										<th style='width: 120px'>分類2<br>
											<a id='headerBunrui2Asc' class='link' data-sort='headerBunrui2Asc'>▲</a>
											<a id='headerBunrui2Desc' class='link' data-sort='headerBunrui2Desc'>▼</a>
										</th>
										<th style='width: 120px'>分類3<br>
											<a id='headerBunrui3Asc' class='link' data-sort='headerBunrui3Asc'>▲</a>
											<a id='headerBunrui3Desc' class='link' data-sort='headerBunrui3Desc'>▼</a>
										</th>
										<th style='width: 150px'>取引名<br>
											<a id='headerTorihikiNmAsc' class='link' data-sort='headerTorihikiNmAsc'>▲</a>
											<a id='headerTorihikiNmDesc' class='link' data-sort='headerTorihikiNmDesc'>▼</a>
										</th>
										<th style='width: 195px'>借方科目<br>
											<a id='headerKarikataKamokuAsc' class='link' data-sort='headerKarikataKamokuAsc'>▲</a>
											<a id='headerKarikataKamokuDesc' class='link' data-sort='headerKarikataKamokuDesc'>▼</a>
										</th>
										<th style='width: 195px'>貸方科目<br>
											<a id='headerKashikataKamokuAsc' class='link' data-sort='headerKashikataKamokuAsc'>▲</a>
											<a id='headerKashikataKamokuDesc' class='link' data-sort='headerKashikataKamokuDesc'>▼</a>
										</th>
										<th>　</th>
									</tr>
								</thead>
								<tbody id='torihikiIchiranBody'>
									<c:forEach var='record' items='${torihikiList}' varStatus="st">
									<tr class='${record.bg_color}' data-edano='${su:htmlEscape(record.shiwake_edano)}'>
										<td>${su:htmlEscape(record.shiwake_edano)}</td>
										<td>${su:htmlEscape(record.yuukou_kigen_from)}-<br>${su:htmlEscape(record.yuukou_kigen_to)}</td>
										<td class='displayBunrui1'>${su:htmlEscape(record.bunrui1)}</td>
										<td class='displayBunrui2'>${su:htmlEscape(record.bunrui2)}</td>
										<td class='displayBunrui3'>${su:htmlEscape(record.bunrui3)}</td>
										<td class='torihikiName'>${su:htmlEscape(record.torihiki_name)}</td>
										<td class='kariKamokuName'>${su:htmlEscape(record.kari_kamoku_cd)}：${su:htmlEscape(record.kari_kamoku_name)}</td>
										<td class='kashiKamokuName'>${su:htmlEscapeBr(record.kasi_kamoku_disp)}</td>
										<td>
											<button type='button'id='listHenkouButton_${st.index}' class='btn btn-mini listHenkouButton'>変更</button><br>
											<button type='button'id='listSakujyoButton_${st.index}' class='btn btn-mini listSakujyoButton' style='display:none;'>削除</button>
										</td>
									</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</c:if>
					</section>

					<!-- Modal -->
					<div id='dialog'></div>
		
					<form name='jyoukenForm'>
						<input type='hidden' id='denpyouKbn' name='denpyouKbn' value='${su:htmlEscape(denpyouKbn)}'>
						<input type='hidden' id='shiwakeEdano' name='shiwakeEdano' value='${su:htmlEscape(shiwakeEdano)}'>
						<input type='hidden' id='sortItem' name='sortItem' value='${su:htmlEscape(sortItem)}'>
						<input type='hidden' id='sortOrder' name='sortOrder' value='${su:htmlEscape(sortOrder)}'>
						<input type='hidden' id='bunrui1Val' name='bunrui1Val' value='${su:htmlEscape(bunrui1Val)}'>
						<input type='hidden' id='bunrui2Val' name='bunrui2Val' value='${su:htmlEscape(bunrui2Val)}'>
						<input type='hidden' id='bunrui3Val' name='bunrui3Val' value='${su:htmlEscape(bunrui3Val)}'>
						<input type='hidden' id='keywordVal' name='keywordVal' value='${su:htmlEscape(keywordVal)}'>
						<input type='hidden' id='preEventUrl' name='preEventUrl' value='${su:htmlEscape(preEventUrl)}'>
					</form>
				</div><!-- main -->
			</div><!-- /container -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>
<!--
		
$(document).ready(function(){
	
	//初期表示でカテゴリ１作成
	$("#searchBunrui1").append($("<option></option>"));
	var c1List = new Array();
	$("#torihikiIchiranBody").find("tr").each(function(){
		var tmpC1 = $(this).find(".displayBunrui1").text();
		if ("" != jQuery.trim(tmpC1) && -1 == $.inArray(tmpC1, c1List)) c1List.push(tmpC1);
	});
	for (var i = 0; i < c1List.length; i++) {
		var opt = $("<option></option>");
		opt.text(c1List[i]);
		$("#searchBunrui1").append(opt);
	}

	/**
	 * カテゴリ１選択時
	 */
	$("#searchBunrui1").change(c1Change);
	function c1Change() {
		var c1 = $("#searchBunrui1").children("option:selected").text();

		//C2C3空にする
		$("#searchBunrui2").empty();
		$("#searchBunrui3").empty();

		//検索
		torihikiSentakuSearch();

		//C2作成
		$("#searchBunrui2").append($("<option></option>"));
		var c2List = new Array();
		$("#torihikiIchiranBody").find("tr").each(function(){
			var tmpC1 = $(this).find(".displayBunrui1").text();
			var tmpC2 = $(this).find(".displayBunrui2").text();
			if (c1 == tmpC1 && "" != jQuery.trim(tmpC2) && -1 == $.inArray(tmpC2, c2List)) c2List.push(tmpC2);
		});
		for (var i = 0; i < c2List.length; i++) {
			var opt = $("<option></option>");
			opt.text(c2List[i]);
			$("#searchBunrui2").append(opt);
		}	
	}

	/**
	 * カテゴリ２選択時
	 */
	$("#searchBunrui2").change(function(){
		var c1 = $("#searchBunrui1").children("option:selected").text();
		var c2 = $("#searchBunrui2").children("option:selected").text();

		//C3空にする
		$("#searchBunrui3").empty();

		//検索
		torihikiSentakuSearch();

		//C3作成
		$("#searchBunrui3").append($("<option></option>"));
		var c3List = new Array();
		$("#torihikiIchiranBody").find("tr").each(function(){
			var tmpC1 = $(this).find(".displayBunrui1").text();
			var tmpC2 = $(this).find(".displayBunrui2").text();
			var tmpC3 = $(this).find(".displayBunrui3").text();
			if (c1 == tmpC1 && c2 == tmpC2 && "" != jQuery.trim(tmpC3) && -1 == $.inArray(tmpC3, c3List)) c3List.push(tmpC3);
		});
		for (var i = 0; i < c3List.length; i++) {
			var opt = $("<option></option>");
			opt.text(c3List[i]);
			$("#searchBunrui3").append(opt);
		}
	});

	/**
	 * カテゴリ３選択時
	 */
	$("#searchBunrui3").change(function(){

		//検索
		torihikiSentakuSearch();
	});

	/**
	 * 検索ワード変更時
	 */
	$("#keyword").change(function(){

		//検索
		torihikiSentakuSearch();
	});

	/**
	 * 分類とテキストで絞り込む
	 */
	function torihikiSentakuSearch() {
		var c1 = $("#searchBunrui1").children("option:selected");
		c1 = (1 == c1.length) ? c1.text() : "";
		var c2 = $("#searchBunrui2").children("option:selected");
		c2 = (1 == c2.length) ? c2.text() : "";
		var c3 = $("#searchBunrui3").children("option:selected");
		c3 = (1 == c3.length) ? c3.text() : "";
		var word = $("#keyword").val();
		var excludedSpaceWord = word.zenkana2hankanaWithhyphen().replace(/\s+/g, "");

		//該当するものだけ表示
		$("#torihikiIchiranBody").find("tr").css("display", "none");
		$("#torihikiIchiranBody").find("tr").each(function(){
			if (
				("" == c1 || c1 == $(this).find(".displayBunrui1").text()) &&
				("" == c2 || c2 == $(this).find(".displayBunrui2").text()) &&
				("" == c3 || c3 == $(this).find(".displayBunrui3").text()) &&
				("" == word || (
					-1 < excludedSpace($(this), ".displayBunrui1").indexOf(excludedSpaceWord) ||
					-1 < excludedSpace($(this), ".displayBunrui2").indexOf(excludedSpaceWord) ||
					-1 < excludedSpace($(this), ".displayBunrui3").indexOf(excludedSpaceWord) ||
					-1 < excludedSpace($(this), ".torihikiName").indexOf(excludedSpaceWord) ||
					-1 < excludedSpace($(this), ".kariKamokuName").indexOf(excludedSpaceWord) ||
					-1 < excludedSpace($(this), ".kashiKamokuName").indexOf(excludedSpaceWord)
				))
			) {
				$(this).css("display", "");
			}
		});
		
	};

	/**
	 * 空白を除去した文章を返す
	 */
	function excludedSpace(t, str) {
		return t.find(str).text().zenkana2hankanaWithhyphen().replace(/\s+/g, "");
	}

	/**
	 * 条件クリアボタン押下時
	 * 検索条件をクリアして再検索
	 */
	$("#clearButton").click(function(){
		$("#keyword").val("");
		$("#searchBunrui1").children("option:first").prop('selected', true);
		c1Change();
	});
	
	
	//伝票種別変更
	$("#searchDenpyouShubetsu").change(function(e){
		if ($("#searchDenpyouShubetsu").val() == '') {
			return;
		}
		
		bunruiKeywordSet(false);
		
		$("#denpyouKbn").val($("#searchDenpyouShubetsu").val());
		$("form[name=jyoukenForm]").attr("action", "torihiki_ichiran_kensaku");
		$("form[name=jyoukenForm]").attr("method", "get");
		$("form[name=jyoukenForm]").submit();
	});
	
	//追加
	$("#tsuikaButton").click(function(e){
		if ($("#searchDenpyouShubetsu").val() == '') {
			alert("伝票種別を選択してください。");
			return;
		} 

		var shiwakeAddFlg = $("#searchDenpyouShubetsu").find("option:selected").attr("data-shiwakeAddFlg");
		
		if(shiwakeAddFlg == 0){
			var text = $("#searchDenpyouShubetsu").find("option:selected").text();
			alert(text + "の取引（仕訳）は追加できません。");
			return;
		}
		
		$("#denpyouKbn").val($("#searchDenpyouShubetsu").val());
		
		bunruiKeywordSet(true);
		
		// イベントURL(遷移元画面)設定
		var preEventUrl = "torihiki_ichiran_kensaku?"  
				+ "denpyouKbn=" + $("#searchDenpyouShubetsu").val() + "&"
				+ "sortItem=" + $("#sortItem").val() + "&"
				+ "sortOrder=" + $("#sortOrder").val() + "&"
				+ "bunrui1Val=" + encodeURI($("#bunrui1Val").val()) + "&"
				+ "bunrui2Val=" + encodeURI($("#bunrui2Val").val()) + "&"
				+ "bunrui3Val=" + encodeURI($("#bunrui3Val").val()) + "&"
				+ "keywordVal=" + encodeURI($("#keywordVal").val());

		$("#preEventUrl").val(preEventUrl);
		$("form[name=jyoukenForm]").attr("action", "torihiki_tsuika");
		$("form[name=jyoukenForm]").attr("method", "get");
		$("form[name=jyoukenForm]").submit();
	});
	
	//変更
	$(".listHenkouButton").click(function(e){
		$("#shiwakeEdano").val($(e.target).closest('tr').data('edano'));
		
		bunruiKeywordSet(true);
		
		// イベントURL(遷移元画面)設定
		var preEventUrl = "torihiki_ichiran_kensaku?"  
				+ "denpyouKbn=" + $("#searchDenpyouShubetsu").val() + "&"
				+ "sortItem=" + $("#sortItem").val() + "&"
				+ "sortOrder=" + $("#sortOrder").val() + "&"
				+ "bunrui1Val=" + encodeURI($("#bunrui1Val").val()) + "&"
				+ "bunrui2Val=" + encodeURI($("#bunrui2Val").val()) + "&"
				+ "bunrui3Val=" + encodeURI($("#bunrui3Val").val()) + "&"
				+ "keywordVal=" + encodeURI($("#keywordVal").val());

		$("#preEventUrl").val(preEventUrl);
		
		$("form[name=jyoukenForm]").attr("action", "torihiki_henkou");
		$("form[name=jyoukenForm]").attr("method", "get");
		$("form[name=jyoukenForm]").submit();
	});
	
	//削除
	$(".listSakujyoButton").click(function(e){
		
		var edaNo = $(e.target).closest('tr').data('edano');
		
		if(edaNo < 0) {
			alert("特殊な取引（仕訳）のため、削除できません。");
		}
		else
		{
			if (confirm("削除します。よろしいですか？")) {
				$("#shiwakeEdano").val($(e.target).closest('tr').data('edano'));
				$("form[name=jyoukenForm]").attr("action", "torihiki_ichiran_sakujo");
				$("form[name=jyoukenForm]").attr("method", "post");
				$("form[name=jyoukenForm]").submit();
			}
		}
	});

	//ソート
	$(".link").click(function(e){
		
		//有効期限
		if ($(e.target).closest('a').data('sort') == 'headerYuukouKigenAsc') {
			$("#sortItem").val('yuukou_kigen_from');
			$("#sortOrder").val('ASC');
		} else if ($(e.target).closest('a').data('sort') == 'headerYuukouKigenDesc') {
			$("#sortItem").val('yuukou_kigen_from');
			$("#sortOrder").val('DESC');
			
		//分類１
		} else if ($(e.target).closest('a').data('sort') == 'headerBunrui1Asc') {
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
			
		//貸方科目
		} else if ($(e.target).closest('a').data('sort') == 'headerKashikataKamokuAsc') {
			$("#sortItem").val('kashi_kamoku_cd1');
			$("#sortOrder").val('ASC');
		} else if ($(e.target).closest('a').data('sort') == 'headerKashikataKamokuDesc') {
			$("#sortItem").val('kashi_kamoku_cd1');
			$("#sortOrder").val('DESC');
		}
		
		bunruiKeywordSet(true);

		$("form[name=jyoukenForm]").attr("action", "torihiki_ichiran_kensaku");
		$("form[name=jyoukenForm]").attr("method", "get");
		$("form[name=jyoukenForm]").submit();
	});
	
	function bunruiKeywordSet(flg) {
		if(flg == true){
			//分類・キーワードの設定値を記録
			if($("#searchBunrui1").val() != null){
				$("#bunrui1Val").val($("#searchBunrui1").val());
			}else{
				$("#bunrui1Val").val("");
			};
			if($("#searchBunrui2").val() != null){
				$("#bunrui2Val").val($("#searchBunrui2").val());
			}else{
				$("#bunrui2Val").val("");
			};
			if($("#searchBunrui3").val() != null){
				$("#bunrui3Val").val($("#searchBunrui3").val());
			}else{
				$("#bunrui3Val").val("");
			};
			if($("#keyword").val() != null){
				$("#keywordVal").val($("#keyword").val());
			}else{
				$("#keywordVal").val("");
			};
		}else{
			//分類・キーワードの設定値をクリア
			$("#bunrui1Val").val("");
			$("#bunrui2Val").val("");
			$("#bunrui3Val").val("");
			$("#keywordVal").val("");
		}
		
	}
	
	//画面読み込み時処理
	//分類・キーワードに指定していた値を再設定
	if($("#bunrui1Val").val() != null && "" != jQuery.trim($("#bunrui1Val").val())){
		$("#searchBunrui1").val($("#bunrui1Val").val()).change();
	}
	if($("#bunrui2Val").val() != null && "" != jQuery.trim($("#bunrui2Val").val())){
		$("#searchBunrui2").val($("#bunrui2Val").val()).change();
	}
	if($("#bunrui3Val").val() != null && "" != jQuery.trim($("#bunrui3Val").val())){
		$("#searchBunrui3").val($("#bunrui3Val").val()).change();
	}
	if($("#keywordVal").val() != null && "" != jQuery.trim($("#keywordVal").val())){
		$("#keyword").val($("#keywordVal").val());
	}
	torihikiSentakuSearch();
	
});
-->
		</script>
	</body>
</html>
