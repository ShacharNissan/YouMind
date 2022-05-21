package com.shacharnissan.youmind.storage;

import com.shacharnissan.youmind.TaskEntity;
import com.shacharnissan.youmind.TaskLevelsEnum;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

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

    public ArrayList<TaskEntity> loadData(String fileURL) {
        ArrayList<TaskEntity> tasks = new ArrayList<TaskEntity>();
        //File file = new File();
        try {
            JSONObject obj = new JSONObject(fileURL + "/" + FILENAME);
            int number_of_tasks = obj.getInt(COUNTER_STRING_REF);
            if (number_of_tasks < 0)
                return tasks;

            JSONArray jarray = obj.getJSONArray(TASKS_STRING_REF);

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject task = jarray.getJSONObject(i);

                String id = task.getString(ID_STRING_REF);
                String name = task.getString(NAME_STRING_REF);
                String level = task.getString(LEVEL_STRING_REF);
                Date createDate = (Date) task.get(CREATE_DATE_STRING_REF);
                Date todoDate = (Date) task.get(TODO_DATE_STRING_REF);
                tasks.add(new TaskEntity(id, name, TaskLevelsEnum.valueOf(level), createDate, todoDate));
            }
        } catch (Exception ignored) {

        }
        return tasks;
    }

    public void saveData(String fileURL, ArrayList<TaskEntity> tasks) {
        try {
            JSONObject jo = new JSONObject();
            jo.put(COUNTER_STRING_REF, tasks.size());
            JSONArray tasksJson = new JSONArray();
            for (TaskEntity task : tasks) {
                tasksJson.put(getAsJsonObject(task));
            }
            jo.put(TASKS_STRING_REF, tasksJson);
        } catch (Exception ignored) {

        }
    }
}
