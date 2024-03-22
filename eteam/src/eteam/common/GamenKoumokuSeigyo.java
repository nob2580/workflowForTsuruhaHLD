package eteam.common;

import java.util.List;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.database.abstractdao.GamenKoumokuSeigyoAbstractDao;
import eteam.database.dao.GamenKoumokuSeigyoDao;
import lombok.Getter;
import lombok.ToString;

/**
 * 画面項目制御テーブルの情報保持用
 * 伝票区分に所属する全ての項目情報を含む
 * 一部jspをpartialViewとして使用しているため各Actionの変数名を統一すること
 * メイン:ks,経費明細:ks1
 */
@Getter @ToString
public class GamenKoumokuSeigyo {
	
	/**
	 * コンストラクタ
	 * @param denpyouKbn 伝票区分
	 */
	public GamenKoumokuSeigyo(String denpyouKbn) {
		this.denpyouKbn = denpyouKbn;
		var denpyouKoumokuList = reloadKoumokuInfo(denpyouKbn);
		// DBに該当項目があれば項目制御クラスを生成する
		// 基本koumoku_id=プロパティ名とする。但しNameは実態とあっていないためカット。
		denpyouKoumokuList.forEach(x -> {
			switch (x.koumokuId) {
			case "bikou":
				bikou = new koumokuSeigyo(x);
			    break;
			case "bikou_nittou":
				bikouNittou = new koumokuSeigyo(x);
			    break;
			case "bikou_koutsuuhi":
				bikouKoutsuuhi = new koumokuSeigyo(x);
			    break;
			case "bunri_kbn":
				bunriKbn = new koumokuSeigyo(x);
				break;
			case "denpyou_date":
				denpyouDate = new koumokuSeigyo(x);
				break;
			case "furikomisaki_jouhou":
				furikomisakiJouhou = new koumokuSeigyo(x);
				break;
			case "futan_bumon_name":
				futanBumon = new koumokuSeigyo(x);
				break;
			case "gaika":
				gaika = new koumokuSeigyo(x);
				break;
			case "gaika_kingaku":
				gaikaKingaku = new koumokuSeigyo(x);
				break;
			case "gaika_zeigaku":
				gaikaZeigaku = new koumokuSeigyo(x);
				break;
			case "gaika_taika":
				gaikaTaika = new koumokuSeigyo(x);
				break;
			case "goukei_kingaku":
				goukeiKingaku = new koumokuSeigyo(x);
				break;
			case "heishu":
			case "heishu_cd":
				heishu = new koumokuSeigyo(x);
				break;
			case "hf1_cd":
				hf1 = new koumokuSeigyo(x);
				break;
			case "hf10_cd":
				hf10 = new koumokuSeigyo(x);
				break;
			case "hf2_cd":
				hf2 = new koumokuSeigyo(x);
				break;
			case "hf3_cd":
				hf3 = new koumokuSeigyo(x);
				break;
			case "hf4_cd":
				hf4 = new koumokuSeigyo(x);
				break;
			case "hf5_cd":
				hf5 = new koumokuSeigyo(x);
				break;
			case "hf6_cd":
				hf6 = new koumokuSeigyo(x);
				break;
			case "hf7_cd":
				hf7 = new koumokuSeigyo(x);
				break;
			case "hf8_cd":
				hf8 = new koumokuSeigyo(x);
				break;
			case "hf9_cd":
				hf9 = new koumokuSeigyo(x);
				break;
			case "hi_kazei_kingaku":
				hiKazeiKingaku = new koumokuSeigyo(x);
				break;
			case "hikiotoshibi":
				hikiotoshibi = new koumokuSeigyo(x);
				break;
			case "hosoku":
				hosoku = new koumokuSeigyo(x);
				break;
			case "houjin_card_riyou":
				houjinCardRiyou = new koumokuSeigyo(x);
				break;
			case "houmonsaki":
				houmonsaki = new koumokuSeigyo(x);
				break;
			case "jyousha_kukan":
				jyoushaKukan = new koumokuSeigyo(x);
				break;
			case "kaisha_tehai":
				kaishaTehai = new koumokuSeigyo(x);
				break;
			case "kaisha_tehai_goukei":
				kaishaTehaiGoukei = new koumokuSeigyo(x);
				break;
			case "kake_flg":
				kakeFlg = new koumokuSeigyo(x);
				break;
			case "kamoku_edaban_name":
				kamokuEdaban = new koumokuSeigyo(x);
				break;
			case "kamoku_name":
				kamoku = new koumokuSeigyo(x);
				break;
			case "kari_futan_bumon_name": // futan_bumon_nameの表記揺れ
				futanBumon = new koumokuSeigyo(x);
				break;
			case "kari_kamoku_edaban_name": // kamoku_edaban_nameの表記揺れ
				kamokuEdaban = new koumokuSeigyo(x);
				break;
			case "kari_kamoku_name": // kamoku_nameの表記揺れ
				kamoku = new koumokuSeigyo(x);
				break;
			case "kari_kazei_kbn":
				kazeiKbn = new koumokuSeigyo(x);
				break;
			case "karibarai_denpyou_id":
				karibaraiDenpyouId = new koumokuSeigyo(x);
				break;
			case "karibarai_kingaku":
				karibaraiKingaku = new koumokuSeigyo(x);
				break;
			case "karibarai_kingaku_sagaku":
				karibaraiKingakuSagaku = new koumokuSeigyo(x);
				break;
			case "karibarai_mishiyou_flg":
				karibaraiMishiyouFlg = new koumokuSeigyo(x);
				break;
			case "karibarai_on":
				karibaraiOn = new koumokuSeigyo(x);
				break;
			case "karibarai_sentaku":
				karibaraiSentaku = new koumokuSeigyo(x);
				break;
			case "karibarai_tekiyou":
				karibaraiTekiyou = new koumokuSeigyo(x);
				break;
			case "kazei_kbn":
				kazeiKbn = new koumokuSeigyo(x);
				break;
			case "kazei_kingaku":
				kazeiKingaku = new koumokuSeigyo(x);
				break;
			case "kikan":
				kikan = new koumokuSeigyo(x);
				break;
			case "kingaku":
				kingaku = new koumokuSeigyo(x);
				break;
			case "kingaku_goukei":
				kingakuGoukei = new koumokuSeigyo(x);
				break;
			case "kobetsu_kbn":
				kobetsuKbn = new koumokuSeigyo(x);
				break;
			case "kousaihi_shousai":
				kousaihiShousai = new koumokuSeigyo(x);
				break;
			case "koutsuu_shudan":
				koutsuuShudan = new koumokuSeigyo(x);
				break;
			case "kyuujitsu_nissuu":
				kyuujitsuNissuu = new koumokuSeigyo(x);
				break;
			case "meisai_kingaku":
				meisaiKingaku = new koumokuSeigyo(x);
				break;
			case "mokuteki":
				mokuteki = new koumokuSeigyo(x);
				break;
			case "naiyou_koutsuuhi":
				naiyouKoutsuuhi = new koumokuSeigyo(x);
				break;
			case "naiyou_nittou":
				naiyouNittou = new koumokuSeigyo(x);
				break;
			case "ninzuu":
				ninzuu = new koumokuSeigyo(x);
				break;
			case "nissuu":
				nissuu = new koumokuSeigyo(x);
				break;
			case "nyukin_goukei":
				nyukinGoukei = new koumokuSeigyo(x);
				break;
			case "oufuku_flg":
				oufukuFlg = new koumokuSeigyo(x);
				break;
			case "project_name":
				project = new koumokuSeigyo(x);
				break;
			case "rate":
				rate = new koumokuSeigyo(x);
				break;
			case "ryoushuusho_seikyuusho_tou_flg":
				ryoushuushoSeikyuushoTouFlg = new koumokuSeigyo(x);
				break;
			case "sashihiki_goukei":  // koumoku_idのほうが微妙なため例外
				sashihikiShiharaiGaku = new koumokuSeigyo(x);
				break;
			case "sashihiki_heishu_cd_kaigai":
				sashihikiHeishuCdKaigai = new koumokuSeigyo(x);
				break;
			case "sashihiki_rate_kaigai":
				sashihikiRateKaigai = new koumokuSeigyo(x);
				break;
			case "sashihiki_tanka_kaigai_gaika":
				sashihikiTankaKaigaiGaika = new koumokuSeigyo(x);
				break;
			case "sashihiki_tanka_kaigai":
				sashihikiTankaKaigai = new koumokuSeigyo(x);
				break;
			case "sashihiki_kingaku":
				sashihikiKingaku = new koumokuSeigyo(x);
				break;
			case "sashihiki_num":
				sashihikiNum = new koumokuSeigyo(x);
				break;
			case "sashihiki_shikyuu_kingaku":
				sashihikiShikyuuKingaku = new koumokuSeigyo(x);
				break;
			case "segment_name_ryakushiki":
				segment = new koumokuSeigyo(x);
				break;
			case "seisan_yoteibi":
				seisanYoteibi = new koumokuSeigyo(x);
				break;
			case "seisankikan":
				seisankikan = new koumokuSeigyo(x);
				break;
			case "seisankikan_jikoku":
				seisankikanJikoku = new koumokuSeigyo(x);
				break;
			case "shiharai_goukei":
				shiharaiGoukei = new koumokuSeigyo(x);
				break;
			case "shiharai_kigen":
				shiharaiKigen = new koumokuSeigyo(x);
				break;
			case "shiharai_kingaku":
				shiharaiKingaku = new koumokuSeigyo(x);
				break;
			case "shiharai_kingaku_goukei":
				shiharaiKingakuGoukei = new koumokuSeigyo(x);
				break;
			case "shiharai_kingaku_goukei_8_percent": // 表記は仮。
				shiharaiKingakuGoukei8Percent = new koumokuSeigyo(x);
				break;
			case "shiharai_kingaku_goukei_10_percent":
				shiharaiKingakuGoukei10Percent = new koumokuSeigyo(x);
				break;
			case "shiharaihouhou":
				shiharaiHouhou = new koumokuSeigyo(x);
				break;
			case "shiharaikiboubi":
				shiharaiKiboubi = new koumokuSeigyo(x);
				break;
			case "shiharaisaki_name":
				shiharaisaki = new koumokuSeigyo(x);
				break;
			case "shiire_kbn":
				shiireKbn = new koumokuSeigyo(x);
				break;
			case "shinsei_kingaku":
				shinseiKingaku = new koumokuSeigyo(x);
				break;
			case "shiyou_kaishibi":
				shiyouKaishibi = new koumokuSeigyo(x);
				break;
			case "shiyou_kikan_kbn":
				shiyouKikanKbn = new koumokuSeigyo(x);
				break;
			case "shiyou_shuuryoubi":
				shiyouShuuryoubi = new koumokuSeigyo(x);
				break;
			case "shiyoubi":
				shiyoubi = new koumokuSeigyo(x);
				break;
			case "shouhizeigaku":
				shouhizeigaku = new koumokuSeigyo(x);
				break;
			case "shouhizeigaku_8_percent": // 表記は仮。
				shouhizeigaku8Percent = new koumokuSeigyo(x);
				break;
			case "shouhizeigaku_10_percent":
				shouhizeigaku10Percent = new koumokuSeigyo(x);
				break;
			case "shouhizeitaishou_bunri_kbn":
				shouhizeitaishouBunriKbn = new koumokuSeigyo(x);
				break;
			case "shouhizeitaishou_shiire_kbn":
				shouhizeitaishouShiireKbn = new koumokuSeigyo(x);
				break;
			case "shouhizeitaishou_kamoku_name":
				shouhizeitaishouKamoku = new koumokuSeigyo(x);
				break;
			case "shouhizeitaishou_kazei_kbn":
				shouhizeitaishouKazeiKbn = new koumokuSeigyo(x);
				break;
			case "shouhizeitaishou_zeiritsu":
				shouhizeitaishouZeiritsu = new koumokuSeigyo(x);
				break;
			case "shouhyou_shorui_flg":
				shouhyouShoruiFlg = new koumokuSeigyo(x);
				break;
			case "shubetsu1":
				shubetsu1 = new koumokuSeigyo(x);
				break;
			case "shubetsu2":
				shubetsu2 = new koumokuSeigyo(x);
				break;
			case "shucchou_chuushi_flg":
				shucchouChuushiFlg = new koumokuSeigyo(x);
				break;
			case "shucchou_kbn":
				shucchouKbn = new koumokuSeigyo(x);
				break;
			case "shukkin_goukei":
				shukkinGoukei = new koumokuSeigyo(x);
				break;
			case "sousai_goukei":
				sousaiGoukei = new koumokuSeigyo(x);
				break;
			case "tanka":
				tanka = new koumokuSeigyo(x);
				break;
			case "taika":
				taika = new koumokuSeigyo(x);
				break;
			case "tekiyou":
				tekiyou = new koumokuSeigyo(x);
				break;
			case "torihiki_name":
				torihiki = new koumokuSeigyo(x);
				break;
			case "torihikisaki_name_ryakushiki":
				torihikisaki = new koumokuSeigyo(x);
				break;
			case "toujitsu_zandaka":
				toujitsuZandaka = new koumokuSeigyo(x);
				break;
			case "uchi_houjin_card_riyou_goukei":
				uchiHoujinCardRiyouGoukei = new koumokuSeigyo(x);
				break;
			case "uf1_cd":
				uf1 = new koumokuSeigyo(x);
				break;
			case "uf10_cd":
				uf10 = new koumokuSeigyo(x);
				break;
			case "uf2_cd":
				uf2 = new koumokuSeigyo(x);
				break;
			case "uf3_cd":
				uf3 = new koumokuSeigyo(x);
				break;
			case "uf4_cd":
				uf4 = new koumokuSeigyo(x);
				break;
			case "uf5_cd":
				uf5 = new koumokuSeigyo(x);
				break;
			case "uf6_cd":
				uf6 = new koumokuSeigyo(x);
				break;
			case "uf7_cd":
				uf7 = new koumokuSeigyo(x);
				break;
			case "uf8_cd":
				uf8 = new koumokuSeigyo(x);
				break;
			case "uf9_cd":
				uf9 = new koumokuSeigyo(x);
				break;
			case "user_name":
				userName = new koumokuSeigyo(x);
				break;
			case "yoteibi":
				yoteibi = new koumokuSeigyo(x);
				break;
			case "zeiritsu":
				zeiritsu = new koumokuSeigyo(x);
				break;
			case "zenjitsu_zandaka":
				zenjitsuZandaka = new koumokuSeigyo(x);
				break;
			case "haya_yasu_raku":
				hayaYasuRaku = new koumokuSeigyo(x);
				break;
			case "nyuryoku_houshiki":
				nyuryokuHoushiki = new koumokuSeigyo(x);
				break;
			case "jigyousha_kbn":
				jigyoushaKbn = new koumokuSeigyo(x);
				jigyoushaKbn.hissuFlg = false; // 事業者区分の必須フラグは常時false
				break;
			case "jigyousha_no":
				jigyoushaNo = new koumokuSeigyo(x);
				break;
			case "zeinuki_kingaku":
				zeinukiKingaku = new koumokuSeigyo(x);
				break;
			case "zeinuki_kingaku_8_percent": // 表記は仮。
				zeinukiKingaku8Percent = new koumokuSeigyo(x);
				break;
			case "zeinuki_kingaku_10_percent":
				zeinukiKingaku10Percent = new koumokuSeigyo(x);
				break;
			case "uriagezeigaku_keisan":
				uriagezeigakuKeisan = new koumokuSeigyo(x);
				break;
			default:
				throw new IllegalArgumentException("画面項目制御の項目IDが不正です" + x.koumokuId);
			}
		  });
	}

	/** 伝票区分 */public String denpyouKbn;
	/** 備考 */ public koumokuSeigyo bikou;
	/** 備考(交通費) */ public koumokuSeigyo bikouKoutsuuhi;
	/** 備考(日当・宿泊費等) */ public koumokuSeigyo bikouNittou;
	/** 伝票日 */ public koumokuSeigyo denpyouDate;
	/** 分離区分 */ public koumokuSeigyo bunriKbn = new koumokuSeigyo("分離区分"); //DB上拠点にしかないため仮置き…
	/** 振込先 */ public koumokuSeigyo furikomisakiJouhou;
	/** 負担部門 */ public koumokuSeigyo futanBumon;
	/** 外貨 */ public koumokuSeigyo gaika;
	/** 外貨金額 */ public koumokuSeigyo gaikaKingaku;
	/** 明細金額合計 */ public koumokuSeigyo goukeiKingaku;
	/** 外貨税額 */public koumokuSeigyo gaikaZeigaku;
	/** 外貨対価 */public koumokuSeigyo gaikaTaika;
	/** 幣種 */ public koumokuSeigyo heishu;
	/** HF1 */ public koumokuSeigyo hf1;
	/** HF2 */ public koumokuSeigyo hf2;
	/** HF3 */ public koumokuSeigyo hf3;
	/** HF4 */ public koumokuSeigyo hf4;
	/** HF5 */ public koumokuSeigyo hf5;
	/** HF6 */ public koumokuSeigyo hf6;
	/** HF7 */ public koumokuSeigyo hf7;
	/** HF8 */ public koumokuSeigyo hf8;
	/** HF9 */ public koumokuSeigyo hf9;
	/** HF10 */ public koumokuSeigyo hf10;
	/** 非課税金額 */ public koumokuSeigyo hiKazeiKingaku;
	/** 引落日 */ public koumokuSeigyo hikiotoshibi;
	/** 補足 */ public koumokuSeigyo hosoku;
	/** 法人カード利用 */ public koumokuSeigyo houjinCardRiyou;
	/** 出張先・訪問先 */ public koumokuSeigyo houmonsaki;
	/** 乗車区間 */ public koumokuSeigyo jyoushaKukan;
	/** 会社手配 */ public koumokuSeigyo kaishaTehai;
	/** 会社手配合計 */ public koumokuSeigyo kaishaTehaiGoukei;
	/** 掛け */ public koumokuSeigyo kakeFlg;
	/** 勘定科目枝番 */ public koumokuSeigyo kamokuEdaban;
	/** 勘定科目 */ public koumokuSeigyo kamoku;
	/** 伝票ID */ public koumokuSeigyo karibaraiDenpyouId;
	/** 仮払金額 */ public koumokuSeigyo karibaraiKingaku;
	/** 精算金額との差額 */ public koumokuSeigyo karibaraiKingakuSagaku;
	/** 仮払金未使用 */ public koumokuSeigyo karibaraiMishiyouFlg;
	/** 仮払 */ public koumokuSeigyo karibaraiOn;
	/** 伺い(仮払)選択 */ public koumokuSeigyo karibaraiSentaku;
	/** 摘要 */ public koumokuSeigyo karibaraiTekiyou;
	/** 課税区分 */ public koumokuSeigyo kazeiKbn;
	/** 課税金額 */ public koumokuSeigyo kazeiKingaku;
	/** 期間 */ public koumokuSeigyo kikan;
	/** 金額 */ public koumokuSeigyo kingaku;
	/** 合計金額 */ public koumokuSeigyo kingakuGoukei;
	/** 個別区分 */ public koumokuSeigyo kobetsuKbn;
	/** 交際費詳細 */ public koumokuSeigyo kousaihiShousai;
	/** 交通手段 */ public koumokuSeigyo koutsuuShudan;
	/** 休日日数 */ public koumokuSeigyo kyuujitsuNissuu;
	/** 金額 */ public koumokuSeigyo meisaiKingaku;
	/** 目的 */ public koumokuSeigyo mokuteki;
	/** 内容(交通費) */ public koumokuSeigyo naiyouKoutsuuhi;
	/** 内容(日当・宿泊費等) */ public koumokuSeigyo naiyouNittou;
	/** 人数 */ public koumokuSeigyo ninzuu;
	/** 日数 */ public koumokuSeigyo nissuu;
	/** 入金合計 */public koumokuSeigyo nyukinGoukei;
	/** 往復 */ public koumokuSeigyo oufukuFlg;
	/** プロジェクト */ public koumokuSeigyo project;
	/** セグメント */ public koumokuSeigyo segment;
	/** レート */ public koumokuSeigyo rate;
	/** 領収書・請求書等 */ public koumokuSeigyo ryoushuushoSeikyuushoTouFlg;
	/** 差引幣種 */ public koumokuSeigyo sashihikiHeishuCdKaigai;
	/** 差引レート */ public koumokuSeigyo sashihikiRateKaigai;
	/** 差引単価（外貨）*/ public koumokuSeigyo sashihikiTankaKaigaiGaika;
	/** 差引単価（邦貨）*/ public koumokuSeigyo sashihikiTankaKaigai;
	/** 金額 */ public koumokuSeigyo sashihikiKingaku;
	/**  */ public koumokuSeigyo sashihikiNum;
	/** 差引支給金額 */ public koumokuSeigyo sashihikiShikyuuKingaku;
	/** 差引支払額 */ public koumokuSeigyo sashihikiShiharaiGaku;
	/** 精算予定日 */ public koumokuSeigyo seisanYoteibi;
	/** 精算期間 */ public koumokuSeigyo seisankikan;
	/** 精算期間時刻 */ public koumokuSeigyo seisankikanJikoku;
	/** 支払合計 */ public koumokuSeigyo shiharaiGoukei;
	/** 支払期限 */ public koumokuSeigyo shiharaiKigen;
	/** 支払金額 */ public koumokuSeigyo shiharaiKingaku;
	/** 支払金額合計 */ public koumokuSeigyo shiharaiKingakuGoukei = new koumokuSeigyo("支払金額合計"); // インボイス関連分について、DBに追加されるまでの仮置きデフォルトを用意しておく。
	/** 支払金額合計（10%） */ public koumokuSeigyo shiharaiKingakuGoukei10Percent = new koumokuSeigyo("支払金額（10%）");
	/** 支払金額合計（*8%） */ public koumokuSeigyo shiharaiKingakuGoukei8Percent = new koumokuSeigyo("支払金額（*8%）");
	/** 方法 */ public koumokuSeigyo shiharaiHouhou;
	/** 希望日 */ public koumokuSeigyo shiharaiKiboubi;
	/** 支払先 */ public koumokuSeigyo shiharaisaki;
	/** 仕入区分 */public koumokuSeigyo shiireKbn = new koumokuSeigyo("仕入区分"); //DB上拠点にしかないため仮置き…
	/** 申請金額 */ public koumokuSeigyo shinseiKingaku;
	/** 使用開始日 */ public koumokuSeigyo shiyouKaishibi;
	/** 使用期間 */ public koumokuSeigyo shiyouKikanKbn;
	/** 使用終了日 */ public koumokuSeigyo shiyouShuuryoubi;
	/** 使用日 */ public koumokuSeigyo shiyoubi;
	/** 消費税額 */ public koumokuSeigyo shouhizeigaku = new koumokuSeigyo("消費税額");
	/** 消費税額（10%）*/ public koumokuSeigyo shouhizeigaku10Percent = new koumokuSeigyo("消費税額（10%）");
	/** 消費税額（*8%） */ public koumokuSeigyo shouhizeigaku8Percent = new koumokuSeigyo("消費税額（*8%）");
	/** 消費税対象科目 */ public koumokuSeigyo shouhizeitaishouKamoku;
	/** 消費税対象課税区分 */ public koumokuSeigyo shouhizeitaishouKazeiKbn;
	/** 消費税対象税率 */ public koumokuSeigyo shouhizeitaishouZeiritsu;
	/** 消費税対象分離区分 */public koumokuSeigyo shouhizeitaishouBunriKbn;
	/** 消費税対象仕入区分 */public koumokuSeigyo shouhizeitaishouShiireKbn;
	/** 領収書・請求書等 */ public koumokuSeigyo shouhyouShoruiFlg;
	/** 種別 */ public koumokuSeigyo shubetsu1;
	/** 種別 */ public koumokuSeigyo shubetsu2;
	/** 出張区分 */ public koumokuSeigyo shucchouKbn;
	/** 出張中止 */ public koumokuSeigyo shucchouChuushiFlg;
	/** 出金合計 */public koumokuSeigyo shukkinGoukei;
	/** 相殺合計 */ public koumokuSeigyo sousaiGoukei;
	/** 単価 */ public koumokuSeigyo tanka;
	/** 対価 */ public koumokuSeigyo taika;
	/** 摘要 */ public koumokuSeigyo tekiyou;
	/** 取引 */ public koumokuSeigyo torihiki;
	/** 取引先 */ public koumokuSeigyo torihikisaki;
	/** 当日残高 */public koumokuSeigyo toujitsuZandaka;
	/** 法人カード利用合計 */ public koumokuSeigyo uchiHoujinCardRiyouGoukei;
	/** UF1 */ public koumokuSeigyo uf1;
	/** UF2 */ public koumokuSeigyo uf2;
	/** UF3 */ public koumokuSeigyo uf3;
	/** UF4 */ public koumokuSeigyo uf4;
	/** UF5 */ public koumokuSeigyo uf5;
	/** UF6 */ public koumokuSeigyo uf6;
	/** UF7 */ public koumokuSeigyo uf7;
	/** UF8 */ public koumokuSeigyo uf8;
	/** UF9 */ public koumokuSeigyo uf9;
	/** UF10 */ public koumokuSeigyo uf10;
	/** 使用者 */ public koumokuSeigyo userName;
	/** 予定日 */ public koumokuSeigyo yoteibi;
	/** 消費税率 */ public koumokuSeigyo zeiritsu;
	/** 前日残高 */public koumokuSeigyo zenjitsuZandaka;
	/** 早安楽 */ public koumokuSeigyo hayaYasuRaku;
	/** 入力方式 */ public koumokuSeigyo nyuryokuHoushiki;
	/** 税額計算方式 */ public koumokuSeigyo uriagezeigakuKeisan;
	/** 事業者区分 */ public koumokuSeigyo jigyoushaKbn = new koumokuSeigyo("事業者区分");
	/** 事業者登録番号 */ public koumokuSeigyo jigyoushaNo = new koumokuSeigyo("適格請求書発行事業者登録番号");
	/** 税抜金額 */ public koumokuSeigyo zeinukiKingaku;
	/** 税抜金額（10%） */ public koumokuSeigyo zeinukiKingaku10Percent = new koumokuSeigyo("税抜金額（10%）");
	/** 税抜金額（*8%） */ public koumokuSeigyo zeinukiKingaku8Percent = new koumokuSeigyo("税抜金額（*8%）");
	
	/**
	 * 項目制御クラス
	 */
	@Getter @ToString
	public class koumokuSeigyo
	{
		/**
		 * 項目名
		 */
		String name;
		/**
		 * 表示フラグ
		 */
		Boolean hyoujiFlg;
		/**
		 * 必須フラグ
		 */
		Boolean hissuFlg;
		/**
		 * 帳票表示フラグ
		 */
		Boolean pdfHyoujiFlg;
		/**
		 * 帳票コード印字フラグ
		 */
		Boolean codeOutputFlg;
		
		/** Map（念のため持たせる） */
		GMap map;

		/**
		 * default constructor
		 * @param name 名称
		 */
		public koumokuSeigyo(String name)
		{
			this.hyoujiFlg = true;
			this.hissuFlg = false;
			this.name = name;
			this.pdfHyoujiFlg = true;
			this.codeOutputFlg = true;
			this.map = new GMap();
		}
		
		/**
		 * @param x 画面項目制御レコード
		 */
		public koumokuSeigyo(eteam.database.dto.GamenKoumokuSeigyo x) {
			this.hyoujiFlg = ("1".equals(x.hyoujiFlg));
			this.hissuFlg = ("1".equals(x.hissuFlg));
			this.name = x.koumokuName.toString();
			this.pdfHyoujiFlg = ("1".equals(x.pdfHyoujiFlg));
			this.codeOutputFlg = ("1".equals(x.codeOutputFlg));
			this.map = x.map;
		}

		/**
		 * 必須フラグ(1/0の文字列)を返す
		 * @return 必須フラグ
		 */
		public String getHissuFlgS() {
			return hissuFlg ? "1" : "0";
		}

		
		/**
		 * インボイス考慮の必須フラグを返す
		 * @param invoiceDenpyou 伝票単位フラグ
		 * @param invoiceStartFlg 全体設定開始フラグ
		 * @return 必須フラグ
		 */
		public String getInvoiceHissuFlgS(String invoiceDenpyou, String invoiceStartFlg)
		{
			return "0".equals(invoiceDenpyou) && "1".equals(invoiceStartFlg)
				? this.getHissuFlgS()
				: "0";
		}
		
		/**
		 * 帳票コード出力設定に応じて出力内容を編集して返す
		 * @param code コード
		 * @param koumokuName 名称
		 * @param delimiter 区切り文字
		 * @return 編集後の項目内容
		 */
		public String getCodeSeigyoValue(String code, String koumokuName, String delimiter)
		{
			if (koumokuName == null || koumokuName.isEmpty())
			{
				return "";
			}
			
			if (codeOutputFlg)
			{
				return code + delimiter + koumokuName;
			}
			else
			{
				return koumokuName;
			}
		}
	}

	/**
	 * 画面項目情報を取得
	 * ※当初スレッドローカルで処理していたが、伝票区分単位の取得になったため都度
	 * @param denpyouKbn 伝票区分
	 * @return 画面項目制御情報
	 */
	protected static List<eteam.database.dto.GamenKoumokuSeigyo> reloadKoumokuInfo(String denpyouKbn) {
		try(EteamConnection connection = EteamConnection.connect()) {
			GamenKoumokuSeigyoAbstractDao dao = EteamContainer.getComponent(GamenKoumokuSeigyoDao.class, connection);

			return dao.load(denpyouKbn);
		}
	}
}
