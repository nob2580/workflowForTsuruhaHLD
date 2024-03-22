cd /d %~dp0


set SERVICE=Tomcat9
set LOG=c:\eteam\work\logs\start.log


rem Tomcatの起動
net start %SERVICE%


rem Apacheの起動
c:\Apache24\bin\httpd -k start


rem # 対象サービス停止エラー判定
if %errorlevel%==0 goto STOP_OK


rem # エラー時
net helpmsg %errorlevel% >> %LOG%


rem # 正常時
:STOP_OK


pause
