package com.trading.tradergateway.Util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil extends org.apache.commons.lang3.time.DateUtils {

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        if (date == null) {
            return null;
        }
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }


    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {

        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {

        return formatDate(new Date(), "dd");
    }

    public static String getHour() {

        return formatDate(new Date(), "HH");
    }
    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {

        return formatDate(new Date(), "E");
    }

    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
     * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     *
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        if(t>=0){
            return t / (24 * 60 * 60 * 1000);
        }
        else{
            return -1;
        }
    }
    /**
     * 得到当前周起始时间
     *
     * @param date
     * @return
     */
    public static String getWeekStart(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.get(Calendar.WEEK_OF_YEAR);
        int firstDay = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, firstDay);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return format.format(calendar.getTime());
    }

    /**
     * 得到当前周截止时间
     *
     * @param date
     * @return
     */
    public static String getWeekEnd(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.get(Calendar.WEEK_OF_YEAR);
        int firstDay = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, 8 - firstDay);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return format.format(calendar.getTime());
    }

    public static String getFirstDayOfMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());
        return first;
    }

    /**
     * 月的最后一天
     *
     * @param date
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getLastDayOfMonth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cDay1 = Calendar.getInstance();
        cDay1.setTime(date);
        int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date lastDate = cDay1.getTime();
        lastDate.setDate(lastDay);
        String last = formatDate(lastDate);
        return last;
    }



    /**
     * 两个日期相关天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getDateDiff(String startDate, String endDate) {
        long diff = 0;
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

            long num = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);
            if(num >=0){
                diff = num;

            }
            else{
                diff = -num;
            }
        }
        catch (ParseException e) {
            return -1;
        }
        return diff;
    }

    /**
     * 计算 day 天后的时间
     *
     * @param date
     * @param day
     * @return
     */
    public static String addDay(Date date, int day) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        String dayStr = format.format(calendar.getTime());
        return dayStr;
    }



    public static boolean isSameWeekWithToday(Date date) {

        if (date == null) {
            return false;
        }

// 0.先把Date类型的对象转换Calendar类型的对象
        Calendar todayCal = Calendar.getInstance();
        Calendar dateCal = Calendar.getInstance();

        todayCal.setTime(new Date());
        dateCal.setTime(date);
        int subYear = todayCal.get(Calendar.YEAR) - dateCal.get(Calendar.YEAR);
// subYear==0,说明是同一年
        if (subYear == 0) {
            if (todayCal.get(Calendar.WEEK_OF_YEAR) == dateCal.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        }
        else if (subYear == 1 && dateCal.get(Calendar.MONTH) == 11 && todayCal.get(Calendar.MONTH) == 0) {
            if (todayCal.get(Calendar.WEEK_OF_YEAR) == dateCal.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        }
        else if (subYear == -1 && todayCal.get(Calendar.MONTH) == 11 && dateCal.get(Calendar.MONTH) == 0) {
            if (todayCal.get(Calendar.WEEK_OF_YEAR) == dateCal.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     *
     * @param date
     * @return
     */
    public static int getSeason(Date date) {

        int season = 0;

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }

    /**
     * 字符转日期
     *
     * @param dateStr
     * @return
     */
    public static Date getDateByStr(String dateStr) throws Exception{
        SimpleDateFormat formatter = null;
        if (dateStr == null) {
            return null;
        }
        else if (dateStr.length() == 10) {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        }
        else if (dateStr.length() == 16) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
        else if (dateStr.length() == 19) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        else if (dateStr.length() > 19) {
            dateStr = dateStr.substring(0, 19);
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        else {
            return null;
        }

        return formatter.parse(dateStr);

    }

}