using System;
using System.Collections;
using System.Globalization;
using System.Security;
using System.Text;
using System.Collections.Generic;
using System.IO;
using System.Text.RegularExpressions;

namespace Importer.Util
{
    /// <summary>
    /// 文字列操作のユーティリティー
    /// </summary>
    public static class StringUtil
    {
        /// <summary>
        /// Encodingオブジェクトを取得（シフトＪＩＳ）
        /// </summary>
        private static readonly Encoding EncSjis = Encoding.GetEncoding("Shift_JIS");

        #region RightPadding 小数点の編集
        /// <summary>
        /// 小数点の編集
        /// </summary>
        /// <remarks>
        /// <para>指定された精度まで小数点以下の値にゼロを追加する</para>
        /// <para>数値以外の文字列を引数にした場合は空文字列が返される</para>
        /// </remarks>
        /// <param name="number">処理対処となる値のオブジェクト</param>
        /// <param name="digits">精度</param>
        /// <returns>編集後の文字列</returns>
        public static string RightPadding(object number, int digits)
        {
            // 変換後の値
            double result;

            if (Double.TryParse(number.ToString(), out result) == false)
            {
                // 値を判定し小数値と解釈されない場合はnullを返す
                return String.Empty;
            }

            string num = result.ToString();

            StringBuilder returnStr = new StringBuilder();
            char[] chrPoint = { '.' };
            string[] strSeparate = num.Split(chrPoint);
            if (strSeparate.Length == 1)
            {
                // 整数値の場合
                returnStr.Append(strSeparate[0]);
                if (digits > 0)
                {
                    returnStr.Append(".".PadRight(digits + 1, '0'));
                }
            }
            else if (strSeparate.Length == 2)
            {
                // 小数値の場合
                returnStr.Append(strSeparate[0]);
                returnStr.Append(".");
                returnStr.Append(StringUtil.SubString(strSeparate[1].PadRight(digits, '0'), digits));
            }

            return returnStr.ToString();
        }
        #endregion

        #region GetTextFromArr string配列をstring文字列に変換
        /// <summary>
        /// string配列をstring文字列に変換
        /// </summary>
        /// <remarks>
        /// 配列を１つの文字列に変換する
        /// </remarks>
        /// <param name="strArr">string配列</param>
        /// <returns>文字列に変換された配列</returns>
        public static string GetTextFromArr(string[] strArr)
        {
            StringBuilder workString = new StringBuilder();
            for (int i = 0; i < strArr.Length; i++)
            {
                if (strArr[i] == null)
                {
                }
                else
                {
                    workString.Append(strArr[i].Trim());
                }
            }
            return workString.ToString();
        }
        #endregion

        #region GetStringArray string型の配列に変換
        /// <summary>
        /// string型の配列に変換
        /// </summary>
        /// <remarks>
        /// 改行を含んだ文字列を１行毎の文字列型の配列に変換します
        /// </remarks>
        /// <param name="multiLineString">改行を含んだ文字列</param>
        /// <returns>string型の配列</returns>
        public static string[] GetStringArray(string multiLineString)
        {
            // 対象文字列を改行コード毎に区切る
            StringReader sr = new StringReader(multiLineString);

            string line = String.Empty;
            List<string> lineList = new List<string>();
            while ((line = sr.ReadLine()) != null)
            {
                lineList.Add(line);
            }

            return lineList.ToArray();
        }

        /// <summary>
        /// string型の配列に変換
        /// </summary>
        /// <remarks>
        /// 数値型<see cref="long"/>の配列を文字列型の配列に変換します
        /// </remarks>
        /// <param name="lngArr">long型の配列</param>
        /// <returns>string型の配列</returns>
        public static string[] GetStringArray(long[] lngArr)
        {
            string[] strArr = new string[lngArr.Length];
            for (int i = 0; i < lngArr.Length; i++)
            {
                strArr[i] = lngArr[i].ToString();
            }
            return strArr;
        }

        /// <summary>
        /// string型の配列に変換
        /// </summary>
        /// <remarks>
        /// 数値型<see cref="long"/>の配列を文字列型の配列に変換します<br/>
        /// 数値が0の場合は、指定された初期値を使用して文字列を作成します
        /// </remarks>
        /// <param name="lngArr">long型の配列</param>
        /// <param name="strDefault">数値が0の場合に置き換える文字</param>
        /// <returns>string型の配列（0の場合デフォルト値で補完）</returns>
        public static string[] GetStringArray(long[] lngArr, string strDefault)
        {
            string[] strArr = new string[lngArr.Length];
            for (int i = 0; i < lngArr.Length; i++)
            {
                if (lngArr[i] == 0)
                {
                    strArr[i] = strDefault;
                }
                else
                {
                    strArr[i] = lngArr[i].ToString();
                }
            }
            return strArr;
        }

        /// <summary>
        /// string型の配列に変換
        /// </summary>
        /// <remarks>
        /// 数値型<see cref="Double"/>の配列を文字列型の配列に変換します<br/>
        /// </remarks>
        /// <param name="dblArr">Double型の配列</param>
        /// <returns>string型の配列</returns>
        public static string[] GetStringArray(double[] dblArr)
        {
            string[] strArr = new string[dblArr.Length];
            for (int i = 0; i < dblArr.Length; i++)
            {
                strArr[i] = dblArr[i].ToString();
            }
            return strArr;
        }

        /// <summary>
        /// string型の配列に変換
        /// </summary>
        /// 数値型<see cref="Double"/>の配列を文字列型の配列に変換します<br/>
        /// 数値が0の場合は、指定された初期値を使用して文字列を作成します
        /// <param name="dblArr">Double型の配列</param>
        /// <param name="strDefault">数値が0の場合に置き換える文字</param>
        /// <returns>string型の２次元配列</returns>
        public static string[] GetStringArray(double[] dblArr, string strDefault)
        {
            string[] strArr = new string[dblArr.Length];
            for (int i = 0; i < dblArr.Length; i++)
            {
                if (dblArr[i] == 0)
                {
                    strArr[i] = strDefault;
                }
                else
                {
                    strArr[i] = dblArr[i].ToString();
                }
            }
            return strArr;
        }

        /// <summary>
        /// ArrayListをstring型の２次元配列に変換
        /// </summary>
        /// <remarks>
        /// １次元配列をアイテムに持つArrayListを２次元配列に変換します<br/>
        /// ArrayListの各要素は１次元配列である事
        /// </remarks>
        /// <param name="arrList">ArrayList</param>
        /// <returns>string型の２次元配列</returns>
        public static string[][] GetStringArray(ArrayList arrList)
        {
            string[][] strArr = new string[arrList.Count][];
            int i = 0;
            foreach (string[] strList in arrList)
            {
                strArr[i++] = strList;
            }
            return strArr;
        }
        #endregion

        #region StringToInt 文字列(整数)をint型に変換
        /// <summary>
        /// 文字列(整数)をint（Int32）型に変換します
        /// </summary>
        /// <remarks>
        /// <para>文字列(整数)をint（Int32）型に変換します</para>
        /// <para>変換時にエラーが発生した場合は、0を返します</para>
        /// <para>小数は0を返します</para>
        /// </remarks>
        /// <param name="objInt">文字列型の数値</param>
        /// <returns>int型に変換された値</returns>
        public static int StringToInt(object objInt)
        {
            if (objInt == null || objInt.ToString().Length <= 0)
            {
                // nullまたは、値が入っていない場合は、0を返す
                return 0;
            }

            // 変換後の値
            int result;

            Int32.TryParse(objInt.ToString().Trim(), NumberStyles.Any, NumberFormatInfo.InvariantInfo, out result);

            return result;
        }
        #endregion

        #region StringToLong 文字列(整数)をlong型に変換
        /// <summary>
        /// 文字列(整数)をlong（Int64）型に変換します
        /// </summary>
        /// <remarks>
        /// <para>文字列をlong（Int64）型に変換します</para>
        /// <para>変換時にエラーが発生した場合は、0を返します</para>
        /// <para>小数は0を返します</para>
        /// </remarks>
        /// <param name="objLong">文字列型の数値</param>
        /// <returns>long型に変換された値</returns>
        public static long StringToLong(object objLong)
        {
            if (objLong == null || objLong.ToString().Length <= 0)
            {
                // nullまたは、値が入っていない場合は、0を返す
                return 0L;
            }

            // 変換後の値
            long result;

            Int64.TryParse(objLong.ToString().Trim(), NumberStyles.Any, NumberFormatInfo.InvariantInfo, out result);

            return result;
        }
        #endregion

        #region StringToDouble 文字列をDouble型に変換
        /// <summary>
        /// 文字列をDouble型に変換します
        /// </summary>
        /// <remarks>
        /// <para>文字列をDouble型に変換します</para>
        /// <para>変換時にエラーが発生した場合は、0を返します</para>
        /// </remarks>
        /// <param name="objDbl">文字列型の数値</param>
        /// <returns>Double型に変換された値</returns>
        public static double StringToDouble(object objDbl)
        {
            if (objDbl == null || objDbl.ToString().Length <= 0)
            {
                // nullまたは、値が入っていない場合は、0を返す
                return 0d;
            }

            // 変換後の値
            double result;

            Double.TryParse(objDbl.ToString().Trim(), NumberStyles.Any, NumberFormatInfo.InvariantInfo, out result);

            return result;
        }
        #endregion

        #region StringToDecimal 文字列をDecimal型に変換
        /// <summary>
        /// 文字列をDecimal型に変換します
        /// </summary>
        /// <remarks>
        /// 文字列をDecimal型に変換します。<br/>
        /// 変換時にエラーが発生した場合は、0を返します
        /// </remarks>
        /// <param name="objDcm">文字列型の数値</param>
        /// <returns>Decimal型に変換された値</returns>
        public static decimal StringToDecimal(object objDcm)
        {
            if (objDcm == null || objDcm.ToString().Length <= 0)
            {
                // nullまたは、値が入っていない場合は、0を返す
                return 0m;
            }

            // 変換後の値
            decimal result;

            Decimal.TryParse(objDcm.ToString().Trim(), NumberStyles.Any, NumberFormatInfo.InvariantInfo, out result);

            return result;
        }
        #endregion

        #region SubString 文字列を指定されたバイト数で切る
        /// <summary>
        /// 文字列を指定されたバイト数に調整します
        /// </summary>
        /// <remarks>
        /// 文字列が指定されたバイト数を超える場合、指定されたバイト数に調整します。<br/>
        /// 全角文字が指定されたバイト数をまたがる場合、その全角文字は含まれません
        /// </remarks>
        /// <param name="moji">対象の文字列</param>
        /// <param name="len">桁数</param>
        /// <returns>指定された桁数以上だった場合、指定桁数に調整した文字列。</returns>
        public static string SubString(string moji, int len)
        {
            // nullまたは空文字の場合は空文字
            if (moji == null || moji.Length == 0)
            {
                return String.Empty;
            }

            // バイト数
            int byteCount = EncSjis.GetByteCount(moji);

            // 引数の文字長が指定された桁数以内の場合
            if (byteCount <= len)
            {
                return moji;
            }

            // 半角文字列の場合、桁数で切って返す
            if (byteCount == moji.Length)
            {
                return moji.Substring(0, len);
            }

            StringBuilder newString = new StringBuilder();

            // 全角が混ざっている場合、調整して返す。
            string strWk2 = String.Empty;
            int intPos = 0;
            for (int i = 0; i < moji.Length; i++)
            {
                strWk2 = moji.Substring(i, 1);

                if (EncSjis.GetByteCount(strWk2) == strWk2.Length)
                {
                    // 半角文字の場合
                    if (EncSjis.GetByteCount(strWk2) + intPos < len)
                    {
                        // まだ指定文字長内の場合、そのまま結合する
                        newString.Append(strWk2);
                        intPos++;
                    }
                    else if (EncSjis.GetByteCount(strWk2) + intPos == len)
                    {
                        // 指定文字長に達する場合、文字を結合してループを抜ける
                        newString.Append(strWk2);
                        break;
                    }
                }
                else
                {
                    // 全角文字の場合
                    if (EncSjis.GetByteCount(strWk2) + intPos < len)
                    {
                        // まだ指定文字長内の場合、文字を結合する
                        newString.Append(strWk2);
                        intPos = intPos + EncSjis.GetByteCount(strWk2);
                    }
                    else
                    {
                        if (EncSjis.GetByteCount(strWk2) + intPos == len)
                        {
                            // 指定文字長に達する場合、文字を結合してループを抜ける
                            newString.Append(strWk2);
                            break;
                        }
                        else
                        {
                            // 文字を結合すると、指定文字長を超えてしまうのでループを抜ける
                            break;
                        }
                    }
                }
            }

            // 指定文字長に編集した文字列を返す
            return newString.ToString();
        }
        #endregion

        #region PadRight 文字列を指定バイトに足りない分空白埋めするして返す（右側）
        /// <summary>
        /// 文字列を指定バイトまで空白を詰めます
        /// </summary>
        /// <remarks>
        /// 文字列を指定バイトになるまで文字列の右側に空白を埋めます
        /// </remarks>
        /// <param name="moji">対象の文字列</param>
        /// <param name="len">バイト数</param>
        /// <returns>指定されたバイト数以上だった場合、空白埋めした文字列。</returns>
        public static string PadRight(string moji, int len)
        {
            // nullまたは空文字の場合は空文字
            if (moji == null || moji.Length == 0)
            {
                return String.Empty;
            }

            // バイト数
            int byteCount = EncSjis.GetByteCount(moji);

            // 引数の文字長が指定されたバイト数以内の場合
            if (byteCount >= len)
            {
                return moji;
            }

            // 足りないバイト数分空白埋め
            int intPadLen = len - byteCount;
            string strWK = "a";
            strWK = strWK.PadRight(intPadLen + 1);
            strWK = strWK.Remove(0, 1);

            StringBuilder sb = new StringBuilder();
            sb.Append(moji).Append(strWK);

            return sb.ToString();
        }
        #endregion

        #region PadLeft 文字列を指定バイトに足りない分空白埋めするして返す（左側）
        /// <summary>
        /// 文字列を指定バイトまで空白を詰めます
        /// </summary>
        /// <remarks>
        /// 文字列を指定バイトになるまで文字列の左側に空白を埋めます
        /// </remarks>
        /// <param name="moji">対象の文字列</param>
        /// <param name="len">バイト数</param>
        /// <returns>指定されたバイト数以上だった場合、空白埋めした文字列。</returns>
        public static string PadLeft(string moji, int len)
        {
            // nullまたは空文字の場合は空文字
            if (moji == null || moji.Length == 0)
            {
                return String.Empty;
            }

            // バイト数
            int byteCount = EncSjis.GetByteCount(moji);

            // 引数の文字長が指定されたバイト数以内の場合
            if (byteCount >= len)
            {
                return moji;
            }

            // 足りないバイト数分空白埋め
            int intPadLen = len - byteCount;
            string strWK = "a";
            strWK = strWK.PadRight(intPadLen + 1);
            strWK = strWK.Remove(0, 1);

            StringBuilder sb = new StringBuilder();
            sb.Append(strWK).Append(moji);

            return sb.ToString();
        }
        #endregion

        #region ZeroPadRight 文字列に指定桁数に足りない分、右方向に０詰めして返す
        /// <summary>
        /// 文字列に指定桁数に足りない分、右方向に０詰めして返す
        /// </summary>
        /// <remarks>
        /// 文字列に指定桁数になるまで、右方向に０詰めして返す
        /// </remarks>
        /// <param name="moji">対象の文字列</param>
        /// <param name="length">指定桁数</param>
        /// <returns>指定された桁数以下だった場合、０埋めした文字列。</returns>
        public static string ZeroPadRight(string moji, int length)
        {
            // nullまたは空文字の場合は空文字
            if (moji == null || moji.Length == 0)
            {
                return String.Empty;
            }

            // 指定桁数より大きい場合は編集しない
            if (moji.Length >= length)
            {
                return moji;
            }

            string strWK = moji.PadRight(length, '0');
            return strWK;
        }
        #endregion

        #region ZeroPadLeft 文字列に指定桁数に足りない分、左方向に０詰めして返す
        /// <summary>
        /// 文字列に指定桁数に足りない分、左方向に０詰めして返す
        /// </summary>
        /// <remarks>
        /// 文字列に指定桁数になるまで、左方向に０詰めして返す
        /// </remarks>
        /// <param name="moji">対象の文字列</param>
        /// <param name="length">指定桁数</param>
        /// <returns>指定された桁数以下だった場合、０埋めした文字列。</returns>
        public static string ZeroPadLeft(string moji, int length)
        {
            // nullまたは空文字の場合は空文字
            if (moji == null || moji.Length == 0)
            {
                return String.Empty;
            }

            // 指定桁数より大きい場合は編集しない
            if (moji.Length >= length)
            {
                return moji;
            }

            string strWK = moji.PadLeft(length, '0');
            return strWK;
        }
        #endregion

        #region GetNumberFormatString 数値フォーマットに変換
        /// <summary>
        /// 数値フォーマットに変換
        /// </summary>
        /// <remarks>
        /// 指定された文字列をカンマ区切り(999,999,999)の文字列に変換する
        /// </remarks>
        /// <param name="value">対象の数値文字列</param>
        /// <returns>編集された文字列</returns>
        public static string GetNumberFormatString(string value)
        {
            return GetNumberFormatString(value, false, true);
        }

        /// <summary>
        /// 数値フォーマットに変換
        /// </summary>
        /// <remarks>
        /// 指定された文字列をカンマ区切り(999,999,999)の文字列に変換する
        /// </remarks>
        /// <param name="value">対象の数値文字列</param>
        /// <param name="required">true:対象文字列が空でも表示する場合、false:対象文字列が空の場合、空を返す</param>
        /// <param name="comma">true:カンマ区切りで表示する場合、false:カンマ区切りなしで表示する場合</param>
        /// <returns>編集された文字列</returns>
        public static string GetNumberFormatString(string value, bool required, bool comma)
        {
            if (value == null || value.Length <= 0)
            {
                if (required)
                {
                    return "0";
                }
                else
                {
                    return String.Empty;
                }
            }

            string minus = String.Empty;
            if (value.StartsWith("-", StringComparison.CurrentCulture))
            {
                // マイナス値の場合
                minus = "-";
                value = value.Substring(1, value.Length - 1);
            }

            string decimalString = String.Empty;
            string editString = String.Empty;

            // 小数点の位置
            int dotIndex = value.IndexOf(".", StringComparison.CurrentCulture);
            if (dotIndex >= 0)
            {
                // 小数点を含む場合、整数部分のみ編集対象とする
                editString = value.Substring(0, dotIndex).Replace(",", String.Empty);
                decimalString = value.Substring(dotIndex);
            }
            else
            {
                // 小数点を含まない場合はそのまま
                editString = value.Replace(",", String.Empty);
            }
            StringBuilder workString = new StringBuilder();
            int cnt = 0;
            for (int i = editString.Length - 1; i >= 0; i--)
            {
                workString.Insert(0, editString.Substring(i, 1));
                cnt++;
                if (cnt >= 3 && comma)
                {
                    workString.Insert(0, ",");
                    cnt = 0;
                }
            }

            if (workString.ToString().StartsWith(",", StringComparison.CurrentCulture))
            {
                workString.Remove(0, 1);
            }
            //workString.Append(decimalString);

            value = workString.ToString();

            workString.Length = 0;

            workString.Append(minus);
            workString.Append(value);
            return workString.ToString();
        }

        /// <summary>
        /// 数値フォーマットに変換
        /// </summary>
        /// <remarks>
        /// 指定された文字列をカンマ区切り(999,999,999)及び、指定された位まで小数点以下の値を編集します。<br/>
        /// 指定されたくらいに満たない場合は、0が補完されます
        /// </remarks>
        /// <param name="value">対象の数値文字列</param>
        /// <param name="intDig">小数点以下の桁数</param>
        /// <returns>編集された文字列</returns>
        public static string GetNumberFormatString(string value, int intDig)
        {
            return GetNumberFormatString(value, intDig, false, true);
        }

        /// <summary>
        /// 数値フォーマットに変換
        /// </summary>
        /// <remarks>
        /// 指定された文字列をカンマ区切り(999,999,999)及び、指定された位まで小数点以下の値を編集します。<br/>
        /// 指定されたくらいに満たない場合は、0が補完されます
        /// </remarks>
        /// <param name="value">対象の数値文字列</param>
        /// <param name="intDig">小数点以下の桁数</param>
        /// <param name="required">true:対象文字列が空でも表示する場合、false:対象文字列が空の場合、空を返す</param>
        /// <returns>編集された文字列</returns>
        public static string GetNumberFormatString(string value, int intDig, bool required)
        {
            return GetNumberFormatString(value, intDig, required, true);
        }

        /// <summary>
        /// 数値フォーマットに変換
        /// </summary>
        /// <remarks>
        /// 指定された文字列をカンマ区切り(999,999,999)及び、指定された位まで小数点以下の値を編集します。<br/>
        /// 指定されたくらいに満たない場合は、0が補完されます
        /// </remarks>
        /// <param name="value">対象の数値文字列</param>
        /// <param name="intDig">小数点以下の桁数</param>
        /// <param name="required">true:対象文字列が空でも表示する場合、false:対象文字列が空の場合、空を返す</param>
        /// <param name="comma">true:カンマ区切りで表示する場合、false:カンマ区切りなしで表示する場合</param>
        /// <returns>編集された文字列</returns>
        public static string GetNumberFormatString(string value, int intDig, bool required, bool comma)
        {
            if (intDig <= 0)
            {
                return GetNumberFormatString(value, required, comma);
            }

            // 空の場合は編集不要な場合
            if (value == null || value.Length <= 0)
            {
                if (required)
                {
                    return GetNumberFormatString("0", intDig);
                }
                else
                {
                    return String.Empty;
                }
            }

            string integerString = String.Empty;
            string decimalString = String.Empty;
            string strWk = String.Empty;

            // 小数点の位置
            int dotIndex = value.IndexOf(".", StringComparison.CurrentCulture);
            if (dotIndex > 0)
            {
                // 小数点を含む場合
                integerString = value.Substring(0, dotIndex);
                strWk = value.Substring(dotIndex + 1);
                if (strWk.Length > intDig)
                {
                    decimalString = strWk.Substring(0, intDig);
                }
                else
                {
                    decimalString = strWk.PadRight(intDig, '0');
                }
            }
            else
            {
                // 小数点を含まない場合
                integerString = value;
                decimalString = "0".PadRight(intDig, '0');
            }

            if (integerString.Trim().Length > 0)
            {
                integerString = GetNumberFormatString(integerString);
            }

            return integerString + "." + decimalString;
        }
        #endregion

        #region AddDecimal 数値の足し算
        /// <summary>
        /// 数値の足し算
        /// </summary>
        /// <remarks>
        /// 値1と値2を足し算した結果を返します。どちらの値も数値ではない場合、空文字列を返します。
        /// </remarks>
        /// <param name="paraValue1">値1</param>
        /// <param name="paraValue2">値2</param>
        /// <returns>値1と値2を加算した結果</returns>
        public static string AddDecimal(object paraValue1, object paraValue2)
        {
            if (paraValue1 == null && paraValue2 == null)
            {
                // どちらもnullの場合空を返す
                return String.Empty;
            }
            if (paraValue1.ToString().Trim().Length == 0 && paraValue2.ToString().Trim().Length == 0)
            {
                // どちらもnullの場合空を返す
                return String.Empty;
            }
            decimal decWk = 0;
            if (paraValue1.ToString().Trim().Length != 0)
            {
                // 値1を格納
                decWk = StringUtil.StringToDecimal(paraValue1.ToString().Trim());
            }
            if (paraValue2.ToString().Trim().Length != 0)
            {
                // 値2を加算
                decWk += StringUtil.StringToDecimal(paraValue2.ToString().Trim());
            }

            return decWk.ToString();
        }
        #endregion

        #region SubDecimal 数値の引き算
        /// <summary>
        /// 数値の引き算
        /// </summary>
        /// <remarks>
        /// 値1と値2を引き算した結果を返します。どちらの値も数値ではない場合、空文字列を返します。
        /// </remarks>
        /// <param name="paraValue1">値1</param>
        /// <param name="paraValue2">値2</param>
        /// <returns>値1と値2を引き算した結果</returns>
        public static string SubDecimal(object paraValue1, object paraValue2)
        {
            if (paraValue1 == null && paraValue2 == null)
            {
                // どちらもnullの場合空を返す
                return String.Empty;
            }
            if (paraValue1.ToString().Trim().Length == 0 && paraValue1.ToString().Trim().Length == 0)
            {
                // どちらもnullの場合空を返す
                return String.Empty;
            }
            decimal decWk = 0;
            if (paraValue1.ToString().Trim().Length != 0)
            {
                // 値1を格納
                decWk = StringUtil.StringToDecimal(paraValue1.ToString().Trim());
                if (paraValue2.ToString().Trim().Length != 0)
                {
                    // 値1から値2をひく
                    decWk -= StringUtil.StringToDecimal(paraValue2.ToString().Trim());
                }
            }
            else
            {
                if (paraValue2.ToString().Trim().Length != 0)
                {
                    // 値2を格納
                    decWk = StringUtil.StringToDecimal(paraValue2.ToString().Trim());
                }
            }

            return decWk.ToString();
        }
        #endregion

        #region ClearLeftZero 前ゼロ消去
        /// <summary>
        /// 文字列を取得し前ゼロを消去する
        /// </summary>
        /// <remarks>
        /// </remarks>
        /// <param name="value">文字列</param>
        /// <returns>
        /// 前ゼロを消去した文字列
        /// </returns>
        public static string ClearLeftZero(string value)
        {
            int num = value.Length;
            while (num > 0 && value.StartsWith("0", StringComparison.CurrentCulture))
            {
                value = value.Substring(1, num - 1);
                num = value.Length;
            }

            return value;
        }
        #endregion

        #region XmlElementEscape 文字列内の無効な XML 文字を等価の有効な XML に置き換えます
        /// <summary>
        /// 文字列内の無効な XML 文字を等価の有効な XML に置き換えます
        /// </summary>
        /// <param name="value">エスケープする無効な文字を含む文字列</param>
        /// <returns>置き換えられた無効な文字を含む入力文字列</returns>
        public static string XmlElementEscape(object value)
        {
            if (value == null)
            {
                return null;
            }

            StringBuilder replaceValue = new StringBuilder();

            replaceValue.Append(SecurityElement.Escape(value.ToString()));

            return replaceValue.ToString();
        }
        #endregion

        #region SubstringAtCount 文字列を指定の文字数で分割する
        /// <summary>
        /// 文字列を指定の文字数で分割する
        /// </summary>
        /// <param name="value">対象の文字</param>
        /// <param name="charaCount">文字数</param>
        /// <returns>文字数で分割した配列</returns>
        public static string[] SubstringAtCount(string value, int charaCount)
        {
            List<string> line = new List<string>();

            StringBuilder sbLine = new StringBuilder();
            for (int i = 0; i < value.Length; i++)
            {
                sbLine.Append(value.Substring(i, 1));
                if ((i + 1) % charaCount == 0)
                {
                    line.Add(sbLine.ToString());
                    sbLine.Length = 0;
                }
            }

            //指定文字数に満たない場合
            if (sbLine.Length > 0 || value == String.Empty)
            {
                line.Add(sbLine.ToString());
                sbLine.Length = 0;
            }

            return line.ToArray();
        }
        #endregion

        #region SubstringAtCountLF 文字列を指定の文字数・改行で分割する
        /// <summary>
        /// 文字列を指定の文字数・改行で分割する
        /// </summary>
        /// <param name="value">対象の文字</param>
        /// <param name="charaCount">文字数</param>
        /// <returns>文字数で分割した配列</returns>
        public static string[] SubstringAtCountLF(string value, int charaCount)
        {
            // 改行コードで分割
            StringReader sr = new StringReader(value);

            string lineOrg = String.Empty;
            List<string> line = new List<string>();
            while ((lineOrg = sr.ReadLine()) != null)
            {
                StringBuilder sbLine = new StringBuilder();
                for (int i = 0; i < lineOrg.Length; i++)
                {
                    sbLine.Append(lineOrg.Substring(i, 1));
                    if ((i + 1) % charaCount == 0)
                    {
                        line.Add(sbLine.ToString());
                        sbLine.Length = 0;
                    }
                }
                //指定文字数に満たない場合、または改行だけの行
                if (sbLine.Length > 0 || lineOrg == String.Empty)
                {
                    line.Add(sbLine.ToString());
                    sbLine.Length = 0;
                }
            }

            return line.ToArray();
        }
        #endregion

        #region CommaLineToFiledList カンマ区切りの1行から各フィールドに分解する
        /// <summary>
        /// カンマ区切りの1行から各フィールドに分解する
        /// </summary>
        /// <param name="lineData">カンマ区切りの1行</param>
        /// <returns>各フィールドのリスト</returns>
        public static IList<string> CommaLineToFiledList(string lineData)
        {
            // カンマ区切りの1行から各フィールドを取得するための正規表現
            Regex regCsv = new Regex("\\s*(\"(?:[^\"]|\"\")*\"|[^,]*)\\s*,", RegexOptions.None);

            // 行の最後の改行記号を削除
            lineData = lineData.TrimEnd(new char[] { '\r', '\n' });

            // 最後に「,」をつける
            lineData = lineData + ",";

            // 1つの行からフィールドを取り出す
            List<string> fieldList = new List<string>();
            System.Text.RegularExpressions.Match m = regCsv.Match(lineData);
            while (m.Success)
            {
                string field = m.Groups[1].Value;
                //前後の空白を削除
                field = field.Trim();
                //"で囲まれている時
                if (field.StartsWith("\"") && field.EndsWith("\""))
                {
                    //前後の"を取る
                    field = field.Substring(1, field.Length - 2);

                    //「""」を「"」にする
                    field = field.Replace("\"\"", "\"");
                }
                fieldList.Add(field);
                m = m.NextMatch();
            }

            return fieldList;
        }
        #endregion

        #region StringToBoolean 文字列(整数)をBoolean型に変換
        /// <summary>
        /// 文字列(整数)をBoolean型に変換します
        /// </summary>
        /// <remarks>
        /// <para>文字列(整数)をBoolean型に変換します</para>
        /// <para>変換時にエラーが発生した場合は、0を返します</para>
        /// <para>小数は0を返します</para>
        /// </remarks>
        /// <param name="objInt">文字列型の数値</param>
        /// <returns>int型に変換された値</returns>
        public static bool StringToBoolean(object obj)
        {
            return StringUtil.StringToInt(obj) != 0 ? true : false;
        }
        #endregion
    }
}
