package eteam.gyoumu.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー情報。ログイン時にセッションに保持する。
 * オブジェクトの各種項目、getterメソッドの目的・意味は「共通機能（セッションユーザー情報）」を参照。
 */
 @ToString
public class UserExt extends User {
		/** 会社切替設定（スキーマコード） */
		@Getter @Setter
		String[] kaishaKirikaeCd;
		/** 会社切替設定（スキーマ名） */
		@Getter @Setter
		String[] kaishaKirikaeName;
}
