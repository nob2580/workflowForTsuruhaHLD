package eteam.common;

/**
 * 内部コード設定テーブルの中身を定数化したもの
 * ※コード定義書からの自動生成
 */
public class EteamNaibuCodeSetting {

	/** 機能制御コード */
	public static class KINOU_SEIGYO_CD {
		/** 代行 */
		public static final String DAIKOU = "DK";
		/** 一般ユーザーによる代行者指定 */
		public static final String USER_DAIKOU_SHITEI = "UD";
		/** 管理者による代行者指定 */
		public static final String KANRI_DAIKOU_SHITEI = "KD";
		/** ガイダンス起票 */
		public static final String GUIDANCE_KIHYOU = "GD";
		/** お気に入り起票 */
		public static final String OKINIIRIKIHYOU = "OK";
		/** メール配信 */
		public static final String MAIL_HAISHIN = "ML";
		/** 振込明細ダウンロード */
		public static final String CSV_DOWNLOAD = "CS";
		/** バッチ連携結果確認 */
		public static final String BATCH_RENKEIKEKKA_KAKUNIN = "BR";
		/** 会計データ連携 */
		public static final String KAIKEI_RENKEI = "KK";
		/** FBデータ作成 */
		public static final String FBDATA_SAKUSEI = "FB";
		/** 法人カード */
		public static final String HOUJIN_CARD = "HC";
		/** 伝票作成 */
		public static final String DENPYOU_SAKUSEI = "DS";
		/** ワークフロー：起票 */
		public static final String WORKFLOW_KIHYOU = "WK";
		/** ワークフロー：申請 */
		public static final String WORKFLOW_SHINSEI = "WS";
		/** ワークフロー：取下げ */
		public static final String WORKFLOW_TORISAGE = "WG";
		/** ワークフロー：取戻し */
		public static final String WORKFLOW_TORIMODOSHI = "WT";
		/** ワークフロー：差戻し */
		public static final String WORKFLOW_SASHIMODOSHI = "WM";
		/** ワークフロー：承認 */
		public static final String WORKFLOW_SHOUNIN = "WO";
		/** ワークフロー：否認 */
		public static final String WORKFLOW_HININ = "WN";
		/** 承認ルート変更 */
		public static final String SHOUNIN_ROUTE_HENKOU = "RH";
		/** 承認ルート変更(申請者による変更) */
		public static final String SHOUNIN_ROUTE_HENKOU_SHINSEISHA = "RR";
		/** 承認ルート変更(承認者による変更) */
		public static final String SHOUNIN_ROUTE_HENKOU_SHOUNINSHA = "RA";
		/** 上長承認後の取戻し */
		public static final String SHOUNINGO_TORIMODOSHI = "ST";
		/** 上位先決承認 */
		public static final String JOUI_SENKETSU_SHOUNIN = "SS";
		/** 代行者の自己承認 */
		public static final String DAIKOUSHA_JIKO_SHOUNIN = "SD";
		/** ファイル添付 */
		public static final String FILE_TENPU = "TP";
		/** 領収書・請求書等チェック必須 */
		public static final String RYOUSYUUSYO_SEIKYUUSHO_TOU_CHK_HISSU = "RC";
		/** データダウンロード */
		public static final String DATA_DOWNLOAD = "DL";
		/** 外貨入力 */
		public static final String GAIKA = "GK";
		/** 会社手配 */
		public static final String KAISHA_TEHAI = "KT";
		/** ICカード */
		public static final String IC_CARD = "IC";
		/** 添付ファイルのプレビュー表示 */
		public static final String PREVIEW_TENPU_FILE = "FP";
		/** WEB印刷ボタン表示 */
		public static final String WEB_INSATSU_BUTTON = "WB";
		/** 操作履歴欄を表示する */
		public static final String SOUSA_RIREKI = "SR";
		/** 注記の表示 */
		public static final String CHUUKI_PREVIEW = "CP";
	}

	/** 機能制御区分 */
	public static class KINOU_SEIGYO_KBN {
		/** 無効 */
		public static final String MUKOU = "0";
		/** 有効 */
		public static final String YUUKOU = "1";
	}

	/** 業務ロール機能制御コード */
	public static class GYOUMU_ROLE_KINOU_SEIGYO_CD {
		/** ワークフロー */
		public static final String WORKFLOW = "WF";
		/** 会社設定 */
		public static final String KAISHA_SETTEI = "CO";
		/** 経理処理 */
		public static final String KEIRI_SHORI = "KR";
	}

	/** 業務ロール機能制御区分 */
	public static class GYOUMU_ROLE_KINOU_SEIGYO_KBN {
		/** 無効 */
		public static final String MUKOU = "0";
		/** 有効 */
		public static final String YUUKOU = "1";
	}

	/** 通勤区分 */
	public static class TSUKIN_KBN {
		/** 運賃 */
		public static final String UNCHIN = "1";
		/** 通常 */
		public static final String TSUUJOU = "2";
		/** 業務用 */
		public static final String GYOUMUYOU = "3";
		/** その他 */
		public static final String SONOTA = "4";
	}

	/** 使用期間区分 */
	public static class SHIYOU_KIKAN_KBN {
		/** 1ヶ月 */
		public static final String KIKAN1 = "01";
		/** 3ヶ月 */
		public static final String KIKAN3 = "03";
		/** 6ヶ月 */
		public static final String KIKAN6 = "06";
	}

	/** 仕訳区分 */
	public static class SHIWAKE_KBN {
		/** 借方 */
		public static final String KARIKATA = "1";
		/** 貸方 */
		public static final String kASHIKATA = "2";
	}

	/** 課税区分 */
	// TODO 0有無ことや、課込仕「訳」になっていることの修正
	public static class KAZEI_KBN {
		/** 未設定 */
		public static final String MISETTEI = "";
		/** 対象外 */
		public static final String TAISHOUGAI = "000";
		/** 税込 */
		public static final String ZEIKOMI = "001";
		/** 税抜 */
		public static final String ZEINUKI = "002";
		/** 免税 */
		public static final String MENZEI = "003";
		/** 非課税 */
		public static final String HIKAZEI = "004";
		/** 課込仕入 */
		public static final String KAKOMI_SHIWAKE = "11";
		/** 課込売上 */
		public static final String KAKOMI_URIAGE = "12";
		/** 課抜仕入 */
		public static final String KANUKI_SHIIRE = "13";
		/** 課抜売上 */
		public static final String KANUKI_URIAGE = "14";
		/** 貸倒損込 */
		public static final String KASHITAOSHI_SONKOMI = "21";
		/** 貸倒損抜 */
		public static final String KASHITAOSHI_SONNUKI = "22";
		/** 貸倒回込 */
		public static final String KASHITAOSHI_KAIKOMI = "23";
		/** 貸倒回抜 */
		public static final String KASHITAOSHI_KAINUKI = "24";
		/** 課税貨物 */
		public static final String KAZEI_KAMOTSU = "31";
		/** 貨物国税 */
		public static final String KAMOTSU_KOKUZEI = "32";
		/** 貨物地方税 */
		public static final String KAMOTSU_CHIHOUZEI = "33";
		/** 非課税仕入 */
		public static final String HIKAZEI_SHIIRE = "41";
		/** 非課税売上 */
		public static final String HIKAZEI_URIAGE = "42";
		/** 特定収入 */
		public static final String TOKUTEI_SHUUNYUU = "51";
		/** 使途不明特定収入 */
		public static final String SHITOFUMEI = "52";
		/** 計算外 */
		public static final String KEISANGAI = "99";
		/** 対象外(属性無し) */
		public static final String TAISHOUGAI_ZOKUSEINASHI = "100";
	}

	/** 課税区分(支払依頼) */
	public static class KAZEI_KBN_SHIHARAIIRAI {
		/** 課込仕入 */
		public static final String KAKOMI_SHIIRE = "011";
		/** 非課税仕入 */
		public static final String HIKAZEI_SHIIRE = "041";
	}

	/** 課税区分グループ */
	public static class KAZEI_KBN_GROUP {
		/** 税込 */
		public static final String ZEIKOMI = "1";
		/** 税抜 */
		public static final String ZEINUKI = "2";
		/** その他 */
		public static final String SONOTA = "9";
	}

	/** 科目マスター課税区分 */
	public static class KAMOKU_KAZEI_KBN {
		/** 対象外 */
		public static final String TAISHOUGAI = "0";
		/** 税込 */
		public static final String ZEIKOMI = "1";
		/** 税抜 */
		public static final String ZEINUKI = "2";
		/** 免税 */
		public static final String MENZEI = "3";
		/** 非課税 */
		public static final String HIKAZEI = "4";
		/** 課込仕入 */
		public static final String KAKOMI_SHIWAKE = "11";
		/** 課込売上 */
		public static final String KAKOMI_URIAGE = "12";
		/** 課抜仕入 */
		public static final String KANUKI_SHIIRE = "13";
		/** 課抜売上 */
		public static final String KANUKI_URIAGE = "14";
		/** 貸倒損込 */
		public static final String KASHITAOSHI_SONKOMI = "21";
		/** 貸倒損抜 */
		public static final String KASHITAOSHI_SONNUKI = "22";
		/** 貸倒回込 */
		public static final String KASHITAOSHI_KAIKOMI = "23";
		/** 貸倒回抜 */
		public static final String KASHITAOSHI_KAINUKI = "24";
		/** 課税貨物 */
		public static final String KAZEI_KAMOTSU = "31";
		/** 貨物国税 */
		public static final String KAMOTSU_KOKUZEI = "32";
		/** 貨物地方税 */
		public static final String KAMOTSU_CHIHOUZEI = "33";
		/** 非課税仕入 */
		public static final String HIKAZEI_SHIIRE = "41";
		/** 非課税売上 */
		public static final String HIKAZEI_URIAGE = "42";
		/** 特定収入 */
		public static final String TOKUTEI_SHUUNYUU = "51";
		/** 使途不明特定収入 */
		public static final String SHITOFUMEI = "52";
		/** 計算外 */
		public static final String KEISANGAI = "99";
		/** 対象外(属性無し) */
		public static final String TAISHOUGAI_ZOKUSEINASHI = "100";
	}

	/** 科目マスター課税区分グループ */
	public static class KAMOKU_KAZEI_KBN_GROUP {
		/** 非課税 */
		public static final String HIKAZEI = "0";
		/** 課税 */
		public static final String KAZEI = "1";
	}

	/** 科目マスター消費税率 */
	public static class KAMOKU_SHOUHIZEI {
		/** 3 */
		public static final String SAN = "1";
		/** 5 */
		public static final String GO = "2";
		/** 8 */
		public static final String HACHI = "3";
		/** 8 */
		public static final String HACHI_KEIGEN = "4";
		/** 10 */
		public static final String JUU = "5";
	}

	/** 端数計算区分 */
	public static class HASUU_KEISAN_KBN {
		/** 切下 */
		public static final String KIRISAGE = "1";
		/** 切上 */
		public static final String KIRIAGE = "2";
		/** 四捨五入 */
		public static final String SISHAGONYUU = "3";
	}

	/** ソート区分 */
	public static class SORT_KBN {
		/** 昇順 */
		public static final String SHOU_JUN = "1";
		/** 降順 */
		public static final String KOU_JUN = "2";
	}

	/** 伝票区分 */
	public static class DENPYOU_KBN {
		/** 経費立替精算 */
		public static final String KEIHI_TATEKAE_SEISAN = "A001";
		/** 経費伺い申請（仮払申請） */
		public static final String KARIBARAI_SHINSEI = "A002";
		/** 請求書払い申請 */
		public static final String SEIKYUUSHO_BARAI = "A003";
		/** 出張旅費精算（仮払精算） */
		public static final String RYOHI_SEISAN = "A004";
		/** 出張伺い申請（仮払申請） */
		public static final String RYOHI_KARIBARAI_SHINSEI = "A005";
		/** 通勤定期申請 */
		public static final String TSUUKIN_TEIKI_SHINSEI = "A006";
		/** 振替伝票 */
		public static final String FURIKAE_DENPYOU = "A007";
		/** 総合付替伝票 */
		public static final String SOUGOU_TSUKEKAE_DENPYOU = "A008";
		/** 自動引落伝票 */
		public static final String JIDOU_HIKIOTOSHI_DENPYOU = "A009";
		/** 交通費精算 */
		public static final String KOUTSUUHI_SEISAN = "A010";
		/** 海外出張旅費精算（仮払精算） */
		public static final String KAIGAI_RYOHI_SEISAN = "A011";
		/** 海外出張伺い申請（仮払申請） */
		public static final String KAIGAI_RYOHI_KARIBARAI_SHINSEI = "A012";
		/** 支払依頼申請 */
		public static final String SIHARAIIRAI = "A013";
		
		/**
		 * @param denpyouKbn 精算伝票区分
		 * @return 対応する仮払伝票区分
		 */
		public static String getKaribaraiDenpyouKbn(String denpyouKbn)
		{
			switch(denpyouKbn)
			{
				case KEIHI_TATEKAE_SEISAN:
					return KARIBARAI_SHINSEI;
				case RYOHI_SEISAN:
					return RYOHI_KARIBARAI_SHINSEI;
				case KAIGAI_RYOHI_SEISAN:
					return KAIGAI_RYOHI_KARIBARAI_SHINSEI;
				default:
					return null;
			}
		}
		
		/**
		 * @param denpyouKbn 仮払伝票区分
		 * @return 対応する精算伝票区分
		 */
		public static String getSeikyuuDenpyouKbn(String denpyouKbn)
		{
			switch(denpyouKbn)
			{
				case KARIBARAI_SHINSEI:
					return KEIHI_TATEKAE_SEISAN;
				case RYOHI_KARIBARAI_SHINSEI:
					return RYOHI_SEISAN;
				case KAIGAI_RYOHI_KARIBARAI_SHINSEI:
					return KAIGAI_RYOHI_SEISAN;
				default:
					return null;
			}
		}
	}

	/** 伝票状態 */
	public static class DENPYOU_JYOUTAI {
		/** 未起票 */
		public static final String MI_KIHYOU = "00";
		/** 起票中 */
		public static final String KIHYOU_CHUU = "10";
		/** 申請中 */
		public static final String SHINSEI_CHUU = "20";
		/** 承認済 */
		public static final String SYOUNIN_ZUMI = "30";
		/** 否認済 */
		public static final String HININ_ZUMI = "40";
		/** 取下済 */
		public static final String TORISAGE_ZUMI = "90";
	}

	/** 承認状況 */
	public static class JOUKYOU {
		/** 申請 */
		public static final String SHINSEI = "1";
		/** 取戻し */
		public static final String TORIMODOSHI = "2";
		/** 取下げ */
		public static final String TORISAGE = "3";
		/** 承認 */
		public static final String SHOUNIN = "4";
		/** 否認 */
		public static final String HININ = "5";
		/** 差戻し */
		public static final String SASHIMODOSHI = "6";
		/** 承認解除 */
		public static final String SHOUNINKAIJO = "7";
	}

	/** インフォメーションステータス */
	public static class INFO_STATUS {
		/** 全て */
		public static final String ALL = "9";
		/** 掲載中 */
		public static final String KEISAI_CHUU = "1";
		/** 未掲載 */
		public static final String MI_KEISAI = "2";
		/** 掲載終了 */
		public static final String KEISAI_SHUURYOU = "3";
	}

	/** 関連伝票 */
	public static class KANREN_DENPYOU {
		/** 要処理伝票全て */
		public static final String YOUSHORI_ALL = "000";
		/** 起票中 */
		public static final String KIHYOU_CHUU = "010";
		/** 承認待ち */
		public static final String SHOUNIN_MACHI = "020";
		/** 下位未承認伝票のみ */
		public static final String KAI_MISHOUNIN = "021";
		/** 起票伝票全て */
		public static final String KIHYOU_ALL = "100";
		/** 申請中 */
		public static final String SHINSEI_CHUU = "120";
		/** 起票中 or 申請中 */
		public static final String KIHYOU_OR_SHINSEI = "121";
	}

	/** 付替区分 */
	public static class TSUKEKAE_KBN {
		/** 借方固定 */
		public static final String KARIKATA_KOUTEI = "1";
		/** 貸方固定 */
		public static final String KASHIKATA_KOUTEI = "2";
	}

	/** 証憑書類 */
	public static class SHOUHYOU_SHORUI {
		/** あり */
		public static final String ARI = "1";
		/** なし */
		public static final String NASI = "0";
	}

	/** 暗号化方法 */
	public static class ANGOUKA_HOUHOU {
		/** なし */
		public static final String NO = "NO";
		/** SSL */
		public static final String SSL = "SSL";
		/** STARTTLS */
		public static final String STARTTLS = "STARTTLS";
	}

	/** 仕訳パターン設定区分 */
	public static class SHIWAKE_PATTERN_SETTING_KBN {
		/** 取引（仕訳）内容 */
		public static final String TORIHIKI_NAIYOU = "1";
		/** 借方 */
		public static final String KARIKATA = "2";
		/** 貸方1 */
		public static final String KASHIKATA1 = "3";
		/** 貸方2 */
		public static final String KASHIKATA2 = "4";
		/** 貸方3 */
		public static final String KASHIKATA3 = "5";
		/** 貸方4 */
		public static final String KASHIKATA4 = "6";
		/** 貸方5 */
		public static final String KASHIKATA5 = "7";
	}

	/** 仕訳パターン設定項目 */
	public static class SHIWAKE_PATTERN_SETTING_ITEM {
		/** デフォルト表示 */
		public static final String DEFAULT_HYOUJI = "DEFAULT_HYOUJI";
		/** 交際費 */
		public static final String KOUSAIHI = "KOUSAIHI";
		/** 掛け */
		public static final String KAKE = "KAKE";
		/** 社員コード連携 */
		public static final String SHAIN = "SHAIN";
		/** 財務枝番コード連携 */
		public static final String ZAIMU_EDABAN = "ZAIMU_EDABAN";
		/** ヘッダーフィールド1 */
		public static final String HF1 = "HF1";
		/** ヘッダーフィールド2 */
		public static final String HF2 = "HF2";
		/** ヘッダーフィールド3 */
		public static final String HF3 = "HF3";
		/** ヘッダーフィールド4 */
		public static final String HF4 = "HF4";
		/** ヘッダーフィールド5 */
		public static final String HF5 = "HF5";
		/** ヘッダーフィールド6 */
		public static final String HF6 = "HF6";
		/** ヘッダーフィールド7 */
		public static final String HF7 = "HF7";
		/** ヘッダーフィールド8 */
		public static final String HF8 = "HF8";
		/** ヘッダーフィールド9 */
		public static final String HF9 = "HF9";
		/** ヘッダーフィールド10 */
		public static final String HF10 = "HF10";
		/** 部門 */
		public static final String BUMON = "BUMON";
		/** 科目 */
		public static final String KAMOKU = "KAMOKU";
		/** 科目枝番 */
		public static final String KAMOKUEDABAN = "KAMOKUEDABAN";
		/** 取引 */
		public static final String TORIHIKI = "TORIHIKI";
		/** プロジェクト */
		public static final String PROJECT = "PROJECT";
		/** セグメント */
		public static final String SEGMENT = "SEGMENT";
		/** ユニバーサルフィールド1 */
		public static final String UF1 = "UF1";
		/** ユニバーサルフィールド2 */
		public static final String UF2 = "UF2";
		/** ユニバーサルフィールド3 */
		public static final String UF3 = "UF3";
		/** ユニバーサルフィールド4 */
		public static final String UF4 = "UF4";
		/** ユニバーサルフィールド5 */
		public static final String UF5 = "UF5";
		/** ユニバーサルフィールド6 */
		public static final String UF6 = "UF6";
		/** ユニバーサルフィールド7 */
		public static final String UF7 = "UF7";
		/** ユニバーサルフィールド8 */
		public static final String UF8 = "UF8";
		/** ユニバーサルフィールド9 */
		public static final String UF9 = "UF9";
		/** ユニバーサルフィールド10 */
		public static final String UF10 = "UF10";
		/** ユニバーサルフィールド1（固定） */
		public static final String UF_KOTEI1 = "UF_KOTEI1";
		/** ユニバーサルフィールド2（固定） */
		public static final String UF_KOTEI2 = "UF_KOTEI2";
		/** ユニバーサルフィールド3（固定） */
		public static final String UF_KOTEI3 = "UF_KOTEI3";
		/** ユニバーサルフィールド4（固定） */
		public static final String UF_KOTEI4 = "UF_KOTEI4";
		/** ユニバーサルフィールド5（固定） */
		public static final String UF_KOTEI5 = "UF_KOTEI5";
		/** ユニバーサルフィールド6（固定） */
		public static final String UF_KOTEI6 = "UF_KOTEI6";
		/** ユニバーサルフィールド7（固定） */
		public static final String UF_KOTEI7 = "UF_KOTEI7";
		/** ユニバーサルフィールド8（固定） */
		public static final String UF_KOTEI8 = "UF_KOTEI8";
		/** ユニバーサルフィールド9（固定） */
		public static final String UF_KOTEI9 = "UF_KOTEI9";
		/** ユニバーサルフィールド10（固定） */
		public static final String UF_KOTEI10 = "UF_KOTEI10";
		/** 課税区分 */
		public static final String KAZEIKBN = "KAZEIKBN";
		/** 消費税率 */
		public static final String ZEIRITSU = "ZEIRITSU";
		/** 分離区分 */
		public static final String BUNRIKBN = "BUNRIKBN";
		/** 個別区分 */
		public static final String KOBETSUKBN = "KOBETSUKBN";
		/** 説明 */
		public static final String SETSUMEI = "SETSUMEI";
	}

	/** 支払方法 */
	public static class SHIHARAI_HOUHOU {
		/** 現金 */
		public static final String GENKIN = "1";
		/** 振込 */
		public static final String FURIKOMI = "2";
	}

	/** 支払種別 */
	public static class SHIHARAI_SHUBETSU {
		/** 約定A */
		public static final String YAKUTEI_A = "1";
		/** 約定B */
		public static final String YAKUTEI_B = "2";
		/** 支払手形 */
		public static final String SHIHARAI_TEGATA = "3";
		/** 銀行振込 */
		public static final String GINKOU_FURIKOMI = "6";
		/** 期日振込 */
		public static final String KIJITSU_FURIKOMI = "7";
		/** 一括支払 */
		public static final String IKKATSU_SHIHARAI = "8";
		/** 小切手 */
		public static final String KOGITTE = "10";
		/** 現金 */
		public static final String GENKIN = "11";
		/** 電子手形 */
		public static final String DENSHI_TEGATA = "12";
		/** 相殺 */
		public static final String SOUSAI = "21";
		/** 支払保留 */
		public static final String SHIHARAI_HORYUU = "25";
		/** 源泉税 */
		public static final String GENSENZEI = "29";
	}

	/** 手数料負担区分 */
	public static class TESUURYOU_FUTAN_KBN {
		/** 自社負担 */
		public static final String JISHA = "1";
		/** 客先負担 */
		public static final String KYAKUSAKI = "2";
		/** 折半 */
		public static final String SEPPAN = "3";
	}

	/** 預金種別 */
	public static class YOKIN_SHUBETSU {
		/** 普通 */
		public static final String FUTSUU = "1";
		/** 当座 */
		public static final String TOUZA = "2";
		/** 貯蓄 */
		public static final String CHOCHIKU = "4";
		/** その他 */
		public static final String SONOTA = "9";
	}

	/** 仕訳抽出状態 */
	public static class SHIWAKE_STATUS {
		/** 抽出済 */
		public static final String CHUUSHUTSU = "0";
		/** OPEN21転記済 */
		public static final String SHIWAKE_ZUMI = "1";
		/** 仕訳対象外 */
		public static final String SHIWAKE_TAISHOGAI = "9";
	}

	/** FB抽出状態 */
	public static class FB_STATUS {
		/** 抽出済 */
		public static final String CHUUSHUTSU = "0";
		/** FBデータ作成済 */
		public static final String FB_DATA_SAKUSEIZUMI = "1";
	}

	/** 登録状態 */
	public static class TOUROKU_STATUS {
		/** 全て */
		public static final String ALL = "9";
		/** 未登録 */
		public static final String MITOUROKU = "0";
		/** 登録済 */
		public static final String TOUROKUZUMI = "1";
	}

	/** 発着区分 */
	public static class HACCHAKU_KBN {
		/** その他 */
		public static final String SONOTA = "0";
		/** 自宅 */
		public static final String JITAKU = "1";
		/** 会社 */
		public static final String KAISYA = "2";
	}

	/** 旅費精算種別 */
	public static class RYOHISEISAN_SYUBETSU {
		/** 交通費 */
		public static final String KOUTSUUHI = "1";
		/** その他 */
		public static final String SONOTA = "2";
	}

	/** 既読フラグ */
	public static class KIDOKU_FLG {
		/** 未確認 */
		public static final String MIDOKU = "0";
		/** 確認済 */
		public static final String KIDOKU = "1";
		/** 全て */
		public static final String SUBETE = "2";
	}

	/** 画面権限制御区分 */
	public static class GAMEN_KENGEN_SEIGYO_KBN {
		/** 無効 */
		public static final String MUKOU = "0";
		/** 有効 */
		public static final String YUUKOU = "1";
	}

	/** バッチステータス */
	public static class BATCH_STATUS {
		/** 処理中 */
		public static final String SHORICHUU = "1";
		/** 正常終了 */
		public static final String SEIJOU = "2";
		/** 異常終了 */
		public static final String IJOU = "3";
	}

	/** メール通知区分 */
	public static class MAIL_TSUUCHI_KBN {
		/** 滞留通知 */
		public static final String TAIRYUU = "TTT";
		/** 承認待ち */
		public static final String SHOUNIN_MACHI = "RYS";
		/** 自伝票差戻し */
		public static final String JI_DENPYOU_SASHIMODOSHI = "RYK";
		/** 合議部署承認待ち */
		public static final String GOUGISHOUNIN_MACHI = "RYG";
		/** 自伝票承認 */
		public static final String JI_DENPYOU_SHOUNIN = "RJO";
		/** 自伝票否認 */
		public static final String JI_DENPYOU_HININ = "RJN";
		/** 関連伝票承認 */
		public static final String KANREN_DENPYOU_SHOUNIN = "RKO";
		/** 関連伝票否認 */
		public static final String KANREN_DENPYOU_HININ = "RKN";
	}

	/** 会計項目 */
	public static class KAIKEI_KOUMOKU {
		/** 仕訳枝番号 */
		public static final String SHIWAKEEDANO = "1";
		/** 取引先 */
		public static final String TORIHIKISAKI = "2";
		/** 勘定科目 */
		public static final String KAMOKU = "3";
		/** 勘定科目枝番 */
		public static final String KAMOKUEDANO = "4";
		/** 負担部門 */
		public static final String FUTANBUMON = "5";
		/** 課税区分 */
		public static final String KAZEIKBN = "6";
		/** UF1 */
		public static final String UF1 = "7";
		/** UF2 */
		public static final String UF2 = "8";
		/** UF3 */
		public static final String UF3 = "9";
		/** UF4 */
		public static final String UF4 = "10";
		/** UF5 */
		public static final String UF5 = "11";
		/** UF6 */
		public static final String UF6 = "12";
		/** UF7 */
		public static final String UF7 = "13";
		/** UF8 */
		public static final String UF8 = "14";
		/** UF9 */
		public static final String UF9 = "15";
		/** UF10 */
		public static final String UF10 = "16";
		/** プロジェクト */
		public static final String PROJECT = "17";
		/** セグメント */
		public static final String SEGMENT = "18";
	}

	/** 駅すぱあと表示順 */
	public static class HYOUJI_JUN {
		/** 探索順 */
		public static final String EKISPAERT_SORT_1 = "1";
		/** 運賃順 */
		public static final String EKISPAERT_SORT_2 = "2";
		/** 所要時間順 */
		public static final String EKISPAERT_SORT_3 = "3";
		/** 定期順 */
		public static final String EKISPAERT_SORT_4 = "4";
		/** 乗換回数順 */
		public static final String EKISPAERT_SORT_5 = "5";
		/** CO2排出量順 */
		public static final String EKISPAERT_SORT_6 = "6";
		/** 定期１カ月順 */
		public static final String EKISPAERT_SORT_7 = "7";
		/** 定期３カ月順 */
		public static final String EKISPAERT_SORT_8 = "8";
		/** 定期６カ月順 */
		public static final String EKISPAERT_SORT_9 = "9";
	}

	/** 駅すぱあとEX予約 */
	public static class JR_YOYAKU {
		/** 未設定 */
		public static final String EKISPERT_JRYOYAKU_0 = "0";
		/** ＥＸ予約 */
		public static final String EKISPERT_JRYOYAKU_1 = "1";
		/** ＥＸ予約(e 特急券) */
		public static final String EKISPERT_JRYOYAKU_2 = "2";
		/** ＥＸ予約(ＥＸ早特) */
		public static final String EKISPERT_JRYOYAKU_3 = "3";
		/** ＥＸ予約(ＥＸ早特２１)、 */
		public static final String EKISPERT_JRYOYAKU_4 = "4";
		/** ＥＸ予約(ＥＸグリーン早特) */
		public static final String EKISPERT_JRYOYAKU_5 = "5";
		/** スマートＥＸ */
		public static final String EKISPERT_JRYOYAKU_6 = "6";
		/** スマートＥＸ(ＥＸ早特) */
		public static final String EKISPERT_JRYOYAKU_7 = "7";
		/** スマートＥＸ(ＥＸ早特２１) */
		public static final String EKISPERT_JRYOYAKU_8 = "8";
		/** スマートＥＸ(ＥＸグリーン早特) */
		public static final String EKISPERT_JRYOYAKU_9 = "9";
	}

	/** 部品形式 */
	public static class BUHIN_FORMAT {
		/** 文字列 */
		public static final String STRING = "string";
		/** 数値 */
		public static final String NUMBER = "number";
		/** 日付 */
		public static final String DATE = "datepicker";
		/** 金額 */
		public static final String MONEY = "autoNumericWithCalcBox";
		/** 時間 */
		public static final String TIME = "timepicker";
	}

	/** 部品幅 */
	public static class BUHIN_WIDTH {
		/** 小 */
		public static final String INPUT_SMALL = "input-small";
		/** 中 */
		public static final String INPUT_MEDIUM = "input-medium";
		/** 大 */
		public static final String INPUT_XXLARGE = "input-xxlarge";
		/** 特大 */
		public static final String INPUT_BLOCK_LEVEL = "input-block-level";
	}

	/** 部品高さ */
	public static class BUHIN_HEIGHT {
		/** 小 */
		public static final String TEXTAREA = "textarea";
		/** 中 */
		public static final String TEXTAREA_MEDIUM = "textarea-medium";
		/** 大 */
		public static final String TEXTAREA_HEIGHT = "textarea-height";
	}

	/** エリア区分 */
	public static class AREA_KBN {
		/** 申請内容 */
		public static final String SHINSEI = "shinsei";
		/** 明細 */
		public static final String MEISAI = "meisai";
	}

	/** 使用フラグ */
	public static class SHIYOU_FLG {
		/** 使用しない */
		public static final String SHIYOU_SHINAI = "0";
		/** 使用する */
		public static final String SHIYOU_SURU = "1";
		/** マスター化する */
		public static final String MASTER = "2";
		/** 残高作成する */
		public static final String ZANDAKA = "3";
	}

	/** 社員コード連携 */
	public static class SHAIN_CD_RENKEI {
		/** あり */
		public static final String ARI = "1";
		/** なし */
		public static final String NASHI = "0";
	}

	/** 社員別支給一覧ソート対象カラム */
	public static class SHIKYU_ICHIRAN_SORT_COLUMN {
		/** 社員番号 */
		public static final String SHAIN = "SHAIN";
		/** 起票部門 */
		public static final String BUMON = "BUMON";
	}

	/** 社員別支給一覧ソート対象カラム(日付) */
	public static class SHIKYU_ICHIRAN_SORT_COLUMN_DATE {
		/** 起票日 */
		public static final String KIHYOUBI = "KIHYOUBI";
		/** 使用日 */
		public static final String SHIYOUBI = "SHIYOUBI";
	}

	/** 法人カード利用明細ソート対象カラム */
	public static class HOUJIN_MEISAI_SORT_COLUMN {
		/** 社員番号 */
		public static final String SHAIN = "SHAIN";
		/** 起票部門 */
		public static final String BUMON = "BUMON";
	}

	/** 承認ルート変更権限レベル */
	public static class SHOUNIN_ROUTE_HENKOU_LEVEL {
		/** 変更できない */
		public static final String DISABLED = "0";
		/** 追加のみ */
		public static final String ADD = "1";
		/** デフォルトルート処理権限変更可能 */
		public static final String HENKOU = "2";
		/** デフォルトルート削除可能 */
		public static final String SAKUJO = "3";
		/** 全変更可能 */
		public static final String ALL = "4";
	}

	/** 基本モデルコード */
	public static class KIHON_MODEL_CD {
		/** 承認者 */
		public static final String SHOUNI = "1";
		/** 閲覧者 */
		public static final String ETSURAN = "2";
		/** 後伺い */
		public static final String ATOUKAGAI = "3";
	}

	/** 凡例表示コード */
	public static class HANREI_HYOUJI_CD {
		/** 標準 */
		public static final String HYOUJUN = "1";
		/** 合議 */
		public static final String GOUGI = "2";
		/** 共通 */
		public static final String KYOUTSUU = "0";
	}

	/** 承認人数コード */
	public static class SHOUNIN_NINZUU_CD {
		/** 全員 */
		public static final String ZENIN = "1";
		/** いずれか一人 */
		public static final String HITORI = "2";
		/** 比率 */
		public static final String HIRITSU = "3";
	}

	/** 予算執行項目ID */
	public static class YOSAN_SHIKKOU_KOUMOKU_ID {
		/** 稟議金額 */
		public static final String RINGI_KINGAKU = "ringi_kingaku";
		/** 件名 */
		public static final String KENMEI = "kenmei";
		/** 内容 */
		public static final String NAIYOU_SHINSEI = "naiyou_shinsei";
		/** 収入金額合計 */
		public static final String SHUUNYUU_GOUKEI = "shuunyuu_kingaku_goukei";
		/** 支出金額合計 */
		public static final String SHISHUTSU_GOUKEI = "shishutsu_kingaku_goukei";
		/** 収支差額 */
		public static final String SHUUSHI_SAGAKU = "shuushi_sagaku";
		/** 収入部門 */
		public static final String SHUUNYUU_BUMON = "syuunyuu_bumon";
		/** 収入科目 */
		public static final String SHUUNYUU_KAMOKU = "syuunyuu_kamoku";
		/** 収入枝番 */
		public static final String SHUUNYUU_EDA = "syuunyuu_eda_num";
		/** 収入金額 */
		public static final String SHUUNYUU_KINGAKU = "syuunyuu_kingaku";
		/** 収入備考 */
		public static final String SHUUNYUU_BIKOU = "syuunyuu_bikou";
		/** 支出部門 */
		public static final String SHISHUTSU_BUMON = "shishutsu_bumon";
		/** 支出科目 */
		public static final String SHISHUTSU_KAMOKU = "shishutsu_kamoku";
		/** 支出枝番 */
		public static final String SHISHUTSU_EDA = "shishutsu_eda_num";
		/** 支出金額 */
		public static final String SHISHUTSU_KINGAKU = "shishutsu_kingaku";
		/** 支出備考 */
		public static final String SHISHUTSU_BIKOU = "shishutsu_bikou";
		/** 内容 */
		public static final String NAIYOU_MEISAI = "naiyou_meisai";
	}

	/** 予算執行対象 */
	public static class YOSAN_SHIKKOU_TAISHOU {
		/** 実施起案 */
		public static final String JISSHI_KIAN = "A";
		/** 支出起案 */
		public static final String SHISHUTSU_KIAN = "B";
		/** 支出依頼 */
		public static final String SHISHUTSU_IRAI = "C";
		/** 対象外 */
		public static final String TAISHOUGAI = "X";
	}

	/** ルート判定金額 */
	public static class ROUTE_HANTEI_KINGAKU {
		/** 分岐なし */
		public static final String BUNKI_NASHI = "0";
		/** 稟議金額 */
		public static final String RINGI_KINGAKU = "1";
		/** 支出合計 */
		public static final String SHISHUTSU_GOUKEI = "2";
		/** 収入又は支出 */
		public static final String SHISHUTSU_SHUUNYUU = "3";
		/** 収入合計 */
		public static final String SHUUNYUU_GOUKEI = "4";
	}

	/** 起案番号簿選択表示フラグ */
	public static class KIAN_BANGOU_BO_SENTAKU_HYOUJI {
		/** いいえ */
		public static final String NO = "0";
		/** はい */
		public static final String YES = "1";
		/** デフォルト */
		public static final String DEFAULT = "2";
	}

	/** 伝票検索表示フラグ */
	public static class DENPYOU_KENSAKU_HYOUJI {
		/** いいえ */
		public static final String NO = "0";
		/** はい */
		public static final String YES = "1";
	}

	/** 起案番号入力 */
	public static class KIAN_BANGOU_INPUT {
		/** あり */
		public static final String ARI = "";
		/** なし */
		public static final String NASHI = "1";
		/**  */
		public static final String MISHITEI = "2";
	}

	/** 起案番号終了 */
	public static class KIAN_BANGOU_END {
		/** はい */
		public static final String YES = "0";
		/** いいえ */
		public static final String NO = "";
		/**  */
		public static final String MISHITEI = "2";
	}

	/** 起案番号運用 */
	public static class KIAN_BANGOU_UNYOU {
		/** あり */
		public static final String ARI = "0";
		/** なし */
		public static final String NASHI = "1";
		/**  */
		public static final String MISHITEI = "";
	}

	/** 曜日 */
	public static class YOUBI {
		/** 月曜日 */
		public static final String MONDAY = "1";
		/** 火曜日 */
		public static final String TUESDAY = "2";
		/** 水曜日 */
		public static final String WEDNESDAY = "3";
		/** 木曜日 */
		public static final String THURSDAY = "4";
		/** 金曜日 */
		public static final String FRIDAY = "5";
		/** 土曜日 */
		public static final String SATURDAY = "6";
		/** 日曜日 */
		public static final String SUNDAY = "7";
	}

	/** 法人カード種別 */
	public static class HOUJIN_CARD_SHUBETSU {
		/** JCB */
		public static final String JCB = "1";
		/** 三井住友VISA */
		public static final String VISA = "2";
		/** 三菱UFJニコス */
		public static final String NICOS = "3";
		/** クレディセゾン */
		public static final String SAISON = "4";
		/** Bizプリカ */
		public static final String BIZPURICA = "51";
	}

	/** 小数点以下桁数 */
	public static class DECIMAL_POINT {
		/** 0 */
		public static final String ZERO = "0";
		/** 1 */
		public static final String ONE = "1";
		/** 2 */
		public static final String TWO = "2";
		/** 3 */
		public static final String THREE = "3";
		/** 4 */
		public static final String FOUR = "4";
		/** 5 */
		public static final String FIVE = "5";
	}

	/** 固定表示 */
	public static class KOTEI_HYOUJI {
		/** しない */
		public static final String NO = "0";
		/** する */
		public static final String YES = "1";
	}

	/** 手数料 */
	public static class TESUURYOU {
		/** 先方負担 */
		public static final String SENPOU_FUTAN = "1";
		/** 自社負担 */
		public static final String JISHA_FUTAN = "0";
	}

	/** 支払依頼方法 */
	public static class SHIHARAI_IRAI_HOUHOU {
		/** 振込 */
		public static final String FURIKOMI = "1";
		/** 自動引落 */
		public static final String JIDOUHIKIOTOSHI = "0";
	}

	/** 支払依頼種別 */
	public static class SHIHARAI_IRAI_SHUBETSU {
		/** 定期 */
		public static final String TEIKI = "1";
		/** その他 */
		public static final String SONOTA = "0";
	}

	/** 承認状況 */
	public static class SHOUNIN_JOUKYOU {
		/** 未承認 */
		public static final String MI_SHOUNIN = "1";
		/** 上長承認 */
		public static final String JOUCHOU_SHOUNIN = "2";
		/** 最終承認 */
		public static final String SAISHU_SHOUNIN = "3";
	}

	/** 支払依頼種別 */
	public static class SHUTSURYOKU_JOUKYOU {
		/** 未印刷 */
		public static final String MI_INSATSU = "1";
		/** 印刷済 */
		public static final String INSATSU_ZUMI = "2";
	}

	/** e文書 */
	public static class EBUNSHO_SHUBETSU {
		/** 領収書 */
		public static final String RYOSHUSHO = "0";
		/** 請求書 */
		public static final String SEIKYUSHO = "1";
		/** 契約書 */
		public static final String KEIYAKUSHO = "2";
		/** 納品書 */
		public static final String NOHINSHO = "3";
		/** 注文書 */
		public static final String CHUMONSHO = "4";
		/** 見積書 */
		public static final String MITSUMORISHO = "5";
	}

	/** 伝票区分（拠点入力） */
	public static class DENPYOU_KBN_KYOTEN {
		/** 振替伝票（拠点入力） */
		public static final String BUMON_FURIKAE_DENPYOU = "Z001";
		/** 現預金出納帳 */
		public static final String GENYOKIN_SUITOUCHOU = "Z002";
	}

	/** 交通手段数量タイプ */
	public static class KOUTSUU_SHUDAN_SUURYOU_TYPE {
		/** 入力しない */
		public static final String NYUURYOKU_SHINAI = "0";
		/** 数量入力 */
		public static final String SUURYOU_NYUURYOKU = "1";
		/** 数量と単価を入力 */
		public static final String SUURYOU_TANKA_NYUURYOKU = "2";
	}

	/** 事業者区分 */
	public static class JIGYOUSHA_KBN {
		/** 通常課税 */
		public static final String TSUJO_KAZEI = "0";
		/** 免税80% */
		public static final String MENZEI80 = "1";
	}

	/** 入力方式フラグ */
	public static class NYURYOKU_FLG {
		/** 税込入力 */
		public static final String ZEIKOMI = "0";
		/** 税抜入力 */
		public static final String ZEINUKI = "1";
	}

	/** インボイス伝票 */
	public static class INVOICE_DENPYOU {
		/** （インボイス） */
		public static final String INVOICE = "0";
		/** インボイス対応前 */
		public static final String OLD = "1";
	}

	/** 売上税額計算方式 */
	public static class URIAGEZEIGAKU_KEISAN {
		/** 割戻 */
		public static final String WARIMODOSHI = "0";
		/** 積上 */
		public static final String TSUMIAGE = "1";
		/** 併用 */
		public static final String HEIYOU = "2";
	}

	/** 仕入税額計算方式 */
	public static class SHIIREZEIGAKU_KEISAN {
		/** 割戻 */
		public static final String WARIMODOSHI = "0";
		/** 積上 */
		public static final String TSUMIAGE = "1";
	}

	/** 分離区分 */
	public static class BUNRI_KBN {
		/** 無し */
		public static final String NASHI = "0";
		/** 自動分離 */
		public static final String JIDOU_BUNRI = "1";
		/** 振替作成 */
		public static final String FURIKAE_SAKUSEI = "2";
		/** 税作成 */
		public static final String ZEI_SAKUSEI = "3";
		/**  */
		public static final String TAISHOUGAI_ZOKUSEINASHI = "9";
	}

	/** 仕入区分 */
	public static class SHIIRE_KBN {
		/**  */
		public static final String NASHI = "0";
		/** 課売 */
		public static final String KAURI = "1";
		/** 非売 */
		public static final String HIURI = "2";
		/** 共売 */
		public static final String KYOUURI = "3";
	}
}