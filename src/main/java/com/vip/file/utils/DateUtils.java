package com.vip.file.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * 
 * @author ruoyi
 */
@Slf4j
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static String HH_MM_SS = "HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     * 
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }
    
    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2)
    {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
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
     * 给时间加上几个小时
     *
     * @param day  当前时间 格式：yyyy-MM-dd HH:mm:ss
     * @param hour 需要加的时间
     * @param rules   时间规则 hour小时，minute分钟
     * @return
     */
    public static String addDateMinut(String rules,String day, int hour) {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null){
            return "";
        }
        //显示输入的日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 24小时制
        if("hour".equals(rules)){
            cal.add(Calendar.HOUR, hour);
        }else if("minute".equals(rules)){
            cal.add(Calendar.MINUTE,hour);
        }

        date = cal.getTime();
        return format.format(date);
    }

    /**
     * 给时间加上几个小时
     *
     * @return
     */
    public static String reduceDateMinute(String time,int minute ) {
        // 获取当前时间
        LocalTime currentTime = LocalTime.parse(time);
        // 减去2分钟
        LocalTime subtractedTime = currentTime.minusMinutes(minute);
        // 格式化时间输出
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(HH_MM_SS);
        String formattedTime = subtractedTime.format(formatter);
        return formattedTime;
    }



    public static void main(String[] args) {
//        System.out.println("@@@@@@@@@"+addDateMinut("minute","2023-06-20 14:32:55",2));
//        System.out.println(compareTime("2022-08-24 12:00:00","2022-08-24 13:00:00"));
//        compareTime("2022-08-24 12:00:00","2022-08-24 13:00:00");
        System.out.println(reduceDateMinute("12:00:00",2));
    }

    /**
     * 比较时间大小
     * @param time1
     * @param time2
     * @return
     */
    public static boolean compareTime(String time1,String time2){
        try {
            SimpleDateFormat sf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            Date date1 = sf.parse(time1);
            Date date2 = sf.parse(time2);
            if(date2.getTime()<=date1.getTime()){
                return true;
            }else{
                return false;
            }
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
            return false;
        }
    }

//    /**
//     * 获取前一天日期yyyy-MM-dd
//     * @return
//     */
//    public String getDate() {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(Calendar.DAY_OF_MONTH, -1);
//        date = calendar.getTime();
//        String time = df.format(date);
//        return time;
//    }

}
