package ru.mbelin.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresUtils {

    public static void createDataBase(String dbName) throws ClassNotFoundException, SQLException {
            Connection c=null;
            Statement stmt=null;
            Class.forName("org.postgresql.Driver");

            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/",
                            "postgres", "1");
            System.out.println("Успешно подключен к Postgres");
            stmt = c.createStatement();
            String sql = "CREATE DATABASE "+dbName+" OWNER postgres;";
            stmt.executeUpdate(sql);
            System.out.printf("База <<%s>> успешно создана%n", dbName);
            stmt.close();
            c.close();
    }

}
