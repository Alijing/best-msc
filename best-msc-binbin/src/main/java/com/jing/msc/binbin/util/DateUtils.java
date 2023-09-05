package com.jing.msc.binbin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: IntegratedBis
 * @Date: 2018/10/19 10:56
 * @Author: Jing
 * @Description:
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
    public static final String FORMAT_STRING = "yyyy-MM-dd";
    public static final String FORMAT_STRING_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_STRING_2 = "yyyy-MM-dd";
    public static final String FORMAT_STRING_3 = "yyyyMMddHHmmss";
    public static final String FORMAT_STRING_4 = "HH:mm:ss";
    public static final String FORMAT_STRING_5 = "yyyyMMdd";
    public static final String FORMAT_STRING_6 = "yyyy年MM月dd日";

    public static final String FORMAT_STRING_HOUR = "yyyy-MM-dd HH";
    public static final String FORMAT_STRING_MIN = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_STRING_SEC = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_STRING_MONTH = "yyyy-MM";

    public static final long MILLSECONDOFDAY = 24 * 3600 * 1000;
    public static final long MILLSECONDOYEAR = MILLSECONDOFDAY * 365;
    public static long eightHours = 8 * 3600 * 1000;

    public static DateFormat formathhmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    public static String[] dateTimePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss"
    };

    public static String[] timePatterns = {
            "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss"
    };

    public static String[] datePatterns = { "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd" };


    /**
     * 获取一段时间内所有天数
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 所有天数
     */
    public static List<String> getDays(String begin, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STRING_2);
        List<String> strDate = new ArrayList<>();
        try {
            strDate.add(begin);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(begin));
            while (true) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                if (sdf.parse(end).after(calendar.getTime())) {
                    strDate.add(sdf.format(calendar.getTime()));
                } else {
                    break;
                }
            }
            strDate.add(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }

    //上周开始时间
    public static String geLastWeekMondayStrBeginTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday());
        cal.add(Calendar.DATE, -7);
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_STRING_1);//设置日期格式
        return df.format(cal.getTime()).substring(0, 10) + " 00:00:00";
    }

    //上周结束时间
    public static String getLastWeekCurTime() {
        return getFixedDistanceCurTime(Calendar.DATE, -7);
    }

    //本周开始时间
    public static String getThisWeekMondayStrBeginTime() {
        Date date = getThisWeekMonday();
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_STRING_1);//设置日期格式
        return df.format(date).substring(0, 10) + " 00:00:00";
    }

    public static Date getThisWeekMonday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    //上月开始时间
    public static String getLastMonthFirstDayStrBeginTime() {
        String lastMonthFirstDay = getFixedDistanceCurTime(getThisMonthFirstDay(), Calendar.MONTH, -1);
        return lastMonthFirstDay.substring(0, 7) + "-01 00:00:00";
    }

    public static String getFixedDistanceCurTime(int type, int distance) {
        return getFixedDistanceCurTime(new Date(), type, distance);
    }

    public static String getFixedDistanceCurTime(Date date, int type, int distance) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        //过去七天
        c.setTime(date);
        c.add(type, distance);
        Date d = c.getTime();
        String time = format.format(d);
        return time;

    }

    //上月结束时间
    public static String getLastMonthCurTime() {
        return getFixedDistanceCurTime(Calendar.MONTH, -1);
    }


    //getThisMonthFirstDayStrBeginTime
    public static String getThisMonthFirstDayStrBeginTime() {
        SimpleDateFormat myFormatter = new SimpleDateFormat(FORMAT_STRING);
        String thisMonthFirstCurTime = myFormatter.format(getThisMonthFirstDay());
        return thisMonthFirstCurTime.substring(0, 7) + "-01 00:00:00";
    }

    public static Date getThisMonthFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    //今天开始时间
    public static String getCurDayBeginStrTime() {
        String currentTime = DateUtils.getCurrentDateTime();
        String dayBeginTime = currentTime.substring(0, 10) + " 00:00:00";
        return dayBeginTime;
    }

    //上周结束时间(取一整天)
    public static String getLastWeekCurTimeStartAtZero() {
        String lastWeekCurTime = getFixedDistanceCurTime(Calendar.DATE, -7);
        String lastWeekCurTimeStartAtZero = lastWeekCurTime.substring(0,10)+" 00:00:00";
        return lastWeekCurTimeStartAtZero;
    }
    //上周结束时间(取一整天)
    public static String getFixedDistanceCurTimeStartAtZero(int distance) {
        String lastWeekCurTime = getFixedDistanceCurTime(Calendar.DATE, distance);
        String lastWeekCurTimeStartAtZero = lastWeekCurTime.substring(0,10)+" 00:00:00";
        return lastWeekCurTimeStartAtZero;
    }

    /**
     * 获取当前系统时间
     *
     * @return 当前系统时间
     */
    public static String getStrCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_STRING_2);//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    /**
     * 获取当前系统时间
     *
     * @return 当前系统时间
     */
    public static String getCurrentDate(String sdf) {
        SimpleDateFormat df = new SimpleDateFormat(sdf);//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    /**
     * 获取当前时间信息
     *
     * @return
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_STRING_1);//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    public static long getCurrentDate() {
        Date date = new Date();
        return date.getTime();
    }

    public static long getDateTime(String time, String sdf) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(sdf);
            Date date = format.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getDateTime(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(FORMAT_STRING_1);
            Date date = format.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Date getDate(long timestamp, String sdf) throws ParseException {
        return new Date(timestamp);
    }

    public static Date getDate(String timeStr, String sdf) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
        return simpleDateFormat.parse(timeStr);
    }

    public static Calendar getCalendar(String timeStr, String sdf) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(sdf);
        Date date = format.parse(timeStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 将日期按pattern格式化成字符串
     *
     * @param date    日期
     * @param pattern 日期格式化样式，例如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sfDate = new SimpleDateFormat(pattern);
        sfDate.setLenient(false);
        return sfDate.format(date);
    }

    public static String stampToDate(long timestamp, String sdf) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
        Date date = new Date(timestamp);
        return simpleDateFormat.format(date);
    }

    public static String stampToDate(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_STRING_1);
        Date date = new Date(timestamp);
        return simpleDateFormat.format(date);
    }

    public static Date getDate(String timestr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_STRING_1);
        if (!timestr.contains(" ")) timestr += " 00:00:00";
        return format.parse(timestr);
    }

    /**
     * 13位时间戳 转换成日期格式
     *
     * @param time   1525849325942
     * @param format 需转换的时间格式
     * @return
     */
    public static String timeStamp2Date(String time, String format) {
        Long timeLong = Long.parseLong(time);
        SimpleDateFormat sdf = new SimpleDateFormat(format);//要转换的时间格式
        Date date;
        try {
            date = sdf.parse(sdf.format(timeLong));
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当前时间的后几小时或者是前几小时
     *
     * @param interval 间隔小时数
     * @return
     */
    public static String getTimePastHour(int interval, String sdfStr) {

        SimpleDateFormat sdf = new SimpleDateFormat(sdfStr);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, interval);
        date = calendar.getTime();
        return sdf.format(date);


//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STRING_1);
//            Date dt = sdf.parse(timeStr);
//            Calendar newTime = Calendar.getInstance();
//            newTime.setTime(dt);
//            newTime.add(Calendar.HOUR_OF_DAY, addHour);
//            Date dt1 = newTime.getTime();
//            return sdf.format(dt1);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }
    }

    /**
     * 获取当前时间的后几天或者是前几天
     *
     * @param interval 间隔天数
     * @return 后几天或者是前几天
     */
    public static String getDistanceCurrentDayDate(int interval) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, interval);
        date = calendar.getTime();
        return sdf.format(date);
    }

    /**
     * 获取当前时间的后几天或者是前几天
     *
     * @param interval 间隔天数
     * @return 后几天或者是前几天
     */
    public static String getDistanceCurrentDayDate(int interval, String sdfStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(sdfStr);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, interval);
        date = calendar.getTime();
        return sdf.format(date);
    }

    /**
     * 获取指定时间的后几天或者是前几天
     *
     * @param interval 间隔天数
     * @return 后几天或者前几天
     */
    public static String getDistanceAppointDayDate(String appointDate, int interval, String sdfStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(sdfStr);
        Date date = sdf.parse(appointDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, interval);
        date = calendar.getTime();
        return sdf.format(date);
    }

    /**
     * 获取指定时间的后几天或者是前几天
     *
     * @param interval 间隔天数
     * @return 后几天或者前几天
     */
    public static String getDistanceAppointDayDate(Date appointDate, int interval) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STRING_1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(appointDate);
        calendar.add(Calendar.DAY_OF_MONTH, interval);
        appointDate = calendar.getTime();
        return sdf.format(appointDate);
    }




    /**
     * 时间比大小
     *
     * @param t1
     * @param t2
     * @return
     */
    public static int timeCompare(String t1, String t2, String sdf) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(sdf);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(formatter.parse(t1));
        c2.setTime(formatter.parse(t2));
        return c1.compareTo(c2);
    }

    /**
     * 判断两个时间段是否存在交集
     * @param startTime1
     * @param endTime1
     * @param startTime2
     * @param endTime2
     * @return
     */

    public static boolean timeRangeCompare(String startTime1, String endTime1, String startTime2, String endTime2, String sdf){
        try {
            if(DateUtils.timeCompare(startTime1,startTime2,sdf)>=0 && DateUtils.timeCompare(startTime1,endTime2,sdf)<0
                    || DateUtils.timeCompare(endTime1,startTime2,sdf)>0 && DateUtils.timeCompare(endTime1,endTime2,sdf)<=0
                    || DateUtils.timeCompare(startTime1,startTime2,sdf)<=0 && DateUtils.timeCompare(endTime1,endTime2,sdf)>=0
                    || DateUtils.timeCompare(startTime1,startTime2,sdf)>=0 && DateUtils.timeCompare(endTime1,endTime2,sdf)<=0){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String dateFormat(String dateStr, String sSdfStr, String eSdfStr) {
        try {
            SimpleDateFormat sFormat = new SimpleDateFormat(sSdfStr);
            SimpleDateFormat eFormat = new SimpleDateFormat(eSdfStr);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(sFormat.parse(dateStr));
            return eFormat.format(c1.getTime());
        } catch (ParseException e) {
            logger.error("-- >>>> [ dateFormat ] parse error : ", e);
        }
        return null;
    }

    public static String dateFormat(long time, String eSdfStr) {
        return new SimpleDateFormat(eSdfStr).format(new Date(time));
    }

    public static long dateToTimestamp(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(time);
            long ts = date.getTime();
            return ts;
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 计算指定年度共有多少个周。
     *
     * @param year 格式 yyyy ，必须大于1900年度 小于9999年
     * @return
     */
    public static int getWeekNumByYear(final int year) {
        if (year < 1900 || year > 9999) {
            throw new NullPointerException("年度必须大于等于1900年小于等于9999年");
        }
        int result = 52;// 每年至少有52个周 ，最多有53个周。
        String date = getYearWeekFirstDay(year, 53);
        if (date.substring(0, 4).equals(year + "")) { // 判断年度是否相符，如果相符说明有53个周。
            result = 53;
        }
        return result;
    }

    /**
     * 计算某年某周的开始日期
     *
     * @param yearNum 格式 yyyy ，必须大于1900年度 小于9999年
     * @param weekNum 1到52或者53
     * @return 日期，格式为yyyy-MM-dd
     */
    public static String getYearWeekFirstDay(int yearNum, int weekNum) {
        if (yearNum < 1900 || yearNum > 9999) {
            throw new NullPointerException("年度必须大于等于1900年小于等于9999年");
        }
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 每周从周一开始
        // 上面两句代码配合，才能实现，每年度的第一个周，是包含第一个星期一的那个周。
        cal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
        cal.set(Calendar.YEAR, yearNum);
        cal.set(Calendar.WEEK_OF_YEAR, weekNum);

        // 分别取得当前日期的年、月、日
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /**
     * 计算某年某周的结束日期
     *
     * @param yearNum 格式 yyyy ，必须大于1900年度 小于9999年
     * @param weekNum 1到52或者53
     * @return 日期，格式为yyyy-MM-dd
     */
    public static String getYearWeekEndDay(int yearNum, int weekNum) {
        if (yearNum < 1900 || yearNum > 9999) {
            throw new NullPointerException("年度必须大于等于1900年小于等于9999年");
        }
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);// 每周从周一开始
        // 上面两句代码配合，才能实现，每年度的第一个周，是包含第一个星期一的那个周。
        cal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
        cal.set(Calendar.YEAR, yearNum);
        cal.set(Calendar.WEEK_OF_YEAR, weekNum);

        // 分别取得当前日期的年、月、日
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * 大于24小时返回天数
     * 小于24小时返回小时
     *
     * @param dateStr1
     * @param dateStr2
     * @return
     */
    public static int diffTimeBMillisecond(String dateStr1, String dateStr2, String sdf) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(sdf);
        Date date1 = format.parse(dateStr1);
        Date date2 = format.parse(dateStr2);
        long hours = (date2.getTime() - date1.getTime()) / (1000 * 3600);
        if (hours > 24) return (int) (hours / 24);
        else return (int) hours;
    }

    /**
     * 在指定的时间上添加指定秒数
     *
     * @param timeStr   指定的时间
     * @param addSecond 添加指定秒数
     * @return
     */
    public static String timePastSecond(String timeStr, int addSecond) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STRING_4);
            Date dt = sdf.parse(timeStr);
            Calendar newTime = Calendar.getInstance();
            newTime.setTime(dt);
            newTime.add(Calendar.SECOND, addSecond);
            Date dt1 = newTime.getTime();
            return sdf.format(dt1);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 在指定的时间上添加指定小时数
     *
     * @param timeStr 指定的时间
     * @param addHour 添加指定小时
     * @return
     */
    public static String timePastHour(String timeStr, int addHour) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STRING_1);
            Date dt = sdf.parse(timeStr);
            Calendar newTime = Calendar.getInstance();
            newTime.setTime(dt);
            newTime.add(Calendar.HOUR_OF_DAY, addHour);
            Date dt1 = newTime.getTime();
            return sdf.format(dt1);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 在指定的时间上添加指定小时数
     *
     * @param timeStr 指定的时间
     * @param addHour 添加指定小时
     * @return
     */
    public static String timePastHour(String timeStr, int addHour, String sdfStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(sdfStr);
            Date dt = sdf.parse(timeStr);
            Calendar newTime = Calendar.getInstance();
            newTime.setTime(dt);
            newTime.add(Calendar.HOUR_OF_DAY, addHour);
            Date dt1 = newTime.getTime();
            return sdf.format(dt1);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 获取时间段内所有的年月集合
     *
     * @param minDate 最小时间  2017-01
     * @param maxDate 最大时间 2017-10
     * @return 日期集合 格式为 年-月
     * @throws Exception
     */
    public static List<String> getMonthBetween(String minDate, String maxDate) throws Exception {
        ArrayList<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STRING_MONTH);//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        while (min.before(max)) {
            result.add(sdf.format(min.getTime()));
            min.add(Calendar.MONTH, 1);
        }
        return result;
    }

    /**
     * 获取某天的开始时间
     *
     * @param date
     * @return
     */
    public static Date getBeginTime(Date date) {
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return start.getTime();
    }

    /**
     * 获取某天的结束时间
     *
     * @param date
     * @return
     */
    public static Date getEndTime(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取当前系统时间
     *
     * @return 当前系统时间
     */
    public static String getStrCurrentMonthDate() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_STRING_MONTH);//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    public static String getDistanceCurrentMonthDate(int interval) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, interval);
        Date m = c.getTime();
        return sdf.format(m);
    }

    public static String getFormatStartTime(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return sdf.format(date);
    }

    public static String getFormatEndTime(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return sdf.format(date);
    }


    public static String getDate()
    {
        return formatDate(new Date(), FORMAT_STRING);
    }

    public static String getDate(Date date)
    {
        return formatDate(date, FORMAT_STRING);
    }

    public static final String getDateTime()
    {
        return formatDate(new Date(), FORMAT_STRING_1);
    }

    public static String getDateTime(Date date)
    {
        return formatDate(date, FORMAT_STRING_1);
    }


    /**
     * 日期型字符串转化为日期 只有日期格式
     */
    public static Date parseByDate(String str) {
        try {
            return parseDate(str, datePatterns);
        }
        catch (Exception e) { }
        return null;
    }

    /**
     * 日期型字符串转化为日期 带时分秒日期格式
     */
    public static Date parseByTime(String str) {
        try {
            return parseDate(str, timePatterns);
        }
        catch (Exception e) { }
        return null;
    }

    /**
     * 日期型字符串转化为日期 同时有日期和带时分秒日期格式
     */
    public static Date parseByDateTime(String str) {
        try {
            return parseDate(str, dateTimePatterns);
        }
        catch (Exception e) { }
        return null;
    }

    /**
     * 设置时间为当天的最后一秒
     * @param dateStr 时间字符串
     * @date 2021-06-30
     */
    public static Date getEndTime(String dateStr) {
        Date endDate = parseByDate(dateStr);
        return endDate == null ? parseByTime(dateStr) : getEndTime(endDate);
    }

}
