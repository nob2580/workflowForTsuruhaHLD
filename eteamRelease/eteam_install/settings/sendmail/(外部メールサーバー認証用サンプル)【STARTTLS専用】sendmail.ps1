#############################################################################################################################
# STARTTLS接続専用の外部サーバー専用の認証用サンプルです。
#############################################################################################################################
# 動作環境
#  PowerShell v2.0以降
#  .Net Framework 2.0以降
# 
# ※手動で実行する場合は、先に以下のコマンドを実行してください。
#       Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope Process
##############################################################################################################################

# ★送信元メールアドレスを登録してください。
$From="送信元メールアドレス"

# ★送信先メールアドレスを登録してください。
$To="送信先メールアドレス"

# ★メールタイトルを登録してください。
$Subject="WFバッチエラー発生"

# ★メール本文を登録してください。
$body="WFバッチでエラーが発生しました。詳細はWFサーバーで確認してください。"

# ★メールサーバーのホストを登録してください。
$SMTPServer="ホスト名またはIPアドレス"

# ★メールサーバーのポート番号を登録してください。
$Port="587"


$SMTPClient=New-Object Net.Mail.SmtpClient($SMTPServer,$Port)
$SMTPClient.EnableSsl=$true

# ★IDを登録してください。
$User="ID"

# ★パスワードを登録してください。
$Password="パスワード"

$SMTPClient.Credentials=New-Object Net.NetworkCredential($User,$Password)
$SMTPClient.TimeOut=60
$MailMassage=New-Object Net.Mail.MailMessage($From,$To,$Subject,$body)
$SMTPClient.Send($MailMassage)