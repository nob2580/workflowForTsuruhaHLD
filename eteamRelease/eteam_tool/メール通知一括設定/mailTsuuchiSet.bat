@echo off
rem ######################################################################################
rem �S���[�U�[���[���ʒm�ݒ�X�V�c�[��
rem ��1���� �X�L�[�}��
rem ��2���� �ؗ��ʒm        (�ؗ��ʒm���s��)                                 0:���M���Ȃ� 1:���M����
rem ��3���� ���F�҂�        (�����ɓ`�[������Ă���)                         0:���M���Ȃ� 1:���M����
rem ��4���� ���`�[���߂�    (�������\��(���F)�����`�[�����߂����ꂽ)         0:���M���Ȃ� 1:���M����
rem ��5���� ���c�������F�҂�(�����̏������鍇�c�����ɓ`�[������Ă���)       0:���M���Ȃ� 1:���M����
rem ��6���� ���`�[���F      (�������\�������`�[���ŏI���F���ꂽ)             0:���M���Ȃ� 1:���M����
rem ��7���� ���`�[�۔F      (�������\�������`�[���۔F���ꂽ)                 0:���M���Ȃ� 1:���M����
rem ��8���� �֘A�`�[���F    (���������F���[�g�Ɋ܂܂��`�[���ŏI���F���ꂽ) 0:���M���Ȃ� 1:���M����
rem ��9���� �֘A�`�[�۔F    (���������F���[�g�Ɋ܂܂��`�[���۔F���ꂽ)     0:���M���Ȃ� 1:���M����
rem ######################################################################################


rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

IF "%1" EQU "" (
    echo ��1�������ݒ肳��Ă��܂���B
    GOTO ERROR
)
IF "%2" NEQ "0" IF "%2" NEQ "1" (
    echo ��2������ 0:���M���Ȃ� 1:���M���� �ǂ��炩�Ŏw�肵�Ă��������B
    GOTO ERROR
)
IF "%3" NEQ "0" IF "%3" NEQ "1" (
    echo ��3������ 0:���M���Ȃ� 1:���M���� �ǂ��炩�Ŏw�肵�Ă��������B
    GOTO ERROR
)
IF "%4" NEQ "0" IF "%4" NEQ "1" (
    echo ��4������ 0:���M���Ȃ� 1:���M���� �ǂ��炩�Ŏw�肵�Ă��������B
    GOTO ERROR
)
IF "%5" NEQ "0" IF "%5" NEQ "1" (
    echo ��5������ 0:���M���Ȃ� 1:���M���� �ǂ��炩�Ŏw�肵�Ă��������B
    GOTO ERROR
)
IF "%6" NEQ "0" IF "%6" NEQ "1" (
    echo ��6������ 0:���M���Ȃ� 1:���M���� �ǂ��炩�Ŏw�肵�Ă��������B
    GOTO ERROR
)
IF "%7" NEQ "0" IF "%7" NEQ "1" (
    echo ��7������ 0:���M���Ȃ� 1:���M���� �ǂ��炩�Ŏw�肵�Ă��������B
    GOTO ERROR
)
IF "%8" NEQ "0" IF "%8" NEQ "1" (
    echo ��8������ 0:���M���Ȃ� 1:���M���� �ǂ��炩�Ŏw�肵�Ă��������B
    GOTO ERROR
)
IF "%9" NEQ "0" IF "%9" NEQ "1" (
    echo ��9������ 0:���M���Ȃ� 1:���M���� �ǂ��炩�Ŏw�肵�Ă��������B
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
echo �ΏۃX�L�[�}       �F%SCHEMA%
echo �ؗ��ʒm           �F%TTT%�@(0:���M���Ȃ� 1:���M����)
echo ���F�҂�           �F%RYS%�@(0:���M���Ȃ� 1:���M����)
echo ���`�[���߂�       �F%RYK%�@(0:���M���Ȃ� 1:���M����)
echo ���c�������F�҂�   �F%RYG%�@(0:���M���Ȃ� 1:���M����)
echo ���`�[���F         �F%RJO%�@(0:���M���Ȃ� 1:���M����)
echo ���`�[�۔F         �F%RJN%�@(0:���M���Ȃ� 1:���M����)
echo �֘A�`�[���F       �F%RKO%�@(0:���M���Ȃ� 1:���M����)
echo �֘A�`�[�۔F       �F%RKN%�@(0:���M���Ȃ� 1:���M����)
echo.

echo ��L���e��(%SCHEMA%)�X�L�[�}�S���[�U�[�̃��[���ʒm�ݒ��ύX���܂��B
SET /P ANSWER="�����̃��[���ʒm�ݒ�͍폜����܂�����낵���ł���(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1

rem �S���[�U�擾
psql -U eteam -d eteam -t -c "SELECT user_id FROM %SCHEMA%.user_info WHERE user_id <> 'admin'" > user_list_upd.txt
rem ����mail_tsuuchi�f�[�^�폜
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.mail_tsuuchi"

rem �e���[�U�[mail_tsuuchi�f�[�^�ݒ�
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
echo ���[���ʒm�ݒ�X�V�������܂����B
pause

rem ���[�U�[�����X�g�t�@�C���폜
del user_list_upd.txt

ENDLOCAL
cd "%~dp0"
exit /b 0

:ERROR
echo usage:
echo ��1���� �X�L�[�}��
echo ��2���� �ؗ��ʒm           0:���M���Ȃ� 1:���M����
echo ��3���� ���F�҂�           0:���M���Ȃ� 1:���M����
echo ��4���� ���`�[���߂�       0:���M���Ȃ� 1:���M����
echo ��5���� ���c�������F�҂�   0:���M���Ȃ� 1:���M����
echo ��6���� ���`�[���F         0:���M���Ȃ� 1:���M����
echo ��7���� ���`�[�۔F         0:���M���Ȃ� 1:���M����
echo ��8���� �֘A�`�[���F       0:���M���Ȃ� 1:���M����
echo ��9���� �֘A�`�[�۔F       0:���M���Ȃ� 1:���M����
pause
exit /b 1

