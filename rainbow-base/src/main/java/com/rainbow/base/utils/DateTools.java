package com.rainbow.base.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author rainvom
 */
public class DateTools extends org.apache.commons.lang3.time.DateUtils {
  public static final String YYYY = "yyyy";

  public static final String YYYY_MM = "yyyy-MM";

  public static final String YYYY_MM_DD = "yyyy-MM-dd";

  public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

  public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

  private static String[] parsePatterns = {
          "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
          "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
          "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

  /**
   * 获取当前Date型日期
   *
   * @return Date() 当前日期
   */
  public static Date getNowDate() {
    return new Date();
  }

  /**
   * 获取当前日期, 默认格式为yyyy-MM-dd
   *
   * @return String
   */
  public static String getDate() {
    return dateTimeNow(YYYY_MM_DD);
  }

  public static final String getTime() {
    return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
  }

  public static final String dateTimeNow() {
    return dateTimeNow(YYYYMMDDHHMMSS);
  }

  public static final String dateTimeNow(final String format) {
    return parseDateToStr(format, new Date());
  }

  public static final String dateTime(final Date date) {
    return parseDateToStr(YYYY_MM_DD, date);
  }

  public static final String parseDateToStr(final String format, final Date date) {
    return new SimpleDateFormat(format).format(date);
  }

  public static final Date dateTime(final String format, final String ts) {
    try {
      return new SimpleDateFormat(format).parse(ts);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 日期路径 即年/月/日 如2018/08/08
   */
  public static final String datePath() {
    Date now = new Date();
    return DateFormatUtils.format(now, "yyyy/MM/dd");
  }

  /**
   * 日期路径 即年/月/日 如20180808
   */
  public static final String dateTime() {
    Date now = new Date();
    return DateFormatUtils.format(now, "yyyyMMdd");
  }


  public static LocalDate convertDateToLocalDate(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }


  public static int getMonthDifference(LocalDate date1, LocalDate date2) {
    Period period = Period.between(date1.withDayOfMonth(1), date2.withDayOfMonth(1));
    return period.getYears() * 12 + period.getMonths();
  }

  public static int getMonthDifference(Date d1, Date d2) {

    LocalDate date1 = convertDateToLocalDate(d1);
    LocalDate date2 = convertDateToLocalDate(d2);

    Period period = Period.between(date1.withDayOfMonth(1), date2.withDayOfMonth(1));
    return period.getYears() * 12 + period.getMonths();
  }
  public static int getDayDifference(Date d1, Date d2) {
    long divisor = 24L * 60L * 60L * 1000L;
    long timeDiff = d2.getTime() - d1.getTime();
    long dayDiff = Math.abs(timeDiff) / divisor;
    
    // 如果时间差不能被整天除尽，则需要向上取整
    if (Math.abs(timeDiff) % divisor != 0) {
      dayDiff += 1;
    }
    
    return (int) dayDiff;
  }


  public static int getMonthDifference(Calendar startDate, Calendar endDate) {
    int yearDiff = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
    int monthDiff = endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);
    return yearDiff * 12 + monthDiff;
  }

  /**
   * 日期型字符串转化为日期 格式
   */
  public static Date parseDate(Object str) {
    if (str == null) {
      return null;
    }
    try {
      return parseDate(str.toString(), parsePatterns);
    } catch (ParseException e) {
      return null;
    }
  }

  /**
   * 获取服务器启动时间
   */
  public static Date getServerStartDate() {
    long time = ManagementFactory.getRuntimeMXBean().getStartTime();
    return new Date(time);
  }

  /**
   * 计算相差天数
   */
  public static int differentDaysByMillisecond(Date date1, Date date2) {
    return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
  }

  /**
   * 计算时间差
   *
   * @param endDate   最后时间
   * @param startTime 开始时间
   * @return 时间差（天/小时/分钟）
   */
  public static String timeDistance(Date endDate, Date startTime) {
    long nd = 1000 * 24 * 60 * 60;
    long nh = 1000 * 60 * 60;
    long nm = 1000 * 60;
    // long ns = 1000;
    // 获得两个时间的毫秒时间差异
    long diff = endDate.getTime() - startTime.getTime();
    // 计算差多少天
    long day = diff / nd;
    // 计算差多少小时
    long hour = diff % nd / nh;
    // 计算差多少分钟
    long min = diff % nd % nh / nm;
    // 计算差多少秒//输出结果
    // long sec = diff % nd % nh % nm / ns;
    return day + "天" + hour + "小时" + min + "分钟";
  }

  /**
   * 增加 LocalDateTime ==> Date
   */
  public static Date toDate(LocalDateTime temporalAccessor) {
    ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
    return Date.from(zdt.toInstant());
  }

  /**
   * 增加 LocalDate ==> Date
   */
  public static Date toDate(LocalDate temporalAccessor) {
    LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
    ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
    return Date.from(zdt.toInstant());
  }
}
