package com.shacharnissan.youmind;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shacharnissan.youmind.storage.LocalJson;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.TaskItemHolder> {
    private final String TagName = "YouMind-TaskItemAdapter";
    public final String DATE_FORMAT_REF = "HH:mm:ss dd-MM-yyyy";

    private ArrayList<TaskEntity> items;
    private Context context;

    public static class TaskItemHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rl_task;
        //public ImageView mImage;
        public TextView lbl_task_name;
        public TextView lbl_time_left;

        public TaskItemHolder(View itemView) {
            super(itemView);
            rl_task = itemView.findViewById(R.id.layout_task);
            lbl_task_name = itemView.findViewById(R.id.lbl_task_name);
            lbl_time_left = itemView.findViewById(R.id.lbl_task_time);
        }
    }

    public TaskItemAdapter(ArrayList<TaskEntity> items, Context context){
        Log.d(TagName, "Creating TaskItemAdapter Object.");
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TagName, "Starting onCreateViewHolder Function.");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_fragment,parent, false);
        return new TaskItemHolder(v);
    }

    @Override
    public void onBindViewHolder(TaskItemHolder holder, int position) {
        Log.d(TagName, "Starting onBindViewHolder Function.");
        TaskEntity currentItem = this.items.get(position);
        //holder.rl_task.getBackground().setColorFilter(new BlendModeColorFilter(getColorBySeverity(currentItem.getLevel()), BlendMode.SRC_ATOP));
        holder.rl_task.setBackgroundColor(context.getColor(getColorBySeverity(currentItem.getLevel())));
        //holder.rl_task.setBackgroundColor(getColorBySeverity(currentItem.getLevel()));
        holder.lbl_task_name.setText(currentItem.getName());
        holder.lbl_time_left.setText(getTimeLeft(currentItem.getTodoDate()));
    }

    private int getColorBySeverity(TaskLevelsEnum level) {
        Log.d(TagName, "Starting getColorBySeverity Function.");
        switch (level){
            case EASY:
                return R.color.color_easy;
            case MEDIUM:
                return R.color.color_medium;
            case HARD:
                return R.color.color_hard;
        }
        return R.color.white;
    }

    private String getTimeLeft(Date task){
        char c = 'S';
        long time = 0;
        try {
            final long millis = task.getTime() - System.currentTimeMillis();
            final long days = TimeUnit.MILLISECONDS.toDays(millis);
            final long hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.MILLISECONDS.toHours(TimeUnit.MILLISECONDS.toDays(millis));
            final long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
            final long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));

            if (days != 0){
                c = 'D';
                time = days;
            } else if (hours != 0) {
                c = 'H';
                time = hours;
            } else if (minutes != 0) {
                c = 'M';
                time = minutes;
            } else {
            c = 'S';
            time = seconds;
        }


        }catch (Exception ex){
            Log.e(TagName,ex.getMessage());
        }
        return String.format("%d %c", time, c);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
