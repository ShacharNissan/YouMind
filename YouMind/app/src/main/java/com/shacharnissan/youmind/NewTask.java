package com.shacharnissan.youmind;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewTask extends AppCompatActivity {
    private final String TagName = "YouMind-MainActivity";
    private final String DATE_FORMAT_REF = "hh:mm dd-MM-yyyy";

    // UI Components
    private EditText et_name;
    private EditText et_tododate; // https://www.tutorialsbuzz.com/2019/09/android-datepicker-dialog-styling-kotlin.html
    private EditText et_todotime;
    private RadioGroup rg_severity;
    private RadioButton rd_severity;
    private Button btnNewTask;

    // Service Vars
    private TasksService mService;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtask);

        initActivity();
        SetupActivity();
        //
        //TaskEntity task = new TaskEntity("Level1",TaskLevelsEnum.HARD);
        //
        //setView(task);
    }

    private void SetupActivity() {
        // Service Init
        this.serviceIntent = new Intent(this, TasksService.class);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);


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

        btnNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTaskButtonClicked();
            }
        });
    }

    private void NewTaskButtonClicked() {
        try {
            TaskLevelsEnum taskSeverity = null;
            switch (rg_severity.getCheckedRadioButtonId()) {
                case R.id.task_rb_easy:
                    taskSeverity = TaskLevelsEnum.EASY;
                    break;
                case R.id.task_rb_medium:
                    taskSeverity = TaskLevelsEnum.MEDIUM;
                    break;
                case R.id.task_rb_hard:
                    taskSeverity = TaskLevelsEnum.HARD;
                    break;
            }
            String taskName = this.et_name.getText().toString();
            String taskDate = this.et_tododate.getText().toString();
            String taskTime = this.et_todotime.getText().toString();
            Date todoDate = new SimpleDateFormat(DATE_FORMAT_REF).parse(taskTime + " " + taskDate);
            TaskEntity taskEntity = new TaskEntity(taskName, taskSeverity, todoDate);
            this.mService.addTask(taskEntity);

        } catch (Exception ex){
            Log.e(TagName, "NewTaskButtonClicked: Failed to save Task");
        }
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

    private void initActivity(){
        this.et_name = findViewById(R.id.task_et_name);
        this.et_tododate = findViewById(R.id.task_et_date);
        this.et_todotime = findViewById(R.id.task_et_time);
        this.rg_severity = findViewById(R.id.task_rg_severity);
        this.btnNewTask = findViewById(R.id.new_task_btn);
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

    private ServiceConnection serviceConnection = new ServiceConnection() {

        // ComonentName = The Service name that we are binded to (TasksService)
        // IBinder = The link between the Client and the server (view to model)
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TagName, "onServiceConnected: connected to service");
            TasksService.MyBinder binder = (TasksService.MyBinder) service;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TagName, "onServiceDisconnected: disconnected from service");
            mService = null;
        }
    };
}