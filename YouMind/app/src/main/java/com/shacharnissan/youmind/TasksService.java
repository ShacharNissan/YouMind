package com.shacharnissan.youmind;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shacharnissan.youmind.storage.LocalJson;
import java.util.ArrayList;

public class TasksService extends Service {
    private final String TagName = "YouMind-TasksService";

    private static int totalInstances = 0;
    //private static TasksService localService;
    private LocalJson localsave;
    private ArrayList<TaskEntity> tasks;
    private IBinder mBinder = new MyBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TagName, "Starting onStartCommand Function.");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.d(TagName, "Starting onCreate Function.");
        this.localsave = new LocalJson();
        this.tasks = new ArrayList<TaskEntity>();
        loadDataFromMemory();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TagName, "Starting onBind Function.");
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TagName, "Starting onUnbind Function.");
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {
        TasksService getService(){
            return TasksService.this;
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TagName, "Starting onDestroy Function.");
        Toast.makeText(this, "Service Closed", Toast.LENGTH_SHORT).show();
        saveDataToMemory();

        super.onDestroy();
    }

    public ArrayList<TaskEntity> getTasks(){
        loadDataFromMemory();
        return tasks;
    }

    public void addTask(TaskEntity task){
        this.tasks.add(task);
        saveDataToMemory();
    }

    private void loadDataFromMemory() {
        if (localsave == null) {
            Log.e(TagName, "Could not locate localSave Instance, data not loaded");
            return;
        }

        this.tasks = this.localsave.loadData(getApplicationContext().getFilesDir());
        totalInstances = 0;
        for (TaskEntity task : tasks)
            task.setId(generateID());

        Log.d(TagName, "Finished loading data from memory");
    }

    private void saveDataToMemory() {
        if (localsave == null) {
            Log.e(TagName, "Could not locate localSave Instance, data not saved!");
            return;
        }

        this.localsave.saveData(getApplicationContext().getFilesDir(), this.tasks);
        Log.d(TagName, "Finished saving data to memory");
    }

    public static String generateID() {
        return "taskID" + totalInstances++;
    }

    public TaskEntity getTask(String taskId){
        for (TaskEntity task : tasks) {
            if (task.getId().equals(taskId))
                return task;
        }
        return null;
    }
}