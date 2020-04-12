package ru.mbelin.hw1_interface;

import ru.mbelin.hw1_interface.impl.ImplAction;
import ru.mbelin.hw1_interface.impl.ImplRunAndJump;
import ru.mbelin.hw1_interface.model.*;
import ru.mbelin.utils.ConsoleColors;
import ru.mbelin.utils.Utils;

public class TestTask34 {


    public static void main(String[] args) {
        testTask3();
        testTask4();
    }

    // заполнить препятствия
    private static ImplAction[] fillBarriers(int count) {
        ImplAction[] barriers = new ImplAction[count];
        for (int i = 0; i < barriers.length; i++) {
            int typeBarrier = Utils.getRandomInt(1, 2);
            switch (typeBarrier) {
                case 1 :
                    barriers[i] = new TreadMill(Utils.getRandomInt(1, 500));
                    break;
                case 2 :
                    barriers[i] = new Wall(Utils.getRandomInt(1, 20));
                    break;
            }
        }
        return barriers;
    }

    // заполнить участников
    private static ImplRunAndJump[] fillPlayers(int count) {
        ImplRunAndJump[] players = new ImplRunAndJump[count];
        for (int i = 0; i < players.length ; i++) {
            int playerType = Utils.getRandomInt(1, 3);
            int randomNum = Utils.getRandomInt(1, 1000);
            if (playerType == 1)
                players[i] = new Cat("№ " + randomNum, Utils.getRandomInt(0, 100), Utils.getRandomInt(0, 1000));
            else if (playerType == 2)
                players[i] = new Human("Человек № " + randomNum);
            else
                players[i] = new Robot("№ " + randomNum);
        }
        return players;
    }

    private static void testTask3() {
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "############################## Проверка задачи 3 ##############################" + ConsoleColors.RESET);
        // заполнить препятствия
        ImplAction[] barriers = fillBarriers(4);
        // заполнить участников
        ImplRunAndJump[] players = fillPlayers(4);

        for (ImplRunAndJump player : players) {
            System.out.println(ConsoleColors.GREEN + "#####################################################################");
            System.out.print("Характеристики участника: " + ConsoleColors.RED_BOLD); player.info();
            System.out.print(ConsoleColors.RESET);
            for (ImplAction barrier: barriers) {
                barrier.doAction(player);
            }
        }
    }

    private static void testTask4() {
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "############################## Проверка задачи 4 ##############################" + ConsoleColors.RESET);
        // заполнить препятствия
        ImplAction[] barriers = fillBarriers(5);
        // заполнить участников
        ImplRunAndJump[] players = fillPlayers(10);

        for (ImplRunAndJump player : players) {
            System.out.println(ConsoleColors.GREEN + "#####################################################################");
            System.out.print("Характеристики участника: " + ConsoleColors.RED_BOLD); player.info();
            System.out.print(ConsoleColors.RESET);
            int cntSuccess = 0;
            int idx = 0;
            for (ImplAction barrier: barriers) {
                idx++;
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Испытание № "+ (idx) + ConsoleColors.RESET);
                if (barrier.doAction(player))
                    cntSuccess++;
                else
                    break;
            }
            if (cntSuccess == barriers.length)
                System.out.println(ConsoleColors.GREEN_BOLD + player + " справился со всеми препятствиями!!!" + ConsoleColors.RESET);
            else
                System.out.println(ConsoleColors.RED + player + " прошел " + cntSuccess + " из " + barriers.length + " препятствий" + ConsoleColors.RESET);
        }
    }

}
