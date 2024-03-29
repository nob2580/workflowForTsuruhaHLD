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
		<title>振込明細ダウンロード｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>振込明細ダウンロード</h1>
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				
				<!-- メイン -->
				<div id='main'>
				<form id='csvDownloadForm' name='csvDownloadForm' class='form-horizontal'>
					<section class='print-unit'>
						<div>
							<div class='control-group'>
								<label class='control-label' for=''><span class='required'>*</span>年月</label>
								<div class='controls'>
									<select id='nengetsu' name='nengetsu' class='input-medium'>
										<option></option>
										<c:forEach var='nengetsuList' items='${nengetsuList}'>
											<option value='${su:htmlEscape(nengetsuList.naibu_cd)}' <c:if test='${nengetsuList.naibu_cd eq nengetsu}'>selected</c:if>>${su:htmlEscape(nengetsuList.name)}</option>
										</c:forEach>
									</select>
									<span>※直近２ヶ月</span>
								</div>
							</div>
						</div>
						<div>
							<div class='control-group'>
								<label class='control-label' for=''><span class='required'>*</span>対象ファイル</label>
								<div class='controls'>
									<select id='taishouFile' name='taishouFile' class='input-xlarge'>
										<option></option>
										<c:forEach var='taishouFileList' items='${taishouFileList}'>
											<option value='${su:htmlEscape(taishouFileList.naibu_cd)}' <c:if test='${taishouFileList.naibu_cd eq taishouFile}'>selected</c:if>>${su:htmlEscape(taishouFileList.name)}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
					</section>
					<span class='non_print'>
						<button type='button' id='downloadButton' class='btn csvsyutsuryoku'><i class='icon-download'></i> ダウンロード</button>
					</span>
				</form>
				</div><!-- main -->
				

			</div><!-- content -->
			<div id='push'></div>
			</div><!-- wrap -->
			<!-- フッター -->
			<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		

		<%@ include file="/jsp/eteam/include/Script.jsp" %>

		<script style='text/javascript'>

$(document).ready(function(){

	//年月プルダウン")
	$("#nengetsu").change(function(e){
		$("form[name=csvDownloadForm]").attr("action", "csv_download_filelist");
		$("form[name=csvDownloadForm]").attr("method", "post");
		$("form[name=csvDownloadForm]").submit();
	});

	//ダウンロード
	$("#downloadButton").click(function(e){
		$("form[name=csvDownloadForm]").attr("action", "csv_download_download");
		$("form[name=csvDownloadForm]").attr("method", "post");
		$("form[name=csvDownloadForm]").submit();
	});
	
});

		</script>
	</body>
</html>
