package me.makeachoice.gymratpta.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Usuario on 2/16/2017.
 */

public class DateTimeHelper {

    public static String getToday(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
        return dateFormat.format(cal.getTime());
    }

    public static String getCurrentTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
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

    public static String convert24Hour(int hour, int minute){
        String marker;
        String strHour;
        if(hour < 12){
            marker = "AM";
            strHour = pad(hour);
        }
        else{
            marker = "PM";
            hour = hour - 12;
            strHour = pad(hour);
        }

        String strTime = strHour + ":" + pad(minute) + " " + marker;
        return strTime;
    }


    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

}
