@echo off
rem ######################################################################################
rem 全ユーザーメール通知設定更新ツール
rem 第1引数 スキーマ名
rem 第2引数 滞留通知        (滞留通知を行う)                                 0:送信しない 1:送信する
rem 第3引数 承認待ち        (自分に伝票が回ってきた)                         0:送信しない 1:送信する
rem 第4引数 自伝票差戻し    (自分が申請(承認)した伝票が差戻しされた)         0:送信しない 1:送信する
rem 第5引数 合議部署承認待ち(自分の所属する合議部署に伝票が回ってきた)       0:送信しない 1:送信する
rem 第6引数 自伝票承認      (自分が申請した伝票が最終承認された)             0:送信しない 1:送信する
rem 第7引数 自伝票否認      (自分が申請した伝票が否認された)                 0:送信しない 1:送信する
rem 第8引数 関連伝票承認    (自分が承認ルートに含まれる伝票が最終承認された) 0:送信しない 1:送信する
rem 第9引数 関連伝票否認    (自分が承認ルートに含まれる伝票が否認された)     0:送信しない 1:送信する
rem ######################################################################################


rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

IF "%1" EQU "" (
    echo 第1引数が設定されていません。
    GOTO ERROR
)
IF "%2" NEQ "0" IF "%2" NEQ "1" (
    echo 第2引数は 0:送信しない 1:送信する どちらかで指定してください。
    GOTO ERROR
)
IF "%3" NEQ "0" IF "%3" NEQ "1" (
    echo 第3引数は 0:送信しない 1:送信する どちらかで指定してください。
    GOTO ERROR
)
IF "%4" NEQ "0" IF "%4" NEQ "1" (
    echo 第4引数は 0:送信しない 1:送信する どちらかで指定してください。
    GOTO ERROR
)
IF "%5" NEQ "0" IF "%5" NEQ "1" (
    echo 第5引数は 0:送信しない 1:送信する どちらかで指定してください。
    GOTO ERROR
)
IF "%6" NEQ "0" IF "%6" NEQ "1" (
    echo 第6引数は 0:送信しない 1:送信する どちらかで指定してください。
    GOTO ERROR
)
IF "%7" NEQ "0" IF "%7" NEQ "1" (
    echo 第7引数は 0:送信しない 1:送信する どちらかで指定してください。
    GOTO ERROR
)
IF "%8" NEQ "0" IF "%8" NEQ "1" (
    echo 第8引数は 0:送信しない 1:送信する どちらかで指定してください。
    GOTO ERROR
)
IF "%9" NEQ "0" IF "%9" NEQ "1" (
    echo 第9引数は 0:送信しない 1:送信する どちらかで指定してください。
    GOTO ERROR
)


SETLOCAL enabledelayedexpansion

SET SCHEMA=%1
SET TTT=%2
SET RYS=%3
SET RYK=%4
SET RYG=%5
SET RJO=%6
SET RJN=%7
SET RKO=%8
SET RKN=%9

echo.
echo 対象スキーマ       ：%SCHEMA%
echo 滞留通知           ：%TTT%　(0:送信しない 1:送信する)
echo 承認待ち           ：%RYS%　(0:送信しない 1:送信する)
echo 自伝票差戻し       ：%RYK%　(0:送信しない 1:送信する)
echo 合議部署承認待ち   ：%RYG%　(0:送信しない 1:送信する)
echo 自伝票承認         ：%RJO%　(0:送信しない 1:送信する)
echo 自伝票否認         ：%RJN%　(0:送信しない 1:送信する)
echo 関連伝票承認       ：%RKO%　(0:送信しない 1:送信する)
echo 関連伝票否認       ：%RKN%　(0:送信しない 1:送信する)
echo.

echo 上記内容で(%SCHEMA%)スキーマ全ユーザーのメール通知設定を変更します。
SET /P ANSWER="既存のメール通知設定は削除されますがよろしいですか(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1

rem 全ユーザ取得
psql -U eteam -d eteam -t -c "SELECT user_id FROM %SCHEMA%.user_info WHERE user_id <> 'admin'" > user_list_upd.txt
rem 既存mail_tsuuchiデータ削除
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.mail_tsuuchi"

rem 各ユーザーmail_tsuuchiデータ設定
for /f %%L in (user_list_upd.txt) do (
    psql -U eteam -d eteam -c "INSERT INTO %SCHEMA%.mail_tsuuchi (user_id, tsuuchi_kbn, soushinumu) VALUES ('%%L', 'TTT', '%TTT%')"
    psql -U eteam -d eteam -c "INSERT INTO %SCHEMA%.mail_tsuuchi (user_id, tsuuchi_kbn, soushinumu) VALUES ('%%L', 'RYS', '%RYS%')"
    psql -U eteam -d eteam -c "INSERT INTO %SCHEMA%.mail_tsuuchi (user_id, tsuuchi_kbn, soushinumu) VALUES ('%%L', 'RYK', '%RYK%')"
    psql -U eteam -d eteam -c "INSERT INTO %SCHEMA%.mail_tsuuchi (user_id, tsuuchi_kbn, soushinumu) VALUES ('%%L', 'RYG', '%RYG%')"
    psql -U eteam -d eteam -c "INSERT INTO %SCHEMA%.mail_tsuuchi (user_id, tsuuchi_kbn, soushinumu) VALUES ('%%L', 'RJO', '%RJO%')"
    psql -U eteam -d eteam -c "INSERT INTO %SCHEMA%.mail_tsuuchi (user_id, tsuuchi_kbn, soushinumu) VALUES ('%%L', 'RJN', '%RJN%')"
    psql -U eteam -d eteam -c "INSERT INTO %SCHEMA%.mail_tsuuchi (user_id, tsuuchi_kbn, soushinumu) VALUES ('%%L', 'RKO', '%RKO%')"
    psql -U eteam -d eteam -c "INSERT INTO %SCHEMA%.mail_tsuuchi (user_id, tsuuchi_kbn, soushinumu) VALUES ('%%L', 'RKN', '%RKN%')"
)
echo メール通知設定更新完了しました。
pause

rem ユーザー名リストファイル削除
del user_list_upd.txt

ENDLOCAL
cd "%~dp0"
exit /b 0

:ERROR
echo usage:
echo 第1引数 スキーマ名
echo 第2引数 滞留通知           0:送信しない 1:送信する
echo 第3引数 承認待ち           0:送信しない 1:送信する
echo 第4引数 自伝票差戻し       0:送信しない 1:送信する
echo 第5引数 合議部署承認待ち   0:送信しない 1:送信する
echo 第6引数 自伝票承認         0:送信しない 1:送信する
echo 第7引数 自伝票否認         0:送信しない 1:送信する
echo 第8引数 関連伝票承認       0:送信しない 1:送信する
echo 第9引数 関連伝票否認       0:送信しない 1:送信する
pause
exit /b 1

