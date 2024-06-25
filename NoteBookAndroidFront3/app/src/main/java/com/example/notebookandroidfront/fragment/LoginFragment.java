package com.example.notebookandroidfront.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.notebookandroidfront.R;
import com.example.notebookandroidfront.model.User;
import com.example.notebookandroidfront.service.UserService;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private EditText login;

    private EditText password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        login = view.findViewById(R.id.editTextLogin);
        password = view.findViewById(R.id.editTextPassword);

        view.findViewById(R.id.buttonSignUp).setOnClickListener(x->{
            String email = this.login.getText().toString();
            String password = this.password.getText().toString();

            UserService.getInstance().login(email,password, new UserService.AuthorizationCallback() {
                @Override
                public void onAuthorized(User user) {
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, new MainFragment())
                            .commit();
                }

                @Override
                public void onAuthorizationFailed() {
                    Toast.makeText(getContext(),"Login failed review your data!!!", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, new LoginFragment())
                            .commit();
                }
            });
        });
        return view;
    }
}
