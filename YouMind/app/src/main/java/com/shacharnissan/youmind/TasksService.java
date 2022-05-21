package com.shacharnissan.youmind;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class TasksService extends Service {
    private static TasksService localService;
    private ArrayList<TaskEntity> tasks;

    // this.getFilesDir().getAbsolutePath() +
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO: load all tasks from memory
        localService = this;

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static ArrayList<TaskEntity> getTasks(){
        ArrayList<TaskEntity> tasks = new ArrayList<>();
        tasks.add(new TaskEntity("Task #1",TaskLevelsEnum.HARD));
        tasks.add(new TaskEntity("Task #2",TaskLevelsEnum.MEDIUM));
        tasks.add(new TaskEntity("Task #3"));

        return tasks;
        //return localService != null ? localService.tasks : null;
    }
}