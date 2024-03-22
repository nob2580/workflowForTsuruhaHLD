Set objMail = CreateObject("CDO.Message")

'★送信元メールアドレスを登録してください。
objMail.From = "送信元メールアドレス"

'★送信先メールアドレスを登録してください。複数宛先の場合はカンマ区切りで指定してください。
objMail.To = "送信先メールアドレス"

'★メールタイトルを登録してください。
objMail.Subject = "WFバッチエラー発生"

'★メール本文を登録してください。
objMail.TextBody = "WFバッチでエラーが発生しました。詳細はWFサーバーで確認してください。"

strConfigurationField = "http://schemas.microsoft.com/cdo/configuration/"
With objMail.Configuration.Fields
   .Item(strConfigurationField & "sendusing") = 2
   .Item(strConfigurationField & "smtpconnectiontimeout") = 60
   
   '★メールサーバーを登録してください。
   .Item(strConfigurationField & "smtpserver") = "localhost"
   
   .Update
end With

objMail.Send

Set objMail = Nothing
