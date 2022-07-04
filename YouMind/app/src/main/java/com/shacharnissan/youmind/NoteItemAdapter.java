package com.shacharnissan.youmind;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shacharnissan.youmind.data.NoteEntity;

import java.util.ArrayList;

public class NoteItemAdapter extends RecyclerView.Adapter<NoteItemAdapter.NoteItemHolder> {
    private final String TagName = "YouMind-NoteItemAdapter";

    private ArrayList<NoteEntity> items;
    private OnNoteClickListener listener;

    public static class NoteItemHolder extends RecyclerView.ViewHolder {
        public TextView lbl_task_name;

        public NoteItemHolder(View itemView) {
            super(itemView);
            lbl_task_name = itemView.findViewById(R.id.lbl_task_name);
        }

        public void bind(final NoteEntity item, final OnNoteClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }

    public NoteItemAdapter(ArrayList<NoteEntity> items, OnNoteClickListener listener){
        Log.d(TagName, "Creating NoteItemAdapter Object.");
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TagName, "Starting onCreateViewHolder Function.");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_fragment,parent, false);
        return new NoteItemHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteItemHolder holder, int position) {
        Log.d(TagName, "Starting onBindViewHolder Function.");
        NoteEntity currentItem = this.items.get(position);
        holder.lbl_task_name.setText(currentItem.getName());

        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnNoteClickListener {
        void onItemClick(NoteEntity task);
    }
}

