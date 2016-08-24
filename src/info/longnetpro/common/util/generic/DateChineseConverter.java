package info.longnetpro.common.util.generic;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateChineseConverter {
	private static final String[] NAMES = new String[] { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "年",
			"月", "日", "时", "分", "秒", "", /*"毫秒",*/ "〇", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天" };
	private static final String INDEX = "0123456789TNYRSFMHOWabcdefg"; // 一二三四五六七八九十〇年月日时分秒毫秒星期

	public DateChineseConverter() {
		// TODO Auto-generated constructor stub
	}

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

	public static String defaultCodeFromDate(Date date, boolean specialZero) {
		StringBuilder sb = new StringBuilder();
		sb.append(codeYear(date, specialZero));
		sb.append('-');
		sb.append(codeMonth(date));
		sb.append('-');
		sb.append(codeDay(date));
		sb.append('-');
		sb.append(codeHour(date));
		sb.append('-');
		sb.append(codeMinute(date));
		sb.append('-');
		sb.append(codeSecond(date));
		sb.append('-');
		sb.append(codeMillisecond(date));
		sb.append('-');
		sb.append(codeWeekDay(date));
		return sb.toString();
	}
	
	public static String defaultCodeFromDate(Date date) {
		return defaultCodeFromDate(date, false);
	}
	
	
	public static String defaultFromDate(Date date, boolean specialZero) {
		String result = defaultCodeFromDate(date, specialZero);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < result.length(); i++) {
			char ch = result.charAt(i);
			int index = INDEX.indexOf(ch);
			if (index >= 0) {
				sb.append(NAMES[index]);
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
	
	public static String defaultFromDate(Date date) {
		return defaultCodeFromDate(date, false);
	}
	

	public static void main(String[] args) {
		Date date = new Date();
		boolean specialZero = true;
		System.out.println(defaultCodeFromDate(date, specialZero));
		System.out.println(defaultFromDate(date, specialZero));

		// for (int number = 0; number < 60; number++)
		// System.out.println(number + " -> " + codeNumberWithin20(number));
	}

}
