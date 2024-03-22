package eteam.base;

/**
 * DTOインターフェース
 */
public interface IEteamDTO{
	
	/**
	 * @param map Query結果map
	 */
	void setMap(GMap map);
	
	/**
	 * @return DTO内Map（区分を超えて同一のカラム名の何かが欲しい、などのケースに）
	 */
	GMap getMap();
}