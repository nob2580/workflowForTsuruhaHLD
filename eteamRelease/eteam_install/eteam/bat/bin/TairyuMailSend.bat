@echo off
cd /d %~dp0
set classpath=C:\eteam\bat\bin\eteam.jar;c:\eteam\bat\lib\*


rem �X�L�[�}�P�ʂɏ���
if "%1" NEQ "" goto UNIT
for /f %%L in (tenant_list.txt) do (call TairyuMailSend.bat %%L >> c:\eteam\bat\logs\%~n0_%DATE:~-10,4%%DATE:~-5,2%%DATE:~-2%_%%L.txt 2>> c:\eteam\bat\logs\%~n0_%DATE:~-10,4%%DATE:~-5,2%%DATE:~-2%_%%LError.txt)
exit /b 0


:UNIT
rem �ؗ����[���z�M�o�b�`
echo �ؗ����[���z�M�o�b�` �J�n �X�L�[�}[%1]
set isError=false


rem �ؗ����[���i�v�����ʒm���j��z�M����
java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.xerces.internal.jaxp=ALL-UNNAMED eteam.gyoumu.workflow.TairyuMailSendBat %1
if %ERRORLEVEL%==0 call NormalEndBatch.bat TairyuMailSend
if %ERRORLEVEL%==1 call ErrorBatch.bat TairyuMailSend


rem Sub�ŃG���[���o�Ă���΃G���[�A�����łȂ���ΐ���
if %isError%==true  exit /b 1
exit /b 0


