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

import java.util.ArrayList;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private final String TagName = "YouMind-MainActivity";

    // UI Components
    private RecyclerView recyclerView;
    private RecyclerView.Adapter tableAdapterRV;
    private RecyclerView.LayoutManager tableLayoutManagerRV;

    private FloatingActionButton taskAddButton;

    // Service Vars
    private TasksService mService;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TagName, "Starting OnCreate Function.");
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        initActivity();
        SetupActivity();

    }

    private void SetupActivity() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        taskAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTaskClicked();
            }
        });

        // Service Init
        //mViewModel = ViewModelProviders
        this.serviceIntent = new Intent(this, TasksService.class);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        refreshViews();
    }

    private void initActivity() {
        this.recyclerView = findViewById(R.id.main_recyclerView);
        this.taskAddButton = findViewById(R.id.btn_tasks_add);

        this.tableLayoutManagerRV = new LinearLayoutManager(this);
    }

    @Override
    protected void onResume() {
        Log.d(TagName, "Starting onResume Function.");
        refreshViews();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TagName, "Starting onDestroy Function.");
        //stopService(this.serviceIntent);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d(TagName, "Starting onPause Function.");
        stopService(this.serviceIntent);
        super.onPause();
    }

    private void newTaskClicked() {
        Log.d(TagName, "Starting newTaskClicked Function.");
        // Move to Score Activity
        Intent myIntent = new Intent(MainActivity.this, NewTask.class);
        startActivity(myIntent);

        // myIntent.putExtra(getResources().getString(R.string.username_tag), "" + name);
    }

    private void refreshViews(){
        Log.d(TagName, "Starting refreshViews Function.");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                ArrayList<TaskEntity> tasks = mService.getTasks();
                recyclerView.setHasFixedSize(true);

                tableAdapterRV = new TaskItemAdapter(tasks, MainActivity.this);

                recyclerView.setLayoutManager(tableLayoutManagerRV);
                recyclerView.setAdapter(tableAdapterRV);
            }
        }, 250);

    }

    public ArrayList<TaskEntity> getTasks(){
        ArrayList<TaskEntity> tasks = new ArrayList<>();
        tasks.add(new TaskEntity("Level1",TaskLevelsEnum.HARD));
        tasks.add(new TaskEntity("Level2",TaskLevelsEnum.MEDIUM));
        tasks.add(new TaskEntity("Level3"));

        return tasks;
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