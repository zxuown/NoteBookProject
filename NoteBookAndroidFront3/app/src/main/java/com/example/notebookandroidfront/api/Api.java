package com.example.notebookandroidfront.api;


import com.example.notebookandroidfront.model.AuthRequest;
import com.example.notebookandroidfront.model.NoteBook;
import com.example.notebookandroidfront.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {

//    @GET("/notebooks/{id}")
//    Call<List<NoteBook>> getAllNoteBooks(@Header("Authorization") String authHeader, @Path("id") int id);

    @GET("/notebooks")
    Call<List<NoteBook>> getAllNoteBooks(@Header("Authorization") String authHeader);

    @GET("/notebooks/user/{id}")
    Call<List<NoteBook>> getAllUserNoteBooks(@Header("Authorization") String authHeader, @Path("id") int id);

    @POST("/notebooks")
    Call<NoteBook> postNoteBook(@Header("Authorization") String authHeader, @Body NoteBook body);

    @PUT("/notebooks")
    Call<NoteBook> putNoteBook(@Header("Authorization") String authHeader, @Body NoteBook body);

    @DELETE("/notebooks/{id}")
    Call<Void> deleteNoteBook(@Header("Authorization") String authHeader, @Path("id") int id);

    @POST("/auth/login")
    Call<String> login(@Body AuthRequest body);

    @GET("/auth/user/token/{userToken}")
    Call<User> authorize(@Path("userToken") String userToken);

    @POST("/auth/register")
    Call<String> register(@Body AuthRequest body);
}
