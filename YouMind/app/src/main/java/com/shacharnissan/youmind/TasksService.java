package com.shacharnissan.youmind;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.shacharnissan.youmind.data.TaskEntity;
import com.shacharnissan.youmind.storage.LocalJson;
import java.util.ArrayList;

public class TasksService extends Service {
    private final String TagName = "YouMind-TasksService";

    private static int totalInstances = 0;
    //private static TasksService localService;
    private LocalJson local_save_instance;
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
        this.local_save_instance = new LocalJson();
        this.tasks = new ArrayList<>();
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

    public void deleteTask(String taskId) {
        getTask(taskId).setActive(false);
        saveDataToMemory();
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

    public ArrayList<TaskEntity> getActiveTasks(){
        loadDataFromMemory();
        ArrayList<TaskEntity> actives = new ArrayList<>();
        for (int i = 0; i < tasks.size() ; i++) {
            if (tasks.get(i).isActive())
                actives.add(tasks.get(i));
        }
        return actives;
    }

    public ArrayList<TaskEntity> getAllTasks(){
        loadDataFromMemory();
        return tasks;
    }

    public void sortByTodo(ArrayList<TaskEntity> tasks){
        tasks.sort(new TaskUtills.TaskComparator());
    }

    public void addTask(TaskEntity task){
        this.tasks.add(task);
        task.setId(generateID());
        saveDataToMemory();
    }

    private void loadDataFromMemory() {
        if (local_save_instance == null) {
            Log.e(TagName, "Could not locate localSave Instance, data not loaded");
            return;
        }

        this.tasks = this.local_save_instance.loadData(getApplicationContext().getFilesDir());
        totalInstances = 0;
        for (TaskEntity task : tasks)
            task.setId(generateID());

        sortByTodo(tasks);
        Log.d(TagName, "Finished loading data from memory");
    }

    private void saveDataToMemory() {
        if (local_save_instance == null) {
            Log.e(TagName, "Could not locate localSave Instance, data not saved!");
            return;
        }

        this.local_save_instance.saveData(getApplicationContext().getFilesDir(), this.tasks);
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

    public void updateTask(TaskEntity task){
        try {
            TaskEntity te = getTask(task.getId());
            te.setName(task.getName());
            te.setTodoDate(task.getTodoDate());
            te.setSeverity(task.getSeverity());
            te.setActive(task.isActive());
            saveDataToMemory();
        }catch (Exception ex){
            Log.e(TagName, "updateTask: " + ex.getMessage());
            throw new RuntimeException("updateTask: " + ex.getMessage());
        }
    }
}