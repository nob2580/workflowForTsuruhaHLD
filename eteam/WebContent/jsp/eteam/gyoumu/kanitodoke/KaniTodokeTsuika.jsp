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
		<title>ユーザー定義届書追加｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<style type='text/css'>
		.number {
			text-align:right;
		}
		.ui-state-default {
			background: none;
			border: none;
			font-weight: normal;
			color: #000000;
		}
		</style>
	</head>
	<body>
    	<div id='wrap'>

			<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<!-- 中身 -->
			<div id='content' class='container'>

				<!-- タイトル -->
				<h1>ユーザー定義届書追加</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>

				<!-- メイン -->
				<div id='main'>
					<form id='kaniForm' method='post' class='form-horizontal'>

						<!-- 入力フォームフィールド  -->
						<section id='denpyouCommon'>
							<h2>伝票</h2>
							<div>
								<div class='control-group'>
									<label class="control-label label-sub"><span class='required'>*</span>伝票種別</label>
									<div class='controls'>
										<input type='text' name='denpyouName' maxlength='20' value='${su:htmlEscape(denpyouName)}'>
									</div>
								</div>
								<div class='control-group'>
									<label class="control-label label-sub"><span class='required'>*</span>内容</label>
									<div class='controls'>
										<textarea name='denpyouNaiyou' class='input-inline input-xxlarge' maxlength='160'>${su:htmlEscape(denpyouNaiyou)}</textarea>
									</div>
								</div>
							</div>
						</section>
						
						<!-- 入力フォームフィールド  -->
						<section id='inputForm'>
							<h2>入力部品</h2>
							<div>
								<div class='control-group'>
									<label class="control-label label-sub">エリア選択</label>
									<div class='controls'>
										<div class="btn-group" data-toggle="buttons-radio">
											<button type="button" class="btn btn-primary active" name='toggleShinsei' onclick="changeButton('0')">申請内容</button>
											<button type="button" class="btn" name="toggleMeisai" onclick="changeButton('1')">&nbsp;&nbsp;&nbsp;明細&nbsp;&nbsp;&nbsp;</button>
										</div>
										<input type='hidden' name='toggleFlg' value='${su:htmlEscape(toggleFlg)}'>
										<input type='hidden' name='shinseiItemCnt' value='${su:htmlEscape(shinseiItemCnt)}'>
										<input type='hidden' name='meisaiItemCnt' value='${su:htmlEscape(meisaiItemCnt)}'>
									</div>
								</div>
								<div class='control-group'>
									<label class="control-label label-sub">入力部品</label>
									<div class='controls'>
									<button type='button' class='btn btn-small addTextButton'>テキスト</button>
										<button type='button' class='btn btn-small addTextAreaButton'>テキストエリア</button>
										<button type='button' class='btn btn-small addRadioButton'>ラジオ</button>
										<button type='button' class='btn btn-small addPullDown'>プルダウン</button>
										<button type='button' class='btn btn-small addCheckBox'>チェックボックス</button>
										<button type='button' class='btn btn-small addMasterItem'>マスター</button>
									</div>
									<div id='yosanShikkouPartsShinsei' style="display:none;">
										<div style="height:4px;"></div>
										<div class='controls'>
											<button type='button' class='btn btn-small addRingiKingaku'>稟議金額</button>
											<button type='button' class='btn btn-small addKenmei'>件名</button>
											<button type='button' class='btn btn-small addNaiyouShinsei'>内容</button>
											<button type='button' class='btn btn-small addShuunyuuKingakuGoukei'>収入金額合計</button>
											<button type='button' class='btn btn-small addShisyhutsuKingakuGoukei'>支出金額合計</button>
											<button type='button' class='btn btn-small addShuushiSagaku'>収支差額</button>
										</div>
									</div>
									<div id='yosanShikkouPartsMeisai' style="display:none;">
										<div style="height:4px;"></div>
										<div class='controls'>
											<button type='button' class='btn btn-small addShuunyuuBumon'>収入部門</button>
											<button type='button' class='btn btn-small addShuunyuuKamoku'>収入科目</button>
											<button type='button' class='btn btn-small addShuunyuuEda'>収入枝番</button>
											<button type='button' class='btn btn-small addShuunyuuKingaku'>収入金額</button>
											<button type='button' class='btn btn-small addShuunyuuBikou'>収入備考</button>
										</div>
										<div style="height:4px;"></div>
										<div class='controls'>
											<button type='button' class='btn btn-small addShishutsuBumon'>支出部門</button>
											<button type='button' class='btn btn-small addShishutsuKamoku'>支出科目</button>
											<button type='button' class='btn btn-small addShishutsuEda'>支出枝番</button>
											<button type='button' class='btn btn-small addShishutsuKingaku'>支出金額</button>
											<button type='button' class='btn btn-small addShishutsuBikou'>支出備考</button>
											<button type='button' class='btn btn-small addNaiyouMeisai'>内容</button>
										</div>
									</div>
								</div>
							</div>
						</section>
						
						<!-- 入力フィールド -->
						${shinseiHtml}
						${meisaiHtml}
						
						<section>
							<div class='control-group'>
								<button type='button' onClick="eventBtn('entry')" class='btn'><i class='icon-hdd'></i> 登録</button>
							</div>
						</section>
						<datalist id="jikoku"><c:forEach var='record' items='${jikokuList}'><option value='${su:htmlEscape(record.name)}'></c:forEach></datalist>
						<!-- Modal -->
						<div id='dialog'></div>
						
					</form>
				</div><!-- main -->
				<div id='push'></div>
			</div><!-- content -->
		</div><!-- wrap -->

		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script src="/eteam/static/js/eteam_kanitodoke.js"></script>
<script style='text/javascript'>
//初期表示処理
$(document).ready(function(){

	// トグルボタンの変更
	changeButton($("input[name=toggleFlg]").val());
	
	// 項目のドラッグ＆ドロップによる並び替え
	$('#shinseiList').sortable();
	$('#meisaiList').sortable();
	
	var target = null;
	
	// 入力フォームボタンのアクション紐づけ
	target = $("#inputForm");
	target.find("button.addTextButton").click(commonAddText);			// テキストボタン押下時アクション
	target.find("button.addRadioButton").click(commonAddRadio);			// ラジオボタン押下時アクション
	target.find("button.addTextAreaButton").click(commonAddTextArea);	// テキストエリアボタン押下時アクション
	target.find("button.addPullDown").click(addPullDown);				// プルダウン押下時アクション
	target.find("button.addCheckBox").click(addCheckBox);				// チェックボックス押下時アクション
	target.find("button.addMasterItem").click(addMasterItem);			// マスターボタン押下時アクション

	// 予算執行入力部品（申請内容）
	target.find("button.addRingiKingaku").click(addRingiKingaku);						// 稟議金額ボタン押下時アクション
	target.find("button.addKenmei").click(addKenmei);									// 件名ボタン押下時アクション
	target.find("button.addNaiyouShinsei").click(addNaiyouShinsei);						// 内容ボタン押下時アクション
	target.find("button.addShuunyuuKingakuGoukei").click(addShuunyuuKingakuGoukei);		// 収入金額合計ボタン押下時アクション
	target.find("button.addShisyhutsuKingakuGoukei").click(addShisyhutsuKingakuGoukei);	// 支出金額合計ボタン押下時アクション
	target.find("button.addShuushiSagaku").click(addShuushiSagaku);						// 収支差額ボタン押下時アクション

	// 予算執行入力部品（明細）
	target.find("button.addShuunyuuBumon").click(addShuunyuuBumon);						// 収入部門ボタン押下時アクション
	target.find("button.addShuunyuuKamoku").click(addShuunyuuKamoku);					// 収入科目ボタン押下時アクション
	target.find("button.addShuunyuuEda").click(addShuunyuuEda);							// 収入枝番ボタン押下時アクション
	target.find("button.addShuunyuuKingaku").click(addShuunyuuKingaku);					// 収入金額ボタン押下時アクション
	target.find("button.addShuunyuuBikou").click(addShuunyuuBikou);						// 収入備考ボタン押下時アクション
	target.find("button.addShishutsuBumon").click(addShishutsuBumon);					// 支出部門ボタン押下時アクション
	target.find("button.addShishutsuKamoku").click(addShishutsuKamoku);					// 支出科目ボタン押下時アクション
	target.find("button.addShishutsuEda").click(addShishutsuEda);						// 支出枝番ボタン押下時アクション
	target.find("button.addShishutsuKingaku").click(addShishutsuKingaku);				// 支出金額ボタン押下時アクション
	target.find("button.addShishutsuBikou").click(addShishutsuBikou);					// 支出備考ボタン押下時アクション
	target.find("button.addNaiyouMeisai").click(addNaiyouMeisai);						// 内容ボタン押下時アクション
	
	// 申請内容のアクション紐づけ
	target = $("#shinseiList");
	target.find("button[name=rowChange]").click(rowChange);
	target.find("button[name=rowDelete]").click(rowDelete);
	target.find("button[name=rowUp]").click(rowUp);
	target.find("button[name=rowDown]").click(rowDown);
	
	// 明細のアクション紐づけ
	target = $("#meisaiList");
	target.find("button[name=rowChange]").click(meisaiChange);
	target.find("button[name=rowDelete]").click(meisaiDelete);
	target.find("button[name=rowUp]").click(meisaiUp);
	target.find("button[name=rowDown]").click(meisaiDown);
});

</script>
	</body>
</html>
