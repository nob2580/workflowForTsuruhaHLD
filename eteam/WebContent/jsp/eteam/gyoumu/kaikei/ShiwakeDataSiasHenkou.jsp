<%@page import="eteam.gyoumu.kaikei.ShiwakeDataSiasHenkouAction"%>
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
		<title>仕訳データ変更｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
			/** ラベル用(共通CSSを上書きする) */
			.form-horizontal .control-label {
				width: 300px; /*元→230px*/
				margin-right:10px;
			}
		</style>
	</head>
	<body>
		<div id='wrap'>
			<!-- ヘッダ -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>
			<!-- 中身 -->
			<div id='content' class='container'>
				<h1>仕訳データ変更</h1>
				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				
				<div id='main'>
				<form id='myForm' class='form-horizontal' method='post'>
				<section>
					<a href='${su:htmlEscape(backDenpyouUrl)}'>戻る</a>
					<div class='control-group'>
						<label class='control-label'>伝票ID</label>
						<div class='controls'>
							<input type='text' name='denpyouId' class='input-inline input-large' value='${su:htmlEscape(denpyouId)}' disabled>
						</div>
					</div>
					<input type='hidden' name='serialNo' value='${su:htmlEscape(serialNo)}'>
					<div class='control-group'>
	<c:forEach var="record" items="${serialNoList}">
			<c:if test='${serialNo != record.serial_no }'>
						<a href='shiwake_data_sias_henkou?denpyouId=${su:htmlEscape(denpyouId)}&serialNo=${su:htmlEscape(record.serial_no)}'>${su:htmlEscape(record.serial_no)}（${su:htmlEscape(record.shiwake_status_nm)}）</a>
			</c:if>
			<c:if test='${serialNo == record.serial_no }'>
						${su:htmlEscape(record.serial_no)}（${su:htmlEscape(record.shiwake_status_nm)}）
			</c:if>
	</c:forEach>
					</div>
				</section>
				<section id='data'>
					<p>仕訳用データを直接変更してください。</p>
					<div class='control-group'>
						<label class='control-label'>伝票日付</label>
						<div class='controls'>
							<input type='text' name='denpyouDate' class='input-small datepicker' value='${su:htmlEscape(denpyouDate)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>整理月フラグ</label>
						<div class='controls'>
							<input type='text' name='seiriTukiFlg' class='input-mini' maxlength='1' value='${su:htmlEscape(seiriTukiFlg)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>伝票番号</label>
						<div class='controls'>
							<input type='text' name='denpyouNo' class='input-medium' maxlength='8' value='${su:htmlEscape(denpyouNo)}'>
						</div>
					</div>
					<c:forEach var="i" begin="0" end="<%=ShiwakeDataSiasHenkouAction.HF_NUM - 1 %>" step="1">
						<div class='control-group' <c:if test='${"0" eq hfShiyouFlgArr[i]}'>style='display:none;'</c:if>>
							<label class='control-label'>ヘッダーフィールド${numArr[i]}</label>
							<div class='controls'>
								<input type='text' name='headerFieldCd[${i}]' class='input-xlarge' maxlength='20' value='${su:htmlEscape(headerFieldCd[i])}'>
								<input type='text' name="headerFieldName[${i}]" class='input-xlarge' disabled value='${su:htmlEscape(headerFieldName[i])}' <c:if test='${"0" eq hfShiyouFlgArr[i] or "1" eq hfShiyouFlgArr[i]}'>style='display:none;'</c:if>>
								<button type='button' id='kashiUfSentakuButton' class='btn btn-small' onClick = 'sentakuBotton("hf", ${i}, ${hfMappingArr[i]})' <c:if test='${"0" eq hfShiyouFlgArr[i] or "1" eq hfShiyouFlgArr[i]}'>disabled</c:if> >選択</button>
							</div>
						</div>
					</c:forEach>
					<div class='control-group'>
						<label class='control-label'>借方　部門コード</label>
						<div class='controls'>
							<input type='text' name='kariBumonCd' class='input-medium' maxlength='8' value='${su:htmlEscape(kariBumonCd)}'>
							<input type='text' name='kariBumonName' class='input-xlarge' disabled value='${su:htmlEscape(kariBumonName)}' >
							<button type='button' id='kariBumonSentakuButton' class='btn btn-small' >選択</button>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>借方　取引先コード</label>
						<div class='controls'>
							<input type='text' name='kariTorihikisakiCd' class='input-medium' maxlength='12' value='${su:htmlEscape(kariTorihikisakiCd)}'>
							<input type='text' name='kariTorihikisakiName' class='input-xlarge' disabled value='${su:htmlEscape(kariTorihikisakiName)}' >
							<button type='button' id='kariTorihikisakiSentakuButton' class='btn btn-small' >選択</button>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>借方　科目コード</label>
						<div class='controls'>
							<input type='text' name='kariKamokuCd' class='input-medium' maxlength='6' value='${su:htmlEscape(kariKamokuCd)}'>
							<input type='text' name='kariKamokuName' class='input-xlarge' disabled value='${su:htmlEscape(kariKamokuName)}'>
							<button type='button' id='kariKamokuSenakuButton' class='btn btn-small'>選択</button>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>借方　枝番コード</label>
						<div class='controls'>
							<input type='text' name='kariEdabanCd' class='input-medium' maxlength='12' value='${su:htmlEscape(kariEdabanCd)}'>
							<input type='text' name='kariEdabanName' class='input-xlarge' disabled value='${su:htmlEscape(kariEdabanName)}'>
							<button type='button' id='kariEdabanSenakuButton' class='btn btn-small' >選択</button>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>借方　工事コード</label>
						<div class='controls'>
							<input type='text' name='kariKoujiCd' class='input-medium' maxlength='10' value='${su:htmlEscape(kariKoujiCd)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>借方　工種コード</label>
						<div class='controls'>
							<input type='text' name='kariKoushuCd' class='input-medium' maxlength='6' value='${su:htmlEscape(kariKoushuCd)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>借方　プロジェクトコード</label>
						<div class='controls'>
							<input type='text' name='kariProjectCd' class='input-medium' maxlength='12' value='${su:htmlEscape(kariProjectCd)}'>
							<input type='text' name='kariProjectName'  class='input-xlarge' disabled value='${su:htmlEscape(kariProjectName)}'>
							<button type='button' id='kariProjectSentakuButton' class='btn btn-small'<c:if test='${"0" eq pjShiyouFlg}'>disabled</c:if> >選択</button>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>借方　セグメントコード</label>
						<div class='controls'>
							<input type='text' name='kariSegmentCd' class='input-medium' maxlength='8' value='${su:htmlEscape(kariSegmentCd)}'>
							<input type='text' name='kariSegmentName'  class='input-xlarge' disabled value='${su:htmlEscape(kariSegmentName)}'>
							<button type='button' id='kariSegmentSentakuButton' class='btn btn-small'<c:if test='${"0" eq segmentShiyouFlg}'>disabled</c:if> >選択</button>	
						</div>
					</div>
					<c:forEach var="i" begin="0" end="<%=ShiwakeDataSiasHenkouAction.UF_NUM - 1 %>" step="1">
						<div class='control-group' <c:if test='${"0" eq ufShiyouFlgArr[i]}'>style='display:none;'</c:if>>
							<label class='control-label'>借方　ユニバーサルフィールド${numArr[i]}</label>
							<div class='controls'>
								<input type='text' name='kariUfCd[${i}]' class='input-xlarge' maxlength='20' value='${su:htmlEscape(kariUfCd[i])}'>
								<input type='text' name="kariUfName[${i}]" class='input-xlarge' disabled value='${su:htmlEscape(kariUfName[i])}' <c:if test='${"0" eq ufShiyouFlgArr[i] or "1" eq ufShiyouFlgArr[i]}'>style='display:none;'</c:if>>
								<!-- UF選択の通常と固定を切替 -->
								<c:choose>
									<c:when test = '${ufMappingArr[i] <= 10}'>
										<button type='button' id='kariUfSentakuButton' class='btn btn-small' onClick= 'sentakuBotton("kariUf", ${i}, ${ufMappingArr[i]})' <c:if test='${"0" eq ufShiyouFlgArr[i] or "1" eq ufShiyouFlgArr[i]}'>disabled</c:if> >選択</button>
									</c:when>
									<c:otherwise>
										<button type='button' id='kariUfSentakuButton' class='btn btn-small' onClick= 'sentakuBotton("kariUfKotei", ${i}, ${ufMappingArr[i]})' <c:if test='${"0" eq ufShiyouFlgArr[i] or "1" eq ufShiyouFlgArr[i]}'>disabled</c:if> >選択</button>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</c:forEach>
					<div class='control-group'>
						<label class='control-label'>借方　摘要</label>
						<div class='controls'>
							<input type='text' name='kariTekiyou' class='input-xxlarge' maxlength='120' value='${su:htmlEscape(kariTekiyou)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>借方　摘要コード</label>
						<div class='controls'>
							<input type='text' name='kariTekiyouCd' class='input-medium' maxlength='4' value='${su:htmlEscape(kariTekiyouCd)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>貸方　部門コード</label>
						<div class='controls'>
							<input type='text' name='kashiBumonCd' class='input-medium' maxlength='8' value='${su:htmlEscape(kashiBumonCd)}'>
							<input type='text' name='kashiBumonName' class='input-xlarge' disabled value='${su:htmlEscape(kashiBumonName)}' >
							<button type='button' id='kashiBumonSentakuButton' class='btn btn-small' >選択</button>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>貸方　取引先コード</label>
						<div class='controls'>
							<input type='text' name='kashiTorihikisakiCd' class='input-medium' maxlength='12' value='${su:htmlEscape(kashiTorihikisakiCd)}'>
							<input type='text' name='kashiTorihikisakiName' class='input-xlarge' disabled value='${su:htmlEscape(kashiTorihikisakiName)}' >
							<button type='button' id='kashiTorihikisakiSentakuButton' class='btn btn-small' >選択</button>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>貸方　科目コード</label>
						<div class='controls'>
							<input type='text' name='kashiKamokuCd' class='input-medium' maxlength='6' value='${su:htmlEscape(kashiKamokuCd)}'>
							<input type='text' name='kashiKamokuName' class='input-xlarge' disabled value='${su:htmlEscape(kashiKamokuName)}'>
							<button type='button' id='kashiKamokuSenakuButton' class='btn btn-small'>選択</button>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>貸方　枝番コード</label>
						<div class='controls'>
							<input type='text' name='kashiEdabanCd' class='input-medium' maxlength='12' value='${su:htmlEscape(kashiEdabanCd)}'>
							<input type='text' name='kashiEdabanName' class='input-xlarge' disabled value='${su:htmlEscape(kashiEdabanName)}'>
							<button type='button' id='kashiEdabanSenakuButton' class='btn btn-small' >選択</button>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>貸方　工事コード</label>
						<div class='controls'>
							<input type='text' name='kashiKoujiCd' class='input-medium' maxlength='10' value='${su:htmlEscape(kashiKoujiCd)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>貸方　工種コード</label>
						<div class='controls'>
							<input type='text' name='kashiKoushuCd' class='input-medium' maxlength='6' value='${su:htmlEscape(kashiKoushuCd)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>貸方　プロジェクトコード</label>
						<div class='controls'>
							<input type='text' name='kashiProjectCd' class='input-medium' maxlength='12' value='${su:htmlEscape(kashiProjectCd)}'>
							<input type='text' name='kashiProjectName'  class='input-xlarge' disabled value='${su:htmlEscape(kashiProjectName)}'>
							<button type='button' id='kashiProjectSentakuButton' class='btn btn-small'<c:if test='${"0" eq pjShiyouFlg}'>disabled</c:if> >選択</button>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>貸方　セグメントコード</label>
						<div class='controls'>
							<input type='text' name='kashiSegmentCd' class='input-medium' maxlength='8' value='${su:htmlEscape(kashiSegmentCd)}'>
							<input type='text' name='kashiSegmentName'  class='input-xlarge' disabled value='${su:htmlEscape(kashiSegmentName)}'>
							<button type='button' id='kashiSegmentSentakuButton' class='btn btn-small'<c:if test='${"0" eq segmentShiyouFlg}'>disabled</c:if> >選択</button>
						</div>
					</div>
					<c:forEach var="i" begin="0" end="<%=ShiwakeDataSiasHenkouAction.UF_NUM - 1 %>" step="1">
						<div class='control-group' <c:if test='${"0" eq ufShiyouFlgArr[i]}'>style='display:none;'</c:if>>
							<label class='control-label'>貸方　ユニバーサルフィールド${numArr[i]}</label>
							<div class='controls'>
								<input type='text' name='kashiUfCd[${i}]' class='input-xlarge' maxlength='20' value='${su:htmlEscape(kashiUfCd[i])}'>
								<input type='text' name="kashiUfName[${i}]" class='input-xlarge' disabled value='${su:htmlEscape(kashiUfName[i])}' <c:if test='${"0" eq ufShiyouFlgArr[i] or "1" eq ufShiyouFlgArr[i]}'>style='display:none;'</c:if>>
								<!-- UF選択の通常と固定を切替 -->
								<c:choose>
									<c:when test = '${ufMappingArr[i] <= 10}'>
										<button type='button' id='kashiUfSentakuButton' class='btn btn-small' onClick= 'sentakuBotton("kashiUf", ${i}, ${ufMappingArr[i]})' <c:if test='${"0" eq ufShiyouFlgArr[i] or "1" eq ufShiyouFlgArr[i]}'>disabled</c:if> >選択</button>
									</c:when>
									<c:otherwise>
										<button type='button' id='kashiUfSentakuButton' class='btn btn-small' onClick= 'sentakuBotton("kashiUfKotei", ${i}, ${ufMappingArr[i]})' <c:if test='${"0" eq ufShiyouFlgArr[i] or "1" eq ufShiyouFlgArr[i]}'>disabled</c:if> >選択</button>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</c:forEach>
					<div class='control-group'>
						<label class='control-label'>貸方　摘要</label>
						<div class='controls'>
							<input type='text' name='kashiTekiyou' class='input-xxlarge' maxlength='120' value='${su:htmlEscape(kashiTekiyou)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>貸方　摘要コード</label>
						<div class='controls'>
							<input type='text' name='kashiTekiyouCd' class='input-medium' maxlength='4' value='${su:htmlEscape(kashiTekiyouCd)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>対価金額</label>
						<div class='controls'>
							<input type='text' name='taikaKingaku' class='input-medium autoNumeric' value='${su:htmlEscape(taikaKingaku)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>金額</label>
						<div class='controls'>
							<input type='text' name='kingaku' class='input-medium autoNumeric' value='${su:htmlEscape(kingaku)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>消費税対象科目コード</label>
						<div class='controls'>
							<input type='text' name='shouhizeiTaishoKamokuCd' class='input-medium' maxlength='6' value='${su:htmlEscape(shouhizeiTaishoKamokuCd)}'>
							<button type='button' id='shouhizeiTaishoKamokuSentakuButton' class='btn btn-small' >選択</button>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>消費税対象科目税率</label>
						<div class='controls'>
							<input type='text' name='shouhizeiTaishoKamokuZeiritsu' class='input-small' maxlength='3' value='${su:htmlEscape(shouhizeiTaishoKamokuZeiritsu)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>消費税対象科目　軽減税率区分</label>
						<div class='controls'>
							<input type='text' name='shouhizeiTaishoKeigenZeiritsuKbn' class='input-small' maxlength='1' value='${su:htmlEscape(shouhizeiTaishoKeigenZeiritsuKbn)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>消費税対象科目　課税区分</label>
						<div class='controls'>
							<input type='text' name='shouhizeiTaishoKamokuKazeiKbn' class='input-small' maxlength='3' value='${su:htmlEscape(shouhizeiTaishoKamokuKazeiKbn)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>消費税対象科目　業種区分</label>
						<div class='controls'>
							<input type='text' name='shouhizeiTaishoKamokuGyoushuKbn' class='input-small' maxlength='1' value='${su:htmlEscape(shouhizeiTaishoKamokuGyoushuKbn)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>消費税対象科目　仕入区分</label>
						<div class='controls'>
							<input type='text' name='shouhizeiTaishoKamokuShiireKbn' class='input-small' maxlength='1' value='${su:htmlEscape(shouhizeiTaishoKamokuShiireKbn)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>借方　税率</label>
						<div class='controls'>
							<input type='text' name='kariZeiritsu' class='input-small' maxlength='3' value='${su:htmlEscape(kariZeiritsu)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>借方　軽減税率区分</label>
						<div class='controls'>
							<input type='text' name='kariKeigenZeiritsuKbn' class='input-small' maxlength='1' value='${su:htmlEscape(kariKeigenZeiritsuKbn)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>貸方　税率</label>
						<div class='controls'>
							<input type='text' name='kashiZeiritsu' class='input-small' maxlength='3' value='${su:htmlEscape(kashiZeiritsu)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>貸方　軽減税率区分</label>
						<div class='controls'>
							<input type='text' name='kashiKeigenZeiritsuKbn' class='input-small' maxlength='1' value='${su:htmlEscape(kashiKeigenZeiritsuKbn)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>借方　課税区分</label>
						<div class='controls'>
							<input type='text' name='kariKazeiKbn' class='input-small' maxlength='3' value='${su:htmlEscape(kariKazeiKbn)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>借方　業種区分</label>
						<div class='controls'>
							<input type='text' name='kariGyoushuKbn' class='input-small' maxlength='1' value='${su:htmlEscape(kariGyoushuKbn)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>借方　仕入区分</label>
						<div class='controls'>
							<input type='text' name='kariShiireKbn' class='input-small' maxlength='1' value='${su:htmlEscape(kariShiireKbn)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>貸方　課税区分</label>
						<div class='controls'>
							<input type='text' name='kashiKazeiKbn' class='input-small' maxlength='3' value='${su:htmlEscape(kashiKazeiKbn)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>貸方　業種区分</label>
						<div class='controls'>
							<input type='text' name='kashiGyoushuKbn' class='input-small' maxlength='1' value='${su:htmlEscape(kashiGyoushuKbn)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>貸方　仕入区分</label>
						<div class='controls'>
							<input type='text' name='kashiShiireKbn' class='input-small' maxlength='1' value='${su:htmlEscape(kashiShiireKbn)}'>
						</div>
					</div>
								<!-- インボイス項目の追加場所予定地 -->
					<div class='control-group' name='invoice'>
						<label class='control-label'>借方　併用売上税額計算方式</label>
						<div class='controls'>
							<input type='text' name='kariUrizeikeisan' class='input-small' maxlength='1' value='${su:htmlEscape(kariUrizeikeisan)}'>
						</div>
					</div>
					<div class='control-group' name='invoice'>
						<label class='control-label'>借方　仕入税額控除経過措置割合</label>
						<div class='controls'>
							<input type='text' name='kariMenzeikeika' class='input-small' maxlength='1' value='${su:htmlEscape(kariMenzeikeika)}'>
						</div>
					</div>
										<!-- インボイス項目の追加場所予定地 -->
					<div class='control-group' name='invoice'>
						<label class='control-label'>貸方　併用売上税額計算方式</label>
						<div class='controls'>
							<input type='text' name='kashiUrizeikeisan' class='input-small' maxlength='1' value='${su:htmlEscape(kashiUrizeikeisan)}'>
						</div>
					</div>
					<div class='control-group' name='invoice'>
						<label class='control-label'>貸方　仕入税額控除経過措置割合</label>
						<div class='controls'>
							<input type='text' name='kashiMenzeikeika' class='input-small' maxlength='1' value='${su:htmlEscape(kashiMenzeikeika)}'>
						</div>
					</div>
										<!-- インボイス項目の追加場所予定地 -->
					<div class='control-group' name='invoice'>
						<label class='control-label'>消費税対象科目　併用売上税額計算方式</label>
						<div class='controls'>
							<input type='text' name='zeiUrizeikeisan' class='input-small' maxlength='1' value='${su:htmlEscape(zeiUrizeikeisan)}'>
						</div>
					</div>
					<div class='control-group' name='invoice'>
						<label class='control-label'>消費税対象科目　仕入税額控除経過措置割合</label>
						<div class='controls'>
							<input type='text' name='zeiMenzeikeika' class='input-small' maxlength='1' value='${su:htmlEscape(zeiMenzeikeika)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>支払日</label>
						<div class='controls'>
							<input type='text' name='shiharaibi' class='input-small datepicker' value='${su:htmlEscape(shiharaibi)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>支払区分</label>
						<div class='controls'>
							<input type='text' name='shiharaiKbn' class='input-small' maxlength='2' value='${su:htmlEscape(shiharaiKbn)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>支払期日</label>
						<div class='controls'>
							<input type='text' name='shiharaiKijitu' class='input-small datepicker' value='${su:htmlEscape(shiharaiKijitu)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>回収日</label>
						<div class='controls'>
							<input type='text' name='kaishubi' class='input-small datepicker' value='${su:htmlEscape(kaishubi)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>入金区分</label>
						<div class='controls'>
							<input type='text' name='nyukinKbn' class='input-small' maxlength='2' value='${su:htmlEscape(nyukinKbn)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>回収期日</label>
						<div class='controls'>
							<input type='text' name='kaishuKijitu' class='input-small datepicker' value='${su:htmlEscape(kaishuKijitu)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>消込コード</label>
						<div class='controls'>
							<input type='text' name='keshikomiCd' class='input-medium' maxlength='12' value='${su:htmlEscape(keshikomiCd)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>起票年月日</label>
						<div class='controls'>
							<input type='text' name='kihyouNengappi' class='input-small datepicker' value='${su:htmlEscape(kihyouNengappi)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>起票部門コード</label>
						<div class='controls'>
							<input type='text' name='kihyouBumonCd' class='input-medium' maxlength='8' value='${su:htmlEscape(kihyouBumonCd)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>起票者コード</label>
						<div class='controls'>
							<input type='text' name='kihyoushaCd' class='input-medium' maxlength='12' value='${su:htmlEscape(kihyoushaCd)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>入力者コード</label>
						<div class='controls'>
							<input type='text' name='nyuryokushaCd' class='input-small' maxlength='4' value='${su:htmlEscape(nyuryokushaCd)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>付箋番号</label>
						<div class='controls'>
							<input type='text' name='husenNo' class='input-small' maxlength='2' value='${su:htmlEscape(husenNo)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>貸借別摘要フラグ</label>
						<div class='controls'>
							<input type='text' name='tkflg' class='input-small' maxlength='1' value='${su:htmlEscape(tkflg)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>承認グループNo.</label>
						<div class='controls'>
							<input type='text' name='shouninGroupNo' class='input-small' maxlength='4' value='${su:htmlEscape(shouninGroupNo)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>分離区分</label>
						<div class='controls'>
							<input type='text' name='bunriKbn' class='input-small' maxlength='1' value='${su:htmlEscape(bunriKbn)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>幣種</label>
						<div class='controls'>
							<input type='text' name='heic' class='input-small' maxlength='4' value='${su:htmlEscape(heic)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>レート</label>
						<div class='controls'>
							<input type='text' name='rate' class='input-medium' maxlength='12' value='${su:htmlEscape(rate)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>外貨対価金額</label>
						<div class='controls'>
							<input type='text' name='gaikaTaikaKingaku' class='input-medium autoNumeric' value='${su:htmlEscape(gaikaTaikaKingaku)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>外貨金額</label>
						<div class='controls'>
							<input type='text' name='gaikaKingaku' class='input-medium autoNumeric' value='${su:htmlEscape(gaikaKingaku)}'>
						</div>
					</div>
					<div class='control-group'>
						<label class='control-label'>区切り</label>
						<div class='controls'>
							<input type='text' name='kugiri' class='input-small' maxlength='1' value='${su:htmlEscape(kugiri)}'>
						</div>
					</div>
				</section>
<c:if test='${shiwakeStatusCd eq 0 }'>
 				<section>
					<!-- ボタン -->
					<div class='control-group'>
						<button type="button" class='btn' onClick="buttonPush('koushin')"><i class='icon-refresh'></i>変更</button>
						<button type="button" class='btn' onClick="buttonPush('shiwakeTaishougai')"><i class='icon-remove'></i>仕訳対象外とする</button>
					</div>
				</section>
</c:if>
				</form>
				</div><!-- main -->
			<!-- Modal -->
			<div id='dialog'></div>
			</div><!-- content -->
		</div><!-- wrap -->
		<br /><br />
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		
		<script style='text/javascript'>

/**
 * 初期表示イベント
 */
$(document).ready(function(){
	// 仕訳済or仕訳対象外の時は、input属性をdisabled=trueに。選択ボタンは非表示。
	<c:if test='${shiwakeStatusCd ne 0}'>;
		$("#main").find("input").prop("disabled", true);
		$("#data").find("button").hide();
	</c:if>;
	
	//（借方）部門選択ボタン押下時Function
	$("#kariBumonSentakuButton").click(function(){
		dialogRetFutanBumonCd = $("input[name=kariBumonCd]");
		dialogRetFutanBumonName = $("input[name=kariBumonName]");
		commonFutanBumonSentaku("2",$("input[name=kariKamokuCd]").val());
	});
	
	//（借方）取引先選択ボタン押下時Function
	$("#kariTorihikisakiSentakuButton").click(function(){
		dialogRetTorihikisakiCd = $("input[name=kariTorihikisakiCd]");
		dialogRetTorihikisakiName = $("input[name=kariTorihikisakiName]");
		commonTorihikisakiSentaku("");
	});
	
	//（借方）勘定科目選択ボタン押下時Function
	$("#kariKamokuSenakuButton").click(function(){
		dialogRetKamokuCd = $("input[name=kariKamokuCd]");
		dialogRetKamokuName = $("input[name=kariKamokuName]");
		commonKamokuSentaku();
	});
	
	//（借方）勘定科目枝番選択ボタン押下時Function
	$("#kariEdabanSenakuButton").click(function(){
		if($("input[name=kariKamokuCd]").val() == "") {
			alert("借方 科目コードを入力してください。");
		}else {
			dialogRetKamokuEdabanCd = $("input[name=kariEdabanCd]");
			dialogRetKamokuEdabanName = $("input[name=kariEdabanName]");
			commonKamokuEdabanSentaku($("input[name=kariKamokuCd]").val());
		}
	});
	
	//（借方）プロジェクト選択ボタン押下時Function
	$("#kariProjectSentakuButton").click(function(){
		dialogRetProjectCd = $("input[name=kariProjectCd]");
		dialogRetProjectName = $("input[name=kariProjectName]");
		commonProjectSentaku();
	});
	
	//（借方）セグメント選択ボタン押下時Function
	$("#kariSegmentSentakuButton").click(function(){
		dialogRetSegmentCd = $("input[name=kariSegmentCd]");
		dialogRetSegmentName = $("input[name=kariSegmentName]");
		commonSegmentSentaku();
	});
	
	//（貸方）部門選択ボタン押下時Function
	$("#kashiBumonSentakuButton").click(function(){
		dialogRetFutanBumonCd = $("input[name=kashiBumonCd]");
		dialogRetFutanBumonName = $("input[name=kashiBumonName]");
		commonFutanBumonSentaku("2",$("input[name=kashiKamokuCd]").val());
	});
	
	//（貸方）取引先選択ボタン押下時Function
	$("#kashiTorihikisakiSentakuButton").click(function(){
		dialogRetTorihikisakiCd = $("input[name=kashiTorihikisakiCd]");
		dialogRetTorihikisakiName = $("input[name=kashiTorihikisakiName]");
		commonTorihikisakiSentaku("");
	});
	
	//（貸方）勘定科目選択ボタン押下時Function
	$("#kashiKamokuSenakuButton").click(function(){
		dialogRetKamokuCd = $("input[name=kashiKamokuCd]");
		dialogRetKamokuName = $("input[name=kashiKamokuName]");
		commonKamokuSentaku();
	});
	
	//（貸方）勘定科目枝番選択ボタン押下時Function
	$("#kashiEdabanSenakuButton").click(function(){
		if($("input[name=kashiKamokuCd]").val() == "") {
			alert("貸方 科目コードを入力してください。");
		}else {
			dialogRetKamokuEdabanCd = $("input[name=kashiEdabanCd]");
			dialogRetKamokuEdabanName = $("input[name=kashiEdabanName]");
			commonKamokuEdabanSentaku($("input[name=kashiKamokuCd]").val());
		}
	});
	
	//（貸方）プロジェクト選択ボタン押下時Function
	$("#kashiProjectSentakuButton").click(function(){
		dialogRetProjectCd = $("input[name=kashiProjectCd]");
		dialogRetProjectName = $("input[name=kashiProjectName]");
		commonProjectSentaku();
	});
	
	//（貸方）セグメント選択ボタン押下時Function
	$("#kashiSegmentSentakuButton").click(function(){
		dialogRetSegmentCd = $("input[name=kashiSegmentCd]");
		dialogRetSegmentName = $("input[name=kashiSegmentName]");
		commonSegmentSentaku();
	});
	
	//消費税対象科目コード選択ボタン押下時Function
	$("#shouhizeiTaishoKamokuSentakuButton").click(function(){
		dialogRetKamokuCd = $("input[name=shouhizeiTaishoKamokuCd]");
		commonKamokuSentaku();
	});
	
});


/**
 * ボタン押下時
 */
function buttonPush(obj){
	
	var formObject = document.getElementById("myForm");
	/** 押下されたボタンによって処理を変える  */
	switch (obj) {
	case 'koushin':
		formObject.action = 'shiwake_data_sias_henkou_koushin';
		if(window.confirm('変更してもよろしいですか？')){
			$(formObject).submit();
		}
		break;
	case 'shiwakeTaishougai':
		formObject.action = 'shiwake_data_sias_henkou_shiwake_taishougai_koushin';
		if(window.confirm('仕訳対象外としてもよろしいですか？')){
			$(formObject).submit();
		}
		break;
		
	}
}

/**
 * HF・UFの選択ボタン押下時
 */
function sentakuBotton(state, num, map){
	
	if (state == "hf") {
		dialogRetHeaderFieldCd = $("input[name='headerFieldCd[" + num + "]']");
		dialogRetHeaderFieldName = $("input[name='headerFieldName[" + num + "]']");
		commonHeaderSentaku(map);
	} else if (state == "kariUf") {
		dialogRetUniversalFieldCd = $("input[name='kariUfCd[" + num + "]']");
		dialogRetUniversalFieldName = $("input[name='kariUfName[" + num + "]']");
		commonUniversalSentaku($("input[name=kariKamokuCd]").val() ,map);
	} else if (state == "kashiUf") {
		dialogRetUniversalFieldCd = $("input[name='kashiUfCd[" + num + "]']");
		dialogRetUniversalFieldName = $("input[name='kashiUfName[" + num + "]']");
		commonUniversalSentaku($("input[name=kashiKamokuCd]").val() ,map);
	}
	else if (state == "kariUfKotei") {
		dialogRetUniversalFieldCd = $("input[name='kariUfCd[" + num + "]']");
		dialogRetUniversalFieldName = $("input[name='kariUfName[" + num + "]']");
		commonUniversalKoteiSentaku($("input[name=kariKamokuCd]").val() ,map);
	} else if (state == "kashiUfKotei") {
		dialogRetUniversalFieldCd = $("input[name='kashiUfCd[" + num + "]']");
		dialogRetUniversalFieldName = $("input[name='kashiUfName[" + num + "]']");
		commonUniversalKoteiSentaku($("input[name=kashiKamokuCd]").val() ,map);
	}
}

		</script>
	</body>
</html>
