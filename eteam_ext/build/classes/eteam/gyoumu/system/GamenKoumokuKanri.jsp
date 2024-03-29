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
		<title>画面項目設定｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
	</head>

	<body>
    	<div id='wrap'>

    		<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
				<h1>画面項目設定</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' action='gamen_koumoku_kanri_koushin' class='form-horizontal'>
					<input type='hidden' name='tabId' value='${su:htmlEscape(tabId)}'>
					<!-- 項目選択用タブ -->
					<ul id ="setting_tabs" class="nav nav-tabs">
<c:forEach var='head' items='${gamenHyoujiList}'>
						<li><a href="#setting_${head.denpyou_kbn}" data-toggle="tab">${su:htmlEscape(head.denpyou_shubetsu)}</a></li>
</c:forEach>
					</ul>
					<!-- 画面項目設定 -->
					<section>
						<div id="TabSetting" class="tab-content">
<c:forEach var='setting' items='${gamenKoumouList}' varStatus="st">
<c:if test='${"1" eq setting.hyouji_jun}'>
							<div class="tab-pane" id="setting_${su:htmlEscape(setting.denpyou_kbn)}">
								<div class='no-more-tables'>
									<table class='table-bordered table-condensed'>
										<thead>
											<tr>
												<th rowspan="2">項目ID</th>
												<th rowspan="2">デフォルト項目名</th>
												<th rowspan="2">項目名</th>
												<th rowspan="2">表示</th>
												<th rowspan="2">必須</th>
												<th colspan="2">帳票</th>
											</tr>
											<tr>
												<th>表示</th>
												<th>コード印字</th>
											</tr>
										</thead>
										<tbody>
</c:if>
									<tr class="koumokurow">
										<td>${su:htmlEscape(setting.koumoku_id)}</td>
										<td>${su:htmlEscape(setting.default_koumoku_name)}</td>
										<td>
											<input type='hidden' name='denpyouKbn' value='${su:htmlEscape(denpyouKbn[st.count-1])}'/>
											<input type='hidden' name='koumokuId' value='${su:htmlEscape(koumokuId[st.count-1])}'/>
											<input type='text' name='koumokuName' class='input-block-level' value='${su:htmlEscape(koumokuName[st.count-1])}'/>
										</td>
										<td align="center">
											<input type='checkbox' name='hyouji' value='1' <c:if test='${"1" eq hyoujiFlg[st.count-1]}'>checked</c:if> <c:if test='${"0" eq setting.hyouji_seigyo_flg}'>style='display:none;'</c:if>/>
											<input type='hidden' name='hyoujiFlg' value='${su:htmlEscape(hyoujiFlg[st.count-1])}'/>
										</td>
										<td align="center">
											<input type='checkbox' name='hissu' value='1' <c:if test='${"1" eq hissuFlg[st.count-1]}'>checked</c:if> <c:if test='${"0" eq setting.hyouji_seigyo_flg}'>style='display:none;'</c:if>/>
											<input type='hidden' name='hissuFlg' value='${su:htmlEscape(hissuFlg[st.count-1])}'/>
										</td>
										<td align="center">
											<input type='checkbox' name='pdfHyouji' value='1' <c:if test='${"1" eq pdfHyoujiFlg[st.count-1]}'>checked</c:if> <c:if test='${"0" eq setting.pdf_hyouji_seigyo_flg}'>style='display:none;'</c:if>/>
											<input type='hidden' name='pdfHyoujiFlg' value='${su:htmlEscape(pdfHyoujiFlg[st.count-1])}'/>
										</td>
										<td align="center">
											<input type='checkbox' name='codeOutput' value='1' <c:if test='${"1" eq codeOutputFlg[st.count-1]}'>checked</c:if> <c:if test='${"0" eq setting.code_output_seigyo_flg}'>style='display:none;'</c:if>/>
											<input type='hidden' name='codeOutputFlg' value='${su:htmlEscape(codeOutputFlg[st.count-1])}'/>
										</td>
									</tr>
<c:if test='${setting.hyouji_jun_max eq setting.hyouji_jun}'>
										</tbody>
									</table>
								</div>
							</div>
</c:if>
</c:forEach>
						</div>
					</section>
					<!-- 処理ボタン -->
					<section>
						<button type='button' id='koushinButton' class='btn'><i class='icon-hdd'></i> 更新</button>
					</section>
				</form></div><!-- main -->
			</div><!-- content -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<!-- スクリプト -->
		<script style='text/javascript'>

$(document).ready(function(){

	//表示チェック押下時
	$("input[name=hyouji]").click(function(){
		if ($(this).prop("checked")) {
			$(this).closest("tr").find("input[name=hissu]").prop("disabled", false);
			$(this).closest("tr").find("input[name=pdfHyouji]").prop("disabled", false);
			//休日日数の制御
			$(this).parent().parent().find("input[name=koumokuId]").filter(function(){return this.value=='kyuujitsu_nissuu'}).closest("tr").find("input[name=hissu]").prop("disabled", true).prop("checked", true);
		} else {
			$(this).closest("tr").find("input[name=hissu]").prop("disabled", true).prop("checked", false);
			$(this).closest("tr").find("input[name=pdfHyouji]").prop("disabled", true).prop("checked", false);
			$(this).closest("tr").find("input[name=codeOutput]").prop("disabled", true).prop("checked", false);
			//休日日数の制御
			$(this).parent().parent().find("input[name=koumokuId]").filter(function(){return this.value=='kyuujitsu_nissuu'}).closest("tr").find("input[name=hissu]").prop("disabled", true);
		}
	});
	
	//帳票表示チェック押下時
	$("input[name=pdfHyouji]").click(function(){
		if ($(this).prop("checked")) {
			$(this).closest("tr").find("input[name=codeOutput]").prop("disabled", false);
		}else{
			$(this).closest("tr").find("input[name=codeOutput]").prop("disabled", true).prop("checked", false);
		}
	});

	// HF・UF・差引回数の項目名は変更不可
	$("tr.koumokurow").each(function(){
		var ufKoumokuId = $(this).find("input[name=koumokuId]").val();
		if(ufKoumokuId.indexOf("hf1_cd") >= 0
		|| ufKoumokuId.indexOf("hf2_cd") >= 0
		|| ufKoumokuId.indexOf("hf3_cd") >= 0
		|| ufKoumokuId.indexOf("hf4_cd") >= 0
		|| ufKoumokuId.indexOf("hf5_cd") >= 0
		|| ufKoumokuId.indexOf("hf6_cd") >= 0
		|| ufKoumokuId.indexOf("hf7_cd") >= 0
		|| ufKoumokuId.indexOf("hf8_cd") >= 0
		|| ufKoumokuId.indexOf("hf9_cd") >= 0
		|| ufKoumokuId.indexOf("hf10_cd") >= 0
		|| ufKoumokuId.indexOf("uf1_cd") >= 0
		|| ufKoumokuId.indexOf("uf2_cd") >= 0
		|| ufKoumokuId.indexOf("uf3_cd") >= 0
		|| ufKoumokuId.indexOf("uf4_cd") >= 0
		|| ufKoumokuId.indexOf("uf5_cd") >= 0
		|| ufKoumokuId.indexOf("uf6_cd") >= 0
		|| ufKoumokuId.indexOf("uf7_cd") >= 0
		|| ufKoumokuId.indexOf("uf8_cd") >= 0
		|| ufKoumokuId.indexOf("uf9_cd") >= 0
		|| ufKoumokuId.indexOf("uf10_cd") >= 0
		|| ufKoumokuId.indexOf("sashihiki_num") >= 0
		) {
			$(this).find("input[name=koumokuName]").prop("disabled", true);
		}
	});
	
	// 連絡票No.990 「精算金額との差額」必須チェックの無効化
	$("tr.koumokurow").each(function(){
		var ufKoumokuId = $(this).find("input[name=koumokuId]").val();
		if(ufKoumokuId.indexOf("karibarai_kingaku_sagaku") >= 0) {
			$(this).find("input[name=hissu]").prop("disabled", true);
			$(this).find("input[name=hissu]").css("display", "none");
			$(this).find("input[name=hissuFlg]").val("0");
		}
	});
	// 20221017 「早安楽アイコン」必須チェックの無効化
	$("tr.koumokurow").each(function(){
		var ufKoumokuId = $(this).find("input[name=koumokuId]").val();
		if(ufKoumokuId.indexOf("haya_yasu_raku") >= 0) {
			$(this).find("input[name=hissu]").prop("disabled", true);
			$(this).find("input[name=hissu]").css("display", "none");
			$(this).find("input[name=hissuFlg]").val("0");
		}
	});
	// 2023 インボイス・税抜対応　「うち消費税額」「消費税額」必須チェックの無効化
	$("tr.koumokurow").each(function(){
		var ufKoumokuId = $(this).find("input[name=koumokuId]").val();
		if(ufKoumokuId.indexOf("shouhizeigaku") >= 0) {
			$(this).find("input[name=hissu]").prop("disabled", true);
			$(this).find("input[name=hissu]").css("display", "none");
			$(this).find("input[name=hissuFlg]").val("0");
		}
	});
	// 未使用HF/UH非表示
	<c:if test='${hfUfSeigyo.hf1ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='hf1_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.hf2ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='hf2_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.hf3ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='hf3_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.hf4ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='hf4_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.hf5ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='hf5_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.hf6ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='hf6_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.hf7ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='hf7_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.hf8ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='hf8_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.hf9ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='hf9_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.hf10ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='hf10_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.uf1ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='uf1_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.uf2ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='uf2_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.uf3ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='uf3_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.uf4ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='uf4_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.uf5ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='uf5_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.uf6ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='uf6_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.uf7ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='uf7_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.uf8ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='uf8_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.uf9ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='uf9_cd'}).closest("tr").css("display", "none");</c:if>
	<c:if test='${hfUfSeigyo.uf10ShiyouFlg == "0"}'>$("input[name=koumokuId]").filter(function(){return this.value=='uf10_cd'}).closest("tr").css("display", "none");</c:if>

	//タブ押下時に復元位置として記憶する
	$("#setting_tabs a").click(function(e) {
		$("[name=tabId]").val($(this).attr("href").substr(1));
	});
	
	//更新ボタン押下時
	$("#koushinButton").click(function(){
		$("input[name=hyouji]").each(function(){
			$(this).closest("tr").find("input[name=hyoujiFlg]").val($(this).prop("checked") ? "1" : "0");
		});
		$("input[name=hissu]").each(function(){
			$(this).closest("tr").find("input[name=hissuFlg]").val($(this).prop("checked") ? "1" : "0");
		});
		$("input[name=pdfHyouji]").each(function(){
			$(this).closest("tr").find("input[name=pdfHyoujiFlg]").val($(this).prop("checked") ? "1" : "0");
		});
		$("input[name=codeOutput]").each(function(){
			$(this).closest("tr").find("input[name=codeOutputFlg]").val($(this).prop("checked") ? "1" : "0");
		});
		$("#myForm").submit();
	});
	
	//タブ初期表示
	$("#setting_tabs a[href=#${su:htmlEscape(tabId)}]").tab("show");
	
	//チェックボックスの整合性
	$("input[name=hyouji]").each(function(){
		if (! $(this).prop("checked")) {
			$(this).closest("tr").find("input[name=hissu]").prop("disabled", true).prop("checked", false);
			$(this).closest("tr").find("input[name=pdfHyouji]").prop("disabled", true).prop("checked", false);
		}
	});
	$("input[name=pdfHyouji]").each(function(){
		if ($(this).prop("checked")) {
			$(this).closest("tr").find("input[name=codeOutput]").prop("disabled", false);
		}else{
			$(this).closest("tr").find("input[name=codeOutput]").prop("disabled", true).prop("checked", false);
		}
	});
	
	//休日日数の制御
	$("input[name=koumokuId]").filter(function(){return this.value=='kyuujitsu_nissuu'}).closest("tr").find("input[name=hissu]").prop("disabled", true);
}); 
		</script>
	</body>
</html>
