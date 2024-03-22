package eteam.base;

import java.util.ResourceBundle;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.CyclicReferenceRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.TooManyRegistrationRuntimeException;
import org.seasar.framework.container.factory.S2ContainerFactory;

import eteam.common.EteamConst;

/**
 * ロジッククラスの生成部品。
 * ロジッククラスはnewせずに、{@link #getComponent(Class)}を使用するルールである。
 */
public class EteamContainer {

	/** Seasar DIコンテナ */
	public static S2Container container;
	/** DIマップ */
	protected static ResourceBundle diProp;
	/** ログ */
	protected static EteamLogger log = EteamLogger.getLogger(EteamContainer.class);
			
	//初期化
	static {
		container = S2ContainerFactory.create(EteamConst.Env.SEASAR_DICON_FILENAME);
		container.init();
		try {
			diProp = ResourceBundle.getBundle("di_ext");
		} catch (Exception e) {
			log.info("di_ext.propertiesはロードできませんでした。通常のオブジェクトを作成します。");
		}
	}

	/**
	 * オブジェクトを取得する
	 * @param c 取得したいオブジェクトのクラス
	 * @return  オブジェクト
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T getComponent(Class c) {
		if (diProp != null && diProp.containsKey(c.getName())) {
			try {
				return (T)container.getComponent(Class.forName(diProp.getString(c.getName())));
			} catch (ComponentNotFoundRuntimeException | TooManyRegistrationRuntimeException
					| CyclicReferenceRuntimeException | ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		return (T)container.getComponent(c.getSimpleName());
	}
// 
//    /**
//     * DB接続を行う通常のサービスインタフェース
//     */
//    public interface IEteamService{
//        /**
//         * @param connection DBコネクション
//         */
//        void init(EteamConnection connection);
//    }
    
	/**
	 * 既存のコネクションを流用してオブジェクトを取得する
	 * @param c 取得したいオブジェクトのクラス
	 * @param connection DBコネクション
	 * @return  オブジェクト
	 */
	public static <T> T getComponent(@SuppressWarnings("rawtypes") Class c,EteamConnection connection) {
		EteamAbstractLogic serviceBySimpleName = getComponent(c);
		serviceBySimpleName.init(connection);
		return (T)serviceBySimpleName;
	}
}
