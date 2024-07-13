package com.example.notebookandroidfront.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebookandroidfront.R;
import com.example.notebookandroidfront.adapter.NoteBookAdapter;
import com.example.notebookandroidfront.dialog.NoteBookDialog;
import com.example.notebookandroidfront.model.NoteBook;
import com.example.notebookandroidfront.service.NoteBookService;

import java.io.Serializable;
import java.util.List;

public class MainFragment extends Fragment {

    private RecyclerView rvNoteBooks;
    public static NoteBookAdapter noteBookAdapter;

    public boolean deletedButtonClicked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        rvNoteBooks = view.findViewById(R.id.rvNoteBooks);
        NoteBookService.getInstance().getAllUserNoteBooks(new NoteBookService.NoteBooksCallback() {
            @Override
            public void onNoteBookLoaded(List<NoteBook> noteBooks) {
                Toast.makeText(getContext(), "Good load note books", Toast.LENGTH_SHORT).show();
                noteBookAdapter = new NoteBookAdapter(getContext(), R.layout.item_list, noteBooks, MainFragment.this);
                rvNoteBooks.setAdapter(noteBookAdapter);

                LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                rvNoteBooks.setLayoutManager(manager);

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(rvNoteBooks);

                view.findViewById(R.id.btnAddNoteBook).setOnClickListener(v -> {
                    NoteBookDialog noteBookDialog = new NoteBookDialog();
                    noteBookDialog.show(MainFragment.this.getChildFragmentManager(), "noteBook");
                });
            }

            @Override
            public void onNoteBookLoadFailed() {
                Toast.makeText(getContext(), "Failed to load note books", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            NoteBook noteBook = NoteBookService.getInstance().noteBooks.get(position);

            if (direction == ItemTouchHelper.LEFT) {
                handleSwipeLeft(noteBook, position);
            } else if (direction == ItemTouchHelper.RIGHT) {
                handleSwipeRight(noteBook, position);
            }
        }
    };

    private void handleSwipeLeft(NoteBook noteBook, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Are you sure you want to delete this notebook? " + noteBook.getId())
                .setPositiveButton("Yes", (dialog, id) -> {
                    NoteBookService.getInstance().deleteNoteBook(noteBook.getId());
                })
                .setNegativeButton("No", (dialog, id) -> {
                    noteBookAdapter.notifyItemChanged(position);
                    deletedButtonClicked = true;
                    dialog.dismiss();
                }).setOnDismissListener(dialog -> {
                    if (!deletedButtonClicked){
                        noteBookAdapter.notifyItemChanged(position);
                    }
                    deletedButtonClicked = false;
                });
        builder.create().show();
    }

    private void handleSwipeRight(NoteBook noteBook, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Edit this notebook? " + noteBook.getId())
                .setPositiveButton("Yes", (dialog, id) -> {
                    NoteBookDialog noteBookDialog = new NoteBookDialog();
                    Bundle args = new Bundle();
                    args.putSerializable("noteBook", noteBook);
                    noteBookDialog.setArguments(args);
                    noteBookDialog.show(MainFragment.this.getChildFragmentManager(), "noteBook");
                })
                .setNegativeButton("No", (dialog, id) -> {
                    noteBookAdapter.notifyItemChanged(position);
                    deletedButtonClicked = true;
                    dialog.dismiss();
                }).setOnDismissListener(dialog -> {
                    if (!deletedButtonClicked){
                        noteBookAdapter.notifyItemChanged(position);
                    }
                    deletedButtonClicked = false;
                });
        builder.create().show();
    }
}
