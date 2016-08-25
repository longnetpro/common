package info.longnetpro.common.util.generic;

public final class NumberChineseConverter {
	public static final String NUMBER_PATTERN = "^[+\\-]?(?:(?:\\d+(?:\\.\\d*)?)|(?:\\.\\d+))$";
	
	private static final char[] CHINESE_UNITS1 = new char[] { 'S', 'B', 'Q' };
	private static final char[] CHINESE_UNITS2 = new char[] { 'Y', 'W' };
	private static final char[] CHINESE_INTEGERS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private static final String INDEX = "0123456789SBQWY+-.$JFZR";
	private static final String[] NAMES_LOWER = new String[] { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十",
			"百", "千", "万", "亿", "正", "负", "点", "元", "角", "分", "整", "人民币" };
	private static final String[] NAMES_UPPER = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾",
			"佰", "仟", "萬", "億", "正", "负", "点", "圆", "角", "分", "整", "人民币" };

	// 一二三四五六七八九十百千万亿正负点元角分整人民币〇
	// 壹贰叁肆伍陆柒捌玖拾佰仟萬億正负点圆角分整人民币

	public static String normalizeNumber(String numberString) {
		String result = numberString;
		result = result.replaceAll("\\.$", "");
		result = result.replaceAll("^([+\\-]?)0+", "$1");
		result = result.replaceAll("^([+\\-]?)\\.", "$10.");
		if (result.equals("+") || result.equals("-") || result.isEmpty()) {
			result = "0";
		}
		return result;
	}

	private static String codeIntegerString(String integerString) {
		StringBuilder sb = new StringBuilder();
		if (integerString == null || integerString.isEmpty()) {
			return CHINESE_INTEGERS[0] + "";
		}

		char sign = integerString.charAt(0);
		if (sign != '+' && sign != '-') {
			sign = ' ';
		} else {
			sb.append(sign);
		}

		boolean zeros = false;
		int start = (sign == ' ') ? 0 : 1;

		if (integerString.length() - start == 1 && integerString.charAt(start) == '0') {
			return integerString;
		}

		for (int i = start; i < integerString.length(); i++) { // process
																// integer only
			int index = integerString.length() - i - 1; // from right
			int unit = index % 4;
			int n = integerString.charAt(i) - '0';
			if (n != 0) {
				if (zeros) {
					sb.append(CHINESE_INTEGERS[0]);
				}
				sb.append(CHINESE_INTEGERS[n]);
				if (unit > 0) {
					sb.append(CHINESE_UNITS1[unit - 1]);
				}
				zeros = false;
			} else {
				zeros = true;
			}
			if (unit == 0 && index > 0) {
				int group = index / 4 % 2;
				sb.append(CHINESE_UNITS2[group]);
			}
		}
		String result = sb.toString();
		if (result.isEmpty()) {
			result = CHINESE_INTEGERS[0] + "";
		}
		return result;
	}

	public static String codeNumberString(String numberString) throws NumberChineseConverterException {
		if (numberString == null || !numberString.trim().matches(NUMBER_PATTERN)) {
			throw new NumberChineseConverterException(
					"Invalid Number: " + (numberString == null ? "null" : numberString.trim()));
		}
		String number = normalizeNumber(numberString.trim());
		if (number.length() == 0) {
			return CHINESE_INTEGERS[0] + "";
		}

		String intStr = number;
		String decStr = "";

		int decimalPos = number.indexOf('.');
		if (decimalPos > 0) {
			intStr = number.substring(0, decimalPos);
			decStr = number.substring(decimalPos);
		}

		intStr = codeIntegerString(intStr);
		return intStr + decStr;
	}

	public static String codeCurrencyString(String moneyString) throws NumberChineseConverterException {
		if (moneyString == null || !moneyString.trim().matches(NUMBER_PATTERN)) {
			throw new NumberChineseConverterException(
					"Invalid Number: " + (moneyString == null ? "null" : moneyString.trim()));
		}
		String number = normalizeNumber(moneyString.trim());
		number = number.replaceAll("0*$", "");
		number = number.replaceAll("\\.$", "");

		if (number.length() == 0) {
			return "R" + CHINESE_INTEGERS[0] + "$Z";
		}

		String intStr = number;
		String decStr = "";

		int decimalPos = number.indexOf('.');
		if (decimalPos > 0) {
			intStr = number.substring(0, decimalPos);
			decStr = number.substring(decimalPos + 1);
		}

		boolean zeros = intStr.endsWith("0");
		boolean intZero = intStr.matches("^[+\\-]?0$");

		intStr = codeIntegerString(intStr);
		if (decStr.isEmpty()) {
			return "R" + intStr + "$Z";
		}

		// process decimal
		StringBuilder sb = new StringBuilder();
		if (!intZero)
			sb.append("$");
		for (int i = 0; i < decStr.length(); i++) {
			char ch = decStr.charAt(i);
			if (i == 0) { // Jiao
				if (ch != '0') {
					if (zeros && !intZero) {
						sb.append(CHINESE_INTEGERS[0]);
					}
					sb.append(ch);
					sb.append('J');
					zeros = false;
				} else {
					zeros = true;
				}
			} else if (i == 1) { // Fen
				if (ch != '0' && zeros && !intZero) {
					sb.append('0');
				}
				sb.append(ch);
				sb.append("F");
			} else {
				sb.append(ch);
			}
		}
		String result = (intZero) ? sb.toString() : intStr + sb.toString();
		result = "R" + result;
		return result;
	}

	public static String formatNumber(String numberString, boolean isLower) throws NumberChineseConverterException {
		String s = codeNumberString(numberString);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			int index = INDEX.indexOf(ch);
			if (isLower) {
				sb.append(NAMES_LOWER[index]);
			} else {
				sb.append(NAMES_UPPER[index]);
			}
		}
		return sb.toString();
	}

	public static String toNumberLowerCase(String numberString) throws NumberChineseConverterException {
		return formatNumber(numberString, true);
	}

	public static String toNumberUpperCase(String numberString) throws NumberChineseConverterException {
		return formatNumber(numberString, false);
	}

	public static String formatNumber(int number, boolean isLower) throws NumberChineseConverterException {
		String numStr = number + "";
		return formatNumber(numStr, isLower);
	}

	public static String toNumberLowerCase(int number) throws NumberChineseConverterException {
		return formatNumber(number, true);
	}

	public static String toNumberUpperCase(int number) throws NumberChineseConverterException {
		return formatNumber(number, false);
	}

	public static String formatCurrency(String moneyString, boolean isLower) throws NumberChineseConverterException {
		String s = codeCurrencyString(moneyString);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			int index = INDEX.indexOf(ch);
			if (isLower) {
				sb.append(NAMES_LOWER[index]);
			} else {
				sb.append(NAMES_UPPER[index]);
			}
		}
		return sb.toString();
	}

	public static String toCurrencyLowerCase(String moneyString) throws NumberChineseConverterException {
		return formatCurrency(moneyString, true);
	}

	public static String toCurrencyUpperCase(String moneyString) throws NumberChineseConverterException {
		return formatCurrency(moneyString, false);
	}

}
