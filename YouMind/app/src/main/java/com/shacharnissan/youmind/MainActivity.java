package com.shacharnissan.youmind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shacharnissan.youmind.data.TaskEntity;
import com.shacharnissan.youmind.data.TaskSeverityEnum;

import java.util.ArrayList;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {
    private final String TagName = "YouMind-MainActivity";

    // UI Components
    private RecyclerView recyclerView;
    private RecyclerView.Adapter tableAdapterRV;
    private RecyclerView.LayoutManager tableLayoutManagerRV;

    private FloatingActionButton taskAddButton;
    private CheckBox cbHidden;

    // Service Vars
    private TasksService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TagName, "Starting OnCreate Function.");
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
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
    protected void onResume() {
        Log.d(TagName, "Starting onResume Function.");
        super.onResume();
        refreshViews();
    }

    private void SetupActivity() {
        cbHidden.setChecked(false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        taskAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskClicked(null);
            }
        });

        cbHidden.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                refreshViews();
            }
        });
    }

    private void initActivity() {
        this.recyclerView = findViewById(R.id.main_recyclerView);
        this.taskAddButton = findViewById(R.id.btn_tasks_add);
        this.cbHidden = findViewById(R.id.cb_hidden);

        this.tableLayoutManagerRV = new LinearLayoutManager(this);
    }

    @Override
    protected void onDestroy() {
        Log.d(TagName, "Starting onDestroy Function.");
        unbindService(serviceConnection);
        super.onDestroy();
    }

    private void taskClicked(TaskEntity task) {
        Log.d(TagName, "Starting taskClicked Function.");

        String taskId = task != null ? task.getId() : getResources().getString(R.string.not_task_id);

        Intent myIntent = new Intent(MainActivity.this, TaskActivity.class);
        myIntent.putExtra(getResources().getString(R.string.task_tag), taskId);
        startActivity(myIntent);
    }

    private void refreshViews(){
        Log.d(TagName, "Starting refreshViews Function.");

        if (mService == null)
            return;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                ArrayList<TaskEntity> tasks;
                if (cbHidden.isChecked())
                    tasks = mService.getAllTasks();
                else
                    tasks = mService.getActiveTasks();
                recyclerView.setHasFixedSize(true);

                tableAdapterRV = new TaskItemAdapter(tasks, MainActivity.this, new TaskItemAdapter.OnTasKClickListener() {
                    @Override
                    public void onItemClick(TaskEntity task) {
                        taskClicked(task);
                    }
                });

                recyclerView.setLayoutManager(tableLayoutManagerRV);
                recyclerView.setAdapter(tableAdapterRV);
            }
        }, 250);

    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TagName, "onServiceConnected: connected to service");
            TasksService.MyBinder binder = (TasksService.MyBinder) service;
            mService = binder.getService();
            refreshViews();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TagName, "onServiceDisconnected: disconnected from service");
            mService = null;
        }
    };
}