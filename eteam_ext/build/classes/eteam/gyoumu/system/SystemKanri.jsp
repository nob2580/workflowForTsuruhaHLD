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
		<title>会社設定｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
	</head>

	<body>
    	<div id='wrap'>

    		<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
				<h1>会社設定</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' action='system_kanri_koushin' class='form-horizontal'>
					<input type='hidden' name='tabId' value='${su:htmlEscape(tabId)}'>
					<input type='hidden' name='ebunshoFlg' value='${su:htmlEscape(ebunshoFlg)}'>
					<input type='hidden' name='kaniTodokeOn' value='${su:htmlEscape(kaniTodokeOn)}'>

					<!-- バージョン -->
					<section>
						<%=EteamSymbol.formatVerYYMMDDXX()%> (DB ${dbVersion})
					</section>

					<!-- 項目選択用タブ -->
					<ul id ="setting_tabs" class="nav nav-tabs">
						<li><a href="#setting_workflow" data-toggle="tab">ワークフロー</a></li>
						<li><a href="#setting_option" data-toggle="tab">オプション機能</a></li>
						<li><a href="#setting_mail1" data-toggle="tab">メール配信(1)</a></li>
						<!-- setting_info内カテゴリタブ -->
						<c:forEach var='setting' items='${settingList}' varStatus="st">
							<c:if test='${"" != setting.category}'>
								<li><a href=#settingno_${st.count} data-toggle="tab">${su:htmlEscape(setting.category)}</a></li>
							</c:if>
						</c:forEach>
					</ul>

					<!-- システム設定 -->
					<section>
						<div id="TabSetting" class="tab-content">
							<div class="tab-pane" id="setting_workflow">
								<div class='no-more-tables'>
									<table class='table-bordered table-condensed'>
										<thead>
											<tr>
												<th>設定項目</th>
												<th>設定内容</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>可能な操作</td>
												<td>
													<p>可能なワークフロー操作を選択してください。</p>

													<div class='control-group'>
														<label class='control-label'>起票</label>
														<div class='controls'>
															<input type='checkbox' id='kihyou' name='kihyou' value='1' <c:if test='${"1" eq kihyou}'>checked</c:if> disabled />
														</div>
													</div>
													<div class='control-group'>
														<label class='control-label'>申請</label>
														<div class='controls'>
															<input type='checkbox' id='shinsei' name='shinsei' value='1' <c:if test='${"1" eq shinsei}'>checked</c:if> disabled />
														</div>
													</div>
													<div class='control-group'>
														<label class='control-label'>取下げ</label>
														<div class='controls'>
															<input type='checkbox' id='torisage' name='torisage' value='1' <c:if test='${"1" eq torisage}'>checked</c:if>/>
														</div>
													</div>
													<div class='control-group'>
														<label class='control-label'>取戻し</label>
														<div class='controls'>
															<input type='checkbox' id='torimodoshi' name='torimodoshi' value='1' <c:if test='${"1" eq torimodoshi}'>checked</c:if>/>
														</div>
													</div>
													<div class='control-group'>
														<label class='control-label'>差戻し</label>
														<div class='controls'>
															<input type='checkbox' id='sashimodoshi' name='sashimodoshi' value='1' <c:if test='${"1" eq sashimodoshi}'>checked</c:if>/>
														</div>
													</div>
													<div class='control-group'>
														<label class='control-label'>承認</label>
														<div class='controls'>
															<input type='checkbox' id='shounin' name='shounin' value='1' <c:if test='${"1" eq shounin}'>checked</c:if> disabled />
														</div>
													</div>
													<div class='control-group'>
														<label class='control-label'>否認</label>
														<div class='controls'>
															<input type='checkbox' id='hinin' name='hinin' value='1' <c:if test='${"1" eq hinin}'>checked</c:if>/>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>承認ルート登録</td>
												<td>
													<p><label class="checkbox"><input type="checkbox" id='shouninRouteTourokuShinseisha' name='shouninRouteTourokuShinseisha' value='1' <c:if test='${"1" eq shouninRouteTourokuShinseisha}'>checked</c:if>/> 申請者による承認ルート変更を可能とする</label></p>
													<p><label class="checkbox"><input type="checkbox" id='shouninRouteTourokuShouninsha' name='shouninRouteTourokuShouninsha' value='1' <c:if test='${"1" eq shouninRouteTourokuShouninsha}'>checked</c:if>/> 承認者による承認ルート変更を可能とする</label></p>
													<p>　※変更不可能とする場合、ルートは自動引き当てによるもののみとなります。</p>
												</td>
											</tr>
											<tr>
												<td>承認後の取戻し</td>
												<td>
													<p><label class="checkbox"><input type="checkbox" id='shouningoTorimodoshi' name='shouningoTorimodoshi' value='1' <c:if test='${"1" eq shouningoTorimodoshi}'>checked</c:if> <c:if test='${"0" eq torimodoshi}'>disabled</c:if>/> 上長承認後の取戻しを可能とする</label></p>
													<p>　※承認後の取戻し可否に関わらず、最終承認後は取戻し不可能。</p>
												</td>
											</tr>
											<tr>
												<td>上位先決承認</td>
												<td>
													<p><label class="checkbox"><input type="checkbox" id='jouiSenketsuShounin' name='jouiSenketsuShounin' value='1' <c:if test='${"1" eq jouiSenketsuShounin}'>checked</c:if>/> 上位先決承認を可能とする</label></p>
													<p>　上位先決承認とは、<br>
　申請者→承認者１→承認者２の承認ルートの際に、承認者１より先に、承認者２が承認する事です。<br>
　※上位先決承認を可能にしていない場合、承認ルートに登録された順序のみ承認可能(順次承認)。<br>
　※未処理の合議部署を飛ばして、上位先決承認を行う事はできません。</p>
												</td>
											</tr>
											<tr>
												<td>代行者の自己承認</td>
												<td>
													<p><label class="checkbox"><input type="checkbox" id='daikoushaJikoShounin' name='daikoushaJikoShounin' value='1' <c:if test='${"1" eq daikoushaJikoShounin}'>checked</c:if>/> 代行者の自己承認を可能とする</label></p>
													<p>　代行者の自己承認とは、<br>
　代行者としてログインしているユーザーが、自分の起票した伝票について代行者の権限により承認することを意味します。</p>
												</td>
											</tr>
		 									<tr>
												<td>ファイル添付</td>
												<td>
													<label class="checkbox"><input type="checkbox" id='fileTenpu' name='fileTenpu' value='1' <c:if test='${"1" eq fileTenpu}'>checked</c:if>/> ファイル添付機能を有効にする</label>
												</td>
											</tr>
											<tr>
												<td>WEB印刷ボタン表示</td>
												<td>
													<label class="checkbox"><input type="checkbox" id='webInsatsuButton' name='webInsatsuButton' value='1' <c:if test='${"1" eq webInsatsuButton}'>checked</c:if>/>WEB印刷ボタンを表示する</label>
												</td>
											</tr>
											<tr>
												<td>領収書・請求書等チェック必須</td>
												<td>
													<p><label class="checkbox"><input type="checkbox" id='ryoushuushoSeikyuushoTouChkHissu' name='ryoushuushoSeikyuushoTouChkHissu' value='1' <c:if test='${"1" eq ryoushuushoSeikyuushoTouChkHissu}'>checked</c:if>/> 領収書・請求書等にチェックがない場合エラーとする</label></p>
													<p>　※対象伝票は出張旅費精算（仮払精算）と海外出張旅費精算（仮払精算）と交通費精算</p>
												</td>
											</tr>
											<tr>
												<td>操作履歴欄の表示</td>
												<td>
													<p><label class="checkbox"><input type="checkbox" id='sousaRireki' name='sousaRireki' value='1' <c:if test='${"1" eq sousaRireki}'>checked</c:if>/> 伝票画面に操作履歴欄を表示する</label></p>
												</td>
											</tr>
											<tr>
												<td>注記の表示</td>
												<td>
													<p><label class="checkbox"><input type="checkbox" id='chuukiPreview' name='chuukiPreview' value='1' <c:if test='${"1" eq chuukiPreview}'>checked</c:if>/> 摘要が財務転記時にカットされる場合、注記を表示する</label></p>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<div class="tab-pane" id="setting_option">
								<div class='no-more-tables'>
									<table class='table-bordered table-condensed'>

										<thead>
											<tr>
												<th>設定項目</th>
												<th>設定内容</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>代行</td>
												<td>
													<label class="checkbox"><input type="checkbox" id='daikou' name='daikou' value='1' <c:if test='${"1" eq daikou}'>checked</c:if>/> 代行機能を有効にする</label>
													<label class="checkbox"><input type="checkbox" id='userDaikouShitei' name='userDaikouShitei' value='1' <c:if test='${"1" eq userDaikouShitei}'>checked</c:if>/> 一般ユーザーによる代行者指定を有効にする</label>
													<label class="checkbox"><input type="checkbox" id='kanriDaikouShitei' name='kanriDaikouShitei' value='1' <c:if test='${"1" eq kanriDaikouShitei}'>checked</c:if>/> 管理者による代行者指定を有効にする</label>
												</td>
											</tr>
											<tr>
												<td>ガイダンス起票</td>
												<td>
													<label class="checkbox"><input type="checkbox" id='guidanceKihyou' name='guidanceKihyou' value='1' <c:if test='${"1" eq guidanceKihyou}'>checked</c:if>/> ガイダンス起票機能を有効にする</label>
												</td>
											</tr>
											<tr>
												<td>お気に入り起票
												</td>
												<td>
													<label class="checkbox"><input type="checkbox" id='okiniiriKihyou' name='okiniiriKihyou' value='1' <c:if test='${"1" eq okiniiriKihyou}'>checked</c:if>/> お気に入り起票機能を有効にする</label>
												</td>
											</tr>
		 									<tr>
												<td>バッチ連携結果確認</td>
												<td>
													<label class="checkbox"><input type="checkbox" id='batchRenkeiKekkaKakunin' name='batchRenkeiKekkaKakunin' value='1' <c:if test='${"1" eq batchRenkeiKekkaKakunin}'>checked</c:if>/> バッチ連携結果確認機能を有効にする</label>
												</td>
											</tr>
											<c:if test="${ kaniTodokeOn}">
											<tr>
												<td>届出ジェネレータ</td>
												<td>
													<label class="checkbox"><input type="checkbox" id='denpyouSakusei' name='denpyouSakusei' value='1' <c:if test='${"1" eq denpyouSakusei}'>checked</c:if>/> 届出ジェネレータ機能を有効にする</label>
												</td>
											</tr>
											</c:if>
											<tr>
												<td>振込明細ダウンロード</td>
												<td>
													<label class="checkbox"><input type="checkbox" id ='csvFurikomiDownload' name='csvFurikomiDownload' value='1' <c:if test='${"1" eq csvFurikomiDownload}'>checked</c:if>/> 振込明細ダウンロード機能を有効にする</label>
												</td>
											</tr>
											<tr>
												<td>FBデータ作成</td>
												<td>
													<label class="checkbox"><input type="checkbox" id='FBDataSakusei' name='FBDataSakusei' value='1' <c:if test='${"1" eq FBDataSakusei}'>checked</c:if>/> FBデータ作成機能を有効にする</label>
												</td>
											</tr>
											<tr>
												<td>法人カード</td>
												<td>
													<label class="checkbox"><input type="checkbox" id='houjinCard' name='houjinCard' value='1' <c:if test='${"1" eq houjinCard}'>checked</c:if>/> 法人カード利用を有効にする</label>
												</td>
											</tr>
											<tr>
												<td>会社手配</td>
												<td>
													<label class="checkbox"><input type="checkbox" id='kaishaTehai' name='kaishaTehai' value='1' <c:if test='${"1" eq kaishaTehai}'>checked</c:if>/> 会社手配を有効にする</label>
												</td>
											</tr>
											<tr>
												<td>外貨入力</td>
												<td>
													<label class="checkbox"><input type="checkbox" id='gaikaNyuuryoku' name='gaikaNyuuryoku' value='1' <c:if test='${"1" eq gaikaNyuuryoku}'>checked</c:if>/> 外貨入力を有効にする</label>
												</td>
											</tr>
											<tr>
												<td>ICカード</td>
												<td>
													<label class="checkbox"><input type="checkbox" id='icCard' name='icCard' value='1' <c:if test='${"1" eq icCard}'>checked</c:if>/> ICカード利用を有効にする</label>
												</td>
											</tr>
											<tr>
												<td>添付ファイルのプレビュー表示</td>
												<td>
													<label class="checkbox"><input type="checkbox" id='previewTenpuFile' name='previewTenpuFile' value='1' <c:if test='${"1" eq previewTenpuFile}'>checked</c:if>/> 経費精算本体側において、以下の伝票種別を除き添付ファイルのプレビュー表示を有効にする</label>
													<p>　・振替伝票<br>　・総合付替伝票<br>　・届出ジェネレータ</p>
												</td>
											</tr>
										</tbody>

									</table>
								</div>
							</div>
							<div class="tab-pane" id="setting_mail1">
								<div class='no-more-tables'>
									<table class='table-bordered table-condensed'>
										<thead>
											<tr>
												<th>設定項目</th>
												<th>設定内容</th>
											</tr>
										</thead>
										<tbody>
									 		<tr>
												<td>メール配信(SMTP)</td>
												<td>
													<label class="checkbox"><input type="checkbox" id='mailHaishin' name='mailHaishin' value='1' <c:if test='${"1" eq mailHaishin}'>checked</c:if>/> メール配信機能を有効にする</label>
													<div class='control-group'>
														<label class='control-label'>SMTPサーバー名</label>
														<div class='controls'>
															<input type='text' id='mailServer' name='mailServer' class='input-xlarge' value='${su:htmlEscape(mailServer)}' <c:if test='${"0" eq mailHaishin}'>disabled</c:if>/>
														</div>
													</div>
													<div class='control-group'>
														<label class='control-label'>ポート番号</label>
														<div class='controls'>
															<input type='text' id='mailPort' name='mailPort' class='input-mini' value='${su:htmlEscape(mailPort)}' <c:if test='${"0" eq mailHaishin}'>disabled</c:if>/>
														</div>
													</div>
													<div class='control-group'>
														<label class='control-label'>認証方法</label>
														<div class='controls'>
															<select id='mailNinshou' name='mailNinshou' <c:if test='${"0" eq mailHaishin}'>disabled</c:if>>
																<c:forEach var='mailNinshouList' items='${ninshouList}'>
																	<option value='${su:htmlEscape(mailNinshouList.naibu_cd)}' <c:if test='${mailNinshouList.naibu_cd eq mailNinshou}'>selected</c:if>>${su:htmlEscape(mailNinshouList.name)}</option>
																</c:forEach>
															</select>
														</div>
													</div>
													<div class='control-group'>
														<label class='control-label'>暗号化方法</label>
														<div class='controls'>
															<select id='mailAngouka' name='mailAngouka' <c:if test='${"0" eq mailHaishin}'>disabled</c:if>>
																<c:forEach var='mailAngoukaList' items='${angoukaList}'>
																	<option value='${su:htmlEscape(mailAngoukaList.naibu_cd)}' <c:if test='${mailAngoukaList.naibu_cd eq mailAngouka}'>selected</c:if>>${su:htmlEscape(mailAngoukaList.name)}</option>
																</c:forEach>
															</select>
														</div>
													</div>
													<div class='control-group'>
														<label class='control-label'>メールアドレス</label>
														<div class='controls'>
															<input type='text' id='mailAddress' name='mailAddress' class='input-xlarge' maxlength="50" value='${su:htmlEscape(mailAddress)}' <c:if test='${"0" eq mailHaishin}'>disabled</c:if>/>
														</div>
													</div>
													<div class='control-group'>
														<label class='control-label'>ユーザー名</label>
														<div class='controls'>
															<input type='text' id='mailUser' name='mailUser' class='input-xlarge' value='${su:htmlEscape(mailUser)}' <c:if test='${"0" eq mailHaishin}'>disabled</c:if>/>
														</div>
													</div>
													<div class='control-group'>
														<label class='control-label'>パスワード(変更時)</label>
														<div class='controls'>
															<input type='password' id='mailPassword' name='mailPassword' class='input-medium' value='${su:htmlEscape(mailPassword)}' autocomplete='off' <c:if test='${"0" eq mailHaishin}'>disabled</c:if>/>
														</div>
													</div>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>

<c:forEach var='setting' items='${settingList}' varStatus="st">
<!-- setting_info各カテゴリごとにタブ作成 -->
<c:if test='${0 < setting.categoryCount}'>
							<div class="tab-pane" id='settingno_${st.count}'>
								<div class='no-more-tables'>
									<table class='table-bordered table-condensed'>
										<tr>
											<c:set var='ctgLast' value='${st.count+setting.categoryCount-1}'/>
											<thead>
												<tr>
													<th>設定項目</th>
													<th>設定内容</th>
												</tr>
											</thead>
											<tbody>
</c:if>
<c:choose>
<c:when test='${"sashihiki_tanka_kaigai_gaika_heishu" eq su:htmlEscape(settingName[st.count-1])}'>
<!-- 差引単価（海外）外貨のレイアウトは特殊 -->
												<td rowspan='3'>${su:htmlEscape(setting.setting_name_wa)}</td>
												<td>
													<input type='hidden' name='settingName' value='${su:htmlEscape(settingName[st.count-1])}'/>
													<span class='description'>幣種コード</span>
													<input type='text' name='settingVal' id='sashihikiTankaKaigaiGaikaHeishuCd' class='input-small' value='${su:htmlEscape(settingVal[st.count-1])}'/>
													<button type='button' id='heishuSentakuButton' class='btn btn-small'>選択</button>
													<span class='description'>　</span>
													<span class='description'>レート</span>
													<input type='text' name='sashihikiTankakaigaiGaikaRate' class='input-small' value='${su:htmlEscape(sashihikiTankaKaigaiGaikaRate)}' disabled/>
													<input type='hidden' name='sashiki' class='input-small' value='${su:htmlEscape(sashihikiTankaKaigaiGaikaRate)}' disabled/>
												</td>
</c:when>
<c:when test='${"sashihiki_tanka_kaigai_gaika" eq su:htmlEscape(settingName[st.count-1])}'>
												<tr>
													<td>
														<input type='hidden' name='settingName' value='${su:htmlEscape(settingName[st.count-1])}'/>
														<input type='text' name='settingVal' class='input-block-level' value='${su:htmlEscape(settingVal[st.count-1])}'/>
													</td>
												</tr>
												<tr>
													<td class='description'>${su:htmlEscapeBr(setting.description)}</td>
												</tr>
</c:when>
<c:otherwise>
<!-- 通常の項目はこっち -->
												<td rowspan='2'>${su:htmlEscape(setting.setting_name_wa)}</td>
												<td>
													<input type='hidden' name='settingName' value='${su:htmlEscape(settingName[st.count-1])}'/>
													<input type='text' name='settingVal' class='input-block-level' value='${su:htmlEscape(settingVal[st.count-1])}'/>
												</td>
												<tr>
													<td class='description'>${su:htmlEscapeBr(setting.description)}</td>
												</tr>
</c:otherwise>
</c:choose>
<c:if test='${ st.count == ctgLast}'>
											</tbody>
										</tr>
									</table>
								</div>
							</div>
</c:if>
</c:forEach>
						</div>
					</section>

					<!-- 処理ボタン -->
					<section>
						<button type='submit' class='btn'><i class='icon-hdd'></i> 更新</button>
					</section>
					<!-- Modal -->
					<div id='dialog' style="position:relative"></div>
				</form></div><!-- main -->
			</div><!-- content -->
			<div id='push'></div>
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<%@ include file="/jsp/eteam/include/Script.jsp" %>

		<!-- スクリプト -->
		<script style='text/javascript'>

/**
 * 押印枠（出力形式）に押印枠（表示内容）と印影も連動
 */
function ouinRendou(){
	var ouinNaiyou = $("input[value^=ouin_naiyou]").next();
	var inei		 = $("input[value^=inei]").next();
	if($("input[value^=ouin_format]").next().val() === "W" || $("input[value^=ouin_format]").next().val() === "X"){
		ouinNaiyou.prop("disabled", false);
		inei.prop("disabled", false);
	}else{
		ouinNaiyou.val("0");
		ouinNaiyou.prop("disabled", true);
		inei.val("0");
		inei.prop("disabled", true);
	}
}

/**
 * e文書全体が無効なら配下も連動
 */
function ebunshoRendou(){
	var ebsub = $("input[value^=ebunsho_seisei]").next();
	if($("input[value=ebunsho_enable_flg]").next().val() != "0") {
		ebsub.prop("disabled", false);
	} else {
		ebsub.val("0");
		ebsub.prop("disabled", true);
	}

	ebsub = $("input[value^=ebunsho_shubetsu]").next();
	if($("input[value=ebunsho_enable_flg]").next().val() != "0") {
		ebsub.prop("disabled", false);
	} else {
		ebsub.val("0");
		ebsub.prop("disabled", true);
	}

	ebsub = $("input[value^=ebunsho_compress]").next();
	if($("input[value=ebunsho_enable_flg]").next().val() != "0") {
		ebsub.prop("disabled", false);
	} else {
		ebsub.val("0");
		ebsub.prop("disabled", true);
	}

	ebsub = $("input[value^=ebunsho_taishou_check_default]").next();
	if($("input[value=ebunsho_enable_flg]").next().val() != "0") {
		ebsub.prop("disabled", false);
	} else {
		ebsub.val("0");
		ebsub.prop("disabled", true);
	}

	ebsub = $("input[value^=ebunsho_denshits_check_default]").next();
	if($("input[value=ebunsho_enable_flg]").next().val() != "0") {
		ebsub.prop("disabled", false);
	} else {
		ebsub.val("0");
		ebsub.prop("disabled", true);
	}
}

/**
 * 仕訳区分が日次用の場合、確定有無の項目入力欄を空欄化・グレーアウト
 */
function shiwakeKbnRendou(){
	var sksub = $("input[value=kakutei_umu]").next();
	if($("input[value=shiwake_kbn]").next().val() == "1") {
		sksub.val("");
		sksub.prop("disabled", true);
	} else {
		sksub.prop("disabled", false);
	}
}

/**
 * 申請者の計上日入力する設定の時のみ申請者の計上日制限を入力できる
 * @param keijoubiNyuuryoku	「計上日入力」の項目名
 * @param keijoubiSeigen	「申請者の計上日制限」の項目名
 * @param shinseiVal		申請者が入力する設定値
 */
function keijoubiSeigenRendou(keijoubiNyuuryoku, keijoubiSeigen, shinseiVal){
	var seigen = $("input[value=" + keijoubiSeigen +"]").next();
	if($("input[value=" + keijoubiNyuuryoku + "]").next().val() == shinseiVal) {
		seigen.prop("disabled", false);
	} else {
		seigen.val("");
		seigen.prop("disabled", true);
	}
}

/**
 * 外貨入力にチェックがない場合、邦貨換算端数処理方法の項目入力欄をグレーアウト
 */
function gaikaRendou(){
	var sksub = $("input[value=houka_kansan_hasuu]").next();
	if($("input[name=gaikaNyuuryoku]:checked").val() == "1") {
		sksub.prop("disabled", false);
	} else {
		if (sksub.val() == "") {
			sksub.val("0");
		}
		sksub.prop("disabled", true);
	}
}

/**
 * 控除項目　項目名称に入力がない場合、控除項目関連の項目入力欄をグレーアウト
 */
function manekinRendou(){
	var manekinName = $("input[value=manekin_name]").next().val();
	if(manekinName != ""){
		$("input[value=manekin_cd]"		).next().prop("disabled", false);
		$("input[value=manekin_edaban]"	).next().prop("disabled", false);
		$("input[value=manekin_bumon]"	).next().prop("disabled", false);
	} else {
		$("input[value=manekin_cd]"		).next().prop("disabled", true).val("");
		$("input[value=manekin_edaban]"	).next().prop("disabled", true).val("");
		$("input[value=manekin_bumon]"	).next().prop("disabled", true).val("");
	}
}

/**
 * 一見先コードに入力がない場合、一言先関連の項目入力欄をグレーアウト
 */
function ichigenRendou(){
	var ichigenCd = $("input[value=ichigen_cd]").next().val();
	if(ichigenCd != ""){
		$("input[value=ichigen_tesuuryou]"			).next().prop("disabled", false);
		$("input[value=ichigen_tesuuryou_kamoku_cd]").next().prop("disabled", false);
		$("input[value=ichigen_tesuuryou_edaban_cd]").next().prop("disabled", false);
		$("input[value=ichigen_tesuuryou_bumon_cd]" ).next().prop("disabled", false);
		$("input[value=ichigen_tesuuryou_bumon_cd_nini_bumon_hantei]" ).next().prop("disabled", false);
	} else {
		$("input[value=ichigen_tesuuryou]"			).next().prop("disabled", true).val("");
		$("input[value=ichigen_tesuuryou_kamoku_cd]").next().prop("disabled", true).val("");
		$("input[value=ichigen_tesuuryou_edaban_cd]").next().prop("disabled", true).val("");
		$("input[value=ichigen_tesuuryou_bumon_cd]" ).next().prop("disabled", true).val("");
		$("input[value=ichigen_tesuuryou_bumon_cd_nini_bumon_hantei]" ).next().prop("disabled", true).val("1");
	}
}

//20220225 追加 e文書と同じような制御でいけるか？
/**
 * 駅すぱあと連携がイントラ版の場合、往復割適用反映先を0・グレーアウト
 */
function ekispertIntraRendou(){
	var intraFlg = $("input[value=intra_flg]").next().val();
	if(intraFlg == "1"){
		$("input[value=ekispert_oufukuwari_hanei]").next().prop("disabled", true).val("0");
	}else{
		$("input[value=ekispert_oufukuwari_hanei]").next().prop("disabled", false);
	}
}

$(document).ready(function(){

	/**
	 * メール配信機能の有効・無効切替時のアクション
	 */
	$("[name=mailHaishin]").click(function(e) {
		if ($(this).prop("checked")) {
			$("[name=mailServer]").prop("disabled", false);
			$("[name=mailPort]").prop("disabled", false);
			$("[name=mailNinshou]").prop("disabled", false);
			$("[name=mailAngouka]").prop("disabled", false);
			$("[name=mailAddress]").prop("disabled", false);
			$("[name=mailUser]").prop("disabled", false);
			$("[name=mailPassword]").prop("disabled", false);
		} else {
			// 項目をクリア・デフォルトに戻す
			$("[name=mailServer]").attr("value","");
			$("[name=mailPort]").attr("value","");
			$("[name=mailNinshou]").val("NO").prop("selected", true)
			$("[name=mailAngouka]").val("NO").prop("selected", true)
			$("[name=mailAddress]").attr("value","");
			$("[name=mailUser]").attr("value","");
			$("[name=mailPassword]").attr("value","");
			// 項目を入力不可にする
			$("[name=mailServer]").prop("disabled", true);
			$("[name=mailPort]").prop("disabled", true);
			$("[name=mailNinshou]").prop("disabled", true);
			$("[name=mailAngouka]").prop("disabled", true);
			$("[name=mailAddress]").prop("disabled", true);
			$("[name=mailUser]").prop("disabled", true);
			$("[name=mailPassword]").prop("disabled", true);
		}
	});
	/**
	 * 取戻しのチェックが外れた時に下位オプションもOFFにする
	 */
	$("[name=torimodoshi]").click(function(e) {
		if ($(this).prop("checked")) {
			$("[name=shouningoTorimodoshi]").prop("disabled", false);
		} else {
			$("[name=shouningoTorimodoshi]").prop("checked", false);
			$("[name=shouningoTorimodoshi]").prop("disabled", true);
		}
	});
	/**
	 * 代行オプションが無効の場合、代行関連の項目入力欄をグレーアウト
	 */
	 if( !($("input[name=daikou]").prop("checked")) ){
			$("[name=userDaikouShitei]").prop("disabled", true);
			$("[name=kanriDaikouShitei]").prop("disabled", true);
	 }
	/**
	 * 代行のチェックが外れた時に下位オプションもOFFにする
	 */
	$("[name=daikou]").click(function(e) {
		if ($(this).prop("checked")) {
			$("[name=userDaikouShitei]").prop("disabled", false);
			$("[name=kanriDaikouShitei]").prop("disabled", false);
		} else {
			$("[name=userDaikouShitei]").prop("checked", false);
			$("[name=userDaikouShitei]").prop("disabled", true);
			$("[name=kanriDaikouShitei]").prop("checked", false);
			$("[name=kanriDaikouShitei]").prop("disabled", true);
		}
	});

	/*
	 * 押印枠（出力形式）の値に応じて押印枠（表示内容）と印影を制御
	 */
	ouinRendou();
	$("input[value=ouin_format]").next().change(ouinRendou);

	/**
	 * e文書使用制限オプションが無効の場合、e文書関連の項目入力欄をグレーアウト
	 */
	if($("input[name=ebunshoFlg]").val() != "1"){
		$.each($("input[name=settingName]"),function(){
			var setnm = $(this).val();
			if(setnm.substring(0,8) == "ebunsho_"){
				$(this).next("input[name=settingVal]").prop("disabled", true);
			}
		});
	}
	/**
	 * e文書全体が無効なら配下の設定も連動
	 */
	ebunshoRendou();
	$("input[value=ebunsho_enable_flg]").next().change(ebunshoRendou);

	/**
	 * 仕訳区分が日次用の場合、確定有無の項目入力欄を空欄化・グレーアウト
	 */
	shiwakeKbnRendou();
	$("input[value=shiwake_kbn]").next().change(shiwakeKbnRendou);

	/**
	 * 申請者の計上日入力が0以外(申請者が入力する)時のみ申請者の計上日制限は入力できる
	 */
	keijoubiSeigenRendou("seikyuu_keijou_nyuuryoku"		,"seikyuu_keijou_seigen"			,"1");
	//keijoubiSeigenRendou("shiharaiirai_keijou_nyuuryoku","shiharaiirai_keijou_seigen"		,"1");
	keijoubiSeigenRendou("jidouhiki_keijou_nyuuryoku"	,"jidouhiki_keijou_seigen"			,"1");
	keijoubiSeigenRendou("keijoubi_default_A001"		,"keihiseisan_keijou_seigen"		,"3");
	keijoubiSeigenRendou("keijoubi_default_A004"		,"ryohiseisan_keijou_seigen"		,"3");
	keijoubiSeigenRendou("keijoubi_default_A011"		,"kaigairyohiseisan_keijou_seigen"	,"3");
	keijoubiSeigenRendou("keijoubi_default_A010"		,"koutsuuhiseisan_keijou_seigen"	,"3");
	$("input[value=seikyuu_keijou_nyuuryoku]"		).next().change(function(){keijoubiSeigenRendou("seikyuu_keijou_nyuuryoku"		,"seikyuu_keijou_seigen"			,"1");});
	//$("input[value=shiharaiirai_keijou_nyuuryoku]"	).next().change(function(){keijoubiSeigenRendou("shiharaiirai_keijou_nyuuryoku"	,"shiharaiirai_keijou_seigen"		,"1");});
	$("input[value=jidouhiki_keijou_nyuuryoku]"		).next().change(function(){keijoubiSeigenRendou("jidouhiki_keijou_nyuuryoku"	,"jidouhiki_keijou_seigen"			,"1");});
	$("input[value=keijoubi_default_A001]"			).next().change(function(){keijoubiSeigenRendou("keijoubi_default_A001"			,"keihiseisan_keijou_seigen"		,"3");});
	$("input[value=keijoubi_default_A004]"			).next().change(function(){keijoubiSeigenRendou("keijoubi_default_A004"			,"ryohiseisan_keijou_seigen"		,"3");});
	$("input[value=keijoubi_default_A011]"			).next().change(function(){keijoubiSeigenRendou("keijoubi_default_A011"			,"kaigairyohiseisan_keijou_seigen"	,"3");});
	$("input[value=keijoubi_default_A010]"			).next().change(function(){keijoubiSeigenRendou("keijoubi_default_A010"			,"koutsuuhiseisan_keijou_seigen"	,"3");});

	/**
	 * 外貨入力にチェックがない場合、邦貨換算端数処理方法の項目入力欄をグレーアウト
	 */
	gaikaRendou();
	$("input[name=gaikaNyuuryoku]").change(gaikaRendou);

	/**
	 * 控除項目　項目名称に入力がない場合、控除項目関連の項目入力欄をグレーアウト
	 */
	manekinRendou();
	$("input[value=manekin_name]").next().change(manekinRendou);

	/**
	 * 一見先コードに入力がない場合、一言先関連の項目入力欄をグレーアウト
	 */
	ichigenRendou();
	$("input[value=ichigen_cd]").next().change(ichigenRendou);
	/**
	 * 駅すぱあと連携がイントラ版の場合、往復割適用反映先を0・グレーアウト
	 */
	ekispertIntraRendou();
	$("input[value=intra_flg]").next().change(ekispertIntraRendou);

	/**
	 * タブ押下時に復元位置として記憶する
	 */
	$("#setting_tabs a").click(function(e) {
		$("[name=tabId]").val($(this).attr("href").substr(1));
	});
	/**
	 * 表示時にタブ位置を復元する
	 */
	$("#setting_tabs a[href=#${su:htmlEscape(tabId)}]").tab("show");

	/**
	 * 幣種コード選択
	 */
	$("#heishuSentakuButton").on("click", function(){
		dialogRetHeishuCd		= $("#heishuSentakuButton").closest("td").find("input[name=settingVal]");
		dialogRetTsuukaTani		= null;		//通貨単位は不要だがセットしないと幣種選択でエラーになる
		dialogRetRate			= $("input[name=sashihikiTankakaigaiGaikaRate]");
		commonHeishuSentaku();
	 });
	$("#sashihikiTankaKaigaiGaikaHeishuCd").blur(function(){
		var title = $("#heishuSentakuButton").closest("td").find("span:first").text();
		dialogRetHeishuCd		= $("#heishuSentakuButton").closest("td").find("input[name=settingVal]");
		dialogRetRate			= $("input[name=sashihikiTankakaigaiGaikaRate]");
		commonHeishuCdLostFocus(dialogRetHeishuCd, null, dialogRetRate, title);
	});

	//OPEN21連携（財務拠点）タブの制御
	$("input[value=op21mparam_kaisha_cd_kyoten]").next().prop("disabled", true);
	$("input[value=op21mparam_kaisha_cd]").next().change(function(){
		$("input[value=op21mparam_kaisha_cd_kyoten]").next().val($(this).val());
	});
	$("input[value=shokuchi_cd_kyoten]").next().prop("disabled", true);
	$("input[value=shokuchi_cd]").next().change(function(){
		$("input[value=shokuchi_cd_kyoten]").next().val($(this).val());
	});
	$("input[value=op21mparam_kidou_user_kyoten]").next().prop("disabled", true);
	$("input[value=op21mparam_kidou_user]").next().change(function(){
		$("input[value=op21mparam_kidou_user_kyoten]").next().val($(this).val());
	});
	$("input[value=uf1_denpyou_id_hanei_kyoten]").next().prop("disabled", true);
	$("input[value=uf1_denpyou_id_hanei]").next().change(function(){
		$("input[value=uf1_denpyou_id_hanei_kyoten]").next().val($(this).val());
	});
	$("input[value=shain_cd_renkei_kyoten]").next().prop("disabled", true);
	$("input[value=shain_cd_renkei]").next().change(function(){
		$("input[value=shain_cd_renkei_kyoten]").next().val($(this).val());
	});
	$("input[value=hf1_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=hf1_mapping]").next().change(function(){
		$("input[value=hf1_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=hf2_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=hf2_mapping]").next().change(function(){
			$("input[value=hf2_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=hf3_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=hf3_mapping]").next().change(function(){
			$("input[value=hf3_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=hf4_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=hf4_mapping]").next().change(function(){
			$("input[value=hf4_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=hf5_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=hf5_mapping]").next().change(function(){
			$("input[value=hf5_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=hf6_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=hf6_mapping]").next().change(function(){
			$("input[value=hf6_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=hf7_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=hf7_mapping]").next().change(function(){
			$("input[value=hf7_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=hf8_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=hf8_mapping]").next().change(function(){
			$("input[value=hf8_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=hf9_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=hf9_mapping]").next().change(function(){
			$("input[value=hf9_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=hf10_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=hf10_mapping]").next().change(function(){
			$("input[value=hf10_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf1_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf1_mapping]").next().change(function(){
			$("input[value=uf1_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf2_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf2_mapping]").next().change(function(){
			$("input[value=uf2_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf3_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf3_mapping]").next().change(function(){
			$("input[value=uf3_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf4_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf4_mapping]").next().change(function(){
			$("input[value=uf4_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf5_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf5_mapping]").next().change(function(){
			$("input[value=uf5_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf6_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf6_mapping]").next().change(function(){
			$("input[value=uf6_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf7_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf7_mapping]").next().change(function(){
			$("input[value=uf7_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf8_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf8_mapping]").next().change(function(){
			$("input[value=uf8_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf9_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf9_mapping]").next().change(function(){
			$("input[value=uf9_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf10_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf10_mapping]").next().change(function(){
			$("input[value=uf10_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf_kotei1_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf_kotei1_mapping]").next().change(function(){
			$("input[value=uf_kotei1_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf_kotei2_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf_kotei2_mapping]").next().change(function(){
			$("input[value=uf_kotei2_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf_kotei3_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf_kotei3_mapping]").next().change(function(){
			$("input[value=uf_kotei3_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf_kotei4_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf_kotei4_mapping]").next().change(function(){
			$("input[value=uf_kotei4_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf_kotei5_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf_kotei5_mapping]").next().change(function(){
			$("input[value=uf_kotei5_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf_kotei6_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf_kotei6_mapping]").next().change(function(){
			$("input[value=uf_kotei6_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf_kotei7_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf_kotei7_mapping]").next().change(function(){
			$("input[value=uf_kotei7_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf_kotei8_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf_kotei8_mapping]").next().change(function(){
			$("input[value=uf_kotei8_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf_kotei9_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf_kotei9_mapping]").next().change(function(){
			$("input[value=uf_kotei9_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=uf_kotei10_mapping_kyoten]").next().prop("disabled", true);
	$("input[value=uf_kotei10_mapping]").next().change(function(){
			$("input[value=uf_kotei10_mapping_kyoten]").next().val($(this).val());
	});
	$("input[value=tenpu_file_renkei_kyoten]").next().prop("disabled", true);
	$("input[value=tenpu_file_renkei]").next().change(function(){
			$("input[value=tenpu_file_renkei_kyoten]").next().val($(this).val());
	});

});
		</script>
	</body>
</html>
