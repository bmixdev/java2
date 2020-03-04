package ru.mbelin.hw3;

import jdk.internal.dynalink.linker.LinkerServices;

import java.util.*;

public class Test1 {

    public static void main(String[] args) {
        System.out.println("Набор слов:");
        List<String> arr = new ArrayList<String>();
        arr.add("Word1");
        arr.add("Word2");
        arr.add("Word3");
        arr.add("Word4");
        arr.add("Word1");
        arr.add("Word5");
        arr.add("Word2");
        arr.add("Word6");
        arr.add("Word3");
        arr.add("Word3");
        arr.add("Word8");
        arr.add("Word9");
        arr.add("Word10");
        arr.add("Word11");
        arr.add("Word10");
        arr.add("Word12");
        arr.add("Word15");
        arr.add("Word14");
        arr.add("Word15");
        System.out.println(arr);
        System.out.println("Список уникальных слов:");
        LinkedHashSet<String> arrUniq = new LinkedHashSet<String>(arr);
        System.out.println(arrUniq);

        System.out.println("Сколько раз встречается слово:");
        int cnt = 0;
        LinkedHashMap<String, Integer> hm = new LinkedHashMap<String, Integer>();
        for (String s: arr) {
            if (hm.containsKey(s)) cnt = hm.get(s);
            else cnt = 0;
            cnt++;
            hm.put(s, cnt);
        }
        System.out.println(hm);

    }
}
