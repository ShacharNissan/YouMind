package com.shacharnissan.youmind;

import android.annotation.SuppressLint;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class TaskUtills {
    public static final String DATE_FULL_FORMAT_REF = "HH:mm:ss dd-MM-yyyy";
    public static final String DATE_ONLY_FORMAT_REF = "dd-MM-yyyy";
    public static final String DATE_TIME_FORMAT_REF = "HH:mm:ss";

    public static String get_date_as_string(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FULL_FORMAT_REF);
        return dateFormat.format(date);
    }

    public static Date get_string_as_date(String date) {
        try {
            return new SimpleDateFormat(DATE_FULL_FORMAT_REF).parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String get_date_string(Date date){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_ONLY_FORMAT_REF);
        return sdf.format(date);
    }

    public static String get_time_string(Date date){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT_REF);
        return sdf.format(date);
    }

    public static Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    public static class TaskComparator implements Comparator<TaskEntity> {

        @Override
        public int compare(TaskEntity o1, TaskEntity o2) {
            return o1.getTodoDate().compareTo(o2.getTodoDate());
        }

        @Override
        public boolean equals(Object obj) {
            return this.equals(obj);
        }
    }
}
