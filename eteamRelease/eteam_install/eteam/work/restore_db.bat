@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd /d %~dp0
set DUMP_PATH=c:\eteam\tmp\eteamDB.dump
set CHK_FILE=c:\eteam\tmp\SesChkTmp.txt

rem 実施確認
SET /P ANSWER="ダンプファイル(%DUMP_PATH%)からDBを復元してよろしいですか？(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1

rem パスワード入力
SET /P PGPASSWORD="PostgreSQLの管理者パスワードを入力してください。"
if "%PGPASSWORD%" == "" (
    echo PostgreSQLのパスワードが入力されませんでした。処理中止します。
    GOTO BAT_END
)

rem eteamデータベース接続セッション確認
psql -U postgres -c "SELECT count(*) FROM pg_stat_activity WHERE datname = 'eteam' OR usename = 'eteam';" -A -t > %CHK_FILE%
if %ERRORLEVEL% == 0 (
    for /f %%A in (%CHK_FILE%) do (
        if not %%A == 0 (
            echo eteamデータベースへの接続セッションが残っています。処理中止します。
            del /Q %CHK_FILE%
            GOTO BAT_END
        )
    )
    del /Q %CHK_FILE%
) else (
    echo 接続セッションチェックが実行できませんでした。処理中止します。
    GOTO BAT_END
)

rem ダンプファイル確認
IF EXIST %DUMP_PATH% (GOTO FILE_TRUE) ELSE GOTO FILE_FALSE


rem ダンプファイルがない
:FILE_FALSE
ECHO "ダンプファイル(%DUMP_PATH%)が存在しません。"
GOTO BAT_END


rem ダンプファイルがある
:FILE_TRUE
psql -U postgres -f .\files\restore_db.sql
pg_restore -U postgres -d eteam -F c %DUMP_PATH%


:BAT_END
if exist %CHK_FILE% del /Q %CHK_FILE%
pause