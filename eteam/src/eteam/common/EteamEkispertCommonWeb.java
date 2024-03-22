package eteam.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidParameterException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.SHIYOU_KIKAN_KBN;
import eteam.common.RegAccess.REG_KEY_NAME;

/**
 * 駅すぱあとWebサービス呼出共通
 */
public class EteamEkispertCommonWeb {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(EteamEkispertCommonWeb.class);
	
	/**
	 * 駅すぱあとWebサービスと連携し、経路情報を取得する
	 * @param from 出発地
	 * @param to1 目的地または経由地1
	 * @param to2 目的地または経由地2
	 * @param to3 目的地または経由地3
	 * @param to4 目的地
	 * @param date 検索したい日付
	 * @param shiyouKikanKbn 使用期間コード
	 * @param hyoujiCnt 表示件数
	 * @param sortKbn ソート区分
	 * @param teiki 定期区間
	 * @return 経路情報リスト
	 * @throws IOException 不正なURL
	 * @throws ParserConfigurationException xmlデータ取得失敗
	 * @throws SAXException xmlデータ不正
	 */
	public List<GMap> createRouteList(String from, String to1,
			String to2, String to3, String to4, String date, String shiyouKikanKbn, String hyoujiCnt, String sortKbn, String teiki) 
			throws IOException, SAXException, ParserConfigurationException {

		
		// 駅すぱあとWebサービス接続キー
		String parameterKey = RegAccess.readRegistory(REG_KEY_NAME.EKISPERT_WEBSERVICE_KEY[0] + EteamCommon.getContextSchemaName(), REG_KEY_NAME.EKISPERT_WEBSERVICE_KEY[1]);
		
		// 探索条件を取得する
		String jouken = getConditionParameter(parameterKey);
		
		String parameter = createParameter(parameterKey, from, to1, to2, to3, to4, date, jouken, hyoujiCnt, sortKbn, teiki);
		if (parameter == null) {
			return new ArrayList<GMap>();
		}
		
		log.info("■パラメータ：" + parameter);
		// 金額区分(null:運賃, 01:１ヶ月定期, 03:3ヶ月定期, 06:6ヶ月定期)
		String moneyKindCd = selectKind(shiyouKikanKbn);
		
		// 駅すぱあとWebサービス経路探索用URL
		URL url = new URL(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.EKISPERT_WEB_URL_ROUTE) + parameter);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setRequestMethod("GET");
		urlConn.setInstanceFollowRedirects(false);
		urlConn.setRequestProperty("Accept-Language", "ja;q=0.7,en;q=0.3");
		urlConn.connect();

		try {
			Document doc = getDocumet(urlConn.getInputStream());

			// 全ルートの駅名リスト
			ArrayList<ArrayList<String>> routeListStation = new ArrayList<ArrayList<String>>();
			// 全ルートの路線リスト
			ArrayList<ArrayList<GMap>> routeListTrain = new ArrayList<ArrayList<GMap>>();
			// 全ルートの金額リスト
			ArrayList<ArrayList<BigDecimal>> routeListPrice = new ArrayList<ArrayList<BigDecimal>>();
			// 全ルートの乗車券名称リスト
			ArrayList<ArrayList<GMap>> routeListTicketName = new ArrayList<ArrayList<GMap>>();
			// 全ルートの料金リスト
			ArrayList<ArrayList<GMap>> routeListCharge = new ArrayList<ArrayList<GMap>>();
			
			// ルートの要素名になっている子ノードを取得する
			Element root = doc.getDocumentElement();
	
			// 各ノードリストを取得
			NodeList nodeListCourse = root.getElementsByTagName("Course");
	
			for (int i = 0; i < nodeListCourse.getLength(); i++) {
	
				Element element = (Element)nodeListCourse.item(i);
				// 子ノードリストを取得
				NodeList nodeListRoute = element.getElementsByTagName("Route");
	
				// 駅名リストを追加
				routeListStation.add(getStationList(nodeListRoute));
				// 路線リストを追加
				routeListTrain.add(getTrainList(nodeListRoute));
	
				// 子ノードリストを取得
				NodeList nodeListPrice = element.getElementsByTagName("Price");
	
				// 金額リストを取得
				routeListPrice.add(getPriceList(nodeListPrice, moneyKindCd));
				// 乗車券リストを取得
				routeListTicketName.add(getTicketSystemName(nodeListPrice, moneyKindCd));
				// 料金リストを取得
				routeListCharge.add(getChargeList(nodeListPrice));
				
			}
	
			// ルートリスト（駅名・路線[乗車券区分]・駅名の順に詰め替え）
			ArrayList<ArrayList<String>> routeList = new ArrayList<ArrayList<String>>();
			
			for (int i = 0; i < routeListStation.size(); i++) {
				ArrayList<String> list = new ArrayList<String>();
				
				ArrayList<String> stationList = routeListStation.get(i);
				ArrayList<GMap> trainList = routeListTrain.get(i);
				ArrayList<GMap> ticketList = routeListTicketName.get(i);
				for (int j = 0; j < stationList.size(); j++) {

					if (j > 0) {
						// IC使用区分がICの場合、金額区分が運賃の場合のみ乗車券名称を表示させる
						if ("ic".equals(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.EKISPERT_IC_KBN)) &&
								"Fare".equals(moneyKindCd)) {

							boolean setTicket = false;
							for (GMap map : ticketList) {
								if (trainList.get(j - 1).getString("LineIndex").equals(map.getString("fromLineIndex")) ||
									trainList.get(j - 1).getString("LineIndex").equals(map.getString("toLineIndex"))) {
									
									// 乗車券名称がない場合(新幹線や、1円単位運賃ではない区間)
									if (!"".equals(map.getString("Name"))) {
										list.add("＝" + trainList.get(j - 1).get("Name") + "[" + map.get("Name") + "]＝");
										setTicket = true;
										break;
										
									}
								}
							}
							
							if (!setTicket) {
								list.add("＝" + trainList.get(j - 1).get("Name") + "＝");
							}

						} else {
							list.add("＝" + trainList.get(j - 1).get("Name") + "＝");
						}
					}
					list.add(stationList.get(j));
	
				}
				routeList.add(list);
			}
			
			// 料金と路線名をマッピングする
			for (int i = 0; i < routeListCharge.size(); i++) {
				for (int j = 0; j < routeListCharge.get(i).size(); j++) {// 料金リスト
					
					String chargeIndex = routeListCharge.get(i).get(j).getString("fromLineIndex");
					
					for (int k = 0; k < routeListTrain.get(i).size(); k++) {
						String lineIndex = routeListTrain.get(i).get(k).getString("LineIndex");
						String trainName = routeListTrain.get(i).get(k).getString("Name");
						
						if (chargeIndex.equals(lineIndex)) {
							routeListCharge.get(i).get(j).put("trainName", trainName);
						}
					}
				}
			}
			
			// 検索結果リストマップ
			ArrayList<GMap> keiroList = new ArrayList<GMap>();
			int routeIndex = 0;
	
			// 経路情報順にコードを設定し、マップのリストに設定
			for (ArrayList<String>list : routeList) {
				GMap map = new GMap();
				String route = "";
				for (String str : list) {
					route = route + str;
				}
				map.put("code", routeIndex + 1);
				map.put("keiro", route);
				keiroList.add(map);
				
				routeIndex++;
			}
			
			int priceIndex = 0;
			// 経路情報順に金額をマップに差込む
			for (ArrayList<BigDecimal> list : routeListPrice) {
				
				BigDecimal price = BigDecimal.ZERO;
				for (BigDecimal num : list) {
					price = price.add(num);
				}
				
				keiroList.get(priceIndex).put("price", formatMoney(price));
				priceIndex++;
			}

			int chargeIndex = 0;
			// 経路情報順に料金をマップに差込む
			for (ArrayList<GMap> list : routeListCharge) {
	
				keiroList.get(chargeIndex).put("routeChargeList", list);
				chargeIndex++;
			}
	
			urlConn.disconnect();
			return keiroList;

		}catch (IOException e) {
			return new ArrayList<GMap>();
		}
	}
	
	/**
	 * 駅すぱあとWebサービスにて経路検索する際のURLパラメータを作成する
	 * @param parameterKey WebService接続キー
	 * @param from 出発地
	 * @param to1 目的地または経由地1
	 * @param to2 目的地または経由地2
	 * @param to3 目的地または経由地3
	 * @param to4 目的地
	 * @param date 検索したい日付
	 * @param jouken 探索条件文字列
	 * @param hyoujiCnt 表示件数
	 * @param sortKbn ソート区分
	 * @param teiki 定期区間
	 * @return パラメータ文字列
	 * @throws UnsupportedEncodingException エンコードエラー
	 */
	protected String createParameter(String parameterKey, String from, String to1, String to2, String to3, String to4,
			String date, String jouken, String hyoujiCnt, String sortKbn, String teiki) throws UnsupportedEncodingException {
		
		String parameter = parameterKey;
		// 必須項目
		if (from != null && to1 != null) {
			parameter = parameter + "&viaList=" + URLEncoder.encode(from, "UTF-8") + ":" + URLEncoder.encode(to1, "UTF-8");
		} else {
			
			return null;
		}
		
		// 任意項目
		if (to2 != null && !to2.isEmpty()) {
			parameter = parameter + ":" + URLEncoder.encode(to2, "UTF-8");
		}

		// 任意項目
		if (to3 != null && !to3.isEmpty()) {
			parameter = parameter + ":" + URLEncoder.encode(to3, "UTF-8");
			
		}
		
		// 任意項目
		if (to4 != null && !to4.isEmpty()) {
			parameter = parameter + ":" + URLEncoder.encode(to4, "UTF-8");
			
		}
		
		// 任意項目
		if (date != null && !date.isEmpty()) {
			// yyyy/mm/ddをyyyymmddに変換
			date = date.replaceAll("/", "");
			parameter = parameter + "&date=" + date;
		}
		
		// 探索条件
		if (jouken != null && !jouken.isEmpty()) {
			parameter = parameter + "&conditionDetail=" + jouken;
		}
		
		// 表示件数
		if (hyoujiCnt != null && !hyoujiCnt.isEmpty()) {
			parameter = parameter + "&answerCount=" + hyoujiCnt;
		}
		
		// ソート区分
		if (sortKbn != null && !sortKbn.isEmpty()) {
			parameter = parameter + "&sort=" + sortKbn;
		}
		
		
		// 定期券情報
		if (teiki != null && !teiki.isEmpty() && !"undefined".equals(teiki)) {
			parameter = parameter + "&assignRoute=" + URLEncoder.encode(teiki, "UTF-8");
		}
		
		return parameter;
	}
	
	/**
	 * 駅名リストを取得する
	 * @param nodeList ノードリスト
	 * @return １経路の駅名リスト
	 */
	protected ArrayList<String> getStationList(NodeList nodeList) {
		
		
		/* 駅名リスト */
		ArrayList<String> stationList = new ArrayList<String>();
		for (int i = 0; i < nodeList.getLength(); i++) {

			/* 駅名取得 */
			Element elementPoint = (Element)nodeList.item(i);
			// 子ノードリストを取得
			NodeList nodeListPoint = elementPoint.getElementsByTagName("Point");

			for (int j = 0; j < nodeListPoint.getLength(); j++) {

				Element elementStation = (Element)nodeListPoint.item(j);
				// 子ノードリストを取得
				NodeList nodeListStation = elementStation.getElementsByTagName("Station");
				
	
				for (int k = 0; k < nodeListStation.getLength(); k++) {
					
					Element elementName = (Element)nodeListStation.item(k);
					// ノードのValue値
					stationList.add(getChildren(elementName, "Name"));
				}
			}
		}
		
		return stationList;
		
	}
	
	/**
	 * 路線名リストを取得する
	 * @param nodeList ノードリスト
	 * @return １経路の路線名リスト
	 */
	protected ArrayList<GMap> getTrainList(NodeList nodeList) {
		
		/* 路線リスト */
		ArrayList<GMap> trainList = new ArrayList<GMap>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			
			/* 路線取得 */
			Element elementLine = (Element)nodeList.item(i);
			// 子ノードリストを取得
			NodeList nodeListLine = elementLine.getElementsByTagName("Line");
			for (int j = 0; j < nodeListLine.getLength(); j++) {
				GMap map = new GMap();
				
				Element elementName = (Element)nodeListLine.item(j);
				// ノードのValue値
				map.put("LineIndex", elementName.getAttribute("index"));
				map.put("Name", getChildren(elementName, "Name"));
				trainList.add(map);
			}
		}
		return trainList;
	}
	
	/**
	 * 金額リストを取得する
	 * @param nodeList ノードリスト
	 * @param moneyKindCd 金額区分(null:運賃, 01:１ヶ月定期, 03:3ヶ月定期, 06:6ヶ月定期)
	 * @return １経路の金額リスト
	 */
	protected ArrayList<BigDecimal> getPriceList(NodeList nodeList, String moneyKindCd) {
		
		ArrayList<BigDecimal> moneyList = new ArrayList<BigDecimal>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element)nodeList.item(i);
			
			/* 金額取得 */
			if (moneyKindCd.equals(element.getAttribute("kind")) && "true".equals(element.getAttribute("selected"))) {

				moneyList.add(new BigDecimal(getChildren(element, "Oneway")));
			}
		}
		
		return moneyList;
	}

	/**
	 * 料金リストを取得する
	 * @param nodeList ノードリスト
	 * @return １経路の料金リスト
	 */
	protected ArrayList<GMap> getChargeList(NodeList nodeList) {
		
		ArrayList<GMap> retList = new ArrayList<GMap>();
		
		// 料金情報のみ取り出す
		ArrayList<GMap> chargeList = new ArrayList<GMap>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element)nodeList.item(i);
			
			/* 料金の場合 */
			if ("Charge".equals(element.getAttribute("kind"))) {
					GMap map = new GMap();
					map.put("fromLineIndex", element.getAttribute("fromLineIndex"));
					map.put("name", getChildren(element, "Name"));
					map.put("charge", formatMoney(new BigDecimal(getChildren(element, "Oneway"))));
					chargeList.add(map);
			}
			
		}

		int listIndex = 0;
		int roopIndex = 0;
		GMap chargeListMap;
		
		ArrayList<GMap> list = new ArrayList<GMap>();
		for (GMap map : chargeList) {
			
			// 最初はそのまま追加
			if (roopIndex == 0) {
				list.add(map);
			// 前回のリストのインデックスと今回のインデックスが同じ場合もそのまま追加
			} else if (list.get(listIndex - 1).get("fromLineIndex").equals(map.get("fromLineIndex"))) {

				list.add(map);
				// ループが最後の場合、マップに詰める
				if (chargeList.size() - 1 == roopIndex) {
					
					chargeListMap = new GMap();
					chargeListMap.put("fromLineIndex", list.get(listIndex - 1).get("fromLineIndex"));
					chargeListMap.put("chargeList", list);
					retList.add(chargeListMap);
				}
			// ループが最後の場合、マップに詰める
			} else if (chargeList.size() - 1 == roopIndex) {
				
				chargeListMap = new GMap();
				chargeListMap.put("fromLineIndex", list.get(listIndex - 1).get("fromLineIndex"));
				chargeListMap.put("chargeList", list);
				retList.add(chargeListMap);

			// 同じじゃなかったらマップに詰めて、リストを初期化し、詰めなおす
			} else {
				chargeListMap = new GMap();
				chargeListMap.put("fromLineIndex", list.get(listIndex - 1).get("fromLineIndex"));
				chargeListMap.put("chargeList", list);
				retList.add(chargeListMap);
				
				list = new ArrayList<GMap>();
				list.add(map);
				listIndex = 0;
			}
			listIndex++;
			roopIndex++;
		}
		
		return retList;
	}
	
	/**
	 * IC使用区分が'IC'の場合のみ、乗車券区分(ICカード乗車券/普通乗車券)のリストを返却する。
	 * @param nodeList ノードリスト
	 * @param moneyKindCd 金額区分(null:運賃, 01:１ヶ月定期, 03:3ヶ月定期, 06:6ヶ月定期)
	 * @return 乗車券名称リスト
	 */
	public ArrayList<GMap> getTicketSystemName(NodeList nodeList, String moneyKindCd) {
	
		// IC使用区分
		String icKbn = EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.EKISPERT_IC_KBN);
		
		ArrayList<GMap> ticketList = new ArrayList<GMap>();

		// IC使用区分がICで、金額区分が運賃の場合のみ
		if ("ic".equals(icKbn) && "Fare".equals(moneyKindCd)) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element)nodeList.item(i);
				
				GMap map = new GMap();
				/* 乗車券名称を取得 */
				if (moneyKindCd.equals(element.getAttribute("kind")) && "true".equals(element.getAttribute("selected"))) {

					map.put("fromLineIndex", String.valueOf(element.getAttribute("fromLineIndex")));
					map.put("toLineIndex", String.valueOf(element.getAttribute("toLineIndex")));
					
					try {
						map.put("Name", String.valueOf(getChildren(element, "Name")));
					} catch (Exception e) {
						// 新幹線など、乗車券名称が取得できない場合は空文字を設定
						map.put("Name", "");
					}
					ticketList.add(map);
				}
			}
		}
		return ticketList;
		

	}
	/**
	 * 駅すぱあとWebサービスと連携し、駅名情報を取得する
	 * @param name 駅名
	 * @return 駅名情報リスト
	 * @throws IOException 不正なURL
	 * @throws ParserConfigurationException xmlデータ取得失敗
	 * @throws SAXException xmlデータ不正
	 */
	public List<String> searchStationList(String name) 
			throws IOException, SAXException, ParserConfigurationException {
		
		// 駅すぱあとWebサービス接続キー
		String parameterKey = RegAccess.readRegistory(REG_KEY_NAME.EKISPERT_WEBSERVICE_KEY[0] + EteamCommon.getContextSchemaName(), REG_KEY_NAME.EKISPERT_WEBSERVICE_KEY[1]);
		
		String parameter = parameterKey;
		// 必須項目
		if (name != null) {
			parameter = parameter + "&name=" + URLEncoder.encode(name, "UTF-8");
		} else {
			
			log.warn("■xml取得処理入力パラメータ不正■");
			return new ArrayList<String>();
		}
		
		// 駅すぱあとWebサービス経路探索用URL
		URL url = new URL(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.EKISPERT_WEB_URL_STATION) + parameter);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setRequestMethod("GET");
		urlConn.setInstanceFollowRedirects(false);
		urlConn.setRequestProperty("Accept-Language", "ja;q=0.7,en;q=0.3");
		urlConn.connect();

		Document doc = getDocumet(urlConn.getInputStream());

		// 駅名リスト
		ArrayList<String> listStation = new ArrayList<String>();
		
		// ルートの要素名になっている子ノードを取得する
		Element root = doc.getDocumentElement();

		// 各ノードリストを取得
		NodeList nodeListPoint = root.getElementsByTagName("Point");

		for (int i = 0; i < nodeListPoint.getLength(); i++) {

			Element elementStation = (Element)nodeListPoint.item(i);
			// 子ノードリストを取得
			NodeList nodeListStation = elementStation.getElementsByTagName("Station");

			for (int j = 0; j < nodeListStation.getLength(); j++) {

				/* 駅名取得 */
				Element elementName = (Element)nodeListStation.item(j);
				// ノードのValue値
				listStation.add(getChildren(elementName, "Name"));
				
			}
		}
		urlConn.disconnect();
		return listStation;
	}
	
	/**
	 * 路線図一覧を取得する
	 * @param todouhukenCd 都道府県コード
	 * @return 路線図一覧リスト
	 * @throws IOException 不正なURL
	 * @throws ParserConfigurationException xmlデータ取得失敗
	 * @throws SAXException xmlデータ不正
	 */
	public List<GMap> createRailMapList(String todouhukenCd) throws IOException, SAXException, ParserConfigurationException {
		
		// 駅すぱあとWebサービス接続キー
		String parameterKey = RegAccess.readRegistory(REG_KEY_NAME.EKISPERT_WEBSERVICE_KEY[0] + EteamCommon.getContextSchemaName(), REG_KEY_NAME.EKISPERT_WEBSERVICE_KEY[1]);

		String strUrl = EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.EKISPERT_WEB_URL_MAP_LIST) + parameterKey;
		
		URL url = new URL(strUrl);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setRequestMethod("GET");
		urlConn.setInstanceFollowRedirects(false);
		urlConn.setRequestProperty("Accept-Language", "ja;q=0.7,en;q=0.3");
		urlConn.connect();
		
		Document doc = getDocumet(urlConn.getInputStream());
		
		// 路線図一覧リスト
		ArrayList<GMap> railMapList = new ArrayList<GMap>();
		
		// ルートの要素名になっている子ノードを取得する
		Element root = doc.getDocumentElement();
		
		// 各ノードリストを取得
		NodeList nodeListRailMap = root.getElementsByTagName("RailMap");
		
		for (int i = 0; i < nodeListRailMap.getLength(); i++) {
			
			GMap railMap = new GMap();
			
			Element elementRailMap = (Element)nodeListRailMap.item(i);
			
			boolean isAdd = false;

			String prefectureName = "";
			// 子ノードリストを取得
			NodeList nodeListPoint = elementRailMap.getElementsByTagName("Point");

			for (int j = 0; j < nodeListPoint.getLength(); j++) {
				Element elementPoint = (Element)nodeListPoint.item(j);

				// 子ノードリストを取得
				NodeList nodeListPrefecture = elementPoint.getElementsByTagName("Prefecture");
				
				for (int k = 0; k < nodeListPrefecture.getLength(); k++) {

					Element elementPrefecture = (Element)nodeListPrefecture.item(k);
					// 都道府県名を取得
					// ノードのValue値
					if (todouhukenCd.equals(elementPrefecture.getAttribute("code"))) {
						prefectureName = getChildren(elementPrefecture, "Name");
						
						isAdd = true;
					}
				}
			}
			if (isAdd) {
				// 路線図IDを取得
				String railMapId = elementRailMap.getAttribute("id");
				railMap.put("railMapId", railMapId);
				// 表示名
				String DisplayName = getChildren(elementRailMap, "DisplayName");

				railMap.put("Name", prefectureName + ":" + DisplayName);
				railMapList.add(railMap);
			}
		}
		urlConn.disconnect();
		return railMapList;
	}
	
	/**
	 * 基点駅名入力時、駅の座標を取得する
	 * @param railMapId 路線図ID
	 * @param stationName 基点駅名
	 * @return 座標Map
	 * @throws IOException 不正なURL
	 * @throws ParserConfigurationException xmlデータ取得失敗
	 * @throws SAXException xmlデータ不正
	 */
	public Map<String, String> railMapStation(String railMapId, String stationName) throws IOException, SAXException, ParserConfigurationException {
		
		// 駅すぱあとWebサービス接続キー
		String parameterKey = RegAccess.readRegistory(REG_KEY_NAME.EKISPERT_WEBSERVICE_KEY[0] + EteamCommon.getContextSchemaName(), REG_KEY_NAME.EKISPERT_WEBSERVICE_KEY[1]);

		// 駅すぱあとWebサービス経路探索用URL
		URL url = new URL(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.EKISPERT_WEB_URL_MAP_STATON) + parameterKey + "&id=" + railMapId + "&stationName=" + URLEncoder.encode(stationName, "UTF-8"));
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setRequestMethod("GET");
		urlConn.setInstanceFollowRedirects(false);
		urlConn.setRequestProperty("Accept-Language", "ja;q=0.7,en;q=0.3");
		urlConn.connect();
		
		Document doc = getDocumet(urlConn.getInputStream());
		
		// ルートの要素名になっている子ノードを取得する
		Element root = doc.getDocumentElement();
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		// 各ノードリストを取得
		NodeList nodeListRailMap = root.getElementsByTagName("RailMap");
		
		for (int i = 0; i < nodeListRailMap.getLength(); i++) {
			
			Element elementRailMap = (Element)nodeListRailMap.item(i);
			
			// 子ノードリストを取得
			NodeList nodeListPoint = elementRailMap.getElementsByTagName("Point");

			for (int j = 0; j < nodeListPoint.getLength(); j++) {
				Element elementPoint = (Element)nodeListPoint.item(j);
				
				// 子ノードリストを取得
				NodeList nodeListMark = elementPoint.getElementsByTagName("MarkCoordinates");
				
				for (int k = 0; k < nodeListMark.getLength(); k++) {
					Element element = (Element)nodeListMark.item(k);
					
					map.put("x", String.valueOf(element.getAttribute("x")));
					map.put("y", String.valueOf(element.getAttribute("y")));
				}
				
			}
		}
		urlConn.disconnect();
		return map;
	}
	
	/**
	 * 路線図を取得する(バイナリデータ)
	 * @param railMapId 路線図ID
	 * @param railMapInfo 基点座標Map
	 * @return バイナリ形式の文字列
	 * @throws IOException 不正なURL
	 * @throws ParserConfigurationException xmlデータ取得失敗
	 * @throws SAXException xmlデータ不正
	 */
	public String railMapImage(String railMapId, Map<String, String> railMapInfo) throws IOException, SAXException, ParserConfigurationException {
		
		// 駅すぱあとWebサービス接続キー
		String parameterKey = RegAccess.readRegistory(REG_KEY_NAME.EKISPERT_WEBSERVICE_KEY[0] + EteamCommon.getContextSchemaName(), REG_KEY_NAME.EKISPERT_WEBSERVICE_KEY[1]);

		// 駅すぱあとWebサービス経路探索用URL
		URL url = new URL(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.EKISPERT_WEB_URL_MAP_IMAGE) + parameterKey + "&id=" + railMapId +
				"&x=" + railMapInfo.get("x") + "&y=" + railMapInfo.get("y") + "&width=450&height=340");
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setRequestMethod("GET");
		urlConn.setInstanceFollowRedirects(false);
		urlConn.setRequestProperty("Accept-Language", "ja;q=0.7,en;q=0.3");
		urlConn.connect();
		
		Document doc = getDocumet(urlConn.getInputStream());
		
		String imageData = "";
		// ルートの要素名になっている子ノードを取得する
		Element root = doc.getDocumentElement();
		
		// 各ノードリストを取得
		NodeList nodeListRailMap = root.getElementsByTagName("RailMap");
		
		for (int i = 0; i < nodeListRailMap.getLength(); i++) {
			
			Element elementImageData = (Element)nodeListRailMap.item(i);
			
			imageData = getChildren(elementImageData, "ImageData");
			log.info("■イメージバイナリデータ:" + imageData);
		}
		
		urlConn.disconnect();
		return imageData;
	}
	
	/**
	 * 画像クリック時の該当駅名を取得
	 * @param railMapId 路線図ID
	 * @param clickMapInfo クリックした座標
	 * @return 駅名
	 * @throws IOException 不正なURL
	 * @throws ParserConfigurationException xmlデータ取得失敗
	 * @throws SAXException xmlデータ不正
	 */
	public String railMapStationName(String railMapId, Map<String, String> clickMapInfo) throws IOException, SAXException, ParserConfigurationException {
		
		// 駅すぱあとWebサービス接続キー
		String parameterKey = RegAccess.readRegistory(REG_KEY_NAME.EKISPERT_WEBSERVICE_KEY[0] + EteamCommon.getContextSchemaName(), REG_KEY_NAME.EKISPERT_WEBSERVICE_KEY[1]);
		
		// 駅すぱあとWebサービス経路探索用URL
		URL url = new URL(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.EKISPERT_WEB_URL_MAP_STATON) + parameterKey + "&id=" + railMapId +
				"&x=" + clickMapInfo.get("x") + "&y=" + clickMapInfo.get("y") + "&width=5&height=5");
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setRequestMethod("GET");
		urlConn.setInstanceFollowRedirects(false);
		urlConn.setRequestProperty("Accept-Language", "ja;q=0.7,en;q=0.3");
		urlConn.connect();
		
		Document doc = getDocumet(urlConn.getInputStream());
		
		String clickStName = "";
		// ルートの要素名になっている子ノードを取得する
		Element root = doc.getDocumentElement();
		
		// 各ノードリストを取得
		NodeList nodeListRailMap = root.getElementsByTagName("RailMap");
		
		for (int i = 0; i < nodeListRailMap.getLength(); i++) {
			
			Element elementRailMap = (Element)nodeListRailMap.item(i);
			
			// 子ノードリストを取得
			NodeList nodeListPoint = elementRailMap.getElementsByTagName("Point");

			for (int j = 0; j < nodeListPoint.getLength(); j++) {
				Element elementPoint = (Element)nodeListPoint.item(j);
				
				// 子ノードリストを取得
				NodeList nodeListStation = elementPoint.getElementsByTagName("Station");
				
				for (int k = 0; k < nodeListStation.getLength(); k++) {
					Element element = (Element)nodeListStation.item(k);
					
					
					clickStName = getChildren(element, "Name");
					log.info("■選択駅:" + clickStName);
				}
				
			}
		}
		
		urlConn.disconnect();
		return clickStName;
	}
	
	/**
	 * IC乗車券算出パラメータ生成
	 * @param parameterKey 接続キー
	 * @return パラメータ文字列
	 * @throws IOException icYuusendo
	 * @throws ParserConfigurationException xmlデータ取得失敗
	 * @throws SAXException xmlデータ不正
	 */
	protected String getConditionParameter(String parameterKey) throws IOException, SAXException, ParserConfigurationException {
		
		if (parameterKey == null) {
			return null;
		}
		
		// IC使用区分
		String icKbn = EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.EKISPERT_IC_KBN);
		
		// 優先度
		String icYuusendo = EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.EKISPERT_WEB_IC_PREFERRED);
		
		// 駅すぱあとWebサービス探索条件取得用URL
		URL url = new URL(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.EKISPERT_WEB_SEARCH) + parameterKey + "&ticketSystemType=" + icKbn + "&" + icYuusendo);

		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setRequestMethod("GET");
		urlConn.setInstanceFollowRedirects(false);
		urlConn.setRequestProperty("Accept-Language", "ja;q=0.7,en;q=0.3");
		urlConn.connect();
		
		Document doc = getDocumet(urlConn.getInputStream());
		
		// ルートの要素名になっている子ノードを取得する
		Element root = doc.getDocumentElement();
		String condition = getChildren(root, "Condition");
		log.info("探索条件:" + condition);

		urlConn.disconnect();
		return condition;
	}
	
	
	/**
	 * xmlデータを取得する
	 * @param is インプットストリーム
	 * @return xmlデータ
	 * @throws IOException 不正なURL
	 * @throws ParserConfigurationException xmlデータ取得失敗
	 * @throws SAXException xmlデータ不正
	 */
	protected Document getDocumet(InputStream is) throws SAXException,
			IOException, ParserConfigurationException {

		DocumentBuilder docbuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		return docbuilder.parse(is);

	}

	/**
	 * 指定されたエレメントから子要素の内容を取得
	 * 
	 * @param element 指定エレメント
	 * @param tagName 指定タグ名
	 * @return 取得した内容
	 */
	protected String getChildren(Element element, String tagName) {
		NodeList list = element.getElementsByTagName(tagName);
		Element cElement = (Element) list.item(0);
		return cElement.getFirstChild().getNodeValue();
	}
	
	
	/**
	 * 区分によって駅すぱあとから取得するモードを変更する
	 * @param useCode 使用期間コード
	 * @return 駅すぱあと用金額検索文字列
	 */
	protected String selectKind(String useCode) {
		String ret = "";

		if (SHIYOU_KIKAN_KBN.KIKAN1.equals(useCode)) {
			ret = "Teiki1";
		} else if (SHIYOU_KIKAN_KBN.KIKAN3.equals(useCode)) {
			ret = "Teiki3";
		} else if (SHIYOU_KIKAN_KBN.KIKAN6.equals(useCode)) {
			ret = "Teiki6";
		} else {
			ret = "Fare";
		}
		
		log.info("■金額区分" + ret + "■" + useCode);
		return ret;
	}
	
	/**
	 * BigDecimal型を文字列(カンマ区切り数値)に変換。
	 * @param d 変換前
	 * @return 変換後
	 */
	protected String formatMoney(Object d){
		if (null == d) {
			return "";
		} else if (d instanceof BigDecimal) {
			return new DecimalFormat("#,###").format(d);
		} else {
			throw new InvalidParameterException("BigDecimal以外禁止.d:" + d);
		}
	}
}
