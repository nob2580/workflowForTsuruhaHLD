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
		<title>インフォメーション一覧｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
  				<h1>インフォメーション一覧</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='buttonForm' method='post' action='information_ichiran_sakujo'>

					<!-- 処理ボタン -->
					<section>
						<button type='button' id='tsuikaButton' class='btn add'><i class='icon-plus'></i> インフォメーション追加</button>
						<button type='button' id='sakujoButton' class='btn delete'><i class='icon-trash'></i> 削除</button>
						<button type='button' id='orderSaveButton' class='btn non-print'><i class='icon-hdd'></i> 並び順の保存</button>
						<a href='information_ichiran?sortKbn=3&status=${su:htmlEscape(status)}'><button type="button" class='btn non-print'><i class='icon-search'></i>並び順で表示</button></a>
					</section>

					<!-- 検索フィールド -->
					<section>
						<div>
							<div class="form-horizontal">
								<select name='statusSelect' class='input-small'>
<c:forEach var="statusList" items="${infoStatusList}">
									<option value='${su:htmlEscape(statusList.naibu_cd)}' <c:if test='${su:htmlEscape(statusList.naibu_cd) eq status}'>selected</c:if>>${su:htmlEscape(statusList.name)}</option>
</c:forEach>
								</select>
							</div>
						</div>
					</section>

					<!-- 一覧 -->
					<section>
						<div class='no-more-tables'>
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th style="text-align:center;">#<br>　</th>
										<th style="text-align:center;"><input type='checkbox' id='allcheck'><br>　</th>
										<th>ステータス<br>　</th>
										<th>掲示期間<br><a href='information_ichiran?sortKbn=1&status=${su:htmlEscape(status)}'>▲</a>    <a href='information_ichiran?sortKbn=2&status=${su:htmlEscape(status)}'>▼</a></th>
										<th style="max-width:300px;">掲示内容<br>　</th>
										<th></th>
									</tr>
								</thead>
<c:if test="${fn:length(ichiranList) > 0}" >
								<tbody>
<c:forEach var="record" items="${ichiranList}">
									<tr class="${su:htmlEscape(record.bkclrset)}">									
										<td style="text-align: center;">
											<a href='information_henkou?informationId=${su:htmlEscape(record.informationId)}&pageNo=${su:htmlEscape(pageNo)}&sortKbn=${su:htmlEscape(sortKbn)}&status=${su:htmlEscape(status)}'>${su:htmlEscape(fn:substring(record.informationId,0,7))}<br>${su:htmlEscape(fn:substring(record.informationId,8,14))}</a>
											<input type="hidden" name="informationIdList" value="${su:htmlEscape(record.informationId)}">
											<input type="hidden" name="statusNameList" value="${su:htmlEscape(record.statusName)}">
										</td>
										<td style="text-align: center;"><input type='checkbox' name='infoSentakuIdList' value='${su:htmlEscape(record.informationId)}'></td>
										<td>${su:htmlEscape(record.statusName)}</td>
										<td>${su:htmlEscape(record.keijikikanFrom)} ～ ${su:htmlEscape(record.keijikikanTo)}</td>
										<td style="word-wrap:break-word; word-break:break-all;">${su:htmlEscapeBrLink(record.tsuuchinaiyou)}</td>
										<td >
											<button type='button' name='rowUp'   class='btn btn-mini' data-id='${su:htmlEscape(record.informationId)}'>↑</button>
											<button type='button' name='rowDown' class='btn btn-mini' data-id='${su:htmlEscape(record.informationId)}'>↓</button>
										</td>
									</tr>
</c:forEach>
								</tbody>
</c:if>
							</table>
						</div>
						<input type="hidden" name="pageNo" value="${su:htmlEscape(pageNo)}">
						<input type="hidden" name="sortKbn" value="${su:htmlEscape(sortKbn)}">
						<input type="hidden" name="status" value="${su:htmlEscape(status)}">
						<div class='blank'></div>
						<%@ include file="/jsp/eteam/include/Paging.jsp" %>
					</section>
				</form></div><!-- main -->
			</div> <!-- /container -->
			<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<!-- スクリプト -->
		<script style='text/javascript'>

$(document).ready(function(){

	//追加ボタン押下
	$("#tsuikaButton").click(function(){
		location.href ="information_tsuika?pageNo=${su:htmlEscape(pageNo)}&sortKbn=${su:htmlEscape(sortKbn)}&status=${su:htmlEscape(status)}";
	});

	//削除ボタン押下
	$("#sakujoButton").click(function(){
		var flg = false;
		$("[name='infoSentakuIdList']:checked").each(function(){
			// チェックボックスが選択されていればtrue
			flg = true;
		});
		if (!flg) {
			alert("削除対象が選択されていません。");
			return;
		}
		if (window.confirm('削除してよろしいですか？')){
			$("#buttonForm").submit();
		}
	});

	//並び順の保存ボタン押下
	$("#orderSaveButton").click(function(){
		var msg = "並び順を保存します。\n";
		msg += "対象は現在表示ページのステータスが未掲載または掲載中のレコードです。";
	
		if (window.confirm(msg)){
			$("#buttonForm").attr("action", "information_ichiran_order_save");
			$("#buttonForm").submit();
		}
	});
	
	//ステータス変更時
	$("select[name=statusSelect]").change(function(){
		var pram = $(this).val();
		location.href = "information_ichiran?sortKbn=${su:htmlEscape(sortKbn)}&status=" + pram;
	});

	//全選択チェックボックスチェック時Function
	$("#allcheck").click(function(){
		$("input[type='checkbox']").prop('checked', $(this).prop("checked"));
	});
	
	//上ボタン
	$("button[name=rowUp]").click(function(){
		var tr = $(this).closest("tr");
		
		// 1行目はイベント実行されないように制御します。
		if (tr[0].rowIndex == 1) return;
		
		if(tr.prev()){
			tr.insertBefore(tr.prev());
		}
	});

	//下ボタン押下
	$("button[name=rowDown]").click(function(){
		var tr = $(this).closest("tr");
		
		// 最終行のDOWNイベントは実行されないように制御します。
		if (tr[0].rowIndex ==  $("tr:last")[0].rowIndex) return;
		
		if(tr.next()){
			tr.insertAfter(tr.next());
		}
	});
});
		</script>
	</body>
</html>
