package com.shacharnissan.youmind.storage;

import android.util.Log;

import com.shacharnissan.youmind.TaskUtills;
import com.shacharnissan.youmind.data.NoteEntity;
import com.shacharnissan.youmind.data.TaskEntity;
import com.shacharnissan.youmind.data.YouMindEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class NoteDao extends EntityDao{
    private final String TagName = "YouMind-NoteDao";
    private final String TagIdPrefix = "noteID";

    private ArrayList<NoteEntity> notes;

    public NoteDao(){
        notes = new ArrayList<NoteEntity>();
    }

    @Override
    public void add(YouMindEntity entity) {
        NoteEntity note = verifyNoteObject(entity);
        notes.add(note);
        note.setId(TaskUtills.generateID(TagIdPrefix));
    }

    @Override
    public void update(YouMindEntity entity) {
        NoteEntity note = verifyNoteObject(entity);
        try {
            NoteEntity ne = get(note.getId());
            ne.setName(note.getName());
            ne.setValue(note.getValue());
        }catch (Exception ex) {
            Log.e(TagName, "update: " + ex.getMessage());
            throw new RuntimeException("update: " + ex.getMessage());
        }
    }

    @Override
    public void delete(String entityID) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId().equals(entityID)) {
                notes.remove(i);
            }
        }
    }

    @Override
    public NoteEntity get(String entityID) {
        for (NoteEntity note : notes) {
            if (note.getId().equals(entityID))
                return note;
        }
        return null;
    }

    @Override
    public JSONObject entityToJsonObject(YouMindEntity entity) {
        Log.d(TagName, "Starting EntityToJsonObject Function.");
        NoteEntity note = verifyNoteObject(entity);
        JSONObject noteObject = null;
        try {
            noteObject = new JSONObject();
            noteObject.put(TaskUtills.ID_STRING_REF, note.getId());
            noteObject.put(TaskUtills.NAME_STRING_REF, note.getName());
            noteObject.put(TaskUtills.CREATE_DATE_STRING_REF, TaskUtills.get_date_as_string(note.getCreateDate()));
            noteObject.put(TaskUtills.VALUE_STRING_REF, note.getValue());
        } catch (Exception ex) {
            Log.e(TagName, "Error converting Entity to JsonObject - " + ex.getMessage());
        }
        return noteObject;
    }

    @Override
    public NoteEntity jsonObjectToEntity(JSONObject entity) {
        Log.d(TagName, "Starting jsonObjectToEntity Function.");
        try {
            String id = entity.getString(TaskUtills.ID_STRING_REF);
            String name = entity.getString(TaskUtills.NAME_STRING_REF);
            String createDateStr = entity.getString(TaskUtills.CREATE_DATE_STRING_REF);
            String value = entity.getString(TaskUtills.VALUE_STRING_REF);
            Date createDate = TaskUtills.get_string_as_date(createDateStr);
            return new NoteEntity(id, name, createDate,value);
        } catch (Exception ex){
            Log.e(TagName, "Error converting JsonObject to Entity - " + ex.getMessage());
        }
        return null;
    }

    @Override
    public JSONArray entitiesToJsonArray() {
        JSONArray ja = new JSONArray();
        for (NoteEntity note: notes) {
            ja.put(entityToJsonObject(note));
        }
        return ja;
    }

    @Override
    public void jsonArrayToEntities(JSONArray array) {
        try {
            for (int i = 0; i < array.length(); i++) {
                NoteEntity note = jsonObjectToEntity(array.getJSONObject(i));
                if (note != null)
                    notes.add(note);
            }
        } catch (Exception ex) {
            Log.e(TagName, "Error loading data from local Storage - " + ex.getMessage());
        }
    }

    public ArrayList<NoteEntity> getAllNotes(){
        return notes;
    }

    private NoteEntity verifyNoteObject(YouMindEntity entity){
        if (entity instanceof NoteEntity)
            return (NoteEntity) entity;

        Log.e(TagName, "Entity is not a Note.");
        throw new RuntimeException("Entity is not a Note.");
    }

    public static class NoteComparator implements Comparator<NoteEntity> {

        @Override
        public int compare(NoteEntity o1, NoteEntity o2) {
            return o1.getCreateDate().compareTo(o2.getCreateDate());
        }

        @Override
        public boolean equals(Object obj) {
            return this.equals(obj);
        }
    }
}
