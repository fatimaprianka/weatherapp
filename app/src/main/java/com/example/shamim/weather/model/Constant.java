package com.example.shamim.weather.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Constant {
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
        public static class WeatherLocation{
       // public String units = "metric";//imperial
            public static  final String FARHENHITE="imperial";
            public static  final String CELCIUS="metric";
        public static double latitude;
        public static double longitude;
    }

    public static class DateTimeFormat{
        public static String unixToDay(long timeStamp) {
            java.util.Date dateTime = new java.util.Date((long)timeStamp*1000);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateTime);
            int dayInt = cal.get(Calendar.DAY_OF_WEEK);

            String dayStr = "";

            switch (dayInt) {
                case Calendar.SATURDAY:
                    dayStr = "Saturday";
                    break;
                case Calendar.SUNDAY:
                    dayStr = "Sunday";
                    break;
                case Calendar.MONDAY:
                    dayStr = "Monday";
                    break;
                case Calendar.TUESDAY:
                    dayStr = "Tuesday";
                    break;
                case Calendar.WEDNESDAY:
                    dayStr = "Wednesday";
                    break;
                case Calendar.THURSDAY:
                    dayStr = "Thursday";
                    break;
                case Calendar.FRIDAY:
                    dayStr = "Friday";
                    break;
            }

            return dayStr;
        }

        public static String unixToDate(long timestamp) {
            // convert seconds to milliseconds
            Date date = new java.util.Date(timestamp*1000L);
            // the format of your date
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d,yyyy", Locale.US);

            return sdf.format(date);
        }

        public static String getTimeFromUnix(long timestamp) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp * 1000L);

            Date d = calendar.getTime();

            return new SimpleDateFormat("hh:mm a",Locale.US).format(d);
        }
    }
}
