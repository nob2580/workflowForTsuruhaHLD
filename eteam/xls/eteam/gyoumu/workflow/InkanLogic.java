package eteam.gyoumu.workflow;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import eteam.base.GMap;
import eteam.common.EteamCommon;

/**
 * 印鑑作るクラス
 */
public class InkanLogic {
	/** 標準フォント */
	protected static Font MY_FONT = new Font("MS ゴシック", Font.PLAIN, 24);
	/** 小さめフォント */
	protected static Font MY_FONT_MINI = new Font("MS ゴシック", Font.PLAIN, 16);
	
	static {
		//java.awt.HeadlessException対策
		System.setProperty("java.awt.headless", "false");
	}

	/**
	 * 印鑑データをバイトデータにして返す。
	 * 
	 * @param inkanParams 印鑑リスト
	 * @return バイトデータ
	 */
	public byte[] make(GMap inkanParams){
		if (inkanParams.isEmpty()) {
			return null;
		}
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		make(bout, inkanParams);
		return bout.toByteArray();
  	}
	
	/**
	 * 印鑑作る
	 * @param out 出力先
	 * @param inkanParams 印鑑リスト
	 */
	public static void make(OutputStream out, GMap inkanParams) {

		//枠画像読み込み→キャンバス作成
		BufferedImage image = null;
		try {
			image = ImageIO.read(InkanLogic.class.getResourceAsStream("inkan.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Graphics g = image.getGraphics();
		
		Graphics2D g2d = (Graphics2D)g;
		BasicStroke wideStroke = new BasicStroke(5);
		g2d.setStroke(wideStroke);
		g2d.setPaint(Color.BLACK);
		g2d.drawRect(0, 0, 199, 199);
		
		
		//氏 名だけの画像を作ってキャンバスに貼り付け
		BufferedImage nameImage = makeNameImage((String)inkanParams.get("inkan_name"));
		g.drawImage(nameImage, 32, 74, 137, 53, null);
		
		//印鑑上部
		String inkanTop = (String)inkanParams.get("inkan_top");
		int inkanTopLocation = 0;
		switch(EteamCommon.getByteLength(inkanTop)){
			//経験則での位置計算にすぎない
			case  1: inkanTopLocation = 91; break;
			case  2: inkanTopLocation = 86; break;
			case  3: inkanTopLocation = 80; break;
			case  4: inkanTopLocation = 75; break;
			case  5: inkanTopLocation = 69; break;
			case  6: inkanTopLocation = 64; break;
			case  7: inkanTopLocation = 58; break;
			case  8: inkanTopLocation = 53; break;
			case  9: inkanTopLocation = 47; break;
			case 10: inkanTopLocation = 42; break;
			case 11: inkanTopLocation = 34; break;
			case 12: inkanTopLocation = 28; break;
			case 13: inkanTopLocation = 41; break;
			case 14: inkanTopLocation = 39; break;
			case 15: inkanTopLocation = 36; break;
			case 16: inkanTopLocation = 33; break;
		}
		String inkanBottom = (String)inkanParams.get("inkan_bottom");

		//キャンバスに日付と処理文言書く
		g.setColor(Color.red);
		g.setFont(EteamCommon.getByteLength(inkanTop) <= 12 ? MY_FONT : MY_FONT_MINI);
		g.drawString(inkanTop, inkanTopLocation, 62);
		g.setFont(MY_FONT);
		g.drawString(inkanBottom, 44, 160);

		//キャンバスのイメージを出力
        try {
        	ImageIO.write(image, "png", out);
        } catch(IOException e) {
        	throw new RuntimeException(e);
        }
	}

    /**
     * テキストの画像作る
     * @param text テキスト
     * @return テキストの画像
     */
	public static BufferedImage makeNameImage(String text) {
		//画像サイズ調べる
		Rectangle2D rect = getTextSize(text);

		//キャンバス作る
        BufferedImage image = new BufferedImage((int)Math.ceil(rect.getWidth()), (int)Math.ceil(rect.getHeight()), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        //白背景
        g.setColor(Color.white);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        //text描画
		g.setColor(Color.red);
		g.setFont(MY_FONT);
		g.drawString(text, 0, 30);
		return image;
	}

	/**
	 * テキスト描画した時のサイズを調べる
	 * @param text テキスト
	 * @return サイズ
	 */
	protected static Rectangle2D getTextSize(String text) {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
		g.setColor(Color.red);
		g.setFont(MY_FONT);
		Rectangle2D rect = g.getFontMetrics().getStringBounds(text, g);
		return rect;
	}
}