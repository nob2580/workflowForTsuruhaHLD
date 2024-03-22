package eteam.gyoumu.system;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author j_matsumoto
 * SystemKanriAction用のStrategyクラス
 */
@Getter @Setter
public abstract class AbstractSystemKanriActionCustomStrategy {
	/**
	 * キーとパラーメーター用の追加マップ
	 */
	protected Map<String, String> additionalKeysAndParametersMap;
	
	/**
	 * 変数代入用のキー名称
	 */
	protected String keyName;
	
	/**
	 * 名称に応じた設定先に入れたい値
	 */
	protected String targetSettingValue;
	
	/**
	 * エラーリスト
	 */
	protected List<String> errorList;
	
	/**
	 * ターゲットの設定変数値を設定する
	 * まずは基本形。これで足りないなら適宜overrideしてもらう想定で。
	 */
	public void setTargetValueBykeyName()
	{
		for(String key : this.additionalKeysAndParametersMap.keySet())
		{
			if(this.keyName.equals(key))
			{
				this.additionalKeysAndParametersMap.put(key, this.targetSettingValue);
				return;
			}
		}
	}
	
	
	/**
	 * 追加の設定情報をチェックする
	 * ここは項目次第のはずなので空で。
	 */
	public void checkAddtionalSettingInfo()
	{
	}
	
	/** カスタム和名の設定。再実装不要なようにデフォルトはそのまま。 
	 * @param originalName 元の名前
	 * @return そのまま（必要ならOverrideすること）
	 */
	public String setCustomName(String originalName)
	{
		return originalName;
	}
}
