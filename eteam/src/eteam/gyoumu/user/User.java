package eteam.gyoumu.user;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー情報。ログイン時にセッションに保持する。
 * オブジェクトの各種項目、getterメソッドの目的・意味は「共通機能（セッションユーザー情報）」を参照。
 */
 @ToString
public class User implements Serializable {
	 /** SUID */
	protected static final long serialVersionUID = -6188638353811171164L;

//@Getter @Setterをつけていないのは、直接意識しないで欲しい項目

//＜ログイン時に決まる項目＞
	 /** ログインユーザーID */
	@Getter @Setter
	String loginUserId;
	/** ログインユーザー姓 */
	String loginUserSei;
	/** ログインユーザー名 */
	String loginUserMei;

	/** ログイン社員番号 */
	String loginShainNo;
	/** ログイン代表負担部門コード */
	String[] loginDaihyouFutanBumonCd;
	/** ログイン役職コード */
	String loginYakushokuCd;

	/** ログイン所属部門コード */
	String[] loginBumonCd;
	/** ログイン所属部門名 */
	String[] loginBumonName;
	/** ログイン所属部門フル名 */
	String[] loginBumonFullName;
	/** ログイン所属部門ロールID */
	String[] loginBumonRoleId;
	/** ログイン所属部門ロール名 */
	String[] loginBumonRoleName;
	/** 業務ロールID候補 */
	String[] gyoumuRoleIdKouho;
	/** 業務ロール名候補 */
	String[] gyoumuRoleNameKouho;
	/** 業務ロール処理部門コード候補 */
	String[] gyoumuRoleShozokuBumonCdKouho;
	/** ロール変更可能 */
	@Getter @Setter
	boolean enableRoleSentaku;
	/** マル秘参照権限 */
	@Getter @Setter
	boolean maruhiKengenFlg;
	/** マル秘参照権限 */
	@Getter @Setter
	boolean maruhiSetteiFlg;
	/** マル秘解除権限 */
	@Getter @Setter
	boolean maruhiKaijyoFlg;
	/** 承認ルート変更権限 */
	@Getter @Setter
	String shouninRouteHenkouLevel;

//＜被代行者指定画面にて切り替える項目＞
	/** 被代行ユーザーID */
	String hiDaikouUserId;
	/** 被代行ユーザー姓 */
	String hiDaikouUserSei;
	/** 被代行ユーザー名 */
	String hiDaikouUserMei;

	/** 被代行社員番号 */
	String hiDaikouShainNo;
	/** 被代行代表負担部門コード */
	String[] hiDaikouDaihyouFutanBumonCd;
	/** 被代行役職コード */
	String hiDaikouYakushokuCd;

	/** 被代行所属部門コード */
	String[] hiDaikouBumonCd;
	/** 被代行所属部門名 */
	String[] hiDaikouBumonName;
	/** 被代行所属部門フル名 */
	String[] hiDaikouBumonFullName;
	/** 被代行所属部門ロールID */
	String[] hiDaikouBumonRoleId;
	/** 被代行所属部門ロール名 */
	String[] hiDaikouBumonRoleName;
	
//＜ロール変更画面にて切り替える項目＞
	/** 業務ロールID */
	@Getter @Setter
	String gyoumuRoleId;
	/** 業務ロール名 */
	@Getter @Setter
	String gyoumuRoleName;
	/** 業務ロール処理部門コード */
	@Getter @Setter
	String[] gyoumuRoleShozokuBumonCd;

	/**
	 * 登録ユーザーID・更新ユーザーIDにマッピングする用のユーザーID（本人のユーザーID）を取得
	 * @return 上記ユーザーID
	 */
	public String getTourokuOrKoushinUserId() {
		return loginUserId;
	}
	
	/**
	 * WF等の制御に使用するユーザーIDを取得
	 * @return 上記ユーザーID
	 */
	public String getSeigyoUserId() {
		return (null != hiDaikouUserId) ? hiDaikouUserId : loginUserId;
	}

	/**
	 * 表示用ユーザー名を取得
	 * @return 表示用ユーザー名
	 */
	public String getDisplayUserName() {
		return (null != hiDaikouUserId) ?
				hiDaikouUserSei + "　" + hiDaikouUserMei + "(代行者:" + loginUserSei + "　" + loginUserMei + ")"
				:loginUserSei + "　" + loginUserMei;
	}

	/**
	 * 表示用ユーザー名を取得
	 * @return 表示用ユーザー名
	 */
	public String getDisplayUserNameShort() {
		return (null != hiDaikouUserId) ?
				hiDaikouUserSei + "　" + hiDaikouUserMei
				:loginUserSei + "　" + loginUserMei;
	}

	/**
	 * 表示用ユーザー名(代行等関係なく本人)を取得
	 * @return 表示用ユーザー名
	 */
	public String getDisplayUserNameHonnin() {
		return loginUserSei + "　" + loginUserMei;
	}

	/**
	 * 社員番号を取得（本人 or 被代行者)
	 * @return 社員番号
	 */
	public String getShainNo() {
		return (null != hiDaikouUserId) ? hiDaikouShainNo : loginShainNo;
	}

	/**
	 * 姓を取得（本人 or 被代行者)
	 * @return 姓
	 */
	public String getUserSei() {
		return (null != hiDaikouUserId) ? hiDaikouUserSei : loginUserSei;
	}

	/**
	 * 名を取得（本人 or 被代行者)
	 * @return 名
	 */
	public String getUserMei() {
		return (null != hiDaikouUserId) ? hiDaikouUserMei : loginUserMei;
	}

	/**
	 * 代表負担部門コードを取得（本人 or 被代行者)
	 * @return 代表負担部門コード
	 */
	public String[] getDaihyouFutanCd() {
		return (null != hiDaikouUserId) ? hiDaikouDaihyouFutanBumonCd : loginDaihyouFutanBumonCd;
	}

	/**
	 * 役職コードを取得（本人 or 被代行者)
	 * @return 役職コード
	 */
	public String getYakushoCd() {
		return (null != hiDaikouUserId) ? hiDaikouYakushokuCd : loginYakushokuCd;
	}

	/**
	 * 部門コード取得（本人 or 被代行者)
	 * @return 部門コード
	 */
	public String[] getBumonCd() {
		return (null != gyoumuRoleId) ? null : (null != hiDaikouUserId) ? hiDaikouBumonCd : loginBumonCd;
	}

	/**
	 * 部門名取得（本人 or 被代行者)
	 * @return 部門名
	 */
	public String[] getBumonName() {
		return (null != gyoumuRoleId) ? null : (null != hiDaikouUserId) ? hiDaikouBumonName :loginBumonName;
	}

	/**
	 * 部門フル名取得（本人 or 被代行者)
	 * @return 部門フル名
	 */
	public String[] getBumonFullName() {
		return (null != gyoumuRoleId) ? null : (null != hiDaikouUserId) ? hiDaikouBumonFullName :loginBumonFullName;
	}

	/**
	 * 部門ロールID取得（本人 or 被代行者)
	 * @return 部門ロールID
	 */
	public String[] getBumonRoleId() {
		return (null != gyoumuRoleId) ? null : (null != hiDaikouUserId) ? hiDaikouBumonRoleId :loginBumonRoleId;
	}

	/**
	 * 部門ロール名取得（本人 or 被代行者)
	 * @return 部門ロール名
	 */
	public String[] getBumonRoleName() {
		return (null != gyoumuRoleId) ? null : (null != hiDaikouUserId) ? hiDaikouBumonRoleName :loginBumonRoleName;
	}
	
}
