package com.example.notebookandroidfront.service;

import android.util.Log;

import com.example.notebookandroidfront.fragment.MainFragment;
import com.example.notebookandroidfront.model.NoteBook;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteBookService {
    private static NoteBookService instance;

    public List<NoteBook> noteBooks;

    private NoteBookService() {
    }

    public static NoteBookService getInstance() {
        if (instance == null) {
            instance = new NoteBookService();
        }
        return instance;
    }

    public interface NoteBooksCallback {
        void onNoteBookLoaded(List<NoteBook> noteBooks);
        void onNoteBookLoadFailed();
    }

    public interface NoteBookCallback {
        void onNoteBookLoaded(NoteBook noteBook);
        void onNoteBookLoadFailed();
    }

    public void getAllUserNoteBooks(NoteBooksCallback callback) {
        String token = UserService.getInstance().sharedPreferences.getString("token", null);
        Log.d("MyLocaleServer", "token: " + token);
        Call<List<NoteBook>> call = NetworkService.getInstance().getApi().getAllUserNoteBooks("Bearer " + token, UserService.getInstance().currentUser.getId());
        call.enqueue(new Callback<List<NoteBook>>() {
            @Override
            public void onResponse(Call<List<NoteBook>> call, Response<List<NoteBook>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    noteBooks = response.body();
                    callback.onNoteBookLoaded(noteBooks);
                } else {
                    Log.d("MyLocaleServer", "getAllUserNoteBooks error: response unsuccessful or empty body");
                    callback.onNoteBookLoadFailed();
                }
            }

            @Override
            public void onFailure(Call<List<NoteBook>> call, Throwable t) {
                Log.d("MyLocaleServer", "getAllUserNoteBooks error: " + t.getMessage());
                callback.onNoteBookLoadFailed();
            }
        });
    }

    public void createNoteBook(String title, String description, NoteBookCallback callback) {
        String token = UserService.getInstance().sharedPreferences.getString("token", null);
        Call<NoteBook> call = NetworkService.getInstance().getApi().postNoteBook("Bearer " + token, new NoteBook(title, description, UserService.getInstance().currentUser));
        call.enqueue(new Callback<NoteBook>() {
            @Override
            public void onResponse(Call<NoteBook> call, Response<NoteBook> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onNoteBookLoaded(response.body());
                    getAllUserNoteBooks(new NoteBooksCallback() {
                        @Override
                        public void onNoteBookLoaded(List<NoteBook> noteBooks) {
                            MainFragment.noteBookAdapter.updateNoteBooks(noteBooks);
                        }

                        @Override
                        public void onNoteBookLoadFailed() {

                        }
                    });
                } else {
                    Log.d("MyLocaleServer", "getAllNoteBooks error: response unsuccessful or empty body");
                    callback.onNoteBookLoadFailed();
                }
            }

            @Override
            public void onFailure(Call<NoteBook> call, Throwable t) {
                Log.d("MyLocaleServer", "getAllNoteBooks error: " + t.getMessage());
                callback.onNoteBookLoadFailed();
            }
        });
    }

    public void editNoteBook(NoteBook noteBook, NoteBookCallback callback) {
        String token = UserService.getInstance().sharedPreferences.getString("token", null);
        Call<NoteBook> call = NetworkService.getInstance().getApi().putNoteBook("Bearer " + token, noteBook);
        call.enqueue(new Callback<NoteBook>() {
            @Override
            public void onResponse(Call<NoteBook> call, Response<NoteBook> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onNoteBookLoaded(response.body());
                    getAllUserNoteBooks(new NoteBooksCallback() {
                        @Override
                        public void onNoteBookLoaded(List<NoteBook> noteBooks) {
                            MainFragment.noteBookAdapter.updateNoteBooks(noteBooks);
                        }

                        @Override
                        public void onNoteBookLoadFailed() {

                        }
                    });
                } else {
                    Log.d("MyLocaleServer", "putNoteBook error: response unsuccessful or empty body");
                    callback.onNoteBookLoadFailed();
                }
            }

            @Override
            public void onFailure(Call<NoteBook> call, Throwable t) {
                Log.d("MyLocaleServer", "putNoteBook error: " + t.getMessage());
                callback.onNoteBookLoadFailed();
            }
        });
    }

    public void deleteNoteBook(int id){
        String token = UserService.getInstance().sharedPreferences.getString("token", null);
        Call<Void> call = NetworkService.getInstance().getApi().deleteNoteBook("Bearer " + token, id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                getAllUserNoteBooks(new NoteBooksCallback() {
                    @Override
                    public void onNoteBookLoaded(List<NoteBook> noteBooks) {
                        MainFragment.noteBookAdapter.updateNoteBooks(noteBooks);
                    }

                    @Override
                    public void onNoteBookLoadFailed() {

                    }
                });
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("MyLocaleServer", "deleteNoteBook error: " + t.getMessage());
            }
        });
    }
}
