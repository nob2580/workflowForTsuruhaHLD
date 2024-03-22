package eteam.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.aspose.cells.License;

import eteam.common.EteamCommon;
import eteam.common.EteamConst.Encoding;
import jxl.Cell;
import jxl.CellType;
import jxl.Range;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import lombok.Getter;

/** 
 * JavaExcepApiの機能を拡張するユーティリティ
 */
public class EteamXls {
	
	static {
		//プロセス内で１回だけ実行しておくライセンス読込
		License license = new License();
		license.setLicense("Aspose.Cells.lic");
		System.setProperty("nogc", "true");
	}
	
	/** JavaExcelApiの出力ブック */
	@Getter
	WritableWorkbook book;
	/** JavaExcelApiの出力ブックの1シート目 */
	@Getter
	WritableSheet sheet;

	/**
	 * コンストラクタ(内部用)
	 * @param book JavaExcelApiの出力ブック
	 */
	protected EteamXls(WritableWorkbook book) {
		this.book = book;
        this.sheet = book.getSheet(0);
	}

	/**
     * テンプレートブックをコピーした出力ブックをメモリ上で作成。
     * @param in               テンプレートファイル入力元
     * @param out              生成ファイル出力先
     * @return                 出力ブック
     */
    public static EteamXls createBook(InputStream in, OutputStream out) {
        try {
            
            Workbook templateBook = Workbook.getWorkbook(in);

            WorkbookSettings settings = new WorkbookSettings();
            settings.setGCDisabled(true);
            settings.setFormulaAdjust(false);
            settings.setExcelDisplayLanguage("JP");
            settings.setExcelRegionalSettings("JP");
            settings.setSuppressWarnings(true);
            
            WritableWorkbook book = Workbook.createWorkbook(out, templateBook, settings);
            return new EteamXls(book);
        } catch (BiffException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 出力ブックを閉じる。
     */
    public void closeBook() {
        try {
            convertChars();
            book.write();
            book.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (WriteException e) {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * シート選択
	 * @param index シート番号(0～)
	 */
	public void selectSheet(int index) {
		sheet = book.getSheet(index);
	}
	
    /**
     * セルに文字列を設定する。
     * @param cellName   セル名
     * @param value      文字列
     */
    public void write(String cellName, String value) {
        write(cellName, 0, 0, value);
    }
    
    /**
     * セルに文字列を設定します。
     * @param cellName   基点セル名
     * @param col        相対列番号
     * @param row        相対行番号
     * @param value      文字列
     */
    public void write(String cellName, int col, int row, String value) {
        Cell cell = getCell(cellName);
        write(cell.getColumn() + col, cell.getRow() + row, value);
    }

    /**
     * セルに文字列を設定します。
     * @param col        列番号
     * @param row        行番号
     * @param value      文字列
     */
    public void write(int col, int row, String value) {
        try {
            Label cellData = null;

            //元々のセル固有のフォーマットを取得
            CellFormat originalFormat = sheet.getCell(col, row).getCellFormat();
            
            //セル固有のフォーマットが設定されている場合、フォーマットを引き継いだ形でセル情報を作成
            if (originalFormat != null) {
                cellData = new Label(col, row, value, originalFormat);
                
            //セル固有のフォーマットが設定されていない場合、デフォルトフォーマットでセル情報を作成
            } else {
                cellData = new Label(col, row, value);
            }

            //作成したセル情報をシートに加える
            sheet.addCell(cellData);
        } catch (WriteException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 指定セルの文字サイズを変更します。
     * ※注意：未精査　罫線等、他設定にも影響与える可能性あり
     * @param cellName   基点セル名
     * @param col        相対列番号
     * @param row        相対行番号
     * @param fontSize   文字サイズ
     */
    public void changeFontSize(String cellName, int col, int row, int fontSize) {
        try {
            Cell cell = getCell(cellName);
            int chgRow = cell.getRow() + row;
            int chgCol = cell.getColumn() + col;
			WritableCell chgCell = sheet.getWritableCell(chgCol, chgRow);
			WritableCellFormat format;
			if(chgCell.getCellFormat() != null) {
				format = new WritableCellFormat(chgCell.getCellFormat());
			}else {
				format = new WritableCellFormat();
			}
			
			WritableFont font = new WritableFont(format.getFont());
			font.setPointSize(fontSize);
			format.setFont(font);
			chgCell.setCellFormat(format);
        } catch (WriteException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * セルに画像を貼り付ける。
     * @param cellName   セル名
     * @param xsize      横幅(セル数)
     * @param ysize      縦幅(セル数)
     * @param imageData  画像ファイルデータ
     */
    public void writeImage(String cellName, int xsize, int ysize, byte[] imageData) {
        Cell cell = getCell(cellName);
        writeImage(cell.getColumn(), cell.getRow(), xsize, ysize, imageData);
    }
    
    /**
     * セルに画像を貼り付ける。
     * @param cellName   基点セル名
     * @param col        相対列番号
     * @param row        相対行番号
     * @param xsize      横幅
     * @param ysize      縦サイズ
     * @param imageData  画像ファイルデータ(PNG)
     */
    public void writeImage(String cellName, int col, int row, int xsize, int ysize,  byte[] imageData) {
        Cell cell = getCell(cellName);
        writeImage(cell.getColumn() + col, cell.getRow() + row, xsize, ysize, imageData);
    }

    /**
     * セルに画像を貼り付ける。
     * @param col        列番号
     * @param row        行番号
     * @param xsize      横幅(セル数)
     * @param ysize      縦幅(セル数)
     * @param imageData  画像ファイルデータ(PNG)
     */
    public void writeImage(int col, int row, int xsize, int ysize, byte[] imageData) {
        WritableImage image = new WritableImage(col, row, xsize, ysize, imageData);
        sheet.addImage(image);
    }
 
	/**
     * セルに色をつけた文字列を設定します。
     * @param cellName   基点セル名
     * @param col        相対列番号
     * @param row        相対行番号
     * @param value      文字列
     * @param color     文字色
     */
    public void writeWithColor(String cellName, int col, int row, String value, Colour color) {
    	Cell cell = getCell(cellName);
    	col = cell.getColumn() + col;
    	row = cell.getRow() + row;
        
    	try {
    		
    		Label cellData = null;

    		//元々のセル固有のフォーマットを取得
    		CellFormat originalFormat = sheet.getCell(col, row).getCellFormat();

    		//セル固有のフォーマットが設定されている場合、フォーマットを引き継いだ形でセル情報を作成
    		if (originalFormat != null) {
    			WritableCellFormat format = new WritableCellFormat(originalFormat);
    			WritableFont font = new WritableFont(originalFormat.getFont());
    			font.setColour(color);
    			format.setFont(font);
    			
    			cellData = new Label(col, row, value, format);
    		//セル固有のフォーマットが設定されていない場合、デフォルトフォーマットでセル情報を作成
    		} else {
    			WritableCellFormat format = new WritableCellFormat();
    			WritableFont font = new WritableFont(format.getFont());
    			font.setColour(color);
    			format.setFont(font);
    			
    			cellData = new Label(col, row, value ,format);
    		}

    		//作成したセル情報をシートに加える
    		sheet.addCell(cellData);
    	} catch (WriteException e) {
    		throw new RuntimeException(e);
    	}
	}
    
	/**
	 * セルに色をつけた文字列を設定します。
	 * @param col 列番号
	 * @param row 行番号
	 * @param value 文字列
	 * @param color 文字色
	 */
	public void writeWithColor(int col, int row, String value, Colour color) {
		try {
			
			Label cellData = null;

			//元々のセル固有のフォーマットを取得
			CellFormat originalFormat = sheet.getCell(col, row).getCellFormat();

			//セル固有のフォーマットが設定されている場合、フォーマットを引き継いだ形でセル情報を作成
			if (originalFormat != null) {
				WritableCellFormat format = new WritableCellFormat(originalFormat);
				WritableFont font = new WritableFont(originalFormat.getFont());
				font.setColour(color);
				format.setFont(font);
				
				cellData = new Label(col, row, value, format);
			//セル固有のフォーマットが設定されていない場合、デフォルトフォーマットでセル情報を作成
			} else {
				WritableCellFormat format = new WritableCellFormat();
				WritableFont font = new WritableFont(format.getFont());
				font.setColour(color);
				format.setFont(font);
				
				cellData = new Label(col, row, value ,format);
			}

			//作成したセル情報をシートに加える
			sheet.addCell(cellData);
		} catch (WriteException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * セルの背景色を変更します。
	 * @param cellName セル名
     * @param col 相対列番号
     * @param row 相対行番号
	 * @param color 背景色
	 */
	public void writeBackgroundColor(String cellName, int col, int row, Colour color) {
		Cell cell = getCell(cellName);
		WritableCellFormat format = new WritableCellFormat(cell.getCellFormat());
		try {
			format.setBackground(color);
			Label label = new Label(cell.getColumn() + col, cell.getRow() + row, cell.getContents() ,format);
			sheet.addCell(label);
			
		} catch (WriteException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
     * セルの色と文字列の色を設定します。
     * @param cellName   基点セル名
     * @param col        相対列番号
     * @param row        相対行番号
     * @param value      文字列
     * @param fontcolor     文字色
	 * @param backColor セルの色
     */
    public void writeWithColorBackgroundColor(String cellName, int col, int row, String value, Colour fontcolor, Colour backColor) {
    	Cell cell = getCell(cellName);
    	col = cell.getColumn() + col;
    	row = cell.getRow() + row;
        
    	try {
    		
    		Label cellData = null;

    		//元々のセル固有のフォーマットを取得
    		CellFormat originalFormat = sheet.getCell(col, row).getCellFormat();

    		//セル固有のフォーマットが設定されている場合、フォーマットを引き継いだ形でセル情報を作成
    		if (originalFormat != null) {
    			WritableCellFormat format = new WritableCellFormat(originalFormat);
    			WritableFont font = new WritableFont(originalFormat.getFont());
    			font.setColour(fontcolor);
    			format.setFont(font);
    			format.setBackground(backColor);
    			
    			cellData = new Label(col, row, value, format);
    		//セル固有のフォーマットが設定されていない場合、デフォルトフォーマットでセル情報を作成
    		} else {
    			WritableCellFormat format = new WritableCellFormat();
    			WritableFont font = new WritableFont(format.getFont());
    			font.setColour(fontcolor);
    			format.setFont(font);
    			format.setBackground(backColor);
    			
    			cellData = new Label(col, row, value ,format);
    		}

    		//作成したセル情報をシートに加える
    		sheet.addCell(cellData);
    	} catch (WriteException e) {
    		throw new RuntimeException(e);
    	}
	}
	
	
    /**
     * 範囲指定コピーを行います。
     * コピー元範囲内に結合セルを含む場合、結合セルの一部だけを含めた状態になっていないことに注意してください。
     * コピー先範囲内に既に結合されたセルが存在していないようにしてください。
     * @param originalStartCol   コピー元開始列番号
     * @param originalStartRow   コピー元開始行番号
     * @param originalEndCol     コピー元終了列番号
     * @param originalEndRow     コピー元終了行番号
     * @param copyCol            コピー先開始列番号
     * @param copyRow            コピー先開始行番号
     */
    public void copy(
    		int originalStartCol, int originalStartRow,
            int originalEndCol, int originalEndRow,
            int copyCol, int copyRow) {

        try {

            //コピー元の全セル情報取得
            List<WritableCell[]> cellsArray = copyRowGetBase(originalStartCol, originalStartRow, originalEndCol, originalEndRow);
            
            //コピー元シート内の結合情報を取得
            Range[] originalRanges = sheet.getMergedCells();
            
            //コピー行数と列数を計算
            int rowCount = originalEndRow - originalStartRow + 1;
            int colCount = originalEndCol - originalStartCol + 1;
        
            //コピー元の結合状態をコピー先でも作り出す。
            if (originalRanges != null) {

                //結合セルの個数回繰り返す
                for (int i = 0; i < originalRanges.length; i++) {
                    
                    //コピー元マージセルの左上角、右下角位置を取得
                    int topRow        = originalRanges[i].getTopLeft().getRow();
                    int bottomRow    = originalRanges[i].getBottomRight().getRow();
                    int leftCol        = originalRanges[i].getTopLeft().getColumn();
                    int rightCol    = originalRanges[i].getBottomRight().getColumn();
                    
                    //コピー元の範囲内に結合セルが含まれれば、その結合セルはコピーする必要がある
                    if (
                        originalStartRow <= topRow && bottomRow <= originalEndRow &&
                        originalStartCol <= leftCol && rightCol <= originalEndCol
                    ) {
                        
                        //コピー元結合セルのコピー元範囲内での相対位置を計算
                        int originalRangeRelativeStartCol = originalRanges[i].getTopLeft().getColumn() - originalStartCol;
                        int originalRangeRelativeStartRow = originalRanges[i].getTopLeft().getRow() - originalStartRow;
                        int originalRangeRelativeEndCol = originalRanges[i].getBottomRight().getColumn() - originalStartCol;
                        int originalRangeRelativeEndRow = originalRanges[i].getBottomRight().getRow() - originalStartRow;
                        
                        //コピー先シートでセルの結合を行う。
                        sheet.mergeCells(
                                copyCol + originalRangeRelativeStartCol,
                                copyRow + originalRangeRelativeStartRow, 
                                copyCol + originalRangeRelativeEndCol,
                                copyRow + originalRangeRelativeEndRow);
                    }
                }
            }

            //全セルをコピー
            for (int i = 0; i < rowCount; i++) {
                
                //コピー元と位置だけ変えて中身は同じセル情報を作り出し、コピー先シートに設定
                WritableCell[] cells = cellsArray.get(i);
                for (int j = 0; j < colCount; j++) {
                	sheet.addCell(cells[j].copyTo(copyCol + j, copyRow + i));
                }

                //行高設定(コピー元の行高に合わせる)
                sheet.setRowView(copyRow + i, sheet.getRowView(originalStartRow + i).getSize());
            }
            
            //EMPTY行の高さが自動変更されてしまうバグ回避
            for (int i = 0; i < rowCount; i++) {
                resetRowHeight(originalStartRow + i);
            }
        } catch (RowsExceededException e) {
            throw new RuntimeException(e);
        } catch (WriteException e) {
        }
    }
    
    /**
     * 行コピーを行う。
     * @param originalRow        コピー元の行番号
     * @param copyRow            コピー先の行番号
     * @param rowCount           コピー行数
     * @param loopCount          コピー回数
     * @param isInsert           インサートするかどうか
     */
    public void copyRow(
            int originalRow,
            int copyRow,
            int rowCount, int loopCount, boolean isInsert) {
        
        //コピー元の列の最大値を取得⇒0～最大列が横のコピー範囲となる
        int originalColMax = sheet.getColumns();

        //挿入の場合、コピーの前にEMPTY行を作り出す。
        if (isInsert) {
            for (int i = 0; i < rowCount * loopCount; i++) {
                sheet.insertRow(copyRow);
            }
        }

        //１回ずつコピーを行う。
        for (int i = 0; i < loopCount; i++) {
            copy(
                    0, originalRow, originalColMax, originalRow + rowCount - 1,
                    0, copyRow + i * rowCount);
        }
    }
    
    /**
     * 任意の範囲から任意行分だけ、セル群を取得します。
     * @param startCol 取得開始列番号
     * @param startRow 取得開始行番号
     * @param endCol   取得終了列番号
     * @param endRow   取得終了行番号
     * @return         WritableCell[]のリスト
     */
    protected List<WritableCell[]> copyRowGetBase(int startCol, int startRow, int endCol, int endRow) {
        int copyRows = endRow - startRow + 1;    //コピー範囲行数
        int copyCols = endCol - startCol + 1;    //コピー範囲列数
        List<WritableCell[]> cellsArray = new ArrayList<>(copyRows);    //セル群の配列(要素数は行数分)

        //行単位のループ
        for (int i = 0; i < copyRows; i++) {

            //1行分のセルデータを格納するセルオブジェクト配列
            WritableCell[] cells = new WritableCell[copyCols];

            //列単位のループで、セルオブジェクト配列１つずつ取得
            for (int j = 0; j < copyCols; j++) {
                cells[j] = sheet.getWritableCell(j + startCol, startRow + i); // 1セル分の情報を取得
            }

            // 1行分のデータをアレイリストに追加
            cellsArray.add(cells);
        }
        return cellsArray;
    }
    
    /**
     * セル名よりセルを取得する。
     * 該当するセルが見つからない場合、例外発生。
     * @param cellName セル名
     * @return セル
     */
    public Cell getCell(String cellName) {
        Cell cell = book.findCellByName(cellName);
        if (cell == null) {
            throw new InvalidParameterException();
        }
        return cell;
    }
    
    /**
     * セル名よりセルを取得する。
     * 該当するセルが見つからない場合、例外発生。
     * @param cellName セル名
     * @return セル
     */
    public boolean isExistCell(String cellName) {
        Cell cell = book.findCellByName(cellName);
        return cell != null;
    }

	/**
	 * 改頁
	 * @param row この行前で改頁
	 */
	public void pageBreak(int row) {
		sheet.addRowPageBreak(row);
	}
    
    //JXLのバグ(と思われる)を回避する為のロジック。
    /**
     * 行の高さを元に戻す。<br>
     * 勝手に行の高さが変わってしまう恐れのある場合は、本メソッドを呼び出す。
     * @param row        行番号
     */
    protected void resetRowHeight(int row) {
        try {
            sheet.setRowView(row, sheet.getRowView(row).getSize());
        } catch (RowsExceededException e) {
            throw new RuntimeException(e);
        }
    }
    
    /** 共通置換テーブル */
    protected static final String[] SIMILAR_CHARS_COMMON_FROM = new String[]{
        "\u00AD", "\u2011", "\u2012", "\u2013", "\u2043", "\uFE63", //半角ハイフン
        "\u223C", "\u223E", //半角波線→半角チルダ
        "\u22EF", //3点
        "\u00B7", "\u2022", "\u2219", "\u22C5" //半角中点
    };
    /** 共通置換テーブル */
    protected static final String[] SIMILAR_CHARS_COMMON_TO = new String[]{
        "\u002D", "\u002D", "\u002D", "\u002D", "\u002D", "\u002D", //半角ハイフン
        "\u007E", "\u007E", //半角波線→半角チルダ
        "\u2026", //3点
        "\uFF65", "\uFF65", "\uFF65", "\uFF65" //半角中点
    };
    /** エンコーディング別置換テーブル */
    protected static final String[] SIMILAR_CHARS_W31J_FROM;
    /** エンコーディング別置換テーブル */
    protected static final String[] SIMILAR_CHARS_W31J_TO;
    static {
        SIMILAR_CHARS_W31J_FROM = ArrayUtils.addAll(SIMILAR_CHARS_COMMON_FROM, new String[]{
            "\u2212"/*全角マイナス*/, "\u2014"/*強調引用*/, "\u3030", "\u301C"/*波線*/
        });
        SIMILAR_CHARS_W31J_TO = ArrayUtils.addAll(SIMILAR_CHARS_COMMON_TO, new String[]{
            "\uFF0D"/*全角マイナス*/, "\u2015"/*強調引用*/, "\uFF5E", "\uFF5E"/*波線*/
        });
    }

    /**
     * 表示シートの全てのLabelに対し、エンコーディングをやり直す。
     */
    protected void convertChars() {
        //全行に対し
        int rowLen = sheet.getRows();
        for (int rowIndex = 0; rowIndex < rowLen; rowIndex++) {
            Cell[] arrCell = sheet.getRow(rowIndex);
            
            //全列に対し
            int cellLen = arrCell.length;
            for (int colIndex = 0; colIndex < cellLen; colIndex++) {
                Cell cell = arrCell[colIndex];
                
                //LABELに対してのみ
                if (null != cell && CellType.LABEL == cell.getType()) {
                    Label label = (Label) cell;
                    String str1 = label.getString();
                    
                    //中身がある場合のみ
                    if (null != str1 && ! "".equals(str1)) {
//                        label.setString(new String(label.getString().getBytes("Cp943C"), "MS932"));
                    	label.setString(StringUtils.replaceEach(label.getString(), SIMILAR_CHARS_W31J_FROM, SIMILAR_CHARS_W31J_TO));
                    }
                }
            }
        }
    }

    /**
     * 行の高さ変更。
     * 指定行数分（１行１８ピクセル想定）の高さに変更する。
     * 行数 <= 最低行数 なら何もしない。
     * @param row 行
     * @param lineNum 行数
     * @param minLineNum 最低行数
     */
	public void setHeight(int row,  int lineNum, int minLineNum) {
		if (minLineNum < lineNum) {
			try {
				sheet.setRowView(row, 270 * lineNum);
			} catch (RowsExceededException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
    /**
     * 行の高さ変更。
     * @param row 行
     * @param height        高さ
     */
	public void setHeightFree(int row,  int height) {
		try {
			sheet.setRowView(row, height);
		} catch (RowsExceededException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 行の高さを0に変更し、隠す。
	 * 
	 * @param row 行
	 */
	public void hideRow(int row) {
		try {
			sheet.setRowView(row, 0);
		} catch (RowsExceededException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 行の高さを0に変更し、隠す。
	 * 
	 * @param firstCol 最初の列
	 * @param lastCol 最後の列
	 */
	public void hideColumns(int firstCol, int lastCol) {
		for (int i = firstCol; i < lastCol; i++) {
			sheet.setColumnView(i, 0);
		}
	}
	
	/**
	 * 指定セルの罫線を引く
	 * @param cellName セル名
	 * @param border 罫線位置
	 * @param style 罫線書式
	 */
	public void setBorder(String cellName, Border border, BorderLineStyle style){
        setBorder(cellName, 0, 0, border, style);
	}
	
	/**
	 * 指定セルの罫線を引く
	 * @param cellName セル名
	 * @param row 指定行
	 * @param col 指定行
	 * @param border 罫線位置
	 * @param style 罫線書式
	 */
	public void setBorder(String cellName, int row, int col, Border border, BorderLineStyle style){
        Cell cell = getCell(cellName);
        setBorder(cell.getRow() + row, cell.getColumn() + col, border, style);
	}
	
	/**
	 * 指定セルの罫線を引く
	 * @param row 指定行
	 * @param col 指定行
	 * @param border 罫線位置
	 * @param style 罫線書式
	 */
	public void setBorder(int row, int col, Border border, BorderLineStyle style){
		try {
			WritableCell cell = sheet.getWritableCell(col, row);
			WritableCellFormat format = new WritableCellFormat(cell.getCellFormat());
			format.setBorder(border, style);
			cell.setCellFormat(format);
		} catch (WriteException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 指定範囲の周りを罫線で囲う。
	 * 指定範囲は１行ずつ横にマージされている前提。
	 * (row,col)で指定したセル（横結合前提）からlineNum行数分罫線で囲う。
	 * 
	 * @param row 囲い開始位置（行）
	 * @param col 囲い開始位置（列）
	 * @param lineNum 囲い行数(1～)
	 */
	public void encloseInBorder(int row, int col, int lineNum) {
		for (int i = 0; i < lineNum; i++) {
			try {
				WritableCell cell = sheet.getWritableCell(col, row + i);
				WritableCellFormat format = new WritableCellFormat(cell.getCellFormat());
		        
				if (lineNum == 1) {
					//１行だけの範囲指定なので、１セル全方位
					format.setBorder(Border.ALL, BorderLineStyle.THIN);
				} else {
					//２行以上の範囲指定なので、必ず左右罫線引いて、天井セルと底辺セルの罫線は個別に
					format.setBorder(Border.LEFT, BorderLineStyle.THIN);
					format.setBorder(Border.RIGHT, BorderLineStyle.THIN);
					if (i == 0) {
						format.setBorder(Border.TOP, BorderLineStyle.THIN);
					} else if (i == lineNum - 1) {
						format.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
					}
				}
				cell.setCellFormat(format);
			} catch (WriteException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * 指定セルを囲う罫線を作成する。
	 * 
	 * @param cellName セル名
	 */
	public void makeBorder(String cellName) {
		try {
			int col = getCell(cellName).getColumn();
			int row = getCell(cellName).getRow();
			WritableCell cell = sheet.getWritableCell(col, row);
			WritableCellFormat format = new WritableCellFormat(cell.getCellFormat());
			format.setBorder(Border.ALL, BorderLineStyle.THIN);
			cell.setCellFormat(format);
		} catch (WriteException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * セルをマージする
	 * ※結合対象セルの一部がすでに結合されている状態だと動作しないため注意
	 * @param cellName1 セル名1
	 * @param row1 行1
	 * @param col1 列1
	 * @param cellName2 セル名2
	 * @param row2 行2
	 * @param col2 列2
	 */
	public void merge(String cellName1, int row1, int col1, String cellName2, int row2, int col2){
        Cell cell1 = getCell(cellName1);
        Cell cell2 = getCell(cellName2);
        merge(cell1.getRow() + row1, cell1.getColumn() + col1, cell2.getRow() + row2, cell2.getColumn() + col2);
	}
	
	/**
	 * セルをマージする
	 * ※結合対象セルの一部がすでに結合されている状態だと動作しないため注意
	 * @param row1 行1
	 * @param col1 列1
	 * @param row2 行2
	 * @param col2 列2
	 */
	public void merge(int row1, int col1, int row2, int col2){
		try {
			sheet.mergeCells(col1, row1, col2, row2);
		} catch (WriteException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 行数のカウント。
	 * 文字列を改行コードで区切る。更に１行ずつ見ていって、length bytesより長い行があれば、その行は複数行とみなす。
	 * 全行数を返す。ただし、defCount未満の行数ならばdefCountを返す。（最低限、N行分の高さは確保する、というような意図）
	 * 例）s = 「あいうえお\r\nかきくけこ」に対して
	 * 　　length = 5, defCount = 1 → ret = 2
	 * 　　length = 5, defCount = 2 → ret = 2
	 * 　　length = 1, defCount = 1 → ret = 10
	 * 　　length = 5, defCount = 1 → ret = 2
	 * 　　length = 10, defCount = 1 → ret = 1
	 * @param s 文字列
	 * @param length １行あたりのバイト数
	 * @param defCount デフォルト行数
	 * @return 行数
	 */
	public int lineCount(String s, int length, int defCount) {
		String crChars = EteamCommon.getCRChars(s);
		String[] lines = crChars.equals("")? new String[]{s} : s.split(crChars);
		int totalLinelNum = 0;
		if (0 == lines.length)
		{
			return defCount;
		}

		for (String line : lines) {
			try {
				int lineCharNum = line.getBytes(Encoding.MS932).length;
				int curLineNum = line.getBytes(Encoding.MS932).length / length;
				if (0 < lineCharNum % length)
				{
					curLineNum++;
				}
				if (0 == curLineNum)
				{
					curLineNum = 1;
				}
				totalLinelNum += curLineNum;
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		return totalLinelNum < defCount ? defCount : totalLinelNum;
	}
	/**
	 * 文字列を改行コードまたは規定バイト分で区切る。
	 * @param s 文字列
	 * @param length １行あたりのバイト数
	 * @return 区切った結果
	 */
	public List<String> splitLine(String s, int length) {
		String crChars = EteamCommon.getCRChars(s);
		String[] lines = crChars.equals("")? new String[]{s} : s.split(crChars);
		List<String> ret = new ArrayList<>();

		for (String line : lines) {
			ret.addAll(splitStr(line, length));
		}
		return ret;
	}
	
	/**
	 * 文字列を規定バイト分で区切る。
	 * @param s 文字列
	 * @param length １行あたりのバイト数
	 * @return 区切った結果
	 */
	protected List<String> splitStr(String s, int length) {
		List<String> ret = new ArrayList<>();
		StringBuffer buf = new StringBuffer();
		int byteCount = 0;
		for (int i = 0; i < s.length(); i++) {
			String c = s.substring(i, i + 1);
			int cBLen;
			try {
				cBLen = c.getBytes(Encoding.MS932).length;
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			if (byteCount + cBLen > length) {
				ret.add(buf.toString());
				buf = new StringBuffer();
				byteCount = 0;
			}
			buf.append(c);
			byteCount += cBLen;
		}
		ret.add(buf.toString());
		return ret;
	}
}