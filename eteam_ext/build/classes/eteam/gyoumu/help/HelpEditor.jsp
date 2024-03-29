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
		<title>ヘルプ編集｜<%=EteamSymbol.SYSTEM_NAME%></title>
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>
		<meta name='description' content=''>
		<meta name='author' content=''>
		<meta http-equiv="X-UA-Compatible" content="IE=10" />
		<%@ include file="/jsp/eteam/include/Style.jsp" %>
		<link href="/eteam/static/css/jquery.cleditor.css" rel='stylesheet'/>
		<style type='text/css'>
<!--
-->
		</style>
	</head>

	<body>
    	<div id='wrap'>
			<!-- ヘッダー -->
			<%@ include file="/jsp/eteam/include/Header.jsp" %>

			<div id='content' class='container'>

				<!-- タイトル -->
				<h1>ヘルプ編集 (${su:htmlEscape(gamenName)})</h1>

				<!-- エラー -->
				<%@ include file="/jsp/eteam/include/InputError.jsp" %>
				
				<!-- メイン -->
				<div id='main'><form id='myForm' method='post' class='form-horizontal'>
					<input type='hidden' name='gamenId' value='${su:htmlEscape(gamenId)}'>
					<input type='hidden' name='gamenName' value='${su:htmlEscape(gamenName)}'>

					<!-- 表示 -->
					<section>
						<a href="gazou_upload" target="_blank">画像をアップロードする場合はこちら</a>
						<div>
							<textarea id="input" name="input">${input}</textarea>
						</div>
					</section>

					<!-- 処理ボタン -->
					<section>
						<button type='button' id='koushinButton' class='btn'><i class='icon-refresh'></i> 更新</button>
						<button type='button' id='clearButton' class='btn'><i class='icon-trash'></i> クリア</button>
<c:if test='${delBtnFlg == "1"}'>
						<button type='button' id='sakujoButton' class='btn'><i class='icon-remove'></i> 削除</button>
</c:if>
						<button type='button' id='modoruButton' class='btn'><i class='icon-arrow-left'></i> 戻る</button>
					</section>
				</form></div><!-- main -->
			</div><!-- content -->
			<div id='push'></div>
		</div><!-- wrap -->
		<!-- フッター -->
		<%@ include file="/jsp/eteam/include/Footer.jsp" %>

		<!-- スクリプト -->
		<%@ include file="/jsp/eteam/include/Script.jsp" %>
		<script src="/eteam/static/js/jquery.cleditor.min.js"></script>
		<script style='text/javascript'>

$(document).ready(function(){
	
    // ツールバーに表示する文字を指定する
    $.cleditor.buttons.bold.title = '太字';
	$.cleditor.buttons.italic.title = '斜体';
	$.cleditor.buttons.underline.title = '下線';
	$.cleditor.buttons.strikethrough.title = '取り消し線';
	$.cleditor.buttons.subscript.title = '下付き';
	$.cleditor.buttons.superscript.title = '上付き';
    $.cleditor.buttons.font.title = 'フォント';
    $.cleditor.buttons.size.title = 'フォントサイズ';
    $.cleditor.buttons.style.title = 'フォントスタイル';
    $.cleditor.buttons.color.title = 'フォント色';
    $.cleditor.buttons.highlight.title = 'ハイライト';
    $.cleditor.buttons.removeformat.title = 'フォーマット削除';
    $.cleditor.buttons.source.title = 'ソースの表示';
    $.cleditor.buttons.bullets.title = '箇条書き';
    $.cleditor.buttons.numbering.title = '段落番号';
    $.cleditor.buttons.outdent.title = 'インデントを減らす';
    $.cleditor.buttons.indent.title = 'インデントを増やす';
    $.cleditor.buttons.alignleft.title = '文字列を左に揃える';
    $.cleditor.buttons.center.title = '中央揃え';
    $.cleditor.buttons.alignright.title = '文字列を右に揃える';
    $.cleditor.buttons.justify.title = '両揃え';
    $.cleditor.buttons.rule.title = '罫線を追加する';
    $.cleditor.buttons.image.title = '画像を追加する';
    $.cleditor.buttons.link.title = 'リンクを追加する';
    $.cleditor.buttons.unlink.title = 'リンクを削除する';

    // リッチテキストの設定
	$("#input").cleditor({
		
		height: 500,
		controls:  "bold italic underline strikethrough subscript superscript | font size " +
	          "style | color highlight removeformat | bullets numbering | outdent " +
	          "indent | alignleft center alignright justify | " +
	          "rule image link unlink",
		styles: [["段落", "<p>"], ["見出し 1", "<h1>"], ["見出し 2", "<h2>"],["見出し 3", "<h3>"],  ["見出し 4","<h4>"],  ["見出し 5","<h5>"],["見出し 6","<h6>"]],
		docCSSFile: "../../static/css/cleditor_doc.css"
	});
	
	var editor = $("#input").cleditor()[0];
	editor.updateFrame();

	//ボタン押下
    $("#koushinButton").click(function() {
		$("#myForm").attr("action", "help_editor_koushin");
		$("#myForm").submit();
    });

	//クリアボタン押下
    $("#clearButton").click(function() {
        $("#input").val(null);
        var editor = $("#input").cleditor()[0];
        editor.updateFrame();
    });

	//削除ボタン押下
    $("#sakujoButton").click(function() {
		if(window.confirm('削除してもよろしいですか？')) {
			$("#myForm").attr("action", "help_editor_sakujo");
			$("#myForm").submit();
		}
    });

	//戻るボタン押下
    $("#modoruButton").click(function() {
		location.href = 'help_viewer?gamenId=${gamenId}';
    });
});
		</script>
	</body>
</html>
