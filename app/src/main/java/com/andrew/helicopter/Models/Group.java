package com.andrew.helicopter.Models;

import androidx.annotation.NonNull;

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
        return "{\"name\": \"" + name + "\"}";
    }
}
