using System;
using System.Globalization;
using System.Text;
using System.Text.RegularExpressions;
using System.IO;

namespace Importer.Util
{
    /// <summary>
    /// チェック関数のユーティリティー
    /// </summary>
    public static class CheckUtil
    {
        /// <summary>
        /// Encodingオブジェクトを取得（シフトＪＩＳ）
        /// </summary>
        private static readonly Encoding EncSjis = Encoding.GetEncoding("Shift_JIS");

        /// <summary>
        /// Encodingオブジェクトを取得（UTF8）
        /// </summary>
        private static readonly Encoding EncUtf8 = Encoding.UTF8;

        /// <summary>
        /// 半角スペース
        /// </summary>
        private const string TokenSpace = " ";

        #region IsNullOrEmpty nullまたは空文字列かチェック
        /// <summary>
        /// nullまたは空文字列かチェック
        /// </summary>
        /// <param name="value">チェック対象の文字列</param>
        /// <returns>判定結果
        /// <para>true：nullまたは空文字列</para>
        /// <para>false：nullまたは空文字列ではない</para>
        /// </returns>
        public static bool IsNullOrEmpty(string value)
        {
            if (value != null)
            {
                return value.Length == 0;
            }

            return true;
        }
        #endregion

        #region IsNotNullOrEmpty nullまたは空文字列ではないかチェック
        /// <summary>
        /// nullまたは空文字列ではないかチェック
        /// </summary>
        /// <param name="value">チェック対象の文字列</param>
        /// <returns>判定結果
        /// <para>true：nullまたは空文字列ではない</para>
        /// <para>false：nullまたは空文字列</para>
        /// </returns>
        public static bool IsNotNullOrEmpty(string value)
        {
            return !IsNullOrEmpty(value);
        }
        #endregion

        #region IsHalfSizeStr 半角文字チェック
        /// <summary>
        /// 半角文字チェック
        /// </summary>
        /// <remarks>
        /// 半角文字のみの文字列かをチェックします
        /// <para>nullまたは空文字列の場合はtrueを返します</para>
        /// </remarks>
        /// <param name="value">チェック対象の文字列</param>
        /// <returns>判定結果
        /// <para>true：全て半角の場合</para>
        /// <para>false：全て半角でない場合</para>
        /// </returns>
        public static bool IsHalfSizeStr(string value)
        {
            if (IsNullOrEmpty(value))
            {
                return true;
            }

            // シフトJISに変換後バイト数をカウント
            int intNum = EncSjis.GetByteCount(value);

            return intNum == value.Length;
        }
        #endregion

        #region IsContainedSpace 半角スペースの存在チェック
        /// <summary>
        /// 半角スペースの存在チェック
        /// </summary>
        /// <remarks>
        /// 対象の文字列に半角スペースが存在するかチェックします
        /// <para>nullまたは空文字列の場合はfalseを返します</para>
        /// </remarks>
        /// <param name="value">チェック対象の文字列</param>
        /// <returns>判定結果
        /// <para>true：スペースが存在する</para>
        /// <para>false：スペースが存在しない</para>
        /// </returns>
        public static bool IsContainedSpace(string value)
        {
            if (IsNullOrEmpty(value))
            {
                return false;
            }

            // 文字列内にスペースが含まれている場合はtrue
            return value.Contains(TokenSpace);
        }
        #endregion

        #region CheckMaxByteLength バイト数チェック
        /// <summary>
        /// バイト数チェック
        /// </summary>
        /// <remarks>
        /// 指定されたバイト数より小さい（または等しい）かをチェックします
        /// <para>nullまたは空文字列の場合はtrueを返します</para>
        /// </remarks>
        /// <param name="value">チェック対象の文字列</param>
        /// <param name="len">バイト数</param>
        /// <returns>判定結果
        /// <para>true：指定されたバイト数よりも小さいもしくは同じ場合</para>
        /// <para>false：指定されたバイト数よりも大きい場合</para>
        /// </returns>
        public static bool CheckMaxByteLength(string value, int len)
        {
            if (IsNullOrEmpty(value))
            {
                return true;
            }

            // 文字列のバイト数取得
            int intByteCount = EncUtf8.GetByteCount(value);

            return intByteCount <= len;
        }
        #endregion

        #region CheckMaxLength 桁数チェック
        /// <summary>
        /// 桁数チェックメソッド
        /// </summary>
        /// <remarks>
        /// コントロールのCheckLengthが設定されている場合にテキストの桁数をチェックする。<br/>
        /// CheckLengthへはカンマ区切りで数値を指定する。<br/>
        /// テキストに入力された桁数が指定された桁数以外の場合はメッセージを表示する。
        /// </remarks>
        /// <param name="value">値</param>
        /// <param name="len">桁数</param>
        /// <returns>true:チェックOK false:チェックNG</returns>
        public static bool CheckMaxLength(string value, string len)
        {
            // コントロール未入力の場合
            if (value.Length == 0)
            {
                return true;
            }

            // チェック桁数未設定の場合
            if (len == null || "".Equals(len))
            {
                return true;
            }

            char[] ch = { char.Parse(",") };

            string[] strChkLen = len.Split(ch);

            bool blnRet = false;

            foreach (string strLength in strChkLen)
            {
                if (value.Length == int.Parse(strLength))
                {
                    blnRet = true;
                }
            }

            return blnRet;
        }
        #endregion

        #region CheckMaxLengthWithSign 桁数チェック
        /// <summary>
        /// 桁数チェックメソッド（符号あり）
        /// </summary>
        /// <remarks>
        /// コントロールのCheckLengthが設定されている場合にテキストの桁数をチェックする。<br/>
        /// CheckLengthへはカンマ区切りで数値を指定する。<br/>
        /// テキストに入力された桁数が指定された桁数以外の場合はメッセージを表示する。
        /// </remarks>
        /// <param name="value">チェック値</param>
        /// <param name="len">チェック桁数（カンマ区切りの数値）</param>
        /// <returns>true:チェックOK false:チェックNG</returns>
        public static bool CheckMaxLengthWithSign(string value, string len)
        {
            int intConLength = value.Length;

            // コントロール未入力の場合
            if (intConLength == 0)
            {
                return true;
            }

            // チェック桁数未設定の場合
            if (len == null || "".Equals(len))
            {
                return true;
            }

            char[] ch = { char.Parse(",") };
            string[] strChkLen = len.Split(ch);

            bool blnRet = false;
            foreach (string strLength in strChkLen)
            {
                if (intConLength == int.Parse(strLength))
                {
                    blnRet = true;
                }
            }

            return blnRet;
        }
        #endregion

        #region CheckMaxByteLengthWithSign バイト数チェック
        /// <summary>
        /// バイト数チェック（符号あり）
        /// </summary>
        /// <remarks>
        /// 指定されたバイト数より大きいか小さい（または等しい）かをチェックします
        /// </remarks>
        /// <param name="value">チェック対象のコントロール</param>
        /// <param name="len">バイト数</param>
        /// <returns>判定結果
        /// true: 指定されたバイト数よりも小さいもしくは同じ場合、false : 指定されたバイト数よりも大きい場合</returns>
        public static bool CheckMaxByteLengthWithSign(string value, int len)
        {
            // コントロール未入力の場合
            if (value.Length <= 0)
            {
                return true;
            }

            string strString = value.Replace("-", "");
            int intByteCount = EncUtf8.GetByteCount(strString);

            if (intByteCount > len)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        #endregion

        #region IsMatchRegularExpressions 正規表現による文字列チェック
        /// <summary>
        /// 正規表現を使用して文字列をチェックする
        /// </summary>
        /// <remarks>
        /// 指定された正規表現に一致するかをチェックします
        /// </remarks>
        /// <param name="checkStr">チェック対象の文字列</param>
        /// <param name="regExpres">正規表現</param>
        /// <returns>判定結果
        /// <para>true : 正規表現にマッチする文字列有り</para> 
        /// <para>false : 正規表現にマッチする文字列無し</para>
        /// </returns>
        public static bool IsMatchRegularExpressions(string checkStr, string regExpres)
        {
            Regex regex = new Regex(regExpres);

            return regex.IsMatch(checkStr);
        }
        #endregion

        #region IsHalfNumber 半角数字チェック
        /// <summary>
        /// 半角数字チェック
        /// </summary>
        /// <remarks>
        /// 対象文字列が半角数字（0～9）だけで構成されているかチェックします
        /// </remarks>
        /// <param name="checkStr">チェック対象の文字列</param>
        /// <returns>判定結果
        /// <para>true：半角数字だけの文字列</para>
        /// <para>false：半角数字以外の文字を含む文字列</para>
        /// </returns>
        public static bool IsHalfNumber(string checkStr)
        {
            return IsMatchRegularExpressions(checkStr, "^[0-9]+$");
        }
        #endregion

        #region IsTimeFormatHhmi 時間型フォーマットのチェック(HHMI)
        /// <summary>
        /// 時間型フォーマットのチェック(00:00から23:59を許す)
        /// </summary>
        /// <remarks>
        /// 文字列が時間型フォーマット（00:00から23:59）であるかチェックします
        /// </remarks>
        /// <param name="value">チェック対象の文字列</param>
        /// <returns>判定結果
        /// <para>true：正常なフォーマット</para>
        /// <para>false：異常なフォーマット</para>
        /// </returns>
        public static bool IsTimeFormatHhmi(string value)
        {
            return IsMatchRegularExpressions(value, "^([01]?[0-9]|2[0-3]):?([0-5][0-9])$");
        }
        #endregion

        #region IsTimeFormat 時間型フォーマットのチェック
        /// <summary>
        /// 時間型フォーマットのチェック(00:00から99:59を許す)
        /// </summary>
        /// <remarks>
        /// 文字列が時間型フォーマット（HH:MM、最大値99:59）であるかチェックします
        /// </remarks>
        /// <param name="value">チェック対象の文字列</param>
        /// <returns>判定結果
        /// <para>true：正常なフォーマット</para>
        /// <para>false：異常なフォーマット</para>
        /// </returns>
        public static bool IsTimeFormat(string value)
        {
            return IsMatchRegularExpressions(value, "^([0-9]{1,2}):?([0-5][0-9])$");
        }
        #endregion

        #region IsIPAdressFormat IPアドレスのフォーマットチェック
        /// <summary>
        /// IPアドレスのフォーマットチェック
        /// </summary>
        /// <remarks>
        /// 対象文字列が半角数字または*で表されたIPアドレスのフォーマットになっているかチェックします
        /// </remarks>
        /// <param name="value">チェック対象の文字列</param>
        /// <returns>判定結果
        /// <para>true：正常なフォーマット</para>
        /// <para>false：異常なフォーマット</para>
        /// </returns>
        public static bool IsIPAdressFormat(string value)
        {
            return CheckUtil.IsMatchRegularExpressions(value, @"^((\d{1,3}|\*)\.){3}(\d{1,3}|\*)$");
        }
        #endregion

        #region IsHalfAlphabetNumber 半角英数字チェック
        /// <summary>
        /// 半角英数字チェック
        /// </summary>
        /// <remarks>
        /// 対象文字列が半角英数字のみかチェックします
        /// </remarks>
        /// <param name="value">チェック対象の文字列</param>
        /// <returns>判定結果
        /// <para>true：半角英数字のみ</para>
        /// <para>false：半角英数字以外を含む</para>
        /// </returns>
        public static bool IsHalfAlphabetNumber(string value)
        {
            return CheckUtil.IsMatchRegularExpressions(value, @"^[a-zA-Z0-9]+$");
        }
        #endregion

        #region IsDateFormat 日付型フォーマットのチェック
        /// <summary>
        /// 日付型フォーマットのチェック
        /// </summary>
        /// <remarks>
        /// 文字列が日付型フォーマット（YYYYMMDD or YYYY/MM/DD）であるかチェックします
        /// </remarks>
        /// <param name="value">チェック対象の文字列</param>
        /// <returns>判定結果
        /// <para>true：正常なフォーマット</para>
        /// <para>false：異常なフォーマット</para>
        /// </returns>
        public static bool IsDateFormat(string value)
        {
            DateTime workDateTime;

            return DateTime.TryParseExact(value, new string[] { "yyyyMMdd", "yyyy/MM/dd" }, DateTimeFormatInfo.InvariantInfo, DateTimeStyles.None, out workDateTime);
        }


        /// <summary>
        /// 日付型フォーマットのチェック
        /// </summary>
        /// <remarks>
        /// 文字列が日付型フォーマット（YYYYMMDD or YYYY/MM/DD）、かつ指定した範囲の日付であることをチェックします
        /// </remarks>
        /// <param name="minDate">最小年月日</param>
        /// <param name="maxDate">最大年月日</param>
        /// <param name="value">チェック対象の文字列</param>
        /// <returns>判定結果
        /// <para>true：正常なフォーマット</para>
        /// <para>false：異常なフォーマット</para>
        /// </returns>
        public static bool IsDateFormat(DateTime minDate, DateTime maxDate, string value)
        {
            DateTime workDateTime;

            if (DateTime.TryParseExact(value, new string[] { "yyyyMMdd", "yyyy/MM/dd" }, DateTimeFormatInfo.InvariantInfo, DateTimeStyles.None, out workDateTime))
            {
                if (minDate.Date <= workDateTime && workDateTime <= maxDate.Date)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        #endregion

        #region IsDateFormatYM 日付型フォーマットのチェック
        /// <summary>
        /// 日付型フォーマットのチェック
        /// </summary>
        /// <remarks>
        /// 文字列が日付型フォーマット（YYYYMM or YYYY/MM or YYYY/M）であるかチェックします
        /// </remarks>
        /// <param name="value">チェック対象の文字列</param>
        /// <returns>判定結果
        /// <para>true：正常なフォーマット</para>
        /// <para>false：異常なフォーマット</para>
        /// </returns>
        public static bool IsDateFormatYM(string value)
        {
            DateTime workDateTime;

            return DateTime.TryParseExact(value, new string[] { "yyyyMM", "yyyy/MM", "yyyy/M" }, DateTimeFormatInfo.InvariantInfo, DateTimeStyles.None, out workDateTime);
        }
        #endregion

        #region IsRealNumber 実数チェック
        /// <summary>
        /// 実数チェック(指数形式NG)
        /// </summary>
        /// <remarks>
        /// 対象文字列が指定された整数部桁数・小数部桁数であるかチェックします
        /// </remarks>
        /// <param name="value">対象文字列</param>
        /// <param name="integerLength">整数部桁数</param>
        /// <param name="decimalLength">小数部桁数</param>
        /// <returns>判定結果
        /// <para>true：OK</para>
        /// <para>false：NG</para>
        /// </returns>
        public static bool IsRealNumber(string value, int integerLength, int decimalLength)
        {
            //実数リテラルを除くの半角数字チェックを行う(指数表記が不許可)
            //リテラル： + - , . e
            string strOnlyNumber = value.Trim();
            strOnlyNumber = strOnlyNumber.Replace("+", "");
            strOnlyNumber = strOnlyNumber.Replace("-", "");
            strOnlyNumber = strOnlyNumber.Replace(",", "");
            strOnlyNumber = strOnlyNumber.Replace(".", "");
            if (IsHalfNumber(strOnlyNumber) == false)
            {
                return false;
            }

            decimal d;
            if (!decimal.TryParse(value.Trim(), NumberStyles.Any, NumberFormatInfo.InvariantInfo, out d))
            {
                return false;
            }

            //絶対値
            d = Math.Abs(d);

            string strObj = d.ToString("G", CultureInfo.InvariantCulture);

            if (strObj.IndexOf('.') < 0)
            {
                //小数点なし
                if (strObj.Length > integerLength)
                {
                    return false;
                }
            }
            else
            {
                //小数点あり
                string[] str = strObj.Split(new string[] { "." }, StringSplitOptions.RemoveEmptyEntries);
                if (str.Length == 2)
                {
                    if (str[0].Length > integerLength || str[1].Length > decimalLength)
                    {
                        return false;
                    }
                }
            }

            return true;
        }
        #endregion

        #region CheckFileExtension ファイルの拡張子チェック
        /// <summary>
        /// ファイルの拡張子チェック
        /// </summary>
        /// <param name="fileName">ファイル名</param>
        /// <param name="extension">拡張子</param>
        /// <returns>判定結果
        /// <para>true：指定の拡張子と同じ</para>
        /// <para>false：指定の拡張子と違う</para>
        /// </returns>
        public static bool CheckFileExtension(string fileName, string extension)
        {
            FileInfo file = new FileInfo(fileName);
            if (extension != null && !extension.StartsWith("."))
            {
                extension = "." + extension;
            }

            return file.Extension == extension;
        }
        #endregion

        #region IsNumeric 数値チェック
        /// <summary>
        /// 文字列が数値であるかどうかを返します。
        /// </summary>
        /// <param name="stTarget">検査対象となる文字列。</param>
        /// <returns>指定した文字列が数値であれば true。それ以外は false。</returns>
        public static bool IsNumeric(string stTarget)
        {
            double dNullable; 

            return double.TryParse(
                stTarget,
                System.Globalization.NumberStyles.Any,
                null,
                out dNullable
            );
        }
        /// <summary>
        /// オブジェクトが数値であるかどうかを返します。
        /// </summary>
        /// <param name="oTarget">検査対象となるオブジェクト。</param>
        /// <returns>指定したオブジェクトが数値であれば true。それ以外は false。</returns>
        public static bool IsNumeric(object oTarget)
        {
            if (oTarget == null)
            {
                return false;
            }
            else
            {
                return IsNumeric(oTarget.ToString());
            }
        }

        #endregion

        #region CheckForbiddenCharacters 入力禁止文字列の存在チェック
        /// <summary>
        /// 入力禁止文字列の存在チェック
        /// </summary>
        /// <param name="forbiddenCharacters">入力禁止文字列</param>
        /// <param name="checkString">チェック文字列</param>
        /// <param name="inputForbiddenCharacters">存在した入力禁止文字列</param>
        /// <returns>
        /// <para>true:入力禁止文字なし</para>
        /// <para>false:入力禁止文字あり</para>
        /// </returns>
        public static bool CheckForbiddenCharacters(string forbiddenCharacters, string checkString, out string inputForbiddenCharacters)
        {
            inputForbiddenCharacters = String.Empty;

            if (String.IsNullOrEmpty(forbiddenCharacters))
            {
                return true;
            }

            StringBuilder sb = new StringBuilder();

            // 入力禁止文字を一文字づつチェック
            for (int i = 0; i < forbiddenCharacters.Length; i++)
            {
                string forbiddenCharacter = forbiddenCharacters.Substring(i, 1);

                if (checkString.IndexOf(forbiddenCharacter) >= 0)
                {
                    sb.Append("「");
                    sb.Append(forbiddenCharacter);
                    sb.Append("」");
                }
            }

            inputForbiddenCharacters = sb.ToString();

            // 文字列内に入力禁止文字文字が含まれている場合はflase
            return inputForbiddenCharacters.Length == 0;
        }
        #endregion
    }
}
