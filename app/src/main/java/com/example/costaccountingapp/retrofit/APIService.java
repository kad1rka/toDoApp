package com.example.costaccountingapp.retrofit;

import com.example.costaccountingapp.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {
    @PUT("update")
    Call<Task> updateTask(@Body Task task);
    @GET("tasks")
    Call<List<Task>> getAllTask();

    @DELETE("delete/{id}")
    Call<String> deleteTask(@Path("id") int id);

    @POST("add")
    Call<Task> addTask(@Body Task task);
}
