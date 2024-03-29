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
	<!-- 入力フィールド -->
	<section id='futanBumonSearchJouken'>
		<div id='kensaku'>
			<form class="form-inline">
				<input type='hidden' name='mode' value='${su:htmlEscape(mode)}'>
				<input type='hidden' name='denpyouId' value='${su:htmlEscape(denpyouId)}'>
				<input type='hidden' name='kihyouBumonCd' value='${su:htmlEscape(kihyouBumonCd)}'>
				<input type='hidden' id='kamokuCdForShiireKbn' value='${su:htmlEscape(kamokuCd)}'>
				<input type='hidden' name='shiwakeEdano' value='${su:htmlEscape(shiwakeEdano)}'>
				<input type='hidden' name='denpyouKbn' value='${su:htmlEscape(denpyouKbn)}'>
<c:if test="${!empty kamokuCd}">
				<div class='control-group'>
					<label class="label label-sub" for='kamokuCd'>勘定科目ｺｰﾄﾞ</label>
					<input type='text' id='kamokuCd' name='kamokuCd' value='${su:htmlEscape(kamokuCd)}' readonly class='input-small'>
					<label class="label label-sub" for='kamokuName'>勘定科目名</label>
					<input type='text' id='kamokuName' name='kamokuName' value='${su:htmlEscape(kamokuName)}' readonly>
				</div>
</c:if>
<c:if test="${syuukeiFlag}">
				<div class='control-group'>
					<label class="label label-sub" for='syuukeiBumonCd'>集計部門</label>
					<input type='text' id='syuukeiBumonCd' name='syuukeiBumonCd_dialog' class='input-small' value='${su:htmlEscape(syuukeiBumonCd)}'>
					<input type='text' id='syuukeiBumonName' name='syuukeiBumonName_dialog' class='' disabled value='${su:htmlEscape(syuukeiBumonName)}' >
					<button type='button' id='syuukeiBumonSentakuButton' class='btn btn-small' >選択</button>
					<button type='button' id='syuukeiBumonClearButton' class='btn btn-small' >クリア</button>
				</div>
</c:if>
				<div class='control-group'>
					<label class="label label-sub" for='futanBumonCd'>負担部門ｺｰﾄﾞ</label>
					<input type='text' id='futanBumonCd' name='futanBumonCd_dialog' class='input-small'>
					<label class="label label-sub" for='futanBumonName_dialog'>負担部門名</label>
					<input type='text' id='futanBumonName' name='futanBumonName_dialog'>
					<button type='button' id='futanBummonSearchButton' class='btn'>検索</button>
				</div>
			</form>
		</div>
		<div id='syuukeiDialog'>
		</div>
	</section>
	<section id='futanBumonSearchResult'>
	    <div>
<c:if test="${fn:length(futanBumonList) > 0}" >
		<table class='table-bordered table-condensed'>
			<thead>
				<tr>
					<th>負担部門ｺｰﾄﾞ</th>
					<th>負担部門名</th>
				</tr>
			</thead>
			<tbody>
<c:forEach var="record" items="${futanBumonList}">
				<tr>
					<td class='futanBumonCd'>${su:htmlEscape(record.futan_bumon_cd)}</td>
					<td><a class='link futanBumonSentaku'>${su:htmlEscape(record.futan_bumon_name)}</a></td>	            
				</tr>
				<input type='hidden' name='syuukeiBumonCd_record' value='${su:htmlEscape(record.syuukei_bumon_cd)}'>
</c:forEach>
			</tbody>
		</table>
</c:if>
	    </div>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
/**
 * 検索ボタン押下時Function
 */
$("#futanBummonSearchButton").click(function(){
	$("a.futanBumonSentaku").each(function(){
		var cd = $("input[name=futanBumonCd_dialog]").val();
		var name = $("input[name=futanBumonName_dialog]").val();
		if (wordSearch($(this).parent().prev().text(), cd) && wordSearch($(this).text(), name)) {
			$(this).parent().parent().css("display","table-row");
		}else{
			$(this).parent().parent().css("display","none");
		}
	});
});

/**
 * 部門検索ダイアログ：部門選択
 */
$("a.futanBumonSentaku").click(function(){
	var a = $(this);
	var tr = a.closest("tr");
	var td = tr.find("td.futanBumonCd");
	var title = a.closest("#dialog").parent().find(".ui-dialog-title").text();
	var mode = a.closest("#dialogMain").find("[name=mode]").val();
	dialogRetFutanBumonCd.val(a.closest("tr").find(".futanBumonCd").text());
	commonFutanBumonCdLostFocus(mode ,dialogRetFutanBumonCd, dialogRetFutanBumonName, title, $("div#dialogMain").find("input[name=denpyouId]").val(), $("div#dialogMain").find("input[name=kihyouBumonCd]").val(), $("#kamokuCdForShiireKbn").val(), dialogRetKariShiireKbn, $("div#dialogMain").find("input[name=denpyouKbn]").val(), $("div#dialogMain").find("input[name=shiwakeEdano]").val());

	isDirty = true;

	$("#dialog").dialog("close");
});
//初期表示処理
$("#dialog").ready(function(){
	commonInit($("#futanBumonSearchJouken"));
	
	//集計部門選択ボタン押下時Function
	$("#syuukeiBumonSentakuButton").click(function(){
		dialogRetSyuukeiBumonCd = $("input[name=syuukeiBumonCd_dialog]");
		dialogRetSyuukeiBumonName = $("input[name=syuukeiBumonName_dialog]");
		dialogCallbackSyuukeiBumonSentaku = function() {syuukeiBumonCdLostFocus($('#kensaku input[name=syuukeiBumonCd_dialog]').val(), $('#kensaku input[name=mode]').val(), $('#kensaku input[name=denpyouId]').val(), $('#kensaku input[name=kihyouBumonCd]').val());};
		syuukeiDialogOpen($('#kensaku input[name=mode]').val(), $('#kensaku input[name=denpyouId]').val(), $('#kensaku input[name=kihyouBumonCd]').val());
	});
	//集計部門クリアボタン押下時Function
	$("#syuukeiBumonClearButton").click(function(){
		$("input[name=syuukeiBumonCd_dialog]").val("");
		$("input[name=syuukeiBumonName_dialog]").val("");
		syuukeiBumonCdLostFocus($('#kensaku input[name=syuukeiBumonCd_dialog]').val(), $('#kensaku input[name=mode]').val(), $('#kensaku input[name=denpyouId]').val(), $('#kensaku input[name=kihyouBumonCd]').val());
	});
	//集計部門コードロストフォーカス時Function
	$("input[name=syuukeiBumonCd_dialog]").blur(function(){
		syuukeiBumonCdLostFocus($('#kensaku input[name=syuukeiBumonCd_dialog]').val(), $('#kensaku input[name=mode]').val(), $('#kensaku input[name=denpyouId]').val(), $('#kensaku input[name=kihyouBumonCd]').val());
	});
	
});

</script>
