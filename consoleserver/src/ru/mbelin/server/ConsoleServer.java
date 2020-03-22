package ru.mbelin.server;

import ru.mbelin.server.service.AuthException;
import ru.mbelin.server.service.BaseAuthService;
import ru.mbelin.server.service.UserData;
import ru.mbelin.utils.ConstantMessage;
import ru.mbelin.utils.ConsoleColors;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleServer {

    private static ConsoleServer instance;
    private int port;
    private ServerSocket serverSocket;
    private LinkedList<ClientSocketThread> sockets;
    private Thread socketListener;
    private Thread inputThread;
    private IClientSocketAction socketAction;
    private BaseAuthService authService;


    private ConsoleServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.sockets = new LinkedList<>();
        this.socketListener = getSocketListener();
        this.authService = new BaseAuthService();
        ConsoleColors.print("<< Сервер запущен <<"+ InetAddress.getLocalHost().getHostAddress() + ":" + port+">> >>", ConsoleColors.CYAN_UNDERLINED);
        this.socketListener.start();
        this.inputThread = new Thread(this::doInput);
        this.inputThread.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.authService.start();
        printHelpServerCommand();

        socketAction = new IClientSocketAction() {
            @Override
            public void subscribe(ClientSocketThread socket) {
                sockets.add(socket);
            }

            @Override
            public void unsubscribe(ClientSocketThread socket) {
                sockets.remove(socket);
            }

            @Override
            public void sendMessageAllClients(ClientSocketThread socket, String message) {
                sendToAll(String.format("[%s]: %s", socket.getUser(), message));
            }

            @Override
            public void sendMessageToClient(ClientSocketThread socket, String user, String message) {
                sendToUser(user, String.format("[%s] -> [%s]: %s", socket.getUser(), user, message));
            }

            @Override
            public boolean authentication(ClientSocketThread socket, String command) {
                try {
                    String[] parts = command.split("\\s");
                    if (parts.length < 3 || !parts[0].substring(0, 1).equals("#")) throw new AuthException("Не верная комманда аутентификации!\nДопустимые значения #reg или #auth!");
                    executeCommand(socket, command);
                    System.out.println(ConsoleServer.successMsg("Аутентификация на сервере одобрена"));
                    printHelpClientCommand(socket);
                    sendToAllFromServer( ConsoleColors.get("\tПодключился новый клиент: " + socket.getUser(), ConsoleColors.BLUE));
                } catch (RuntimeException | AuthException e) {
                    System.err.print(e.getMessage());
                    sendForward(socket, ConsoleServer.errorMsg(e.getMessage()));
                    return false;
                }
                return true;
            }

            @Override
            public void executeCommand(ClientSocketThread socket, String command) {
                String[] parts = command.split("\\s");
                if (parts.length > 0 && parts[0].startsWith("#")) {
                    String firstCmd = parts[0].toUpperCase();
                    if (ConstantMessage.CMD_END.toUpperCase().equals(firstCmd)) {
                        System.out.printf("\tКлиент <<%s>> отключился%n", socket.getUser() != null ? socket.getUser() : socket.toString());
                        socket.close();
                    }
                    else if (ConstantMessage.CMD_USERLIST.toUpperCase().equals(firstCmd)) {
                        cmdGetUserList(TypePrint.BUFFER, socket);
                    }
                    else if (ConstantMessage.CMD_HELP.toUpperCase().equals(firstCmd)) {
                       printHelpClientCommand(socket);
                    }
                    else if (ConstantMessage.CMD_PRINT_FIRST.toUpperCase().endsWith(firstCmd)) {
                        printFirstHelpClientCommand(socket);
                    }
                    else if (parts.length >= 3) {
                        if (ConstantMessage.CMD_TO_USER.toUpperCase().equals(firstCmd)) {
                            sendMessageToClient(socket, parts[1], parts[2]);
                        } else if (ConstantMessage.CMD_AUTH.toUpperCase().equals(firstCmd)) {
                            try {
                                UserData userData = authService.getUserByLoginAndPassword(parts[1], parts[2]);
                                socket.setUserDate(userData);
                                sendForward(socket, successMsg("пользователь "+parts[1]+" аутентифицирован успешно!"));
                            } catch (AuthException e) {
                                throw new RuntimeException(e.getMessage());
                            }
                        } else if (ConstantMessage.CMD_REGISTER.toUpperCase().equals(firstCmd)) {
                            try {
                                UserData userData = authService.register(parts[1], parts[2], (parts.length == 4 ? parts[3] : null));
                                socket.setUserDate(userData);
                                sendForward(socket, successMsg("пользователь "+parts[1]+" зарегистрирован успешно!"));
                            } catch (AuthException e) {
                                throw new RuntimeException(e.getMessage());
                            }
                        }
                    }
                    else {
                        sendForward(socket, errorMsg("Неизвестная команда <<"+parts[0]+">>!"));
                    }
                }
            }

            @Override
            public void sendForward(ClientSocketThread socket, String message) {
                sendToSocket(socket, message);
            }
        };

    }

    public static String errorMsg(String msg) {
        return ConsoleColors.get(msg, ConsoleColors.RED_BOLD);
    }

   public static String successMsg(String msg) {
        return ConsoleColors.get(msg, ConsoleColors.GREEN_BOLD);
    }

    // Слушатель новых клиентов
    private Thread getSocketListener() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                Socket clientSocket;
                System.out.print('\t'); ConsoleColors.print("Ожидаю новых подключений:", ConsoleColors.PURPLE_UNDERLINED);
                while (true) {
                    try {
                        if (!socketListener.isInterrupted()) {

                            if (serverSocket == null)
                                throw new NullPointerException("Сервер не инициализирован!");

                            if (!serverSocket.isClosed()) {
                                clientSocket = serverSocket.accept();
                                ClientSocketThread newClient = new ClientSocketThread(clientSocket, socketAction);
                                /*
                                ConsoleColors.print("\tПодключился новый клиент: " + clientSocket.toString(), Color.BLUE);
                                // переделать на ожидание аутентификации нового клиента
                                while (newClient.getUuid() == null ) {
                                    Thread.sleep(10);
                                }
                                printHelpClientCommand(newClient);
                                sendToAllFromServer( ConsoleColors.get("\tПодключился новый клиент: " + newClient.getUser(), ConsoleColors.BLUE));
                                 */
                            }

                        } else
                            throw new InterruptedException();
                    }
                    catch (SocketException ignore) {}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
    }

    public static ConsoleServer getServer(int port) throws IOException {
        if (instance ==  null) {
            instance = new ConsoleServer(port);
        } else
        {
            if (instance.port != port)
                throw new RuntimeException("ConsoleServer уже запущен на порте" + instance.port);
        }
        return instance;
    }

    private void printHelpServerCommand() {
        ConsoleColors.print("\t\t******************************************************************************************", ConsoleColors.YELLOW_BOLD);
        System.out.print("\t\t"); ConsoleColors.print("Список допустимых команд сервера:", ConsoleColors.GREEN_UNDERLINED);
        ConsoleColors.print("\t\t******************************************************************************************", ConsoleColors.YELLOW_BOLD);
        ConsoleColors.print("\t\t\t[СООБЩЕНИЕ] - будет отправлено произвольное сообщение всем клиентам", ConsoleColors.YELLOW_BOLD);
        ConsoleColors.print("\t\t\t#users - вывести список клиентов", ConsoleColors.YELLOW_BOLD);
        ConsoleColors.print("\t\t\t#kill [ПОЛЬЗОВАТЕЛЬ(USER)] - завершить сессию клиента", ConsoleColors.YELLOW_BOLD);
        ConsoleColors.print("\t\t\t#to [ПОЛЬЗОВАТЕЛЬ(USER)] [СООБЩЕНИЕ] - отправить сообщение конкретному пользователю", ConsoleColors.YELLOW_BOLD);
        ConsoleColors.print("\t\t\t#end - завершить работу сервера", ConsoleColors.YELLOW_BOLD);
        ConsoleColors.print("\t\t******************************************************************************************", ConsoleColors.YELLOW_BOLD);
    }

    private void printFirstHelpClientCommand(ClientSocketThread socket) {
        StringBuilder sb = new StringBuilder();
        sb.append(ConsoleColors.get("*****************************************************************************************************************\n", ConsoleColors.YELLOW_BOLD))
                .append(ConsoleColors.get("Для подключения к серверу <<" + this.serverSocket.getInetAddress() + ":" + this.port + ">> необходимо авторизироваться или зарегистрироваться:\n", ConsoleColors.GREEN_UNDERLINED))
                .append(ConsoleColors.get("****************************************************************************************************************\n", ConsoleColors.YELLOW_BOLD))
                .append(ConsoleColors.get("\tЗарегистрироваться на сервере:\n", ConsoleColors.GREEN_UNDERLINED))
                .append(ConsoleColors.get("\t\t#reg [LOGIN] [PASSWORD] [COLOR]\n", ConsoleColors.YELLOW_BOLD))
                .append(ConsoleColors.get("\tАвторизироваться на сервере:\n", ConsoleColors.GREEN_UNDERLINED))
                .append(ConsoleColors.get("\t\t#auth [LOGIN] [PASSWORD] \n", ConsoleColors.YELLOW_BOLD))
                .append(ConsoleColors.get("*****************************************************************************************************************\n", ConsoleColors.YELLOW_BOLD));
        sendToSocket(socket, sb.toString());
    }

    private void printHelpClientCommand(ClientSocketThread socket) {
        StringBuilder sb = new StringBuilder();
        sb.append(ConsoleColors.get("******************************************************************************************\n", ConsoleColors.YELLOW_BOLD))
                .append(ConsoleColors.get("Список допустимых команд клиента:\n", ConsoleColors.GREEN_UNDERLINED))
                .append(ConsoleColors.get("******************************************************************************************\n", ConsoleColors.YELLOW_BOLD))
                .append(ConsoleColors.get("\t[СООБЩЕНИЕ] - будет отправлено произвольное сообщение всем клиентам\n", ConsoleColors.YELLOW_BOLD))
                .append(ConsoleColors.get("\t#help - вывести помощь\n", ConsoleColors.YELLOW_BOLD))
                .append(ConsoleColors.get("\t#users - вывести список клиентов\n", ConsoleColors.YELLOW_BOLD))
                .append(ConsoleColors.get("\t#to [ПОЛЬЗОВАТЕЛЬ(USER)] [СООБЩЕНИЕ] - отправить сообщение конкретному пользователю\n", ConsoleColors.YELLOW_BOLD))
                .append(ConsoleColors.get("\t#end - завершить работу\n", ConsoleColors.YELLOW_BOLD))
                .append(ConsoleColors.get("******************************************************************************************\n", ConsoleColors.YELLOW_BOLD));
        sendToSocket(socket, sb.toString());
    }

    private boolean executeSystemCommand(String msg, Boolean needBreak) {
        if (msg.startsWith("#")) {
            Pattern pattern = Pattern.compile("^(.+?)( |$)");
            Matcher matcher = pattern.matcher(msg);
            if (matcher.find()) {
                String paramCode = matcher.group(1);
                //ConsoleColors.print(matcher.group(1).toString(), ConsoleColors.GREEN_BOLD_BRIGHT);
                //ConsoleColors.print(matcher.group(2).toString(),ConsoleColors.GREEN);
                switch (paramCode.toUpperCase()) {
                    case ConstantMessage.CMD_END: {
                        ConsoleColors.print("\tВыход из консольного ввода", ConsoleColors.PURPLE_UNDERLINED);
                        if (this.socketListener.isAlive())
                            this.socketListener.interrupt();
                        while (!this.socketListener.isInterrupted()) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        close();
                        needBreak = true;
                        break;
                    }
                    case ConstantMessage.CMD_USERLIST: {
                        cmdGetUserList(TypePrint.CONSOLE, null);
                        break;
                    }
                    case ConstantMessage.CMD_KILL: {
                        pattern = Pattern.compile("^#(.+?) (.+?)$");
                        matcher = pattern.matcher(msg);
                        if (matcher.find()) {
                            killUser(matcher.group(2));
                        }
                        break;
                    }
                    case ConstantMessage.CMD_TO_USER: {
                        pattern = Pattern.compile("^#(.+?) (.+?) (.+?)$");
                        matcher = pattern.matcher(msg);
                        if (matcher.find()) {
                            sendToUserFromServer(matcher.group(2), matcher.group(3));
                        }
                        break;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void cmdGetUserList(TypePrint typePrint, ClientSocketThread socketThread) {
        StringBuilder sb = new StringBuilder();
        sb.append(ConsoleColors.PURPLE_UNDERLINED).append("Список подключенных пользователей").append(ConsoleColors.RESET).append('\n');
        sockets.forEach((t) -> sb.append(t).append('\n'));
        switch (typePrint) {
            case BUFFER:
                sendToSocket(socketThread, sb.toString());
                break;
            case CONSOLE:
                System.out.print(sb);
                break;
        }
    }


    private void killUser(String user) {
        ClientSocketThread socketThread = null;
        for (ClientSocketThread s: sockets) {
            if (s.getUser().toUpperCase().equals(user.toUpperCase())) {
                socketThread = s;
                sendToSocket(s, ConstantMessage.CMD_END);
                break;
            }
        }
        if (socketThread != null)
            sockets.remove(socketThread);
    }


    private void sendToUser(String user, String msg) {
        for (ClientSocketThread s: sockets) {
            if (s.getUser().toUpperCase().equals(user.toUpperCase())) {
                sendToSocket(s, msg);
                break;
            }
        }
    }

    private void doInput() {
        System.out.print('\t'); ConsoleColors.print("Консоль ответа для клиентов готова:" , ConsoleColors.PURPLE_UNDERLINED);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = scanner.nextLine();
            checkSockets();
            Boolean needBreak = false;
            // если системная команда - не показываем пользователям
            if (executeSystemCommand(str, needBreak)) { if (needBreak) break; else continue; };
            //отправить сообщения всем пользователям
            sendToAllFromServer(str);
        }
    }

    private void sendToAllFromServer(String message) {
        sendToAll(String.format("[SERVER]: %s", message));
    }

    private void sendToUserFromServer(String user, String message) {
        sendToUser(user, String.format("[SERVER]: %s", message));
    }


    private void checkSockets() {
        LinkedList<ClientSocketThread> tmp = new LinkedList<>();
        for (ClientSocketThread s: sockets) {
            if (s != null) tmp.add(s);
        }
        sockets = tmp;
    }

    private void sendToAll(String str) {
        checkSockets();
        for (ClientSocketThread s: sockets) {
            sendToSocket(s, str);
        }
    }

    private synchronized void sendToSocket(ClientSocketThread socket, String msg) {
        try {
            if (!msg.trim().isEmpty())
                socket.sendMessage(msg);
        } catch (SocketException e)
        {
            if ( e.toString().indexOf("reset") > 0 )
                sockets.remove(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<ClientSocketThread> getSockets() {
        return sockets;
    }

    public synchronized void close() {
        for (ClientSocketThread socket: sockets) {
            socket.close();
        }
        try {
            serverSocket.close();
            serverSocket = null;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            ConsoleColors.print("<< Сервер остановлен >>" , ConsoleColors.CYAN_UNDERLINED);
            System.exit(1);
        }
    }

}
