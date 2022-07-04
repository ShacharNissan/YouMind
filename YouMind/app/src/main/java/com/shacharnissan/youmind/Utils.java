package com.shacharnissan.youmind;

import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
    // Entity Dao Ref
    public static final String DAO_STRING_REF = "dao_number";
    public static final String ID_STRING_REF = "id";
    public static final String NAME_STRING_REF = "name";
    public static final String CREATE_DATE_STRING_REF = "create_date";

    // Task Dao Ref
    public static final String LEVEL_STRING_REF = "level";
    public static final String TODO_DATE_STRING_REF = "todo_date";
    public static final String IS_ACTIVE_STRING_REF = "isActive";

    // Note Dao Ref
    public static final String VALUE_STRING_REF = "value";

    public static final String DATE_FULL_FORMAT_REF = "HH:mm:ss dd-MM-yyyy";
    public static final String DATE_ONLY_FORMAT_REF = "dd-MM-yyyy";
//    public static final String DATE_TIME_FORMAT_REF = "HH:mm:ss";

    public static String get_date_as_string(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FULL_FORMAT_REF, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static Date get_string_as_date(String date) {
        try {
            return new SimpleDateFormat(DATE_FULL_FORMAT_REF, Locale.getDefault()).parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String get_date_string(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_ONLY_FORMAT_REF, Locale.getDefault());
        return sdf.format(date);
    }

//    public static String get_time_string(Date date){
//        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT_REF, Locale.getDefault());
//        return sdf.format(date);
//    }

    public static Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    public static String generateID(String prefix) {
        String currentTimeStr = get_date_as_string(new Date());
        currentTimeStr = currentTimeStr.replace(" :-", "");
        String uuid = java.util.UUID.randomUUID().toString().replace("-","").substring(8);
        return prefix + "_" + currentTimeStr + "_" + uuid;
    }
}
