package ru.mbelin.hw6_client_server.client;

import ru.mbelin.hw6_client_server.ConstantMessage;
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
            sendMsg(String.format("#[%s:%s]#", ConstantMessage.SYSCODE_UUID, this.uuid));
            sendMsg(String.format("#[%s:%s]#", ConstantMessage.SYSCODE_USER, this.user));
            ConsoleColors.print(String.format("<< Успешно подключен к серверу: %s:%s >>", this.SERVER_HOST, this.SERVER_PORT), ConsoleColors.PURPLE_UNDERLINED);
            ConsoleColors.print(String.format("USER: %s\t UUID: %s", this.user, this.uuid), Color.GREEN);
        }
        catch (ConnectException e) {
            ConsoleColors.print(String.format("Сервер %s:%s не доступен!", this.SERVER_HOST, this.SERVER_PORT), Color.RED);
        }
        catch (IOException e) {
            e.printStackTrace();
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
                System.out.println("[SERVER]->: " + msgFromServer);
            }
        } catch (Exception e) {
            ConsoleColors.print("Соединение с серверном закрыто: " + e.getMessage(), Color.RED);
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
