package ru.mbelin.hw6_client_server.client;

import ru.mbelin.utils.Utils;

import java.util.Scanner;

public class MainClient {
    public static void main(String[] args) {
        ConsoleClient consoleClient = new ConsoleClient("user_"+ Utils.getRandomInt(0, 100));
    }
}
