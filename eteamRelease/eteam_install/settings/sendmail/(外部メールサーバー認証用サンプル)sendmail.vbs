Set objMail = CreateObject("CDO.Message")

'�����M�����[���A�h���X��o�^���Ă��������B
objMail.From = "���M�����[���A�h���X"

'�����M�惁�[���A�h���X��o�^���Ă��������B��������̏ꍇ�̓J���}��؂�Ŏw�肵�Ă��������B
objMail.To = "���M�惁�[���A�h���X"

'�����[���^�C�g����o�^���Ă��������B
objMail.Subject = "WF�o�b�`�G���[����"

'�����[���{����o�^���Ă��������B
objMail.TextBody = "WF�o�b�`�ŃG���[���������܂����B�ڍׂ�WF�T�[�o�[�Ŋm�F���Ă��������B"

strConfigurationField = "http://schemas.microsoft.com/cdo/configuration/"
With objMail.Configuration.Fields
   .Item(strConfigurationField & "sendusing") = 2
   .Item(strConfigurationField & "smtpconnectiontimeout") = 60
   .Item(strConfigurationField & "smtpauthenticate") = 1
   
   '�����[���T�[�o�[�̃z�X�g��o�^���Ă��������B
   .Item(strConfigurationField & "smtpserver") = "�z�X�g���܂���IP�A�h���X"
   
   '�����[���T�[�o�[�̃|�[�g�ԍ���o�^���Ă��������B
   .Item(strConfigurationField & "smtpserverport") = 465
   
   '��SSL�ʐM�Ƃ��邩�ǂ���(TRUE/FALSE)��o�^���Ă��������B
   .Item(strConfigurationField & "smtpusessl") = TRUE
   
   
   '��ID��o�^���Ă��������B
   .Item(strConfigurationField & "sendusername") = "ID"
   
   '���p�X���[�h��o�^���Ă��������B
   .Item(strConfigurationField & "sendpassword") = "�p�X���[�h"
   
   .Update
end With

objMail.Send

Set objMail = Nothing
