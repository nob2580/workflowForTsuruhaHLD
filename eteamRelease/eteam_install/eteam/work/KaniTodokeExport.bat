@echo off
cd /d %~dp0
set classpath=C:\eteam\bat\bin\eteam.jar;c:\eteam\bat\lib\*


rem ����
echo "���[�U�[��`�͏���`�̃G�N�X�|�[�g���s���܂��B"
set /P SCHEMA_NAME="�����Ώۂ̃X�L�[�}������͂��Ă�������: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1
set /P TARGET="�Ώۂ̓`�[�敪��4���Ŏw�肵�Ă��������B�S�`�[�̏ꍇ�� ALL �Ǝw�肵�Ă�������: "
if /i {%TARGET%} equ {} exit /b 1
SET /P ANSWER="�X�L�[�}��(%SCHEMA_NAME%)�A�Ώۓ`�[(%TARGET%)�ŏ������܂��B��낵���ł����H(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1


rem �͏o��`�G�N�X�|�[�g
echo �͏o��`�G�N�X�|�[�g�o�b�` �J�n �X�L�[�}[%SCHEMA_NAME%] �Ώۓ`�[[%TARGET%]
set isError=false
java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.xerces.internal.jaxp=ALL-UNNAMED eteam.gyoumu.kanitodoke.KaniTodokeExportBat %SCHEMA_NAME% %TARGET%
if %ERRORLEVEL%==0 call ..\bat\bin\NormalEndBatch.bat KaniTodokeExportBat
if %ERRORLEVEL%==1 call ..\bat\bin\ErrorBatch.bat KaniTodokeExportBat

pause
if %isError%==true  exit /b 1
exit /b 0

