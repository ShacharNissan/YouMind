package com.shacharnissan.youmind;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.shacharnissan.youmind.data.TaskEntity;
import com.shacharnissan.youmind.storage.EntityDao;
import com.shacharnissan.youmind.storage.LocalJson;
import com.shacharnissan.youmind.storage.NoteDao;
import com.shacharnissan.youmind.storage.TaskDao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TasksService extends Service {
    private final String TagName = "YouMind-TasksService";
    private LocalJson local_save_instance;
    private IBinder mBinder = new MyBinder();

    public TaskDao taskDao;
    public NoteDao noteDao;

    public class MyBinder extends Binder {
        TasksService getService() {
            return TasksService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TagName, "Starting onStartCommand Function.");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.d(TagName, "Starting onCreate Function.");
        local_save_instance = new LocalJson();
        taskDao = new TaskDao();
        noteDao = new NoteDao();
        loadDataFromMemory();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TagName, "Starting onDestroy Function.");
        Toast.makeText(this, "Service Closed", Toast.LENGTH_SHORT).show();
        saveDataToMemory();

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TagName, "Starting onBind Function.");
        return mBinder;
    }

    private void loadDataFromMemory() {
        if (local_save_instance == null) {
            Log.e(TagName, "Could not locate localSave Instance, data not loaded");
            return;
        }

        try {
            JSONObject jo = this.local_save_instance.loadData(getApplicationContext().getFilesDir());
            int index = 1;
            for (EntityDao dao : getAllDao()) {
                dao.jsonArrayToEntities(jo.getJSONArray(TaskUtills.DAO_STRING_REF + index));
                index++;
            }
        } catch (Exception ex) {
            Log.e(TagName, "Error loading data from local Storage - " + ex.getMessage());
        }

        Log.d(TagName, "Finished loading data from memory");
    }

    private void saveDataToMemory() {
        if (local_save_instance == null) {
            Log.e(TagName, "Could not locate localSave Instance, data not saved!");
            return;
        }

        try {
            JSONObject jo = new JSONObject();
            int index = 1;
            for (EntityDao dao : getAllDao()) {
                JSONArray tasksJson = dao.entitiesToJsonArray();
                jo.put(TaskUtills.DAO_STRING_REF + index, tasksJson);
                index++;
            }
            this.local_save_instance.saveData(getApplicationContext().getFilesDir(), jo);
            Log.d(TagName, "Finished saving data to memory");
        } catch (Exception ignored) {

        }
    }

    private ArrayList<EntityDao> getAllDao(){
        ArrayList<EntityDao> dao = new ArrayList<EntityDao>();
        dao.add(taskDao);
        dao.add(noteDao);

        return dao;
    }
}