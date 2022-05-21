package com.shacharnissan.youmind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter tableAdapterRV;
    private RecyclerView.LayoutManager tableLayoutManagerRV;

    private FloatingActionButton taskAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.recyclerView = findViewById(R.id.main_recyclerView);
        this.taskAddButton = findViewById(R.id.btn_tasks_add);

        this.tableLayoutManagerRV = new LinearLayoutManager(this);
        
        taskAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTaskClicked();
            }
        });

        // Service commands
        Intent serviceIntent = new Intent(this, TasksService.class);
        startService(serviceIntent);

        refreshViews();
    }

    private void newTaskClicked() {
        // Move to Score Activity
        Intent myIntent = new Intent(MainActivity.this, NewTask.class);
        startActivity(myIntent);

        // myIntent.putExtra(getResources().getString(R.string.username_tag), "" + name);
    }

    private void refreshViews(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                //String lines[] = MyService.get_file_lines_clean(context);
                //ArrayList<UnlockItem> items = new ArrayList<>();
                //for (int i = 0; i < lines.length; i++) {
                //    items.add(new UnlockItem(lines[i]));
                //}
                ArrayList<TaskEntity> tasks = TasksService.getTasks();
                recyclerView.setHasFixedSize(true);

                tableAdapterRV = new TaskItemAdapter(tasks, MainActivity.this);

                recyclerView.setLayoutManager(tableLayoutManagerRV);
                recyclerView.setAdapter(tableAdapterRV);
            }
        }, 250);

    }

    public ArrayList<TaskEntity> getTasks(){
        ArrayList<TaskEntity> tasks = new ArrayList<>();
        tasks.add(new TaskEntity("Level1",TaskLevelsEnum.HARD));
        tasks.add(new TaskEntity("Level2",TaskLevelsEnum.MEDIUM));
        tasks.add(new TaskEntity("Level3"));

        return tasks;
    }
}