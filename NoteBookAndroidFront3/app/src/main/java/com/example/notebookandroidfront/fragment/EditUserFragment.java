package com.example.notebookandroidfront.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.notebookandroidfront.MainActivity;
import com.example.notebookandroidfront.R;
import com.example.notebookandroidfront.model.User;
import com.example.notebookandroidfront.service.UserService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditUserFragment extends Fragment {

    private EditText etEmail, etPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        view.findViewById(R.id.btnSave).setOnClickListener(x -> {
            String email = this.etEmail.getText().toString();
            String password = this.etPassword.getText().toString();

            if (email.isEmpty() && password.isEmpty()) {
                Toast.makeText(getContext(), "Fill one of fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.isEmpty()) {
                Pattern pattern = Pattern.compile( "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
                Matcher matcher = pattern.matcher(email);
                if (!matcher.matches()) {
                    Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserService.getInstance().currentUser.setUsername(email);
            }

            UserService.getInstance().currentUser.setPassword(password.isEmpty() ? UserService.getInstance().currentUser.getPassword() : password);

            UserService.getInstance().updateUser(new UserService.AuthorizationCallback() {
                @Override
                public void onAuthorized(User user) {
                    Toast.makeText(getContext(),"Edit user success", Toast.LENGTH_SHORT).show();
                        ((MainActivity) requireActivity()).isLoggedIn = true;
                        requireActivity().invalidateOptionsMenu();
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainerView, new EditUserFragment())
                                .commit();
                }

                @Override
                public void onAuthorizationFailed() {
                    Toast.makeText(getContext(),"Edit user failed review your data!!!", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, new EditUserFragment())
                            .commit();
                }
            });

        });

        view.findViewById(R.id.btnBack).setOnClickListener(x -> {
            ((MainActivity) requireActivity()).isUserEdit = true;
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, new MainFragment())
                    .commit();
        });
        return view;
    }
}
