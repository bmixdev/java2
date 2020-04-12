package ru.mbelin.hw4_swing;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TestChat {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(()->{
            NetChatForm netChatForm = new NetChatForm();
            List<String> list = new ArrayList<>();
            list.add("Mike"); list.add("Ivan"); list.add("Anna");
            netChatForm.loadListContacts(list);
        });

        System.out.println("exit");

       /*
        //https://javarush.ru/groups/posts/regulyarnye-vyrazheniya-v-java
        String s = "@Mike: Привет";
        Pattern pattern = Pattern.compile("@\\w+?:");
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            System.out.println(s.substring(matcher.start(), matcher.end()));
            System.out.println(s.substring(matcher.end(), s.length()));
        }
        */
    }
}
