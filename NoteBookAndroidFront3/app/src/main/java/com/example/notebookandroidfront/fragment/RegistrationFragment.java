package com.example.notebookandroidfront.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.notebookandroidfront.R;
import com.example.notebookandroidfront.service.UserService;
import com.example.notebookandroidfront.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationFragment extends Fragment {

    private EditText login;
    private EditText password;
    private EditText confirmPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        login = view.findViewById(R.id.editTextLogin);
        password = view.findViewById(R.id.editTextPassword);
        confirmPassword = view.findViewById(R.id.editTextConfirmPassword);

        view.findViewById(R.id.buttonSignUp).setOnClickListener(x -> {
            String email = this.login.getText().toString();
            String password = this.password.getText().toString();
            String confirmPassword = this.confirmPassword.getText().toString();

            Pattern pattern = Pattern.compile( "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            UserService.getInstance().register(email, password, new UserService.AuthorizationCallback() {
                @Override
                public void onAuthorized(User user) {
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, new MainFragment())
                            .commit();
                }

                @Override
                public void onAuthorizationFailed() {
                    Toast.makeText(getContext(),"Register failed review your data!!!", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, new RegistrationFragment())
                            .commit();
                }
            });

        });
        return view;
    }
}
