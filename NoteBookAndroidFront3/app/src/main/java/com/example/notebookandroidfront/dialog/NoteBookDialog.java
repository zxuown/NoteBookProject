package com.example.notebookandroidfront.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.notebookandroidfront.R;
import com.example.notebookandroidfront.fragment.MainFragment;
import com.example.notebookandroidfront.model.NoteBook;
import com.example.notebookandroidfront.service.NoteBookService;

import java.util.List;

public class NoteBookDialog extends DialogFragment {
    private EditText etTitle;
    private EditText etDescription;
    private Button btnSave;
    private NoteBook noteBook;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.dialog_new_notebook, null);
        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            if(getArguments() != null){
                noteBook = (NoteBook) getArguments().getSerializable("noteBook");
                noteBook.setTitle(etTitle.getText().toString());
                noteBook.setDescription(etDescription.getText().toString());
                NoteBookService.getInstance().editNoteBook(noteBook, new NoteBookService.NoteBookCallback() {
                    @Override
                    public void onNoteBookLoaded(NoteBook noteBook) {
                        dismiss();
                    }

                    @Override
                    public void onNoteBookLoadFailed() {

                    }
                });
                return;
            }
            NoteBookService.getInstance().createNoteBook(etTitle.getText().toString(), etDescription.getText().toString(), new NoteBookService.NoteBookCallback() {
                @Override
                public void onNoteBookLoaded(NoteBook noteBook) {
                    dismiss();
                }
                @Override
                public void onNoteBookLoadFailed() {

                }
            });
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        return builder.create();
    }
}
