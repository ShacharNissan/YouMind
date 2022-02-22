package com.shacharnissan.youmind;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class NewTask extends AppCompatActivity {
    private EditText et_name;
    private EditText et_tododate;
    private RadioGroup rg_severity;
    private RadioButton rd_severity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtask);

        setInit();
        //
        TaskEntity task = new TaskEntity("Level1",TaskLevelsEnum.HARD);
        //
        setView(task);
    }

    private void setView(TaskEntity task) {
        this.et_name.setText(task.getName());
        this.et_tododate.setText(task.getTodoDate().toString());
        switch (task.getLevel()){
            case EASY:
                this.rd_severity = findViewById(R.id.task_rb_easy);
                break;
            case MEDIUM:
                this.rd_severity = findViewById(R.id.task_rb_medium);
                break;
            case HARD:
                this.rd_severity = findViewById(R.id.task_rb_hard);
                break;
            default:
                this.rd_severity = null;
        }
        if (this.rd_severity != null){
            this.rd_severity.setChecked(true);
        }
    }

    private void setInit(){
        this.et_name = findViewById(R.id.task_et_name);
        this.et_tododate = findViewById(R.id.task_et_tododate);
        this.rg_severity = findViewById(R.id.task_rg_severity);
    }
}