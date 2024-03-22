package eteam.gyoumu.workflow;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 部門推奨ルート親CSVと部門推奨ルート子CSVのセッション全部 */
@Getter @Setter @ToString
public class IkkatsuTourokuBumonSuishouRouteCSVUploadSessionInfo implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;
	/** ファイル名（部門推奨ルート(親)） */
	String fileNameOyaInfo;
	/** ファイル名（部門推奨ルート(子)） */
	String fileNameKoInfo;
	/** 部門推奨ルート(親)情報リスト */
	List<IkkatsuTourokuBumonSuishouRouteCSVUploadInfoOya> oyaList;
	/** 部門推奨ルート(子)情報リスト */
	List<IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo> koList;
	/** エラーリスト */
	List<String> errorList;
	
	/** アップロードステータス */
	enum UploadStatus {
		/** 初期表示 */
		Init, 
		/** 処理中 */
		Run,
		/** 処理終了 */
		End
    };
    
    /** 現在のアップロードステータス */
    UploadStatus status;
    
	/**
	 * コンストラクタ
	 * @param fileNameOyaInfo ファイル名（部門推奨ルート(親)）
	 * @param oyaInfoList 部門推奨ルート(親)情報リスト
	 * @param fileNameKoInfo ファイル名（部門推奨ルート(子)）
	 * @param koInfoList 部門推奨ルート(子)情報リスト
	 * @param errorList エラーメッセージリスト
	 */
	public IkkatsuTourokuBumonSuishouRouteCSVUploadSessionInfo(String fileNameOyaInfo, List<IkkatsuTourokuBumonSuishouRouteCSVUploadInfoOya> oyaInfoList, String fileNameKoInfo, List<IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo> koInfoList, List<String> errorList) {
		this.fileNameOyaInfo = fileNameOyaInfo;
		this.fileNameKoInfo = fileNameKoInfo;
		this.oyaList = oyaInfoList;
		this.koList = koInfoList;
		this.errorList = errorList;
	}
}