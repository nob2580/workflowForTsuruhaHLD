@echo off
set classpath=C:\eteam\bat\bin\eteam.jar;c:\eteam\bat\lib\*


rem �X�L�[�}���w�肳��Ă��Ȃ���΃G���[�Ƃ���
if "%1" NEQ "" goto UNIT
echo �X�L�[�}�����w�肳��Ă��܂���B
exit /b 1

:UNIT
rem �`�[�ꗗ�e�[�u���f�[�^�o�^�o�b�`
echo �`�[�ꗗ�f�[�^�쐬 �J�n �X�L�[�}[%1]

rem �`�[�ꗗ�e�[�u���f�[�^�o�^�o�b�`�����s
java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.xerces.internal.jaxp=ALL-UNNAMED eteam.gyoumu.workflow.DenpyouIchiranTableCreateBat %1

echo �`�[�ꗗ�f�[�^�쐬 �I�� �X�L�[�}[%1]

if %ERRORLEVEL%==0 exit /b 0
if %ERRORLEVEL%==1 exit /b 1
