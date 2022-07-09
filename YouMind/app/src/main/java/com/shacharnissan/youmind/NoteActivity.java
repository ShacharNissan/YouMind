package com.shacharnissan.youmind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shacharnissan.youmind.data.NoteEntity;

import java.util.ArrayList;
import java.util.Arrays;

public class NoteActivity extends AppCompatActivity {
    private final String TagName = "YouMind-NoteActivity";
    // UI Components
    private EditText et_name;
    private EditText et_value;
    private EditText et_labels;
    private AutoCompleteTextView et_auto_labels;
    private Button btnNewNote;
    private Button btnEditNote;
    private Button btnDeleteNote;
    private Button btnNewLabel;

    // Service Vars
    private TasksService mService;
    private String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TagName, "Starting onCreate Function.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initActivity();
        SetupActivity();
    }

    @Override
    protected void onStart() {
        Log.d(TagName, "Starting onStart Function.");
        super.onStart();

        // Service Init
        if (mService == null) {
            Intent serviceIntent = new Intent(this, TasksService.class);
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        Log.d(TagName, "Starting onStop Function.");
        mService.saveDataToMemory();
        unbindService(serviceConnection);
        super.onStop();
    }

    private void initActivity() {
        this.et_name = findViewById(R.id.et_note_name);
        this.et_value = findViewById(R.id.et_note_value);
        this.et_labels = findViewById(R.id.et_note_labels);
        this.et_auto_labels = findViewById(R.id.et_auto_note_label);
        this.btnNewNote = findViewById(R.id.btn_note_add);
        this.btnEditNote = findViewById(R.id.btn_note_edit);
        this.btnDeleteNote = findViewById(R.id.btn_note_delete);
        this.btnNewLabel = findViewById(R.id.btn_note_labels);
    }

    private void SetupActivity() {
        Intent intent = getIntent();
        noteId = intent.getStringExtra( getResources().getString(R.string.note_id_tag));

        // set Listeners
        btnNewNote.setOnClickListener(v -> newButtonClicked());
        btnEditNote.setOnClickListener(v -> editButtonClicked());
        btnDeleteNote.setOnClickListener(v -> deleteButtonClicked());
        btnNewLabel.setOnClickListener(v-> newLabelButtonClicked());
    }

    private void newLabelButtonClicked() {
        et_labels.setText(String.format("%s %s", et_labels.getText().toString(), et_auto_labels.getText().toString()));
        et_auto_labels.setText("");
    }

    private void setLabelsAutoFill() {
        // Set labels auto fill
        ArrayList<String> labels = mService.getAllLabels();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, labels);
        et_auto_labels.setThreshold(1);
        et_auto_labels.setAdapter(adapter);
        et_auto_labels.setOnItemClickListener((parent, view, position, id) -> et_auto_labels.setText(adapter.getItem(position)));
    }

    private void newButtonClicked() {
        try {
            NoteEntity noteEntity = getNoteFromView();
            mService.noteDao.add(noteEntity);
            mService.saveDataToMemory();
            Log.i(TagName, "New Note Created.");
            Toast.makeText(this, "New Note Created.", Toast.LENGTH_SHORT).show();
            returnToPreviousActivity();
        } catch (Exception ex){
            Log.e(TagName, "NewTaskButtonClicked: Failed to save Task");
        }
    }

    private void editButtonClicked() {
        NoteEntity noteEntity = getNoteFromView();
        noteEntity.setId(noteId);
        mService.noteDao.update(noteEntity);
        mService.saveDataToMemory();
        Toast.makeText(this, "Updated Note.", Toast.LENGTH_SHORT).show();
        returnToPreviousActivity();
    }

    private void deleteButtonClicked() {
        mService.noteDao.delete(noteId);
        mService.saveDataToMemory();
        Toast.makeText(this, "Note Deleted.", Toast.LENGTH_SHORT).show();
        returnToPreviousActivity();
    }

    private NoteEntity getNoteFromView(){
        String name = this.et_name.getText().toString();
        String value = this.et_value.getText().toString();
        ArrayList<String> labels = Utils.text_to_labels_array(this.et_labels.getText().toString());
        return new NoteEntity(name, labels, value);
    }

    private void returnToPreviousActivity(){
        this.finish();
    }

    private void setView() {
        if (noteId.equals(getResources().getString(R.string.no_entity_id))) {
            setNewNoteView();
            return;
        }
        setEditNoteView();
        mService.loadDataFromMemory();
        NoteEntity note = mService.noteDao.get(noteId);

        this.et_name.setText(note.getName());
        this.et_value.setText(note.getValue());
        this.et_labels.setText(Utils.labels_array_to_string(note.getLabels()));
    }

    private void setEditNoteView() {
        btnEditNote.setVisibility(View.VISIBLE);
        btnEditNote.setClickable(true);
        btnDeleteNote.setVisibility(View.VISIBLE);
        btnDeleteNote.setClickable(true);
        btnNewNote.setVisibility(View.INVISIBLE);
        btnNewNote.setClickable(false);
    }

    private void setNewNoteView() {
        btnEditNote.setVisibility(View.INVISIBLE);
        btnEditNote.setClickable(false);
        btnDeleteNote.setVisibility(View.INVISIBLE);
        btnDeleteNote.setClickable(false);
        btnNewNote.setVisibility(View.VISIBLE);
        btnNewNote.setClickable(true);
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TagName, "onServiceConnected: connected to service");
            TasksService.MyBinder binder = (TasksService.MyBinder) service;
            mService = binder.getService();
            mService.loadDataFromMemory();
            setView();
            setLabelsAutoFill();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TagName, "onServiceDisconnected: disconnected from service");
            mService = null;
        }
    };
}