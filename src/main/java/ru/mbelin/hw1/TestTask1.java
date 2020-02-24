package ru.mbelin.hw1;

import ru.mbelin.hw1.impl.ImplRunAndJump;
import ru.mbelin.hw1.model.Cat;
import ru.mbelin.hw1.model.Human;
import ru.mbelin.hw1.model.Robot;

public class TestTask1 {

    public static void main(String[] args) {
        ImplRunAndJump player = new Cat("Васька", 10, 400);
        runAndJump(player);

        player = new Human("Иван Петров");
        runAndJump(player);

        player = new Robot("R2D2");
        runAndJump(player);

    }

    public static void runAndJump(ImplRunAndJump player) {
        player.jump(); player.run();
    }
}
