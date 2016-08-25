package info.longnetpro.common.util.generic;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class StringUtils {
	public static String toUTF8String(String string) {
		return convertString(string, "UTF-8");
	}

	public static String convertString(String string, String fromCharSet, String toCharset) {
		String text = string;
		try {
			text = (fromCharSet == null) ? new String(string.getBytes(), toCharset)
					: new String(string.getBytes(fromCharSet), toCharset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return text;
	}

	public static String convertString(String string, String toCharset) {
		return convertString(string, null, toCharset);
	}

	public static String toHexString(int ch, boolean upperCase) {
		String hex = String.format("%04x", ch);
		if (upperCase) {
			hex = hex.toUpperCase();
		}
		return hex;
	}

	public static String toHexString(char ch) {
		return toHexString(ch, false);
	}

	public static String toSqlList(List<?> list, boolean trimmed) {
		String sql = "";
		if (list == null)
			return sql;
		for (Object obj : list) {
			String s = obj == null ? "" : obj.toString();
			if (trimmed)
				s = s.trim();
			if (obj instanceof String) {
				s = "'" + s + "'";
			} else if (obj instanceof Number || obj instanceof java.lang.Number) {
				;
			} else {
				s = "";
			}
			if (!s.isEmpty())
				sql += s + ",";
		}
		if (!sql.isEmpty()) {
			sql = sql.substring(0, sql.length() - 1);
			sql = "(" + sql + ")";
		}
		return sql;
	}

	public static String repeat(String text, int repeat) {
		if (text == null || text.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < repeat; i++) {
			sb.append(text);
		}
		return sb.toString();
	}

	public static String repeatSpace(int repeat) {
		if (repeat <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < repeat; i++) {
			sb.append(' ');
		}
		return sb.toString();
	}

	public static String leftPad(String text, int maxLength) {
		if (text != null && maxLength <= text.length()) {
			return text;
		}
		if (text == null || text.isEmpty()) {
			return repeatSpace(maxLength);
		} else {
			int len = maxLength - text.length();
			return repeatSpace(len) + text;
		}
	}

	public static String rightPad(String text, int maxLength) {
		if (text != null && maxLength <= text.length()) {
			return text;
		}
		if (text == null || text.isEmpty()) {
			return repeatSpace(maxLength);
		} else {
			int len = maxLength - text.length();
			return text + repeatSpace(len);
		}
	}

	public static String join(String[] texts, String delimiter) {
		if (texts == null || texts.length == 0) {
			return "";
		}
		if (texts.length == 1) {
			return texts[0];
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < texts.length - 1; i++) {
			sb.append(texts[0]);
			sb.append(delimiter);
		}
		sb.append(texts[texts.length - 1]);
		return sb.toString();
	}
}
