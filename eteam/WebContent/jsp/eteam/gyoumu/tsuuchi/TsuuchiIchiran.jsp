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
		<title>通知一覧｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
  				<h1>通知一覧</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' class='form-horizontal'>
					<input type="hidden" name="pageNo" value="${su:htmlEscape(pageNo)}">
					<input type="hidden" name="sortKbn" value="${su:htmlEscape(sortKbn)}">

					<!-- 検索条件 -->
					<section>
						<select name='statusSelect' class='input-small'>
<c:forEach var="record" items="${statusList}">
									<option value='${su:htmlEscape(record.naibu_cd)}' <c:if test='${record.naibu_cd eq statusSelect}'>selected</c:if>>${su:htmlEscape(record.name)}</option>
</c:forEach>
						</select>
					</section>

					<!-- 一覧表示 -->
					<section>
						<div>
							<button type='button' class='btn' onclick="koushin('1')"><i class='icon-check'></i> 確認済</button>
							<button type='button' class='btn' onclick="koushin('0')"><i class='icon-bookmark'></i> 未確認</button>
						</div>
						<div class='blank'></div>
						<div id="List" class='no-more-tables'>
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th style="text-align:center;">#<br>　</th>
										<th style="text-align:center;"><input type='checkbox' id='allcheck'><br>　</th>
										<th>伝票種別<br>　</th>
										<th>伝票ID<br><a href='tsuuchi_ichiran?pageNo=1&sortKbn=3&statusSelect=${statusSelect}'>▲</a>    <a href='tsuuchi_ichiran?pageNo=1&sortKbn=4&statusSelect=${statusSelect}'>▼</a></th>
										<th>通知件名<br><a href='tsuuchi_ichiran?pageNo=1&sortKbn=1&statusSelect=${statusSelect}'>▲</a>    <a href='tsuuchi_ichiran?pageNo=1&sortKbn=2&statusSelect=${statusSelect}'>▼</a></th>
										<th style="width:300px;">コメント<br>　</th>
										<th>通知日時<br><a href='tsuuchi_ichiran?pageNo=1&sortKbn=5&statusSelect=${statusSelect}'>▲</a>    <a href='tsuuchi_ichiran?pageNo=1&sortKbn=6&statusSelect=${statusSelect}'>▼</a></th>
									</tr>
								</thead>
								<tbody>
<c:forEach var="record" items="${tsuuchiList}" varStatus="status">
									<tr class="${su:htmlEscape(record.bg_color)}">
										<td style="text-align:center;">${status.index + 1}</td>
										<td style="text-align:center;"><input type='checkbox'id='${su:htmlEscape(record.serial_no)}' name='kidoku_flg' value='1'/></td>
										<td>${su:htmlEscape(record.denpyou_shubetsu)}
										<td>
											<c:choose>
												<c:when test='${record.version >= 1}'>
													<a href='${su:htmlEscape(record.denpyou_shubetsu_url)}?denpyouKbn=${su:htmlEscape(record.denpyou_kbn)}&denpyouId=${su:htmlEscape(record.denpyou_id)}&version=${su:htmlEscape(record.version)}' target='_blank'>${su:htmlEscape(record.denpyou_id)}</a>
												</c:when>
												<c:when test='${!record.zaimu_kyoten_nyuryoku_pattern_no.isEmpty()}'>
													<a href='${su:htmlEscape(record.denpyou_shubetsu_url)}?denpyouKbn=${su:htmlEscape(record.denpyou_kbn)}&patternNo=${su:htmlEscape(record.zaimu_kyoten_nyuryoku_pattern_no)}&denpyouId=${su:htmlEscape(record.denpyou_id)}' target='_blank'>${su:htmlEscape(record.denpyou_id)}</a>
												</c:when>
												<c:otherwise>
													<a href='${su:htmlEscape(record.denpyou_shubetsu_url)}?denpyouKbn=${su:htmlEscape(record.denpyou_kbn)}&denpyouId=${su:htmlEscape(record.denpyou_id)}' target='_blank'>${su:htmlEscape(record.denpyou_id)}</a>
												</c:otherwise>
											</c:choose>
										</td>
										<td>伝票${su:htmlEscape(record.joukyou)}</td>
										<td style='word-break:break-all;'>${su:htmlEscape(record.comment)}</td>
										<td>${su:htmlEscape(record.touroku_time)}</td>
									</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
						<div class='blank'></div>
						<%@ include file="/jsp/eteam/include/Paging.jsp" %>
					</section>
				</form></div><!-- main -->
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
		var param = $("select[name=statusSelect]").val();
		location.href = 'tsuuchi_ichiran?pageNo=1&sortKbn=${sortKbn}&statusSelect=' + param;
	});

	//一括選択
	$("#allcheck").click(function(){	
		$("input[type='checkbox']").prop('checked', $(this).prop("checked"));
	});
});

/**
 * 既読ボタン・未読ボタン押下時Function
 */
function koushin(btnFlg) {
	
	var array = new Array();
	var i = 0;
	var flg = false;
	$("[name='kidoku_flg']:checked").each(function() {
		flg = true;
		array[i] = $(this).attr("id");
		i++;
	});
	
	if (flg) {
		$("#myForm").attr('action', 'tsuuchi_ichiran_koushin?serialNo=' + array + '&mode=' + btnFlg);
		$("#myForm").submit();
	}
}
		</script>
	</body>
</html>
