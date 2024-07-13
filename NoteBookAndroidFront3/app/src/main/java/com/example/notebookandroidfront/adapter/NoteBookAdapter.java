package com.example.notebookandroidfront.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebookandroidfront.R;
import com.example.notebookandroidfront.dialog.NoteBookDialog;
import com.example.notebookandroidfront.fragment.MainFragment;
import com.example.notebookandroidfront.fragment.NoteBookInfo;
import com.example.notebookandroidfront.model.NoteBook;

import java.util.List;

public class NoteBookAdapter extends RecyclerView.Adapter<NoteBookAdapter.NoteBookViewHolder> {
    private Context context;
    private int resource;
    private List<NoteBook> noteBooks;
    private LayoutInflater inflater;
    private Fragment fragment;

    private AdapterView.OnItemClickListener onItemClickListener;
    public NoteBookAdapter(Context context, int resource, List<NoteBook> data, Fragment fragment) {
        this.context = context;
        this.resource = resource;
        this.noteBooks = data;
        this.fragment = fragment;
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
        holder.tvTitle.setText(noteBook.getTitle());
        holder.tvDescription.setText(noteBook.getDescription());
        holder.itemView.setOnClickListener(v ->{
            NoteBookInfo noteBookInfo = new NoteBookInfo();
            Bundle args = new Bundle();
            args.putSerializable("noteBook", noteBook);
            noteBookInfo.setArguments(args);
            fragment.requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, noteBookInfo)
                    .commit();
            Toast.makeText(context, "Click on " + noteBook.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return noteBooks.size();
    }

    public static class NoteBookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDescription;
        public NoteBookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }


}
