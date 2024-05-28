using System;
using System.IO;
using System.Text;

namespace Importer.Util
{
    /// <summary>
    /// ファイル操作ユーティリティ
    /// </summary>
    public static class FileUtil
    {
        #region CreateFile 指定されたパスへファイルを作る
        /// <summary>
        /// テキストから指定されたパスへファイルを作る
        /// </summary>
        /// <param name="filePath">作成ファイルのパス</param>
        /// <param name="text">テキスト</param>
        public static void CreateFile(string filePath, string text)
        {
            FileUtil.CreateFile(filePath, text, Encoding.UTF8);
        }

        /// <summary>
        /// テキストから指定されたパスへファイルを作る
        /// </summary>
        /// <param name="filePath">作成ファイルのパス</param>
        /// <param name="text">テキスト</param>
        /// <param name="encoding">エンコーディング</param>
        public static void CreateFile(string filePath, string text, Encoding encoding)
        {
            string dirName = Path.GetDirectoryName(filePath);

            if (!String.IsNullOrEmpty(dirName))
            {
                // フォルダ存在チェック
                if (!Directory.Exists(dirName))
                {
                    // フォルダ作成
                    Directory.CreateDirectory(dirName);
                }
            }

            // ファイル属性から読み取り専用を削除(同名ファイルが既に存在している場合を想定)
            FileUtil.RemoveReadOnlyAttributes(filePath);

            // 作成先ファイルパスへファイルを作成
            File.AppendAllText(filePath, text, encoding);
        }

        /// <summary>
        /// バイト配列から指定されたパスへファイルを作る
        /// </summary>
        /// <param name="filePath">作成ファイルのパス</param>
        /// <param name="buffer">作成ファイルのバイト配列</param>
        public static void CreateFile(string filePath, byte[] buffer)
        {
            string dirName = Path.GetDirectoryName(filePath);

            if (!String.IsNullOrEmpty(dirName))
            {
                // フォルダ存在チェック
                if (!Directory.Exists(dirName))
                {
                    // フォルダ作成
                    Directory.CreateDirectory(dirName);
                }
            }

            // ファイル属性から読み取り専用を削除(同名ファイルが既に存在している場合を想定)
            FileUtil.RemoveReadOnlyAttributes(filePath);

            // 作成先ファイルパスへファイルを作成
            File.WriteAllBytes(filePath, buffer);
        }
        #endregion

        #region GetFileByteArray 指定されたファイルをバイト配列に変換する
        /// <summary>
        /// 指定されたファイルをバイト配列に変換し、元ファイルを削除します
        /// </summary>
        /// <param name="filePath">取得元のファイルパス</param>
        /// <returns>バイト配列</returns>
        public static byte[] GetFileByteArray(string filePath)
        {
            return GetFileByteArray(filePath, true);
        }

        /// <summary>
        /// 指定されたファイルをバイト配列に変換する
        /// </summary>
        /// <param name="filePath">取得元のファイルパス</param>
        /// <param name="deleteFlg">
        /// <para>true:元ファイルを削除する</para>
        /// <para>false:元ファイルを削除しない</para>
        /// </param>
        /// <returns>バイト配列</returns>
        public static byte[] GetFileByteArray(string filePath, bool deleteFlg)
        {
            // 読み取り専用でファイルを開く
            byte[] bytBuffer = File.ReadAllBytes(filePath);

            if (deleteFlg)
            {
                // ファイル削除
                FileUtil.Delete(filePath);
            }

            return bytBuffer;
        }
        #endregion

        #region RemoveReadOnlyAttributes 読み取り専用属性削除
        /// <summary>
        /// 読み取り専用属性削除
        /// </summary>
        /// <param name="filePath">対象ファイルのパス</param>
        public static void RemoveReadOnlyAttributes(string filePath)
        {
            // ファイルの存在チェック
            if (File.Exists(filePath))
            {
                // ファイル属性を取得
                FileAttributes fileAttributes = File.GetAttributes(filePath);

                // 読み取り専用かどうか確認
                if ((fileAttributes & FileAttributes.ReadOnly) == FileAttributes.ReadOnly)
                {
                    // ファイル属性から読み取り専用を削除
                    fileAttributes = fileAttributes & ~FileAttributes.ReadOnly;
                }

                // ファイル属性を設定
                File.SetAttributes(filePath, fileAttributes);
            }
        }
        #endregion

        #region Delete ファイル削除
        /// <summary>
        /// ファイル削除
        /// </summary>
        /// <param name="filePath">ファイルパス</param>
        public static void Delete(string filePath)
        {
            // ファイルの存在チェック
            // アクセス権限がない場合は存在しないとなる
            if (File.Exists(filePath))
            {
                // ファイル属性から読み取り専用を削除
                FileUtil.RemoveReadOnlyAttributes(filePath);

                // ファイル削除
                File.Delete(filePath);
            }
        }
        #endregion

        #region DeleteDirectory 指定したディレクトリを削除
        /// <summary>
        /// 指定したディレクトリを削除
        /// </summary>
        /// <remarks>
        /// 指定したディレクトリ内のファイルも全て削除
        /// </remarks>
        /// <param name="dirPath">削除するディレクトリのパス</param>
        public static void DeleteDirectory(string dirPath)
        {
            if (Directory.Exists(dirPath))
            {
                FileUtil.DeleteDirectory(new DirectoryInfo(dirPath));
            }
        }

        /// <summary>
        /// 指定したディレクトリを削除
        /// </summary>
        /// <remarks>
        /// 指定したディレクトリ内のファイルも全て削除
        /// </remarks>
        /// <param name="directoryInfo">削除するディレクトリの <see cref="DirectoryInfo"/></param>
        public static void DeleteDirectory(DirectoryInfo directoryInfo)
        {
            // すべてのファイルの読み取り専用属性を解除する
            foreach (FileInfo fileInfo in directoryInfo.GetFiles())
            {
                if ((fileInfo.Attributes & FileAttributes.ReadOnly) == FileAttributes.ReadOnly)
                {
                    fileInfo.Attributes = FileAttributes.Normal;
                }
            }

            // サブディレクトリ内を削除 (再帰)
            foreach (DirectoryInfo dirInfo in directoryInfo.GetDirectories())
            {
                FileUtil.DeleteDirectory(dirInfo);
            }

            // このディレクトリの読み取り専用属性を解除する
            if ((directoryInfo.Attributes & FileAttributes.ReadOnly) == FileAttributes.ReadOnly)
            {
                directoryInfo.Attributes = FileAttributes.Directory;
            }

            // このディレクトリを削除する
            directoryInfo.Delete(true);
        }
        #endregion

        #region Copy ファイルコピー
        /// <summary>
        /// ファイルコピー
        /// </summary>
        /// <remarks>
        /// <para>コピーするファイルが存在しない場合は、コピー処理は行いません。</para>
        /// <para>同じ名前のファイルが存在する場合は上書きされます。</para>
        /// </remarks>
        /// <param name="sourceFileName">コピーするファイル</param>
        /// <param name="destFileName">コピー先ファイルの名前</param>
        public static void Copy(string sourceFileName, string destFileName)
        {
            // ファイルの存在チェック
            // アクセス権限がない場合は存在しないとなる
            if (File.Exists(sourceFileName))
            {
                string dirName = Path.GetDirectoryName(destFileName);

                if (!String.IsNullOrEmpty(dirName))
                {
                    // フォルダ存在チェック
                    if (!Directory.Exists(dirName))
                    {
                        // フォルダ作成
                        Directory.CreateDirectory(dirName);
                    }
                }

                // ファイル属性から読み取り専用を削除(同名ファイルが既に存在している場合)
                FileUtil.RemoveReadOnlyAttributes(destFileName);

                // ファイルコピー
                File.Copy(sourceFileName, destFileName, true);

                // ファイル属性から読み取り専用を削除(コピーファイルが読み取り専用の場合)
                FileUtil.RemoveReadOnlyAttributes(destFileName);
            }
        }
        #endregion
    }
}
