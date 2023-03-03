package com.andrew.helicopter.Models;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Time implements Serializable {
    private String date; // дата работ
    private int air;     // время работы в воздухе (хранится в минутах)
    private int earth;   // время работы на земле (хранится в минутах)
    private int start;   // запуск (хранится в штуках)
    private int sel;     // отбор (хранится в штуках)
    private int gen;     // ген. реж. (хранится в минутах)
    private int common;  // общее (хранится в минутах)
    private int land;    // посадки (хранится в штуках)

    public Time() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH);

        this.date = formatDate.format(date);
        this.air = 0;
        this.earth = 0;
        this.start = 0;
        this.sel = 0;
        this.gen = 0;
        this.common = 0;
        this.land = 0;
    }

    public Time(String date, int air, int earth, int start, int sel, int gen, int common, int land) {
        this.date = date;
        this.air = air;
        this.earth = earth;
        this.start = start;
        this.sel = sel;
        this.gen = gen;
        this.common = common;
        this.land = land;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAir() {
        return air;
    }

    public void setAir(int air) {
        this.air = air;
    }

    public int getEarth() {
        return earth;
    }

    public void setEarth(int earth) {
        this.earth = earth;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getSel() {
        return sel;
    }

    public void setSel(int sel) {
        this.sel = sel;
    }

    public int getGen() {
        return gen;
    }

    public void setGen(int gen) {
        this.gen = gen;
    }

    public int getCommon() {
        return common;
    }

    public void setCommon(int common) {
        this.common = common;
    }

    public int getLand() {
        return land;
    }

    public void setLand(int land) {
        this.land = land;
    }

    @Override
    public String toString() {
        return "Time{" +
                "date='" + date + '\'' +
                ", air=" + air +
                ", earth=" + earth +
                ", start=" + start +
                ", sel=" + sel +
                ", gen=" + gen +
                ", common=" + common +
                ", land=" + land +
                '}';
    }

    /**
     * Возвращает объект в виде json-строки
     * @return
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);

//        return "{" +
//                "\"date\": \"" + date + "\"," +
//                "\"air\": " + air + "," +
//                "\"earth\": " + earth + "," +
//                "\"start\": " + start + "," +
//                "\"sel\": " + sel + "," +
//                "\"gen\": " + gen + "," +
//                "\"common\": " + common +
//                "}";
    }
}
