package eteam.gyoumu.kaikei;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** CSVファイル情報（登録内容） */
@Getter @Setter @ToString
public class IkkatsuTourokuTorihikiCSVUploadInfo implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;
	/** csv内のnumbering */
	String number;
	
	/** 伝票区分 */
	String denpyouKbn;
	/** 仕訳枝番号 */
	String shiwakeEdano;
	/** 削除フラグ */
	String deleteFlg;
	/** 有効期限開始日 */
	String yuukouKigenFrom;
	/** 有効期限終了日 */
	String yuukouKigenTo;
	/** 分類1 */
	String bunrui1;
	/** 分類2 */
	String bunrui2;
	/** 分類3 */
	String bunrui3;
	/** 取引名 */
	String torihikiName;
	/** 摘要フラグ */
	String tekiyouFlg;
	/** 摘要 */
	String tekiyou;
	/** デフォルト表示フラグ */
	String defaultHyoujiFlg;
	/** 交際費表示フラグ */
	String kousaihiHyoujiFlg;
	/** 人数項目表示フラグ */
	String kousaihiNinzuuRiyouFlg;
	/** 交際費基準額 */
	String kousaihiKijungaku;
	/** 交際費チェック方法 */
	String kousaihiCheckHouhou;
	/** 交際費チェック後登録許可 */
	String kousaihiCheckResult;
	/** 掛けフラグ */
	String kakeFlg;
	/** 表示順 */
	String hyoujiJun;
	/** 社員コード連携フラグ */
	String shainCdRenkeiFlg;
	/** 財務枝番コード連携フラグ */
	String zaimuEdabanRenkeiFlg;
	/** 借方負担部門コード（仕訳パターン） */
	String kariFutanBumonCd;
	/** 借方科目コード（仕訳パターン） */
	String kariKamokuCd;
	/** 借方科目枝番コード（仕訳パターン） */
	String kariKamokuEdabanCd;
	/** 借方取引先コード（仕訳パターン） */
	String kariTorihikisakiCd;
	/** 借方プロジェクトコード（仕訳パターン） */
	String kariProjectCd;
	/** 借方セグメントコード（仕訳パターン） */
	String kariSegmentCd;
	/** 借方UFコード1 */
	String kariUf1Cd;
	/** 借方UFコード2 */
	String kariUf2Cd;
	/** 借方UFコード3 */
	String kariUf3Cd;
	/** 借方UFコード4 */
	String kariUf4Cd;
	/** 借方UFコード5 */
	String kariUf5Cd;
	/** 借方UFコード6 */
	String kariUf6Cd;
	/** 借方UFコード7 */
	String kariUf7Cd;
	/** 借方UFコード8 */
	String kariUf8Cd;
	/** 借方UFコード9 */
	String kariUf9Cd;
	/** 借方UFコード10 */
	String kariUf10Cd;
	/** 借方UFコード(固定)1 */
	String kariUfKotei1Cd;
	/** 借方UFコード(固定)2 */
	String kariUfKotei2Cd;
	/** 借方UFコード(固定)3 */
	String kariUfKotei3Cd;
	/** 借方UFコード(固定)4 */
	String kariUfKotei4Cd;
	/** 借方UFコード(固定)5 */
	String kariUfKotei5Cd;
	/** 借方UFコード(固定)6 */
	String kariUfKotei6Cd;
	/** 借方UFコード(固定)7 */
	String kariUfKotei7Cd;
	/** 借方UFコード(固定)8 */
	String kariUfKotei8Cd;
	/** 借方UFコード(固定)9 */
	String kariUfKotei9Cd;
	/** 借方UFコード(固定)10 */
	String kariUfKotei10Cd;
	/** 借方課税区分（仕訳パターン） */
	String kariKazeiKbn;
	/** 借方消費税率（仕訳パターン） */
	String kariZeiritsu;
	/** 借方軽減税率区分（仕訳パターン） */
	String kariKeigenZeiritsuKbn;
	/** (借方)分離区分 */
	String kariBunriKbn;
	/** (借方)仕入区分 */
	String kariShiireKbn;
	
	/** 貸方負担部門コード１（仕訳パターン） */
	String kashiFutanBumonCd1;
	/** 貸方科目コード１（仕訳パターン） */
	String kashiKamokuCd1;
	/** 貸方科目枝番コード１（仕訳パターン） */
	String kashiKamokuEdabanCd1;
	/** 貸方取引先コード１（仕訳パターン） */
	String kashiTorihikisakiCd1;
	/** 貸方１プロジェクトコード（仕訳パターン） */
	String kashiProjectCd1;
	/** 貸方１セグメントコード（仕訳パターン） */
	String kashiSegmentCd1;
	/** 貸方１UFコード1 */
	String kashiUf1Cd1;
	/** 貸方１UFコード2 */
	String kashiUf2Cd1;
	/** 貸方１UFコード3 */
	String kashiUf3Cd1;
	/** 貸方１UFコード4 */
	String kashiUf4Cd1;
	/** 貸方１UFコード5 */
	String kashiUf5Cd1;
	/** 貸方１UFコード6 */
	String kashiUf6Cd1;
	/** 貸方１UFコード7 */
	String kashiUf7Cd1;
	/** 貸方１UFコード8 */
	String kashiUf8Cd1;
	/** 貸方１UFコード9 */
	String kashiUf9Cd1;
	/** 貸方１UFコード10 */
	String kashiUf10Cd1;
	/** 貸方１UFコード(固定)1 */
	String kashiUfKotei1Cd1;
	/** 貸方１UFコード(固定)2 */
	String kashiUfKotei2Cd1;
	/** 貸方１UFコード(固定)3 */
	String kashiUfKotei3Cd1;
	/** 貸方１UFコード(固定)4 */
	String kashiUfKotei4Cd1;
	/** 貸方１UFコード(固定)5 */
	String kashiUfKotei5Cd1;
	/** 貸方１UFコード(固定)6 */
	String kashiUfKotei6Cd1;
	/** 貸方１UFコード(固定)7 */
	String kashiUfKotei7Cd1;
	/** 貸方１UFコード(固定)8 */
	String kashiUfKotei8Cd1;
	/** 貸方１UFコード(固定)9 */
	String kashiUfKotei9Cd1;
	/** 貸方１UFコード(固定)10 */
	String kashiUfKotei10Cd1;
	/** 貸方課税区分１（仕訳パターン） */
	String kashiKazeiKbn1;
	/** (貸方1)分離区分 */
	String kashiBunriKbn1;
	/** (貸方1)仕入区分 */
	String kashiShiireKbn1;
	
	/** 貸方負担部門コード２（仕訳パターン） */
	String kashiFutanBumonCd2;
	/** 貸方取引先コード２（仕訳パターン） */
	String kashiTorihikisakiCd2;
	/** 貸方科目コード２（仕訳パターン） */
	String kashiKamokuCd2;
	/** 貸方科目枝番コード２（仕訳パターン） */
	String kashiKamokuEdabanCd2;
	/** 貸方２プロジェクトコード（仕訳パターン） */
	String kashiProjectCd2;
	/** 貸方２セグメントコード（仕訳パターン） */
	String kashiSegmentCd2;
	/** 貸方２UFコード1 */
	String kashiUf1Cd2;
	/** 貸方２UFコード2 */
	String kashiUf2Cd2;
	/** 貸方２UFコード3 */
	String kashiUf3Cd2;
	/** 貸方２UFコード4 */
	String kashiUf4Cd2;
	/** 貸方２UFコード5 */
	String kashiUf5Cd2;
	/** 貸方２UFコード6 */
	String kashiUf6Cd2;
	/** 貸方２UFコード7 */
	String kashiUf7Cd2;
	/** 貸方２UFコード8 */
	String kashiUf8Cd2;
	/** 貸方２UFコード9 */
	String kashiUf9Cd2;
	/** 貸方２UFコード10 */
	String kashiUf10Cd2;
	/** 貸方２UFコード(固定)1 */
	String kashiUfKotei1Cd2;
	/** 貸方２UFコード(固定)2 */
	String kashiUfKotei2Cd2;
	/** 貸方２UFコード(固定)3 */
	String kashiUfKotei3Cd2;
	/** 貸方２UFコード(固定)4 */
	String kashiUfKotei4Cd2;
	/** 貸方２UFコード(固定)5 */
	String kashiUfKotei5Cd2;
	/** 貸方２UFコード(固定)6 */
	String kashiUfKotei6Cd2;
	/** 貸方２UFコード(固定)7 */
	String kashiUfKotei7Cd2;
	/** 貸方２UFコード(固定)8 */
	String kashiUfKotei8Cd2;
	/** 貸方２UFコード(固定)9 */
	String kashiUfKotei9Cd2;
	/** 貸方２UFコード(固定)10 */
	String kashiUfKotei10Cd2;
	/** 貸方課税区分２（仕訳パターン） */
	String kashiKazeiKbn2;
	/** (貸方2)分離区分 */
	String kashiBunriKbn2;
	/** (貸方2)仕入区分 */
	String kashiShiireKbn2;
	
	/** 貸方負担部門コード３（仕訳パターン） */
	String kashiFutanBumonCd3;
	/** 貸方取引先コード３（仕訳パターン） */
	String kashiTorihikisakiCd3;
	/** 貸方科目コード３（仕訳パターン） */
	String kashiKamokuCd3;
	/** 貸方科目枝番コード３（仕訳パターン） */
	String kashiKamokuEdabanCd3;
	/** 貸方３プロジェクトコード（仕訳パターン） */
	String kashiProjectCd3;
	/** 貸方３セグメントコード（仕訳パターン） */
	String kashiSegmentCd3;
	/** 貸方３UFコード1 */
	String kashiUf1Cd3;
	/** 貸方３UFコード2 */
	String kashiUf2Cd3;
	/** 貸方３UFコード3 */
	String kashiUf3Cd3;
	/** 貸方３UFコード4 */
	String kashiUf4Cd3;
	/** 貸方３UFコード5 */
	String kashiUf5Cd3;
	/** 貸方３UFコード6 */
	String kashiUf6Cd3;
	/** 貸方３UFコード7 */
	String kashiUf7Cd3;
	/** 貸方３UFコード8 */
	String kashiUf8Cd3;
	/** 貸方３UFコード9 */
	String kashiUf9Cd3;
	/** 貸方３UFコード10 */
	String kashiUf10Cd3;
	/** 貸方３UFコード(固定)1 */
	String kashiUfKotei1Cd3;
	/** 貸方３UFコード(固定)2 */
	String kashiUfKotei2Cd3;
	/** 貸方３UFコード(固定)3 */
	String kashiUfKotei3Cd3;
	/** 貸方３UFコード(固定)4 */
	String kashiUfKotei4Cd3;
	/** 貸方３UFコード(固定)5 */
	String kashiUfKotei5Cd3;
	/** 貸方３UFコード(固定)6 */
	String kashiUfKotei6Cd3;
	/** 貸方３UFコード(固定)7 */
	String kashiUfKotei7Cd3;
	/** 貸方３UFコード(固定)8 */
	String kashiUfKotei8Cd3;
	/** 貸方３UFコード(固定)9 */
	String kashiUfKotei9Cd3;
	/** 貸方３UFコード(固定)10 */
	String kashiUfKotei10Cd3;
	/** 貸方課税区分３（仕訳パターン） */
	String kashiKazeiKbn3;
	/** (貸方3)分離区分 */
	String kashiBunriKbn3;
	/** (貸方3)仕入区分 */
	String kashiShiireKbn3;
	
	/** 貸方負担部門コード４（仕訳パターン） */
	String kashiFutanBumonCd4;
	/** 貸方取引先コード４（仕訳パターン） */
	String kashiTorihikisakiCd4;
	/** 貸方科目コード４（仕訳パターン） */
	String kashiKamokuCd4;
	/** 貸方科目枝番コード４（仕訳パターン） */
	String kashiKamokuEdabanCd4;
	/** 貸方４プロジェクトコード（仕訳パターン） */
	String kashiProjectCd4;
	/** 貸方４セグメントコード（仕訳パターン） */
	String kashiSegmentCd4;
	/** 貸方４UFコード1 */
	String kashiUf1Cd4;
	/** 貸方４UFコード2 */
	String kashiUf2Cd4;
	/** 貸方４UFコード3 */
	String kashiUf3Cd4;
	/** 貸方４UFコード4 */
	String kashiUf4Cd4;
	/** 貸方４UFコード5 */
	String kashiUf5Cd4;
	/** 貸方４UFコード6 */
	String kashiUf6Cd4;
	/** 貸方４UFコード7 */
	String kashiUf7Cd4;
	/** 貸方４UFコード8 */
	String kashiUf8Cd4;
	/** 貸方４UFコード9 */
	String kashiUf9Cd4;
	/** 貸方４UFコード10 */
	String kashiUf10Cd4;
	/** 貸方４UFコード(固定)1 */
	String kashiUfKotei1Cd4;
	/** 貸方４UFコード(固定)2 */
	String kashiUfKotei2Cd4;
	/** 貸方４UFコード(固定)3 */
	String kashiUfKotei3Cd4;
	/** 貸方４UFコード(固定)4 */
	String kashiUfKotei4Cd4;
	/** 貸方４UFコード(固定)5 */
	String kashiUfKotei5Cd4;
	/** 貸方４UFコード(固定)6 */
	String kashiUfKotei6Cd4;
	/** 貸方４UFコード(固定)7 */
	String kashiUfKotei7Cd4;
	/** 貸方４UFコード(固定)8 */
	String kashiUfKotei8Cd4;
	/** 貸方４UFコード(固定)9 */
	String kashiUfKotei9Cd4;
	/** 貸方４UFコード(固定)10 */
	String kashiUfKotei10Cd4;
	/** 貸方課税区分４（仕訳パターン） */
	String kashiKazeiKbn4;
	/** (貸方4)分離区分 */
	String kashiBunriKbn4;
	/** (貸方4)仕入区分 */
	String kashiShiireKbn4;
	
	/** 貸方負担部門コード５（仕訳パターン） */
	String kashiFutanBumonCd5;
	/** 貸方取引先コード５（仕訳パターン） */
	String kashiTorihikisakiCd5;
	/** 貸方科目コード５（仕訳パターン） */
	String kashiKamokuCd5;
	/** 貸方科目枝番コード５（仕訳パターン） */
	String kashiKamokuEdabanCd5;
	/** 貸方５プロジェクトコード（仕訳パターン） */
	String kashiProjectCd5;
	/** 貸方５セグメントコード（仕訳パターン） */
	String kashiSegmentCd5;
	/** 貸方５UFコード1 */
	String kashiUf1Cd5;
	/** 貸方５UFコード2 */
	String kashiUf2Cd5;
	/** 貸方５UFコード3 */
	String kashiUf3Cd5;
	/** 貸方５UFコード4 */
	String kashiUf4Cd5;
	/** 貸方５UFコード5 */
	String kashiUf5Cd5;
	/** 貸方５UFコード6 */
	String kashiUf6Cd5;
	/** 貸方５UFコード7 */
	String kashiUf7Cd5;
	/** 貸方５UFコード8 */
	String kashiUf8Cd5;
	/** 貸方５UFコード9 */
	String kashiUf9Cd5;
	/** 貸方５UFコード10 */
	String kashiUf10Cd5;
	/** 貸方５UFコード(固定)1 */
	String kashiUfKotei1Cd5;
	/** 貸方５UFコード(固定)2 */
	String kashiUfKotei2Cd5;
	/** 貸方５UFコード(固定)3 */
	String kashiUfKotei3Cd5;
	/** 貸方５UFコード(固定)4 */
	String kashiUfKotei4Cd5;
	/** 貸方５UFコード(固定)5 */
	String kashiUfKotei5Cd5;
	/** 貸方５UFコード(固定)6 */
	String kashiUfKotei6Cd5;
	/** 貸方５UFコード(固定)7 */
	String kashiUfKotei7Cd5;
	/** 貸方５UFコード(固定)8 */
	String kashiUfKotei8Cd5;
	/** 貸方５UFコード(固定)9 */
	String kashiUfKotei9Cd5;
	/** 貸方５UFコード(固定)10 */
	String kashiUfKotei10Cd5;
	/** 貸方課税区分５（仕訳パターン） */
	String kashiKazeiKbn5;
	/** (貸方5)分離区分 */
	String kashiBunriKbn5;
	/** (貸方5)仕入区分 */
	String kashiShiireKbn5;
	
	/** エラーフラグ */
	boolean errorFlg;
}