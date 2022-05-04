package com.shacharnissan.youmind;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewTask extends AppCompatActivity {
    private EditText et_name;
    private EditText et_tododate; // https://www.tutorialsbuzz.com/2019/09/android-datepicker-dialog-styling-kotlin.html
    private EditText et_todotime;
    private RadioGroup rg_severity;
    private RadioButton rd_severity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtask);

        setInit();
        //
        TaskEntity task = new TaskEntity("Level1",TaskLevelsEnum.HARD);
        //
        setView(task);
    }

    private void setView(TaskEntity task) {
        this.et_name.setText(task.getName());
        this.et_tododate.setText(task.getTodoDate().toString());
        switch (task.getLevel()){
            case EASY:
                this.rd_severity = findViewById(R.id.task_rb_easy);
                break;
            case MEDIUM:
                this.rd_severity = findViewById(R.id.task_rb_medium);
                break;
            case HARD:
                this.rd_severity = findViewById(R.id.task_rb_hard);
                break;
            default:
                this.rd_severity = null;
        }
        if (this.rd_severity != null){
            this.rd_severity.setChecked(true);
        }
    }

    private void setInit(){
        this.et_name = findViewById(R.id.task_et_name);
        this.et_tododate = findViewById(R.id.task_et_date);
        this.et_todotime = findViewById(R.id.task_et_time);
        this.rg_severity = findViewById(R.id.task_rg_severity);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Date Picker
        et_tododate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        NewTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date date = getDateFromDatePicker(view);
                        set_et_date_Text(date);
                    }
                },
                        year,month,day);
                datePickerDialog.show();
            }
        });

        // Time Picker
        et_todotime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(NewTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        et_todotime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true); //Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

    }
    public static Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    public void set_et_date_Text(Date date){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.AppDateFormat));
        String dateString = sdf.format(date);

        et_tododate.setText(dateString); // set the date
    }
}