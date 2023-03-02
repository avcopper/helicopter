package com.andrew.helicopter.Models;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public class Group {
    private String name; // название группы

    public Group() {}

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                '}';
    }

    /**
     * Возвращает объект в виде json-строки
     * @return
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);

//        return "{\"name\": \"" + name + "\"}";
    }
}
