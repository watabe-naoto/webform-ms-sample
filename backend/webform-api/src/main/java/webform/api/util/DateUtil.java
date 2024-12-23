package webform.api.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.JapaneseChronology;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	/** 西暦年月 */
	public static DateTimeFormatter format_uuuuMM = DateTimeFormatter.ofPattern("uuuuMM");
	/** 西暦年月(uuuu年M月) */
	public static DateTimeFormatter format_uuuuM_kanji = DateTimeFormatter.ofPattern("uuuu年M月");
	/** 西暦年月日 */
	public static DateTimeFormatter format_uuuuMMdd = DateTimeFormatter.ofPattern("uuuuMMdd");
	/** 西暦年月日(uuuu/MM/dd) */
	public static DateTimeFormatter format_uuuuMMdd_solidus = DateTimeFormatter.ofPattern("uuuu/MM/dd");
	/** 西暦年月日(24)時分秒 */
	public static DateTimeFormatter format_uuuuMMddHHmmss = DateTimeFormatter.ofPattern("uuuuMMddHHmmss");
	/** 西暦年月日(24)時分秒(uuuu/MM/dd HH:mm:ss) */
	public static DateTimeFormatter format_uuuuMMddHHmmss_solidus = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
	/** 西暦年月日(12)時分 */
	public static DateTimeFormatter format_uuuuMMddEaKmm = DateTimeFormatter.ofPattern("uuuu年MM月dd日(E) aK:mm", Locale.JAPANESE);
	/** 和暦年月日 */
	public static DateTimeFormatter format_Gymd = DateTimeFormatter.ofPattern("Gy年M月d日").withLocale(Locale.JAPANESE).withChronology(JapaneseChronology.INSTANCE);
	/** 時分 */
	public static DateTimeFormatter format_HHmm = DateTimeFormatter.ofPattern("HHmm");
	/** 時分(12時間制表記) */
	public static DateTimeFormatter format_aKmm = DateTimeFormatter.ofPattern("aK:mm", Locale.JAPANESE);
	/** 時分秒 */
	public static DateTimeFormatter format_HHmmss = DateTimeFormatter.ofPattern("HHmmss");
	/** 時分秒(コロン区切り) */
	public static DateTimeFormatter format_HHmmss_colon = DateTimeFormatter.ofPattern("HH:mm:ss");

	/**
	 * 時刻文字列取得
	 *
	 * @param dateTime LocalDateTime
	 * @return String
	 */
	public static String getDateTimeString(LocalDateTime dateTime) {
		return dateTime.format(format_uuuuMMddHHmmss);
	}

	/**
	 * LocalDateTime -> Date変換
	 * @param dateTime LocalDateTime
	 * @return java.util.Date
	 */
	public static Date convertDateFromLocalDateTime(LocalDateTime dateTime) {
		return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Date -> LocalDateTime変換
	 * @param dateTime LocalDateTime
	 * @return java.util.Date
	 */
	public static LocalDateTime convertLocalDateTimeFromDate(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	/**
	 * 月末を取得
	 *
	 * @param year
	 * @param month
	 * @return LocalDate
	 */
	public static LocalDate getLastDayOfMonth(int year, int month) {
		return LocalDate.of(year, month, LocalDate.of(year, month, 1).lengthOfMonth());
	}
}
