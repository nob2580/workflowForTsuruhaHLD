package eteam.gyoumu.userjouhoukanri;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** CSVファイル情報（登録内容） */
@Getter @Setter @ToString
public class IkkatsuTourokuUserCSVUploadInfoUserInfo implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;
	/** csv内のnumbering */
	String number;
	/** ユーザーID */
	String userId;
	/** 社員番号 */
	String shainNo;
	/** ユーザー姓 */
	String userSei;
	/** ユーザー名 */
	String userMei;
	/** メールアドレス */
	String mailAddress;
	/** 有効期限From */
	String yuukouKigenFrom;
	/** 有効期限To */
	String yuukouKigenTo;
	/** 代理起票可能フラグ */
	String dairikihyouFlg;
	/** 法人カード利用 */
	String houjinCardRiyouFlag;
	/** 法人カード識別用番号 */
	String houjinCardShikibetsuyouNum;
	/** セキュリティパターン */
	String securityPattern;
	/** セキュリティワークフロー限定フラグ */
	String securityWfonlyFlg;
	/** 承認ルート変更権限レベル */
	String shouninRouteHenkouLevel;
	/** マル秘設定権限 */
	String maruhiKengenFlg;
	/** マル秘解除権限 */
	String maruhiKaijyoFlg;
	/** 財務拠点のみ使用 */
	String zaimuKyotenNyuryokuOnlyFlg;
	
	/** エラーフラグ */
	boolean errorFlg;
}