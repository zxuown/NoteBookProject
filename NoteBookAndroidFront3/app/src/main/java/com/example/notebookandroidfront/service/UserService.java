package com.example.notebookandroidfront.service;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.notebookandroidfront.MainActivity;
import com.example.notebookandroidfront.model.AuthRequest;
import com.example.notebookandroidfront.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {

    private static UserService instance;

    public User currentUser;

    public SharedPreferences sharedPreferences;

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public interface AuthorizationCallback {
        void onAuthorized(User user);
        void onAuthorizationFailed();
    }

    public void register(String email, String password, AuthorizationCallback callback) {
        Call<String> call = NetworkService.getInstance().getApi().register(new AuthRequest(email, password));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> registerCall, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("MyLocaleServer","register body " + response.body());
                    authorize(response.body(), callback);
                } else {
                    Log.d("MyLocaleServer", "Registration failed");
                    callback.onAuthorizationFailed();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("MyLocaleServer", "Registration error: " + t.getMessage());
                callback.onAuthorizationFailed();
            }
        });
    }

    public void authorize(String token, AuthorizationCallback callback) {
        if (token == null || token.isEmpty()) {
            Log.e("MyLocaleServer", "Token is null or empty, cannot authorize");
            callback.onAuthorizationFailed();
            return;
        }
        Call<User> call = NetworkService.getInstance().getApi().authorize(token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    Log.d("MyLocaleServer", "authorize: " + currentUser);
                    if (!token.equals(sharedPreferences.getString("token", null))) {
                        sharedPreferences.edit().putString("token", token).apply();
                    }
                    callback.onAuthorized(currentUser);
                } else {
                    Log.d("MyLocaleServer", "Authorization failed");
                    callback.onAuthorizationFailed();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("MyLocaleServer", "authorize error: " + t.getMessage());
                currentUser = null;
                callback.onAuthorizationFailed();
            }
        });
    }

    public void login(String email, String password, AuthorizationCallback callback) {
        Call<String> call = NetworkService.getInstance().getApi().login(new AuthRequest(email, password));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> registerCall, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("MyLocaleServer","login body " + response.body());
                    authorize(response.body(), callback);
                } else {
                    Log.d("MyLocaleServer", "login failed");
                    callback.onAuthorizationFailed();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("MyLocaleServer", "login error: " + t.getMessage());
                callback.onAuthorizationFailed();
            }
        });
    }

    public void updateUser(AuthorizationCallback authorizationCallback){
        Call<String> call = NetworkService.getInstance().getApi()
                .updateUser("Bearer " + sharedPreferences.getString("token", null), currentUser);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> registerCall, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("MyLocaleServer","updateUser body " + response.body());
                    authorize(response.body(), authorizationCallback);
                } else {
                    Log.d("MyLocaleServer", "updateUser failed");
                    authorizationCallback.onAuthorizationFailed();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("MyLocaleServer", "updateUser error: " + t.getMessage());
                authorizationCallback.onAuthorizationFailed();
            }
        });
    }
}

