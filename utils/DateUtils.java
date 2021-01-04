package com.qiandai.fourfactors.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    /**
     * 变量：日期格式化类型 - 格式:yyyy/MM/dd
     */
    public static final int DEFAULT = 0;
    public static final String DEFAULT_PATTERN = "yyyy/MM/dd";

    /**
     * 变量：日期格式化类型 - 格式:yyyy/MM
     */
    public static final int YM = 1;
    public static final String YM_PATTERN = "yyyy/MM";


    /**
     * 变量：日期格式化类型 - 格式:yyyyMMdd
     */
    public static final int NO_SLASH = 2;
    public static final String NO_SLASH_PATTERN = "yyyyMMdd";

    /**
     * 变量：日期格式化类型 - 格式:yyyyMM
     */
    public static final int YM_NO_SLASH = 3;
    public static final String YM_NO_SLASH_PATTERN = "yyyyMM";

    /**
     * 变量：日期格式化类型 - 格式:yyyy/MM/dd HH:mm:ss
     */
    public static final int DATE_TIME = 4;
    public static final String DATE_TIME_PATTERN = "yyyy/MM/dd HH:mm:ss";

    /**
     * 变量：日期格式化类型 - 格式:yyyyMMddHHmmss
     */
    public static final int DATE_TIME_NO_SLASH = 5;
    public static final String DATE_TIME_NO_SLASH_PATTERN = "yyyyMMddHHmmss";

    /**
     * 变量：日期格式化类型 - 格式:yyyy/MM/dd HH:mm
     */
    public static final int DATE_HM = 6;
    public static final String DATE_HM_PATTERN = "yyyy/MM/dd HH:mm";

    /**
     * 变量：日期格式化类型 - 格式:HH:mm:ss
     */
    public static final int TIME = 7;
    public static final String TIME_PATTERN = "HH:mm:ss";

    /**
     * 变量：日期格式化类型 - 格式:HH:mm
     */
    public static final int HM = 8;
    public static final String HM_PATTERN = "HH:mm";

    /**
     * 变量：日期格式化类型 - 格式:HHmmss
     */
    public static final int LONG_TIME = 9;
    public static final String LONG_TIME_PATTERN = "HHmmss";

    /**
     * 变量：日期格式化类型 - 格式:HHmm
     */
    public static final int SHORT_TIME = 10;
    public static final String SHORT_TIME_PATTERN = "HHmm";

    /**
     * 变量：日期格式化类型 - 格式:yyyy-MM-dd
     */
    public static final int YMR_SLASH = 11;
    public static final String YMR_SLASH_PATTERN = "yyyy-MM-dd";

    /**
     * 变量：日期格式化类型 - 格式:yyyy-MM-dd HH:mm:ss
     */
    public static final int DATE_TIME_LINE = 12;
    public static final String DATE_TIME_LINE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 变量：日期格式化类型 - 格式:yyyy-MM-dd'T'HH:mm:ss.SSS
     */
    public static final int DATETIME_ZONE_FORMAT = 13;
    public static final String DATETIME_ZONE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    /**
     * 变量：日期格式化类型 - 格式:yyyyMMddHHmmssSSS
     */
    public static final int LONG_DATETIME_FORMAT = 14;
    public static final String LONG_DATETIME_FORMAT_PATTERN = "yyyyMMddHHmmssSSS";

    /**
     * 返回指定格式的日期字符串
     * @param date
     * @param type
     * @return
     */
     public static String dateToStr(Date date, int type) {
        switch (type) {
            case DEFAULT:
                return defaultDateToStr(date);
            case YM:
                return dateToStr(date, YM_PATTERN);
            case NO_SLASH:
                return dateToStr(date, NO_SLASH_PATTERN);
            case YM_NO_SLASH:
                return dateToStr(date, YM_NO_SLASH_PATTERN);
            case DATE_TIME:
                return dateToStr(date, DATE_TIME_PATTERN);
            case DATE_TIME_NO_SLASH:
                return dateToStr(date, DATE_TIME_NO_SLASH_PATTERN);
            case DATE_HM:
                return dateToStr(date, DATE_HM_PATTERN);
            case TIME:
                return dateToStr(date, TIME_PATTERN);
            case HM:
                return dateToStr(date, HM_PATTERN);
            case LONG_TIME:
                return dateToStr(date, LONG_TIME_PATTERN);
            case SHORT_TIME:
                return dateToStr(date, SHORT_TIME_PATTERN);
            case YMR_SLASH:
                return dateToStr(date, YMR_SLASH_PATTERN);
            case DATE_TIME_LINE:
                return dateToStr(date, DATE_TIME_LINE_PATTERN);
            case DATETIME_ZONE_FORMAT:
                return dateToStr(date, DATETIME_ZONE_FORMAT_PATTERN);
            case LONG_DATETIME_FORMAT:
                return dateToStr(date, LONG_DATETIME_FORMAT_PATTERN);
            default:
                throw new IllegalArgumentException("Type undefined : " + type);
        }
    }

    public static String getSimpleTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_ZONE_FORMAT_PATTERN);
        String simpleTime = dateFormat.format(new Date());
        return simpleTime;
    }

    public static String currentTimestamp2String(Date date, String format) {
        if (StringUtils.isEmpty(format)) {
            format = DATE_TIME_LINE_PATTERN;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date String2Date(String sourceTime) {
        return string2Date(sourceTime, YMR_SLASH_PATTERN);
    }

    public static Date string2Date(String sourceTime, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(sourceTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String date2String(Date date) {
        if (null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            return sdf.format(date);
        } else {
            return null;
        }
    }

    /**
     * 获得前几天的时间
     *
     * @param now
     * @param day
     * @return
     */
    public static Date getDateBefore(Date now, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.set(Calendar.DATE, c.get(Calendar.DATE) - day);
        return c.getTime();
    }

    /**
     * 获得后几天的时间
     *
     * @param now
     * @param day
     * @return
     */
    public static Date getDateAfter(Date now, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.set(Calendar.DATE, c.get(Calendar.DATE) + day);
        return c.getTime();
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDaysOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    public static String defaultDateToStr(Date date) {
        return dateToStr(date, "yyyy/MM/dd");
    }

    public static String dateToStr(Date date, String pattern) {
        if (date == null)
            return null;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public static Date getMonthFirstDay(String dateStr) {
        Date firstDay = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
            Date date = sdf.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DATE, 1);
            firstDay = cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return firstDay;
    }

    public static Date getMonthLastDay(String dateStr) {
        Date firstDay = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
            Date date = sdf.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DATE, 1);
            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.DATE, -1);
            firstDay = cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return firstDay;
    }
}
