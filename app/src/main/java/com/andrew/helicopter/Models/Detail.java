package com.andrew.helicopter.Models;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.Serializable;

public class Detail implements Serializable {
    public static final int LIMIT_HOURS = 3000;      // 50 ч. в минутах (50 * 60)
    public static final int LIMIT_MONTHS = 15811200; // 6 мес. в минутах (183 * 24 * 60 * 60)

    private String id;        // id детали (нужен для идентификации деталей с одинаковым названием)
    private String number;    // заводской номер
    private String name;      // название
    private String group;     // группа детали Group.name
    private int typeTemplate; // тип шаблона (1 - основной, 2 - ВСУ, 3 - количество посадок)

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

    private int startGlobal;        // общее количество запусков
    private int startGlobalCurrent; // текущее количество запусков
    private int startGlobalBalance; // остаток запусков

    private int startRepair;        // количество запусков до ремонта
    private int startRepairCurrent; // текущее количество запусков до ремонта
    private int startRepairBalance; // остаток запусков до ремонта

    private int selGlobal;        // общее количество отборов
    private int selGlobalCurrent; // текущее количество отборов
    private int selGlobalBalance; // остаток отборов

    private int selRepair;        // количество отборов до ремонта
    private int selRepairCurrent; // текущее количество отборов до ремонта
    private int selRepairBalance; // остаток отборов до ремонта

    private int genGlobal;        // общее время ген. режима (хранится в минутах)
    private int genGlobalCurrent; // текущее время ген. режима (хранится в минутах)
    private int genGlobalBalance; // остаток времени ген. режима (хранится в минутах)

    private int genRepair;        // время ген. режима до ремонта (хранится в минутах)
    private int genRepairCurrent; // текущее время ген. режима до ремонта (хранится в минутах)
    private int genRepairBalance; // остаток времени ген. режима до ремонта (хранится в минутах)

    private int commonGlobal;        // общее время (хранится в минутах)
    private int commonGlobalCurrent; // текущее время (хранится в минутах)
    private int commonGlobalBalance; // остаток времени (хранится в минутах)

    private int commonRepair;        // время до ремонта (хранится в минутах)
    private int commonRepairCurrent; // текущее время до ремонта (хранится в минутах)
    private int commonRepairBalance; // остаток времени до ремонта (хранится в минутах)

    private int landGlobal;        // назначенное количество посадок (хранится в минутах)
    private int landGlobalCurrent; // текущее назначенное количество посадок (хранится в минутах)
    private int landGlobalBalance; // остаток назначенных посадок (хранится в минутах)

    private int landRepair;        // межремонтное количество посадок (хранится в минутах)
    private int landRepairCurrent; // текущее межремонтное количество посадок (хранится в минутах)
    private int landRepairBalance; // остаток межремонтных посадок (хранится в минутах)

    public Detail() {}

    public Detail(String number, String name, String group, String id, int typeTemplate,
                  int resourceGlobal, int resourceGlobalCurrent,
                  int resourceRepair, int resourceRepairCurrent,
                  long resourceGlobalDate, long resourceGlobalPeriod,
                  long resourceRepairDate, long resourceRepairPeriod,
                  int startGlobal, int startGlobalCurrent,
                  int startRepair, int startRepairCurrent,
                  int selGlobal, int selGlobalCurrent,
                  int selRepair, int selRepairCurrent,
                  int genGlobal, int genGlobalCurrent,
                  int genRepair, int genRepairCurrent,
                  int commonGlobal, int commonGlobalCurrent,
                  int commonRepair, int commonRepairCurrent,
                  int landGlobal, int landGlobalCurrent,
                  int landRepair, int landRepairCurrent)
    {
        this.number = number;
        this.name = name;
        this.group = group;
        this.id = id;
        this.typeTemplate = typeTemplate;
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
        // общие запуски
        this.startGlobal = startGlobal;
        this.startGlobalCurrent = startGlobalCurrent;
        this.startGlobalBalance = this.startGlobal - this.startGlobalCurrent;
        // ремонтные запуски
        this.startRepair = startRepair;
        this.startRepairCurrent = startRepairCurrent;
        this.startRepairBalance = this.startRepair - this.startRepairCurrent;
        // общие отборы
        this.selGlobal = selGlobal;
        this.selGlobalCurrent = selGlobalCurrent;
        this.selGlobalBalance = this.selGlobal - this.selGlobalCurrent;
        // ремонтные отборы
        this.selRepair = selRepair;
        this.selRepairCurrent = selRepairCurrent;
        this.selRepairBalance = this.selRepair - this.selRepairCurrent;
        // общее время ген. режима
        this.genGlobal = genGlobal;
        this.genGlobalCurrent = genGlobalCurrent;
        this.genGlobalBalance = this.genGlobal - this.genGlobalCurrent;
        // ремонтное время ген. режима
        this.genRepair = genRepair;
        this.genRepairCurrent = genRepairCurrent;
        this.genRepairBalance = this.genRepair - this.genRepairCurrent;
        // общие время
        this.commonGlobal = commonGlobal;
        this.commonGlobalCurrent = commonGlobalCurrent;
        this.commonGlobalBalance = this.commonGlobal - this.commonGlobalCurrent;
        // ремонтное время
        this.commonRepair = commonRepair;
        this.commonRepairCurrent = commonRepairCurrent;
        this.commonRepairBalance = this.commonRepair - this.commonRepairCurrent;
        // назначенные посадки
        this.landGlobal = landGlobal;
        this.landGlobalCurrent = landGlobalCurrent;
        this.landGlobalBalance = this.landGlobal - this.landGlobalCurrent;
        // межремонтные посадки
        this.landRepair = landRepair;
        this.landRepairCurrent = landRepairCurrent;
        this.landRepairBalance = this.landRepair - this.landRepairCurrent;
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

    public int getTypeTemplate() {
        return typeTemplate;
    }

    public void setTypeTemplate(int typeTemplate) {
        this.typeTemplate = typeTemplate;
    }

    public int getStartGlobal() {
        return startGlobal;
    }

    public void setStartGlobal(int startGlobal) {
        this.startGlobal = startGlobal;
    }

    public int getStartGlobalCurrent() {
        return startGlobalCurrent;
    }

    public void setStartGlobalCurrent(int startGlobalCurrent) {
        this.startGlobalCurrent = startGlobalCurrent;
    }

    public int getStartGlobalBalance() {
        return startGlobalBalance;
    }

    public void setStartGlobalBalance(int startGlobalBalance) {
        this.startGlobalBalance = startGlobalBalance;
    }

    public int getStartRepair() {
        return startRepair;
    }

    public void setStartRepair(int startRepair) {
        this.startRepair = startRepair;
    }

    public int getStartRepairCurrent() {
        return startRepairCurrent;
    }

    public void setStartRepairCurrent(int startRepairCurrent) {
        this.startRepairCurrent = startRepairCurrent;
    }

    public int getStartRepairBalance() {
        return startRepairBalance;
    }

    public void setStartRepairBalance(int startRepairBalance) {
        this.startRepairBalance = startRepairBalance;
    }

    public int getSelGlobal() {
        return selGlobal;
    }

    public void setSelGlobal(int selGlobal) {
        this.selGlobal = selGlobal;
    }

    public int getSelGlobalCurrent() {
        return selGlobalCurrent;
    }

    public void setSelGlobalCurrent(int selGlobalCurrent) {
        this.selGlobalCurrent = selGlobalCurrent;
    }

    public int getSelGlobalBalance() {
        return selGlobalBalance;
    }

    public void setSelGlobalBalance(int selGlobalBalance) {
        this.selGlobalBalance = selGlobalBalance;
    }

    public int getSelRepair() {
        return selRepair;
    }

    public void setSelRepair(int selRepair) {
        this.selRepair = selRepair;
    }

    public int getSelRepairCurrent() {
        return selRepairCurrent;
    }

    public void setSelRepairCurrent(int selRepairCurrent) {
        this.selRepairCurrent = selRepairCurrent;
    }

    public int getSelRepairBalance() {
        return selRepairBalance;
    }

    public void setSelRepairBalance(int selRepairBalance) {
        this.selRepairBalance = selRepairBalance;
    }

    public int getGenGlobal() {
        return genGlobal;
    }

    public void setGenGlobal(int genGlobal) {
        this.genGlobal = genGlobal;
    }

    public int getGenGlobalCurrent() {
        return genGlobalCurrent;
    }

    public void setGenGlobalCurrent(int genGlobalCurrent) {
        this.genGlobalCurrent = genGlobalCurrent;
    }

    public int getGenGlobalBalance() {
        return genGlobalBalance;
    }

    public void setGenGlobalBalance(int genGlobalBalance) {
        this.genGlobalBalance = genGlobalBalance;
    }

    public int getGenRepair() {
        return genRepair;
    }

    public void setGenRepair(int genRepair) {
        this.genRepair = genRepair;
    }

    public int getGenRepairCurrent() {
        return genRepairCurrent;
    }

    public void setGenRepairCurrent(int genRepairCurrent) {
        this.genRepairCurrent = genRepairCurrent;
    }

    public int getGenRepairBalance() {
        return genRepairBalance;
    }

    public void setGenRepairBalance(int genRepairBalance) {
        this.genRepairBalance = genRepairBalance;
    }

    public int getCommonGlobal() {
        return commonGlobal;
    }

    public void setCommonGlobal(int commonGlobal) {
        this.commonGlobal = commonGlobal;
    }

    public int getCommonGlobalCurrent() {
        return commonGlobalCurrent;
    }

    public void setCommonGlobalCurrent(int commonGlobalCurrent) {
        this.commonGlobalCurrent = commonGlobalCurrent;
    }

    public int getCommonGlobalBalance() {
        return commonGlobalBalance;
    }

    public void setCommonGlobalBalance(int commonGlobalBalance) {
        this.commonGlobalBalance = commonGlobalBalance;
    }

    public int getCommonRepair() {
        return commonRepair;
    }

    public void setCommonRepair(int commonRepair) {
        this.commonRepair = commonRepair;
    }

    public int getCommonRepairCurrent() {
        return commonRepairCurrent;
    }

    public void setCommonRepairCurrent(int commonRepairCurrent) {
        this.commonRepairCurrent = commonRepairCurrent;
    }

    public int getCommonRepairBalance() {
        return commonRepairBalance;
    }

    public void setCommonRepairBalance(int commonRepairBalance) {
        this.commonRepairBalance = commonRepairBalance;
    }

    public int getLandGlobal() {
        return landGlobal;
    }

    public void setLandGlobal(int landGlobal) {
        this.landGlobal = landGlobal;
    }

    public int getLandGlobalCurrent() {
        return landGlobalCurrent;
    }

    public void setLandGlobalCurrent(int landGlobalCurrent) {
        this.landGlobalCurrent = landGlobalCurrent;
    }

    public int getLandGlobalBalance() {
        return landGlobalBalance;
    }

    public void setLandGlobalBalance(int landGlobalBalance) {
        this.landGlobalBalance = landGlobalBalance;
    }

    public int getLandRepair() {
        return landRepair;
    }

    public void setLandRepair(int landRepair) {
        this.landRepair = landRepair;
    }

    public int getLandRepairCurrent() {
        return landRepairCurrent;
    }

    public void setLandRepairCurrent(int landRepairCurrent) {
        this.landRepairCurrent = landRepairCurrent;
    }

    public int getLandRepairBalance() {
        return landRepairBalance;
    }

    public void setLandRepairBalance(int landRepairBalance) {
        this.landRepairBalance = landRepairBalance;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", typeTemplate=" + typeTemplate +
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
                ", startGlobal=" + startGlobal +
                ", startGlobalCurrent=" + startGlobalCurrent +
                ", startGlobalBalance=" + startGlobalBalance +
                ", startRepair=" + startRepair +
                ", startRepairCurrent=" + startRepairCurrent +
                ", startRepairBalance=" + startRepairBalance +
                ", selGlobal=" + selGlobal +
                ", selGlobalCurrent=" + selGlobalCurrent +
                ", selGlobalBalance=" + selGlobalBalance +
                ", selRepair=" + selRepair +
                ", selRepairCurrent=" + selRepairCurrent +
                ", selRepairBalance=" + selRepairBalance +
                ", genGlobal=" + genGlobal +
                ", genGlobalCurrent=" + genGlobalCurrent +
                ", genGlobalBalance=" + genGlobalBalance +
                ", genRepair=" + genRepair +
                ", genRepairCurrent=" + genRepairCurrent +
                ", genRepairBalance=" + genRepairBalance +
                ", commonGlobal=" + commonGlobal +
                ", commonGlobalCurrent=" + commonGlobalCurrent +
                ", commonGlobalBalance=" + commonGlobalBalance +
                ", commonRepair=" + commonRepair +
                ", commonRepairCurrent=" + commonRepairCurrent +
                ", commonRepairBalance=" + commonRepairBalance +
                ", landGlobal=" + landGlobal +
                ", landGlobalCurrent=" + landGlobalCurrent +
                ", landGlobalBalance=" + landGlobalBalance +
                ", landRepair=" + landRepair +
                ", landRepairCurrent=" + landRepairCurrent +
                ", landRepairBalance=" + landRepairBalance +
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
//                "\"id\": \"" + id + "\"," +
//                "\"number\": \"" + number + "\"," +
//                "\"name\": \"" + name + "\"," +
//                "\"group\": \"" + group + "\"," +
//                "\"resourceGlobal\": " + resourceGlobal + "," +
//                "\"resourceGlobalCurrent\": " + resourceGlobalCurrent + "," +
//                "\"resourceGlobalBalance\": " + resourceGlobalBalance + "," +
//                "\"resourceRepair\": " + resourceRepair + "," +
//                "\"resourceRepairCurrent\": " + resourceRepairCurrent + "," +
//                "\"resourceRepairBalance\": " + resourceRepairBalance + "," +
//                "\"resourceGlobalDate\": " + resourceGlobalDate + "," +
//                "\"resourceGlobalPeriod\": " + resourceGlobalPeriod + "," +
//                "\"resourceGlobalNext\": " + resourceGlobalNext + "," +
//                "\"resourceRepairDate\": " + resourceRepairDate + "," +
//                "\"resourceRepairPeriod\": " + resourceRepairPeriod + "," +
//                "\"resourceRepairNext\": " + resourceRepairNext +
//                "}";
    }
}
