@echo off
cd /d %~dp0
set classpath=C:\eteam\bat\bin\eteam.jar;c:\eteam\bat\lib\*


rem ����
echo "�o��׃f�[�^�X�V���s���܂��B"
set /P SCHEMA_NAME="�����Ώۂ̃X�L�[�}������͂��Ă�������: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1
set /P TARGET_MONTH="�擾����J�n����YYYYMM�`���œ��͂��Ă�������: "
if /i {%TARGET_MONTH%} equ {} exit /b 1
SET /P ANSWER="�X�L�[�}��(%SCHEMA_NAME%)�A�J�n��(%TARGET_MONTH%)�ȍ~���������܂��B��낵���ł����H(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1


rem �o��׃f�[�^�X�V(�����C���|�[�g)�o�b�`
echo �o��׃f�[�^�X�V(�����C���|�[�g)�o�b�` �J�n �X�L�[�}[%SCHEMA_NAME%] �J�n��[%TARGET_MONTH%]
set isError=false


rem �o��׃f�[�^�X�V
java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.xerces.internal.jaxp=ALL-UNNAMED eteam.gyoumu.masterkanri.MasterTorikomiTermBat %SCHEMA_NAME% %TARGET_MONTH%
if %ERRORLEVEL%==0 call ..\bat\bin\NormalEndBatch.bat MasterTorikomiTermBat
if %ERRORLEVEL%==1 call ..\bat\bin\ErrorBatch.bat MasterTorikomiTermBat


pause
if %isError%==true  exit /b 1
exit /b 0

