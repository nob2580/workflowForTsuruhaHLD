package eteam.common;

import java.util.List;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.util.ThreadLocalUtil;

/**
 * 設定情報テーブルの情報保持用
 * 都度SELECTするより多少パフォーマンスが上がるのと、connectしないと触れないのが無駄なので。
 */
public class EteamSettingInfo {

	/** ログ */
	protected static EteamLogger log = EteamLogger.getLogger(EteamSettingInfo.class);

	// キー指定取得ではなくプロパティを持たせ、その中でキー指定。データ型変更必要な場合はプロパティ内でやる。
	/**
	* アプリURL
	* @return アプリURL
	*/
	public String shotodokeUrl(){return getSettingInfo(Key.SHOTODOKE_URL);}
	/**
	* メール通知システム名
	* @return メール通知システム名
	*/
	public String mailTsuuchiSystemName(){return getSettingInfo(Key.MAIL_TSUUCHI_SYSTEM_NAME);}
	/**
	* パスワードの最小文字数
	* @return パスワードの最小文字数
	*/
	public String passLenMin(){return getSettingInfo(Key.PASS_LEN_MIN);}
	/**
	* パスワードの最小文字種類
	* @return パスワードの最小文字種類
	*/
	public String passKindMin(){return getSettingInfo(Key.PASS_KIND_MIN);}
	/**
	* パスワードの有効期間(日数)
	* @return パスワードの有効期間(日数)
	*/
	public String passValidTerm(){return getSettingInfo(Key.PASS_VALID_TERM);}
	/**
	* アカウント一時ロックするパスワード誤り回数
	* @return アカウント一時ロックするパスワード誤り回数
	*/
	public String passLockNum(){return getSettingInfo(Key.PASS_LOCK_NUM);}
	/**
	* アカウント一時ロック時間(分)
	* @return アカウント一時ロック時間(分)
	*/
	public String passLockTerm(){return getSettingInfo(Key.PASS_LOCK_TERM);}
	/**
	* 初期パスワードの変更を必須とする
	* @return 初期パスワードの変更を必須とする
	*/
	public String requirePassChange(){return getSettingInfo(Key.REQUIRE_PASS_CHANGE);}
	/**
	* 連携フラグ
	* @return 連携フラグ
	*/
	public String intraFlg(){return getSettingInfo(Key.INTRA_FLG);}
	/**
	* イントラ版起動URL
	* @return イントラ版起動URL
	*/
	public String ekispertIntraActionUrl(){return getSettingInfo(Key.EKISPERT_INTRA_ACTION_URL);}
	/**
	* Webサービス駅名検索URL
	* @return Webサービス駅名検索URL
	*/
	public String ekispertWebUrlStation(){return getSettingInfo(Key.EKISPERT_WEB_URL_STATION);}
	/**
	* Webサービス経路検索URL
	* @return Webサービス経路検索URL
	*/
	public String ekispertWebUrlRoute(){return getSettingInfo(Key.EKISPERT_WEB_URL_ROUTE);}
	/**
	* Webサービス路線図一覧検索URL
	* @return Webサービス路線図一覧検索URL
	*/
	public String ekispertWebUrlMapList(){return getSettingInfo(Key.EKISPERT_WEB_URL_MAP_LIST);}
	/**
	* Webサービス路線図駅名検索URL
	* @return Webサービス路線図駅名検索URL
	*/
	public String ekispertWebUrlMapStaton(){return getSettingInfo(Key.EKISPERT_WEB_URL_MAP_STATON);}
	/**
	* Webサービス路線図取得URL
	* @return Webサービス路線図取得URL
	*/
	public String ekispertWebUrlMapImage(){return getSettingInfo(Key.EKISPERT_WEB_URL_MAP_IMAGE);}
	/**
	* Webサービス探索条件生成URL
	* @return Webサービス探索条件生成URL
	*/
	public String ekispertWebSearch(){return getSettingInfo(Key.EKISPERT_WEB_SEARCH);}
	/**
	* IC使用区分
	* @return IC使用区分
	*/
	public String ekispertIcKbn(){return getSettingInfo(Key.EKISPERT_IC_KBN);}
	/**
	* IC乗車券区分の全社指定
	* @return IC乗車券区分の全社指定
	*/
	public String ekispertIcZenshashitei(){return getSettingInfo(Key.EKISPERT_IC_ZENSHASHITEI);}
	/**
	* Webサービス探索優先度
	* @return Webサービス探索優先度
	*/
	public String ekispertWebIcPreferred(){return getSettingInfo(Key.EKISPERT_WEB_IC_PREFERRED);}
	/**
	* 定期区間の反映先
	* @return 定期区間の反映先
	*/
	public String ekispertTeikikukanHaneisaki(){return getSettingInfo(Key.EKISPERT_TEIKIKUKAN_HANEISAKI);}
	/**
	* 経路表示方法
	* @return 経路表示方法
	*/
	public String ekispertKeiroKanihyouji(){return getSettingInfo(Key.EKISPERT_KEIRO_KANIHYOUJI);}
	/**
	* 距離情報の反映先
	* @return 距離情報の反映先
	*/
	public String ekispertKyoriHanei(){return getSettingInfo(Key.EKISPERT_KYORI_HANEI);}
	/**
	* 割引情報の反映先(EX予約/スマートEX/新幹線eチケット)
	* @return 割引情報の反映先(EX予約/スマートEX/新幹線eチケット)
	*/
	public String ekispertWaribikiHanei(){return getSettingInfo(Key.EKISPERT_WARIBIKI_HANEI);}
	/**
	* 往復割引運賃適応の反映先
	* @return 往復割引運賃適応の反映先
	*/
	public String ekispertOufukuwariHanei(){return getSettingInfo(Key.EKISPERT_OUFUKUWARI_HANEI);}
	/**
	* 法人カード利用明細の日付変更
	* @return 法人カード利用明細の日付変更
	*/
	public String houjinCardDateEnabled(){return getSettingInfo(Key.HOUJIN_CARD_DATE_ENABLED);}
	/**
	* 法人カード履歴の除外
	* @return 法人カード履歴の除外
	*/
	public String houjinCardRirekiJyogaiEnable(){return getSettingInfo(Key.HOUJIN_CARD_RIREKI_JYOGAI_ENABLE);}
	/**
	* 法人カード履歴を除外するユーザー
	* @return 法人カード履歴を除外するユーザー
	*/
	public String houjinCardRirekiJyogaiUser(){return getSettingInfo(Key.HOUJIN_CARD_RIREKI_JYOGAI_USER);}
	/**
	* ICカードデフォルト交通手段
	* @return ICカードデフォルト交通手段
	*/
	public String icCardTrain(){return getSettingInfo(Key.IC_CARD_TRAIN);}
	/**
	* ICカード「バス/路面等」交通手段
	* @return ICカード「バス/路面等」交通手段
	*/
	public String icCardBus(){return getSettingInfo(Key.IC_CARD_BUS);}
	/**
	* ICカード明細の日付変更
	* @return ICカード明細の日付変更
	*/
	public String icCardDateEnabled(){return getSettingInfo(Key.IC_CARD_DATE_ENABLED);}
	/**
	* FBデータ出力ファイルパス
	* @return FBデータ出力ファイルパス
	*/
	public String outputFilePathFbdata(){return getSettingInfo(Key.OUTPUT_FILE_PATH_FBDATA);}
	/**
	* FBデータ出力ファイル名
	* @return FBデータ出力ファイル名
	*/
	public String outputFileNmFbdata(){return getSettingInfo(Key.OUTPUT_FILE_NM_FBDATA);}
	/**
	* 支払日リスト件数
	* @return 支払日リスト件数
	*/
	public String listNumForShiharaibi(){return getSettingInfo(Key.LIST_NUM_FOR_SHIHARAIBI);}
	/**
	* 顧客コード1への社員コード反映
	* @return 顧客コード1への社員コード反映
	*/
	public String shainCd2KokyakuCd1(){return getSettingInfo(Key.SHAIN_CD2KOKYAKU_CD1);}
	/**
	* 全銀総合振込依頼データ形式
	* @return 全銀総合振込依頼データ形式
	*/
	public String zenginFurikomiKeishiki(){return getSettingInfo(Key.ZENGIN_FURIKOMI_KEISHIKI);}
	/**
	* XMLメッセージ単位の識別番号
	* @return XMLメッセージ単位の識別番号
	*/
	public String xmlMessageShikibetsuNo(){return getSettingInfo(Key.XML_MESSAGE_SHIKIBETSU_NO);}
	/**
	* 会社コード
	* @return 会社コード
	*/
	public String op21MparamKaishaCd(){return getSettingInfo(Key.OP21MPARAM_KAISHA_CD);}
	/**
	* 諸口コード
	* @return 諸口コード
	*/
	public String shokuchiCd(){return getSettingInfo(Key.SHOKUCHI_CD);}
	/**
	* 処理区分
	* @return 処理区分
	*/
	public String op21MparamShoriKbn(){return getSettingInfo(Key.OP21MPARAM_SHORI_KBN);}
	/**
	* 伝票形式
	* @return 伝票形式
	*/
	public String op21MparamDenpyouKeishiki(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI);}
	/**
	* 伝票形式(経費立替精算)
	* @return 伝票形式(経費立替精算)
	*/
	public String op21MparamDenpyouKeishikiA001(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A001);}
	/**
	* 伝票形式(経費伺い申請（仮払申請）)
	* @return 伝票形式(経費伺い申請（仮払申請）)
	*/
	public String op21MparamDenpyouKeishikiA002(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A002);}
	/**
	* 伝票形式(請求書払い申請)
	* @return 伝票形式(請求書払い申請)
	*/
	public String op21MparamDenpyouKeishikiA003(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A003);}
	/**
	* 伝票形式(支払依頼申請)
	* @return 伝票形式(支払依頼申請)
	*/
	public String op21MparamDenpyouKeishikiA013(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A013);}
	/**
	* 伝票形式(自動引落伝票)
	* @return 伝票形式(自動引落伝票)
	*/
	public String op21MparamDenpyouKeishikiA009(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A009);}
	/**
	* 伝票形式(出張旅費精算)
	* @return 伝票形式(出張旅費精算)
	*/
	public String op21MparamDenpyouKeishikiA004(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A004);}
	/**
	* 伝票形式(出張伺い申請（仮払申請）)
	* @return 伝票形式(出張伺い申請（仮払申請）)
	*/
	public String op21MparamDenpyouKeishikiA005(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A005);}
	/**
	* 伝票形式(海外出張旅費精算)
	* @return 伝票形式(海外出張旅費精算)
	*/
	public String op21MparamDenpyouKeishikiA011(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A011);}
	/**
	* 伝票形式(海外出張伺い申請（仮払申請）)
	* @return 伝票形式(海外出張伺い申請（仮払申請）)
	*/
	public String op21MparamDenpyouKeishikiA012(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A012);}
	/**
	* 伝票形式(交通費精算)
	* @return 伝票形式(交通費精算)
	*/
	public String op21MparamDenpyouKeishikiA010(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A010);}
	/**
	* 伝票形式(通勤定期申請)
	* @return 伝票形式(通勤定期申請)
	*/
	public String op21MparamDenpyouKeishikiA006(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A006);}
	/**
	* 不良データログ
	* @return 不良データログ
	*/
	public String op21MparamFuryouDataLog(){return getSettingInfo(Key.OP21MPARAM_FURYOU_DATA_LOG);}
	/**
	* CSV用ﾊﾟｽ
	* @return CSV用ﾊﾟｽ
	*/
	public String op21MparamCsvPath(){return getSettingInfo(Key.OP21MPARAM_CSV_PATH);}
	/**
	* CSVファイル名
	* @return CSVファイル名
	*/
	public String op21MparamCsvFileNm(){return getSettingInfo(Key.OP21MPARAM_CSV_FILE_NM);}
	/**
	* ログ用パス
	* @return ログ用パス
	*/
	public String op21MparamLogPath(){return getSettingInfo(Key.OP21MPARAM_LOG_PATH);}
	/**
	* ログファイル名
	* @return ログファイル名
	*/
	public String op21MparamLogFileNm(){return getSettingInfo(Key.OP21MPARAM_LOG_FILE_NM);}
	/**
	* レイアウトNo
	* @return レイアウトNo
	*/
	public String op21MparamLayoutNo(){return getSettingInfo(Key.OP21MPARAM_LAYOUT_NO);}
	/**
	* 伝票入力パターン
	* @return 伝票入力パターン
	*/
	public String op21MparamDenpyouNyuuryokuPattern(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN);}
	/**
	* 伝票入力パターン(経費立替精算)
	* @return 伝票入力パターン(経費立替精算)
	*/
	public String op21MparamDenpyouNyuuryokuPatternA001(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A001);}
	/**
	* 伝票入力パターン(経費伺い申請（仮払申請）)
	* @return 伝票入力パターン(経費伺い申請（仮払申請）)
	*/
	public String op21MparamDenpyouNyuuryokuPatternA002(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A002);}
	/**
	* 伝票入力パターン(請求書払い申請)
	* @return 伝票入力パターン(請求書払い申請)
	*/
	public String op21MparamDenpyouNyuuryokuPatternA003(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A003);}
	/**
	* 伝票入力パターン(支払依頼申請)
	* @return 伝票入力パターン(支払依頼申請)
	*/
	public String op21MparamDenpyouNyuuryokuPatternA013(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A013);}
	/**
	* 伝票入力パターン(自動引落伝票)
	* @return 伝票入力パターン(自動引落伝票)
	*/
	public String op21MparamDenpyouNyuuryokuPatternA009(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A009);}
	/**
	* 伝票入力パターン(出張旅費精算)
	* @return 伝票入力パターン(出張旅費精算)
	*/
	public String op21MparamDenpyouNyuuryokuPatternA004(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A004);}
	/**
	* 伝票入力パターン(出張伺い申請（仮払申請）)
	* @return 伝票入力パターン(出張伺い申請（仮払申請）)
	*/
	public String op21MparamDenpyouNyuuryokuPatternA005(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A005);}
	/**
	* 伝票入力パターン(海外出張旅費精算)
	* @return 伝票入力パターン(海外出張旅費精算)
	*/
	public String op21MparamDenpyouNyuuryokuPatternA011(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A011);}
	/**
	* 伝票入力パターン(海外出張伺い申請（仮払申請）)
	* @return 伝票入力パターン(海外出張伺い申請（仮払申請）)
	*/
	public String op21MparamDenpyouNyuuryokuPatternA012(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A012);}
	/**
	* 伝票入力パターン(交通費精算)
	* @return 伝票入力パターン(交通費精算)
	*/
	public String op21MparamDenpyouNyuuryokuPatternA010(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A010);}
	/**
	* 伝票入力パターン(通勤定期申請)
	* @return 伝票入力パターン(通勤定期申請)
	*/
	public String op21MparamDenpyouNyuuryokuPatternA006(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A006);}
	/**
	* 邦貨換算フラグ
	* @return 邦貨換算フラグ
	*/
	public String op21MparamHoukaKanzanFlg(){return getSettingInfo(Key.OP21MPARAM_HOUKA_KANZAN_FLG);}
	/**
	* 起動ユーザー
	* @return 起動ユーザー
	*/
	public String op21MparamKidouUser(){return getSettingInfo(Key.OP21MPARAM_KIDOU_USER);}
	/**
	* 仕訳区分
	* @return 仕訳区分
	*/
	public String shiwakeKbn(){return getSettingInfo(Key.SHIWAKE_KBN);}
	/**
	* 確定有無
	* @return 確定有無
	*/
	public String kakuteiUmu(){return getSettingInfo(Key.KAKUTEI_UMU);}
	/**
	* 伝票IDの連携先
	* @return 伝票IDの連携先
	*/
	public String uf1DenpyouIdHanei(){return getSettingInfo(Key.UF1_DENPYOU_ID_HANEI);}
	/**
	* 掛けなし　支払結果仕訳作成有無
	* @return 掛けなし　支払結果仕訳作成有無
	*/
	public String kakeNashiShiharaiShiwakeSakusei(){return getSettingInfo(Key.KAKE_NASHI_SHIHARAI_SHIWAKE_SAKUSEI);}
	/**
	* 掛けあり　支払結果仕訳作成有無
	* @return 掛けあり　支払結果仕訳作成有無
	*/
	public String kakeAriShiharaiShiwakeSakusei(){return getSettingInfo(Key.KAKE_ARI_SHIHARAI_SHIWAKE_SAKUSEI);}
	/**
	* 通勤定期　仕訳作成有無
	* @return 通勤定期　仕訳作成有無
	*/
	public String tsuukinteikiShiwakeSakuseiUmu(){return getSettingInfo(Key.TSUUKINTEIKI_SHIWAKE_SAKUSEI_UMU);}
	/**
	* 社員コードの連携先
	* @return 社員コードの連携先
	*/
	public String shainCdRenkei(){return getSettingInfo(Key.SHAIN_CD_RENKEI);}
	/**
	* 法人カード識別用番号の連携先
	* @return 法人カード識別用番号の連携先
	*/
	public String houjinCardRenkei(){return getSettingInfo(Key.HOUJIN_CARD_RENKEI);}
	/**
	* ヘッダーフィールドマッピング①
	* @return ヘッダーフィールドマッピング①
	*/
	public String hf1Mapping(){return getSettingInfo(Key.HF1_MAPPING);}
	/**
	* ヘッダーフィールドマッピング②
	* @return ヘッダーフィールドマッピング②
	*/
	public String hf2Mapping(){return getSettingInfo(Key.HF2_MAPPING);}
	/**
	* ヘッダーフィールドマッピング③
	* @return ヘッダーフィールドマッピング③
	*/
	public String hf3Mapping(){return getSettingInfo(Key.HF3_MAPPING);}
	/**
	* ヘッダーフィールドマッピング④
	* @return ヘッダーフィールドマッピング④
	*/
	public String hf4Mapping(){return getSettingInfo(Key.HF4_MAPPING);}
	/**
	* ヘッダーフィールドマッピング⑤
	* @return ヘッダーフィールドマッピング⑤
	*/
	public String hf5Mapping(){return getSettingInfo(Key.HF5_MAPPING);}
	/**
	* ヘッダーフィールドマッピング⑥
	* @return ヘッダーフィールドマッピング⑥
	*/
	public String hf6Mapping(){return getSettingInfo(Key.HF6_MAPPING);}
	/**
	* ヘッダーフィールドマッピング⑦
	* @return ヘッダーフィールドマッピング⑦
	*/
	public String hf7Mapping(){return getSettingInfo(Key.HF7_MAPPING);}
	/**
	* ヘッダーフィールドマッピング⑧
	* @return ヘッダーフィールドマッピング⑧
	*/
	public String hf8Mapping(){return getSettingInfo(Key.HF8_MAPPING);}
	/**
	* ヘッダーフィールドマッピング⑨
	* @return ヘッダーフィールドマッピング⑨
	*/
	public String hf9Mapping(){return getSettingInfo(Key.HF9_MAPPING);}
	/**
	* ヘッダーフィールドマッピング⑩
	* @return ヘッダーフィールドマッピング⑩
	*/
	public String hf10Mapping(){return getSettingInfo(Key.HF10_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング①
	* @return ユニバーサルフィールドマッピング①
	*/
	public String uf1Mapping(){return getSettingInfo(Key.UF1_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング②
	* @return ユニバーサルフィールドマッピング②
	*/
	public String uf2Mapping(){return getSettingInfo(Key.UF2_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング③
	* @return ユニバーサルフィールドマッピング③
	*/
	public String uf3Mapping(){return getSettingInfo(Key.UF3_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング④
	* @return ユニバーサルフィールドマッピング④
	*/
	public String uf4Mapping(){return getSettingInfo(Key.UF4_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング⑤
	* @return ユニバーサルフィールドマッピング⑤
	*/
	public String uf5Mapping(){return getSettingInfo(Key.UF5_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング⑥
	* @return ユニバーサルフィールドマッピング⑥
	*/
	public String uf6Mapping(){return getSettingInfo(Key.UF6_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング⑦
	* @return ユニバーサルフィールドマッピング⑦
	*/
	public String uf7Mapping(){return getSettingInfo(Key.UF7_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング⑧
	* @return ユニバーサルフィールドマッピング⑧
	*/
	public String uf8Mapping(){return getSettingInfo(Key.UF8_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング⑨
	* @return ユニバーサルフィールドマッピング⑨
	*/
	public String uf9Mapping(){return getSettingInfo(Key.UF9_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング⑩
	* @return ユニバーサルフィールドマッピング⑩
	*/
	public String uf10Mapping(){return getSettingInfo(Key.UF10_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング(固定値)①
	* @return ユニバーサルフィールドマッピング(固定値)①
	*/
	public String ufKotei1Mapping(){return getSettingInfo(Key.UF_KOTEI1_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング(固定値)②
	* @return ユニバーサルフィールドマッピング(固定値)②
	*/
	public String ufKotei2Mapping(){return getSettingInfo(Key.UF_KOTEI2_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング(固定値)③
	* @return ユニバーサルフィールドマッピング(固定値)③
	*/
	public String ufKotei3Mapping(){return getSettingInfo(Key.UF_KOTEI3_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング(固定値)④
	* @return ユニバーサルフィールドマッピング(固定値)④
	*/
	public String ufKotei4Mapping(){return getSettingInfo(Key.UF_KOTEI4_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング(固定値)⑤
	* @return ユニバーサルフィールドマッピング(固定値)⑤
	*/
	public String ufKotei5Mapping(){return getSettingInfo(Key.UF_KOTEI5_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング(固定値)⑥
	* @return ユニバーサルフィールドマッピング(固定値)⑥
	*/
	public String ufKotei6Mapping(){return getSettingInfo(Key.UF_KOTEI6_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング(固定値)⑦
	* @return ユニバーサルフィールドマッピング(固定値)⑦
	*/
	public String ufKotei7Mapping(){return getSettingInfo(Key.UF_KOTEI7_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング(固定値)⑧
	* @return ユニバーサルフィールドマッピング(固定値)⑧
	*/
	public String ufKotei8Mapping(){return getSettingInfo(Key.UF_KOTEI8_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング(固定値)⑨
	* @return ユニバーサルフィールドマッピング(固定値)⑨
	*/
	public String ufKotei9Mapping(){return getSettingInfo(Key.UF_KOTEI9_MAPPING);}
	/**
	* ユニバーサルフィールドマッピング(固定値)⑩
	* @return ユニバーサルフィールドマッピング(固定値)⑩
	*/
	public String ufKotei10Mapping(){return getSettingInfo(Key.UF_KOTEI10_MAPPING);}
	/**
	* 添付ファイル連携
	* @return 添付ファイル連携
	*/
	public String tenpuFileRenkei(){return getSettingInfo(Key.TENPU_FILE_RENKEI);}
	/**
	* 仕訳作成エラー発生時の進め方
	* @return 仕訳作成エラー発生時の進め方
	*/
	public String shiwakeSakuseiErrorAction(){return getSettingInfo(Key.SHIWAKE_SAKUSEI_ERROR_ACTION);}
	/**
	* 先日付の仕訳連携
	* @return 先日付の仕訳連携
	*/
	public String shiwakeSakuseiSakihiduke(){return getSettingInfo(Key.SHIWAKE_SAKUSEI_SAKIHIDUKE);}
	/**
	* 仕訳作成方法(経費立替精算)
	* @return 仕訳作成方法(経費立替精算)
	*/
	public String shiwakeSakuseiHouhouA001(){return getSettingInfo(Key.SHIWAKE_SAKUSEI_HOUHOU_A001);}
	/**
	* 仕訳作成方法(自動引落)
	* @return 仕訳作成方法(自動引落)
	*/
	public String shiwakeSakuseiHouhouA009(){return getSettingInfo(Key.SHIWAKE_SAKUSEI_HOUHOU_A009);}
	/**
	* 仕訳作成方法(出張旅費精算)
	* @return 仕訳作成方法(出張旅費精算)
	*/
	public String shiwakeSakuseiHouhouA004(){return getSettingInfo(Key.SHIWAKE_SAKUSEI_HOUHOU_A004);}
	/**
	* 仕訳作成方法(海外出張旅費精算)
	* @return 仕訳作成方法(海外出張旅費精算)
	*/
	public String shiwakeSakuseiHouhouA011(){return getSettingInfo(Key.SHIWAKE_SAKUSEI_HOUHOU_A011);}
	/**
	* 仕訳作成方法(交通費精算)
	* @return 仕訳作成方法(交通費精算)
	*/
	public String shiwakeSakuseiHouhouA010(){return getSettingInfo(Key.SHIWAKE_SAKUSEI_HOUHOU_A010);}
	/**
	* 仮払残金仕訳作成有無(経費立替精算)
	* @return 仮払残金仕訳作成有無(経費立替精算)
	*/
	public String zankinShiwakeSakuseiUmuA001(){return getSettingInfo(Key.ZANKIN_SHIWAKE_SAKUSEI_UMU_A001);}
	/**
	* 仮払残金仕訳作成有無(出張旅費精算)
	* @return 仮払残金仕訳作成有無(出張旅費精算)
	*/
	public String zankinShiwakeSakuseiUmuA004(){return getSettingInfo(Key.ZANKIN_SHIWAKE_SAKUSEI_UMU_A004);}
	/**
	* 仮払残金仕訳作成有無(海外出張旅費精算)
	* @return 仮払残金仕訳作成有無(海外出張旅費精算)
	*/
	public String zankinShiwakeSakuseiUmuA011(){return getSettingInfo(Key.ZANKIN_SHIWAKE_SAKUSEI_UMU_A011);}
	/**
	* 仮払残金仕訳の日付(経費立替精算)
	* @return 仮払残金仕訳の日付(経費立替精算)
	*/
	public String zankinShiwakeSakuseiHiA001(){return getSettingInfo(Key.ZANKIN_SHIWAKE_SAKUSEI_HI_A001);}
	/**
	* 仮払残金仕訳の日付(出張旅費精算)
	* @return 仮払残金仕訳の日付(出張旅費精算)
	*/
	public String zankinShiwakeSakuseiHiA004(){return getSettingInfo(Key.ZANKIN_SHIWAKE_SAKUSEI_HI_A004);}
	/**
	* 仮払残金仕訳の日付(海外出張旅費精算)
	* @return 仮払残金仕訳の日付(海外出張旅費精算)
	*/
	public String zankinShiwakeSakuseiHiA011(){return getSettingInfo(Key.ZANKIN_SHIWAKE_SAKUSEI_HI_A011);}
	/**
	* 開始番号(伝票番号)
	* @return 開始番号(伝票番号)
	*/
	public String denpyouSerialNoStart(){return getSettingInfo(Key.DENPYOU_SERIAL_NO_START);}
	/**
	* 終了番号(伝票番号)
	* @return 終了番号(伝票番号)
	*/
	public String denpyouSerialNoEnd(){return getSettingInfo(Key.DENPYOU_SERIAL_NO_END);}
	/**
	* 経費立替精算
	* @return 経費立替精算
	*/
	public String denpyouSakuseiTaniA001(){return getSettingInfo(Key.DENPYOU_SAKUSEI_TANI_A001);}
	/**
	* 経費伺い申請（仮払申請）
	* @return 経費伺い申請（仮払申請）
	*/
	public String denpyouSakuseiTaniA002(){return getSettingInfo(Key.DENPYOU_SAKUSEI_TANI_A002);}
	/**
	* 請求書払い申請
	* @return 請求書払い申請
	*/
	public String denpyouSakuseiTaniA003(){return getSettingInfo(Key.DENPYOU_SAKUSEI_TANI_A003);}
	/**
	* 支払依頼申請
	* @return 支払依頼申請
	*/
	public String denpyouSakuseiTaniA013(){return getSettingInfo(Key.DENPYOU_SAKUSEI_TANI_A013);}
	/**
	* 自動引落伝票
	* @return 自動引落伝票
	*/
	public String denpyouSakuseiTaniA009(){return getSettingInfo(Key.DENPYOU_SAKUSEI_TANI_A009);}
	/**
	* 出張旅費精算
	* @return 出張旅費精算
	*/
	public String denpyouSakuseiTaniA004(){return getSettingInfo(Key.DENPYOU_SAKUSEI_TANI_A004);}
	/**
	* 出張伺い申請（仮払申請）
	* @return 出張伺い申請（仮払申請）
	*/
	public String denpyouSakuseiTaniA005(){return getSettingInfo(Key.DENPYOU_SAKUSEI_TANI_A005);}
	/**
	* 海外出張旅費精算
	* @return 海外出張旅費精算
	*/
	public String denpyouSakuseiTaniA011(){return getSettingInfo(Key.DENPYOU_SAKUSEI_TANI_A011);}
	/**
	* 海外出張伺い申請（仮払申請）
	* @return 海外出張伺い申請（仮払申請）
	*/
	public String denpyouSakuseiTaniA012(){return getSettingInfo(Key.DENPYOU_SAKUSEI_TANI_A012);}
	/**
	* 交通費精算
	* @return 交通費精算
	*/
	public String denpyouSakuseiTaniA010(){return getSettingInfo(Key.DENPYOU_SAKUSEI_TANI_A010);}
	/**
	* 通勤定期申請
	* @return 通勤定期申請
	*/
	public String denpyouSakuseiTaniA006(){return getSettingInfo(Key.DENPYOU_SAKUSEI_TANI_A006);}
	/**
	* 振替伝票
	* @return 振替伝票
	*/
	public String denpyouSakuseiTaniA007(){return getSettingInfo(Key.DENPYOU_SAKUSEI_TANI_A007);}
	/**
	* 総合付替伝票
	* @return 総合付替伝票
	*/
	public String denpyouSakuseiTaniA008(){return getSettingInfo(Key.DENPYOU_SAKUSEI_TANI_A008);}
	/**
	* 経費立替精算
	* @return 経費立替精算
	*/
	public String shiwakeTekiyouNaiyouA001(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A001);}
	/**
	* 経費立替仮払相殺
	* @return 経費立替仮払相殺
	*/
	public String shiwakeTekiyouNaiyouA0011(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A001_1);}
	/**
	* (仮払未使用)経費立替仮払相殺
	* @return (仮払未使用)経費立替仮払相殺
	*/
	public String shiwakeTekiyouNaiyouA0012(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A001_2);}
	/**
	* 経費立替仮払残金
	* @return 経費立替仮払残金
	*/
	public String shiwakeTekiyouNaiyouA0013(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A001_3);}
	/**
	* 経費伺い申請（仮払申請）
	* @return 経費伺い申請（仮払申請）
	*/
	public String shiwakeTekiyouNaiyouA002(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A002);}
	/**
	* 請求書払い申請
	* @return 請求書払い申請
	*/
	public String shiwakeTekiyouNaiyouA003(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A003);}
	/**
	* 支払依頼申請
	* @return 支払依頼申請
	*/
	public String shiwakeTekiyouNaiyouA013(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A013);}
	/**
	* 出張旅費精算
	* @return 出張旅費精算
	*/
	public String shiwakeTekiyouNaiyouA004(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A004);}
	/**
	* 出張旅費精算仮払相殺
	* @return 出張旅費精算仮払相殺
	*/
	public String shiwakeTekiyouNaiyouA0041(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A004_1);}
	/**
	* (仮払未使用)出張旅費精算仮払相殺
	* @return (仮払未使用)出張旅費精算仮払相殺
	*/
	public String shiwakeTekiyouNaiyouA0042(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A004_2);}
	/**
	* 出張旅費精算仮払残金
	* @return 出張旅費精算仮払残金
	*/
	public String shiwakeTekiyouNaiyouA0043(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A004_3);}
	/**
	* 出張伺い申請（仮払申請）
	* @return 出張伺い申請（仮払申請）
	*/
	public String shiwakeTekiyouNaiyouA005(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A005);}
	/**
	* 海外出張旅費精算
	* @return 海外出張旅費精算
	*/
	public String shiwakeTekiyouNaiyouA011(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A011);}
	/**
	* 海外出張旅費精算仮払相殺
	* @return 海外出張旅費精算仮払相殺
	*/
	public String shiwakeTekiyouNaiyouA0111(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A011_1);}
	/**
	* (仮払未使用)海外出張旅費精算仮払相殺
	* @return (仮払未使用)海外出張旅費精算仮払相殺
	*/
	public String shiwakeTekiyouNaiyouA0112(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A011_2);}
	/**
	* 海外出張旅費精算仮払残金
	* @return 海外出張旅費精算仮払残金
	*/
	public String shiwakeTekiyouNaiyouA0113(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A011_3);}
	/**
	* 海外出張伺い申請（仮払申請）
	* @return 海外出張伺い申請（仮払申請）
	*/
	public String shiwakeTekiyouNaiyouA012(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A012);}
	/**
	* 通勤定期申請
	* @return 通勤定期申請
	*/
	public String shiwakeTekiyouNaiyouA006(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A006);}
	/**
	* 自動引落伝票
	* @return 自動引落伝票
	*/
	public String shiwakeTekiyouNaiyouA009(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A009);}
	/**
	* 交通費精算
	* @return 交通費精算
	*/
	public String shiwakeTekiyouNaiyouA010(){return getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A010);}
	/**
	* 支払方法(経費立替精算)
	* @return 支払方法(経費立替精算)
	*/
	public String shiharaiHouhouA001(){return getSettingInfo(Key.SHIHARAI_HOUHOU_A001);}
	/**
	* 支払方法(経費伺い申請（仮払申請）)
	* @return 支払方法(経費伺い申請（仮払申請）)
	*/
	public String shiharaiHouhouA002(){return getSettingInfo(Key.SHIHARAI_HOUHOU_A002);}
	/**
	* 支払方法(海外出張旅費・出張旅費・交通費)
	* @return 支払方法(海外出張旅費・出張旅費・交通費)
	*/
	public String shiharaiHouhouA004(){return getSettingInfo(Key.SHIHARAI_HOUHOU_A004);}
	/**
	* 支払方法(海外出張伺い申請（仮払申請）・出張伺い申請（仮払申請）)
	* @return 支払方法(海外出張伺い申請（仮払申請）・出張伺い申請（仮払申請）)
	*/
	public String shiharaiHouhouA005(){return getSettingInfo(Key.SHIHARAI_HOUHOU_A005);}
	/**
	* 請求書払い申請(計上日入力)
	* @return 請求書払い申請(計上日入力)
	*/
	public String seikyuuKeijouNyuurohyku(){return getSettingInfo(Key.SEIKYUU_KEIJOU_NYUUROHYKU);}
	/**
	* 請求書払い申請(申請者の計上日制限)
	* @return 請求書払い申請(申請者の計上日制限)
	*/
	public String seikyuuKeijouSeigen(){return getSettingInfo(Key.SEIKYUU_KEIJOU_SEIGEN);}
	/**
	* 請求書払い申請(取引先マスター参照)
	* @return 請求書払い申請(取引先マスター参照)
	*/
	public String seikyuuMasref(){return getSettingInfo(Key.SEIKYUU_MASREF);}
	/**
	* 請求書払い申請（支払日一括登録）
	* @return 請求書払い申請（支払日一括登録）
	*/
	public String seikyuuShiharaibiIkkatsu(){return getSettingInfo(Key.SEIKYUU_SHIHARAIBI_IKKATSU);}
	/**
	* 支払依頼申請(計上日制限)
	* @return 支払依頼申請(計上日制限)
	*/
	public String shiharaiiraiKeijouSeigen(){return getSettingInfo(Key.SHIHARAIIRAI_KEIJOU_SEIGEN);}
	/**
	* 支払依頼申請（支払日一括登録）
	* @return 支払依頼申請（支払日一括登録）
	*/
	public String shiharaiiraiShiharaibiIkkatsu(){return getSettingInfo(Key.SHIHARAIIRAI_SHIHARAIBI_IKKATSU);}
	/**
	* 自動引落伝票(計上日入力)
	* @return 自動引落伝票(計上日入力)
	*/
	public String jidouhikiKeijouNyuurohyku(){return getSettingInfo(Key.JIDOUHIKI_KEIJOU_NYUUROHYKU);}
	/**
	* 自動引落伝票(申請者の計上日制限)
	* @return 自動引落伝票(申請者の計上日制限)
	*/
	public String jidouhikiKeijouSeigen(){return getSettingInfo(Key.JIDOUHIKI_KEIJOU_SEIGEN);}
	/**
	* 経費立替精算(計上日入力)
	* @return 経費立替精算(計上日入力)
	*/
	public String keijoubiDefaultA001(){return getSettingInfo(Key.KEIJOUBI_DEFAULT_A001);}
	/**
	* 経費立替精算(申請者の計上日制限)
	* @return 経費立替精算(申請者の計上日制限)
	*/
	public String keihiseisanKeijouSeigen(){return getSettingInfo(Key.KEIHISEISAN_KEIJOU_SEIGEN);}
	/**
	* 出張旅費精算(計上日入力)
	* @return 出張旅費精算(計上日入力)
	*/
	public String keijoubiDefaultA004(){return getSettingInfo(Key.KEIJOUBI_DEFAULT_A004);}
	/**
	* 出張旅費精算(申請者の計上日制限)
	* @return 出張旅費精算(申請者の計上日制限)
	*/
	public String ryohiseisanKeijouSeigen(){return getSettingInfo(Key.RYOHISEISAN_KEIJOU_SEIGEN);}
	/**
	* 海外出張旅費精算(計上日入力)
	* @return 海外出張旅費精算(計上日入力)
	*/
	public String keijoubiDefaultA011(){return getSettingInfo(Key.KEIJOUBI_DEFAULT_A011);}
	/**
	* 海外出張旅費精算(申請者の計上日制限)
	* @return 海外出張旅費精算(申請者の計上日制限)
	*/
	public String kaigairyohiseisanKeijouSeigen(){return getSettingInfo(Key.KAIGAIRYOHISEISAN_KEIJOU_SEIGEN);}
	/**
	* 交通費精算(計上日入力)
	* @return 交通費精算(計上日入力)
	*/
	public String keijoubiDefaultA010(){return getSettingInfo(Key.KEIJOUBI_DEFAULT_A010);}
	/**
	* 交通費精算(申請者の計上日制限)
	* @return 交通費精算(申請者の計上日制限)
	*/
	public String koutsuuhiseisanKeijouSeigen(){return getSettingInfo(Key.KOUTSUUHISEISAN_KEIJOU_SEIGEN);}
	/**
	* 計上日入力
	* @return 計上日入力
	*/
	public String keijouNyuuryoku(){return getSettingInfo(Key.KEIJOU_NYUURYOKU);}
	/**
	* 計上日範囲・前(請求書払い申請)
	* @return 計上日範囲・前(請求書払い申請)
	*/
	public String keijouHaniMaeA003(){return getSettingInfo(Key.KEIJOU_HANI_MAE_A003);}
	/**
	* 計上日範囲・後(請求書払い申請)
	* @return 計上日範囲・後(請求書払い申請)
	*/
	public String keijouHaniAtoA003(){return getSettingInfo(Key.KEIJOU_HANI_ATO_A003);}
	/**
	* 計上日範囲・前(支払依頼申請)
	* @return 計上日範囲・前(支払依頼申請)
	*/
	public String keijouHaniMaeA013(){return getSettingInfo(Key.KEIJOU_HANI_MAE_A013);}
	/**
	* 計上日範囲・後(支払依頼申請)
	* @return 計上日範囲・後(支払依頼申請)
	*/
	public String keijouHaniAtoA013(){return getSettingInfo(Key.KEIJOU_HANI_ATO_A013);}
	/**
	* 計上日範囲・前(自動引落伝票)
	* @return 計上日範囲・前(自動引落伝票)
	*/
	public String keijouHaniMaeA009(){return getSettingInfo(Key.KEIJOU_HANI_MAE_A009);}
	/**
	* 計上日範囲・後(自動引落伝票)
	* @return 計上日範囲・後(自動引落伝票)
	*/
	public String keijouHaniAtoA009(){return getSettingInfo(Key.KEIJOU_HANI_ATO_A009);}
	/**
	* 計上日範囲・前(経費立替精算)
	* @return 計上日範囲・前(経費立替精算)
	*/
	public String keijouHaniMaeA001(){return getSettingInfo(Key.KEIJOU_HANI_MAE_A001);}
	/**
	* 計上日範囲・後(経費立替精算)
	* @return 計上日範囲・後(経費立替精算)
	*/
	public String keijouHaniAtoA001(){return getSettingInfo(Key.KEIJOU_HANI_ATO_A001);}
	/**
	* 計上日範囲・前(出張旅費精算)
	* @return 計上日範囲・前(出張旅費精算)
	*/
	public String keijouHaniMaeA004(){return getSettingInfo(Key.KEIJOU_HANI_MAE_A004);}
	/**
	* 計上日範囲・後(出張旅費精算)
	* @return 計上日範囲・後(出張旅費精算)
	*/
	public String keijouHaniAtoA004(){return getSettingInfo(Key.KEIJOU_HANI_ATO_A004);}
	/**
	* 計上日範囲・前(海外出張旅費精算)
	* @return 計上日範囲・前(海外出張旅費精算)
	*/
	public String keijouHaniMaeA011(){return getSettingInfo(Key.KEIJOU_HANI_MAE_A011);}
	/**
	* 計上日範囲・後(海外出張旅費精算)
	* @return 計上日範囲・後(海外出張旅費精算)
	*/
	public String keijouHaniAtoA011(){return getSettingInfo(Key.KEIJOU_HANI_ATO_A011);}
	/**
	* 計上日範囲・前(交通費精算)
	* @return 計上日範囲・前(交通費精算)
	*/
	public String keijouHaniMaeA010(){return getSettingInfo(Key.KEIJOU_HANI_MAE_A010);}
	/**
	* 計上日範囲・後(交通費精算)
	* @return 計上日範囲・後(交通費精算)
	*/
	public String keijouHaniAtoA010(){return getSettingInfo(Key.KEIJOU_HANI_ATO_A010);}
	/**
	* 経費立替精算
	* @return 経費立替精算
	*/
	public String furikomiRuleA001(){return getSettingInfo(Key.FURIKOMI_RULE_A001);}
	/**
	* 経費伺い申請（仮払申請）
	* @return 経費伺い申請（仮払申請）
	*/
	public String furikomiRuleA002(){return getSettingInfo(Key.FURIKOMI_RULE_A002);}
	/**
	* 出張旅費精算
	* @return 出張旅費精算
	*/
	public String furikomiRuleA004(){return getSettingInfo(Key.FURIKOMI_RULE_A004);}
	/**
	* 出張伺い申請（仮払申請）
	* @return 出張伺い申請（仮払申請）
	*/
	public String furikomiRuleA005(){return getSettingInfo(Key.FURIKOMI_RULE_A005);}
	/**
	* 海外出張旅費精算
	* @return 海外出張旅費精算
	*/
	public String furikomiRuleA011(){return getSettingInfo(Key.FURIKOMI_RULE_A011);}
	/**
	* 海外出張伺い申請（仮払申請）
	* @return 海外出張伺い申請（仮払申請）
	*/
	public String furikomiRuleA012(){return getSettingInfo(Key.FURIKOMI_RULE_A012);}
	/**
	* 交通費精算
	* @return 交通費精算
	*/
	public String furikomiRuleA010(){return getSettingInfo(Key.FURIKOMI_RULE_A010);}
	/**
	* 通勤定期申請
	* @return 通勤定期申請
	*/
	public String furikomiRuleA006(){return getSettingInfo(Key.FURIKOMI_RULE_A006);}
	/**
	* 押印枠　出力形式
	* @return 押印枠　出力形式
	*/
	public String ouinFormat(){return getSettingInfo(Key.OUIN_FORMAT);}
	/**
	* 押印枠　表示内容
	* @return 押印枠　表示内容
	*/
	public String ouinNaiyou(){return getSettingInfo(Key.OUIN_NAIYOU);}
	/**
	* 印影（画像）表示
	* @return 印影（画像）表示
	*/
	public String inei(){return getSettingInfo(Key.INEI);}
	/**
	* 押印枠サイズ
	* @return 押印枠サイズ
	*/
	public String ouinSize(){return getSettingInfo(Key.OUIN_SIZE);}
	/**
	* 添付ファイルのファイル名出力
	* @return 添付ファイルのファイル名出力
	*/
	public String filenameOut(){return getSettingInfo(Key.FILENAME_OUT);}
	/**
	* 遡及日適用印刷機能利用
	* @return 遡及日適用印刷機能利用
	*/
	public String sokyuInsatsu(){return getSettingInfo(Key.SOKYU_INSATSU);}
	/**
	* 経費明細一覧に部門を出力する
	* @return 経費明細一覧に部門を出力する
	*/
	public String keihimeisaiBumon(){return getSettingInfo(Key.KEIHIMEISAI_BUMON);}
	/**
	* 経費明細一覧に枝番を出力する
	* @return 経費明細一覧に枝番を出力する
	*/
	public String keihimeisaiEdano(){return getSettingInfo(Key.KEIHIMEISAI_EDANO);}
	/**
	* 経費明細一覧セキュリティパターン設定
	* @return 経費明細一覧セキュリティパターン設定
	*/
	public String keihimeisaiSecurityPsettei(){return getSettingInfo(Key.KEIHIMEISAI_SECURITY_PSETTEI);}
	/**
	* 経費立替精算　消費税額設定
	* @return 経費立替精算　消費税額設定
	*/
	public String zeinukiZeigakuHenkouA001(){return getSettingInfo(Key.ZEINUKI_ZEIGAKU_HENKOU_A001);}
	/**
	* 請求書払い申請　消費税額設定
	* @return 請求書払い申請　消費税額設定
	*/
	public String zeinukiZeigakuHenkouA003(){return getSettingInfo(Key.ZEINUKI_ZEIGAKU_HENKOU_A003);}
	/**
	* 請求書払い申請　入力方式
	* @return 請求書払い申請　入力方式
	*/
	public String zeiDefaultA003(){return getSettingInfo(Key.ZEI_DEFAULT_A003);}
	/**
	* 請求書払い申請　入力方式の変更
	* @return 請求書払い申請　入力方式の変更
	*/
	public String zeiHenkouA003(){return getSettingInfo(Key.ZEI_HENKOU_A003);}
	/**
	* 出張旅費精算　消費税額設定
	* @return 出張旅費精算　消費税額設定
	*/
	public String zeinukiZeigakuHenkouA004(){return getSettingInfo(Key.ZEINUKI_ZEIGAKU_HENKOU_A004);}
	/**
	* 通勤定期申請　消費税額設定
	* @return 通勤定期申請　消費税額設定
	*/
	public String zeinukiZeigakuHenkouA006(){return getSettingInfo(Key.ZEINUKI_ZEIGAKU_HENKOU_A006);}
	/**
	* 自動引落伝票　消費税額設定
	* @return 自動引落伝票　消費税額設定
	*/
	public String zeinukiZeigakuHenkouA009(){return getSettingInfo(Key.ZEINUKI_ZEIGAKU_HENKOU_A009);}
	/**
	* 自動引落伝票　入力方式
	* @return 自動引落伝票　入力方式
	*/
	public String zeiDefaultA009(){return getSettingInfo(Key.ZEI_DEFAULT_A009);}
	/**
	* 自動引落伝票　入力方式の変更
	* @return 自動引落伝票　入力方式の変更
	*/
	public String zeiHenkouA009(){return getSettingInfo(Key.ZEI_HENKOU_A009);}
	/**
	* 交通費精算　消費税額設定
	* @return 交通費精算　消費税額設定
	*/
	public String zeinukiZeigakuHenkouA010(){return getSettingInfo(Key.ZEINUKI_ZEIGAKU_HENKOU_A010);}
	/**
	* 海外出張旅費精算　消費税額設定
	* @return 海外出張旅費精算　消費税額設定
	*/
	public String zeinukiZeigakuHenkouA011(){return getSettingInfo(Key.ZEINUKI_ZEIGAKU_HENKOU_A011);}
	/**
	* 支払依頼申請　消費税額設定
	* @return 支払依頼申請　消費税額設定
	*/
	public String zeinukiZeigakuHenkouA013(){return getSettingInfo(Key.ZEINUKI_ZEIGAKU_HENKOU_A013);}
	/**
	* 支払依頼申請　入力方式
	* @return 支払依頼申請　入力方式
	*/
	public String zeiDefaultA013(){return getSettingInfo(Key.ZEI_DEFAULT_A013);}
	/**
	* 支払依頼申請　入力方式の変更
	* @return 支払依頼申請　入力方式の変更
	*/
	public String zeiHenkouA013(){return getSettingInfo(Key.ZEI_HENKOU_A013);}
	/**
	* インボイス対応伝票 設定項目の使用設定
	* @return インボイス対応伝票 設定項目の使用設定
	*/
	public String invoiceSetteiFlg(){return getSettingInfo(Key.INVOICE_SETTEI_FLG);}
	/**
	* 請求書払い申請　事業者区分の変更
	* @return 請求書払い申請　事業者区分の変更
	*/
	public String jigyoushaHenkouFlgA003(){return getSettingInfo(Key.JIGYOUSHA_HENKOU_FLG_A003);}
	/**
	* 自動引落伝票　事業者区分の変更
	* @return 自動引落伝票　事業者区分の変更
	*/
	public String jigyoushaHenkouFlgA009(){return getSettingInfo(Key.JIGYOUSHA_HENKOU_FLG_A009);}
	/**
	* 支払依頼申請　事業者区分の変更
	* @return 支払依頼申請　事業者区分の変更
	*/
	public String jigyoushaHenkouFlgA013(){return getSettingInfo(Key.JIGYOUSHA_HENKOU_FLG_A013);}
	/**
	* インボイス対応後　消費税額端数処理設定
	* @return インボイス対応後　消費税額端数処理設定
	*/
	public String zeigakuHasuuHenkanFlg(){return getSettingInfo(Key.ZEIGAKU_HASUU_HENKAN_FLG);}
	/**
	* e文書を有効にする
	* @return e文書を有効にする
	*/
	public String ebunshoEnableFlg(){return getSettingInfo(Key.EBUNSHO_ENABLE_FLG);}
	/**
	* e文書対象チェックの初期値
	* @return e文書対象チェックの初期値
	*/
	public String ebunshoTaishouCheckDefault(){return getSettingInfo(Key.EBUNSHO_TAISHOU_CHECK_DEFAULT);}
	/**
	* 電子取引のTS付与チェックの初期値
	* @return 電子取引のTS付与チェックの初期値
	*/
	public String ebunshoDenshitsCheckDefault(){return getSettingInfo(Key.EBUNSHO_DENSHITS_CHECK_DEFAULT);}
	/**
	* 経費立替精算
	* @return 経費立替精算
	*/
	public String ebunshoSeiseiA001(){return getSettingInfo(Key.EBUNSHO_SEISEI_A001);}
	/**
	* 書類種別(経費立替精算)
	* @return 書類種別(経費立替精算)
	*/
	public String ebunshoShubetsuA001(){return getSettingInfo(Key.EBUNSHO_SHUBETSU_A001);}
	/**
	* 経費伺い申請（仮払申請）
	* @return 経費伺い申請（仮払申請）
	*/
	public String ebunshoSeiseiA002(){return getSettingInfo(Key.EBUNSHO_SEISEI_A002);}
	/**
	* 書類種別(経費伺い申請（仮払申請）)
	* @return 書類種別(経費伺い申請（仮払申請）)
	*/
	public String ebunshoShubetsuA002(){return getSettingInfo(Key.EBUNSHO_SHUBETSU_A002);}
	/**
	* 請求書払い申請
	* @return 請求書払い申請
	*/
	public String ebunshoSeiseiA003(){return getSettingInfo(Key.EBUNSHO_SEISEI_A003);}
	/**
	* 書類種別(請求書払い申請)
	* @return 書類種別(請求書払い申請)
	*/
	public String ebunshoShubetsuA003(){return getSettingInfo(Key.EBUNSHO_SHUBETSU_A003);}
	/**
	* 出張旅費精算
	* @return 出張旅費精算
	*/
	public String ebunshoSeiseiA004(){return getSettingInfo(Key.EBUNSHO_SEISEI_A004);}
	/**
	* 書類種別(出張旅費精算)
	* @return 書類種別(出張旅費精算)
	*/
	public String ebunshoShubetsuA004(){return getSettingInfo(Key.EBUNSHO_SHUBETSU_A004);}
	/**
	* 出張伺い申請（仮払申請）
	* @return 出張伺い申請（仮払申請）
	*/
	public String ebunshoSeiseiA005(){return getSettingInfo(Key.EBUNSHO_SEISEI_A005);}
	/**
	* 書類種別(出張伺い申請（仮払申請）)
	* @return 書類種別(出張伺い申請（仮払申請）)
	*/
	public String ebunshoShubetsuA005(){return getSettingInfo(Key.EBUNSHO_SHUBETSU_A005);}
	/**
	* 通勤定期申請
	* @return 通勤定期申請
	*/
	public String ebunshoSeiseiA006(){return getSettingInfo(Key.EBUNSHO_SEISEI_A006);}
	/**
	* 書類種別(通勤定期申請)
	* @return 書類種別(通勤定期申請)
	*/
	public String ebunshoShubetsuA006(){return getSettingInfo(Key.EBUNSHO_SHUBETSU_A006);}
	/**
	* 自動引落伝票
	* @return 自動引落伝票
	*/
	public String ebunshoSeiseiA009(){return getSettingInfo(Key.EBUNSHO_SEISEI_A009);}
	/**
	* 書類種別(自動引落伝票)
	* @return 書類種別(自動引落伝票)
	*/
	public String ebunshoShubetsuA009(){return getSettingInfo(Key.EBUNSHO_SHUBETSU_A009);}
	/**
	* 交通費精算
	* @return 交通費精算
	*/
	public String ebunshoSeiseiA010(){return getSettingInfo(Key.EBUNSHO_SEISEI_A010);}
	/**
	* 書類種別(交通費精算)
	* @return 書類種別(交通費精算)
	*/
	public String ebunshoShubetsuA010(){return getSettingInfo(Key.EBUNSHO_SHUBETSU_A010);}
	/**
	* 海外出張旅費精算
	* @return 海外出張旅費精算
	*/
	public String ebunshoSeiseiA011(){return getSettingInfo(Key.EBUNSHO_SEISEI_A011);}
	/**
	* 書類種別(海外出張旅費精算)
	* @return 書類種別(海外出張旅費精算)
	*/
	public String ebunshoShubetsuA011(){return getSettingInfo(Key.EBUNSHO_SHUBETSU_A011);}
	/**
	* 海外出張伺い申請（仮払申請）
	* @return 海外出張伺い申請（仮払申請）
	*/
	public String ebunshoSeiseiA012(){return getSettingInfo(Key.EBUNSHO_SEISEI_A012);}
	/**
	* 書類種別(海外出張伺い申請（仮払申請）)
	* @return 書類種別(海外出張伺い申請（仮払申請）)
	*/
	public String ebunshoShubetsuA012(){return getSettingInfo(Key.EBUNSHO_SHUBETSU_A012);}
	/**
	* 支払依頼申請
	* @return 支払依頼申請
	*/
	public String ebunshoSeiseiA013(){return getSettingInfo(Key.EBUNSHO_SEISEI_A013);}
	/**
	* 書類種別(支払依頼申請)
	* @return 書類種別(支払依頼申請)
	*/
	public String ebunshoShubetsuA013(){return getSettingInfo(Key.EBUNSHO_SHUBETSU_A013);}
	/**
	* ユーザー定義届出
	* @return ユーザー定義届出
	*/
	public String ebunshoSeiseiB000(){return getSettingInfo(Key.EBUNSHO_SEISEI_B000);}
	/**
	* 書類種別(ユーザー定義届出)
	* @return 書類種別(ユーザー定義届出)
	*/
	public String ebunshoShubetsuB000(){return getSettingInfo(Key.EBUNSHO_SHUBETSU_B000);}
	/**
	* e文書変換時に画像を圧縮する
	* @return e文書変換時に画像を圧縮する
	*/
	public String ebunshoCompressFlg(){return getSettingInfo(Key.EBUNSHO_COMPRESS_FLG);}
	/**
	* 経費立替精算　添付ファイルチェック
	* @return 経費立替精算　添付ファイルチェック
	*/
	public String tenpuCheckA001(){return getSettingInfo(Key.TENPU_CHECK_A001);}
	/**
	* 経費立替精算　領収書・請求書等デフォルト値
	* @return 経費立替精算　領収書・請求書等デフォルト値
	*/
	public String shouhyouShoruiDefaultA001(){return getSettingInfo(Key.SHOUHYOU_SHORUI_DEFAULT_A001);}
	/**
	* 出張旅費精算　添付ファイルチェック
	* @return 出張旅費精算　添付ファイルチェック
	*/
	public String tenpuCheckA004(){return getSettingInfo(Key.TENPU_CHECK_A004);}
	/**
	* 出張旅費精算　領収書・請求書等デフォルト値
	* @return 出張旅費精算　領収書・請求書等デフォルト値
	*/
	public String shouhyouShoruiDefaultA004(){return getSettingInfo(Key.SHOUHYOU_SHORUI_DEFAULT_A004);}
	/**
	* 海外出張旅費精算　添付ファイルチェック
	* @return 海外出張旅費精算　添付ファイルチェック
	*/
	public String tenpuCheckA011(){return getSettingInfo(Key.TENPU_CHECK_A011);}
	/**
	* 海外出張旅費精算　領収書・請求書等デフォルト値
	* @return 海外出張旅費精算　領収書・請求書等デフォルト値
	*/
	public String shouhyouShoruiDefaultA011(){return getSettingInfo(Key.SHOUHYOU_SHORUI_DEFAULT_A011);}
	/**
	* 交通費精算　添付ファイルチェック
	* @return 交通費精算　添付ファイルチェック
	*/
	public String tenpuCheckA010(){return getSettingInfo(Key.TENPU_CHECK_A010);}
	/**
	* 交通費精算　領収書・請求書等デフォルト値
	* @return 交通費精算　領収書・請求書等デフォルト値
	*/
	public String shouhyouShoruiDefaultA010(){return getSettingInfo(Key.SHOUHYOU_SHORUI_DEFAULT_A010);}
	/**
	* 請求書払い申請　添付ファイルチェック
	* @return 請求書払い申請　添付ファイルチェック
	*/
	public String tenpuCheckA003(){return getSettingInfo(Key.TENPU_CHECK_A003);}
	/**
	* 請求書払い申請　領収書・請求書等デフォルト値
	* @return 請求書払い申請　領収書・請求書等デフォルト値
	*/
	public String shouhyouShoruiDefaultA003(){return getSettingInfo(Key.SHOUHYOU_SHORUI_DEFAULT_A003);}
	/**
	* 支払依頼申請　添付ファイルチェック
	* @return 支払依頼申請　添付ファイルチェック
	*/
	public String tenpuCheckA013(){return getSettingInfo(Key.TENPU_CHECK_A013);}
	/**
	* 支払依頼申請　領収書・請求書等デフォルト値
	* @return 支払依頼申請　領収書・請求書等デフォルト値
	*/
	public String shouhyouShoruiDefaultA013(){return getSettingInfo(Key.SHOUHYOU_SHORUI_DEFAULT_A013);}
	/**
	* 自動引落伝票　添付ファイルチェック
	* @return 自動引落伝票　添付ファイルチェック
	*/
	public String tenpuCheckA009(){return getSettingInfo(Key.TENPU_CHECK_A009);}
	/**
	* 自動引落伝票　領収書・請求書等デフォルト値
	* @return 自動引落伝票　領収書・請求書等デフォルト値
	*/
	public String shouhyouShoruiDefaultA009(){return getSettingInfo(Key.SHOUHYOU_SHORUI_DEFAULT_A009);}
	/**
	* 添付伝票に仮払案件を自動的に追加
	* @return 添付伝票に仮払案件を自動的に追加
	*/
	public String tenpuDenpyouJidouFlg(){return getSettingInfo(Key.TENPU_DENPYOU_JIDOU_FLG);}
	/**
	* 差引項目名称
	* @return 差引項目名称
	*/
	public String sashihikiName(){return getSettingInfo(Key.SASHIHIKI_NAME);}
	/**
	* 差引単価
	* @return 差引単価
	*/
	public String sashihikiTanka(){return getSettingInfo(Key.SASHIHIKI_TANKA);}
	/**
	* 差引項目名称（海外）
	* @return 差引項目名称（海外）
	*/
	public String sashihikiNameKaigai(){return getSettingInfo(Key.SASHIHIKI_NAME_KAIGAI);}
	/**
	* 差引単価（海外）邦貨
	* @return 差引単価（海外）邦貨
	*/
	public String sashihikiTankaKaigai(){return getSettingInfo(Key.SASHIHIKI_TANKA_KAIGAI);}
	/**
	* 差引単価（海外）外貨
	* @return 差引単価（海外）外貨
	*/
	public String sashihikiTankaKaigaiGaikaHeishu(){return getSettingInfo(Key.SASHIHIKI_TANKA_KAIGAI_GAIKA_HEISHU);}
	/**
	* 差引単価（海外）外貨
	* @return 差引単価（海外）外貨
	*/
	public String sashihikiTankaKaigaiGaika(){return getSettingInfo(Key.SASHIHIKI_TANKA_KAIGAI_GAIKA);}
	/**
	* 申請期間チェック日数
	* @return 申請期間チェック日数
	*/
	public String keihiShinseiKikanCheckNissuu(){return getSettingInfo(Key.KEIHI_SHINSEI_KIKAN_CHECK_NISSUU);}
	/**
	* 申請期間チェック文言
	* @return 申請期間チェック文言
	*/
	public String keihiShinseiKikanCheckMongon(){return getSettingInfo(Key.KEIHI_SHINSEI_KIKAN_CHECK_MONGON);}
	/**
	* 仮払選択制御
	* @return 仮払選択制御
	*/
	public String keihiKaribaraiSentakuSeigyo(){return getSettingInfo(Key.KEIHI_KARIBARAI_SENTAKU_SEIGYO);}
	/**
	* 申請期間チェック日数
	* @return 申請期間チェック日数
	*/
	public String shinseiKikanCheckNissuu(){return getSettingInfo(Key.SHINSEI_KIKAN_CHECK_NISSUU);}
	/**
	* 申請期間チェック文言
	* @return 申請期間チェック文言
	*/
	public String shinseiKikanCheckMongon(){return getSettingInfo(Key.SHINSEI_KIKAN_CHECK_MONGON);}
	/**
	* 仮払選択制御
	* @return 仮払選択制御
	*/
	public String karibaraiSentakuSeigyo(){return getSettingInfo(Key.KARIBARAI_SENTAKU_SEIGYO);}
	/**
	* 申請期間チェック日数
	* @return 申請期間チェック日数
	*/
	public String kaigaiShinseiKikanCheckNissuu(){return getSettingInfo(Key.KAIGAI_SHINSEI_KIKAN_CHECK_NISSUU);}
	/**
	* 申請期間チェック文言
	* @return 申請期間チェック文言
	*/
	public String kaigaiShinseiKikanCheckMongon(){return getSettingInfo(Key.KAIGAI_SHINSEI_KIKAN_CHECK_MONGON);}
	/**
	* 仮払選択制御
	* @return 仮払選択制御
	*/
	public String kaigaiKaribaraiSentakuSeigyo(){return getSettingInfo(Key.KAIGAI_KARIBARAI_SENTAKU_SEIGYO);}
	/**
	* 予算チェック:判定コメント・予算内
	* @return 予算チェック:判定コメント・予算内
	*/
	public String yosanCommentYosannai(){return getSettingInfo(Key.YOSAN_COMMENT_YOSANNAI);}
	/**
	* 予算チェック:判定コメント・超過
	* @return 予算チェック:判定コメント・超過
	*/
	public String yosanCommentChouka(){return getSettingInfo(Key.YOSAN_COMMENT_CHOUKA);}
	/**
	* 予算チェック:超過判定％
	* @return 予算チェック:超過判定％
	*/
	public String yosanChoukaHantei(){return getSettingInfo(Key.YOSAN_CHOUKA_HANTEI);}
	/**
	* 予算チェック:予算番号
	* @return 予算チェック:予算番号
	*/
	public String yosanYosanNo(){return getSettingInfo(Key.YOSAN_YOSAN_NO);}
	/**
	* 起案チェック:判定コメント・予算内
	* @return 起案チェック:判定コメント・予算内
	*/
	public String kianCommentYosannai(){return getSettingInfo(Key.KIAN_COMMENT_YOSANNAI);}
	/**
	* 起案チェック:判定コメント・超過
	* @return 起案チェック:判定コメント・超過
	*/
	public String kianCommentChouka(){return getSettingInfo(Key.KIAN_COMMENT_CHOUKA);}
	/**
	* 起案チェック:超過判定％
	* @return 起案チェック:超過判定％
	*/
	public String kianChoukaHantei(){return getSettingInfo(Key.KIAN_CHOUKA_HANTEI);}
	/**
	* 起案番号のHF転記先①略号
	* @return 起案番号のHF転記先①略号
	*/
	public String kianNoHaneiRyakugou(){return getSettingInfo(Key.KIAN_NO_HANEI_RYAKUGOU);}
	/**
	* 起案番号のHF転記先②番号
	* @return 起案番号のHF転記先②番号
	*/
	public String kianNoHaneiBangou(){return getSettingInfo(Key.KIAN_NO_HANEI_BANGOU);}
	/**
	* セキュリティパターン
	* @return セキュリティパターン
	*/
	public String yosanSecurityPattern(){return getSettingInfo(Key.YOSAN_SECURITY_PATTERN);}
	/**
	* 合議部署の予算確認設定
	* @return 合議部署の予算確認設定
	*/
	public String gougiYosanKakunin(){return getSettingInfo(Key.GOUGI_YOSAN_KAKUNIN);}
	/**
	* 予算チェック粒度
	* @return 予算チェック粒度
	*/
	public String yosanCheckTani(){return getSettingInfo(Key.YOSAN_CHECK_TANI);}
	/**
	* 予算チェック期間
	* @return 予算チェック期間
	*/
	public String yosanCheckKikan(){return getSettingInfo(Key.YOSAN_CHECK_KIKAN);}
	/**
	* 予算・起案チェックコメント表示制御
	* @return 予算・起案チェックコメント表示制御
	*/
	public String yosanKianCommnent(){return getSettingInfo(Key.YOSAN_KIAN_COMMNENT);}
	/**
	* 控除項目　項目名称
	* @return 控除項目　項目名称
	*/
	public String manekinName(){return getSettingInfo(Key.MANEKIN_NAME);}
	/**
	* 控除項目　勘定科目コード
	* @return 控除項目　勘定科目コード
	*/
	public String manekinCd(){return getSettingInfo(Key.MANEKIN_CD);}
	/**
	* 控除項目　勘定科目枝番コード
	* @return 控除項目　勘定科目枝番コード
	*/
	public String manekinEdaban(){return getSettingInfo(Key.MANEKIN_EDABAN);}
	/**
	* 控除項目　負担部門コード
	* @return 控除項目　負担部門コード
	*/
	public String manekinBumon(){return getSettingInfo(Key.MANEKIN_BUMON);}
	/**
	* 一見先　取引先コード
	* @return 一見先　取引先コード
	*/
	public String ichigenCd(){return getSettingInfo(Key.ICHIGEN_CD);}
	/**
	* 一見先　振込手数料
	* @return 一見先　振込手数料
	*/
	public String ichigenTesuuryou(){return getSettingInfo(Key.ICHIGEN_TESUURYOU);}
	/**
	* 一見先　振込手数料勘定科目コード
	* @return 一見先　振込手数料勘定科目コード
	*/
	public String ichigenTesuuryouKamokuCd(){return getSettingInfo(Key.ICHIGEN_TESUURYOU_KAMOKU_CD);}
	/**
	* 一見先　振込手数料勘定科目枝番コード
	* @return 一見先　振込手数料勘定科目枝番コード
	*/
	public String ichigenTesuuryouEdabanCd(){return getSettingInfo(Key.ICHIGEN_TESUURYOU_EDABAN_CD);}
	/**
	* 一見先　振込手数料負担部門コード
	* @return 一見先　振込手数料負担部門コード
	*/
	public String ichigenTesuuryouBumonCd(){return getSettingInfo(Key.ICHIGEN_TESUURYOU_BUMON_CD);}
	/**
	* 一見先　振込手数料負担部門コードの任意部門の判断基準
	* @return 一見先　振込手数料負担部門コードの任意部門の判断基準
	*/
	public String ichigenTesuuryouBumonCdNiniBumonHantei(){return getSettingInfo(Key.ICHIGEN_TESUURYOU_BUMON_CD_NINI_BUMON_HANTEI);}
	/**
	* 未払い不定期　貸方科目コード
	* @return 未払い不定期　貸方科目コード
	*/
	public String kashikatakamokuCdMibaraiFuteiki(){return getSettingInfo(Key.KASHIKATAKAMOKU_CD_MIBARAI_FUTEIKI);}
	/**
	* 未払い定期　　貸方科目コード
	* @return 未払い定期　　貸方科目コード
	*/
	public String kashikatakamokuCdMibaraiTeiki(){return getSettingInfo(Key.KASHIKATAKAMOKU_CD_MIBARAI_TEIKI);}
	/**
	* 設備未払い　　貸方科目コード
	* @return 設備未払い　　貸方科目コード
	*/
	public String kashikatakamokuCdSetsubiMibarai(){return getSettingInfo(Key.KASHIKATAKAMOKU_CD_SETSUBI_MIBARAI);}
	/**
	* 部門コード桁数
	* @return 部門コード桁数
	*/
	public String bumonCdKetasuu(){return getSettingInfo(Key.BUMON_CD_KETASUU);}
	/**
	* １ページ表示件数
	* @return １ページ表示件数
	*/
	public String recordNumPerPage(){return getSettingInfo(Key.RECORD_NUM_PER_PAGE);}
	/**
	* １ページ表示件数(取引先選択)
	* @return １ページ表示件数(取引先選択)
	*/
	public String recordNumPerPageTorihikisaki(){return getSettingInfo(Key.RECORD_NUM_PER_PAGE_TORIHIKISAKI);}
	/**
	* ダウンロードファイル件数
	* @return ダウンロードファイル件数
	*/
	public String fileNumForDownload(){return getSettingInfo(Key.FILE_NUM_FOR_DOWNLOAD);}
	/**
	* データ保存日数(WF一般)
	* @return データ保存日数(WF一般)
	*/
	public String dataHozonNissuu(){return getSettingInfo(Key.DATA_HOZON_NISSUU);}
	/**
	* データ保存日数(ログ)
	* @return データ保存日数(ログ)
	*/
	public String dataHozonNissuuLog(){return getSettingInfo(Key.DATA_HOZON_NISSUU_LOG);}
	/**
	* 金融機関コード(郵貯)
	* @return 金融機関コード(郵貯)
	*/
	public String kinyukikanCdYuucho(){return getSettingInfo(Key.KINYUKIKAN_CD_YUUCHO);}
	/**
	* DBバックアップファイルパス
	* @return DBバックアップファイルパス
	*/
	public String dbdumpUrl(){return getSettingInfo(Key.DBDUMP_URL);}
	/**
	* テナント最大ユーザー数
	* @return テナント最大ユーザー数
	*/
	public String tenantMaxUsersNum(){return getSettingInfo(Key.TENANT_MAX_USERS_NUM);}
	/**
	* 法人名
	* @return 法人名
	*/
	public String hyoujiKaishaNum(){return getSettingInfo(Key.HYOUJI_KAISHA_NUM);}
	/**
	* 取引コードの設定可否
	* @return 取引コードの設定可否
	*/
	public String torihikiSettei(){return getSettingInfo(Key.TORIHIKI_SETTEI);}
	/**
	* ユーザー情報の変更可否
	* @return ユーザー情報の変更可否
	*/
	public String userInfoHenkou(){return getSettingInfo(Key.USER_INFO_HENKOU);}
	/**
	* 邦貨換算端数処理方法
	* @return 邦貨換算端数処理方法
	*/
	public String houkaKansanHasuu(){return getSettingInfo(Key.HOUKA_KANSAN_HASUU);}
	/**
	* 負担部門選択時に集計部門による制限を行う
	* @return 負担部門選択時に集計部門による制限を行う
	*/
	public String futanBumonShukeiFilter(){return getSettingInfo(Key.FUTAN_BUMON_SHUKEI_FILTER);}
	/**
	* 添付ファイル容量
	* @return 添付ファイル容量
	*/
	public String tenpuSizeSeigen(){return getSettingInfo(Key.TENPU_SIZE_SEIGEN);}
	/**
	* 経理処理ロールのマル秘参照制限
	* @return 経理処理ロールのマル秘参照制限
	*/
	public String keiriMaruhiRef(){return getSettingInfo(Key.KEIRI_MARUHI_REF);}
	/**
	* 申請・承認時のみマル秘設定可能とする
	* @return 申請・承認時のみマル秘設定可能とする
	*/
	public String maruhiSetteiShouninSeigen(){return getSettingInfo(Key.MARUHI_SETTEI_SHOUNIN_SEIGEN);}
	/**
	* 合議部署の最終決裁後の閲覧制限設定
	* @return 合議部署の最終決裁後の閲覧制限設定
	*/
	public String etsuranSeigenGougi(){return getSettingInfo(Key.ETSURAN_SEIGEN_GOUGI);}
	/**
	* 閲覧者の最終決裁後の閲覧制限設定
	* @return 閲覧者の最終決裁後の閲覧制限設定
	*/
	public String etsuranSeigenEtsuransha(){return getSettingInfo(Key.ETSURAN_SEIGEN_ETSURANSHA);}
	/**
	* 通勤定期申請で仕訳作成しない場合に保持する定期情報の有効期限
	* @return 通勤定期申請で仕訳作成しない場合に保持する定期情報の有効期限
	*/
	public String teikiJouhouYuukouKigen(){return getSettingInfo(Key.TEIKI_JOUHOU_YUUKOU_KIGEN);}
	/**
	* 稟議金額の超過判定%
	* @return 稟議金額の超過判定%
	*/
	public String ringiChoukaHantei(){return getSettingInfo(Key.RINGI_CHOUKA_HANTEI);}
	/**
	* 稟議金額を超過した伝票の承認等可否
	* @return 稟議金額を超過した伝票の承認等可否
	*/
	public String ringiChoukaShounintou(){return getSettingInfo(Key.RINGI_CHOUKA_SHOUNINTOU);}
	/**
	* ユーザー定義届書伝票検索条件名称（件名）
	* @return ユーザー定義届書伝票検索条件名称（件名）
	*/
	public String userTeigiTodokeKensakuKenmei(){return getSettingInfo(Key.USER_TEIGI_TODOKE_KENSAKU_KENMEI);}
	/**
	* ユーザー定義届書伝票検索条件名称（内容）
	* @return ユーザー定義届書伝票検索条件名称（内容）
	*/
	public String userTeigiTodokeKensakuNaiyou(){return getSettingInfo(Key.USER_TEIGI_TODOKE_KENSAKU_NAIYOU);}
	/**
	* 所属部門＆配下部門のデータ共有する・しない
	* @return 所属部門＆配下部門のデータ共有する・しない
	*/
	public String shozokuBumonDataKyoyu(){return getSettingInfo(Key.SHOZOKU_BUMON_DATA_KYOYU);}
	/**
	* 複数明細がある場合のルート判断基準
	* @return 複数明細がある場合のルート判断基準
	*/
	public String routeHanteiTorihikiSentakuRule(){return getSettingInfo(Key.ROUTE_HANTEI_TORIHIKI_SENTAKU_RULE);}
	/**
	* 海外用日当の単価設定
	* @return 海外用日当の単価設定
	*/
	public String kaigaiNittouTankaGaikaSettei(){return getSettingInfo(Key.KAIGAI_NITTOU_TANKA_GAIKA_SETTEI);}
	/**
	* ログイン状態を維持する時間
	* @return ログイン状態を維持する時間
	*/
	public String loginIjiJikan(){return getSettingInfo(Key.LOGIN_IJI_JIKAN);}
	/**
	* ユーザー登録の代表負担部門の未入力チェック
	* @return ユーザー登録の代表負担部門の未入力チェック
	*/
	public String userDaihyoufutanbumonMinyuuryokuCheck(){return getSettingInfo(Key.USER_DAIHYOUFUTANBUMON_MINYUURYOKU_CHECK);}
	/**
	* 最終承認の解除
	* @return 最終承認の解除
	*/
	public String saishuushouninKaijoFlg(){return getSettingInfo(Key.SAISHUUSHOUNIN_KAIJO_FLG);}
	/**
	* 伝票変更権限
	* @return 伝票変更権限
	*/
	public String denpyouHenkouFlg(){return getSettingInfo(Key.DENPYOU_HENKOU_FLG);}
	/**
	* 伝票検索でのマル秘伝票表示制御
	* @return 伝票検索でのマル秘伝票表示制御
	*/
	public String maruhiHyoujiSeigyoFlg(){return getSettingInfo(Key.MARUHI_HYOUJI_SEIGYO_FLG);}
	/**
	* 予算オプション
	* @return 予算オプション
	*/
	public String yosanOption(){return getSettingInfo(Key.YOSAN_OPTION);}
	/**
	* WF一般オプション
	* @return WF一般オプション
	*/
	public String ippanOption(){return getSettingInfo(Key.IPPAN_OPTION);}
	/**
	* 拠点入力オプション
	* @return 拠点入力オプション
	*/
	public String kyotenOption(){return getSettingInfo(Key.KYOTEN_OPTION);}
	/**
	* 会社コード
	* @return 会社コード
	*/
	public String op21MparamKaishaCdKyoten(){return getSettingInfo(Key.OP21MPARAM_KAISHA_CD_KYOTEN);}
	/**
	* 諸口コード
	* @return 諸口コード
	*/
	public String shokuchiCdKyoten(){return getSettingInfo(Key.SHOKUCHI_CD_KYOTEN);}
	/**
	* 伝票形式(拠点入力：振替伝票)
	* @return 伝票形式(拠点入力：振替伝票)
	*/
	public String op21MparamDenpyouKeishikiZ001(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_Z001);}
	/**
	* 伝票形式(拠点入力：現預金出納帳)
	* @return 伝票形式(拠点入力：現預金出納帳)
	*/
	public String op21MparamDenpyouKeishikiZ002(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_Z002);}
	/**
	* 伝票入力パターン(拠点入力：振替伝票)
	* @return 伝票入力パターン(拠点入力：振替伝票)
	*/
	public String op21MparamDenpyouNyuuryokuPatternZ001(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_Z001);}
	/**
	* 伝票入力パターン(拠点入力：現預金出納帳)
	* @return 伝票入力パターン(拠点入力：現預金出納帳)
	*/
	public String op21MparamDenpyouNyuuryokuPatternZ002(){return getSettingInfo(Key.OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_Z002);}
	/**
	* 起動ユーザー
	* @return 起動ユーザー
	*/
	public String op21MparamKidouUserKyoten(){return getSettingInfo(Key.OP21MPARAM_KIDOU_USER_KYOTEN);}
	/**
	* 仕訳区分
	* @return 仕訳区分
	*/
	public String shiwakeKbnKyoten(){return getSettingInfo(Key.SHIWAKE_KBN_KYOTEN);}
	/**
	* 確定有無
	* @return 確定有無
	*/
	public String kakuteiUmuKyoten(){return getSettingInfo(Key.KAKUTEI_UMU_KYOTEN);}
	/**
	* 伝票IDの連携先
	* @return 伝票IDの連携先
	*/
	public String uf1DenpyouIdHaneiKyoten(){return getSettingInfo(Key.UF1_DENPYOU_ID_HANEI_KYOTEN);}
	/**
	* 社員コードの連携先
	* @return 社員コードの連携先
	*/
	public String shainCdRenkeiKyoten(){return getSettingInfo(Key.SHAIN_CD_RENKEI_KYOTEN);}
	/**
	* ヘッダーフィールドマッピング①
	* @return ヘッダーフィールドマッピング①
	*/
	public String hf1MappingKyoten(){return getSettingInfo(Key.HF1_MAPPING_KYOTEN);}
	/**
	* ヘッダーフィールドマッピング②
	* @return ヘッダーフィールドマッピング②
	*/
	public String hf2MappingKyoten(){return getSettingInfo(Key.HF2_MAPPING_KYOTEN);}
	/**
	* ヘッダーフィールドマッピング③
	* @return ヘッダーフィールドマッピング③
	*/
	public String hf3MappingKyoten(){return getSettingInfo(Key.HF3_MAPPING_KYOTEN);}
	/**
	* ヘッダーフィールドマッピング④
	* @return ヘッダーフィールドマッピング④
	*/
	public String hf4MappingKyoten(){return getSettingInfo(Key.HF4_MAPPING_KYOTEN);}
	/**
	* ヘッダーフィールドマッピング⑤
	* @return ヘッダーフィールドマッピング⑤
	*/
	public String hf5MappingKyoten(){return getSettingInfo(Key.HF5_MAPPING_KYOTEN);}
	/**
	* ヘッダーフィールドマッピング⑥
	* @return ヘッダーフィールドマッピング⑥
	*/
	public String hf6MappingKyoten(){return getSettingInfo(Key.HF6_MAPPING_KYOTEN);}
	/**
	* ヘッダーフィールドマッピング⑦
	* @return ヘッダーフィールドマッピング⑦
	*/
	public String hf7MappingKyoten(){return getSettingInfo(Key.HF7_MAPPING_KYOTEN);}
	/**
	* ヘッダーフィールドマッピング⑧
	* @return ヘッダーフィールドマッピング⑧
	*/
	public String hf8MappingKyoten(){return getSettingInfo(Key.HF8_MAPPING_KYOTEN);}
	/**
	* ヘッダーフィールドマッピング⑨
	* @return ヘッダーフィールドマッピング⑨
	*/
	public String hf9MappingKyoten(){return getSettingInfo(Key.HF9_MAPPING_KYOTEN);}
	/**
	* ヘッダーフィールドマッピング⑩
	* @return ヘッダーフィールドマッピング⑩
	*/
	public String hf10MappingKyoten(){return getSettingInfo(Key.HF10_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング①
	* @return ユニバーサルフィールドマッピング①
	*/
	public String uf1MappingKyoten(){return getSettingInfo(Key.UF1_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング②
	* @return ユニバーサルフィールドマッピング②
	*/
	public String uf2MappingKyoten(){return getSettingInfo(Key.UF2_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング③
	* @return ユニバーサルフィールドマッピング③
	*/
	public String uf3MappingKyoten(){return getSettingInfo(Key.UF3_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング④
	* @return ユニバーサルフィールドマッピング④
	*/
	public String uf4MappingKyoten(){return getSettingInfo(Key.UF4_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング⑤
	* @return ユニバーサルフィールドマッピング⑤
	*/
	public String uf5MappingKyoten(){return getSettingInfo(Key.UF5_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング⑥
	* @return ユニバーサルフィールドマッピング⑥
	*/
	public String uf6MappingKyoten(){return getSettingInfo(Key.UF6_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング⑦
	* @return ユニバーサルフィールドマッピング⑦
	*/
	public String uf7MappingKyoten(){return getSettingInfo(Key.UF7_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング⑧
	* @return ユニバーサルフィールドマッピング⑧
	*/
	public String uf8MappingKyoten(){return getSettingInfo(Key.UF8_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング⑨
	* @return ユニバーサルフィールドマッピング⑨
	*/
	public String uf9MappingKyoten(){return getSettingInfo(Key.UF9_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング⑩
	* @return ユニバーサルフィールドマッピング⑩
	*/
	public String uf10MappingKyoten(){return getSettingInfo(Key.UF10_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング(固定値)①
	* @return ユニバーサルフィールドマッピング(固定値)①
	*/
	public String ufKotei1MappingKyoten(){return getSettingInfo(Key.UF_KOTEI1_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング(固定値)②
	* @return ユニバーサルフィールドマッピング(固定値)②
	*/
	public String ufKotei2MappingKyoten(){return getSettingInfo(Key.UF_KOTEI2_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング(固定値)③
	* @return ユニバーサルフィールドマッピング(固定値)③
	*/
	public String ufKotei3MappingKyoten(){return getSettingInfo(Key.UF_KOTEI3_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング(固定値)④
	* @return ユニバーサルフィールドマッピング(固定値)④
	*/
	public String ufKotei4MappingKyoten(){return getSettingInfo(Key.UF_KOTEI4_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング(固定値)⑤
	* @return ユニバーサルフィールドマッピング(固定値)⑤
	*/
	public String ufKotei5MappingKyoten(){return getSettingInfo(Key.UF_KOTEI5_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング(固定値)⑥
	* @return ユニバーサルフィールドマッピング(固定値)⑥
	*/
	public String ufKotei6MappingKyoten(){return getSettingInfo(Key.UF_KOTEI6_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング(固定値)⑦
	* @return ユニバーサルフィールドマッピング(固定値)⑦
	*/
	public String ufKotei7MappingKyoten(){return getSettingInfo(Key.UF_KOTEI7_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング(固定値)⑧
	* @return ユニバーサルフィールドマッピング(固定値)⑧
	*/
	public String ufKotei8MappingKyoten(){return getSettingInfo(Key.UF_KOTEI8_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング(固定値)⑨
	* @return ユニバーサルフィールドマッピング(固定値)⑨
	*/
	public String ufKotei9MappingKyoten(){return getSettingInfo(Key.UF_KOTEI9_MAPPING_KYOTEN);}
	/**
	* ユニバーサルフィールドマッピング(固定値)⑩
	* @return ユニバーサルフィールドマッピング(固定値)⑩
	*/
	public String ufKotei10MappingKyoten(){return getSettingInfo(Key.UF_KOTEI10_MAPPING_KYOTEN);}
	/**
	* 添付ファイル連携
	* @return 添付ファイル連携
	*/
	public String tenpuFileRenkeiKyoten(){return getSettingInfo(Key.TENPU_FILE_RENKEI_KYOTEN);}
	/**
	* 仕訳作成エラー発生時の進め方
	* @return 仕訳作成エラー発生時の進め方
	*/
	public String shiwakeSakuseiErrorActionKyoten(){return getSettingInfo(Key.SHIWAKE_SAKUSEI_ERROR_ACTION_KYOTEN);}
	/**
	* 先日付の仕訳連携
	* @return 先日付の仕訳連携
	*/
	public String shiwakeSakuseiSakihidukeKyoten(){return getSettingInfo(Key.SHIWAKE_SAKUSEI_SAKIHIDUKE_KYOTEN);}
	/**
	* 振替伝票開始番号(伝票番号)
	* @return 振替伝票開始番号(伝票番号)
	*/
	public String bumonFurikaeDenpyouSerialNoStart(){return getSettingInfo(Key.BUMON_FURIKAE_DENPYOU_SERIAL_NO_START);}
	/**
	* 振替伝票終了番号(伝票番号)
	* @return 振替伝票終了番号(伝票番号)
	*/
	public String bumonFurikaeDenpyouSerialNoEnd(){return getSettingInfo(Key.BUMON_FURIKAE_DENPYOU_SERIAL_NO_END);}
	/**
	* 現預金出納帳開始番号(伝票番号)
	* @return 現預金出納帳開始番号(伝票番号)
	*/
	public String suitouchouDenpyouSerialNoStart(){return getSettingInfo(Key.SUITOUCHOU_DENPYOU_SERIAL_NO_START);}
	/**
	* 現預金出納帳終了番号(伝票番号)
	* @return 現預金出納帳終了番号(伝票番号)
	*/
	public String suitouchouDenpyouSerialNoEnd(){return getSettingInfo(Key.SUITOUCHOU_DENPYOU_SERIAL_NO_END);}
	/**
	* 現預金出納帳　消費税額設定
	* @return 現預金出納帳　消費税額設定
	*/
	public String zeinukiZeigakuHenkouZ002(){return getSettingInfo(Key.ZEINUKI_ZEIGAKU_HENKOU_Z002);}
	/**
	* 振替伝票(拠点入力)
	* @return 振替伝票(拠点入力)
	*/
	public String ebunshoSeiseiZ001(){return getSettingInfo(Key.EBUNSHO_SEISEI_Z001);}
	/**
	* 書類種別(振替伝票(拠点入力))
	* @return 書類種別(振替伝票(拠点入力))
	*/
	public String ebunshoShubetsuZ001(){return getSettingInfo(Key.EBUNSHO_SHUBETSU_Z001);}
	/**
	* 現預金出納帳
	* @return 現預金出納帳
	*/
	public String ebunshoSeiseiZ002(){return getSettingInfo(Key.EBUNSHO_SEISEI_Z002);}
	/**
	* 書類種別(現預金出納帳)
	* @return 書類種別(現預金出納帳)
	*/
	public String ebunshoShubetsuZ002(){return getSettingInfo(Key.EBUNSHO_SHUBETSU_Z002);}
	/**
	* データ保存日数(拠点入力)
	* @return データ保存日数(拠点入力)
	*/
	public String dataHozonNissuuKyoten(){return getSettingInfo(Key.DATA_HOZON_NISSUU_KYOTEN);}




	/**
	 * 設定情報テーブルの設定名
	 */
	public class Key {
		/** アプリURL */
		public static final String SHOTODOKE_URL = "appl_url";
		/** メール通知システム名 */
		public static final String MAIL_TSUUCHI_SYSTEM_NAME = "mail_tsuuchi_system_name";
		/** パスワードの最小文字数 */
		public static final String PASS_LEN_MIN = "password_len_min";
		/** パスワードの最小文字種類 */
		public static final String PASS_KIND_MIN = "password_kind_min";
		/** パスワードの有効期間(日数) */
		public static final String PASS_VALID_TERM = "password_valid_term";
		/** アカウント一時ロックするパスワード誤り回数 */
		public static final String PASS_LOCK_NUM = "password_lock_num";
		/** アカウント一時ロック時間(分) */
		public static final String PASS_LOCK_TERM = "password_lock_term";
		/** 初期パスワードの変更を必須とする */
		public static final String REQUIRE_PASS_CHANGE = "require_password_change";
		/** 連携フラグ */
		public static final String INTRA_FLG = "intra_flg";
		/** イントラ版起動URL */
		public static final String EKISPERT_INTRA_ACTION_URL = "intra_action";
		/** Webサービス駅名検索URL */
		public static final String EKISPERT_WEB_URL_STATION = "ekispert_web_url_station";
		/** Webサービス経路検索URL */
		public static final String EKISPERT_WEB_URL_ROUTE = "ekispert_web_url_route";
		/** Webサービス路線図一覧検索URL */
		public static final String EKISPERT_WEB_URL_MAP_LIST = "ekispert_web_url_map_list";
		/** Webサービス路線図駅名検索URL */
		public static final String EKISPERT_WEB_URL_MAP_STATON = "ekispert_web_url_map_station";
		/** Webサービス路線図取得URL */
		public static final String EKISPERT_WEB_URL_MAP_IMAGE = "ekispert_web_url_map_image";
		/** Webサービス探索条件生成URL */
		public static final String EKISPERT_WEB_SEARCH = "ekispert_web_search";
		/** IC使用区分 */
		public static final String EKISPERT_IC_KBN = "ekispert_ic_kbn";
		/** IC乗車券区分の全社指定 */
		public static final String EKISPERT_IC_ZENSHASHITEI = "ekispert_ic_zenshashitei";
		/** Webサービス探索優先度 */
		public static final String EKISPERT_WEB_IC_PREFERRED = "ekispert_web_ic_preferred";
		/** 定期区間の反映先 */
		public static final String EKISPERT_TEIKIKUKAN_HANEISAKI = "ekispert_teikikukan_haneisaki";
		/** 経路表示方法 */
		public static final String EKISPERT_KEIRO_KANIHYOUJI = "ekispert_keiro_kanihyouji";
		/** 距離情報の反映先 */
		public static final String EKISPERT_KYORI_HANEI = "ekispert_kyori_hanei";
		/** 割引情報の反映先(EX予約/スマートEX/新幹線eチケット) */
		public static final String EKISPERT_WARIBIKI_HANEI = "ekispert_waribiki_hanei";
		/** 往復割引運賃適応の反映先 */
		public static final String EKISPERT_OUFUKUWARI_HANEI = "ekispert_oufukuwari_hanei";
		/** 法人カード利用明細の日付変更 */
		public static final String HOUJIN_CARD_DATE_ENABLED = "houjin_card_date_enabled";
		/** 法人カード履歴の除外 */
		public static final String HOUJIN_CARD_RIREKI_JYOGAI_ENABLE = "houjin_card_rireki_jyogai_enable";
		/** 法人カード履歴を除外するユーザー */
		public static final String HOUJIN_CARD_RIREKI_JYOGAI_USER = "houjin_card_rireki_jyogai_user";
		/** ICカードデフォルト交通手段 */
		public static final String IC_CARD_TRAIN = "ic_card_train";
		/** ICカード「バス/路面等」交通手段 */
		public static final String IC_CARD_BUS = "ic_card_bus";
		/** ICカード明細の日付変更 */
		public static final String IC_CARD_DATE_ENABLED = "ic_card_date_enabled";
		/** FBデータ出力ファイルパス */
		public static final String OUTPUT_FILE_PATH_FBDATA = "output_file_path_fbdata";
		/** FBデータ出力ファイル名 */
		public static final String OUTPUT_FILE_NM_FBDATA = "output_file_nm_fbdata";
		/** 支払日リスト件数 */
		public static final String LIST_NUM_FOR_SHIHARAIBI = "list_num_for_shiharaibi";
		/** 顧客コード1への社員コード反映 */
		public static final String SHAIN_CD2KOKYAKU_CD1 = "shain_cd2kokyaku_cd1";
		/** 全銀総合振込依頼データ形式 */
		public static final String ZENGIN_FURIKOMI_KEISHIKI = "zengin_furikomi_keishiki";
		/** XMLメッセージ単位の識別番号 */
		public static final String XML_MESSAGE_SHIKIBETSU_NO = "xml_message_shikibetsu_no";
		/** 会社コード */
		public static final String OP21MPARAM_KAISHA_CD = "op21mparam_kaisha_cd";
		/** 諸口コード */
		public static final String SHOKUCHI_CD = "shokuchi_cd";
		/** 処理区分 */
		public static final String OP21MPARAM_SHORI_KBN = "op21mparam_shori_kbn";
		/** 伝票形式 */
		public static final String OP21MPARAM_DENPYOU_KEISHIKI = "op21mparam_denpyou_keishiki";
		/** 伝票形式(経費立替精算) */
		public static final String OP21MPARAM_DENPYOU_KEISHIKI_A001 = "op21mparam_denpyou_keishiki_A001";
		/** 伝票形式(経費伺い申請（仮払申請）) */
		public static final String OP21MPARAM_DENPYOU_KEISHIKI_A002 = "op21mparam_denpyou_keishiki_A002";
		/** 伝票形式(請求書払い申請) */
		public static final String OP21MPARAM_DENPYOU_KEISHIKI_A003 = "op21mparam_denpyou_keishiki_A003";
		/** 伝票形式(支払依頼申請) */
		public static final String OP21MPARAM_DENPYOU_KEISHIKI_A013 = "op21mparam_denpyou_keishiki_A013";
		/** 伝票形式(自動引落伝票) */
		public static final String OP21MPARAM_DENPYOU_KEISHIKI_A009 = "op21mparam_denpyou_keishiki_A009";
		/** 伝票形式(出張旅費精算) */
		public static final String OP21MPARAM_DENPYOU_KEISHIKI_A004 = "op21mparam_denpyou_keishiki_A004";
		/** 伝票形式(出張伺い申請（仮払申請）) */
		public static final String OP21MPARAM_DENPYOU_KEISHIKI_A005 = "op21mparam_denpyou_keishiki_A005";
		/** 伝票形式(海外出張旅費精算) */
		public static final String OP21MPARAM_DENPYOU_KEISHIKI_A011 = "op21mparam_denpyou_keishiki_A011";
		/** 伝票形式(海外出張伺い申請（仮払申請）) */
		public static final String OP21MPARAM_DENPYOU_KEISHIKI_A012 = "op21mparam_denpyou_keishiki_A012";
		/** 伝票形式(交通費精算) */
		public static final String OP21MPARAM_DENPYOU_KEISHIKI_A010 = "op21mparam_denpyou_keishiki_A010";
		/** 伝票形式(通勤定期申請) */
		public static final String OP21MPARAM_DENPYOU_KEISHIKI_A006 = "op21mparam_denpyou_keishiki_A006";
		/** 不良データログ */
		public static final String OP21MPARAM_FURYOU_DATA_LOG = "op21mparam_furyou_data_log";
		/** CSV用ﾊﾟｽ */
		public static final String OP21MPARAM_CSV_PATH = "op21mparam_csv_path";
		/** CSVファイル名 */
		public static final String OP21MPARAM_CSV_FILE_NM = "op21mparam_csv_file_nm";
		/** ログ用パス */
		public static final String OP21MPARAM_LOG_PATH = "op21mparam_log_path";
		/** ログファイル名 */
		public static final String OP21MPARAM_LOG_FILE_NM = "op21mparam_log_file_nm";
		/** レイアウトNo */
		public static final String OP21MPARAM_LAYOUT_NO = "op21mparam_layout_no";
		/** 伝票入力パターン */
		public static final String OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN = "op21mparam_denpyou_nyuuryoku_pattern";
		/** 伝票入力パターン(経費立替精算) */
		public static final String OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A001 = "op21mparam_denpyou_nyuuryoku_pattern_A001";
		/** 伝票入力パターン(経費伺い申請（仮払申請）) */
		public static final String OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A002 = "op21mparam_denpyou_nyuuryoku_pattern_A002";
		/** 伝票入力パターン(請求書払い申請) */
		public static final String OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A003 = "op21mparam_denpyou_nyuuryoku_pattern_A003";
		/** 伝票入力パターン(支払依頼申請) */
		public static final String OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A013 = "op21mparam_denpyou_nyuuryoku_pattern_A013";
		/** 伝票入力パターン(自動引落伝票) */
		public static final String OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A009 = "op21mparam_denpyou_nyuuryoku_pattern_A009";
		/** 伝票入力パターン(出張旅費精算) */
		public static final String OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A004 = "op21mparam_denpyou_nyuuryoku_pattern_A004";
		/** 伝票入力パターン(出張伺い申請（仮払申請）) */
		public static final String OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A005 = "op21mparam_denpyou_nyuuryoku_pattern_A005";
		/** 伝票入力パターン(海外出張旅費精算) */
		public static final String OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A011 = "op21mparam_denpyou_nyuuryoku_pattern_A011";
		/** 伝票入力パターン(海外出張伺い申請（仮払申請）) */
		public static final String OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A012 = "op21mparam_denpyou_nyuuryoku_pattern_A012";
		/** 伝票入力パターン(交通費精算) */
		public static final String OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A010 = "op21mparam_denpyou_nyuuryoku_pattern_A010";
		/** 伝票入力パターン(通勤定期申請) */
		public static final String OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_A006 = "op21mparam_denpyou_nyuuryoku_pattern_A006";
		/** 邦貨換算フラグ */
		public static final String OP21MPARAM_HOUKA_KANZAN_FLG = "op21mparam_houka_kanzan_flg";
		/** 起動ユーザー */
		public static final String OP21MPARAM_KIDOU_USER = "op21mparam_kidou_user";
		/** 仕訳区分 */
		public static final String SHIWAKE_KBN = "shiwake_kbn";
		/** 確定有無 */
		public static final String KAKUTEI_UMU = "kakutei_umu";
		/** 伝票IDの連携先 */
		public static final String UF1_DENPYOU_ID_HANEI = "uf1_denpyou_id_hanei";
		/** 掛けなし　支払結果仕訳作成有無 */
		public static final String KAKE_NASHI_SHIHARAI_SHIWAKE_SAKUSEI = "kake_nashi_shiharai_shiwake_sakusei";
		/** 掛けあり　支払結果仕訳作成有無 */
		public static final String KAKE_ARI_SHIHARAI_SHIWAKE_SAKUSEI = "kake_ari_shiharai_shiwake_sakusei";
		/** 通勤定期　仕訳作成有無 */
		public static final String TSUUKINTEIKI_SHIWAKE_SAKUSEI_UMU = "tsuukinteiki_shiwake_sakusei_umu";
		/** 社員コードの連携先 */
		public static final String SHAIN_CD_RENKEI = "shain_cd_renkei";
		/** 法人カード識別用番号の連携先 */
		public static final String HOUJIN_CARD_RENKEI = "houjin_card_renkei";
		/** ヘッダーフィールドマッピング① */
		public static final String HF1_MAPPING = "hf1_mapping";
		/** ヘッダーフィールドマッピング② */
		public static final String HF2_MAPPING = "hf2_mapping";
		/** ヘッダーフィールドマッピング③ */
		public static final String HF3_MAPPING = "hf3_mapping";
		/** ヘッダーフィールドマッピング④ */
		public static final String HF4_MAPPING = "hf4_mapping";
		/** ヘッダーフィールドマッピング⑤ */
		public static final String HF5_MAPPING = "hf5_mapping";
		/** ヘッダーフィールドマッピング⑥ */
		public static final String HF6_MAPPING = "hf6_mapping";
		/** ヘッダーフィールドマッピング⑦ */
		public static final String HF7_MAPPING = "hf7_mapping";
		/** ヘッダーフィールドマッピング⑧ */
		public static final String HF8_MAPPING = "hf8_mapping";
		/** ヘッダーフィールドマッピング⑨ */
		public static final String HF9_MAPPING = "hf9_mapping";
		/** ヘッダーフィールドマッピング⑩ */
		public static final String HF10_MAPPING = "hf10_mapping";
		/** ユニバーサルフィールドマッピング① */
		public static final String UF1_MAPPING = "uf1_mapping";
		/** ユニバーサルフィールドマッピング② */
		public static final String UF2_MAPPING = "uf2_mapping";
		/** ユニバーサルフィールドマッピング③ */
		public static final String UF3_MAPPING = "uf3_mapping";
		/** ユニバーサルフィールドマッピング④ */
		public static final String UF4_MAPPING = "uf4_mapping";
		/** ユニバーサルフィールドマッピング⑤ */
		public static final String UF5_MAPPING = "uf5_mapping";
		/** ユニバーサルフィールドマッピング⑥ */
		public static final String UF6_MAPPING = "uf6_mapping";
		/** ユニバーサルフィールドマッピング⑦ */
		public static final String UF7_MAPPING = "uf7_mapping";
		/** ユニバーサルフィールドマッピング⑧ */
		public static final String UF8_MAPPING = "uf8_mapping";
		/** ユニバーサルフィールドマッピング⑨ */
		public static final String UF9_MAPPING = "uf9_mapping";
		/** ユニバーサルフィールドマッピング⑩ */
		public static final String UF10_MAPPING = "uf10_mapping";
		/** ユニバーサルフィールドマッピング(固定値)① */
		public static final String UF_KOTEI1_MAPPING = "uf_kotei1_mapping";
		/** ユニバーサルフィールドマッピング(固定値)② */
		public static final String UF_KOTEI2_MAPPING = "uf_kotei2_mapping";
		/** ユニバーサルフィールドマッピング(固定値)③ */
		public static final String UF_KOTEI3_MAPPING = "uf_kotei3_mapping";
		/** ユニバーサルフィールドマッピング(固定値)④ */
		public static final String UF_KOTEI4_MAPPING = "uf_kotei4_mapping";
		/** ユニバーサルフィールドマッピング(固定値)⑤ */
		public static final String UF_KOTEI5_MAPPING = "uf_kotei5_mapping";
		/** ユニバーサルフィールドマッピング(固定値)⑥ */
		public static final String UF_KOTEI6_MAPPING = "uf_kotei6_mapping";
		/** ユニバーサルフィールドマッピング(固定値)⑦ */
		public static final String UF_KOTEI7_MAPPING = "uf_kotei7_mapping";
		/** ユニバーサルフィールドマッピング(固定値)⑧ */
		public static final String UF_KOTEI8_MAPPING = "uf_kotei8_mapping";
		/** ユニバーサルフィールドマッピング(固定値)⑨ */
		public static final String UF_KOTEI9_MAPPING = "uf_kotei9_mapping";
		/** ユニバーサルフィールドマッピング(固定値)⑩ */
		public static final String UF_KOTEI10_MAPPING = "uf_kotei10_mapping";
		/** 添付ファイル連携 */
		public static final String TENPU_FILE_RENKEI = "tenpu_file_renkei";
		/** 仕訳作成エラー発生時の進め方 */
		public static final String SHIWAKE_SAKUSEI_ERROR_ACTION = "shiwake_sakusei_error_action";
		/** 先日付の仕訳連携 */
		public static final String SHIWAKE_SAKUSEI_SAKIHIDUKE = "shiwake_sakusei_sakihiduke";
		/** 仕訳作成方法(経費立替精算) */
		public static final String SHIWAKE_SAKUSEI_HOUHOU_A001 = "shiwake_sakusei_houhou_A001";
		/** 仕訳作成方法(自動引落) */
		public static final String SHIWAKE_SAKUSEI_HOUHOU_A009 = "shiwake_sakusei_houhou_A009";
		/** 仕訳作成方法(出張旅費精算) */
		public static final String SHIWAKE_SAKUSEI_HOUHOU_A004 = "shiwake_sakusei_houhou_A004";
		/** 仕訳作成方法(海外出張旅費精算) */
		public static final String SHIWAKE_SAKUSEI_HOUHOU_A011 = "shiwake_sakusei_houhou_A011";
		/** 仕訳作成方法(交通費精算) */
		public static final String SHIWAKE_SAKUSEI_HOUHOU_A010 = "shiwake_sakusei_houhou_A010";
		/** 仮払残金仕訳作成有無(経費立替精算) */
		public static final String ZANKIN_SHIWAKE_SAKUSEI_UMU_A001 = "zankin_shiwake_sakusei_umu_A001";
		/** 仮払残金仕訳作成有無(出張旅費精算) */
		public static final String ZANKIN_SHIWAKE_SAKUSEI_UMU_A004 = "zankin_shiwake_sakusei_umu_A004";
		/** 仮払残金仕訳作成有無(海外出張旅費精算) */
		public static final String ZANKIN_SHIWAKE_SAKUSEI_UMU_A011 = "zankin_shiwake_sakusei_umu_A011";
		/** 仮払残金仕訳の日付(経費立替精算) */
		public static final String ZANKIN_SHIWAKE_SAKUSEI_HI_A001 = "zankin_shiwake_hi_A001";
		/** 仮払残金仕訳の日付(出張旅費精算) */
		public static final String ZANKIN_SHIWAKE_SAKUSEI_HI_A004 = "zankin_shiwake_hi_A004";
		/** 仮払残金仕訳の日付(海外出張旅費精算) */
		public static final String ZANKIN_SHIWAKE_SAKUSEI_HI_A011 = "zankin_shiwake_hi_A011";
		/** 開始番号(伝票番号) */
		public static final String DENPYOU_SERIAL_NO_START = "denpyou_serial_no_start";
		/** 終了番号(伝票番号) */
		public static final String DENPYOU_SERIAL_NO_END = "denpyou_serial_no_end";
		/** 経費立替精算 */
		public static final String DENPYOU_SAKUSEI_TANI_A001 = "denpyou_sakusei_tani_A001";
		/** 経費伺い申請（仮払申請） */
		public static final String DENPYOU_SAKUSEI_TANI_A002 = "denpyou_sakusei_tani_A002";
		/** 請求書払い申請 */
		public static final String DENPYOU_SAKUSEI_TANI_A003 = "denpyou_sakusei_tani_A003";
		/** 支払依頼申請 */
		public static final String DENPYOU_SAKUSEI_TANI_A013 = "denpyou_sakusei_tani_A013";
		/** 自動引落伝票 */
		public static final String DENPYOU_SAKUSEI_TANI_A009 = "denpyou_sakusei_tani_A009";
		/** 出張旅費精算 */
		public static final String DENPYOU_SAKUSEI_TANI_A004 = "denpyou_sakusei_tani_A004";
		/** 出張伺い申請（仮払申請） */
		public static final String DENPYOU_SAKUSEI_TANI_A005 = "denpyou_sakusei_tani_A005";
		/** 海外出張旅費精算 */
		public static final String DENPYOU_SAKUSEI_TANI_A011 = "denpyou_sakusei_tani_A011";
		/** 海外出張伺い申請（仮払申請） */
		public static final String DENPYOU_SAKUSEI_TANI_A012 = "denpyou_sakusei_tani_A012";
		/** 交通費精算 */
		public static final String DENPYOU_SAKUSEI_TANI_A010 = "denpyou_sakusei_tani_A010";
		/** 通勤定期申請 */
		public static final String DENPYOU_SAKUSEI_TANI_A006 = "denpyou_sakusei_tani_A006";
		/** 振替伝票 */
		public static final String DENPYOU_SAKUSEI_TANI_A007 = "denpyou_sakusei_tani_A007";
		/** 総合付替伝票 */
		public static final String DENPYOU_SAKUSEI_TANI_A008 = "denpyou_sakusei_tani_A008";
		/** 経費立替精算 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A001 = "shiwake_tekiyou_naiyou_A001";
		/** 経費立替仮払相殺 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A001_1 = "shiwake_tekiyou_naiyou_A001_1";
		/** (仮払未使用)経費立替仮払相殺 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A001_2 = "shiwake_tekiyou_naiyou_A001_2";
		/** 経費立替仮払残金 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A001_3 = "shiwake_tekiyou_naiyou_A001_3";
		/** 経費伺い申請（仮払申請） */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A002 = "shiwake_tekiyou_naiyou_A002";
		/** 請求書払い申請 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A003 = "shiwake_tekiyou_naiyou_A003";
		/** 支払依頼申請 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A013 = "shiwake_tekiyou_naiyou_A013";
		/** 出張旅費精算 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A004 = "shiwake_tekiyou_naiyou_A004";
		/** 出張旅費精算仮払相殺 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A004_1 = "shiwake_tekiyou_naiyou_A004_1";
		/** (仮払未使用)出張旅費精算仮払相殺 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A004_2 = "shiwake_tekiyou_naiyou_A004_2";
		/** 出張旅費精算仮払残金 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A004_3 = "shiwake_tekiyou_naiyou_A004_3";
		/** 出張伺い申請（仮払申請） */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A005 = "shiwake_tekiyou_naiyou_A005";
		/** 海外出張旅費精算 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A011 = "shiwake_tekiyou_naiyou_A011";
		/** 海外出張旅費精算仮払相殺 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A011_1 = "shiwake_tekiyou_naiyou_A011_1";
		/** (仮払未使用)海外出張旅費精算仮払相殺 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A011_2 = "shiwake_tekiyou_naiyou_A011_2";
		/** 海外出張旅費精算仮払残金 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A011_3 = "shiwake_tekiyou_naiyou_A011_3";
		/** 海外出張伺い申請（仮払申請） */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A012 = "shiwake_tekiyou_naiyou_A012";
		/** 通勤定期申請 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A006 = "shiwake_tekiyou_naiyou_A006";
		/** 自動引落伝票 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A009 = "shiwake_tekiyou_naiyou_A009";
		/** 交通費精算 */
		public static final String SHIWAKE_TEKIYOU_NAIYOU_A010 = "shiwake_tekiyou_naiyou_A010";
		/** 支払方法(経費立替精算) */
		public static final String SHIHARAI_HOUHOU_A001 = "shiharai_houhou_A001";
		/** 支払方法(経費伺い申請（仮払申請）) */
		public static final String SHIHARAI_HOUHOU_A002 = "shiharai_houhou_A002";
		/** 支払方法(海外出張旅費・出張旅費・交通費) */
		public static final String SHIHARAI_HOUHOU_A004 = "shiharai_houhou_A004";
		/** 支払方法(海外出張伺い申請（仮払申請）・出張伺い申請（仮払申請）) */
		public static final String SHIHARAI_HOUHOU_A005 = "shiharai_houhou_A005";
		/** 請求書払い申請(計上日入力) */
		public static final String SEIKYUU_KEIJOU_NYUUROHYKU = "seikyuu_keijou_nyuuryoku";
		/** 請求書払い申請(申請者の計上日制限) */
		public static final String SEIKYUU_KEIJOU_SEIGEN = "seikyuu_keijou_seigen";
		/** 請求書払い申請(取引先マスター参照) */
		public static final String SEIKYUU_MASREF = "seikyuu_masref";
		/** 請求書払い申請（支払日一括登録） */
		public static final String SEIKYUU_SHIHARAIBI_IKKATSU = "seikyuu_shiharaibi_ikkatsu";
		/** 支払依頼申請(計上日制限) */
		public static final String SHIHARAIIRAI_KEIJOU_SEIGEN = "shiharaiirai_keijou_seigen";
		/** 支払依頼申請（支払日一括登録） */
		public static final String SHIHARAIIRAI_SHIHARAIBI_IKKATSU = "shiharaiirai_shiharaibi_ikkatsu";
		/** 自動引落伝票(計上日入力) */
		public static final String JIDOUHIKI_KEIJOU_NYUUROHYKU = "jidouhiki_keijou_nyuuryoku";
		/** 自動引落伝票(申請者の計上日制限) */
		public static final String JIDOUHIKI_KEIJOU_SEIGEN = "jidouhiki_keijou_seigen";
		/** 経費立替精算(計上日入力) */
		public static final String KEIJOUBI_DEFAULT_A001 = "keijoubi_default_A001";
		/** 経費立替精算(申請者の計上日制限) */
		public static final String KEIHISEISAN_KEIJOU_SEIGEN = "keihiseisan_keijou_seigen";
		/** 出張旅費精算(計上日入力) */
		public static final String KEIJOUBI_DEFAULT_A004 = "keijoubi_default_A004";
		/** 出張旅費精算(申請者の計上日制限) */
		public static final String RYOHISEISAN_KEIJOU_SEIGEN = "ryohiseisan_keijou_seigen";
		/** 海外出張旅費精算(計上日入力) */
		public static final String KEIJOUBI_DEFAULT_A011 = "keijoubi_default_A011";
		/** 海外出張旅費精算(申請者の計上日制限) */
		public static final String KAIGAIRYOHISEISAN_KEIJOU_SEIGEN = "kaigairyohiseisan_keijou_seigen";
		/** 交通費精算(計上日入力) */
		public static final String KEIJOUBI_DEFAULT_A010 = "keijoubi_default_A010";
		/** 交通費精算(申請者の計上日制限) */
		public static final String KOUTSUUHISEISAN_KEIJOU_SEIGEN = "koutsuuhiseisan_keijou_seigen";
		/** 計上日入力 */
		public static final String KEIJOU_NYUURYOKU = "keijou_nyuuryoku";
		/** 計上日範囲・前(請求書払い申請) */
		public static final String KEIJOU_HANI_MAE_A003 = "keijou_hani_mae_A003";
		/** 計上日範囲・後(請求書払い申請) */
		public static final String KEIJOU_HANI_ATO_A003 = "keijou_hani_ato_A003";
		/** 計上日範囲・前(支払依頼申請) */
		public static final String KEIJOU_HANI_MAE_A013 = "keijou_hani_mae_A013";
		/** 計上日範囲・後(支払依頼申請) */
		public static final String KEIJOU_HANI_ATO_A013 = "keijou_hani_ato_A013";
		/** 計上日範囲・前(自動引落伝票) */
		public static final String KEIJOU_HANI_MAE_A009 = "keijou_hani_mae_A009";
		/** 計上日範囲・後(自動引落伝票) */
		public static final String KEIJOU_HANI_ATO_A009 = "keijou_hani_ato_A009";
		/** 計上日範囲・前(経費立替精算) */
		public static final String KEIJOU_HANI_MAE_A001 = "keijou_hani_mae_A001";
		/** 計上日範囲・後(経費立替精算) */
		public static final String KEIJOU_HANI_ATO_A001 = "keijou_hani_ato_A001";
		/** 計上日範囲・前(出張旅費精算) */
		public static final String KEIJOU_HANI_MAE_A004 = "keijou_hani_mae_A004";
		/** 計上日範囲・後(出張旅費精算) */
		public static final String KEIJOU_HANI_ATO_A004 = "keijou_hani_ato_A004";
		/** 計上日範囲・前(海外出張旅費精算) */
		public static final String KEIJOU_HANI_MAE_A011 = "keijou_hani_mae_A011";
		/** 計上日範囲・後(海外出張旅費精算) */
		public static final String KEIJOU_HANI_ATO_A011 = "keijou_hani_ato_A011";
		/** 計上日範囲・前(交通費精算) */
		public static final String KEIJOU_HANI_MAE_A010 = "keijou_hani_mae_A010";
		/** 計上日範囲・後(交通費精算) */
		public static final String KEIJOU_HANI_ATO_A010 = "keijou_hani_ato_A010";
		/** 経費立替精算 */
		public static final String FURIKOMI_RULE_A001 = "furikomi_rule_A001";
		/** 経費伺い申請（仮払申請） */
		public static final String FURIKOMI_RULE_A002 = "furikomi_rule_A002";
		/** 出張旅費精算 */
		public static final String FURIKOMI_RULE_A004 = "furikomi_rule_A004";
		/** 出張伺い申請（仮払申請） */
		public static final String FURIKOMI_RULE_A005 = "furikomi_rule_A005";
		/** 海外出張旅費精算 */
		public static final String FURIKOMI_RULE_A011 = "furikomi_rule_A011";
		/** 海外出張伺い申請（仮払申請） */
		public static final String FURIKOMI_RULE_A012 = "furikomi_rule_A012";
		/** 交通費精算 */
		public static final String FURIKOMI_RULE_A010 = "furikomi_rule_A010";
		/** 通勤定期申請 */
		public static final String FURIKOMI_RULE_A006 = "furikomi_rule_A006";
		/** 押印枠　出力形式 */
		public static final String OUIN_FORMAT = "ouin_format";
		/** 押印枠　表示内容 */
		public static final String OUIN_NAIYOU = "ouin_naiyou";
		/** 印影（画像）表示 */
		public static final String INEI = "inei";
		/** 押印枠サイズ */
		public static final String OUIN_SIZE = "ouin_size";
		/** 添付ファイルのファイル名出力 */
		public static final String FILENAME_OUT = "filename_out";
		/** 遡及日適用印刷機能利用 */
		public static final String SOKYU_INSATSU = "sokyu_insatsu";
		/** 経費明細一覧に部門を出力する */
		public static final String KEIHIMEISAI_BUMON = "keihimeisai_bumon";
		/** 経費明細一覧に枝番を出力する */
		public static final String KEIHIMEISAI_EDANO = "keihimeisai_edano";
		/** 経費明細一覧セキュリティパターン設定 */
		public static final String KEIHIMEISAI_SECURITY_PSETTEI = "keihimeisai_security_psettei";
		/** 経費立替精算　消費税額設定 */
		public static final String ZEINUKI_ZEIGAKU_HENKOU_A001 = "zeinuki_zeigaku_henkou_A001";
		/** 請求書払い申請　消費税額設定 */
		public static final String ZEINUKI_ZEIGAKU_HENKOU_A003 = "zeinuki_zeigaku_henkou_A003";
		/** 請求書払い申請　入力方式 */
		public static final String ZEI_DEFAULT_A003 = "zei_default_A003";
		/** 請求書払い申請　入力方式の変更 */
		public static final String ZEI_HENKOU_A003 = "zei_henkou_A003";
		/** 出張旅費精算　消費税額設定 */
		public static final String ZEINUKI_ZEIGAKU_HENKOU_A004 = "zeinuki_zeigaku_henkou_A004";
		/** 通勤定期申請　消費税額設定 */
		public static final String ZEINUKI_ZEIGAKU_HENKOU_A006 = "zeinuki_zeigaku_henkou_A006";
		/** 自動引落伝票　消費税額設定 */
		public static final String ZEINUKI_ZEIGAKU_HENKOU_A009 = "zeinuki_zeigaku_henkou_A009";
		/** 自動引落伝票　入力方式 */
		public static final String ZEI_DEFAULT_A009 = "zei_default_A009";
		/** 自動引落伝票　入力方式の変更 */
		public static final String ZEI_HENKOU_A009 = "zei_henkou_A009";
		/** 交通費精算　消費税額設定 */
		public static final String ZEINUKI_ZEIGAKU_HENKOU_A010 = "zeinuki_zeigaku_henkou_A010";
		/** 海外出張旅費精算　消費税額設定 */
		public static final String ZEINUKI_ZEIGAKU_HENKOU_A011 = "zeinuki_zeigaku_henkou_A011";
		/** 支払依頼申請　消費税額設定 */
		public static final String ZEINUKI_ZEIGAKU_HENKOU_A013 = "zeinuki_zeigaku_henkou_A013";
		/** 支払依頼申請　入力方式 */
		public static final String ZEI_DEFAULT_A013 = "zei_default_A013";
		/** 支払依頼申請　入力方式の変更 */
		public static final String ZEI_HENKOU_A013 = "zei_henkou_A013";
		/** インボイス対応伝票 設定項目の使用設定 */
		public static final String INVOICE_SETTEI_FLG = "invoice_settei_flg";
		/** 請求書払い申請　事業者区分の変更 */
		public static final String JIGYOUSHA_HENKOU_FLG_A003 = "jigyousha_henkou_flg_A003";
		/** 自動引落伝票　事業者区分の変更 */
		public static final String JIGYOUSHA_HENKOU_FLG_A009 = "jigyousha_henkou_flg_A009";
		/** 支払依頼申請　事業者区分の変更 */
		public static final String JIGYOUSHA_HENKOU_FLG_A013 = "jigyousha_henkou_flg_A013";
		/** インボイス対応後　消費税額端数処理設定 */
		public static final String ZEIGAKU_HASUU_HENKAN_FLG = "zeigaku_hasuu_henkan_flg";
		/** e文書を有効にする */
		public static final String EBUNSHO_ENABLE_FLG = "ebunsho_enable_flg";
		/** e文書対象チェックの初期値 */
		public static final String EBUNSHO_TAISHOU_CHECK_DEFAULT = "ebunsho_taishou_check_default";
		/** 電子取引のTS付与チェックの初期値 */
		public static final String EBUNSHO_DENSHITS_CHECK_DEFAULT = "ebunsho_denshits_check_default";
		/** 経費立替精算 */
		public static final String EBUNSHO_SEISEI_A001 = "ebunsho_seisei_A001";
		/** 書類種別(経費立替精算) */
		public static final String EBUNSHO_SHUBETSU_A001 = "ebunsho_shubetsu_A001";
		/** 経費伺い申請（仮払申請） */
		public static final String EBUNSHO_SEISEI_A002 = "ebunsho_seisei_A002";
		/** 書類種別(経費伺い申請（仮払申請）) */
		public static final String EBUNSHO_SHUBETSU_A002 = "ebunsho_shubetsu_A002";
		/** 請求書払い申請 */
		public static final String EBUNSHO_SEISEI_A003 = "ebunsho_seisei_A003";
		/** 書類種別(請求書払い申請) */
		public static final String EBUNSHO_SHUBETSU_A003 = "ebunsho_shubetsu_A003";
		/** 出張旅費精算 */
		public static final String EBUNSHO_SEISEI_A004 = "ebunsho_seisei_A004";
		/** 書類種別(出張旅費精算) */
		public static final String EBUNSHO_SHUBETSU_A004 = "ebunsho_shubetsu_A004";
		/** 出張伺い申請（仮払申請） */
		public static final String EBUNSHO_SEISEI_A005 = "ebunsho_seisei_A005";
		/** 書類種別(出張伺い申請（仮払申請）) */
		public static final String EBUNSHO_SHUBETSU_A005 = "ebunsho_shubetsu_A005";
		/** 通勤定期申請 */
		public static final String EBUNSHO_SEISEI_A006 = "ebunsho_seisei_A006";
		/** 書類種別(通勤定期申請) */
		public static final String EBUNSHO_SHUBETSU_A006 = "ebunsho_shubetsu_A006";
		/** 自動引落伝票 */
		public static final String EBUNSHO_SEISEI_A009 = "ebunsho_seisei_A009";
		/** 書類種別(自動引落伝票) */
		public static final String EBUNSHO_SHUBETSU_A009 = "ebunsho_shubetsu_A009";
		/** 交通費精算 */
		public static final String EBUNSHO_SEISEI_A010 = "ebunsho_seisei_A010";
		/** 書類種別(交通費精算) */
		public static final String EBUNSHO_SHUBETSU_A010 = "ebunsho_shubetsu_A010";
		/** 海外出張旅費精算 */
		public static final String EBUNSHO_SEISEI_A011 = "ebunsho_seisei_A011";
		/** 書類種別(海外出張旅費精算) */
		public static final String EBUNSHO_SHUBETSU_A011 = "ebunsho_shubetsu_A011";
		/** 海外出張伺い申請（仮払申請） */
		public static final String EBUNSHO_SEISEI_A012 = "ebunsho_seisei_A012";
		/** 書類種別(海外出張伺い申請（仮払申請）) */
		public static final String EBUNSHO_SHUBETSU_A012 = "ebunsho_shubetsu_A012";
		/** 支払依頼申請 */
		public static final String EBUNSHO_SEISEI_A013 = "ebunsho_seisei_A013";
		/** 書類種別(支払依頼申請) */
		public static final String EBUNSHO_SHUBETSU_A013 = "ebunsho_shubetsu_A013";
		/** ユーザー定義届出 */
		public static final String EBUNSHO_SEISEI_B000 = "ebunsho_seisei_B000";
		/** 書類種別(ユーザー定義届出) */
		public static final String EBUNSHO_SHUBETSU_B000 = "ebunsho_shubetsu_B000";
		/** e文書変換時に画像を圧縮する */
		public static final String EBUNSHO_COMPRESS_FLG = "ebunsho_compress_flg";
		/** 経費立替精算　添付ファイルチェック */
		public static final String TENPU_CHECK_A001 = "tenpu_check_A001";
		/** 経費立替精算　領収書・請求書等デフォルト値 */
		public static final String SHOUHYOU_SHORUI_DEFAULT_A001 = "shouhyou_shorui_default_A001";
		/** 出張旅費精算　添付ファイルチェック */
		public static final String TENPU_CHECK_A004 = "tenpu_check_A004";
		/** 出張旅費精算　領収書・請求書等デフォルト値 */
		public static final String SHOUHYOU_SHORUI_DEFAULT_A004 = "shouhyou_shorui_default_A004";
		/** 海外出張旅費精算　添付ファイルチェック */
		public static final String TENPU_CHECK_A011 = "tenpu_check_A011";
		/** 海外出張旅費精算　領収書・請求書等デフォルト値 */
		public static final String SHOUHYOU_SHORUI_DEFAULT_A011 = "shouhyou_shorui_default_A011";
		/** 交通費精算　添付ファイルチェック */
		public static final String TENPU_CHECK_A010 = "tenpu_check_A010";
		/** 交通費精算　領収書・請求書等デフォルト値 */
		public static final String SHOUHYOU_SHORUI_DEFAULT_A010 = "shouhyou_shorui_default_A010";
		/** 請求書払い申請　添付ファイルチェック */
		public static final String TENPU_CHECK_A003 = "tenpu_check_A003";
		/** 請求書払い申請　領収書・請求書等デフォルト値 */
		public static final String SHOUHYOU_SHORUI_DEFAULT_A003 = "shouhyou_shorui_default_A003";
		/** 支払依頼申請　添付ファイルチェック */
		public static final String TENPU_CHECK_A013 = "tenpu_check_A013";
		/** 支払依頼申請　領収書・請求書等デフォルト値 */
		public static final String SHOUHYOU_SHORUI_DEFAULT_A013 = "shouhyou_shorui_default_A013";
		/** 自動引落伝票　添付ファイルチェック */
		public static final String TENPU_CHECK_A009 = "tenpu_check_A009";
		/** 自動引落伝票　領収書・請求書等デフォルト値 */
		public static final String SHOUHYOU_SHORUI_DEFAULT_A009 = "shouhyou_shorui_default_A009";
		/** 添付伝票に仮払案件を自動的に追加 */
		public static final String TENPU_DENPYOU_JIDOU_FLG = "tenpu_denpyou_jidou_flg";
		/** 差引項目名称 */
		public static final String SASHIHIKI_NAME = "sashihiki_name";
		/** 差引単価 */
		public static final String SASHIHIKI_TANKA = "sashihiki_tanka";
		/** 差引項目名称（海外） */
		public static final String SASHIHIKI_NAME_KAIGAI = "sashihiki_name_kaigai";
		/** 差引単価（海外）邦貨 */
		public static final String SASHIHIKI_TANKA_KAIGAI = "sashihiki_tanka_kaigai";
		/** 差引単価（海外）外貨 */
		public static final String SASHIHIKI_TANKA_KAIGAI_GAIKA_HEISHU = "sashihiki_tanka_kaigai_gaika_heishu";
		/** 差引単価（海外）外貨 */
		public static final String SASHIHIKI_TANKA_KAIGAI_GAIKA = "sashihiki_tanka_kaigai_gaika";
		/** 申請期間チェック日数 */
		public static final String KEIHI_SHINSEI_KIKAN_CHECK_NISSUU = "keihi_shinsei_kikan_check_nissuu";
		/** 申請期間チェック文言 */
		public static final String KEIHI_SHINSEI_KIKAN_CHECK_MONGON = "keihi_shinsei_kikan_check_mongon";
		/** 仮払選択制御 */
		public static final String KEIHI_KARIBARAI_SENTAKU_SEIGYO = "keihi_karibarai_sentaku_seigyo";
		/** 申請期間チェック日数 */
		public static final String SHINSEI_KIKAN_CHECK_NISSUU = "shinsei_kikan_check_nissuu";
		/** 申請期間チェック文言 */
		public static final String SHINSEI_KIKAN_CHECK_MONGON = "shinsei_kikan_check_mongon";
		/** 仮払選択制御 */
		public static final String KARIBARAI_SENTAKU_SEIGYO = "karibarai_sentaku_seigyo";
		/** 申請期間チェック日数 */
		public static final String KAIGAI_SHINSEI_KIKAN_CHECK_NISSUU = "kaigai_shinsei_kikan_check_nissuu";
		/** 申請期間チェック文言 */
		public static final String KAIGAI_SHINSEI_KIKAN_CHECK_MONGON = "kaigai_shinsei_kikan_check_mongon_gai_";
		/** 仮払選択制御 */
		public static final String KAIGAI_KARIBARAI_SENTAKU_SEIGYO = "kaigai_karibarai_sentaku_seigyo_gai_";
		/** 予算チェック:判定コメント・予算内 */
		public static final String YOSAN_COMMENT_YOSANNAI = "yosan_comment_yosannai";
		/** 予算チェック:判定コメント・超過 */
		public static final String YOSAN_COMMENT_CHOUKA = "yosan_comment_chouka";
		/** 予算チェック:超過判定％ */
		public static final String YOSAN_CHOUKA_HANTEI = "yosan_chouka_hantei";
		/** 予算チェック:予算番号 */
		public static final String YOSAN_YOSAN_NO = "yosan_yosan_no";
		/** 起案チェック:判定コメント・予算内 */
		public static final String KIAN_COMMENT_YOSANNAI = "kian_comment_yosannai";
		/** 起案チェック:判定コメント・超過 */
		public static final String KIAN_COMMENT_CHOUKA = "kian_comment_chouka";
		/** 起案チェック:超過判定％ */
		public static final String KIAN_CHOUKA_HANTEI = "kian_chouka_hantei";
		/** 起案番号のHF転記先①略号 */
		public static final String KIAN_NO_HANEI_RYAKUGOU = "kian_no_hanei_ryakugou";
		/** 起案番号のHF転記先②番号 */
		public static final String KIAN_NO_HANEI_BANGOU = "kian_no_hanei_bangou";
		/** セキュリティパターン */
		public static final String YOSAN_SECURITY_PATTERN = "yosan_security_pattern";
		/** 合議部署の予算確認設定 */
		public static final String GOUGI_YOSAN_KAKUNIN = "gougi_yosan_kakunin";
		/** 予算チェック粒度 */
		public static final String YOSAN_CHECK_TANI = "yosan_check_tani";
		/** 予算チェック期間 */
		public static final String YOSAN_CHECK_KIKAN = "yosan_check_kikan";
		/** 予算・起案チェックコメント表示制御 */
		public static final String YOSAN_KIAN_COMMNENT = "yosan_kian_comment";
		/** 控除項目　項目名称 */
		public static final String MANEKIN_NAME = "manekin_name";
		/** 控除項目　勘定科目コード */
		public static final String MANEKIN_CD = "manekin_cd";
		/** 控除項目　勘定科目枝番コード */
		public static final String MANEKIN_EDABAN = "manekin_edaban";
		/** 控除項目　負担部門コード */
		public static final String MANEKIN_BUMON = "manekin_bumon";
		/** 一見先　取引先コード */
		public static final String ICHIGEN_CD = "ichigen_cd";
		/** 一見先　振込手数料 */
		public static final String ICHIGEN_TESUURYOU = "ichigen_tesuuryou";
		/** 一見先　振込手数料勘定科目コード */
		public static final String ICHIGEN_TESUURYOU_KAMOKU_CD = "ichigen_tesuuryou_kamoku_cd";
		/** 一見先　振込手数料勘定科目枝番コード */
		public static final String ICHIGEN_TESUURYOU_EDABAN_CD = "ichigen_tesuuryou_edaban_cd";
		/** 一見先　振込手数料負担部門コード */
		public static final String ICHIGEN_TESUURYOU_BUMON_CD = "ichigen_tesuuryou_bumon_cd";
		/** 一見先　振込手数料負担部門コードの任意部門の判断基準 */
		public static final String ICHIGEN_TESUURYOU_BUMON_CD_NINI_BUMON_HANTEI = "ichigen_tesuuryou_bumon_cd_nini_bumon_hantei";
		/** 未払い不定期　貸方科目コード */
		public static final String KASHIKATAKAMOKU_CD_MIBARAI_FUTEIKI = "kashikatakamoku_cd_mibarai_futeiki";
		/** 未払い定期　　貸方科目コード */
		public static final String KASHIKATAKAMOKU_CD_MIBARAI_TEIKI = "kashikatakamoku_cd_mibarai_teiki";
		/** 設備未払い　　貸方科目コード */
		public static final String KASHIKATAKAMOKU_CD_SETSUBI_MIBARAI = "kashikatakamoku_cd_setsubi_mibarai";
		/** 部門コード桁数 */
		public static final String BUMON_CD_KETASUU = "bumon_cd_ketasuu";
		/** １ページ表示件数 */
		public static final String RECORD_NUM_PER_PAGE = "record_num_per_page";
		/** １ページ表示件数(取引先選択) */
		public static final String RECORD_NUM_PER_PAGE_TORIHIKISAKI = "record_num_per_page_torihikisaki";
		/** ダウンロードファイル件数 */
		public static final String FILE_NUM_FOR_DOWNLOAD = "file_num_for_download";
		/** データ保存日数(WF一般) */
		public static final String DATA_HOZON_NISSUU = "data_hozon_nissuu";
		/** データ保存日数(ログ) */
		public static final String DATA_HOZON_NISSUU_LOG = "data_hozon_nissuu_log";
		/** 金融機関コード(郵貯) */
		public static final String KINYUKIKAN_CD_YUUCHO = "kinyukikan_cd_yuucho";
		/** DBバックアップファイルパス */
		public static final String DBDUMP_URL = "dbdump_url";
		/** テナント最大ユーザー数 */
		public static final String TENANT_MAX_USERS_NUM = "tenant_max_users_num";
		/** 法人名 */
		public static final String HYOUJI_KAISHA_NUM = "hyouji_kaisha_num";
		/** 取引コードの設定可否 */
		public static final String TORIHIKI_SETTEI = "torihiki_settei";
		/** ユーザー情報の変更可否 */
		public static final String USER_INFO_HENKOU = "user_info_henkou";
		/** 邦貨換算端数処理方法 */
		public static final String HOUKA_KANSAN_HASUU = "houka_kansan_hasuu";
		/** 負担部門選択時に集計部門による制限を行う */
		public static final String FUTAN_BUMON_SHUKEI_FILTER = "futan_bumon_shukei_filter";
		/** 添付ファイル容量 */
		public static final String TENPU_SIZE_SEIGEN = "tenpu_size_seigen";
		/** 経理処理ロールのマル秘参照制限 */
		public static final String KEIRI_MARUHI_REF = "keiri_maruhi_ref";
		/** 申請・承認時のみマル秘設定可能とする */
		public static final String MARUHI_SETTEI_SHOUNIN_SEIGEN = "maruhi_settei_shounin_seigen";
		/** 合議部署の最終決裁後の閲覧制限設定 */
		public static final String ETSURAN_SEIGEN_GOUGI = "etsuran_seigen_gougi";
		/** 閲覧者の最終決裁後の閲覧制限設定 */
		public static final String ETSURAN_SEIGEN_ETSURANSHA = "etsuran_seigen_etsuransha";
		/** 通勤定期申請で仕訳作成しない場合に保持する定期情報の有効期限 */
		public static final String TEIKI_JOUHOU_YUUKOU_KIGEN = "teiki_jouhou_yuukou_kigen";
		/** 稟議金額の超過判定% */
		public static final String RINGI_CHOUKA_HANTEI = "ringi_chouka_hantei";
		/** 稟議金額を超過した伝票の承認等可否 */
		public static final String RINGI_CHOUKA_SHOUNINTOU = "ringi_chouka_shounintou";
		/** ユーザー定義届書伝票検索条件名称（件名） */
		public static final String USER_TEIGI_TODOKE_KENSAKU_KENMEI = "user_teigi_todoke_kensaku_kenmei";
		/** ユーザー定義届書伝票検索条件名称（内容） */
		public static final String USER_TEIGI_TODOKE_KENSAKU_NAIYOU = "user_teigi_todoke_kensaku_naiyou";
		/** 所属部門＆配下部門のデータ共有する・しない */
		public static final String SHOZOKU_BUMON_DATA_KYOYU = "shozoku_bumon_data_kyoyu";
		/** 複数明細がある場合のルート判断基準 */
		public static final String ROUTE_HANTEI_TORIHIKI_SENTAKU_RULE = "route_hantei_torihiki_sentaku_rule";
		/** 海外用日当の単価設定 */
		public static final String KAIGAI_NITTOU_TANKA_GAIKA_SETTEI = "kaigai_nittou_tanka_gaika_settei";
		/** ログイン状態を維持する時間 */
		public static final String LOGIN_IJI_JIKAN = "login_iji_jikan";
		/** ユーザー登録の代表負担部門の未入力チェック */
		public static final String USER_DAIHYOUFUTANBUMON_MINYUURYOKU_CHECK = "user_daihyoufutanbumon_minyuuryoku_check";
		/** 最終承認の解除 */
		public static final String SAISHUUSHOUNIN_KAIJO_FLG = "saishuushounin_kaijo_flg";
		/** 伝票変更権限 */
		public static final String DENPYOU_HENKOU_FLG = "denpyou_henkou_flg";
		/** 伝票検索でのマル秘伝票表示制御 */
		public static final String MARUHI_HYOUJI_SEIGYO_FLG = "maruhi_hyouji_seigyo_flg";
		/** 予算オプション */
		public static final String YOSAN_OPTION = "yosan_option";
		/** WF一般オプション */
		public static final String IPPAN_OPTION = "ippan_option";
		/** 拠点入力オプション */
		public static final String KYOTEN_OPTION = "kyoten_option";



		/** 会社コード */
		public static final String OP21MPARAM_KAISHA_CD_KYOTEN = "op21mparam_kaisha_cd_kyoten";
		/** 諸口コード */
		public static final String SHOKUCHI_CD_KYOTEN = "shokuchi_cd_kyoten";
		/** 伝票形式(拠点入力：振替伝票) */
		public static final String OP21MPARAM_DENPYOU_KEISHIKI_Z001 = "op21mparam_denpyou_keishiki_Z001";
		/** 伝票形式(拠点入力：現預金出納帳) */
		public static final String OP21MPARAM_DENPYOU_KEISHIKI_Z002 = "op21mparam_denpyou_keishiki_Z002";
		/** 伝票入力パターン(拠点入力：振替伝票) */
		public static final String OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_Z001 = "op21mparam_denpyou_nyuuryoku_pattern_Z001";
		/** 伝票入力パターン(拠点入力：現預金出納帳) */
		public static final String OP21MPARAM_DENPYOU_NYUURYOKU_PATTERN_Z002 = "op21mparam_denpyou_nyuuryoku_pattern_Z002";
		/** 起動ユーザー */
		public static final String OP21MPARAM_KIDOU_USER_KYOTEN = "op21mparam_kidou_user_kyoten";
		/** 仕訳区分 */
		public static final String SHIWAKE_KBN_KYOTEN = "shiwake_kbn_kyoten";
		/** 確定有無 */
		public static final String KAKUTEI_UMU_KYOTEN = "kakutei_umu_kyoten";
		/** 伝票IDの連携先 */
		public static final String UF1_DENPYOU_ID_HANEI_KYOTEN = "uf1_denpyou_id_hanei_kyoten";
		/** 社員コードの連携先 */
		public static final String SHAIN_CD_RENKEI_KYOTEN = "shain_cd_renkei_kyoten";
		/** ヘッダーフィールドマッピング① */
		public static final String HF1_MAPPING_KYOTEN = "hf1_mapping_kyoten";
		/** ヘッダーフィールドマッピング② */
		public static final String HF2_MAPPING_KYOTEN = "hf2_mapping_kyoten";
		/** ヘッダーフィールドマッピング③ */
		public static final String HF3_MAPPING_KYOTEN = "hf3_mapping_kyoten";
		/** ヘッダーフィールドマッピング④ */
		public static final String HF4_MAPPING_KYOTEN = "hf4_mapping_kyoten";
		/** ヘッダーフィールドマッピング⑤ */
		public static final String HF5_MAPPING_KYOTEN = "hf5_mapping_kyoten";
		/** ヘッダーフィールドマッピング⑥ */
		public static final String HF6_MAPPING_KYOTEN = "hf6_mapping_kyoten";
		/** ヘッダーフィールドマッピング⑦ */
		public static final String HF7_MAPPING_KYOTEN = "hf7_mapping_kyoten";
		/** ヘッダーフィールドマッピング⑧ */
		public static final String HF8_MAPPING_KYOTEN = "hf8_mapping_kyoten";
		/** ヘッダーフィールドマッピング⑨ */
		public static final String HF9_MAPPING_KYOTEN = "hf9_mapping_kyoten";
		/** ヘッダーフィールドマッピング⑩ */
		public static final String HF10_MAPPING_KYOTEN = "hf10_mapping_kyoten";
		/** ユニバーサルフィールドマッピング① */
		public static final String UF1_MAPPING_KYOTEN = "uf1_mapping_kyoten";
		/** ユニバーサルフィールドマッピング② */
		public static final String UF2_MAPPING_KYOTEN = "uf2_mapping_kyoten";
		/** ユニバーサルフィールドマッピング③ */
		public static final String UF3_MAPPING_KYOTEN = "uf3_mapping_kyoten";
		/** ユニバーサルフィールドマッピング④ */
		public static final String UF4_MAPPING_KYOTEN = "uf4_mapping_kyoten";
		/** ユニバーサルフィールドマッピング⑤ */
		public static final String UF5_MAPPING_KYOTEN = "uf5_mapping_kyoten";
		/** ユニバーサルフィールドマッピング⑥ */
		public static final String UF6_MAPPING_KYOTEN = "uf6_mapping_kyoten";
		/** ユニバーサルフィールドマッピング⑦ */
		public static final String UF7_MAPPING_KYOTEN = "uf7_mapping_kyoten";
		/** ユニバーサルフィールドマッピング⑧ */
		public static final String UF8_MAPPING_KYOTEN = "uf8_mapping_kyoten";
		/** ユニバーサルフィールドマッピング⑨ */
		public static final String UF9_MAPPING_KYOTEN = "uf9_mapping_kyoten";
		/** ユニバーサルフィールドマッピング⑩ */
		public static final String UF10_MAPPING_KYOTEN = "uf10_mapping_kyoten";
		/** ユニバーサルフィールドマッピング(固定値)① */
		public static final String UF_KOTEI1_MAPPING_KYOTEN = "uf_kotei1_mapping_kyoten";
		/** ユニバーサルフィールドマッピング(固定値)② */
		public static final String UF_KOTEI2_MAPPING_KYOTEN = "uf_kotei2_mapping_kyoten";
		/** ユニバーサルフィールドマッピング(固定値)③ */
		public static final String UF_KOTEI3_MAPPING_KYOTEN = "uf_kotei3_mapping_kyoten";
		/** ユニバーサルフィールドマッピング(固定値)④ */
		public static final String UF_KOTEI4_MAPPING_KYOTEN = "uf_kotei4_mapping_kyoten";
		/** ユニバーサルフィールドマッピング(固定値)⑤ */
		public static final String UF_KOTEI5_MAPPING_KYOTEN = "uf_kotei5_mapping_kyoten";
		/** ユニバーサルフィールドマッピング(固定値)⑥ */
		public static final String UF_KOTEI6_MAPPING_KYOTEN = "uf_kotei6_mapping_kyoten";
		/** ユニバーサルフィールドマッピング(固定値)⑦ */
		public static final String UF_KOTEI7_MAPPING_KYOTEN = "uf_kotei7_mapping_kyoten";
		/** ユニバーサルフィールドマッピング(固定値)⑧ */
		public static final String UF_KOTEI8_MAPPING_KYOTEN = "uf_kotei8_mapping_kyoten";
		/** ユニバーサルフィールドマッピング(固定値)⑨ */
		public static final String UF_KOTEI9_MAPPING_KYOTEN = "uf_kotei9_mapping_kyoten";
		/** ユニバーサルフィールドマッピング(固定値)⑩ */
		public static final String UF_KOTEI10_MAPPING_KYOTEN = "uf_kotei10_mapping_kyoten";
		/** 添付ファイル連携 */
		public static final String TENPU_FILE_RENKEI_KYOTEN = "tenpu_file_renkei_kyoten";
		/** 仕訳作成エラー発生時の進め方 */
		public static final String SHIWAKE_SAKUSEI_ERROR_ACTION_KYOTEN = "shiwake_sakusei_error_action_kyoten";
		/** 先日付の仕訳連携 */
		public static final String SHIWAKE_SAKUSEI_SAKIHIDUKE_KYOTEN = "shiwake_sakusei_sakihiduke_kyoten";
		/** 振替伝票開始番号(伝票番号) */
		public static final String BUMON_FURIKAE_DENPYOU_SERIAL_NO_START = "bumon_furikae_denpyou_serial_no_start";
		/** 振替伝票終了番号(伝票番号) */
		public static final String BUMON_FURIKAE_DENPYOU_SERIAL_NO_END = "bumon_furikae_denpyou_serial_no_end";
		/** 現預金出納帳開始番号(伝票番号) */
		public static final String SUITOUCHOU_DENPYOU_SERIAL_NO_START = "suitouchou_denpyou_serial_no_start";
		/** 現預金出納帳終了番号(伝票番号) */
		public static final String SUITOUCHOU_DENPYOU_SERIAL_NO_END = "suitouchou_denpyou_serial_no_end";
		/** 現預金出納帳　消費税額設定 */
		public static final String ZEINUKI_ZEIGAKU_HENKOU_Z002 = "zeinuki_zeigaku_henkou_Z002";
		/** 振替伝票(拠点入力) */
		public static final String EBUNSHO_SEISEI_Z001 = "ebunsho_seisei_Z001";
		/** 書類種別(振替伝票(拠点入力)) */
		public static final String EBUNSHO_SHUBETSU_Z001 = "ebunsho_shubetsu_Z001";
		/** 現預金出納帳 */
		public static final String EBUNSHO_SEISEI_Z002 = "ebunsho_seisei_Z002";
		/** 書類種別(現預金出納帳) */
		public static final String EBUNSHO_SHUBETSU_Z002 = "ebunsho_shubetsu_Z002";
		/** データ保存日数(拠点入力) */
		public static final String DATA_HOZON_NISSUU_KYOTEN = "data_hozon_nissuu_kyoten";


	}

	/**
	 * 設定情報をスキーマ単位でメモリ上に読込
	 * @param settingMap 設定情報
	 */
	protected static void reload(GMap settingMap) {
		try(EteamConnection connection = EteamConnection.connect()) {
			SystemKanriCategoryLogic lg = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			List<GMap> list = lg.loadSettingVal();
			for (GMap record : list) {
				settingMap.put(record.get("setting_name"), record.get("setting_val"));
			}
		}
	}

	/**
	 * 設定値取得
	 * @param settingName 設定名
	 * @return 設定値
	 */
	public static String getSettingInfo(String settingName) {
		GMap settingMap = ThreadLocalUtil.get("settingMap");
		if(settingMap.isEmpty()) {
			//スレッドローカルの初回のみ読み込み実施
			reload(settingMap);
		}

		String settingVal = settingMap.get(settingName);
		if(settingName.equals(Key.YOSAN_SECURITY_PATTERN) && settingVal.equals("")) {
			throw new RuntimeException("会社設定(予算執行)の「セキュリティパターン」に値がセットされていません。\r\n管理者に確認してください。");
		}
		return settingVal;
	}

	/**
	 * 社員コード連携先フィールドが各フィールドのマッピングと一致しているかを判定します。
	 * @return HF1～3:ヘッダフィールド①～③と一致 UF1～3:ユニバーサルフィールド①～③と一致 0:マッピング一致無し
	 */
	public static String getShainCdRenkeiSakiKyoten() {
		/*
		 * 財務拠点入力用の社員コード連携やHF・UFマッピングは、画面入力と入力チェックでOPEN21連携タブの項目と同じ値になるよう制御している。
		 * そのため、同じメソッド利用としてよい。
		 */
		return getShainCdRenkeiSaki();
	}
	/**
	 * 社員コード連携先フィールドが各フィールドのマッピングと一致しているかを判定します。
	 * @return HF1～3:ヘッダフィールド①～③と一致 UF1～3:ユニバーサルフィールド①～③と一致 0:マッピング一致無し
	 */
	public static String getShainCdRenkeiSaki() {

		String shainCdRenkei = getSettingInfo(EteamSettingInfo.Key.SHAIN_CD_RENKEI);
		String[] hfMapping = {
				getSettingInfo(EteamSettingInfo.Key.HF1_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.HF2_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.HF3_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.HF4_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.HF5_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.HF6_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.HF7_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.HF8_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.HF9_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.HF10_MAPPING)
		};
		String[] ufMapping = {
				getSettingInfo(EteamSettingInfo.Key.UF1_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF2_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF3_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF4_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF5_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF6_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF7_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF8_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF9_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF10_MAPPING)
		};
		String[] ufKoteiMapping = {
				getSettingInfo(EteamSettingInfo.Key.UF_KOTEI1_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF_KOTEI2_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF_KOTEI3_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF_KOTEI4_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF_KOTEI5_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF_KOTEI6_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF_KOTEI7_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF_KOTEI8_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF_KOTEI9_MAPPING),
				getSettingInfo(EteamSettingInfo.Key.UF_KOTEI10_MAPPING)
		};
		String retStr = "0";

		if(shainCdRenkei.length() >= 3){
			if( Open21Env.getVersion() == Version.DE3 ){
				//DE3版の場合はshain_cd_renkeiの値がユニバーサルフィールドの表示上マッピングにそのまま対応
				retStr = shainCdRenkei;
			}else{
				//SIAS版の場合はshain_cd_renkeiの値とヘッダー・ユニバーサルフィールドマッピングの値を照合
				String numStr = shainCdRenkei.substring(2);
				if( "HF".equals(shainCdRenkei.substring(0,2)) ){
					for (int i = 0; i < hfMapping.length; i++) {
						if(numStr.equals(hfMapping[i])){
							retStr = "HF" + (i+1);
						}
					}
				}
				else if( "UF".equals(shainCdRenkei.substring(0,2)) ){
					for (int i = 0; i < ufMapping.length; i++) {
						if(numStr.equals(ufMapping[i])){
							retStr = "UF" + (i+1);
						}
					}
					for (int i = 0; i < ufKoteiMapping.length; i++) {
						if(numStr.equals(ufKoteiMapping[i])){
							retStr = "UF_KOTEI" + (i+1);
						}
					}
				}
			}
		}
		return retStr;
	}
}
