package com.shacharnissan.youmind;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.shacharnissan.youmind.storage.LocalJson;
import java.util.ArrayList;

public class TasksService extends Service {
    private final String TagName = "YouMind-TasksService";

    private static TasksService localService;
    private LocalJson localsave;
    private ArrayList<TaskEntity> tasks;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TagName, "Starting onStartCommand Function.");
        localService = this;
        this.localsave = new LocalJson();
        this.tasks = this.localsave.loadData(getApplicationContext().getFilesDir());

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TagName, "Starting onBind Function.");
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        Log.d(TagName, "Starting onDestroy Function.");
        this.localsave.saveData(getApplicationContext().getFilesDir(), this.tasks);

        super.onDestroy();
    }

    public static ArrayList<TaskEntity> getTasks(){
//        ArrayList<TaskEntity> tasks = new ArrayList<>();
//        tasks.add(new TaskEntity("Task #1",TaskLevelsEnum.HARD));
//        tasks.add(new TaskEntity("Task #2",TaskLevelsEnum.MEDIUM));
//        tasks.add(new TaskEntity("Task #3"));
//
//        return tasks;
        return localService != null ? localService.tasks : null;
    }
}