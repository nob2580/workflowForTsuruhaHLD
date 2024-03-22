@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd /d %~dp0
set DUMP_PATH=c:\eteam\tmp\eteamDB.dump
set CHK_FILE=c:\eteam\tmp\SesChkTmp.txt

rem ���{�m�F
SET /P ANSWER="�_���v�t�@�C��(%DUMP_PATH%)����DB�𕜌����Ă�낵���ł����H(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1

rem �p�X���[�h����
SET /P PGPASSWORD="PostgreSQL�̊Ǘ��҃p�X���[�h����͂��Ă��������B"
if "%PGPASSWORD%" == "" (
    echo PostgreSQL�̃p�X���[�h�����͂���܂���ł����B�������~���܂��B
    GOTO BAT_END
)

rem eteam�f�[�^�x�[�X�ڑ��Z�b�V�����m�F
psql -U postgres -c "SELECT count(*) FROM pg_stat_activity WHERE datname = 'eteam' OR usename = 'eteam';" -A -t > %CHK_FILE%
if %ERRORLEVEL% == 0 (
    for /f %%A in (%CHK_FILE%) do (
        if not %%A == 0 (
            echo eteam�f�[�^�x�[�X�ւ̐ڑ��Z�b�V�������c���Ă��܂��B�������~���܂��B
            del /Q %CHK_FILE%
            GOTO BAT_END
        )
    )
    del /Q %CHK_FILE%
) else (
    echo �ڑ��Z�b�V�����`�F�b�N�����s�ł��܂���ł����B�������~���܂��B
    GOTO BAT_END
)

rem �_���v�t�@�C���m�F
IF EXIST %DUMP_PATH% (GOTO FILE_TRUE) ELSE GOTO FILE_FALSE


rem �_���v�t�@�C�����Ȃ�
:FILE_FALSE
ECHO "�_���v�t�@�C��(%DUMP_PATH%)�����݂��܂���B"
GOTO BAT_END


rem �_���v�t�@�C��������
:FILE_TRUE
psql -U postgres -f .\files\restore_db.sql
pg_restore -U postgres -d eteam -F c %DUMP_PATH%


:BAT_END
if exist %CHK_FILE% del /Q %CHK_FILE%
pause