package com.shacharnissan.youmind.storage;

import com.shacharnissan.youmind.TaskEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class LocalJson {
    public final String FILENAME = "Tasks_data.json";

    public final String ID_STRING_REF = "id";
    public final String NAME_STRING_REF = "name";
    public final String LEVEL_STRING_REF = "level";
    public final String CREATE_DATE_STRING_REF = "create_date";
    public final String TODO_DATE_STRING_REF = "todo_date";

    public final String TASKS_STRING_REF = "tasks";
    public final String COUNTER_STRING_REF = "collection_size";

    public JSONObject getAsJsonObject(TaskEntity task) {
        JSONObject taskObject = null;
        try {
            taskObject = new JSONObject();
            taskObject.put(ID_STRING_REF, task.getID());
            taskObject.put(NAME_STRING_REF, task.getName());
            taskObject.put(LEVEL_STRING_REF, task.getLevel().toString());
            taskObject.put(CREATE_DATE_STRING_REF, task.getCreateDate());
            taskObject.put(TODO_DATE_STRING_REF, task.getTodoDate());
        } catch (Exception ex) {
            return null;
        }
        return taskObject;
    }

    public void loadData(String fileURL){
        File file = new File(fileURL +  "/" + FILENAME);
    }

    public void saveData(String fileURL, ArrayList<TaskEntity> tasks){
        try {
            JSONObject jo = new JSONObject();
            jo.put(COUNTER_STRING_REF, tasks.size());
            JSONArray tasksJson = new JSONArray();
            for (TaskEntity task : tasks) {
                tasksJson.put(getAsJsonObject(task));
            }
            jo.put(TASKS_STRING_REF, tasksJson);
        } catch (Exception ignored){

        }
    }
}
