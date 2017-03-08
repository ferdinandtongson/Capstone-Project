package me.makeachoice.gymratpta.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateTimeHelper {

    private final static String FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm";
    private final static String FORMAT_DATESTAMP = "yyyy-MM-dd";

    public static String getMonthDayFromToday(int addDays){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");

        //get today's date
        cal.getTime();

        //add number of days to date
        cal.add(Calendar.DATE, addDays);

        return dateFormat.format(cal.getTime());
    }

    public static String getDatestamp(int year, int month, int dayOfMonth){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth);

        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATESTAMP);

        return dateFormat.format(cal.getTime());
    }


    public static String getDatestamp(int addDays){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATESTAMP);

        //get today's date
        cal.getTime();

        //add number of days to date
        cal.add(Calendar.DATE, addDays);

        return dateFormat.format(cal.getTime());
    }

    public static String getDatestamp(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATESTAMP);

        return dateFormat.format(date);
    }

    public static String getDatestamp(Date date, int addDay){
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATESTAMP);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, addDay);

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
        SimpleDateFormat stampFormat = new SimpleDateFormat(FORMAT_DATESTAMP);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyy");

        try {
            Date date = (Date)stampFormat.parse(datestamp);

            return dateFormat.format(date.getTime());
        } catch (ParseException e){
            // Exception handling goes here
        }
        return "";
    }

    public static String convertDateToDatestamp(String strDate){
        SimpleDateFormat stampFormat = new SimpleDateFormat(FORMAT_DATESTAMP);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyy");

        try {
            Date date = (Date)dateFormat.parse(strDate);

            return stampFormat.format(date.getTime());
        } catch (ParseException e){
            // Exception handling goes here
        }
        return "";
    }

    public static String getTimestamp(String strDate, String strTime){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyy HH:mm");
        SimpleDateFormat stampFormat = new SimpleDateFormat(FORMAT_TIMESTAMP);
        try {
            Date date = (Date)dateFormat.parse(strDate + " " + strTime);

            return stampFormat.format(date.getTime());
        } catch (ParseException e){
            // Exception handling goes here
        }
        return strDate;
    }

    public static boolean hasAppointmentTimePassed(String timestamp){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat stampFormat = new SimpleDateFormat(FORMAT_TIMESTAMP);
        try {
            Date appointmentTime = (Date)stampFormat.parse(timestamp);
            Date currentTime = cal.getTime();

            //return 1 if appointmentTime in past, return -1 if appointmentTime in future
            //return 1 if currentTime is after appointmentTime, return -1 if currentTime is before appointmentTime
            int hasPassed = currentTime.compareTo(appointmentTime);
            if(hasPassed == 1){
                //has passed, appointment time is invalid
                return true;
            }
            else{
                //has not passed, appointment time is valid
                return false;
            }
        } catch (ParseException e){
            // Exception handling goes here
        }
        return true;
    }

    public static boolean hasDatePast(String datestamp){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat stampFormat = new SimpleDateFormat(FORMAT_DATESTAMP);
        try {
            Date appointmentDate = (Date)stampFormat.parse(datestamp);
            Date currentDate = cal.getTime();

            //if current date is after appointment true, invalid (past already)
            return currentDate.after(appointmentDate);

        } catch (ParseException e){
            // Exception handling goes here
        }
        return true;
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



    public static Date getStartOfWeek(int addWeek){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, addWeek);

        return calendar.getTime();
    }

    public static Date getEndOfWeek(int addWeek){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, addWeek);

        calendar.add(Calendar.DATE, 6);

        return calendar.getTime();
    }

    public static String convertWeekRange(Date startOfWeek, Date endOfWeek){
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");

        String startDateInStr = formatter.format(startOfWeek);
        String endDateInString = formatter.format(endOfWeek);

        return startDateInStr + " - " + endDateInString;
    }
}
