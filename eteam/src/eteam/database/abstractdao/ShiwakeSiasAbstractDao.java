package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.ShiwakeSias;

/**
 * 仕訳抽出(SIAS)に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class ShiwakeSiasAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected ShiwakeSias mapToDto(GMap map){
		return map == null ? null : new ShiwakeSias(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<ShiwakeSias> mapToDto(List<GMap> mapList){
		List<ShiwakeSias> dtoList = new ArrayList<ShiwakeSias>();
		for (var map : mapList) {
			dtoList.add(new ShiwakeSias(map));
		}
		return dtoList;
	}
	
	/**
	 * 仕訳抽出(SIAS)のレコード有無を判定
	 * @param serialNo シリアル番号
	 * @return true:exist false:not exist
	 */
	public boolean exists(long serialNo) {
		return this.find(serialNo) == null ? false : true;
	}
	
	/**
	 * 仕訳抽出(SIAS)から主キー指定でレコードを取得
	 * @param serialNo シリアル番号
	 * @return 仕訳抽出(SIAS)DTO
	 */
	public ShiwakeSias find(long serialNo) {
		final String sql = "SELECT * FROM shiwake_sias WHERE serial_no = ?";
		return mapToDto(connection.find(sql, serialNo));
	}
	
	/**
	 * 仕訳抽出(SIAS)からレコードを全件取得 ※大量データ取得に注意
	 * @return List<仕訳抽出(SIAS)DTO>
	 */
	public List<ShiwakeSias> load() {
		final String sql = "SELECT * FROM shiwake_sias ORDER BY serial_no";
		return mapToDto(connection.load(sql));
	}

	/**
	* 仕訳抽出(SIAS)登録
	* @param dto 仕訳抽出(SIAS)
	* @return 件数
	*/
	public int insert(
		ShiwakeSias dto
	){
		final String sql =
				"INSERT INTO shiwake_sias "
			+ "( denpyou_id, shiwake_status, touroku_time, koushin_time, dymd, seiri, dcno, kymd, kbmn, kusr, sgno, hf1, hf2, hf3, hf4, hf5, hf6, hf7, hf8, hf9, hf10, rbmn, rtor, rkmk, reda, rkoj, rkos, rprj, rseg, rdm1, rdm2, rdm3, rdm4, rdm5, rdm6, rdm7, rdm8, rdm9, rdm10, rdm11, rdm12, rdm13, rdm14, rdm15, rdm16, rdm17, rdm18, rdm19, rdm20, rrit, rkeigen, rzkb, rgyo, rsre, rtky, rtno, sbmn, stor, skmk, seda, skoj, skos, sprj, sseg, sdm1, sdm2, sdm3, sdm4, sdm5, sdm6, sdm7, sdm8, sdm9, sdm10, sdm11, sdm12, sdm13, sdm14, sdm15, sdm16, sdm17, sdm18, sdm19, sdm20, srit, skeigen, szkb, sgyo, ssre, stky, stno, zkmk, zrit, zkeigen, zzkb, zgyo, zsre, exvl, valu, symd, skbn, skiz, uymd, ukbn, ukiz, dkec, fusr, fsen, tkflg, bunri, heic, rate, gexvl, gvalu, gsep ,rurizeikeisan, surizeikeisan, zurizeikeisan, rmenzeikeika, smenzeikeika, zmenzeikeika) "
			+ "VALUES(?, ?, current_timestamp, current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.denpyouId, dto.shiwakeStatus, dto.dymd, dto.seiri, dto.dcno, dto.kymd, dto.kbmn, dto.kusr, dto.sgno, dto.hf1, dto.hf2, dto.hf3, dto.hf4, dto.hf5, dto.hf6, dto.hf7, dto.hf8, dto.hf9, dto.hf10, dto.rbmn, dto.rtor, dto.rkmk, dto.reda, dto.rkoj, dto.rkos, dto.rprj, dto.rseg, dto.rdm1, dto.rdm2, dto.rdm3, dto.rdm4, dto.rdm5, dto.rdm6, dto.rdm7, dto.rdm8, dto.rdm9, dto.rdm10, dto.rdm11, dto.rdm12, dto.rdm13, dto.rdm14, dto.rdm15, dto.rdm16, dto.rdm17, dto.rdm18, dto.rdm19, dto.rdm20, dto.rrit, dto.rkeigen, dto.rzkb, dto.rgyo, dto.rsre, dto.rtky, dto.rtno, dto.sbmn, dto.stor, dto.skmk, dto.seda, dto.skoj, dto.skos, dto.sprj, dto.sseg, dto.sdm1, dto.sdm2, dto.sdm3, dto.sdm4, dto.sdm5, dto.sdm6, dto.sdm7, dto.sdm8, dto.sdm9, dto.sdm10, dto.sdm11, dto.sdm12, dto.sdm13, dto.sdm14, dto.sdm15, dto.sdm16, dto.sdm17, dto.sdm18, dto.sdm19, dto.sdm20, dto.srit, dto.skeigen, dto.szkb, dto.sgyo, dto.ssre, dto.stky, dto.stno, dto.zkmk, dto.zrit, dto.zkeigen, dto.zzkb, dto.zgyo, dto.zsre, dto.exvl, dto.valu, dto.symd, dto.skbn, dto.skiz, dto.uymd, dto.ukbn, dto.ukiz, dto.dkec, dto.fusr, dto.fsen, dto.tkflg, dto.bunri, dto.heic, dto.rate, dto.gexvl, dto.gvalu, dto.gsep, dto.rurizeikeisan, dto.surizeikeisan, dto.zurizeikeisan, dto.rmenzeikeika, dto.smenzeikeika, dto.zmenzeikeika
					);
	}

	/**
	* 仕訳抽出(SIAS)の非キー全てと共通列を更新
	* 値設定漏れによるデータ消失防止のためDaoで取得したShiwakeSiasの使用を前提
	* @param dto 仕訳抽出(SIAS)
	* @return 件数
	*/
	public int update(
		ShiwakeSias dto
		 ){
		final String sql =
				"UPDATE shiwake_sias "
		    + "SET denpyou_id = ?, shiwake_status = ?, koushin_time = current_timestamp, dymd = ?, seiri = ?, dcno = ?, kymd = ?, kbmn = ?, kusr = ?, sgno = ?, hf1 = ?, hf2 = ?, hf3 = ?, hf4 = ?, hf5 = ?, hf6 = ?, hf7 = ?, hf8 = ?, hf9 = ?, hf10 = ?, rbmn = ?, rtor = ?, rkmk = ?, reda = ?, rkoj = ?, rkos = ?, rprj = ?, rseg = ?, rdm1 = ?, rdm2 = ?, rdm3 = ?, rdm4 = ?, rdm5 = ?, rdm6 = ?, rdm7 = ?, rdm8 = ?, rdm9 = ?, rdm10 = ?, rdm11 = ?, rdm12 = ?, rdm13 = ?, rdm14 = ?, rdm15 = ?, rdm16 = ?, rdm17 = ?, rdm18 = ?, rdm19 = ?, rdm20 = ?, rrit = ?, rkeigen = ?, rzkb = ?, rgyo = ?, rsre = ?, rtky = ?, rtno = ?, sbmn = ?, stor = ?, skmk = ?, seda = ?, skoj = ?, skos = ?, sprj = ?, sseg = ?, sdm1 = ?, sdm2 = ?, sdm3 = ?, sdm4 = ?, sdm5 = ?, sdm6 = ?, sdm7 = ?, sdm8 = ?, sdm9 = ?, sdm10 = ?, sdm11 = ?, sdm12 = ?, sdm13 = ?, sdm14 = ?, sdm15 = ?, sdm16 = ?, sdm17 = ?, sdm18 = ?, sdm19 = ?, sdm20 = ?, srit = ?, skeigen = ?, szkb = ?, sgyo = ?, ssre = ?, stky = ?, stno = ?, zkmk = ?, zrit = ?, zkeigen = ?, zzkb = ?, zgyo = ?, zsre = ?, exvl = ?, valu = ?, symd = ?, skbn = ?, skiz = ?, uymd = ?, ukbn = ?, ukiz = ?, dkec = ?, fusr = ?, fsen = ?, tkflg = ?, bunri = ?, heic = ?, rate = ?, gexvl = ?, gvalu = ?, gsep = ?,rurizeikeisan = ?, surizeikeisan = ?, zurizeikeisan = ?, rmenzeikeika = ?, smenzeikeika = ?, zmenzeikeika = ? "
	 		+ "WHERE koushin_time = ? AND serial_no = ?";
			return connection.update(sql,
				dto.denpyouId, dto.shiwakeStatus, dto.dymd, dto.seiri, dto.dcno, dto.kymd, dto.kbmn, dto.kusr, dto.sgno, dto.hf1, dto.hf2, dto.hf3, dto.hf4, dto.hf5, dto.hf6, dto.hf7, dto.hf8, dto.hf9, dto.hf10, dto.rbmn, dto.rtor, dto.rkmk, dto.reda, dto.rkoj, dto.rkos, dto.rprj, dto.rseg, dto.rdm1, dto.rdm2, dto.rdm3, dto.rdm4, dto.rdm5, dto.rdm6, dto.rdm7, dto.rdm8, dto.rdm9, dto.rdm10, dto.rdm11, dto.rdm12, dto.rdm13, dto.rdm14, dto.rdm15, dto.rdm16, dto.rdm17, dto.rdm18, dto.rdm19, dto.rdm20, dto.rrit, dto.rkeigen, dto.rzkb, dto.rgyo, dto.rsre, dto.rtky, dto.rtno, dto.sbmn, dto.stor, dto.skmk, dto.seda, dto.skoj, dto.skos, dto.sprj, dto.sseg, dto.sdm1, dto.sdm2, dto.sdm3, dto.sdm4, dto.sdm5, dto.sdm6, dto.sdm7, dto.sdm8, dto.sdm9, dto.sdm10, dto.sdm11, dto.sdm12, dto.sdm13, dto.sdm14, dto.sdm15, dto.sdm16, dto.sdm17, dto.sdm18, dto.sdm19, dto.sdm20, dto.srit, dto.skeigen, dto.szkb, dto.sgyo, dto.ssre, dto.stky, dto.stno, dto.zkmk, dto.zrit, dto.zkeigen, dto.zzkb, dto.zgyo, dto.zsre, dto.exvl, dto.valu, dto.symd, dto.skbn, dto.skiz, dto.uymd, dto.ukbn, dto.ukiz, dto.dkec, dto.fusr, dto.fsen, dto.tkflg, dto.bunri, dto.heic, dto.rate, dto.gexvl, dto.gvalu, dto.gsep, dto.rurizeikeisan, dto.surizeikeisan, dto.zurizeikeisan, dto.rmenzeikeika, dto.smenzeikeika, dto.zmenzeikeika 
				,dto.koushinTime, dto.serialNo);
    }

	/**
	* 仕訳抽出(SIAS)登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 仕訳抽出(SIAS)
	* @return 件数
	*/
	public int upsert(
		ShiwakeSias dto
		 ){
		final String sql =
				"INSERT INTO shiwake_sias "
			+ "( denpyou_id, shiwake_status, touroku_time, koushin_time, dymd, seiri, dcno, kymd, kbmn, kusr, sgno, hf1, hf2, hf3, hf4, hf5, hf6, hf7, hf8, hf9, hf10, rbmn, rtor, rkmk, reda, rkoj, rkos, rprj, rseg, rdm1, rdm2, rdm3, rdm4, rdm5, rdm6, rdm7, rdm8, rdm9, rdm10, rdm11, rdm12, rdm13, rdm14, rdm15, rdm16, rdm17, rdm18, rdm19, rdm20, rrit, rkeigen, rzkb, rgyo, rsre, rtky, rtno, sbmn, stor, skmk, seda, skoj, skos, sprj, sseg, sdm1, sdm2, sdm3, sdm4, sdm5, sdm6, sdm7, sdm8, sdm9, sdm10, sdm11, sdm12, sdm13, sdm14, sdm15, sdm16, sdm17, sdm18, sdm19, sdm20, srit, skeigen, szkb, sgyo, ssre, stky, stno, zkmk, zrit, zkeigen, zzkb, zgyo, zsre, exvl, valu, symd, skbn, skiz, uymd, ukbn, ukiz, dkec, fusr, fsen, tkflg, bunri, heic, rate, gexvl, gvalu, gsep, rurizeikeisan, surizeikeisan, zurizeikeisan, rmenzeikeika, smenzeikeika, zmenzeikeika) "
			+ "VALUES(?, ?, current_timestamp, current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT shiwake_sias_pkey "
			+ "DO UPDATE SET denpyou_id = ?, shiwake_status = ?, koushin_time = current_timestamp, dymd = ?, seiri = ?, dcno = ?, kymd = ?, kbmn = ?, kusr = ?, sgno = ?, hf1 = ?, hf2 = ?, hf3 = ?, hf4 = ?, hf5 = ?, hf6 = ?, hf7 = ?, hf8 = ?, hf9 = ?, hf10 = ?, rbmn = ?, rtor = ?, rkmk = ?, reda = ?, rkoj = ?, rkos = ?, rprj = ?, rseg = ?, rdm1 = ?, rdm2 = ?, rdm3 = ?, rdm4 = ?, rdm5 = ?, rdm6 = ?, rdm7 = ?, rdm8 = ?, rdm9 = ?, rdm10 = ?, rdm11 = ?, rdm12 = ?, rdm13 = ?, rdm14 = ?, rdm15 = ?, rdm16 = ?, rdm17 = ?, rdm18 = ?, rdm19 = ?, rdm20 = ?, rrit = ?, rkeigen = ?, rzkb = ?, rgyo = ?, rsre = ?, rtky = ?, rtno = ?, sbmn = ?, stor = ?, skmk = ?, seda = ?, skoj = ?, skos = ?, sprj = ?, sseg = ?, sdm1 = ?, sdm2 = ?, sdm3 = ?, sdm4 = ?, sdm5 = ?, sdm6 = ?, sdm7 = ?, sdm8 = ?, sdm9 = ?, sdm10 = ?, sdm11 = ?, sdm12 = ?, sdm13 = ?, sdm14 = ?, sdm15 = ?, sdm16 = ?, sdm17 = ?, sdm18 = ?, sdm19 = ?, sdm20 = ?, srit = ?, skeigen = ?, szkb = ?, sgyo = ?, ssre = ?, stky = ?, stno = ?, zkmk = ?, zrit = ?, zkeigen = ?, zzkb = ?, zgyo = ?, zsre = ?, exvl = ?, valu = ?, symd = ?, skbn = ?, skiz = ?, uymd = ?, ukbn = ?, ukiz = ?, dkec = ?, fusr = ?, fsen = ?, tkflg = ?, bunri = ?, heic = ?, rate = ?, gexvl = ?, gvalu = ?, gsep = ?, rurizeikeisan = ?, surizeikeisan = ?, zurizeikeisan = ?, rmenzeikeika = ?, smenzeikeika = ?, zmenzeikeika = ? "
			+ "";
			return connection.update(sql,
				dto.denpyouId, dto.shiwakeStatus, dto.dymd, dto.seiri, dto.dcno, dto.kymd, dto.kbmn, dto.kusr, dto.sgno, dto.hf1, dto.hf2, dto.hf3, dto.hf4, dto.hf5, dto.hf6, dto.hf7, dto.hf8, dto.hf9, dto.hf10, dto.rbmn, dto.rtor, dto.rkmk, dto.reda, dto.rkoj, dto.rkos, dto.rprj, dto.rseg, dto.rdm1, dto.rdm2, dto.rdm3, dto.rdm4, dto.rdm5, dto.rdm6, dto.rdm7, dto.rdm8, dto.rdm9, dto.rdm10, dto.rdm11, dto.rdm12, dto.rdm13, dto.rdm14, dto.rdm15, dto.rdm16, dto.rdm17, dto.rdm18, dto.rdm19, dto.rdm20, dto.rrit, dto.rkeigen, dto.rzkb, dto.rgyo, dto.rsre, dto.rtky, dto.rtno, dto.sbmn, dto.stor, dto.skmk, dto.seda, dto.skoj, dto.skos, dto.sprj, dto.sseg, dto.sdm1, dto.sdm2, dto.sdm3, dto.sdm4, dto.sdm5, dto.sdm6, dto.sdm7, dto.sdm8, dto.sdm9, dto.sdm10, dto.sdm11, dto.sdm12, dto.sdm13, dto.sdm14, dto.sdm15, dto.sdm16, dto.sdm17, dto.sdm18, dto.sdm19, dto.sdm20, dto.srit, dto.skeigen, dto.szkb, dto.sgyo, dto.ssre, dto.stky, dto.stno, dto.zkmk, dto.zrit, dto.zkeigen, dto.zzkb, dto.zgyo, dto.zsre, dto.exvl, dto.valu, dto.symd, dto.skbn, dto.skiz, dto.uymd, dto.ukbn, dto.ukiz, dto.dkec, dto.fusr, dto.fsen, dto.tkflg, dto.bunri, dto.heic, dto.rate, dto.gexvl, dto.gvalu, dto.gsep, dto.rurizeikeisan, dto.surizeikeisan, dto.zurizeikeisan, dto.rmenzeikeika, dto.smenzeikeika, dto.zmenzeikeika 
				, dto.denpyouId, dto.shiwakeStatus, dto.dymd, dto.seiri, dto.dcno, dto.kymd, dto.kbmn, dto.kusr, dto.sgno, dto.hf1, dto.hf2, dto.hf3, dto.hf4, dto.hf5, dto.hf6, dto.hf7, dto.hf8, dto.hf9, dto.hf10, dto.rbmn, dto.rtor, dto.rkmk, dto.reda, dto.rkoj, dto.rkos, dto.rprj, dto.rseg, dto.rdm1, dto.rdm2, dto.rdm3, dto.rdm4, dto.rdm5, dto.rdm6, dto.rdm7, dto.rdm8, dto.rdm9, dto.rdm10, dto.rdm11, dto.rdm12, dto.rdm13, dto.rdm14, dto.rdm15, dto.rdm16, dto.rdm17, dto.rdm18, dto.rdm19, dto.rdm20, dto.rrit, dto.rkeigen, dto.rzkb, dto.rgyo, dto.rsre, dto.rtky, dto.rtno, dto.sbmn, dto.stor, dto.skmk, dto.seda, dto.skoj, dto.skos, dto.sprj, dto.sseg, dto.sdm1, dto.sdm2, dto.sdm3, dto.sdm4, dto.sdm5, dto.sdm6, dto.sdm7, dto.sdm8, dto.sdm9, dto.sdm10, dto.sdm11, dto.sdm12, dto.sdm13, dto.sdm14, dto.sdm15, dto.sdm16, dto.sdm17, dto.sdm18, dto.sdm19, dto.sdm20, dto.srit, dto.skeigen, dto.szkb, dto.sgyo, dto.ssre, dto.stky, dto.stno, dto.zkmk, dto.zrit, dto.zkeigen, dto.zzkb, dto.zgyo, dto.zsre, dto.exvl, dto.valu, dto.symd, dto.skbn, dto.skiz, dto.uymd, dto.ukbn, dto.ukiz, dto.dkec, dto.fusr, dto.fsen, dto.tkflg, dto.bunri, dto.heic, dto.rate, dto.gexvl, dto.gvalu, dto.gsep, dto.rurizeikeisan, dto.surizeikeisan, dto.zurizeikeisan, dto.rmenzeikeika, dto.smenzeikeika, dto.zmenzeikeika
				);
    }
	
	/**
	 * 仕訳抽出(SIAS)から主キー指定でレコードを削除
	 * @param serialNo シリアル番号
	 * @return 削除件数
	 */
	public int delete(long serialNo){
		final String sql = "DELETE FROM shiwake_sias WHERE serial_no = ?";
		return connection.update(sql, serialNo);
	}
}
