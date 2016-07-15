package com.utils.datahelper;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class TimeConversion {

    public static String getHourMinsData(long timeMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);

        System.out.println(timeMillis + " = " + formatter.format(calendar.getTime()));
        return formatter.format(calendar.getTime());
    }

    // 00:00 --> 24:00
    public static String getHourMinsDataEnd(long timeMillis) {
        String timeStr = getHourMinsData(timeMillis);
        if (timeStr.equals("00:00")) {
            return "24:00";
        }
        return timeStr;
    }

    public static String getHourMinSecondsData(long timeMillis) {
        long hours = timeMillis/1000l/3600l;
        SimpleDateFormat formatter = new SimpleDateFormat(":mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return hours + formatter.format(new Date(timeMillis));
    }

    public static String getData(long timeMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-HH:mm");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);

        System.out.println(timeMillis + " = " + formatter.format(calendar.getTime()));
        return formatter.format(calendar.getTime());
    }

    public static String getFullDate(long timeMillis){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);

        System.out.println(timeMillis + " = " + formatter.format(calendar.getTime()));
        return formatter.format(calendar.getTime());
    }

    public static String getYearsMonthsData(long timeMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);

        System.out.println(timeMillis + " = " + formatter.format(calendar.getTime()));
        return formatter.format(calendar.getTime());
    }

    public static String getMinutesData(long timeMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);

        System.out.println(timeMillis + " = " + formatter.format(calendar.getTime()));
        return formatter.format(calendar.getTime());
    }

    public static String getStandardData(long timeMinutes) {
        StringBuffer sb = new StringBuffer();
        long minute = timeMinutes;
        long day = minute/60/24;// 天
        long hour = minute/60%24;// 小时
        minute = minute - day*24*60 - hour * 60;
        if (day > 0) {
            sb.append(day + "天");
        }
        if (hour > 0) {
            sb.append(hour + "小时");
        }
        if (minute > 0) {
            sb.append(minute + "分钟");
        }
        return sb.toString();
    }

    public static int getDurationByStart(String durationStart, int duration) {
        Date date = new Date();
        if (TextUtils.isEmpty(durationStart)) {
            return duration;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try {
            date = formatter.parse(durationStart);
            long current = System.currentTimeMillis();
            long seconds = date.getTime() - current + duration*1000;
            if (seconds > 0) {
                seconds = seconds/1000;
                return (int) seconds;
            }
        } catch (Exception e) {
            return duration;
        }
        return -1;
    }

    public static int getDurationByEnd(String durationStr) {
        if (TextUtils.isEmpty(durationStr)) {
            return 0;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date();
        try {
            date = formatter.parse(durationStr);
            long current = System.currentTimeMillis();
            long seconds = date.getTime() - current;
            if (seconds > 0) {
                seconds = seconds/1000;
                return (int) seconds;
            }
        } catch (Exception e) {

        }
        return 0;
    }

    public static long getDurationWithGMT(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return System.currentTimeMillis();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date();
        try{
            date = formatter.parse(dateStr);
            return date.getTime();
        }catch (Exception e) {

        }
        return System.currentTimeMillis();
    }

    public static Date getDate(String strDate) {
        Date date = null;
        if (strDate!= null) {
            Calendar startTime = Calendar.getInstance();
            int year = startTime.get(Calendar.YEAR) - 20;
            // 这里初始化时间，然后设置年份。只以年份为基准，不看时间
            startTime.clear();
            startTime.set(Calendar.YEAR, year);

            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-HH:mm");
            formatter.setLenient(false);
            formatter.set2DigitYearStart(startTime.getTime());

            try {
                date = formatter.parse(strDate);
            }
            catch (Exception e) {
            }
        }
        return date;
    }
}
