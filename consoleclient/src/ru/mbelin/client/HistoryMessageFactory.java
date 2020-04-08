package ru.mbelin.client;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryMessageFactory {

    private static HistoryMessageFactory instance;
    private String uuid;
    private List<String> historyList;

    private HistoryMessageFactory() {
        this.historyList = new ArrayList<>();
        this.uuid =  "1.txt";
    }
    public static HistoryMessageFactory getInstance() {
        if (instance == null) {
            instance = new HistoryMessageFactory();
        }
        return instance;
    }

    public void load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(this.uuid))) {
            String line;
            while ((line = reader.readLine()) != null) {
                historyList.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(String str) {
        this.historyList.add(str);
    }

    public void save(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.uuid))) {
            for (String s: this.historyList) {
                writer.write(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printLastMsg(int cntMsg) {
        for (int i = this.historyList.size(); (this.historyList.size() - i) == cntMsg ; i--) {
            System.out.println(this.historyList.get(i));
        }
    }


}
