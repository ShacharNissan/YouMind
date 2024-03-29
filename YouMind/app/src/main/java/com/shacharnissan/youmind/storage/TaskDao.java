package com.shacharnissan.youmind.storage;

import android.util.Log;

import com.shacharnissan.youmind.Utils;
import com.shacharnissan.youmind.data.TaskEntity;
import com.shacharnissan.youmind.data.TaskSeverityEnum;
import com.shacharnissan.youmind.data.YouMindEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class TaskDao extends EntityDao {
    private final String TagName = "YouMind-TaskDao";
    private final String TagIdPrefix = "taskID";

    private ArrayList<TaskEntity> tasks;

    public TaskDao(){
        labels = new ArrayList<>();
        tasks = new ArrayList<>();
    }

    @Override
    public void add(YouMindEntity entity) {
        TaskEntity task = verifyTaskObject(entity);
        tasks.add(task);
        task.setId(Utils.generateID(TagIdPrefix));
    }

    @Override
    public void update(YouMindEntity entity) {
        TaskEntity task = verifyTaskObject(entity);
        try {
            TaskEntity te = get(task.getId());
            te.setName(task.getName());
            te.setTodoDate(task.getTodoDate());
            te.setSeverity(task.getSeverity());
            te.setActive(task.isActive());
        } catch (Exception ex) {
            Log.e(TagName, "update: " + ex.getMessage());
            throw new RuntimeException("update: " + ex.getMessage());
        }
    }

    @Override
    public void delete(String entityID) {
        get(entityID).setActive(false);
    }

    @Override
    public TaskEntity get(String entityID) {
        for (TaskEntity task : tasks) {
            if (task.getId().equals(entityID))
                return task;
        }
        return null;
    }

    @Override
    public JSONObject entityToJsonObject(YouMindEntity entity) {
        Log.d(TagName, "Starting EntityToJsonObject Function.");
        TaskEntity task = verifyTaskObject(entity);
        JSONObject taskObject = null;
        try {
            taskObject = new JSONObject();
            taskObject.put(Utils.ID_STRING_REF, task.getId());
            taskObject.put(Utils.NAME_STRING_REF, task.getName());
            taskObject.put(Utils.CREATE_DATE_STRING_REF, Utils.get_date_as_string(task.getCreateDate()));
            taskObject.put(Utils.LEVEL_STRING_REF, task.getSeverity().toString());
            taskObject.put(Utils.TODO_DATE_STRING_REF, Utils.get_date_as_string(task.getTodoDate()));
            taskObject.put(Utils.IS_ACTIVE_STRING_REF, task.isActive());
            JSONArray labelsArray = new JSONArray(entity.getLabels());
            taskObject.put(Utils.LABELS_STRING_REF, labelsArray);
        } catch (Exception ex) {
            Log.e(TagName, "Error converting Entity to JsonObject - " + ex.getMessage());
        }
        return taskObject;
    }

    @Override
    public TaskEntity jsonObjectToEntity(JSONObject entity) {
        Log.d(TagName, "Starting jsonObjectToEntity Function.");
        try {
            String id = entity.getString(Utils.ID_STRING_REF);
            String name = entity.getString(Utils.NAME_STRING_REF);
            String createDateStr = entity.getString(Utils.CREATE_DATE_STRING_REF);
            String level = entity.getString(Utils.LEVEL_STRING_REF);
            String todoDateStr = entity.getString(Utils.TODO_DATE_STRING_REF);
            Date createDate = Utils.get_string_as_date(createDateStr);
            Date todoDate = Utils.get_string_as_date(todoDateStr);
            boolean isActive = entity.getBoolean(Utils.IS_ACTIVE_STRING_REF);
            JSONArray labelsArray = entity.getJSONArray(Utils.LABELS_STRING_REF);
            ArrayList<String> labels = new ArrayList<>();
            for (int i = 0; i < labelsArray.length(); i++) {
                labels.add(labelsArray.getString(i));
                addLabelName(labelsArray.getString(i));
            }
            return new TaskEntity(id, name, createDate, labels, TaskSeverityEnum.valueOf(level), todoDate, isActive);
        } catch (Exception ex) {
            Log.e(TagName, "Error converting JsonObject to Entity - " + ex.getMessage());
        }
        return null;
    }

    @Override
    public JSONArray entitiesToJsonArray() {
        JSONArray ja = new JSONArray();
        for (TaskEntity task: tasks) {
            ja.put(entityToJsonObject(task));
        }
        return ja;
    }

    @Override
    public void jsonArrayToEntities(JSONArray array) {
        tasks.clear();
        try {
            for (int i = 0; i < array.length(); i++) {
                TaskEntity task = jsonObjectToEntity(array.getJSONObject(i));
                if (task != null)
                    tasks.add(task);
            }
        } catch (Exception ex) {
            Log.e(TagName, "Error loading data from local Storage - " + ex.getMessage());
        }
    }

    public ArrayList<TaskEntity> getActiveTasks() {
        ArrayList<TaskEntity> actives = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).isActive())
                actives.add(tasks.get(i));
        }
        return actives;
    }

    public ArrayList<TaskEntity> getAllTasks() {
        return tasks;
    }

    private TaskEntity verifyTaskObject(YouMindEntity entity){
        if (entity instanceof TaskEntity)
            return (TaskEntity) entity;

        Log.e(TagName, "Entity is not a Task.");
        throw new RuntimeException("Entity is not a Task.");
    }

    public static class TaskComparator implements Comparator<TaskEntity> {

        @Override
        public int compare(TaskEntity o1, TaskEntity o2) {
            return o1.getTodoDate().compareTo(o2.getTodoDate());
        }

        @Override
        public boolean equals(Object obj) {
            return this.equals(obj);
        }
    }
}
