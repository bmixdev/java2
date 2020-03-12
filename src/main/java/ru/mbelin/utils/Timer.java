package ru.mbelin.utils;

import java.text.DecimalFormat;
import java.util.HashMap;

public class Timer {

    private HashMap<String, Long> map;
    private static Timer instance;

    private Timer() {
        this.map = new HashMap<>();
    }

    public static Timer getInstance() {
        if (instance == null)
            instance = new Timer();
        return instance;
    }

    public void set(String key) {
        if (!map.containsKey(key))
            map.put(key, System.currentTimeMillis());
    }

    public void get(String key) {
        long t;
        if (map.containsKey(key)) {
            t = System.currentTimeMillis() - map.get(key);
            map.remove(key);
            System.out.printf("Выполнение точки <<%s>>: %.4f сек.\n", key, (double) t / 1000);
        }
        else
            System.out.printf("Точка <<%s>> не инициализирована\n", 0);
    }

}
