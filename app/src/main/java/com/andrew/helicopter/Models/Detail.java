package com.andrew.helicopter.Models;

import androidx.annotation.NonNull;
import java.io.Serializable;

public class Detail implements Serializable {
    public static final int LIMIT_HOURS = 3000;      // 50 ч. в минутах (50 * 60)
    public static final int LIMIT_MONTHS = 15811200; // 6 мес. в минутах (183 * 24 * 60 * 60)

    private String id;     // id детали (нужен для идентификации деталей с одинаковым названием)
    private String number; // заводской номер
    private String name;   // название
    private String group;  // группа детали Group.name

    private int resourceGlobal;        // ресурс назначенный в часах (хранится в минутах)
    private int resourceGlobalCurrent; // ресурс текущий (хранится в минутах)
    private int resourceGlobalBalance; // ресурс текущий оставшийся (хранится в минутах)

    private int resourceRepair;        // ресурс ремонтный в часах (хранится в минутах)
    private int resourceRepairCurrent; // ресурс ремонтный (хранится в минутах)
    private int resourceRepairBalance; // ресурс ремонтный оставшийся (хранится в минутах)

    private long resourceGlobalDate;   // дата производства (хранится в метке времени unix в секундах)
    private long resourceGlobalPeriod; // срок эксплуатации в годах (хранится в секундах)
    private long resourceGlobalNext;   // дата замены (хранится в метке времени unix в секундах)

    private long resourceRepairDate;   // дата ремонта (хранится в метке времени unix в секундах)
    private long resourceRepairPeriod; // ремонтный ресурс в годах (хранится в секундах)
    private long resourceRepairNext;   // дата следущего ремонта (хранится в метке времени unix в секундах)

    public Detail() {}

    public Detail(String number, String name, String group, String id,
                  int resourceGlobal, int resourceGlobalCurrent,
                  int resourceRepair, int resourceRepairCurrent,
                  long resourceGlobalDate, long resourceGlobalPeriod,
                  long resourceRepairDate, long resourceRepairPeriod) {
        this.number = number;
        this.name = name;
        this.group = group;
        this.id = id;
        // назначенный ресурс
        this.resourceGlobal = resourceGlobal;
        this.resourceGlobalCurrent = resourceGlobalCurrent;
        this.resourceGlobalBalance = this.resourceGlobal - this.resourceGlobalCurrent;
        // ремонтный ресурс
        this.resourceRepair = resourceRepair;
        this.resourceRepairCurrent = resourceRepairCurrent;
        this.resourceRepairBalance = this.resourceRepair - this.resourceRepairCurrent;
        // срок службы
        this.resourceGlobalDate = resourceGlobalDate;
        this.resourceGlobalPeriod = resourceGlobalPeriod;
        this.resourceGlobalNext = this.resourceGlobalDate + this.resourceGlobalPeriod;
        // ремонтный срок
        this.resourceRepairDate = resourceRepairDate;
        this.resourceRepairPeriod = resourceRepairPeriod;
        this.resourceRepairNext = this.resourceRepairDate + this.resourceRepairPeriod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getResourceGlobal() {
        return resourceGlobal;
    }

    public void setResourceGlobal(int resourceGlobal) {
        this.resourceGlobal = resourceGlobal;
    }

    public int getResourceRepair() {
        return resourceRepair;
    }

    public void setResourceRepair(int resourceRepair) {
        this.resourceRepair = resourceRepair;
    }

    public int getResourceGlobalCurrent() {
        return resourceGlobalCurrent;
    }

    public void setResourceGlobalCurrent(int resourceGlobalCurrent) {
        this.resourceGlobalCurrent = resourceGlobalCurrent;
    }

    public int getResourceRepairCurrent() {
        return resourceRepairCurrent;
    }

    public void setResourceRepairCurrent(int resourceRepairCurrent) {
        this.resourceRepairCurrent = resourceRepairCurrent;
    }

    public int getResourceGlobalBalance() {
        return resourceGlobalBalance;
    }

    public void setResourceGlobalBalance(int resourceGlobalBalance) {
        this.resourceGlobalBalance = resourceGlobalBalance;
    }

    public int getResourceRepairBalance() {
        return resourceRepairBalance;
    }

    public void setResourceRepairBalance(int resourceRepairBalance) {
        this.resourceRepairBalance = resourceRepairBalance;
    }

    public long getResourceGlobalDate() {
        return resourceGlobalDate;
    }

    public void setResourceGlobalDate(long resourceGlobalDate) {
        this.resourceGlobalDate = resourceGlobalDate;
    }

    public long getResourceGlobalPeriod() {
        return resourceGlobalPeriod;
    }

    public void setResourceGlobalPeriod(long resourceGlobalPeriod) {
        this.resourceGlobalPeriod = resourceGlobalPeriod;
    }

    public long getResourceGlobalNext() {
        return resourceGlobalNext;
    }

    public void setResourceGlobalNext(long resourceGlobalNext) {
        this.resourceGlobalNext = resourceGlobalNext;
    }

    public long getResourceRepairDate() {
        return resourceRepairDate;
    }

    public void setResourceRepairDate(long resourceRepairDate) {
        this.resourceRepairDate = resourceRepairDate;
    }

    public long getResourceRepairPeriod() {
        return resourceRepairPeriod;
    }

    public void setResourceRepairPeriod(long resourceRepairPeriod) {
        this.resourceRepairPeriod = resourceRepairPeriod;
    }

    public long getResourceRepairNext() {
        return resourceRepairNext;
    }

    public void setResourceRepairNext(long resourceRepairNext) {
        this.resourceRepairNext = resourceRepairNext;
    }

    @NonNull
    @Override
    public String toString() {
        return "Detail{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", resourceGlobal=" + resourceGlobal +
                ", resourceGlobalCurrent=" + resourceGlobalCurrent +
                ", resourceGlobalBalance=" + resourceGlobalBalance +
                ", resourceRepair=" + resourceRepair +
                ", resourceRepairCurrent=" + resourceRepairCurrent +
                ", resourceRepairBalance=" + resourceRepairBalance +
                ", resourceGlobalDate=" + resourceGlobalDate +
                ", resourceGlobalPeriod=" + resourceGlobalPeriod +
                ", resourceGlobalNext=" + resourceGlobalNext +
                ", resourceRepairDate=" + resourceRepairDate +
                ", resourceRepairPeriod=" + resourceRepairPeriod +
                ", resourceRepairNext=" + resourceRepairNext +
                '}';
    }

    /**
     * Возвращает объект в виде json-строки
     * @return
     */
    public String toJson() {
        return "{" +
                "\"id\": \"" + id + "\"," +
                "\"number\": \"" + number + "\"," +
                "\"name\": \"" + name + "\"," +
                "\"group\": \"" + group + "\"," +
                "\"resourceGlobal\": " + resourceGlobal + "," +
                "\"resourceGlobalCurrent\": " + resourceGlobalCurrent + "," +
                "\"resourceGlobalBalance\": " + resourceGlobalBalance + "," +
                "\"resourceRepair\": " + resourceRepair + "," +
                "\"resourceRepairCurrent\": " + resourceRepairCurrent + "," +
                "\"resourceRepairBalance\": " + resourceRepairBalance + "," +
                "\"resourceGlobalDate\": " + resourceGlobalDate + "," +
                "\"resourceGlobalPeriod\": " + resourceGlobalPeriod + "," +
                "\"resourceGlobalNext\": " + resourceGlobalNext + "," +
                "\"resourceRepairDate\": " + resourceRepairDate + "," +
                "\"resourceRepairPeriod\": " + resourceRepairPeriod + "," +
                "\"resourceRepairNext\": " + resourceRepairNext +
                "}";
    }
}
