package com.m1kes.expressscript.objects.custom;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CustomDate {

    private Calendar calendar;
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.US);

    public CustomDate(long time){
        calendar = Calendar.getInstance(TimeZone.getTimeZone("Africa/Harare"));
        calendar.setTimeInMillis(time);

        System.out.println("Current Time in this Object : " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(calendar.getTime()));
    }

    public CustomDate(){
        calendar = Calendar.getInstance(TimeZone.getTimeZone("Africa/Harare"));
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println("Current Time in this Object : " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(calendar.getTime()));
    }

    public String getFormattedTime(String pattern){
        return new SimpleDateFormat(pattern,Locale.US).format(calendar.getTime());
    }

    public long getLongTime() {
        return calendar.getTimeInMillis();
    }

    public Date getDate() {
        return calendar.getTime();
    }

    public String getShortTime(){
        return df.format(calendar.getTime());
    }
}
