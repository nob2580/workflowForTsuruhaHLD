package eteam.common;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.select.SystemKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * メール送信共通
 */
@Getter @Setter @ToString
public class EteamSendMailLogic extends EteamAbstractLogic {

	/** エンコーディング */
	protected static final String ENCODING = "UTF-8";

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(EteamSendMailLogic.class);
	
	/**
	 * MAIL送信実行
	 * @param subject 件名
	 * @param text 内容
	 * @param to 送信先アドレスリスト
	 * @param cc カーボン・コピーリスト
	 * @param bcc ブラインド・カーボン・コピーリスト
	 * @return 実行結果
	 * @throws RuntimeException 処理例外
	 */
	public boolean execute(String subject, String text, List<String> to,
			List<String> cc, List<String> bcc) throws RuntimeException {

		// 戻り値
		boolean ret = false;
		
		// メール設定情報の取得
		SystemKanriCategoryLogic skcl = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		
		// メール設定情報マップ
		GMap map = skcl.findMailSetteiAll();
		
		// 取得できなかった場合、処理失敗とする
		if (map == null) {
			return ret;
		}
		String host = map.getString("smtp_server_name");
		String portNo = map.getString("port_no");
		String from = map.getString("mail_address");
		String mailId = map.getString("mail_id");
		String mailPass = map.getString("mail_password");
		String ninshouHouhou = map.getString("ninshou_houhou");
		String angoukaHouhou = map.getString("angouka_houhou");
		
		// SMTPサーバーの設定
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", portNo);
		if (! "NO".equals(ninshouHouhou)) {
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.EnableSSL.enable", "true");
			props.put("mail.smtp.sasl.enable", "true");
			props.put("mail.smtp.sasl.mechanisms", "LOGIN,PLAIN,DIGEST-MD5,NTLM");
			props.put("mail.smtp.starttls.required", "true");
			props.put("mail.smtp.ssl.protocols", "TLSv1.2");
			if("SSL".equals(angoukaHouhou)) {
				// SMTP over SSL
				props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.setProperty("mail.smtp.socketFactory.fallback", "true");
			}else {
				// 暗号化なし または STARTTLS
				props.put("mail.smtp.starttls.enable", "true");
			}
		}
		
		Session session = Session.getInstance(props, (! "NO".equals(ninshouHouhou) ? new myAuth(mailId, mailPass) : null));
		session.setDebug(true); // デバッグモードにする(認証のログを出力)

		try {
			// メッセージの生成
			MimeMessage msg = new MimeMessage(session);

			// "FROM"の設定
			msg.setFrom(new InternetAddress(from));

			// "TO"の設定
			if (to != null) { 
				msg.setRecipients(Message.RecipientType.TO, getAdress(to));
			}
			// "CC"の設定
			if (cc != null) {
				msg.setRecipients(Message.RecipientType.CC, getAdress(cc));
			}

			// "BCC"の設定
			if (bcc != null) {
				msg.setRecipients(Message.RecipientType.BCC, getAdress(bcc));
			}
			
			// "SUBJECT"の設定
			msg.setSubject(MimeUtility.encodeText(subject, ENCODING, "B"));

			// データタイプの設定
			msg.setContent(text, "text/plain; charset=" + ENCODING);
			// メールの送信
			Transport.send(msg);

			ret = true;
		} catch (IOException | MessagingException e) {
			log.error("メール送信失敗", e);
			//エラーログは出すが、システムエラー扱いにはせず処理を続ける（メールサーバー外部要因なので）
			//throw new RuntimeException(e);
		}
		return ret;
	}

	/**
	 * アドレスリストを変換する
	 * @param ls アドレスリスト
	 * @return インターネットアドレス配列
	 * @throws AddressException 変換エラー
	 */
	protected InternetAddress[] getAdress(List<String> ls) throws AddressException {

		InternetAddress[] ia = new InternetAddress[ls.size()];
		int i = 0;
		Iterator<String> itr = ls.iterator();
		while (itr.hasNext()) {
			String adress = itr.next();
			ia[i] = new InternetAddress(adress);
			i++;
		}
		return ia;
	}
	
	/**
	 * SMTP認証クラス
	 */
	protected class myAuth extends Authenticator {

		/** メールID */
		protected String mailId;
		/** メールパスワード */
		protected String mailPass;
		
		/**
		 * コンストラクタ
		 * @param _mailId メールID
		 * @param _mailPass メールパスワード
		 */
		protected myAuth(String _mailId, String _mailPass) {
			mailId = _mailId;
			mailPass = _mailPass;
		}
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(mailId, mailPass);
		}
	}
}
