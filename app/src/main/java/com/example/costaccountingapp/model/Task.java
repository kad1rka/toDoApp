package com.example.costaccountingapp.model;

import java.time.LocalDate;

public class Task {
     int id;
     String title;
     String description;
     boolean is_completed;
     LocalDate created_at;

    public Task(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Task(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIs_completed() {
        return is_completed;
    }

    public void setIs_completed(boolean is_completed) {
        this.is_completed = is_completed;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }
}
