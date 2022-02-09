package com.shacharnissan.youmind;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.TaskItemHolder> {
    private ArrayList<TaskEntity> items;

    public static class TaskItemHolder extends RecyclerView.ViewHolder {
        public ImageView mImage;
        public TextView mTextView;

        public TaskItemHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.fragmentImg_taskLvl);
            mTextView = itemView.findViewById(R.id.fragmentText_taskName);
        }
    }

    public TaskItemAdapter(ArrayList<TaskEntity> items){
        this.items = items;
    }

    @Override
    public TaskItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_fragment,parent, false);
        TaskItemHolder holder = new TaskItemHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(TaskItemHolder holder, int position) {
        TaskEntity currentItem = this.items.get(position);

       // holder.mTextView.setText(currentItem.getDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
