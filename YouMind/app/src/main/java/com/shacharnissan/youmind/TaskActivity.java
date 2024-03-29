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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.shacharnissan.youmind.data.TaskEntity;
import com.shacharnissan.youmind.data.TaskSeverityEnum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity {
    private final String TagName = "YouMind-TaskActivity";

    // UI Components
    private EditText et_name;
    private EditText et_todo_date; // https://www.tutorialsbuzz.com/2019/09/android-datepicker-dialog-styling-kotlin.html
    private EditText et_todo_time;
    private EditText et_labels;
    private AutoCompleteTextView et_auto_labels;
    private RadioGroup rg_severity;
    private Button btnNewTask;
    private Button btnEditTask;
    private Button btnDeleteTask;
    private Button btnNewLabel;
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
        mService.saveDataToMemory();
        unbindService(serviceConnection);
        super.onStop();
    }

    private void initActivity(){
        this.et_name = findViewById(R.id.task_et_name);
        this.et_todo_date = findViewById(R.id.task_et_date);
        this.et_todo_time = findViewById(R.id.task_et_time);
        this.et_labels = findViewById(R.id.et_task_labels);
        this.et_auto_labels = findViewById(R.id.et_auto_task_label);
        this.rg_severity = findViewById(R.id.task_rg_severity);
        this.btnNewTask = findViewById(R.id.new_task_add);
        this.btnEditTask = findViewById(R.id.new_task_edit);
        this.btnDeleteTask = findViewById(R.id.new_task_delete);
        this.btnNewLabel = findViewById(R.id.btn_task_labels);
        this.cbIsActive = findViewById(R.id.new_task_active_cb);
    }

    private void SetupActivity() {
        Intent intent = getIntent();
        taskId = intent.getStringExtra( getResources().getString(R.string.task_id_tag));

        // set Listeners
        et_todo_date.setOnClickListener(v -> setDateClickListener());
        et_todo_time.setOnClickListener(v -> setTimeClickListener());
        btnNewTask.setOnClickListener(v -> newButtonClicked());
        btnEditTask.setOnClickListener(v -> editButtonClicked());
        btnDeleteTask.setOnClickListener(v -> deleteButtonClicked());
        btnNewLabel.setOnClickListener(v-> newLabelButtonClicked());
    }

    private void newLabelButtonClicked() {
        et_labels.setText(String.format("%s %s", et_labels.getText().toString(), et_auto_labels.getText().toString()));
        et_auto_labels.setText("");
    }

    private void setLabelsAutoFill() {
        // Set labels auto fill
        ArrayList<String> labels = mService.getAllLabels();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, labels);
        et_auto_labels.setThreshold(1);
        et_auto_labels.setAdapter(adapter);
        et_auto_labels.setOnItemClickListener((parent, view, position, id) -> et_auto_labels.setText(adapter.getItem(position)));
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
                    Date date = Utils.getDateFromDatePicker(view);
                    et_todo_date.setText(Utils.get_date_string(date));
                },
                year,month,day);
        datePickerDialog.show();
    }

    private void deleteButtonClicked() {
        mService.taskDao.delete(taskId);
        mService.saveDataToMemory();
        Toast.makeText(this, "Task Deleted.", Toast.LENGTH_SHORT).show();
        returnToMainMenu();
    }

    private void editButtonClicked() {
        TaskEntity task = getTaskFromView();
        task.setId(taskId);
        mService.taskDao.update(task);
        mService.saveDataToMemory();
        Toast.makeText(this, "Updated Task.", Toast.LENGTH_SHORT).show();
        returnToMainMenu();
    }

    private void newButtonClicked() {
        try {
            TaskEntity taskEntity = getTaskFromView();
            mService.taskDao.add(taskEntity);
            mService.saveDataToMemory();
            Log.i(TagName, "New Task Created.");
            Toast.makeText(this, "New Task Created.", Toast.LENGTH_SHORT).show();
            returnToMainMenu();
        } catch (Exception ex){
            Log.e(TagName, "NewTaskButtonClicked: Failed to save Task");
        }
    }

    private void setView() {
        if (taskId.equals(getResources().getString(R.string.no_entity_id))) {
            setNewTaskView();
            return;
        }
        setEditTaskView();
        mService.loadDataFromMemory();
        TaskEntity task = mService.taskDao.get(taskId);

        this.et_name.setText(task.getName());
        this.et_labels.setText(Utils.labels_array_to_string(task.getLabels()));
        String[] todo_time = Utils.get_date_as_string(task.getTodoDate()).split(" ");
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
        ArrayList<String> labels = Utils.text_to_labels_array(this.et_labels.getText().toString());
        Date todoDate = getDateFromView();
        TaskSeverityEnum taskSeverity = getSeverityFromView();
        boolean isActive = cbIsActive.isChecked();
        return new TaskEntity(taskName, labels, taskSeverity, todoDate, isActive);
    }

    private Date getDateFromView(){
        String taskDate = this.et_todo_date.getText().toString();
        String taskTime = this.et_todo_time.getText().toString();
        return Utils.get_string_as_date(taskTime + " " + taskDate);
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
            mService.loadDataFromMemory();
            setView();
            setLabelsAutoFill();
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