@echo off
cd /d %~dp0
set classpath=C:\eteam\bat\bin\eteam.jar;c:\eteam\bat\lib\*


rem �X�L�[�}�P�ʂɏ���
if "%1" NEQ "" goto UNIT
for /f %%L in (tenant_list.txt) do (call MasterTorikomiTerm.bat %%L >> c:\eteam\bat\logs\%~n0_%DATE:~-10,4%%DATE:~-5,2%%DATE:~-2%_%%L.txt 2>> c:\eteam\bat\logs\%~n0_%DATE:~-10,4%%DATE:~-5,2%%DATE:~-2%_%%LError.txt)
exit /b 0


:UNIT
rem �o��׃f�[�^�X�V�o�b�`
echo �o��׃f�[�^�X�V�o�b�` �J�n �X�L�[�}[%1]
set isError=false


rem OPEN21����}�X�^�[�f�[�^���捞��
java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.xerces.internal.jaxp=ALL-UNNAMED eteam.gyoumu.masterkanri.MasterTorikomiTermBat %1
if %ERRORLEVEL%==0 call NormalEndBatch.bat MasterTorikomiTermBat
if %ERRORLEVEL%==1 call ErrorBatch.bat MasterTorikomiTermBat


rem Sub�ŃG���[���o�Ă���΃G���[�A�����łȂ���ΐ���
if %isError%==true  exit /b 1
exit /b 0

