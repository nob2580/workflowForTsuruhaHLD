#############################################################################################################################
# STARTTLS�ڑ���p�̊O���T�[�o�[��p�̔F�ؗp�T���v���ł��B
#############################################################################################################################
# �����
#  PowerShell v2.0�ȍ~
#  .Net Framework 2.0�ȍ~
# 
# ���蓮�Ŏ��s����ꍇ�́A��Ɉȉ��̃R�}���h�����s���Ă��������B
#       Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope Process
##############################################################################################################################

# �����M�����[���A�h���X��o�^���Ă��������B
$From="���M�����[���A�h���X"

# �����M�惁�[���A�h���X��o�^���Ă��������B
$To="���M�惁�[���A�h���X"

# �����[���^�C�g����o�^���Ă��������B
$Subject="WF�o�b�`�G���[����"

# �����[���{����o�^���Ă��������B
$body="WF�o�b�`�ŃG���[���������܂����B�ڍׂ�WF�T�[�o�[�Ŋm�F���Ă��������B"

# �����[���T�[�o�[�̃z�X�g��o�^���Ă��������B
$SMTPServer="�z�X�g���܂���IP�A�h���X"

# �����[���T�[�o�[�̃|�[�g�ԍ���o�^���Ă��������B
$Port="587"


$SMTPClient=New-Object Net.Mail.SmtpClient($SMTPServer,$Port)
$SMTPClient.EnableSsl=$true

# ��ID��o�^���Ă��������B
$User="ID"

# ���p�X���[�h��o�^���Ă��������B
$Password="�p�X���[�h"

$SMTPClient.Credentials=New-Object Net.NetworkCredential($User,$Password)
$SMTPClient.TimeOut=60
$MailMassage=New-Object Net.Mail.MailMessage($From,$To,$Subject,$body)
$SMTPClient.Send($MailMassage)