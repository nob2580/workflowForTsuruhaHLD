package eteam.common;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamLogger;
import jp.co.amano.etiming.apl3161.APLException;
import jp.co.amano.etiming.apl3161.PDFDocument;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * E文書関連ロジック
 */
public class EbunshoLogic extends EteamAbstractLogic{
	
//＜定数＞
	/** e文書番号作成用補助コード */
	protected static final String EBUNSHO_HOJYO_CODE = "0";
	/** e文書番号スキーマ毎作成コードデフォルト */
	protected static final String EBUNSHO_SAKUSEI_CODE_DEFAULT = "50";
	/** e文書番号スキーマ毎作成コード定義ファイル */
	protected static final String EBUNSHO_SAKUSEI_CODE_INIFILE = "C:/eteam/def/ebunshoSakuseiCd.ini";
	/** アマノタイムスタンプサーバURL */
	protected static final String AMANO_TIMESTAMPSERVER_URL = "http://tss3161.e-timing.ne.jp/astdtssvr/TSRequest";
	// 松本商工会議所様カスタマイズ向けにメソッド化（オーバーライドできるようにする）
	/** e文書認証情報取得用exeファイル 
	 * @return 上記 */
	protected String EBUNSHO_AUTH_EXE() { return "C:/eteam/bat/bin/sias/auth.exe"; }
	/** LIB-Wの一式ディレクトリパス 
	 * @return 上記 */
	protected String AMANO_DIR_PATH() { return "c:\\eteam\\3161pdf"; }
	/** LIB-WのEXEファイルパス 
	 * @return 上記 */
	protected String AMANO_EXE_PATH() { return "c:\\eteam\\3161pdf\\pdftimestamp.exe"; }
	
// 2022/04/22 e文書同時実行時不具合（#115201）対応 *-	
	/** 入力PDF */
// protected static final String INPUT_PDF_PATH = "c:\\eteam\\tmp\\input.pdf";
	protected String INPUT_PDF_PATH = "";
	/** 出力PDF */
// protected static final String OUTPUT_PDF_PATH = "c:\\eteam\\tmp\\output.pdf";
	protected String OUTPUT_PDF_PATH = "";
// -*	
	
	/** bmp拡張子 */
	protected static final String EXTENSION_BMP = "bmp";
	/** jpeg拡張子 */
	protected static final String EXTENSION_JPEG = "jpeg";
	/** jpg拡張子 */
	protected static final String EXTENSION_JPG = "jpg";
	/** PDF拡張子 */
	public static final String EXTENSION_PDF = "pdf";
	/** e文書対象拡張子ALL */
	protected static final String[] EXTENSIONS_EBUNSHO = {EXTENSION_BMP, EXTENSION_JPEG, EXTENSION_JPG, EXTENSION_PDF};
	/** e文書対象拡張子ALL */
	public static final String EXTENSIONS_EBUNSHO_STR = String.join(",", EXTENSIONS_EBUNSHO);
	/** ATL拡張子 */
	protected static final String EXTENSION_ATL = "atl";

	/** A4用紙横ドット数 */
	protected static final float SIZE_A4_SHORT = 595;
	/** A4用紙縦ドット数 */
	protected static final float SIZE_A4_LONG = 841;
	
	/** 1mbサイズ */
	protected static int MBSIZE_BYTE = 1048576;
	
	/** ファイルサイズによる初期画質設定 */
	protected static float[][] JPEG_QUALITY_SETTINGS = {{ (float)2.0  * MBSIZE_BYTE, 0.9f },
													  { (float)4.0  * MBSIZE_BYTE, 0.8f },
													  { (float)8.0  * MBSIZE_BYTE, 0.7f },
													  { (float)16.0 * MBSIZE_BYTE, 0.5f },
													  { Float.MAX_VALUE,           0.2f }};

//＜部品＞
	/** ログ */
	EteamLogger log = EteamLogger.getLogger(EbunshoLogic.class);

//＜メンバ変数＞
	/** yyyyMMddHHmmssSSS形式で採番した値をもつ */
	protected Long millis;

	/**
	 * e文書対象となり得るファイルかチェック。
	 * @param fileNm ファイル名
	 * @return e文書対象であるならtrue
	 */
	public boolean isEbunshoOrgFile(String fileNm) {
		String extension = EteamIO.getExtension(fileNm);
		for (String s : EXTENSIONS_EBUNSHO) {
			if (s.equalsIgnoreCase(extension))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 現在時刻とiniファイルからe文書番号を生成します。
	 * @return e文書番号　作成年月日(8桁)+作成コード(2桁)+時分秒100分の1秒(8桁)+補助コード(1桁)
	 */
	public String createEbunshoNo(){
		//現在時刻：同シーケンス処理内2回目以降は+10ミリ秒
		if (millis == null) {
			millis = System.currentTimeMillis();
		} else {
			millis = millis + 10;
		}
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String timestr = fmt.format(new Date(millis));
		//iniファイルからスキーマ毎の作成コード取得
		String sakuseicd = getEbunshoSakuseiCdFromIni();
		//作成コードを追加し、e文書番号を作成
        String ebunshoNo = timestr.substring(0, 8) + sakuseicd + timestr.substring(8, 16) + EBUNSHO_HOJYO_CODE;
        if(ebunshoNo.length() != 19) throw new RuntimeException("e文書番号作成結果が異常");
		return ebunshoNo;
	}

	/**
	 * 画像ファイルをPDFに変換する(JPEG利用)
	 * 
	 * @param imageData 画像ファイルデータ
	 * @return 変換後バイトデータ
	 */
	public byte[] image2Pdf_Jpeg(byte[] imageData) {
		try {
			
			// 基準サイズを取得・サイズ変更
			BufferedImage awtImage = ImageIO.read(new ByteArrayInputStream(imageData));
			float orgWidth = awtImage.getWidth();
			float orgHeight = awtImage.getHeight();
			float[] originalSize = {orgWidth, orgHeight};
			float[] drawingSize = resizeImage(orgWidth, orgHeight);
			
			int[] rotate = {0};
			imageData = this.readImageGetaff(imageData, originalSize, rotate);
			
			// PDF作成用オブジェクト生成
			PDDocument document = new PDDocument();
			PDPage page = new PDPage(new PDRectangle(drawingSize[0], drawingSize[1]));
			page.setRotation(rotate[0]);
			document.addPage(page);
			
			// JPEGのバイナリデータをそのまま渡してPDFに変換(必要なら回転)
			InputStream imgStream = new ByteArrayInputStream(imageData);
			PDImageXObject ximage = JPEGFactory.createFromStream(document, imgStream);
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			contentStream.drawImage(ximage, new Matrix(new AffineTransform(drawingSize[0], 0.0, 0.0, drawingSize[1], 0.0, 0.0)));
			contentStream.close();

			// バイト列として出力
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
			document.save(pdfOut);
			document.close();
			return pdfOut.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 画像ファイルをPDFに変換する(JPEG利用、画像圧縮)
	 * 
	 * @param imageData 画像ファイルデータ
	 * @param extension 元画像拡張子
	 * @return 変換後バイトデータ
	 */
	public byte[] image2Pdf_JpegCompress(byte[] imageData, String extension) {
		try {
			
			//画質の初期値を設定(jpeg以外の画像は初期値0.9とする)
			float quality = 0.9f;
			if ( ("jpeg".equalsIgnoreCase(extension)) || ("jpg".equalsIgnoreCase(extension)) ){
				long imgSize = imageData.length;
				
				//元画像1mb以下ならデータを圧縮せず使う
				if (imgSize < MBSIZE_BYTE) return image2Pdf_Jpeg(imageData);
				
				// 元画像サイズによって画質初期値を指定
				for( int i = 0; i < JPEG_QUALITY_SETTINGS.length ; i++ ){
					if( imgSize <= JPEG_QUALITY_SETTINGS[i][0] ){
						quality = JPEG_QUALITY_SETTINGS[i][1];
						break;
					}
				}
			}
			
			// 基準サイズを取得・サイズ変更
			BufferedImage awtImage = ImageIO.read(new ByteArrayInputStream(imageData));
			float orgWidth = awtImage.getWidth();
			float orgHeight = awtImage.getHeight();
			float[] originalSize = {orgWidth, orgHeight};
			float[] drawingSize = resizeImage(orgWidth, orgHeight);
			
			int[] rotate = {0};
			imageData = this.readImageGetaff(imageData, originalSize, rotate);
			
			//作成するファイルのサイズを調べながら1mb切るまでループ
			//但し画質0.1以下になったら終了
			while(true){
				// 画像オブジェクトからPDFへ変換(画像はJPEG化)
				// PDF作成用オブジェクト生成
				PDDocument document = new PDDocument();
				PDPage page = new PDPage(new PDRectangle(drawingSize[0], drawingSize[1]));
				page.setRotation(rotate[0]);
				document.addPage(page);
				
				// 取得した画像データを指定画質でJPEG変換し、さらにPDFに変換(必要なら回転)
				PDImageXObject ximage = JPEGFactory.createFromImage(document, awtImage, quality);
				PDPageContentStream contentStream = new PDPageContentStream(document, page);
				contentStream.drawImage(ximage, new Matrix(new AffineTransform(drawingSize[0], 0.0, 0.0, drawingSize[1], 0.0, 0.0)));
				contentStream.close();

				ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
				document.save(pdfOut);
				document.close();
				if( pdfOut.size() < MBSIZE_BYTE || quality <= 0.1f ){
					return pdfOut.toByteArray();
				}
				pdfOut.close();
				quality = quality - 0.05f;
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 画像ファイルをPDFに変換する(無圧縮)
	 * 
	 * @param imageData 画像ファイルデータ
	 * @return 変換後バイトデータ
	 */
	public byte[] image2Pdf_Lossless(byte[] imageData) {
		try {
			//画像オブジェクト作る
			BufferedImage awtImage = readImage(imageData);

			//以下基準サイズより大きいのでサイズ変更
			float orgWidth = awtImage.getWidth();
			float orgHeight = awtImage.getHeight();
			float[] f = resizeImage(orgWidth, orgHeight);
			float width = f[0], height = f[1];
			  
			// 画像オブジェクトからPDFへ変換
			PDDocument document = new PDDocument();
			PDPage page = new PDPage(new PDRectangle(width, height));
			document.addPage(page);
			PDImageXObject ximage = LosslessFactory.createFromImage(document, awtImage);
			
			PDPageContentStream contentStream = new PDPageContentStream(document, page);   
			contentStream.drawImage(ximage, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());
			contentStream.close();

			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
			document.save(pdfOut);
			document.close();
			return pdfOut.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	//http://stackoverflow.com/questions/5905868/how-to-rotate-jpeg-images-based-on-the-orientation-metadata
	/**
	 * 画像オブジェクト化(無圧縮イメージ)
	 * @param imageData 画像データ
	 * @return 画像オブジェクト
	 */
	// BMPでは画像の回転は発生しない想定だが、処理は念のため残しておく
	public BufferedImage readImage(byte[] imageData) {
		try {
			//Javaの標準読み込み
			BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
			
			//EXIF(ORIENTATION)を参照、見つからなければ元データのまま返す
			int orientation = 1;
			int width;
			int height;
			try {
				Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(imageData));
				Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
				JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
				orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
				width = jpegDirectory.getImageWidth();
				height = jpegDirectory.getImageHeight();
			} catch (Exception ex) {
				return originalImage;
			}

			//EXIF(ORIENTATION)があって、1以外なら回転して返す
			AffineTransform affineTransform = new AffineTransform();
			switch (orientation) {
			case 2: // Flip X
				affineTransform.scale(-1.0, 1.0);
				affineTransform.translate(-width, 0);
				break;
			case 3: // PI rotation
				affineTransform.translate(width, height);
				affineTransform.rotate(Math.PI);
				break;
			case 4: // Flip Y
				affineTransform.scale(1.0, -1.0);
				affineTransform.translate(0, -height);
				break;
			case 5: // - PI/2 and Flip X
				affineTransform.rotate(-Math.PI / 2);
				affineTransform.scale(-1.0, 1.0);
				break;
			case 6: // -PI/2 and -width
				affineTransform.translate(height, 0);
				affineTransform.rotate(Math.PI / 2);
				break;
			case 7: // PI/2 and Flip
				affineTransform.scale(-1.0, 1.0);
				affineTransform.translate(-height, 0);
				affineTransform.translate(0, width);
				affineTransform.rotate(3 * Math.PI / 2);
				break;
			case 8: // PI / 2
				affineTransform.translate(0, width);
				affineTransform.rotate(3 * Math.PI / 2);
				break;
			default: //1だけを想定しているが、万が一1～8以外でもそのままかえす。
				return originalImage;
			}    
			AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);  
			BufferedImage destinationImage = new BufferedImage(originalImage.getHeight(), originalImage.getWidth(), originalImage.getType());
			return affineTransformOp.filter(originalImage, destinationImage);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 渡されたJPEGのEXIF情報から回転方向情報を取得し、
	 * 画像の向きを補正するための行列情報を作成する。
	 * @param imageData 画像データ
	 * @param size 元画像の縦横サイズ
	 * @param rotate 用紙の回転方向(参照渡し)
	 * @return 更新されたImageData配列
	 */
	public byte[] readImageGetaff(byte[] imageData, float[] size, int[] rotate) {
		//EXIF(ORIENTATION)を参照、見つからなければ回転なしとする
		int orientation = 1;
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(imageData));
			Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
			imageData = this.flipImageAndUpdateExif(imageData, size, orientation);
		} catch (Exception ex) {
			return imageData;
		}
		
		//EXIF(ORIENTATION)があって、1以外なら回転して返す。ただし反転系は上記で処理済みなので注意。
		switch (orientation) {
		case 3: // PI rotation
			rotate[0] = 180;
			return imageData;
		case 6:
		case 7: // -PI/2 and -width
			rotate[0] = 90;
			return imageData;
		case 5:
		case 8: // PI / 2
			rotate[0] = 270;
			return imageData;
		default: //1, 2, 4を想定しているが、万が一1～8以外でもそのままかえす。
			return imageData;
		}
	}
	
    /**
     * jpgを対象に、Exifで管理している反転画像の反転処理と、Exifタグの更新を実行。解像度がマイナスになるアフィン変換は廃止する。
     * @param imageData 画像データバイナリ配列
     * @param size サイズ配列（width, height）
     * @param orientation 方向のExifデータ
     * @return 反転処理&orientation更新済みの画像
     * @throws ImageReadException ImageReadException
     * @throws ImageWriteException ImageWriteException
     * @throws IOException IOException
     */
    protected byte[] flipImageAndUpdateExif(byte[] imageData, float[] size, int orientation) throws ImageReadException, ImageWriteException, IOException {
    	// 反転不要な種別ならおしまい
    	if(List.of(1, 3, 6, 8).contains(orientation)) {
    		return imageData;
    	}
        // 画像データをバイト配列からバッファーイメージに変換
        BufferedImage image = Imaging.getBufferedImage(imageData);

        // 画像を反転させる処理
        // 4以外なら左右反転
        var width = (int)size[0];
        var height = (int)size[1];
        BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
            	boolean isYFlip = orientation == 4;
                flippedImage.setRGB(isYFlip ? x : (width - 1 - x), isYFlip ? (height - 1 - y) : y, image.getRGB(x, y));
            }
        }

        // 新しい画像データとして書き出し
        ByteArrayOutputStream flippedByteArrayOutputStream = new ByteArrayOutputStream();
        // 反転画像の書き込み
        ImageIO.write(flippedImage, "jpg", flippedByteArrayOutputStream);

        return flippedByteArrayOutputStream.toByteArray();
    }
	
	/**
	 * 画像サイズをA4に調整
	 * @param orgWidth オリジナル横
	 * @param orgHeight オリジナル縦
	 * @return 調整後の横縦
	 */
	protected float[] resizeImage(float orgWidth, float orgHeight) {
		//規定サイズ以内なのでサイズ変えず
		if (orgWidth <= SIZE_A4_SHORT && orgHeight <= SIZE_A4_SHORT) {
			return new float[]{orgWidth, orgHeight};
		}
		//規定サイズ超過なのでサイズを変える
		float width = 0;
		float height = 0;
		if (orgWidth < orgHeight) {
			width = SIZE_A4_SHORT;
			height = SIZE_A4_SHORT * orgHeight / orgWidth;
			if (height > SIZE_A4_LONG) {
				height = SIZE_A4_LONG;
				width = SIZE_A4_LONG * orgWidth / orgHeight;
			}
		} else {
			height = SIZE_A4_SHORT;
			width = SIZE_A4_SHORT * orgWidth / orgHeight;
			if (width > SIZE_A4_LONG) {
				width = SIZE_A4_LONG;
				height = SIZE_A4_LONG * orgHeight / orgWidth;
			}
		}
		return new float[]{width, height};
	}
	
	/** タイムスタンプPDF */
	@Getter @Setter
	public class StampedPdf {
		/** タイムスタンプ(付与失敗ならnull) */
		Date timeStamp;
		/** PDFデータ */
		byte[] pdf;
		/**
		 * new
		 * @param timeStamp タイムスタンプ
		 * @param pdf PDF
		 */
		StampedPdf(Date timeStamp, byte[] pdf) {
			this.timeStamp = timeStamp;
			this.pdf = pdf;
		}
	}
	
// Ver22.09.30.00 e文書同時実行対応（3次版）（パラメータ追加） *-	
	/**
	 * PDFにタイムスタンプを付与する。
	 * (1)すでにタイムスタンプが付いているPDFならば、何も変更せず、元PDFの情報を返す。
	 * (2)タイムスタンプが付いていないPDFならば、タイムスタンプを付与して、変更後PDFの情報を返す。
	 *    ただし、タイムスタンプ付与に失敗した時は、元PDFのままタイムスタンプなしとして返す
	 * タイムスタンプ付与には、アマノ株式会社提供ツールe-timing EVIDENCE 3161 PDF Lib-Jを用いる。
	 * 
	 * @param orgPdf  タイムスタンプ付与PDFファイル
	 * @param denpyouId 伝票ID
	 * @param edano  添付ファイル枝番号
	 * @return [0]Date:タイムスタンプ、[1]byte[]:PDFデータ
	 */
// public synchronized StampedPdf addTimeStamp(byte[] orgPdf){
	public synchronized StampedPdf addTimeStamp(byte[] orgPdf, String denpyouId, int edano) {
// -*		
		
		if(Env.timeStampDummy()) return addTimeStampDummy(orgPdf);
		try {

			//-------------------------------------
			//e-timing接続・認証情報の取得
			//-------------------------------------
			AmanoAuth auth = geteTimingAuthentication();
			
			//-------------------------------------
			//Windowsで処理する便宜上、ローカルにファイルを書き出す
			//-------------------------------------
			
// 2022/04/22 e文書同時実行時不具合（#115201）対応 *-
			/** ユーザーID **/ //（3次対応で不要）
			//var    userId = getUser();
			//String user   = userId.getLoginUserId();
			/** ランダムID **/
			Random random = new Random();
			int num1 = random.nextInt(10) * 1000;  // ←　2次対応(追加)
			int num2 = random.nextInt(10) * 100;
			int num3 = random.nextInt(10) * 10;
			int num4 = random.nextInt(10);
			Integer num = num1 + num2 + num3 + num4;
			
	// Ver22.09.30.00 e文書同時実行対応（3次版） *-
			/** スキーマ名 */
			String schema = EteamCommon.getContextSchemaName();
			
	// INPUT_PDF_PATH  = "c:\\eteam\\tmp\\input"  + user + schema + num.toString() + ".pdf";
	// OUTPUT_PDF_PATH = "c:\\eteam\\tmp\\output" + user + schema + num.toString() + ".pdf";
			INPUT_PDF_PATH  = "c:\\eteam\\tmp\\input"  + denpyouId + edano + schema + num.toString() + ".pdf";
			OUTPUT_PDF_PATH = "c:\\eteam\\tmp\\output" + denpyouId + edano + schema + num.toString() + ".pdf";
			
	// -*
// -*			
			
			EteamIO.writeFile(orgPdf, INPUT_PDF_PATH);
			
			//-------------------------------------
			//既にタイムスタンプ付与済みなら該当スタンプの付与日付を返却し、スタンプ付与処理は行わない
			//-------------------------------------
			String command = "\"{EXE}\" /V \"{INPUT}\" /L \"{ATLPATH}\" /P {PASSWORD}"
					.replace("{EXE}", AMANO_EXE_PATH())
					.replace("{INPUT}", INPUT_PDF_PATH)
					.replace("{ATLPATH}", auth.getUser())
					.replace("{PASSWORD}", auth.getPassword());
			int exeRet = EteamCommon.executeWindowsProcess(command);
			if(exeRet == 2501) {
				//タイムスタンプ作成時間を取得して返却
				PDFDocument pdfAddedStampDoc = new PDFDocument(orgPdf);
				Date resDate =  pdfAddedStampDoc.getLastTimeStamp().getTimeStampToken().getTSTInfo().getGenTime().getDate();
				return new StampedPdf(resDate, orgPdf);
			}//2501以外だったらタイムスタンプ付与なしとみなして後続処理でタイムスタンプ付与する

			//-------------------------------------
			//e-timing Lib-Wを用いてPDFにタイムスタンプ付与
			//-------------------------------------
			command = "\"{EXE}\" \"{INPUT}\" \"{OUTPUT}\" /C /TR 2 /L \"{ATLPATH}\" /P {PASSWORD}"
					.replace("{EXE}", AMANO_EXE_PATH())
					.replace("{INPUT}", INPUT_PDF_PATH)
					.replace("{OUTPUT}", OUTPUT_PDF_PATH)
					.replace("{ATLPATH}", auth.getUser())
					.replace("{PASSWORD}", auth.getPassword());
			exeRet = EteamCommon.executeWindowsProcess(command);
			if(exeRet != 0) {
				//タイムスタンプなしの状態で続行
				log.error("タイムスタンプ付与に失敗しました。エラーコード:" + exeRet);
				return new StampedPdf(null, orgPdf);
			}
			byte[] stampedPdf = EteamIO.readFile(OUTPUT_PDF_PATH);
			
			//タイムスタンプ作成時間を記録して返却
			PDFDocument pdfAddedStampDoc = new PDFDocument(stampedPdf);
			Date resDate =  pdfAddedStampDoc.getLastTimeStamp().getTimeStampToken().getTSTInfo().getGenTime().getDate();
			return new StampedPdf(resDate, stampedPdf);
		} catch (ParseException | IOException | APLException e) {
			throw new RuntimeException(e);
		} finally{
			EteamIO.deleteFile(INPUT_PDF_PATH);
			EteamIO.deleteFile(OUTPUT_PDF_PATH);
		}
	}
	
	/**
	 * PDFにタイムスタンプを付与する。のダミー
	 * @param orgPdf タイムスタンプ付与PDFファイル
	 * @return [0]Date:タイムスタンプ、[1]byte[]:PDFデータ
	 */
	public synchronized StampedPdf addTimeStampDummy(byte[] orgPdf){
		 return new StampedPdf(new Date(System.currentTimeMillis()), orgPdf); //正常系
//  return new StampedPdf(null, orgPdf); //エラー系
	}
	

	/** アマノ認証情報 */
	@Getter @Setter @ToString
	protected class AmanoAuth {
		/** ユーザーファイルパス */
		String user;
		/** パスワード */
		String password;
		/** URL */
		String url;
	}
	/**
	 * propertiesファイルを読み取り、e-timing接続情報を取得します。
	 * @return 接続情報のリスト
	 */
	protected AmanoAuth geteTimingAuthentication(){
		AmanoAuth ret = new AmanoAuth();

		//ユーザーファイル・パスワードファイルのありかはレジストリ「PDdir32」に入っている
		String pdDirPath = AMANO_DIR_PATH();

		//ユーザーファイル(.atl)配置パスを取得
		String userFilePath = null;
		File pdDir = new File(pdDirPath);
		if (pdDir.isDirectory()){
			File fList[] = pdDir.listFiles();
			if (fList != null){
				for (int i = 0 ; i < fList.length ; i++ ) {
					if (fList[i].isFile() && EXTENSION_ATL.equalsIgnoreCase(EteamIO.getExtension(fList[i].getName()))){
						userFilePath = fList[i].getAbsolutePath();
					}
				}
			}
		}
		if(isEmpty(userFilePath)){
			throw new RuntimeException("タイムスタンプ用ライセンスファイルが取得できませんでした。");
		}
		ret.setUser(userFilePath);

		//パスワードファイルはauth.exe経由で復号化して読む
		String password = null;
		BufferedReader brd = null;
		try {
			String[] strY = new String[]{
				"WorkFlow",
				"2015",
				"auth",
				"Et",
				"Def",
				"Pwd719"
				};
			
			// 松本商工会議所様カスタマイズ用に、ProccessBuikderの受け取れる配列を可変長とする
			List<String> commandLineParams = new ArrayList<String>();
			commandLineParams.add(EBUNSHO_AUTH_EXE());
			commandLineParams.add(strY[3]);
			commandLineParams.add(strY[5]);
			
			this.addAuthCommandLineParams(commandLineParams);
			
			ProcessBuilder pbr = new ProcessBuilder(commandLineParams);
			Process proc = pbr.start();
			brd = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			proc.waitFor();
			String line = brd.readLine();
			if (line != null){
				password = line;
			}
			
			brd.close();
			
			if (isEmpty(password)){
				throw new RuntimeException("e-timing認証情報取得に失敗しました。所定パスに認証情報ファイルが配置されているか確認してください。");
			}
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		} finally{
		}
			
		ret.setPassword(password);
		
		// URLは固定
		ret.setUrl(AMANO_TIMESTAMPSERVER_URL);
		return ret;
	}

	/**
	 * iniファイルを読み取り、スキーマ毎に設定されているe文書作成コードを取得します。
	 * @return スキーマ毎のe文書作成コード
	 */
	protected String getEbunshoSakuseiCdFromIni(){
		String schema = EteamCommon.getContextSchemaName();
		
		//iniファイルや設定が無い場合のデフォルト値
        String sakuseicd = EBUNSHO_SAKUSEI_CODE_DEFAULT;
        
		//iniファイルからe文書作成コード読み取り
		Properties prop = new Properties();
		File iniFile = new File(EBUNSHO_SAKUSEI_CODE_INIFILE);
		if(iniFile.exists()){
			try (InputStream iniIS = new FileInputStream(iniFile);
				 InputStreamReader isr = new InputStreamReader(iniIS, "SJIS");
				 BufferedReader br = new BufferedReader(isr)){
				prop.load(br);
				String iniCode = prop.getProperty(schema);
				//適切なコードなら作成コードとして使用
				if(!(isEmpty(iniCode))){
					if(checkEbunshoSakuseiCd(iniCode) == true){
						sakuseicd = iniCode;
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return sakuseicd;
	}
	
	/**
	 * 指定されたcodeがスキーマ毎e文書作成コードとして適切な値かチェックします。
	 * @param  code チェックするスキーマ毎e文書作成コード
	 * @return true:正常値　false:異常値
	 */
	protected boolean checkEbunshoSakuseiCd(String code){
		
		//数値化できる2文字であるか
		if(code.length() != 2){
			return false;
		}
		int codenum;
		try {
			codenum = Integer.parseInt(code);
		} catch (NumberFormatException e) {
			return false;
		}
		//50～99の範囲内か
		if( 50 <= codenum && codenum <= 99 ){
			return true;
		}else{
			return false;
		}
	}

	/** テスト 
	 * @param argv ダミー引数 */
	public static void main(String[] argv) {
		EbunshoLogic l = new EbunshoLogic();
		String[] fname = {"left-300x225.jpg"};
		for (String f : fname) {
			try {
				byte[] imageData = EteamIO.readFile("c:/temp/" + f);
// byte[] pdfData = l.image2Pdf(imageData);
				BufferedImage image = l.readImage(imageData);
				ImageIO.write(image, "jpeg", new FileOutputStream("c:/temp/" + f + "_after.jpg"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/** カスタマイズ用 */
	protected void addAuthCommandLineParams(List<String> commandLineParams)
	{
	}
}
