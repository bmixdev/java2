package ru.mbelin.server.postgres;

import java.sql.SQLException;

public class DBUpdater {

    public static void update() {
        try {
            DBHelper dbHelper = DBHelper.getHelper();
            dbHelper.executeFromFile("consoleserver/src/ru/mbelin/server/postgres/UpdateScript.sql");
        } catch (SQLException|ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        update();
    }
}
