package eteam.common;

import java.io.File;

import org.apache.struts2.ServletActionContext;

/**
 * JSPの補助機能
 */
public class JspUtil {
	/** jsp_extの実パス */
	static String jspExtPath = null;
	
	//とりあえず初回にjsp_extフォルダがいるか確認。パッケージだといない。カスタマイズだといる。
	static {
		jspExtPath = ServletActionContext.getServletContext().getRealPath("jsp_ext");
		if (! new File(jspExtPath).exists())
		{
			jspExtPath = null;
		}
	}

	/**
	 * /jsp/eteam/hoge/Hoge.jspがある前提で、
	 * makeJspPath("eteam/hoge/Hoge.jsp")を呼ぶと、
	 * /jsp_ext/eteam/hoge/Hoge.jspがあれば、"/jsp_ext/eteam/hoge/Hoge.jsp"を返すし、
	 * なければ、"/jsp/eteam/hoge/Hoge.jsp"を返す。
	 * @param path "eteam/hoge/Hoge.jsp"みたいなの
	 * @return "/jsp/eteam/hoge/Hoge.jsp"または"/jsp_ext/eteam/hoge/Hoge.jsp"みたいなの
	 */
	public static String makeJspPath(String path) {
		if(jspExtPath != null) {
			if(new File(jspExtPath + "/" + path).exists()) {
				return "/jsp_ext/" + path;
			}
		}
		return "/jsp/" + path;
	}
}
