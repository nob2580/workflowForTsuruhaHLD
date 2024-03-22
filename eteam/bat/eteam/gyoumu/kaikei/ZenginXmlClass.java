package eteam.gyoumu.kaikei;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("javadoc")
@Setter @Getter
@XmlRootElement(name = "Document")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZenginXmlClass{
	
	
	@XmlAttribute(name="xmlns")
	protected static String xmlns = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03";
	
	//TODO まずjaxb向けタグなどを入れていく
	//TODO xmlelementwrapperとxmlelementの詳細な使い方を調べておく
	
	//TODO ドキュメントルートに各種文字列の設定必要 固定文字列出力させる方法ある？
		
	@XmlElement(name = "CstmrCdtTrfInitn")
	CstmrCdtTrfInitnClass CstmrCdtTrfInitn;

	@Setter @Getter
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class CstmrCdtTrfInitnClass{
		
		@XmlElement(name = "GrpHdr")
		GrpHdrClass GrpHdr; /* グループヘッダー情報 */
		@XmlElement(name = "PmtInf")
		List<PmtInfClass> PmtInf; /* 支払情報 */
		
		/* グループヘッダー情報 */
		@Setter @Getter
		@XmlAccessorType(XmlAccessType.FIELD)
		public static class GrpHdrClass{
			@XmlElement(name = "MsgId")
			String MsgId; /* グループメッセージID */
			@XmlElement(name = "CreDtTm")
			String CreDtTm; /* XMLファイル作成日時 */
			@XmlElement(name = "NbOfTxs")
			String NbOfTxs; /* 支払情報数 */
			@XmlElement(name = "InitgPty")
			List<String> InitgPty; /* 開始集団 */
			
		}
		
		/* 支払情報 */
		@Setter @Getter
		@XmlAccessorType(XmlAccessType.FIELD)
		public static class PmtInfClass{
			
			@XmlElement(name = "PmtInfId")
			String PmtInfId; /* 支払情報ID */
			@XmlElement(name = "PmtMtd")
			String PmtMtd; /* 支払方法 */
			@XmlElement(name = "NbOfTxs")
			String NbOfTxs; /* 合計件数 */
			@XmlElement(name = "CtrlSum")
			String CtrlSum; /* 合計金額 */
			@XmlElement(name = "PmtTpInf")
			PmtTpInfClass PmtTpInf; /* 支払種別情報 */
			@XmlElement(name = "ReqdExctnDt")
			String ReqdExctnDt; /* 取組日 */
			@XmlElement(name = "Dbtr")
			DbtrClass Dbtr; /* 振込依頼人情報 */
			@XmlElement(name = "DbtrAcct")
			DbtrAcctClass DbtrAcct; /* 振込依頼人口座情報 */
			@XmlElement(name = "DbtrAgt")
			DbtrAgtClass DbtrAgt; /* 仕向金融機関情報 */
			@XmlElement(name = "UltmtDbtr")
			UltmtDbtrClass UltmtDbtr; /* 振込依頼人情報 */
			@XmlElement(name = "CdtTrfTxInf")
			List<CdtTrfTxInfClass> CdtTrfTxInf; /* 取引明細 */
			
			/* 支払種別情報 */
			@Setter @Getter
			@XmlAccessorType(XmlAccessType.FIELD)
			public static class PmtTpInfClass{
				@XmlElement(name = "CtgyPurp")
				CtgyPurpClass CtgyPurp; /* 種別情報 */
				public static class CtgyPurpClass{
					@XmlElement(name = "Cd")
					String Cd; /* 種別コード */
				}
			}
			
			/* 振込依頼人情報 */
			@Setter @Getter
			@XmlAccessorType(XmlAccessType.FIELD)
			public static class DbtrClass{
				@XmlElement(name = "Id")
				IdClass Id; /* 振込依頼人識別情報 */
			}
			
			/* 振込依頼人口座情報 */
			@Setter @Getter
			@XmlAccessorType(XmlAccessType.FIELD)
			public static class DbtrAcctClass{
				@XmlElement(name = "Id")
				IdClass Id; /* 振込依頼人口座識別情報 */
				@XmlElement(name = "Tp")
				TpClass Tp; /* 振込依頼人預金種目情報 */
			}
			
			/* 仕向金融機関情報 */
			@Setter @Getter
			@XmlAccessorType(XmlAccessType.FIELD)
			public static class DbtrAgtClass{
				@XmlElement(name = "FinInstnId")
				FinInstnIdClass FinInstnId; /* 仕向金融機関識別情報 */
				@XmlElement(name = "BrnchId")
				BrnchIdClass BrnchId; /* 仕向支店情報 */
			}
			
			/* 振込依頼人情報 */
			@Setter @Getter
			@XmlAccessorType(XmlAccessType.FIELD)
			public static class UltmtDbtrClass{
				@XmlElement(name = "Nm")
				String Nm; /* 振込依頼人名 */
			}
			
			/* 取引明細 */
			@Setter @Getter
			@XmlAccessorType(XmlAccessType.FIELD)
			public static class CdtTrfTxInfClass{
				@XmlElement(name = "PmtId")
				PmtIdClass PmtId; /* 支払識別情報 */
				public static class PmtIdClass{
					@XmlElement(name = "EndToEndId")
					String EndToEndId; /* 取引明細識別番号 */
				}
				@XmlElement(name = "Amt")
				AmtClass Amt; /* 振込金額情報 */
				public static class AmtClass{
					//TODO ここだけ名前ルールが微妙なのでできれば作り変える
					@XmlElement(name = "InstdAmt")
					InstdAmtClass iAmtCls; /* 振込金額 */
					public void setIAmtCls(InstdAmtClass iAmtCls) {
						this.iAmtCls = iAmtCls;
					}
					public static class InstdAmtClass{
						String InstdAmt;
						String Ccy;
						public void setInstdAmt(String setStr) {
							this.InstdAmt = setStr;
						}
						@XmlValue
						public String getInstdAmt() {
							return this.InstdAmt;
						}
						@XmlAttribute(name="Ccy")
						public String getCcy() {
							return this.Ccy;
						}
						public void setCcy(String Ccy) {
							this.Ccy = Ccy;
						}
					}
				}
				@XmlElement(name = "CdtrAgt")
				CdtrAgtClass CdtrAgt; /* 被仕向金融機関情報 */
				public static class CdtrAgtClass{
					@XmlElement(name = "FinInstnId")
					FinInstnIdClass FinInstnId; /* 被仕向金融機関識別情報 */

					@XmlElement(name = "BrnchId")
					BrnchIdClass BrnchId; /* 被仕向支店情報 */
				}
				@XmlElement(name = "Cdtr")
				CdtrClass Cdtr; /* 受取人情報 */
				public static class CdtrClass{
					@XmlElement(name = "Nm")
					String Nm; /* 受取人名 */
					@XmlElement(name = "Id")
					IdClass Id; /* 受取人識別情報 */
					//TODO 受取先法人番号情報、顧客コード2に該当するタグは出力させない
				}
				
				@XmlElement(name = "CdtrAcct")
				CdtrAcctClass CdtrAcct; /* 受取人口座情報 */
				public static class CdtrAcctClass{
					@XmlElement(name = "Id")
					IdClass Id; /* 受取人口座識別情報 */
					@XmlElement(name = "Tp")
					TpClass Tp; /* 受取人預金種目情報 */
				}
				
				@XmlElement(name = "InstrForCdtrAgt")
				InstrForCdtrAgtClass InstrForCdtrAgt; /* 振込指定区分情報 */
				public static class InstrForCdtrAgtClass{
					@XmlElement(name = "InstrInf")
					String InstrInf; /* 振込指定区分 */
				}
				
				@XmlElement(name = "InstrForDbtrAgt")
				String InstrForDbtrAgt; /* 振込指定区分情報 */
				
				@XmlElement(name = "Purp")
				PurpClass Purp; /* 新規コード情報 */
				public static class PurpClass{
					@XmlElement(name = "Prtry")
					String Prtry; /* 新規コード */
				}
				
				@XmlElement(name = "RmtInf")
				RmtInfClass RmtInf; /* 商流情報 */
				public static class RmtInfClass{
					@XmlElement(name = "Ustrd")
					List<String> Ustrd; /* 金融EDI情報 */
				}
			}
			
		}
		
		
		public static class FinInstnIdClass{
			@XmlElement(name = "ClrSysMmbId")
			ClrSysMmbIdClass ClrSysMmbId; /* 被仕向決済システム識別情報 */
			@XmlElement(name = "Nm")
			String Nm; /* 被仕向銀行名 */
			@XmlElement(name = "Othr")
			OthrClass Othr; /* 手形交換所番号情報 */
		}
		
		public static class ClrSysMmbIdClass{
			@XmlElement(name = "ClrSysId")
			ClrSysIdClass ClrSysId; /* 仕向決済システム識別詳細情報 */
			@XmlElement(name = "MmbId")
			String MmbId; /* 被仕向銀行番号 */
		}
		
		public static class ClrSysIdClass{
			@XmlElement(name = "Cd")
			String Cd; /* 仕向決済システム識別コード */
		}


		public static class IdClass{
			@XmlElement(name = "OrgId")
			OrgIdClass OrgId; /* 振込依頼人組織識別情報 */
			@XmlElement(name = "Othr")
			OthrClass Othr; /* 振込依頼人口座識別詳細情報 */
		}
		
		public static class OrgIdClass{
			@XmlElement(name = "Othr")
			List<OthrClass> Othr; /* 振込依頼人組織識別情報・振込依頼人法人番号情報 */
		}

		
		public static class OthrClass{
			@XmlElement(name = "Id")
			String Id; /* 振込依頼人コード・振込依頼人法人番号 */
			@XmlElement(name = "SchmeNm")
			SchmeNmClass SchmeNm; /* 振込依頼人概要情報 */
		}
		
		public static class BrnchIdClass{
			@XmlElement(name = "Id")
			String Id; /* 仕向支店番号 */
			@XmlElement(name = "Nm")
			String Nm; /* 仕向支店名 */
		}
		
		
		public static class TpClass{
			@XmlElement(name = "Prtry")
			String Prtry; /* 振込依頼人預金種目 */
		}


		public static class SchmeNmClass{
			@XmlElement(name = "Cd")
			String Cd;
			@XmlElement(name = "Prtry")
			String Prtry; /* 顧客コード１概要名 */
		}
	}

}