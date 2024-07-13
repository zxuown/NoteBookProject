package com.example.notebookandroidfront.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebookandroidfront.R;
import com.example.notebookandroidfront.adapter.NoteBookAdapter;
import com.example.notebookandroidfront.dialog.NoteBookDialog;
import com.example.notebookandroidfront.model.NoteBook;
import com.example.notebookandroidfront.service.NoteBookService;

import java.util.List;
import java.util.Objects;

public class NoteBookInfo extends Fragment {
    private TextView tvTitle, tvDescription, tvUserId, tvUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_book_info, container, false);

        tvTitle = view.findViewById(R.id.tvTitle);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvUserId = view.findViewById(R.id.tvUserId);
        tvUsername = view.findViewById(R.id.tvUsername);

        assert getArguments() != null;
        NoteBook noteBook = (NoteBook) getArguments().getSerializable("noteBook");

        if (noteBook != null) {
            tvTitle.setText("Title: " + noteBook.getTitle());
            tvDescription.setText("Description: " + noteBook.getDescription());
            tvUserId.setText("User ID: " + noteBook.getUser().getId());
            tvUsername.setText("Username: " + noteBook.getUser().getUsername());
        }
        (view.findViewById(R.id.btnBack)).setOnClickListener(v->{
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new MainFragment())
                    .commit();
        });
        return view;
    }
}
