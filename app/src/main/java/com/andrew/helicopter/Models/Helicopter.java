package com.andrew.helicopter.Models;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.Serializable;

public class Helicopter implements Serializable {
    private String number; // номер вертолета

    public Helicopter() {}

    public Helicopter(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @NonNull
    @Override
    public String toString() {
        return "Helicopter{" +
                "number='" + number + '\'' +
                '}';
    }

    /**
     * Возвращает объект в виде json-строки
     * @return
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);

//        return "{\"number\": \"" + number + "\"}";
    }
}
