package com.example.notebookandroidfront;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.notebookandroidfront.api.Api;
import com.example.notebookandroidfront.fragment.EditUserFragment;
import com.example.notebookandroidfront.fragment.LoginFragment;
import com.example.notebookandroidfront.fragment.MainFragment;
import com.example.notebookandroidfront.fragment.RegistrationFragment;
import com.example.notebookandroidfront.model.NoteBook;
import com.example.notebookandroidfront.model.User;
import com.example.notebookandroidfront.service.UserService;

public class MainActivity extends AppCompatActivity {
    public boolean isLoggedIn = false;
    public boolean isUserEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UserService.getInstance().sharedPreferences = getSharedPreferences("MyLocaleServer", MODE_PRIVATE);

        String token = UserService.getInstance().sharedPreferences.getString("token", null);
        if (token != null) {
            UserService.getInstance().authorize(token, new UserService.AuthorizationCallback() {
                @Override
                public void onAuthorized(User user) {
                    isLoggedIn = true;
                    isUserEdit = true;
                    invalidateOptionsMenu();
                    changeFragment(new MainFragment());
                }

                @Override
                public void onAuthorizationFailed() {
                    isLoggedIn = false;
                    isUserEdit = false;
                    invalidateOptionsMenu();
                    changeFragment(new LoginFragment());
                }
            });
        } else {
            isLoggedIn = false;
            isUserEdit = false;
            invalidateOptionsMenu();
            changeFragment(new LoginFragment());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem loginItem = menu.findItem(R.id.action_login);
        MenuItem registerItem = menu.findItem(R.id.action_register);
        MenuItem exitItem = menu.findItem(R.id.action_exit);
        MenuItem editUserItem = menu.findItem(R.id.action_edit_user);
        editUserItem.setVisible(isUserEdit);
        if (isLoggedIn) {
            if (loginItem != null) loginItem.setVisible(false);
            if (registerItem != null) registerItem.setVisible(false);
            if (exitItem != null) exitItem.setVisible(true);
        } else {
            if (loginItem != null) loginItem.setVisible(true);
            if (registerItem != null) registerItem.setVisible(true);
            if (exitItem != null) exitItem.setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_login) {
            changeFragment(new LoginFragment());
            Toast.makeText(this, "Login clicked", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.action_register) {
            changeFragment(new RegistrationFragment());
            Toast.makeText(this, "Register clicked", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.action_exit) {
            changeFragment(new LoginFragment());
            UserService.getInstance().sharedPreferences.edit().remove("token").apply();
            isLoggedIn = false;
            invalidateOptionsMenu();
            Toast.makeText(this, "Exit clicked", Toast.LENGTH_SHORT).show();
        }else if (item.getItemId() == R.id.action_edit_user){
            changeFragment(new EditUserFragment());
            isUserEdit = false;
            invalidateOptionsMenu();
            Toast.makeText(this, "Edit user clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }
}
