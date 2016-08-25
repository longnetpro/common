package info.longnetpro.common.util.generic;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateChineseConverter {
	private static final String[] NAMES = new String[] { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "年",
			"月", "日", "时", "分", "秒", "", /* "毫秒", */ "〇", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天" };
	private static final String INDEX = "0123456789TNYRSFMHOabcdefg"; // 一二三四五六七八九十〇年月日时分秒毫秒星期
	private static final String FORMAT_INDEX = "YyMdHmsSW"; // YyMdHmsSW DA tT

	private static String format(String format, Date date) {
		SimpleDateFormat sdf = null;
		if (format == null)
			sdf = new SimpleDateFormat();
		else
			sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	private static String codeNumberWithin20(int number) {
		if (number < 0 || number >= 60)
			return null;
		int num = number;
		if (num == 0) {
			return "0";
		}
		int u = num / 10;
		int l = num % 10;

		if (u == 0) {
			return l + ""; // 0 - 9
		}

		if (u == 1) {
			return l == 0 ? "T" : "T" + l; // 10, 11 - 19
		}

		if (l == 0) {
			return u + "T"; // 20, 30 ...
		}

		return u + "T" + (l == 0 ? "" : l);
	}

	private static String codeYear(Date date, boolean specialZero) {
		String result = format("yyyy'N'", date);
		if (specialZero) {
			result = result.replace('0', 'O');
		}
		return result;
	}

	private static String formatDateNumber(Date date, String format, String suffixCode) {
		String numberString = format(format, date);
		int number = Integer.parseInt(numberString);
		return codeNumberWithin20(number) + suffixCode;
	}

	private static String codeMonth(Date date) {
		return formatDateNumber(date, "M", "Y");
	}

	private static String codeDay(Date date) {
		return formatDateNumber(date, "d", "R");
	}

	private static String codeHour(Date date) {
		return formatDateNumber(date, "H", "S");
	}

	private static String codeMinute(Date date) {
		return formatDateNumber(date, "m", "F");
	}

	private static String codeSecond(Date date) {
		return formatDateNumber(date, "s", "M");
	}

	private static String codeMillisecond(Date date) {
		return format("SSS'H'", date);
	}

	private static String codeWeekDay(Date date) {
		String wd = format("u", date);
		int number = Integer.parseInt(wd);
		char ch = (char) ('a' + number - 1);
		return ch + "";
	}

	static String[] getCodesFromDate(Date date) {
		String[] codes = new String[9];
		codes[0] = codeYear(date, false);
		codes[1] = codeYear(date, true);
		codes[2] = codeMonth(date);
		codes[3] = codeDay(date);
		codes[4] = codeHour(date);
		codes[5] = codeMinute(date);
		codes[6] = codeSecond(date);
		codes[7] = codeMillisecond(date);
		codes[8] = codeWeekDay(date);
		return codes;
	}

	static String translateCode(String code) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < code.length(); i++) {
			char ch = code.charAt(i);
			int index = INDEX.indexOf(ch);
			if (index >= 0) {
				sb.append(NAMES[index]);
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	private static String parse(char ch, String[] dateCodes) {
		int index = FORMAT_INDEX.indexOf(ch);
		if (index >= 0 && index < dateCodes.length) {
			return dateCodes[index];
		} else {
			return ch + "";
		}
	}

	public static String formatDate(String format, Date date) {
		if (format == null) {
			return null;
		}

		int index = 0;
		String[] codes = getCodesFromDate(date);
		StringBuilder sb = new StringBuilder();
		while (index < format.length()) {
			char ch = format.charAt(index);
			switch (ch) {
			case 'D':
				sb.append(formatDate("yMd", date));
				break;
			case 'A':
				sb.append(formatDate("YMd", date));
				break;
			case 't':
				sb.append(formatDate("Hms", date));
				break;
			case 'T':
				sb.append(formatDate("HmsS", date));
				break;
			case '\'':
				boolean isQuote = true;
				index++;
				while (index < format.length() && format.charAt(index) != '\'') {
					isQuote = false;
					sb.append(format.charAt(index));
					index++;
				}
				if (isQuote) {
					sb.append('\'');
					isQuote = false;
				}
				break;
			default:
				sb.append(translateCode(parse(ch, codes)));
				break;
			}
			index++;
		}

		return sb.toString();
	}
}
