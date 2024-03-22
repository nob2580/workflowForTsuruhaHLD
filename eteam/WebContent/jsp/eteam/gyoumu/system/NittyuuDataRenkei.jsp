<%@page import="eteam.common.JspUtil"%>
<%@ page import="eteam.symbol.EteamSymbol"%>
<%@ page import="eteam.common.RegAccess"%>
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
		<title>データ連携｜<%=EteamSymbol.SYSTEM_NAME%></title>
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
				<h1>データ連携</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' action='nittyuu_data_renkei_renkei' class='form-horizontal'>

					<!-- 指定１ -->
<c:if test='<%= RegAccess.checkEnableKeihiSeisan() %>'>
					<section>
						<h2>仕訳データ一覧</h2>
						<div class='no-more-tables'>
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th style="width:45px;text-align:center;"><input type='checkbox' class='allCheck'></th>
										<th>連携対象</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td style="text-align: center;"><input type='checkbox' id='Karibarai' name='swkRenkeiArr' value='eteam.gyoumu.kaikei.KaribaraiChuushutsuBat'></td>
										<td><label for="Karibarai">経費伺い申請（仮払申請）</label></td>
									</tr>
									<tr>
										<td style="text-align: center;"><input type='checkbox' id='KeihiTatekaeSeisan' name='swkRenkeiArr' value='eteam.gyoumu.kaikei.KeihiTatekaeSeisanChuushutsuBat'></td>
										<td><label for="KeihiTatekaeSeisan">経費立替精算</label></td>
									</tr>
									<tr>
										<td style="text-align: center;"><input type='checkbox' id='RyohiKaribarai' name='swkRenkeiArr' value='eteam.gyoumu.kaikei.RyohiKaribaraiChuushutsuBat'></td>
										<td><label for="RyohiKaribarai">出張伺い申請（仮払申請）</label></td>
									</tr>
									<tr>
										<td style="text-align: center;"><input type='checkbox' id='RyohiSeisan' name='swkRenkeiArr' value='eteam.gyoumu.kaikei.RyohiSeisanChuushutsuBat'></td>
										<td><label for="RyohiSeisan">出張旅費精算（仮払精算）</label></td>
									</tr>
									<tr>
										<td style="text-align: center;"><input type='checkbox' id='KaigaiRyohiKaribarai' name='swkRenkeiArr' value='eteam.gyoumu.kaikei.KaigaiRyohiKaribaraiChuushutsuBat'></td>
										<td><label for="KaigaiRyohiKaribarai">海外出張伺い申請（仮払申請）</label></td>
									</tr>
									<tr>
										<td style="text-align: center;"><input type='checkbox' id='KaigaiRyohiSeisan' name='swkRenkeiArr' value='eteam.gyoumu.kaikei.KaigaiRyohiSeisanChuushutsuBat'></td>
										<td><label for="KaigaiRyohiSeisan">海外出張旅費精算（仮払精算）</label></td>
									</tr>
									<tr>
										<td style="text-align: center;"><input type='checkbox' id='KoutsuuhiSeisan' name='swkRenkeiArr' value='eteam.gyoumu.kaikei.KoutsuuhiSeisanChuushutsuBat'></td>
										<td><label for="KoutsuuhiSeisan">交通費精算</label></td>
									</tr>
									<tr>
										<td style="text-align: center;"><input type='checkbox' id='TsuukinTeiki' name='swkRenkeiArr' value='eteam.gyoumu.kaikei.TsuukinTeikiChuushutsuBat'></td>
										<td><label for="TsuukinTeiki">通勤定期申請</label></td>
									</tr>
<c:if test='${seikyuushoBaraiOn}'>
									<tr>
										<td style="text-align: center;"><input type='checkbox' id='SeikyuushoBarai' name='swkRenkeiArr' value='eteam.gyoumu.kaikei.SeikyuushoBaraiChuushutsuBat'></td>
										<td><label for="SeikyuushoBarai">請求書払い申請</label></td>
									</tr>
</c:if>
<c:if test='${shiharaiIraiOn}'>
                                    <tr>
                                        <td style="text-align: center;"><input type='checkbox' id='ShiharaiIrai' name='swkRenkeiArr' value='eteam.gyoumu.kaikei.ShiharaiIraiChuushutsuBat'></td>
                                        <td><label for="ShiharaiIrai">支払依頼申請</label></td>
                                    </tr>
</c:if>
									<tr>
										<td style="text-align: center;"><input type='checkbox' id='JidouHikiotoshi' name='swkRenkeiArr' value='eteam.gyoumu.kaikei.JidouHikiotoshiChuushutsuBat'></td>
										<td><label for="JidouHikiotoshi">自動引落伝票</label></td>
									</tr>
									<tr>
										<td style="text-align: center;"><input type='checkbox' id='FurikaeDenpyou' name='swkRenkeiArr' value='eteam.gyoumu.kaikei.FurikaeDenpyouChuushutsuBat'></td>
										<td><label for="FurikaeDenpyou">振替伝票</label></td>
									</tr>
									<tr>
										<td style="text-align: center;"><input type='checkbox' id='SougouTsukekaeDenpyou' name='swkRenkeiArr' value='eteam.gyoumu.kaikei.SougouTsukekaeDenpyouChuushutsuBat'></td>
										<td><label for="SougouTsukekaeDenpyou">総合付替伝票</label></td>
									</tr>
									<jsp:include page='<%= JspUtil.makeJspPath("eteam/gyoumu/system/NittyuuDataRenkei_list.jsp") %>' />
								</tbody>
							</table>
						</div>
					</section>
</c:if>

					<!-- 指定２ -->
					<section>
						<h2>マスターデータ一覧</h2>
						<div class='no-more-tables'>
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th style="width:45px;text-align:center;"><input type='checkbox' class='allCheck'></th>
										<th>連携対象</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td style="text-align: center;"><input type='checkbox' id='MasterTorikomiBat' name='mstRenkeiArr' value='eteam.gyoumu.masterkanri.MasterTorikomiBat'></td>
										<td><label for="MasterTorikomiBat">マスター取込</label></td>
									</tr>
								</tbody>
							</table>
						</div>
					</section>

					<!-- 処理ボタン -->
					<section>
						<button type='submit' name='renkeiButton' class='btn'><i class='icon-random'></i> 連携</button>
					</section>
				</form></div><!-- main -->
			</div><!-- /container -->
			<div id='push'></div>
		</div><!-- wrap -->
		
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>
		
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script style='text/javascript'>

$(document).ready(function(){
	//全選択
	$(".allCheck").click(function(){
		$(this).closest("div").find("table").find("tbody").find("input[type=checkbox]").prop("checked", $(this).prop("checked"));
	});
});
		</script>
	</body>
</html>
