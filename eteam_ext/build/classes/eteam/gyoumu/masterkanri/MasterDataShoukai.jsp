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
		<title>マスターデータ照会｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
			<div id='content' class='container' style="position:relative">
				<!-- タイトル -->
				<h1>マスターデータ照会</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main' class='form-horizontal'>

					<!-- ぱんくず &gt; hoge -->
					<section><h3>
						<a href='master_data_ichiran'>一覧</a>
					</h3></section>

					<!-- マスター属性表示 -->
					<section>
				  		<h2>マスターデータ</h2>
						<div>
							<div class='control-group'>
								<label class='control-label'>マスターID</label>
								<div class='controls non-input' id='masterIdForShow'><b>${su:htmlEscape(masterId)}</b></div>
							</div>
							<div class='control-group'>
								<label class='control-label'>マスター名</label>
								<div class='controls non-input'><b>${su:htmlEscape(masterName)}</b></div>
							</div>
						</div>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>#</th>
										<th style="width:135px">更新日時</th>
									</tr>
								</thead>
								<tbody>
<c:forEach var="record" items="${masterDataVersionList}">
									<tr>
										<td>
											${su:htmlEscape(record.version)}
										</td>
										<td class="${record.bg_color}">
											<a href='master_data_shoukai?masterId=${su:htmlEscape(masterId)}&amp;version=${record.version}'>${record.koushin_time}</a><br>
										</td>
									</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
					</section>

<c:if test='${henkouKahiFlg eq "1"}'>
					<!-- 処理ボタン -->
					<section><form id='henkouForm' action="master_data_henkou" method="get">
						<input type='hidden' name='masterId' value='${su:htmlEscape(masterId)}'/>
						<button type='button' id='henkouButton' class='btn'><i class='icon-refresh'></i> 一括変更</button>
					</form></section>
</c:if>

					<!-- ダウンロード -->
					<section>
						<h2>CSVファイル</h2>
						<a href='master_data_shoukai_download?masterId=${su:htmlEscape(masterId)}&version=${su:htmlEscape(version)}'>${su:htmlEscape(fileName)}</a>
					</section>

					<!-- データ -->
					<section>
						<h2>データ
							<c:if test='${editable}'>
								<span class='non-print'>
									<button type='button' id='lineAddButton' class='btn btn-small'>行追加</button>
								</span>
							</c:if>
						</h2>
						<c:if test='${masterId eq "kinyuukikan"}'>
							<div>
								<form id='myForm' method='get' action='master_data_shoukai_search' class='form-horizontal'>
									<label class="label"><span class='required'>*</span>金融機関</label>
									<input type='hidden' name='masterId' value='${su:htmlEscape(masterId)}'/>
									<input type="text" name="ginkouCd" class="input-mini " maxlength=4 value="${ginkouCd}">
									<input type="text" name="ginkouName" class="input-mediam" disabled value="${ginkouName}">
									<input type="hidden" name="isValid" value="0">
									<input type="hidden" name='isSearch' value='${isSearch}'>
									<button type='button' id='ginkouSentakuButton' class='btn btn-small'>選択</button>
									<button type='button' id='kensakuButton' class='btn' ><i class='icon-search'></i> 検索実行</button>
								</form>
							</div>
						</c:if>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<c:if test='${editable}'><th rowspan='2'></th></c:if>
<c:forEach var="record" items="${logicalNameList}">
										<th>${su:htmlEscape(record)}</th>
</c:forEach>
									</tr>
									<tr id='physicalRow'>
<c:forEach var="record" items="${physicalNameList}" varStatus='st'>
										<th data-type='${columnTypeList[st.index]}' data-decimal='${columnDecimalList[st.index]}'>${su:htmlEscape(record)}</th>
</c:forEach>
									</tr>
								</thead>
								<tbody id='dataList'>
<c:forEach var="record" items="${dataList}" varStatus='st'>
									<tr data-index='${st.index}' data-pkstr='<c:out value="${pkDataList[st.index]}"/>' data-orgPkstr='<c:out value="${pkDataList[st.index]}"/>'  <c:if test='${!displayableList[st.index]}'>style='display:none'</c:if>>
										<c:if test='${editable}'>
											<td><nobr><button type='button' name='lineEditButton' class='btn btn-mini'>変更</button></nobr></td>
										</c:if>
<c:forEach var="data" items="${record}" varStatus='st'>
										<td <c:if test='${editable}'>data-value='<c:out value="${data}"/>'</c:if> <c:if test='${columnTypeList[st.index] eq "numeric"}'> align='right'</c:if> ><c:if test='${columnTypeList[st.index] ne "numeric"}'>${su:htmlEscape(data)}</c:if><c:if test='${columnTypeList[st.index] eq "numeric" && !(columnDecimalList[st.index])}'><fmt:formatNumber value ="${su:htmlEscape(data)}"/></c:if><c:if test='${columnTypeList[st.index] eq "numeric" && columnDecimalList[st.index] && su:htmlEscape(masterId) ne "kaigai_nittou_nado_master" && su:htmlEscape(masterId) ne "koutsuu_shudan_master"}'><fmt:formatNumber value ="${su:htmlEscape(data)}" pattern="##0.00000"/></c:if><c:if test='${columnTypeList[st.index] eq "numeric" && columnDecimalList[st.index] && su:htmlEscape(masterId) eq "kaigai_nittou_nado_master"}'><fmt:formatNumber value ="${su:htmlEscape(data)}" pattern="#,##0.00"/></c:if>
											<c:if test='${columnTypeList[st.index] eq "numeric" && columnDecimalList[st.index] && su:htmlEscape(masterId) eq "koutsuu_shudan_master"}'><fmt:formatNumber value ="${su:htmlEscape(data)}" pattern="#0.000"/></c:if></td>
</c:forEach>
									
</c:forEach>
								</tbody>
							</table>
						</div>
					</section>
					<c:if test='${editable}'>
						<section>
							<button type='button' id='registButton' class='btn'><i class='icon-refresh'></i> 変更登録</button>
							<form id='registForm' action="master_data_henkou_touroku" method="post" style='display:none;'>
								<input type='hidden' id='masterId' name='masterId' value='${su:htmlEscape(masterId)}'/>
								<input type='hidden' id='version' name='version' value='${su:htmlEscape(version)}'>
								<input type='hidden' id='masterName' name='masterName' value='${su:htmlEscape(masterName)}'>
							</form>
						</section>
					</c:if>
				</div><!-- main -->
				
			</div><!-- content -->
			<div id='dialog'></div>
			<div id='dialogMeisai'></div>
			<div id="push"></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>
var updateInfo = {};
var indexSequence = 0;
$(document).ready(function() {
	<c:if test='${masterId eq "kinyuukikan"}'>
	// 検索行数が0以上=検索結果の時、再度ボタンを押しても動くようにisValidを更新
	$("[name=isValid]").val($("#dataList").length > 0 ? "1" : "0");
	
	//銀行コード入力欄ロストフォーカス時、名称取得
	$("[name=ginkouCd]").change(function(){
		ginkouCd = $("input[name=ginkouCd]").val();

		// 一旦消す
		$("input[name=ginkouName]").val("");
		ginkouName = $("input[name=ginkouName]").val();

		//コード0パッディング
		ginkouCd = (ginkouCd == "" || ginkouCd.match(/[^0-9]/))
			? ""
			: ginkouCd = ("0000"  + ginkouCd).slice(-4);
		$("input[name=ginkouCd]").val(ginkouCd);

		//AJAXで取得
			if(ginkouCd != "") {
				$.ajax({
					type : "GET",
					url : "ginkou_sentaku_name_get",
					data : $.param({ginkouCd: ginkouCd, isMasterSearch: "1"}),
					dataType : 'text',
					success : function(response) {
						if (response == "") {
							$("[name=isValid]").val("0");
							alert("金融機関が不正です。");
						}

						$("[name=isValid]").val(response == "" ? "0" : "1");
						response = response.replace(new RegExp('exists$'), '');
						$("input[name=ginkouName]").val(response);
						if ("dialogCallbackRetGinkouSentaku" in window) {
							dialogCallbackRetGinkouSentaku();
						}
					}
				});
			}
			else
			{
				$("[name=isValid]").val("0");
			}
		});
	
	//銀行選択ボタン押下時、ダイアログ表示
	$("#ginkouSentakuButton").click(ginkouSentaku);
	
	//検索ボタン押下
	$("#kensakuButton").click(function(e) {
		$("[name=isSearch]").val("1");
		// コードのチェック
		if($("[name=ginkouCd").val() == "")
		{
			alert("金融機関を入力してください。");
			return;
		}

		if($("[name=ginkouName]").val() == "" && $("[name=isValid").val() != "1")
		{
			alert("金融機関が不正です。");
			return;
		}

		// 検索語のリロード処理
		$("#myForm").attr("action", "master_data_shoukai_search");
		$("#myForm").attr("method", "get");
		$("#myForm").submit();
	});
	</c:if>
	
	//変更ボタン押下
	$("#henkouButton").click(function() {
		$("#henkouForm").submit();
	});
<c:if test='${editable}'>
	//明細追加ボタン
	$("#lineAddButton").click(function() {
		lineDialogOpen("");
	});
	//明細変更ボタン
	$("button[name=lineEditButton]").each(function(ii, obj) {
		$(obj).click(function() {
			var index = $(obj).closest("tr").attr("data-index");
			lineDialogOpen(index);
		});
	});

	//変更登録
	$("#registButton").click(function() {
		if (0 >= Object.keys(updateInfo).length) {
			alert("マスター情報が変更されていません。");
			return ;
		}
		if (!window.confirm("変更した情報を反映します。よろしいですか？")) {
			return ;
		}
		$.ajaxSetup({traditional : true});
		$("#dialogMeisai").load(
			"master_data_koushin",
			getPostRegistData(),
			function() {
				var code = $(this).find("#returnCode").val();
				switch (code) {
				case	"0":
					// success
					break;
				case	"1":
					// エラーメッセージ表示
					alert($(this).find("#errorMessage").val());
					return ;
				default:
					// 例外error
					window.location.href = "illegalRequestError";
					return ;
				}

				// 変更要求送信
				$("#registForm").submit();
			}
		);
	});

	// 初期インデックス最大値を取得する
	var lastTr = $("#dataList tr:last");
	if (null != lastTr && lastTr.length > 0) {
		indexSequence = parseInt(lastTr.attr("data-index")) + 1;
	}

</c:if>
	sortTable()
});

/**
 * 銀行選択ボタン押下時Function
 */
function ginkouSentaku() {
	dialogRetGinkouCd = $("[name=ginkouCd]");
	dialogRetGinkouName = $("[name=ginkouName]");
	dialogCallbackRetGinkouSentaku = function() {
		$("[name=isValid]").val("1");
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
		title: "銀行選択",
		buttons: {閉じる: function()
			 {
			 	// 前のisValidを記憶
			 	var isValid = $("[name=isValid]").val();
			 	$(this).dialog("close");

			 	// closeでクリアされてしまうようなので、再代入
			 	$("[name=isValid]").val(isValid);
		 	}}
	})
	.load("ginkou_sentaku?isMasterSearch=1");
}

// テーブルをソートする
function sortTable() {
	if( $("#masterIdForShow").text() == "furikomi_bi_rule_hi" ){
		//「振込日ルール」に限りソート処理を調整
		var trlist = $("#dataList tr").map(function() {
			return $(this);
		}).get();
		trlist.sort(function(a, b) {
			var regExp = new RegExp("[^0-9]","g") ;
			var _a = a.attr("data-pkstr");
			var _b = b.attr("data-pkstr");
			//内部処理用の文字列が含まれているため数値のみのデータに変換
			if (null != _a) _a = parseInt(a.attr("data-pkstr").replace(regExp,""));
			if (null != _b) _b = parseInt(b.attr("data-pkstr").replace(regExp,""));
			if (null == _a && null == _b) {
				_a = a.attr("data-orgPkstr");
				_b = b.attr("data-orgPkstr");
				if (null != _a) _a = parseInt(a.attr("data-orgPkstr").replace(regExp,""));
				if (null != _b) _b = parseInt(b.attr("data-orgPkstr").replace(regExp,""));
			}
			if ( _a == _b ){
				return 0;
			}
			if (null == _a) {
				return 1;
			}
			if (null == _b) {
				return -1;
			}
			return ( _a > _b) ? 1 : -1;
		});
		$("#dataList").append(trlist);
		
	}else{
		var trlist = $("#dataList tr").map(function() {
			return $(this);
		}).get();
		trlist.sort(function(a, b) {
			var _a = a.attr("data-pkstr");
			var _b = b.attr("data-pkstr");
			if (null == _a && null == _b) {
				_a = a.attr("data-orgPkstr");
				_b = b.attr("data-orgPkstr");
				return _a.localeCompare(_b);
			}
			if (null == _a) {
				return 1;
			}
			if (null == _b) {
				return -1;
			}
			return _a.localeCompare(_b);
		});
		$("#dataList").append(trlist);
		
	}
	
}

// POST用のデータを生成する
function getPostRegistData() {
	var postData = {};

	// 基礎情報の追加
	postData.masterId = $("#masterId").val();
	postData.masterName = $("#masterName").val();
	postData.version = $("#version").val();

	var types = [];
	var keys  = [];
	var names = null;
	var count = 0;
	for (var index in updateInfo) {
		var info = getLineInfo(index);
		switch (info.type) {
		case	"add":
		case	"update":
		case	"delete":
			var values = [];
			var ns = [];
			for (var name in info.data) {
				if (names == null) {
					ns.push(name);
				}
				values.push(info.data[name]);
			}
			if (names == null) {
				names = ns;
			}
			postData["colv[" + count + "]"] = values;
			types.push(info.type);
			keys.push(getOrgPkstr(index));
			count++;
			break;
		default:
		case	"same":
			break;
		}
	}
	postData.count = count;
	postData.type  = types;
	postData.key   = keys;
	postData.name  = names;

	return postData;
}

//orgpkstrの取得
function getOrgPkstr(index) {
	var obj = getLineObj(index);
	var pkstr = obj.attr("data-orgPkstr");
	if (null == pkstr) {
		pkstr = obj.attr("data-pkstr");
	}
	return pkstr;
}

//Indexの存在確認
function getIndexForPkstr(pkstr) {
	return $("#dataList tr[data-pkstr=" + pkstr.selectorEscape() + "]").attr("data-index");
}

//TRを取得する
function getLineObj(index) {
	return $("#dataList tr[data-index=" + index.selectorEscape() + "]");
}

//最大Index+1の取得
function getNewIndex() {
	return indexSequence++;
}

//行情報を取得する
function getLineInfo(index) {
	var info = updateInfo[index];
	if (null != info) {
		return info;
	}
	return {
		type  : 'same',
		data  : getOrgLineInfo(index)
	};
}

//オリジナル行情報を取得する
function getOrgLineInfo(index) {
	var colNameAry = $("#physicalRow th").map(function() {return $(this).text();}).get();
	var colValueAry = getLineObj(index).find("td:not(:first)").map(function() {return $(this).attr('data-value');}).get();

	var ret = {};
	for (var ii = 0; ii < colNameAry.length; ii++) {
		ret[colNameAry[ii]] = colValueAry[ii];
	}
	return ret;
}

// 行に色を付ける
function setLineColor(index, type) {
	var tr = getLineObj(index);
	tr.removeClass("trupdate");
	tr.removeClass("trdelete");
	tr.removeClass("tradd");

	// 色設定
	switch (type) {
	case 'update':
		tr.addClass("trupdate");
		break;
	case 'delete':
		tr.addClass("trdelete");
		break;
	case 'add':
		tr.addClass("tradd");
		break;
	}
}

//行情報を設定する
function setLineInfo(index, info) {
	var colNameAry = $("#physicalRow th").map(function() {return $(this).text();}).get();
	var colTdAry = getLineObj(index).find("td:not(:first)").map(function() {return $(this);}).get();

	for (var ii = 0; ii < colNameAry.length; ii++) {
		//numeric型は右揃え・カンマ区切りで出力
		if($("#physicalRow th").eq(ii).attr('data-type').toLowerCase() == "numeric" ){
			$(colTdAry[ii]).attr('align','right');
			if("true" == $("#physicalRow th").eq(ii).attr('data-decimal')){
				$(colTdAry[ii]).text(String(info[colNameAry[ii]]));
			}else{
				$(colTdAry[ii]).text(String(info[colNameAry[ii]]).formatMoney());
			}
		}else{
			$(colTdAry[ii]).text(info[colNameAry[ii]].replace(/ /g, "\u00a0"));
		}
	}
}

// 行が追加行か確認する。
function isAddLine(index) {
	var info = updateInfo[index];
	if (null != info) {
		if ('add' == info.type) {
			return true;
		}
	}
	return false;
}

//行情報を追加する
function lineAdd(pkstr, info) {
	var index = getNewIndex();

	// 更新キャッシュに保存
	updateInfo[index] = {
		type : 'add',
		data : info
	};

	// 最下段に追加する
	var colNameAry = $("#physicalRow th").map(function() {return $(this).text();}).get();
	var tr = $("<tr/>").append($("<td/>").append($("<button type='button' name='lineEditButton' class='btn btn-mini'>変更</button>").click(
		function() {
			var _index = $(this).closest("tr").attr("data-index");
			lineDialogOpen(_index);
		}
	)));
	$(tr).attr("data-index", index);
	$(tr).attr("data-pkstr", pkstr);
	for (var ii = 0; ii < colNameAry.length; ii++) {
		//numeric型は右揃え・カンマ区切りで出力
		if($("#physicalRow th").eq(ii).attr('data-type').toLowerCase() == "numeric" ){
			if("true" == $("#physicalRow th").eq(ii).attr('data-decimal')){
				tr.append($("<td align='right'/>").text(String(info[colNameAry[ii]])));
			}else{
				tr.append($("<td align='right'/>").text(String(info[colNameAry[ii]]).formatMoney()));
			}
		}else{
			tr.append($("<td/>").text(info[colNameAry[ii]].replace(/ /g, "\u00a0")));
		}
	}
	$("#dataList").append(tr);
	$(tr).addClass("tradd");

	sortTable();
}

//行情報を更新する
function lineUpdate(index, pkstr, info) {
	var _pkstr = getLineObj(index).attr("data-pkstr");
	if (isAddLine(index)) {
		// 追加キャッシュに保存
		updateInfo[index] = {
			type : 'add',
			data : info
		};
		// 表示を変更する
		setLineInfo(index, info);
	} else {
		// 更新キャッシュに保存
		updateInfo[index] = {
			type : 'update',
			data : info
		};
		// 表示を変更する
		setLineInfo(index, info);

		// 色設定
		setLineColor(index, 'update');
	}
	getLineObj(index).attr("data-pkstr", pkstr);

	if (pkstr != _pkstr) {
		sortTable();
	}
}

//行情報を削除する
function lineDelete(index, info) {
	if (isAddLine(index)) {
		var tr = getLineObj(index);
		tr.remove();
		delete updateInfo[index];
	} else {
		// 更新キャッシュに保存
		updateInfo[index] = {
			type : 'delete',
			data : info
		};

		// 色設定
		setLineColor(index, 'delete');
		getLineObj(index).removeAttr("data-pkstr");

		sortTable();
	}
}

//行情報を元に戻す
function lineUndo(index, info) {
	if (isAddLine(index)) {
		lineDelete(index, info);
	} else {
		// 戻せるかチェックする
		var tr = getLineObj(index);
		var dupIndex = getIndexForPkstr(tr.attr("data-orgPkstr"));
		if (null != dupIndex && index != dupIndex) {
			return false;
		}

		// 更新キャッシュを確認
		var info = updateInfo[index];
		if (null != info) {
			delete updateInfo[index];
		}
		// 表示を変更する
		setLineInfo(index, getOrgLineInfo(index));

		// 色設定
		setLineColor(index, 'default');

		var isSort = getOrgPkstr(index) != tr.attr("data-pkstr");
		tr.attr("data-pkstr", getOrgPkstr(index));

		if (isSort) {
			sortTable();
		}
	}
	return true;
}

//明細ダイアログを開く
function lineDialogOpen(index) {
	var dialog = null;
	var parent = $("#dialogMeisai");
	parent.children().remove();

	var registFunction = function(type) {
		var info = getDialogInfo();
		switch (type) {
		case 'delete':
			lineDelete(index, info);
			dialog.dialog("close");
			isDirty = true;
			return ;
		case 'undo':
			if (!lineUndo(index, info)) {
				setInputError("プライマリーキーが重複しています。");
				return ;
			}
			dialog.dialog("close");
			isDirty = true;
			return ;
		}
		$.ajaxSetup({traditional : true});
		dialog.load(
			"master_data_edit_confirm",
			getPostDialogInfo(),
			//callback
			function() {
				// 更新ページ表示チェック
				var exceptionPage = $(this).find("#exceptionPage").val();
				if (null == exceptionPage) {
					exceptionPage = "systemError";
				}
				if ("" != exceptionPage) {
					window.location.href = exceptionPage;
					return ;
				}
				// 入力チェック状況確認
				if ("0" != $(this).find("#errorCnt").val()) {
					setDialogInfo(info);
					return ;
				}
				var pkstr = $(this).find("#pkstr").val();
				var dupIndex = getIndexForPkstr(pkstr);
				if (null != dupIndex && index != dupIndex) {
					setInputError("プライマリーキーが重複しています。");
					setDialogInfo(info);
					return ;
				}

				$(this).dialog("close");
				isDirty = true;
				switch (type) {
				case 'update':
					lineUpdate(index, pkstr, info);
					break;
				case 'add':
					lineAdd(pkstr, info);
					break;
				}
			}
		);
	};

	var buttons = {};
	if ("" == index) {
		buttons["追加"] = function() {
			registFunction('add');
		};
	} else {
		buttons["変更"] = function() {
			registFunction('update');
		};
		buttons["削除"] = function() {
			registFunction('delete');
		};
		buttons["元に戻す"] = function() {
			registFunction('undo');
		};
	}
	buttons["閉じる"] = function() {
		$(this).dialog("close");
	};

	dialog = parent.dialog({
		modal: true,
		width: "600",
		height: "600",
		title: "マスターデータ編集",
		appendTo:"#content",
		buttons: buttons,
		close: function() {
			parent.children().remove();
		}
	});
	dialog.load("master_data_edit_init", { masterId : $("#masterId").val() }, function() {
		// 表示完了チェック
		var exceptionPage = $(this).find("#exceptionPage").val();
		if (null == exceptionPage) {
			exceptionPage = "systemError";
		}
		if ("" != exceptionPage) {
			window.location.href = exceptionPage;
			return ;
		}
		// 値の設定
		if ("" == index) {
			setDialogInfo(null);
		} else {
			setDialogInfo(getLineInfo(index).data);
		}
	});
}
		</script>
	</body>
</html>
