package com.utils.datahelper;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(new Date(timeMillis));
    }

    public static String getData(long timeMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

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

    public static int getDuration(String durationStr) {
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
}
