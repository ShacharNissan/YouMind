package com.shacharnissan.youmind.storage;

import com.shacharnissan.youmind.data.YouMindEntity;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class EntityDao {
    public abstract void add(YouMindEntity entity);
    public abstract void update(YouMindEntity entity);
    public abstract void delete(String entityID);
    public abstract YouMindEntity get(String entityID);

    public abstract JSONObject entityToJsonObject(YouMindEntity entity);
    public abstract YouMindEntity jsonObjectToEntity(JSONObject entity);
    public abstract JSONArray entitiesToJsonArray();
    public abstract void jsonArrayToEntities(JSONArray array);
}
