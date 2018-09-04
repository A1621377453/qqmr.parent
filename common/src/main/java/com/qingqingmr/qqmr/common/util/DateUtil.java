package com.qingqingmr.qqmr.common.util;

import org.apache.commons.lang3.StringUtils;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil extends org.apache.commons.lang3.time.DateUtils {

    private DateUtil() {
    }

    /**
     * 日期增加
     *
     * @param date
     * @param calendarType
     * @param calendarValue
     * @return
     */
    public static Date add(Date date, int calendarType, int calendarValue) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendarType, calendarValue);
        return calendar.getTime();
    }

    /**
     * 日期增加n年
     *
     * @param date 日期
     * @param year 年数
     * @return
     */
    public static Date addYear(Date date, int year) {
        return add(date, Calendar.YEAR, year);
    }

    /**
     * 日期增加n月
     *
     * @param date  日期
     * @param month 月数
     * @return
     */
    public static Date addMonth(Date date, int month) {
        return add(date, Calendar.MONTH, month);
    }

    /**
     * 日期增加n天
     *
     * @param date 日期
     * @param day  天数
     * @return
     */
    public static Date addDay(Date date, int day) {
        return add(date, Calendar.DAY_OF_YEAR, day);
    }

    /**
     * 日期减少n年
     *
     * @param date 日期
     * @param year 年数
     * @return
     */
    public static Date minusYear(Date date, int year) {
        return add(date, Calendar.YEAR, -year);
    }

    /**
     * 日期减少n月
     *
     * @param date  日期
     * @param month 月数
     * @return
     */
    public static Date minusMonth(Date date, int month) {
        return add(date, Calendar.MONTH, -month);
    }

    /**
     * 日期减少n天
     *
     * @param date 日期
     * @param day  天数
     * @return
     */
    public static Date minusDay(Date date, int day) {
        return add(date, Calendar.DAY_OF_YEAR, -day);
    }

    /**
     * 计算两个时间之间相差的分钟数
     *
     * @param startDate
     * @param endDate
     * @return
     * @author yintao
     * @createDate 2018年1月29日
     */
    public static long getMinutesDiffFloor(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidParameterException("startDate and endDate cannot be null!");
        }
        if (startDate.after(endDate)) {
            throw new InvalidParameterException("startDate cannot be after endDate!");
        }
        long differenceTime = (endDate.getTime() - startDate.getTime()) / (1000 * 60);
        return differenceTime;
    }

    /**
     * 时间字符串转换成时间
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        if (StringUtils.isBlank(strDate)) {
            throw new InvalidParameterException("strDate cannot be null!");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException("strDate convert date failure!");
        }
        return date;
    }

    /**
     * 将时间转换成时间字符串
     *
     * @param date
     * @param format
     * @param defaultValue
     * @return
     */
    public static String dateToStr(Date date, String format, String defaultValue) {
        if (date == null || StringUtils.isBlank(format)) {
            return defaultValue;
        }
        String result = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            result = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
        return result;
    }

    /**
     * 日期格式化
     *
     * @param date    日期
     * @param formate 格式，以本类定义的格式为准，可自行添加。
     * @return
     */
    public static String dateToString(Date date, String formate) {
        if (date == null) {
            return "";
        }

        return new SimpleDateFormat(formate).format(date);
    }

    /**
     * 字符串转日期
     *
     * @param dateString
     * @param format     日期格式
     * @return
     */
    public static Date strToDate(String dateString, String format) {
        if (dateString == null) {
            throw new InvalidParameterException("dateString cannot be null!");
        }
        try {
            return new SimpleDateFormat(format).parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("parse error! [dateString:" + dateString + ";format:" + format + "]");
        }
    }

    /**
     * 判断是否过期
     *
     * @param validTime 生产日期
     * @param time      有效期 单位秒
     * @return
     */
    public static boolean isInValidTime(Date validTime, int time) {
        long currTime = System.currentTimeMillis();
        long valid = validTime.getTime();

        return ((currTime - valid) < time * 1000);
    }
}
