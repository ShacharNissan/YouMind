package com.shacharnissan.youmind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shacharnissan.youmind.data.NoteEntity;
import com.shacharnissan.youmind.data.adapters.NoteItemAdapter;
import com.shacharnissan.youmind.storage.NoteDao;

import java.util.ArrayList;

public class NoteListDisplayActivity extends AppCompatActivity {
    private final String TagName = "YouMind-NoteListDisplayActivity";

    // UI Components
    private RecyclerView recyclerView;
    private RecyclerView.Adapter tableAdapterRV;
    private RecyclerView.LayoutManager tableLayoutManagerRV;

    private FloatingActionButton noteAddButton;

    // Service Vars
    private TasksService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TagName, "Starting OnCreate Function.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list_display);

        initActivity();
        SetupActivity();
    }

    private void initActivity() {
        this.recyclerView = findViewById(R.id.rv_noteList);
        this.noteAddButton = findViewById(R.id.btn_noteList_add);

        this.tableLayoutManagerRV = new LinearLayoutManager(this);
    }

    private void SetupActivity() {
        noteAddButton.setOnClickListener(v -> noteButtonClicked(null));
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
    protected void onResume() {
        Log.d(TagName, "Starting onResume Function.");
        super.onResume();
        refreshViews();
    }

    @Override
    protected void onDestroy() {
        Log.d(TagName, "Starting onDestroy Function.");
        unbindService(serviceConnection);
        super.onDestroy();
    }

    private void refreshViews() {
        Log.d(TagName, "Starting refreshViews Function.");

        if (mService == null)
            return;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                ArrayList<NoteEntity> notes = mService.noteDao.getAllNotes();
                notes.sort(new NoteDao.NoteComparator());
                recyclerView.setHasFixedSize(true);

                tableAdapterRV = new NoteItemAdapter(notes, task -> noteButtonClicked(task));

                recyclerView.setLayoutManager(tableLayoutManagerRV);
                recyclerView.setAdapter(tableAdapterRV);
            }
        }, 250);

    }

    private void noteButtonClicked(NoteEntity note) {
        Log.d(TagName, "Starting taskClicked Function.");

        String noteId = note != null ? note.getId() : getResources().getString(R.string.no_entity_id);

        Intent myIntent = new Intent(NoteListDisplayActivity.this, NoteActivity.class);
        myIntent.putExtra(getResources().getString(R.string.note_id_tag), noteId);
        startActivity(myIntent);
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TagName, "onServiceConnected: connected to service");
            TasksService.MyBinder binder = (TasksService.MyBinder) service;
            mService = binder.getService();
            refreshViews();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TagName, "onServiceDisconnected: disconnected from service");
            mService = null;
        }
    };
}