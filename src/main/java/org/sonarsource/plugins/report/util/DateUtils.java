package org.sonarsource.plugins.report.util;

import org.apache.commons.lang3.ObjectUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具
 */
public class DateUtils {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    private static DateFormat sdf;

    static {
        sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String formatDateCurrent() {
        return sdf.format(new Date());
    }


    /**
     * 格式化时间
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return sdf.format(date);
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }


    /**
     * 获取当天
     *
     * @return
     */
    public static Date todayWithoutTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 不带时分秒的日期
     *
     * @param date
     * @return
     */
    public static Date dateWithoutTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取日期最后时刻
     *
     * @param date
     * @return
     */
    public static Date dateTimeEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }


    /**
     * 获取后面日期
     *
     * @param date
     * @param n
     * @return
     */
    public static Date nextDate(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.roll(Calendar.DAY_OF_YEAR, n);
        return calendar.getTime();
    }

    /**
     * 两个日期间隔天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int daysBetween(Date date1, Date date2) {
        date1 = dateWithoutTime(date1);
        date2 = dateWithoutTime(date2);
        return (int) ((date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000));
    }

    /**
     * 获取两个日期中大的一个
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Date max(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return ObjectUtils.firstNonNull(date1, date2);
        }
        if (date1.getTime() > date2.getTime()) {
            return date1;
        }
        return date2;
    }


    /**
     * 两个时间间隔秒值
     *
     * @param date
     * @return
     */
    public static long secondsDiff(Date date) {
        return Math.abs(date.getTime() - System.currentTimeMillis()) / 1000L;
    }

}
