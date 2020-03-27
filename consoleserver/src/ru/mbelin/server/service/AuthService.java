package ru.mbelin.server.service;

public interface AuthService {
    UserData getUserByLoginAndPassword(String login, String password) throws AuthException;
    UserData register(String login, String password, String color) throws AuthException;
    void start();
    void stop();
}
