/**
 * トグルボタン押下時イベント
 */
function changeButton(value) {
	
	// 申請内容へ追加の場合、明細へ追加のボタン色をグレーに
	// 選択中のエリアの見出しの色を青色に
	if (value == "0") {
		$("input[name=toggleFlg]").val("0");
		$("button[name=toggleShinsei]").attr("class", "btn btn-primary");
		$("button[name=toggleMeisai]").attr("class", "btn");
		
		$("#shinseiSection").children('h2').css("color", "#0000FF"); 
		$("#shinseiSection").children('h2').css("border-color", "#0000FF"); 
		$("#meisaiSection").children('h2').css("color", ""); 
		$("#meisaiSection").children('h2').css("border-color", ""); 

		// 予算執行入力部品
		$("#yosanShikkouPartsShinsei").css("display", "block");
		$("#yosanShikkouPartsMeisai").css("display", "none");

	} else {
		$("input[name=toggleFlg]").val("1");
		$("button[name=toggleShinsei]").attr("class", "btn");
		$("button[name=toggleMeisai]").attr("class", "btn btn-primary");
		
		$("#meisaiSection").children('h2').css("color", "#0000FF"); 
		$("#meisaiSection").children('h2').css("border-color", "#0000FF"); 
		$("#shinseiSection").children('h2').css("color", ""); 
		$("#shinseiSection").children('h2').css("border-color", ""); 

		// 予算執行入力部品
		$("#yosanShikkouPartsShinsei").css("display", "none");
		$("#yosanShikkouPartsMeisai").css("display", "block");
	}
}

/**
 * エリア区分の取得
 */
function getAreaKbn() {
	
	var area_kbn = ($("input[name=toggleFlg]").val() == "0") ? "shinsei" : "meisai";
	
	if ("dialogKaniTodokeDiv" in window && dialogKaniTodokeDiv != null) {
		var item_name = dialogKaniTodokeDiv.find("input[name=item_name]").val();
		if (item_name.indexOf("shinsei") >= 0) {
			area_kbn = "shinsei";
		}
		if (item_name.indexOf("meisai") >= 0) {
			area_kbn = "meisai";
		}
	}
	
	return area_kbn;
}

/**
 * 入力項目を追加する
 */
function addInputItem(area_kbn, hyouji_jun, label_name, item_name, buhin_type, value_name, inputItem) {

	var area_div = "<div class='control-group " + area_kbn + " ui-state-default'>";														// エリアDIV
	var hissu_span = ($("input:radio[name=hissuFlg]:checked").val() == "1") ? "<span class='required'>*</span>" : "";					// 必須フラグ

	// 項目数インクリメント
	$("input[name=" + area_kbn + "ItemCnt]").val(hyouji_jun);

	// HTML 
	var innerHtml = "";

	innerHtml += "<label class='control-label'>{hissu_flg}{label_name}</label>";
	innerHtml += "<input type='hidden' name='item_name'  value='{item_name}'>";	
	innerHtml += "<input type='hidden' name='buhin_type' value='{buhin_type}'>";
	innerHtml += "    <div class='controls'>";
	innerHtml += "        {input_item}";
	innerHtml += "        <button type='button' name='rowChange' class='btn btn-mini' style='margin-left:3px'>変更</button>";
	innerHtml += "        <button type='button' name='rowDelete' class='btn btn-mini' style='margin-left:3px'>削除</button>";
	innerHtml += "        <button type='button' name='rowUp'     class='btn btn-mini' style='margin-left:3px'>↑</button>";
	innerHtml += "        <button type='button' name='rowDown'   class='btn btn-mini' style='margin-left:3px'>↓</button>";
	innerHtml += "    </div>";
	innerHtml = innerHtml.replace("{item_name}",item_name);		// 項目名
	innerHtml = innerHtml.replace("{buhin_type}",buhin_type);	// 部品タイプ
	innerHtml = innerHtml.replace("{hissu_flg}",hissu_span);	// 必須フラグ
	innerHtml = innerHtml.replace("{label_name}",label_name);	// ラベル名
	innerHtml = innerHtml.replace("{area_div}",area_div);		// エリアDIV
	innerHtml = innerHtml.split("{input_item}").join(inputItem);	// 入力項目

	var html = area_div + innerHtml + "</div>";

	// 追加 or 更新
	var append = true;
	if ("dialogKaniTodokeDiv" in window && dialogKaniTodokeDiv != null) {
		append = false; 
	}

	if (area_kbn == "shinsei") {
		if (append) {
			// 申請内容にHTMLを追記する
			$("#shinseiList").append(html);
		} else {

			// 変更前のDIV情報
			var dialogKaniTodokeDivBf = dialogKaniTodokeDiv.clone();
			var item_name_bf = dialogKaniTodokeDivBf.find("input[name=item_name]").val();
			var val1 = $('*[name=' + item_name_bf + '_val1]').val();
			var val2 = $('*[name=' + item_name_bf + '_val2]').val();

			// 申請内容にHTMLを更新する
			dialogKaniTodokeDiv.html(innerHtml);

			// デフォルト値の設定
			setDefaultValue(buhin_type, dialogKaniTodokeDivBf, dialogKaniTodokeDiv, val1, val2);
		}

		// ボタンのイベントを設定する
		$('input[name=' + value_name + ']').closest('div').find("button[name=rowChange]").click(rowChange);
		$('input[name=' + value_name + ']').closest('div').find("button[name=rowDelete]").click(rowDelete);
		$('input[name=' + value_name + ']').closest('div').find("button[name=rowUp]").click(rowUp);
		$('input[name=' + value_name + ']').closest('div').find("button[name=rowDown]").click(rowDown);

		$('textarea[name=' + value_name + ']').closest('div').find("button[name=rowChange]").click(rowChange);
		$('textarea[name=' + value_name + ']').closest('div').find("button[name=rowDelete]").click(rowDelete);
		$('textarea[name=' + value_name + ']').closest('div').find("button[name=rowUp]").click(rowUp);
		$('textarea[name=' + value_name + ']').closest('div').find("button[name=rowDown]").click(rowDown);

		$('select[name=' + value_name + ']').closest('div').find("button[name=rowChange]").click(rowChange);
		$('select[name=' + value_name + ']').closest('div').find("button[name=rowDelete]").click(rowDelete);
		$('select[name=' + value_name + ']').closest('div').find("button[name=rowUp]").click(rowUp);
		$('select[name=' + value_name + ']').closest('div').find("button[name=rowDown]").click(rowDown);

		// 補助機能部品の再呼出
		commonInit($("#shinseiList").children());
	} 

	if (area_kbn == "meisai") {
		if (append) {
			// 明細にHTMLを追記する
			$("#meisaiList").append(html);
		} else {

			// 変更前のDIV情報
			var dialogKaniTodokeDivBf = dialogKaniTodokeDiv.clone();
			var item_name_bf = dialogKaniTodokeDivBf.find("input[name=item_name]").val();
			var val1 = $('*[name=' + item_name_bf + '_val1]').val();
			var val2 = $('*[name=' + item_name_bf + '_val2]').val();

			// 明細にHTMLを更新する
			dialogKaniTodokeDiv.html(innerHtml);

			// デフォルト値の設定
			setDefaultValue(buhin_type, dialogKaniTodokeDivBf, dialogKaniTodokeDiv, val1, val2);
		}

		// ボタンのイベントを設定する
		$('input[name=' + value_name + ']').closest('div').find("button[name=rowChange]").click(meisaiChange);
		$('input[name=' + value_name + ']').closest('div').find("button[name=rowDelete]").click(meisaiDelete);
		$('input[name=' + value_name + ']').closest('div').find("button[name=rowUp]").click(meisaiUp);
		$('input[name=' + value_name + ']').closest('div').find("button[name=rowDown]").click(meisaiDown);

		$('textarea[name=' + value_name + ']').closest('div').find("button[name=rowChange]").click(meisaiChange);
		$('textarea[name=' + value_name + ']').closest('div').find("button[name=rowDelete]").click(meisaiDelete);
		$('textarea[name=' + value_name + ']').closest('div').find("button[name=rowUp]").click(meisaiUp);
		$('textarea[name=' + value_name + ']').closest('div').find("button[name=rowDown]").click(meisaiDown);

		$('select[name=' + value_name + ']').closest('div').find("button[name=rowChange]").click(meisaiChange);
		$('select[name=' + value_name + ']').closest('div').find("button[name=rowDelete]").click(meisaiDelete);
		$('select[name=' + value_name + ']').closest('div').find("button[name=rowUp]").click(meisaiUp);
		$('select[name=' + value_name + ']').closest('div').find("button[name=rowDown]").click(meisaiDown);

		// 補助機能部品の再呼出
		commonInit($("#meisaiList").children());
	}
}

/**
 * デフォルト値の設定
 */
function setDefaultValue(buhin_type, divBf, divAf, val1, val2) {

	// 変更前のDIV情報
	var item_name_bf = divBf.find("input[name=item_name]").val();
	var item_bf = divBf.find("*[name=" + item_name_bf + "_val1]");

	// 変更後のDIV情報
	var item_name_af = divAf.find("input[name=item_name]").val();
	var item_af = divAf.find("*[name=" + item_name_af + "_val1]");

	switch (buhin_type) {
	case 'text': 

		var buhin_format_bf = null;
		var buhin_format_af = null;
		
		// 変更前後の部品形式の取得
		var buhinFormats = ["string", "number", "datepicker", "timepicker", "autoNumericWithCalcBox"];
		for ( var j = 0; j < buhinFormats.length; ++j ) {var buhinFormat = buhinFormats[j]; if (item_bf.hasClass(buhinFormat)) {buhin_format_bf = buhinFormat; break;}}
		for ( var j = 0; j < buhinFormats.length; ++j ) {var buhinFormat = buhinFormats[j]; if (item_af.hasClass(buhinFormat)) {buhin_format_af = buhinFormat; break;}}

		// 部品形式が変更前後で同じ場合、デフォルト値を引き継ぐ
		if (buhin_format_bf == buhin_format_af) {
			item_af.val(val1);
		}
		break;

	case 'textarea': 
		item_af.val(val1);
		break;

	case 'radio': 

		var item_bf_tmp = divBf.find("input[name=" + item_name_bf + "_val1_1]");
		var item_af_tmp = divAf.find("input[name=" + item_name_af + "_val1_1]");
		var valBfAry = "";
		var valAfAry = "";
		
		// 変更前後のオプション値を取得
		item_bf_tmp.each(function(){valBfAry = valBfAry + $(this).val();});
		item_af_tmp.each(function(){valAfAry = valAfAry + $(this).val();});

		// オプションが変更前後で同じ場合、デフォルト値を引き継ぐ
		if (valBfAry == valAfAry) {
			item_af_tmp.each(function(){
				if($(this).val() == val1) {
					$(this).prop("checked", true);
				}
			});
		}
		break;

	case 'pulldown': 

		var valBfAry = "";
		var valAfAry = "";
		var objBf = item_bf.children();
		var objAf = item_af.children();
		
		// 変更前後のオプション値を取得
		for( var i=0; i<objBf.length; i++ ){valBfAry = valBfAry + objBf.eq(i).val();}
		for( var i=0; i<objAf.length; i++ ){valAfAry = valAfAry + objAf.eq(i).val();}

		// オプションが変更前後で同じ場合、デフォルト値を引き継ぐ
		if (valBfAry == valAfAry) {
			item_af.val(val1);
		}
		break;
		
	case 'checkbox': 
		
		var item_af_tmp = divAf.find("input[name=" + item_name_af + "_val1_1]");
		item_af_tmp.prop("checked", (val1 == 1));
		break;
		
	case 'master': 
		
		var master_kbn_bf = divBf.find("input[name=master_kbn]").val();
		var master_kbn_af = divAf.find("input[name=master_kbn]").val();
		
		// マスター区分が変更前後で同じ場合、デフォルト値を引き継ぐ
		if (master_kbn_bf == master_kbn_af) {
			var item_af1 = divAf.find("input[name=" + item_name_af + "_val1]");
			var item_af2 = divAf.find("input[name=" + item_name_af + "_val2]");
			item_af1.val(val1);
			item_af2.val(val2);
		}
		
		break;
		
	}
}

/** 申請内容の行を変更する */
function rowChange() {commonRowChange($(this).closest("div.shinsei"), "shinsei");}
/** 申請内容の行を削除する */
function rowDelete() {if (0 == $("#shinseiList").children().length) {return;}$(this).closest("div.shinsei").remove();}
/** 申請内容の行を１行上に移動する */
function rowUp()   {var div = $(this).closest("div.shinsei");if(div.prev()) {div.insertBefore(div.prev());}}
/** 申請内容の行を１行下に移動する */
function rowDown() {var div = $(this).closest("div.shinsei");if(div.next()){div.insertAfter(div.next());}}
/** 明細の行を変更する */
function meisaiChange() {commonRowChange($(this).closest("div.meisai"), "meisai");}
/** 明細の行を削除する */
function meisaiDelete() {if (0 == $("#meisaiList").children().length) {return;}$(this).closest("div.meisai").remove();}
/** 明細の行を１行上に移動する */
function meisaiUp()   {var div = $(this).closest("div.meisai");if(div.prev()){div.insertBefore(div.prev());}}
/** 明細の行を１行下に移動する */
function meisaiDown() {var div = $(this).closest("div.meisai");if(div.next()){div.insertAfter(div.next());}}
/** 行変更(共通) */
function commonRowChange(div, area_kbn) {
	
	//Tel化されたテキストボックスを一時的にTextに戻す
	changeTypeTel();

	var dataList = new Array();
	var optionList = new Array();

	setItemDataListCommon(area_kbn, dataList, 0, div);
	setItemOptionListCommon(area_kbn, optionList, 0, div);

	/*
	 * データリストから要素を取得
	 */
	var data = dataList[0].split("\r\n");
	// 部品名
	var buhin_name = data[8];
	// 部品タイプ
	var buhin_type = data[9];
	// 予算執行項目ID
	var yosanId = data[15];

	// 部品タイプごとにダイアログを表示する
	switch (buhin_type) {
		case 'text': 
			switch (yosanId) {
				case 'ringi_kingaku':			addRingiKingaku(div, dataList[0], buhin_name);				break;
				case 'kenmei':					addKenmei(div, dataList[0], buhin_name);					break;
				case 'shuunyuu_kingaku_goukei':	addShuunyuuKingakuGoukei(div, dataList[0], buhin_name);		break;
				case 'shishutsu_kingaku_goukei':addShisyhutsuKingakuGoukei(div, dataList[0], buhin_name);	break;
				case 'shuushi_sagaku':			addShuushiSagaku(div, dataList[0], buhin_name);				break;
				case 'syuunyuu_kingaku':		addShuunyuuKingaku(div, dataList[0], buhin_name);			break;
				case 'syuunyuu_bikou':			addShuunyuuBikou(div, dataList[0], buhin_name);				break;
				case 'shishutsu_kingaku':		addShishutsuKingaku(div, dataList[0], buhin_name);			break;
				case 'shishutsu_bikou':			addShishutsuBikou(div, dataList[0], buhin_name);			break;
				default:
					commonAddText(div, dataList[0]);
					break;
			}
			break;

		case 'textarea': 
			switch (yosanId) {
				case 'naiyou_shinsei':			addNaiyouShinsei(div, dataList[0], buhin_name);				break;
				case 'naiyou_meisai':			addNaiyouMeisai(div, dataList[0], buhin_name);				break;
				default:
					commonAddTextArea(div, dataList[0]);
					break;
			}
			break;

		case 'radio':
			commonAddRadio(div, dataList[0], optionList);
			break;

		case 'checkbox': 
			addCheckBox(div, dataList[0]);
			break;

		case 'pulldown':  
			addPullDown(div, dataList[0], optionList);
			break;

		case 'master':  
			switch (yosanId) {
				case 'syuunyuu_bumon':			addShuunyuuBumon(div, dataList[0], buhin_name);				break;
				case 'syuunyuu_kamoku':			addShuunyuuKamoku(div, dataList[0], buhin_name);			break;
				case 'syuunyuu_eda_num':		addShuunyuuEda(div, dataList[0], buhin_name);				break;
				case 'shishutsu_bumon':			addShishutsuBumon(div, dataList[0], buhin_name);			break;
				case 'shishutsu_kamoku':		addShishutsuKamoku(div, dataList[0], buhin_name);			break;
				case 'shishutsu_eda_num':		addShishutsuEda(div, dataList[0], buhin_name);				break;
				default:
					addMasterItem(div, dataList[0]);
					break;
			}
			break;
	}
}

/**
 * イベントボタン押下時のアクションの切り替え
 */
function eventBtn(eventName) {

	//Tel化されたテキストボックスを一時的にTextに戻す
	changeTypeTel()
	
	// 要素名を変更する
	changeItemName("shinsei");
	changeItemName("meisai");

	// POSTするデータを作成する
	createPostData("shinsei");
	createPostData("meisai");
	
	var formObject = document.getElementById("kaniForm");

	switch (eventName) {
		case 'entry':
			
			formObject.action = 'kani_todoke_tsuika_touroku';
			break;
			
		case 'update':
			formObject.action = 'kani_todoke_henkou_koushin';
			break;
			
		case 'delete':
			if(!window.confirm('削除してもよろしいですか？')) {
				return;
			}
			formObject.action = 'kani_todoke_henkou_sakujo';
			break;
	}
	
	formObject.method = 'post';
	formObject.target = '_self';
	$(formObject).submit();
}

/**
 * 追加したアイテムの要素名を変更する
 */
function changeItemName(area_kbn) {
	
	var list = $("#" + area_kbn + "List");	
	var divArea  = list.find("div." + area_kbn);
	
	for (var i = 0; i < divArea.length; i++) {

		var div = divArea.eq(i);
		
		// 最初のinput要素を取得
		var input_item = div.find("input[name=item_name]");
		var input_type = div.find("input[name=buhin_type]");
		var input_value1 = null;
		
		var item_name = area_kbn + ("0" + (i + 1)).slice(-2);
		var buhin_type = input_type.val();
		
		switch (buhin_type) {
		
		  case 'text': 
			  input_item.val(item_name);
			  input_value1 = div.find("input[type=" + buhin_type + "]");
			  input_value1.attr('name', item_name + '_val1');
			  break;
			  
		  case 'textarea': 
			  input_item.val(item_name);
			  input_value1 = div.find(buhin_type);
			  input_value1.attr('name', item_name + '_val1');
			  break;

		  case 'radio': 
		  case 'checkbox': 
			  input_item.val(item_name);
			  input_value1 = div.children('div.controls').find("input[type=hidden]");
			  input_value1.attr('name', item_name + '_val1');
			  break;
			  
		  case 'pulldown':  
			  input_item.val(item_name);
			  input_value1 = div.find("select");
			  input_value1.attr('name', item_name + '_val1');
			  break;
			  
		  case 'master':  
			  input_item.val(item_name);
			  div.find("input[type=text]").each(function(){
				  var name = $(this).attr('name');
				  if (name.indexOf("val1") >= 0) {
					  $(this).attr('name', item_name + '_val1');
				  }
				  if (name.indexOf("val2") >= 0) {
					  $(this).attr('name', item_name + '_val2');
				  }
              });
			  
			  break;
			  
		  default : 
		  	  break;
		}
	}
}

/**
 * POSTデータを作成する
 */
function createPostData(area_kbn) {
	
	var dataList = new Array();
	var optionList = new Array();

	setItemDataList(area_kbn, dataList);
	setItemOptionList(area_kbn, optionList);

	for (var i = 0; i < dataList.length; i++) {
		$('<input>').attr({type: 'hidden', name: area_kbn + 'DataList', value: dataList[i]}).appendTo('#kaniForm');
	}

	for (var i = 0; i < optionList.length; i++) {
		$('<input>').attr({type: 'hidden', name: area_kbn + 'OptionList',value: optionList[i]}).appendTo('#kaniForm');
	}
}

/**
 * ブロック（申請内容・明細）ごとの要素の必要項目をデータリストに設定し、返却する
 */
function setItemDataList(area_kbn, dataList) {
	
	var list = $("#" + area_kbn + "List");	
	var divArea  = list.find("div." + area_kbn);
	
	for (var i = 0; i < divArea.length; i++) {
		var div = divArea.eq(i);
		setItemDataListCommon(area_kbn, dataList, i, div);
	}
}

function setItemDataListCommon(area_kbn, dataList, i, div) {

	var item_name               = ""; // 項目名
	var hyouji_jun              = ""; // 表示順
	var denpyou_edano           = ""; // 伝票枝番号
	var hissu_flg               = ""; // 必須フラグ
	var max_length              = ""; // 最大桁数
	var min_value               = ""; // 最大値
	var max_value               = ""; // 最大値
	var label_name              = ""; // ラベル名
	var buhin_type              = ""; // 部品タイプ
	var buhin_format            = ""; // 部品形式
	var buhin_width             = ""; // 部品幅
	var buhin_height            = ""; // 部品高さ
	var checkbox_label_name     = ""; // チェックボックスラベル名
	var master_kbn              = ""; // マスター区分
	var yosan_id                = ""; // 予算執行項目ID
	var decimal_point           = ""; // 小数点以下桁数
	var kotei_hyouji            = ""; // 固定表示

	// 最初のinput要素を取得
	var input_item = div.find("input[name=item_name]");
	var input_type = div.find("input[name=buhin_type]");
	var input_value1 = null;
	var label = div.find("label").first();

	// 項目名
	item_name = input_item.val();

	// 部品タイプ
	buhin_type = input_type.val();

	// ラベル名
	label_name = label.text().replace("*", "");

	// 必須フラグ
	hissu_flg = (label.find("span").text() == "*") ? "1" : "0";

	// 表示順
	hyouji_jun = i + 1;

	// 伝票枝番号
	denpyou_edano = 1;

	switch (buhin_type) {

		case 'text': 

			// 入力値
			input_value1 = div.find("input[type=" + buhin_type + "]");

			// 最大桁数
			max_length = input_value1.attr('maxlength');

			// 小数点以下桁数
			decimal_point = input_value1.attr('decimalPoint');

			// 固定表示
			kotei_hyouji = input_value1.attr('koteiHyouji');

			// 最小値・最大値
			min_value = div.find("input[name=min_value]").val();
			max_value = div.find("input[name=max_value]").val();

			// 部品形式
			var buhinFormats = ["string", "number", "datepicker", "timepicker", "autoNumericWithCalcBox"];
			for ( var j = 0; j < buhinFormats.length; ++j ) {
				var buhinFormat = buhinFormats[j];
				if (input_value1.hasClass(buhinFormat)) {
					buhin_format = buhinFormat;
					break;
				}
			}

			// 部品幅
			var buhinWidths = ["input-small", "input-medium", "input-xxlarge", "input-block-level"];
			for ( var j = 0; j < buhinWidths.length; ++j ) {
				var buhinWidth = buhinWidths[j];
				if (input_value1.hasClass(buhinWidth)) {
					buhin_width = buhinWidth;
					break;
				}
			}

			// 予算執行項目ID
			yosan_id = input_value1.attr('yosanId');

			break;

		case 'textarea': 

			// 入力値
			input_value1 = div.find(buhin_type);

			// 最大桁数
			max_length = input_value1.attr('maxlength');

			// 固定表示
			kotei_hyouji = input_value1.attr('koteiHyouji');
			
			// 部品幅
			var buhinWidths = ["input-small", "input-medium", "input-xxlarge", "input-block-level"];
			for ( var j = 0; j < buhinWidths.length; ++j ) {
				var buhinWidth = buhinWidths[j];
				if (input_value1.hasClass(buhinWidth)) {
					buhin_width = buhinWidth;
					break;
				}
			}

			// 部品高さ
			var buhinHeights = ["textarea", "textarea-medium", "textarea-height"];
			for ( var j = 0; j < buhinHeights.length; ++j ) {
				var buhinHeight = buhinHeights[j];
				if (input_value1.hasClass(buhinHeight)) {
					buhin_height = buhinHeight;
					break;
				}
			}

			// 予算執行項目ID
			yosan_id = input_value1.attr('yosanId');

			break;

		case 'checkbox': 

			// 入力値
			var input_hidden = div.children('div.controls').find("input[type=hidden]");

			if (div.find("input[type=" + buhin_type + "]:checked").val() == 1) {
				input_hidden.val(1);
			} else {
				input_hidden.val(0);
			}

			// チェックボックスラベル名
			checkbox_label_name = div.find("input[type=" + buhin_type + "]").parent().text();

			break;

		case 'radio': 

			// 入力値
			var input_hidden = div.children('div.controls').find("input[type=hidden]");
			var input_radio  = div.find("input[type=" + buhin_type + "]:checked");
			input_hidden.val(input_radio.val());

			break;

		case 'master': 

			/*
			 * 入力値
			 */
			var input_value1 = div.find("input[name=master_kbn]");

			// マスター区分
			master_kbn = input_value1.val();

			// 予算執行項目ID
			yosan_id = input_value1.attr('yosanId');
			break;
	}

	var array = [];

	array.push(item_name);                 // 項目名
	array.push(area_kbn);                  // エリア区分
	array.push(hyouji_jun);                // 表示順
	array.push(denpyou_edano);             // 伝票枝番号
	array.push(hissu_flg);                 // 必須フラグ
	array.push(max_length);                // 最大桁数
	array.push(min_value);                 // 最小値
	array.push(max_value);                 // 最大値
	array.push(label_name);                // ラベル名
	array.push(buhin_type);                // 部品タイプ
	array.push(buhin_format);              // 部品形式
	array.push(buhin_width);               // 部品幅
	array.push(buhin_height);              // 部品高さ
	array.push(checkbox_label_name);       // チェックボックスラベル名
	array.push(master_kbn);                // マスター区分
	array.push(yosan_id);                  // 予算執行項目ID
	array.push(decimal_point);             // 小数点以下桁数
	array.push(kotei_hyouji);              // 固定表示
	
	dataList.push(array.join("\r\n"));
}

/**
 * ブロック（申請内容・明細）ごとの要素の必要項目をオプションリストに設定し、返却する
 */
function setItemOptionList(area_kbn, optionList) {
	
	var list = $("#" + area_kbn + "List");	
	var divArea  = list.find("div." + area_kbn);
	
	for (var i = 0; i < divArea.length; i++) {
		var div = divArea.eq(i);
		setItemOptionListCommon(area_kbn, optionList, i, div);
	}
}

/**
 * ブロック（申請内容・明細）ごとの要素の必要項目をオプションリストに設定し、返却する
 */
function setItemOptionListCommon(area_kbn, optionList, i, div) {
	
	var input_item = div.find("input[name=item_name]");
	var input_type = div.find("input[name=buhin_type]");

	// 項目名
	var item_name = input_item.val();

	// 部品タイプ
	var buhin_type = input_type.val();
	
	switch (buhin_type) {
	
		case 'radio': 
		
    		// 表示順
    		var hyouji_jun = 0;
			
    		div.find("input[type=" + buhin_type + "]").each(function(){
                
            	hyouji_jun += 1;
            	
				var item = $(this).attr('data-item');  // 選択項目名
				var name = $(this).attr('data-text');  // 名称
				var value = $(this).val();             // 値
				var array = [];
				
				array.push(item_name);                 // 項目名
				array.push(area_kbn);                  // エリア区分
				array.push(hyouji_jun);                // 表示順
				array.push(name);                      // 名称
				array.push(value);                     // 値
				array.push(item);                      // 選択項目名
				
				optionList.push(array.join("\r\n"));
            });
    		
			break;
		
		case 'pulldown': 

    	    var pulldown = div.find("select").children();
    	    
    	    for (var j = 0; j < pulldown.length; j++ ){

    	    	var item = pulldown.eq(j).attr('data-item');  // 選択項目名
				var name = pulldown.eq(j).text();      // 名称
				var value = pulldown.eq(j).val();      // 値
				var array = [];

				array.push(item_name);                 // 項目名
				array.push(area_kbn);                  // エリア区分
				array.push((j + 1));                   // 表示順
				array.push(name);                      // 名称
				array.push(value);                     // 値
				array.push(item);                      // 選択項目名
				
				optionList.push(array.join("\r\n"));
    	    }

			break;
	}
}

/**
 * HTMLエスケープが含まれているか判定する
 */
function containEscapeHTML(html) {
	if (html != jQuery('<div>').text(html).html()) {
		return true;
	}
	if (html.indexOf(",") >= 0) {
		return true;
	}
	if (html.indexOf("$'") >= 0) {
		return true;
	}
	return false;
}

/**
 * 簡易ダイアログ<br>
 * 
 * @param div 入力部品を挿入する申請内容or明細のDIV
 * @param data ユーザー定義届書の入力部品子画面に渡すパラメータを格納
 * @param optionList 選択要素情報（プルダウン／ラジオの場合に使用）
 * @param title ユーザー定義届書の入力部品子画面タイトル（後に追加or変更が追記される）
 * @param url ユーザー定義届書の入力部品子画面ID
 * @param yosanShikkouId 予算執行項目ID（予算執行入力部品を識別するID）
 */
function commonKaniDialog(div, data, optionList, title, url, yosanShikkouId) {

	var titleTmp = title;
	var urlTmp = url;

	if(typeof data === "undefined") {
		titleTmp = titleTmp + "追加";
		dialogKaniTodokeDiv = null;
		// 予算執行の入力部品はパラメータを追加する。
		if(typeof yosanShikkouId !== "undefined") {
			urlTmp = urlTmp + "?yosanId=" + encodeURI(yosanShikkouId);
		}

	} else {
		titleTmp = titleTmp + "変更";
		dialogKaniTodokeDiv = div;
		if(optionList == null) {
			urlTmp = urlTmp + "?data=" + encodeURIComponent(data);
			// 予算執行の入力部品はパラメータを追加する。
			if(typeof yosanShikkouId !== "undefined") {
				urlTmp = urlTmp + "&yosanId=" + encodeURI(yosanShikkouId);
			}

		} else {
			
			var option = "";
			for (var i = 0; i < optionList.length; i++) {
				option = option + "&optionList=" + encodeURIComponent(optionList[i]);
			}
			
			urlTmp = urlTmp + "?data=" + encodeURIComponent(data) + option;
		}
	}
	
	var width = 550;
	var height = 500;
	
	// ユーザー定義届書マスターのみ画面サイズ変更
	if (url == "kani_todoke_master_item_add") {
		width = 950;
	}
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: height,
		title: titleTmp,
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load(urlTmp);
}
function commonAddText(div, data)              {commonKaniDialog(div, data, null,       "届書テキスト", "kani_todoke_text_add");}
function commonAddTextArea(div, data)          {commonKaniDialog(div, data, null,       "届書テキストエリア", "kani_todoke_textarea_add");}
function commonAddRadio(div, data, optionList) {commonKaniDialog(div, data, optionList, "届書ラジオ", "kani_todoke_radio_add");}
function addPullDown(div, data, optionList)    {commonKaniDialog(div, data, optionList, "届書プルダウン", "kani_todoke_pulldown_add");}
function addCheckBox(div, data)                {commonKaniDialog(div, data, null,       "届書チェックボックス", "kani_todoke_checkbox_add");}
function addMasterItem(div, data)              {commonKaniDialog(div, data, null,       "届書マスター", "kani_todoke_master_item_add");}

/*
 * 予算執行入力部品（申請内容）
 */
function addRingiKingaku(div, data) {
	// 稟議金額
	var koumokuId = "ringi_kingaku";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#shinseiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 稟議金額 ", "kani_todoke_text_add", koumokuId);
}
function addKenmei(div, data) {
	// 件名
	var koumokuId = "kenmei";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#shinseiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 件名 ", "kani_todoke_text_add", koumokuId);
}
function addNaiyouShinsei(div, data) {
	// 内容
	var koumokuId = "naiyou_shinsei";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#shinseiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 内容 ", "kani_todoke_textarea_add", koumokuId);
}
function addShuunyuuKingakuGoukei(div, data) {
	// 収入金額合計
	var koumokuId = "shuunyuu_kingaku_goukei";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#shinseiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 収入金額合計 ", "kani_todoke_text_add", koumokuId);
}
function addShisyhutsuKingakuGoukei(div, data) {
	// 支出金額合計
	var koumokuId = "shishutsu_kingaku_goukei";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#shinseiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 支出金額合計 ", "kani_todoke_text_add", koumokuId);
}
function addShuushiSagaku(div, data) {
	// 収支差額
	var koumokuId = "shuushi_sagaku";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#shinseiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 収支差額 ", "kani_todoke_text_add", koumokuId);
}
/*
 * 予算執行入力部品（明細）
 */
function addShuunyuuBumon(div, data) {
	// 収入部門
	var koumokuId = "syuunyuu_bumon";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#meisaiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 収入部門 ", "kani_todoke_master_item_add", koumokuId);
}
function addShuunyuuKamoku(div, data) {
	// 収入科目
	var koumokuId = "syuunyuu_kamoku";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#meisaiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 収入科目 ", "kani_todoke_master_item_add", koumokuId);
}
function addShuunyuuEda(div, data) {
	// 収入枝番
	var koumokuId = "syuunyuu_eda_num";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#meisaiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 収入枝番 ", "kani_todoke_master_item_add", koumokuId);
}
function addShuunyuuKingaku(div, data) {
	// 収入金額
	var koumokuId = "syuunyuu_kingaku";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#meisaiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 収入金額 ", "kani_todoke_text_add", koumokuId);
}
function addShuunyuuBikou(div, data) {
	// 収入備考
	var koumokuId = "syuunyuu_bikou";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#meisaiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 収入備考 ", "kani_todoke_text_add", koumokuId);
}
function addShishutsuBumon(div, data) {
	// 支出部門
	var koumokuId = "shishutsu_bumon";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#meisaiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 支出部門 ", "kani_todoke_master_item_add", koumokuId);
}
function addShishutsuKamoku(div, data) {
	// 支出科目
	var koumokuId = "shishutsu_kamoku";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#meisaiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 支出科目 ", "kani_todoke_master_item_add", koumokuId);
}
function addShishutsuEda(div, data) {
	// 支出枝番
	var koumokuId = "shishutsu_eda_num";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#meisaiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 支出枝番 ", "kani_todoke_master_item_add", koumokuId);
}
function addShishutsuKingaku(div, data) {
	// 支出金額
	var koumokuId = "shishutsu_kingaku";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#meisaiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 支出金額 ", "kani_todoke_text_add", koumokuId);
}
function addShishutsuBikou(div, data) {
	// 支出備考
	var koumokuId = "shishutsu_bikou";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#meisaiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 支出備考 ", "kani_todoke_text_add", koumokuId);
}
function addNaiyouMeisai(div, data) {
	// 内容
	var koumokuId = "naiyou_meisai";
	// dataがundefinedの場合（入力部品追加）に入力部品の重複チェックを行う
	if (typeof data === "undefined") {
		if (chkCyoufukuNyuuryokuBuhin($('#meisaiList'), koumokuId, this.innerText)){
			return;
		}
	}
	commonKaniDialog(div, data, null, "届書 内容 ", "kani_todoke_textarea_add", koumokuId);
}

/**
 * 予算執行入力部品の重複チェック<br>
 * 
 * @param searchSection 配置先セクション
 * @param koumokuId 予算執行項目ID
 * @param buhinNm 入力部品名
 * @return true:重複あり false:重複なし
 */
function chkCyoufukuNyuuryokuBuhin(searchSection, koumokuId, buhinNm) {
	var result = false;
	// 予算執行項目IDを持つ入力部品を取得
	var objs = searchSection.find("[yosanid]");
	for (var i = 0; i < objs.length; i++) {
		// 入力部品の属性に同じ予算執行項目IDが設定されているものを検索
		for (var j = 0; j < objs[i].attributes.length; j++){
			// 存在する場合は重複ありとする
			if (koumokuId === objs[i].attributes[j].value){
				result = true;
				break;
			}
		}
		if (result) {
			break;
		}
	}
	if (result) {
		alert(buhinNm + "の入力部品は複数追加できません。");
	}
	return result;
}

//勘定科目選択時
function kanjyouKamokuSentaku(btn) {
	var btnNm = $(btn).attr("name");
	var parent = $(btn).parent();
	var val1 = btnNm.replace("btn", "val1");
	var val2 = btnNm.replace("btn", "val2");
 	dialogRetKamokuCd   = parent.find("input[name=" + val1 + "]");
 	dialogRetKamokuName = parent.find("input[name=" + val2 + "]");
 	var preKamokuCd = dialogRetKamokuCd.val();

	// 押下されたボタンの３つ上にあるhiddenのmaster_kbnから
	// 予算執行項目IDを取得
	var yosanId = $(btn).prev().prev().prev().attr("yosanId");
	// 押下されたボタンが存在する明細番号を取得する。
	var meisaiNum = parent.parent().parent().parent().find("h2 *.number").text();
	// 科目選択時には科目枝番をクリア
	dialogCallbackKanjyouKamokuSentaku = function(){
		if (preKamokuCd != dialogRetKamokuCd.val()){
			kanjyouKamokuEdabanClear(btn, yosanId, meisaiNum);
		}
	};
 	commonKamokuSentaku();
}

//勘定科目コードロストフォーカス時Function
function kanjyouKamokuSentakuLostFocus(input) {
 	var title = $(input).parent().parent().find(".control-label").text();
	var inputNm = $(input).attr("name");
	var parent = $(input).parent();
	var val1 = inputNm.replace("val1", "val1");
	var val2 = inputNm.replace("val1", "val2");
 	dialogRetKamokuCd   = parent.find("input[name=" + val1 + "]");
 	dialogRetKamokuName = parent.find("input[name=" + val2 + "]");
	commonKamokuCdLostFocus(dialogRetKamokuCd, dialogRetKamokuName, title);
}

// 負担部門選択時
function futanBumonSentaku(btn) {
	var btnNm = $(btn).attr("name");
	var parent = $(btn).parent();
	var val1 = btnNm.replace("btn", "val1");
	var val2 = btnNm.replace("btn", "val2");
	dialogRetFutanBumonCd   = parent.find("input[name=" + val1 + "]");
	dialogRetFutanBumonName = parent.find("input[name=" + val2 + "]");
	//ユーザー定義届書マスター登録時か伝票編集中かに関わらずユーザもしくは部門のセキュリティコードを参照させる
	commonFutanBumonSentaku("1","",$("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val());
}

//負担部門コードロストフォーカス時Function
function futanBumonSentakuLostFocus(input) {
 	var title = $(input).parent().parent().find(".control-label").text();
	var inputNm = $(input).attr("name");
	var parent = $(input).parent();
	var val1 = inputNm.replace("val1", "val1");
	var val2 = inputNm.replace("val1", "val2");
	dialogRetFutanBumonCd   = parent.find("input[name=" + val1 + "]");
	dialogRetFutanBumonName = parent.find("input[name=" + val2 + "]");
	commonFutanBumonCdLostFocus("1",dialogRetFutanBumonCd, dialogRetFutanBumonName, title,$("input[name=denpyouId]").val(),$("input[name=kihyouBumonCd]").val());
}

// 取引先選択時
function torihikisakiSentaku(btn) {
	var btnNm = $(btn).attr("name");
	var parent = $(btn).parent();
	var val1 = btnNm.replace("btn", "val1");
	var val2 = btnNm.replace("btn", "val2");
	dialogRetTorihikisakiCd   = parent.find("input[name=" + val1 + "]");
	dialogRetTorihikisakiName = parent.find("input[name=" + val2 + "]");
	commonTorihikisakiSentaku();
}

// 取引先コードロストフォーカス時Function
function torihikisakiSentakuLostFocus(input) {
 	var title = $(input).parent().parent().find(".control-label").text();
	var inputNm = $(input).attr("name");
	var parent = $(input).parent();
	var val1 = inputNm.replace("val1", "val1");
	var val2 = inputNm.replace("val1", "val2");
	dialogRetTorihikisakiCd   = parent.find("input[name=" + val1 + "]");
	dialogRetTorihikisakiName = parent.find("input[name=" + val2 + "]");
	commonTorihikisakiCdLostFocus(dialogRetTorihikisakiCd, dialogRetTorihikisakiName, title);
}

//UF1選択時
function uf1Sentaku(btn) {
	var btnNm = $(btn).attr("name");
	var parent = $(btn).parent();
	var val1 = btnNm.replace("btn", "val1");
	var val2 = btnNm.replace("btn", "val2");
	dialogRetUniversalFieldCd   = parent.find("input[name=" + val1 + "]");
	dialogRetUniversalFieldName = parent.find("input[name=" + val2 + "]");
	commonUniversalSentaku("", 1);
}

//UF1コードロストフォーカス時Function
function uf1SentakuLostFocus(input) {
 	var title = $(input).parent().parent().find(".control-label").text();
	var inputNm = $(input).attr("name");
	var parent = $(input).parent();
	var val1 = inputNm.replace("val1", "val1");
	var val2 = inputNm.replace("val1", "val2");
	dialogRetUniversalFieldCd   = parent.find("input[name=" + val1 + "]");
	dialogRetUniversalFieldName = parent.find("input[name=" + val2 + "]");
	commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 1, title);
}

// UF2選択時
function uf2Sentaku(btn) {
	var btnNm = $(btn).attr("name");
	var parent = $(btn).parent();
	var val1 = btnNm.replace("btn", "val1");
	var val2 = btnNm.replace("btn", "val2");
	dialogRetUniversalFieldCd   = parent.find("input[name=" + val1 + "]");
	dialogRetUniversalFieldName = parent.find("input[name=" + val2 + "]");
	commonUniversalSentaku("", 2);
}

//UF2コードロストフォーカス時Function
function uf2SentakuLostFocus(input) {
 	var title = $(input).parent().parent().find(".control-label").text();
	var inputNm = $(input).attr("name");
	var parent = $(input).parent();
	var val1 = inputNm.replace("val1", "val1");
	var val2 = inputNm.replace("val1", "val2");
	dialogRetUniversalFieldCd   = parent.find("input[name=" + val1 + "]");
	dialogRetUniversalFieldName = parent.find("input[name=" + val2 + "]");
	commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 2, title);
}

// UF3選択時
function uf3Sentaku(btn) {
	var btnNm = $(btn).attr("name");
	var parent = $(btn).parent();
	var val1 = btnNm.replace("btn", "val1");
	var val2 = btnNm.replace("btn", "val2");
	dialogRetUniversalFieldCd   = parent.find("input[name=" + val1 + "]");
	dialogRetUniversalFieldName = parent.find("input[name=" + val2 + "]");
	commonUniversalSentaku("", 3);
}

//UF3コードロストフォーカス時Function
function uf3SentakuLostFocus(input) {
 	var title = $(input).parent().parent().find(".control-label").text();
	var inputNm = $(input).attr("name");
	var parent = $(input).parent();
	var val1 = inputNm.replace("val1", "val1");
	var val2 = inputNm.replace("val1", "val2");
	dialogRetUniversalFieldCd   = parent.find("input[name=" + val1 + "]");
	dialogRetUniversalFieldName = parent.find("input[name=" + val2 + "]");
	commonUniversalCdLostFocus(dialogRetUniversalFieldCd, dialogRetUniversalFieldName, 3, title);
}

/**
 * 勘定科目枝番選択ボタン押下処理<br>
 * <ul>
 * <li>対応する科目に入力がない場合はメッセージを表示する</li>
 * <li>勘定科目枝番選択子画面を表示する</li>
 * </ul>
 * 
 * @param btn 勘定科目枝番の選択ボタン
 */
function kanjyouKamokuEdabanSentaku(btn) {
	var btnNm = $(btn).attr("name");
	var parent = $(btn).parent();
	var val1 = btnNm.replace("btn", "val1");
	var val2 = btnNm.replace("btn", "val2");
	dialogRetKamokuEdabanCd   = parent.find("input[name=" + val1 + "]");
	dialogRetKamokuEdabanName = parent.find("input[name=" + val2 + "]");

	// 押下されたボタンの３つ上にあるhiddenのmaster_kbnから
	// 予算執行項目IDを取得
	var yosanId = $(btn).prev().prev().prev().attr("yosanId");

	// 押下されたボタンが存在する明細番号を取得する。
	var meisaiNum = parent.parent().parent().parent().find("h2 *.number").text();


	// 勘定科目枝番選択共通処理を呼び出して、メッセージまたは子画面表示を行う。
	var result = kanjyouKamokuEdabanCommon(yosanId, meisaiNum).split(",");
	if ("" !== result[1]){
		alert(result[1]);
	}else{
		commonKamokuEdabanSentaku(result[0]);
	}
}

/**
 * 勘定科目枝番ロストフォーカス処理<br>
 * <ul>
 * <li>枝番コード値に入力がなければ処理しない</li>
 * <li>対応する科目に入力がない場合はメッセージを表示する</li>
 * <li>枝番コードに対応する名称を取得して枝番名称フィールドに表示する</li>
 * </ul>
 * 
 * @param input 勘定科目枝番のコード値フィールド
 */
// 勘定科目枝番コードロストフォーカス時Function
function kanjyouKamokuEdabanSentakuLostFocus(input) {
	var title = $(input).parent().parent().find(".control-label").text();
	var inputNm = $(input).attr("name");
	var parent = $(input).parent();
	var val1 = inputNm.replace("val1", "val1");
	var val2 = inputNm.replace("val1", "val2");
	dialogRetKamokuEdabanCd   = parent.find("input[name=" + val1 + "]");
	dialogRetKamokuEdabanName = parent.find("input[name=" + val2 + "]");

	// 枝番のコード値に入力がなければ名称をクリアして処理を終了
	var code = dialogRetKamokuEdabanCd.val();
	if(code == "" || code == null) {
		dialogRetKamokuEdabanName.val("");
		return;
	}

	// ロストフォーカスする枝番のコード値フィールドの１つ上にあるhiddenのmaster_kbnから
	// 予算執行項目IDを取得
	var yosanId = $(input).prev().attr("yosanId");

	// 押下されたボタンが存在する明細番号を取得する。
	var meisaiNum = parent.parent().parent().parent().find("h2 *.number").text();

	// 勘定科目枝番選択共通処理を呼び出して、メッセージまたは名称取得を行う。
	var result = kanjyouKamokuEdabanCommon(yosanId, meisaiNum).split(",");
	if ("" !== result[1]){
		alert(result[1]);
	}else{
		commonKamokuEdabanCdLostFocus(dialogRetKamokuEdabanCd, dialogRetKamokuEdabanName, result[0], title);
	}
}

/**
 * 勘定科目枝番選択共通処理<br>
 * 枝番の予算執行項目IDから対応する科目の入力値を取得して
 * 科目入力を先に入力させる必要がある場合、メッセージ文字列を返却する。
 * <ul>
 * <li>戻り値のメッセージ文字列に設定がある場合はメッセージを表示させること</li>
 * <li>上記以外の場合は戻り値の科目コード値を使用して子画面表示や名称取得の処理を行うこと</li>
 * </ul>
 * 
 * @param yosanId 予算執行項目ID
 * @param meisaiNum 明細番号
 * @return [科目コード値],[メッセージ文字列]
 */
function kanjyouKamokuEdabanCommon(yosanId, meisaiNum) {
	var retKamokuCd = "";
	var retMsg = "";

	// 枝番に対する科目を判別
	// 予算執行項目IDから対応する科目の予算執行項目IDを決定
	var kamokuId = "";
	var kamokuNm = "";
	if (yosanId == "syuunyuu_eda_num"){
		// 収入枝番の対応する科目は収入科目
		kamokuId = "syuunyuu_kamoku";
		kamokuNm = "収入科目"
	}else{
		// 支出枝番の対応する科目は支出科目
		kamokuId = "shishutsu_kamoku";
		kamokuNm = "支出科目"
	}

	// 対象明細を取得
	var meisaiObj = null;
	// 管理画面の時
	if (meisaiNum == "") {
		meisaiObj = $("[yosanid=" + yosanId + "]").parent().parent().parent();
	}
	else {
		var meisaiListObj = $('#meisaiList').find("h2 *.number");
		for (var j = 0; j < meisaiListObj.length; j++) {
			var number = meisaiListObj.eq(j).text();
			if (number == meisaiNum){
				meisaiObj = meisaiListObj.eq(j).parent().parent();
				break;
			}
		}
	}
	if (null == meisaiObj){
		return;
	}

	// 対応する科目から選択値を取得
	var kamokuCd = "";
	var objs = meisaiObj.find("[yosanid='" + kamokuId + "']");
	if (1 == objs.length){
		kamokuCd = objs[0].nextElementSibling.value;
		// 対応する科目の入力部品が存在する場合
		if ("" === kamokuCd){
			// 対応する科目に入力値がない場合
			retMsg = "対応する" + kamokuNm + "を先に選択して下さい。";
		}else{
			retKamokuCd = kamokuCd;
		}
	}else{
		// 対応する科目の入力部品が存在しない場合
		retMsg = "対応する" + kamokuNm + "の入力部品を追加して下さい。";
	}
	return retKamokuCd + "," + retMsg;
}

/**
 * マスタークリアボタン押下処理<br>
 * マスター部品のクリアボタンが押下された場合、そのコードと名称をクリアする。<br>
 * また、入力部品が科目の場合は対応する枝番のコードと名称もクリアする。<br>
 * 
 * @param btn マスター入力部品のクリアボタン
 */
function clearMaster(btn) {
	var btnNm = $(btn).attr("name");
	var parent = $(btn).parent();
	var val1 = btnNm.replace("clrbtn", "val1");
	var val2 = btnNm.replace("clrbtn", "val2");
	if("" != parent.find("input[name=" + val1 + "]").val() || "" != parent.find("input[name=" + val2 + "]").val()){isDirty = true;}
	parent.find("input[name=" + val1 + "]").val("");
	parent.find("input[name=" + val2 + "]").val("");

	// 押下されたボタンの４つ上にあるhiddenのmaster_kbnから予算執行項目IDを取得
	// 値が設定されていない場合は何もしない
	var yosanId = $(btn).prev().prev().prev().prev().attr("yosanId");
	if ("" !== yosanId){
		// 押下されたボタンが存在する明細番号を取得する。
		var meisaiNum = parent.parent().parent().parent().find("h2 *.number").text();
		// 対応する枝番の入力値をクリア
		kanjyouKamokuEdabanClear(btn, yosanId, meisaiNum);
	}
}

//Tel化されたテキストボックスを一時的にTextに戻す
function changeTypeTel(){
	$("#shinseiList").children().find("input.datepicker:enabled:not([readonly])").each(function(){
		$(this).attr("maxlength",10); //一時的にmaxlengthも設定
		$(this).get(0).type = 'text';
	});
	$("#shinseiList").children().find("input.timepicker:enabled:not([readonly])").each(function(){
		$(this).attr("maxlength",7); //一時的にmaxlengthも設定
		$(this).get(0).type = 'text';
	});
	$("#shinseiList").children().find("input.autoNumeric,input.autoNumericWithCalcBox,input.autoNumericNoMax").each(function(){
		$(this).get(0).type = 'text';
	});
	$("#meisaiList").children().find("input.datepicker:enabled:not([readonly])").each(function(){
		$(this).attr("maxlength",10); //一時的にmaxlengthも設定
		$(this).get(0).type = 'text';
	});
	$("#meisaiList").children().find("input.autoNumeric,input.autoNumericWithCalcBox,input.autoNumericNoMax").each(function(){
		$(this).get(0).type = 'text';
	});
	$("#meisaiList").children().find("input.timepicker:enabled:not([readonly])").each(function(){
		$(this).attr("maxlength",7); //一時的にmaxlengthも設定
		$(this).get(0).type = 'text';
	});
}

/**
 * 金額計算<br>
 * 「収入金額合計」「支出金額合計」「収支差額」の入力部品の存在を確認して
 * それぞれの金額を算出して反映する。
 */
function kingakuKeisan(){

	/*
	 * 収入金額合計を算出する。
	 */

	// 収入金額合計の入力部品を取得する。
	var objShuuNyuuGoukei = $('#shinseiList').find("[yosanid='shuunyuu_kingaku_goukei']");
	if (1 == objShuuNyuuGoukei.length){
		// 入力部品が存在する場合、明細の収入金額を合算する。
		var shoukei = 0;
		var objShuuNyuuKingaku = $('#meisaiList').find("[yosanid='syuunyuu_kingaku']");
		for (var i = 0; i < objShuuNyuuKingaku.length; i++){
			var value = objShuuNyuuKingaku[i].value.replace(/,/g, "");
			if ("" === value) {
				value = "0";
			}
			shoukei += parseInt(value);
		}
		// 合算値にカンマ編集を行って収入金額合計に設定する。
		var num = String(shoukei);
		while(num != (num = num.replace(/^(-?\d+)(\d{3})/, "$1,$2")));
		objShuuNyuuGoukei[0].value = num;
	}

	/*
	 * 支出金額合計を算出する。
	 */

	// 支出金額合計の入力部品を取得する。
	var objShishutsuGoukei = $('#shinseiList').find("[yosanid='shishutsu_kingaku_goukei']");
	if (1 == objShishutsuGoukei.length){
		// 入力部品が存在する場合、明細の支出金額を合算する。
		var shoukei = 0;
		var objShishutsuKingaku = $('#meisaiList').find("[yosanid='shishutsu_kingaku']");
		for (var i = 0; i < objShishutsuKingaku.length; i++){
			var value = objShishutsuKingaku[i].value.replace(/,/g, "");
			if ("" === value) {
				value = "0";
			}
			shoukei += parseInt(value);
		}
		// 合算値にカンマ編集を行って支出金額合計に設定する。
		var num = String(shoukei);
		while(num != (num = num.replace(/^(-?\d+)(\d{3})/, "$1,$2")));
		objShishutsuGoukei[0].value = num;
	}

	/*
	 * 収支差額を算出する。
	 */

	// 収支差額の入力部品を取得する。
	var objShuushiSagaku = $('#shinseiList').find("[yosanid='shuushi_sagaku']");
	if (1 == objShuushiSagaku.length){
		// 入力部品が存在する場合、収入金額合計－支出金額合計を計算する。
		var shuunyuu = parseInt(objShuuNyuuGoukei[0].value.replace(/,/g, ""));
		var shishutsu = parseInt(objShishutsuGoukei[0].value.replace(/,/g, ""));
		var shoukei = shuunyuu - shishutsu;
		// 計算結果にカンマ編集を行って収支差額に設定する。
		var num = String(shoukei);
		while(num != (num = num.replace(/^(-?\d+)(\d{3})/, "$1,$2")));
		objShuushiSagaku[0].value = num;
	}
}

/**
 * 負担部門コードチェンジ時Function<br>
 * 何もしない。
 */
function futanBumonSentakuChange(input) {
	return;
}

/**
 * 勘定科目コードチェンジ時Function<br>
 */
function kanjyouKamokuSentakuChange(input) {

	// 対象オブジェクトの１つ上にあるhiddenのmaster_kbnから
	// 予算執行項目IDを取得
	var yosanId = $(input).prev().attr("yosanId");

	// 対象オブジェクトが存在する明細番号を取得する。
	var parent = $(input).parent().parent().parent().parent();
	var meisaiNum = parent.find("h2 *.number").text();

	// 勘定科目に対応する勘定科目枝番をクリアする。
	kanjyouKamokuEdabanClear(input, yosanId, meisaiNum);
}

/**
 * 勘定科目枝番クリア<br>
 * 勘定科目に対応する勘定科目枝番をクリアする。
 */
function kanjyouKamokuEdabanClear(input, yosanId, meisaiNum) {

	// 枝番に対する科目を判別
	// 予算執行項目IDから対応する科目の予算執行項目IDを決定
	var kamokuEdaId = "";
	if (yosanId == "syuunyuu_kamoku"){
		// 収入科目の対応する科目枝番は収入枝番
		kamokuEdaId = "syuunyuu_eda_num"
	}else if (yosanId == "shishutsu_kamoku"){
		// 支出科目の対応する科目枝番は支出枝番
		kamokuEdaId = "shishutsu_eda_num"
	}else{
		// 上記以外は対象外のため処理しない
		return;
	}

	// 対象明細を取得
	var meisaiObj = null;
	// 管理画面の時
	if (meisaiNum == "") {
		meisaiObj = $("[yosanid=" + yosanId + "]").parent().parent().parent();
	}
	else {
		var meisaiListObj = $('#meisaiList').find("h2 *.number");
		for (var j = 0; j < meisaiListObj.length; j++) {
			var number = meisaiListObj.eq(j).text();
			if (number == meisaiNum){
				meisaiObj = meisaiListObj.eq(j).parent().parent();
				break;
			}
		}
	}
	if (null == meisaiObj){
		return;
	}

	// 対応する科目枝番のコードと名称をクリアする
	var objs = meisaiObj.find("[yosanid='" + kamokuEdaId + "']");
	if (1 == objs.length){
		objs[0].nextElementSibling.value = "";
		objs[0].nextElementSibling.nextElementSibling.value = "";
	}
	return;
}
