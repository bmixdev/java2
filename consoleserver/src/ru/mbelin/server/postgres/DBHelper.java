package ru.mbelin.server.postgres;

import ru.mbelin.utils.ConsoleColors;
import sun.security.pkcs11.Secmod;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {
    private Connection connection;
    private Statement statement;
    private static DBHelper instance;

    private static final String url = "jdbc:postgresql://localhost:5432/chat";
    private static final String user = "postgres";
    private static final String password = "1";

    public DBHelper () throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        ConsoleColors.print("<<Драйвер Postgres подключен>>", ConsoleColors.PURPLE_UNDERLINED);
        //URL к базе состоит из протокола:подпротокола://[хоста]:[порта_СУБД]/[БД] и других_сведений
        connection = DriverManager.getConnection(url, user, password);
        ConsoleColors.print("<<Соединение с базой установлено>>", ConsoleColors.PURPLE_UNDERLINED);
        statement = connection.createStatement();
    }

    public static DBHelper getHelper() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new DBHelper();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void close() {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void executeFromFile(String file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();
            while(line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            String fileAsString = sb.toString();
            statement.execute(fileAsString);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

}
