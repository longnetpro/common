package info.longnetpro.common.util.date;

public class FiscalDate {
	public FiscalDate() {
		super();
	}

	private boolean startMonthInThisFiscalYear = true;
	private int startMonthNumber = 1; // 1 - 12
	private int endMonthNumber = 12; // 1 - 12
	private int fiscalYear = -1;
	private int fiscalMonth = -1;
	private int fiscalQuarter = -1;

	public static FiscalDate create() {
		return new FiscalDate();
	}

	public void directBuild(int fYear, int fMonth) {
		int m = normalizeMonth(fMonth);
		int q = (m % 3 == 0) ? m / 3 : m / 3 + 1;
		this.fiscalYear = fYear;
		this.fiscalMonth = m;
		this.fiscalQuarter = q;
	}

	public void build(int year, int month, int day) {
		int offset = 0;

		if (startMonthNumber > 1) {
			if (month >= this.startMonthNumber) {
				offset = startMonthInThisFiscalYear ? 0 : 1;
			} else {
				offset = startMonthInThisFiscalYear ? -1 : 0;
			}
		} else if (day == 0) {
			offset = 0;
		}

		int y = year + offset;
		int m = positiveModulus(month - this.startMonthNumber, 12) + 1;
		int q = (m % 3 == 0) ? m / 3 : m / 3 + 1;
		this.fiscalYear = y;
		this.fiscalMonth = m;
		this.fiscalQuarter = q;
	}

	public void build(int year, int month) {
		build(year, month, 1);
	}

	public void build(int julianDate) {
		DateTime dt = new DateTime();
		dt.setJdeDateTime(julianDate, 0);
		dt.complete();
		int year = dt.getYear();
		int month = dt.getMonth();
		build(year, month);
	}

	public static int positiveModulus(int a, int b) {
		return (a % b + b) % b;
	}

	public static FiscalDate fromCalendarDate(DateTime dateTime, int monthNumber, boolean isStartMonth,
			boolean startMonthInThisFiscalYear) {
		if (dateTime == null)
			return null;
		int year = dateTime.getYear();
		int month = dateTime.getMonth();
		FiscalDate fd = FiscalDate.create();
		fd.setStartMonthInThisFiscalYear(startMonthInThisFiscalYear);
		fd.setBorderMonth(monthNumber, isStartMonth);
		fd.build(year, month);
		return fd;
	}

	public static FiscalDate fromCalendarDate(DateTime dateTime, int monthNumber, boolean isStartMonth) {
		return fromCalendarDate(dateTime, monthNumber, isStartMonth, true);
	}

	public static FiscalDate fromCalendarDate(DateTime dateTime, int monthNumber) {
		return fromCalendarDate(dateTime, monthNumber, true, true);
	}

	public static int normalizeMonth(int month) {
		int m = positiveModulus(month, 12);
		return m;
	}

	public static int monthOffset(int monthNumber, boolean isStartMonth, boolean startMonthInThisFiscalYear) {
		int offset = 0;

		int mStart = isStartMonth ? normalizeMonth(monthNumber) : getStartMonthFromEndMonth(monthNumber);
		if (mStart == 1)
			return 0;
		if (startMonthInThisFiscalYear) {
			offset = mStart - 1;
		} else {
			offset = mStart - 13;
		}
		return offset;
	}

	public static int getStartMonthFromJulianRange(int year, int startJulian, int endJulian) {
		// if the result is positive, then start month is in Year year
		// if the result is negative, then start month is in previous year (end
		// month in this year)
		// if the result is zero, means error.

		DateTime dt1 = new DateTime(startJulian, 0);
		DateTime dt2 = new DateTime(endJulian, 0);

		int year1 = dt1.getYear();
		int year2 = dt2.getYear();
		int month1 = dt1.getMonth();
		int month2 = dt2.getMonth();

		if (year == year1 && year == year2 && month1 == 1 && month2 == 12) {
			return 1;
		}

		if (month1 == month2 + 1) {
			if (year == year1 && year == year2 - 1) {
				return month1;
			} else if (year == year1 - 1 && year == year2) {
				return -month1;
			}
		}

		return 0;

	}

	public static DateTime[] getFiscalDateRangeFromJulianRange(int fYear, int fiscalYear, int startJulian,
			int endJulian) {
		FiscalDate fd = new FiscalDate();
		fd.setBorderMonthByJulianRange(fiscalYear, startJulian, endJulian);
		fd.directBuild(fYear, 1);
		return fd.getFiscalDateRange();
	}

	public static DateTime toCalendarDate(int fiscalYear, int fiscalMonth, int monthNumber, boolean isStartMonth,
			boolean startMonthInThisFiscalYear) {
		if (fiscalYear == -1 || fiscalMonth == -1) {
			return null;
		}
		DateTime dt = new DateTime();
		dt.setYear(fiscalYear);
		dt.setMonth(fiscalMonth);
		dt.setDay(1);
		dt.complete();
		int offset = monthOffset(monthNumber, isStartMonth, startMonthInThisFiscalYear);
		dt.addMonths(offset);
		dt.complete();
		return dt;
	}

	public static DateTime toCalendarDate(FiscalDate fiscalDate) {
		if (fiscalDate == null)
			return null;
		int fy = fiscalDate.getFiscalYear();
		int fm = fiscalDate.getFiscalMonth();
		int m = fiscalDate.getStartMonthNumber();
		boolean startMonthInThisFiscalYear = fiscalDate.isStartMonthInThisFiscalYear();
		return toCalendarDate(fy, fm, m, true, startMonthInThisFiscalYear);
	}

	public DateTime[] getFiscalDateRange() {
		DateTime[] range = { null, null };
		int year1 = fiscalYear;
		int year2 = fiscalYear;

		if (startMonthNumber > 1) {
			if (startMonthInThisFiscalYear) {
				year2++;
			} else {
				year1--;
			}
		}

		DateTime dtStart = new DateTime();
		dtStart.setYear(year1);
		dtStart.setMonth(startMonthNumber);
		dtStart.setTime(0, 0, 0);
		range[0] = dtStart.getFirstDayOfMonth();
		DateTime dtEnd = new DateTime();
		dtEnd.setYear(year2);
		dtEnd.setMonth(endMonthNumber);
		dtEnd.setTime(23, 59, 59);
		range[1] = dtEnd.getLastDayOfMonth();

		return range;
	}

	public void printFiscalDateDetails() {
		System.out.println("---------------------------------------------------------");
		final String dateFormat = "yyyy-MM-dd";
		final String actualDate = "Actual Date: %s.";
		final String fiscalYearStarts = "Fiscal year starts from the first day on Month %02d.";
		final String fiscalYearEnds = "Fiscal year ends on the last day on Month %02d.";
		final String fiscalDate = "Fiscal Date: %4d-%02d (Quarter %d).";
		final String startsInActualDate = "Fiscal year starts in actual year.";
		final String endsInActualDate = "Fiscal year ends in actual year.";
		final String dateRange = "Date Range: from %s to %s";
		DateTime dt = toCalendarDate();
		System.out.println(String.format(actualDate, dt.format(dateFormat)));
		System.out.println(String.format(fiscalDate, getFiscalYear(), getFiscalMonth(), getFiscalQuarter()));
		System.out.println(String.format(fiscalYearStarts, getStartMonthNumber()));
		System.out.println(String.format(fiscalYearEnds, getEndMonthNumber()));
		System.out.println(isStartMonthInThisFiscalYear() ? startsInActualDate : endsInActualDate);
		DateTime[] range = getFiscalDateRange();
		System.out.println(String.format(dateRange, range[0].format(dateFormat), range[1].format(dateFormat)));
	}

	public DateTime toCalendarDate() {
		return toCalendarDate(this);
	}

	public static int getEndMonthFromStartMonth(int startMonth) {
		int endMonth = normalizeMonth(startMonth - 1);
		if (endMonth == 0) {
			endMonth = 12;
		}
		return endMonth;
	}

	public static int getStartMonthFromEndMonth(int endMonth) {
		int startMonth = normalizeMonth(endMonth + 1);
		if (startMonth == 0) {
			startMonth = 12;
		}
		return startMonth;
	}

	public FiscalDate setFiscalYearStartMonth(int fiscalYearStartMonth) {
		return setBorderMonth(fiscalYearStartMonth, true);
	}

	public FiscalDate setFiscalYearEndMonth(int fiscalYearEndMonth) {
		return setBorderMonth(fiscalYearEndMonth, false);
	}

	public FiscalDate setBorderMonthByJulianRange(int fYear, int startJulian, int endJulian) {
		int m = getStartMonthFromJulianRange(fYear, startJulian, endJulian);
		boolean startMonthInThisFiscalYear = m > 0;
		if (m < 0)
			m = -m;
		setFiscalYearStartMonth(m).setStartMonthInThisFiscalYear(startMonthInThisFiscalYear);
		return this;
	}

	public FiscalDate setBorderMonth(int monthNumber, boolean isStartMonth) {
		if (isStartMonth) {
			int mStart = normalizeMonth(monthNumber);
			int mEnd = getEndMonthFromStartMonth(mStart);
			this.startMonthNumber = mStart == 0 ? 12 : mStart;
			this.endMonthNumber = mEnd;
		} else {
			int mEnd = normalizeMonth(monthNumber);
			int mStart = getStartMonthFromEndMonth(mEnd);
			this.startMonthNumber = mStart;
			this.endMonthNumber = mEnd == 0 ? 12 : mEnd;
		}
		return this;
	}

	public int getFiscalYear() {
		return fiscalYear;
	}

	public int getFiscalMonth() {
		return fiscalMonth;
	}

	public int getFiscalQuarter() {
		return fiscalQuarter;
	}

	public int getStartMonthNumber() {
		return startMonthNumber;
	}

	public int getEndMonthNumber() {
		return endMonthNumber;
	}

	public FiscalDate setStartMonthInThisFiscalYear(boolean startMonthInThisFiscalYear) {
		this.startMonthInThisFiscalYear = startMonthInThisFiscalYear;
		return this;
	}

	public boolean isStartMonthInThisFiscalYear() {
		return startMonthInThisFiscalYear;
	}
}
