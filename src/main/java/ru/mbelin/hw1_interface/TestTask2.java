package ru.mbelin.hw1_interface;

import ru.mbelin.hw1_interface.impl.ImplAction;
import ru.mbelin.hw1_interface.impl.ImplRunAndJump;
import ru.mbelin.hw1_interface.model.*;
import ru.mbelin.utils.Utils;

public class TestTask2 {

    public static void main(String[] args) {
        ImplRunAndJump player;
        ImplAction barrier;

        System.out.println("*********************************************************************");
        player = new Cat("Васька", Utils.getRandomInt(2,7), Utils.getRandomInt(10, 100));
        barrier = new TreadMill(100);
        barrier.doAction(player);
        barrier = new Wall(7);
        barrier.doAction(player);

        System.out.println("*********************************************************************");
        player = new Human("Иван Иванов");
        barrier = new TreadMill(Utils.getRandomInt(10, 200));
        barrier.doAction(player);
        barrier = new Wall(Utils.getRandomInt(1, 10));
        barrier.doAction(player);

        System.out.println("*********************************************************************");
        player = new Robot("Terminator T-200");
        barrier = new TreadMill(Utils.getRandomInt(100, 2000));
        barrier.doAction(player);
        barrier = new Wall(Utils.getRandomInt(0, 20));
        barrier.doAction(player);

    }



}
