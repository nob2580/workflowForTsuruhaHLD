<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script>
/** URL「＋」ボタン押下時Function */
function urlAdd() {

	//添付ファイル合計が10以上なら中止
	if(10 <= $("#urlList div.newurlDiv").length ) return;

	//１行目をコピー
	var div = $("<div class='newurlDiv'>" + $("#urlList div:first").html() + "<\/div>");
	div.children().children().remove("span:gt(0)");
	//div.children().children().remove("br");
	$("#urlList").append(div);

	//中身を消してアクション紐付け
	inputClear(div);
	commonInit(div);

}

/** URL「－」ボタン押下時Function */
function urlDelete() {
	//1行しかないなら中止
	if($("div.newurlDiv").length <= 1) return;

	$(this).closest("div").parent("div.newurlDiv").remove();
}
</script>