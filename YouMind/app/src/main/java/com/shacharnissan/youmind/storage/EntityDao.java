package com.shacharnissan.youmind.storage;

import com.shacharnissan.youmind.data.YouMindEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class EntityDao {
    protected ArrayList<String> labels;

    public abstract void add(YouMindEntity entity);
    public abstract void update(YouMindEntity entity);
    public abstract void delete(String entityID);
    public abstract YouMindEntity get(String entityID);

    public abstract JSONObject entityToJsonObject(YouMindEntity entity);
    public abstract YouMindEntity jsonObjectToEntity(JSONObject entity);
    public abstract JSONArray entitiesToJsonArray();
    public abstract void jsonArrayToEntities(JSONArray array);

    public ArrayList<String> getLabels(){
        return labels;
    }

    public void setLabels(String EntityID, ArrayList<String> labels) {
        YouMindEntity ne = get(EntityID);
        ne.setLabels(labels);
    }

    public void addLabelName(String name){
        if (name == null || name.isEmpty())
            throw new RuntimeException("setLabel: Label name cannot be Empty.");

        labels.add(name);
        Set<String> set = new HashSet<>(labels);
        labels.clear();
        labels.addAll(set);
        labels.sort(String::compareTo);
    }
}
