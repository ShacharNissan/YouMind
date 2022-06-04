package com.shacharnissan.youmind.storage;

import android.util.Log;

import com.shacharnissan.youmind.TaskEntity;
import com.shacharnissan.youmind.TaskLevelsEnum;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LocalJson {
    private final String TagName = "YouMind-LocalJson";
    public final String FILENAME = "Tasks_data.json";

    public final String ID_STRING_REF = "id";
    public final String NAME_STRING_REF = "name";
    public final String LEVEL_STRING_REF = "level";
    public final String CREATE_DATE_STRING_REF = "create_date";
    public final String TODO_DATE_STRING_REF = "todo_date";

    public final String TASKS_STRING_REF = "tasks";
    public final String COUNTER_STRING_REF = "collection_size";
    public final String DATE_FORMAT_REF = "hh:mm:ss dd-MM-yyyy";

    public JSONObject taskEntityToJsonObject(TaskEntity task) {
        Log.d(TagName, "Starting taskEntityToJsonObject Function.");
        JSONObject taskObject = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_REF);

            taskObject = new JSONObject();
            taskObject.put(ID_STRING_REF, task.getID());
            taskObject.put(NAME_STRING_REF, task.getName());
            taskObject.put(LEVEL_STRING_REF, task.getLevel().toString());
            taskObject.put(CREATE_DATE_STRING_REF, dateFormat.format(task.getCreateDate()));
            taskObject.put(TODO_DATE_STRING_REF, dateFormat.format(task.getTodoDate()));
        } catch (Exception ex) {
            Log.e(TagName, "Error converting TaskEntity to JsonObject - " + ex.getMessage());
        }
        return taskObject;
    }

    public TaskEntity jsonObjectToTaskEntity(JSONObject task) {
        Log.d(TagName, "Starting jsonObjectToTaskEntity Function.");
        try {
            String id = task.getString(ID_STRING_REF);
            String name = task.getString(NAME_STRING_REF);
            String level = task.getString(LEVEL_STRING_REF);
            String createDateStr = task.getString(CREATE_DATE_STRING_REF);
            String todoDateStr = task.getString(TODO_DATE_STRING_REF);
            Date createDate = new SimpleDateFormat(DATE_FORMAT_REF).parse(createDateStr);
            Date todoDate = new SimpleDateFormat(DATE_FORMAT_REF).parse(todoDateStr);
            return new TaskEntity(id, name, TaskLevelsEnum.valueOf(level), createDate, todoDate);
        } catch (Exception ex){
            Log.e(TagName, "Error converting JsonObject to TaskEntity - " + ex.getMessage());
        }
        return null;
    }

    public ArrayList<TaskEntity> loadData(File fileURL) {
        Log.d(TagName, "Starting loadData Function.");
        ArrayList<TaskEntity> tasks = new ArrayList<TaskEntity>();
        try {
            File readFile = new File(fileURL,FILENAME);
            FileInputStream fstream = new FileInputStream(readFile);
            byte[] content = new byte[(int) readFile.length()];
            fstream.read(content);
            JSONObject obj =new JSONObject(new String(content));
            int number_of_tasks = obj.getInt(COUNTER_STRING_REF);
            if (number_of_tasks < 0)
                return tasks;

            JSONArray jarray = obj.getJSONArray(TASKS_STRING_REF);

            for (int i = 0; i < jarray.length(); i++) {
                TaskEntity task = jsonObjectToTaskEntity(jarray.getJSONObject(i));
                if (task != null)
                    tasks.add(task);
            }
        } catch (Exception ex) {
            Log.e(TagName, "Error loading data from local Storage - " + ex.getMessage());
        }
        return tasks;
    }

    public void saveData(File fileURL, ArrayList<TaskEntity> tasks) {
        Log.d(TagName, "Starting saveData Function.");
        try {
            JSONObject jo = new JSONObject();
            jo.put(COUNTER_STRING_REF, tasks.size());
            JSONArray tasksJson = new JSONArray();
            for (TaskEntity task : tasks) {
                tasksJson.put(taskEntityToJsonObject(task));
            }
            jo.put(TASKS_STRING_REF, tasksJson);
            writeFileOnInternalStorage(fileURL, jo.toString());
        } catch (Exception ignored) {

        }
    }

    public void writeFileOnInternalStorage(File fileURL, String sBody){
        Log.d(TagName, "Starting writeFileOnInternalStorage Function.");
        try {
            FileOutputStream writer = new FileOutputStream(new File(fileURL, FILENAME));
            writer.write(sBody.getBytes());
            writer.close();
            Log.i(TagName, "File Saved to local storage");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
