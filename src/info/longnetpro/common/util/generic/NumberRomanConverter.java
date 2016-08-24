package info.longnetpro.common.util.generic;

public class NumberRomanConverter {
	private static final String[] ROMANS = new String[] { "I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D",
			"CM", "M", "M(V)", "(V)", "M(X)" };
	private static final int[] INTEGERS = new int[] { 1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000, 4000, 5000,
			9000 };

	private static String fromIntegerLessThan10000(int num) {
		if (num == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		int times = 0;
		for (int i = INTEGERS.length - 1; i >= 0; i--) {
			times = num / INTEGERS[i];
			num %= INTEGERS[i];
			while (times > 0) {
				sb.append(ROMANS[i]);
				times--;
			}
		}
		String s = sb.toString();
		return s;
	}

	private static String fromIntegerString(String num) {
		String number = num.replaceAll("^0+", "");
		final String format = "(%s)%s";
		String result = "";
		if (number.length() < 5) {
			int n = Integer.parseInt(number);
			result = fromIntegerLessThan10000(n);
		} else {
			int length = number.length();
			String sn = number.substring(0, length - 3);
			String sr = number.substring(length - 3, length);
			int r = Integer.parseInt(sr);
			result = String.format(format, fromIntegerString(sn), fromIntegerLessThan10000(r));
		}
		return result;
	}

	public static String fromInteger(String num) throws NumberRomanConverterException {
		if (num == null || !num.trim().matches("^\\d+$") || num.trim().equals("0")) {
			throw new NumberRomanConverterException("Invalid Positive Integer: " + (num == null ? "null" : num.trim()));
		}
		return fromIntegerString(num);
	}

	public static String fromInteger(int num) throws NumberRomanConverterException {
		String number = num + "";
		return fromInteger(number);
	}
}
