package eteam.gyoumu.kaikei.common;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 仕訳抽出テーブルのレコード
 */
@Getter @Setter @ToString
public class ShiwakeData implements Cloneable {

//＜未使用項目＞・・・個々て定義した固定値のままINSERTするだけのどうでもいい項目たち
	//伝票情報
	/** 整理月フラグ */ String SEIRI = "0";
	/** 起票年月日 */ Date KYMD;
	/** 起票部門コード */ String KBMN = "";
	/** 起票者コード */ String KUSR = "";
	/** 承認グループNo. */ String SGNO = "0";
	//借方情報
	/** 借方　科目コード */ String RKMK = "";
	/** 借方　工事コード */ String RKOJ = "";
	/** 借方　工種コード */ String RKOS = "";
	/** 借方　業種区分 */ String RGYO = "";
	/** 借方　仕入区分 */ String RSRE = "";
	/** 借方　併用売上税額計算方式 */ String RURIZEIKEISAN = "";
	/** 借方　仕入税額控除経過措置割合 */ String RMENZEIKEIKA = "";
	//貸方情報
	/** 貸方　科目コード */ String SKMK = "";
	/** 貸方　工事コード */ String SKOJ = "";
	/** 貸方　工種コード */ String SKOS = "";
	/** 貸方　業種区分 */ String SGYO = "";
	/** 貸方　仕入区分 */ String SSRE = "";
	/** 貸方　併用売上税額計算方式 */ String SURIZEIKEISAN = "";
	/** 貸方　仕入税額控除経過措置割合 */ String SMENZEIKEIKA = "";
	//消費税対象
	/** 消費税対象科目コード */ String ZKMK = "";
	/** 消費税対象科目税率 */ BigDecimal ZRIT;
	/** 消費税対象科目　課税区分 */ String ZZKB = "";
	/** 消費税対象科目　業種区分 */ String ZGYO = "";
	/** 消費税対象科目　仕入区分 */ String ZSRE = "";
	/** 消費税対象科目　軽減税率区分 */ String ZKEIGEN = "";
	/** 消費税対象科目　併用売上税額計算方式 */ String ZURIZEIKEISAN = "";
	/** 消費税対象科目　仕入税額控除経過措置割合 */ String ZMENZEIKEIKA = "";
	//仕訳共通
	/** 対価金額 */ BigDecimal EXVL = new BigDecimal(0);
	/** 支払区分 */ String SKBN = "";
	/** 支払期日 */ Date SKIZ;
	/** 回収日 */ Date UYMD;
	/** 入金区分 */ String UKBN = "";
	/** 回収期日 */ Date UKIZ;
	/** 店券フラグ */ String STEN = "0";
	/** 消込コード */ String DKEC = "";
	/** 入力者コード */ String FUSR = "";
	/** 付箋番号 */ String FSEN = "0";
	/** 貸借別摘要フラグ */ String TKFLG = "0";
// /** 分離区分 */ String BUNRI = "";
	/** 幣種 */ String HEIC = "";
	/** レート */ String RATE = "";
	/** 外貨対価金額 */ String GEXVL = "";
	/** 外貨金額 */ String GVALU = "";
	/** 行区切り */ String GSEP = "";
	/** 仕訳データシリアルNo */ long SerialNo;

//＜使用項目＞・・・各種抽出BATの中で動的にセットされる項目たち
	/** 伝票情報 */ ShiwakeDataMain main = new ShiwakeDataMain();
	/** 借方情報 */ ShiwakeDataRS r = new ShiwakeDataRS();
	/** 貸方情報 */ ShiwakeDataRS s = new ShiwakeDataRS();
	/** 仕訳共通 */ ShiwakeDataCom com = new ShiwakeDataCom();

	/**
	 * 借方セット
	 * @param r 借方
	 * @param dId 伝票IDを借方に反映するなら該当の伝票IDを
	 * @param shainNo 社員IDを借方に反映するなら該当の社員NOを
	 */
	public void setKari(ShiwakeDataRS r, String dId, String shainNo){
		this.r = r;
		if(dId != null){
			r.setDidFlg(true);
			r.setDid(dId);
		}else{
			r.setDidFlg(false);
			r.setDid(null);;
		}
		if(shainNo != null){
			r.setShainCdFlg(true);
			r.setShainNo(shainNo);
		}else{
			r.setShainCdFlg(false);
			r.setShainNo(null);;
		}
	}
	/**
	 * 貸方セット
	 * @param s 貸方
	 * @param dId 伝票IDを貸方に反映するなら該当の伝票IDを
	 * @param shainNo 社員IDを貸方に反映するなら該当の社員NOを
	 * @param houjin 法人カード識別用番号
	 */
	public void setKashi(ShiwakeDataRS s, String dId, String shainNo, String houjin){
		this.s = s;
		if(dId != null){
			s.setDidFlg(true);
			s.setDid(dId);
		}else{
			s.setDidFlg(false);
			s.setDid(null);
		}
		if(shainNo != null){
			s.setShainCdFlg(true);
			s.setShainNo(shainNo);
		}else{
			s.setShainCdFlg(false);
			s.setShainNo(null);
		}
		if(houjin != null){
			s.setHoujinFlg(true);
			s.setHoujin(houjin);
		}else{
			s.setHoujinFlg(false);
			s.setHoujin(null);
		}
	}
	
	@Override
	public ShiwakeData clone() {
		try {
			ShiwakeData ret = (ShiwakeData)super.clone();
			ret.main = main.clone();
			ret.r = r.clone();
			ret.s = s.clone();
			ret.com = com.clone();
			return ret;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
