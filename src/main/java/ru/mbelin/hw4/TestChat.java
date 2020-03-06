package ru.mbelin.hw4;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
