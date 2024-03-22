<%@page import="eteam.common.JspUtil"%>
<%@ page import="eteam.symbol.EteamSymbol"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>
				<!-- 添付ファイルのプレビュー -->
				<!-- モバイル対応として、UAでも弾く。パフォーマンスを加味するなら、本当はファイル関連データ処理を行わずにJavaクラスに何とかさせたいが、一旦それは保留 -->
				<c:set var="userAgent" value="${header['User-Agent']}" scope="session"/>
				<c:set var="length" value="${fn:length(tenpuFileName)}" />
				<c:set var="isPreviewTargetDenpyou" value="${isPreviewTargetDenpyou}" />
				<c:if test='${isPreviewTargetDenpyou && length > 0 && !fn:contains(userAgent, "iPhone") && !fn:contains(userAgent, "Android") && !fn:contains(userAgent, "Windows Phone") && !fn:contains(userAgent, "BlackBerry") && !fn:contains(userAgent, "Mobile") && !fn:contains(userAgent, "Tablet")}'>
					<li class = "filePreviewCell non-print" style="margin-left:20px; padding-right:9px; min-width:600px;">
					<c:forEach var="i" begin="0" end="${fn:length(tenpuFileName) - 1}" step="1">
						<!-- 添付ファイル名 -->
						<input type="hidden" name='tenpuFileName' value='${su:htmlEscape(tenpuFileName[i])}'>
						<!-- 添付ファイル拡張子 -->
						<input type="hidden" name='tenpuFileExtension' value='${su:htmlEscape(tenpuFileExtension[i])}'>
						<!-- 添付ファイル表示名 -->
						<input type="hidden" name="tenpuFileDisplayName" value='${su:htmlEscape(tempFileEbunshoList.get(i).file_name)}'>
					</c:forEach>
					<c:set var="currentIndex" value="0" />
					<c:set var="maxIndex" value="${fn:length(tenpuFileName) - 1}" />
					<!-- レイアウト調整用divタグ。 -->
						<div style="position: sticky; top: 50px; background-color: white; width: 600px; padding: 5px;">
							<p id="previewPageCount" style="display:inline-block;white-space:nowrap;margin-inline-end:20px;">(${currentIndex + 1} / ${su:htmlEscapeBr(fn:length(tenpuFileName))}) ${su:htmlEscape(tempFileEbunshoList.get(currentIndex).file_name)}</p><br>
							<button type="button" id="btnPreviousFile" class="btn" onclick="previewPreviousFile(${currentIndex}, ${maxIndex})" style="left: 10px; font-size: 14px;"><i class="icon-arrow-left"></i> 前へ</button>
							<button type="button" id="btnNextFile" class="btn" onclick="previewNextFile(${currentIndex}, ${maxIndex})" style="left: 90px;  font-size: 14px;">次へ <i class="icon-arrow-right"></i></button>
						</div>
						<!-- レイアウト調整用divタグ -->
						<div style="position: sticky; top: 115px; min-height: 900px; background-color:white;">
							<!-- pdf -->
							<embed id="pdfPreview" src="${su:htmlEscape(scheme)}://${su:htmlEscape(host)}/eteam/static/js/pdfjs-dist/web/viewer.html?file=${su:htmlEscape(scheme)}://${su:htmlEscape(host)}/eteam/static/tmp/${su:htmlEscape(tenpuFileName[currentIndex])}.${su:htmlEscape(tenpuFileExtension[currentIndex])}#pagemode=none" width="600px" height="900px" style="position:sticky; top:50px; <c:if test="${tenpuFileExtension[currentIndex].toLowerCase() ne 'pdf'}">display:none;</c:if>">
							
							<!-- bmp, jpg/jpeg, png -->
							<img id="imagePreview" src="${su:htmlEscape(scheme)}://${su:htmlEscape(host)}/eteam/static/tmp/${su:htmlEscape(tenpuFileName[currentIndex])}.${su:htmlEscape(tenpuFileExtension[currentIndex])}" style="position:sticky; top:50px; min-height: 900px; max-height: 900px; object-fit: contain; object-position: top center; <c:if test="${tenpuFileExtension[currentIndex].toLowerCase() ne 'bmp' && tenpuFileExtension[currentIndex].toLowerCase() ne 'jpg' && tenpuFileExtension[currentIndex].toLowerCase() ne 'jpeg' && tenpuFileExtension[currentIndex].toLowerCase() ne 'png'}">display:none;</c:if>">
							<!-- プレビュー対象外 -->
							<div id="previewError" style="width:600px; height:900px; position:sticky; top:50px; text-align:center; align-items: center; background-color: gray; <c:if test="${tenpuFileExtension[currentIndex].toLowerCase() eq 'pdf' || tenpuFileExtension[currentIndex].toLowerCase() eq 'bmp' || tenpuFileExtension[currentIndex].toLowerCase() eq 'jpg' || tenpuFileExtension[currentIndex].toLowerCase() eq 'jpeg' || tenpuFileExtension[currentIndex].toLowerCase() eq 'png'}">display:none;</c:if>">
									<br><p>プレビュー対象外のファイルです。</p><br>
									<p>ご参照いただくには、以下のリンクからダウンロード（名前を付けてリンク先を保存<!-- テキストなどはそのまま開けてしまうため -->）してください。</p><br>
									<a id="previewErrorLink" href="${su:htmlEscape(scheme)}://${su:htmlEscape(host)}/eteam/static/tmp/${su:htmlEscape(tenpuFileName[currentIndex])}.${su:htmlEscape(tenpuFileExtension[currentIndex])}">リンク</a>
							</div>
						</div>
					</li>
				</c:if>