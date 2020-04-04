package ru.mbelin.client;

import ru.mbelin.utils.ComStatePrd;
import ru.mbelin.utils.ConstantMessage;
import ru.mbelin.utils.Color;
import ru.mbelin.utils.ConsoleColors;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleClient {
    private final static String SERVER_HOST = "localhost";
    private final static int SERVER_PORT = 8188;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Socket socket;
    private Thread threadRead, threadWrite;
    private ConsoleClient instance;
    private UUID uuid;
    private String user;

    public ConsoleClient(String user) {
        try {
            this.socket = new Socket(SERVER_HOST, SERVER_PORT);
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.threadRead = new Thread(this::read,"TR");
            this.threadWrite = new Thread(this::write,"TW");
            this.threadRead.start(); threadWrite.start();
            this.instance = this;
            this.uuid = UUID.randomUUID();
            this.user = user;
            /*
            sendMsg(String.format("#[%s:%s]#", ConstantMessage.SYSCODE_UUID, this.uuid));
            sendMsg(String.format("#[%s:%s]#", ConstantMessage.SYSCODE_USER, this.user));
            ConsoleColors.print(String.format("<< Успешно подключен к серверу: %s:%s >>", this.SERVER_HOST, this.SERVER_PORT), ConsoleColors.PURPLE_UNDERLINED);
            ConsoleColors.print(String.format("USER: %s\t UUID: %s", this.user, this.uuid), Color.GREEN);
            */

        }
        catch (ConnectException e) {
            ConsoleColors.print(String.format("Сервер %s:%s не доступен!", this.SERVER_HOST, this.SERVER_PORT), Color.RED);
        }
        catch (IOException e) {
            e.printStackTrace();
            exit();
        }
    }

    void waitAndExit(long millis) {

        HistoryMessageFactory.getInstance().save();

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            exit();
        }
    }

    private void read() {
        try {
            while (true) {
                String msgFromServer = inputStream.readUTF();
                if (msgFromServer.toUpperCase().equals(ConstantMessage.CMD_END.toUpperCase())) {
                    instance.threadWrite.interrupt();
                    exit();
                    break;
                }
                if (msgFromServer.toUpperCase().startsWith("#")) {
                    String[] parts = msgFromServer.split("\\s");
                    String command = parts[0];
                    if (command.toUpperCase().equals(ConstantMessage.CMD_AUTH_SUCCESS)) {
                        StringBuilder msgAuthSuccess = new StringBuilder();
                        if (parts.length > 1)
                            for (int i = 1; i < parts.length ; i++) {
                                msgAuthSuccess.append(parts[i]).append("\n");
                            }
                        HistoryMessageFactory.getInstance().load();
                        HistoryMessageFactory.getInstance().printLastMsg(100);
                        System.out.println(msgAuthSuccess);
                        continue;
                    }
                }
                System.out.println(msgFromServer);
                HistoryMessageFactory.getInstance(this.uuid.toString()).add(msgFromServer);
            }
        } catch (Exception e) {
            ConsoleColors.print("Соединение с серверном закрыто: " + e.getMessage() +" \nОкно автоматически закроется через 5 сек.", Color.RED);
            waitAndExit(5000);
        }
    }

    private void write() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String msgToServer = scanner.nextLine();
            sendMsg(msgToServer);
        }
    }

    private void sendMsg(String msg) {
        try {
            outputStream.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exit() {
        ConsoleColors.print(String.format("<< Отключился >>"), ConsoleColors.PURPLE_UNDERLINED);
        System.exit(1);
    }
}
