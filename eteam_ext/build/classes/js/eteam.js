/** 「&nbsp;」の文字 */
var NBSP = String.fromCharCode(160);
/** focus対象 */
var ONFOCUS = "input[type!=hidden]:enabled:not([readonly]):visible,select[type!=hidden]:enabled:not([readonly]):visible,textarea[type!=hidden]:enabled:not([readonly]):visible,button[type!=hidden]:enabled:not([readonly]):visible";
/** 祝日リスト ※shukujitsu_listリクエストで上書くがエラー対策で初期値だけもっとく */
var shukujitsuList = [];
/** MAXのDECIMAL値 */
var MAX_DECIMAL = new Decimal("999999999999");

// dialogRet系グローバル変数。現時点ではエラーにならない最低限度だが、無印で定義されたグローバル変数はどこで定義されたか/未定義かを
// 検索では追いきれないので、いずれはここに全部洗い出したい
var dialogRetJigyoushaKbn;
var dialogRetKazeiKbn;
var dialogRetBunriKbn;
var dialogRetKariShiireKbn;
var dialogRetShoriGroup;
var dialogRetZeiritsuKbn;

//＜文字列系＞
/**
 * StringのFunction：文字列置換。
 * 使用例："xbc xx bb".replaceAll("x", "A")→"Abc AA bb"。
 * @param org 検索文字列
 * @param dest 置き換え文字列
 * @returns 置換後文字列
 */
String.prototype.replaceAll = function (org, dest) {
	return this.split(org).join(dest);
};

/**
 * StringのFunction：正の整数かどうか調べる
 * @returns 正の正数ならtrue、そうでなければfalse。
 */
String.prototype.isInteger = function () {
    var str = this.toString();
    var regex = /[^0-9]/;
    if (str.match(regex)) {
        return false;
    }
    return true;
};

/**
 * 全角カタカナを半角カタカナに変換
 * 撥音促音は撥音促音のまま、ハイフンなどは「-」に統一、アルファベットは小文字に統一)
 * 文字コード違いの空白文字の対応を追加
 */
String.prototype.zenkana2hankanaWithhyphen = function() {

	var str = this.toString();
	/** 半角ｶﾅ */
	var hankanaHyphen = new Array(
			'ｧ','ｨ','ｩ','ｪ','ｫ','ｬ','ｭ','ｮ','ｯ',
			'ｳﾞ','ｶﾞ','ｷﾞ','ｸﾞ','ｹﾞ','ｺﾞ','ｻﾞ','ｼﾞ','ｽﾞ','ｾﾞ','ｿﾞ','ﾀﾞ','ﾁﾞ','ﾂﾞ','ﾃﾞ','ﾄﾞ','ﾊﾞ','ﾋﾞ','ﾌﾞ','ﾍﾞ','ﾎﾞ','ﾊﾟ','ﾋﾟ','ﾌﾟ','ﾍﾟ','ﾎﾟ',
			'ｱ','ｲ','ｳ','ｴ','ｵ','ｶ','ｷ','ｸ','ｹ','ｺ','ｻ','ｼ','ｽ','ｾ','ｿ','ﾀ','ﾁ','ﾂ','ﾃ','ﾄ','ﾅ','ﾆ','ﾇ','ﾈ','ﾉ','ﾊ','ﾋ','ﾌ','ﾍ','ﾎ','ﾏ','ﾐ','ﾑ','ﾒ','ﾓ','ﾔ','ﾕ','ﾖ','ﾗ','ﾘ','ﾙ','ﾚ','ﾛ','ﾜ','ｦ','ﾝ',
			'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			'0','1','2','3','4','5','6','7','8','9',
			'ﾞ','ﾟ','(',')','{','}','[',']','.',',','/',
			'-','-','-',
			' ',' ',' ');
	/** 全角カナ */
	var zenkanaHyphen = new Array(
			'ァ','ィ','ゥ','ェ','ォ','ャ','ュ','ョ','ッ',
			'ヴ','ガ','ギ','グ','ゲ','ゴ','ザ','ジ','ズ','ゼ','ゾ','ダ','ヂ','ヅ','デ','ド','バ','ビ','ブ','ベ','ボ','パ','ピ','プ','ペ','ポ',
			'ア','イ','ウ','エ','オ','カ','キ','ク','ケ','コ','サ','シ','ス','セ','ソ','タ','チ','ツ','テ','ト','ナ','ニ','ヌ','ネ','ノ','ハ','ヒ','フ','ヘ','ホ','マ','ミ','ム','メ','モ','ヤ','ユ','ヨ','ラ','リ','ル','レ','ロ','ワ','ヲ','ン',
			'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
			'Ａ','Ｂ','Ｃ','Ｄ','Ｅ','Ｆ','Ｇ','Ｈ','Ｉ','Ｊ','Ｋ','Ｌ','Ｍ','Ｎ','Ｏ','Ｐ','Ｑ','Ｒ','Ｓ','Ｔ','Ｕ','Ｖ','Ｗ','Ｘ','Ｙ','Ｚ',
			'ａ','ｂ','ｃ','ｄ','ｅ','ｆ','ｇ','ｈ','ｉ','ｊ','ｋ','ｌ','ｍ','ｎ','ｏ','ｐ','ｑ','ｒ','ｓ','ｔ','ｕ','ｖ','ｗ','ｘ','ｙ','ｚ',
			'０','１','２','３','４','５','６','７','８','９',
			'゛','゜','（','）','｛','｝','［','］','．','，','／',
			'ー','－','ｰ',
			'　',' ',' ');	//3番目は文字コード違いの空白文字
	var henkanMatrix = [
		[hankanaHyphen,	zenkanaHyphen]
	];

	for(var i = 0; i < henkanMatrix.length; i++) {
		for(var j = 0; j < henkanMatrix[i][0].length; j++) {
			str = str.replace(new RegExp(henkanMatrix[i][1][j], 'g'), henkanMatrix[i][0][j]);
		}
	}
	return str;
};

//セレクタのエスケープ
String.prototype.selectorEscape = function() {
	var val = this.toString();
	return val.replace(/[ !"#$%&'()*+,.\/:;<=>?@\[\\\]^`{|}~]/g, '\\$&');
};

/**
 * HTMLエスケープ処理
 * @returns エスケープ後
 */
String.prototype.htmlEscape = function(){
	var str = this.toString();
	str = str.replace(/&/g,'&amp;');
	str = str.replace(/>/g,'&gt;');
	str = str.replace(/</g,'&lt;');
	return str;
};

/**
 * name中にwordを含むかどうかの判定（ブランクは除く）
 * @param name 検索対象文言
 * @param word 検索ワード
 */
function wordSearch(name, word) {

	var nameTrim = name.zenkana2hankanaWithhyphen().replace(/\s+/g, "");
	var wordTrim = word.zenkana2hankanaWithhyphen().replace(/\s+/g, "");
	return -1 < nameTrim.indexOf(wordTrim);
}

/**
 * URLを要素別ハッシュにする
 * ※
 * @returns str url?param1=value1&param2=value2...
 */
function getUrlParams(str) {
	var vars = {}, max = 0, url, hash = "", array = "";
	//?前をurlとして返す
	if (str.indexOf("?") == -1) {
		vars["url"] = str;
	} else {
		url = str.substring(0, str.indexOf("?"));
		vars["url"] = url;
	}
	//?後をクエリとして返す
	hash  = str.substring(str.indexOf("?") + 1, str.length).split('&');
	max = hash.length;
	for (var i = 0; i < max; i++) {
		array = hash[i].split('=');	//keyと値に分割。
		vars[array[0]] = array[1];	//先ほど確保したkeyに、値を代入。
	}
	return vars;
}

/**
 * ０詰め
 */
$.fn.zeropadding = function() {
	$(this).each(function(){
		if($(this).attr("maxlength")){
			var tmpVal = $(this).val();
			if ("" == tmpVal || tmpVal.match(/[^0-9]/)) {
				$(this).val("");
			} else {
				$(this).val(("00000000000000000000"  + tmpVal).slice(0 - $(this).attr("maxlength")));
			}
		}
	});
};

/**
 * FORMを連想配列に
 * @param form FORM要素の$
 * @returns
 */
function getFormValues(form) {
	var data = {};
	form.find("input, select, textarea").each(function() {
		var el = $(this);
		var name = el.attr("name");
		var value = el.val();
		if (typeof name === "undefined"
			||this.tagName.toLowerCase() === "input" && el.attr("type") === "checkbox" && !el.prop("checked")
			||this.tagName.toLowerCase() === "input" && el.attr("type") === "radio" && !el.prop("checked")
		) {
			return;
		}
		if (name in data) {
			var existsValue = data[name];
			if($.isArray(existsValue)){
				existsValue.push(value);
			}else{
				data[name] = [existsValue, value];
			}
		}
		else {
			data[name] = value;
		}
	});
	return data;
}

/**
 * チェックの付いたcheckboxのvalueを連結
 * @param btsForm
 * @param find
 * @returns
 */
function joinCheckboxValue(boxes, str){
	var arr = [];
	boxes.each(function(){
		arr.push($(this).val());
	});
	return arr.join(str)
}


//＜金額系＞
/**
 * カンマ区切り編集
 * @returns
 */
String.prototype.formatMoney = function() {
	return this.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
};

/**
 * 金額の値を取得する。
 * カンマを外した上、数値型で返す。
 * 空なら0を返す。
 * @return 数値型の値 or null
 */
$.fn.getMoney = function() {
	return ("" == this.val()) ? 0 : Number(this.autoNumeric("get"));
};
$.fn.getDecimal = function(){
	return ("" == this.val()) ? null : new Decimal(this.val().replaceAll(",",""));
};

/**
 * 金額の値をセットする。
 * 引数は数値型を想定。
 * 引数がnullならブランクになる。
 * @param num 数値型の値
 */
$.fn.putMoney = function(num) {
	if (null == num) {
		this.val("");
	} else {
		try {
		    // set メソッドを試みる
		    this.autoNumeric("set", num);
		} catch (error) {
			// 初期化がされていない場合、初期化を行う
			this.autoNumeric("init", {vMin: '-99999999999.99', vMax: '9999999999.99'});
			// set メソッドを再度呼び出す
			this.autoNumeric("set", num);
		}
	}
};
$.fn.putDecimal = function(dec){
	if (null == dec) {
		this.val("");
	} else {
		var str = dec.toString().split('.');
		if(str.length == 1){
			// 整数
			this.val(dec.toString().formatMoney());
		}else{
			// 小数桁あり
			this.val(str[0].toString().formatMoney().concat(".").concat(str[1]));
		}
	}
};

/**
 * セレクタのvalueを配列で取得
 * @return 文字列配列
 */
$.fn.valArray = function(){
	return this.map(function() {return $(this).val();}).get();
};

//キャッシュ無効
$.ajaxSetup({
	cache: false
});

/**
 * 入力項目（明細金額）の合計を計算して入力項目（合計金額）に反映する。
 * @param meisaikingaku 明細の金額（複数）を表すセレクタ
 * @param goukeiKingaku 合計の金額（単体）を表すセレクタ
 */
function amountKingaku(meisaikingaku, goukeiKingaku) {
	var allGaku = 0;
	var i;
	for (i = 0; i < meisaikingaku.length; i++) {
		if ("" != $(meisaikingaku[i]).val()) {
			allGaku = allGaku + parseInt($(meisaikingaku[i]).val().replaceAll(",", ""));
		}
		else
		{
			if ("" != $(meisaikingaku[i]).text()) {
				allGaku = allGaku + parseInt($(meisaikingaku[i]).text().replaceAll(",", ""));
			}
		}
	}
	goukeiKingaku.val(String(allGaku).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
}

//＜金額計算＞
/**
 * 明細の支払い金額より、本体金額、消費税額を計算
 * @param meisaiKingaku       明細の支払金額（単体）を表すセレクタ
 * @param meisaiHontaiKakaku  明細の本体金額（単体）を表すセレクタ
 * @param meisaiShouhizei     明細の消費税額（単体）を表すセレクタ
 * @param kazeiKbnGroup       課税区分グループ
 * @param shouhizeiritsu      消費税率
 * @param hasuuKeisanKbn      端数計算区分
 */
function calcShouhizei(meisaiKingaku, meisaiHontaiKakaku, meisaiShouhizei, kazeiKbnGroup, shouhizeiritsu, hasuuKeisanKbn) {

	//金額なし
	if(meisaiKingaku.val() == ""){return;}

	//消費税率を少数に変換
	var zeiritsu = shouhizeiritsu / 100;
	//金額からカンマを除く
	var valKingaku = eval(parseInt($(meisaiKingaku).val().replaceAll(",", "")));

	if(kazeiKbnGroup == "1"){ //税込
		valHontai = valKingaku / (1 + zeiritsu);
		valShouhi = valKingaku - valHontai;

		//端数処理
		valHontai = hasuuKeisan(valHontai, hasuuKeisanKbn, true);
		valShouhi = hasuuKeisan(valShouhi, hasuuKeisanKbn, false);

	}else if(kazeiKbnGroup == "2"){ //税抜
		valHontai = valKingaku;
		valShouhi = valKingaku * zeiritsu;

		//端数処理
		valShouhi = hasuuKeisan(valShouhi, hasuuKeisanKbn, false);

	}else if(kazeiKbnGroup == "9"){ //その他
		if(shouhizeiMeisaiFlag != 1){ //消費税明細でない明細
			valHontai = valKingaku;
			valShouhi = "";
		}else{ //消費税明細
			valHontai = "";
			valShouhi = "";
		}
	}else{}

	//カンマ付き書式に変換
	if(meisaiHontaiKakaku != null) $(meisaiHontaiKakaku).val(String(valHontai).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	if(meisaiShouhizei != null)	 $(meisaiShouhizei).val(String(valShouhi).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
}

/**
 * 自動計算額と調整差額から消費税合計を算出する
 * @param shouhizeigakuGoukei 消費税合計を表すセレクタ
 * @param jidoukeisangaku     自動計算額を表すセレクタ
 * @param chouseisagaku       調整差額を表すセレクタ
 */
function calcShouhizeiGoukei(shouhizeigakuGoukei, jidoukeisangaku, chouseisagaku) {

	var valGoukei = 0;
	var valJidou = 0;
	var valChousei = 0;

	if ("" != $(jidoukeisangaku).val())
	{
		valJidou = eval(parseInt($(jidoukeisangaku).val().replaceAll(",", "")));
	}

	if ("" != $(chouseisagaku).val())
	{
		valChousei = eval(parseInt($(chouseisagaku).val().replaceAll(",", "")));
	}

	valGoukei = eval(valJidou) + eval(valChousei);

	shouhizeigakuGoukei.val(String(valGoukei).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
}

/**
 * 本体金額合計と消費税額合計から支払金額合計を算出する
 * @param shiharaiGoukeiKingaku 支払合計金額を表すセレクタ
 * @param hontaiKingakuGoukei   本体金額合計を表すセレクタ
 * @param shouhizeigakuGoukei   消費税合計を表すセレクタ
 */
function calcShiharaiGoukei(shiharaiGoukeiKingaku, hontaiKingakuGoukei, shouhizeigakuGoukei) {

	var valGoukei = 0;
	var valHontai = 0;
	var valShouhi = 0;

	if ("" != $(hontaiKingakuGoukei).val())
	{
		valHontai = eval(parseInt($(hontaiKingakuGoukei).val().replaceAll(",", "")));
	}

	if ("" != $(shouhizeigakuGoukei).val())
	{
		valShouhi = eval(parseInt($(shouhizeigakuGoukei).val().replaceAll(",", "")));
	}

	valGoukei = eval(valHontai) + eval(valShouhi);

	shiharaiGoukeiKingaku.val(String(valGoukei).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
}
/**
 * 切上、切下、四捨五入
 * @param val 値
 * @param hasuuKeisanKbn 端数計算区分(1:切下、2:切上、3:四捨五入)
 * @param hasuukeisanKbnReverse 端数計算を逆転させる（端数計算区分は消費税計算用なので本体計算の場合は切上⇔切下を逆にみないといけない）
 */
function hasuuKeisan(val, hasuuKeisanKbn, hasuukeisanKbnReverse) {
	if (hasuukeisanKbnReverse) {
		     if ("1" == hasuuKeisanKbn) hasuuKeisanKbn = "2";
		else if ("2" == hasuuKeisanKbn) hasuuKeisanKbn = "1";
	}
	if ("1" == hasuuKeisanKbn) return Math.floor(val);
	if ("2" == hasuuKeisanKbn) return Math.ceil(val);
	if ("3" == hasuuKeisanKbn) return Math.round(val);
}

//calcShouhizeigakuを使用している箇所は無い　ただし、ここと同じロジックにしている拠点のJavaメソッドはある
/**
 * 消費税額計【財務拠点入出力用】
 */
function calcShouhizeigaku(kazeiKbnGroup, kingaku, taika, zeiritsu, hasuuShoriKbn){
	//対価が1より大きい→対価から消費税額を計算
	//対価が0のとき→金額から消費税額を計算
	if( taika > 0) kingaku = taika;
	if ("1" == kazeiKbnGroup) {
		//税込の場合
		var hontaiKingaku = hasuuKeisanZaimu(kingaku / (1 + zeiritsu), hasuuShoriKbn, true);
		return  kingaku - hontaiKingaku;

	}else if ("2" == kazeiKbnGroup) {
		//税抜の場合
		return  hasuuKeisanZaimu(kingaku * zeiritsu, hasuuShoriKbn, false);
	}
	return 0;
}

/**
 * 切上、切下、四捨五入【財務拠点入出力用】
 * @param val 値
 * @param hasuuShoriKbn 端数処理区分(0：切捨、1：切上、2：四捨五入)	※（期別）消費税設定の値
 * @param hasuukeisanKbnReverse 端数計算を逆転させる（端数計算区分は消費税計算用なので本体計算の場合は切上⇔切下を逆にみないといけない）
 */
function hasuuKeisanZaimuDouble(val, hasuuShoriKbn, hasuukeisanKbnReverse) {
	if (hasuukeisanKbnReverse) {
	     if ("0" == hasuuShoriKbn) hasuuShoriKbn = "1";
	else if ("1" == hasuuShoriKbn) hasuuShoriKbn = "0";
	}
	var afterCalc = Decimal.floor(val.mul(new Decimal(10))).div(new Decimal(10));
	if ("0" == hasuuShoriKbn) return Decimal.floor(afterCalc);
	if ("1" == hasuuShoriKbn) return Decimal.ceil(afterCalc);
	if ("2" == hasuuShoriKbn) return Decimal.round(afterCalc);
}

/**
 * 切上、切下、四捨五入【財務拠点入出力用】
 * @param val 値
 * @param hasuuShoriKbn 端数処理区分(0：切捨、1：切上、2：四捨五入)	※（期別）消費税設定の値
 * @param hasuukeisanKbnReverse 端数計算を逆転させる（端数計算区分は消費税計算用なので本体計算の場合は切上⇔切下を逆にみないといけない）
 */
function hasuuKeisanZaimuFromImporter(val, hasuuShoriKbn, hasuukeisanKbnReverse) {
	if (hasuukeisanKbnReverse) {
	     if ("0" == hasuuShoriKbn) hasuuShoriKbn = "1";
	else if ("1" == hasuuShoriKbn) hasuuShoriKbn = "0";
	}
	var dotfive = new Decimal('0.5');
	var afterCalc = Decimal.floor(new Decimal(val).abs().mul(new Decimal(10))).div(new Decimal(10));
	if ("0" == hasuuShoriKbn){
		if(0 != afterCalc){
			afterCalc = afterCalc.sub(dotfive);
		}
	}
	if ("1" == hasuuShoriKbn) {
		if(0 != afterCalc){
			afterCalc = afterCalc.add(new Decimal('0.4'));
		}
	}
	if(val > 0){
		afterCalc = afterCalc.add(dotfive).mul(Decimal.sign(afterCalc)).trunc();
	}else{
		afterCalc = afterCalc.add(dotfive).mul(Decimal.sign(afterCalc)).trunc().mul(new Decimal('-1'));
	}
	return afterCalc;
}

/**
 * 切上、切下、四捨五入
 * @param val 値
 * @param hasuuShoriKbn 端数処理区分(0：切捨、1：切上、2：四捨五入)	※（期別）消費税設定の値
 * @param hasuukeisanKbnReverse 端数計算を逆転させる（端数計算区分は消費税計算用なので本体計算の場合は切上⇔切下を逆にみないといけない）
 */
function hasuuKeisanZaimu(val, hasuuShoriKbn, hasuukeisanKbnReverse) {
	if (hasuukeisanKbnReverse) {
	     if ("0" == hasuuShoriKbn) hasuuShoriKbn = "1";
	else if ("1" == hasuuShoriKbn) hasuuShoriKbn = "0";
	}
	var dZero = new Decimal(0);
	// dZero < val → -1
	// dZero = val → 0
	// dZero > val → 1
	var minusflg = dZero.comparedTo(val) == 1;
	var afterCalc = Decimal.floor(val.abs().mul(new Decimal(10))).div(new Decimal(10));
	if ("0" == hasuuShoriKbn){
		afterCalc = afterCalc.floor();
	}
	if ("1" == hasuuShoriKbn) {
		afterCalc = afterCalc.ceil();
	}
	if ("2" == hasuuShoriKbn) {
		afterCalc = afterCalc.round();
	}
	if(minusflg) afterCalc = afterCalc.mul(new Decimal('-1'));
	return afterCalc;
}

//＜日付関連＞
/**
 * 年月を指定して月末日を求めるFunction
 * year 年
 * month 月
 */
function calcMonthEndDay(year, month) {
	//日付を0にすると前月の末日を指定したことになります
	//指定月の翌月の0日を取得して末日を求めます
	//そのため、ここでは month - 1 は行いません
	var dt = new Date(year, month, 0);
	return dt.getDate();
}

/**
 * 日付の妥当性チェック
 * year 年
 * month 月
 * day 日
 */
function checkDate(year, month, day) {
	var dt = new Date(year, month - 1, day);
	if(dt == null || dt.getFullYear() != year || dt.getMonth() + 1 != month || dt.getDate() != day) {
		return false;
	}
	return true;
}

/**
 * 使用終了日計算Function
 * kaishi    開始日(yyyy/mm/dd形式)
 * addMonths 加算月
 * return    終了日(yyyy/mm/dd形式)
 */
function shiyouShuuryouHenkou(kaishi, addMonths) {
	if(kaishi != "") {
		var kaishiBi = kaishi.split("/");
		var year = Number(kaishiBi[0]);
		var month = Number(kaishiBi[1]);
		var day = Number(kaishiBi[2]);
		if(checkDate(year, month, day)) {

			month += addMonths;
			// 月末日を求める
			var endDay = calcMonthEndDay(year, month);
			// 入力された日が月末日より大きかった場合、月末日を設定
			if(day > endDay) {day = endDay;}
			else {day = day -1;}
			var dt = new Date(year, month - 1, day);

			var getYear = String(dt.getFullYear());
			// 1～9月をMM形式にする
			if(dt.getMonth() + 1 <= 9) {
				getMonth = "0" + String(dt.getMonth() + 1);
			}
			else {
				getMonth = String(dt.getMonth() + 1);
			}
			// 1～9日をdd形式にする
			if(dt.getDate() <= 9) {
				getDate = "0" + String(dt.getDate());
			}
			else {
				getDate = String(dt.getDate());
			}

			return (getYear  + "/" + getMonth + "/" + getDate );
		}
	}
}

/**
 * 使用終了日からの期間逆算Function
 * kaishi    開始日(yyyy/mm/dd形式)
 * syuryo    終了日(yyyy/mm/dd形式)
 * return    1：1ヶ月  2：3ヶ月  3：6ヶ月
 */
function calcShiyouKikan(kaishi,syuryo) {

	if(kaishi != "" && syuryo != "") {
		var kaishiBi = kaishi.split("/");
		var syuryoBi = syuryo.split("/");
		var yearA = Number(kaishiBi[0]);
		var monthA = Number(kaishiBi[1]);
		var dayA = Number(kaishiBi[2]);
		var yearB = Number(syuryoBi[0]);
		var monthB = Number(syuryoBi[1]);
		var dayB = Number(syuryoBi[2]);


		var addMt = [1,3,6];	//1,3,6ヶ月
		if(checkDate(yearA, monthA, dayA) && checkDate(yearB, monthB, dayB)) {
			for(i = 0; i < addMt.length ; i++){
				var yearS = yearA;
				var monthS = monthA;
				var dayS = dayA;
				monthS += addMt[i];

				var endDay = calcMonthEndDay(yearS, monthS);
				if(dayS > endDay) {
					dayS = endDay;
				}else {
					dayS = dayS - 1;
				}
				var dtS = new Date(yearS, monthS - 1, dayS);
				var dtB = new Date(yearB, monthB - 1, dayB);
				if(dtS.getTime() >= dtB.getTime()){
					return i;
				}
			}
		}
	}
}

/**
 *
 */
function todayYMD(){
	var nnow = new Date();
	var nYear = nnow.getFullYear();
	var nMonth = ("00" + (nnow.getMonth()+1)).slice(-2);
	var nDate =  ("00" + nnow.getDate()).slice(-2);
	var date = nYear + "/" + nMonth + "/" + nDate;
	return date;
}

/**
 * 入力部品を空にする
 * 対象エリアのセレクタ
 */
function inputClear(target, hidden) {
	target.find("input[type=text], input[type=number], textarea").val("");
	target.find("input[type=tel], textarea").val("");
	target.find("input[type=radio]").removeAttr("checked");
	target.find("input[type=checkbox]").removeAttr("checked");
	target.find("input[type=radio]").attr("checked", false);
	target.find("select").find("option:first").prop("selected", true);
	if(hidden) target.find("input[type=hidden]").val("");
}

/**
 * select部品の値をコピーする
 * 対象エリアのセレクタ
 */
function selectValCopy(target, copy) {
	var objSelect = target.find("select");
	for( var i=0;i<objSelect.length;i++ ){
		var selectTarget = objSelect.eq(i);
		var name = selectTarget.prop("name");
		var selectCopy = copy.find("select[name="+ name + "]");
		var val = selectTarget.find("option:selected").val();
		selectCopy.val(val);
	}
}

/**
 * hiddenを空にする
 * 対象エリアのセレクタ
 */
function hiddenClear(target) {
	target.find("input[type=hidden]").val("");
}

/**
 * optionタグを隠す
 */
$.fn.optionHide = function(){
	//一部ブラウザ（IEなど）ではoptionのstyle=display:none;が効かない為。<span class="option-hide"><option></span>にしてspan非表示にする
	if(!$(this).parent().hasClass("option-hide")){
		$(this).wrap('<span class="option-hide">');
	}
	$(this).parent().hide();
};
/**
 * optionタグを表示する
 */
$.fn.optionShow = function(){
	if($(this).parent().hasClass("option-hide")){
		$(this).parent().show();
		$(this).unwrap();
	}
};


//＜共通ダイアログ＞
/** 
 * 消費税詳細ダイアログを表示
 * @param denpyouKbn 常に渡す
 */
function commonShouhizeiShousai(denpyouKbn) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "消費税詳細",
		buttons: {OK: function() {$(this).dialog("close");}}
	})
	.load("shouhizei_shousai?denpyouKbn=" + denpyouKbn);
}

/**
 * 仮払案件検索ボタン押下時Function
 * 'dialog'id内に仮払案件選択画面をロードする。
 *
 * ダイアログで仮払案件を選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 * ◆当function呼び出し前に、呼び出し元画面の選択結果反映先inputタグのクエリオブジェクトをセットする。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名						項目名
 * 		----------------------------------------------------------
 * 		dialogRetKaribaraiAnkenCd				仮払伝票ID
 * 		dialogRetKaribaraiAnkenName				仮払摘要
 * 		dialogRetKaribaraiAnkenNote				仮払金額
 * 		dialogRetKaribaraiAnkenShutchousaki		仮払出張先
 * 		dialogRetKaribaraiAnkenMokuteki			仮払目的
 * 		dialogRetKaribaraiAnkenKikanFrom		仮払精算期間開始日
 * 		dialogRetKaribaraiAnkenKikanTo			仮払精算期間終了日
 * 		dialogRetRingiHikitsugiUmFlg			稟議金額引継ぎ有無フラグ
 * 		dialogRetKianTenpuZumiFlg				起案添付済みフラグ
 *
 * ◆当function呼び出し前に、仮払案件選択時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackKaribaraiAnkenSentaku
 */
function commonKaribaraiAnkenSentaku(denpyouKbn, denpyouId, userId) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "仮払案件選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("karibarai_anken_sentaku?denpyouKbn=" + denpyouKbn  + "&oyaDenpyouId=" + encodeURI(denpyouId) + "&userId=" + encodeURI(userId));
}

/**
 * 取引先検索ボタン押下時Function
 * 'dialog'id内に取引先選択画面をロードする。
 *
 * ダイアログで取引先を選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * ◆当function呼び出し前に、呼び出し元画面の選択結果反映先inputタグのクエリオブジェクトをセットする。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名				項目名
 * 		----------------------------------------
 * 		dialogRetTorihikisakiCd			取引コード
 * 		dialogRetTorihikisakiName		取引名
 * 		dialogRetFurikomisaki			振込先
 *
 * ◆当function呼び出し前に、取引選択時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackTorihikisakiSentaku
 *
 * @param denpyouKbn 呼び出し元の伝票区分を特定したい時だけ渡す。
 */
function commonTorihikisakiSentaku(denpyouKbn) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "取引先選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("torihikisaki_sentaku?denpyouKbn=" + convTorihikisakiCd4Sentaku(denpyouKbn));
}

/**
 * 取引先コードロストフォーカス時Function
 * ※inputFurikomisakiは省略可能
 *
 * @param denpyouKbn 呼び出し元の伝票区分を特定したい時だけ渡す。
 */
function commonTorihikisakiCdLostFocus(inputCode, inputName, title, inputFurikomisaki, denpyouKbn, inputJigyoushaKbn) {
	var code = inputCode.val();
	if(code == "" || code == null) {
		inputName.val(null);
		if(inputFurikomisaki != null) inputFurikomisaki.val(null);
		return;
	}
	$.ajax({
		type : "GET",
		url : "torihikisaki_name_get",
		data : "torihikisakiCd=" + code + "&denpyouKbn=" + convTorihikisakiCd4Sentaku(denpyouKbn),
		dataType : 'text',
		success : function(response) {
			var dataAry = response.split("\t");
			var name = dataAry[0];
			var furikomisaki = dataAry[1];
			var jigyoushaKbn = dataAry[2];

			inputName.val(name);
			if(inputFurikomisaki != null) inputFurikomisaki.val(furikomisaki);
			if(inputJigyoushaKbn !== undefined && inputJigyoushaKbn != null){
				inputJigyoushaKbn.val([jigyoushaKbn]);
			}

			if (name == "" && title != "") {
				alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
			}

			if ("dialogCallbackTorihikisakiSentaku" in window)	{
				dialogCallbackTorihikisakiSentaku();
			}
		}
	});
}

/**
 * 取引先選択/AJAX呼び出し時の取引先コード変換
 * サーバーにわたす伝票区分(仕入先フラグ特定用)
 * ①function引数のdenpyouKbnがnull,undefined等でなければそれを採用。function引数にブランク渡したらブランクを採用ってことにする。ちょっとややこしいけど。
 * ②現在画面のhidden(denpyouKbn)がいれば、そのvalueを採用。
 * ③①②に該当しなければブランク。
 * @param torihikisakiCd 取引先コード
 * @return 取引先コード
 */
function convTorihikisakiCd4Sentaku(denpyouKbn){
	if(denpyouKbn == null){
		if($("input[type=hidden][name=denpyouKbn][value!='']").length > 0){
			denpyouKbn = $("input[type=hidden][name=denpyouKbn][value!='']")[0].value;
		}else{
			denpyouKbn = ""
		}
	}
	return denpyouKbn;
}

/**
 * 取引選択ボタン押下時Function
 * 'dialog'id内に取引選択画面をロードする。
 *
 * ダイアログで取引を選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * ◆当function呼び出し前に、呼び出し元画面の選択結果反映先inputタグのクエリオブジェクトをセットする。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名				項目名
 * 		----------------------------------------
 * 		dialogRetTorihikiName				取引名
 * 		dialogRetShiwakeEdaNo				仕訳枝番号
 * 		dialogRetKamokuCd					勘定科目コード
 * 		dialogRetKamokuName					勘定科目名
 * 		dialogRetKamokuEdabanCd				勘定科目枝番コード
 * 		dialogRetKamokuEdabanName			勘定科目枝番名
 * 		dialogRetKamokuEdabanSentakuButton	勘定科目枝番選択ボタン
 * 		dialogRetFutanBumonCd				負担部門コード
 * 		dialogRetFutanBumonName				負担部門名
 * 		dialogRetFutanBumonSentakuButton	負担部門選択ボタン
 * 		dialogRetProjectCd					プロジェクトコード
 * 		dialogRetProjectName				プロジェクト名
 * 		dialogRetProjectSentakuButton		プロジェクト選択ボタン
 * 		dialogRetSegmentCd					セグメントコード
 * 		dialogRetSegmentName				セグメント名
 * 		dialogRetSegmentSentakuButton		セグメント選択ボタン
 * 		dialogRetKazeiKbn					課税区分
 * 		dialogRetTekiyou					摘要
 * 		dialogRetKousaihiShousaiHyoujiFlg	交際費詳細表示フラグ
 * 		dialogRetKousaihiShousai			交際費詳細
 * 		dialogRetKousaihiArea				交際費エリア(div等)
 * 		dialogRetNinzuuArea					人数項目エリア
 * 		dialogRetShainCd					社員コード連携箇所
 * 		dialogRetSahinName					社員名連携箇所
 *
 * ◆当function呼び出し前に、取引選択時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackTorihikiSentaku
 *
 * @param denpyouKbn			伝票区分
 * @param bumonCd				部門コード
 * @param denpyouId				伝票ID
 * @param daihyouFutanBumonCd	代表負担部門コード
 * @param userId				ユーザーID
 */
function commonTorihikiSentaku(denpyouKbn, bumonCd, denpyouId, daihyouFutanBumonCd, userId) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "取引選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	if(userId!="") {
		$("#dialog").load("torihiki_sentaku?denpyouKbn=" + denpyouKbn + "&bumonCd=" + bumonCd + "&denpyouId=" + denpyouId + "&daihyouFutanBumonCd=" + daihyouFutanBumonCd + "&userId=" + userId);
	}
	else {
		$("#dialog").load("torihiki_sentaku?denpyouKbn=" + denpyouKbn + "&bumonCd=" + bumonCd + "&denpyouId=" + denpyouId + "&daihyouFutanBumonCd=" + daihyouFutanBumonCd);
	}
}

/**
 * 仕訳枝番号ロストフォーカス時Function
 */
function commonShiwakeEdaNoLostFocus(inputCode, inputName, title, denpyouKbn, bumonCd, denpyouId, daihyouFutanBumonCd, userId,invoiceDenpyou) {
	if (!userId) userId = "";
	var code = inputCode.val();
	$.ajax({
		type : "GET",
		url : "torihiki_name_get",
		data: { 'denpyouKbn': denpyouKbn, 'bumonCd': bumonCd, 'denpyouId': denpyouId, 'daihyouFutanBumonCd': daihyouFutanBumonCd, 'userId': userId, 'shiwakeEdaNo': code  },
		dataType : 'text',
		success : function(response) {
			var dataAry = response.split("\t");
			var torihikiName = dataAry[0];
			if(code != "") {
				if (torihikiName == "" && title != "") {
					alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
				}
			}
			var m = 1;
			var data = {
				torihikiName: torihikiName,
				shiwakeEdaNo: dataAry[m++],
				kamokuCd: dataAry[m++],
				kamokuName: dataAry[m++],
				kamokuEdabanCd: dataAry[m++],
				kamokuEdabanName: dataAry[m++],
				futanBumonCd: dataAry[m++],
				futanBumonName: dataAry[m++],
				daihyouFutanBumonFlg: dataAry[m++],
				torihikisakiCd: dataAry[m++],
				torihikisakiName: dataAry[m++],
				projectCd: dataAry[m++],
				projectName: dataAry[m++],
				segmentCd: dataAry[m++],
				segmentName: dataAry[m++],
				furikomisaki: dataAry[m++],
				kazeiKbn: dataAry[m++],
				kousaihiShousaiHyoujiFlg: dataAry[m++],
				ninzuuRiyouFlg: dataAry[m++],
				kakeFlg: dataAry[m++],
				shainCdRenkeiFlg: dataAry[m++],
				shainCd: dataAry[m++],
				tekiyouFlg: dataAry[m++],
				tekiyou: dataAry[m++],
				shoriGroup: dataAry[m++],
				kazeiFlg: dataAry[m++],
				kazeiFlgKamoku:dataAry[m++],
				uf1Cd: dataAry[m++],
				uf1Name: dataAry[m++],
				uf2Cd: dataAry[m++],
				uf2Name: dataAry[m++],
				uf3Cd: dataAry[m++],
				uf3Name: dataAry[m++],
				uf4Cd: dataAry[m++],
				uf4Name: dataAry[m++],
				uf5Cd: dataAry[m++],
				uf5Name: dataAry[m++],
				uf6Cd: dataAry[m++],
				uf6Name: dataAry[m++],
				uf7Cd: dataAry[m++],
				uf7Name: dataAry[m++],
				uf8Cd: dataAry[m++],
				uf8Name: dataAry[m++],
				uf9Cd: dataAry[m++],
				uf9Name: dataAry[m++],
				uf10Cd: dataAry[m++],
				uf10Name: dataAry[m++],
				zeiritsu: dataAry[m++],
				keigenZeiritsuKbn: dataAry[m++],
				bunriKbn: dataAry[m++],
				shiireKbn: dataAry[m++],
				oldKazeiKbn: dataAry[m++],
				oldKazeiFlg: dataAry[m++],
			};

			commonSetTorihiki(data,invoiceDenpyou);
		}
	});
}

/**
 * 取引設定
 * ◆当function呼び出し前に、取引選択時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackCommonSetTorihiki
 */
function commonSetTorihiki(torihiki,invoiceDenpyou) {

	var torihikiName = torihiki["torihikiName"];
	var shiwakeEdaNo = torihiki["shiwakeEdaNo"];
	var kamokuCd = torihiki["kamokuCd"];
	var kamokuName = torihiki["kamokuName"];
	var kamokuEdabanCd = torihiki["kamokuEdabanCd"];
	var kamokuEdabanName = torihiki["kamokuEdabanName"];
	var futanBumonCd = torihiki["futanBumonCd"];
	var futanBumonName = torihiki["futanBumonName"];
	var daihyouFutanBumonFlg = torihiki["daihyouFutanBumonFlg"];
	var torihikisakiCd = torihiki["torihikisakiCd"];
	var torihikisakiName = torihiki["torihikisakiName"];
	var projectCd = torihiki["projectCd"];
	var projectName = torihiki["projectName"];
	var segmentCd = torihiki["segmentCd"];
	var segmentName = torihiki["segmentName"];
	var furikomisaki = torihiki["furikomisaki"];
	var kazeiKbn = torihiki["kazeiKbn"];
	var kousaihiShousaiHyoujiFlg = torihiki["kousaihiShousaiHyoujiFlg"];
	var ninzuuRiyouFlg = torihiki["ninzuuRiyouFlg"];
	var kousaihiKijungaku = torihiki["kousaihiKijungaku"];
	var kousaihiCheckHouhou = torihiki["kousaihiCheckHouhou"];
	var kousaihiCheckResult = torihiki["kousaihiCheckResult"];
	var kakeFlg = torihiki["kakeFlg"];
	var shainCdRenkeiFlg = torihiki["shainCdRenkeiFlg"];
	var shainCd = torihiki["shainCd"];
	var tekiyouFlg = torihiki["tekiyouFlg"];
	var tekiyou = torihiki["tekiyou"];
	var shoriGroup = torihiki["shoriGroup"];
	var kazeiFlg = torihiki["kazeiFlg"];
	var kazeiFlgKamoku = torihiki["kazeiFlgKamoku"];
	var uf1Cd = torihiki["uf1Cd"];
	var uf1Name = torihiki["uf1Name"];
	var uf2Cd = torihiki["uf2Cd"];
	var uf2Name = torihiki["uf2Name"];
	var uf3Cd = torihiki["uf3Cd"];
	var uf3Name = torihiki["uf3Name"];
	var uf4Cd = torihiki["uf4Cd"];
	var uf4Name = torihiki["uf4Name"];
	var uf5Cd = torihiki["uf5Cd"];
	var uf5Name = torihiki["uf5Name"];
	var uf6Cd = torihiki["uf6Cd"];
	var uf6Name = torihiki["uf6Name"];
	var uf7Cd = torihiki["uf7Cd"];
	var uf7Name = torihiki["uf7Name"];
	var uf8Cd = torihiki["uf8Cd"];
	var uf8Name = torihiki["uf8Name"];
	var uf9Cd = torihiki["uf9Cd"];
	var uf9Name = torihiki["uf9Name"];
	var uf10Cd = torihiki["uf10Cd"];
	var uf10Name = torihiki["uf10Name"];
	var zeiritsu = torihiki["zeiritsu"];
	var keigenZeiritsuKbn = torihiki["keigenZeiritsuKbn"];
	var bunriKbn = torihiki["bunriKbn"];
	var shiireKbn = torihiki["shiireKbn"];
	var oldKazeiKbn = torihiki["oldKazeiKbn"];
	var oldKazeiFlg = torihiki["oldKazeiFlg"];

	//取引
	if ("dialogRetTorihikiName"				in window)	dialogRetTorihikiName.val(torihikiName);
	if ("dialogRetShiwakeEdaNo"				in window)	dialogRetShiwakeEdaNo.val(shiwakeEdaNo);

	//科目
	if ("dialogRetKamokuCd"					in window)	dialogRetKamokuCd.val(kamokuCd);
	if ("dialogRetKamokuName"				in window)	dialogRetKamokuName.val(kamokuName);

	//科目枝番
	if ("dialogRetKamokuEdabanCd"			in window) {
		if ("<EDABAN>" == kamokuEdabanCd) {
			dialogRetKamokuEdabanCd.val("");
			dialogRetKamokuEdabanCd.removeAttr("disabled");
			dialogRetKamokuEdabanName.val("");
			dialogRetKamokuEdabanSentakuButton.removeAttr("disabled");
		} else {//ブランク or 特定のコード値
			dialogRetKamokuEdabanCd.val(kamokuEdabanCd);
			dialogRetKamokuEdabanCd.attr("disabled", "disabled");
			dialogRetKamokuEdabanName.val(kamokuEdabanName);
			dialogRetKamokuEdabanSentakuButton.attr("disabled", "disabled");
		}
	}

	//負担部門
	if ("dialogRetFutanBumonCd"				in window) {
		if ("<FUTAN>" == futanBumonCd || "<SYOKIDAIHYOU>" == daihyouFutanBumonFlg) {
			dialogRetFutanBumonCd.removeAttr("disabled");
			dialogRetFutanBumonSentakuButton.removeAttr("disabled");
			//任意の場合は内容クリア
			if("<FUTAN>" == futanBumonCd){
				dialogRetFutanBumonCd.val("");
				dialogRetFutanBumonName.val("");
			}
		} else {//ブランク or 特定のコード値
			dialogRetFutanBumonCd.attr("disabled", "disabled");
			dialogRetFutanBumonSentakuButton.attr("disabled", "disabled");
		}

		if("<FUTAN>" != futanBumonCd)
		{
			dialogRetFutanBumonCd.val(futanBumonCd);
			dialogRetFutanBumonName.val(futanBumonName);
		}
	}

	//取引先
	//選択できるが仕訳に反映されないことがある
	if ("dialogRetTorihikisakiCd" in window && $("input[name=denpyouKbn]").val() != "A013") {
		if ("<TORIHIKI>" == torihikisakiCd) {
			dialogRetTorihikisakiCd.removeAttr("disabled");
			dialogRetTorihikisakiSentakuButton.removeAttr("disabled");
			dialogRetTorihikisakiClearButton.removeAttr("disabled");
			if ("dialogRetFurikomisaki" in window) dialogRetFurikomisaki.removeAttr("disabled");
		} else {//ブランク or 特定のコード値
			dialogRetTorihikisakiCd.val(torihikisakiCd);
			dialogRetTorihikisakiCd.attr("disabled", "disabled");
			dialogRetTorihikisakiName.val(torihikisakiName);
			dialogRetTorihikisakiSentakuButton.attr("disabled", "disabled");
			dialogRetTorihikisakiClearButton.attr("disabled", "disabled");
			if ("dialogRetFurikomisaki" in window) dialogRetFurikomisaki.val(furikomisaki);
			if ("dialogRetFurikomisaki" in window) dialogRetFurikomisaki.attr("disabled", "disabled");
		}
	}

	//プロジェクト
	if ("dialogRetProjectCd"				in window) {
		if ("<PROJECT>" == projectCd) {
			dialogRetProjectCd.removeAttr("disabled");
			dialogRetProjectSentakuButton.removeAttr("disabled");
		} else  {
			//ブランク or 特定のコード値
			dialogRetProjectCd.val(projectCd);
			dialogRetProjectCd.attr("disabled", "disabled");
			dialogRetProjectName.val(projectName);
			dialogRetProjectSentakuButton.attr("disabled", "disabled");
		}
	}

	//セグメント
	if ("dialogRetSegmentCd"				in window) {
		if ("<SG>" == segmentCd) {
			dialogRetSegmentCd.removeAttr("disabled");
			dialogRetSegmentSentakuButton.removeAttr("disabled");
		} else  {
			//ブランク or 特定のコード値
			dialogRetSegmentCd.val(segmentCd);
			dialogRetSegmentCd.attr("disabled", "disabled");
			dialogRetSegmentName.val(segmentName);
			dialogRetSegmentSentakuButton.attr("disabled", "disabled");
		}
	}

	//社員コード
	if ("dialogRetShainCd"				in window) {
		if ("1" == shainCdRenkeiFlg) {
			// 社員コード連携
			dialogRetShainCd.val(shainCd);
			dialogRetSahinName.val("");
		} else {
			dialogRetShainCd.val("");
			dialogRetSahinName.val("");
		}
	}

	//課税区分
	if ("dialogRetKazeiKbn"					in window && dialogRetKazeiKbn !== undefined) dialogRetKazeiKbn.val((invoiceDenpyou !== undefined && invoiceDenpyou == "1") ? oldKazeiKbn : kazeiKbn);

	//摘要
	if ("dialogRetTekiyou"					in window) {
		if ("1" == tekiyouFlg) {
			dialogRetTekiyou.val(tekiyou);
		} else {
			dialogRetTekiyou.val(torihikiName);
		}
	}

	//交際費
	if ("dialogRetKousaihiShousaiHyoujiFlg"	in window)	dialogRetKousaihiShousaiHyoujiFlg.val(kousaihiShousaiHyoujiFlg);
	if ("dialogRetKousaihiShousai"			in window) {
		if ("1" == kousaihiShousaiHyoujiFlg) {
			dialogRetKousaihiArea.css("display", "block");
		} else {
			dialogRetKousaihiShousai.val("");
			//人数・一人あたり金額も消す
			if ("dialogRetKousaihiNinzuu"			in window) {dialogRetKousaihiNinzuu.val("");}
			if ("dialogRetKousaihiHitoriKingaku"	in window) {dialogRetKousaihiHitoriKingaku.val("");}
			dialogRetKousaihiArea.css("display", "none");
		}
		if ("1" == ninzuuRiyouFlg){
			dialogRetNinzuuArea.css("display", "block");
		} else{
			if ("dialogRetKousaihiNinzuu"			in window) {dialogRetKousaihiNinzuu.val("");}
			if ("dialogRetKousaihiHitoriKingaku"	in window) {dialogRetKousaihiHitoriKingaku.val("");}
			dialogRetNinzuuArea.css("display", "none");
		}
	}

	//掛け
	if ("dialogRetKakeFlg"					in window) {
		dialogRetKakeFlg.val(kakeFlg);
	}

	//処理グループ
	if ("dialogRetShoriGroup"				in window) {
		dialogRetShoriGroup?.val(shoriGroup);
	}

	//課税フラグ
	if ("dialogRetKazeiFlg"				in window) {
		dialogRetKazeiFlg.val((invoiceDenpyou !== undefined && invoiceDenpyou == "1") ? oldKazeiFlg : kazeiFlg);
	}

	//課税フラグ（科目）
	if ("dialogRetKazeiFlgKamoku"				in window) {
		dialogRetKazeiFlgKamoku.val(kazeiFlgKamoku);
	}



	//ユニバーサルフィールド１
	if("dialogRetUf1Cd" 				in window ) {
		if ("<UF>" == uf1Cd) {
			dialogRetUf1Cd.removeAttr("disabled");
			dialogRetUf1SentakuButton.removeAttr("disabled");
		} else {//ブランク or 特定のコード値
			dialogRetUf1Cd.val(uf1Cd);
			dialogRetUf1Cd.attr("disabled", "disabled");
			dialogRetUf1Name.val(uf1Name);
			dialogRetUf1SentakuButton.attr("disabled", "disabled");
		}
	}

	//ユニバーサルフィールド２
	if("dialogRetUf2Cd" 				in window ) {
		if ("<UF>" == uf2Cd) {
			dialogRetUf2Cd.removeAttr("disabled");
			dialogRetUf2SentakuButton.removeAttr("disabled");
		} else {//ブランク or 特定のコード値
			dialogRetUf2Cd.val(uf2Cd);
			dialogRetUf2Cd.attr("disabled", "disabled");
			dialogRetUf2Name.val(uf2Name);
			dialogRetUf2SentakuButton.attr("disabled", "disabled");
		}
	}

	//ユニバーサルフィールド３
	if("dialogRetUf3Cd" 				in window ) {
		if ("<UF>" == uf3Cd) {
			dialogRetUf3Cd.removeAttr("disabled");
			dialogRetUf3SentakuButton.removeAttr("disabled");
		} else {//ブランク or 特定のコード値
			dialogRetUf3Cd.val(uf3Cd);
			dialogRetUf3Cd.attr("disabled", "disabled");
			dialogRetUf3Name.val(uf3Name);
			dialogRetUf3SentakuButton.attr("disabled", "disabled");
		}
	}

	//ユニバーサルフィールド４
	if("dialogRetUf4Cd" 				in window ) {
		if ("<UF>" == uf4Cd) {
			dialogRetUf4Cd.removeAttr("disabled");
			dialogRetUf4SentakuButton.removeAttr("disabled");
		} else {//ブランク or 特定のコード値
			dialogRetUf4Cd.val(uf4Cd);
			dialogRetUf4Cd.attr("disabled", "disabled");
			dialogRetUf4Name.val(uf4Name);
			dialogRetUf4SentakuButton.attr("disabled", "disabled");
		}
	}

	//ユニバーサルフィールド５
	if("dialogRetUf5Cd" 				in window ) {
		if ("<UF>" == uf5Cd) {
			dialogRetUf5Cd.removeAttr("disabled");
			dialogRetUf5SentakuButton.removeAttr("disabled");
		} else {//ブランク or 特定のコード値
			dialogRetUf5Cd.val(uf5Cd);
			dialogRetUf5Cd.attr("disabled", "disabled");
			dialogRetUf5Name.val(uf5Name);
			dialogRetUf5SentakuButton.attr("disabled", "disabled");
		}
	}

	//ユニバーサルフィールド６
	if("dialogRetUf6Cd" 				in window ) {
		if ("<UF>" == uf6Cd) {
			dialogRetUf6Cd.removeAttr("disabled");
			dialogRetUf6SentakuButton.removeAttr("disabled");
		} else {//ブランク or 特定のコード値
			dialogRetUf6Cd.val(uf6Cd);
			dialogRetUf6Cd.attr("disabled", "disabled");
			dialogRetUf6Name.val(uf6Name);
			dialogRetUf6SentakuButton.attr("disabled", "disabled");
		}
	}

	//ユニバーサルフィールド７
	if("dialogRetUf7Cd" 				in window ) {
		if ("<UF>" == uf7Cd) {
			dialogRetUf7Cd.removeAttr("disabled");
			dialogRetUf7SentakuButton.removeAttr("disabled");
		} else {//ブランク or 特定のコード値
			dialogRetUf7Cd.val(uf7Cd);
			dialogRetUf7Cd.attr("disabled", "disabled");
			dialogRetUf7Name.val(uf7Name);
			dialogRetUf7SentakuButton.attr("disabled", "disabled");
		}
	}

	//ユニバーサルフィールド８
	if("dialogRetUf8Cd" 				in window ) {
		if ("<UF>" == uf8Cd) {
			dialogRetUf8Cd.removeAttr("disabled");
			dialogRetUf8SentakuButton.removeAttr("disabled");
		} else {//ブランク or 特定のコード値
			dialogRetUf8Cd.val(uf8Cd);
			dialogRetUf8Cd.attr("disabled", "disabled");
			dialogRetUf8Name.val(uf8Name);
			dialogRetUf8SentakuButton.attr("disabled", "disabled");
		}
	}

	//ユニバーサルフィールド９
	if("dialogRetUf9Cd" 				in window ) {
		if ("<UF>" == uf9Cd) {
			dialogRetUf9Cd.removeAttr("disabled");
			dialogRetUf9SentakuButton.removeAttr("disabled");
		} else {//ブランク or 特定のコード値
			dialogRetUf9Cd.val(uf9Cd);
			dialogRetUf9Cd.attr("disabled", "disabled");
			dialogRetUf9Name.val(uf9Name);
			dialogRetUf9SentakuButton.attr("disabled", "disabled");
		}
	}

	//ユニバーサルフィールド１０
	if("dialogRetUf10Cd" 				in window ) {
		if ("<UF>" == uf10Cd) {
			dialogRetUf10Cd.removeAttr("disabled");
			dialogRetUf10SentakuButton.removeAttr("disabled");
		} else {//ブランク or 特定のコード値
			dialogRetUf10Cd.val(uf10Cd);
			dialogRetUf10Cd.attr("disabled", "disabled");
			dialogRetUf10Name.val(uf10Name);
			dialogRetUf10SentakuButton.attr("disabled", "disabled");
		}
	}

	//消費税率
	if ("dialogRetZeiritsu"				in window && dialogRetZeiritsu !== undefined) {
		if ("<ZEIRITSU>" == zeiritsu) {
			dialogRetZeiritsu.removeAttr("disabled");
		}else if(zeiritsu){
			dialogRetZeiritsu.attr("disabled", "disabled");
			dialogRetZeiritsu.val(zeiritsu);
			if ("dialogRetKeigenZeiritsuKbn"	in window) dialogRetKeigenZeiritsuKbn.val(keigenZeiritsuKbn);
		}
	}
	
	//分離区分 インボイス対応で追加　取引周り未対応
	if ("dialogRetBunriKbn"				in window && dialogRetBunriKbn !== undefined){
		dialogRetBunriKbn.val(bunriKbn);
	}
	//仕入区分
	if ("dialogRetKariShiireKbn"			in window && dialogRetKariShiireKbn !== undefined){
		dialogRetKariShiireKbn.val(shiireKbn == "" ? "0" : shiireKbn);
	}
	//後処理
	if ("dialogCallbackCommonSetTorihiki" in window)	dialogCallbackCommonSetTorihiki();
}

/**
 * 勘定科目選択ボタン押下時Function
 * 'dialog'id内に勘定科目選択画面をロードする。
 *
 * ダイアログで取引を選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * ◆当function呼び出し前に、呼び出し元画面の選択結果反映先inputタグのクエリオブジェクトをセットする。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名		:項目名
 * 		----------------------------------------
 * 		dialogRetKamokuCd		:科目コード
 * 		dialogRetKamokuName		:科目名

 * ◆当function呼び出し前に、取引選択時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackKanjyouKamokuSentaku
 */
function commonKamokuSentaku(isGenyokin, denpyouId) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	var onlyGenyokin = isGenyokin;
	if( isGenyokin == "" || isGenyokin == null) onlyGenyokin = false;
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "勘定科目選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("kanjyou_kamoku_sentaku?onlyGenyokin=" + onlyGenyokin + "&denpyouId=" + denpyouId);
}

/**
 * 勘定科目コードロストフォーカス時Function
 */
function commonKamokuCdLostFocus(inputCode, inputName, title, isGenyokin, denpyouId, inputKazeiKbn, inputBunriKbn, inputShiireKbn, inputShoriGroup, zeiritsu) {
	var code = inputCode.val();
	if(code == "" || code == null) {
		inputName.val(null);
		if ("dialogCallbackKanjyouKamokuSentaku" in window)	{
			dialogCallbackKanjyouKamokuSentaku();
		}
		return;
	}
	var onlyGenyokin = isGenyokin;
	if( isGenyokin == "" || isGenyokin == null) onlyGenyokin = false;
	$.ajax({
		type : "GET",
		url : "kanjyou_kamoku_name_get",
		data : "kamokuCd=" + code + "&onlyGenyokin=" + onlyGenyokin + "&denpyouId=" + denpyouId,
		dataType : 'json',
		success : function(response) {
			var val = inputName.val();
			inputName.val(response.kamokuNameRyakushiki);
			if(response.kazeiKbn != null && response.kazeiKbn.toString() !=""&& inputKazeiKbn !== undefined && inputKazeiKbn != null){ // 0==""はtrue、"0"==""はfalse
				if([18,19,49].includes(response.kazeiKbn)){
					if(inputKazeiKbn.selector.indexOf("kari") >= 0 || inputKazeiKbn.selector.indexOf("kashi") >= 0){
						if(inputKazeiKbn.selector.indexOf("kari") >= 0){
							// 借方の場合
							if(response.kazeiKbn == 18){
								inputKazeiKbn.val('011'); // 課込自動　→　課込仕入
							}else if(response.kazeiKbn == 19){
								inputKazeiKbn.val('013'); // 課抜自動　→　課抜仕入
							}else{
								inputKazeiKbn.val('041'); // 非課自動　→　非課仕入
							}
						}else{
							// 貸方で課税区分制御を使用するのは振替伝票、総合付け替え伝票のみ
							if(response.kazeiKbn == 18){
								inputKazeiKbn.val('012'); // 課込自動　→　課込売上
							}else if(response.kazeiKbn == 19){
								inputKazeiKbn.val('014'); // 課抜自動　→　課抜売上
							}else{
								inputKazeiKbn.val('042'); // 非課自動　→　非課売上
							}
						}
					}else if(inputKazeiKbn.selector.indexOf("saki") >= 0 || inputKazeiKbn.selector.indexOf("moto") >= 0){
						inputKazeiKbn.val(''); 
					}
				}else{
					 inputKazeiKbn.val(response.kazeiKbn.toString().padStart(3, '0')); // 選択肢側が3桁、枝番側はsmallintで桁づめがないのでフォーマットを統一
				}
			}
			if(response.bunriKbn != null && response.bunriKbn.toString() != "" && inputBunriKbn !== undefined && inputBunriKbn != null){
				inputBunriKbn.val(response.bunriKbn == 3 ? 0 : response.bunriKbn);
			}
			if(response.shiireKbn != null && response.shiireKbn.toString() != "" && inputShiireKbn !== undefined && inputShiireKbn != null){
				//科目マスター上で、仕入区分「共売」「設定不可」の両方が「0」となっており、取引仕訳で共売のデフォルト値が表示されないため一旦ここで変換
				if(response.shiireKbn == "0"){
				    if ( ['2', '5', '6', '7', '8', '10'].includes(response.shoriGroup.toString()) && ['1', '2', '11', '13'].includes(response.kazeiKbn.toString())) {
				        inputShiireKbn.val('3');
				    }else {
				        inputShiireKbn.val('0');
				    }
				}else{
					inputShiireKbn.val(response.shiireKbn);
				}
			}
			if(response.shoriGroup != null && response.shoriGroup.toString() != "" && inputShoriGroup !== undefined && inputShoriGroup != null){
				inputShoriGroup.val(response.shoriGroup);
			}
			
			// 財務の科目税率。参考情報で、取引追加・変更（spanクラスなのでtextでセットする）以外では使用箇所なし
			if(response.zeiritsuKbn != null && response.zeiritsuKbn.toString() != "" && zeiritsu !== undefined && zeiritsu != null){
				zeiritsu.text(getZaimuZeiKbnText(response.zeiritsuKbn));
			}
			if (response.kamokuNameRyakushiki == "" && title != "") {
				alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
			}
			if ("dialogCallbackKanjyouKamokuSentaku" in window)	{
				dialogCallbackKanjyouKamokuSentaku();
			}
		}
	});
}

/** 財務税区分文字列に変換 */
function getZaimuZeiKbnText(zeiKbn)
{
    switch (zeiKbn)
    {
        case 1:
            return "3.0%";
        case 2:
            return "5.0%";
        case 3:
            return "8.0%";
        case 4:
            return "*8.0%";
        case 5:
            return "10.0%";
        default:
            return "";
    }
}

/**
 * 処理グループ取得function
 */
function getKamokuShoriGroup(kamokuCode)
{
    return new Promise(function (resolve, reject) {
        if (kamokuCode == "" || kamokuCode == null) {
            resolve(null);
        } else {
            $.ajax({
                type: "GET",
                url: "kanjyou_kamoku_shori_group_get",
                data: "kamokuCd=" + kamokuCode + "&onlyGenyokin=" + false,
                dataType: 'text',
                success: function (response) {
                    resolve(parseInt(response, 10));
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    reject(errorThrown);
                }
            });
        }
    });
}

/**
 * 勘定科目枝番検索ボタン押下時Function
 * 'dialog'id内に勘定科目枝番選択画面をロードする。
 *
 * ダイアログで勘定科目枝番を選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * ◆当function呼び出し前に、呼び出し元画面の選択結果反映先inputタグのクエリオブジェクトをセットする。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名			項目名
 * 		----------------------------------------
 * 		dialogRetKamokuEdabanCd		科目コード
 * 		dialogRetKamokuEdabanName	科目名
 *		dialogRetKazeiKbn			課税区分select
 *		dialogRetBunriKbn			分離区分select

 * ◆当function呼び出し前に、取引選択時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackKanjyouKamokuEdabanSentaku
 *
 * @param kamokuCd 勘定科目コード
 * @param denpyouKbn 伝票ID
 * @param shiwakeEdano 仕訳枝番
 */
function commonKamokuEdabanSentaku(kamokuCd, denpyouKbn, shiwakeEdano) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "勘定科目枝番選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("kanjyou_kamoku_edaban_sentaku?kamokuCd=" + kamokuCd + "&denpyouKbn=" + denpyouKbn + "&shiwakeEdano=" + shiwakeEdano);
}


/**
 * 勘定科目枝番コードロストフォーカス時Function
 */
function commonKamokuEdabanCdLostFocus(inputCode, inputName, kamokuCd, title, inputKazeiKbn, inputBunriKbn, denpyouKbn, shiwakeEdano) {
	var code = inputCode.val();
	if(code == "" || code == null) {
		inputName.val(null);
		return;
	}
	$.ajax({
		type : "GET",
		url : "kanjyou_kamoku_edaban_name_get",
		data: { 'kamokuEdabanCd': code, 'kamokuCd': kamokuCd, 'shiwakeEdano': shiwakeEdano, 'denpyouKbn': denpyouKbn },
		dataType : 'json',
		success : function(response) {
			inputName.val(response.edabanName);
			if(response.kazeiKbn != null && response.kazeiKbn.toString() !=""&& inputKazeiKbn !== undefined && inputKazeiKbn != null){ // 0==""はtrue、"0"==""はfalse
				let validKazeiKbnList = $(inputKazeiKbn).find('option').map(function() {
					return this.value;
				}).get();
				// 自動系と財務未対応課税区分の処理
				if(!validKazeiKbnList.includes(response.kazeiKbn)){
					let selector = inputKazeiKbn.selector;
					let isKari = selector.indexOf("kashi") < 0; // 通常明細では貸借は明記されないが借方
					let kazeiKbn = (selector.indexOf("saki") >= 0 || selector.indexOf("moto"))
						// 総合付替は空でおしまい
						? ""
						// 自動系
						: response.kazeiKbn == 18
							? (isKari ? "011" : "012")
							: response.kazeiKbn == 19
								? (isKari ? "013" : "014")
								: resoponse.kazeiKbn == 49
									? (isKari ? "041" : "042")
									// WF未対応系：分岐はさせるが、実際にはAction側・上流で取引のものを採用し、変更させないことでふさぐ
									: "";
					inputKazeiKbn.val(kazeiKbn);
				}else{
					 inputKazeiKbn.val(response.kazeiKbn);
				}
			}
			if(response.bunriKbn != null && response.bunriKbn.toString() != "" && inputBunriKbn !== undefined && inputBunriKbn != null){
				inputBunriKbn.val(response.bunriKbn == 3 ? 0 : response.bunriKbn);
			}
			
			if ((response == null || response.edabanName == null || response.edabanName == "") && title != "") {
				alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
			}
			if ("dialogCallbackKanjyouKamokuEdabanSentaku" in window)	{
				dialogCallbackKanjyouKamokuEdabanSentaku();
			}
		}
	});
}

/**
 * 負担部門検索ボタン押下時Function
 * 'dialog'id内に負担部門選択画面をロードする。
 *
 * ダイアログで負担部門を選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * ◆当function呼び出し前に、呼び出し元画面の選択結果反映先inputタグのクエリオブジェクトをセットする。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名		項目名
 * 		----------------------------------------
 * 		dialogRetFutanBumonCd	負担部門コード
 * 		dialogRetFutanBumonName	負担部門名
 * 		dialogRetKariShiireKbn	仕入区分select

 * ◆当function呼び出し前に、取引選択時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackFutanBumonSentaku
 *
 * @param mode        表示モード(1:伝票編集・検索 2:全表示)
 * @param kamokuCd    勘定科目コード
 * @param denpyouId   伝票ID
 * @param bumonCd     起票部門コード
 * @param denpyouKbn   伝票区分
 */
function commonFutanBumonSentaku(mode, kamokuCd, denpyouId, bumonCd, denpyouKbn, shiwakeEdano) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "負担部門選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	});

	$("#dialog").load("futan_bumon_sentaku",	{
		mode				: mode,
		kamokuCd			: kamokuCd,
		denpyouId			: denpyouId,
		kihyouBumonCd		: bumonCd,
		shiwakeEdano: shiwakeEdano,
		denpyouKbn: denpyouKbn,
	});
	return;

}


/**
 * 負担部門コードロストフォーカス時Function
 */
function commonFutanBumonCdLostFocus(mode, inputCode, inputName, title, denpyouId, kihyouBumonCd, kamokuCd, inputShiireKbn, denpyouKbn, shiwakeEdano) {
	var code = inputCode.val();
	if(code == "" || code == null) {
		inputName.val(null);
		return;
	}
	$.ajax({
		type : "GET",
		url : "futan_bumon_name_get",
		data : {
			futanBumonCd: code,
			mode: mode,
			denpyouId: denpyouId,
			kihyouBumonCd : kihyouBumonCd,
			kamokuCd : kamokuCd,
			shiwakeEdano: shiwakeEdano,
			denpyouKbn: denpyouKbn,
			},
		dataType : 'json',
		success : function(response) {
			inputName.val(response.futanBumonName);
			var shiwakeEnable = (inputShiireKbn !== undefined && inputShiireKbn != null) ? inputShiireKbn.val() == "0": true;
			if(response.shiireKbn != null && response.shiireKbn.toString() != "" && response.shiireKbn.toString() != "0" && !shiwakeEnable){
				inputShiireKbn.val(response.shiireKbn);
			}
			if ((response == null || response.futanBumonName == null || response.futanBumonName == "") && title != "") {
				alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
			}
			//親画面でdialogCallbackFutanBumonSentaku()が定義されているときに、子画面ではdialogCallbackFutanBumonSentaku()を動かしたくない場合
			//　ボタン押下時Functionとロストフォーカス時FunctionのどちらにもdialogCallbackFutanBumonSentakuの空functionを定義してください
			if ("dialogCallbackFutanBumonSentaku" in window)	{
				dialogCallbackFutanBumonSentaku();
			}
		}
	});
}

/**
 * ヘッダーフィールド選択ボタン押下時Function
 *
 * 'dialog'id内にユニバーサルフィールド選択画面をロードする。
 *
 * ヘッダーフィールドを選択してダイアログを閉じた場合、呼び出し元画面に選択を反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * ◆当function呼び出し前に、呼び出し元画面-選択結果反映先inputタグのjQueryオブジェクトを以下のグローバス変数にセットされたい。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名
 * 		----------------------------------------
 * 		dialogRetHeaderFiedCd	:ヘッダーフィールドコード
 * 		dialogRetHeaderFiedName	:ヘッダーフィールド名
 *
 * ◆当function呼び出し前に、取引選択時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackHeaderSentaku
 *
 * @param no 1(HF1) or 2(HF2) or 3(HF3)
 */
function commonHeaderSentaku(no) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "ヘッダーフィールド選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("header_sentaku?no=" + no);
}

/**
 * ヘッダーフィールドコードロストフォーカス時Function
 */
function commonHeaderCdLostFocus(inputCode, inputName, no, title) {
	var code = inputCode.val();
	if(code == "" || code == null) {
		inputName.val(null);
		return;
	}
	$.ajax({
		type : "GET",
		url : "header_name_get",
		data: { 'hfCd': code, 'no': no },
		dataType : 'text',
		success : function(response) {
			inputName.val(response);
			if (response == "" && title != "") {
				alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
			}
			if ("dialogCallbackHeaderSentaku" in window)	{
				dialogCallbackHeaderSentaku();
			}
		}
	});
}

/**
 * ユニバーサルフィールド選択ボタン押下時Function
 *
 * 'dialog'id内にユニバーサルフィールド選択画面をロードする。
 *
 * ユニバーサルフィールドを選択してダイアログを閉じた場合、呼び出し元画面に選択を反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * ◆当function呼び出し前に、呼び出し元画面-選択結果反映先inputタグのjQueryオブジェクトを以下のグローバス変数にセットされたい。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名
 * 		----------------------------------------
 * 		dialogRetUniversalFiedCd	:ユニバーサルフィールドコード
 * 		dialogRetUniversalFiedName	:ユニバーサルフィールド名
 *
 * ◆当function呼び出し前に、取引選択時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackUniversalSentaku
 *
 * @param kamokuCd 勘定科目コード
 * @param no 1-10
 */
function commonUniversalSentaku(kamokuCd, no) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "ユニバーサルフィールド選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("universal_sentaku?kamokuCd=" + kamokuCd + "&no=" + no + "&isKotei=0");
}

/**
 * ユニバーサルフィールド(固定)選択ボタン押下時Function
 * ※呼び出し時のルールはユニバーサルフィールド選択と同様
 */
function commonUniversalKoteiSentaku(kamokuCd, no) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "ユニバーサルフィールド固定選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("universal_sentaku?kamokuCd=" + kamokuCd + "&no=" + no + "&isKotei=1");
}

/**
 * ユニバーサルフィールドコードロストフォーカス時Function
 */
function commonUniversalCdLostFocus(inputCode, inputName, no, title) {
	var code = inputCode.val();
	if(code == "" || code == null) {
		inputName.val(null);
		return;
	}
	$.ajax({
		type : "GET",
		url : "universal_name_get",
		data: { 'ufCd': code, 'no': no, 'isKotei': "0"},
		dataType : 'text',
		success : function(response) {
			inputName.val(response);
			if (response == "" && title != "") {
				alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
			}
			if ("dialogCallbackUniversalSentaku" in window)	{
				dialogCallbackUniversalSentaku();
			}
		}
	});
}

/**
 * ユニバーサルフィールド(固定)コードロストフォーカス時Function
 */
function commonUniversalKoteiCdLostFocus(inputCode, inputName, no, title) {
	var code = inputCode.val();
	if(code == "" || code == null) {
		inputName.val(null);
		return;
	}
	$.ajax({
		type : "GET",
		url : "universal_name_get",
		data: { 'ufCd': code, 'no': no, 'isKotei': "1"},
		dataType : 'text',
		success : function(response) {
			inputName.val(response);
			if (response == "" && title != "") {
				alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
			}
			if ("dialogCallbackUniversalSentaku" in window)	{
				dialogCallbackUniversalSentaku();
			}
		}
	});
}

/**
 * ユーザー選択ボタン押下時Function
 * 'dialog'id内にユーザー選択画面をロードする。
 *
 * ユーザー選択ダイアログでユーザーを選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 当function呼び出し前に、呼び出し元画面-選択結果反映先inputタグのnameを以下のグローバル変数にセット。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名			:項目名
 * 		----------------------------------------
 * 		dialogRetuserId				:ユーザーID
 * 		dialogRetusername			:ユーザー名（ユーザー姓+(全角ブランク)+ユーザー名　形式）
 * 		dialogRetshainNo			:社員番号
 * 		dialogRetuserBumonCd		:部門コード
 * 		dialogRetuserBumonName		:部門名
 * 		dialogRetCardNum			:法人カードチェック
 * 		dialogRetTableArea			:ユーザー変更による法人カード表示制御の場所
 *
 * @param flag boolean。（有効期限的に）無効な部門とユーザーの表示を行う場合true。行わないならfalse。
 * @param houjinRirekiNo 法人カード使用履歴明細番号。指定がある場合、該当カード履歴のカード番号と法人カード識別用番号が一致するユーザーのみ表示。
 */
function commonUserSentaku(flag,houjinRirekiNo) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "ユーザー選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	});
	if(houjinRirekiNo != null && houjinRirekiNo != ""){
		$("#dialog").load("user_sentaku?houjinRirekiNo=" + houjinRirekiNo );
	}else if(flag) {
		$("#dialog").load("user_sentaku?isAllDate=true");
	} else {
		$("#dialog").load("user_sentaku");
	}
}

/**
 * 社員番号ロストフォーカス時Function
 * @param inputCode	社員番号のquery
 * @param inputName	ユーザー名のquery
 * @param title		ラベル文言
 * @param userId	ユーザーIDののquery(オプション)
 * @param card		法人カードチェック
 *  *◆当function呼び出し前に、社員番号ロストフォーカス時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackShainNoLostFocus
 *
 */
function commonShainNoLostFocus(inputCode, inputName, title, userId, card) {
	var code = inputCode.val();
	if(code == "" || code == null) {
		inputName.val("");
		if (userId) userId.val("");
		if (card) {
			card.closest("span").hide();
			card.prop("checked", false);
		}
		return;
	}
	$.ajax({
		type : "GET",
		url : "user_shain_name_get",
		data : "shainNo=" + code,
		dataType : 'text',
		success : function(response) {
			inputName.val("");
			if (userId) userId.val("");
			if (card) {
				card.val("");
				card.closest("span").hide();
				card.prop("checked", false);
			}
			if (response == "") {
				alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
			} else {
				var dataAry = response.split("\t");
				inputName.val(dataAry[0]);
				if (userId) userId.val(dataAry[1]);
				if (card) {
					if (dataAry[2] == '1') {
						card.closest("span").show();
						if(card)card.val(dataAry[3]);
					} else {
						card.closest("span").hide();
						card.prop("checked", false);
					}
				}
			}
			if ("dialogCallbackShainNoLostFocus" in window)	{
				dialogCallbackShainNoLostFocus();
			}
		}
	});
}

/**
 * 被代行者選択ボタン押下時Function
 * 'dialog'id内にユーザー選択画面をロードする。
 *
 * 被代行者選択ダイアログでユーザーを選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * 当function呼び出し前に、呼び出し元画面-選択結果反映先inputタグのnameを以下のグローバル変数にセット。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名			:項目名
 * 		----------------------------------------
 * 		dialogRetHiDaikouUserId			:ユーザーID
 * 		dialogRetHiDaikouUsername		:ユーザー名（ユーザー姓+(全角ブランク)+ユーザー名　形式）
 *
 *◆当function呼び出し前に、取引選択時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackHiDaikouUserSentaku
 */
function commonHiDaikouUserSentaku() {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}

	$("#dialog2")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "被代行者選択",
		buttons: {閉じる: function() {$("#dialog2").children().remove();$(this).dialog("close");}}
	});
	$("#dialog2").load("hi_daikou_user_sentaku");
}
/**
 * 部門検索ボタン押下時Function
 * 'dialog'id内に部門選択画面をロードする。
 *
 * 部門選択ダイアログで部門を選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 * どちらかで呼んでください。
 *
 * ①当function呼び出し前に、呼び出し元画面-選択結果反映先inputタグのjQueryオブジェクトを以下のグローバス変数にセット
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名			:項目名
 * 		----------------------------------------
 * 		dialogRetBumonCd			:部門コード
 * 		dialogRetBumonName			:部門名
 * 		dialogRetBumonFullName		:部門名（親部門｜自部門　形式のフル名）
 *
 * ②当function呼び出し前に、コールバックfunctionを以下のグローバル変数にセット。
 * 		dialogRetBumonSentakuCallback(引数は選択したaタグ)
 *
 * @param flag boolean。（有効期限的に）無効な部門の表示を行う場合true。行わないならfalse。
 */
function commonBumonSentaku(flag) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "部門選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	});

	if(flag) {
		$("#dialog").load("bumon_sentaku?isAllDate=true");
	} else {
		$("#dialog").load("bumon_sentaku");
	}
}

/**
 * 部門コードロストフォーカス時Function
 */
function commonBumonCdLostFocus(flag, inputCode, inputName, title) {
	var code = inputCode.val();
	if(code == "" || code == null) {
		inputName.val(null);
		return;
	}
	$.ajax({
		type : "GET",
		url : "bumon_name_get",
		data : "bumonCd=" + code + "&isAllDate=" + flag,
		dataType : 'text',
		success : function(response) {
			inputName.val(response);
			if (response == "" && title != "") {
				alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
			}
		}
	});
}

/**
 * 役割選択ボタン押下時Function
 * 'dialog'id内に部門ロール選択画面(役割選択画面)をロードする。
 *
 * 部門ロール選択ダイアログで部門ロールを選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * 当function呼び出し前に、呼び出し元画面-選択結果反映先inputタグのjQueryオブジェクトを以下のグローバス変数にセットされたい。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名			：
 * 		----------------------------------------
 * 		dialogRetBumonRoleId		:部門ロールID
 * 		dialogRetBumonRoleName		:部門ロール名
 */
function commonBumonRoleSentaku() {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "役割選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("bumon_role_sentaku");
}
/**
 * 合議部署選択ボタン押下時Function
 * 'dialog'id内に部門ロール選択画面(合議部署選択画面)をロードする。
 *
 * 合議部署選択ダイアログで合議部署を選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * 当function呼び出し前に、呼び出し元画面-選択結果反映先inputタグのjQueryオブジェクトを以下のグローバス変数にセットされたい。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名			：
 * 		----------------------------------------
 * 		dialogRetGougiBushoId		:合議部署ID
 * 		dialogRetGougiBushoName		:合議部署名
 */
function commonGougiBushoSentaku() {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "合議部署選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("gougi_busho_sentaku");
}
/**
 * 伝票選択ボタン押下時Function
 *
 * 本画面クローズ時に、denpyouKbnDataというグローバルに
 * 選択された要素を格納する。
 * 要素には以下の属性が含まれる
 * 		属性名						：項目名
 * 		data-cd						：伝票区分
 * 		data-name					：伝票種別
 * 		data-kind					：業務種別
 * 		data-setsumei				：内容
 *
 * ◆当function呼び出し前に、コールバックするfunctionをセットする。
 * コールバック処理不要の場合、セット不要。
 * 		dialogCallbackDenpyouSentaku
 */
function commonDenpyouSentaku() {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "伝票選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("denpyou_sentaku");
}

/**
 * 業務ロール選択ボタン押下時Function
 * 'dialog'id内に業務ロール選択画面をロードする。
 *
 * 業務ロール選択ダイアログで業務ロールを選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 * どちらかで呼んでください。
 *
 * ①当function呼び出し前に、呼び出し元画面-選択結果反映先inputタグのjQueryオブジェクトを以下のグローバス変数にセットされたい。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名			：
 * 		----------------------------------------
 * 		dialogRetGyoumuRoleId		:業務ロールID
 * 		dialogRetGyoumuRoleName		:部門ロール名
 *
 * ②当function呼び出し前に、コールバックfunctionを以下のグローバル変数にセット。
 * 		dialogRetGyoumuRoleSentakuCallback(引数は選択したaタグ)
 */
function commonGyoumuRoleSentaku() {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "業務ロール選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	}).load("gyoumu_role_sentaku");
}
/**
 * 旅費精算（過去明細）入力ボタン押下時Function
 * 'dialog'id内に旅費精算（過去明細）選択画面をロードする。
 */
function commonKakoMeisaiSentaku(path) {

	parentPath = path;

	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}

	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "旅費精算（過去明細選択）",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("sub/RyohiSeisanKakoMeisaiSentaku.html");
}

/**
 * プロジェクト選択ボタン押下時Function
 * 'dialog'id内に選択画面をロードする。
 *
 * ダイアログでプロジェクトを選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * ◆当function呼び出し前に、呼び出し元画面の選択結果反映先inputタグのクエリオブジェクトをセットする。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名		:項目名
 * 		----------------------------------------
 * 		dialogRetProjectCd		:プロジェクトコード
 * 		dialogRetProjectName	:プロジェクト名

 * ◆当function呼び出し前に、取引選択時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackProjectSentaku
 */
function commonProjectSentaku() {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "プロジェクト選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("project_sentaku");
}

/**
 * プロジェクトコードロストフォーカス時Function
 */
function commonProjectCdLostFocus(inputCode, inputName, title) {
	var code = inputCode.val();
	if(code == "" || code == null) {
		inputName.val(null);
		return;
	}
	$.ajax({
		type : "GET",
		url : "project_name_get",
		data : "projectCd=" + code,
		dataType : 'text',
		success : function(response) {
			inputName.val(response);
			if (response == "" && title != "") {
				alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
			}
			if ("dialogCallbackProjectSentaku" in window) {
				dialogCallbackProjectSentaku();
			}
		}
	});
}

/**
 * セグメント選択ボタン押下時Function
 * 'dialog'id内に選択画面をロードする。
 *
 * ダイアログでセグメントを選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * ◆当function呼び出し前に、呼び出し元画面の選択結果反映先inputタグのクエリオブジェクトをセットする。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名		:項目名
 * 		----------------------------------------
 * 		dialogRetSegmentCd		:セグメントコード
 * 		dialogRetSegmentName	:セグメント名

 * ◆当function呼び出し前に、取引選択時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackSegmentSentaku
 */
function commonSegmentSentaku() {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "セグメント選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("segment_sentaku");
}

/**
 * セグメントコードロストフォーカス時Function
 */
function commonSegmentCdLostFocus(inputCode, inputName, title) {
	var code = inputCode.val();
	if(code == "" || code == null) {
		inputName.val(null);
		return;
	}
	$.ajax({
		type : "GET",
		url : "segment_name_get",
		data : "segmentCd=" + code,
		dataType : 'text',
		success : function(response) {
			inputName.val(response);
			if (response == "" && title != "") {
				alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
			}
			if ("dialogCallbackSegmentSentaku" in window) {
				dialogCallbackSegmentSentaku();
			}
		}
	});
}

/**
 * 幣種選択ボタン押下時Function
 * 'dialog'id内に勘定科目選択画面をロードする。
 *
 * ダイアログで取引を選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * ◆当function呼び出し前に、呼び出し元画面の選択結果反映先inputタグのクエリオブジェクトをセットする。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名		:項目名
 * 		----------------------------------------
 * 		dialogRetHeishuCd			:幣種コード
 * 		dialogRetTsuukaTani			:通貨単位
 * 		dialogRetRate				:レート
 *
 * ◆当function呼び出し前に、取引選択時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackHeishuSentaku
 */
function commonHeishuSentaku() {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "幣種選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	})
	.load("heishu_sentaku");
}

/**
 * 幣種コードロストフォーカス時Function
 */
function commonHeishuCdLostFocus(inputCode, inputName, inputName2, title) {
	var code = inputCode.val();
	if(code == "" || code == null) {
		if(null != inputName) {
			inputName.text("");
			inputName.val("");
		}
		if(null != inputName2) inputName2.val(null);
		if ("dialogCallbackHeishuSentaku" in window)	{
			dialogCallbackHeishuSentaku();
		}
		return;
	}
	$.ajax({
		type : "GET",
		url : "heishu_name_get",
		data : "heishuCd=" + code,
		dataType : 'text',
		success : function(response) {
			var dataAry = response.split("\t");
			var dataAry = response.split("\t");
			var heishu = dataAry[0]
			if(code != "") {
				if (heishu == "" && title != "") {
					alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
				}
			}
			if(null != inputName){
				//ラベル・テキスト両方に対応できるように。
				inputName.text(dataAry[1]);
				inputName.val(dataAry[1]);
			}

			var rate = dataAry[2];
			var dl = 5;
			if(rate != ""){
				var num = new Number(rate);
				var tmp = rate.split(".");
				var tmp1 = tmp[1];
				for(var i=0; i< tmp1.length; i++){
					tmp1 = tmp1.replace(/0$/, "");
				}
				if(tmp1.length > 5){
					dl = tmp1.length;
				}
				rate = num.toFixed(dl);
			}
			if(null != inputName2) inputName2.val(rate);
			if ("dialogCallbackHeishuSentaku" in window)	{
				dialogCallbackHeishuSentaku();
			}
		}
	});
}

$.fn.myDatePicker = function(){
	this.removeClass('hasDatepicker').datepicker({
		//決定時点で強制カーソル移動(IEと他ブラウザでのdatepicker挙動差異を吸収)
		onSelect: function(date, obj) {
			var objQ = $(this);
			objQ.datepicker('hide');
			setTimeout(function(){
				var elements = $(ONFOCUS);
				var index = elements.index(objQ);
				if (index >= 0) {
					elements.eq(index+1).focus();
				}
				objQ.change();
				if (elements.eq(index+1).hasClass("datepicker")) {
					elements.eq(index+1).datepicker('show');
				}
			 }, 0);
		//取得した祝日リスト・曜日に合わせて文字色クラス変更
		},beforeShowDay: function(date){
			var dateStr = $.datepicker.formatDate('yymmdd', date);
			if( shukujitsuList.indexOf(dateStr) != -1 ) {
				return [true, 'date_shk',''];	//祝日
			}else if(date.getDay() == 0){
				return [true, 'date_sun',''];	//日曜
			}else if(date.getDay() == 6){
				return [true, 'date_sat',''];	//土曜
			}else{
				return[true,"",""];
			}
		}
	}).datepicker("hide").mask("9999/99/99");

	this.each(function(index, element){
		//フォーカス指定時点での全角入力抑制
		//Androidでの文字入力のため、maxlength属性削除
		$(this).removeAttr("maxlength");
		$(this).get(0).type = 'tel';
	});
};

/**
 * システム共通の初期化処理Function
 * HTML内のDOM操作を行う。
 *
 * 以下のタイミングで呼ばれることを想定。
 * ・画面ロード後にbody配下に対して。
 * ・部分ロード（AJax、紙芝居時点での共通サブHTML読み込み）後、ロード部分のはいかに対して。
 *
 * 初期化とは具体的に以下のこと。
 * ・'datepicker'クラスを持つinputタグに対して
 * 		カレンダー入力補助
 * ・'autoNumeric'クラスを持つinputタグに対して
 * 		金額カンマ入力補助
 * ・'no-more-tables'クラス配下のtableタグに対して
 * 		thの中身（テーブルヘッダー）を対応するtdの'data-title'属性にコピー。
 * @param target 補助機能を付ける対象のセレクタ
 */
function commonInit(target) {

	//--------------------
	//入力補助
	//--------------------

	//日付
	target.find("input.datepicker:enabled:not([readonly])").attr("id", "").myDatePicker();

	//金額
	//カンマ付き金額で初期表示させた場合、autoNumericで不具合が起きるのを回避
	target.find("input.autoNumeric,input.autoNumericWithCalcBox,input.autoNumericNoMax,input.autoNumericDecimal,input.autoNumericDecimalThree,input.autoNumericDecimalFive,input.autoNumericDecimalNoPad").each(function(){
		$(this).val($(this).val().replaceAll(",", ""));
		//フォーカス指定時点での全角入力抑制
		if ( $(this).get(0).type == ("text") ) {
			$(this).get(0).type = 'tel';
		}
	});

	target.find("input.autoNumeric,input.autoNumericWithCalcBox").autoNumeric("init", {aPad: false, vMin:-999999999999, vMax:999999999999});
	target.find("input.autoNumericNoMax").autoNumeric("init", {aPad: false, vMin:-999999999999});
	target.find("input.autoNumericWithCalcBox").each(function(){
		//明細コピーした時に電卓画像もコピーされるが、jquery.calculator.jsがこれだと動作しないので、一旦電卓画像を取り除いてから再設定する。
		if ($(this).hasClass("hasCalculator")) {
			$(this).removeClass("hasCalculator");
			$(this).next("img:first").remove();
		}
		//電卓を付ける
		$(this).calculator({
			showOn: 'button',
			buttonImageOnly: true,
			buttonImage: '../../static/img/calculator.png',
			onOpen: function(value, inst) {
				// カンマ削除
				if ("" != value) {
					$(this).val(parseInt(value.replaceAll(",", "")));
				}

				// autoNumericの使用不可
				$(this).autoNumeric('destroy');
			}
			,onClose: function(value, inst) {
			   // autoNumericの使用不可
			   $(this).autoNumeric("init", {aPad: false, vMin:0, vMax:999999999999});

			   // 四捨五入(小数点対応)
			   var valueTemp = Math.round(value);

			   // カンマ追加
			   $(this).val(String(valueTemp).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,")).change();

			   // テキストフォーカス
			   $(this).focus();
			}
		});
	});
	target.find("input.autoNumericDecimal").autoNumeric("init", {aPad: true, vMin:-999999999999});
	target.find("input.autoNumericDecimalThree").autoNumeric("init", {aPad: true, vMin:-999999999999.999, vMax:999999999999.999, mDec: '3'});
	target.find("input.autoNumericDecimalFive").autoNumeric("init", {aPad: true, vMin:-999999999999, mDec: '5'});
	target.find("input.autoNumericDecimalNoPad").autoNumeric("init", {aPad: false, vMin:-999999999999, mDec: '5'});

	// 時刻
	//target.find("input.timepicker").timeInput();
	// timepicker.jsを採用↓
	target.find("input.timepicker:enabled:not([readonly])").timepicker();
	target.find("input.timepicker:enabled:not([readonly])").timepicker('option', 'step', '10');
	target.find("input.timepicker:enabled:not([readonly])").timepicker('option', 'timeFormat', 'H:i');

	//ログイン画面以外の各input部品にautocomplete=off属性追加(既に設定されている部品は除く、chromeでは該当設定無視される？)
	//ログイン画面であるかは現状は「form.form-signin」の部品があるかだけでチェック
	if( $("form.form-signin").length == 0 ){
		target.find("input").each(function(){
			if ($(this).attr('autocomplete') == undefined) {
				$(this).attr('autocomplete', 'off');
			}
		});
	}

	// コード入力項目の全角入力抑制(hankaku classを持つ項目または固定nameの項目) 固定nameは当初対応時に全JSPに手を入れるのがしんどかったんで
	var cdInp = "(";
	cdInp = cdInp + "shiwakeEdaNo";
	cdInp = cdInp + "|KamokuCd";
	cdInp = cdInp + "|kamokuEdabanCd";
	cdInp = cdInp + "|BumonCd";
	cdInp = cdInp + "|torihikisakiCd";
	cdInp = cdInp + "|projectCd";
	cdInp = cdInp + "|segmentCd";
	cdInp = cdInp + "|DenpyouId";
	cdInp = cdInp + "|SerialNo";
	cdInp = cdInp + "|ShainNo";
	cdInp = cdInp + "|CardNum";
	cdInp = cdInp + "|Edano";
	cdInp = cdInp + "|hyoujijun";
	cdInp = cdInp + "|nissuu";
	cdInp = cdInp + "|kyuujitsuNissuu";
	cdInp = cdInp + "|seisankikanFromHour";
	cdInp = cdInp + "|seisankikanFromMin";
	cdInp = cdInp + "|seisankikanToHour";
	cdInp = cdInp + "|seisankikanToMin";
	cdInp = cdInp + ")+";
	reg = new RegExp(cdInp,"i");
	$('input').filter(function(){
	    if(this.name.match(reg)){
	    	return true;
	    }
	    if($(this).hasClass("hankaku")){
	    	return true;
	    }
	    return false;
	}).each(function(){
		if ( $(this).get(0).type == ("text") ) {
			$(this).get(0).type = 'tel';
		}
	});

	//ゼロパディング
	target.find("input.zeropadding").blur(function(){
		var tmpVal = $(this).val();
		if ("" == tmpVal || tmpVal.match(/[^0-9]/)) {
			$(this).val("");
		} else {
			$(this).val(("00000000000000000000"  + tmpVal).slice(0 - $(this).attr("maxlength")));
		}
	});

	//--------------------
	//テーブルの縮小表示対応
	//--------------------
	target.find(".no-more-tables table").each(function(){
		var thead = $(this).find("thead");
		var tbody = $(this).find("tbody");
		var theadThs = $(thead.children()[0]).children();
		var tbodyTrs = tbody.children();
		var tbodyTds;
		var i;
		var j;
		for (i = 0; i < tbodyTrs.length; i++) {
			tbodyTds = $(tbodyTrs[i]).children();
			for (j = 0; j < theadThs.length; j++) {
				$(tbodyTds[j]).attr("data-title", $(theadThs[j]).text());
				if ("" == $(tbodyTds[j]).html()) {
					$(tbodyTds[j]).text("　");
				} else if ($(tbodyTds[j]).find("nobr").length == 1 && "" == $(tbodyTds[j]).find("nobr").text()) {
					$(tbodyTds[j]).find("nobr").text("　");
				}
			}
		}
	});
	
	// inputタグ共通のサイズチェック&オーバー時クリア処理
	$("input[type='file']").change(function () { fileSizeCheck(this) });
}

/**
 * 共通部分読み込み
 * 読み込み後、汎用入力補助機能を付ける
 * @param target 読み込み先のセレクタ
 * @param html 読み込むhtmlパス
 */
function loadSub(target, html) {
	target.load(html, null, function(){
		commonInit(target);
	});
}

/**
 * 背景の強調表示を行う
 * @param none 強調しない部分のセレクタ
 * @param current 強調する部分のセレクタ
 */
function choice(none, current) {
	none.removeClass("choice-current").addClass("choice-none");
	current.removeClass("choice-none").addClass("choice-current");
}

/**
 * 表示切り替えを行う。
 * @param none 非表示とするセレクタ
 * @param block ブロック表示とするセレクタ
 */

function displayOnOff (none, block){
	none.css("display", "none");
	block.css("display", "block");
}

/**
 * 印刷プレビューを表示する(IEのみ)
 */
function printPreview()
{
	if(window.ActiveXObject == null || document.body.insertAdjacentHTML == null) return;
	var sWebBrowserCode = '<object width="0" height="0" classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></object>';
	document.body.insertAdjacentHTML('beforeEnd', sWebBrowserCode);
	var objWebBrowser = document.body.lastChild;
	if(objWebBrowser == null) return;
	objWebBrowser.ExecWB(7, 1);
	document.body.removeChild(objWebBrowser);
}

$(document).ready(function(){

	//ダーティーフラグ。内容が変更されたらtrueになる。
	isDirty = false;
	$("input,textarea,select").change(function(){
		// nonChkDirtyエリアは変更されていてもなにもしない
		if($(this).closest(".nonChkDirty").size() == 0){
			isDirty = true;
		}
    });
	$("#kaikeiContent").find("button").click(function(){
		// nonChkDirtyエリアと「選択」ボタン、「消費税額詳細」ボタン、及び空のテキストボックスを「クリア」した場合は変更されていてもなにもしない
		if($(this).text() != "消費税額詳細" && !($(this).text() == "クリア" && $(this).prev("input").val() == "") && $(this).text() != "選択" && $(this).closest(".nonChkDirty").size() == 0){
			isDirty = true;
		}
    });

	//class指定から共通的に入力補助機能を付ける
	commonInit($("body"));

	//submit前処理
	$("form").submit(function(){
		//各種送信項目のdisabled解除
		$(this).find("input,textarea,select,option").removeAttr("disabled");

		//&nbsp;をブランクに変換
		$(this).find("input[type=text],input[type=hidden],textarea").each(function(){
			$(this).val($(this).val().replaceAll(NBSP, " "));
		});

		//エラー欄削除
		$("div.alert").first().remove();

		//アクションブロック ※大半のsubmitは画面ロードし直すのでほっといてよいが、ファイルダウンロード等画面ロードし直さないsubmitもあるので一定時間後に戻してやる
		bodyBlanket = $("<div class='ui-widget-overlay ui-front' style='z-index: 99999;'></div>")
		bodyBlanket.appendTo(document.body);
		setTimeout(function(){bodyBlanket.remove();}, 3000);
	});

	//AJAX(load)時
	var orgLoad = $.fn.load;
	$.fn.load = function(url, params, cback) {
		//&nbsp;をブランクに変換
		if (params != null) {
			for (var key in params) {
				var val = params[key];
				if (val != null && typeof val === "string") {
					params[key] = val.replaceAll(NBSP, " ");
				}
			}
		}

		var result = orgLoad.apply(this, arguments);
		$(".ui-dialog-buttonset").find("button").keypress(inputsKeypress);
		return result;
	};

	//AJAX送信前
	$(document).ajaxStart(function(){
		//アクションブロック
		dialogBlanket = $("<div class='ui-widget-overlay ui-front' style='z-index: 99999;'></div>")
		dialogBlanket.appendTo(document.body);
	});

	//AJAX送信後(成功/失敗によらず)
	$(document).ajaxComplete(function(){
		//アクション可能に戻す
		dialogBlanket.remove();
	});

	//ボタンは一律印刷非表示
	$("button").addClass("non-print");

	//Enterキーをカーソル移動にする
	$("body").delegate("input,select,button", "keypress", inputsKeypress);

});

/**
 * 打鍵イベント
 * Enterキー：カーソル遷移（本来のアクションはキャンセル）
 * Enterキー以外：何もせず（本来のアクション）
 * @param e	イベント
 */
function inputsKeypress(e) {
	var elements = $(ONFOCUS);
	var c = e.which ? e.which : e.keyCode;
	if (c == 13) {
		var index = elements.index($(this));
		index = (e.shiftKey) ? index - 1 : index + 1;
		if (index >= 0) elements.eq(index).focus();
		e.preventDefault();
	}
}

/**
 * 駅名検索ボタン押下時Function
 * 'dialog'id内に駅名選択画面をロードする。
 *
 * 駅名選択ダイアログで部門を選択して閉じた場合、呼び出し元画面に反映する。
 *
 *
 * ①当function呼び出し前に、呼び出し元画面-選択結果反映先inputタグのjQueryオブジェクトを以下のグローバス変数にセットされたい。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名			:項目名
 * 		----------------------------------------
 * 		dialogRetStationName		:駅名
 *
 */
function commonStationSentaku(searchName) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}

	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "駅名選択",
		buttons: {
			'閉じる': function(event) {
				$("#dialog").children().remove();
				$("#dialog").dialog('close');
			}
		}
	});


	$("#dialog").load("station_sentaku?searchName=" + encodeURI(searchName));

}

/**
 * 経路検索ボタン押下時Function
 * 'dialog'id内に経路選択画面をロードする。
 *
 * 経路選択ダイアログで部門を選択して閉じた場合、呼び出し元画面に反映する。
 *
 *
 * ①当function呼び出し前に、呼び出し元画面-選択結果反映先inputタグのjQueryオブジェクトを以下のグローバス変数にセットされたい。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名			:項目名
 * 		----------------------------------------
 * 		dialogRetRouteKukan		:経路
 * 		dialogRetRouteMoney		:金額
 *
 */
function commonRouteSentaku(from, to01, to02, to03, to04, date, shiyouKikanKbn, hyoujiCnt, sortKbn, teiki) {

	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}

	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "620",
		title: "経路選択",
		buttons: {
			'閉じる': function(event) {
				$("#dialog").children().remove();
				$("#dialog").dialog('close');
			}
		}
	});

	$("#dialog").load("route_sentaku?date=" + encodeURI(date) + "&shiyouKikanKbn=" + encodeURI(shiyouKikanKbn) +
			"&from=" + encodeURI(from) + "&to01=" + encodeURI(to01) + "&to02=" + encodeURI(to02)
			 + "&to03=" + encodeURI(to03) + "&to04=" + encodeURI(to04) + "&hyoujiCnt=" + encodeURI(hyoujiCnt)
			 + "&sortKbn=" + encodeURI(sortKbn) + "&teiki=" + encodeURI(teiki));

}
/**
 * 路線図押下時Function
 * 'dialog'id内に路線図選択画面をロードする。
 *
 * 路線図選択ダイアログで駅名を選択して閉じた場合、呼び出し元画面に反映する。
 * @param station
 */
function commonRailMapSentaku(from, to01, to02, to03, to04) {

	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}

	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "路線図選択",
		buttons: {
			'選択': function(event) {
				railMapSentakuClick();
			},
			'閉じる': function(event) {
				$("#dialog").children().remove();
				$("#dialog").dialog('close');
			}
		}
	});

	$("#dialog").load("railmap_sentaku?" + "selStName1=" + encodeURI(from) + "&selStName2=" + encodeURI(to01) +
			"&selStName3=" + encodeURI(to02) + "&selStName4=" + encodeURI(to03) + "&selStName5=" + encodeURI(to04));
}

/**
 * 起案番号選択Function
 * 'dialog'id内に起案番号選択画面をロードする。
 *
 * 起案番号選択ダイアログで略号を選択して閉じた場合、呼び出し元画面に反映する。
 * 		----------------------------------------
 * 			dialogRetKianBangouRyakugou	:略号
 * 			dialogRetKianBangouNendo	:年度
 */
function commonKianBangouSentaku() {
	var width = "600";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "起案番号選択",
		buttons: {閉じる: function() {$(this).dialog("close");}}
	});

	$("#dialog").load("kian_bangou_sentaku");
}

/**
 * ウインドウサイズチェックFunction
 * windowサイズが「767px」以下かどうか判定する。
 * スマホ対応
 *
 * @return 「767px」以下ならtrue。「767px」より大きければfalse
 */
function windowSizeChk() {
	if($(window).width() <= 767) {
		return true;
	}
	return false;
}

/**
 * 閉じる(閉じれない場合はメッセージ)
 */
function winClose(){
	window.close();
}


$.fn.timeInput = function() {
	$(this).each(function(){
		$(this).val(fmtTime($(this).val()));
		$(this).change(function(){
			$(this).val(fmtTime($(this).val()));
		});
	});
}

function fmtTime(s){
	s = s.replace(/[Ａ-Ｚａ-ｚ０-９]/g,function(s){return String.fromCharCode(s.charCodeAt(0)-0xFEE0)});
	s = s.replace("：", ":");

	if (! s.match("^[0-9]{1,4}:[0-9]{1,2}$")) return "";

	// 時のパディング
	var left = s.split(":")[0];
	left = String(Number(left));
	if (left.length < 2) {
		left = "00" + left;
		left = left.slice(-2);
	}
	// 分のパディング
	var right = s.split(":")[1];
	right = "00" + right;
	right = right.slice(-2);
	return left + ":" + right;
};


/**
 * JSP上の「data-delayenable」クラスが設定されているinput部品に対して、
 * 一時的に該当部品にdisabled属性を設定し、1000ミリ秒経過後解除。
 * ※Chromeのオートコンプリート機能抑制のため
 */
function delayEnable() {
	setTimeout(function(){
		$("input.data-delayenable").prop("disabled",false);
	}, 1000);
}

/**
 * 銀行コードロストフォーカス時Function
 *
 * ダイアログで銀行名称を選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * ◆当function呼び出し前に、呼び出し元画面の選択結果反映先inputタグのクエリオブジェクトをセットする。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名		:項目名
 * 		----------------------------------------
 * 		dialogRetGinkouCd		:銀行コード
 * 		dialogRetGinkouName		:銀行名称
 *		isMasterSearch :マスター検索か
 *
 * ◆当function呼び出し前に、取引選択時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackRetGinkouSentaku
 */
function commonGinkouCdLostFocus(inputCode, inputName, title, isMasterSearch) {
	var ginkouCd = inputCode.val();

	//支店クリア
	$("[name=furikomiGinkouShitenCd]").val("");
	$("[name=furikomiGinkouShitenName]").val("");

	//コード0パッディング
	if (ginkouCd == "" || ginkouCd.match(/[^0-9]/)) {
		ginkouCd = ""
	} else {
		ginkouCd = ("0000"  + ginkouCd).slice(-4);
	}
	inputCode.val(ginkouCd);

	//一旦名前消してからAJAXで取得
	$("[name=furikomiGinkouName]").val("");
	if(ginkouCd != "") {
		$.ajax({
			type : "GET",
			url : "ginkou_sentaku_name_get",
			data : $.param({ginkouCd: ginkouCd, isMasterSearch: isMasterSearch}),
			dataType : 'text',
			success : function(response, isMasterSearch) {
				if (response == "" && title != "") {
					alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
				}
				inputName.val(isMasterSearch ? response.replace(new RegExp('exists$'), '') : response); // マスター検索のとき、空名称対策で最後に含ませたexistsを削除
				if ("dialogCallbackRetGinkouSentaku" in window) {
					dialogCallbackRetGinkouSentaku();
				}
			}
		});
	}
}

/**
 * 銀行支店コードロストフォーカス時Function
 *
 * ダイアログで支店名称を選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * ◆当function呼び出し前に、呼び出し元画面の選択結果反映先inputタグのクエリオブジェクトをセットする。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名				:項目名
 * 		----------------------------------------
 * 		dialogRetGinkouShitenCd			:支店コード
 * 		dialogRetGinkouShitenName		:支店名称
 */
function commonGinkouShitenCdLostFocus(inputCode, inputName, title, ginkouCd) {
	var ginkouCd = ginkouCd;
	var shitenCd = inputCode.val();

	//コード0パッディング
	if (shitenCd == "" || shitenCd.match(/[^0-9]/)) {
		shitenCd = ""
	} else {
		shitenCd = ("000"  + shitenCd).slice(-3);
	}
	inputCode.val(shitenCd);

	//一旦名前消してからAJAXで取得
	$("[name=furikomiGinkouShitenName]").val("");
	if(ginkouCd == "") {
		alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "を入力してください。");
	} else if (shitenCd != "") {
		$.ajax({
			type : "GET",
			url : "ginkou_shiten_sentaku_name_get",
			data : $.param({ginkouCd: ginkouCd, shitenCd: shitenCd}),
			dataType : 'text',
			success : function(response) {
				if (response == "") {
					alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
					return
				}
				inputName.val(response);
			}
		});
	}
}

/**
 * 集計部門コードロストフォーカス時アクション（小画面内での呼び出し）
 *
 * ダイアログで集計部門名を選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * ◆当function呼び出し前に、呼び出し元画面の選択結果反映先inputタグのクエリオブジェクトをセットする。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名				:項目名
 * 		----------------------------------------
 * 		dialogRetSyuukeiBumonCd			:集計部門コード
 * 		dialogRetSyuukeiBumonName		:集計部門名
 */
function syuukeiBumonCdLostFocus(syuukeiBumonCd, mode, denpyouId, kihyouBumonCd) {

	var tmpStr = "";
	if(! ($('#kensaku input[name=kamokuCd]').val() == null) ){
		tmpStr = $('#kensaku input[name=kamokuCd]').val();
	}
	$("div.alert").remove();
	$("#dialogMain").load("futan_bumon_sentaku",	{
		syuukeiBumonCd		: syuukeiBumonCd,
		mode				: mode,
		denpyouId			: denpyouId,
		kihyouBumonCd		: kihyouBumonCd,
		kamokuCd			: tmpStr
	});
}

/**
 * 集計部門コードロストフォーカス時アクション
 *
 * ダイアログで集計部門名を選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * ◆当function呼び出し前に、呼び出し元画面の選択結果反映先inputタグのクエリオブジェクトをセットする。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名				:項目名
 * 		----------------------------------------
 * 		dialogRetSyuukeiBumonCd			:集計部門コード
 * 		dialogRetSyuukeiBumonName		:集計部門名
 */
function syuukeiBumonCdLostFocusGamen(mode, inputCode, inputName, title, denpyouId, kihyouBumonCd) {
	var code = inputCode.val();
	if(code == "" || code == null) {
		inputName.val(null);
		return;
	}
	$.ajax({
		type : "GET",
		url : "syuukei_bumon_name_get",
		data : {
			syuukeiBumonCd: code,
			mode: mode,
			denpyouId: denpyouId,
			kihyouBumonCd : kihyouBumonCd
			},
		dataType : 'text',
		success : function(response) {
			inputName.val(response);
			if (response == "" && title != "") {
				alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
			}
			if ("dialogCallbacksyuukeiBumonSentaku" in window)	{
				dialogCallbacksyuukeiBumonSentaku();
			}
		}
	});
}

/**
 * 集計部門選択ダイアログを表示させる
 */
function syuukeiDialogOpen(mode, denpyouId, kihyouBumonCd) {

	var width = "600";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#syuukeiDialog").dialog({
		modal: true,
		width: width,
		height: "520",
		title: "集計部門選択",
		buttons: {閉じる: function() {$(this).dialog("close");}},
		close: function() {
			$("#syuukeiDialog").children().remove();
		}
	}).load("syuukei_bumon_sentaku",	{
		mode				: mode,
		denpyouId			: denpyouId,
		kihyouBumonCd		: kihyouBumonCd
	});
}

/**
 * cookieに明細画面のサイズを保存する
 */
function saveMeisaiSize(dialog, key){
	//pathを設定しているのはライブラリ入替前との互換を持たすため
	var path = location.pathname.split('/');	// 例："/eteam/schemaname/appl/" → ["", "eteam", "schemaname", "appl" ""]
	var option = {expires: 1, path:"/eteam/" + path[2] + "/appl"};		//サイズ変更したらまる1日有効
	if(null != key){
		Cookies.set(key+"H", dialog.dialog('option', 'height'), option);
		Cookies.set(key+"W", dialog.dialog('option', 'width'),  option);
	}
}

/**
 * cookieに保存された明細画面の大きさをダイアログオプションに反映
 */
function resizeMeisai(dialog, key){
	if(null != Cookies.get(key+"H")) dialog.dialog('option', 'height', Cookies.get(key+"H"));
	if(null != Cookies.get(key+"W")) dialog.dialog('option', 'width',  Cookies.get(key+"W"));
}

/**
 * 財務拠点入力パターンNoロストフォーカス時Function
 *
 * @param denpyouKbn 呼び出し元の伝票区分を特定したい時だけ渡す。
 */
function commonZaimuKyotenNyuryokuPatternNoLostForcus(denpyouKbn, zaimuKyotenNyuryokuPatternNo, title, onlyNyuryokuUser) {
	if(zaimuKyotenNyuryokuPatternNo == "" || zaimuKyotenNyuryokuPatternNo == null) {
		if ("dialogRetZaimuKyotenNyuryokuPatternName" in window)	{
			dialogRetZaimuKyotenNyuryokuPatternName.val("");
		}
		return;
	}

	$.ajax({
		type : "GET",
		url : "zaimu_kyoten_nyurykou_pattern_name_get",
		data : "zaimuKyotenNyuryokuPatternNo=" + zaimuKyotenNyuryokuPatternNo + "&denpyouKbn=" + denpyouKbn + "&onlyNyuryokuUser=" + onlyNyuryokuUser,
		dataType : 'text',
		success : function(response) {
			var dataAry = response.split("\t");
			var name = dataAry[0];
			var kamokuCd = dataAry[1];
			var kamokuName = dataAry[2];
			var futanBumonCd = dataAry[3];
			var futanBumonName = dataAry[4];
			var kamokuEdabanCd = dataAry[5];
			var kamokuEdabanName = dataAry[6];

			if (name == "" && title != "") {
				alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
			}

			if ("dialogRetZaimuKyotenNyuryokuPatternName" in window)	{
				dialogRetZaimuKyotenNyuryokuPatternName.val(name);
			}
			if ("dialogRetKamokuCd" in window)	{
				dialogRetKamokuCd.val(kamokuCd);
			}
			if ("dialogRetKamokuName" in window)	{
				dialogRetKamokuName.val(kamokuName);
			}
			if ("dialogRetFutanBumonCd" in window)	{
				dialogRetFutanBumonCd.val(futanBumonCd);
			}
			if ("dialogRetFutanBumonName" in window)	{
				dialogRetFutanBumonName.val(futanBumonName);
			}
			if ("dialogRetKamokuEdabanCd" in window)	{
				dialogRetKamokuEdabanCd.val(kamokuEdabanCd);
			}
			if ("dialogRetKamokuEdabanName" in window)	{
				dialogRetKamokuEdabanName.val(kamokuEdabanName);
			}

			if ("dialogCallbackZaimuKyotenNyuryokuPatternNoSentaku" in window){
				dialogCallbackZaimuKyotenNyuryokuPatternNoSentaku();
			}
		}

	});
}

/**
 * 科目マスター情報取得Function
 */
function commonFindKamokuMaster(inputCode, title) {
	var code = inputCode.val();
	if(code == "" || code == null) {
		return;
	}
	$.ajax({
		type : "GET",
		url : "kamoku_master_get",
		data : "kamokuCd=" + code,
		dataType : 'text',
		success : function(response) {
			var dataAry = response.split("\t");
			var kazeiKbn = dataAry[0];
			var zeiritsu = dataAry[1];
			var keigenZeiritsuKbn = dataAry[2];
			var bunriKbn = dataAry[3];
			var kobetsuKbn = dataAry[4];
			var shouhizeitaishouKamokuEnableInputFlg = dataAry[5];
			var taikakingakuNyuuryokuFlg = dataAry[6];
			var gaikaFlg = dataAry[7];
			var shoriGroup = dataAry[8];
			if (response == "") {
				inputName.val(response);
				if (response == "" && title != "") {
					alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
				}
			}
			if ("dialogRetKazeiKbn" in window)	{
				// プルダウンの選択肢に「税抜」がなければ「税込」に変換。
				if(kazeiKbn == '002' && !(dialogRetKazeiKbn.find("option[value='002']").length)){
					kazeiKbn = "001";
				}
				dialogRetKazeiKbn.val(kazeiKbn);
			}
			if ("dialogRetZeiritsu" in window)	{
				dialogRetZeiritsu.val(zeiritsu);
			}
			if ("dialogRetKeigenZeiritsuKbn" in window)	{
				dialogRetKeigenZeiritsuKbn.val(keigenZeiritsuKbn);
			}
			if ("dialogRetBunriKbn" in window)	{
				// 課税区分が税込系で自動分離区分が0(無し)、1(自動分離)以外なのは認めない(マスタ上でそもそも無いかもしれないのだが)
				if(kazeiKbn == "001" || kazeiKbn == "011" || kazeiKbn == "012"){ //1:税込、11:課込仕入、12:課込売上
					if(! (bunriKbn == "0" || bunriKbn == "1" || bunriKbn == "2")){
						bunriKbn = "0";
					}
				}
				dialogRetBunriKbn.val(bunriKbn);
			}
			if ("dialogRetKobetsuKbn" in window)	{
				dialogRetKobetsuKbn.val(kobetsuKbn);
			}
			if ("dialogRetShouhizeitaishouKamokuEnableInputFlg" in window)	{
				if( null != dialogRetShouhizeitaishouKamokuEnableInputFlg ){
					dialogRetShouhizeitaishouKamokuEnableInputFlg.val(shouhizeitaishouKamokuEnableInputFlg);
				}
			}
			if ("dialogRetTaikakingakuNyuuryokuFlg" in window)	{
				dialogRetTaikakingakuNyuuryokuFlg.val(taikakingakuNyuuryokuFlg);
			}
			if ("dialogRetGaikaFlg" in window)	{
				dialogRetGaikaFlg.val(gaikaFlg);
			}
			if ("dialogRetShoriGroup" in window)	{
				dialogRetShoriGroup.val(shoriGroup);
			}
			if ("dialogCallbackFindKamokuMaster" in window){
				dialogCallbackFindKamokuMaster();
			}

		}
	});
}

/**
 * 【財務拠点入力専用：××設定画面で登録した科目セキュリティの範囲内】勘定科目選択ボタン押下時Function
 * 'dialog'id内に勘定科目選択画面をロードする。
 *
 * ダイアログで取引を選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * ◆当function呼び出し前に、呼び出し元画面の選択結果反映先inputタグのクエリオブジェクトをセットする。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名		:項目名
 * 		----------------------------------------
 * 		dialogRetKamokuCd		:科目コード
 * 		dialogRetKamokuName		:科目名

 * ◆当function呼び出し前に、取引選択時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackKanjyouKamokuSentaku
 */
function commonZaimuKyotenNyuryokuKamokuSentaku(kijunbi, from, to, denpyouKbn, patternNo) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	if (denpyouKbn == null || denpyouKbn == ""){
		denpyouKbn = $("input[name=denpyouKbn]").val();
	}
	if (patternNo == null || patternNo == ""){
		patternNo = $("input[name=patternNo]").val();
	}
	var params = {
		denpyouKbn: denpyouKbn,
		patternNo: patternNo
	};
	if(kijunbi != null){
		params["kijunbi"] =  kijunbi;
	}else if(from != null && to != null){
		params["from"] =  from;
		params["to"] =  to;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "勘定科目選択",
		buttons: {閉じる: function() {$(this).dialog("close");}},
		close: function() {
			$("#dialog").children().remove();
		}
	})
	.load("zaimu_kyoten_nyuryoku_kamoku_sentaku?" + $.param(params));
}
/**
 * commonZaimuKyotenNyuryokuKamokuSentakuの基準日指定版
 * @param denpyouKbn
 * @param patternNo
 * @returns
 */
function commonZaimuKyotenNyuryokuKamokuSentakuKensaku(denpyouKbn, patternNo) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	if (denpyouKbn == null || denpyouKbn == ""){
		denpyouKbn = $("input[name=denpyouKbn]").val();
	}
	if (patternNo == null || patternNo == ""){
		patternNo = $("input[name=patternNo]").val();
	}
	var params = {
		denpyouKbn: denpyouKbn,
		patternNo: patternNo
	};

	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "勘定科目選択",
		buttons: {閉じる: function() {$(this).dialog("close");}},
		close: function() {
			$("#dialog").children().remove();
		}
	})
	.load("zaimu_kyoten_nyuryoku_kamoku_sentaku_kensaku?" + $.param(params));
}

/**
 * 【財務拠点入力専用：××設定画面で登録した科目セキュリティの範囲内】勘定科目コードロストフォーカス時Function
 */
function commonZaimuKyotenNyuryokuKamokuCdLostFocus(inputCode, inputName, title, kijunbi, from, to, denpyouKbn, patternNo) {
	var code = inputCode.val();
	if(code == "" || code == null) {
		inputName.val(null);
		if ("dialogCallbackKanjyouKamokuSentaku" in window)	{
			dialogCallbackKanjyouKamokuSentaku();
		}
		return;
	}
	if (denpyouKbn == null || denpyouKbn == ""){
		denpyouKbn = $("input[name=denpyouKbn]").val();
	}
	if (patternNo == null || patternNo == ""){
		patternNo = $("input[name=patternNo]").val();
	}
	var params = {
		kamokuCd: code,
		denpyouKbn: denpyouKbn,
		patternNo: patternNo
	};
	if(kijunbi != null){
		params["kijunbi"] =  kijunbi;
	}else if(from != null && to != null){
		params["from"] =  from;
		params["to"] =  to;
	}
	$.ajax({
		type : "GET",
		url : "zaimu_kyoten_nyuryoku_kamoku_name_get",
		data : $.param(params),
		dataType : 'text',
		success : function(response) {
			inputName.val(response);
			if (response == "" && title != "") {
				alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
			}
			if ("dialogCallbackKanjyouKamokuSentaku" in window)	{
				dialogCallbackKanjyouKamokuSentaku();
			}
		}
	});
}

/**
 * 【財務拠点入力専用：××設定画面で登録した部門セキュリティの範囲内】負担部門検索ボタン押下時Function
 * 'dialog'id内に負担部門選択画面をロードする。
 *
 * ダイアログで負担部門を選択して閉じた場合、呼び出し元画面に反映する。
 *
 * 以下、呼び出し時のルール。
 *
 * ◆当function呼び出し前に、呼び出し元画面の選択結果反映先inputタグのクエリオブジェクトをセットする。
 * 　反映不要な項目については、セット不要（必要な項目分のグローバル変数のみセットすればよい）。
 * 		グローバル変数名		項目名
 * 		----------------------------------------
 * 		dialogRetFutanBumonCd	負担部門コード
 * 		dialogRetFutanBumonName	負担部門名

 * ◆当function呼び出し前に、取引選択時にコールバックするfunctionをセットする。
 * 　コールバック処理不要の場合、セット不要。
 * 		dialogCallbackFutanBumonSentaku
 */
function commonZaimuKyotenNyuryokuFutanBumonSentaku(kijunbi, from, to) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	var denpyouKbn = $("input[name=denpyouKbn]").val();
	var patternNo = $("input[name=patternNo]").val();
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "負担部門選択",
		buttons: {閉じる: function() {$(this).dialog("close");}},
		close: function() {
			$("#dialog").children().remove();
		}
	});

	var params = {
		denpyouKbn: denpyouKbn,
		patternNo: patternNo
	};
	if(kijunbi != null){
		params["kijunbi"] =  kijunbi;
	}else if(from != null && to != null){
		params["from"] =  from;
		params["to"] =  to;
	}
	$("#dialog").load("zaimu_kyoten_nyuryoku_futan_bumon_sentaku?" + $.param(params));
	return;
}
/**
 * commonZaimuKyotenNyuryokuFutanBumonSentakuの基準日指定版
 * @param denpyouKbn
 * @param patternNo
 * @returns
 */
function commonZaimuKyotenNyuryokuFutanBumonSentakuKensaku(denpyouKbn, patternNo) {
	var width = "800";
	if(windowSizeChk()) {
		width = $(window).width() * 0.9;
	}
	$("#dialog")
	.dialog({
		modal: true,
		width: width,
		height: "520",
		title: "負担部門選択",
		buttons: {閉じる: function() {$(this).dialog("close");}},
		close: function() {
			$("#dialog").children().remove();
		}
	});

	var params = {
		denpyouKbn: denpyouKbn,
		patternNo: patternNo,
	};

	$("#dialog").load("zaimu_kyoten_nyuryoku_futan_bumon_sentaku_kensaku?" + $.param(params));
	return;
}

/**
 * 【財務拠点入力専用：××設定画面で登録した部門セキュリティの範囲内】負担部門コードロストフォーカス時Function
 */
function commonZaimuKyotenNyuryokuFutanBumonCdLostFocus(inputCode, inputName, title, kijunbi, from, to, denpyouKbn, patternNo) {
	var code = inputCode.val();
	if(code == "" || code == null) {
		inputName.val(null);
		return;
	}
	if (denpyouKbn == null || denpyouKbn == ""){
		denpyouKbn = $("input[name=denpyouKbn]").val();
	}
	if (patternNo == null || patternNo == ""){
		patternNo = $("input[name=patternNo]").val();
	}

	var params = {
		futanBumonCd: code,
		denpyouKbn: denpyouKbn,
		patternNo: patternNo
	};
	if(kijunbi != null){
		params["kijunbi"] =  kijunbi;
	}else if(from != null && to != null){
		params["from"] =  from;
		params["to"] =  to;
	}
	$.ajax({
		type : "GET",
		url : "zaimu_kyoten_nyuryoku_futan_bumon_name_get",
		data : $.param(params),
		dataType : 'json',
		success : function(response) {
			var shiwakeEnable = (inputShiireKbn !== undefined && inputShiireKbn != null) ? inputShiireKbn.prop("disabled"): true;
			inputName.val(response.futanBumonName);
			if(response.shiireKbn != null && response.shiireKbn.toString() != "" && response.shiireKbn.toString() != "0" && !shiwakeEnable){ // 0==""はtrue、"0"==""はfalse
				inputShiireKbn.val(response.shiireKbn);
			}
			if (response == "" && title != "") {
				alert(title.replace("*","").replace("\t", "").replace("\r", "").replace("\n", "") + "が不正です。");
			}
			if ("dialogCallbackFutanBumonSentaku" in window)	{
				dialogCallbackFutanBumonSentaku();
			}
		}
	});
}

/*
 * 値が入力されている行を先頭方向に集める
 */
function gatherTbodyUpside(tbody, name){
	var length = tbody.find("tr").length;
	for(var ii = 0 ; ii < length ; ii++){
		var tr = tbody.find("tr").eq(ii);
		if( $(tr).find("input[name=" + name + "]").val() == "" ){
			for(var i = ii + 1 ; i < length ; i++ ){
				var compTr = $(tr).closest("tbody").find("tr:eq(" + i + ")");
				if( compTr.find("input[name=" + name + "]").val() == "" ){
					//要素に値が設定されていなければ次要素へ
					continue;
				}else{
					//行を入れ替える
					$(tr).before(compTr);
					break;
				}
			}
		}
	}
}

/**
 * 幣種からレートを取得する
 */
function commonHeishuRateLoad(heishuCd, denpyouDate){
	if( heishuCd == null || heishuCd == "" ){
		return;
	}

	// 伝票日付が入力されていることは呼出元で確認済みなのでチェックしない

	var params = {
			denpyouDate: denpyouDate,
			heishuCd: heishuCd
		};
	$.ajax({
		type : "GET",
		url : "zaimu_kyoten_nyuuryoku_heishu_rate_get",
		data : $.param(params),
		dataType : 'text',
		success : function(response) {
			if (response == "") {
				return;
			}
			dialogRetRate.val(response);

			if ("dialogCallbackHeishuRateLoad" in window)	{
				dialogCallbackHeishuRateLoad();
			}
		}
	});

}

// ファイルサイズチェック関数本体
function fileSizeCheck(input)
{
    let totalSize = 0;
    $("input[type='file']").each(function () {
        for (let i = 0; this.files != null && i < this.files.length; i++) {
            totalSize += this.files[i] != null ? this.files[i].size : 0;
        }
    });
    // console.log(totalSize); // デバッグ用
    if (totalSize >= 20 * 1024 * 1024) { //合計ファイルサイズが20MBを超えるなら　20230131 20MB超過→20MB以上 に変更
        alert("1回のアップロードの合計サイズが制限容量(20MB)を超過しています。");
        $(input).val("");//そのファイルをクリアする
    }
}

//ウィンドウリサイズ時処理
$(window).resize(function(){
	resizeTitlePadding();
})
//画面ロード時処理
$(window).load(function(){
	resizeTitlePadding();
})
//タイトル記載部表示位置調整処理
function resizeTitlePadding() {
	if($('#header').length && $("#content").length){
		//メニュー一覧表示用ボタンが出て入ればスマホ表示とみなす
		if($(".btn-navbar").is(':hidden')){
			//ヘッダ部長さ分だけマージン確保
			$("#content").css("padding-top",$('#header').css('height'));
		}else{
			//スマホ表示は別要素で上下間隔が保たれるが、一応5px空けておく
			$("#content").css("padding-top","5px");
		}
	}
}

/** 税区分周り。消費税区分及び仕入税額按分はこの内部で取得する。
* 伝票IDと処理グループは値、課税区分以降の引数はコントロールを指定。 */
function setShouhizeiControls(denpyouId, shoriGroup, kazeiKbn, zeiritsu, bunriKbn, shiireKbn) {
    // 0. kazeiKbn, zeiritsu, bunriKbn, shiireKbnの各selectタグについて、全選択肢を非表示・使用可とする
    let kazeiKbnName = "select[name="+kazeiKbn.attr("name")+"]";
    let zeiritsuName = "select[name="+zeiritsu.attr("name")+"]";
    let bunriKbnName = "select[name="+bunriKbn.attr("name")+"]";
    let shiireKbnName = "select[name="+shiireKbn.attr("name")+"]";
    $(kazeiKbnName + ' option, ' + zeiritsuName + ' option, ' + bunriKbnName + ' option, ' + shiireKbnName + ' option').hide();
    $(kazeiKbnName + ', ' + zeiritsuName + ', ' + bunriKbnName + ', ' + shiireKbnName).prop('disabled', false);
    
    // 0-1. 消費税区分・仕入税額按分の取得
    let shouhizeiKbnInput = $("<input>", { type: 'hidden' });
    let shiireZeigakuAnbunInput = $("<input>", { type: 'hidden' });
    getShouhizeiKbnAndShiireZeigakuAnbun(denpyouId, shouhizeiKbnInput, shiireZeigakuAnbunInput);
    let shouhizeiKbn = shouhizeiKbnInput.val();
    let shiireZeigakuAnbun = shiireZeigakuAnbunInput.val();
    
    // 1. kazeiKbn
    // 1-1. shoriGroupが3～6,9,10の中に含まれているならば、kazeiKbnのvalue=000, 001, 003, 004の選択肢を表示
    if (['3', '4', '5', '6', '9', '10'].includes(shoriGroup.toString())) {
        $(kazeiKbnName + ' option[value=000], ' + kazeiKbnName + ' option[value=001], ' + kazeiKbnName + ' option[value=003], ' + kazeiKbnName + ' option[value=004]').show();
        // 1-1-1. shouhizeiKbnが1,2ならばkazeiKbnのvalue=002の選択肢を追加で表示
        if (['1', '2'].includes(shouhizeiKbn.toString())) {
            $(kazeiKbnName + ' option[value=002]').show();
        }
    }
    // 1-2. shoriGroupが2,7,8ならば、kazeiKbnのvalue=000, 003, 011, 012, 041, 042の選択肢を表示
    else if (['2', '7', '8'].includes(shoriGroup.toString())) {
        $(kazeiKbnName + ' option[value=000], ' + kazeiKbnName + ' option[value=003], ' + kazeiKbnName + ' option[value=011], ' + kazeiKbnName + ' option[value=012], ' + kazeiKbnName + ' option[value=041], ' + kazeiKbnName + ' option[value=042]').show();
        // 1-2-1. shouhizeiKbnが1,2ならばkazeiKbnのvalue=013, 014の選択肢を追加で表示
        if (['1', '2'].includes(shouhizeiKbn.toString())) {
            $(kazeiKbnName + ' option[value=013], ' + kazeiKbnName + ' option[value=014]').show();
        }
        // 1-3. shoriGroupが7ならば、kazeiKbnのvalue=004も追加で表示
        if (shoriGroup == 7) {
            $(kazeiKbnName + ' option[value=004]').show();
        }
    }
    // 1-4. 上記以外の処理グループならば、kazeiKbnはvalue=100の選択肢のみを表示し、固定で選択・使用不可
    else {
        $(kazeiKbnName + ' option[value=100]').show().prop('selected', true);
        $(kazeiKbnName).prop('disabled', true);
    }
    
	// 2. zeiritsu
	// 2-1. kazeiKbnが001, 002, 011, 012, 013, 014か、shoriGroupが21, 22ならば全てのzeiritsuを表示
	if (['001', '002', '011', '012', '013', '014'].includes(kazeiKbn.val()) || ['21', '22'].includes(shoriGroup.toString())) {
        $(zeiritsuName + ' option').show();
        //対象外→税込に切り替え等で税率がNULLの場合は初期値にリストのはじめの値を表示させる
        if($(zeiritsuName).val() == null){
	        $(zeiritsuName + ' option[value="'+ $(zeiritsuName + ' option').first().val() + '"]').prop('selected',true);
		}
    }
    // 2-2. 上記以外なら使用不可
    else {
        $(zeiritsuName).prop('disabled', true);
    }

    // 3. shouhizeiKbnが1,2の時、kazeiKbnが002,013,014ならば、bunriKbnはvalue=0の選択肢で固定・使用不可
    if (['1', '2'].includes(shouhizeiKbn) && ['001', '011', '012', '002', '013', '014'].includes(kazeiKbn.val())) {
        if (['002', '013', '014'].includes(kazeiKbn.val())) {
            $(bunriKbnName + ' option[value=0]').show().prop('selected', true)
            $(bunriKbnName).prop('disabled', true);
        }
        // 3-1. shouhizeiKbnが1の時、kazeiKbnが001,011,012ならば、bunriKbnは1で固定・使用不可
        else {
            $(bunriKbnName + ' option[value=1]').show();
            $(bunriKbnName).prop('disabled', shouhizeiKbn == 1);
            if(shouhizeiKbn == 1)
            {
				$(bunriKbnName + ' option[value=1]').prop('selected', true);
            // 3-2. shouhizeiKbnが2の時、kazeiKbnが001,011,012ならば、bunriKbnに0,2を追加し、使用可
            }else{
            	$(bunriKbnName + ' option[value=0],' + bunriKbnName + ' option[value=2]').show();
			}
        }
        if(bunriKbn.val() == 9){
			$(bunriKbnName + ' option[value=0]').prop('selected',true);
		}
    }
    // 3-3. 上記以外の時、bunriKbnはvalue=9の選択肢で固定・使用不可
    else {
        $(bunriKbnName + ' option[value=9]').prop('selected', true);
        $(bunriKbnName).prop('disabled', true);
    }

    // 4. shiireZeigakuAnubun==1、かつshoriGroupが2, 5～8, 10の中に含まれており、かつkazeiKbnが001, 002, 011, 013に含まれているならば、value=1,2,3の仕入区分利用可
    if (shiireZeigakuAnbun == 1 && ['2', '5', '6', '7', '8', '10'].includes(shoriGroup.toString()) && ['001', '002', '011', '013'].includes(kazeiKbn.val())) {
        $(shiireKbnName + ' option[value=1], ' + shiireKbnName + ' option[value=2], ' + shiireKbnName + ' option[value=3]').show();
        // 何もセットされていなくて使用可の時は3をデフォに（既に何かセットされているなら上書きリセットはしない）
        if($(shiireKbnName + ' option[value=0]').prop('selected')){
        	$(shiireKbnName + ' option[value=3]').prop('selected', true);
        }
    }
    // 4-1. 上記以外なら仕入区分はvalue=0固定で、使用不可
    else {
        $(shiireKbnName + ' option[value=0]').show().prop('selected', true);
        $(shiireKbnName).prop('disabled', true);
    }
}


/**
 * 消費税区分取得Function
 */
function getShouhizeiKbnAndShiireZeigakuAnbun(denpyouId, shouhizeiKbn, shiireZeigakuAnbun) {
	$.ajax({
		type : "GET",
		url : "shouhizei_kbn_get",
		data : {
			denpyouId: denpyouId
			},
		dataType : 'json',
		async : false,   // callbackなどはややこしさの元なので同期実行させる
		success : function(response) {
			shouhizeiKbn.val(response?.shouhizeiKbn ? response.shouhizeiKbn : 0); // 初期値は仮。0でダメなら修正する
			shiireZeigakuAnbun.val(response?.shiireZeigakuAnbunFlg ? response.shiireZeigakuAnbunFlg : 0); // 初期値は仮。0でダメなら修正する
		}
	});
}

// 各jspでオーバーライドされる空function
function getMeisaiList()
{
	return [];
}

// 旅費・海外旅費用
function getRyohiMeisaiList() {
	let map1 = $.map($("#meisaiList01 tr.meisai"), function(tr, ii) {
		var meisaiInfo = getRyohiMeisaiInfo(ii, 1);
		if (!meisaiInfo['shubetsuCd']) { // 隠れ行ではshubetsuCdがundefinedなので
			return null;
		}
		return meisaiInfo;
	});
	let map2 = $.map($("#meisaiList02 tr.meisai"), function(tr, ii) {
		var meisaiInfo = getRyohiMeisaiInfo(ii, 2);
		if (!meisaiInfo['shubetsuCd']) {
			return null;
		}
		return meisaiInfo;
	});
	let kaigaiMap1 = $.map($("#kaigaiMeisaiList01 tr.meisai"), function(tr, ii) {
		var meisaiInfo = getKaigaiRyohiMeisaiInfo(ii, 1);
		if (!meisaiInfo['shubetsuCd']) {
			return null;
		}
		return meisaiInfo;
	});
	let kaigaiMap2 = $.map($("#kaigaiMeisaiList02 tr.meisai"), function(tr, ii) {
		var meisaiInfo = getKaigaiRyohiMeisaiInfo(ii, 2);
		if (!meisaiInfo['shubetsuCd']) {
			return null;
		}
		return meisaiInfo;
	});
	let kokunaiMap = $.merge(map1 ?? [], map2 ?? []);
	let kaigaiMap = $.merge(kaigaiMap1 ?? [], kaigaiMap2 ?? []);
	return $.merge(kokunaiMap, kaigaiMap);
}

//インボイス関連金額計算
function calcInvoiceKingaku()
{
	// インボイスでの追加分
	let shiharaiKingakuGoukei10PercentTag = $("input[name=shiharaiKingakuGoukei10Percent]");
	let zeinukiKingaku10PercentTag = $("input[name=zeinukiKingaku10Percent]");
	let shouhizeigaku10PercentTag = $("input[name=shouhizeigaku10Percent]");
	let shiharaiKingakuGoukei8PercentTag = $("input[name=shiharaiKingakuGoukei8Percent]");
	let zeinukiKingaku8PercentTag = $("input[name=zeinukiKingaku8Percent]");
	let shouhizeigaku8PercentTag = $("input[name=shouhizeigaku8Percent]");

	let shiharaiKingakuGoukei10Percent = 0;
	let zeinukiKingaku10Percent = 0;
	let shouhizeigaku10Percent = 0;
	let shiharaiKingakuGoukei8Percent = 0;
	let zeinukiKingaku8Percent = 0;
	let shouhizeigaku8Percent = 0;
	
	$.each($.merge(getMeisaiList(), getRyohiMeisaiList()), function(ii, meisaiInfo) {
		//経費系・支払依頼明細と、旅費系明細で一部項目名が異なるのでまとめてくみ取る
		let shiharaiKingaku = (meisaiInfo?.["shiharaiKingaku"])
			? parseInt(meisaiInfo["shiharaiKingaku"].split(',').join(''))
			: (meisaiInfo?.["meisaiKingaku"])
				? parseInt(meisaiInfo["meisaiKingaku"].split(',').join(''))
				: 0;
		let zeinukiKingaku = (meisaiInfo?.["zeinukiKingaku"])
			? parseInt(meisaiInfo["zeinukiKingaku"].split(',').join(''))
			: (meisaiInfo?.["hontaiKingaku"])
				? parseInt(meisaiInfo["hontaiKingaku"].split(',').join(''))
				: 0;
		let shouhizeigaku = (meisaiInfo?.["shouhizeigaku"]) ? parseInt(meisaiInfo["shouhizeigaku"].split(',').join('')) : 0;

		// 税率に応じて分岐
    	// TODO 旧税制の扱い（要望があり次第）　　海外課税区分の考慮（未定　普通は税込税抜系使わないから必要ない）
    	// 海外交通費・日当→課税区分が何であれ加算しない
    	// 国内交通費・日当→課税区分と税率を考慮
    	// その他経費　　　→課税区分と税率を考慮
        if(meisaiInfo?.["zeiritsu"]?.includes("10") || meisaiInfo?.["kaigaiFlgRyohi"] == "1")
		{
			//旅費は明細上に課税区分を持っていない+kaigaiFlgRyohiは旅費明細にしかない　→海外分は加算されない
			let kazeiKbn = meisaiInfo?.["kazeiKbn"];
			if(Number(["001", "002", "011", "012", "013", "014"].includes(kazeiKbn))){
				shiharaiKingakuGoukei10Percent += shiharaiKingaku;
				zeinukiKingaku10Percent += zeinukiKingaku;
				shouhizeigaku10Percent += shouhizeigaku;
			}
		}
		else if(meisaiInfo?.["zeiritsu"]?.includes("8"))
		{
			let kazeiKbn = meisaiInfo?.["kazeiKbn"];
			if(Number(["001", "002", "011", "012", "013", "014"].includes(kazeiKbn))){
				shiharaiKingakuGoukei8Percent += shiharaiKingaku;
				zeinukiKingaku8Percent += zeinukiKingaku;
				shouhizeigaku8Percent += shouhizeigaku;
			}
		}
        // 国内旅費明細の場合
        else if(meisaiInfo?.["kaigaiFlgRyohi"] == "0")
        {
            let kazeiKbn = $("select[name=kazeiKbnRyohi] :selected").val();
            let zeiritsu = Number(["001", "002", "011", "012", "013", "014"].includes(kazeiKbn) 
                    ? $("#zeiritsuRyohi").find("option:selected").val()
                    : 0);
            if(zeiritsu == "8")
            {
                shiharaiKingakuGoukei8Percent += shiharaiKingaku;
                zeinukiKingaku8Percent += zeinukiKingaku;
                shouhizeigaku8Percent += shouhizeigaku;
            }
            else if(zeiritsu == "10") //明確に10指定
            {
                shiharaiKingakuGoukei10Percent += shiharaiKingaku;
                zeinukiKingaku10Percent += zeinukiKingaku;
                shouhizeigaku10Percent += shouhizeigaku;
            }
        }
	});

	// 金額をセット
	// nullなら無視
	shiharaiKingakuGoukei10PercentTag?.putMoney(shiharaiKingakuGoukei10Percent);
	zeinukiKingaku10PercentTag?.putMoney(zeinukiKingaku10Percent);
	shouhizeigaku10PercentTag?.putMoney(shouhizeigaku10Percent);
	shiharaiKingakuGoukei8PercentTag?.putMoney(shiharaiKingakuGoukei8Percent);
	zeinukiKingaku8PercentTag?.putMoney(zeinukiKingaku8Percent);
	shouhizeigaku8PercentTag?.putMoney(shouhizeigaku8Percent);
}

/**
 * 電卓表示Function
 */
function addCalculator(target){
	if(target.hasClass("autoNumericWithCalcBox")){
		target.calculator({
			showOn: 'button',
			buttonImageOnly: true,
			buttonImage: '../../static/img/calculator.png',
			onOpen: function(value, inst) {
				// カンマ削除
				if ("" != value) {
					$(this).val(parseInt(value.replaceAll(",", "")));
				}

				// autoNumericの使用不可
				$(this).autoNumeric('destroy');
			}
			,onClose: function(value, inst) {
			   // autoNumericの使用不可
			   $(this).autoNumeric("init", {aPad: false, vMin:0, vMax:999999999999});

			   // 四捨五入(小数点対応)
			   var valueTemp = Math.round(value);

			   // カンマ追加
			   $(this).val(String(valueTemp).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,")).change();

			   // テキストフォーカス
			   $(this).focus();
			}
		});
	}
}

/**
 * 電卓削除Function
 */
function delCalculator(target){
	if(target.hasClass("autoNumericWithCalcBox")){
		target.removeClass("hasCalculator");
		target.next("img:first").remove();
	}
}