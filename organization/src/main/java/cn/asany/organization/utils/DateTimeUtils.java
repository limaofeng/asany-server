package cn.asany.organization.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author JIANGKAIJIE
 * @version 1.0
 * @date 2019/3/25 10:52
 */
public class DateTimeUtils {

    /**
     * 取得当前时间
     * <p>
     * 取得Date类型当前时间
     *
     * @return
     */
    public static Date getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * 取得当前时间
     * <p>
     * 取得当前时间字符串
     *
     * @param datetimePattern
     * @return
     */
    public static String getCurrentTime(String datetimePattern) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datetimePattern);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 取得当前时间毫秒数
     * <p>
     * 取得从1970-01-01 00:00:00到当前时间的毫秒数
     *
     * @return
     */
    public static String getCurrentMilliseconds() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.getTimeInMillis());
    }

    /**
     * 取得当前时间秒数
     * <p>
     * 取得从1970-01-01 00:00:00到当前时间的秒数
     *
     * @return
     */
    public static String getCurrentSeconds() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf((int) calendar.getTimeInMillis() / 1000);
    }

    /**
     * 比较时间是否早于另一时间
     *
     * @param sourceDate
     * @param targetDate
     * @return
     */
    public static Boolean compareDate(Date sourceDate, Date targetDate) {

        if (sourceDate.before(targetDate)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 比较时间是否早于另一时间
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Boolean compareDate(String startDate, String endDate) throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat(DatePattern.YYYY_MM_DD_HH_MM_SS_DASH.getPattern());
        if(dateFormat.parse(startDate).getTime() < dateFormat.parse(endDate).getTime()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 取得日期时间
     * <p>
     * 将日期时间字符串转换为Date类型
     *
     * @param datetime
     * @param datetimePattern
     * @return
     * @throws ParseException
     */
    public static Date getDatetime(String datetime, String datetimePattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datetimePattern);
        return simpleDateFormat.parse(datetime);
    }

    /**
     * 取得日期时间
     * <p>
     * 将Date类型日期时间转换为字符串
     *
     * @param datetime
     * @param datetimePattern
     * @return
     */
    public static String getDatetime(Date datetime, String datetimePattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datetimePattern);
        return simpleDateFormat.format(datetime);
    }

    /**
     * 计算年份
     * <p>
     * 将Date类型的日期时间计算后返回格式化后的日期时间字符串
     *
     * @param sourceDatetime
     * @param datetimePattern
     * @param interval
     * @return
     */
    public static String calculateYear(Date sourceDatetime, String datetimePattern, int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sourceDatetime);
        calendar.add(Calendar.YEAR, interval);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datetimePattern);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 计算年份
     * <p>
     * 将日期时间字符串计算后返回格式化后的日期时间字符串
     *
     * @param sourceDatetime
     * @param sourceDatetimePattern
     * @param targetDatetimePattern
     * @param interval
     * @return
     * @throws ParseException
     */
    public static String calculateYear(String sourceDatetime, String sourceDatetimePattern,
                                       String targetDatetimePattern, int interval) throws ParseException {

        Date date = getDatetime(sourceDatetime, sourceDatetimePattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, interval);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(targetDatetimePattern);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 计算月份
     * <p>
     * 将Date类型的日期时间计算后返回格式化后的日期时间字符串
     *
     * @param sourceDatetime
     * @param datetimePattern
     * @param interval
     * @return
     */
    public static String calculateMonth(Date sourceDatetime, String datetimePattern, int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sourceDatetime);
        calendar.add(Calendar.MONTH, interval);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datetimePattern);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 计算月份
     * <p>
     * 将日期时间字符串计算后返回格式化后的日期时间字符串
     *
     * @param sourceDatetime
     * @param sourceDatetimePattern
     * @param targetDatetimePattern
     * @param interval
     * @return
     * @throws ParseException
     */
    public static String calculateMonth(String sourceDatetime, String sourceDatetimePattern,
                                        String targetDatetimePattern, int interval) throws ParseException {

        Date date = getDatetime(sourceDatetime, sourceDatetimePattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, interval);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(targetDatetimePattern);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 计算日期天数
     * <p>
     * 将Date类型的日期时间计算后返回格式化后的日期时间字符串
     *
     * @param sourceDatetime
     * @param datetimePattern
     * @param interval
     * @return
     */
    public static String calculateDate(Date sourceDatetime, String datetimePattern, int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sourceDatetime);
        calendar.add(Calendar.DATE, interval);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datetimePattern);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 计算日期天数
     * <p>
     * 将日期时间字符串计算后返回格式化后的日期时间字符串
     *
     * @param sourceDatetime
     * @param sourceDatetimePattern
     * @param targetDatetimePattern
     * @param interval
     * @return
     * @throws ParseException
     */
    public static String calculateDate(String sourceDatetime, String sourceDatetimePattern,
                                       String targetDatetimePattern, int interval) throws ParseException {

        Date date = getDatetime(sourceDatetime, sourceDatetimePattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, interval);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(targetDatetimePattern);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 计算小时
     * <p>
     * 将Date类型的日期时间计算后返回格式化后的日期时间字符串
     *
     * @param sourceDatetime
     * @param datetimePattern
     * @param interval
     * @return
     */
    public static String calculateHour(Date sourceDatetime, String datetimePattern, int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sourceDatetime);
        calendar.add(Calendar.HOUR_OF_DAY, interval);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datetimePattern);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 计算小时
     * <p>
     * 将日期时间字符串计算后返回格式化后的日期时间字符串
     *
     * @param sourceDatetime
     * @param sourceDatetimePattern
     * @param targetDatetimePattern
     * @param interval
     * @return
     * @throws ParseException
     */
    public static String calculateHour(String sourceDatetime, String sourceDatetimePattern,
                                       String targetDatetimePattern, int interval) throws ParseException {

        Date date = getDatetime(sourceDatetime, sourceDatetimePattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, interval);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(targetDatetimePattern);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 计算分钟
     * <p>
     * 将Date类型的日期时间计算后返回格式化后的日期时间字符串
     *
     * @param sourceDatetime
     * @param datetimePattern
     * @param interval
     * @return
     */
    public static String calculateMinute(Date sourceDatetime, String datetimePattern, int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sourceDatetime);
        calendar.add(Calendar.MINUTE, interval);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datetimePattern);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 计算分钟
     * <p>
     * 将日期时间字符串计算后返回格式化后的日期时间字符串
     *
     * @param sourceDatetime
     * @param sourceDatetimePattern
     * @param targetDatetimePattern
     * @param interval
     * @return
     * @throws ParseException
     */
    public static String calculateMinute(String sourceDatetime, String sourceDatetimePattern,
                                         String targetDatetimePattern, int interval) throws ParseException {

        Date date = getDatetime(sourceDatetime, sourceDatetimePattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, interval);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(targetDatetimePattern);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 计算秒数
     * <p>
     * 将Date类型的日期时间计算后返回格式化后的日期时间字符串
     *
     * @param sourceDatetime
     * @param datetimePattern
     * @param interval
     * @return
     */
    public static String calculateSecond(Date sourceDatetime, String datetimePattern, int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sourceDatetime);
        calendar.add(Calendar.SECOND, interval);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datetimePattern);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 计算秒数
     * <p>
     * 将日期时间字符串计算后返回格式化后的日期时间字符串
     *
     * @param sourceDatetime
     * @param sourceDatetimePattern
     * @param targetDatetimePattern
     * @param interval
     * @return
     * @throws ParseException
     */
    public static String calculateSecond(String sourceDatetime, String sourceDatetimePattern,
                                         String targetDatetimePattern, int interval) throws ParseException {

        Date date = getDatetime(sourceDatetime, sourceDatetimePattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, interval);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(targetDatetimePattern);
        return simpleDateFormat.format(calendar.getTime());
    }


    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static String getMondayOfThisWeek() {

        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        c.add(Calendar.DATE, -day_of_week + 1);
        return format.format(c.getTime());
    }

    /**
     * 得到本周周日
     *
     * @return yyyy-MM-dd
     */
    public static String getSundayOfThisWeek() {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0){
            day_of_week = 7;
        }
        c.add(Calendar.DATE, -day_of_week + 7);
        return format.format(c.getTime());
    }


    /**
     * 根据星期几获取本周对应的日期
     *
     * @return yyyy-MM-dd
     */
    public static String getweekByDate(String weekName) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0){
            day_of_week = 7;
        }

        int i=1;

        if("星期一".equals(weekName)){
        i=1;
        }else if("星期二".equals(weekName)){
            i=2;
        }else if("星期三".equals(weekName)){
            i=3;
        }else if("星期四".equals(weekName)){
            i=4;
        }else if("星期五".equals(weekName)){
            i=5;
        }else if("星期六".equals(weekName)){
            i=6;
        }else if("星期日".equals(weekName)){
            i=7;
        }
        c.add(Calendar.DATE, -day_of_week + i);
        return format.format(c.getTime());
    }
}