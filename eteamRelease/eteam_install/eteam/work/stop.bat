cd /d %~dp0


set SERVICE=Tomcat9
set LOG=c:\eteam\work\logs\stop.log


rem ApacheΜβ~
c:\Apache24\bin\httpd -k stop


rem TomcatΜβ~
net stop %SERVICE%


rem # ΞΫT[rXβ~G[»θ
if %errorlevel%==0 goto STOP_OK


rem # G[
net helpmsg %errorlevel% >> %LOG%


rem # ³ν
:STOP_OK


pause
