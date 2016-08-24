package info.longnetpro.common.util.date;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateTime extends GregorianCalendar {
	private static final long serialVersionUID = 1L;
	public static final int MILLISECONDS_OF_DAY = 24 * 60 * 60 * 1000;

	public DateTime() {
		super();
	}

	public DateTime(int jdate) {
		super();
		this.setJdeDateTime(jdate, 0);
	}

	public DateTime(int jdate, int jtime) {
		super();
		this.setJdeDateTime(jdate, jtime);
	}

	public DateTime(TimeZone zone, Locale aLocale) {
		super(zone, aLocale);
	}

	public DateTime(TimeZone zone) {
		super(zone);
	}

	public DateTime(Locale aLocale) {
		super(aLocale);
	}

	public DateTime(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
		// month is 0-based
		super(year, month, dayOfMonth, hourOfDay, minute, second);
	}

	public DateTime(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
		// month is 0-based
		super(year, month, dayOfMonth, hourOfDay, minute);
	}

	public DateTime(int year, int month, int dayOfMonth) {
		// month is 0-based
		super(year, month, dayOfMonth);
	}

	public void complete() {
		super.complete();
	}

	public void setDate(Date date) {
		super.setTime(date);
		complete();
	}

	public Date getDate() {
		return super.getTime();
	}

	public void setYear(int year) {
		super.set(Calendar.YEAR, year);
	}

	public int getYear() {
		return super.get(Calendar.YEAR);
	}

	public void setMonth(int month) {
		super.set(Calendar.MONTH, month - 1);
	}

	public int getMonth() {
		return super.get(Calendar.MONTH) + 1;
	}

	public void setDay(int day) {
		super.set(Calendar.DAY_OF_MONTH, day);
	}

	public int getDay() {
		return super.get(Calendar.DAY_OF_MONTH);
	}

	public void setHour(int hour) {
		super.set(Calendar.HOUR_OF_DAY, hour);
	}

	public int getHour() {
		return super.get(Calendar.HOUR_OF_DAY);
	}

	public void setMinute(int minute) {
		super.set(Calendar.MINUTE, minute);
	}

	public int getMinute() {
		return super.get(Calendar.MINUTE);
	}

	public void setSecond(int second) {
		super.set(Calendar.SECOND, second);
	}

	public int getSecond() {
		return super.get(Calendar.SECOND);
	}

	public void setDayOfYear(int day) {
		super.set(Calendar.DAY_OF_YEAR, day);
		complete();
	}

	public int getDayOfYear() {
		return super.get(Calendar.DAY_OF_YEAR);
	}

	public void setJdeDate(int jdate) {
		int date = jdate;
		int day = date % 1000;
		date = (date - day) / 1000;
		int year = date + 1900;
		setYear(year);
		setDayOfYear(day);
	}

	public int getJdeDate() {
		int year = getYear();
		int day = getDayOfYear();
		int ret = (year - 1900) * 1000 + day;
		return ret;
	}

	public void setJdeTime(int jdeTime) {
		int time = jdeTime;
		int second = time % 100;
		time = (time - second) / 100;
		int minute = time % 100;
		int hour = (time - minute) / 100;
		setTime(hour, minute, second);
	}

	public int getJdeTime() {
		int hour = getHour();
		int minute = getMinute();
		int second = getSecond();
		int ret = (hour * 100 + minute) * 100 + second;
		return ret;
	}

	public void setMillisecond(int millisecond) {
		super.set(Calendar.MILLISECOND, millisecond);
	}

	public int getMillisecond() {
		return super.get(Calendar.MILLISECOND);
	}

	public int getDaysFromUnixEpoch() {
		long time = super.getTimeInMillis();
		int days = (int) (time / MILLISECONDS_OF_DAY);
		return days;
	}

	public Timestamp getTimestamp() {
		return new Timestamp(getTimeInMillis());
	}

	public void setDate(int year, int month, int date) {
		super.set(year, month - 1, date);
		complete();
	}

	public void setTime(int hour, int minute, int second) {
		super.set(HOUR_OF_DAY, hour);
		super.set(MINUTE, minute);
		super.set(SECOND, second);
		complete();
	}

	public void setMidNight() {
		setTime(0, 0, 0);
	}

	public void setJdeDateTime(int jdate, int jtime) {
		setJdeDate(jdate);
		setJdeTime(jtime);
		complete();
	}

	public void setJdeDateTime(int jdate) {
		setJdeDateTime(jdate, 0);
	}

	public void addYears(int years) {
		super.add(Calendar.YEAR, years);
	}

	public void addMonths(int months) {
		super.add(Calendar.MONTH, months);
	}

	public void addDays(int days) {
		super.add(Calendar.DAY_OF_MONTH, days);
	}

	public void addHours(int hours) {
		super.add(Calendar.HOUR_OF_DAY, hours);
	}

	public void addMinutes(int minutes) {
		super.add(Calendar.MINUTE, minutes);
	}

	public void addSeconds(int seconds) {
		super.add(Calendar.SECOND, seconds);
	}

	public int differMonths(DateTime dt) {
		int year = this.getYear();
		int month = this.getMonth();
		int y = dt.getYear();
		int m = dt.getMonth();
		int m0 = year * 12 + month;
		int m1 = y * 12 + m;
		return m0 - m1;
	}

	public DateTime copy() {
		return (DateTime) clone();
	}

	public String format(String format) {
		SimpleDateFormat sdf = null;
		if (format == null)
			sdf = new SimpleDateFormat();
		else
			sdf = new SimpleDateFormat(format);
		return sdf.format(getDate());
	}

	public int compareDateTo(DateTime datetime) {
		return getDaysFromUnixEpoch() - datetime.getDaysFromUnixEpoch();
	}

	public static Date toJavaDate(DateTime date) {
		return date.getDate();
	}

	public java.util.Date toJavaDate() {
		return toJavaDate(this);
	}

	public static DateTime fromJavaDate(Date date) {
		DateTime dt = new DateTime();
		dt.setDate(date);
		dt.complete();
		return dt;
	}

	public static String formatDate(Date date, String format) {
		DateTime dt = new DateTime();
		dt.setDate(date);
		return dt.format(format);
	}

	public static String formatDateISO8601(Date date) {
		return formatDate(date, "yyyy-MM-dd'T'HH:mm:ss','SSS");
	}

	public static String formatJdeTime(Integer time) {
		String t = null;
		if (time != null && time.intValue() >= 0) {
			int ss = time;
			int hh = ss / 10000;
			ss = ss % 10000;
			int mm = ss / 100;
			ss = ss % 100;

			if (ss >= 60) {
				mm += ss / 60;
				ss = ss % 60;
			}

			if (mm >= 60) {
				hh += mm / 60;
				mm = mm % 60;
			}

			if (hh >= 24) {
				hh = hh % 24;
			}

			String format = "%2d:%2d:%2d";
			t = String.format(format, hh, mm, ss);
			t = t.replaceAll(" ", "0");
		}
		return t;
	}

	public static Timestamp getCurrentTimeStamp() {
		return new Timestamp((new Date()).getTime());
	}

	public static Date stringToDate(String datetime) {
		return stringToDate(datetime, "yyyy-MM-dd");
	}

	public static Date stringToDate(String datetime, String format) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			Date date = dateFormat.parse(datetime);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int year4To2(int year) {
		if (year >= 2000)
			year -= 100;
		year -= 1900;
		return year;
	}

	public static Timestamp stringToTimestamp(String time, String format) {
		Date date = stringToDate(time, format);
		if (date == null)
			return null;
		Timestamp timestamp = new Timestamp(date.getTime());
		return timestamp;
	}

	public static Timestamp stringToTimestamp(String time) {
		return stringToTimestamp(time, "yyyy-MM-dd HH:mm:ss.SSS");
	}

	public static String getFullMonthName(int month) {
		DateTime dt = new DateTime();
		dt.setDate(new Date());
		dt.setMonth(month);
		return dt.format("MMMMMMMMMM");
	}

	public String getFullMonthName() {
		return format("MMMMMMMMMM");
	}

	public DateTime getFirstDayOfMonth() {
		DateTime dt = copy();
		dt.setDay(1);
		return dt;
	}

	public DateTime getLastDayOfMonth() {
		DateTime dt = copy();
		dt.addMonths(1);
		dt.setDay(1);
		dt.addDays(-1);
		return dt;
	}

	public DateTime getLastDayOfYear() {
		DateTime dt = copy();
		dt.setMonth(12);
		dt.setDay(31);
		return dt;
	}

	public String toString() {
		Date date = getDate();
		if (date == null)
			return null;
		else
			return date.toString();
	}
}
