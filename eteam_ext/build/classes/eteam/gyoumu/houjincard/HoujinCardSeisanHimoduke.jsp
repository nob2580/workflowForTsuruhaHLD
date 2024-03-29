<%@ page import="eteam.symbol.EteamSymbol"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
<!DOCTYPE html>
<html lang="ja">
	<head>
		<meta charset="utf-8">
		<title>法人カード精算紐付｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
	</head>

	<body>
    	<div id="wrap">

    		<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
  				<h1>法人カード精算紐付</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<input type="hidden" name="sortKbn" value="${su:htmlEscape(sortKbn)}">
				<input type="hidden" name='preEventUrl' value='${su:htmlEscape(preEventUrl)}'>
				<div id='main'><form id='myForm' method='get' class='form-horizontal'>
					<input type="hidden" name="pageNo" value="${su:htmlEscape(pageNo)}">
					<input type='hidden' name='jyogaiChangeList' value=''>
					<input type='hidden' name='jyogaiChangeFlg' value=''>
					<input type='hidden' name='jyogaiRiyuu' value=''>

					<!-- 検索条件 -->
					<section>
						<select name='statusSelect' class='input-small'>
							<option value='0' <c:if test='${"0" eq statusSelect}'>selected</c:if>>未精算</option>
							<option value='1' <c:if test='${"1" eq statusSelect}'>selected</c:if>>精算済</option>
							<option value='2' <c:if test='${"2" eq statusSelect}'>selected</c:if>>全て</option>
						</select>
						<div>
							<div class='no-more-tables'>
								<br>
								<div class='control-group'>
									<label class='control-label'>対象カード</label>
									<div class='controls'>
										<select id='searchCardShubetsu' name='searchCardShubetsu' class='input-large'>
<c:forEach var="record" items="${cardList}" varStatus="st">
											<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq searchCardShubetsu}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
</c:forEach>
										</select>
									</div>
								</div>
								<div class='control-group'>
									<label class='control-label'>部署</label>
									<div class='controls'>
										<input type="text" name='bushoCd' class='input-small' value='${su:htmlEscape(bushoCd)}'>
									</div>
								</div>
								<div class='control-group'>
									<label class='control-label'>社員</label>
									<div class='controls'>
										<input type='text' name="shainNo" class='input-medium pc_only' <c:if test='${keiriFlg eq false}'> disabled </c:if>value='${su:htmlEscape(shainNo)}'>
										<input type='text' id="userName" class='input-xlarge' disabled value='${su:htmlEscape(userName)}'>
										<input type='hidden' name="userId" value='${su:htmlEscape(userId)}'>
										<button type='button' name='userSentakuButton' class='btn btn-small' <c:if test='${keiriFlg eq false}'> style='display:none' </c:if> >選択</button>
									</div>
								</div>
								<div class='control-group'>
									<label class='control-label'>日付</label>
									<div class='controls'>
										<input type='text' name='dateFrom' class='input-small datepicker' value='${su:htmlEscape(dateFrom)}'>
										～
										<input type='text' name='dateTo' class='input-small datepicker' value='${su:htmlEscape(dateTo)}'>
									</div>
								</div>
							</div>
							<div class='blank'></div>
							<div>
								<button type='submit' class='btn'><i class='icon-search'></i> 検索</button>
<c:if test="${jyogaiBtnFlg}">
								<button id='jyogaiBtn' type='button' class='btn' href='#jyogaiModal' role='button' data-toggle='modal'>除外</button>
								<button id='fukkatsuBtn' type='button' class='btn'>復活</button>
</c:if>
							</div>
						</div>
					</section>
				</form></div><!-- main -->

					<!-- 一覧表示 -->
					<section>
						<div class='blank'></div>
						<div id="List" class='no-more-tables'>
							<table class="table-bordered table-condensed">
								<thead>
									<tr>
										<th><nobr>対象</nobr><br><input type='checkbox' id='allcheck'></th>
										<th><nobr>紐付先伝票ID</nobr></th>
										<th><nobr>カード会社</nobr></th>
										<th><nobr>カード番号</nobr></th>
										<th>利用日<br><a href='${preEventUrl}pageNo=1&sortKbn=1'>▲</a>    <a href='${preEventUrl}pageNo=1&sortKbn=2'>▼</a></th>
										<th>部署<br><a href='${preEventUrl}pageNo=1&sortKbn=3'>▲</a>    <a href='${preEventUrl}pageNo=1&sortKbn=4'>▼</a></th>
										<th>社員No<br><a href='${preEventUrl}pageNo=1&sortKbn=5'>▲</a>    <a href='${preEventUrl}pageNo=1&sortKbn=6'>▼</a></th>
										<th><nobr>使用者</nobr></th>
										<th><nobr>金額</nobr></th>
										<th><nobr>加盟店</nobr></th>
										<th><nobr>業種コード</nobr></th>
										<th><nobr>除外理由</nobr></th>
									</tr>
								</thead>
								<tbody>
<c:forEach var="record" items="${rirekiList}" varStatus="status">
									<tr>
										<td align="center">
											<input type='checkbox' name='sentaku' value='${su:htmlEscape(record.card_jouhou_id)}'/>
											<input type='hidden' name='himodukeId' value='${su:htmlEscape(record.torikomi_denpyou_id)}'/>
											<input type='hidden' name='jyogaiFlg' value='${su:htmlEscape(record.jyogai_flg)}'/>
										</td>
										<td>
											<nobr>
											<c:choose>
												<c:when test='${record.torikomi_denpyou_id != ""}'>
													<a href='${su:htmlEscape(record.denpyou_shubetsu_url)}?denpyouKbn=${su:htmlEscape(record.denpyou_kbn)}&denpyouId=${su:htmlEscape(record.torikomi_denpyou_id)}' target='_blank'>${su:htmlEscape(record.torikomi_denpyou_id)}</a>
												</c:when>
											</c:choose>
											</nobr>
										</td>
										<td><nobr>${su:htmlEscape(record.cardName)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.card_bangou)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.riyoubi)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.busho_cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.shain_bangou)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.shiyousha)}</nobr></td>
										<td class="text-r"><nobr>${su:htmlEscape(record.kingaku)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.kameiten)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.gyoushu_cd)}</nobr></td>
										<td><nobr>${su:htmlEscape(record.jyogai_riyuu)}</nobr></td>
									</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
						<div class='blank'></div>
						<%@ include file="/jsp/eteam/include/Paging.jsp" %>
					</section>
				<!-- Modal 除外理由 -->
				<div id='jyogaiModal' class='modal hide fade' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>
					<div class='modal-header'>
						<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>×</button>
						<h3 id='myModalLabel'>除外理由</h3>
					</div>
					<div class='modal-body'>
						<p>除外理由</p>
						<input type='text' maxlength='60' class='input-block-level' name='jyogaiRiyuuModal' placeholder='除外理由を入力してください。(60字)'>
					</div>
					<div class='modal-footer'>
						<button class='btn' data-dismiss='modal' aria-hidden='true'>閉じる</button>
						<button type='button' class='btn btn-primary' onClick="jyogaiFlgSetAction('1')">除外</button>
					</div>
				</div>
				<!-- Modal -->
				<div id='dialog'></div>
			</div> <!-- /container -->
			<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<!-- スクリプト -->
		<script style='text/javascript'>

$(document).ready(function(){

	//ステータス変更
	$("select[name=statusSelect]").change(function(){
		var objForm = $("#myForm");
		objForm.attr("action", "houjin_card_seisan_himoduke");
		objForm.attr("method", "get");
		objForm.attr("target", "_self");
		objForm.submit();
	});
	
	dialogRetuserId   = $("input[name=userId]");
	dialogRetusername = $("#userName");
	dialogRetshainNo  = $("input[name=shainNo]");
	
	commonShainNoLostFocus(dialogRetshainNo, dialogRetusername, "ユーザー", dialogRetuserId, false);
	
	/**  社員番号ロストフォーカス時 */
	$('input[name=shainNo]').change(function(event) {
		commonShainNoLostFocus(dialogRetshainNo, dialogRetusername, "ユーザー", dialogRetuserId, false);
	});
	
	/** ユーザー選択ボタン押下時Function */
	$('button[name=userSentakuButton]').click(function(event) {
		commonUserSentaku();
	});
	
	//チェック
	$("#allcheck").click(function(){
		$("input[name=sentaku]").prop('checked', $(this).prop("checked"));
	});
	
	
});

/**
 * 除外ボタン押下時
 */
$("#jyogaiBtn").click(function(){
	if($("input[name=sentaku]:checked").length == 0){
		alert( "除外対象データが選択されていません。");
		return false;
	}
	//除外操作で既にデータ紐付済みでないか確認
	var errFlg = false;
	$("input[name=sentaku]:checked").each(function(){
		if($(this).parent().find("input[name=himodukeId]").val() != "" ){
			alert("伝票に紐付いている明細を除外することができません。");
			errFlg = true;
			return false;
		}
	});
	if(errFlg == true) return false;
});

/**
 * 復活ボタン押下時
 */
$("#fukkatsuBtn").click(function(){
	if($("input[name=sentaku]:checked").length == 0){
		alert("復活対象データが選択されていません。");
		return false;
	}
	//復活操作で全未除外でないか確認
	var chkFlg = false;
	$("input[name=sentaku]:checked").each(function(){
		if($(this).parent().find("input[name=jyogaiFlg]").val() == "1" ){chkFlg = true;}
	});
	if(chkFlg == false){
		alert("チェックされたデータは全て未除外です。");
		return false;
	}
	jyogaiFlgSetAction('0');
});

/**
 * 除外フラグ設定処理
 * @param sort 除外フラグ
 */
function jyogaiFlgSetAction(jyogaiFlg) {
	if(jyogaiFlg == "1"){
		if($("input[name=jyogaiRiyuuModal]").val().length <= 0){
			alert("除外理由を入力してください。");
			return false;
		}else if($("input[name=jyogaiRiyuuModal]").val().length > 60){
			alert("除外理由は60文字以内で入力してください。");
			return false;
		}
	}
	
	var jyogaiList = "";
	$("input[name=sentaku]:checked").each(function(){
		jyogaiList = jyogaiList + ($(this).val() + ",");
	});
	jyogaiList = jyogaiList.substring(0, (jyogaiList.length - 1));
	$("input[name=jyogaiChangeFlg]").val(jyogaiFlg);
	$("input[name=jyogaiChangeList]").val(jyogaiList);
	$("input[name=jyogaiRiyuu]").val(jyogaiFlg == "1" ? $("input[name=jyogaiRiyuuModal]").val() : "");
	
	var objForm = $("#myForm");
	objForm.attr("action", "houjin_card_seisan_himoduke_jyogaichange");
	objForm.attr("method", "get");
	objForm.attr("target", "_self");
	objForm.submit();
	
}

		</script>
	</body>
</html>
