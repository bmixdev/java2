package ru.mbelin.client;
import ru.mbelin.utils.Utils;
public class MainClient {
    public static void main(String[] args) {
        ConsoleClient consoleClient = new ConsoleClient("usr"+ Utils.getRandomInt(0, 100));
    }
}
