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
   
   '�����[���T�[�o�[��o�^���Ă��������B
   .Item(strConfigurationField & "smtpserver") = "localhost"
   
   .Update
end With

objMail.Send

Set objMail = Nothing
