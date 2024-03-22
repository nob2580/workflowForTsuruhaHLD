package eteam.gyoumu.tsuuchi;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 伝票一覧テーブル設定用クラス
 */
@Getter @Setter @ToString
public class DenpyouIchiranDataWFCom {

	/** 伝票区分 */
	String denpyouKbn = "";
	/** シリアルNo */
	long serialNo = 0;
	/** 伝票状態 */
	String denpyouJoutai = "";
	/** ステータス */
	String name = "";
	/** 伝票種別(URL用) */
	String denpyouShubetsu_url = "";

	/** 登録日時(起票日) */
	Timestamp tourokuTime = null;
	/** 更新日時(申請日) */
	Timestamp koushinTime = null;
	/** 承認日(最終承認日) */
	Timestamp shouninbi = null;

	/** 起票ユーザーID */
	String userId = "";
	/** 起票ユーザーフル名 */
	String userFullName = "";
	/** 起票部門コード */
	String bumonCd = "";
	/** 起票部門フル名 */
	String bumonFullName = "";

	/** 全承認人数カウント */
	long allCnt = 0;
	/** 承認済人数カウント */
	long curCnt = 0;
	/** 残り承認人数カウント */
	long zanCnt = 0;

	/** 現在承認者ユーザーID(検索用) */
	String genUserId = "";
	/** 現在承認者ユーザーフル名 */
	String genUserFullName = "";
	/** 現在承認者部門フル名 */
	String genBumonFullName = "";
	/** 現在承認者業務ロールID(検索用) */
	String genGyoumuRoleId = "";
	/** 現在承認者業務ロールフル名 */
	String genGyoumuRoleName = "";
	/** 現在承認者名 */
	String genName = "";

	/** FBデータ作成ステータス */
	String fbStatus = "";
	/** 仕訳データ作成ステータス */
	String shiwakeStatus = "";
	/** マル秘文書フラグ */
	String maruhiFlg = "";
	
	/** 社員番号（起票者） */
	String shain_no = "";
}
