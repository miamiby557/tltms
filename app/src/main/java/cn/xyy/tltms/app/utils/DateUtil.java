package cn.xyy.tltms.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/7/20.
 */

public class DateUtil {

    public static long GET_LOCATION_TIME=10000;//毫秒
    public static int POST_LOCATION_TIME=10000;//毫秒

    public static String getDate(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(Constant.DATE_FORMAT, Locale.CHINA).format(date);
    }

    public static String getTime(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(Constant.TIME_FORMAT, Locale.CHINA).format(date);
    }

    public static String getDateTime(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(Constant.DATE_TIME_FORMAT, Locale.CHINA).format(date);
    }

    public static Date MillsecondsToTime(long millseconds) {
        return new Date(millseconds);
    }

    public static long getDate(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATE_FORMAT, Locale.CHINA);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr.trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static Date strToDate(String strDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATE_FORMAT, Locale.CHINA);
        Date date = null;
        try {
            date = simpleDateFormat.parse(strDate.trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long getDateTime(String dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATE_TIME_FORMAT, Locale.CHINA);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateTime.trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static Date strToDateTime(String dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATE_TIME_FORMAT, Locale.CHINA);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateTime.trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String nowDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_TIME_FORMAT, Locale.CHINA);
        Date date = new Date();
        return dateFormat.format(date);
    }



    public static String getDate(long millseconds) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTimeInMillis(millseconds);
        int year = calendar.get(Calendar.YEAR);

        int month = calendar.get(Calendar.MONTH) + 1;
        String mToMonth = null;
        if (String.valueOf(month).length() == 1) {
            mToMonth = "0" + month;
        } else {
            mToMonth = String.valueOf(month);
        }

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dToDay = null;
        if (String.valueOf(day).length() == 1) {
            dToDay = "0" + day;
        } else {
            dToDay = String.valueOf(day);
        }


        return year + "-" + mToMonth + "-" + dToDay;
    }

    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     */
    public static int daysBetween(Date smdate,Date bdate)
    {
        if(smdate==null){
            return 0;
        }
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            smdate=sdf.parse(sdf.format(smdate));
            bdate=sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days=(time2-time1)/(1000*3600*24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 31;
    }


    public static String getDateTime(long millseconds) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTimeInMillis(millseconds);
        int year = calendar.get(Calendar.YEAR);

        int month = calendar.get(Calendar.MONTH) + 1;
        String mToMonth = null;
        if (String.valueOf(month).length() == 1) {
            mToMonth = "0" + month;
        } else {
            mToMonth = String.valueOf(month);
        }

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dToDay = null;
        if (String.valueOf(day).length() == 1) {
            dToDay = "0" + day;
        } else {
            dToDay = String.valueOf(day);
        }

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String hToHour = null;
        if (String.valueOf(hour).length() == 1) {
            hToHour = "0" + hour;
        } else {
            hToHour = String.valueOf(hour);
        }

        int minute = calendar.get(Calendar.MINUTE);
        String mToMinute = null;
        if (String.valueOf(minute).length() == 1) {
            mToMinute = "0" + minute;
        } else {
            mToMinute = String.valueOf(minute);
        }

        int second = calendar.get(Calendar.SECOND);
        String sToSecond = null;
        if (String.valueOf(second).length() == 1) {
            sToSecond = "0" + second;
        } else {
            sToSecond = String.valueOf(second);
        }
        return year + "-" + mToMonth + "-" + dToDay + " " + hToHour + ":" + mToMinute + ":" + sToSecond;
    }
}
