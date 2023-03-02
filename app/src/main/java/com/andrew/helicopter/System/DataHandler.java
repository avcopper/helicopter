package com.andrew.helicopter.System;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import android.util.Log;

public class DataHandler {
    /**
     * Массив месяцев для вывода на странице работ
     */
    private static final HashMap<String, String> MONTHS = new HashMap<String, String>() {{
        put("Январь","01");
        put("Февраль","02");
        put("Март","03");
        put("Апрель","04");
        put("Май","05");
        put("Июнь","06");
        put("Июль","07");
        put("Август","08");
        put("Сентябрь","09");
        put("Октябрь","10");
        put("Ноябрь","11");
        put("Декабрь","12");
    }};

    /**
     * Возвращает номер выбранного месяца
     * @param index - индекс выбранного месяца
     * @return
     */
    public static String getMonth(String index) {
        return MONTHS.get(index);
    }

    /**
     * Возвращает случайно число в диапазоне
     * @param min - минимальное число
     * @param max - максимальное число
     * @return
     */
    public static int getRandomIntegerBetweenRange(int min, int max){
        return (int)(Math.random() * ((max - min) + 1)) + min;
    }

    /**
     * Возвращает время в формате часы:минуты (HH:mm) из минут
     * @param time - время в минутах
     * @return
     */
    public static String simpleFormatDateFromMinutes(int time) {
        int minutesInHour = 60;
        return ((time / minutesInHour) + ":" + Math.abs(time % minutesInHour));
    }

    /**
     * Вовзращает минуты из времени в формате часы:минуты (HH:mm)
     * @param time - время в формате часы:минуты (HH:mm)
     * @return
     */
    public static int getMinutesFromTimeFormat(String time) {
        time = time.trim();
        String[] parts = time.split(":");
        int part1 = Integer.parseInt(parts[0]);
        int part2 = isValidIndex(parts, 1) ? Integer.parseInt(parts[1]) : 0;

        return part1 * 60 + part2;
    }

    /**
     * Возвращает дату в формате день.месяц.год (dd.MM.yyyy) из метки времени Unix в секундах
     * @param time - метка времени Unix в секундах
     * @return
     */
    public static String getDateFromUnixTimestamp(long time) {
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);
        return formatter.format(calendar.getTime());
    }

    /**
     * Возвращает метку времени Unix в секундах их даты в формате день.месяц.год (dd.MM.yyyy)
     * @param date - дата в формате день.месяц.год (dd.MM.yyyy)
     * @return
     */
    public static long getUnixTimestampFromDate(String date) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH);
        try {
            calendar.setTime(sdf.parse(date + " 12:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * Возвращает количество месяцев из секунд
     * @param time - время в секундах
     * @return
     */
    public static String getMonthsFromUnixTimestamp(long time) {
        return String.valueOf(Math.round(time / 2617920.));
    }

    /**
     * Возвращает количество лет из секунд
     * @param time - время в секундах
     * @return
     */
    public static String getYearsFromUnixTimestamp(long time) {
        return String.valueOf(Math.round(time / 31536000.));
    }

    /**
     * Возвращает время в секундах из количества месяцев
     * @param month - количество месяцев
     * @return
     */
    public static long getUnixTimestampFromMoths(int month) {
        return month * 2617920L;
    }

    /**
     * Возвращает время в секундах из количества лет
     * @param years - количество лет
     * @return
     */
    public static long getUnixTimestampFromYears(double years) {
        return (long) (years * 31536000);
    }

    /**
     * Возвращает текущую метку времени Unix в секундах
     * @return
     */
    public static long getCurrentUnixTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * Проверяет существует ли индекс в массиве
     * @param arr - массив
     * @param index - индекс
     * @return
     */
    static boolean isValidIndex(String[] arr, int index) {
        return index >= 0 && index < arr.length;
    }

    /**
     * Проверяет строку на соответствие формату времени часы:минуты (12:34)
     * @param data - строка времени
     * @return
     */
    public static boolean isValidTime(String data) {
        return data.matches("^\\d{1,8}[:]?\\d{0,2}$");
    }

    /**
     * Проверяет значения из массива на соответствие формату времени часы:минуты (12:34)
     * @param data - массив строк времени
     * @return
     */
    public static boolean isValidTime(String[] data) {
        for (String item : data)
            if (!isValidTime(item)) return false;

        return true;
    }

    /**
     * Проверяет строку на соответствие формату даты день.месяц.год (15.01.2023)
     * @param data - строка даты
     * @return
     */
    public static boolean isValidDate(String data) {
        return data.matches("^\\d{2}.\\d{2}.\\d{4}$");
    }

    /**
     * Проверяет значения из массива на соответствие формату даты день.месяц.год (15.01.2023)
     * @param data - массив строк даты
     * @return
     */
    public static boolean isValidDate(String[] data) {
        for (String item : data)
            if (!isValidDate(item)) return false;

        return true;
    }

    /**
     * Проверяет строку на соответствие формату числа
     * @param data - строка с числом
     * @return
     */
    public static boolean isValidNumber(String data) {
        return data.matches("^\\d+(\\.\\d+)?$");
    }

    /**
     * Проверяет значения из массива на соответствие формату числа
     * @param data - массив строк с числом
     * @return
     */
    public static boolean isValidNumber(String[] data) {
        for (String item : data)
            if (!isValidNumber(item)) return false;

        return true;
    }

    /**
     * Проверяет строку на соответствие формату email
     * @param data - строка с email
     * @return
     */
    public static boolean isValidEmail(String data) {
        return data.matches("^([a-z0-9_\\-]+\\.)*[a-z0-9_\\-]+@([a-z0-9][a-z0-9\\-]*[a-z0-9]\\.)+[a-z]{2,6}$");
    }
}
