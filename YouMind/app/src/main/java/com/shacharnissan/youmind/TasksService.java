package com.shacharnissan.youmind;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class TasksService extends Service {
    private ArrayList<TaskEntity> tasks;

    // this.getFilesDir().getAbsolutePath() +
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO: load all tasks from memory
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}