package com.shacharnissan.youmind.storage;

import android.util.Log;

import com.shacharnissan.youmind.data.TaskEntity;
import com.shacharnissan.youmind.data.TaskSeverityEnum;
import com.shacharnissan.youmind.TaskUtills;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class LocalJson {
    private final String TagName = "YouMind-LocalJson";
    public final String FILENAME = "Tasks_data.json";

    public JSONObject loadData(File fileURL) {
        Log.d(TagName, "Starting loadData Function.");
        try {
            File readFile = new File(fileURL,FILENAME);
            FileInputStream fstream = new FileInputStream(readFile);
            byte[] content = new byte[(int) readFile.length()];
            fstream.read(content);
            return new JSONObject(new String(content));
        } catch (Exception ex) {
            Log.e(TagName, "Error loading data from local Storage - " + ex.getMessage());
        }
        return null;
    }

    public void saveData(File fileURL, JSONObject jo) {
        Log.d(TagName, "Starting saveData Function.");
        try {
            String sBody = jo.toString();
            FileOutputStream writer = new FileOutputStream(new File(fileURL, FILENAME));
            writer.write(sBody.getBytes());
            writer.close();
            Log.i(TagName, "File Saved to local storage");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
