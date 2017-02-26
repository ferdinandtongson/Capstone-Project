package me.makeachoice.gymratpta.utilities;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static me.makeachoice.gymratpta.R.string.date;

/**
 * Created by Usuario on 2/16/2017.
 */

public class DateTimeHelper {

    public static String getMonthDayFromToday(int addDays){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");

        //get today's date
        cal.getTime();

        //add number of days to date
        cal.add(Calendar.DATE, addDays);

        return dateFormat.format(cal.getTime());
    }

    public static String getDatestamp(int addDays){
        Log.d("Choice", "DateTimeHelper.getDateStamp");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //get today's date
        cal.getTime();

        //add number of days to date
        cal.add(Calendar.DATE, addDays);

        return dateFormat.format(cal.getTime());
    }

    public static long getDatestamp(String strDate){
       SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyy");
        try {
            Date date = (Date)dateFormat.parse(strDate);

            return date.getTime();
        } catch (ParseException e){
            // Exception handling goes here
        }
        return -1;
    }

    public static String convertDatestampToDate(String datestamp){
        SimpleDateFormat stampFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyy");

        try {
            Date date = (Date)stampFormat.parse(datestamp);


            return dateFormat.format(date.getTime());
        } catch (ParseException e){
            // Exception handling goes here
        }
        return "";
    }

    public static long getTimestamp(String strDate, String strTime){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyy hh:mm aa");
        try {
            Date date = (Date)dateFormat.parse(strDate + " " + strTime);

            return date.getTime();
        } catch (ParseException e){
            // Exception handling goes here
        }
        return -1;
    }

    public static String getToday(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
        return dateFormat.format(cal.getTime());
    }

    public static String getCurrentTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(cal.getTime());
    }

    public static int getCurrentYear(){
        Calendar cal = Calendar.getInstance();

        return cal.get(Calendar.YEAR);
    }

    public static int getCurrentMonth(){
        Calendar cal = Calendar.getInstance();

        return cal.get(Calendar.MONTH);
    }

    public static int getCurrentDayOfMonth(){
        Calendar cal = Calendar.getInstance();

        return cal.get(Calendar.DAY_OF_MONTH);
    }


    public static String getDate(int year, int month, int dayOfMonth){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");

        return dateFormat.format(cal.getTime());
    }

    public static int getCurrentHour(){
        Calendar cal = Calendar.getInstance();

        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentMinute(){
        Calendar cal = Calendar.getInstance();

        return cal.get(Calendar.MINUTE);
    }

    public static String getTime(int hour, int minute){

        String strTime = pad(hour) + ":" + pad(minute);
        return strTime;
    }

    public static String convert24Hour(String strTime){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        try {
            SimpleDateFormat convertFormat = new SimpleDateFormat("hh:mm aa");
            Date parseDate = (Date)sdf.parse(strTime);
            return convertFormat.format(parseDate);
        } catch (ParseException e){
            // Exception handling goes here
        }

        return strTime;
    }


    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public static int isTime1AfterTime2(String time1, String time2){
        String pattern = "HH:mm aa";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date1 = sdf.parse(time1);
            Date date2 = sdf.parse(time2);

            //return 1 if date1 is after date2, return -1 if date1 is before date2
            return date1.compareTo(date2);
        } catch (ParseException e){
            // Exception handling goes here
        }

        return -1;
    }

}
