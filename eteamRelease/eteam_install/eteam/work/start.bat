cd /d %~dp0


set SERVICE=Tomcat9
set LOG=c:\eteam\work\logs\start.log


rem Tomcat�̋N��
net start %SERVICE%


rem Apache�̋N��
c:\Apache24\bin\httpd -k start


rem # �ΏۃT�[�r�X��~�G���[����
if %errorlevel%==0 goto STOP_OK


rem # �G���[��
net helpmsg %errorlevel% >> %LOG%


rem # ���펞
:STOP_OK


pause