cd /d %~dp0


set SERVICE=Tomcat9
set LOG=c:\eteam\work\logs\stop.log


rem Apacheの停止
c:\Apache24\bin\httpd -k stop


rem Tomcatの停止
net stop %SERVICE%


rem # 対象サービス停止エラー判定
if %errorlevel%==0 goto STOP_OK


rem # エラー時
net helpmsg %errorlevel% >> %LOG%


rem # 正常時
:STOP_OK


pause
