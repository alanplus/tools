package com.android.tools.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mouse on 2017/11/20.
 * yyyy-MM-dd HH:mm:ss 24小时
 * yyyy-MM-dd hh:mm:ss
 */
public class TimeUtils {

    public static final int ONE_SECOND_MILLIS = 1000;
    public static final int ONE_MINUTE_MILLIS = 60*ONE_SECOND_MILLIS;
    public static final int ONE_HOUR_MILLIS = 60*ONE_MINUTE_MILLIS;
    public static final int ONE_DAY_MILLIS = 24*ONE_HOUR_MILLIS;



    /**
     * 是否是今天
     *
     * @param timeMillis
     *            距离1970的毫秒数
     * @return
     */
    public static boolean isToday(long timeMillis) {
        long[] t = getTodayTimeMillis();
        return timeMillis >= t[0] && timeMillis <= t[1];
    }

    /**
     * 是否是昨天
     *
     * @param timeMillis
     *            距离1970年的毫秒数
     * @return
     */
    public static boolean isYestoday(long timeMillis) {
        return isToday(timeMillis + ONE_DAY_MILLIS);
    }

    /**
     * 是否是今年
     *
     * @param timeMillis
     * @return
     */
    public static boolean isThisYear(long timeMillis) {
        Calendar now = Calendar.getInstance();
        int thisYear = now.get(Calendar.YEAR);
        now.setTimeInMillis(timeMillis);
        return now.get(Calendar.YEAR) == thisYear;
    }

    public static int getMonth(long timeMillis){
        Calendar according = Calendar.getInstance();
        according.setTimeInMillis(timeMillis);
        int month = according.get(Calendar.MONTH);
        return month+1;
    }

    public static int getYear(long timeMillis){
        Calendar according = Calendar.getInstance();
        according.setTimeInMillis(timeMillis);
        return according.get(Calendar.YEAR);
    }

    public static String getStrBylong(long time,String regex){
        Date date = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat(regex);
        return  sf.format(date);
    }

    /**
     * 当年只返回月日，其它年份都返回
     */
    public static String getTimeString(String year, long time){
        Date date = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", new Locale(""));
        String timeStr = sf.format(date);

        if(timeStr.contains(year)) timeStr = timeStr.replace(year + "-", "");
        return timeStr;
    }

    public static long getTimeByString(String time, String formatStr){
        SimpleDateFormat format =  new SimpleDateFormat(formatStr, new Locale(""));
        try {
            Date date = format.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getTimeByString(String time){
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    //"yyyy-MM-dd"
    public static String formatDateTime(long time, String format){
        Date date = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat(format, new Locale(""));
        return sf.format(date);
    }

    private static String[] weeks = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
    public static String dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", new Locale(""));
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException ignored) {
        }
        int dayForWeek;
        if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            dayForWeek = 7;
        }else{
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return weeks[dayForWeek - 1];
    }

    public static String dayForWeek(long pTime) {
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", new Locale(""));
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(pTime));
        int dayForWeek;
        if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            dayForWeek = 7;
        }else{
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return weeks[dayForWeek - 1];
    }

    public static int getWeekByInt(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
    public static String getWeekByStr(long time,String pre){
        int week = getWeekByInt(time);
        String str = "未知";
        switch (week){
            case 1:
                str = pre+"日";
                break;
            case 2:
                str = pre+"一";
                break;
            case 3:
                str = pre+"二";
                break;
            case 4:
                str = pre+"三";
                break;
            case 5:
                str = pre+"四";
                break;
            case 6:
                str = pre+"五";
                break;
            case 7:
                str = pre+"六";
                break;
        }
        return str;
    }


    /**
     * 获取今天的毫秒区间
     *
     * @return
     */
    public static long[] getTodayTimeMillis() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.MILLISECOND, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);

        long start = now.getTimeInMillis();
        long end = start + ONE_DAY_MILLIS;
        return new long[] { start, end - 1 };
    }

    /**
     *
     * @param mss 单位是秒
     * @return 时分秒
     */
    public static String formatDateTime(long mss){
        if(mss == 0) return "";
        String dateTimes = "";
        long days = mss / ( 60 * 60 * 24);
        long hours = (mss % ( 60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % ( 60 * 60)) /60;
        long seconds = mss % 60;
        if(days>0){
            dateTimes= days + "天";
        }
        if(hours>0){
            dateTimes +=hours + "小时";
        }
        if(minutes>0){
            dateTimes +=minutes + "分钟";
        }
        if(seconds>0){
            dateTimes +=seconds + "秒";
        }

        return dateTimes;
    }

    public static String getTimeString(long time){
        StringBuilder sb = new StringBuilder();
        if(TimeUtils.isToday(time)){
            sb.append("今天  ");
        }else{
            String date = TimeUtils.getStrBylong(time, "MM月dd日");
            sb.append(date+"  ");
        }

        String week = TimeUtils.getWeekByStr(time, "周");
        sb.append(week+"  ");

        String timeStr = TimeUtils.getStrBylong(time, "HH:mm");
        sb.append(timeStr);
        return sb.toString();
    }

    public static int getRealMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int i = calendar.get(Calendar.MONTH);
        return i+1;
    }

    public static int getRealDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int i = calendar.get(Calendar.DAY_OF_MONTH);
        return i;
    }

}
