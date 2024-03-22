package eteam.gyoumu.kaikei;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;

/**
 * 仮払抽出ロジック
 */
public class KaikeiMainLogic extends EteamAbstractLogic  {

	/**
	 * 各種抽出バッチのリストを返す
	 * @return 各種抽出バッチ
	 */
	@SuppressWarnings("rawtypes")
	public List<Class> getChuushutsuBatClassList() {
		List<Class> ret = new ArrayList<>();
		ret.add(KaribaraiChuushutsuBat.class);
		ret.add(KeihiTatekaeSeisanChuushutsuBat.class);
		ret.add(RyohiKaribaraiChuushutsuBat.class);
		ret.add(RyohiSeisanChuushutsuBat.class);
		ret.add(KaigaiRyohiKaribaraiChuushutsuBat.class);
		ret.add(KaigaiRyohiSeisanChuushutsuBat.class);
		ret.add(TsuukinTeikiChuushutsuBat.class);
		ret.add(SeikyuushoBaraiChuushutsuBat.class);
		ret.add(ShiharaiIraiChuushutsuBat.class);
		ret.add(JidouHikiotoshiChuushutsuBat.class);
		ret.add(FurikaeDenpyouChuushutsuBat.class);
		ret.add(SougouTsukekaeDenpyouChuushutsuBat.class);
		ret.add(KoutsuuhiSeisanChuushutsuBat.class);
		
		return ret;
	}
}