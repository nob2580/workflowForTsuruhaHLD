package eteam.database.dto;

import java.sql.Date;
import java.sql.Timestamp;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー情報DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class UserInfo implements IEteamDTO {

	/**
	 * default constructor
	 */
	public UserInfo() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public UserInfo(GMap m) {
		this.userId = m.get("user_id");
		this.shainNo = m.get("shain_no");
		this.userSei = m.get("user_sei");
		this.userMei = m.get("user_mei");
		this.mailAddress = m.get("mail_address");
		this.yuukouKigenFrom = m.get("yuukou_kigen_from");
		this.yuukouKigenTo = m.get("yuukou_kigen_to");
		this.tourokuUserId = m.get("touroku_user_id");
		this.tourokuTime = m.get("touroku_time");
		this.koushinUserId = m.get("koushin_user_id");
		this.koushinTime = m.get("koushin_time");
		this.passKoushinDate = m.get("pass_koushin_date");
		this.passFailureCount = m.get("pass_failure_count");
		this.passFailureTime = m.get("pass_failure_time");
		this.tmpLockFlg = m.get("tmp_lock_flg");
		this.tmpLockTime = m.get("tmp_lock_time");
		this.lockFlg = m.get("lock_flg");
		this.lockTime = m.get("lock_time");
		this.dairikihyouFlg = m.get("dairikihyou_flg");
		this.houjinCardRiyouFlag = m.get("houjin_card_riyou_flag");
		this.houjinCardShikibetsuyouNum = m.get("houjin_card_shikibetsuyou_num");
		this.securityPattern = m.get("security_pattern");
		this.securityWfonlyFlg = m.get("security_wfonly_flg");
		this.shouninRouteHenkouLevel = m.get("shounin_route_henkou_level");
		this.maruhiKengenFlg = m.get("maruhi_kengen_flg");
		this.maruhiKaijyoFlg = m.get("maruhi_kaijyo_flg");
		this.zaimuKyotenNyuryokuOnlyFlg = m.get("zaimu_kyoten_nyuryoku_only_flg");
		this.map = m;
	}

	/** ユーザーID */
	public String userId;

	/** 社員番号 */
	public String shainNo;

	/** ユーザー姓 */
	public String userSei;

	/** ユーザー名 */
	public String userMei;

	/** メールアドレス */
	public String mailAddress;

	/** 有効期限開始日 */
	public Date yuukouKigenFrom;

	/** 有効期限終了日 */
	public Date yuukouKigenTo;

	/** 登録ユーザーID */
	public String tourokuUserId;

	/** 登録日時 */
	public Timestamp tourokuTime;

	/** 更新ユーザーID */
	public String koushinUserId;

	/** 更新日時 */
	public Timestamp koushinTime;

	/** パスワード変更日 */
	public Date passKoushinDate;

	/** パスワード誤り回数 */
	public int passFailureCount;

	/** パスワード誤り時間 */
	public Timestamp passFailureTime;

	/** アカウント一時ロックフラグ */
	public String tmpLockFlg;

	/** アカウント一時ロック時間 */
	public Timestamp tmpLockTime;

	/** アカウントロックフラグ */
	public String lockFlg;

	/** アカウントロック時間 */
	public Timestamp lockTime;

	/** 代理起票可能フラグ */
	public String dairikihyouFlg;

	/** 法人カード利用 */
	public String houjinCardRiyouFlag;

	/** 法人カード識別用番号 */
	public String houjinCardShikibetsuyouNum;

	/** セキュリティパターン */
	public String securityPattern;

	/** セキュリティワークフロー限定フラグ */
	public String securityWfonlyFlg;

	/** 承認ルート変更権限レベル */
	public String shouninRouteHenkouLevel;

	/** マル秘設定権限 */
	public String maruhiKengenFlg;

	/** マル秘解除権限 */
	public String maruhiKaijyoFlg;

	/** 拠点入力のみ使用フラグ */
	public String zaimuKyotenNyuryokuOnlyFlg;

	/** その他項目map */
	public GMap map;
}