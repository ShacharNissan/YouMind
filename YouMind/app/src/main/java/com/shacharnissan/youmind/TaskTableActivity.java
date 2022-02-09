package com.shacharnissan.youmind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;

public class TaskTableActivity extends AppCompatActivity {
    private RecyclerView tableRV;
    private RecyclerView.Adapter tableAdapterRV;
    private RecyclerView.LayoutManager tableLayoutManagerRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_table);

        getViews();
        initViews();

        refreshViews();
    }

    private void getViews(){
        tableRV = findViewById(R.id.taskTable_recyclerView);
    }

    private void initViews(){
        tableLayoutManagerRV = new LinearLayoutManager(this);
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
                ArrayList<TaskEntity> tasks = getTasks();
                tableRV.setHasFixedSize(true);

                tableAdapterRV = new TaskItemAdapter(tasks);

                tableRV.setLayoutManager(tableLayoutManagerRV);
                tableRV.setAdapter(tableAdapterRV);
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