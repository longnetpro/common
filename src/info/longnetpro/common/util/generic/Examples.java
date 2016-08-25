package info.longnetpro.common.util.generic;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class Examples {
	private static void numberChineseConverterEx1()
			throws UnsupportedEncodingException, FileNotFoundException, IOException, NumberChineseConverterException {
		String file = "c:\\temp\\chinese.txt";
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		for (int i = 0; i < 1000000; i++) {
			int j = i * 1;
			String s = String.format("%d -> %s", j, NumberChineseConverter.toNumberUpperCase(j));
			writer.write(s);
			writer.write('\n');
		}
		writer.flush();
		writer.close();

	}

	public static void numberChineseConverterEx2()
			throws UnsupportedEncodingException, FileNotFoundException, IOException, NumberChineseConverterException {
		String num = "0.00";
		System.out.println("Pattern: " + num.matches(NumberChineseConverter.NUMBER_PATTERN));
		System.out.println("Normal: " + NumberChineseConverter.normalizeNumber(num));
		System.out.println("Coding: " + NumberChineseConverter.codeNumberString(num));
		System.out.println("Money: " + NumberChineseConverter.codeMoneyString(num));
		System.out.println("Number: " + NumberChineseConverter.toNumberLowerCase(num));
		System.out.println("Money: " + NumberChineseConverter.toMoneyLowerCase(num));
	}

	private static void numberRomanConverterEx1()
			throws UnsupportedEncodingException, FileNotFoundException, IOException, NumberRomanConverterException {
		String file = "c:\\temp\\roman.txt";
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		for (int i = 1; i < 1000; i++) {
			int j = i * 100000;
			String s = String.format("%d -> %s", j, NumberRomanConverter.fromInteger(j));
			writer.write(s);
			writer.write('\n');
		}
		writer.flush();
		writer.close();
	}

	private static void numberRomanConverterEx2()
			throws UnsupportedEncodingException, FileNotFoundException, IOException, NumberRomanConverterException {
		String file = "c:\\temp\\roman1.txt";
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		for (int i = 1; i < 1000; i++) {
			int j = i * 100000;
			String number = j + "";
			String s = String.format("%d -> %s", j, NumberRomanConverter.fromInteger(number));
			writer.write(s);
			writer.write('\n');
		}
		writer.flush();
		writer.close();
	}

	public static void numberRomanConverterEx3()
			throws UnsupportedEncodingException, FileNotFoundException, IOException, NumberRomanConverterException {
		String result = NumberRomanConverter.fromInteger(12345678);
		System.out.println(result);
	}

	public static void DateChineseConverterEx1() {
		Date date = new Date();
		String[] codes = DateChineseConverter.getCodesFromDate(date);

		String dateString = "";
		for (String code : codes) {
			dateString += code;
		}
		System.out.println(dateString);
		System.out.println(DateChineseConverter.translateCode(dateString));
		System.out.println(DateChineseConverter.formatDate("Dt|AT'YMdH'|'msW'", date));

		// for (int number = 0; number < 60; number++)
		// System.out.println(number + " -> " + codeNumberWithin20(number));
	}

	public static void main(String[] args)
			throws UnsupportedEncodingException, FileNotFoundException, IOException, NumberRomanConverterException {
		numberRomanConverterEx3();
	}
}
