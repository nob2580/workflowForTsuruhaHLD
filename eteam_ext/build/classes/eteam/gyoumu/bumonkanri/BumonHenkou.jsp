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
		<title>部門変更｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>部門変更</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' action='bumon_tsuika_touroku' class='form-horizontal'>
					<input type='hidden' name='bumonCd' value='${bumonCd}'>
					<input type='hidden' name='kijunDate' value='${kijunDate}'>
					<input type='hidden' id='addListCnt' value='0'>

					<!-- 入力フィールド -->
					<section>
						<h2>部門情報</h2>
					<!-- 項目選択用タブ -->
					<ul id ="setting_tabs" class="nav nav-tabs">
						<c:forEach var="i" begin="0" end="${fn:length(bumonName) - 1}" step="1" varStatus="st">
							<li <c:if test='${tabIndex eq st.count-1}'>class='active'</c:if>><a href=#bumonlist_${st.count} data-toggle="tab">${su:htmlEscape(tabMeisyo[(st.count-1)])}</a></li>
						</c:forEach>
						<li id="addBtnLi">
							<button type='Button' name='kikanAddButton' class='btn'><i class='icon-plus'></i> 期間追加</button>
							<button type='Button' name='kikanDelButton' class='btn'><i class='icon-remove'></i> 期間削除</button>
						</li>
					</ul>
					<div id="bumonTabs" class="tab-content">
<c:forEach var="i" begin="0" end="${fn:length(bumonName) - 1}" step="1" varStatus="st">
						<div class='tab-pane <c:if test="${tabIndex eq st.count-1}">active</c:if>' id='bumonlist_${st.count}'>
							<div class='control-group'>
								<label class='control-label'>部門コード</label>
								<div class='controls non-input'>${bumonCd}</div>
							</div>
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>部門名</label>
								<div class='controls'>
									<input type='text' name='bumonName' class='input-large' maxlength="20" value='${su:htmlEscape(bumonName[(st.count-1)])}'>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>親部門コード</label>
								<div class='controls'>
									<input type='text' name='oyaBumonCd' class='input-inline input-small' disabled value='${su:htmlEscape(oyaBumonCd[(st.count-1)])}'>
									<button type='button' name='bumonSentakuBtn' class='btn btn-small'>選択</button>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>親部門名</label>
								<div class='controls'>
									<input type='text' name='oyaBumonName' class='input-inline input-large' disabled value='${su:htmlEscape(oyaBumonName[(st.count-1)])}'>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>有効期限</label>
								<div class='controls'>
									<b><span class='required'>*</span></b>
									<input type='text' name='kigenFrom' class='input-small datepicker' value='${su:htmlEscape(kigenFrom[(st.count-1)])}'>
									<span> ～ </span>
									<input type='text' name='kigenTo' class='input-small datepicker' value='${su:htmlEscape(kigenTo[(st.count-1)])}'>
								</div>
							</div>
							<div class='control-group' <c:if test="${!securityPatternFlag}"> style="display: none"</c:if>>
								<label class='control-label' for='securityPattern'>セキュリティパターン</label>
								<div class='controls'>
									<input type='text' id='securityPattern' name='securityPattern' class='input-small' maxlength='4' value='${su:htmlEscape(securityPattern[(st.count-1)])}'>
								</div>
							</div>
						</div>
</c:forEach>
					<span class="lasttab"></span>
					</div>
					</section>

					<!-- 処理ボタン -->
					<section>
						<button type='button' id='henkouButton' class='btn'><i class='icon-refresh'></i> 変更</button>
						<button type='button' id='sakujoButton' class='btn'><i class='icon-remove' ></i> 部門削除</button>
					</section>
				</form></div><!-- main -->
				<!-- Modal -->
				<div id='dialog'></div>
			</div><!-- content -->
			<div id="push"></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>


/**
 * 期間追加ボタン押下時Function
 */
function kikanAdd() {
	
	//現在タブを複製して新規期間用タブ作成
	var actLi = $("#setting_tabs").find(".active");
	var orgTab = $(".tab-pane.active");
	var addTab = orgTab.clone();
	var span = $(".lasttab");
	var blCnt = Number($("#addListCnt").val());
	blCnt = blCnt + 1;
	var newId = "newbumonlist_" + blCnt;
	$("#addListCnt").val(blCnt);
	
	var addLi = $("<li class='active'><a href=#" + newId + " data-toggle='tab'>新規期間"+ blCnt +"</a></li>");
	
	//現在タブのactive解除
	actLi.removeClass("active");
	orgTab.removeClass("active");
	
	//中身をクリアしてタブ追加
	addTab.removeAttr("id");
	addTab.attr("id",newId);
	commonInit(addTab);
	inputClear(addTab);
	addTab.insertBefore(span);
	addLi.insertBefore($("#addBtnLi"));
	
}

/**
 * 期間削除ボタン押下時Function
 */
function kikanDel() {
	
	//最初の要素なら削除させない
	if($("#bumonTabs").children("div").length <= 1){
		alert("タブが1つしかないため、削除できません。");
		return;
	}
	
	var delTab = $(".tab-pane.active");
	var delLi = $("#setting_tabs").find(".active");
	
	//直前(無ければ直後)のタブに遷移させる
	var movTab;
	var movLi;
	if(delTab.prev("div").length){
		movTab = delTab.prev("div");
	}else{
		movTab = delTab.next("div");
	}
	if(delLi.prev("li").length){
		movLi = delLi.prev("li");
	}else{
		movLi = delLi.next("li");
	}
	
	//削除対象タブ・本体を削除
	delTab.remove();
	delLi.remove();
	
	//activeを遷移させる
	movTab.addClass("active");
	movLi.addClass("active");
	
}


/**
 * 部門選択ボタン押下時Function
 */
function bumonSentaku() {
	dialogRetBumonName =  $(this).closest(".tab-pane").find("input[name=oyaBumonName]");
	dialogRetBumonCd =  $(this).closest(".tab-pane").find("input[name=oyaBumonCd]");
	var kijunBi = $(this).closest(".tab-pane").find("input[name=kigenFrom]").val();
	if(kijunBi.length != 10){
		commonBumonSentaku();
		return false;
	}
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "部門選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	});
	$("#dialog").load("bumon_sentaku?d_kijunBi="+kijunBi);
	return false;
}


/**
 * 期間開始日変更時Function
 */
function kikanChange() {
	//現在タブ名を期間開始日に変更
	var actLi = $("#setting_tabs").find(".active");
	var orgTab = $(".tab-pane.active");
	var date = orgTab.find("input[name=kigenFrom]").val();
	if(date != ""){
		date = date + "～";
	}else{
		date = "(開始日未入力)"
	}
	actLi.find("a").html(date);
	
}

$(document).ready(function(){	
	
	//期間追加ボタン押下
	$("body").on("click","button[name=kikanAddButton]",kikanAdd);
	//期間削除ボタン押下
	$("body").on("click","button[name=kikanDelButton]",kikanDel);
	//部門選択ボタン押下
	$("body").on("click","button[name=bumonSentakuBtn]",bumonSentaku);
	
	//変更ボタン押下
	$("body").on("click","#henkouButton",function(event){
		$("#myForm").attr("action", "bumon_henkou_henkou");
		$("#myForm").submit();
	});

	//削除ボタン押下
	$("body").on("click","#sakujoButton",function(event){
		if(window.confirm('該当部門コードの全期間データを削除してもよろしいですか？\r\n※いずれかの期間で本部門を親部門とする部門がある場合、データの削除はできません。')) {
			$("#myForm").attr("action", "bumon_henkou_sakujo");
			$("#myForm").submit();
		}
	});
	
	//期間変更時処理
	$("body").on("change","input[name=kigenFrom]",kikanChange);
	
});

		</script>
	</body>
</html>
