package com.example.notebookandroidfront.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebookandroidfront.R;
import com.example.notebookandroidfront.model.NoteBook;

import java.util.List;

public class NoteBookAdapter extends RecyclerView.Adapter<NoteBookAdapter.NoteBookViewHolder> {
    private Context context;
    private int resource;
    private List<NoteBook> noteBooks;
    private LayoutInflater inflater;

    private AdapterView.OnItemClickListener onItemClickListener;
    public NoteBookAdapter(Context context, int resource, List<NoteBook> data) {
        this.context = context;
        this.resource = resource;
        this.noteBooks = data;
        inflater = LayoutInflater.from(context);
    }

    public void updateNoteBooks(List<NoteBook> newNoteBooks) {
        this.noteBooks = newNoteBooks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = inflater.inflate(resource, parent, false);
        return new NoteBookViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteBookViewHolder holder, int position) {
        NoteBook noteBook = noteBooks.get(position);
        holder.tvId.setText(String.valueOf(noteBook.getId()));
        holder.tvTitle.setText(noteBook.getTitle());
        holder.tvDescription.setText(noteBook.getDescription());
    }

    @Override
    public int getItemCount() {
        return noteBooks.size();
    }

    public static class NoteBookViewHolder extends RecyclerView.ViewHolder {
        TextView tvId;
        TextView tvTitle;
        TextView tvDescription;
        public NoteBookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }


}
