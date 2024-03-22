package eteam.common;

import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.contentstream.operator.DrawObject;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.state.Concatenate;
import org.apache.pdfbox.contentstream.operator.state.Restore;
import org.apache.pdfbox.contentstream.operator.state.Save;
import org.apache.pdfbox.contentstream.operator.state.SetGraphicsStateParameters;
import org.apache.pdfbox.contentstream.operator.state.SetMatrix;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import lombok.Getter;
import lombok.Setter;

/**
 * @author j_matsumoto
 *
 */
public class PdfImageChecker {

	/**
	 * points per inch (PDRectangleからコピペ)
	 */
	private static final float POINTS_PER_INCH = 72.0F;

	/**
	 * @param page PDPage
	 * @return PDPage内の画像の検証結果（string）
	 * @throws IOException  IOException
	 */
	public static String CheckImage(PDPage page) throws IOException
	{
    	try {
			ImageSizeExtractor streamEngine = new ImageSizeExtractor();

			streamEngine.processPage(page);

			var imageMap = streamEngine.getPageImages().entrySet();

			if(imageMap.isEmpty())
			{
				return "success";
			}

	    	for(var imageWithDpi : imageMap)
	    	{
	    		RenderedImage image = imageWithDpi.getKey();
	    		ColorModel colorModel = image.getColorModel();

	    		// RGB色空間ではないか、各階調が8bit以下ならreturn
	    		if(colorModel.getColorSpace().getType() != ColorSpace.TYPE_RGB
	    			|| Arrays.stream(colorModel.getComponentSize()).anyMatch(bit -> bit < 8))
	    		{
	    			return "スキャンされた画像がカラーではないか、階調が足りません。";
	    		}

                int imageWidth = image.getWidth();
                int imageHeight = image.getHeight();

                float imageInchWidth = imageWithDpi.getValue()[0];
                float imageInchHeight = imageWithDpi.getValue()[1];

                // CommonsImagingに解像度が渡せないので諦めて手計算でdpiを出す。
                // 境界値の場合の端数処理が課題。
	    		if(Math.round(imageWidth/imageInchWidth) < 200
					|| Math.round(imageHeight/imageInchHeight) < 200)
	    		{
	    			return "解像度が200dpi未満です。"; // + imageWidth/imageInchWidth + " , " + imageHeight/imageInchHeight; // デバッグ用に解像度を出力
	    		}
	    	}
		} catch (Exception e) {
			return "PDF内の画像を正常に読み込めませんでした。";// + Arrays.toString(e.getStackTrace()); // デバッグ用
		};

		return "success";
	}

	/**
     * @author j_matsumoto
     * https://stackoverflow.com/questions/69494732/get-displayed-size-of-an-image-in-a-pdf-with-pdfbox をもとに作成し、不要部分を除去
     */
    private static class ImageSizeExtractor extends PDFStreamEngine {

    	/**
    	 * ページ内の画像一覧と、インチ幅・高さを格納した配列
    	 */
    	@Getter @Setter
    	private Map<RenderedImage, float[]> pageImages = new HashMap<RenderedImage, float[]>();

        /**
         * @throws IOException
         * とりあえずOperatorは全部持ってきた。SaveやRestoreは不要かもしれないので消して試すかはお任せします。
         */
        public ImageSizeExtractor() throws IOException {
            // preparing PDFStreamEngine
            addOperator(new Concatenate());
            addOperator(new DrawObject());
            addOperator(new SetGraphicsStateParameters());
            addOperator(new Save());
            addOperator(new Restore());
            addOperator(new SetMatrix());
        }

        @Override
        protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
            String operation = operator.getName();
            if ("Do".equals(operation)) {
                COSName objectName = (COSName) operands.get(0);
                // get the PDF object
                PDXObject xobject = getResources().getXObject(objectName);
                // check if the object is an image object
                if (xobject instanceof PDImageXObject) {
                    PDImageXObject image = (PDImageXObject) xobject;

                    Matrix ctmNew = getGraphicsState().getCurrentTransformationMatrix();
                    float imageInchWidth = ctmNew.getScalingFactorX() / POINTS_PER_INCH; // ScalingFactorはポイント数らしいのでインチに直す
                    float imageInchHeight = ctmNew.getScalingFactorY() / POINTS_PER_INCH;

                    float[] xy = {imageInchWidth,imageInchHeight};
                    this.pageImages.put(image.getImage(), xy);
                }
            } else {
                super.processOperator(operator, operands);
            }
        }
    }
}
