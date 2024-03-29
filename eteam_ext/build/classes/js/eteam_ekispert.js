var ekicom_url = "http://api.ekispert.jp/";
var ekicom_key = "";

/**
 *  駅すぱあと Web サービス
 *  探索条件パーツ
 *  サンプルコード
 *  https://github.com/EkispertWebService/GUI
 *
 *  Version:2016-06-20
 *
 *  Copyright (C) Val Laboratory Corporation. All rights reserved.
 **/

var expGuiCondition = function (pObject, config) {
    // ドキュメントのオブジェクトを格納
    var documentObject = pObject;
    var baseId = pObject.id;

    // Webサービスの設定
    var apiURL = ekicom_url;
    var key = ekicom_key;

    // AGENTのチェック
    var agent = 1;
    var isiPad = navigator.userAgent.match(/iPad/i) != null;
    var isiPhone = navigator.userAgent.match(/iPhone/i) != null;
    var isAndroid_phone = (navigator.userAgent.match(/Android/i) != null && navigator.userAgent.match(/Mobile/i) != null);
    var isAndroid_tablet = (navigator.userAgent.match(/Android/i) != null && navigator.userAgent.match(/Mobile/i) == null);
    if (isiPhone || isAndroid_phone) { agent = 2; }
    if (isiPad || isAndroid_tablet) { agent = 3; }

    /**
    * イベントの設定(IE対応版)
    */
    function addEvent(element, eventName, func) {
        if (element) {
            if (typeof eventName == 'string' && typeof func == 'function') {
                if (element.addEventListener) {
                    element.addEventListener(eventName, func, false);
                } else if (element.attachEvent) {
                    element.attachEvent("on" + eventName, func);
                }
            }
        }
    }

    // 変数郡
    // デフォルト探索条件
    var def_condition_t = "T3221233232319";
    var def_condition_f = "F3321122120";
    var def_condition_a = "A23121141";
    var def_sortType = "ekispert"; // デフォルトソート
    var def_priceType = "oneway"; // 片道運賃がデフォルト
    var def_answerCount = "5"; // 探索結果数のデフォルト
    var checkBoxItemName = "shinkansen:shinkansenNozomi:limitedExpress:localBus:liner:midnightBus"; //チェックボックスに表示する条件
    var checkboxItem = new Array();
    var conditionObject = initCondition();

    /**
     * 探索条件のオブジェクトの初期化
     */
    function initCondition() {
        // 探索条件のオブジェクトを作成
        var tmp_conditionObject = new Object();
        // 回答数
        var conditionId = "answerCount";
        var conditionLabel = "回答数";
        var tmpOption = new Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption);
        // 探索時の表示順設定
        var conditionId = "sortType";
        var conditionLabel = "表示順設定";
        //  var conditionLabel = "探索時の表示順設定";
        //  var tmpOption = new Array("駅すぱあと探索順","料金順","時間順","定期券の料金順","乗換回数順","CO2排出量順","1ヶ月定期券の料金順","3ヶ月定期券の料金順","6ヶ月定期券の料金順");
        var tmpOption = new Array("探索順", "料金順", "時間順", "定期券順", "乗換回数順", "CO2排出量順", "1ヶ月定期順", "3ヶ月定期順", "6ヶ月定期順");
        var tmpValue = new Array("ekispert", "price", "time", "teiki", "transfer", "co2", "teiki1", "teiki3", "teiki6");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 探索時の料金設定
        var conditionId = "priceType";
        //  var conditionLabel = "探索時の料金設定";
        var conditionLabel = "料金設定";
        var tmpOption = new Array("片道", "往復", "定期");
        var tmpValue = new Array("oneway", "round", "teiki");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 飛行機
        var conditionId = "plane";
        var conditionLabel = "飛行機";
        var tmpOption = new Array("気軽に利用", "普通に利用", "極力利用しない", "利用しない");
        var tmpValue = new Array("light", "normal", "bit", "never");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 新幹線
        var conditionId = "shinkansen";
        var conditionLabel = "新幹線";
        var tmpOption = new Array("利用する", "利用しない");
        var tmpValue = new Array("normal", "never");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 新幹線のぞみ
        var conditionId = "shinkansenNozomi";
        var conditionLabel = "新幹線のぞみ";
        var tmpOption = new Array("利用する", "利用しない");
        var tmpValue = new Array("normal", "never");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 寝台列車
        var conditionId = "sleeperTrain";
        var conditionLabel = "寝台列車";
        var tmpOption = new Array("極力利用する", "普通に利用", "利用しない");
        var tmpValue = new Array("possible", "normal", "never");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 有料特急
        var conditionId = "limitedExpress";
        var conditionLabel = "有料特急";
        var tmpOption = new Array("利用する", "利用しない");
        var tmpValue = new Array("normal", "never");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 高速バス
        var conditionId = "highwayBus";
        var conditionLabel = "高速バス";
        var tmpOption = new Array("気軽に利用", "普通に利用", "極力利用しない", "利用しない");
        var tmpValue = new Array("light", "normal", "bit", "never");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 連絡バス
        var conditionId = "connectionBus";
        var conditionLabel = "連絡バス";
        var tmpOption = new Array("気軽に利用", "普通に利用", "極力利用しない", "利用しない");
        var tmpValue = new Array("light", "normal", "bit", "never");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 路線バス
        var conditionId = "localBus";
        var conditionLabel = "路線バス";
        var tmpOption = new Array("利用する", "利用しない");
        var tmpValue = new Array("normal", "never");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 船
        var conditionId = "ship";
        var conditionLabel = "船";
        var tmpOption = new Array("気軽に利用", "普通に利用", "極力利用しない", "利用しない");
        var tmpValue = new Array("light", "normal", "bit", "never");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 有料普通列車
        var conditionId = "liner";
        var conditionLabel = "有料普通列車";
        var tmpOption = new Array("利用する", "利用しない");
        var tmpValue = new Array("normal", "never");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 駅間徒歩
        var conditionId = "walk";
        var conditionLabel = "駅間徒歩";
        var tmpOption = new Array("気にならない", "少し気になる", "利用しない");
        var tmpValue = new Array("normal", "little", "never");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 深夜急行バス
        var conditionId = "midnightBus";
        var conditionLabel = "深夜急行バス";
        var tmpOption = new Array("利用する", "利用しない");
        var tmpValue = new Array("normal", "never");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 特急料金初期値
        var conditionId = "surchargeKind";
        var conditionLabel = "特急料金初期値";
        var tmpOption = new Array("自由席", "指定席", "グリーン");
        var tmpValue = new Array("free", "reserved", "green");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 定期種別初期値
        var conditionId = "teikiKind";
        var conditionLabel = "定期種別初期値";
        var tmpOption = new Array("通勤", "学割（高校）", "学割");
        var tmpValue = new Array("bussiness", "highSchool", "university");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // JR季節料金
        var conditionId = "JRSeasonalRate";
        var conditionLabel = "JR季節料金";
        //  var tmpOption = new Array("繁忙期・閑散期の季節料金を考慮する","無視する");
        var tmpOption = new Array("繁忙期・閑散期を考慮", "無視する");
        var tmpValue = new Array("true", "false");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 学割乗車券
        var conditionId = "studentDiscount";
        var conditionLabel = "学割乗車券";
        var tmpOption = new Array("計算する", "計算しない");
        var tmpValue = new Array("true", "false");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        //20220217 追加 サンプルコード：https://github.com/EkispertWebService/GUI Version:2020-10-12
        // EX予約/スマートEX
        var conditionId = "JRReservation";
        var conditionLabel = "EX予約/スマートEX";
        var tmpOption = new Array("適用しない","ＥＸ予約", "ＥＸ予約(ｅ特急券)", "ＥＸ予約(ＥＸ早特)", "ＥＸ予約(ＥＸ早特２１)", "ＥＸ予約(ＥＸグリーン早特)", "スマートＥＸ", "スマートＥＸ(ＥＸ早特)", "スマートＥＸ(ＥＸ早特２１)", "スマートＥＸ(ＥＸグリーン早特)");
        var tmpValue = new Array("none", "exYoyaku", "exETokkyu", "exHayatoku", "exHayatoku21", "exGreenHayatoku", "smartEx", "smartExHayatoku", "smartExHayatoku21", "smartExGreenHayatoku");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 新幹線eチケット
        var conditionId = "shinkansenETicket";
        var conditionLabel = "新幹線eチケット";
        var tmpOption = new Array("適用しない","新幹線ｅチケット");
        var tmpValue = new Array("none", "eTicket");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 航空運賃の指定
        //var conditionId = "airFare";
        //var conditionLabel = "航空運賃の指定";
        //var tmpOption = new Array("常に普通運賃を採用","特定便割引を極力採用");
        //var tmpValue  = new Array("normal","tokuwari");
        //tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel,tmpOption,tmpValue);
        // 航空保険特別料金
        var conditionId = "includeInsurance";
        var conditionLabel = "航空保険特別料金";
        var tmpOption = new Array("運賃に含む", "運賃に含まない");
        var tmpValue = new Array("true", "false");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 乗車券計算のシステム
        var conditionId = "ticketSystemType";
        //  var conditionLabel = "乗車券計算のシステム";
        var conditionLabel = "乗車券計算";
        //  var tmpOption = new Array("普通乗車券として計算","ICカード乗車券として計算");
        var tmpOption = new Array("普通乗車券", "ICカード乗車券");
        var tmpValue = new Array("normal", "ic");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // ２区間定期の利用
        var conditionId = "nikukanteiki";
        var conditionLabel = "２区間定期の利用";
        var tmpOption = new Array("利用する", "利用しない");
        var tmpValue = new Array("true", "false");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // JR路線
        var conditionId = "useJR";
        var conditionLabel = "JR路線";
        var tmpOption = new Array("気軽に利用", "普通に利用", "極力利用しない");
        var tmpValue = new Array("light", "normal", "bit");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 乗換え
        var conditionId = "transfer";
        var conditionLabel = "乗換え";
        var tmpOption = new Array("気にならない", "少し気になる", "利用しない");
        var tmpValue = new Array("normal", "little", "never");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 特急始発駅
        var conditionId = "expressStartingStation";
        var conditionLabel = "特急始発駅";
        var tmpOption = new Array("なるべく利用", "普通に利用");
        var tmpValue = new Array("possible", "normal");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 出発駅乗車
        var conditionId = "waitAverageTime";
        var conditionLabel = "出発駅乗車";
        //  var tmpOption = new Array("平均待ち時間を利用する","待ち時間なし");
        var tmpOption = new Array("平均待ち時間", "待ち時間なし");
        var tmpValue = new Array("true", "false");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 路線バスのみ探索
        var conditionId = "localBusOnly";
        var conditionLabel = "路線バスのみ探索";
        var tmpOption = new Array("する", "しない");
        var tmpValue = new Array("true", "false");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 路線名あいまい指定
        //  var conditionId = "fuzzyLine";
        //  var conditionLabel = "路線名あいまい指定";
        //  var tmpOption = new Array("あいまいに行う","厳格に行う");
        //  var tmpValue  = new Array("true","false");
        //  tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel,tmpOption,tmpValue);
        // 乗換え時間
        var conditionId = "transferTime";
        var conditionLabel = "乗換え時間";
        //  var tmpOption = new Array("駅すぱあとの既定値","既定値より少し余裕をみる","既定値より余裕をみる","既定値より短い時間にする");
        var tmpOption = new Array("既定値", "少し余裕をみる", "余裕をみる", "短い時間");
        var tmpValue = new Array("normal", "moreMargin", "mostMargin", "lessMargin");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        // 経由駅指定の継承
        //  var conditionId = "entryPathBehavior";
        //  var conditionLabel = "経由駅指定の継承";
        //  var tmpOption = new Array("する","しない");
        //  var tmpValue  = new Array("true","false");
        //  tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel,tmpOption,tmpValue);
        // 優先する乗車券の順序
        var conditionId = "preferredTicketOrder";
        //  var conditionLabel = "優先する乗車券の順序";
        var conditionLabel = "優先する乗車券";
        //var tmpOption = new Array("指定なし", "普通乗車券を優先する", "ＩＣカード乗車券を優先する", "安い乗車券を優先する");
        var tmpOption = new Array("指定なし", "安い乗車券", "ＩＣカード乗車券", "普通乗車券");
        var tmpValue = new Array("none", "cheap", "ic", "normal");
        tmp_conditionObject[conditionId.toLowerCase()] = addCondition(conditionLabel, tmpOption, tmpValue);
        return tmp_conditionObject;
    }

    /**
    * 探索条件オブジェクト追加
    */
    function addCondition(name, option, value) {
        var tmpCondition = new Object();
        tmpCondition.name = name;
        tmpCondition.option = option;
        if (typeof value != 'undefined') {
            tmpCondition.value = value;
        } else {
            tmpCondition.value = option;
        }
        // デフォルトは表示
        tmpCondition.visible = true;
        return tmpCondition;
    }

    /**
    * 探索条件の設置
    */
    function dispCondition() {
        // HTML本体
        var buffer;
        if (agent == 1) {
            buffer = '<div class="expGuiCondition expGuiConditionPc">';
        } else if (agent == 2) {
            buffer = '<div class="expGuiCondition expGuiConditionPhone">';
        } else if (agent == 3) {
            buffer = '<div class="expGuiCondition expGuiConditionTablet">';
        }
        if (agent == 1 || agent == 3) {
            // チェックボックスの設定とデフォルト
            buffer += '<div class="exp_clearfix">';
            buffer += viewConditionSimple(true);
            buffer += viewConditionDetail();
            buffer += "</div>";
        } else if (agent == 2) {
            // セレクトボックス
            buffer += viewConditionPhone();
        }
        buffer += '</div>';
        documentObject.innerHTML = buffer;

        //チェックボックスを設定した条件は非表示にする
        for (var i = 0; i < checkboxItem.length; i++) {
            setConditionView(checkboxItem[i], false);
        }

        // イベントを設定
        addEvent(document.getElementById(baseId + ":conditionOpen"), "click", onEvent);
        var tabCount = 1;
        while (document.getElementById(baseId + ":conditionTab:" + String(tabCount))) {
            addEvent(document.getElementById(baseId + ":conditionTab:" + String(tabCount)), "click", onEvent);
            tabCount++;
        }
        var tabCount = 1;
        while (document.getElementById(baseId + ":conditionSection:" + String(tabCount) + ":open")) {
            addEvent(document.getElementById(baseId + ":conditionSection:" + String(tabCount) + ":open"), "click", onEvent);
            addEvent(document.getElementById(baseId + ":conditionSection:" + String(tabCount) + ":close"), "click", onEvent);
            tabCount++;
        }
        addEvent(document.getElementById(baseId + ":conditionClose"), "click", onEvent);
        // チェックボックスの設定
        for (var i = 0; i < checkboxItem.length; i++) {
            addEvent(document.getElementById(baseId + ':' + checkboxItem[i] + ':checkbox'), "change", onEvent);
        }
        // 連動機能の追加
        setEvent("shinkansen");
        setEvent("shinkansenNozomi");
        setEvent("ticketSystemType");
        setEvent("preferredTicketOrder");
        // デフォルト設定
        resetCondition();
        // 簡易設定のデフォルトも設定
        setSimpleCondition();
    }

    /**
    * 探索条件の設置
    */
    function setEvent(id) {
        id = id.toLowerCase();
        if (agent == 1 || agent == 3) {
            for (var i = 0; i < conditionObject[id].option.length; i++) {
                addEvent(document.getElementById(baseId + ':' + id + ':' + String(i + 1)), "click", onEvent);
            }
        } else if (agent == 2) {
            addEvent(document.getElementById(baseId + ':' + id), "change", onEvent);
        }
    }

    /**
    * 簡易版探索条件の設置
    */
    function dispConditionSimple() {
        // HTML本体
        var buffer;
        if (agent == 1) {
            buffer = '<div class="expGuiCondition expGuiConditionPc">';
        } else if (agent == 2) {
            buffer = '<div class="expGuiCondition expGuiConditionPhone">';
        } else if (agent == 3) {
            buffer = '<div class="expGuiCondition expGuiConditionTablet">';
        }
        buffer += viewConditionSimple(false);
        buffer += viewConditionDetail();
        buffer += '</div>';
        documentObject.innerHTML = buffer;

        // チェックボックスの設定
        for (var i = 0; i < checkboxItem.length; i++) {
            addEvent(document.getElementById(baseId + ':' + checkboxItem[i] + ':checkbox'), "change", onEvent);
        }
        // デフォルト設定
        resetCondition();
        // 簡易設定のデフォルトも設定
        setSimpleCondition();
    }

    /**
     * 無償版用探索条件の設置
     */
    function dispConditionLight() {
        // HTML本体
        var buffer;
        if (agent == 1) {
            buffer = '<div class="expGuiCondition expGuiConditionPc">';
        } else if (agent == 2) {
            buffer = '<div class="expGuiCondition expGuiConditionPhone">';
        } else if (agent == 3) {
            buffer = '<div class="expGuiCondition expGuiConditionTablet">';
        }

        buffer += '<div class="exp_conditionSimple exp_clearfix">';
        buffer += '<div class="exp_title">交通手段</div>';
        buffer += outConditionCheckbox("plane", "normal", "never");
        buffer += outConditionCheckbox("shinkansen", "normal", "never");
        buffer += outConditionCheckbox("limitedExpress", "normal", "never", "特急");
        buffer += outConditionCheckbox("localBus", "normal", "never", "バス");
        buffer += viewConditionDetail();
        buffer += '</div>';
        documentObject.innerHTML = buffer;

        // チェックボックスの設定
        for (var i = 0; i < checkboxItem.length; i++) {
            addEvent(document.getElementById(baseId + ':' + checkboxItem[i] + ':checkbox'), "change", onEvent);
        }
        // デフォルト設定
        resetCondition();
        // 簡易設定のデフォルトも設定
        setSimpleCondition();
    }

    /**
    * 簡易設定のデフォルト設定
    */
    function setSimpleCondition() {
        for (var i = 0; i < checkboxItem.length; i++) {
            document.getElementById(baseId + ':' + checkboxItem[i] + ':checkbox').checked = (getValue(checkboxItem[i]) == document.getElementById(baseId + ':' + checkboxItem[i] + ':checkbox').value ? true : false);
        }
    }

    /**
    * 探索条件簡易
    */
    function viewConditionSimple(detail) {
        var buffer = "";
        if (typeof checkBoxItemName != 'undefined') {
            if (checkBoxItemName != "") {
                buffer += '<div class="exp_conditionSimple exp_clearfix">';
                buffer += '<div class="exp_title">交通手段</div>';
                var checkBoxItemList = checkBoxItemName.split(":");
                for (var i = 0; i < checkBoxItemList.length; i++) {
                    buffer += outConditionCheckbox(checkBoxItemList[i], "normal", "never");
                }
                buffer += '</div>';
            }
        }
        if (detail) {
            buffer += '<div class="exp_conditionOpen" id="' + baseId + ':conditionOpenButton">';
            if (agent == 1) {
                buffer += '<a class="exp_conditionOpenButton" id="' + baseId + ':conditionOpen" href="Javascript:void(0);"><span class="exp_text" id="' + baseId + ':conditionOpen:text">探索詳細条件を設定</span></a>';
            } else if (agent == 3) {
                buffer += '<a class="exp_conditionOpenButton" id="' + baseId + ':conditionOpen" href="Javascript:void(0);">探索詳細条件を設定</a>';
            }
            buffer += '</div>';
        }
        return buffer;
    }

    /**
    * 探索条件詳細
    */
    function viewConditionDetail() {
        var buffer = "";
        buffer += '<div id="' + baseId + ':conditionDetail" class="exp_conditionDetail" style="display:none;">';
        buffer += '<div class="exp_conditionTable exp_clearfix">';
        if (agent == 3) {
            // タブレット用閉じるボタン
            buffer += '<div class="exp_titlebar exp_clearfix">';
            buffer += '探索条件';
            buffer += '<span class="exp_button">';
            buffer += '<a class="exp_conditionClose" id="' + baseId + ':conditionClose" href="Javascript:void(0);">閉じる</a>';
            buffer += '</span>';
            buffer += '</div>';
        }
        // タブ
        buffer += '<div class="exp_header exp_clearfix">';
        var groupList = new Array("表示", "運賃", "交通手段", "ダイヤ経路", "平均経路");
        buffer += '<div class="exp_conditionLeft"></div>';
        for (var i = 0; i < groupList.length; i++) {
            var tabType = "conditionTab";
            if (agent == 3) {
                if (i == 0) { tabType = "conditionTabLeft"; }
                if (i == (groupList.length - 1)) { tabType = "conditionTabRight"; }
            }
            buffer += '<div class="exp_' + tabType + ' exp_conditionTabSelected" id="' + baseId + ':conditionTab:' + String(i + 1) + ':active" style="display:' + (i == 0 ? "block" : "none") + ';">';
            buffer += '<span class="exp_text">' + groupList[i] + '</span>';
            buffer += '</div>';
            buffer += '<div class="exp_' + tabType + ' exp_conditionTabNoSelect" id="' + baseId + ':conditionTab:' + String(i + 1) + ':none" style="display:' + (i != 0 ? "block" : "none") + ';">';
            buffer += '<a id="' + baseId + ':conditionTab:' + String(i + 1) + '" href="Javascript:void(0);">';
            buffer += groupList[i];
            buffer += '</a>';
            buffer += '</div>';
        }
        buffer += '<div class="exp_conditionRight"></div>';
        buffer += '</div>';

        // 探索条件
        buffer += '<div class="exp_conditionList">';
        buffer += '<div id="' + baseId + ':conditionGroup:' + String(1) + '" class="exp_clearfix">';
        // 回答数
        if (agent == 1 || agent == 2) {
            buffer += outConditionSelect("answerCount");
        } else if (agent == 3) {
            buffer += outConditionRadio("answerCount");
        }
        buffer += outSeparator("answerCount");
        // 探索時の表示順設定
        if (agent == 1 || agent == 2) {
            buffer += outConditionSelect("sortType", "whiteSelect");
        } else if (agent == 3) {
            buffer += outConditionRadio("sortType", "whiteSelect");
        }
        buffer += outSeparator("sortType");
        // 探索時の料金設定
        buffer += outConditionRadio("priceType", "greenSelect");
        buffer += '</div>';

        buffer += '<div id="' + baseId + ':conditionGroup:' + String(2) + '" class="exp_clearfix" style="display:none;">';
        // 特急料金初期値
        buffer += outConditionRadio("surchargeKind");
        buffer += outSeparator("surchargeKind");
        // 学割乗車券
        buffer += outConditionRadio("studentDiscount", "whiteSelect");
        buffer += outSeparator("studentDiscount");
        // 定期種別初期値
        buffer += outConditionRadio("teikiKind", "greenSelect");
        buffer += outSeparator("teikiKind");
        //20220217 追加 サンプルコード：https://github.com/EkispertWebService/GUI Version:2020-10-12
        // EX予約/スマートEX
        if (agent == 1 || agent == 2) {
            buffer += outConditionSelect("JRReservation");
        } else if (agent == 3) {
            buffer += outConditionRadio("JRReservation");
        }
        buffer += outSeparator("JRReservation");
        // 新幹線eチケット
        if (agent == 1 || agent == 2) {
            buffer += outConditionSelect("shinkansenETicket");
        } else if (agent == 3) {
            buffer += outConditionRadio("shinkansenETicket");
        }
        buffer += outSeparator("shinkansenETicket");
        // JR季節料金
        buffer += outConditionRadio("JRSeasonalRate", "whiteSelect");
        buffer += outSeparator("JRSeasonalRate");
        // 乗車券計算のシステム
        buffer += outConditionRadio("ticketSystemType", "greenSelect");
        buffer += outSeparator("ticketSystemType");
        // 優先する乗車券の順序
        buffer += outConditionRadio("preferredTicketOrder", "whiteSelect");
        buffer += outSeparator("preferredTicketOrder");
        // ２区間定期の利用
        buffer += outConditionRadio("nikukanteiki", "greenSelect");
        buffer += outSeparator("nikukanteiki");
        // 航空保険特別料金
        buffer += outConditionRadio("includeInsurance", "whiteSelect");
        // 航空運賃の指定
        //  buffer += outConditionRadio("airFare");
        buffer += '</div>';

        buffer += '<div id="' + baseId + ':conditionGroup:' + String(3) + '" class="exp_clearfix" style="display:none;">';
        // 飛行機
        buffer += outConditionRadio("plane");
        buffer += outSeparator("plane");
        // 新幹線
        buffer += outConditionRadio("shinkansen");
        buffer += outSeparator("shinkansen");
        // 新幹線のぞみ
        buffer += outConditionRadio("shinkansenNozomi");
        buffer += outSeparator("shinkansenNozomi");
        // 有料特急
        buffer += outConditionRadio("limitedExpress");
        buffer += outSeparator("limitedExpress");
        // 寝台列車
        buffer += outConditionRadio("sleeperTrain");
        buffer += outSeparator("sleeperTrain");
        // 有料普通列車
        buffer += outConditionRadio("liner");
        buffer += outSeparator("liner");
        // 高速バス
        buffer += outConditionRadio("highwayBus");
        buffer += outSeparator("highwayBus");
        // 連絡バス
        buffer += outConditionRadio("connectionBus");
        buffer += outSeparator("connectionBus");
        // 深夜急行バス
        buffer += outConditionRadio("midnightBus");
        buffer += outSeparator("midnightBus");
        // 路線バス
        buffer += outConditionRadio("localBus");
        buffer += outSeparator("localBus");
        // 船
        buffer += outConditionRadio("ship");
        buffer += '</div>';

        buffer += '<div id="' + baseId + ':conditionGroup:' + String(4) + '" class="exp_clearfix" style="display:none;">';
        // 乗換え時間
        if (agent == 1 || agent == 2) {
            buffer += outConditionSelect("transferTime", "whiteSelect");
        } else if (agent == 3) {
            buffer += outConditionRadio("transferTime", "whiteSelect");
        }
        buffer += '</div>';

        buffer += '<div id="' + baseId + ':conditionGroup:' + String(5) + '" class="exp_clearfix" style="display:none;">';
        // 駅間徒歩
        buffer += outConditionRadio("walk", "whiteSelect");
        buffer += outSeparator("walk");
        // JR路線
        buffer += outConditionRadio("useJR", "greenSelect");
        buffer += outSeparator("useJR");
        // 特急始発駅
        buffer += outConditionRadio("expressStartingStation", "whiteSelect");
        buffer += outSeparator("expressStartingStation");
        // 出発駅乗車
        buffer += outConditionRadio("waitAverageTime", "greenSelect");
        buffer += outSeparator("waitAverageTime");
        // 路線バスのみ探索
        buffer += outConditionRadio("localBusOnly", "whiteSelect");
        buffer += outSeparator("localBusOnly");
        // 乗換え
        buffer += outConditionRadio("transfer", "greenSelect");
        buffer += '</div>';
        // 隠しタブ
        buffer += '<div style="display:none;">';
        // 路線名あいまい指定
        //  buffer += outConditionRadio("fuzzyLine");
        // 経由駅指定の継承
        //  buffer += outConditionRadio("entryPathBehavior");
        buffer += '</div>';

        if (agent == 1) {
            // PC用閉じるボタン
            buffer += '<div class="exp_conditionFooter">';
            buffer += '<div class="exp_conditionClose">';
            buffer += '<a class="exp_conditionCloseButton" id="' + baseId + ':conditionClose" href="Javascript:void(0);"><span class="exp_text" id="' + baseId + ':conditionClose:text">閉じる</span></a>';
            buffer += '</div>';
            buffer += '</div>';
        }
        buffer += '</div>';
        buffer += '</div>';
        return buffer;
    }

    /**
    * スマートフォン用探索条件
    */
    function viewConditionPhone() {
        var buffer = "";
        buffer += '<div id="' + baseId + ':conditionDetail" class="exp_conditionDetail">';
        // 交通手段
        buffer += '<div class="exp_conditionSection">';
        buffer += '<div class="exp_title">交通手段</div>';
        buffer += '<div class="exp_conditionCheckList exp_clearfix">';
        buffer += outConditionCheckbox("shinkansen", "normal", "never");
        buffer += outConditionCheckbox("shinkansenNozomi", "normal", "never");
        buffer += outConditionCheckbox("limitedExpress", "normal", "never");
        buffer += outConditionCheckbox("localBus", "normal", "never");
        buffer += outConditionCheckbox("liner", "normal", "never");
        buffer += outConditionCheckbox("midnightBus", "normal", "never");
        buffer += '</div>';
        buffer += '<div class="exp_detailButton exp_clearfix">';
        buffer += '<div id="' + baseId + ':conditionSection:' + String(1) + ':active">';
        buffer += '<a class="exp_visible" id="' + baseId + ':conditionSection:' + String(1) + ':open" href="Javascript:void(0);">';
        buffer += '詳細条件を開く';
        buffer += '</a>';
        buffer += '</div>';
        buffer += '<div id="' + baseId + ':conditionSection:' + String(1) + ':none" style="display:none;">';
        buffer += '<a class="exp_hidden" id="' + baseId + ':conditionSection:' + String(1) + ':close" href="Javascript:void(0);">';
        buffer += '詳細条件を閉じる';
        buffer += '</a>';
        buffer += '</div>';
        buffer += '</div>';
        // 詳細
        buffer += '<div id="' + baseId + ':conditionGroup:' + String(1) + '" class="exp_conditionGroup exp_clearfix" style="display:none;">';
        buffer += '<div class="exp_line exp_clearfix">';
        buffer += '<div class="exp_left"></div><div class="exp_right"></div>';
        buffer += '</div>';
        buffer += outConditionSelect("plane", "whiteSelect"); // 飛行機
        buffer += outConditionSelect("sleeperTrain", "greenSelect"); // 寝台列車
        buffer += outConditionSelect("highwayBus", "whiteSelect"); // 高速バス
        buffer += outConditionSelect("connectionBus", "greenSelect"); // 連絡バス
        buffer += outConditionSelect("ship", "whiteSelect"); // 船
        buffer += '</div>';
        buffer += '</div>';

        // 運賃
        buffer += '<div class="exp_conditionSection">';
        buffer += '<div class="exp_title">運賃</div>';
        buffer += '<div class="exp_conditionGroup exp_clearfix">';
        buffer += outConditionSelect("surchargeKind"); // 特急料金初期値
        buffer += '</div>';
        buffer += '<div class="exp_detailButton exp_clearfix">';
        buffer += '<div id="' + baseId + ':conditionSection:' + String(2) + ':active">';
        buffer += '<a class="exp_visible" id="' + baseId + ':conditionSection:' + String(2) + ':open" href="Javascript:void(0);">';
        buffer += '詳細条件を開く';
        buffer += '</a>';
        buffer += '</div>';
        buffer += '<div id="' + baseId + ':conditionSection:' + String(2) + ':none" style="display:none;">';
        buffer += '<a class="exp_hidden" id="' + baseId + ':conditionSection:' + String(2) + ':close" href="Javascript:void(0);">';
        buffer += '詳細条件を閉じる';
        buffer += '</a>';
        buffer += '</div>';
        buffer += '</div>';
        // 詳細
        buffer += '<div id="' + baseId + ':conditionGroup:' + String(2) + '" class="exp_conditionGroup exp_clearfix" style="display:none;">';
        buffer += '<div class="exp_line exp_clearfix">';
        buffer += '<div class="exp_left"></div><div class="exp_right"></div>';
        buffer += '</div>';
        buffer += outConditionSelect("studentDiscount", "whiteSelect"); // 学割乗車券
        buffer += outConditionSelect("teikiKind", "greenSelect"); // 定期種別初期値
        buffer += outConditionSelect("JRSeasonalRate", "whiteSelect"); // JR季節料金
        //20220217 追加 サンプルコード：https://github.com/EkispertWebService/GUI Version:2020-10-12
        buffer += outConditionSelect("JRReservation", "greenSelect"); // EX予約/スマートEX
        buffer += outConditionSelect("shinkansenETicket", "whiteSelect"); // 新幹線eチケット
        buffer += outConditionSelect("ticketSystemType", "greenSelect"); // 乗車券計算のシステム
        buffer += outConditionSelect("preferredTicketOrder", "whiteSelect"); // 優先する乗車券の順序
        buffer += outConditionSelect("nikukanteiki", "greenSelect"); // ２区間定期の利用
        buffer += outConditionSelect("includeInsurance", "whiteSelect"); // 航空保険特別料金
        //  buffer += outConditionSelect("airFare");// 航空運賃の指定
        buffer += '</div>';
        buffer += '</div>';

        //表示
        buffer += '<div class="exp_conditionSection">';
        buffer += '<div class="exp_title">表示</div>';
        // 回答数
        buffer += '<div class="exp_conditionGroup exp_clearfix">';
        buffer += outConditionSelect("answerCount");
        buffer += '</div>';
        buffer += '<div class="exp_detailButton exp_clearfix">';
        buffer += '<div id="' + baseId + ':conditionSection:' + String(3) + ':active">';
        buffer += '<a class="exp_visible" id="' + baseId + ':conditionSection:' + String(3) + ':open" href="Javascript:void(0);">';
        buffer += '詳細条件を開く';
        buffer += '</a>';
        buffer += '</div>';
        buffer += '<div id="' + baseId + ':conditionSection:' + String(3) + ':none" style="display:none;">';
        buffer += '<a class="exp_hidden" id="' + baseId + ':conditionSection:' + String(3) + ':close" href="Javascript:void(0);">';
        buffer += '詳細条件を閉じる';
        buffer += '</a>';
        buffer += '</div>';
        buffer += '</div>';
        // 詳細
        buffer += '<div id="' + baseId + ':conditionGroup:' + String(3) + '" class="exp_conditionGroup exp_clearfix" style="display:none;">';
        buffer += '<div class="exp_line exp_clearfix">';
        buffer += '<div class="exp_left"></div><div class="exp_right"></div>';
        buffer += '</div>';
        buffer += outConditionSelect("sortType", "whiteSelect"); // 探索時の表示順設定
        buffer += outConditionSelect("priceType", "greenSelect"); // 探索時の料金設定
        buffer += '</div>';
        buffer += '</div>';

        // ダイヤ経路
        buffer += '<div class="exp_conditionSection">';
        buffer += '<div class="exp_title">ダイヤ経路</div>';
        buffer += '<div class="exp_detailButton exp_clearfix">';
        buffer += '<div id="' + baseId + ':conditionSection:' + String(4) + ':active">';
        buffer += '<a class="exp_visible" id="' + baseId + ':conditionSection:' + String(4) + ':open" href="Javascript:void(0);">';
        buffer += '詳細条件を開く';
        buffer += '</a>';
        buffer += '</div>';
        buffer += '<div id="' + baseId + ':conditionSection:' + String(4) + ':none" style="display:none;">';
        buffer += '<a class="exp_hidden" id="' + baseId + ':conditionSection:' + String(4) + ':close" href="Javascript:void(0);">';
        buffer += '詳細条件を閉じる';
        buffer += '</a>';
        buffer += '</div>';
        buffer += '</div>';
        // 詳細
        buffer += '<div id="' + baseId + ':conditionGroup:' + String(4) + '" class="exp_conditionGroup exp_clearfix" style="display:none;">';
        buffer += '<div class="exp_line exp_clearfix">';
        buffer += '<div class="exp_left"></div><div class="exp_right"></div>';
        buffer += '</div>';
        buffer += outConditionSelect("transferTime", "whiteSelect"); // 乗換え時間
        buffer += '</div>';
        buffer += '</div>';

        // 平均経路
        buffer += '<div class="exp_conditionSection">';
        buffer += '<div class="exp_title">平均経路</div>';
        buffer += '<div class="exp_detailButton exp_clearfix">';
        buffer += '<div id="' + baseId + ':conditionSection:' + String(5) + ':active">';
        buffer += '<a class="exp_visible" id="' + baseId + ':conditionSection:' + String(5) + ':open" href="Javascript:void(0);">';
        buffer += '詳細条件を開く';
        buffer += '</a>';
        buffer += '</div>';
        buffer += '<div id="' + baseId + ':conditionSection:' + String(5) + ':none" style="display:none;">';
        buffer += '<a class="exp_hidden" id="' + baseId + ':conditionSection:' + String(5) + ':close" href="Javascript:void(0);">';
        buffer += '詳細条件を閉じる';
        buffer += '</a>';
        buffer += '</div>';
        buffer += '</div>';
        // 詳細
        buffer += '<div id="' + baseId + ':conditionGroup:' + String(5) + '" class="exp_conditionGroup exp_clearfix" style="display:none;">';
        buffer += '<div class="exp_line exp_clearfix">';
        buffer += '<div class="exp_left"></div><div class="exp_right"></div>';
        buffer += '</div>';
        buffer += outConditionSelect("walk", "whiteSelect"); // 駅間徒歩
        buffer += outConditionSelect("useJR", "greenSelect"); // JR路線
        buffer += outConditionSelect("expressStartingStation", "whiteSelect"); // 特急始発駅
        buffer += outConditionSelect("waitAverageTime", "greenSelect"); // 出発駅乗車
        buffer += outConditionSelect("localBusOnly", "whiteSelect"); // 路線バスのみ探索
        buffer += outConditionSelect("transfer", "greenSelect"); // 乗換え
        buffer += '</div>';
        buffer += '</div>';

        // 隠しタブ
        buffer += '<div style="display:none;">';
        // 新幹線
        buffer += outConditionSelect("shinkansen");
        // 新幹線のぞみ
        buffer += outConditionSelect("shinkansenNozomi");
        // 有料特急
        buffer += outConditionSelect("limitedExpress");
        // 路線バス
        buffer += outConditionSelect("localBus");
        // 有料普通列車
        buffer += outConditionSelect("liner");
        // 深夜急行バス
        buffer += outConditionSelect("midnightBus");
        // 路線名あいまい指定
        //  buffer += outConditionSelect("fuzzyLine");
        // 経由駅指定の継承
        //  buffer += outConditionSelect("entryPathBehavior");
        buffer += '</div>';

        buffer += '</div>';
        return buffer;
    }

    /**
    * イベントの振り分け
    */
    function onEvent(e) {
        var eventIdList = (e.srcElement) ? e.srcElement.id.split(":") : e.target.id.split(":");
        if (eventIdList.length >= 2) {
            if (eventIdList[1] == "conditionTab" && eventIdList.length == 3) {
                // タブの選択
                var tabCount = 1;
                while (document.getElementById(baseId + ":conditionTab:" + String(tabCount))) {
                    if (tabCount == parseInt(eventIdList[2])) {
                        document.getElementById(baseId + ':conditionGroup:' + String(tabCount)).style.display = "block";
                        document.getElementById(baseId + ':conditionTab:' + String(tabCount) + ':active').style.display = "block";
                        document.getElementById(baseId + ':conditionTab:' + String(tabCount) + ':none').style.display = "none";
                    } else {
                        document.getElementById(baseId + ':conditionGroup:' + String(tabCount)).style.display = "none";
                        document.getElementById(baseId + ':conditionTab:' + String(tabCount) + ':active').style.display = "none";
                        document.getElementById(baseId + ':conditionTab:' + String(tabCount) + ':none').style.display = "block";
                    }
                    tabCount++;
                }
                if(agent == 1){
                    changeDialogHeight(parseInt(eventIdList[2]));
                }
            } else if (eventIdList[1] == "conditionOpen") {
                // 探索条件を開く
                document.getElementById(baseId + ':conditionDetail').style.display = "block";
                if(agent == 1){
                	$("#" + baseId + '\\:conditionDetail').dialog({
                		modal: true,
                		dialogClass: "expGuiConditionPc norikaeShousaiJoukenPc",
                		width: 670,
                		height:350,
               			title: "探索詳細条件",
                	}).dialog('open');

                	//開いていたタブの画面サイズに調整
                    var tabCount = 1;
                    while (document.getElementById(baseId + ":conditionTab:" + String(tabCount))) {
                    	 if((document.getElementById(baseId + ':conditionTab:' + String(tabCount) + ':active').style.display) == "block"){
                    		 break;
                    	 }
                    	 tabCount++;
                    }
                	changeDialogHeight(tabCount);
                }
            } else if (eventIdList[1] == "conditionClose") {
                document.getElementById(baseId + ':conditionDetail').style.display = "none";
                $("#" + baseId + '\\:conditionDetail').dialog('close');
                // 簡易設定のデフォルトも設定
                setSimpleCondition();
            } else if (eventIdList[2] == "checkbox" && eventIdList.length == 3) {
                if (document.getElementById(baseId + ':' + eventIdList[1] + ':checkbox').checked) {
                    // オンの時
                    setValue(eventIdList[1], document.getElementById(baseId + ':' + eventIdList[1] + ':checkbox').value);
                    // 追加連動処理
                    if (eventIdList[1].toLowerCase() == String("shinkansenNozomi").toLowerCase()) {
                        setValue("shinkansen", document.getElementById(baseId + ':' + 'shinkansen:checkbox').value);
                        setSimpleCondition();
                    }
                } else {
                    // オフの時
                    setValue(eventIdList[1], document.getElementById(baseId + ':' + eventIdList[1] + ':checkbox:none').value);
                    // 追加連動処理
                    if (eventIdList[1] == "shinkansen") {
                        setValue("shinkansenNozomi", document.getElementById(baseId + ':' + String('shinkansenNozomi').toLowerCase() + ':checkbox:none').value);
                        setSimpleCondition();
                    }
                }
            } else if (eventIdList[1] == "conditionSection" && eventIdList.length == 4) {
                // スマートフォン用の選択
                if (eventIdList[3] == "open") {
                    // タブを開く
                    document.getElementById(baseId + ':conditionSection:' + eventIdList[2] + ':active').style.display = "none";
                    document.getElementById(baseId + ':conditionSection:' + eventIdList[2] + ':none').style.display = "block";
                    document.getElementById(baseId + ':conditionGroup:' + eventIdList[2]).style.display = "block";
                } else if (eventIdList[3] == "close") {
                    // タブを閉じる
                    document.getElementById(baseId + ':conditionSection:' + eventIdList[2] + ':active').style.display = "block";
                    document.getElementById(baseId + ':conditionSection:' + eventIdList[2] + ':none').style.display = "none";
                    document.getElementById(baseId + ':conditionGroup:' + eventIdList[2]).style.display = "none";
                }
            } else {
                // 探索条件の変更
                if (eventIdList[1].toLowerCase() == String("shinkansen").toLowerCase()) {
                    // 新幹線
                    if (getValue("shinkansen") == "never") {
                        setValue("shinkansenNozomi", "never");
                    }
                } else if (eventIdList[1].toLowerCase() == String("shinkansenNozomi").toLowerCase()) {
                    // 新幹線
                    if (getValue("shinkansenNozomi") == "normal") {
                        setValue("shinkansen", "normal");
                    }
                } else if (eventIdList[1].toLowerCase() == String("ticketSystemType").toLowerCase()) {
                    // 乗車券計算のシステム
                    if (getValue("ticketSystemType") == "normal") {
                        setValue("preferredTicketOrder", "none");
                    }
                } else if (eventIdList[1].toLowerCase() == String("preferredTicketOrder").toLowerCase()) {
                    // 優先する乗車券の順序
                    if (getValue("preferredTicketOrder") != "none") {
                        setValue("ticketSystemType", "ic");
                    }
                } else if (eventIdList[1].toLowerCase() == String("studentDiscount").toLowerCase()) {
                	//20220217 追加 サンプルコード：https://github.com/EkispertWebService/GUI Version:2020-10-12
                    // 学割乗車券とEX予約/スマートEXは排他
                    if (getValue("studentDiscount") == "true" && getValue("JRReservation") != "none") {
                        setValue("studentDiscount", "false");
                        alert("学割乗車券とEX予約/スマートEXを同時に有効にすることはできません。")
                    }
                    // 学割乗車券と新幹線eチケットは排他
                    if (getValue("studentDiscount") == "true" && getValue("shinkansenETicket") != "none") {
                        setValue("studentDiscount", "false");
                        alert("学割乗車券と新幹線eチケットを同時に有効にすることはできません。")
                    }
                } else if (eventIdList[1].toLowerCase() == String("JRReservation").toLowerCase()) {
                    // 学割乗車券とEX予約/スマートEXは排他
                    if (getValue("JRReservation") != "none" && getValue("studentDiscount") == "true") {
                        setValue("JRReservation", "none");
                        alert("学割乗車券とEX予約/スマートEXを同時に有効にすることはできません。")
                    }
                } else if (eventIdList[1].toLowerCase() == String("shinkansenETicket").toLowerCase()) {
                    // 学割乗車券と新幹線eチケットは排他
                    if (getValue("shinkansenETicket") != "none" && getValue("studentDiscount") == "true") {
                        setValue("shinkansenETicket", "none");
                        alert("学割乗車券と新幹線eチケットを同時に有効にすることはできません。")
                    }
                }
            }
        }
    }

    //詳細条件ダイアログサイズ調整
    function changeDialogHeight(id){
    	var hgt = 350;
    	switch(id){
    	case 1  :hgt = 350 ; break;
    	case 2  :hgt = 580 ; break;
    	case 3  :hgt = 440 ; break;
    	case 4  :hgt = 240 ; break;
    	case 5  :hgt = 490 ; break;
    	}
    	$("#" + baseId + '\\:conditionDetail').dialog( { width: 670, height: hgt, position: 'center' } );
    }

    /**
    * セパレータを出力
    */
    function outSeparator(id) {
        id = id.toLowerCase();
        var buffer = "";
        buffer += '<div class="exp_separator" id="' + baseId + ':' + id + ':separator"></div>';
        return buffer;
    }

    /**
    * 探索条件の項目出力
    */
    function outConditionSelect(id, classType) {
        id = id.toLowerCase();
        var buffer = "";
        buffer = '<div id="' + baseId + ':' + id + ':condition" style="display:' + (conditionObject[id].visible ? 'block;' : 'none;') + '">';
        if (typeof classType == 'undefined') {
            buffer += '<dl class="exp_conditionItemList">';
        } else {
            buffer += '<dl class="exp_conditionItemList exp_' + classType + '">';
        }
        buffer += '<dt class="exp_conditionHeader" id="' + baseId + ':' + id + ':title">' + conditionObject[id].name + '</dt>';
        buffer += '<dd class="exp_conditionValue" id="' + baseId + ':' + id + ':value">';
        buffer += '<select id="' + baseId + ':' + id + '">';
        for (var i = 0; i < conditionObject[id].option.length; i++) {
            buffer += '<option value="' + conditionObject[id].value[i] + '">' + conditionObject[id].option[i] + '</option>';
        }
        buffer += '</select>';
        buffer += '</dd>';
        buffer += '</dl>';
        buffer += '</div>';
        return buffer;
    }

    /**
    * 探索条件の項目出力
    */
    function outConditionRadio(id, classType) {
        id = id.toLowerCase();
        var buffer = "";
        buffer = '<div id="' + baseId + ':' + id + ':condition" style="display:' + (conditionObject[id].visible ? 'block;' : 'none;') + '">';
        if (typeof classType == 'undefined') {
            buffer += '<dl class="exp_conditionItemList">';
        } else {
            buffer += '<dl class="exp_conditionItemList exp_' + classType + '">';
        }
        buffer += '<dt class="exp_conditionHeader" id="' + baseId + ':' + id + ':title">' + conditionObject[id].name + '</dt>';
        if (id == "answercount" || id == "sorttype") {
            buffer += '<dd class="exp_conditionValueMulti" id="' + baseId + ':' + id + ':value">';
        } else {
            buffer += '<dd class="exp_conditionValue" id="' + baseId + ':' + id + ':value">';
        }
        buffer += '<div>';
        for (var i = 0; i < conditionObject[id].option.length; i++) {
            // 改行処理
            if (i > 0) {
                if (id == "answerCount" && i % 10 == 0) { buffer += '</div><span class="exp_separator"></span><div>'; }
                if (id == "sortType" && i % 5 == 0) { buffer += '</div><span class="exp_separator"></span><div>'; }
            }
            if (i == 0) {
                buffer += '<span class="exp_conditionItemLeft">';
            } else if ((i + 1) == conditionObject[id].option.length) {
                buffer += '<span class="exp_conditionItemRight">';
            } else {
                buffer += '<span class="exp_conditionItem">';
            }
            buffer += '<input type="radio" id="' + baseId + ':' + id + ':' + String(i + 1) + '" name="' + baseId + ':' + id + '" value="' + conditionObject[id].value[i] + '"><label for="' + baseId + ':' + id + ':' + String(i + 1) + '">' + conditionObject[id].option[i] + '</label></span>';
        }
        buffer += '</div>';
        buffer += '</dd>';
        buffer += '</dl>';
        buffer += '</div>';
        return buffer;
    }

    /**
    * 探索条件の項目出力
    */
    function outConditionCheckbox(id, value, none, label) {
        // 簡易条件のリストに入れる
        id = id.toLowerCase();
        checkboxItem.push(id);
        var buffer = "";
        buffer += '<div id="' + baseId + ':' + id + ':simple" class="exp_item" style="display:' + (conditionObject[id].visible ? 'block;' : 'none;') + '">';
        buffer += '<input type="checkbox" id="' + baseId + ':' + id + ':checkbox" value="' + value + '"><label for="' + baseId + ':' + id + ':checkbox">' + ((typeof label != 'undefined') ? label : conditionObject[id].name) + '</label>';
        if (typeof none != 'undefined') {
            buffer += '<input type="hidden" id="' + baseId + ':' + id + ':checkbox:none" value="' + none + '">';
        }
        buffer += '</div>';
        return buffer;
    }

    /**
    * ソート順の取得
    */
    function getSortType() {
        return getValue("sortType");
    }

    /**
    * 探索結果数の取得
    */
    function getAnswerCount() {
        return getValue("answerCount");
    }

    /**
    * 探索条件文字列の取得
    */
    function getConditionDetail() {
        return fixCondition();
    }

    /**
    * 片道・往復・定期のフラグ取得
    */
    function getPriceType() {
        return getValue("priceType");
    }
    //20220217 追加 サンプルコード：https://github.com/EkispertWebService/GUI Version:2020-10-12
    /**
    * EX予約/スマートEXのインデックスの桁数変換
    */
    function getJRReservation() {
        var selectedName = getValue("JRReservation");
        switch(selectedName) {
            case 'none':
              return [0, 0];
            case 'exYoyaku':
              return [1, 0];
            case 'exETokkyu':
              return [2, 0];
            case 'exHayatoku':
              return [3, 0];
            case 'exHayatoku21':
              return [4, 0];
            case 'exGreenHayatoku':
              return [5, 0];
            case 'smartEx':
              return [0, 1];
            case 'smartExHayatoku':
              return [0, 2];
            case 'smartExHayatoku21':
              return [0, 3];
            case 'smartExGreenHayatoku':
              return [0, 4];
            default:
              return [0, 0];
        }
    }
    /**
     * 新幹線eチケットのインデックスの桁数変換
     */
    function getShinkansenETicket(){
    	var selectedName = getValue("shinkansenETicket");
        switch(selectedName) {
        case 'none':
          return 0;
        case 'eTicket':
          return 1;
        default:
          return 0;
        }
    }

    /**
    * 探索条件をフォームにセットする
    */
    function setCondition(param1, param2, priceType, condition) {
        if (isNaN(param1)) {
            // 単独で指定
            setValue(param1, String(param2));
        } else {
            // 全部指定
            // ヘッダ部分
            setValue("answerCount", String(param1));
            setValue("sortType", String(param2));
            setValue("priceType", String(priceType));
            var conditionList_t, conditionList_f, conditionList_a;
            var condition_split = condition.split(':');
            for (var i = 0; i < condition_split.length; i++) {
                if (condition_split[i].length > 0) {
                    if (condition_split[i].substring(0, 1) == "T") {
                        conditionList_t = condition_split[i].split('');
                    } else if (condition_split[i].substring(0, 1) == "F") {
                        conditionList_f = condition_split[i].split('');
                    } else if (condition_split[i].substring(0, 1) == "A") {
                        conditionList_a = condition_split[i].split('');
                    }
                }
            }
            // 探索条件(T)
            setValue("plane", parseInt(conditionList_t[1]));
            setValue("shinkansen", parseInt(conditionList_t[2]));
            setValue("shinkansenNozomi", parseInt(conditionList_t[3]));
            setValue("sleeperTrain", parseInt(conditionList_t[4]));
            setValue("limitedExpress", parseInt(conditionList_t[5]));
            setValue("highwayBus", parseInt(conditionList_t[6]));
            setValue("connectionBus", parseInt(conditionList_t[7]));
            setValue("localBus", parseInt(conditionList_t[8]));
            setValue("ship", parseInt(conditionList_t[9]));
            setValue("liner", parseInt(conditionList_t[10]));
            setValue("walk", parseInt(conditionList_t[11]));
            setValue("midnightBus", parseInt(conditionList_t[12]));
            // 13:固定
            // 探索条件(F)
            setValue("surchargeKind", parseInt(conditionList_f[1]));
            setValue("teikiKind", parseInt(conditionList_f[2]));
            setValue("JRSeasonalRate", parseInt(conditionList_f[3]));
            setValue("studentDiscount", parseInt(conditionList_f[4]));
            //  setValue("airFare",parseInt(conditionList_f[5]));(固定)
            setValue("includeInsurance", parseInt(conditionList_f[6]));
            setValue("ticketSystemType", parseInt(conditionList_f[7]));
            setValue("nikukanteiki", parseInt(conditionList_f[8]));
            // 9:固定
            setValue("preferredTicketOrder", parseInt(conditionList_f[10]));
            //20220217 追加 サンプルコード：https://github.com/EkispertWebService/GUI Version:2020-10-12
            if (conditionList_f.length >= 12) {
                if ( parseInt(conditionList_f[11]) > 0 ) {
                    setValue("JRReservation", 10 - parseInt(conditionList_f[11]));
                } else if ( parseInt(conditionList_f[12]) > 0 ) {
                    setValue("JRReservation", 10 - ( parseInt(conditionList_f[12]) + 5) );
                } else {
                    setValue("JRReservation", 10);
                }
            } else {
                setValue("JRReservation", 10);
            }
            setValue("shinkansenETicket", parseInt(conditionList_f[13]));
            // 探索条件(A)
            setValue("useJR", parseInt(conditionList_a[1]));
            setValue("transfer", parseInt(conditionList_a[2]));
            setValue("expressStartingStation", parseInt(conditionList_a[3]));
            setValue("waitAverageTime", parseInt(conditionList_a[4]));
            setValue("localBusOnly", parseInt(conditionList_a[5]));
            //  setValue("fuzzyLine",parseInt(conditionList_a[6]));(固定)
            setValue("transferTime", parseInt(conditionList_a[7]));
            //  setValue("entryPathBehavior",parseInt(conditionList_a[8]));(固定)
        }
        setSimpleCondition();
    }

    /**
    * フォームに値をセットする
    */
    function setValue(id, value) {
        var name = id.toLowerCase();
        if (document.getElementById(baseId + ':' + name)) {
            if (typeof document.getElementById(baseId + ':' + name).length != 'undefined') {
                // セレクトボックス
                if (value == "0") {
                    setSelect(name, "none");
                } else if (typeof value == 'number') {
                    setSelectIndex(name, value);
                } else {
                    setSelect(name, value);
                }
                return;
            }
        }
        // ラジオボタン
        if (value == "0") {
            setRadio(name, "none");
        } else if (typeof value == 'number') {
            setRadioIndex(name, value);
        } else {
            setRadio(name, value);
        }
    }

    /**
    * ラジオボタンをインデックスで指定する
    */
    function setRadioIndex(name, value) {
        document.getElementsByName(baseId + ':' + name)[(document.getElementsByName(baseId + ':' + name).length - value)].checked = true;
    }
    /**
    * ラジオボタンを値で指定する
    */
    function setRadio(name, value) {
        for (var i = 0; i < document.getElementsByName(baseId + ':' + name).length; i++) {
            if (document.getElementsByName(baseId + ':' + name)[i].value == String(value)) {
                document.getElementsByName(baseId + ':' + name)[i].checked = true;
            }
        }
    }

    /**
    * セレクトボックスをインデックスで指定する
    */
    function setSelectIndex(name, value) {
        document.getElementById(baseId + ':' + name).selectedIndex = (document.getElementById(baseId + ':' + name).options.length - value);
    }

    /**
    * セレクトボックスを値で指定する
    */
    function setSelect(name, value) {
        for (var i = 0; i < document.getElementById(baseId + ':' + name).options.length; i++) {
            if (document.getElementById(baseId + ':' + name)[i].value == String(value)) {
                document.getElementById(baseId + ':' + name).selectedIndex = i;
                return;
            }
        }
    }
    /**
    * 探索条件の確定
    */
    function fixCondition() {
        var conditionList_t = def_condition_t.split('');
        // 探索条件(T)
        conditionList_t[1] = getValueIndex("plane", parseInt(conditionList_t[1]));
        conditionList_t[2] = getValueIndex("shinkansen", parseInt(conditionList_t[2]));
        conditionList_t[3] = getValueIndex("shinkansenNozomi", parseInt(conditionList_t[3]));
        conditionList_t[4] = getValueIndex("sleeperTrain", parseInt(conditionList_t[4]));
        conditionList_t[5] = getValueIndex("limitedExpress", parseInt(conditionList_t[5]));
        conditionList_t[6] = getValueIndex("highwayBus", parseInt(conditionList_t[6]));
        conditionList_t[7] = getValueIndex("connectionBus", parseInt(conditionList_t[7]));
        conditionList_t[8] = getValueIndex("localBus", parseInt(conditionList_t[8]));
        conditionList_t[9] = getValueIndex("ship", parseInt(conditionList_t[9]));
        conditionList_t[10] = getValueIndex("liner", parseInt(conditionList_t[10]));
        conditionList_t[11] = getValueIndex("walk", parseInt(conditionList_t[11]));
        conditionList_t[12] = getValueIndex("midnightBus", parseInt(conditionList_t[12]));
        // 13:固定
        // 探索条件(F)
        var conditionList_f = def_condition_f.split('');
        conditionList_f[1] = getValueIndex("surchargeKind", parseInt(conditionList_f[1]));
        conditionList_f[2] = getValueIndex("teikiKind", parseInt(conditionList_f[2]));
        conditionList_f[3] = getValueIndex("JRSeasonalRate", parseInt(conditionList_f[3]));
        conditionList_f[4] = getValueIndex("studentDiscount", parseInt(conditionList_f[4]));
        //  conditionList_f[5] = getValueIndex("airFare",parseInt(conditionList_f[5]));
        conditionList_f[6] = getValueIndex("includeInsurance", parseInt(conditionList_f[6]));
        conditionList_f[7] = getValueIndex("ticketSystemType", parseInt(conditionList_f[7]));
        conditionList_f[8] = getValueIndex("nikukanteiki", parseInt(conditionList_f[8]));
        // 9:固定
        conditionList_f[10] = getValueIndex("preferredTicketOrder", parseInt(conditionList_f[10]));
        //20220217 追加 サンプルコード：https://github.com/EkispertWebService/GUI Version:2020-10-12
        conditionList_f[11] = getJRReservation()[0];
        conditionList_f[12] = getJRReservation()[1];
        conditionList_f[13] = getShinkansenETicket();
        // 探索条件(A)
        var conditionList_a = def_condition_a.split('');
        conditionList_a[1] = getValueIndex("useJR", parseInt(conditionList_a[1]));
        conditionList_a[2] = getValueIndex("transfer", parseInt(conditionList_a[2]));
        conditionList_a[3] = getValueIndex("expressStartingStation", parseInt(conditionList_a[3]));
        conditionList_a[4] = getValueIndex("waitAverageTime", parseInt(conditionList_a[4]));
        conditionList_a[5] = getValueIndex("localBusOnly", parseInt(conditionList_a[5]));
        //  conditionList_a[6] = getValueIndex("fuzzyLine",parseInt(conditionList_a[6]));
        conditionList_a[7] = getValueIndex("transferTime", parseInt(conditionList_a[7]));
        //  conditionList_a[8] = getValueIndex("entryPathBehavior",parseInt(conditionList_a[8]));

        // 設定値
        var tmpCondition = conditionList_t.join('') + ":" + conditionList_f.join('') + ":" + conditionList_a.join('') + ":";
        return tmpCondition;
    }

    /**
    * 詳細探索条件のオープン
    */
    function openConditionDetail() {
        document.getElementById(baseId + ':conditionDetail').style.display = "block";
    }

    /**
    * フォームの値を取得する
    */
    function getValue(id) {
        var name = id.toLowerCase();
        if (document.getElementById(baseId + ':' + name)) {
            if (typeof document.getElementById(baseId + ':' + name).length != 'undefined') {
                // セレクトボックス
                return getSelect(name);
            } else {
                // ラジオボタン
                return getRadio(name);
            }
        } else {
            // ラジオボタン
            return getRadio(name);
        }
    }
    /**
    * ラジオボタンの値を取得
    */
    function getRadio(name) {
        for (var i = 0; i < document.getElementsByName(baseId + ':' + name).length; i++) {
            if (document.getElementsByName(baseId + ':' + name)[i].checked == true) {
                return document.getElementsByName(baseId + ':' + name)[i].value;
            }
        }
        return null;
    }
    /**
    * セレクトボックスの値を取得
    */
    function getSelect(name) {
        return document.getElementById(baseId + ':' + name).options.item(document.getElementById(baseId + ':' + name).selectedIndex).value;
    }

    /**
    * フォームのインデックスを取得する
    */
    function getValueIndex(id) {
        var name = id.toLowerCase();
        if (getValue(id) == "none") {
            return 0;
        } else {
            if (document.getElementById(baseId + ':' + name)) {
                if (typeof document.getElementById(baseId + ':' + name).length != 'undefined') {
                    // セレクトボックス
                    return getSelectIndex(name);
                }
            }
            // ラジオボタン
            return getRadioIndex(name);
        }
    }
    /**
    * ラジオボタンのインデックスを取得
    */
    function getRadioIndex(name) {
        var index = document.getElementsByName(baseId + ':' + name).length;
        for (var i = 0; i < document.getElementsByName(baseId + ':' + name).length; i++) {
            if (document.getElementsByName(baseId + ':' + name)[i].checked) {
                return (index - i);
            }
        }
    }
    /**
    * セレクトボックスのインデックスを取得
    */
    function getSelectIndex(name) {
        return (document.getElementById(baseId + ':' + name).options.length - document.getElementById(baseId + ':' + name).selectedIndex)
    }

    /**
    * デフォルトを設定
    */
    function resetCondition() {
        var def_condition = def_condition_t + ":" + def_condition_f + ":" + def_condition_a + ":";
        setCondition(def_answerCount, def_sortType, def_priceType, def_condition);
    }

    /**
    * 環境設定
    */
    function setConfigure(name, value) {
        if (name.toLowerCase() == String("apiURL").toLowerCase()) {
            apiURL = value;
        } else if (name.toLowerCase() == String("key").toLowerCase()) {
            key = value;
        } else if (name.toLowerCase() == String("agent").toLowerCase()) {
            agent = value;
        } else if (name.toLowerCase() == String("conditionDetailButton").toLowerCase()) {
            //詳細設定ボタンを切り替え
            if (String(value).toLowerCase() == "visible") {
                document.getElementById(baseId + ':conditionOpenButton').style.display = "block";
            } else if (String(value).toLowerCase() == "hidden") {
                document.getElementById(baseId + ':conditionOpenButton').style.display = "none";
            }
        } else if (name.toLowerCase() == String("simpleCondition").toLowerCase()) {
            //探索条件を簡易指定にする
            checkBoxItemName = value;
        } else if (String(value).toLowerCase() == "visible") {
            // 探索条件の表示
            setConditionView(name, true);
            if (document.getElementById(baseId + ':' + name.toLowerCase() + ':simple')) {
                document.getElementById(baseId + ':' + name.toLowerCase() + ':simple').style.display = "block";
            }
        } else if (String(value).toLowerCase() == "hidden") {
            // 探索条件の非表示
            setConditionView(name, false);
            if (document.getElementById(baseId + ':' + name.toLowerCase() + ':simple')) {
                document.getElementById(baseId + ':' + name.toLowerCase() + ':simple').style.display = "none";
            }
        } else if (String(name).toLowerCase() == String("ssl").toLowerCase()) {
            if (String(value).toLowerCase() == "true" || String(value).toLowerCase() == "enable" || String(value).toLowerCase() == "enabled") {
                apiURL = apiURL.replace('http://', 'https://');
            } else {
                apiURL = apiURL.replace('https://', 'http://');
            }
        }
    }

    /**
    * 探索条件の表示切り替え
    */
    function setConditionView(name, display) {
        conditionObject[name.toLowerCase()].visible = display;
        if (document.getElementById(baseId + ':' + name.toLowerCase() + ':separator')) {
            document.getElementById(baseId + ':' + name.toLowerCase() + ':separator').style.display = display ? "block" : "none";
        }
        if (document.getElementById(baseId + ':' + name.toLowerCase() + ':condition')) {
            document.getElementById(baseId + ':' + name.toLowerCase() + ':condition').style.display = display ? "block" : "none";
        }
    }

    /**
    * 探索条件を取得
    */
    function getCondition(id) {
        return getValue(id.toLowerCase());
    }

    /**
    * 簡易探索条件を取得
    */
    function getConditionLight(id) {
        if (getValue(id.toLowerCase()) == "normal") {
            return true;
        } else {
            return false;
        }
    }

    // 外部参照可能な関数リスト
    this.dispCondition = dispCondition;
    this.dispConditionSimple = dispConditionSimple;
    this.dispConditionLight = dispConditionLight;
    this.getPriceType = getPriceType;
    this.getConditionDetail = getConditionDetail;
    this.getAnswerCount = getAnswerCount;
    this.getSortType = getSortType;
    this.getCondition = getCondition;
    this.getConditionLight = getConditionLight;
    this.setCondition = setCondition;
    this.openConditionDetail = openConditionDetail;
    this.resetCondition = resetCondition;
    this.setConfigure = setConfigure;

    // 定数リスト
    this.SORT_EKISPERT = "ekispert";
    this.SORT_PRICE = "price";
    this.SORT_TIME = "time";
    this.SORT_TEIKI = "teiki";
    this.SORT_TRANSFER = "transfer";
    this.SORT_CO2 = "co2";
    this.SORT_TEIKI1 = "teiki1";
    this.SORT_TEIKI3 = "teiki3";
    this.SORT_TEIKI6 = "teiki6";
    this.PRICE_ONEWAY = "oneway";
    this.PRICE_ROUND = "round";
    this.PRICE_TEIKI = "teiki";

    this.CONDITON_ANSWERCOUNT = "answerCount";
    this.CONDITON_SORTTYPE = "sortType";
    this.CONDITON_PRICETYPE = "priceType";
    this.CONDITON_PLANE = "plane";
    this.CONDITON_SHINKANSEN = "shinkansen";
    this.CONDITON_SHINKANSENNOZOMI = "shinkansenNozomi";
    this.CONDITON_SLEEPERTRAIN = "sleeperTrain";
    this.CONDITON_LIMITEDEXPRESS = "limitedExpress";
    this.CONDITON_HIGHWAYBUS = "highwayBus";
    this.CONDITON_CONNECTIONBUS = "connectionBus";
    this.CONDITON_LOCALBUS = "localBus";
    this.CONDITON_SHIP = "ship";
    this.CONDITON_LINER = "liner";
    this.CONDITON_WALK = "walk";
    this.CONDITON_MIDNIGHTBUS = "midnightBus";
    this.CONDITON_SURCHARGEKIND = "surchargeKind";
    this.CONDITON_TEIKIKIND = "teikiKind";
    this.CONDITON_JRSEASONALRATE = "JRSeasonalRate";
    this.CONDITON_STUDENTDISCOUNT = "studentDiscount";
    //this.CONDITON_AIRFARE = "airFare";
    this.CONDITON_INCLUDEINSURANCE = "includeInsurance";
    this.CONDITON_TICKETSYSTEMTYPE = "ticketSystemType";
    this.CONDITON_NIKUKANTEIKI = "nikukanteiki";
    this.CONDITON_USEJR = "useJR";
    this.CONDITON_TRANSFER = "transfer";
    this.CONDITON_EXPRESSSTARTINGSTATION = "expressStartingStation";
    this.CONDITON_WAITAVERAGETIME = "waitAverageTime";
    this.CONDITON_LOCALBUSONLY = "localBusOnly";
    //this.CONDITON_FUZZYLINE = "fuzzyLine";
    this.CONDITON_TRANSFERTIME = "transferTime";
    //this.CONDITON_ENTRYPATHBEHAVIOR = "entryPathBehavior";
    this.CONDITON_PREFERREDTICKETORDER = "preferredTicketOrder";

    // 端末制御
    this.AGENT_PC = 1;
    this.AGENT_PHONE = 2;
    this.AGENT_TABLET = 3;
};

/**
 *  駅すぱあと Web サービス
 *  経路表示パーツ
 *  サンプルコード
 *  https://github.com/EkispertWebService/GUI
 *
 *  Version:2018-02-22
 *
 *  Copyright (C) Val Laboratory Corporation. All rights reserved.
 **/

var expGuiCourse = function (pObject, config) {
    // ドキュメントのオブジェクトを格納
    var documentObject = pObject;
    var baseId = pObject.id;

    // Webサービスの設定
    var apiURL = ekicom_url;
    var key = ekicom_key;

    // AGENTのチェック
    var agent = 1;
    var isiPad = navigator.userAgent.match(/iPad/i) != null;
    var isiPhone = navigator.userAgent.match(/iPhone/i) != null;
    var isAndroid_phone = (navigator.userAgent.match(/Android/i) != null && navigator.userAgent.match(/Mobile/i) != null);
    var isAndroid_tablet = (navigator.userAgent.match(/Android/i) != null && navigator.userAgent.match(/Mobile/i) == null);
    if (isiPhone || isAndroid_phone) { agent = 2; }
    if (isiPad || isAndroid_tablet) { agent = 3; }

    /**
    * イベントの設定(IE対応版)
    */
    function addEvent(element, eventName, func) {
        if (element) {
            if (typeof eventName == 'string' && typeof func == 'function') {
                if (element.addEventListener) {
                    element.addEventListener(eventName, func, false);
                } else if (element.attachEvent) {
                    element.attachEvent("on" + eventName, func);
                }
            }
        }
    }

    // 変数郡
    var searchObj; // 探索条件のオブジェクト
    var resultObj; // 探索結果のリクエストオブジェクト
    var result; // 探索結果オブジェクト
    var selectNo = 0; // 表示している探索経路NO
    var resultCount = 0; // 探索結果数
    var viewCourseListFlag = false; // 経路一覧表示
    var priceChangeFlag = true; // 座席種別を変更できるかどうか
    var priceChangeRefreshFlag = false; // 座席種別変更時にリクエストするかどうか
    var priceViewFlag = "oneway"; // 片道・往復・定期の表示切替
    var assignDiaFlag = false; // 前後のダイヤ割り当ての設定
    var courseListFlag = false; // 探索結果の一覧自動オープン
    var callbackFunction; // コールバック関数の設定
    var callBackFunctionBind = new Object();
    var windowFlag = false; // ウィンドウ表示フラグ
    var checkEngineVersion = true; // エンジンバージョン同一チェック
    var courseDisplayAll = false;// 経路一覧・探索結果の表示
    var fromName; // 座標指定等で名称を上書きする際の変数
    var toName; // 座標指定等で名称を上書きする際の変数
    var selectedIndex; // 選択中の経路NOを指定
    var sortCourseList; // ソート用配列
    var sortType; // ソートする基準
    var resultTab = true; // タブを表示の有無を指定(オンにすることでソート可能)
    var resultSearchType; // 平均・ダイヤ探索

    // 最適経路の変数
    var minEkispertIndex;
    var minTimeSummary;
    var minTransferCount;
    var minPriceSummary;
    var minPriceRoundSummary;
    var minTeikiSummary;
    var minTeiki1Summary;
    var minTeiki3Summary;
    var minTeiki6Summary;
    var minTeiki12Summary;
    var minExhaustCO2;

    /**
    * メニューのコールバック
    */
    var callBackObjectStation = new Array();
    var callBackObjectLine = new Array();

    /**
     * 探索結果ウィンドウの表示
     */
    function dispCourseWindow() {
        windowFlag = true;
        dispCourse();
    }

    /**
    * 探索結果の設置
    */
    function dispCourse() {
        var buffer = "";
        // 探索結果の表示
        if (agent == 1) {
            buffer += '<div class="expGuiCourse expGuiCoursePc" id="' + baseId + ':course" style="display:none;">';
        } else if (agent == 2) {
            buffer += '<div class="expGuiCourse expGuiCoursePhone" id="' + baseId + ':course" style="display:none;">';
        } else if (agent == 3) {
            buffer += '<div class="expGuiCourse expGuiCourseTablet" id="' + baseId + ':course" style="display:none;">';
        }
        if (windowFlag) {
            // ポップアップ版
            buffer += '<div id="' + baseId + ':resultPopup" class="exp_resultPopup">';
            // 結果本体
            buffer += '<div class="exp_resultBody">';
            // 閉じるボタン
            buffer += '<div class="exp_header">';
            buffer += '<div class="exp_resultClose">';
            buffer += '<a class="exp_resultCloseButton" id="' + baseId + ':resultClose" href="Javascript:void(0);"><span class="exp_text" id="' + baseId + ':resultClose:text">閉じる</span></a>';
            buffer += '</div>';
            buffer += '</div>';
            // 探索結果の表示
            buffer += '<div class="exp_result" id="' + baseId + ':result"></div>';
            buffer += '</div>';
            buffer += '</div>';
        } else {
            // 探索結果の表示
            buffer += '<div class="exp_result" id="' + baseId + ':result"></div>';
        }
        buffer += '</div>';
        // HTMLへ出力
        documentObject.innerHTML = buffer;
        // イベントの登録
        addEvent(document.getElementById(baseId + ":course"), "click", onEvent);
    }

    /**
    * IE用に配列の検索機能を実装
    */
    function checkArray(arr, target) {
        for (var i = 0; i < arr.length; i++) {
            if (arr[i] === target) { return i; }
        }
        return -1;
    }

    /**
    * 探索実行
    */
    function search(searchObject, param1, param2) {
        // その他パラメータ
        var etcParam = new Array();
        if (typeof searchObject == "string") {
            // コールバック関数の設定
            callbackFunction = param2;
            // 探索オブジェクトを生成
            searchObj = createSearchInterface();
            searchObj.setPriceType(param1);
            // パラメータを解析
            var tmpParamList = searchObject.split('&');
            for (var i = 0; i < tmpParamList.length; i++) {
                var tmpParam = tmpParamList[i].split('=');
                if (tmpParam.length == 2) {
                    switch (tmpParam[0].toLowerCase()) {
                        case "vialist":
                            searchObj.setViaList(tmpParam[1]);
                            break;
                        case "fixedraillist":
                            searchObj.setFixedRailList(tmpParam[1]);
                            break;
                        case "fixedraildirectionlist":
                            searchObj.setFixedRailDirectionList(tmpParam[1]);
                            break;
                        case "date":
                            searchObj.setDate(tmpParam[1]);
                            break;
                        case "time":
                            searchObj.setTime(tmpParam[1]);
                            break;
                        case "searchtype":
                            searchObj.setSearchType(tmpParam[1]);
                            break;
                        case "sort":
                            searchObj.setSort(tmpParam[1]);
                            break;
                        case "answercount":
                            searchObj.setAnswerCount(tmpParam[1]);
                            break;
                        case "searchcount":
                            searchObj.setSearchCount(tmpParam[1]);
                            break;
                        case "conditiondetail":
                            searchObj.setConditionDetail(tmpParam[1]);
                            break;
                        case "corporationbind":
                            searchObj.setCorporationBind(tmpParam[1]);
                            break;
                        case "interruptcorporationlist":
                            searchObj.setInterruptCorporationList(tmpParam[1]);
                            break;
                        case "interruptraillist":
                            searchObj.setInterruptRailList(tmpParam[1]);
                            break;
                        case "resultdetail":
                            searchObj.setResultDetail(tmpParam[1]);
                            break;
                        case "assignroute":
                            searchObj.setAssignRoute(tmpParam[1]);
                            break;
                        case "assigndetailroute":
                            searchObj.setAssignDetailRoute(tmpParam[1]);
                            break;
                        case "assignnikukanteikiindex":
                            searchObj.setAssignNikukanteikiIndex(tmpParam[1]);
                            break;
                        case "coupon":
                            searchObj.setCoupon(tmpParam[1]);
                            break;
                        case "bringAssignmentError":
                            searchObj.setBringAssignmentError(tmpParam[1]);
                            break;
                        default:
                            etcParam.push(tmpParam[0] + "=" + encodeURIComponent(tmpParam[1]));
                            break;
                    }
                }
            }
        } else {
            // 探索オブジェクトを指定
            searchObj = searchObject;
            // コールバック関数の設定
            callbackFunction = param1;
        }
        // 探索オブジェクトを文字列に変換
        var searchWord = "";
        if (typeof searchObj.getViaList() != 'undefined') {
            var tmp_stationList = searchObj.getViaList().split(":");
            for (var i = 0; i < tmp_stationList.length; i++) {
                if (isNaN(tmp_stationList[i])) {
                    if (tmp_stationList[i].indexOf("P-") != 0) {
                        tmp_stationList[i] = encodeURIComponent(tmp_stationList[i]);
                    }
                }
            }
            searchWord += "&viaList=" + tmp_stationList.join(":");
        }
        if (typeof searchObj.getFixedRailList() != 'undefined') {
            searchWord += "&fixedRailList=" + encodeURIComponent(searchObj.getFixedRailList());
        }
        if (typeof searchObj.getFixedRailDirectionList() != 'undefined') {
            searchWord += "&fixedRailDirectionList=" + encodeURIComponent(searchObj.getFixedRailDirectionList());
        }
        if (typeof searchObj.getDate() != 'undefined') {
            searchWord += "&date=" + searchObj.getDate();
        }
        if (typeof searchObj.getTime() != 'undefined') {
            searchWord += "&time=" + searchObj.getTime();
        }
        if (typeof searchObj.getSearchType() != 'undefined') {
            searchWord += "&searchType=" + searchObj.getSearchType();
        }
        if (typeof searchObj.getSort() != 'undefined') {
            searchWord += "&sort=" + searchObj.getSort();
        }
        if (typeof searchObj.getAnswerCount() != 'undefined') {
            searchWord += "&answerCount=" + searchObj.getAnswerCount();
        }
        if (typeof searchObj.getSearchCount() != 'undefined') {
            searchWord += "&searchCount=" + searchObj.getSearchCount();
        }
        if (typeof searchObj.getConditionDetail() != 'undefined') {
            searchWord += "&conditionDetail=" + searchObj.getConditionDetail();
        }
        if (typeof searchObj.getCorporationBind() != 'undefined') {
            searchWord += "&corporationBind=" + encodeURIComponent(searchObj.getCorporationBind());
        }
        if (typeof searchObj.getInterruptCorporationList() != 'undefined') {
            searchWord += "&interruptCorporationList=" + encodeURIComponent(searchObj.getInterruptCorporationList());
        }
        if (typeof searchObj.getInterruptRailList() != 'undefined') {
            searchWord += "&interruptRailList=" + encodeURIComponent(searchObj.getInterruptRailList());
        }
        if (typeof searchObj.getResultDetail() != 'undefined') {
            searchWord += "&resultDetail=" + searchObj.getResultDetail();
        }
        if (typeof searchObj.getAssignRoute() != 'undefined') {
            searchWord += "&assignRoute=" + encodeURIComponent(searchObj.getAssignRoute());
        }
        if (typeof searchObj.getAssignDetailRoute() != 'undefined') {
            searchWord += "&assignDetailRoute=" + encodeURIComponent(searchObj.getAssignDetailRoute());
        }
        if (typeof searchObj.getAssignNikukanteikiIndex() != 'undefined') {
            searchWord += "&assignNikukanteikiIndex=" + searchObj.getAssignNikukanteikiIndex();
        }
        if (typeof searchObj.getCoupon() != 'undefined') {
            searchWord += "&coupon=" + encodeURIComponent(searchObj.getCoupon());
        }
        if (typeof searchObj.getBringAssignmentError() != 'undefined') {
            searchWord += "&bringAssignmentError=" + searchObj.getBringAssignmentError();
        }
        // その他パラメータ追加
        if (etcParam.length > 0) {
            searchWord += "&" + etcParam.join("&");
        }
        // 探索文字列の生成
        var url = apiURL + "v1/json/search/course/extreme?key=" + key + searchWord;
        searchRun(url, searchObj.getPriceType());
    }

    /**
    * 探索結果の編集
    */
    function courseEdit(param, callback) {
        if (resultCount >= 1 && selectNo >= 1) {
            callbackFunction = callback;
            // 探索オブジェクトの特定
            var tmpResult;
            if (resultCount == 1) {
                tmpResult = result.ResultSet.Course;
            } else {
                tmpResult = result.ResultSet.Course[(selectNo - 1)];
            }
            // シリアライズデータを設定
            var url = apiURL + "v1/json/course/edit?key=" + key + "&serializeData=" + encodeURIComponent(tmpResult.SerializeData);
            url += "&" + param;
            // エンジンバージョン同一チェック
            if (!checkEngineVersion) {
                url += "&checkEngineVersion=false";
            }
            // 探索を実行
            reSearch(url, selectNo);
        }
    }

    /**
    * 探索実行本体
    */
    function searchRun(url, tmpPriceFlag) {
        // 金額指定されていた場合はセットする
        if (typeof tmpPriceFlag != 'undefined') {
            priceViewFlag = tmpPriceFlag;
        } else {
            priceViewFlag = "oneway";
        }
        //探索実行中はキャンセル
        if (typeof resultObj != 'undefined') {
            resultObj.abort();
        }
        //ロード中の表示
        if (!document.getElementById(baseId + ':result')) {
            dispCourse();
        }
        document.getElementById(baseId + ':result').innerHTML = '<div class="expLoading"><div class="expText">経路取得中...</div></div>';
        document.getElementById(baseId + ':course').style.display = "block";
        var JSON_object = {};
        if (window.XDomainRequest) {
            // IE用
            resultObj = new XDomainRequest();
            resultObj.onload = function () {
                // OK時の処理
                setResult(resultObj.responseText, callbackFunction);
            };
            resultObj.onerror = function () {
                // エラー時の処理
                document.getElementById(baseId + ':course').style.display = "none";
                if (typeof callbackFunction == 'function') {
                    callbackFunction(false);
                }
            };
        } else {
            resultObj = new XMLHttpRequest();
            resultObj.onreadystatechange = function () {
                var done = 4, ok = 200, fbd = 403;
                if (resultObj.readyState == done && resultObj.status == ok) {
                    // OK時の処理
                    setResult(resultObj.responseText, callbackFunction);
                } else if (resultObj.readyState == done && resultObj.status == fbd) {
                	alert("WEB APIが403エラーを返却しました。管理者に連絡してください。",1);
                } else if (resultObj.readyState == done && resultObj.status != ok) {
                    // エラー時の処理
                    document.getElementById(baseId + ':course').style.display = "none";
                    if (typeof callbackFunction == 'function') {
                        callbackFunction(false);
                    }
                    alert("経路情報が取得できませんでした。入力内容を確認してください。",1);
                }
            };
        }
        var newurl = "ekispert_apicall?callURL=" + encodeURIComponent(url);
        resultObj.open("GET", newurl, true);
        resultObj.overrideMimeType('text/plain; charset=utf-8');
        resultObj.send(null);
    }

    /**
    * シリアライズデータを探索結果に復元
    */
    function setSerializeData(serialize, tmpPriceFlag, callback) {
        callbackFunction = callback;
        var url = apiURL + "v1/json/course/edit?key=" + key + "&serializeData=" + encodeURIComponent(serialize);
        // エンジンバージョン同一チェック
        if (!checkEngineVersion) {
            url += "&checkEngineVersion=false";
        }
        searchRun(url, tmpPriceFlag);
    }

    /**
    * 前後のダイヤ探索
    */
    function assignDia(type) {
        // 探索オブジェクトの特定
        var tmpResult;
        if (resultCount == 1) {
            tmpResult = result.ResultSet.Course;
        } else {
            tmpResult = result.ResultSet.Course[(selectNo - 1)];
        }
        // シリアライズデータを設定し
        var url = apiURL + "v1/json/course/edit?key=" + key + "&serializeData=" + encodeURIComponent(tmpResult.SerializeData);
        if (type == "prev") {
            // 前のダイヤ
            url += "&assignInstruction=AutoPrevious";
        } else if (type == "next") {
            // 次のダイヤ
            url += "&assignInstruction=AutoNext";
        }
        // エンジンバージョン同一チェック
        if (!checkEngineVersion) {
            url += "&checkEngineVersion=false";
        }
        // 探索を実行
        reSearch(url, selectNo);
    }

    /**
    * JSONを解析して探索結果を出力
    */
    function setResult(resultObject, param1, param2) {
        if (!document.getElementById(baseId + ':result')) {
            dispCourse();
        }
        if (typeof param1 == 'undefined') {
            callbackFunction = undefined;
        } else if (typeof param2 == 'undefined') {
            if (typeof param1 == 'function') {
                callbackFunction = param1;
            } else {
                priceViewFlag = param1;
                callbackFunction = undefined;
            }
        } else {
            priceViewFlag = param1;
            callbackFunction = param2;
        }
        if (typeof resultObject == 'undefined') {
            // 探索結果が取得できていない場合
            if (typeof callbackFunction == 'function') {
                callbackFunction(false);
            }
        } else if (resultObject == "") {
            // 探索結果が取得できていない場合
            if (typeof callbackFunction == 'function') {
                callbackFunction(false);
            }
        } else {
            // オブジェクトの場合は一度テキストに変換しておく
            if (typeof resultObject == 'object') {
                result = JSON.parse(JSON.stringify(resultObject));
            } else {
                result = JSON.parse(resultObject);
            }
            // 描画領域を初期化
            if (!document.getElementById(baseId + ':result')) {
                dispCourse();
            }
            // 経路表示
            viewResult();
            // 表示する
            document.getElementById(baseId + ':course').style.display = "block";
            // 一度だけコールバックする
            if (typeof callbackFunction == 'function') {
                if (typeof result == 'undefined') {
                    // 探索結果オブジェクトがない場合
                    document.getElementById(baseId + ':course').style.display = "none";
                    callbackFunction(false);
                } else if (typeof result.ResultSet.Course == 'undefined') {
                    // 探索結果が取得できていない場合
                    document.getElementById(baseId + ':course').style.display = "none";
                    callbackFunction(false);
                } else {
                    // 探索完了
                    callbackFunction(true);
                }
                callbackFunction = undefined;
            }
        }
    }

    /**
    * 料金種別の変更
    */
    function setPriceType(tmpPriceFlag) {
        priceViewFlag = tmpPriceFlag;
        changeCourse(selectNo);
    }

    /**
    * 探索結果出力部分
    */
    function viewResult() {
        if (typeof result == 'undefined') {
            // 探索結果オブジェクトがない場合
            return false;
        } else if (typeof result.ResultSet.Course == 'undefined') {
            // 探索結果がない場合
            return false;
        } else {
            // 必ず第一経路を表示
            selectNo = 1;
            // 経路一覧を表示に切り替え
            viewCourseListFlag = courseListFlag;
            if (typeof result.ResultSet.Course.length == 'undefined') {
                // 探索結果が単一の場合
                resultCount = 1;
            } else {
                // 探索結果が複数の場合
                resultCount = result.ResultSet.Course.length;
            }
            // 最適経路のチェック
            checkCourseList();

            // 探索結果の描画
            var buffer = '';
            buffer += '<div id="' + baseId + ':result:header" class="exp_resultHeader exp_clearfix"></div>';
            buffer += '<div id="' + baseId + ':result:body"></div>';
            document.getElementById(baseId + ':result').innerHTML = buffer;

            // 経路を出力
            viewResultList();
        }
    }

    /**
    * 表示している経路を変更
    */
    function changeCourse(n, callback) {
        if (n >= 1 && n <= resultCount) {
            selectNo = n;
            viewCourseListFlag = false;
            // 最適経路のチェック
            checkCourseList();
            // 経路を出力
            viewResultList();
            // 変更を検出
            if (typeof callback == 'function') {
                callback(true);
            } else if (typeof callBackFunctionBind['change'] == 'function') {
                callBackFunctionBind['change'](true);
            }
        } else {
            // 失敗
            if (typeof callback == 'function') {
                callback(false);
            } else if (typeof callBackFunctionBind['change'] == 'function') {
                callBackFunctionBind['change'](false);
            }
        }
    }

    /**
    * 最適経路のフラグをチェックする
    */
    function checkCourseList() {
        // ソート用の配列を用意する
        sortCourseList = new Array();
        for (var i = 0; i < resultCount; i++) {
            var tmp_sortObject = new Object();
            tmp_sortObject.index = i + 1;
            sortCourseList.push(tmp_sortObject);
        }
        minEkispertIndex = undefined;
        minTimeSummary = undefined;
        minTransferCount = undefined;
        minPriceSummary = undefined;
        minPriceRoundSummary = undefined;
        minTeikiSummary = undefined;
        minTeiki1Summary = undefined;
        minTeiki3Summary = undefined;
        minTeiki6Summary = undefined;
        minTeiki12Summary = undefined;
        minExhaustCO2 = undefined;
        // 探索結果が2以上の場合にチェックする
        if (resultCount >= 2) {
            // 駅すぱあとの時間順
            resultSearchType = "plain";
            // 最適経路フラグ
            for (var i = 0; i < resultCount; i++) {
                var tmpResult;
                tmpResult = result.ResultSet.Course[i];
                if (typeof tmpResult.searchType != 'undefined' && tmpResult.searchType != "plain") {
                    resultSearchType = result.ResultSet.Course[i].searchType;
                }
                sortCourseList[i].departure = getDepartureState(tmpResult).getTime();
                sortCourseList[i].arrival = getArrivalState(tmpResult).getTime();
                // 所要時間をチェック
                var TimeSummary = parseInt(tmpResult.Route.timeOnBoard) + parseInt(tmpResult.Route.timeWalk) + parseInt(tmpResult.Route.timeOther);
                if (typeof minTimeSummary == 'undefined') {
                    minTimeSummary = TimeSummary;
                } else if (minTimeSummary > TimeSummary) {
                    minTimeSummary = TimeSummary;
                }
                sortCourseList[i].timeSummary = TimeSummary;
                // 乗り換え回数をチェック
                var transferCount = parseInt(tmpResult.Route.transferCount);
                if (typeof minTransferCount == 'undefined') {
                    minTransferCount = transferCount;
                } else if (minTransferCount > transferCount) {
                    minTransferCount = transferCount;
                }
                sortCourseList[i].transferCount = transferCount;
                // CO2排出量をチェック
                var exhaustCO2 = parseInt(tmpResult.Route.exhaustCO2);
                if (typeof minExhaustCO2 == 'undefined') {
                    minExhaustCO2 = exhaustCO2;
                } else if (minExhaustCO2 > exhaustCO2) {
                    minExhaustCO2 = exhaustCO2;
                }
                // 料金の計算
                var FareSummary = 0;
                var FareRoundSummary = 0;
                var ChargeSummary = 0;
                var ChargeRoundSummary = 0;
                var Teiki1Summary = undefined;
                var Teiki3Summary = undefined;
                var Teiki6Summary = undefined;
                var Teiki12Summary = undefined;
                if (typeof tmpResult.Price != 'undefined') {
                    for (var j = 0; j < tmpResult.Price.length; j++) {
                        if (tmpResult.Price[j].kind == "FareSummary") {
                            if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                                FareSummary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                            }
                            if (typeof tmpResult.Price[j].Round != 'undefined') {
                                FareRoundSummary = parseInt(getTextValue(tmpResult.Price[j].Round));
                            }
                        } else if (tmpResult.Price[j].kind == "ChargeSummary") {
                            if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                                ChargeSummary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                            }
                            if (typeof tmpResult.Price[j].Round != 'undefined') {
                                ChargeRoundSummary = parseInt(getTextValue(tmpResult.Price[j].Round));
                            }
                        } else if (tmpResult.Price[j].kind == "Teiki1Summary") {
                            if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                                Teiki1Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                            }
                        } else if (tmpResult.Price[j].kind == "Teiki3Summary") {
                            if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                                Teiki3Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                            }
                        } else if (tmpResult.Price[j].kind == "Teiki6Summary") {
                            if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                                Teiki6Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                            }
                        } else if (tmpResult.Price[j].kind == "Teiki12Summary") {
                            if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                                Teiki12Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                            }
                        }
                    }
                    // 金額のチェック
                    if (typeof minPriceSummary == 'undefined') {
                        minPriceSummary = FareSummary + ChargeSummary;
                    } else if (minPriceSummary > (FareSummary + ChargeSummary)) {
                        minPriceSummary = FareSummary + ChargeSummary;
                    }
                    sortCourseList[i].priceSummary = FareSummary + ChargeSummary;
                    // 往復金額のチェック
                    if (typeof minPriceRoundSummary == 'undefined') {
                        minPriceRoundSummary = FareRoundSummary + ChargeRoundSummary;
                    } else if (minPriceRoundSummary > (FareRoundSummary + ChargeRoundSummary)) {
                        minPriceRoundSummary = FareRoundSummary + ChargeRoundSummary;
                    }
                    sortCourseList[i].priceRoundSummary = FareSummary + ChargeSummary;
                    // 定期券1
                    if (typeof Teiki1Summary != 'undefined') {
                        if (typeof minTeiki1Summary == 'undefined') {
                            minTeiki1Summary = Teiki1Summary;
                        } else if (minTeiki1Summary > Teiki1Summary) {
                            minTeiki1Summary = Teiki1Summary;
                        }
                    }
                    sortCourseList[i].teiki1Summary = Teiki1Summary;
                    // 定期券3
                    if (typeof Teiki3Summary != 'undefined') {
                        if (typeof minTeiki3Summary == 'undefined') {
                            minTeiki3Summary = Teiki3Summary;
                        } else if (minTeiki3Summary > Teiki3Summary) {
                            minTeiki3Summary = Teiki3Summary;
                        }
                    }
                    sortCourseList[i].teiki3Summary = Teiki3Summary;
                    // 定期券6
                    if (typeof Teiki6Summary != 'undefined') {
                        if (typeof minTeiki6Summary == 'undefined') {
                            minTeiki6Summary = Teiki6Summary;
                        } else if (minTeiki6Summary > Teiki6Summary) {
                            minTeiki6Summary = Teiki6Summary;
                        }
                    }
                    var TeikiSummary = 0;
                    //定期の合計
                    if (typeof Teiki6Summary != 'undefined') {
                        if (typeof minTeikiSummary == 'undefined') {
                            minTeikiSummary = Teiki6Summary;
                        } else if (minTeikiSummary > Teiki6Summary) {
                            minTeikiSummary = Teiki6Summary;
                        }
                        TeikiSummary = Teiki6Summary;
                    } else if (typeof Teiki3Summary != 'undefined') {
                        if (typeof minTeikiSummary == 'undefined') {
                            minTeikiSummary = Teiki3Summary * 2;
                        } else if (minTeikiSummary > Teiki3Summary * 2) {
                            minTeikiSummary = Teiki3Summary * 2;
                        }
                        TeikiSummary = Teiki3Summary * 2;
                    } else if (typeof Teiki1Summary != 'undefined') {
                        if (typeof minTeikiSummary == 'undefined') {
                            minTeikiSummary = Teiki1Summary * 6;
                        } else if (minTeikiSummary > Teiki1Summary * 6) {
                            minTeikiSummary = Teiki1Summary * 6;
                        }
                        TeikiSummary = Teiki3Summary * 6;
                    }
                    sortCourseList[i].teikiSummary = TeikiSummary;
                }
            }
            // 時間のソートを行う
            sortCourse("time");
            if (resultSearchType != "plain") {
                minEkispertIndex = sortCourseList[0].index;
            }
            // ソートを実施
            if (sortType != "time") {
                sortCourse(sortType);
            }
        }
    }

    /**
    * 経路一覧の表示・非表示設定
    */
    function dispCourseList() {
        viewCourseListFlag = (viewCourseListFlag ? false : true);
        // 経路を出力
        viewResultList();
    }

    /**
    * 探索結果のタブを出力し、選択されている経路も出力
    */
    function viewResultList() {
        // 経路が複数ある場合と初期表示が経路一覧の場合は、タブを出力
        if (resultTab) {
            if (!courseDisplayAll) {
                if (resultCount > 1 || courseListFlag) {
                    if (agent == 1 || agent == 3) {
                        viewResultTab();
                    } else if (agent == 2) {
                        if (!viewCourseListFlag) {
                            viewResultTab();
                        } else {
                            document.getElementById(baseId + ':result:header').innerHTML = '';
                            document.getElementById(baseId + ':result:header').style.display = "none";
                        }
                    }
                } else {
                    document.getElementById(baseId + ':result:header').style.display = "none";
                }
            } else {
                document.getElementById(baseId + ':result:header').style.display = "none";
            }
        } else {
            document.getElementById(baseId + ':result:header').style.display = "none";
        }
        // リンクの設定
        var buffer = '';
        if (viewCourseListFlag) {
            // 経路一覧の表示
            buffer += viewCourseList();
        } else {
            // 経路一覧同時表示用
            if (courseDisplayAll) {
                buffer += viewCourseList();
            }
            // 経路出力本体
            var tmpResult;
            if (resultCount == 1) {
                // 探索結果が単一
                buffer += viewResultRoute(result.ResultSet.Course);
            } else if (courseDisplayAll) {
                // 全経路出力
                for (var n = 1; n <= resultCount; n++) {
                    buffer += viewResultRoute(result.ResultSet.Course[(n - 1)], n);
                }
            } else {
                // 探索結果が複数
                buffer += viewResultRoute(result.ResultSet.Course[(selectNo - 1)], selectNo);
            }
        }
        // 表示
        document.getElementById(baseId + ':result:body').innerHTML = buffer;
        // 金額の切り替え
        if (agent == 2 || agent == 3) {
            for (var n = 0; n < resultCount; n++) {
                if (resultCount == 1) {
                    tmpResult = result.ResultSet.Course;
                } else {
                    tmpResult = result.ResultSet.Course[n];
                }
                for (var i = 0; i < (tmpResult.Route.Point.length - 1); i++) {
                    if (priceChangeFlag && document.getElementById(baseId + ':fareSelect:' + (n + 1) + ':' + (i + 1))) {
                        if (typeof document.getElementById(baseId + ':fareSelect:' + (n + 1) + ':' + (i + 1)).selectedIndex != 'undefined') {
                            addEvent(document.getElementById(baseId + ':fareSelect:' + (n + 1) + ':' + (i + 1)), "change", changePrice);
                        }
                    }
                    if (document.getElementById(baseId + ':chargeSelect:' + (n + 1) + ':' + (i + 1))) {
                        if (typeof document.getElementById(baseId + ':chargeSelect:' + (n + 1) + ':' + (i + 1)).selectedIndex != 'undefined') {
                            addEvent(document.getElementById(baseId + ':chargeSelect:' + (n + 1) + ':' + (i + 1)), "change", changePrice);
                        }
                    }
                    if (priceChangeFlag && priceChangeRefreshFlag) {
                        if (document.getElementById(baseId + ':teikiSelect:' + (n + 1) + ':' + (i + 1))) {
                            if (typeof document.getElementById(baseId + ':teikiSelect:' + (n + 1) + ':' + (i + 1)).selectedIndex != 'undefined') {
                                addEvent(document.getElementById(baseId + ':teikiSelect:' + (n + 1) + ':' + (i + 1)), "change", changePrice);
                            }
                        }
                    }
                }
            }
        }
        document.getElementById(baseId + ':result:body').style.display = "block";
        document.getElementById(baseId + ':result').style.display = "block";
        if (agent == 1){
            $('#dialog').animate({scrollTop: 470},300);
        }else if(agent == 2){
        	$('#dialog').animate({scrollTop: 1500},300);
        }else if(agent == 3){
        	$('#dialog').animate({scrollTop: 600},300);
        }
    }

    /**
    * 探索結果のタブを出力
    */
    function viewResultTab() {
        var buffer = '';
        buffer += '<div>';
        buffer += '<div class="exp_resultListButton">';
        if (viewCourseListFlag) {
            buffer += '<div class="exp_on">';
            if (agent == 1) {
                buffer += '<a id="' + baseId + ':tab:list" href="Javascript:void(0);"><span class="exp_text" id="' + baseId + ':tab:list:text">結果一覧</span></a>';
            } else if (agent == 2 || agent == 3) {
                buffer += '<a class="exp_link" id="' + baseId + ':tab:list" href="Javascript:void(0);"><span class="exp_text" id="' + baseId + ':tab:list:text">結果一覧</span></a>';
            }
            buffer += '</div>';
        } else {
            buffer += '<div class="exp_off">';
            if (agent == 1) {
                buffer += '<a id="' + baseId + ':tab:list" href="Javascript:void(0);"><span class="exp_text" id="' + baseId + ':tab:list:text">結果一覧</span></a>';
            } else if (agent == 2 || agent == 3) {
                buffer += '<a class="exp_link" id="' + baseId + ':tab:list" href="Javascript:void(0);"><span class="exp_text" id="' + baseId + ':tab:list:text">結果一覧</span></a>';
            }

            buffer += '</div>';
        }
        buffer += '</div>';
        if (agent == 1 || agent == 3) {
            buffer += '<ul class="exp_resultTab">';
            for (var n = 1; n <= resultCount; n++) {
                var buttonType = "";
                if (agent == 1) {
                    if (n == resultCount || n == 10) {
                        buttonType = "resultTabButtonRight";
                    }
                } else if (agent == 2 || agent == 3) {
                    if (n == 1) {
                        buttonType = "leftBtn";
                    } else if (n == resultCount) {
                        buttonType = "rightBtn";
                    }
                }
                if (selectNo == (n)) {
                    if (agent == 1) {
                        if (n == 11) {
                            // 改行
                            buffer += '<div class="exp_return"></div>';
                        }
                        buffer += '<li class="exp_resultTabButtonSelect' + ((n <= 10 && resultCount > 10) ? " exp_buttom" : "") + (buttonType != "" ? " exp_" + buttonType : "") + '">';
                        buffer += '<a class="exp_link" id="' + baseId + ':tab:' + String(n) + '" href="Javascript:void(0);"><span class="exp_text" id="' + baseId + ':tab:' + String(n) + ':text">' + String(n) + '</span></a>';
                        buffer += '</li>';
                    } else if (agent == 2 || agent == 3) {
                        buffer += '<li class="exp_resultTabButtonSelect' + (buttonType != "" ? " exp_" + buttonType : "") + '">';
                        buffer += '<a class="exp_link" id="' + baseId + ':tab:' + String(n) + '" href="Javascript:void(0);">' + String(n) + '</a>';
                        buffer += '</li>';
                    }
                } else {
                    if (agent == 1) {
                        if (n <= 10) {
                            buffer += '<li class="exp_resultTabButtonNoSelect' + (resultCount > 10 ? " exp_buttom" : "") + (buttonType != "" ? " exp_" + buttonType : "") + '">';
                        } else {
                            if (n == 11) {
                                // 改行
                                buffer += '<div class="exp_return"></div>';
                            }
                            buffer += '<li class="exp_resultTabButtonNoSelect' + (buttonType != "" ? " exp_" + buttonType : "") + '">';
                        }
                        buffer += '<a class="exp_link" id="' + baseId + ':tab:' + String(n) + '" href="Javascript:void(0);"><span class="exp_text" id="' + baseId + ':tab:' + String(n) + ':text">' + String(n) + '</span></a>';
                        buffer += '</li>';
                    } else if (agent == 2 || agent == 3) {
                        buffer += '<li class="exp_resultTabButtonNoSelect' + (buttonType != "" ? " exp_" + buttonType : "") + '">';
                        buffer += '<a class="exp_link" id="' + baseId + ':tab:' + String(n) + '" href="Javascript:void(0);">' + String(n) + '</a>';
                        buffer += '</li>';
                    }
                }
            }
            buffer += '</ul>';
        } else if (agent == 2) {
            /**
            buffer += '<div class="exp_resultChangeButton">';
            buffer += '<div class="exp_button">';
            buffer += '<span class="exp_text" id="' + baseId + ':result:change:text">他の経路</span>';
            //選択
            buffer += '<select class="exp_selectObj" id="' + baseId + ':resultSelect">';
            for (var n = 1; n <= resultCount; n++) {
            buffer += '<option value="' + String(n) + '"' + (n == selectNo ? " selected" : "") + '>' + String(n) + '</option>';
            }
            buffer += '</select>';
            buffer += '</div>';
            buffer += '</div>';
            */
        }
        buffer += '</div>';

        document.getElementById(baseId + ':result:header').innerHTML = buffer;
        document.getElementById(baseId + ':result:header').style.display = "block";
    }

    /**
    * 経路の出力文字列を作成
    */
    function viewCourseList() {
        var buffer = "";
        // リストの出力
        buffer += '<div class="exp_resultList">';
        // タイトル
        buffer += '<div class="exp_resultListTitle exp_clearfix">';
        // 出発地
        buffer += '<div class="exp_from">';
        var firstCourse;
        var courseList = new Array();
        if (resultCount == 1) {
            // 探索結果が単一
            firstCourse = result.ResultSet.Course;
            courseList.push(firstCourse);
        } else {
            // 探索結果が複数
            firstCourse = result.ResultSet.Course[0];
            for (var i = 0; i < resultCount; i++) {
                // ソート時の経路を設定
                courseList.push(result.ResultSet.Course[sortCourseList[i].index - 1]);
            }
        }
        if (typeof fromName != 'undefined') {
            buffer += sanitaize(fromName);
        } else if (typeof firstCourse.Route.Point[0].Station != 'undefined') {
            buffer += firstCourse.Route.Point[0].Station.Name;
        } else if (typeof firstCourse.Route.Point[0].Name != 'undefined') {
            if (firstCourse.Route.Point[0].Name.split(",")[2] == "tokyo" || firstCourse.Route.Point[0].Name.split(",")[2] == "wgs84") {
                buffer += "座標情報";
            } else {
                buffer += firstCourse.Route.Point[0].Name;
            }
        }
        buffer += '</div>';
        buffer += '<div class="exp_cursor"></div>';
        // 目的地
        buffer += '<div class="exp_to">';
        if (typeof toName != 'undefined') {
            buffer += sanitaize(toName);
        } else if (typeof firstCourse.Route.Point[firstCourse.Route.Point.length - 1].Station != 'undefined') {
            buffer += firstCourse.Route.Point[firstCourse.Route.Point.length - 1].Station.Name;
        } else if (typeof firstCourse.Route.Point[firstCourse.Route.Point.length - 1].Name != 'undefined') {
            if (firstCourse.Route.Point[firstCourse.Route.Point.length - 1].Name.split(",")[2] == "tokyo" || firstCourse.Route.Point[firstCourse.Route.Point.length - 1].Name.split(",")[2] == "wgs84") {
                buffer += "座標情報";
            } else {
                buffer += firstCourse.Route.Point[firstCourse.Route.Point.length - 1].Name;
            }
        }
        buffer += '</div>';
        var searchDate;
        if (typeof searchObj != 'undefined') {
            if (typeof searchObj.getDate() != 'undefined') {
                searchDate = new Date(parseInt(String(searchObj.getDate()).substring(0, 4), 10), parseInt(String(searchObj.getDate()).substring(4, 6), 10) - 1, parseInt(String(searchObj.getDate()).substring(6, 8), 10));
                buffer += '<div class="exp_date">';
                var week = new Array('日', '月', '火', '水', '木', '金', '土');
                buffer += String(searchDate.getFullYear()) + '年' + String(searchDate.getMonth() + 1) + '月' + String(searchDate.getDate()) + '日';
                buffer += '(' + week[searchDate.getDay()] + ')';
                buffer += '</div>';
            }
        }
        buffer += '</div>';
        // 運賃改定未対応
        var salesTaxRateIsNotSupported = false;
        for (var i = 0; i < resultCount; i++) {
            if (typeof courseList[i].Price != 'undefined') {
                for (var j = 0; j < courseList[i].Price.length; j++) {
                    if (typeof courseList[i].Price[j].fareRevisionStatus != 'undefined') {
                        if (courseList[i].Price[j].fareRevisionStatus == 'salesTaxRateIsNotSupported') {
                            if (priceViewFlag == "oneway" || priceViewFlag == "round") {
                                // 片道・往復計算時
                                if (courseList[i].Price[j].kind == "Fare" || courseList[i].Price[j].kind == "Charge") {
                                    // 運賃改定未対応
                            salesTaxRateIsNotSupported = true;
                                }
                            } else if (priceViewFlag == "teiki") {
                                // 定期計算時
                                if (courseList[i].Price[j].kind == "Teiki1" || courseList[i].Price[j].kind == "Teiki3" || courseList[i].Price[j].kind == "Teiki6") {
                                    // 運賃改定未対応
                                    salesTaxRateIsNotSupported = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (salesTaxRateIsNotSupported) {
            buffer += '<div class="exp_fareRevisionStatus">';
            buffer += '赤色の金額は消費税率変更に未対応です';
            buffer += '</div>';
        }
        // ソートを行う
        if (!resultTab) {
            buffer += '<div class="exp_sortTab exp_clearfix">';
            // 時間順
            buffer += '<a class="exp_sortButton ' + (sortType == "time" ? "exp_hayaiButtonSelected" : "exp_hayaiButton") + '" id="' + baseId + ':sort:time" href="Javascript:void(0);">';
            buffer += '<span class="exp_hayai" id="' + baseId + ':sort:time:icon"></span>';
            buffer += '<span class="exp_text" id="' + baseId + ':sort:time:text">時間順</span>';
            buffer += '</a>';
            // 安い順
            buffer += '<a class="exp_sortButton ' + (sortType == "price" ? "exp_yasuiButtonSelected" : "exp_yasuiButton") + '" id="' + baseId + ':sort:price" href="Javascript:void(0);">';
            buffer += '<span class="exp_yasui" id="' + baseId + ':sort:price:icon"></span>';
            buffer += '<span class="exp_text" id="' + baseId + ':sort:price:text">料金の安い順</span>';
            buffer += '</a>';
            // 乗り換え順
            buffer += '<a class="exp_sortButton ' + (sortType == "transfer" ? "exp_rakuButtonSelected" : "exp_rakuButton") + '" id="' + baseId + ':sort:transfer" href="Javascript:void(0);">';
            buffer += '<span class="exp_raku" id="' + baseId + ':sort:transfer:icon"></span>';
            buffer += '<span class="exp_text" id= "' + baseId + ':sort:transfer:text" > 乗換回数順</span>';
            buffer += '</a>';
            buffer += '</div>';
        }
        // 金額をチェック
        for (var i = 0; i < resultCount; i++) {
            var tmpResult;
            tmpResult = courseList[i];
            var time = parseInt(tmpResult.Route.timeOnBoard) + parseInt(tmpResult.Route.timeWalk) + parseInt(tmpResult.Route.timeOther);
            var TransferCount = parseInt(tmpResult.Route.transferCount);
            var FareSummary = 0;
            var FareRoundSummary = 0;
            var ChargeSummary = 0;
            var ChargeRoundSummary = 0;
            var Teiki1Summary = undefined;
            var Teiki3Summary = undefined;
            var Teiki6Summary = undefined;
            var Teiki12Summary = undefined;
            // 運賃改定未対応
            var FareSummarySalesTaxRateIsNotSupported = false;
            var ChargeSummarySalesTaxRateIsNotSupported = false;
            var Teiki1SummarySalesTaxRateIsNotSupported = false;
            var Teiki3SummarySalesTaxRateIsNotSupported = false;
            var Teiki6SummarySalesTaxRateIsNotSupported = false;
            var Teiki12SummarySalesTaxRateIsNotSupported = false;
            // 料金の計算
            if (typeof tmpResult.Price != 'undefined') {
                for (var j = 0; j < tmpResult.Price.length; j++) {
                    if (tmpResult.Price[j].kind == "FareSummary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            FareSummary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                        if (typeof tmpResult.Price[j].Round != 'undefined') {
                            FareRoundSummary = parseInt(getTextValue(tmpResult.Price[j].Round));
                        }
                    } else if (tmpResult.Price[j].kind == "ChargeSummary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            ChargeSummary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                        if (typeof tmpResult.Price[j].Round != 'undefined') {
                            ChargeRoundSummary = parseInt(getTextValue(tmpResult.Price[j].Round));
                        }
                    } else if (tmpResult.Price[j].kind == "Teiki1Summary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            Teiki1Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                    } else if (tmpResult.Price[j].kind == "Teiki3Summary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            Teiki3Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                    } else if (tmpResult.Price[j].kind == "Teiki6Summary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            Teiki6Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                    } else if (tmpResult.Price[j].kind == "Teiki12Summary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            Teiki12Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                    } else {
                        // 運賃改定未対応チェック
                        if (typeof tmpResult.Price[j].fareRevisionStatus != 'undefined') {
                            if (tmpResult.Price[j].fareRevisionStatus == 'salesTaxRateIsNotSupported') {
                                if (tmpResult.Price[j].kind == "Fare") {
                                    FareSummarySalesTaxRateIsNotSupported = true;
                                } else if (tmpResult.Price[j].kind == "Charge") {
                                    ChargeSummarySalesTaxRateIsNotSupported = true;
                                } else if (tmpResult.Price[j].kind == "Teiki1") {
                                    Teiki1SummarySalesTaxRateIsNotSupported = true;
                                } else if (tmpResult.Price[j].kind == "Teiki3") {
                                    Teiki3SummarySalesTaxRateIsNotSupported = true;
                                } else if (tmpResult.Price[j].kind == "Teiki6") {
                                    Teiki6SummarySalesTaxRateIsNotSupported = true;
                                } else if (tmpResult.Price[j].kind == "Teiki12") {
                                    Teiki12SummarySalesTaxRateIsNotSupported = true;
                                }
                            }
                        }
                    }
                }
            }
            var salesTaxRateIsNotSupported = (FareSummarySalesTaxRateIsNotSupported || ChargeSummarySalesTaxRateIsNotSupported);
            // 探索結果一覧
            buffer += '<a class="exp_link" id="' + baseId + ':list:' + String(i + 1) + '" href="Javascript:void(0);">';
            buffer += '<div class="exp_resultListRow exp_' + (i % 2 == 0 ? 'odd' : 'even') + (sortCourseList[i].index == selectedIndex ? ' exp_selectedRoute' : '') + ' exp_clearfix">';
            // 結果NO
            buffer += '<div class="exp_no" id="' + baseId + ':list:' + String(i + 1) + ':no">';
            buffer += '<span class="exp_routeNo" id="' + baseId + ':list:' + String(i + 1) + ':no:text">' + String(i + 1) + '</span>';
            buffer += '</div>';
            // 探索結果の情報
            buffer += '<div class="exp_summary">';
            // 上の段
            buffer += '<div class="exp_upper" id="' + baseId + ':list:' + String(i + 1) + ':upper">';
            var icon_count = 0;
            // アイコン
            buffer += '<div class="exp_mark exp_clearfix" id="' + baseId + ':list:' + String(i + 1) + ':icon">';
            if (typeof minEkispertIndex != 'undefined') {
                if (getDepartureState(result.ResultSet.Course[minEkispertIndex - 1]).getTime() == getDepartureState(tmpResult).getTime() && getArrivalState(result.ResultSet.Course[minEkispertIndex - 1]).getTime() == getArrivalState(tmpResult).getTime()) {
                    buffer += '<span class="exp_hayai" id="' + baseId + ':list:' + String(i + 1) + ':icon:hayai"></span>';
                    icon_count++;
                }
            } else if (minTimeSummary == time) {
                buffer += '<span class="exp_hayai" id="' + baseId + ':list:' + String(i + 1) + ':icon:hayai"></span>';
                icon_count++;
            }
            if (priceViewFlag == "oneway") {
                if (minPriceSummary == (FareSummary + ChargeSummary)) {
                    buffer += '<span class="exp_yasui" id="' + baseId + ':list:' + String(i + 1) + ':icon:yasui"></span>';
                    icon_count++;
                }
            } else if (priceViewFlag == "round") {
                if (minPriceRoundSummary == (FareRoundSummary + ChargeRoundSummary)) {
                    buffer += '<span class="exp_yasui" id="' + baseId + ':list:' + String(i + 1) + ':icon:yasui"></span>';
                    icon_count++;
                }
            } else if (priceViewFlag == "teiki") {
                if (typeof Teiki6Summary != 'undefined') {
                    if (minTeikiSummary == Teiki6Summary) {
                        buffer += '<span class="exp_yasui" id="' + baseId + ':list:' + String(i + 1) + ':icon:yasui"></span>';
                        icon_count++;
                    }
                } else if (typeof Teiki3Summary != 'undefined') {
                    if (minTeikiSummary == Teiki3Summary * 2) {
                        buffer += '<span class="exp_yasui" id="' + baseId + ':list:' + String(i + 1) + ':icon:yasui"></span>';
                        icon_count++;
                    }
                } else if (typeof Teiki1Summary != 'undefined') {
                    if (minTeikiSummary == Teiki1Summary * 6) {
                        buffer += '<span class="exp_yasui" id="' + baseId + ':list:' + String(i + 1) + ':icon:yasui"></span>';
                        icon_count++;
                    }
                }
            }
            if (minTransferCount == TransferCount) {
                buffer += '<span class="exp_raku" id="' + baseId + ':list:' + String(i + 1) + ':icon:raku"></span>';
                icon_count++;
            }
            // 残りの情報を入れる
            var summary_info = "";
            if (typeof tmpResult.Route.Line.length == 'undefined') {
                if (getTextValue(tmpResult.Route.Line.Type) == "walk") {
                    summary_info = "徒歩";
                } else {
                    summary_info = "直通";
                }
            } else {
                // 最初と最後の駅は除く
                for (var j = 1; j < tmpResult.Route.Point.length - 1; j++) {
                    if (j > 1) { summary_info += "・"; }
                    if (typeof tmpResult.Route.Point[j].Station != 'undefined') {
                        summary_info += tmpResult.Route.Point[j].Station.Name;
                    } else if (typeof point.Name != 'undefined') {
                        summary_info += tmpResult.Route.Point[j].Name;
                    }
                }
                summary_info += " 乗換";
            }
            buffer += '<span class="exp_information_' + ((tmpResult.dataType == "onTimetable" ? "dia" : "plane")) + (icon_count > 0 ? String(icon_count) : "") + '" id="' + baseId + ':list:' + String(i + 1) + ':information">' + summary_info + '</span>';

            buffer += '</div>';
            // ダイヤ探索のみ
            if (tmpResult.dataType == "onTimetable") {
                buffer += '<div class="exp_time exp_clearfix" id="' + baseId + ':list:' + String(i + 1) + ':time">';
                // 発着時刻
                var DepartureTime, ArrivalTime;
                if (typeof tmpResult.Route.Line.length == 'undefined') {
                    if (typeof tmpResult.Route.Line.DepartureState.Datetime.text != 'undefined') {
                        DepartureTime = convertISOtoDate(tmpResult.Route.Line.DepartureState.Datetime.text);
                    }
                    if (typeof tmpResult.Route.Line.ArrivalState.Datetime.text != 'undefined') {
                        ArrivalTime = convertISOtoDate(tmpResult.Route.Line.ArrivalState.Datetime.text);
                    }
                } else {
                    if (typeof tmpResult.Route.Line[0].DepartureState.Datetime.text != 'undefined') {
                        DepartureTime = convertISOtoDate(tmpResult.Route.Line[0].DepartureState.Datetime.text);
                    }
                    if (typeof tmpResult.Route.Line[tmpResult.Route.Line.length - 1].ArrivalState.Datetime.text != 'undefined') {
                        ArrivalTime = convertISOtoDate(tmpResult.Route.Line[tmpResult.Route.Line.length - 1].ArrivalState.Datetime.text);
                    }
                }
                //buffer += '<span class="exp_departure">出</span>';
                buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':time:dep">' + String(DepartureTime.getHours()) + ':' + (DepartureTime.getMinutes() < 10 ? '0' : '') + String(DepartureTime.getMinutes()) + '</span>';
                buffer += '<span class="exp_cursor" id="' + baseId + ':list:' + String(i + 1) + ':time:cursur"></span>';
                //buffer += '<span class="exp_arrival">着</span>';
                buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':time:arr">' + String(ArrivalTime.getHours()) + ':' + (ArrivalTime.getMinutes() < 10 ? '0' : '') + String(ArrivalTime.getMinutes()) + '</span>';
                buffer += '</div>';
            }
            buffer += '</div>';
            // 下の段
            buffer += '<div class="exp_lower" id="' + baseId + ':list:' + String(i + 1) + ':lower">';
            if (agent == 1 || agent == 3) {
                // 所要時間
                buffer += '<span class="exp_title" id="' + baseId + ':list:' + String(i + 1) + ':time">所要時間</span>';
                buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':time:text">' + String(time) + '分</span>';
                // 乗り換え回数
                if (priceViewFlag == "teiki") {
                    buffer += '<span class="exp_title" id="' + baseId + ':list:' + String(i + 1) + ':trans">乗り換え</span>';
                    buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':trans:text">' + String(TransferCount) + '回</span>';
                }
                //運賃
                if (priceViewFlag == "oneway") {
                    buffer += '<span class="exp_title" id="' + baseId + ':list:' + String(i + 1) + ':price">片道金額</span>';
                    buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price:text">';
                    buffer += salesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported">' : '';
                    buffer += num2String(FareSummary + ChargeSummary) + '円';
                    buffer += salesTaxRateIsNotSupported ? '</span>' : '';
                    buffer += '</span>';
                } else if (priceViewFlag == "round") {
                    buffer += '<span class="exp_title" id="' + baseId + ':list:' + String(i + 1) + ':price">往復金額</span>';
                    buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price:text">';
                    buffer += salesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported" id="' + baseId + ':list:' + String(i + 1) + ':price:text2">' : '';
                    buffer += num2String(FareRoundSummary + ChargeRoundSummary) + '円';
                    buffer += salesTaxRateIsNotSupported ? '</span>' : '';
                    buffer += '</span>';
                } else if (priceViewFlag == "teiki") {
                    // 定期券の表示
                    buffer += '<span class="exp_titleTeiki1" id="' + baseId + ':list:' + String(i + 1) + ':price">定期券1ヶ月</span>';
                    if (typeof Teiki1Summary != 'undefined') {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price:text">';
                        buffer += Teiki1SummarySalesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported" id="' + baseId + ':list:' + String(i + 1) + ':price:support">' : '';
                        buffer += num2String(Teiki1Summary) + '円';
                        buffer += Teiki1SummarySalesTaxRateIsNotSupported ? '</span>' : '';
                        buffer += '</span>';
                    } else {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price:text">------円</span>';
                    }
                    buffer += '<span class="exp_titleTeiki3">定期券3ヶ月</span>';
                    if (typeof Teiki3Summary != 'undefined') {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price:text">';
                        buffer += Teiki3SummarySalesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported" id="' + baseId + ':list:' + String(i + 1) + ':price:support">' : '';
                        buffer += num2String(Teiki3Summary) + '円';
                        buffer += Teiki3SummarySalesTaxRateIsNotSupported ? '</span>' : '';
                        buffer += '</span>';
                    } else {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price:text">------円</span>';
                    }
                    buffer += '<span class="exp_titleTeiki6" id="' + baseId + ':list:' + String(i + 1) + ':price">定期券6ヶ月</span>';
                    if (typeof Teiki6Summary != 'undefined') {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price:text">';
                        buffer += Teiki6SummarySalesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported" id="' + baseId + ':list:' + String(i + 1) + ':price:support">' : '';
                        buffer += num2String(Teiki6Summary) + '円';
                        buffer += Teiki6SummarySalesTaxRateIsNotSupported ? '</span>' : '';
                        buffer += '</span>';
                    } else {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price:text">------円</span>';
                    }
                    buffer += '<span class="exp_titleTeiki12" id="' + baseId + ':list:' + String(i + 1) + ':price">定期券12ヶ月</span>';
                    if (typeof Teiki12Summary != 'undefined') {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price:text">';
                        buffer += Teiki12SummarySalesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported" id="' + baseId + ':list:' + String(i + 1) + ':price:support">' : '';
                        buffer += num2String(Teiki12Summary) + '円';
                        buffer += Teiki12SummarySalesTaxRateIsNotSupported ? '</span>' : '';
                        buffer += '</span>';
                    } else {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price:text">------円</span>';
                    }
                }
                // 乗り換え回数
                if (priceViewFlag == "oneway" || priceViewFlag == "round") {
                    buffer += '<span class="exp_title" id="' + baseId + ':list:' + String(i + 1) + ':trans">乗り換え</span>';
                    if (TransferCount > 0) {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':trans:text">' + String(TransferCount) + '回</span>';
                    } else {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':trans:text">なし</span>';
                    }
                }
            } else if (agent == 2) {
                // 所要時間
                buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':time:text">' + String(time) + '分</span>';
                //運賃
                if (priceViewFlag == "oneway") {
                    buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price">';
                    buffer += salesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported">' : '';
                    buffer += '\\' + num2String(FareSummary + ChargeSummary);
                    buffer += salesTaxRateIsNotSupported ? '</span>' : '';
                    buffer += '</span>';
                } else if (priceViewFlag == "round") {
                    buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price">';
                    buffer += salesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported">' : '';
                    buffer += '\\' + num2String(FareRoundSummary + ChargeRoundSummary);
                    buffer += salesTaxRateIsNotSupported ? '</span>' : '';
                    buffer += '</span>';
                } else if (priceViewFlag == "teiki") {
                    // 定期券の表示
                    if (typeof Teiki1Summary != 'undefined') {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price">';
                        buffer += Teiki1SummarySalesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported" id="' + baseId + ':list:' + String(i + 1) + ':price:text">' : '';
                        buffer += '\\' + num2String(Teiki1Summary);
                        buffer += Teiki1SummarySalesTaxRateIsNotSupported ? '</span>' : '';
                        buffer += '(1ヵ月)</span>';
                    } else {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price:text">------</span>';
                    }
                    if (typeof Teiki3Summary != 'undefined') {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price">';
                        buffer += Teiki3SummarySalesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported" id="' + baseId + ':list:' + String(i + 1) + ':price:text">' : '';
                        buffer += '\\' + num2String(Teiki3Summary);
                        buffer += Teiki3SummarySalesTaxRateIsNotSupported ? '</span>' : '';
                        buffer += '(3ヵ月)</span>';
                    } else {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price:text">------</span>';
                    }
                    if (typeof Teiki6Summary != 'undefined') {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price">';
                        buffer += Teiki6SummarySalesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported" id="' + baseId + ':list:' + String(i + 1) + ':price:text">' : '';
                        buffer += '\\' + num2String(Teiki6Summary);
                        buffer += Teiki6SummarySalesTaxRateIsNotSupported ? '</span>' : '';
                        buffer += '(6ヵ月)</span>';
                    } else {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price:text">------</span>';
                    }
                    if (typeof Teiki12Summary != 'undefined') {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price">';
                        buffer += Teiki12SummarySalesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported" id="' + baseId + ':list:' + String(i + 1) + ':price:text">' : '';
                        buffer += '\\' + num2String(Teiki12Summary);
                        buffer += Teiki12SummarySalesTaxRateIsNotSupported ? '</span>' : '';
                        buffer += '(12ヵ月)</span>';
                    } else {
                        buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':price:text">------</span>';
                    }
                }
                // 乗り換え回数
                if (TransferCount > 0) {
                    buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':trans">乗換' + String(TransferCount) + '回</span>';
                } else {
                    buffer += '<span class="exp_value" id="' + baseId + ':list:' + String(i + 1) + ':trans">乗換なし</span>';
                }
            }
            buffer += '</div>';
            buffer += '</div>';
            buffer += '</div>';
            buffer += '</a>';
        }
        buffer += '</div>';
        return buffer;
    }

    /**
    * イベントの振り分け
    */
    function onEvent(e) {
        var eventIdList;
        if (typeof e == 'string') {
            eventIdList = e.split(":");
        } else {
            eventIdList = (e.srcElement) ? e.srcElement.id.split(":") : e.target.id.split(":");
        }
        if (eventIdList.length >= 2) {
            if (eventIdList[1] == "resultClose") {
                // ウィンドウを閉じる
                document.getElementById(baseId + ':course').style.display = "none";
                if (typeof callBackFunctionBind['close'] == 'function') {
                    callBackFunctionBind['close'](true);
                }
            } else if (eventIdList[1] == "resultPopup") {
                // 画面外
                document.getElementById(baseId + ':course').style.display = "none";
                if (typeof callBackFunctionBind['close'] == 'function') {
                    callBackFunctionBind['close'](true);
                }
            } else if (eventIdList[1] == "courseSelect" && eventIdList.length >= 3) {
                // 経路選択
                if (courseDisplayAll) {
                    selectNo = parseInt(eventIdList[2]);
                }
                if (windowFlag) {
                    document.getElementById(baseId + ':course').style.display = "none";
                }
                if (typeof callBackFunctionBind['select'] == 'function') {
                    callBackFunctionBind['select'](true);
                }
            } else if (eventIdList[1] == "tab" && eventIdList.length >= 3) {
                if (eventIdList[2] == "list") {
                    // 一覧の表示
                    dispCourseList();
                    if (typeof callBackFunctionBind['click'] == 'function') {
                        callBackFunctionBind['click'](true);
                    }
                } else {
                    // 経路の切り替え
                    changeCourse(sortCourseList[parseInt(eventIdList[2]) - 1].index);
                }
            } else if (eventIdList[1] == "list" && eventIdList.length >= 3) {
                // 経路の切り替え
                changeCourse(sortCourseList[parseInt(eventIdList[2]) - 1].index);
            } else if (eventIdList[1] == "resultSelect" && eventIdList.length >= 2) {
                // 経路の切り替え
                changeCourse(parseInt(document.getElementById(baseId + ':resultSelect').options.item(document.getElementById(baseId + ':resultSelect').selectedIndex).value));
            } else if (eventIdList[1] == "stationMenu" && eventIdList.length >= 5) {
                // 駅メニュー
                if (callBackObjectStation.length > 0) {
                    if (eventIdList[4] == "open") {
                        if (document.getElementById(baseId + ':stationMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display == "none") {
                            document.getElementById(baseId + ':stationMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "block";
                        } else {
                            document.getElementById(baseId + ':stationMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "none";
                        }
                    } else if (eventIdList[3] == "close") {
                        document.getElementById(baseId + ':stationMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "none";
                    } else {
                        if (courseDisplayAll) {
                            selectNo = parseInt(eventIdList[2]);
                        }
                        document.getElementById(baseId + ':stationMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "none";
                        callBackObjectStation[parseInt(eventIdList[4]) - 1].callBack(parseInt(eventIdList[3]));
                    }
                }
            } else if (eventIdList[1] == "lineMenu" && eventIdList.length >= 5) {
                // 路線メニュー
                if (callBackObjectLine.length > 0) {
                    if (eventIdList[4] == "open") {
                        if (document.getElementById(baseId + ':lineMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display == "none") {
                            document.getElementById(baseId + ':lineMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "block";
                        } else {
                            document.getElementById(baseId + ':lineMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "none";
                        }
                    } else if (eventIdList[3] == "close") {
                        document.getElementById(baseId + ':lineMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "none";
                    } else {
                        if (courseDisplayAll) {
                            selectNo = parseInt(eventIdList[2]);
                        }
                        document.getElementById(baseId + ':lineMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "none";
                        callBackObjectLine[parseInt(eventIdList[4]) - 1].callBack(parseInt(eventIdList[3]));
                    }
                }
            } else if (eventIdList[1] == "fareMenu" && eventIdList.length >= 5) {
                // 運賃メニュー
                if (priceChangeFlag) {
                    if (eventIdList[4] == "open") {
                        if (document.getElementById(baseId + ':fareMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display == "none") {
                            document.getElementById(baseId + ':fareMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "block";
                        } else {
                            document.getElementById(baseId + ':fareMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "none";
                        }
                    } else if (eventIdList[4] == "close") {
                        document.getElementById(baseId + ':fareMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "none";
                    } else {
                        if (priceChangeFlag) {
                            document.getElementById(baseId + ':fare:' + eventIdList[2] + ':' + eventIdList[3]).value = eventIdList[4];
                            if (courseDisplayAll) {
                                selectNo = parseInt(eventIdList[2]);
                            }
                            changePrice();
                        } else {
                            document.getElementById(baseId + ':fareMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "none";
                        }
                    }
                }
            } else if (eventIdList[1] == "chargeMenu" && eventIdList.length >= 5) {
                // 特急券メニュー
                if (true) {
                    if (eventIdList[4] == "open") {
                        if (document.getElementById(baseId + ':chargeMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display == "none") {
                            document.getElementById(baseId + ':chargeMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "block";
                        } else {
                            document.getElementById(baseId + ':chargeMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "none";
                        }
                    } else if (eventIdList[4] == "close") {
                        document.getElementById(baseId + ':chargeMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "none";
                    } else {
                        if (true) {
                            document.getElementById(baseId + ':charge:' + eventIdList[2] + ':' + eventIdList[3]).value = eventIdList[4];
                            if (courseDisplayAll) {
                                selectNo = parseInt(eventIdList[2]);
                            }
                            changePrice();
                        } else {
                            document.getElementById(baseId + ':chargeMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "none";
                        }
                    }
                }
            } else if (eventIdList[1] == "teikiMenu" && eventIdList.length >= 5) {
                // 定期券メニュー
                if (priceChangeFlag && priceChangeRefreshFlag) {
                    if (eventIdList[4] == "open") {
                        if (document.getElementById(baseId + ':teikiMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display == "none") {
                            document.getElementById(baseId + ':teikiMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "block";
                        } else {
                            document.getElementById(baseId + ':teikiMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "none";
                        }
                    } else if (eventIdList[4] == "close") {
                        document.getElementById(baseId + ':teikiMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "none";
                    } else {
                        if (priceChangeFlag) {
                            document.getElementById(baseId + ':teiki:' + eventIdList[2] + ':' + eventIdList[3]).value = eventIdList[4];
                            if (courseDisplayAll) {
                                selectNo = parseInt(eventIdList[2]);
                            }
                            changePrice();
                        } else {
                            document.getElementById(baseId + ':teikiMenu:' + eventIdList[2] + ':' + eventIdList[3]).style.display = "none";
                        }
                    }
                }
            } else if ((eventIdList[1] == "prevDia" || eventIdList[1] == "prevDia2") && eventIdList.length >= 3) {
                if (courseDisplayAll) {
                    selectNo = parseInt(eventIdList[2]);
                }
                assignDia("prev");
            } else if ((eventIdList[1] == "nextDia" || eventIdList[1] == "nextDia2") && eventIdList.length >= 3) {
                if (courseDisplayAll) {
                    selectNo = parseInt(eventIdList[2]);
                }
                assignDia("next");
            } else if (eventIdList[1] == "link" && eventIdList.length >= 3) {
                if (eventIdList[2] == "resultList") {
                    if (!courseDisplayAll) {
                        dispCourseList();
                    }
                    if (typeof callBackFunctionBind['click'] == 'function') {
                        callBackFunctionBind['click'](true);
                    }
                }
            } else if (eventIdList[1] == "sort" && eventIdList.length >= 3) {
                sortType = eventIdList[2];
                // 最適経路のチェック
                checkCourseList();
                // 経路を出力
                viewResultList();
                if (typeof callBackFunctionBind['click'] == 'function') {
                    callBackFunctionBind['click'](true);
                }
            }
        }
    }

    /**
    * 探索結果をソートする
    */
    function sortCourse(sortType) {
        sortCourseList.sort(function (a, b) {
            if (sortType == "ekispert") {
                if (a.index > b.index) return 1;
                if (a.index < b.index) return -1;
                return 0;
            } else if (sortType == "time") {
                if (resultSearchType == "departure" || resultSearchType == "firstTrain") {
                    if (a.arrival > b.arrival || a.arrival === b.arrival && a.departure < b.departure) return 1;
                    if (a.arrival < b.arrival || a.arrival === b.arrival && a.departure > b.departure) return -1;
                    return 0;
                } else if (resultSearchType == "arrival" || resultSearchType == "lastTrain") {
                    if (a.departure < b.departure || a.departure === b.departure && a.arrival > b.arrival) return 1;
                    if (a.departure > b.departure || a.departure === b.departure && a.arrival < b.arrival) return -1;
                    return 0;
                } else {
                    if (a.timeSummary > b.timeSummary) return 1;
                    if (a.timeSummary < b.timeSummary) return -1;
                    return 0;
                }
            } else if (sortType == "price" && priceViewFlag == "oneway") {
                if (a.priceSummary > b.priceSummary) return 1;
                if (a.priceSummary < b.priceSummary) return -1;
                return 0;
            } else if (sortType == "price" && priceViewFlag == "round") {
                if (a.priceRoundSummary > b.priceRoundSummary) return 1;
                if (a.priceRoundSummary < b.priceRoundSummary) return -1;
                return 0;
            } else if (sortType == "price" && priceViewFlag == "teiki") {
                if (a.teikiSummary > b.teikiSummary) return 1;
                if (a.teikiSummary < b.teikiSummary) return -1;
                return 0;
            } else if (sortType == "transfer") {
                if (a.transferCount > b.transferCount) return 1;
                if (a.transferCount < b.transferCount) return -1;
                return 0;
            } else {
                if (a.index > b.index) return 1;
                if (a.index < b.index) return -1;
                return 0;
            }
        });
    }

    /**
    * 分を時+分表記に変更する
    */
    function fun2ji(num) {
        var hour = Math.floor(num / 60);
        var minute = num % 60;
        if (hour > 0) {
            if (minute == 0) {
                return hour + "時間";
            } else {
                return hour + "時間" + minute + "分";
            }
        } else {
            return minute + "分";
        }
    }

    /**
    * 駅のマーク種別を取得する
    */
    function getStationType(tmpStationType) {
        for (var i = 0; i < tmpStationType.length; i++) {
            if (tmpStationType[i] == "back") {
                return 3; // 戻る
            } else if (tmpStationType[i] == "extension" || tmpStationType[i] == "pass") {
                return 4; // 乗り入れ・通過
            }
        }
        return 2; // 通常
    }

    /**
    * コースオブジェクトを経路に展開
    */
    function viewResultRoute(courseObj, routeNo) {
        var buffer = "";
        buffer += '<div class="exp_route">';
        // 経路一覧に戻るリンク
        if (courseDisplayAll || !resultTab) {
            buffer += '<div class="exp_resultTopLink">';
            buffer += '<a class="exp_resultTopLinkArea" id="' + baseId + ':link:resultList" href="Javascript:void(0);"><span class="exp_text" id="' + baseId + ':link:resultList:text">経路一覧へ</span></a>';
            buffer += '</div>';
        }
        // サマリー
        buffer += outSummary(courseObj, routeNo);
        // 前後のダイヤで探索
        if (courseObj.dataType == "onTimetable" && assignDiaFlag) {
            buffer += '<div class="exp_routeHeader exp_clearfix">';
            if (agent == 1) {
                buffer += '<div class="exp_assignButton exp_left">';
                buffer += '<a id="' + baseId + ':prevDia:' + String(routeNo) + '" href="Javascript:void(0);"><span class="exp_text" id="' + baseId + ':prevDia:' + String(routeNo) + ':text">一本前</span></a>';
                buffer += '</div>';
                buffer += '<div class="exp_assignButton exp_right">';
                buffer += '<a id="' + baseId + ':nextDia:' + String(routeNo) + '" href="Javascript:void(0);"><span class="exp_text" id="' + baseId + ':nextDia:' + String(routeNo) + ':text">一本後</span></a>';
                buffer += '</div>';
            } else if (agent == 2 || agent == 3) {
                buffer += '<span class="exp_assign exp_left"><a class="exp_prev" id="' + baseId + ':prevDia:' + String(routeNo) + '" href="Javascript:void(0);">一本前</a></span>';
                buffer += '<span class="exp_assign exp_right"><a class="exp_next" id="' + baseId + ':nextDia:' + String(routeNo) + '" href="Javascript:void(0);">一本後</a></span>';
            }
            // テキスト
            var DepartureTime, ArrivalTime;
            if (typeof courseObj.Route.Line.length == 'undefined') {
                if (typeof courseObj.Route.Line.DepartureState.Datetime.text != 'undefined') {
                    DepartureTime = convertISOtoDate(courseObj.Route.Line.DepartureState.Datetime.text);
                }
                if (typeof courseObj.Route.Line.ArrivalState.Datetime.text != 'undefined') {
                    ArrivalTime = convertISOtoDate(courseObj.Route.Line.ArrivalState.Datetime.text);
                }
            } else {
                if (typeof courseObj.Route.Line[0].DepartureState.Datetime.text != 'undefined') {
                    DepartureTime = convertISOtoDate(courseObj.Route.Line[0].DepartureState.Datetime.text);
                }
                if (typeof courseObj.Route.Line[courseObj.Route.Line.length - 1].ArrivalState.Datetime.text != 'undefined') {
                    ArrivalTime = convertISOtoDate(courseObj.Route.Line[courseObj.Route.Line.length - 1].ArrivalState.Datetime.text);
                }
            }
            if (agent == 2) {
                buffer += '<div class="exp_headerText">' + String(DepartureTime.getHours()) + ':' + (DepartureTime.getMinutes() < 10 ? '0' : '') + String(DepartureTime.getMinutes()) + '発</div>';
            } else if (agent == 3) {
                buffer += '<div class="exp_headerText">' + String(DepartureTime.getHours()) + ':' + (DepartureTime.getMinutes() < 10 ? '0' : '') + String(DepartureTime.getMinutes()) + '発～' + String(ArrivalTime.getHours()) + ':' + (ArrivalTime.getMinutes() < 10 ? '0' : '') + String(ArrivalTime.getMinutes()) + '着' + '</div>';
            }
            buffer += '</div>';
        }

        // まずは配列を作成
        var point = new Array();
        var line = new Array();
        for (var i = 0; i < courseObj.Route.Point.length; i++) {
            point.push(courseObj.Route.Point[i]);
        }
        if (typeof courseObj.Route.Line.length == 'undefined') {
            line.push(courseObj.Route.Line);
        } else {
            for (var i = 0; i < courseObj.Route.Line.length; i++) {
                line.push(courseObj.Route.Line[i]);
            }
        }
        // 金額の配列
        var fare = new Array();
        var charge = new Array();
        var teiki1 = new Array();
        var teiki3 = new Array();
        var teiki6 = new Array();
        var teiki12 = new Array();
        var teiki = new Array();
        if (typeof courseObj.Price != 'undefined') {
            for (var i = 0; i < courseObj.Price.length; i++) {
                if (courseObj.Price[i].kind == "Fare") {
                    // 乗車券のリスト作成
                    fare.push(courseObj.Price[i]);
                } else if (courseObj.Price[i].kind == "Charge") {
                    // 特急券のリスト作成
                    charge.push(courseObj.Price[i]);
                } else if (courseObj.Price[i].kind == "Teiki1") {
                    // 定期券のリスト作成
                    teiki1.push(courseObj.Price[i]);
                } else if (courseObj.Price[i].kind == "Teiki3") {
                    // 定期券のリスト作成
                    teiki3.push(courseObj.Price[i]);
                } else if (courseObj.Price[i].kind == "Teiki6") {
                    // 定期券のリスト作成
                    teiki6.push(courseObj.Price[i]);
                } else if (courseObj.Price[i].kind == "Teiki12") {
                    // 定期券のリスト作成
                    teiki12.push(courseObj.Price[i]);
                }
            }
        }
        // 複数の定期
        if (typeof courseObj.PassStatus != 'undefined') {
            for (var i = 0; i < courseObj.PassStatus.length; i++) {
                teiki.push(courseObj.PassStatus[i]);
            }
        }
        // 経路本体
        buffer += '<div class="exp_detail exp_clearfix">';
        for (var i = 0; i < point.length; i++) {
            // 金額区間の終了
            if (priceViewFlag == "oneway" || priceViewFlag == "round") {
                // 運賃の終わり
                for (var j = 0; j < fare.length; j++) {
                    if (parseInt(fare[j].toLineIndex) == i && fare[j].selected == "true") {
                        buffer += '</div>';
                        break;
                    }
                }
            } else if (priceViewFlag == "teiki") {
                // 定期券の終わり
                for (var j = 0; j < teiki1.length; j++) {
                    if (parseInt(teiki1[j].toLineIndex) == i && teiki1[j].selected == "true") {
                        buffer += '</div>';
                    }
                }
            }
            // 運賃の出力
            if (priceViewFlag == "oneway" || priceViewFlag == "round") {
                // 乗車券
                var fareList = new Array();
                for (var j = 0; j < fare.length; j++) {
                    // 対象となる乗車券をセット
                    if (parseInt(fare[j].fromLineIndex) == (i + 1)) {
                        fareList.push(fare[j]);
                    }
                }
                if (fareList.length > 0) {
                    // 1つだけ表示
                    for (var j = 0; j < fareList.length; j++) {
                        if (fareList[j].selected == "true") {
                            // 値を出力
                            if (fareList[j].Type == "WithTeiki") {
                                buffer += '<div class="exp_fareTeikiValue">';
                                // buffer += '<div class="exp_cost">定期券区間<div class="exp_top"></div></div>';
                                buffer += '<div class="exp_cost">定期券区間</div>';
                                buffer += '</div>';
                            } else {
                                buffer += '<div class="exp_fareValue">';
                                buffer += '<div class="exp_cost">';
                                var fareName = "";
                                if (typeof fareList[j].Name != 'undefined') {
                                    fareName += fareList[j].Name + (agent == 2 ? "<br>" : "&nbsp;");
                                } else if (fareList.length >= 2) {
                                    fareName += "指定なし" + (agent == 2 ? "<br>" : "&nbsp;");
                                } else {
                                    fareName += "乗車券" + (agent == 2 ? "<br>" : "&nbsp;");
                                }
                                // 運賃改定未対応
                                var salesTaxRateIsNotSupported = false;
                                if (typeof fareList[j].RevisionStatus != 'undefined') {
                                    if (fareList[j].RevisionStatus != 'latest') {
                                        salesTaxRateIsNotSupported = true;
                                    }
                                }
                                fareName += '<span class="' + (salesTaxRateIsNotSupported ? 'exp_taxRateIsNotSupportedLine' : 'exp_linePrice') + '" id="' + baseId + ':fareMenu:' + String(routeNo) + ':' + String(i + 1) + ':open:2">';
                                if (priceViewFlag == "oneway") {
                                    fareName += num2String(parseInt(getTextValue(fareList[j].Oneway))) + '円';
                                } else if (priceViewFlag == "round") {
                                    fareName += num2String(parseInt(getTextValue(fareList[j].Round))) + '円';
                                }
                                fareName += '</span>';
                                if (fareList.length >= 2) {
                                    if (agent == 1) {
                                        // 選択している値
                                        buffer += '<input type="hidden" id="' + baseId + ':fare:' + String(routeNo) + ':' + String(i + 1) + '" value="' + fareList[j].index + '">';
                                        // 2つ以上ある場合はメニューのリンクを設置
                                        if (priceChangeFlag) {
                                            buffer += '<span class="exp_priceMenu"><a id="' + baseId + ':fareMenu:' + String(routeNo) + ':' + String(i + 1) + ':open" href="Javascript:void(0);">' + fareName + '▼</a></span>';
                                        } else {
                                            buffer += fareName;
                                        }
                                    } else if (agent == 2 || agent == 3) {
                                        // スマホ・タブレット用
                                        buffer += '<div class="exp_fareSelect">';
                                        buffer += '<div class="exp_fareSelectText">';
                                        buffer += fareName + (priceChangeFlag ? "▼" : "");
                                        buffer += '</div>';
                                        if (priceChangeFlag) {
                                            buffer += '<select id="' + baseId + ':fareSelect:' + String(routeNo) + ':' + fareList[j].fromLineIndex + '">';
                                            for (var k = 0; k < fareList.length; k++) {
                                                buffer += '<option value="' + fareList[k].index + '"' + ((fareList[k].selected == "true") ? "selected" : "") + '>';
                                                if (typeof fareList[k].Name != 'undefined') {
                                                    buffer += fareList[k].Name + ":";
                                                } else {
                                                    buffer += "指定なし:";
                                                }
                                                if (priceViewFlag == "oneway") {
                                                    buffer += num2String(parseInt(getTextValue(fareList[k].Oneway))) + '円';
                                                } else if (priceViewFlag == "round") {
                                                    buffer += num2String(parseInt(getTextValue(fareList[k].Round))) + '円';
                                                }
                                                buffer += '</option>';
                                            }
                                            buffer += '</select>';
                                        }
                                        buffer += '</div>';
                                    }
                                } else {
                                    buffer += fareName;
                                }
                                //              buffer += '<div class="exp_top"></div>';
                                buffer += '</div>';
                                buffer += '</div>';
                            }
                            // メニュー本体
                            if (agent == 1 && fareList.length >= 2) {
                                buffer += '<div class="exp_menu exp_fareWindow" id="' + baseId + ':fareMenu:' + String(routeNo) + ':' + String(i + 1) + '" style="display:none;">';
                                buffer += '<div class="exp_header exp_clearfix">';
                                buffer += '<span class="exp_title">乗車券</span>';
                                buffer += '<span class="exp_close">';
                                buffer += '<a class="exp_link" id="' + baseId + ':fareMenu:' + String(routeNo) + ':' + String(i + 1) + ':close" href="Javascript:void(0);">×</a>';
                                buffer += '</span>';
                                buffer += '</div>';
                                buffer += '<div class="exp_body">';
                                buffer += '<div class="exp_list">';
                                // メニュー
                                for (var k = 0; k < fareList.length; k++) {
                                    buffer += '<div class="exp_item' + (fareList[k].selected == "true" ? " exp_checked" : "") + ' exp_' + (k % 2 == 0 ? 'odd' : 'even') + '">';
                                    buffer += '<a href="Javascript:void(0);" id="' + baseId + ':fareMenu:' + String(routeNo) + ':' + String(i + 1) + ':' + String(fareList[k].index) + '">';
                                    // 金額
                                    buffer += '<span class="exp_costList" id="' + baseId + ':fareMenu:' + String(routeNo) + ':' + String(i + 1) + ':' + String(fareList[k].index) + ':cost">';
                                    if (priceViewFlag == "oneway") {
                                        buffer += num2String(parseInt(getTextValue(fareList[k].Oneway))) + '円';
                                    } else if (priceViewFlag == "round") {
                                        buffer += num2String(parseInt(getTextValue(fareList[k].Round))) + '円';
                                    }
                                    buffer += '</span>';
                                    buffer += ((typeof fareList[k].Name != 'undefined') ? fareList[k].Name : "指定なし") + '&nbsp;</a></div>';
                                }
                                buffer += '</div>';
                                buffer += '</div>';
                                buffer += '</div>';
                            }
                        }
                    }
                }
            } else if (priceViewFlag == "teiki") {
                // 定期券の出力
                var teiki1List = new Array();
                var teiki3List = new Array();
                var teiki6List = new Array();
                var teiki12List = new Array();
                // 対象となる定期券をセット
                for (var j = 0; j < teiki1.length; j++) {
                    if (parseInt(teiki1[j].fromLineIndex) == (i + 1)) {
                        teiki1List.push(teiki1[j]);
                    }
                }
                for (var j = 0; j < teiki3.length; j++) {
                    if (parseInt(teiki3[j].fromLineIndex) == (i + 1)) {
                        teiki3List.push(teiki3[j]);
                    }
                }
                for (var j = 0; j < teiki6.length; j++) {
                    if (parseInt(teiki6[j].fromLineIndex) == (i + 1)) {
                        teiki6List.push(teiki6[j]);
                    }
                }
                for (var j = 0; j < teiki12.length; j++) {
                    if (parseInt(teiki12[j].fromLineIndex) == (i + 1)) {
                        teiki12List.push(teiki12[j]);
                    }
                }
                if (teiki1List.length > 0 || teiki3List.length > 0 || teiki6List.length > 0 || teiki12List.length > 0) {
                    // 1つだけ表示
                    for (var j = 0; j < teiki1List.length; j++) {
                        // 定期のチェック
                        var teikiIndex = 0;
                        var teikiName = "";
                        var teikiKind = "";
                        for (var k = 0; k < teiki.length; k++) {
                            if (teiki[k].teiki1Index == teiki1List[j].index) {
                                // 選択している値
                                if (teiki[k].selected == "true") {
                                    teikiIndex = k + 1;
                                    teikiName = teiki[k].Name;
                                    teikiKind = teiki[k].kind;
                                }
                            }
                        }
                        // 値を出力
                        buffer += '<div class="exp_teikiValue">';
                        buffer += '<div class="exp_cost">';
                        buffer += '<div class="exp_name">';
                        if (agent == 1) {
                            if (teikiIndex == 0 || !priceChangeFlag || !priceChangeRefreshFlag) {
                                buffer += (teikiName != "" ? teikiName : "定期");
                            } else {
                                // 2つ以上ある場合はメニューのリンクを設置
                                buffer += '<span class="exp_priceMenu"><a id="' + baseId + ':teikiMenu:' + String(routeNo) + ':' + String(i + 1) + ':open" href="Javascript:void(0);">' + (teikiName != "" ? teikiName : "定期") + '▼</a></span>';
                            }
                        } else if (agent == 2 || agent == 3) {
                            if (teikiIndex == 0 || !priceChangeFlag || !priceChangeRefreshFlag) {
                                buffer += (teikiName != "" ? teikiName : "定期");
                            } else {
                                // 定期が複数あった場合のフォーム出力
                                buffer += '<div class="exp_teikiSelect">';
                                buffer += '<div class="exp_teikiSelectText">' + teikiName + '▼</div>';
                                buffer += '<input type="hidden" id="' + baseId + ':teikiKind:' + String(routeNo) + ':' + String(i + 1) + '" value="' + teikiKind + '">';
                                buffer += '<select id="' + baseId + ':teikiSelect:' + String(routeNo) + ':' + String(i + 1) + '" value="' + String(teikiIndex) + '">';
                                for (var k = 0; k < teiki.length; k++) {
                                    if (teiki[k].teiki1Index == teiki1List[j].index) {
                                        buffer += '<option value="' + String(k + 1) + '"' + (teiki[k].selected == "true" ? " selected" : "") + '>';
                                        buffer += String(teiki[k].Name);
                                        buffer += '</option>';
                                    }
                                }
                                buffer += '</select>';
                                buffer += '</div>';
                            }
                        }
                        buffer += '</div>';
                        buffer += '<div class="exp_teiki1">' + (agent != 2 ? '1ヵ月' : '');
                        if (typeof teiki1List[j] != 'undefined') {
                            // 運賃改定未対応
                            var salesTaxRateIsNotSupported = false;
                            if (typeof teiki1List[j].RevisionStatus != 'undefined') {
                                if (teiki1List[j].RevisionStatus != 'latest') {
                                    salesTaxRateIsNotSupported = true;
                                }
                            }
                            buffer += '<span class="' + (salesTaxRateIsNotSupported ? 'exp_taxRateIsNotSupportedLine' : 'exp_linePrice') + '">';
                            if (getTextValue(teiki1List[j].Name) != "") {
                                buffer += getTextValue(teiki1List[j].Name);
                            } else {
                                buffer += num2String(parseInt(getTextValue(teiki1List[j].Oneway))) + '円';
                            }
                            buffer += '</span>';
                        } else {
                            buffer += '------円';
                        }
                        buffer += '</div>';
                        buffer += '<div class="exp_teiki3">' + (agent != 2 ? '3ヵ月' : '');
                        if (typeof teiki3List[j] != 'undefined') {
                            // 運賃改定未対応
                            var salesTaxRateIsNotSupported = false;
                            if (typeof teiki3List[j].RevisionStatus != 'undefined') {
                                if (teiki3List[j].RevisionStatus != 'latest') {
                                    salesTaxRateIsNotSupported = true;
                                }
                            }
                            buffer += '<span class="' + (salesTaxRateIsNotSupported ? 'exp_taxRateIsNotSupportedLine' : 'exp_linePrice') + '">';
                            if (getTextValue(teiki3List[j].Name) != "") {
                                buffer += getTextValue(teiki3List[j].Name);
                            } else {
                                buffer += num2String(parseInt(getTextValue(teiki3List[j].Oneway))) + '円';
                            }
                            buffer += '</span>';
                        } else {
                            buffer += '------円';
                        }
                        buffer += '</div>';
                        buffer += '<div class="exp_teiki6">' + (agent != 2 ? '6ヵ月' : '');
                        if (typeof teiki6List[j] != 'undefined') {
                            // 運賃改定未対応
                            var salesTaxRateIsNotSupported = false;
                            if (typeof teiki6List[j].RevisionStatus != 'undefined') {
                                if (teiki6List[j].RevisionStatus != 'latest') {
                                    salesTaxRateIsNotSupported = true;
                                }
                            }
                            buffer += '<span class="' + (salesTaxRateIsNotSupported ? 'exp_taxRateIsNotSupportedLine' : 'exp_linePrice') + '">';
                            if (getTextValue(teiki6List[j].Name) != "") {
                                buffer += getTextValue(teiki6List[j].Name);
                            } else {
                                buffer += num2String(parseInt(getTextValue(teiki6List[j].Oneway))) + '円';
                            }
                            buffer += '</span>';
                        } else {
                            buffer += '------円';
                        }
                        buffer += '</div>';
                        buffer += '<div class="exp_teiki12">' + (agent != 2 ? '12ヵ月' : '');
                        if (typeof teiki12List[j] != 'undefined') {
                            // 運賃改定未対応
                            var salesTaxRateIsNotSupported = false;
                            if (typeof teiki12List[j].RevisionStatus != 'undefined') {
                                if (teiki12List[j].RevisionStatus != 'latest') {
                                    salesTaxRateIsNotSupported = true;
                                }
                            }
                            buffer += '<span class="' + (salesTaxRateIsNotSupported ? 'exp_taxRateIsNotSupportedLine' : 'exp_linePrice') + '">';
                            if (getTextValue(teiki12List[j].Name) != "") {
                                buffer += getTextValue(teiki12List[j].Name);
                            } else {
                                buffer += num2String(parseInt(getTextValue(teiki12List[j].Oneway))) + '円';
                            }
                            buffer += '</span>';
                        } else {
                            buffer += '------円';
                        }
                        buffer += '</div>';
                        //          buffer += '<div class="exp_top"></div>';
                        buffer += '</div>';
                        buffer += '</div>';
                        if (teikiIndex > 0) {
                            if (agent == 1) {
                                buffer += '<input type="hidden" id="' + baseId + ':teiki:' + String(routeNo) + ':' + String(i + 1) + '" value="' + String(teikiIndex) + '">';
                                // タイプを入れる
                                buffer += '<input type="hidden" id="' + baseId + ':teikiKind:' + String(routeNo) + ':' + String(i + 1) + '" value="' + teikiKind + '">';
                                // メニュー本体
                                buffer += '<div class="exp_menu exp_teikiWindow" id="' + baseId + ':teikiMenu:' + String(routeNo) + ':' + String(i + 1) + '" style="display:none;">';
                                buffer += '<div class="exp_header exp_clearfix">';
                                buffer += '<span class="exp_title">定期</span>';
                                buffer += '<span class="exp_close">';
                                buffer += '<a class="exp_link" id="' + baseId + ':teikiMenu:' + String(routeNo) + ':' + String(i + 1) + ':close" href="Javascript:void(0);">×</a>';
                                buffer += '</span>';
                                buffer += '</div>';
                                buffer += '<div class="exp_body">';
                                buffer += '<div class="exp_list">';
                                // メニュー
                                var menuCount = 0;
                                for (var k = 0; k < teiki.length; k++) {
                                    if (teiki[k].teiki1Index == teiki1List[j].index) {
                                        buffer += '<div class="exp_item' + (teiki[k].selected == "true" ? " exp_checked" : "") + ' exp_' + (menuCount % 2 == 0 ? 'odd' : 'even') + '"><a href="Javascript:void(0);" id="' + baseId + ':teikiMenu:' + String(routeNo) + ':' + String(i + 1) + ':' + String(k + 1) + '">&nbsp;' + String(teiki[k].Name) + '&nbsp;</a></div>';
                                        menuCount++;
                                    }
                                }
                                buffer += '</div>';
                                buffer += '</div>';
                                buffer += '</div>';
                            }
                        }
                    }
                }
            }

            // 駅の出力
            var stationType = "transfer";
            if (i == 0) {
                stationType = "start";
            } else if (i == point.length - 1) {
                stationType = "end";
            }
            buffer += outStation(routeNo, i, point[i], line[i - 1], line[i], courseObj.dataType, stationType);
            // 運賃の開始
            if (priceViewFlag == "oneway" || priceViewFlag == "round") {
                if (fareList.length > 0) {
                    buffer += '<div class="exp_priceSection">';
                    buffer += '<div class="exp_priceData">';
                    if (fareList[0].Type == "WithTeiki") {
                        buffer += '<div class="exp_teiki">';
                    } else {
                        buffer += '<div class="exp_fare">';
                    }
                    buffer += '<div class="exp_bar"><div class="exp_base"><div class="exp_color"></div></div></div>';
                    //buffer += '<div class="exp_end"></div>';
                    buffer += '</div>';
                    buffer += '</div>';
                }
            } else if (priceViewFlag == "teiki") {
                if (teiki1List.length > 0 || teiki3List.length > 0 || teiki6List.length > 0 || teiki12List.length > 0) {
                    buffer += '<div class="exp_priceSection">';
                    buffer += '<div class="exp_priceData">';
                    buffer += '<div class="exp_teiki">';
                    buffer += '<div class="exp_bar"><div class="exp_base"><div class="exp_color"></div></div></div>';
                    //buffer += '<div class="exp_end"></div>';
                    buffer += '</div>';
                    buffer += '</div>';
                }
            }
            // 路線の出力
            if (typeof line[i] != 'undefined') {
                var chargeList = new Array();
                // 特急券の設定
                if (priceViewFlag == "oneway" || priceViewFlag == "round") {
                    for (var j = 0; j < charge.length; j++) {
                        // 対象となる特急券をセット
                        if (parseInt(charge[j].fromLineIndex) == (i + 1)) {
                            chargeList.push(charge[j]);
                        }
                    }
                }
                // 出力
                buffer += outLine(routeNo, i, line[i], chargeList);
            }
        }

        // フッター
        if (agent == 2 || agent == 3) {
            if (courseObj.dataType == "onTimetable" && assignDiaFlag) {
                buffer += '<div class="exp_routeHeader exp_clearfix">';
                buffer += '<span class="exp_assign exp_right"><a class="exp_next" id="' + baseId + ':nextDia2:' + String(routeNo) + '" href="Javascript:void(0);">一本後</a></span>';
                buffer += '<span class="exp_assign exp_left"><a class="exp_prev" id="' + baseId + ':prevDia2:' + String(routeNo) + '" href="Javascript:void(0);">一本前</a></span>';
                // テキスト
                var DepartureTime, ArrivalTime;
                if (typeof courseObj.Route.Line.length == 'undefined') {
                    if (typeof courseObj.Route.Line.DepartureState.Datetime.text != 'undefined') {
                        DepartureTime = convertISOtoDate(courseObj.Route.Line.DepartureState.Datetime.text);
                    }
                    if (typeof courseObj.Route.Line.ArrivalState.Datetime.text != 'undefined') {
                        ArrivalTime = convertISOtoDate(courseObj.Route.Line.ArrivalState.Datetime.text);
                    }
                } else {
                    if (typeof courseObj.Route.Line[0].DepartureState.Datetime.text != 'undefined') {
                        DepartureTime = convertISOtoDate(courseObj.Route.Line[0].DepartureState.Datetime.text);
                    }
                    if (typeof courseObj.Route.Line[courseObj.Route.Line.length - 1].ArrivalState.Datetime.text != 'undefined') {
                        ArrivalTime = convertISOtoDate(courseObj.Route.Line[courseObj.Route.Line.length - 1].ArrivalState.Datetime.text);
                    }
                }
                if (agent == 2) {
                    buffer += '<div class="exp_headerText">' + String(ArrivalTime.getHours()) + ':' + (ArrivalTime.getMinutes() < 10 ? '0' : '') + String(ArrivalTime.getMinutes()) + '着' + '</div>';
                } else if (agent == 3) {
                    buffer += '<div class="exp_headerText">' + String(DepartureTime.getHours()) + ':' + (DepartureTime.getMinutes() < 10 ? '0' : '') + String(DepartureTime.getMinutes()) + '発～' + String(ArrivalTime.getHours()) + ':' + (ArrivalTime.getMinutes() < 10 ? '0' : '') + String(ArrivalTime.getMinutes()) + '着' + '</div>';
                }
                buffer += '</div>';
            }
        }
        // 確定ボタン
        if (typeof callBackFunctionBind['select'] == 'function') {
            buffer += '<div class="exp_footer">';
            buffer += '<div class="exp_resultSelect">';
            buffer += '<a class="exp_resultSelectButton" id="' + baseId + ':courseSelect:' + String(routeNo) + '" href="Javascript:void(0);"><span class="exp_text" id="' + baseId + ':courseSelect:' + String(routeNo) + ':text">経路確定</span></a>';
            buffer += '</div>';
            buffer += '</div>';
        }
        buffer += '</div>';
        buffer += '</div>';
        return buffer;
    }

    /**
    * サマリーを出力
    */
    function outSummary(courseObj, routeNo) {
        var buffer = "";
        buffer += '<div style=\'text-align:center;margin-top:10px;margin-bottom:2px\'><button class=\'btn ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button-text ui-state-hover\' style=\'width:180px;\' onclick=\'setRoute();\'><span class=\'ui-button-text\' style=\'font-size: 1.2em;\'>この経路を選択</span></button></div>';
        buffer += '<div class="exp_summary exp_clearfix">';
        buffer += '<div class="exp_row">';
        // 経路番号
        buffer += '<span class="exp_titleRouteNo">経路' + ((typeof routeNo != 'undefined') ? getCourseNo(routeNo) : "") + '</span>';
        // 出発日・到着日
        var departureDate;
        var arrivalDate;
        var week = new Array('日', '月', '火', '水', '木', '金', '土');
        if (typeof courseObj.Route.Line.length == 'undefined') {
            departureDate = convertISOtoDate(courseObj.Route.Line.DepartureState.Datetime.text);
            arrivalDate = convertISOtoDate(courseObj.Route.Line.ArrivalState.Datetime.text);
        } else {
            departureDate = convertISOtoDate(courseObj.Route.Line[0].DepartureState.Datetime.text);
            arrivalDate = convertISOtoDate(courseObj.Route.Line[courseObj.Route.Line.length - 1].ArrivalState.Datetime.text);
        }
        buffer += '<span class="exp_date">' + departureDate.getFullYear() + '年' + (departureDate.getMonth() + 1) + '月' + departureDate.getDate() + '日' + '(' + week[departureDate.getDay()] + ')</span>';
        // アイコン
        var time = parseInt(courseObj.Route.timeOnBoard) + parseInt(courseObj.Route.timeWalk) + parseInt(courseObj.Route.timeOther);
        var TransferCount = parseInt(courseObj.Route.transferCount);
        var FareSummary = 0;
        var FareRoundSummary = 0;
        var ChargeSummary;
        var ChargeRoundSummary;
        var Teiki1Summary;
        var Teiki3Summary;
        var Teiki6Summary;
        var Teiki12Summary;
        // 運賃改定未対応
        var FareSummarySalesTaxRateIsNotSupported = false;
        var ChargeSummarySalesTaxRateIsNotSupported = false;
        var Teiki1SummarySalesTaxRateIsNotSupported = false;
        var Teiki3SummarySalesTaxRateIsNotSupported = false;
        var Teiki6SummarySalesTaxRateIsNotSupported = false;
        var Teiki12SummarySalesTaxRateIsNotSupported = false;
        var revStatFlg = 0;
        if (typeof courseObj.Price != 'undefined') {
            for (var j = 0; j < courseObj.Price.length; j++) {
                if (courseObj.Price[j].kind == "FareSummary") {
                    if (typeof courseObj.Price[j].Oneway != 'undefined') {
                        FareSummary = parseInt(getTextValue(courseObj.Price[j].Oneway));
                    }
                    if (typeof courseObj.Price[j].Round != 'undefined') {
                        FareRoundSummary = parseInt(getTextValue(courseObj.Price[j].Round));
                    }
                } else if (courseObj.Price[j].kind == "ChargeSummary") {
                    if (typeof courseObj.Price[j].Oneway != 'undefined') {
                        ChargeSummary = parseInt(getTextValue(courseObj.Price[j].Oneway));
                    }
                    if (typeof courseObj.Price[j].Round != 'undefined') {
                        ChargeRoundSummary = parseInt(getTextValue(courseObj.Price[j].Round));
                    }
                } else if (courseObj.Price[j].kind == "Teiki1Summary") {
                    if (typeof courseObj.Price[j].Oneway != 'undefined') {
                        Teiki1Summary = parseInt(getTextValue(courseObj.Price[j].Oneway));
                    }
                } else if (courseObj.Price[j].kind == "Teiki3Summary") {
                    if (typeof courseObj.Price[j].Oneway != 'undefined') {
                        Teiki3Summary = parseInt(getTextValue(courseObj.Price[j].Oneway));
                    }
                } else if (courseObj.Price[j].kind == "Teiki6Summary") {
                    if (typeof courseObj.Price[j].Oneway != 'undefined') {
                        Teiki6Summary = parseInt(getTextValue(courseObj.Price[j].Oneway));
                    }
                } else if (courseObj.Price[j].kind == "Teiki12Summary") {
                    if (typeof courseObj.Price[j].Oneway != 'undefined') {
                        Teiki12Summary = parseInt(getTextValue(courseObj.Price[j].Oneway));
                    }
                }
                if (typeof courseObj.Price[j].RevisionStatus != 'undefined') {
                    if (courseObj.Price[j].RevisionStatus != 'latest') {
                        if (courseObj.Price[j].kind == "Fare") {
                            FareSummarySalesTaxRateIsNotSupported = true;
                            revStatFlg = judgeRevStatFlg(courseObj.Price[j].RevisionStatus, revStatFlg);
                        } else if (courseObj.Price[j].kind == "Charge") {
                            ChargeSummarySalesTaxRateIsNotSupported = true;
                            revStatFlg = judgeRevStatFlg(courseObj.Price[j].RevisionStatus, revStatFlg);
                        } else if (courseObj.Price[j].kind == "Teiki1") {
                            Teiki1SummarySalesTaxRateIsNotSupported = true;
                            revStatFlg = judgeRevStatFlg(courseObj.Price[j].RevisionStatus, revStatFlg);
                        } else if (courseObj.Price[j].kind == "Teiki3") {
                            Teiki3SummarySalesTaxRateIsNotSupported = true;
                            revStatFlg = judgeRevStatFlg(courseObj.Price[j].RevisionStatus, revStatFlg);
                        } else if (courseObj.Price[j].kind == "Teiki6") {
                            Teiki6SummarySalesTaxRateIsNotSupported = true;
                            revStatFlg = judgeRevStatFlg(courseObj.Price[j].RevisionStatus, revStatFlg);
                        } else if (courseObj.Price[j].kind == "Teiki12") {
                            Teiki12SummarySalesTaxRateIsNotSupported = true;
                            revStatFlg = judgeRevStatFlg(courseObj.Price[j].RevisionStatus, revStatFlg);
                        }
                    }
                }
            }
        }
        var salesTaxRateIsNotSupported = (FareSummarySalesTaxRateIsNotSupported || ChargeSummarySalesTaxRateIsNotSupported);
        // アイコン
        buffer += '<div class="exp_mark exp_clearfix">';
        if (typeof minEkispertIndex != 'undefined') {
            if (getDepartureState(result.ResultSet.Course[minEkispertIndex - 1]).getTime() == getDepartureState(courseObj).getTime() && getArrivalState(result.ResultSet.Course[minEkispertIndex - 1]).getTime() == getArrivalState(courseObj).getTime()) {
                buffer += '<span class="exp_hayai"></span>';
            }
        } else if (minTimeSummary == time) {
            buffer += '<span class="exp_hayai"></span>';
        }
        if (priceViewFlag == "oneway") {
            if (typeof ChargeSummary == 'undefined') {
                if (minPriceSummary == FareSummary) {
                    buffer += '<span class="exp_yasui"></span>';
                }
            } else {
                if (minPriceSummary == (FareSummary + ChargeSummary)) {
                    buffer += '<span class="exp_yasui"></span>';
                }
            }
        } else if (priceViewFlag == "round") {
            if (typeof ChargeRoundSummary == 'undefined') {
                if (minPriceRoundSummary == FareRoundSummary) {
                    buffer += '<span class="exp_yasui"></span>';
                }
            } else {
                if (minPriceRoundSummary == (FareRoundSummary + ChargeRoundSummary)) {
                    buffer += '<span class="exp_yasui"></span>';
                }
            }
        } else if (priceViewFlag == "teiki") {
            if (typeof Teiki6Summary != 'undefined') {
                if (minTeikiSummary == Teiki6Summary) {
                    buffer += '<span class="exp_yasui"></span>';
                }
            } else if (typeof Teiki3Summary != 'undefined') {

                if (minTeikiSummary == Teiki3Summary * 2) {
                    buffer += '<span class="exp_yasui"></span>';
                }
            } else if (typeof Teiki1Summary != 'undefined') {
                if (minTeikiSummary == Teiki1Summary * 6) {
                    buffer += '<span class="exp_yasui"></span>';
                }
            }
        }
        if (minTransferCount == TransferCount) {
            buffer += '<span class="exp_raku"></span>';
        }
        buffer += '</div>';
        buffer += '</div>';
        // セパレータ
        buffer += '<div class="exp_row exp_line">';
        buffer += '<span class="exp_title">所要時間</span>';
        buffer += '<span class="exp_value">';
        buffer += fun2ji(parseInt(courseObj.Route.timeOnBoard) + parseInt(courseObj.Route.timeWalk) + parseInt(courseObj.Route.timeOther));
        if (agent == 1 || agent == 3) {
            var tmp_timeStr = "";
            var timeCount = 0;
            // スマートフォンは非表示
            if (typeof courseObj.Route.timeOnBoard != 'undefined') {
                if (parseInt(courseObj.Route.timeOnBoard) > 0) {
                    tmp_timeStr += '乗車&nbsp;' + parseInt(courseObj.Route.timeOnBoard) + '分';
                    timeCount++;
                }
            }
            if (typeof courseObj.Route.timeOther != 'undefined') {
                if (parseInt(courseObj.Route.timeOther) > 0) {
                    if (tmp_timeStr != "") { tmp_timeStr += "、"; }
                    tmp_timeStr += '他&nbsp;' + parseInt(courseObj.Route.timeOther) + '分';
                    timeCount++;
                }
            }
            if (typeof courseObj.Route.timeWalk != 'undefined') {
                if (parseInt(courseObj.Route.timeWalk) > 0) {
                    if (tmp_timeStr != "") { tmp_timeStr += "、"; }
                    tmp_timeStr += '徒歩&nbsp;' + parseInt(courseObj.Route.timeWalk) + '分';
                    timeCount++;
                }
            }
            if (timeCount >= 2) {
                buffer += '<span class="exp_valueDetail">';
                buffer += "(" + tmp_timeStr + ")";
                buffer += '</span>';
            }
        }
        buffer += '</span>';
        buffer += '<span class="exp_title">距離</span>';
        buffer += '<span class="exp_value">';
        if (parseInt(courseObj.Route.distance) >= 10) {
            buffer += (parseInt(courseObj.Route.distance) / 10) + "km";
        } else {
            buffer += parseInt(courseObj.Route.distance) * 100 + "m";
        }
        buffer += '</span>';
        if (priceViewFlag == "teiki") {
            buffer += '<span class="exp_title">乗り換え</span>';
            buffer += '<span class="exp_value">';
            if (TransferCount > 0) {
                buffer += String(TransferCount) + '回';
            } else {
                buffer += 'なし';
            }
            buffer += '</span>';
        }
        buffer += '</div>';
        // 改行
        buffer += '<div class="exp_row">';
        if (priceViewFlag == "oneway") {
            buffer += '<span class="exp_title">運賃</span>';
            buffer += '<span class="exp_value">';
            if (typeof ChargeSummary == 'undefined') {
                buffer += salesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported">' : '';
                buffer += num2String(FareSummary) + '円';
                buffer += salesTaxRateIsNotSupported ? '</span>' : '';
            } else {
                buffer += salesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported">' : '';
                buffer += num2String(FareSummary + ChargeSummary) + '円';
                buffer += salesTaxRateIsNotSupported ? '</span>' : '';
                if (agent == 1 || agent == 3) {
                    buffer += '<span class="exp_valueDetail">';
                    buffer += '(乗車券&nbsp;' + num2String(FareSummary) + '円&nbsp;料金' + num2String(ChargeSummary) + '円)';
                    buffer += '</span>';
                }
            }
            buffer += '</span>';
        } else if (priceViewFlag == "round") {
            buffer += '<span class="exp_title">往復運賃</span>';
            buffer += '<span class="exp_value">';
            if (typeof ChargeSummary == 'undefined') {
                buffer += salesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported">' : '';
                buffer += num2String(FareRoundSummary) + '円';
                buffer += salesTaxRateIsNotSupported ? '</span>' : '';
            } else {
                buffer += salesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported">' : '';
                buffer += num2String(FareRoundSummary + ChargeRoundSummary) + '円';
                buffer += salesTaxRateIsNotSupported ? '</span>' : '';
                if (agent == 1 || agent == 3) {
                    buffer += '<span class="exp_detail">';
                    buffer += '(乗車券&nbsp;' + num2String(FareRoundSummary) + '円&nbsp;料金' + num2String(ChargeRoundSummary) + '円)';
                    buffer += '</span>';
                }
            }
            buffer += '</span>';
        } else if (priceViewFlag == "teiki") {
            buffer += '<span class="exp_titleTeiki1">定期1ヵ月</span>';
            buffer += '<span class="exp_value">';
            if (typeof Teiki1Summary != 'undefined') {
                buffer += Teiki1SummarySalesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported">' : '';
                buffer += num2String(Teiki1Summary) + '円';
                buffer += Teiki1SummarySalesTaxRateIsNotSupported ? '</span>' : '';
            } else {
                buffer += '------円';
            }
            buffer += '</span>';
            buffer += '<span class="exp_titleTeiki3">定期3ヵ月</span>';
            buffer += '<span class="exp_value">';
            if (typeof Teiki3Summary != 'undefined') {
                buffer += Teiki3SummarySalesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported">' : '';
                buffer += num2String(Teiki3Summary) + '円';
                buffer += Teiki3SummarySalesTaxRateIsNotSupported ? '</span>' : '';
            } else {
                buffer += '------円';
            }
            buffer += '</span>';
            buffer += '<span class="exp_titleTeiki6">定期6ヵ月</span>';
            buffer += '<span class="exp_value">';
            if (typeof Teiki6Summary != 'undefined') {
                buffer += Teiki6SummarySalesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported">' : '';
                buffer += num2String(Teiki6Summary) + '円';
                buffer += Teiki6SummarySalesTaxRateIsNotSupported ? '</span>' : '';
            } else {
                buffer += '------円';
            }
            buffer += '</span>';
            buffer += '<span class="exp_titleTeiki12">定期12ヵ月</span>';
            buffer += '<span class="exp_value">';
            if (typeof Teiki12Summary != 'undefined') {
                buffer += Teiki12SummarySalesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported">' : '';
                buffer += num2String(Teiki12Summary) + '円';
                buffer += Teiki12SummarySalesTaxRateIsNotSupported ? '</span>' : '';
            } else {
                buffer += '------円';
            }
            buffer += '</span>';
        }
        if (priceViewFlag == "oneway" || priceViewFlag == "round") {
            buffer += '<span class="exp_title">乗り換え</span>';
            buffer += '<span class="exp_value">';
            if (TransferCount > 0) {
                buffer += String(TransferCount) + '回';
            } else {
                buffer += 'なし';
            }
            buffer += '</span>';
        }

        buffer += '</div>';
        buffer += '</div>';
        // 運賃改定未対応
        if (priceViewFlag == "oneway" || priceViewFlag == "round") {
            if (salesTaxRateIsNotSupported) {
                buffer += '<div class="exp_fareRevisionStatus exp_clearfix">';
                buffer += '※' + revStatFlg2Msg(revStatFlg);
                buffer += '</div>';
            }
        } else if (priceViewFlag == "teiki") {
            if (Teiki1SummarySalesTaxRateIsNotSupported || Teiki3SummarySalesTaxRateIsNotSupported || Teiki6SummarySalesTaxRateIsNotSupported || Teiki12SummarySalesTaxRateIsNotSupported) {
                buffer += '<div class="exp_fareRevisionStatus exp_clearfix">';
                buffer += '※' + revStatFlg2Msg(revStatFlg);
                buffer += '</div>';
            }
        }
        return buffer;
    }

    /**
    * 駅を出力
    */
    function outStation(routeNo, index, point, arrLine, depLine, dataType, stationType) {
        var buffer = "";
        // 駅
        buffer += '<div class="exp_point exp_' + stationType + ' exp_clearfix">';
        // 到着時刻
        var type = "";
        var ArrivalStateFlag = false;
        var ArrivalState;
        if (typeof arrLine != 'undefined') {
            if (typeof arrLine.Type != 'undefined') {
                // タイプがある
                type = getTextValue(arrLine.Type);
            }
            if (dataType == "onTimetable" && type != "walk") {
                // 徒歩以外は出力
                if (typeof arrLine.ArrivalState != 'undefined') {
                    if (typeof arrLine.ArrivalState.Datetime.text != 'undefined') {
                        ArrivalState = convertISOtoDate(arrLine.ArrivalState.Datetime.text);
                        ArrivalStateFlag = true;
                    }
                }
            }
        }
        // 出発時刻
        var DepartureStateFlag = false;
        var DepartureState;
        if (typeof depLine != 'undefined') {
            if (typeof depLine.Type != 'undefined') {
                // タイプがある
                type = getTextValue(depLine.Type);
            }
            if (dataType == "onTimetable" && type != "walk") {
                // 徒歩以外は出力
                if (typeof depLine.DepartureState != 'undefined') {
                    if (typeof depLine.DepartureState.Datetime.text != 'undefined') {
                        DepartureState = convertISOtoDate(depLine.DepartureState.Datetime.text);
                        DepartureStateFlag = true;
                    }
                }
            }
        }
        // 発着時刻
        if (ArrivalStateFlag && DepartureStateFlag) {
            buffer += '<div class="exp_time exp_both">';
        } else if (ArrivalStateFlag) {
            buffer += '<div class="exp_time exp_arrivalOnly">';
        } else if (DepartureStateFlag) {
            buffer += '<div class="exp_time exp_departureOnly">';
        } else if (dataType == "onTimetable") {
            buffer += '<div class="exp_time exp_noData">&nbsp;';
        } else {
            buffer += '<div>';
        }
        if (typeof ArrivalState != 'undefined') {
            buffer += '<div class="exp_arrival">' + convertDate2TimeString(ArrivalState, arrLine.TimeReliability) + '</div>';
        }
        if (typeof DepartureState != 'undefined') {
            buffer += '<div class="exp_departure">' + convertDate2TimeString(DepartureState, depLine.TimeReliability) + '</div>';
        }
        buffer += '</div>';
        // 駅アイコン
        if (dataType == "onTimetable") {
            buffer += '<div class="exp_stationIcon">';
        } else {
            buffer += '<div class="exp_stationIconPlain">';
        }
        // 駅のマーク
        if (typeof arrLine == 'undefined' || typeof depLine == 'undefined') {
            buffer += '<div class="exp_edge"></div>';
        } else {
            var tmpStationType = new Array();
            if (typeof depLine.DepartureState.Type == 'string') {
                tmpStationType.push(depLine.DepartureState.Type);
            } else {
                for (var stType = 0; stType < depLine.DepartureState.Type.length; stType++) {
                    tmpStationType.push(depLine.DepartureState.Type[stType].text);
                }
            }
            stationType = getStationType(tmpStationType);
            // 駅のマークを出力
            if (stationType == 2) {
                buffer += '<div class="exp_none"></div>';
            } else if (stationType == 3) {
                buffer += '<div class="exp_back"></div>';
            } else if (stationType == 4) {
                buffer += '<div class="exp_extend"></div>';
            }
        }
        buffer += '</div>';
        // 駅名
        buffer += '<div class="exp_station">';
        if (stationType == "start" && typeof fromName != 'undefined') {
            buffer += sanitaize(fromName);
        } else if (stationType == "end" && typeof toName != 'undefined') {
            buffer += sanitaize(toName);
        } else if (typeof point.Station != 'undefined') {
            buffer += point.Station.Name;
        } else if (typeof point.Name != 'undefined') {
            if (point.Name.split(",")[2] == "tokyo") {
                buffer += "座標情報";
            } else {
                buffer += point.Name;
            }
        }
        // メニューリスト作成
        if (callBackObjectStation.length > 0) {
            buffer += '<span class="exp_stationMenu"><a id="' + baseId + ':stationMenu:' + String(routeNo) + ':' + String(index + 1) + ':open" href="Javascript:void(0);">&nbsp;&nbsp;</a></span>';
        }
        buffer += '</div>';
        buffer += '</div>';
        // メニュー本体
        if (callBackObjectStation.length > 0) {
            buffer += '<div class="exp_menu exp_stationWindow" id="' + baseId + ':stationMenu:' + String(routeNo) + ':' + String(index + 1) + '" style="display:none;">';
            buffer += '<div class="exp_header exp_clearfix">';
            buffer += '<span class="exp_title">駅情報</span>';
            buffer += '<span class="exp_close">';
            buffer += '<a class="exp_link" id="' + baseId + ':stationMenu:' + String(routeNo) + ':' + String(index + 1) + ':close" href="Javascript:void(0);">×</a>';
            buffer += '</span>';
            buffer += '</div>';
            buffer += '<div class="exp_body">';
            buffer += '<div class="exp_list">';
            // メニュー
            for (var i = 0; i < callBackObjectStation.length; i++) {
                buffer += '<div class="exp_item exp_' + (i % 2 == 0 ? 'odd' : 'even') + '"><a href="Javascript:void(0);" id="' + baseId + ':stationMenu:' + String(routeNo) + ':' + String(index + 1) + ':' + String(i + 1) + '">&nbsp;' + String(callBackObjectStation[i].text) + '&nbsp;</a></div>';
            }
            buffer += '</div>';
            buffer += '</div>';
            buffer += '</div>';
        }
        return buffer;
    }

    /**
     * 路線を出力
     */
     function outLine(routeNo, index, line, chargeList) {
         var buffer = "";
         var type;
         if (typeof line.Type != 'undefined') {
             // タイプがある
             type = getTextValue(line.Type);
         }
         // 路線メニュー本体
         if (callBackObjectLine.length > 0) {
             buffer += '<div class="exp_menu exp_lineWindow" id="' + baseId + ':lineMenu:' + String(routeNo) + ':' + String(index + 1) + '" style="display:none;">';
             buffer += '<div class="exp_header exp_clearfix">';
             buffer += '<span class="exp_title">路線情報</span>';
             buffer += '<span class="exp_close">';
             buffer += '<a class="exp_link" id="' + baseId + ':lineMenu:' + String(routeNo) + ':' + String(index + 1) + ':close" href="Javascript:void(0);">×</a>';
             buffer += '</span>';
             buffer += '</div>';
             buffer += '<div class="exp_body">';
             buffer += '<div class="exp_list">';
             // メニュー
             for (var i = 0; i < callBackObjectLine.length; i++) {
                 buffer += '<div class="exp_item exp_' + (i % 2 == 0 ? 'odd' : 'even') + '"><a href="Javascript:void(0);" id="' + baseId + ':lineMenu:' + String(routeNo) + ':' + String(index + 1) + ':' + String(i + 1) + '">&nbsp;' + String(callBackObjectLine[i].text) + '&nbsp;</a></div>';
             }
             buffer += '</div>';
             buffer += '</div>';
             buffer += '</div>';
         }

         // 路線
         if (chargeList.length > 0) {
             buffer += '<div class="exp_line exp_charge exp_clearfix">';
         } else {
             buffer += '<div class="exp_line exp_normal exp_clearfix">';
         }
         // 縦の線
         buffer += '<div class="exp_bar">';
         buffer += '<div class="exp_base">';
         var R = Math.floor(parseInt(line.Color, 10) / 1000000).toString(16);
         var G = (Math.floor(parseInt(line.Color, 10) / 1000) % 1000).toString(16);
         var B = (parseInt(line.Color, 10) % 1000).toString(16);
         buffer += '<div class="exp_color" style="background-color:#' + (R.length == 1 ? '0' + R : R) + (G.length == 1 ? '0' + G : G) + (B.length == 1 ? '0' + B : B) + ';"></div>';
         buffer += '</div>';
         buffer += '</div>';

         if (agent == 1) {
             // PC用の情報表示
             buffer += '<div class="exp_data">';
             buffer += '<div class="exp_info">';
             buffer += '<div class="exp_cell">';
             if (parseInt(line.timeOnBoard) > 0) {
                 buffer += '<div class="exp_timeOnBoard">' + line.timeOnBoard + '分</div>';
             }
             if (parseInt(line.stopStationCount) > 0) {
                 buffer += '<div class="exp_stopStationCount">' + line.stopStationCount + '駅</div>';
             }
             if (parseInt(line.distance) > 0) {
                 if (parseInt(line.distance) >= 10) {
                     buffer += '<div class="exp_distance">' + (parseInt(line.distance) / 10) + 'km</div>';
                 } else {
                     buffer += '<div class="exp_distance">' + parseInt(line.distance) * 100 + 'm</div>';
                 }
             }
             buffer += '</div>';
             buffer += '</div>';
             buffer += '</div>';
         } else if (agent == 2 || agent == 3) {
             // スマホ・タブレット用のアイコン
             buffer += '<div class="exp_iconArea">';
             buffer += '<div class="exp_iconCol">';
             buffer += '<div class="exp_iconCell">';
             if (parseInt(line.stopStationCount) > 0) {
                 buffer += '<div class="exp_icon">';
             } else {
                 buffer += '<div class="exp_icon exp_direct">';
             }
             if (type == "train") {
                 buffer += '<span class="exp_train"></span>';
             } else if (type == "plane") {
                 buffer += '<span class="exp_plane"></span>';
             } else if (type == "ship") {
                 buffer += '<span class="exp_ship"></span>';
             } else if (type == "bus") {
                 buffer += '<span class="exp_bus"></span>';
             } else if (type == "walk") {
                 buffer += '<span class="exp_walk"></span>';
             }
             if (parseInt(line.stopStationCount) > 0) {
                 buffer += '<div class="exp_stopStationCount">' + line.stopStationCount + '駅</div>';
             }
             buffer += '</div>';
             buffer += '</div>';
             buffer += '</div>';
             buffer += '</div>';
         }

         // 路線情報
         if (agent == 1) {
             if (typeof type == 'undefined') {
                 buffer += '<div class="exp_rail exp_rail_normal">';
             } else if (type == "train") {
                 buffer += '<div class="exp_rail exp_rail_normal exp_train">';
             } else if (type == "plane") {
                 buffer += '<div class="exp_rail exp_rail_normal exp_plane">';
             } else if (type == "ship") {
                 buffer += '<div class="exp_rail exp_rail_normal exp_ship">';
             } else if (type == "bus") {
                 buffer += '<div class="exp_rail exp_rail_normal exp_bus">';
             } else if (type == "walk") {
                 buffer += '<div class="exp_rail exp_rail_normal exp_walk">';
             } else {
                 buffer += '<div class="exp_rail exp_rail_normal">';
             }
         } else if (agent == 2 || agent == 3) {
             buffer += '<div class="exp_rail exp_rail_icon">';
         }
         // 番線表示
         if (typeof line.DepartureState.no != 'undefined') {
             buffer += '<div class="exp_no">[' + line.DepartureState.no + '番線]</div>';
         } else {
             buffer += '<div class="exp_no">&nbsp;</div>';
         }
         // 路線名
         var lineName = getTextValue(line.Name);
         // 列車番号・便名を出力するかどうか
         if (typeof line.Number != 'undefined') {
             if (type == "train") {
                 lineName += '&nbsp;<span class="exp_trainNo">' + line.Number + '号</span>';
             } else if (type == "plane" || type == "ship") {
                 lineName += '&nbsp;<span class="exp_trainNo">' + line.Number + '便</span>';
             } else {
                 lineName += '&nbsp;<span class="exp_trainNo">' + line.Number + '</span>';
             }
         }
         buffer += '<div class="exp_name">';
         buffer += lineName;
         // メニューリンク
         if (callBackObjectLine.length > 0) {
             buffer += '<span class="exp_lineMenu"><a id="' + baseId + ':lineMenu:' + String(routeNo) + ':' + String(index + 1) + ':open" href="Javascript:void(0);">&nbsp;</a></span>';
         }
         buffer += '</div>';
         // 改行
         buffer += '<div class="exp_separator"></div>';
         // その他情報
         if (agent == 2 || agent == 3) {
             buffer += '<div class="exp_etcInfo">';
             if (parseInt(line.timeOnBoard) > 0) {
                 buffer += '<span class="exp_timeOnBoard">' + line.timeOnBoard + '分</span>';
             }
             if (parseInt(line.distance) > 0) {
                 if (parseInt(line.distance) >= 10) {
                     buffer += '/' + '<span class="exp_distance">' + (parseInt(line.distance) / 10) + 'km</span>';
                 } else {
                     buffer += '/' + '<span class="exp_distance">' + parseInt(line.distance) * 100 + 'm</span>';
                 }
             }
             buffer += '</div>';
             buffer += '<div class="exp_separator"></div>';
         }
         // 特急券の情報
         if (chargeList.length > 0) {
             if (agent == 1) {
                 for (var i = 0; i < chargeList.length; i++) {
                     if (chargeList[i].selected == "true") {
                         // 1つだけ表示
                         buffer += '<input type="hidden" id="' + baseId + ':charge:' + String(routeNo) + ':' + String(index + 1) + '" value="' + chargeList[i].index + '">';
                         buffer += '<div class="exp_chargeDetail">';
                         if (chargeList.length >= 2) {
                             // 2つ以上ある場合はメニューのリンクを設置
                             buffer += '<a id="' + baseId + ':chargeMenu:' + String(routeNo) + ':' + String(index + 1) + ':open" href="Javascript:void(0);">';
                         }
                         buffer += '<div class="exp_chargeCost" id="' + baseId + ':chargeMenu:' + String(routeNo) + ':' + String(index + 1) + ':open:2">';
                         var chargeRemark = '';
                         if (typeof chargeList[i].Oneway.remark != 'undefined') {
                             chargeRemark = '【' + chargeList[i].Oneway.fullRemark + '】';
                             if (typeof chargeList[i].Oneway.expectedRemark != 'undefined') {
                                 chargeRemark += '※ ';
                             }
                         }
                         buffer += ((typeof chargeList[i].Name != 'undefined') ? chargeList[i].Name + chargeRemark : "指定なし") + ": ";
                         // 運賃改定未対応
                         var salesTaxRateIsNotSupported = false;
                         if (typeof chargeList[i].fareRevisionStatus != 'undefined') {
                             if (chargeList[i].fareRevisionStatus == 'salesTaxRateIsNotSupported') {
                                 salesTaxRateIsNotSupported = true;
                             }
                         }
                         buffer += salesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported" id="' + baseId + ':chargeMenu:' + String(routeNo) + ':' + String(index + 1) + ':open:3">' : '';
                         if (priceViewFlag == "oneway") {
                             buffer += num2String(parseInt(getTextValue(chargeList[i].Oneway))) + '円';
                         } else if (priceViewFlag == "round") {
                             buffer += num2String(parseInt(getTextValue(chargeList[i].Round))) + '円';
                         }
                         buffer += salesTaxRateIsNotSupported ? '</span>' : '';
                         buffer += '</div>';
                         if (chargeList.length >= 2) {
                             // メニューリンク終了
                             buffer += '</a>';
                         }
                         if (typeof chargeList[i].Oneway.expectedRemark != 'undefined') {
                             buffer += '<div><span class="exp_detail">';
                             buffer += '※探索詳細条件で指定された「' + chargeList[i].Oneway.expectedFullRemark + '」とは別の割引が適用されています。';
                             buffer += '</span></div>';
                         }
                         buffer += '</div>';
                     }
                 }
                 // 特急券リスト
                 if (chargeList.length >= 2) {
                     // 特急券メニュー本体
                     buffer += '<div class="exp_menu exp_chargeWindow" id="' + baseId + ':chargeMenu:' + String(routeNo) + ':' + String(index + 1) + '" style="display:none;">';
                     buffer += '<div class="exp_header exp_clearfix">';
                     buffer += '<span class="exp_title">種別</span>';
                     buffer += '<span class="exp_close">';
                     buffer += '<a class="exp_link" id="' + baseId + ':chargeMenu:' + String(routeNo) + ':' + String(index + 1) + ':close" href="Javascript:void(0);">×</a>';
                     buffer += '</span>';
                     buffer += '</div>';
                     buffer += '<div class="exp_body">';
                     buffer += '<div class="exp_list">';
                     var expectedRemark;
                     // メニュー
                     for (var k = 0; k < chargeList.length; k++) {
                         // 運賃改定未対応
                         var salesTaxRateIsNotSupported = false;
                         if (typeof chargeList[k].fareRevisionStatus != 'undefined') {
                             if (chargeList[k].fareRevisionStatus == 'salesTaxRateIsNotSupported') {
                                 salesTaxRateIsNotSupported = true;
                             }
                         }
                         buffer += '<div class="exp_item' + (chargeList[k].selected == "true" ? " exp_checked" : "") + ' exp_' + (k % 2 == 0 ? 'odd' : 'even') + '">';
                         buffer += '<a href="Javascript:void(0);" id="' + baseId + ':chargeMenu:' + String(routeNo) + ':' + String(index + 1) + ':' + String(chargeList[k].index) + '">';
                         // 金額
                         buffer += '<span class="exp_costList" id="' + baseId + ':chargeMenu:' + String(routeNo) + ':' + String(index + 1) + ':' + String(chargeList[k].index) + ':cost">';
                         buffer += '<span class="exp_cost" id="' + baseId + ':chargeMenu:' + String(routeNo) + ':' + String(index + 1) + ':' + String(chargeList[k].index) + ':cost:text">';
                         if (priceViewFlag == "oneway") {
                             buffer += num2String(parseInt(getTextValue(chargeList[k].Oneway))) + '円';
                         } else if (priceViewFlag == "round") {
                             buffer += num2String(parseInt(getTextValue(chargeList[k].Round))) + '円';
                         }
                         buffer += '</span>';
                         buffer += '</span>';
                         var chargeRemark = '';
                         if (typeof chargeList[k].Oneway.remark != 'undefined') {
                             chargeRemark = '(' + chargeList[k].Oneway.remark + ')';
                         }

                         buffer += ((typeof chargeList[k].Name != 'undefined') ? chargeList[k].Name + chargeRemark : "指定なし")

                         if (typeof chargeList[k].Oneway.expectedRemark != 'undefined') {
                             expectedRemark = chargeList[k].Oneway.expectedFullRemark;
                             buffer += '※';
                         }

                         buffer +=  '&nbsp;</a></div>';
                     }
                     buffer += '</div>';
                     buffer += '</div>';
                     if (typeof expectedRemark != 'undefined') {
                         buffer += '<div class="exp_footer"><span class="exp_title">';
                         buffer += '※探索詳細条件で指定された「' + expectedRemark + '」とは<br>別の割引が適用されています。';
                         buffer += '</span></div>';
                     }
                     buffer += '</div>';
                 }
             } else if (agent == 2 || agent == 3) {
                 // 運賃が複数あった場合のフォーム出力
                 buffer += '<div class="exp_chargeSelect">';
                 var expectedRemark;
                 for (var i = 0; i < chargeList.length; i++) {
                     if (chargeList[i].selected == "true") {
                         buffer += '<div class="exp_chargeSelectText">';
                         if (typeof chargeList[i].Name != 'undefined') {
                             buffer += chargeList[i].Name;
                             if (typeof chargeList[i].Oneway.fullRemark != 'undefined') {
                               buffer += '【' + chargeList[i].Oneway.fullRemark + '】';
                             }
                             if (typeof chargeList[i].Oneway.expectedRemark != 'undefined') {
                                 buffer += '※';
                                 expectedRemark = chargeList[i].Oneway.expectedFullRemark;
                             }
                             buffer += " : ";
                         } else {
                             buffer += "指定なし:";
                         }
                         // 運賃改定未対応
                         var salesTaxRateIsNotSupported = false;
                         if (typeof chargeList[i].fareRevisionStatus != 'undefined') {
                             if (chargeList[i].fareRevisionStatus == 'salesTaxRateIsNotSupported') {
                                 salesTaxRateIsNotSupported = true;
                             }
                         }
                         buffer += salesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported">' : '';
                         if (priceViewFlag == "oneway") {
                             buffer += num2String(parseInt(getTextValue(chargeList[i].Oneway))) + '円';
                         } else if (priceViewFlag == "round") {
                             buffer += num2String(parseInt(getTextValue(chargeList[i].Round))) + '円';
                         }
                         buffer += salesTaxRateIsNotSupported ? '</span>' : '';
                         buffer += '</div>';
                     }
                 }
                 if (priceChangeFlag) {
                     buffer += '<select id="' + baseId + ':chargeSelect:' + String(routeNo) + ':' + chargeList[0].fromLineIndex + '">';
                     for (var i = 0; i < chargeList.length; i++) {
                         buffer += '<option value="' + chargeList[i].index + '"' + ((chargeList[i].selected == "true") ? "selected" : "") + '>';
                         if (typeof chargeList[i].Name != 'undefined') {
                             buffer += chargeList[i].Name;
                             if (typeof chargeList[i].Oneway.remark != "undefined") {
                               buffer += " (" + chargeList[i].Oneway.remark + ")";
                             }
                             if (typeof chargeList[i].Oneway.expectedRemark != 'undefined') {
                                 buffer += '※';
                             }
                             buffer += " : ";
                         } else {
                             buffer += "指定なし:";
                         }
                         var salesTaxRateIsNotSupported = false;
                         if (typeof chargeList[i].fareRevisionStatus != 'undefined') {
                             if (chargeList[i].fareRevisionStatus == 'salesTaxRateIsNotSupported') {
                                 salesTaxRateIsNotSupported = true;
                             }
                         }
                         buffer += salesTaxRateIsNotSupported ? '<span class="exp_taxRateIsNotSupported">' : '';
                         if (priceViewFlag == "oneway") {
                             buffer += num2String(parseInt(getTextValue(chargeList[i].Oneway))) + '円';
                         } else if (priceViewFlag == "round") {
                             buffer += num2String(parseInt(getTextValue(chargeList[i].Round))) + '円';
                         }
                         buffer += salesTaxRateIsNotSupported ? '</span>' : '';
                         buffer += '</option>';
                     }
                     buffer += '</select>';
                 }

                 buffer += '</div>';
                 if (typeof expectedRemark != 'undefined') {
                     buffer += '<span class="exp_detail">';
                     buffer += '※詳細条件で指定された「' + expectedRemark + '」とは別の割引が適用されています。';
                     buffer += '</span>';
                 }
             }
         }

         // 番線表示
         if (typeof line.ArrivalState.no != 'undefined') {
             buffer += '<div class="exp_no">[' + line.ArrivalState.no + '番線]</div>';
         } else {
             if (agent == 2 || agent == 3) {
                 buffer += '<div class="exp_no">&nbsp;</div>';
             }
         }
         buffer += '</div>';
         buffer += '</div>';
         return buffer;
     }

    /**
    * ISOの日時をDateオブジェクトに変換
    */
    function convertISOtoDate(str) {
        var tmp_date;
        if (str.indexOf("T") != -1) {
            // 時間あり
            tmp_date = new Date(parseInt(str.substring(0, 4), 10), parseInt(str.substring(5, 7), 10) - 1, parseInt(str.substring(8, 10), 10), parseInt(str.substring(11, 13), 10), parseInt(str.substring(14, 16), 10), 0);
        } else {
            // 日付のみ
            tmp_date = new Date(parseInt(str.substring(0, 4), 10), parseInt(str.substring(5, 7), 10) - 1, parseInt(str.substring(8, 10), 10));
        }
        return tmp_date;
    }

    /**
    * ISOの日時を文字列に変換
    */
    function convertISOtoTime(str, type) {
        if (typeof str != 'undefined') {
            var tmp_time = str.split(":");
            var hour = parseInt(tmp_time[0], 10);
            if (typeof type != 'undefined') {
                if (type == "yesterday") { hour += 24; }
            }
            return String(hour) + ":" + tmp_time[1];
        } else {
            return;
        }
    }

    /**
    * 路線の発着時刻を判定し、出力
    */
    function convertDate2TimeString(date, type) {
        if (typeof type != 'undefined') {
            var time;
            if (date.getMinutes() >= 10) {
                time = date.getHours() + ":" + date.getMinutes();
            } else {
                time = date.getHours() + ":0" + date.getMinutes();
            }
            if (type == 'onTimetable') {
                return '<span class="exp_onTimetable">' + time + '</span>';
            } else if (type == 'interval') {
                return '<span class="exp_interval">[' + time + ']</span>';
            } else if (type == 'outside') {
                return '<span class="exp_outside">&lt;' + time + '&gt;</span>';
            } else if (type == 'average') {
                return '<span class="exp_average">(' + time + ')</span>';
            }
        } else {
            return "";
        }
    }

    /**
    * カンマ区切りの数値を出力
    */
    function num2String(str) {
        var num = new String(str).replace(/,/g, "");
        while (num != (num = num.replace(/^(-?\d+)(\d{3})/, "$1,$2")));
        return num;
    }

    /**
    * 料金変更時の処理
    */
    function changePrice() {
        // 探索結果オブジェクトの特定
        var tmpResult;
        if (resultCount == 1) {
            tmpResult = result.ResultSet.Course;
        } else {
            tmpResult = result.ResultSet.Course[(selectNo - 1)];
        }
        // 変更対象となった料金のリスト作成
        var fareList = new Array();
        var chargeList = new Array();
        var vehicleTeikiList = new Array();
        var nikukanTeikiList = new Array();
        var passTeikiList = new Array();
        for (var i = 0; i < (tmpResult.Route.Point.length - 1); i++) {
            // 乗車券のリスト作成
            if (document.getElementById(baseId + ':fareSelect:' + selectNo + ':' + (i + 1))) {
                fareList.push(parseInt(document.getElementById(baseId + ':fareSelect:' + selectNo + ':' + (i + 1)).options.item(document.getElementById(baseId + ':fareSelect:' + selectNo + ':' + (i + 1)).selectedIndex).value));
            } else if (document.getElementById(baseId + ':fare:' + selectNo + ':' + (i + 1))) {
                fareList.push(parseInt(document.getElementById(baseId + ':fare:' + selectNo + ':' + (i + 1)).value));
            }
            // 特急券のリスト作成
            if (document.getElementById(baseId + ':chargeSelect:' + selectNo + ':' + (i + 1))) {
                chargeList.push(parseInt(document.getElementById(baseId + ':chargeSelect:' + selectNo + ':' + (i + 1)).options.item(document.getElementById(baseId + ':chargeSelect:' + selectNo + ':' + (i + 1)).selectedIndex).value));
            } else if (document.getElementById(baseId + ':charge:' + selectNo + ':' + (i + 1))) {
                chargeList.push(parseInt(document.getElementById(baseId + ':charge:' + selectNo + ':' + (i + 1)).value));
            }
            // 定期の選択リスト作成
            if (document.getElementById(baseId + ':teikiSelect:' + selectNo + ':' + (i + 1))) {
                if (document.getElementById(baseId + ':teikiKind:' + selectNo + ':' + (i + 1)).value == "vehicle") {
                    // 車両選択
                    vehicleTeikiList.push(parseInt(document.getElementById(baseId + ':teikiSelect:' + selectNo + ':' + (i + 1)).options.item(document.getElementById(baseId + ':teikiSelect:' + selectNo + ':' + (i + 1)).selectedIndex).value));
                } else if (document.getElementById(baseId + ':teikiKind:' + selectNo + ':' + (i + 1)).value == "nikukanteiki") {
                    // 二区間定期
                    nikukanTeikiList.push(parseInt(document.getElementById(baseId + ':teikiSelect:' + selectNo + ':' + (i + 1)).options.item(document.getElementById(baseId + ':teikiSelect:' + selectNo + ':' + (i + 1)).selectedIndex).value));
                } else if (document.getElementById(baseId + ':teikiKind:' + selectNo + ':' + (i + 1)).value == "bycorporation") {
                    // 各事業者が定める定期
                    passTeikiList.push(parseInt(document.getElementById(baseId + ':teikiSelect:' + selectNo + ':' + (i + 1)).options.item(document.getElementById(baseId + ':teikiSelect:' + selectNo + ':' + (i + 1)).selectedIndex).value));
                }
            } else if (document.getElementById(baseId + ':teiki:' + selectNo + ':' + (i + 1))) {
                if (document.getElementById(baseId + ':teikiKind:' + selectNo + ':' + (i + 1)).value == "vehicle") {
                    // 車両選択
                    vehicleTeikiList.push(parseInt(document.getElementById(baseId + ':teiki:' + selectNo + ':' + (i + 1)).value));
                } else if (document.getElementById(baseId + ':teikiKind:' + selectNo + ':' + (i + 1)).value == "nikukanteiki") {
                    // 二区間定期
                    nikukanTeikiList.push(parseInt(document.getElementById(baseId + ':teiki:' + selectNo + ':' + (i + 1)).value));
                } else if (document.getElementById(baseId + ':teikiKind:' + selectNo + ':' + (i + 1)).value == "bycorporation") {
                    // 各事業者が定める定期
                    passTeikiList.push(parseInt(document.getElementById(baseId + ':teiki:' + selectNo + ':' + (i + 1)).value));
                }
            }
        }
        // 再探索を行なって運賃を計算する
        if (priceChangeRefreshFlag) {
            var searchWord = "";
            searchWord += "serializeData=" + encodeURIComponent(tmpResult.SerializeData);
            if (fareList.length >= 1) {
                searchWord += "&fareIndex=" + fareList.join(":");
            }
            if (chargeList.length >= 1) {
                searchWord += "&chargeIndex=" + chargeList.join(":");
            }
            if (vehicleTeikiList.length >= 1) {
                searchWord += "&vehicleIndex=" + vehicleTeikiList.join(":");
            }
            if (nikukanTeikiList.length >= 1) {
                searchWord += "&nikukanteikiIndex=" + nikukanTeikiList.join(":");
            }
            if (passTeikiList.length >= 1) {
                searchWord += "&passStatusIndex=" + passTeikiList.join(":");
            }
            searchWord += "&addRouteData=true";
            var url = apiURL + "v1/json/course/recalculate?key=" + key + "&" + searchWord;
            // エンジンバージョン同一チェック
            if (!checkEngineVersion) {
                url += "&checkEngineVersion=false";
            }
            reSearch(url, selectNo);
        } else {
            // フォームを解析して運賃を再計算する
            var fare = 0;
            var fareRound = 0;
            var charge = 0;
            var chargeRound = 0;
            for (var i = 0; i < tmpResult.Price.length; i++) {
                if (tmpResult.Price[i].kind == "Fare") {
                    // 乗車券の運賃再計算
                    if (checkArray(fareList, parseInt(tmpResult.Price[i].index)) != -1) {
                        // 探索結果オブジェクトの選択を変える
                        tmpResult.Price[i].selected = "true";
                        // 選択していない料金はオフにする
                        for (var j = 0; j < tmpResult.Price.length; j++) {
                            if (tmpResult.Price[i].index != tmpResult.Price[j].index && tmpResult.Price[i].kind == tmpResult.Price[j].kind && tmpResult.Price[i].fromLineIndex == tmpResult.Price[j].fromLineIndex) {
                                tmpResult.Price[j].selected = "false";
                            }
                        }
                    }
                } else if (tmpResult.Price[i].kind == "Charge") {
                    // 特急券の運賃再計算
                    if (checkArray(chargeList, parseInt(tmpResult.Price[i].index)) != -1) {
                        // 探索結果オブジェクトの選択を変える
                        tmpResult.Price[i].selected = "true";
                        // 選択していない料金はオフにする
                        for (var j = 0; j < tmpResult.Price.length; j++) {
                            if (tmpResult.Price[i].index != tmpResult.Price[j].index && tmpResult.Price[i].kind == tmpResult.Price[j].kind && tmpResult.Price[i].fromLineIndex == tmpResult.Price[j].fromLineIndex) {
                                tmpResult.Price[j].selected = "false";
                            }
                        }
                    }
                }
            }
            // 合計金額の算出
            for (var i = 0; i < tmpResult.Price.length; i++) {
                if (tmpResult.Price[i].kind == "Fare" && tmpResult.Price[i].selected == "true") {
                    // 片道運賃の再計算
                    fare += parseInt(getTextValue(tmpResult.Price[i].Oneway));
                    // 往復運賃の再計算
                    fareRound += parseInt(getTextValue(tmpResult.Price[i].Round));
                } else if (tmpResult.Price[i].kind == "Charge" && tmpResult.Price[i].selected == "true") {
                    // 片道運賃の再計算
                    charge += parseInt(getTextValue(tmpResult.Price[i].Oneway));
                    // 往復運賃の再計算
                    chargeRound += parseInt(getTextValue(tmpResult.Price[i].Round));
                }
            }
            // 合計金額の変更
            for (var i = 0; i < tmpResult.Price.length; i++) {
                if (tmpResult.Price[i].kind == "FareSummary") {
                    // 乗車券の運賃再計算
                    tmpResult.Price[i].Oneway = String(fare);
                    tmpResult.Price[i].Round = String(fareRound);
                } else if (tmpResult.Price[i].kind == "ChargeSummary") {
                    // 特急券の運賃再計算
                    tmpResult.Price[i].Oneway = String(charge);
                    tmpResult.Price[i].Round = String(chargeRound);
                }
            }
            changeCourse(selectNo);
        }
    }

    /**
    * 運賃変更時の最短作処理
    */
    function reSearch(url, no) {
        if (typeof resultObj != 'undefined') {
            resultObj.abort();
        }
        var JSON_object = {};
        if (window.XDomainRequest) {
            // IE用
            resultObj = new XDomainRequest();
            resultObj.onload = function () {
                setResultSingle(resultObj.responseText, no);
            };
        } else {
            resultObj = new XMLHttpRequest();
            resultObj.onreadystatechange = function () {
                var done = 4, ok = 200, fbd = 403;
                if (resultObj.readyState == done && resultObj.status == ok) {
                    setResultSingle(resultObj.responseText, no);
                } else if (resultObj.readyState == done && resultObj.status == fbd) {
                	alert("WEB APIが403エラーを返却しました。管理者に連絡してください。",1);
                }
            };
        }
        var newurl = "ekispert_apicall?callURL=" + encodeURIComponent(url);
        resultObj.open("GET", newurl, true);
        resultObj.overrideMimeType('text/plain; charset=utf-8');
        resultObj.send(null);
    }

    /**
     * ソート済みインデックスを取得する
     */
    function getCourseNo(index) {
        for (var i = 0; i < sortCourseList.length; i++) {
            if (sortCourseList[i].index == index) {
                return (i + 1);
            }
        }
    }

    /**
    * 探索結果オブジェクト内の1経路だけ入れ替え
    */
    function setResultSingle(resultObject, no) {
        tmpResult = JSON.parse(resultObject);
        if (resultCount == 1) {
            result.ResultSet.Course = tmpResult.ResultSet.Course;
        } else {
            result.ResultSet.Course[(no - 1)] = tmpResult.ResultSet.Course;
        }
        // 探索結果の切り替え
        changeCourse(no);
    }

    /**
    * 表示している探索結果のシリアライズデータを取得
    */
    function getSerializeData() {
        if (viewCourseListFlag) {
            // 一覧表示中は返さない
            return;
        } else if (typeof result != 'undefined') {
            var tmpResult;
            if (resultCount == 1) {
                tmpResult = result.ResultSet.Course;
            } else {
                tmpResult = result.ResultSet.Course[(selectNo - 1)];
            }
            return tmpResult.SerializeData;
        } else {
            return;
        }
    }

    /**
    * 表示している探索結果すべてののシリアライズデータを取得
    */
    function getSerializeDataAll() {
        var tmpSerializeList = new Array();
        if (typeof result != 'undefined') {
            if (resultCount == 1) {
                tmpSerializeList.push(result.ResultSet.Course.SerializeData);
            } else {
                for (var i = 0; i < resultCount; i++) {
                    tmpSerializeList.push(result.ResultSet.Course[i].SerializeData);
                }
            }
            return tmpSerializeList;
        } else {
            return tmpSerializeList;
        }
    }

    /**
     * 表示している探索結果の定期券情報シリアライズデータを取得
     */
     function getTeikiSerializeData() {
         if (viewCourseListFlag) {
             // 一覧表示中は返さない
             return;
         } else if (typeof result != 'undefined') {
             var tmpResult;
             if (resultCount == 1) {
                 tmpResult = result.ResultSet.Course.Teiki;
             } else {
                 tmpResult = result.ResultSet.Course[(selectNo - 1)].Teiki;
             }
             if (typeof tmpResult != 'undefined') {
                 return tmpResult.SerializeData;
             }
         } else {
             return;
         }
     }


    /**
    * 表示している探索結果の定期控除のための文字列を取得
    */
    function getTeiki() {
        if (typeof result == 'undefined') {
            return;
        }
        var tmpResult;
        if (resultCount == 1) {
            tmpResult = result.ResultSet.Course;
        } else {
            tmpResult = result.ResultSet.Course[(selectNo - 1)];
        }
        // 事前チェック
        var Teiki1Summary;
        var Teiki3Summary;
        var Teiki6Summary;
        var Teiki12Summary;
        if (typeof tmpResult.Price != 'undefined') {
            for (var j = 0; j < tmpResult.Price.length; j++) {
                if (tmpResult.Price[j].kind == "Teiki1Summary") {
                    if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                        Teiki1Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                    }
                } else if (tmpResult.Price[j].kind == "Teiki3Summary") {
                    if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                        Teiki3Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                    }
                } else if (tmpResult.Price[j].kind == "Teiki6Summary") {
                    if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                        Teiki6Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                    }
                } else if (tmpResult.Price[j].kind == "Teiki12Summary") {
                    if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                        Teiki12Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                    }
                }
            }
        }
        if (typeof Teiki1Summary == 'undefined' && typeof Teiki3Summary == 'undefined' && typeof Teiki6Summary == 'undefined' && Teiki12Summary == 'undefined') {
            return;
        }
        if (typeof tmpResult.Route.Line.length == 'undefined') {
            if (getTextValue(tmpResult.Route.Line.Type) != "train" && getTextValue(tmpResult.Route.Line.Type) != "walk") {
                return;
            }
        } else {
            for (var i = 0; i < (tmpResult.Route.Point.length - 1); i++) {
                if (getTextValue(tmpResult.Route.Line[i].Type) != "train" && getTextValue(tmpResult.Route.Line[i].Type) != "walk") {
                    return;
                }
            }
        }
        if (tmpResult.dataType == "plain") {
            var buffer = "";
            if (typeof tmpResult.Route.Line.length == 'undefined') {
                if (typeof tmpResult.Route.Point[0].Station != 'undefined') {
                    buffer += tmpResult.Route.Point[0].Station.Name;
                } else if (typeof tmpResult.Route.Point[0].Name != 'undefined') {
                    buffer += tmpResult.Route.Point[0].Name;
                }
                buffer += ":" + tmpResult.Route.Line.Name + ":" + tmpResult.Route.Line.direction + ":";
                if (typeof tmpResult.Route.Point[1].Station != 'undefined') {
                    buffer += tmpResult.Route.Point[1].Station.Name;
                } else if (typeof tmpResult.Route.Point[1].Name != 'undefined') {
                    buffer += tmpResult.Route.Point[1].Name;
                }
            } else {
                for (var i = 0; i < (tmpResult.Route.Point.length - 1); i++) {
                    if (typeof tmpResult.Route.Point[i].Station != 'undefined') {
                        buffer += tmpResult.Route.Point[i].Station.Name;
                    } else if (typeof tmpResult.Route.Point[i].Name != 'undefined') {
                        buffer += tmpResult.Route.Point[i].Name;
                    }
                    buffer += ":" + tmpResult.Route.Line[i].Name + ":" + tmpResult.Route.Line[i].direction + ":";
                }
                if (typeof tmpResult.Route.Point[tmpResult.Route.Point.length - 1].Station != 'undefined') {
                    buffer += tmpResult.Route.Point[tmpResult.Route.Point.length - 1].Station.Name;
                } else if (typeof tmpResult.Route.Point[tmpResult.Route.Point.length - 1].Name != 'undefined') {
                    buffer += tmpResult.Route.Point[tmpResult.Route.Point.length - 1].Name;
                }
            }
            return buffer;
        } else {
            return;
        }
    }

    /**
    * 二区間定期の控除用インデックスリストの取得
    */
    function getNikukanteikiIndex() {
        if (typeof result == 'undefined') {
            return;
        }
        var tmpResult;
        if (resultCount == 1) {
            tmpResult = result.ResultSet.Course;
        } else {
            tmpResult = result.ResultSet.Course[(selectNo - 1)];
        }
        if (typeof tmpResult.PassStatus != 'undefined') {
            var buffer = "";
            if (typeof tmpResult.PassStatus.length == 'undefined') {
                if (tmpResult.PassStatus.kind == "nikukanteiki") {
                    if (tmpResult.PassStatus.selected == "true") {
                        buffer += '1';
                    }
                }
            } else {
                for (var i = 0; i < tmpResult.PassStatus.length; i++) {
                    if (tmpResult.PassStatus[i].kind == "nikukanteiki") {
                        if (tmpResult.PassStatus[i].selected == "true") {
                            if (buffer != "") { buffer += ':'; }
                            buffer += String(i + 1);
                        }
                    }
                }
            }
            if (buffer != "") {
                return buffer;
            }
        }
        return;
    }

    /**
    * 車両のインデックスリストの取得
    */
    function getVehicleIndex() {
        if (typeof result == 'undefined') {
            return;
        }
        var tmpResult;
        if (resultCount == 1) {
            tmpResult = result.ResultSet.Course;
        } else {
            tmpResult = result.ResultSet.Course[(selectNo - 1)];
        }
        if (typeof tmpResult.PassStatus != 'undefined') {
            var buffer = "";
            if (typeof tmpResult.PassStatus.length == 'undefined') {
                if (tmpResult.PassStatus.kind == "vehicle") {
                    if (tmpResult.PassStatus.selected == "true") {
                        buffer += '1';
                    }
                }
            } else {
                for (var i = 0; i < tmpResult.PassStatus.length; i++) {
                    if (tmpResult.PassStatus[i].kind == "vehicle") {
                        if (tmpResult.PassStatus[i].selected == "true") {
                            if (buffer != "") { buffer += ':'; }
                            buffer += String(i + 1);
                        }
                    }
                }
            }
            if (buffer != "") {
                return buffer;
            }
        }
        return;
    }

    /**
    * 定期の状態を取得
    */
    function getPassStatusObject(index) {
        var tmpPassStatusObject;
        if (typeof result != 'undefined') {
            var tmpResult, passStatusObject;
            if (resultCount == 1) {
                tmpResult = result.ResultSet.Course;
            } else {
                tmpResult = result.ResultSet.Course[(selectNo - 1)];
            }
            if (typeof tmpResult.PassStatus.length == 'undefined') {
                if (index == 1) {
                    passStatusObject = tmpResult.PassStatus;
                }
            } else {
                if (typeof tmpResult.PassStatus[parseInt(index) - 1] != 'undefined') {
                    passStatusObject = tmpResult.PassStatus[parseInt(index) - 1];
                }
            }
            if (typeof passStatusObject != 'undefined') {
                tmpPassStatusObject = new Object();
                // 名称
                if (typeof passStatusObject.Name != 'undefined') {
                    tmpPassStatusObject.name = getTextValue(passStatusObject.Name);
                }
                // タイプ
                if (typeof passStatusObject.Type != 'undefined') {
                    tmpPassStatusObject.type = getTextValue(passStatusObject.Type);
                }
                // 種別
                if (typeof passStatusObject.kind != 'undefined') {
                    tmpPassStatusObject.kind = passStatusObject.kind;
                }
                // コメント
                if (typeof passStatusObject.Comment != 'undefined') {
                    tmpPassStatusObject.comment = getTextValue(passStatusObject.Comment);
                }
            }
        }
        return tmpPassStatusObject;
    }

    /**
    * 探索結果すべての経路オブジェクトを取得
    */
    function getResultAll() {
        if (typeof result != 'undefined') {
            return JSON.parse(JSON.stringify(result));
        } else {
            return;
        }
    }

    /**
    * 表示している経路オブジェクトの番号を取得
    */
    function getResultNo() {
        if (viewCourseListFlag) {
            // 一覧表示中は返さない
            return;
        } else if (typeof result != 'undefined') {
            return selectNo;
        } else {
            return;
        }
    }

    /**
    * 表示している経路オブジェクトを取得
    */
    function getResult() {
        if (viewCourseListFlag) {
            // 一覧表示中は返さない
            return;
        } else if (typeof result != 'undefined') {
            if (resultCount == 1) {
                return JSON.parse(JSON.stringify(result));
            } else {
                // 探索結果を一つにする
                var tmpResult = JSON.parse(JSON.stringify(result));
                tmpResult.ResultSet.Course = tmpResult.ResultSet.Course[(selectNo - 1)];
                return JSON.parse(JSON.stringify(tmpResult));
            }
        } else {
            return;
        }
    }

    /**
    * 探索結果すべての経路オブジェクトをJSONに変換して取得
    */
    function getResultStringAll() {
        if (typeof result != 'undefined') {
            return JSON.stringify(result);
        } else {
            return;
        }
    }

    /**
    * オブジェクトの値を取得
    */
    function getTextValue(obj) {
        if (typeof obj != "undefined") {
            if (typeof obj.text != "undefined") {
                return obj.text;
            }
            return obj;
        } else {
            return "";
        }
    }

    /**
    * 表示している経路オブジェクトをJSONに変換して取得
    */
    function getResultString() {
        if (viewCourseListFlag) {
            // 一覧表示中は返さない
            return;
        } else if (typeof result != 'undefined') {
            if (resultCount == 1) {
                return JSON.stringify(result);
            } else {
                // 探索結果を一つにする
                var tmpResult = JSON.parse(JSON.stringify(result));
                tmpResult.ResultSet.Course = tmpResult.ResultSet.Course[(selectNo - 1)];
                return JSON.stringify(tmpResult);
            }
        } else {
            return;
        }
    }

    /**
    * 指定した経路の出発時刻を取得
    */
    function getDepartureState(tmpCourse, index) {
        if (typeof tmpCourse.Route.Line.length == 'undefined') {
            return convertISOtoDate(tmpCourse.Route.Line.DepartureState.Datetime.text, tmpCourse.Route.Line.DepartureState.Datetime.operation);
        } else {
            if (typeof index == 'undefined') {
                // index未指定時
                return convertISOtoDate(tmpCourse.Route.Line[0].DepartureState.Datetime.text, tmpCourse.Route.Line[0].DepartureState.Datetime.operation);
            } else {
                // index指定時
                return convertISOtoDate(tmpCourse.Route.Line[parseInt(index) - 1].DepartureState.Datetime.text, tmpCourse.Route.Line[parseInt(index) - 1].DepartureState.Datetime.operation);
            }
        }
    }

    /**
    * 出発時刻を取得
    */
    function getDepartureDate(index) {
        if (viewCourseListFlag) {
            // 一覧表示中は返さない
            return;
        } else if (typeof result != 'undefined') {
            var tmpCourse;
            if (resultCount == 1) {
                tmpCourse = result.ResultSet.Course;
            } else {
                tmpCourse = result.ResultSet.Course[(parseInt(selectNo) - 1)];
            }
            return getDepartureState(tmpCourse, index);
        } else {
            return;
        }
    }

    /**
    * 指定した経路の到着時刻を取得
    */
    function getArrivalState(tmpCourse, index) {
        if (typeof tmpCourse.Route.Line.length == 'undefined') {
            return convertISOtoDate(tmpCourse.Route.Line.ArrivalState.Datetime.text, tmpCourse.Route.Line.ArrivalState.Datetime.operation);
        } else {
            if (typeof index == 'undefined') {
                // index未指定時
                return convertISOtoDate(tmpCourse.Route.Line[tmpCourse.Route.Line.length - 1].ArrivalState.Datetime.text, tmpCourse.Route.Line[tmpCourse.Route.Line.length - 1].ArrivalState.Datetime.operation);
            } else {
                // index指定時
                return convertISOtoDate(tmpCourse.Route.Line[parseInt(index) - 1].ArrivalState.Datetime.text, tmpCourse.Route.Line[parseInt(index) - 1].ArrivalState.Datetime.operation);
            }
        }
    }

    /**
    * 到着時刻を取得
    */
    function getArrivalDate(index) {
        if (viewCourseListFlag) {
            // 一覧表示中は返さない
            return;
        } else if (typeof result != 'undefined') {
            var tmpCourse;
            if (resultCount == 1) {
                tmpCourse = result.ResultSet.Course;
            } else {
                tmpCourse = result.ResultSet.Course[(parseInt(selectNo) - 1)];
            }
            return getArrivalState(tmpCourse, index);
        } else {
            return;
        }
    }

    /**
    * 最適経路のチェック
    */
    function checkBestCourse(type) {
        if (viewCourseListFlag) {
            // 一覧表示中は返さない
            return;
        } else if (typeof result != 'undefined') {
            if (typeof result == 'undefined') {
                return;
            } else {
                var tmpResult;
                //ekispertを指定した場合は第一経路のみtrue
                if (type == "ekispert") {
                    if (selectNo == 1) {
                        return true;
                    } else {
                        return false;
                    }
                }
                if (resultCount == 1) {
                    tmpResult = result.ResultSet.Course;
                } else {
                    tmpResult = result.ResultSet.Course[(selectNo - 1)];
                }
                var time = parseInt(tmpResult.Route.timeOnBoard) + parseInt(tmpResult.Route.timeWalk) + parseInt(tmpResult.Route.timeOther);
                var TransferCount = parseInt(tmpResult.Route.transferCount);
                var exhaustCO2 = parseInt(tmpResult.Route.exhaustCO2);
                if (type == "price") {
                    if (priceViewFlag == "oneway") {
                        //片道
                        if (getPriceSummary("total", false) == minPriceSummary) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        //往復
                        if (getPriceSummary("total", true) == minPriceRoundSummary) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                } else if (type == "time") {
                    if (typeof minEkispertIndex != 'undefined') {
                        if (getDepartureState(result.ResultSet.Course[minEkispertIndex - 1]).getTime() == getDepartureState(tmpResult).getTime() && getArrivalState(result.ResultSet.Course[minEkispertIndex - 1]).getTime() == getArrivalState(tmpResult).getTime()) {
                            return true;
                        } else {
                            return false;
                        }
                    } else if (time == minTimeSummary) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (type == "transfer") {
                    if (TransferCount == minTransferCount) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (type == "teiki") {
                    if (typeof getPriceSummary("teiki6") != 'undefined') {
                        if (minTeikiSummary == getPriceSummary("teiki6")) {
                            return true;
                        }
                    } else if (typeof getPriceSummary("teiki3") != 'undefined') {
                        if (minTeikiSummary == getPriceSummary("teiki3") * 2) {
                            return true;
                        }
                    } else if (typeof getPriceSummary("teiki1") != 'undefined') {
                        if (minTeikiSummary == getPriceSummary("teiki1") * 6) {
                            return true;
                        }
                    }
                    return false;
                } else if (type == "teiki1") {
                    if (typeof getPriceSummary("teiki1") != 'undefined' && getPriceSummary("teiki1") == minTeiki1Summary) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (type == "teiki3") {
                    if (typeof getPriceSummary("teiki3") != 'undefined' && getPriceSummary("teiki3") == minTeiki3Summary) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (type == "teiki6") {
                    if (typeof getPriceSummary("teiki6") != 'undefined' && getPriceSummary("teiki6") == minTeiki6Summary) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (type == "co2") {
                    if (exhaustCO2 == minExhaustCO2) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    /**
    * 定期券利用のチェック
    */
    function checkWithTeiki() {
        if (typeof result != 'undefined') {
            var tmpResult;
            if (resultCount == 1) {
                tmpResult = result.ResultSet.Course;
            } else {
                tmpResult = result.ResultSet.Course[(selectNo - 1)];
            }
            if (typeof tmpResult.Price != 'undefined') {
                for (var j = 0; j < tmpResult.Price.length; j++) {
                    if (tmpResult.Price[j].kind == "Fare") {
                        if (typeof tmpResult.Price[j].Type != 'undefined') {
                            if (tmpResult.Price[j].Type == "WithTeiki") {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        } else {
            return;
        }
    }

    /**
    * 区間名のリストを取得
    */
    function getLineList() {
        var buffer = "";
        if (typeof result != 'undefined') {
            var tmpResult;
            if (resultCount == 1) {
                tmpResult = result.ResultSet.Course;
            } else {
                tmpResult = result.ResultSet.Course[(selectNo - 1)];
            }
            if (typeof tmpResult.Route.Line.length == 'undefined') {
                if (typeof tmpResult.Route.Line.Name != 'undefined') {
                    buffer += getTextValue(tmpResult.Route.Line.Name);
                }
            } else {
                for (var i = 0; i < tmpResult.Route.Line.length; i++) {
                    if (i != 0) { buffer += ","; }
                    if (typeof tmpResult.Route.Line[i].Name != 'undefined') {
                        buffer += getTextValue(tmpResult.Route.Line[i].Name);
                    }
                }
            }
        }
        return buffer;
    }

    /**
     * 区間名のリストを取得(代表名称を取得、存在しなければ通常の名称取得)
     */
     function getLineListTypical() {
         var buffer = "";
         if (typeof result != 'undefined') {
             var tmpResult;
             if (resultCount == 1) {
                 tmpResult = result.ResultSet.Course;
             } else {
                 tmpResult = result.ResultSet.Course[(selectNo - 1)];
             }
             if (typeof tmpResult.Route.Line.length == 'undefined') {
                 if (typeof tmpResult.Route.Line.TypicalName != 'undefined') {
                     buffer += getTextValue(tmpResult.Route.Line.TypicalName);
                 }else if(typeof tmpResult.Route.Line.Name != 'undefined') {
                     buffer += getTextValue(tmpResult.Route.Line.Name);
                 }
             } else {
                 for (var i = 0; i < tmpResult.Route.Line.length; i++) {
                     if (i != 0) { buffer += ","; }
                     if (typeof tmpResult.Route.Line[i].TypicalName != 'undefined') {
                         buffer += getTextValue(tmpResult.Route.Line[i].TypicalName);
                     }else if (typeof tmpResult.Route.Line[i].Name != 'undefined') {
                         buffer += getTextValue(tmpResult.Route.Line[i].Name);
                     }
                 }
             }
         }
         return buffer;
     }

     /**
      * 指定インデックスで使用された乗車券種別を取得
      */
      function getTicketKind(index) {
          if (typeof result != 'undefined') {
              var tmpResult;
              if (resultCount == 1) {
                  tmpResult = result.ResultSet.Course;
              } else {
                  tmpResult = result.ResultSet.Course[(selectNo - 1)];
              }
              if (typeof tmpResult.Price != 'undefined') {
                  for (var j = 0; j < tmpResult.Price.length; j++) {
                      if (tmpResult.Price[j].fromLineIndex == index) {
                          if (tmpResult.Price[j].kind == "Fare" || tmpResult.Price[j].kind == "Charge") {
                              if (tmpResult.Price[j].selected == "true") {
                            	  if (typeof tmpResult.Price[j].Name != 'undefined') {
                                      return getTextValue(tmpResult.Price[j].Name);
                            	  }
                              }
                          }
                      }
                  }
              }
              return "";
          } else {
              return "";
          }
      }

    /**
    * 区間オブジェクトを取得
    */
    function getLineObject(index) {
        var tmpLineObject;
        if (typeof result != 'undefined') {
            var tmpResult, lineObject;
            if (resultCount == 1) {
                tmpResult = result.ResultSet.Course;
            } else {
                tmpResult = result.ResultSet.Course[(selectNo - 1)];
            }
            if (typeof tmpResult.Route.Line.length == 'undefined') {
                if (index == 1) {
                    lineObject = tmpResult.Route.Line;
                }
            } else {
                if (typeof tmpResult.Route.Line[parseInt(index) - 1] != 'undefined') {
                    lineObject = tmpResult.Route.Line[parseInt(index) - 1];
                }
            }
            if (typeof lineObject != 'undefined') {
                tmpLineObject = new Object();
                // 名称
                if (typeof lineObject.Name != 'undefined') {
                    tmpLineObject.name = getTextValue(lineObject.Name);
                    // 略称
                    if (typeof lineObject.Name.abbreviation != 'undefined') {
                        tmpLineObject.abbreviation = lineObject.Name.abbreviation;
                    }
                }
                // タイプ
                if (typeof lineObject.Type != 'undefined') {
                    if (typeof lineObject.Type.text != 'undefined') {
                        if (typeof lineObject.Type.detail != 'undefined') {
                            tmpLineObject.type = lineObject.Type.text;
                            if (typeof lineObject.Type.detail != 'undefined') {
                                tmpLineObject.type_detail = lineObject.Type.text + "." + lineObject.Type.detail;
                            }
                        } else {
                            tmpLineObject.type = lineObject.Type.text;
                        }
                    } else {
                        tmpLineObject.type = lineObject.Type;
                    }
                }
                // 番号
                if (typeof lineObject.Number != 'undefined') {
                    tmpLineObject.number = Number(lineObject.Number);
                }
                // 色
                if (typeof lineObject.Color != 'undefined') {
                    tmpLineObject.color = Number(lineObject.Color);
                }
                // 発着時刻
                tmpLineObject.departureTime = convertISOtoTime(lineObject.DepartureState.Datetime.text, lineObject.DepartureState.Datetime.operation);
                tmpLineObject.arrivalTime = convertISOtoTime(lineObject.ArrivalState.Datetime.text, lineObject.ArrivalState.Datetime.operation);
                // 運行会社
                if (typeof lineObject.Corporation != 'undefined') {
                    if (typeof lineObject.Corporation.Name != 'undefined') {
                        tmpLineObject.corporation = lineObject.Corporation.Name;
                    }
                }
                // 軌道種別
                if (typeof lineObject.track != 'undefined') {
                    tmpLineObject.track = lineObject.track;
                }
            }
        }
        return tmpLineObject;
    }

    /**
    * 地点名のリストを取得
    */
    function getPointList() {
        var buffer = "";
        if (typeof result != 'undefined') {
            var tmpResult;
            if (resultCount == 1) {
                tmpResult = result.ResultSet.Course;
            } else {
                tmpResult = result.ResultSet.Course[(selectNo - 1)];
            }
            if (typeof tmpResult.Route.Point.length == 'undefined') {
                if (typeof tmpResult.Route.Point.Station != 'undefined') {
                    buffer += tmpResult.Route.Point.Station.Name;
                } else if (typeof tmpResult.Route.Point.Name != 'undefined') {
                    buffer += tmpResult.Route.Point.Name;
                }
            } else {
                for (var i = 0; i < tmpResult.Route.Point.length; i++) {
                    if (i != 0) { buffer += ","; }
                    if (typeof tmpResult.Route.Point[i].Station != 'undefined') {
                        buffer += tmpResult.Route.Point[i].Station.Name;
                    } else if (typeof tmpResult.Route.Point[i].Name != 'undefined') {
                        buffer += tmpResult.Route.Point[i].Name;
                    }
                }
            }
        }
        return buffer;
    }

    /**
    * 地点オブジェクトを取得
    */
    function getPointObject(index) {
        var tmp_station;
        if (typeof result != 'undefined') {
            var tmpResult;
            if (resultCount == 1) {
                tmpResult = result.ResultSet.Course;
            } else {
                tmpResult = result.ResultSet.Course[(selectNo - 1)];
            }
            if (typeof tmpResult.Route.Point[parseInt(index) - 1] != 'undefined') {
                var stationObj = tmpResult.Route.Point[parseInt(index) - 1];
                tmp_station = new Object();
                if (typeof stationObj.Station != 'undefined') {
                    tmp_station.name = stationObj.Station.Name;
                    tmp_station.code = stationObj.Station.code;
                    tmp_station.yode = stationObj.Station.Yomi;
                    if (typeof stationObj.Station.Type.text != 'undefined') {
                        tmp_station.type = stationObj.Station.Type.text;
                        if (typeof stationObj.Station.Type.detail != 'undefined') {
                            tmp_station.type_detail = stationObj.Station.Type.text + "." + stationObj.Station.Type.detail;
                        }
                    } else {
                        tmp_station.type = stationObj.Station.Type;
                    }
                } else if (typeof stationObj.Name != 'undefined') {
                    tmp_station.name = stationObj.Name;
                }
                if (typeof stationObj.GeoPoint != 'undefined') {
                    // 緯度
                    tmp_station.lati = stationObj.GeoPoint.lati;
                    tmp_station.lati_d = stationObj.GeoPoint.lati_d;
                    // 経度
                    tmp_station.longi = stationObj.GeoPoint.longi;
                    tmp_station.longi_d = stationObj.GeoPoint.longi_d;
                    // gcs
                    tmp_station.gcs = stationObj.GeoPoint.gcs;
                }
                //県コード
                if (typeof stationObj.Prefecture != 'undefined') {
                    tmp_station.kenCode = parseInt(stationObj.Prefecture.code);
                }
            }
        }
        return tmp_station;
    }

    /**
    * 乗車券のリストを出力
    */
    function getFareList(roundFlag) {
        return getPriceList("Fare", roundFlag);
    }

    /**
    * 特急券のリストを出力
    */
    function getChargeList(roundFlag) {
        return getPriceList("Charge", roundFlag);
    }

    /**
    * 運賃のリストを出力
    */
    function getPriceList(kind, roundFlag) {
        var priceList = new Array();
        if (typeof result != 'undefined') {
            var tmpResult;
            if (resultCount == 1) {
                tmpResult = result.ResultSet.Course;
            } else {
                tmpResult = result.ResultSet.Course[(selectNo - 1)];
            }
            if (typeof tmpResult.Price != 'undefined') {
                for (var i = 0; i < tmpResult.Price.length; i++) {
                    if (tmpResult.Price[i].kind == kind && tmpResult.Price[i].selected.toLowerCase() == "true") {
                        if (roundFlag == "round") {
                            if (typeof tmpResult.Price[i].Round != 'undefined') {
                                priceList.push(parseInt(getTextValue(tmpResult.Price[i].Round)));
                            }
                        } else {
                            if (typeof tmpResult.Price[i].Oneway != 'undefined') {
                                priceList.push(parseInt(getTextValue(tmpResult.Price[i].Oneway)));
                            }
                        }
                    }
                }
            }
        }
        if (priceList.length > 0) {
            return priceList.join(",");
        }
    }



    /**
     * 使用ルートの名称リストを出力
     * TODO 処理調整
     */
     function getTicketNameList(kind, roundFlag) {
         var ticketNameList = new Array();
         if (typeof result != 'undefined') {
             var tmpResult;
             if (resultCount == 1) {
                 tmpResult = result.ResultSet.Course;
             } else {
                 tmpResult = result.ResultSet.Course[(selectNo - 1)];
             }
             if (typeof tmpResult.Price != 'undefined') {
                 for (var i = 0; i < tmpResult.Price.length; i++) {
                     if ((tmpResult.Price[i].kind == 'Fare' || tmpResult.Price[i].kind == 'Charge') && tmpResult.Price[i].selected.toLowerCase() == "true") {
                          ticketNameList.push(getTextValue(tmpResult.Price[i].Name));
                     }
                 }
             }
         }
         if (ticketNameList.length > 0) {
             return ticketNameList.join(",");
         }
     }

     /**
      * 使用ルートの距離文字列を出力 全てkm単位で表示
      */
      function getDistance() {
          var buffer = "";
          if (typeof result != 'undefined') {
              var tmpResult;
              if (resultCount == 1) {
                  tmpResult = result.ResultSet.Course;
              } else {
                  tmpResult = result.ResultSet.Course[(selectNo - 1)];
              }
              if (typeof tmpResult.Route.distance != 'undefined') {
                  buffer += (parseInt(tmpResult.Route.distance) / 10) + "km";
              }
          }
          return buffer;
      }


    /**
    * 乗車券のオブジェクトを取得
    */
    function getFareObject(index) {
        return getPriceList("Fare", index);
    }

    /**
    * 特急券のオブジェクトを取得
    */
    function getChargeObject(index) {
        return getPriceList("Charge", index);
    }

    /**
    * 運賃のオブジェクトを取得
    */
    function getPriceObject(kind, index) {
        var tmp_price;
        if (typeof result != 'undefined') {
            var tmpResult;
            if (resultCount == 1) {
                tmpResult = result.ResultSet.Course;
            } else {
                tmpResult = result.ResultSet.Course[(selectNo - 1)];
            }
            if (typeof tmpResult.Price != 'undefined') {
                var price_index = 0;
                for (var i = 0; i < tmpResult.Price.length; i++) {
                    if (tmpResult.Price[i].kind == kind && tmpResult.Price[i].selected.toLowerCase() == "true") {
                        price_index++;
                        if (price_index == parseInt(index)) {
                            tmp_price = new Object();
                            tmp_price.fareRevisionStatus = tmpResult.Price[i].fareRevisionStatus;
                            tmp_price.fromLineIndex = tmpResult.Price[i].fromLineIndex;
                            tmp_price.toLineIndex = tmpResult.Price[i].toLineIndex;
                            tmp_price.selected = tmpResult.Price[i].selected.toLowerCase() == "true" ? true : false;
                            tmp_price.name = tmpResult.Price[i].Name;
                            tmp_price.type = tmpResult.Price[i].Type;
                            tmp_price.oneway = parseInt(getTextValue(tmpResult.Price[i].Oneway));
                            if (typeof tmpResult.Price[i].Round != 'undefined') {
                                tmp_price.round = parseInt(getTextValue(tmpResult.Price[i].Round));
                            }
                            tmp_price.rate = getTextValue(tmpResult.Price[i].Round);
                        }
                    }
                }
            }
        }
        return tmp_price;
    }

    /**
    * 運賃を取得
    */
    function getPrice(roundFlag) {
        if (roundFlag == "round") {
            return getPriceSummary("total", true);
        } else {
            return getPriceSummary("total", false);
        }
    }

    //20220218 追加
    /**
     * 往復割引の確認
     */
    function isRoundDiscount(){
        if(getPriceSummary("total",false) * 2 > getPriceSummary("total",true)){
            return true;
        }else{
            return false;
        }
    }
    //20220218 追加
    /**
     * 適応されている割引を取得（EX予約、eチケット）
     */
    function getFullRemark(){
 	   var fullRemarkName = "";
        if (typeof result != 'undefined') {
 	        var tmpResult;
 	        if(resultCount == 1) {
 	           tmpResult = result.ResultSet.Course;
 	        }else{
 	            tmpResult = result.ResultSet.Course[(selectNo - 1)];
 	        }

 	        if (typeof tmpResult.Price != 'undefined') {
 	        	for (var j = 0; j < tmpResult.Price.length; j++){
 	        		if (typeof tmpResult.Price[j].Oneway != 'undefined'){
 	        			if(typeof tmpResult.Price[j].Oneway.remark != 'undefined'){
 	        				if(fullRemarkName.length < 1){
 	        					fullRemarkName = getTextValue(tmpResult.Price[j].Oneway.fullRemark);
 	        				}else{
 	        					var remark = getTextValue(tmpResult.Price[j].Oneway.fullRemark);
 	        					if(fullRemarkName.indexOf(remark) < 0){
 	        						fullRemarkName = fullRemarkName + "　"+ remark;
 	        					}
 	        				}
 	        			}
 	        		}
 	        	}
 	        }
         }
        return fullRemarkName;
    }
    
    /**
     * 早安楽チェック
     */
    function isFastCheapMinTransfer(){
	//ひとまずコピペ
    //備考にくっつけるだけではだめになった　漢字に○付きで出す
    //　早安楽の3文字の文字列を返却して、jspで一文字ずつに分けてフラグとする？　111とか101とか001とか
        var hayastr = "0";
        var yasustr = "0";
        var rakustr = "0";
        var FareSummary = 0;
        var FareRoundSummary = 0;
        var ChargeSummary;
        var ChargeRoundSummary;
        var Teiki1Summary;
        var Teiki3Summary;
        var Teiki6Summary;
        
        if (typeof result != 'undefined'){
			var tmpResult;
			if(resultCount == 1) {
				tmpResult = result.ResultSet.Course;
			}else{
				tmpResult = result.ResultSet.Course[(selectNo - 1)];
			}
	        var time = parseInt(tmpResult.Route.timeOnBoard) + parseInt(tmpResult.Route.timeWalk) + parseInt(tmpResult.Route.timeOther);
	        var TransferCount = parseInt(tmpResult.Route.transferCount);
	        if (typeof tmpResult.Price != 'undefined') {
                for (var j = 0; j < tmpResult.Price.length; j++) {
                    if (tmpResult.Price[j].kind == "FareSummary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            FareSummary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                        if (typeof tmpResult.Price[j].Round != 'undefined') {
                            FareRoundSummary = parseInt(getTextValue(tmpResult.Price[j].Round));
                        }
                    } else if (tmpResult.Price[j].kind == "ChargeSummary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            ChargeSummary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                        if (typeof tmpResult.Price[j].Round != 'undefined') {
                            ChargeRoundSummary = parseInt(getTextValue(tmpResult.Price[j].Round));
                        }
                    } else if (tmpResult.Price[j].kind == "Teiki1Summary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            Teiki1Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                    } else if (tmpResult.Price[j].kind == "Teiki3Summary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            Teiki3Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                    } else if (tmpResult.Price[j].kind == "Teiki6Summary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            Teiki6Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                    } else if (tmpResult.Price[j].kind == "Teiki12Summary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            Teiki12Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                    }
                }
            }
            if (typeof minEkispertIndex != 'undefined') {
                if (getDepartureState(result.ResultSet.Course[minEkispertIndex - 1]).getTime() == getDepartureState(tmpResult).getTime() && getArrivalState(result.ResultSet.Course[minEkispertIndex - 1]).getTime() == getArrivalState(tmpResult).getTime()) {
                    hayastr = "1";
                }
            } else if (minTimeSummary == time) {
                hayastr = "1";
            }
            if (priceViewFlag == "oneway") {
                if (typeof ChargeSummary == 'undefined') {
                    if (minPriceSummary == FareSummary) {
                        yasustr = "1";
                    }
                } else {
                    if (minPriceSummary == (FareSummary + ChargeSummary)) {
                        yasustr = "1";
                    }
                }
            } else if (priceViewFlag == "round") {
                if (typeof ChargeRoundSummary == 'undefined') {
                    if (minPriceRoundSummary == FareRoundSummary) {
                        yasustr = "1";
                    }
                } else {
                    if (minPriceRoundSummary == (FareRoundSummary + ChargeRoundSummary)) {
                        yasustr = "1";
                    }
                }
            } else if (priceViewFlag == "teiki") {
                if (typeof Teiki6Summary != 'undefined') {
                    if (minTeikiSummary == Teiki6Summary) {
                        yasustr = "1";
                    }
                } else if (typeof Teiki3Summary != 'undefined') {

                    if (minTeikiSummary == Teiki3Summary * 2) {
                        yasustr = "1";
                    }
                } else if (typeof Teiki1Summary != 'undefined') {
                    if (minTeikiSummary == Teiki1Summary * 6) {
                        yasustr = "1";
                    }
                }
            }
            if (minTransferCount == TransferCount) {
                rakustr = "1";
            }
        }
        var str = ""+hayastr+yasustr+rakustr;
        return str;
	}


    /**
    * 乗車券を取得
    */
    function getFarePrice(roundFlag) {
        if (roundFlag == "round") {
            return getPriceSummary("fare", true);
        } else {
            return getPriceSummary("fare", false);
        }
    }

    /**
    * 特急券を取得
    */
    function getChargePrice(roundFlag) {
        if (roundFlag == "round") {
            return getPriceSummary("charge", true);
        } else {
            return getPriceSummary("charge", false);
        }
    }

    /**
    * 定期券を取得
    */
    function getTeikiPrice(month) {
        if (String(month) == "1") {
            return getPriceSummary("teiki1");
        } else if (String(month) == "3") {
            return getPriceSummary("teiki3");
        } else if (String(month) == "6") {
            return getPriceSummary("teiki6");
        } else if (String(month) == "12") {
            return getPriceSummary("teiki12");
        }
    }

    /**
    * 金額の計算
    */
    function getPriceSummary(type, round) {
        if (typeof result != 'undefined') {
            var tmpResult;
            if (resultCount == 1) {
                tmpResult = result.ResultSet.Course;
            } else {
                tmpResult = result.ResultSet.Course[(selectNo - 1)];
            }
            var FareSummary = 0;
            var FareRoundSummary = 0;
            var ChargeSummary = 0;
            var ChargeRoundSummary = 0;
            var Teiki1Summary;
            var Teiki3Summary;
            var Teiki6Summary;
            var Teiki12Summary;
            if (typeof tmpResult.Price != 'undefined') {
                for (var j = 0; j < tmpResult.Price.length; j++) {
                    if (tmpResult.Price[j].kind == "FareSummary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            FareSummary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                        if (typeof tmpResult.Price[j].Round != 'undefined') {
                            FareRoundSummary = parseInt(getTextValue(tmpResult.Price[j].Round));
                        }
                    } else if (tmpResult.Price[j].kind == "ChargeSummary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            ChargeSummary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                        if (typeof tmpResult.Price[j].Round != 'undefined') {
                            ChargeRoundSummary = parseInt(getTextValue(tmpResult.Price[j].Round));
                        }
                    } else if (tmpResult.Price[j].kind == "Teiki1Summary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            Teiki1Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                    } else if (tmpResult.Price[j].kind == "Teiki3Summary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            Teiki3Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                    } else if (tmpResult.Price[j].kind == "Teiki6Summary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            Teiki6Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                    } else if (tmpResult.Price[j].kind == "Teiki12Summary") {
                        if (typeof tmpResult.Price[j].Oneway != 'undefined') {
                            Teiki12Summary = parseInt(getTextValue(tmpResult.Price[j].Oneway));
                        }
                    }
                }
            }
            if (type == "total") {
                return (round ? FareRoundSummary + ChargeRoundSummary : FareSummary + ChargeSummary);
            } else if (type == "fare") {
                return (round ? FareRoundSummary : FareSummary);
            } else if (type == "charge") {
                return (round ? ChargeRoundSummary : ChargeSummary);
            } else if (type == "teiki1") {
                return Teiki1Summary;
            } else if (type == "teiki3") {
                return Teiki3Summary;
            } else if (type == "teiki6") {
                return Teiki6Summary;
            } else if (type == "teiki12") {
                return Teiki12Summary;
            }
        }
    }

    /**
    * 探索結果数を取得
    */
    function getResultCount() {
        return resultCount;
    }

    /**
     * サニタイズ
     */
    function sanitaize(str) {
        return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;');
    }

    /**
    * 環境設定
    */
    function setConfigure(name, value) {
        if (String(name).toLowerCase() == String("apiURL").toLowerCase()) {
            apiURL = value;
        } else if (name.toLowerCase() == String("key").toLowerCase()) {
            key = value;
        } else if (String(name).toLowerCase() == String("PriceChangeRefresh").toLowerCase()) {
            priceChangeRefreshFlag = (String(value) == "true" ? true : false);
        } else if (String(name).toLowerCase() == String("PriceChange").toLowerCase()) {
            priceChangeFlag = (String(value) == "true" ? true : false);
        } else if (String(name).toLowerCase() == String("AssignDia").toLowerCase()) {
            assignDiaFlag = (String(value) == "true" ? true : false);
        } else if (String(name).toLowerCase() == String("checkEngineVersion").toLowerCase()) {
            checkEngineVersion = (String(value) == "true" ? true : false);
        } else if (String(name).toLowerCase() == String("CourseList").toLowerCase()) {
            courseListFlag = (String(value) == "true" ? true : false);
        } else if (String(name).toLowerCase() == String("Agent").toLowerCase()) {
            agent = value;
        } else if (String(name).toLowerCase() == String("window").toLowerCase()) {
            windowFlag = (String(value) == "true" ? true : false);
        } else if (String(name).toLowerCase() == String("tab").toLowerCase()) {
            resultTab = (String(value) == "hidden" ? false : true);
            sortType = "time";
        } else if (String(name).toLowerCase() == String("CourseDisplay").toLowerCase()) {
            if (String(value).toLowerCase() == "all") {
                courseDisplayAll = true;
            } else if (String(value).toLowerCase() == "once") {
                courseDisplayAll = false;
            }
        } else if (String(name).toLowerCase() == String("from").toLowerCase()) {
            fromName = value;
        } else if (String(name).toLowerCase() == String("to").toLowerCase()) {
            toName = value;
        } else if (String(name).toLowerCase() == String("selectedIndex").toLowerCase()) {
            selectedIndex = parseInt(value);
        } else if (String(name).toLowerCase() == String("ssl").toLowerCase()) {
            if (String(value).toLowerCase() == "true" || String(value).toLowerCase() == "enable" || String(value).toLowerCase() == "enabled") {
                apiURL = apiURL.replace('http://', 'https://');
            } else {
                apiURL = apiURL.replace('https://', 'http://');
            }
        }
    }

    /**
    * 探索オブジェクトのインターフェースを返す
    */
    function createSearchInterface() {
        return new searchInterface();
    };

    /**
    * 探索インターフェースオブジェクト
    */
    function searchInterface() {
        // データリスト
        var viaList;
        var fixedRailList;
        var fixedRailDirectionList;
        var date;
        var time;
        var searchType;
        var sort;
        var answerCount;
        var searchCount;
        var conditionDetail;
        var corporationBind;
        var interruptCorporationList;
        var interruptRailList;
        var resultDetail;
        var assignRoute;
        var assignDetailRoute;
        var assignNikukanteikiIndex;
        var coupon;
        var bringAssignmentError;
        // 関数リスト
        // ViaList設定
        function setViaList(value) { viaList = value; };
        function getViaList() { return viaList; };
        this.setViaList = setViaList;
        this.getViaList = getViaList;
        // FixedRailList設定
        function setFixedRailList(value) { fixedRailList = value; };
        function getFixedRailList() { return fixedRailList; };
        this.setFixedRailList = setFixedRailList;
        this.getFixedRailList = getFixedRailList;
        // FixedRailDirectionList設定
        function setFixedRailDirectionList(value) { fixedRailDirectionList = value; };
        function getFixedRailDirectionList() { return fixedRailDirectionList; };
        this.setFixedRailDirectionList = setFixedRailDirectionList;
        this.getFixedRailDirectionList = getFixedRailDirectionList;
        // Date設定
        function setDate(value) { date = value; };
        function getDate() { return date; };
        this.setDate = setDate;
        this.getDate = getDate;
        // Time設定
        function setTime(value) { time = value; };
        function getTime() { return time; };
        this.setTime = setTime;
        this.getTime = getTime;
        // SearchType設定
        function setSearchType(value) { searchType = value; };
        function getSearchType() { return searchType; };
        this.setSearchType = setSearchType;
        this.getSearchType = getSearchType;
        // Sort設定
        function setSort(value) { sort = value; };
        function getSort() { return sort; };
        this.setSort = setSort;
        this.getSort = getSort;
        // AnswerCount設定
        function setAnswerCount(value) { answerCount = value; };
        function getAnswerCount() { return answerCount; };
        this.setAnswerCount = setAnswerCount;
        this.getAnswerCount = getAnswerCount;
        // SearchCount設定
        function setSearchCount(value) { searchCount = value; };
        function getSearchCount() { return searchCount; };
        this.setSearchCount = setSearchCount;
        this.getSearchCount = getSearchCount;
        // ConditionDetail設定
        function setConditionDetail(value) { conditionDetail = value; };
        function getConditionDetail() { return conditionDetail; };
        this.setConditionDetail = setConditionDetail;
        this.getConditionDetail = getConditionDetail;
        // CorporationBind設定
        function setCorporationBind(value) { corporationBind = value; };
        function getCorporationBind() { return corporationBind; };
        this.setCorporationBind = setCorporationBind;
        this.getCorporationBind = getCorporationBind;
        // InterruptCorporationList設定
        function setInterruptCorporationList(value) { interruptCorporationList = value; };
        function getInterruptCorporationList() { return interruptCorporationList; };
        this.setInterruptCorporationList = setInterruptCorporationList;
        this.getInterruptCorporationList = getInterruptCorporationList;
        // InterruptRailList設定
        function setInterruptRailList(value) { interruptRailList = value; };
        function getInterruptRailList() { return interruptRailList; };
        this.setInterruptRailList = setInterruptRailList;
        this.getInterruptRailList = getInterruptRailList;
        // ResultDetail設定
        function setResultDetail(value) { resultDetail = value; };
        function getResultDetail() { return resultDetail; };
        this.setResultDetail = setResultDetail;
        this.getResultDetail = getResultDetail;
        // AssignRoute設定
        function setAssignRoute(value) { assignRoute = value; };
        function getAssignRoute() { return assignRoute; };
        this.setAssignRoute = setAssignRoute;
        this.getAssignRoute = getAssignRoute;
        // AssignDetailRoute設定
        function setAssignDetailRoute(value) { assignDetailRoute = value; };
        function getAssignDetailRoute() { return assignDetailRoute; };
        this.setAssignDetailRoute = setAssignDetailRoute;
        this.getAssignDetailRoute = getAssignDetailRoute;
        // AssignNikukanteikiIndex設定
        function setAssignNikukanteikiIndex(value) { assignNikukanteikiIndex = value; };
        function getAssignNikukanteikiIndex() { return assignNikukanteikiIndex; };
        this.setAssignNikukanteikiIndex = setAssignNikukanteikiIndex;
        this.getAssignNikukanteikiIndex = getAssignNikukanteikiIndex;
        // Coupon設定
        function setCoupon(value) { coupon = value; };
        function getCoupon() { return coupon; };
        this.setCoupon = setCoupon;
        this.getCoupon = getCoupon;
        // 金額設定
        var priceType;
        function setPriceType(value) { priceType = value; };
        function getPriceType() { return priceType; };
        this.setPriceType = setPriceType;
        this.getPriceType = getPriceType;
        // 割り当てエラーの場合にエラーとする
        var bringAssignmentError;
        function setBringAssignmentError(value) { bringAssignmentError = value; };
        function getBringAssignmentError() { return bringAssignmentError; };
        this.setBringAssignmentError = setBringAssignmentError;
        this.getBringAssignmentError = getBringAssignmentError;
    };

    /**
    * コールバック関数の設定
    */
    function bind(type, func) {
        if (type == 'change' && typeof func == 'function') {
            callBackFunctionBind[type] = func;
        } else if (type == 'click' && typeof func == 'function') {
            callBackFunctionBind[type] = func;
        } else if (type == 'close' && typeof func == 'function') {
            callBackFunctionBind[type] = func;
        } else if (type == 'select' && typeof func == 'function') {
            callBackFunctionBind[type] = func;
        }
    }

    /**
    * コールバック関数の解除
    */
    function unbind(type) {
        if (typeof callBackFunctionBind[type] != undefined) {
            callBackFunctionBind[type] = undefined;
        }
    }

    /**
    * メニューオブジェクト作成
    */
    var menu = function (p_text, p_callBack, mask) {
        var text = p_text;
        var callBack = p_callBack;
        var type;
        var mask;
        this.text = text;
        this.callBack = callBack;
        this.mask = mask;
    };

    /**
    * 路線メニューを追加
    */
    function addLineMenu(obj) {
        callBackObjectLine.push(obj);
    };

    /**
    * 駅メニューを追加
    */
    function addPointMenu(obj) {
        callBackObjectStation.push(obj);
    };

    /**
     * ResultSet / Course / Price / RevisionStatusの文言から内部の運賃改定フラグを判定する。
     * ・2 previous		運賃改定に未対応の金額
     * ・1 forecast		「駅すぱあと」が予測した運賃改定後の見込の金額  を利用
     * ・1 predicted		予測計算した金額。 applyPredictedConsumptionTax に true を指定時、計算が適用された場合に返ります
     * 					※ もしくは、消費税率変更時に交通事業者が運賃改定を行わない場合の据え置きの金額
     * ・0 latest		運賃改定に対応後の金額。
     * @param RevisionStatus	エキスパートの返した価格の運賃改定対応状況
     * @param revStatFlg		現在の内部運賃改定フラグ
     * @return					再判定後の内部運賃改定フラグ
     */
    function judgeRevStatFlg(RevisionStatus, revStatFlg){
        if(RevisionStatus == 'predicted' && revStatFlg < 1){
            revStatFlg = 1
        }
        if(RevisionStatus == 'forecast' && revStatFlg < 1){
            revStatFlg = 1
        }
        if(RevisionStatus == 'previous' && revStatFlg < 2){
            revStatFlg = 2;
        }
        return revStatFlg;
    }

    /**
     * 内部の運賃改定フラグから文言を生成します。
     * @param revStatFlg		内部運賃改定フラグ
     * @returns					文言
     */
    function revStatFlg2Msg(revStatFlg){
    	var ret = "";
    	if(revStatFlg == 1){
    		ret = "「駅すぱあと」が予測した運賃改定後の見込の金額を含みます。";
    	}else if(revStatFlg == 2){
    		ret = "運賃改定に未対応の金額を含みます。";
    	}
    	return ret;
    }

    /**
    * 選択経路に運賃改定未対応金額を含むかどうか。
    */
    function getKaiteiAlert() {
        if (typeof result != 'undefined') {
            var courseObj;
            if (resultCount == 1) {
                courseObj = result.ResultSet.Course;
            } else {
                courseObj = result.ResultSet.Course[(selectNo - 1)];
            }

            // 運賃改定未対応
            var FareSummarySalesTaxRateIsNotSupported = false;
            var ChargeSummarySalesTaxRateIsNotSupported = false;
            var Teiki1SummarySalesTaxRateIsNotSupported = false;
            var Teiki3SummarySalesTaxRateIsNotSupported = false;
            var Teiki6SummarySalesTaxRateIsNotSupported = false;
            var Teiki12SummarySalesTaxRateIsNotSupported = false;
            var revStatFlg = 0;
            if (typeof courseObj.Price != 'undefined') {
                for (var j = 0; j < courseObj.Price.length; j++) {
                    if (typeof courseObj.Price[j].RevisionStatus != 'undefined') {
                        if (courseObj.Price[j].RevisionStatus != 'latest') {
                            if (courseObj.Price[j].kind == "Fare") {
                                FareSummarySalesTaxRateIsNotSupported = true;
                                revStatFlg = judgeRevStatFlg(courseObj.Price[j].RevisionStatus, revStatFlg);
                            } else if (courseObj.Price[j].kind == "Charge") {
                                ChargeSummarySalesTaxRateIsNotSupported = true;
                                revStatFlg = judgeRevStatFlg(courseObj.Price[j].RevisionStatus, revStatFlg);
                            } else if (courseObj.Price[j].kind == "Teiki1") {
                                Teiki1SummarySalesTaxRateIsNotSupported = true;
                                revStatFlg = judgeRevStatFlg(courseObj.Price[j].RevisionStatus, revStatFlg);
                            } else if (courseObj.Price[j].kind == "Teiki3") {
                                Teiki3SummarySalesTaxRateIsNotSupported = true;
                                revStatFlg = judgeRevStatFlg(courseObj.Price[j].RevisionStatus, revStatFlg);
                            } else if (courseObj.Price[j].kind == "Teiki6") {
                                Teiki6SummarySalesTaxRateIsNotSupported = true;
                                revStatFlg = judgeRevStatFlg(courseObj.Price[j].RevisionStatus, revStatFlg);
                            } else if (courseObj.Price[j].kind == "Teiki12") {
                                Teiki12SummarySalesTaxRateIsNotSupported = true;
                                revStatFlg = judgeRevStatFlg(courseObj.Price[j].RevisionStatus, revStatFlg);
                            }
                        }
                    }
                }
            }
            var salesTaxRateIsNotSupported = (FareSummarySalesTaxRateIsNotSupported || ChargeSummarySalesTaxRateIsNotSupported);
            // 運賃改定未対応
	        if (priceViewFlag == "oneway" || priceViewFlag == "round") {
	            if (salesTaxRateIsNotSupported) {
	            	return revStatFlg2Msg(revStatFlg);
	         	}
	        } else if (priceViewFlag == "teiki") {
	            if (Teiki1SummarySalesTaxRateIsNotSupported || Teiki3SummarySalesTaxRateIsNotSupported || Teiki6SummarySalesTaxRateIsNotSupported || Teiki12SummarySalesTaxRateIsNotSupported) {
	            	return revStatFlg2Msg(revStatFlg);
	            }
	        }
        }
        return "";
    }

    // 外部参照可能な関数リスト
    this.dispCourse = dispCourse;
    this.search = search;
    this.changeCourse = changeCourse;
    this.dispCourseList = dispCourseList;
    this.getSerializeData = getSerializeData;
    this.getSerializeDataAll = getSerializeDataAll;
    this.getTeikiSerializeData = getTeikiSerializeData;
    this.getTeiki = getTeiki;
    this.getNikukanteikiIndex = getNikukanteikiIndex;
    this.getVehicleIndex = getVehicleIndex;
    this.getPassStatusObject = getPassStatusObject;
    this.getResultNo = getResultNo;
    this.getResult = getResult;
    this.getResultString = getResultString;
    this.getResultAll = getResultAll;
    this.getResultStringAll = getResultStringAll;
    this.getDepartureDate = getDepartureDate;
    this.getArrivalDate = getArrivalDate;
    this.checkBestCourse = checkBestCourse;
    this.checkWithTeiki = checkWithTeiki;
    this.setResult = setResult;
    this.setPriceType = setPriceType;
    this.setSerializeData = setSerializeData;
    this.getLineList = getLineList;
    this.getLineListTypical = getLineListTypical;
    this.getTicketKind = getTicketKind;
    this.getLineObject = getLineObject;
    this.getPointList = getPointList;
    this.getPointObject = getPointObject;
    this.getPrice = getPrice;
    this.getFarePrice = getFarePrice;
    this.getChargePrice = getChargePrice;
    this.getTeikiPrice = getTeikiPrice;
    this.getResultCount = getResultCount;
    this.getFareList = getFareList;
    this.getTicketNameList = getTicketNameList;
    this.getDistance = getDistance;
    this.getFareObject = getFareObject;
    this.getChargeList = getChargeList;
    this.getChargeObject = getChargeObject;
    this.createSearchInterface = createSearchInterface;
    this.setConfigure = setConfigure;
    this.courseEdit = courseEdit;
    this.bind = bind;
    this.unbind = unbind;
    this.menu = menu;
    this.addLineMenu = addLineMenu;
    this.addPointMenu = addPointMenu;
    this.getKaiteiAlert = getKaiteiAlert;

    //20220218 追加
    this.isRoundDiscount = isRoundDiscount;
    this.getFullRemark = getFullRemark;
    
    //202210 早楽安判定メソッド
    this.isFastCheapMinTransfer = isFastCheapMinTransfer;

    // 定数リスト
    this.SORT_EKISPERT = "ekispert";
    this.SORT_PRICE = "price";
    this.SORT_TIME = "time";
    this.SORT_TEIKI = "teiki";
    this.SORT_TRANSFER = "transfer";
    this.SORT_CO2 = "co2";
    this.SORT_TEIKI1 = "teiki1";
    this.SORT_TEIKI3 = "teiki3";
    this.SORT_TEIKI6 = "teiki6";
    this.PRICE_ONEWAY = "oneway";
    this.PRICE_ROUND = "round";
    this.PRICE_TEIKI = "teiki";
    this.TEIKI1 = "1";
    this.TEIKI3 = "3";
    this.TEIKI6 = "6";
    this.SEARCHTYPE_DEPARTURE = "departure";
    this.SEARCHTYPE_ARRIVAL = "arrival";
    this.SEARCHTYPE_FIRSTTRAIN = "firstTrain";
    this.SEARCHTYPE_LASTTRAIN = "lastTrain";
    this.SEARCHTYPE_PLAIN = "plain";
    this.TYPE_TRAIN = "train";
    this.TYPE_PLANE = "plane";
    this.TYPE_SHIP = "ship";
    this.TYPE_BUS = "bus";
    this.TYPE_WALK = "walk";
    this.TYPE_STRANGE = "strange";
    this.TYPE_BUS_LOCAL = "bus.local";
    this.TYPE_BUS_CONNECTION = "bus.connection";
    this.TYPE_BUS_HIGHWAY = "bus.highway";
    this.TYPE_BUS_MIDNIGHT = "bus.midnight";
    this.TYPE_TRAIN_LIMITEDEXPRESS = "train.limitedExpress";
    this.TYPE_TRAIN_SHINKANSEN = "train.shinkansen";
    this.TYPE_TRAIN_SLEEPERTRAIN = "train.sleeperTrain";
    this.TYPE_TRAIN_LINER = "train.liner";
    this.TDFK_HOKKAIDO = 1;
    this.TDFK_AOMORI = 2;
    this.TDFK_IWATE = 3;
    this.TDFK_MIYAGI = 4;
    this.TDFK_AKITA = 5;
    this.TDFK_YAMAGATA = 6;
    this.TDFK_FUKUSHIMA = 7;
    this.TDFK_IBARAKI = 8;
    this.TDFK_TOCHIGI = 9;
    this.TDFK_GUNMA = 10;
    this.TDFK_SAITAMA = 11;
    this.TDFK_CHIBA = 12;
    this.TDFK_TOKYO = 13;
    this.TDFK_KANAGAWA = 14;
    this.TDFK_NIIGATA = 15;
    this.TDFK_TOYAMA = 16;
    this.TDFK_ISHIKAWA = 17;
    this.TDFK_FUKUI = 18;
    this.TDFK_YAMANASHI = 19;
    this.TDFK_NAGANO = 20;
    this.TDFK_GIFU = 21;
    this.TDFK_SHIZUOKA = 22;
    this.TDFK_AICHI = 23;
    this.TDFK_MIE = 24;
    this.TDFK_SHIGA = 25;
    this.TDFK_KYOTO = 26;
    this.TDFK_OSAKA = 27;
    this.TDFK_HYOGO = 28;
    this.TDFK_NARA = 29;
    this.TDFK_WAKAYAMA = 30;
    this.TDFK_TOTTORI = 31;
    this.TDFK_SHIMANE = 32;
    this.TDFK_OKAYAMA = 33;
    this.TDFK_HIROSHIMA = 34;
    this.TDFK_YAMAGUCHI = 35;
    this.TDFK_TOKUSHIMA = 36;
    this.TDFK_KAGAWA = 37;
    this.TDFK_EHIME = 38;
    this.TDFK_KOCHI = 39;
    this.TDFK_FUKUOKA = 40;
    this.TDFK_SAGA = 41;
    this.TDFK_NAGASAKI = 42;
    this.TDFK_KUMAMOTO = 43;
    this.TDFK_OITA = 44;
    this.TDFK_MIYAZAKI = 45;
    this.TDFK_KAGOSHIMA = 46;
    this.TDFK_OKINAWA = 47;

    // 端末制御
    this.AGENT_PC = 1;
    this.AGENT_PHONE = 2;
    this.AGENT_TABLET = 3;
};

/**
 *  駅すぱあと Web サービス
 *  日付入力パーツ
 *  サンプルコード
 *  https://github.com/EkispertWebService/GUI
 *
 *  Version:2016-08-04
 *
 *  Copyright (C) Val Laboratory Corporation. All rights reserved.
 **/

var expGuiDateTime = function (pObject, config) {
    // ドキュメントのオブジェクトを格納
    var documentObject = pObject;
    var baseId = pObject.id;

    // Webサービスの設定
    var apiURL = ekicom_url;
    var key = ekicom_key;

    // AGENTのチェック
    var agent = 1;
    var isiPad = navigator.userAgent.match(/iPad/i) != null;
    var isiPhone = navigator.userAgent.match(/iPhone/i) != null;
    var isAndroid_phone = (navigator.userAgent.match(/Android/i) != null && navigator.userAgent.match(/Mobile/i) != null);
    var isAndroid_tablet = (navigator.userAgent.match(/Android/i) != null && navigator.userAgent.match(/Mobile/i) == null);
    if (isiPhone || isAndroid_phone) { agent = 2; }
    if (isiPad || isAndroid_tablet) { agent = 3; }

    /**
    * イベントの設定(IE対応版)
    */
    function addEvent(element, eventName, func) {
        if (element) {
            if (typeof eventName == 'string' && typeof func == 'function') {
                if (element.addEventListener) {
                    element.addEventListener(eventName, func, false);
                } else if (element.attachEvent) {
                    element.attachEvent("on" + eventName, func);
                }
            }
        }
    }

    // 変数郡
    // カレンダー連携用変数
    var c_year;
    var c_month;
    var c_date;

    /**
    * 日時入力の設置
    */
    function dispDateTime(type) {
        // 探索条件・日付・時間設定のテーブル
        var buffer = "";
        if (agent == 1) {
            buffer += '<div class="expGuiDateTime expGuiDateTimePc">';
            buffer += '<input type="hidden" id="' + baseId + ':searchType">';
            buffer += '<div id="' + baseId + ':searchTypeList" class="exp_searchTypeList exp_clearfix">';
            buffer += '<div id="' + baseId + ':searchType:dia">';
            buffer += '<div class="exp_searchTypeDepartureOn" id="' + baseId + ':searchType:' + String(1) + ':active"><span class="exp_text">出発</span></div>';
            buffer += '<div class="exp_searchTypeDepartureOff" id="' + baseId + ':searchType:' + String(1) + ':none">';
            buffer += '<a class="exp_searchTypeButton" id="' + baseId + ':searchType:' + String(1) + '" href="javascript:void(0);"><span class="exp_text">出発</span></a>';
            buffer += '</div>';
            buffer += '<div class="exp_searchTypeArrivalOn" id="' + baseId + ':searchType:' + String(2) + ':active"><span class="exp_text">到着</span></div>';
            buffer += '<div class="exp_searchTypeArrivalOff" id="' + baseId + ':searchType:' + String(2) + ':none">';
            buffer += '<a class="exp_searchTypeButton" id="' + baseId + ':searchType:' + String(2) + '" href="javascript:void(0);"><span class="exp_text">到着</span></a>';
            buffer += '</div>';
            buffer += '<div class="exp_searchTypeFirstTrainOn" id="' + baseId + ':searchType:' + String(3) + ':active"><span class="exp_text">始発</span></div>';
            buffer += '<div class="exp_searchTypeFirstTrainOff" id="' + baseId + ':searchType:' + String(3) + ':none">';
            buffer += '<a class="exp_searchTypeButton" id="' + baseId + ':searchType:' + String(3) + '" href="javascript:void(0);"><span class="exp_text">始発</span></a>';
            buffer += '</div>';
            buffer += '<div class="exp_searchTypeLastTrainOn" id="' + baseId + ':searchType:' + String(4) + ':active"><span class="exp_text">終電</span></div>';
            buffer += '<div class="exp_searchTypeLastTrainOff" id="' + baseId + ':searchType:' + String(4) + ':none">';
            buffer += '<a class="exp_searchTypeButton" id="' + baseId + ':searchType:' + String(4) + '" href="javascript:void(0);"><span class="exp_text">終電</span></a>';
            buffer += '</div>';
            buffer += '</div>';
            buffer += '<div id="' + baseId + ':searchType:average">';
            buffer += '<div class="exp_searchTypePlainOn" id="' + baseId + ':searchType:' + String(5) + ':active"><span class="exp_text">平均</span></div>';
            buffer += '<div class="exp_searchTypePlainOff" id="' + baseId + ':searchType:' + String(5) + ':none">';
            buffer += '<a class="exp_searchTypeButton" id="' + baseId + ':searchType:' + String(5) + '" href="javascript:void(0);"><span class="exp_text">平均</span></a>';
            buffer += '</div>';
            buffer += '</div>';
            buffer += '</div>';
        } else if (agent == 2 || agent == 3) {
            if (agent == 2) {
                buffer += '<div class="expGuiDateTime expGuiDateTimePhone">';
            } else if (agent == 3) {
                buffer += '<div class="expGuiDateTime expGuiDateTimeTablet">';
            }
            buffer += '<div id="' + baseId + ':searchTypeList" class="exp_searchType exp_clearfix">';
            buffer += '<div id="' + baseId + ':searchType:dia">';
            buffer += '<span class="exp_departure"><input type="radio" id="' + baseId + ':searchType:1" name="' + baseId + ':searchType" value="departure" id="' + baseId + ':searchType:1"><label for="' + baseId + ':searchType:1" id="' + baseId + ':searchType:1:text">出発</label></span>';
            buffer += '<span class="exp_arrival"><input type="radio" id="' + baseId + ':searchType:2" name="' + baseId + ':searchType" value="arrival" id="' + baseId + ':searchType:2"><label for="' + baseId + ':searchType:2" id="' + baseId + ':searchType:2:text">到着</label></span>';
            buffer += '<span class="exp_firstTrain"><input type="radio" id="' + baseId + ':searchType:3" name="' + baseId + ':searchType" value="firstTrain" id="' + baseId + ':searchType:3"><label for="' + baseId + ':searchType:3" id="' + baseId + ':searchType:3:text">始発</label></span>';
            buffer += '<span class="exp_lastTrain"><input type="radio" id="' + baseId + ':searchType:4" name="' + baseId + ':searchType" value="lastTrain" id="' + baseId + ':searchType:4"><label for="' + baseId + ':searchType:4" id="' + baseId + ':searchType:4:text">終電</span></span>';
            buffer += '</div>';
            buffer += '<div id="' + baseId + ':searchType:average">';
            buffer += '<span class="exp_plain"><input type="radio" id="' + baseId + ':searchType:5" name="' + baseId + ':searchType" value="plain" id="' + baseId + ':searchType:5"><label for="' + baseId + ':searchType:5" id="' + baseId + ':searchType:5:text">平均</label></span>';
            buffer += '</div>';
            buffer += '</div>';
        }
        buffer += '<div class="exp_dateTime exp_clearfix">';
        buffer += '<div id="' + baseId + ':calendar" style="display:none;"></div>';
        buffer += '<div class="exp_dateGroup">';
        if (agent == 1) {
            buffer += '<select id="' + baseId + ':date:mm" class="exp_date" style="width:120px;"></select>';
            buffer += '<select id="' + baseId + ':date:dd" class="exp_date" style="width:90px;"></select>';
        } else if (agent == 2 || agent == 3) {
            buffer += '<select id="' + baseId + ':date" class="exp_date"></select>';
        }
        buffer += '</div>';
        buffer += '<a class="exp_cal_open" id="' + baseId + ':cal_open" href="javascript:void(0);"></a>';
        buffer += '<div id="' + baseId + ':time">';
        // 改行
        if (agent == 2) {
            buffer += '<div class="exp_separate"></div>';
        }
        buffer += '<div class="exp_timeGroup">';
        buffer += '<select class="exp_time" id="' + baseId + ':timeHH" style="width:60px;">';
        for (var i = 0; i <= 23; i++) {
            buffer += '<option value="' + i + '">' + String(i) + '時</option>';
        }
        buffer += '</select>';
        buffer += '<select class="exp_time" id="' + baseId + ':timeMM" style="width:60px;">';
        for (var i = 0; i <= 59; i++) {
            buffer += '<option value="' + i + '">' + String(((i <= 9) ? '0' : '') + i) + '分</option>';
        }
        buffer += '</select>';
        buffer += '</div>';
        buffer += '<a class="exp_setNow" id="' + baseId + ':setNow" href="javascript:void(0);"></a>';
        buffer += '</div>';
        buffer += '</div>';
        buffer += '</div>';
        // HTMLへ出力
        documentObject.innerHTML = buffer;

        // デフォルト設定
        setSearchType("departure");

        // 表示設定
        if (typeof type != 'undefined') {
            if (type == "dia") {
                // 駅すぱあと for Web用の設定
                document.getElementById(baseId + ":searchType:average").style.display = "none";
            } else if (type == "plain") {
                document.getElementById(baseId + ":searchTypeList").style.display = "none";
                document.getElementById(baseId + ":time").style.display = "none";
                setSearchType("plain");
            }
        }

        // イベントの設定
        addEvent(document.getElementById(baseId + ":searchType:1"), "click", function () { setSearchType("departure"); });
        addEvent(document.getElementById(baseId + ":searchType:2"), "click", function () { setSearchType("arrival"); });
        addEvent(document.getElementById(baseId + ":searchType:3"), "click", function () { setSearchType("firstTrain"); });
        addEvent(document.getElementById(baseId + ":searchType:4"), "click", function () { setSearchType("lastTrain"); });
        addEvent(document.getElementById(baseId + ":searchType:5"), "click", function () { setSearchType("plain"); });
        addEvent(document.getElementById(baseId + ":cal_open"), "click", function () { changeCalendar(); });
        if (agent == 1) {
            addEvent(document.getElementById(baseId + ":date:mm"), "change", function () { changeDate(); });
        }
        addEvent(document.getElementById(baseId + ":setNow"), "click", function () { setNow(); });
        // デフォルトの日時設定
        setNow();
    }

    /**
    * 現在日時をフォームに設定
    */
    function setNow() {
        // 現在日時の設定
        var now = new Date();
        setDate(now.getFullYear() + "/" + (now.getMonth() + 1) + "/" + now.getDate());
        document.getElementById(baseId + ':timeHH').selectedIndex = now.getHours();
        document.getElementById(baseId + ':timeMM').selectedIndex = now.getMinutes();
    }

    /**
    * 年月を変更する
    */
    function changeDate() {
        var tmp_date = document.getElementById(baseId + ':date:mm').value.split("/");
        tmp_date.push(document.getElementById(baseId + ':date:dd').value);
        setDateList(tmp_date[0], tmp_date[1]);
        setDate(tmp_date.join("/"));
    }

    /**
    * 日付設定を取得する
    */
    function getDate() {
        var tmp_date = new Array();
        if (agent == 1) {
            tmp_date = document.getElementById(baseId + ':date:mm').value.split("/");
            tmp_date.push(document.getElementById(baseId + ':date:dd').value);
        } else if (agent == 2 || agent == 3) {
            tmp_date = document.getElementById(baseId + ':date').value.split("/");
        }
        // 日付設定
        var buffer = "";
        buffer += tmp_date[0];
        if (parseInt(tmp_date[1], 10) >= 10) {
            buffer += parseInt(tmp_date[1], 10);
        } else {
            buffer += "0" + parseInt(tmp_date[1], 10);
        }
        if (parseInt(tmp_date[2], 10) >= 10) {
            buffer += parseInt(tmp_date[2], 10);
        } else {
            buffer += "0" + parseInt(tmp_date[2], 10);
        }
        return buffer;
    }

    /**
    * 時間設定を取得する
    */
    function getTime() {
        var hh;
        var mi;
        if (document.getElementById(baseId + ':timeHH').selectedIndex < 10) {
            hh = "0" + String(document.getElementById(baseId + ':timeHH').selectedIndex);
        } else {
            hh = String(document.getElementById(baseId + ':timeHH').selectedIndex);
        }
        if (document.getElementById(baseId + ':timeMM').selectedIndex < 10) {
            mi = "0" + String(document.getElementById(baseId + ':timeMM').selectedIndex);
        } else {
            mi = String(document.getElementById(baseId + ':timeMM').selectedIndex);
        }
        return String(hh) + String(mi);
    }

    /**
    * 日付の正当性チェック
    */
    function checkDate() {
        var tmp_date = new Array();
        if (agent == 1) {
            tmp_date = document.getElementById(baseId + ':date:mm').value.split("/");
            tmp_date.push(document.getElementById(baseId + ':date:dd').value);
        } else if (agent == 2 || agent == 3) {
            tmp_date = document.getElementById(baseId + ':date').value.split("/");
        }
        if (tmp_date.length != 3) {
            return false;
        } else if (isNaN(parseInt(tmp_date[0], 10)) || isNaN(parseInt(tmp_date[1], 10)) || isNaN(parseInt(tmp_date[2], 10))) {
            return false;
        }
        return true;
    }

    /**
    * 探索種別を取得
    */
    function getSearchType() {
        if (agent == 1) {
            return document.getElementById(baseId + ':searchType').value;
        } else if (agent == 2 || agent == 3) {
            for (var i = 0; i < document.getElementsByName(baseId + ':searchType').length; i++) {
                if (document.getElementsByName(baseId + ':searchType')[i].checked == true) {
                    return document.getElementsByName(baseId + ':searchType')[i].value;
                }
            }
        }
        return;
    }

    /**
    * 探索種別によって時間指定の有無を設定
    */
    function changeSearchType() {
        if (getSearchType() == "departure" || getSearchType() == "arrival") {
            document.getElementById(baseId + ':timeHH').disabled = false;
            document.getElementById(baseId + ':timeMM').disabled = false;
        } else {
            document.getElementById(baseId + ':timeHH').disabled = true;
            document.getElementById(baseId + ':timeMM').disabled = true;
        }
    }

    /**
    * カレンダーで本日を設定
    */
    function today() {
        var now = new Date();
        setDate(now.getFullYear() + "/" + (now.getMonth() + 1) + "/" + now.getDate());
        document.getElementById(baseId + ':calendar').innerHTML = "";
        document.getElementById(baseId + ':calendar').style.display = "none";
    }

    /**
    * カレンダーの月を変更する
    */
    function changeMonth(type) {
        if (type == 'prev') {
            c_month--;
            if (c_month == 0) {
                c_year--;
                c_month = 12;
            }
        } else if (type == 'next') {
            c_month++;
            if (c_month == 13) {
                c_year++;
                c_month = 1;
            }
        }
        document.getElementById(baseId + ':c_table').innerHTML = getCalendarTable(c_year, c_month, c_date);
        document.getElementById(baseId + ':calendar').style.display = "block";

        setFunction();
    }

    /**
    * カレンダーボタンを押した時の動作
    */
    function changeCalendar() {
        if (document.getElementById(baseId + ':calendar').innerHTML == "") {
            openCalendar();
        } else {
            closeCalendar();
        }
    }

    /**
    * カレンダーを閉じる
    */
    function closeCalendar() {
        document.getElementById(baseId + ':calendar').innerHTML = "";
        document.getElementById(baseId + ':calendar').style.display = "none";
    }

    /**
    * カレンダーを表示する
    */
    function openCalendar() {
        var tmp_date = new Array();
        if (agent == 1) {
            tmp_date = document.getElementById(baseId + ':date:mm').value.split("/");
            tmp_date.push(document.getElementById(baseId + ':date:dd').value);
        } else if (agent == 2 || agent == 3) {
            tmp_date = document.getElementById(baseId + ':date').value.split("/");
        }
        var date;
        if (tmp_date.length != 3) {
            date = new Date();
        } else {
            try {
                date = new Date(parseInt(tmp_date[0], 10), parseInt(tmp_date[1], 10) - 1, parseInt(tmp_date[2], 10));
            } catch (e) {
                date = new Date();
            }
        }
        var buffer = '';
        // カレンダー本体
        buffer += '<div class="exp_calendar" id="' + baseId + ':c_table">';
        buffer += getCalendarTable(date.getFullYear(), (date.getMonth() + 1), date.getDate());
        buffer += '</div>';
        // デフォルトの日付設定
        c_year = date.getFullYear();
        c_month = (date.getMonth() + 1);
        c_date = date.getDate();
        // カレンダー出力
        document.getElementById(baseId + ':calendar').innerHTML = buffer;
        document.getElementById(baseId + ':calendar').style.display = "block";
        // カレンダーのボタンを設定
        setFunction();
    }

    /**
    * カレンダーの各イベントを設定
    */
    function setFunction() {
        // 本日ボタンのイベント設定
        //  addEvent(document.getElementById(baseId+":cal_today"), "click", function(){today();});
        // 前後の月のイベント設定
        addEvent(document.getElementById(baseId + ":header_prev"), "click", function () { changeMonth('prev'); });
        addEvent(document.getElementById(baseId + ":header_next"), "click", function () { changeMonth('next'); });

        // 日付選択のイベント設定
        // 前月
        for (var i = 23; i <= 31; i++) {
            addEvent(document.getElementById(baseId + ":prev:" + String(i)), "click", onEvent);
        }
        // 当月
        for (var i = 1; i <= 31; i++) {
            addEvent(document.getElementById(baseId + ":this:" + String(i)), "click", onEvent);
        }
        // 翌月
        for (var i = 1; i <= 14; i++) {
            addEvent(document.getElementById(baseId + ":next:" + String(i)), "click", onEvent);
        }
    }

    /**
    * イベントの振り分け
    */
    function onEvent(e) {
        var eventIdList = (e.srcElement) ? e.srcElement.id.split(":") : e.target.id.split(":");
        if (eventIdList.length == 3) {
            if (eventIdList[1] == "prev") {
                // 前月の指定
                selectDate(-parseInt(eventIdList[2]));
            } else if (eventIdList[1] == "this") {
                // 当月の指定
                selectDate(parseInt(eventIdList[2]));
            } else if (eventIdList[1] == "next") {
                // 翌月の指定
                selectDate(parseInt(eventIdList[2]) + 50);
            }
        }
    }

    /**
    * カレンダーで日付を選んだ時の動作
    */
    function selectDate(dd) {
        var tmp_year = c_year;
        var tmp_month = c_month;
        var tmp_day;
        // 振り分け
        if (dd < 0) {
            // 前の月
            tmp_month--;
            if (tmp_month < 1) {
                tmp_year--;
                tmp_month = 12;
            }
            tmp_day = Math.abs(dd);
        } else if (dd > 50) {
            // 翌月
            tmp_month++;
            if (tmp_month > 12) {
                tmp_year++;
                tmp_month = 1;
            }
            tmp_day = dd - 50;
        } else {
            tmp_day = dd;
        }
        setDate(tmp_year + "/" + tmp_month + "/" + tmp_day);
        document.getElementById(baseId + ':calendar').innerHTML = "";
        document.getElementById(baseId + ':calendar').style.display = "none";
        if (typeof(setPassRoute) == 'function'){
            setPassRoute();
        }
    }

    /**
    * 月の最終日を判定し、カレンダーに反映する
    */
    function getLastDate(yyyy, mm) {
        if (mm == 4 || mm == 6 || mm == 9 || mm == 11) {
            return 30;
        } else if (mm == 2) {
            if (yyyy % 4 == 0 && (yyyy % 100 != 0 || yyyy % 400 == 0)) {
                return 29;
            } else {
                return 28;
            }
        } else {
            return 31;
        }
    }

    /**
    * カレンダーをテーブルとして取得
    */
    function getCalendarTable(yyyy, mm, dd) {
        // 祝日チェック用の変数初期化
        furi = 0;
        ck = 0;
        // 現在日
        var date;
        var today = new Date();
        try {
            date = new Date(yyyy, mm - 1, dd);
        } catch (e) {
            date = new Date();
        }
        // 曜日の色設定
        var week = new Array('<span class="exp_header_sunday">日</span>', '<span class="exp_header_week">月</span>', '<span class="exp_header_week">火</span>', '<span class="exp_header_week">水</span>', '<span class="exp_header_week">木</span>', '<span class="exp_header_week">金</span>', '<span class="exp_header_saturday">土</span>');
        var weekLineClass = new Array("calFirstWeek", "calSecWeek", "calThirWeek", "calFourWeek", "calFifWeek", "calSixWeek");
        // カレンダー出力設定
        var doc = "";
        doc += '<table>';
        doc += '<tr>';
        doc += '<td colspan="7">';
        doc += '<div class="exp_cal_header">';
        doc += '<div class="exp_prev"><a class="exp_header_prev" href="javascript:void(0);" id="' + baseId + ':header_prev"></a></div>';
        doc += '<div class="exp_title"><span class="exp_header_month">' + date.getFullYear() + "年" + (date.getMonth() + 1) + "月" + '</span></div>';
        doc += '<div class="exp_next"><a class="exp_header_next" href="javascript:void(0);" id="' + baseId + ':header_next"></a></div>';
        doc += '</td>';
        doc += '</tr>';
        doc += '</div>';
        // 曜日設定
        doc += '<tr class="exp_calWeek">';
        for (var i = 0; i < week.length; i++) {
            doc += '<td>' + week[i] + '</td>';
        }
        doc += '</tr>';
        // 曜日の計算
        var dayOfWeek = 0;
        // 表示する段
        var viewRows = 0;
        // 開始日付まで進める
        var prevYear = date.getFullYear();
        var prevMonth = (date.getMonth() + 1) - 1;
        if (prevMonth == 0) {
            prevYear--;
            prevMonth = 12;
        }
        doc += '<tr class="exp_' + weekLineClass[viewRows] + '">';
        var firstDate = new Date(date.getFullYear(), date.getMonth(), 1);
        for (var i = -firstDate.getDay(); i < 0; i++) {
            doc += '<td class="exp_otherday">';
            doc += '<a href="javascript:void(0);" id="' + baseId + ':prev:' + String(getLastDate(prevYear, prevMonth) + i + 1) + '">' + (getLastDate(prevYear, prevMonth) + i + 1) + '</a>';
            doc += '</td>';
            dayOfWeek++;
        }
        // 日を設定
        for (var i = 1; i <= getLastDate(date.getFullYear(), date.getMonth() + 1); i++) {
            // 当日に色を付ける
            if (today.getFullYear() == date.getFullYear() && today.getMonth() == date.getMonth() && today.getDate() == i) {
                doc += '<td class="exp_today">';
            } else {
                doc += '<td class="exp_days">';
            }
            doc += '<a class="exp_' + getDateColor(yyyy, mm, i, dayOfWeek) + '" href="javascript:void(0);" id="' + baseId + ':this:' + String(i) + '">' + String(i) + '</a>';
            doc += '</td>';
            dayOfWeek++;
            if (dayOfWeek == 7 && i != getLastDate(date.getFullYear(), date.getMonth() + 1)) {
                dayOfWeek = 0;
                viewRows++;
                doc += '</tr>';
                doc += '<tr class="exp_' + weekLineClass[viewRows] + '">';
            }
        }
        // 残りの空白をセット
        var startWeek = dayOfWeek;
        var n = 1;
        var endDay = (6 - viewRows) * 7;
        for (var i = startWeek; i < endDay; i++) {
            if (dayOfWeek == 7) {
                dayOfWeek = 0;
                viewRows++;
                doc += '</tr>';
                doc += '<tr class="exp_' + weekLineClass[viewRows] + '">';
            }
            doc += '<td class="exp_otherday">';
            doc += '<a href="javascript:void(0);" id="' + baseId + ':next:' + String(n) + '">' + String(n) + '</a>';
            doc += '</td>';
            dayOfWeek++;
            n++;
        }
        doc += '</tr>';
        // 本日ボタン
        /**
        doc+='<tr>';
        doc+='<td colspan="7">';
        doc+='<a class="exp_cal_today" id="'+ baseId +':cal_today" href="javascript:void(0);"></a>';
        doc+='</td>';
        doc+='</tr>';
        */
        doc += '</table>';
        return doc;
    }

    /**
    * カレンダー内の文字の色を取得
    */
    function getDateColor(yyyy, mm, i, dayOfWeek) {
        if (getNationalHoliday(yyyy, mm, i, dayOfWeek) != '') {
            return "holiday";
        } else if (dayOfWeek == 0) {
            return "sunday";
        } else if (dayOfWeek == 6) {
            return "saturday";
        } else {
            return "weekday";
        }
    }

    /**
    * 祭日の取得
    */
    var furi = 0;
    var ck = 0;
    var Syunbunpar1 = new Array(19.8277, 20.8357, 20.8431, 21.8510);  // 春分・秋分の日付計算用1980-2099
    var Syunbunpar2 = new Array(22.2588, 23.2588, 23.2488, 24.2488);  // 春分・秋分の日付計算用1980-2099
    function getNationalHoliday(year, month, day, week) {
        // 変数の初期化
        syuku = '';
        // ハッピーマンデーと振替休日
        if (week == 1) {
            var moncnt = Math.floor(day / 7) + 1;
            // 振替休日
            // (2006年まで)「国民の祝日」が日曜日にあたるときは、その翌日を休日とする。
            if (furi == 1 && year <= 2006) {
                syuku = '振替休日';   // 振替フラグが立っていたら休み
                furi = 0;
            }
            // 第2月曜
            if (moncnt == 2) {
                if (month == 1) { syuku = '成人の日'; }    // 1月
                if (month == 10) { syuku = '体育の日'; }    // 10月
            }
            // 第3月曜
            if (moncnt == 3) {
                if (year >= 2003 && month == 7) { syuku = '海の日'; }   // 7月(2003～)
                if (year >= 2003 && month == 9) { syuku = '敬老の日'; } // 9月(2003～)
            }
        }

        // 春分の日・秋分の日
        var i, tyear;
        if ((year >= 1851) && (year <= 1899)) i = 0;
        else if ((year >= 1900) && (year <= 1979)) i = 1;
        else if ((year >= 1980) && (year <= 2099)) i = 2;
        else if ((year >= 2100) && (year <= 2150)) i = 3;
        else i = 4;   // 範囲外
        if (i < 4) {
            if (i < 2) tyear = 1983; else tyear = 1980;
            tyear = (year - tyear);
            if (month == 3) {      // 春分の日
                if (day == Math.floor(Syunbunpar1[i] + 0.242194 * tyear - Math.floor((tyear + 0.1) / 4))) syuku = '春分の日';
            } else if (month == 9) { // 秋分の日
                if (day == Math.floor(Syunbunpar2[i] + 0.242194 * tyear - Math.floor((tyear + 0.1) / 4))) syuku = '秋分の日';
            }
        }

        // その他の祝日
        if (month == 1 && day == 1) { syuku = '元日'; }            //  1月 1日
        if (month == 2 && day == 11) { syuku = '建国記念の日'; }    //  2月11日
        if (month == 4 && day == 29 && year <= 2006) { syuku = 'みどりの日'; }      //  4月29日(2006年まで)
        if (month == 4 && day == 29 && year >= 2007) { syuku = '昭和の日'; }        //  4月29日(2007年から)
        if (month == 5 && day == 3) { syuku = '憲法記念日'; }      //  5月 3日
        if (month == 5 && day == 4 && year >= 2007) { syuku = 'みどりの日'; }      //  5月 4日(2007年から)
        if (month == 5 && day == 5) { syuku = 'こどもの日'; }      //  5月 5日
        if (month == 11 && day == 3) { syuku = '文化の日'; }       // 11月 3日
        if (month == 11 && day == 23) { syuku = '勤労感謝の日'; }   // 11月23日
        if (month == 12 && day == 23) { syuku = '天皇誕生日'; }     // 12月23日
        if (year < 2003 && month == 7 && day == 20) { syuku = '海の日'; }   // 7月20日(～2002)
        if (year < 2003 && month == 9 && day == 15) { syuku = '敬老の日'; } //  9月15日(～2002)
        if (month == 8 && day == 11 && year >= 2016) { syuku = '山の日'; } //  8月11日(2016年から)

        // 振替休日
        // (2007年から)「国民の祝日」が日曜日に当たるときは、その日後においてその日に最も近い「国民の祝日」でない日を休日とする。
        if (furi == 1 && syuku == '' && year >= 2007) {
            syuku = '振替休日';   // 振替フラグが立っていたら休み
            furi = 0;
        } else if (furi == 1 && syuku != '' && year >= 2007) {
            furi = 1;             // 振替フラグが立っていて祝日の場合は振替フラグを立てる
        } else if (week == 0 && syuku != '') {
            furi = 1;             // 日曜で祝日の場合は振替フラグを立てる
        } else {
            furi = 0;
        }

        // 国民の休日(祝日に挟まれた平日)
        // (2006年まで)その前日及び翌日が「国民の祝日」である日（日曜日にあたる日及び前項に規定する休日にあたる日を除く。）は、休日とする。
        // (2007年から)その前日及び翌日が「国民の祝日」である日（「国民の祝日」でない日に限る。）は、休日とする。
        if ((week > 0 && syuku == '' && !ck && year <= 2006) || (syuku == '' && !ck && syuku != '振替休日' && year >= 2007)) {
            ck = 1;  // 再帰呼び出しでここを通らないようにする
            // 前日と次日が祝日か確認
            // １日と末日が祝日の場合はないので日にちは単純に１を増減する
            // 曜日の設定
            bweek = week - 1; if (bweek < 0) bweek = 6;
            aweek = week + 1; if (bweek > 6) bweek = 0;
            if (getNationalHoliday(year, month, day - 1, bweek) && getNationalHoliday(year, month, day + 1, aweek)) {
                syuku = '国民の休日';
            }
            ck = 0;  // フラグの初期化
        }

        return syuku;
    }

    /**
    * 探索種別を外部から制御
    */
    function setSearchType(str) {
        if (agent == 1) {
            for (var i = 0; i < 5; i++) {
                document.getElementById(baseId + ':searchType:' + String(i + 1) + ':active').style.display = 'none';
                document.getElementById(baseId + ':searchType:' + String(i + 1) + ':none').style.display = 'block';
            }
            document.getElementById(baseId + ':searchType').value = str;
            if (str == "departure") {
                document.getElementById(baseId + ':searchType:' + String(1) + ':active').style.display = 'block';
                document.getElementById(baseId + ':searchType:' + String(1) + ':none').style.display = 'none';
            } else if (str == "arrival") {
                document.getElementById(baseId + ':searchType:' + String(2) + ':active').style.display = 'block';
                document.getElementById(baseId + ':searchType:' + String(2) + ':none').style.display = 'none';
            } else if (str == "firstTrain") {
                document.getElementById(baseId + ':searchType:' + String(3) + ':active').style.display = 'block';
                document.getElementById(baseId + ':searchType:' + String(3) + ':none').style.display = 'none';
            } else if (str == "lastTrain") {
                document.getElementById(baseId + ':searchType:' + String(4) + ':active').style.display = 'block';
                document.getElementById(baseId + ':searchType:' + String(4) + ':none').style.display = 'none';
            } else if (str == "plain") {
                document.getElementById(baseId + ':searchType:' + String(5) + ':active').style.display = 'block';
                document.getElementById(baseId + ':searchType:' + String(5) + ':none').style.display = 'none';
            }
        } else if (agent == 2 || agent == 3) {
            if (str == "departure") {
                document.getElementsByName(baseId + ':searchType')[0].checked = true;
            } else if (str == "arrival") {
                document.getElementsByName(baseId + ':searchType')[1].checked = true;
            } else if (str == "firstTrain") {
                document.getElementsByName(baseId + ':searchType')[2].checked = true;
            } else if (str == "lastTrain") {
                document.getElementsByName(baseId + ':searchType')[3].checked = true;
            } else if (str == "plain") {
                document.getElementsByName(baseId + ':searchType')[4].checked = true;
            }
        }
        changeSearchType();
    }

    /**
    * 日付を外部から設定
    */
    function setDate(date) {
        var tmpDate = String(date);
        var yyyy, mm, dd;
        if (tmpDate.length == 8 && !isNaN(tmpDate)) {
            yyyy = tmpDate.substr(0, 4).replace(new RegExp('^0+'), '');
            mm = tmpDate.substr(4, 2).replace(new RegExp('^0+'), '');
            dd = tmpDate.substr(6, 2).replace(new RegExp('^0+'), '');
        } else if (tmpDate.split("/").length == 3) {
            yyyy = tmpDate.split("/")[0].replace(new RegExp('^0+'), '');
            mm = tmpDate.split("/")[1].replace(new RegExp('^0+'), '');
            dd = tmpDate.split("/")[2].replace(new RegExp('^0+'), '');
            if (isNaN(yyyy) || isNaN(mm) || isNaN(dd)) {
                // 日付は数値で指定してください。
                return false;
            }
        } else {
            // 日付はyyyy/mm/dd形式で指定してください。
            return false;
        }
        if (yyyy < 1900 || yyyy > 2099) {
            // 年の指定が間違っています。\n年は西暦で指定してください。
            return false;
        } else if (mm < 1 || mm > 12) {
            // 月の指定が間違っています。\n1月～12月の間で指定してください。
            return false;
        } else {
            if (dd < 1) {
                // 日の指定が間違っています。
                return false;
            }
            /**
            if(mm == 4 || mm == 6 || mm == 9 || mm == 11){
            if(dd<1 || dd>30){
            // 日の指定が間違っています。\n"+mm+"月は1日～30日の間で指定してください。
            return false;
            }
            }else if(mm == 2){
            if(yyyy%4 == 0 && (yyyy%100 != 0 || yyyy%400 == 0)){
            if(dd<1 || dd>29){
            // 日の指定が間違っています。\n"+yyyy+"年"+mm+"月は1日～29日の間で指定してください。
            return false;
            }
            }else{
            if(dd<1 || dd>28){
            // 日の指定が間違っています。\n"+yyyy+"年"+mm+"月は1日～28日の間で指定してください。
            return false;
            }
            }
            }else{
            if(dd<1 || dd>31){
            // 日の指定が間違っています。\n"+mm+"月は1日～31日の間で指定してください。
            return false;
            }
            }
            */
        }
        // リストから選択
        if (agent == 1) {
            // 年月+日の場合
            if (document.getElementById(baseId + ':date:mm').options.length == 0) {
                // まずは年月を設定
                var now = new Date();
                for (var i = now.getFullYear() - 1; i <= now.getFullYear() + 1; i++) {
                    for (var j = 1; j <= 12; j++) {
                        var tmp_option = document.createElement('option');
                        tmp_option.text = String(i) + '年' + String(j) + '月';
                        tmp_option.value = String(i) + '/' + String(j);
                        document.getElementById(baseId + ':date:mm').add(tmp_option);
                    }
                }
            }
            var refrech = false;
            var check = false;
            for (var i = 0; i < document.getElementById(baseId + ':date:mm').options.length; i++) {
                if (document.getElementById(baseId + ':date:mm').options.item(i).value == String(yyyy) + "/" + String(mm)) {
                    check = true;
                    if (document.getElementById(baseId + ':date:mm').selectedIndex != i) {
                        document.getElementById(baseId + ':date:mm').selectedIndex = i;
                        refrech = true;
                    }
                }
            }
            if (!check) {
                // 対象外の日付
                return false;
            }
            // カレンダーを再設定
            if (refrech) {
                setDateList(yyyy, mm);
            }
            for (var i = 0; i < document.getElementById(baseId + ':date:dd').options.length; i++) {
                if (document.getElementById(baseId + ':date:dd').options.item(i).value == String(dd)) {
                    document.getElementById(baseId + ':date:dd').selectedIndex = i;
                    return true;
                }
            }
            // 存在しない日付の場合は、最終日を設定
            document.getElementById(baseId + ':date:dd').selectedIndex = document.getElementById(baseId + ':date:dd').options.length - 1;
        } else if (agent == 2 || agent == 3) {
            // 月日の場合
            for (var i = 0; i < document.getElementById(baseId + ':date').options.length; i++) {
                if (document.getElementById(baseId + ':date').options.item(i).value == String(yyyy) + "/" + String(mm) + "/" + String(dd)) {
                    document.getElementById(baseId + ':date').selectedIndex = i;
                    return true;
                }
            }
            // リストがないので、カレンダーを再設定
            while (document.getElementById(baseId + ':date').lastChild) {
                document.getElementById(baseId + ':date').removeChild(document.getElementById(baseId + ':date').lastChild);
            }
            // リストを設定
            var calender_limit = 1;
            var week = new Array("日", "月", "火", "水", "木", "金", "土");
            var tmp_year = yyyy;
            var tmp_month = mm - calender_limit;
            if (tmp_month < 1) { tmp_year--; tmp_month += 12; }
            for (var i = 0; i < (calender_limit * 2) + 1; i++) {
                for (var j = 1; j <= getLastDate(tmp_year, tmp_month); j++) {
                    var tmp_option = document.createElement('option');
                    tmp_option.text = String(tmp_month) + '月' + String(j) + '日(' + week[new Date(tmp_year, tmp_month - 1, j).getDay()] + ')';
                    tmp_option.value = String(tmp_year) + '/' + String(tmp_month) + '/' + String(j);
                    document.getElementById(baseId + ':date').appendChild(tmp_option);
                }
                tmp_month++;
                if (tmp_month > 12) { tmp_year++; tmp_month = 1; }
            }
            for (var i = 0; i < document.getElementById(baseId + ':date').options.length; i++) {
                if (document.getElementById(baseId + ':date').options.item(i).value == String(yyyy) + "/" + String(mm) + "/" + String(dd)) {
                    document.getElementById(baseId + ':date').selectedIndex = i;
                    return true;
                }
            }
        }
    }

    /**
    * 日付のリストを修正
    */
    function setDateList(yyyy, mm) {
        while (document.getElementById(baseId + ':date:dd').lastChild) {
            document.getElementById(baseId + ':date:dd').removeChild(document.getElementById(baseId + ':date:dd').lastChild);
        }
        // リストを設定
        var calender_limit = 1;
        var week = new Array("日", "月", "火", "水", "木", "金", "土");
        for (var j = 1; j <= getLastDate(yyyy, mm); j++) {
            var tmp_option = document.createElement('option');
            tmp_option.text = String(j) + '日(' + week[new Date(yyyy, mm - 1, j).getDay()] + ')';
            tmp_option.value = String(j);
            document.getElementById(baseId + ':date:dd').add(tmp_option);
        }
    }

    /**
    * 時間を外部から設定
    */
    function setTime(time) {
        var tmpTime = String(time);
        if (tmpTime.length == 3 && tmpTime.indexOf(":") == -1) {
            document.getElementById(baseId + ':timeHH').selectedIndex = parseInt(tmpTime.substr(0, 1), 10);
            document.getElementById(baseId + ':timeMM').selectedIndex = parseInt(tmpTime.substr(1, 2), 10);
        } else if (tmpTime.length == 4 && tmpTime.indexOf(":") == -1) {
            document.getElementById(baseId + ':timeHH').selectedIndex = parseInt(tmpTime.substr(0, 2), 10);
            document.getElementById(baseId + ':timeMM').selectedIndex = parseInt(tmpTime.substr(2, 2), 10);
        } else if (tmpTime.indexOf(":") != -1) {
            var timeList = tmpTime.split(":");
            if (timeList.length == 2) {
                document.getElementById(baseId + ':timeHH').selectedIndex = parseInt(timeList[0], 10);
                document.getElementById(baseId + ':timeMM').selectedIndex = parseInt(timeList[1], 10);
            }
        }
    }

    /**
     * 平日に補正
     */
    function setWeekday() {
        var tmp_Date = getDate();
        var check_date = new Date(parseInt(tmp_Date.substr(0, 4), 10), parseInt(tmp_Date.substr(4, 2), 10) - 1, parseInt(tmp_Date.substr(6, 4), 10));
        for (var i = 0; i < 31; i++) {
            if (check_date.getDay() > 0 && check_date.getDay() < 6) {
                // 祝日判定
                if (getNationalHoliday(check_date.getFullYear(), check_date.getMonth() + 1, check_date.getDate(), check_date.getDay()) == '') {
                    break;
                }
            }
            check_date.setDate(check_date.getDate() + 1);
        }
        setDate(check_date.getFullYear() + "/" + (check_date.getMonth() + 1) + "/" + check_date.getDate());
    }

    /**
    * 環境設定
    */
    function setConfigure(name, value) {
        if (name.toLowerCase() == String("apiURL").toLowerCase()) {
            apiURL = value;
        } else if (name.toLowerCase() == String("key").toLowerCase()) {
            key = value;
        } else if (name.toLowerCase() == String("agent").toLowerCase()) {
            agent = value;
        } else if (String(name).toLowerCase() == String("ssl").toLowerCase()) {
            if (String(value).toLowerCase() == "true" || String(value).toLowerCase() == "enable" || String(value).toLowerCase() == "enabled") {
                apiURL = apiURL.replace('http://', 'https://');
            } else {
                apiURL = apiURL.replace('https://', 'http://');
            }
        }
    }

    // 外部参照可能な関数リスト
    this.dispDateTime = dispDateTime;
    this.checkDate = checkDate;
    this.getDate = getDate;
    this.getTime = getTime;
    this.getSearchType = getSearchType;
    this.setSearchType = setSearchType;
    this.setDate = setDate;
    this.setTime = setTime;
    this.openCalendar = openCalendar;
    this.closeCalendar = closeCalendar;
    this.setConfigure = setConfigure;
    this.setWeekday = setWeekday;

    // 定数リスト
    this.SEARCHTYPE_DEPARTURE = "departure";
    this.SEARCHTYPE_ARRIVAL = "arrival";
    this.SEARCHTYPE_FIRSTTRAIN = "firstTrain";
    this.SEARCHTYPE_LASTTRAIN = "lastTrain";
    this.SEARCHTYPE_PLAIN = "plain";
    this.SEARCHTYPE_DIA = "dia";

    // 端末制御
    this.AGENT_PC = 1;
    this.AGENT_PHONE = 2;
    this.AGENT_TABLET = 3;
};


/**
 *  駅すぱあと Web サービス
 *  駅名入力パーツ
 *  サンプルコード
 *  https://github.com/EkispertWebService/GUI
 *
 *  Version:2016-08-03
 *
 *  Copyright (C) Val Laboratory Corporation. All rights reserved.
 **/

var expGuiStation = function (pObject, config) {
    // ドキュメントのオブジェクトを格納
    var documentObject = pObject;
    var baseId = pObject.id;

    // Webサービスの設定
    var apiURL = ekicom_url;
    var key = ekicom_key;

    // AGENTのチェック
    var agent = 1;
    var isiPad = navigator.userAgent.match(/iPad/i) != null;
    var isiPhone = navigator.userAgent.match(/iPhone/i) != null;
    var isAndroid_phone = (navigator.userAgent.match(/Android/i) != null && navigator.userAgent.match(/Mobile/i) != null);
    var isAndroid_tablet = (navigator.userAgent.match(/Android/i) != null && navigator.userAgent.match(/Mobile/i) == null);
    if (isiPhone || isAndroid_phone) { agent = 2; }
    if (isiPad || isAndroid_tablet) { agent = 3; }

    //古い端末向けのフラグ
    if (/Android\s2\.[0|1|2|3]/.test(navigator.userAgent)) {
        agent = 3;
    }

    /**
    * イベントの設定(IE対応版)
    */
    function addEvent(element, eventName, func) {
        if (element) {
            if (typeof eventName == 'string' && typeof func == 'function') {
                if (element.addEventListener) {
                    element.addEventListener(eventName, func, false);
                } else if (element.attachEvent) {
                    element.attachEvent("on" + eventName, func);
                }
            }
        }
    }

    // 変数郡
    var stationList = new Array(); // インクリメンタルサーチ結果
    var httpObj; // インクリメンタルサーチのリクエストオブジェクト
    var oldvalue = ""; // キー監視用の文字列
    var stationCorporationBind;
    var stationType;
    var stationPrefectureCode;
    var callBackFunction = new Object();
    var maxStation = 30; //最大駅数
    var selectStation = 0;
    var callBackFunctionDelay = false;

    var stationSort = new Array(createSortObject("駅", "train"), createSortObject("空港", "plane"), createSortObject("船", "ship"), createSortObject("バス", "bus"));
    function createSortObject(name, type, sList) {
        var tmpObj = new Object();
        tmpObj.name = name;
        tmpObj.type = type;
        tmpObj.visible = true;
        tmpObj.stationList = new Array();
        return tmpObj;
    }

    /**
    * 駅名入力の設置
    */
    function dispStation() {
        // 駅名入力
        var buffer;
        if (agent == 1) {
            buffer = '<div class="expGuiStation expGuiStationPc">';
        } else if (agent == 2) {
            buffer = '<div class="expGuiStation expGuiStationPhone">';
        } else if (agent == 3) {
            buffer = '<div class="expGuiStation expGuiStationTablet">';
        }
        if (agent == 1 || agent == 3) {
            buffer += '<div><input class="exp_station" type="text" id="' + baseId + ':stationInput" autocomplete="off"></div>';
            buffer += '<div class="exp_stationList" id="' + baseId + ':stationList" style="display:none;">';
            if (agent == 3) {
                buffer += '<div class="exp_stationTabList exp_clearfix">';
                buffer += '<span class="exp_stationTab"><input type="checkbox" class="exp_stationTabCheck" id="' + baseId + ':stationView:' + String(1) + '" value="1"><label class="exp_stationTabTextLeft" for="' + baseId + ':stationView:' + String(1) + '">駅</label></span>';
                buffer += '<span class="exp_stationTab"><input type="checkbox" class="exp_stationTabCheck" id="' + baseId + ':stationView:' + String(2) + '" value="1"><label class="exp_stationTabText" for="' + baseId + ':stationView:' + String(2) + '">空港</label></span>';
                buffer += '<span class="exp_stationTab"><input type="checkbox" class="exp_stationTabCheck" id="' + baseId + ':stationView:' + String(3) + '" value="1"><label class="exp_stationTabText" for="' + baseId + ':stationView:' + String(3) + '">船</label></span>';
                buffer += '<span class="exp_stationTab"><input type="checkbox" class="exp_stationTabCheck" id="' + baseId + ':stationView:' + String(4) + '" value="1"><label class="exp_stationTabTextRight" for="' + baseId + ':stationView:' + String(4) + '">バス</label></span>';
                buffer += '</div>';
            }
            buffer += '<div class="exp_stationSelect" id="' + baseId + ':stationSelect"></div>';
            buffer += '</div>';
        } else if (agent == 2) {
            buffer += '<input class="exp_station" type="text" id="' + baseId + ':stationOutput">';
            buffer += '<div class="exp_stationPopupBack" id="' + baseId + ':stationPopupBack"style="display:none;">';
            buffer += '<div class="exp_stationPopup" id="' + baseId + ':stationPopup" style="display:none;">';
            buffer += '<div class="exp_stationInputHeader exp_clearfix">';
            buffer += '<div class="exp_stationInputBack"><a id="' + baseId + ':stationBack" href="Javascript:void(0);"></a></div>';
            buffer += '<div class="exp_stationInputText"><input type="text" id="' + baseId + ':stationInput" autocomplete="off"></div>';
            buffer += '</div>';
            buffer += '<div class="exp_stationTabList exp_clearfix">';
            buffer += '<span class="exp_stationTab"><input type="checkbox" class="exp_stationTabCheck" id="' + baseId + ':stationView:' + String(1) + '" value="1"><label class="exp_stationTabText" for="' + baseId + ':stationView:' + String(1) + '">駅</label></span>';
            buffer += '<span class="exp_stationTab"><input type="checkbox" class="exp_stationTabCheck" id="' + baseId + ':stationView:' + String(2) + '" value="1"><label class="exp_stationTabText" for="' + baseId + ':stationView:' + String(2) + '">空港</label></span>';
            buffer += '<span class="exp_stationTab"><input type="checkbox" class="exp_stationTabCheck" id="' + baseId + ':stationView:' + String(3) + '" value="1"><label class="exp_stationTabText" for="' + baseId + ':stationView:' + String(3) + '">船</label></span>';
            buffer += '<span class="exp_stationTab"><input type="checkbox" class="exp_stationTabCheck" id="' + baseId + ':stationView:' + String(4) + '" value="1"><label class="exp_stationTabText" for="' + baseId + ':stationView:' + String(4) + '">バス</label></span>';
            buffer += '</div>';
            buffer += '<div class="exp_stationSPListBase" id="' + baseId + ':stationList" style="display:none;">';
            buffer += '<div class="exp_stationSPList exp_clearfix" id="' + baseId + ':stationSelect"></div>';
            buffer += '</div>';
            buffer += '</div>';
            buffer += '</div>';
        }
        buffer += '</div>';
        // HTMLへ出力
        documentObject.innerHTML = buffer;
        // イベントの設定
        addEvent(document.getElementById(baseId + ":stationInput"), "keyup", inputStation);
        addEvent(document.getElementById(baseId + ":stationInput"), "keydown", selectStationChange);
        if (agent == 1 || agent == 3) {
            addEvent(document.getElementById(baseId + ":stationInput"), "blur", onblurEvent);
            addEvent(document.getElementById(baseId + ":stationInput"), "focus", onFocusEvent);
        } else if (agent == 2) {
            addEvent(document.getElementById(baseId + ":stationOutput"), "keyup", openStationInput);
            addEvent(document.getElementById(baseId + ":stationOutput"), "click", openStationInput);
            addEvent(document.getElementById(baseId + ":stationBack"), "click", closeStationInput);
        }
        // 種別のチェックタブ
        if (agent == 2 || agent == 3) {
            document.getElementById(baseId + ':stationView:1').checked = true;
            document.getElementById(baseId + ':stationView:2').checked = true;
            document.getElementById(baseId + ':stationView:3').checked = true;
            document.getElementById(baseId + ':stationView:4').checked = true;
        }

        // キーの監視
        if (agent == 1 || agent == 3) {
            inputCheck();
        }
    }

    /**
    * スマートフォン用入力画面を開く
    */
    function openStationInput() {
        document.getElementById(baseId + ':stationPopupBack').style.display = "block";
        document.getElementById(baseId + ':stationPopup').style.display = "block";
        document.getElementById(baseId + ':stationInput').value = document.getElementById(baseId + ':stationOutput').value;
        document.getElementById(baseId + ':stationInput').focus();
        document.getElementById(baseId + ':stationPopup').style.top = 0;
        document.getElementById(baseId + ':stationPopup').style.left = 0;
        //キー監視
        inputCheck();
    }

    /**
    * スマートフォン用入力画面を閉じる
    */
    function closeStationInput() {
        if (document.getElementById(baseId + ':stationOutput').value != "" && document.getElementById(baseId + ':stationInput').value == "") {
            // 空にする
            document.getElementById(baseId + ':stationOutput').value = "";
            if (typeof callBackFunction['change'] == 'function') {
                callBackFunction['change']();
            }
        } else {
            for (var i = 0; i < stationList.length; i++) {
                if (stationList[i].name == document.getElementById(baseId + ':stationInput').value) {
                    if (document.getElementById(baseId + ':stationOutput').value != stationList[i].name) {
                        // 変わっていたら変更
                        document.getElementById(baseId + ':stationOutput').value = stationList[i].name;
                        if (typeof callBackFunction['change'] == 'function') {
                            callBackFunction['change']();
                        }
                    }
                }
            }
        }
        document.getElementById(baseId + ':stationPopupBack').style.display = "none";
        document.getElementById(baseId + ':stationPopup').style.display = "none";
        document.getElementById(baseId + ':stationOutput').focus();
    }

    /**
    * フォーカスが外れた時にイベント
    */
    function onblurEvent() {
        callBackFunctionDelay = true;
        setTimeout(onblurEventCallBack, 1000);
    }

    /**
    * 遅延処理を行った際に実行される
    */
    function onblurEventCallBack() {
        if (callBackFunctionDelay) {
            callBackFunctionDelay = false;
            if (typeof callBackFunction['blur'] == 'function') {
                callBackFunction['blur']();
            }
        }
    }

    /**
    * フォーカスが合った時にイベント
    */
    function onFocusEvent() {
        callBackFunctionDelay = false;
        if (typeof callBackFunction['focus'] == 'function') {
            callBackFunction['focus']();
        }
        if (agent == 1 || agent == 3) {
            if (document.getElementById(baseId + ':stationInput').value != "") {
                if (document.getElementById(baseId + ':stationList').style.display == "none") {
                    document.getElementById(baseId + ':stationList').style.display = "block";
                    // コールバック
                    if (typeof callBackFunction['open'] == 'function') {
                        callBackFunction['open']();
                    }
                }
            }
        }
    }

    /**
    * 文字の入力中でもチェックする
    */
    var inputCheck = function () {
        if (document.getElementById(baseId + ':stationInput')) {
            if (oldvalue != document.getElementById(baseId + ':stationInput').value) {
                oldvalue = document.getElementById(baseId + ':stationInput').value;
                searchStation(true, oldvalue);
            };
            setTimeout(inputCheck, 100);
        }
    };

    /**
    * フォームのイベント処理
    */
    function inputStation(event) {
        var iStation = document.getElementById(baseId + ':stationInput').value;
        if (iStation == "") {
            document.getElementById(baseId + ':stationList').style.display = "none";
        }
        if (event.keyCode == 13) {
            // エンターキー
            if (selectStation > 0) {
                // カーソルで移動済み
                setStationNo(selectStation);
            } else {
                // エンターキーのみ
                /*
                if (iStation != "") {
                    var tmp_stationList = new Array();
                    for (var n = 0; n < stationSort.length; n++) {
                        if (stationSort[n].visible) {
                            for (var i = 0; i < stationSort[n].stationList.length; i++) {
                                tmp_stationList.push(stationSort[n].stationList[i] + 1);
                            }
                        }
                    }
                    setStationNo(tmp_stationList[0]);
                }
                */
            }
            // エンターキー
            if (typeof callBackFunction['enter'] == 'function') {
                callBackFunction['enter']();
            }
        }
    }

    /**
    * カーソルによる駅指定
    */
    function selectStationChange(event) {
        if (event.keyCode == 38 || event.keyCode == 40) {
            var tmp_stationList = new Array();
            for (var n = 0; n < stationSort.length; n++) {
                if (stationSort[n].visible) {
                    for (var i = 0; i < stationSort[n].stationList.length; i++) {
                        tmp_stationList.push(stationSort[n].stationList[i] + 1);
                    }
                }
            }
            //マークを消す
            if (document.getElementById(baseId + ":stationRow:" + String(selectStation))) {
                document.getElementById(baseId + ":stationRow:" + String(selectStation)).className = "exp_stationName";
            }
            if (tmp_stationList.length == 0) {
                selectStation = 0;
            } else {
                var pos = checkArray(tmp_stationList, selectStation);
                if (pos == -1) {
                    selectStation = tmp_stationList[0];
                } else if (event.keyCode == 38) {
                    if (pos > 0) {
                        selectStation = tmp_stationList[pos - 1];
                    }
                } else if (event.keyCode == 40) {
                    if (pos < tmp_stationList.length - 1) {
                        selectStation = tmp_stationList[pos + 1];
                    }
                }
            }
            if (selectStation > 0) {
                if (document.getElementById(baseId + ":stationRow:" + String(selectStation))) {
                    document.getElementById(baseId + ":stationRow:" + String(selectStation)).className = "exp_stationName exp_stationNameCursor";
                }
                if (document.getElementById(baseId + ':stationList').style.display == "none") {
                    document.getElementById(baseId + ':stationList').style.display = "block";
                    // コールバック
                    if (typeof callBackFunction['open'] == 'function') {
                        callBackFunction['open']();
                    }
                }
            }
        }
    }

    /**
    * 駅名の検索
    */
    function searchStation(openFlag, str) {
        resetCursor();
        if (typeof httpObj != 'undefined') {
            httpObj.abort();
        }
        if (str.length == "") {
            closeStationList();
            return;
        }
        var url = apiURL + "v1/json/station/light?key=" + key + "&name=" + encodeURIComponent(str);
        if (typeof stationType != 'undefined') {
            url += "&type=" + stationType;
        } else {
            var tmp_type = new Array();
            for (var n = 0; n < stationSort.length; n++) {
                if (stationSort[n].visible) {
                    tmp_type.push(stationSort[n].type);
                }
            }
            // すべてオフの場合は問い合わせない
            if (tmp_type.length == 0) {
                return;
            }
            url += "&type=" + tmp_type.join(":");
        }
        if (typeof stationPrefectureCode != 'undefined') {
            url += "&prefectureCode=" + stationPrefectureCode;
        }
        if (typeof stationCorporationBind != 'undefined') {
            url += "&corporationBind=" + encodeURIComponent(stationCorporationBind);
        }
    	var JSON_object = {};
        if (window.XDomainRequest) {
            // IE用
            httpObj = new XDomainRequest();
            httpObj.onload = function () {
                JSON_object = JSON.parse(httpObj.responseText);
                outStationList(openFlag, JSON_object);
            };
        } else {
            httpObj = new XMLHttpRequest();
            httpObj.onreadystatechange = function () {
                var done = 4, ok = 200, fbd = 403;
                if (httpObj.readyState == done && httpObj.status == ok) {
                    JSON_object = JSON.parse(httpObj.responseText);
                    outStationList(openFlag, JSON_object);
                } else if (httpObj.readyState == done && httpObj.status == fbd) {
                	alert("WEB APIが403エラーを返却しました。管理者に連絡してください。",1);
                }
            };
        }

        var newurl = "ekispert_apicall?callURL=" + encodeURIComponent(url);
        httpObj.open("GET", newurl, true);
        httpObj.overrideMimeType('text/plain; charset=utf-8');
        httpObj.send(null);
    }

    /**
    * 駅名をセットする
    */
    function setStationNo(n) {
        resetCursor();
        if (typeof stationList[n - 1] != 'undefined') {
            if (agent == 1 || agent == 3) {
                if (stationList[(n - 1)].name != document.getElementById(baseId + ':stationInput').value) {
                    setStation(stationList[(n - 1)].name);
                    if (typeof callBackFunction['change'] == 'function') {
                        callBackFunction['change']();
                    }
                } else {
                    setStation(stationList[(n - 1)].name);
                }
            } else if (agent == 2) {
                if (stationList[(n - 1)].name != document.getElementById(baseId + ':stationOutput').value) {
                    setStation(stationList[(n - 1)].name);
                    if (typeof callBackFunction['change'] == 'function') {
                        callBackFunction['change']();
                    }
                } else {
                    setStation(stationList[(n - 1)].name);
                }
            }
        }
    }

    /**
    * 駅のアイコンを設定
    */
    function getStationIconType(type) {
        if (typeof type != 'object') {
            // 単一の場合
            return '<span class="exp_' + type + '"></span>';
        } else if (typeof type.text != 'undefined') {
            return '<span class="exp_' + type.text + '"></span>';
        } else if (type.length > 0) {
            // 複数の場合
            var buffer = "";
            for (var i = 0; i < type.length; i++) {
                if (typeof type[i].text != 'undefined') {
                    buffer += '<span class="exp_' + type[i].text + '"></span>';
                } else {
                    buffer += '<span class="exp_' + type[i] + '"></span>';
                }
            }
            return buffer;
        }
        return '';
    }

    /**
    * 検索した駅リストの出力
    */
    function outStationList(openFlag, tmp_stationList) {
        if (typeof tmp_stationList != 'undefined') {
            if (typeof tmp_stationList.ResultSet.Point != 'undefined') {
                stationList = new Array();
                if (typeof tmp_stationList.ResultSet.Point.length != 'undefined') {
                    // 複数
                    for (var i = 0; i < tmp_stationList.ResultSet.Point.length; i++) {
                        stationList.push(setStationObject(tmp_stationList.ResultSet.Point[i]));
                    }
                } else {
                    // 一つだけ
                    stationList.push(setStationObject(tmp_stationList.ResultSet.Point));
                }
            }
        }
        // 駅名を出力
        if (stationList.length > 0) {
            // リストを出力
            var viewStationType = (typeof stationType != 'undefined') ? stationType : "";
            var buffer = "";
            buffer += '<ul class="exp_stationTable">';
            for (var n = 0; n < stationSort.length; n++) {
                stationSort[n].stationList = new Array();
                for (var i = 0; i < stationList.length; i++) {
                    if (stationList[i].type.split(":")[0] == stationSort[n].type) {
                        stationSort[n].stationList.push(i);
                    }
                }
                if (agent == 1) {
                    if (viewStationType.indexOf(stationSort[n].type) != -1 || viewStationType == "") {
                        buffer += '<li>';
                        if (stationSort[n].visible) {
                            buffer += '<a class="exp_stationTitle" id="' + baseId + ':stationView:' + String(n + 1) + '" href="Javascript:void(0);">';
                        } else {
                            buffer += '<a class="exp_stationTitleClose" id="' + baseId + ':stationView:' + String(n + 1) + '" href="Javascript:void(0);">';
                        }
                        buffer += '<div class="exp_stationCount">' + stationSort[n].stationList.length + '件</div>';
                        buffer += '<div class="exp_stationIcon">';
                        buffer += '<span class="exp_' + stationSort[n].type + '" id="' + baseId + ':stationView:' + String(n + 1) + ':icon"></span>';
                        buffer += '</div>';
                        buffer += '<div class="exp_stationType" id="' + baseId + ':stationView:' + String(n + 1) + ':type">';
                        buffer += stationSort[n].name;
                        buffer += '</div>';
                        buffer += '</a>';
                        buffer += '</li>';
                    }
                }
                if (stationSort[n].visible) {
                    // リストの出力
                    for (var i = 0; i < stationList.length; i++) {
                        if (stationList[i].type.split(":")[0] == stationSort[n].type) {
                            buffer += getStationListItem(i + 1, stationList[i]);
                        }
                    }
                }
            }
            buffer += '</ul>';
            document.getElementById(baseId + ':stationSelect').innerHTML = buffer;
            // イベントを設定
            for (var i = 0; i < stationList.length; i++) {
                addEvent(document.getElementById(baseId + ":stationRow:" + String(i + 1)), "click", onEvent);
            }
            if (viewStationType.split(":").length >= 2 || viewStationType == "") {
                for (var i = 0; i < stationSort.length; i++) {
                    addEvent(document.getElementById(baseId + ":stationView:" + String(i + 1)), "click", onEvent);
                }
            }
            if (document.getElementById(baseId + ':stationList').style.display == "none" && openFlag) {
                document.getElementById(baseId + ':stationList').style.display = "block";
                // コールバック
                if (typeof callBackFunction['open'] == 'function') {
                    callBackFunction['open']();
                }
            }
            // リストが取得できたためコールバックする
            if (typeof callBackFunction['callback'] == 'function') {
                callBackFunction['callback'](true);
                callBackFunction['callback'] = undefined;
            }
        } else {
            if (typeof callBackFunction['callback'] == 'function') {
                callBackFunction['callback'](false);
                callBackFunction['callback'] = undefined;
            }
        }
    }

    /**
    * 表示切替
    */
    function stationView(n) {
        stationSort[n].visible = !stationSort[n].visible;
        outStationList(true);
    }

    /**
    * 地点オブジェクトの作成
    */
    function setStationObject(stationObj) {
        var tmp_station = new Object();
        tmp_station.name = stationObj.Station.Name;
        tmp_station.code = stationObj.Station.code;
        tmp_station.yomi = stationObj.Station.Yomi;

        if (typeof stationObj.Station.Type == 'string') {
            // 1つのタイプだけある
            tmp_station.type = stationObj.Station.Type;
        } else {
            if (typeof stationObj.Station.Type.length == 'undefined') {
                // 単一のタイプ
                if (typeof stationObj.Station.Type.text != 'undefined') {
                    tmp_station.type = stationObj.Station.Type.text;
                    if (typeof stationObj.Station.Type.detail != 'undefined') {
                        tmp_station.type_detail = stationObj.Station.Type.text + "." + stationObj.Station.Type.detail;
                    }
                } else {
                    tmp_station.type = stationObj.Station.Type;
                }
            } else {
                // 駅のタイプが複数
                var temp_type = "";
                var temp_type_detail = "";
                for (var i = 0; i < stationObj.Station.Type.length; i++) {
                    if (typeof stationObj.Station.Type[i].text != 'undefined') {
                        if (temp_type != "") { temp_type += ":"; }
                        temp_type += stationObj.Station.Type[i].text;
                        if (typeof stationObj.Station.Type[i].detail != 'undefined') {
                            if (temp_type_detail != "") { temp_type_detail += ":"; }
                            temp_type_detail += stationObj.Station.Type[i].text + "." + stationObj.Station.Type[i].detail;
                        }
                    } else {
                        if (temp_type != "") { temp_type += ":"; }
                        temp_type += stationObj.Station.Type[i];
                    }
                    tmp_station.type = temp_type;
                    tmp_station.type_detail = temp_type_detail;
                }
            }
        }
        // 県コード
        if (typeof stationObj.Prefecture != 'undefined') {
            tmp_station.kenCode = parseInt(stationObj.Prefecture.code);
        }
        return tmp_station;
    }

    /**
    * 駅のリストを出力
    */
    function getStationListItem(n, stationItem) {
        var buffer = "";
        buffer += '<li>';
        if (agent == 2 || agent == 3) {
            buffer += '<div class="exp_stationIcon">';
            buffer += getStationIconType(stationItem.type);
            buffer += '</div>';
        }
        buffer += '<div>';
        buffer += '<a class="exp_stationName" id="' + baseId + ':stationRow:' + String(n) + '" href="Javascript:void(0);" title="' + stationItem.yomi + '">' + stationItem.name + '</a>';
        buffer += '</div>';
        buffer += '</li>';
        return buffer;
    }

    /**
    * IE用に配列の検索機能を実装
    */
    function checkArray(arr, target) {
        for (var i = 0; i < arr.length; i++) {
            if (arr[i] === target) { return i; }
        }
        return -1;
    }

    /**
    * イベントの振り分け
    */
    function onEvent(e) {
        callBackFunctionDelay = false;
        var eventIdList = (e.srcElement) ? e.srcElement.id.split(":") : e.target.id.split(":");
        if (eventIdList.length >= 2) {
            if (eventIdList[1] == "stationRow" && eventIdList.length == 3) {
                // 駅の選択
                setStationNo(parseInt(eventIdList[2]));
                callBackFunctionDelay = true;
                onblurEventCallBack();
            } else if (eventIdList[1] == "stationView" && eventIdList.length >= 3) {
                // 表示切替
                stationView(parseInt(eventIdList[2]) - 1);
                // 駅名を検索
                searchStation(true, oldvalue);
            }
        }
    }

    /**
    * フォームの駅名を返す
    */
    function getStation() {
        if (agent == 1 || agent == 3) {
            return document.getElementById(baseId + ':stationInput').value;
        } else if (agent == 2) {
            return document.getElementById(baseId + ':stationOutput').value;
        }
    }

    /**
    * 検索した駅名リストを返す
    */
    function getStationList() {
        var stationArray = new Array();
        for (var i = 0; i < stationList.length; i++) {
            stationArray.push(stationList[i].name);
        }
        return stationArray.join(",");
    }

    /**
    * 選択中の駅名を返す
    */
    function getStationName() {
        var tmp_station;
        if (agent == 1 || agent == 3) {
            tmp_station = document.getElementById(baseId + ':stationInput').value;
        } else if (agent == 2) {
            tmp_station = document.getElementById(baseId + ':stationOutput').value;
        }
        if (stationList.length > 0) {
            for (var i = 0; i < stationList.length; i++) {
                if (stationList[i].name == tmp_station) {
                    return stationList[i].name;
                }
            }
        }
        return "";
    }

    /**
    * 選択中の駅コードを返す
    */
    function getStationCode() {
        var tmp_station;
        if (agent == 1 || agent == 3) {
            tmp_station = document.getElementById(baseId + ':stationInput').value;
        } else if (agent == 2) {
            tmp_station = document.getElementById(baseId + ':stationOutput').value;
        }
        if (stationList.length > 0) {
            for (var i = 0; i < stationList.length; i++) {
                if (stationList[i].name == tmp_station) {
                    return stationList[i].code;
                }
            }
        }
        return "";
    }


    /**
    * 駅情報の取得
    */
    function getPointObject(station) {
        // オブジェクトコピー用インライン関数
        function clone(obj) {
            var f = function () { };
            f.prototype = obj;
            return new f;
        }
        for (var i = 0; i < stationList.length; i++) {
            if (isNaN(station)) {
                if (stationList[i].name == station) {
                    return clone(stationList[i]);
                }
            } else if (stationList[i].code == station) {
                return clone(stationList[i]);
            }
        }
    }

    /**
    * 検索した駅名リストを閉じる
    */
    function closeStationList() {
        resetCursor();
        if (agent == 1 || agent == 3) {
            document.getElementById(baseId + ':stationList').style.display = "none";
            // コールバック
            if (typeof callBackFunction['close'] == 'function') {
                callBackFunction['close']();
            }
        }
    }

    /**
    * 駅リストを開いているかどうかのチェック
    */
    function checkStationList() {
        if (document.getElementById(baseId + ':stationList').style.display == "block") {
            return true;
        } else {
            return false;
        }
    }

    function resetCursor() {
        if (document.getElementById(baseId + ":stationRow:" + String(selectStation))) {
            document.getElementById(baseId + ":stationRow:" + String(selectStation)).className = "exp_stationName";
        }
        selectStation = 0;
    }

    /**
    * フォームに駅名をセットしてリストを閉じる
    */
    function setStation(str, callback) {
        callBackFunction['callback'] = callback;
        if (agent == 1 || agent == 3) {
            document.getElementById(baseId + ':stationInput').value = str;
            // チェックはしない
            oldvalue = document.getElementById(baseId + ':stationInput').value;
            closeStationList();
        } else if (agent == 2) {
            document.getElementById(baseId + ':stationOutput').value = str;
            //リストを閉じる
            document.getElementById(baseId + ':stationPopup').style.display = "none";
            document.getElementById(baseId + ':stationPopupBack').style.display = "none";
        }
        if (str != "") {
            //駅リスト検索をチェックし、無かった場合は問い合わせ
            if (stationList.length > 0) {
                for (var i = 0; i < stationList.length; i++) {
                    if (stationList[i].name == str) {
                        if (typeof callBackFunction['callback'] == 'function') {
                            callBackFunction['callback'](true);
                            callBackFunction['callback'] = undefined;
                        }
                        return;
                    }
                }
            }
            //一致する駅が無いため、問い合わせ
            searchStation(false, str);
        }
    }

    /**
    * 環境設定
    */
    function setConfigure(name, value) {
        if (name.toLowerCase() == String("apiURL").toLowerCase()) {
            apiURL = value;
        } else if (name.toLowerCase() == String("key").toLowerCase()) {
            key = value;
        } else if (name.toLowerCase() == "type") {
            if (typeof value == "object") {
                stationType = value.join(":");
            } else {
                stationType = value;
            }
        } else if (name.toLowerCase() == String("corporationBind").toLowerCase()) {
            if (typeof value == "object") {
                stationCorporationBind = value.join(":");
            } else {
                stationCorporationBind = value;
            }
        } else if (name.toLowerCase() == String("prefectureCode").toLowerCase()) {
            if (typeof value == "object") {
                stationPrefectureCode = value.join(":");
            } else {
                stationPrefectureCode = value;
            }
        } else if (name.toLowerCase() == String("maxStation").toLowerCase()) {
            maxStation = value;
        } else if (name.toLowerCase() == String("agent").toLowerCase()) {
            agent = value;
        } else if (String(name).toLowerCase() == String("ssl").toLowerCase()) {
            if (String(value).toLowerCase() == "true" || String(value).toLowerCase() == "enable" || String(value).toLowerCase() == "enabled") {
                apiURL = apiURL.replace('http://', 'https://');
            } else {
                apiURL = apiURL.replace('https://', 'http://');
            }
        }
    }

    /**
    * コールバック関数の定義
    */
    function bind(type, func) {
        if (type == 'open' && typeof func == 'function') {
            callBackFunction[type] = func;
        } else if (type == 'close' && typeof func == 'function') {
            callBackFunction[type] = func;
        } else if (type == 'change' && typeof func == 'function') {
            callBackFunction[type] = func;
        } else if (type == 'blur' && typeof func == 'function') {
            callBackFunction[type] = func;
        } else if (type == 'enter' && typeof func == 'function') {
            callBackFunction[type] = func;
        } else if (type == 'focus' && typeof func == 'function') {
            callBackFunction[type] = func;
        }
    }

    /**
    * コールバック関数の解除
    */
    function unbind(type) {
        if (typeof callBackFunction[type] == 'function') {
            callBackFunction[type] = undefined;
        }
    }

    // 外部参照可能な関数リスト
    this.dispStation = dispStation;
    this.getStation = getStation;
    this.setStation = setStation;
    this.getStationList = getStationList;
    this.getStationName = getStationName;
    this.getStationCode = getStationCode;
    this.getPointObject = getPointObject;
    this.closeStationList = closeStationList;
    this.checkStationList = checkStationList;
    this.setConfigure = setConfigure;
    this.bind = bind;
    this.unbind = unbind;

    // 定数リスト
    this.TYPE_TRAIN = "train";
    this.TYPE_PLANE = "plane";
    this.TYPE_SHIP = "ship";
    this.TYPE_BUS = "bus";
    this.TYPE_WALK = "walk";
    this.TYPE_STRANGE = "strange";
    this.TYPE_BUS_LOCAL = "bus.local";
    this.TYPE_BUS_CONNECTION = "bus.connection";
    this.TYPE_BUS_HIGHWAY = "bus.highway";
    this.TYPE_BUS_MIDNIGHT = "bus.midnight";
    this.TYPE_TRAIN_LIMITEDEXPRESS = "train.limitedExpress";
    this.TYPE_TRAIN_SHINKANSEN = "train.shinkansen";
    this.TYPE_TRAIN_SLEEPERTRAIN = "train.sleeperTrain";
    this.TYPE_TRAIN_LINER = "train.liner";
    this.TDFK_HOKKAIDO = 1;
    this.TDFK_AOMORI = 2;
    this.TDFK_IWATE = 3;
    this.TDFK_MIYAGI = 4;
    this.TDFK_AKITA = 5;
    this.TDFK_YAMAGATA = 6;
    this.TDFK_FUKUSHIMA = 7;
    this.TDFK_IBARAKI = 8;
    this.TDFK_TOCHIGI = 9;
    this.TDFK_GUNMA = 10;
    this.TDFK_SAITAMA = 11;
    this.TDFK_CHIBA = 12;
    this.TDFK_TOKYO = 13;
    this.TDFK_KANAGAWA = 14;
    this.TDFK_NIIGATA = 15;
    this.TDFK_TOYAMA = 16;
    this.TDFK_ISHIKAWA = 17;
    this.TDFK_FUKUI = 18;
    this.TDFK_YAMANASHI = 19;
    this.TDFK_NAGANO = 20;
    this.TDFK_GIFU = 21;
    this.TDFK_SHIZUOKA = 22;
    this.TDFK_AICHI = 23;
    this.TDFK_MIE = 24;
    this.TDFK_SHIGA = 25;
    this.TDFK_KYOTO = 26;
    this.TDFK_OSAKA = 27;
    this.TDFK_HYOGO = 28;
    this.TDFK_NARA = 29;
    this.TDFK_WAKAYAMA = 30;
    this.TDFK_TOTTORI = 31;
    this.TDFK_SHIMANE = 32;
    this.TDFK_OKAYAMA = 33;
    this.TDFK_HIROSHIMA = 34;
    this.TDFK_YAMAGUCHI = 35;
    this.TDFK_TOKUSHIMA = 36;
    this.TDFK_KAGAWA = 37;
    this.TDFK_EHIME = 38;
    this.TDFK_KOCHI = 39;
    this.TDFK_FUKUOKA = 40;
    this.TDFK_SAGA = 41;
    this.TDFK_NAGASAKI = 42;
    this.TDFK_KUMAMOTO = 43;
    this.TDFK_OITA = 44;
    this.TDFK_MIYAZAKI = 45;
    this.TDFK_KAGOSHIMA = 46;
    this.TDFK_OKINAWA = 47;

    // 端末制御
    this.AGENT_PC = 1;
    this.AGENT_PHONE = 2;
    this.AGENT_TABLET = 3;
};
