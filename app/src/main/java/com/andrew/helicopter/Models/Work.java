package com.andrew.helicopter.Models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Work implements Serializable {
    public static final int LIMIT_SHORT = 300; // 5 ч. в минутах (5 * 60)
    public static final int LIMIT_LONG = 600; // 10 ч. в минутах (10 * 60)
    public static final int LIMIT_MONTH = 604800; // 7 дн. в минутах (7 * 24 * 60 * 60)

    private String name;
    private int resourceHour;        // ресурс работ в часах (хранится в минутах)
    private int resourceHourCurrent; // ресурс работ текущий (хранится в минутах)
    private int resourceHourBalance; // остаток ресурса до следующих работ (хранится в минутах)

    private long resourceMonth; // ресурс работ в месяцах (хранится в секундах)
    private long workDate;      // дата работ (хранится в метке времени unix в секундах)
    private long workDateNext;  // дата следующих работ  (хранится в метке времени unix в секундах)

    private int sort; // сортировка списка

    public Work() {}

    public Work(String name, int resourceHour, int resourceHourCurrent, long resourceMonth, long workDate, int sort) {
        this.name = name;
        this.resourceHour = resourceHour;
        this.resourceHourCurrent = resourceHourCurrent;
        this.resourceHourBalance = resourceHour - resourceHourCurrent;
        this.resourceMonth = resourceMonth;
        this.workDate = workDate;
        this.workDateNext = resourceMonth + workDate;
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResourceHour() {
        return resourceHour;
    }

    public void setResourceHour(int resourceHour) {
        this.resourceHour = resourceHour;
    }

    public int getResourceHourCurrent() {
        return resourceHourCurrent;
    }

    public void setResourceHourCurrent(int resourceHourCurrent) {
        this.resourceHourCurrent = resourceHourCurrent;
    }

    public int getResourceHourBalance() {
        return resourceHourBalance;
    }

    public void setResourceHourBalance(int resourceHourBalance) {
        this.resourceHourBalance = resourceHourBalance;
    }

    public long getResourceMonth() {
        return resourceMonth;
    }

    public void setResourceMonth(long resourceMonth) {
        this.resourceMonth = resourceMonth;
    }

    public long getWorkDate() {
        return workDate;
    }

    public void setWorkDate(long workDate) {
        this.workDate = workDate;
    }

    public long getWorkDateNext() {
        return workDateNext;
    }

    public void setWorkDateNext(long workDateNext) {
        this.workDateNext = workDateNext;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @NonNull
    @Override
    public String toString() {
        return "Work{" +
                "name='" + name + '\'' +
                ", resourceHour=" + resourceHour +
                ", resourceHourCurrent=" + resourceHourCurrent +
                ", resourceHourBalance=" + resourceHourBalance +
                ", resourceMonth=" + resourceMonth +
                ", workDate=" + workDate +
                ", workDateNext=" + workDateNext +
                ", sort=" + sort +
                '}';
    }

    /**
     * Возвращает объект в виде json-строки
     * @return
     */
    public String toJson() {
        return "{" +
                "\"name\": \"" + name.replace("\\", "\\\\") + "\"," +
                "\"resourceHour\": " + resourceHour + "," +
                "\"resourceHourCurrent\": " + resourceHourCurrent + "," +
                "\"resourceHourBalance\": " + resourceHourBalance + "," +
                "\"resourceMonth\": " + resourceMonth + "," +
                "\"workDate\": " + workDate + "," +
                "\"workDateNext\": " + workDateNext + "," +
                "\"sort\": " + sort +
                "}";
    }
}
