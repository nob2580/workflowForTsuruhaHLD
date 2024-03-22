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
   .Item(strConfigurationField & "smtpauthenticate") = 1
   
   '★メールサーバーのホストを登録してください。
   .Item(strConfigurationField & "smtpserver") = "ホスト名またはIPアドレス"
   
   '★メールサーバーのポート番号を登録してください。
   .Item(strConfigurationField & "smtpserverport") = 465
   
   '★SSL通信とするかどうか(TRUE/FALSE)を登録してください。
   .Item(strConfigurationField & "smtpusessl") = TRUE
   
   
   '★IDを登録してください。
   .Item(strConfigurationField & "sendusername") = "ID"
   
   '★パスワードを登録してください。
   .Item(strConfigurationField & "sendpassword") = "パスワード"
   
   .Update
end With

objMail.Send

Set objMail = Nothing
