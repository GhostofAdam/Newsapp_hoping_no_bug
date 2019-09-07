package com.example.myapplication.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtility {
    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
    public Date getCurrent(){
        Date date = new Date();
        return date;
    }
    public String getDateString(Date date){
        return ft.format(date);
    }
    public Date backAWeek(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_WEEK,-1);
        return calendar.getTime();

    }
}
