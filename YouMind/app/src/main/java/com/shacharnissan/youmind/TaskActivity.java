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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.shacharnissan.youmind.data.TaskEntity;
import com.shacharnissan.youmind.data.TaskSeverityEnum;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity {
    private final String TagName = "YouMind-NewTask";

    // UI Components
    private EditText et_name;
    private EditText et_todo_date; // https://www.tutorialsbuzz.com/2019/09/android-datepicker-dialog-styling-kotlin.html
    private EditText et_todo_time;
    private RadioGroup rg_severity;
    private Button btnNewTask;
    private Button btnEditTask;
    private Button btnDeleteTask;
    private CheckBox cbIsActive;

    // Service Vars
    private TasksService mService;
    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TagName, "Starting onCreate Function.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        initActivity();
        SetupActivity();
    }

    @Override
    protected void onStart() {
        Log.d(TagName, "Starting onStart Function.");
        super.onStart();

        // Service Init
        if (mService == null) {
            Intent serviceIntent = new Intent(this, TasksService.class);
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        Log.d(TagName, "Starting onStop Function.");
        unbindService(serviceConnection);
        super.onStop();
    }

    private void initActivity(){
        this.et_name = findViewById(R.id.task_et_name);
        this.et_todo_date = findViewById(R.id.task_et_date);
        this.et_todo_time = findViewById(R.id.task_et_time);
        this.rg_severity = findViewById(R.id.task_rg_severity);
        this.btnNewTask = findViewById(R.id.new_task_add);
        this.btnEditTask = findViewById(R.id.new_task_edit);
        this.btnDeleteTask = findViewById(R.id.new_task_delete);
        this.cbIsActive = findViewById(R.id.new_task_active_cb);
    }

    private void SetupActivity() {
        Intent intent = getIntent();
        taskId = intent.getStringExtra( getResources().getString(R.string.task_tag));

        // set Listeners
        et_todo_date.setOnClickListener(v -> setDateClickListener());
        et_todo_time.setOnClickListener(v -> setTimeClickListener());
        btnNewTask.setOnClickListener(v -> NewTaskButtonClicked());
        btnEditTask.setOnClickListener(v -> editButtonClicked());
        btnDeleteTask.setOnClickListener(v -> deleteButtonClicked());
    }

    private void setTimeClickListener() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        // Theme_Panel || Theme_Light_Panel || Theme_Dialog || theme_holo_light
        TimePickerDialog mTimePicker = new TimePickerDialog(
                TaskActivity.this,
                R.style.Time_Picker_style,
                (timePicker, selectedHour, selectedMinute) ->
                        et_todo_time.setText(String.format(Locale.getDefault(),
                                "%d:%d:00",
                                selectedHour,
                                selectedMinute)), hour, minute, true); //Yes 24 hour time
        //mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mTimePicker.setTitle("Select a Time");
        mTimePicker.show();
    }

    private void setDateClickListener() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                TaskActivity.this, (view, year1, month1, dayOfMonth) -> {
                    Date date = TaskUtills.getDateFromDatePicker(view);
                    et_todo_date.setText(TaskUtills.get_date_string(date));
                },
                year,month,day);
        datePickerDialog.show();
    }

    private void deleteButtonClicked() {
        mService.deleteTask(taskId);
        Toast.makeText(this, "Task Deleted.", Toast.LENGTH_SHORT).show();
        returnToMainMenu();
    }

    private void editButtonClicked() {
        TaskEntity task = getTaskFromView();
        task.setId(taskId);
        mService.updateTask(task);
        Toast.makeText(this, "Updated Task.", Toast.LENGTH_SHORT).show();
        returnToMainMenu();
    }

    private void NewTaskButtonClicked() {
        try {
            TaskEntity taskEntity = getTaskFromView();
            mService.addTask(taskEntity);
            Log.d(TagName, "New Task Created.");
            Toast.makeText(this, "New Task Created.", Toast.LENGTH_SHORT).show();
            returnToMainMenu();
        } catch (Exception ex){
            Log.e(TagName, "NewTaskButtonClicked: Failed to save Task");
        }
    }

    private void setView() {
        if (taskId.equals(getResources().getString(R.string.not_task_id))) {
            setNewTaskView();
            return;
        }
        setEditTaskView();
        TaskEntity task = mService.getTask(taskId);

        this.et_name.setText(task.getName());
        String[] todo_time = TaskUtills.get_date_as_string(task.getTodoDate()).split(" ");
        this.et_todo_date.setText(todo_time[1]);
        this.et_todo_time.setText(todo_time[0]);
        RadioButton rd_severity;
        switch (task.getSeverity()){
            case EASY:
                rd_severity = findViewById(R.id.task_rb_easy);
                break;
            case MEDIUM:
                rd_severity = findViewById(R.id.task_rb_medium);
                break;
            case HARD:
                rd_severity = findViewById(R.id.task_rb_hard);
                break;
            default:
                rd_severity = null;
        }
        if (rd_severity != null){
            rd_severity.setChecked(true);
        }

        this.cbIsActive.setChecked(task.isActive());
    }

    private void setEditTaskView() {
        btnEditTask.setVisibility(View.VISIBLE);
        btnEditTask.setClickable(true);
        btnDeleteTask.setVisibility(View.VISIBLE);
        btnDeleteTask.setClickable(true);
        btnNewTask.setVisibility(View.INVISIBLE);
        btnNewTask.setClickable(false);
    }

    private void setNewTaskView() {
        btnEditTask.setVisibility(View.INVISIBLE);
        btnEditTask.setClickable(false);
        btnDeleteTask.setVisibility(View.INVISIBLE);
        btnDeleteTask.setClickable(false);
        btnNewTask.setVisibility(View.VISIBLE);
        btnNewTask.setClickable(true);
    }

    private TaskEntity getTaskFromView(){
        String taskName = this.et_name.getText().toString();
        Date todoDate = getDateFromView();
        TaskSeverityEnum taskSeverity = getSeverityFromView();
        boolean isActive = cbIsActive.isChecked();
        return new TaskEntity(taskName, taskSeverity, todoDate, isActive);
    }

    private Date getDateFromView(){
        String taskDate = this.et_todo_date.getText().toString();
        String taskTime = this.et_todo_time.getText().toString();
        return TaskUtills.get_string_as_date(taskTime + " " + taskDate);
    }

    @SuppressLint("NonConstantResourceId")
    private TaskSeverityEnum getSeverityFromView(){
        TaskSeverityEnum taskSeverity = null;
        switch (rg_severity.getCheckedRadioButtonId()) {
            case R.id.task_rb_easy:
                taskSeverity = TaskSeverityEnum.EASY;
                break;
            case R.id.task_rb_medium:
                taskSeverity = TaskSeverityEnum.MEDIUM;
                break;
            case R.id.task_rb_hard:
                taskSeverity = TaskSeverityEnum.HARD;
                break;
        }
        return taskSeverity;
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TagName, "onServiceConnected: connected to service");
            TasksService.MyBinder binder = (TasksService.MyBinder) service;
            mService = binder.getService();
            setView();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TagName, "onServiceDisconnected: disconnected from service");
            mService = null;
        }
    };

    private void returnToMainMenu(){
        this.finish();
    }
}