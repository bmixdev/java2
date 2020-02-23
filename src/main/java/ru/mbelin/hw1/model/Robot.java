package ru.mbelin.hw1.model;

import ru.mbelin.hw1.impl.ImplRunAndJump;
import ru.mbelin.utils.Utils;

import java.util.Objects;

public class Robot implements ImplRunAndJump {

    private String codeName;
    private int maxJump;
    private int maxRun;

    public Robot(String codeName) {
        this.codeName = codeName;
        this.maxJump = Utils.getRandomInt(2, 20);
        this.maxRun = Utils.getRandomInt(500, 1500);
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Robot robot = (Robot) o;
        return Objects.equals(codeName, robot.codeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeName);
    }

    public void info() {
        System.out.println("Робот " + this.codeName + " (макс. бег: " + this.maxRun + ", макс. прыжок: " + this.maxJump + ")");
    }

    @Override
    public String toString() {
        return "Робот " + this.codeName;
    }

    public int run() {
        System.out.println(this + " бежит");
        return this.maxRun;
    }

    public int jump() {
        System.out.println(this + " прыгает");
        return this.maxJump;
    }
}
