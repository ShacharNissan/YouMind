package com.shacharnissan.youmind.data.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shacharnissan.youmind.R;
import com.shacharnissan.youmind.data.TaskEntity;
import com.shacharnissan.youmind.data.TaskSeverityEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.TaskItemHolder> {
    private final String TagName = "YouMind-TaskItemAdapter";

    private ArrayList<TaskEntity> items;
    private Context context;
    private OnTasKClickListener listener;

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

        public void bind(final TaskEntity item, final OnTasKClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }

    public TaskItemAdapter(ArrayList<TaskEntity> items, Context context, OnTasKClickListener listener){
        Log.d(TagName, "Creating TaskItemAdapter Object.");
        this.items = items;
        this.context = context;
        this.listener = listener;
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
        holder.rl_task.setBackgroundColor(context.getColor(getColorBySeverity(currentItem.getSeverity())));
        //holder.rl_task.setBackgroundColor(getColorBySeverity(currentItem.getLevel()));
        holder.lbl_task_name.setText(currentItem.getName());
        holder.lbl_time_left.setText(getTimeLeft(currentItem.getTodoDate()));

        holder.bind(items.get(position), listener);
    }

    private int getColorBySeverity(TaskSeverityEnum level) {
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
        return String.format(Locale.getDefault(),"%d %c", time, c);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnTasKClickListener {
        void onItemClick(TaskEntity task);
    }
}

