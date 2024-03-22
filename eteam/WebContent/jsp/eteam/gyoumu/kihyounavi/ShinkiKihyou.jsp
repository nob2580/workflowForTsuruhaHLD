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
		<title>新規起票｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
  				<h1>新規起票</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'>
					<div class="controls form-inline">
						<label class="control-label">伝票種別絞り込み</label>
						<input type="text" id="keyword">
					</div>
					<section>
						<div id="List" class='no-more-tables'>
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>#</th>
										<th style="min-width:100px">業務種別</th>
										<th style="min-width:100px">伝票種別</th>
										<th>内容</th>
									</tr>
								</thead>
								<tbody id="shubetsuSentakuBody">
<c:forEach var="record" items="${list}" varStatus="i">
									<tr>
										<td style='text-align:center'>${su:htmlEscape(record.gyouNo)}</td>
										<td>
<c:if test="${(i.index == 0) or list[i.index - 1].gyoumuShubetsu != record.gyoumuShubetsu}">
											${su:htmlEscape(record.gyoumuShubetsu)}
</c:if>
										</td>
										<td>
											<input type='hidden' name='gyoumuShubetsu' value='${su:htmlEscape(record.gyoumuShubetsu)}'>
											<c:choose>
												<c:when test='${record.version >= 1}'>
													<a href='${su:htmlEscape(record.denpyouShubetsuUrl)}?denpyouKbn=${su:htmlEscape(record.denpyouKbn)}&version=${su:htmlEscape(record.version)}' target='_blank'>${su:htmlEscape(record.denpyouShubetsu)}</a>
												</c:when>
												<c:otherwise>
													<a href='${su:htmlEscape(record.denpyouShubetsuUrl)}?denpyouKbn=${su:htmlEscape(record.denpyouKbn)}' target='_blank'>${su:htmlEscape(record.denpyouShubetsu)}</a>
												</c:otherwise>
											</c:choose>
										</td>
										<td style="word-wrap:break-word; word-break:break-all;">${su:htmlEscapeBrLink(record.naiyou)}</td>
									</tr>
</c:forEach>
								</tbody>
							</table>
						</div>
					</section>


				</div><!-- main -->
			</div> <!-- /container -->
			<div id="push"></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<!-- スクリプト -->
		<script style='text/javascript'>
<!-- 

/**
 * 検索ワード変更時
 */
$("#keyword").change(function(){
	//検索
	shubetsuSentakuSearch();
});

/**
 * enterキー押下時動作
 */
$("#keyword").keypress(function (e) {
	if (e.which == 13) {
	  shubetsuSentakuSearch();
	}
});

/**
 * 分類とテキストで絞り込む
 */
function shubetsuSentakuSearch() {
	var word = $("#keyword").val();
	//該当するものだけ表示
	$("#shubetsuSentakuBody").find("tr").css("display", "none");
	$("#shubetsuSentakuBody").find("tr").each(function(){
		if (
			("" == word || (
				wordSearch($(this).find("input[name=gyoumuShubetsu]").val(), word) ||
				wordSearch($(this).find("td").eq(2).text(), word) ||
				wordSearch($(this).find("td").eq(3).text(), word)
			))
		) {
			$(this).css("display", "");
		}
	});
	clearDuplicateGyoumuShubetsu();
};

/**
 * 業務種別が一つ上の行のものと同じ場合、該当行の業務種別表示をブランクとする
 */
function clearDuplicateGyoumuShubetsu() {
	
	//表示中の一つ上の行gyoumuShubetsuを参照
	//処理中行のそれと一致していればブランク化、違うならgyoumuShubetsu表示
	
	var prevVal = "";
	$("#shubetsuSentakuBody").find("tr").each(function(){
		if($(this).css("display") == "none"){return true;}
		var shubetsuVal = $(this).find("input[name=gyoumuShubetsu]").val()
		if (shubetsuVal == prevVal){
			 $(this).find("td").eq(1).text("");
		}else{
			 $(this).find("td").eq(1).text(shubetsuVal);
		}
		prevVal = shubetsuVal;
	});
};


// -->
		</script>
	</body>
</html>
