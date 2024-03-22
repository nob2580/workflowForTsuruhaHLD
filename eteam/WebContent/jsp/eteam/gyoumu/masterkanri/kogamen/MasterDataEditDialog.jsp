<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

<!-- タイトル -->
<h1>${su:htmlEscape(masterName)} マスター編集</h1>

<!-- エラー -->
<%@ include file="/jsp/eteam/include/InputError.jsp" %>
<div class="alert" style="display:none;">
	<button type="button" class="close" data-dismiss="alert">&times;</button>
	<p id='customError'></p>
</div>


<!-- メイン -->
<div id='dialogMain' class='form-horizontal'>
	<input type='hidden' id="exceptionPage" value='${su:htmlEscape(exceptionPage)}'>
	<input type='hidden' id="errorCnt" value='${fn:length(errorList)}'>
	<input type='hidden' id="masterId" value='${su:htmlEscape(masterId)}'>
	<input type='hidden' id="pkstr" value='<c:out value="${pkstr}"/>'>

	<!-- 入力フィールド -->
	<section>
		<form id='tourokuForm' class='form-horizontal'>
			<div class='no-more-tables empty'>
				<table class='table-bordered table-condensed'>
					<thead>
						<tr>
							<th>項目名</th>
							<th>形式</th>
							<th>データ</th>
						</tr>
					</thead>
					<tbody id='columns'>
						<c:forEach var="col" items="${columnList}">
							<tr>
								<td><c:if test="${col.required}"><span class='required'>*</span></c:if>
									<span>${su:htmlEscape(col.displayName)}</span>
									<input type='hidden' name='name' value='${su:htmlEscape(col.name)}'>
								</td>
								<td>${su:htmlEscape(col.displayTypeName)}</td>
								<td>
<c:if test="${su:htmlEscape(masterId) ne 'kaigai_nittou_nado_master'}">
	<!-- 消費税率マスター 端数計算区分だけの特殊制御 -->
	<c:if test="${(su:htmlEscape(masterId) eq 'shouhizeiritsu') and (su:htmlEscape(col.name) eq 'hasuu_keisan_kbn')}">
								<input type='text' name='value' class='input-large ${col.inputClassText}' ${col.maxLengthAttrText} value='3' disabled>
	</c:if>
	<c:if test="${(su:htmlEscape(masterId) ne 'shouhizeiritsu') or (su:htmlEscape(col.name) ne 'hasuu_keisan_kbn')}">
								<input type='text' name='value' class='input-large ${col.inputClassText}' ${col.maxLengthAttrText} value=''>
	</c:if>
</c:if>
<c:if test="${su:htmlEscape(masterId) eq 'kaigai_nittou_nado_master'}">
								<!-- 海外日当等マスターだけの特殊制御 -->
								<c:if test="${su:htmlEscape(col.name) eq 'heishu_cd'}">
									<input type='text' name='value' class='input-small ${col.inputClassText}' ${col.maxLengthAttrText} value=''>
									<button type='button' id='heishuSentakuButton' class='btn btn-small'>選択</button>
								</c:if>
								<c:if test="${su:htmlEscape(col.name) ne 'heishu_cd'}">
									<!-- 通貨単位は幣種コード選択で自動入力 -->
									<input type='text' name='value' class='input-large ${col.inputClassText}' ${col.maxLengthAttrText} value='${su:htmlEscape(currencyUnit)}' <c:if test="${su:htmlEscape(col.name) eq 'currency_unit'}">disabled</c:if>>
								</c:if>
</c:if>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</form>
	</section>
</div><!-- main -->

<!-- スクリプト -->
<script style='text/javascript'>
/**
 * エラーメッセージの設定
 */
function setInputError(msg) {
	var obj = $("#customError");
	obj.text(msg);
	obj.closest("div.alert").show();
};

/**
 * POST送信用ダイアログ明細情報取得
 */
function getPostDialogInfo() {
	var cols = $("#columns");
	return {
		"masterId"	: $("#masterId").val(),
		"name"		: cols.find("input[name=name]").map(function(){return $(this).val();}).get(),
		"value"		: cols.find("input[name=value]").map(function(){return $(this).val();}).get()
	};
}

/**
 * ダイアログ明細情報取得
 */
function getDialogInfo() {
	var cols = $("#columns");
	var colNameAry = cols.find("input[name=name]").map(function(){return $(this).val();}).get();
	var colValueAry = cols.find("input[name=value]").map(function(){return $(this).val();}).get();

	var ret = {};
	for (var ii = 0; ii < colNameAry.length; ii++) {
		ret[colNameAry[ii]] = colValueAry[ii];
	}
	return ret;
}

/**
 * 通貨単位取得
 */
function setCurrencyUnit(){
	var title = $("#heishuSentakuButton").closest("tr").find("td:first span").text();
	dialogRetHeishuCd		= $("#heishuSentakuButton").prev();
	dialogRetTsuukaTani		= $("#heishuSentakuButton").closest("tr").next().find("input[name=value]");
	commonHeishuCdLostFocus(dialogRetHeishuCd, dialogRetTsuukaTani, null,title);
}

/**
 * ダイアログ明細情報設定
 */
function setDialogInfo(info) {	
	var main = $("#dialogMain");

	//入力補助
	commonInit(main);
	
	//幣種選択ボタン押下時Function
	$("#heishuSentakuButton").on("click", function(){
		dialogRetHeishuCd		= $("#heishuSentakuButton").prev();
		dialogRetTsuukaTani		= $("#heishuSentakuButton").closest("tr").next().find("input[name=value]");
		dialogRetRate			= null;
		commonHeishuSentaku();
	});

	//幣種コードロストフォーカス時Function
	$("#columns").find("#heishuSentakuButton").prev().blur(setCurrencyUnit);
	
	if (!info) {
		return ;
	}
	var cols = $("#columns");
	for (var name in info) {
		if( ($("#masterId").val() == "shouhizeiritsu") && (name == "hasuu_keisan_kbn") ){
			//消費税率マスター 端数計算区分は3を固定で設定
			cols.find("input[name=name][value=" + name.selectorEscape() + "]").closest("tr").find("input[name=value]").val("3");
		} else {
			cols.find("input[name=name][value=" + name.selectorEscape() + "]").closest("tr").find("input[name=value]").val(info[name]);
		}
	}
	
	//最新の通貨単位を取得
	setCurrencyUnit();
}
//-->
</script>
