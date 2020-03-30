package ru.mbelin.server.service;

import ru.mbelin.server.postgres.DBHelper;
import ru.mbelin.utils.ComStatePrd;
import ru.mbelin.utils.ConsoleColors;

import javax.jws.soap.SOAPBinding;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BaseAuthService implements AuthService {

    private static final String defaultPassword = "123";
    private ComStatePrd state;
    private List<UserData> userDataList;
    private DBHelper dbHelper;

    public BaseAuthService() {
        this.userDataList = new ArrayList<>();
        this.state = ComStatePrd.READY;
        try {
            this.dbHelper = DBHelper.getHelper();
        } catch (SQLException|ClassNotFoundException e) {
            e.printStackTrace();
        }
        fillUserDataList();
    }

    private void fillUserDataList() {
        try {
            ResultSet rs = dbHelper.getStatement().executeQuery("select * from users");
            while (rs.next()) {
                    UserData userData = new UserData(rs.getString("username"), rs.getString("password"), rs.getString("color"));
                    this.userDataList.add(userData);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserData getUserByLoginAndPassword(String login, String password) throws AuthException {
        checkProcessedService();
        if (login.isEmpty() || password.isEmpty()) return null;
        for (UserData u: userDataList) {
            if (login.equals(u.login) && password.equals(u.password))
                return u;
        }
        throw new RuntimeException("Неверно введены данный login/password!");
    }

    @Override
    public UserData register(String login, String password, String color) throws AuthException {
        checkProcessedService();
        checkEmpty(login.isEmpty(), "Логин пользователя не заполнен!");
        checkEmpty(password.isEmpty(), "Пароль пользователя не заполнен!");
        for (UserData u: userDataList) {
            if (login.equals(u.login)) throw new AuthException("Логин \""+login+"\" уже занят другим пользователем!");
        }
        UserData userData = new UserData(login, password, color);
        try {
            PreparedStatement ps =dbHelper.getConnection().prepareStatement("INSERT INTO users(username, password, uid, color) VALUES(?,?,?,?)");
            ps.setString(1, userData.login);
            ps.setString(2, userData.password);
            ps.setString(3, userData.uid);
            ps.setString(4, userData.color);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userDataList.add(userData);
        return  userData;
    }

    private void checkEmpty(boolean empty, String s) throws AuthException {
        if (empty) throw new AuthException(s);
    }

    private void checkProcessedService() throws AuthException {
        checkEmpty(this.state != ComStatePrd.PROCESSED, "Сервис аутентификации данных не запущен!");
    }

    @Override
    public void start() {
        System.out.print('\t');
        ConsoleColors.print("Сервис аутентификации клиентов запущен", ConsoleColors.PURPLE_UNDERLINED);
        this.state = ComStatePrd.PROCESSED;
    }

    @Override
    public void stop() {
        System.out.print('\t');
        ConsoleColors.print("Сервис аутентификации клиентов остановлен", ConsoleColors.PURPLE_UNDERLINED);
        this.state = ComStatePrd.STOPED;
    }
}
