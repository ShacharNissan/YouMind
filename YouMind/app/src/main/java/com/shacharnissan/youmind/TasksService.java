package com.shacharnissan.youmind;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shacharnissan.youmind.storage.LocalJson;
import java.util.ArrayList;

public class TasksService extends Service {
    private final String TagName = "YouMind-TasksService";

    //private static TasksService localService;
    private LocalJson localsave;
    private ArrayList<TaskEntity> tasks;
    private IBinder mBinder = new MyBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TagName, "Starting onCreate Function.");
        this.localsave = new LocalJson();
        this.tasks = new ArrayList<TaskEntity>();
        loadDataFromMemory();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TagName, "Starting onBind Function.");
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    public class MyBinder extends Binder {
        TasksService getService(){
            return TasksService.this;
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TagName, "Starting onDestroy Function.");
        this.saveDataToMemory();

        super.onDestroy();
    }

    public ArrayList<TaskEntity> getTasks(){
//        ArrayList<TaskEntity> tasks = new ArrayList<>();
//        tasks.add(new TaskEntity("Task #1",TaskLevelsEnum.HARD));
//        tasks.add(new TaskEntity("Task #2",TaskLevelsEnum.MEDIUM));
//        tasks.add(new TaskEntity("Task #3"));
//
//        return tasks;
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
}