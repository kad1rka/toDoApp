package com.example.costaccountingapp.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.costaccountingapp.R;
import com.example.costaccountingapp.model.Task;
import com.example.costaccountingapp.retrofit.APIService;
import com.example.costaccountingapp.retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    Context context;
    List<Task> tasks;
    public void addTask(Task newTask) {
        tasks.add(newTask);
        notifyItemInserted(tasks.size() + 1); // Уведомляем адаптер о том, что элемент был добавлен
    }
    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tasksItems = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new TaskAdapter.TaskViewHolder(tasksItems);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.taskTitle.setText(tasks.get(position).getTitle());
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int taskId = task.getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                builder.setView(dialogView);

                TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
                EditText input = dialogView.findViewById(R.id.editTextTaskId);


                input.setText(String.valueOf(task.getTitle()));
                builder.setPositiveButton("Сохранить", (dialog, which) -> {
                    String editedText = input.getText().toString();

                    Task updatedTask = new Task(taskId, editedText);

                    APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
                    Call<Task> call = apiService.updateTask(updatedTask);
                    call.enqueue(new Callback<Task>() {
                        @Override
                        public void onResponse(Call<Task> call, Response<Task> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Задача обновлена: " + editedText, Toast.LENGTH_SHORT).show();
                                tasks.set(holder.getAdapterPosition(), updatedTask);
                                notifyItemChanged(holder.getAdapterPosition());
                            } else {
                                Toast.makeText(context, "Ошибка обновления задачи", Toast.LENGTH_SHORT).show();

                            }
                        }
                        @Override
                        public void onFailure(Call<Task> call, Throwable t) {
                            Toast.makeText(context, "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                });
                builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
                Call<String> call = apiService.deleteTask(task.getId());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Задача успешно удалена! ", Toast.LENGTH_SHORT).show();

                            int position = holder.getAdapterPosition();
                            tasks.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, tasks.size());

                        } else {
                            Toast.makeText(context, "Ошибка удаления задачи", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(context, "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }
    @Override
    public int getItemCount() {
        return tasks.size();
    }
    public static final class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle;
        private MaterialButton editButton;
        private MaterialButton deleteButton;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.titleTv);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

}
