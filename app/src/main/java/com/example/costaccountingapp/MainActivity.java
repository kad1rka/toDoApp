package com.example.costaccountingapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.costaccountingapp.adapter.TaskAdapter;
import com.example.costaccountingapp.model.Task;
import com.example.costaccountingapp.retrofit.APIService;
import com.example.costaccountingapp.retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    RecyclerView tasksRecycler;
    TaskAdapter taskAdapter;

    MaterialButton addButton;
    private List<Task> taskList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<Task>> call = apiService.getAllTask();

        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful()) {
                    taskList = response.body();
                    setTaskRecycler(taskList);
                } else {
                    Log.e("MainActivity", "Ошибка: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.e("MainActivity", "Не удалось получить данные: " + t.getMessage());
            }
        });

        addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                builder.setView(dialogView);


                TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);

                dialogTitle.setText("Добавление задачи");
                EditText input = dialogView.findViewById(R.id.editTextTaskId);


                builder.setPositiveButton("Сохранить", (dialog, which) -> {
                    String editedText = input.getText().toString();
                    Task task = new Task(editedText);

                    APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
                    Call<Task> call = apiService.addTask(task);
                    call.enqueue(new Callback<Task>() {
                        @Override
                        public void onResponse(Call<Task> call, Response<Task> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Задача успешно добавлена: " + editedText, Toast.LENGTH_SHORT).show();
                                taskAdapter.addTask(task);
                            } else {
                                Toast.makeText(getApplicationContext(), "Ошибка добавления задачи", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Task> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                });

                builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



    }

    private void setTaskRecycler(List<Task> tasksList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        tasksRecycler = findViewById(R.id.tasksRecyclerView);
        tasksRecycler.setLayoutManager(layoutManager);

        taskAdapter = new TaskAdapter(this, tasksList);
        tasksRecycler.setAdapter(taskAdapter);
    }




}